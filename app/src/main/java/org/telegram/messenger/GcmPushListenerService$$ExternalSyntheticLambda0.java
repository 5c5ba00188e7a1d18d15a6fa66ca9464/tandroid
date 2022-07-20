package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class GcmPushListenerService$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ String f$0;

    public /* synthetic */ GcmPushListenerService$$ExternalSyntheticLambda0(String str) {
        this.f$0 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        GcmPushListenerService.lambda$onNewToken$0(this.f$0);
    }
}
