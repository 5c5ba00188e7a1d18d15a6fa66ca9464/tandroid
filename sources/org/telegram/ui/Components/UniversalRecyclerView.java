package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.util.Consumer;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.RecyclerListView;

/* loaded from: classes3.dex */
public class UniversalRecyclerView extends RecyclerListView {
    public final UniversalAdapter adapter;
    private boolean doNotDetachViews;
    private ItemTouchHelper itemTouchHelper;
    public LinearLayoutManager layoutManager;
    private boolean reorderingAllowed;

    /* loaded from: classes3.dex */
    private class TouchHelperCallback extends ItemTouchHelper.Callback {
        private TouchHelperCallback() {
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return (UniversalRecyclerView.this.reorderingAllowed && UniversalRecyclerView.this.adapter.isReorderItem(viewHolder.getAdapterPosition())) ? ItemTouchHelper.Callback.makeMovementFlags(3, 0) : ItemTouchHelper.Callback.makeMovementFlags(0, 0);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean isLongPressDragEnabled() {
            return UniversalRecyclerView.this.reorderingAllowed;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
            if (!UniversalRecyclerView.this.adapter.isReorderItem(viewHolder.getAdapterPosition()) || UniversalRecyclerView.this.adapter.getReorderSectionId(viewHolder.getAdapterPosition()) != UniversalRecyclerView.this.adapter.getReorderSectionId(viewHolder2.getAdapterPosition())) {
                return false;
            }
            UniversalRecyclerView.this.adapter.swapElements(viewHolder.getAdapterPosition(), viewHolder2.getAdapterPosition());
            return true;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder != null) {
                UniversalRecyclerView.this.hideSelector(false);
            }
            if (i == 0) {
                UniversalRecyclerView.this.adapter.reorderDone();
            } else {
                UniversalRecyclerView.this.cancelClickRunnables(false);
                if (viewHolder != null) {
                    viewHolder.itemView.setPressed(true);
                }
            }
            super.onSelectedChanged(viewHolder, i);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        }
    }

    public UniversalRecyclerView(Context context, int i, int i2, Utilities.Callback2 callback2, Utilities.Callback5 callback5, Utilities.Callback5Return callback5Return, Theme.ResourcesProvider resourcesProvider) {
        this(context, i, i2, false, callback2, callback5, callback5Return, resourcesProvider);
    }

    public UniversalRecyclerView(Context context, int i, int i2, boolean z, Utilities.Callback2 callback2, Utilities.Callback5 callback5, Utilities.Callback5Return callback5Return, Theme.ResourcesProvider resourcesProvider) {
        this(context, i, i2, z, callback2, callback5, callback5Return, resourcesProvider, -1);
    }

    public UniversalRecyclerView(Context context, int i, int i2, boolean z, Utilities.Callback2 callback2, final Utilities.Callback5 callback5, final Utilities.Callback5Return callback5Return, Theme.ResourcesProvider resourcesProvider, int i3) {
        super(context, resourcesProvider);
        boolean z2 = false;
        if (i3 == -1) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, z2) { // from class: org.telegram.ui.Components.UniversalRecyclerView.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // androidx.recyclerview.widget.LinearLayoutManager
                public int getExtraLayoutSpace(RecyclerView.State state) {
                    return UniversalRecyclerView.this.doNotDetachViews ? AndroidUtilities.displaySize.y : super.getExtraLayoutSpace(state);
                }
            };
            this.layoutManager = linearLayoutManager;
            setLayoutManager(linearLayoutManager);
        } else {
            final ExtendedGridLayoutManager extendedGridLayoutManager = new ExtendedGridLayoutManager(context, i3) { // from class: org.telegram.ui.Components.UniversalRecyclerView.2
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // androidx.recyclerview.widget.LinearLayoutManager
                public int getExtraLayoutSpace(RecyclerView.State state) {
                    return UniversalRecyclerView.this.doNotDetachViews ? AndroidUtilities.displaySize.y : super.getExtraLayoutSpace(state);
                }
            };
            extendedGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.Components.UniversalRecyclerView.3
                @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                public int getSpanSize(int i4) {
                    UItem item;
                    int i5;
                    UniversalAdapter universalAdapter = UniversalRecyclerView.this.adapter;
                    return (universalAdapter == null || (item = universalAdapter.getItem(i4)) == null || (i5 = item.spanCount) == -1) ? extendedGridLayoutManager.getSpanCount() : i5;
                }
            });
            this.layoutManager = extendedGridLayoutManager;
            setLayoutManager(extendedGridLayoutManager);
        }
        UniversalAdapter universalAdapter = new UniversalAdapter(this, context, i, i2, z, callback2, resourcesProvider);
        this.adapter = universalAdapter;
        setAdapter(universalAdapter);
        if (callback5 != null) {
            setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.Components.UniversalRecyclerView$$ExternalSyntheticLambda0
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public /* synthetic */ boolean hasDoubleTap(View view, int i4) {
                    return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i4);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public /* synthetic */ void onDoubleTap(View view, int i4, float f, float f2) {
                    RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i4, f, f2);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public final void onItemClick(View view, int i4, float f, float f2) {
                    UniversalRecyclerView.this.lambda$new$0(callback5, view, i4, f, f2);
                }
            });
        }
        if (callback5Return != null) {
            setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListenerExtended() { // from class: org.telegram.ui.Components.UniversalRecyclerView$$ExternalSyntheticLambda1
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
                public final boolean onItemClick(View view, int i4, float f, float f2) {
                    boolean lambda$new$1;
                    lambda$new$1 = UniversalRecyclerView.this.lambda$new$1(callback5Return, view, i4, f, f2);
                    return lambda$new$1;
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
                public /* synthetic */ void onLongClickRelease() {
                    RecyclerListView.OnItemLongClickListenerExtended.-CC.$default$onLongClickRelease(this);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
                public /* synthetic */ void onMove(float f, float f2) {
                    RecyclerListView.OnItemLongClickListenerExtended.-CC.$default$onMove(this, f, f2);
                }
            });
        }
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator() { // from class: org.telegram.ui.Components.UniversalRecyclerView.4
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.DefaultItemAnimator
            public void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                super.onMoveAnimationUpdate(viewHolder);
                UniversalRecyclerView.this.invalidate();
            }
        };
        defaultItemAnimator.setSupportsChangeAnimations(false);
        defaultItemAnimator.setDelayAnimations(false);
        defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        defaultItemAnimator.setDurations(350L);
        setItemAnimator(defaultItemAnimator);
    }

    public UniversalRecyclerView(BaseFragment baseFragment, Utilities.Callback2 callback2, Utilities.Callback5 callback5, Utilities.Callback5Return callback5Return) {
        this(baseFragment.getContext(), baseFragment.getCurrentAccount(), baseFragment.getClassGuid(), callback2, callback5, callback5Return, baseFragment.getResourceProvider());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$allowReorder$2(View view) {
        this.adapter.updateReorder(getChildViewHolder(view), this.reorderingAllowed);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Utilities.Callback5 callback5, View view, int i, float f, float f2) {
        UItem item = this.adapter.getItem(i);
        if (item == null) {
            return;
        }
        callback5.run(item, view, Integer.valueOf(i), Float.valueOf(f), Float.valueOf(f2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$1(Utilities.Callback5Return callback5Return, View view, int i, float f, float f2) {
        UItem item = this.adapter.getItem(i);
        if (item == null) {
            return false;
        }
        return ((Boolean) callback5Return.run(item, view, Integer.valueOf(i), Float.valueOf(f), Float.valueOf(f2))).booleanValue();
    }

    public void allowReorder(boolean z) {
        if (this.reorderingAllowed == z) {
            return;
        }
        UniversalAdapter universalAdapter = this.adapter;
        this.reorderingAllowed = z;
        universalAdapter.updateReorder(z);
        AndroidUtilities.forEachViews((RecyclerView) this, new Consumer() { // from class: org.telegram.ui.Components.UniversalRecyclerView$$ExternalSyntheticLambda2
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                UniversalRecyclerView.this.lambda$allowReorder$2((View) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        this.adapter.drawWhiteSections(canvas, this);
        super.dispatchDraw(canvas);
    }

    public void doNotDetachViews() {
        this.doNotDetachViews = true;
    }

    public int findPositionByItemId(int i) {
        for (int i2 = 0; i2 < this.adapter.getItemCount(); i2++) {
            UItem item = this.adapter.getItem(i2);
            if (item != null && item.id == i) {
                return i2;
            }
        }
        return -1;
    }

    public View findViewByItemId(int i) {
        int i2 = 0;
        while (true) {
            if (i2 >= this.adapter.getItemCount()) {
                i2 = -1;
                break;
            }
            UItem item = this.adapter.getItem(i2);
            if (item != null && item.id == i) {
                break;
            }
            i2++;
        }
        return findViewByPosition(i2);
    }

    public View findViewByPosition(int i) {
        if (i == -1) {
            return null;
        }
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View childAt = getChildAt(i2);
            int childAdapterPosition = getChildAdapterPosition(childAt);
            if (childAdapterPosition != -1 && childAdapterPosition == i) {
                return childAt;
            }
        }
        return null;
    }

    public void listenReorder(Utilities.Callback2 callback2) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelperCallback());
        this.itemTouchHelper = itemTouchHelper;
        itemTouchHelper.attachToRecyclerView(this);
        this.adapter.listenReorder(callback2);
    }

    public void setSpanCount(int i) {
        LinearLayoutManager linearLayoutManager = this.layoutManager;
        if (linearLayoutManager instanceof ExtendedGridLayoutManager) {
            ((ExtendedGridLayoutManager) linearLayoutManager).setSpanCount(i);
            return;
        }
        if (!(linearLayoutManager instanceof LinearLayoutManager) || i == -1) {
            return;
        }
        final ExtendedGridLayoutManager extendedGridLayoutManager = new ExtendedGridLayoutManager(getContext(), i) { // from class: org.telegram.ui.Components.UniversalRecyclerView.5
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.LinearLayoutManager
            public int getExtraLayoutSpace(RecyclerView.State state) {
                return UniversalRecyclerView.this.doNotDetachViews ? AndroidUtilities.displaySize.y : super.getExtraLayoutSpace(state);
            }
        };
        extendedGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.Components.UniversalRecyclerView.6
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i2) {
                UItem item;
                int i3;
                UniversalAdapter universalAdapter = UniversalRecyclerView.this.adapter;
                return (universalAdapter == null || (item = universalAdapter.getItem(i2)) == null || (i3 = item.spanCount) == -1) ? extendedGridLayoutManager.getSpanCount() : i3;
            }
        });
        this.layoutManager = extendedGridLayoutManager;
        setLayoutManager(extendedGridLayoutManager);
    }
}
