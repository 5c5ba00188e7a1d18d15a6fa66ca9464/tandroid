package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.EmojiPacksAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiPacksAlert$EmojiPackHeader$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ EmojiPacksAlert.EmojiPackHeader f$0;

    public /* synthetic */ EmojiPacksAlert$EmojiPackHeader$$ExternalSyntheticLambda0(EmojiPacksAlert.EmojiPackHeader emojiPackHeader) {
        this.f$0 = emojiPackHeader;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$toggle$4(valueAnimator);
    }
}
