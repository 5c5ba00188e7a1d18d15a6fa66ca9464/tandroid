package org.telegram.messenger;

import org.telegram.messenger.SendMessagesHelper;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Message;
/* loaded from: classes.dex */
public final /* synthetic */ class SendMessagesHelper$$ExternalSyntheticLambda55 implements Runnable {
    public final /* synthetic */ SendMessagesHelper f$0;
    public final /* synthetic */ TLRPC$Message f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ TLObject f$3;
    public final /* synthetic */ SendMessagesHelper.DelayedMessage f$4;

    public /* synthetic */ SendMessagesHelper$$ExternalSyntheticLambda55(SendMessagesHelper sendMessagesHelper, TLRPC$Message tLRPC$Message, boolean z, TLObject tLObject, SendMessagesHelper.DelayedMessage delayedMessage) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = tLRPC$Message;
        this.f$2 = z;
        this.f$3 = tLObject;
        this.f$4 = delayedMessage;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$performSendMessageRequest$47(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
