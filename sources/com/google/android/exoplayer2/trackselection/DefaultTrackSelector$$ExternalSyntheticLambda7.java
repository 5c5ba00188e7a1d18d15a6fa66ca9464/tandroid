package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import java.util.Comparator;
import java.util.List;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultTrackSelector$$ExternalSyntheticLambda7 implements Comparator {
    public static final /* synthetic */ DefaultTrackSelector$$ExternalSyntheticLambda7 INSTANCE = new DefaultTrackSelector$$ExternalSyntheticLambda7();

    private /* synthetic */ DefaultTrackSelector$$ExternalSyntheticLambda7() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        return DefaultTrackSelector.TextTrackInfo.compareSelections((List) obj, (List) obj2);
    }
}
