package com.google.android.exoplayer2;

import android.os.Bundle;
import com.google.android.exoplayer2.Bundleable;
import com.google.android.exoplayer2.MediaItem;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaItem$ClippingConfiguration$$ExternalSyntheticLambda0 implements Bundleable.Creator {
    public static final /* synthetic */ MediaItem$ClippingConfiguration$$ExternalSyntheticLambda0 INSTANCE = new MediaItem$ClippingConfiguration$$ExternalSyntheticLambda0();

    private /* synthetic */ MediaItem$ClippingConfiguration$$ExternalSyntheticLambda0() {
    }

    @Override // com.google.android.exoplayer2.Bundleable.Creator
    public final Bundleable fromBundle(Bundle bundle) {
        MediaItem.ClippingProperties lambda$static$0;
        lambda$static$0 = MediaItem.ClippingConfiguration.lambda$static$0(bundle);
        return lambda$static$0;
    }
}
