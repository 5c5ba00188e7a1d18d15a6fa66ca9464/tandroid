package com.google.android.gms.internal.mlkit_language_id;

/* loaded from: classes.dex */
abstract class zzfu {
    private static final zzfs zza = zzc();
    private static final zzfs zzb = new zzfv();

    static zzfs zza() {
        return zza;
    }

    static zzfs zzb() {
        return zzb;
    }

    private static zzfs zzc() {
        try {
            return (zzfs) Class.forName("com.google.protobuf.MapFieldSchemaFull").getDeclaredConstructor(null).newInstance(null);
        } catch (Exception unused) {
            return null;
        }
    }
}
