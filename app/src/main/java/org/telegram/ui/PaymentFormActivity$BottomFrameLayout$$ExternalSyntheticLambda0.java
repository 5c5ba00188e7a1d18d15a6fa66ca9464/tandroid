package org.telegram.ui;

import androidx.dynamicanimation.animation.DynamicAnimation;
import org.telegram.ui.PaymentFormActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PaymentFormActivity$BottomFrameLayout$$ExternalSyntheticLambda0 implements DynamicAnimation.OnAnimationEndListener {
    public final /* synthetic */ PaymentFormActivity.BottomFrameLayout f$0;

    public /* synthetic */ PaymentFormActivity$BottomFrameLayout$$ExternalSyntheticLambda0(PaymentFormActivity.BottomFrameLayout bottomFrameLayout) {
        this.f$0 = bottomFrameLayout;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        this.f$0.lambda$setChecked$1(dynamicAnimation, z, f, f2);
    }
}
