package com.google.android.play.core.integrity;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.gms.tasks.TaskCompletionSource;

/* loaded from: classes.dex */
final class bh extends bm {
    final /* synthetic */ Bundle a;
    final /* synthetic */ Activity b;
    final /* synthetic */ TaskCompletionSource c;
    final /* synthetic */ int d;
    final /* synthetic */ bn e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    bh(bn bnVar, TaskCompletionSource taskCompletionSource, Bundle bundle, Activity activity, TaskCompletionSource taskCompletionSource2, int i) {
        super(bnVar, taskCompletionSource);
        this.e = bnVar;
        this.a = bundle;
        this.b = activity;
        this.c = taskCompletionSource2;
        this.d = i;
    }

    @Override // com.google.android.play.integrity.internal.t
    protected final void b() {
        com.google.android.play.integrity.internal.s sVar;
        at atVar;
        if (bn.k(this.e)) {
            super.a(new StandardIntegrityException(-2, null));
            return;
        }
        try {
            bn bnVar = this.e;
            com.google.android.play.integrity.internal.ae aeVar = bnVar.a;
            com.google.android.play.integrity.internal.i iVar = (com.google.android.play.integrity.internal.i) aeVar.e();
            Bundle bundle = this.a;
            atVar = bnVar.e;
            iVar.c(bundle, atVar.a(this.b, this.c, aeVar));
        } catch (RemoteException e) {
            bn bnVar2 = this.e;
            int i = this.d;
            sVar = bnVar2.b;
            sVar.c(e, "requestAndShowDialog(%s)", Integer.valueOf(i));
            this.c.trySetException(new StandardIntegrityException(-100, e));
        }
    }
}
