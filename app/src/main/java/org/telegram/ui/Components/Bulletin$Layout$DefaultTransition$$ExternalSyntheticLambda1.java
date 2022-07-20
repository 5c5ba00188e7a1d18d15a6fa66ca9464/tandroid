package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import androidx.core.util.Consumer;
import org.telegram.ui.Components.Bulletin;
/* loaded from: classes3.dex */
public final /* synthetic */ class Bulletin$Layout$DefaultTransition$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ Consumer f$0;
    public final /* synthetic */ Bulletin.Layout f$1;

    public /* synthetic */ Bulletin$Layout$DefaultTransition$$ExternalSyntheticLambda1(Consumer consumer, Bulletin.Layout layout) {
        this.f$0 = consumer;
        this.f$1 = layout;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        Bulletin.Layout.DefaultTransition.lambda$animateExit$1(this.f$0, this.f$1, valueAnimator);
    }
}
