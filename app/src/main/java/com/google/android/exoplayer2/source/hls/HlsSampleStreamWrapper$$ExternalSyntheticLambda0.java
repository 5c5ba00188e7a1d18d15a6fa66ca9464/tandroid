package com.google.android.exoplayer2.source.hls;

import com.google.android.exoplayer2.source.hls.HlsSampleStreamWrapper;
/* loaded from: classes.dex */
public final /* synthetic */ class HlsSampleStreamWrapper$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ HlsSampleStreamWrapper.Callback f$0;

    public /* synthetic */ HlsSampleStreamWrapper$$ExternalSyntheticLambda0(HlsSampleStreamWrapper.Callback callback) {
        this.f$0 = callback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.onPrepared();
    }
}
