package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.ChatAttachAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlert$AttachBotButton$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChatAttachAlert.AttachBotButton f$0;

    public /* synthetic */ ChatAttachAlert$AttachBotButton$$ExternalSyntheticLambda0(ChatAttachAlert.AttachBotButton attachBotButton) {
        this.f$0 = attachBotButton;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$updateCheckedState$0(valueAnimator);
    }
}
