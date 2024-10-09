package org.telegram.ui.Components;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.WallpaperCell;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.WallpapersListActivity;

/* loaded from: classes3.dex */
public class ChatAttachAlertColorsLayout extends ChatAttachAlert.AttachAlertLayout {
    Adapter adapter;
    public int currentItemTop;
    public RecyclerListView gridView;
    private int itemSize;
    private int itemsPerRow;
    GridLayoutManager layoutManager;
    Consumer wallpaperConsumer;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class Adapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;
        private final ArrayList wallpapers = new ArrayList();

        public Adapter(Context context) {
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.wallpapers.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            WallpaperCell wallpaperCell = (WallpaperCell) viewHolder.itemView;
            wallpaperCell.setParams(1, false, false);
            wallpaperCell.setSize(ChatAttachAlertColorsLayout.this.itemSize);
            wallpaperCell.setWallpaper(1, 0, this.wallpapers.get(i), null, null, false);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            WallpaperCell wallpaperCell = new WallpaperCell(this.mContext, 1) { // from class: org.telegram.ui.Components.ChatAttachAlertColorsLayout.Adapter.1
                @Override // org.telegram.ui.Cells.WallpaperCell
                protected void onWallpaperClick(Object obj, int i2) {
                    Consumer consumer = ChatAttachAlertColorsLayout.this.wallpaperConsumer;
                    if (consumer != null) {
                        consumer.accept(obj);
                    }
                }
            };
            wallpaperCell.drawStubBackground = false;
            return new RecyclerListView.Holder(wallpaperCell);
        }
    }

    public ChatAttachAlertColorsLayout(ChatAttachAlert chatAttachAlert, Context context, Theme.ResourcesProvider resourcesProvider) {
        super(chatAttachAlert, context, resourcesProvider);
        this.itemSize = AndroidUtilities.dp(80.0f);
        this.itemsPerRow = 3;
        this.currentItemTop = 0;
        RecyclerListView recyclerListView = new RecyclerListView(context, resourcesProvider) { // from class: org.telegram.ui.Components.ChatAttachAlertColorsLayout.1
            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() != 0 || motionEvent.getY() >= ChatAttachAlertColorsLayout.this.parentAlert.scrollOffsetY[0] - AndroidUtilities.dp(80.0f)) {
                    return super.onInterceptTouchEvent(motionEvent);
                }
                return false;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
            public void onLayout(boolean z, int i, int i2, int i3, int i4) {
                super.onLayout(z, i, i2, i3, i4);
                PhotoViewer.getInstance().checkCurrentImageVisibility();
            }

            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() != 0 || motionEvent.getY() >= ChatAttachAlertColorsLayout.this.parentAlert.scrollOffsetY[0] - AndroidUtilities.dp(80.0f)) {
                    return super.onTouchEvent(motionEvent);
                }
                return false;
            }
        };
        this.gridView = recyclerListView;
        Adapter adapter = new Adapter(context);
        this.adapter = adapter;
        recyclerListView.setAdapter(adapter);
        this.gridView.setClipToPadding(false);
        this.gridView.setItemAnimator(null);
        this.gridView.setLayoutAnimation(null);
        this.gridView.setVerticalScrollBarEnabled(false);
        this.gridView.setGlowColor(getThemedColor(Theme.key_dialogScrollGlow));
        addView(this.gridView, LayoutHelper.createFrame(-1, -1.0f));
        this.gridView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.ChatAttachAlertColorsLayout.2
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                RecyclerListView.Holder holder;
                if (i == 0) {
                    int dp = AndroidUtilities.dp(13.0f);
                    ActionBarMenuItem actionBarMenuItem = ChatAttachAlertColorsLayout.this.parentAlert.selectedMenuItem;
                    int dp2 = dp + (actionBarMenuItem != null ? AndroidUtilities.dp(actionBarMenuItem.getAlpha() * 26.0f) : 0);
                    int backgroundPaddingTop = ChatAttachAlertColorsLayout.this.parentAlert.getBackgroundPaddingTop();
                    if (((ChatAttachAlertColorsLayout.this.parentAlert.scrollOffsetY[0] - backgroundPaddingTop) - dp2) + backgroundPaddingTop >= ActionBar.getCurrentActionBarHeight() || (holder = (RecyclerListView.Holder) ChatAttachAlertColorsLayout.this.gridView.findViewHolderForAdapterPosition(0)) == null || holder.itemView.getTop() <= AndroidUtilities.dp(7.0f)) {
                        return;
                    }
                    ChatAttachAlertColorsLayout.this.gridView.smoothScrollBy(0, holder.itemView.getTop() - AndroidUtilities.dp(7.0f));
                }
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                if (ChatAttachAlertColorsLayout.this.gridView.getChildCount() <= 0) {
                    return;
                }
                ChatAttachAlertColorsLayout chatAttachAlertColorsLayout = ChatAttachAlertColorsLayout.this;
                chatAttachAlertColorsLayout.parentAlert.updateLayout(chatAttachAlertColorsLayout, true, i2);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, this.itemSize) { // from class: org.telegram.ui.Components.ChatAttachAlertColorsLayout.3
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
                LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) { // from class: org.telegram.ui.Components.ChatAttachAlertColorsLayout.3.1
                    @Override // androidx.recyclerview.widget.LinearSmoothScroller
                    public int calculateDyToMakeVisible(View view, int i2) {
                        return super.calculateDyToMakeVisible(view, i2) - (ChatAttachAlertColorsLayout.this.gridView.getPaddingTop() - AndroidUtilities.dp(7.0f));
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // androidx.recyclerview.widget.LinearSmoothScroller
                    public int calculateTimeForDeceleration(int i2) {
                        return super.calculateTimeForDeceleration(i2) * 2;
                    }
                };
                linearSmoothScroller.setTargetPosition(i);
                startSmoothScroll(linearSmoothScroller);
            }

            @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        this.layoutManager = gridLayoutManager;
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.Components.ChatAttachAlertColorsLayout.4
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i) {
                return ChatAttachAlertColorsLayout.this.itemSize + (i % ChatAttachAlertColorsLayout.this.itemsPerRow != ChatAttachAlertColorsLayout.this.itemsPerRow + (-1) ? AndroidUtilities.dp(5.0f) : 0);
            }
        });
        this.gridView.setLayoutManager(this.layoutManager);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getCurrentItemTop() {
        if (this.gridView.getChildCount() <= 0) {
            RecyclerListView recyclerListView = this.gridView;
            int paddingTop = recyclerListView.getPaddingTop();
            this.currentItemTop = paddingTop;
            recyclerListView.setTopGlowOffset(paddingTop);
            return ConnectionsManager.DEFAULT_DATACENTER_ID;
        }
        View childAt = this.gridView.getChildAt(0);
        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.gridView.findContainingViewHolder(childAt);
        int top = childAt.getTop();
        int dp = AndroidUtilities.dp(7.0f);
        if (top < AndroidUtilities.dp(7.0f) || holder == null || holder.getAdapterPosition() != 0) {
            top = dp;
        }
        this.gridView.setTopGlowOffset(top);
        this.currentItemTop = top;
        return top;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getFirstOffset() {
        return getListTopPadding() + AndroidUtilities.dp(56.0f);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getListTopPadding() {
        return this.gridView.getPaddingTop();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int needsActionBar() {
        return 1;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00a7  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00b0  */
    /* JADX WARN: Removed duplicated region for block: B:23:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0036  */
    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onPreMeasure(int i, int i2) {
        int i3;
        int dp;
        int i4;
        int i5;
        if (!AndroidUtilities.isTablet()) {
            android.graphics.Point point = AndroidUtilities.displaySize;
            if (point.x <= point.y) {
                i3 = 3;
                this.itemsPerRow = i3;
                ((FrameLayout.LayoutParams) getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
                dp = ((i - AndroidUtilities.dp(12.0f)) - AndroidUtilities.dp(10.0f)) / this.itemsPerRow;
                if (this.itemSize != dp) {
                    this.itemSize = dp;
                    this.adapter.notifyDataSetChanged();
                }
                this.layoutManager.setSpanCount(Math.max(1, (this.itemsPerRow * dp) + (AndroidUtilities.dp(5.0f) * (this.itemsPerRow - 1))));
                int ceil = (int) Math.ceil((this.adapter.getItemCount() - 1) / this.itemsPerRow);
                Math.max(0, ((i2 - ((dp * ceil) + ((ceil - 1) * AndroidUtilities.dp(5.0f)))) - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.dp(60.0f));
                if (!AndroidUtilities.isTablet()) {
                    android.graphics.Point point2 = AndroidUtilities.displaySize;
                    if (point2.x > point2.y) {
                        i4 = (int) (i2 / 3.5f);
                        int dp2 = i4 - AndroidUtilities.dp(52.0f);
                        i5 = dp2 >= 0 ? dp2 : 0;
                        if (this.gridView.getPaddingTop() != i5) {
                            this.gridView.setPadding(AndroidUtilities.dp(6.0f), i5, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(48.0f));
                            return;
                        }
                        return;
                    }
                }
                i4 = (i2 / 5) * 2;
                int dp22 = i4 - AndroidUtilities.dp(52.0f);
                if (dp22 >= 0) {
                }
                if (this.gridView.getPaddingTop() != i5) {
                }
            }
        }
        i3 = 4;
        this.itemsPerRow = i3;
        ((FrameLayout.LayoutParams) getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
        dp = ((i - AndroidUtilities.dp(12.0f)) - AndroidUtilities.dp(10.0f)) / this.itemsPerRow;
        if (this.itemSize != dp) {
        }
        this.layoutManager.setSpanCount(Math.max(1, (this.itemsPerRow * dp) + (AndroidUtilities.dp(5.0f) * (this.itemsPerRow - 1))));
        int ceil2 = (int) Math.ceil((this.adapter.getItemCount() - 1) / this.itemsPerRow);
        Math.max(0, ((i2 - ((dp * ceil2) + ((ceil2 - 1) * AndroidUtilities.dp(5.0f)))) - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.dp(60.0f));
        if (!AndroidUtilities.isTablet()) {
        }
        i4 = (i2 / 5) * 2;
        int dp222 = i4 - AndroidUtilities.dp(52.0f);
        if (dp222 >= 0) {
        }
        if (this.gridView.getPaddingTop() != i5) {
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onShow(ChatAttachAlert.AttachAlertLayout attachAlertLayout) {
        try {
            this.parentAlert.actionBar.getTitleTextView().setBuildFullLayout(true);
        } catch (Exception unused) {
        }
        this.parentAlert.actionBar.setTitle(LocaleController.getString(R.string.SelectColor));
        this.layoutManager.scrollToPositionWithOffset(0, 0);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void scrollToTop() {
        this.gridView.smoothScrollToPosition(0);
    }

    public void setDelegate(Consumer consumer) {
        this.wallpaperConsumer = consumer;
    }

    @Override // android.view.View
    public void setTranslationY(float f) {
        super.setTranslationY(f);
        this.parentAlert.getSheetContainer().invalidate();
        invalidate();
    }

    public void updateColors(boolean z) {
        this.adapter.wallpapers.clear();
        WallpapersListActivity.fillDefaultColors(this.adapter.wallpapers, z);
        this.adapter.notifyDataSetChanged();
    }
}
