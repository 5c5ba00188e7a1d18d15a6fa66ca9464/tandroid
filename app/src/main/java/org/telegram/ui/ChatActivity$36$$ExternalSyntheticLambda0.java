package org.telegram.ui;

import android.animation.ValueAnimator;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ChatActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$36$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ActionBarMenu f$0;

    public /* synthetic */ ChatActivity$36$$ExternalSyntheticLambda0(ActionBarMenu actionBarMenu) {
        this.f$0 = actionBarMenu;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ChatActivity.AnonymousClass36.lambda$onTextSelectionChanged$0(this.f$0, valueAnimator);
    }
}
