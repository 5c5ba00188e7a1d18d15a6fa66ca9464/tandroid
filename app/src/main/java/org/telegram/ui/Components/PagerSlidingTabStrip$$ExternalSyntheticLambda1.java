package org.telegram.ui.Components;
/* loaded from: classes3.dex */
public final /* synthetic */ class PagerSlidingTabStrip$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ PagerSlidingTabStrip f$0;

    public /* synthetic */ PagerSlidingTabStrip$$ExternalSyntheticLambda1(PagerSlidingTabStrip pagerSlidingTabStrip) {
        this.f$0 = pagerSlidingTabStrip;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.notifyDataSetChanged();
    }
}
