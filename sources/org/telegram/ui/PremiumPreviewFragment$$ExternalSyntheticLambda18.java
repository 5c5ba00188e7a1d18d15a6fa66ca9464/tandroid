package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class PremiumPreviewFragment$$ExternalSyntheticLambda18 implements RequestDelegate {
    public static final /* synthetic */ PremiumPreviewFragment$$ExternalSyntheticLambda18 INSTANCE = new PremiumPreviewFragment$$ExternalSyntheticLambda18();

    private /* synthetic */ PremiumPreviewFragment$$ExternalSyntheticLambda18() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        PremiumPreviewFragment.lambda$sentShowFeaturePreview$19(tLObject, tLRPC$TL_error);
    }
}
