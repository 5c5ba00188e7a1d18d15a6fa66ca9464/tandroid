package com.google.android.gms.internal.mlkit_vision_label;
/* loaded from: classes.dex */
public abstract class zzoa {
    private static zznz zza;

    public static synchronized zznp zza(zznh zznhVar) {
        zznp zznpVar;
        synchronized (zzoa.class) {
            try {
                if (zza == null) {
                    zza = new zznz(null);
                }
                zznpVar = (zznp) zza.get(zznhVar);
            } catch (Throwable th) {
                throw th;
            }
        }
        return zznpVar;
    }

    public static synchronized zznp zzb(String str) {
        zznp zza2;
        synchronized (zzoa.class) {
            zza2 = zza(zznh.zzd("play-services-mlkit-image-labeling").zzd());
        }
        return zza2;
    }
}
