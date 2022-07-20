package org.telegram.ui;

import java.util.List;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_payments_canPurchasePremium;
import org.telegram.ui.ActionBar.BaseFragment;
/* loaded from: classes3.dex */
public final /* synthetic */ class PremiumPreviewFragment$$ExternalSyntheticLambda10 implements RequestDelegate {
    public final /* synthetic */ BaseFragment f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ TLRPC$TL_payments_canPurchasePremium f$2;

    public /* synthetic */ PremiumPreviewFragment$$ExternalSyntheticLambda10(BaseFragment baseFragment, List list, TLRPC$TL_payments_canPurchasePremium tLRPC$TL_payments_canPurchasePremium) {
        this.f$0 = baseFragment;
        this.f$1 = list;
        this.f$2 = tLRPC$TL_payments_canPurchasePremium;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        PremiumPreviewFragment.lambda$buyPremium$9(this.f$0, this.f$1, this.f$2, tLObject, tLRPC$TL_error);
    }
}
