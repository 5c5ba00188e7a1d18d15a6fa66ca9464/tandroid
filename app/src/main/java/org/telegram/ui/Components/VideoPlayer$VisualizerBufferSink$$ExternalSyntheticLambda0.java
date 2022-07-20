package org.telegram.ui.Components;

import org.telegram.ui.Components.VideoPlayer;
/* loaded from: classes3.dex */
public final /* synthetic */ class VideoPlayer$VisualizerBufferSink$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ VideoPlayer.VisualizerBufferSink f$0;

    public /* synthetic */ VideoPlayer$VisualizerBufferSink$$ExternalSyntheticLambda0(VideoPlayer.VisualizerBufferSink visualizerBufferSink) {
        this.f$0 = visualizerBufferSink;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$handleBuffer$0();
    }
}
