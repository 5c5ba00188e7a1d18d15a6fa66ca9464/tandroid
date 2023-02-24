package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.common.base.Predicate;
import java.util.Map;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultHttpDataSource$NullFilteringHeadersMap$$ExternalSyntheticLambda1 implements Predicate {
    public static final /* synthetic */ DefaultHttpDataSource$NullFilteringHeadersMap$$ExternalSyntheticLambda1 INSTANCE = new DefaultHttpDataSource$NullFilteringHeadersMap$$ExternalSyntheticLambda1();

    private /* synthetic */ DefaultHttpDataSource$NullFilteringHeadersMap$$ExternalSyntheticLambda1() {
    }

    @Override // com.google.common.base.Predicate
    public final boolean apply(Object obj) {
        boolean lambda$entrySet$1;
        lambda$entrySet$1 = DefaultHttpDataSource.NullFilteringHeadersMap.lambda$entrySet$1((Map.Entry) obj);
        return lambda$entrySet$1;
    }
}
