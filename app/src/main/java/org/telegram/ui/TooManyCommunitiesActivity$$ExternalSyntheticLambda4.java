package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class TooManyCommunitiesActivity$$ExternalSyntheticLambda4 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ TooManyCommunitiesActivity f$0;

    public /* synthetic */ TooManyCommunitiesActivity$$ExternalSyntheticLambda4(TooManyCommunitiesActivity tooManyCommunitiesActivity) {
        this.f$0 = tooManyCommunitiesActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$6();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
