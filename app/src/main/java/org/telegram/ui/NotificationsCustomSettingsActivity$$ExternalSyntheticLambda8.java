package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class NotificationsCustomSettingsActivity$$ExternalSyntheticLambda8 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ NotificationsCustomSettingsActivity f$0;

    public /* synthetic */ NotificationsCustomSettingsActivity$$ExternalSyntheticLambda8(NotificationsCustomSettingsActivity notificationsCustomSettingsActivity) {
        this.f$0 = notificationsCustomSettingsActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$11();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
