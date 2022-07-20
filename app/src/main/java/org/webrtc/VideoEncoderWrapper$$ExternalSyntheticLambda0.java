package org.webrtc;

import org.webrtc.VideoEncoder;
/* loaded from: classes3.dex */
public final /* synthetic */ class VideoEncoderWrapper$$ExternalSyntheticLambda0 implements VideoEncoder.Callback {
    public final /* synthetic */ long f$0;

    public /* synthetic */ VideoEncoderWrapper$$ExternalSyntheticLambda0(long j) {
        this.f$0 = j;
    }

    @Override // org.webrtc.VideoEncoder.Callback
    public final void onEncodedFrame(EncodedImage encodedImage, VideoEncoder.CodecSpecificInfo codecSpecificInfo) {
        VideoEncoderWrapper.nativeOnEncodedFrame(this.f$0, encodedImage);
    }
}
