package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class BotWebViewMenuContainer$$ExternalSyntheticLambda17 implements RequestDelegate {
    public final /* synthetic */ BotWebViewMenuContainer f$0;

    public /* synthetic */ BotWebViewMenuContainer$$ExternalSyntheticLambda17(BotWebViewMenuContainer botWebViewMenuContainer) {
        this.f$0 = botWebViewMenuContainer;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$new$3(tLObject, tLRPC$TL_error);
    }
}
