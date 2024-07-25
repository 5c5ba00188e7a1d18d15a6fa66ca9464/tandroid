package com.android.billingclient.api;

import android.content.Context;
import android.content.IntentFilter;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class zzh {
    private final Context zza;
    private final zzg zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzh(Context context, zzaz zzazVar, zzar zzarVar) {
        this.zza = context;
        this.zzb = new zzg(this, null, zzarVar, null);
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzh(Context context, PurchasesUpdatedListener purchasesUpdatedListener, AlternativeBillingListener alternativeBillingListener, zzar zzarVar) {
        this.zza = context;
        this.zzb = new zzg(this, purchasesUpdatedListener, alternativeBillingListener, zzarVar, null);
    }
}
