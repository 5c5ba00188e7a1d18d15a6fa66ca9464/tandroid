package org.webrtc;
/* loaded from: classes3.dex */
public final /* synthetic */ class VideoSource$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ VideoSource f$0;
    public final /* synthetic */ VideoFrame f$1;

    public /* synthetic */ VideoSource$$ExternalSyntheticLambda0(VideoSource videoSource, VideoFrame videoFrame) {
        this.f$0 = videoSource;
        this.f$1 = videoFrame;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setVideoProcessor$0(this.f$1);
    }
}
