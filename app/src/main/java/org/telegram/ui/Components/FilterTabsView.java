package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_updateDialogFiltersOrder;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.FilterTabsView;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class FilterTabsView extends FrameLayout {
    private String aActiveTextColorKey;
    private String aBackgroundColorKey;
    private String aTabLineColorKey;
    private String aUnactiveTextColorKey;
    private ListAdapter adapter;
    private int additionalTabWidth;
    private int allTabsWidth;
    private boolean animatingIndicator;
    private float animatingIndicatorProgress;
    private float animationTime;
    private float animationValue;
    private AnimatorSet colorChangeAnimator;
    private int currentPosition;
    private FilterTabsViewDelegate delegate;
    private float editingAnimationProgress;
    private boolean editingForwardAnimation;
    private float editingStartAnimationProgress;
    private boolean ignoreLayout;
    private boolean invalidated;
    private boolean isEditing;
    DefaultItemAnimator itemAnimator;
    private long lastAnimationTime;
    private long lastEditingAnimationTime;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private Drawable lockDrawable;
    private int lockDrawableColor;
    private boolean orderChanged;
    private int prevLayoutWidth;
    private int previousId;
    private int previousPosition;
    private TextPaint textPaint = new TextPaint(1);
    private TextPaint textCounterPaint = new TextPaint(1);
    private Paint deletePaint = new TextPaint(1);
    private Paint counterPaint = new Paint(1);
    private ArrayList<Tab> tabs = new ArrayList<>();
    private int selectedTabId = -1;
    private int manualScrollingToPosition = -1;
    private int manualScrollingToId = -1;
    private int scrollingToChild = -1;
    private String tabLineColorKey = "actionBarTabLine";
    private String activeTextColorKey = "actionBarTabActiveText";
    private String unactiveTextColorKey = "actionBarTabUnactiveText";
    private String selectorColorKey = "actionBarTabSelector";
    private String backgroundColorKey = "actionBarDefault";
    private CubicBezierInterpolator interpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
    private SparseIntArray positionToId = new SparseIntArray(5);
    private SparseIntArray positionToStableId = new SparseIntArray(5);
    private SparseIntArray idToPosition = new SparseIntArray(5);
    private SparseIntArray positionToWidth = new SparseIntArray(5);
    private SparseIntArray positionToX = new SparseIntArray(5);
    private Runnable animationRunnable = new Runnable() { // from class: org.telegram.ui.Components.FilterTabsView.1
        @Override // java.lang.Runnable
        public void run() {
            if (!FilterTabsView.this.animatingIndicator) {
                return;
            }
            long elapsedRealtime = SystemClock.elapsedRealtime() - FilterTabsView.this.lastAnimationTime;
            if (elapsedRealtime > 17) {
                elapsedRealtime = 17;
            }
            FilterTabsView.access$2616(FilterTabsView.this, ((float) elapsedRealtime) / 200.0f);
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
            if (FilterTabsView.this.delegate == null) {
                return;
            }
            FilterTabsView.this.delegate.onPageScrolled(1.0f);
        }
    };
    private final Property<FilterTabsView, Float> COLORS = new AnimationProperties.FloatProperty<FilterTabsView>("animationValue") { // from class: org.telegram.ui.Components.FilterTabsView.2
        public void setValue(FilterTabsView filterTabsView, float f) {
            FilterTabsView.this.animationValue = f;
            FilterTabsView.this.selectorDrawable.setColor(ColorUtils.blendARGB(Theme.getColor(FilterTabsView.this.tabLineColorKey), Theme.getColor(FilterTabsView.this.aTabLineColorKey), f));
            FilterTabsView.this.listView.invalidateViews();
            FilterTabsView.this.listView.invalidate();
            filterTabsView.invalidate();
        }

        public Float get(FilterTabsView filterTabsView) {
            return Float.valueOf(FilterTabsView.this.animationValue);
        }
    };
    private GradientDrawable selectorDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, null);

    /* loaded from: classes3.dex */
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

    public static /* synthetic */ void lambda$setIsEditing$2(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    static /* synthetic */ float access$2616(FilterTabsView filterTabsView, float f) {
        float f2 = filterTabsView.animationTime + f;
        filterTabsView.animationTime = f2;
        return f2;
    }

    public int getCurrentTabStableId() {
        return this.positionToStableId.get(this.currentPosition, -1);
    }

    public int getStableId(int i) {
        return this.positionToStableId.get(i, -1);
    }

    /* loaded from: classes3.dex */
    public class Tab {
        public int counter;
        public int id;
        public boolean isDefault;
        public boolean isLocked;
        public String title;
        public int titleWidth;

        public Tab(int i, String str) {
            FilterTabsView.this = r1;
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

    /* loaded from: classes3.dex */
    public class TabView extends View {
        public boolean animateChange;
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
        private float lastTabWidth;
        float lastTextX;
        String lastTitle;
        private int lastTitleWidth;
        private float lastWidth;
        private float locIconXOffset;
        StaticLayout outCounter;
        private float progressToLocked;
        StaticLayout stableCounter;
        private int tabWidth;
        private int textHeight;
        private StaticLayout textLayout;
        private int textOffsetX;
        private StaticLayout titleAnimateInLayout;
        private StaticLayout titleAnimateOutLayout;
        private StaticLayout titleAnimateStableLayout;
        private float titleXOffset;
        private RectF rect = new RectF();
        int lastTabCount = -1;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TabView(Context context) {
            super(context);
            FilterTabsView.this = r1;
        }

        public void setTab(Tab tab, int i) {
            this.currentTab = tab;
            this.currentPosition = i;
            setContentDescription(tab.title);
            requestLayout();
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

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            setMeasuredDimension(this.currentTab.getWidth(false) + AndroidUtilities.dp(32.0f) + FilterTabsView.this.additionalTabWidth, View.MeasureSpec.getSize(i2));
        }

        /* JADX WARN: Removed duplicated region for block: B:179:0x0519  */
        /* JADX WARN: Removed duplicated region for block: B:185:0x0544  */
        /* JADX WARN: Removed duplicated region for block: B:189:0x056c  */
        /* JADX WARN: Removed duplicated region for block: B:200:0x05bc  */
        /* JADX WARN: Removed duplicated region for block: B:201:0x05c7  */
        /* JADX WARN: Removed duplicated region for block: B:204:0x05cd  */
        /* JADX WARN: Removed duplicated region for block: B:207:0x060b  */
        /* JADX WARN: Removed duplicated region for block: B:210:0x064a  */
        /* JADX WARN: Removed duplicated region for block: B:212:0x067c  */
        /* JADX WARN: Removed duplicated region for block: B:226:0x0759  */
        /* JADX WARN: Removed duplicated region for block: B:231:0x078d  */
        /* JADX WARN: Removed duplicated region for block: B:235:0x079c  */
        /* JADX WARN: Removed duplicated region for block: B:238:0x07b5  */
        /* JADX WARN: Removed duplicated region for block: B:242:0x07c3  */
        /* JADX WARN: Removed duplicated region for block: B:245:0x07e5  */
        /* JADX WARN: Removed duplicated region for block: B:248:0x0801  */
        /* JADX WARN: Removed duplicated region for block: B:251:0x085f  */
        /* JADX WARN: Removed duplicated region for block: B:252:0x0892  */
        @Override // android.view.View
        @SuppressLint({"DrawAllocation"})
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onDraw(Canvas canvas) {
            int i;
            int i2;
            String str;
            String str2;
            String str3;
            String str4;
            int i3;
            float f;
            String str5;
            int i4;
            int i5;
            boolean z;
            boolean z2;
            float f2;
            float f3;
            float f4;
            float f5;
            int i6;
            int dp;
            int lineBottom;
            int lineTop;
            int i7;
            boolean z3;
            int color;
            int i8;
            int i9;
            int i10 = 0;
            boolean z4 = !this.currentTab.isDefault || UserConfig.getInstance(UserConfig.selectedAccount).isPremium();
            boolean z5 = !this.currentTab.isDefault && z4;
            if (z4 && FilterTabsView.this.editingAnimationProgress != 0.0f) {
                canvas.save();
                float f6 = FilterTabsView.this.editingAnimationProgress * (this.currentPosition % 2 == 0 ? 1.0f : -1.0f);
                canvas.translate(AndroidUtilities.dp(0.66f) * f6, 0.0f);
                canvas.rotate(f6, getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f);
            }
            if (FilterTabsView.this.manualScrollingToId != -1) {
                i2 = FilterTabsView.this.manualScrollingToId;
                i = FilterTabsView.this.selectedTabId;
            } else {
                i2 = FilterTabsView.this.selectedTabId;
                i = FilterTabsView.this.previousId;
            }
            String str6 = "chats_tabUnreadActiveBackground";
            String str7 = "chats_tabUnreadUnactiveBackground";
            if (this.currentTab.id == i2) {
                str2 = FilterTabsView.this.activeTextColorKey;
                str = FilterTabsView.this.aActiveTextColorKey;
                str3 = FilterTabsView.this.unactiveTextColorKey;
                str4 = FilterTabsView.this.aUnactiveTextColorKey;
            } else {
                str2 = FilterTabsView.this.unactiveTextColorKey;
                str = FilterTabsView.this.aUnactiveTextColorKey;
                str3 = FilterTabsView.this.activeTextColorKey;
                str4 = FilterTabsView.this.aUnactiveTextColorKey;
                str7 = str6;
                str6 = str7;
            }
            if (str == null) {
                if ((FilterTabsView.this.animatingIndicator || FilterTabsView.this.manualScrollingToId != -1) && ((i9 = this.currentTab.id) == i2 || i9 == i)) {
                    FilterTabsView.this.textPaint.setColor(ColorUtils.blendARGB(Theme.getColor(str3), Theme.getColor(str2), FilterTabsView.this.animatingIndicatorProgress));
                } else {
                    FilterTabsView.this.textPaint.setColor(Theme.getColor(str2));
                }
            } else {
                int color2 = Theme.getColor(str2);
                int color3 = Theme.getColor(str);
                if ((FilterTabsView.this.animatingIndicator || FilterTabsView.this.manualScrollingToPosition != -1) && ((i8 = this.currentTab.id) == i2 || i8 == i)) {
                    FilterTabsView.this.textPaint.setColor(ColorUtils.blendARGB(ColorUtils.blendARGB(Theme.getColor(str3), Theme.getColor(str4), FilterTabsView.this.animationValue), ColorUtils.blendARGB(color2, color3, FilterTabsView.this.animationValue), FilterTabsView.this.animatingIndicatorProgress));
                } else {
                    FilterTabsView.this.textPaint.setColor(ColorUtils.blendARGB(color2, color3, FilterTabsView.this.animationValue));
                }
            }
            int i11 = this.animateFromTabCount;
            boolean z6 = i11 == 0 && this.animateTabCounter;
            boolean z7 = i11 > 0 && this.currentTab.counter == 0 && this.animateTabCounter;
            boolean z8 = i11 > 0 && this.currentTab.counter > 0 && this.animateTabCounter;
            int i12 = this.currentTab.counter;
            if (i12 > 0 || z7) {
                str5 = z7 ? String.format("%d", Integer.valueOf(i11)) : String.format("%d", Integer.valueOf(i12));
                i3 = i2;
                float ceil = (int) Math.ceil(FilterTabsView.this.textCounterPaint.measureText(str5));
                f = ceil;
                i10 = (int) (Math.max(AndroidUtilities.dp(10.0f), ceil) + AndroidUtilities.dp(10.0f));
            } else {
                str5 = null;
                i3 = i2;
                f = 0.0f;
            }
            if (z5 && (FilterTabsView.this.isEditing || FilterTabsView.this.editingStartAnimationProgress != 0.0f)) {
                i10 = (int) (i10 + ((AndroidUtilities.dp(20.0f) - i10) * FilterTabsView.this.editingStartAnimationProgress));
            }
            int i13 = i10;
            int i14 = this.currentTab.titleWidth;
            if (i13 == 0 || z7) {
                i4 = 0;
            } else {
                i4 = AndroidUtilities.dp((str5 != null ? 1.0f : FilterTabsView.this.editingStartAnimationProgress) * 6.0f) + i13;
            }
            this.tabWidth = i14 + i4;
            float measuredWidth = (getMeasuredWidth() - this.tabWidth) / 2.0f;
            if (this.animateTextX) {
                float f7 = this.changeProgress;
                i5 = i3;
                measuredWidth = (measuredWidth * f7) + (this.animateFromTextX * (1.0f - f7));
            } else {
                i5 = i3;
            }
            float f8 = measuredWidth;
            if (!TextUtils.equals(this.currentTab.title, this.currentText)) {
                String str8 = this.currentTab.title;
                this.currentText = str8;
                z2 = z4;
                z = z7;
                StaticLayout staticLayout = new StaticLayout(Emoji.replaceEmoji(str8, FilterTabsView.this.textPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false), FilterTabsView.this.textPaint, AndroidUtilities.dp(400.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.textLayout = staticLayout;
                this.textHeight = staticLayout.getHeight();
                this.textOffsetX = (int) (-this.textLayout.getLineLeft(0));
            } else {
                z2 = z4;
                z = z7;
            }
            if (this.animateTextChange) {
                f3 = this.titleXOffset * (this.animateTextChangeOut ? this.changeProgress : 1.0f - this.changeProgress);
                if (this.titleAnimateStableLayout != null) {
                    canvas.save();
                    canvas.translate(this.textOffsetX + f8 + f3, ((getMeasuredHeight() - this.textHeight) / 2.0f) + 1.0f);
                    this.titleAnimateStableLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.titleAnimateInLayout != null) {
                    canvas.save();
                    int alpha = FilterTabsView.this.textPaint.getAlpha();
                    f2 = f;
                    FilterTabsView.this.textPaint.setAlpha((int) (alpha * (this.animateTextChangeOut ? 1.0f - this.changeProgress : this.changeProgress)));
                    canvas.translate(this.textOffsetX + f8 + f3, ((getMeasuredHeight() - this.textHeight) / 2.0f) + 1.0f);
                    this.titleAnimateInLayout.draw(canvas);
                    canvas.restore();
                    FilterTabsView.this.textPaint.setAlpha(alpha);
                } else {
                    f2 = f;
                }
                if (this.titleAnimateOutLayout != null) {
                    canvas.save();
                    int alpha2 = FilterTabsView.this.textPaint.getAlpha();
                    FilterTabsView.this.textPaint.setAlpha((int) (alpha2 * (this.animateTextChangeOut ? this.changeProgress : 1.0f - this.changeProgress)));
                    canvas.translate(this.textOffsetX + f8 + f3, ((getMeasuredHeight() - this.textHeight) / 2.0f) + 1.0f);
                    this.titleAnimateOutLayout.draw(canvas);
                    canvas.restore();
                    FilterTabsView.this.textPaint.setAlpha(alpha2);
                }
            } else {
                f2 = f;
                if (this.textLayout != null) {
                    canvas.save();
                    canvas.translate(this.textOffsetX + f8, ((getMeasuredHeight() - this.textHeight) / 2.0f) + 1.0f);
                    this.textLayout.draw(canvas);
                    canvas.restore();
                }
                f3 = 0.0f;
            }
            if (z6 || str5 != null || (z5 && (FilterTabsView.this.isEditing || FilterTabsView.this.editingStartAnimationProgress != 0.0f))) {
                if (FilterTabsView.this.aBackgroundColorKey == null) {
                    FilterTabsView.this.textCounterPaint.setColor(Theme.getColor(FilterTabsView.this.backgroundColorKey));
                } else {
                    FilterTabsView.this.textCounterPaint.setColor(ColorUtils.blendARGB(Theme.getColor(FilterTabsView.this.backgroundColorKey), Theme.getColor(FilterTabsView.this.aBackgroundColorKey), FilterTabsView.this.animationValue));
                }
                if (!Theme.hasThemeKey(str6) || !Theme.hasThemeKey(str7)) {
                    FilterTabsView.this.counterPaint.setColor(FilterTabsView.this.textPaint.getColor());
                } else {
                    int color4 = Theme.getColor(str6);
                    if ((FilterTabsView.this.animatingIndicator || FilterTabsView.this.manualScrollingToPosition != -1) && ((i7 = this.currentTab.id) == i5 || i7 == i)) {
                        FilterTabsView.this.counterPaint.setColor(ColorUtils.blendARGB(Theme.getColor(str7), color4, FilterTabsView.this.animatingIndicatorProgress));
                    } else {
                        FilterTabsView.this.counterPaint.setColor(color4);
                    }
                }
                int i15 = this.currentTab.titleWidth;
                float f9 = i15;
                boolean z9 = this.animateTextChange;
                if (z9) {
                    float f10 = this.changeProgress;
                    f9 = (this.animateFromTitleWidth * (1.0f - f10)) + (i15 * f10);
                }
                if (z9 && this.titleAnimateOutLayout == null) {
                    f4 = (f8 - this.titleXOffset) + f3 + f9 + AndroidUtilities.dp(6.0f);
                } else {
                    f4 = f9 + f8 + AndroidUtilities.dp(6.0f);
                }
                int measuredHeight = (getMeasuredHeight() - AndroidUtilities.dp(20.0f)) / 2;
                if (!z5 || ((!FilterTabsView.this.isEditing && FilterTabsView.this.editingStartAnimationProgress == 0.0f) || str5 != null)) {
                    FilterTabsView.this.counterPaint.setAlpha(255);
                } else {
                    FilterTabsView.this.counterPaint.setAlpha((int) (FilterTabsView.this.editingStartAnimationProgress * 255.0f));
                }
                if (z8) {
                    float f11 = this.animateFromCountWidth;
                    float f12 = i13;
                    if (f11 != f12) {
                        float f13 = this.changeProgress;
                        f5 = (f11 * (1.0f - f13)) + (f12 * f13);
                        if (z8) {
                            float f14 = this.animateFromCounterWidth;
                            float f15 = this.changeProgress;
                            f2 = (f14 * (1.0f - f15)) + (f15 * f2);
                        }
                        float f16 = measuredHeight;
                        this.rect.set(f4, f16, f5 + f4, measuredHeight + AndroidUtilities.dp(20.0f));
                        if (!z6 || z) {
                            canvas.save();
                            float f17 = this.changeProgress;
                            if (!z6) {
                                f17 = 1.0f - f17;
                            }
                            canvas.scale(f17, f17, this.rect.centerX(), this.rect.centerY());
                        }
                        RectF rectF = this.rect;
                        float f18 = AndroidUtilities.density;
                        canvas.drawRoundRect(rectF, f18 * 11.5f, f18 * 11.5f, FilterTabsView.this.counterPaint);
                        if (!z8) {
                            if (this.inCounter != null) {
                                dp = AndroidUtilities.dp(20.0f);
                                lineBottom = this.inCounter.getLineBottom(0);
                                lineTop = this.inCounter.getLineTop(0);
                            } else if (this.outCounter != null) {
                                dp = AndroidUtilities.dp(20.0f);
                                lineBottom = this.outCounter.getLineBottom(0);
                                lineTop = this.outCounter.getLineTop(0);
                            } else {
                                if (this.stableCounter != null) {
                                    dp = AndroidUtilities.dp(20.0f);
                                    lineBottom = this.stableCounter.getLineBottom(0);
                                    lineTop = this.stableCounter.getLineTop(0);
                                }
                                float f19 = !z5 ? 1.0f - FilterTabsView.this.editingStartAnimationProgress : 1.0f;
                                if (this.inCounter != null) {
                                    canvas.save();
                                    FilterTabsView.this.textCounterPaint.setAlpha((int) (f19 * 255.0f * this.changeProgress));
                                    RectF rectF2 = this.rect;
                                    canvas.translate(rectF2.left + ((rectF2.width() - f2) / 2.0f), ((1.0f - this.changeProgress) * AndroidUtilities.dp(15.0f)) + f16);
                                    this.inCounter.draw(canvas);
                                    canvas.restore();
                                }
                                if (this.outCounter != null) {
                                    canvas.save();
                                    FilterTabsView.this.textCounterPaint.setAlpha((int) (f19 * 255.0f * (1.0f - this.changeProgress)));
                                    RectF rectF3 = this.rect;
                                    canvas.translate(rectF3.left + ((rectF3.width() - f2) / 2.0f), (this.changeProgress * (-AndroidUtilities.dp(15.0f))) + f16);
                                    this.outCounter.draw(canvas);
                                    canvas.restore();
                                }
                                if (this.stableCounter != null) {
                                    canvas.save();
                                    FilterTabsView.this.textCounterPaint.setAlpha((int) (f19 * 255.0f));
                                    RectF rectF4 = this.rect;
                                    canvas.translate(rectF4.left + ((rectF4.width() - f2) / 2.0f), f16);
                                    this.stableCounter.draw(canvas);
                                    canvas.restore();
                                }
                                FilterTabsView.this.textCounterPaint.setAlpha(255);
                            }
                            f16 += (dp - (lineBottom - lineTop)) / 2.0f;
                            if (!z5) {
                            }
                            if (this.inCounter != null) {
                            }
                            if (this.outCounter != null) {
                            }
                            if (this.stableCounter != null) {
                            }
                            FilterTabsView.this.textCounterPaint.setAlpha(255);
                        } else if (str5 != null) {
                            if (z5) {
                                FilterTabsView.this.textCounterPaint.setAlpha((int) ((1.0f - FilterTabsView.this.editingStartAnimationProgress) * 255.0f));
                            }
                            RectF rectF5 = this.rect;
                            canvas.drawText(str5, rectF5.left + ((rectF5.width() - f2) / 2.0f), measuredHeight + AndroidUtilities.dp(14.5f), FilterTabsView.this.textCounterPaint);
                        }
                        if (!z6 || z) {
                            canvas.restore();
                        }
                        if (z5 && (FilterTabsView.this.isEditing || FilterTabsView.this.editingStartAnimationProgress != 0.0f)) {
                            FilterTabsView.this.deletePaint.setColor(FilterTabsView.this.textCounterPaint.getColor());
                            FilterTabsView.this.deletePaint.setAlpha((int) (FilterTabsView.this.editingStartAnimationProgress * 255.0f));
                            float dp2 = AndroidUtilities.dp(3.0f);
                            i6 = i13;
                            canvas.drawLine(this.rect.centerX() - dp2, this.rect.centerY() - dp2, this.rect.centerX() + dp2, this.rect.centerY() + dp2, FilterTabsView.this.deletePaint);
                            canvas.drawLine(this.rect.centerX() - dp2, this.rect.centerY() + dp2, this.rect.centerX() + dp2, this.rect.centerY() - dp2, FilterTabsView.this.deletePaint);
                            float f20 = f2;
                            if (z2 && FilterTabsView.this.editingAnimationProgress != 0.0f) {
                                canvas.restore();
                            }
                            this.lastTextX = f8;
                            Tab tab = this.currentTab;
                            this.lastTabCount = tab.counter;
                            this.lastTitle = this.currentText;
                            this.lastTitleWidth = tab.titleWidth;
                            this.lastCountWidth = i6;
                            this.lastCounterWidth = f20;
                            this.lastTabWidth = this.tabWidth;
                            this.lastWidth = getMeasuredWidth();
                            if (this.currentTab.isLocked && this.progressToLocked == 0.0f) {
                                return;
                            }
                            if (FilterTabsView.this.lockDrawable == null) {
                                FilterTabsView.this.lockDrawable = ContextCompat.getDrawable(getContext(), R.drawable.other_lockedfolders);
                            }
                            z3 = this.currentTab.isLocked;
                            if (z3) {
                                float f21 = this.progressToLocked;
                                if (f21 != 1.0f) {
                                    this.progressToLocked = f21 + 0.10666667f;
                                    this.progressToLocked = Utilities.clamp(this.progressToLocked, 1.0f, 0.0f);
                                    color = Theme.getColor(FilterTabsView.this.unactiveTextColorKey);
                                    if (FilterTabsView.this.aUnactiveTextColorKey != null) {
                                        color = ColorUtils.blendARGB(color, Theme.getColor(FilterTabsView.this.aUnactiveTextColorKey), FilterTabsView.this.animationValue);
                                    }
                                    if (FilterTabsView.this.lockDrawableColor != color) {
                                        FilterTabsView.this.lockDrawableColor = color;
                                        FilterTabsView.this.lockDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
                                    }
                                    int measuredWidth2 = (int) (((getMeasuredWidth() - FilterTabsView.this.lockDrawable.getIntrinsicWidth()) / 2.0f) + this.locIconXOffset);
                                    int measuredHeight2 = getMeasuredHeight() - AndroidUtilities.dp(12.0f);
                                    FilterTabsView.this.lockDrawable.setBounds(measuredWidth2, measuredHeight2, FilterTabsView.this.lockDrawable.getIntrinsicWidth() + measuredWidth2, FilterTabsView.this.lockDrawable.getIntrinsicHeight() + measuredHeight2);
                                    if (this.progressToLocked != 1.0f) {
                                        FilterTabsView.this.lockDrawable.draw(canvas);
                                        return;
                                    }
                                    canvas.save();
                                    float f22 = this.progressToLocked;
                                    canvas.scale(f22, f22, FilterTabsView.this.lockDrawable.getBounds().centerX(), FilterTabsView.this.lockDrawable.getBounds().centerY());
                                    FilterTabsView.this.lockDrawable.draw(canvas);
                                    canvas.restore();
                                    return;
                                }
                            }
                            if (!z3) {
                                this.progressToLocked -= 0.10666667f;
                            }
                            this.progressToLocked = Utilities.clamp(this.progressToLocked, 1.0f, 0.0f);
                            color = Theme.getColor(FilterTabsView.this.unactiveTextColorKey);
                            if (FilterTabsView.this.aUnactiveTextColorKey != null) {
                            }
                            if (FilterTabsView.this.lockDrawableColor != color) {
                            }
                            int measuredWidth22 = (int) (((getMeasuredWidth() - FilterTabsView.this.lockDrawable.getIntrinsicWidth()) / 2.0f) + this.locIconXOffset);
                            int measuredHeight22 = getMeasuredHeight() - AndroidUtilities.dp(12.0f);
                            FilterTabsView.this.lockDrawable.setBounds(measuredWidth22, measuredHeight22, FilterTabsView.this.lockDrawable.getIntrinsicWidth() + measuredWidth22, FilterTabsView.this.lockDrawable.getIntrinsicHeight() + measuredHeight22);
                            if (this.progressToLocked != 1.0f) {
                            }
                        }
                    }
                }
                f5 = i13;
                if (z8) {
                }
                float f162 = measuredHeight;
                this.rect.set(f4, f162, f5 + f4, measuredHeight + AndroidUtilities.dp(20.0f));
                if (!z6) {
                }
                canvas.save();
                float f172 = this.changeProgress;
                if (!z6) {
                }
                canvas.scale(f172, f172, this.rect.centerX(), this.rect.centerY());
                RectF rectF6 = this.rect;
                float f182 = AndroidUtilities.density;
                canvas.drawRoundRect(rectF6, f182 * 11.5f, f182 * 11.5f, FilterTabsView.this.counterPaint);
                if (!z8) {
                }
                if (!z6) {
                }
                canvas.restore();
                if (z5) {
                    FilterTabsView.this.deletePaint.setColor(FilterTabsView.this.textCounterPaint.getColor());
                    FilterTabsView.this.deletePaint.setAlpha((int) (FilterTabsView.this.editingStartAnimationProgress * 255.0f));
                    float dp22 = AndroidUtilities.dp(3.0f);
                    i6 = i13;
                    canvas.drawLine(this.rect.centerX() - dp22, this.rect.centerY() - dp22, this.rect.centerX() + dp22, this.rect.centerY() + dp22, FilterTabsView.this.deletePaint);
                    canvas.drawLine(this.rect.centerX() - dp22, this.rect.centerY() + dp22, this.rect.centerX() + dp22, this.rect.centerY() - dp22, FilterTabsView.this.deletePaint);
                    float f202 = f2;
                    if (z2) {
                        canvas.restore();
                    }
                    this.lastTextX = f8;
                    Tab tab2 = this.currentTab;
                    this.lastTabCount = tab2.counter;
                    this.lastTitle = this.currentText;
                    this.lastTitleWidth = tab2.titleWidth;
                    this.lastCountWidth = i6;
                    this.lastCounterWidth = f202;
                    this.lastTabWidth = this.tabWidth;
                    this.lastWidth = getMeasuredWidth();
                    if (this.currentTab.isLocked) {
                    }
                    if (FilterTabsView.this.lockDrawable == null) {
                    }
                    z3 = this.currentTab.isLocked;
                    if (z3) {
                    }
                    if (!z3) {
                    }
                    this.progressToLocked = Utilities.clamp(this.progressToLocked, 1.0f, 0.0f);
                    color = Theme.getColor(FilterTabsView.this.unactiveTextColorKey);
                    if (FilterTabsView.this.aUnactiveTextColorKey != null) {
                    }
                    if (FilterTabsView.this.lockDrawableColor != color) {
                    }
                    int measuredWidth222 = (int) (((getMeasuredWidth() - FilterTabsView.this.lockDrawable.getIntrinsicWidth()) / 2.0f) + this.locIconXOffset);
                    int measuredHeight222 = getMeasuredHeight() - AndroidUtilities.dp(12.0f);
                    FilterTabsView.this.lockDrawable.setBounds(measuredWidth222, measuredHeight222, FilterTabsView.this.lockDrawable.getIntrinsicWidth() + measuredWidth222, FilterTabsView.this.lockDrawable.getIntrinsicHeight() + measuredHeight222);
                    if (this.progressToLocked != 1.0f) {
                    }
                }
            }
            i6 = i13;
            float f2022 = f2;
            if (z2) {
            }
            this.lastTextX = f8;
            Tab tab22 = this.currentTab;
            this.lastTabCount = tab22.counter;
            this.lastTitle = this.currentText;
            this.lastTitleWidth = tab22.titleWidth;
            this.lastCountWidth = i6;
            this.lastCounterWidth = f2022;
            this.lastTabWidth = this.tabWidth;
            this.lastWidth = getMeasuredWidth();
            if (this.currentTab.isLocked) {
            }
            if (FilterTabsView.this.lockDrawable == null) {
            }
            z3 = this.currentTab.isLocked;
            if (z3) {
            }
            if (!z3) {
            }
            this.progressToLocked = Utilities.clamp(this.progressToLocked, 1.0f, 0.0f);
            color = Theme.getColor(FilterTabsView.this.unactiveTextColorKey);
            if (FilterTabsView.this.aUnactiveTextColorKey != null) {
            }
            if (FilterTabsView.this.lockDrawableColor != color) {
            }
            int measuredWidth2222 = (int) (((getMeasuredWidth() - FilterTabsView.this.lockDrawable.getIntrinsicWidth()) / 2.0f) + this.locIconXOffset);
            int measuredHeight2222 = getMeasuredHeight() - AndroidUtilities.dp(12.0f);
            FilterTabsView.this.lockDrawable.setBounds(measuredWidth2222, measuredHeight2222, FilterTabsView.this.lockDrawable.getIntrinsicWidth() + measuredWidth2222, FilterTabsView.this.lockDrawable.getIntrinsicHeight() + measuredHeight2222);
            if (this.progressToLocked != 1.0f) {
            }
        }

        public boolean animateChange() {
            boolean z;
            int i;
            String str;
            boolean z2;
            String str2;
            String str3;
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
                        this.outCounter = new StaticLayout(spannableStringBuilder, FilterTabsView.this.textCounterPaint, ceil, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                        this.stableCounter = new StaticLayout(spannableStringBuilder3, FilterTabsView.this.textCounterPaint, ceil, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                        this.inCounter = new StaticLayout(spannableStringBuilder2, FilterTabsView.this.textCounterPaint, ceil, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                    } else {
                        this.outCounter = new StaticLayout(valueOf, FilterTabsView.this.textCounterPaint, (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(valueOf)), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                        this.inCounter = new StaticLayout(valueOf2, FilterTabsView.this.textCounterPaint, (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(valueOf2)), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
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
            float f = this.lastTextX;
            if ((getMeasuredWidth() - dp) / 2 != f) {
                this.animateTextX = true;
                this.animateFromTextX = f;
                z = true;
            }
            String str4 = this.lastTitle;
            if (str4 != null && !this.currentTab.title.equals(str4)) {
                if (this.lastTitle.length() > this.currentTab.title.length()) {
                    str3 = this.lastTitle;
                    str2 = this.currentTab.title;
                    z2 = true;
                } else {
                    str3 = this.currentTab.title;
                    str2 = this.lastTitle;
                    z2 = false;
                }
                int indexOf = str3.indexOf(str2);
                float f2 = 0.0f;
                if (indexOf >= 0) {
                    CharSequence replaceEmoji = Emoji.replaceEmoji(str3, FilterTabsView.this.textPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false);
                    SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder(replaceEmoji);
                    SpannableStringBuilder spannableStringBuilder5 = new SpannableStringBuilder(replaceEmoji);
                    if (indexOf != 0) {
                        spannableStringBuilder5.setSpan(new EmptyStubSpan(), 0, indexOf, 0);
                    }
                    if (str2.length() + indexOf != str3.length()) {
                        spannableStringBuilder5.setSpan(new EmptyStubSpan(), str2.length() + indexOf, str3.length(), 0);
                    }
                    spannableStringBuilder4.setSpan(new EmptyStubSpan(), indexOf, str2.length() + indexOf, 0);
                    this.titleAnimateInLayout = new StaticLayout(spannableStringBuilder4, FilterTabsView.this.textPaint, AndroidUtilities.dp(400.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    StaticLayout staticLayout = new StaticLayout(spannableStringBuilder5, FilterTabsView.this.textPaint, AndroidUtilities.dp(400.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    this.titleAnimateStableLayout = staticLayout;
                    this.animateTextChange = true;
                    this.animateTextChangeOut = z2;
                    if (indexOf != 0) {
                        f2 = -staticLayout.getPrimaryHorizontal(indexOf);
                    }
                    this.titleXOffset = f2;
                    this.animateFromTitleWidth = this.lastTitleWidth;
                    this.titleAnimateOutLayout = null;
                } else {
                    this.titleAnimateInLayout = new StaticLayout(this.currentTab.title, FilterTabsView.this.textPaint, AndroidUtilities.dp(400.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    this.titleAnimateOutLayout = new StaticLayout(this.lastTitle, FilterTabsView.this.textPaint, AndroidUtilities.dp(400.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
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

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setSelected((this.currentTab == null || FilterTabsView.this.selectedTabId == -1 || this.currentTab.id != FilterTabsView.this.selectedTabId) ? false : true);
            accessibilityNodeInfo.addAction(16);
            if (Build.VERSION.SDK_INT >= 21) {
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(32, LocaleController.getString("AccDescrOpenMenu2", R.string.AccDescrOpenMenu2)));
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

        public void clearTransitionParams() {
            this.animateChange = false;
            this.animateTabCounter = false;
            this.animateTextChange = false;
            this.animateTextX = false;
            this.animateTabWidth = false;
            this.changeAnimator = null;
            invalidate();
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

        public /* synthetic */ void lambda$shakeLockIcon$0(ValueAnimator valueAnimator) {
            this.locIconXOffset = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidate();
        }
    }

    public FilterTabsView(Context context) {
        super(context);
        this.textCounterPaint.setTextSize(AndroidUtilities.dp(13.0f));
        this.textCounterPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.textPaint.setTextSize(AndroidUtilities.dp(15.0f));
        this.textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.deletePaint.setStyle(Paint.Style.STROKE);
        this.deletePaint.setStrokeCap(Paint.Cap.ROUND);
        this.deletePaint.setStrokeWidth(AndroidUtilities.dp(1.5f));
        float dpf2 = AndroidUtilities.dpf2(3.0f);
        this.selectorDrawable.setCornerRadii(new float[]{dpf2, dpf2, dpf2, dpf2, 0.0f, 0.0f, 0.0f, 0.0f});
        this.selectorDrawable.setColor(Theme.getColor(this.tabLineColorKey));
        setHorizontalScrollBarEnabled(false);
        RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.Components.FilterTabsView.3
            @Override // android.view.View
            public void setAlpha(float f) {
                super.setAlpha(f);
                FilterTabsView.this.invalidate();
            }

            @Override // org.telegram.ui.Components.RecyclerListView
            public boolean allowSelectChildAtPosition(View view) {
                return FilterTabsView.this.isEnabled() && FilterTabsView.this.delegate.canPerformActions();
            }

            @Override // org.telegram.ui.Components.RecyclerListView
            public boolean canHighlightChildAt(View view, float f, float f2) {
                if (FilterTabsView.this.isEditing) {
                    TabView tabView = (TabView) view;
                    float dp = AndroidUtilities.dp(6.0f);
                    if (tabView.rect.left - dp < f && tabView.rect.right + dp > f) {
                        return false;
                    }
                }
                return super.canHighlightChildAt(view, f, f2);
            }
        };
        this.listView = recyclerListView;
        recyclerListView.setClipChildren(false);
        AnonymousClass4 anonymousClass4 = new AnonymousClass4();
        this.itemAnimator = anonymousClass4;
        anonymousClass4.setDelayAnimations(false);
        this.listView.setItemAnimator(this.itemAnimator);
        this.listView.setSelectorType(8);
        this.listView.setSelectorRadius(6);
        this.listView.setSelectorDrawableColor(Theme.getColor(this.selectorColorKey));
        RecyclerListView recyclerListView2 = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 0, false) { // from class: org.telegram.ui.Components.FilterTabsView.5
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean supportsPredictiveItemAnimations() {
                return true;
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
                        int max = Math.max(180, calculateTimeForDeceleration((int) Math.sqrt((calculateDxToMakeVisible * calculateDxToMakeVisible) + (calculateDyToMakeVisible * calculateDyToMakeVisible))));
                        if (max > 0) {
                            action.update(-calculateDxToMakeVisible, -calculateDyToMakeVisible, max, this.mDecelerateInterpolator);
                        }
                    }
                };
                linearSmoothScroller.setTargetPosition(i);
                startSmoothScroll(linearSmoothScroller);
            }

            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public int scrollHorizontallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
                if (FilterTabsView.this.delegate.isTabMenuVisible()) {
                    i = 0;
                }
                return super.scrollHorizontallyBy(i, recycler, state);
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
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.Components.FilterTabsView$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i) {
                return RecyclerListView.OnItemClickListenerExtended.CC.$default$hasDoubleTap(this, view, i);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.CC.$default$onDoubleTap(this, view, i, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i, float f, float f2) {
                FilterTabsView.this.lambda$new$0(view, i, f, f2);
            }
        });
        this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.Components.FilterTabsView$$ExternalSyntheticLambda2
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

    /* renamed from: org.telegram.ui.Components.FilterTabsView$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends DefaultItemAnimator {
        AnonymousClass4() {
            FilterTabsView.this = r1;
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
                        FilterTabsView.AnonymousClass4.this.lambda$runPendingAnimations$0(valueAnimator);
                    }
                });
                ofFloat.setDuration(getMoveDuration());
                ofFloat.start();
            }
            super.runPendingAnimations();
        }

        public /* synthetic */ void lambda$runPendingAnimations$0(ValueAnimator valueAnimator) {
            FilterTabsView.this.listView.invalidate();
            FilterTabsView.this.invalidate();
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.SimpleItemAnimator
        public boolean animateMove(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, int i, int i2, int i3, int i4) {
            View view = viewHolder.itemView;
            if (view instanceof TabView) {
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
            return super.animateMove(viewHolder, itemHolderInfo, i, i2, i3, i4);
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator
        public void animateMoveImpl(RecyclerView.ViewHolder viewHolder, DefaultItemAnimator.MoveInfo moveInfo) {
            super.animateMoveImpl(viewHolder, moveInfo);
            View view = viewHolder.itemView;
            if (view instanceof TabView) {
                final TabView tabView = (TabView) view;
                if (!tabView.animateChange) {
                    return;
                }
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
                        FilterTabsView.AnonymousClass4.lambda$animateMoveImpl$1(FilterTabsView.TabView.this, valueAnimator2);
                    }
                });
                ofFloat.addListener(new AnimatorListenerAdapter(this) { // from class: org.telegram.ui.Components.FilterTabsView.4.1
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

        public static /* synthetic */ void lambda$animateMoveImpl$1(TabView tabView, ValueAnimator valueAnimator) {
            tabView.changeProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            tabView.invalidate();
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
        public void endAnimation(RecyclerView.ViewHolder viewHolder) {
            super.endAnimation(viewHolder);
            viewHolder.itemView.setTranslationX(0.0f);
            View view = viewHolder.itemView;
            if (view instanceof TabView) {
                ((TabView) view).clearTransitionParams();
            }
        }
    }

    public /* synthetic */ void lambda$new$0(View view, int i, float f, float f2) {
        FilterTabsViewDelegate filterTabsViewDelegate;
        if (!this.delegate.canPerformActions()) {
            return;
        }
        TabView tabView = (TabView) view;
        if (!this.isEditing) {
            if (i != this.currentPosition || (filterTabsViewDelegate = this.delegate) == null) {
                scrollToTab(tabView.currentTab, i);
            } else {
                filterTabsViewDelegate.onSamePageSelected();
            }
        } else if (i == 0) {
        } else {
            float dp = AndroidUtilities.dp(6.0f);
            if (tabView.rect.left - dp >= f || tabView.rect.right + dp <= f) {
                return;
            }
            this.delegate.onDeletePressed(tabView.currentTab.id);
        }
    }

    public /* synthetic */ boolean lambda$new$1(View view, int i) {
        if (this.delegate.canPerformActions() && !this.isEditing) {
            if (this.delegate.didSelectTab((TabView) view, i == this.currentPosition)) {
                this.listView.hideSelector(true);
                return true;
            }
        }
        return false;
    }

    public void setDelegate(FilterTabsViewDelegate filterTabsViewDelegate) {
        this.delegate = filterTabsViewDelegate;
    }

    public boolean isAnimatingIndicator() {
        return this.animatingIndicator;
    }

    private void scrollToTab(Tab tab, int i) {
        if (tab.isLocked) {
            FilterTabsViewDelegate filterTabsViewDelegate = this.delegate;
            if (filterTabsViewDelegate == null) {
                return;
            }
            filterTabsViewDelegate.onPageSelected(tab, false);
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
        scrollToTab(this.tabs.get(0), 0);
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

    public Drawable getSelectorDrawable() {
        return this.selectorDrawable;
    }

    public RecyclerListView getTabsContainer() {
        return this.listView;
    }

    public int getNextPageId(boolean z) {
        return this.positionToId.get(this.currentPosition + (z ? 1 : -1), -1);
    }

    public void removeTabs() {
        this.tabs.clear();
        this.positionToId.clear();
        this.idToPosition.clear();
        this.positionToWidth.clear();
        this.positionToX.clear();
        this.allTabsWidth = 0;
    }

    public void resetTabId() {
        this.selectedTabId = -1;
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

    public void finishAddingTabs(boolean z) {
        this.listView.setItemAnimator(z ? this.itemAnimator : null);
        this.adapter.notifyDataSetChanged();
    }

    public void animateColorsTo(String str, String str2, String str3, String str4, String str5) {
        AnimatorSet animatorSet = this.colorChangeAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        this.aTabLineColorKey = str;
        this.aActiveTextColorKey = str2;
        this.aUnactiveTextColorKey = str3;
        this.aBackgroundColorKey = str5;
        this.selectorColorKey = str4;
        this.listView.setSelectorDrawableColor(Theme.getColor(str4));
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.colorChangeAnimator = animatorSet2;
        animatorSet2.playTogether(ObjectAnimator.ofFloat(this, this.COLORS, 0.0f, 1.0f));
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
                FilterTabsView.this.aTabLineColorKey = null;
                FilterTabsView.this.aActiveTextColorKey = null;
                FilterTabsView.this.aUnactiveTextColorKey = null;
                FilterTabsView.this.aBackgroundColorKey = null;
            }
        });
        this.colorChangeAnimator.start();
    }

    public int getCurrentTabId() {
        return this.selectedTabId;
    }

    public int getFirstTabId() {
        return this.positionToId.get(0, 0);
    }

    public String getSelectorColorKey() {
        return this.selectorColorKey;
    }

    public void updateTabsWidths() {
        this.positionToX.clear();
        this.positionToWidth.clear();
        int dp = AndroidUtilities.dp(7.0f);
        int size = this.tabs.size();
        for (int i = 0; i < size; i++) {
            int width = this.tabs.get(i).getWidth(false);
            this.positionToWidth.put(i, width);
            this.positionToX.put(i, (this.additionalTabWidth / 2) + dp);
            dp += width + AndroidUtilities.dp(32.0f) + this.additionalTabWidth;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0108  */
    @Override // android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected boolean drawChild(Canvas canvas, View view, long j) {
        float f;
        float f2;
        RecyclerView.ViewHolder findViewHolderForAdapterPosition;
        int i;
        int i2;
        boolean drawChild = super.drawChild(canvas, view, j);
        if (view == this.listView) {
            int measuredHeight = getMeasuredHeight();
            this.selectorDrawable.setAlpha((int) (this.listView.getAlpha() * 255.0f));
            if (this.animatingIndicator || this.manualScrollingToPosition != -1) {
                int findFirstVisibleItemPosition = this.layoutManager.findFirstVisibleItemPosition();
                if (findFirstVisibleItemPosition != -1 && (findViewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(findFirstVisibleItemPosition)) != null) {
                    if (this.animatingIndicator) {
                        i2 = this.previousPosition;
                        i = this.currentPosition;
                    } else {
                        i2 = this.currentPosition;
                        i = this.manualScrollingToPosition;
                    }
                    int i3 = this.positionToX.get(i2);
                    int i4 = this.positionToX.get(i);
                    int i5 = this.positionToWidth.get(i2);
                    int i6 = this.positionToWidth.get(i);
                    if (this.additionalTabWidth != 0) {
                        f = ((int) (i3 + ((i4 - i3) * this.animatingIndicatorProgress))) + AndroidUtilities.dp(16.0f);
                    } else {
                        f = (((int) (i3 + ((i4 - i3) * this.animatingIndicatorProgress))) - (this.positionToX.get(findFirstVisibleItemPosition) - findViewHolderForAdapterPosition.itemView.getLeft())) + AndroidUtilities.dp(16.0f);
                    }
                    f2 = (int) (i5 + ((i6 - i5) * this.animatingIndicatorProgress));
                    if (f2 != 0.0f) {
                        canvas.save();
                        canvas.translate(this.listView.getTranslationX(), 0.0f);
                        canvas.scale(this.listView.getScaleX(), 1.0f, this.listView.getPivotX() + this.listView.getX(), this.listView.getPivotY());
                        this.selectorDrawable.setBounds((int) f, measuredHeight - AndroidUtilities.dpr(4.0f), (int) (f + f2), measuredHeight);
                        this.selectorDrawable.draw(canvas);
                        canvas.restore();
                    }
                }
                f = 0.0f;
                f2 = 0.0f;
                if (f2 != 0.0f) {
                }
            } else {
                RecyclerView.ViewHolder findViewHolderForAdapterPosition2 = this.listView.findViewHolderForAdapterPosition(this.currentPosition);
                if (findViewHolderForAdapterPosition2 != null) {
                    TabView tabView = (TabView) findViewHolderForAdapterPosition2.itemView;
                    f2 = Math.max(AndroidUtilities.dp(40.0f), tabView.animateTabWidth ? (tabView.animateFromTabWidth * (1.0f - tabView.changeProgress)) + (tabView.tabWidth * tabView.changeProgress) : tabView.tabWidth);
                    f = (int) (tabView.getX() + (((tabView.animateTabWidth ? (tabView.animateFromWidth * (1.0f - tabView.changeProgress)) + (tabView.getMeasuredWidth() * tabView.changeProgress) : tabView.getMeasuredWidth()) - f2) / 2.0f));
                    if (f2 != 0.0f) {
                    }
                }
                f = 0.0f;
                f2 = 0.0f;
                if (f2 != 0.0f) {
                }
            }
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long min = Math.min(17L, elapsedRealtime - this.lastEditingAnimationTime);
        this.lastEditingAnimationTime = elapsedRealtime;
        boolean z = this.isEditing;
        boolean z2 = false;
        boolean z3 = true;
        if (z || this.editingAnimationProgress != 0.0f) {
            if (this.editingForwardAnimation) {
                float f3 = this.editingAnimationProgress;
                boolean z4 = f3 <= 0.0f;
                float f4 = f3 + (((float) min) / 120.0f);
                this.editingAnimationProgress = f4;
                if (!z && z4 && f4 >= 0.0f) {
                    this.editingAnimationProgress = 0.0f;
                }
                if (this.editingAnimationProgress >= 1.0f) {
                    this.editingAnimationProgress = 1.0f;
                    this.editingForwardAnimation = false;
                }
            } else {
                float f5 = this.editingAnimationProgress;
                if (f5 >= 0.0f) {
                    z2 = true;
                }
                float f6 = f5 - (((float) min) / 120.0f);
                this.editingAnimationProgress = f6;
                if (!z && z2 && f6 <= 0.0f) {
                    this.editingAnimationProgress = 0.0f;
                }
                if (this.editingAnimationProgress <= -1.0f) {
                    this.editingAnimationProgress = -1.0f;
                    this.editingForwardAnimation = true;
                }
            }
            z2 = true;
        }
        if (z) {
            float f7 = this.editingStartAnimationProgress;
            if (f7 < 1.0f) {
                float f8 = f7 + (((float) min) / 180.0f);
                this.editingStartAnimationProgress = f8;
                if (f8 > 1.0f) {
                    this.editingStartAnimationProgress = 1.0f;
                }
            }
            z3 = z2;
        } else {
            if (!z) {
                float f9 = this.editingStartAnimationProgress;
                if (f9 > 0.0f) {
                    float f10 = f9 - (((float) min) / 180.0f);
                    this.editingStartAnimationProgress = f10;
                    if (f10 < 0.0f) {
                        this.editingStartAnimationProgress = 0.0f;
                    }
                }
            }
            z3 = z2;
        }
        if (z3) {
            this.listView.invalidateViews();
            invalidate();
        }
        return drawChild;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        if (!this.tabs.isEmpty()) {
            int size = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(7.0f)) - AndroidUtilities.dp(7.0f);
            Tab findDefaultTab = findDefaultTab();
            findDefaultTab.setTitle(LocaleController.getString("FilterAllChats", R.string.FilterAllChats));
            int width = findDefaultTab.getWidth(false);
            findDefaultTab.setTitle(this.allTabsWidth > size ? LocaleController.getString("FilterAllChatsShort", R.string.FilterAllChatsShort) : LocaleController.getString("FilterAllChats", R.string.FilterAllChats));
            int width2 = (this.allTabsWidth - width) + findDefaultTab.getWidth(false);
            int i3 = this.additionalTabWidth;
            int size2 = width2 < size ? (size - width2) / this.tabs.size() : 0;
            this.additionalTabWidth = size2;
            if (i3 != size2) {
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
        super.onMeasure(i, i2);
    }

    private Tab findDefaultTab() {
        for (int i = 0; i < this.tabs.size(); i++) {
            if (this.tabs.get(i).isDefault) {
                return this.tabs.get(i);
            }
        }
        return null;
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.ignoreLayout) {
            return;
        }
        super.requestLayout();
    }

    private void scrollToChild(int i) {
        if (this.tabs.isEmpty() || this.scrollingToChild == i || i < 0 || i >= this.tabs.size()) {
            return;
        }
        this.scrollingToChild = i;
        this.listView.smoothScrollToPosition(i);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        int i5 = i3 - i;
        if (this.prevLayoutWidth != i5) {
            this.prevLayoutWidth = i5;
            this.scrollingToChild = -1;
            if (!this.animatingIndicator) {
                return;
            }
            AndroidUtilities.cancelRunOnUIThread(this.animationRunnable);
            this.animatingIndicator = false;
            setEnabled(true);
            FilterTabsViewDelegate filterTabsViewDelegate = this.delegate;
            if (filterTabsViewDelegate == null) {
                return;
            }
            filterTabsViewDelegate.onPageScrolled(1.0f);
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
        if (f < 1.0f) {
            return;
        }
        this.manualScrollingToPosition = -1;
        this.manualScrollingToId = -1;
        this.currentPosition = i2;
        this.selectedTabId = i;
    }

    public boolean isEditing() {
        return this.isEditing;
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
        TLRPC$TL_messages_updateDialogFiltersOrder tLRPC$TL_messages_updateDialogFiltersOrder = new TLRPC$TL_messages_updateDialogFiltersOrder();
        ArrayList<MessagesController.DialogFilter> arrayList = MessagesController.getInstance(UserConfig.selectedAccount).dialogFilters;
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            MessagesController.DialogFilter dialogFilter = arrayList.get(i);
            if (dialogFilter.isDefault()) {
                tLRPC$TL_messages_updateDialogFiltersOrder.order.add(0);
            } else {
                tLRPC$TL_messages_updateDialogFiltersOrder.order.add(Integer.valueOf(dialogFilter.id));
            }
        }
        MessagesController.getInstance(UserConfig.selectedAccount).lockFiltersInternal();
        ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(tLRPC$TL_messages_updateDialogFiltersOrder, FilterTabsView$$ExternalSyntheticLambda0.INSTANCE);
        this.orderChanged = false;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void checkTabsCounter() {
        int size = this.tabs.size();
        boolean z = false;
        for (int i = 0; i < size; i++) {
            Tab tab = this.tabs.get(i);
            if (tab.counter != this.delegate.getTabCounter(tab.id) && this.delegate.getTabCounter(tab.id) >= 0) {
                if (this.positionToWidth.get(i) != tab.getWidth(true) || this.invalidated) {
                    this.invalidated = true;
                    requestLayout();
                    this.allTabsWidth = 0;
                    findDefaultTab().setTitle(LocaleController.getString("FilterAllChats", R.string.FilterAllChats));
                    for (int i2 = 0; i2 < size; i2++) {
                        this.allTabsWidth += this.tabs.get(i2).getWidth(true) + AndroidUtilities.dp(32.0f);
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

    public void notifyTabCounterChanged(int i) {
        int i2 = this.idToPosition.get(i, -1);
        if (i2 < 0 || i2 >= this.tabs.size()) {
            return;
        }
        Tab tab = this.tabs.get(i2);
        if (tab.counter == this.delegate.getTabCounter(tab.id) || this.delegate.getTabCounter(tab.id) < 0) {
            return;
        }
        this.listView.invalidateViews();
        if (this.positionToWidth.get(i2) == tab.getWidth(true) && !this.invalidated) {
            return;
        }
        this.invalidated = true;
        requestLayout();
        this.listView.setItemAnimator(this.itemAnimator);
        this.adapter.notifyDataSetChanged();
        this.allTabsWidth = 0;
        findDefaultTab().setTitle(LocaleController.getString("FilterAllChats", R.string.FilterAllChats));
        int size = this.tabs.size();
        for (int i3 = 0; i3 < size; i3++) {
            this.allTabsWidth += this.tabs.get(i3).getWidth(true) + AndroidUtilities.dp(32.0f);
        }
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        public ListAdapter(Context context) {
            FilterTabsView.this = r1;
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
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new RecyclerListView.Holder(new TabView(this.mContext));
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

        public void swapElements(int i, int i2) {
            int size = FilterTabsView.this.tabs.size();
            if (i < 0 || i2 < 0 || i >= size || i2 >= size) {
                return;
            }
            ArrayList<MessagesController.DialogFilter> arrayList = MessagesController.getInstance(UserConfig.selectedAccount).dialogFilters;
            MessagesController.DialogFilter dialogFilter = arrayList.get(i);
            MessagesController.DialogFilter dialogFilter2 = arrayList.get(i2);
            int i3 = dialogFilter.order;
            dialogFilter.order = dialogFilter2.order;
            dialogFilter2.order = i3;
            arrayList.set(i, dialogFilter2);
            arrayList.set(i2, dialogFilter);
            Tab tab = (Tab) FilterTabsView.this.tabs.get(i);
            Tab tab2 = (Tab) FilterTabsView.this.tabs.get(i2);
            int i4 = tab.id;
            tab.id = tab2.id;
            tab2.id = i4;
            int i5 = FilterTabsView.this.positionToStableId.get(i);
            FilterTabsView.this.positionToStableId.put(i, FilterTabsView.this.positionToStableId.get(i2));
            FilterTabsView.this.positionToStableId.put(i2, i5);
            FilterTabsView.this.delegate.onPageReorder(tab2.id, tab.id);
            if (FilterTabsView.this.currentPosition == i) {
                FilterTabsView.this.currentPosition = i2;
                FilterTabsView.this.selectedTabId = tab.id;
            } else if (FilterTabsView.this.currentPosition == i2) {
                FilterTabsView.this.currentPosition = i;
                FilterTabsView.this.selectedTabId = tab2.id;
            }
            if (FilterTabsView.this.previousPosition == i) {
                FilterTabsView.this.previousPosition = i2;
                FilterTabsView.this.previousId = tab.id;
            } else if (FilterTabsView.this.previousPosition == i2) {
                FilterTabsView.this.previousPosition = i;
                FilterTabsView.this.previousId = tab2.id;
            }
            FilterTabsView.this.tabs.set(i, tab2);
            FilterTabsView.this.tabs.set(i2, tab);
            FilterTabsView.this.updateTabsWidths();
            FilterTabsView.this.orderChanged = true;
            FilterTabsView.this.listView.setItemAnimator(FilterTabsView.this.itemAnimator);
            notifyItemMoved(i, i2);
        }
    }

    /* loaded from: classes3.dex */
    public class TouchHelperCallback extends ItemTouchHelper.Callback {
        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        }

        public TouchHelperCallback() {
            FilterTabsView.this = r1;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean isLongPressDragEnabled() {
            return FilterTabsView.this.isEditing;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (!FilterTabsView.this.isEditing || (viewHolder.getAdapterPosition() == 0 && ((Tab) FilterTabsView.this.tabs.get(0)).isDefault && !UserConfig.getInstance(UserConfig.selectedAccount).isPremium())) {
                return ItemTouchHelper.Callback.makeMovementFlags(0, 0);
            }
            return ItemTouchHelper.Callback.makeMovementFlags(12, 0);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
            if ((viewHolder.getAdapterPosition() == 0 || viewHolder2.getAdapterPosition() == 0) && !UserConfig.getInstance(UserConfig.selectedAccount).isPremium()) {
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
            }
            super.onSelectedChanged(viewHolder, i);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
            viewHolder.itemView.setBackground(null);
        }
    }

    public RecyclerListView getListView() {
        return this.listView;
    }

    public boolean currentTabIsDefault() {
        Tab findDefaultTab = findDefaultTab();
        return findDefaultTab != null && findDefaultTab.id == this.selectedTabId;
    }

    public int getDefaultTabId() {
        Tab findDefaultTab = findDefaultTab();
        if (findDefaultTab == null) {
            return -1;
        }
        return findDefaultTab.id;
    }

    public boolean isEmpty() {
        return this.tabs.isEmpty();
    }

    public boolean isFirstTabSelected() {
        return this.tabs.isEmpty() || this.selectedTabId == this.tabs.get(0).id;
    }

    public boolean isLocked(int i) {
        for (int i2 = 0; i2 < this.tabs.size(); i2++) {
            if (this.tabs.get(i2).id == i) {
                return this.tabs.get(i2).isLocked;
            }
        }
        return false;
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
}
