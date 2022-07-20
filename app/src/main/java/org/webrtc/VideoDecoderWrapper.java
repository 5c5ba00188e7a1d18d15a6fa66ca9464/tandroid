package org.webrtc;

import org.webrtc.VideoDecoder;
/* loaded from: classes3.dex */
public class VideoDecoderWrapper {
    public static native void nativeOnDecodedFrame(long j, VideoFrame videoFrame, Integer num, Integer num2);

    VideoDecoderWrapper() {
    }

    @CalledByNative
    static VideoDecoder.Callback createDecoderCallback(long j) {
        return new VideoDecoderWrapper$$ExternalSyntheticLambda0(j);
    }
}
