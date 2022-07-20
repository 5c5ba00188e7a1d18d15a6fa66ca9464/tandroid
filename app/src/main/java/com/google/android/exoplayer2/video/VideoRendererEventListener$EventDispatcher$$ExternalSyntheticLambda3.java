package com.google.android.exoplayer2.video;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
/* loaded from: classes.dex */
public final /* synthetic */ class VideoRendererEventListener$EventDispatcher$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ VideoRendererEventListener.EventDispatcher f$0;
    public final /* synthetic */ Format f$1;

    public /* synthetic */ VideoRendererEventListener$EventDispatcher$$ExternalSyntheticLambda3(VideoRendererEventListener.EventDispatcher eventDispatcher, Format format) {
        this.f$0 = eventDispatcher;
        this.f$1 = format;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$inputFormatChanged$2(this.f$1);
    }
}
