package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import java.util.Comparator;
import java.util.List;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultTrackSelector$$ExternalSyntheticLambda8 implements Comparator {
    public static final /* synthetic */ DefaultTrackSelector$$ExternalSyntheticLambda8 INSTANCE = new DefaultTrackSelector$$ExternalSyntheticLambda8();

    private /* synthetic */ DefaultTrackSelector$$ExternalSyntheticLambda8() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        return DefaultTrackSelector.VideoTrackInfo.compareSelections((List) obj, (List) obj2);
    }
}
