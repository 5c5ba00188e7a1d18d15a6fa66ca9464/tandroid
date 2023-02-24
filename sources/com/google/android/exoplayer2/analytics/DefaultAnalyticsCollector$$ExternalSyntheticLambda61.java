package com.google.android.exoplayer2.analytics;

import com.google.android.exoplayer2.util.FlagSet;
import com.google.android.exoplayer2.util.ListenerSet;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultAnalyticsCollector$$ExternalSyntheticLambda61 implements ListenerSet.IterationFinishedEvent {
    public static final /* synthetic */ DefaultAnalyticsCollector$$ExternalSyntheticLambda61 INSTANCE = new DefaultAnalyticsCollector$$ExternalSyntheticLambda61();

    private /* synthetic */ DefaultAnalyticsCollector$$ExternalSyntheticLambda61() {
    }

    @Override // com.google.android.exoplayer2.util.ListenerSet.IterationFinishedEvent
    public final void invoke(Object obj, FlagSet flagSet) {
        DefaultAnalyticsCollector.lambda$new$0((AnalyticsListener) obj, flagSet);
    }
}
