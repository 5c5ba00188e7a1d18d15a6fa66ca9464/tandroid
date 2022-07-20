package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultBandwidthMeter$ConnectivityActionReceiver$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DefaultBandwidthMeter.ConnectivityActionReceiver f$0;
    public final /* synthetic */ DefaultBandwidthMeter f$1;

    public /* synthetic */ DefaultBandwidthMeter$ConnectivityActionReceiver$$ExternalSyntheticLambda0(DefaultBandwidthMeter.ConnectivityActionReceiver connectivityActionReceiver, DefaultBandwidthMeter defaultBandwidthMeter) {
        this.f$0 = connectivityActionReceiver;
        this.f$1 = defaultBandwidthMeter;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$register$0(this.f$1);
    }
}
