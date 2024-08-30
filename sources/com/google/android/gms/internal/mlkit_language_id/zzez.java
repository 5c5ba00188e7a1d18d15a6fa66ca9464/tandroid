package com.google.android.gms.internal.mlkit_language_id;

import java.io.IOException;
/* loaded from: classes.dex */
public class zzez extends IOException {
    private zzfz zza;

    public zzez(String str) {
        super(str);
        this.zza = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzey zza() {
        return new zzey("Protocol message tag had invalid wire type.");
    }
}
