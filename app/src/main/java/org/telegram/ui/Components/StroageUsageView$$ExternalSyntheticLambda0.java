package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class StroageUsageView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ StroageUsageView f$0;

    public /* synthetic */ StroageUsageView$$ExternalSyntheticLambda0(StroageUsageView stroageUsageView) {
        this.f$0 = stroageUsageView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setStorageUsage$0(valueAnimator);
    }
}
