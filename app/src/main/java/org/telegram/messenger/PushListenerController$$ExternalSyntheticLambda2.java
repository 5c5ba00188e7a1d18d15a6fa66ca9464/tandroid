package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class PushListenerController$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ PushListenerController$$ExternalSyntheticLambda2(int i, int i2, String str) {
        this.f$0 = i;
        this.f$1 = i2;
        this.f$2 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        PushListenerController.lambda$sendRegistrationToServer$2(this.f$0, this.f$1, this.f$2);
    }
}
