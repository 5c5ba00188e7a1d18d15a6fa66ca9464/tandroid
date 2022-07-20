package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class ActionIntroActivity$$ExternalSyntheticLambda8 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ ActionIntroActivity f$0;

    public /* synthetic */ ActionIntroActivity$$ExternalSyntheticLambda8(ActionIntroActivity actionIntroActivity) {
        this.f$0 = actionIntroActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.updateColors();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
