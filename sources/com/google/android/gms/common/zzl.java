package com.google.android.gms.common;

import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
abstract class zzl extends zzj {
    private static final WeakReference zza = new WeakReference(null);
    private WeakReference zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzl(byte[] bArr) {
        super(bArr);
        this.zzb = zza;
    }

    protected abstract byte[] zzb();

    @Override // com.google.android.gms.common.zzj
    final byte[] zzf() {
        byte[] bArr;
        synchronized (this) {
            try {
                bArr = (byte[]) this.zzb.get();
                if (bArr == null) {
                    bArr = zzb();
                    this.zzb = new WeakReference(bArr);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return bArr;
    }
}
