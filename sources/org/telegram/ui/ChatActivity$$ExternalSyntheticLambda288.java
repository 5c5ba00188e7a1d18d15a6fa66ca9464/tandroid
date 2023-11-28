package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda288 implements RequestDelegate {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda288 INSTANCE = new ChatActivity$$ExternalSyntheticLambda288();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda288() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        ChatActivity.lambda$markSponsoredAsRead$293(tLObject, tLRPC$TL_error);
    }
}
