package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.InviteMembersBottomSheet;
/* loaded from: classes3.dex */
public final /* synthetic */ class InviteMembersBottomSheet$SpansContainer$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ InviteMembersBottomSheet.SpansContainer f$0;

    public /* synthetic */ InviteMembersBottomSheet$SpansContainer$$ExternalSyntheticLambda0(InviteMembersBottomSheet.SpansContainer spansContainer) {
        this.f$0 = spansContainer;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onMeasure$0(valueAnimator);
    }
}
