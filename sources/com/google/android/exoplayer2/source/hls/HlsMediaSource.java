package com.google.android.exoplayer2.source.hls;

import android.os.Looper;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManagerProvider;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManagerProvider;
import com.google.android.exoplayer2.offline.StreamKey;
import com.google.android.exoplayer2.source.BaseMediaSource;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.DefaultCompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.SinglePeriodTimeline;
import com.google.android.exoplayer2.source.hls.playlist.DefaultHlsPlaylistParserFactory;
import com.google.android.exoplayer2.source.hls.playlist.DefaultHlsPlaylistTracker;
import com.google.android.exoplayer2.source.hls.playlist.FilteringHlsPlaylistParserFactory;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMultivariantPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParserFactory;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.List;
/* loaded from: classes.dex */
public final class HlsMediaSource extends BaseMediaSource implements HlsPlaylistTracker.PrimaryPlaylistListener {
    private final boolean allowChunklessPreparation;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final HlsDataSourceFactory dataSourceFactory;
    private final DrmSessionManager drmSessionManager;
    private final long elapsedRealTimeOffsetMs;
    private final HlsExtractorFactory extractorFactory;
    private MediaItem.LiveConfiguration liveConfiguration;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private final MediaItem.LocalConfiguration localConfiguration;
    private final MediaItem mediaItem;
    private TransferListener mediaTransferListener;
    private final int metadataType;
    private final HlsPlaylistTracker playlistTracker;
    private final boolean useSessionKeys;

    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.hls");
    }

    /* loaded from: classes.dex */
    public static final class Factory implements MediaSource.Factory {
        private boolean allowChunklessPreparation;
        private CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
        private DrmSessionManagerProvider drmSessionManagerProvider;
        private long elapsedRealTimeOffsetMs;
        private HlsExtractorFactory extractorFactory;
        private final HlsDataSourceFactory hlsDataSourceFactory;
        private LoadErrorHandlingPolicy loadErrorHandlingPolicy;
        private int metadataType;
        private HlsPlaylistParserFactory playlistParserFactory;
        private HlsPlaylistTracker.Factory playlistTrackerFactory;
        private boolean useSessionKeys;

        public Factory(DataSource.Factory factory) {
            this(new DefaultHlsDataSourceFactory(factory));
        }

        public Factory(HlsDataSourceFactory hlsDataSourceFactory) {
            this.hlsDataSourceFactory = (HlsDataSourceFactory) Assertions.checkNotNull(hlsDataSourceFactory);
            this.drmSessionManagerProvider = new DefaultDrmSessionManagerProvider();
            this.playlistParserFactory = new DefaultHlsPlaylistParserFactory();
            this.playlistTrackerFactory = DefaultHlsPlaylistTracker.FACTORY;
            this.extractorFactory = HlsExtractorFactory.DEFAULT;
            this.loadErrorHandlingPolicy = new DefaultLoadErrorHandlingPolicy();
            this.compositeSequenceableLoaderFactory = new DefaultCompositeSequenceableLoaderFactory();
            this.metadataType = 1;
            this.elapsedRealTimeOffsetMs = -9223372036854775807L;
            this.allowChunklessPreparation = true;
        }

        @Override // com.google.android.exoplayer2.source.MediaSource.Factory
        public Factory setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy loadErrorHandlingPolicy) {
            this.loadErrorHandlingPolicy = (LoadErrorHandlingPolicy) Assertions.checkNotNull(loadErrorHandlingPolicy, "MediaSource.Factory#setLoadErrorHandlingPolicy no longer handles null by instantiating a new DefaultLoadErrorHandlingPolicy. Explicitly construct and pass an instance in order to retain the old behavior.");
            return this;
        }

        @Override // com.google.android.exoplayer2.source.MediaSource.Factory
        public Factory setDrmSessionManagerProvider(DrmSessionManagerProvider drmSessionManagerProvider) {
            this.drmSessionManagerProvider = (DrmSessionManagerProvider) Assertions.checkNotNull(drmSessionManagerProvider, "MediaSource.Factory#setDrmSessionManagerProvider no longer handles null by instantiating a new DefaultDrmSessionManagerProvider. Explicitly construct and pass an instance in order to retain the old behavior.");
            return this;
        }

        @Override // com.google.android.exoplayer2.source.MediaSource.Factory
        public HlsMediaSource createMediaSource(MediaItem mediaItem) {
            Assertions.checkNotNull(mediaItem.localConfiguration);
            HlsPlaylistParserFactory hlsPlaylistParserFactory = this.playlistParserFactory;
            List<StreamKey> list = mediaItem.localConfiguration.streamKeys;
            if (!list.isEmpty()) {
                hlsPlaylistParserFactory = new FilteringHlsPlaylistParserFactory(hlsPlaylistParserFactory, list);
            }
            HlsDataSourceFactory hlsDataSourceFactory = this.hlsDataSourceFactory;
            HlsExtractorFactory hlsExtractorFactory = this.extractorFactory;
            CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory = this.compositeSequenceableLoaderFactory;
            DrmSessionManager drmSessionManager = this.drmSessionManagerProvider.get(mediaItem);
            LoadErrorHandlingPolicy loadErrorHandlingPolicy = this.loadErrorHandlingPolicy;
            return new HlsMediaSource(mediaItem, hlsDataSourceFactory, hlsExtractorFactory, compositeSequenceableLoaderFactory, drmSessionManager, loadErrorHandlingPolicy, this.playlistTrackerFactory.createTracker(this.hlsDataSourceFactory, loadErrorHandlingPolicy, hlsPlaylistParserFactory), this.elapsedRealTimeOffsetMs, this.allowChunklessPreparation, this.metadataType, this.useSessionKeys);
        }
    }

    private HlsMediaSource(MediaItem mediaItem, HlsDataSourceFactory hlsDataSourceFactory, HlsExtractorFactory hlsExtractorFactory, CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, DrmSessionManager drmSessionManager, LoadErrorHandlingPolicy loadErrorHandlingPolicy, HlsPlaylistTracker hlsPlaylistTracker, long j, boolean z, int i, boolean z2) {
        this.localConfiguration = (MediaItem.LocalConfiguration) Assertions.checkNotNull(mediaItem.localConfiguration);
        this.mediaItem = mediaItem;
        this.liveConfiguration = mediaItem.liveConfiguration;
        this.dataSourceFactory = hlsDataSourceFactory;
        this.extractorFactory = hlsExtractorFactory;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.drmSessionManager = drmSessionManager;
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
        this.playlistTracker = hlsPlaylistTracker;
        this.elapsedRealTimeOffsetMs = j;
        this.allowChunklessPreparation = z;
        this.metadataType = i;
        this.useSessionKeys = z2;
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public MediaItem getMediaItem() {
        return this.mediaItem;
    }

    @Override // com.google.android.exoplayer2.source.BaseMediaSource
    protected void prepareSourceInternal(TransferListener transferListener) {
        this.mediaTransferListener = transferListener;
        this.drmSessionManager.prepare();
        this.drmSessionManager.setPlayer((Looper) Assertions.checkNotNull(Looper.myLooper()), getPlayerId());
        this.playlistTracker.start(this.localConfiguration.uri, createEventDispatcher(null), this);
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.playlistTracker.maybeThrowPrimaryPlaylistRefreshError();
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public MediaPeriod createPeriod(MediaSource.MediaPeriodId mediaPeriodId, Allocator allocator, long j) {
        MediaSourceEventListener.EventDispatcher createEventDispatcher = createEventDispatcher(mediaPeriodId);
        return new HlsMediaPeriod(this.extractorFactory, this.playlistTracker, this.dataSourceFactory, this.mediaTransferListener, this.drmSessionManager, createDrmEventDispatcher(mediaPeriodId), this.loadErrorHandlingPolicy, createEventDispatcher, allocator, this.compositeSequenceableLoaderFactory, this.allowChunklessPreparation, this.metadataType, this.useSessionKeys, getPlayerId());
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void releasePeriod(MediaPeriod mediaPeriod) {
        ((HlsMediaPeriod) mediaPeriod).release();
    }

    @Override // com.google.android.exoplayer2.source.BaseMediaSource
    protected void releaseSourceInternal() {
        this.playlistTracker.stop();
        this.drmSessionManager.release();
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker.PrimaryPlaylistListener
    public void onPrimaryPlaylistRefreshed(HlsMediaPlaylist hlsMediaPlaylist) {
        SinglePeriodTimeline createTimelineForOnDemand;
        long usToMs = hlsMediaPlaylist.hasProgramDateTime ? Util.usToMs(hlsMediaPlaylist.startTimeUs) : -9223372036854775807L;
        int i = hlsMediaPlaylist.playlistType;
        long j = (i == 2 || i == 1) ? usToMs : -9223372036854775807L;
        HlsManifest hlsManifest = new HlsManifest((HlsMultivariantPlaylist) Assertions.checkNotNull(this.playlistTracker.getMultivariantPlaylist()), hlsMediaPlaylist);
        if (this.playlistTracker.isLive()) {
            createTimelineForOnDemand = createTimelineForLive(hlsMediaPlaylist, j, usToMs, hlsManifest);
        } else {
            createTimelineForOnDemand = createTimelineForOnDemand(hlsMediaPlaylist, j, usToMs, hlsManifest);
        }
        refreshSourceInfo(createTimelineForOnDemand);
    }

    private SinglePeriodTimeline createTimelineForLive(HlsMediaPlaylist hlsMediaPlaylist, long j, long j2, HlsManifest hlsManifest) {
        long targetLiveOffsetUs;
        long initialStartTimeUs = hlsMediaPlaylist.startTimeUs - this.playlistTracker.getInitialStartTimeUs();
        long j3 = hlsMediaPlaylist.hasEndTag ? initialStartTimeUs + hlsMediaPlaylist.durationUs : -9223372036854775807L;
        long liveEdgeOffsetUs = getLiveEdgeOffsetUs(hlsMediaPlaylist);
        long j4 = this.liveConfiguration.targetOffsetMs;
        if (j4 != -9223372036854775807L) {
            targetLiveOffsetUs = Util.msToUs(j4);
        } else {
            targetLiveOffsetUs = getTargetLiveOffsetUs(hlsMediaPlaylist, liveEdgeOffsetUs);
        }
        updateLiveConfiguration(hlsMediaPlaylist, Util.constrainValue(targetLiveOffsetUs, liveEdgeOffsetUs, hlsMediaPlaylist.durationUs + liveEdgeOffsetUs));
        return new SinglePeriodTimeline(j, j2, -9223372036854775807L, j3, hlsMediaPlaylist.durationUs, initialStartTimeUs, getLiveWindowDefaultStartPositionUs(hlsMediaPlaylist, liveEdgeOffsetUs), true, !hlsMediaPlaylist.hasEndTag, hlsMediaPlaylist.playlistType == 2 && hlsMediaPlaylist.hasPositiveStartOffset, hlsManifest, this.mediaItem, this.liveConfiguration);
    }

    private SinglePeriodTimeline createTimelineForOnDemand(HlsMediaPlaylist hlsMediaPlaylist, long j, long j2, HlsManifest hlsManifest) {
        long j3;
        if (hlsMediaPlaylist.startOffsetUs == -9223372036854775807L || hlsMediaPlaylist.segments.isEmpty()) {
            j3 = 0;
        } else {
            if (!hlsMediaPlaylist.preciseStart) {
                long j4 = hlsMediaPlaylist.startOffsetUs;
                if (j4 != hlsMediaPlaylist.durationUs) {
                    j3 = findClosestPrecedingSegment(hlsMediaPlaylist.segments, j4).relativeStartTimeUs;
                }
            }
            j3 = hlsMediaPlaylist.startOffsetUs;
        }
        long j5 = hlsMediaPlaylist.durationUs;
        return new SinglePeriodTimeline(j, j2, -9223372036854775807L, j5, j5, 0L, j3, true, false, true, hlsManifest, this.mediaItem, null);
    }

    private long getLiveEdgeOffsetUs(HlsMediaPlaylist hlsMediaPlaylist) {
        if (hlsMediaPlaylist.hasProgramDateTime) {
            return Util.msToUs(Util.getNowUnixTimeMs(this.elapsedRealTimeOffsetMs)) - hlsMediaPlaylist.getEndTimeUs();
        }
        return 0L;
    }

    private long getLiveWindowDefaultStartPositionUs(HlsMediaPlaylist hlsMediaPlaylist, long j) {
        long j2 = hlsMediaPlaylist.startOffsetUs;
        if (j2 == -9223372036854775807L) {
            j2 = (hlsMediaPlaylist.durationUs + j) - Util.msToUs(this.liveConfiguration.targetOffsetMs);
        }
        if (hlsMediaPlaylist.preciseStart) {
            return j2;
        }
        HlsMediaPlaylist.Part findClosestPrecedingIndependentPart = findClosestPrecedingIndependentPart(hlsMediaPlaylist.trailingParts, j2);
        if (findClosestPrecedingIndependentPart != null) {
            return findClosestPrecedingIndependentPart.relativeStartTimeUs;
        }
        if (hlsMediaPlaylist.segments.isEmpty()) {
            return 0L;
        }
        HlsMediaPlaylist.Segment findClosestPrecedingSegment = findClosestPrecedingSegment(hlsMediaPlaylist.segments, j2);
        HlsMediaPlaylist.Part findClosestPrecedingIndependentPart2 = findClosestPrecedingIndependentPart(findClosestPrecedingSegment.parts, j2);
        if (findClosestPrecedingIndependentPart2 != null) {
            return findClosestPrecedingIndependentPart2.relativeStartTimeUs;
        }
        return findClosestPrecedingSegment.relativeStartTimeUs;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x003a  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x003d  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0048  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateLiveConfiguration(HlsMediaPlaylist hlsMediaPlaylist, long j) {
        boolean z;
        MediaItem.LiveConfiguration liveConfiguration = this.mediaItem.liveConfiguration;
        if (liveConfiguration.minPlaybackSpeed == -3.4028235E38f && liveConfiguration.maxPlaybackSpeed == -3.4028235E38f) {
            HlsMediaPlaylist.ServerControl serverControl = hlsMediaPlaylist.serverControl;
            if (serverControl.holdBackUs == -9223372036854775807L && serverControl.partHoldBackUs == -9223372036854775807L) {
                z = true;
                this.liveConfiguration = new MediaItem.LiveConfiguration.Builder().setTargetOffsetMs(Util.usToMs(j)).setMinPlaybackSpeed(!z ? 1.0f : this.liveConfiguration.minPlaybackSpeed).setMaxPlaybackSpeed(z ? 1.0f : this.liveConfiguration.maxPlaybackSpeed).build();
            }
        }
        z = false;
        this.liveConfiguration = new MediaItem.LiveConfiguration.Builder().setTargetOffsetMs(Util.usToMs(j)).setMinPlaybackSpeed(!z ? 1.0f : this.liveConfiguration.minPlaybackSpeed).setMaxPlaybackSpeed(z ? 1.0f : this.liveConfiguration.maxPlaybackSpeed).build();
    }

    private static long getTargetLiveOffsetUs(HlsMediaPlaylist hlsMediaPlaylist, long j) {
        long j2;
        HlsMediaPlaylist.ServerControl serverControl = hlsMediaPlaylist.serverControl;
        long j3 = hlsMediaPlaylist.startOffsetUs;
        if (j3 != -9223372036854775807L) {
            j2 = hlsMediaPlaylist.durationUs - j3;
        } else {
            long j4 = serverControl.partHoldBackUs;
            if (j4 == -9223372036854775807L || hlsMediaPlaylist.partTargetDurationUs == -9223372036854775807L) {
                long j5 = serverControl.holdBackUs;
                j2 = j5 != -9223372036854775807L ? j5 : hlsMediaPlaylist.targetDurationUs * 3;
            } else {
                j2 = j4;
            }
        }
        return j2 + j;
    }

    private static HlsMediaPlaylist.Part findClosestPrecedingIndependentPart(List<HlsMediaPlaylist.Part> list, long j) {
        HlsMediaPlaylist.Part part = null;
        for (int i = 0; i < list.size(); i++) {
            HlsMediaPlaylist.Part part2 = list.get(i);
            long j2 = part2.relativeStartTimeUs;
            if (j2 <= j && part2.isIndependent) {
                part = part2;
            } else if (j2 > j) {
                break;
            }
        }
        return part;
    }

    private static HlsMediaPlaylist.Segment findClosestPrecedingSegment(List<HlsMediaPlaylist.Segment> list, long j) {
        return list.get(Util.binarySearchFloor((List<? extends Comparable<? super Long>>) list, Long.valueOf(j), true, true));
    }
}
