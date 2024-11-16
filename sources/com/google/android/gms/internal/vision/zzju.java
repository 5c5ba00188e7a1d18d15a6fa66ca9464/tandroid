package com.google.android.gms.internal.vision;

/* loaded from: classes.dex */
abstract class zzju {
    private static final zzju zza;
    private static final zzju zzb;

    static {
        zzjx zzjxVar = null;
        zza = new zzjw();
        zzb = new zzjz();
    }

    private zzju() {
    }

    static zzju zza() {
        return zza;
    }

    static zzju zzb() {
        return zzb;
    }

    abstract void zza(Object obj, Object obj2, long j);

    abstract void zzb(Object obj, long j);
}
