package org.telegram.ui.Components;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public abstract class BottomSheetWithRecyclerListView extends BottomSheet {
    protected ActionBar actionBar;
    private BaseFragment baseFragment;
    protected boolean clipToActionBar;
    private int contentHeight;
    public final boolean hasFixedSize;
    protected RecyclerListView recyclerListView;
    public float topPadding = 0.4f;
    boolean wasDrawn;

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean canDismissWithSwipe() {
        return false;
    }

    protected abstract RecyclerListView.SelectionAdapter createAdapter();

    protected abstract CharSequence getTitle();

    protected void onPreDraw(Canvas canvas, int i, float f) {
    }

    public void onPreMeasure(int i, int i2) {
    }

    public void onViewCreated(FrameLayout frameLayout) {
    }

    public BottomSheetWithRecyclerListView(BaseFragment baseFragment, boolean z, boolean z2) {
        super(baseFragment.getParentActivity(), z);
        this.baseFragment = baseFragment;
        this.hasFixedSize = z2;
        Activity parentActivity = baseFragment.getParentActivity();
        FrameLayout anonymousClass1 = new AnonymousClass1(parentActivity, z2, ContextCompat.getDrawable(parentActivity, 2131165446).mutate());
        RecyclerListView recyclerListView = new RecyclerListView(parentActivity);
        this.recyclerListView = recyclerListView;
        recyclerListView.setLayoutManager(new LinearLayoutManager(parentActivity));
        RecyclerListView.SelectionAdapter createAdapter = createAdapter();
        if (z2) {
            this.recyclerListView.setHasFixedSize(true);
            this.recyclerListView.setAdapter(createAdapter);
            setCustomView(anonymousClass1);
            anonymousClass1.addView(this.recyclerListView, LayoutHelper.createFrame(-1, -2.0f));
        } else {
            this.recyclerListView.setAdapter(new AnonymousClass2(createAdapter, parentActivity));
            this.containerView = anonymousClass1;
            AnonymousClass3 anonymousClass3 = new AnonymousClass3(parentActivity, anonymousClass1);
            this.actionBar = anonymousClass3;
            anonymousClass3.setBackgroundColor(getThemedColor("dialogBackground"));
            this.actionBar.setTitleColor(getThemedColor("windowBackgroundWhiteBlackText"));
            this.actionBar.setItemsBackgroundColor(getThemedColor("actionBarActionModeDefaultSelector"), false);
            this.actionBar.setItemsColor(getThemedColor("actionBarActionModeDefaultIcon"), false);
            this.actionBar.setCastShadows(true);
            this.actionBar.setBackButtonImage(2131165449);
            this.actionBar.setTitle(getTitle());
            this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass4());
            anonymousClass1.addView(this.recyclerListView);
            anonymousClass1.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f, 0, 6.0f, 0.0f, 6.0f, 0.0f));
            this.recyclerListView.addOnScrollListener(new AnonymousClass5(this, anonymousClass1));
        }
        onViewCreated(anonymousClass1);
        updateStatusBar();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.Components.BottomSheetWithRecyclerListView$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends FrameLayout {
        final /* synthetic */ boolean val$hasFixedSize;
        final /* synthetic */ Drawable val$headerShadowDrawable;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context, boolean z, Drawable drawable) {
            super(context);
            BottomSheetWithRecyclerListView.this = r1;
            this.val$hasFixedSize = z;
            this.val$headerShadowDrawable = drawable;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            BottomSheetWithRecyclerListView.this.contentHeight = View.MeasureSpec.getSize(i2);
            BottomSheetWithRecyclerListView.this.onPreMeasure(i, i2);
            super.onMeasure(i, i2);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            if (!this.val$hasFixedSize) {
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = BottomSheetWithRecyclerListView.this.recyclerListView.findViewHolderForAdapterPosition(0);
                int i = -AndroidUtilities.dp(16.0f);
                if (findViewHolderForAdapterPosition != null) {
                    i = findViewHolderForAdapterPosition.itemView.getBottom() - AndroidUtilities.dp(16.0f);
                }
                float dp = 1.0f - ((AndroidUtilities.dp(16.0f) + i) / AndroidUtilities.dp(56.0f));
                if (dp < 0.0f) {
                    dp = 0.0f;
                }
                BottomSheetWithRecyclerListView bottomSheetWithRecyclerListView = BottomSheetWithRecyclerListView.this;
                AndroidUtilities.updateViewVisibilityAnimated(bottomSheetWithRecyclerListView.actionBar, dp != 0.0f, 1.0f, bottomSheetWithRecyclerListView.wasDrawn);
                ((BottomSheet) BottomSheetWithRecyclerListView.this).shadowDrawable.setBounds(0, i, getMeasuredWidth(), getMeasuredHeight());
                ((BottomSheet) BottomSheetWithRecyclerListView.this).shadowDrawable.draw(canvas);
                BottomSheetWithRecyclerListView.this.onPreDraw(canvas, i, dp);
            }
            super.dispatchDraw(canvas);
            ActionBar actionBar = BottomSheetWithRecyclerListView.this.actionBar;
            if (actionBar != null && actionBar.getVisibility() == 0 && BottomSheetWithRecyclerListView.this.actionBar.getAlpha() != 0.0f) {
                this.val$headerShadowDrawable.setBounds(0, BottomSheetWithRecyclerListView.this.actionBar.getBottom(), getMeasuredWidth(), BottomSheetWithRecyclerListView.this.actionBar.getBottom() + this.val$headerShadowDrawable.getIntrinsicHeight());
                this.val$headerShadowDrawable.setAlpha((int) (BottomSheetWithRecyclerListView.this.actionBar.getAlpha() * 255.0f));
                this.val$headerShadowDrawable.draw(canvas);
            }
            BottomSheetWithRecyclerListView.this.wasDrawn = true;
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (!this.val$hasFixedSize) {
                BottomSheetWithRecyclerListView bottomSheetWithRecyclerListView = BottomSheetWithRecyclerListView.this;
                if (bottomSheetWithRecyclerListView.clipToActionBar && view == bottomSheetWithRecyclerListView.recyclerListView) {
                    canvas.save();
                    canvas.clipRect(0, BottomSheetWithRecyclerListView.this.actionBar.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
                    super.drawChild(canvas, view, j);
                    canvas.restore();
                    return true;
                }
            }
            return super.drawChild(canvas, view, j);
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && motionEvent.getY() < ((BottomSheet) BottomSheetWithRecyclerListView.this).shadowDrawable.getBounds().top) {
                BottomSheetWithRecyclerListView.this.dismiss();
            }
            return super.dispatchTouchEvent(motionEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.Components.BottomSheetWithRecyclerListView$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends RecyclerListView.SelectionAdapter {
        final /* synthetic */ RecyclerListView.SelectionAdapter val$adapter;
        final /* synthetic */ Context val$context;

        AnonymousClass2(RecyclerListView.SelectionAdapter selectionAdapter, Context context) {
            BottomSheetWithRecyclerListView.this = r1;
            this.val$adapter = selectionAdapter;
            this.val$context = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return this.val$adapter.isEnabled(viewHolder);
        }

        /* renamed from: org.telegram.ui.Components.BottomSheetWithRecyclerListView$2$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends View {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context) {
                super(context);
                AnonymousClass2.this = r1;
            }

            @Override // android.view.View
            protected void onMeasure(int i, int i2) {
                int i3;
                if (BottomSheetWithRecyclerListView.this.contentHeight != 0) {
                    i3 = (int) (BottomSheetWithRecyclerListView.this.contentHeight * BottomSheetWithRecyclerListView.this.topPadding);
                } else {
                    i3 = AndroidUtilities.dp(300.0f);
                }
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(i3, 1073741824));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            if (i == -1000) {
                return new RecyclerListView.Holder(new AnonymousClass1(this.val$context));
            }
            return this.val$adapter.onCreateViewHolder(viewGroup, i);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (i != 0) {
                this.val$adapter.onBindViewHolder(viewHolder, i - 1);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == 0) {
                return -1000;
            }
            return this.val$adapter.getItemViewType(i - 1);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.val$adapter.getItemCount() + 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.Components.BottomSheetWithRecyclerListView$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends ActionBar {
        final /* synthetic */ FrameLayout val$containerView;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context, FrameLayout frameLayout) {
            super(context);
            BottomSheetWithRecyclerListView.this = r1;
            this.val$containerView = frameLayout;
        }

        @Override // android.view.View
        public void setAlpha(float f) {
            if (getAlpha() != f) {
                super.setAlpha(f);
                this.val$containerView.invalidate();
            }
        }

        @Override // android.view.View
        public void setTag(Object obj) {
            super.setTag(obj);
            BottomSheetWithRecyclerListView.this.updateStatusBar();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.Components.BottomSheetWithRecyclerListView$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass4() {
            BottomSheetWithRecyclerListView.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                BottomSheetWithRecyclerListView.this.dismiss();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.Components.BottomSheetWithRecyclerListView$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends RecyclerView.OnScrollListener {
        final /* synthetic */ FrameLayout val$containerView;

        AnonymousClass5(BottomSheetWithRecyclerListView bottomSheetWithRecyclerListView, FrameLayout frameLayout) {
            this.val$containerView = frameLayout;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            super.onScrolled(recyclerView, i, i2);
            this.val$containerView.invalidate();
        }
    }

    private boolean isLightStatusBar() {
        return ColorUtils.calculateLuminance(Theme.getColor("dialogBackground")) > 0.699999988079071d;
    }

    public void notifyDataSetChanged() {
        this.recyclerListView.getAdapter().notifyDataSetChanged();
    }

    public BaseFragment getBaseFragment() {
        return this.baseFragment;
    }

    public void updateStatusBar() {
        ActionBar actionBar = this.actionBar;
        if (actionBar != null && actionBar.getTag() != null) {
            AndroidUtilities.setLightStatusBar(getWindow(), isLightStatusBar());
        } else if (this.baseFragment == null) {
        } else {
            AndroidUtilities.setLightStatusBar(getWindow(), this.baseFragment.isLightStatusBar());
        }
    }
}
