package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda423 implements RequestDelegate {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda423 INSTANCE = new MessagesController$$ExternalSyntheticLambda423();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda423() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MessagesController.lambda$hidePromoDialog$121(tLObject, tLRPC$TL_error);
    }
}
