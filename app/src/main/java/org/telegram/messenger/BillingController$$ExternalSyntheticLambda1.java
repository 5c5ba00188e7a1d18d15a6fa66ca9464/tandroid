package org.telegram.messenger;

import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.PurchasesResponseListener;
import java.util.List;
/* loaded from: classes.dex */
public final /* synthetic */ class BillingController$$ExternalSyntheticLambda1 implements PurchasesResponseListener {
    public final /* synthetic */ BillingController f$0;

    @Override // com.android.billingclient.api.PurchasesResponseListener
    public final void onQueryPurchasesResponse(BillingResult billingResult, List list) {
        this.f$0.onPurchasesUpdated(billingResult, list);
    }
}
