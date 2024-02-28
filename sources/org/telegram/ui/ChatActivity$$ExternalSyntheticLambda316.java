package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda316 implements RequestDelegate {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda316 INSTANCE = new ChatActivity$$ExternalSyntheticLambda316();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda316() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        ChatActivity.lambda$markSponsoredAsRead$315(tLObject, tLRPC$TL_error);
    }
}
