package com.google.android.gms.cloudmessaging;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.util.concurrent.NamedThreadFactory;
import com.google.android.gms.tasks.Task;
import java.util.concurrent.ScheduledExecutorService;

/* loaded from: classes.dex */
public final class zze {
    private static zze zza;
    private final Context zzb;
    private final ScheduledExecutorService zzc;
    private zzf zzd = new zzf(this);
    private int zze = 1;

    private zze(Context context, ScheduledExecutorService scheduledExecutorService) {
        this.zzc = scheduledExecutorService;
        this.zzb = context.getApplicationContext();
    }

    private final synchronized int zza() {
        int i;
        i = this.zze;
        this.zze = i + 1;
        return i;
    }

    public static synchronized zze zza(Context context) {
        zze zzeVar;
        synchronized (zze.class) {
            try {
                if (zza == null) {
                    zza = new zze(context, com.google.android.gms.internal.cloudmessaging.zza.zza().zza(1, new NamedThreadFactory("MessengerIpcClient"), com.google.android.gms.internal.cloudmessaging.zzf.zzb));
                }
                zzeVar = zza;
            } catch (Throwable th) {
                throw th;
            }
        }
        return zzeVar;
    }

    private final synchronized Task zza(zzq zzqVar) {
        try {
            if (Log.isLoggable("MessengerIpcClient", 3)) {
                String valueOf = String.valueOf(zzqVar);
                StringBuilder sb = new StringBuilder(valueOf.length() + 9);
                sb.append("Queueing ");
                sb.append(valueOf);
                Log.d("MessengerIpcClient", sb.toString());
            }
            if (!this.zzd.zza(zzqVar)) {
                zzf zzfVar = new zzf(this);
                this.zzd = zzfVar;
                zzfVar.zza(zzqVar);
            }
        } catch (Throwable th) {
            throw th;
        }
        return zzqVar.zzb.getTask();
    }

    public final Task zza(int i, Bundle bundle) {
        return zza(new zzn(zza(), 2, bundle));
    }

    public final Task zzb(int i, Bundle bundle) {
        return zza(new zzs(zza(), 1, bundle));
    }
}
