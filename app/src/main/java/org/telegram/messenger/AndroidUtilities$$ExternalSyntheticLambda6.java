package org.telegram.messenger;

import androidx.dynamicanimation.animation.DynamicAnimation;
/* loaded from: classes.dex */
public final /* synthetic */ class AndroidUtilities$$ExternalSyntheticLambda6 implements DynamicAnimation.OnAnimationEndListener {
    public final /* synthetic */ Runnable f$0;

    public /* synthetic */ AndroidUtilities$$ExternalSyntheticLambda6(Runnable runnable) {
        this.f$0 = runnable;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        AndroidUtilities.lambda$shakeViewSpring$6(this.f$0, dynamicAnimation, z, f, f2);
    }
}
