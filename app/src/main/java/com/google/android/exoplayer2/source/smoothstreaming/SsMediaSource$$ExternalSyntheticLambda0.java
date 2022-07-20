package com.google.android.exoplayer2.source.smoothstreaming;
/* loaded from: classes.dex */
public final /* synthetic */ class SsMediaSource$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SsMediaSource f$0;

    public /* synthetic */ SsMediaSource$$ExternalSyntheticLambda0(SsMediaSource ssMediaSource) {
        this.f$0 = ssMediaSource;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.startLoadingManifest();
    }
}
