package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda287 implements RequestDelegate {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda287 INSTANCE = new ChatActivity$$ExternalSyntheticLambda287();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda287() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        ChatActivity.lambda$markSponsoredAsRead$292(tLObject, tLRPC$TL_error);
    }
}
