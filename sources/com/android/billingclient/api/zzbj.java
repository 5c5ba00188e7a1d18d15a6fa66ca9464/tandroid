package com.android.billingclient.api;

import java.util.List;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class zzbj {
    private final List zza;
    private final BillingResult zzb;

    public zzbj(BillingResult billingResult, List list) {
        this.zza = list;
        this.zzb = billingResult;
    }

    public final BillingResult zza() {
        return this.zzb;
    }

    public final List zzb() {
        return this.zza;
    }
}
