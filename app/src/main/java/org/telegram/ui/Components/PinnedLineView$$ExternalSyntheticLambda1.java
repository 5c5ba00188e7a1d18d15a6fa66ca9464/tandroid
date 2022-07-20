package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class PinnedLineView$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ PinnedLineView f$0;

    public /* synthetic */ PinnedLineView$$ExternalSyntheticLambda1(PinnedLineView pinnedLineView) {
        this.f$0 = pinnedLineView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$set$1(valueAnimator);
    }
}
