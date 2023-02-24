package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import java.lang.reflect.Constructor;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultExtractorsFactory$$ExternalSyntheticLambda1 implements DefaultExtractorsFactory.ExtensionLoader.ConstructorSupplier {
    public static final /* synthetic */ DefaultExtractorsFactory$$ExternalSyntheticLambda1 INSTANCE = new DefaultExtractorsFactory$$ExternalSyntheticLambda1();

    private /* synthetic */ DefaultExtractorsFactory$$ExternalSyntheticLambda1() {
    }

    @Override // com.google.android.exoplayer2.extractor.DefaultExtractorsFactory.ExtensionLoader.ConstructorSupplier
    public final Constructor getConstructor() {
        Constructor flacExtractorConstructor;
        flacExtractorConstructor = DefaultExtractorsFactory.getFlacExtractorConstructor();
        return flacExtractorConstructor;
    }
}
