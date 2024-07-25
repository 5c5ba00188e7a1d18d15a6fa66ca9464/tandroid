package com.android.billingclient.api;

import java.util.List;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public interface PurchasesUpdatedListener {
    void onPurchasesUpdated(BillingResult billingResult, List<Purchase> list);
}
