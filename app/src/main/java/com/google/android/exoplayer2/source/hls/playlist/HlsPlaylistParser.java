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
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.exoplayer2.source.UnrecognizedInputFormatException;
import com.google.android.exoplayer2.source.hls.HlsTrackMetadataEntry;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import com.microsoft.appcenter.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;
/* loaded from: classes3.dex */
public final class HlsPlaylistParser implements ParsingLoadable.Parser<HlsPlaylist> {
    private static final String ATTR_CLOSED_CAPTIONS_NONE = "CLOSED-CAPTIONS=NONE";
    private static final String BOOLEAN_FALSE = "NO";
    private static final String BOOLEAN_TRUE = "YES";
    private static final String KEYFORMAT_IDENTITY = "identity";
    private static final String KEYFORMAT_PLAYREADY = "com.microsoft.playready";
    private static final String KEYFORMAT_WIDEVINE_PSSH_BINARY = "urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed";
    private static final String KEYFORMAT_WIDEVINE_PSSH_JSON = "com.widevine";
    private static final String METHOD_AES_128 = "AES-128";
    private static final String METHOD_NONE = "NONE";
    private static final String METHOD_SAMPLE_AES = "SAMPLE-AES";
    private static final String METHOD_SAMPLE_AES_CENC = "SAMPLE-AES-CENC";
    private static final String METHOD_SAMPLE_AES_CTR = "SAMPLE-AES-CTR";
    private static final String PLAYLIST_HEADER = "#EXTM3U";
    private static final String TAG_BYTERANGE = "#EXT-X-BYTERANGE";
    private static final String TAG_DEFINE = "#EXT-X-DEFINE";
    private static final String TAG_DISCONTINUITY = "#EXT-X-DISCONTINUITY";
    private static final String TAG_DISCONTINUITY_SEQUENCE = "#EXT-X-DISCONTINUITY-SEQUENCE";
    private static final String TAG_ENDLIST = "#EXT-X-ENDLIST";
    private static final String TAG_GAP = "#EXT-X-GAP";
    private static final String TAG_INDEPENDENT_SEGMENTS = "#EXT-X-INDEPENDENT-SEGMENTS";
    private static final String TAG_INIT_SEGMENT = "#EXT-X-MAP";
    private static final String TAG_KEY = "#EXT-X-KEY";
    private static final String TAG_MEDIA = "#EXT-X-MEDIA";
    private static final String TAG_MEDIA_DURATION = "#EXTINF";
    private static final String TAG_MEDIA_SEQUENCE = "#EXT-X-MEDIA-SEQUENCE";
    private static final String TAG_PLAYLIST_TYPE = "#EXT-X-PLAYLIST-TYPE";
    private static final String TAG_PREFIX = "#EXT";
    private static final String TAG_PROGRAM_DATE_TIME = "#EXT-X-PROGRAM-DATE-TIME";
    private static final String TAG_SESSION_KEY = "#EXT-X-SESSION-KEY";
    private static final String TAG_START = "#EXT-X-START";
    private static final String TAG_STREAM_INF = "#EXT-X-STREAM-INF";
    private static final String TAG_TARGET_DURATION = "#EXT-X-TARGETDURATION";
    private static final String TAG_VERSION = "#EXT-X-VERSION";
    private static final String TYPE_AUDIO = "AUDIO";
    private static final String TYPE_CLOSED_CAPTIONS = "CLOSED-CAPTIONS";
    private static final String TYPE_SUBTITLES = "SUBTITLES";
    private static final String TYPE_VIDEO = "VIDEO";
    private final HlsMasterPlaylist masterPlaylist;
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
    private static final Pattern REGEX_VERSION = Pattern.compile("#EXT-X-VERSION:(\\d+)\\b");
    private static final Pattern REGEX_PLAYLIST_TYPE = Pattern.compile("#EXT-X-PLAYLIST-TYPE:(.+)\\b");
    private static final Pattern REGEX_MEDIA_SEQUENCE = Pattern.compile("#EXT-X-MEDIA-SEQUENCE:(\\d+)\\b");
    private static final Pattern REGEX_MEDIA_DURATION = Pattern.compile("#EXTINF:([\\d\\.]+)\\b");
    private static final Pattern REGEX_MEDIA_TITLE = Pattern.compile("#EXTINF:[\\d\\.]+\\b,(.+)");
    private static final Pattern REGEX_TIME_OFFSET = Pattern.compile("TIME-OFFSET=(-?[\\d\\.]+)\\b");
    private static final Pattern REGEX_BYTERANGE = Pattern.compile("#EXT-X-BYTERANGE:(\\d+(?:@\\d+)?)\\b");
    private static final Pattern REGEX_ATTR_BYTERANGE = Pattern.compile("BYTERANGE=\"(\\d+(?:@\\d+)?)\\b\"");
    private static final Pattern REGEX_METHOD = Pattern.compile("METHOD=(NONE|AES-128|SAMPLE-AES|SAMPLE-AES-CENC|SAMPLE-AES-CTR)\\s*(?:,|$)");
    private static final Pattern REGEX_KEYFORMAT = Pattern.compile("KEYFORMAT=\"(.+?)\"");
    private static final Pattern REGEX_KEYFORMATVERSIONS = Pattern.compile("KEYFORMATVERSIONS=\"(.+?)\"");
    private static final Pattern REGEX_URI = Pattern.compile("URI=\"(.+?)\"");
    private static final Pattern REGEX_IV = Pattern.compile("IV=([^,.*]+)");
    private static final Pattern REGEX_TYPE = Pattern.compile("TYPE=(AUDIO|VIDEO|SUBTITLES|CLOSED-CAPTIONS)");
    private static final Pattern REGEX_LANGUAGE = Pattern.compile("LANGUAGE=\"(.+?)\"");
    private static final Pattern REGEX_NAME = Pattern.compile("NAME=\"(.+?)\"");
    private static final Pattern REGEX_GROUP_ID = Pattern.compile("GROUP-ID=\"(.+?)\"");
    private static final Pattern REGEX_CHARACTERISTICS = Pattern.compile("CHARACTERISTICS=\"(.+?)\"");
    private static final Pattern REGEX_INSTREAM_ID = Pattern.compile("INSTREAM-ID=\"((?:CC|SERVICE)\\d+)\"");
    private static final Pattern REGEX_AUTOSELECT = compileBooleanAttrPattern("AUTOSELECT");
    private static final Pattern REGEX_DEFAULT = compileBooleanAttrPattern("DEFAULT");
    private static final Pattern REGEX_FORCED = compileBooleanAttrPattern("FORCED");
    private static final Pattern REGEX_VALUE = Pattern.compile("VALUE=\"(.+?)\"");
    private static final Pattern REGEX_IMPORT = Pattern.compile("IMPORT=\"(.+?)\"");
    private static final Pattern REGEX_VARIABLE_REFERENCE = Pattern.compile("\\{\\$([a-zA-Z0-9\\-_]+)\\}");

    public HlsPlaylistParser() {
        this(HlsMasterPlaylist.EMPTY);
    }

    public HlsPlaylistParser(HlsMasterPlaylist masterPlaylist) {
        this.masterPlaylist = masterPlaylist;
    }

    @Override // com.google.android.exoplayer2.upstream.ParsingLoadable.Parser
    public HlsPlaylist parse(Uri uri, InputStream inputStream) throws IOException {
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Queue<String> extraLines = new ArrayDeque<>();
        try {
            if (!checkPlaylistHeader(reader)) {
                throw new UnrecognizedInputFormatException("Input does not start with the #EXTM3U header.", uri);
            }
            while (true) {
                String line2 = reader.readLine();
                if (line2 == null) {
                    Util.closeQuietly(reader);
                    throw new ParserException("Failed to parse the playlist, could not identify any tags.");
                }
                line = line2.trim();
                if (!line.isEmpty()) {
                    if (!line.startsWith(TAG_STREAM_INF)) {
                        if (line.startsWith(TAG_TARGET_DURATION) || line.startsWith(TAG_MEDIA_SEQUENCE) || line.startsWith(TAG_MEDIA_DURATION) || line.startsWith(TAG_KEY) || line.startsWith(TAG_BYTERANGE) || line.equals(TAG_DISCONTINUITY) || line.equals(TAG_DISCONTINUITY_SEQUENCE) || line.equals(TAG_ENDLIST)) {
                            break;
                        }
                        extraLines.add(line);
                    } else {
                        extraLines.add(line);
                        return parseMasterPlaylist(new LineIterator(extraLines, reader), uri.toString());
                    }
                }
            }
            extraLines.add(line);
            return parseMediaPlaylist(this.masterPlaylist, new LineIterator(extraLines, reader), uri.toString());
        } finally {
            Util.closeQuietly(reader);
        }
    }

    private static boolean checkPlaylistHeader(BufferedReader reader) throws IOException {
        int last = reader.read();
        if (last == 239) {
            if (reader.read() != 187 || reader.read() != 191) {
                return false;
            }
            last = reader.read();
        }
        int last2 = skipIgnorableWhitespace(reader, true, last);
        int playlistHeaderLength = PLAYLIST_HEADER.length();
        for (int i = 0; i < playlistHeaderLength; i++) {
            if (last2 != PLAYLIST_HEADER.charAt(i)) {
                return false;
            }
            last2 = reader.read();
        }
        return Util.isLinebreak(skipIgnorableWhitespace(reader, false, last2));
    }

    private static int skipIgnorableWhitespace(BufferedReader reader, boolean skipLinebreaks, int c) throws IOException {
        while (c != -1 && Character.isWhitespace(c) && (skipLinebreaks || !Util.isLinebreak(c))) {
            c = reader.read();
        }
        return c;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Incorrect condition in loop: B:4:0x0049 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static HlsMasterPlaylist parseMasterPlaylist(LineIterator iterator, String baseUri) throws IOException {
        Uri uri;
        char c;
        ArrayList<HlsMasterPlaylist.Rendition> videos;
        List<Format> muxedCaptionFormats;
        int accessibilityChannel;
        String mimeType;
        Format muxedAudioFormat;
        List<Format> muxedCaptionFormats2;
        int height;
        int width;
        float frameRate;
        ArrayList<HlsTrackMetadataEntry.VariantInfo> variantInfosForUrl;
        String str = baseUri;
        HashMap<Uri, ArrayList<HlsTrackMetadataEntry.VariantInfo>> urlToVariantInfos = new HashMap<>();
        HashMap<String, String> variableDefinitions = new HashMap<>();
        ArrayList<HlsMasterPlaylist.Variant> variants = new ArrayList<>();
        ArrayList<HlsMasterPlaylist.Rendition> videos2 = new ArrayList<>();
        ArrayList<HlsMasterPlaylist.Rendition> audios = new ArrayList<>();
        ArrayList<HlsMasterPlaylist.Rendition> subtitles = new ArrayList<>();
        ArrayList<HlsMasterPlaylist.Rendition> closedCaptions = new ArrayList<>();
        ArrayList<String> mediaTags = new ArrayList<>();
        ArrayList<DrmInitData> sessionKeyDrmInitData = new ArrayList<>();
        ArrayList<String> tags = new ArrayList<>();
        Format muxedAudioFormat2 = null;
        List<Format> muxedCaptionFormats3 = null;
        boolean noClosedCaptions = false;
        boolean hasIndependentSegmentsTag = false;
        while (noClosedCaptions) {
            String line = iterator.next();
            if (line.startsWith(TAG_PREFIX)) {
                tags.add(line);
            }
            if (line.startsWith(TAG_DEFINE)) {
                variableDefinitions.put(parseStringAttr(line, REGEX_NAME, variableDefinitions), parseStringAttr(line, REGEX_VALUE, variableDefinitions));
                muxedAudioFormat = muxedAudioFormat2;
                muxedCaptionFormats2 = muxedCaptionFormats3;
            } else if (line.equals(TAG_INDEPENDENT_SEGMENTS)) {
                hasIndependentSegmentsTag = true;
            } else if (line.startsWith(TAG_MEDIA)) {
                mediaTags.add(line);
                muxedAudioFormat = muxedAudioFormat2;
                muxedCaptionFormats2 = muxedCaptionFormats3;
            } else if (line.startsWith(TAG_SESSION_KEY)) {
                String keyFormat = parseOptionalStringAttr(line, REGEX_KEYFORMAT, KEYFORMAT_IDENTITY, variableDefinitions);
                DrmInitData.SchemeData schemeData = parseDrmSchemeData(line, keyFormat, variableDefinitions);
                if (schemeData == null) {
                    muxedAudioFormat = muxedAudioFormat2;
                    muxedCaptionFormats2 = muxedCaptionFormats3;
                } else {
                    muxedAudioFormat = muxedAudioFormat2;
                    String method = parseStringAttr(line, REGEX_METHOD, variableDefinitions);
                    muxedCaptionFormats2 = muxedCaptionFormats3;
                    String scheme = parseEncryptionScheme(method);
                    sessionKeyDrmInitData.add(new DrmInitData(scheme, schemeData));
                }
            } else {
                muxedAudioFormat = muxedAudioFormat2;
                muxedCaptionFormats2 = muxedCaptionFormats3;
                if (line.startsWith(TAG_STREAM_INF)) {
                    noClosedCaptions |= line.contains(ATTR_CLOSED_CAPTIONS_NONE);
                    int bitrate = parseIntAttr(line, REGEX_BANDWIDTH);
                    parseOptionalIntAttr(line, REGEX_AVERAGE_BANDWIDTH, -1);
                    String codecs = parseOptionalStringAttr(line, REGEX_CODECS, variableDefinitions);
                    String resolutionString = parseOptionalStringAttr(line, REGEX_RESOLUTION, variableDefinitions);
                    if (resolutionString != null) {
                        String[] widthAndHeight = resolutionString.split("x");
                        width = Integer.parseInt(widthAndHeight[0]);
                        height = Integer.parseInt(widthAndHeight[1]);
                        if (width <= 0 || height <= 0) {
                            width = -1;
                            height = -1;
                        }
                    } else {
                        width = -1;
                        height = -1;
                    }
                    String frameRateString = parseOptionalStringAttr(line, REGEX_FRAME_RATE, variableDefinitions);
                    if (frameRateString == null) {
                        frameRate = -1.0f;
                    } else {
                        float frameRate2 = Float.parseFloat(frameRateString);
                        frameRate = frameRate2;
                    }
                    String videoGroupId = parseOptionalStringAttr(line, REGEX_VIDEO, variableDefinitions);
                    String audioGroupId = parseOptionalStringAttr(line, REGEX_AUDIO, variableDefinitions);
                    ArrayList<DrmInitData> sessionKeyDrmInitData2 = sessionKeyDrmInitData;
                    String subtitlesGroupId = parseOptionalStringAttr(line, REGEX_SUBTITLES, variableDefinitions);
                    ArrayList<HlsMasterPlaylist.Rendition> closedCaptions2 = closedCaptions;
                    String closedCaptionsGroupId = parseOptionalStringAttr(line, REGEX_CLOSED_CAPTIONS, variableDefinitions);
                    if (!iterator.hasNext()) {
                        throw new ParserException("#EXT-X-STREAM-INF tag must be followed by another line");
                    }
                    ArrayList<String> tags2 = tags;
                    Uri uri2 = UriUtil.resolveToUri(str, replaceVariableReferences(iterator.next(), variableDefinitions));
                    Format format = Format.createVideoContainerFormat(Integer.toString(variants.size()), null, MimeTypes.APPLICATION_M3U8, null, codecs, null, bitrate, width, height, frameRate, null, 0, 0);
                    variants.add(new HlsMasterPlaylist.Variant(uri2, format, videoGroupId, audioGroupId, subtitlesGroupId, closedCaptionsGroupId));
                    ArrayList<HlsTrackMetadataEntry.VariantInfo> variantInfosForUrl2 = urlToVariantInfos.get(uri2);
                    if (variantInfosForUrl2 == null) {
                        variantInfosForUrl = new ArrayList<>();
                        urlToVariantInfos.put(uri2, variantInfosForUrl);
                    } else {
                        variantInfosForUrl = variantInfosForUrl2;
                    }
                    variantInfosForUrl.add(new HlsTrackMetadataEntry.VariantInfo(bitrate, videoGroupId, audioGroupId, subtitlesGroupId, closedCaptionsGroupId));
                    muxedAudioFormat2 = muxedAudioFormat;
                    muxedCaptionFormats3 = muxedCaptionFormats2;
                    videos2 = videos2;
                    audios = audios;
                    sessionKeyDrmInitData = sessionKeyDrmInitData2;
                    closedCaptions = closedCaptions2;
                    tags = tags2;
                }
            }
            muxedAudioFormat2 = muxedAudioFormat;
            muxedCaptionFormats3 = muxedCaptionFormats2;
            videos2 = videos2;
            audios = audios;
            sessionKeyDrmInitData = sessionKeyDrmInitData;
            closedCaptions = closedCaptions;
            tags = tags;
        }
        Format muxedAudioFormat3 = muxedAudioFormat2;
        List<Format> muxedCaptionFormats4 = muxedCaptionFormats3;
        ArrayList<String> tags3 = tags;
        ArrayList<DrmInitData> sessionKeyDrmInitData3 = sessionKeyDrmInitData;
        ArrayList<HlsMasterPlaylist.Rendition> closedCaptions3 = closedCaptions;
        ArrayList<HlsMasterPlaylist.Rendition> audios2 = audios;
        ArrayList<HlsMasterPlaylist.Rendition> videos3 = videos2;
        ArrayList<HlsMasterPlaylist.Variant> deduplicatedVariants = new ArrayList<>();
        HashSet<Uri> urlsInDeduplicatedVariants = new HashSet<>();
        int i = 0;
        while (true) {
            uri = null;
            if (i >= variants.size()) {
                break;
            }
            HlsMasterPlaylist.Variant variant = variants.get(i);
            if (urlsInDeduplicatedVariants.add(variant.url)) {
                Assertions.checkState(variant.format.metadata == null);
                HlsTrackMetadataEntry hlsMetadataEntry = new HlsTrackMetadataEntry(null, null, (List) Assertions.checkNotNull(urlToVariantInfos.get(variant.url)));
                deduplicatedVariants.add(variant.copyWithFormat(variant.format.copyWithMetadata(new Metadata(hlsMetadataEntry))));
            }
            i++;
        }
        int i2 = 0;
        List<Format> muxedCaptionFormats5 = muxedCaptionFormats4;
        while (i2 < mediaTags.size()) {
            String line2 = mediaTags.get(i2);
            String groupId = parseStringAttr(line2, REGEX_GROUP_ID, variableDefinitions);
            String name = parseStringAttr(line2, REGEX_NAME, variableDefinitions);
            String referenceUri = parseOptionalStringAttr(line2, REGEX_URI, variableDefinitions);
            Uri uri3 = referenceUri == null ? uri : UriUtil.resolveToUri(str, referenceUri);
            String language = parseOptionalStringAttr(line2, REGEX_LANGUAGE, variableDefinitions);
            int selectionFlags = parseSelectionFlags(line2);
            int roleFlags = parseRoleFlags(line2, variableDefinitions);
            StringBuilder sb = new StringBuilder();
            sb.append(groupId);
            ArrayList<String> mediaTags2 = mediaTags;
            sb.append(Constants.COMMON_SCHEMA_PREFIX_SEPARATOR);
            sb.append(name);
            String formatId = sb.toString();
            HashSet<Uri> urlsInDeduplicatedVariants2 = urlsInDeduplicatedVariants;
            Metadata metadata = new Metadata(new HlsTrackMetadataEntry(groupId, name, Collections.emptyList()));
            String parseStringAttr = parseStringAttr(line2, REGEX_TYPE, variableDefinitions);
            switch (parseStringAttr.hashCode()) {
                case -959297733:
                    if (parseStringAttr.equals(TYPE_SUBTITLES)) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case -333210994:
                    if (parseStringAttr.equals(TYPE_CLOSED_CAPTIONS)) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 62628790:
                    if (parseStringAttr.equals(TYPE_AUDIO)) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 81665115:
                    if (parseStringAttr.equals(TYPE_VIDEO)) {
                        c = 0;
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
                    muxedCaptionFormats = muxedCaptionFormats5;
                    ArrayList<HlsMasterPlaylist.Rendition> audios3 = audios2;
                    HlsMasterPlaylist.Variant variant2 = getVariantWithVideoGroup(variants, groupId);
                    String codecs2 = null;
                    int width2 = -1;
                    int height2 = -1;
                    float frameRate3 = -1.0f;
                    if (variant2 == null) {
                        audios2 = audios3;
                    } else {
                        Format variantFormat = variant2.format;
                        audios2 = audios3;
                        codecs2 = Util.getCodecsOfType(variantFormat.codecs, 2);
                        width2 = variantFormat.width;
                        int height3 = variantFormat.height;
                        height2 = height3;
                        frameRate3 = variantFormat.frameRate;
                    }
                    Format format2 = Format.createVideoContainerFormat(formatId, name, MimeTypes.APPLICATION_M3U8, codecs2 != null ? MimeTypes.getMediaMimeType(codecs2) : null, codecs2, null, -1, width2, height2, frameRate3, null, selectionFlags, roleFlags).copyWithMetadata(metadata);
                    if (uri3 != null) {
                        videos = videos3;
                        videos.add(new HlsMasterPlaylist.Rendition(uri3, format2, groupId, name));
                        break;
                    } else {
                        videos = videos3;
                        break;
                    }
                case 1:
                    muxedCaptionFormats = muxedCaptionFormats5;
                    HlsMasterPlaylist.Variant variant3 = getVariantWithAudioGroup(variants, groupId);
                    String codecs3 = variant3 != null ? Util.getCodecsOfType(variant3.format.codecs, 1) : null;
                    String sampleMimeType = codecs3 != null ? MimeTypes.getMediaMimeType(codecs3) : null;
                    String channelsString = parseOptionalStringAttr(line2, REGEX_CHANNELS, variableDefinitions);
                    int channelCount = -1;
                    if (channelsString != null) {
                        channelCount = Integer.parseInt(Util.splitAtFirst(channelsString, "/")[0]);
                        if (MimeTypes.AUDIO_E_AC3.equals(sampleMimeType) && channelsString.endsWith("/JOC")) {
                            sampleMimeType = MimeTypes.AUDIO_E_AC3_JOC;
                        } else {
                            sampleMimeType = sampleMimeType;
                        }
                    }
                    Format format3 = Format.createAudioContainerFormat(formatId, name, MimeTypes.APPLICATION_M3U8, sampleMimeType, codecs3, null, -1, channelCount, -1, null, selectionFlags, roleFlags, language);
                    if (uri3 == null) {
                        muxedAudioFormat3 = format3;
                        muxedCaptionFormats5 = muxedCaptionFormats;
                        videos = videos3;
                        continue;
                        i2++;
                        str = baseUri;
                        videos3 = videos;
                        mediaTags = mediaTags2;
                        urlsInDeduplicatedVariants = urlsInDeduplicatedVariants2;
                        uri = null;
                    } else {
                        audios2.add(new HlsMasterPlaylist.Rendition(uri3, format3.copyWithMetadata(metadata), groupId, name));
                        videos = videos3;
                        break;
                    }
                    break;
                case 2:
                    String codecs4 = null;
                    String sampleMimeType2 = null;
                    HlsMasterPlaylist.Variant variant4 = getVariantWithSubtitleGroup(variants, groupId);
                    if (variant4 == null) {
                        muxedCaptionFormats = muxedCaptionFormats5;
                    } else {
                        muxedCaptionFormats = muxedCaptionFormats5;
                        codecs4 = Util.getCodecsOfType(variant4.format.codecs, 3);
                        sampleMimeType2 = MimeTypes.getMediaMimeType(codecs4);
                    }
                    if (sampleMimeType2 == null) {
                        sampleMimeType2 = MimeTypes.TEXT_VTT;
                    }
                    Format format4 = Format.createTextContainerFormat(formatId, name, MimeTypes.APPLICATION_M3U8, sampleMimeType2, codecs4, -1, selectionFlags, roleFlags, language).copyWithMetadata(metadata);
                    subtitles.add(new HlsMasterPlaylist.Rendition(uri3, format4, groupId, name));
                    videos = videos3;
                    break;
                case 3:
                    String instreamId = parseStringAttr(line2, REGEX_INSTREAM_ID, variableDefinitions);
                    if (instreamId.startsWith("CC")) {
                        mimeType = MimeTypes.APPLICATION_CEA608;
                        accessibilityChannel = Integer.parseInt(instreamId.substring(2));
                    } else {
                        mimeType = MimeTypes.APPLICATION_CEA708;
                        accessibilityChannel = Integer.parseInt(instreamId.substring(7));
                    }
                    if (muxedCaptionFormats5 == null) {
                        muxedCaptionFormats5 = new ArrayList<>();
                    }
                    muxedCaptionFormats5.add(Format.createTextContainerFormat(formatId, name, null, mimeType, null, -1, selectionFlags, roleFlags, language, accessibilityChannel));
                    videos = videos3;
                    continue;
                    i2++;
                    str = baseUri;
                    videos3 = videos;
                    mediaTags = mediaTags2;
                    urlsInDeduplicatedVariants = urlsInDeduplicatedVariants2;
                    uri = null;
                default:
                    muxedCaptionFormats = muxedCaptionFormats5;
                    videos = videos3;
                    break;
            }
            muxedCaptionFormats5 = muxedCaptionFormats;
            i2++;
            str = baseUri;
            videos3 = videos;
            mediaTags = mediaTags2;
            urlsInDeduplicatedVariants = urlsInDeduplicatedVariants2;
            uri = null;
        }
        List<Format> muxedCaptionFormats6 = muxedCaptionFormats5;
        ArrayList<HlsMasterPlaylist.Rendition> arrayList = videos3;
        if (noClosedCaptions) {
            muxedCaptionFormats6 = Collections.emptyList();
        }
        return new HlsMasterPlaylist(baseUri, tags3, deduplicatedVariants, arrayList, audios2, subtitles, closedCaptions3, muxedAudioFormat3, muxedCaptionFormats6, hasIndependentSegmentsTag, variableDefinitions, sessionKeyDrmInitData3);
    }

    private static HlsMasterPlaylist.Variant getVariantWithAudioGroup(ArrayList<HlsMasterPlaylist.Variant> variants, String groupId) {
        for (int i = 0; i < variants.size(); i++) {
            HlsMasterPlaylist.Variant variant = variants.get(i);
            if (groupId.equals(variant.audioGroupId)) {
                return variant;
            }
        }
        return null;
    }

    private static HlsMasterPlaylist.Variant getVariantWithVideoGroup(ArrayList<HlsMasterPlaylist.Variant> variants, String groupId) {
        for (int i = 0; i < variants.size(); i++) {
            HlsMasterPlaylist.Variant variant = variants.get(i);
            if (groupId.equals(variant.videoGroupId)) {
                return variant;
            }
        }
        return null;
    }

    private static HlsMasterPlaylist.Variant getVariantWithSubtitleGroup(ArrayList<HlsMasterPlaylist.Variant> variants, String groupId) {
        for (int i = 0; i < variants.size(); i++) {
            HlsMasterPlaylist.Variant variant = variants.get(i);
            if (groupId.equals(variant.subtitleGroupId)) {
                return variant;
            }
        }
        return null;
    }

    private static HlsMediaPlaylist parseMediaPlaylist(HlsMasterPlaylist masterPlaylist, LineIterator iterator, String baseUri) throws IOException {
        TreeMap<String, DrmInitData.SchemeData> currentSchemeDatas;
        String segmentEncryptionIV;
        DrmInitData cachedDrmInitData;
        HlsMasterPlaylist hlsMasterPlaylist = masterPlaylist;
        int playlistType = 0;
        long startOffsetUs = C.TIME_UNSET;
        long mediaSequence = 0;
        int version = 1;
        long targetDurationUs = C.TIME_UNSET;
        boolean hasIndependentSegmentsTag = hlsMasterPlaylist.hasIndependentSegments;
        boolean hasEndTag = false;
        HlsMediaPlaylist.Segment initializationSegment = null;
        HashMap<String, String> variableDefinitions = new HashMap<>();
        List<HlsMediaPlaylist.Segment> segments = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        TreeMap<String, DrmInitData.SchemeData> currentSchemeDatas2 = new TreeMap<>();
        int playlistDiscontinuitySequence = 0;
        int relativeDiscontinuitySequence = 0;
        long playlistStartTimeUs = 0;
        long segmentStartTimeUs = 0;
        long segmentByteRangeOffset = 0;
        long segmentByteRangeLength = -1;
        long segmentMediaSequence = 0;
        boolean hasGapTag = false;
        DrmInitData playlistProtectionSchemes = null;
        String fullSegmentEncryptionKeyUri = null;
        String fullSegmentEncryptionIV = null;
        DrmInitData cachedDrmInitData2 = null;
        String segmentTitle = "";
        long segmentDurationUs = 0;
        String encryptionScheme = null;
        boolean hasDiscontinuitySequence = false;
        while (true) {
            boolean hasEndTag2 = hasEndTag;
            if (!iterator.hasNext()) {
                return new HlsMediaPlaylist(playlistType, baseUri, tags, startOffsetUs, playlistStartTimeUs, hasDiscontinuitySequence, playlistDiscontinuitySequence, mediaSequence, version, targetDurationUs, hasIndependentSegmentsTag, hasEndTag2, playlistStartTimeUs != 0, playlistProtectionSchemes, segments);
            }
            String line = iterator.next();
            boolean hasIndependentSegmentsTag2 = hasIndependentSegmentsTag;
            if (line.startsWith(TAG_PREFIX)) {
                tags.add(line);
            }
            if (line.startsWith(TAG_PLAYLIST_TYPE)) {
                String playlistTypeString = parseStringAttr(line, REGEX_PLAYLIST_TYPE, variableDefinitions);
                List<String> tags2 = tags;
                if ("VOD".equals(playlistTypeString)) {
                    playlistType = 1;
                } else if ("EVENT".equals(playlistTypeString)) {
                    playlistType = 2;
                }
                tags = tags2;
                hasEndTag = hasEndTag2;
                hasIndependentSegmentsTag = hasIndependentSegmentsTag2;
            } else {
                List<String> tags3 = tags;
                if (line.startsWith(TAG_START)) {
                    startOffsetUs = (long) (parseDoubleAttr(line, REGEX_TIME_OFFSET) * 1000000.0d);
                    tags = tags3;
                    hasEndTag = hasEndTag2;
                    hasIndependentSegmentsTag = hasIndependentSegmentsTag2;
                    targetDurationUs = targetDurationUs;
                } else {
                    long targetDurationUs2 = targetDurationUs;
                    if (line.startsWith(TAG_INIT_SEGMENT)) {
                        String uri = parseStringAttr(line, REGEX_URI, variableDefinitions);
                        String byteRange = parseOptionalStringAttr(line, REGEX_ATTR_BYTERANGE, variableDefinitions);
                        if (byteRange != null) {
                            String[] splitByteRange = byteRange.split("@");
                            segmentByteRangeLength = Long.parseLong(splitByteRange[0]);
                            if (splitByteRange.length > 1) {
                                segmentByteRangeOffset = Long.parseLong(splitByteRange[1]);
                            }
                        }
                        if (fullSegmentEncryptionKeyUri != null && fullSegmentEncryptionIV == null) {
                            throw new ParserException("The encryption IV attribute must be present when an initialization segment is encrypted with METHOD=AES-128.");
                        }
                        initializationSegment = new HlsMediaPlaylist.Segment(uri, segmentByteRangeOffset, segmentByteRangeLength, fullSegmentEncryptionKeyUri, fullSegmentEncryptionIV);
                        segmentByteRangeOffset = 0;
                        segmentByteRangeLength = -1;
                        tags = tags3;
                        hasEndTag = hasEndTag2;
                        hasIndependentSegmentsTag = hasIndependentSegmentsTag2;
                        targetDurationUs = targetDurationUs2;
                    } else if (line.startsWith(TAG_TARGET_DURATION)) {
                        targetDurationUs = parseIntAttr(line, REGEX_TARGET_DURATION) * 1000000;
                        tags = tags3;
                        hasEndTag = hasEndTag2;
                        hasIndependentSegmentsTag = hasIndependentSegmentsTag2;
                    } else if (line.startsWith(TAG_MEDIA_SEQUENCE)) {
                        mediaSequence = parseLongAttr(line, REGEX_MEDIA_SEQUENCE);
                        segmentMediaSequence = mediaSequence;
                        tags = tags3;
                        hasEndTag = hasEndTag2;
                        hasIndependentSegmentsTag = hasIndependentSegmentsTag2;
                        targetDurationUs = targetDurationUs2;
                    } else if (line.startsWith(TAG_VERSION)) {
                        version = parseIntAttr(line, REGEX_VERSION);
                        tags = tags3;
                        hasEndTag = hasEndTag2;
                        hasIndependentSegmentsTag = hasIndependentSegmentsTag2;
                        targetDurationUs = targetDurationUs2;
                    } else {
                        if (line.startsWith(TAG_DEFINE)) {
                            String importName = parseOptionalStringAttr(line, REGEX_IMPORT, variableDefinitions);
                            if (importName != null) {
                                String value = hlsMasterPlaylist.variableDefinitions.get(importName);
                                if (value != null) {
                                    variableDefinitions.put(importName, value);
                                }
                            } else {
                                variableDefinitions.put(parseStringAttr(line, REGEX_NAME, variableDefinitions), parseStringAttr(line, REGEX_VALUE, variableDefinitions));
                            }
                        } else if (line.startsWith(TAG_MEDIA_DURATION)) {
                            long segmentDurationUs2 = (long) (parseDoubleAttr(line, REGEX_MEDIA_DURATION) * 1000000.0d);
                            segmentTitle = parseOptionalStringAttr(line, REGEX_MEDIA_TITLE, "", variableDefinitions);
                            segmentDurationUs = segmentDurationUs2;
                            tags = tags3;
                            hasEndTag = hasEndTag2;
                            hasIndependentSegmentsTag = hasIndependentSegmentsTag2;
                            targetDurationUs = targetDurationUs2;
                        } else if (line.startsWith(TAG_KEY)) {
                            String method = parseStringAttr(line, REGEX_METHOD, variableDefinitions);
                            String keyFormat = parseOptionalStringAttr(line, REGEX_KEYFORMAT, KEYFORMAT_IDENTITY, variableDefinitions);
                            if (METHOD_NONE.equals(method)) {
                                currentSchemeDatas2.clear();
                                cachedDrmInitData2 = null;
                                fullSegmentEncryptionKeyUri = null;
                                fullSegmentEncryptionIV = null;
                                currentSchemeDatas = currentSchemeDatas2;
                            } else {
                                String fullSegmentEncryptionIV2 = parseOptionalStringAttr(line, REGEX_IV, variableDefinitions);
                                if (KEYFORMAT_IDENTITY.equals(keyFormat)) {
                                    if (!METHOD_AES_128.equals(method)) {
                                        fullSegmentEncryptionIV = fullSegmentEncryptionIV2;
                                        fullSegmentEncryptionKeyUri = null;
                                        currentSchemeDatas = currentSchemeDatas2;
                                    } else {
                                        fullSegmentEncryptionIV = fullSegmentEncryptionIV2;
                                        fullSegmentEncryptionKeyUri = parseStringAttr(line, REGEX_URI, variableDefinitions);
                                        currentSchemeDatas = currentSchemeDatas2;
                                    }
                                } else {
                                    if (encryptionScheme == null) {
                                        encryptionScheme = parseEncryptionScheme(method);
                                    }
                                    DrmInitData.SchemeData schemeData = parseDrmSchemeData(line, keyFormat, variableDefinitions);
                                    if (schemeData != null) {
                                        currentSchemeDatas = currentSchemeDatas2;
                                        currentSchemeDatas.put(keyFormat, schemeData);
                                        fullSegmentEncryptionKeyUri = null;
                                        cachedDrmInitData2 = null;
                                        fullSegmentEncryptionIV = fullSegmentEncryptionIV2;
                                    } else {
                                        currentSchemeDatas = currentSchemeDatas2;
                                        fullSegmentEncryptionKeyUri = null;
                                        fullSegmentEncryptionIV = fullSegmentEncryptionIV2;
                                    }
                                }
                            }
                            currentSchemeDatas2 = currentSchemeDatas;
                            tags = tags3;
                            hasEndTag = hasEndTag2;
                            hasIndependentSegmentsTag = hasIndependentSegmentsTag2;
                            targetDurationUs = targetDurationUs2;
                            hlsMasterPlaylist = masterPlaylist;
                        } else {
                            TreeMap<String, DrmInitData.SchemeData> currentSchemeDatas3 = currentSchemeDatas2;
                            if (line.startsWith(TAG_BYTERANGE)) {
                                String[] splitByteRange2 = parseStringAttr(line, REGEX_BYTERANGE, variableDefinitions).split("@");
                                segmentByteRangeLength = Long.parseLong(splitByteRange2[0]);
                                if (splitByteRange2.length > 1) {
                                    segmentByteRangeOffset = Long.parseLong(splitByteRange2[1]);
                                }
                                currentSchemeDatas2 = currentSchemeDatas3;
                                tags = tags3;
                                hasEndTag = hasEndTag2;
                                hasIndependentSegmentsTag = hasIndependentSegmentsTag2;
                                targetDurationUs = targetDurationUs2;
                                hlsMasterPlaylist = masterPlaylist;
                            } else if (line.startsWith(TAG_DISCONTINUITY_SEQUENCE)) {
                                hasDiscontinuitySequence = true;
                                playlistDiscontinuitySequence = Integer.parseInt(line.substring(line.indexOf(58) + 1));
                                currentSchemeDatas2 = currentSchemeDatas3;
                                tags = tags3;
                                hasEndTag = hasEndTag2;
                                hasIndependentSegmentsTag = hasIndependentSegmentsTag2;
                                targetDurationUs = targetDurationUs2;
                                hlsMasterPlaylist = masterPlaylist;
                            } else if (line.equals(TAG_DISCONTINUITY)) {
                                relativeDiscontinuitySequence++;
                                currentSchemeDatas2 = currentSchemeDatas3;
                                tags = tags3;
                                hasEndTag = hasEndTag2;
                                hasIndependentSegmentsTag = hasIndependentSegmentsTag2;
                                targetDurationUs = targetDurationUs2;
                                hlsMasterPlaylist = masterPlaylist;
                            } else if (line.startsWith(TAG_PROGRAM_DATE_TIME)) {
                                if (playlistStartTimeUs != 0) {
                                    currentSchemeDatas2 = currentSchemeDatas3;
                                } else {
                                    long programDatetimeUs = C.msToUs(Util.parseXsDateTime(line.substring(line.indexOf(58) + 1)));
                                    playlistStartTimeUs = programDatetimeUs - segmentStartTimeUs;
                                    currentSchemeDatas2 = currentSchemeDatas3;
                                    tags = tags3;
                                    hasEndTag = hasEndTag2;
                                    hasIndependentSegmentsTag = hasIndependentSegmentsTag2;
                                    targetDurationUs = targetDurationUs2;
                                    hlsMasterPlaylist = masterPlaylist;
                                }
                            } else if (line.equals(TAG_GAP)) {
                                hasGapTag = true;
                                currentSchemeDatas2 = currentSchemeDatas3;
                                tags = tags3;
                                hasEndTag = hasEndTag2;
                                hasIndependentSegmentsTag = hasIndependentSegmentsTag2;
                                targetDurationUs = targetDurationUs2;
                                hlsMasterPlaylist = masterPlaylist;
                            } else if (line.equals(TAG_INDEPENDENT_SEGMENTS)) {
                                hasIndependentSegmentsTag = true;
                                currentSchemeDatas2 = currentSchemeDatas3;
                                tags = tags3;
                                hasEndTag = hasEndTag2;
                                targetDurationUs = targetDurationUs2;
                                hlsMasterPlaylist = masterPlaylist;
                            } else if (line.equals(TAG_ENDLIST)) {
                                currentSchemeDatas2 = currentSchemeDatas3;
                                hasEndTag = true;
                                tags = tags3;
                                hasIndependentSegmentsTag = hasIndependentSegmentsTag2;
                                targetDurationUs = targetDurationUs2;
                                hlsMasterPlaylist = masterPlaylist;
                            } else if (line.startsWith("#")) {
                                currentSchemeDatas2 = currentSchemeDatas3;
                            } else {
                                if (fullSegmentEncryptionKeyUri == null) {
                                    segmentEncryptionIV = null;
                                } else if (fullSegmentEncryptionIV != null) {
                                    segmentEncryptionIV = fullSegmentEncryptionIV;
                                } else {
                                    segmentEncryptionIV = Long.toHexString(segmentMediaSequence);
                                }
                                segmentMediaSequence++;
                                if (segmentByteRangeLength == -1) {
                                    segmentByteRangeOffset = 0;
                                }
                                if (cachedDrmInitData2 != null || currentSchemeDatas3.isEmpty()) {
                                    currentSchemeDatas2 = currentSchemeDatas3;
                                } else {
                                    DrmInitData.SchemeData[] schemeDatas = (DrmInitData.SchemeData[]) currentSchemeDatas3.values().toArray(new DrmInitData.SchemeData[0]);
                                    DrmInitData cachedDrmInitData3 = new DrmInitData(encryptionScheme, schemeDatas);
                                    if (playlistProtectionSchemes != null) {
                                        currentSchemeDatas2 = currentSchemeDatas3;
                                        cachedDrmInitData2 = cachedDrmInitData3;
                                    } else {
                                        DrmInitData.SchemeData[] playlistSchemeDatas = new DrmInitData.SchemeData[schemeDatas.length];
                                        currentSchemeDatas2 = currentSchemeDatas3;
                                        int i = 0;
                                        while (true) {
                                            cachedDrmInitData = cachedDrmInitData3;
                                            if (i >= schemeDatas.length) {
                                                break;
                                            }
                                            playlistSchemeDatas[i] = schemeDatas[i].copyWithData(null);
                                            i++;
                                            cachedDrmInitData3 = cachedDrmInitData;
                                            schemeDatas = schemeDatas;
                                        }
                                        playlistProtectionSchemes = new DrmInitData(encryptionScheme, playlistSchemeDatas);
                                        cachedDrmInitData2 = cachedDrmInitData;
                                    }
                                }
                                segments.add(new HlsMediaPlaylist.Segment(replaceVariableReferences(line, variableDefinitions), initializationSegment, segmentTitle, segmentDurationUs, relativeDiscontinuitySequence, segmentStartTimeUs, cachedDrmInitData2, fullSegmentEncryptionKeyUri, segmentEncryptionIV, segmentByteRangeOffset, segmentByteRangeLength, hasGapTag));
                                segmentStartTimeUs += segmentDurationUs;
                                segmentDurationUs = 0;
                                segmentTitle = "";
                                if (segmentByteRangeLength != -1) {
                                    segmentByteRangeOffset += segmentByteRangeLength;
                                }
                                segmentByteRangeLength = -1;
                                hasGapTag = false;
                                hlsMasterPlaylist = masterPlaylist;
                                tags = tags3;
                                hasEndTag = hasEndTag2;
                                hasIndependentSegmentsTag = hasIndependentSegmentsTag2;
                                targetDurationUs = targetDurationUs2;
                            }
                        }
                        hlsMasterPlaylist = masterPlaylist;
                        tags = tags3;
                        hasEndTag = hasEndTag2;
                        hasIndependentSegmentsTag = hasIndependentSegmentsTag2;
                        targetDurationUs = targetDurationUs2;
                    }
                }
            }
        }
    }

    private static int parseSelectionFlags(String line) {
        int flags = 0;
        if (parseOptionalBooleanAttribute(line, REGEX_DEFAULT, false)) {
            flags = 0 | 1;
        }
        if (parseOptionalBooleanAttribute(line, REGEX_FORCED, false)) {
            flags |= 2;
        }
        if (parseOptionalBooleanAttribute(line, REGEX_AUTOSELECT, false)) {
            return flags | 4;
        }
        return flags;
    }

    private static int parseRoleFlags(String line, Map<String, String> variableDefinitions) {
        String concatenatedCharacteristics = parseOptionalStringAttr(line, REGEX_CHARACTERISTICS, variableDefinitions);
        if (TextUtils.isEmpty(concatenatedCharacteristics)) {
            return 0;
        }
        String[] characteristics = Util.split(concatenatedCharacteristics, ",");
        int roleFlags = 0;
        if (Util.contains(characteristics, "public.accessibility.describes-video")) {
            roleFlags = 0 | 512;
        }
        if (Util.contains(characteristics, "public.accessibility.transcribes-spoken-dialog")) {
            roleFlags |= 4096;
        }
        if (Util.contains(characteristics, "public.accessibility.describes-music-and-sound")) {
            roleFlags |= 1024;
        }
        if (Util.contains(characteristics, "public.easy-to-read")) {
            return roleFlags | 8192;
        }
        return roleFlags;
    }

    private static DrmInitData.SchemeData parseDrmSchemeData(String line, String keyFormat, Map<String, String> variableDefinitions) throws ParserException {
        String keyFormatVersions = parseOptionalStringAttr(line, REGEX_KEYFORMATVERSIONS, IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE, variableDefinitions);
        if (KEYFORMAT_WIDEVINE_PSSH_BINARY.equals(keyFormat)) {
            String uriString = parseStringAttr(line, REGEX_URI, variableDefinitions);
            return new DrmInitData.SchemeData(C.WIDEVINE_UUID, MimeTypes.VIDEO_MP4, Base64.decode(uriString.substring(uriString.indexOf(44)), 0));
        } else if (KEYFORMAT_WIDEVINE_PSSH_JSON.equals(keyFormat)) {
            return new DrmInitData.SchemeData(C.WIDEVINE_UUID, DownloadRequest.TYPE_HLS, Util.getUtf8Bytes(line));
        } else {
            if (KEYFORMAT_PLAYREADY.equals(keyFormat) && IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE.equals(keyFormatVersions)) {
                String uriString2 = parseStringAttr(line, REGEX_URI, variableDefinitions);
                byte[] data = Base64.decode(uriString2.substring(uriString2.indexOf(44)), 0);
                byte[] psshData = PsshAtomUtil.buildPsshAtom(C.PLAYREADY_UUID, data);
                return new DrmInitData.SchemeData(C.PLAYREADY_UUID, MimeTypes.VIDEO_MP4, psshData);
            }
            return null;
        }
    }

    private static String parseEncryptionScheme(String method) {
        if (METHOD_SAMPLE_AES_CENC.equals(method) || METHOD_SAMPLE_AES_CTR.equals(method)) {
            return C.CENC_TYPE_cenc;
        }
        return C.CENC_TYPE_cbcs;
    }

    private static int parseIntAttr(String line, Pattern pattern) throws ParserException {
        return Integer.parseInt(parseStringAttr(line, pattern, Collections.emptyMap()));
    }

    private static int parseOptionalIntAttr(String line, Pattern pattern, int defaultValue) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return defaultValue;
    }

    private static long parseLongAttr(String line, Pattern pattern) throws ParserException {
        return Long.parseLong(parseStringAttr(line, pattern, Collections.emptyMap()));
    }

    private static double parseDoubleAttr(String line, Pattern pattern) throws ParserException {
        return Double.parseDouble(parseStringAttr(line, pattern, Collections.emptyMap()));
    }

    private static String parseStringAttr(String line, Pattern pattern, Map<String, String> variableDefinitions) throws ParserException {
        String value = parseOptionalStringAttr(line, pattern, variableDefinitions);
        if (value != null) {
            return value;
        }
        throw new ParserException("Couldn't match " + pattern.pattern() + " in " + line);
    }

    private static String parseOptionalStringAttr(String line, Pattern pattern, Map<String, String> variableDefinitions) {
        return parseOptionalStringAttr(line, pattern, null, variableDefinitions);
    }

    private static String parseOptionalStringAttr(String line, Pattern pattern, String defaultValue, Map<String, String> variableDefinitions) {
        Matcher matcher = pattern.matcher(line);
        String value = matcher.find() ? matcher.group(1) : defaultValue;
        if (variableDefinitions.isEmpty() || value == null) {
            return value;
        }
        return replaceVariableReferences(value, variableDefinitions);
    }

    private static String replaceVariableReferences(String string, Map<String, String> variableDefinitions) {
        Matcher matcher = REGEX_VARIABLE_REFERENCE.matcher(string);
        StringBuffer stringWithReplacements = new StringBuffer();
        while (matcher.find()) {
            String groupName = matcher.group(1);
            if (variableDefinitions.containsKey(groupName)) {
                matcher.appendReplacement(stringWithReplacements, Matcher.quoteReplacement(variableDefinitions.get(groupName)));
            }
        }
        matcher.appendTail(stringWithReplacements);
        return stringWithReplacements.toString();
    }

    private static boolean parseOptionalBooleanAttribute(String line, Pattern pattern, boolean defaultValue) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1).equals(BOOLEAN_TRUE);
        }
        return defaultValue;
    }

    private static Pattern compileBooleanAttrPattern(String attribute) {
        return Pattern.compile(attribute + "=(" + BOOLEAN_FALSE + "|" + BOOLEAN_TRUE + ")");
    }

    /* loaded from: classes3.dex */
    public static class LineIterator {
        private final Queue<String> extraLines;
        private String next;
        private final BufferedReader reader;

        public LineIterator(Queue<String> extraLines, BufferedReader reader) {
            this.extraLines = extraLines;
            this.reader = reader;
        }

        @EnsuresNonNullIf(expression = {"next"}, result = true)
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
                if (readLine != null) {
                    trim = readLine.trim();
                    this.next = trim;
                } else {
                    return false;
                }
            } while (trim.isEmpty());
            return true;
        }

        public String next() throws IOException {
            if (hasNext()) {
                String result = this.next;
                this.next = null;
                return result;
            }
            throw new NoSuchElementException();
        }
    }
}
