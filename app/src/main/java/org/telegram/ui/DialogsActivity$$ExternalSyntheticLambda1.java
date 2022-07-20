package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsActivity$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ DialogsActivity f$0;

    public /* synthetic */ DialogsActivity$$ExternalSyntheticLambda1(DialogsActivity dialogsActivity) {
        this.f$0 = dialogsActivity;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$showSearch$20(valueAnimator);
    }
}
