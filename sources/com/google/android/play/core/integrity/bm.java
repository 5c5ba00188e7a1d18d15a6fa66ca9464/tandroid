package com.google.android.play.core.integrity;

import com.google.android.gms.tasks.TaskCompletionSource;

/* loaded from: classes.dex */
abstract class bm extends com.google.android.play.integrity.internal.t {
    final /* synthetic */ bn f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    bm(bn bnVar, TaskCompletionSource taskCompletionSource) {
        super(taskCompletionSource);
        this.f = bnVar;
    }

    @Override // com.google.android.play.integrity.internal.t
    public final void a(Exception exc) {
        if (exc instanceof com.google.android.play.integrity.internal.af) {
            super.a(bn.k(this.f) ? new StandardIntegrityException(-2, exc) : new StandardIntegrityException(-9, exc));
        } else {
            super.a(exc);
        }
    }
}
