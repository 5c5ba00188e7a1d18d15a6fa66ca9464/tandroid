package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class AudioPlayerAlert$$ExternalSyntheticLambda10 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ AudioPlayerAlert f$0;

    public /* synthetic */ AudioPlayerAlert$$ExternalSyntheticLambda10(AudioPlayerAlert audioPlayerAlert) {
        this.f$0 = audioPlayerAlert;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$11();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
