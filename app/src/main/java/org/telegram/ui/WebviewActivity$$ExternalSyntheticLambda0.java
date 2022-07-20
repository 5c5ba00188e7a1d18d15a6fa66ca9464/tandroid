package org.telegram.ui;

import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class WebviewActivity$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ WebviewActivity f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ WebviewActivity$$ExternalSyntheticLambda0(WebviewActivity webviewActivity, TLObject tLObject) {
        this.f$0 = webviewActivity;
        this.f$1 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$reloadStats$0(this.f$1);
    }
}
