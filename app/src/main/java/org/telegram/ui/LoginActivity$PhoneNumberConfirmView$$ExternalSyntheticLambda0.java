package org.telegram.ui;

import android.animation.ValueAnimator;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$PhoneNumberConfirmView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ LoginActivity.PhoneNumberConfirmView f$0;

    public /* synthetic */ LoginActivity$PhoneNumberConfirmView$$ExternalSyntheticLambda0(LoginActivity.PhoneNumberConfirmView phoneNumberConfirmView) {
        this.f$0 = phoneNumberConfirmView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$dismiss$6(valueAnimator);
    }
}
