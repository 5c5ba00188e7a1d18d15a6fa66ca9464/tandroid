package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class SenderSelectView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SenderSelectView f$0;

    public /* synthetic */ SenderSelectView$$ExternalSyntheticLambda0(SenderSelectView senderSelectView) {
        this.f$0 = senderSelectView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setProgress$4(valueAnimator);
    }
}
