package com.android.billingclient.api;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public interface BillingClientStateListener {
    void onBillingServiceDisconnected();

    void onBillingSetupFinished(BillingResult billingResult);
}
