package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ScrollSlidingTabStrip$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ScrollSlidingTabStrip f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ float f$2;

    public /* synthetic */ ScrollSlidingTabStrip$$ExternalSyntheticLambda1(ScrollSlidingTabStrip scrollSlidingTabStrip, boolean z, float f) {
        this.f$0 = scrollSlidingTabStrip;
        this.f$1 = z;
        this.f$2 = f;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$expandStickers$6(this.f$1, this.f$2, valueAnimator);
    }
}
