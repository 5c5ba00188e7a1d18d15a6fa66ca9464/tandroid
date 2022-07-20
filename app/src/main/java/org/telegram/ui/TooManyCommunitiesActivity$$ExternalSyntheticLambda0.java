package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class TooManyCommunitiesActivity$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ TooManyCommunitiesActivity f$0;

    public /* synthetic */ TooManyCommunitiesActivity$$ExternalSyntheticLambda0(TooManyCommunitiesActivity tooManyCommunitiesActivity) {
        this.f$0 = tooManyCommunitiesActivity;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$loadInactiveChannels$3(valueAnimator);
    }
}
