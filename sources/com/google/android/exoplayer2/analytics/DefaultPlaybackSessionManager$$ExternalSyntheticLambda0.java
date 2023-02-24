package com.google.android.exoplayer2.analytics;

import com.google.common.base.Supplier;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultPlaybackSessionManager$$ExternalSyntheticLambda0 implements Supplier {
    public static final /* synthetic */ DefaultPlaybackSessionManager$$ExternalSyntheticLambda0 INSTANCE = new DefaultPlaybackSessionManager$$ExternalSyntheticLambda0();

    private /* synthetic */ DefaultPlaybackSessionManager$$ExternalSyntheticLambda0() {
    }

    @Override // com.google.common.base.Supplier
    public final Object get() {
        String generateDefaultSessionId;
        generateDefaultSessionId = DefaultPlaybackSessionManager.generateDefaultSessionId();
        return generateDefaultSessionId;
    }
}
