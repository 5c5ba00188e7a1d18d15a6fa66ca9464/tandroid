package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import java.util.Comparator;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultTrackSelector$VideoTrackInfo$$ExternalSyntheticLambda0 implements Comparator {
    public static final /* synthetic */ DefaultTrackSelector$VideoTrackInfo$$ExternalSyntheticLambda0 INSTANCE = new DefaultTrackSelector$VideoTrackInfo$$ExternalSyntheticLambda0();

    private /* synthetic */ DefaultTrackSelector$VideoTrackInfo$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int compareNonQualityPreferences;
        compareNonQualityPreferences = DefaultTrackSelector.VideoTrackInfo.compareNonQualityPreferences((DefaultTrackSelector.VideoTrackInfo) obj, (DefaultTrackSelector.VideoTrackInfo) obj2);
        return compareNonQualityPreferences;
    }
}
