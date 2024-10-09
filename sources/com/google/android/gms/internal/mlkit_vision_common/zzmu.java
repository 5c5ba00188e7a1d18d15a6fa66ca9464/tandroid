package com.google.android.gms.internal.mlkit_vision_common;

import android.os.SystemClock;

/* loaded from: classes.dex */
public abstract class zzmu {
    public static void zza(zzmj zzmjVar, int i, int i2, long j, int i3, int i4, int i5, int i6) {
        zzmjVar.zzc(zzc(i, i2, j, i3, i4, i5, i6), zziv.zzbA);
    }

    private static zzmt zzc(int i, int i2, long j, int i3, int i4, int i5, int i6) {
        return new zzmt(i, i2, i5, i3, i4, SystemClock.elapsedRealtime() - j, i6);
    }
}
