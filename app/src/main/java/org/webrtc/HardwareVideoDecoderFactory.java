package org.webrtc;

import android.media.MediaCodecInfo;
import com.google.android.exoplayer2.util.MimeTypes;
import org.telegram.messenger.voip.Instance;
import org.telegram.messenger.voip.VoIPService;
import org.webrtc.EglBase;
import org.webrtc.Predicate;
/* loaded from: classes5.dex */
public class HardwareVideoDecoderFactory extends MediaCodecVideoDecoderFactory {
    private static final Predicate<MediaCodecInfo> defaultAllowedPredicate = new Predicate<MediaCodecInfo>() { // from class: org.webrtc.HardwareVideoDecoderFactory.1
        @Override // org.webrtc.Predicate
        public /* synthetic */ Predicate<MediaCodecInfo> and(Predicate<? super MediaCodecInfo> predicate) {
            return Predicate.CC.$default$and(this, predicate);
        }

        @Override // org.webrtc.Predicate
        public /* synthetic */ Predicate<MediaCodecInfo> negate() {
            return Predicate.CC.$default$negate(this);
        }

        @Override // org.webrtc.Predicate
        public /* synthetic */ Predicate<MediaCodecInfo> or(Predicate<? super MediaCodecInfo> predicate) {
            return Predicate.CC.$default$or(this, predicate);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:15:0x002d, code lost:
            if (r4.equals(com.google.android.exoplayer2.util.MimeTypes.VIDEO_VP9) != false) goto L26;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean test(MediaCodecInfo arg) {
            String[] types;
            if (!MediaCodecUtils.isHardwareAccelerated(arg) || (types = arg.getSupportedTypes()) == null || types.length == 0) {
                return false;
            }
            Instance.ServerConfig config = Instance.getGlobalServerConfig();
            int a = 0;
            while (true) {
                char c = 1;
                if (a >= types.length) {
                    return true;
                }
                String str = types[a];
                switch (str.hashCode()) {
                    case -1662541442:
                        if (str.equals(MimeTypes.VIDEO_H265)) {
                            c = 3;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1331836730:
                        if (str.equals("video/avc")) {
                            c = 2;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1599127256:
                        if (str.equals(MimeTypes.VIDEO_VP8)) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1599127257:
                        break;
                    default:
                        c = 65535;
                        break;
                }
                switch (c) {
                    case 0:
                        if (VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().groupCall != null) {
                            return false;
                        }
                        return config.enable_vp8_decoder;
                    case 1:
                        return config.enable_vp9_decoder;
                    case 2:
                        if (VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().groupCall != null) {
                            return false;
                        }
                        return config.enable_h264_decoder;
                    case 3:
                        return config.enable_h265_decoder;
                    default:
                        a++;
                }
            }
        }
    };

    @Override // org.webrtc.MediaCodecVideoDecoderFactory, org.webrtc.VideoDecoderFactory
    public /* bridge */ /* synthetic */ VideoDecoder createDecoder(VideoCodecInfo videoCodecInfo) {
        return super.createDecoder(videoCodecInfo);
    }

    @Override // org.webrtc.MediaCodecVideoDecoderFactory, org.webrtc.VideoDecoderFactory
    public /* bridge */ /* synthetic */ VideoCodecInfo[] getSupportedCodecs() {
        return super.getSupportedCodecs();
    }

    @Deprecated
    public HardwareVideoDecoderFactory() {
        this(null);
    }

    public HardwareVideoDecoderFactory(EglBase.Context sharedContext) {
        this(sharedContext, null);
    }

    public HardwareVideoDecoderFactory(EglBase.Context sharedContext, Predicate<MediaCodecInfo> codecAllowedPredicate) {
        super(sharedContext, codecAllowedPredicate == null ? defaultAllowedPredicate : codecAllowedPredicate.and(defaultAllowedPredicate));
    }
}
