package org.telegram.ui.Components;

import androidx.dynamicanimation.animation.DynamicAnimation;
/* loaded from: classes3.dex */
public final /* synthetic */ class SenderSelectPopup$$ExternalSyntheticLambda1 implements DynamicAnimation.OnAnimationEndListener {
    public final /* synthetic */ SenderSelectPopup f$0;

    public /* synthetic */ SenderSelectPopup$$ExternalSyntheticLambda1(SenderSelectPopup senderSelectPopup) {
        this.f$0 = senderSelectPopup;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        this.f$0.lambda$startDismissAnimation$6(dynamicAnimation, z, f, f2);
    }
}
