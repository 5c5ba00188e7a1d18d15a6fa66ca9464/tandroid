package org.telegram.ui.Components.voip;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.voip.VoIPPiPView;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoIPPiPView$FloatingView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ float f$0;

    public /* synthetic */ VoIPPiPView$FloatingView$$ExternalSyntheticLambda0(float f) {
        this.f$0 = f;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        VoIPPiPView.FloatingView.lambda$expand$2(this.f$0, valueAnimator);
    }
}
