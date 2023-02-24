package com.google.android.exoplayer2.extractor.ogg;

import android.net.Uri;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import java.util.Map;
/* loaded from: classes.dex */
public final /* synthetic */ class OggExtractor$$ExternalSyntheticLambda0 implements ExtractorsFactory {
    public static final /* synthetic */ OggExtractor$$ExternalSyntheticLambda0 INSTANCE = new OggExtractor$$ExternalSyntheticLambda0();

    private /* synthetic */ OggExtractor$$ExternalSyntheticLambda0() {
    }

    @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
    public final Extractor[] createExtractors() {
        Extractor[] lambda$static$0;
        lambda$static$0 = OggExtractor.lambda$static$0();
        return lambda$static$0;
    }

    @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
    public /* synthetic */ Extractor[] createExtractors(Uri uri, Map map) {
        Extractor[] createExtractors;
        createExtractors = createExtractors();
        return createExtractors;
    }
}
