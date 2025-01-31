package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
final class zzih {
    private static final zzif zza = new zzig();
    private static final zzif zzb;

    static {
        zzif zzifVar = null;
        try {
            zzifVar = (zzif) Class.forName("com.google.protobuf.ExtensionSchemaFull").getDeclaredConstructor(null).newInstance(null);
        } catch (Exception unused) {
        }
        zzb = zzifVar;
    }

    static zzif zza() {
        zzif zzifVar = zzb;
        if (zzifVar != null) {
            return zzifVar;
        }
        throw new IllegalStateException("Protobuf runtime is not correctly loaded.");
    }

    static zzif zzb() {
        return zza;
    }
}
