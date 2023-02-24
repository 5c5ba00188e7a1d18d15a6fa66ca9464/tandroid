package com.google.android.exoplayer2.extractor.amr;

import android.net.Uri;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import java.util.Map;
/* loaded from: classes.dex */
public final /* synthetic */ class AmrExtractor$$ExternalSyntheticLambda0 implements ExtractorsFactory {
    public static final /* synthetic */ AmrExtractor$$ExternalSyntheticLambda0 INSTANCE = new AmrExtractor$$ExternalSyntheticLambda0();

    private /* synthetic */ AmrExtractor$$ExternalSyntheticLambda0() {
    }

    @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
    public final Extractor[] createExtractors() {
        Extractor[] lambda$static$0;
        lambda$static$0 = AmrExtractor.lambda$static$0();
        return lambda$static$0;
    }

    @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
    public /* synthetic */ Extractor[] createExtractors(Uri uri, Map map) {
        Extractor[] createExtractors;
        createExtractors = createExtractors();
        return createExtractors;
    }
}
