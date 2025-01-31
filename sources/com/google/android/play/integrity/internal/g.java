package com.google.android.play.integrity.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;

/* loaded from: classes.dex */
public final class g extends a implements i {
    g(IBinder iBinder) {
        super(iBinder, "com.google.android.play.core.integrity.protocol.IExpressIntegrityService");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.play.integrity.internal.i
    public final void c(Bundle bundle, r rVar) {
        Parcel a = a();
        c.c(a, bundle);
        a.writeStrongBinder(rVar);
        b(6, a);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.play.integrity.internal.i
    public final void d(Bundle bundle, k kVar) {
        Parcel a = a();
        c.c(a, bundle);
        a.writeStrongBinder(kVar);
        b(3, a);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.play.integrity.internal.i
    public final void e(Bundle bundle, k kVar) {
        Parcel a = a();
        c.c(a, bundle);
        a.writeStrongBinder(kVar);
        b(2, a);
    }
}
