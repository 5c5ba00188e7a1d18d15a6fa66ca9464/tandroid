package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class PullForegroundDrawable$$ExternalSyntheticLambda4 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ PullForegroundDrawable f$0;

    public /* synthetic */ PullForegroundDrawable$$ExternalSyntheticLambda4(PullForegroundDrawable pullForegroundDrawable) {
        this.f$0 = pullForegroundDrawable;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$startOutAnimation$6(valueAnimator);
    }
}
