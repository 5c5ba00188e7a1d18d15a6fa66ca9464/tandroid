package org.telegram.ui;

import org.telegram.ui.Components.WallpaperParallaxEffect;
/* loaded from: classes3.dex */
public final /* synthetic */ class ThemePreviewActivity$$ExternalSyntheticLambda26 implements WallpaperParallaxEffect.Callback {
    public final /* synthetic */ ThemePreviewActivity f$0;

    public /* synthetic */ ThemePreviewActivity$$ExternalSyntheticLambda26(ThemePreviewActivity themePreviewActivity) {
        this.f$0 = themePreviewActivity;
    }

    @Override // org.telegram.ui.Components.WallpaperParallaxEffect.Callback
    public final void onOffsetsChanged(int i, int i2, float f) {
        this.f$0.lambda$createView$5(i, i2, f);
    }
}
