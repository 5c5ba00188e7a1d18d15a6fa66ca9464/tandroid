package com.google.android.play.integrity.internal;

import android.os.IBinder;
import android.os.IInterface;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
final class aa extends t {
    final /* synthetic */ IBinder a;
    final /* synthetic */ ad b;

    aa(ad adVar, IBinder iBinder) {
        this.b = adVar;
        this.a = iBinder;
    }

    @Override // com.google.android.play.integrity.internal.t
    public final void b() {
        z zVar;
        List list;
        List list2;
        zVar = this.b.a.j;
        this.b.a.o = (IInterface) zVar.a(this.a);
        ae.r(this.b.a);
        this.b.a.h = false;
        list = this.b.a.e;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ((Runnable) it.next()).run();
        }
        list2 = this.b.a.e;
        list2.clear();
    }
}
