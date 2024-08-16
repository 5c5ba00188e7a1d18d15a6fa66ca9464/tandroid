package com.google.android.exoplayer2.source.rtsp;

import android.net.Uri;
import android.util.Base64;
import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.audio.AacUtil;
import com.google.android.exoplayer2.source.rtsp.MediaDescription;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.Util;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.NotificationCenter;
import org.webrtc.MediaStreamTrack;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class RtspMediaTrack {
    public final RtpPayloadFormat payloadFormat;
    public final Uri uri;

    public RtspMediaTrack(MediaDescription mediaDescription, Uri uri) {
        Assertions.checkArgument(mediaDescription.attributes.containsKey("control"));
        this.payloadFormat = generatePayloadFormat(mediaDescription);
        this.uri = extractTrackUri(uri, (String) Util.castNonNull(mediaDescription.attributes.get("control")));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || RtspMediaTrack.class != obj.getClass()) {
            return false;
        }
        RtspMediaTrack rtspMediaTrack = (RtspMediaTrack) obj;
        return this.payloadFormat.equals(rtspMediaTrack.payloadFormat) && this.uri.equals(rtspMediaTrack.uri);
    }

    public int hashCode() {
        return ((this.payloadFormat.hashCode() + NotificationCenter.channelStarsUpdated) * 31) + this.uri.hashCode();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    static RtpPayloadFormat generatePayloadFormat(MediaDescription mediaDescription) {
        int i;
        char c;
        Format.Builder builder = new Format.Builder();
        int i2 = mediaDescription.bitrate;
        if (i2 > 0) {
            builder.setAverageBitrate(i2);
        }
        MediaDescription.RtpMapAttribute rtpMapAttribute = mediaDescription.rtpMapAttribute;
        int i3 = rtpMapAttribute.payloadType;
        String str = rtpMapAttribute.mediaEncoding;
        String mimeTypeFromRtpMediaType = RtpPayloadFormat.getMimeTypeFromRtpMediaType(str);
        builder.setSampleMimeType(mimeTypeFromRtpMediaType);
        int i4 = mediaDescription.rtpMapAttribute.clockRate;
        if (MediaStreamTrack.AUDIO_TRACK_KIND.equals(mediaDescription.mediaType)) {
            i = inferChannelCount(mediaDescription.rtpMapAttribute.encodingParameters, mimeTypeFromRtpMediaType);
            builder.setSampleRate(i4).setChannelCount(i);
        } else {
            i = -1;
        }
        ImmutableMap<String, String> fmtpParametersAsMap = mediaDescription.getFmtpParametersAsMap();
        switch (mimeTypeFromRtpMediaType.hashCode()) {
            case -1664118616:
                if (mimeTypeFromRtpMediaType.equals("video/3gpp")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -1662541442:
                if (mimeTypeFromRtpMediaType.equals("video/hevc")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -1606874997:
                if (mimeTypeFromRtpMediaType.equals("audio/amr-wb")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -53558318:
                if (mimeTypeFromRtpMediaType.equals(MediaController.AUDIO_MIME_TYPE)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 187078296:
                if (mimeTypeFromRtpMediaType.equals("audio/ac3")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 187094639:
                if (mimeTypeFromRtpMediaType.equals("audio/raw")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 1187890754:
                if (mimeTypeFromRtpMediaType.equals("video/mp4v-es")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 1331836730:
                if (mimeTypeFromRtpMediaType.equals(MediaController.VIDEO_MIME_TYPE)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 1503095341:
                if (mimeTypeFromRtpMediaType.equals("audio/3gpp")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1504891608:
                if (mimeTypeFromRtpMediaType.equals("audio/opus")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1599127256:
                if (mimeTypeFromRtpMediaType.equals("video/x-vnd.on2.vp8")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 1599127257:
                if (mimeTypeFromRtpMediaType.equals("video/x-vnd.on2.vp9")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 1903231877:
                if (mimeTypeFromRtpMediaType.equals("audio/g711-alaw")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 1903589369:
                if (mimeTypeFromRtpMediaType.equals("audio/g711-mlaw")) {
                    c = '\r';
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
                Assertions.checkArgument(i != -1);
                Assertions.checkArgument(!fmtpParametersAsMap.isEmpty());
                if (str.equals("MP4A-LATM")) {
                    Assertions.checkArgument(fmtpParametersAsMap.containsKey("cpresent") && fmtpParametersAsMap.get("cpresent").equals("0"), "Only supports cpresent=0 in AAC audio.");
                    String str2 = fmtpParametersAsMap.get("config");
                    Assertions.checkNotNull(str2, "AAC audio stream must include config fmtp parameter");
                    boolean z = str2.length() % 2 == 0;
                    Assertions.checkArgument(z, "Malformat MPEG4 config: " + str2);
                    AacUtil.Config parseAacStreamMuxConfig = parseAacStreamMuxConfig(str2);
                    builder.setSampleRate(parseAacStreamMuxConfig.sampleRateHz).setChannelCount(parseAacStreamMuxConfig.channelCount).setCodecs(parseAacStreamMuxConfig.codecs);
                }
                processAacFmtpAttribute(builder, fmtpParametersAsMap, i, i4);
                break;
            case 1:
            case 2:
                Assertions.checkArgument(i == 1, "Multi channel AMR is not currently supported.");
                Assertions.checkArgument(!fmtpParametersAsMap.isEmpty(), "fmtp parameters must include octet-align.");
                Assertions.checkArgument(fmtpParametersAsMap.containsKey("octet-align"), "Only octet aligned mode is currently supported.");
                Assertions.checkArgument(!fmtpParametersAsMap.containsKey("interleaving"), "Interleaving mode is not currently supported.");
                break;
            case 3:
                Assertions.checkArgument(i != -1);
                Assertions.checkArgument(i4 == 48000, "Invalid OPUS clock rate.");
                break;
            case 4:
                Assertions.checkArgument(!fmtpParametersAsMap.isEmpty());
                processMPEG4FmtpAttribute(builder, fmtpParametersAsMap);
                break;
            case 5:
                builder.setWidth(352).setHeight(NotificationCenter.storiesUpdated);
                break;
            case 6:
                Assertions.checkArgument(!fmtpParametersAsMap.isEmpty());
                processH264FmtpAttribute(builder, fmtpParametersAsMap);
                break;
            case 7:
                Assertions.checkArgument(!fmtpParametersAsMap.isEmpty());
                processH265FmtpAttribute(builder, fmtpParametersAsMap);
                break;
            case '\b':
                builder.setWidth(320).setHeight(NotificationCenter.reloadInterface);
                break;
            case '\t':
                builder.setWidth(320).setHeight(NotificationCenter.reloadInterface);
                break;
            case '\n':
                builder.setPcmEncoding(RtpPayloadFormat.getRawPcmEncodingType(str));
                break;
        }
        Assertions.checkArgument(i4 > 0);
        return new RtpPayloadFormat(builder.build(), i3, i4, fmtpParametersAsMap, str);
    }

    private static int inferChannelCount(int i, String str) {
        return i != -1 ? i : str.equals("audio/ac3") ? 6 : 1;
    }

    private static void processAacFmtpAttribute(Format.Builder builder, ImmutableMap<String, String> immutableMap, int i, int i2) {
        Assertions.checkArgument(immutableMap.containsKey("profile-level-id"));
        builder.setCodecs("mp4a.40." + ((String) Assertions.checkNotNull(immutableMap.get("profile-level-id"))));
        builder.setInitializationData(ImmutableList.of(AacUtil.buildAacLcAudioSpecificConfig(i2, i)));
    }

    private static AacUtil.Config parseAacStreamMuxConfig(String str) {
        ParsableBitArray parsableBitArray = new ParsableBitArray(Util.getBytesFromHexString(str));
        Assertions.checkArgument(parsableBitArray.readBits(1) == 0, "Only supports audio mux version 0.");
        Assertions.checkArgument(parsableBitArray.readBits(1) == 1, "Only supports allStreamsSameTimeFraming.");
        parsableBitArray.skipBits(6);
        Assertions.checkArgument(parsableBitArray.readBits(4) == 0, "Only supports one program.");
        Assertions.checkArgument(parsableBitArray.readBits(3) == 0, "Only supports one numLayer.");
        try {
            return AacUtil.parseAudioSpecificConfig(parsableBitArray, false);
        } catch (ParserException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static void processMPEG4FmtpAttribute(Format.Builder builder, ImmutableMap<String, String> immutableMap) {
        String str = immutableMap.get("config");
        if (str != null) {
            byte[] bytesFromHexString = Util.getBytesFromHexString(str);
            builder.setInitializationData(ImmutableList.of(bytesFromHexString));
            Pair<Integer, Integer> videoResolutionFromMpeg4VideoConfig = CodecSpecificDataUtil.getVideoResolutionFromMpeg4VideoConfig(bytesFromHexString);
            builder.setWidth(((Integer) videoResolutionFromMpeg4VideoConfig.first).intValue()).setHeight(((Integer) videoResolutionFromMpeg4VideoConfig.second).intValue());
        } else {
            builder.setWidth(352).setHeight(NotificationCenter.storiesUpdated);
        }
        String str2 = immutableMap.get("profile-level-id");
        StringBuilder sb = new StringBuilder();
        sb.append("mp4v.");
        if (str2 == null) {
            str2 = "1";
        }
        sb.append(str2);
        builder.setCodecs(sb.toString());
    }

    private static byte[] getInitializationDataFromParameterSet(String str) {
        byte[] decode = Base64.decode(str, 0);
        int length = decode.length;
        byte[] bArr = NalUnitUtil.NAL_START_CODE;
        byte[] bArr2 = new byte[length + bArr.length];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        System.arraycopy(decode, 0, bArr2, bArr.length, decode.length);
        return bArr2;
    }

    private static void processH264FmtpAttribute(Format.Builder builder, ImmutableMap<String, String> immutableMap) {
        Assertions.checkArgument(immutableMap.containsKey("sprop-parameter-sets"));
        String[] split = Util.split((String) Assertions.checkNotNull(immutableMap.get("sprop-parameter-sets")), ",");
        Assertions.checkArgument(split.length == 2);
        ImmutableList of = ImmutableList.of(getInitializationDataFromParameterSet(split[0]), getInitializationDataFromParameterSet(split[1]));
        builder.setInitializationData(of);
        byte[] bArr = of.get(0);
        NalUnitUtil.SpsData parseSpsNalUnit = NalUnitUtil.parseSpsNalUnit(bArr, NalUnitUtil.NAL_START_CODE.length, bArr.length);
        builder.setPixelWidthHeightRatio(parseSpsNalUnit.pixelWidthHeightRatio);
        builder.setHeight(parseSpsNalUnit.height);
        builder.setWidth(parseSpsNalUnit.width);
        String str = immutableMap.get("profile-level-id");
        if (str != null) {
            builder.setCodecs("avc1." + str);
            return;
        }
        builder.setCodecs(CodecSpecificDataUtil.buildAvcCodecString(parseSpsNalUnit.profileIdc, parseSpsNalUnit.constraintsFlagsAndReservedZero2Bits, parseSpsNalUnit.levelIdc));
    }

    private static void processH265FmtpAttribute(Format.Builder builder, ImmutableMap<String, String> immutableMap) {
        if (immutableMap.containsKey("sprop-max-don-diff")) {
            int parseInt = Integer.parseInt((String) Assertions.checkNotNull(immutableMap.get("sprop-max-don-diff")));
            boolean z = parseInt == 0;
            Assertions.checkArgument(z, "non-zero sprop-max-don-diff " + parseInt + " is not supported");
        }
        Assertions.checkArgument(immutableMap.containsKey("sprop-vps"));
        Assertions.checkArgument(immutableMap.containsKey("sprop-sps"));
        Assertions.checkArgument(immutableMap.containsKey("sprop-pps"));
        ImmutableList of = ImmutableList.of(getInitializationDataFromParameterSet((String) Assertions.checkNotNull(immutableMap.get("sprop-vps"))), getInitializationDataFromParameterSet((String) Assertions.checkNotNull(immutableMap.get("sprop-sps"))), getInitializationDataFromParameterSet((String) Assertions.checkNotNull(immutableMap.get("sprop-pps"))));
        builder.setInitializationData(of);
        byte[] bArr = of.get(1);
        NalUnitUtil.H265SpsData parseH265SpsNalUnit = NalUnitUtil.parseH265SpsNalUnit(bArr, NalUnitUtil.NAL_START_CODE.length, bArr.length);
        builder.setPixelWidthHeightRatio(parseH265SpsNalUnit.pixelWidthHeightRatio);
        builder.setHeight(parseH265SpsNalUnit.height).setWidth(parseH265SpsNalUnit.width);
        builder.setCodecs(CodecSpecificDataUtil.buildHevcCodecString(parseH265SpsNalUnit.generalProfileSpace, parseH265SpsNalUnit.generalTierFlag, parseH265SpsNalUnit.generalProfileIdc, parseH265SpsNalUnit.generalProfileCompatibilityFlags, parseH265SpsNalUnit.constraintBytes, parseH265SpsNalUnit.generalLevelIdc));
    }

    private static Uri extractTrackUri(Uri uri, String str) {
        Uri parse = Uri.parse(str);
        return parse.isAbsolute() ? parse : str.equals("*") ? uri : uri.buildUpon().appendEncodedPath(str).build();
    }
}
