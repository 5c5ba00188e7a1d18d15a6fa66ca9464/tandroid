package org.telegram.ui;

import android.animation.ValueAnimator;
import org.telegram.ui.AvatarPreviewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class AvatarPreviewer$Layout$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ AvatarPreviewer.Layout f$0;

    public /* synthetic */ AvatarPreviewer$Layout$$ExternalSyntheticLambda1(AvatarPreviewer.Layout layout) {
        this.f$0 = layout;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onDraw$3(valueAnimator);
    }
}
