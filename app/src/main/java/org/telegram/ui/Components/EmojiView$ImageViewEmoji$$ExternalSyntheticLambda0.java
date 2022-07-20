package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.EmojiView;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiView$ImageViewEmoji$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ EmojiView.ImageViewEmoji f$0;

    public /* synthetic */ EmojiView$ImageViewEmoji$$ExternalSyntheticLambda0(EmojiView.ImageViewEmoji imageViewEmoji) {
        this.f$0 = imageViewEmoji;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setPressed$1(valueAnimator);
    }
}
