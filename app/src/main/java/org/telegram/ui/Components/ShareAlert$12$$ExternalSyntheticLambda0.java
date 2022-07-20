package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.ShareAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class ShareAlert$12$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ EditTextCaption f$0;

    public /* synthetic */ ShareAlert$12$$ExternalSyntheticLambda0(EditTextCaption editTextCaption) {
        this.f$0 = editTextCaption;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ShareAlert.AnonymousClass12.lambda$dispatchDraw$0(this.f$0, valueAnimator);
    }
}
