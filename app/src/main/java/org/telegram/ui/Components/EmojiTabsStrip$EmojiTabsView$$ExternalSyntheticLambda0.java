package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.EmojiTabsStrip;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiTabsStrip$EmojiTabsView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ EmojiTabsStrip.EmojiTabsView f$0;

    public /* synthetic */ EmojiTabsStrip$EmojiTabsView$$ExternalSyntheticLambda0(EmojiTabsStrip.EmojiTabsView emojiTabsView) {
        this.f$0 = emojiTabsView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$show$0(valueAnimator);
    }
}
