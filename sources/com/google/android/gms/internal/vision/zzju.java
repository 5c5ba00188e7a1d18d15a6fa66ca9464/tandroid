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

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzju zza() {
        return zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzju zzb() {
        return zzb;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void zza(Object obj, Object obj2, long j);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void zzb(Object obj, long j);
}
