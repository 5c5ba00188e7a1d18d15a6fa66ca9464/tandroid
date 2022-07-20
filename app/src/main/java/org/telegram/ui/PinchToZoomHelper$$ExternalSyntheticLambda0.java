package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class PinchToZoomHelper$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ PinchToZoomHelper f$0;

    public /* synthetic */ PinchToZoomHelper$$ExternalSyntheticLambda0(PinchToZoomHelper pinchToZoomHelper) {
        this.f$0 = pinchToZoomHelper;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$finishZoom$0(valueAnimator);
    }
}
