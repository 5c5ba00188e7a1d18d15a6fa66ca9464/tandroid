package com.google.android.exoplayer2.source.rtsp;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.Assertions;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.NotificationCenter;
/* loaded from: classes.dex */
public final class RtpPayloadFormat {
    public final int clockRate;
    public final ImmutableMap<String, String> fmtpParameters;
    public final Format format;
    public final String mediaEncoding;
    public final int rtpPayloadType;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isFormatSupported(MediaDescription mediaDescription) {
        String upperCase = Ascii.toUpperCase(mediaDescription.rtpMapAttribute.mediaEncoding);
        upperCase.hashCode();
        char c = 65535;
        switch (upperCase.hashCode()) {
            case -1922091719:
                if (upperCase.equals("MPEG4-GENERIC")) {
                    c = 0;
                    break;
                }
                break;
            case 2412:
                if (upperCase.equals("L8")) {
                    c = 1;
                    break;
                }
                break;
            case 64593:
                if (upperCase.equals("AC3")) {
                    c = 2;
                    break;
                }
                break;
            case 64934:
                if (upperCase.equals("AMR")) {
                    c = 3;
                    break;
                }
                break;
            case 74609:
                if (upperCase.equals("L16")) {
                    c = 4;
                    break;
                }
                break;
            case 85182:
                if (upperCase.equals("VP8")) {
                    c = 5;
                    break;
                }
                break;
            case 85183:
                if (upperCase.equals("VP9")) {
                    c = 6;
                    break;
                }
                break;
            case 2194728:
                if (upperCase.equals("H264")) {
                    c = 7;
                    break;
                }
                break;
            case 2194729:
                if (upperCase.equals("H265")) {
                    c = '\b';
                    break;
                }
                break;
            case 2433087:
                if (upperCase.equals("OPUS")) {
                    c = '\t';
                    break;
                }
                break;
            case 2450119:
                if (upperCase.equals("PCMA")) {
                    c = '\n';
                    break;
                }
                break;
            case 2450139:
                if (upperCase.equals("PCMU")) {
                    c = 11;
                    break;
                }
                break;
            case 1061166827:
                if (upperCase.equals("MP4A-LATM")) {
                    c = '\f';
                    break;
                }
                break;
            case 1934494802:
                if (upperCase.equals("AMR-WB")) {
                    c = '\r';
                    break;
                }
                break;
            case 1959269366:
                if (upperCase.equals("MP4V-ES")) {
                    c = 14;
                    break;
                }
                break;
            case 2137188397:
                if (upperCase.equals("H263-1998")) {
                    c = 15;
                    break;
                }
                break;
            case 2137209252:
                if (upperCase.equals("H263-2000")) {
                    c = 16;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case '\b':
            case '\t':
            case '\n':
            case 11:
            case '\f':
            case '\r':
            case 14:
            case 15:
            case 16:
                return true;
            default:
                return false;
        }
    }

    public static String getMimeTypeFromRtpMediaType(String str) {
        String upperCase = Ascii.toUpperCase(str);
        upperCase.hashCode();
        char c = 65535;
        switch (upperCase.hashCode()) {
            case -1922091719:
                if (upperCase.equals("MPEG4-GENERIC")) {
                    c = 0;
                    break;
                }
                break;
            case 2412:
                if (upperCase.equals("L8")) {
                    c = 1;
                    break;
                }
                break;
            case 64593:
                if (upperCase.equals("AC3")) {
                    c = 2;
                    break;
                }
                break;
            case 64934:
                if (upperCase.equals("AMR")) {
                    c = 3;
                    break;
                }
                break;
            case 74609:
                if (upperCase.equals("L16")) {
                    c = 4;
                    break;
                }
                break;
            case 85182:
                if (upperCase.equals("VP8")) {
                    c = 5;
                    break;
                }
                break;
            case 85183:
                if (upperCase.equals("VP9")) {
                    c = 6;
                    break;
                }
                break;
            case 2194728:
                if (upperCase.equals("H264")) {
                    c = 7;
                    break;
                }
                break;
            case 2194729:
                if (upperCase.equals("H265")) {
                    c = '\b';
                    break;
                }
                break;
            case 2433087:
                if (upperCase.equals("OPUS")) {
                    c = '\t';
                    break;
                }
                break;
            case 2450119:
                if (upperCase.equals("PCMA")) {
                    c = '\n';
                    break;
                }
                break;
            case 2450139:
                if (upperCase.equals("PCMU")) {
                    c = 11;
                    break;
                }
                break;
            case 1061166827:
                if (upperCase.equals("MP4A-LATM")) {
                    c = '\f';
                    break;
                }
                break;
            case 1934494802:
                if (upperCase.equals("AMR-WB")) {
                    c = '\r';
                    break;
                }
                break;
            case 1959269366:
                if (upperCase.equals("MP4V-ES")) {
                    c = 14;
                    break;
                }
                break;
            case 2137188397:
                if (upperCase.equals("H263-1998")) {
                    c = 15;
                    break;
                }
                break;
            case 2137209252:
                if (upperCase.equals("H263-2000")) {
                    c = 16;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case '\f':
                return MediaController.AUDIO_MIME_TYPE;
            case 1:
            case 4:
                return "audio/raw";
            case 2:
                return "audio/ac3";
            case 3:
                return "audio/3gpp";
            case 5:
                return "video/x-vnd.on2.vp8";
            case 6:
                return "video/x-vnd.on2.vp9";
            case 7:
                return MediaController.VIDEO_MIME_TYPE;
            case '\b':
                return "video/hevc";
            case '\t':
                return "audio/opus";
            case '\n':
                return "audio/g711-alaw";
            case 11:
                return "audio/g711-mlaw";
            case '\r':
                return "audio/amr-wb";
            case 14:
                return "video/mp4v-es";
            case 15:
            case 16:
                return "video/3gpp";
            default:
                throw new IllegalArgumentException(str);
        }
    }

    public static int getRawPcmEncodingType(String str) {
        Assertions.checkArgument(str.equals("L8") || str.equals("L16"));
        return str.equals("L8") ? 3 : 268435456;
    }

    public RtpPayloadFormat(Format format, int i, int i2, Map<String, String> map, String str) {
        this.rtpPayloadType = i;
        this.clockRate = i2;
        this.format = format;
        this.fmtpParameters = ImmutableMap.copyOf((Map) map);
        this.mediaEncoding = str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || RtpPayloadFormat.class != obj.getClass()) {
            return false;
        }
        RtpPayloadFormat rtpPayloadFormat = (RtpPayloadFormat) obj;
        return this.rtpPayloadType == rtpPayloadFormat.rtpPayloadType && this.clockRate == rtpPayloadFormat.clockRate && this.format.equals(rtpPayloadFormat.format) && this.fmtpParameters.equals(rtpPayloadFormat.fmtpParameters) && this.mediaEncoding.equals(rtpPayloadFormat.mediaEncoding);
    }

    public int hashCode() {
        return ((((((((this.rtpPayloadType + NotificationCenter.channelStarsUpdated) * 31) + this.clockRate) * 31) + this.format.hashCode()) * 31) + this.fmtpParameters.hashCode()) * 31) + this.mediaEncoding.hashCode();
    }
}
