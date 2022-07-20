package org.telegram.ui;

import android.animation.ValueAnimator;
import org.telegram.ui.ChatActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$UnreadCounterTextView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChatActivity.UnreadCounterTextView f$0;

    public /* synthetic */ ChatActivity$UnreadCounterTextView$$ExternalSyntheticLambda0(ChatActivity.UnreadCounterTextView unreadCounterTextView) {
        this.f$0 = unreadCounterTextView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setText$0(valueAnimator);
    }
}
