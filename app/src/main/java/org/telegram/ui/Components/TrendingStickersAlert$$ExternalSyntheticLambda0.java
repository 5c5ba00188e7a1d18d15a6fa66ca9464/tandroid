package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class TrendingStickersAlert$$ExternalSyntheticLambda0 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ TrendingStickersLayout f$0;

    public /* synthetic */ TrendingStickersAlert$$ExternalSyntheticLambda0(TrendingStickersLayout trendingStickersLayout) {
        this.f$0 = trendingStickersLayout;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.updateColors();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
