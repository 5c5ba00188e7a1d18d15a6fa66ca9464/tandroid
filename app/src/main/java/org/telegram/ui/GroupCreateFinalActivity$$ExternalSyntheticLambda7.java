package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCreateFinalActivity$$ExternalSyntheticLambda7 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ GroupCreateFinalActivity f$0;

    public /* synthetic */ GroupCreateFinalActivity$$ExternalSyntheticLambda7(GroupCreateFinalActivity groupCreateFinalActivity) {
        this.f$0 = groupCreateFinalActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$9();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
