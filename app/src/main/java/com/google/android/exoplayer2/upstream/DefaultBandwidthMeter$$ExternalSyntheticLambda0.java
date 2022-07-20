package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.util.EventDispatcher;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultBandwidthMeter$$ExternalSyntheticLambda0 implements EventDispatcher.Event {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ DefaultBandwidthMeter$$ExternalSyntheticLambda0(int i, long j, long j2) {
        this.f$0 = i;
        this.f$1 = j;
        this.f$2 = j2;
    }

    @Override // com.google.android.exoplayer2.util.EventDispatcher.Event
    public final void sendTo(Object obj) {
        ((BandwidthMeter.EventListener) obj).onBandwidthSample(this.f$0, this.f$1, this.f$2);
    }
}
