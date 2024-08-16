package com.google.android.exoplayer2.source.rtsp;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import org.telegram.messenger.NotificationCenter;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class MediaDescription {
    public final ImmutableMap<String, String> attributes;
    public final int bitrate;
    public final String connection;
    public final String key;
    public final String mediaTitle;
    public final String mediaType;
    public final int payloadType;
    public final int port;
    public final RtpMapAttribute rtpMapAttribute;
    public final String transportProtocol;

    /* loaded from: classes.dex */
    public static final class RtpMapAttribute {
        public final int clockRate;
        public final int encodingParameters;
        public final String mediaEncoding;
        public final int payloadType;

        public static RtpMapAttribute parse(String str) throws ParserException {
            String[] splitAtFirst = Util.splitAtFirst(str, " ");
            Assertions.checkArgument(splitAtFirst.length == 2);
            int parseInt = RtspMessageUtil.parseInt(splitAtFirst[0]);
            String[] split = Util.split(splitAtFirst[1].trim(), "/");
            Assertions.checkArgument(split.length >= 2);
            return new RtpMapAttribute(parseInt, split[0], RtspMessageUtil.parseInt(split[1]), split.length == 3 ? RtspMessageUtil.parseInt(split[2]) : -1);
        }

        private RtpMapAttribute(int i, String str, int i2, int i3) {
            this.payloadType = i;
            this.mediaEncoding = str;
            this.clockRate = i2;
            this.encodingParameters = i3;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || RtpMapAttribute.class != obj.getClass()) {
                return false;
            }
            RtpMapAttribute rtpMapAttribute = (RtpMapAttribute) obj;
            return this.payloadType == rtpMapAttribute.payloadType && this.mediaEncoding.equals(rtpMapAttribute.mediaEncoding) && this.clockRate == rtpMapAttribute.clockRate && this.encodingParameters == rtpMapAttribute.encodingParameters;
        }

        public int hashCode() {
            return ((((((this.payloadType + NotificationCenter.channelStarsUpdated) * 31) + this.mediaEncoding.hashCode()) * 31) + this.clockRate) * 31) + this.encodingParameters;
        }
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private final HashMap<String, String> attributes = new HashMap<>();
        private int bitrate = -1;
        private String connection;
        private String key;
        private String mediaTitle;
        private final String mediaType;
        private final int payloadType;
        private final int port;
        private final String transportProtocol;

        public Builder(String str, int i, String str2, int i2) {
            this.mediaType = str;
            this.port = i;
            this.transportProtocol = str2;
            this.payloadType = i2;
        }

        public Builder setMediaTitle(String str) {
            this.mediaTitle = str;
            return this;
        }

        public Builder setConnection(String str) {
            this.connection = str;
            return this;
        }

        public Builder setBitrate(int i) {
            this.bitrate = i;
            return this;
        }

        public Builder setKey(String str) {
            this.key = str;
            return this;
        }

        public Builder addAttribute(String str, String str2) {
            this.attributes.put(str, str2);
            return this;
        }

        public MediaDescription build() {
            RtpMapAttribute parse;
            try {
                if (this.attributes.containsKey("rtpmap")) {
                    parse = RtpMapAttribute.parse((String) Util.castNonNull(this.attributes.get("rtpmap")));
                } else {
                    parse = RtpMapAttribute.parse(getRtpMapStringByPayloadType(this.payloadType));
                }
                return new MediaDescription(this, ImmutableMap.copyOf((Map) this.attributes), parse);
            } catch (ParserException e) {
                throw new IllegalStateException(e);
            }
        }

        private static String getRtpMapStringByPayloadType(int i) {
            Assertions.checkArgument(i < 96);
            if (i != 0) {
                if (i != 8) {
                    if (i != 10) {
                        if (i == 11) {
                            return constructAudioRtpMap(11, "L16", 44100, 1);
                        }
                        throw new IllegalStateException("Unsupported static paylod type " + i);
                    }
                    return constructAudioRtpMap(10, "L16", 44100, 2);
                }
                return constructAudioRtpMap(8, "PCMA", 8000, 1);
            }
            return constructAudioRtpMap(0, "PCMU", 8000, 1);
        }

        private static String constructAudioRtpMap(int i, String str, int i2, int i3) {
            return Util.formatInvariant("%d %s/%d/%d", Integer.valueOf(i), str, Integer.valueOf(i2), Integer.valueOf(i3));
        }
    }

    private MediaDescription(Builder builder, ImmutableMap<String, String> immutableMap, RtpMapAttribute rtpMapAttribute) {
        this.mediaType = builder.mediaType;
        this.port = builder.port;
        this.transportProtocol = builder.transportProtocol;
        this.payloadType = builder.payloadType;
        this.mediaTitle = builder.mediaTitle;
        this.connection = builder.connection;
        this.bitrate = builder.bitrate;
        this.key = builder.key;
        this.attributes = immutableMap;
        this.rtpMapAttribute = rtpMapAttribute;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || MediaDescription.class != obj.getClass()) {
            return false;
        }
        MediaDescription mediaDescription = (MediaDescription) obj;
        return this.mediaType.equals(mediaDescription.mediaType) && this.port == mediaDescription.port && this.transportProtocol.equals(mediaDescription.transportProtocol) && this.payloadType == mediaDescription.payloadType && this.bitrate == mediaDescription.bitrate && this.attributes.equals(mediaDescription.attributes) && this.rtpMapAttribute.equals(mediaDescription.rtpMapAttribute) && Util.areEqual(this.mediaTitle, mediaDescription.mediaTitle) && Util.areEqual(this.connection, mediaDescription.connection) && Util.areEqual(this.key, mediaDescription.key);
    }

    public int hashCode() {
        int hashCode = (((((((((((((this.mediaType.hashCode() + NotificationCenter.channelStarsUpdated) * 31) + this.port) * 31) + this.transportProtocol.hashCode()) * 31) + this.payloadType) * 31) + this.bitrate) * 31) + this.attributes.hashCode()) * 31) + this.rtpMapAttribute.hashCode()) * 31;
        String str = this.mediaTitle;
        int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
        String str2 = this.connection;
        int hashCode3 = (hashCode2 + (str2 == null ? 0 : str2.hashCode())) * 31;
        String str3 = this.key;
        return hashCode3 + (str3 != null ? str3.hashCode() : 0);
    }

    public ImmutableMap<String, String> getFmtpParametersAsMap() {
        String str = this.attributes.get("fmtp");
        if (str == null) {
            return ImmutableMap.of();
        }
        String[] splitAtFirst = Util.splitAtFirst(str, " ");
        Assertions.checkArgument(splitAtFirst.length == 2, str);
        String[] split = splitAtFirst[1].split(";\\s?", 0);
        ImmutableMap.Builder builder = new ImmutableMap.Builder();
        for (String str2 : split) {
            String[] splitAtFirst2 = Util.splitAtFirst(str2, "=");
            builder.put(splitAtFirst2[0], splitAtFirst2[1]);
        }
        return builder.buildOrThrow();
    }
}
