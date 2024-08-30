package org.telegram.ui.Adapters;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes4.dex */
public class PaddedListAdapter extends RecyclerListView.SelectionAdapter {
    private int lastPadding;
    private RecyclerView.AdapterDataObserver mDataObserver;
    public View paddingView;
    private RecyclerListView.SelectionAdapter wrappedAdapter;
    private final int PADDING_VIEW_TYPE = -983904;
    private Integer padding = null;
    public boolean paddingViewAttached = false;

    public PaddedListAdapter(RecyclerListView.SelectionAdapter selectionAdapter) {
        RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() { // from class: org.telegram.ui.Adapters.PaddedListAdapter.2
            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onChanged() {
                super.onChanged();
                PaddedListAdapter.this.notifyDataSetChanged();
            }

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeChanged(int i, int i2) {
                super.onItemRangeChanged(i, i2);
                PaddedListAdapter.this.notifyItemRangeChanged(i + 1, i2);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeInserted(int i, int i2) {
                super.onItemRangeInserted(i, i2);
                PaddedListAdapter.this.notifyItemRangeInserted(i + 1, i2);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeMoved(int i, int i2, int i3) {
                super.onItemRangeMoved(i, i2, i3);
                PaddedListAdapter.this.notifyItemRangeChanged(i + 1, i2 + 1 + i3);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeRemoved(int i, int i2) {
                super.onItemRangeRemoved(i, i2);
                PaddedListAdapter.this.notifyItemRangeRemoved(i + 1, i2);
            }
        };
        this.mDataObserver = adapterDataObserver;
        this.wrappedAdapter = selectionAdapter;
        selectionAdapter.registerAdapterDataObserver(adapterDataObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPadding(int i) {
        Integer num = this.padding;
        int intValue = num != null ? num.intValue() : 0;
        this.lastPadding = intValue;
        return intValue;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.wrappedAdapter.getItemCount() + 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (i == 0) {
            return -983904;
        }
        return this.wrappedAdapter.getItemViewType(i - 1);
    }

    public int getPadding() {
        return this.lastPadding;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
    public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getAdapterPosition() == 0) {
            return false;
        }
        return this.wrappedAdapter.isEnabled(viewHolder);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (i > 0) {
            this.wrappedAdapter.onBindViewHolder(viewHolder, i - 1);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == -983904) {
            View view = new View(viewGroup.getContext()) { // from class: org.telegram.ui.Adapters.PaddedListAdapter.1
                @Override // android.view.View
                protected void onAttachedToWindow() {
                    super.onAttachedToWindow();
                    PaddedListAdapter.this.paddingViewAttached = true;
                }

                @Override // android.view.View
                protected void onDetachedFromWindow() {
                    super.onDetachedFromWindow();
                    PaddedListAdapter.this.paddingViewAttached = false;
                }

                @Override // android.view.View
                protected void onMeasure(int i2, int i3) {
                    super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec(PaddedListAdapter.this.getPadding(((View) getParent()).getMeasuredHeight()), 1073741824));
                }
            };
            this.paddingView = view;
            return new RecyclerListView.Holder(view);
        }
        return this.wrappedAdapter.onCreateViewHolder(viewGroup, i);
    }

    public void setPadding(int i) {
        this.padding = Integer.valueOf(i);
        View view = this.paddingView;
        if (view != null) {
            view.requestLayout();
        }
    }
}
