package org.telegram.ui.Components.Premium;

import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetailsResponseListener;
import java.util.List;
/* loaded from: classes3.dex */
public final /* synthetic */ class GiftPremiumBottomSheet$$ExternalSyntheticLambda3 implements ProductDetailsResponseListener {
    public final /* synthetic */ GiftPremiumBottomSheet f$0;

    public /* synthetic */ GiftPremiumBottomSheet$$ExternalSyntheticLambda3(GiftPremiumBottomSheet giftPremiumBottomSheet) {
        this.f$0 = giftPremiumBottomSheet;
    }

    @Override // com.android.billingclient.api.ProductDetailsResponseListener
    public final void onProductDetailsResponse(BillingResult billingResult, List list) {
        this.f$0.lambda$new$1(billingResult, list);
    }
}
