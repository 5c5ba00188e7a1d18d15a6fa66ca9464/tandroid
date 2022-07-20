package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class PremiumPreviewFragment$$ExternalSyntheticLambda15 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ PremiumPreviewFragment f$0;

    public /* synthetic */ PremiumPreviewFragment$$ExternalSyntheticLambda15(PremiumPreviewFragment premiumPreviewFragment) {
        this.f$0 = premiumPreviewFragment;
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
