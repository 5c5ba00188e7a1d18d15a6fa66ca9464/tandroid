package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda413 implements RequestDelegate {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda413 INSTANCE = new MessagesController$$ExternalSyntheticLambda413();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda413() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MessagesController.lambda$deleteUserPhoto$107(tLObject, tLRPC$TL_error);
    }
}
