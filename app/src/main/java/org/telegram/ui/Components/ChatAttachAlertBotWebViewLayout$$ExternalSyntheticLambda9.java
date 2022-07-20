package org.telegram.ui.Components;

import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda9 implements Runnable {
    public final /* synthetic */ ChatAttachAlertBotWebViewLayout f$0;
    public final /* synthetic */ TLObject f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda9(ChatAttachAlertBotWebViewLayout chatAttachAlertBotWebViewLayout, TLObject tLObject, int i) {
        this.f$0 = chatAttachAlertBotWebViewLayout;
        this.f$1 = tLObject;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$requestWebView$12(this.f$1, this.f$2);
    }
}
