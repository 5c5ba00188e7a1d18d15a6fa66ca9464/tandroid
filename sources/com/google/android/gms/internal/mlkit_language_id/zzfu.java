package com.google.android.gms.internal.mlkit_language_id;

/* loaded from: classes.dex */
abstract class zzfu {
    private static final zzfs zza = zzc();
    private static final zzfs zzb = new zzfv();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzfs zza() {
        return zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzfs zzb() {
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
