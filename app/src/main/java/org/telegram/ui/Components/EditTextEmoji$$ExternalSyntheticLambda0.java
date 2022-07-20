package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class EditTextEmoji$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ EditTextEmoji f$0;

    public /* synthetic */ EditTextEmoji$$ExternalSyntheticLambda0(EditTextEmoji editTextEmoji) {
        this.f$0 = editTextEmoji;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$showPopup$2(valueAnimator);
    }
}
