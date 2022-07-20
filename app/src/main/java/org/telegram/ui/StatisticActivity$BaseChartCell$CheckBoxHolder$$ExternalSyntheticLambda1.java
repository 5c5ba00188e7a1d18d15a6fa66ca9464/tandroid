package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Charts.view_data.LineViewData;
import org.telegram.ui.StatisticActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class StatisticActivity$BaseChartCell$CheckBoxHolder$$ExternalSyntheticLambda1 implements View.OnLongClickListener {
    public final /* synthetic */ StatisticActivity.BaseChartCell.CheckBoxHolder f$0;
    public final /* synthetic */ LineViewData f$1;

    public /* synthetic */ StatisticActivity$BaseChartCell$CheckBoxHolder$$ExternalSyntheticLambda1(StatisticActivity.BaseChartCell.CheckBoxHolder checkBoxHolder, LineViewData lineViewData) {
        this.f$0 = checkBoxHolder;
        this.f$1 = lineViewData;
    }

    @Override // android.view.View.OnLongClickListener
    public final boolean onLongClick(View view) {
        boolean lambda$setData$1;
        lambda$setData$1 = this.f$0.lambda$setData$1(this.f$1, view);
        return lambda$setData$1;
    }
}
