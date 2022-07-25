package org.webrtc;

import android.media.MediaCodecInfo;
import org.telegram.messenger.voip.Instance;
import org.telegram.messenger.voip.VoIPService;
import org.webrtc.EglBase;
import org.webrtc.Predicate;
/* loaded from: classes3.dex */
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
        /* JADX WARN: Code restructure failed: missing block: B:46:0x0047, code lost:
            if (r3.equals(org.telegram.messenger.MediaController.VIDEO_MIME_TYPE) == false) goto L14;
         */
        @Override // org.webrtc.Predicate
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean test(MediaCodecInfo mediaCodecInfo) {
            String[] supportedTypes;
            if (!MediaCodecUtils.isHardwareAccelerated(mediaCodecInfo) || (supportedTypes = mediaCodecInfo.getSupportedTypes()) == null || supportedTypes.length == 0) {
                return false;
            }
            Instance.ServerConfig globalServerConfig = Instance.getGlobalServerConfig();
            int i = 0;
            while (true) {
                char c = 1;
                if (i >= supportedTypes.length) {
                    return true;
                }
                String str = supportedTypes[i];
                str.hashCode();
                switch (str.hashCode()) {
                    case -1662541442:
                        if (str.equals("video/hevc")) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1331836730:
                        break;
                    case 1599127256:
                        if (str.equals("video/x-vnd.on2.vp8")) {
                            c = 2;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1599127257:
                        if (str.equals("video/x-vnd.on2.vp9")) {
                            c = 3;
                            break;
                        }
                        c = 65535;
                        break;
                    default:
                        c = 65535;
                        break;
                }
                switch (c) {
                    case 0:
                        return globalServerConfig.enable_h265_decoder;
                    case 1:
                        if (VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().groupCall != null) {
                            return false;
                        }
                        return globalServerConfig.enable_h264_decoder;
                    case 2:
                        if (VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().groupCall != null) {
                            return false;
                        }
                        return globalServerConfig.enable_vp8_decoder;
                    case 3:
                        return globalServerConfig.enable_vp9_decoder;
                    default:
                        i++;
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

    public HardwareVideoDecoderFactory(EglBase.Context context) {
        this(context, null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public HardwareVideoDecoderFactory(EglBase.Context context, Predicate<MediaCodecInfo> predicate) {
        super(context, r3);
        Predicate<MediaCodecInfo> and;
        if (predicate == null) {
            and = defaultAllowedPredicate;
        } else {
            and = predicate.and(defaultAllowedPredicate);
        }
    }
}
