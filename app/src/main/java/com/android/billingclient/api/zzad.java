package com.android.billingclient.api;

import com.google.android.gms.internal.play_billing.zzu;
/* compiled from: com.android.billingclient:billing@@5.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzad implements Runnable {
    public final /* synthetic */ PurchasesResponseListener zza;

    @Override // java.lang.Runnable
    public final void run() {
        this.zza.onQueryPurchasesResponse(zzbb.zzn, zzu.zzl());
    }
}
