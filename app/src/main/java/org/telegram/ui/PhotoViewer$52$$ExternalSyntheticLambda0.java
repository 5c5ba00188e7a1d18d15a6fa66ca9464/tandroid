package org.telegram.ui;

import android.animation.ValueAnimator;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$52$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ PhotoViewer.AnonymousClass52 f$0;

    public /* synthetic */ PhotoViewer$52$$ExternalSyntheticLambda0(PhotoViewer.AnonymousClass52 anonymousClass52) {
        this.f$0 = anonymousClass52;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onSurfaceTextureUpdated$3(valueAnimator);
    }
}
