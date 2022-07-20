package org.webrtc;

import org.webrtc.Camera2Session;
import org.webrtc.VideoSink;
/* loaded from: classes3.dex */
public final /* synthetic */ class Camera2Session$CaptureSessionCallback$$ExternalSyntheticLambda0 implements VideoSink {
    public final /* synthetic */ Camera2Session.CaptureSessionCallback f$0;

    public /* synthetic */ Camera2Session$CaptureSessionCallback$$ExternalSyntheticLambda0(Camera2Session.CaptureSessionCallback captureSessionCallback) {
        this.f$0 = captureSessionCallback;
    }

    @Override // org.webrtc.VideoSink
    public final void onFrame(VideoFrame videoFrame) {
        this.f$0.lambda$onConfigured$0(videoFrame);
    }

    @Override // org.webrtc.VideoSink
    public /* synthetic */ void setParentSink(VideoSink videoSink) {
        VideoSink.CC.$default$setParentSink(this, videoSink);
    }
}
