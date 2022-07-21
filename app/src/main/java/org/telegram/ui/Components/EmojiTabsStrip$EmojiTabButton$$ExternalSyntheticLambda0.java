package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.EmojiTabsStrip;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiTabsStrip$EmojiTabButton$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ EmojiTabsStrip.EmojiTabButton f$0;

    public /* synthetic */ EmojiTabsStrip$EmojiTabButton$$ExternalSyntheticLambda0(EmojiTabsStrip.EmojiTabButton emojiTabButton) {
        this.f$0 = emojiTabButton;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$updateSelect$2(valueAnimator);
    }
}
