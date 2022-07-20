package com.google.android.gms.cloudmessaging;

import android.os.IBinder;
import android.os.RemoteException;
/* compiled from: com.google.android.gms:play-services-cloud-messaging@@16.0.0 */
/* loaded from: classes.dex */
final /* synthetic */ class zzk implements Runnable {
    private final zzf zza;
    private final IBinder zzb;

    public zzk(zzf zzfVar, IBinder iBinder) {
        this.zza = zzfVar;
        this.zzb = iBinder;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzf zzfVar = this.zza;
        IBinder iBinder = this.zzb;
        synchronized (zzfVar) {
            try {
                if (iBinder == null) {
                    zzfVar.zza(0, "Null service connection");
                    return;
                }
                try {
                    zzfVar.zzc = new zzo(iBinder);
                    zzfVar.zza = 2;
                    zzfVar.zza();
                } catch (RemoteException e) {
                    zzfVar.zza(0, e.getMessage());
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
