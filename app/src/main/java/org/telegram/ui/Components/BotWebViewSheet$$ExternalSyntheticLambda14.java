package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class BotWebViewSheet$$ExternalSyntheticLambda14 implements RequestDelegate {
    public final /* synthetic */ BotWebViewSheet f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ BotWebViewSheet$$ExternalSyntheticLambda14(BotWebViewSheet botWebViewSheet, int i) {
        this.f$0 = botWebViewSheet;
        this.f$1 = i;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$requestWebView$18(this.f$1, tLObject, tLRPC$TL_error);
    }
}
