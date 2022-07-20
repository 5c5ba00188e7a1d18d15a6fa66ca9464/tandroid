package org.telegram.ui.Components.Premium;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class StarParticlesView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ StarParticlesView f$0;

    public /* synthetic */ StarParticlesView$$ExternalSyntheticLambda0(StarParticlesView starParticlesView) {
        this.f$0 = starParticlesView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$flingParticles$0(valueAnimator);
    }
}
