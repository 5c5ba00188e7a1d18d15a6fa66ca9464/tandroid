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
import com.google.common.base.Ascii;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.R;
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
    private static final int[] MPEG_CHANNEL_CONFIGURATION_MAPPING = {-1, 1, 2, 3, 4, 5, 6, 8, 2, 3, 4, 7, 8, 24, 8, 12, 10, 12, 14, 12, 14};

    private static long getFinalAvailabilityTimeOffset(long j, long j2) {
        if (j2 != -9223372036854775807L) {
            j = j2;
        }
        if (j == Long.MAX_VALUE) {
            return -9223372036854775807L;
        }
        return j;
    }

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
                throw ParserException.createForMalformedManifest("inputStream does not contain a valid media presentation description", null);
            }
            return parseMediaPresentationDescription(newPullParser, uri);
        } catch (XmlPullParserException e) {
            throw ParserException.createForMalformedManifest(null, e);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:78:0x01c0  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01e0  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01e7 A[LOOP:0: B:25:0x00a5->B:82:0x01e7, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01a3 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected DashManifest parseMediaPresentationDescription(XmlPullParser xmlPullParser, Uri uri) throws XmlPullParserException, IOException {
        long j;
        ArrayList arrayList;
        ArrayList arrayList2;
        long j2;
        Throwable th;
        ArrayList arrayList3;
        long j3;
        boolean z;
        long j4;
        DashManifestParser dashManifestParser = this;
        boolean isDvbProfileDeclared = dashManifestParser.isDvbProfileDeclared(dashManifestParser.parseProfiles(xmlPullParser, "profiles", new String[0]));
        long j5 = -9223372036854775807L;
        long parseDateTime = parseDateTime(xmlPullParser, "availabilityStartTime", -9223372036854775807L);
        long parseDuration = parseDuration(xmlPullParser, "mediaPresentationDuration", -9223372036854775807L);
        long parseDuration2 = parseDuration(xmlPullParser, "minBufferTime", -9223372036854775807L);
        Throwable th2 = null;
        boolean equals = "dynamic".equals(xmlPullParser.getAttributeValue(null, "type"));
        long parseDuration3 = equals ? parseDuration(xmlPullParser, "minimumUpdatePeriod", -9223372036854775807L) : -9223372036854775807L;
        long parseDuration4 = equals ? parseDuration(xmlPullParser, "timeShiftBufferDepth", -9223372036854775807L) : -9223372036854775807L;
        long parseDuration5 = equals ? parseDuration(xmlPullParser, "suggestedPresentationDelay", -9223372036854775807L) : -9223372036854775807L;
        long parseDateTime2 = parseDateTime(xmlPullParser, "publishTime", -9223372036854775807L);
        long j6 = equals ? 0L : -9223372036854775807L;
        ArrayList newArrayList = Lists.newArrayList(new BaseUrl(uri.toString(), uri.toString(), isDvbProfileDeclared ? 1 : Integer.MIN_VALUE, 1));
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        long j7 = equals ? -9223372036854775807L : 0L;
        ProgramInformation programInformation = null;
        UtcTimingElement utcTimingElement = null;
        Uri uri2 = null;
        ServiceDescriptionElement serviceDescriptionElement = null;
        boolean z2 = false;
        boolean z3 = false;
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "BaseURL")) {
                if (!z2) {
                    j6 = dashManifestParser.parseAvailabilityTimeOffsetUs(xmlPullParser, j6);
                    z2 = true;
                }
                arrayList5.addAll(dashManifestParser.parseBaseUrl(xmlPullParser, newArrayList, isDvbProfileDeclared));
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "ProgramInformation")) {
                programInformation = parseProgramInformation(xmlPullParser);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "UTCTiming")) {
                utcTimingElement = parseUtcTiming(xmlPullParser);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "Location")) {
                uri2 = UriUtil.resolveToUri(uri.toString(), xmlPullParser.nextText());
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "ServiceDescription")) {
                serviceDescriptionElement = parseServiceDescription(xmlPullParser);
            } else {
                if (XmlPullParserUtil.isStartTag(xmlPullParser, "Period") && !z3) {
                    j = j6;
                    ArrayList arrayList6 = arrayList4;
                    arrayList = arrayList5;
                    arrayList2 = newArrayList;
                    j2 = j5;
                    th = th2;
                    Pair<Period, Long> parsePeriod = parsePeriod(xmlPullParser, !arrayList5.isEmpty() ? arrayList5 : newArrayList, j7, j, parseDateTime, parseDuration4, isDvbProfileDeclared);
                    Period period = (Period) parsePeriod.first;
                    if (period.startMs != j2) {
                        long longValue = ((Long) parsePeriod.second).longValue();
                        if (longValue == j2) {
                            arrayList3 = arrayList6;
                            j3 = j2;
                        } else {
                            j3 = period.startMs + longValue;
                            arrayList3 = arrayList6;
                        }
                        arrayList3.add(period);
                        j7 = j3;
                        z = z3;
                    } else if (!equals) {
                        throw ParserException.createForMalformedManifest("Unable to determine start of period " + arrayList6.size(), th);
                    } else {
                        arrayList3 = arrayList6;
                        z = true;
                    }
                    z3 = z;
                } else {
                    j = j6;
                    arrayList = arrayList5;
                    arrayList2 = newArrayList;
                    j2 = j5;
                    th = th2;
                    arrayList3 = arrayList4;
                    maybeSkipTag(xmlPullParser);
                }
                j6 = j;
                if (XmlPullParserUtil.isEndTag(xmlPullParser, "MPD")) {
                    arrayList4 = arrayList3;
                    th2 = th;
                    arrayList5 = arrayList;
                    newArrayList = arrayList2;
                    j5 = j2;
                    dashManifestParser = this;
                } else {
                    if (parseDuration == j2) {
                        if (j7 != j2) {
                            j4 = j7;
                            if (!arrayList3.isEmpty()) {
                                throw ParserException.createForMalformedManifest("No periods found.", th);
                            }
                            return buildMediaPresentationDescription(parseDateTime, j4, parseDuration2, equals, parseDuration3, parseDuration4, parseDuration5, parseDateTime2, programInformation, utcTimingElement, serviceDescriptionElement, uri2, arrayList3);
                        } else if (!equals) {
                            throw ParserException.createForMalformedManifest("Unable to determine duration of static manifest.", th);
                        }
                    }
                    j4 = parseDuration;
                    if (!arrayList3.isEmpty()) {
                    }
                }
            }
            arrayList = arrayList5;
            arrayList2 = newArrayList;
            j2 = j5;
            th = th2;
            arrayList3 = arrayList4;
            if (XmlPullParserUtil.isEndTag(xmlPullParser, "MPD")) {
            }
        }
    }

    protected DashManifest buildMediaPresentationDescription(long j, long j2, long j3, boolean z, long j4, long j5, long j6, long j7, ProgramInformation programInformation, UtcTimingElement utcTimingElement, ServiceDescriptionElement serviceDescriptionElement, Uri uri, List<Period> list) {
        return new DashManifest(j, j2, j3, z, j4, j5, j6, j7, programInformation, utcTimingElement, serviceDescriptionElement, uri, list);
    }

    protected UtcTimingElement parseUtcTiming(XmlPullParser xmlPullParser) {
        return buildUtcTimingElement(xmlPullParser.getAttributeValue(null, "schemeIdUri"), xmlPullParser.getAttributeValue(null, "value"));
    }

    protected UtcTimingElement buildUtcTimingElement(String str, String str2) {
        return new UtcTimingElement(str, str2);
    }

    protected ServiceDescriptionElement parseServiceDescription(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        long j = -9223372036854775807L;
        long j2 = -9223372036854775807L;
        long j3 = -9223372036854775807L;
        float f = -3.4028235E38f;
        float f2 = -3.4028235E38f;
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Latency")) {
                j = parseLong(xmlPullParser, "target", -9223372036854775807L);
                j2 = parseLong(xmlPullParser, "min", -9223372036854775807L);
                j3 = parseLong(xmlPullParser, "max", -9223372036854775807L);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "PlaybackRate")) {
                f = parseFloat(xmlPullParser, "min", -3.4028235E38f);
                f2 = parseFloat(xmlPullParser, "max", -3.4028235E38f);
            }
            long j4 = j;
            long j5 = j2;
            long j6 = j3;
            float f3 = f;
            float f4 = f2;
            if (XmlPullParserUtil.isEndTag(xmlPullParser, "ServiceDescription")) {
                return new ServiceDescriptionElement(j4, j5, j6, f3, f4);
            }
            j = j4;
            j2 = j5;
            j3 = j6;
            f = f3;
            f2 = f4;
        }
    }

    protected Pair<Period, Long> parsePeriod(XmlPullParser xmlPullParser, List<BaseUrl> list, long j, long j2, long j3, long j4, boolean z) throws XmlPullParserException, IOException {
        long j5;
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        Object obj;
        long j6;
        SegmentBase parseSegmentTemplate;
        DashManifestParser dashManifestParser = this;
        XmlPullParser xmlPullParser2 = xmlPullParser;
        Object obj2 = null;
        String attributeValue = xmlPullParser2.getAttributeValue(null, "id");
        long parseDuration = parseDuration(xmlPullParser2, "start", j);
        long j7 = -9223372036854775807L;
        long j8 = j3 != -9223372036854775807L ? j3 + parseDuration : -9223372036854775807L;
        long parseDuration2 = parseDuration(xmlPullParser2, "duration", -9223372036854775807L);
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        long j9 = j2;
        long j10 = -9223372036854775807L;
        SegmentBase.SingleSegmentBase singleSegmentBase = null;
        Descriptor descriptor = null;
        boolean z2 = false;
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser2, "BaseURL")) {
                if (!z2) {
                    j9 = dashManifestParser.parseAvailabilityTimeOffsetUs(xmlPullParser2, j9);
                    z2 = true;
                }
                arrayList6.addAll(dashManifestParser.parseBaseUrl(xmlPullParser2, list, z));
                arrayList3 = arrayList5;
                arrayList = arrayList6;
                j6 = j7;
                obj = obj2;
                arrayList2 = arrayList4;
            } else {
                if (XmlPullParserUtil.isStartTag(xmlPullParser2, "AdaptationSet")) {
                    j5 = j9;
                    arrayList = arrayList6;
                    arrayList2 = arrayList4;
                    arrayList2.add(parseAdaptationSet(xmlPullParser, !arrayList6.isEmpty() ? arrayList6 : list, singleSegmentBase, parseDuration2, j9, j10, j8, j4, z));
                    xmlPullParser2 = xmlPullParser;
                    arrayList3 = arrayList5;
                } else {
                    j5 = j9;
                    ArrayList arrayList7 = arrayList5;
                    arrayList = arrayList6;
                    arrayList2 = arrayList4;
                    xmlPullParser2 = xmlPullParser;
                    if (XmlPullParserUtil.isStartTag(xmlPullParser2, "EventStream")) {
                        arrayList7.add(parseEventStream(xmlPullParser));
                        arrayList3 = arrayList7;
                    } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentBase")) {
                        arrayList3 = arrayList7;
                        singleSegmentBase = parseSegmentBase(xmlPullParser2, null);
                        obj = null;
                        j9 = j5;
                        j6 = -9223372036854775807L;
                    } else {
                        arrayList3 = arrayList7;
                        if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentList")) {
                            long parseAvailabilityTimeOffsetUs = parseAvailabilityTimeOffsetUs(xmlPullParser2, -9223372036854775807L);
                            obj = null;
                            parseSegmentTemplate = parseSegmentList(xmlPullParser, null, j8, parseDuration2, j5, parseAvailabilityTimeOffsetUs, j4);
                            j10 = parseAvailabilityTimeOffsetUs;
                            j9 = j5;
                            j6 = -9223372036854775807L;
                        } else {
                            obj = null;
                            if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SegmentTemplate")) {
                                long parseAvailabilityTimeOffsetUs2 = parseAvailabilityTimeOffsetUs(xmlPullParser2, -9223372036854775807L);
                                j6 = -9223372036854775807L;
                                parseSegmentTemplate = parseSegmentTemplate(xmlPullParser, null, ImmutableList.of(), j8, parseDuration2, j5, parseAvailabilityTimeOffsetUs2, j4);
                                j10 = parseAvailabilityTimeOffsetUs2;
                                j9 = j5;
                            } else {
                                j6 = -9223372036854775807L;
                                if (XmlPullParserUtil.isStartTag(xmlPullParser2, "AssetIdentifier")) {
                                    descriptor = parseDescriptor(xmlPullParser2, "AssetIdentifier");
                                } else {
                                    maybeSkipTag(xmlPullParser);
                                }
                                j9 = j5;
                            }
                        }
                        singleSegmentBase = parseSegmentTemplate;
                    }
                }
                obj = null;
                j6 = -9223372036854775807L;
                j9 = j5;
            }
            if (XmlPullParserUtil.isEndTag(xmlPullParser2, "Period")) {
                return Pair.create(buildPeriod(attributeValue, parseDuration, arrayList2, arrayList3, descriptor), Long.valueOf(parseDuration2));
            }
            arrayList4 = arrayList2;
            arrayList6 = arrayList;
            obj2 = obj;
            arrayList5 = arrayList3;
            j7 = j6;
            dashManifestParser = this;
        }
    }

    protected Period buildPeriod(String str, long j, List<AdaptationSet> list, List<EventStream> list2, Descriptor descriptor) {
        return new Period(str, j, list, list2, descriptor);
    }

    /* JADX WARN: Removed duplicated region for block: B:71:0x030e A[LOOP:0: B:3:0x007d->B:71:0x030e, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x02cf A[EDGE_INSN: B:72:0x02cf->B:65:0x02cf ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected AdaptationSet parseAdaptationSet(XmlPullParser xmlPullParser, List<BaseUrl> list, SegmentBase segmentBase, long j, long j2, long j3, long j4, long j5, boolean z) throws XmlPullParserException, IOException {
        long j6;
        ArrayList<Descriptor> arrayList;
        Object obj;
        long j7;
        ArrayList arrayList2;
        ArrayList arrayList3;
        ArrayList arrayList4;
        ArrayList arrayList5;
        ArrayList arrayList6;
        ArrayList<DrmInitData.SchemeData> arrayList7;
        String str;
        String str2;
        ArrayList arrayList8;
        int i;
        ArrayList<Descriptor> arrayList9;
        long parseAvailabilityTimeOffsetUs;
        DashManifestParser dashManifestParser = this;
        XmlPullParser xmlPullParser2 = xmlPullParser;
        int parseInt = parseInt(xmlPullParser2, "id", -1);
        int parseContentType = parseContentType(xmlPullParser);
        String attributeValue = xmlPullParser2.getAttributeValue(null, "mimeType");
        String attributeValue2 = xmlPullParser2.getAttributeValue(null, "codecs");
        int parseInt2 = parseInt(xmlPullParser2, "width", -1);
        int parseInt3 = parseInt(xmlPullParser2, "height", -1);
        float parseFrameRate = parseFrameRate(xmlPullParser2, -1.0f);
        int parseInt4 = parseInt(xmlPullParser2, "audioSamplingRate", -1);
        String str3 = "lang";
        String attributeValue3 = xmlPullParser2.getAttributeValue(null, "lang");
        String attributeValue4 = xmlPullParser2.getAttributeValue(null, "label");
        ArrayList<DrmInitData.SchemeData> arrayList10 = new ArrayList<>();
        ArrayList<Descriptor> arrayList11 = new ArrayList<>();
        ArrayList arrayList12 = new ArrayList();
        ArrayList arrayList13 = new ArrayList();
        ArrayList arrayList14 = new ArrayList();
        ArrayList arrayList15 = new ArrayList();
        ArrayList arrayList16 = new ArrayList();
        ArrayList arrayList17 = new ArrayList();
        SegmentBase.SingleSegmentBase singleSegmentBase = segmentBase;
        int i2 = parseContentType;
        String str4 = attributeValue3;
        String str5 = attributeValue4;
        String str6 = null;
        int i3 = -1;
        boolean z2 = false;
        long j8 = j2;
        long j9 = j3;
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser2, "BaseURL")) {
                if (!z2) {
                    j8 = dashManifestParser.parseAvailabilityTimeOffsetUs(xmlPullParser2, j8);
                    z2 = true;
                }
                j6 = j9;
                arrayList = arrayList11;
                arrayList17.addAll(dashManifestParser.parseBaseUrl(xmlPullParser2, list, z));
            } else {
                j6 = j9;
                arrayList = arrayList11;
                if (XmlPullParserUtil.isStartTag(xmlPullParser2, "ContentProtection")) {
                    Pair<String, DrmInitData.SchemeData> parseContentProtection = parseContentProtection(xmlPullParser);
                    Object obj2 = parseContentProtection.first;
                    if (obj2 != null) {
                        str6 = (String) obj2;
                    }
                    Object obj3 = parseContentProtection.second;
                    if (obj3 != null) {
                        arrayList10.add((DrmInitData.SchemeData) obj3);
                    }
                } else {
                    if (XmlPullParserUtil.isStartTag(xmlPullParser2, "ContentComponent")) {
                        str2 = checkLanguageConsistency(str4, xmlPullParser2.getAttributeValue(null, str3));
                        obj = null;
                        j7 = j8;
                        arrayList2 = arrayList17;
                        arrayList8 = arrayList16;
                        arrayList3 = arrayList15;
                        arrayList4 = arrayList14;
                        arrayList5 = arrayList13;
                        arrayList6 = arrayList12;
                        arrayList7 = arrayList10;
                        str = str3;
                        i = checkContentTypeConsistency(i2, parseContentType(xmlPullParser));
                        arrayList9 = arrayList;
                    } else {
                        int i4 = i2;
                        String str7 = str4;
                        if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Role")) {
                            arrayList13.add(parseDescriptor(xmlPullParser2, "Role"));
                        } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "AudioChannelConfiguration")) {
                            i3 = parseAudioChannelConfiguration(xmlPullParser);
                        } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Accessibility")) {
                            arrayList12.add(parseDescriptor(xmlPullParser2, "Accessibility"));
                        } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "EssentialProperty")) {
                            arrayList14.add(parseDescriptor(xmlPullParser2, "EssentialProperty"));
                        } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "SupplementalProperty")) {
                            arrayList15.add(parseDescriptor(xmlPullParser2, "SupplementalProperty"));
                        } else if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Representation")) {
                            j7 = j8;
                            arrayList2 = arrayList17;
                            arrayList3 = arrayList15;
                            arrayList4 = arrayList14;
                            arrayList5 = arrayList13;
                            arrayList6 = arrayList12;
                            arrayList7 = arrayList10;
                            str = str3;
                            obj = null;
                            str2 = str7;
                            RepresentationInfo parseRepresentation = parseRepresentation(xmlPullParser, !arrayList17.isEmpty() ? arrayList17 : list, attributeValue, attributeValue2, parseInt2, parseInt3, parseFrameRate, i3, parseInt4, str7, arrayList5, arrayList6, arrayList4, arrayList3, singleSegmentBase, j4, j, j7, j6, j5, z);
                            int checkContentTypeConsistency = checkContentTypeConsistency(i4, MimeTypes.getTrackType(parseRepresentation.format.sampleMimeType));
                            arrayList8 = arrayList16;
                            arrayList8.add(parseRepresentation);
                            xmlPullParser2 = xmlPullParser;
                            i = checkContentTypeConsistency;
                            arrayList9 = arrayList;
                        } else {
                            obj = null;
                            j7 = j8;
                            arrayList2 = arrayList17;
                            arrayList3 = arrayList15;
                            arrayList4 = arrayList14;
                            arrayList5 = arrayList13;
                            arrayList6 = arrayList12;
                            arrayList7 = arrayList10;
                            str = str3;
                            str2 = str7;
                            arrayList8 = arrayList16;
                            if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentBase")) {
                                singleSegmentBase = parseSegmentBase(xmlPullParser, (SegmentBase.SingleSegmentBase) singleSegmentBase);
                                i = i4;
                                arrayList9 = arrayList;
                                j9 = j6;
                                xmlPullParser2 = xmlPullParser;
                            } else {
                                if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentList")) {
                                    parseAvailabilityTimeOffsetUs = parseAvailabilityTimeOffsetUs(xmlPullParser, j6);
                                    i = i4;
                                    singleSegmentBase = parseSegmentList(xmlPullParser, (SegmentBase.SegmentList) singleSegmentBase, j4, j, j7, parseAvailabilityTimeOffsetUs, j5);
                                    xmlPullParser2 = xmlPullParser;
                                } else {
                                    j9 = j6;
                                    i = i4;
                                    if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentTemplate")) {
                                        parseAvailabilityTimeOffsetUs = parseAvailabilityTimeOffsetUs(xmlPullParser, j9);
                                        xmlPullParser2 = xmlPullParser;
                                        singleSegmentBase = parseSegmentTemplate(xmlPullParser, (SegmentBase.SegmentTemplate) singleSegmentBase, arrayList3, j4, j, j7, parseAvailabilityTimeOffsetUs, j5);
                                    } else {
                                        xmlPullParser2 = xmlPullParser;
                                        if (XmlPullParserUtil.isStartTag(xmlPullParser2, "InbandEventStream")) {
                                            arrayList9 = arrayList;
                                            arrayList9.add(parseDescriptor(xmlPullParser2, "InbandEventStream"));
                                        } else {
                                            arrayList9 = arrayList;
                                            if (XmlPullParserUtil.isStartTag(xmlPullParser2, "Label")) {
                                                str5 = parseLabel(xmlPullParser);
                                            } else if (XmlPullParserUtil.isStartTag(xmlPullParser)) {
                                                parseAdaptationSetChild(xmlPullParser);
                                            }
                                        }
                                    }
                                }
                                j9 = parseAvailabilityTimeOffsetUs;
                                arrayList9 = arrayList;
                            }
                            if (!XmlPullParserUtil.isEndTag(xmlPullParser2, "AdaptationSet")) {
                                break;
                            }
                            arrayList11 = arrayList9;
                            arrayList16 = arrayList8;
                            arrayList17 = arrayList2;
                            arrayList15 = arrayList3;
                            j8 = j7;
                            arrayList14 = arrayList4;
                            arrayList13 = arrayList5;
                            arrayList12 = arrayList6;
                            arrayList10 = arrayList7;
                            str3 = str;
                            i2 = i;
                            str4 = str2;
                            dashManifestParser = this;
                        }
                        obj = null;
                        j7 = j8;
                        arrayList2 = arrayList17;
                        arrayList3 = arrayList15;
                        arrayList4 = arrayList14;
                        arrayList5 = arrayList13;
                        arrayList6 = arrayList12;
                        arrayList7 = arrayList10;
                        str = str3;
                        i = i4;
                        str2 = str7;
                        arrayList9 = arrayList;
                        j9 = j6;
                        arrayList8 = arrayList16;
                        if (!XmlPullParserUtil.isEndTag(xmlPullParser2, "AdaptationSet")) {
                        }
                    }
                    j9 = j6;
                    if (!XmlPullParserUtil.isEndTag(xmlPullParser2, "AdaptationSet")) {
                    }
                }
            }
            j9 = j6;
            j7 = j8;
            arrayList8 = arrayList16;
            arrayList4 = arrayList14;
            arrayList5 = arrayList13;
            arrayList6 = arrayList12;
            arrayList7 = arrayList10;
            str = str3;
            arrayList9 = arrayList;
            i = i2;
            str2 = str4;
            obj = null;
            arrayList2 = arrayList17;
            arrayList3 = arrayList15;
            if (!XmlPullParserUtil.isEndTag(xmlPullParser2, "AdaptationSet")) {
            }
        }
        ArrayList arrayList18 = new ArrayList(arrayList8.size());
        for (int i5 = 0; i5 < arrayList8.size(); i5++) {
            arrayList18.add(buildRepresentation((RepresentationInfo) arrayList8.get(i5), str5, str6, arrayList7, arrayList9));
        }
        return buildAdaptationSet(parseInt, i, arrayList18, arrayList6, arrayList4, arrayList3);
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
        if ("text".equals(attributeValue)) {
            return 3;
        }
        return "image".equals(attributeValue) ? 4 : -1;
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x00b9  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x011e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected Pair<String, DrmInitData.SchemeData> parseContentProtection(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        String str;
        UUID uuid;
        UUID uuid2;
        byte[] bArr;
        byte[] bArr2;
        String attributeValue = xmlPullParser.getAttributeValue(null, "schemeIdUri");
        if (attributeValue != null) {
            String lowerCase = Ascii.toLowerCase(attributeValue);
            lowerCase.hashCode();
            char c = 65535;
            switch (lowerCase.hashCode()) {
                case -1980789791:
                    if (lowerCase.equals("urn:uuid:e2719d58-a985-b3c9-781a-b030af78d30e")) {
                        c = 0;
                        break;
                    }
                    break;
                case 489446379:
                    if (lowerCase.equals("urn:uuid:9a04f079-9840-4286-ab92-e65be0885f95")) {
                        c = 1;
                        break;
                    }
                    break;
                case 755418770:
                    if (lowerCase.equals("urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed")) {
                        c = 2;
                        break;
                    }
                    break;
                case 1812765994:
                    if (lowerCase.equals("urn:mpeg:dash:mp4protection:2011")) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    uuid = C.CLEARKEY_UUID;
                    str = null;
                    uuid2 = null;
                    bArr = uuid2;
                    bArr2 = uuid2;
                    break;
                case 1:
                    uuid = C.PLAYREADY_UUID;
                    str = null;
                    uuid2 = null;
                    bArr = uuid2;
                    bArr2 = uuid2;
                    break;
                case 2:
                    uuid = C.WIDEVINE_UUID;
                    str = null;
                    uuid2 = null;
                    bArr = uuid2;
                    bArr2 = uuid2;
                    break;
                case 3:
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
                if (!XmlPullParserUtil.isStartTag(xmlPullParser, "clearkey:Laurl") && xmlPullParser.next() == 4) {
                    bArr = xmlPullParser.getText();
                    bArr2 = bArr2;
                } else if (!XmlPullParserUtil.isStartTag(xmlPullParser, "ms:laurl")) {
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
            return Pair.create(str, uuid != null ? new DrmInitData.SchemeData(uuid, bArr, "video/mp4", bArr2) : null);
        }
        str = null;
        uuid = null;
        uuid2 = uuid;
        bArr = uuid2;
        bArr2 = uuid2;
        do {
            xmlPullParser.next();
            if (!XmlPullParserUtil.isStartTag(xmlPullParser, "clearkey:Laurl")) {
            }
            if (!XmlPullParserUtil.isStartTag(xmlPullParser, "ms:laurl")) {
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "ContentProtection"));
        return Pair.create(str, uuid != null ? new DrmInitData.SchemeData(uuid, bArr, "video/mp4", bArr2) : null);
    }

    protected void parseAdaptationSetChild(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        maybeSkipTag(xmlPullParser);
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x01ef A[LOOP:0: B:3:0x006b->B:57:0x01ef, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0199 A[EDGE_INSN: B:58:0x0199->B:47:0x0199 ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected RepresentationInfo parseRepresentation(XmlPullParser xmlPullParser, List<BaseUrl> list, String str, String str2, int i, int i2, float f, int i3, int i4, String str3, List<Descriptor> list2, List<Descriptor> list3, List<Descriptor> list4, List<Descriptor> list5, SegmentBase segmentBase, long j, long j2, long j3, long j4, long j5, boolean z) throws XmlPullParserException, IOException {
        long j6;
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        ArrayList arrayList4;
        ArrayList arrayList5;
        ArrayList arrayList6;
        ArrayList arrayList7;
        int i5;
        long parseAvailabilityTimeOffsetUs;
        SegmentBase segmentBase2;
        DashManifestParser dashManifestParser = this;
        String attributeValue = xmlPullParser.getAttributeValue(null, "id");
        int parseInt = parseInt(xmlPullParser, "bandwidth", -1);
        String parseString = parseString(xmlPullParser, "mimeType", str);
        String parseString2 = parseString(xmlPullParser, "codecs", str2);
        int parseInt2 = parseInt(xmlPullParser, "width", i);
        int parseInt3 = parseInt(xmlPullParser, "height", i2);
        float parseFrameRate = parseFrameRate(xmlPullParser, f);
        int parseInt4 = parseInt(xmlPullParser, "audioSamplingRate", i4);
        ArrayList arrayList8 = new ArrayList();
        ArrayList arrayList9 = new ArrayList();
        ArrayList arrayList10 = new ArrayList(list4);
        ArrayList arrayList11 = new ArrayList(list5);
        ArrayList arrayList12 = new ArrayList();
        int i6 = i3;
        long j7 = j3;
        long j8 = j4;
        String str4 = null;
        boolean z2 = false;
        SegmentBase segmentBase3 = segmentBase;
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "BaseURL")) {
                if (!z2) {
                    j7 = dashManifestParser.parseAvailabilityTimeOffsetUs(xmlPullParser, j7);
                    z2 = true;
                }
                arrayList12.addAll(dashManifestParser.parseBaseUrl(xmlPullParser, list, z));
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "AudioChannelConfiguration")) {
                segmentBase2 = segmentBase3;
                arrayList = arrayList12;
                arrayList5 = arrayList8;
                i5 = parseAudioChannelConfiguration(xmlPullParser);
                arrayList6 = arrayList9;
                arrayList7 = arrayList11;
                if (XmlPullParserUtil.isEndTag(xmlPullParser, "Representation")) {
                    break;
                }
                arrayList11 = arrayList7;
                arrayList9 = arrayList6;
                arrayList8 = arrayList5;
                segmentBase3 = segmentBase2;
                dashManifestParser = this;
                i6 = i5;
                arrayList12 = arrayList;
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentBase")) {
                segmentBase3 = dashManifestParser.parseSegmentBase(xmlPullParser, (SegmentBase.SingleSegmentBase) segmentBase3);
            } else {
                if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentList")) {
                    parseAvailabilityTimeOffsetUs = dashManifestParser.parseAvailabilityTimeOffsetUs(xmlPullParser, j8);
                    j6 = j7;
                    arrayList = arrayList12;
                    arrayList2 = arrayList11;
                    arrayList3 = arrayList9;
                    arrayList4 = arrayList10;
                    segmentBase3 = parseSegmentList(xmlPullParser, (SegmentBase.SegmentList) segmentBase3, j, j2, j6, parseAvailabilityTimeOffsetUs, j5);
                    arrayList5 = arrayList8;
                } else {
                    j6 = j7;
                    arrayList = arrayList12;
                    arrayList2 = arrayList11;
                    arrayList3 = arrayList9;
                    arrayList4 = arrayList10;
                    if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentTemplate")) {
                        parseAvailabilityTimeOffsetUs = dashManifestParser.parseAvailabilityTimeOffsetUs(xmlPullParser, j8);
                        arrayList5 = arrayList8;
                        segmentBase3 = parseSegmentTemplate(xmlPullParser, (SegmentBase.SegmentTemplate) segmentBase3, list5, j, j2, j6, parseAvailabilityTimeOffsetUs, j5);
                    } else {
                        arrayList5 = arrayList8;
                        if (XmlPullParserUtil.isStartTag(xmlPullParser, "ContentProtection")) {
                            Pair<String, DrmInitData.SchemeData> parseContentProtection = parseContentProtection(xmlPullParser);
                            Object obj = parseContentProtection.first;
                            if (obj != null) {
                                str4 = (String) obj;
                            }
                            Object obj2 = parseContentProtection.second;
                            if (obj2 != null) {
                                arrayList5.add((DrmInitData.SchemeData) obj2);
                            }
                            i5 = i6;
                            j7 = j6;
                            arrayList7 = arrayList2;
                            arrayList6 = arrayList3;
                            arrayList10 = arrayList4;
                            segmentBase2 = segmentBase3;
                            if (XmlPullParserUtil.isEndTag(xmlPullParser, "Representation")) {
                            }
                        } else {
                            if (XmlPullParserUtil.isStartTag(xmlPullParser, "InbandEventStream")) {
                                arrayList6 = arrayList3;
                                arrayList6.add(parseDescriptor(xmlPullParser, "InbandEventStream"));
                                arrayList7 = arrayList2;
                                arrayList10 = arrayList4;
                            } else {
                                arrayList6 = arrayList3;
                                if (XmlPullParserUtil.isStartTag(xmlPullParser, "EssentialProperty")) {
                                    arrayList10 = arrayList4;
                                    arrayList10.add(parseDescriptor(xmlPullParser, "EssentialProperty"));
                                    arrayList7 = arrayList2;
                                } else {
                                    arrayList10 = arrayList4;
                                    if (XmlPullParserUtil.isStartTag(xmlPullParser, "SupplementalProperty")) {
                                        arrayList7 = arrayList2;
                                        arrayList7.add(parseDescriptor(xmlPullParser, "SupplementalProperty"));
                                    } else {
                                        arrayList7 = arrayList2;
                                        maybeSkipTag(xmlPullParser);
                                    }
                                }
                            }
                            i5 = i6;
                            j7 = j6;
                            segmentBase2 = segmentBase3;
                            if (XmlPullParserUtil.isEndTag(xmlPullParser, "Representation")) {
                            }
                        }
                    }
                }
                i5 = i6;
                j8 = parseAvailabilityTimeOffsetUs;
                j7 = j6;
                arrayList7 = arrayList2;
                arrayList6 = arrayList3;
                arrayList10 = arrayList4;
                segmentBase2 = segmentBase3;
                if (XmlPullParserUtil.isEndTag(xmlPullParser, "Representation")) {
                }
            }
            arrayList = arrayList12;
            arrayList5 = arrayList8;
            i5 = i6;
            segmentBase2 = segmentBase3;
            arrayList6 = arrayList9;
            arrayList7 = arrayList11;
            if (XmlPullParserUtil.isEndTag(xmlPullParser, "Representation")) {
            }
        }
        ArrayList arrayList13 = arrayList7;
        ArrayList arrayList14 = arrayList10;
        ArrayList arrayList15 = arrayList6;
        Format buildFormat = buildFormat(attributeValue, parseString, parseInt2, parseInt3, parseFrameRate, i5, parseInt4, parseInt, str3, list2, list3, parseString2, arrayList14, arrayList13);
        SegmentBase.SingleSegmentBase singleSegmentBase = segmentBase2;
        if (segmentBase2 == null) {
            singleSegmentBase = new SegmentBase.SingleSegmentBase();
        }
        boolean isEmpty = arrayList.isEmpty();
        ArrayList arrayList16 = arrayList;
        if (isEmpty) {
            arrayList16 = list;
        }
        return new RepresentationInfo(buildFormat, arrayList16, singleSegmentBase, str4, arrayList5, arrayList15, arrayList14, arrayList13, -1L);
    }

    protected Format buildFormat(String str, String str2, int i, int i2, float f, int i3, int i4, int i5, String str3, List<Descriptor> list, List<Descriptor> list2, String str4, List<Descriptor> list3, List<Descriptor> list4) {
        String str5 = str4;
        String sampleMimeType = getSampleMimeType(str2, str5);
        if ("audio/eac3".equals(sampleMimeType)) {
            sampleMimeType = parseEac3SupplementalProperties(list4);
            if ("audio/eac3-joc".equals(sampleMimeType)) {
                str5 = "ec+3";
            }
        }
        int parseSelectionFlagsFromRoleDescriptors = parseSelectionFlagsFromRoleDescriptors(list);
        int parseRoleFlagsFromRoleDescriptors = parseRoleFlagsFromRoleDescriptors(list) | parseRoleFlagsFromAccessibilityDescriptors(list2) | parseRoleFlagsFromProperties(list3) | parseRoleFlagsFromProperties(list4);
        Pair<Integer, Integer> parseTileCountFromProperties = parseTileCountFromProperties(list3);
        Format.Builder language = new Format.Builder().setId(str).setContainerMimeType(str2).setSampleMimeType(sampleMimeType).setCodecs(str5).setPeakBitrate(i5).setSelectionFlags(parseSelectionFlagsFromRoleDescriptors).setRoleFlags(parseRoleFlagsFromRoleDescriptors).setLanguage(str3);
        int i6 = -1;
        Format.Builder tileCountVertical = language.setTileCountHorizontal(parseTileCountFromProperties != null ? ((Integer) parseTileCountFromProperties.first).intValue() : -1).setTileCountVertical(parseTileCountFromProperties != null ? ((Integer) parseTileCountFromProperties.second).intValue() : -1);
        if (MimeTypes.isVideo(sampleMimeType)) {
            tileCountVertical.setWidth(i).setHeight(i2).setFrameRate(f);
        } else if (MimeTypes.isAudio(sampleMimeType)) {
            tileCountVertical.setChannelCount(i3).setSampleRate(i4);
        } else if (MimeTypes.isText(sampleMimeType)) {
            if ("application/cea-608".equals(sampleMimeType)) {
                i6 = parseCea608AccessibilityChannel(list2);
            } else if ("application/cea-708".equals(sampleMimeType)) {
                i6 = parseCea708AccessibilityChannel(list2);
            }
            tileCountVertical.setAccessibilityChannel(i6);
        } else if (MimeTypes.isImage(sampleMimeType)) {
            tileCountVertical.setWidth(i).setHeight(i2);
        }
        return tileCountVertical.build();
    }

    protected Representation buildRepresentation(RepresentationInfo representationInfo, String str, String str2, ArrayList<DrmInitData.SchemeData> arrayList, ArrayList<Descriptor> arrayList2) {
        Format.Builder buildUpon = representationInfo.format.buildUpon();
        if (str != null) {
            buildUpon.setLabel(str);
        }
        String str3 = representationInfo.drmSchemeType;
        if (str3 != null) {
            str2 = str3;
        }
        ArrayList<DrmInitData.SchemeData> arrayList3 = representationInfo.drmSchemeDatas;
        arrayList3.addAll(arrayList);
        if (!arrayList3.isEmpty()) {
            fillInClearKeyInformation(arrayList3);
            filterRedundantIncompleteSchemeDatas(arrayList3);
            buildUpon.setDrmInitData(new DrmInitData(str2, arrayList3));
        }
        ArrayList<Descriptor> arrayList4 = representationInfo.inbandEventStreams;
        arrayList4.addAll(arrayList2);
        return Representation.newInstance(representationInfo.revisionId, buildUpon.build(), representationInfo.baseUrls, representationInfo.segmentBase, arrayList4, representationInfo.essentialProperties, representationInfo.supplementalProperties, null);
    }

    protected SegmentBase.SingleSegmentBase parseSegmentBase(XmlPullParser xmlPullParser, SegmentBase.SingleSegmentBase singleSegmentBase) throws XmlPullParserException, IOException {
        long j;
        long j2;
        long parseLong = parseLong(xmlPullParser, "timescale", singleSegmentBase != null ? singleSegmentBase.timescale : 1L);
        long parseLong2 = parseLong(xmlPullParser, "presentationTimeOffset", singleSegmentBase != null ? singleSegmentBase.presentationTimeOffset : 0L);
        long j3 = singleSegmentBase != null ? singleSegmentBase.indexStart : 0L;
        long j4 = singleSegmentBase != null ? singleSegmentBase.indexLength : 0L;
        String attributeValue = xmlPullParser.getAttributeValue(null, "indexRange");
        if (attributeValue != null) {
            String[] split = attributeValue.split("-");
            long parseLong3 = Long.parseLong(split[0]);
            j = (Long.parseLong(split[1]) - parseLong3) + 1;
            j2 = parseLong3;
        } else {
            j = j4;
            j2 = j3;
        }
        RangedUri rangedUri = singleSegmentBase != null ? singleSegmentBase.initialization : null;
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

    protected SegmentBase.SegmentList parseSegmentList(XmlPullParser xmlPullParser, SegmentBase.SegmentList segmentList, long j, long j2, long j3, long j4, long j5) throws XmlPullParserException, IOException {
        long parseLong = parseLong(xmlPullParser, "timescale", segmentList != null ? segmentList.timescale : 1L);
        long parseLong2 = parseLong(xmlPullParser, "presentationTimeOffset", segmentList != null ? segmentList.presentationTimeOffset : 0L);
        long parseLong3 = parseLong(xmlPullParser, "duration", segmentList != null ? segmentList.duration : -9223372036854775807L);
        long parseLong4 = parseLong(xmlPullParser, "startNumber", segmentList != null ? segmentList.startNumber : 1L);
        long finalAvailabilityTimeOffset = getFinalAvailabilityTimeOffset(j3, j4);
        List<SegmentBase.SegmentTimelineElement> list = null;
        List<RangedUri> list2 = null;
        RangedUri rangedUri = null;
        do {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Initialization")) {
                rangedUri = parseInitialization(xmlPullParser);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentTimeline")) {
                list = parseSegmentTimeline(xmlPullParser, parseLong, j2);
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
        return buildSegmentList(rangedUri, parseLong, parseLong2, parseLong4, parseLong3, list, finalAvailabilityTimeOffset, list2, j5, j);
    }

    protected SegmentBase.SegmentList buildSegmentList(RangedUri rangedUri, long j, long j2, long j3, long j4, List<SegmentBase.SegmentTimelineElement> list, long j5, List<RangedUri> list2, long j6, long j7) {
        return new SegmentBase.SegmentList(rangedUri, j, j2, j3, j4, list, j5, list2, Util.msToUs(j6), Util.msToUs(j7));
    }

    protected SegmentBase.SegmentTemplate parseSegmentTemplate(XmlPullParser xmlPullParser, SegmentBase.SegmentTemplate segmentTemplate, List<Descriptor> list, long j, long j2, long j3, long j4, long j5) throws XmlPullParserException, IOException {
        long parseLong = parseLong(xmlPullParser, "timescale", segmentTemplate != null ? segmentTemplate.timescale : 1L);
        long parseLong2 = parseLong(xmlPullParser, "presentationTimeOffset", segmentTemplate != null ? segmentTemplate.presentationTimeOffset : 0L);
        long parseLong3 = parseLong(xmlPullParser, "duration", segmentTemplate != null ? segmentTemplate.duration : -9223372036854775807L);
        long parseLong4 = parseLong(xmlPullParser, "startNumber", segmentTemplate != null ? segmentTemplate.startNumber : 1L);
        long parseLastSegmentNumberSupplementalProperty = parseLastSegmentNumberSupplementalProperty(list);
        long finalAvailabilityTimeOffset = getFinalAvailabilityTimeOffset(j3, j4);
        List<SegmentBase.SegmentTimelineElement> list2 = null;
        UrlTemplate parseUrlTemplate = parseUrlTemplate(xmlPullParser, "media", segmentTemplate != null ? segmentTemplate.mediaTemplate : null);
        UrlTemplate parseUrlTemplate2 = parseUrlTemplate(xmlPullParser, "initialization", segmentTemplate != null ? segmentTemplate.initializationTemplate : null);
        RangedUri rangedUri = null;
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Initialization")) {
                rangedUri = parseInitialization(xmlPullParser);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentTimeline")) {
                list2 = parseSegmentTimeline(xmlPullParser, parseLong, j2);
            } else {
                maybeSkipTag(xmlPullParser);
            }
            if (XmlPullParserUtil.isEndTag(xmlPullParser, "SegmentTemplate")) {
                break;
            }
        }
        if (segmentTemplate != null) {
            if (rangedUri == null) {
                rangedUri = segmentTemplate.initialization;
            }
            if (list2 == null) {
                list2 = segmentTemplate.segmentTimeline;
            }
        }
        return buildSegmentTemplate(rangedUri, parseLong, parseLong2, parseLong4, parseLastSegmentNumberSupplementalProperty, parseLong3, list2, finalAvailabilityTimeOffset, parseUrlTemplate2, parseUrlTemplate, j5, j);
    }

    protected SegmentBase.SegmentTemplate buildSegmentTemplate(RangedUri rangedUri, long j, long j2, long j3, long j4, long j5, List<SegmentBase.SegmentTimelineElement> list, long j6, UrlTemplate urlTemplate, UrlTemplate urlTemplate2, long j7, long j8) {
        return new SegmentBase.SegmentTemplate(rangedUri, j, j2, j3, j4, j5, list, j6, urlTemplate, urlTemplate2, Util.msToUs(j7), Util.msToUs(j8));
    }

    protected EventStream parseEventStream(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        ByteArrayOutputStream byteArrayOutputStream;
        long j;
        ArrayList arrayList;
        String parseString = parseString(xmlPullParser, "schemeIdUri", "");
        String parseString2 = parseString(xmlPullParser, "value", "");
        long parseLong = parseLong(xmlPullParser, "timescale", 1L);
        long parseLong2 = parseLong(xmlPullParser, "presentationTimeOffset", 0L);
        ArrayList arrayList2 = new ArrayList();
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream(LiteMode.FLAG_CALLS_ANIMATIONS);
        while (true) {
            xmlPullParser.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Event")) {
                byteArrayOutputStream = byteArrayOutputStream2;
                long j2 = parseLong2;
                j = parseLong2;
                arrayList = arrayList2;
                arrayList.add(parseEvent(xmlPullParser, parseString, parseString2, parseLong, j2, byteArrayOutputStream));
            } else {
                byteArrayOutputStream = byteArrayOutputStream2;
                j = parseLong2;
                arrayList = arrayList2;
                maybeSkipTag(xmlPullParser);
            }
            if (XmlPullParserUtil.isEndTag(xmlPullParser, "EventStream")) {
                break;
            }
            arrayList2 = arrayList;
            byteArrayOutputStream2 = byteArrayOutputStream;
            parseLong2 = j;
        }
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

    protected Pair<Long, EventMessage> parseEvent(XmlPullParser xmlPullParser, String str, String str2, long j, long j2, ByteArrayOutputStream byteArrayOutputStream) throws IOException, XmlPullParserException {
        long parseLong = parseLong(xmlPullParser, "id", 0L);
        long parseLong2 = parseLong(xmlPullParser, "duration", -9223372036854775807L);
        long parseLong3 = parseLong(xmlPullParser, "presentationTime", 0L);
        long scaleLargeTimestamp = Util.scaleLargeTimestamp(parseLong2, 1000L, j);
        long scaleLargeTimestamp2 = Util.scaleLargeTimestamp(parseLong3 - j2, 1000000L, j);
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
        newSerializer.setOutput(byteArrayOutputStream, Charsets.UTF_8.name());
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

    protected List<BaseUrl> parseBaseUrl(XmlPullParser xmlPullParser, List<BaseUrl> list, boolean z) throws XmlPullParserException, IOException {
        int i;
        String attributeValue = xmlPullParser.getAttributeValue(null, "dvb:priority");
        if (attributeValue != null) {
            i = Integer.parseInt(attributeValue);
        } else {
            i = z ? 1 : Integer.MIN_VALUE;
        }
        String attributeValue2 = xmlPullParser.getAttributeValue(null, "dvb:weight");
        int parseInt = attributeValue2 != null ? Integer.parseInt(attributeValue2) : 1;
        String attributeValue3 = xmlPullParser.getAttributeValue(null, "serviceLocation");
        String parseText = parseText(xmlPullParser, "BaseURL");
        if (UriUtil.isAbsolute(parseText)) {
            if (attributeValue3 == null) {
                attributeValue3 = parseText;
            }
            return Lists.newArrayList(new BaseUrl(parseText, attributeValue3, i, parseInt));
        }
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < list.size(); i2++) {
            BaseUrl baseUrl = list.get(i2);
            String resolve = UriUtil.resolve(baseUrl.url, parseText);
            String str = attributeValue3 == null ? resolve : attributeValue3;
            if (z) {
                i = baseUrl.priority;
                parseInt = baseUrl.weight;
                str = baseUrl.serviceLocation;
            }
            arrayList.add(new BaseUrl(resolve, str, i, parseInt));
        }
        return arrayList;
    }

    protected long parseAvailabilityTimeOffsetUs(XmlPullParser xmlPullParser, long j) {
        String attributeValue = xmlPullParser.getAttributeValue(null, "availabilityTimeOffset");
        if (attributeValue == null) {
            return j;
        }
        if ("INF".equals(attributeValue)) {
            return Long.MAX_VALUE;
        }
        return Float.parseFloat(attributeValue) * 1000000.0f;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    protected int parseAudioChannelConfiguration(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        char c;
        String parseString = parseString(xmlPullParser, "schemeIdUri", null);
        parseString.hashCode();
        int i = -1;
        switch (parseString.hashCode()) {
            case -2128649360:
                if (parseString.equals("urn:dts:dash:audio_channel_configuration:2012")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1352850286:
                if (parseString.equals("urn:mpeg:dash:23003:3:audio_channel_configuration:2011")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -1138141449:
                if (parseString.equals("tag:dolby.com,2014:dash:audio_channel_configuration:2011")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -986633423:
                if (parseString.equals("urn:mpeg:mpegB:cicp:ChannelConfiguration")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -79006963:
                if (parseString.equals("tag:dts.com,2014:dash:audio_channel_configuration:2012")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 312179081:
                if (parseString.equals("tag:dts.com,2018:uhd:audio_channel_configuration")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 2036691300:
                if (parseString.equals("urn:dolby:dash:audio_channel_configuration:2011")) {
                    c = 6;
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
            case 4:
                i = parseDtsChannelConfiguration(xmlPullParser);
                break;
            case 1:
                i = parseInt(xmlPullParser, "value", -1);
                break;
            case 2:
            case 6:
                i = parseDolbyChannelConfiguration(xmlPullParser);
                break;
            case 3:
                i = parseMpegChannelConfiguration(xmlPullParser);
                break;
            case 5:
                i = parseDtsxChannelConfiguration(xmlPullParser);
                break;
        }
        do {
            xmlPullParser.next();
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "AudioChannelConfiguration"));
        return i;
    }

    protected int parseSelectionFlagsFromRoleDescriptors(List<Descriptor> list) {
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            Descriptor descriptor = list.get(i2);
            if (Ascii.equalsIgnoreCase("urn:mpeg:dash:role:2011", descriptor.schemeIdUri)) {
                i |= parseSelectionFlagsFromDashRoleScheme(descriptor.value);
            }
        }
        return i;
    }

    protected int parseSelectionFlagsFromDashRoleScheme(String str) {
        if (str == null) {
            return 0;
        }
        return (str.equals("forced_subtitle") || str.equals("forced-subtitle")) ? 2 : 0;
    }

    protected int parseRoleFlagsFromRoleDescriptors(List<Descriptor> list) {
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            Descriptor descriptor = list.get(i2);
            if (Ascii.equalsIgnoreCase("urn:mpeg:dash:role:2011", descriptor.schemeIdUri)) {
                i |= parseRoleFlagsFromDashRoleScheme(descriptor.value);
            }
        }
        return i;
    }

    protected int parseRoleFlagsFromAccessibilityDescriptors(List<Descriptor> list) {
        int parseTvaAudioPurposeCsValue;
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            Descriptor descriptor = list.get(i2);
            if (Ascii.equalsIgnoreCase("urn:mpeg:dash:role:2011", descriptor.schemeIdUri)) {
                parseTvaAudioPurposeCsValue = parseRoleFlagsFromDashRoleScheme(descriptor.value);
            } else if (Ascii.equalsIgnoreCase("urn:tva:metadata:cs:AudioPurposeCS:2007", descriptor.schemeIdUri)) {
                parseTvaAudioPurposeCsValue = parseTvaAudioPurposeCsValue(descriptor.value);
            }
            i |= parseTvaAudioPurposeCsValue;
        }
        return i;
    }

    protected int parseRoleFlagsFromProperties(List<Descriptor> list) {
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (Ascii.equalsIgnoreCase("http://dashif.org/guidelines/trickmode", list.get(i2).schemeIdUri)) {
                i |= LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM;
            }
        }
        return i;
    }

    protected int parseRoleFlagsFromDashRoleScheme(String str) {
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
            case -1574842690:
                if (str.equals("forced_subtitle")) {
                    c = 3;
                    break;
                }
                break;
            case -1408024454:
                if (str.equals("alternate")) {
                    c = 4;
                    break;
                }
                break;
            case -1396432756:
                if (str.equals("forced-subtitle")) {
                    c = 5;
                    break;
                }
                break;
            case 99825:
                if (str.equals("dub")) {
                    c = 6;
                    break;
                }
                break;
            case 3343801:
                if (str.equals("main")) {
                    c = 7;
                    break;
                }
                break;
            case 3530173:
                if (str.equals("sign")) {
                    c = '\b';
                    break;
                }
                break;
            case 552573414:
                if (str.equals("caption")) {
                    c = '\t';
                    break;
                }
                break;
            case 899152809:
                if (str.equals("commentary")) {
                    c = '\n';
                    break;
                }
                break;
            case 1629013393:
                if (str.equals("emergency")) {
                    c = 11;
                    break;
                }
                break;
            case 1855372047:
                if (str.equals("supplementary")) {
                    c = '\f';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 3:
            case 5:
                return 128;
            case 1:
                return LiteMode.FLAG_CALLS_ANIMATIONS;
            case 2:
                return 2048;
            case 4:
                return 2;
            case 6:
                return 16;
            case 7:
                return 1;
            case '\b':
                return LiteMode.FLAG_CHAT_BLUR;
            case '\t':
                return 64;
            case '\n':
                return 8;
            case 11:
                return 32;
            case '\f':
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
            case R.styleable.AppCompatTheme_checkedTextViewStyle /* 49 */:
                if (str.equals("1")) {
                    c = 0;
                    break;
                }
                break;
            case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                if (str.equals("2")) {
                    c = 1;
                    break;
                }
                break;
            case R.styleable.AppCompatTheme_colorBackgroundFloating /* 51 */:
                if (str.equals("3")) {
                    c = 2;
                    break;
                }
                break;
            case R.styleable.AppCompatTheme_colorButtonNormal /* 52 */:
                if (str.equals("4")) {
                    c = 3;
                    break;
                }
                break;
            case R.styleable.AppCompatTheme_colorControlHighlight /* 54 */:
                if (str.equals("6")) {
                    c = 4;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return LiteMode.FLAG_CALLS_ANIMATIONS;
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

    protected String[] parseProfiles(XmlPullParser xmlPullParser, String str, String[] strArr) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? strArr : attributeValue.split(",");
    }

    protected Pair<Integer, Integer> parseTileCountFromProperties(List<Descriptor> list) {
        String str;
        for (int i = 0; i < list.size(); i++) {
            Descriptor descriptor = list.get(i);
            if ((Ascii.equalsIgnoreCase("http://dashif.org/thumbnail_tile", descriptor.schemeIdUri) || Ascii.equalsIgnoreCase("http://dashif.org/guidelines/thumbnail_tile", descriptor.schemeIdUri)) && (str = descriptor.value) != null) {
                String[] split = Util.split(str, "x");
                if (split.length == 2) {
                    try {
                        return Pair.create(Integer.valueOf(Integer.parseInt(split[0])), Integer.valueOf(Integer.parseInt(split[1])));
                    } catch (NumberFormatException unused) {
                        continue;
                    }
                }
            }
        }
        return null;
    }

    public static void maybeSkipTag(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        if (XmlPullParserUtil.isStartTag(xmlPullParser)) {
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

    private static void fillInClearKeyInformation(ArrayList<DrmInitData.SchemeData> arrayList) {
        String str;
        int i = 0;
        while (true) {
            if (i >= arrayList.size()) {
                str = null;
                break;
            }
            DrmInitData.SchemeData schemeData = arrayList.get(i);
            if (C.CLEARKEY_UUID.equals(schemeData.uuid) && (str = schemeData.licenseServerUrl) != null) {
                arrayList.remove(i);
                break;
            }
            i++;
        }
        if (str == null) {
            return;
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            DrmInitData.SchemeData schemeData2 = arrayList.get(i2);
            if (C.COMMON_PSSH_UUID.equals(schemeData2.uuid) && schemeData2.licenseServerUrl == null) {
                arrayList.set(i2, new DrmInitData.SchemeData(C.CLEARKEY_UUID, str, schemeData2.mimeType, schemeData2.data));
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
        if (MimeTypes.isText(str) || MimeTypes.isImage(str)) {
            return str;
        }
        if ("application/mp4".equals(str)) {
            String mediaMimeType = MimeTypes.getMediaMimeType(str2);
            return "text/vtt".equals(mediaMimeType) ? "application/x-mp4-vtt" : mediaMimeType;
        }
        return null;
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
            if (matcher.matches()) {
                int parseInt = Integer.parseInt(matcher.group(1));
                String group = matcher.group(2);
                return !TextUtils.isEmpty(group) ? parseInt / Integer.parseInt(group) : parseInt;
            }
            return f;
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

    protected static float parseFloat(XmlPullParser xmlPullParser, String str, float f) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? f : Float.parseFloat(attributeValue);
    }

    protected static String parseString(XmlPullParser xmlPullParser, String str, String str2) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? str2 : attributeValue;
    }

    protected static int parseMpegChannelConfiguration(XmlPullParser xmlPullParser) {
        int parseInt = parseInt(xmlPullParser, "value", -1);
        if (parseInt >= 0) {
            int[] iArr = MPEG_CHANNEL_CONFIGURATION_MAPPING;
            if (parseInt < iArr.length) {
                return iArr[parseInt];
            }
            return -1;
        }
        return -1;
    }

    protected static int parseDtsChannelConfiguration(XmlPullParser xmlPullParser) {
        int parseInt = parseInt(xmlPullParser, "value", -1);
        if (parseInt <= 0 || parseInt >= 33) {
            return -1;
        }
        return parseInt;
    }

    protected static int parseDtsxChannelConfiguration(XmlPullParser xmlPullParser) {
        int bitCount;
        String attributeValue = xmlPullParser.getAttributeValue(null, "value");
        if (attributeValue == null || (bitCount = Integer.bitCount(Integer.parseInt(attributeValue, 16))) == 0) {
            return -1;
        }
        return bitCount;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    protected static int parseDolbyChannelConfiguration(XmlPullParser xmlPullParser) {
        char c;
        String attributeValue = xmlPullParser.getAttributeValue(null, "value");
        if (attributeValue == null) {
            return -1;
        }
        String lowerCase = Ascii.toLowerCase(attributeValue);
        lowerCase.hashCode();
        switch (lowerCase.hashCode()) {
            case 1596796:
                if (lowerCase.equals("4000")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 2937391:
                if (lowerCase.equals("a000")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 3094035:
                if (lowerCase.equals("f801")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 3133436:
                if (lowerCase.equals("fa01")) {
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
            if (Ascii.equalsIgnoreCase("http://dashif.org/guidelines/last-segment-number", descriptor.schemeIdUri)) {
                return Long.parseLong(descriptor.value);
            }
        }
        return -1L;
    }

    private boolean isDvbProfileDeclared(String[] strArr) {
        for (String str : strArr) {
            if (str.startsWith("urn:dvb:dash:profile:dvb-dash:")) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static final class RepresentationInfo {
        public final ImmutableList<BaseUrl> baseUrls;
        public final ArrayList<DrmInitData.SchemeData> drmSchemeDatas;
        public final String drmSchemeType;
        public final List<Descriptor> essentialProperties;
        public final Format format;
        public final ArrayList<Descriptor> inbandEventStreams;
        public final long revisionId;
        public final SegmentBase segmentBase;
        public final List<Descriptor> supplementalProperties;

        public RepresentationInfo(Format format, List<BaseUrl> list, SegmentBase segmentBase, String str, ArrayList<DrmInitData.SchemeData> arrayList, ArrayList<Descriptor> arrayList2, List<Descriptor> list2, List<Descriptor> list3, long j) {
            this.format = format;
            this.baseUrls = ImmutableList.copyOf((Collection) list);
            this.segmentBase = segmentBase;
            this.drmSchemeType = str;
            this.drmSchemeDatas = arrayList;
            this.inbandEventStreams = arrayList2;
            this.essentialProperties = list2;
            this.supplementalProperties = list3;
            this.revisionId = j;
        }
    }
}
