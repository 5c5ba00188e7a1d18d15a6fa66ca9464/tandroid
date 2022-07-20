package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class LinkEditActivity$$ExternalSyntheticLambda9 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ LinkEditActivity f$0;

    public /* synthetic */ LinkEditActivity$$ExternalSyntheticLambda9(LinkEditActivity linkEditActivity) {
        this.f$0 = linkEditActivity;
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
