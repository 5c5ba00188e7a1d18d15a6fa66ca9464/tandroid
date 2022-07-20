package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.PasscodeView;
/* loaded from: classes3.dex */
public final /* synthetic */ class PasscodeView$9$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ PasscodeView.AnonymousClass9 f$0;
    public final /* synthetic */ double f$1;

    public /* synthetic */ PasscodeView$9$$ExternalSyntheticLambda0(PasscodeView.AnonymousClass9 anonymousClass9, double d) {
        this.f$0 = anonymousClass9;
        this.f$1 = d;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onGlobalLayout$1(this.f$1, valueAnimator);
    }
}
