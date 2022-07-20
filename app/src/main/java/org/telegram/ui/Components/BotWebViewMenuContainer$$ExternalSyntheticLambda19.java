package org.telegram.ui.Components;

import org.telegram.ui.Components.ChatAttachAlertBotWebViewLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class BotWebViewMenuContainer$$ExternalSyntheticLambda19 implements ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer.Delegate {
    public final /* synthetic */ BotWebViewMenuContainer f$0;

    public /* synthetic */ BotWebViewMenuContainer$$ExternalSyntheticLambda19(BotWebViewMenuContainer botWebViewMenuContainer) {
        this.f$0 = botWebViewMenuContainer;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer.Delegate
    public final void onDismiss() {
        this.f$0.dismiss();
    }
}
