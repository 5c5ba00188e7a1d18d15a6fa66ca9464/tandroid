package com.google.android.play.integrity.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;

/* loaded from: classes.dex */
public final class l extends a implements n {
    l(IBinder iBinder) {
        super(iBinder, "com.google.android.play.core.integrity.protocol.IIntegrityService");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.play.integrity.internal.n
    public final void c(Bundle bundle, r rVar) {
        Parcel a = a();
        c.c(a, bundle);
        a.writeStrongBinder(rVar);
        b(3, a);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.play.integrity.internal.n
    public final void d(Bundle bundle, p pVar) {
        Parcel a = a();
        c.c(a, bundle);
        a.writeStrongBinder(pVar);
        b(2, a);
    }
}
