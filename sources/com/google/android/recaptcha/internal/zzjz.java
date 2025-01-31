package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
final class zzjz {
    zzjz() {
    }

    public static final boolean zza(Object obj) {
        return !((zzjy) obj).zze();
    }

    public static final Object zzb(Object obj, Object obj2) {
        zzjy zzjyVar = (zzjy) obj;
        zzjy zzjyVar2 = (zzjy) obj2;
        if (!zzjyVar2.isEmpty()) {
            if (!zzjyVar.zze()) {
                zzjyVar = zzjyVar.zzb();
            }
            zzjyVar.zzd(zzjyVar2);
        }
        return zzjyVar;
    }
}
