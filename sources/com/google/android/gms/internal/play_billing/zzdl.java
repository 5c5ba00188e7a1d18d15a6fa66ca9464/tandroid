package com.google.android.gms.internal.play_billing;

/* loaded from: classes.dex */
abstract class zzdl {
    private static final zzdk zza;
    private static final zzdk zzb;

    static {
        zzdk zzdkVar = null;
        try {
            zzdkVar = (zzdk) Class.forName("com.google.protobuf.NewInstanceSchemaFull").getDeclaredConstructor(null).newInstance(null);
        } catch (Exception unused) {
        }
        zza = zzdkVar;
        zzb = new zzdk();
    }

    static zzdk zza() {
        return zza;
    }

    static zzdk zzb() {
        return zzb;
    }
}
