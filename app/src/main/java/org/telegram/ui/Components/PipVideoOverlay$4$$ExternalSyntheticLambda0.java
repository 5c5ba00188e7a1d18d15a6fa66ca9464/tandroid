package org.telegram.ui.Components;

import androidx.dynamicanimation.animation.DynamicAnimation;
import org.telegram.ui.Components.PipVideoOverlay;
/* loaded from: classes3.dex */
public final /* synthetic */ class PipVideoOverlay$4$$ExternalSyntheticLambda0 implements DynamicAnimation.OnAnimationEndListener {
    public final /* synthetic */ PipVideoOverlay.AnonymousClass4 f$0;
    public final /* synthetic */ float f$1;

    public /* synthetic */ PipVideoOverlay$4$$ExternalSyntheticLambda0(PipVideoOverlay.AnonymousClass4 anonymousClass4, float f) {
        this.f$0 = anonymousClass4;
        this.f$1 = f;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        this.f$0.lambda$onScroll$0(this.f$1, dynamicAnimation, z, f, f2);
    }
}
