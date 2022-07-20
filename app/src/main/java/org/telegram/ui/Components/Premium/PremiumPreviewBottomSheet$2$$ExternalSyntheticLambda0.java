package org.telegram.ui.Components.Premium;

import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import org.telegram.ui.Components.Premium.PremiumPreviewBottomSheet;
/* loaded from: classes3.dex */
public final /* synthetic */ class PremiumPreviewBottomSheet$2$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ PremiumPreviewBottomSheet.AnonymousClass2 f$0;
    public final /* synthetic */ Drawable f$1;

    public /* synthetic */ PremiumPreviewBottomSheet$2$$ExternalSyntheticLambda0(PremiumPreviewBottomSheet.AnonymousClass2 anonymousClass2, Drawable drawable) {
        this.f$0 = anonymousClass2;
        this.f$1 = drawable;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onAnimationEnd$0(this.f$1, valueAnimator);
    }
}
