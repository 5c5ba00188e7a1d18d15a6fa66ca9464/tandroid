package com.google.mlkit.common.sdkinternal;
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes.dex */
final /* synthetic */ class zzh implements Runnable {
    private final Runnable zza;

    public zzh(Runnable runnable) {
        this.zza = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        MlKitThreadPool.zzd(this.zza);
    }
}
