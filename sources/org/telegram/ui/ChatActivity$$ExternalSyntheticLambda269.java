package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda269 implements RequestDelegate {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda269 INSTANCE = new ChatActivity$$ExternalSyntheticLambda269();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda269() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        ChatActivity.lambda$markSponsoredAsRead$274(tLObject, tLRPC$TL_error);
    }
}
