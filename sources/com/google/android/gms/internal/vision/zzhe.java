package com.google.android.gms.internal.vision;

/* loaded from: classes.dex */
public abstract class zzhe implements zzkn {
    protected abstract zzhe zza(zzhf zzhfVar);

    public abstract zzhe zza(byte[] bArr, int i, int i2, zzio zzioVar);

    @Override // com.google.android.gms.internal.vision.zzkn
    public final /* synthetic */ zzkn zza(zzkk zzkkVar) {
        if (zzr().getClass().isInstance(zzkkVar)) {
            return zza((zzhf) zzkkVar);
        }
        throw new IllegalArgumentException("mergeFrom(MessageLite) can only merge messages of the same type.");
    }
}
