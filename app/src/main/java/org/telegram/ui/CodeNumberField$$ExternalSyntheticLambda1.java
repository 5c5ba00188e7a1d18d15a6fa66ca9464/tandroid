package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class CodeNumberField$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ CodeNumberField f$0;

    public /* synthetic */ CodeNumberField$$ExternalSyntheticLambda1(CodeNumberField codeNumberField) {
        this.f$0 = codeNumberField;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$startEnterAnimation$9(valueAnimator);
    }
}
