package com.google.android.play.core.integrity;

import android.os.RemoteException;
import com.google.android.gms.tasks.TaskCompletionSource;

/* loaded from: classes.dex */
final class bf extends bm {
    final /* synthetic */ long a;
    final /* synthetic */ TaskCompletionSource b;
    final /* synthetic */ bn c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    bf(bn bnVar, TaskCompletionSource taskCompletionSource, int i, long j, TaskCompletionSource taskCompletionSource2) {
        super(bnVar, taskCompletionSource);
        this.c = bnVar;
        this.a = j;
        this.b = taskCompletionSource2;
    }

    @Override // com.google.android.play.integrity.internal.t
    protected final void b() {
        com.google.android.play.integrity.internal.s sVar;
        if (bn.k(this.c)) {
            super.a(new StandardIntegrityException(-2, null));
            return;
        }
        try {
            bn bnVar = this.c;
            ((com.google.android.play.integrity.internal.i) bnVar.a.e()).e(bn.b(bnVar, this.a, 0), new bl(this.c, this.b));
        } catch (RemoteException e) {
            bn bnVar2 = this.c;
            long j = this.a;
            sVar = bnVar2.b;
            sVar.c(e, "warmUpIntegrityToken(%s)", Long.valueOf(j));
            this.b.trySetException(new StandardIntegrityException(-100, e));
        }
    }
}
