package com.google.mlkit.common.sdkinternal;

import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.concurrent.Callable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzl implements Runnable {
    private final ModelResource zza;
    private final CancellationToken zzb;
    private final CancellationTokenSource zzc;
    private final Callable zzd;
    private final TaskCompletionSource zze;

    public zzl(ModelResource modelResource, CancellationToken cancellationToken, CancellationTokenSource cancellationTokenSource, Callable callable, TaskCompletionSource taskCompletionSource) {
        this.zza = modelResource;
        this.zzb = cancellationToken;
        this.zzc = cancellationTokenSource;
        this.zzd = callable;
        this.zze = taskCompletionSource;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zza.zza(this.zzb, this.zzc, this.zzd, this.zze);
    }
}
