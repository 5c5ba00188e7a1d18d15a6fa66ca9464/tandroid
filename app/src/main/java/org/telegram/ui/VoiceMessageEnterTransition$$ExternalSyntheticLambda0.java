package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoiceMessageEnterTransition$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ VoiceMessageEnterTransition f$0;
    public final /* synthetic */ MessageEnterTransitionContainer f$1;

    public /* synthetic */ VoiceMessageEnterTransition$$ExternalSyntheticLambda0(VoiceMessageEnterTransition voiceMessageEnterTransition, MessageEnterTransitionContainer messageEnterTransitionContainer) {
        this.f$0 = voiceMessageEnterTransition;
        this.f$1 = messageEnterTransitionContainer;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$new$0(this.f$1, valueAnimator);
    }
}
