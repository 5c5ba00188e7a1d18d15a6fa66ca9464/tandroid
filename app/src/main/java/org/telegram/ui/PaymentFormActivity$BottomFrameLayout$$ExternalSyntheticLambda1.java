package org.telegram.ui;

import androidx.dynamicanimation.animation.DynamicAnimation;
import org.telegram.ui.PaymentFormActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PaymentFormActivity$BottomFrameLayout$$ExternalSyntheticLambda1 implements DynamicAnimation.OnAnimationUpdateListener {
    public final /* synthetic */ PaymentFormActivity.BottomFrameLayout f$0;

    public /* synthetic */ PaymentFormActivity$BottomFrameLayout$$ExternalSyntheticLambda1(PaymentFormActivity.BottomFrameLayout bottomFrameLayout) {
        this.f$0 = bottomFrameLayout;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
        this.f$0.lambda$setChecked$0(dynamicAnimation, f, f2);
    }
}
