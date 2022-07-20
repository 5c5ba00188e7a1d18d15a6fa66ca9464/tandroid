package org.telegram.ui;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.CrossfadeDrawable;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda18 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ CrossfadeDrawable f$0;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda18(CrossfadeDrawable crossfadeDrawable) {
        this.f$0 = crossfadeDrawable;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ChatActivity.lambda$createMenu$164(this.f$0, valueAnimator);
    }
}
