package com.google.android.exoplayer2.source.smoothstreaming.manifest;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.audio.AacUtil;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer2.extractor.mp4.TrackEncryptionBox;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.Util;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.telegram.messenger.MediaController;
import org.webrtc.MediaStreamTrack;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes.dex */
public class SsManifestParser implements ParsingLoadable.Parser {
    private final XmlPullParserFactory xmlParserFactory;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static abstract class ElementParser {
        private final String baseUri;
        private final List normalizedAttributes = new LinkedList();
        private final ElementParser parent;
        private final String tag;

        public ElementParser(ElementParser elementParser, String str, String str2) {
            this.parent = elementParser;
            this.baseUri = str;
            this.tag = str2;
        }

        private ElementParser newChildParser(ElementParser elementParser, String str, String str2) {
            if ("QualityLevel".equals(str)) {
                return new QualityLevelParser(elementParser, str2);
            }
            if ("Protection".equals(str)) {
                return new ProtectionParser(elementParser, str2);
            }
            if ("StreamIndex".equals(str)) {
                return new StreamIndexParser(elementParser, str2);
            }
            return null;
        }

        protected void addChild(Object obj) {
        }

        protected abstract Object build();

        protected final Object getNormalizedAttribute(String str) {
            for (int i = 0; i < this.normalizedAttributes.size(); i++) {
                Pair pair = (Pair) this.normalizedAttributes.get(i);
                if (((String) pair.first).equals(str)) {
                    return pair.second;
                }
            }
            ElementParser elementParser = this.parent;
            if (elementParser == null) {
                return null;
            }
            return elementParser.getNormalizedAttribute(str);
        }

        protected boolean handleChildInline(String str) {
            return false;
        }

        public final Object parse(XmlPullParser xmlPullParser) {
            boolean z = false;
            int i = 0;
            while (true) {
                int eventType = xmlPullParser.getEventType();
                if (eventType == 1) {
                    return null;
                }
                if (eventType == 2) {
                    String name = xmlPullParser.getName();
                    if (this.tag.equals(name)) {
                        parseStartTag(xmlPullParser);
                        z = true;
                    } else if (z) {
                        if (i > 0) {
                            i++;
                        } else if (handleChildInline(name)) {
                            parseStartTag(xmlPullParser);
                        } else {
                            ElementParser newChildParser = newChildParser(this, name, this.baseUri);
                            if (newChildParser == null) {
                                i = 1;
                            } else {
                                addChild(newChildParser.parse(xmlPullParser));
                            }
                        }
                    }
                } else if (eventType != 3) {
                    if (eventType == 4 && z && i == 0) {
                        parseText(xmlPullParser);
                    }
                } else if (!z) {
                    continue;
                } else if (i > 0) {
                    i--;
                } else {
                    String name2 = xmlPullParser.getName();
                    parseEndTag(xmlPullParser);
                    if (!handleChildInline(name2)) {
                        return build();
                    }
                }
                xmlPullParser.next();
            }
        }

        protected final boolean parseBoolean(XmlPullParser xmlPullParser, String str, boolean z) {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            return attributeValue != null ? Boolean.parseBoolean(attributeValue) : z;
        }

        protected void parseEndTag(XmlPullParser xmlPullParser) {
        }

        protected final int parseInt(XmlPullParser xmlPullParser, String str, int i) {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            if (attributeValue == null) {
                return i;
            }
            try {
                return Integer.parseInt(attributeValue);
            } catch (NumberFormatException e) {
                throw ParserException.createForMalformedManifest(null, e);
            }
        }

        protected final long parseLong(XmlPullParser xmlPullParser, String str, long j) {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            if (attributeValue == null) {
                return j;
            }
            try {
                return Long.parseLong(attributeValue);
            } catch (NumberFormatException e) {
                throw ParserException.createForMalformedManifest(null, e);
            }
        }

        protected final int parseRequiredInt(XmlPullParser xmlPullParser, String str) {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            if (attributeValue == null) {
                throw new MissingFieldException(str);
            }
            try {
                return Integer.parseInt(attributeValue);
            } catch (NumberFormatException e) {
                throw ParserException.createForMalformedManifest(null, e);
            }
        }

        protected final long parseRequiredLong(XmlPullParser xmlPullParser, String str) {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            if (attributeValue == null) {
                throw new MissingFieldException(str);
            }
            try {
                return Long.parseLong(attributeValue);
            } catch (NumberFormatException e) {
                throw ParserException.createForMalformedManifest(null, e);
            }
        }

        protected final String parseRequiredString(XmlPullParser xmlPullParser, String str) {
            String attributeValue = xmlPullParser.getAttributeValue(null, str);
            if (attributeValue != null) {
                return attributeValue;
            }
            throw new MissingFieldException(str);
        }

        protected abstract void parseStartTag(XmlPullParser xmlPullParser);

        protected void parseText(XmlPullParser xmlPullParser) {
        }

        protected final void putNormalizedAttribute(String str, Object obj) {
            this.normalizedAttributes.add(Pair.create(str, obj));
        }
    }

    /* loaded from: classes.dex */
    public static class MissingFieldException extends ParserException {
        public MissingFieldException(String str) {
            super("Missing required field: " + str, null, true, 4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ProtectionParser extends ElementParser {
        private boolean inProtectionHeader;
        private byte[] initData;
        private UUID uuid;

        public ProtectionParser(ElementParser elementParser, String str) {
            super(elementParser, str, "Protection");
        }

        private static TrackEncryptionBox[] buildTrackEncryptionBoxes(byte[] bArr) {
            return new TrackEncryptionBox[]{new TrackEncryptionBox(true, null, 8, getProtectionElementKeyId(bArr), 0, 0, null)};
        }

        private static byte[] getProtectionElementKeyId(byte[] bArr) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bArr.length; i += 2) {
                sb.append((char) bArr[i]);
            }
            String sb2 = sb.toString();
            byte[] decode = Base64.decode(sb2.substring(sb2.indexOf("<KID>") + 5, sb2.indexOf("</KID>")), 0);
            swap(decode, 0, 3);
            swap(decode, 1, 2);
            swap(decode, 4, 5);
            swap(decode, 6, 7);
            return decode;
        }

        private static String stripCurlyBraces(String str) {
            return (str.charAt(0) == '{' && str.charAt(str.length() - 1) == '}') ? str.substring(1, str.length() - 1) : str;
        }

        private static void swap(byte[] bArr, int i, int i2) {
            byte b = bArr[i];
            bArr[i] = bArr[i2];
            bArr[i2] = b;
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public Object build() {
            UUID uuid = this.uuid;
            return new SsManifest.ProtectionElement(uuid, PsshAtomUtil.buildPsshAtom(uuid, this.initData), buildTrackEncryptionBoxes(this.initData));
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public boolean handleChildInline(String str) {
            return "ProtectionHeader".equals(str);
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseEndTag(XmlPullParser xmlPullParser) {
            if ("ProtectionHeader".equals(xmlPullParser.getName())) {
                this.inProtectionHeader = false;
            }
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseStartTag(XmlPullParser xmlPullParser) {
            if ("ProtectionHeader".equals(xmlPullParser.getName())) {
                this.inProtectionHeader = true;
                this.uuid = UUID.fromString(stripCurlyBraces(xmlPullParser.getAttributeValue(null, "SystemID")));
            }
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseText(XmlPullParser xmlPullParser) {
            if (this.inProtectionHeader) {
                this.initData = Base64.decode(xmlPullParser.getText(), 0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class QualityLevelParser extends ElementParser {
        private Format format;

        public QualityLevelParser(ElementParser elementParser, String str) {
            super(elementParser, str, "QualityLevel");
        }

        private static List buildCodecSpecificData(String str) {
            ArrayList arrayList = new ArrayList();
            if (!TextUtils.isEmpty(str)) {
                byte[] bytesFromHexString = Util.getBytesFromHexString(str);
                byte[][] splitNalUnits = CodecSpecificDataUtil.splitNalUnits(bytesFromHexString);
                if (splitNalUnits == null) {
                    arrayList.add(bytesFromHexString);
                } else {
                    Collections.addAll(arrayList, splitNalUnits);
                }
            }
            return arrayList;
        }

        private static String fourCCToMimeType(String str) {
            if (str.equalsIgnoreCase("H264") || str.equalsIgnoreCase("X264") || str.equalsIgnoreCase("AVC1") || str.equalsIgnoreCase("DAVC")) {
                return MediaController.VIDEO_MIME_TYPE;
            }
            if (str.equalsIgnoreCase("AAC") || str.equalsIgnoreCase("AACL") || str.equalsIgnoreCase("AACH") || str.equalsIgnoreCase("AACP")) {
                return MediaController.AUDIO_MIME_TYPE;
            }
            if (str.equalsIgnoreCase("TTML") || str.equalsIgnoreCase("DFXP")) {
                return "application/ttml+xml";
            }
            if (str.equalsIgnoreCase("ac-3") || str.equalsIgnoreCase("dac3")) {
                return "audio/ac3";
            }
            if (str.equalsIgnoreCase("ec-3") || str.equalsIgnoreCase("dec3")) {
                return "audio/eac3";
            }
            if (str.equalsIgnoreCase("dtsc")) {
                return "audio/vnd.dts";
            }
            if (str.equalsIgnoreCase("dtsh") || str.equalsIgnoreCase("dtsl")) {
                return "audio/vnd.dts.hd";
            }
            if (str.equalsIgnoreCase("dtse")) {
                return "audio/vnd.dts.hd;profile=lbr";
            }
            if (str.equalsIgnoreCase("opus")) {
                return "audio/opus";
            }
            return null;
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public Object build() {
            return this.format;
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseStartTag(XmlPullParser xmlPullParser) {
            int i;
            Format.Builder builder = new Format.Builder();
            String fourCCToMimeType = fourCCToMimeType(parseRequiredString(xmlPullParser, "FourCC"));
            int intValue = ((Integer) getNormalizedAttribute("Type")).intValue();
            if (intValue == 2) {
                builder.setContainerMimeType("video/mp4").setWidth(parseRequiredInt(xmlPullParser, "MaxWidth")).setHeight(parseRequiredInt(xmlPullParser, "MaxHeight")).setInitializationData(buildCodecSpecificData(xmlPullParser.getAttributeValue(null, "CodecPrivateData")));
            } else if (intValue == 1) {
                if (fourCCToMimeType == null) {
                    fourCCToMimeType = MediaController.AUDIO_MIME_TYPE;
                }
                int parseRequiredInt = parseRequiredInt(xmlPullParser, "Channels");
                int parseRequiredInt2 = parseRequiredInt(xmlPullParser, "SamplingRate");
                List buildCodecSpecificData = buildCodecSpecificData(xmlPullParser.getAttributeValue(null, "CodecPrivateData"));
                if (buildCodecSpecificData.isEmpty() && MediaController.AUDIO_MIME_TYPE.equals(fourCCToMimeType)) {
                    buildCodecSpecificData = Collections.singletonList(AacUtil.buildAacLcAudioSpecificConfig(parseRequiredInt2, parseRequiredInt));
                }
                builder.setContainerMimeType("audio/mp4").setChannelCount(parseRequiredInt).setSampleRate(parseRequiredInt2).setInitializationData(buildCodecSpecificData);
            } else if (intValue == 3) {
                String str = (String) getNormalizedAttribute("Subtype");
                if (str != null) {
                    if (str.equals("CAPT")) {
                        i = 64;
                    } else if (str.equals("DESC")) {
                        i = 1024;
                    }
                    builder.setContainerMimeType("application/mp4").setRoleFlags(i);
                }
                i = 0;
                builder.setContainerMimeType("application/mp4").setRoleFlags(i);
            } else {
                builder.setContainerMimeType("application/mp4");
            }
            this.format = builder.setId(xmlPullParser.getAttributeValue(null, "Index")).setLabel((String) getNormalizedAttribute("Name")).setSampleMimeType(fourCCToMimeType).setAverageBitrate(parseRequiredInt(xmlPullParser, "Bitrate")).setLanguage((String) getNormalizedAttribute("Language")).build();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SmoothStreamingMediaParser extends ElementParser {
        private long duration;
        private long dvrWindowLength;
        private boolean isLive;
        private int lookAheadCount;
        private int majorVersion;
        private int minorVersion;
        private SsManifest.ProtectionElement protectionElement;
        private final List streamElements;
        private long timescale;

        public SmoothStreamingMediaParser(ElementParser elementParser, String str) {
            super(elementParser, str, "SmoothStreamingMedia");
            this.lookAheadCount = -1;
            this.protectionElement = null;
            this.streamElements = new LinkedList();
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void addChild(Object obj) {
            if (obj instanceof SsManifest.StreamElement) {
                this.streamElements.add((SsManifest.StreamElement) obj);
            } else if (obj instanceof SsManifest.ProtectionElement) {
                Assertions.checkState(this.protectionElement == null);
                this.protectionElement = (SsManifest.ProtectionElement) obj;
            }
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public Object build() {
            int size = this.streamElements.size();
            SsManifest.StreamElement[] streamElementArr = new SsManifest.StreamElement[size];
            this.streamElements.toArray(streamElementArr);
            if (this.protectionElement != null) {
                SsManifest.ProtectionElement protectionElement = this.protectionElement;
                DrmInitData drmInitData = new DrmInitData(new DrmInitData.SchemeData(protectionElement.uuid, "video/mp4", protectionElement.data));
                for (int i = 0; i < size; i++) {
                    SsManifest.StreamElement streamElement = streamElementArr[i];
                    int i2 = streamElement.type;
                    if (i2 == 2 || i2 == 1) {
                        Format[] formatArr = streamElement.formats;
                        for (int i3 = 0; i3 < formatArr.length; i3++) {
                            formatArr[i3] = formatArr[i3].buildUpon().setDrmInitData(drmInitData).build();
                        }
                    }
                }
            }
            return new SsManifest(this.majorVersion, this.minorVersion, this.timescale, this.duration, this.dvrWindowLength, this.lookAheadCount, this.isLive, this.protectionElement, streamElementArr);
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseStartTag(XmlPullParser xmlPullParser) {
            this.majorVersion = parseRequiredInt(xmlPullParser, "MajorVersion");
            this.minorVersion = parseRequiredInt(xmlPullParser, "MinorVersion");
            this.timescale = parseLong(xmlPullParser, "TimeScale", 10000000L);
            this.duration = parseRequiredLong(xmlPullParser, "Duration");
            this.dvrWindowLength = parseLong(xmlPullParser, "DVRWindowLength", 0L);
            this.lookAheadCount = parseInt(xmlPullParser, "LookaheadCount", -1);
            this.isLive = parseBoolean(xmlPullParser, "IsLive", false);
            putNormalizedAttribute("TimeScale", Long.valueOf(this.timescale));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class StreamIndexParser extends ElementParser {
        private final String baseUri;
        private int displayHeight;
        private int displayWidth;
        private final List formats;
        private String language;
        private long lastChunkDuration;
        private int maxHeight;
        private int maxWidth;
        private String name;
        private ArrayList startTimes;
        private String subType;
        private long timescale;
        private int type;
        private String url;

        public StreamIndexParser(ElementParser elementParser, String str) {
            super(elementParser, str, "StreamIndex");
            this.baseUri = str;
            this.formats = new LinkedList();
        }

        private void parseStreamElementStartTag(XmlPullParser xmlPullParser) {
            int parseType = parseType(xmlPullParser);
            this.type = parseType;
            putNormalizedAttribute("Type", Integer.valueOf(parseType));
            this.subType = this.type == 3 ? parseRequiredString(xmlPullParser, "Subtype") : xmlPullParser.getAttributeValue(null, "Subtype");
            putNormalizedAttribute("Subtype", this.subType);
            String attributeValue = xmlPullParser.getAttributeValue(null, "Name");
            this.name = attributeValue;
            putNormalizedAttribute("Name", attributeValue);
            this.url = parseRequiredString(xmlPullParser, "Url");
            this.maxWidth = parseInt(xmlPullParser, "MaxWidth", -1);
            this.maxHeight = parseInt(xmlPullParser, "MaxHeight", -1);
            this.displayWidth = parseInt(xmlPullParser, "DisplayWidth", -1);
            this.displayHeight = parseInt(xmlPullParser, "DisplayHeight", -1);
            String attributeValue2 = xmlPullParser.getAttributeValue(null, "Language");
            this.language = attributeValue2;
            putNormalizedAttribute("Language", attributeValue2);
            long parseInt = parseInt(xmlPullParser, "TimeScale", -1);
            this.timescale = parseInt;
            if (parseInt == -1) {
                this.timescale = ((Long) getNormalizedAttribute("TimeScale")).longValue();
            }
            this.startTimes = new ArrayList();
        }

        private void parseStreamFragmentStartTag(XmlPullParser xmlPullParser) {
            int size = this.startTimes.size();
            long parseLong = parseLong(xmlPullParser, "t", -9223372036854775807L);
            int i = 1;
            if (parseLong == -9223372036854775807L) {
                if (size == 0) {
                    parseLong = 0;
                } else {
                    if (this.lastChunkDuration == -1) {
                        throw ParserException.createForMalformedManifest("Unable to infer start time", null);
                    }
                    parseLong = ((Long) this.startTimes.get(size - 1)).longValue() + this.lastChunkDuration;
                }
            }
            this.startTimes.add(Long.valueOf(parseLong));
            this.lastChunkDuration = parseLong(xmlPullParser, "d", -9223372036854775807L);
            long parseLong2 = parseLong(xmlPullParser, "r", 1L);
            if (parseLong2 > 1 && this.lastChunkDuration == -9223372036854775807L) {
                throw ParserException.createForMalformedManifest("Repeated chunk with unspecified duration", null);
            }
            while (true) {
                long j = i;
                if (j >= parseLong2) {
                    return;
                }
                this.startTimes.add(Long.valueOf((this.lastChunkDuration * j) + parseLong));
                i++;
            }
        }

        private int parseType(XmlPullParser xmlPullParser) {
            String attributeValue = xmlPullParser.getAttributeValue(null, "Type");
            if (attributeValue == null) {
                throw new MissingFieldException("Type");
            }
            if (MediaStreamTrack.AUDIO_TRACK_KIND.equalsIgnoreCase(attributeValue)) {
                return 1;
            }
            if (MediaStreamTrack.VIDEO_TRACK_KIND.equalsIgnoreCase(attributeValue)) {
                return 2;
            }
            if ("text".equalsIgnoreCase(attributeValue)) {
                return 3;
            }
            throw ParserException.createForMalformedManifest("Invalid key value[" + attributeValue + "]", null);
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void addChild(Object obj) {
            if (obj instanceof Format) {
                this.formats.add((Format) obj);
            }
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public Object build() {
            Format[] formatArr = new Format[this.formats.size()];
            this.formats.toArray(formatArr);
            return new SsManifest.StreamElement(this.baseUri, this.url, this.type, this.subType, this.timescale, this.name, this.maxWidth, this.maxHeight, this.displayWidth, this.displayHeight, this.language, formatArr, this.startTimes, this.lastChunkDuration);
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public boolean handleChildInline(String str) {
            return "c".equals(str);
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseStartTag(XmlPullParser xmlPullParser) {
            if ("c".equals(xmlPullParser.getName())) {
                parseStreamFragmentStartTag(xmlPullParser);
            } else {
                parseStreamElementStartTag(xmlPullParser);
            }
        }
    }

    public SsManifestParser() {
        try {
            this.xmlParserFactory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.ParsingLoadable.Parser
    public SsManifest parse(Uri uri, InputStream inputStream) {
        try {
            XmlPullParser newPullParser = this.xmlParserFactory.newPullParser();
            newPullParser.setInput(inputStream, null);
            return (SsManifest) new SmoothStreamingMediaParser(null, uri.toString()).parse(newPullParser);
        } catch (XmlPullParserException e) {
            throw ParserException.createForMalformedManifest(null, e);
        }
    }
}
