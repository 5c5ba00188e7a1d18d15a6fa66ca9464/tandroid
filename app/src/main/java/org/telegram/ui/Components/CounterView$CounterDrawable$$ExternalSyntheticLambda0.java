package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.CounterView;
/* loaded from: classes3.dex */
public final /* synthetic */ class CounterView$CounterDrawable$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ CounterView.CounterDrawable f$0;

    public /* synthetic */ CounterView$CounterDrawable$$ExternalSyntheticLambda0(CounterView.CounterDrawable counterDrawable) {
        this.f$0 = counterDrawable;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setCount$0(valueAnimator);
    }
}
