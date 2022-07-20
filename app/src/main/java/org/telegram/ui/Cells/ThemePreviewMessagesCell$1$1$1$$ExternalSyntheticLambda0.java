package org.telegram.ui.Cells;

import android.animation.ValueAnimator;
import org.telegram.ui.Cells.ThemePreviewMessagesCell;
/* loaded from: classes3.dex */
public final /* synthetic */ class ThemePreviewMessagesCell$1$1$1$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ThemePreviewMessagesCell.AnonymousClass1.C00101.ViewTreeObserver$OnPreDrawListenerC00111 f$0;

    public /* synthetic */ ThemePreviewMessagesCell$1$1$1$$ExternalSyntheticLambda0(ThemePreviewMessagesCell.AnonymousClass1.C00101.ViewTreeObserver$OnPreDrawListenerC00111 viewTreeObserver$OnPreDrawListenerC00111) {
        this.f$0 = viewTreeObserver$OnPreDrawListenerC00111;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onPreDraw$0(valueAnimator);
    }
}
