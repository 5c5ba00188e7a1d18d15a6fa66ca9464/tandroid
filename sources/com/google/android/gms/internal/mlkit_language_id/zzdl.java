package com.google.android.gms.internal.mlkit_language_id;

/* loaded from: classes.dex */
abstract class zzdl {
    private static final Class zza = zza("libcore.io.Memory");
    private static final boolean zzb;

    static {
        zzb = zza("org.robolectric.Robolectric") != null;
    }

    private static Class zza(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable unused) {
            return null;
        }
    }

    static boolean zza() {
        return (zza == null || zzb) ? false : true;
    }

    static Class zzb() {
        return zza;
    }
}
