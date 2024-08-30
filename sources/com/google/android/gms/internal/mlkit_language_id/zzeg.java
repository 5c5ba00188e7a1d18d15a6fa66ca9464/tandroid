package com.google.android.gms.internal.mlkit_language_id;
/* loaded from: classes.dex */
abstract class zzeg {
    private static final zzee zza = new zzeh();
    private static final zzee zzb = zzc();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzee zza() {
        return zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzee zzb() {
        zzee zzeeVar = zzb;
        if (zzeeVar != null) {
            return zzeeVar;
        }
        throw new IllegalStateException("Protobuf runtime is not correctly loaded.");
    }

    private static zzee zzc() {
        try {
            return (zzee) Class.forName("com.google.protobuf.ExtensionSchemaFull").getDeclaredConstructor(null).newInstance(null);
        } catch (Exception unused) {
            return null;
        }
    }
}
