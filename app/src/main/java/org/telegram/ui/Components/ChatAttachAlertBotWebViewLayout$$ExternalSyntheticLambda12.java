package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda12 implements RequestDelegate {
    public final /* synthetic */ ChatAttachAlertBotWebViewLayout f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda12(ChatAttachAlertBotWebViewLayout chatAttachAlertBotWebViewLayout, int i) {
        this.f$0 = chatAttachAlertBotWebViewLayout;
        this.f$1 = i;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$requestWebView$12(this.f$1, tLObject, tLRPC$TL_error);
    }
}
