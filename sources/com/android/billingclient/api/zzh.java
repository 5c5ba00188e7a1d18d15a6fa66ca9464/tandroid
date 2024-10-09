package com.android.billingclient.api;

import android.content.Context;
import android.content.IntentFilter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzh {
    private final Context zza;
    private final zzg zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzh(Context context, PurchasesUpdatedListener purchasesUpdatedListener, AlternativeBillingListener alternativeBillingListener, zzar zzarVar) {
        this.zza = context;
        this.zzb = new zzg(this, purchasesUpdatedListener, alternativeBillingListener, zzarVar, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public zzh(Context context, zzaz zzazVar, zzar zzarVar) {
        this.zza = context;
        this.zzb = new zzg(this, null, zzarVar, 0 == true ? 1 : 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzaz zzb() {
        zzg.zza(this.zzb);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final PurchasesUpdatedListener zzc() {
        return zzg.zzb(this.zzb);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zze() {
        IntentFilter intentFilter = new IntentFilter("com.android.vending.billing.PURCHASES_UPDATED");
        intentFilter.addAction("com.android.vending.billing.ALTERNATIVE_BILLING");
        this.zzb.zzc(this.zza, intentFilter);
    }
}
