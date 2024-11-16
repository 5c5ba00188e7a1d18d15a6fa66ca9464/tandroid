package com.google.android.gms.internal.mlkit_language_id;

/* loaded from: classes.dex */
abstract class zzgg {
    private static final zzge zza = zzc();
    private static final zzge zzb = new zzgh();

    static zzge zza() {
        return zza;
    }

    static zzge zzb() {
        return zzb;
    }

    private static zzge zzc() {
        try {
            return (zzge) Class.forName("com.google.protobuf.NewInstanceSchemaFull").getDeclaredConstructor(null).newInstance(null);
        } catch (Exception unused) {
            return null;
        }
    }
}
