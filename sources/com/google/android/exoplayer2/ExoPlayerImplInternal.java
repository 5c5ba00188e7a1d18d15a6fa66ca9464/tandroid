package com.google.android.exoplayer2;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Pair;
import androidx.recyclerview.widget.RecyclerView$ItemAnimator$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.android.exoplayer2.DefaultMediaClock;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaSourceList;
import com.google.android.exoplayer2.PlayerMessage;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.analytics.AnalyticsCollector;
import com.google.android.exoplayer2.analytics.PlayerId;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataRenderer;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.ShuffleOrder;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.trackselection.ExoTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSourceException;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.HandlerWrapper;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.telegram.messenger.MediaDataController;

/* loaded from: classes.dex */
final class ExoPlayerImplInternal implements Handler.Callback, MediaPeriod.Callback, TrackSelector.InvalidationListener, MediaSourceList.MediaSourceListInfoRefreshListener, DefaultMediaClock.PlaybackParametersListener, PlayerMessage.Sender {
    private final long backBufferDurationUs;
    private final BandwidthMeter bandwidthMeter;
    private final Clock clock;
    private boolean deliverPendingMessageAtStartPositionRequired;
    private final TrackSelectorResult emptyTrackSelectorResult;
    private int enabledRendererCount;
    private boolean foregroundMode;
    private final HandlerWrapper handler;
    private final HandlerThread internalPlaybackThread;
    private boolean isRebuffering;
    private final LivePlaybackSpeedControl livePlaybackSpeedControl;
    private final LoadControl loadControl;
    private final DefaultMediaClock mediaClock;
    private final MediaSourceList mediaSourceList;
    private int nextPendingMessageIndexHint;
    private boolean offloadSchedulingEnabled;
    private boolean pauseAtEndOfWindow;
    private SeekPosition pendingInitialSeekPosition;
    private final ArrayList pendingMessages;
    private boolean pendingPauseAtEndOfPeriod;
    private ExoPlaybackException pendingRecoverableRendererError;
    private final Timeline.Period period;
    private PlaybackInfo playbackInfo;
    private PlaybackInfoUpdate playbackInfoUpdate;
    private final PlaybackInfoUpdateListener playbackInfoUpdateListener;
    private final Looper playbackLooper;
    private long playbackMaybeBecameStuckAtMs = -9223372036854775807L;
    private final MediaPeriodQueue queue;
    private final long releaseTimeoutMs;
    private boolean released;
    private final RendererCapabilities[] rendererCapabilities;
    private long rendererPositionUs;
    private final Renderer[] renderers;
    private final Set renderersToReset;
    private int repeatMode;
    private boolean requestForRendererSleep;
    private final boolean retainBackBufferFromKeyframe;
    private SeekParameters seekParameters;
    private long setForegroundModeTimeoutMs;
    private boolean shouldContinueLoading;
    private boolean shuffleModeEnabled;
    private final TrackSelector trackSelector;
    private final Timeline.Window window;

    private static final class MediaSourceListUpdateMessage {
        private final List mediaSourceHolders;
        private final long positionUs;
        private final ShuffleOrder shuffleOrder;
        private final int windowIndex;

        private MediaSourceListUpdateMessage(List list, ShuffleOrder shuffleOrder, int i, long j) {
            this.mediaSourceHolders = list;
            this.shuffleOrder = shuffleOrder;
            this.windowIndex = i;
            this.positionUs = j;
        }
    }

    private static class MoveMediaItemsMessage {
    }

    private static final class PendingMessageInfo implements Comparable {
        public final PlayerMessage message;
        public int resolvedPeriodIndex;
        public long resolvedPeriodTimeUs;
        public Object resolvedPeriodUid;

        public PendingMessageInfo(PlayerMessage playerMessage) {
            this.message = playerMessage;
        }

        @Override // java.lang.Comparable
        public int compareTo(PendingMessageInfo pendingMessageInfo) {
            Object obj = this.resolvedPeriodUid;
            if ((obj == null) != (pendingMessageInfo.resolvedPeriodUid == null)) {
                return obj != null ? -1 : 1;
            }
            if (obj == null) {
                return 0;
            }
            int i = this.resolvedPeriodIndex - pendingMessageInfo.resolvedPeriodIndex;
            return i != 0 ? i : Util.compareLong(this.resolvedPeriodTimeUs, pendingMessageInfo.resolvedPeriodTimeUs);
        }

        public void setResolvedPosition(int i, long j, Object obj) {
            this.resolvedPeriodIndex = i;
            this.resolvedPeriodTimeUs = j;
            this.resolvedPeriodUid = obj;
        }
    }

    public static final class PlaybackInfoUpdate {
        public int discontinuityReason;
        private boolean hasPendingChange;
        public boolean hasPlayWhenReadyChangeReason;
        public int operationAcks;
        public int playWhenReadyChangeReason;
        public PlaybackInfo playbackInfo;
        public boolean positionDiscontinuity;

        public PlaybackInfoUpdate(PlaybackInfo playbackInfo) {
            this.playbackInfo = playbackInfo;
        }

        public void incrementPendingOperationAcks(int i) {
            this.hasPendingChange |= i > 0;
            this.operationAcks += i;
        }

        public void setPlayWhenReadyChangeReason(int i) {
            this.hasPendingChange = true;
            this.hasPlayWhenReadyChangeReason = true;
            this.playWhenReadyChangeReason = i;
        }

        public void setPlaybackInfo(PlaybackInfo playbackInfo) {
            this.hasPendingChange |= this.playbackInfo != playbackInfo;
            this.playbackInfo = playbackInfo;
        }

        public void setPositionDiscontinuity(int i) {
            if (this.positionDiscontinuity && this.discontinuityReason != 5) {
                Assertions.checkArgument(i == 5);
                return;
            }
            this.hasPendingChange = true;
            this.positionDiscontinuity = true;
            this.discontinuityReason = i;
        }
    }

    public interface PlaybackInfoUpdateListener {
        void onPlaybackInfoUpdate(PlaybackInfoUpdate playbackInfoUpdate);
    }

    private static final class PositionUpdateForPlaylistChange {
        public final boolean endPlayback;
        public final boolean forceBufferingState;
        public final MediaSource.MediaPeriodId periodId;
        public final long periodPositionUs;
        public final long requestedContentPositionUs;
        public final boolean setTargetLiveOffset;

        public PositionUpdateForPlaylistChange(MediaSource.MediaPeriodId mediaPeriodId, long j, long j2, boolean z, boolean z2, boolean z3) {
            this.periodId = mediaPeriodId;
            this.periodPositionUs = j;
            this.requestedContentPositionUs = j2;
            this.forceBufferingState = z;
            this.endPlayback = z2;
            this.setTargetLiveOffset = z3;
        }
    }

    private static final class SeekPosition {
        public final Timeline timeline;
        public final int windowIndex;
        public final long windowPositionUs;

        public SeekPosition(Timeline timeline, int i, long j) {
            this.timeline = timeline;
            this.windowIndex = i;
            this.windowPositionUs = j;
        }
    }

    public ExoPlayerImplInternal(Renderer[] rendererArr, TrackSelector trackSelector, TrackSelectorResult trackSelectorResult, LoadControl loadControl, BandwidthMeter bandwidthMeter, int i, boolean z, AnalyticsCollector analyticsCollector, SeekParameters seekParameters, LivePlaybackSpeedControl livePlaybackSpeedControl, long j, boolean z2, Looper looper, Clock clock, PlaybackInfoUpdateListener playbackInfoUpdateListener, PlayerId playerId, Looper looper2) {
        this.playbackInfoUpdateListener = playbackInfoUpdateListener;
        this.renderers = rendererArr;
        this.trackSelector = trackSelector;
        this.emptyTrackSelectorResult = trackSelectorResult;
        this.loadControl = loadControl;
        this.bandwidthMeter = bandwidthMeter;
        this.repeatMode = i;
        this.shuffleModeEnabled = z;
        this.seekParameters = seekParameters;
        this.livePlaybackSpeedControl = livePlaybackSpeedControl;
        this.releaseTimeoutMs = j;
        this.setForegroundModeTimeoutMs = j;
        this.pauseAtEndOfWindow = z2;
        this.clock = clock;
        this.backBufferDurationUs = loadControl.getBackBufferDurationUs();
        this.retainBackBufferFromKeyframe = loadControl.retainBackBufferFromKeyframe();
        PlaybackInfo createDummy = PlaybackInfo.createDummy(trackSelectorResult);
        this.playbackInfo = createDummy;
        this.playbackInfoUpdate = new PlaybackInfoUpdate(createDummy);
        this.rendererCapabilities = new RendererCapabilities[rendererArr.length];
        for (int i2 = 0; i2 < rendererArr.length; i2++) {
            rendererArr[i2].init(i2, playerId);
            this.rendererCapabilities[i2] = rendererArr[i2].getCapabilities();
        }
        this.mediaClock = new DefaultMediaClock(this, clock);
        this.pendingMessages = new ArrayList();
        this.renderersToReset = Sets.newIdentityHashSet();
        this.window = new Timeline.Window();
        this.period = new Timeline.Period();
        trackSelector.init(this, bandwidthMeter);
        this.deliverPendingMessageAtStartPositionRequired = true;
        HandlerWrapper createHandler = clock.createHandler(looper, null);
        this.queue = new MediaPeriodQueue(analyticsCollector, createHandler);
        this.mediaSourceList = new MediaSourceList(this, analyticsCollector, createHandler, playerId);
        if (looper2 != null) {
            this.internalPlaybackThread = null;
            this.playbackLooper = looper2;
        } else {
            HandlerThread handlerThread = new HandlerThread("ExoPlayer:Playback", -16);
            this.internalPlaybackThread = handlerThread;
            handlerThread.start();
            this.playbackLooper = handlerThread.getLooper();
        }
        this.handler = clock.createHandler(this.playbackLooper, this);
    }

    private void addMediaItemsInternal(MediaSourceListUpdateMessage mediaSourceListUpdateMessage, int i) {
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
        MediaSourceList mediaSourceList = this.mediaSourceList;
        if (i == -1) {
            i = mediaSourceList.getSize();
        }
        handleMediaSourceListInfoRefreshed(mediaSourceList.addMediaSources(i, mediaSourceListUpdateMessage.mediaSourceHolders, mediaSourceListUpdateMessage.shuffleOrder), false);
    }

    private void attemptRendererErrorRecovery() {
        seekToCurrentPosition(true);
    }

    private void deliverMessage(PlayerMessage playerMessage) {
        if (playerMessage.isCanceled()) {
            return;
        }
        try {
            playerMessage.getTarget().handleMessage(playerMessage.getType(), playerMessage.getPayload());
        } finally {
            playerMessage.markAsProcessed(true);
        }
    }

    private void disableRenderer(Renderer renderer) {
        if (isRendererEnabled(renderer)) {
            this.mediaClock.onRendererDisabled(renderer);
            ensureStopped(renderer);
            renderer.disable();
            this.enabledRendererCount--;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x01b0  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x01bf  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x01c9  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x013f  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01a3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void doSomeWork() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        PlaybackInfo playbackInfo;
        int i;
        long j;
        long uptimeMillis = this.clock.uptimeMillis();
        this.handler.removeMessages(2);
        updatePeriods();
        int i2 = this.playbackInfo.playbackState;
        if (i2 == 1 || i2 == 4) {
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
            int i3 = 0;
            z = true;
            z2 = true;
            while (true) {
                Renderer[] rendererArr = this.renderers;
                if (i3 >= rendererArr.length) {
                    break;
                }
                Renderer renderer = rendererArr[i3];
                if (isRendererEnabled(renderer)) {
                    renderer.render(this.rendererPositionUs, elapsedRealtime);
                    z = z && renderer.isEnded();
                    boolean z5 = playingPeriod.sampleStreams[i3] != renderer.getStream();
                    boolean z6 = z5 || (!z5 && renderer.hasReadStreamToEnd()) || renderer.isReady() || renderer.isEnded();
                    z2 = z2 && z6;
                    if (!z6) {
                        renderer.maybeThrowStreamError();
                    }
                }
                i3++;
            }
        } else {
            playingPeriod.mediaPeriod.maybeThrowPrepareError();
            z = true;
            z2 = true;
        }
        long j2 = playingPeriod.info.durationUs;
        boolean z7 = z && playingPeriod.prepared && (j2 == -9223372036854775807L || j2 <= this.playbackInfo.positionUs);
        if (z7 && this.pendingPauseAtEndOfPeriod) {
            this.pendingPauseAtEndOfPeriod = false;
            setPlayWhenReadyInternal(false, this.playbackInfo.playbackSuppressionReason, false, 5);
        }
        if (!z7 || !playingPeriod.info.isFinal) {
            if (this.playbackInfo.playbackState == 2 && shouldTransitionToReadyState(z2)) {
                setState(3);
                this.pendingRecoverableRendererError = null;
                if (shouldPlayWhenReady()) {
                    startRenderers();
                }
            } else if (this.playbackInfo.playbackState == 3 && (this.enabledRendererCount != 0 ? !z2 : !isTimelineReady())) {
                this.isRebuffering = shouldPlayWhenReady();
                setState(2);
                if (this.isRebuffering) {
                    notifyTrackSelectionRebuffer();
                    this.livePlaybackSpeedControl.notifyRebuffer();
                }
            }
            if (this.playbackInfo.playbackState == 2) {
                int i4 = 0;
                while (true) {
                    Renderer[] rendererArr2 = this.renderers;
                    if (i4 >= rendererArr2.length) {
                        break;
                    }
                    if (isRendererEnabled(rendererArr2[i4]) && this.renderers[i4].getStream() == playingPeriod.sampleStreams[i4]) {
                        this.renderers[i4].maybeThrowStreamError();
                    }
                    i4++;
                }
                PlaybackInfo playbackInfo2 = this.playbackInfo;
                if (!playbackInfo2.isLoading && playbackInfo2.totalBufferedDurationUs < 500000 && isLoadingPossible()) {
                    long j3 = this.playbackMaybeBecameStuckAtMs;
                    long elapsedRealtime2 = this.clock.elapsedRealtime();
                    if (j3 == -9223372036854775807L) {
                        this.playbackMaybeBecameStuckAtMs = elapsedRealtime2;
                    } else if (elapsedRealtime2 - this.playbackMaybeBecameStuckAtMs >= 4000) {
                        throw new IllegalStateException("Playback stuck buffering and not loading");
                    }
                    z3 = !shouldPlayWhenReady() && this.playbackInfo.playbackState == 3;
                    z4 = !this.offloadSchedulingEnabled && this.requestForRendererSleep && z3;
                    playbackInfo = this.playbackInfo;
                    if (playbackInfo.sleepingForOffload != z4) {
                        this.playbackInfo = playbackInfo.copyWithSleepingForOffload(z4);
                    }
                    this.requestForRendererSleep = false;
                    if (!z4 && (i = this.playbackInfo.playbackState) != 4) {
                        if (z3 && i != 2) {
                            j = (i == 3 && this.enabledRendererCount != 0) ? 1000L : 10L;
                        }
                        scheduleNextWork(uptimeMillis, j);
                    }
                    TraceUtil.endSection();
                }
            }
            this.playbackMaybeBecameStuckAtMs = -9223372036854775807L;
            if (shouldPlayWhenReady()) {
            }
            if (this.offloadSchedulingEnabled) {
            }
            playbackInfo = this.playbackInfo;
            if (playbackInfo.sleepingForOffload != z4) {
            }
            this.requestForRendererSleep = false;
            if (!z4) {
                if (z3) {
                }
                scheduleNextWork(uptimeMillis, j);
            }
            TraceUtil.endSection();
        }
        setState(4);
        stopRenderers();
        if (this.playbackInfo.playbackState == 2) {
        }
        this.playbackMaybeBecameStuckAtMs = -9223372036854775807L;
        if (shouldPlayWhenReady()) {
        }
        if (this.offloadSchedulingEnabled) {
        }
        playbackInfo = this.playbackInfo;
        if (playbackInfo.sleepingForOffload != z4) {
        }
        this.requestForRendererSleep = false;
        if (!z4) {
        }
        TraceUtil.endSection();
    }

    private void enableRenderer(int i, boolean z) {
        Renderer renderer = this.renderers[i];
        if (isRendererEnabled(renderer)) {
            return;
        }
        MediaPeriodHolder readingPeriod = this.queue.getReadingPeriod();
        boolean z2 = readingPeriod == this.queue.getPlayingPeriod();
        TrackSelectorResult trackSelectorResult = readingPeriod.getTrackSelectorResult();
        RendererConfiguration rendererConfiguration = trackSelectorResult.rendererConfigurations[i];
        Format[] formats = getFormats(trackSelectorResult.selections[i]);
        boolean z3 = shouldPlayWhenReady() && this.playbackInfo.playbackState == 3;
        boolean z4 = !z && z3;
        this.enabledRendererCount++;
        this.renderersToReset.add(renderer);
        renderer.enable(rendererConfiguration, formats, readingPeriod.sampleStreams[i], this.rendererPositionUs, z4, z2, readingPeriod.getStartPositionRendererTime(), readingPeriod.getRendererOffset());
        renderer.handleMessage(11, new Renderer.WakeupListener() { // from class: com.google.android.exoplayer2.ExoPlayerImplInternal.1
            @Override // com.google.android.exoplayer2.Renderer.WakeupListener
            public void onSleep() {
                ExoPlayerImplInternal.this.requestForRendererSleep = true;
            }

            @Override // com.google.android.exoplayer2.Renderer.WakeupListener
            public void onWakeup() {
                ExoPlayerImplInternal.this.handler.sendEmptyMessage(2);
            }
        });
        this.mediaClock.onRendererEnabled(renderer);
        if (z3) {
            renderer.start();
        }
    }

    private void enableRenderers() {
        enableRenderers(new boolean[this.renderers.length]);
    }

    private void enableRenderers(boolean[] zArr) {
        MediaPeriodHolder readingPeriod = this.queue.getReadingPeriod();
        TrackSelectorResult trackSelectorResult = readingPeriod.getTrackSelectorResult();
        for (int i = 0; i < this.renderers.length; i++) {
            if (!trackSelectorResult.isRendererEnabled(i) && this.renderersToReset.remove(this.renderers[i])) {
                this.renderers[i].reset();
            }
        }
        for (int i2 = 0; i2 < this.renderers.length; i2++) {
            if (trackSelectorResult.isRendererEnabled(i2)) {
                enableRenderer(i2, zArr[i2]);
            }
        }
        readingPeriod.allRenderersInCorrectState = true;
    }

    private void ensureStopped(Renderer renderer) {
        if (renderer.getState() == 2) {
            renderer.stop();
        }
    }

    private ImmutableList extractMetadataFromTrackSelectionArray(ExoTrackSelection[] exoTrackSelectionArr) {
        ImmutableList.Builder builder = new ImmutableList.Builder();
        boolean z = false;
        for (ExoTrackSelection exoTrackSelection : exoTrackSelectionArr) {
            if (exoTrackSelection != null) {
                Metadata metadata = exoTrackSelection.getFormat(0).metadata;
                if (metadata == null) {
                    builder.add((Object) new Metadata(new Metadata.Entry[0]));
                } else {
                    builder.add((Object) metadata);
                    z = true;
                }
            }
        }
        return z ? builder.build() : ImmutableList.of();
    }

    private long getCurrentLiveOffsetUs() {
        PlaybackInfo playbackInfo = this.playbackInfo;
        return getLiveOffsetUs(playbackInfo.timeline, playbackInfo.periodId.periodUid, playbackInfo.positionUs);
    }

    private static Format[] getFormats(ExoTrackSelection exoTrackSelection) {
        int length = exoTrackSelection != null ? exoTrackSelection.length() : 0;
        Format[] formatArr = new Format[length];
        for (int i = 0; i < length; i++) {
            formatArr[i] = exoTrackSelection.getFormat(i);
        }
        return formatArr;
    }

    private long getLiveOffsetUs(Timeline timeline, Object obj, long j) {
        timeline.getWindow(timeline.getPeriodByUid(obj, this.period).windowIndex, this.window);
        Timeline.Window window = this.window;
        if (window.windowStartTimeMs != -9223372036854775807L && window.isLive()) {
            Timeline.Window window2 = this.window;
            if (window2.isDynamic) {
                return Util.msToUs(window2.getCurrentUnixTimeMs() - this.window.windowStartTimeMs) - (j + this.period.getPositionInWindowUs());
            }
        }
        return -9223372036854775807L;
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
            if (isRendererEnabled(rendererArr[i]) && this.renderers[i].getStream() == readingPeriod.sampleStreams[i]) {
                long readingPositionUs = this.renderers[i].getReadingPositionUs();
                if (readingPositionUs == Long.MIN_VALUE) {
                    return Long.MIN_VALUE;
                }
                rendererOffset = Math.max(readingPositionUs, rendererOffset);
            }
            i++;
        }
    }

    private Pair getPlaceholderFirstMediaPeriodPositionUs(Timeline timeline) {
        long j = 0;
        if (timeline.isEmpty()) {
            return Pair.create(PlaybackInfo.getDummyPeriodForEmptyTimeline(), 0L);
        }
        Pair periodPositionUs = timeline.getPeriodPositionUs(this.window, this.period, timeline.getFirstWindowIndex(this.shuffleModeEnabled), -9223372036854775807L);
        MediaSource.MediaPeriodId resolveMediaPeriodIdForAdsAfterPeriodPositionChange = this.queue.resolveMediaPeriodIdForAdsAfterPeriodPositionChange(timeline, periodPositionUs.first, 0L);
        long longValue = ((Long) periodPositionUs.second).longValue();
        if (resolveMediaPeriodIdForAdsAfterPeriodPositionChange.isAd()) {
            timeline.getPeriodByUid(resolveMediaPeriodIdForAdsAfterPeriodPositionChange.periodUid, this.period);
            if (resolveMediaPeriodIdForAdsAfterPeriodPositionChange.adIndexInAdGroup == this.period.getFirstAdIndexToPlay(resolveMediaPeriodIdForAdsAfterPeriodPositionChange.adGroupIndex)) {
                j = this.period.getAdResumePositionUs();
            }
        } else {
            j = longValue;
        }
        return Pair.create(resolveMediaPeriodIdForAdsAfterPeriodPositionChange, Long.valueOf(j));
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

    private void handleContinueLoadingRequested(MediaPeriod mediaPeriod) {
        if (this.queue.isLoading(mediaPeriod)) {
            this.queue.reevaluateBuffer(this.rendererPositionUs);
            maybeContinueLoading();
        }
    }

    private void handleIoException(IOException iOException, int i) {
        ExoPlaybackException createForSource = ExoPlaybackException.createForSource(iOException, i);
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        if (playingPeriod != null) {
            createForSource = createForSource.copyWithMediaPeriodId(playingPeriod.info.id);
        }
        Log.e("ExoPlayerImplInternal", "Playback error", createForSource);
        stopInternal(false, false);
        this.playbackInfo = this.playbackInfo.copyWithPlaybackError(createForSource);
    }

    private void handleLoadingMediaPeriodChanged(boolean z) {
        MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        MediaSource.MediaPeriodId mediaPeriodId = loadingPeriod == null ? this.playbackInfo.periodId : loadingPeriod.info.id;
        boolean z2 = !this.playbackInfo.loadingMediaPeriodId.equals(mediaPeriodId);
        if (z2) {
            this.playbackInfo = this.playbackInfo.copyWithLoadingMediaPeriodId(mediaPeriodId);
        }
        PlaybackInfo playbackInfo = this.playbackInfo;
        playbackInfo.bufferedPositionUs = loadingPeriod == null ? playbackInfo.positionUs : loadingPeriod.getBufferedPositionUs();
        this.playbackInfo.totalBufferedDurationUs = getTotalBufferedDurationUs();
        if ((z2 || z) && loadingPeriod != null && loadingPeriod.prepared) {
            updateLoadControlTrackSelection(loadingPeriod.getTrackGroups(), loadingPeriod.getTrackSelectorResult());
        }
    }

    private void handleMediaSourceListInfoRefreshed(Timeline timeline, boolean z) {
        boolean z2;
        PositionUpdateForPlaylistChange resolvePositionForPlaylistChange = resolvePositionForPlaylistChange(timeline, this.playbackInfo, this.pendingInitialSeekPosition, this.queue, this.repeatMode, this.shuffleModeEnabled, this.window, this.period);
        MediaSource.MediaPeriodId mediaPeriodId = resolvePositionForPlaylistChange.periodId;
        long j = resolvePositionForPlaylistChange.requestedContentPositionUs;
        boolean z3 = resolvePositionForPlaylistChange.forceBufferingState;
        long j2 = resolvePositionForPlaylistChange.periodPositionUs;
        boolean z4 = (this.playbackInfo.periodId.equals(mediaPeriodId) && j2 == this.playbackInfo.positionUs) ? false : true;
        SeekPosition seekPosition = null;
        try {
            if (resolvePositionForPlaylistChange.endPlayback) {
                if (this.playbackInfo.playbackState != 1) {
                    setState(4);
                }
                resetInternal(false, false, false, true);
            }
            try {
                if (z4) {
                    z2 = false;
                    if (!timeline.isEmpty()) {
                        for (MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod(); playingPeriod != null; playingPeriod = playingPeriod.getNext()) {
                            if (playingPeriod.info.id.equals(mediaPeriodId)) {
                                playingPeriod.info = this.queue.getUpdatedMediaPeriodInfo(timeline, playingPeriod.info);
                                playingPeriod.updateClipping();
                            }
                        }
                        j2 = seekToPeriodPosition(mediaPeriodId, j2, z3);
                    }
                } else {
                    try {
                        z2 = false;
                        if (!this.queue.updateQueuedPeriods(timeline, this.rendererPositionUs, getMaxRendererReadPositionUs())) {
                            seekToCurrentPosition(false);
                        }
                    } catch (Throwable th) {
                        th = th;
                        seekPosition = null;
                        PlaybackInfo playbackInfo = this.playbackInfo;
                        SeekPosition seekPosition2 = seekPosition;
                        updatePlaybackSpeedSettingsForNewPeriod(timeline, mediaPeriodId, playbackInfo.timeline, playbackInfo.periodId, resolvePositionForPlaylistChange.setTargetLiveOffset ? j2 : -9223372036854775807L);
                        if (z4 || j != this.playbackInfo.requestedContentPositionUs) {
                            PlaybackInfo playbackInfo2 = this.playbackInfo;
                            Object obj = playbackInfo2.periodId.periodUid;
                            Timeline timeline2 = playbackInfo2.timeline;
                            this.playbackInfo = handlePositionDiscontinuity(mediaPeriodId, j2, j, this.playbackInfo.discontinuityStartPositionUs, z4 && z && !timeline2.isEmpty() && !timeline2.getPeriodByUid(obj, this.period).isPlaceholder, timeline.getIndexOfPeriod(obj) == -1 ? 4 : 3);
                        }
                        resetPendingPauseAtEndOfPeriod();
                        resolvePendingMessagePositions(timeline, this.playbackInfo.timeline);
                        this.playbackInfo = this.playbackInfo.copyWithTimeline(timeline);
                        if (!timeline.isEmpty()) {
                            this.pendingInitialSeekPosition = seekPosition2;
                        }
                        handleLoadingMediaPeriodChanged(false);
                        throw th;
                    }
                }
                PlaybackInfo playbackInfo3 = this.playbackInfo;
                updatePlaybackSpeedSettingsForNewPeriod(timeline, mediaPeriodId, playbackInfo3.timeline, playbackInfo3.periodId, resolvePositionForPlaylistChange.setTargetLiveOffset ? j2 : -9223372036854775807L);
                if (z4 || j != this.playbackInfo.requestedContentPositionUs) {
                    PlaybackInfo playbackInfo4 = this.playbackInfo;
                    Object obj2 = playbackInfo4.periodId.periodUid;
                    Timeline timeline3 = playbackInfo4.timeline;
                    this.playbackInfo = handlePositionDiscontinuity(mediaPeriodId, j2, j, this.playbackInfo.discontinuityStartPositionUs, z4 && z && !timeline3.isEmpty() && !timeline3.getPeriodByUid(obj2, this.period).isPlaceholder, timeline.getIndexOfPeriod(obj2) == -1 ? 4 : 3);
                }
                resetPendingPauseAtEndOfPeriod();
                resolvePendingMessagePositions(timeline, this.playbackInfo.timeline);
                this.playbackInfo = this.playbackInfo.copyWithTimeline(timeline);
                if (!timeline.isEmpty()) {
                    this.pendingInitialSeekPosition = null;
                }
                handleLoadingMediaPeriodChanged(z2);
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    private void handlePeriodPrepared(MediaPeriod mediaPeriod) {
        if (this.queue.isLoading(mediaPeriod)) {
            MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
            loadingPeriod.handlePrepared(this.mediaClock.getPlaybackParameters().speed, this.playbackInfo.timeline);
            updateLoadControlTrackSelection(loadingPeriod.getTrackGroups(), loadingPeriod.getTrackSelectorResult());
            if (loadingPeriod == this.queue.getPlayingPeriod()) {
                resetRendererPosition(loadingPeriod.info.startPositionUs);
                enableRenderers();
                PlaybackInfo playbackInfo = this.playbackInfo;
                MediaSource.MediaPeriodId mediaPeriodId = playbackInfo.periodId;
                long j = loadingPeriod.info.startPositionUs;
                this.playbackInfo = handlePositionDiscontinuity(mediaPeriodId, j, playbackInfo.requestedContentPositionUs, j, false, 5);
            }
            maybeContinueLoading();
        }
    }

    private void handlePlaybackParameters(PlaybackParameters playbackParameters, float f, boolean z, boolean z2) {
        if (z) {
            if (z2) {
                this.playbackInfoUpdate.incrementPendingOperationAcks(1);
            }
            this.playbackInfo = this.playbackInfo.copyWithPlaybackParameters(playbackParameters);
        }
        updateTrackSelectionPlaybackSpeed(playbackParameters.speed);
        for (Renderer renderer : this.renderers) {
            if (renderer != null) {
                renderer.setPlaybackSpeed(f, playbackParameters.speed);
            }
        }
    }

    private void handlePlaybackParameters(PlaybackParameters playbackParameters, boolean z) {
        handlePlaybackParameters(playbackParameters, playbackParameters.speed, true, z);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v4, types: [java.util.List] */
    private PlaybackInfo handlePositionDiscontinuity(MediaSource.MediaPeriodId mediaPeriodId, long j, long j2, long j3, boolean z, int i) {
        ImmutableList immutableList;
        TrackGroupArray trackGroupArray;
        TrackSelectorResult trackSelectorResult;
        this.deliverPendingMessageAtStartPositionRequired = (!this.deliverPendingMessageAtStartPositionRequired && j == this.playbackInfo.positionUs && mediaPeriodId.equals(this.playbackInfo.periodId)) ? false : true;
        resetPendingPauseAtEndOfPeriod();
        PlaybackInfo playbackInfo = this.playbackInfo;
        TrackGroupArray trackGroupArray2 = playbackInfo.trackGroups;
        TrackSelectorResult trackSelectorResult2 = playbackInfo.trackSelectorResult;
        ?? r1 = playbackInfo.staticMetadata;
        if (this.mediaSourceList.isPrepared()) {
            MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
            TrackGroupArray trackGroups = playingPeriod == null ? TrackGroupArray.EMPTY : playingPeriod.getTrackGroups();
            TrackSelectorResult trackSelectorResult3 = playingPeriod == null ? this.emptyTrackSelectorResult : playingPeriod.getTrackSelectorResult();
            ImmutableList extractMetadataFromTrackSelectionArray = extractMetadataFromTrackSelectionArray(trackSelectorResult3.selections);
            if (playingPeriod != null) {
                MediaPeriodInfo mediaPeriodInfo = playingPeriod.info;
                if (mediaPeriodInfo.requestedContentPositionUs != j2) {
                    playingPeriod.info = mediaPeriodInfo.copyWithRequestedContentPositionUs(j2);
                }
            }
            trackGroupArray = trackGroups;
            trackSelectorResult = trackSelectorResult3;
            immutableList = extractMetadataFromTrackSelectionArray;
        } else if (mediaPeriodId.equals(this.playbackInfo.periodId)) {
            immutableList = r1;
            trackGroupArray = trackGroupArray2;
            trackSelectorResult = trackSelectorResult2;
        } else {
            trackGroupArray = TrackGroupArray.EMPTY;
            trackSelectorResult = this.emptyTrackSelectorResult;
            immutableList = ImmutableList.of();
        }
        if (z) {
            this.playbackInfoUpdate.setPositionDiscontinuity(i);
        }
        return this.playbackInfo.copyWithNewPosition(mediaPeriodId, j, j2, j3, getTotalBufferedDurationUs(), trackGroupArray, trackSelectorResult, immutableList);
    }

    private boolean hasReachedServerSideInsertedAdsTransition(Renderer renderer, MediaPeriodHolder mediaPeriodHolder) {
        MediaPeriodHolder next = mediaPeriodHolder.getNext();
        return mediaPeriodHolder.info.isFollowedByTransitionToSameStream && next.prepared && ((renderer instanceof TextRenderer) || (renderer instanceof MetadataRenderer) || renderer.getReadingPositionUs() >= next.getStartPositionRendererTime());
    }

    private boolean hasReadingPeriodFinishedReading() {
        MediaPeriodHolder readingPeriod = this.queue.getReadingPeriod();
        if (!readingPeriod.prepared) {
            return false;
        }
        int i = 0;
        while (true) {
            Renderer[] rendererArr = this.renderers;
            if (i >= rendererArr.length) {
                return true;
            }
            Renderer renderer = rendererArr[i];
            SampleStream sampleStream = readingPeriod.sampleStreams[i];
            if (renderer.getStream() != sampleStream || (sampleStream != null && !renderer.hasReadStreamToEnd() && !hasReachedServerSideInsertedAdsTransition(renderer, readingPeriod))) {
                break;
            }
            i++;
        }
        return false;
    }

    private static boolean isIgnorableServerSideAdInsertionPeriodChange(boolean z, MediaSource.MediaPeriodId mediaPeriodId, long j, MediaSource.MediaPeriodId mediaPeriodId2, Timeline.Period period, long j2) {
        if (!z && j == j2 && mediaPeriodId.periodUid.equals(mediaPeriodId2.periodUid)) {
            return (mediaPeriodId.isAd() && period.isServerSideInsertedAdGroup(mediaPeriodId.adGroupIndex)) ? (period.getAdState(mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup) == 4 || period.getAdState(mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup) == 2) ? false : true : mediaPeriodId2.isAd() && period.isServerSideInsertedAdGroup(mediaPeriodId2.adGroupIndex);
        }
        return false;
    }

    private boolean isLoadingPossible() {
        MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        return (loadingPeriod == null || loadingPeriod.getNextLoadPositionUs() == Long.MIN_VALUE) ? false : true;
    }

    private static boolean isRendererEnabled(Renderer renderer) {
        return renderer.getState() != 0;
    }

    private boolean isTimelineReady() {
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        long j = playingPeriod.info.durationUs;
        return playingPeriod.prepared && (j == -9223372036854775807L || this.playbackInfo.positionUs < j || !shouldPlayWhenReady());
    }

    private static boolean isUsingPlaceholderPeriod(PlaybackInfo playbackInfo, Timeline.Period period) {
        MediaSource.MediaPeriodId mediaPeriodId = playbackInfo.periodId;
        Timeline timeline = playbackInfo.timeline;
        return timeline.isEmpty() || timeline.getPeriodByUid(mediaPeriodId.periodUid, period).isPlaceholder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$release$0() {
        return Boolean.valueOf(this.released);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessageToTargetThread$1(PlayerMessage playerMessage) {
        try {
            deliverMessage(playerMessage);
        } catch (ExoPlaybackException e) {
            Log.e("ExoPlayerImplInternal", "Unexpected error delivering message on external thread.", e);
            throw new RuntimeException(e);
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

    private void maybeNotifyPlaybackInfoChanged() {
        this.playbackInfoUpdate.setPlaybackInfo(this.playbackInfo);
        if (this.playbackInfoUpdate.hasPendingChange) {
            this.playbackInfoUpdateListener.onPlaybackInfoUpdate(this.playbackInfoUpdate);
            this.playbackInfoUpdate = new PlaybackInfoUpdate(this.playbackInfo);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x006d, code lost:
    
        if (r1 < r8.pendingMessages.size()) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0070, code lost:
    
        r3 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0071, code lost:
    
        if (r3 == null) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0075, code lost:
    
        if (r3.resolvedPeriodUid == null) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0077, code lost:
    
        r4 = r3.resolvedPeriodIndex;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0079, code lost:
    
        if (r4 < r0) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x007b, code lost:
    
        if (r4 != r0) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0081, code lost:
    
        if (r3.resolvedPeriodTimeUs > r9) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0096, code lost:
    
        if (r3 == null) goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x009a, code lost:
    
        if (r3.resolvedPeriodUid == null) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x009e, code lost:
    
        if (r3.resolvedPeriodIndex != r0) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00a0, code lost:
    
        r4 = r3.resolvedPeriodTimeUs;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00a4, code lost:
    
        if (r4 <= r9) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00a8, code lost:
    
        if (r4 > r11) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00aa, code lost:
    
        sendMessageToTarget(r3.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00b5, code lost:
    
        if (r3.message.getDeleteAfterDelivery() != false) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00bd, code lost:
    
        if (r3.message.isCanceled() == false) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00c0, code lost:
    
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00ce, code lost:
    
        if (r1 >= r8.pendingMessages.size()) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00d9, code lost:
    
        r3 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00d0, code lost:
    
        r3 = (com.google.android.exoplayer2.ExoPlayerImplInternal.PendingMessageInfo) r8.pendingMessages.get(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00c3, code lost:
    
        r8.pendingMessages.remove(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00db, code lost:
    
        r9 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00e2, code lost:
    
        if (r3.message.getDeleteAfterDelivery() != false) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00f1, code lost:
    
        throw r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00ec, code lost:
    
        r8.pendingMessages.remove(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00f2, code lost:
    
        r8.nextPendingMessageIndexHint = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x00f4, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0083, code lost:
    
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x008b, code lost:
    
        if (r1 >= r8.pendingMessages.size()) goto L82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x008d, code lost:
    
        r3 = (com.google.android.exoplayer2.ExoPlayerImplInternal.PendingMessageInfo) r8.pendingMessages.get(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x0071, code lost:
    
        r3 = (com.google.android.exoplayer2.ExoPlayerImplInternal.PendingMessageInfo) r8.pendingMessages.get(r1);
     */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:34:0x008b -> B:24:0x0070). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void maybeTriggerPendingMessages(long j, long j2) {
        if (!this.pendingMessages.isEmpty() && !this.playbackInfo.periodId.isAd()) {
            if (this.deliverPendingMessageAtStartPositionRequired) {
                j--;
                this.deliverPendingMessageAtStartPositionRequired = false;
            }
            PlaybackInfo playbackInfo = this.playbackInfo;
            int indexOfPeriod = playbackInfo.timeline.getIndexOfPeriod(playbackInfo.periodId.periodUid);
            int min = Math.min(this.nextPendingMessageIndexHint, this.pendingMessages.size());
            PendingMessageInfo pendingMessageInfo = min > 0 ? (PendingMessageInfo) this.pendingMessages.get(min - 1) : null;
            while (pendingMessageInfo != null) {
                int i = pendingMessageInfo.resolvedPeriodIndex;
                if (i <= indexOfPeriod && (i != indexOfPeriod || pendingMessageInfo.resolvedPeriodTimeUs <= j)) {
                    break;
                }
                int i2 = min - 1;
                pendingMessageInfo = i2 > 0 ? (PendingMessageInfo) this.pendingMessages.get(min - 2) : null;
                min = i2;
            }
        }
    }

    private void maybeUpdateLoadingPeriod() {
        MediaPeriodInfo nextMediaPeriodInfo;
        this.queue.reevaluateBuffer(this.rendererPositionUs);
        if (this.queue.shouldLoadNextMediaPeriod() && (nextMediaPeriodInfo = this.queue.getNextMediaPeriodInfo(this.rendererPositionUs, this.playbackInfo)) != null) {
            MediaPeriodHolder enqueueNextMediaPeriodHolder = this.queue.enqueueNextMediaPeriodHolder(this.rendererCapabilities, this.trackSelector, this.loadControl.getAllocator(), this.mediaSourceList, nextMediaPeriodInfo, this.emptyTrackSelectorResult);
            enqueueNextMediaPeriodHolder.mediaPeriod.prepare(this, nextMediaPeriodInfo.startPositionUs);
            if (this.queue.getPlayingPeriod() == enqueueNextMediaPeriodHolder) {
                resetRendererPosition(nextMediaPeriodInfo.startPositionUs);
            }
            handleLoadingMediaPeriodChanged(false);
        }
        if (!this.shouldContinueLoading) {
            maybeContinueLoading();
        } else {
            this.shouldContinueLoading = isLoadingPossible();
            updateIsLoading();
        }
    }

    private void maybeUpdatePlayingPeriod() {
        boolean z;
        boolean z2 = false;
        while (shouldAdvancePlayingPeriod()) {
            if (z2) {
                maybeNotifyPlaybackInfoChanged();
            }
            MediaPeriodHolder mediaPeriodHolder = (MediaPeriodHolder) Assertions.checkNotNull(this.queue.advancePlayingPeriod());
            if (this.playbackInfo.periodId.periodUid.equals(mediaPeriodHolder.info.id.periodUid)) {
                MediaSource.MediaPeriodId mediaPeriodId = this.playbackInfo.periodId;
                if (mediaPeriodId.adGroupIndex == -1) {
                    MediaSource.MediaPeriodId mediaPeriodId2 = mediaPeriodHolder.info.id;
                    if (mediaPeriodId2.adGroupIndex == -1 && mediaPeriodId.nextAdGroupIndex != mediaPeriodId2.nextAdGroupIndex) {
                        z = true;
                        MediaPeriodInfo mediaPeriodInfo = mediaPeriodHolder.info;
                        MediaSource.MediaPeriodId mediaPeriodId3 = mediaPeriodInfo.id;
                        long j = mediaPeriodInfo.startPositionUs;
                        this.playbackInfo = handlePositionDiscontinuity(mediaPeriodId3, j, mediaPeriodInfo.requestedContentPositionUs, j, !z, 0);
                        resetPendingPauseAtEndOfPeriod();
                        updatePlaybackPositions();
                        z2 = true;
                    }
                }
            }
            z = false;
            MediaPeriodInfo mediaPeriodInfo2 = mediaPeriodHolder.info;
            MediaSource.MediaPeriodId mediaPeriodId32 = mediaPeriodInfo2.id;
            long j2 = mediaPeriodInfo2.startPositionUs;
            this.playbackInfo = handlePositionDiscontinuity(mediaPeriodId32, j2, mediaPeriodInfo2.requestedContentPositionUs, j2, !z, 0);
            resetPendingPauseAtEndOfPeriod();
            updatePlaybackPositions();
            z2 = true;
        }
    }

    private void maybeUpdateReadingPeriod() {
        MediaPeriodHolder readingPeriod = this.queue.getReadingPeriod();
        if (readingPeriod == null) {
            return;
        }
        int i = 0;
        if (readingPeriod.getNext() != null && !this.pendingPauseAtEndOfPeriod) {
            if (hasReadingPeriodFinishedReading()) {
                if (readingPeriod.getNext().prepared || this.rendererPositionUs >= readingPeriod.getNext().getStartPositionRendererTime()) {
                    TrackSelectorResult trackSelectorResult = readingPeriod.getTrackSelectorResult();
                    MediaPeriodHolder advanceReadingPeriod = this.queue.advanceReadingPeriod();
                    TrackSelectorResult trackSelectorResult2 = advanceReadingPeriod.getTrackSelectorResult();
                    Timeline timeline = this.playbackInfo.timeline;
                    updatePlaybackSpeedSettingsForNewPeriod(timeline, advanceReadingPeriod.info.id, timeline, readingPeriod.info.id, -9223372036854775807L);
                    if (advanceReadingPeriod.prepared && advanceReadingPeriod.mediaPeriod.readDiscontinuity() != -9223372036854775807L) {
                        setAllRendererStreamsFinal(advanceReadingPeriod.getStartPositionRendererTime());
                        return;
                    }
                    for (int i2 = 0; i2 < this.renderers.length; i2++) {
                        boolean isRendererEnabled = trackSelectorResult.isRendererEnabled(i2);
                        boolean isRendererEnabled2 = trackSelectorResult2.isRendererEnabled(i2);
                        if (isRendererEnabled && !this.renderers[i2].isCurrentStreamFinal()) {
                            boolean z = this.rendererCapabilities[i2].getTrackType() == -2;
                            RendererConfiguration rendererConfiguration = trackSelectorResult.rendererConfigurations[i2];
                            RendererConfiguration rendererConfiguration2 = trackSelectorResult2.rendererConfigurations[i2];
                            if (!isRendererEnabled2 || !rendererConfiguration2.equals(rendererConfiguration) || z) {
                                setCurrentStreamFinal(this.renderers[i2], advanceReadingPeriod.getStartPositionRendererTime());
                            }
                        }
                    }
                    return;
                }
                return;
            }
            return;
        }
        if (!readingPeriod.info.isFinal && !this.pendingPauseAtEndOfPeriod) {
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
                long j = readingPeriod.info.durationUs;
                setCurrentStreamFinal(renderer, (j == -9223372036854775807L || j == Long.MIN_VALUE) ? -9223372036854775807L : readingPeriod.getRendererOffset() + readingPeriod.info.durationUs);
            }
            i++;
        }
    }

    private void maybeUpdateReadingRenderers() {
        MediaPeriodHolder readingPeriod = this.queue.getReadingPeriod();
        if (readingPeriod == null || this.queue.getPlayingPeriod() == readingPeriod || readingPeriod.allRenderersInCorrectState || !replaceStreamsOrDisableRendererForTransition()) {
            return;
        }
        enableRenderers();
    }

    private void mediaSourceListUpdateRequestedInternal() {
        handleMediaSourceListInfoRefreshed(this.mediaSourceList.createTimeline(), true);
    }

    private void moveMediaItemsInternal(MoveMediaItemsMessage moveMediaItemsMessage) {
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
        throw null;
    }

    private void notifyTrackSelectionDiscontinuity() {
        for (MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod(); playingPeriod != null; playingPeriod = playingPeriod.getNext()) {
            for (ExoTrackSelection exoTrackSelection : playingPeriod.getTrackSelectorResult().selections) {
                if (exoTrackSelection != null) {
                    exoTrackSelection.onDiscontinuity();
                }
            }
        }
    }

    private void notifyTrackSelectionPlayWhenReadyChanged(boolean z) {
        for (MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod(); playingPeriod != null; playingPeriod = playingPeriod.getNext()) {
            for (ExoTrackSelection exoTrackSelection : playingPeriod.getTrackSelectorResult().selections) {
                if (exoTrackSelection != null) {
                    exoTrackSelection.onPlayWhenReadyChanged(z);
                }
            }
        }
    }

    private void notifyTrackSelectionRebuffer() {
        for (MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod(); playingPeriod != null; playingPeriod = playingPeriod.getNext()) {
            for (ExoTrackSelection exoTrackSelection : playingPeriod.getTrackSelectorResult().selections) {
                if (exoTrackSelection != null) {
                    exoTrackSelection.onRebuffer();
                }
            }
        }
    }

    private void prepareInternal() {
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
        resetInternal(false, false, false, true);
        this.loadControl.onPrepared();
        setState(this.playbackInfo.timeline.isEmpty() ? 4 : 2);
        this.mediaSourceList.prepare(this.bandwidthMeter.getTransferListener());
        this.handler.sendEmptyMessage(2);
    }

    private void releaseInternal() {
        resetInternal(true, false, true, false);
        this.loadControl.onReleased();
        setState(1);
        HandlerThread handlerThread = this.internalPlaybackThread;
        if (handlerThread != null) {
            handlerThread.quit();
        }
        synchronized (this) {
            this.released = true;
            notifyAll();
        }
    }

    private void removeMediaItemsInternal(int i, int i2, ShuffleOrder shuffleOrder) {
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
        handleMediaSourceListInfoRefreshed(this.mediaSourceList.removeMediaSourceRange(i, i2, shuffleOrder), false);
    }

    private boolean replaceStreamsOrDisableRendererForTransition() {
        MediaPeriodHolder readingPeriod = this.queue.getReadingPeriod();
        TrackSelectorResult trackSelectorResult = readingPeriod.getTrackSelectorResult();
        int i = 0;
        boolean z = false;
        while (true) {
            Renderer[] rendererArr = this.renderers;
            if (i >= rendererArr.length) {
                return !z;
            }
            Renderer renderer = rendererArr[i];
            if (isRendererEnabled(renderer)) {
                boolean z2 = renderer.getStream() != readingPeriod.sampleStreams[i];
                if (!trackSelectorResult.isRendererEnabled(i) || z2) {
                    if (!renderer.isCurrentStreamFinal()) {
                        renderer.replaceStream(getFormats(trackSelectorResult.selections[i]), readingPeriod.sampleStreams[i], readingPeriod.getStartPositionRendererTime(), readingPeriod.getRendererOffset());
                    } else if (renderer.isEnded()) {
                        disableRenderer(renderer);
                    } else {
                        z = true;
                    }
                }
            }
            i++;
        }
    }

    private void reselectTracksInternal() {
        float f = this.mediaClock.getPlaybackParameters().speed;
        MediaPeriodHolder readingPeriod = this.queue.getReadingPeriod();
        boolean z = true;
        for (MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod(); playingPeriod != null && playingPeriod.prepared; playingPeriod = playingPeriod.getNext()) {
            TrackSelectorResult selectTracks = playingPeriod.selectTracks(f, this.playbackInfo.timeline);
            if (!selectTracks.isEquivalent(playingPeriod.getTrackSelectorResult())) {
                MediaPeriodQueue mediaPeriodQueue = this.queue;
                if (z) {
                    MediaPeriodHolder playingPeriod2 = mediaPeriodQueue.getPlayingPeriod();
                    boolean removeAfter = this.queue.removeAfter(playingPeriod2);
                    boolean[] zArr = new boolean[this.renderers.length];
                    long applyTrackSelection = playingPeriod2.applyTrackSelection(selectTracks, this.playbackInfo.positionUs, removeAfter, zArr);
                    PlaybackInfo playbackInfo = this.playbackInfo;
                    boolean z2 = (playbackInfo.playbackState == 4 || applyTrackSelection == playbackInfo.positionUs) ? false : true;
                    PlaybackInfo playbackInfo2 = this.playbackInfo;
                    this.playbackInfo = handlePositionDiscontinuity(playbackInfo2.periodId, applyTrackSelection, playbackInfo2.requestedContentPositionUs, playbackInfo2.discontinuityStartPositionUs, z2, 5);
                    if (z2) {
                        resetRendererPosition(applyTrackSelection);
                    }
                    boolean[] zArr2 = new boolean[this.renderers.length];
                    int i = 0;
                    while (true) {
                        Renderer[] rendererArr = this.renderers;
                        if (i >= rendererArr.length) {
                            break;
                        }
                        Renderer renderer = rendererArr[i];
                        boolean isRendererEnabled = isRendererEnabled(renderer);
                        zArr2[i] = isRendererEnabled;
                        SampleStream sampleStream = playingPeriod2.sampleStreams[i];
                        if (isRendererEnabled) {
                            if (sampleStream != renderer.getStream()) {
                                disableRenderer(renderer);
                            } else if (zArr[i]) {
                                renderer.resetPosition(this.rendererPositionUs);
                            }
                        }
                        i++;
                    }
                    enableRenderers(zArr2);
                } else {
                    mediaPeriodQueue.removeAfter(playingPeriod);
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
            if (playingPeriod == readingPeriod) {
                z = false;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x00c3  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00d5  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:55:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00dc  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00d0  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00be  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void resetInternal(boolean z, boolean z2, boolean z3, boolean z4) {
        MediaSource.MediaPeriodId mediaPeriodId;
        long j;
        boolean z5;
        this.handler.removeMessages(2);
        this.pendingRecoverableRendererError = null;
        this.isRebuffering = false;
        this.mediaClock.stop();
        this.rendererPositionUs = 1000000000000L;
        for (Renderer renderer : this.renderers) {
            try {
                disableRenderer(renderer);
            } catch (ExoPlaybackException | RuntimeException e) {
                Log.e("ExoPlayerImplInternal", "Disable failed.", e);
            }
        }
        if (z) {
            for (Renderer renderer2 : this.renderers) {
                if (this.renderersToReset.remove(renderer2)) {
                    try {
                        renderer2.reset();
                    } catch (RuntimeException e2) {
                        Log.e("ExoPlayerImplInternal", "Reset failed.", e2);
                    }
                }
            }
        }
        this.enabledRendererCount = 0;
        PlaybackInfo playbackInfo = this.playbackInfo;
        MediaSource.MediaPeriodId mediaPeriodId2 = playbackInfo.periodId;
        long j2 = playbackInfo.positionUs;
        long j3 = (this.playbackInfo.periodId.isAd() || isUsingPlaceholderPeriod(this.playbackInfo, this.period)) ? this.playbackInfo.requestedContentPositionUs : this.playbackInfo.positionUs;
        if (z2) {
            this.pendingInitialSeekPosition = null;
            Pair placeholderFirstMediaPeriodPositionUs = getPlaceholderFirstMediaPeriodPositionUs(this.playbackInfo.timeline);
            mediaPeriodId2 = (MediaSource.MediaPeriodId) placeholderFirstMediaPeriodPositionUs.first;
            j2 = ((Long) placeholderFirstMediaPeriodPositionUs.second).longValue();
            j3 = -9223372036854775807L;
            if (!mediaPeriodId2.equals(this.playbackInfo.periodId)) {
                z5 = true;
                mediaPeriodId = mediaPeriodId2;
                j = j2;
                this.queue.clear();
                this.shouldContinueLoading = false;
                PlaybackInfo playbackInfo2 = this.playbackInfo;
                Timeline timeline = playbackInfo2.timeline;
                int i = playbackInfo2.playbackState;
                ExoPlaybackException exoPlaybackException = z4 ? null : playbackInfo2.playbackError;
                TrackGroupArray trackGroupArray = !z5 ? TrackGroupArray.EMPTY : playbackInfo2.trackGroups;
                TrackSelectorResult trackSelectorResult = !z5 ? this.emptyTrackSelectorResult : playbackInfo2.trackSelectorResult;
                List of = !z5 ? ImmutableList.of() : playbackInfo2.staticMetadata;
                PlaybackInfo playbackInfo3 = this.playbackInfo;
                this.playbackInfo = new PlaybackInfo(timeline, mediaPeriodId, j3, j, i, exoPlaybackException, false, trackGroupArray, trackSelectorResult, of, mediaPeriodId, playbackInfo3.playWhenReady, playbackInfo3.playbackSuppressionReason, playbackInfo3.playbackParameters, j, 0L, j, false);
                if (z3) {
                    return;
                }
                this.mediaSourceList.release();
                return;
            }
        }
        mediaPeriodId = mediaPeriodId2;
        j = j2;
        z5 = false;
        this.queue.clear();
        this.shouldContinueLoading = false;
        PlaybackInfo playbackInfo22 = this.playbackInfo;
        Timeline timeline2 = playbackInfo22.timeline;
        int i2 = playbackInfo22.playbackState;
        ExoPlaybackException exoPlaybackException2 = z4 ? null : playbackInfo22.playbackError;
        TrackGroupArray trackGroupArray2 = !z5 ? TrackGroupArray.EMPTY : playbackInfo22.trackGroups;
        TrackSelectorResult trackSelectorResult2 = !z5 ? this.emptyTrackSelectorResult : playbackInfo22.trackSelectorResult;
        List of2 = !z5 ? ImmutableList.of() : playbackInfo22.staticMetadata;
        PlaybackInfo playbackInfo32 = this.playbackInfo;
        this.playbackInfo = new PlaybackInfo(timeline2, mediaPeriodId, j3, j, i2, exoPlaybackException2, false, trackGroupArray2, trackSelectorResult2, of2, mediaPeriodId, playbackInfo32.playWhenReady, playbackInfo32.playbackSuppressionReason, playbackInfo32.playbackParameters, j, 0L, j, false);
        if (z3) {
        }
    }

    private void resetPendingPauseAtEndOfPeriod() {
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        this.pendingPauseAtEndOfPeriod = playingPeriod != null && playingPeriod.info.isLastInTimelineWindow && this.pauseAtEndOfWindow;
    }

    private void resetRendererPosition(long j) {
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        long rendererTime = playingPeriod == null ? j + 1000000000000L : playingPeriod.toRendererTime(j);
        this.rendererPositionUs = rendererTime;
        this.mediaClock.resetPosition(rendererTime);
        for (Renderer renderer : this.renderers) {
            if (isRendererEnabled(renderer)) {
                renderer.resetPosition(this.rendererPositionUs);
            }
        }
        notifyTrackSelectionDiscontinuity();
    }

    private static void resolvePendingMessageEndOfStreamPosition(Timeline timeline, PendingMessageInfo pendingMessageInfo, Timeline.Window window, Timeline.Period period) {
        int i = timeline.getWindow(timeline.getPeriodByUid(pendingMessageInfo.resolvedPeriodUid, period).windowIndex, window).lastPeriodIndex;
        Object obj = timeline.getPeriod(i, period, true).uid;
        long j = period.durationUs;
        pendingMessageInfo.setResolvedPosition(i, j != -9223372036854775807L ? j - 1 : Long.MAX_VALUE, obj);
    }

    private static boolean resolvePendingMessagePosition(PendingMessageInfo pendingMessageInfo, Timeline timeline, Timeline timeline2, int i, boolean z, Timeline.Window window, Timeline.Period period) {
        Object obj = pendingMessageInfo.resolvedPeriodUid;
        if (obj == null) {
            Pair resolveSeekPositionUs = resolveSeekPositionUs(timeline, new SeekPosition(pendingMessageInfo.message.getTimeline(), pendingMessageInfo.message.getMediaItemIndex(), pendingMessageInfo.message.getPositionMs() == Long.MIN_VALUE ? -9223372036854775807L : Util.msToUs(pendingMessageInfo.message.getPositionMs())), false, i, z, window, period);
            if (resolveSeekPositionUs == null) {
                return false;
            }
            pendingMessageInfo.setResolvedPosition(timeline.getIndexOfPeriod(resolveSeekPositionUs.first), ((Long) resolveSeekPositionUs.second).longValue(), resolveSeekPositionUs.first);
            if (pendingMessageInfo.message.getPositionMs() == Long.MIN_VALUE) {
                resolvePendingMessageEndOfStreamPosition(timeline, pendingMessageInfo, window, period);
            }
            return true;
        }
        int indexOfPeriod = timeline.getIndexOfPeriod(obj);
        if (indexOfPeriod == -1) {
            return false;
        }
        if (pendingMessageInfo.message.getPositionMs() == Long.MIN_VALUE) {
            resolvePendingMessageEndOfStreamPosition(timeline, pendingMessageInfo, window, period);
            return true;
        }
        pendingMessageInfo.resolvedPeriodIndex = indexOfPeriod;
        timeline2.getPeriodByUid(pendingMessageInfo.resolvedPeriodUid, period);
        if (period.isPlaceholder && timeline2.getWindow(period.windowIndex, window).firstPeriodIndex == timeline2.getIndexOfPeriod(pendingMessageInfo.resolvedPeriodUid)) {
            Pair periodPositionUs = timeline.getPeriodPositionUs(window, period, timeline.getPeriodByUid(pendingMessageInfo.resolvedPeriodUid, period).windowIndex, pendingMessageInfo.resolvedPeriodTimeUs + period.getPositionInWindowUs());
            pendingMessageInfo.setResolvedPosition(timeline.getIndexOfPeriod(periodPositionUs.first), ((Long) periodPositionUs.second).longValue(), periodPositionUs.first);
        }
        return true;
    }

    private void resolvePendingMessagePositions(Timeline timeline, Timeline timeline2) {
        if (timeline.isEmpty() && timeline2.isEmpty()) {
            return;
        }
        for (int size = this.pendingMessages.size() - 1; size >= 0; size--) {
            if (!resolvePendingMessagePosition((PendingMessageInfo) this.pendingMessages.get(size), timeline, timeline2, this.repeatMode, this.shuffleModeEnabled, this.window, this.period)) {
                ((PendingMessageInfo) this.pendingMessages.get(size)).message.markAsProcessed(false);
                this.pendingMessages.remove(size);
            }
        }
        Collections.sort(this.pendingMessages);
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x015a  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0184  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0196  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x01b9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x01c2  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0178  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static PositionUpdateForPlaylistChange resolvePositionForPlaylistChange(Timeline timeline, PlaybackInfo playbackInfo, SeekPosition seekPosition, MediaPeriodQueue mediaPeriodQueue, int i, boolean z, Timeline.Window window, Timeline.Period period) {
        int i2;
        MediaSource.MediaPeriodId mediaPeriodId;
        int i3;
        long j;
        int i4;
        boolean z2;
        boolean z3;
        boolean z4;
        int i5;
        boolean z5;
        MediaPeriodQueue mediaPeriodQueue2;
        long j2;
        MediaSource.MediaPeriodId resolveMediaPeriodIdForAdsAfterPeriodPositionChange;
        boolean z6;
        MediaSource.MediaPeriodId mediaPeriodId2;
        int i6;
        boolean z7;
        int i7;
        if (timeline.isEmpty()) {
            return new PositionUpdateForPlaylistChange(PlaybackInfo.getDummyPeriodForEmptyTimeline(), 0L, -9223372036854775807L, false, true, false);
        }
        MediaSource.MediaPeriodId mediaPeriodId3 = playbackInfo.periodId;
        Object obj = mediaPeriodId3.periodUid;
        boolean isUsingPlaceholderPeriod = isUsingPlaceholderPeriod(playbackInfo, period);
        long j3 = (playbackInfo.periodId.isAd() || isUsingPlaceholderPeriod) ? playbackInfo.requestedContentPositionUs : playbackInfo.positionUs;
        if (seekPosition != null) {
            i2 = -1;
            Pair resolveSeekPositionUs = resolveSeekPositionUs(timeline, seekPosition, true, i, z, window, period);
            if (resolveSeekPositionUs != null) {
                if (seekPosition.windowPositionUs == -9223372036854775807L) {
                    i7 = timeline.getPeriodByUid(resolveSeekPositionUs.first, period).windowIndex;
                    j = j3;
                    z7 = false;
                } else {
                    obj = resolveSeekPositionUs.first;
                    j = ((Long) resolveSeekPositionUs.second).longValue();
                    z7 = true;
                    i7 = -1;
                }
                z4 = z7;
                if (playbackInfo.playbackState == 4) {
                    i4 = i7;
                    mediaPeriodId = mediaPeriodId3;
                    z2 = true;
                } else {
                    i4 = i7;
                    mediaPeriodId = mediaPeriodId3;
                    z2 = false;
                }
                z3 = false;
                if (i4 == i2) {
                }
                resolveMediaPeriodIdForAdsAfterPeriodPositionChange = mediaPeriodQueue2.resolveMediaPeriodIdForAdsAfterPeriodPositionChange(timeline, obj, j);
                int i8 = resolveMediaPeriodIdForAdsAfterPeriodPositionChange.nextAdGroupIndex;
                if (mediaPeriodId.periodUid.equals(obj)) {
                }
                mediaPeriodId2 = mediaPeriodId;
                boolean isIgnorableServerSideAdInsertionPeriodChange = isIgnorableServerSideAdInsertionPeriodChange(isUsingPlaceholderPeriod, mediaPeriodId, j3, resolveMediaPeriodIdForAdsAfterPeriodPositionChange, timeline.getPeriodByUid(obj, period), j2);
                if (!z6) {
                }
                resolveMediaPeriodIdForAdsAfterPeriodPositionChange = mediaPeriodId2;
                if (resolveMediaPeriodIdForAdsAfterPeriodPositionChange.isAd()) {
                }
                return new PositionUpdateForPlaylistChange(resolveMediaPeriodIdForAdsAfterPeriodPositionChange, j, j2, z2, z3, z4);
            }
            i4 = timeline.getFirstWindowIndex(z);
            j = j3;
            mediaPeriodId = mediaPeriodId3;
            z2 = false;
            z3 = true;
            z4 = false;
            if (i4 == i2) {
                Pair periodPositionUs = timeline.getPeriodPositionUs(window, period, i4, -9223372036854775807L);
                obj = periodPositionUs.first;
                j = ((Long) periodPositionUs.second).longValue();
                mediaPeriodQueue2 = mediaPeriodQueue;
                j2 = -9223372036854775807L;
            } else {
                mediaPeriodQueue2 = mediaPeriodQueue;
                j2 = j;
            }
            resolveMediaPeriodIdForAdsAfterPeriodPositionChange = mediaPeriodQueue2.resolveMediaPeriodIdForAdsAfterPeriodPositionChange(timeline, obj, j);
            int i82 = resolveMediaPeriodIdForAdsAfterPeriodPositionChange.nextAdGroupIndex;
            z6 = (mediaPeriodId.periodUid.equals(obj) || mediaPeriodId.isAd() || resolveMediaPeriodIdForAdsAfterPeriodPositionChange.isAd() || !(i82 != i2 || ((i6 = mediaPeriodId.nextAdGroupIndex) != i2 && i82 >= i6))) ? false : true;
            mediaPeriodId2 = mediaPeriodId;
            boolean isIgnorableServerSideAdInsertionPeriodChange2 = isIgnorableServerSideAdInsertionPeriodChange(isUsingPlaceholderPeriod, mediaPeriodId, j3, resolveMediaPeriodIdForAdsAfterPeriodPositionChange, timeline.getPeriodByUid(obj, period), j2);
            if (!z6 || isIgnorableServerSideAdInsertionPeriodChange2) {
                resolveMediaPeriodIdForAdsAfterPeriodPositionChange = mediaPeriodId2;
            }
            if (resolveMediaPeriodIdForAdsAfterPeriodPositionChange.isAd()) {
                if (resolveMediaPeriodIdForAdsAfterPeriodPositionChange.equals(mediaPeriodId2)) {
                    j = playbackInfo.positionUs;
                } else {
                    timeline.getPeriodByUid(resolveMediaPeriodIdForAdsAfterPeriodPositionChange.periodUid, period);
                    j = resolveMediaPeriodIdForAdsAfterPeriodPositionChange.adIndexInAdGroup == period.getFirstAdIndexToPlay(resolveMediaPeriodIdForAdsAfterPeriodPositionChange.adGroupIndex) ? period.getAdResumePositionUs() : 0L;
                }
            }
            return new PositionUpdateForPlaylistChange(resolveMediaPeriodIdForAdsAfterPeriodPositionChange, j, j2, z2, z3, z4);
        }
        i2 = -1;
        if (!playbackInfo.timeline.isEmpty()) {
            if (timeline.getIndexOfPeriod(obj) == -1) {
                Object resolveSubsequentPeriod = resolveSubsequentPeriod(window, period, i, z, obj, playbackInfo.timeline, timeline);
                if (resolveSubsequentPeriod == null) {
                    i3 = timeline.getFirstWindowIndex(z);
                    mediaPeriodId = mediaPeriodId3;
                    z5 = true;
                    i4 = i3;
                    z3 = z5;
                    j = j3;
                    z2 = false;
                    z4 = false;
                    if (i4 == i2) {
                    }
                    resolveMediaPeriodIdForAdsAfterPeriodPositionChange = mediaPeriodQueue2.resolveMediaPeriodIdForAdsAfterPeriodPositionChange(timeline, obj, j);
                    int i822 = resolveMediaPeriodIdForAdsAfterPeriodPositionChange.nextAdGroupIndex;
                    if (mediaPeriodId.periodUid.equals(obj)) {
                    }
                    mediaPeriodId2 = mediaPeriodId;
                    boolean isIgnorableServerSideAdInsertionPeriodChange22 = isIgnorableServerSideAdInsertionPeriodChange(isUsingPlaceholderPeriod, mediaPeriodId, j3, resolveMediaPeriodIdForAdsAfterPeriodPositionChange, timeline.getPeriodByUid(obj, period), j2);
                    if (!z6) {
                    }
                    resolveMediaPeriodIdForAdsAfterPeriodPositionChange = mediaPeriodId2;
                    if (resolveMediaPeriodIdForAdsAfterPeriodPositionChange.isAd()) {
                    }
                    return new PositionUpdateForPlaylistChange(resolveMediaPeriodIdForAdsAfterPeriodPositionChange, j, j2, z2, z3, z4);
                }
                i3 = timeline.getPeriodByUid(resolveSubsequentPeriod, period).windowIndex;
                mediaPeriodId = mediaPeriodId3;
            } else if (j3 == -9223372036854775807L) {
                i5 = timeline.getPeriodByUid(obj, period).windowIndex;
            } else {
                if (isUsingPlaceholderPeriod) {
                    mediaPeriodId = mediaPeriodId3;
                    playbackInfo.timeline.getPeriodByUid(mediaPeriodId.periodUid, period);
                    if (playbackInfo.timeline.getWindow(period.windowIndex, window).firstPeriodIndex == playbackInfo.timeline.getIndexOfPeriod(mediaPeriodId.periodUid)) {
                        Pair periodPositionUs2 = timeline.getPeriodPositionUs(window, period, timeline.getPeriodByUid(obj, period).windowIndex, j3 + period.getPositionInWindowUs());
                        obj = periodPositionUs2.first;
                        j = ((Long) periodPositionUs2.second).longValue();
                    } else {
                        j = j3;
                    }
                    i4 = -1;
                    z2 = false;
                    z3 = false;
                    z4 = true;
                    if (i4 == i2) {
                    }
                    resolveMediaPeriodIdForAdsAfterPeriodPositionChange = mediaPeriodQueue2.resolveMediaPeriodIdForAdsAfterPeriodPositionChange(timeline, obj, j);
                    int i8222 = resolveMediaPeriodIdForAdsAfterPeriodPositionChange.nextAdGroupIndex;
                    if (mediaPeriodId.periodUid.equals(obj)) {
                    }
                    mediaPeriodId2 = mediaPeriodId;
                    boolean isIgnorableServerSideAdInsertionPeriodChange222 = isIgnorableServerSideAdInsertionPeriodChange(isUsingPlaceholderPeriod, mediaPeriodId, j3, resolveMediaPeriodIdForAdsAfterPeriodPositionChange, timeline.getPeriodByUid(obj, period), j2);
                    if (!z6) {
                    }
                    resolveMediaPeriodIdForAdsAfterPeriodPositionChange = mediaPeriodId2;
                    if (resolveMediaPeriodIdForAdsAfterPeriodPositionChange.isAd()) {
                    }
                    return new PositionUpdateForPlaylistChange(resolveMediaPeriodIdForAdsAfterPeriodPositionChange, j, j2, z2, z3, z4);
                }
                mediaPeriodId = mediaPeriodId3;
                i3 = -1;
            }
            z5 = false;
            i4 = i3;
            z3 = z5;
            j = j3;
            z2 = false;
            z4 = false;
            if (i4 == i2) {
            }
            resolveMediaPeriodIdForAdsAfterPeriodPositionChange = mediaPeriodQueue2.resolveMediaPeriodIdForAdsAfterPeriodPositionChange(timeline, obj, j);
            int i82222 = resolveMediaPeriodIdForAdsAfterPeriodPositionChange.nextAdGroupIndex;
            if (mediaPeriodId.periodUid.equals(obj)) {
            }
            mediaPeriodId2 = mediaPeriodId;
            boolean isIgnorableServerSideAdInsertionPeriodChange2222 = isIgnorableServerSideAdInsertionPeriodChange(isUsingPlaceholderPeriod, mediaPeriodId, j3, resolveMediaPeriodIdForAdsAfterPeriodPositionChange, timeline.getPeriodByUid(obj, period), j2);
            if (!z6) {
            }
            resolveMediaPeriodIdForAdsAfterPeriodPositionChange = mediaPeriodId2;
            if (resolveMediaPeriodIdForAdsAfterPeriodPositionChange.isAd()) {
            }
            return new PositionUpdateForPlaylistChange(resolveMediaPeriodIdForAdsAfterPeriodPositionChange, j, j2, z2, z3, z4);
        }
        i5 = timeline.getFirstWindowIndex(z);
        i4 = i5;
        j = j3;
        mediaPeriodId = mediaPeriodId3;
        z2 = false;
        z3 = false;
        z4 = false;
        if (i4 == i2) {
        }
        resolveMediaPeriodIdForAdsAfterPeriodPositionChange = mediaPeriodQueue2.resolveMediaPeriodIdForAdsAfterPeriodPositionChange(timeline, obj, j);
        int i822222 = resolveMediaPeriodIdForAdsAfterPeriodPositionChange.nextAdGroupIndex;
        if (mediaPeriodId.periodUid.equals(obj)) {
        }
        mediaPeriodId2 = mediaPeriodId;
        boolean isIgnorableServerSideAdInsertionPeriodChange22222 = isIgnorableServerSideAdInsertionPeriodChange(isUsingPlaceholderPeriod, mediaPeriodId, j3, resolveMediaPeriodIdForAdsAfterPeriodPositionChange, timeline.getPeriodByUid(obj, period), j2);
        if (!z6) {
        }
        resolveMediaPeriodIdForAdsAfterPeriodPositionChange = mediaPeriodId2;
        if (resolveMediaPeriodIdForAdsAfterPeriodPositionChange.isAd()) {
        }
        return new PositionUpdateForPlaylistChange(resolveMediaPeriodIdForAdsAfterPeriodPositionChange, j, j2, z2, z3, z4);
    }

    private static Pair resolveSeekPositionUs(Timeline timeline, SeekPosition seekPosition, boolean z, int i, boolean z2, Timeline.Window window, Timeline.Period period) {
        Pair periodPositionUs;
        Object resolveSubsequentPeriod;
        Timeline timeline2 = seekPosition.timeline;
        if (timeline.isEmpty()) {
            return null;
        }
        Timeline timeline3 = timeline2.isEmpty() ? timeline : timeline2;
        try {
            periodPositionUs = timeline3.getPeriodPositionUs(window, period, seekPosition.windowIndex, seekPosition.windowPositionUs);
        } catch (IndexOutOfBoundsException unused) {
        }
        if (timeline.equals(timeline3)) {
            return periodPositionUs;
        }
        if (timeline.getIndexOfPeriod(periodPositionUs.first) != -1) {
            return (timeline3.getPeriodByUid(periodPositionUs.first, period).isPlaceholder && timeline3.getWindow(period.windowIndex, window).firstPeriodIndex == timeline3.getIndexOfPeriod(periodPositionUs.first)) ? timeline.getPeriodPositionUs(window, period, timeline.getPeriodByUid(periodPositionUs.first, period).windowIndex, seekPosition.windowPositionUs) : periodPositionUs;
        }
        if (z && (resolveSubsequentPeriod = resolveSubsequentPeriod(window, period, i, z2, periodPositionUs.first, timeline3, timeline)) != null) {
            return timeline.getPeriodPositionUs(window, period, timeline.getPeriodByUid(resolveSubsequentPeriod, period).windowIndex, -9223372036854775807L);
        }
        return null;
    }

    static Object resolveSubsequentPeriod(Timeline.Window window, Timeline.Period period, int i, boolean z, Object obj, Timeline timeline, Timeline timeline2) {
        int indexOfPeriod = timeline.getIndexOfPeriod(obj);
        int periodCount = timeline.getPeriodCount();
        int i2 = indexOfPeriod;
        int i3 = -1;
        for (int i4 = 0; i4 < periodCount && i3 == -1; i4++) {
            i2 = timeline.getNextPeriodIndex(i2, period, window, i, z);
            if (i2 == -1) {
                break;
            }
            i3 = timeline2.getIndexOfPeriod(timeline.getUidOfPeriod(i2));
        }
        if (i3 == -1) {
            return null;
        }
        return timeline2.getUidOfPeriod(i3);
    }

    private void scheduleNextWork(long j, long j2) {
        this.handler.sendEmptyMessageAtTime(2, j + j2);
    }

    private void seekToCurrentPosition(boolean z) {
        MediaSource.MediaPeriodId mediaPeriodId = this.queue.getPlayingPeriod().info.id;
        long seekToPeriodPosition = seekToPeriodPosition(mediaPeriodId, this.playbackInfo.positionUs, true, false);
        if (seekToPeriodPosition != this.playbackInfo.positionUs) {
            PlaybackInfo playbackInfo = this.playbackInfo;
            this.playbackInfo = handlePositionDiscontinuity(mediaPeriodId, seekToPeriodPosition, playbackInfo.requestedContentPositionUs, playbackInfo.discontinuityStartPositionUs, z, 5);
        }
    }

    private void seekToInternal(SeekPosition seekPosition) {
        long j;
        long j2;
        boolean z;
        MediaSource.MediaPeriodId mediaPeriodId;
        long j3;
        long j4;
        long seekToPeriodPosition;
        boolean z2;
        long j5;
        PlaybackInfo playbackInfo;
        int i;
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
        Pair resolveSeekPositionUs = resolveSeekPositionUs(this.playbackInfo.timeline, seekPosition, true, this.repeatMode, this.shuffleModeEnabled, this.window, this.period);
        if (resolveSeekPositionUs == null) {
            Pair placeholderFirstMediaPeriodPositionUs = getPlaceholderFirstMediaPeriodPositionUs(this.playbackInfo.timeline);
            mediaPeriodId = (MediaSource.MediaPeriodId) placeholderFirstMediaPeriodPositionUs.first;
            long longValue = ((Long) placeholderFirstMediaPeriodPositionUs.second).longValue();
            z = !this.playbackInfo.timeline.isEmpty();
            j = longValue;
            j2 = -9223372036854775807L;
        } else {
            Object obj = resolveSeekPositionUs.first;
            long longValue2 = ((Long) resolveSeekPositionUs.second).longValue();
            long j6 = seekPosition.windowPositionUs == -9223372036854775807L ? -9223372036854775807L : longValue2;
            MediaSource.MediaPeriodId resolveMediaPeriodIdForAdsAfterPeriodPositionChange = this.queue.resolveMediaPeriodIdForAdsAfterPeriodPositionChange(this.playbackInfo.timeline, obj, longValue2);
            if (resolveMediaPeriodIdForAdsAfterPeriodPositionChange.isAd()) {
                this.playbackInfo.timeline.getPeriodByUid(resolveMediaPeriodIdForAdsAfterPeriodPositionChange.periodUid, this.period);
                longValue2 = this.period.getFirstAdIndexToPlay(resolveMediaPeriodIdForAdsAfterPeriodPositionChange.adGroupIndex) == resolveMediaPeriodIdForAdsAfterPeriodPositionChange.adIndexInAdGroup ? this.period.getAdResumePositionUs() : 0L;
            } else if (seekPosition.windowPositionUs != -9223372036854775807L) {
                j = longValue2;
                j2 = j6;
                z = false;
                mediaPeriodId = resolveMediaPeriodIdForAdsAfterPeriodPositionChange;
            }
            j = longValue2;
            j2 = j6;
            z = true;
            mediaPeriodId = resolveMediaPeriodIdForAdsAfterPeriodPositionChange;
        }
        try {
            if (this.playbackInfo.timeline.isEmpty()) {
                this.pendingInitialSeekPosition = seekPosition;
            } else if (resolveSeekPositionUs == null) {
                if (this.playbackInfo.playbackState != 1) {
                    setState(4);
                }
                resetInternal(false, true, false, true);
            } else {
                try {
                    if (mediaPeriodId.equals(this.playbackInfo.periodId)) {
                        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
                        j4 = (playingPeriod == null || !playingPeriod.prepared || j == 0) ? j : playingPeriod.mediaPeriod.getAdjustedSeekPositionUs(j, this.seekParameters);
                        if (Util.usToMs(j4) == Util.usToMs(this.playbackInfo.positionUs) && ((i = (playbackInfo = this.playbackInfo).playbackState) == 2 || i == 3)) {
                            j5 = playbackInfo.positionUs;
                            this.playbackInfo = handlePositionDiscontinuity(mediaPeriodId, j5, j2, j5, z, 2);
                        }
                    } else {
                        j4 = j;
                    }
                    PlaybackInfo playbackInfo2 = this.playbackInfo;
                    Timeline timeline = playbackInfo2.timeline;
                    updatePlaybackSpeedSettingsForNewPeriod(timeline, mediaPeriodId, timeline, playbackInfo2.periodId, j2);
                    z = z2;
                    j5 = seekToPeriodPosition;
                    this.playbackInfo = handlePositionDiscontinuity(mediaPeriodId, j5, j2, j5, z, 2);
                } catch (Throwable th) {
                    th = th;
                    z = z2;
                    j3 = seekToPeriodPosition;
                    this.playbackInfo = handlePositionDiscontinuity(mediaPeriodId, j3, j2, j3, z, 2);
                    throw th;
                }
                seekToPeriodPosition = seekToPeriodPosition(mediaPeriodId, j4, this.playbackInfo.playbackState == 4);
                z2 = (j != seekToPeriodPosition) | z;
            }
            j5 = j;
            this.playbackInfo = handlePositionDiscontinuity(mediaPeriodId, j5, j2, j5, z, 2);
        } catch (Throwable th2) {
            th = th2;
            j3 = j;
        }
    }

    private long seekToPeriodPosition(MediaSource.MediaPeriodId mediaPeriodId, long j, boolean z) {
        return seekToPeriodPosition(mediaPeriodId, j, this.queue.getPlayingPeriod() != this.queue.getReadingPeriod(), z);
    }

    private long seekToPeriodPosition(MediaSource.MediaPeriodId mediaPeriodId, long j, boolean z, boolean z2) {
        stopRenderers();
        this.isRebuffering = false;
        if (z2 || this.playbackInfo.playbackState == 3) {
            setState(2);
        }
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        MediaPeriodHolder mediaPeriodHolder = playingPeriod;
        while (mediaPeriodHolder != null && !mediaPeriodId.equals(mediaPeriodHolder.info.id)) {
            mediaPeriodHolder = mediaPeriodHolder.getNext();
        }
        if (z || playingPeriod != mediaPeriodHolder || (mediaPeriodHolder != null && mediaPeriodHolder.toRendererTime(j) < 0)) {
            for (Renderer renderer : this.renderers) {
                disableRenderer(renderer);
            }
            if (mediaPeriodHolder != null) {
                while (this.queue.getPlayingPeriod() != mediaPeriodHolder) {
                    this.queue.advancePlayingPeriod();
                }
                this.queue.removeAfter(mediaPeriodHolder);
                mediaPeriodHolder.setRendererOffset(1000000000000L);
                enableRenderers();
            }
        }
        MediaPeriodQueue mediaPeriodQueue = this.queue;
        if (mediaPeriodHolder != null) {
            mediaPeriodQueue.removeAfter(mediaPeriodHolder);
            if (!mediaPeriodHolder.prepared) {
                mediaPeriodHolder.info = mediaPeriodHolder.info.copyWithStartPositionUs(j);
            } else if (mediaPeriodHolder.hasEnabledTracks) {
                j = mediaPeriodHolder.mediaPeriod.seekToUs(j);
                mediaPeriodHolder.mediaPeriod.discardBuffer(j - this.backBufferDurationUs, this.retainBackBufferFromKeyframe);
            }
            resetRendererPosition(j);
            maybeContinueLoading();
        } else {
            mediaPeriodQueue.clear();
            resetRendererPosition(j);
        }
        handleLoadingMediaPeriodChanged(false);
        this.handler.sendEmptyMessage(2);
        return j;
    }

    private void sendMessageInternal(PlayerMessage playerMessage) {
        if (playerMessage.getPositionMs() == -9223372036854775807L) {
            sendMessageToTarget(playerMessage);
            return;
        }
        if (this.playbackInfo.timeline.isEmpty()) {
            this.pendingMessages.add(new PendingMessageInfo(playerMessage));
            return;
        }
        PendingMessageInfo pendingMessageInfo = new PendingMessageInfo(playerMessage);
        Timeline timeline = this.playbackInfo.timeline;
        if (!resolvePendingMessagePosition(pendingMessageInfo, timeline, timeline, this.repeatMode, this.shuffleModeEnabled, this.window, this.period)) {
            playerMessage.markAsProcessed(false);
        } else {
            this.pendingMessages.add(pendingMessageInfo);
            Collections.sort(this.pendingMessages);
        }
    }

    private void sendMessageToTarget(PlayerMessage playerMessage) {
        if (playerMessage.getLooper() != this.playbackLooper) {
            this.handler.obtainMessage(15, playerMessage).sendToTarget();
            return;
        }
        deliverMessage(playerMessage);
        int i = this.playbackInfo.playbackState;
        if (i == 3 || i == 2) {
            this.handler.sendEmptyMessage(2);
        }
    }

    private void sendMessageToTargetThread(final PlayerMessage playerMessage) {
        Looper looper = playerMessage.getLooper();
        if (looper.getThread().isAlive()) {
            this.clock.createHandler(looper, null).post(new Runnable() { // from class: com.google.android.exoplayer2.ExoPlayerImplInternal$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ExoPlayerImplInternal.this.lambda$sendMessageToTargetThread$1(playerMessage);
                }
            });
        } else {
            Log.w("TAG", "Trying to send message on a dead thread.");
            playerMessage.markAsProcessed(false);
        }
    }

    private void setAllRendererStreamsFinal(long j) {
        for (Renderer renderer : this.renderers) {
            if (renderer.getStream() != null) {
                setCurrentStreamFinal(renderer, j);
            }
        }
    }

    private void setCurrentStreamFinal(Renderer renderer, long j) {
        renderer.setCurrentStreamFinal();
        if (renderer instanceof TextRenderer) {
            ((TextRenderer) renderer).setFinalStreamEndPositionUs(j);
        }
    }

    private void setForegroundModeInternal(boolean z, AtomicBoolean atomicBoolean) {
        if (this.foregroundMode != z) {
            this.foregroundMode = z;
            if (!z) {
                for (Renderer renderer : this.renderers) {
                    if (!isRendererEnabled(renderer) && this.renderersToReset.remove(renderer)) {
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

    private void setMediaItemsInternal(MediaSourceListUpdateMessage mediaSourceListUpdateMessage) {
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
        if (mediaSourceListUpdateMessage.windowIndex != -1) {
            this.pendingInitialSeekPosition = new SeekPosition(new PlaylistTimeline(mediaSourceListUpdateMessage.mediaSourceHolders, mediaSourceListUpdateMessage.shuffleOrder), mediaSourceListUpdateMessage.windowIndex, mediaSourceListUpdateMessage.positionUs);
        }
        handleMediaSourceListInfoRefreshed(this.mediaSourceList.setMediaSources(mediaSourceListUpdateMessage.mediaSourceHolders, mediaSourceListUpdateMessage.shuffleOrder), false);
    }

    private void setOffloadSchedulingEnabledInternal(boolean z) {
        if (z == this.offloadSchedulingEnabled) {
            return;
        }
        this.offloadSchedulingEnabled = z;
        if (z || !this.playbackInfo.sleepingForOffload) {
            return;
        }
        this.handler.sendEmptyMessage(2);
    }

    private void setPauseAtEndOfWindowInternal(boolean z) {
        this.pauseAtEndOfWindow = z;
        resetPendingPauseAtEndOfPeriod();
        if (!this.pendingPauseAtEndOfPeriod || this.queue.getReadingPeriod() == this.queue.getPlayingPeriod()) {
            return;
        }
        seekToCurrentPosition(true);
        handleLoadingMediaPeriodChanged(false);
    }

    private void setPlayWhenReadyInternal(boolean z, int i, boolean z2, int i2) {
        this.playbackInfoUpdate.incrementPendingOperationAcks(z2 ? 1 : 0);
        this.playbackInfoUpdate.setPlayWhenReadyChangeReason(i2);
        this.playbackInfo = this.playbackInfo.copyWithPlayWhenReady(z, i);
        this.isRebuffering = false;
        notifyTrackSelectionPlayWhenReadyChanged(z);
        if (!shouldPlayWhenReady()) {
            stopRenderers();
            updatePlaybackPositions();
            return;
        }
        int i3 = this.playbackInfo.playbackState;
        if (i3 == 3) {
            startRenderers();
        } else if (i3 != 2) {
            return;
        }
        this.handler.sendEmptyMessage(2);
    }

    private void setPlaybackParametersInternal(PlaybackParameters playbackParameters) {
        this.mediaClock.setPlaybackParameters(playbackParameters);
        handlePlaybackParameters(this.mediaClock.getPlaybackParameters(), true);
    }

    private void setRepeatModeInternal(int i) {
        this.repeatMode = i;
        if (!this.queue.updateRepeatMode(this.playbackInfo.timeline, i)) {
            seekToCurrentPosition(true);
        }
        handleLoadingMediaPeriodChanged(false);
    }

    private void setSeekParametersInternal(SeekParameters seekParameters) {
        this.seekParameters = seekParameters;
    }

    private void setShuffleModeEnabledInternal(boolean z) {
        this.shuffleModeEnabled = z;
        if (!this.queue.updateShuffleModeEnabled(this.playbackInfo.timeline, z)) {
            seekToCurrentPosition(true);
        }
        handleLoadingMediaPeriodChanged(false);
    }

    private void setShuffleOrderInternal(ShuffleOrder shuffleOrder) {
        this.playbackInfoUpdate.incrementPendingOperationAcks(1);
        handleMediaSourceListInfoRefreshed(this.mediaSourceList.setShuffleOrder(shuffleOrder), false);
    }

    private void setState(int i) {
        PlaybackInfo playbackInfo = this.playbackInfo;
        if (playbackInfo.playbackState != i) {
            if (i != 2) {
                this.playbackMaybeBecameStuckAtMs = -9223372036854775807L;
            }
            this.playbackInfo = playbackInfo.copyWithPlaybackState(i);
        }
    }

    private boolean shouldAdvancePlayingPeriod() {
        MediaPeriodHolder playingPeriod;
        MediaPeriodHolder next;
        return shouldPlayWhenReady() && !this.pendingPauseAtEndOfPeriod && (playingPeriod = this.queue.getPlayingPeriod()) != null && (next = playingPeriod.getNext()) != null && this.rendererPositionUs >= next.getStartPositionRendererTime() && next.allRenderersInCorrectState;
    }

    private boolean shouldContinueLoading() {
        if (!isLoadingPossible()) {
            return false;
        }
        MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        long totalBufferedDurationUs = getTotalBufferedDurationUs(loadingPeriod.getNextLoadPositionUs());
        long periodTime = loadingPeriod == this.queue.getPlayingPeriod() ? loadingPeriod.toPeriodTime(this.rendererPositionUs) : loadingPeriod.toPeriodTime(this.rendererPositionUs) - loadingPeriod.info.startPositionUs;
        boolean shouldContinueLoading = this.loadControl.shouldContinueLoading(periodTime, totalBufferedDurationUs, this.mediaClock.getPlaybackParameters().speed);
        if (shouldContinueLoading || totalBufferedDurationUs >= 500000) {
            return shouldContinueLoading;
        }
        if (this.backBufferDurationUs <= 0 && !this.retainBackBufferFromKeyframe) {
            return shouldContinueLoading;
        }
        this.queue.getPlayingPeriod().mediaPeriod.discardBuffer(this.playbackInfo.positionUs, false);
        return this.loadControl.shouldContinueLoading(periodTime, totalBufferedDurationUs, this.mediaClock.getPlaybackParameters().speed);
    }

    private boolean shouldPlayWhenReady() {
        PlaybackInfo playbackInfo = this.playbackInfo;
        return playbackInfo.playWhenReady && playbackInfo.playbackSuppressionReason == 0;
    }

    private boolean shouldTransitionToReadyState(boolean z) {
        if (this.enabledRendererCount == 0) {
            return isTimelineReady();
        }
        if (!z) {
            return false;
        }
        PlaybackInfo playbackInfo = this.playbackInfo;
        if (!playbackInfo.isLoading) {
            return true;
        }
        long targetLiveOffsetUs = shouldUseLivePlaybackSpeedControl(playbackInfo.timeline, this.queue.getPlayingPeriod().info.id) ? this.livePlaybackSpeedControl.getTargetLiveOffsetUs() : -9223372036854775807L;
        MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        return (loadingPeriod.isFullyBuffered() && loadingPeriod.info.isFinal) || (loadingPeriod.info.id.isAd() && !loadingPeriod.prepared) || this.loadControl.shouldStartPlayback(getTotalBufferedDurationUs(), this.mediaClock.getPlaybackParameters().speed, this.isRebuffering, targetLiveOffsetUs);
    }

    private boolean shouldUseLivePlaybackSpeedControl(Timeline timeline, MediaSource.MediaPeriodId mediaPeriodId) {
        if (mediaPeriodId.isAd() || timeline.isEmpty()) {
            return false;
        }
        timeline.getWindow(timeline.getPeriodByUid(mediaPeriodId.periodUid, this.period).windowIndex, this.window);
        if (!this.window.isLive()) {
            return false;
        }
        Timeline.Window window = this.window;
        return window.isDynamic && window.windowStartTimeMs != -9223372036854775807L;
    }

    private void startRenderers() {
        this.isRebuffering = false;
        this.mediaClock.start();
        for (Renderer renderer : this.renderers) {
            if (isRendererEnabled(renderer)) {
                renderer.start();
            }
        }
    }

    private void stopInternal(boolean z, boolean z2) {
        resetInternal(z || !this.foregroundMode, false, true, false);
        this.playbackInfoUpdate.incrementPendingOperationAcks(z2 ? 1 : 0);
        this.loadControl.onStopped();
        setState(1);
    }

    private void stopRenderers() {
        this.mediaClock.stop();
        for (Renderer renderer : this.renderers) {
            if (isRendererEnabled(renderer)) {
                ensureStopped(renderer);
            }
        }
    }

    private void updateIsLoading() {
        MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        boolean z = this.shouldContinueLoading || (loadingPeriod != null && loadingPeriod.mediaPeriod.isLoading());
        PlaybackInfo playbackInfo = this.playbackInfo;
        if (z != playbackInfo.isLoading) {
            this.playbackInfo = playbackInfo.copyWithIsLoading(z);
        }
    }

    private void updateLoadControlTrackSelection(TrackGroupArray trackGroupArray, TrackSelectorResult trackSelectorResult) {
        this.loadControl.onTracksSelected(this.renderers, trackGroupArray, trackSelectorResult.selections);
    }

    private void updatePeriods() {
        if (this.playbackInfo.timeline.isEmpty() || !this.mediaSourceList.isPrepared()) {
            return;
        }
        maybeUpdateLoadingPeriod();
        maybeUpdateReadingPeriod();
        maybeUpdateReadingRenderers();
        maybeUpdatePlayingPeriod();
    }

    private void updatePlaybackPositions() {
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        if (playingPeriod == null) {
            return;
        }
        long readDiscontinuity = playingPeriod.prepared ? playingPeriod.mediaPeriod.readDiscontinuity() : -9223372036854775807L;
        if (readDiscontinuity != -9223372036854775807L) {
            resetRendererPosition(readDiscontinuity);
            if (readDiscontinuity != this.playbackInfo.positionUs) {
                PlaybackInfo playbackInfo = this.playbackInfo;
                this.playbackInfo = handlePositionDiscontinuity(playbackInfo.periodId, readDiscontinuity, playbackInfo.requestedContentPositionUs, readDiscontinuity, true, 5);
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
        PlaybackInfo playbackInfo2 = this.playbackInfo;
        if (playbackInfo2.playWhenReady && playbackInfo2.playbackState == 3 && shouldUseLivePlaybackSpeedControl(playbackInfo2.timeline, playbackInfo2.periodId) && this.playbackInfo.playbackParameters.speed == 1.0f) {
            float adjustedPlaybackSpeed = this.livePlaybackSpeedControl.getAdjustedPlaybackSpeed(getCurrentLiveOffsetUs(), getTotalBufferedDurationUs());
            if (this.mediaClock.getPlaybackParameters().speed != adjustedPlaybackSpeed) {
                this.mediaClock.setPlaybackParameters(this.playbackInfo.playbackParameters.withSpeed(adjustedPlaybackSpeed));
                handlePlaybackParameters(this.playbackInfo.playbackParameters, this.mediaClock.getPlaybackParameters().speed, false, false);
            }
        }
    }

    private void updatePlaybackSpeedSettingsForNewPeriod(Timeline timeline, MediaSource.MediaPeriodId mediaPeriodId, Timeline timeline2, MediaSource.MediaPeriodId mediaPeriodId2, long j) {
        if (!shouldUseLivePlaybackSpeedControl(timeline, mediaPeriodId)) {
            PlaybackParameters playbackParameters = mediaPeriodId.isAd() ? PlaybackParameters.DEFAULT : this.playbackInfo.playbackParameters;
            if (this.mediaClock.getPlaybackParameters().equals(playbackParameters)) {
                return;
            }
            this.mediaClock.setPlaybackParameters(playbackParameters);
            return;
        }
        timeline.getWindow(timeline.getPeriodByUid(mediaPeriodId.periodUid, this.period).windowIndex, this.window);
        this.livePlaybackSpeedControl.setLiveConfiguration((MediaItem.LiveConfiguration) Util.castNonNull(this.window.liveConfiguration));
        if (j != -9223372036854775807L) {
            this.livePlaybackSpeedControl.setTargetLiveOffsetOverrideUs(getLiveOffsetUs(timeline, mediaPeriodId.periodUid, j));
            return;
        }
        if (Util.areEqual(!timeline2.isEmpty() ? timeline2.getWindow(timeline2.getPeriodByUid(mediaPeriodId2.periodUid, this.period).windowIndex, this.window).uid : null, this.window.uid)) {
            return;
        }
        this.livePlaybackSpeedControl.setTargetLiveOffsetOverrideUs(-9223372036854775807L);
    }

    private void updateTrackSelectionPlaybackSpeed(float f) {
        for (MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod(); playingPeriod != null; playingPeriod = playingPeriod.getNext()) {
            for (ExoTrackSelection exoTrackSelection : playingPeriod.getTrackSelectorResult().selections) {
                if (exoTrackSelection != null) {
                    exoTrackSelection.onPlaybackSpeed(f);
                }
            }
        }
    }

    private synchronized void waitUninterruptibly(Supplier supplier, long j) {
        long elapsedRealtime = this.clock.elapsedRealtime() + j;
        boolean z = false;
        while (!((Boolean) supplier.get()).booleanValue() && j > 0) {
            try {
                this.clock.onThreadBlocked();
                wait(j);
            } catch (InterruptedException unused) {
                z = true;
            }
            j = elapsedRealtime - this.clock.elapsedRealtime();
        }
        if (z) {
            Thread.currentThread().interrupt();
        }
    }

    public void experimentalSetForegroundModeTimeoutMs(long j) {
        this.setForegroundModeTimeoutMs = j;
    }

    public Looper getPlaybackLooper() {
        return this.playbackLooper;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        ExoPlaybackException e;
        int i;
        IOException iOException;
        MediaPeriodHolder readingPeriod;
        int i2 = MediaDataController.MAX_STYLE_RUNS_COUNT;
        try {
            switch (message.what) {
                case 0:
                    prepareInternal();
                    break;
                case 1:
                    setPlayWhenReadyInternal(message.arg1 != 0, message.arg2, true, 1);
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
                    stopInternal(false, true);
                    break;
                case 7:
                    releaseInternal();
                    return true;
                case 8:
                    handlePeriodPrepared((MediaPeriod) message.obj);
                    break;
                case 9:
                    handleContinueLoadingRequested((MediaPeriod) message.obj);
                    break;
                case 10:
                    reselectTracksInternal();
                    break;
                case 11:
                    setRepeatModeInternal(message.arg1);
                    break;
                case 12:
                    setShuffleModeEnabledInternal(message.arg1 != 0);
                    break;
                case 13:
                    setForegroundModeInternal(message.arg1 != 0, (AtomicBoolean) message.obj);
                    break;
                case 14:
                    sendMessageInternal((PlayerMessage) message.obj);
                    break;
                case 15:
                    sendMessageToTargetThread((PlayerMessage) message.obj);
                    break;
                case 16:
                    handlePlaybackParameters((PlaybackParameters) message.obj, false);
                    break;
                case 17:
                    setMediaItemsInternal((MediaSourceListUpdateMessage) message.obj);
                    break;
                case 18:
                    addMediaItemsInternal((MediaSourceListUpdateMessage) message.obj, message.arg1);
                    break;
                case 19:
                    RecyclerView$ItemAnimator$$ExternalSyntheticThrowCCEIfNotNull0.m(message.obj);
                    moveMediaItemsInternal(null);
                    break;
                case 20:
                    removeMediaItemsInternal(message.arg1, message.arg2, (ShuffleOrder) message.obj);
                    break;
                case 21:
                    setShuffleOrderInternal((ShuffleOrder) message.obj);
                    break;
                case 22:
                    mediaSourceListUpdateRequestedInternal();
                    break;
                case 23:
                    setPauseAtEndOfWindowInternal(message.arg1 != 0);
                    break;
                case 24:
                    setOffloadSchedulingEnabledInternal(message.arg1 == 1);
                    break;
                case 25:
                    attemptRendererErrorRecovery();
                    break;
                default:
                    return false;
            }
        } catch (ExoPlaybackException e2) {
            e = e2;
            if (e.type == 1 && (readingPeriod = this.queue.getReadingPeriod()) != null) {
                e = e.copyWithMediaPeriodId(readingPeriod.info.id);
            }
            if (e.isRecoverable && this.pendingRecoverableRendererError == null) {
                Log.w("ExoPlayerImplInternal", "Recoverable renderer error", e);
                this.pendingRecoverableRendererError = e;
                HandlerWrapper handlerWrapper = this.handler;
                handlerWrapper.sendMessageAtFrontOfQueue(handlerWrapper.obtainMessage(25, e));
            } else {
                ExoPlaybackException exoPlaybackException = this.pendingRecoverableRendererError;
                if (exoPlaybackException != null) {
                    exoPlaybackException.addSuppressed(e);
                    e = this.pendingRecoverableRendererError;
                }
                Log.e("ExoPlayerImplInternal", "Playback error", e);
                stopInternal(true, false);
                this.playbackInfo = this.playbackInfo.copyWithPlaybackError(e);
            }
        } catch (ParserException e3) {
            int i3 = e3.dataType;
            if (i3 == 1) {
                i2 = e3.contentIsMalformed ? 3001 : 3003;
            } else if (i3 == 4) {
                i2 = e3.contentIsMalformed ? 3002 : 3004;
            }
            handleIoException(e3, i2);
        } catch (DrmSession.DrmSessionException e4) {
            i = e4.errorCode;
            iOException = e4;
            handleIoException(iOException, i);
        } catch (BehindLiveWindowException e5) {
            i = 1002;
            iOException = e5;
            handleIoException(iOException, i);
        } catch (DataSourceException e6) {
            i = e6.reason;
            iOException = e6;
            handleIoException(iOException, i);
        } catch (IOException e7) {
            i = 2000;
            iOException = e7;
            handleIoException(iOException, i);
        } catch (RuntimeException e8) {
            if ((e8 instanceof IllegalStateException) || (e8 instanceof IllegalArgumentException)) {
                i2 = 1004;
            }
            e = ExoPlaybackException.createForUnexpected(e8, i2);
            Log.e("ExoPlayerImplInternal", "Playback error", e);
            stopInternal(true, false);
            this.playbackInfo = this.playbackInfo.copyWithPlaybackError(e);
        }
        maybeNotifyPlaybackInfoChanged();
        return true;
    }

    @Override // com.google.android.exoplayer2.source.SequenceableLoader.Callback
    public void onContinueLoadingRequested(MediaPeriod mediaPeriod) {
        this.handler.obtainMessage(9, mediaPeriod).sendToTarget();
    }

    @Override // com.google.android.exoplayer2.DefaultMediaClock.PlaybackParametersListener
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        this.handler.obtainMessage(16, playbackParameters).sendToTarget();
    }

    @Override // com.google.android.exoplayer2.MediaSourceList.MediaSourceListInfoRefreshListener
    public void onPlaylistUpdateRequested() {
        this.handler.sendEmptyMessage(22);
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod.Callback
    public void onPrepared(MediaPeriod mediaPeriod) {
        this.handler.obtainMessage(8, mediaPeriod).sendToTarget();
    }

    @Override // com.google.android.exoplayer2.trackselection.TrackSelector.InvalidationListener
    public void onTrackSelectionsInvalidated() {
        this.handler.sendEmptyMessage(10);
    }

    public void prepare() {
        this.handler.obtainMessage(0).sendToTarget();
    }

    public synchronized boolean release() {
        if (!this.released && this.playbackLooper.getThread().isAlive()) {
            this.handler.sendEmptyMessage(7);
            waitUninterruptibly(new Supplier() { // from class: com.google.android.exoplayer2.ExoPlayerImplInternal$$ExternalSyntheticLambda0
                @Override // com.google.common.base.Supplier
                public final Object get() {
                    Boolean lambda$release$0;
                    lambda$release$0 = ExoPlayerImplInternal.this.lambda$release$0();
                    return lambda$release$0;
                }
            }, this.releaseTimeoutMs);
            return this.released;
        }
        return true;
    }

    public void removeMediaSources(int i, int i2, ShuffleOrder shuffleOrder) {
        this.handler.obtainMessage(20, i, i2, shuffleOrder).sendToTarget();
    }

    public void seekTo(Timeline timeline, int i, long j) {
        this.handler.obtainMessage(3, new SeekPosition(timeline, i, j)).sendToTarget();
    }

    @Override // com.google.android.exoplayer2.PlayerMessage.Sender
    public synchronized void sendMessage(PlayerMessage playerMessage) {
        if (!this.released && this.playbackLooper.getThread().isAlive()) {
            this.handler.obtainMessage(14, playerMessage).sendToTarget();
            return;
        }
        Log.w("ExoPlayerImplInternal", "Ignoring messages sent after release.");
        playerMessage.markAsProcessed(false);
    }

    public void setMediaSources(List list, int i, long j, ShuffleOrder shuffleOrder) {
        this.handler.obtainMessage(17, new MediaSourceListUpdateMessage(list, shuffleOrder, i, j)).sendToTarget();
    }

    public void setPlayWhenReady(boolean z, int i) {
        this.handler.obtainMessage(1, z ? 1 : 0, i).sendToTarget();
    }

    public void setPlaybackParameters(PlaybackParameters playbackParameters) {
        this.handler.obtainMessage(4, playbackParameters).sendToTarget();
    }

    public void setRepeatMode(int i) {
        this.handler.obtainMessage(11, i, 0).sendToTarget();
    }

    public void setSeekParameters(SeekParameters seekParameters) {
        this.handler.obtainMessage(5, seekParameters).sendToTarget();
    }

    public void stop() {
        this.handler.obtainMessage(6).sendToTarget();
    }
}
