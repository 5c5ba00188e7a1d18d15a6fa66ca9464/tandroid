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
public class GridLayoutManagerFixed extends GridLayoutManager {
    private ArrayList<View> additionalViews = new ArrayList<>(4);
    private boolean canScrollVertically = true;

    protected boolean hasSiblingChild(int i) {
        throw null;
    }

    public boolean shouldLayoutChildFromOpositeSide(View view) {
        throw null;
    }

    public GridLayoutManagerFixed(Context context, int i, int i2, boolean z) {
        super(context, i, i2, z);
    }

    public void setCanScrollVertically(boolean z) {
        this.canScrollVertically = z;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean canScrollVertically() {
        return this.canScrollVertically;
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
                if (this.mOrientationHelper.getDecoratedEnd(childAt) > i || this.mOrientationHelper.getTransformedEndWithDecoration(childAt) > i) {
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

    @Override // androidx.recyclerview.widget.GridLayoutManager
    public void measureChild(View view, int i, boolean z) {
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        Rect rect = layoutParams.mDecorInsets;
        int i2 = rect.top + rect.bottom + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        measureChildWithDecorationsAndMargin(view, RecyclerView.LayoutManager.getChildMeasureSpec(this.mCachedBorders[layoutParams.mSpanSize], i, rect.left + rect.right + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, ((ViewGroup.MarginLayoutParams) layoutParams).width, false), RecyclerView.LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getHeightMode(), i2, ((ViewGroup.MarginLayoutParams) layoutParams).height, true), z);
    }

    /* JADX WARN: Code restructure failed: missing block: B:47:0x00d5, code lost:
        r28.mFinished = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00d7, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0187, code lost:
        if (r27.mLayoutDirection != (-1)) goto L78;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v0 */
    /* JADX WARN: Type inference failed for: r12v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r12v2 */
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
        int i6;
        int i7;
        int i8;
        View view;
        RecyclerView.Recycler recycler2 = recycler;
        int modeInOther = this.mOrientationHelper.getModeInOther();
        ?? r12 = 0;
        boolean z = true;
        boolean z2 = layoutState.mItemDirection == 1;
        layoutChunkResult.mConsumed = 0;
        int i9 = layoutState.mCurrentPosition;
        int i10 = -1;
        if (layoutState.mLayoutDirection != -1 && hasSiblingChild(i9) && findViewByPosition(layoutState.mCurrentPosition + 1) == null) {
            if (hasSiblingChild(layoutState.mCurrentPosition + 1)) {
                layoutState.mCurrentPosition += 3;
            } else {
                layoutState.mCurrentPosition += 2;
            }
            int i11 = layoutState.mCurrentPosition;
            for (int i12 = i11; i12 > i9; i12--) {
                View next = layoutState.next(recycler2);
                if (next != null) {
                    this.additionalViews.add(next);
                    if (i12 != i11) {
                        calculateItemDecorationsForChild(next, this.mDecorInsets);
                        measureChild(next, modeInOther, false);
                        int decoratedMeasurement = this.mOrientationHelper.getDecoratedMeasurement(next);
                        layoutState.mOffset -= decoratedMeasurement;
                        layoutState.mAvailable += decoratedMeasurement;
                    }
                }
            }
            layoutState.mCurrentPosition = i11;
        }
        boolean z3 = true;
        while (z3) {
            int i13 = this.mSpanCount;
            boolean isEmpty = this.additionalViews.isEmpty();
            boolean z4 = z ? 1 : 0;
            char c = z ? 1 : 0;
            boolean z5 = isEmpty ^ z4;
            int i14 = 0;
            while (i14 < this.mSpanCount && layoutState.hasMore(state) && i13 > 0) {
                int i15 = layoutState.mCurrentPosition;
                i13 -= getSpanSize(recycler2, state, i15);
                if (i13 < 0) {
                    break;
                }
                if (!this.additionalViews.isEmpty()) {
                    view = this.additionalViews.get(r12);
                    this.additionalViews.remove((int) r12);
                    int i16 = layoutState.mCurrentPosition;
                    int i17 = z ? 1 : 0;
                    int i18 = z ? 1 : 0;
                    layoutState.mCurrentPosition = i16 - i17;
                } else {
                    view = layoutState.next(recycler2);
                }
                if (view == null) {
                    break;
                }
                this.mSet[i14] = view;
                i14++;
                if (layoutState.mLayoutDirection == i10 && i13 <= 0 && hasSiblingChild(i15)) {
                    z5 = true;
                }
            }
            float f = 0.0f;
            assignSpans(recycler2, state, i14, z2);
            int i19 = 0;
            for (int i20 = 0; i20 < i14; i20++) {
                View view2 = this.mSet[i20];
                if (layoutState.mScrapList == null) {
                    if (z2) {
                        addView(view2);
                    } else {
                        addView(view2, r12);
                    }
                } else if (z2) {
                    addDisappearingView(view2);
                } else {
                    int i21 = r12 == true ? 1 : 0;
                    int i22 = r12 == true ? 1 : 0;
                    addDisappearingView(view2, i21);
                }
                calculateItemDecorationsForChild(view2, this.mDecorInsets);
                measureChild(view2, modeInOther, r12);
                int decoratedMeasurement2 = this.mOrientationHelper.getDecoratedMeasurement(view2);
                if (decoratedMeasurement2 > i19) {
                    i19 = decoratedMeasurement2;
                }
                float decoratedMeasurementInOther = (this.mOrientationHelper.getDecoratedMeasurementInOther(view2) * 1.0f) / ((GridLayoutManager.LayoutParams) view2.getLayoutParams()).mSpanSize;
                if (decoratedMeasurementInOther > f) {
                    f = decoratedMeasurementInOther;
                }
            }
            for (int i23 = 0; i23 < i14; i23++) {
                View view3 = this.mSet[i23];
                if (this.mOrientationHelper.getDecoratedMeasurement(view3) != i19) {
                    GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view3.getLayoutParams();
                    Rect rect = layoutParams.mDecorInsets;
                    measureChildWithDecorationsAndMargin(view3, RecyclerView.LayoutManager.getChildMeasureSpec(this.mCachedBorders[layoutParams.mSpanSize], 1073741824, rect.left + rect.right + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, ((ViewGroup.MarginLayoutParams) layoutParams).width, false), View.MeasureSpec.makeMeasureSpec(i19 - (((rect.top + rect.bottom) + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin) + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin), 1073741824), true);
                }
            }
            boolean shouldLayoutChildFromOpositeSide = shouldLayoutChildFromOpositeSide(this.mSet[0]);
            if (shouldLayoutChildFromOpositeSide) {
                i2 = -1;
            } else {
                i2 = -1;
            }
            if (shouldLayoutChildFromOpositeSide || layoutState.mLayoutDirection != 1) {
                i = i19;
                if (layoutState.mLayoutDirection == -1) {
                    int i24 = layoutState.mOffset - layoutChunkResult.mConsumed;
                    i8 = getWidth();
                    i7 = i24;
                    i6 = i24 - i;
                } else {
                    int i25 = layoutChunkResult.mConsumed + layoutState.mOffset;
                    i7 = i25 + i;
                    i6 = i25;
                    i8 = 0;
                }
                int i26 = 0;
                while (i26 < i14) {
                    View view4 = this.mSet[i26];
                    GridLayoutManager.LayoutParams layoutParams2 = (GridLayoutManager.LayoutParams) view4.getLayoutParams();
                    int decoratedMeasurementInOther2 = this.mOrientationHelper.getDecoratedMeasurementInOther(view4);
                    if (layoutState.mLayoutDirection == -1) {
                        i8 -= decoratedMeasurementInOther2;
                    }
                    int i27 = i8;
                    int i28 = i27 + decoratedMeasurementInOther2;
                    int i29 = i14;
                    layoutDecoratedWithMargins(view4, i27, i6, i28, i7);
                    i8 = layoutState.mLayoutDirection != -1 ? i28 : i27;
                    if (layoutParams2.isItemRemoved() || layoutParams2.isItemChanged()) {
                        layoutChunkResult.mIgnoreConsumed = true;
                    }
                    layoutChunkResult.mFocusable |= view4.hasFocusable();
                    i26++;
                    i14 = i29;
                }
                layoutChunkResult.mConsumed += i;
                Arrays.fill(this.mSet, (Object) null);
                recycler2 = recycler;
                z3 = z5;
                r12 = 0;
                z = true;
                i10 = -1;
            }
            if (layoutState.mLayoutDirection == i2) {
                int i30 = layoutState.mOffset - layoutChunkResult.mConsumed;
                i4 = i30;
                i3 = i30 - i19;
                i5 = 0;
            } else {
                int i31 = layoutChunkResult.mConsumed + layoutState.mOffset;
                i5 = getWidth();
                i4 = i31 + i19;
                i3 = i31;
            }
            int i32 = i14 - 1;
            while (i32 >= 0) {
                View view5 = this.mSet[i32];
                GridLayoutManager.LayoutParams layoutParams3 = (GridLayoutManager.LayoutParams) view5.getLayoutParams();
                int decoratedMeasurementInOther3 = this.mOrientationHelper.getDecoratedMeasurementInOther(view5);
                if (layoutState.mLayoutDirection == 1) {
                    i5 -= decoratedMeasurementInOther3;
                }
                int i33 = i5;
                int i34 = i33 + decoratedMeasurementInOther3;
                int i35 = i19;
                layoutDecoratedWithMargins(view5, i33, i3, i34, i4);
                i5 = layoutState.mLayoutDirection == -1 ? i34 : i33;
                if (layoutParams3.isItemRemoved() || layoutParams3.isItemChanged()) {
                    layoutChunkResult.mIgnoreConsumed = true;
                }
                layoutChunkResult.mFocusable |= view5.hasFocusable();
                i32--;
                i19 = i35;
            }
            i = i19;
            layoutChunkResult.mConsumed += i;
            Arrays.fill(this.mSet, (Object) null);
            recycler2 = recycler;
            z3 = z5;
            r12 = 0;
            z = true;
            i10 = -1;
        }
    }
}
