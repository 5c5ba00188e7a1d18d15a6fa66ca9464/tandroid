package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$TL_updateNewChannelMessage;
/* loaded from: classes.dex */
public final /* synthetic */ class SendMessagesHelper$$ExternalSyntheticLambda64 implements Runnable {
    public final /* synthetic */ SendMessagesHelper f$0;
    public final /* synthetic */ TLRPC$TL_updateNewChannelMessage f$1;

    public /* synthetic */ SendMessagesHelper$$ExternalSyntheticLambda64(SendMessagesHelper sendMessagesHelper, TLRPC$TL_updateNewChannelMessage tLRPC$TL_updateNewChannelMessage) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = tLRPC$TL_updateNewChannelMessage;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$performSendMessageRequest$53(this.f$1);
    }
}
