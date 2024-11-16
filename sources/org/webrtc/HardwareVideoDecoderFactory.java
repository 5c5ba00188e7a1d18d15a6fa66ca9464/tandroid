package org.webrtc;

import android.media.MediaCodecInfo;
import org.telegram.messenger.voip.Instance;
import org.telegram.messenger.voip.VoIPService;
import org.webrtc.EglBase;
import org.webrtc.Predicate;

/* loaded from: classes5.dex */
public class HardwareVideoDecoderFactory extends MediaCodecVideoDecoderFactory {
    private static final Predicate<MediaCodecInfo> defaultAllowedPredicate = new Predicate<MediaCodecInfo>() { // from class: org.webrtc.HardwareVideoDecoderFactory.1
        @Override // org.webrtc.Predicate
        public /* synthetic */ Predicate<MediaCodecInfo> and(Predicate<? super MediaCodecInfo> predicate) {
            return Predicate.-CC.$default$and(this, predicate);
        }

        @Override // org.webrtc.Predicate
        public /* synthetic */ Predicate<MediaCodecInfo> negate() {
            return Predicate.-CC.$default$negate(this);
        }

        @Override // org.webrtc.Predicate
        public /* synthetic */ Predicate<MediaCodecInfo> or(Predicate<? super MediaCodecInfo> predicate) {
            return Predicate.-CC.$default$or(this, predicate);
        }

        /* JADX WARN: Code restructure failed: missing block: B:46:0x0047, code lost:
        
            if (r3.equals(org.telegram.messenger.MediaController.VIDEO_MIME_TYPE) == false) goto L15;
         */
        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
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
                        break;
                    case 1331836730:
                        break;
                    case 1599127256:
                        if (str.equals("video/x-vnd.on2.vp8")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 1599127257:
                        if (str.equals("video/x-vnd.on2.vp9")) {
                            c = 3;
                            break;
                        }
                        break;
                }
                c = 65535;
                switch (c) {
                    case 0:
                        return globalServerConfig.enable_h265_decoder;
                    case 1:
                        if (VoIPService.getSharedInstance() == null || VoIPService.getSharedInstance().groupCall == null) {
                            return globalServerConfig.enable_h264_decoder;
                        }
                        return false;
                    case 2:
                        if (VoIPService.getSharedInstance() == null || VoIPService.getSharedInstance().groupCall == null) {
                            return globalServerConfig.enable_vp8_decoder;
                        }
                        return false;
                    case 3:
                        return globalServerConfig.enable_vp9_decoder;
                    default:
                        i++;
                }
            }
        }
    };

    @Deprecated
    public HardwareVideoDecoderFactory() {
        this(null);
    }

    public HardwareVideoDecoderFactory(EglBase.Context context) {
        this(context, null);
    }

    public HardwareVideoDecoderFactory(EglBase.Context context, Predicate<MediaCodecInfo> predicate) {
        super(context, predicate == null ? defaultAllowedPredicate : predicate.and(defaultAllowedPredicate));
    }

    @Override // org.webrtc.MediaCodecVideoDecoderFactory, org.webrtc.VideoDecoderFactory
    public /* bridge */ /* synthetic */ VideoDecoder createDecoder(VideoCodecInfo videoCodecInfo) {
        return super.createDecoder(videoCodecInfo);
    }

    @Override // org.webrtc.MediaCodecVideoDecoderFactory, org.webrtc.VideoDecoderFactory
    public /* bridge */ /* synthetic */ VideoCodecInfo[] getSupportedCodecs() {
        return super.getSupportedCodecs();
    }
}
