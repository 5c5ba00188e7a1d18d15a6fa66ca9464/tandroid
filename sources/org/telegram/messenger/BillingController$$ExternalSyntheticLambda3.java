package org.telegram.messenger;

import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetailsResponseListener;
import java.util.List;

/* loaded from: classes3.dex */
public final /* synthetic */ class BillingController$$ExternalSyntheticLambda3 implements ProductDetailsResponseListener {
    public final /* synthetic */ BillingController f$0;

    public /* synthetic */ BillingController$$ExternalSyntheticLambda3(BillingController billingController) {
        this.f$0 = billingController;
    }

    @Override // com.android.billingclient.api.ProductDetailsResponseListener
    public final void onProductDetailsResponse(BillingResult billingResult, List list) {
        this.f$0.onQueriedPremiumProductDetails(billingResult, list);
    }
}
