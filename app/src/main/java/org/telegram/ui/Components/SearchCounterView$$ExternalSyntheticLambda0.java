package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class SearchCounterView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SearchCounterView f$0;

    public /* synthetic */ SearchCounterView$$ExternalSyntheticLambda0(SearchCounterView searchCounterView) {
        this.f$0 = searchCounterView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setCount$0(valueAnimator);
    }
}
