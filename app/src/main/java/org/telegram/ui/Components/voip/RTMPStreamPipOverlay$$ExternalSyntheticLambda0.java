package org.telegram.ui.Components.voip;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class RTMPStreamPipOverlay$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ RTMPStreamPipOverlay f$0;

    public /* synthetic */ RTMPStreamPipOverlay$$ExternalSyntheticLambda0(RTMPStreamPipOverlay rTMPStreamPipOverlay) {
        this.f$0 = rTMPStreamPipOverlay;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$toggleControls$5(valueAnimator);
    }
}
