package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatLinkActivity$$ExternalSyntheticLambda14 implements RequestDelegate {
    public final /* synthetic */ ChatLinkActivity f$0;

    public /* synthetic */ ChatLinkActivity$$ExternalSyntheticLambda14(ChatLinkActivity chatLinkActivity) {
        this.f$0 = chatLinkActivity;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$loadChats$17(tLObject, tLRPC$TL_error);
    }
}
