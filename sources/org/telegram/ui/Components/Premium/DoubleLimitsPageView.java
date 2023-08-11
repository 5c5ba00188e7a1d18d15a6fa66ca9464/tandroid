package org.telegram.ui.Components.Premium;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes4.dex */
public class DoubleLimitsPageView extends BaseListPageView {
    DoubledLimitsBottomSheet$Adapter adapter;

    public DoubleLimitsPageView(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context, resourcesProvider);
    }

    @Override // org.telegram.ui.Components.Premium.BaseListPageView
    public RecyclerView.Adapter createAdapter() {
        DoubledLimitsBottomSheet$Adapter doubledLimitsBottomSheet$Adapter = new DoubledLimitsBottomSheet$Adapter(UserConfig.selectedAccount, true, this.resourcesProvider);
        this.adapter = doubledLimitsBottomSheet$Adapter;
        doubledLimitsBottomSheet$Adapter.containerView = this;
        return doubledLimitsBottomSheet$Adapter;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.adapter.measureGradient(getContext(), getMeasuredWidth(), getMeasuredHeight());
    }
}
