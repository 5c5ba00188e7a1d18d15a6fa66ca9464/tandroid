package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatLinkActivity$$ExternalSyntheticLambda17 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ ChatLinkActivity f$0;

    public /* synthetic */ ChatLinkActivity$$ExternalSyntheticLambda17(ChatLinkActivity chatLinkActivity) {
        this.f$0 = chatLinkActivity;
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
