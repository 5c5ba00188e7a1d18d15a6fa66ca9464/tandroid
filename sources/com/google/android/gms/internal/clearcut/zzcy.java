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

    static zzcy zzbv() {
        return zzlt;
    }

    static zzcy zzbw() {
        return zzlu;
    }

    abstract void zza(Object obj, long j);

    abstract void zza(Object obj, Object obj2, long j);
}
