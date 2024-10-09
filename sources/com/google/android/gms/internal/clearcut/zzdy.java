package com.google.android.gms.internal.clearcut;

/* loaded from: classes.dex */
abstract class zzdy {
    private static final zzdw zzna = zzcl();
    private static final zzdw zznb = new zzdx();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzdw zzcj() {
        return zzna;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzdw zzck() {
        return zznb;
    }

    private static zzdw zzcl() {
        try {
            return (zzdw) Class.forName("com.google.protobuf.NewInstanceSchemaFull").getDeclaredConstructor(null).newInstance(null);
        } catch (Exception unused) {
            return null;
        }
    }
}
