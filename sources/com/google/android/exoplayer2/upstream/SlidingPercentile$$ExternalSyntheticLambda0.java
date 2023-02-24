package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.upstream.SlidingPercentile;
import java.util.Comparator;
/* loaded from: classes.dex */
public final /* synthetic */ class SlidingPercentile$$ExternalSyntheticLambda0 implements Comparator {
    public static final /* synthetic */ SlidingPercentile$$ExternalSyntheticLambda0 INSTANCE = new SlidingPercentile$$ExternalSyntheticLambda0();

    private /* synthetic */ SlidingPercentile$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$static$1;
        lambda$static$1 = SlidingPercentile.lambda$static$1((SlidingPercentile.Sample) obj, (SlidingPercentile.Sample) obj2);
        return lambda$static$1;
    }
}
