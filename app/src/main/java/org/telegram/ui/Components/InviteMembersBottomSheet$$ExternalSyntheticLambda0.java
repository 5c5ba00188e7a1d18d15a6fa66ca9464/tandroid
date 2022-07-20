package org.telegram.ui.Components;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class InviteMembersBottomSheet$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ InviteMembersBottomSheet f$0;

    public /* synthetic */ InviteMembersBottomSheet$$ExternalSyntheticLambda0(InviteMembersBottomSheet inviteMembersBottomSheet) {
        this.f$0 = inviteMembersBottomSheet;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$spansCountChanged$3(valueAnimator);
    }
}
