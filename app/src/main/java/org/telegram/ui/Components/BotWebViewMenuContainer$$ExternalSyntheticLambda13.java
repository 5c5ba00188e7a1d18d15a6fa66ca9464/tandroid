package org.telegram.ui.Components;

import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class BotWebViewMenuContainer$$ExternalSyntheticLambda13 implements Runnable {
    public final /* synthetic */ BotWebViewMenuContainer f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ BotWebViewMenuContainer$$ExternalSyntheticLambda13(BotWebViewMenuContainer botWebViewMenuContainer, TLObject tLObject) {
        this.f$0 = botWebViewMenuContainer;
        this.f$1 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadWebView$14(this.f$1);
    }
}
