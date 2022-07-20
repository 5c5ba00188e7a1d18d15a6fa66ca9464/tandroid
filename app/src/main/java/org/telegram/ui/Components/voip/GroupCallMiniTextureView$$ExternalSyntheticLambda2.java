package org.telegram.ui.Components.voip;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallMiniTextureView$$ExternalSyntheticLambda2 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ GroupCallMiniTextureView f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ int f$4;

    public /* synthetic */ GroupCallMiniTextureView$$ExternalSyntheticLambda2(GroupCallMiniTextureView groupCallMiniTextureView, int i, int i2, int i3, int i4) {
        this.f$0 = groupCallMiniTextureView;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = i3;
        this.f$4 = i4;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$updateIconColor$6(this.f$1, this.f$2, this.f$3, this.f$4, valueAnimator);
    }
}
