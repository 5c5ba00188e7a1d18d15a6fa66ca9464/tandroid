package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class MediaActivity$$ExternalSyntheticLambda0 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ MediaActivity f$0;

    public /* synthetic */ MediaActivity$$ExternalSyntheticLambda0(MediaActivity mediaActivity) {
        this.f$0 = mediaActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$0();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
