package com.google.mlkit.common.sdkinternal;

import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.concurrent.Executor;
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes.dex */
final /* synthetic */ class zzm implements Executor {
    private final Executor zza;
    private final CancellationToken zzb;
    private final CancellationTokenSource zzc;
    private final TaskCompletionSource zzd;

    public zzm(Executor executor, CancellationToken cancellationToken, CancellationTokenSource cancellationTokenSource, TaskCompletionSource taskCompletionSource) {
        this.zza = executor;
        this.zzb = cancellationToken;
        this.zzc = cancellationTokenSource;
        this.zzd = taskCompletionSource;
    }

    @Override // java.util.concurrent.Executor
    public final void execute(Runnable runnable) {
        Executor executor = this.zza;
        CancellationToken cancellationToken = this.zzb;
        CancellationTokenSource cancellationTokenSource = this.zzc;
        TaskCompletionSource taskCompletionSource = this.zzd;
        try {
            executor.execute(runnable);
        } catch (RuntimeException e) {
            if (cancellationToken.isCancellationRequested()) {
                cancellationTokenSource.cancel();
            } else {
                taskCompletionSource.setException(e);
            }
            throw e;
        }
    }
}
