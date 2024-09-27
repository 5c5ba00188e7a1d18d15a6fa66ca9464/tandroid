package com.google.android.exoplayer2.source;

import java.util.List;
/* loaded from: classes.dex */
public interface CompositeSequenceableLoaderFactory {
    SequenceableLoader create(List list, List list2);

    SequenceableLoader createCompositeSequenceableLoader(SequenceableLoader... sequenceableLoaderArr);

    SequenceableLoader empty();
}
