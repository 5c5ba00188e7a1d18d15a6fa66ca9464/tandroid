package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$InputPeer;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda106 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ TLRPC$InputPeer f$2;
    public final /* synthetic */ long f$3;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda106(MessagesStorage messagesStorage, long j, TLRPC$InputPeer tLRPC$InputPeer, long j2) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
        this.f$2 = tLRPC$InputPeer;
        this.f$3 = j2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadPendingTasks$19(this.f$1, this.f$2, this.f$3);
    }
}
