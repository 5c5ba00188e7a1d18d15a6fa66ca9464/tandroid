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
public final class HlsPlaylistParser implements ParsingLoadable.Parser<HlsPlaylist> {
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

    public HlsPlaylistParser() {
        this(HlsMultivariantPlaylist.EMPTY, null);
    }

    public HlsPlaylistParser(HlsMultivariantPlaylist hlsMultivariantPlaylist, HlsMediaPlaylist hlsMediaPlaylist) {
        this.multivariantPlaylist = hlsMultivariantPlaylist;
        this.previousMediaPlaylist = hlsMediaPlaylist;
    }

    @Override // com.google.android.exoplayer2.upstream.ParsingLoadable.Parser
    public HlsPlaylist parse(Uri uri, InputStream inputStream) throws IOException {
        String trim;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        ArrayDeque arrayDeque = new ArrayDeque();
        try {
            if (!checkPlaylistHeader(bufferedReader)) {
                throw ParserException.createForMalformedManifest("Input does not start with the #EXTM3U header.", null);
            }
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    trim = readLine.trim();
                    if (!trim.isEmpty()) {
                        if (trim.startsWith("#EXT-X-STREAM-INF")) {
                            arrayDeque.add(trim);
                            return parseMultivariantPlaylist(new LineIterator(arrayDeque, bufferedReader), uri.toString());
                        } else if (trim.startsWith("#EXT-X-TARGETDURATION") || trim.startsWith("#EXT-X-MEDIA-SEQUENCE") || trim.startsWith("#EXTINF") || trim.startsWith("#EXT-X-KEY") || trim.startsWith("#EXT-X-BYTERANGE") || trim.equals("#EXT-X-DISCONTINUITY") || trim.equals("#EXT-X-DISCONTINUITY-SEQUENCE") || trim.equals("#EXT-X-ENDLIST")) {
                            break;
                        } else {
                            arrayDeque.add(trim);
                        }
                    }
                } else {
                    Util.closeQuietly(bufferedReader);
                    throw ParserException.createForMalformedManifest("Failed to parse the playlist, could not identify any tags.", null);
                }
            }
            arrayDeque.add(trim);
            return parseMediaPlaylist(this.multivariantPlaylist, this.previousMediaPlaylist, new LineIterator(arrayDeque, bufferedReader), uri.toString());
        } finally {
            Util.closeQuietly(bufferedReader);
        }
    }

    private static boolean checkPlaylistHeader(BufferedReader bufferedReader) throws IOException {
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

    private static int skipIgnorableWhitespace(BufferedReader bufferedReader, boolean z, int i) throws IOException {
        while (i != -1 && Character.isWhitespace(i) && (z || !Util.isLinebreak(i))) {
            i = bufferedReader.read();
        }
        return i;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v6, types: [java.util.List] */
    private static HlsMultivariantPlaylist parseMultivariantPlaylist(LineIterator lineIterator, String str) throws IOException {
        char c;
        Format format;
        ArrayList arrayList;
        ArrayList arrayList2;
        String str2;
        ArrayList arrayList3;
        int parseInt;
        String str3;
        String str4;
        boolean z;
        int i;
        ArrayList arrayList4;
        ArrayList arrayList5;
        ArrayList arrayList6;
        ArrayList arrayList7;
        int i2;
        int i3;
        ArrayList arrayList8;
        ArrayList arrayList9;
        float f;
        ArrayList arrayList10;
        Uri resolveToUri;
        HashMap hashMap;
        int i4;
        String str5 = str;
        HashMap hashMap2 = new HashMap();
        HashMap hashMap3 = new HashMap();
        ArrayList arrayList11 = new ArrayList();
        ArrayList arrayList12 = new ArrayList();
        ArrayList arrayList13 = new ArrayList();
        ArrayList arrayList14 = new ArrayList();
        ArrayList arrayList15 = new ArrayList();
        ArrayList arrayList16 = new ArrayList();
        ArrayList arrayList17 = new ArrayList();
        ArrayList arrayList18 = new ArrayList();
        boolean z2 = false;
        boolean z3 = false;
        while (true) {
            String str6 = "application/x-mpegURL";
            if (lineIterator.hasNext()) {
                String next = lineIterator.next();
                if (next.startsWith("#EXT")) {
                    arrayList18.add(next);
                }
                boolean startsWith = next.startsWith("#EXT-X-I-FRAME-STREAM-INF");
                boolean z4 = z2;
                if (next.startsWith("#EXT-X-DEFINE")) {
                    hashMap3.put(parseStringAttr(next, REGEX_NAME, hashMap3), parseStringAttr(next, REGEX_VALUE, hashMap3));
                } else {
                    if (next.equals("#EXT-X-INDEPENDENT-SEGMENTS")) {
                        hashMap = hashMap2;
                        arrayList10 = arrayList16;
                        arrayList9 = arrayList12;
                        arrayList8 = arrayList13;
                        arrayList7 = arrayList14;
                        arrayList5 = arrayList15;
                        arrayList6 = arrayList18;
                        arrayList4 = arrayList17;
                        z2 = true;
                    } else if (next.startsWith("#EXT-X-MEDIA")) {
                        arrayList16.add(next);
                    } else if (next.startsWith("#EXT-X-SESSION-KEY")) {
                        DrmInitData.SchemeData parseDrmSchemeData = parseDrmSchemeData(next, parseOptionalStringAttr(next, REGEX_KEYFORMAT, "identity", hashMap3), hashMap3);
                        if (parseDrmSchemeData != null) {
                            arrayList17.add(new DrmInitData(parseEncryptionScheme(parseStringAttr(next, REGEX_METHOD, hashMap3)), parseDrmSchemeData));
                        }
                    } else if (next.startsWith("#EXT-X-STREAM-INF") || startsWith) {
                        boolean contains = z3 | next.contains("CLOSED-CAPTIONS=NONE");
                        if (startsWith) {
                            i = 16384;
                            z = contains;
                        } else {
                            z = contains;
                            i = 0;
                        }
                        int parseIntAttr = parseIntAttr(next, REGEX_BANDWIDTH);
                        arrayList4 = arrayList17;
                        arrayList5 = arrayList15;
                        int parseOptionalIntAttr = parseOptionalIntAttr(next, REGEX_AVERAGE_BANDWIDTH, -1);
                        String parseOptionalStringAttr = parseOptionalStringAttr(next, REGEX_CODECS, hashMap3);
                        arrayList6 = arrayList18;
                        String parseOptionalStringAttr2 = parseOptionalStringAttr(next, REGEX_RESOLUTION, hashMap3);
                        if (parseOptionalStringAttr2 != null) {
                            arrayList7 = arrayList14;
                            String[] split = Util.split(parseOptionalStringAttr2, "x");
                            int parseInt2 = Integer.parseInt(split[0]);
                            int parseInt3 = Integer.parseInt(split[1]);
                            if (parseInt2 <= 0 || parseInt3 <= 0) {
                                parseInt3 = -1;
                                i4 = -1;
                            } else {
                                i4 = parseInt2;
                            }
                            i3 = parseInt3;
                            i2 = i4;
                        } else {
                            arrayList7 = arrayList14;
                            i2 = -1;
                            i3 = -1;
                        }
                        arrayList8 = arrayList13;
                        String parseOptionalStringAttr3 = parseOptionalStringAttr(next, REGEX_FRAME_RATE, hashMap3);
                        if (parseOptionalStringAttr3 != null) {
                            arrayList9 = arrayList12;
                            f = Float.parseFloat(parseOptionalStringAttr3);
                        } else {
                            arrayList9 = arrayList12;
                            f = -1.0f;
                        }
                        String parseOptionalStringAttr4 = parseOptionalStringAttr(next, REGEX_VIDEO, hashMap3);
                        arrayList10 = arrayList16;
                        String parseOptionalStringAttr5 = parseOptionalStringAttr(next, REGEX_AUDIO, hashMap3);
                        HashMap hashMap4 = hashMap2;
                        String parseOptionalStringAttr6 = parseOptionalStringAttr(next, REGEX_SUBTITLES, hashMap3);
                        String parseOptionalStringAttr7 = parseOptionalStringAttr(next, REGEX_CLOSED_CAPTIONS, hashMap3);
                        if (startsWith) {
                            resolveToUri = UriUtil.resolveToUri(str5, parseStringAttr(next, REGEX_URI, hashMap3));
                        } else if (!lineIterator.hasNext()) {
                            throw ParserException.createForMalformedManifest("#EXT-X-STREAM-INF must be followed by another line", null);
                        } else {
                            resolveToUri = UriUtil.resolveToUri(str5, replaceVariableReferences(lineIterator.next(), hashMap3));
                        }
                        arrayList11.add(new HlsMultivariantPlaylist.Variant(resolveToUri, new Format.Builder().setId(arrayList11.size()).setContainerMimeType("application/x-mpegURL").setCodecs(parseOptionalStringAttr).setAverageBitrate(parseOptionalIntAttr).setPeakBitrate(parseIntAttr).setWidth(i2).setHeight(i3).setFrameRate(f).setRoleFlags(i).build(), parseOptionalStringAttr4, parseOptionalStringAttr5, parseOptionalStringAttr6, parseOptionalStringAttr7));
                        hashMap = hashMap4;
                        ArrayList arrayList19 = (ArrayList) hashMap.get(resolveToUri);
                        if (arrayList19 == null) {
                            arrayList19 = new ArrayList();
                            hashMap.put(resolveToUri, arrayList19);
                        }
                        arrayList19.add(new HlsTrackMetadataEntry.VariantInfo(parseOptionalIntAttr, parseIntAttr, parseOptionalStringAttr4, parseOptionalStringAttr5, parseOptionalStringAttr6, parseOptionalStringAttr7));
                        z2 = z4;
                        z3 = z;
                    }
                    hashMap2 = hashMap;
                    arrayList17 = arrayList4;
                    arrayList15 = arrayList5;
                    arrayList18 = arrayList6;
                    arrayList14 = arrayList7;
                    arrayList13 = arrayList8;
                    arrayList12 = arrayList9;
                    arrayList16 = arrayList10;
                    str5 = str;
                }
                hashMap = hashMap2;
                arrayList10 = arrayList16;
                arrayList9 = arrayList12;
                arrayList8 = arrayList13;
                arrayList7 = arrayList14;
                arrayList5 = arrayList15;
                arrayList6 = arrayList18;
                arrayList4 = arrayList17;
                z2 = z4;
                hashMap2 = hashMap;
                arrayList17 = arrayList4;
                arrayList15 = arrayList5;
                arrayList18 = arrayList6;
                arrayList14 = arrayList7;
                arrayList13 = arrayList8;
                arrayList12 = arrayList9;
                arrayList16 = arrayList10;
                str5 = str;
            } else {
                HashMap hashMap5 = hashMap2;
                ArrayList arrayList20 = arrayList16;
                ArrayList arrayList21 = arrayList12;
                ArrayList arrayList22 = arrayList13;
                ArrayList arrayList23 = arrayList14;
                ArrayList arrayList24 = arrayList15;
                ArrayList arrayList25 = arrayList18;
                boolean z5 = z2;
                ArrayList arrayList26 = arrayList17;
                ArrayList arrayList27 = new ArrayList();
                HashSet hashSet = new HashSet();
                for (int i5 = 0; i5 < arrayList11.size(); i5++) {
                    HlsMultivariantPlaylist.Variant variant = (HlsMultivariantPlaylist.Variant) arrayList11.get(i5);
                    if (hashSet.add(variant.url)) {
                        Assertions.checkState(variant.format.metadata == null);
                        arrayList27.add(variant.copyWithFormat(variant.format.buildUpon().setMetadata(new Metadata(new HlsTrackMetadataEntry(null, null, (List) Assertions.checkNotNull((ArrayList) hashMap5.get(variant.url))))).build()));
                    }
                }
                Uri uri = null;
                ArrayList arrayList28 = null;
                Format format2 = null;
                int i6 = 0;
                while (i6 < arrayList20.size()) {
                    ArrayList arrayList29 = arrayList20;
                    String str7 = (String) arrayList29.get(i6);
                    String parseStringAttr = parseStringAttr(str7, REGEX_GROUP_ID, hashMap3);
                    String parseStringAttr2 = parseStringAttr(str7, REGEX_NAME, hashMap3);
                    Format.Builder language = new Format.Builder().setId(parseStringAttr + ":" + parseStringAttr2).setLabel(parseStringAttr2).setContainerMimeType(str6).setSelectionFlags(parseSelectionFlags(str7)).setRoleFlags(parseRoleFlags(str7, hashMap3)).setLanguage(parseOptionalStringAttr(str7, REGEX_LANGUAGE, hashMap3));
                    String parseOptionalStringAttr8 = parseOptionalStringAttr(str7, REGEX_URI, hashMap3);
                    Uri resolveToUri2 = parseOptionalStringAttr8 == null ? uri : UriUtil.resolveToUri(str, parseOptionalStringAttr8);
                    arrayList20 = arrayList29;
                    String str8 = str6;
                    Metadata metadata = new Metadata(new HlsTrackMetadataEntry(parseStringAttr, parseStringAttr2, Collections.emptyList()));
                    String parseStringAttr3 = parseStringAttr(str7, REGEX_TYPE, hashMap3);
                    parseStringAttr3.hashCode();
                    switch (parseStringAttr3.hashCode()) {
                        case -959297733:
                            if (parseStringAttr3.equals("SUBTITLES")) {
                                c = 0;
                                break;
                            }
                            c = 65535;
                            break;
                        case -333210994:
                            if (parseStringAttr3.equals("CLOSED-CAPTIONS")) {
                                c = 1;
                                break;
                            }
                            c = 65535;
                            break;
                        case 62628790:
                            if (parseStringAttr3.equals("AUDIO")) {
                                c = 2;
                                break;
                            }
                            c = 65535;
                            break;
                        case 81665115:
                            if (parseStringAttr3.equals("VIDEO")) {
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
                            format = format2;
                            arrayList = arrayList22;
                            arrayList2 = arrayList21;
                            HlsMultivariantPlaylist.Variant variantWithSubtitleGroup = getVariantWithSubtitleGroup(arrayList11, parseStringAttr);
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
                            if (resolveToUri2 != null) {
                                HlsMultivariantPlaylist.Rendition rendition = new HlsMultivariantPlaylist.Rendition(resolveToUri2, language.build(), parseStringAttr, parseStringAttr2);
                                arrayList3 = arrayList23;
                                arrayList3.add(rendition);
                                break;
                            } else {
                                arrayList3 = arrayList23;
                                Log.w("HlsPlaylistParser", "EXT-X-MEDIA tag with missing mandatory URI attribute: skipping");
                                break;
                            }
                        case 1:
                            format = format2;
                            arrayList = arrayList22;
                            arrayList2 = arrayList21;
                            String parseStringAttr4 = parseStringAttr(str7, REGEX_INSTREAM_ID, hashMap3);
                            if (parseStringAttr4.startsWith("CC")) {
                                parseInt = Integer.parseInt(parseStringAttr4.substring(2));
                                str3 = "application/cea-608";
                            } else {
                                parseInt = Integer.parseInt(parseStringAttr4.substring(7));
                                str3 = "application/cea-708";
                            }
                            if (arrayList28 == null) {
                                arrayList28 = new ArrayList();
                            }
                            language.setSampleMimeType(str3).setAccessibilityChannel(parseInt);
                            arrayList28.add(language.build());
                            arrayList3 = arrayList23;
                            break;
                        case 2:
                            arrayList2 = arrayList21;
                            HlsMultivariantPlaylist.Variant variantWithAudioGroup = getVariantWithAudioGroup(arrayList11, parseStringAttr);
                            if (variantWithAudioGroup != null) {
                                format = format2;
                                String codecsOfType2 = Util.getCodecsOfType(variantWithAudioGroup.format.codecs, 1);
                                language.setCodecs(codecsOfType2);
                                str4 = MimeTypes.getMediaMimeType(codecsOfType2);
                            } else {
                                format = format2;
                                str4 = null;
                            }
                            String parseOptionalStringAttr9 = parseOptionalStringAttr(str7, REGEX_CHANNELS, hashMap3);
                            if (parseOptionalStringAttr9 != null) {
                                language.setChannelCount(Integer.parseInt(Util.splitAtFirst(parseOptionalStringAttr9, "/")[0]));
                                if ("audio/eac3".equals(str4) && parseOptionalStringAttr9.endsWith("/JOC")) {
                                    language.setCodecs("ec+3");
                                    str4 = "audio/eac3-joc";
                                }
                            }
                            language.setSampleMimeType(str4);
                            if (resolveToUri2 != null) {
                                language.setMetadata(metadata);
                                arrayList = arrayList22;
                                arrayList.add(new HlsMultivariantPlaylist.Rendition(resolveToUri2, language.build(), parseStringAttr, parseStringAttr2));
                            } else {
                                arrayList = arrayList22;
                                if (variantWithAudioGroup != null) {
                                    format = language.build();
                                }
                            }
                            arrayList3 = arrayList23;
                            break;
                        case 3:
                            HlsMultivariantPlaylist.Variant variantWithVideoGroup = getVariantWithVideoGroup(arrayList11, parseStringAttr);
                            if (variantWithVideoGroup != null) {
                                Format format3 = variantWithVideoGroup.format;
                                String codecsOfType3 = Util.getCodecsOfType(format3.codecs, 2);
                                language.setCodecs(codecsOfType3).setSampleMimeType(MimeTypes.getMediaMimeType(codecsOfType3)).setWidth(format3.width).setHeight(format3.height).setFrameRate(format3.frameRate);
                            }
                            if (resolveToUri2 != null) {
                                language.setMetadata(metadata);
                                arrayList2 = arrayList21;
                                arrayList2.add(new HlsMultivariantPlaylist.Rendition(resolveToUri2, language.build(), parseStringAttr, parseStringAttr2));
                                format = format2;
                                arrayList3 = arrayList23;
                                arrayList = arrayList22;
                                break;
                            }
                        default:
                            format = format2;
                            arrayList3 = arrayList23;
                            arrayList = arrayList22;
                            arrayList2 = arrayList21;
                            break;
                    }
                    i6++;
                    arrayList23 = arrayList3;
                    arrayList22 = arrayList;
                    arrayList21 = arrayList2;
                    str6 = str8;
                    format2 = format;
                    uri = null;
                }
                return new HlsMultivariantPlaylist(str, arrayList25, arrayList27, arrayList21, arrayList22, arrayList23, arrayList24, format2, z3 ? Collections.emptyList() : arrayList28, z5, hashMap3, arrayList26);
            }
        }
    }

    private static HlsMultivariantPlaylist.Variant getVariantWithAudioGroup(ArrayList<HlsMultivariantPlaylist.Variant> arrayList, String str) {
        for (int i = 0; i < arrayList.size(); i++) {
            HlsMultivariantPlaylist.Variant variant = arrayList.get(i);
            if (str.equals(variant.audioGroupId)) {
                return variant;
            }
        }
        return null;
    }

    private static HlsMultivariantPlaylist.Variant getVariantWithVideoGroup(ArrayList<HlsMultivariantPlaylist.Variant> arrayList, String str) {
        for (int i = 0; i < arrayList.size(); i++) {
            HlsMultivariantPlaylist.Variant variant = arrayList.get(i);
            if (str.equals(variant.videoGroupId)) {
                return variant;
            }
        }
        return null;
    }

    private static HlsMultivariantPlaylist.Variant getVariantWithSubtitleGroup(ArrayList<HlsMultivariantPlaylist.Variant> arrayList, String str) {
        for (int i = 0; i < arrayList.size(); i++) {
            HlsMultivariantPlaylist.Variant variant = arrayList.get(i);
            if (str.equals(variant.subtitleGroupId)) {
                return variant;
            }
        }
        return null;
    }

    private static HlsMediaPlaylist parseMediaPlaylist(HlsMultivariantPlaylist hlsMultivariantPlaylist, HlsMediaPlaylist hlsMediaPlaylist, LineIterator lineIterator, String str) throws IOException {
        ArrayList arrayList;
        int i;
        ArrayList arrayList2;
        String str2;
        String str3;
        long j;
        HlsMediaPlaylist.Part part;
        ArrayList arrayList3;
        ArrayList arrayList4;
        ArrayList arrayList5;
        long j2;
        long j3;
        DrmInitData drmInitData;
        long j4;
        String str4;
        HlsMultivariantPlaylist hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
        HlsMediaPlaylist hlsMediaPlaylist2 = hlsMediaPlaylist;
        boolean z = hlsMultivariantPlaylist2.hasIndependentSegments;
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        ArrayList arrayList6 = new ArrayList();
        ArrayList arrayList7 = new ArrayList();
        ArrayList arrayList8 = new ArrayList();
        ArrayList arrayList9 = new ArrayList();
        HlsMediaPlaylist.ServerControl serverControl = new HlsMediaPlaylist.ServerControl(-9223372036854775807L, false, -9223372036854775807L, -9223372036854775807L, false);
        TreeMap treeMap = new TreeMap();
        String str5 = "";
        boolean z2 = false;
        boolean z3 = z;
        HlsMediaPlaylist.ServerControl serverControl2 = serverControl;
        String str6 = "";
        long j5 = 0;
        long j6 = 0;
        long j7 = 0;
        long j8 = 0;
        long j9 = 0;
        long j10 = 0;
        long j11 = 0;
        long j12 = 0;
        int i2 = 0;
        long j13 = -9223372036854775807L;
        boolean z4 = false;
        boolean z5 = false;
        int i3 = 0;
        int i4 = 1;
        long j14 = -9223372036854775807L;
        long j15 = -9223372036854775807L;
        boolean z6 = false;
        DrmInitData drmInitData2 = null;
        DrmInitData drmInitData3 = null;
        boolean z7 = false;
        String str7 = null;
        long j16 = -1;
        String str8 = null;
        String str9 = null;
        int i5 = 0;
        boolean z8 = false;
        HlsMediaPlaylist.Segment segment = null;
        ArrayList arrayList10 = arrayList7;
        HlsMediaPlaylist.Part part2 = null;
        while (lineIterator.hasNext()) {
            String next = lineIterator.next();
            if (next.startsWith("#EXT")) {
                arrayList9.add(next);
            }
            if (next.startsWith("#EXT-X-PLAYLIST-TYPE")) {
                String parseStringAttr = parseStringAttr(next, REGEX_PLAYLIST_TYPE, hashMap);
                if ("VOD".equals(parseStringAttr)) {
                    i2 = 1;
                } else if ("EVENT".equals(parseStringAttr)) {
                    i2 = 2;
                }
            } else if (next.equals("#EXT-X-I-FRAMES-ONLY")) {
                z8 = true;
            } else {
                if (next.startsWith("#EXT-X-START")) {
                    arrayList = arrayList6;
                    z4 = parseOptionalBooleanAttribute(next, REGEX_PRECISE, z2);
                    j13 = (long) (parseDoubleAttr(next, REGEX_TIME_OFFSET) * 1000000.0d);
                } else {
                    arrayList = arrayList6;
                    if (next.startsWith("#EXT-X-SERVER-CONTROL")) {
                        serverControl2 = parseServerControl(next);
                    } else if (next.startsWith("#EXT-X-PART-INF")) {
                        j15 = (long) (parseDoubleAttr(next, REGEX_PART_TARGET_DURATION) * 1000000.0d);
                    } else if (next.startsWith("#EXT-X-MAP")) {
                        String parseStringAttr2 = parseStringAttr(next, REGEX_URI, hashMap);
                        String parseOptionalStringAttr = parseOptionalStringAttr(next, REGEX_ATTR_BYTERANGE, hashMap);
                        if (parseOptionalStringAttr != null) {
                            String[] split = Util.split(parseOptionalStringAttr, "@");
                            j16 = Long.parseLong(split[z2 ? 1 : 0]);
                            if (split.length > 1) {
                                j7 = Long.parseLong(split[1]);
                            }
                        }
                        if (j16 == -1) {
                            j7 = 0;
                        }
                        String str10 = str7;
                        String str11 = str8;
                        if (str10 != null && str11 == null) {
                            throw ParserException.createForMalformedManifest("The encryption IV attribute must be present when an initialization segment is encrypted with METHOD=AES-128.", null);
                        }
                        segment = new HlsMediaPlaylist.Segment(parseStringAttr2, j7, j16, str10, str11);
                        if (j16 != -1) {
                            j7 += j16;
                        }
                        str8 = str11;
                        str7 = str10;
                        arrayList6 = arrayList;
                        j16 = -1;
                    } else {
                        String str12 = str7;
                        String str13 = str8;
                        if (next.startsWith("#EXT-X-TARGETDURATION")) {
                            j14 = 1000000 * parseIntAttr(next, REGEX_TARGET_DURATION);
                        } else if (next.startsWith("#EXT-X-MEDIA-SEQUENCE")) {
                            j10 = parseLongAttr(next, REGEX_MEDIA_SEQUENCE);
                            str8 = str13;
                            str7 = str12;
                            j6 = j10;
                            arrayList6 = arrayList;
                            z2 = false;
                        } else if (next.startsWith("#EXT-X-VERSION")) {
                            i4 = parseIntAttr(next, REGEX_VERSION);
                        } else {
                            if (next.startsWith("#EXT-X-DEFINE")) {
                                String parseOptionalStringAttr2 = parseOptionalStringAttr(next, REGEX_IMPORT, hashMap);
                                if (parseOptionalStringAttr2 != null) {
                                    String str14 = hlsMultivariantPlaylist2.variableDefinitions.get(parseOptionalStringAttr2);
                                    if (str14 != null) {
                                        hashMap.put(parseOptionalStringAttr2, str14);
                                    }
                                } else {
                                    hashMap.put(parseStringAttr(next, REGEX_NAME, hashMap), parseStringAttr(next, REGEX_VALUE, hashMap));
                                }
                                i = i2;
                                arrayList2 = arrayList8;
                                str2 = str5;
                                str3 = str9;
                                j = j10;
                                part = part2;
                                arrayList3 = arrayList9;
                                arrayList4 = arrayList;
                            } else if (next.startsWith("#EXTINF")) {
                                j11 = parseTimeSecondsToUs(next, REGEX_MEDIA_DURATION);
                                str6 = parseOptionalStringAttr(next, REGEX_MEDIA_TITLE, str5, hashMap);
                            } else if (next.startsWith("#EXT-X-SKIP")) {
                                int parseIntAttr = parseIntAttr(next, REGEX_SKIPPED_SEGMENTS);
                                Assertions.checkState(hlsMediaPlaylist2 != null && arrayList.isEmpty());
                                int i6 = (int) (j6 - ((HlsMediaPlaylist) Util.castNonNull(hlsMediaPlaylist)).mediaSequence);
                                int i7 = parseIntAttr + i6;
                                if (i6 < 0 || i7 > hlsMediaPlaylist2.segments.size()) {
                                    throw new DeltaUpdateException();
                                }
                                String str15 = str5;
                                str8 = str13;
                                long j17 = j9;
                                while (i6 < i7) {
                                    HlsMediaPlaylist.Segment segment2 = hlsMediaPlaylist2.segments.get(i6);
                                    int i8 = i7;
                                    String str16 = str15;
                                    if (j6 != hlsMediaPlaylist2.mediaSequence) {
                                        segment2 = segment2.copyWith(j17, (hlsMediaPlaylist2.discontinuitySequence - i3) + segment2.relativeDiscontinuitySequence);
                                    }
                                    ArrayList arrayList11 = arrayList;
                                    arrayList11.add(segment2);
                                    long j18 = j17 + segment2.durationUs;
                                    long j19 = segment2.byteRangeLength;
                                    if (j19 != -1) {
                                        j7 = segment2.byteRangeOffset + j19;
                                    }
                                    int i9 = segment2.relativeDiscontinuitySequence;
                                    HlsMediaPlaylist.Segment segment3 = segment2.initializationSegment;
                                    DrmInitData drmInitData4 = segment2.drmInitData;
                                    String str17 = segment2.fullSegmentEncryptionKeyUri;
                                    String str18 = segment2.encryptionIV;
                                    if (str18 == null || !str18.equals(Long.toHexString(j10))) {
                                        str8 = segment2.encryptionIV;
                                    }
                                    j10++;
                                    i6++;
                                    segment = segment3;
                                    str12 = str17;
                                    arrayList = arrayList11;
                                    i5 = i9;
                                    i7 = i8;
                                    j8 = j18;
                                    str15 = str16;
                                    hlsMediaPlaylist2 = hlsMediaPlaylist;
                                    drmInitData3 = drmInitData4;
                                    j17 = j8;
                                }
                                hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                hlsMediaPlaylist2 = hlsMediaPlaylist;
                                j9 = j17;
                                str5 = str15;
                                str7 = str12;
                                arrayList6 = arrayList;
                                z2 = false;
                            } else {
                                str2 = str5;
                                if (next.startsWith("#EXT-X-KEY")) {
                                    String parseStringAttr3 = parseStringAttr(next, REGEX_METHOD, hashMap);
                                    String parseOptionalStringAttr3 = parseOptionalStringAttr(next, REGEX_KEYFORMAT, "identity", hashMap);
                                    if ("NONE".equals(parseStringAttr3)) {
                                        treeMap.clear();
                                        str4 = null;
                                        drmInitData3 = null;
                                        str8 = null;
                                    } else {
                                        String parseOptionalStringAttr4 = parseOptionalStringAttr(next, REGEX_IV, hashMap);
                                        if ("identity".equals(parseOptionalStringAttr3)) {
                                            if ("AES-128".equals(parseStringAttr3)) {
                                                str4 = parseStringAttr(next, REGEX_URI, hashMap);
                                                str8 = parseOptionalStringAttr4;
                                            }
                                            str8 = parseOptionalStringAttr4;
                                            str4 = null;
                                        } else {
                                            String str19 = str9;
                                            str9 = str19 == null ? parseEncryptionScheme(parseStringAttr3) : str19;
                                            DrmInitData.SchemeData parseDrmSchemeData = parseDrmSchemeData(next, parseOptionalStringAttr3, hashMap);
                                            if (parseDrmSchemeData != null) {
                                                treeMap.put(parseOptionalStringAttr3, parseDrmSchemeData);
                                                str8 = parseOptionalStringAttr4;
                                                str4 = null;
                                                drmInitData3 = null;
                                            }
                                            str8 = parseOptionalStringAttr4;
                                            str4 = null;
                                        }
                                    }
                                    hlsMediaPlaylist2 = hlsMediaPlaylist;
                                    str7 = str4;
                                    arrayList6 = arrayList;
                                    str5 = str2;
                                    z2 = false;
                                    hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                } else {
                                    str3 = str9;
                                    if (next.startsWith("#EXT-X-BYTERANGE")) {
                                        String[] split2 = Util.split(parseStringAttr(next, REGEX_BYTERANGE, hashMap), "@");
                                        j16 = Long.parseLong(split2[0]);
                                        if (split2.length > 1) {
                                            j7 = Long.parseLong(split2[1]);
                                        }
                                    } else {
                                        if (next.startsWith("#EXT-X-DISCONTINUITY-SEQUENCE")) {
                                            i3 = Integer.parseInt(next.substring(next.indexOf(58) + 1));
                                            hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                            hlsMediaPlaylist2 = hlsMediaPlaylist;
                                            str9 = str3;
                                            str8 = str13;
                                            str7 = str12;
                                            str5 = str2;
                                            z5 = true;
                                        } else if (next.equals("#EXT-X-DISCONTINUITY")) {
                                            i5++;
                                        } else {
                                            if (next.startsWith("#EXT-X-PROGRAM-DATE-TIME")) {
                                                if (j5 == 0) {
                                                    j5 = Util.msToUs(Util.parseXsDateTime(next.substring(next.indexOf(58) + 1))) - j9;
                                                } else {
                                                    i = i2;
                                                    arrayList2 = arrayList8;
                                                    arrayList3 = arrayList9;
                                                    arrayList4 = arrayList;
                                                    j = j10;
                                                }
                                            } else if (next.equals("#EXT-X-GAP")) {
                                                hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                                hlsMediaPlaylist2 = hlsMediaPlaylist;
                                                str9 = str3;
                                                str8 = str13;
                                                str7 = str12;
                                                str5 = str2;
                                                z7 = true;
                                            } else if (next.equals("#EXT-X-INDEPENDENT-SEGMENTS")) {
                                                hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                                hlsMediaPlaylist2 = hlsMediaPlaylist;
                                                str9 = str3;
                                                str8 = str13;
                                                str7 = str12;
                                                str5 = str2;
                                                z3 = true;
                                            } else if (next.equals("#EXT-X-ENDLIST")) {
                                                hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                                hlsMediaPlaylist2 = hlsMediaPlaylist;
                                                str9 = str3;
                                                str8 = str13;
                                                str7 = str12;
                                                str5 = str2;
                                                z6 = true;
                                            } else {
                                                if (next.startsWith("#EXT-X-RENDITION-REPORT")) {
                                                    i = i2;
                                                    arrayList5 = arrayList;
                                                    arrayList8.add(new HlsMediaPlaylist.RenditionReport(Uri.parse(UriUtil.resolve(str, parseStringAttr(next, REGEX_URI, hashMap))), parseOptionalLongAttr(next, REGEX_LAST_MSN, -1L), parseOptionalIntAttr(next, REGEX_LAST_PART, -1)));
                                                } else {
                                                    i = i2;
                                                    arrayList5 = arrayList;
                                                    if (next.startsWith("#EXT-X-PRELOAD-HINT")) {
                                                        if (part2 == null && "PART".equals(parseStringAttr(next, REGEX_PRELOAD_HINT_TYPE, hashMap))) {
                                                            String parseStringAttr4 = parseStringAttr(next, REGEX_URI, hashMap);
                                                            long parseOptionalLongAttr = parseOptionalLongAttr(next, REGEX_BYTERANGE_START, -1L);
                                                            long parseOptionalLongAttr2 = parseOptionalLongAttr(next, REGEX_BYTERANGE_LENGTH, -1L);
                                                            long j20 = j10;
                                                            String segmentEncryptionIV = getSegmentEncryptionIV(j20, str12, str13);
                                                            if (drmInitData3 != null || treeMap.isEmpty()) {
                                                                j2 = j20;
                                                            } else {
                                                                j2 = j20;
                                                                DrmInitData.SchemeData[] schemeDataArr = (DrmInitData.SchemeData[]) treeMap.values().toArray(new DrmInitData.SchemeData[0]);
                                                                DrmInitData drmInitData5 = new DrmInitData(str3, schemeDataArr);
                                                                if (drmInitData2 == null) {
                                                                    drmInitData2 = getPlaylistProtectionSchemes(str3, schemeDataArr);
                                                                }
                                                                drmInitData3 = drmInitData5;
                                                            }
                                                            if (parseOptionalLongAttr == -1 || parseOptionalLongAttr2 != -1) {
                                                                part2 = new HlsMediaPlaylist.Part(parseStringAttr4, segment, 0L, i5, j8, drmInitData3, str12, segmentEncryptionIV, parseOptionalLongAttr != -1 ? parseOptionalLongAttr : 0L, parseOptionalLongAttr2, false, false, true);
                                                            }
                                                            hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                                            hlsMediaPlaylist2 = hlsMediaPlaylist;
                                                            str9 = str3;
                                                            str7 = str12;
                                                            j10 = j2;
                                                            i2 = i;
                                                            arrayList6 = arrayList5;
                                                            str5 = str2;
                                                            z2 = false;
                                                            str8 = str13;
                                                        }
                                                    } else {
                                                        j = j10;
                                                        if (next.startsWith("#EXT-X-PART")) {
                                                            String segmentEncryptionIV2 = getSegmentEncryptionIV(j, str12, str13);
                                                            String parseStringAttr5 = parseStringAttr(next, REGEX_URI, hashMap);
                                                            HlsMediaPlaylist.Part part3 = part2;
                                                            ArrayList arrayList12 = arrayList8;
                                                            long parseDoubleAttr = (long) (parseDoubleAttr(next, REGEX_ATTR_DURATION) * 1000000.0d);
                                                            ArrayList arrayList13 = arrayList9;
                                                            boolean parseOptionalBooleanAttribute = parseOptionalBooleanAttribute(next, REGEX_INDEPENDENT, false) | (z3 && arrayList10.isEmpty());
                                                            boolean parseOptionalBooleanAttribute2 = parseOptionalBooleanAttribute(next, REGEX_GAP, false);
                                                            String parseOptionalStringAttr5 = parseOptionalStringAttr(next, REGEX_ATTR_BYTERANGE, hashMap);
                                                            if (parseOptionalStringAttr5 != null) {
                                                                String[] split3 = Util.split(parseOptionalStringAttr5, "@");
                                                                j4 = Long.parseLong(split3[0]);
                                                                if (split3.length > 1) {
                                                                    j12 = Long.parseLong(split3[1]);
                                                                }
                                                            } else {
                                                                j4 = -1;
                                                            }
                                                            if (j4 == -1) {
                                                                j12 = 0;
                                                            }
                                                            if (drmInitData3 == null && !treeMap.isEmpty()) {
                                                                DrmInitData.SchemeData[] schemeDataArr2 = (DrmInitData.SchemeData[]) treeMap.values().toArray(new DrmInitData.SchemeData[0]);
                                                                DrmInitData drmInitData6 = new DrmInitData(str3, schemeDataArr2);
                                                                if (drmInitData2 == null) {
                                                                    drmInitData2 = getPlaylistProtectionSchemes(str3, schemeDataArr2);
                                                                }
                                                                drmInitData3 = drmInitData6;
                                                            }
                                                            arrayList10.add(new HlsMediaPlaylist.Part(parseStringAttr5, segment, parseDoubleAttr, i5, j8, drmInitData3, str12, segmentEncryptionIV2, j12, j4, parseOptionalBooleanAttribute2, parseOptionalBooleanAttribute, false));
                                                            j8 += parseDoubleAttr;
                                                            if (j4 != -1) {
                                                                j12 += j4;
                                                            }
                                                            arrayList8 = arrayList12;
                                                            str7 = str12;
                                                            i2 = i;
                                                            arrayList9 = arrayList13;
                                                            part2 = part3;
                                                            arrayList6 = arrayList5;
                                                            z2 = false;
                                                            j10 = j;
                                                            str9 = str3;
                                                            str8 = str13;
                                                            str5 = str2;
                                                            hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                                            hlsMediaPlaylist2 = hlsMediaPlaylist;
                                                        } else {
                                                            part = part2;
                                                            arrayList2 = arrayList8;
                                                            arrayList3 = arrayList9;
                                                            if (next.startsWith("#")) {
                                                                arrayList4 = arrayList5;
                                                            } else {
                                                                String segmentEncryptionIV3 = getSegmentEncryptionIV(j, str12, str13);
                                                                long j21 = j + 1;
                                                                String replaceVariableReferences = replaceVariableReferences(next, hashMap);
                                                                HlsMediaPlaylist.Segment segment4 = (HlsMediaPlaylist.Segment) hashMap2.get(replaceVariableReferences);
                                                                if (j16 == -1) {
                                                                    j3 = 0;
                                                                } else {
                                                                    if (z8 && segment == null && segment4 == null) {
                                                                        segment4 = new HlsMediaPlaylist.Segment(replaceVariableReferences, 0L, j7, null, null);
                                                                        hashMap2.put(replaceVariableReferences, segment4);
                                                                    }
                                                                    j3 = j7;
                                                                }
                                                                if (drmInitData3 != null || treeMap.isEmpty()) {
                                                                    drmInitData = drmInitData3;
                                                                } else {
                                                                    DrmInitData.SchemeData[] schemeDataArr3 = (DrmInitData.SchemeData[]) treeMap.values().toArray(new DrmInitData.SchemeData[0]);
                                                                    drmInitData = new DrmInitData(str3, schemeDataArr3);
                                                                    if (drmInitData2 == null) {
                                                                        drmInitData2 = getPlaylistProtectionSchemes(str3, schemeDataArr3);
                                                                    }
                                                                }
                                                                arrayList5.add(new HlsMediaPlaylist.Segment(replaceVariableReferences, segment != null ? segment : segment4, str6, j11, i5, j9, drmInitData, str12, segmentEncryptionIV3, j3, j16, z7, arrayList10));
                                                                j8 = j9 + j11;
                                                                arrayList10 = new ArrayList();
                                                                if (j16 != -1) {
                                                                    j3 += j16;
                                                                }
                                                                j7 = j3;
                                                                arrayList8 = arrayList2;
                                                                drmInitData3 = drmInitData;
                                                                str7 = str12;
                                                                j11 = 0;
                                                                j9 = j8;
                                                                i2 = i;
                                                                part2 = part;
                                                                str6 = str2;
                                                                z2 = false;
                                                                z7 = false;
                                                                j16 = -1;
                                                                j10 = j21;
                                                                arrayList6 = arrayList5;
                                                                str8 = str13;
                                                                arrayList9 = arrayList3;
                                                                hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                                                hlsMediaPlaylist2 = hlsMediaPlaylist;
                                                                str9 = str3;
                                                                str5 = str6;
                                                            }
                                                        }
                                                    }
                                                }
                                                arrayList2 = arrayList8;
                                                arrayList3 = arrayList9;
                                                j = j10;
                                                arrayList4 = arrayList5;
                                            }
                                            part = part2;
                                        }
                                        arrayList6 = arrayList;
                                        z2 = false;
                                    }
                                    hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                                    hlsMediaPlaylist2 = hlsMediaPlaylist;
                                    str9 = str3;
                                    str8 = str13;
                                    str7 = str12;
                                    str5 = str2;
                                    arrayList6 = arrayList;
                                    z2 = false;
                                }
                            }
                            arrayList8 = arrayList2;
                            str7 = str12;
                            i2 = i;
                            part2 = part;
                            z2 = false;
                            j10 = j;
                            arrayList6 = arrayList4;
                            str8 = str13;
                            arrayList9 = arrayList3;
                            hlsMultivariantPlaylist2 = hlsMultivariantPlaylist;
                            hlsMediaPlaylist2 = hlsMediaPlaylist;
                            str9 = str3;
                            str5 = str2;
                        }
                        str8 = str13;
                        str7 = str12;
                        arrayList6 = arrayList;
                        z2 = false;
                    }
                }
                arrayList6 = arrayList;
            }
        }
        int i10 = i2;
        HlsMediaPlaylist.Part part4 = part2;
        ArrayList arrayList14 = arrayList8;
        ArrayList arrayList15 = arrayList9;
        ArrayList arrayList16 = arrayList6;
        HashMap hashMap3 = new HashMap();
        for (int i11 = 0; i11 < arrayList14.size(); i11++) {
            HlsMediaPlaylist.RenditionReport renditionReport = (HlsMediaPlaylist.RenditionReport) arrayList14.get(i11);
            long j22 = renditionReport.lastMediaSequence;
            if (j22 == -1) {
                j22 = (j6 + arrayList16.size()) - (arrayList10.isEmpty() ? 1L : 0L);
            }
            int i12 = renditionReport.lastPartIndex;
            if (i12 == -1 && j15 != -9223372036854775807L) {
                i12 = (arrayList10.isEmpty() ? ((HlsMediaPlaylist.Segment) Iterables.getLast(arrayList16)).parts : arrayList10).size() - 1;
            }
            Uri uri = renditionReport.playlistUri;
            hashMap3.put(uri, new HlsMediaPlaylist.RenditionReport(uri, j22, i12));
        }
        if (part4 != null) {
            arrayList10.add(part4);
        }
        return new HlsMediaPlaylist(i10, str, arrayList15, j13, z4, j5, z5, i3, j6, i4, j14, j15, z3, z6, j5 != 0, drmInitData2, arrayList16, arrayList10, serverControl2, hashMap3);
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

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [int] */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r0v7 */
    private static int parseSelectionFlags(String str) {
        boolean parseOptionalBooleanAttribute = parseOptionalBooleanAttribute(str, REGEX_DEFAULT, false);
        ?? r0 = parseOptionalBooleanAttribute;
        if (parseOptionalBooleanAttribute(str, REGEX_FORCED, false)) {
            r0 = (parseOptionalBooleanAttribute ? 1 : 0) | true;
        }
        return parseOptionalBooleanAttribute(str, REGEX_AUTOSELECT, false) ? r0 | 4 : r0;
    }

    private static int parseRoleFlags(String str, Map<String, String> map) {
        String parseOptionalStringAttr = parseOptionalStringAttr(str, REGEX_CHARACTERISTICS, map);
        if (TextUtils.isEmpty(parseOptionalStringAttr)) {
            return 0;
        }
        String[] split = Util.split(parseOptionalStringAttr, ",");
        int i = Util.contains(split, "public.accessibility.describes-video") ? LiteMode.FLAG_CALLS_ANIMATIONS : 0;
        if (Util.contains(split, "public.accessibility.transcribes-spoken-dialog")) {
            i |= 4096;
        }
        if (Util.contains(split, "public.accessibility.describes-music-and-sound")) {
            i |= 1024;
        }
        return Util.contains(split, "public.easy-to-read") ? i | 8192 : i;
    }

    private static DrmInitData.SchemeData parseDrmSchemeData(String str, String str2, Map<String, String> map) throws ParserException {
        String parseOptionalStringAttr = parseOptionalStringAttr(str, REGEX_KEYFORMATVERSIONS, "1", map);
        if ("urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed".equals(str2)) {
            String parseStringAttr = parseStringAttr(str, REGEX_URI, map);
            return new DrmInitData.SchemeData(C.WIDEVINE_UUID, "video/mp4", Base64.decode(parseStringAttr.substring(parseStringAttr.indexOf(44)), 0));
        } else if ("com.widevine".equals(str2)) {
            return new DrmInitData.SchemeData(C.WIDEVINE_UUID, "hls", Util.getUtf8Bytes(str));
        } else {
            if ("com.microsoft.playready".equals(str2) && "1".equals(parseOptionalStringAttr)) {
                String parseStringAttr2 = parseStringAttr(str, REGEX_URI, map);
                byte[] decode = Base64.decode(parseStringAttr2.substring(parseStringAttr2.indexOf(44)), 0);
                UUID uuid = C.PLAYREADY_UUID;
                return new DrmInitData.SchemeData(uuid, "video/mp4", PsshAtomUtil.buildPsshAtom(uuid, decode));
            }
            return null;
        }
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

    private static String parseEncryptionScheme(String str) {
        return ("SAMPLE-AES-CENC".equals(str) || "SAMPLE-AES-CTR".equals(str)) ? "cenc" : "cbcs";
    }

    private static int parseIntAttr(String str, Pattern pattern) throws ParserException {
        return Integer.parseInt(parseStringAttr(str, pattern, Collections.emptyMap()));
    }

    private static int parseOptionalIntAttr(String str, Pattern pattern, int i) {
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Integer.parseInt((String) Assertions.checkNotNull(matcher.group(1))) : i;
    }

    private static long parseLongAttr(String str, Pattern pattern) throws ParserException {
        return Long.parseLong(parseStringAttr(str, pattern, Collections.emptyMap()));
    }

    private static long parseOptionalLongAttr(String str, Pattern pattern, long j) {
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Long.parseLong((String) Assertions.checkNotNull(matcher.group(1))) : j;
    }

    private static long parseTimeSecondsToUs(String str, Pattern pattern) throws ParserException {
        return new BigDecimal(parseStringAttr(str, pattern, Collections.emptyMap())).multiply(new BigDecimal(1000000L)).longValue();
    }

    private static double parseDoubleAttr(String str, Pattern pattern) throws ParserException {
        return Double.parseDouble(parseStringAttr(str, pattern, Collections.emptyMap()));
    }

    private static String parseStringAttr(String str, Pattern pattern, Map<String, String> map) throws ParserException {
        String parseOptionalStringAttr = parseOptionalStringAttr(str, pattern, map);
        if (parseOptionalStringAttr != null) {
            return parseOptionalStringAttr;
        }
        throw ParserException.createForMalformedManifest("Couldn't match " + pattern.pattern() + " in " + str, null);
    }

    private static String parseOptionalStringAttr(String str, Pattern pattern, Map<String, String> map) {
        return parseOptionalStringAttr(str, pattern, null, map);
    }

    private static String parseOptionalStringAttr(String str, Pattern pattern, String str2, Map<String, String> map) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            str2 = (String) Assertions.checkNotNull(matcher.group(1));
        }
        return (map.isEmpty() || str2 == null) ? str2 : replaceVariableReferences(str2, map);
    }

    private static double parseOptionalDoubleAttr(String str, Pattern pattern, double d) {
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? Double.parseDouble((String) Assertions.checkNotNull(matcher.group(1))) : d;
    }

    private static String replaceVariableReferences(String str, Map<String, String> map) {
        Matcher matcher = REGEX_VARIABLE_REFERENCE.matcher(str);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String group = matcher.group(1);
            if (map.containsKey(group)) {
                matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement(map.get(group)));
            }
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    private static boolean parseOptionalBooleanAttribute(String str, Pattern pattern, boolean z) {
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? "YES".equals(matcher.group(1)) : z;
    }

    private static Pattern compileBooleanAttrPattern(String str) {
        return Pattern.compile(str + "=(NO|YES)");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class LineIterator {
        private final Queue<String> extraLines;
        private String next;
        private final BufferedReader reader;

        public LineIterator(Queue<String> queue, BufferedReader bufferedReader) {
            this.extraLines = queue;
            this.reader = bufferedReader;
        }

        public boolean hasNext() throws IOException {
            String trim;
            if (this.next != null) {
                return true;
            }
            if (!this.extraLines.isEmpty()) {
                this.next = (String) Assertions.checkNotNull(this.extraLines.poll());
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

        public String next() throws IOException {
            if (hasNext()) {
                String str = this.next;
                this.next = null;
                return str;
            }
            throw new NoSuchElementException();
        }
    }
}
