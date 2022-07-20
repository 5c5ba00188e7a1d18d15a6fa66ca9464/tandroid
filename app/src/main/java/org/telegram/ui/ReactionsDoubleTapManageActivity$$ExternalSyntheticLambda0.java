package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class ReactionsDoubleTapManageActivity$$ExternalSyntheticLambda0 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ ReactionsDoubleTapManageActivity f$0;

    public /* synthetic */ ReactionsDoubleTapManageActivity$$ExternalSyntheticLambda0(ReactionsDoubleTapManageActivity reactionsDoubleTapManageActivity) {
        this.f$0 = reactionsDoubleTapManageActivity;
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
