package org.telegram.ui.Charts;

import android.animation.ValueAnimator;
import org.telegram.ui.Charts.view_data.ChartHorizontalLinesData;
/* loaded from: classes3.dex */
public final /* synthetic */ class BaseChartView$$ExternalSyntheticLambda3 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ BaseChartView f$0;
    public final /* synthetic */ ChartHorizontalLinesData f$1;

    public /* synthetic */ BaseChartView$$ExternalSyntheticLambda3(BaseChartView baseChartView, ChartHorizontalLinesData chartHorizontalLinesData) {
        this.f$0 = baseChartView;
        this.f$1 = chartHorizontalLinesData;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setMaxMinValue$2(this.f$1, valueAnimator);
    }
}
