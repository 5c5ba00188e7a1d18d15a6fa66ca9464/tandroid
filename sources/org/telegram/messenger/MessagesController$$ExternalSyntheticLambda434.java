package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda434 implements RequestDelegate {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda434 INSTANCE = new MessagesController$$ExternalSyntheticLambda434();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda434() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MessagesController.lambda$processUpdates$341(tLObject, tLRPC$TL_error);
    }
}
