package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCreateActivity$$ExternalSyntheticLambda6 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ GroupCreateActivity f$0;

    public /* synthetic */ GroupCreateActivity$$ExternalSyntheticLambda6(GroupCreateActivity groupCreateActivity) {
        this.f$0 = groupCreateActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$7();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
