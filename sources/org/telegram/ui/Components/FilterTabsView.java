package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Property;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.FilterTabsView;
import org.telegram.ui.Components.RecyclerListView;

/* loaded from: classes3.dex */
public abstract class FilterTabsView extends FrameLayout {
    private final Property COLORS;
    private int aActiveTextColorKey;
    private int aBackgroundColorKey;
    private int aTabLineColorKey;
    private int aUnactiveTextColorKey;
    private int activeTextColorKey;
    private ListAdapter adapter;
    private int additionalTabWidth;
    private int allTabsWidth;
    private boolean animatingIndicator;
    private float animatingIndicatorProgress;
    private Runnable animationRunnable;
    private float animationTime;
    private float animationValue;
    private int backgroundColorKey;
    private AnimatorSet colorChangeAnimator;
    private Paint counterPaint;
    private int currentPosition;
    private FilterTabsViewDelegate delegate;
    private Paint deletePaint;
    private float editingAnimationProgress;
    private boolean editingForwardAnimation;
    private float editingStartAnimationProgress;
    private SparseIntArray idToPosition;
    private boolean ignoreLayout;
    private CubicBezierInterpolator interpolator;
    private boolean invalidated;
    private boolean isEditing;
    DefaultItemAnimator itemAnimator;
    private long lastAnimationTime;
    private long lastEditingAnimationTime;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private Drawable lockDrawable;
    private int lockDrawableColor;
    private int manualScrollingToId;
    private int manualScrollingToPosition;
    private boolean orderChanged;
    private SparseIntArray positionToId;
    private SparseIntArray positionToStableId;
    private SparseIntArray positionToWidth;
    private SparseIntArray positionToX;
    private int prevLayoutWidth;
    private int previousId;
    private int previousPosition;
    private int scrollingToChild;
    private int selectedTabId;
    private int selectorColorKey;
    private GradientDrawable selectorDrawable;
    private int tabLineColorKey;
    private ArrayList tabs;
    private TextPaint textCounterPaint;
    private TextPaint textPaint;
    private int unactiveTextColorKey;

    class 4 extends DefaultItemAnimator {
        4() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$animateMoveImpl$1(TabView tabView, ValueAnimator valueAnimator) {
            tabView.changeProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            tabView.invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$runPendingAnimations$0(ValueAnimator valueAnimator) {
            FilterTabsView.this.listView.invalidate();
            FilterTabsView.this.invalidate();
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.SimpleItemAnimator
        public boolean animateMove(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, int i, int i2, int i3, int i4) {
            View view = viewHolder.itemView;
            if (!(view instanceof TabView)) {
                return super.animateMove(viewHolder, itemHolderInfo, i, i2, i3, i4);
            }
            int translationX = i + ((int) view.getTranslationX());
            int translationY = i2 + ((int) viewHolder.itemView.getTranslationY());
            resetAnimation(viewHolder);
            int i5 = i3 - translationX;
            int i6 = i4 - translationY;
            if (i5 != 0) {
                view.setTranslationX(-i5);
            }
            if (i6 != 0) {
                view.setTranslationY(-i6);
            }
            TabView tabView = (TabView) viewHolder.itemView;
            boolean animateChange = tabView.animateChange();
            if (animateChange) {
                tabView.changeProgress = 0.0f;
                tabView.animateChange = true;
                FilterTabsView.this.invalidate();
            }
            if (i5 == 0 && i6 == 0 && !animateChange) {
                dispatchMoveFinished(viewHolder);
                return false;
            }
            this.mPendingMoves.add(new DefaultItemAnimator.MoveInfo(viewHolder, translationX, translationY, i3, i4));
            return true;
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator
        protected void animateMoveImpl(RecyclerView.ViewHolder viewHolder, DefaultItemAnimator.MoveInfo moveInfo) {
            super.animateMoveImpl(viewHolder, moveInfo);
            View view = viewHolder.itemView;
            if (view instanceof TabView) {
                final TabView tabView = (TabView) view;
                if (tabView.animateChange) {
                    ValueAnimator valueAnimator = tabView.changeAnimator;
                    if (valueAnimator != null) {
                        valueAnimator.removeAllListeners();
                        tabView.changeAnimator.removeAllUpdateListeners();
                        tabView.changeAnimator.cancel();
                    }
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                    ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.FilterTabsView$4$$ExternalSyntheticLambda1
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                            FilterTabsView.4.lambda$animateMoveImpl$1(FilterTabsView.TabView.this, valueAnimator2);
                        }
                    });
                    ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.FilterTabsView.4.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            tabView.clearTransitionParams();
                        }
                    });
                    tabView.changeAnimator = ofFloat;
                    ofFloat.setDuration(getMoveDuration());
                    ofFloat.start();
                }
            }
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
        public void endAnimation(RecyclerView.ViewHolder viewHolder) {
            super.endAnimation(viewHolder);
            viewHolder.itemView.setTranslationX(0.0f);
            View view = viewHolder.itemView;
            if (view instanceof TabView) {
                ((TabView) view).clearTransitionParams();
            }
        }

        @Override // androidx.recyclerview.widget.SimpleItemAnimator
        public void onMoveFinished(RecyclerView.ViewHolder viewHolder) {
            super.onMoveFinished(viewHolder);
            viewHolder.itemView.setTranslationX(0.0f);
            View view = viewHolder.itemView;
            if (view instanceof TabView) {
                ((TabView) view).clearTransitionParams();
            }
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
        public void runPendingAnimations() {
            boolean z = !this.mPendingRemovals.isEmpty();
            boolean z2 = !this.mPendingMoves.isEmpty();
            boolean z3 = !this.mPendingChanges.isEmpty();
            boolean z4 = !this.mPendingAdditions.isEmpty();
            if (z || z2 || z4 || z3) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.1f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.FilterTabsView$4$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        FilterTabsView.4.this.lambda$runPendingAnimations$0(valueAnimator);
                    }
                });
                ofFloat.setDuration(getMoveDuration());
                ofFloat.start();
            }
            super.runPendingAnimations();
        }
    }

    public interface FilterTabsViewDelegate {
        boolean canPerformActions();

        boolean didSelectTab(TabView tabView, boolean z);

        int getTabCounter(int i);

        boolean isTabMenuVisible();

        void onDeletePressed(int i);

        void onPageReorder(int i, int i2);

        void onPageScrolled(float f);

        void onPageSelected(Tab tab, boolean z);

        void onSamePageSelected();
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return FilterTabsView.this.tabs.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public long getItemId(int i) {
            return FilterTabsView.this.positionToStableId.get(i);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        public void moveElementToStart(int i) {
            int size = FilterTabsView.this.tabs.size();
            if (i < 0 || i >= size) {
                return;
            }
            ArrayList<MessagesController.DialogFilter> dialogFilters = MessagesController.getInstance(UserConfig.selectedAccount).getDialogFilters();
            int i2 = FilterTabsView.this.positionToStableId.get(i);
            int i3 = ((Tab) FilterTabsView.this.tabs.get(i)).id;
            for (int i4 = i - 1; i4 >= 0; i4--) {
                FilterTabsView.this.positionToStableId.put(i4 + 1, FilterTabsView.this.positionToStableId.get(i4));
            }
            MessagesController.DialogFilter remove = dialogFilters.remove(i);
            remove.order = 0;
            dialogFilters.add(0, remove);
            FilterTabsView.this.positionToStableId.put(0, i2);
            FilterTabsView.this.tabs.add(0, (Tab) FilterTabsView.this.tabs.remove(i));
            ((Tab) FilterTabsView.this.tabs.get(0)).id = i3;
            for (int i5 = 0; i5 <= i; i5++) {
                ((Tab) FilterTabsView.this.tabs.get(i5)).id = i5;
                dialogFilters.get(i5).order = i5;
            }
            int i6 = 0;
            while (i6 <= i) {
                if (FilterTabsView.this.currentPosition == i6) {
                    FilterTabsView filterTabsView = FilterTabsView.this;
                    filterTabsView.currentPosition = filterTabsView.selectedTabId = i6 == i ? 0 : i6 + 1;
                }
                if (FilterTabsView.this.previousPosition == i6) {
                    FilterTabsView filterTabsView2 = FilterTabsView.this;
                    filterTabsView2.previousPosition = filterTabsView2.previousId = i6 == i ? 0 : i6 + 1;
                }
                i6++;
            }
            notifyItemMoved(i, 0);
            FilterTabsView.this.delegate.onPageReorder(((Tab) FilterTabsView.this.tabs.get(i)).id, i3);
            FilterTabsView.this.updateTabsWidths();
            FilterTabsView.this.orderChanged = true;
            FilterTabsView.this.listView.setItemAnimator(FilterTabsView.this.itemAnimator);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            TabView tabView = (TabView) viewHolder.itemView;
            int id = tabView.currentTab != null ? tabView.getId() : -1;
            tabView.setTab((Tab) FilterTabsView.this.tabs.get(i), i);
            if (id != tabView.getId()) {
                tabView.progressToLocked = tabView.currentTab.isLocked ? 1.0f : 0.0f;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new RecyclerListView.Holder(FilterTabsView.this.new TabView(this.mContext));
        }

        /* JADX WARN: Removed duplicated region for block: B:12:0x00ba  */
        /* JADX WARN: Removed duplicated region for block: B:17:0x00c7  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void swapElements(int i, int i2) {
            FilterTabsView filterTabsView;
            int i3;
            FilterTabsView filterTabsView2;
            int i4;
            int size = FilterTabsView.this.tabs.size();
            if (i < 0 || i2 < 0 || i >= size || i2 >= size) {
                return;
            }
            ArrayList<MessagesController.DialogFilter> dialogFilters = MessagesController.getInstance(UserConfig.selectedAccount).getDialogFilters();
            MessagesController.DialogFilter dialogFilter = dialogFilters.get(i);
            MessagesController.DialogFilter dialogFilter2 = dialogFilters.get(i2);
            int i5 = dialogFilter.order;
            dialogFilter.order = dialogFilter2.order;
            dialogFilter2.order = i5;
            dialogFilters.set(i, dialogFilter2);
            dialogFilters.set(i2, dialogFilter);
            Tab tab = (Tab) FilterTabsView.this.tabs.get(i);
            Tab tab2 = (Tab) FilterTabsView.this.tabs.get(i2);
            int i6 = tab.id;
            tab.id = tab2.id;
            tab2.id = i6;
            int i7 = FilterTabsView.this.positionToStableId.get(i);
            FilterTabsView.this.positionToStableId.put(i, FilterTabsView.this.positionToStableId.get(i2));
            FilterTabsView.this.positionToStableId.put(i2, i7);
            FilterTabsView.this.delegate.onPageReorder(tab2.id, tab.id);
            if (FilterTabsView.this.currentPosition != i) {
                if (FilterTabsView.this.currentPosition == i2) {
                    FilterTabsView.this.currentPosition = i;
                    filterTabsView = FilterTabsView.this;
                    i3 = tab2.id;
                }
                if (FilterTabsView.this.previousPosition == i) {
                    if (FilterTabsView.this.previousPosition == i2) {
                        FilterTabsView.this.previousPosition = i;
                        filterTabsView2 = FilterTabsView.this;
                        i4 = tab2.id;
                    }
                    FilterTabsView.this.tabs.set(i, tab2);
                    FilterTabsView.this.tabs.set(i2, tab);
                    FilterTabsView.this.updateTabsWidths();
                    FilterTabsView.this.orderChanged = true;
                    FilterTabsView.this.listView.setItemAnimator(FilterTabsView.this.itemAnimator);
                    notifyItemMoved(i, i2);
                }
                FilterTabsView.this.previousPosition = i2;
                filterTabsView2 = FilterTabsView.this;
                i4 = tab.id;
                filterTabsView2.previousId = i4;
                FilterTabsView.this.tabs.set(i, tab2);
                FilterTabsView.this.tabs.set(i2, tab);
                FilterTabsView.this.updateTabsWidths();
                FilterTabsView.this.orderChanged = true;
                FilterTabsView.this.listView.setItemAnimator(FilterTabsView.this.itemAnimator);
                notifyItemMoved(i, i2);
            }
            FilterTabsView.this.currentPosition = i2;
            filterTabsView = FilterTabsView.this;
            i3 = tab.id;
            filterTabsView.selectedTabId = i3;
            if (FilterTabsView.this.previousPosition == i) {
            }
            filterTabsView2.previousId = i4;
            FilterTabsView.this.tabs.set(i, tab2);
            FilterTabsView.this.tabs.set(i2, tab);
            FilterTabsView.this.updateTabsWidths();
            FilterTabsView.this.orderChanged = true;
            FilterTabsView.this.listView.setItemAnimator(FilterTabsView.this.itemAnimator);
            notifyItemMoved(i, i2);
        }
    }

    public class Tab {
        public int counter;
        public int id;
        public boolean isDefault;
        public boolean isLocked;
        public String title;
        public int titleWidth;

        public Tab(int i, String str) {
            this.id = i;
            this.title = str;
        }

        public int getWidth(boolean z) {
            int i;
            int ceil = (int) Math.ceil(FilterTabsView.this.textPaint.measureText(this.title));
            this.titleWidth = ceil;
            if (z) {
                i = FilterTabsView.this.delegate.getTabCounter(this.id);
                if (i < 0) {
                    i = 0;
                }
                if (z) {
                    this.counter = i;
                }
            } else {
                i = this.counter;
            }
            if (i > 0) {
                ceil += Math.max(AndroidUtilities.dp(10.0f), (int) Math.ceil(FilterTabsView.this.textCounterPaint.measureText(String.format("%d", Integer.valueOf(i))))) + AndroidUtilities.dp(10.0f) + AndroidUtilities.dp(6.0f);
            }
            return Math.max(AndroidUtilities.dp(40.0f), ceil);
        }

        public boolean setTitle(String str) {
            if (TextUtils.equals(this.title, str)) {
                return false;
            }
            this.title = str;
            return true;
        }
    }

    public class TabView extends View {
        public boolean animateChange;
        public boolean animateCounterChange;
        private float animateFromCountWidth;
        private float animateFromCounterWidth;
        int animateFromTabCount;
        private float animateFromTabWidth;
        float animateFromTextX;
        private int animateFromTitleWidth;
        private float animateFromWidth;
        boolean animateTabCounter;
        private boolean animateTabWidth;
        private boolean animateTextChange;
        private boolean animateTextChangeOut;
        boolean animateTextX;
        public ValueAnimator changeAnimator;
        public float changeProgress;
        private int currentPosition;
        private Tab currentTab;
        private String currentText;
        StaticLayout inCounter;
        private int lastCountWidth;
        private float lastCounterWidth;
        int lastTabCount;
        private float lastTabWidth;
        float lastTextX;
        String lastTitle;
        StaticLayout lastTitleLayout;
        private int lastTitleWidth;
        private float lastWidth;
        private float locIconXOffset;
        StaticLayout outCounter;
        private float progressToLocked;
        private RectF rect;
        StaticLayout stableCounter;
        private int tabWidth;
        private int textHeight;
        private StaticLayout textLayout;
        private int textOffsetX;
        private StaticLayout titleAnimateInLayout;
        private StaticLayout titleAnimateOutLayout;
        private StaticLayout titleAnimateStableLayout;
        private float titleXOffset;

        public TabView(Context context) {
            super(context);
            this.rect = new RectF();
            this.lastTabCount = -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$shakeLockIcon$0(ValueAnimator valueAnimator) {
            this.locIconXOffset = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidate();
        }

        public boolean animateChange() {
            boolean z;
            String str;
            int i;
            String str2;
            String str3;
            boolean z2;
            int i2 = this.currentTab.counter;
            int i3 = this.lastTabCount;
            if (i2 != i3) {
                this.animateTabCounter = true;
                this.animateFromTabCount = i3;
                this.animateFromCountWidth = this.lastCountWidth;
                this.animateFromCounterWidth = this.lastCounterWidth;
                if (i3 > 0 && i2 > 0) {
                    String valueOf = String.valueOf(i3);
                    String valueOf2 = String.valueOf(this.currentTab.counter);
                    if (valueOf.length() == valueOf2.length()) {
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(valueOf);
                        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(valueOf2);
                        SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(valueOf2);
                        for (int i4 = 0; i4 < valueOf.length(); i4++) {
                            if (valueOf.charAt(i4) == valueOf2.charAt(i4)) {
                                int i5 = i4 + 1;
                                spannableStringBuilder.setSpan(new EmptyStubSpan(), i4, i5, 0);
                                spannableStringBuilder2.setSpan(new EmptyStubSpan(), i4, i5, 0);
                            } else {
                                spannableStringBuilder3.setSpan(new EmptyStubSpan(), i4, i4 + 1, 0);
                            }
                        }
                        int ceil = (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(valueOf));
                        TextPaint textPaint = FilterTabsView.this.textCounterPaint;
                        Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
                        this.outCounter = new StaticLayout(spannableStringBuilder, textPaint, ceil, alignment, 1.0f, 0.0f, false);
                        this.stableCounter = new StaticLayout(spannableStringBuilder3, FilterTabsView.this.textCounterPaint, ceil, alignment, 1.0f, 0.0f, false);
                        this.inCounter = new StaticLayout(spannableStringBuilder2, FilterTabsView.this.textCounterPaint, ceil, alignment, 1.0f, 0.0f, false);
                    } else {
                        int ceil2 = (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(valueOf));
                        TextPaint textPaint2 = FilterTabsView.this.textCounterPaint;
                        Layout.Alignment alignment2 = Layout.Alignment.ALIGN_CENTER;
                        this.outCounter = new StaticLayout(valueOf, textPaint2, ceil2, alignment2, 1.0f, 0.0f, false);
                        this.inCounter = new StaticLayout(valueOf2, FilterTabsView.this.textCounterPaint, (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(valueOf2)), alignment2, 1.0f, 0.0f, false);
                    }
                }
                z = true;
            } else {
                z = false;
            }
            int i6 = this.currentTab.counter;
            if (i6 > 0) {
                str = String.format("%d", Integer.valueOf(i6));
                i = Math.max(AndroidUtilities.dp(10.0f), (int) Math.ceil(FilterTabsView.this.textCounterPaint.measureText(str))) + AndroidUtilities.dp(10.0f);
            } else {
                str = null;
                i = 0;
            }
            int dp = this.currentTab.titleWidth + (i != 0 ? i + AndroidUtilities.dp((str != null ? 1.0f : FilterTabsView.this.editingStartAnimationProgress) * 6.0f) : 0);
            float measuredWidth = (getMeasuredWidth() - dp) / 2;
            float f = this.lastTextX;
            if (measuredWidth != f) {
                this.animateTextX = true;
                this.animateFromTextX = f;
                z = true;
            }
            String str4 = this.lastTitle;
            if (str4 != null && !this.currentTab.title.equals(str4)) {
                if (this.lastTitle.length() > this.currentTab.title.length()) {
                    str2 = this.lastTitle;
                    str3 = this.currentTab.title;
                    z2 = true;
                } else {
                    str2 = this.currentTab.title;
                    str3 = this.lastTitle;
                    z2 = false;
                }
                int indexOf = str2.indexOf(str3);
                if (indexOf >= 0) {
                    CharSequence replaceEmoji = Emoji.replaceEmoji((CharSequence) str2, FilterTabsView.this.textPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false);
                    SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder(replaceEmoji);
                    SpannableStringBuilder spannableStringBuilder5 = new SpannableStringBuilder(replaceEmoji);
                    if (indexOf != 0) {
                        spannableStringBuilder5.setSpan(new EmptyStubSpan(), 0, indexOf, 0);
                    }
                    if (str3.length() + indexOf != str2.length()) {
                        spannableStringBuilder5.setSpan(new EmptyStubSpan(), str3.length() + indexOf, str2.length(), 0);
                    }
                    spannableStringBuilder4.setSpan(new EmptyStubSpan(), indexOf, str3.length() + indexOf, 0);
                    TextPaint textPaint3 = FilterTabsView.this.textPaint;
                    int dp2 = AndroidUtilities.dp(400.0f);
                    Layout.Alignment alignment3 = Layout.Alignment.ALIGN_NORMAL;
                    this.titleAnimateInLayout = new StaticLayout(spannableStringBuilder4, textPaint3, dp2, alignment3, 1.0f, 0.0f, false);
                    StaticLayout staticLayout = new StaticLayout(spannableStringBuilder5, FilterTabsView.this.textPaint, AndroidUtilities.dp(400.0f), alignment3, 1.0f, 0.0f, false);
                    this.titleAnimateStableLayout = staticLayout;
                    this.animateTextChange = true;
                    this.animateTextChangeOut = z2;
                    this.titleXOffset = indexOf != 0 ? -staticLayout.getPrimaryHorizontal(indexOf) : 0.0f;
                    this.animateFromTitleWidth = this.lastTitleWidth;
                    this.titleAnimateOutLayout = null;
                } else {
                    String str5 = this.currentTab.title;
                    TextPaint textPaint4 = FilterTabsView.this.textPaint;
                    int dp3 = AndroidUtilities.dp(400.0f);
                    Layout.Alignment alignment4 = Layout.Alignment.ALIGN_NORMAL;
                    this.titleAnimateInLayout = new StaticLayout(str5, textPaint4, dp3, alignment4, 1.0f, 0.0f, false);
                    this.titleAnimateOutLayout = new StaticLayout(this.lastTitle, FilterTabsView.this.textPaint, AndroidUtilities.dp(400.0f), alignment4, 1.0f, 0.0f, false);
                    this.titleAnimateStableLayout = null;
                    this.animateTextChange = true;
                    this.titleXOffset = 0.0f;
                    this.animateFromTitleWidth = this.lastTitleWidth;
                }
                z = true;
            }
            if (dp == this.lastTabWidth && getMeasuredWidth() == this.lastWidth) {
                return z;
            }
            this.animateTabWidth = true;
            this.animateFromTabWidth = this.lastTabWidth;
            this.animateFromWidth = this.lastWidth;
            return true;
        }

        public void clearTransitionParams() {
            this.animateChange = false;
            this.animateTabCounter = false;
            this.animateCounterChange = false;
            this.animateTextChange = false;
            this.animateTextX = false;
            this.animateTabWidth = false;
            this.changeAnimator = null;
            invalidate();
        }

        @Override // android.view.View
        public int getId() {
            return this.currentTab.id;
        }

        @Override // android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.animateChange = false;
            this.animateTabCounter = false;
            this.animateCounterChange = false;
            this.animateTextChange = false;
            this.animateTextX = false;
            this.animateTabWidth = false;
            ValueAnimator valueAnimator = this.changeAnimator;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.changeAnimator.removeAllUpdateListeners();
                this.changeAnimator.cancel();
                this.changeAnimator = null;
            }
            invalidate();
        }

        /* JADX WARN: Removed duplicated region for block: B:118:0x0818  */
        /* JADX WARN: Removed duplicated region for block: B:121:0x0834  */
        /* JADX WARN: Removed duplicated region for block: B:124:0x0892  */
        /* JADX WARN: Removed duplicated region for block: B:126:0x08c5  */
        /* JADX WARN: Removed duplicated region for block: B:145:0x04c7  */
        /* JADX WARN: Removed duplicated region for block: B:159:0x053a  */
        /* JADX WARN: Removed duplicated region for block: B:163:0x0554  */
        /* JADX WARN: Removed duplicated region for block: B:166:0x0575 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:169:0x05a5  */
        /* JADX WARN: Removed duplicated region for block: B:174:0x05ed  */
        /* JADX WARN: Removed duplicated region for block: B:177:0x05fe  */
        /* JADX WARN: Removed duplicated region for block: B:180:0x063e  */
        /* JADX WARN: Removed duplicated region for block: B:183:0x067f  */
        /* JADX WARN: Removed duplicated region for block: B:186:0x06ec A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:188:0x06f3  */
        /* JADX WARN: Removed duplicated region for block: B:194:0x05f8  */
        /* JADX WARN: Removed duplicated region for block: B:201:0x06b1  */
        /* JADX WARN: Removed duplicated region for block: B:207:0x057f  */
        /* JADX WARN: Removed duplicated region for block: B:210:0x054f  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onDraw(Canvas canvas) {
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            int i8;
            int i9;
            String format;
            float ceil;
            int max;
            int i10;
            float f;
            boolean z;
            int i11;
            float f2;
            Paint paint;
            int color;
            boolean z2;
            int i12;
            float f3;
            int i13;
            int dp;
            int lineBottom;
            StaticLayout staticLayout;
            int i14;
            int color2;
            int i15;
            TextPaint textPaint;
            int blendARGB;
            boolean z3 = !this.currentTab.isDefault;
            if (FilterTabsView.this.editingAnimationProgress != 0.0f) {
                canvas.save();
                float f4 = FilterTabsView.this.editingAnimationProgress;
                int i16 = this.currentPosition % 2;
                double d = (f4 * (i16 == 0 ? 1.0f : -1.0f)) + i16;
                Double.isNaN(d);
                float sin = (float) Math.sin(d * 3.141592653589793d * 2.5d);
                double elapsedRealtime = SystemClock.elapsedRealtime() / 400.0f;
                Double.isNaN(elapsedRealtime);
                double d2 = elapsedRealtime * 3.141592653589793d;
                double d3 = this.currentPosition % 2 == 0 ? 1.0f : -1.0f;
                Double.isNaN(d3);
                double d4 = (float) (d2 * d3);
                double cos = Math.cos(d4);
                double dp2 = AndroidUtilities.dp(0.33f);
                Double.isNaN(dp2);
                double d5 = cos * dp2;
                double d6 = this.currentPosition % 2 == 0 ? 1.0f : -1.0f;
                Double.isNaN(d6);
                double sin2 = Math.sin(d4);
                double d7 = -AndroidUtilities.dp(0.33f);
                Double.isNaN(d7);
                canvas.translate((float) (d5 * d6), (float) (sin2 * d7));
                canvas.rotate(sin * 1.4f, getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f);
            }
            if (FilterTabsView.this.manualScrollingToId != -1) {
                i = FilterTabsView.this.manualScrollingToId;
                i2 = FilterTabsView.this.selectedTabId;
            } else {
                i = FilterTabsView.this.selectedTabId;
                i2 = FilterTabsView.this.previousId;
            }
            if (this.currentTab.id == i) {
                i3 = FilterTabsView.this.activeTextColorKey;
                i4 = FilterTabsView.this.aActiveTextColorKey;
                i5 = FilterTabsView.this.unactiveTextColorKey;
                i6 = FilterTabsView.this.aUnactiveTextColorKey;
                i7 = Theme.key_chats_tabUnreadActiveBackground;
                i8 = Theme.key_chats_tabUnreadUnactiveBackground;
            } else {
                i3 = FilterTabsView.this.unactiveTextColorKey;
                i4 = FilterTabsView.this.aUnactiveTextColorKey;
                i5 = FilterTabsView.this.activeTextColorKey;
                i6 = FilterTabsView.this.aUnactiveTextColorKey;
                i7 = Theme.key_chats_tabUnreadUnactiveBackground;
                i8 = Theme.key_chats_tabUnreadActiveBackground;
            }
            if (i4 < 0) {
                if ((FilterTabsView.this.animatingIndicator || FilterTabsView.this.manualScrollingToId != -1) && ((i15 = this.currentTab.id) == i || i15 == i2)) {
                    textPaint = FilterTabsView.this.textPaint;
                    blendARGB = ColorUtils.blendARGB(Theme.getColor(i5), Theme.getColor(i3), FilterTabsView.this.animatingIndicatorProgress);
                } else {
                    textPaint = FilterTabsView.this.textPaint;
                    blendARGB = Theme.getColor(i3);
                }
                textPaint.setColor(blendARGB);
            } else {
                int color3 = Theme.getColor(i3);
                int color4 = Theme.getColor(i4);
                if ((FilterTabsView.this.animatingIndicator || FilterTabsView.this.manualScrollingToPosition != -1) && ((i9 = this.currentTab.id) == i || i9 == i2)) {
                    FilterTabsView.this.textPaint.setColor(ColorUtils.blendARGB(ColorUtils.blendARGB(Theme.getColor(i5), Theme.getColor(i6), FilterTabsView.this.animationValue), ColorUtils.blendARGB(color3, color4, FilterTabsView.this.animationValue), FilterTabsView.this.animatingIndicatorProgress));
                } else {
                    FilterTabsView.this.textPaint.setColor(ColorUtils.blendARGB(color3, color4, FilterTabsView.this.animationValue));
                }
            }
            int i17 = this.animateFromTabCount;
            boolean z4 = i17 == 0 && this.animateTabCounter;
            boolean z5 = i17 > 0 && this.currentTab.counter == 0 && this.animateTabCounter;
            boolean z6 = i17 > 0 && this.currentTab.counter > 0 && this.animateTabCounter;
            int i18 = this.currentTab.counter;
            if (i18 > 0 || z5) {
                format = z5 ? String.format("%d", Integer.valueOf(i17)) : String.format("%d", Integer.valueOf(i18));
                ceil = (int) Math.ceil(FilterTabsView.this.textCounterPaint.measureText(format));
                max = (int) (Math.max(AndroidUtilities.dp(10.0f), ceil) + AndroidUtilities.dp(10.0f));
            } else {
                format = null;
                ceil = 0.0f;
                max = 0;
            }
            if (z3 && (FilterTabsView.this.isEditing || FilterTabsView.this.editingStartAnimationProgress != 0.0f)) {
                max = (int) (max + ((AndroidUtilities.dp(20.0f) - max) * FilterTabsView.this.editingStartAnimationProgress));
            }
            int i19 = this.currentTab.titleWidth;
            if (max == 0 || z5) {
                i10 = 0;
            } else {
                i10 = AndroidUtilities.dp((format != null ? 1.0f : FilterTabsView.this.editingStartAnimationProgress) * 6.0f) + max;
            }
            this.tabWidth = i19 + i10;
            float measuredWidth = (getMeasuredWidth() - this.tabWidth) / 2.0f;
            if (this.animateTextX) {
                float f5 = this.changeProgress;
                measuredWidth = (measuredWidth * f5) + (this.animateFromTextX * (1.0f - f5));
            }
            float f6 = measuredWidth;
            if (TextUtils.equals(this.currentTab.title, this.currentText)) {
                f = ceil;
                z = z5;
            } else {
                String str = this.currentTab.title;
                this.currentText = str;
                z = z5;
                f = ceil;
                StaticLayout staticLayout2 = new StaticLayout(Emoji.replaceEmoji((CharSequence) str, FilterTabsView.this.textPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false), FilterTabsView.this.textPaint, AndroidUtilities.dp(400.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.textLayout = staticLayout2;
                this.textHeight = staticLayout2.getHeight();
                this.textOffsetX = (int) (-this.textLayout.getLineLeft(0));
            }
            if (this.animateTextChange) {
                f2 = this.titleXOffset * (this.animateTextChangeOut ? this.changeProgress : 1.0f - this.changeProgress);
                if (this.titleAnimateStableLayout != null) {
                    canvas.save();
                    canvas.translate(this.textOffsetX + f6 + f2, ((getMeasuredHeight() - this.textHeight) / 2.0f) + 1.0f);
                    this.titleAnimateStableLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.titleAnimateInLayout != null) {
                    canvas.save();
                    int alpha = FilterTabsView.this.textPaint.getAlpha();
                    i11 = max;
                    FilterTabsView.this.textPaint.setAlpha((int) (alpha * (this.animateTextChangeOut ? 1.0f - this.changeProgress : this.changeProgress)));
                    canvas.translate(this.textOffsetX + f6 + f2, ((getMeasuredHeight() - this.textHeight) / 2.0f) + 1.0f);
                    this.titleAnimateInLayout.draw(canvas);
                    canvas.restore();
                    FilterTabsView.this.textPaint.setAlpha(alpha);
                } else {
                    i11 = max;
                }
                if (this.titleAnimateOutLayout != null) {
                    canvas.save();
                    int alpha2 = FilterTabsView.this.textPaint.getAlpha();
                    FilterTabsView.this.textPaint.setAlpha((int) (alpha2 * (this.animateTextChangeOut ? this.changeProgress : 1.0f - this.changeProgress)));
                    canvas.translate(this.textOffsetX + f6 + f2, ((getMeasuredHeight() - this.textHeight) / 2.0f) + 1.0f);
                    this.titleAnimateOutLayout.draw(canvas);
                    canvas.restore();
                    FilterTabsView.this.textPaint.setAlpha(alpha2);
                }
            } else {
                i11 = max;
                if (this.textLayout != null) {
                    canvas.save();
                    canvas.translate(this.textOffsetX + f6, ((getMeasuredHeight() - this.textHeight) / 2.0f) + 1.0f);
                    this.textLayout.draw(canvas);
                    canvas.restore();
                }
                f2 = 0.0f;
            }
            if (z4 || format != null || (z3 && (FilterTabsView.this.isEditing || FilterTabsView.this.editingStartAnimationProgress != 0.0f))) {
                if (FilterTabsView.this.aBackgroundColorKey < 0) {
                    FilterTabsView.this.textCounterPaint.setColor(Theme.getColor(FilterTabsView.this.backgroundColorKey));
                } else {
                    FilterTabsView.this.textCounterPaint.setColor(ColorUtils.blendARGB(Theme.getColor(FilterTabsView.this.backgroundColorKey), Theme.getColor(FilterTabsView.this.aBackgroundColorKey), FilterTabsView.this.animationValue));
                }
                if (Theme.hasThemeKey(i7) && Theme.hasThemeKey(i8)) {
                    color = Theme.getColor(i7);
                    if ((FilterTabsView.this.animatingIndicator || FilterTabsView.this.manualScrollingToPosition != -1) && ((i14 = this.currentTab.id) == i || i14 == i2)) {
                        FilterTabsView.this.counterPaint.setColor(ColorUtils.blendARGB(Theme.getColor(i8), color, FilterTabsView.this.animatingIndicatorProgress));
                        float f7 = this.currentTab.titleWidth;
                        z2 = this.animateTextChange;
                        if (z2) {
                            float f8 = this.animateFromTitleWidth;
                            float f9 = this.changeProgress;
                            f7 = (f7 * f9) + (f8 * (1.0f - f9));
                        }
                        float dp3 = (z2 || this.titleAnimateOutLayout != null) ? f7 + f6 + AndroidUtilities.dp(6.0f) : (f6 - this.titleXOffset) + f2 + f7 + AndroidUtilities.dp(6.0f);
                        int measuredHeight = (getMeasuredHeight() - AndroidUtilities.dp(20.0f)) / 2;
                        if (z3 || ((!FilterTabsView.this.isEditing && FilterTabsView.this.editingStartAnimationProgress == 0.0f) || format != null)) {
                            FilterTabsView.this.counterPaint.setAlpha(NotificationCenter.notificationsCountUpdated);
                        } else {
                            FilterTabsView.this.counterPaint.setAlpha((int) (FilterTabsView.this.editingStartAnimationProgress * 255.0f));
                        }
                        if (z6) {
                            i12 = i11;
                        } else {
                            float f10 = this.animateFromCountWidth;
                            i12 = i11;
                            float f11 = i12;
                            if (f10 != f11) {
                                float f12 = this.changeProgress;
                                f3 = (f10 * (1.0f - f12)) + (f11 * f12);
                                if (z6) {
                                    float f13 = this.animateFromCounterWidth;
                                    float f14 = this.changeProgress;
                                    f = (f13 * (1.0f - f14)) + (f14 * f);
                                }
                                float f15 = measuredHeight;
                                this.rect.set(dp3, f15, f3 + dp3, measuredHeight + AndroidUtilities.dp(20.0f));
                                if (!z4 || z) {
                                    canvas.save();
                                    float f16 = this.changeProgress;
                                    if (!z4) {
                                        f16 = 1.0f - f16;
                                    }
                                    canvas.scale(f16, f16, this.rect.centerX(), this.rect.centerY());
                                }
                                RectF rectF = this.rect;
                                float f17 = AndroidUtilities.density * 11.5f;
                                canvas.drawRoundRect(rectF, f17, f17, FilterTabsView.this.counterPaint);
                                if (z6) {
                                    if (this.inCounter != null) {
                                        dp = AndroidUtilities.dp(20.0f);
                                        i13 = 0;
                                        lineBottom = this.inCounter.getLineBottom(0);
                                        staticLayout = this.inCounter;
                                    } else {
                                        i13 = 0;
                                        if (this.outCounter != null) {
                                            dp = AndroidUtilities.dp(20.0f);
                                            lineBottom = this.outCounter.getLineBottom(0);
                                            staticLayout = this.outCounter;
                                        } else {
                                            if (this.stableCounter != null) {
                                                dp = AndroidUtilities.dp(20.0f);
                                                lineBottom = this.stableCounter.getLineBottom(0);
                                                staticLayout = this.stableCounter;
                                            }
                                            float f18 = !z3 ? 1.0f - FilterTabsView.this.editingStartAnimationProgress : 1.0f;
                                            if (this.inCounter != null) {
                                                canvas.save();
                                                FilterTabsView.this.textCounterPaint.setAlpha((int) (f18 * 255.0f * this.changeProgress));
                                                RectF rectF2 = this.rect;
                                                canvas.translate(rectF2.left + ((rectF2.width() - f) / 2.0f), ((1.0f - this.changeProgress) * AndroidUtilities.dp(15.0f)) + f15);
                                                this.inCounter.draw(canvas);
                                                canvas.restore();
                                            }
                                            if (this.outCounter != null) {
                                                canvas.save();
                                                FilterTabsView.this.textCounterPaint.setAlpha((int) (f18 * 255.0f * (1.0f - this.changeProgress)));
                                                RectF rectF3 = this.rect;
                                                canvas.translate(rectF3.left + ((rectF3.width() - f) / 2.0f), (this.changeProgress * (-AndroidUtilities.dp(15.0f))) + f15);
                                                this.outCounter.draw(canvas);
                                                canvas.restore();
                                            }
                                            if (this.stableCounter != null) {
                                                canvas.save();
                                                FilterTabsView.this.textCounterPaint.setAlpha((int) (f18 * 255.0f));
                                                RectF rectF4 = this.rect;
                                                canvas.translate(rectF4.left + ((rectF4.width() - f) / 2.0f), f15);
                                                this.stableCounter.draw(canvas);
                                                canvas.restore();
                                            }
                                            FilterTabsView.this.textCounterPaint.setAlpha(NotificationCenter.notificationsCountUpdated);
                                        }
                                    }
                                    f15 += (dp - (lineBottom - staticLayout.getLineTop(i13))) / 2.0f;
                                    if (!z3) {
                                    }
                                    if (this.inCounter != null) {
                                    }
                                    if (this.outCounter != null) {
                                    }
                                    if (this.stableCounter != null) {
                                    }
                                    FilterTabsView.this.textCounterPaint.setAlpha(NotificationCenter.notificationsCountUpdated);
                                } else if (format != null) {
                                    if (z3) {
                                        FilterTabsView.this.textCounterPaint.setAlpha((int) ((1.0f - FilterTabsView.this.editingStartAnimationProgress) * 255.0f));
                                    }
                                    RectF rectF5 = this.rect;
                                    canvas.drawText(format, rectF5.left + ((rectF5.width() - f) / 2.0f), measuredHeight + AndroidUtilities.dp(14.5f), FilterTabsView.this.textCounterPaint);
                                }
                                if (!z4 || z) {
                                    canvas.restore();
                                }
                                if (z3 && (FilterTabsView.this.isEditing || FilterTabsView.this.editingStartAnimationProgress != 0.0f)) {
                                    FilterTabsView.this.deletePaint.setColor(FilterTabsView.this.textCounterPaint.getColor());
                                    FilterTabsView.this.deletePaint.setAlpha((int) (FilterTabsView.this.editingStartAnimationProgress * 255.0f));
                                    float dp4 = AndroidUtilities.dp(3.0f);
                                    canvas.drawLine(this.rect.centerX() - dp4, this.rect.centerY() - dp4, this.rect.centerX() + dp4, this.rect.centerY() + dp4, FilterTabsView.this.deletePaint);
                                    canvas.drawLine(this.rect.centerX() - dp4, this.rect.centerY() + dp4, this.rect.centerX() + dp4, this.rect.centerY() - dp4, FilterTabsView.this.deletePaint);
                                }
                            }
                        }
                        f3 = i12;
                        if (z6) {
                        }
                        float f152 = measuredHeight;
                        this.rect.set(dp3, f152, f3 + dp3, measuredHeight + AndroidUtilities.dp(20.0f));
                        if (!z4) {
                        }
                        canvas.save();
                        float f162 = this.changeProgress;
                        if (!z4) {
                        }
                        canvas.scale(f162, f162, this.rect.centerX(), this.rect.centerY());
                        RectF rectF6 = this.rect;
                        float f172 = AndroidUtilities.density * 11.5f;
                        canvas.drawRoundRect(rectF6, f172, f172, FilterTabsView.this.counterPaint);
                        if (z6) {
                        }
                        if (!z4) {
                        }
                        canvas.restore();
                        if (z3) {
                            FilterTabsView.this.deletePaint.setColor(FilterTabsView.this.textCounterPaint.getColor());
                            FilterTabsView.this.deletePaint.setAlpha((int) (FilterTabsView.this.editingStartAnimationProgress * 255.0f));
                            float dp42 = AndroidUtilities.dp(3.0f);
                            canvas.drawLine(this.rect.centerX() - dp42, this.rect.centerY() - dp42, this.rect.centerX() + dp42, this.rect.centerY() + dp42, FilterTabsView.this.deletePaint);
                            canvas.drawLine(this.rect.centerX() - dp42, this.rect.centerY() + dp42, this.rect.centerX() + dp42, this.rect.centerY() - dp42, FilterTabsView.this.deletePaint);
                        }
                    } else {
                        paint = FilterTabsView.this.counterPaint;
                    }
                } else {
                    paint = FilterTabsView.this.counterPaint;
                    color = FilterTabsView.this.textPaint.getColor();
                }
                paint.setColor(color);
                float f72 = this.currentTab.titleWidth;
                z2 = this.animateTextChange;
                if (z2) {
                }
                if (z2) {
                }
                int measuredHeight2 = (getMeasuredHeight() - AndroidUtilities.dp(20.0f)) / 2;
                if (z3) {
                }
                FilterTabsView.this.counterPaint.setAlpha(NotificationCenter.notificationsCountUpdated);
                if (z6) {
                }
                f3 = i12;
                if (z6) {
                }
                float f1522 = measuredHeight2;
                this.rect.set(dp3, f1522, f3 + dp3, measuredHeight2 + AndroidUtilities.dp(20.0f));
                if (!z4) {
                }
                canvas.save();
                float f1622 = this.changeProgress;
                if (!z4) {
                }
                canvas.scale(f1622, f1622, this.rect.centerX(), this.rect.centerY());
                RectF rectF62 = this.rect;
                float f1722 = AndroidUtilities.density * 11.5f;
                canvas.drawRoundRect(rectF62, f1722, f1722, FilterTabsView.this.counterPaint);
                if (z6) {
                }
                if (!z4) {
                }
                canvas.restore();
                if (z3) {
                }
            } else {
                i12 = i11;
            }
            float f19 = f;
            if (FilterTabsView.this.editingAnimationProgress != 0.0f) {
                canvas.restore();
            }
            this.lastTextX = f6;
            Tab tab = this.currentTab;
            this.lastTabCount = tab.counter;
            this.lastTitleLayout = this.textLayout;
            this.lastTitle = this.currentText;
            this.lastTitleWidth = tab.titleWidth;
            this.lastCountWidth = i12;
            this.lastCounterWidth = f19;
            this.lastTabWidth = this.tabWidth;
            this.lastWidth = getMeasuredWidth();
            if (!this.currentTab.isLocked && this.progressToLocked == 0.0f) {
                return;
            }
            if (FilterTabsView.this.lockDrawable == null) {
                FilterTabsView.this.lockDrawable = ContextCompat.getDrawable(getContext(), R.drawable.other_lockedfolders);
            }
            boolean z7 = this.currentTab.isLocked;
            if (z7) {
                float f20 = this.progressToLocked;
                if (f20 != 1.0f) {
                    this.progressToLocked = f20 + 0.10666667f;
                    this.progressToLocked = Utilities.clamp(this.progressToLocked, 1.0f, 0.0f);
                    color2 = Theme.getColor(FilterTabsView.this.unactiveTextColorKey);
                    if (FilterTabsView.this.aUnactiveTextColorKey >= 0) {
                        color2 = ColorUtils.blendARGB(color2, Theme.getColor(FilterTabsView.this.aUnactiveTextColorKey), FilterTabsView.this.animationValue);
                    }
                    if (FilterTabsView.this.lockDrawableColor != color2) {
                        FilterTabsView.this.lockDrawableColor = color2;
                        FilterTabsView.this.lockDrawable.setColorFilter(new PorterDuffColorFilter(color2, PorterDuff.Mode.MULTIPLY));
                    }
                    int measuredWidth2 = (int) (((getMeasuredWidth() - FilterTabsView.this.lockDrawable.getIntrinsicWidth()) / 2.0f) + this.locIconXOffset);
                    int measuredHeight3 = getMeasuredHeight() - AndroidUtilities.dp(12.0f);
                    FilterTabsView.this.lockDrawable.setBounds(measuredWidth2, measuredHeight3, FilterTabsView.this.lockDrawable.getIntrinsicWidth() + measuredWidth2, FilterTabsView.this.lockDrawable.getIntrinsicHeight() + measuredHeight3);
                    if (this.progressToLocked != 1.0f) {
                        FilterTabsView.this.lockDrawable.draw(canvas);
                        return;
                    }
                    canvas.save();
                    float f21 = this.progressToLocked;
                    canvas.scale(f21, f21, FilterTabsView.this.lockDrawable.getBounds().centerX(), FilterTabsView.this.lockDrawable.getBounds().centerY());
                    FilterTabsView.this.lockDrawable.draw(canvas);
                    canvas.restore();
                    return;
                }
            }
            if (!z7) {
                this.progressToLocked -= 0.10666667f;
            }
            this.progressToLocked = Utilities.clamp(this.progressToLocked, 1.0f, 0.0f);
            color2 = Theme.getColor(FilterTabsView.this.unactiveTextColorKey);
            if (FilterTabsView.this.aUnactiveTextColorKey >= 0) {
            }
            if (FilterTabsView.this.lockDrawableColor != color2) {
            }
            int measuredWidth22 = (int) (((getMeasuredWidth() - FilterTabsView.this.lockDrawable.getIntrinsicWidth()) / 2.0f) + this.locIconXOffset);
            int measuredHeight32 = getMeasuredHeight() - AndroidUtilities.dp(12.0f);
            FilterTabsView.this.lockDrawable.setBounds(measuredWidth22, measuredHeight32, FilterTabsView.this.lockDrawable.getIntrinsicWidth() + measuredWidth22, FilterTabsView.this.lockDrawable.getIntrinsicHeight() + measuredHeight32);
            if (this.progressToLocked != 1.0f) {
            }
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setSelected((this.currentTab == null || FilterTabsView.this.selectedTabId == -1 || this.currentTab.id != FilterTabsView.this.selectedTabId) ? false : true);
            accessibilityNodeInfo.addAction(16);
            if (Build.VERSION.SDK_INT >= 21) {
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(32, LocaleController.getString(R.string.AccDescrOpenMenu2)));
            } else {
                accessibilityNodeInfo.addAction(32);
            }
            if (this.currentTab != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(this.currentTab.title);
                Tab tab = this.currentTab;
                int i = tab != null ? tab.counter : 0;
                if (i > 0) {
                    sb.append("\n");
                    sb.append(LocaleController.formatPluralString("AccDescrUnreadCount", i, new Object[0]));
                }
                accessibilityNodeInfo.setContentDescription(sb);
            }
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            setMeasuredDimension(this.currentTab.getWidth(false) + AndroidUtilities.dp(32.0f) + FilterTabsView.this.additionalTabWidth, View.MeasureSpec.getSize(i2));
        }

        public void setTab(Tab tab, int i) {
            this.currentTab = tab;
            this.currentPosition = i;
            setContentDescription(tab.title);
            requestLayout();
        }

        public void shakeLockIcon(final float f, final int i) {
            if (i == 6) {
                this.locIconXOffset = 0.0f;
                return;
            }
            AnimatorSet animatorSet = new AnimatorSet();
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, AndroidUtilities.dp(f));
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.FilterTabsView$TabView$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    FilterTabsView.TabView.this.lambda$shakeLockIcon$0(valueAnimator);
                }
            });
            animatorSet.playTogether(ofFloat);
            animatorSet.setDuration(50L);
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.FilterTabsView.TabView.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    TabView tabView = TabView.this;
                    int i2 = i;
                    tabView.shakeLockIcon(i2 == 5 ? 0.0f : -f, i2 + 1);
                    TabView.this.locIconXOffset = 0.0f;
                    TabView.this.invalidate();
                }
            });
            animatorSet.start();
        }
    }

    public class TouchHelperCallback extends ItemTouchHelper.Callback {
        private Runnable resetDefaultPosition = new Runnable() { // from class: org.telegram.ui.Components.FilterTabsView$TouchHelperCallback$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FilterTabsView.TouchHelperCallback.this.lambda$new$0();
            }
        };

        public TouchHelperCallback() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            if (UserConfig.getInstance(UserConfig.selectedAccount).isPremium()) {
                return;
            }
            for (int i = 0; i < FilterTabsView.this.tabs.size(); i++) {
                if (((Tab) FilterTabsView.this.tabs.get(i)).isDefault && i != 0) {
                    FilterTabsView.this.adapter.moveElementToStart(i);
                    FilterTabsView.this.listView.scrollToPosition(0);
                    FilterTabsView.this.onDefaultTabMoved();
                    return;
                }
            }
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
            viewHolder.itemView.setBackground(null);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return (!MessagesController.getInstance(UserConfig.selectedAccount).premiumFeaturesBlocked() || (FilterTabsView.this.isEditing && !(viewHolder.getAdapterPosition() == 0 && ((Tab) FilterTabsView.this.tabs.get(0)).isDefault && !UserConfig.getInstance(UserConfig.selectedAccount).isPremium()))) ? ItemTouchHelper.Callback.makeMovementFlags(12, 0) : ItemTouchHelper.Callback.makeMovementFlags(0, 0);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean isLongPressDragEnabled() {
            return FilterTabsView.this.isEditing;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
            if (MessagesController.getInstance(UserConfig.selectedAccount).premiumFeaturesBlocked() && ((viewHolder.getAdapterPosition() == 0 || viewHolder2.getAdapterPosition() == 0) && !UserConfig.getInstance(UserConfig.selectedAccount).isPremium())) {
                return false;
            }
            FilterTabsView.this.adapter.swapElements(viewHolder.getAdapterPosition(), viewHolder2.getAdapterPosition());
            return true;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
            if (i != 0) {
                FilterTabsView.this.listView.cancelClickRunnables(false);
                viewHolder.itemView.setPressed(true);
                viewHolder.itemView.setBackgroundColor(Theme.getColor(FilterTabsView.this.backgroundColorKey));
            } else {
                AndroidUtilities.cancelRunOnUIThread(this.resetDefaultPosition);
                AndroidUtilities.runOnUIThread(this.resetDefaultPosition, 320L);
            }
            super.onSelectedChanged(viewHolder, i);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public FilterTabsView(Context context) {
        super(context);
        this.textPaint = new TextPaint(1);
        this.textCounterPaint = new TextPaint(1);
        this.deletePaint = new TextPaint(1);
        this.counterPaint = new Paint(1);
        this.tabs = new ArrayList();
        this.selectedTabId = -1;
        this.manualScrollingToPosition = -1;
        this.manualScrollingToId = -1;
        this.scrollingToChild = -1;
        this.tabLineColorKey = Theme.key_actionBarTabLine;
        this.activeTextColorKey = Theme.key_actionBarTabActiveText;
        this.unactiveTextColorKey = Theme.key_actionBarTabUnactiveText;
        this.selectorColorKey = Theme.key_actionBarTabSelector;
        this.backgroundColorKey = Theme.key_actionBarDefault;
        this.aTabLineColorKey = -1;
        this.aActiveTextColorKey = -1;
        this.aUnactiveTextColorKey = -1;
        this.aBackgroundColorKey = -1;
        this.interpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.positionToId = new SparseIntArray(5);
        this.positionToStableId = new SparseIntArray(5);
        this.idToPosition = new SparseIntArray(5);
        this.positionToWidth = new SparseIntArray(5);
        this.positionToX = new SparseIntArray(5);
        this.animationRunnable = new Runnable() { // from class: org.telegram.ui.Components.FilterTabsView.1
            @Override // java.lang.Runnable
            public void run() {
                if (FilterTabsView.this.animatingIndicator) {
                    long elapsedRealtime = SystemClock.elapsedRealtime() - FilterTabsView.this.lastAnimationTime;
                    if (elapsedRealtime > 17) {
                        elapsedRealtime = 17;
                    }
                    FilterTabsView.access$2616(FilterTabsView.this, elapsedRealtime / 200.0f);
                    FilterTabsView filterTabsView = FilterTabsView.this;
                    filterTabsView.setAnimationIdicatorProgress(filterTabsView.interpolator.getInterpolation(FilterTabsView.this.animationTime));
                    if (FilterTabsView.this.animationTime > 1.0f) {
                        FilterTabsView.this.animationTime = 1.0f;
                    }
                    if (FilterTabsView.this.animationTime < 1.0f) {
                        AndroidUtilities.runOnUIThread(FilterTabsView.this.animationRunnable);
                        return;
                    }
                    FilterTabsView.this.animatingIndicator = false;
                    FilterTabsView.this.setEnabled(true);
                    if (FilterTabsView.this.delegate != null) {
                        FilterTabsView.this.delegate.onPageScrolled(1.0f);
                    }
                }
            }
        };
        this.COLORS = new AnimationProperties.FloatProperty("animationValue") { // from class: org.telegram.ui.Components.FilterTabsView.2
            @Override // android.util.Property
            public Float get(FilterTabsView filterTabsView) {
                return Float.valueOf(FilterTabsView.this.animationValue);
            }

            @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
            public void setValue(FilterTabsView filterTabsView, float f) {
                FilterTabsView.this.animationValue = f;
                FilterTabsView.this.selectorDrawable.setColor(ColorUtils.blendARGB(Theme.getColor(FilterTabsView.this.tabLineColorKey), Theme.getColor(FilterTabsView.this.aTabLineColorKey), f));
                FilterTabsView.this.listView.invalidateViews();
                FilterTabsView.this.listView.invalidate();
                filterTabsView.invalidate();
            }
        };
        this.textCounterPaint.setTextSize(AndroidUtilities.dp(13.0f));
        this.textCounterPaint.setTypeface(AndroidUtilities.bold());
        this.textPaint.setTextSize(AndroidUtilities.dp(15.0f));
        this.textPaint.setTypeface(AndroidUtilities.bold());
        this.deletePaint.setStyle(Paint.Style.STROKE);
        this.deletePaint.setStrokeCap(Paint.Cap.ROUND);
        this.deletePaint.setStrokeWidth(AndroidUtilities.dp(1.5f));
        this.selectorDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, null);
        float dpf2 = AndroidUtilities.dpf2(3.0f);
        this.selectorDrawable.setCornerRadii(new float[]{dpf2, dpf2, dpf2, dpf2, 0.0f, 0.0f, 0.0f, 0.0f});
        this.selectorDrawable.setColor(Theme.getColor(this.tabLineColorKey));
        setHorizontalScrollBarEnabled(false);
        RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.Components.FilterTabsView.3
            @Override // org.telegram.ui.Components.RecyclerListView
            protected boolean allowSelectChildAtPosition(View view) {
                return FilterTabsView.this.isEnabled() && FilterTabsView.this.delegate.canPerformActions();
            }

            @Override // org.telegram.ui.Components.RecyclerListView
            protected boolean canHighlightChildAt(View view, float f, float f2) {
                if (FilterTabsView.this.isEditing) {
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
                FilterTabsView.this.invalidate();
            }
        };
        this.listView = recyclerListView;
        recyclerListView.setClipChildren(false);
        4 r2 = new 4();
        this.itemAnimator = r2;
        r2.setDelayAnimations(false);
        this.listView.setItemAnimator(this.itemAnimator);
        this.listView.setSelectorType(8);
        this.listView.setSelectorRadius(6);
        this.listView.setSelectorDrawableColor(Theme.getColor(this.selectorColorKey));
        RecyclerListView recyclerListView2 = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 0, 0 == true ? 1 : 0) { // from class: org.telegram.ui.Components.FilterTabsView.5
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public int scrollHorizontallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
                if (FilterTabsView.this.delegate.isTabMenuVisible()) {
                    i = 0;
                }
                return super.scrollHorizontallyBy(i, recycler, state);
            }

            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
                LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) { // from class: org.telegram.ui.Components.FilterTabsView.5.1
                    @Override // androidx.recyclerview.widget.LinearSmoothScroller, androidx.recyclerview.widget.RecyclerView.SmoothScroller
                    protected void onTargetFound(View view, RecyclerView.State state2, RecyclerView.SmoothScroller.Action action) {
                        int calculateDxToMakeVisible = calculateDxToMakeVisible(view, getHorizontalSnapPreference());
                        if (calculateDxToMakeVisible > 0 || (calculateDxToMakeVisible == 0 && view.getLeft() - AndroidUtilities.dp(21.0f) < 0)) {
                            calculateDxToMakeVisible += AndroidUtilities.dp(60.0f);
                        } else if (calculateDxToMakeVisible < 0 || (calculateDxToMakeVisible == 0 && view.getRight() + AndroidUtilities.dp(21.0f) > FilterTabsView.this.getMeasuredWidth())) {
                            calculateDxToMakeVisible -= AndroidUtilities.dp(60.0f);
                        }
                        int calculateDyToMakeVisible = calculateDyToMakeVisible(view, getVerticalSnapPreference());
                        int max = Math.max(NotificationCenter.updateBotMenuButton, calculateTimeForDeceleration((int) Math.sqrt((calculateDxToMakeVisible * calculateDxToMakeVisible) + (calculateDyToMakeVisible * calculateDyToMakeVisible))));
                        if (max > 0) {
                            action.update(-calculateDxToMakeVisible, -calculateDyToMakeVisible, max, this.mDecelerateInterpolator);
                        }
                    }
                };
                linearSmoothScroller.setTargetPosition(i);
                startSmoothScroll(linearSmoothScroller);
            }

            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        };
        this.layoutManager = linearLayoutManager;
        recyclerListView2.setLayoutManager(linearLayoutManager);
        new ItemTouchHelper(new TouchHelperCallback()).attachToRecyclerView(this.listView);
        this.listView.setPadding(AndroidUtilities.dp(7.0f), 0, AndroidUtilities.dp(7.0f), 0);
        this.listView.setClipToPadding(false);
        this.listView.setDrawSelectorBehind(true);
        ListAdapter listAdapter = new ListAdapter(context);
        this.adapter = listAdapter;
        listAdapter.setHasStableIds(true);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.Components.FilterTabsView$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i, float f, float f2) {
                FilterTabsView.this.lambda$new$0(view, i, f, f2);
            }
        });
        this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.Components.FilterTabsView$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view, int i) {
                boolean lambda$new$1;
                lambda$new$1 = FilterTabsView.this.lambda$new$1(view, i);
                return lambda$new$1;
            }
        });
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.FilterTabsView.6
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                FilterTabsView.this.invalidate();
            }
        });
        addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
    }

    static /* synthetic */ float access$2616(FilterTabsView filterTabsView, float f) {
        float f2 = filterTabsView.animationTime + f;
        filterTabsView.animationTime = f2;
        return f2;
    }

    private Tab findDefaultTab() {
        for (int i = 0; i < this.tabs.size(); i++) {
            if (((Tab) this.tabs.get(i)).isDefault) {
                return (Tab) this.tabs.get(i);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view, int i, float f, float f2) {
        FilterTabsViewDelegate filterTabsViewDelegate;
        if (this.delegate.canPerformActions()) {
            TabView tabView = (TabView) view;
            if (!this.isEditing) {
                if (i != this.currentPosition || (filterTabsViewDelegate = this.delegate) == null) {
                    scrollToTab(tabView.currentTab, i);
                    return;
                } else {
                    filterTabsViewDelegate.onSamePageSelected();
                    return;
                }
            }
            if (i != 0) {
                float dp = AndroidUtilities.dp(6.0f);
                if (tabView.rect.left - dp >= f || tabView.rect.right + dp <= f) {
                    return;
                }
                this.delegate.onDeletePressed(tabView.currentTab.id);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$1(View view, int i) {
        if (this.delegate.canPerformActions() && !this.isEditing) {
            if (this.delegate.didSelectTab((TabView) view, i == this.currentPosition)) {
                this.listView.hideSelector(true);
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setIsEditing$2(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    private void scrollToChild(int i) {
        if (this.tabs.isEmpty() || this.scrollingToChild == i || i < 0 || i >= this.tabs.size()) {
            return;
        }
        this.scrollingToChild = i;
        this.listView.smoothScrollToPosition(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTabsWidths() {
        this.positionToX.clear();
        this.positionToWidth.clear();
        int dp = AndroidUtilities.dp(7.0f);
        int size = this.tabs.size();
        for (int i = 0; i < size; i++) {
            int width = ((Tab) this.tabs.get(i)).getWidth(false);
            this.positionToWidth.put(i, width);
            this.positionToX.put(i, (this.additionalTabWidth / 2) + dp);
            dp += width + AndroidUtilities.dp(32.0f) + this.additionalTabWidth;
        }
    }

    public void addTab(int i, int i2, String str, boolean z, boolean z2) {
        int size = this.tabs.size();
        if (size == 0 && this.selectedTabId == -1) {
            this.selectedTabId = i;
        }
        this.positionToId.put(size, i);
        this.positionToStableId.put(size, i2);
        this.idToPosition.put(i, size);
        int i3 = this.selectedTabId;
        if (i3 != -1 && i3 == i) {
            this.currentPosition = size;
        }
        Tab tab = new Tab(i, str);
        tab.isDefault = z;
        tab.isLocked = z2;
        this.allTabsWidth += tab.getWidth(true) + AndroidUtilities.dp(32.0f);
        this.tabs.add(tab);
    }

    public void animateColorsTo(int i, int i2, int i3, int i4, int i5) {
        AnimatorSet animatorSet = this.colorChangeAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        this.aTabLineColorKey = i;
        this.aActiveTextColorKey = i2;
        this.aUnactiveTextColorKey = i3;
        this.aBackgroundColorKey = i5;
        this.selectorColorKey = i4;
        this.listView.setSelectorDrawableColor(Theme.getColor(i4));
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.colorChangeAnimator = animatorSet2;
        animatorSet2.playTogether(ObjectAnimator.ofFloat(this, (Property<FilterTabsView, Float>) this.COLORS, 0.0f, 1.0f));
        this.colorChangeAnimator.setDuration(200L);
        this.colorChangeAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.FilterTabsView.7
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                FilterTabsView filterTabsView = FilterTabsView.this;
                filterTabsView.tabLineColorKey = filterTabsView.aTabLineColorKey;
                FilterTabsView filterTabsView2 = FilterTabsView.this;
                filterTabsView2.backgroundColorKey = filterTabsView2.aBackgroundColorKey;
                FilterTabsView filterTabsView3 = FilterTabsView.this;
                filterTabsView3.activeTextColorKey = filterTabsView3.aActiveTextColorKey;
                FilterTabsView filterTabsView4 = FilterTabsView.this;
                filterTabsView4.unactiveTextColorKey = filterTabsView4.aUnactiveTextColorKey;
                FilterTabsView.this.aTabLineColorKey = -1;
                FilterTabsView.this.aActiveTextColorKey = -1;
                FilterTabsView.this.aUnactiveTextColorKey = -1;
                FilterTabsView.this.aBackgroundColorKey = -1;
            }
        });
        this.colorChangeAnimator.start();
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void checkTabsCounter() {
        int size = this.tabs.size();
        boolean z = false;
        for (int i = 0; i < size; i++) {
            Tab tab = (Tab) this.tabs.get(i);
            if (tab.counter != this.delegate.getTabCounter(tab.id) && this.delegate.getTabCounter(tab.id) >= 0) {
                if (this.positionToWidth.get(i) != tab.getWidth(true) || this.invalidated) {
                    this.invalidated = true;
                    requestLayout();
                    this.allTabsWidth = 0;
                    findDefaultTab().setTitle(LocaleController.getString(R.string.FilterAllChats));
                    for (int i2 = 0; i2 < size; i2++) {
                        this.allTabsWidth += ((Tab) this.tabs.get(i2)).getWidth(true) + AndroidUtilities.dp(32.0f);
                    }
                    z = true;
                    if (z) {
                        return;
                    }
                    this.listView.setItemAnimator(this.itemAnimator);
                    this.adapter.notifyDataSetChanged();
                    return;
                }
                z = true;
            }
        }
        if (z) {
        }
    }

    public boolean currentTabIsDefault() {
        Tab findDefaultTab = findDefaultTab();
        return findDefaultTab != null && findDefaultTab.id == this.selectedTabId;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0108  */
    @Override // android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected boolean drawChild(Canvas canvas, View view, long j) {
        RecyclerView.ViewHolder findViewHolderForAdapterPosition;
        int i;
        int i2;
        float dp;
        float f;
        boolean drawChild = super.drawChild(canvas, view, j);
        if (view == this.listView) {
            int measuredHeight = getMeasuredHeight();
            this.selectorDrawable.setAlpha((int) (this.listView.getAlpha() * 255.0f));
            if (this.animatingIndicator || this.manualScrollingToPosition != -1) {
                int findFirstVisibleItemPosition = this.layoutManager.findFirstVisibleItemPosition();
                if (findFirstVisibleItemPosition != -1 && (findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(findFirstVisibleItemPosition)) != null) {
                    if (this.animatingIndicator) {
                        i = this.previousPosition;
                        i2 = this.currentPosition;
                    } else {
                        i = this.currentPosition;
                        i2 = this.manualScrollingToPosition;
                    }
                    int i3 = this.positionToX.get(i);
                    int i4 = this.positionToX.get(i2);
                    int i5 = this.positionToWidth.get(i);
                    int i6 = this.positionToWidth.get(i2);
                    dp = this.additionalTabWidth != 0 ? ((int) (i3 + ((i4 - i3) * this.animatingIndicatorProgress))) + AndroidUtilities.dp(16.0f) : (((int) (i3 + ((i4 - i3) * this.animatingIndicatorProgress))) - (this.positionToX.get(findFirstVisibleItemPosition) - findViewHolderForAdapterPosition.itemView.getLeft())) + AndroidUtilities.dp(16.0f);
                    f = (int) (i5 + ((i6 - i5) * this.animatingIndicatorProgress));
                    if (f != 0.0f) {
                        canvas.save();
                        canvas.translate(this.listView.getTranslationX(), 0.0f);
                        canvas.scale(this.listView.getScaleX(), 1.0f, this.listView.getPivotX() + this.listView.getX(), this.listView.getPivotY());
                        this.selectorDrawable.setBounds((int) dp, measuredHeight - AndroidUtilities.dpr(4.0f), (int) (dp + f), measuredHeight);
                        this.selectorDrawable.draw(canvas);
                        canvas.restore();
                    }
                }
                dp = 0.0f;
                f = 0.0f;
                if (f != 0.0f) {
                }
            } else {
                RecyclerView.ViewHolder findViewHolderForAdapterPosition2 = this.listView.findViewHolderForAdapterPosition(this.currentPosition);
                if (findViewHolderForAdapterPosition2 != null) {
                    TabView tabView = (TabView) findViewHolderForAdapterPosition2.itemView;
                    f = Math.max(AndroidUtilities.dp(40.0f), tabView.animateTabWidth ? (tabView.animateFromTabWidth * (1.0f - tabView.changeProgress)) + (tabView.tabWidth * tabView.changeProgress) : tabView.tabWidth);
                    dp = (int) (tabView.getX() + (((tabView.animateTabWidth ? (tabView.animateFromWidth * (1.0f - tabView.changeProgress)) + (tabView.getMeasuredWidth() * tabView.changeProgress) : tabView.getMeasuredWidth()) - f) / 2.0f));
                    if (f != 0.0f) {
                    }
                }
                dp = 0.0f;
                f = 0.0f;
                if (f != 0.0f) {
                }
            }
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long min = Math.min(17L, elapsedRealtime - this.lastEditingAnimationTime);
        this.lastEditingAnimationTime = elapsedRealtime;
        boolean z = this.isEditing;
        boolean z2 = true;
        if (z || this.editingAnimationProgress != 0.0f) {
            if (this.editingForwardAnimation) {
                float f2 = this.editingAnimationProgress;
                boolean z3 = f2 <= 0.0f;
                float f3 = f2 + (min / 420.0f);
                this.editingAnimationProgress = f3;
                if (!z && z3 && f3 >= 0.0f) {
                    this.editingAnimationProgress = 0.0f;
                }
                if (this.editingAnimationProgress >= 1.0f) {
                    this.editingAnimationProgress = 1.0f;
                    this.editingForwardAnimation = false;
                }
            } else {
                float f4 = this.editingAnimationProgress;
                r11 = f4 >= 0.0f;
                float f5 = f4 - (min / 420.0f);
                this.editingAnimationProgress = f5;
                if (!z && r11 && f5 <= 0.0f) {
                    this.editingAnimationProgress = 0.0f;
                }
                if (this.editingAnimationProgress <= -1.0f) {
                    this.editingAnimationProgress = -1.0f;
                    this.editingForwardAnimation = true;
                }
            }
            r11 = true;
        }
        if (z) {
            float f6 = this.editingStartAnimationProgress;
            if (f6 < 1.0f) {
                float f7 = f6 + (min / 180.0f);
                this.editingStartAnimationProgress = f7;
                if (f7 > 1.0f) {
                    this.editingStartAnimationProgress = 1.0f;
                }
            }
            z2 = r11;
        } else {
            if (!z) {
                float f8 = this.editingStartAnimationProgress;
                if (f8 > 0.0f) {
                    float f9 = f8 - (min / 180.0f);
                    this.editingStartAnimationProgress = f9;
                    if (f9 < 0.0f) {
                        this.editingStartAnimationProgress = 0.0f;
                    }
                }
            }
            z2 = r11;
        }
        if (z2) {
            this.listView.invalidateViews();
            invalidate();
        }
        return drawChild;
    }

    public void finishAddingTabs(boolean z) {
        this.listView.setItemAnimator(z ? this.itemAnimator : null);
        this.adapter.notifyDataSetChanged();
    }

    public int getCurrentTabId() {
        return this.selectedTabId;
    }

    public int getCurrentTabStableId() {
        return this.positionToStableId.get(this.currentPosition, -1);
    }

    public int getDefaultTabId() {
        Tab findDefaultTab = findDefaultTab();
        if (findDefaultTab == null) {
            return -1;
        }
        return findDefaultTab.id;
    }

    public int getFirstTabId() {
        return this.positionToId.get(0, 0);
    }

    public RecyclerListView getListView() {
        return this.listView;
    }

    public int getNextPageId(boolean z) {
        return this.positionToId.get(this.currentPosition + (z ? 1 : -1), -1);
    }

    public int getSelectorColorKey() {
        return this.selectorColorKey;
    }

    public Drawable getSelectorDrawable() {
        return this.selectorDrawable;
    }

    public int getStableId(int i) {
        return this.positionToStableId.get(i, -1);
    }

    public Tab getTab(int i) {
        if (i < 0 || i >= getTabsCount()) {
            return null;
        }
        return (Tab) this.tabs.get(i);
    }

    public RecyclerListView getTabsContainer() {
        return this.listView;
    }

    public int getTabsCount() {
        return this.tabs.size();
    }

    public boolean isAnimatingIndicator() {
        return this.animatingIndicator;
    }

    public boolean isEditing() {
        return this.isEditing;
    }

    public boolean isEmpty() {
        return this.tabs.isEmpty();
    }

    public boolean isFirstTab() {
        return this.currentPosition <= 0;
    }

    public boolean isFirstTabSelected() {
        return this.tabs.isEmpty() || this.selectedTabId == ((Tab) this.tabs.get(0)).id;
    }

    public boolean isLocked(int i) {
        for (int i2 = 0; i2 < this.tabs.size(); i2++) {
            if (((Tab) this.tabs.get(i2)).id == i) {
                return ((Tab) this.tabs.get(i2)).isLocked;
            }
        }
        return false;
    }

    public void notifyTabCounterChanged(int i) {
        int i2 = this.idToPosition.get(i, -1);
        if (i2 < 0 || i2 >= this.tabs.size()) {
            return;
        }
        Tab tab = (Tab) this.tabs.get(i2);
        if (tab.counter == this.delegate.getTabCounter(tab.id) || this.delegate.getTabCounter(tab.id) < 0) {
            return;
        }
        this.listView.invalidateViews();
        if (this.positionToWidth.get(i2) != tab.getWidth(true) || this.invalidated) {
            this.invalidated = true;
            requestLayout();
            this.listView.setItemAnimator(this.itemAnimator);
            this.adapter.notifyDataSetChanged();
            this.allTabsWidth = 0;
            findDefaultTab().setTitle(LocaleController.getString(R.string.FilterAllChats));
            int size = this.tabs.size();
            for (int i3 = 0; i3 < size; i3++) {
                this.allTabsWidth += ((Tab) this.tabs.get(i3)).getWidth(true) + AndroidUtilities.dp(32.0f);
            }
        }
    }

    protected abstract void onDefaultTabMoved();

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
                FilterTabsViewDelegate filterTabsViewDelegate = this.delegate;
                if (filterTabsViewDelegate != null) {
                    filterTabsViewDelegate.onPageScrolled(1.0f);
                }
            }
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        if (!this.tabs.isEmpty()) {
            int size = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(7.0f)) - AndroidUtilities.dp(7.0f);
            Tab findDefaultTab = findDefaultTab();
            if (findDefaultTab != null) {
                int i3 = R.string.FilterAllChats;
                findDefaultTab.setTitle(LocaleController.getString(i3));
                int width = findDefaultTab.getWidth(false);
                if (this.allTabsWidth > size) {
                    i3 = R.string.FilterAllChatsShort;
                }
                findDefaultTab.setTitle(LocaleController.getString(i3));
                int width2 = (this.allTabsWidth - width) + findDefaultTab.getWidth(false);
                int i4 = this.additionalTabWidth;
                int size2 = width2 < size ? (size - width2) / this.tabs.size() : 0;
                this.additionalTabWidth = size2;
                if (i4 != size2) {
                    this.ignoreLayout = true;
                    RecyclerView.ItemAnimator itemAnimator = this.listView.getItemAnimator();
                    this.listView.setItemAnimator(null);
                    this.adapter.notifyDataSetChanged();
                    this.listView.setItemAnimator(itemAnimator);
                    this.ignoreLayout = false;
                }
                updateTabsWidths();
                this.invalidated = false;
            }
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

    public void resetTabId() {
        this.selectedTabId = -1;
    }

    public void scrollToTab(Tab tab, int i) {
        if (tab.isLocked) {
            FilterTabsViewDelegate filterTabsViewDelegate = this.delegate;
            if (filterTabsViewDelegate != null) {
                filterTabsViewDelegate.onPageSelected(tab, false);
                return;
            }
            return;
        }
        int i2 = this.currentPosition;
        boolean z = i2 < i;
        this.scrollingToChild = -1;
        this.previousPosition = i2;
        this.previousId = this.selectedTabId;
        this.currentPosition = i;
        this.selectedTabId = tab.id;
        if (this.animatingIndicator) {
            AndroidUtilities.cancelRunOnUIThread(this.animationRunnable);
            this.animatingIndicator = false;
        }
        this.animationTime = 0.0f;
        this.animatingIndicatorProgress = 0.0f;
        this.animatingIndicator = true;
        setEnabled(false);
        AndroidUtilities.runOnUIThread(this.animationRunnable, 16L);
        FilterTabsViewDelegate filterTabsViewDelegate2 = this.delegate;
        if (filterTabsViewDelegate2 != null) {
            filterTabsViewDelegate2.onPageSelected(tab, z);
        }
        scrollToChild(i);
    }

    public void selectFirstTab() {
        if (this.tabs.isEmpty()) {
            return;
        }
        scrollToTab((Tab) this.tabs.get(0), 0);
    }

    public void selectLastTab() {
        if (this.tabs.isEmpty()) {
            return;
        }
        scrollToTab((Tab) this.tabs.get(r0.size() - 1), this.tabs.size() - 1);
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

    public boolean selectTabWithStableId(int i) {
        for (int i2 = 0; i2 < this.tabs.size(); i2++) {
            if (this.positionToStableId.get(i2, -1) == i) {
                this.currentPosition = i2;
                this.selectedTabId = this.positionToId.get(i2);
                return true;
            }
        }
        return false;
    }

    public void setAnimationIdicatorProgress(float f) {
        this.animatingIndicatorProgress = f;
        this.listView.invalidateViews();
        invalidate();
        FilterTabsViewDelegate filterTabsViewDelegate = this.delegate;
        if (filterTabsViewDelegate != null) {
            filterTabsViewDelegate.onPageScrolled(f);
        }
    }

    public void setDelegate(FilterTabsViewDelegate filterTabsViewDelegate) {
        this.delegate = filterTabsViewDelegate;
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
        ArrayList<MessagesController.DialogFilter> dialogFilters = MessagesController.getInstance(UserConfig.selectedAccount).getDialogFilters();
        int size = dialogFilters.size();
        for (int i = 0; i < size; i++) {
            MessagesController.DialogFilter dialogFilter = dialogFilters.get(i);
            if (dialogFilter.isDefault()) {
                tL_messages_updateDialogFiltersOrder.order.add(0);
            } else {
                tL_messages_updateDialogFiltersOrder.order.add(Integer.valueOf(dialogFilter.id));
            }
        }
        MessagesController.getInstance(UserConfig.selectedAccount).lockFiltersInternal();
        ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(tL_messages_updateDialogFiltersOrder, new RequestDelegate() { // from class: org.telegram.ui.Components.FilterTabsView$$ExternalSyntheticLambda2
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                FilterTabsView.lambda$setIsEditing$2(tLObject, tL_error);
            }
        });
        this.orderChanged = false;
    }

    public void shakeLock(int i) {
        for (int i2 = 0; i2 < this.listView.getChildCount(); i2++) {
            if (this.listView.getChildAt(i2) instanceof TabView) {
                TabView tabView = (TabView) this.listView.getChildAt(i2);
                if (tabView.currentTab.id == i) {
                    tabView.shakeLockIcon(1.0f, 0);
                    tabView.performHapticFeedback(3);
                    return;
                }
            }
        }
    }

    public void stopAnimatingIndicator() {
        this.animatingIndicator = false;
    }
}
