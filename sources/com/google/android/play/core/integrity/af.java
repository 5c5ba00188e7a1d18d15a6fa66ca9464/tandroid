package com.google.android.play.core.integrity;

import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.play.integrity.internal.n;
import com.google.android.play.integrity.internal.t;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class af extends t {
    final /* synthetic */ byte[] a;
    final /* synthetic */ Long b;
    final /* synthetic */ TaskCompletionSource c;
    final /* synthetic */ IntegrityTokenRequest d;
    final /* synthetic */ aj e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public af(aj ajVar, TaskCompletionSource taskCompletionSource, byte[] bArr, Long l, Parcelable parcelable, TaskCompletionSource taskCompletionSource2, IntegrityTokenRequest integrityTokenRequest) {
        super(taskCompletionSource);
        this.e = ajVar;
        this.a = bArr;
        this.b = l;
        this.c = taskCompletionSource2;
        this.d = integrityTokenRequest;
    }

    @Override // com.google.android.play.integrity.internal.t
    public final void a(Exception exc) {
        if (exc instanceof com.google.android.play.integrity.internal.af) {
            super.a(new IntegrityServiceException(-9, exc));
        } else {
            super.a(exc);
        }
    }

    @Override // com.google.android.play.integrity.internal.t
    protected final void b() {
        com.google.android.play.integrity.internal.s sVar;
        try {
            ((n) this.e.a.e()).d(aj.a(this.e, this.a, this.b, null), new ai(this.e, this.c));
        } catch (RemoteException e) {
            aj ajVar = this.e;
            IntegrityTokenRequest integrityTokenRequest = this.d;
            sVar = ajVar.b;
            sVar.c(e, "requestIntegrityToken(%s)", integrityTokenRequest);
            this.c.trySetException(new IntegrityServiceException(-100, e));
        }
    }
}
