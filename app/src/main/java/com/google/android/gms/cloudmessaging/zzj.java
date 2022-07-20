package com.google.android.gms.cloudmessaging;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-cloud-messaging@@16.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzj implements Runnable {
    private final zzf zza;

    public zzj(zzf zzfVar) {
        this.zza = zzfVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzq<?> poll;
        ScheduledExecutorService scheduledExecutorService;
        Context context;
        zzf zzfVar = this.zza;
        while (true) {
            synchronized (zzfVar) {
                if (zzfVar.zza != 2) {
                    return;
                }
                if (zzfVar.zzd.isEmpty()) {
                    zzfVar.zzb();
                    return;
                }
                poll = zzfVar.zzd.poll();
                zzfVar.zze.put(poll.zza, poll);
                scheduledExecutorService = zzfVar.zzf.zzc;
                scheduledExecutorService.schedule(new zzl(zzfVar, poll), 30L, TimeUnit.SECONDS);
            }
            if (Log.isLoggable("MessengerIpcClient", 3)) {
                String valueOf = String.valueOf(poll);
                StringBuilder sb = new StringBuilder(valueOf.length() + 8);
                sb.append("Sending ");
                sb.append(valueOf);
                Log.d("MessengerIpcClient", sb.toString());
            }
            context = zzfVar.zzf.zzb;
            Messenger messenger = zzfVar.zzb;
            Message obtain = Message.obtain();
            obtain.what = poll.zzc;
            obtain.arg1 = poll.zza;
            obtain.replyTo = messenger;
            Bundle bundle = new Bundle();
            bundle.putBoolean("oneWay", poll.zza());
            bundle.putString("pkg", context.getPackageName());
            bundle.putBundle("data", poll.zzd);
            obtain.setData(bundle);
            try {
                zzfVar.zzc.zza(obtain);
            } catch (RemoteException e) {
                zzfVar.zza(2, e.getMessage());
            }
        }
    }
}
