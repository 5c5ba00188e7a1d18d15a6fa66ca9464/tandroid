package com.google.android.gms.common.providers;

import java.util.concurrent.ScheduledExecutorService;
/* loaded from: classes.dex */
public abstract class PooledExecutorsProvider {
    private static PooledExecutorFactory zza;

    /* loaded from: classes.dex */
    public interface PooledExecutorFactory {
        ScheduledExecutorService newSingleThreadScheduledExecutor();
    }

    public static synchronized PooledExecutorFactory getInstance() {
        PooledExecutorFactory pooledExecutorFactory;
        synchronized (PooledExecutorsProvider.class) {
            try {
                if (zza == null) {
                    zza = new zza();
                }
                pooledExecutorFactory = zza;
            } catch (Throwable th) {
                throw th;
            }
        }
        return pooledExecutorFactory;
    }
}
