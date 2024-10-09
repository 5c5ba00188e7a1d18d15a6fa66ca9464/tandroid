package com.google.android.gms.common;

/* loaded from: classes.dex */
public abstract class GooglePlayServicesManifestException extends IllegalStateException {
    private final int zza;

    public GooglePlayServicesManifestException(int i, String str) {
        super(str);
        this.zza = i;
    }
}
