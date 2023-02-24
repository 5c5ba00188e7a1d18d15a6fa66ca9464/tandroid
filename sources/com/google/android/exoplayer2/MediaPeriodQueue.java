package com.google.android.exoplayer2;

import android.util.Pair;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.analytics.AnalyticsCollector;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.HandlerWrapper;
import com.google.common.collect.ImmutableList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class MediaPeriodQueue {
    private final AnalyticsCollector analyticsCollector;
    private final HandlerWrapper analyticsCollectorHandler;
    private int length;
    private MediaPeriodHolder loading;
    private long nextWindowSequenceNumber;
    private Object oldFrontPeriodUid;
    private long oldFrontPeriodWindowSequenceNumber;
    private MediaPeriodHolder playing;
    private MediaPeriodHolder reading;
    private int repeatMode;
    private boolean shuffleModeEnabled;
    private final Timeline.Period period = new Timeline.Period();
    private final Timeline.Window window = new Timeline.Window();

    private boolean areDurationsCompatible(long j, long j2) {
        return j == -9223372036854775807L || j == j2;
    }

    public MediaPeriodQueue(AnalyticsCollector analyticsCollector, HandlerWrapper handlerWrapper) {
        this.analyticsCollector = analyticsCollector;
        this.analyticsCollectorHandler = handlerWrapper;
    }

    public boolean updateRepeatMode(Timeline timeline, int i) {
        this.repeatMode = i;
        return updateForPlaybackModeChange(timeline);
    }

    public boolean updateShuffleModeEnabled(Timeline timeline, boolean z) {
        this.shuffleModeEnabled = z;
        return updateForPlaybackModeChange(timeline);
    }

    public boolean isLoading(MediaPeriod mediaPeriod) {
        MediaPeriodHolder mediaPeriodHolder = this.loading;
        return mediaPeriodHolder != null && mediaPeriodHolder.mediaPeriod == mediaPeriod;
    }

    public void reevaluateBuffer(long j) {
        MediaPeriodHolder mediaPeriodHolder = this.loading;
        if (mediaPeriodHolder != null) {
            mediaPeriodHolder.reevaluateBuffer(j);
        }
    }

    public boolean shouldLoadNextMediaPeriod() {
        MediaPeriodHolder mediaPeriodHolder = this.loading;
        return mediaPeriodHolder == null || (!mediaPeriodHolder.info.isFinal && mediaPeriodHolder.isFullyBuffered() && this.loading.info.durationUs != -9223372036854775807L && this.length < 100);
    }

    public MediaPeriodInfo getNextMediaPeriodInfo(long j, PlaybackInfo playbackInfo) {
        MediaPeriodHolder mediaPeriodHolder = this.loading;
        if (mediaPeriodHolder == null) {
            return getFirstMediaPeriodInfo(playbackInfo);
        }
        return getFollowingMediaPeriodInfo(playbackInfo.timeline, mediaPeriodHolder, j);
    }

    public MediaPeriodHolder enqueueNextMediaPeriodHolder(RendererCapabilities[] rendererCapabilitiesArr, TrackSelector trackSelector, Allocator allocator, MediaSourceList mediaSourceList, MediaPeriodInfo mediaPeriodInfo, TrackSelectorResult trackSelectorResult) {
        MediaPeriodHolder mediaPeriodHolder = this.loading;
        MediaPeriodHolder mediaPeriodHolder2 = new MediaPeriodHolder(rendererCapabilitiesArr, mediaPeriodHolder == null ? 1000000000000L : (mediaPeriodHolder.getRendererOffset() + this.loading.info.durationUs) - mediaPeriodInfo.startPositionUs, trackSelector, allocator, mediaSourceList, mediaPeriodInfo, trackSelectorResult);
        MediaPeriodHolder mediaPeriodHolder3 = this.loading;
        if (mediaPeriodHolder3 != null) {
            mediaPeriodHolder3.setNext(mediaPeriodHolder2);
        } else {
            this.playing = mediaPeriodHolder2;
            this.reading = mediaPeriodHolder2;
        }
        this.oldFrontPeriodUid = null;
        this.loading = mediaPeriodHolder2;
        this.length++;
        notifyQueueUpdate();
        return mediaPeriodHolder2;
    }

    public MediaPeriodHolder getLoadingPeriod() {
        return this.loading;
    }

    public MediaPeriodHolder getPlayingPeriod() {
        return this.playing;
    }

    public MediaPeriodHolder getReadingPeriod() {
        return this.reading;
    }

    public MediaPeriodHolder advanceReadingPeriod() {
        MediaPeriodHolder mediaPeriodHolder = this.reading;
        Assertions.checkState((mediaPeriodHolder == null || mediaPeriodHolder.getNext() == null) ? false : true);
        this.reading = this.reading.getNext();
        notifyQueueUpdate();
        return this.reading;
    }

    public MediaPeriodHolder advancePlayingPeriod() {
        MediaPeriodHolder mediaPeriodHolder = this.playing;
        if (mediaPeriodHolder == null) {
            return null;
        }
        if (mediaPeriodHolder == this.reading) {
            this.reading = mediaPeriodHolder.getNext();
        }
        this.playing.release();
        int i = this.length - 1;
        this.length = i;
        if (i == 0) {
            this.loading = null;
            MediaPeriodHolder mediaPeriodHolder2 = this.playing;
            this.oldFrontPeriodUid = mediaPeriodHolder2.uid;
            this.oldFrontPeriodWindowSequenceNumber = mediaPeriodHolder2.info.id.windowSequenceNumber;
        }
        this.playing = this.playing.getNext();
        notifyQueueUpdate();
        return this.playing;
    }

    public boolean removeAfter(MediaPeriodHolder mediaPeriodHolder) {
        boolean z = false;
        Assertions.checkState(mediaPeriodHolder != null);
        if (mediaPeriodHolder.equals(this.loading)) {
            return false;
        }
        this.loading = mediaPeriodHolder;
        while (mediaPeriodHolder.getNext() != null) {
            mediaPeriodHolder = mediaPeriodHolder.getNext();
            if (mediaPeriodHolder == this.reading) {
                this.reading = this.playing;
                z = true;
            }
            mediaPeriodHolder.release();
            this.length--;
        }
        this.loading.setNext(null);
        notifyQueueUpdate();
        return z;
    }

    public void clear() {
        if (this.length == 0) {
            return;
        }
        MediaPeriodHolder mediaPeriodHolder = (MediaPeriodHolder) Assertions.checkStateNotNull(this.playing);
        this.oldFrontPeriodUid = mediaPeriodHolder.uid;
        this.oldFrontPeriodWindowSequenceNumber = mediaPeriodHolder.info.id.windowSequenceNumber;
        while (mediaPeriodHolder != null) {
            mediaPeriodHolder.release();
            mediaPeriodHolder = mediaPeriodHolder.getNext();
        }
        this.playing = null;
        this.loading = null;
        this.reading = null;
        this.length = 0;
        notifyQueueUpdate();
    }

    public boolean updateQueuedPeriods(Timeline timeline, long j, long j2) {
        MediaPeriodInfo mediaPeriodInfo;
        MediaPeriodHolder mediaPeriodHolder = this.playing;
        MediaPeriodHolder mediaPeriodHolder2 = null;
        while (mediaPeriodHolder != null) {
            MediaPeriodInfo mediaPeriodInfo2 = mediaPeriodHolder.info;
            if (mediaPeriodHolder2 == null) {
                mediaPeriodInfo = getUpdatedMediaPeriodInfo(timeline, mediaPeriodInfo2);
            } else {
                MediaPeriodInfo followingMediaPeriodInfo = getFollowingMediaPeriodInfo(timeline, mediaPeriodHolder2, j);
                if (followingMediaPeriodInfo == null) {
                    return !removeAfter(mediaPeriodHolder2);
                }
                if (!canKeepMediaPeriodHolder(mediaPeriodInfo2, followingMediaPeriodInfo)) {
                    return !removeAfter(mediaPeriodHolder2);
                }
                mediaPeriodInfo = followingMediaPeriodInfo;
            }
            mediaPeriodHolder.info = mediaPeriodInfo.copyWithRequestedContentPositionUs(mediaPeriodInfo2.requestedContentPositionUs);
            if (!areDurationsCompatible(mediaPeriodInfo2.durationUs, mediaPeriodInfo.durationUs)) {
                mediaPeriodHolder.updateClipping();
                long j3 = mediaPeriodInfo.durationUs;
                return (removeAfter(mediaPeriodHolder) || (mediaPeriodHolder == this.reading && !mediaPeriodHolder.info.isFollowedByTransitionToSameStream && ((j2 > Long.MIN_VALUE ? 1 : (j2 == Long.MIN_VALUE ? 0 : -1)) == 0 || (j2 > ((j3 > (-9223372036854775807L) ? 1 : (j3 == (-9223372036854775807L) ? 0 : -1)) == 0 ? Long.MAX_VALUE : mediaPeriodHolder.toRendererTime(j3)) ? 1 : (j2 == ((j3 > (-9223372036854775807L) ? 1 : (j3 == (-9223372036854775807L) ? 0 : -1)) == 0 ? Long.MAX_VALUE : mediaPeriodHolder.toRendererTime(j3)) ? 0 : -1)) >= 0))) ? false : true;
            }
            mediaPeriodHolder2 = mediaPeriodHolder;
            mediaPeriodHolder = mediaPeriodHolder.getNext();
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x006c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MediaPeriodInfo getUpdatedMediaPeriodInfo(Timeline timeline, MediaPeriodInfo mediaPeriodInfo) {
        long durationUs;
        long j;
        boolean z;
        int i;
        MediaSource.MediaPeriodId mediaPeriodId = mediaPeriodInfo.id;
        boolean isLastInPeriod = isLastInPeriod(mediaPeriodId);
        boolean isLastInWindow = isLastInWindow(timeline, mediaPeriodId);
        boolean isLastInTimeline = isLastInTimeline(timeline, mediaPeriodId, isLastInPeriod);
        timeline.getPeriodByUid(mediaPeriodInfo.id.periodUid, this.period);
        long adGroupTimeUs = (mediaPeriodId.isAd() || (i = mediaPeriodId.nextAdGroupIndex) == -1) ? -9223372036854775807L : this.period.getAdGroupTimeUs(i);
        if (mediaPeriodId.isAd()) {
            durationUs = this.period.getAdDurationUs(mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup);
        } else if (adGroupTimeUs == -9223372036854775807L || adGroupTimeUs == Long.MIN_VALUE) {
            durationUs = this.period.getDurationUs();
        } else {
            j = adGroupTimeUs;
            if (!mediaPeriodId.isAd()) {
                z = this.period.isServerSideInsertedAdGroup(mediaPeriodId.adGroupIndex);
            } else {
                int i2 = mediaPeriodId.nextAdGroupIndex;
                z = i2 != -1 && this.period.isServerSideInsertedAdGroup(i2);
            }
            return new MediaPeriodInfo(mediaPeriodId, mediaPeriodInfo.startPositionUs, mediaPeriodInfo.requestedContentPositionUs, adGroupTimeUs, j, z, isLastInPeriod, isLastInWindow, isLastInTimeline);
        }
        j = durationUs;
        if (!mediaPeriodId.isAd()) {
        }
        return new MediaPeriodInfo(mediaPeriodId, mediaPeriodInfo.startPositionUs, mediaPeriodInfo.requestedContentPositionUs, adGroupTimeUs, j, z, isLastInPeriod, isLastInWindow, isLastInTimeline);
    }

    private static MediaSource.MediaPeriodId resolveMediaPeriodIdForAds(Timeline timeline, Object obj, long j, long j2, Timeline.Window window, Timeline.Period period) {
        timeline.getPeriodByUid(obj, period);
        timeline.getWindow(period.windowIndex, window);
        int indexOfPeriod = timeline.getIndexOfPeriod(obj);
        Object obj2 = obj;
        while (period.durationUs == 0 && period.getAdGroupCount() > 0 && period.isServerSideInsertedAdGroup(period.getRemovedAdGroupCount()) && period.getAdGroupIndexForPositionUs(0L) == -1) {
            int i = indexOfPeriod + 1;
            if (indexOfPeriod >= window.lastPeriodIndex) {
                break;
            }
            timeline.getPeriod(i, period, true);
            obj2 = Assertions.checkNotNull(period.uid);
            indexOfPeriod = i;
        }
        timeline.getPeriodByUid(obj2, period);
        int adGroupIndexForPositionUs = period.getAdGroupIndexForPositionUs(j);
        if (adGroupIndexForPositionUs == -1) {
            return new MediaSource.MediaPeriodId(obj2, j2, period.getAdGroupIndexAfterPositionUs(j));
        }
        return new MediaSource.MediaPeriodId(obj2, adGroupIndexForPositionUs, period.getFirstAdIndexToPlay(adGroupIndexForPositionUs), j2);
    }

    public MediaSource.MediaPeriodId resolveMediaPeriodIdForAdsAfterPeriodPositionChange(Timeline timeline, Object obj, long j) {
        long resolvePeriodIndexToWindowSequenceNumber = resolvePeriodIndexToWindowSequenceNumber(timeline, obj);
        timeline.getPeriodByUid(obj, this.period);
        timeline.getWindow(this.period.windowIndex, this.window);
        boolean z = false;
        for (int indexOfPeriod = timeline.getIndexOfPeriod(obj); indexOfPeriod >= this.window.firstPeriodIndex; indexOfPeriod--) {
            timeline.getPeriod(indexOfPeriod, this.period, true);
            boolean z2 = this.period.getAdGroupCount() > 0;
            z |= z2;
            Timeline.Period period = this.period;
            if (period.getAdGroupIndexForPositionUs(period.durationUs) != -1) {
                obj = Assertions.checkNotNull(this.period.uid);
            }
            if (z && (!z2 || this.period.durationUs != 0)) {
                break;
            }
        }
        return resolveMediaPeriodIdForAds(timeline, obj, j, resolvePeriodIndexToWindowSequenceNumber, this.window, this.period);
    }

    private void notifyQueueUpdate() {
        final ImmutableList.Builder builder = ImmutableList.builder();
        for (MediaPeriodHolder mediaPeriodHolder = this.playing; mediaPeriodHolder != null; mediaPeriodHolder = mediaPeriodHolder.getNext()) {
            builder.add((ImmutableList.Builder) mediaPeriodHolder.info.id);
        }
        MediaPeriodHolder mediaPeriodHolder2 = this.reading;
        final MediaSource.MediaPeriodId mediaPeriodId = mediaPeriodHolder2 == null ? null : mediaPeriodHolder2.info.id;
        this.analyticsCollectorHandler.post(new Runnable() { // from class: com.google.android.exoplayer2.MediaPeriodQueue$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MediaPeriodQueue.this.lambda$notifyQueueUpdate$0(builder, mediaPeriodId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyQueueUpdate$0(ImmutableList.Builder builder, MediaSource.MediaPeriodId mediaPeriodId) {
        this.analyticsCollector.updateMediaPeriodQueueInfo(builder.build(), mediaPeriodId);
    }

    private long resolvePeriodIndexToWindowSequenceNumber(Timeline timeline, Object obj) {
        int indexOfPeriod;
        int i = timeline.getPeriodByUid(obj, this.period).windowIndex;
        Object obj2 = this.oldFrontPeriodUid;
        if (obj2 != null && (indexOfPeriod = timeline.getIndexOfPeriod(obj2)) != -1 && timeline.getPeriod(indexOfPeriod, this.period).windowIndex == i) {
            return this.oldFrontPeriodWindowSequenceNumber;
        }
        for (MediaPeriodHolder mediaPeriodHolder = this.playing; mediaPeriodHolder != null; mediaPeriodHolder = mediaPeriodHolder.getNext()) {
            if (mediaPeriodHolder.uid.equals(obj)) {
                return mediaPeriodHolder.info.id.windowSequenceNumber;
            }
        }
        for (MediaPeriodHolder mediaPeriodHolder2 = this.playing; mediaPeriodHolder2 != null; mediaPeriodHolder2 = mediaPeriodHolder2.getNext()) {
            int indexOfPeriod2 = timeline.getIndexOfPeriod(mediaPeriodHolder2.uid);
            if (indexOfPeriod2 != -1 && timeline.getPeriod(indexOfPeriod2, this.period).windowIndex == i) {
                return mediaPeriodHolder2.info.id.windowSequenceNumber;
            }
        }
        long j = this.nextWindowSequenceNumber;
        this.nextWindowSequenceNumber = 1 + j;
        if (this.playing == null) {
            this.oldFrontPeriodUid = obj;
            this.oldFrontPeriodWindowSequenceNumber = j;
        }
        return j;
    }

    private boolean canKeepMediaPeriodHolder(MediaPeriodInfo mediaPeriodInfo, MediaPeriodInfo mediaPeriodInfo2) {
        return mediaPeriodInfo.startPositionUs == mediaPeriodInfo2.startPositionUs && mediaPeriodInfo.id.equals(mediaPeriodInfo2.id);
    }

    private boolean updateForPlaybackModeChange(Timeline timeline) {
        MediaPeriodHolder mediaPeriodHolder = this.playing;
        if (mediaPeriodHolder == null) {
            return true;
        }
        int indexOfPeriod = timeline.getIndexOfPeriod(mediaPeriodHolder.uid);
        while (true) {
            indexOfPeriod = timeline.getNextPeriodIndex(indexOfPeriod, this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
            while (mediaPeriodHolder.getNext() != null && !mediaPeriodHolder.info.isLastInTimelinePeriod) {
                mediaPeriodHolder = mediaPeriodHolder.getNext();
            }
            MediaPeriodHolder next = mediaPeriodHolder.getNext();
            if (indexOfPeriod == -1 || next == null || timeline.getIndexOfPeriod(next.uid) != indexOfPeriod) {
                break;
            }
            mediaPeriodHolder = next;
        }
        boolean removeAfter = removeAfter(mediaPeriodHolder);
        mediaPeriodHolder.info = getUpdatedMediaPeriodInfo(timeline, mediaPeriodHolder.info);
        return !removeAfter;
    }

    private MediaPeriodInfo getFirstMediaPeriodInfo(PlaybackInfo playbackInfo) {
        return getMediaPeriodInfo(playbackInfo.timeline, playbackInfo.periodId, playbackInfo.requestedContentPositionUs, playbackInfo.positionUs);
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x00cc, code lost:
        if (r0.isServerSideInsertedAdGroup(r0.getRemovedAdGroupCount()) != false) goto L26;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private MediaPeriodInfo getFollowingMediaPeriodInfo(Timeline timeline, MediaPeriodHolder mediaPeriodHolder, long j) {
        long j2;
        long j3;
        long j4;
        long j5;
        long j6;
        MediaPeriodInfo mediaPeriodInfo = mediaPeriodHolder.info;
        long rendererOffset = (mediaPeriodHolder.getRendererOffset() + mediaPeriodInfo.durationUs) - j;
        boolean z = true;
        if (mediaPeriodInfo.isLastInTimelinePeriod) {
            int nextPeriodIndex = timeline.getNextPeriodIndex(timeline.getIndexOfPeriod(mediaPeriodInfo.id.periodUid), this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
            if (nextPeriodIndex == -1) {
                return null;
            }
            int i = timeline.getPeriod(nextPeriodIndex, this.period, true).windowIndex;
            Object checkNotNull = Assertions.checkNotNull(this.period.uid);
            long j7 = mediaPeriodInfo.id.windowSequenceNumber;
            if (timeline.getWindow(i, this.window).firstPeriodIndex == nextPeriodIndex) {
                Pair<Object, Long> periodPositionUs = timeline.getPeriodPositionUs(this.window, this.period, i, -9223372036854775807L, Math.max(0L, rendererOffset));
                if (periodPositionUs == null) {
                    return null;
                }
                checkNotNull = periodPositionUs.first;
                long longValue = ((Long) periodPositionUs.second).longValue();
                MediaPeriodHolder next = mediaPeriodHolder.getNext();
                if (next != null && next.uid.equals(checkNotNull)) {
                    j6 = next.info.id.windowSequenceNumber;
                } else {
                    j6 = this.nextWindowSequenceNumber;
                    this.nextWindowSequenceNumber = 1 + j6;
                }
                j2 = longValue;
                j3 = -9223372036854775807L;
                j7 = j6;
            } else {
                j2 = 0;
                j3 = 0;
            }
            MediaSource.MediaPeriodId resolveMediaPeriodIdForAds = resolveMediaPeriodIdForAds(timeline, checkNotNull, j2, j7, this.window, this.period);
            if (j3 != -9223372036854775807L && mediaPeriodInfo.requestedContentPositionUs != -9223372036854775807L) {
                if (timeline.getPeriodByUid(mediaPeriodInfo.id.periodUid, this.period).getAdGroupCount() > 0) {
                    Timeline.Period period = this.period;
                }
                z = false;
                if (resolveMediaPeriodIdForAds.isAd() && z) {
                    j5 = mediaPeriodInfo.requestedContentPositionUs;
                    j4 = j2;
                    return getMediaPeriodInfo(timeline, resolveMediaPeriodIdForAds, j5, j4);
                } else if (z) {
                    j4 = mediaPeriodInfo.requestedContentPositionUs;
                    j5 = j3;
                    return getMediaPeriodInfo(timeline, resolveMediaPeriodIdForAds, j5, j4);
                }
            }
            j4 = j2;
            j5 = j3;
            return getMediaPeriodInfo(timeline, resolveMediaPeriodIdForAds, j5, j4);
        }
        MediaSource.MediaPeriodId mediaPeriodId = mediaPeriodInfo.id;
        timeline.getPeriodByUid(mediaPeriodId.periodUid, this.period);
        if (mediaPeriodId.isAd()) {
            int i2 = mediaPeriodId.adGroupIndex;
            int adCountInAdGroup = this.period.getAdCountInAdGroup(i2);
            if (adCountInAdGroup == -1) {
                return null;
            }
            int nextAdIndexToPlay = this.period.getNextAdIndexToPlay(i2, mediaPeriodId.adIndexInAdGroup);
            if (nextAdIndexToPlay < adCountInAdGroup) {
                return getMediaPeriodInfoForAd(timeline, mediaPeriodId.periodUid, i2, nextAdIndexToPlay, mediaPeriodInfo.requestedContentPositionUs, mediaPeriodId.windowSequenceNumber);
            }
            long j8 = mediaPeriodInfo.requestedContentPositionUs;
            if (j8 == -9223372036854775807L) {
                Timeline.Window window = this.window;
                Timeline.Period period2 = this.period;
                Pair<Object, Long> periodPositionUs2 = timeline.getPeriodPositionUs(window, period2, period2.windowIndex, -9223372036854775807L, Math.max(0L, rendererOffset));
                if (periodPositionUs2 == null) {
                    return null;
                }
                j8 = ((Long) periodPositionUs2.second).longValue();
            }
            return getMediaPeriodInfoForContent(timeline, mediaPeriodId.periodUid, Math.max(getMinStartPositionAfterAdGroupUs(timeline, mediaPeriodId.periodUid, mediaPeriodId.adGroupIndex), j8), mediaPeriodInfo.requestedContentPositionUs, mediaPeriodId.windowSequenceNumber);
        }
        int firstAdIndexToPlay = this.period.getFirstAdIndexToPlay(mediaPeriodId.nextAdGroupIndex);
        boolean z2 = this.period.isServerSideInsertedAdGroup(mediaPeriodId.nextAdGroupIndex) && this.period.getAdState(mediaPeriodId.nextAdGroupIndex, firstAdIndexToPlay) == 3;
        if (firstAdIndexToPlay == this.period.getAdCountInAdGroup(mediaPeriodId.nextAdGroupIndex) || z2) {
            return getMediaPeriodInfoForContent(timeline, mediaPeriodId.periodUid, getMinStartPositionAfterAdGroupUs(timeline, mediaPeriodId.periodUid, mediaPeriodId.nextAdGroupIndex), mediaPeriodInfo.durationUs, mediaPeriodId.windowSequenceNumber);
        }
        return getMediaPeriodInfoForAd(timeline, mediaPeriodId.periodUid, mediaPeriodId.nextAdGroupIndex, firstAdIndexToPlay, mediaPeriodInfo.durationUs, mediaPeriodId.windowSequenceNumber);
    }

    private MediaPeriodInfo getMediaPeriodInfo(Timeline timeline, MediaSource.MediaPeriodId mediaPeriodId, long j, long j2) {
        timeline.getPeriodByUid(mediaPeriodId.periodUid, this.period);
        if (mediaPeriodId.isAd()) {
            return getMediaPeriodInfoForAd(timeline, mediaPeriodId.periodUid, mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup, j, mediaPeriodId.windowSequenceNumber);
        }
        return getMediaPeriodInfoForContent(timeline, mediaPeriodId.periodUid, j2, j, mediaPeriodId.windowSequenceNumber);
    }

    private MediaPeriodInfo getMediaPeriodInfoForAd(Timeline timeline, Object obj, int i, int i2, long j, long j2) {
        MediaSource.MediaPeriodId mediaPeriodId = new MediaSource.MediaPeriodId(obj, i, i2, j2);
        long adDurationUs = timeline.getPeriodByUid(mediaPeriodId.periodUid, this.period).getAdDurationUs(mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup);
        long adResumePositionUs = i2 == this.period.getFirstAdIndexToPlay(i) ? this.period.getAdResumePositionUs() : 0L;
        return new MediaPeriodInfo(mediaPeriodId, (adDurationUs == -9223372036854775807L || adResumePositionUs < adDurationUs) ? adResumePositionUs : Math.max(0L, adDurationUs - 1), j, -9223372036854775807L, adDurationUs, this.period.isServerSideInsertedAdGroup(mediaPeriodId.adGroupIndex), false, false, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x002a, code lost:
        if (r9.isServerSideInsertedAdGroup(r9.getRemovedAdGroupCount()) != false) goto L7;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private MediaPeriodInfo getMediaPeriodInfoForContent(Timeline timeline, Object obj, long j, long j2, long j3) {
        boolean z;
        long j4;
        long j5;
        long j6;
        long j7 = j;
        timeline.getPeriodByUid(obj, this.period);
        int adGroupIndexAfterPositionUs = this.period.getAdGroupIndexAfterPositionUs(j7);
        int i = 1;
        if (adGroupIndexAfterPositionUs == -1) {
            if (this.period.getAdGroupCount() > 0) {
                Timeline.Period period = this.period;
            }
            z = false;
        } else {
            if (this.period.isServerSideInsertedAdGroup(adGroupIndexAfterPositionUs)) {
                long adGroupTimeUs = this.period.getAdGroupTimeUs(adGroupIndexAfterPositionUs);
                Timeline.Period period2 = this.period;
                if (adGroupTimeUs == period2.durationUs && period2.hasPlayedAdGroup(adGroupIndexAfterPositionUs)) {
                    adGroupIndexAfterPositionUs = -1;
                    z = true;
                }
            }
            z = false;
        }
        MediaSource.MediaPeriodId mediaPeriodId = new MediaSource.MediaPeriodId(obj, j3, adGroupIndexAfterPositionUs);
        boolean isLastInPeriod = isLastInPeriod(mediaPeriodId);
        boolean isLastInWindow = isLastInWindow(timeline, mediaPeriodId);
        boolean isLastInTimeline = isLastInTimeline(timeline, mediaPeriodId, isLastInPeriod);
        boolean z2 = adGroupIndexAfterPositionUs != -1 && this.period.isServerSideInsertedAdGroup(adGroupIndexAfterPositionUs);
        if (adGroupIndexAfterPositionUs != -1) {
            j5 = this.period.getAdGroupTimeUs(adGroupIndexAfterPositionUs);
        } else if (z) {
            j5 = this.period.durationUs;
        } else {
            j4 = -9223372036854775807L;
            j6 = (j4 != -9223372036854775807L || j4 == Long.MIN_VALUE) ? this.period.durationUs : j4;
            if (j6 != -9223372036854775807L && j7 >= j6) {
                if (!isLastInTimeline && z) {
                    i = 0;
                }
                j7 = Math.max(0L, j6 - i);
            }
            return new MediaPeriodInfo(mediaPeriodId, j7, j2, j4, j6, z2, isLastInPeriod, isLastInWindow, isLastInTimeline);
        }
        j4 = j5;
        if (j4 != -9223372036854775807L) {
        }
        if (j6 != -9223372036854775807L) {
            if (!isLastInTimeline) {
                i = 0;
            }
            j7 = Math.max(0L, j6 - i);
        }
        return new MediaPeriodInfo(mediaPeriodId, j7, j2, j4, j6, z2, isLastInPeriod, isLastInWindow, isLastInTimeline);
    }

    private boolean isLastInPeriod(MediaSource.MediaPeriodId mediaPeriodId) {
        return !mediaPeriodId.isAd() && mediaPeriodId.nextAdGroupIndex == -1;
    }

    private boolean isLastInWindow(Timeline timeline, MediaSource.MediaPeriodId mediaPeriodId) {
        if (isLastInPeriod(mediaPeriodId)) {
            return timeline.getWindow(timeline.getPeriodByUid(mediaPeriodId.periodUid, this.period).windowIndex, this.window).lastPeriodIndex == timeline.getIndexOfPeriod(mediaPeriodId.periodUid);
        }
        return false;
    }

    private boolean isLastInTimeline(Timeline timeline, MediaSource.MediaPeriodId mediaPeriodId, boolean z) {
        int indexOfPeriod = timeline.getIndexOfPeriod(mediaPeriodId.periodUid);
        return !timeline.getWindow(timeline.getPeriod(indexOfPeriod, this.period).windowIndex, this.window).isDynamic && timeline.isLastPeriod(indexOfPeriod, this.period, this.window, this.repeatMode, this.shuffleModeEnabled) && z;
    }

    private long getMinStartPositionAfterAdGroupUs(Timeline timeline, Object obj, int i) {
        timeline.getPeriodByUid(obj, this.period);
        long adGroupTimeUs = this.period.getAdGroupTimeUs(i);
        if (adGroupTimeUs == Long.MIN_VALUE) {
            return this.period.durationUs;
        }
        return adGroupTimeUs + this.period.getContentResumeOffsetUs(i);
    }
}
