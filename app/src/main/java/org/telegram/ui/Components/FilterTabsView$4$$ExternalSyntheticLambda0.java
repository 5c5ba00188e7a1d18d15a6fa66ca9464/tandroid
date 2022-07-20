package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.FilterTabsView;
/* loaded from: classes3.dex */
public final /* synthetic */ class FilterTabsView$4$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ FilterTabsView.AnonymousClass4 f$0;

    public /* synthetic */ FilterTabsView$4$$ExternalSyntheticLambda0(FilterTabsView.AnonymousClass4 anonymousClass4) {
        this.f$0 = anonymousClass4;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$runPendingAnimations$0(valueAnimator);
    }
}
