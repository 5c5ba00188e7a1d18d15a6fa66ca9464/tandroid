package com.huawei.hmf.tasks.a;

import com.huawei.hmf.tasks.ExecuteResult;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class h<TResult> implements ExecuteResult<TResult> {
    private OnSuccessListener<TResult> a;
    private Executor b;
    private final Object c = new Object();

    /* renamed from: com.huawei.hmf.tasks.a.h$1 */
    /* loaded from: classes.dex */
    final class AnonymousClass1 implements Runnable {
        final /* synthetic */ Task a;

        AnonymousClass1(Task task) {
            h.this = r1;
            this.a = task;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.lang.Runnable
        public final void run() {
            synchronized (h.this.c) {
                if (h.this.a != null) {
                    h.this.a.onSuccess(this.a.getResult());
                }
            }
        }
    }

    public h(Executor executor, OnSuccessListener<TResult> onSuccessListener) {
        this.a = onSuccessListener;
        this.b = executor;
    }

    @Override // com.huawei.hmf.tasks.ExecuteResult
    public final void onComplete(Task<TResult> task) {
        if (!task.isSuccessful() || task.isCanceled()) {
            return;
        }
        this.b.execute(new AnonymousClass1(task));
    }
}
