package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallPip$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ GroupCallPip f$0;

    public /* synthetic */ GroupCallPip$$ExternalSyntheticLambda0(GroupCallPip groupCallPip) {
        this.f$0 = groupCallPip;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$pinnedToCenter$3(valueAnimator);
    }
}
