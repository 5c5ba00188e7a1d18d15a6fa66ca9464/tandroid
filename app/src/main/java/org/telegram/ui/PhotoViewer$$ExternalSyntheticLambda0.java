package org.telegram.ui;

import android.animation.ValueAnimator;
import org.telegram.messenger.ImageReceiver;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ImageReceiver f$0;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda0(ImageReceiver imageReceiver) {
        this.f$0 = imageReceiver;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        PhotoViewer.lambda$switchToPip$45(this.f$0, valueAnimator);
    }
}
