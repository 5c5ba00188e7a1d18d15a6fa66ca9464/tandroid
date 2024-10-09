package com.google.android.exoplayer2.offline;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import java.io.InputStream;
import java.util.List;

/* loaded from: classes.dex */
public final class FilteringManifestParser implements ParsingLoadable.Parser {
    private final ParsingLoadable.Parser parser;
    private final List streamKeys;

    public FilteringManifestParser(ParsingLoadable.Parser parser, List list) {
        this.parser = parser;
        this.streamKeys = list;
    }

    @Override // com.google.android.exoplayer2.upstream.ParsingLoadable.Parser
    public FilterableManifest parse(Uri uri, InputStream inputStream) {
        FilterableManifest filterableManifest = (FilterableManifest) this.parser.parse(uri, inputStream);
        List list = this.streamKeys;
        return (list == null || list.isEmpty()) ? filterableManifest : (FilterableManifest) filterableManifest.copy(this.streamKeys);
    }
}
