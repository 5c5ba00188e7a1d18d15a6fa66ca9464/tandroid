package org.telegram.ui.Components;

import androidx.dynamicanimation.animation.DynamicAnimation;
import org.telegram.ui.Components.Bulletin;
/* loaded from: classes3.dex */
public final /* synthetic */ class Bulletin$ParentLayout$1$$ExternalSyntheticLambda1 implements DynamicAnimation.OnAnimationEndListener {
    public final /* synthetic */ Bulletin.ParentLayout.AnonymousClass1 f$0;

    public /* synthetic */ Bulletin$ParentLayout$1$$ExternalSyntheticLambda1(Bulletin.ParentLayout.AnonymousClass1 anonymousClass1) {
        this.f$0 = anonymousClass1;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        this.f$0.lambda$onFling$2(dynamicAnimation, z, f, f2);
    }
}
