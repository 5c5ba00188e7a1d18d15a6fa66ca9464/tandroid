package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class WebviewActivity$$ExternalSyntheticLambda1 implements RequestDelegate {
    public final /* synthetic */ WebviewActivity f$0;

    public /* synthetic */ WebviewActivity$$ExternalSyntheticLambda1(WebviewActivity webviewActivity) {
        this.f$0 = webviewActivity;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$reloadStats$1(tLObject, tLRPC$TL_error);
    }
}
