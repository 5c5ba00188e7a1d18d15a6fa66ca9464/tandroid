package com.android.billingclient.api;

import android.os.Bundle;
import java.util.concurrent.Callable;
/* compiled from: com.android.billingclient:billing@@5.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzab implements Callable {
    public final /* synthetic */ BillingClientImpl zza;
    public final /* synthetic */ int zzb;
    public final /* synthetic */ String zzc;
    public final /* synthetic */ String zzd;
    public final /* synthetic */ BillingFlowParams zze;
    public final /* synthetic */ Bundle zzf;

    public /* synthetic */ zzab(BillingClientImpl billingClientImpl, int i, String str, String str2, BillingFlowParams billingFlowParams, Bundle bundle) {
        this.zza = billingClientImpl;
        this.zzb = i;
        this.zzc = str;
        this.zzd = str2;
        this.zze = billingFlowParams;
        this.zzf = bundle;
    }

    @Override // java.util.concurrent.Callable
    public final Object call() {
        return this.zza.zzc(this.zzb, this.zzc, this.zzd, this.zze, this.zzf);
    }
}
