package com.google.android.exoplayer2.source.hls.playlist;

import com.google.android.exoplayer2.upstream.ParsingLoadable;
/* loaded from: classes.dex */
public interface HlsPlaylistParserFactory {
    ParsingLoadable.Parser createPlaylistParser();

    ParsingLoadable.Parser createPlaylistParser(HlsMultivariantPlaylist hlsMultivariantPlaylist, HlsMediaPlaylist hlsMediaPlaylist);
}
