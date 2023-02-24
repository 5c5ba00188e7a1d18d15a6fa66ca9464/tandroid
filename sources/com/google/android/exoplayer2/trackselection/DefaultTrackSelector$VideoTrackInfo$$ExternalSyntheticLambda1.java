package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import java.util.Comparator;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultTrackSelector$VideoTrackInfo$$ExternalSyntheticLambda1 implements Comparator {
    public static final /* synthetic */ DefaultTrackSelector$VideoTrackInfo$$ExternalSyntheticLambda1 INSTANCE = new DefaultTrackSelector$VideoTrackInfo$$ExternalSyntheticLambda1();

    private /* synthetic */ DefaultTrackSelector$VideoTrackInfo$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int compareQualityPreferences;
        compareQualityPreferences = DefaultTrackSelector.VideoTrackInfo.compareQualityPreferences((DefaultTrackSelector.VideoTrackInfo) obj, (DefaultTrackSelector.VideoTrackInfo) obj2);
        return compareQualityPreferences;
    }
}
