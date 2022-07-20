package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.TranslateAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class TranslateAlert$InlineLoadingTextView$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ TranslateAlert.InlineLoadingTextView f$0;

    public /* synthetic */ TranslateAlert$InlineLoadingTextView$$ExternalSyntheticLambda1(TranslateAlert.InlineLoadingTextView inlineLoadingTextView) {
        this.f$0 = inlineLoadingTextView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$loaded$1(valueAnimator);
    }
}
