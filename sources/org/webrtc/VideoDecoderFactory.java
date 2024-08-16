package org.webrtc;
/* loaded from: classes.dex */
public interface VideoDecoderFactory {
    @Deprecated
    VideoDecoder createDecoder(String str);

    @CalledByNative
    VideoDecoder createDecoder(VideoCodecInfo videoCodecInfo);

    @CalledByNative
    VideoCodecInfo[] getSupportedCodecs();

    /* loaded from: classes.dex */
    public final /* synthetic */ class -CC {
        @Deprecated
        public static VideoDecoder $default$createDecoder(VideoDecoderFactory videoDecoderFactory, String str) {
            throw new UnsupportedOperationException("Deprecated and not implemented.");
        }

        @CalledByNative
        public static VideoCodecInfo[] $default$getSupportedCodecs(VideoDecoderFactory videoDecoderFactory) {
            return new VideoCodecInfo[0];
        }
    }
}
