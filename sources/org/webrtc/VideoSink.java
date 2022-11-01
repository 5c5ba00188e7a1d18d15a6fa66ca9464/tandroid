package org.webrtc;
/* loaded from: classes.dex */
public interface VideoSink {

    /* loaded from: classes.dex */
    public final /* synthetic */ class -CC {
        public static void $default$setParentSink(VideoSink videoSink, VideoSink videoSink2) {
        }
    }

    @CalledByNative
    void onFrame(VideoFrame videoFrame);

    void setParentSink(VideoSink videoSink);
}
