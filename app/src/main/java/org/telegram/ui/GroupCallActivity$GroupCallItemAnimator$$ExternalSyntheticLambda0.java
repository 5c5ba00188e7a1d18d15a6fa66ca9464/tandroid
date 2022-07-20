package org.telegram.ui;

import android.animation.ValueAnimator;
import org.telegram.ui.GroupCallActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallActivity$GroupCallItemAnimator$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ GroupCallActivity.GroupCallItemAnimator f$0;

    public /* synthetic */ GroupCallActivity$GroupCallItemAnimator$$ExternalSyntheticLambda0(GroupCallActivity.GroupCallItemAnimator groupCallItemAnimator) {
        this.f$0 = groupCallItemAnimator;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$runPendingAnimations$0(valueAnimator);
    }
}
