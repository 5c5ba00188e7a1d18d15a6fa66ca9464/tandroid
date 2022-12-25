package com.google.android.gms.common.api.internal;

import android.os.Looper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Lock;
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* loaded from: classes.dex */
final class zaal implements BaseGmsClient.ConnectionProgressReportCallbacks {
    private final WeakReference zaa;
    private final Api zab;
    private final boolean zac;

    public zaal(zaaw zaawVar, Api api, boolean z) {
        this.zaa = new WeakReference(zaawVar);
        this.zab = api;
        this.zac = z;
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient.ConnectionProgressReportCallbacks
    public final void onReportServiceBinding(ConnectionResult connectionResult) {
        zabi zabiVar;
        Lock lock;
        Lock lock2;
        boolean zaG;
        boolean zaH;
        Lock lock3;
        zaaw zaawVar = (zaaw) this.zaa.get();
        if (zaawVar == null) {
            return;
        }
        Looper myLooper = Looper.myLooper();
        zabiVar = zaawVar.zaa;
        Preconditions.checkState(myLooper == zabiVar.zag.getLooper(), "onReportServiceBinding must be called on the GoogleApiClient handler thread");
        lock = zaawVar.zab;
        lock.lock();
        try {
            zaG = zaawVar.zaG(0);
            if (zaG) {
                if (!connectionResult.isSuccess()) {
                    zaawVar.zaE(connectionResult, this.zab, this.zac);
                }
                zaH = zaawVar.zaH();
                if (zaH) {
                    zaawVar.zaF();
                }
                lock3 = zaawVar.zab;
            } else {
                lock3 = zaawVar.zab;
            }
            lock3.unlock();
        } catch (Throwable th) {
            lock2 = zaawVar.zab;
            lock2.unlock();
            throw th;
        }
    }
}
