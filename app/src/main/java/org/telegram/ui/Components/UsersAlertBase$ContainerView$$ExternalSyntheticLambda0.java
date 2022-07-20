package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.UsersAlertBase;
/* loaded from: classes3.dex */
public final /* synthetic */ class UsersAlertBase$ContainerView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ UsersAlertBase.ContainerView f$0;

    public /* synthetic */ UsersAlertBase$ContainerView$$ExternalSyntheticLambda0(UsersAlertBase.ContainerView containerView) {
        this.f$0 = containerView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onMeasure$0(valueAnimator);
    }
}
