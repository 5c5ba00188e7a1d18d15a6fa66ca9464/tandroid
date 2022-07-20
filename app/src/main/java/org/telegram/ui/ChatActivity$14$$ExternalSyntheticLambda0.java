package org.telegram.ui;

import android.animation.ValueAnimator;
import org.telegram.ui.ChatActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$14$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChatActivity.AnonymousClass14 f$0;

    public /* synthetic */ ChatActivity$14$$ExternalSyntheticLambda0(ChatActivity.AnonymousClass14 anonymousClass14) {
        this.f$0 = anonymousClass14;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onTouchEvent$2(valueAnimator);
    }
}
