package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProxySettingsActivity$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ProxySettingsActivity f$0;

    public /* synthetic */ ProxySettingsActivity$$ExternalSyntheticLambda0(ProxySettingsActivity proxySettingsActivity) {
        this.f$0 = proxySettingsActivity;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setShareDoneEnabled$5(valueAnimator);
    }
}
