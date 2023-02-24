package com.google.android.exoplayer2;

import android.os.Bundle;
import com.google.android.exoplayer2.Bundleable;
import com.google.android.exoplayer2.Timeline;
/* loaded from: classes.dex */
public final /* synthetic */ class Timeline$Window$$ExternalSyntheticLambda0 implements Bundleable.Creator {
    public static final /* synthetic */ Timeline$Window$$ExternalSyntheticLambda0 INSTANCE = new Timeline$Window$$ExternalSyntheticLambda0();

    private /* synthetic */ Timeline$Window$$ExternalSyntheticLambda0() {
    }

    @Override // com.google.android.exoplayer2.Bundleable.Creator
    public final Bundleable fromBundle(Bundle bundle) {
        Timeline.Window fromBundle;
        fromBundle = Timeline.Window.fromBundle(bundle);
        return fromBundle;
    }
}
