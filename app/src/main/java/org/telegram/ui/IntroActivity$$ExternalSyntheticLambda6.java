package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class IntroActivity$$ExternalSyntheticLambda6 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ IntroActivity f$0;

    public /* synthetic */ IntroActivity$$ExternalSyntheticLambda6(IntroActivity introActivity) {
        this.f$0 = introActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$5();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
