package com.google.android.gms.internal.clearcut;
/* loaded from: classes.dex */
final class zzdl {
    private static final zzdj zzmf = zzce();
    private static final zzdj zzmg = new zzdk();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzdj zzcc() {
        return zzmf;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzdj zzcd() {
        return zzmg;
    }

    private static zzdj zzce() {
        try {
            return (zzdj) Class.forName("com.google.protobuf.MapFieldSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }
}
