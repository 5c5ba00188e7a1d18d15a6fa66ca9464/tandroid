package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$TL_chatOnlines;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda88 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ TLRPC$TL_chatOnlines f$2;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda88(MessagesController messagesController, long j, TLRPC$TL_chatOnlines tLRPC$TL_chatOnlines) {
        this.f$0 = messagesController;
        this.f$1 = j;
        this.f$2 = tLRPC$TL_chatOnlines;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateTimerProc$129(this.f$1, this.f$2);
    }
}
