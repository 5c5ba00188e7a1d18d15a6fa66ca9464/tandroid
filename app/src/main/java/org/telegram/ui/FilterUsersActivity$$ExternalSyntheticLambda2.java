package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class FilterUsersActivity$$ExternalSyntheticLambda2 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ FilterUsersActivity f$0;

    public /* synthetic */ FilterUsersActivity$$ExternalSyntheticLambda2(FilterUsersActivity filterUsersActivity) {
        this.f$0 = filterUsersActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$3();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
