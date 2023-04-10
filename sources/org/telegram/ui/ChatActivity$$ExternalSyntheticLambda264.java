package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda264 implements RequestDelegate {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda264 INSTANCE = new ChatActivity$$ExternalSyntheticLambda264();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda264() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        ChatActivity.lambda$markSponsoredAsRead$269(tLObject, tLRPC$TL_error);
    }
}
