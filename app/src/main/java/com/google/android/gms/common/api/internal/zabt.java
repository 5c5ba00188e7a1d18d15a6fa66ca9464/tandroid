package com.google.android.gms.common.api.internal;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
final /* synthetic */ class zabt implements Runnable {
    private final NonGmsServiceBrokerClient zaa;

    public zabt(NonGmsServiceBrokerClient nonGmsServiceBrokerClient) {
        this.zaa = nonGmsServiceBrokerClient;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zaa.zaa();
    }
}
