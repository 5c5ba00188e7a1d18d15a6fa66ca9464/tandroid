package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import androidx.core.util.Consumer;
import org.telegram.ui.Components.ReactionsContainerLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ReactionsContainerLayout$LeftRightShadowsListener$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ Consumer f$0;

    public /* synthetic */ ReactionsContainerLayout$LeftRightShadowsListener$$ExternalSyntheticLambda0(Consumer consumer) {
        this.f$0 = consumer;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ReactionsContainerLayout.LeftRightShadowsListener.lambda$startAnimator$4(this.f$0, valueAnimator);
    }
}
