package com.google.android.exoplayer2.source.hls;

import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMultivariantPlaylist;
/* loaded from: classes.dex */
public final class HlsManifest {
    /* JADX INFO: Access modifiers changed from: package-private */
    public HlsManifest(HlsMultivariantPlaylist hlsMultivariantPlaylist, HlsMediaPlaylist hlsMediaPlaylist) {
        new HlsMasterPlaylist(hlsMultivariantPlaylist.baseUri, hlsMultivariantPlaylist.tags, hlsMultivariantPlaylist.variants, hlsMultivariantPlaylist.videos, hlsMultivariantPlaylist.audios, hlsMultivariantPlaylist.subtitles, hlsMultivariantPlaylist.closedCaptions, hlsMultivariantPlaylist.muxedAudioFormat, hlsMultivariantPlaylist.muxedCaptionFormats, hlsMultivariantPlaylist.hasIndependentSegments, hlsMultivariantPlaylist.variableDefinitions, hlsMultivariantPlaylist.sessionKeyDrmInitData);
    }
}
