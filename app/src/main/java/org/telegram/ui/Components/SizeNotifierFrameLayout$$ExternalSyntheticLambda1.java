package org.telegram.ui.Components;

import org.telegram.ui.Components.WallpaperParallaxEffect;
/* loaded from: classes3.dex */
public final /* synthetic */ class SizeNotifierFrameLayout$$ExternalSyntheticLambda1 implements WallpaperParallaxEffect.Callback {
    public final /* synthetic */ SizeNotifierFrameLayout f$0;

    public /* synthetic */ SizeNotifierFrameLayout$$ExternalSyntheticLambda1(SizeNotifierFrameLayout sizeNotifierFrameLayout) {
        this.f$0 = sizeNotifierFrameLayout;
    }

    @Override // org.telegram.ui.Components.WallpaperParallaxEffect.Callback
    public final void onOffsetsChanged(int i, int i2, float f) {
        this.f$0.lambda$setBackgroundImage$0(i, i2, f);
    }
}
