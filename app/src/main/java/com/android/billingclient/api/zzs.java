package com.android.billingclient.api;

import java.util.concurrent.Callable;
/* compiled from: com.android.billingclient:billing@@5.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzs implements Callable {
    public final /* synthetic */ BillingClientImpl zza;
    public final /* synthetic */ QueryProductDetailsParams zzb;
    public final /* synthetic */ ProductDetailsResponseListener zzc;

    public /* synthetic */ zzs(BillingClientImpl billingClientImpl, QueryProductDetailsParams queryProductDetailsParams, ProductDetailsResponseListener productDetailsResponseListener) {
        this.zza = billingClientImpl;
        this.zzb = queryProductDetailsParams;
        this.zzc = productDetailsResponseListener;
    }

    @Override // java.util.concurrent.Callable
    public final Object call() {
        this.zza.zzm(this.zzb, this.zzc);
        return null;
    }
}
