package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
/* loaded from: classes.dex */
public abstract class GridLayoutManagerFixed extends GridLayoutManager {
    private ArrayList additionalViews;
    private boolean canScrollVertically;

    public GridLayoutManagerFixed(Context context, int i, int i2, boolean z) {
        super(context, i, i2, z);
        this.additionalViews = new ArrayList(4);
        this.canScrollVertically = true;
    }

    @Override // androidx.recyclerview.widget.GridLayoutManager
    protected int[] calculateItemBorders(int[] iArr, int i, int i2) {
        if (iArr == null || iArr.length != i + 1 || iArr[iArr.length - 1] != i2) {
            iArr = new int[i + 1];
        }
        iArr[0] = 0;
        for (int i3 = 1; i3 <= i; i3++) {
            iArr[i3] = (int) Math.ceil((i3 / i) * i2);
        }
        return iArr;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean canScrollVertically() {
        return this.canScrollVertically;
    }

    protected abstract boolean hasSiblingChild(int i);

    /* JADX WARN: Code restructure failed: missing block: B:53:0x00d8, code lost:
        r28.mFinished = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00da, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x018a, code lost:
        if (r27.mLayoutDirection != (-1)) goto L119;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v0 */
    /* JADX WARN: Type inference failed for: r12v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r12v5 */
    /* JADX WARN: Type inference failed for: r13v0 */
    /* JADX WARN: Type inference failed for: r13v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r13v5 */
    @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void layoutChunk(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.LayoutState layoutState, LinearLayoutManager.LayoutChunkResult layoutChunkResult) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int width;
        int i6;
        int i7;
        View next;
        RecyclerView.Recycler recycler2 = recycler;
        int modeInOther = this.mOrientationHelper.getModeInOther();
        ?? r12 = 0;
        ?? r13 = 1;
        boolean z = layoutState.mItemDirection == 1;
        layoutChunkResult.mConsumed = 0;
        int i8 = layoutState.mCurrentPosition;
        int i9 = -1;
        if (this.mShouldReverseLayout && layoutState.mLayoutDirection != -1 && hasSiblingChild(i8) && findViewByPosition(layoutState.mCurrentPosition + 1) == null) {
            layoutState.mCurrentPosition = hasSiblingChild(layoutState.mCurrentPosition + 1) ? layoutState.mCurrentPosition + 3 : layoutState.mCurrentPosition + 2;
            int i10 = layoutState.mCurrentPosition;
            for (int i11 = i10; i11 > i8; i11--) {
                View next2 = layoutState.next(recycler2);
                if (next2 != null) {
                    this.additionalViews.add(next2);
                    if (i11 != i10) {
                        calculateItemDecorationsForChild(next2, this.mDecorInsets);
                        measureChild(next2, modeInOther, false);
                        int decoratedMeasurement = this.mOrientationHelper.getDecoratedMeasurement(next2);
                        layoutState.mOffset -= decoratedMeasurement;
                        layoutState.mAvailable += decoratedMeasurement;
                    }
                }
            }
            layoutState.mCurrentPosition = i10;
        }
        int i12 = 1;
        while (i12 != 0) {
            int i13 = this.mSpanCount;
            int isEmpty = this.additionalViews.isEmpty() ^ r13;
            int i14 = 0;
            while (i14 < this.mSpanCount && layoutState.hasMore(state) && i13 > 0) {
                int i15 = layoutState.mCurrentPosition;
                i13 -= getSpanSize(recycler2, state, i15);
                if (i13 < 0) {
                    break;
                }
                if (this.additionalViews.isEmpty()) {
                    next = layoutState.next(recycler2);
                } else {
                    next = (View) this.additionalViews.get(r12);
                    this.additionalViews.remove((int) r12);
                    layoutState.mCurrentPosition -= r13;
                }
                if (next == null) {
                    break;
                }
                this.mSet[i14] = next;
                i14++;
                if (layoutState.mLayoutDirection == i9 && i13 <= 0 && hasSiblingChild(i15)) {
                    isEmpty = 1;
                }
            }
            assignSpans(recycler2, state, i14, z);
            float f = 0.0f;
            int i16 = 0;
            for (int i17 = 0; i17 < i14; i17++) {
                View view = this.mSet[i17];
                if (layoutState.mScrapList == null) {
                    if (z) {
                        addView(view);
                    } else {
                        addView(view, r12);
                    }
                } else if (z) {
                    addDisappearingView(view);
                } else {
                    addDisappearingView(view, r12);
                }
                calculateItemDecorationsForChild(view, this.mDecorInsets);
                measureChild(view, modeInOther, r12);
                int decoratedMeasurement2 = this.mOrientationHelper.getDecoratedMeasurement(view);
                if (decoratedMeasurement2 > i16) {
                    i16 = decoratedMeasurement2;
                }
                float decoratedMeasurementInOther = (this.mOrientationHelper.getDecoratedMeasurementInOther(view) * 1.0f) / ((GridLayoutManager.LayoutParams) view.getLayoutParams()).mSpanSize;
                if (decoratedMeasurementInOther > f) {
                    f = decoratedMeasurementInOther;
                }
            }
            for (int i18 = 0; i18 < i14; i18++) {
                View view2 = this.mSet[i18];
                if (this.mOrientationHelper.getDecoratedMeasurement(view2) != i16) {
                    GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view2.getLayoutParams();
                    Rect rect = layoutParams.mDecorInsets;
                    measureChildWithDecorationsAndMargin(view2, RecyclerView.LayoutManager.getChildMeasureSpec(this.mCachedBorders[layoutParams.mSpanSize], 1073741824, rect.left + rect.right + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, ((ViewGroup.MarginLayoutParams) layoutParams).width, false), View.MeasureSpec.makeMeasureSpec(i16 - (((rect.top + rect.bottom) + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin) + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin), 1073741824), true);
                }
            }
            boolean shouldLayoutChildFromOpositeSide = shouldLayoutChildFromOpositeSide(this.mSet[0]);
            if (shouldLayoutChildFromOpositeSide) {
                i = -1;
            } else {
                i = -1;
            }
            if (shouldLayoutChildFromOpositeSide || layoutState.mLayoutDirection != 1) {
                i2 = i16;
                if (layoutState.mLayoutDirection == -1) {
                    int i19 = layoutState.mOffset - layoutChunkResult.mConsumed;
                    i5 = getWidth();
                    i3 = i19;
                    i4 = i19 - i2;
                } else {
                    int i20 = layoutChunkResult.mConsumed + layoutState.mOffset;
                    i3 = i20 + i2;
                    i4 = i20;
                    i5 = 0;
                }
                int i21 = 0;
                while (i21 < i14) {
                    View view3 = this.mSet[i21];
                    GridLayoutManager.LayoutParams layoutParams2 = (GridLayoutManager.LayoutParams) view3.getLayoutParams();
                    int decoratedMeasurementInOther2 = this.mOrientationHelper.getDecoratedMeasurementInOther(view3);
                    if (layoutState.mLayoutDirection == -1) {
                        i5 -= decoratedMeasurementInOther2;
                    }
                    int i22 = i5;
                    int i23 = i22 + decoratedMeasurementInOther2;
                    int i24 = i14;
                    layoutDecoratedWithMargins(view3, i22, i4, i23, i3);
                    i5 = layoutState.mLayoutDirection == 1 ? i23 : i22;
                    if (layoutParams2.isItemRemoved() || layoutParams2.isItemChanged()) {
                        layoutChunkResult.mIgnoreConsumed = true;
                    }
                    layoutChunkResult.mFocusable |= view3.hasFocusable();
                    i21++;
                    i14 = i24;
                }
                layoutChunkResult.mConsumed += i2;
                Arrays.fill(this.mSet, (Object) null);
                recycler2 = recycler;
                i12 = isEmpty;
                r12 = 0;
                r13 = 1;
                i9 = -1;
            }
            if (layoutState.mLayoutDirection == i) {
                int i25 = layoutState.mOffset - layoutChunkResult.mConsumed;
                i6 = i25;
                i7 = i25 - i16;
                width = 0;
            } else {
                int i26 = layoutChunkResult.mConsumed + layoutState.mOffset;
                width = getWidth();
                i6 = i26 + i16;
                i7 = i26;
            }
            int i27 = i14 - 1;
            while (i27 >= 0) {
                View view4 = this.mSet[i27];
                GridLayoutManager.LayoutParams layoutParams3 = (GridLayoutManager.LayoutParams) view4.getLayoutParams();
                int decoratedMeasurementInOther3 = this.mOrientationHelper.getDecoratedMeasurementInOther(view4);
                if (layoutState.mLayoutDirection == 1) {
                    width -= decoratedMeasurementInOther3;
                }
                int i28 = width;
                int i29 = i28 + decoratedMeasurementInOther3;
                int i30 = i16;
                layoutDecoratedWithMargins(view4, i28, i7, i29, i6);
                width = layoutState.mLayoutDirection == -1 ? i29 : i28;
                if (layoutParams3.isItemRemoved() || layoutParams3.isItemChanged()) {
                    layoutChunkResult.mIgnoreConsumed = true;
                }
                layoutChunkResult.mFocusable |= view4.hasFocusable();
                i27--;
                i16 = i30;
            }
            i2 = i16;
            layoutChunkResult.mConsumed += i2;
            Arrays.fill(this.mSet, (Object) null);
            recycler2 = recycler;
            i12 = isEmpty;
            r12 = 0;
            r13 = 1;
            i9 = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.recyclerview.widget.GridLayoutManager
    public void measureChild(View view, int i, boolean z) {
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        Rect rect = layoutParams.mDecorInsets;
        int i2 = rect.top + rect.bottom + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        measureChildWithDecorationsAndMargin(view, RecyclerView.LayoutManager.getChildMeasureSpec(this.mCachedBorders[layoutParams.mSpanSize], i, rect.left + rect.right + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, ((ViewGroup.MarginLayoutParams) layoutParams).width, false), RecyclerView.LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getHeightMode(), i2, ((ViewGroup.MarginLayoutParams) layoutParams).height, true), z);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    protected void recycleViewsFromStart(RecyclerView.Recycler recycler, int i, int i2) {
        if (i < 0) {
            return;
        }
        int childCount = getChildCount();
        if (!this.mShouldReverseLayout) {
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                if (childAt.getBottom() + ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) childAt.getLayoutParams())).bottomMargin > i || childAt.getTop() + childAt.getHeight() > i) {
                    recycleChildren(recycler, 0, i3);
                    return;
                }
            }
            return;
        }
        int i4 = childCount - 1;
        for (int i5 = i4; i5 >= 0; i5--) {
            View childAt2 = getChildAt(i5);
            if (childAt2.getBottom() + ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) childAt2.getLayoutParams())).bottomMargin > i || childAt2.getTop() + childAt2.getHeight() > i) {
                recycleChildren(recycler, i4, i5);
                return;
            }
        }
    }

    public void setCanScrollVertically(boolean z) {
        this.canScrollVertically = z;
    }

    public abstract boolean shouldLayoutChildFromOpositeSide(View view);
}
