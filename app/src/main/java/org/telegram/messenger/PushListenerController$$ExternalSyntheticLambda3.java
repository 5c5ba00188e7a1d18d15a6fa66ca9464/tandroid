package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$TL_updates;
/* loaded from: classes.dex */
public final /* synthetic */ class PushListenerController$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ TLRPC$TL_updates f$1;

    public /* synthetic */ PushListenerController$$ExternalSyntheticLambda3(int i, TLRPC$TL_updates tLRPC$TL_updates) {
        this.f$0 = i;
        this.f$1 = tLRPC$TL_updates;
    }

    @Override // java.lang.Runnable
    public final void run() {
        PushListenerController.lambda$processRemoteMessage$4(this.f$0, this.f$1);
    }
}
