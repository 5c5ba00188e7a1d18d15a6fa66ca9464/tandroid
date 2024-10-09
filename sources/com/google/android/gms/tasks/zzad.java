package com.google.android.gms.tasks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
final class zzad implements zzae {
    private final CountDownLatch zza = new CountDownLatch(1);

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzad(zzac zzacVar) {
    }

    @Override // com.google.android.gms.tasks.OnCanceledListener
    public final void onCanceled() {
        this.zza.countDown();
    }

    @Override // com.google.android.gms.tasks.OnFailureListener
    public final void onFailure(Exception exc) {
        this.zza.countDown();
    }

    @Override // com.google.android.gms.tasks.OnSuccessListener
    public final void onSuccess(Object obj) {
        this.zza.countDown();
    }

    public final void zza() {
        this.zza.await();
    }

    public final boolean zzb(long j, TimeUnit timeUnit) {
        return this.zza.await(j, timeUnit);
    }
}
