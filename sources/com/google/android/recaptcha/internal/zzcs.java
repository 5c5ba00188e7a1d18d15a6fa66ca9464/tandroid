package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zzcs implements zzdd {
    public static final zzcs zza = new zzcs();

    private zzcs() {
    }

    @Override // com.google.android.recaptcha.internal.zzdd
    public final void zza(int i, zzcj zzcjVar, zzpq... zzpqVarArr) {
        boolean z = true;
        if (zzpqVarArr.length != 1) {
            throw new zzae(4, 3, null);
        }
        Object zza2 = zzcjVar.zzc().zza(zzpqVarArr[0]);
        if (true != (zza2 instanceof Object)) {
            zza2 = null;
        }
        if (zza2 == null) {
            throw new zzae(4, 5, null);
        }
        try {
            try {
                if (zza2 instanceof String) {
                    zza2 = zzcjVar.zzh().zza((String) zza2);
                }
                zzck zzc = zzcjVar.zzc();
                try {
                    zzci.zza(zza2);
                } catch (zzae e) {
                    if (e.zzb() == 8 || e.zzb() == 6) {
                        z = false;
                    } else if (e.zzb() != 47) {
                        throw e;
                    }
                }
                zzc.zzf(i, Boolean.valueOf(z));
            } catch (Exception e2) {
                throw new zzae(6, 8, e2);
            }
        } catch (zzae e3) {
            throw e3;
        }
    }
}
