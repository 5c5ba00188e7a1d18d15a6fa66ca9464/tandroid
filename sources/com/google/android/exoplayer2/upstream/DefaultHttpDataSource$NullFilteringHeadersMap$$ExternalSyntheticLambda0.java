package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.common.base.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultHttpDataSource$NullFilteringHeadersMap$$ExternalSyntheticLambda0 implements Predicate {
    public static final /* synthetic */ DefaultHttpDataSource$NullFilteringHeadersMap$$ExternalSyntheticLambda0 INSTANCE = new DefaultHttpDataSource$NullFilteringHeadersMap$$ExternalSyntheticLambda0();

    private /* synthetic */ DefaultHttpDataSource$NullFilteringHeadersMap$$ExternalSyntheticLambda0() {
    }

    @Override // com.google.common.base.Predicate
    public final boolean apply(Object obj) {
        boolean lambda$keySet$0;
        lambda$keySet$0 = DefaultHttpDataSource.NullFilteringHeadersMap.lambda$keySet$0((String) obj);
        return lambda$keySet$0;
    }
}
