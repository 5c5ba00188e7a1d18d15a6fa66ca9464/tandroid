package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zzds implements zzdd {
    public static final zzds zza = new zzds();

    private zzds() {
    }

    @Override // com.google.android.recaptcha.internal.zzdd
    public final void zza(int i, zzcj zzcjVar, zzpq... zzpqVarArr) {
        if (zzpqVarArr.length != 1) {
            throw new zzae(4, 3, null);
        }
        Object zza2 = zzcjVar.zzc().zza(zzpqVarArr[0]);
        if (true != (zza2 instanceof String)) {
            zza2 = null;
        }
        String str = (String) zza2;
        if (str == null) {
            throw new zzae(4, 5, null);
        }
        zzcjVar.zzf(str);
    }
}
