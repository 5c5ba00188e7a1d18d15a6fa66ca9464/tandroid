package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
abstract class zzlx {
    zzlx() {
    }

    abstract int zza(int i, byte[] bArr, int i2, int i3);

    final boolean zzb(byte[] bArr, int i, int i2) {
        return zza(0, bArr, i, i2) == 0;
    }
}
