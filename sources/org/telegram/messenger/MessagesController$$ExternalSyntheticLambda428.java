package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda428 implements RequestDelegate {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda428 INSTANCE = new MessagesController$$ExternalSyntheticLambda428();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda428() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MessagesController.lambda$markMentionMessageAsRead$218(tLObject, tLRPC$TL_error);
    }
}
