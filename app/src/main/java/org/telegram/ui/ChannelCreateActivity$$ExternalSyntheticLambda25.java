package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChannelCreateActivity$$ExternalSyntheticLambda25 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ ChannelCreateActivity f$0;

    public /* synthetic */ ChannelCreateActivity$$ExternalSyntheticLambda25(ChannelCreateActivity channelCreateActivity) {
        this.f$0 = channelCreateActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$25();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
