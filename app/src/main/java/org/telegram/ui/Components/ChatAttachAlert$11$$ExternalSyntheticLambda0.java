package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.ChatAttachAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlert$11$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ EditTextCaption f$0;

    public /* synthetic */ ChatAttachAlert$11$$ExternalSyntheticLambda0(EditTextCaption editTextCaption) {
        this.f$0 = editTextCaption;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ChatAttachAlert.AnonymousClass11.lambda$dispatchDraw$0(this.f$0, valueAnimator);
    }
}
