package com.google.android.play.integrity.internal;

import android.content.Context;
import android.content.ServiceConnection;
import android.os.IInterface;
import java.util.concurrent.atomic.AtomicInteger;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.play:integrity@@1.3.0 */
/* loaded from: classes.dex */
public final class x extends t {
    final /* synthetic */ ae a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public x(ae aeVar) {
        this.a = aeVar;
    }

    @Override // com.google.android.play.integrity.internal.t
    public final void b() {
        Object obj;
        AtomicInteger atomicInteger;
        IInterface iInterface;
        s sVar;
        Context context;
        ServiceConnection serviceConnection;
        AtomicInteger atomicInteger2;
        s sVar2;
        obj = this.a.g;
        synchronized (obj) {
            try {
                atomicInteger = this.a.m;
                if (atomicInteger.get() > 0) {
                    atomicInteger2 = this.a.m;
                    if (atomicInteger2.decrementAndGet() > 0) {
                        sVar2 = this.a.c;
                        sVar2.d("Leaving the connection open for other ongoing calls.", new Object[0]);
                        return;
                    }
                }
                ae aeVar = this.a;
                iInterface = aeVar.o;
                if (iInterface != null) {
                    sVar = aeVar.c;
                    sVar.d("Unbind from service.", new Object[0]);
                    ae aeVar2 = this.a;
                    context = aeVar2.b;
                    serviceConnection = aeVar2.n;
                    context.unbindService(serviceConnection);
                    this.a.h = false;
                    this.a.o = null;
                    this.a.n = null;
                }
                this.a.x();
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
