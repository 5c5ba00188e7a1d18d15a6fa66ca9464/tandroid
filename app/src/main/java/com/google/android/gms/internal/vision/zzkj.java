package com.google.android.gms.internal.vision;
/* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
/* loaded from: classes.dex */
final class zzkj {
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
            return (zzkh) Class.forName("com.google.protobuf.MapFieldSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }
}
