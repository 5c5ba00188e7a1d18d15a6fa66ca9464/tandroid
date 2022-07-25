package com.google.android.exoplayer2.offline;

import android.net.Uri;
import com.google.android.exoplayer2.offline.FilterableManifest;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
/* loaded from: classes.dex */
public final class FilteringManifestParser<T extends FilterableManifest<T>> implements ParsingLoadable.Parser<T> {
    private final ParsingLoadable.Parser<? extends T> parser;
    private final List<StreamKey> streamKeys;

    public FilteringManifestParser(ParsingLoadable.Parser<? extends T> parser, List<StreamKey> list) {
        this.parser = parser;
        this.streamKeys = list;
    }

    @Override // com.google.android.exoplayer2.upstream.ParsingLoadable.Parser
    /* renamed from: parse */
    public T mo164parse(Uri uri, InputStream inputStream) throws IOException {
        T mo164parse = this.parser.mo164parse(uri, inputStream);
        List<StreamKey> list = this.streamKeys;
        return (list == null || list.isEmpty()) ? mo164parse : (T) mo164parse.mo163copy(this.streamKeys);
    }
}
