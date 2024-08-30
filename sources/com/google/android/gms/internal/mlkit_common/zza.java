package com.google.android.gms.internal.mlkit_common;

import android.os.Handler;
import android.os.Looper;
/* loaded from: classes.dex */
public final class zza extends Handler {
    private final Looper zza;

    public zza(Looper looper) {
        super(looper);
        this.zza = Looper.getMainLooper();
    }
}
