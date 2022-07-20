package org.telegram.ui;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class StatisticActivity$$ExternalSyntheticLambda6 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ StatisticActivity f$0;

    public /* synthetic */ StatisticActivity$$ExternalSyntheticLambda6(StatisticActivity statisticActivity) {
        this.f$0 = statisticActivity;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.lambda$getThemeDescriptions$8();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
