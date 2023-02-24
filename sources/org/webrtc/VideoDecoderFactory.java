package org.webrtc;
/* loaded from: classes4.dex */
public interface VideoDecoderFactory {
    @Deprecated
    VideoDecoder createDecoder(String str);

    @CalledByNative
    VideoDecoder createDecoder(VideoCodecInfo videoCodecInfo);

    @CalledByNative
    VideoCodecInfo[] getSupportedCodecs();

    /* loaded from: classes4.dex */
    public final /* synthetic */ class -CC {
        @CalledByNative
        public static VideoCodecInfo[] $default$getSupportedCodecs(VideoDecoderFactory videoDecoderFactory) {
            return new VideoCodecInfo[0];
        }

        @Deprecated
        public static VideoDecoder $default$createDecoder(VideoDecoderFactory _this, String str) {
            throw new UnsupportedOperationException("Deprecated and not implemented.");
        }
    }
}
