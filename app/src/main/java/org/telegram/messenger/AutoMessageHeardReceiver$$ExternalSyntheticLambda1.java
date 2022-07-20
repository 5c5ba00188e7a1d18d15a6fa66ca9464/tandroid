package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class AutoMessageHeardReceiver$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ AccountInstance f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ AutoMessageHeardReceiver$$ExternalSyntheticLambda1(AccountInstance accountInstance, long j, int i, int i2) {
        this.f$0 = accountInstance;
        this.f$1 = j;
        this.f$2 = i;
        this.f$3 = i2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AutoMessageHeardReceiver.lambda$onReceive$1(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
