package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.ChatAttachAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlert$1$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChatAttachAlert.AnonymousClass1 f$0;

    public /* synthetic */ ChatAttachAlert$1$$ExternalSyntheticLambda0(ChatAttachAlert.AnonymousClass1 anonymousClass1) {
        this.f$0 = anonymousClass1;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onSetupMainButton$3(valueAnimator);
    }
}
