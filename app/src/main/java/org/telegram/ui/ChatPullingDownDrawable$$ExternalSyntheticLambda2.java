package org.telegram.ui;

import android.animation.ValueAnimator;
import android.view.View;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatPullingDownDrawable$$ExternalSyntheticLambda2 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChatPullingDownDrawable f$0;
    public final /* synthetic */ View f$1;

    public /* synthetic */ ChatPullingDownDrawable$$ExternalSyntheticLambda2(ChatPullingDownDrawable chatPullingDownDrawable, View view) {
        this.f$0 = chatPullingDownDrawable;
        this.f$1 = view;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$showReleaseState$4(this.f$1, valueAnimator);
    }
}
