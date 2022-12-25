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
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ExoPlayerImplInternal implements Handler.Callback, MediaPeriod.Callback, TrackSelector.InvalidationListener, MediaSource.MediaSourceCaller, DefaultMediaClock.PlaybackParameterListener, PlayerMessage.Sender {
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

    public ExoPlayerImplInternal(Renderer[] rendererArr, TrackSelector trackSelector, TrackSelectorResult trackSelectorResult, LoadControl loadControl, BandwidthMeter bandwidthMeter, boolean z, int i, boolean z2, Handler handler, Clock clock) {
        this.renderers = rendererArr;
        this.trackSelector = trackSelector;
        this.emptyTrackSelectorResult = trackSelectorResult;
        this.loadControl = loadControl;
        this.bandwidthMeter = bandwidthMeter;
        this.playWhenReady = z;
        this.repeatMode = i;
        this.shuffleModeEnabled = z2;
        this.eventHandler = handler;
        this.clock = clock;
        this.backBufferDurationUs = loadControl.getBackBufferDurationUs();
        this.retainBackBufferFromKeyframe = loadControl.retainBackBufferFromKeyframe();
        this.playbackInfo = PlaybackInfo.createDummy(-9223372036854775807L, trackSelectorResult);
        this.rendererCapabilities = new RendererCapabilities[rendererArr.length];
        for (int i2 = 0; i2 < rendererArr.length; i2++) {
            rendererArr[i2].setIndex(i2);
            this.rendererCapabilities[i2] = rendererArr[i2].getCapabilities();
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

    public void prepare(MediaSource mediaSource, boolean z, boolean z2) {
        this.handler.obtainMessage(0, z ? 1 : 0, z2 ? 1 : 0, mediaSource).sendToTarget();
    }

    public void setPlayWhenReady(boolean z) {
        this.handler.obtainMessage(1, z ? 1 : 0, 0).sendToTarget();
    }

    public void setRepeatMode(int i) {
        this.handler.obtainMessage(12, i, 0).sendToTarget();
    }

    public void seekTo(Timeline timeline, int i, long j) {
        this.handler.obtainMessage(3, new SeekPosition(timeline, i, j)).sendToTarget();
    }

    public void setPlaybackParameters(PlaybackParameters playbackParameters) {
        this.handler.obtainMessage(4, playbackParameters).sendToTarget();
    }

    @Override // com.google.android.exoplayer2.PlayerMessage.Sender
    public synchronized void sendMessage(PlayerMessage playerMessage) {
        if (!this.released && this.internalPlaybackThread.isAlive()) {
            this.handler.obtainMessage(15, playerMessage).sendToTarget();
            return;
        }
        Log.w("ExoPlayerImplInternal", "Ignoring messages sent after release.");
        playerMessage.markAsProcessed(false);
    }

    public synchronized void release() {
        if (!this.released && this.internalPlaybackThread.isAlive()) {
            this.handler.sendEmptyMessage(7);
            boolean z = false;
            while (!this.released) {
                try {
                    wait();
                } catch (InterruptedException unused) {
                    z = true;
                }
            }
            if (z) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public Looper getPlaybackLooper() {
        return this.internalPlaybackThread.getLooper();
    }

    @Override // com.google.android.exoplayer2.source.MediaSource.MediaSourceCaller
    public void onSourceInfoRefreshed(MediaSource mediaSource, Timeline timeline) {
        this.handler.obtainMessage(8, new MediaSourceRefreshInfo(mediaSource, timeline)).sendToTarget();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod.Callback
    public void onPrepared(MediaPeriod mediaPeriod) {
        this.handler.obtainMessage(9, mediaPeriod).sendToTarget();
    }

    @Override // com.google.android.exoplayer2.source.SequenceableLoader.Callback
    public void onContinueLoadingRequested(MediaPeriod mediaPeriod) {
        this.handler.obtainMessage(10, mediaPeriod).sendToTarget();
    }

    @Override // com.google.android.exoplayer2.DefaultMediaClock.PlaybackParameterListener
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        sendPlaybackParametersChangedInternal(playbackParameters, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:60:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00ce  */
    @Override // android.os.Handler.Callback
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean handleMessage(Message message) {
        ExoPlaybackException createForUnexpected;
        try {
            switch (message.what) {
                case 0:
                    prepareInternal((MediaSource) message.obj, message.arg1 != 0, message.arg2 != 0);
                    break;
                case 1:
                    setPlayWhenReadyInternal(message.arg1 != 0);
                    break;
                case 2:
                    doSomeWork();
                    break;
                case 3:
                    seekToInternal((SeekPosition) message.obj);
                    break;
                case 4:
                    setPlaybackParametersInternal((PlaybackParameters) message.obj);
                    break;
                case 5:
                    setSeekParametersInternal((SeekParameters) message.obj);
                    break;
                case 6:
                    stopInternal(false, message.arg1 != 0, true);
                    break;
                case 7:
                    releaseInternal();
                    return true;
                case 8:
                    handleSourceInfoRefreshed((MediaSourceRefreshInfo) message.obj);
                    break;
                case 9:
                    handlePeriodPrepared((MediaPeriod) message.obj);
                    break;
                case 10:
                    handleContinueLoadingRequested((MediaPeriod) message.obj);
                    break;
                case 11:
                    reselectTracksInternal();
                    break;
                case 12:
                    setRepeatModeInternal(message.arg1);
                    break;
                case 13:
                    setShuffleModeEnabledInternal(message.arg1 != 0);
                    break;
                case 14:
                    setForegroundModeInternal(message.arg1 != 0, (AtomicBoolean) message.obj);
                    break;
                case 15:
                    sendMessageInternal((PlayerMessage) message.obj);
                    break;
                case 16:
                    sendMessageToTargetThread((PlayerMessage) message.obj);
                    break;
                case 17:
                    handlePlaybackParameters((PlaybackParameters) message.obj, message.arg1 != 0);
                    break;
                default:
                    return false;
            }
            maybeNotifyPlaybackInfoChanged();
        } catch (ExoPlaybackException e) {
            Log.e("ExoPlayerImplInternal", getExoPlaybackExceptionMessage(e), e);
            stopInternal(true, false, false);
            this.playbackInfo = this.playbackInfo.copyWithPlaybackError(e);
            maybeNotifyPlaybackInfoChanged();
        } catch (IOException e2) {
            Log.e("ExoPlayerImplInternal", "Source error", e2);
            stopInternal(false, false, false);
            this.playbackInfo = this.playbackInfo.copyWithPlaybackError(ExoPlaybackException.createForSource(e2));
            maybeNotifyPlaybackInfoChanged();
        } catch (OutOfMemoryError e3) {
            e = e3;
            Log.e("ExoPlayerImplInternal", "Internal runtime error", e);
            if (!(e instanceof OutOfMemoryError)) {
                createForUnexpected = ExoPlaybackException.createForOutOfMemoryError((OutOfMemoryError) e);
            } else {
                createForUnexpected = ExoPlaybackException.createForUnexpected((RuntimeException) e);
            }
            stopInternal(true, false, false);
            this.playbackInfo = this.playbackInfo.copyWithPlaybackError(createForUnexpected);
            maybeNotifyPlaybackInfoChanged();
        } catch (RuntimeException e4) {
            e = e4;
            Log.e("ExoPlayerImplInternal", "Internal runtime error", e);
            if (!(e instanceof OutOfMemoryError)) {
            }
            stopInternal(true, false, false);
            this.playbackInfo = this.playbackInfo.copyWithPlaybackError(createForUnexpected);
            maybeNotifyPlaybackInfoChanged();
        }
        return true;
    }

    private String getExoPlaybackExceptionMessage(ExoPlaybackException exoPlaybackException) {
        if (exoPlaybackException.type != 1) {
            return "Playback error.";
        }
        return "Renderer error: index=" + exoPlaybackException.rendererIndex + ", type=" + Util.getTrackTypeString(this.renderers[exoPlaybackException.rendererIndex].getTrackType()) + ", format=" + exoPlaybackException.rendererFormat + ", rendererSupport=" + RendererCapabilities.-CC.getFormatSupportString(exoPlaybackException.rendererFormatSupport);
    }

    private void setState(int i) {
        PlaybackInfo playbackInfo = this.playbackInfo;
        if (playbackInfo.playbackState != i) {
            this.playbackInfo = playbackInfo.copyWithPlaybackState(i);
        }
    }

    private void maybeNotifyPlaybackInfoChanged() {
        if (this.playbackInfoUpdate.hasPendingUpdate(this.playbackInfo)) {
            this.eventHandler.obtainMessage(0, this.playbackInfoUpdate.operationAcks, this.playbackInfoUpdate.positionDiscontinuity ? this.playbackInfoUpdate.discontinuityReason : -1, this.playbackInfo).sendToTarget();
            this.playbackInfoUpdate.reset(this.playbackInfo);
        }
    }

    private void prepareInternal(MediaSource mediaSource, boolean z, boolean z2) {
        this.pendingPrepareCount++;
        resetInternal(false, true, z, z2, true);
        this.loadControl.onPrepared();
        this.mediaSource = mediaSource;
        setState(2);
        mediaSource.prepareSource(this, this.bandwidthMeter.getTransferListener());
        this.handler.sendEmptyMessage(2);
    }

    private void setPlayWhenReadyInternal(boolean z) throws ExoPlaybackException {
        this.rebuffering = false;
        this.playWhenReady = z;
        if (!z) {
            stopRenderers();
            updatePlaybackPositions();
            return;
        }
        int i = this.playbackInfo.playbackState;
        if (i == 3) {
            startRenderers();
            this.handler.sendEmptyMessage(2);
        } else if (i == 2) {
            this.handler.sendEmptyMessage(2);
        }
    }

    private void setRepeatModeInternal(int i) throws ExoPlaybackException {
        this.repeatMode = i;
        if (!this.queue.updateRepeatMode(i)) {
            seekToCurrentPosition(true);
        }
        handleLoadingMediaPeriodChanged(false);
    }

    private void setShuffleModeEnabledInternal(boolean z) throws ExoPlaybackException {
        this.shuffleModeEnabled = z;
        if (!this.queue.updateShuffleModeEnabled(z)) {
            seekToCurrentPosition(true);
        }
        handleLoadingMediaPeriodChanged(false);
    }

    private void seekToCurrentPosition(boolean z) throws ExoPlaybackException {
        MediaSource.MediaPeriodId mediaPeriodId = this.queue.getPlayingPeriod().info.id;
        long seekToPeriodPosition = seekToPeriodPosition(mediaPeriodId, this.playbackInfo.positionUs, true);
        if (seekToPeriodPosition != this.playbackInfo.positionUs) {
            this.playbackInfo = copyWithNewPosition(mediaPeriodId, seekToPeriodPosition, this.playbackInfo.contentPositionUs);
            if (z) {
                this.playbackInfoUpdate.setPositionDiscontinuity(4);
            }
        }
    }

    private void startRenderers() throws ExoPlaybackException {
        this.rebuffering = false;
        this.mediaClock.start();
        for (Renderer renderer : this.enabledRenderers) {
            renderer.start();
        }
    }

    private void stopRenderers() throws ExoPlaybackException {
        this.mediaClock.stop();
        for (Renderer renderer : this.enabledRenderers) {
            ensureStopped(renderer);
        }
    }

    private void updatePlaybackPositions() throws ExoPlaybackException {
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        if (playingPeriod == null) {
            return;
        }
        long readDiscontinuity = playingPeriod.prepared ? playingPeriod.mediaPeriod.readDiscontinuity() : -9223372036854775807L;
        if (readDiscontinuity != -9223372036854775807L) {
            resetRendererPosition(readDiscontinuity);
            if (readDiscontinuity != this.playbackInfo.positionUs) {
                PlaybackInfo playbackInfo = this.playbackInfo;
                this.playbackInfo = copyWithNewPosition(playbackInfo.periodId, readDiscontinuity, playbackInfo.contentPositionUs);
                this.playbackInfoUpdate.setPositionDiscontinuity(4);
            }
        } else {
            long syncAndGetPositionUs = this.mediaClock.syncAndGetPositionUs(playingPeriod != this.queue.getReadingPeriod());
            this.rendererPositionUs = syncAndGetPositionUs;
            long periodTime = playingPeriod.toPeriodTime(syncAndGetPositionUs);
            maybeTriggerPendingMessages(this.playbackInfo.positionUs, periodTime);
            this.playbackInfo.positionUs = periodTime;
        }
        this.playbackInfo.bufferedPositionUs = this.queue.getLoadingPeriod().getBufferedPositionUs();
        this.playbackInfo.totalBufferedDurationUs = getTotalBufferedDurationUs();
    }

    /* JADX WARN: Removed duplicated region for block: B:84:0x011d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void doSomeWork() throws ExoPlaybackException, IOException {
        boolean z;
        boolean z2;
        int i;
        int i2;
        long uptimeMillis = this.clock.uptimeMillis();
        updatePeriods();
        int i3 = this.playbackInfo.playbackState;
        if (i3 == 1 || i3 == 4) {
            this.handler.removeMessages(2);
            return;
        }
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        if (playingPeriod == null) {
            scheduleNextWork(uptimeMillis, 10L);
            return;
        }
        TraceUtil.beginSection("doSomeWork");
        updatePlaybackPositions();
        if (playingPeriod.prepared) {
            long elapsedRealtime = SystemClock.elapsedRealtime() * 1000;
            playingPeriod.mediaPeriod.discardBuffer(this.playbackInfo.positionUs - this.backBufferDurationUs, this.retainBackBufferFromKeyframe);
            int i4 = 0;
            boolean z3 = true;
            boolean z4 = true;
            while (true) {
                Renderer[] rendererArr = this.renderers;
                if (i4 >= rendererArr.length) {
                    break;
                }
                Renderer renderer = rendererArr[i4];
                if (renderer.getState() != 0) {
                    renderer.render(this.rendererPositionUs, elapsedRealtime);
                    z3 = z3 && renderer.isEnded();
                    boolean z5 = playingPeriod.sampleStreams[i4] != renderer.getStream();
                    boolean z6 = z5 || (!z5 && playingPeriod.getNext() != null && renderer.hasReadStreamToEnd()) || renderer.isReady() || renderer.isEnded();
                    z4 = z4 && z6;
                    if (!z6) {
                        renderer.maybeThrowStreamError();
                    }
                }
                i4++;
            }
            z = z3;
            z2 = z4;
        } else {
            playingPeriod.mediaPeriod.maybeThrowPrepareError();
            z = true;
            z2 = true;
        }
        long j = playingPeriod.info.durationUs;
        if (z && playingPeriod.prepared && ((j == -9223372036854775807L || j <= this.playbackInfo.positionUs) && playingPeriod.info.isFinal)) {
            setState(4);
            stopRenderers();
        } else if (this.playbackInfo.playbackState == 2 && shouldTransitionToReadyState(z2)) {
            setState(3);
            if (this.playWhenReady) {
                startRenderers();
            }
        } else if (this.playbackInfo.playbackState == 3 && (this.enabledRenderers.length != 0 ? !z2 : !isTimelineReady())) {
            this.rebuffering = this.playWhenReady;
            i = 2;
            setState(2);
            stopRenderers();
            if (this.playbackInfo.playbackState == i) {
                for (Renderer renderer2 : this.enabledRenderers) {
                    renderer2.maybeThrowStreamError();
                }
            }
            if ((!this.playWhenReady && this.playbackInfo.playbackState == 3) || (i2 = this.playbackInfo.playbackState) == 2) {
                scheduleNextWork(uptimeMillis, 10L);
            } else if (this.enabledRenderers.length == 0 && i2 != 4) {
                scheduleNextWork(uptimeMillis, 1000L);
            } else {
                this.handler.removeMessages(2);
            }
            TraceUtil.endSection();
        }
        i = 2;
        if (this.playbackInfo.playbackState == i) {
        }
        if (!this.playWhenReady) {
        }
        if (this.enabledRenderers.length == 0) {
        }
        this.handler.removeMessages(2);
        TraceUtil.endSection();
    }

    private void scheduleNextWork(long j, long j2) {
        this.handler.removeMessages(2);
        this.handler.sendEmptyMessageAtTime(2, j + j2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00e1  */
    /* JADX WARN: Removed duplicated region for block: B:68:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r8v0 */
    /* JADX WARN: Type inference failed for: r8v1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void seekToInternal(SeekPosition seekPosition) throws ExoPlaybackException {
        long longValue;
        long j;
        boolean z;
        MediaSource.MediaPeriodId mediaPeriodId;
        int i;
        long j2;
        long j3;
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
        Pair<Object, Long> resolveSeekPosition = resolveSeekPosition(seekPosition, true);
        int i2 = -9223372036854775807;
        if (resolveSeekPosition == null) {
            mediaPeriodId = this.playbackInfo.getDummyFirstMediaPeriodId(this.shuffleModeEnabled, this.window, this.period);
            longValue = -9223372036854775807L;
            j = -9223372036854775807L;
            z = true;
        } else {
            Object obj = resolveSeekPosition.first;
            long longValue2 = ((Long) resolveSeekPosition.second).longValue();
            MediaSource.MediaPeriodId resolveMediaPeriodIdForAds = this.queue.resolveMediaPeriodIdForAds(obj, longValue2);
            if (resolveMediaPeriodIdForAds.isAd()) {
                longValue = 0;
                j = longValue2;
                z = true;
            } else {
                longValue = ((Long) resolveSeekPosition.second).longValue();
                j = longValue2;
                z = seekPosition.windowPositionUs == -9223372036854775807L;
            }
            mediaPeriodId = resolveMediaPeriodIdForAds;
        }
        try {
            try {
                if (this.mediaSource != null && this.pendingPrepareCount <= 0) {
                    if (longValue == -9223372036854775807L) {
                        setState(4);
                        i = 2;
                        resetInternal(false, false, true, false, true);
                        j2 = longValue;
                        this.playbackInfo = copyWithNewPosition(mediaPeriodId, j2, j);
                        if (z) {
                            this.playbackInfoUpdate.setPositionDiscontinuity(i);
                            return;
                        }
                        return;
                    }
                    i = 2;
                    if (mediaPeriodId.equals(this.playbackInfo.periodId)) {
                        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
                        j3 = (playingPeriod == null || !playingPeriod.prepared || longValue == 0) ? longValue : playingPeriod.mediaPeriod.getAdjustedSeekPositionUs(longValue, this.seekParameters);
                        if (C.usToMs(j3) == C.usToMs(this.playbackInfo.positionUs)) {
                            this.playbackInfo = copyWithNewPosition(mediaPeriodId, this.playbackInfo.positionUs, j);
                            if (z) {
                                this.playbackInfoUpdate.setPositionDiscontinuity(2);
                                return;
                            }
                            return;
                        }
                    } else {
                        j3 = longValue;
                    }
                    long seekToPeriodPosition = seekToPeriodPosition(mediaPeriodId, j3);
                    z |= longValue != seekToPeriodPosition;
                    j2 = seekToPeriodPosition;
                    this.playbackInfo = copyWithNewPosition(mediaPeriodId, j2, j);
                    if (z) {
                    }
                }
                i = 2;
                this.pendingInitialSeekPosition = seekPosition;
                j2 = longValue;
                this.playbackInfo = copyWithNewPosition(mediaPeriodId, j2, j);
                if (z) {
                }
            } catch (Throwable th) {
                th = th;
                this.playbackInfo = copyWithNewPosition(mediaPeriodId, longValue, j);
                if (z) {
                    this.playbackInfoUpdate.setPositionDiscontinuity(i2);
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            i2 = 2;
        }
    }

    private long seekToPeriodPosition(MediaSource.MediaPeriodId mediaPeriodId, long j) throws ExoPlaybackException {
        return seekToPeriodPosition(mediaPeriodId, j, this.queue.getPlayingPeriod() != this.queue.getReadingPeriod());
    }

    private long seekToPeriodPosition(MediaSource.MediaPeriodId mediaPeriodId, long j, boolean z) throws ExoPlaybackException {
        stopRenderers();
        this.rebuffering = false;
        PlaybackInfo playbackInfo = this.playbackInfo;
        if (playbackInfo.playbackState != 1 && !playbackInfo.timeline.isEmpty()) {
            setState(2);
        }
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        MediaPeriodHolder mediaPeriodHolder = playingPeriod;
        while (true) {
            if (mediaPeriodHolder == null) {
                break;
            }
            if (mediaPeriodId.equals(mediaPeriodHolder.info.id) && mediaPeriodHolder.prepared) {
                this.queue.removeAfter(mediaPeriodHolder);
                break;
            }
            mediaPeriodHolder = this.queue.advancePlayingPeriod();
        }
        if (z || playingPeriod != mediaPeriodHolder || (mediaPeriodHolder != null && mediaPeriodHolder.toRendererTime(j) < 0)) {
            for (Renderer renderer : this.enabledRenderers) {
                disableRenderer(renderer);
            }
            this.enabledRenderers = new Renderer[0];
            playingPeriod = null;
            if (mediaPeriodHolder != null) {
                mediaPeriodHolder.setRendererOffset(0L);
            }
        }
        if (mediaPeriodHolder != null) {
            updatePlayingPeriodRenderers(playingPeriod);
            if (mediaPeriodHolder.hasEnabledTracks) {
                long seekToUs = mediaPeriodHolder.mediaPeriod.seekToUs(j);
                mediaPeriodHolder.mediaPeriod.discardBuffer(seekToUs - this.backBufferDurationUs, this.retainBackBufferFromKeyframe);
                j = seekToUs;
            }
            resetRendererPosition(j);
            maybeContinueLoading();
        } else {
            this.queue.clear(true);
            this.playbackInfo = this.playbackInfo.copyWithTrackInfo(TrackGroupArray.EMPTY, this.emptyTrackSelectorResult);
            resetRendererPosition(j);
        }
        handleLoadingMediaPeriodChanged(false);
        this.handler.sendEmptyMessage(2);
        return j;
    }

    private void resetRendererPosition(long j) throws ExoPlaybackException {
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        if (playingPeriod != null) {
            j = playingPeriod.toRendererTime(j);
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

    private void setForegroundModeInternal(boolean z, AtomicBoolean atomicBoolean) {
        Renderer[] rendererArr;
        if (this.foregroundMode != z) {
            this.foregroundMode = z;
            if (!z) {
                for (Renderer renderer : this.renderers) {
                    if (renderer.getState() == 0) {
                        renderer.reset();
                    }
                }
            }
        }
        if (atomicBoolean != null) {
            synchronized (this) {
                atomicBoolean.set(true);
                notifyAll();
            }
        }
    }

    private void stopInternal(boolean z, boolean z2, boolean z3) {
        resetInternal(z || !this.foregroundMode, true, z2, z2, z2);
        this.playbackInfoUpdate.incrementPendingOperationAcks(this.pendingPrepareCount + (z3 ? 1 : 0));
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

    /* JADX WARN: Removed duplicated region for block: B:35:0x0093  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00b9  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00c6  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00d6  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00df  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00e8  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00eb  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00f6  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00f8  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00ff  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0108  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x010b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void resetInternal(boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        boolean z6;
        MediaSource.MediaPeriodId mediaPeriodId;
        MediaSource mediaSource;
        this.handler.removeMessages(2);
        this.rebuffering = false;
        this.mediaClock.stop();
        this.rendererPositionUs = 0L;
        for (Renderer renderer : this.enabledRenderers) {
            try {
                disableRenderer(renderer);
            } catch (ExoPlaybackException | RuntimeException e) {
                Log.e("ExoPlayerImplInternal", "Disable failed.", e);
            }
        }
        if (z) {
            for (Renderer renderer2 : this.renderers) {
                try {
                    renderer2.reset();
                } catch (RuntimeException e2) {
                    Log.e("ExoPlayerImplInternal", "Reset failed.", e2);
                }
            }
        }
        this.enabledRenderers = new Renderer[0];
        if (z3) {
            this.pendingInitialSeekPosition = null;
        } else if (z4) {
            if (this.pendingInitialSeekPosition == null && !this.playbackInfo.timeline.isEmpty()) {
                PlaybackInfo playbackInfo = this.playbackInfo;
                playbackInfo.timeline.getPeriodByUid(playbackInfo.periodId.periodUid, this.period);
                this.pendingInitialSeekPosition = new SeekPosition(Timeline.EMPTY, this.period.windowIndex, this.playbackInfo.positionUs + this.period.getPositionInWindowUs());
            }
            z6 = true;
            this.queue.clear(!z4);
            this.shouldContinueLoading = false;
            if (z4) {
                this.queue.setTimeline(Timeline.EMPTY);
                Iterator<PendingMessageInfo> it = this.pendingMessages.iterator();
                while (it.hasNext()) {
                    it.next().message.markAsProcessed(false);
                }
                this.pendingMessages.clear();
            }
            if (!z6) {
                mediaPeriodId = this.playbackInfo.getDummyFirstMediaPeriodId(this.shuffleModeEnabled, this.window, this.period);
            } else {
                mediaPeriodId = this.playbackInfo.periodId;
            }
            MediaSource.MediaPeriodId mediaPeriodId2 = mediaPeriodId;
            long j = !z6 ? -9223372036854775807L : this.playbackInfo.positionUs;
            long j2 = z6 ? -9223372036854775807L : this.playbackInfo.contentPositionUs;
            Timeline timeline = !z4 ? Timeline.EMPTY : this.playbackInfo.timeline;
            PlaybackInfo playbackInfo2 = this.playbackInfo;
            this.playbackInfo = new PlaybackInfo(timeline, mediaPeriodId2, j, j2, playbackInfo2.playbackState, !z5 ? null : playbackInfo2.playbackError, false, !z4 ? TrackGroupArray.EMPTY : playbackInfo2.trackGroups, !z4 ? this.emptyTrackSelectorResult : playbackInfo2.trackSelectorResult, mediaPeriodId2, j, 0L, j);
            if (z2 || (mediaSource = this.mediaSource) == null) {
            }
            try {
                mediaSource.releaseSource(this);
            } catch (RuntimeException e3) {
                Log.e("ExoPlayerImplInternal", "Failed to release child source.", e3);
            }
            this.mediaSource = null;
            return;
        }
        z6 = z3;
        this.queue.clear(!z4);
        this.shouldContinueLoading = false;
        if (z4) {
        }
        if (!z6) {
        }
        MediaSource.MediaPeriodId mediaPeriodId22 = mediaPeriodId;
        if (!z6) {
        }
        long j22 = z6 ? -9223372036854775807L : this.playbackInfo.contentPositionUs;
        Timeline timeline2 = !z4 ? Timeline.EMPTY : this.playbackInfo.timeline;
        PlaybackInfo playbackInfo22 = this.playbackInfo;
        this.playbackInfo = new PlaybackInfo(timeline2, mediaPeriodId22, j, j22, playbackInfo22.playbackState, !z5 ? null : playbackInfo22.playbackError, false, !z4 ? TrackGroupArray.EMPTY : playbackInfo22.trackGroups, !z4 ? this.emptyTrackSelectorResult : playbackInfo22.trackSelectorResult, mediaPeriodId22, j, 0L, j);
        if (z2) {
        }
    }

    private void sendMessageInternal(PlayerMessage playerMessage) throws ExoPlaybackException {
        if (playerMessage.getPositionMs() == -9223372036854775807L) {
            sendMessageToTarget(playerMessage);
        } else if (this.mediaSource == null || this.pendingPrepareCount > 0) {
            this.pendingMessages.add(new PendingMessageInfo(playerMessage));
        } else {
            PendingMessageInfo pendingMessageInfo = new PendingMessageInfo(playerMessage);
            if (resolvePendingMessagePosition(pendingMessageInfo)) {
                this.pendingMessages.add(pendingMessageInfo);
                Collections.sort(this.pendingMessages);
                return;
            }
            playerMessage.markAsProcessed(false);
        }
    }

    private void sendMessageToTarget(PlayerMessage playerMessage) throws ExoPlaybackException {
        if (playerMessage.getHandler().getLooper() == this.handler.getLooper()) {
            deliverMessage(playerMessage);
            int i = this.playbackInfo.playbackState;
            if (i == 3 || i == 2) {
                this.handler.sendEmptyMessage(2);
                return;
            }
            return;
        }
        this.handler.obtainMessage(16, playerMessage).sendToTarget();
    }

    private void sendMessageToTargetThread(final PlayerMessage playerMessage) {
        Handler handler = playerMessage.getHandler();
        if (!handler.getLooper().getThread().isAlive()) {
            Log.w("TAG", "Trying to send message on a dead thread.");
            playerMessage.markAsProcessed(false);
            return;
        }
        handler.post(new Runnable() { // from class: com.google.android.exoplayer2.ExoPlayerImplInternal$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ExoPlayerImplInternal.this.lambda$sendMessageToTargetThread$0(playerMessage);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessageToTargetThread$0(PlayerMessage playerMessage) {
        try {
            deliverMessage(playerMessage);
        } catch (ExoPlaybackException e) {
            Log.e("ExoPlayerImplInternal", "Unexpected error delivering message on external thread.", e);
            throw new RuntimeException(e);
        }
    }

    private void deliverMessage(PlayerMessage playerMessage) throws ExoPlaybackException {
        if (playerMessage.isCanceled()) {
            return;
        }
        try {
            playerMessage.getTarget().handleMessage(playerMessage.getType(), playerMessage.getPayload());
        } finally {
            playerMessage.markAsProcessed(true);
        }
    }

    private void resolvePendingMessagePositions() {
        for (int size = this.pendingMessages.size() - 1; size >= 0; size--) {
            if (!resolvePendingMessagePosition(this.pendingMessages.get(size))) {
                this.pendingMessages.get(size).message.markAsProcessed(false);
                this.pendingMessages.remove(size);
            }
        }
        Collections.sort(this.pendingMessages);
    }

    private boolean resolvePendingMessagePosition(PendingMessageInfo pendingMessageInfo) {
        Object obj = pendingMessageInfo.resolvedPeriodUid;
        if (obj == null) {
            Pair<Object, Long> resolveSeekPosition = resolveSeekPosition(new SeekPosition(pendingMessageInfo.message.getTimeline(), pendingMessageInfo.message.getWindowIndex(), C.msToUs(pendingMessageInfo.message.getPositionMs())), false);
            if (resolveSeekPosition == null) {
                return false;
            }
            pendingMessageInfo.setResolvedPosition(this.playbackInfo.timeline.getIndexOfPeriod(resolveSeekPosition.first), ((Long) resolveSeekPosition.second).longValue(), resolveSeekPosition.first);
            return true;
        }
        int indexOfPeriod = this.playbackInfo.timeline.getIndexOfPeriod(obj);
        if (indexOfPeriod == -1) {
            return false;
        }
        pendingMessageInfo.resolvedPeriodIndex = indexOfPeriod;
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x007c  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0096 A[LOOP:1: B:29:0x007a->B:39:0x0096, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0079 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x00e2 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x00d9 A[SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:15:0x004a -> B:16:0x004b). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:28:0x0079 -> B:29:0x007a). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void maybeTriggerPendingMessages(long j, long j2) throws ExoPlaybackException {
        PendingMessageInfo pendingMessageInfo;
        PendingMessageInfo pendingMessageInfo2;
        long j3;
        int i;
        if (this.pendingMessages.isEmpty() || this.playbackInfo.periodId.isAd()) {
            return;
        }
        PlaybackInfo playbackInfo = this.playbackInfo;
        if (playbackInfo.startPositionUs == j && this.deliverPendingMessageAtStartPositionRequired) {
            j--;
        }
        this.deliverPendingMessageAtStartPositionRequired = false;
        int indexOfPeriod = playbackInfo.timeline.getIndexOfPeriod(playbackInfo.periodId.periodUid);
        int min = Math.min(this.nextPendingMessageIndexHint, this.pendingMessages.size());
        if (min > 0) {
            pendingMessageInfo = this.pendingMessages.get(min - 1);
            while (pendingMessageInfo != null) {
                int i2 = pendingMessageInfo.resolvedPeriodIndex;
                if (i2 <= indexOfPeriod && (i2 != indexOfPeriod || pendingMessageInfo.resolvedPeriodTimeUs <= j)) {
                    break;
                }
                min--;
                if (min > 0) {
                    pendingMessageInfo = this.pendingMessages.get(min - 1);
                }
            }
            if (min < this.pendingMessages.size()) {
                pendingMessageInfo2 = this.pendingMessages.get(min);
                while (pendingMessageInfo2 != null && pendingMessageInfo2.resolvedPeriodUid != null) {
                    i = pendingMessageInfo2.resolvedPeriodIndex;
                    if (i < indexOfPeriod && (i != indexOfPeriod || pendingMessageInfo2.resolvedPeriodTimeUs > j)) {
                        break;
                    }
                    min++;
                    if (min >= this.pendingMessages.size()) {
                        pendingMessageInfo2 = this.pendingMessages.get(min);
                    }
                }
                while (pendingMessageInfo2 != null && pendingMessageInfo2.resolvedPeriodUid != null && pendingMessageInfo2.resolvedPeriodIndex == indexOfPeriod) {
                    j3 = pendingMessageInfo2.resolvedPeriodTimeUs;
                    if (j3 > j || j3 > j2) {
                        break;
                        break;
                    }
                    try {
                        sendMessageToTarget(pendingMessageInfo2.message);
                        if (!pendingMessageInfo2.message.getDeleteAfterDelivery() || pendingMessageInfo2.message.isCanceled()) {
                            this.pendingMessages.remove(min);
                        } else {
                            min++;
                        }
                        pendingMessageInfo2 = min >= this.pendingMessages.size() ? this.pendingMessages.get(min) : null;
                    } catch (Throwable th) {
                        if (pendingMessageInfo2.message.getDeleteAfterDelivery() || pendingMessageInfo2.message.isCanceled()) {
                            this.pendingMessages.remove(min);
                        }
                        throw th;
                    }
                }
                this.nextPendingMessageIndexHint = min;
            }
            pendingMessageInfo2 = null;
            while (pendingMessageInfo2 != null) {
                i = pendingMessageInfo2.resolvedPeriodIndex;
                if (i < indexOfPeriod) {
                }
                min++;
                if (min >= this.pendingMessages.size()) {
                }
            }
            while (pendingMessageInfo2 != null) {
                j3 = pendingMessageInfo2.resolvedPeriodTimeUs;
                if (j3 > j) {
                    break;
                }
                sendMessageToTarget(pendingMessageInfo2.message);
                if (pendingMessageInfo2.message.getDeleteAfterDelivery()) {
                }
                this.pendingMessages.remove(min);
                if (min >= this.pendingMessages.size()) {
                }
            }
            this.nextPendingMessageIndexHint = min;
        }
        pendingMessageInfo = null;
        while (pendingMessageInfo != null) {
        }
        if (min < this.pendingMessages.size()) {
        }
        pendingMessageInfo2 = null;
        while (pendingMessageInfo2 != null) {
        }
        while (pendingMessageInfo2 != null) {
        }
        this.nextPendingMessageIndexHint = min;
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
        MediaPeriodHolder mediaPeriodHolder;
        boolean[] zArr;
        float f = this.mediaClock.getPlaybackParameters().speed;
        MediaPeriodHolder readingPeriod = this.queue.getReadingPeriod();
        boolean z = true;
        for (MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod(); playingPeriod != null && playingPeriod.prepared; playingPeriod = playingPeriod.getNext()) {
            TrackSelectorResult selectTracks = playingPeriod.selectTracks(f, this.playbackInfo.timeline);
            if (selectTracks.isEquivalent(playingPeriod.getTrackSelectorResult())) {
                if (playingPeriod == readingPeriod) {
                    z = false;
                }
            } else {
                if (z) {
                    MediaPeriodHolder playingPeriod2 = this.queue.getPlayingPeriod();
                    boolean removeAfter = this.queue.removeAfter(playingPeriod2);
                    boolean[] zArr2 = new boolean[this.renderers.length];
                    long applyTrackSelection = playingPeriod2.applyTrackSelection(selectTracks, this.playbackInfo.positionUs, removeAfter, zArr2);
                    PlaybackInfo playbackInfo = this.playbackInfo;
                    if (playbackInfo.playbackState == 4 || applyTrackSelection == playbackInfo.positionUs) {
                        mediaPeriodHolder = playingPeriod2;
                        zArr = zArr2;
                    } else {
                        PlaybackInfo playbackInfo2 = this.playbackInfo;
                        mediaPeriodHolder = playingPeriod2;
                        zArr = zArr2;
                        this.playbackInfo = copyWithNewPosition(playbackInfo2.periodId, applyTrackSelection, playbackInfo2.contentPositionUs);
                        this.playbackInfoUpdate.setPositionDiscontinuity(4);
                        resetRendererPosition(applyTrackSelection);
                    }
                    boolean[] zArr3 = new boolean[this.renderers.length];
                    int i = 0;
                    int i2 = 0;
                    while (true) {
                        Renderer[] rendererArr = this.renderers;
                        if (i >= rendererArr.length) {
                            break;
                        }
                        Renderer renderer = rendererArr[i];
                        zArr3[i] = renderer.getState() != 0;
                        SampleStream sampleStream = mediaPeriodHolder.sampleStreams[i];
                        if (sampleStream != null) {
                            i2++;
                        }
                        if (zArr3[i]) {
                            if (sampleStream != renderer.getStream()) {
                                disableRenderer(renderer);
                            } else if (zArr[i]) {
                                renderer.resetPosition(this.rendererPositionUs);
                            }
                        }
                        i++;
                    }
                    this.playbackInfo = this.playbackInfo.copyWithTrackInfo(mediaPeriodHolder.getTrackGroups(), mediaPeriodHolder.getTrackSelectorResult());
                    enableRenderers(zArr3, i2);
                } else {
                    this.queue.removeAfter(playingPeriod);
                    if (playingPeriod.prepared) {
                        playingPeriod.applyTrackSelection(selectTracks, Math.max(playingPeriod.info.startPositionUs, playingPeriod.toPeriodTime(this.rendererPositionUs)), false);
                    }
                }
                handleLoadingMediaPeriodChanged(true);
                if (this.playbackInfo.playbackState != 4) {
                    maybeContinueLoading();
                    updatePlaybackPositions();
                    this.handler.sendEmptyMessage(2);
                    return;
                }
                return;
            }
        }
    }

    private void updateTrackSelectionPlaybackSpeed(float f) {
        TrackSelection[] all;
        for (MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod(); playingPeriod != null; playingPeriod = playingPeriod.getNext()) {
            for (TrackSelection trackSelection : playingPeriod.getTrackSelectorResult().selections.getAll()) {
                if (trackSelection != null) {
                    trackSelection.onPlaybackSpeed(f);
                }
            }
        }
    }

    private void notifyTrackSelectionDiscontinuity() {
        TrackSelection[] all;
        for (MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod(); playingPeriod != null; playingPeriod = playingPeriod.getNext()) {
            for (TrackSelection trackSelection : playingPeriod.getTrackSelectorResult().selections.getAll()) {
                if (trackSelection != null) {
                    trackSelection.onDiscontinuity();
                }
            }
        }
    }

    private boolean shouldTransitionToReadyState(boolean z) {
        if (this.enabledRenderers.length == 0) {
            return isTimelineReady();
        }
        if (z) {
            if (this.playbackInfo.isLoading) {
                MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
                return (loadingPeriod.isFullyBuffered() && loadingPeriod.info.isFinal) || this.loadControl.shouldStartPlayback(getTotalBufferedDurationUs(), this.mediaClock.getPlaybackParameters().speed, this.rebuffering);
            }
            return true;
        }
        return false;
    }

    private boolean isTimelineReady() {
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        long j = playingPeriod.info.durationUs;
        return playingPeriod.prepared && (j == -9223372036854775807L || this.playbackInfo.positionUs < j);
    }

    private void maybeThrowSourceInfoRefreshError() throws IOException {
        if (this.queue.getLoadingPeriod() != null) {
            for (Renderer renderer : this.enabledRenderers) {
                if (!renderer.hasReadStreamToEnd()) {
                    return;
                }
            }
        }
        this.mediaSource.maybeThrowSourceInfoRefreshError();
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x0118 A[LOOP:0: B:48:0x0118->B:62:0x0118, LOOP_START, PHI: r12 
      PHI: (r12v20 com.google.android.exoplayer2.MediaPeriodHolder) = (r12v17 com.google.android.exoplayer2.MediaPeriodHolder), (r12v21 com.google.android.exoplayer2.MediaPeriodHolder) binds: [B:47:0x0116, B:62:0x0118] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x013d  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0140  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void handleSourceInfoRefreshed(MediaSourceRefreshInfo mediaSourceRefreshInfo) throws ExoPlaybackException {
        MediaSource.MediaPeriodId mediaPeriodId;
        long j;
        long longValue;
        MediaSource.MediaPeriodId resolveMediaPeriodIdForAds;
        MediaPeriodHolder playingPeriod;
        if (mediaSourceRefreshInfo.source != this.mediaSource) {
            return;
        }
        this.playbackInfoUpdate.incrementPendingOperationAcks(this.pendingPrepareCount);
        this.pendingPrepareCount = 0;
        Timeline timeline = this.playbackInfo.timeline;
        Timeline timeline2 = mediaSourceRefreshInfo.timeline;
        this.queue.setTimeline(timeline2);
        this.playbackInfo = this.playbackInfo.copyWithTimeline(timeline2);
        resolvePendingMessagePositions();
        MediaSource.MediaPeriodId mediaPeriodId2 = this.playbackInfo.periodId;
        long j2 = mediaPeriodId2.isAd() ? this.playbackInfo.contentPositionUs : this.playbackInfo.positionUs;
        SeekPosition seekPosition = this.pendingInitialSeekPosition;
        if (seekPosition != null) {
            Pair<Object, Long> resolveSeekPosition = resolveSeekPosition(seekPosition, true);
            this.pendingInitialSeekPosition = null;
            if (resolveSeekPosition == null) {
                handleSourceInfoRefreshEndedPlayback();
                return;
            } else {
                longValue = ((Long) resolveSeekPosition.second).longValue();
                resolveMediaPeriodIdForAds = this.queue.resolveMediaPeriodIdForAds(resolveSeekPosition.first, longValue);
            }
        } else {
            if (j2 == -9223372036854775807L && !timeline2.isEmpty()) {
                Pair<Object, Long> periodPosition = getPeriodPosition(timeline2, timeline2.getFirstWindowIndex(this.shuffleModeEnabled), -9223372036854775807L);
                MediaSource.MediaPeriodId resolveMediaPeriodIdForAds2 = this.queue.resolveMediaPeriodIdForAds(periodPosition.first, ((Long) periodPosition.second).longValue());
                j = !resolveMediaPeriodIdForAds2.isAd() ? ((Long) periodPosition.second).longValue() : j2;
                mediaPeriodId = resolveMediaPeriodIdForAds2;
            } else if (timeline2.getIndexOfPeriod(mediaPeriodId2.periodUid) == -1) {
                Object resolveSubsequentPeriod = resolveSubsequentPeriod(mediaPeriodId2.periodUid, timeline, timeline2);
                if (resolveSubsequentPeriod == null) {
                    handleSourceInfoRefreshEndedPlayback();
                    return;
                }
                Pair<Object, Long> periodPosition2 = getPeriodPosition(timeline2, timeline2.getPeriodByUid(resolveSubsequentPeriod, this.period).windowIndex, -9223372036854775807L);
                longValue = ((Long) periodPosition2.second).longValue();
                resolveMediaPeriodIdForAds = this.queue.resolveMediaPeriodIdForAds(periodPosition2.first, longValue);
            } else {
                MediaSource.MediaPeriodId resolveMediaPeriodIdForAds3 = this.queue.resolveMediaPeriodIdForAds(this.playbackInfo.periodId.periodUid, j2);
                if (!this.playbackInfo.periodId.isAd() && !resolveMediaPeriodIdForAds3.isAd()) {
                    resolveMediaPeriodIdForAds3 = this.playbackInfo.periodId;
                }
                mediaPeriodId = resolveMediaPeriodIdForAds3;
                j = j2;
            }
            if (!this.playbackInfo.periodId.equals(mediaPeriodId) && j2 == j) {
                if (!this.queue.updateQueuedPeriods(this.rendererPositionUs, getMaxRendererReadPositionUs())) {
                    seekToCurrentPosition(false);
                }
            } else {
                playingPeriod = this.queue.getPlayingPeriod();
                if (playingPeriod != null) {
                    while (playingPeriod.getNext() != null) {
                        playingPeriod = playingPeriod.getNext();
                        if (playingPeriod.info.id.equals(mediaPeriodId)) {
                            playingPeriod.info = this.queue.getUpdatedMediaPeriodInfo(playingPeriod.info);
                        }
                    }
                }
                this.playbackInfo = copyWithNewPosition(mediaPeriodId, seekToPeriodPosition(mediaPeriodId, !mediaPeriodId.isAd() ? 0L : j), j);
            }
            handleLoadingMediaPeriodChanged(false);
        }
        mediaPeriodId = resolveMediaPeriodIdForAds;
        j = longValue;
        if (!this.playbackInfo.periodId.equals(mediaPeriodId)) {
        }
        playingPeriod = this.queue.getPlayingPeriod();
        if (playingPeriod != null) {
        }
        this.playbackInfo = copyWithNewPosition(mediaPeriodId, seekToPeriodPosition(mediaPeriodId, !mediaPeriodId.isAd() ? 0L : j), j);
        handleLoadingMediaPeriodChanged(false);
    }

    private long getMaxRendererReadPositionUs() {
        MediaPeriodHolder readingPeriod = this.queue.getReadingPeriod();
        if (readingPeriod == null) {
            return 0L;
        }
        long rendererOffset = readingPeriod.getRendererOffset();
        if (!readingPeriod.prepared) {
            return rendererOffset;
        }
        int i = 0;
        while (true) {
            Renderer[] rendererArr = this.renderers;
            if (i >= rendererArr.length) {
                return rendererOffset;
            }
            if (rendererArr[i].getState() != 0 && this.renderers[i].getStream() == readingPeriod.sampleStreams[i]) {
                long readingPositionUs = this.renderers[i].getReadingPositionUs();
                if (readingPositionUs == Long.MIN_VALUE) {
                    return Long.MIN_VALUE;
                }
                rendererOffset = Math.max(readingPositionUs, rendererOffset);
            }
            i++;
        }
    }

    private void handleSourceInfoRefreshEndedPlayback() {
        if (this.playbackInfo.playbackState != 1) {
            setState(4);
        }
        resetInternal(false, false, true, false, true);
    }

    private Object resolveSubsequentPeriod(Object obj, Timeline timeline, Timeline timeline2) {
        int indexOfPeriod = timeline.getIndexOfPeriod(obj);
        int periodCount = timeline.getPeriodCount();
        int i = indexOfPeriod;
        int i2 = -1;
        for (int i3 = 0; i3 < periodCount && i2 == -1; i3++) {
            i = timeline.getNextPeriodIndex(i, this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
            if (i == -1) {
                break;
            }
            i2 = timeline2.getIndexOfPeriod(timeline.getUidOfPeriod(i));
        }
        if (i2 == -1) {
            return null;
        }
        return timeline2.getUidOfPeriod(i2);
    }

    private Pair<Object, Long> resolveSeekPosition(SeekPosition seekPosition, boolean z) {
        Pair<Object, Long> periodPosition;
        Object resolveSubsequentPeriod;
        Timeline timeline = this.playbackInfo.timeline;
        Timeline timeline2 = seekPosition.timeline;
        if (timeline.isEmpty()) {
            return null;
        }
        if (timeline2.isEmpty()) {
            timeline2 = timeline;
        }
        try {
            periodPosition = timeline2.getPeriodPosition(this.window, this.period, seekPosition.windowIndex, seekPosition.windowPositionUs);
        } catch (IndexOutOfBoundsException unused) {
        }
        if (timeline != timeline2 && timeline.getIndexOfPeriod(periodPosition.first) == -1) {
            if (z && (resolveSubsequentPeriod = resolveSubsequentPeriod(periodPosition.first, timeline2, timeline)) != null) {
                return getPeriodPosition(timeline, timeline.getPeriodByUid(resolveSubsequentPeriod, this.period).windowIndex, -9223372036854775807L);
            }
            return null;
        }
        return periodPosition;
    }

    private Pair<Object, Long> getPeriodPosition(Timeline timeline, int i, long j) {
        return timeline.getPeriodPosition(this.window, this.period, i, j);
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
            MediaPeriodInfo nextMediaPeriodInfo = this.queue.getNextMediaPeriodInfo(this.rendererPositionUs, this.playbackInfo);
            if (nextMediaPeriodInfo == null) {
                maybeThrowSourceInfoRefreshError();
            } else {
                MediaPeriodHolder enqueueNextMediaPeriodHolder = this.queue.enqueueNextMediaPeriodHolder(this.rendererCapabilities, this.trackSelector, this.loadControl.getAllocator(), this.mediaSource, nextMediaPeriodInfo, this.emptyTrackSelectorResult);
                enqueueNextMediaPeriodHolder.mediaPeriod.prepare(this, nextMediaPeriodInfo.startPositionUs);
                if (this.queue.getPlayingPeriod() == enqueueNextMediaPeriodHolder) {
                    resetRendererPosition(enqueueNextMediaPeriodHolder.getStartPositionRendererTime());
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
        MediaPeriodHolder readingPeriod = this.queue.getReadingPeriod();
        if (readingPeriod == null) {
            return;
        }
        int i = 0;
        if (readingPeriod.getNext() == null) {
            if (!readingPeriod.info.isFinal) {
                return;
            }
            while (true) {
                Renderer[] rendererArr = this.renderers;
                if (i >= rendererArr.length) {
                    return;
                }
                Renderer renderer = rendererArr[i];
                SampleStream sampleStream = readingPeriod.sampleStreams[i];
                if (sampleStream != null && renderer.getStream() == sampleStream && renderer.hasReadStreamToEnd()) {
                    renderer.setCurrentStreamFinal();
                }
                i++;
            }
        } else if (!hasReadingPeriodFinishedReading() || !readingPeriod.getNext().prepared) {
        } else {
            TrackSelectorResult trackSelectorResult = readingPeriod.getTrackSelectorResult();
            MediaPeriodHolder advanceReadingPeriod = this.queue.advanceReadingPeriod();
            TrackSelectorResult trackSelectorResult2 = advanceReadingPeriod.getTrackSelectorResult();
            if (advanceReadingPeriod.mediaPeriod.readDiscontinuity() != -9223372036854775807L) {
                setAllRendererStreamsFinal();
                return;
            }
            int i2 = 0;
            while (true) {
                Renderer[] rendererArr2 = this.renderers;
                if (i2 >= rendererArr2.length) {
                    return;
                }
                Renderer renderer2 = rendererArr2[i2];
                if (trackSelectorResult.isRendererEnabled(i2) && !renderer2.isCurrentStreamFinal()) {
                    TrackSelection trackSelection = trackSelectorResult2.selections.get(i2);
                    boolean isRendererEnabled = trackSelectorResult2.isRendererEnabled(i2);
                    boolean z = this.rendererCapabilities[i2].getTrackType() == 6;
                    RendererConfiguration rendererConfiguration = trackSelectorResult.rendererConfigurations[i2];
                    RendererConfiguration rendererConfiguration2 = trackSelectorResult2.rendererConfigurations[i2];
                    if (isRendererEnabled && rendererConfiguration2.equals(rendererConfiguration) && !z) {
                        renderer2.replaceStream(getFormats(trackSelection), advanceReadingPeriod.sampleStreams[i2], advanceReadingPeriod.getRendererOffset());
                    } else {
                        renderer2.setCurrentStreamFinal();
                    }
                }
                i2++;
            }
        }
    }

    private void maybeUpdatePlayingPeriod() throws ExoPlaybackException {
        boolean z = false;
        while (shouldAdvancePlayingPeriod()) {
            if (z) {
                maybeNotifyPlaybackInfoChanged();
            }
            MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
            if (playingPeriod == this.queue.getReadingPeriod()) {
                setAllRendererStreamsFinal();
            }
            MediaPeriodHolder advancePlayingPeriod = this.queue.advancePlayingPeriod();
            updatePlayingPeriodRenderers(playingPeriod);
            MediaPeriodInfo mediaPeriodInfo = advancePlayingPeriod.info;
            this.playbackInfo = copyWithNewPosition(mediaPeriodInfo.id, mediaPeriodInfo.startPositionUs, mediaPeriodInfo.contentPositionUs);
            this.playbackInfoUpdate.setPositionDiscontinuity(playingPeriod.info.isLastInTimelinePeriod ? 0 : 3);
            updatePlaybackPositions();
            z = true;
        }
    }

    private boolean shouldAdvancePlayingPeriod() {
        MediaPeriodHolder playingPeriod;
        MediaPeriodHolder next;
        if (!this.playWhenReady || (playingPeriod = this.queue.getPlayingPeriod()) == null || (next = playingPeriod.getNext()) == null) {
            return false;
        }
        return (playingPeriod != this.queue.getReadingPeriod() || hasReadingPeriodFinishedReading()) && this.rendererPositionUs >= next.getStartPositionRendererTime();
    }

    private boolean hasReadingPeriodFinishedReading() {
        MediaPeriodHolder readingPeriod = this.queue.getReadingPeriod();
        if (readingPeriod.prepared) {
            int i = 0;
            while (true) {
                Renderer[] rendererArr = this.renderers;
                if (i >= rendererArr.length) {
                    return true;
                }
                Renderer renderer = rendererArr[i];
                SampleStream sampleStream = readingPeriod.sampleStreams[i];
                if (renderer.getStream() != sampleStream || (sampleStream != null && !renderer.hasReadStreamToEnd())) {
                    break;
                }
                i++;
            }
            return false;
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
        if (this.queue.isLoading(mediaPeriod)) {
            MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
            loadingPeriod.handlePrepared(this.mediaClock.getPlaybackParameters().speed, this.playbackInfo.timeline);
            updateLoadControlTrackSelection(loadingPeriod.getTrackGroups(), loadingPeriod.getTrackSelectorResult());
            if (loadingPeriod == this.queue.getPlayingPeriod()) {
                resetRendererPosition(loadingPeriod.info.startPositionUs);
                updatePlayingPeriodRenderers(null);
            }
            maybeContinueLoading();
        }
    }

    private void handleContinueLoadingRequested(MediaPeriod mediaPeriod) {
        if (this.queue.isLoading(mediaPeriod)) {
            this.queue.reevaluateBuffer(this.rendererPositionUs);
            maybeContinueLoading();
        }
    }

    private void handlePlaybackParameters(PlaybackParameters playbackParameters, boolean z) throws ExoPlaybackException {
        Renderer[] rendererArr;
        this.eventHandler.obtainMessage(1, z ? 1 : 0, 0, playbackParameters).sendToTarget();
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
        if (isLoadingPossible()) {
            return this.loadControl.shouldContinueLoading(getTotalBufferedDurationUs(this.queue.getLoadingPeriod().getNextLoadPositionUs()), this.mediaClock.getPlaybackParameters().speed);
        }
        return false;
    }

    private boolean isLoadingPossible() {
        MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        return (loadingPeriod == null || loadingPeriod.getNextLoadPositionUs() == Long.MIN_VALUE) ? false : true;
    }

    private void updateIsLoading() {
        MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        boolean z = this.shouldContinueLoading || (loadingPeriod != null && loadingPeriod.mediaPeriod.isLoading());
        PlaybackInfo playbackInfo = this.playbackInfo;
        if (z != playbackInfo.isLoading) {
            this.playbackInfo = playbackInfo.copyWithIsLoading(z);
        }
    }

    private PlaybackInfo copyWithNewPosition(MediaSource.MediaPeriodId mediaPeriodId, long j, long j2) {
        this.deliverPendingMessageAtStartPositionRequired = true;
        return this.playbackInfo.copyWithNewPosition(mediaPeriodId, j, j2, getTotalBufferedDurationUs());
    }

    private void updatePlayingPeriodRenderers(MediaPeriodHolder mediaPeriodHolder) throws ExoPlaybackException {
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        if (playingPeriod == null || mediaPeriodHolder == playingPeriod) {
            return;
        }
        boolean[] zArr = new boolean[this.renderers.length];
        int i = 0;
        int i2 = 0;
        while (true) {
            Renderer[] rendererArr = this.renderers;
            if (i < rendererArr.length) {
                Renderer renderer = rendererArr[i];
                zArr[i] = renderer.getState() != 0;
                if (playingPeriod.getTrackSelectorResult().isRendererEnabled(i)) {
                    i2++;
                }
                if (zArr[i] && (!playingPeriod.getTrackSelectorResult().isRendererEnabled(i) || (renderer.isCurrentStreamFinal() && renderer.getStream() == mediaPeriodHolder.sampleStreams[i]))) {
                    disableRenderer(renderer);
                }
                i++;
            } else {
                this.playbackInfo = this.playbackInfo.copyWithTrackInfo(playingPeriod.getTrackGroups(), playingPeriod.getTrackSelectorResult());
                enableRenderers(zArr, i2);
                return;
            }
        }
    }

    private void enableRenderers(boolean[] zArr, int i) throws ExoPlaybackException {
        this.enabledRenderers = new Renderer[i];
        TrackSelectorResult trackSelectorResult = this.queue.getPlayingPeriod().getTrackSelectorResult();
        for (int i2 = 0; i2 < this.renderers.length; i2++) {
            if (!trackSelectorResult.isRendererEnabled(i2)) {
                this.renderers[i2].reset();
            }
        }
        int i3 = 0;
        for (int i4 = 0; i4 < this.renderers.length; i4++) {
            if (trackSelectorResult.isRendererEnabled(i4)) {
                enableRenderer(i4, zArr[i4], i3);
                i3++;
            }
        }
    }

    private void enableRenderer(int i, boolean z, int i2) throws ExoPlaybackException {
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        Renderer renderer = this.renderers[i];
        this.enabledRenderers[i2] = renderer;
        if (renderer.getState() == 0) {
            TrackSelectorResult trackSelectorResult = playingPeriod.getTrackSelectorResult();
            RendererConfiguration rendererConfiguration = trackSelectorResult.rendererConfigurations[i];
            Format[] formats = getFormats(trackSelectorResult.selections.get(i));
            boolean z2 = this.playWhenReady && this.playbackInfo.playbackState == 3;
            renderer.enable(rendererConfiguration, formats, playingPeriod.sampleStreams[i], this.rendererPositionUs, !z && z2, playingPeriod.getRendererOffset());
            this.mediaClock.onRendererEnabled(renderer);
            if (z2) {
                renderer.start();
            }
        }
    }

    private void handleLoadingMediaPeriodChanged(boolean z) {
        long bufferedPositionUs;
        MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        MediaSource.MediaPeriodId mediaPeriodId = loadingPeriod == null ? this.playbackInfo.periodId : loadingPeriod.info.id;
        boolean z2 = !this.playbackInfo.loadingMediaPeriodId.equals(mediaPeriodId);
        if (z2) {
            this.playbackInfo = this.playbackInfo.copyWithLoadingMediaPeriodId(mediaPeriodId);
        }
        PlaybackInfo playbackInfo = this.playbackInfo;
        if (loadingPeriod == null) {
            bufferedPositionUs = playbackInfo.positionUs;
        } else {
            bufferedPositionUs = loadingPeriod.getBufferedPositionUs();
        }
        playbackInfo.bufferedPositionUs = bufferedPositionUs;
        this.playbackInfo.totalBufferedDurationUs = getTotalBufferedDurationUs();
        if ((z2 || z) && loadingPeriod != null && loadingPeriod.prepared) {
            updateLoadControlTrackSelection(loadingPeriod.getTrackGroups(), loadingPeriod.getTrackSelectorResult());
        }
    }

    private long getTotalBufferedDurationUs() {
        return getTotalBufferedDurationUs(this.playbackInfo.bufferedPositionUs);
    }

    private long getTotalBufferedDurationUs(long j) {
        MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        if (loadingPeriod == null) {
            return 0L;
        }
        return Math.max(0L, j - loadingPeriod.toPeriodTime(this.rendererPositionUs));
    }

    private void updateLoadControlTrackSelection(TrackGroupArray trackGroupArray, TrackSelectorResult trackSelectorResult) {
        this.loadControl.onTracksSelected(this.renderers, trackGroupArray, trackSelectorResult.selections);
    }

    private void sendPlaybackParametersChangedInternal(PlaybackParameters playbackParameters, boolean z) {
        this.handler.obtainMessage(17, z ? 1 : 0, 0, playbackParameters).sendToTarget();
    }

    private static Format[] getFormats(TrackSelection trackSelection) {
        int length = trackSelection != null ? trackSelection.length() : 0;
        Format[] formatArr = new Format[length];
        for (int i = 0; i < length; i++) {
            formatArr[i] = trackSelection.getFormat(i);
        }
        return formatArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class SeekPosition {
        public final Timeline timeline;
        public final int windowIndex;
        public final long windowPositionUs;

        public SeekPosition(Timeline timeline, int i, long j) {
            this.timeline = timeline;
            this.windowIndex = i;
            this.windowPositionUs = j;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class PendingMessageInfo implements Comparable<PendingMessageInfo> {
        public final PlayerMessage message;
        public int resolvedPeriodIndex;
        public long resolvedPeriodTimeUs;
        public Object resolvedPeriodUid;

        public PendingMessageInfo(PlayerMessage playerMessage) {
            this.message = playerMessage;
        }

        public void setResolvedPosition(int i, long j, Object obj) {
            this.resolvedPeriodIndex = i;
            this.resolvedPeriodTimeUs = j;
            this.resolvedPeriodUid = obj;
        }

        @Override // java.lang.Comparable
        public int compareTo(PendingMessageInfo pendingMessageInfo) {
            Object obj = this.resolvedPeriodUid;
            if ((obj == null) != (pendingMessageInfo.resolvedPeriodUid == null)) {
                return obj != null ? -1 : 1;
            } else if (obj == null) {
                return 0;
            } else {
                int i = this.resolvedPeriodIndex - pendingMessageInfo.resolvedPeriodIndex;
                return i != 0 ? i : Util.compareLong(this.resolvedPeriodTimeUs, pendingMessageInfo.resolvedPeriodTimeUs);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class MediaSourceRefreshInfo {
        public final MediaSource source;
        public final Timeline timeline;

        public MediaSourceRefreshInfo(MediaSource mediaSource, Timeline timeline) {
            this.source = mediaSource;
            this.timeline = timeline;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
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

        public void incrementPendingOperationAcks(int i) {
            this.operationAcks += i;
        }

        public void setPositionDiscontinuity(int i) {
            if (this.positionDiscontinuity && this.discontinuityReason != 4) {
                Assertions.checkArgument(i == 4);
                return;
            }
            this.positionDiscontinuity = true;
            this.discontinuityReason = i;
        }
    }
}
