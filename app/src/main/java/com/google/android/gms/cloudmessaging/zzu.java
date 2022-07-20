package com.google.android.gms.cloudmessaging;

import com.google.android.gms.tasks.TaskCompletionSource;
/* compiled from: com.google.android.gms:play-services-cloud-messaging@@16.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzu implements Runnable {
    private final TaskCompletionSource zza;

    public zzu(TaskCompletionSource taskCompletionSource) {
        this.zza = taskCompletionSource;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Rpc.zza(this.zza);
    }
}
