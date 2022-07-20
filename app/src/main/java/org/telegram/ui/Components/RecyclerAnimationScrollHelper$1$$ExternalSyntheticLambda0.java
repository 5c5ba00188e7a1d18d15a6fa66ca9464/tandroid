package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import java.util.ArrayList;
import org.telegram.ui.Components.RecyclerAnimationScrollHelper;
/* loaded from: classes3.dex */
public final /* synthetic */ class RecyclerAnimationScrollHelper$1$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ RecyclerAnimationScrollHelper.AnonymousClass1 f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ ArrayList f$4;

    public /* synthetic */ RecyclerAnimationScrollHelper$1$$ExternalSyntheticLambda0(RecyclerAnimationScrollHelper.AnonymousClass1 anonymousClass1, ArrayList arrayList, boolean z, int i, ArrayList arrayList2) {
        this.f$0 = anonymousClass1;
        this.f$1 = arrayList;
        this.f$2 = z;
        this.f$3 = i;
        this.f$4 = arrayList2;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$onLayoutChange$0(this.f$1, this.f$2, this.f$3, this.f$4, valueAnimator);
    }
}
