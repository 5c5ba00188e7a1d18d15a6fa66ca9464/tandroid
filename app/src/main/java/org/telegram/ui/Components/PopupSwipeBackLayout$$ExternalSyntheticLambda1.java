package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class PopupSwipeBackLayout$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ PopupSwipeBackLayout f$0;

    public /* synthetic */ PopupSwipeBackLayout$$ExternalSyntheticLambda1(PopupSwipeBackLayout popupSwipeBackLayout) {
        this.f$0 = popupSwipeBackLayout;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$animateToState$0(valueAnimator);
    }
}
