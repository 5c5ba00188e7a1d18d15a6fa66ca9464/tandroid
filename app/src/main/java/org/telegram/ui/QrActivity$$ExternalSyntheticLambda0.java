package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class QrActivity$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ QrActivity f$0;

    public /* synthetic */ QrActivity$$ExternalSyntheticLambda0(QrActivity qrActivity) {
        this.f$0 = qrActivity;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onPatternLoaded$4(valueAnimator);
    }
}
