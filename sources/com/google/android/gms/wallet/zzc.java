package com.google.android.gms.wallet;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import com.google.android.gms.internal.wallet.zzh;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
final class zzc implements OnCompleteListener, Runnable {
    static final Handler zza = new zzh(Looper.getMainLooper());
    static final SparseArray zzb = new SparseArray(2);
    private static final AtomicInteger zzd = new AtomicInteger();
    int zzc;
    private zzd zze;
    private Task zzf;

    zzc() {
    }

    public static zzc zza(Task task) {
        long j;
        zzc zzcVar = new zzc();
        int incrementAndGet = zzd.incrementAndGet();
        zzcVar.zzc = incrementAndGet;
        zzb.put(incrementAndGet, zzcVar);
        Handler handler = zza;
        j = AutoResolveHelper.zzb;
        handler.postDelayed(zzcVar, j);
        task.addOnCompleteListener(zzcVar);
        return zzcVar;
    }

    private final void zzd() {
        if (this.zzf == null || this.zze == null) {
            return;
        }
        zzb.delete(this.zzc);
        zza.removeCallbacks(this);
        zzd zzdVar = this.zze;
        if (zzdVar != null) {
            zzdVar.zzb(this.zzf);
        }
    }

    @Override // com.google.android.gms.tasks.OnCompleteListener
    public final void onComplete(Task task) {
        this.zzf = task;
        zzd();
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzb.delete(this.zzc);
    }

    public final void zzb(zzd zzdVar) {
        if (this.zze == zzdVar) {
            this.zze = null;
        }
    }

    public final void zzc(zzd zzdVar) {
        this.zze = zzdVar;
        zzd();
    }
}
