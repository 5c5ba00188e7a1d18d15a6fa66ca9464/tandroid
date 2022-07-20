package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.ScrollSlidingTabStrip;
/* loaded from: classes3.dex */
public final /* synthetic */ class ScrollSlidingTabStrip$3$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ScrollSlidingTabStrip.AnonymousClass3 f$0;

    public /* synthetic */ ScrollSlidingTabStrip$3$$ExternalSyntheticLambda0(ScrollSlidingTabStrip.AnonymousClass3 anonymousClass3) {
        this.f$0 = anonymousClass3;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$createAnimator$0(valueAnimator);
    }
}
