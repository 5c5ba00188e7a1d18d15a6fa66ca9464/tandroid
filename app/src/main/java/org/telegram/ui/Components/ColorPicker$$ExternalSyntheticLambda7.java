package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class ColorPicker$$ExternalSyntheticLambda7 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ ColorPicker f$0;

    public /* synthetic */ ColorPicker$$ExternalSyntheticLambda7(ColorPicker colorPicker) {
        this.f$0 = colorPicker;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$provideThemeDescriptions$7();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
