package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class InviteContactsActivity$$ExternalSyntheticLambda2 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ InviteContactsActivity f$0;

    public /* synthetic */ InviteContactsActivity$$ExternalSyntheticLambda2(InviteContactsActivity inviteContactsActivity) {
        this.f$0 = inviteContactsActivity;
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
