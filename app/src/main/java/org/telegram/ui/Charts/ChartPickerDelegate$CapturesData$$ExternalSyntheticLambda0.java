package org.telegram.ui.Charts;

import android.animation.ValueAnimator;
import org.telegram.ui.Charts.ChartPickerDelegate;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChartPickerDelegate$CapturesData$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChartPickerDelegate.CapturesData f$0;

    public /* synthetic */ ChartPickerDelegate$CapturesData$$ExternalSyntheticLambda0(ChartPickerDelegate.CapturesData capturesData) {
        this.f$0 = capturesData;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$captured$0(valueAnimator);
    }
}
