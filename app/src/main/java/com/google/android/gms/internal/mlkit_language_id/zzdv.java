package com.google.android.gms.internal.mlkit_language_id;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public final class zzdv {
    private final zzea zza;
    private final byte[] zzb;

    private zzdv(int i) {
        byte[] bArr = new byte[i];
        this.zzb = bArr;
        this.zza = zzea.zza(bArr);
    }

    public final zzdn zza() {
        this.zza.zzb();
        return new zzdx(this.zzb);
    }

    public final zzea zzb() {
        return this.zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzdv(int i, zzdm zzdmVar) {
        this(i);
    }
}
