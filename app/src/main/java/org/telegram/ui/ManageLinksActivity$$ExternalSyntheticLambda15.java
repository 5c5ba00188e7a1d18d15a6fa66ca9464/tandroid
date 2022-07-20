package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class ManageLinksActivity$$ExternalSyntheticLambda15 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ ManageLinksActivity f$0;

    public /* synthetic */ ManageLinksActivity$$ExternalSyntheticLambda15(ManageLinksActivity manageLinksActivity) {
        this.f$0 = manageLinksActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$17();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
