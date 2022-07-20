package com.google.android.gms.cloudmessaging;
/* compiled from: com.google.android.gms:play-services-cloud-messaging@@16.0.0 */
/* loaded from: classes.dex */
final /* synthetic */ class zzl implements Runnable {
    private final zzf zza;
    private final zzq zzb;

    public zzl(zzf zzfVar, zzq zzqVar) {
        this.zza = zzfVar;
        this.zzb = zzqVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zza.zza(this.zzb.zza);
    }
}
