package com.google.android.gms.vision;
/* loaded from: classes.dex */
public abstract class Detector {
    private final Object zza = new Object();

    public void release() {
        synchronized (this.zza) {
        }
    }
}
