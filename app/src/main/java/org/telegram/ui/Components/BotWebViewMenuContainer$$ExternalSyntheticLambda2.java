package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class BotWebViewMenuContainer$$ExternalSyntheticLambda2 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ BotWebViewMenuContainer f$0;

    public /* synthetic */ BotWebViewMenuContainer$$ExternalSyntheticLambda2(BotWebViewMenuContainer botWebViewMenuContainer) {
        this.f$0 = botWebViewMenuContainer;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onPanTransitionStart$13(valueAnimator);
    }
}
