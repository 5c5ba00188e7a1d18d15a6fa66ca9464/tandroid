package com.google.android.exoplayer2.extractor;

import android.net.Uri;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public interface ExtractorsFactory {
    public static final ExtractorsFactory EMPTY = null;

    Extractor[] createExtractors();

    Extractor[] createExtractors(Uri uri, Map<String, List<String>> map);

    /* loaded from: classes.dex */
    public final /* synthetic */ class -CC {
        static {
            ExtractorsFactory extractorsFactory = ExtractorsFactory.EMPTY;
        }
    }
}
