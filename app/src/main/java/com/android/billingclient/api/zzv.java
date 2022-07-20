package com.android.billingclient.api;
/* compiled from: com.android.billingclient:billing@@5.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzv implements Runnable {
    public final /* synthetic */ ConsumeResponseListener zza;
    public final /* synthetic */ ConsumeParams zzb;

    public /* synthetic */ zzv(ConsumeResponseListener consumeResponseListener, ConsumeParams consumeParams) {
        this.zza = consumeResponseListener;
        this.zzb = consumeParams;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zza.onConsumeResponse(zzbb.zzn, this.zzb.getPurchaseToken());
    }
}
