package org.telegram.ui;

import org.telegram.ui.Charts.BaseChartView;
import org.telegram.ui.StatisticActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class StatisticActivity$BaseChartCell$$ExternalSyntheticLambda5 implements BaseChartView.DateSelectionListener {
    public final /* synthetic */ StatisticActivity.BaseChartCell f$0;

    public /* synthetic */ StatisticActivity$BaseChartCell$$ExternalSyntheticLambda5(StatisticActivity.BaseChartCell baseChartCell) {
        this.f$0 = baseChartCell;
    }

    @Override // org.telegram.ui.Charts.BaseChartView.DateSelectionListener
    public final void onDateSelected(long j) {
        this.f$0.lambda$new$1(j);
    }
}
