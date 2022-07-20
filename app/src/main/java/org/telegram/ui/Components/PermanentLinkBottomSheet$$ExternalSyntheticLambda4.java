package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class PermanentLinkBottomSheet$$ExternalSyntheticLambda4 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ PermanentLinkBottomSheet f$0;

    public /* synthetic */ PermanentLinkBottomSheet$$ExternalSyntheticLambda4(PermanentLinkBottomSheet permanentLinkBottomSheet) {
        this.f$0 = permanentLinkBottomSheet;
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
