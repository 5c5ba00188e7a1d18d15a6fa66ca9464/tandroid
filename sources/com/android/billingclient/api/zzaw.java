package com.android.billingclient.api;

import android.content.Context;
import com.google.android.gms.internal.play_billing.zzb;
import com.google.android.gms.internal.play_billing.zzfb;
import com.google.android.gms.internal.play_billing.zzff;
import com.google.android.gms.internal.play_billing.zzfm;
import com.google.android.gms.internal.play_billing.zzfy;
import com.google.android.gms.internal.play_billing.zzfz;
import com.google.android.gms.internal.play_billing.zzgd;

/* loaded from: classes.dex */
final class zzaw implements zzar {
    private final zzfm zza;
    private final zzay zzb;

    zzaw(Context context, zzfm zzfmVar) {
        this.zzb = new zzay(context);
        this.zza = zzfmVar;
    }

    @Override // com.android.billingclient.api.zzar
    public final void zza(zzfb zzfbVar) {
        try {
            zzfy zzv = zzfz.zzv();
            zzfm zzfmVar = this.zza;
            if (zzfmVar != null) {
                zzv.zzk(zzfmVar);
            }
            zzv.zzi(zzfbVar);
            this.zzb.zza((zzfz) zzv.zzc());
        } catch (Throwable unused) {
            zzb.zzj("BillingLogger", "Unable to log.");
        }
    }

    @Override // com.android.billingclient.api.zzar
    public final void zzb(zzff zzffVar) {
        try {
            zzfy zzv = zzfz.zzv();
            zzfm zzfmVar = this.zza;
            if (zzfmVar != null) {
                zzv.zzk(zzfmVar);
            }
            zzv.zzj(zzffVar);
            this.zzb.zza((zzfz) zzv.zzc());
        } catch (Throwable unused) {
            zzb.zzj("BillingLogger", "Unable to log.");
        }
    }

    @Override // com.android.billingclient.api.zzar
    public final void zzc(zzgd zzgdVar) {
        try {
            zzfy zzv = zzfz.zzv();
            zzfm zzfmVar = this.zza;
            if (zzfmVar != null) {
                zzv.zzk(zzfmVar);
            }
            zzv.zzl(zzgdVar);
            this.zzb.zza((zzfz) zzv.zzc());
        } catch (Throwable unused) {
            zzb.zzj("BillingLogger", "Unable to log.");
        }
    }
}
