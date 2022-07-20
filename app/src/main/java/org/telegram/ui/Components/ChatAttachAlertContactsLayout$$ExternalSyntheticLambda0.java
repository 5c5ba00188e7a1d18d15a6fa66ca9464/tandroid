package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertContactsLayout$$ExternalSyntheticLambda0 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ ChatAttachAlertContactsLayout f$0;

    public /* synthetic */ ChatAttachAlertContactsLayout$$ExternalSyntheticLambda0(ChatAttachAlertContactsLayout chatAttachAlertContactsLayout) {
        this.f$0 = chatAttachAlertContactsLayout;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$2();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
