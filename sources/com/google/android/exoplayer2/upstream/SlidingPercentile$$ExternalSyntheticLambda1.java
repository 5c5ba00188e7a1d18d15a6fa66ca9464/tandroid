package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.upstream.SlidingPercentile;
import java.util.Comparator;
/* loaded from: classes.dex */
public final /* synthetic */ class SlidingPercentile$$ExternalSyntheticLambda1 implements Comparator {
    public static final /* synthetic */ SlidingPercentile$$ExternalSyntheticLambda1 INSTANCE = new SlidingPercentile$$ExternalSyntheticLambda1();

    private /* synthetic */ SlidingPercentile$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$static$0;
        lambda$static$0 = SlidingPercentile.lambda$static$0((SlidingPercentile.Sample) obj, (SlidingPercentile.Sample) obj2);
        return lambda$static$0;
    }
}
