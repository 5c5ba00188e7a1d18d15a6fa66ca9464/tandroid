package com.google.android.play.core.integrity;

import android.os.Bundle;
import com.google.android.gms.tasks.TaskCompletionSource;

/* loaded from: classes.dex */
class bi extends com.google.android.play.integrity.internal.j {
    final TaskCompletionSource a;
    final /* synthetic */ bn b;

    bi(bn bnVar, TaskCompletionSource taskCompletionSource) {
        this.b = bnVar;
        this.a = taskCompletionSource;
    }

    @Override // com.google.android.play.integrity.internal.k
    public final void b(Bundle bundle) {
        this.b.a.v(this.a);
    }

    @Override // com.google.android.play.integrity.internal.k
    public void c(Bundle bundle) {
        this.b.a.v(this.a);
    }

    @Override // com.google.android.play.integrity.internal.k
    public final void d(Bundle bundle) {
        this.b.a.v(this.a);
    }

    @Override // com.google.android.play.integrity.internal.k
    public void e(Bundle bundle) {
        this.b.a.v(this.a);
    }
}
