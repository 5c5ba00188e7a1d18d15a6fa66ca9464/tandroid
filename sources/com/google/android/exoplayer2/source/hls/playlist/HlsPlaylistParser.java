package com.google.android.exoplayer2.source.hls.playlist;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.hls.HlsTrackMetadataEntry;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMultivariantPlaylist;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.common.collect.Iterables;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.messenger.LiteMode;

/* loaded from: classes.dex */
public final class HlsPlaylistParser implements ParsingLoadable.Parser {
    private final HlsMultivariantPlaylist multivariantPlaylist;
    private final HlsMediaPlaylist previousMediaPlaylist;
    private static final Pattern REGEX_AVERAGE_BANDWIDTH = Pattern.compile("AVERAGE-BANDWIDTH=(\\d+)\\b");
    private static final Pattern REGEX_VIDEO = Pattern.compile("VIDEO=\"(.+?)\"");
    private static final Pattern REGEX_AUDIO = Pattern.compile("AUDIO=\"(.+?)\"");
    private static final Pattern REGEX_SUBTITLES = Pattern.compile("SUBTITLES=\"(.+?)\"");
    private static final Pattern REGEX_CLOSED_CAPTIONS = Pattern.compile("CLOSED-CAPTIONS=\"(.+?)\"");
    private static final Pattern REGEX_BANDWIDTH = Pattern.compile("[^-]BANDWIDTH=(\\d+)\\b");
    private static final Pattern REGEX_CHANNELS = Pattern.compile("CHANNELS=\"(.+?)\"");
    private static final Pattern REGEX_CODECS = Pattern.compile("CODECS=\"(.+?)\"");
    private static final Pattern REGEX_RESOLUTION = Pattern.compile("RESOLUTION=(\\d+x\\d+)");
    private static final Pattern REGEX_FRAME_RATE = Pattern.compile("FRAME-RATE=([\\d\\.]+)\\b");
    private static final Pattern REGEX_TARGET_DURATION = Pattern.compile("#EXT-X-TARGETDURATION:(\\d+)\\b");
    private static final Pattern REGEX_ATTR_DURATION = Pattern.compile("DURATION=([\\d\\.]+)\\b");
    private static final Pattern REGEX_PART_TARGET_DURATION = Pattern.compile("PART-TARGET=([\\d\\.]+)\\b");
    private static final Pattern REGEX_VERSION = Pattern.compile("#EXT-X-VERSION:(\\d+)\\b");
    private static final Pattern REGEX_PLAYLIST_TYPE = Pattern.compile("#EXT-X-PLAYLIST-TYPE:(.+)\\b");
    private static final Pattern REGEX_CAN_SKIP_UNTIL = Pattern.compile("CAN-SKIP-UNTIL=([\\d\\.]+)\\b");
    private static final Pattern REGEX_CAN_SKIP_DATE_RANGES = compileBooleanAttrPattern("CAN-SKIP-DATERANGES");
    private static final Pattern REGEX_SKIPPED_SEGMENTS = Pattern.compile("SKIPPED-SEGMENTS=(\\d+)\\b");
    private static final Pattern REGEX_HOLD_BACK = Pattern.compile("[:|,]HOLD-BACK=([\\d\\.]+)\\b");
    private static final Pattern REGEX_PART_HOLD_BACK = Pattern.compile("PART-HOLD-BACK=([\\d\\.]+)\\b");
    private static final Pattern REGEX_CAN_BLOCK_RELOAD = compileBooleanAttrPattern("CAN-BLOCK-RELOAD");
    private static final Pattern REGEX_MEDIA_SEQUENCE = Pattern.compile("#EXT-X-MEDIA-SEQUENCE:(\\d+)\\b");
    private static final Pattern REGEX_MEDIA_DURATION = Pattern.compile("#EXTINF:([\\d\\.]+)\\b");
    private static final Pattern REGEX_MEDIA_TITLE = Pattern.compile("#EXTINF:[\\d\\.]+\\b,(.+)");
    private static final Pattern REGEX_LAST_MSN = Pattern.compile("LAST-MSN=(\\d+)\\b");
    private static final Pattern REGEX_LAST_PART = Pattern.compile("LAST-PART=(\\d+)\\b");
    private static final Pattern REGEX_TIME_OFFSET = Pattern.compile("TIME-OFFSET=(-?[\\d\\.]+)\\b");
    private static final Pattern REGEX_BYTERANGE = Pattern.compile("#EXT-X-BYTERANGE:(\\d+(?:@\\d+)?)\\b");
    private static final Pattern REGEX_ATTR_BYTERANGE = Pattern.compile("BYTERANGE=\"(\\d+(?:@\\d+)?)\\b\"");
    private static final Pattern REGEX_BYTERANGE_START = Pattern.compile("BYTERANGE-START=(\\d+)\\b");
    private static final Pattern REGEX_BYTERANGE_LENGTH = Pattern.compile("BYTERANGE-LENGTH=(\\d+)\\b");
    private static final Pattern REGEX_METHOD = Pattern.compile("METHOD=(NONE|AES-128|SAMPLE-AES|SAMPLE-AES-CENC|SAMPLE-AES-CTR)\\s*(?:,|$)");
    private static final Pattern REGEX_KEYFORMAT = Pattern.compile("KEYFORMAT=\"(.+?)\"");
    private static final Pattern REGEX_KEYFORMATVERSIONS = Pattern.compile("KEYFORMATVERSIONS=\"(.+?)\"");
    private static final Pattern REGEX_URI = Pattern.compile("URI=\"(.+?)\"");
    private static final Pattern REGEX_IV = Pattern.compile("IV=([^,.*]+)");
    private static final Pattern REGEX_TYPE = Pattern.compile("TYPE=(AUDIO|VIDEO|SUBTITLES|CLOSED-CAPTIONS)");
    private static final Pattern REGEX_PRELOAD_HINT_TYPE = Pattern.compile("TYPE=(PART|MAP)");
    private static final Pattern REGEX_LANGUAGE = Pattern.compile("LANGUAGE=\"(.+?)\"");
    private static final Pattern REGEX_NAME = Pattern.compile("NAME=\"(.+?)\"");
    private static final Pattern REGEX_GROUP_ID = Pattern.compile("GROUP-ID=\"(.+?)\"");
    private static final Pattern REGEX_CHARACTERISTICS = Pattern.compile("CHARACTERISTICS=\"(.+?)\"");
    private static final Pattern REGEX_INSTREAM_ID = Pattern.compile("INSTREAM-ID=\"((?:CC|SERVICE)\\d+)\"");
    private static final Pattern REGEX_AUTOSELECT = compileBooleanAttrPattern("AUTOSELECT");
    private static final Pattern REGEX_DEFAULT = compileBooleanAttrPattern("DEFAULT");
    private static final Pattern REGEX_FORCED = compileBooleanAttrPattern("FORCED");
    private static final Pattern REGEX_INDEPENDENT = compileBooleanAttrPattern("INDEPENDENT");
    private static final Pattern REGEX_GAP = compileBooleanAttrPattern("GAP");
    private static final Pattern REGEX_PRECISE = compileBooleanAttrPattern("PRECISE");
    private static final Pattern REGEX_VALUE = Pattern.compile("VALUE=\"(.+?)\"");
    private static final Pattern REGEX_IMPORT = Pattern.compile("IMPORT=\"(.+?)\"");
    private static final Pattern REGEX_VARIABLE_REFERENCE = Pattern.compile("\\{\\$([a-zA-Z0-9\\-_]+)\\}");

    /* loaded from: classes.dex */
    public static final class DeltaUpdateException extends IOException {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class LineIterator {
        private final Queue extraLines;
        private String next;
        private final BufferedReader reader;

        public LineIterator(Queue queue, BufferedReader bufferedReader) {
            this.extraLines = queue;
            this.reader = bufferedReader;
        }

        public boolean hasNext() {
            String trim;
            if (this.next != null) {
                return true;
            }
            if (!this.extraLines.isEmpty()) {
                this.next = (String) Assertions.checkNotNull((String) this.extraLines.poll());
                return true;
            }
            do {
                String readLine = this.reader.readLine();
                this.next = readLine;
                if (readLine == null) {
                    return false;
                }
                trim = readLine.trim();
                this.next = trim;
            } while (trim.isEmpty());
            return true;
        }

        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            String str = this.next;
            this.next = null;
            return str;
        }
    }

    public HlsPlaylistParser() {
        this(HlsMultivariantPlaylist.EMPTY, null);
    }

    public HlsPlaylistParser(HlsMultivariantPlaylist hlsMultivariantPlaylist, HlsMediaPlaylist hlsMediaPlaylist) {
        this.multivariantPlaylist = hlsMultivariantPlaylist;
        this.previousMediaPlaylist = hlsMediaPlaylist;
    }

    private static boolean checkPlaylistHeader(BufferedReader bufferedReader) {
        int read = bufferedReader.read();
        if (read == 239) {
            if (bufferedReader.read() != 187 || bufferedReader.read() != 191) {
                return false;
            }
            read = bufferedReader.read();
        }
        int skipIgnorableWhitespace = skipIgnorableWhitespace(bufferedReader, true, read);
        for (int i = 0; i < 7; i++) {
            if (skipIgnorableWhitespace != "#EXTM3U".charAt(i)) {
                return false;
            }
            skipIgnorableWhitespace = bufferedReader.read();
        }
        return Util.isLinebreak(skipIgnorableWhitespace(bufferedReader, false, skipIgnorableWhitespace));
    }

    private static Pattern compileBooleanAttrPattern(String str) {
        return Pattern.compile(str + "=(NO|YES)");
    }

    private static DrmInitData getPlaylistProtectionSchemes(String str, DrmInitData.SchemeData[] schemeDataArr) {
        DrmInitData.SchemeData[] schemeDataArr2 = new DrmInitData.SchemeData[schemeDataArr.length];
        for (int i = 0; i < schemeDataArr.length; i++) {
            schemeDataArr2[i] = schemeDataArr[i].copyWithData(null);
        }
        return new DrmInitData(str, schemeDataArr2);
    }

    private static String getSegmentEncryptionIV(long j, String str, String str2) {
        if (str == null) {
            return null;
        }
        return str2 != null ? str2 : Long.toHexString(j);
    }

    private static HlsMultivariantPlaylist.Variant getVariantWithAudioGroup(ArrayList arrayList, String str) {
        for (int i = 0; i < arrayList.size(); i++) {
            HlsMultivariantPlaylist.Variant variant = (HlsMultivariantPlaylist.Variant) arrayList.get(i);
            if (str.equals(variant.audioGroupId)) {
                return variant;
            }
        }
        return null;
    }

    private static HlsMultivariantPlaylist.Variant getVariantWithSubtitleGroup(ArrayList arrayList, String str) {
        for (int i = 0; i < arrayList.size(); i++) {
            HlsMultivariantPlaylist.Variant variant = (HlsMultivariantPlaylist.Variant) arrayList.get(i);
            if (str.equals(variant.subtitleGroupId)) {
                return variant;
            }
        }
        return null;
    }

    private static HlsMultivariantPlaylist.Variant getVariantWithVideoGroup(ArrayList arrayList, String str) {
        for (int i = 0; i < arrayList.size(); i++) {
            HlsMultivariantPlaylist.Variant variant = (HlsMultivariantPlaylist.Variant) arrayList.get(i);
            if (str.equals(variant.videoGroupId)) {
                return variant;
            }
        }
        return null;
    }

    private static double parseDoubleAttr(String str, Pattern pattern) {
        return Double.parseDouble(parseStringAttr(str, pattern, Collections.emptyMap()));
    }

    private static DrmInitData.SchemeData parseDrmSchemeData(String str, String str2, Map map) {
        String parseOptionalStringAttr = parseOptionalStringAttr(str, REGEX_KEYFORMATVERSIONS, "1", map);
        if ("urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed".equals(str2)) {
            String parseStringAttr = parseStringAttr(str, REGEX_URI, map);
            return new DrmInitData.SchemeData(C.WIDEVINE_UUID, "video/mp4", Base64.decode(parseStringAttr.substring(parseStringAttr.indexOf(44)), 0));
        }
        if ("com.widevine".equals(str2)) {
            return new DrmInitData.SchemeData(C.WIDEVINE_UUID, "hls", Util.getUtf8Bytes(str));
        }
        if (!"com.microsoft.playready".equals(str2) || !"1".equals(parseOptionalStringAttr)) {
            return null;
        }
        String parseStringAttr2 = parseStringAttr(str, REGEX_URI, map);
        byte[] decode = Base64.decode(parseStringAttr2.substring(parseStringAttr2.indexOf(44)), 0);
        UUID uuid = C.PLAYREADY_UUID;
        return new DrmInitData.SchemeData(uuid, "video/mp4", PsshAtomUtil.buildPsshAtom(uuid, decode));
    }

    private static String parseEncryptionScheme(String str) {
        return ("SAMPLE-AES-CENC".equals(str) || "SAMPLE-AES-CTR".equals(str)) ? "cenc" : "cbcs";
    }

    private static int parseIntAttr(String str, Pattern pattern) {
        return Integer.parseInt(parseStringAttr(str, pattern, Collections.emptyMap()));
    }

    private static long parseLongAttr(String str, Pattern pattern) {
        return Long.parseLong(parseStringAttr(str, pattern, Collections.emptyMap()));
    }

    /* JADX WARN: Code restructure failed: missing block: B:268:0x01dc, code lost:
    
        if (r12 != null) goto L68;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static HlsMediaPlaylist parseMediaPlaylist(HlsMultivariantPlaylist hlsMultivariantPlaylist, HlsMediaPlaylist hlsMediaPlaylist, LineIterator lineIterator, String str) {
        ArrayList arrayList;
        ArrayList arrayList2;
        String str2;
        String str3;
        long j;
        int i;
        HlsMediaPlaylist.Part part;
        String parseStringAttr;
        int i2;
        long j2;
        long j3;
        DrmInitData drmInitData;
        long j4;
        long j5;
        HlsMultivariantPlaylist hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
        HlsMediaPlaylist hlsMediaPlaylist2 = hlsMediaPlaylist;
        boolean z = hlsMultivariantPlaylist2.hasIndependentSegments;
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        HlsMediaPlaylist.ServerControl serverControl = new HlsMediaPlaylist.ServerControl(-9223372036854775807L, false, -9223372036854775807L, -9223372036854775807L, false);
        TreeMap treeMap = new TreeMap();
        boolean z2 = false;
        String str4 = "";
        boolean z3 = z;
        HlsMediaPlaylist.ServerControl serverControl2 = serverControl;
        String str5 = str4;
        int i3 = 0;
        String str6 = null;
        long j6 = -9223372036854775807L;
        boolean z4 = false;
        long j7 = 0;
        boolean z5 = false;
        int i4 = 0;
        long j8 = 0;
        int i5 = 1;
        long j9 = -9223372036854775807L;
        long j10 = -9223372036854775807L;
        boolean z6 = false;
        DrmInitData drmInitData2 = null;
        long j11 = 0;
        DrmInitData drmInitData3 = null;
        long j12 = 0;
        boolean z7 = false;
        long j13 = -1;
        String str7 = null;
        String str8 = null;
        int i6 = 0;
        long j14 = 0;
        long j15 = 0;
        boolean z8 = false;
        HlsMediaPlaylist.Segment segment = null;
        long j16 = 0;
        long j17 = 0;
        ArrayList arrayList7 = arrayList4;
        HlsMediaPlaylist.Part part2 = null;
        while (lineIterator.hasNext()) {
            String next = lineIterator.next();
            if (next.startsWith("#EXT")) {
                arrayList6.add(next);
            }
            if (next.startsWith("#EXT-X-PLAYLIST-TYPE")) {
                String parseStringAttr2 = parseStringAttr(next, REGEX_PLAYLIST_TYPE, hashMap);
                if ("VOD".equals(parseStringAttr2)) {
                    i3 = 1;
                } else if ("EVENT".equals(parseStringAttr2)) {
                    i3 = 2;
                } else {
                    arrayList = arrayList7;
                    arrayList2 = arrayList6;
                    str2 = str7;
                    str3 = str8;
                    j = j15;
                    i = i3;
                }
            } else if (next.equals("#EXT-X-I-FRAMES-ONLY")) {
                z8 = true;
            } else if (next.startsWith("#EXT-X-START")) {
                j6 = (long) (parseDoubleAttr(next, REGEX_TIME_OFFSET) * 1000000.0d);
                z4 = parseOptionalBooleanAttribute(next, REGEX_PRECISE, z2);
            } else if (next.startsWith("#EXT-X-SERVER-CONTROL")) {
                serverControl2 = parseServerControl(next);
            } else if (next.startsWith("#EXT-X-PART-INF")) {
                j10 = (long) (parseDoubleAttr(next, REGEX_PART_TARGET_DURATION) * 1000000.0d);
            } else if (next.startsWith("#EXT-X-MAP")) {
                String parseStringAttr3 = parseStringAttr(next, REGEX_URI, hashMap);
                String parseOptionalStringAttr = parseOptionalStringAttr(next, REGEX_ATTR_BYTERANGE, hashMap);
                if (parseOptionalStringAttr != null) {
                    String[] split = Util.split(parseOptionalStringAttr, "@");
                    j13 = Long.parseLong(split[z2 ? 1 : 0]);
                    if (split.length > 1) {
                        j11 = Long.parseLong(split[1]);
                    }
                }
                if (j13 == -1) {
                    j11 = 0;
                }
                String str9 = str7;
                if (str6 != null && str9 == null) {
                    throw ParserException.createForMalformedManifest("The encryption IV attribute must be present when an initialization segment is encrypted with METHOD=AES-128.", null);
                }
                segment = new HlsMediaPlaylist.Segment(parseStringAttr3, j11, j13, str6, str9);
                if (j13 != -1) {
                    j11 += j13;
                }
                str7 = str9;
                j13 = -1;
            } else {
                str2 = str7;
                str7 = null;
                if (next.startsWith("#EXT-X-TARGETDURATION")) {
                    j9 = 1000000 * parseIntAttr(next, REGEX_TARGET_DURATION);
                } else {
                    if (next.startsWith("#EXT-X-MEDIA-SEQUENCE")) {
                        j15 = parseLongAttr(next, REGEX_MEDIA_SEQUENCE);
                        str7 = str2;
                        j8 = j15;
                    } else if (next.startsWith("#EXT-X-VERSION")) {
                        i5 = parseIntAttr(next, REGEX_VERSION);
                    } else if (next.startsWith("#EXT-X-DEFINE")) {
                        String parseOptionalStringAttr2 = parseOptionalStringAttr(next, REGEX_IMPORT, hashMap);
                        if (parseOptionalStringAttr2 != null) {
                            parseStringAttr = (String) hlsMultivariantPlaylist2.variableDefinitions.get(parseOptionalStringAttr2);
                        } else {
                            parseOptionalStringAttr2 = parseStringAttr(next, REGEX_NAME, hashMap);
                            parseStringAttr = parseStringAttr(next, REGEX_VALUE, hashMap);
                        }
                        hashMap.put(parseOptionalStringAttr2, parseStringAttr);
                        arrayList = arrayList7;
                        arrayList2 = arrayList6;
                        str3 = str8;
                        j = j15;
                        i = i3;
                    } else if (next.startsWith("#EXTINF")) {
                        j16 = parseTimeSecondsToUs(next, REGEX_MEDIA_DURATION);
                        str5 = parseOptionalStringAttr(next, REGEX_MEDIA_TITLE, str4, hashMap);
                    } else {
                        String str10 = str4;
                        if (next.startsWith("#EXT-X-SKIP")) {
                            int parseIntAttr = parseIntAttr(next, REGEX_SKIPPED_SEGMENTS);
                            Assertions.checkState(hlsMediaPlaylist2 != null && arrayList3.isEmpty());
                            int i7 = (int) (j8 - ((HlsMediaPlaylist) Util.castNonNull(hlsMediaPlaylist)).mediaSequence);
                            int i8 = parseIntAttr + i7;
                            if (i7 < 0 || i8 > hlsMediaPlaylist2.segments.size()) {
                                throw new DeltaUpdateException();
                            }
                            str4 = str10;
                            String str11 = str2;
                            long j18 = j14;
                            while (i7 < i8) {
                                HlsMediaPlaylist.Segment segment2 = (HlsMediaPlaylist.Segment) hlsMediaPlaylist2.segments.get(i7);
                                ArrayList arrayList8 = arrayList7;
                                ArrayList arrayList9 = arrayList6;
                                if (j8 != hlsMediaPlaylist2.mediaSequence) {
                                    segment2 = segment2.copyWith(j18, (hlsMediaPlaylist2.discontinuitySequence - i4) + segment2.relativeDiscontinuitySequence);
                                }
                                arrayList3.add(segment2);
                                j18 += segment2.durationUs;
                                long j19 = segment2.byteRangeLength;
                                if (j19 != -1) {
                                    i2 = i8;
                                    j11 = segment2.byteRangeOffset + j19;
                                } else {
                                    i2 = i8;
                                }
                                int i9 = segment2.relativeDiscontinuitySequence;
                                HlsMediaPlaylist.Segment segment3 = segment2.initializationSegment;
                                DrmInitData drmInitData4 = segment2.drmInitData;
                                String str12 = segment2.fullSegmentEncryptionKeyUri;
                                String str13 = segment2.encryptionIV;
                                if (str13 == null || !str13.equals(Long.toHexString(j15))) {
                                    str11 = segment2.encryptionIV;
                                }
                                j15++;
                                i7++;
                                hlsMediaPlaylist2 = hlsMediaPlaylist;
                                drmInitData3 = drmInitData4;
                                str6 = str12;
                                j12 = j18;
                                i8 = i2;
                                i6 = i9;
                                segment = segment3;
                                arrayList7 = arrayList8;
                                arrayList6 = arrayList9;
                            }
                            hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                            hlsMediaPlaylist2 = hlsMediaPlaylist;
                            j14 = j18;
                            str7 = str11;
                        } else {
                            ArrayList arrayList10 = arrayList7;
                            arrayList2 = arrayList6;
                            str4 = str10;
                            if (next.startsWith("#EXT-X-KEY")) {
                                String parseStringAttr4 = parseStringAttr(next, REGEX_METHOD, hashMap);
                                String parseOptionalStringAttr3 = parseOptionalStringAttr(next, REGEX_KEYFORMAT, "identity", hashMap);
                                if ("NONE".equals(parseStringAttr4)) {
                                    treeMap.clear();
                                    hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                    hlsMediaPlaylist2 = hlsMediaPlaylist;
                                    arrayList7 = arrayList10;
                                    str6 = null;
                                    drmInitData3 = null;
                                } else {
                                    String parseOptionalStringAttr4 = parseOptionalStringAttr(next, REGEX_IV, hashMap);
                                    if (!"identity".equals(parseOptionalStringAttr3)) {
                                        String str14 = str8;
                                        str8 = str14 == null ? parseEncryptionScheme(parseStringAttr4) : str14;
                                        DrmInitData.SchemeData parseDrmSchemeData = parseDrmSchemeData(next, parseOptionalStringAttr3, hashMap);
                                        if (parseDrmSchemeData != null) {
                                            treeMap.put(parseOptionalStringAttr3, parseDrmSchemeData);
                                            hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                            hlsMediaPlaylist2 = hlsMediaPlaylist;
                                            arrayList7 = arrayList10;
                                            str6 = null;
                                            drmInitData3 = null;
                                            z2 = false;
                                            str7 = parseOptionalStringAttr4;
                                            arrayList6 = arrayList2;
                                        }
                                    } else if ("AES-128".equals(parseStringAttr4)) {
                                        str6 = parseStringAttr(next, REGEX_URI, hashMap);
                                        hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                        hlsMediaPlaylist2 = hlsMediaPlaylist;
                                        str7 = parseOptionalStringAttr4;
                                        arrayList7 = arrayList10;
                                    }
                                    hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                    hlsMediaPlaylist2 = hlsMediaPlaylist;
                                    arrayList7 = arrayList10;
                                    str6 = null;
                                    z2 = false;
                                    str7 = parseOptionalStringAttr4;
                                    arrayList6 = arrayList2;
                                }
                            } else {
                                String str15 = str8;
                                if (next.startsWith("#EXT-X-BYTERANGE")) {
                                    String[] split2 = Util.split(parseStringAttr(next, REGEX_BYTERANGE, hashMap), "@");
                                    j13 = Long.parseLong(split2[0]);
                                    if (split2.length > 1) {
                                        j11 = Long.parseLong(split2[1]);
                                    }
                                } else if (next.startsWith("#EXT-X-DISCONTINUITY-SEQUENCE")) {
                                    i4 = Integer.parseInt(next.substring(next.indexOf(58) + 1));
                                    hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                    hlsMediaPlaylist2 = hlsMediaPlaylist;
                                    str8 = str15;
                                    str7 = str2;
                                    arrayList7 = arrayList10;
                                    arrayList6 = arrayList2;
                                    z2 = false;
                                    z5 = true;
                                } else if (next.equals("#EXT-X-DISCONTINUITY")) {
                                    i6++;
                                } else {
                                    if (next.startsWith("#EXT-X-PROGRAM-DATE-TIME")) {
                                        if (j7 == 0) {
                                            j7 = Util.msToUs(Util.parseXsDateTime(next.substring(next.indexOf(58) + 1))) - j14;
                                        } else {
                                            i = i3;
                                            str3 = str15;
                                        }
                                    } else if (next.equals("#EXT-X-GAP")) {
                                        hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                        hlsMediaPlaylist2 = hlsMediaPlaylist;
                                        str8 = str15;
                                        str7 = str2;
                                        arrayList7 = arrayList10;
                                        arrayList6 = arrayList2;
                                        z2 = false;
                                        z7 = true;
                                    } else if (next.equals("#EXT-X-INDEPENDENT-SEGMENTS")) {
                                        hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                        hlsMediaPlaylist2 = hlsMediaPlaylist;
                                        str8 = str15;
                                        str7 = str2;
                                        arrayList7 = arrayList10;
                                        arrayList6 = arrayList2;
                                        z2 = false;
                                        z3 = true;
                                    } else if (next.equals("#EXT-X-ENDLIST")) {
                                        hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                        hlsMediaPlaylist2 = hlsMediaPlaylist;
                                        str8 = str15;
                                        str7 = str2;
                                        arrayList7 = arrayList10;
                                        arrayList6 = arrayList2;
                                        z2 = false;
                                        z6 = true;
                                    } else if (next.startsWith("#EXT-X-RENDITION-REPORT")) {
                                        i = i3;
                                        str3 = str15;
                                        arrayList5.add(new HlsMediaPlaylist.RenditionReport(Uri.parse(UriUtil.resolve(str, parseStringAttr(next, REGEX_URI, hashMap))), parseOptionalLongAttr(next, REGEX_LAST_MSN, -1L), parseOptionalIntAttr(next, REGEX_LAST_PART, -1)));
                                    } else {
                                        i = i3;
                                        str3 = str15;
                                        if (!next.startsWith("#EXT-X-PRELOAD-HINT")) {
                                            j = j15;
                                            if (next.startsWith("#EXT-X-PART")) {
                                                String segmentEncryptionIV = getSegmentEncryptionIV(j, str6, str2);
                                                String parseStringAttr5 = parseStringAttr(next, REGEX_URI, hashMap);
                                                long parseDoubleAttr = (long) (parseDoubleAttr(next, REGEX_ATTR_DURATION) * 1000000.0d);
                                                part = part2;
                                                boolean parseOptionalBooleanAttribute = parseOptionalBooleanAttribute(next, REGEX_INDEPENDENT, false) | (z3 && arrayList10.isEmpty());
                                                boolean parseOptionalBooleanAttribute2 = parseOptionalBooleanAttribute(next, REGEX_GAP, false);
                                                String parseOptionalStringAttr5 = parseOptionalStringAttr(next, REGEX_ATTR_BYTERANGE, hashMap);
                                                if (parseOptionalStringAttr5 != null) {
                                                    String[] split3 = Util.split(parseOptionalStringAttr5, "@");
                                                    j5 = Long.parseLong(split3[0]);
                                                    if (split3.length > 1) {
                                                        j17 = Long.parseLong(split3[1]);
                                                    }
                                                    j4 = -1;
                                                } else {
                                                    j4 = -1;
                                                    j5 = -1;
                                                }
                                                if (j5 == j4) {
                                                    j17 = 0;
                                                }
                                                if (drmInitData3 == null && !treeMap.isEmpty()) {
                                                    DrmInitData.SchemeData[] schemeDataArr = (DrmInitData.SchemeData[]) treeMap.values().toArray(new DrmInitData.SchemeData[0]);
                                                    DrmInitData drmInitData5 = new DrmInitData(str3, schemeDataArr);
                                                    if (drmInitData2 == null) {
                                                        drmInitData2 = getPlaylistProtectionSchemes(str3, schemeDataArr);
                                                    }
                                                    drmInitData3 = drmInitData5;
                                                }
                                                arrayList = arrayList10;
                                                arrayList.add(new HlsMediaPlaylist.Part(parseStringAttr5, segment, parseDoubleAttr, i6, j12, drmInitData3, str6, segmentEncryptionIV, j17, j5, parseOptionalBooleanAttribute2, parseOptionalBooleanAttribute, false));
                                                j12 += parseDoubleAttr;
                                                if (j5 != -1) {
                                                    j17 += j5;
                                                }
                                            } else {
                                                part = part2;
                                                arrayList = arrayList10;
                                                if (!next.startsWith("#")) {
                                                    String segmentEncryptionIV2 = getSegmentEncryptionIV(j, str6, str2);
                                                    long j20 = j + 1;
                                                    String replaceVariableReferences = replaceVariableReferences(next, hashMap);
                                                    HlsMediaPlaylist.Segment segment4 = (HlsMediaPlaylist.Segment) hashMap2.get(replaceVariableReferences);
                                                    if (j13 == -1) {
                                                        j2 = 0;
                                                    } else {
                                                        if (z8 && segment == null && segment4 == null) {
                                                            segment4 = new HlsMediaPlaylist.Segment(replaceVariableReferences, 0L, j11, null, null);
                                                            hashMap2.put(replaceVariableReferences, segment4);
                                                        }
                                                        j2 = j11;
                                                    }
                                                    if (drmInitData3 != null || treeMap.isEmpty()) {
                                                        j3 = j20;
                                                        drmInitData = drmInitData3;
                                                    } else {
                                                        j3 = j20;
                                                        DrmInitData.SchemeData[] schemeDataArr2 = (DrmInitData.SchemeData[]) treeMap.values().toArray(new DrmInitData.SchemeData[0]);
                                                        drmInitData = new DrmInitData(str3, schemeDataArr2);
                                                        if (drmInitData2 == null) {
                                                            drmInitData2 = getPlaylistProtectionSchemes(str3, schemeDataArr2);
                                                        }
                                                    }
                                                    arrayList3.add(new HlsMediaPlaylist.Segment(replaceVariableReferences, segment != null ? segment : segment4, str5, j16, i6, j14, drmInitData, str6, segmentEncryptionIV2, j2, j13, z7, arrayList));
                                                    j12 = j14 + j16;
                                                    arrayList7 = new ArrayList();
                                                    if (j13 != -1) {
                                                        j2 += j13;
                                                    }
                                                    j11 = j2;
                                                    hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                                    hlsMediaPlaylist2 = hlsMediaPlaylist;
                                                    drmInitData3 = drmInitData;
                                                    str7 = str2;
                                                    str5 = str4;
                                                    j14 = j12;
                                                    i3 = i;
                                                    part2 = part;
                                                    arrayList6 = arrayList2;
                                                    z7 = false;
                                                    j13 = -1;
                                                    j16 = 0;
                                                    str8 = str3;
                                                    j15 = j3;
                                                }
                                            }
                                            hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                            hlsMediaPlaylist2 = hlsMediaPlaylist;
                                            str7 = str2;
                                            i3 = i;
                                            part2 = part;
                                            j15 = j;
                                            str8 = str3;
                                            arrayList7 = arrayList;
                                        } else if (part2 == null && "PART".equals(parseStringAttr(next, REGEX_PRELOAD_HINT_TYPE, hashMap))) {
                                            String parseStringAttr6 = parseStringAttr(next, REGEX_URI, hashMap);
                                            long parseOptionalLongAttr = parseOptionalLongAttr(next, REGEX_BYTERANGE_START, -1L);
                                            long parseOptionalLongAttr2 = parseOptionalLongAttr(next, REGEX_BYTERANGE_LENGTH, -1L);
                                            long j21 = j15;
                                            String segmentEncryptionIV3 = getSegmentEncryptionIV(j21, str6, str2);
                                            if (drmInitData3 == null && !treeMap.isEmpty()) {
                                                DrmInitData.SchemeData[] schemeDataArr3 = (DrmInitData.SchemeData[]) treeMap.values().toArray(new DrmInitData.SchemeData[0]);
                                                DrmInitData drmInitData6 = new DrmInitData(str3, schemeDataArr3);
                                                if (drmInitData2 == null) {
                                                    drmInitData2 = getPlaylistProtectionSchemes(str3, schemeDataArr3);
                                                }
                                                drmInitData3 = drmInitData6;
                                            }
                                            if (parseOptionalLongAttr == -1 || parseOptionalLongAttr2 != -1) {
                                                part2 = new HlsMediaPlaylist.Part(parseStringAttr6, segment, 0L, i6, j12, drmInitData3, str6, segmentEncryptionIV3, parseOptionalLongAttr != -1 ? parseOptionalLongAttr : 0L, parseOptionalLongAttr2, false, false, true);
                                            }
                                            hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                            hlsMediaPlaylist2 = hlsMediaPlaylist;
                                            j15 = j21;
                                            str7 = str2;
                                            arrayList7 = arrayList10;
                                            i3 = i;
                                            arrayList6 = arrayList2;
                                            str8 = str3;
                                        }
                                    }
                                    arrayList = arrayList10;
                                    j = j15;
                                }
                                hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                hlsMediaPlaylist2 = hlsMediaPlaylist;
                                str8 = str15;
                                str7 = str2;
                                arrayList7 = arrayList10;
                            }
                            arrayList6 = arrayList2;
                        }
                    }
                    z2 = false;
                }
                str7 = str2;
                z2 = false;
            }
            part = part2;
            hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
            hlsMediaPlaylist2 = hlsMediaPlaylist;
            str7 = str2;
            i3 = i;
            part2 = part;
            j15 = j;
            str8 = str3;
            arrayList7 = arrayList;
            arrayList6 = arrayList2;
            z2 = false;
        }
        int i10 = i3;
        HlsMediaPlaylist.Part part3 = part2;
        ArrayList arrayList11 = arrayList7;
        ArrayList arrayList12 = arrayList6;
        HashMap hashMap3 = new HashMap();
        for (int i11 = 0; i11 < arrayList5.size(); i11++) {
            HlsMediaPlaylist.RenditionReport renditionReport = (HlsMediaPlaylist.RenditionReport) arrayList5.get(i11);
            long j22 = renditionReport.lastMediaSequence;
            if (j22 == -1) {
                j22 = (j8 + arrayList3.size()) - (arrayList11.isEmpty() ? 1L : 0L);
            }
            int i12 = renditionReport.lastPartIndex;
            if (i12 == -1 && j10 != -9223372036854775807L) {
                i12 = (arrayList11.isEmpty() ? ((HlsMediaPlaylist.Segment) Iterables.getLast(arrayList3)).parts : arrayList11).size() - 1;
            }
            Uri uri = renditionReport.playlistUri;
            hashMap3.put(uri, new HlsMediaPlaylist.RenditionReport(uri, j22, i12));
        }
        if (part3 != null) {
            arrayList11.add(part3);
        }
        return new HlsMediaPlaylist(i10, str, arrayList12, j6, z4, j7, z5, i4, j8, i5, j9, j10, z3, z6, j7 != 0, drmInitData2, arrayList3, arrayList11, serverControl2, hashMap3);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:94:0x032a. Please report as an issue. */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0122  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x014c  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x01b5  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0157  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0129  */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.util.List] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static HlsMultivariantPlaylist parseMultivariantPlaylist(LineIterator lineIterator, String str) {
        char c;
        Format format;
        ArrayList arrayList;
        ArrayList arrayList2;
        String str2;
        ArrayList arrayList3;
        int parseInt;
        String str3;
        String str4;
        int i;
        ArrayList arrayList4;
        ArrayList arrayList5;
        int i2;
        int i3;
        String replaceVariableReferences;
        ArrayList arrayList6;
        String str5 = str;
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        ArrayList arrayList7 = new ArrayList();
        ArrayList arrayList8 = new ArrayList();
        ArrayList arrayList9 = new ArrayList();
        ArrayList arrayList10 = new ArrayList();
        ArrayList arrayList11 = new ArrayList();
        ArrayList arrayList12 = new ArrayList();
        ArrayList arrayList13 = new ArrayList();
        ArrayList arrayList14 = new ArrayList();
        boolean z = false;
        boolean z2 = false;
        while (true) {
            String str6 = "application/x-mpegURL";
            if (!lineIterator.hasNext()) {
                HashMap hashMap3 = hashMap;
                ArrayList arrayList15 = arrayList8;
                ArrayList arrayList16 = arrayList9;
                ArrayList arrayList17 = arrayList10;
                ArrayList arrayList18 = arrayList11;
                ArrayList arrayList19 = arrayList12;
                ArrayList arrayList20 = arrayList14;
                ArrayList arrayList21 = arrayList13;
                boolean z3 = z;
                ArrayList arrayList22 = new ArrayList();
                HashSet hashSet = new HashSet();
                int i4 = 0;
                while (i4 < arrayList7.size()) {
                    HlsMultivariantPlaylist.Variant variant = (HlsMultivariantPlaylist.Variant) arrayList7.get(i4);
                    if (hashSet.add(variant.url)) {
                        Assertions.checkState(variant.format.metadata == null);
                        i = 1;
                        arrayList22.add(variant.copyWithFormat(variant.format.buildUpon().setMetadata(new Metadata(new HlsTrackMetadataEntry(null, null, (List) Assertions.checkNotNull((ArrayList) hashMap3.get(variant.url))))).build()));
                    } else {
                        i = 1;
                    }
                    i4 += i;
                }
                Uri uri = null;
                ArrayList arrayList23 = null;
                Format format2 = null;
                int i5 = 0;
                while (i5 < arrayList19.size()) {
                    ArrayList arrayList24 = arrayList19;
                    String str7 = (String) arrayList24.get(i5);
                    String parseStringAttr = parseStringAttr(str7, REGEX_GROUP_ID, hashMap2);
                    String parseStringAttr2 = parseStringAttr(str7, REGEX_NAME, hashMap2);
                    Format.Builder language = new Format.Builder().setId(parseStringAttr + ":" + parseStringAttr2).setLabel(parseStringAttr2).setContainerMimeType(str6).setSelectionFlags(parseSelectionFlags(str7)).setRoleFlags(parseRoleFlags(str7, hashMap2)).setLanguage(parseOptionalStringAttr(str7, REGEX_LANGUAGE, hashMap2));
                    String parseOptionalStringAttr = parseOptionalStringAttr(str7, REGEX_URI, hashMap2);
                    Uri resolveToUri = parseOptionalStringAttr == null ? uri : UriUtil.resolveToUri(str, parseOptionalStringAttr);
                    arrayList19 = arrayList24;
                    String str8 = str6;
                    Metadata metadata = new Metadata(new HlsTrackMetadataEntry(parseStringAttr, parseStringAttr2, Collections.emptyList()));
                    String parseStringAttr3 = parseStringAttr(str7, REGEX_TYPE, hashMap2);
                    parseStringAttr3.hashCode();
                    switch (parseStringAttr3.hashCode()) {
                        case -959297733:
                            if (parseStringAttr3.equals("SUBTITLES")) {
                                c = 0;
                                break;
                            }
                            break;
                        case -333210994:
                            if (parseStringAttr3.equals("CLOSED-CAPTIONS")) {
                                c = 1;
                                break;
                            }
                            break;
                        case 62628790:
                            if (parseStringAttr3.equals("AUDIO")) {
                                c = 2;
                                break;
                            }
                            break;
                        case 81665115:
                            if (parseStringAttr3.equals("VIDEO")) {
                                c = 3;
                                break;
                            }
                            break;
                    }
                    c = 65535;
                    switch (c) {
                        case 0:
                            format = format2;
                            arrayList = arrayList16;
                            arrayList2 = arrayList15;
                            HlsMultivariantPlaylist.Variant variantWithSubtitleGroup = getVariantWithSubtitleGroup(arrayList7, parseStringAttr);
                            if (variantWithSubtitleGroup != null) {
                                String codecsOfType = Util.getCodecsOfType(variantWithSubtitleGroup.format.codecs, 3);
                                language.setCodecs(codecsOfType);
                                str2 = MimeTypes.getMediaMimeType(codecsOfType);
                            } else {
                                str2 = null;
                            }
                            if (str2 == null) {
                                str2 = "text/vtt";
                            }
                            language.setSampleMimeType(str2).setMetadata(metadata);
                            if (resolveToUri == null) {
                                arrayList3 = arrayList17;
                                Log.w("HlsPlaylistParser", "EXT-X-MEDIA tag with missing mandatory URI attribute: skipping");
                                break;
                            } else {
                                HlsMultivariantPlaylist.Rendition rendition = new HlsMultivariantPlaylist.Rendition(resolveToUri, language.build(), parseStringAttr, parseStringAttr2);
                                arrayList3 = arrayList17;
                                arrayList3.add(rendition);
                                break;
                            }
                        case 1:
                            format = format2;
                            arrayList = arrayList16;
                            arrayList2 = arrayList15;
                            String parseStringAttr4 = parseStringAttr(str7, REGEX_INSTREAM_ID, hashMap2);
                            if (parseStringAttr4.startsWith("CC")) {
                                parseInt = Integer.parseInt(parseStringAttr4.substring(2));
                                str3 = "application/cea-608";
                            } else {
                                parseInt = Integer.parseInt(parseStringAttr4.substring(7));
                                str3 = "application/cea-708";
                            }
                            if (arrayList23 == null) {
                                arrayList23 = new ArrayList();
                            }
                            language.setSampleMimeType(str3).setAccessibilityChannel(parseInt);
                            arrayList23.add(language.build());
                            arrayList3 = arrayList17;
                            break;
                        case 2:
                            arrayList2 = arrayList15;
                            HlsMultivariantPlaylist.Variant variantWithAudioGroup = getVariantWithAudioGroup(arrayList7, parseStringAttr);
                            if (variantWithAudioGroup != null) {
                                format = format2;
                                String codecsOfType2 = Util.getCodecsOfType(variantWithAudioGroup.format.codecs, 1);
                                language.setCodecs(codecsOfType2);
                                str4 = MimeTypes.getMediaMimeType(codecsOfType2);
                            } else {
                                format = format2;
                                str4 = null;
                            }
                            String parseOptionalStringAttr2 = parseOptionalStringAttr(str7, REGEX_CHANNELS, hashMap2);
                            if (parseOptionalStringAttr2 != null) {
                                language.setChannelCount(Integer.parseInt(Util.splitAtFirst(parseOptionalStringAttr2, "/")[0]));
                                if ("audio/eac3".equals(str4) && parseOptionalStringAttr2.endsWith("/JOC")) {
                                    language.setCodecs("ec+3");
                                    str4 = "audio/eac3-joc";
                                }
                            }
                            language.setSampleMimeType(str4);
                            if (resolveToUri != null) {
                                language.setMetadata(metadata);
                                arrayList = arrayList16;
                                arrayList.add(new HlsMultivariantPlaylist.Rendition(resolveToUri, language.build(), parseStringAttr, parseStringAttr2));
                            } else {
                                arrayList = arrayList16;
                                if (variantWithAudioGroup != null) {
                                    format = language.build();
                                }
                            }
                            arrayList3 = arrayList17;
                            break;
                        case 3:
                            HlsMultivariantPlaylist.Variant variantWithVideoGroup = getVariantWithVideoGroup(arrayList7, parseStringAttr);
                            if (variantWithVideoGroup != null) {
                                Format format3 = variantWithVideoGroup.format;
                                String codecsOfType3 = Util.getCodecsOfType(format3.codecs, 2);
                                language.setCodecs(codecsOfType3).setSampleMimeType(MimeTypes.getMediaMimeType(codecsOfType3)).setWidth(format3.width).setHeight(format3.height).setFrameRate(format3.frameRate);
                            }
                            if (resolveToUri != null) {
                                language.setMetadata(metadata);
                                arrayList2 = arrayList15;
                                arrayList2.add(new HlsMultivariantPlaylist.Rendition(resolveToUri, language.build(), parseStringAttr, parseStringAttr2));
                                format = format2;
                                arrayList3 = arrayList17;
                                arrayList = arrayList16;
                                break;
                            }
                        default:
                            format = format2;
                            arrayList3 = arrayList17;
                            arrayList = arrayList16;
                            arrayList2 = arrayList15;
                            break;
                    }
                    i5++;
                    arrayList17 = arrayList3;
                    arrayList16 = arrayList;
                    arrayList15 = arrayList2;
                    str6 = str8;
                    format2 = format;
                    uri = null;
                }
                return new HlsMultivariantPlaylist(str, arrayList20, arrayList22, arrayList15, arrayList16, arrayList17, arrayList18, format2, z2 ? Collections.emptyList() : arrayList23, z3, hashMap2, arrayList21);
            }
            String next = lineIterator.next();
            if (next.startsWith("#EXT")) {
                arrayList14.add(next);
            }
            boolean startsWith = next.startsWith("#EXT-X-I-FRAME-STREAM-INF");
            boolean z4 = z;
            if (next.startsWith("#EXT-X-DEFINE")) {
                hashMap2.put(parseStringAttr(next, REGEX_NAME, hashMap2), parseStringAttr(next, REGEX_VALUE, hashMap2));
            } else if (next.equals("#EXT-X-INDEPENDENT-SEGMENTS")) {
                z = true;
            } else if (next.startsWith("#EXT-X-MEDIA")) {
                arrayList12.add(next);
            } else {
                if (next.startsWith("#EXT-X-SESSION-KEY")) {
                    DrmInitData.SchemeData parseDrmSchemeData = parseDrmSchemeData(next, parseOptionalStringAttr(next, REGEX_KEYFORMAT, "identity", hashMap2), hashMap2);
                    if (parseDrmSchemeData != null) {
                        arrayList4 = arrayList11;
                        arrayList13.add(new DrmInitData(parseEncryptionScheme(parseStringAttr(next, REGEX_METHOD, hashMap2)), parseDrmSchemeData));
                    }
                } else {
                    arrayList4 = arrayList11;
                    if (next.startsWith("#EXT-X-STREAM-INF") || startsWith) {
                        boolean contains = z2 | next.contains("CLOSED-CAPTIONS=NONE");
                        int i6 = startsWith ? LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : 0;
                        int parseIntAttr = parseIntAttr(next, REGEX_BANDWIDTH);
                        ArrayList arrayList25 = arrayList13;
                        int parseOptionalIntAttr = parseOptionalIntAttr(next, REGEX_AVERAGE_BANDWIDTH, -1);
                        String parseOptionalStringAttr3 = parseOptionalStringAttr(next, REGEX_CODECS, hashMap2);
                        ArrayList arrayList26 = arrayList14;
                        String parseOptionalStringAttr4 = parseOptionalStringAttr(next, REGEX_RESOLUTION, hashMap2);
                        ArrayList arrayList27 = arrayList10;
                        if (parseOptionalStringAttr4 != null) {
                            String[] split = Util.split(parseOptionalStringAttr4, "x");
                            i3 = Integer.parseInt(split[0]);
                            i2 = Integer.parseInt(split[1]);
                            if (i3 > 0 && i2 > 0) {
                                arrayList5 = arrayList9;
                                String parseOptionalStringAttr5 = parseOptionalStringAttr(next, REGEX_FRAME_RATE, hashMap2);
                                float parseFloat = parseOptionalStringAttr5 == null ? Float.parseFloat(parseOptionalStringAttr5) : -1.0f;
                                ArrayList arrayList28 = arrayList8;
                                String parseOptionalStringAttr6 = parseOptionalStringAttr(next, REGEX_VIDEO, hashMap2);
                                ArrayList arrayList29 = arrayList12;
                                String parseOptionalStringAttr7 = parseOptionalStringAttr(next, REGEX_AUDIO, hashMap2);
                                HashMap hashMap4 = hashMap;
                                String parseOptionalStringAttr8 = parseOptionalStringAttr(next, REGEX_SUBTITLES, hashMap2);
                                String parseOptionalStringAttr9 = parseOptionalStringAttr(next, REGEX_CLOSED_CAPTIONS, hashMap2);
                                if (!startsWith) {
                                    replaceVariableReferences = parseStringAttr(next, REGEX_URI, hashMap2);
                                } else {
                                    if (!lineIterator.hasNext()) {
                                        throw ParserException.createForMalformedManifest("#EXT-X-STREAM-INF must be followed by another line", null);
                                    }
                                    replaceVariableReferences = replaceVariableReferences(lineIterator.next(), hashMap2);
                                }
                                Uri resolveToUri2 = UriUtil.resolveToUri(str5, replaceVariableReferences);
                                arrayList7.add(new HlsMultivariantPlaylist.Variant(resolveToUri2, new Format.Builder().setId(arrayList7.size()).setContainerMimeType("application/x-mpegURL").setCodecs(parseOptionalStringAttr3).setAverageBitrate(parseOptionalIntAttr).setPeakBitrate(parseIntAttr).setWidth(i3).setHeight(i2).setFrameRate(parseFloat).setRoleFlags(i6).build(), parseOptionalStringAttr6, parseOptionalStringAttr7, parseOptionalStringAttr8, parseOptionalStringAttr9));
                                arrayList6 = (ArrayList) hashMap4.get(resolveToUri2);
                                if (arrayList6 == null) {
                                    arrayList6 = new ArrayList();
                                    hashMap4.put(resolveToUri2, arrayList6);
                                }
                                arrayList6.add(new HlsTrackMetadataEntry.VariantInfo(parseOptionalIntAttr, parseIntAttr, parseOptionalStringAttr6, parseOptionalStringAttr7, parseOptionalStringAttr8, parseOptionalStringAttr9));
                                hashMap = hashMap4;
                                z = z4;
                                arrayList11 = arrayList4;
                                z2 = contains;
                                arrayList13 = arrayList25;
                                arrayList14 = arrayList26;
                                arrayList10 = arrayList27;
                                arrayList9 = arrayList5;
                                arrayList8 = arrayList28;
                                arrayList12 = arrayList29;
                                str5 = str;
                            }
                        }
                        arrayList5 = arrayList9;
                        i2 = -1;
                        i3 = -1;
                        String parseOptionalStringAttr52 = parseOptionalStringAttr(next, REGEX_FRAME_RATE, hashMap2);
                        if (parseOptionalStringAttr52 == null) {
                        }
                        ArrayList arrayList282 = arrayList8;
                        String parseOptionalStringAttr62 = parseOptionalStringAttr(next, REGEX_VIDEO, hashMap2);
                        ArrayList arrayList292 = arrayList12;
                        String parseOptionalStringAttr72 = parseOptionalStringAttr(next, REGEX_AUDIO, hashMap2);
                        HashMap hashMap42 = hashMap;
                        String parseOptionalStringAttr82 = parseOptionalStringAttr(next, REGEX_SUBTITLES, hashMap2);
                        String parseOptionalStringAttr92 = parseOptionalStringAttr(next, REGEX_CLOSED_CAPTIONS, hashMap2);
                        if (!startsWith) {
                        }
                        Uri resolveToUri22 = UriUtil.resolveToUri(str5, replaceVariableReferences);
                        arrayList7.add(new HlsMultivariantPlaylist.Variant(resolveToUri22, new Format.Builder().setId(arrayList7.size()).setContainerMimeType("application/x-mpegURL").setCodecs(parseOptionalStringAttr3).setAverageBitrate(parseOptionalIntAttr).setPeakBitrate(parseIntAttr).setWidth(i3).setHeight(i2).setFrameRate(parseFloat).setRoleFlags(i6).build(), parseOptionalStringAttr62, parseOptionalStringAttr72, parseOptionalStringAttr82, parseOptionalStringAttr92));
                        arrayList6 = (ArrayList) hashMap42.get(resolveToUri22);
                        if (arrayList6 == null) {
                        }
                        arrayList6.add(new HlsTrackMetadataEntry.VariantInfo(parseOptionalIntAttr, parseIntAttr, parseOptionalStringAttr62, parseOptionalStringAttr72, parseOptionalStringAttr82, parseOptionalStringAttr92));
                        hashMap = hashMap42;
                        z = z4;
                        arrayList11 = arrayList4;
                        z2 = contains;
                        arrayList13 = arrayList25;
                        arrayList14 = arrayList26;
                        arrayList10 = arrayList27;
                        arrayList9 = arrayList5;
                        arrayList8 = arrayList282;
                        arrayList12 = arrayList292;
                        str5 = str;
                    }
                }
                z = z4;
                arrayList11 = arrayList4;
            }
            arrayList4 = arrayList11;
            z = z4;
            arrayList11 = arrayList4;
        }
    }

    private static boolean parseOptionalBooleanAttribute(String str, Pattern pattern, boolean z) {
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? "YES".equals(matcher.group(1)) : z;
    }

    private static double parseOptionalDoubleAttr(String str, Pattern pattern, double d) {
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Double.parseDouble((String) Assertions.checkNotNull(matcher.group(1))) : d;
    }

    private static int parseOptionalIntAttr(String str, Pattern pattern, int i) {
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Integer.parseInt((String) Assertions.checkNotNull(matcher.group(1))) : i;
    }

    private static long parseOptionalLongAttr(String str, Pattern pattern, long j) {
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Long.parseLong((String) Assertions.checkNotNull(matcher.group(1))) : j;
    }

    private static String parseOptionalStringAttr(String str, Pattern pattern, String str2, Map map) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            str2 = (String) Assertions.checkNotNull(matcher.group(1));
        }
        return (map.isEmpty() || str2 == null) ? str2 : replaceVariableReferences(str2, map);
    }

    private static String parseOptionalStringAttr(String str, Pattern pattern, Map map) {
        return parseOptionalStringAttr(str, pattern, null, map);
    }

    private static int parseRoleFlags(String str, Map map) {
        String parseOptionalStringAttr = parseOptionalStringAttr(str, REGEX_CHARACTERISTICS, map);
        if (TextUtils.isEmpty(parseOptionalStringAttr)) {
            return 0;
        }
        String[] split = Util.split(parseOptionalStringAttr, ",");
        int i = Util.contains(split, "public.accessibility.describes-video") ? 512 : 0;
        if (Util.contains(split, "public.accessibility.transcribes-spoken-dialog")) {
            i |= LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM;
        }
        if (Util.contains(split, "public.accessibility.describes-music-and-sound")) {
            i |= 1024;
        }
        return Util.contains(split, "public.easy-to-read") ? i | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : i;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [int] */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r0v7 */
    private static int parseSelectionFlags(String str) {
        boolean parseOptionalBooleanAttribute = parseOptionalBooleanAttribute(str, REGEX_DEFAULT, false);
        ?? r0 = parseOptionalBooleanAttribute;
        if (parseOptionalBooleanAttribute(str, REGEX_FORCED, false)) {
            r0 = (parseOptionalBooleanAttribute ? 1 : 0) | 2;
        }
        return parseOptionalBooleanAttribute(str, REGEX_AUTOSELECT, false) ? r0 | 4 : r0;
    }

    private static HlsMediaPlaylist.ServerControl parseServerControl(String str) {
        double parseOptionalDoubleAttr = parseOptionalDoubleAttr(str, REGEX_CAN_SKIP_UNTIL, -9.223372036854776E18d);
        long j = parseOptionalDoubleAttr == -9.223372036854776E18d ? -9223372036854775807L : (long) (parseOptionalDoubleAttr * 1000000.0d);
        boolean parseOptionalBooleanAttribute = parseOptionalBooleanAttribute(str, REGEX_CAN_SKIP_DATE_RANGES, false);
        double parseOptionalDoubleAttr2 = parseOptionalDoubleAttr(str, REGEX_HOLD_BACK, -9.223372036854776E18d);
        long j2 = parseOptionalDoubleAttr2 == -9.223372036854776E18d ? -9223372036854775807L : (long) (parseOptionalDoubleAttr2 * 1000000.0d);
        double parseOptionalDoubleAttr3 = parseOptionalDoubleAttr(str, REGEX_PART_HOLD_BACK, -9.223372036854776E18d);
        return new HlsMediaPlaylist.ServerControl(j, parseOptionalBooleanAttribute, j2, parseOptionalDoubleAttr3 != -9.223372036854776E18d ? (long) (parseOptionalDoubleAttr3 * 1000000.0d) : -9223372036854775807L, parseOptionalBooleanAttribute(str, REGEX_CAN_BLOCK_RELOAD, false));
    }

    private static String parseStringAttr(String str, Pattern pattern, Map map) {
        String parseOptionalStringAttr = parseOptionalStringAttr(str, pattern, map);
        if (parseOptionalStringAttr != null) {
            return parseOptionalStringAttr;
        }
        throw ParserException.createForMalformedManifest("Couldn't match " + pattern.pattern() + " in " + str, null);
    }

    private static long parseTimeSecondsToUs(String str, Pattern pattern) {
        return new BigDecimal(parseStringAttr(str, pattern, Collections.emptyMap())).multiply(new BigDecimal(1000000L)).longValue();
    }

    private static String replaceVariableReferences(String str, Map map) {
        Matcher matcher = REGEX_VARIABLE_REFERENCE.matcher(str);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String group = matcher.group(1);
            if (map.containsKey(group)) {
                matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement((String) map.get(group)));
            }
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    private static int skipIgnorableWhitespace(BufferedReader bufferedReader, boolean z, int i) {
        while (i != -1 && Character.isWhitespace(i) && (z || !Util.isLinebreak(i))) {
            i = bufferedReader.read();
        }
        return i;
    }

    @Override // com.google.android.exoplayer2.upstream.ParsingLoadable.Parser
    public HlsPlaylist parse(Uri uri, InputStream inputStream) {
        String trim;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        ArrayDeque arrayDeque = new ArrayDeque();
        try {
            if (!checkPlaylistHeader(bufferedReader)) {
                throw ParserException.createForMalformedManifest("Input does not start with the #EXTM3U header.", null);
            }
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    Util.closeQuietly(bufferedReader);
                    throw ParserException.createForMalformedManifest("Failed to parse the playlist, could not identify any tags.", null);
                }
                trim = readLine.trim();
                if (!trim.isEmpty()) {
                    if (!trim.startsWith("#EXT-X-STREAM-INF")) {
                        if (trim.startsWith("#EXT-X-TARGETDURATION") || trim.startsWith("#EXT-X-MEDIA-SEQUENCE") || trim.startsWith("#EXTINF") || trim.startsWith("#EXT-X-KEY") || trim.startsWith("#EXT-X-BYTERANGE") || trim.equals("#EXT-X-DISCONTINUITY") || trim.equals("#EXT-X-DISCONTINUITY-SEQUENCE") || trim.equals("#EXT-X-ENDLIST")) {
                            break;
                        }
                        arrayDeque.add(trim);
                    } else {
                        arrayDeque.add(trim);
                        return parseMultivariantPlaylist(new LineIterator(arrayDeque, bufferedReader), uri.toString());
                    }
                }
            }
            arrayDeque.add(trim);
            return parseMediaPlaylist(this.multivariantPlaylist, this.previousMediaPlaylist, new LineIterator(arrayDeque, bufferedReader), uri.toString());
        } finally {
            Util.closeQuietly(bufferedReader);
        }
    }
}
