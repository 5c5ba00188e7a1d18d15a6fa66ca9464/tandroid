package com.google.android.gms.cloudmessaging;
/* compiled from: com.google.android.gms:play-services-cloud-messaging@@16.0.0 */
/* loaded from: classes.dex */
final /* synthetic */ class zzm implements Runnable {
    private final zzf zza;

    public zzm(zzf zzfVar) {
        this.zza = zzfVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zza.zza(2, "Service disconnected");
    }
}
