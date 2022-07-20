package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class QrActivity$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ QrActivity f$0;
    public final /* synthetic */ int[] f$1;

    public /* synthetic */ QrActivity$$ExternalSyntheticLambda1(QrActivity qrActivity, int[] iArr) {
        this.f$0 = qrActivity;
        this.f$1 = iArr;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onItemSelected$8(this.f$1, valueAnimator);
    }
}
