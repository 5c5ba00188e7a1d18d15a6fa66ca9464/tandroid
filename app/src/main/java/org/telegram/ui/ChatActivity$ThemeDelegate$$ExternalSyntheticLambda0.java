package org.telegram.ui;

import android.animation.ValueAnimator;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.MotionBackgroundDrawable;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$ThemeDelegate$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ MotionBackgroundDrawable f$0;

    public /* synthetic */ ChatActivity$ThemeDelegate$$ExternalSyntheticLambda0(MotionBackgroundDrawable motionBackgroundDrawable) {
        this.f$0 = motionBackgroundDrawable;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ChatActivity.ThemeDelegate.lambda$setupChatTheme$4(this.f$0, valueAnimator);
    }
}
