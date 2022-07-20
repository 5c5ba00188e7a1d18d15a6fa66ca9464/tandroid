package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProxySettingsActivity$$ExternalSyntheticLambda7 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ ProxySettingsActivity f$0;

    public /* synthetic */ ProxySettingsActivity$$ExternalSyntheticLambda7(ProxySettingsActivity proxySettingsActivity) {
        this.f$0 = proxySettingsActivity;
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
