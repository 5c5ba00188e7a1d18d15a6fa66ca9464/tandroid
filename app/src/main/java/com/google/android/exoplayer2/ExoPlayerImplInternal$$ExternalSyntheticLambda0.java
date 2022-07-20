package com.google.android.exoplayer2;
/* loaded from: classes.dex */
public final /* synthetic */ class ExoPlayerImplInternal$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ExoPlayerImplInternal f$0;
    public final /* synthetic */ PlayerMessage f$1;

    public /* synthetic */ ExoPlayerImplInternal$$ExternalSyntheticLambda0(ExoPlayerImplInternal exoPlayerImplInternal, PlayerMessage playerMessage) {
        this.f$0 = exoPlayerImplInternal;
        this.f$1 = playerMessage;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$sendMessageToTargetThread$0(this.f$1);
    }
}
