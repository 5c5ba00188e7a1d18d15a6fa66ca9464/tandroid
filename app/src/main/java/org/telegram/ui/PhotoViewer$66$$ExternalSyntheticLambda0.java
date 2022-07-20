package org.telegram.ui;

import android.animation.ValueAnimator;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$66$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ PhotoViewer.AnonymousClass66 f$0;

    public /* synthetic */ PhotoViewer$66$$ExternalSyntheticLambda0(PhotoViewer.AnonymousClass66 anonymousClass66) {
        this.f$0 = anonymousClass66;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onDisappear$0(valueAnimator);
    }
}
