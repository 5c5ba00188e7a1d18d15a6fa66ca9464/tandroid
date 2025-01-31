package com.google.android.recaptcha.internal;

import java.lang.reflect.Array;

/* loaded from: classes.dex */
public final class zzda implements zzdd {
    public static final zzda zza = new zzda();

    private zzda() {
    }

    @Override // com.google.android.recaptcha.internal.zzdd
    public final void zza(int i, zzcj zzcjVar, zzpq... zzpqVarArr) {
        if (zzpqVarArr.length != 2) {
            throw new zzae(4, 3, null);
        }
        Object zza2 = zzcjVar.zzc().zza(zzpqVarArr[0]);
        if (true != (zza2 instanceof Object)) {
            zza2 = null;
        }
        if (zza2 == null) {
            throw new zzae(4, 5, null);
        }
        Object zza3 = zzcjVar.zzc().zza(zzpqVarArr[1]);
        if (true != (zza3 instanceof Integer)) {
            zza3 = null;
        }
        Integer num = (Integer) zza3;
        if (num == null) {
            throw new zzae(4, 5, null);
        }
        int intValue = num.intValue();
        try {
            if (zza2 instanceof String) {
                zza2 = zzcjVar.zzh().zza((String) zza2);
            }
            zzcjVar.zzc().zzf(i, Array.newInstance((Class<?>) zzci.zza(zza2), intValue));
        } catch (Exception e) {
            throw new zzae(6, 21, e);
        }
    }
}
