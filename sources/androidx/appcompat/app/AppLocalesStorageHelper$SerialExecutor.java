package androidx.appcompat.app;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class AppLocalesStorageHelper$SerialExecutor implements Executor {
    Runnable mActive;
    final Executor mExecutor;
    private final Object mLock = new Object();
    final Queue mTasks = new ArrayDeque();

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppLocalesStorageHelper$SerialExecutor(Executor executor) {
        this.mExecutor = executor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$execute$0(Runnable runnable) {
        try {
            runnable.run();
        } finally {
            scheduleNext();
        }
    }

    @Override // java.util.concurrent.Executor
    public void execute(final Runnable runnable) {
        synchronized (this.mLock) {
            try {
                this.mTasks.add(new Runnable() { // from class: androidx.appcompat.app.AppLocalesStorageHelper$SerialExecutor$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        AppLocalesStorageHelper$SerialExecutor.this.lambda$execute$0(runnable);
                    }
                });
                if (this.mActive == null) {
                    scheduleNext();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    protected void scheduleNext() {
        synchronized (this.mLock) {
            try {
                Runnable runnable = (Runnable) this.mTasks.poll();
                this.mActive = runnable;
                if (runnable != null) {
                    this.mExecutor.execute(runnable);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
