package com.google.mlkit.common.sdkinternal;

import java.util.concurrent.ThreadFactory;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzi implements ThreadFactory {
    private final MlKitThreadPool zza;

    public zzi(MlKitThreadPool mlKitThreadPool) {
        this.zza = mlKitThreadPool;
    }

    @Override // java.util.concurrent.ThreadFactory
    public final Thread newThread(Runnable runnable) {
        return this.zza.zzb(runnable);
    }
}
