package com.google.android.exoplayer2.video;

import android.view.Surface;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
/* loaded from: classes.dex */
public final /* synthetic */ class VideoRendererEventListener$EventDispatcher$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ VideoRendererEventListener.EventDispatcher f$0;
    public final /* synthetic */ Surface f$1;

    public /* synthetic */ VideoRendererEventListener$EventDispatcher$$ExternalSyntheticLambda2(VideoRendererEventListener.EventDispatcher eventDispatcher, Surface surface) {
        this.f$0 = eventDispatcher;
        this.f$1 = surface;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$renderedFirstFrame$5(this.f$1);
    }
}
