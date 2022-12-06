package com.google.android.gms.common.api.internal;

import java.util.concurrent.locks.Lock;
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* loaded from: classes.dex */
abstract class zaav implements Runnable {
    final /* synthetic */ zaaw zab;

    @Override // java.lang.Runnable
    public final void run() {
        Lock lock;
        Lock lock2;
        zabi zabiVar;
        Lock lock3;
        lock = this.zab.zab;
        lock.lock();
        try {
            try {
                if (Thread.interrupted()) {
                    lock3 = this.zab.zab;
                } else {
                    zaa();
                    lock3 = this.zab.zab;
                }
            } catch (RuntimeException e) {
                zabiVar = this.zab.zaa;
                zabiVar.zam(e);
                lock3 = this.zab.zab;
            }
            lock3.unlock();
        } catch (Throwable th) {
            lock2 = this.zab.zab;
            lock2.unlock();
            throw th;
        }
    }

    protected abstract void zaa();
}
