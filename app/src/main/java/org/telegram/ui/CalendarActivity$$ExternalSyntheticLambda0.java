package org.telegram.ui;

import android.animation.ValueAnimator;
/* loaded from: classes3.dex */
public final /* synthetic */ class CalendarActivity$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ CalendarActivity f$0;

    public /* synthetic */ CalendarActivity$$ExternalSyntheticLambda0(CalendarActivity calendarActivity) {
        this.f$0 = calendarActivity;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$animateSelection$4(valueAnimator);
    }
}
