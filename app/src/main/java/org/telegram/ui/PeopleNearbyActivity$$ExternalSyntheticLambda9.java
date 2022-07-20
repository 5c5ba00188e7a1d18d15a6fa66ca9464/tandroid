package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class PeopleNearbyActivity$$ExternalSyntheticLambda9 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ PeopleNearbyActivity f$0;

    public /* synthetic */ PeopleNearbyActivity$$ExternalSyntheticLambda9(PeopleNearbyActivity peopleNearbyActivity) {
        this.f$0 = peopleNearbyActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$10();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
