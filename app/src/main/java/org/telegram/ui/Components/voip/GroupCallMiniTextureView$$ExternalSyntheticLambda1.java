package org.telegram.ui.Components.voip;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallMiniTextureView$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ GroupCallMiniTextureView f$0;

    public /* synthetic */ GroupCallMiniTextureView$$ExternalSyntheticLambda1(GroupCallMiniTextureView groupCallMiniTextureView) {
        this.f$0 = groupCallMiniTextureView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$updateAttachState$3(valueAnimator);
    }
}
