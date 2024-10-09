package com.google.android.gms.common;

import java.util.Arrays;

/* loaded from: classes.dex */
final class zzk extends zzj {
    private final byte[] zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzk(byte[] bArr) {
        super(Arrays.copyOfRange(bArr, 0, 25));
        this.zza = bArr;
    }

    @Override // com.google.android.gms.common.zzj
    final byte[] zzf() {
        return this.zza;
    }
}
