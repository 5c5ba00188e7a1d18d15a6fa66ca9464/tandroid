package com.google.android.gms.internal.vision;

import java.util.Arrays;

/* loaded from: classes.dex */
final class zzhx implements zzhz {
    private zzhx() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzhx(zzhs zzhsVar) {
        this();
    }

    @Override // com.google.android.gms.internal.vision.zzhz
    public final byte[] zza(byte[] bArr, int i, int i2) {
        return Arrays.copyOfRange(bArr, i, i2 + i);
    }
}
