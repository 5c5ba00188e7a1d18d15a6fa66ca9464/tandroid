package org.telegram.ui;

import android.animation.ValueAnimator;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$65$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ PhotoViewer.AnonymousClass65 f$0;

    public /* synthetic */ PhotoViewer$65$$ExternalSyntheticLambda0(PhotoViewer.AnonymousClass65 anonymousClass65) {
        this.f$0 = anonymousClass65;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onAppear$0(valueAnimator);
    }
}
