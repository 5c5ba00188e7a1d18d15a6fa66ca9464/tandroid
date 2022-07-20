package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class PullForegroundDrawable$$ExternalSyntheticLambda3 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ PullForegroundDrawable f$0;

    public /* synthetic */ PullForegroundDrawable$$ExternalSyntheticLambda3(PullForegroundDrawable pullForegroundDrawable) {
        this.f$0 = pullForegroundDrawable;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$colorize$4(valueAnimator);
    }
}
