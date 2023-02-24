package com.google.android.exoplayer2.source.ads;

import android.os.Bundle;
import com.google.android.exoplayer2.Bundleable;
import com.google.android.exoplayer2.source.ads.AdPlaybackState;
/* loaded from: classes.dex */
public final /* synthetic */ class AdPlaybackState$AdGroup$$ExternalSyntheticLambda0 implements Bundleable.Creator {
    public static final /* synthetic */ AdPlaybackState$AdGroup$$ExternalSyntheticLambda0 INSTANCE = new AdPlaybackState$AdGroup$$ExternalSyntheticLambda0();

    private /* synthetic */ AdPlaybackState$AdGroup$$ExternalSyntheticLambda0() {
    }

    @Override // com.google.android.exoplayer2.Bundleable.Creator
    public final Bundleable fromBundle(Bundle bundle) {
        AdPlaybackState.AdGroup fromBundle;
        fromBundle = AdPlaybackState.AdGroup.fromBundle(bundle);
        return fromBundle;
    }
}
