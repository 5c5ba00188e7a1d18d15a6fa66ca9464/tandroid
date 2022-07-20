package com.google.android.gms.common.api.internal;

import android.os.IBinder;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
final /* synthetic */ class zabs implements Runnable {
    private final NonGmsServiceBrokerClient zaa;
    private final IBinder zab;

    public zabs(NonGmsServiceBrokerClient nonGmsServiceBrokerClient, IBinder iBinder) {
        this.zaa = nonGmsServiceBrokerClient;
        this.zab = iBinder;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zaa.zaa(this.zab);
    }
}
