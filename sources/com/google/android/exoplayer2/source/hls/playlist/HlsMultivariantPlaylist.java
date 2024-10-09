package com.google.android.exoplayer2.source.hls.playlist;

import android.net.Uri;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.offline.StreamKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public final class HlsMultivariantPlaylist extends HlsPlaylist {
    public static final HlsMultivariantPlaylist EMPTY = new HlsMultivariantPlaylist("", Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), null, Collections.emptyList(), false, Collections.emptyMap(), Collections.emptyList());
    public final List audios;
    public final List closedCaptions;
    public final List mediaPlaylistUrls;
    public final Format muxedAudioFormat;
    public final List muxedCaptionFormats;
    public final List sessionKeyDrmInitData;
    public final List subtitles;
    public final Map variableDefinitions;
    public final List variants;
    public final List videos;

    /* loaded from: classes.dex */
    public static final class Rendition {
        public final Format format;
        public final String groupId;
        public final String name;
        public final Uri url;

        public Rendition(Uri uri, Format format, String str, String str2) {
            this.url = uri;
            this.format = format;
            this.groupId = str;
            this.name = str2;
        }
    }

    /* loaded from: classes.dex */
    public static final class Variant {
        public final String audioGroupId;
        public final String captionGroupId;
        public final Format format;
        public final String subtitleGroupId;
        public final Uri url;
        public final String videoGroupId;

        public Variant(Uri uri, Format format, String str, String str2, String str3, String str4) {
            this.url = uri;
            this.format = format;
            this.videoGroupId = str;
            this.audioGroupId = str2;
            this.subtitleGroupId = str3;
            this.captionGroupId = str4;
        }

        public static Variant createMediaPlaylistVariantUrl(Uri uri) {
            return new Variant(uri, new Format.Builder().setId("0").setContainerMimeType("application/x-mpegURL").build(), null, null, null, null);
        }

        public Variant copyWithFormat(Format format) {
            return new Variant(this.url, format, this.videoGroupId, this.audioGroupId, this.subtitleGroupId, this.captionGroupId);
        }
    }

    public HlsMultivariantPlaylist(String str, List list, List list2, List list3, List list4, List list5, List list6, Format format, List list7, boolean z, Map map, List list8) {
        super(str, list, z);
        this.mediaPlaylistUrls = Collections.unmodifiableList(getMediaPlaylistUrls(list2, list3, list4, list5, list6));
        this.variants = Collections.unmodifiableList(list2);
        this.videos = Collections.unmodifiableList(list3);
        this.audios = Collections.unmodifiableList(list4);
        this.subtitles = Collections.unmodifiableList(list5);
        this.closedCaptions = Collections.unmodifiableList(list6);
        this.muxedAudioFormat = format;
        this.muxedCaptionFormats = list7 != null ? Collections.unmodifiableList(list7) : null;
        this.variableDefinitions = Collections.unmodifiableMap(map);
        this.sessionKeyDrmInitData = Collections.unmodifiableList(list8);
    }

    private static void addMediaPlaylistUrls(List list, List list2) {
        for (int i = 0; i < list.size(); i++) {
            Uri uri = ((Rendition) list.get(i)).url;
            if (uri != null && !list2.contains(uri)) {
                list2.add(uri);
            }
        }
    }

    private static List copyStreams(List list, int i, List list2) {
        ArrayList arrayList = new ArrayList(list2.size());
        for (int i2 = 0; i2 < list.size(); i2++) {
            Object obj = list.get(i2);
            int i3 = 0;
            while (true) {
                if (i3 < list2.size()) {
                    StreamKey streamKey = (StreamKey) list2.get(i3);
                    if (streamKey.groupIndex == i && streamKey.streamIndex == i2) {
                        arrayList.add(obj);
                        break;
                    }
                    i3++;
                }
            }
        }
        return arrayList;
    }

    public static HlsMultivariantPlaylist createSingleVariantMultivariantPlaylist(String str) {
        return new HlsMultivariantPlaylist("", Collections.emptyList(), Collections.singletonList(Variant.createMediaPlaylistVariantUrl(Uri.parse(str))), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), null, null, false, Collections.emptyMap(), Collections.emptyList());
    }

    private static List getMediaPlaylistUrls(List list, List list2, List list3, List list4, List list5) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Uri uri = ((Variant) list.get(i)).url;
            if (!arrayList.contains(uri)) {
                arrayList.add(uri);
            }
        }
        addMediaPlaylistUrls(list2, arrayList);
        addMediaPlaylistUrls(list3, arrayList);
        addMediaPlaylistUrls(list4, arrayList);
        addMediaPlaylistUrls(list5, arrayList);
        return arrayList;
    }

    @Override // com.google.android.exoplayer2.offline.FilterableManifest
    public HlsMultivariantPlaylist copy(List list) {
        return new HlsMultivariantPlaylist(this.baseUri, this.tags, copyStreams(this.variants, 0, list), Collections.emptyList(), copyStreams(this.audios, 1, list), copyStreams(this.subtitles, 2, list), Collections.emptyList(), this.muxedAudioFormat, this.muxedCaptionFormats, this.hasIndependentSegments, this.variableDefinitions, this.sessionKeyDrmInitData);
    }
}
