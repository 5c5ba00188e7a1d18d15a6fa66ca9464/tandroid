package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ThemeSmallPreviewView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ThemeSmallPreviewView f$0;

    public /* synthetic */ ThemeSmallPreviewView$$ExternalSyntheticLambda0(ThemeSmallPreviewView themeSmallPreviewView) {
        this.f$0 = themeSmallPreviewView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setSelected$4(valueAnimator);
    }
}
