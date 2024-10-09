package com.google.mlkit.common;

import com.google.android.gms.common.internal.Preconditions;

/* loaded from: classes.dex */
public class MlKitException extends Exception {
    private final int zza;

    public MlKitException(String str, int i) {
        super(Preconditions.checkNotEmpty(str, "Provided message must not be empty."));
        this.zza = i;
    }

    public MlKitException(String str, int i, Throwable th) {
        super(Preconditions.checkNotEmpty(str, "Provided message must not be empty."), th);
        this.zza = i;
    }

    public int getErrorCode() {
        return this.zza;
    }
}
