package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileNotificationsActivity$$ExternalSyntheticLambda5 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ ProfileNotificationsActivity f$0;

    public /* synthetic */ ProfileNotificationsActivity$$ExternalSyntheticLambda5(ProfileNotificationsActivity profileNotificationsActivity) {
        this.f$0 = profileNotificationsActivity;
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
