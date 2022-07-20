package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatUsersActivity$$ExternalSyntheticLambda16 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ ChatUsersActivity f$0;

    public /* synthetic */ ChatUsersActivity$$ExternalSyntheticLambda16(ChatUsersActivity chatUsersActivity) {
        this.f$0 = chatUsersActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$18();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
