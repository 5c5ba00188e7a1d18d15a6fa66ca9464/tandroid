package org.telegram.ui.Components;

import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda9 implements Runnable {
    public final /* synthetic */ ChatAttachAlertBotWebViewLayout f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;

    public /* synthetic */ ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda9(ChatAttachAlertBotWebViewLayout chatAttachAlertBotWebViewLayout, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0 = chatAttachAlertBotWebViewLayout;
        this.f$1 = tLRPC$TL_error;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$new$0(this.f$1);
    }
}
