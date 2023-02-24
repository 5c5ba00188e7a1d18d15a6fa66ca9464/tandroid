package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.Format;
import java.util.Comparator;
/* loaded from: classes.dex */
public final /* synthetic */ class BaseTrackSelection$$ExternalSyntheticLambda0 implements Comparator {
    public static final /* synthetic */ BaseTrackSelection$$ExternalSyntheticLambda0 INSTANCE = new BaseTrackSelection$$ExternalSyntheticLambda0();

    private /* synthetic */ BaseTrackSelection$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$new$0;
        lambda$new$0 = BaseTrackSelection.lambda$new$0((Format) obj, (Format) obj2);
        return lambda$new$0;
    }
}
