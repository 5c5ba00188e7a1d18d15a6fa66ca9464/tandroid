package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.PointF;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;

/* loaded from: classes3.dex */
public class SmoothScroller extends LinearSmoothScroller {
    private float durationScale;
    private Interpolator interpolator;
    private int offset;

    public SmoothScroller(Context context) {
        super(context);
        this.interpolator = CubicBezierInterpolator.DEFAULT;
        this.durationScale = 1.0f;
    }

    @Override // androidx.recyclerview.widget.LinearSmoothScroller
    public int calculateDyToMakeVisible(View view, int i) {
        return super.calculateDyToMakeVisible(view, i) - this.offset;
    }

    @Override // androidx.recyclerview.widget.LinearSmoothScroller
    protected int calculateTimeForDeceleration(int i) {
        return Math.round(Math.min(super.calculateTimeForDeceleration(i), 500) * this.durationScale);
    }

    @Override // androidx.recyclerview.widget.LinearSmoothScroller
    protected int calculateTimeForScrolling(int i) {
        return Math.round(Math.min(super.calculateTimeForScrolling(i), 150) * this.durationScale);
    }

    protected void onEnd() {
    }

    @Override // androidx.recyclerview.widget.LinearSmoothScroller, androidx.recyclerview.widget.RecyclerView.SmoothScroller
    protected void onTargetFound(View view, RecyclerView.State state, RecyclerView.SmoothScroller.Action action) {
        int calculateDxToMakeVisible = calculateDxToMakeVisible(view, getHorizontalSnapPreference());
        int calculateDyToMakeVisible = calculateDyToMakeVisible(view, getVerticalSnapPreference());
        int calculateTimeForDeceleration = calculateTimeForDeceleration((int) Math.sqrt((calculateDxToMakeVisible * calculateDxToMakeVisible) + (calculateDyToMakeVisible * calculateDyToMakeVisible)));
        if (calculateTimeForDeceleration > 0) {
            action.update(-calculateDxToMakeVisible, -calculateDyToMakeVisible, calculateTimeForDeceleration, this.interpolator);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.SmoothScroller$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SmoothScroller.this.onEnd();
            }
        }, Math.max(0, calculateTimeForDeceleration));
    }

    public void setDurationScale(float f) {
        this.durationScale = f;
    }

    public void setOffset(int i) {
        this.offset = i;
    }

    @Override // androidx.recyclerview.widget.LinearSmoothScroller
    protected void updateActionForInterimTarget(RecyclerView.SmoothScroller.Action action) {
        PointF computeScrollVectorForPosition = computeScrollVectorForPosition(getTargetPosition());
        if (computeScrollVectorForPosition == null || (computeScrollVectorForPosition.x == 0.0f && computeScrollVectorForPosition.y == 0.0f)) {
            action.jumpTo(getTargetPosition());
            stop();
            return;
        }
        normalize(computeScrollVectorForPosition);
        this.mTargetVector = computeScrollVectorForPosition;
        this.mInterimTargetDx = (int) (computeScrollVectorForPosition.x * 10000.0f);
        this.mInterimTargetDy = (int) (computeScrollVectorForPosition.y * 10000.0f);
        action.update((int) (this.mInterimTargetDx * 1.2f), (int) (this.mInterimTargetDy * 1.2f), (int) (calculateTimeForScrolling(10000) * 1.2f), this.interpolator);
    }
}
