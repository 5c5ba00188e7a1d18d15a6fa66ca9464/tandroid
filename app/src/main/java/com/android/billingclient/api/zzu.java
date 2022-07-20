package com.android.billingclient.api;

import java.util.concurrent.Callable;
/* compiled from: com.android.billingclient:billing@@5.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzu implements Callable {
    public final /* synthetic */ BillingClientImpl zza;
    public final /* synthetic */ ConsumeParams zzb;
    public final /* synthetic */ ConsumeResponseListener zzc;

    public /* synthetic */ zzu(BillingClientImpl billingClientImpl, ConsumeParams consumeParams, ConsumeResponseListener consumeResponseListener) {
        this.zza = billingClientImpl;
        this.zzb = consumeParams;
        this.zzc = consumeResponseListener;
    }

    @Override // java.util.concurrent.Callable
    public final Object call() {
        this.zza.zzl(this.zzb, this.zzc);
        return null;
    }
}
