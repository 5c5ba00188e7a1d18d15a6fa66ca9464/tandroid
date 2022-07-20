package org.telegram.ui.Components;

import android.view.MotionEvent;
import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.TrendingStickersLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class TrendingStickersLayout$$ExternalSyntheticLambda0 implements View.OnTouchListener {
    public final /* synthetic */ TrendingStickersLayout f$0;
    public final /* synthetic */ TrendingStickersLayout.Delegate f$1;
    public final /* synthetic */ RecyclerListView.OnItemClickListener f$2;

    public /* synthetic */ TrendingStickersLayout$$ExternalSyntheticLambda0(TrendingStickersLayout trendingStickersLayout, TrendingStickersLayout.Delegate delegate, RecyclerListView.OnItemClickListener onItemClickListener) {
        this.f$0 = trendingStickersLayout;
        this.f$1 = delegate;
        this.f$2 = onItemClickListener;
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        boolean lambda$new$1;
        lambda$new$1 = this.f$0.lambda$new$1(this.f$1, this.f$2, view, motionEvent);
        return lambda$new$1;
    }
}
