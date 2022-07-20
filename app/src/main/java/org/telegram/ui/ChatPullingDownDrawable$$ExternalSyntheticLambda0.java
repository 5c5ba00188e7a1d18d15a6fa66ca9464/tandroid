package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatPullingDownDrawable$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChatPullingDownDrawable f$0;

    public /* synthetic */ ChatPullingDownDrawable$$ExternalSyntheticLambda0(ChatPullingDownDrawable chatPullingDownDrawable) {
        this.f$0 = chatPullingDownDrawable;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$runOnAnimationFinish$6(valueAnimator);
    }
}
