package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class GcmPushListenerService$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ TLRPC$TL_error f$0;

    public /* synthetic */ GcmPushListenerService$$ExternalSyntheticLambda8(TLRPC$TL_error tLRPC$TL_error) {
        this.f$0 = tLRPC$TL_error;
    }

    @Override // java.lang.Runnable
    public final void run() {
        GcmPushListenerService.lambda$sendRegistrationToServer$6(this.f$0);
    }
}
