package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class FingerprintController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ FingerprintController$$ExternalSyntheticLambda1(boolean z) {
        this.f$0 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        FingerprintController.generateNewKey(this.f$0);
    }
}
