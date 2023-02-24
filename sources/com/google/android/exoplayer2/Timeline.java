package com.google.android.exoplayer2;

import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import com.google.android.exoplayer2.Bundleable;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.BundleUtil;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
/* loaded from: classes.dex */
public abstract class Timeline implements Bundleable {
    public static final Timeline EMPTY = new Timeline() { // from class: com.google.android.exoplayer2.Timeline.1
        @Override // com.google.android.exoplayer2.Timeline
        public int getIndexOfPeriod(Object obj) {
            return -1;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public int getPeriodCount() {
            return 0;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public int getWindowCount() {
            return 0;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public Window getWindow(int i, Window window, long j) {
            throw new IndexOutOfBoundsException();
        }

        @Override // com.google.android.exoplayer2.Timeline
        public Period getPeriod(int i, Period period, boolean z) {
            throw new IndexOutOfBoundsException();
        }

        @Override // com.google.android.exoplayer2.Timeline
        public Object getUidOfPeriod(int i) {
            throw new IndexOutOfBoundsException();
        }
    };
    private static final String FIELD_WINDOWS = Util.intToStringMaxRadix(0);
    private static final String FIELD_PERIODS = Util.intToStringMaxRadix(1);
    private static final String FIELD_SHUFFLED_WINDOW_INDICES = Util.intToStringMaxRadix(2);

    public abstract int getIndexOfPeriod(Object obj);

    public abstract Period getPeriod(int i, Period period, boolean z);

    public abstract int getPeriodCount();

    public abstract Object getUidOfPeriod(int i);

    public abstract Window getWindow(int i, Window window, long j);

    public abstract int getWindowCount();

    /* loaded from: classes.dex */
    public static final class Window implements Bundleable {
        public long defaultPositionUs;
        public long durationUs;
        public long elapsedRealtimeEpochOffsetMs;
        public int firstPeriodIndex;
        public boolean isDynamic;
        @Deprecated
        public boolean isLive;
        public boolean isPlaceholder;
        public boolean isSeekable;
        public int lastPeriodIndex;
        public MediaItem.LiveConfiguration liveConfiguration;
        public Object manifest;
        public long positionInFirstPeriodUs;
        public long presentationStartTimeMs;
        @Deprecated
        public Object tag;
        public long windowStartTimeMs;
        public static final Object SINGLE_WINDOW_UID = new Object();
        private static final Object FAKE_WINDOW_UID = new Object();
        private static final MediaItem PLACEHOLDER_MEDIA_ITEM = new MediaItem.Builder().setMediaId("com.google.android.exoplayer2.Timeline").setUri(Uri.EMPTY).build();
        private static final String FIELD_MEDIA_ITEM = Util.intToStringMaxRadix(1);
        private static final String FIELD_PRESENTATION_START_TIME_MS = Util.intToStringMaxRadix(2);
        private static final String FIELD_WINDOW_START_TIME_MS = Util.intToStringMaxRadix(3);
        private static final String FIELD_ELAPSED_REALTIME_EPOCH_OFFSET_MS = Util.intToStringMaxRadix(4);
        private static final String FIELD_IS_SEEKABLE = Util.intToStringMaxRadix(5);
        private static final String FIELD_IS_DYNAMIC = Util.intToStringMaxRadix(6);
        private static final String FIELD_LIVE_CONFIGURATION = Util.intToStringMaxRadix(7);
        private static final String FIELD_IS_PLACEHOLDER = Util.intToStringMaxRadix(8);
        private static final String FIELD_DEFAULT_POSITION_US = Util.intToStringMaxRadix(9);
        private static final String FIELD_DURATION_US = Util.intToStringMaxRadix(10);
        private static final String FIELD_FIRST_PERIOD_INDEX = Util.intToStringMaxRadix(11);
        private static final String FIELD_LAST_PERIOD_INDEX = Util.intToStringMaxRadix(12);
        private static final String FIELD_POSITION_IN_FIRST_PERIOD_US = Util.intToStringMaxRadix(13);
        public static final Bundleable.Creator<Window> CREATOR = Timeline$Window$$ExternalSyntheticLambda0.INSTANCE;
        public Object uid = SINGLE_WINDOW_UID;
        public MediaItem mediaItem = PLACEHOLDER_MEDIA_ITEM;

        public Window set(Object obj, MediaItem mediaItem, Object obj2, long j, long j2, long j3, boolean z, boolean z2, MediaItem.LiveConfiguration liveConfiguration, long j4, long j5, int i, int i2, long j6) {
            MediaItem.LocalConfiguration localConfiguration;
            this.uid = obj;
            this.mediaItem = mediaItem != null ? mediaItem : PLACEHOLDER_MEDIA_ITEM;
            this.tag = (mediaItem == null || (localConfiguration = mediaItem.localConfiguration) == null) ? null : localConfiguration.tag;
            this.manifest = obj2;
            this.presentationStartTimeMs = j;
            this.windowStartTimeMs = j2;
            this.elapsedRealtimeEpochOffsetMs = j3;
            this.isSeekable = z;
            this.isDynamic = z2;
            this.isLive = liveConfiguration != null;
            this.liveConfiguration = liveConfiguration;
            this.defaultPositionUs = j4;
            this.durationUs = j5;
            this.firstPeriodIndex = i;
            this.lastPeriodIndex = i2;
            this.positionInFirstPeriodUs = j6;
            this.isPlaceholder = false;
            return this;
        }

        public long getDefaultPositionMs() {
            return Util.usToMs(this.defaultPositionUs);
        }

        public long getDefaultPositionUs() {
            return this.defaultPositionUs;
        }

        public long getDurationMs() {
            return Util.usToMs(this.durationUs);
        }

        public long getPositionInFirstPeriodUs() {
            return this.positionInFirstPeriodUs;
        }

        public long getCurrentUnixTimeMs() {
            return Util.getNowUnixTimeMs(this.elapsedRealtimeEpochOffsetMs);
        }

        public boolean isLive() {
            Assertions.checkState(this.isLive == (this.liveConfiguration != null));
            return this.liveConfiguration != null;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !Window.class.equals(obj.getClass())) {
                return false;
            }
            Window window = (Window) obj;
            return Util.areEqual(this.uid, window.uid) && Util.areEqual(this.mediaItem, window.mediaItem) && Util.areEqual(this.manifest, window.manifest) && Util.areEqual(this.liveConfiguration, window.liveConfiguration) && this.presentationStartTimeMs == window.presentationStartTimeMs && this.windowStartTimeMs == window.windowStartTimeMs && this.elapsedRealtimeEpochOffsetMs == window.elapsedRealtimeEpochOffsetMs && this.isSeekable == window.isSeekable && this.isDynamic == window.isDynamic && this.isPlaceholder == window.isPlaceholder && this.defaultPositionUs == window.defaultPositionUs && this.durationUs == window.durationUs && this.firstPeriodIndex == window.firstPeriodIndex && this.lastPeriodIndex == window.lastPeriodIndex && this.positionInFirstPeriodUs == window.positionInFirstPeriodUs;
        }

        public int hashCode() {
            int hashCode = (((217 + this.uid.hashCode()) * 31) + this.mediaItem.hashCode()) * 31;
            Object obj = this.manifest;
            int hashCode2 = (hashCode + (obj == null ? 0 : obj.hashCode())) * 31;
            MediaItem.LiveConfiguration liveConfiguration = this.liveConfiguration;
            int hashCode3 = liveConfiguration != null ? liveConfiguration.hashCode() : 0;
            long j = this.presentationStartTimeMs;
            long j2 = this.windowStartTimeMs;
            long j3 = this.elapsedRealtimeEpochOffsetMs;
            long j4 = this.defaultPositionUs;
            long j5 = this.durationUs;
            long j6 = this.positionInFirstPeriodUs;
            return ((((((((((((((((((((((hashCode2 + hashCode3) * 31) + ((int) (j ^ (j >>> 32)))) * 31) + ((int) (j2 ^ (j2 >>> 32)))) * 31) + ((int) (j3 ^ (j3 >>> 32)))) * 31) + (this.isSeekable ? 1 : 0)) * 31) + (this.isDynamic ? 1 : 0)) * 31) + (this.isPlaceholder ? 1 : 0)) * 31) + ((int) (j4 ^ (j4 >>> 32)))) * 31) + ((int) (j5 ^ (j5 >>> 32)))) * 31) + this.firstPeriodIndex) * 31) + this.lastPeriodIndex) * 31) + ((int) (j6 ^ (j6 >>> 32)));
        }

        @Override // com.google.android.exoplayer2.Bundleable
        public Bundle toBundle() {
            Bundle bundle = new Bundle();
            if (!MediaItem.EMPTY.equals(this.mediaItem)) {
                bundle.putBundle(FIELD_MEDIA_ITEM, this.mediaItem.toBundle());
            }
            long j = this.presentationStartTimeMs;
            if (j != -9223372036854775807L) {
                bundle.putLong(FIELD_PRESENTATION_START_TIME_MS, j);
            }
            long j2 = this.windowStartTimeMs;
            if (j2 != -9223372036854775807L) {
                bundle.putLong(FIELD_WINDOW_START_TIME_MS, j2);
            }
            long j3 = this.elapsedRealtimeEpochOffsetMs;
            if (j3 != -9223372036854775807L) {
                bundle.putLong(FIELD_ELAPSED_REALTIME_EPOCH_OFFSET_MS, j3);
            }
            boolean z = this.isSeekable;
            if (z) {
                bundle.putBoolean(FIELD_IS_SEEKABLE, z);
            }
            boolean z2 = this.isDynamic;
            if (z2) {
                bundle.putBoolean(FIELD_IS_DYNAMIC, z2);
            }
            MediaItem.LiveConfiguration liveConfiguration = this.liveConfiguration;
            if (liveConfiguration != null) {
                bundle.putBundle(FIELD_LIVE_CONFIGURATION, liveConfiguration.toBundle());
            }
            boolean z3 = this.isPlaceholder;
            if (z3) {
                bundle.putBoolean(FIELD_IS_PLACEHOLDER, z3);
            }
            long j4 = this.defaultPositionUs;
            if (j4 != 0) {
                bundle.putLong(FIELD_DEFAULT_POSITION_US, j4);
            }
            long j5 = this.durationUs;
            if (j5 != -9223372036854775807L) {
                bundle.putLong(FIELD_DURATION_US, j5);
            }
            int i = this.firstPeriodIndex;
            if (i != 0) {
                bundle.putInt(FIELD_FIRST_PERIOD_INDEX, i);
            }
            int i2 = this.lastPeriodIndex;
            if (i2 != 0) {
                bundle.putInt(FIELD_LAST_PERIOD_INDEX, i2);
            }
            long j6 = this.positionInFirstPeriodUs;
            if (j6 != 0) {
                bundle.putLong(FIELD_POSITION_IN_FIRST_PERIOD_US, j6);
            }
            return bundle;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Window fromBundle(Bundle bundle) {
            Bundle bundle2 = bundle.getBundle(FIELD_MEDIA_ITEM);
            MediaItem fromBundle = bundle2 != null ? MediaItem.CREATOR.fromBundle(bundle2) : MediaItem.EMPTY;
            long j = bundle.getLong(FIELD_PRESENTATION_START_TIME_MS, -9223372036854775807L);
            long j2 = bundle.getLong(FIELD_WINDOW_START_TIME_MS, -9223372036854775807L);
            long j3 = bundle.getLong(FIELD_ELAPSED_REALTIME_EPOCH_OFFSET_MS, -9223372036854775807L);
            boolean z = bundle.getBoolean(FIELD_IS_SEEKABLE, false);
            boolean z2 = bundle.getBoolean(FIELD_IS_DYNAMIC, false);
            Bundle bundle3 = bundle.getBundle(FIELD_LIVE_CONFIGURATION);
            MediaItem.LiveConfiguration fromBundle2 = bundle3 != null ? MediaItem.LiveConfiguration.CREATOR.fromBundle(bundle3) : null;
            boolean z3 = bundle.getBoolean(FIELD_IS_PLACEHOLDER, false);
            long j4 = bundle.getLong(FIELD_DEFAULT_POSITION_US, 0L);
            long j5 = bundle.getLong(FIELD_DURATION_US, -9223372036854775807L);
            int i = bundle.getInt(FIELD_FIRST_PERIOD_INDEX, 0);
            int i2 = bundle.getInt(FIELD_LAST_PERIOD_INDEX, 0);
            long j6 = bundle.getLong(FIELD_POSITION_IN_FIRST_PERIOD_US, 0L);
            Window window = new Window();
            window.set(FAKE_WINDOW_UID, fromBundle, null, j, j2, j3, z, z2, fromBundle2, j4, j5, i, i2, j6);
            window.isPlaceholder = z3;
            return window;
        }
    }

    /* loaded from: classes.dex */
    public static final class Period implements Bundleable {
        private AdPlaybackState adPlaybackState = AdPlaybackState.NONE;
        public long durationUs;
        public Object id;
        public boolean isPlaceholder;
        public long positionInWindowUs;
        public Object uid;
        public int windowIndex;
        private static final String FIELD_WINDOW_INDEX = Util.intToStringMaxRadix(0);
        private static final String FIELD_DURATION_US = Util.intToStringMaxRadix(1);
        private static final String FIELD_POSITION_IN_WINDOW_US = Util.intToStringMaxRadix(2);
        private static final String FIELD_PLACEHOLDER = Util.intToStringMaxRadix(3);
        private static final String FIELD_AD_PLAYBACK_STATE = Util.intToStringMaxRadix(4);
        public static final Bundleable.Creator<Period> CREATOR = Timeline$Period$$ExternalSyntheticLambda0.INSTANCE;

        public Period set(Object obj, Object obj2, int i, long j, long j2) {
            return set(obj, obj2, i, j, j2, AdPlaybackState.NONE, false);
        }

        public Period set(Object obj, Object obj2, int i, long j, long j2, AdPlaybackState adPlaybackState, boolean z) {
            this.id = obj;
            this.uid = obj2;
            this.windowIndex = i;
            this.durationUs = j;
            this.positionInWindowUs = j2;
            this.adPlaybackState = adPlaybackState;
            this.isPlaceholder = z;
            return this;
        }

        public long getDurationUs() {
            return this.durationUs;
        }

        public long getPositionInWindowMs() {
            return Util.usToMs(this.positionInWindowUs);
        }

        public long getPositionInWindowUs() {
            return this.positionInWindowUs;
        }

        public int getAdGroupCount() {
            return this.adPlaybackState.adGroupCount;
        }

        public int getRemovedAdGroupCount() {
            return this.adPlaybackState.removedAdGroupCount;
        }

        public long getAdGroupTimeUs(int i) {
            return this.adPlaybackState.getAdGroup(i).timeUs;
        }

        public int getFirstAdIndexToPlay(int i) {
            return this.adPlaybackState.getAdGroup(i).getFirstAdIndexToPlay();
        }

        public int getNextAdIndexToPlay(int i, int i2) {
            return this.adPlaybackState.getAdGroup(i).getNextAdIndexToPlay(i2);
        }

        public boolean hasPlayedAdGroup(int i) {
            return !this.adPlaybackState.getAdGroup(i).hasUnplayedAds();
        }

        public int getAdGroupIndexForPositionUs(long j) {
            return this.adPlaybackState.getAdGroupIndexForPositionUs(j, this.durationUs);
        }

        public int getAdGroupIndexAfterPositionUs(long j) {
            return this.adPlaybackState.getAdGroupIndexAfterPositionUs(j, this.durationUs);
        }

        public int getAdCountInAdGroup(int i) {
            return this.adPlaybackState.getAdGroup(i).count;
        }

        public long getAdDurationUs(int i, int i2) {
            AdPlaybackState.AdGroup adGroup = this.adPlaybackState.getAdGroup(i);
            if (adGroup.count != -1) {
                return adGroup.durationsUs[i2];
            }
            return -9223372036854775807L;
        }

        public int getAdState(int i, int i2) {
            AdPlaybackState.AdGroup adGroup = this.adPlaybackState.getAdGroup(i);
            if (adGroup.count != -1) {
                return adGroup.states[i2];
            }
            return 0;
        }

        public long getAdResumePositionUs() {
            return this.adPlaybackState.adResumePositionUs;
        }

        public boolean isServerSideInsertedAdGroup(int i) {
            return this.adPlaybackState.getAdGroup(i).isServerSideInserted;
        }

        public long getContentResumeOffsetUs(int i) {
            return this.adPlaybackState.getAdGroup(i).contentResumeOffsetUs;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !Period.class.equals(obj.getClass())) {
                return false;
            }
            Period period = (Period) obj;
            return Util.areEqual(this.id, period.id) && Util.areEqual(this.uid, period.uid) && this.windowIndex == period.windowIndex && this.durationUs == period.durationUs && this.positionInWindowUs == period.positionInWindowUs && this.isPlaceholder == period.isPlaceholder && Util.areEqual(this.adPlaybackState, period.adPlaybackState);
        }

        public int hashCode() {
            Object obj = this.id;
            int hashCode = (217 + (obj == null ? 0 : obj.hashCode())) * 31;
            Object obj2 = this.uid;
            int hashCode2 = obj2 != null ? obj2.hashCode() : 0;
            long j = this.durationUs;
            long j2 = this.positionInWindowUs;
            return ((((((((((hashCode + hashCode2) * 31) + this.windowIndex) * 31) + ((int) (j ^ (j >>> 32)))) * 31) + ((int) (j2 ^ (j2 >>> 32)))) * 31) + (this.isPlaceholder ? 1 : 0)) * 31) + this.adPlaybackState.hashCode();
        }

        @Override // com.google.android.exoplayer2.Bundleable
        public Bundle toBundle() {
            Bundle bundle = new Bundle();
            int i = this.windowIndex;
            if (i != 0) {
                bundle.putInt(FIELD_WINDOW_INDEX, i);
            }
            long j = this.durationUs;
            if (j != -9223372036854775807L) {
                bundle.putLong(FIELD_DURATION_US, j);
            }
            long j2 = this.positionInWindowUs;
            if (j2 != 0) {
                bundle.putLong(FIELD_POSITION_IN_WINDOW_US, j2);
            }
            boolean z = this.isPlaceholder;
            if (z) {
                bundle.putBoolean(FIELD_PLACEHOLDER, z);
            }
            if (!this.adPlaybackState.equals(AdPlaybackState.NONE)) {
                bundle.putBundle(FIELD_AD_PLAYBACK_STATE, this.adPlaybackState.toBundle());
            }
            return bundle;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Period fromBundle(Bundle bundle) {
            AdPlaybackState adPlaybackState;
            int i = bundle.getInt(FIELD_WINDOW_INDEX, 0);
            long j = bundle.getLong(FIELD_DURATION_US, -9223372036854775807L);
            long j2 = bundle.getLong(FIELD_POSITION_IN_WINDOW_US, 0L);
            boolean z = bundle.getBoolean(FIELD_PLACEHOLDER, false);
            Bundle bundle2 = bundle.getBundle(FIELD_AD_PLAYBACK_STATE);
            if (bundle2 != null) {
                adPlaybackState = AdPlaybackState.CREATOR.fromBundle(bundle2);
            } else {
                adPlaybackState = AdPlaybackState.NONE;
            }
            AdPlaybackState adPlaybackState2 = adPlaybackState;
            Period period = new Period();
            period.set(null, null, i, j, j2, adPlaybackState2, z);
            return period;
        }
    }

    public final boolean isEmpty() {
        return getWindowCount() == 0;
    }

    public int getNextWindowIndex(int i, int i2, boolean z) {
        if (i2 == 0) {
            if (i == getLastWindowIndex(z)) {
                return -1;
            }
            return i + 1;
        } else if (i2 != 1) {
            if (i2 == 2) {
                return i == getLastWindowIndex(z) ? getFirstWindowIndex(z) : i + 1;
            }
            throw new IllegalStateException();
        } else {
            return i;
        }
    }

    public int getPreviousWindowIndex(int i, int i2, boolean z) {
        if (i2 == 0) {
            if (i == getFirstWindowIndex(z)) {
                return -1;
            }
            return i - 1;
        } else if (i2 != 1) {
            if (i2 == 2) {
                return i == getFirstWindowIndex(z) ? getLastWindowIndex(z) : i - 1;
            }
            throw new IllegalStateException();
        } else {
            return i;
        }
    }

    public int getLastWindowIndex(boolean z) {
        if (isEmpty()) {
            return -1;
        }
        return getWindowCount() - 1;
    }

    public int getFirstWindowIndex(boolean z) {
        return isEmpty() ? -1 : 0;
    }

    public final Window getWindow(int i, Window window) {
        return getWindow(i, window, 0L);
    }

    public final int getNextPeriodIndex(int i, Period period, Window window, int i2, boolean z) {
        int i3 = getPeriod(i, period).windowIndex;
        if (getWindow(i3, window).lastPeriodIndex == i) {
            int nextWindowIndex = getNextWindowIndex(i3, i2, z);
            if (nextWindowIndex == -1) {
                return -1;
            }
            return getWindow(nextWindowIndex, window).firstPeriodIndex;
        }
        return i + 1;
    }

    public final boolean isLastPeriod(int i, Period period, Window window, int i2, boolean z) {
        return getNextPeriodIndex(i, period, window, i2, z) == -1;
    }

    public final Pair<Object, Long> getPeriodPositionUs(Window window, Period period, int i, long j) {
        return (Pair) Assertions.checkNotNull(getPeriodPositionUs(window, period, i, j, 0L));
    }

    public final Pair<Object, Long> getPeriodPositionUs(Window window, Period period, int i, long j, long j2) {
        Assertions.checkIndex(i, 0, getWindowCount());
        getWindow(i, window, j2);
        if (j == -9223372036854775807L) {
            j = window.getDefaultPositionUs();
            if (j == -9223372036854775807L) {
                return null;
            }
        }
        int i2 = window.firstPeriodIndex;
        getPeriod(i2, period);
        while (i2 < window.lastPeriodIndex && period.positionInWindowUs != j) {
            int i3 = i2 + 1;
            if (getPeriod(i3, period).positionInWindowUs > j) {
                break;
            }
            i2 = i3;
        }
        getPeriod(i2, period, true);
        long j3 = j - period.positionInWindowUs;
        long j4 = period.durationUs;
        if (j4 != -9223372036854775807L) {
            j3 = Math.min(j3, j4 - 1);
        }
        return Pair.create(Assertions.checkNotNull(period.uid), Long.valueOf(Math.max(0L, j3)));
    }

    public Period getPeriodByUid(Object obj, Period period) {
        return getPeriod(getIndexOfPeriod(obj), period, true);
    }

    public final Period getPeriod(int i, Period period) {
        return getPeriod(i, period, false);
    }

    public boolean equals(Object obj) {
        int lastWindowIndex;
        if (this == obj) {
            return true;
        }
        if (obj instanceof Timeline) {
            Timeline timeline = (Timeline) obj;
            if (timeline.getWindowCount() == getWindowCount() && timeline.getPeriodCount() == getPeriodCount()) {
                Window window = new Window();
                Period period = new Period();
                Window window2 = new Window();
                Period period2 = new Period();
                for (int i = 0; i < getWindowCount(); i++) {
                    if (!getWindow(i, window).equals(timeline.getWindow(i, window2))) {
                        return false;
                    }
                }
                for (int i2 = 0; i2 < getPeriodCount(); i2++) {
                    if (!getPeriod(i2, period, true).equals(timeline.getPeriod(i2, period2, true))) {
                        return false;
                    }
                }
                int firstWindowIndex = getFirstWindowIndex(true);
                if (firstWindowIndex == timeline.getFirstWindowIndex(true) && (lastWindowIndex = getLastWindowIndex(true)) == timeline.getLastWindowIndex(true)) {
                    while (firstWindowIndex != lastWindowIndex) {
                        int nextWindowIndex = getNextWindowIndex(firstWindowIndex, 0, true);
                        if (nextWindowIndex != timeline.getNextWindowIndex(firstWindowIndex, 0, true)) {
                            return false;
                        }
                        firstWindowIndex = nextWindowIndex;
                    }
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        Window window = new Window();
        Period period = new Period();
        int windowCount = 217 + getWindowCount();
        for (int i = 0; i < getWindowCount(); i++) {
            windowCount = (windowCount * 31) + getWindow(i, window).hashCode();
        }
        int periodCount = (windowCount * 31) + getPeriodCount();
        for (int i2 = 0; i2 < getPeriodCount(); i2++) {
            periodCount = (periodCount * 31) + getPeriod(i2, period, true).hashCode();
        }
        int firstWindowIndex = getFirstWindowIndex(true);
        while (firstWindowIndex != -1) {
            periodCount = (periodCount * 31) + firstWindowIndex;
            firstWindowIndex = getNextWindowIndex(firstWindowIndex, 0, true);
        }
        return periodCount;
    }

    @Override // com.google.android.exoplayer2.Bundleable
    public final Bundle toBundle() {
        ArrayList arrayList = new ArrayList();
        int windowCount = getWindowCount();
        Window window = new Window();
        for (int i = 0; i < windowCount; i++) {
            arrayList.add(getWindow(i, window, 0L).toBundle());
        }
        ArrayList arrayList2 = new ArrayList();
        int periodCount = getPeriodCount();
        Period period = new Period();
        for (int i2 = 0; i2 < periodCount; i2++) {
            arrayList2.add(getPeriod(i2, period, false).toBundle());
        }
        int[] iArr = new int[windowCount];
        if (windowCount > 0) {
            iArr[0] = getFirstWindowIndex(true);
        }
        for (int i3 = 1; i3 < windowCount; i3++) {
            iArr[i3] = getNextWindowIndex(iArr[i3 - 1], 0, true);
        }
        Bundle bundle = new Bundle();
        BundleUtil.putBinder(bundle, FIELD_WINDOWS, new BundleListRetriever(arrayList));
        BundleUtil.putBinder(bundle, FIELD_PERIODS, new BundleListRetriever(arrayList2));
        bundle.putIntArray(FIELD_SHUFFLED_WINDOW_INDICES, iArr);
        return bundle;
    }
}
