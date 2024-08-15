package com.google.android.gms.vision;
/* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
/* loaded from: classes.dex */
public abstract class Detector<T> {
    private final Object zza = new Object();

    public void release() {
        synchronized (this.zza) {
        }
    }
}
