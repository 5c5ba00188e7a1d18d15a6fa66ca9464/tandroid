package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$TL_messages_chatFull;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda90 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ TLRPC$TL_messages_chatFull f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda90(MessagesController messagesController, long j, TLRPC$TL_messages_chatFull tLRPC$TL_messages_chatFull, int i) {
        this.f$0 = messagesController;
        this.f$1 = j;
        this.f$2 = tLRPC$TL_messages_chatFull;
        this.f$3 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadFullChat$46(this.f$1, this.f$2, this.f$3);
    }
}
