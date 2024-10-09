package com.google.android.gms.vision.clearcut;

/* loaded from: classes.dex */
public final class zzb {
    private final Object zzb = new Object();
    private long zzc = Long.MIN_VALUE;
    private final long zza = Math.round(30000.0d);

    public zzb(double d) {
    }

    public final boolean zza() {
        synchronized (this.zzb) {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                if (this.zzc + this.zza > currentTimeMillis) {
                    return false;
                }
                this.zzc = currentTimeMillis;
                return true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
