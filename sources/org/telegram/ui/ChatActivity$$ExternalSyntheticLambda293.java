package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda293 implements RequestDelegate {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda293 INSTANCE = new ChatActivity$$ExternalSyntheticLambda293();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda293() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        ChatActivity.lambda$markSponsoredAsRead$293(tLObject, tLRPC$TL_error);
    }
}
