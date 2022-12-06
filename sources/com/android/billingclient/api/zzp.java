package com.android.billingclient.api;

import android.content.Context;
import android.content.IntentFilter;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.android.billingclient:billing@@5.1.0 */
/* loaded from: classes.dex */
public final class zzp {
    private final Context zza;
    private final zzo zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzp(Context context, zzbf zzbfVar) {
        this.zza = context;
        this.zzb = new zzo(this, null, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzbf zzb() {
        zzo.zza(this.zzb);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final PurchasesUpdatedListener zzc() {
        return zzo.zzb(this.zzb);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zze() {
        IntentFilter intentFilter = new IntentFilter("com.android.vending.billing.PURCHASES_UPDATED");
        intentFilter.addAction("com.android.vending.billing.ALTERNATIVE_BILLING");
        this.zzb.zzc(this.zza, intentFilter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzp(Context context, PurchasesUpdatedListener purchasesUpdatedListener, zzc zzcVar) {
        this.zza = context;
        this.zzb = new zzo(this, purchasesUpdatedListener, zzcVar, null);
    }
}
