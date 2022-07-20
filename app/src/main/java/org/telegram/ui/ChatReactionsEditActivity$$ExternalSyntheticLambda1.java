package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatReactionsEditActivity$$ExternalSyntheticLambda1 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ ChatReactionsEditActivity f$0;

    public /* synthetic */ ChatReactionsEditActivity$$ExternalSyntheticLambda1(ChatReactionsEditActivity chatReactionsEditActivity) {
        this.f$0 = chatReactionsEditActivity;
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
