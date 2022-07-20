package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import org.telegram.ui.Components.GroupCallFullscreenAdapter;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallFullscreenAdapter$GroupCallUserCell$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ GroupCallFullscreenAdapter.GroupCallUserCell f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ int f$4;

    public /* synthetic */ GroupCallFullscreenAdapter$GroupCallUserCell$$ExternalSyntheticLambda0(GroupCallFullscreenAdapter.GroupCallUserCell groupCallUserCell, int i, int i2, int i3, int i4) {
        this.f$0 = groupCallUserCell;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = i3;
        this.f$4 = i4;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$updateState$0(this.f$1, this.f$2, this.f$3, this.f$4, valueAnimator);
    }
}
