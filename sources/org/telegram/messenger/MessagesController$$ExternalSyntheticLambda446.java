package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda446 implements RequestDelegate {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda446 INSTANCE = new MessagesController$$ExternalSyntheticLambda446();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda446() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MessagesController.lambda$reportSpam$70(tLObject, tLRPC$TL_error);
    }
}
