package org.telegram.ui.Components;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
/* loaded from: classes3.dex */
public final /* synthetic */ class SenderSelectPopup$$ExternalSyntheticLambda2 implements DynamicAnimation.OnAnimationEndListener {
    public final /* synthetic */ SenderSelectPopup f$0;
    public final /* synthetic */ SpringAnimation f$1;

    public /* synthetic */ SenderSelectPopup$$ExternalSyntheticLambda2(SenderSelectPopup senderSelectPopup, SpringAnimation springAnimation) {
        this.f$0 = senderSelectPopup;
        this.f$1 = springAnimation;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        this.f$0.lambda$startShowAnimation$3(this.f$1, dynamicAnimation, z, f, f2);
    }
}
