package com.google.android.gms.internal.mlkit_language_id;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
final class zzeg {
    private static final zzee<?> zza = new zzeh();
    private static final zzee<?> zzb = zzc();

    private static zzee<?> zzc() {
        try {
            return (zzee) Class.forName("com.google.protobuf.ExtensionSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzee<?> zza() {
        return zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzee<?> zzb() {
        zzee<?> zzeeVar = zzb;
        if (zzeeVar != null) {
            return zzeeVar;
        }
        throw new IllegalStateException("Protobuf runtime is not correctly loaded.");
    }
}
