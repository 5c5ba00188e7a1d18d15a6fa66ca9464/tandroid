package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import java.lang.reflect.Constructor;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultExtractorsFactory$$ExternalSyntheticLambda0 implements DefaultExtractorsFactory.ExtensionLoader.ConstructorSupplier {
    public static final /* synthetic */ DefaultExtractorsFactory$$ExternalSyntheticLambda0 INSTANCE = new DefaultExtractorsFactory$$ExternalSyntheticLambda0();

    private /* synthetic */ DefaultExtractorsFactory$$ExternalSyntheticLambda0() {
    }

    @Override // com.google.android.exoplayer2.extractor.DefaultExtractorsFactory.ExtensionLoader.ConstructorSupplier
    public final Constructor getConstructor() {
        Constructor midiExtractorConstructor;
        midiExtractorConstructor = DefaultExtractorsFactory.getMidiExtractorConstructor();
        return midiExtractorConstructor;
    }
}
