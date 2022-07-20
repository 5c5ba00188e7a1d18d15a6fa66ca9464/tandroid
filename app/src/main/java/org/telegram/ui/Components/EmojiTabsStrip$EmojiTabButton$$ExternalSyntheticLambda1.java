package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.EmojiTabsStrip;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiTabsStrip$EmojiTabButton$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ EmojiTabsStrip.EmojiTabButton f$0;

    public /* synthetic */ EmojiTabsStrip$EmojiTabButton$$ExternalSyntheticLambda1(EmojiTabsStrip.EmojiTabButton emojiTabButton) {
        this.f$0 = emojiTabButton;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$updateLock$2(valueAnimator);
    }
}
