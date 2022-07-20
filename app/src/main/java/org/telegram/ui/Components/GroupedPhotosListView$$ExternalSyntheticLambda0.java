package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupedPhotosListView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ GroupedPhotosListView f$0;

    public /* synthetic */ GroupedPhotosListView$$ExternalSyntheticLambda0(GroupedPhotosListView groupedPhotosListView) {
        this.f$0 = groupedPhotosListView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$fillList$0(valueAnimator);
    }
}
