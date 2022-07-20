package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class GcmPushListenerService$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ GcmPushListenerService$$ExternalSyntheticLambda2(int i, String str) {
        this.f$0 = i;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        GcmPushListenerService.lambda$sendRegistrationToServer$8(this.f$0, this.f$1);
    }
}
