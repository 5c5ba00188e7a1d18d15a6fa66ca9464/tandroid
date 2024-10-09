package com.google.android.exoplayer2;

import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import com.google.android.exoplayer2.Bundleable;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.text.CueGroup;
import com.google.android.exoplayer2.util.FlagSet;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.google.android.exoplayer2.video.VideoSize;
import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public interface Player {
    public static final ArrayList videoListeners = new ArrayList();

    /* loaded from: classes.dex */
    public abstract /* synthetic */ class -CC {
    }

    /* loaded from: classes.dex */
    public static final class Commands implements Bundleable {
        private final FlagSet flags;
        public static final Commands EMPTY = new Builder().build();
        private static final String FIELD_COMMANDS = Util.intToStringMaxRadix(0);
        public static final Bundleable.Creator CREATOR = new Bundleable.Creator() { // from class: com.google.android.exoplayer2.Player$Commands$$ExternalSyntheticLambda0
            @Override // com.google.android.exoplayer2.Bundleable.Creator
            public final Bundleable fromBundle(Bundle bundle) {
                Player.Commands fromBundle;
                fromBundle = Player.Commands.fromBundle(bundle);
                return fromBundle;
            }
        };

        /* loaded from: classes.dex */
        public static final class Builder {
            private static final int[] SUPPORTED_COMMANDS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 31, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
            private final FlagSet.Builder flagsBuilder = new FlagSet.Builder();

            public Builder add(int i) {
                this.flagsBuilder.add(i);
                return this;
            }

            public Builder addAll(Commands commands) {
                this.flagsBuilder.addAll(commands.flags);
                return this;
            }

            public Builder addAll(int... iArr) {
                this.flagsBuilder.addAll(iArr);
                return this;
            }

            public Builder addIf(int i, boolean z) {
                this.flagsBuilder.addIf(i, z);
                return this;
            }

            public Commands build() {
                return new Commands(this.flagsBuilder.build());
            }
        }

        private Commands(FlagSet flagSet) {
            this.flags = flagSet;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Commands fromBundle(Bundle bundle) {
            ArrayList<Integer> integerArrayList = bundle.getIntegerArrayList(FIELD_COMMANDS);
            if (integerArrayList == null) {
                return EMPTY;
            }
            Builder builder = new Builder();
            for (int i = 0; i < integerArrayList.size(); i++) {
                builder.add(integerArrayList.get(i).intValue());
            }
            return builder.build();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof Commands) {
                return this.flags.equals(((Commands) obj).flags);
            }
            return false;
        }

        public int hashCode() {
            return this.flags.hashCode();
        }

        @Override // com.google.android.exoplayer2.Bundleable
        public Bundle toBundle() {
            Bundle bundle = new Bundle();
            ArrayList<Integer> arrayList = new ArrayList<>();
            for (int i = 0; i < this.flags.size(); i++) {
                arrayList.add(Integer.valueOf(this.flags.get(i)));
            }
            bundle.putIntegerArrayList(FIELD_COMMANDS, arrayList);
            return bundle;
        }
    }

    /* loaded from: classes.dex */
    public static final class Events {
        private final FlagSet flags;

        public Events(FlagSet flagSet) {
            this.flags = flagSet;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof Events) {
                return this.flags.equals(((Events) obj).flags);
            }
            return false;
        }

        public int hashCode() {
            return this.flags.hashCode();
        }
    }

    /* loaded from: classes.dex */
    public interface Listener {

        /* loaded from: classes.dex */
        public abstract /* synthetic */ class -CC {
            public static void $default$onAudioAttributesChanged(Listener listener, AudioAttributes audioAttributes) {
            }

            public static void $default$onAvailableCommandsChanged(Listener listener, Commands commands) {
            }

            public static void $default$onCues(Listener listener, CueGroup cueGroup) {
            }

            public static void $default$onCues(Listener listener, List list) {
            }

            public static void $default$onDeviceInfoChanged(Listener listener, DeviceInfo deviceInfo) {
            }

            public static void $default$onDeviceVolumeChanged(Listener listener, int i, boolean z) {
            }

            public static void $default$onEvents(Listener listener, Player player, Events events) {
            }

            public static void $default$onIsLoadingChanged(Listener listener, boolean z) {
            }

            public static void $default$onIsPlayingChanged(Listener listener, boolean z) {
            }

            public static void $default$onLoadingChanged(Listener listener, boolean z) {
            }

            public static void $default$onMediaItemTransition(Listener listener, MediaItem mediaItem, int i) {
            }

            public static void $default$onMediaMetadataChanged(Listener listener, MediaMetadata mediaMetadata) {
            }

            public static void $default$onMetadata(Listener listener, Metadata metadata) {
            }

            public static void $default$onPlayWhenReadyChanged(Listener listener, boolean z, int i) {
            }

            public static void $default$onPlaybackParametersChanged(Listener listener, PlaybackParameters playbackParameters) {
            }

            public static void $default$onPlaybackStateChanged(Listener listener, int i) {
            }

            public static void $default$onPlaybackSuppressionReasonChanged(Listener listener, int i) {
            }

            public static void $default$onPlayerError(Listener listener, PlaybackException playbackException) {
            }

            public static void $default$onPlayerErrorChanged(Listener listener, PlaybackException playbackException) {
            }

            public static void $default$onPositionDiscontinuity(Listener listener, int i) {
            }

            public static void $default$onPositionDiscontinuity(Listener listener, PositionInfo positionInfo, PositionInfo positionInfo2, int i) {
            }

            public static void $default$onRenderedFirstFrame(Listener listener) {
            }

            public static void $default$onRepeatModeChanged(Listener listener, int i) {
            }

            public static void $default$onSeekProcessed(Listener listener) {
            }

            public static void $default$onSkipSilenceEnabledChanged(Listener listener, boolean z) {
            }

            public static void $default$onSurfaceSizeChanged(Listener listener, int i, int i2) {
            }

            public static void $default$onTimelineChanged(Listener listener, Timeline timeline, int i) {
            }

            public static void $default$onTracksChanged(Listener listener, Tracks tracks) {
            }

            public static void $default$onVideoSizeChanged(Listener listener, VideoSize videoSize) {
            }

            public static void $default$onVolumeChanged(Listener listener, float f) {
            }
        }

        void onAudioAttributesChanged(AudioAttributes audioAttributes);

        void onAvailableCommandsChanged(Commands commands);

        void onCues(CueGroup cueGroup);

        void onCues(List list);

        void onDeviceInfoChanged(DeviceInfo deviceInfo);

        void onDeviceVolumeChanged(int i, boolean z);

        void onEvents(Player player, Events events);

        void onIsLoadingChanged(boolean z);

        void onIsPlayingChanged(boolean z);

        void onLoadingChanged(boolean z);

        void onMediaItemTransition(MediaItem mediaItem, int i);

        void onMediaMetadataChanged(MediaMetadata mediaMetadata);

        void onMetadata(Metadata metadata);

        void onPlayWhenReadyChanged(boolean z, int i);

        void onPlaybackParametersChanged(PlaybackParameters playbackParameters);

        void onPlaybackStateChanged(int i);

        void onPlaybackSuppressionReasonChanged(int i);

        void onPlayerError(PlaybackException playbackException);

        void onPlayerErrorChanged(PlaybackException playbackException);

        void onPlayerStateChanged(boolean z, int i);

        void onPositionDiscontinuity(int i);

        void onPositionDiscontinuity(PositionInfo positionInfo, PositionInfo positionInfo2, int i);

        void onRenderedFirstFrame();

        void onRepeatModeChanged(int i);

        void onSeekProcessed();

        void onSkipSilenceEnabledChanged(boolean z);

        void onSurfaceSizeChanged(int i, int i2);

        void onTimelineChanged(Timeline timeline, int i);

        void onTracksChanged(Tracks tracks);

        void onVideoSizeChanged(VideoSize videoSize);

        void onVolumeChanged(float f);
    }

    /* loaded from: classes.dex */
    public static final class PositionInfo implements Bundleable {
        public final int adGroupIndex;
        public final int adIndexInAdGroup;
        public final long contentPositionMs;
        public final MediaItem mediaItem;
        public final int mediaItemIndex;
        public final int periodIndex;
        public final Object periodUid;
        public final long positionMs;
        public final int windowIndex;
        public final Object windowUid;
        private static final String FIELD_MEDIA_ITEM_INDEX = Util.intToStringMaxRadix(0);
        private static final String FIELD_MEDIA_ITEM = Util.intToStringMaxRadix(1);
        private static final String FIELD_PERIOD_INDEX = Util.intToStringMaxRadix(2);
        private static final String FIELD_POSITION_MS = Util.intToStringMaxRadix(3);
        private static final String FIELD_CONTENT_POSITION_MS = Util.intToStringMaxRadix(4);
        private static final String FIELD_AD_GROUP_INDEX = Util.intToStringMaxRadix(5);
        private static final String FIELD_AD_INDEX_IN_AD_GROUP = Util.intToStringMaxRadix(6);
        public static final Bundleable.Creator CREATOR = new Bundleable.Creator() { // from class: com.google.android.exoplayer2.Player$PositionInfo$$ExternalSyntheticLambda0
            @Override // com.google.android.exoplayer2.Bundleable.Creator
            public final Bundleable fromBundle(Bundle bundle) {
                Player.PositionInfo fromBundle;
                fromBundle = Player.PositionInfo.fromBundle(bundle);
                return fromBundle;
            }
        };

        public PositionInfo(Object obj, int i, MediaItem mediaItem, Object obj2, int i2, long j, long j2, int i3, int i4) {
            this.windowUid = obj;
            this.windowIndex = i;
            this.mediaItemIndex = i;
            this.mediaItem = mediaItem;
            this.periodUid = obj2;
            this.periodIndex = i2;
            this.positionMs = j;
            this.contentPositionMs = j2;
            this.adGroupIndex = i3;
            this.adIndexInAdGroup = i4;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static PositionInfo fromBundle(Bundle bundle) {
            int i = bundle.getInt(FIELD_MEDIA_ITEM_INDEX, 0);
            Bundle bundle2 = bundle.getBundle(FIELD_MEDIA_ITEM);
            return new PositionInfo(null, i, bundle2 == null ? null : (MediaItem) MediaItem.CREATOR.fromBundle(bundle2), null, bundle.getInt(FIELD_PERIOD_INDEX, 0), bundle.getLong(FIELD_POSITION_MS, 0L), bundle.getLong(FIELD_CONTENT_POSITION_MS, 0L), bundle.getInt(FIELD_AD_GROUP_INDEX, -1), bundle.getInt(FIELD_AD_INDEX_IN_AD_GROUP, -1));
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || PositionInfo.class != obj.getClass()) {
                return false;
            }
            PositionInfo positionInfo = (PositionInfo) obj;
            return this.mediaItemIndex == positionInfo.mediaItemIndex && this.periodIndex == positionInfo.periodIndex && this.positionMs == positionInfo.positionMs && this.contentPositionMs == positionInfo.contentPositionMs && this.adGroupIndex == positionInfo.adGroupIndex && this.adIndexInAdGroup == positionInfo.adIndexInAdGroup && Objects.equal(this.windowUid, positionInfo.windowUid) && Objects.equal(this.periodUid, positionInfo.periodUid) && Objects.equal(this.mediaItem, positionInfo.mediaItem);
        }

        public int hashCode() {
            return Objects.hashCode(this.windowUid, Integer.valueOf(this.mediaItemIndex), this.mediaItem, this.periodUid, Integer.valueOf(this.periodIndex), Long.valueOf(this.positionMs), Long.valueOf(this.contentPositionMs), Integer.valueOf(this.adGroupIndex), Integer.valueOf(this.adIndexInAdGroup));
        }

        @Override // com.google.android.exoplayer2.Bundleable
        public Bundle toBundle() {
            return toBundle(true, true);
        }

        public Bundle toBundle(boolean z, boolean z2) {
            Bundle bundle = new Bundle();
            bundle.putInt(FIELD_MEDIA_ITEM_INDEX, z2 ? this.mediaItemIndex : 0);
            MediaItem mediaItem = this.mediaItem;
            if (mediaItem != null && z) {
                bundle.putBundle(FIELD_MEDIA_ITEM, mediaItem.toBundle());
            }
            bundle.putInt(FIELD_PERIOD_INDEX, z2 ? this.periodIndex : 0);
            bundle.putLong(FIELD_POSITION_MS, z ? this.positionMs : 0L);
            bundle.putLong(FIELD_CONTENT_POSITION_MS, z ? this.contentPositionMs : 0L);
            bundle.putInt(FIELD_AD_GROUP_INDEX, z ? this.adGroupIndex : -1);
            bundle.putInt(FIELD_AD_INDEX_IN_AD_GROUP, z ? this.adIndexInAdGroup : -1);
            return bundle;
        }
    }

    void addListener(Listener listener);

    void addVideoListener(VideoListener videoListener);

    void clearVideoTextureView(TextureView textureView);

    long getBufferedPosition();

    long getContentPosition();

    int getCurrentAdGroupIndex();

    int getCurrentAdIndexInAdGroup();

    int getCurrentMediaItemIndex();

    int getCurrentPeriodIndex();

    long getCurrentPosition();

    Timeline getCurrentTimeline();

    Tracks getCurrentTracks();

    long getDuration();

    boolean getPlayWhenReady();

    int getPlaybackState();

    int getPlaybackSuppressionReason();

    PlaybackException getPlayerError();

    int getRepeatMode();

    boolean getShuffleModeEnabled();

    long getTotalBufferedDuration();

    float getVolume();

    boolean hasNextMediaItem();

    boolean hasPreviousMediaItem();

    boolean isCurrentMediaItemDynamic();

    boolean isCurrentMediaItemLive();

    boolean isCurrentMediaItemSeekable();

    boolean isPlaying();

    boolean isPlayingAd();

    void pause();

    void play();

    void prepare();

    void release();

    void seekTo(long j);

    void setPlayWhenReady(boolean z);

    void setPlaybackParameters(PlaybackParameters playbackParameters);

    void setRepeatMode(int i);

    void setVideoSurface(Surface surface);

    void setVideoSurfaceView(SurfaceView surfaceView);

    void setVideoTextureView(TextureView textureView);

    void setVolume(float f);
}
