package androidx.recyclerview.widget;

import android.animation.ValueAnimator;
import androidx.recyclerview.widget.RecyclerView;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultItemAnimator$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ DefaultItemAnimator f$0;
    public final /* synthetic */ RecyclerView.ViewHolder f$1;

    public /* synthetic */ DefaultItemAnimator$$ExternalSyntheticLambda0(DefaultItemAnimator defaultItemAnimator, RecyclerView.ViewHolder viewHolder) {
        this.f$0 = defaultItemAnimator;
        this.f$1 = viewHolder;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$0.lambda$animateMoveImpl$0(this.f$1, valueAnimator);
    }
}
