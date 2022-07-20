package org.telegram.ui;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.ChatActivityEnterView;
/* loaded from: classes3.dex */
public final /* synthetic */ class TextMessageEnterTransition$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ TextMessageEnterTransition f$0;
    public final /* synthetic */ ChatActivityEnterView f$1;
    public final /* synthetic */ MessageEnterTransitionContainer f$2;

    public /* synthetic */ TextMessageEnterTransition$$ExternalSyntheticLambda0(TextMessageEnterTransition textMessageEnterTransition, ChatActivityEnterView chatActivityEnterView, MessageEnterTransitionContainer messageEnterTransitionContainer) {
        this.f$0 = textMessageEnterTransition;
        this.f$1 = chatActivityEnterView;
        this.f$2 = messageEnterTransitionContainer;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$new$0(this.f$1, this.f$2, valueAnimator);
    }
}
