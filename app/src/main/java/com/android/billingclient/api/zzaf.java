package com.android.billingclient.api;

import com.google.android.gms.internal.play_billing.zzb;
import java.util.concurrent.Future;
/* compiled from: com.android.billingclient:billing@@5.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzaf implements Runnable {
    public final /* synthetic */ Future zza;
    public final /* synthetic */ Runnable zzb;

    public /* synthetic */ zzaf(Future future, Runnable runnable) {
        this.zza = future;
        this.zzb = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Future future = this.zza;
        Runnable runnable = this.zzb;
        if (future.isDone() || future.isCancelled()) {
            return;
        }
        future.cancel(true);
        zzb.zzo("BillingClient", "Async task is taking too long, cancel it!");
        if (runnable == null) {
            return;
        }
        runnable.run();
    }
}
