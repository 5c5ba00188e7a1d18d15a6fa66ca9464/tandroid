package com.google.android.gms.internal.vision;

/* loaded from: classes.dex */
abstract class zzme {
    zzme() {
    }

    abstract int zza(int i, byte[] bArr, int i2, int i3);

    abstract int zza(CharSequence charSequence, byte[] bArr, int i, int i2);

    final boolean zza(byte[] bArr, int i, int i2) {
        return zza(0, bArr, i, i2) == 0;
    }

    abstract String zzb(byte[] bArr, int i, int i2);
}
