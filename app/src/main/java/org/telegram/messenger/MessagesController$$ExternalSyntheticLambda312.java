package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda312 implements RequestDelegate {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ Long f$1;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda312(MessagesController messagesController, Long l) {
        this.f$0 = messagesController;
        this.f$1 = l;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$loadChannelParticipants$119(this.f$1, tLObject, tLRPC$TL_error);
    }
}
