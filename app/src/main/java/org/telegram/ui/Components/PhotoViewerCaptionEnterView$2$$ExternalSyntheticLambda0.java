package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.PhotoViewerCaptionEnterView;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewerCaptionEnterView$2$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ PhotoViewerCaptionEnterView.AnonymousClass2 f$0;

    public /* synthetic */ PhotoViewerCaptionEnterView$2$$ExternalSyntheticLambda0(PhotoViewerCaptionEnterView.AnonymousClass2 anonymousClass2) {
        this.f$0 = anonymousClass2;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$afterTextChanged$0(valueAnimator);
    }
}
