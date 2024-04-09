package com.google.android.gms.internal.mlkit_vision_label;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public final class zzoa {
    private static zznz zza;

    public static synchronized zznp zza(zznh zznhVar) {
        zznp zznpVar;
        synchronized (zzoa.class) {
            if (zza == null) {
                zza = new zznz(null);
            }
            zznpVar = (zznp) zza.get(zznhVar);
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
