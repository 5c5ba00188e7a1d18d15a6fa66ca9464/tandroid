package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda40 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda40(MediaDataController mediaDataController, long j, long j2, int i) {
        this.f$0 = mediaDataController;
        this.f$1 = j;
        this.f$2 = j2;
        this.f$3 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadBotInfo$155(this.f$1, this.f$2, this.f$3);
    }
}
