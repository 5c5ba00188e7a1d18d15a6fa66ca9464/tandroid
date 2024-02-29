package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda443 implements RequestDelegate {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda443 INSTANCE = new MessagesController$$ExternalSyntheticLambda443();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda443() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MessagesController.lambda$deleteParticipantFromChat$282(tLObject, tLRPC$TL_error);
    }
}
