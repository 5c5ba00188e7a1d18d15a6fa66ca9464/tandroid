package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
final class zzkl {
    private static final zzkk zza;
    private static final zzkk zzb;

    static {
        zzkk zzkkVar = null;
        try {
            zzkkVar = (zzkk) Class.forName("com.google.protobuf.NewInstanceSchemaFull").getDeclaredConstructor(null).newInstance(null);
        } catch (Exception unused) {
        }
        zza = zzkkVar;
        zzb = new zzkk();
    }

    static zzkk zza() {
        return zza;
    }

    static zzkk zzb() {
        return zzb;
    }
}
