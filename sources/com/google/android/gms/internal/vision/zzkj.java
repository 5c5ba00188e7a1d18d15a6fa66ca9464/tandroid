package com.google.android.gms.internal.vision;

/* loaded from: classes.dex */
abstract class zzkj {
    private static final zzkh zza = zzc();
    private static final zzkh zzb = new zzkg();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzkh zza() {
        return zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzkh zzb() {
        return zzb;
    }

    private static zzkh zzc() {
        try {
            return (zzkh) Class.forName("com.google.protobuf.MapFieldSchemaFull").getDeclaredConstructor(null).newInstance(null);
        } catch (Exception unused) {
            return null;
        }
    }
}
