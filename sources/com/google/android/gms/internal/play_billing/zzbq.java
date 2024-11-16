package com.google.android.gms.internal.play_billing;

/* loaded from: classes.dex */
abstract class zzbq {
    private static final zzbo zza = new zzbp();
    private static final zzbo zzb;

    static {
        zzbo zzboVar = null;
        try {
            zzboVar = (zzbo) Class.forName("com.google.protobuf.ExtensionSchemaFull").getDeclaredConstructor(null).newInstance(null);
        } catch (Exception unused) {
        }
        zzb = zzboVar;
    }

    static zzbo zza() {
        zzbo zzboVar = zzb;
        if (zzboVar != null) {
            return zzboVar;
        }
        throw new IllegalStateException("Protobuf runtime is not correctly loaded.");
    }

    static zzbo zzb() {
        return zza;
    }
}
