package com.android.billingclient.api;

import android.content.Context;
import android.content.IntentFilter;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.android.billingclient:billing@@5.0.0 */
/* loaded from: classes.dex */
public final class zzo {
    private final Context zza;
    private final zzn zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzo(Context context, zzbe zzbeVar) {
        this.zza = context;
        this.zzb = new zzn(this, null, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzbe zzb() {
        zzn.zza(this.zzb);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final PurchasesUpdatedListener zzc() {
        return zzn.zzb(this.zzb);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zze() {
        IntentFilter intentFilter = new IntentFilter("com.android.vending.billing.PURCHASES_UPDATED");
        intentFilter.addAction("com.android.vending.billing.ALTERNATIVE_BILLING");
        this.zzb.zzc(this.zza, intentFilter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzo(Context context, PurchasesUpdatedListener purchasesUpdatedListener, zzc zzcVar) {
        this.zza = context;
        this.zzb = new zzn(this, purchasesUpdatedListener, zzcVar, null);
    }
}
