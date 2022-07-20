package org.telegram.ui.Delegates;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivityMemberRequestsDelegate$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChatActivityMemberRequestsDelegate f$0;

    public /* synthetic */ ChatActivityMemberRequestsDelegate$$ExternalSyntheticLambda0(ChatActivityMemberRequestsDelegate chatActivityMemberRequestsDelegate) {
        this.f$0 = chatActivityMemberRequestsDelegate;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$animatePendingRequests$2(valueAnimator);
    }
}
