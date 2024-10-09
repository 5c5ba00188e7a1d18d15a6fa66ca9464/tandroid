package com.google.android.gms.internal.mlkit_vision_common;

/* loaded from: classes.dex */
public final class zzmw {
    private static zzmw zza;

    private zzmw() {
    }

    public static synchronized zzmw zza() {
        zzmw zzmwVar;
        synchronized (zzmw.class) {
            try {
                if (zza == null) {
                    zza = new zzmw();
                }
                zzmwVar = zza;
            } catch (Throwable th) {
                throw th;
            }
        }
        return zzmwVar;
    }

    public static final boolean zzb() {
        return zzmv.zza("mlkit-dev-profiling");
    }
}
