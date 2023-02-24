package com.google.android.gms.tasks;

import java.util.ArrayDeque;
import java.util.Queue;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-tasks@@18.0.2 */
/* loaded from: classes.dex */
public final class zzr {
    private final Object zza = new Object();
    private Queue zzb;
    private boolean zzc;

    public final void zza(zzq zzqVar) {
        synchronized (this.zza) {
            if (this.zzb == null) {
                this.zzb = new ArrayDeque();
            }
            this.zzb.add(zzqVar);
        }
    }

    public final void zzb(Task task) {
        zzq zzqVar;
        synchronized (this.zza) {
            if (this.zzb != null && !this.zzc) {
                this.zzc = true;
                while (true) {
                    synchronized (this.zza) {
                        zzqVar = (zzq) this.zzb.poll();
                        if (zzqVar == null) {
                            this.zzc = false;
                            return;
                        }
                    }
                    zzqVar.zzd(task);
                }
            }
        }
    }
}
