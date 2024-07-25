package com.android.billingclient.api;

import java.util.List;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public interface PurchasesResponseListener {
    void onQueryPurchasesResponse(BillingResult billingResult, List<Purchase> list);
}
