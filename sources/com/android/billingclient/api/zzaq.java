package com.android.billingclient.api;

import com.google.android.gms.internal.play_billing.zzfa;
import com.google.android.gms.internal.play_billing.zzfb;
import com.google.android.gms.internal.play_billing.zzfe;
import com.google.android.gms.internal.play_billing.zzff;
import com.google.android.gms.internal.play_billing.zzfh;
import com.google.android.gms.internal.play_billing.zzfj;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzaq {
    public static zzfb zza(int i, int i2, BillingResult billingResult) {
        zzfa zzv = zzfb.zzv();
        zzfh zzv2 = zzfj.zzv();
        zzv2.zzj(billingResult.getResponseCode());
        zzv2.zzi(billingResult.getDebugMessage());
        zzv2.zzk(i);
        zzv.zzi(zzv2);
        zzv.zzk(i2);
        return (zzfb) zzv.zzc();
    }

    public static zzff zzb(int i) {
        zzfe zzv = zzff.zzv();
        zzv.zzj(i);
        return (zzff) zzv.zzc();
    }
}
