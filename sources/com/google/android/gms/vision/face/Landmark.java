package com.google.android.gms.vision.face;

import android.graphics.PointF;

/* loaded from: classes.dex */
public final class Landmark {
    private final PointF zza;
    private final int zzb;

    public Landmark(PointF pointF, int i) {
        this.zza = pointF;
        this.zzb = i;
    }

    public final PointF getPosition() {
        return this.zza;
    }

    public final int getType() {
        return this.zzb;
    }
}
