package com.google.android.gms.internal.vision;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
/* loaded from: classes.dex */
public final class zzib {
    private final zzii zza;
    private final byte[] zzb;

    private zzib(int i) {
        byte[] bArr = new byte[i];
        this.zzb = bArr;
        this.zza = zzii.zza(bArr);
    }

    public final zzht zza() {
        this.zza.zzb();
        return new zzid(this.zzb);
    }

    public final zzii zzb() {
        return this.zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzib(int i, zzhs zzhsVar) {
        this(i);
    }
}
