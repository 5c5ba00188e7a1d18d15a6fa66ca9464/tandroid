package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.EmojiView;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiView$EmojiPackHeader$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ EmojiView.EmojiPackHeader f$0;

    public /* synthetic */ EmojiView$EmojiPackHeader$$ExternalSyntheticLambda1(EmojiView.EmojiPackHeader emojiPackHeader) {
        this.f$0 = emojiPackHeader;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$updateInstall$4(valueAnimator);
    }
}
