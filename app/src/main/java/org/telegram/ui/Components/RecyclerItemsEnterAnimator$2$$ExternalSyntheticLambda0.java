package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.RecyclerItemsEnterAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class RecyclerItemsEnterAnimator$2$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ RecyclerItemsEnterAnimator.AnonymousClass2 f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ RecyclerItemsEnterAnimator$2$$ExternalSyntheticLambda0(RecyclerItemsEnterAnimator.AnonymousClass2 anonymousClass2, int i) {
        this.f$0 = anonymousClass2;
        this.f$1 = i;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onPreDraw$0(this.f$1, valueAnimator);
    }
}
