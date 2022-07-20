package org.telegram.ui.Components;

import org.telegram.ui.Components.TrendingStickersAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class TrendingStickersAlert$AlertContainerView$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ TrendingStickersAlert.AlertContainerView f$0;

    public /* synthetic */ TrendingStickersAlert$AlertContainerView$$ExternalSyntheticLambda1(TrendingStickersAlert.AlertContainerView alertContainerView) {
        this.f$0 = alertContainerView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.requestLayout();
    }
}
