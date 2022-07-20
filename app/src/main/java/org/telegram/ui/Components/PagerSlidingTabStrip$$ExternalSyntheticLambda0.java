package org.telegram.ui.Components;

import android.view.View;
/* loaded from: classes3.dex */
public final /* synthetic */ class PagerSlidingTabStrip$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ PagerSlidingTabStrip f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ PagerSlidingTabStrip$$ExternalSyntheticLambda0(PagerSlidingTabStrip pagerSlidingTabStrip, int i) {
        this.f$0 = pagerSlidingTabStrip;
        this.f$1 = i;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$addIconTab$0(this.f$1, view);
    }
}
