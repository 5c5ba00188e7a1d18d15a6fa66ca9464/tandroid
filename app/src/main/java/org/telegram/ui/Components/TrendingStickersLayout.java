package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetID;
import org.telegram.tgnet.TLRPC$TL_messages_featuredStickers;
import org.telegram.tgnet.TLRPC$TL_messages_getOldFeaturedStickers;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.StickersSearchAdapter;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.FeaturedStickerSetCell2;
import org.telegram.ui.Cells.FeaturedStickerSetInfoCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.StickerEmojiCell;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickersAlert;
/* loaded from: classes3.dex */
public class TrendingStickersLayout extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private final TrendingStickersAdapter adapter;
    private final int currentAccount;
    private final Delegate delegate;
    ValueAnimator glueToTopAnimator;
    private boolean gluedToTop;
    private long hash;
    private float highlightProgress;
    private boolean ignoreLayout;
    private final LongSparseArray<TLRPC$StickerSetCovered> installingStickerSets;
    private final GridLayoutManager layoutManager;
    private final RecyclerListView listView;
    private boolean loaded;
    private boolean motionEventCatchedByListView;
    private RecyclerView.OnScrollListener onScrollListener;
    Paint paint;
    private BaseFragment parentFragment;
    private final TLRPC$StickerSetCovered[] primaryInstallingStickerSets;
    private final LongSparseArray<TLRPC$StickerSetCovered> removingStickerSets;
    private final Theme.ResourcesProvider resourcesProvider;
    private boolean scrollFromAnimator;
    private TLRPC$StickerSetCovered scrollToSet;
    private final StickersSearchAdapter searchAdapter;
    private final FrameLayout searchLayout;
    private final SearchField searchView;
    private final View shadowView;
    private boolean shadowVisible;
    private int topOffset;
    private boolean wasLayout;

    /* loaded from: classes3.dex */
    public static abstract class Delegate {
        private String[] lastSearchKeyboardLanguage = new String[0];

        public boolean canSchedule() {
            return false;
        }

        public boolean canSendSticker() {
            return false;
        }

        public boolean isInScheduleMode() {
            return false;
        }

        public boolean onListViewInterceptTouchEvent(RecyclerListView recyclerListView, MotionEvent motionEvent) {
            return false;
        }

        public boolean onListViewTouchEvent(RecyclerListView recyclerListView, RecyclerListView.OnItemClickListener onItemClickListener, MotionEvent motionEvent) {
            return false;
        }

        public void onStickerSelected(TLRPC$Document tLRPC$Document, Object obj, boolean z, boolean z2, int i) {
        }

        public abstract void onStickerSetAdd(TLRPC$StickerSetCovered tLRPC$StickerSetCovered, boolean z);

        public abstract void onStickerSetRemove(TLRPC$StickerSetCovered tLRPC$StickerSetCovered);

        public String[] getLastSearchKeyboardLanguage() {
            return this.lastSearchKeyboardLanguage;
        }

        public void setLastSearchKeyboardLanguage(String[] strArr) {
            this.lastSearchKeyboardLanguage = strArr;
        }
    }

    public TrendingStickersLayout(Context context, Delegate delegate) {
        this(context, delegate, new TLRPC$StickerSetCovered[10], new LongSparseArray(), new LongSparseArray(), null, null);
    }

    public TrendingStickersLayout(Context context, Delegate delegate, TLRPC$StickerSetCovered[] tLRPC$StickerSetCoveredArr, LongSparseArray<TLRPC$StickerSetCovered> longSparseArray, LongSparseArray<TLRPC$StickerSetCovered> longSparseArray2, TLRPC$StickerSetCovered tLRPC$StickerSetCovered, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        int i = UserConfig.selectedAccount;
        this.currentAccount = i;
        this.highlightProgress = 1.0f;
        this.paint = new Paint();
        this.delegate = delegate;
        this.primaryInstallingStickerSets = tLRPC$StickerSetCoveredArr;
        this.installingStickerSets = longSparseArray;
        this.removingStickerSets = longSparseArray2;
        this.scrollToSet = tLRPC$StickerSetCovered;
        this.resourcesProvider = resourcesProvider;
        TrendingStickersAdapter trendingStickersAdapter = new TrendingStickersAdapter(context);
        this.adapter = trendingStickersAdapter;
        this.searchAdapter = new StickersSearchAdapter(context, new AnonymousClass1(delegate), tLRPC$StickerSetCoveredArr, longSparseArray, longSparseArray2, resourcesProvider);
        FrameLayout frameLayout = new FrameLayout(context);
        this.searchLayout = frameLayout;
        frameLayout.setBackgroundColor(getThemedColor("dialogBackground"));
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(context, true, resourcesProvider);
        this.searchView = anonymousClass2;
        anonymousClass2.setHint(LocaleController.getString("SearchTrendingStickersHint", 2131628188));
        frameLayout.addView(anonymousClass2, LayoutHelper.createFrame(-1, -1, 48));
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(context, delegate);
        this.listView = anonymousClass3;
        TrendingStickersLayout$$ExternalSyntheticLambda1 trendingStickersLayout$$ExternalSyntheticLambda1 = new TrendingStickersLayout$$ExternalSyntheticLambda1(this);
        anonymousClass3.setOnTouchListener(new TrendingStickersLayout$$ExternalSyntheticLambda0(this, delegate, trendingStickersLayout$$ExternalSyntheticLambda1));
        anonymousClass3.setOverScrollMode(2);
        anonymousClass3.setClipToPadding(false);
        anonymousClass3.setItemAnimator(null);
        anonymousClass3.setLayoutAnimation(null);
        AnonymousClass4 anonymousClass4 = new AnonymousClass4(context, 5, AndroidUtilities.dp(58.0f), anonymousClass3);
        this.layoutManager = anonymousClass4;
        anonymousClass3.setLayoutManager(anonymousClass4);
        anonymousClass4.setSpanSizeLookup(new AnonymousClass5());
        anonymousClass3.setOnScrollListener(new AnonymousClass6());
        anonymousClass3.setAdapter(trendingStickersAdapter);
        anonymousClass3.setOnItemClickListener(trendingStickersLayout$$ExternalSyntheticLambda1);
        addView(anonymousClass3, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        View view = new View(context);
        this.shadowView = view;
        view.setBackgroundColor(getThemedColor("dialogShadowLine"));
        view.setAlpha(0.0f);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight());
        layoutParams.topMargin = AndroidUtilities.dp(58.0f);
        addView(view, layoutParams);
        addView(frameLayout, LayoutHelper.createFrame(-1, 58, 51));
        updateColors();
        NotificationCenter notificationCenter = NotificationCenter.getInstance(i);
        notificationCenter.addObserver(this, NotificationCenter.stickersDidLoad);
        notificationCenter.addObserver(this, NotificationCenter.featuredStickersDidLoad);
    }

    /* renamed from: org.telegram.ui.Components.TrendingStickersLayout$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements StickersSearchAdapter.Delegate {
        final /* synthetic */ Delegate val$delegate;

        AnonymousClass1(Delegate delegate) {
            TrendingStickersLayout.this = r1;
            this.val$delegate = delegate;
        }

        @Override // org.telegram.ui.Adapters.StickersSearchAdapter.Delegate
        public void onSearchStart() {
            TrendingStickersLayout.this.searchView.getProgressDrawable().startAnimation();
        }

        @Override // org.telegram.ui.Adapters.StickersSearchAdapter.Delegate
        public void onSearchStop() {
            TrendingStickersLayout.this.searchView.getProgressDrawable().stopAnimation();
        }

        @Override // org.telegram.ui.Adapters.StickersSearchAdapter.Delegate
        public void setAdapterVisible(boolean z) {
            boolean z2 = true;
            if (!z || TrendingStickersLayout.this.listView.getAdapter() == TrendingStickersLayout.this.searchAdapter) {
                if (z || TrendingStickersLayout.this.listView.getAdapter() == TrendingStickersLayout.this.adapter) {
                    z2 = false;
                } else {
                    TrendingStickersLayout.this.listView.setAdapter(TrendingStickersLayout.this.adapter);
                }
            } else {
                TrendingStickersLayout.this.listView.setAdapter(TrendingStickersLayout.this.searchAdapter);
            }
            if (!z2 || TrendingStickersLayout.this.listView.getAdapter().getItemCount() <= 0) {
                return;
            }
            TrendingStickersLayout.this.layoutManager.scrollToPositionWithOffset(0, (-TrendingStickersLayout.this.listView.getPaddingTop()) + AndroidUtilities.dp(58.0f) + TrendingStickersLayout.this.topOffset, false);
        }

        @Override // org.telegram.ui.Adapters.StickersSearchAdapter.Delegate
        public void onStickerSetAdd(TLRPC$StickerSetCovered tLRPC$StickerSetCovered, boolean z) {
            this.val$delegate.onStickerSetAdd(tLRPC$StickerSetCovered, z);
        }

        @Override // org.telegram.ui.Adapters.StickersSearchAdapter.Delegate
        public void onStickerSetRemove(TLRPC$StickerSetCovered tLRPC$StickerSetCovered) {
            this.val$delegate.onStickerSetRemove(tLRPC$StickerSetCovered);
        }

        @Override // org.telegram.ui.Adapters.StickersSearchAdapter.Delegate
        public int getStickersPerRow() {
            return TrendingStickersLayout.this.adapter.stickersPerRow;
        }

        @Override // org.telegram.ui.Adapters.StickersSearchAdapter.Delegate
        public String[] getLastSearchKeyboardLanguage() {
            return this.val$delegate.getLastSearchKeyboardLanguage();
        }

        @Override // org.telegram.ui.Adapters.StickersSearchAdapter.Delegate
        public void setLastSearchKeyboardLanguage(String[] strArr) {
            this.val$delegate.setLastSearchKeyboardLanguage(strArr);
        }
    }

    /* renamed from: org.telegram.ui.Components.TrendingStickersLayout$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends SearchField {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context, boolean z, Theme.ResourcesProvider resourcesProvider) {
            super(context, z, resourcesProvider);
            TrendingStickersLayout.this = r1;
        }

        @Override // org.telegram.ui.Components.SearchField
        public void onTextChange(String str) {
            TrendingStickersLayout.this.searchAdapter.search(str);
        }
    }

    /* renamed from: org.telegram.ui.Components.TrendingStickersLayout$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends RecyclerListView {
        final /* synthetic */ Delegate val$delegate;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context, Delegate delegate) {
            super(context);
            TrendingStickersLayout.this = r1;
            this.val$delegate = delegate;
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return super.onInterceptTouchEvent(motionEvent) || this.val$delegate.onListViewInterceptTouchEvent(this, motionEvent);
        }

        @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            TrendingStickersLayout.this.motionEventCatchedByListView = true;
            return super.dispatchTouchEvent(motionEvent);
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (TrendingStickersLayout.this.glueToTopAnimator != null) {
                return false;
            }
            return super.onTouchEvent(motionEvent);
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (!TrendingStickersLayout.this.ignoreLayout) {
                super.requestLayout();
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView
        protected boolean allowSelectChildAtPosition(float f, float f2) {
            return f2 >= ((float) (TrendingStickersLayout.this.topOffset + AndroidUtilities.dp(58.0f)));
        }
    }

    public /* synthetic */ void lambda$new$0(View view, int i) {
        TLRPC$StickerSetCovered tLRPC$StickerSetCovered;
        RecyclerView.Adapter adapter = this.listView.getAdapter();
        StickersSearchAdapter stickersSearchAdapter = this.searchAdapter;
        if (adapter == stickersSearchAdapter) {
            tLRPC$StickerSetCovered = stickersSearchAdapter.getSetForPosition(i);
        } else {
            tLRPC$StickerSetCovered = i < this.adapter.totalItems ? (TLRPC$StickerSetCovered) this.adapter.positionsToSets.get(i) : null;
        }
        if (tLRPC$StickerSetCovered != null) {
            showStickerSet(tLRPC$StickerSetCovered.set);
        }
    }

    public /* synthetic */ boolean lambda$new$1(Delegate delegate, RecyclerListView.OnItemClickListener onItemClickListener, View view, MotionEvent motionEvent) {
        return delegate.onListViewTouchEvent(this.listView, onItemClickListener, motionEvent);
    }

    /* renamed from: org.telegram.ui.Components.TrendingStickersLayout$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends FillLastGridLayoutManager {
        @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context, int i, int i2, RecyclerView recyclerView) {
            super(context, i, i2, recyclerView);
            TrendingStickersLayout.this = r1;
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager
        public boolean isLayoutRTL() {
            return LocaleController.isRTL;
        }

        @Override // org.telegram.ui.Components.FillLastGridLayoutManager
        protected boolean shouldCalcLastItemHeight() {
            return TrendingStickersLayout.this.listView.getAdapter() == TrendingStickersLayout.this.searchAdapter;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public int scrollVerticallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
            int i2;
            View findViewByPosition;
            if (TrendingStickersLayout.this.scrollFromAnimator) {
                return super.scrollVerticallyBy(i, recycler, state);
            }
            TrendingStickersLayout trendingStickersLayout = TrendingStickersLayout.this;
            int i3 = 0;
            if (trendingStickersLayout.glueToTopAnimator != null) {
                return 0;
            }
            if (trendingStickersLayout.gluedToTop) {
                while (true) {
                    i2 = 1;
                    if (i3 >= getChildCount()) {
                        break;
                    }
                    int childAdapterPosition = TrendingStickersLayout.this.listView.getChildAdapterPosition(getChildAt(i3));
                    if (childAdapterPosition < 1) {
                        i2 = childAdapterPosition;
                        break;
                    }
                    i3++;
                }
                if (i2 == 0 && (findViewByPosition = TrendingStickersLayout.this.layoutManager.findViewByPosition(i2)) != null && findViewByPosition.getTop() - i > AndroidUtilities.dp(58.0f)) {
                    i = findViewByPosition.getTop() - AndroidUtilities.dp(58.0f);
                }
            }
            return super.scrollVerticallyBy(i, recycler, state);
        }
    }

    /* renamed from: org.telegram.ui.Components.TrendingStickersLayout$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends GridLayoutManager.SpanSizeLookup {
        AnonymousClass5() {
            TrendingStickersLayout.this = r1;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
        public int getSpanSize(int i) {
            if (TrendingStickersLayout.this.listView.getAdapter() != TrendingStickersLayout.this.adapter) {
                return TrendingStickersLayout.this.searchAdapter.getSpanSize(i);
            }
            if (!(TrendingStickersLayout.this.adapter.cache.get(i) instanceof Integer) && i < TrendingStickersLayout.this.adapter.totalItems) {
                return 1;
            }
            return TrendingStickersLayout.this.adapter.stickersPerRow;
        }
    }

    /* renamed from: org.telegram.ui.Components.TrendingStickersLayout$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 extends RecyclerView.OnScrollListener {
        AnonymousClass6() {
            TrendingStickersLayout.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            if (TrendingStickersLayout.this.onScrollListener != null) {
                TrendingStickersLayout.this.onScrollListener.onScrolled(TrendingStickersLayout.this.listView, i, i2);
            }
            if (i2 <= 0 || TrendingStickersLayout.this.listView.getAdapter() != TrendingStickersLayout.this.adapter || !TrendingStickersLayout.this.loaded || TrendingStickersLayout.this.adapter.loadingMore || TrendingStickersLayout.this.adapter.endReached) {
                return;
            }
            if (TrendingStickersLayout.this.layoutManager.findLastVisibleItemPosition() < (TrendingStickersLayout.this.adapter.getItemCount() - ((TrendingStickersLayout.this.adapter.stickersPerRow + 1) * 10)) - 1) {
                return;
            }
            TrendingStickersLayout.this.adapter.loadMoreStickerSets();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (TrendingStickersLayout.this.onScrollListener != null) {
                TrendingStickersLayout.this.onScrollListener.onScrollStateChanged(recyclerView, i);
            }
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        Integer num;
        super.onLayout(z, i, i2, i3, i4);
        if (!this.wasLayout) {
            this.wasLayout = true;
            this.adapter.refreshStickerSets();
            if (this.scrollToSet == null || (num = (Integer) this.adapter.setsToPosition.get(this.scrollToSet)) == null) {
                return;
            }
            this.layoutManager.scrollToPositionWithOffset(num.intValue(), (-this.listView.getPaddingTop()) + AndroidUtilities.dp(58.0f));
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        int i;
        float f = this.highlightProgress;
        if (f != 0.0f && this.scrollToSet != null) {
            float f2 = f - 0.0053333333f;
            this.highlightProgress = f2;
            if (f2 < 0.0f) {
                this.highlightProgress = 0.0f;
            } else {
                invalidate();
            }
            Integer num = (Integer) this.adapter.setsToPosition.get(this.scrollToSet);
            if (num != null) {
                View findViewByPosition = this.layoutManager.findViewByPosition(num.intValue());
                int i2 = -1;
                if (findViewByPosition != null) {
                    i2 = (int) findViewByPosition.getY();
                    i = ((int) findViewByPosition.getY()) + findViewByPosition.getMeasuredHeight();
                } else {
                    i = -1;
                }
                View findViewByPosition2 = this.layoutManager.findViewByPosition(num.intValue() + 1);
                if (findViewByPosition2 != null) {
                    if (findViewByPosition == null) {
                        i2 = (int) findViewByPosition2.getY();
                    }
                    i = ((int) findViewByPosition2.getY()) + findViewByPosition2.getMeasuredHeight();
                }
                if (findViewByPosition != null || findViewByPosition2 != null) {
                    this.paint.setColor(Theme.getColor("featuredStickers_addButton"));
                    float f3 = this.highlightProgress;
                    this.paint.setAlpha((int) ((f3 < 0.06f ? f3 / 0.06f : 1.0f) * 25.5f));
                    canvas.drawRect(0.0f, i2, getMeasuredWidth(), i, this.paint);
                }
            }
        }
        super.dispatchDraw(canvas);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateLastItemInAdapter();
        this.wasLayout = false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        this.motionEventCatchedByListView = false;
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        if (!this.motionEventCatchedByListView) {
            MotionEvent obtain = MotionEvent.obtain(motionEvent);
            this.listView.dispatchTouchEvent(obtain);
            obtain.recycle();
        }
        return dispatchTouchEvent;
    }

    private void showStickerSet(TLRPC$StickerSet tLRPC$StickerSet) {
        showStickerSet(tLRPC$StickerSet, null);
    }

    public void showStickerSet(TLRPC$StickerSet tLRPC$StickerSet, TLRPC$InputStickerSet tLRPC$InputStickerSet) {
        if (tLRPC$StickerSet != null) {
            tLRPC$InputStickerSet = new TLRPC$TL_inputStickerSetID();
            tLRPC$InputStickerSet.access_hash = tLRPC$StickerSet.access_hash;
            tLRPC$InputStickerSet.id = tLRPC$StickerSet.id;
        }
        if (tLRPC$InputStickerSet != null) {
            showStickerSet(tLRPC$InputStickerSet);
        }
    }

    /* renamed from: org.telegram.ui.Components.TrendingStickersLayout$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 implements StickersAlert.StickersAlertDelegate {
        AnonymousClass7() {
            TrendingStickersLayout.this = r1;
        }

        @Override // org.telegram.ui.Components.StickersAlert.StickersAlertDelegate
        public void onStickerSelected(TLRPC$Document tLRPC$Document, String str, Object obj, MessageObject.SendAnimationData sendAnimationData, boolean z, boolean z2, int i) {
            TrendingStickersLayout.this.delegate.onStickerSelected(tLRPC$Document, obj, z, z2, i);
        }

        @Override // org.telegram.ui.Components.StickersAlert.StickersAlertDelegate
        public boolean canSchedule() {
            return TrendingStickersLayout.this.delegate.canSchedule();
        }

        @Override // org.telegram.ui.Components.StickersAlert.StickersAlertDelegate
        public boolean isInScheduleMode() {
            return TrendingStickersLayout.this.delegate.isInScheduleMode();
        }
    }

    private void showStickerSet(TLRPC$InputStickerSet tLRPC$InputStickerSet) {
        StickersAlert stickersAlert = new StickersAlert(getContext(), this.parentFragment, tLRPC$InputStickerSet, null, this.delegate.canSendSticker() ? new AnonymousClass7() : null, this.resourcesProvider);
        stickersAlert.setShowTooltipWhenToggle(false);
        stickersAlert.setInstallDelegate(new AnonymousClass8(tLRPC$InputStickerSet));
        this.parentFragment.showDialog(stickersAlert);
    }

    /* renamed from: org.telegram.ui.Components.TrendingStickersLayout$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 implements StickersAlert.StickersAlertInstallDelegate {
        final /* synthetic */ TLRPC$InputStickerSet val$inputStickerSet;

        @Override // org.telegram.ui.Components.StickersAlert.StickersAlertInstallDelegate
        public void onStickerSetUninstalled() {
        }

        AnonymousClass8(TLRPC$InputStickerSet tLRPC$InputStickerSet) {
            TrendingStickersLayout.this = r1;
            this.val$inputStickerSet = tLRPC$InputStickerSet;
        }

        @Override // org.telegram.ui.Components.StickersAlert.StickersAlertInstallDelegate
        public void onStickerSetInstalled() {
            if (TrendingStickersLayout.this.listView.getAdapter() != TrendingStickersLayout.this.adapter) {
                TrendingStickersLayout.this.searchAdapter.installStickerSet(this.val$inputStickerSet);
                return;
            }
            for (int i = 0; i < TrendingStickersLayout.this.adapter.sets.size(); i++) {
                TLRPC$StickerSetCovered tLRPC$StickerSetCovered = (TLRPC$StickerSetCovered) TrendingStickersLayout.this.adapter.sets.get(i);
                if (tLRPC$StickerSetCovered.set.id == this.val$inputStickerSet.id) {
                    TrendingStickersLayout.this.adapter.installStickerSet(tLRPC$StickerSetCovered, null);
                    return;
                }
            }
        }
    }

    public void recycle() {
        NotificationCenter notificationCenter = NotificationCenter.getInstance(this.currentAccount);
        notificationCenter.removeObserver(this, NotificationCenter.stickersDidLoad);
        notificationCenter.removeObserver(this, NotificationCenter.featuredStickersDidLoad);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.stickersDidLoad) {
            if (((Integer) objArr[0]).intValue() != 0) {
                return;
            }
            if (this.loaded) {
                updateVisibleTrendingSets();
            } else {
                this.adapter.refreshStickerSets();
            }
        } else if (i != NotificationCenter.featuredStickersDidLoad) {
        } else {
            if (this.hash != MediaDataController.getInstance(this.currentAccount).getFeaturedStickersHashWithoutUnread(false)) {
                this.loaded = false;
            }
            if (this.loaded) {
                updateVisibleTrendingSets();
            } else {
                this.adapter.refreshStickerSets();
            }
        }
    }

    public void setOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void setParentFragment(BaseFragment baseFragment) {
        this.parentFragment = baseFragment;
    }

    public void setContentViewPaddingTop(int i) {
        int dp = i + AndroidUtilities.dp(58.0f);
        if (this.listView.getPaddingTop() != dp) {
            this.ignoreLayout = true;
            this.listView.setPadding(0, dp, 0, 0);
            this.ignoreLayout = false;
        }
    }

    private void updateLastItemInAdapter() {
        RecyclerView.Adapter adapter = this.listView.getAdapter();
        adapter.notifyItemChanged(adapter.getItemCount() - 1);
    }

    public int getContentTopOffset() {
        return this.topOffset;
    }

    public boolean update() {
        if (this.listView.getChildCount() <= 0) {
            int paddingTop = this.listView.getPaddingTop();
            this.topOffset = paddingTop;
            this.listView.setTopGlowOffset(paddingTop);
            this.searchLayout.setTranslationY(this.topOffset);
            this.shadowView.setTranslationY(this.topOffset);
            setShadowVisible(false);
            return true;
        }
        View childAt = this.listView.getChildAt(0);
        for (int i = 1; i < this.listView.getChildCount(); i++) {
            View childAt2 = this.listView.getChildAt(i);
            if (childAt2.getTop() < childAt.getTop()) {
                childAt = childAt2;
            }
        }
        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findContainingViewHolder(childAt);
        int top = childAt.getTop() - AndroidUtilities.dp(58.0f);
        int i2 = (top <= 0 || holder == null || holder.getAdapterPosition() != 0) ? 0 : top;
        setShadowVisible(top < 0);
        if (this.topOffset == i2) {
            return false;
        }
        this.topOffset = i2;
        this.listView.setTopGlowOffset(i2 + AndroidUtilities.dp(58.0f));
        this.searchLayout.setTranslationY(this.topOffset);
        this.shadowView.setTranslationY(this.topOffset);
        return true;
    }

    private void updateVisibleTrendingSets() {
        RecyclerView.Adapter adapter = this.listView.getAdapter();
        if (adapter != null) {
            adapter.notifyItemRangeChanged(0, adapter.getItemCount(), 0);
        }
    }

    private void setShadowVisible(boolean z) {
        if (this.shadowVisible != z) {
            this.shadowVisible = z;
            this.shadowView.animate().alpha(z ? 1.0f : 0.0f).setDuration(200L).start();
        }
    }

    public void updateColors() {
        RecyclerView.Adapter adapter = this.listView.getAdapter();
        TrendingStickersAdapter trendingStickersAdapter = this.adapter;
        if (adapter == trendingStickersAdapter) {
            trendingStickersAdapter.updateColors(this.listView);
        } else {
            this.searchAdapter.updateColors(this.listView);
        }
    }

    public void getThemeDescriptions(List<ThemeDescription> list, ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate) {
        this.searchView.getThemeDescriptions(list);
        this.adapter.getThemeDescriptions(list, this.listView, themeDescriptionDelegate);
        this.searchAdapter.getThemeDescriptions(list, this.listView, themeDescriptionDelegate);
        list.add(new ThemeDescription(this.shadowView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "dialogShadowLine"));
        list.add(new ThemeDescription(this.searchLayout, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "dialogBackground"));
    }

    public void glueToTop(boolean z) {
        this.gluedToTop = z;
        if (z) {
            if (getContentTopOffset() <= 0 || this.glueToTopAnimator != null) {
                return;
            }
            int contentTopOffset = getContentTopOffset();
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.glueToTopAnimator = ofFloat;
            ofFloat.addUpdateListener(new AnonymousClass9(contentTopOffset));
            this.glueToTopAnimator.addListener(new AnonymousClass10());
            this.glueToTopAnimator.setDuration(250L);
            this.glueToTopAnimator.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
            this.glueToTopAnimator.start();
            return;
        }
        ValueAnimator valueAnimator = this.glueToTopAnimator;
        if (valueAnimator == null) {
            return;
        }
        valueAnimator.removeAllListeners();
        this.glueToTopAnimator.cancel();
        this.glueToTopAnimator = null;
    }

    /* renamed from: org.telegram.ui.Components.TrendingStickersLayout$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 implements ValueAnimator.AnimatorUpdateListener {
        int dy = 0;
        final /* synthetic */ int val$startFrom;

        AnonymousClass9(int i) {
            TrendingStickersLayout.this = r1;
            this.val$startFrom = i;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int floatValue = (int) (this.val$startFrom * ((Float) valueAnimator.getAnimatedValue()).floatValue());
            TrendingStickersLayout.this.scrollFromAnimator = true;
            TrendingStickersLayout.this.listView.scrollBy(0, floatValue - this.dy);
            TrendingStickersLayout.this.scrollFromAnimator = false;
            this.dy = floatValue;
        }
    }

    /* renamed from: org.telegram.ui.Components.TrendingStickersLayout$10 */
    /* loaded from: classes3.dex */
    public class AnonymousClass10 extends AnimatorListenerAdapter {
        AnonymousClass10() {
            TrendingStickersLayout.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            TrendingStickersLayout.this.glueToTopAnimator = null;
        }
    }

    /* loaded from: classes3.dex */
    public class TrendingStickersAdapter extends RecyclerListView.SelectionAdapter {
        private final Context context;
        private boolean endReached;
        private boolean loadingMore;
        private int totalItems;
        private final SparseArray<Object> cache = new SparseArray<>();
        private final ArrayList<TLRPC$StickerSetCovered> sets = new ArrayList<>();
        private final SparseArray<TLRPC$StickerSetCovered> positionsToSets = new SparseArray<>();
        private final HashMap<TLRPC$StickerSetCovered, Integer> setsToPosition = new HashMap<>();
        private final ArrayList<TLRPC$StickerSetCovered> otherPacks = new ArrayList<>();
        private int stickersPerRow = 5;

        public TrendingStickersAdapter(Context context) {
            TrendingStickersLayout.this = r1;
            this.context = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.totalItems + 1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 5;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == getItemCount() - 1) {
                return 3;
            }
            Object obj = this.cache.get(i);
            if (obj == null) {
                return 1;
            }
            if (obj instanceof TLRPC$Document) {
                return 0;
            }
            return obj.equals(-1) ? 4 : 2;
        }

        /* renamed from: org.telegram.ui.Components.TrendingStickersLayout$TrendingStickersAdapter$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends StickerEmojiCell {
            AnonymousClass1(TrendingStickersAdapter trendingStickersAdapter, Context context, boolean z) {
                super(context, z);
            }

            @Override // android.widget.FrameLayout, android.view.View
            public void onMeasure(int i, int i2) {
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(82.0f), 1073741824));
            }
        }

        public /* synthetic */ void lambda$onCreateViewHolder$0(View view) {
            FeaturedStickerSetInfoCell featuredStickerSetInfoCell = (FeaturedStickerSetInfoCell) view.getParent();
            TLRPC$StickerSetCovered stickerSet = featuredStickerSetInfoCell.getStickerSet();
            if (TrendingStickersLayout.this.installingStickerSets.indexOfKey(stickerSet.set.id) >= 0 || TrendingStickersLayout.this.removingStickerSets.indexOfKey(stickerSet.set.id) >= 0) {
                return;
            }
            if (featuredStickerSetInfoCell.isInstalled()) {
                TrendingStickersLayout.this.removingStickerSets.put(stickerSet.set.id, stickerSet);
                TrendingStickersLayout.this.delegate.onStickerSetRemove(stickerSet);
                return;
            }
            installStickerSet(stickerSet, featuredStickerSetInfoCell);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            GraySectionCell graySectionCell;
            AnonymousClass1 anonymousClass1;
            if (i == 0) {
                AnonymousClass1 anonymousClass12 = new AnonymousClass1(this, this.context, false);
                anonymousClass12.getImageView().setLayerNum(3);
                anonymousClass1 = anonymousClass12;
            } else {
                if (i == 1) {
                    graySectionCell = new EmptyCell(this.context);
                } else if (i == 2) {
                    FeaturedStickerSetInfoCell featuredStickerSetInfoCell = new FeaturedStickerSetInfoCell(this.context, 17, true, true, TrendingStickersLayout.this.resourcesProvider);
                    featuredStickerSetInfoCell.setAddOnClickListener(new TrendingStickersLayout$TrendingStickersAdapter$$ExternalSyntheticLambda0(this));
                    graySectionCell = featuredStickerSetInfoCell;
                } else if (i == 3) {
                    graySectionCell = new View(this.context);
                } else if (i == 4) {
                    graySectionCell = new GraySectionCell(this.context, TrendingStickersLayout.this.resourcesProvider);
                } else if (i != 5) {
                    graySectionCell = null;
                } else {
                    FeaturedStickerSetCell2 featuredStickerSetCell2 = new FeaturedStickerSetCell2(this.context, TrendingStickersLayout.this.resourcesProvider);
                    featuredStickerSetCell2.setAddOnClickListener(new TrendingStickersLayout$TrendingStickersAdapter$$ExternalSyntheticLambda1(this));
                    featuredStickerSetCell2.getImageView().setLayerNum(3);
                    anonymousClass1 = featuredStickerSetCell2;
                }
                return new RecyclerListView.Holder(graySectionCell);
            }
            graySectionCell = anonymousClass1;
            return new RecyclerListView.Holder(graySectionCell);
        }

        public /* synthetic */ void lambda$onCreateViewHolder$1(View view) {
            FeaturedStickerSetCell2 featuredStickerSetCell2 = (FeaturedStickerSetCell2) view.getParent();
            TLRPC$StickerSetCovered stickerSet = featuredStickerSetCell2.getStickerSet();
            if (TrendingStickersLayout.this.installingStickerSets.indexOfKey(stickerSet.set.id) >= 0 || TrendingStickersLayout.this.removingStickerSets.indexOfKey(stickerSet.set.id) >= 0) {
                return;
            }
            if (featuredStickerSetCell2.isInstalled()) {
                TrendingStickersLayout.this.removingStickerSets.put(stickerSet.set.id, stickerSet);
                TrendingStickersLayout.this.delegate.onStickerSetRemove(stickerSet);
                return;
            }
            installStickerSet(stickerSet, featuredStickerSetCell2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                ((StickerEmojiCell) viewHolder.itemView).setSticker((TLRPC$Document) this.cache.get(i), this.positionsToSets.get(i), false);
            } else if (itemViewType == 1) {
                ((EmptyCell) viewHolder.itemView).setHeight(AndroidUtilities.dp(82.0f));
            } else {
                if (itemViewType != 2) {
                    if (itemViewType == 4) {
                        ((GraySectionCell) viewHolder.itemView).setText(LocaleController.getString("OtherStickers", 2131627174));
                        return;
                    } else if (itemViewType != 5) {
                        return;
                    }
                }
                bindStickerSetCell(viewHolder.itemView, i, false);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i, List list) {
            if (list.contains(0)) {
                int itemViewType = viewHolder.getItemViewType();
                if (itemViewType != 2 && itemViewType != 5) {
                    return;
                }
                bindStickerSetCell(viewHolder.itemView, i, true);
                return;
            }
            super.onBindViewHolder(viewHolder, i, list);
        }

        /* JADX WARN: Code restructure failed: missing block: B:54:0x013b, code lost:
            if (r11.cache.get(r13).equals(-1) != false) goto L55;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void bindStickerSetCell(View view, int i, boolean z) {
            boolean z2;
            TLRPC$StickerSetCovered tLRPC$StickerSetCovered;
            boolean z3;
            boolean z4;
            MediaDataController mediaDataController = MediaDataController.getInstance(TrendingStickersLayout.this.currentAccount);
            boolean z5 = true;
            if (i < this.totalItems) {
                TLRPC$StickerSetCovered tLRPC$StickerSetCovered2 = this.sets.get(((Integer) this.cache.get(i)).intValue());
                ArrayList<Long> unreadStickerSets = mediaDataController.getUnreadStickerSets();
                boolean z6 = unreadStickerSets != null && unreadStickerSets.contains(Long.valueOf(tLRPC$StickerSetCovered2.set.id));
                if (z6) {
                    mediaDataController.markFeaturedStickersByIdAsRead(false, tLRPC$StickerSetCovered2.set.id);
                }
                tLRPC$StickerSetCovered = tLRPC$StickerSetCovered2;
                z2 = z6;
            } else {
                tLRPC$StickerSetCovered = this.sets.get(((Integer) this.cache.get(i)).intValue());
                z2 = false;
            }
            mediaDataController.preloadStickerSetThumb(tLRPC$StickerSetCovered);
            int i2 = 0;
            while (true) {
                if (i2 >= TrendingStickersLayout.this.primaryInstallingStickerSets.length) {
                    z3 = false;
                    break;
                }
                if (TrendingStickersLayout.this.primaryInstallingStickerSets[i2] != null) {
                    TLRPC$TL_messages_stickerSet stickerSetById = MediaDataController.getInstance(TrendingStickersLayout.this.currentAccount).getStickerSetById(TrendingStickersLayout.this.primaryInstallingStickerSets[i2].set.id);
                    if (stickerSetById == null || stickerSetById.set.archived) {
                        if (TrendingStickersLayout.this.primaryInstallingStickerSets[i2].set.id == tLRPC$StickerSetCovered.set.id) {
                            z3 = true;
                            break;
                        }
                    } else {
                        TrendingStickersLayout.this.primaryInstallingStickerSets[i2] = null;
                    }
                }
                i2++;
            }
            boolean isStickerPackInstalled = mediaDataController.isStickerPackInstalled(tLRPC$StickerSetCovered.set.id);
            boolean z7 = TrendingStickersLayout.this.installingStickerSets.indexOfKey(tLRPC$StickerSetCovered.set.id) >= 0;
            boolean z8 = TrendingStickersLayout.this.removingStickerSets.indexOfKey(tLRPC$StickerSetCovered.set.id) >= 0;
            if (!z7 || !isStickerPackInstalled) {
                if (z8 && !isStickerPackInstalled) {
                    TrendingStickersLayout.this.removingStickerSets.remove(tLRPC$StickerSetCovered.set.id);
                }
                z4 = z7;
            } else {
                TrendingStickersLayout.this.installingStickerSets.remove(tLRPC$StickerSetCovered.set.id);
                z4 = false;
            }
            FeaturedStickerSetInfoCell featuredStickerSetInfoCell = (FeaturedStickerSetInfoCell) view;
            featuredStickerSetInfoCell.setStickerSet(tLRPC$StickerSetCovered, z2, z, 0, 0, z3);
            featuredStickerSetInfoCell.setAddDrawProgress(!z3 && z4, z);
            if (i > 0) {
                int i3 = i - 1;
                if (this.cache.get(i3) != null) {
                }
                featuredStickerSetInfoCell.setNeedDivider(z5);
            }
            z5 = false;
            featuredStickerSetInfoCell.setNeedDivider(z5);
        }

        public void installStickerSet(TLRPC$StickerSetCovered tLRPC$StickerSetCovered, View view) {
            boolean z;
            int i = 0;
            while (true) {
                if (i >= TrendingStickersLayout.this.primaryInstallingStickerSets.length) {
                    break;
                }
                if (TrendingStickersLayout.this.primaryInstallingStickerSets[i] != null) {
                    TLRPC$TL_messages_stickerSet stickerSetById = MediaDataController.getInstance(TrendingStickersLayout.this.currentAccount).getStickerSetById(TrendingStickersLayout.this.primaryInstallingStickerSets[i].set.id);
                    if (stickerSetById == null || stickerSetById.set.archived) {
                        if (TrendingStickersLayout.this.primaryInstallingStickerSets[i].set.id == tLRPC$StickerSetCovered.set.id) {
                            return;
                        }
                    } else {
                        TrendingStickersLayout.this.primaryInstallingStickerSets[i] = null;
                        break;
                    }
                }
                i++;
            }
            int i2 = 0;
            while (true) {
                if (i2 >= TrendingStickersLayout.this.primaryInstallingStickerSets.length) {
                    z = false;
                    break;
                } else if (TrendingStickersLayout.this.primaryInstallingStickerSets[i2] == null) {
                    TrendingStickersLayout.this.primaryInstallingStickerSets[i2] = tLRPC$StickerSetCovered;
                    z = true;
                    break;
                } else {
                    i2++;
                }
            }
            if (!z && view != null) {
                if (view instanceof FeaturedStickerSetCell2) {
                    ((FeaturedStickerSetCell2) view).setDrawProgress(true, true);
                } else if (view instanceof FeaturedStickerSetInfoCell) {
                    ((FeaturedStickerSetInfoCell) view).setAddDrawProgress(true, true);
                }
            }
            TrendingStickersLayout.this.installingStickerSets.put(tLRPC$StickerSetCovered.set.id, tLRPC$StickerSetCovered);
            if (view != null) {
                TrendingStickersLayout.this.delegate.onStickerSetAdd(tLRPC$StickerSetCovered, z);
                return;
            }
            int size = this.positionsToSets.size();
            for (int i3 = 0; i3 < size; i3++) {
                TLRPC$StickerSetCovered tLRPC$StickerSetCovered2 = this.positionsToSets.get(i3);
                if (tLRPC$StickerSetCovered2 != null && tLRPC$StickerSetCovered2.set.id == tLRPC$StickerSetCovered.set.id) {
                    notifyItemChanged(i3, 0);
                    return;
                }
            }
        }

        public void refreshStickerSets() {
            int i;
            int measuredWidth = TrendingStickersLayout.this.getMeasuredWidth();
            if (measuredWidth != 0) {
                this.stickersPerRow = Math.max(5, measuredWidth / AndroidUtilities.dp(72.0f));
                if (TrendingStickersLayout.this.layoutManager.getSpanCount() != this.stickersPerRow) {
                    TrendingStickersLayout.this.layoutManager.setSpanCount(this.stickersPerRow);
                    TrendingStickersLayout.this.loaded = false;
                }
            }
            if (TrendingStickersLayout.this.loaded) {
                return;
            }
            this.cache.clear();
            this.positionsToSets.clear();
            this.setsToPosition.clear();
            this.sets.clear();
            this.totalItems = 0;
            MediaDataController mediaDataController = MediaDataController.getInstance(TrendingStickersLayout.this.currentAccount);
            ArrayList arrayList = new ArrayList(mediaDataController.getFeaturedStickerSets());
            int size = arrayList.size();
            arrayList.addAll(this.otherPacks);
            int i2 = 0;
            int i3 = 0;
            while (true) {
                int i4 = 1;
                if (i2 >= arrayList.size()) {
                    break;
                }
                TLRPC$StickerSetCovered tLRPC$StickerSetCovered = (TLRPC$StickerSetCovered) arrayList.get(i2);
                if (!tLRPC$StickerSetCovered.covers.isEmpty() || tLRPC$StickerSetCovered.cover != null) {
                    if (i2 == size) {
                        SparseArray<Object> sparseArray = this.cache;
                        int i5 = this.totalItems;
                        this.totalItems = i5 + 1;
                        sparseArray.put(i5, -1);
                    }
                    this.sets.add(tLRPC$StickerSetCovered);
                    this.positionsToSets.put(this.totalItems, tLRPC$StickerSetCovered);
                    this.setsToPosition.put(tLRPC$StickerSetCovered, Integer.valueOf(this.totalItems));
                    SparseArray<Object> sparseArray2 = this.cache;
                    int i6 = this.totalItems;
                    this.totalItems = i6 + 1;
                    int i7 = i3 + 1;
                    sparseArray2.put(i6, Integer.valueOf(i3));
                    if (!tLRPC$StickerSetCovered.covers.isEmpty()) {
                        i4 = (int) Math.ceil(tLRPC$StickerSetCovered.covers.size() / this.stickersPerRow);
                        for (int i8 = 0; i8 < tLRPC$StickerSetCovered.covers.size(); i8++) {
                            this.cache.put(this.totalItems + i8, tLRPC$StickerSetCovered.covers.get(i8));
                        }
                    } else {
                        this.cache.put(this.totalItems, tLRPC$StickerSetCovered.cover);
                    }
                    int i9 = 0;
                    while (true) {
                        i = this.stickersPerRow;
                        if (i9 >= i4 * i) {
                            break;
                        }
                        this.positionsToSets.put(this.totalItems + i9, tLRPC$StickerSetCovered);
                        i9++;
                    }
                    this.totalItems += i4 * i;
                    i3 = i7;
                }
                i2++;
            }
            if (this.totalItems != 0) {
                TrendingStickersLayout.this.loaded = true;
                TrendingStickersLayout.this.hash = mediaDataController.getFeaturedStickersHashWithoutUnread(false);
            }
            notifyDataSetChanged();
        }

        public void loadMoreStickerSets() {
            if (!TrendingStickersLayout.this.loaded || this.loadingMore || this.endReached) {
                return;
            }
            this.loadingMore = true;
            TLRPC$TL_messages_getOldFeaturedStickers tLRPC$TL_messages_getOldFeaturedStickers = new TLRPC$TL_messages_getOldFeaturedStickers();
            tLRPC$TL_messages_getOldFeaturedStickers.offset = this.otherPacks.size();
            tLRPC$TL_messages_getOldFeaturedStickers.limit = 40;
            ConnectionsManager.getInstance(TrendingStickersLayout.this.currentAccount).sendRequest(tLRPC$TL_messages_getOldFeaturedStickers, new TrendingStickersLayout$TrendingStickersAdapter$$ExternalSyntheticLambda3(this));
        }

        public /* synthetic */ void lambda$loadMoreStickerSets$3(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new TrendingStickersLayout$TrendingStickersAdapter$$ExternalSyntheticLambda2(this, tLRPC$TL_error, tLObject));
        }

        public /* synthetic */ void lambda$loadMoreStickerSets$2(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
            int i;
            int i2;
            this.loadingMore = false;
            if (tLRPC$TL_error == null && (tLObject instanceof TLRPC$TL_messages_featuredStickers)) {
                ArrayList<TLRPC$StickerSetCovered> arrayList = ((TLRPC$TL_messages_featuredStickers) tLObject).sets;
                if (arrayList.size() < 40) {
                    this.endReached = true;
                }
                if (arrayList.isEmpty()) {
                    return;
                }
                if (this.otherPacks.isEmpty()) {
                    SparseArray<Object> sparseArray = this.cache;
                    int i3 = this.totalItems;
                    this.totalItems = i3 + 1;
                    sparseArray.put(i3, -1);
                }
                this.otherPacks.addAll(arrayList);
                int size = this.sets.size();
                for (int i4 = 0; i4 < arrayList.size(); i4++) {
                    TLRPC$StickerSetCovered tLRPC$StickerSetCovered = arrayList.get(i4);
                    if (!tLRPC$StickerSetCovered.covers.isEmpty() || tLRPC$StickerSetCovered.cover != null) {
                        this.sets.add(tLRPC$StickerSetCovered);
                        this.positionsToSets.put(this.totalItems, tLRPC$StickerSetCovered);
                        SparseArray<Object> sparseArray2 = this.cache;
                        int i5 = this.totalItems;
                        this.totalItems = i5 + 1;
                        int i6 = size + 1;
                        sparseArray2.put(i5, Integer.valueOf(size));
                        if (!tLRPC$StickerSetCovered.covers.isEmpty()) {
                            i = (int) Math.ceil(tLRPC$StickerSetCovered.covers.size() / this.stickersPerRow);
                            for (int i7 = 0; i7 < tLRPC$StickerSetCovered.covers.size(); i7++) {
                                this.cache.put(this.totalItems + i7, tLRPC$StickerSetCovered.covers.get(i7));
                            }
                        } else {
                            this.cache.put(this.totalItems, tLRPC$StickerSetCovered.cover);
                            i = 1;
                        }
                        int i8 = 0;
                        while (true) {
                            i2 = this.stickersPerRow;
                            if (i8 >= i * i2) {
                                break;
                            }
                            this.positionsToSets.put(this.totalItems + i8, tLRPC$StickerSetCovered);
                            i8++;
                        }
                        this.totalItems += i * i2;
                        size = i6;
                    }
                }
                notifyDataSetChanged();
                return;
            }
            this.endReached = true;
        }

        public void updateColors(RecyclerListView recyclerListView) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = recyclerListView.getChildAt(i);
                if (childAt instanceof FeaturedStickerSetInfoCell) {
                    ((FeaturedStickerSetInfoCell) childAt).updateColors();
                } else if (childAt instanceof FeaturedStickerSetCell2) {
                    ((FeaturedStickerSetCell2) childAt).updateColors();
                }
            }
        }

        public void getThemeDescriptions(List<ThemeDescription> list, RecyclerListView recyclerListView, ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate) {
            FeaturedStickerSetInfoCell.createThemeDescriptions(list, recyclerListView, themeDescriptionDelegate);
            FeaturedStickerSetCell2.createThemeDescriptions(list, recyclerListView, themeDescriptionDelegate);
            GraySectionCell.createThemeDescriptions(list, recyclerListView);
        }
    }

    private int getThemedColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
        return color != null ? color.intValue() : Theme.getColor(str);
    }
}
