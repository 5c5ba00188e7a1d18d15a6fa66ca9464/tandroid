package org.telegram.ui.Components;

import androidx.dynamicanimation.animation.DynamicAnimation;
/* loaded from: classes3.dex */
public final /* synthetic */ class SenderSelectView$$ExternalSyntheticLambda1 implements DynamicAnimation.OnAnimationEndListener {
    public final /* synthetic */ SenderSelectView f$0;

    public /* synthetic */ SenderSelectView$$ExternalSyntheticLambda1(SenderSelectView senderSelectView) {
        this.f$0 = senderSelectView;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        this.f$0.lambda$setProgress$3(dynamicAnimation, z, f, f2);
    }
}
