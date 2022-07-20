package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.SharedMediaLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class SharedMediaLayout$27$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SharedMediaLayout.AnonymousClass27 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ RecyclerListView f$2;

    public /* synthetic */ SharedMediaLayout$27$$ExternalSyntheticLambda0(SharedMediaLayout.AnonymousClass27 anonymousClass27, int i, RecyclerListView recyclerListView) {
        this.f$0 = anonymousClass27;
        this.f$1 = i;
        this.f$2 = recyclerListView;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onPreDraw$0(this.f$1, this.f$2, valueAnimator);
    }
}
