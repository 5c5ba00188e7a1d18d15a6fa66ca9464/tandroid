package com.google.android.exoplayer2.source;

import com.google.common.collect.ImmutableList;
import java.util.List;
/* loaded from: classes.dex */
public final class DefaultCompositeSequenceableLoaderFactory implements CompositeSequenceableLoaderFactory {
    @Override // com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory
    public SequenceableLoader create(List list, List list2) {
        return new CompositeSequenceableLoader(list, list2);
    }

    @Override // com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory
    public SequenceableLoader createCompositeSequenceableLoader(SequenceableLoader... sequenceableLoaderArr) {
        return new CompositeSequenceableLoader(sequenceableLoaderArr);
    }

    @Override // com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory
    public SequenceableLoader empty() {
        return new CompositeSequenceableLoader(ImmutableList.of(), ImmutableList.of());
    }
}
