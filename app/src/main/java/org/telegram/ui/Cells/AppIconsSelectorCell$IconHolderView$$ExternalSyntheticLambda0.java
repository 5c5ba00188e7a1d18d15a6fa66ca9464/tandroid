package org.telegram.ui.Cells;

import android.animation.ValueAnimator;
import org.telegram.ui.Cells.AppIconsSelectorCell;
/* loaded from: classes3.dex */
public final /* synthetic */ class AppIconsSelectorCell$IconHolderView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ AppIconsSelectorCell.IconHolderView f$0;

    public /* synthetic */ AppIconsSelectorCell$IconHolderView$$ExternalSyntheticLambda0(AppIconsSelectorCell.IconHolderView iconHolderView) {
        this.f$0 = iconHolderView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setSelected$0(valueAnimator);
    }
}
