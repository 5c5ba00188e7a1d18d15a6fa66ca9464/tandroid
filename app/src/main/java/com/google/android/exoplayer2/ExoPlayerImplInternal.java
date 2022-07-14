package com.google.android.exoplayer2;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Pair;
import com.google.android.exoplayer2.DefaultMediaClock;
import com.google.android.exoplayer2.PlayerMessage;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.HandlerWrapper;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes3.dex */
public final class ExoPlayerImplInternal implements Handler.Callback, MediaPeriod.Callback, TrackSelector.InvalidationListener, MediaSource.MediaSourceCaller, DefaultMediaClock.PlaybackParameterListener, PlayerMessage.Sender {
    private static final int ACTIVE_INTERVAL_MS = 10;
    private static final int IDLE_INTERVAL_MS = 1000;
    private static final int MSG_DO_SOME_WORK = 2;
    private static final int MSG_PERIOD_PREPARED = 9;
    public static final int MSG_PLAYBACK_INFO_CHANGED = 0;
    public static final int MSG_PLAYBACK_PARAMETERS_CHANGED = 1;
    private static final int MSG_PLAYBACK_PARAMETERS_CHANGED_INTERNAL = 17;
    private static final int MSG_PREPARE = 0;
    private static final int MSG_REFRESH_SOURCE_INFO = 8;
    private static final int MSG_RELEASE = 7;
    private static final int MSG_SEEK_TO = 3;
    private static final int MSG_SEND_MESSAGE = 15;
    private static final int MSG_SEND_MESSAGE_TO_TARGET_THREAD = 16;
    private static final int MSG_SET_FOREGROUND_MODE = 14;
    private static final int MSG_SET_PLAYBACK_PARAMETERS = 4;
    private static final int MSG_SET_PLAY_WHEN_READY = 1;
    private static final int MSG_SET_REPEAT_MODE = 12;
    private static final int MSG_SET_SEEK_PARAMETERS = 5;
    private static final int MSG_SET_SHUFFLE_ENABLED = 13;
    private static final int MSG_SOURCE_CONTINUE_LOADING_REQUESTED = 10;
    private static final int MSG_STOP = 6;
    private static final int MSG_TRACK_SELECTION_INVALIDATED = 11;
    private static final String TAG = "ExoPlayerImplInternal";
    private final long backBufferDurationUs;
    private final BandwidthMeter bandwidthMeter;
    private final Clock clock;
    private boolean deliverPendingMessageAtStartPositionRequired;
    private final TrackSelectorResult emptyTrackSelectorResult;
    private Renderer[] enabledRenderers;
    private final Handler eventHandler;
    private boolean foregroundMode;
    private final HandlerWrapper handler;
    private final HandlerThread internalPlaybackThread;
    private final LoadControl loadControl;
    private final DefaultMediaClock mediaClock;
    private MediaSource mediaSource;
    private int nextPendingMessageIndexHint;
    private SeekPosition pendingInitialSeekPosition;
    private final ArrayList<PendingMessageInfo> pendingMessages;
    private int pendingPrepareCount;
    private final Timeline.Period period;
    private boolean playWhenReady;
    private PlaybackInfo playbackInfo;
    private boolean rebuffering;
    private boolean released;
    private final RendererCapabilities[] rendererCapabilities;
    private long rendererPositionUs;
    private final Renderer[] renderers;
    private int repeatMode;
    private final boolean retainBackBufferFromKeyframe;
    private boolean shouldContinueLoading;
    private boolean shuffleModeEnabled;
    private final TrackSelector trackSelector;
    private final Timeline.Window window;
    private final MediaPeriodQueue queue = new MediaPeriodQueue();
    private SeekParameters seekParameters = SeekParameters.DEFAULT;
    private final PlaybackInfoUpdate playbackInfoUpdate = new PlaybackInfoUpdate();

    public ExoPlayerImplInternal(Renderer[] renderers, TrackSelector trackSelector, TrackSelectorResult emptyTrackSelectorResult, LoadControl loadControl, BandwidthMeter bandwidthMeter, boolean playWhenReady, int repeatMode, boolean shuffleModeEnabled, Handler eventHandler, Clock clock) {
        this.renderers = renderers;
        this.trackSelector = trackSelector;
        this.emptyTrackSelectorResult = emptyTrackSelectorResult;
        this.loadControl = loadControl;
        this.bandwidthMeter = bandwidthMeter;
        this.playWhenReady = playWhenReady;
        this.repeatMode = repeatMode;
        this.shuffleModeEnabled = shuffleModeEnabled;
        this.eventHandler = eventHandler;
        this.clock = clock;
        this.backBufferDurationUs = loadControl.getBackBufferDurationUs();
        this.retainBackBufferFromKeyframe = loadControl.retainBackBufferFromKeyframe();
        this.playbackInfo = PlaybackInfo.createDummy(C.TIME_UNSET, emptyTrackSelectorResult);
        this.rendererCapabilities = new RendererCapabilities[renderers.length];
        for (int i = 0; i < renderers.length; i++) {
            renderers[i].setIndex(i);
            this.rendererCapabilities[i] = renderers[i].getCapabilities();
        }
        this.mediaClock = new DefaultMediaClock(this, clock);
        this.pendingMessages = new ArrayList<>();
        this.enabledRenderers = new Renderer[0];
        this.window = new Timeline.Window();
        this.period = new Timeline.Period();
        trackSelector.init(this, bandwidthMeter);
        HandlerThread handlerThread = new HandlerThread("ExoPlayerImplInternal:Handler", -16);
        this.internalPlaybackThread = handlerThread;
        handlerThread.start();
        this.handler = clock.createHandler(handlerThread.getLooper(), this);
        this.deliverPendingMessageAtStartPositionRequired = true;
    }

    public void prepare(MediaSource mediaSource, boolean resetPosition, boolean resetState) {
        this.handler.obtainMessage(0, resetPosition ? 1 : 0, resetState ? 1 : 0, mediaSource).sendToTarget();
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        this.handler.obtainMessage(1, playWhenReady ? 1 : 0, 0).sendToTarget();
    }

    public void setRepeatMode(int repeatMode) {
        this.handler.obtainMessage(12, repeatMode, 0).sendToTarget();
    }

    public void setShuffleModeEnabled(boolean shuffleModeEnabled) {
        this.handler.obtainMessage(13, shuffleModeEnabled ? 1 : 0, 0).sendToTarget();
    }

    public void seekTo(Timeline timeline, int windowIndex, long positionUs) {
        this.handler.obtainMessage(3, new SeekPosition(timeline, windowIndex, positionUs)).sendToTarget();
    }

    public void setPlaybackParameters(PlaybackParameters playbackParameters) {
        this.handler.obtainMessage(4, playbackParameters).sendToTarget();
    }

    public void setSeekParameters(SeekParameters seekParameters) {
        this.handler.obtainMessage(5, seekParameters).sendToTarget();
    }

    public void stop(boolean reset) {
        this.handler.obtainMessage(6, reset ? 1 : 0, 0).sendToTarget();
    }

    @Override // com.google.android.exoplayer2.PlayerMessage.Sender
    public synchronized void sendMessage(PlayerMessage message) {
        if (!this.released && this.internalPlaybackThread.isAlive()) {
            this.handler.obtainMessage(15, message).sendToTarget();
            return;
        }
        Log.w(TAG, "Ignoring messages sent after release.");
        message.markAsProcessed(false);
    }

    public synchronized void setForegroundMode(boolean foregroundMode) {
        if (!this.released && this.internalPlaybackThread.isAlive()) {
            if (foregroundMode) {
                this.handler.obtainMessage(14, 1, 0).sendToTarget();
            } else {
                AtomicBoolean processedFlag = new AtomicBoolean();
                this.handler.obtainMessage(14, 0, 0, processedFlag).sendToTarget();
                boolean wasInterrupted = false;
                while (!processedFlag.get()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        wasInterrupted = true;
                    }
                }
                if (wasInterrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public synchronized void release() {
        if (!this.released && this.internalPlaybackThread.isAlive()) {
            this.handler.sendEmptyMessage(7);
            boolean wasInterrupted = false;
            while (!this.released) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    wasInterrupted = true;
                }
            }
            if (wasInterrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public Looper getPlaybackLooper() {
        return this.internalPlaybackThread.getLooper();
    }

    @Override // com.google.android.exoplayer2.source.MediaSource.MediaSourceCaller
    public void onSourceInfoRefreshed(MediaSource source, Timeline timeline) {
        this.handler.obtainMessage(8, new MediaSourceRefreshInfo(source, timeline)).sendToTarget();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod.Callback
    public void onPrepared(MediaPeriod source) {
        this.handler.obtainMessage(9, source).sendToTarget();
    }

    public void onContinueLoadingRequested(MediaPeriod source) {
        this.handler.obtainMessage(10, source).sendToTarget();
    }

    @Override // com.google.android.exoplayer2.trackselection.TrackSelector.InvalidationListener
    public void onTrackSelectionsInvalidated() {
        this.handler.sendEmptyMessage(11);
    }

    @Override // com.google.android.exoplayer2.DefaultMediaClock.PlaybackParameterListener
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        sendPlaybackParametersChangedInternal(playbackParameters, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:58:0x00c8  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00d0  */
    @Override // android.os.Handler.Callback
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean handleMessage(Message msg) {
        Throwable e;
        ExoPlaybackException error;
        try {
            switch (msg.what) {
                case 0:
                    prepareInternal((MediaSource) msg.obj, msg.arg1 != 0, msg.arg2 != 0);
                    break;
                case 1:
                    setPlayWhenReadyInternal(msg.arg1 != 0);
                    break;
                case 2:
                    doSomeWork();
                    break;
                case 3:
                    seekToInternal((SeekPosition) msg.obj);
                    break;
                case 4:
                    setPlaybackParametersInternal((PlaybackParameters) msg.obj);
                    break;
                case 5:
                    setSeekParametersInternal((SeekParameters) msg.obj);
                    break;
                case 6:
                    stopInternal(false, msg.arg1 != 0, true);
                    break;
                case 7:
                    releaseInternal();
                    return true;
                case 8:
                    handleSourceInfoRefreshed((MediaSourceRefreshInfo) msg.obj);
                    break;
                case 9:
                    handlePeriodPrepared((MediaPeriod) msg.obj);
                    break;
                case 10:
                    handleContinueLoadingRequested((MediaPeriod) msg.obj);
                    break;
                case 11:
                    reselectTracksInternal();
                    break;
                case 12:
                    setRepeatModeInternal(msg.arg1);
                    break;
                case 13:
                    setShuffleModeEnabledInternal(msg.arg1 != 0);
                    break;
                case 14:
                    setForegroundModeInternal(msg.arg1 != 0, (AtomicBoolean) msg.obj);
                    break;
                case 15:
                    sendMessageInternal((PlayerMessage) msg.obj);
                    break;
                case 16:
                    sendMessageToTargetThread((PlayerMessage) msg.obj);
                    break;
                case 17:
                    handlePlaybackParameters((PlaybackParameters) msg.obj, msg.arg1 != 0);
                    break;
                default:
                    return false;
            }
            maybeNotifyPlaybackInfoChanged();
        } catch (ExoPlaybackException e2) {
            Log.e(TAG, getExoPlaybackExceptionMessage(e2), e2);
            stopInternal(true, false, false);
            this.playbackInfo = this.playbackInfo.copyWithPlaybackError(e2);
            maybeNotifyPlaybackInfoChanged();
        } catch (IOException e3) {
            Log.e(TAG, "Source error", e3);
            stopInternal(false, false, false);
            this.playbackInfo = this.playbackInfo.copyWithPlaybackError(ExoPlaybackException.createForSource(e3));
            maybeNotifyPlaybackInfoChanged();
        } catch (OutOfMemoryError e4) {
            e = e4;
            Log.e(TAG, "Internal runtime error", e);
            if (!(e instanceof OutOfMemoryError)) {
                error = ExoPlaybackException.createForOutOfMemoryError((OutOfMemoryError) e);
            } else {
                error = ExoPlaybackException.createForUnexpected((RuntimeException) e);
            }
            stopInternal(true, false, false);
            this.playbackInfo = this.playbackInfo.copyWithPlaybackError(error);
            maybeNotifyPlaybackInfoChanged();
        } catch (RuntimeException e5) {
            e = e5;
            Log.e(TAG, "Internal runtime error", e);
            if (!(e instanceof OutOfMemoryError)) {
            }
            stopInternal(true, false, false);
            this.playbackInfo = this.playbackInfo.copyWithPlaybackError(error);
            maybeNotifyPlaybackInfoChanged();
        }
        return true;
    }

    private String getExoPlaybackExceptionMessage(ExoPlaybackException e) {
        if (e.type != 1) {
            return "Playback error.";
        }
        return "Renderer error: index=" + e.rendererIndex + ", type=" + Util.getTrackTypeString(this.renderers[e.rendererIndex].getTrackType()) + ", format=" + e.rendererFormat + ", rendererSupport=" + RendererCapabilities.CC.getFormatSupportString(e.rendererFormatSupport);
    }

    private void setState(int state) {
        if (this.playbackInfo.playbackState != state) {
            this.playbackInfo = this.playbackInfo.copyWithPlaybackState(state);
        }
    }

    private void maybeNotifyPlaybackInfoChanged() {
        int i;
        if (this.playbackInfoUpdate.hasPendingUpdate(this.playbackInfo)) {
            Handler handler = this.eventHandler;
            int i2 = this.playbackInfoUpdate.operationAcks;
            if (!this.playbackInfoUpdate.positionDiscontinuity) {
                i = -1;
            } else {
                i = this.playbackInfoUpdate.discontinuityReason;
            }
            handler.obtainMessage(0, i2, i, this.playbackInfo).sendToTarget();
            this.playbackInfoUpdate.reset(this.playbackInfo);
        }
    }

    private void prepareInternal(MediaSource mediaSource, boolean resetPosition, boolean resetState) {
        this.pendingPrepareCount++;
        resetInternal(false, true, resetPosition, resetState, true);
        this.loadControl.onPrepared();
        this.mediaSource = mediaSource;
        setState(2);
        mediaSource.prepareSource(this, this.bandwidthMeter.getTransferListener());
        this.handler.sendEmptyMessage(2);
    }

    private void setPlayWhenReadyInternal(boolean playWhenReady) throws ExoPlaybackException {
        this.rebuffering = false;
        this.playWhenReady = playWhenReady;
        if (!playWhenReady) {
            stopRenderers();
            updatePlaybackPositions();
        } else if (this.playbackInfo.playbackState == 3) {
            startRenderers();
            this.handler.sendEmptyMessage(2);
        } else if (this.playbackInfo.playbackState == 2) {
            this.handler.sendEmptyMessage(2);
        }
    }

    private void setRepeatModeInternal(int repeatMode) throws ExoPlaybackException {
        this.repeatMode = repeatMode;
        if (!this.queue.updateRepeatMode(repeatMode)) {
            seekToCurrentPosition(true);
        }
        handleLoadingMediaPeriodChanged(false);
    }

    private void setShuffleModeEnabledInternal(boolean shuffleModeEnabled) throws ExoPlaybackException {
        this.shuffleModeEnabled = shuffleModeEnabled;
        if (!this.queue.updateShuffleModeEnabled(shuffleModeEnabled)) {
            seekToCurrentPosition(true);
        }
        handleLoadingMediaPeriodChanged(false);
    }

    private void seekToCurrentPosition(boolean sendDiscontinuity) throws ExoPlaybackException {
        MediaSource.MediaPeriodId periodId = this.queue.getPlayingPeriod().info.id;
        long newPositionUs = seekToPeriodPosition(periodId, this.playbackInfo.positionUs, true);
        if (newPositionUs != this.playbackInfo.positionUs) {
            this.playbackInfo = copyWithNewPosition(periodId, newPositionUs, this.playbackInfo.contentPositionUs);
            if (sendDiscontinuity) {
                this.playbackInfoUpdate.setPositionDiscontinuity(4);
            }
        }
    }

    private void startRenderers() throws ExoPlaybackException {
        Renderer[] rendererArr;
        this.rebuffering = false;
        this.mediaClock.start();
        for (Renderer renderer : this.enabledRenderers) {
            renderer.start();
        }
    }

    private void stopRenderers() throws ExoPlaybackException {
        Renderer[] rendererArr;
        this.mediaClock.stop();
        for (Renderer renderer : this.enabledRenderers) {
            ensureStopped(renderer);
        }
    }

    private void updatePlaybackPositions() throws ExoPlaybackException {
        long discontinuityPositionUs;
        MediaPeriodHolder playingPeriodHolder = this.queue.getPlayingPeriod();
        if (playingPeriodHolder == null) {
            return;
        }
        if (playingPeriodHolder.prepared) {
            discontinuityPositionUs = playingPeriodHolder.mediaPeriod.readDiscontinuity();
        } else {
            discontinuityPositionUs = -9223372036854775807L;
        }
        if (discontinuityPositionUs != C.TIME_UNSET) {
            resetRendererPosition(discontinuityPositionUs);
            if (discontinuityPositionUs != this.playbackInfo.positionUs) {
                this.playbackInfo = copyWithNewPosition(this.playbackInfo.periodId, discontinuityPositionUs, this.playbackInfo.contentPositionUs);
                this.playbackInfoUpdate.setPositionDiscontinuity(4);
            }
        } else {
            long syncAndGetPositionUs = this.mediaClock.syncAndGetPositionUs(playingPeriodHolder != this.queue.getReadingPeriod());
            this.rendererPositionUs = syncAndGetPositionUs;
            long periodPositionUs = playingPeriodHolder.toPeriodTime(syncAndGetPositionUs);
            maybeTriggerPendingMessages(this.playbackInfo.positionUs, periodPositionUs);
            this.playbackInfo.positionUs = periodPositionUs;
        }
        MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        this.playbackInfo.bufferedPositionUs = loadingPeriod.getBufferedPositionUs();
        this.playbackInfo.totalBufferedDurationUs = getTotalBufferedDurationUs();
    }

    private void doSomeWork() throws ExoPlaybackException, IOException {
        long operationStartTimeMs = this.clock.uptimeMillis();
        updatePeriods();
        if (this.playbackInfo.playbackState == 1 || this.playbackInfo.playbackState == 4) {
            this.handler.removeMessages(2);
            return;
        }
        MediaPeriodHolder playingPeriodHolder = this.queue.getPlayingPeriod();
        if (playingPeriodHolder == null) {
            scheduleNextWork(operationStartTimeMs, 10L);
            return;
        }
        TraceUtil.beginSection("doSomeWork");
        updatePlaybackPositions();
        boolean renderersEnded = true;
        boolean renderersAllowPlayback = true;
        if (playingPeriodHolder.prepared) {
            long rendererPositionElapsedRealtimeUs = SystemClock.elapsedRealtime() * 1000;
            playingPeriodHolder.mediaPeriod.discardBuffer(this.playbackInfo.positionUs - this.backBufferDurationUs, this.retainBackBufferFromKeyframe);
            int i = 0;
            while (true) {
                Renderer[] rendererArr = this.renderers;
                if (i >= rendererArr.length) {
                    break;
                }
                Renderer renderer = rendererArr[i];
                if (renderer.getState() != 0) {
                    renderer.render(this.rendererPositionUs, rendererPositionElapsedRealtimeUs);
                    renderersEnded = renderersEnded && renderer.isEnded();
                    boolean isReadingAhead = playingPeriodHolder.sampleStreams[i] != renderer.getStream();
                    boolean isWaitingForNextStream = !isReadingAhead && playingPeriodHolder.getNext() != null && renderer.hasReadStreamToEnd();
                    boolean allowsPlayback = isReadingAhead || isWaitingForNextStream || renderer.isReady() || renderer.isEnded();
                    renderersAllowPlayback = renderersAllowPlayback && allowsPlayback;
                    if (!allowsPlayback) {
                        renderer.maybeThrowStreamError();
                    }
                }
                i++;
            }
        } else {
            playingPeriodHolder.mediaPeriod.maybeThrowPrepareError();
        }
        long playingPeriodDurationUs = playingPeriodHolder.info.durationUs;
        if (renderersEnded && playingPeriodHolder.prepared && ((playingPeriodDurationUs == C.TIME_UNSET || playingPeriodDurationUs <= this.playbackInfo.positionUs) && playingPeriodHolder.info.isFinal)) {
            setState(4);
            stopRenderers();
        } else if (this.playbackInfo.playbackState != 2 || !shouldTransitionToReadyState(renderersAllowPlayback)) {
            if (this.playbackInfo.playbackState == 3 && (this.enabledRenderers.length != 0 ? !renderersAllowPlayback : !isTimelineReady())) {
                this.rebuffering = this.playWhenReady;
                setState(2);
                stopRenderers();
            }
        } else {
            setState(3);
            if (this.playWhenReady) {
                startRenderers();
            }
        }
        if (this.playbackInfo.playbackState == 2) {
            for (Renderer renderer2 : this.enabledRenderers) {
                renderer2.maybeThrowStreamError();
            }
        }
        if ((this.playWhenReady && this.playbackInfo.playbackState == 3) || this.playbackInfo.playbackState == 2) {
            scheduleNextWork(operationStartTimeMs, 10L);
        } else if (this.enabledRenderers.length != 0 && this.playbackInfo.playbackState != 4) {
            scheduleNextWork(operationStartTimeMs, 1000L);
        } else {
            this.handler.removeMessages(2);
        }
        TraceUtil.endSection();
    }

    private void scheduleNextWork(long thisOperationStartTimeMs, long intervalMs) {
        this.handler.removeMessages(2);
        this.handler.sendEmptyMessageAtTime(2, thisOperationStartTimeMs + intervalMs);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0104  */
    /* JADX WARN: Removed duplicated region for block: B:64:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r9v0, types: [android.util.Pair] */
    /* JADX WARN: Type inference failed for: r9v2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void seekToInternal(SeekPosition seekPosition) throws ExoPlaybackException {
        long contentPositionUs;
        long periodPositionUs;
        boolean seekPositionAdjusted;
        MediaSource.MediaPeriodId periodId;
        Throwable th;
        int i;
        long newPeriodPositionUs;
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
        int resolveSeekPosition = resolveSeekPosition(seekPosition, true);
        if (resolveSeekPosition == 0) {
            contentPositionUs = -9223372036854775807L;
            seekPositionAdjusted = true;
            periodId = this.playbackInfo.getDummyFirstMediaPeriodId(this.shuffleModeEnabled, this.window, this.period);
            periodPositionUs = -9223372036854775807L;
        } else {
            Object periodUid = ((Pair) resolveSeekPosition).first;
            long contentPositionUs2 = ((Long) ((Pair) resolveSeekPosition).second).longValue();
            MediaSource.MediaPeriodId periodId2 = this.queue.resolveMediaPeriodIdForAds(periodUid, contentPositionUs2);
            if (periodId2.isAd()) {
                periodPositionUs = 0;
                contentPositionUs = contentPositionUs2;
                periodId = periodId2;
                seekPositionAdjusted = true;
            } else {
                periodPositionUs = ((Long) ((Pair) resolveSeekPosition).second).longValue();
                contentPositionUs = contentPositionUs2;
                periodId = periodId2;
                seekPositionAdjusted = seekPosition.windowPositionUs == C.TIME_UNSET;
            }
        }
        try {
            try {
                if (this.mediaSource == null) {
                    i = 2;
                } else if (this.pendingPrepareCount <= 0) {
                    if (periodPositionUs == C.TIME_UNSET) {
                        setState(4);
                        i = 2;
                        resetInternal(false, false, true, false, true);
                    } else {
                        i = 2;
                        long newPeriodPositionUs2 = periodPositionUs;
                        if (periodId.equals(this.playbackInfo.periodId)) {
                            MediaPeriodHolder playingPeriodHolder = this.queue.getPlayingPeriod();
                            if (playingPeriodHolder != null && playingPeriodHolder.prepared && newPeriodPositionUs2 != 0) {
                                newPeriodPositionUs = playingPeriodHolder.mediaPeriod.getAdjustedSeekPositionUs(newPeriodPositionUs2, this.seekParameters);
                            } else {
                                newPeriodPositionUs = newPeriodPositionUs2;
                            }
                            if (C.usToMs(newPeriodPositionUs) == C.usToMs(this.playbackInfo.positionUs)) {
                                this.playbackInfo = copyWithNewPosition(periodId, this.playbackInfo.positionUs, contentPositionUs);
                                if (!seekPositionAdjusted) {
                                    return;
                                }
                                this.playbackInfoUpdate.setPositionDiscontinuity(2);
                                return;
                            }
                            newPeriodPositionUs2 = newPeriodPositionUs;
                        }
                        long newPeriodPositionUs3 = seekToPeriodPosition(periodId, newPeriodPositionUs2);
                        seekPositionAdjusted |= periodPositionUs != newPeriodPositionUs3;
                        periodPositionUs = newPeriodPositionUs3;
                    }
                    this.playbackInfo = copyWithNewPosition(periodId, periodPositionUs, contentPositionUs);
                    if (!seekPositionAdjusted) {
                        this.playbackInfoUpdate.setPositionDiscontinuity(i);
                        return;
                    }
                    return;
                } else {
                    i = 2;
                }
                this.pendingInitialSeekPosition = seekPosition;
                this.playbackInfo = copyWithNewPosition(periodId, periodPositionUs, contentPositionUs);
                if (!seekPositionAdjusted) {
                }
            } catch (Throwable th2) {
                th = th2;
                this.playbackInfo = copyWithNewPosition(periodId, periodPositionUs, contentPositionUs);
                if (seekPositionAdjusted) {
                    this.playbackInfoUpdate.setPositionDiscontinuity(resolveSeekPosition);
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            resolveSeekPosition = 2;
        }
    }

    private long seekToPeriodPosition(MediaSource.MediaPeriodId periodId, long periodPositionUs) throws ExoPlaybackException {
        return seekToPeriodPosition(periodId, periodPositionUs, this.queue.getPlayingPeriod() != this.queue.getReadingPeriod());
    }

    private long seekToPeriodPosition(MediaSource.MediaPeriodId periodId, long periodPositionUs, boolean forceDisableRenderers) throws ExoPlaybackException {
        Renderer[] rendererArr;
        stopRenderers();
        this.rebuffering = false;
        if (this.playbackInfo.playbackState != 1 && !this.playbackInfo.timeline.isEmpty()) {
            setState(2);
        }
        MediaPeriodHolder oldPlayingPeriodHolder = this.queue.getPlayingPeriod();
        MediaPeriodHolder newPlayingPeriodHolder = oldPlayingPeriodHolder;
        while (true) {
            if (newPlayingPeriodHolder == null) {
                break;
            }
            if (periodId.equals(newPlayingPeriodHolder.info.id) && newPlayingPeriodHolder.prepared) {
                this.queue.removeAfter(newPlayingPeriodHolder);
                break;
            }
            newPlayingPeriodHolder = this.queue.advancePlayingPeriod();
        }
        if (forceDisableRenderers || oldPlayingPeriodHolder != newPlayingPeriodHolder || (newPlayingPeriodHolder != null && newPlayingPeriodHolder.toRendererTime(periodPositionUs) < 0)) {
            for (Renderer renderer : this.enabledRenderers) {
                disableRenderer(renderer);
            }
            this.enabledRenderers = new Renderer[0];
            oldPlayingPeriodHolder = null;
            if (newPlayingPeriodHolder != null) {
                newPlayingPeriodHolder.setRendererOffset(0L);
            }
        }
        if (newPlayingPeriodHolder != null) {
            updatePlayingPeriodRenderers(oldPlayingPeriodHolder);
            if (newPlayingPeriodHolder.hasEnabledTracks) {
                periodPositionUs = newPlayingPeriodHolder.mediaPeriod.seekToUs(periodPositionUs);
                newPlayingPeriodHolder.mediaPeriod.discardBuffer(periodPositionUs - this.backBufferDurationUs, this.retainBackBufferFromKeyframe);
            }
            resetRendererPosition(periodPositionUs);
            maybeContinueLoading();
        } else {
            this.queue.clear(true);
            this.playbackInfo = this.playbackInfo.copyWithTrackInfo(TrackGroupArray.EMPTY, this.emptyTrackSelectorResult);
            resetRendererPosition(periodPositionUs);
        }
        handleLoadingMediaPeriodChanged(false);
        this.handler.sendEmptyMessage(2);
        return periodPositionUs;
    }

    private void resetRendererPosition(long periodPositionUs) throws ExoPlaybackException {
        long j;
        Renderer[] rendererArr;
        MediaPeriodHolder playingMediaPeriod = this.queue.getPlayingPeriod();
        if (playingMediaPeriod == null) {
            j = periodPositionUs;
        } else {
            j = playingMediaPeriod.toRendererTime(periodPositionUs);
        }
        this.rendererPositionUs = j;
        this.mediaClock.resetPosition(j);
        for (Renderer renderer : this.enabledRenderers) {
            renderer.resetPosition(this.rendererPositionUs);
        }
        notifyTrackSelectionDiscontinuity();
    }

    private void setPlaybackParametersInternal(PlaybackParameters playbackParameters) {
        this.mediaClock.setPlaybackParameters(playbackParameters);
        sendPlaybackParametersChangedInternal(this.mediaClock.getPlaybackParameters(), true);
    }

    private void setSeekParametersInternal(SeekParameters seekParameters) {
        this.seekParameters = seekParameters;
    }

    private void setForegroundModeInternal(boolean foregroundMode, AtomicBoolean processedFlag) {
        Renderer[] rendererArr;
        if (this.foregroundMode != foregroundMode) {
            this.foregroundMode = foregroundMode;
            if (!foregroundMode) {
                for (Renderer renderer : this.renderers) {
                    if (renderer.getState() == 0) {
                        renderer.reset();
                    }
                }
            }
        }
        if (processedFlag != null) {
            synchronized (this) {
                processedFlag.set(true);
                notifyAll();
            }
        }
    }

    private void stopInternal(boolean forceResetRenderers, boolean resetPositionAndState, boolean acknowledgeStop) {
        resetInternal(forceResetRenderers || !this.foregroundMode, true, resetPositionAndState, resetPositionAndState, resetPositionAndState);
        this.playbackInfoUpdate.incrementPendingOperationAcks(this.pendingPrepareCount + (acknowledgeStop ? 1 : 0));
        this.pendingPrepareCount = 0;
        this.loadControl.onStopped();
        setState(1);
    }

    private void releaseInternal() {
        resetInternal(true, true, true, true, false);
        this.loadControl.onReleased();
        setState(1);
        this.internalPlaybackThread.quit();
        synchronized (this) {
            this.released = true;
            notifyAll();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00da  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00e1  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00ea  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00f8  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00fa  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0103  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x010e  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0111  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void resetInternal(boolean resetRenderers, boolean releaseMediaSource, boolean resetPosition, boolean resetState, boolean resetError) {
        Renderer[] rendererArr;
        boolean resetPosition2;
        MediaSource.MediaPeriodId mediaPeriodId;
        long startPositionUs;
        ExoPlaybackException exoPlaybackException;
        MediaSource mediaSource;
        Renderer[] rendererArr2;
        this.handler.removeMessages(2);
        this.rebuffering = false;
        this.mediaClock.stop();
        this.rendererPositionUs = 0L;
        for (Renderer renderer : this.enabledRenderers) {
            try {
                disableRenderer(renderer);
            } catch (ExoPlaybackException | RuntimeException e) {
                Log.e(TAG, "Disable failed.", e);
            }
        }
        if (resetRenderers) {
            for (Renderer renderer2 : this.renderers) {
                try {
                    renderer2.reset();
                } catch (RuntimeException e2) {
                    Log.e(TAG, "Reset failed.", e2);
                }
            }
        }
        this.enabledRenderers = new Renderer[0];
        if (resetPosition) {
            this.pendingInitialSeekPosition = null;
        } else if (resetState) {
            if (this.pendingInitialSeekPosition == null && !this.playbackInfo.timeline.isEmpty()) {
                this.playbackInfo.timeline.getPeriodByUid(this.playbackInfo.periodId.periodUid, this.period);
                long windowPositionUs = this.playbackInfo.positionUs + this.period.getPositionInWindowUs();
                this.pendingInitialSeekPosition = new SeekPosition(Timeline.EMPTY, this.period.windowIndex, windowPositionUs);
            }
            resetPosition2 = true;
            this.queue.clear(!resetState);
            this.shouldContinueLoading = false;
            if (resetState) {
                this.queue.setTimeline(Timeline.EMPTY);
                Iterator<PendingMessageInfo> it = this.pendingMessages.iterator();
                while (it.hasNext()) {
                    PendingMessageInfo pendingMessageInfo = it.next();
                    pendingMessageInfo.message.markAsProcessed(false);
                }
                this.pendingMessages.clear();
            }
            if (!resetPosition2) {
                mediaPeriodId = this.playbackInfo.getDummyFirstMediaPeriodId(this.shuffleModeEnabled, this.window, this.period);
            } else {
                mediaPeriodId = this.playbackInfo.periodId;
            }
            long j = C.TIME_UNSET;
            if (resetPosition2) {
                startPositionUs = this.playbackInfo.positionUs;
            } else {
                startPositionUs = -9223372036854775807L;
            }
            if (!resetPosition2) {
                j = this.playbackInfo.contentPositionUs;
            }
            long contentPositionUs = j;
            Timeline timeline = !resetState ? Timeline.EMPTY : this.playbackInfo.timeline;
            int i = this.playbackInfo.playbackState;
            if (resetError) {
                exoPlaybackException = this.playbackInfo.playbackError;
            } else {
                exoPlaybackException = null;
            }
            this.playbackInfo = new PlaybackInfo(timeline, mediaPeriodId, startPositionUs, contentPositionUs, i, exoPlaybackException, false, !resetState ? TrackGroupArray.EMPTY : this.playbackInfo.trackGroups, !resetState ? this.emptyTrackSelectorResult : this.playbackInfo.trackSelectorResult, mediaPeriodId, startPositionUs, 0L, startPositionUs);
            if (!releaseMediaSource && (mediaSource = this.mediaSource) != null) {
                try {
                    mediaSource.releaseSource(this);
                } catch (RuntimeException e3) {
                    Log.e(TAG, "Failed to release child source.", e3);
                }
                this.mediaSource = null;
                return;
            }
        }
        resetPosition2 = resetPosition;
        this.queue.clear(!resetState);
        this.shouldContinueLoading = false;
        if (resetState) {
        }
        if (!resetPosition2) {
        }
        long j2 = C.TIME_UNSET;
        if (resetPosition2) {
        }
        if (!resetPosition2) {
        }
        long contentPositionUs2 = j2;
        Timeline timeline2 = !resetState ? Timeline.EMPTY : this.playbackInfo.timeline;
        int i2 = this.playbackInfo.playbackState;
        if (resetError) {
        }
        this.playbackInfo = new PlaybackInfo(timeline2, mediaPeriodId, startPositionUs, contentPositionUs2, i2, exoPlaybackException, false, !resetState ? TrackGroupArray.EMPTY : this.playbackInfo.trackGroups, !resetState ? this.emptyTrackSelectorResult : this.playbackInfo.trackSelectorResult, mediaPeriodId, startPositionUs, 0L, startPositionUs);
        if (!releaseMediaSource) {
        }
    }

    private void sendMessageInternal(PlayerMessage message) throws ExoPlaybackException {
        if (message.getPositionMs() == C.TIME_UNSET) {
            sendMessageToTarget(message);
        } else if (this.mediaSource == null || this.pendingPrepareCount > 0) {
            this.pendingMessages.add(new PendingMessageInfo(message));
        } else {
            PendingMessageInfo pendingMessageInfo = new PendingMessageInfo(message);
            if (resolvePendingMessagePosition(pendingMessageInfo)) {
                this.pendingMessages.add(pendingMessageInfo);
                Collections.sort(this.pendingMessages);
                return;
            }
            message.markAsProcessed(false);
        }
    }

    private void sendMessageToTarget(PlayerMessage message) throws ExoPlaybackException {
        if (message.getHandler().getLooper() == this.handler.getLooper()) {
            deliverMessage(message);
            if (this.playbackInfo.playbackState == 3 || this.playbackInfo.playbackState == 2) {
                this.handler.sendEmptyMessage(2);
                return;
            }
            return;
        }
        this.handler.obtainMessage(16, message).sendToTarget();
    }

    private void sendMessageToTargetThread(final PlayerMessage message) {
        Handler handler = message.getHandler();
        if (!handler.getLooper().getThread().isAlive()) {
            Log.w("TAG", "Trying to send message on a dead thread.");
            message.markAsProcessed(false);
            return;
        }
        handler.post(new Runnable() { // from class: com.google.android.exoplayer2.ExoPlayerImplInternal$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ExoPlayerImplInternal.this.m38x1fb14fa8(message);
            }
        });
    }

    /* renamed from: lambda$sendMessageToTargetThread$0$com-google-android-exoplayer2-ExoPlayerImplInternal */
    public /* synthetic */ void m38x1fb14fa8(PlayerMessage message) {
        try {
            deliverMessage(message);
        } catch (ExoPlaybackException e) {
            Log.e(TAG, "Unexpected error delivering message on external thread.", e);
            throw new RuntimeException(e);
        }
    }

    private void deliverMessage(PlayerMessage message) throws ExoPlaybackException {
        if (message.isCanceled()) {
            return;
        }
        try {
            message.getTarget().handleMessage(message.getType(), message.getPayload());
        } finally {
            message.markAsProcessed(true);
        }
    }

    private void resolvePendingMessagePositions() {
        for (int i = this.pendingMessages.size() - 1; i >= 0; i--) {
            if (!resolvePendingMessagePosition(this.pendingMessages.get(i))) {
                this.pendingMessages.get(i).message.markAsProcessed(false);
                this.pendingMessages.remove(i);
            }
        }
        Collections.sort(this.pendingMessages);
    }

    private boolean resolvePendingMessagePosition(PendingMessageInfo pendingMessageInfo) {
        if (pendingMessageInfo.resolvedPeriodUid == null) {
            Pair<Object, Long> periodPosition = resolveSeekPosition(new SeekPosition(pendingMessageInfo.message.getTimeline(), pendingMessageInfo.message.getWindowIndex(), C.msToUs(pendingMessageInfo.message.getPositionMs())), false);
            if (periodPosition == null) {
                return false;
            }
            pendingMessageInfo.setResolvedPosition(this.playbackInfo.timeline.getIndexOfPeriod(periodPosition.first), ((Long) periodPosition.second).longValue(), periodPosition.first);
            return true;
        }
        int index = this.playbackInfo.timeline.getIndexOfPeriod(pendingMessageInfo.resolvedPeriodUid);
        if (index == -1) {
            return false;
        }
        pendingMessageInfo.resolvedPeriodIndex = index;
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:63:0x00ea  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00f3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void maybeTriggerPendingMessages(long oldPeriodPositionUs, long newPeriodPositionUs) throws ExoPlaybackException {
        PendingMessageInfo nextInfo;
        boolean deleteAfterDelivery;
        PendingMessageInfo pendingMessageInfo;
        boolean isCanceled;
        PendingMessageInfo pendingMessageInfo2;
        if (this.pendingMessages.isEmpty() || this.playbackInfo.periodId.isAd()) {
            return;
        }
        if (this.playbackInfo.startPositionUs == oldPeriodPositionUs && this.deliverPendingMessageAtStartPositionRequired) {
            oldPeriodPositionUs--;
        }
        this.deliverPendingMessageAtStartPositionRequired = false;
        int currentPeriodIndex = this.playbackInfo.timeline.getIndexOfPeriod(this.playbackInfo.periodId.periodUid);
        int nextPendingMessageIndex = Math.min(this.nextPendingMessageIndexHint, this.pendingMessages.size());
        PendingMessageInfo previousInfo = nextPendingMessageIndex > 0 ? this.pendingMessages.get(nextPendingMessageIndex - 1) : null;
        while (previousInfo != null && (previousInfo.resolvedPeriodIndex > currentPeriodIndex || (previousInfo.resolvedPeriodIndex == currentPeriodIndex && previousInfo.resolvedPeriodTimeUs > oldPeriodPositionUs))) {
            nextPendingMessageIndex--;
            previousInfo = nextPendingMessageIndex > 0 ? this.pendingMessages.get(nextPendingMessageIndex - 1) : null;
        }
        if (nextPendingMessageIndex < this.pendingMessages.size()) {
            nextInfo = this.pendingMessages.get(nextPendingMessageIndex);
        } else {
            nextInfo = null;
        }
        while (nextInfo != null && nextInfo.resolvedPeriodUid != null && (nextInfo.resolvedPeriodIndex < currentPeriodIndex || (nextInfo.resolvedPeriodIndex == currentPeriodIndex && nextInfo.resolvedPeriodTimeUs <= oldPeriodPositionUs))) {
            nextPendingMessageIndex++;
            if (nextPendingMessageIndex < this.pendingMessages.size()) {
                pendingMessageInfo2 = this.pendingMessages.get(nextPendingMessageIndex);
            } else {
                pendingMessageInfo2 = null;
            }
            nextInfo = pendingMessageInfo2;
        }
        while (nextInfo != null && nextInfo.resolvedPeriodUid != null && nextInfo.resolvedPeriodIndex == currentPeriodIndex && nextInfo.resolvedPeriodTimeUs > oldPeriodPositionUs && nextInfo.resolvedPeriodTimeUs <= newPeriodPositionUs) {
            try {
                sendMessageToTarget(nextInfo.message);
                if (!deleteAfterDelivery) {
                    if (!isCanceled) {
                        if (nextPendingMessageIndex >= this.pendingMessages.size()) {
                            pendingMessageInfo = this.pendingMessages.get(nextPendingMessageIndex);
                        } else {
                            pendingMessageInfo = null;
                        }
                        nextInfo = pendingMessageInfo;
                    }
                }
                if (nextPendingMessageIndex >= this.pendingMessages.size()) {
                }
                nextInfo = pendingMessageInfo;
            } finally {
                if (nextInfo.message.getDeleteAfterDelivery() || nextInfo.message.isCanceled()) {
                    this.pendingMessages.remove(nextPendingMessageIndex);
                } else {
                    int nextPendingMessageIndex2 = nextPendingMessageIndex + 1;
                }
            }
        }
        this.nextPendingMessageIndexHint = nextPendingMessageIndex;
    }

    private void ensureStopped(Renderer renderer) throws ExoPlaybackException {
        if (renderer.getState() == 2) {
            renderer.stop();
        }
    }

    private void disableRenderer(Renderer renderer) throws ExoPlaybackException {
        this.mediaClock.onRendererDisabled(renderer);
        ensureStopped(renderer);
        renderer.disable();
    }

    private void reselectTracksInternal() throws ExoPlaybackException {
        int i;
        TrackSelectorResult newTrackSelectorResult;
        boolean[] streamResetFlags;
        MediaPeriodHolder playingPeriodHolder;
        float playbackSpeed = this.mediaClock.getPlaybackParameters().speed;
        MediaPeriodHolder periodHolder = this.queue.getPlayingPeriod();
        MediaPeriodHolder readingPeriodHolder = this.queue.getReadingPeriod();
        MediaPeriodHolder periodHolder2 = periodHolder;
        boolean selectionsChangedForReadPeriod = true;
        while (periodHolder2 != null && periodHolder2.prepared) {
            TrackSelectorResult newTrackSelectorResult2 = periodHolder2.selectTracks(playbackSpeed, this.playbackInfo.timeline);
            if (!newTrackSelectorResult2.isEquivalent(periodHolder2.getTrackSelectorResult())) {
                if (!selectionsChangedForReadPeriod) {
                    i = 4;
                    this.queue.removeAfter(periodHolder2);
                    if (periodHolder2.prepared) {
                        long loadingPeriodPositionUs = Math.max(periodHolder2.info.startPositionUs, periodHolder2.toPeriodTime(this.rendererPositionUs));
                        periodHolder2.applyTrackSelection(newTrackSelectorResult2, loadingPeriodPositionUs, false);
                    }
                } else {
                    MediaPeriodHolder playingPeriodHolder2 = this.queue.getPlayingPeriod();
                    boolean recreateStreams = this.queue.removeAfter(playingPeriodHolder2);
                    boolean[] streamResetFlags2 = new boolean[this.renderers.length];
                    long periodPositionUs = playingPeriodHolder2.applyTrackSelection(newTrackSelectorResult2, this.playbackInfo.positionUs, recreateStreams, streamResetFlags2);
                    if (this.playbackInfo.playbackState == 4 || periodPositionUs == this.playbackInfo.positionUs) {
                        streamResetFlags = streamResetFlags2;
                        newTrackSelectorResult = newTrackSelectorResult2;
                        i = 4;
                        playingPeriodHolder = playingPeriodHolder2;
                    } else {
                        streamResetFlags = streamResetFlags2;
                        playingPeriodHolder = playingPeriodHolder2;
                        i = 4;
                        newTrackSelectorResult = newTrackSelectorResult2;
                        this.playbackInfo = copyWithNewPosition(this.playbackInfo.periodId, periodPositionUs, this.playbackInfo.contentPositionUs);
                        this.playbackInfoUpdate.setPositionDiscontinuity(4);
                        resetRendererPosition(periodPositionUs);
                    }
                    int enabledRendererCount = 0;
                    boolean[] rendererWasEnabledFlags = new boolean[this.renderers.length];
                    int i2 = 0;
                    while (true) {
                        Renderer[] rendererArr = this.renderers;
                        if (i2 >= rendererArr.length) {
                            break;
                        }
                        Renderer renderer = rendererArr[i2];
                        rendererWasEnabledFlags[i2] = renderer.getState() != 0;
                        SampleStream sampleStream = playingPeriodHolder.sampleStreams[i2];
                        if (sampleStream != null) {
                            enabledRendererCount++;
                        }
                        if (rendererWasEnabledFlags[i2]) {
                            if (sampleStream != renderer.getStream()) {
                                disableRenderer(renderer);
                            } else if (streamResetFlags[i2]) {
                                renderer.resetPosition(this.rendererPositionUs);
                            }
                        }
                        i2++;
                    }
                    this.playbackInfo = this.playbackInfo.copyWithTrackInfo(playingPeriodHolder.getTrackGroups(), playingPeriodHolder.getTrackSelectorResult());
                    enableRenderers(rendererWasEnabledFlags, enabledRendererCount);
                }
                handleLoadingMediaPeriodChanged(true);
                if (this.playbackInfo.playbackState != i) {
                    maybeContinueLoading();
                    updatePlaybackPositions();
                    this.handler.sendEmptyMessage(2);
                    return;
                }
                return;
            }
            float playbackSpeed2 = playbackSpeed;
            if (periodHolder2 == readingPeriodHolder) {
                selectionsChangedForReadPeriod = false;
            }
            periodHolder2 = periodHolder2.getNext();
            playbackSpeed = playbackSpeed2;
        }
    }

    private void updateTrackSelectionPlaybackSpeed(float playbackSpeed) {
        for (MediaPeriodHolder periodHolder = this.queue.getPlayingPeriod(); periodHolder != null; periodHolder = periodHolder.getNext()) {
            TrackSelection[] trackSelections = periodHolder.getTrackSelectorResult().selections.getAll();
            for (TrackSelection trackSelection : trackSelections) {
                if (trackSelection != null) {
                    trackSelection.onPlaybackSpeed(playbackSpeed);
                }
            }
        }
    }

    private void notifyTrackSelectionDiscontinuity() {
        for (MediaPeriodHolder periodHolder = this.queue.getPlayingPeriod(); periodHolder != null; periodHolder = periodHolder.getNext()) {
            TrackSelection[] trackSelections = periodHolder.getTrackSelectorResult().selections.getAll();
            for (TrackSelection trackSelection : trackSelections) {
                if (trackSelection != null) {
                    trackSelection.onDiscontinuity();
                }
            }
        }
    }

    private boolean shouldTransitionToReadyState(boolean renderersReadyOrEnded) {
        if (this.enabledRenderers.length == 0) {
            return isTimelineReady();
        }
        if (!renderersReadyOrEnded) {
            return false;
        }
        if (!this.playbackInfo.isLoading) {
            return true;
        }
        MediaPeriodHolder loadingHolder = this.queue.getLoadingPeriod();
        boolean bufferedToEnd = loadingHolder.isFullyBuffered() && loadingHolder.info.isFinal;
        return bufferedToEnd || this.loadControl.shouldStartPlayback(getTotalBufferedDurationUs(), this.mediaClock.getPlaybackParameters().speed, this.rebuffering);
    }

    private boolean isTimelineReady() {
        MediaPeriodHolder playingPeriodHolder = this.queue.getPlayingPeriod();
        long playingPeriodDurationUs = playingPeriodHolder.info.durationUs;
        return playingPeriodHolder.prepared && (playingPeriodDurationUs == C.TIME_UNSET || this.playbackInfo.positionUs < playingPeriodDurationUs);
    }

    private void maybeThrowSourceInfoRefreshError() throws IOException {
        Renderer[] rendererArr;
        MediaPeriodHolder loadingPeriodHolder = this.queue.getLoadingPeriod();
        if (loadingPeriodHolder != null) {
            for (Renderer renderer : this.enabledRenderers) {
                if (!renderer.hasReadStreamToEnd()) {
                    return;
                }
            }
        }
        this.mediaSource.maybeThrowSourceInfoRefreshError();
    }

    private void handleSourceInfoRefreshed(MediaSourceRefreshInfo sourceRefreshInfo) throws ExoPlaybackException {
        long newContentPositionUs;
        MediaSource.MediaPeriodId newPeriodId;
        if (sourceRefreshInfo.source == this.mediaSource) {
            this.playbackInfoUpdate.incrementPendingOperationAcks(this.pendingPrepareCount);
            this.pendingPrepareCount = 0;
            Timeline oldTimeline = this.playbackInfo.timeline;
            Timeline timeline = sourceRefreshInfo.timeline;
            this.queue.setTimeline(timeline);
            this.playbackInfo = this.playbackInfo.copyWithTimeline(timeline);
            resolvePendingMessagePositions();
            MediaSource.MediaPeriodId newPeriodId2 = this.playbackInfo.periodId;
            long newContentPositionUs2 = this.playbackInfo.periodId.isAd() ? this.playbackInfo.contentPositionUs : this.playbackInfo.positionUs;
            long oldContentPositionUs = newContentPositionUs2;
            SeekPosition seekPosition = this.pendingInitialSeekPosition;
            if (seekPosition != null) {
                Pair<Object, Long> periodPosition = resolveSeekPosition(seekPosition, true);
                this.pendingInitialSeekPosition = null;
                if (periodPosition == null) {
                    handleSourceInfoRefreshEndedPlayback();
                    return;
                }
                long newContentPositionUs3 = ((Long) periodPosition.second).longValue();
                newPeriodId = this.queue.resolveMediaPeriodIdForAds(periodPosition.first, newContentPositionUs3);
                newContentPositionUs = newContentPositionUs3;
            } else if (oldContentPositionUs == C.TIME_UNSET && !timeline.isEmpty()) {
                Pair<Object, Long> defaultPosition = getPeriodPosition(timeline, timeline.getFirstWindowIndex(this.shuffleModeEnabled), C.TIME_UNSET);
                MediaSource.MediaPeriodId newPeriodId3 = this.queue.resolveMediaPeriodIdForAds(defaultPosition.first, ((Long) defaultPosition.second).longValue());
                if (!newPeriodId3.isAd()) {
                    newContentPositionUs2 = ((Long) defaultPosition.second).longValue();
                }
                newPeriodId = newPeriodId3;
                newContentPositionUs = newContentPositionUs2;
            } else if (timeline.getIndexOfPeriod(newPeriodId2.periodUid) == -1) {
                Object newPeriodUid = resolveSubsequentPeriod(newPeriodId2.periodUid, oldTimeline, timeline);
                if (newPeriodUid == null) {
                    handleSourceInfoRefreshEndedPlayback();
                    return;
                }
                Pair<Object, Long> defaultPosition2 = getPeriodPosition(timeline, timeline.getPeriodByUid(newPeriodUid, this.period).windowIndex, C.TIME_UNSET);
                long newContentPositionUs4 = ((Long) defaultPosition2.second).longValue();
                newPeriodId = this.queue.resolveMediaPeriodIdForAds(defaultPosition2.first, newContentPositionUs4);
                newContentPositionUs = newContentPositionUs4;
            } else {
                MediaSource.MediaPeriodId newPeriodId4 = this.queue.resolveMediaPeriodIdForAds(this.playbackInfo.periodId.periodUid, newContentPositionUs2);
                if (!this.playbackInfo.periodId.isAd() && !newPeriodId4.isAd()) {
                    newPeriodId = this.playbackInfo.periodId;
                    newContentPositionUs = newContentPositionUs2;
                } else {
                    newPeriodId = newPeriodId4;
                    newContentPositionUs = newContentPositionUs2;
                }
            }
            if (this.playbackInfo.periodId.equals(newPeriodId) && oldContentPositionUs == newContentPositionUs) {
                if (!this.queue.updateQueuedPeriods(this.rendererPositionUs, getMaxRendererReadPositionUs())) {
                    seekToCurrentPosition(false);
                }
            } else {
                MediaPeriodHolder periodHolder = this.queue.getPlayingPeriod();
                if (periodHolder != null) {
                    while (periodHolder.getNext() != null) {
                        periodHolder = periodHolder.getNext();
                        if (periodHolder.info.id.equals(newPeriodId)) {
                            periodHolder.info = this.queue.getUpdatedMediaPeriodInfo(periodHolder.info);
                        }
                    }
                }
                long newPositionUs = newPeriodId.isAd() ? 0L : newContentPositionUs;
                long seekedToPositionUs = seekToPeriodPosition(newPeriodId, newPositionUs);
                long newPositionUs2 = newContentPositionUs;
                this.playbackInfo = copyWithNewPosition(newPeriodId, seekedToPositionUs, newPositionUs2);
            }
            handleLoadingMediaPeriodChanged(false);
        }
    }

    private long getMaxRendererReadPositionUs() {
        MediaPeriodHolder readingHolder = this.queue.getReadingPeriod();
        if (readingHolder == null) {
            return 0L;
        }
        long maxReadPositionUs = readingHolder.getRendererOffset();
        if (!readingHolder.prepared) {
            return maxReadPositionUs;
        }
        int i = 0;
        while (true) {
            Renderer[] rendererArr = this.renderers;
            if (i < rendererArr.length) {
                if (rendererArr[i].getState() != 0 && this.renderers[i].getStream() == readingHolder.sampleStreams[i]) {
                    long readingPositionUs = this.renderers[i].getReadingPositionUs();
                    if (readingPositionUs == Long.MIN_VALUE) {
                        return Long.MIN_VALUE;
                    }
                    maxReadPositionUs = Math.max(readingPositionUs, maxReadPositionUs);
                }
                i++;
            } else {
                return maxReadPositionUs;
            }
        }
    }

    private void handleSourceInfoRefreshEndedPlayback() {
        if (this.playbackInfo.playbackState != 1) {
            setState(4);
        }
        resetInternal(false, false, true, false, true);
    }

    private Object resolveSubsequentPeriod(Object oldPeriodUid, Timeline oldTimeline, Timeline newTimeline) {
        int oldPeriodIndex = oldTimeline.getIndexOfPeriod(oldPeriodUid);
        int newPeriodIndex = -1;
        int maxIterations = oldTimeline.getPeriodCount();
        for (int i = 0; i < maxIterations && newPeriodIndex == -1; i++) {
            oldPeriodIndex = oldTimeline.getNextPeriodIndex(oldPeriodIndex, this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
            if (oldPeriodIndex == -1) {
                break;
            }
            newPeriodIndex = newTimeline.getIndexOfPeriod(oldTimeline.getUidOfPeriod(oldPeriodIndex));
        }
        if (newPeriodIndex == -1) {
            return null;
        }
        return newTimeline.getUidOfPeriod(newPeriodIndex);
    }

    private Pair<Object, Long> resolveSeekPosition(SeekPosition seekPosition, boolean trySubsequentPeriods) {
        Object periodUid;
        Timeline timeline = this.playbackInfo.timeline;
        Timeline seekTimeline = seekPosition.timeline;
        if (timeline.isEmpty()) {
            return null;
        }
        if (seekTimeline.isEmpty()) {
            seekTimeline = timeline;
        }
        try {
            Pair<Object, Long> periodPosition = seekTimeline.getPeriodPosition(this.window, this.period, seekPosition.windowIndex, seekPosition.windowPositionUs);
            if (timeline == seekTimeline) {
                return periodPosition;
            }
            int periodIndex = timeline.getIndexOfPeriod(periodPosition.first);
            if (periodIndex != -1) {
                return periodPosition;
            }
            if (trySubsequentPeriods && (periodUid = resolveSubsequentPeriod(periodPosition.first, seekTimeline, timeline)) != null) {
                return getPeriodPosition(timeline, timeline.getPeriodByUid(periodUid, this.period).windowIndex, C.TIME_UNSET);
            }
            return null;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private Pair<Object, Long> getPeriodPosition(Timeline timeline, int windowIndex, long windowPositionUs) {
        return timeline.getPeriodPosition(this.window, this.period, windowIndex, windowPositionUs);
    }

    private void updatePeriods() throws ExoPlaybackException, IOException {
        MediaSource mediaSource = this.mediaSource;
        if (mediaSource == null) {
            return;
        }
        if (this.pendingPrepareCount > 0) {
            mediaSource.maybeThrowSourceInfoRefreshError();
            return;
        }
        maybeUpdateLoadingPeriod();
        maybeUpdateReadingPeriod();
        maybeUpdatePlayingPeriod();
    }

    private void maybeUpdateLoadingPeriod() throws ExoPlaybackException, IOException {
        this.queue.reevaluateBuffer(this.rendererPositionUs);
        if (this.queue.shouldLoadNextMediaPeriod()) {
            MediaPeriodInfo info = this.queue.getNextMediaPeriodInfo(this.rendererPositionUs, this.playbackInfo);
            if (info == null) {
                maybeThrowSourceInfoRefreshError();
            } else {
                MediaPeriodHolder mediaPeriodHolder = this.queue.enqueueNextMediaPeriodHolder(this.rendererCapabilities, this.trackSelector, this.loadControl.getAllocator(), this.mediaSource, info, this.emptyTrackSelectorResult);
                mediaPeriodHolder.mediaPeriod.prepare(this, info.startPositionUs);
                if (this.queue.getPlayingPeriod() == mediaPeriodHolder) {
                    resetRendererPosition(mediaPeriodHolder.getStartPositionRendererTime());
                }
                handleLoadingMediaPeriodChanged(false);
            }
        }
        if (this.shouldContinueLoading) {
            this.shouldContinueLoading = isLoadingPossible();
            updateIsLoading();
            return;
        }
        maybeContinueLoading();
    }

    private void maybeUpdateReadingPeriod() throws ExoPlaybackException {
        MediaPeriodHolder readingPeriodHolder = this.queue.getReadingPeriod();
        if (readingPeriodHolder == null) {
            return;
        }
        if (readingPeriodHolder.getNext() == null) {
            if (readingPeriodHolder.info.isFinal) {
                int i = 0;
                while (true) {
                    Renderer[] rendererArr = this.renderers;
                    if (i < rendererArr.length) {
                        Renderer renderer = rendererArr[i];
                        SampleStream sampleStream = readingPeriodHolder.sampleStreams[i];
                        if (sampleStream != null && renderer.getStream() == sampleStream && renderer.hasReadStreamToEnd()) {
                            renderer.setCurrentStreamFinal();
                        }
                        i++;
                    } else {
                        return;
                    }
                }
            }
        } else if (!hasReadingPeriodFinishedReading() || !readingPeriodHolder.getNext().prepared) {
        } else {
            TrackSelectorResult oldTrackSelectorResult = readingPeriodHolder.getTrackSelectorResult();
            MediaPeriodHolder readingPeriodHolder2 = this.queue.advanceReadingPeriod();
            TrackSelectorResult newTrackSelectorResult = readingPeriodHolder2.getTrackSelectorResult();
            if (readingPeriodHolder2.mediaPeriod.readDiscontinuity() != C.TIME_UNSET) {
                setAllRendererStreamsFinal();
                return;
            }
            int i2 = 0;
            while (true) {
                Renderer[] rendererArr2 = this.renderers;
                if (i2 < rendererArr2.length) {
                    Renderer renderer2 = rendererArr2[i2];
                    boolean rendererWasEnabled = oldTrackSelectorResult.isRendererEnabled(i2);
                    if (rendererWasEnabled && !renderer2.isCurrentStreamFinal()) {
                        TrackSelection newSelection = newTrackSelectorResult.selections.get(i2);
                        boolean newRendererEnabled = newTrackSelectorResult.isRendererEnabled(i2);
                        boolean isNoSampleRenderer = this.rendererCapabilities[i2].getTrackType() == 6;
                        RendererConfiguration oldConfig = oldTrackSelectorResult.rendererConfigurations[i2];
                        RendererConfiguration newConfig = newTrackSelectorResult.rendererConfigurations[i2];
                        if (newRendererEnabled && newConfig.equals(oldConfig) && !isNoSampleRenderer) {
                            Format[] formats = getFormats(newSelection);
                            renderer2.replaceStream(formats, readingPeriodHolder2.sampleStreams[i2], readingPeriodHolder2.getRendererOffset());
                        } else {
                            renderer2.setCurrentStreamFinal();
                        }
                    }
                    i2++;
                } else {
                    return;
                }
            }
        }
    }

    private void maybeUpdatePlayingPeriod() throws ExoPlaybackException {
        int discontinuityReason;
        boolean advancedPlayingPeriod = false;
        while (shouldAdvancePlayingPeriod()) {
            if (advancedPlayingPeriod) {
                maybeNotifyPlaybackInfoChanged();
            }
            MediaPeriodHolder oldPlayingPeriodHolder = this.queue.getPlayingPeriod();
            if (oldPlayingPeriodHolder == this.queue.getReadingPeriod()) {
                setAllRendererStreamsFinal();
            }
            MediaPeriodHolder newPlayingPeriodHolder = this.queue.advancePlayingPeriod();
            updatePlayingPeriodRenderers(oldPlayingPeriodHolder);
            this.playbackInfo = copyWithNewPosition(newPlayingPeriodHolder.info.id, newPlayingPeriodHolder.info.startPositionUs, newPlayingPeriodHolder.info.contentPositionUs);
            if (oldPlayingPeriodHolder.info.isLastInTimelinePeriod) {
                discontinuityReason = 0;
            } else {
                discontinuityReason = 3;
            }
            this.playbackInfoUpdate.setPositionDiscontinuity(discontinuityReason);
            updatePlaybackPositions();
            advancedPlayingPeriod = true;
        }
    }

    private boolean shouldAdvancePlayingPeriod() {
        MediaPeriodHolder playingPeriodHolder;
        MediaPeriodHolder nextPlayingPeriodHolder;
        if (!this.playWhenReady || (playingPeriodHolder = this.queue.getPlayingPeriod()) == null || (nextPlayingPeriodHolder = playingPeriodHolder.getNext()) == null) {
            return false;
        }
        MediaPeriodHolder readingPeriodHolder = this.queue.getReadingPeriod();
        return (playingPeriodHolder != readingPeriodHolder || hasReadingPeriodFinishedReading()) && this.rendererPositionUs >= nextPlayingPeriodHolder.getStartPositionRendererTime();
    }

    private boolean hasReadingPeriodFinishedReading() {
        MediaPeriodHolder readingPeriodHolder = this.queue.getReadingPeriod();
        if (!readingPeriodHolder.prepared) {
            return false;
        }
        int i = 0;
        while (true) {
            Renderer[] rendererArr = this.renderers;
            if (i < rendererArr.length) {
                Renderer renderer = rendererArr[i];
                SampleStream sampleStream = readingPeriodHolder.sampleStreams[i];
                if (renderer.getStream() != sampleStream || (sampleStream != null && !renderer.hasReadStreamToEnd())) {
                    break;
                }
                i++;
            } else {
                return true;
            }
        }
        return false;
    }

    private void setAllRendererStreamsFinal() {
        Renderer[] rendererArr;
        for (Renderer renderer : this.renderers) {
            if (renderer.getStream() != null) {
                renderer.setCurrentStreamFinal();
            }
        }
    }

    private void handlePeriodPrepared(MediaPeriod mediaPeriod) throws ExoPlaybackException {
        if (!this.queue.isLoading(mediaPeriod)) {
            return;
        }
        MediaPeriodHolder loadingPeriodHolder = this.queue.getLoadingPeriod();
        loadingPeriodHolder.handlePrepared(this.mediaClock.getPlaybackParameters().speed, this.playbackInfo.timeline);
        updateLoadControlTrackSelection(loadingPeriodHolder.getTrackGroups(), loadingPeriodHolder.getTrackSelectorResult());
        if (loadingPeriodHolder == this.queue.getPlayingPeriod()) {
            resetRendererPosition(loadingPeriodHolder.info.startPositionUs);
            updatePlayingPeriodRenderers(null);
        }
        maybeContinueLoading();
    }

    private void handleContinueLoadingRequested(MediaPeriod mediaPeriod) {
        if (!this.queue.isLoading(mediaPeriod)) {
            return;
        }
        this.queue.reevaluateBuffer(this.rendererPositionUs);
        maybeContinueLoading();
    }

    private void handlePlaybackParameters(PlaybackParameters playbackParameters, boolean acknowledgeCommand) throws ExoPlaybackException {
        Renderer[] rendererArr;
        this.eventHandler.obtainMessage(1, acknowledgeCommand ? 1 : 0, 0, playbackParameters).sendToTarget();
        updateTrackSelectionPlaybackSpeed(playbackParameters.speed);
        for (Renderer renderer : this.renderers) {
            if (renderer != null) {
                renderer.setOperatingRate(playbackParameters.speed);
            }
        }
    }

    private void maybeContinueLoading() {
        boolean shouldContinueLoading = shouldContinueLoading();
        this.shouldContinueLoading = shouldContinueLoading;
        if (shouldContinueLoading) {
            this.queue.getLoadingPeriod().continueLoading(this.rendererPositionUs);
        }
        updateIsLoading();
    }

    private boolean shouldContinueLoading() {
        if (!isLoadingPossible()) {
            return false;
        }
        long bufferedDurationUs = getTotalBufferedDurationUs(this.queue.getLoadingPeriod().getNextLoadPositionUs());
        float playbackSpeed = this.mediaClock.getPlaybackParameters().speed;
        return this.loadControl.shouldContinueLoading(bufferedDurationUs, playbackSpeed);
    }

    private boolean isLoadingPossible() {
        MediaPeriodHolder loadingPeriodHolder = this.queue.getLoadingPeriod();
        if (loadingPeriodHolder == null) {
            return false;
        }
        long nextLoadPositionUs = loadingPeriodHolder.getNextLoadPositionUs();
        if (nextLoadPositionUs == Long.MIN_VALUE) {
            return false;
        }
        return true;
    }

    private void updateIsLoading() {
        MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        boolean isLoading = this.shouldContinueLoading || (loadingPeriod != null && loadingPeriod.mediaPeriod.isLoading());
        if (isLoading != this.playbackInfo.isLoading) {
            this.playbackInfo = this.playbackInfo.copyWithIsLoading(isLoading);
        }
    }

    private PlaybackInfo copyWithNewPosition(MediaSource.MediaPeriodId mediaPeriodId, long positionUs, long contentPositionUs) {
        this.deliverPendingMessageAtStartPositionRequired = true;
        return this.playbackInfo.copyWithNewPosition(mediaPeriodId, positionUs, contentPositionUs, getTotalBufferedDurationUs());
    }

    private void updatePlayingPeriodRenderers(MediaPeriodHolder oldPlayingPeriodHolder) throws ExoPlaybackException {
        MediaPeriodHolder newPlayingPeriodHolder = this.queue.getPlayingPeriod();
        if (newPlayingPeriodHolder == null || oldPlayingPeriodHolder == newPlayingPeriodHolder) {
            return;
        }
        int enabledRendererCount = 0;
        boolean[] rendererWasEnabledFlags = new boolean[this.renderers.length];
        int i = 0;
        while (true) {
            Renderer[] rendererArr = this.renderers;
            if (i < rendererArr.length) {
                Renderer renderer = rendererArr[i];
                rendererWasEnabledFlags[i] = renderer.getState() != 0;
                if (newPlayingPeriodHolder.getTrackSelectorResult().isRendererEnabled(i)) {
                    enabledRendererCount++;
                }
                if (rendererWasEnabledFlags[i] && (!newPlayingPeriodHolder.getTrackSelectorResult().isRendererEnabled(i) || (renderer.isCurrentStreamFinal() && renderer.getStream() == oldPlayingPeriodHolder.sampleStreams[i]))) {
                    disableRenderer(renderer);
                }
                i++;
            } else {
                this.playbackInfo = this.playbackInfo.copyWithTrackInfo(newPlayingPeriodHolder.getTrackGroups(), newPlayingPeriodHolder.getTrackSelectorResult());
                enableRenderers(rendererWasEnabledFlags, enabledRendererCount);
                return;
            }
        }
    }

    private void enableRenderers(boolean[] rendererWasEnabledFlags, int totalEnabledRendererCount) throws ExoPlaybackException {
        this.enabledRenderers = new Renderer[totalEnabledRendererCount];
        int enabledRendererCount = 0;
        TrackSelectorResult trackSelectorResult = this.queue.getPlayingPeriod().getTrackSelectorResult();
        for (int i = 0; i < this.renderers.length; i++) {
            if (!trackSelectorResult.isRendererEnabled(i)) {
                this.renderers[i].reset();
            }
        }
        for (int i2 = 0; i2 < this.renderers.length; i2++) {
            if (trackSelectorResult.isRendererEnabled(i2)) {
                enableRenderer(i2, rendererWasEnabledFlags[i2], enabledRendererCount);
                enabledRendererCount++;
            }
        }
    }

    private void enableRenderer(int rendererIndex, boolean wasRendererEnabled, int enabledRendererIndex) throws ExoPlaybackException {
        MediaPeriodHolder playingPeriodHolder = this.queue.getPlayingPeriod();
        Renderer renderer = this.renderers[rendererIndex];
        this.enabledRenderers[enabledRendererIndex] = renderer;
        if (renderer.getState() == 0) {
            TrackSelectorResult trackSelectorResult = playingPeriodHolder.getTrackSelectorResult();
            RendererConfiguration rendererConfiguration = trackSelectorResult.rendererConfigurations[rendererIndex];
            TrackSelection newSelection = trackSelectorResult.selections.get(rendererIndex);
            Format[] formats = getFormats(newSelection);
            boolean playing = this.playWhenReady && this.playbackInfo.playbackState == 3;
            boolean joining = !wasRendererEnabled && playing;
            renderer.enable(rendererConfiguration, formats, playingPeriodHolder.sampleStreams[rendererIndex], this.rendererPositionUs, joining, playingPeriodHolder.getRendererOffset());
            this.mediaClock.onRendererEnabled(renderer);
            if (playing) {
                renderer.start();
            }
        }
    }

    private void handleLoadingMediaPeriodChanged(boolean loadingTrackSelectionChanged) {
        long j;
        MediaPeriodHolder loadingMediaPeriodHolder = this.queue.getLoadingPeriod();
        MediaSource.MediaPeriodId loadingMediaPeriodId = loadingMediaPeriodHolder == null ? this.playbackInfo.periodId : loadingMediaPeriodHolder.info.id;
        boolean loadingMediaPeriodChanged = !this.playbackInfo.loadingMediaPeriodId.equals(loadingMediaPeriodId);
        if (loadingMediaPeriodChanged) {
            this.playbackInfo = this.playbackInfo.copyWithLoadingMediaPeriodId(loadingMediaPeriodId);
        }
        PlaybackInfo playbackInfo = this.playbackInfo;
        if (loadingMediaPeriodHolder == null) {
            j = playbackInfo.positionUs;
        } else {
            j = loadingMediaPeriodHolder.getBufferedPositionUs();
        }
        playbackInfo.bufferedPositionUs = j;
        this.playbackInfo.totalBufferedDurationUs = getTotalBufferedDurationUs();
        if ((loadingMediaPeriodChanged || loadingTrackSelectionChanged) && loadingMediaPeriodHolder != null && loadingMediaPeriodHolder.prepared) {
            updateLoadControlTrackSelection(loadingMediaPeriodHolder.getTrackGroups(), loadingMediaPeriodHolder.getTrackSelectorResult());
        }
    }

    private long getTotalBufferedDurationUs() {
        return getTotalBufferedDurationUs(this.playbackInfo.bufferedPositionUs);
    }

    private long getTotalBufferedDurationUs(long bufferedPositionInLoadingPeriodUs) {
        MediaPeriodHolder loadingPeriodHolder = this.queue.getLoadingPeriod();
        if (loadingPeriodHolder == null) {
            return 0L;
        }
        long totalBufferedDurationUs = bufferedPositionInLoadingPeriodUs - loadingPeriodHolder.toPeriodTime(this.rendererPositionUs);
        return Math.max(0L, totalBufferedDurationUs);
    }

    private void updateLoadControlTrackSelection(TrackGroupArray trackGroups, TrackSelectorResult trackSelectorResult) {
        this.loadControl.onTracksSelected(this.renderers, trackGroups, trackSelectorResult.selections);
    }

    private void sendPlaybackParametersChangedInternal(PlaybackParameters playbackParameters, boolean acknowledgeCommand) {
        this.handler.obtainMessage(17, acknowledgeCommand ? 1 : 0, 0, playbackParameters).sendToTarget();
    }

    private static Format[] getFormats(TrackSelection newSelection) {
        int length = newSelection != null ? newSelection.length() : 0;
        Format[] formats = new Format[length];
        for (int i = 0; i < length; i++) {
            formats[i] = newSelection.getFormat(i);
        }
        return formats;
    }

    /* loaded from: classes3.dex */
    public static final class SeekPosition {
        public final Timeline timeline;
        public final int windowIndex;
        public final long windowPositionUs;

        public SeekPosition(Timeline timeline, int windowIndex, long windowPositionUs) {
            this.timeline = timeline;
            this.windowIndex = windowIndex;
            this.windowPositionUs = windowPositionUs;
        }
    }

    /* loaded from: classes3.dex */
    public static final class PendingMessageInfo implements Comparable<PendingMessageInfo> {
        public final PlayerMessage message;
        public int resolvedPeriodIndex;
        public long resolvedPeriodTimeUs;
        public Object resolvedPeriodUid;

        public PendingMessageInfo(PlayerMessage message) {
            this.message = message;
        }

        public void setResolvedPosition(int periodIndex, long periodTimeUs, Object periodUid) {
            this.resolvedPeriodIndex = periodIndex;
            this.resolvedPeriodTimeUs = periodTimeUs;
            this.resolvedPeriodUid = periodUid;
        }

        public int compareTo(PendingMessageInfo other) {
            Object obj = this.resolvedPeriodUid;
            if ((obj == null) != (other.resolvedPeriodUid == null)) {
                return obj != null ? -1 : 1;
            } else if (obj == null) {
                return 0;
            } else {
                int comparePeriodIndex = this.resolvedPeriodIndex - other.resolvedPeriodIndex;
                if (comparePeriodIndex != 0) {
                    return comparePeriodIndex;
                }
                return Util.compareLong(this.resolvedPeriodTimeUs, other.resolvedPeriodTimeUs);
            }
        }
    }

    /* loaded from: classes3.dex */
    public static final class MediaSourceRefreshInfo {
        public final MediaSource source;
        public final Timeline timeline;

        public MediaSourceRefreshInfo(MediaSource source, Timeline timeline) {
            this.source = source;
            this.timeline = timeline;
        }
    }

    /* loaded from: classes3.dex */
    public static final class PlaybackInfoUpdate {
        private int discontinuityReason;
        private PlaybackInfo lastPlaybackInfo;
        private int operationAcks;
        private boolean positionDiscontinuity;

        private PlaybackInfoUpdate() {
        }

        public boolean hasPendingUpdate(PlaybackInfo playbackInfo) {
            return playbackInfo != this.lastPlaybackInfo || this.operationAcks > 0 || this.positionDiscontinuity;
        }

        public void reset(PlaybackInfo playbackInfo) {
            this.lastPlaybackInfo = playbackInfo;
            this.operationAcks = 0;
            this.positionDiscontinuity = false;
        }

        public void incrementPendingOperationAcks(int operationAcks) {
            this.operationAcks += operationAcks;
        }

        public void setPositionDiscontinuity(int discontinuityReason) {
            boolean z = true;
            if (this.positionDiscontinuity && this.discontinuityReason != 4) {
                if (discontinuityReason != 4) {
                    z = false;
                }
                Assertions.checkArgument(z);
                return;
            }
            this.positionDiscontinuity = true;
            this.discontinuityReason = discontinuityReason;
        }
    }
}
