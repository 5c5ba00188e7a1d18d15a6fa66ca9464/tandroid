package com.google.android.gms.tasks;

/* loaded from: classes.dex */
public class TaskCompletionSource {
    private final zzw zza = new zzw();

    public TaskCompletionSource() {
    }

    public TaskCompletionSource(CancellationToken cancellationToken) {
        cancellationToken.onCanceledRequested(new zzs(this));
    }

    public Task getTask() {
        return this.zza;
    }

    public void setException(Exception exc) {
        this.zza.zza(exc);
    }

    public void setResult(Object obj) {
        this.zza.zzb(obj);
    }

    public boolean trySetException(Exception exc) {
        return this.zza.zzd(exc);
    }

    public boolean trySetResult(Object obj) {
        return this.zza.zze(obj);
    }
}
