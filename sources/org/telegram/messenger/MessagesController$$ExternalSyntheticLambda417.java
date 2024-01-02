package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda417 implements RequestDelegate {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda417 INSTANCE = new MessagesController$$ExternalSyntheticLambda417();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda417() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MessagesController.lambda$reportSpam$67(tLObject, tLRPC$TL_error);
    }
}
