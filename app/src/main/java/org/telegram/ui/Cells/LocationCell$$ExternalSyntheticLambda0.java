package org.telegram.ui.Cells;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class LocationCell$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ LocationCell f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ float f$3;
    public final /* synthetic */ float f$4;

    public /* synthetic */ LocationCell$$ExternalSyntheticLambda0(LocationCell locationCell, long j, long j2, float f, float f2) {
        this.f$0 = locationCell;
        this.f$1 = j;
        this.f$2 = j2;
        this.f$3 = f;
        this.f$4 = f2;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$setLocation$0(this.f$1, this.f$2, this.f$3, this.f$4, valueAnimator);
    }
}
