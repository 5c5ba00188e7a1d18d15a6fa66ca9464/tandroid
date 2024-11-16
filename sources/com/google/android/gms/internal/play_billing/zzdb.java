package com.google.android.gms.internal.play_billing;

/* loaded from: classes.dex */
abstract class zzdb {
    private static final zzda zza;
    private static final zzda zzb;

    static {
        zzda zzdaVar = null;
        try {
            zzdaVar = (zzda) Class.forName("com.google.protobuf.MapFieldSchemaFull").getDeclaredConstructor(null).newInstance(null);
        } catch (Exception unused) {
        }
        zza = zzdaVar;
        zzb = new zzda();
    }

    static zzda zza() {
        return zza;
    }

    static zzda zzb() {
        return zzb;
    }
}
