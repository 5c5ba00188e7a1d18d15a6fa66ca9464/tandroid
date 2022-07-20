package com.huawei.hmf.tasks.a;

import com.huawei.hmf.tasks.ExecuteResult;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.Task;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class d<TResult> implements ExecuteResult<TResult> {
    Executor a;
    private OnCompleteListener<TResult> b;
    private final Object c = new Object();

    /* renamed from: com.huawei.hmf.tasks.a.d$1 */
    /* loaded from: classes.dex */
    final class AnonymousClass1 implements Runnable {
        final /* synthetic */ Task a;

        AnonymousClass1(Task task) {
            d.this = r1;
            this.a = task;
        }

        @Override // java.lang.Runnable
        public final void run() {
            synchronized (d.this.c) {
                if (d.this.b != null) {
                    d.this.b.onComplete(this.a);
                }
            }
        }
    }

    public d(Executor executor, OnCompleteListener<TResult> onCompleteListener) {
        this.b = onCompleteListener;
        this.a = executor;
    }

    @Override // com.huawei.hmf.tasks.ExecuteResult
    public final void onComplete(Task<TResult> task) {
        this.a.execute(new AnonymousClass1(task));
    }
}
