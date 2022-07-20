package com.huawei.hmf.tasks.a;

import android.os.Looper;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.TaskCompletionSource;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class j {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.huawei.hmf.tasks.a.j$1 */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 implements Runnable {
        final /* synthetic */ TaskCompletionSource a;
        final /* synthetic */ Callable b;

        AnonymousClass1(j jVar, TaskCompletionSource taskCompletionSource, Callable callable) {
            this.a = taskCompletionSource;
            this.b = callable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            try {
                this.a.setResult(this.b.call());
            } catch (Exception e) {
                this.a.setException(e);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class a<TResult> implements OnFailureListener, OnSuccessListener<TResult> {
        public final CountDownLatch a = new CountDownLatch(1);

        @Override // com.huawei.hmf.tasks.OnFailureListener
        public final void onFailure(Exception exc) {
            this.a.countDown();
        }

        @Override // com.huawei.hmf.tasks.OnSuccessListener
        public final void onSuccess(TResult tresult) {
            this.a.countDown();
        }
    }

    public static <TResult> TResult a(Task<TResult> task) throws ExecutionException {
        if (task.isSuccessful()) {
            return task.getResult();
        }
        throw new ExecutionException(task.getException());
    }

    public static void a(String str) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }
        throw new IllegalStateException(str);
    }

    public final <TResult> Task<TResult> a(Executor executor, Callable<TResult> callable) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        try {
            executor.execute(new AnonymousClass1(this, taskCompletionSource, callable));
        } catch (Exception e) {
            taskCompletionSource.setException(e);
        }
        return taskCompletionSource.getTask();
    }
}
