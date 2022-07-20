package com.google.mlkit.common.sdkinternal;

import com.google.android.gms.internal.mlkit_common.zzan;
import com.google.mlkit.common.sdkinternal.TaskQueue;
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzp implements Runnable {
    private final TaskQueue zza;
    private final Runnable zzb;

    public zzp(TaskQueue taskQueue, Runnable runnable) {
        this.zza = taskQueue;
        this.zzb = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        TaskQueue taskQueue = this.zza;
        Runnable runnable = this.zzb;
        TaskQueue.zza zzaVar = new TaskQueue.zza();
        try {
            runnable.run();
            zzaVar.close();
        } catch (Throwable th) {
            try {
                zzaVar.close();
            } catch (Throwable th2) {
                zzan.zza(th, th2);
            }
            throw th;
        }
    }
}
