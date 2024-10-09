package com.google.android.gms.internal.vision;

/* loaded from: classes.dex */
abstract class zzku {
    private static final zzks zza = zzc();
    private static final zzks zzb = new zzkv();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzks zza() {
        return zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzks zzb() {
        return zzb;
    }

    private static zzks zzc() {
        try {
            return (zzks) Class.forName("com.google.protobuf.NewInstanceSchemaFull").getDeclaredConstructor(null).newInstance(null);
        } catch (Exception unused) {
            return null;
        }
    }
}
