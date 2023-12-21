package com.google.android.gms.internal.mlkit_vision_subject_segmentation;
/* compiled from: com.google.android.gms:play-services-mlkit-subject-segmentation@@16.0.0-beta1 */
/* loaded from: classes.dex */
public final class zztx {
    private static zztx zza;

    private zztx() {
    }

    public static synchronized zztx zza() {
        zztx zztxVar;
        synchronized (zztx.class) {
            if (zza == null) {
                zza = new zztx();
            }
            zztxVar = zza;
        }
        return zztxVar;
    }
}
