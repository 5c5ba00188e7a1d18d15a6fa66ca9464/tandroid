package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.BotWebViewSheet;
/* loaded from: classes3.dex */
public final /* synthetic */ class BotWebViewSheet$2$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ BotWebViewSheet.AnonymousClass2 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ BotWebViewSheet$2$$ExternalSyntheticLambda1(BotWebViewSheet.AnonymousClass2 anonymousClass2, int i, int i2) {
        this.f$0 = anonymousClass2;
        this.f$1 = i;
        this.f$2 = i2;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onWebAppSetBackgroundColor$2(this.f$1, this.f$2, valueAnimator);
    }
}
