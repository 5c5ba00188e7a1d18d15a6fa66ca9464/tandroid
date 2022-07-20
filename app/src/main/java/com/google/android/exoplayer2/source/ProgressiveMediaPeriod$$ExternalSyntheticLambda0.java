package com.google.android.exoplayer2.source;
/* loaded from: classes.dex */
public final /* synthetic */ class ProgressiveMediaPeriod$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ProgressiveMediaPeriod f$0;

    public /* synthetic */ ProgressiveMediaPeriod$$ExternalSyntheticLambda0(ProgressiveMediaPeriod progressiveMediaPeriod) {
        this.f$0 = progressiveMediaPeriod;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.maybeFinishPrepare();
    }
}
