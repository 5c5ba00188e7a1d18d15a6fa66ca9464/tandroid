package org.webrtc;

/* loaded from: classes5.dex */
public interface VideoSink {

    /* loaded from: classes5.dex */
    public abstract /* synthetic */ class -CC {
        public static void $default$setParentSink(VideoSink videoSink, VideoSink videoSink2) {
        }
    }

    void onFrame(VideoFrame videoFrame);

    void setParentSink(VideoSink videoSink);
}
