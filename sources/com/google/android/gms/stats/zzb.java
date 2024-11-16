package com.google.android.gms.stats;

/* loaded from: classes.dex */
final class zzb implements Runnable {
    private final /* synthetic */ WakeLock zzp;

    zzb(WakeLock wakeLock) {
        this.zzp = wakeLock;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzp.zza(0);
    }
}
