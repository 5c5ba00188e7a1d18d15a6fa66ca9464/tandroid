package com.google.mlkit.common.sdkinternal;

import com.google.android.gms.common.internal.Preconditions;
import java.io.Closeable;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicReference;
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes.dex */
public class TaskQueue {
    private boolean zzb;
    private final Object zza = new Object();
    private final Queue<zzb> zzc = new ArrayDeque();
    private final AtomicReference<Thread> zzd = new AtomicReference<>();

    /* compiled from: com.google.mlkit:common@@17.0.0 */
    /* loaded from: classes.dex */
    public static class zzb {
        final Executor zza;
        final Runnable zzb;

        private zzb(Executor executor, Runnable runnable) {
            this.zza = executor;
            this.zzb = runnable;
        }
    }

    /* compiled from: com.google.mlkit:common@@17.0.0 */
    /* loaded from: classes.dex */
    public class zza implements Closeable {
        /* JADX INFO: Access modifiers changed from: private */
        public zza() {
            TaskQueue.this = r2;
            Preconditions.checkState(((Thread) r2.zzd.getAndSet(Thread.currentThread())) == null);
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public final void close() {
            TaskQueue.this.zzd.set(null);
            TaskQueue.this.zza();
        }
    }

    public void submit(Executor executor, Runnable runnable) {
        synchronized (this.zza) {
            if (this.zzb) {
                this.zzc.add(new zzb(executor, runnable));
                return;
            }
            this.zzb = true;
            zza(executor, runnable);
        }
    }

    private final void zza(Executor executor, Runnable runnable) {
        try {
            executor.execute(new zzp(this, runnable));
        } catch (RejectedExecutionException unused) {
            zza();
        }
    }

    public final void zza() {
        synchronized (this.zza) {
            if (this.zzc.isEmpty()) {
                this.zzb = false;
                return;
            }
            zzb remove = this.zzc.remove();
            zza(remove.zza, remove.zzb);
        }
    }

    public void checkIsRunningOnCurrentThread() {
        Preconditions.checkState(Thread.currentThread().equals(this.zzd.get()));
    }
}
