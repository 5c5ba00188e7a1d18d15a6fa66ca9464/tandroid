package org.telegram.ui.Components;

import androidx.dynamicanimation.animation.DynamicAnimation;
import org.telegram.ui.Components.BotWebViewMenuContainer;
/* loaded from: classes3.dex */
public final /* synthetic */ class BotWebViewMenuContainer$6$$ExternalSyntheticLambda0 implements DynamicAnimation.OnAnimationEndListener {
    public final /* synthetic */ BotWebViewMenuContainer.AnonymousClass6 f$0;

    public /* synthetic */ BotWebViewMenuContainer$6$$ExternalSyntheticLambda0(BotWebViewMenuContainer.AnonymousClass6 anonymousClass6) {
        this.f$0 = anonymousClass6;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        this.f$0.lambda$onLayoutChange$0(dynamicAnimation, z, f, f2);
    }
}
