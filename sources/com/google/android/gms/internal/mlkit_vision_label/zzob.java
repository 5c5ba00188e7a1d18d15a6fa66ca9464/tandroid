package com.google.android.gms.internal.mlkit_vision_label;

/* loaded from: classes.dex */
public final class zzob {
    private static zzob zza;

    private zzob() {
    }

    public static synchronized zzob zza() {
        zzob zzobVar;
        synchronized (zzob.class) {
            try {
                if (zza == null) {
                    zza = new zzob();
                }
                zzobVar = zza;
            } catch (Throwable th) {
                throw th;
            }
        }
        return zzobVar;
    }
}
