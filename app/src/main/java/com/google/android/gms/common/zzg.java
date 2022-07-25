package com.google.android.gms.common;

import java.util.Arrays;
/* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
/* loaded from: classes.dex */
final class zzg extends zzd {
    private final byte[] zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzg(byte[] bArr) {
        super(Arrays.copyOfRange(bArr, 0, 25));
        this.zza = bArr;
    }

    @Override // com.google.android.gms.common.zzd
    final byte[] zza() {
        return this.zza;
    }
}
