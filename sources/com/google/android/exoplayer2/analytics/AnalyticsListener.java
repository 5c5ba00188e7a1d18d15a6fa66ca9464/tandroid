package com.google.android.exoplayer2.analytics;

import android.util.SparseArray;
import com.google.android.exoplayer2.DeviceInfo;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Tracks;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.decoder.DecoderReuseEvaluation;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.LoadEventInfo;
import com.google.android.exoplayer2.source.MediaLoadData;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.text.CueGroup;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.FlagSet;
import com.google.android.exoplayer2.video.VideoSize;
import com.google.common.base.Objects;
import java.io.IOException;
import java.util.List;
/* loaded from: classes.dex */
public interface AnalyticsListener {

    /* loaded from: classes.dex */
    public abstract /* synthetic */ class -CC {
        public static void $default$onAudioAttributesChanged(AnalyticsListener analyticsListener, EventTime eventTime, AudioAttributes audioAttributes) {
        }

        public static void $default$onAudioCodecError(AnalyticsListener analyticsListener, EventTime eventTime, Exception exc) {
        }

        public static void $default$onAudioDecoderInitialized(AnalyticsListener analyticsListener, EventTime eventTime, String str, long j) {
        }

        public static void $default$onAudioDecoderInitialized(AnalyticsListener analyticsListener, EventTime eventTime, String str, long j, long j2) {
        }

        public static void $default$onAudioDecoderReleased(AnalyticsListener analyticsListener, EventTime eventTime, String str) {
        }

        public static void $default$onAudioDisabled(AnalyticsListener analyticsListener, EventTime eventTime, DecoderCounters decoderCounters) {
        }

        public static void $default$onAudioEnabled(AnalyticsListener analyticsListener, EventTime eventTime, DecoderCounters decoderCounters) {
        }

        public static void $default$onAudioInputFormatChanged(AnalyticsListener analyticsListener, EventTime eventTime, Format format) {
        }

        public static void $default$onAudioInputFormatChanged(AnalyticsListener analyticsListener, EventTime eventTime, Format format, DecoderReuseEvaluation decoderReuseEvaluation) {
        }

        public static void $default$onAudioPositionAdvancing(AnalyticsListener analyticsListener, EventTime eventTime, long j) {
        }

        public static void $default$onAudioSinkError(AnalyticsListener analyticsListener, EventTime eventTime, Exception exc) {
        }

        public static void $default$onAudioUnderrun(AnalyticsListener analyticsListener, EventTime eventTime, int i, long j, long j2) {
        }

        public static void $default$onAvailableCommandsChanged(AnalyticsListener analyticsListener, EventTime eventTime, Player.Commands commands) {
        }

        public static void $default$onBandwidthEstimate(AnalyticsListener analyticsListener, EventTime eventTime, int i, long j, long j2) {
        }

        public static void $default$onCues(AnalyticsListener analyticsListener, EventTime eventTime, CueGroup cueGroup) {
        }

        public static void $default$onCues(AnalyticsListener analyticsListener, EventTime eventTime, List list) {
        }

        public static void $default$onDecoderDisabled(AnalyticsListener analyticsListener, EventTime eventTime, int i, DecoderCounters decoderCounters) {
        }

        public static void $default$onDecoderEnabled(AnalyticsListener analyticsListener, EventTime eventTime, int i, DecoderCounters decoderCounters) {
        }

        public static void $default$onDecoderInitialized(AnalyticsListener analyticsListener, EventTime eventTime, int i, String str, long j) {
        }

        public static void $default$onDecoderInputFormatChanged(AnalyticsListener analyticsListener, EventTime eventTime, int i, Format format) {
        }

        public static void $default$onDeviceInfoChanged(AnalyticsListener analyticsListener, EventTime eventTime, DeviceInfo deviceInfo) {
        }

        public static void $default$onDeviceVolumeChanged(AnalyticsListener analyticsListener, EventTime eventTime, int i, boolean z) {
        }

        public static void $default$onDownstreamFormatChanged(AnalyticsListener analyticsListener, EventTime eventTime, MediaLoadData mediaLoadData) {
        }

        public static void $default$onDrmKeysLoaded(AnalyticsListener analyticsListener, EventTime eventTime) {
        }

        public static void $default$onDrmKeysRemoved(AnalyticsListener analyticsListener, EventTime eventTime) {
        }

        public static void $default$onDrmKeysRestored(AnalyticsListener analyticsListener, EventTime eventTime) {
        }

        public static void $default$onDrmSessionAcquired(AnalyticsListener analyticsListener, EventTime eventTime) {
        }

        public static void $default$onDrmSessionAcquired(AnalyticsListener analyticsListener, EventTime eventTime, int i) {
        }

        public static void $default$onDrmSessionManagerError(AnalyticsListener analyticsListener, EventTime eventTime, Exception exc) {
        }

        public static void $default$onDrmSessionReleased(AnalyticsListener analyticsListener, EventTime eventTime) {
        }

        public static void $default$onDroppedVideoFrames(AnalyticsListener analyticsListener, EventTime eventTime, int i, long j) {
        }

        public static void $default$onEvents(AnalyticsListener analyticsListener, Player player, Events events) {
        }

        public static void $default$onIsLoadingChanged(AnalyticsListener analyticsListener, EventTime eventTime, boolean z) {
        }

        public static void $default$onIsPlayingChanged(AnalyticsListener analyticsListener, EventTime eventTime, boolean z) {
        }

        public static void $default$onLoadCanceled(AnalyticsListener analyticsListener, EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
        }

        public static void $default$onLoadCompleted(AnalyticsListener analyticsListener, EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
        }

        public static void $default$onLoadError(AnalyticsListener analyticsListener, EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException iOException, boolean z) {
        }

        public static void $default$onLoadStarted(AnalyticsListener analyticsListener, EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
        }

        public static void $default$onLoadingChanged(AnalyticsListener analyticsListener, EventTime eventTime, boolean z) {
        }

        public static void $default$onMediaItemTransition(AnalyticsListener analyticsListener, EventTime eventTime, MediaItem mediaItem, int i) {
        }

        public static void $default$onMediaMetadataChanged(AnalyticsListener analyticsListener, EventTime eventTime, MediaMetadata mediaMetadata) {
        }

        public static void $default$onMetadata(AnalyticsListener analyticsListener, EventTime eventTime, Metadata metadata) {
        }

        public static void $default$onPlayWhenReadyChanged(AnalyticsListener analyticsListener, EventTime eventTime, boolean z, int i) {
        }

        public static void $default$onPlaybackParametersChanged(AnalyticsListener analyticsListener, EventTime eventTime, PlaybackParameters playbackParameters) {
        }

        public static void $default$onPlaybackStateChanged(AnalyticsListener analyticsListener, EventTime eventTime, int i) {
        }

        public static void $default$onPlaybackSuppressionReasonChanged(AnalyticsListener analyticsListener, EventTime eventTime, int i) {
        }

        public static void $default$onPlayerError(AnalyticsListener analyticsListener, EventTime eventTime, PlaybackException playbackException) {
        }

        public static void $default$onPlayerErrorChanged(AnalyticsListener analyticsListener, EventTime eventTime, PlaybackException playbackException) {
        }

        public static void $default$onPlayerReleased(AnalyticsListener analyticsListener, EventTime eventTime) {
        }

        public static void $default$onPlayerStateChanged(AnalyticsListener analyticsListener, EventTime eventTime, boolean z, int i) {
        }

        public static void $default$onPositionDiscontinuity(AnalyticsListener analyticsListener, EventTime eventTime, int i) {
        }

        public static void $default$onPositionDiscontinuity(AnalyticsListener analyticsListener, EventTime eventTime, Player.PositionInfo positionInfo, Player.PositionInfo positionInfo2, int i) {
        }

        public static void $default$onRenderedFirstFrame(AnalyticsListener analyticsListener, EventTime eventTime, Object obj, long j) {
        }

        public static void $default$onRepeatModeChanged(AnalyticsListener analyticsListener, EventTime eventTime, int i) {
        }

        public static void $default$onSeekProcessed(AnalyticsListener analyticsListener, EventTime eventTime) {
        }

        public static void $default$onSeekStarted(AnalyticsListener analyticsListener, EventTime eventTime) {
        }

        public static void $default$onSkipSilenceEnabledChanged(AnalyticsListener analyticsListener, EventTime eventTime, boolean z) {
        }

        public static void $default$onSurfaceSizeChanged(AnalyticsListener analyticsListener, EventTime eventTime, int i, int i2) {
        }

        public static void $default$onTimelineChanged(AnalyticsListener analyticsListener, EventTime eventTime, int i) {
        }

        public static void $default$onTracksChanged(AnalyticsListener analyticsListener, EventTime eventTime, Tracks tracks) {
        }

        public static void $default$onUpstreamDiscarded(AnalyticsListener analyticsListener, EventTime eventTime, MediaLoadData mediaLoadData) {
        }

        public static void $default$onVideoCodecError(AnalyticsListener analyticsListener, EventTime eventTime, Exception exc) {
        }

        public static void $default$onVideoDecoderInitialized(AnalyticsListener analyticsListener, EventTime eventTime, String str, long j) {
        }

        public static void $default$onVideoDecoderInitialized(AnalyticsListener analyticsListener, EventTime eventTime, String str, long j, long j2) {
        }

        public static void $default$onVideoDecoderReleased(AnalyticsListener analyticsListener, EventTime eventTime, String str) {
        }

        public static void $default$onVideoDisabled(AnalyticsListener analyticsListener, EventTime eventTime, DecoderCounters decoderCounters) {
        }

        public static void $default$onVideoEnabled(AnalyticsListener analyticsListener, EventTime eventTime, DecoderCounters decoderCounters) {
        }

        public static void $default$onVideoFrameProcessingOffset(AnalyticsListener analyticsListener, EventTime eventTime, long j, int i) {
        }

        public static void $default$onVideoInputFormatChanged(AnalyticsListener analyticsListener, EventTime eventTime, Format format) {
        }

        public static void $default$onVideoInputFormatChanged(AnalyticsListener analyticsListener, EventTime eventTime, Format format, DecoderReuseEvaluation decoderReuseEvaluation) {
        }

        public static void $default$onVideoSizeChanged(AnalyticsListener analyticsListener, EventTime eventTime, int i, int i2, int i3, float f) {
        }

        public static void $default$onVideoSizeChanged(AnalyticsListener analyticsListener, EventTime eventTime, VideoSize videoSize) {
        }

        public static void $default$onVolumeChanged(AnalyticsListener analyticsListener, EventTime eventTime, float f) {
        }
    }

    /* loaded from: classes.dex */
    public static final class EventTime {
        public final MediaSource.MediaPeriodId currentMediaPeriodId;
        public final long currentPlaybackPositionMs;
        public final Timeline currentTimeline;
        public final int currentWindowIndex;
        public final long eventPlaybackPositionMs;
        public final MediaSource.MediaPeriodId mediaPeriodId;
        public final long realtimeMs;
        public final Timeline timeline;
        public final long totalBufferedDurationMs;
        public final int windowIndex;

        public EventTime(long j, Timeline timeline, int i, MediaSource.MediaPeriodId mediaPeriodId, long j2, Timeline timeline2, int i2, MediaSource.MediaPeriodId mediaPeriodId2, long j3, long j4) {
            this.realtimeMs = j;
            this.timeline = timeline;
            this.windowIndex = i;
            this.mediaPeriodId = mediaPeriodId;
            this.eventPlaybackPositionMs = j2;
            this.currentTimeline = timeline2;
            this.currentWindowIndex = i2;
            this.currentMediaPeriodId = mediaPeriodId2;
            this.currentPlaybackPositionMs = j3;
            this.totalBufferedDurationMs = j4;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || EventTime.class != obj.getClass()) {
                return false;
            }
            EventTime eventTime = (EventTime) obj;
            return this.realtimeMs == eventTime.realtimeMs && this.windowIndex == eventTime.windowIndex && this.eventPlaybackPositionMs == eventTime.eventPlaybackPositionMs && this.currentWindowIndex == eventTime.currentWindowIndex && this.currentPlaybackPositionMs == eventTime.currentPlaybackPositionMs && this.totalBufferedDurationMs == eventTime.totalBufferedDurationMs && Objects.equal(this.timeline, eventTime.timeline) && Objects.equal(this.mediaPeriodId, eventTime.mediaPeriodId) && Objects.equal(this.currentTimeline, eventTime.currentTimeline) && Objects.equal(this.currentMediaPeriodId, eventTime.currentMediaPeriodId);
        }

        public int hashCode() {
            return Objects.hashCode(Long.valueOf(this.realtimeMs), this.timeline, Integer.valueOf(this.windowIndex), this.mediaPeriodId, Long.valueOf(this.eventPlaybackPositionMs), this.currentTimeline, Integer.valueOf(this.currentWindowIndex), this.currentMediaPeriodId, Long.valueOf(this.currentPlaybackPositionMs), Long.valueOf(this.totalBufferedDurationMs));
        }
    }

    /* loaded from: classes.dex */
    public static final class Events {
        private final SparseArray eventTimes;
        private final FlagSet flags;

        public Events(FlagSet flagSet, SparseArray sparseArray) {
            this.flags = flagSet;
            SparseArray sparseArray2 = new SparseArray(flagSet.size());
            for (int i = 0; i < flagSet.size(); i++) {
                int i2 = flagSet.get(i);
                sparseArray2.append(i2, (EventTime) Assertions.checkNotNull((EventTime) sparseArray.get(i2)));
            }
            this.eventTimes = sparseArray2;
        }

        public boolean contains(int i) {
            return this.flags.contains(i);
        }

        public int get(int i) {
            return this.flags.get(i);
        }

        public EventTime getEventTime(int i) {
            return (EventTime) Assertions.checkNotNull((EventTime) this.eventTimes.get(i));
        }

        public int size() {
            return this.flags.size();
        }
    }

    void onAudioAttributesChanged(EventTime eventTime, AudioAttributes audioAttributes);

    void onAudioCodecError(EventTime eventTime, Exception exc);

    void onAudioDecoderInitialized(EventTime eventTime, String str, long j);

    void onAudioDecoderInitialized(EventTime eventTime, String str, long j, long j2);

    void onAudioDecoderReleased(EventTime eventTime, String str);

    void onAudioDisabled(EventTime eventTime, DecoderCounters decoderCounters);

    void onAudioEnabled(EventTime eventTime, DecoderCounters decoderCounters);

    void onAudioInputFormatChanged(EventTime eventTime, Format format);

    void onAudioInputFormatChanged(EventTime eventTime, Format format, DecoderReuseEvaluation decoderReuseEvaluation);

    void onAudioPositionAdvancing(EventTime eventTime, long j);

    void onAudioSinkError(EventTime eventTime, Exception exc);

    void onAudioUnderrun(EventTime eventTime, int i, long j, long j2);

    void onAvailableCommandsChanged(EventTime eventTime, Player.Commands commands);

    void onBandwidthEstimate(EventTime eventTime, int i, long j, long j2);

    void onCues(EventTime eventTime, CueGroup cueGroup);

    void onCues(EventTime eventTime, List list);

    void onDecoderDisabled(EventTime eventTime, int i, DecoderCounters decoderCounters);

    void onDecoderEnabled(EventTime eventTime, int i, DecoderCounters decoderCounters);

    void onDecoderInitialized(EventTime eventTime, int i, String str, long j);

    void onDecoderInputFormatChanged(EventTime eventTime, int i, Format format);

    void onDeviceInfoChanged(EventTime eventTime, DeviceInfo deviceInfo);

    void onDeviceVolumeChanged(EventTime eventTime, int i, boolean z);

    void onDownstreamFormatChanged(EventTime eventTime, MediaLoadData mediaLoadData);

    void onDrmKeysLoaded(EventTime eventTime);

    void onDrmKeysRemoved(EventTime eventTime);

    void onDrmKeysRestored(EventTime eventTime);

    void onDrmSessionAcquired(EventTime eventTime);

    void onDrmSessionAcquired(EventTime eventTime, int i);

    void onDrmSessionManagerError(EventTime eventTime, Exception exc);

    void onDrmSessionReleased(EventTime eventTime);

    void onDroppedVideoFrames(EventTime eventTime, int i, long j);

    void onEvents(Player player, Events events);

    void onIsLoadingChanged(EventTime eventTime, boolean z);

    void onIsPlayingChanged(EventTime eventTime, boolean z);

    void onLoadCanceled(EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData);

    void onLoadCompleted(EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData);

    void onLoadError(EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException iOException, boolean z);

    void onLoadStarted(EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData);

    void onLoadingChanged(EventTime eventTime, boolean z);

    void onMediaItemTransition(EventTime eventTime, MediaItem mediaItem, int i);

    void onMediaMetadataChanged(EventTime eventTime, MediaMetadata mediaMetadata);

    void onMetadata(EventTime eventTime, Metadata metadata);

    void onPlayWhenReadyChanged(EventTime eventTime, boolean z, int i);

    void onPlaybackParametersChanged(EventTime eventTime, PlaybackParameters playbackParameters);

    void onPlaybackStateChanged(EventTime eventTime, int i);

    void onPlaybackSuppressionReasonChanged(EventTime eventTime, int i);

    void onPlayerError(EventTime eventTime, PlaybackException playbackException);

    void onPlayerErrorChanged(EventTime eventTime, PlaybackException playbackException);

    void onPlayerReleased(EventTime eventTime);

    void onPlayerStateChanged(EventTime eventTime, boolean z, int i);

    void onPositionDiscontinuity(EventTime eventTime, int i);

    void onPositionDiscontinuity(EventTime eventTime, Player.PositionInfo positionInfo, Player.PositionInfo positionInfo2, int i);

    void onRenderedFirstFrame(EventTime eventTime, Object obj, long j);

    void onRepeatModeChanged(EventTime eventTime, int i);

    void onSeekProcessed(EventTime eventTime);

    void onSeekStarted(EventTime eventTime);

    void onSkipSilenceEnabledChanged(EventTime eventTime, boolean z);

    void onSurfaceSizeChanged(EventTime eventTime, int i, int i2);

    void onTimelineChanged(EventTime eventTime, int i);

    void onTracksChanged(EventTime eventTime, Tracks tracks);

    void onUpstreamDiscarded(EventTime eventTime, MediaLoadData mediaLoadData);

    void onVideoCodecError(EventTime eventTime, Exception exc);

    void onVideoDecoderInitialized(EventTime eventTime, String str, long j);

    void onVideoDecoderInitialized(EventTime eventTime, String str, long j, long j2);

    void onVideoDecoderReleased(EventTime eventTime, String str);

    void onVideoDisabled(EventTime eventTime, DecoderCounters decoderCounters);

    void onVideoEnabled(EventTime eventTime, DecoderCounters decoderCounters);

    void onVideoFrameProcessingOffset(EventTime eventTime, long j, int i);

    void onVideoInputFormatChanged(EventTime eventTime, Format format);

    void onVideoInputFormatChanged(EventTime eventTime, Format format, DecoderReuseEvaluation decoderReuseEvaluation);

    void onVideoSizeChanged(EventTime eventTime, int i, int i2, int i3, float f);

    void onVideoSizeChanged(EventTime eventTime, VideoSize videoSize);

    void onVolumeChanged(EventTime eventTime, float f);
}
