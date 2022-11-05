package com.google.android.exoplayer2.source.dash.manifest;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import android.util.Xml;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.source.dash.manifest.SegmentBase;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.XmlPullParserUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.tgnet.ConnectionsManager;
import org.webrtc.MediaStreamTrack;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;
/* loaded from: classes.dex */
public class DashManifestParser extends DefaultHandler implements ParsingLoadable.Parser<DashManifest> {
    private final XmlPullParserFactory xmlParserFactory;
    private static final Pattern FRAME_RATE_PATTERN = Pattern.compile("(\\d+)(?:/(\\d+))?");
    private static final Pattern CEA_608_ACCESSIBILITY_PATTERN = Pattern.compile("CC([1-4])=.*");
    private static final Pattern CEA_708_ACCESSIBILITY_PATTERN = Pattern.compile("([1-9]|[1-5][0-9]|6[0-3])=.*");

    public DashManifestParser() {
        try {
            this.xmlParserFactory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.ParsingLoadable.Parser
    public DashManifest parse(Uri uri, InputStream inputStream) throws IOException {
        try {
            XmlPullParser newPullParser = this.xmlParserFactory.newPullParser();
            newPullParser.setInput(inputStream, null);
            if (newPullParser.next() != 2 || !"MPD".equals(newPullParser.getName())) {
                throw new ParserException("inputStream does not contain a valid media presentation description");
            }
            return parseMediaPresentationDescription(newPullParser, uri.toString());
        } catch (XmlPullParserException e) {
            throw new ParserException(e);
        }
    }

    protected DashManifest parseMediaPresentationDescription(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException {
        String str2;
        long j;
        DashManifestParser dashManifestParser = this;
        long parseDateTime = parseDateTime(xmlPullParser, "availabilityStartTime", -9223372036854775807L);
        long parseDuration = parseDuration(xmlPullParser, "mediaPresentationDuration", -9223372036854775807L);
        long parseDuration2 = parseDuration(xmlPullParser, "minBufferTime", -9223372036854775807L);
        boolean equals = "dynamic".equals(xmlPullParser.getAttributeValue(null, "type"));
        long parseDuration3 = equals ? parseDuration(xmlPullParser, "minimumUpdatePeriod", -9223372036854775807L) : -9223372036854775807L;
        long parseDuration4 = equals ? parseDuration(xmlPullParser, "timeShiftBufferDepth", -9223372036854775807L) : -9223372036854775807L;
        long parseDuration5 = equals ? parseDuration(xmlPullParser, "suggestedPresentationDelay", -9223372036854775807L) : -9223372036854775807L;
        long parseDateTime2 = parseDateTime(xmlPullParser, "publishTime", -9223372036854775807L);
        ArrayList arrayList = new ArrayList();
        Uri uri = null;
        long j2 = equals ? -9223372036854775807L : 0L;
        boolean z = false;
        boolean z2 = false;
        String str3 = str;
        ProgramInformation programInformation = null;
        UtcTimingElement utcTimingElement = null;
        while (true) {
            xmlPullParser.next();
            long j3 = parseDuration4;
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "BaseURL")) {
                if (!z) {
                    str3 = dashManifestParser.parseBaseUrl(xmlPullParser, str3);
                    z = true;
                } else {
                    str2 = str3;
                    j = j2;
                    str3 = str2;
                    j2 = j;
                }
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "ProgramInformation")) {
                programInformation = parseProgramInformation(xmlPullParser);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "UTCTiming")) {
                utcTimingElement = parseUtcTiming(xmlPullParser);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "Location")) {
                uri = Uri.parse(xmlPullParser.nextText());
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "Period") && !z2) {
                Pair<Period, Long> parsePeriod = dashManifestParser.parsePeriod(xmlPullParser, str3, j2);
                String str4 = str3;
                Period period = (Period) parsePeriod.first;
                long j4 = j2;
                if (period.startMs != -9223372036854775807L) {
                    long longValue = ((Long) parsePeriod.second).longValue();
                    j2 = longValue == -9223372036854775807L ? -9223372036854775807L : period.startMs + longValue;
                    arrayList.add(period);
                } else if (!equals) {
                    throw new ParserException("Unable to determine start of period " + arrayList.size());
                } else {
                    j2 = j4;
                    z2 = true;
                }
                str3 = str4;
            } else {
                str2 = str3;
                j = j2;
                maybeSkipTag(xmlPullParser);
                str3 = str2;
                j2 = j;
            }
            if (XmlPullParserUtil.isEndTag(xmlPullParser, "MPD")) {
                if (parseDuration == -9223372036854775807L) {
                    if (j2 != -9223372036854775807L) {
                        parseDuration = j2;
                    } else if (!equals) {
                        throw new ParserException("Unable to determine duration of static manifest.");
                    }
                }
                if (arrayList.isEmpty()) {
                    throw new ParserException("No periods found.");
                }
                return buildMediaPresentationDescription(parseDateTime, parseDuration, parseDuration2, equals, parseDuration3, j3, parseDuration5, parseDateTime2, programInformation, utcTimingElement, uri, arrayList);
            }
            dashManifestParser = this;
            parseDuration4 = j3;
        }
    }

    protected DashManifest buildMediaPresentationDescription(long j, long j2, long j3, boolean z, long j4, long j5, long j6, long j7, ProgramInformation programInformation, UtcTimingElement utcTimingElement, Uri uri, List<Period> list) {
        return new DashManifest(j, j2, j3, z, j4, j5, j6, j7, programInformation, utcTimingElement, uri, list);
    }

    protected UtcTimingElement parseUtcTiming(XmlPullParser xmlPullParser) {
        return buildUtcTimingElement(xmlPullParser.getAttributeValue(null, "schemeIdUri"), xmlPullParser.getAttributeValue(null, "value"));
    }

    protected UtcTimingElement buildUtcTimingElement(String str, String str2) {
        return new UtcTimingElement(str, str2);
    }

    protected Pair<Period, Long> parsePeriod(XmlPullParser xmlPullParser, String str, long j) throws XmlPullParserException, IOException {
        String str2;
        String attributeValue = xmlPullParser.getAttributeValue(null, "id");
        long parseDuration = parseDuration(xmlPullParser, "start", j);
        long parseDuration2 = parseDuration(xmlPullParser, "duration", -9223372036854775807L);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        String str3 = str;
        SegmentBase segmentBase = null;
        Descriptor descriptor = null;
        boolean z = false;
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "BaseURL")) {
                if (!z) {
                    str3 = parseBaseUrl(xmlPullParser, str3);
                    z = true;
                } else {
                    str2 = str3;
                }
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "AdaptationSet")) {
                str2 = str3;
                arrayList.add(parseAdaptationSet(xmlPullParser, str3, segmentBase, parseDuration2));
            } else {
                str2 = str3;
                if (XmlPullParserUtil.isStartTag(xmlPullParser, "EventStream")) {
                    arrayList2.add(parseEventStream(xmlPullParser));
                } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentBase")) {
                    segmentBase = parseSegmentBase(xmlPullParser, null);
                } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentList")) {
                    segmentBase = parseSegmentList(xmlPullParser, null, parseDuration2);
                } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentTemplate")) {
                    segmentBase = parseSegmentTemplate(xmlPullParser, null, Collections.emptyList(), parseDuration2);
                } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "AssetIdentifier")) {
                    descriptor = parseDescriptor(xmlPullParser, "AssetIdentifier");
                } else {
                    maybeSkipTag(xmlPullParser);
                }
            }
            str3 = str2;
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "Period"));
        return Pair.create(buildPeriod(attributeValue, parseDuration, arrayList, arrayList2, descriptor), Long.valueOf(parseDuration2));
    }

    protected Period buildPeriod(String str, long j, List<AdaptationSet> list, List<EventStream> list2, Descriptor descriptor) {
        return new Period(str, j, list, list2, descriptor);
    }

    protected AdaptationSet parseAdaptationSet(XmlPullParser xmlPullParser, String str, SegmentBase segmentBase, long j) throws XmlPullParserException, IOException {
        String str2;
        String str3;
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        ArrayList arrayList4;
        ArrayList<Descriptor> arrayList5;
        ArrayList<DrmInitData.SchemeData> arrayList6;
        String str4;
        String str5;
        DashManifestParser dashManifestParser;
        int i;
        ArrayList arrayList7;
        XmlPullParser xmlPullParser2;
        ArrayList<Descriptor> arrayList8;
        SegmentBase parseSegmentTemplate;
        DashManifestParser dashManifestParser2 = this;
        XmlPullParser xmlPullParser3 = xmlPullParser;
        int parseInt = parseInt(xmlPullParser3, "id", -1);
        int parseContentType = parseContentType(xmlPullParser);
        String str6 = null;
        String attributeValue = xmlPullParser3.getAttributeValue(null, "mimeType");
        String attributeValue2 = xmlPullParser3.getAttributeValue(null, "codecs");
        int parseInt2 = parseInt(xmlPullParser3, "width", -1);
        int parseInt3 = parseInt(xmlPullParser3, "height", -1);
        float parseFrameRate = parseFrameRate(xmlPullParser3, -1.0f);
        int parseInt4 = parseInt(xmlPullParser3, "audioSamplingRate", -1);
        String str7 = "lang";
        String attributeValue3 = xmlPullParser3.getAttributeValue(null, str7);
        String attributeValue4 = xmlPullParser3.getAttributeValue(null, "label");
        ArrayList<DrmInitData.SchemeData> arrayList9 = new ArrayList<>();
        ArrayList<Descriptor> arrayList10 = new ArrayList<>();
        ArrayList arrayList11 = new ArrayList();
        ArrayList arrayList12 = new ArrayList();
        ArrayList arrayList13 = new ArrayList();
        ArrayList arrayList14 = new ArrayList();
        ArrayList arrayList15 = new ArrayList();
        String str8 = str;
        SegmentBase segmentBase2 = segmentBase;
        String str9 = attributeValue4;
        String str10 = null;
        int i2 = -1;
        boolean z = false;
        int i3 = parseContentType;
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser3, "BaseURL")) {
                if (!z) {
                    z = true;
                    str8 = dashManifestParser2.parseBaseUrl(xmlPullParser3, str8);
                    arrayList = arrayList14;
                    arrayList2 = arrayList13;
                    arrayList3 = arrayList12;
                    arrayList4 = arrayList11;
                    arrayList6 = arrayList9;
                    str4 = str7;
                    str5 = str6;
                    xmlPullParser2 = xmlPullParser3;
                    dashManifestParser = dashManifestParser2;
                    i = i3;
                    arrayList7 = arrayList15;
                    arrayList8 = arrayList10;
                }
                str2 = attributeValue3;
                str3 = str8;
                arrayList = arrayList14;
                arrayList2 = arrayList13;
                arrayList3 = arrayList12;
                arrayList4 = arrayList11;
                arrayList6 = arrayList9;
                str4 = str7;
                str5 = str6;
                xmlPullParser2 = xmlPullParser3;
                dashManifestParser = dashManifestParser2;
                i = i3;
                arrayList7 = arrayList15;
                arrayList8 = arrayList10;
                attributeValue3 = str2;
                str8 = str3;
            } else {
                if (XmlPullParserUtil.isStartTag(xmlPullParser3, "ContentProtection")) {
                    Pair<String, DrmInitData.SchemeData> parseContentProtection = parseContentProtection(xmlPullParser);
                    Object obj = parseContentProtection.first;
                    if (obj != null) {
                        str10 = (String) obj;
                    }
                    Object obj2 = parseContentProtection.second;
                    if (obj2 != null) {
                        arrayList9.add((DrmInitData.SchemeData) obj2);
                    }
                } else if (XmlPullParserUtil.isStartTag(xmlPullParser3, "ContentComponent")) {
                    attributeValue3 = checkLanguageConsistency(attributeValue3, xmlPullParser3.getAttributeValue(str6, str7));
                    arrayList = arrayList14;
                    arrayList2 = arrayList13;
                    arrayList3 = arrayList12;
                    arrayList4 = arrayList11;
                    arrayList6 = arrayList9;
                    str4 = str7;
                    str5 = str6;
                    xmlPullParser2 = xmlPullParser3;
                    dashManifestParser = dashManifestParser2;
                    i = checkContentTypeConsistency(i3, parseContentType(xmlPullParser));
                    arrayList7 = arrayList15;
                    arrayList8 = arrayList10;
                } else {
                    if (XmlPullParserUtil.isStartTag(xmlPullParser3, "Role")) {
                        arrayList12.add(parseDescriptor(xmlPullParser3, "Role"));
                    } else if (XmlPullParserUtil.isStartTag(xmlPullParser3, "AudioChannelConfiguration")) {
                        i2 = parseAudioChannelConfiguration(xmlPullParser);
                    } else if (XmlPullParserUtil.isStartTag(xmlPullParser3, "Accessibility")) {
                        arrayList11.add(parseDescriptor(xmlPullParser3, "Accessibility"));
                    } else if (XmlPullParserUtil.isStartTag(xmlPullParser3, "EssentialProperty")) {
                        arrayList13.add(parseDescriptor(xmlPullParser3, "EssentialProperty"));
                    } else if (XmlPullParserUtil.isStartTag(xmlPullParser3, "SupplementalProperty")) {
                        arrayList14.add(parseDescriptor(xmlPullParser3, "SupplementalProperty"));
                    } else {
                        if (XmlPullParserUtil.isStartTag(xmlPullParser3, "Representation")) {
                            str2 = attributeValue3;
                            str3 = str8;
                            arrayList = arrayList14;
                            arrayList2 = arrayList13;
                            arrayList3 = arrayList12;
                            arrayList4 = arrayList11;
                            arrayList5 = arrayList10;
                            arrayList6 = arrayList9;
                            str4 = str7;
                            str5 = str6;
                            RepresentationInfo parseRepresentation = parseRepresentation(xmlPullParser, str8, attributeValue, attributeValue2, parseInt2, parseInt3, parseFrameRate, i2, parseInt4, str2, arrayList3, arrayList4, arrayList2, arrayList, segmentBase2, j);
                            dashManifestParser = this;
                            int checkContentTypeConsistency = checkContentTypeConsistency(i3, dashManifestParser.getContentType(parseRepresentation.format));
                            arrayList7 = arrayList15;
                            arrayList7.add(parseRepresentation);
                            xmlPullParser2 = xmlPullParser;
                            i = checkContentTypeConsistency;
                        } else {
                            str2 = attributeValue3;
                            str3 = str8;
                            arrayList = arrayList14;
                            arrayList2 = arrayList13;
                            arrayList3 = arrayList12;
                            arrayList4 = arrayList11;
                            arrayList5 = arrayList10;
                            arrayList6 = arrayList9;
                            str4 = str7;
                            str5 = str6;
                            dashManifestParser = dashManifestParser2;
                            i = i3;
                            arrayList7 = arrayList15;
                            xmlPullParser2 = xmlPullParser;
                            if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentBase")) {
                                parseSegmentTemplate = dashManifestParser.parseSegmentBase(xmlPullParser2, (SegmentBase.SingleSegmentBase) segmentBase2);
                            } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentList")) {
                                parseSegmentTemplate = dashManifestParser.parseSegmentList(xmlPullParser2, (SegmentBase.SegmentList) segmentBase2, j);
                            } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentTemplate")) {
                                parseSegmentTemplate = parseSegmentTemplate(xmlPullParser, (SegmentBase.SegmentTemplate) segmentBase2, arrayList, j);
                            } else {
                                if (XmlPullParserUtil.isStartTag(xmlPullParser2, "InbandEventStream")) {
                                    arrayList8 = arrayList5;
                                    arrayList8.add(parseDescriptor(xmlPullParser2, "InbandEventStream"));
                                } else {
                                    arrayList8 = arrayList5;
                                    if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Label")) {
                                        str9 = parseLabel(xmlPullParser);
                                    } else if (XmlPullParserUtil.isStartTag(xmlPullParser)) {
                                        parseAdaptationSetChild(xmlPullParser);
                                    }
                                }
                                attributeValue3 = str2;
                                str8 = str3;
                            }
                            segmentBase2 = parseSegmentTemplate;
                        }
                        attributeValue3 = str2;
                        str8 = str3;
                        arrayList8 = arrayList5;
                    }
                    str2 = attributeValue3;
                    str3 = str8;
                    arrayList = arrayList14;
                    arrayList2 = arrayList13;
                    arrayList3 = arrayList12;
                    arrayList4 = arrayList11;
                    arrayList6 = arrayList9;
                    str4 = str7;
                    str5 = str6;
                    xmlPullParser2 = xmlPullParser3;
                    dashManifestParser = dashManifestParser2;
                    i = i3;
                    arrayList7 = arrayList15;
                    arrayList8 = arrayList10;
                    attributeValue3 = str2;
                    str8 = str3;
                }
                arrayList = arrayList14;
                arrayList2 = arrayList13;
                arrayList3 = arrayList12;
                arrayList4 = arrayList11;
                arrayList6 = arrayList9;
                str4 = str7;
                str5 = str6;
                xmlPullParser2 = xmlPullParser3;
                dashManifestParser = dashManifestParser2;
                i = i3;
                arrayList7 = arrayList15;
                arrayList8 = arrayList10;
            }
            if (XmlPullParserUtil.isEndTag(xmlPullParser2, "AdaptationSet")) {
                break;
            }
            i3 = i;
            dashManifestParser2 = dashManifestParser;
            arrayList15 = arrayList7;
            xmlPullParser3 = xmlPullParser2;
            arrayList10 = arrayList8;
            arrayList14 = arrayList;
            arrayList13 = arrayList2;
            arrayList12 = arrayList3;
            arrayList11 = arrayList4;
            arrayList9 = arrayList6;
            str7 = str4;
            str6 = str5;
        }
        ArrayList arrayList16 = new ArrayList(arrayList7.size());
        for (int i4 = 0; i4 < arrayList7.size(); i4++) {
            arrayList16.add(buildRepresentation((RepresentationInfo) arrayList7.get(i4), str9, str10, arrayList6, arrayList8));
        }
        return buildAdaptationSet(parseInt, i, arrayList16, arrayList4, arrayList2, arrayList);
    }

    protected AdaptationSet buildAdaptationSet(int i, int i2, List<Representation> list, List<Descriptor> list2, List<Descriptor> list3, List<Descriptor> list4) {
        return new AdaptationSet(i, i2, list, list2, list3, list4);
    }

    protected int parseContentType(XmlPullParser xmlPullParser) {
        String attributeValue = xmlPullParser.getAttributeValue(null, "contentType");
        if (TextUtils.isEmpty(attributeValue)) {
            return -1;
        }
        if (MediaStreamTrack.AUDIO_TRACK_KIND.equals(attributeValue)) {
            return 1;
        }
        if (MediaStreamTrack.VIDEO_TRACK_KIND.equals(attributeValue)) {
            return 2;
        }
        return "text".equals(attributeValue) ? 3 : -1;
    }

    protected int getContentType(Format format) {
        String str = format.sampleMimeType;
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        if (MimeTypes.isVideo(str)) {
            return 2;
        }
        if (MimeTypes.isAudio(str)) {
            return 1;
        }
        return mimeTypeIsRawText(str) ? 3 : -1;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0091  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00f7  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0098  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected Pair<String, DrmInitData.SchemeData> parseContentProtection(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        String str;
        UUID uuid;
        UUID uuid2;
        byte[] bArr;
        byte[] bArr2;
        DrmInitData.SchemeData schemeData = null;
        String attributeValue = xmlPullParser.getAttributeValue(null, "schemeIdUri");
        if (attributeValue != null) {
            String lowerInvariant = Util.toLowerInvariant(attributeValue);
            lowerInvariant.hashCode();
            char c = 65535;
            switch (lowerInvariant.hashCode()) {
                case 489446379:
                    if (lowerInvariant.equals("urn:uuid:9a04f079-9840-4286-ab92-e65be0885f95")) {
                        c = 0;
                        break;
                    }
                    break;
                case 755418770:
                    if (lowerInvariant.equals("urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1812765994:
                    if (lowerInvariant.equals("urn:mpeg:dash:mp4protection:2011")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    uuid = C.PLAYREADY_UUID;
                    str = null;
                    uuid2 = null;
                    bArr = uuid2;
                    bArr2 = uuid2;
                    break;
                case 1:
                    uuid = C.WIDEVINE_UUID;
                    str = null;
                    uuid2 = null;
                    bArr = uuid2;
                    bArr2 = uuid2;
                    break;
                case 2:
                    str = xmlPullParser.getAttributeValue(null, "value");
                    String attributeValueIgnorePrefix = XmlPullParserUtil.getAttributeValueIgnorePrefix(xmlPullParser, "default_KID");
                    if (!TextUtils.isEmpty(attributeValueIgnorePrefix) && !"00000000-0000-0000-0000-000000000000".equals(attributeValueIgnorePrefix)) {
                        String[] split = attributeValueIgnorePrefix.split("\\s+");
                        UUID[] uuidArr = new UUID[split.length];
                        for (int i = 0; i < split.length; i++) {
                            uuidArr[i] = UUID.fromString(split[i]);
                        }
                        uuid = C.COMMON_PSSH_UUID;
                        bArr = null;
                        bArr2 = PsshAtomUtil.buildPsshAtom(uuid, uuidArr, null);
                        break;
                    } else {
                        uuid = null;
                        uuid2 = uuid;
                        bArr = uuid2;
                        bArr2 = uuid2;
                        break;
                    }
            }
            do {
                xmlPullParser.next();
                if (!XmlPullParserUtil.isStartTag(xmlPullParser, "ms:laurl")) {
                    bArr = xmlPullParser.getAttributeValue(null, "licenseUrl");
                    bArr2 = bArr2;
                } else if (bArr2 == null && XmlPullParserUtil.isStartTagIgnorePrefix(xmlPullParser, "pssh") && xmlPullParser.next() == 4) {
                    byte[] decode = Base64.decode(xmlPullParser.getText(), 0);
                    UUID parseUuid = PsshAtomUtil.parseUuid(decode);
                    if (parseUuid == null) {
                        Log.w("MpdParser", "Skipping malformed cenc:pssh data");
                        uuid = parseUuid;
                        bArr2 = null;
                    } else {
                        bArr2 = decode;
                        uuid = parseUuid;
                    }
                } else {
                    if (bArr2 == null) {
                        UUID uuid3 = C.PLAYREADY_UUID;
                        if (uuid3.equals(uuid) && XmlPullParserUtil.isStartTag(xmlPullParser, "mspr:pro") && xmlPullParser.next() == 4) {
                            bArr2 = PsshAtomUtil.buildPsshAtom(uuid3, Base64.decode(xmlPullParser.getText(), 0));
                        }
                    }
                    maybeSkipTag(xmlPullParser);
                    bArr2 = bArr2;
                }
            } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "ContentProtection"));
            if (uuid != null) {
                schemeData = new DrmInitData.SchemeData(uuid, bArr, "video/mp4", bArr2);
            }
            return Pair.create(str, schemeData);
        }
        str = null;
        uuid = null;
        uuid2 = uuid;
        bArr = uuid2;
        bArr2 = uuid2;
        do {
            xmlPullParser.next();
            if (!XmlPullParserUtil.isStartTag(xmlPullParser, "ms:laurl")) {
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "ContentProtection"));
        if (uuid != null) {
        }
        return Pair.create(str, schemeData);
    }

    protected void parseAdaptationSetChild(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        maybeSkipTag(xmlPullParser);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x019f A[LOOP:0: B:2:0x0065->B:10:0x019f, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:11:0x015b A[EDGE_INSN: B:11:0x015b->B:12:0x015b ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected RepresentationInfo parseRepresentation(XmlPullParser xmlPullParser, String str, String str2, String str3, int i, int i2, float f, int i3, int i4, String str4, List<Descriptor> list, List<Descriptor> list2, List<Descriptor> list3, List<Descriptor> list4, SegmentBase segmentBase, long j) throws XmlPullParserException, IOException {
        int i5;
        int i6;
        String str5;
        SegmentBase parseSegmentTemplate;
        int i7;
        String str6;
        String attributeValue = xmlPullParser.getAttributeValue(null, "id");
        int parseInt = parseInt(xmlPullParser, "bandwidth", -1);
        String parseString = parseString(xmlPullParser, "mimeType", str2);
        String parseString2 = parseString(xmlPullParser, "codecs", str3);
        int parseInt2 = parseInt(xmlPullParser, "width", i);
        int parseInt3 = parseInt(xmlPullParser, "height", i2);
        float parseFrameRate = parseFrameRate(xmlPullParser, f);
        int parseInt4 = parseInt(xmlPullParser, "audioSamplingRate", i4);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList(list3);
        ArrayList arrayList4 = new ArrayList(list4);
        int i8 = i3;
        SegmentBase.SingleSegmentBase singleSegmentBase = segmentBase;
        String str7 = null;
        boolean z = false;
        String str8 = str;
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "BaseURL")) {
                if (!z) {
                    str8 = parseBaseUrl(xmlPullParser, str8);
                    i5 = parseInt4;
                    i6 = parseInt;
                    i7 = i8;
                    z = true;
                    singleSegmentBase = singleSegmentBase;
                    str6 = str8;
                    if (!XmlPullParserUtil.isEndTag(xmlPullParser, "Representation")) {
                        break;
                    }
                    parseInt4 = i5;
                    str8 = str6;
                    parseInt = i6;
                    i8 = i7;
                } else {
                    str5 = str8;
                    i5 = parseInt4;
                    i6 = parseInt;
                    i7 = i8;
                    str6 = str5;
                    if (!XmlPullParserUtil.isEndTag(xmlPullParser, "Representation")) {
                    }
                }
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "AudioChannelConfiguration")) {
                i7 = parseAudioChannelConfiguration(xmlPullParser);
                str6 = str8;
                i5 = parseInt4;
                i6 = parseInt;
                if (!XmlPullParserUtil.isEndTag(xmlPullParser, "Representation")) {
                }
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentBase")) {
                i5 = parseInt4;
                i6 = parseInt;
                singleSegmentBase = parseSegmentBase(xmlPullParser, (SegmentBase.SingleSegmentBase) singleSegmentBase);
                i7 = i8;
                str6 = str8;
                if (!XmlPullParserUtil.isEndTag(xmlPullParser, "Representation")) {
                }
            } else {
                if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentList")) {
                    i5 = parseInt4;
                    i6 = parseInt;
                    parseSegmentTemplate = parseSegmentList(xmlPullParser, (SegmentBase.SegmentList) singleSegmentBase, j);
                } else {
                    i5 = parseInt4;
                    i6 = parseInt;
                    if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentTemplate")) {
                        parseSegmentTemplate = parseSegmentTemplate(xmlPullParser, (SegmentBase.SegmentTemplate) singleSegmentBase, list4, j);
                    } else {
                        if (XmlPullParserUtil.isStartTag(xmlPullParser, "ContentProtection")) {
                            Pair<String, DrmInitData.SchemeData> parseContentProtection = parseContentProtection(xmlPullParser);
                            str5 = str8;
                            Object obj = parseContentProtection.first;
                            if (obj != null) {
                                str7 = (String) obj;
                            }
                            Object obj2 = parseContentProtection.second;
                            if (obj2 != null) {
                                arrayList.add((DrmInitData.SchemeData) obj2);
                            }
                        } else {
                            str5 = str8;
                            if (XmlPullParserUtil.isStartTag(xmlPullParser, "InbandEventStream")) {
                                arrayList2.add(parseDescriptor(xmlPullParser, "InbandEventStream"));
                            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "EssentialProperty")) {
                                arrayList3.add(parseDescriptor(xmlPullParser, "EssentialProperty"));
                            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SupplementalProperty")) {
                                arrayList4.add(parseDescriptor(xmlPullParser, "SupplementalProperty"));
                            } else {
                                maybeSkipTag(xmlPullParser);
                            }
                        }
                        i7 = i8;
                        str6 = str5;
                        if (!XmlPullParserUtil.isEndTag(xmlPullParser, "Representation")) {
                        }
                    }
                }
                singleSegmentBase = parseSegmentTemplate;
                i7 = i8;
                str6 = str8;
                if (!XmlPullParserUtil.isEndTag(xmlPullParser, "Representation")) {
                }
            }
        }
        return new RepresentationInfo(buildFormat(attributeValue, parseString, parseInt2, parseInt3, parseFrameRate, i7, i5, i6, str4, list, list2, parseString2, arrayList3, arrayList4), str6, singleSegmentBase != null ? singleSegmentBase : new SegmentBase.SingleSegmentBase(), str7, arrayList, arrayList2, -1L);
    }

    protected Format buildFormat(String str, String str2, int i, int i2, float f, int i3, int i4, int i5, String str3, List<Descriptor> list, List<Descriptor> list2, String str4, List<Descriptor> list3, List<Descriptor> list4) {
        String str5;
        int i6;
        int parseCea708AccessibilityChannel;
        String sampleMimeType = getSampleMimeType(str2, str4);
        int parseSelectionFlagsFromRoleDescriptors = parseSelectionFlagsFromRoleDescriptors(list);
        int parseRoleFlagsFromRoleDescriptors = parseRoleFlagsFromRoleDescriptors(list) | parseRoleFlagsFromAccessibilityDescriptors(list2) | parseRoleFlagsFromProperties(list3) | parseRoleFlagsFromProperties(list4);
        if (sampleMimeType != null) {
            String parseEac3SupplementalProperties = "audio/eac3".equals(sampleMimeType) ? parseEac3SupplementalProperties(list4) : sampleMimeType;
            if (MimeTypes.isVideo(parseEac3SupplementalProperties)) {
                return Format.createVideoContainerFormat(str, null, str2, parseEac3SupplementalProperties, str4, null, i5, i, i2, f, null, parseSelectionFlagsFromRoleDescriptors, parseRoleFlagsFromRoleDescriptors);
            }
            if (MimeTypes.isAudio(parseEac3SupplementalProperties)) {
                return Format.createAudioContainerFormat(str, null, str2, parseEac3SupplementalProperties, str4, null, i5, i3, i4, null, parseSelectionFlagsFromRoleDescriptors, parseRoleFlagsFromRoleDescriptors, str3);
            }
            if (mimeTypeIsRawText(parseEac3SupplementalProperties)) {
                if ("application/cea-608".equals(parseEac3SupplementalProperties)) {
                    parseCea708AccessibilityChannel = parseCea608AccessibilityChannel(list2);
                } else if ("application/cea-708".equals(parseEac3SupplementalProperties)) {
                    parseCea708AccessibilityChannel = parseCea708AccessibilityChannel(list2);
                } else {
                    i6 = -1;
                    return Format.createTextContainerFormat(str, null, str2, parseEac3SupplementalProperties, str4, i5, parseSelectionFlagsFromRoleDescriptors, parseRoleFlagsFromRoleDescriptors, str3, i6);
                }
                i6 = parseCea708AccessibilityChannel;
                return Format.createTextContainerFormat(str, null, str2, parseEac3SupplementalProperties, str4, i5, parseSelectionFlagsFromRoleDescriptors, parseRoleFlagsFromRoleDescriptors, str3, i6);
            }
            str5 = parseEac3SupplementalProperties;
        } else {
            str5 = sampleMimeType;
        }
        return Format.createContainerFormat(str, null, str2, str5, str4, i5, parseSelectionFlagsFromRoleDescriptors, parseRoleFlagsFromRoleDescriptors, str3);
    }

    protected Representation buildRepresentation(RepresentationInfo representationInfo, String str, String str2, ArrayList<DrmInitData.SchemeData> arrayList, ArrayList<Descriptor> arrayList2) {
        Format format = representationInfo.format;
        if (str != null) {
            format = format.copyWithLabel(str);
        }
        String str3 = representationInfo.drmSchemeType;
        if (str3 != null) {
            str2 = str3;
        }
        ArrayList<DrmInitData.SchemeData> arrayList3 = representationInfo.drmSchemeDatas;
        arrayList3.addAll(arrayList);
        if (!arrayList3.isEmpty()) {
            filterRedundantIncompleteSchemeDatas(arrayList3);
            format = format.copyWithDrmInitData(new DrmInitData(str2, arrayList3));
        }
        ArrayList<Descriptor> arrayList4 = representationInfo.inbandEventStreams;
        arrayList4.addAll(arrayList2);
        return Representation.newInstance(representationInfo.revisionId, format, representationInfo.baseUrl, representationInfo.segmentBase, arrayList4);
    }

    protected SegmentBase.SingleSegmentBase parseSegmentBase(XmlPullParser xmlPullParser, SegmentBase.SingleSegmentBase singleSegmentBase) throws XmlPullParserException, IOException {
        long j;
        long j2;
        long parseLong = parseLong(xmlPullParser, "timescale", singleSegmentBase != null ? singleSegmentBase.timescale : 1L);
        long j3 = 0;
        long parseLong2 = parseLong(xmlPullParser, "presentationTimeOffset", singleSegmentBase != null ? singleSegmentBase.presentationTimeOffset : 0L);
        long j4 = singleSegmentBase != null ? singleSegmentBase.indexStart : 0L;
        if (singleSegmentBase != null) {
            j3 = singleSegmentBase.indexLength;
        }
        RangedUri rangedUri = null;
        String attributeValue = xmlPullParser.getAttributeValue(null, "indexRange");
        if (attributeValue != null) {
            String[] split = attributeValue.split("-");
            long parseLong3 = Long.parseLong(split[0]);
            j = (Long.parseLong(split[1]) - parseLong3) + 1;
            j2 = parseLong3;
        } else {
            j = j3;
            j2 = j4;
        }
        if (singleSegmentBase != null) {
            rangedUri = singleSegmentBase.initialization;
        }
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Initialization")) {
                rangedUri = parseInitialization(xmlPullParser);
            } else {
                maybeSkipTag(xmlPullParser);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "SegmentBase"));
        return buildSingleSegmentBase(rangedUri, parseLong, parseLong2, j2, j);
    }

    protected SegmentBase.SingleSegmentBase buildSingleSegmentBase(RangedUri rangedUri, long j, long j2, long j3, long j4) {
        return new SegmentBase.SingleSegmentBase(rangedUri, j, j2, j3, j4);
    }

    protected SegmentBase.SegmentList parseSegmentList(XmlPullParser xmlPullParser, SegmentBase.SegmentList segmentList, long j) throws XmlPullParserException, IOException {
        long j2 = 1;
        long parseLong = parseLong(xmlPullParser, "timescale", segmentList != null ? segmentList.timescale : 1L);
        long parseLong2 = parseLong(xmlPullParser, "presentationTimeOffset", segmentList != null ? segmentList.presentationTimeOffset : 0L);
        long parseLong3 = parseLong(xmlPullParser, "duration", segmentList != null ? segmentList.duration : -9223372036854775807L);
        if (segmentList != null) {
            j2 = segmentList.startNumber;
        }
        long parseLong4 = parseLong(xmlPullParser, "startNumber", j2);
        List<SegmentBase.SegmentTimelineElement> list = null;
        List<RangedUri> list2 = null;
        RangedUri rangedUri = null;
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Initialization")) {
                rangedUri = parseInitialization(xmlPullParser);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentTimeline")) {
                list = parseSegmentTimeline(xmlPullParser, parseLong, j);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentURL")) {
                if (list2 == null) {
                    list2 = new ArrayList<>();
                }
                list2.add(parseSegmentUrl(xmlPullParser));
            } else {
                maybeSkipTag(xmlPullParser);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "SegmentList"));
        if (segmentList != null) {
            if (rangedUri == null) {
                rangedUri = segmentList.initialization;
            }
            if (list == null) {
                list = segmentList.segmentTimeline;
            }
            if (list2 == null) {
                list2 = segmentList.mediaSegments;
            }
        }
        return buildSegmentList(rangedUri, parseLong, parseLong2, parseLong4, parseLong3, list, list2);
    }

    protected SegmentBase.SegmentList buildSegmentList(RangedUri rangedUri, long j, long j2, long j3, long j4, List<SegmentBase.SegmentTimelineElement> list, List<RangedUri> list2) {
        return new SegmentBase.SegmentList(rangedUri, j, j2, j3, j4, list, list2);
    }

    protected SegmentBase.SegmentTemplate parseSegmentTemplate(XmlPullParser xmlPullParser, SegmentBase.SegmentTemplate segmentTemplate, List<Descriptor> list, long j) throws XmlPullParserException, IOException {
        long j2 = 1;
        long parseLong = parseLong(xmlPullParser, "timescale", segmentTemplate != null ? segmentTemplate.timescale : 1L);
        long parseLong2 = parseLong(xmlPullParser, "presentationTimeOffset", segmentTemplate != null ? segmentTemplate.presentationTimeOffset : 0L);
        long parseLong3 = parseLong(xmlPullParser, "duration", segmentTemplate != null ? segmentTemplate.duration : -9223372036854775807L);
        if (segmentTemplate != null) {
            j2 = segmentTemplate.startNumber;
        }
        long parseLong4 = parseLong(xmlPullParser, "startNumber", j2);
        long parseLastSegmentNumberSupplementalProperty = parseLastSegmentNumberSupplementalProperty(list);
        List<SegmentBase.SegmentTimelineElement> list2 = null;
        UrlTemplate parseUrlTemplate = parseUrlTemplate(xmlPullParser, "media", segmentTemplate != null ? segmentTemplate.mediaTemplate : null);
        UrlTemplate parseUrlTemplate2 = parseUrlTemplate(xmlPullParser, "initialization", segmentTemplate != null ? segmentTemplate.initializationTemplate : null);
        RangedUri rangedUri = null;
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Initialization")) {
                rangedUri = parseInitialization(xmlPullParser);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentTimeline")) {
                list2 = parseSegmentTimeline(xmlPullParser, parseLong, j);
            } else {
                maybeSkipTag(xmlPullParser);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "SegmentTemplate"));
        if (segmentTemplate != null) {
            if (rangedUri == null) {
                rangedUri = segmentTemplate.initialization;
            }
            if (list2 == null) {
                list2 = segmentTemplate.segmentTimeline;
            }
        }
        return buildSegmentTemplate(rangedUri, parseLong, parseLong2, parseLong4, parseLastSegmentNumberSupplementalProperty, parseLong3, list2, parseUrlTemplate2, parseUrlTemplate);
    }

    protected SegmentBase.SegmentTemplate buildSegmentTemplate(RangedUri rangedUri, long j, long j2, long j3, long j4, long j5, List<SegmentBase.SegmentTimelineElement> list, UrlTemplate urlTemplate, UrlTemplate urlTemplate2) {
        return new SegmentBase.SegmentTemplate(rangedUri, j, j2, j3, j4, j5, list, urlTemplate, urlTemplate2);
    }

    protected EventStream parseEventStream(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        String parseString = parseString(xmlPullParser, "schemeIdUri", "");
        String parseString2 = parseString(xmlPullParser, "value", "");
        long parseLong = parseLong(xmlPullParser, "timescale", 1L);
        ArrayList arrayList = new ArrayList();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Event")) {
                arrayList.add(parseEvent(xmlPullParser, parseString, parseString2, parseLong, byteArrayOutputStream));
            } else {
                maybeSkipTag(xmlPullParser);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "EventStream"));
        long[] jArr = new long[arrayList.size()];
        EventMessage[] eventMessageArr = new EventMessage[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            Pair pair = (Pair) arrayList.get(i);
            jArr[i] = ((Long) pair.first).longValue();
            eventMessageArr[i] = (EventMessage) pair.second;
        }
        return buildEventStream(parseString, parseString2, parseLong, jArr, eventMessageArr);
    }

    protected EventStream buildEventStream(String str, String str2, long j, long[] jArr, EventMessage[] eventMessageArr) {
        return new EventStream(str, str2, j, jArr, eventMessageArr);
    }

    protected Pair<Long, EventMessage> parseEvent(XmlPullParser xmlPullParser, String str, String str2, long j, ByteArrayOutputStream byteArrayOutputStream) throws IOException, XmlPullParserException {
        long parseLong = parseLong(xmlPullParser, "id", 0L);
        long parseLong2 = parseLong(xmlPullParser, "duration", -9223372036854775807L);
        long parseLong3 = parseLong(xmlPullParser, "presentationTime", 0L);
        long scaleLargeTimestamp = Util.scaleLargeTimestamp(parseLong2, 1000L, j);
        long scaleLargeTimestamp2 = Util.scaleLargeTimestamp(parseLong3, 1000000L, j);
        String parseString = parseString(xmlPullParser, "messageData", null);
        byte[] parseEventObject = parseEventObject(xmlPullParser, byteArrayOutputStream);
        Long valueOf = Long.valueOf(scaleLargeTimestamp2);
        if (parseString != null) {
            parseEventObject = Util.getUtf8Bytes(parseString);
        }
        return Pair.create(valueOf, buildEvent(str, str2, parseLong, scaleLargeTimestamp, parseEventObject));
    }

    protected byte[] parseEventObject(XmlPullParser xmlPullParser, ByteArrayOutputStream byteArrayOutputStream) throws XmlPullParserException, IOException {
        byteArrayOutputStream.reset();
        XmlSerializer newSerializer = Xml.newSerializer();
        newSerializer.setOutput(byteArrayOutputStream, "UTF-8");
        xmlPullParser.nextToken();
        while (!XmlPullParserUtil.isEndTag(xmlPullParser, "Event")) {
            switch (xmlPullParser.getEventType()) {
                case 0:
                    newSerializer.startDocument(null, Boolean.FALSE);
                    break;
                case 1:
                    newSerializer.endDocument();
                    break;
                case 2:
                    newSerializer.startTag(xmlPullParser.getNamespace(), xmlPullParser.getName());
                    for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
                        newSerializer.attribute(xmlPullParser.getAttributeNamespace(i), xmlPullParser.getAttributeName(i), xmlPullParser.getAttributeValue(i));
                    }
                    break;
                case 3:
                    newSerializer.endTag(xmlPullParser.getNamespace(), xmlPullParser.getName());
                    break;
                case 4:
                    newSerializer.text(xmlPullParser.getText());
                    break;
                case 5:
                    newSerializer.cdsect(xmlPullParser.getText());
                    break;
                case 6:
                    newSerializer.entityRef(xmlPullParser.getText());
                    break;
                case 7:
                    newSerializer.ignorableWhitespace(xmlPullParser.getText());
                    break;
                case 8:
                    newSerializer.processingInstruction(xmlPullParser.getText());
                    break;
                case 9:
                    newSerializer.comment(xmlPullParser.getText());
                    break;
                case 10:
                    newSerializer.docdecl(xmlPullParser.getText());
                    break;
            }
            xmlPullParser.nextToken();
        }
        newSerializer.flush();
        return byteArrayOutputStream.toByteArray();
    }

    protected EventMessage buildEvent(String str, String str2, long j, long j2, byte[] bArr) {
        return new EventMessage(str, str2, j2, j, bArr);
    }

    protected List<SegmentBase.SegmentTimelineElement> parseSegmentTimeline(XmlPullParser xmlPullParser, long j, long j2) throws XmlPullParserException, IOException {
        ArrayList arrayList = new ArrayList();
        long j3 = 0;
        long j4 = -9223372036854775807L;
        boolean z = false;
        int i = 0;
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "S")) {
                long parseLong = parseLong(xmlPullParser, "t", -9223372036854775807L);
                if (z) {
                    j3 = addSegmentTimelineElementsToList(arrayList, j3, j4, i, parseLong);
                }
                if (parseLong == -9223372036854775807L) {
                    parseLong = j3;
                }
                j4 = parseLong(xmlPullParser, "d", -9223372036854775807L);
                i = parseInt(xmlPullParser, "r", 0);
                j3 = parseLong;
                z = true;
            } else {
                maybeSkipTag(xmlPullParser);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "SegmentTimeline"));
        if (z) {
            addSegmentTimelineElementsToList(arrayList, j3, j4, i, Util.scaleLargeTimestamp(j2, j, 1000L));
        }
        return arrayList;
    }

    private long addSegmentTimelineElementsToList(List<SegmentBase.SegmentTimelineElement> list, long j, long j2, int i, long j3) {
        int ceilDivide = i >= 0 ? i + 1 : (int) Util.ceilDivide(j3 - j, j2);
        for (int i2 = 0; i2 < ceilDivide; i2++) {
            list.add(buildSegmentTimelineElement(j, j2));
            j += j2;
        }
        return j;
    }

    protected SegmentBase.SegmentTimelineElement buildSegmentTimelineElement(long j, long j2) {
        return new SegmentBase.SegmentTimelineElement(j, j2);
    }

    protected UrlTemplate parseUrlTemplate(XmlPullParser xmlPullParser, String str, UrlTemplate urlTemplate) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue != null ? UrlTemplate.compile(attributeValue) : urlTemplate;
    }

    protected RangedUri parseInitialization(XmlPullParser xmlPullParser) {
        return parseRangedUrl(xmlPullParser, "sourceURL", "range");
    }

    protected RangedUri parseSegmentUrl(XmlPullParser xmlPullParser) {
        return parseRangedUrl(xmlPullParser, "media", "mediaRange");
    }

    protected RangedUri parseRangedUrl(XmlPullParser xmlPullParser, String str, String str2) {
        long j;
        long j2;
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        String attributeValue2 = xmlPullParser.getAttributeValue(null, str2);
        if (attributeValue2 != null) {
            String[] split = attributeValue2.split("-");
            j = Long.parseLong(split[0]);
            if (split.length == 2) {
                j2 = (Long.parseLong(split[1]) - j) + 1;
                return buildRangedUri(attributeValue, j, j2);
            }
        } else {
            j = 0;
        }
        j2 = -1;
        return buildRangedUri(attributeValue, j, j2);
    }

    protected RangedUri buildRangedUri(String str, long j, long j2) {
        return new RangedUri(str, j, j2);
    }

    protected ProgramInformation parseProgramInformation(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        String str = null;
        String parseString = parseString(xmlPullParser, "moreInformationURL", null);
        String parseString2 = parseString(xmlPullParser, "lang", null);
        String str2 = null;
        String str3 = null;
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Title")) {
                str = xmlPullParser.nextText();
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "Source")) {
                str2 = xmlPullParser.nextText();
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "Copyright")) {
                str3 = xmlPullParser.nextText();
            } else {
                maybeSkipTag(xmlPullParser);
            }
            String str4 = str3;
            if (XmlPullParserUtil.isEndTag(xmlPullParser, "ProgramInformation")) {
                return new ProgramInformation(str, str2, str4, parseString, parseString2);
            }
            str3 = str4;
        }
    }

    protected String parseLabel(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        return parseText(xmlPullParser, "Label");
    }

    protected String parseBaseUrl(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException {
        return UriUtil.resolve(str, parseText(xmlPullParser, "BaseURL"));
    }

    protected int parseAudioChannelConfiguration(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        String parseString = parseString(xmlPullParser, "schemeIdUri", null);
        int i = -1;
        if ("urn:mpeg:dash:23003:3:audio_channel_configuration:2011".equals(parseString)) {
            i = parseInt(xmlPullParser, "value", -1);
        } else if ("tag:dolby.com,2014:dash:audio_channel_configuration:2011".equals(parseString) || "urn:dolby:dash:audio_channel_configuration:2011".equals(parseString)) {
            i = parseDolbyChannelConfiguration(xmlPullParser);
        }
        do {
            xmlPullParser.next();
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "AudioChannelConfiguration"));
        return i;
    }

    protected int parseSelectionFlagsFromRoleDescriptors(List<Descriptor> list) {
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = list.get(i);
            if ("urn:mpeg:dash:role:2011".equalsIgnoreCase(descriptor.schemeIdUri) && "main".equals(descriptor.value)) {
                return 1;
            }
        }
        return 0;
    }

    protected int parseRoleFlagsFromRoleDescriptors(List<Descriptor> list) {
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            Descriptor descriptor = list.get(i2);
            if ("urn:mpeg:dash:role:2011".equalsIgnoreCase(descriptor.schemeIdUri)) {
                i |= parseDashRoleSchemeValue(descriptor.value);
            }
        }
        return i;
    }

    protected int parseRoleFlagsFromAccessibilityDescriptors(List<Descriptor> list) {
        int parseTvaAudioPurposeCsValue;
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            Descriptor descriptor = list.get(i2);
            if ("urn:mpeg:dash:role:2011".equalsIgnoreCase(descriptor.schemeIdUri)) {
                parseTvaAudioPurposeCsValue = parseDashRoleSchemeValue(descriptor.value);
            } else if ("urn:tva:metadata:cs:AudioPurposeCS:2007".equalsIgnoreCase(descriptor.schemeIdUri)) {
                parseTvaAudioPurposeCsValue = parseTvaAudioPurposeCsValue(descriptor.value);
            }
            i |= parseTvaAudioPurposeCsValue;
        }
        return i;
    }

    protected int parseRoleFlagsFromProperties(List<Descriptor> list) {
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            if ("http://dashif.org/guidelines/trickmode".equalsIgnoreCase(list.get(i2).schemeIdUri)) {
                i |= 16384;
            }
        }
        return i;
    }

    protected int parseDashRoleSchemeValue(String str) {
        if (str == null) {
            return 0;
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -2060497896:
                if (str.equals("subtitle")) {
                    c = 0;
                    break;
                }
                break;
            case -1724546052:
                if (str.equals("description")) {
                    c = 1;
                    break;
                }
                break;
            case -1580883024:
                if (str.equals("enhanced-audio-intelligibility")) {
                    c = 2;
                    break;
                }
                break;
            case -1408024454:
                if (str.equals("alternate")) {
                    c = 3;
                    break;
                }
                break;
            case 99825:
                if (str.equals("dub")) {
                    c = 4;
                    break;
                }
                break;
            case 3343801:
                if (str.equals("main")) {
                    c = 5;
                    break;
                }
                break;
            case 3530173:
                if (str.equals("sign")) {
                    c = 6;
                    break;
                }
                break;
            case 552573414:
                if (str.equals("caption")) {
                    c = 7;
                    break;
                }
                break;
            case 899152809:
                if (str.equals("commentary")) {
                    c = '\b';
                    break;
                }
                break;
            case 1629013393:
                if (str.equals("emergency")) {
                    c = '\t';
                    break;
                }
                break;
            case 1855372047:
                if (str.equals("supplementary")) {
                    c = '\n';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return ConnectionsManager.RequestFlagNeedQuickAck;
            case 1:
                return 512;
            case 2:
                return 2048;
            case 3:
                return 2;
            case 4:
                return 16;
            case 5:
                return 1;
            case 6:
                return 256;
            case 7:
                return 64;
            case '\b':
                return 8;
            case '\t':
                return 32;
            case '\n':
                return 4;
            default:
                return 0;
        }
    }

    protected int parseTvaAudioPurposeCsValue(String str) {
        if (str == null) {
            return 0;
        }
        char c = 65535;
        switch (str.hashCode()) {
            case 49:
                if (str.equals("1")) {
                    c = 0;
                    break;
                }
                break;
            case 50:
                if (str.equals("2")) {
                    c = 1;
                    break;
                }
                break;
            case 51:
                if (str.equals("3")) {
                    c = 2;
                    break;
                }
                break;
            case 52:
                if (str.equals("4")) {
                    c = 3;
                    break;
                }
                break;
            case 54:
                if (str.equals("6")) {
                    c = 4;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 512;
            case 1:
                return 2048;
            case 2:
                return 4;
            case 3:
                return 8;
            case 4:
                return 1;
            default:
                return 0;
        }
    }

    public static void maybeSkipTag(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        if (!XmlPullParserUtil.isStartTag(xmlPullParser)) {
            return;
        }
        int i = 1;
        while (i != 0) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser)) {
                i++;
            } else if (XmlPullParserUtil.isEndTag(xmlPullParser)) {
                i--;
            }
        }
    }

    private static void filterRedundantIncompleteSchemeDatas(ArrayList<DrmInitData.SchemeData> arrayList) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            DrmInitData.SchemeData schemeData = arrayList.get(size);
            if (!schemeData.hasData()) {
                int i = 0;
                while (true) {
                    if (i >= arrayList.size()) {
                        break;
                    } else if (arrayList.get(i).canReplace(schemeData)) {
                        arrayList.remove(size);
                        break;
                    } else {
                        i++;
                    }
                }
            }
        }
    }

    private static String getSampleMimeType(String str, String str2) {
        if (MimeTypes.isAudio(str)) {
            return MimeTypes.getAudioMediaMimeType(str2);
        }
        if (MimeTypes.isVideo(str)) {
            return MimeTypes.getVideoMediaMimeType(str2);
        }
        if (mimeTypeIsRawText(str)) {
            return str;
        }
        if ("application/mp4".equals(str)) {
            if (str2 != null) {
                if (str2.startsWith("stpp")) {
                    return "application/ttml+xml";
                }
                if (str2.startsWith("wvtt")) {
                    return "application/x-mp4-vtt";
                }
            }
        } else if ("application/x-rawcc".equals(str) && str2 != null) {
            if (str2.contains("cea708")) {
                return "application/cea-708";
            }
            if (str2.contains("eia608") || str2.contains("cea608")) {
                return "application/cea-608";
            }
        }
        return null;
    }

    private static boolean mimeTypeIsRawText(String str) {
        return MimeTypes.isText(str) || "application/ttml+xml".equals(str) || "application/x-mp4-vtt".equals(str) || "application/cea-708".equals(str) || "application/cea-608".equals(str);
    }

    private static String checkLanguageConsistency(String str, String str2) {
        if (str == null) {
            return str2;
        }
        if (str2 == null) {
            return str;
        }
        Assertions.checkState(str.equals(str2));
        return str;
    }

    private static int checkContentTypeConsistency(int i, int i2) {
        if (i == -1) {
            return i2;
        }
        if (i2 == -1) {
            return i;
        }
        Assertions.checkState(i == i2);
        return i;
    }

    protected static Descriptor parseDescriptor(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException {
        String parseString = parseString(xmlPullParser, "schemeIdUri", "");
        String parseString2 = parseString(xmlPullParser, "value", null);
        String parseString3 = parseString(xmlPullParser, "id", null);
        do {
            xmlPullParser.next();
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, str));
        return new Descriptor(parseString, parseString2, parseString3);
    }

    protected static int parseCea608AccessibilityChannel(List<Descriptor> list) {
        String str;
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = list.get(i);
            if ("urn:scte:dash:cc:cea-608:2015".equals(descriptor.schemeIdUri) && (str = descriptor.value) != null) {
                Matcher matcher = CEA_608_ACCESSIBILITY_PATTERN.matcher(str);
                if (matcher.matches()) {
                    return Integer.parseInt(matcher.group(1));
                }
                Log.w("MpdParser", "Unable to parse CEA-608 channel number from: " + descriptor.value);
            }
        }
        return -1;
    }

    protected static int parseCea708AccessibilityChannel(List<Descriptor> list) {
        String str;
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = list.get(i);
            if ("urn:scte:dash:cc:cea-708:2015".equals(descriptor.schemeIdUri) && (str = descriptor.value) != null) {
                Matcher matcher = CEA_708_ACCESSIBILITY_PATTERN.matcher(str);
                if (matcher.matches()) {
                    return Integer.parseInt(matcher.group(1));
                }
                Log.w("MpdParser", "Unable to parse CEA-708 service block number from: " + descriptor.value);
            }
        }
        return -1;
    }

    protected static String parseEac3SupplementalProperties(List<Descriptor> list) {
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = list.get(i);
            String str = descriptor.schemeIdUri;
            if ("tag:dolby.com,2018:dash:EC3_ExtensionType:2018".equals(str) && "JOC".equals(descriptor.value)) {
                return "audio/eac3-joc";
            }
            if ("tag:dolby.com,2014:dash:DolbyDigitalPlusExtensionType:2014".equals(str) && "ec+3".equals(descriptor.value)) {
                return "audio/eac3-joc";
            }
        }
        return "audio/eac3";
    }

    protected static float parseFrameRate(XmlPullParser xmlPullParser, float f) {
        String attributeValue = xmlPullParser.getAttributeValue(null, "frameRate");
        if (attributeValue != null) {
            Matcher matcher = FRAME_RATE_PATTERN.matcher(attributeValue);
            if (!matcher.matches()) {
                return f;
            }
            int parseInt = Integer.parseInt(matcher.group(1));
            String group = matcher.group(2);
            return !TextUtils.isEmpty(group) ? parseInt / Integer.parseInt(group) : parseInt;
        }
        return f;
    }

    protected static long parseDuration(XmlPullParser xmlPullParser, String str, long j) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? j : Util.parseXsDuration(attributeValue);
    }

    protected static long parseDateTime(XmlPullParser xmlPullParser, String str, long j) throws ParserException {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? j : Util.parseXsDateTime(attributeValue);
    }

    protected static String parseText(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException {
        String str2 = "";
        do {
            xmlPullParser.next();
            if (xmlPullParser.getEventType() == 4) {
                str2 = xmlPullParser.getText();
            } else {
                maybeSkipTag(xmlPullParser);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, str));
        return str2;
    }

    protected static int parseInt(XmlPullParser xmlPullParser, String str, int i) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? i : Integer.parseInt(attributeValue);
    }

    protected static long parseLong(XmlPullParser xmlPullParser, String str, long j) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? j : Long.parseLong(attributeValue);
    }

    protected static String parseString(XmlPullParser xmlPullParser, String str, String str2) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? str2 : attributeValue;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    protected static int parseDolbyChannelConfiguration(XmlPullParser xmlPullParser) {
        char c;
        String lowerInvariant = Util.toLowerInvariant(xmlPullParser.getAttributeValue(null, "value"));
        if (lowerInvariant == null) {
            return -1;
        }
        switch (lowerInvariant.hashCode()) {
            case 1596796:
                if (lowerInvariant.equals("4000")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 2937391:
                if (lowerInvariant.equals("a000")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 3094035:
                if (lowerInvariant.equals("f801")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 3133436:
                if (lowerInvariant.equals("fa01")) {
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
                return 1;
            case 1:
                return 2;
            case 2:
                return 6;
            case 3:
                return 8;
            default:
                return -1;
        }
    }

    protected static long parseLastSegmentNumberSupplementalProperty(List<Descriptor> list) {
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = list.get(i);
            if ("http://dashif.org/guidelines/last-segment-number".equalsIgnoreCase(descriptor.schemeIdUri)) {
                return Long.parseLong(descriptor.value);
            }
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static final class RepresentationInfo {
        public final String baseUrl;
        public final ArrayList<DrmInitData.SchemeData> drmSchemeDatas;
        public final String drmSchemeType;
        public final Format format;
        public final ArrayList<Descriptor> inbandEventStreams;
        public final long revisionId;
        public final SegmentBase segmentBase;

        public RepresentationInfo(Format format, String str, SegmentBase segmentBase, String str2, ArrayList<DrmInitData.SchemeData> arrayList, ArrayList<Descriptor> arrayList2, long j) {
            this.format = format;
            this.baseUrl = str;
            this.segmentBase = segmentBase;
            this.drmSchemeType = str2;
            this.drmSchemeDatas = arrayList;
            this.inbandEventStreams = arrayList2;
            this.revisionId = j;
        }
    }
}
