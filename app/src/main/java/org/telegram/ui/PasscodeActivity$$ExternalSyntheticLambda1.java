package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class PasscodeActivity$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ PasscodeActivity f$0;

    public /* synthetic */ PasscodeActivity$$ExternalSyntheticLambda1(PasscodeActivity passcodeActivity) {
        this.f$0 = passcodeActivity;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setFloatingButtonVisible$14(valueAnimator);
    }
}
