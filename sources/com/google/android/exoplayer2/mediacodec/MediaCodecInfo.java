package com.google.android.exoplayer2.mediacodec;

import android.graphics.Point;
import android.media.MediaCodecInfo;
import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderReuseEvaluation;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.util.List;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.MediaController;
/* loaded from: classes.dex */
public final class MediaCodecInfo {
    public final boolean adaptive;
    public final MediaCodecInfo.CodecCapabilities capabilities;
    public final String codecMimeType;
    public final boolean hardwareAccelerated;
    private final boolean isVideo;
    public final String mimeType;
    public final String name;
    public final boolean secure;

    public static MediaCodecInfo newInstance(String str, String str2, String str3, MediaCodecInfo.CodecCapabilities codecCapabilities, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        return new MediaCodecInfo(str, str2, str3, codecCapabilities, z, z2, z3, (z4 || codecCapabilities == null || !isAdaptive(codecCapabilities) || needsDisableAdaptationWorkaround(str)) ? false : true, codecCapabilities != null && isTunneling(codecCapabilities), z5 || (codecCapabilities != null && isSecure(codecCapabilities)));
    }

    MediaCodecInfo(String str, String str2, String str3, MediaCodecInfo.CodecCapabilities codecCapabilities, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) {
        this.name = (String) Assertions.checkNotNull(str);
        this.mimeType = str2;
        this.codecMimeType = str3;
        this.capabilities = codecCapabilities;
        this.hardwareAccelerated = z;
        this.adaptive = z4;
        this.secure = z6;
        this.isVideo = MimeTypes.isVideo(str2);
    }

    public String toString() {
        return this.name;
    }

    public MediaCodecInfo.CodecProfileLevel[] getProfileLevels() {
        MediaCodecInfo.CodecProfileLevel[] codecProfileLevelArr;
        MediaCodecInfo.CodecCapabilities codecCapabilities = this.capabilities;
        return (codecCapabilities == null || (codecProfileLevelArr = codecCapabilities.profileLevels) == null) ? new MediaCodecInfo.CodecProfileLevel[0] : codecProfileLevelArr;
    }

    public boolean isFormatSupported(Format format) throws MediaCodecUtil.DecoderQueryException {
        int i;
        if (isSampleMimeTypeSupported(format) && isCodecProfileAndLevelSupported(format, true)) {
            if (this.isVideo) {
                int i2 = format.width;
                if (i2 <= 0 || (i = format.height) <= 0) {
                    return true;
                }
                if (Util.SDK_INT >= 21) {
                    return isVideoSizeAndRateSupportedV21(i2, i, format.frameRate);
                }
                boolean z = i2 * i <= MediaCodecUtil.maxH264DecodableFrameSize();
                if (!z) {
                    logNoSupport("legacyFrameSize, " + format.width + "x" + format.height);
                }
                return z;
            }
            if (Util.SDK_INT >= 21) {
                int i3 = format.sampleRate;
                if (i3 != -1 && !isAudioSampleRateSupportedV21(i3)) {
                    return false;
                }
                int i4 = format.channelCount;
                if (i4 != -1 && !isAudioChannelCountSupportedV21(i4)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isFormatFunctionallySupported(Format format) {
        return isSampleMimeTypeSupported(format) && isCodecProfileAndLevelSupported(format, false);
    }

    private boolean isSampleMimeTypeSupported(Format format) {
        return this.mimeType.equals(format.sampleMimeType) || this.mimeType.equals(MediaCodecUtil.getAlternativeCodecMimeType(format));
    }

    private boolean isCodecProfileAndLevelSupported(Format format, boolean z) {
        Pair<Integer, Integer> codecProfileAndLevel = MediaCodecUtil.getCodecProfileAndLevel(format);
        if (codecProfileAndLevel == null) {
            return true;
        }
        int intValue = ((Integer) codecProfileAndLevel.first).intValue();
        int intValue2 = ((Integer) codecProfileAndLevel.second).intValue();
        if ("video/dolby-vision".equals(format.sampleMimeType)) {
            if (MediaController.VIDEO_MIME_TYPE.equals(this.mimeType)) {
                intValue = 8;
            } else {
                intValue = "video/hevc".equals(this.mimeType) ? 2 : 2;
            }
            intValue2 = 0;
        }
        if (this.isVideo || intValue == 42) {
            MediaCodecInfo.CodecProfileLevel[] profileLevels = getProfileLevels();
            if (Util.SDK_INT <= 23 && "video/x-vnd.on2.vp9".equals(this.mimeType) && profileLevels.length == 0) {
                profileLevels = estimateLegacyVp9ProfileLevels(this.capabilities);
            }
            for (MediaCodecInfo.CodecProfileLevel codecProfileLevel : profileLevels) {
                if (codecProfileLevel.profile == intValue && ((codecProfileLevel.level >= intValue2 || !z) && !needsProfileExcludedWorkaround(this.mimeType, intValue))) {
                    return true;
                }
            }
            logNoSupport("codec.profileLevel, " + format.codecs + ", " + this.codecMimeType);
            return false;
        }
        return true;
    }

    public boolean isHdr10PlusOutOfBandMetadataSupported() {
        if (Util.SDK_INT >= 29 && "video/x-vnd.on2.vp9".equals(this.mimeType)) {
            for (MediaCodecInfo.CodecProfileLevel codecProfileLevel : getProfileLevels()) {
                if (codecProfileLevel.profile == 16384) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSeamlessAdaptationSupported(Format format) {
        if (this.isVideo) {
            return this.adaptive;
        }
        Pair<Integer, Integer> codecProfileAndLevel = MediaCodecUtil.getCodecProfileAndLevel(format);
        return codecProfileAndLevel != null && ((Integer) codecProfileAndLevel.first).intValue() == 42;
    }

    public DecoderReuseEvaluation canReuseCodec(Format format, Format format2) {
        int i = !Util.areEqual(format.sampleMimeType, format2.sampleMimeType) ? 8 : 0;
        if (this.isVideo) {
            if (format.rotationDegrees != format2.rotationDegrees) {
                i |= 1024;
            }
            if (!this.adaptive && (format.width != format2.width || format.height != format2.height)) {
                i |= LiteMode.FLAG_CALLS_ANIMATIONS;
            }
            if (!Util.areEqual(format.colorInfo, format2.colorInfo)) {
                i |= LiteMode.FLAG_AUTOPLAY_GIFS;
            }
            if (needsAdaptationReconfigureWorkaround(this.name) && !format.initializationDataEquals(format2)) {
                i |= 2;
            }
            if (i == 0) {
                return new DecoderReuseEvaluation(this.name, format, format2, format.initializationDataEquals(format2) ? 3 : 2, 0);
            }
        } else {
            if (format.channelCount != format2.channelCount) {
                i |= LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM;
            }
            if (format.sampleRate != format2.sampleRate) {
                i |= LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM;
            }
            if (format.pcmEncoding != format2.pcmEncoding) {
                i |= LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM;
            }
            if (i == 0 && MediaController.AUIDO_MIME_TYPE.equals(this.mimeType)) {
                Pair<Integer, Integer> codecProfileAndLevel = MediaCodecUtil.getCodecProfileAndLevel(format);
                Pair<Integer, Integer> codecProfileAndLevel2 = MediaCodecUtil.getCodecProfileAndLevel(format2);
                if (codecProfileAndLevel != null && codecProfileAndLevel2 != null) {
                    int intValue = ((Integer) codecProfileAndLevel.first).intValue();
                    int intValue2 = ((Integer) codecProfileAndLevel2.first).intValue();
                    if (intValue == 42 && intValue2 == 42) {
                        return new DecoderReuseEvaluation(this.name, format, format2, 3, 0);
                    }
                }
            }
            if (!format.initializationDataEquals(format2)) {
                i |= 32;
            }
            if (needsAdaptationFlushWorkaround(this.mimeType)) {
                i |= 2;
            }
            if (i == 0) {
                return new DecoderReuseEvaluation(this.name, format, format2, 1, 0);
            }
        }
        return new DecoderReuseEvaluation(this.name, format, format2, 0, i);
    }

    public boolean isVideoSizeAndRateSupportedV21(int i, int i2, double d) {
        MediaCodecInfo.CodecCapabilities codecCapabilities = this.capabilities;
        if (codecCapabilities == null) {
            logNoSupport("sizeAndRate.caps");
            return false;
        }
        MediaCodecInfo.VideoCapabilities videoCapabilities = codecCapabilities.getVideoCapabilities();
        if (videoCapabilities == null) {
            logNoSupport("sizeAndRate.vCaps");
            return false;
        }
        if (Util.SDK_INT >= 29) {
            int areResolutionAndFrameRateCovered = Api29.areResolutionAndFrameRateCovered(videoCapabilities, i, i2, d);
            if (areResolutionAndFrameRateCovered == 2) {
                return true;
            }
            if (areResolutionAndFrameRateCovered == 1) {
                logNoSupport("sizeAndRate.cover, " + i + "x" + i2 + "@" + d);
                return false;
            }
        }
        if (!areSizeAndRateSupportedV21(videoCapabilities, i, i2, d)) {
            if (i >= i2 || !needsRotatedVerticalResolutionWorkaround(this.name) || !areSizeAndRateSupportedV21(videoCapabilities, i2, i, d)) {
                logNoSupport("sizeAndRate.support, " + i + "x" + i2 + "@" + d);
                return false;
            }
            logAssumedSupport("sizeAndRate.rotated, " + i + "x" + i2 + "@" + d);
        }
        return true;
    }

    public Point alignVideoSizeV21(int i, int i2) {
        MediaCodecInfo.VideoCapabilities videoCapabilities;
        MediaCodecInfo.CodecCapabilities codecCapabilities = this.capabilities;
        if (codecCapabilities == null || (videoCapabilities = codecCapabilities.getVideoCapabilities()) == null) {
            return null;
        }
        return alignVideoSizeV21(videoCapabilities, i, i2);
    }

    public boolean isAudioSampleRateSupportedV21(int i) {
        MediaCodecInfo.CodecCapabilities codecCapabilities = this.capabilities;
        if (codecCapabilities == null) {
            logNoSupport("sampleRate.caps");
            return false;
        }
        MediaCodecInfo.AudioCapabilities audioCapabilities = codecCapabilities.getAudioCapabilities();
        if (audioCapabilities == null) {
            logNoSupport("sampleRate.aCaps");
            return false;
        } else if (audioCapabilities.isSampleRateSupported(i)) {
            return true;
        } else {
            logNoSupport("sampleRate.support, " + i);
            return false;
        }
    }

    public boolean isAudioChannelCountSupportedV21(int i) {
        MediaCodecInfo.CodecCapabilities codecCapabilities = this.capabilities;
        if (codecCapabilities == null) {
            logNoSupport("channelCount.caps");
            return false;
        }
        MediaCodecInfo.AudioCapabilities audioCapabilities = codecCapabilities.getAudioCapabilities();
        if (audioCapabilities == null) {
            logNoSupport("channelCount.aCaps");
            return false;
        } else if (adjustMaxInputChannelCount(this.name, this.mimeType, audioCapabilities.getMaxInputChannelCount()) < i) {
            logNoSupport("channelCount.support, " + i);
            return false;
        } else {
            return true;
        }
    }

    private void logNoSupport(String str) {
        Log.d("MediaCodecInfo", "NoSupport [" + str + "] [" + this.name + ", " + this.mimeType + "] [" + Util.DEVICE_DEBUG_INFO + "]");
    }

    private void logAssumedSupport(String str) {
        Log.d("MediaCodecInfo", "AssumedSupport [" + str + "] [" + this.name + ", " + this.mimeType + "] [" + Util.DEVICE_DEBUG_INFO + "]");
    }

    private static int adjustMaxInputChannelCount(String str, String str2, int i) {
        int i2;
        if (i > 1 || ((Util.SDK_INT >= 26 && i > 0) || "audio/mpeg".equals(str2) || "audio/3gpp".equals(str2) || "audio/amr-wb".equals(str2) || MediaController.AUIDO_MIME_TYPE.equals(str2) || "audio/vorbis".equals(str2) || "audio/opus".equals(str2) || "audio/raw".equals(str2) || "audio/flac".equals(str2) || "audio/g711-alaw".equals(str2) || "audio/g711-mlaw".equals(str2) || "audio/gsm".equals(str2))) {
            return i;
        }
        if ("audio/ac3".equals(str2)) {
            i2 = 6;
        } else {
            i2 = "audio/eac3".equals(str2) ? 16 : 30;
        }
        Log.w("MediaCodecInfo", "AssumedMaxChannelAdjustment: " + str + ", [" + i + " to " + i2 + "]");
        return i2;
    }

    private static boolean isAdaptive(MediaCodecInfo.CodecCapabilities codecCapabilities) {
        return Util.SDK_INT >= 19 && isAdaptiveV19(codecCapabilities);
    }

    private static boolean isAdaptiveV19(MediaCodecInfo.CodecCapabilities codecCapabilities) {
        return codecCapabilities.isFeatureSupported("adaptive-playback");
    }

    private static boolean isTunneling(MediaCodecInfo.CodecCapabilities codecCapabilities) {
        return Util.SDK_INT >= 21 && isTunnelingV21(codecCapabilities);
    }

    private static boolean isTunnelingV21(MediaCodecInfo.CodecCapabilities codecCapabilities) {
        return codecCapabilities.isFeatureSupported("tunneled-playback");
    }

    private static boolean isSecure(MediaCodecInfo.CodecCapabilities codecCapabilities) {
        return Util.SDK_INT >= 21 && isSecureV21(codecCapabilities);
    }

    private static boolean isSecureV21(MediaCodecInfo.CodecCapabilities codecCapabilities) {
        return codecCapabilities.isFeatureSupported("secure-playback");
    }

    private static boolean areSizeAndRateSupportedV21(MediaCodecInfo.VideoCapabilities videoCapabilities, int i, int i2, double d) {
        Point alignVideoSizeV21 = alignVideoSizeV21(videoCapabilities, i, i2);
        int i3 = alignVideoSizeV21.x;
        int i4 = alignVideoSizeV21.y;
        if (d == -1.0d || d < 1.0d) {
            return videoCapabilities.isSizeSupported(i3, i4);
        }
        return videoCapabilities.areSizeAndRateSupported(i3, i4, Math.floor(d));
    }

    private static Point alignVideoSizeV21(MediaCodecInfo.VideoCapabilities videoCapabilities, int i, int i2) {
        int widthAlignment = videoCapabilities.getWidthAlignment();
        int heightAlignment = videoCapabilities.getHeightAlignment();
        return new Point(Util.ceilDivide(i, widthAlignment) * widthAlignment, Util.ceilDivide(i2, heightAlignment) * heightAlignment);
    }

    private static MediaCodecInfo.CodecProfileLevel[] estimateLegacyVp9ProfileLevels(MediaCodecInfo.CodecCapabilities codecCapabilities) {
        MediaCodecInfo.VideoCapabilities videoCapabilities;
        int intValue = (codecCapabilities == null || (videoCapabilities = codecCapabilities.getVideoCapabilities()) == null) ? 0 : videoCapabilities.getBitrateRange().getUpper().intValue();
        int i = intValue >= 180000000 ? 1024 : intValue >= 120000000 ? LiteMode.FLAG_CALLS_ANIMATIONS : intValue >= 60000000 ? LiteMode.FLAG_CHAT_BLUR : intValue >= 30000000 ? 128 : intValue >= 18000000 ? 64 : intValue >= 12000000 ? 32 : intValue >= 7200000 ? 16 : intValue >= 3600000 ? 8 : intValue >= 1800000 ? 4 : intValue >= 800000 ? 2 : 1;
        MediaCodecInfo.CodecProfileLevel codecProfileLevel = new MediaCodecInfo.CodecProfileLevel();
        codecProfileLevel.profile = 1;
        codecProfileLevel.level = i;
        return new MediaCodecInfo.CodecProfileLevel[]{codecProfileLevel};
    }

    private static boolean needsDisableAdaptationWorkaround(String str) {
        if (Util.SDK_INT <= 22) {
            String str2 = Util.MODEL;
            if (("ODROID-XU3".equals(str2) || "Nexus 10".equals(str2)) && ("OMX.Exynos.AVC.Decoder".equals(str) || "OMX.Exynos.AVC.Decoder.secure".equals(str))) {
                return true;
            }
        }
        return false;
    }

    private static boolean needsAdaptationReconfigureWorkaround(String str) {
        return Util.MODEL.startsWith("SM-T230") && "OMX.MARVELL.VIDEO.HW.CODA7542DECODER".equals(str);
    }

    private static boolean needsAdaptationFlushWorkaround(String str) {
        return "audio/opus".equals(str);
    }

    private static final boolean needsRotatedVerticalResolutionWorkaround(String str) {
        return ("OMX.MTK.VIDEO.DECODER.HEVC".equals(str) && "mcv5a".equals(Util.DEVICE)) ? false : true;
    }

    private static boolean needsProfileExcludedWorkaround(String str, int i) {
        if ("video/hevc".equals(str) && 2 == i) {
            String str2 = Util.DEVICE;
            if ("sailfish".equals(str2) || "marlin".equals(str2)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Api29 {
        public static int areResolutionAndFrameRateCovered(MediaCodecInfo.VideoCapabilities videoCapabilities, int i, int i2, double d) {
            List<MediaCodecInfo.VideoCapabilities.PerformancePoint> supportedPerformancePoints = videoCapabilities.getSupportedPerformancePoints();
            if (supportedPerformancePoints == null || supportedPerformancePoints.isEmpty()) {
                return 0;
            }
            MediaCodecInfo.VideoCapabilities.PerformancePoint performancePoint = new MediaCodecInfo.VideoCapabilities.PerformancePoint(i, i2, (int) d);
            for (int i3 = 0; i3 < supportedPerformancePoints.size(); i3++) {
                if (supportedPerformancePoints.get(i3).covers(performancePoint)) {
                    return 2;
                }
            }
            return 1;
        }
    }
}
