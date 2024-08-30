package com.google.android.gms.internal.mlkit_vision_subject_segmentation;
/* loaded from: classes.dex */
public abstract class zztw {
    private static zztv zza;

    public static synchronized zztl zza(zztd zztdVar) {
        zztl zztlVar;
        synchronized (zztw.class) {
            try {
                if (zza == null) {
                    zza = new zztv(null);
                }
                zztlVar = (zztl) zza.get(zztdVar);
            } catch (Throwable th) {
                throw th;
            }
        }
        return zztlVar;
    }

    public static synchronized zztl zzb(String str) {
        zztl zza2;
        synchronized (zztw.class) {
            zza2 = zza(zztd.zzd("subject-segmentation").zzd());
        }
        return zza2;
    }
}
