package com.google.android.exoplayer2;

import com.google.android.exoplayer2.analytics.DefaultAnalyticsCollector;
import com.google.android.exoplayer2.util.Clock;
import com.google.common.base.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class ExoPlayer$Builder$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ ExoPlayer$Builder$$ExternalSyntheticLambda0 INSTANCE = new ExoPlayer$Builder$$ExternalSyntheticLambda0();

    private /* synthetic */ ExoPlayer$Builder$$ExternalSyntheticLambda0() {
    }

    @Override // com.google.common.base.Function
    public final Object apply(Object obj) {
        return new DefaultAnalyticsCollector((Clock) obj);
    }
}
