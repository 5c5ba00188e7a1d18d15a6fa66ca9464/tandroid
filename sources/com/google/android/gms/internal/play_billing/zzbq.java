package com.google.android.gms.internal.play_billing;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
final class zzbq {
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzbo zza() {
        zzbo zzboVar = zzb;
        if (zzboVar != null) {
            return zzboVar;
        }
        throw new IllegalStateException("Protobuf runtime is not correctly loaded.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzbo zzb() {
        return zza;
    }
}
