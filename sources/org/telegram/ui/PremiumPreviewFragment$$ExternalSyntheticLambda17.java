package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class PremiumPreviewFragment$$ExternalSyntheticLambda17 implements RequestDelegate {
    public static final /* synthetic */ PremiumPreviewFragment$$ExternalSyntheticLambda17 INSTANCE = new PremiumPreviewFragment$$ExternalSyntheticLambda17();

    private /* synthetic */ PremiumPreviewFragment$$ExternalSyntheticLambda17() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        PremiumPreviewFragment.lambda$sentPremiumButtonClick$17(tLObject, tLRPC$TL_error);
    }
}
