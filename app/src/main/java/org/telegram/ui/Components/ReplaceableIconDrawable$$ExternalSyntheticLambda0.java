package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ReplaceableIconDrawable$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ReplaceableIconDrawable f$0;

    public /* synthetic */ ReplaceableIconDrawable$$ExternalSyntheticLambda0(ReplaceableIconDrawable replaceableIconDrawable) {
        this.f$0 = replaceableIconDrawable;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setIcon$0(valueAnimator);
    }
}
