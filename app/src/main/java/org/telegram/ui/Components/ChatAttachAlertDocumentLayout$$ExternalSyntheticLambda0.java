package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertDocumentLayout$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ChatAttachAlertDocumentLayout f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ float f$2;

    public /* synthetic */ ChatAttachAlertDocumentLayout$$ExternalSyntheticLambda0(ChatAttachAlertDocumentLayout chatAttachAlertDocumentLayout, int i, float f) {
        this.f$0 = chatAttachAlertDocumentLayout;
        this.f$1 = i;
        this.f$2 = f;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$runAnimation$4(this.f$1, this.f$2, valueAnimator);
    }
}
