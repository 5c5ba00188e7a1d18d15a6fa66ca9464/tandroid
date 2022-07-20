package org.webrtc;

import org.webrtc.VideoSink;
/* loaded from: classes3.dex */
public final /* synthetic */ class VideoSource$$ExternalSyntheticLambda1 implements VideoSink {
    public final /* synthetic */ VideoSource f$0;

    public /* synthetic */ VideoSource$$ExternalSyntheticLambda1(VideoSource videoSource) {
        this.f$0 = videoSource;
    }

    @Override // org.webrtc.VideoSink
    public final void onFrame(VideoFrame videoFrame) {
        this.f$0.lambda$setVideoProcessor$1(videoFrame);
    }

    @Override // org.webrtc.VideoSink
    public /* synthetic */ void setParentSink(VideoSink videoSink) {
        VideoSink.CC.$default$setParentSink(this, videoSink);
    }
}
