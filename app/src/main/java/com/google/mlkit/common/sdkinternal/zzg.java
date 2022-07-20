package com.google.mlkit.common.sdkinternal;

import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.concurrent.Callable;
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzg implements Runnable {
    private final Callable zza;
    private final TaskCompletionSource zzb;

    public zzg(Callable callable, TaskCompletionSource taskCompletionSource) {
        this.zza = callable;
        this.zzb = taskCompletionSource;
    }

    @Override // java.lang.Runnable
    public final void run() {
        MLTaskExecutor.zza(this.zza, this.zzb);
    }
}
