package com.android.billingclient.api;

import android.app.Activity;
import android.content.Context;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public abstract class BillingClient {

    /* compiled from: com.android.billingclient:billing@@6.0.1 */
    /* loaded from: classes.dex */
    public static final class Builder {
        private volatile zzbe zzb;
        private final Context zzc;
        private volatile PurchasesUpdatedListener zzd;

        /* synthetic */ Builder(Context context, zzi zziVar) {
            this.zzc = context;
        }

        public BillingClient build() {
            if (this.zzc != null) {
                if (this.zzd != null) {
                    if (this.zzb != null) {
                        PurchasesUpdatedListener purchasesUpdatedListener = this.zzd;
                        if (this.zzd != null) {
                            return new BillingClientImpl(null, this.zzb, this.zzc, this.zzd, null, null);
                        }
                        return new BillingClientImpl(null, this.zzb, this.zzc, null, null);
                    }
                    throw new IllegalArgumentException("Pending purchases for one-time products must be supported.");
                }
                throw new IllegalArgumentException("Please provide a valid listener for purchases updates.");
            }
            throw new IllegalArgumentException("Please provide a valid Context.");
        }

        public Builder enablePendingPurchases() {
            zzbc zzbcVar = new zzbc(null);
            zzbcVar.zza();
            this.zzb = zzbcVar.zzb();
            return this;
        }

        public Builder setListener(PurchasesUpdatedListener purchasesUpdatedListener) {
            this.zzd = purchasesUpdatedListener;
            return this;
        }
    }

    public static Builder newBuilder(Context context) {
        return new Builder(context, null);
    }

    public abstract void consumeAsync(ConsumeParams consumeParams, ConsumeResponseListener consumeResponseListener);

    public abstract boolean isReady();

    public abstract BillingResult launchBillingFlow(Activity activity, BillingFlowParams billingFlowParams);

    public abstract void queryProductDetailsAsync(QueryProductDetailsParams queryProductDetailsParams, ProductDetailsResponseListener productDetailsResponseListener);

    public abstract void queryPurchasesAsync(QueryPurchasesParams queryPurchasesParams, PurchasesResponseListener purchasesResponseListener);

    public abstract void startConnection(BillingClientStateListener billingClientStateListener);
}
