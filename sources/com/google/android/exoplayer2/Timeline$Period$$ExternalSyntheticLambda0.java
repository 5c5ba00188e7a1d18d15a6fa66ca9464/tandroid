package com.google.android.exoplayer2;

import android.os.Bundle;
import com.google.android.exoplayer2.Bundleable;
import com.google.android.exoplayer2.Timeline;
/* loaded from: classes.dex */
public final /* synthetic */ class Timeline$Period$$ExternalSyntheticLambda0 implements Bundleable.Creator {
    public static final /* synthetic */ Timeline$Period$$ExternalSyntheticLambda0 INSTANCE = new Timeline$Period$$ExternalSyntheticLambda0();

    private /* synthetic */ Timeline$Period$$ExternalSyntheticLambda0() {
    }

    @Override // com.google.android.exoplayer2.Bundleable.Creator
    public final Bundleable fromBundle(Bundle bundle) {
        Timeline.Period fromBundle;
        fromBundle = Timeline.Period.fromBundle(bundle);
        return fromBundle;
    }
}
