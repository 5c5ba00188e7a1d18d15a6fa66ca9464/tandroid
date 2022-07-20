package org.telegram.ui;

import android.animation.ValueAnimator;
import org.telegram.ui.ProfileActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$10$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ProfileActivity.AnonymousClass10 f$0;

    public /* synthetic */ ProfileActivity$10$$ExternalSyntheticLambda0(ProfileActivity.AnonymousClass10 anonymousClass10) {
        this.f$0 = anonymousClass10;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$runPendingAnimations$1(valueAnimator);
    }
}
