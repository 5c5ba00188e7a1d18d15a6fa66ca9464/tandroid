package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class SendMessagesHelper$$ExternalSyntheticLambda12 implements Runnable {
    public final /* synthetic */ AccountInstance f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ SendMessagesHelper$$ExternalSyntheticLambda12(AccountInstance accountInstance, long j, int i) {
        this.f$0 = accountInstance;
        this.f$1 = j;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        SendMessagesHelper.lambda$finishGroup$76(this.f$0, this.f$1, this.f$2);
    }
}
