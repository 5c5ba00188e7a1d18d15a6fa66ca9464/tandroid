package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class KeyboardHideHelper$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ KeyboardHideHelper f$0;

    public /* synthetic */ KeyboardHideHelper$$ExternalSyntheticLambda0(KeyboardHideHelper keyboardHideHelper) {
        this.f$0 = keyboardHideHelper;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onTouch$0(valueAnimator);
    }
}
