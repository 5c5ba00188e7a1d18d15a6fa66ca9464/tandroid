package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ScrollableHorizontalScrollView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ScrollableHorizontalScrollView f$0;

    public /* synthetic */ ScrollableHorizontalScrollView$$ExternalSyntheticLambda0(ScrollableHorizontalScrollView scrollableHorizontalScrollView) {
        this.f$0 = scrollableHorizontalScrollView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$scrollTo$0(valueAnimator);
    }
}
