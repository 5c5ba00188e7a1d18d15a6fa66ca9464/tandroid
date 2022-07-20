package com.android.billingclient.api;

import java.util.ArrayList;
/* compiled from: com.android.billingclient:billing@@5.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzt implements Runnable {
    public final /* synthetic */ ProductDetailsResponseListener zza;

    @Override // java.lang.Runnable
    public final void run() {
        this.zza.onProductDetailsResponse(zzbb.zzn, new ArrayList());
    }
}
