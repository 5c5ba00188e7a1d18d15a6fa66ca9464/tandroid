package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class AvatarsDarawable$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ AvatarsDarawable f$0;

    public /* synthetic */ AvatarsDarawable$$ExternalSyntheticLambda0(AvatarsDarawable avatarsDarawable) {
        this.f$0 = avatarsDarawable;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$commitTransition$0(valueAnimator);
    }
}
