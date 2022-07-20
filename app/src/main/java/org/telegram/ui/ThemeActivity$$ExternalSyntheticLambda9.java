package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class ThemeActivity$$ExternalSyntheticLambda9 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ ThemeActivity f$0;

    public /* synthetic */ ThemeActivity$$ExternalSyntheticLambda9(ThemeActivity themeActivity) {
        this.f$0 = themeActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$10();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
