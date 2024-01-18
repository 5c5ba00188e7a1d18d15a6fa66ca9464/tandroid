package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda297 implements RequestDelegate {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda297 INSTANCE = new ChatActivity$$ExternalSyntheticLambda297();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda297() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        ChatActivity.lambda$markSponsoredAsRead$296(tLObject, tLRPC$TL_error);
    }
}
