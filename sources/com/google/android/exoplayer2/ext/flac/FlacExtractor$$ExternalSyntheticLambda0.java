package com.google.android.exoplayer2.ext.flac;

import android.net.Uri;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import java.util.Map;
/* loaded from: classes.dex */
public final /* synthetic */ class FlacExtractor$$ExternalSyntheticLambda0 implements ExtractorsFactory {
    public static final /* synthetic */ FlacExtractor$$ExternalSyntheticLambda0 INSTANCE = new FlacExtractor$$ExternalSyntheticLambda0();

    private /* synthetic */ FlacExtractor$$ExternalSyntheticLambda0() {
    }

    @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
    public final Extractor[] createExtractors() {
        Extractor[] lambda$static$0;
        lambda$static$0 = FlacExtractor.lambda$static$0();
        return lambda$static$0;
    }

    @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
    public /* synthetic */ Extractor[] createExtractors(Uri uri, Map map) {
        Extractor[] createExtractors;
        createExtractors = createExtractors();
        return createExtractors;
    }
}
