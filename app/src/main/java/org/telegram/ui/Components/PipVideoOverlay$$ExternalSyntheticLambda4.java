package org.telegram.ui.Components;

import androidx.dynamicanimation.animation.DynamicAnimation;
/* loaded from: classes3.dex */
public final /* synthetic */ class PipVideoOverlay$$ExternalSyntheticLambda4 implements DynamicAnimation.OnAnimationEndListener {
    public final /* synthetic */ PipVideoOverlay f$0;

    public /* synthetic */ PipVideoOverlay$$ExternalSyntheticLambda4(PipVideoOverlay pipVideoOverlay) {
        this.f$0 = pipVideoOverlay;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        this.f$0.lambda$showInternal$7(dynamicAnimation, z, f, f2);
    }
}
