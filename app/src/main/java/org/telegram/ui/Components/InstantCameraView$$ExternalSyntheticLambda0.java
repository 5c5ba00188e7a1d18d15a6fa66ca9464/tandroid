package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class InstantCameraView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ InstantCameraView f$0;

    public /* synthetic */ InstantCameraView$$ExternalSyntheticLambda0(InstantCameraView instantCameraView) {
        this.f$0 = instantCameraView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$finishZoom$6(valueAnimator);
    }
}
