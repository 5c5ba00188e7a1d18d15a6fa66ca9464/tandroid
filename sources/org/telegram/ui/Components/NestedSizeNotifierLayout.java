package org.telegram.ui.Components;

import android.content.Context;
import android.view.View;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.ActionBar.BottomSheet;
/* loaded from: classes3.dex */
public class NestedSizeNotifierLayout extends SizeNotifierFrameLayout implements NestedScrollingParent3 {
    BottomSheet.ContainerView bottomSheetContainerView;
    ChildLayout childLayout;
    int maxTop;
    private NestedScrollingParentHelper nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    View targetListView;

    /* loaded from: classes3.dex */
    public interface ChildLayout {
        RecyclerListView getListView();

        int getMeasuredHeight();

        int getTop();

        boolean isAttached();
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5) {
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public void onStopNestedScroll(View view) {
    }

    public NestedSizeNotifierLayout(Context context) {
        super(context);
    }

    private boolean childAttached() {
        ChildLayout childLayout = this.childLayout;
        return (childLayout == null || !childLayout.isAttached() || this.childLayout.getListView() == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        View view = this.targetListView;
        if (view == null || this.childLayout == null) {
            return;
        }
        this.maxTop = (view.getMeasuredHeight() - this.targetListView.getPaddingBottom()) - this.childLayout.getMeasuredHeight();
    }

    @Override // androidx.core.view.NestedScrollingParent3
    public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr) {
        if (view != this.targetListView || !childAttached()) {
            return;
        }
        RecyclerListView listView = this.childLayout.getListView();
        if (this.childLayout.getTop() != this.maxTop) {
            return;
        }
        iArr[1] = i4;
        listView.scrollBy(0, i4);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public boolean onNestedPreFling(View view, float f, float f2) {
        return super.onNestedPreFling(view, f, f2);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3) {
        if (view != this.targetListView || !childAttached()) {
            return;
        }
        int top = this.childLayout.getTop();
        if (i2 < 0) {
            if (top <= this.maxTop) {
                RecyclerListView listView = this.childLayout.getListView();
                int findFirstVisibleItemPosition = ((LinearLayoutManager) listView.getLayoutManager()).findFirstVisibleItemPosition();
                int i4 = -1;
                if (findFirstVisibleItemPosition == -1) {
                    return;
                }
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = listView.findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
                if (findViewHolderForAdapterPosition != null) {
                    i4 = findViewHolderForAdapterPosition.itemView.getTop();
                }
                int paddingTop = listView.getPaddingTop();
                if (i4 == paddingTop && findFirstVisibleItemPosition == 0) {
                    return;
                }
                iArr[1] = findFirstVisibleItemPosition != 0 ? i2 : Math.max(i2, i4 - paddingTop);
                listView.scrollBy(0, i2);
                return;
            } else if (this.bottomSheetContainerView == null || this.targetListView.canScrollVertically(i2)) {
                return;
            } else {
                this.bottomSheetContainerView.onNestedScroll(view, 0, 0, i, i2);
                return;
            }
        }
        BottomSheet.ContainerView containerView = this.bottomSheetContainerView;
        if (containerView == null) {
            return;
        }
        containerView.onNestedPreScroll(view, i, i2, iArr);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
        return view != null && view.isAttachedToWindow() && i == 2;
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedScrollAccepted(View view, View view2, int i, int i2) {
        this.nestedScrollingParentHelper.onNestedScrollAccepted(view, view2, i);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onStopNestedScroll(View view, int i) {
        this.nestedScrollingParentHelper.onStopNestedScroll(view);
        BottomSheet.ContainerView containerView = this.bottomSheetContainerView;
        if (containerView != null) {
            containerView.onStopNestedScroll(view);
        }
    }

    public void setTargetListView(View view) {
        this.targetListView = view;
    }

    public void setChildLayout(ChildLayout childLayout) {
        this.childLayout = childLayout;
    }

    public boolean isPinnedToTop() {
        ChildLayout childLayout = this.childLayout;
        return childLayout != null && childLayout.getTop() == this.maxTop;
    }

    public void setBottomSheetContainerView(BottomSheet.ContainerView containerView) {
        this.bottomSheetContainerView = containerView;
    }
}
