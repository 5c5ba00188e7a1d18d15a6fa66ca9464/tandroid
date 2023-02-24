package com.google.android.exoplayer2.source.rtsp.reader;

import com.google.android.exoplayer2.source.rtsp.RtpPayloadFormat;
import com.google.android.exoplayer2.util.Assertions;
import org.telegram.messenger.MediaController;
/* loaded from: classes.dex */
public final class DefaultRtpPayloadReaderFactory {
    public RtpPayloadReader createPayloadReader(RtpPayloadFormat rtpPayloadFormat) {
        String str = (String) Assertions.checkNotNull(rtpPayloadFormat.format.sampleMimeType);
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1664118616:
                if (str.equals("video/3gpp")) {
                    c = 0;
                    break;
                }
                break;
            case -1662541442:
                if (str.equals("video/hevc")) {
                    c = 1;
                    break;
                }
                break;
            case -1606874997:
                if (str.equals("audio/amr-wb")) {
                    c = 2;
                    break;
                }
                break;
            case -53558318:
                if (str.equals(MediaController.AUIDO_MIME_TYPE)) {
                    c = 3;
                    break;
                }
                break;
            case 187078296:
                if (str.equals("audio/ac3")) {
                    c = 4;
                    break;
                }
                break;
            case 187094639:
                if (str.equals("audio/raw")) {
                    c = 5;
                    break;
                }
                break;
            case 1187890754:
                if (str.equals("video/mp4v-es")) {
                    c = 6;
                    break;
                }
                break;
            case 1331836730:
                if (str.equals(MediaController.VIDEO_MIME_TYPE)) {
                    c = 7;
                    break;
                }
                break;
            case 1503095341:
                if (str.equals("audio/3gpp")) {
                    c = '\b';
                    break;
                }
                break;
            case 1504891608:
                if (str.equals("audio/opus")) {
                    c = '\t';
                    break;
                }
                break;
            case 1599127256:
                if (str.equals("video/x-vnd.on2.vp8")) {
                    c = '\n';
                    break;
                }
                break;
            case 1599127257:
                if (str.equals("video/x-vnd.on2.vp9")) {
                    c = 11;
                    break;
                }
                break;
            case 1903231877:
                if (str.equals("audio/g711-alaw")) {
                    c = '\f';
                    break;
                }
                break;
            case 1903589369:
                if (str.equals("audio/g711-mlaw")) {
                    c = '\r';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return new RtpH263Reader(rtpPayloadFormat);
            case 1:
                return new RtpH265Reader(rtpPayloadFormat);
            case 2:
            case '\b':
                return new RtpAmrReader(rtpPayloadFormat);
            case 3:
                if (rtpPayloadFormat.mediaEncoding.equals("MP4A-LATM")) {
                    return new RtpMp4aReader(rtpPayloadFormat);
                }
                return new RtpAacReader(rtpPayloadFormat);
            case 4:
                return new RtpAc3Reader(rtpPayloadFormat);
            case 5:
            case '\f':
            case '\r':
                return new RtpPcmReader(rtpPayloadFormat);
            case 6:
                return new RtpMpeg4Reader(rtpPayloadFormat);
            case 7:
                return new RtpH264Reader(rtpPayloadFormat);
            case '\t':
                return new RtpOpusReader(rtpPayloadFormat);
            case '\n':
                return new RtpVp8Reader(rtpPayloadFormat);
            case 11:
                return new RtpVp9Reader(rtpPayloadFormat);
            default:
                return null;
        }
    }
}
