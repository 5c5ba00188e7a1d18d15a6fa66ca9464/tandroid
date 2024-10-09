package com.google.android.gms.internal.clearcut;

/* loaded from: classes.dex */
abstract class zzcy {
    private static final zzcy zzlt;
    private static final zzcy zzlu;

    static {
        zzcz zzczVar = null;
        zzlt = new zzda();
        zzlu = new zzdb();
    }

    private zzcy() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzcy zzbv() {
        return zzlt;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzcy zzbw() {
        return zzlu;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void zza(Object obj, long j);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void zza(Object obj, Object obj2, long j);
}
