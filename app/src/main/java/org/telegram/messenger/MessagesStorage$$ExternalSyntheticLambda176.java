package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$User;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda176 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ TLRPC$User f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda176(MessagesStorage messagesStorage, TLRPC$User tLRPC$User, boolean z, int i) {
        this.f$0 = messagesStorage;
        this.f$1 = tLRPC$User;
        this.f$2 = z;
        this.f$3 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadUserInfo$97(this.f$1, this.f$2, this.f$3);
    }
}
