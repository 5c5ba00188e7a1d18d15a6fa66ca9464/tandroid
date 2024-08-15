package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.ref.WeakReference;
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* loaded from: classes.dex */
final class zacy implements Runnable {
    final /* synthetic */ Result zaa;
    final /* synthetic */ zada zab;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zacy(zada zadaVar, Result result) {
        this.zab = zadaVar;
        this.zaa = result;
    }

    @Override // java.lang.Runnable
    public final void run() {
        WeakReference weakReference;
        ResultTransform resultTransform;
        try {
            try {
                BasePendingResult.zaa.set(Boolean.TRUE);
                resultTransform = this.zab.zaa;
                ((ResultTransform) Preconditions.checkNotNull(resultTransform)).onSuccess(this.zaa);
                zada zadaVar = this.zab;
                zada.zab(zadaVar);
                zada.zab(zadaVar);
                throw null;
            } catch (RuntimeException unused) {
                zada zadaVar2 = this.zab;
                zada.zab(zadaVar2);
                zada.zab(zadaVar2);
                throw null;
            }
        } catch (Throwable th) {
            BasePendingResult.zaa.set(Boolean.FALSE);
            zada zadaVar3 = this.zab;
            zada.zan(this.zaa);
            weakReference = this.zab.zag;
            GoogleApiClient googleApiClient = (GoogleApiClient) weakReference.get();
            if (googleApiClient != null) {
                googleApiClient.zap(this.zab);
            }
            throw th;
        }
    }
}
