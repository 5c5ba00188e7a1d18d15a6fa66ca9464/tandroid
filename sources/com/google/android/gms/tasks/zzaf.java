package com.google.android.gms.tasks;

import java.util.concurrent.ExecutionException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzaf implements zzae {
    private final Object zza = new Object();
    private final int zzb;
    private final zzw zzc;
    private int zzd;
    private int zze;
    private int zzf;
    private Exception zzg;
    private boolean zzh;

    public zzaf(int i, zzw zzwVar) {
        this.zzb = i;
        this.zzc = zzwVar;
    }

    private final void zza() {
        if (this.zzd + this.zze + this.zzf == this.zzb) {
            if (this.zzg == null) {
                if (this.zzh) {
                    this.zzc.zzc();
                    return;
                } else {
                    this.zzc.zzb(null);
                    return;
                }
            }
            zzw zzwVar = this.zzc;
            int i = this.zze;
            int i2 = this.zzb;
            zzwVar.zza(new ExecutionException(i + " out of " + i2 + " underlying tasks failed", this.zzg));
        }
    }

    @Override // com.google.android.gms.tasks.OnCanceledListener
    public final void onCanceled() {
        synchronized (this.zza) {
            this.zzf++;
            this.zzh = true;
            zza();
        }
    }

    @Override // com.google.android.gms.tasks.OnFailureListener
    public final void onFailure(Exception exc) {
        synchronized (this.zza) {
            this.zze++;
            this.zzg = exc;
            zza();
        }
    }

    @Override // com.google.android.gms.tasks.OnSuccessListener
    public final void onSuccess(Object obj) {
        synchronized (this.zza) {
            this.zzd++;
            zza();
        }
    }
}
