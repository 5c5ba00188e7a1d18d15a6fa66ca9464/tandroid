package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class PrivacyUsersActivity$$ExternalSyntheticLambda1 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ PrivacyUsersActivity f$0;

    public /* synthetic */ PrivacyUsersActivity$$ExternalSyntheticLambda1(PrivacyUsersActivity privacyUsersActivity) {
        this.f$0 = privacyUsersActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$4();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
