package com.android.billingclient.api;

import java.util.concurrent.Callable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class zzy implements Callable {
    final /* synthetic */ String zza;
    final /* synthetic */ PurchasesResponseListener zzb;
    final /* synthetic */ BillingClientImpl zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzy(BillingClientImpl billingClientImpl, String str, PurchasesResponseListener purchasesResponseListener) {
        this.zzc = billingClientImpl;
        this.zza = str;
        this.zzb = purchasesResponseListener;
    }

    @Override // java.util.concurrent.Callable
    public final /* bridge */ /* synthetic */ Object call() throws Exception {
        zzbj zzN = BillingClientImpl.zzN(this.zzc, this.zza, 9);
        if (zzN.zzb() != null) {
            this.zzb.onQueryPurchasesResponse(zzN.zza(), zzN.zzb());
            return null;
        }
        this.zzb.onQueryPurchasesResponse(zzN.zza(), com.google.android.gms.internal.play_billing.zzu.zzk());
        return null;
    }
}
