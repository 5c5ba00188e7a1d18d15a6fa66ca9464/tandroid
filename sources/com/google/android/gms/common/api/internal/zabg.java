package com.google.android.gms.common.api.internal;

import java.util.concurrent.locks.Lock;
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* loaded from: classes.dex */
abstract class zabg {
    private final zabf zaa;

    /* JADX INFO: Access modifiers changed from: protected */
    public zabg(zabf zabfVar) {
        this.zaa = zabfVar;
    }

    protected abstract void zaa();

    public final void zab(zabi zabiVar) {
        Lock lock;
        Lock lock2;
        zabf zabfVar;
        Lock lock3;
        lock = zabiVar.zai;
        lock.lock();
        try {
            zabfVar = zabiVar.zan;
            if (zabfVar != this.zaa) {
                lock3 = zabiVar.zai;
            } else {
                zaa();
                lock3 = zabiVar.zai;
            }
            lock3.unlock();
        } catch (Throwable th) {
            lock2 = zabiVar.zai;
            lock2.unlock();
            throw th;
        }
    }
}
