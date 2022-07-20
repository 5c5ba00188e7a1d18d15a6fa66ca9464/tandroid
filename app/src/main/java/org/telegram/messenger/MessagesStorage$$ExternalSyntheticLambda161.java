package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$ChatParticipants;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda161 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ TLRPC$ChatParticipants f$1;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda161(MessagesStorage messagesStorage, TLRPC$ChatParticipants tLRPC$ChatParticipants) {
        this.f$0 = messagesStorage;
        this.f$1 = tLRPC$ChatParticipants;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateChatParticipants$91(this.f$1);
    }
}
