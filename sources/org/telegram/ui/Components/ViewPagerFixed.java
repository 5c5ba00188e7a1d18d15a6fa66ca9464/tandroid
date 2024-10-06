package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.SystemClock;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.Property;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ViewPagerFixed;
/* loaded from: classes3.dex */
public class ViewPagerFixed extends FrameLayout {
    private static final Interpolator interpolator = new Interpolator() { // from class: org.telegram.ui.Components.ViewPagerFixed$$ExternalSyntheticLambda1
        @Override // android.animation.TimeInterpolator
        public final float getInterpolation(float f) {
            float lambda$static$0;
            lambda$static$0 = ViewPagerFixed.lambda$static$0(f);
            return lambda$static$0;
        }
    };
    private Adapter adapter;
    private float additionalOffset;
    private boolean allowDisallowInterceptTouch;
    private boolean animatingForward;
    private boolean backAnimation;
    private float backProgress;
    public int currentPosition;
    public float currentProgress;
    private ValueAnimator manualScrolling;
    private int maximumVelocity;
    private boolean maybeStartTracking;
    int nextPosition;
    AnimationNotificationsLocker notificationsLocker;
    private android.graphics.Rect rect;
    private Theme.ResourcesProvider resourcesProvider;
    private boolean startedTracking;
    private int startedTrackingPointerId;
    private int startedTrackingX;
    private int startedTrackingY;
    private AnimatorSet tabsAnimation;
    private boolean tabsAnimationInProgress;
    TabsView tabsView;
    private final float touchSlop;
    ValueAnimator.AnimatorUpdateListener updateTabProgress;
    private VelocityTracker velocityTracker;
    protected View[] viewPages;
    private int[] viewTypes;
    protected SparseArray viewsByType;

    /* loaded from: classes3.dex */
    public static abstract class Adapter {
        public abstract void bindView(View view, int i, int i2);

        public abstract View createView(int i);

        public abstract int getItemCount();

        public int getItemId(int i) {
            return i;
        }

        public String getItemTitle(int i) {
            return "";
        }

        public int getItemViewType(int i) {
            return 0;
        }

        public boolean hasStableId() {
            return false;
        }
    }

    /* loaded from: classes3.dex */
    public static class TabsView extends FrameLayout {
        private int activeTextColorKey;
        private ListAdapter adapter;
        private int additionalTabWidth;
        private int allTabsWidth;
        private boolean animatingIndicator;
        private float animatingIndicatorProgress;
        private Runnable animationRunnable;
        private float animationTime;
        private int backgroundColorKey;
        private Paint counterPaint;
        private float crossfadeAlpha;
        private Bitmap crossfadeBitmap;
        private Paint crossfadePaint;
        private int currentPosition;
        private TabsViewDelegate delegate;
        private Paint deletePaint;
        private float editingAnimationProgress;
        private boolean editingForwardAnimation;
        private float editingStartAnimationProgress;
        private float hideProgress;
        private SparseIntArray idToPosition;
        private boolean ignoreLayout;
        private float indicatorProgress2;
        private CubicBezierInterpolator interpolator;
        private boolean invalidated;
        private boolean isEditing;
        private boolean isInHiddenMode;
        private long lastAnimationTime;
        float lastDrawnIndicatorW;
        float lastDrawnIndicatorX;
        private LinearLayoutManager layoutManager;
        private RecyclerListView listView;
        private int manualScrollingToId;
        private int manualScrollingToPosition;
        private boolean orderChanged;
        private float overrideFromW;
        private float overrideFromX;
        private SparseIntArray positionToId;
        private SparseIntArray positionToWidth;
        private SparseIntArray positionToX;
        private Utilities.Callback2Return preTabClick;
        private int prevLayoutWidth;
        private int previousId;
        private int previousPosition;
        private Theme.ResourcesProvider resourcesProvider;
        private int scrollingToChild;
        private int selectedTabId;
        private int selectorColorKey;
        private GradientDrawable selectorDrawable;
        private final Paint selectorPaint;
        private int selectorType;
        private int tabLineColorKey;
        public int tabMarginDp;
        private ArrayList tabs;
        ValueAnimator tabsAnimator;
        private TextPaint textCounterPaint;
        private TextPaint textPaint;
        private int unactiveTextColorKey;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public class ListAdapter extends RecyclerListView.SelectionAdapter {
            private Context mContext;

            public ListAdapter(Context context) {
                this.mContext = context;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemCount() {
                return TabsView.this.tabs.size();
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public long getItemId(int i) {
                return ((Tab) TabsView.this.tabs.get(i)).id;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemViewType(int i) {
                return 0;
            }

            @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
            public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
                return true;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                ((TabView) viewHolder.itemView).setTab((Tab) TabsView.this.tabs.get(i), i);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new RecyclerListView.Holder(new TabView(this.mContext));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Tab {
            public float alpha = 1.0f;
            public int counter;
            public int id;
            public CharSequence title;
            public int titleWidth;

            public Tab(int i, CharSequence charSequence) {
                this.id = i;
                this.title = charSequence;
            }

            public int getWidth(boolean z, TextPaint textPaint) {
                CharSequence charSequence = this.title;
                int ceil = (int) Math.ceil(textPaint.measureText(charSequence == null ? "" : charSequence.toString()));
                this.titleWidth = ceil;
                return Math.max(0, ceil);
            }
        }

        /* loaded from: classes3.dex */
        public class TabView extends View {
            private int currentPosition;
            private Tab currentTab;
            private CharSequence currentText;
            private RectF rect;
            private int tabWidth;
            private int textHeight;
            private StaticLayout textLayout;
            private int textOffsetX;

            public TabView(Context context) {
                super(context);
                this.rect = new RectF();
            }

            @Override // android.view.View
            public int getId() {
                return this.currentTab.id;
            }

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                int i;
                int i2;
                int i3;
                int i4;
                int i5;
                int i6;
                int i7;
                String str;
                int i8;
                int i9;
                int i10;
                Paint paint;
                int i11;
                int i12;
                if (this.currentTab.id != Integer.MAX_VALUE && TabsView.this.editingAnimationProgress != 0.0f) {
                    canvas.save();
                    float f = TabsView.this.editingAnimationProgress * (this.currentPosition % 2 == 0 ? 1.0f : -1.0f);
                    canvas.translate(AndroidUtilities.dp(0.66f) * f, 0.0f);
                    canvas.rotate(f, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
                }
                if (TabsView.this.manualScrollingToId != -1) {
                    i = TabsView.this.manualScrollingToId;
                    i2 = TabsView.this.selectedTabId;
                } else {
                    i = TabsView.this.selectedTabId;
                    i2 = TabsView.this.previousId;
                }
                if (this.currentTab.id == i) {
                    i3 = TabsView.this.activeTextColorKey;
                    i4 = TabsView.this.unactiveTextColorKey;
                    i5 = Theme.key_chats_tabUnreadActiveBackground;
                    i6 = Theme.key_chats_tabUnreadUnactiveBackground;
                } else {
                    i3 = TabsView.this.unactiveTextColorKey;
                    i4 = TabsView.this.activeTextColorKey;
                    i5 = Theme.key_chats_tabUnreadUnactiveBackground;
                    i6 = Theme.key_chats_tabUnreadActiveBackground;
                }
                if (TabsView.this.selectorType == 9) {
                    TabsView.this.textPaint.setColor(Theme.getColor(TabsView.this.unactiveTextColorKey, TabsView.this.resourcesProvider));
                } else if ((TabsView.this.animatingIndicator || TabsView.this.manualScrollingToId != -1) && ((i7 = this.currentTab.id) == i || i7 == i2)) {
                    TabsView.this.textPaint.setColor(ColorUtils.blendARGB(Theme.getColor(i4, TabsView.this.resourcesProvider), Theme.getColor(i3, TabsView.this.resourcesProvider), TabsView.this.animatingIndicatorProgress));
                } else {
                    TabsView.this.textPaint.setColor(Theme.getColor(i3, TabsView.this.resourcesProvider));
                }
                int i13 = this.currentTab.counter;
                if (i13 > 0) {
                    str = String.format("%d", Integer.valueOf(i13));
                    i8 = (int) Math.ceil(TabsView.this.textCounterPaint.measureText(str));
                    i9 = Math.max(AndroidUtilities.dp(10.0f), i8) + AndroidUtilities.dp(10.0f);
                } else {
                    str = null;
                    i8 = 0;
                    i9 = 0;
                }
                if (this.currentTab.id != Integer.MAX_VALUE && (TabsView.this.isEditing || TabsView.this.editingStartAnimationProgress != 0.0f)) {
                    i9 = (int) (i9 + ((AndroidUtilities.dp(20.0f) - i9) * TabsView.this.editingStartAnimationProgress));
                }
                int i14 = this.currentTab.titleWidth;
                if (i9 != 0) {
                    i10 = AndroidUtilities.dp((str != null ? 1.0f : TabsView.this.editingStartAnimationProgress) * 6.0f) + i9;
                } else {
                    i10 = 0;
                }
                this.tabWidth = i14 + i10;
                int measuredWidth = (getMeasuredWidth() - this.tabWidth) / 2;
                if (!TextUtils.equals(this.currentTab.title, this.currentText)) {
                    CharSequence charSequence = this.currentTab.title;
                    this.currentText = charSequence;
                    StaticLayout staticLayout = new StaticLayout(Emoji.replaceEmoji(charSequence, TabsView.this.textPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false), TabsView.this.textPaint, AndroidUtilities.dp(400.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    this.textLayout = staticLayout;
                    this.textHeight = staticLayout.getHeight();
                    this.textOffsetX = (int) (-this.textLayout.getLineLeft(0));
                }
                if (this.textLayout != null) {
                    canvas.save();
                    canvas.translate(this.textOffsetX + measuredWidth, ((getMeasuredHeight() - this.textHeight) / 2) + 1);
                    this.textLayout.draw(canvas);
                    canvas.restore();
                }
                if (str != null || (this.currentTab.id != Integer.MAX_VALUE && (TabsView.this.isEditing || TabsView.this.editingStartAnimationProgress != 0.0f))) {
                    TabsView.this.textCounterPaint.setColor(Theme.getColor(TabsView.this.backgroundColorKey, TabsView.this.resourcesProvider));
                    if (Theme.hasThemeKey(i5) && Theme.hasThemeKey(i6)) {
                        int color = Theme.getColor(i5, TabsView.this.resourcesProvider);
                        if ((TabsView.this.animatingIndicator || TabsView.this.manualScrollingToPosition != -1) && ((i12 = this.currentTab.id) == i || i12 == i2)) {
                            TabsView.this.counterPaint.setColor(ColorUtils.blendARGB(Theme.getColor(i6, TabsView.this.resourcesProvider), color, TabsView.this.animatingIndicatorProgress));
                        } else {
                            TabsView.this.counterPaint.setColor(color);
                        }
                    } else {
                        TabsView.this.counterPaint.setColor(TabsView.this.textPaint.getColor());
                    }
                    int dp = measuredWidth + this.currentTab.titleWidth + AndroidUtilities.dp(6.0f);
                    int measuredHeight = (getMeasuredHeight() - AndroidUtilities.dp(20.0f)) / 2;
                    if (this.currentTab.id == Integer.MAX_VALUE || ((!TabsView.this.isEditing && TabsView.this.editingStartAnimationProgress == 0.0f) || str != null)) {
                        paint = TabsView.this.counterPaint;
                        i11 = NotificationCenter.closeSearchByActiveAction;
                    } else {
                        paint = TabsView.this.counterPaint;
                        i11 = (int) (TabsView.this.editingStartAnimationProgress * 255.0f);
                    }
                    paint.setAlpha(i11);
                    this.rect.set(dp, measuredHeight, dp + i9, AndroidUtilities.dp(20.0f) + measuredHeight);
                    RectF rectF = this.rect;
                    float f2 = AndroidUtilities.density * 11.5f;
                    canvas.drawRoundRect(rectF, f2, f2, TabsView.this.counterPaint);
                    if (str != null) {
                        if (this.currentTab.id != Integer.MAX_VALUE) {
                            TabsView.this.textCounterPaint.setAlpha((int) ((1.0f - TabsView.this.editingStartAnimationProgress) * 255.0f));
                        }
                        RectF rectF2 = this.rect;
                        canvas.drawText(str, rectF2.left + ((rectF2.width() - i8) / 2.0f), measuredHeight + AndroidUtilities.dp(14.5f), TabsView.this.textCounterPaint);
                    }
                    if (this.currentTab.id != Integer.MAX_VALUE && (TabsView.this.isEditing || TabsView.this.editingStartAnimationProgress != 0.0f)) {
                        TabsView.this.deletePaint.setColor(TabsView.this.textCounterPaint.getColor());
                        TabsView.this.deletePaint.setAlpha((int) (TabsView.this.editingStartAnimationProgress * 255.0f));
                        float dp2 = AndroidUtilities.dp(3.0f);
                        canvas.drawLine(this.rect.centerX() - dp2, this.rect.centerY() - dp2, this.rect.centerX() + dp2, this.rect.centerY() + dp2, TabsView.this.deletePaint);
                        canvas.drawLine(this.rect.centerX() - dp2, this.rect.centerY() + dp2, this.rect.centerX() + dp2, this.rect.centerY() - dp2, TabsView.this.deletePaint);
                    }
                }
                if (this.currentTab.id == Integer.MAX_VALUE || TabsView.this.editingAnimationProgress == 0.0f) {
                    return;
                }
                canvas.restore();
            }

            @Override // android.view.View
            public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                accessibilityNodeInfo.setSelected((this.currentTab == null || TabsView.this.selectedTabId == -1 || this.currentTab.id != TabsView.this.selectedTabId) ? false : true);
            }

            @Override // android.view.View
            protected void onMeasure(int i, int i2) {
                setMeasuredDimension(this.currentTab.getWidth(false, TabsView.this.textPaint) + AndroidUtilities.dp(TabsView.this.tabMarginDp * 2) + TabsView.this.additionalTabWidth, View.MeasureSpec.getSize(i2));
            }

            public void setTab(Tab tab, int i) {
                this.currentTab = tab;
                this.currentPosition = i;
                setContentDescription(tab.title);
                setAlpha(tab.alpha);
                requestLayout();
            }
        }

        /* loaded from: classes3.dex */
        public interface TabsViewDelegate {
            boolean canPerformActions();

            void invalidateBlur();

            void onPageScrolled(float f);

            void onPageSelected(int i, boolean z);

            void onSamePageSelected();
        }

        public TabsView(Context context, boolean z, int i, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            RecyclerListView recyclerListView;
            FrameLayout.LayoutParams createFrame;
            this.indicatorProgress2 = 1.0f;
            this.textPaint = new TextPaint(1);
            this.textCounterPaint = new TextPaint(1);
            this.deletePaint = new TextPaint(1);
            this.counterPaint = new Paint(1);
            this.tabs = new ArrayList();
            this.crossfadePaint = new Paint();
            this.tabMarginDp = 16;
            this.selectedTabId = -1;
            this.manualScrollingToPosition = -1;
            this.manualScrollingToId = -1;
            this.scrollingToChild = -1;
            this.tabLineColorKey = Theme.key_profile_tabSelectedLine;
            this.activeTextColorKey = Theme.key_profile_tabSelectedText;
            this.unactiveTextColorKey = Theme.key_profile_tabText;
            this.selectorColorKey = Theme.key_profile_tabSelector;
            this.backgroundColorKey = Theme.key_actionBarDefault;
            this.interpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            this.positionToId = new SparseIntArray(5);
            this.idToPosition = new SparseIntArray(5);
            this.positionToWidth = new SparseIntArray(5);
            this.positionToX = new SparseIntArray(5);
            this.animationRunnable = new Runnable() { // from class: org.telegram.ui.Components.ViewPagerFixed.TabsView.1
                @Override // java.lang.Runnable
                public void run() {
                    if (TabsView.this.animatingIndicator) {
                        long elapsedRealtime = SystemClock.elapsedRealtime() - TabsView.this.lastAnimationTime;
                        if (elapsedRealtime > 17) {
                            elapsedRealtime = 17;
                        }
                        TabsView.access$3316(TabsView.this, ((float) elapsedRealtime) / 200.0f);
                        TabsView tabsView = TabsView.this;
                        tabsView.setAnimationIdicatorProgress(tabsView.interpolator.getInterpolation(TabsView.this.animationTime));
                        if (TabsView.this.animationTime > 1.0f) {
                            TabsView.this.animationTime = 1.0f;
                        }
                        if (TabsView.this.animationTime < 1.0f) {
                            AndroidUtilities.runOnUIThread(TabsView.this.animationRunnable);
                            return;
                        }
                        TabsView.this.animatingIndicator = false;
                        TabsView.this.setEnabled(true);
                        if (TabsView.this.delegate != null) {
                            TabsView.this.delegate.onPageScrolled(1.0f);
                        }
                    }
                }
            };
            this.selectorPaint = new Paint(1);
            this.resourcesProvider = resourcesProvider;
            this.selectorType = i;
            this.textCounterPaint.setTextSize(AndroidUtilities.dp(13.0f));
            this.textCounterPaint.setTypeface(AndroidUtilities.bold());
            this.textPaint.setTextSize(AndroidUtilities.dp(i == 9 ? 14.0f : 15.0f));
            this.textPaint.setTypeface(AndroidUtilities.bold());
            this.deletePaint.setStyle(Paint.Style.STROKE);
            this.deletePaint.setStrokeCap(Paint.Cap.ROUND);
            this.deletePaint.setStrokeWidth(AndroidUtilities.dp(1.5f));
            this.selectorDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, null);
            float dpf2 = AndroidUtilities.dpf2(3.0f);
            this.selectorDrawable.setCornerRadii(new float[]{dpf2, dpf2, dpf2, dpf2, 0.0f, 0.0f, 0.0f, 0.0f});
            this.selectorDrawable.setColor(Theme.getColor(this.tabLineColorKey, resourcesProvider));
            setHorizontalScrollBarEnabled(false);
            RecyclerListView recyclerListView2 = new RecyclerListView(context) { // from class: org.telegram.ui.Components.ViewPagerFixed.TabsView.2
                @Override // android.view.ViewGroup
                public void addView(View view, int i2, ViewGroup.LayoutParams layoutParams) {
                    float f;
                    super.addView(view, i2, layoutParams);
                    if (TabsView.this.isInHiddenMode) {
                        view.setScaleX(0.3f);
                        view.setScaleY(0.3f);
                        f = 0.0f;
                    } else {
                        f = 1.0f;
                        view.setScaleX(1.0f);
                        view.setScaleY(1.0f);
                    }
                    view.setAlpha(f);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Components.RecyclerListView
                public boolean canHighlightChildAt(View view, float f, float f2) {
                    if (TabsView.this.isEditing) {
                        TabView tabView = (TabView) view;
                        float dp = AndroidUtilities.dp(6.0f);
                        if (tabView.rect.left - dp < f && tabView.rect.right + dp > f) {
                            return false;
                        }
                    }
                    return super.canHighlightChildAt(view, f, f2);
                }

                @Override // android.view.View
                public void setAlpha(float f) {
                    super.setAlpha(f);
                    TabsView.this.invalidate();
                }
            };
            this.listView = recyclerListView2;
            recyclerListView2.setOverScrollMode(2);
            RecyclerListView recyclerListView3 = this.listView;
            if (z) {
                recyclerListView3.setItemAnimator(null);
            } else {
                ((DefaultItemAnimator) recyclerListView3.getItemAnimator()).setDelayAnimations(false);
            }
            this.listView.setSelectorType(i);
            if (i == 3) {
                this.listView.setSelectorRadius(0);
            } else {
                this.listView.setSelectorRadius(6);
            }
            this.listView.setSelectorDrawableColor(Theme.getColor(this.selectorColorKey, resourcesProvider));
            RecyclerListView recyclerListView4 = this.listView;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 0, false) { // from class: org.telegram.ui.Components.ViewPagerFixed.TabsView.3
                @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
                public void onInitializeAccessibilityNodeInfo(RecyclerView.Recycler recycler, RecyclerView.State state, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                    super.onInitializeAccessibilityNodeInfo(recycler, state, accessibilityNodeInfoCompat);
                    if (TabsView.this.isInHiddenMode) {
                        accessibilityNodeInfoCompat.setVisibleToUser(false);
                    }
                }

                @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i2) {
                    LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) { // from class: org.telegram.ui.Components.ViewPagerFixed.TabsView.3.1
                        @Override // androidx.recyclerview.widget.LinearSmoothScroller, androidx.recyclerview.widget.RecyclerView.SmoothScroller
                        protected void onTargetFound(View view, RecyclerView.State state2, RecyclerView.SmoothScroller.Action action) {
                            int calculateDxToMakeVisible = calculateDxToMakeVisible(view, getHorizontalSnapPreference());
                            if (calculateDxToMakeVisible > 0 || (calculateDxToMakeVisible == 0 && view.getLeft() - AndroidUtilities.dp(21.0f) < 0)) {
                                calculateDxToMakeVisible += AndroidUtilities.dp(60.0f);
                            } else if (calculateDxToMakeVisible < 0 || (calculateDxToMakeVisible == 0 && view.getRight() + AndroidUtilities.dp(21.0f) > TabsView.this.getMeasuredWidth())) {
                                calculateDxToMakeVisible -= AndroidUtilities.dp(60.0f);
                            }
                            int calculateDyToMakeVisible = calculateDyToMakeVisible(view, getVerticalSnapPreference());
                            int max = Math.max((int) NotificationCenter.updateBotMenuButton, calculateTimeForDeceleration((int) Math.sqrt((calculateDxToMakeVisible * calculateDxToMakeVisible) + (calculateDyToMakeVisible * calculateDyToMakeVisible))));
                            if (max > 0) {
                                action.update(-calculateDxToMakeVisible, -calculateDyToMakeVisible, max, this.mDecelerateInterpolator);
                            }
                        }
                    };
                    linearSmoothScroller.setTargetPosition(i2);
                    startSmoothScroll(linearSmoothScroller);
                }
            };
            this.layoutManager = linearLayoutManager;
            recyclerListView4.setLayoutManager(linearLayoutManager);
            this.listView.setPadding(AndroidUtilities.dp(7.0f), 0, AndroidUtilities.dp(7.0f), 0);
            this.listView.setClipToPadding(false);
            this.listView.setDrawSelectorBehind(true);
            ListAdapter listAdapter = new ListAdapter(context);
            this.adapter = listAdapter;
            listAdapter.setHasStableIds(z);
            this.listView.setAdapter(this.adapter);
            this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.Components.ViewPagerFixed$TabsView$$ExternalSyntheticLambda2
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public /* synthetic */ boolean hasDoubleTap(View view, int i2) {
                    return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i2);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public /* synthetic */ void onDoubleTap(View view, int i2, float f, float f2) {
                    RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i2, f, f2);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                public final void onItemClick(View view, int i2, float f, float f2) {
                    ViewPagerFixed.TabsView.this.lambda$new$0(view, i2, f, f2);
                }
            });
            this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.ViewPagerFixed.TabsView.4
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i2, int i3) {
                    TabsView.this.invalidate();
                }
            });
            if (i == 9) {
                recyclerListView = this.listView;
                createFrame = LayoutHelper.createFrame(-2, -1, 1);
            } else {
                recyclerListView = this.listView;
                createFrame = LayoutHelper.createFrame(-1, -1.0f);
            }
            addView(recyclerListView, createFrame);
        }

        static /* synthetic */ float access$3316(TabsView tabsView, float f) {
            float f2 = tabsView.animationTime + f;
            tabsView.animationTime = f2;
            return f2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view, int i, float f, float f2) {
            TabsViewDelegate tabsViewDelegate;
            TabsViewDelegate tabsViewDelegate2 = this.delegate;
            if (tabsViewDelegate2 == null || tabsViewDelegate2.canPerformActions()) {
                TabView tabView = (TabView) view;
                if (i == this.currentPosition && (tabsViewDelegate = this.delegate) != null) {
                    tabsViewDelegate.onSamePageSelected();
                    return;
                }
                Utilities.Callback2Return callback2Return = this.preTabClick;
                if (callback2Return == null || !((Boolean) callback2Return.run(Integer.valueOf(tabView.currentTab.id), Integer.valueOf(i))).booleanValue()) {
                    scrollToTab(tabView.currentTab.id, i);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$scrollToTab$1(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            setAnimationIdicatorProgress(floatValue);
            TabsViewDelegate tabsViewDelegate = this.delegate;
            if (tabsViewDelegate != null) {
                tabsViewDelegate.onPageScrolled(floatValue);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$setIsEditing$2(TLObject tLObject, TLRPC.TL_error tL_error) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void saveFromValues() {
            this.overrideFromX = this.lastDrawnIndicatorX;
            this.overrideFromW = this.lastDrawnIndicatorW;
        }

        private void scrollToChild(int i) {
            if (this.tabs.isEmpty() || this.scrollingToChild == i || i < 0 || i >= this.tabs.size()) {
                return;
            }
            this.scrollingToChild = i;
            this.listView.smoothScrollToPosition(i);
        }

        private void updateTabsWidths() {
            this.positionToX.clear();
            this.positionToWidth.clear();
            int dp = AndroidUtilities.dp(7.0f);
            int size = this.tabs.size();
            for (int i = 0; i < size; i++) {
                int width = ((Tab) this.tabs.get(i)).getWidth(false, this.textPaint);
                this.positionToWidth.put(i, width);
                this.positionToX.put(i, (this.additionalTabWidth / 2) + dp);
                dp += width + AndroidUtilities.dp(this.tabMarginDp * 2) + this.additionalTabWidth;
            }
        }

        public void addTab(int i, CharSequence charSequence) {
            int size = this.tabs.size();
            if (size == 0 && this.selectedTabId == -1) {
                this.selectedTabId = i;
            }
            this.positionToId.put(size, i);
            this.idToPosition.put(i, size);
            int i2 = this.selectedTabId;
            if (i2 != -1 && i2 == i) {
                this.currentPosition = size;
            }
            Tab tab = new Tab(i, charSequence);
            this.allTabsWidth += tab.getWidth(true, this.textPaint) + AndroidUtilities.dp(this.tabMarginDp * 2);
            this.tabs.add(tab);
        }

        /* JADX WARN: Removed duplicated region for block: B:32:0x0097  */
        /* JADX WARN: Removed duplicated region for block: B:33:0x009c  */
        /* JADX WARN: Removed duplicated region for block: B:36:0x00bc  */
        /* JADX WARN: Removed duplicated region for block: B:37:0x00ce  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x0105  */
        /* JADX WARN: Removed duplicated region for block: B:51:0x019a  */
        @Override // android.view.ViewGroup
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected boolean drawChild(Canvas canvas, View view, long j) {
            int findFirstVisibleItemPosition;
            RecyclerView.ViewHolder findViewHolderForAdapterPosition;
            int i;
            int i2;
            int dp;
            int i3;
            boolean drawChild = super.drawChild(canvas, view, j);
            if (view == this.listView) {
                int measuredHeight = getMeasuredHeight();
                boolean z = this.isInHiddenMode;
                if (z) {
                    float f = this.hideProgress;
                    if (f != 1.0f) {
                        float f2 = f + 0.1f;
                        this.hideProgress = f2;
                        if (f2 > 1.0f) {
                            this.hideProgress = 1.0f;
                        }
                        invalidate();
                        this.selectorDrawable.setAlpha((int) (this.listView.getAlpha() * 255.0f));
                        if (this.animatingIndicator && this.manualScrollingToPosition == -1) {
                            RecyclerView.ViewHolder findViewHolderForAdapterPosition2 = this.listView.findViewHolderForAdapterPosition(this.currentPosition);
                            if (findViewHolderForAdapterPosition2 != null) {
                                TabView tabView = (TabView) findViewHolderForAdapterPosition2.itemView;
                                i3 = Math.max(AndroidUtilities.dp(40.0f), tabView.tabWidth);
                                dp = (int) (tabView.getX() + ((tabView.getMeasuredWidth() - i3) / 2));
                                int x = (int) (dp + this.listView.getX());
                                if (i3 != 0) {
                                }
                                if (this.crossfadeBitmap != null) {
                                }
                            }
                            i3 = 0;
                            dp = 0;
                            int x2 = (int) (dp + this.listView.getX());
                            if (i3 != 0) {
                            }
                            if (this.crossfadeBitmap != null) {
                            }
                        } else {
                            findFirstVisibleItemPosition = this.layoutManager.findFirstVisibleItemPosition();
                            if (findFirstVisibleItemPosition != -1 && (findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(findFirstVisibleItemPosition)) != null) {
                                if (this.animatingIndicator) {
                                    i = this.currentPosition;
                                    i2 = this.manualScrollingToPosition;
                                } else {
                                    i = this.previousPosition;
                                    i2 = this.currentPosition;
                                }
                                int i4 = this.positionToX.get(i);
                                int i5 = this.positionToX.get(i2);
                                int i6 = this.positionToWidth.get(i);
                                int i7 = this.positionToWidth.get(i2);
                                dp = this.additionalTabWidth == 0 ? ((int) (i4 + ((i5 - i4) * this.animatingIndicatorProgress))) + AndroidUtilities.dp(this.tabMarginDp) : (((int) (i4 + ((i5 - i4) * this.animatingIndicatorProgress))) - (this.positionToX.get(findFirstVisibleItemPosition) - findViewHolderForAdapterPosition.itemView.getLeft())) + AndroidUtilities.dp(this.tabMarginDp);
                                i3 = (int) (i6 + ((i7 - i6) * this.animatingIndicatorProgress));
                                int x22 = (int) (dp + this.listView.getX());
                                if (i3 != 0) {
                                    if (this.selectorType == 9) {
                                        this.selectorPaint.setColor(Theme.multAlpha(this.textPaint.getColor(), 0.15f));
                                        float f3 = measuredHeight / 2.0f;
                                        RectF rectF = AndroidUtilities.rectTmp;
                                        float dp2 = AndroidUtilities.dp(26.0f) / 2.0f;
                                        rectF.set(x22 - AndroidUtilities.dp(12.0f), f3 - dp2, x22 + i3 + AndroidUtilities.dp(12.0f), f3 + dp2);
                                        canvas.drawRoundRect(rectF, rectF.height() / 2.0f, rectF.height() / 2.0f, this.selectorPaint);
                                    } else {
                                        float f4 = x22;
                                        this.lastDrawnIndicatorX = f4;
                                        float f5 = i3;
                                        this.lastDrawnIndicatorW = f5;
                                        float f6 = this.indicatorProgress2;
                                        if (f6 != 1.0f) {
                                            x22 = (int) AndroidUtilities.lerp(f4, f4, f6);
                                            i3 = (int) AndroidUtilities.lerp(this.lastDrawnIndicatorW, f5, this.indicatorProgress2);
                                        }
                                        this.selectorDrawable.setBounds(x22, (int) ((measuredHeight - AndroidUtilities.dpr(4.0f)) + (this.hideProgress * AndroidUtilities.dpr(4.0f))), i3 + x22, (int) (measuredHeight + (this.hideProgress * AndroidUtilities.dpr(4.0f))));
                                        this.selectorDrawable.draw(canvas);
                                    }
                                }
                                if (this.crossfadeBitmap != null) {
                                    this.crossfadePaint.setAlpha((int) (this.crossfadeAlpha * 255.0f));
                                    canvas.drawBitmap(this.crossfadeBitmap, 0.0f, 0.0f, this.crossfadePaint);
                                }
                            }
                            i3 = 0;
                            dp = 0;
                            int x222 = (int) (dp + this.listView.getX());
                            if (i3 != 0) {
                            }
                            if (this.crossfadeBitmap != null) {
                            }
                        }
                    }
                }
                if (!z) {
                    float f7 = this.hideProgress;
                    if (f7 != 0.0f) {
                        float f8 = f7 - 0.12f;
                        this.hideProgress = f8;
                        if (f8 < 0.0f) {
                            this.hideProgress = 0.0f;
                        }
                        invalidate();
                    }
                }
                this.selectorDrawable.setAlpha((int) (this.listView.getAlpha() * 255.0f));
                if (this.animatingIndicator) {
                }
                findFirstVisibleItemPosition = this.layoutManager.findFirstVisibleItemPosition();
                if (findFirstVisibleItemPosition != -1) {
                    if (this.animatingIndicator) {
                    }
                    int i42 = this.positionToX.get(i);
                    int i52 = this.positionToX.get(i2);
                    int i62 = this.positionToWidth.get(i);
                    int i72 = this.positionToWidth.get(i2);
                    if (this.additionalTabWidth == 0) {
                    }
                    i3 = (int) (i62 + ((i72 - i62) * this.animatingIndicatorProgress));
                    int x2222 = (int) (dp + this.listView.getX());
                    if (i3 != 0) {
                    }
                    if (this.crossfadeBitmap != null) {
                    }
                }
                i3 = 0;
                dp = 0;
                int x22222 = (int) (dp + this.listView.getX());
                if (i3 != 0) {
                }
                if (this.crossfadeBitmap != null) {
                }
            }
            return drawChild;
        }

        public void finishAddingTabs() {
            this.adapter.notifyDataSetChanged();
        }

        public float getAnimatingIndicatorProgress() {
            return this.animatingIndicatorProgress;
        }

        public int getCurrentPosition() {
            return this.currentPosition;
        }

        public int getCurrentTabId() {
            return this.selectedTabId;
        }

        public int getFirstTabId() {
            return this.positionToId.get(0, 0);
        }

        public int getPreviousPosition() {
            return this.previousPosition;
        }

        public Drawable getSelectorDrawable() {
            return this.selectorDrawable;
        }

        public RecyclerListView getTabsContainer() {
            return this.listView;
        }

        public void hide(boolean z, boolean z2) {
            this.isInHiddenMode = z;
            int i = 0;
            if (z2) {
                while (i < this.listView.getChildCount()) {
                    this.listView.getChildAt(i).animate().alpha(z ? 0.0f : 1.0f).scaleX(z ? 0.0f : 1.0f).scaleY(z ? 0.0f : 1.0f).setInterpolator(CubicBezierInterpolator.DEFAULT).setDuration(220L).start();
                    i++;
                }
            } else {
                while (i < this.listView.getChildCount()) {
                    View childAt = this.listView.getChildAt(i);
                    childAt.setScaleX(z ? 0.0f : 1.0f);
                    childAt.setScaleY(z ? 0.0f : 1.0f);
                    childAt.setAlpha(z ? 0.0f : 1.0f);
                    i++;
                }
                this.hideProgress = z ? 1.0f : 0.0f;
            }
            invalidate();
        }

        public boolean isAnimatingIndicator() {
            return this.animatingIndicator;
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            int i5 = i3 - i;
            if (this.prevLayoutWidth != i5) {
                this.prevLayoutWidth = i5;
                this.scrollingToChild = -1;
                if (this.animatingIndicator) {
                    AndroidUtilities.cancelRunOnUIThread(this.animationRunnable);
                    this.animatingIndicator = false;
                    setEnabled(true);
                    TabsViewDelegate tabsViewDelegate = this.delegate;
                    if (tabsViewDelegate != null) {
                        tabsViewDelegate.onPageScrolled(1.0f);
                    }
                }
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            if (!this.tabs.isEmpty()) {
                int size = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(7.0f)) - AndroidUtilities.dp(7.0f);
                int i3 = this.additionalTabWidth;
                if (this.tabs.size() == 1 || this.selectorType == 9) {
                    this.additionalTabWidth = 0;
                } else {
                    int i4 = this.allTabsWidth;
                    this.additionalTabWidth = i4 < size ? (size - i4) / this.tabs.size() : 0;
                }
                if (i3 != this.additionalTabWidth) {
                    this.ignoreLayout = true;
                    this.adapter.notifyDataSetChanged();
                    this.ignoreLayout = false;
                }
                updateTabsWidths();
                this.invalidated = false;
            }
            super.onMeasure(i, i2);
        }

        public void removeTabs() {
            this.tabs.clear();
            this.positionToId.clear();
            this.idToPosition.clear();
            this.positionToWidth.clear();
            this.positionToX.clear();
            this.allTabsWidth = 0;
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }

        public void scrollToTab(int i, int i2) {
            int i3 = this.currentPosition;
            boolean z = i3 < i2;
            this.scrollingToChild = -1;
            this.previousPosition = i3;
            this.previousId = this.selectedTabId;
            this.currentPosition = i2;
            this.selectedTabId = i;
            ValueAnimator valueAnimator = this.tabsAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            if (this.animatingIndicator) {
                this.animatingIndicator = false;
            }
            this.animationTime = 0.0f;
            this.animatingIndicatorProgress = 0.0f;
            this.animatingIndicator = true;
            setEnabled(false);
            TabsViewDelegate tabsViewDelegate = this.delegate;
            if (tabsViewDelegate != null) {
                tabsViewDelegate.onPageSelected(i2, z);
            }
            scrollToChild(i2);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.tabsAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ViewPagerFixed$TabsView$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    ViewPagerFixed.TabsView.this.lambda$scrollToTab$1(valueAnimator2);
                }
            });
            this.tabsAnimator.setDuration(250L);
            this.tabsAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.tabsAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ViewPagerFixed.TabsView.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    TabsView.this.animatingIndicator = false;
                    TabsView.this.setEnabled(true);
                    if (TabsView.this.delegate != null) {
                        TabsView.this.delegate.onPageScrolled(1.0f);
                    }
                    TabsView.this.invalidate();
                }
            });
            this.tabsAnimator.start();
        }

        public void selectTab(int i, int i2, float f) {
            if (f < 0.0f) {
                f = 0.0f;
            } else if (f > 1.0f) {
                f = 1.0f;
            }
            this.currentPosition = i;
            this.selectedTabId = this.positionToId.get(i);
            if (f > 0.0f) {
                this.manualScrollingToPosition = i2;
                this.manualScrollingToId = this.positionToId.get(i2);
            } else {
                this.manualScrollingToPosition = -1;
                this.manualScrollingToId = -1;
            }
            this.animatingIndicatorProgress = f;
            this.listView.invalidateViews();
            invalidate();
            scrollToChild(i);
            if (f >= 1.0f) {
                this.manualScrollingToPosition = -1;
                this.manualScrollingToId = -1;
                this.currentPosition = i2;
                this.selectedTabId = this.positionToId.get(i2);
            }
            TabsViewDelegate tabsViewDelegate = this.delegate;
            if (tabsViewDelegate != null) {
                tabsViewDelegate.invalidateBlur();
            }
        }

        public void selectTabWithId(int i, float f) {
            int i2 = this.idToPosition.get(i, -1);
            if (i2 < 0) {
                return;
            }
            if (f < 0.0f) {
                f = 0.0f;
            } else if (f > 1.0f) {
                f = 1.0f;
            }
            if (f > 0.0f) {
                this.manualScrollingToPosition = i2;
                this.manualScrollingToId = i;
            } else {
                this.manualScrollingToPosition = -1;
                this.manualScrollingToId = -1;
            }
            this.animatingIndicatorProgress = f;
            this.listView.invalidateViews();
            invalidate();
            scrollToChild(i2);
            if (f >= 1.0f) {
                this.manualScrollingToPosition = -1;
                this.manualScrollingToId = -1;
                this.currentPosition = i2;
                this.selectedTabId = i;
            }
        }

        public void setAnimationIdicatorProgress(float f) {
            this.animatingIndicatorProgress = f;
            this.listView.invalidateViews();
            invalidate();
            TabsViewDelegate tabsViewDelegate = this.delegate;
            if (tabsViewDelegate != null) {
                tabsViewDelegate.onPageScrolled(f);
            }
        }

        public void setDelegate(TabsViewDelegate tabsViewDelegate) {
            this.delegate = tabsViewDelegate;
        }

        public void setIsEditing(boolean z) {
            this.isEditing = z;
            this.editingForwardAnimation = true;
            this.listView.invalidateViews();
            invalidate();
            if (this.isEditing || !this.orderChanged) {
                return;
            }
            MessagesStorage.getInstance(UserConfig.selectedAccount).saveDialogFiltersOrder();
            TLRPC.TL_messages_updateDialogFiltersOrder tL_messages_updateDialogFiltersOrder = new TLRPC.TL_messages_updateDialogFiltersOrder();
            ArrayList<MessagesController.DialogFilter> arrayList = MessagesController.getInstance(UserConfig.selectedAccount).dialogFilters;
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                arrayList.get(i);
                tL_messages_updateDialogFiltersOrder.order.add(Integer.valueOf(arrayList.get(i).id));
            }
            ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(tL_messages_updateDialogFiltersOrder, new RequestDelegate() { // from class: org.telegram.ui.Components.ViewPagerFixed$TabsView$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    ViewPagerFixed.TabsView.lambda$setIsEditing$2(tLObject, tL_error);
                }
            });
            this.orderChanged = false;
        }

        public void setPreTabClick(Utilities.Callback2Return<Integer, Integer, Boolean> callback2Return) {
            this.preTabClick = callback2Return;
        }

        public void updateColors() {
            this.selectorDrawable.setColor(Theme.getColor(this.tabLineColorKey, this.resourcesProvider));
            this.listView.invalidateViews();
            this.listView.invalidate();
            invalidate();
        }
    }

    public ViewPagerFixed(Context context) {
        this(context, null);
    }

    public ViewPagerFixed(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.currentProgress = 1.0f;
        this.viewsByType = new SparseArray();
        this.notificationsLocker = new AnimationNotificationsLocker();
        this.updateTabProgress = new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ViewPagerFixed.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (ViewPagerFixed.this.tabsAnimationInProgress) {
                    float abs = Math.abs(ViewPagerFixed.this.viewPages[0].getTranslationX()) / ViewPagerFixed.this.viewPages[0].getMeasuredWidth();
                    ViewPagerFixed viewPagerFixed = ViewPagerFixed.this;
                    float f = 1.0f - abs;
                    viewPagerFixed.currentProgress = f;
                    TabsView tabsView = viewPagerFixed.tabsView;
                    if (tabsView != null) {
                        tabsView.selectTab(viewPagerFixed.nextPosition, viewPagerFixed.currentPosition, f);
                    }
                }
                ViewPagerFixed.this.onTabAnimationUpdate(false);
            }
        };
        this.rect = new android.graphics.Rect();
        this.allowDisallowInterceptTouch = true;
        this.resourcesProvider = resourcesProvider;
        this.touchSlop = AndroidUtilities.getPixelsInCM(0.3f, true);
        this.maximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        this.viewTypes = new int[2];
        this.viewPages = new View[2];
        setClipChildren(true);
    }

    public static float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((f - 0.5f) * 0.47123894f);
    }

    private RecyclerListView findRecyclerView(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childAt = viewGroup.getChildAt(i);
                if (childAt instanceof RecyclerListView) {
                    return (RecyclerListView) childAt;
                }
                if (childAt instanceof ViewGroup) {
                    findRecyclerView(childAt);
                }
            }
            return null;
        }
        return null;
    }

    private View findScrollingChild(ViewGroup viewGroup, float f, float f2) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt.getVisibility() == 0) {
                childAt.getHitRect(this.rect);
                if (!this.rect.contains((int) f, (int) f2)) {
                    continue;
                } else if (childAt.canScrollHorizontally(-1)) {
                    return childAt;
                } else {
                    if (childAt instanceof ViewGroup) {
                        android.graphics.Rect rect = this.rect;
                        View findScrollingChild = findScrollingChild((ViewGroup) childAt, f - rect.left, f2 - rect.top);
                        if (findScrollingChild != null) {
                            return findScrollingChild;
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTouchEventInternal$2(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.backProgress = floatValue;
        onBackProgress(floatValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$rebuild$3(ValueAnimator valueAnimator) {
        this.updateTabProgress.onAnimationUpdate(valueAnimator);
        this.tabsView.indicatorProgress2 = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.tabsView.listView.invalidateViews();
        this.tabsView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scrollToPosition$1(ValueAnimator valueAnimator) {
        View view;
        int measuredWidth;
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        View[] viewArr = this.viewPages;
        View view2 = viewArr[1];
        if (view2 == null) {
            return;
        }
        if (this.animatingForward) {
            view2.setTranslationX(viewArr[0].getMeasuredWidth() * (1.0f - floatValue));
            view = this.viewPages[0];
            measuredWidth = -view.getMeasuredWidth();
        } else {
            view2.setTranslationX((-viewArr[0].getMeasuredWidth()) * (1.0f - floatValue));
            view = this.viewPages[0];
            measuredWidth = view.getMeasuredWidth();
        }
        view.setTranslationX(measuredWidth * floatValue);
        this.currentProgress = floatValue;
        onTabAnimationUpdate(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ float lambda$static$0(float f) {
        float f2 = f - 1.0f;
        return (f2 * f2 * f2 * f2 * f2) + 1.0f;
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x000e, code lost:
        if (onBackProgress(0.0f) != false) goto L9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean prepareForMoving(MotionEvent motionEvent, boolean z) {
        if (!z && this.currentPosition == 0) {
            this.backProgress = 0.0f;
        }
        if (!(z && this.currentPosition == this.adapter.getItemCount() - 1) && this.manualScrolling == null && canScroll(motionEvent)) {
            if (!z || canScrollForward(motionEvent)) {
                getParent().requestDisallowInterceptTouchEvent(true);
                this.maybeStartTracking = false;
                this.startedTracking = true;
                onStartTracking();
                this.startedTrackingX = (int) (motionEvent.getX() + this.additionalOffset);
                TabsView tabsView = this.tabsView;
                if (tabsView != null) {
                    tabsView.setEnabled(false);
                }
                this.notificationsLocker.lock();
                this.animatingForward = z;
                this.nextPosition = this.currentPosition + (z ? 1 : -1);
                updateViewForIndex(1);
                View[] viewArr = this.viewPages;
                View view = viewArr[1];
                if (view != null) {
                    view.setTranslationX(z ? viewArr[0].getMeasuredWidth() : -viewArr[0].getMeasuredWidth());
                }
                onTabAnimationUpdate(false);
                return true;
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void swapViews() {
        View[] viewArr = this.viewPages;
        View view = viewArr[0];
        View view2 = viewArr[1];
        viewArr[0] = view2;
        viewArr[1] = view;
        int i = this.currentPosition;
        int i2 = this.nextPosition;
        this.currentPosition = i2;
        this.nextPosition = i;
        this.currentProgress = 1.0f - this.currentProgress;
        int[] iArr = this.viewTypes;
        int i3 = iArr[0];
        iArr[0] = iArr[1];
        iArr[1] = i3;
        onItemSelected(view2, view, i2, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateViewForIndex(int i) {
        View view;
        int i2 = i == 0 ? this.currentPosition : this.nextPosition;
        if (i2 < 0 || i2 >= this.adapter.getItemCount()) {
            return;
        }
        if (this.viewPages[i] == null) {
            this.viewTypes[i] = this.adapter.getItemViewType(i2);
            View view2 = (View) this.viewsByType.get(this.viewTypes[i]);
            if (view2 == null) {
                view2 = this.adapter.createView(this.viewTypes[i]);
            } else {
                this.viewsByType.remove(this.viewTypes[i]);
            }
            if (view2.getParent() != null) {
                ((ViewGroup) view2.getParent()).removeView(view2);
            }
            addView(view2);
            this.viewPages[i] = view2;
            this.adapter.bindView(view2, i2, this.viewTypes[i]);
            view = this.viewPages[i];
        } else if (this.viewTypes[i] != this.adapter.getItemViewType(i2)) {
            this.viewsByType.put(this.viewTypes[i], this.viewPages[i]);
            this.viewPages[i].setVisibility(8);
            removeView(this.viewPages[i]);
            this.viewTypes[i] = this.adapter.getItemViewType(i2);
            View view3 = (View) this.viewsByType.get(this.viewTypes[i]);
            if (view3 == null) {
                view3 = this.adapter.createView(this.viewTypes[i]);
            } else {
                this.viewsByType.remove(this.viewTypes[i]);
            }
            addView(view3);
            this.viewPages[i] = view3;
            view3.setVisibility(0);
            Adapter adapter = this.adapter;
            adapter.bindView(this.viewPages[i], i2, adapter.getItemViewType(i2));
            return;
        } else {
            this.adapter.bindView(this.viewPages[i], i2, this.viewTypes[i]);
            view = this.viewPages[i];
        }
        view.setVisibility(0);
    }

    protected void addMoreTabs() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean canScroll(MotionEvent motionEvent) {
        return true;
    }

    protected boolean canScrollForward(MotionEvent motionEvent) {
        return canScroll(motionEvent);
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int i) {
        if (i == 0) {
            return false;
        }
        if (!this.tabsAnimationInProgress && !this.startedTracking) {
            boolean z = i > 0;
            if ((!z && this.currentPosition == 0) || (z && this.currentPosition == this.adapter.getItemCount() - 1)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkTabsAnimationInProgress() {
        boolean z;
        if (this.tabsAnimationInProgress) {
            if (this.backAnimation) {
                if (Math.abs(this.viewPages[0].getTranslationX()) < 1.0f) {
                    this.viewPages[0].setTranslationX(0.0f);
                    View[] viewArr = this.viewPages;
                    View view = viewArr[1];
                    if (view != null) {
                        view.setTranslationX(viewArr[0].getMeasuredWidth() * (this.animatingForward ? 1 : -1));
                    }
                    z = true;
                }
                z = false;
            } else {
                if (Math.abs(this.viewPages[1].getTranslationX()) < 1.0f) {
                    View view2 = this.viewPages[0];
                    view2.setTranslationX(view2.getMeasuredWidth() * (this.animatingForward ? -1 : 1));
                    View view3 = this.viewPages[1];
                    if (view3 != null) {
                        view3.setTranslationX(0.0f);
                    }
                    z = true;
                }
                z = false;
            }
            onTabAnimationUpdate(true);
            if (z) {
                AnimatorSet animatorSet = this.tabsAnimation;
                if (animatorSet != null) {
                    animatorSet.cancel();
                    this.tabsAnimation = null;
                }
                this.tabsAnimationInProgress = false;
            }
            return this.tabsAnimationInProgress;
        }
        return false;
    }

    public void clearViews() {
        this.viewsByType.clear();
    }

    public TabsView createTabsView(boolean z, int i) {
        TabsView tabsView = new TabsView(getContext(), z, i, this.resourcesProvider) { // from class: org.telegram.ui.Components.ViewPagerFixed.3
            @Override // org.telegram.ui.Components.ViewPagerFixed.TabsView
            public void selectTab(int i2, int i3, float f) {
                super.selectTab(i2, i3, f);
                ViewPagerFixed viewPagerFixed = ViewPagerFixed.this;
                if (f > 0.5f) {
                    i2 = i3;
                }
                viewPagerFixed.onTabPageSelected(i2);
            }
        };
        this.tabsView = tabsView;
        tabsView.tabMarginDp = tabMarginDp();
        this.tabsView.setDelegate(new TabsView.TabsViewDelegate() { // from class: org.telegram.ui.Components.ViewPagerFixed.4
            @Override // org.telegram.ui.Components.ViewPagerFixed.TabsView.TabsViewDelegate
            public boolean canPerformActions() {
                return (ViewPagerFixed.this.tabsAnimationInProgress || ViewPagerFixed.this.startedTracking) ? false : true;
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.TabsView.TabsViewDelegate
            public void invalidateBlur() {
                ViewPagerFixed.this.invalidateBlur();
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.TabsView.TabsViewDelegate
            public void onPageScrolled(float f) {
                View view;
                int measuredWidth;
                if (f == 1.0f) {
                    ViewPagerFixed viewPagerFixed = ViewPagerFixed.this;
                    if (viewPagerFixed.viewPages[1] != null) {
                        viewPagerFixed.swapViews();
                        ViewPagerFixed viewPagerFixed2 = ViewPagerFixed.this;
                        viewPagerFixed2.viewsByType.put(viewPagerFixed2.viewTypes[1], ViewPagerFixed.this.viewPages[1]);
                        ViewPagerFixed viewPagerFixed3 = ViewPagerFixed.this;
                        viewPagerFixed3.removeView(viewPagerFixed3.viewPages[1]);
                        ViewPagerFixed.this.viewPages[0].setTranslationX(0.0f);
                        ViewPagerFixed.this.viewPages[1] = null;
                    }
                    ViewPagerFixed viewPagerFixed4 = ViewPagerFixed.this;
                    viewPagerFixed4.onTabScrollEnd(viewPagerFixed4.currentPosition);
                    return;
                }
                ViewPagerFixed viewPagerFixed5 = ViewPagerFixed.this;
                if (viewPagerFixed5.viewPages[1] == null) {
                    return;
                }
                if (viewPagerFixed5.animatingForward) {
                    View[] viewArr = ViewPagerFixed.this.viewPages;
                    viewArr[1].setTranslationX(viewArr[0].getMeasuredWidth() * (1.0f - f));
                    view = ViewPagerFixed.this.viewPages[0];
                    measuredWidth = -view.getMeasuredWidth();
                } else {
                    View[] viewArr2 = ViewPagerFixed.this.viewPages;
                    viewArr2[1].setTranslationX((-viewArr2[0].getMeasuredWidth()) * (1.0f - f));
                    view = ViewPagerFixed.this.viewPages[0];
                    measuredWidth = view.getMeasuredWidth();
                }
                view.setTranslationX(measuredWidth * f);
                ViewPagerFixed.this.onTabAnimationUpdate(false);
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.TabsView.TabsViewDelegate
            public void onPageSelected(int i2, boolean z2) {
                ViewPagerFixed.this.animatingForward = z2;
                ViewPagerFixed viewPagerFixed = ViewPagerFixed.this;
                viewPagerFixed.nextPosition = i2;
                viewPagerFixed.updateViewForIndex(1);
                ViewPagerFixed.this.onTabPageSelected(i2);
                View view = ViewPagerFixed.this.viewPages[0];
                int measuredWidth = view != null ? view.getMeasuredWidth() : 0;
                View view2 = ViewPagerFixed.this.viewPages[1];
                if (view2 != null) {
                    view2.setTranslationX(z2 ? measuredWidth : -measuredWidth);
                }
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed.TabsView.TabsViewDelegate
            public void onSamePageSelected() {
            }
        });
        fillTabs(false);
        return this.tabsView;
    }

    public void drawForBlur(Canvas canvas) {
        RecyclerListView findRecyclerView;
        int i = 0;
        while (true) {
            View[] viewArr = this.viewPages;
            if (i >= viewArr.length) {
                return;
            }
            View view = viewArr[i];
            if (view != null && view.getVisibility() == 0 && (findRecyclerView = findRecyclerView(this.viewPages[i])) != null) {
                for (int i2 = 0; i2 < findRecyclerView.getChildCount(); i2++) {
                    View childAt = findRecyclerView.getChildAt(i2);
                    if (childAt.getY() < AndroidUtilities.dp(203.0f) + AndroidUtilities.dp(100.0f)) {
                        int save = canvas.save();
                        canvas.translate(this.viewPages[i].getX(), getY() + this.viewPages[i].getY() + findRecyclerView.getY() + childAt.getY());
                        childAt.draw(canvas);
                        canvas.restoreToCount(save);
                    }
                }
            }
            i++;
        }
    }

    public void fillTabs(boolean z) {
        TabsView tabsView;
        if (this.adapter == null || (tabsView = this.tabsView) == null) {
            return;
        }
        tabsView.removeTabs();
        for (int i = 0; i < this.adapter.getItemCount(); i++) {
            this.tabsView.addTab(this.adapter.getItemId(i), this.adapter.getItemTitle(i));
        }
        addMoreTabs();
        if (z) {
            TransitionManager.beginDelayedTransition(this.tabsView.listView, TransitionExt.createSimpleTransition());
        }
        this.tabsView.finishAddingTabs();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float getAvailableTranslationX() {
        return AndroidUtilities.displaySize.x;
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }

    public View getCurrentView() {
        return this.viewPages[0];
    }

    public float getPositionAnimated() {
        float f;
        View view = this.viewPages[0];
        if (view == null || view.getVisibility() != 0) {
            f = 0.0f;
        } else {
            f = (this.currentPosition * Utilities.clamp(1.0f - Math.abs(this.viewPages[0].getTranslationX() / getAvailableTranslationX()), 1.0f, 0.0f)) + 0.0f;
        }
        View view2 = this.viewPages[1];
        if (view2 == null || view2.getVisibility() != 0) {
            return f;
        }
        return f + (this.nextPosition * Utilities.clamp(1.0f - Math.abs(this.viewPages[1].getTranslationX() / getAvailableTranslationX()), 1.0f, 0.0f));
    }

    public View[] getViewPages() {
        return this.viewPages;
    }

    protected void invalidateBlur() {
    }

    public boolean isCurrentTabFirst() {
        return this.currentPosition == 0;
    }

    public boolean isManualScrolling() {
        ValueAnimator valueAnimator = this.manualScrolling;
        return valueAnimator != null && valueAnimator.isRunning();
    }

    public boolean isTouch() {
        return this.startedTracking;
    }

    protected void onBack() {
    }

    protected boolean onBackProgress(float f) {
        return false;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        TabsView tabsView = this.tabsView;
        if (tabsView == null || !tabsView.isAnimatingIndicator()) {
            if (checkTabsAnimationInProgress()) {
                return true;
            }
            onTouchEvent(motionEvent);
            return this.startedTracking;
        }
        return false;
    }

    protected void onItemSelected(View view, View view2, int i, int i2) {
    }

    protected void onScrollEnd() {
    }

    public void onStartTracking() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onTabAnimationUpdate(boolean z) {
    }

    protected void onTabPageSelected(int i) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onTabScrollEnd(int i) {
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return onTouchEventInternal(motionEvent);
    }

    /* JADX WARN: Code restructure failed: missing block: B:159:0x02a9, code lost:
        if (r6.getX() > (r11.viewPages[0].getMeasuredWidth() >> 1)) goto L183;
     */
    /* JADX WARN: Code restructure failed: missing block: B:163:0x02c3, code lost:
        if (r11.viewPages[0].getX() < (r11.viewPages[0].getMeasuredWidth() >> 1)) goto L183;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x02ef, code lost:
        if (java.lang.Math.abs(r5) < java.lang.Math.abs(r6)) goto L183;
     */
    /* JADX WARN: Removed duplicated region for block: B:176:0x02fa  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x036d  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x03f2  */
    /* JADX WARN: Removed duplicated region for block: B:204:0x0453  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x0463  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEventInternal(MotionEvent motionEvent) {
        float f;
        float f2;
        boolean z;
        float f3;
        float abs;
        View[] viewArr;
        boolean z2;
        View[] viewArr2;
        View findScrollingChild;
        View view;
        View view2;
        TabsView tabsView = this.tabsView;
        if (tabsView == null || !tabsView.animatingIndicator) {
            if (motionEvent != null) {
                if (this.velocityTracker == null) {
                    this.velocityTracker = VelocityTracker.obtain();
                }
                this.velocityTracker.addMovement(motionEvent);
            }
            if (motionEvent != null && motionEvent.getAction() == 0 && checkTabsAnimationInProgress()) {
                this.startedTracking = true;
                onStartTracking();
                this.startedTrackingPointerId = motionEvent.getPointerId(0);
                int x = (int) motionEvent.getX();
                this.startedTrackingX = x;
                if (!this.animatingForward) {
                    if (this.viewPages[1] != null) {
                        if (x < view.getMeasuredWidth() + this.viewPages[1].getTranslationX()) {
                            swapViews();
                            this.animatingForward = true;
                            view2 = this.viewPages[0];
                        } else {
                            view2 = this.viewPages[0];
                        }
                    }
                    this.tabsAnimation.removeAllListeners();
                    this.tabsAnimation.cancel();
                    this.tabsAnimationInProgress = false;
                } else if (x < this.viewPages[0].getMeasuredWidth() + this.viewPages[0].getTranslationX()) {
                    view2 = this.viewPages[0];
                } else {
                    swapViews();
                    this.animatingForward = false;
                    view2 = this.viewPages[0];
                }
                this.additionalOffset = view2.getTranslationX();
                this.tabsAnimation.removeAllListeners();
                this.tabsAnimation.cancel();
                this.tabsAnimationInProgress = false;
            } else if (motionEvent != null && motionEvent.getAction() == 0) {
                this.additionalOffset = 0.0f;
            }
            if (this.startedTracking || motionEvent == null || (findScrollingChild = findScrollingChild(this, motionEvent.getX(), motionEvent.getY())) == null || !(findScrollingChild.canScrollHorizontally(1) || findScrollingChild.canScrollHorizontally(-1))) {
                if (motionEvent != null && motionEvent.getAction() == 0 && !this.startedTracking && !this.maybeStartTracking) {
                    this.startedTrackingPointerId = motionEvent.getPointerId(0);
                    this.maybeStartTracking = true;
                    this.startedTrackingX = (int) motionEvent.getX();
                    this.startedTrackingY = (int) motionEvent.getY();
                } else if (motionEvent != null && motionEvent.getAction() == 2 && motionEvent.getPointerId(0) == this.startedTrackingPointerId) {
                    int x2 = (int) ((motionEvent.getX() - this.startedTrackingX) + this.additionalOffset);
                    int abs2 = Math.abs(((int) motionEvent.getY()) - this.startedTrackingY);
                    if (this.startedTracking && (((z2 = this.animatingForward) && x2 > 0) || (!z2 && x2 < 0))) {
                        if (!prepareForMoving(motionEvent, x2 < 0)) {
                            this.maybeStartTracking = true;
                            this.startedTracking = false;
                            this.viewPages[0].setTranslationX(0.0f);
                            View view3 = this.viewPages[1];
                            if (view3 != null) {
                                view3.setTranslationX(this.animatingForward ? viewArr2[0].getMeasuredWidth() : -viewArr2[0].getMeasuredWidth());
                            }
                            this.nextPosition = 0;
                            this.currentProgress = 1.0f;
                            TabsView tabsView2 = this.tabsView;
                            if (tabsView2 != null) {
                                tabsView2.selectTab(0, this.currentPosition, 1.0f);
                            }
                            onTabAnimationUpdate(false);
                        }
                    }
                    if (this.maybeStartTracking && !this.startedTracking) {
                        int x3 = (int) (motionEvent.getX() - this.startedTrackingX);
                        if (Math.abs(x3) >= this.touchSlop && Math.abs(x3) > abs2) {
                            prepareForMoving(motionEvent, x2 < 0);
                        }
                    } else if (this.startedTracking) {
                        float abs3 = Math.abs(x2) / this.viewPages[0].getMeasuredWidth();
                        if (this.nextPosition == -1) {
                            this.backProgress = abs3;
                            onBackProgress(abs3);
                        } else {
                            this.viewPages[0].setTranslationX(x2);
                            View view4 = this.viewPages[1];
                            if (view4 != null) {
                                view4.setTranslationX(this.animatingForward ? viewArr[0].getMeasuredWidth() + x2 : x2 - viewArr[0].getMeasuredWidth());
                            }
                        }
                        float f4 = 1.0f - abs3;
                        this.currentProgress = f4;
                        TabsView tabsView3 = this.tabsView;
                        if (tabsView3 != null) {
                            tabsView3.selectTab(this.nextPosition, this.currentPosition, f4);
                        }
                        onTabAnimationUpdate(false);
                    }
                } else if (motionEvent == null || (motionEvent.getPointerId(0) == this.startedTrackingPointerId && (motionEvent.getAction() == 3 || motionEvent.getAction() == 1 || motionEvent.getAction() == 6))) {
                    VelocityTracker velocityTracker = this.velocityTracker;
                    if (velocityTracker != null) {
                        velocityTracker.computeCurrentVelocity(1000, this.maximumVelocity);
                    }
                    if (motionEvent == null || motionEvent.getAction() == 3) {
                        f = 0.0f;
                        f2 = 0.0f;
                    } else {
                        f = this.velocityTracker.getXVelocity();
                        f2 = this.velocityTracker.getYVelocity();
                        if (!this.startedTracking && Math.abs(f) >= 3000.0f && Math.abs(f) > Math.abs(f2)) {
                            prepareForMoving(motionEvent, f < 0.0f);
                        }
                    }
                    if (this.startedTracking) {
                        float x4 = this.viewPages[0].getX();
                        this.tabsAnimation = new AnimatorSet();
                        if (this.additionalOffset == 0.0f) {
                            if (Math.abs(x4) < this.viewPages[0].getMeasuredWidth() / 3.0f) {
                                if (Math.abs(f) >= 3500.0f) {
                                }
                                z = true;
                            }
                        } else if (Math.abs(f) > 1500.0f) {
                            z = !this.animatingForward ? false : false;
                        } else if (this.animatingForward) {
                            View view5 = this.viewPages[1];
                            if (view5 == null) {
                                this.backAnimation = false;
                                if (!this.backAnimation) {
                                    f3 = Math.abs(x4);
                                    if (this.animatingForward) {
                                        AnimatorSet animatorSet = this.tabsAnimation;
                                        View view6 = this.viewPages[0];
                                        Property property = View.TRANSLATION_X;
                                        animatorSet.playTogether(ObjectAnimator.ofFloat(view6, property, 0.0f));
                                        View view7 = this.viewPages[1];
                                        if (view7 != null) {
                                            this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(view7, property, view7.getMeasuredWidth()));
                                        }
                                    } else {
                                        AnimatorSet animatorSet2 = this.tabsAnimation;
                                        View view8 = this.viewPages[0];
                                        Property property2 = View.TRANSLATION_X;
                                        animatorSet2.playTogether(ObjectAnimator.ofFloat(view8, property2, 0.0f));
                                        View view9 = this.viewPages[1];
                                        if (view9 != null) {
                                            this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(view9, property2, -view9.getMeasuredWidth()));
                                        }
                                    }
                                } else if (this.nextPosition >= 0) {
                                    f3 = this.viewPages[0].getMeasuredWidth() - Math.abs(x4);
                                    if (this.animatingForward) {
                                        AnimatorSet animatorSet3 = this.tabsAnimation;
                                        View view10 = this.viewPages[0];
                                        Property property3 = View.TRANSLATION_X;
                                        animatorSet3.playTogether(ObjectAnimator.ofFloat(view10, property3, -view10.getMeasuredWidth()));
                                        View view11 = this.viewPages[1];
                                        if (view11 != null) {
                                            this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(view11, property3, 0.0f));
                                        }
                                    } else {
                                        AnimatorSet animatorSet4 = this.tabsAnimation;
                                        View view12 = this.viewPages[0];
                                        Property property4 = View.TRANSLATION_X;
                                        animatorSet4.playTogether(ObjectAnimator.ofFloat(view12, property4, view12.getMeasuredWidth()));
                                        View view13 = this.viewPages[1];
                                        if (view13 != null) {
                                            this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(view13, property4, 0.0f));
                                        }
                                    }
                                } else {
                                    f3 = 0.0f;
                                }
                                if (this.nextPosition < 0) {
                                    ValueAnimator ofFloat = ValueAnimator.ofFloat(this.backProgress, this.backAnimation ? 0.0f : 1.0f);
                                    ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ViewPagerFixed$$ExternalSyntheticLambda2
                                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                                        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                            ViewPagerFixed.this.lambda$onTouchEventInternal$2(valueAnimator);
                                        }
                                    });
                                    this.tabsAnimation.playTogether(ofFloat);
                                }
                                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                                ofFloat2.addUpdateListener(this.updateTabProgress);
                                this.tabsAnimation.playTogether(ofFloat2);
                                this.tabsAnimation.setInterpolator(interpolator);
                                int measuredWidth = getMeasuredWidth();
                                float f5 = measuredWidth / 2;
                                float distanceInfluenceForSnapDuration = f5 + (distanceInfluenceForSnapDuration(Math.min(1.0f, (f3 * 1.0f) / measuredWidth)) * f5);
                                this.tabsAnimation.setDuration(Math.max(150, Math.min(Math.abs(f) <= 0.0f ? Math.round(Math.abs(distanceInfluenceForSnapDuration / abs) * 1000.0f) * 4 : (int) (((f3 / getMeasuredWidth()) + 1.0f) * 100.0f), 600)));
                                this.tabsAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ViewPagerFixed.5
                                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                    public void onAnimationEnd(Animator animator) {
                                        ViewPagerFixed.this.tabsAnimation = null;
                                        ViewPagerFixed viewPagerFixed = ViewPagerFixed.this;
                                        if (viewPagerFixed.nextPosition < 0) {
                                            viewPagerFixed.onBack();
                                        }
                                        ViewPagerFixed viewPagerFixed2 = ViewPagerFixed.this;
                                        if (viewPagerFixed2.viewPages[1] != null) {
                                            if (!viewPagerFixed2.backAnimation) {
                                                ViewPagerFixed.this.swapViews();
                                            }
                                            ViewPagerFixed viewPagerFixed3 = ViewPagerFixed.this;
                                            viewPagerFixed3.viewsByType.put(viewPagerFixed3.viewTypes[1], ViewPagerFixed.this.viewPages[1]);
                                            ViewPagerFixed viewPagerFixed4 = ViewPagerFixed.this;
                                            viewPagerFixed4.removeView(viewPagerFixed4.viewPages[1]);
                                            ViewPagerFixed.this.viewPages[1].setVisibility(8);
                                            ViewPagerFixed.this.viewPages[1] = null;
                                        }
                                        ViewPagerFixed.this.tabsAnimationInProgress = false;
                                        ViewPagerFixed.this.maybeStartTracking = false;
                                        TabsView tabsView4 = ViewPagerFixed.this.tabsView;
                                        if (tabsView4 != null) {
                                            tabsView4.setEnabled(true);
                                        }
                                        ViewPagerFixed.this.onTabAnimationUpdate(false);
                                        ViewPagerFixed.this.onScrollEnd();
                                        ViewPagerFixed.this.notificationsLocker.unlock();
                                    }
                                });
                                this.tabsAnimation.start();
                                this.tabsAnimationInProgress = true;
                                this.startedTracking = false;
                                onTabAnimationUpdate(false);
                            }
                        }
                        this.backAnimation = z;
                        if (!this.backAnimation) {
                        }
                        if (this.nextPosition < 0) {
                        }
                        ValueAnimator ofFloat22 = ValueAnimator.ofFloat(0.0f, 1.0f);
                        ofFloat22.addUpdateListener(this.updateTabProgress);
                        this.tabsAnimation.playTogether(ofFloat22);
                        this.tabsAnimation.setInterpolator(interpolator);
                        int measuredWidth2 = getMeasuredWidth();
                        float f52 = measuredWidth2 / 2;
                        float distanceInfluenceForSnapDuration2 = f52 + (distanceInfluenceForSnapDuration(Math.min(1.0f, (f3 * 1.0f) / measuredWidth2)) * f52);
                        this.tabsAnimation.setDuration(Math.max(150, Math.min(Math.abs(f) <= 0.0f ? Math.round(Math.abs(distanceInfluenceForSnapDuration2 / abs) * 1000.0f) * 4 : (int) (((f3 / getMeasuredWidth()) + 1.0f) * 100.0f), 600)));
                        this.tabsAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ViewPagerFixed.5
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                ViewPagerFixed.this.tabsAnimation = null;
                                ViewPagerFixed viewPagerFixed = ViewPagerFixed.this;
                                if (viewPagerFixed.nextPosition < 0) {
                                    viewPagerFixed.onBack();
                                }
                                ViewPagerFixed viewPagerFixed2 = ViewPagerFixed.this;
                                if (viewPagerFixed2.viewPages[1] != null) {
                                    if (!viewPagerFixed2.backAnimation) {
                                        ViewPagerFixed.this.swapViews();
                                    }
                                    ViewPagerFixed viewPagerFixed3 = ViewPagerFixed.this;
                                    viewPagerFixed3.viewsByType.put(viewPagerFixed3.viewTypes[1], ViewPagerFixed.this.viewPages[1]);
                                    ViewPagerFixed viewPagerFixed4 = ViewPagerFixed.this;
                                    viewPagerFixed4.removeView(viewPagerFixed4.viewPages[1]);
                                    ViewPagerFixed.this.viewPages[1].setVisibility(8);
                                    ViewPagerFixed.this.viewPages[1] = null;
                                }
                                ViewPagerFixed.this.tabsAnimationInProgress = false;
                                ViewPagerFixed.this.maybeStartTracking = false;
                                TabsView tabsView4 = ViewPagerFixed.this.tabsView;
                                if (tabsView4 != null) {
                                    tabsView4.setEnabled(true);
                                }
                                ViewPagerFixed.this.onTabAnimationUpdate(false);
                                ViewPagerFixed.this.onScrollEnd();
                                ViewPagerFixed.this.notificationsLocker.unlock();
                            }
                        });
                        this.tabsAnimation.start();
                        this.tabsAnimationInProgress = true;
                        this.startedTracking = false;
                        onTabAnimationUpdate(false);
                    } else {
                        this.maybeStartTracking = false;
                        TabsView tabsView4 = this.tabsView;
                        if (tabsView4 != null) {
                            tabsView4.setEnabled(true);
                        }
                    }
                    VelocityTracker velocityTracker2 = this.velocityTracker;
                    if (velocityTracker2 != null) {
                        velocityTracker2.recycle();
                        this.velocityTracker = null;
                    }
                }
                return this.startedTracking || this.maybeStartTracking;
            }
            return false;
        }
        return false;
    }

    public void rebuild(boolean z) {
        onTouchEvent(null);
        if (!this.adapter.hasStableId()) {
            z = false;
        }
        AnimatorSet animatorSet = this.tabsAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.tabsAnimation = null;
        }
        View view = this.viewPages[1];
        if (view != null) {
            removeView(view);
            this.viewPages[1] = null;
        }
        View[] viewArr = this.viewPages;
        View view2 = viewArr[0];
        viewArr[1] = view2;
        int intValue = (view2 == null || view2.getTag() == null) ? 0 : ((Integer) this.viewPages[1].getTag()).intValue();
        if (this.adapter.getItemCount() == 0) {
            View view3 = this.viewPages[1];
            if (view3 != null) {
                removeView(view3);
                this.viewPages[1] = null;
            }
            View view4 = this.viewPages[0];
            if (view4 != null) {
                removeView(view4);
                this.viewPages[0] = null;
                return;
            }
            return;
        }
        if (this.currentPosition > this.adapter.getItemCount() - 1) {
            this.currentPosition = this.adapter.getItemCount() - 1;
        }
        if (this.currentPosition < 0) {
            this.currentPosition = 0;
        }
        this.viewTypes[0] = this.adapter.getItemViewType(this.currentPosition);
        this.viewPages[0] = this.adapter.createView(this.viewTypes[0]);
        this.adapter.bindView(this.viewPages[0], this.currentPosition, this.viewTypes[0]);
        addView(this.viewPages[0]);
        this.viewPages[0].setVisibility(0);
        if ((this.viewPages[0].getTag() == null ? 0 : ((Integer) this.viewPages[0].getTag()).intValue()) == intValue) {
            z = false;
        }
        if (z) {
            this.tabsView.saveFromValues();
        }
        fillTabs(z);
        if (!z) {
            View view5 = this.viewPages[1];
            if (view5 != null) {
                removeView(view5);
                this.viewPages[1] = null;
                return;
            }
            return;
        }
        this.tabsAnimation = new AnimatorSet();
        View view6 = this.viewPages[1];
        if (view6 != null) {
            view6.setTranslationX(0.0f);
        }
        View view7 = this.viewPages[0];
        if (view7 != null) {
            view7.setTranslationX(-getMeasuredWidth());
        }
        View view8 = this.viewPages[1];
        if (view8 != null) {
            this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(view8, View.TRANSLATION_X, getMeasuredWidth()));
        }
        View view9 = this.viewPages[0];
        if (view9 != null) {
            this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(view9, View.TRANSLATION_X, 0.0f));
        }
        onTabAnimationUpdate(true);
        this.tabsView.indicatorProgress2 = 0.0f;
        this.tabsView.listView.invalidateViews();
        this.tabsView.invalidate();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ViewPagerFixed$$ExternalSyntheticLambda3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewPagerFixed.this.lambda$rebuild$3(valueAnimator);
            }
        });
        this.tabsAnimation.playTogether(ofFloat);
        this.tabsAnimation.setInterpolator(interpolator);
        this.tabsAnimation.setDuration(220L);
        this.tabsAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ViewPagerFixed.6
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ViewPagerFixed.this.tabsAnimation = null;
                ViewPagerFixed viewPagerFixed = ViewPagerFixed.this;
                View view10 = viewPagerFixed.viewPages[1];
                if (view10 != null) {
                    viewPagerFixed.removeView(view10);
                    ViewPagerFixed.this.viewPages[1] = null;
                }
                ViewPagerFixed.this.tabsAnimationInProgress = false;
                TabsView tabsView = ViewPagerFixed.this.tabsView;
                if (tabsView != null) {
                    tabsView.setEnabled(true);
                    ViewPagerFixed.this.tabsView.animatingIndicator = false;
                    ViewPagerFixed.this.tabsView.indicatorProgress2 = 1.0f;
                    ViewPagerFixed.this.tabsView.listView.invalidateViews();
                    ViewPagerFixed.this.tabsView.invalidate();
                }
            }
        });
        this.tabsView.setEnabled(false);
        this.tabsAnimationInProgress = true;
        this.tabsAnimation.start();
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z) {
        if (this.allowDisallowInterceptTouch && this.maybeStartTracking && !this.startedTracking) {
            onTouchEvent(null);
        }
        super.requestDisallowInterceptTouchEvent(z);
    }

    public void resetTouch() {
        if (this.startedTracking) {
            this.maybeStartTracking = true;
            this.startedTracking = false;
            this.viewPages[0].setTranslationX(0.0f);
            View[] viewArr = this.viewPages;
            View view = viewArr[1];
            if (view != null) {
                view.setTranslationX(this.animatingForward ? viewArr[0].getMeasuredWidth() : -viewArr[0].getMeasuredWidth());
            }
            this.nextPosition = 0;
            this.currentProgress = 1.0f;
            TabsView tabsView = this.tabsView;
            if (tabsView != null) {
                tabsView.selectTab(0, this.currentPosition, 1.0f);
            }
            onTabAnimationUpdate(false);
        }
    }

    public boolean scrollToPosition(int i) {
        ValueAnimator valueAnimator;
        View view;
        float f;
        if (i == this.currentPosition || ((valueAnimator = this.manualScrolling) != null && this.nextPosition == i)) {
            return false;
        }
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.manualScrolling = null;
        }
        boolean z = this.currentPosition < i;
        this.animatingForward = z;
        this.nextPosition = i;
        updateViewForIndex(1);
        onTabPageSelected(i);
        View view2 = this.viewPages[0];
        int measuredWidth = view2 != null ? view2.getMeasuredWidth() : 0;
        View[] viewArr = this.viewPages;
        if (z) {
            view = viewArr[1];
            f = measuredWidth;
        } else {
            view = viewArr[1];
            f = -measuredWidth;
        }
        view.setTranslationX(f);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.manualScrolling = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ViewPagerFixed$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ViewPagerFixed.this.lambda$scrollToPosition$1(valueAnimator2);
            }
        });
        this.manualScrolling.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ViewPagerFixed.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ViewPagerFixed viewPagerFixed = ViewPagerFixed.this;
                if (viewPagerFixed.viewPages[1] != null) {
                    viewPagerFixed.swapViews();
                    ViewPagerFixed viewPagerFixed2 = ViewPagerFixed.this;
                    viewPagerFixed2.viewsByType.put(viewPagerFixed2.viewTypes[1], ViewPagerFixed.this.viewPages[1]);
                    ViewPagerFixed viewPagerFixed3 = ViewPagerFixed.this;
                    viewPagerFixed3.removeView(viewPagerFixed3.viewPages[1]);
                    ViewPagerFixed.this.viewPages[0].setTranslationX(0.0f);
                    ViewPagerFixed.this.viewPages[1] = null;
                }
                ViewPagerFixed.this.manualScrolling = null;
                ViewPagerFixed.this.onTabAnimationUpdate(true);
                ViewPagerFixed.this.onScrollEnd();
                ViewPagerFixed.this.notificationsLocker.unlock();
            }
        });
        this.manualScrolling.setDuration(540L);
        this.manualScrolling.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.manualScrolling.start();
        return true;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        this.viewTypes[0] = adapter.getItemViewType(this.currentPosition);
        this.viewPages[0] = adapter.createView(this.viewTypes[0]);
        if (this.viewPages[0] == null && this.currentPosition != 0) {
            this.currentPosition = 0;
            this.viewTypes[0] = adapter.getItemViewType(0);
            this.viewPages[0] = adapter.createView(this.viewTypes[0]);
        }
        adapter.bindView(this.viewPages[0], this.currentPosition, this.viewTypes[0]);
        addView(this.viewPages[0]);
        this.viewPages[0].setVisibility(0);
        fillTabs(false);
    }

    public void setAllowDisallowInterceptTouch(boolean z) {
        this.allowDisallowInterceptTouch = z;
    }

    public void setPosition(int i) {
        if (this.adapter == null) {
            this.currentPosition = i;
            onTabAnimationUpdate(false);
        }
        AnimatorSet animatorSet = this.tabsAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        View view = this.viewPages[1];
        if (view != null) {
            this.viewsByType.put(this.viewTypes[1], view);
            removeView(this.viewPages[1]);
            this.viewPages[1] = null;
        }
        int i2 = this.currentPosition;
        if (i2 != i) {
            this.currentPosition = i;
            this.nextPosition = 0;
            this.currentProgress = 1.0f;
            View view2 = this.viewPages[0];
            updateViewForIndex(0);
            onItemSelected(this.viewPages[0], view2, this.currentPosition, i2);
            this.viewPages[0].setTranslationX(0.0f);
            TabsView tabsView = this.tabsView;
            if (tabsView != null) {
                tabsView.selectTab(this.currentPosition, this.nextPosition, this.currentProgress);
            }
            onTabAnimationUpdate(true);
        }
    }

    protected int tabMarginDp() {
        return 16;
    }

    public void updateCurrent() {
        if (this.viewTypes[0] != this.adapter.getItemViewType(this.currentPosition)) {
            updateViewForIndex(0);
            View view = this.viewPages[1];
            if (view != null) {
                this.viewsByType.put(this.viewTypes[1], view);
                removeView(this.viewPages[1]);
                this.viewPages[1] = null;
            }
            this.viewPages[0].setTranslationX(0.0f);
            onTabAnimationUpdate(true);
        }
    }
}
