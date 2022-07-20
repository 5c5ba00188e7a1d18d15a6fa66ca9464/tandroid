package androidx.recyclerview.widget;

import android.animation.ValueAnimator;
/* loaded from: classes.dex */
public final /* synthetic */ class ChatListItemAnimator$$ExternalSyntheticLambda2 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChatListItemAnimator f$0;

    public /* synthetic */ ChatListItemAnimator$$ExternalSyntheticLambda2(ChatListItemAnimator chatListItemAnimator) {
        this.f$0 = chatListItemAnimator;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$runPendingAnimations$0(valueAnimator);
    }
}
