package org.telegram.ui;

import org.telegram.tgnet.TLRPC$TL_messages_sendScheduledMessages;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda193 implements Runnable {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ TLRPC$TL_messages_sendScheduledMessages f$1;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda193(ChatActivity chatActivity, TLRPC$TL_messages_sendScheduledMessages tLRPC$TL_messages_sendScheduledMessages) {
        this.f$0 = chatActivity;
        this.f$1 = tLRPC$TL_messages_sendScheduledMessages;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processSelectedOption$213(this.f$1);
    }
}
