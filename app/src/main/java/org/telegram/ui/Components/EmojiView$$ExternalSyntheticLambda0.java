package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ EmojiView f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ EmojiView$$ExternalSyntheticLambda0(EmojiView emojiView, int i) {
        this.f$0 = emojiView;
        this.f$1 = i;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$animateTabsY$8(this.f$1, valueAnimator);
    }
}
