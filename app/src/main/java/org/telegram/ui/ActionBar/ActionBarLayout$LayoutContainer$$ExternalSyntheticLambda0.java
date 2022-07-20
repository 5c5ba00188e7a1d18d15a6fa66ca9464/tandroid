package org.telegram.ui.ActionBar;

import android.animation.ValueAnimator;
import org.telegram.ui.ActionBar.ActionBarLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ActionBarLayout$LayoutContainer$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ActionBarLayout.LayoutContainer f$0;

    public /* synthetic */ ActionBarLayout$LayoutContainer$$ExternalSyntheticLambda0(ActionBarLayout.LayoutContainer layoutContainer) {
        this.f$0 = layoutContainer;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$processMenuButtonsTouch$0(valueAnimator);
    }
}
