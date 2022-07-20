package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoIPFragment$$ExternalSyntheticLambda2 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ VoIPFragment f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ float f$2;
    public final /* synthetic */ float f$3;

    public /* synthetic */ VoIPFragment$$ExternalSyntheticLambda2(VoIPFragment voIPFragment, float f, float f2, float f3) {
        this.f$0 = voIPFragment;
        this.f$1 = f;
        this.f$2 = f2;
        this.f$3 = f3;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$finishZoom$12(this.f$1, this.f$2, this.f$3, valueAnimator);
    }
}
