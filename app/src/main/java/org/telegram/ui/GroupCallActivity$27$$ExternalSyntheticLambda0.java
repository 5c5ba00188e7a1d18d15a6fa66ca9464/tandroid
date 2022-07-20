package org.telegram.ui;

import android.animation.ValueAnimator;
import org.telegram.ui.GroupCallActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallActivity$27$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ GroupCallActivity.AnonymousClass27 f$0;

    public /* synthetic */ GroupCallActivity$27$$ExternalSyntheticLambda0(GroupCallActivity.AnonymousClass27 anonymousClass27) {
        this.f$0 = anonymousClass27;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onUiVisibilityChanged$0(valueAnimator);
    }
}
