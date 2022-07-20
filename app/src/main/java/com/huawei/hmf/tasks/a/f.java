package com.huawei.hmf.tasks.a;

import com.huawei.hmf.tasks.ExecuteResult;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.Task;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class f<TResult> implements ExecuteResult<TResult> {
    private OnFailureListener a;
    private Executor b;
    private final Object c = new Object();

    /* renamed from: com.huawei.hmf.tasks.a.f$1 */
    /* loaded from: classes.dex */
    final class AnonymousClass1 implements Runnable {
        final /* synthetic */ Task a;

        AnonymousClass1(Task task) {
            f.this = r1;
            this.a = task;
        }

        @Override // java.lang.Runnable
        public final void run() {
            synchronized (f.this.c) {
                if (f.this.a != null) {
                    f.this.a.onFailure(this.a.getException());
                }
            }
        }
    }

    public f(Executor executor, OnFailureListener onFailureListener) {
        this.a = onFailureListener;
        this.b = executor;
    }

    @Override // com.huawei.hmf.tasks.ExecuteResult
    public final void onComplete(Task<TResult> task) {
        if (task.isSuccessful() || task.isCanceled()) {
            return;
        }
        this.b.execute(new AnonymousClass1(task));
    }
}
