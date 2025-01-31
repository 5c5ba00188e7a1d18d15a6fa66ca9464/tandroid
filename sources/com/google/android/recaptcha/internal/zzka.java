package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
final class zzka {
    private static final zzjz zza;
    private static final zzjz zzb;

    static {
        zzjz zzjzVar = null;
        try {
            zzjzVar = (zzjz) Class.forName("com.google.protobuf.MapFieldSchemaFull").getDeclaredConstructor(null).newInstance(null);
        } catch (Exception unused) {
        }
        zza = zzjzVar;
        zzb = new zzjz();
    }

    static zzjz zza() {
        return zza;
    }

    static zzjz zzb() {
        return zzb;
    }
}
