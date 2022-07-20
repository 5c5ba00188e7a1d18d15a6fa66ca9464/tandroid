package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.ThemeDescription;
/* loaded from: classes3.dex */
public final /* synthetic */ class PollVotesAlert$$ExternalSyntheticLambda5 implements ThemeDescription.ThemeDescriptionDelegate {
    public final /* synthetic */ PollVotesAlert f$0;

    public /* synthetic */ PollVotesAlert$$ExternalSyntheticLambda5(PollVotesAlert pollVotesAlert) {
        this.f$0 = pollVotesAlert;
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public final void didSetColor() {
        this.f$0.updatePlaceholder();
    }

    @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
    public /* synthetic */ void onAnimationProgress(float f) {
        ThemeDescription.ThemeDescriptionDelegate.CC.$default$onAnimationProgress(this, f);
    }
}
