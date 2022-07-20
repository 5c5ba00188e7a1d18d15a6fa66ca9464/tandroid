package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ScrollSlidingTabStrip$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ScrollSlidingTabStrip f$0;

    public /* synthetic */ ScrollSlidingTabStrip$$ExternalSyntheticLambda0(ScrollSlidingTabStrip scrollSlidingTabStrip) {
        this.f$0 = scrollSlidingTabStrip;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$checkLongPress$7(valueAnimator);
    }
}
