package org.telegram.ui.Charts;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChartPickerDelegate$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChartPickerDelegate f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ float f$2;
    public final /* synthetic */ float f$3;
    public final /* synthetic */ float f$4;

    public /* synthetic */ ChartPickerDelegate$$ExternalSyntheticLambda0(ChartPickerDelegate chartPickerDelegate, float f, float f2, float f3, float f4) {
        this.f$0 = chartPickerDelegate;
        this.f$1 = f;
        this.f$2 = f2;
        this.f$3 = f3;
        this.f$4 = f4;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$uncapture$0(this.f$1, this.f$2, this.f$3, this.f$4, valueAnimator);
    }
}
