package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class NewContactActivity$$ExternalSyntheticLambda7 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ NewContactActivity f$0;

    public /* synthetic */ NewContactActivity$$ExternalSyntheticLambda7(NewContactActivity newContactActivity) {
        this.f$0 = newContactActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$8();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
