package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Property;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import androidx.annotation.Keep;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PopupSwipeBackLayout;
/* loaded from: classes3.dex */
public class ActionBarPopupWindow extends PopupWindow {
    private static final ViewTreeObserver.OnScrollChangedListener NOP;
    private static final boolean allowAnimation;
    private static DecelerateInterpolator decelerateInterpolator;
    private static Method layoutInScreenMethod;
    private static final Field superListenerField;
    private boolean animationEnabled;
    private int currentAccount;
    private int dismissAnimationDuration;
    private boolean isClosingAnimated;
    private ViewTreeObserver.OnScrollChangedListener mSuperScrollListener;
    private ViewTreeObserver mViewTreeObserver;
    private long outEmptyTime;
    private boolean pauseNotifications;
    private int popupAnimationIndex;
    private boolean scaleOut;
    private AnimatorSet windowAnimatorSet;

    /* loaded from: classes3.dex */
    public interface OnDispatchKeyEventListener {
        void onDispatchKeyEvent(KeyEvent keyEvent);
    }

    /* loaded from: classes3.dex */
    public interface onSizeChangedListener {
        void onSizeChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$static$0() {
    }

    static {
        allowAnimation = Build.VERSION.SDK_INT >= 18;
        decelerateInterpolator = new DecelerateInterpolator();
        Field field = null;
        try {
            field = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
            field.setAccessible(true);
        } catch (NoSuchFieldException unused) {
        }
        superListenerField = field;
        NOP = ActionBarPopupWindow$$ExternalSyntheticLambda2.INSTANCE;
    }

    public void setScaleOut(boolean z) {
        this.scaleOut = z;
    }

    /* loaded from: classes3.dex */
    public static class ActionBarPopupWindowLayout extends FrameLayout {
        private boolean animationEnabled;
        private int backAlpha;
        private float backScaleX;
        private float backScaleY;
        private int backgroundColor;
        protected Drawable backgroundDrawable;
        private Rect bgPaddings;
        private boolean fitItems;
        private int gapEndY;
        private int gapStartY;
        private ArrayList<AnimatorSet> itemAnimators;
        private int lastStartedChild;
        protected LinearLayout linearLayout;
        private OnDispatchKeyEventListener mOnDispatchKeyEventListener;
        private onSizeChangedListener onSizeChangedListener;
        private HashMap<View, Integer> positions;
        private float reactionsEnterProgress;
        Rect rect;
        private final Theme.ResourcesProvider resourcesProvider;
        private ScrollView scrollView;
        public boolean shownFromBottom;
        private boolean startAnimationPending;
        public int subtractBackgroundHeight;
        public boolean swipeBackGravityRight;
        private PopupSwipeBackLayout swipeBackLayout;
        private View topView;
        public boolean updateAnimation;
        protected ActionBarPopupWindow window;

        public ActionBarPopupWindowLayout(Context context) {
            this(context, null);
        }

        public ActionBarPopupWindowLayout(Context context, Theme.ResourcesProvider resourcesProvider) {
            this(context, R.drawable.popup_fixed_alert2, resourcesProvider);
        }

        public ActionBarPopupWindowLayout(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
            this(context, i, resourcesProvider, 0);
        }

        public ActionBarPopupWindowLayout(Context context, int i, Theme.ResourcesProvider resourcesProvider, int i2) {
            super(context);
            this.backScaleX = 1.0f;
            this.backScaleY = 1.0f;
            this.startAnimationPending = false;
            this.backAlpha = 255;
            this.lastStartedChild = 0;
            this.animationEnabled = ActionBarPopupWindow.allowAnimation;
            this.positions = new HashMap<>();
            this.gapStartY = -1000000;
            this.gapEndY = -1000000;
            this.bgPaddings = new Rect();
            this.reactionsEnterProgress = 1.0f;
            this.backgroundColor = -1;
            this.resourcesProvider = resourcesProvider;
            if (i != 0) {
                this.backgroundDrawable = getResources().getDrawable(i).mutate();
                setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
            }
            Drawable drawable = this.backgroundDrawable;
            if (drawable != null) {
                drawable.getPadding(this.bgPaddings);
                setBackgroundColor(getThemedColor("actionBarDefaultSubmenuBackground"));
            }
            setWillNotDraw(false);
            if ((i2 & 2) > 0) {
                this.shownFromBottom = true;
            }
            if ((i2 & 1) > 0) {
                PopupSwipeBackLayout popupSwipeBackLayout = new PopupSwipeBackLayout(context, resourcesProvider);
                this.swipeBackLayout = popupSwipeBackLayout;
                addView(popupSwipeBackLayout, LayoutHelper.createFrame(-2, -2.0f));
            }
            try {
                ScrollView scrollView = new ScrollView(context);
                this.scrollView = scrollView;
                scrollView.setVerticalScrollBarEnabled(false);
                PopupSwipeBackLayout popupSwipeBackLayout2 = this.swipeBackLayout;
                if (popupSwipeBackLayout2 != null) {
                    popupSwipeBackLayout2.addView(this.scrollView, LayoutHelper.createFrame(-2, -2, this.shownFromBottom ? 80 : 48));
                } else {
                    addView(this.scrollView, LayoutHelper.createFrame(-2, -2.0f));
                }
            } catch (Throwable th) {
                FileLog.e(th);
            }
            LinearLayout linearLayout = new LinearLayout(context) { // from class: org.telegram.ui.ActionBar.ActionBarPopupWindow.ActionBarPopupWindowLayout.1
                @Override // android.widget.LinearLayout, android.view.View
                protected void onMeasure(int i3, int i4) {
                    if (ActionBarPopupWindowLayout.this.fitItems) {
                        ActionBarPopupWindowLayout.this.gapStartY = -1000000;
                        ActionBarPopupWindowLayout.this.gapEndY = -1000000;
                        int childCount = getChildCount();
                        ArrayList arrayList = null;
                        int i5 = 0;
                        int i6 = 0;
                        for (int i7 = 0; i7 < childCount; i7++) {
                            View childAt = getChildAt(i7);
                            if (childAt.getVisibility() != 8) {
                                Object tag = childAt.getTag(R.id.width_tag);
                                Object tag2 = childAt.getTag(R.id.object_tag);
                                Object tag3 = childAt.getTag(R.id.fit_width_tag);
                                if (tag != null) {
                                    childAt.getLayoutParams().width = -2;
                                }
                                measureChildWithMargins(childAt, i3, 0, i4, 0);
                                if (tag3 == null) {
                                    boolean z = tag instanceof Integer;
                                    if (!z && tag2 == null) {
                                        i5 = Math.max(i5, childAt.getMeasuredWidth());
                                    } else if (z) {
                                        int max = Math.max(((Integer) tag).intValue(), childAt.getMeasuredWidth());
                                        ActionBarPopupWindowLayout.this.gapStartY = childAt.getMeasuredHeight();
                                        ActionBarPopupWindowLayout actionBarPopupWindowLayout = ActionBarPopupWindowLayout.this;
                                        actionBarPopupWindowLayout.gapEndY = actionBarPopupWindowLayout.gapStartY + AndroidUtilities.dp(6.0f);
                                        i6 = max;
                                    }
                                }
                                if (arrayList == null) {
                                    arrayList = new ArrayList();
                                }
                                arrayList.add(childAt);
                            }
                        }
                        if (arrayList != null) {
                            int size = arrayList.size();
                            for (int i8 = 0; i8 < size; i8++) {
                                ((View) arrayList.get(i8)).getLayoutParams().width = Math.max(i5, i6);
                            }
                        }
                    }
                    super.onMeasure(i3, i4);
                }

                @Override // android.view.ViewGroup
                protected boolean drawChild(Canvas canvas, View view, long j) {
                    if (view instanceof GapView) {
                        return false;
                    }
                    return super.drawChild(canvas, view, j);
                }
            };
            this.linearLayout = linearLayout;
            linearLayout.setOrientation(1);
            ScrollView scrollView2 = this.scrollView;
            if (scrollView2 != null) {
                scrollView2.addView(this.linearLayout, new FrameLayout.LayoutParams(-2, -2));
                return;
            }
            PopupSwipeBackLayout popupSwipeBackLayout3 = this.swipeBackLayout;
            if (popupSwipeBackLayout3 != null) {
                popupSwipeBackLayout3.addView(this.linearLayout, LayoutHelper.createFrame(-2, -2, this.shownFromBottom ? 80 : 48));
            } else {
                addView(this.linearLayout, LayoutHelper.createFrame(-2, -2.0f));
            }
        }

        public PopupSwipeBackLayout getSwipeBack() {
            return this.swipeBackLayout;
        }

        public int addViewToSwipeBack(View view) {
            this.swipeBackLayout.addView(view, LayoutHelper.createFrame(-2, -2, this.shownFromBottom ? 80 : 48));
            return this.swipeBackLayout.getChildCount() - 1;
        }

        public void setFitItems(boolean z) {
            this.fitItems = z;
        }

        public void setShownFromBottom(boolean z) {
            this.shownFromBottom = z;
        }

        public void setDispatchKeyEventListener(OnDispatchKeyEventListener onDispatchKeyEventListener) {
            this.mOnDispatchKeyEventListener = onDispatchKeyEventListener;
        }

        public int getBackgroundColor() {
            return this.backgroundColor;
        }

        @Override // android.view.View
        public void setBackgroundColor(int i) {
            Drawable drawable;
            if (this.backgroundColor == i || (drawable = this.backgroundDrawable) == null) {
                return;
            }
            this.backgroundColor = i;
            drawable.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
        }

        @Keep
        public void setBackAlpha(int i) {
            this.backAlpha = i;
        }

        @Keep
        public int getBackAlpha() {
            return this.backAlpha;
        }

        @Keep
        public void setBackScaleX(float f) {
            if (this.backScaleX != f) {
                this.backScaleX = f;
                invalidate();
                onSizeChangedListener onsizechangedlistener = this.onSizeChangedListener;
                if (onsizechangedlistener != null) {
                    onsizechangedlistener.onSizeChanged();
                }
            }
        }

        @Keep
        public void setBackScaleY(float f) {
            Integer num;
            if (this.backScaleY != f) {
                this.backScaleY = f;
                if (this.animationEnabled && this.updateAnimation) {
                    int measuredHeight = getMeasuredHeight() - AndroidUtilities.dp(16.0f);
                    if (this.shownFromBottom) {
                        for (int i = this.lastStartedChild; i >= 0; i--) {
                            View itemAt = getItemAt(i);
                            if (itemAt != null && itemAt.getVisibility() == 0 && !(itemAt instanceof GapView)) {
                                if (this.positions.get(itemAt) != null && measuredHeight - ((num.intValue() * AndroidUtilities.dp(48.0f)) + AndroidUtilities.dp(32.0f)) > measuredHeight * f) {
                                    break;
                                }
                                this.lastStartedChild = i - 1;
                                startChildAnimation(itemAt);
                            }
                        }
                    } else {
                        int itemsCount = getItemsCount();
                        int i2 = 0;
                        for (int i3 = 0; i3 < itemsCount; i3++) {
                            View itemAt2 = getItemAt(i3);
                            if (itemAt2.getVisibility() == 0) {
                                i2 += itemAt2.getMeasuredHeight();
                                if (i3 < this.lastStartedChild) {
                                    continue;
                                } else if (this.positions.get(itemAt2) != null && i2 - AndroidUtilities.dp(24.0f) > measuredHeight * f) {
                                    break;
                                } else {
                                    this.lastStartedChild = i3 + 1;
                                    startChildAnimation(itemAt2);
                                }
                            }
                        }
                    }
                }
                invalidate();
                onSizeChangedListener onsizechangedlistener = this.onSizeChangedListener;
                if (onsizechangedlistener != null) {
                    onsizechangedlistener.onSizeChanged();
                }
            }
        }

        @Override // android.view.View
        public void setBackgroundDrawable(Drawable drawable) {
            this.backgroundColor = -1;
            this.backgroundDrawable = drawable;
            if (drawable != null) {
                drawable.getPadding(this.bgPaddings);
            }
        }

        private void startChildAnimation(final View view) {
            if (this.animationEnabled) {
                final AnimatorSet animatorSet = new AnimatorSet();
                Animator[] animatorArr = new Animator[2];
                Property property = View.ALPHA;
                float[] fArr = new float[2];
                fArr[0] = 0.0f;
                fArr[1] = view.isEnabled() ? 1.0f : 0.5f;
                animatorArr[0] = ObjectAnimator.ofFloat(view, property, fArr);
                Property property2 = View.TRANSLATION_Y;
                float[] fArr2 = new float[2];
                fArr2[0] = AndroidUtilities.dp(this.shownFromBottom ? 6.0f : -6.0f);
                fArr2[1] = 0.0f;
                animatorArr[1] = ObjectAnimator.ofFloat(view, property2, fArr2);
                animatorSet.playTogether(animatorArr);
                animatorSet.setDuration(180L);
                animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.ActionBarPopupWindow.ActionBarPopupWindowLayout.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        ActionBarPopupWindowLayout.this.itemAnimators.remove(animatorSet);
                        View view2 = view;
                        if (view2 instanceof ActionBarMenuSubItem) {
                            ((ActionBarMenuSubItem) view2).onItemShown();
                        }
                    }
                });
                animatorSet.setInterpolator(ActionBarPopupWindow.decelerateInterpolator);
                animatorSet.start();
                if (this.itemAnimators == null) {
                    this.itemAnimators = new ArrayList<>();
                }
                this.itemAnimators.add(animatorSet);
            }
        }

        public void setAnimationEnabled(boolean z) {
            this.animationEnabled = z;
        }

        @Override // android.view.ViewGroup
        public void addView(View view) {
            this.linearLayout.addView(view);
        }

        public void addView(View view, LinearLayout.LayoutParams layoutParams) {
            this.linearLayout.addView(view, layoutParams);
        }

        public int getViewsCount() {
            return this.linearLayout.getChildCount();
        }

        public void removeInnerViews() {
            this.linearLayout.removeAllViews();
        }

        public float getBackScaleX() {
            return this.backScaleX;
        }

        public float getBackScaleY() {
            return this.backScaleY;
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            OnDispatchKeyEventListener onDispatchKeyEventListener = this.mOnDispatchKeyEventListener;
            if (onDispatchKeyEventListener != null) {
                onDispatchKeyEventListener.onDispatchKeyEvent(keyEvent);
            }
            return super.dispatchKeyEvent(keyEvent);
        }

        /* JADX WARN: Removed duplicated region for block: B:104:0x0256 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:38:0x00f9  */
        /* JADX WARN: Removed duplicated region for block: B:39:0x00fc  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x0105  */
        /* JADX WARN: Removed duplicated region for block: B:43:0x0122  */
        /* JADX WARN: Removed duplicated region for block: B:67:0x01b2  */
        /* JADX WARN: Removed duplicated region for block: B:73:0x01dd  */
        /* JADX WARN: Removed duplicated region for block: B:88:0x0253  */
        @Override // android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void dispatchDraw(Canvas canvas) {
            int i;
            boolean z;
            int i2;
            int i3;
            boolean z2;
            boolean z3;
            if (this.swipeBackGravityRight) {
                setTranslationX(getMeasuredWidth() * (1.0f - this.backScaleX));
                View view = this.topView;
                if (view != null) {
                    view.setTranslationX(getMeasuredWidth() * (1.0f - this.backScaleX));
                    this.topView.setAlpha(1.0f - this.swipeBackLayout.transitionProgress);
                    float f = (-(this.topView.getMeasuredHeight() - AndroidUtilities.dp(16.0f))) * this.swipeBackLayout.transitionProgress;
                    this.topView.setTranslationY(f);
                    setTranslationY(f);
                }
            }
            if (this.backgroundDrawable != null) {
                int scrollY = this.gapStartY - this.scrollView.getScrollY();
                int scrollY2 = this.gapEndY - this.scrollView.getScrollY();
                int i4 = 0;
                while (true) {
                    if (i4 >= this.linearLayout.getChildCount()) {
                        z = false;
                        break;
                    } else if ((this.linearLayout.getChildAt(i4) instanceof GapView) && this.linearLayout.getChildAt(i4).getVisibility() == 0) {
                        z = true;
                        break;
                    } else {
                        i4++;
                    }
                }
                int i5 = 0;
                for (i = 1; i5 < 2 && (i5 != i || scrollY >= (-AndroidUtilities.dp(16.0f))); i = 1) {
                    if (z && this.backAlpha != 255) {
                        i3 = -1000000;
                        i2 = i5;
                        canvas.saveLayerAlpha(0.0f, this.bgPaddings.top, getMeasuredWidth(), getMeasuredHeight(), this.backAlpha, 31);
                        z2 = false;
                    } else {
                        i2 = i5;
                        i3 = -1000000;
                        if (this.gapStartY != -1000000) {
                            canvas.save();
                            canvas.clipRect(0, this.bgPaddings.top, getMeasuredWidth(), getMeasuredHeight());
                            z2 = true;
                        } else {
                            z2 = true;
                            z3 = false;
                            this.backgroundDrawable.setAlpha(!z2 ? this.backAlpha : 255);
                            if (!this.shownFromBottom) {
                                int measuredHeight = getMeasuredHeight();
                                AndroidUtilities.rectTmp2.set(0, (int) (measuredHeight * (1.0f - this.backScaleY)), (int) (getMeasuredWidth() * this.backScaleX), measuredHeight);
                            } else if (scrollY > (-AndroidUtilities.dp(16.0f))) {
                                int measuredHeight2 = (int) (getMeasuredHeight() * this.backScaleY);
                                if (i2 == 0) {
                                    Rect rect = AndroidUtilities.rectTmp2;
                                    int dp = (-this.scrollView.getScrollY()) + (this.gapStartY != i3 ? AndroidUtilities.dp(1.0f) : 0);
                                    int measuredWidth = (int) (getMeasuredWidth() * this.backScaleX);
                                    if (this.gapStartY != i3) {
                                        measuredHeight2 = Math.min(measuredHeight2, AndroidUtilities.dp(16.0f) + scrollY);
                                    }
                                    rect.set(0, dp, measuredWidth, measuredHeight2 - this.subtractBackgroundHeight);
                                } else if (measuredHeight2 < scrollY2) {
                                    if (this.gapStartY != i3) {
                                        canvas.restore();
                                    }
                                    i5 = i2 + 1;
                                } else {
                                    AndroidUtilities.rectTmp2.set(0, scrollY2, (int) (getMeasuredWidth() * this.backScaleX), measuredHeight2 - this.subtractBackgroundHeight);
                                }
                            } else {
                                AndroidUtilities.rectTmp2.set(0, this.gapStartY < 0 ? 0 : -AndroidUtilities.dp(16.0f), (int) (getMeasuredWidth() * this.backScaleX), ((int) (getMeasuredHeight() * this.backScaleY)) - this.subtractBackgroundHeight);
                            }
                            if (this.reactionsEnterProgress != 1.0f) {
                                if (this.rect == null) {
                                    this.rect = new Rect();
                                }
                                Rect rect2 = this.rect;
                                Rect rect3 = AndroidUtilities.rectTmp2;
                                int i6 = rect3.right;
                                int i7 = rect3.top;
                                rect2.set(i6, i7, i6, i7);
                                AndroidUtilities.lerp(this.rect, rect3, this.reactionsEnterProgress, rect3);
                            }
                            Drawable drawable = this.backgroundDrawable;
                            Rect rect4 = AndroidUtilities.rectTmp2;
                            drawable.setBounds(rect4);
                            this.backgroundDrawable.draw(canvas);
                            if (z) {
                                canvas.save();
                                rect4.set(this.backgroundDrawable.getBounds());
                                rect4.inset(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                                canvas.clipRect(rect4);
                                for (int i8 = 0; i8 < this.linearLayout.getChildCount(); i8++) {
                                    if ((this.linearLayout.getChildAt(i8) instanceof GapView) && this.linearLayout.getChildAt(i8).getVisibility() == 0) {
                                        canvas.save();
                                        GapView gapView = (GapView) this.linearLayout.getChildAt(i8);
                                        float f2 = 0.0f;
                                        View view2 = gapView;
                                        float f3 = 0.0f;
                                        while (view2 != this) {
                                            f2 += view2.getX();
                                            f3 += view2.getY();
                                            view2 = (View) view2.getParent();
                                            if (view2 == null) {
                                                break;
                                            }
                                        }
                                        canvas.translate(f2, f3 * this.scrollView.getScaleY());
                                        gapView.draw(canvas);
                                        canvas.restore();
                                    }
                                }
                                canvas.restore();
                            }
                            if (!z3) {
                                canvas.restore();
                            }
                            i5 = i2 + 1;
                        }
                    }
                    z3 = true;
                    this.backgroundDrawable.setAlpha(!z2 ? this.backAlpha : 255);
                    if (!this.shownFromBottom) {
                    }
                    if (this.reactionsEnterProgress != 1.0f) {
                    }
                    Drawable drawable2 = this.backgroundDrawable;
                    Rect rect42 = AndroidUtilities.rectTmp2;
                    drawable2.setBounds(rect42);
                    this.backgroundDrawable.draw(canvas);
                    if (z) {
                    }
                    if (!z3) {
                    }
                    i5 = i2 + 1;
                }
            }
            float f4 = this.reactionsEnterProgress;
            if (f4 != 1.0f) {
                Rect rect5 = AndroidUtilities.rectTmp2;
                canvas.saveLayerAlpha(rect5.left, rect5.top, rect5.right, rect5.bottom, (int) (f4 * 255.0f), 31);
                float f5 = (this.reactionsEnterProgress * 0.5f) + 0.5f;
                canvas.scale(f5, f5, rect5.right, rect5.top);
                super.dispatchDraw(canvas);
                canvas.restore();
                return;
            }
            super.dispatchDraw(canvas);
        }

        public Drawable getBackgroundDrawable() {
            return this.backgroundDrawable;
        }

        public int getItemsCount() {
            return this.linearLayout.getChildCount();
        }

        public View getItemAt(int i) {
            return this.linearLayout.getChildAt(i);
        }

        public void scrollToTop() {
            ScrollView scrollView = this.scrollView;
            if (scrollView != null) {
                scrollView.scrollTo(0, 0);
            }
        }

        public void setupRadialSelectors(int i) {
            int childCount = this.linearLayout.getChildCount();
            int i2 = 0;
            while (i2 < childCount) {
                View childAt = this.linearLayout.getChildAt(i2);
                int i3 = 6;
                int i4 = i2 == 0 ? 6 : 0;
                if (i2 != childCount - 1) {
                    i3 = 0;
                }
                childAt.setBackground(Theme.createRadSelectorDrawable(i, i4, i3));
                i2++;
            }
        }

        public void updateRadialSelectors() {
            int childCount = this.linearLayout.getChildCount();
            View view = null;
            View view2 = null;
            for (int i = 0; i < childCount; i++) {
                View childAt = this.linearLayout.getChildAt(i);
                if (childAt.getVisibility() == 0) {
                    if (view == null) {
                        view = childAt;
                    }
                    view2 = childAt;
                }
            }
            boolean z = false;
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt2 = this.linearLayout.getChildAt(i2);
                if (childAt2.getVisibility() == 0) {
                    Object tag = childAt2.getTag(R.id.object_tag);
                    if (childAt2 instanceof ActionBarMenuSubItem) {
                        ((ActionBarMenuSubItem) childAt2).updateSelectorBackground(childAt2 == view || z, childAt2 == view2);
                    }
                    z = tag != null;
                }
            }
        }

        private int getThemedColor(String str) {
            Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
            Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
            return color != null ? color.intValue() : Theme.getColor(str);
        }

        public void setOnSizeChangedListener(onSizeChangedListener onsizechangedlistener) {
            this.onSizeChangedListener = onsizechangedlistener;
        }

        public int getVisibleHeight() {
            return (int) (getMeasuredHeight() * this.backScaleY);
        }

        public void setTopView(View view) {
            this.topView = view;
        }

        public void setSwipeBackForegroundColor(int i) {
            getSwipeBack().setForegroundColor(i);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.widget.FrameLayout, android.view.View
        public void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            PopupSwipeBackLayout popupSwipeBackLayout = this.swipeBackLayout;
            if (popupSwipeBackLayout != null) {
                popupSwipeBackLayout.invalidateTransforms(!this.startAnimationPending);
            }
        }

        public void setParentWindow(ActionBarPopupWindow actionBarPopupWindow) {
            this.window = actionBarPopupWindow;
        }

        public void setReactionsTransitionProgress(float f) {
            this.reactionsEnterProgress = f;
            invalidate();
        }
    }

    public ActionBarPopupWindow() {
        this.animationEnabled = allowAnimation;
        this.dismissAnimationDuration = ImageReceiver.DEFAULT_CROSSFADE_DURATION;
        this.currentAccount = UserConfig.selectedAccount;
        this.outEmptyTime = -1L;
        this.popupAnimationIndex = -1;
        init();
    }

    public ActionBarPopupWindow(Context context) {
        super(context);
        this.animationEnabled = allowAnimation;
        this.dismissAnimationDuration = ImageReceiver.DEFAULT_CROSSFADE_DURATION;
        this.currentAccount = UserConfig.selectedAccount;
        this.outEmptyTime = -1L;
        this.popupAnimationIndex = -1;
        init();
    }

    public ActionBarPopupWindow(View view, int i, int i2) {
        super(view, i, i2);
        this.animationEnabled = allowAnimation;
        this.dismissAnimationDuration = ImageReceiver.DEFAULT_CROSSFADE_DURATION;
        this.currentAccount = UserConfig.selectedAccount;
        this.outEmptyTime = -1L;
        this.popupAnimationIndex = -1;
        init();
    }

    public void setAnimationEnabled(boolean z) {
        this.animationEnabled = z;
    }

    public void setLayoutInScreen(boolean z) {
        try {
            if (layoutInScreenMethod == null) {
                Method declaredMethod = PopupWindow.class.getDeclaredMethod("setLayoutInScreenEnabled", Boolean.TYPE);
                layoutInScreenMethod = declaredMethod;
                declaredMethod.setAccessible(true);
            }
            layoutInScreenMethod.invoke(this, Boolean.TRUE);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private void init() {
        View contentView = getContentView();
        if (contentView instanceof ActionBarPopupWindowLayout) {
            ActionBarPopupWindowLayout actionBarPopupWindowLayout = (ActionBarPopupWindowLayout) contentView;
            if (actionBarPopupWindowLayout.getSwipeBack() != null) {
                actionBarPopupWindowLayout.getSwipeBack().setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ActionBar.ActionBarPopupWindow$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ActionBarPopupWindow.this.lambda$init$1(view);
                    }
                });
            }
        }
        Field field = superListenerField;
        if (field != null) {
            try {
                this.mSuperScrollListener = (ViewTreeObserver.OnScrollChangedListener) field.get(this);
                field.set(this, NOP);
            } catch (Exception unused) {
                this.mSuperScrollListener = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$1(View view) {
        dismiss();
    }

    public void setDismissAnimationDuration(int i) {
        this.dismissAnimationDuration = i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterListener() {
        ViewTreeObserver viewTreeObserver;
        if (this.mSuperScrollListener == null || (viewTreeObserver = this.mViewTreeObserver) == null) {
            return;
        }
        if (viewTreeObserver.isAlive()) {
            this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
        }
        this.mViewTreeObserver = null;
    }

    private void registerListener(View view) {
        if (this.mSuperScrollListener != null) {
            ViewTreeObserver viewTreeObserver = view.getWindowToken() != null ? view.getViewTreeObserver() : null;
            ViewTreeObserver viewTreeObserver2 = this.mViewTreeObserver;
            if (viewTreeObserver != viewTreeObserver2) {
                if (viewTreeObserver2 != null && viewTreeObserver2.isAlive()) {
                    this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
                }
                this.mViewTreeObserver = viewTreeObserver;
                if (viewTreeObserver != null) {
                    viewTreeObserver.addOnScrollChangedListener(this.mSuperScrollListener);
                }
            }
        }
    }

    public void dimBehind() {
        dimBehind(0.2f);
    }

    public void dimBehind(float f) {
        View rootView = getContentView().getRootView();
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) rootView.getLayoutParams();
        layoutParams.flags |= 2;
        layoutParams.dimAmount = f;
        ((WindowManager) getContentView().getContext().getSystemService("window")).updateViewLayout(rootView, layoutParams);
    }

    private void dismissDim() {
        View rootView = getContentView().getRootView();
        WindowManager windowManager = (WindowManager) getContentView().getContext().getSystemService("window");
        if (rootView.getLayoutParams() == null || !(rootView.getLayoutParams() instanceof WindowManager.LayoutParams)) {
            return;
        }
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) rootView.getLayoutParams();
        try {
            int i = layoutParams.flags;
            if ((i & 2) != 0) {
                layoutParams.flags = i & (-3);
                layoutParams.dimAmount = 0.0f;
                windowManager.updateViewLayout(rootView, layoutParams);
            }
        } catch (Exception unused) {
        }
    }

    @Override // android.widget.PopupWindow
    public void showAsDropDown(View view, int i, int i2) {
        try {
            super.showAsDropDown(view, i, i2);
            registerListener(view);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static AnimatorSet startAnimation(final ActionBarPopupWindowLayout actionBarPopupWindowLayout) {
        actionBarPopupWindowLayout.startAnimationPending = true;
        actionBarPopupWindowLayout.setTranslationY(0.0f);
        float f = 1.0f;
        actionBarPopupWindowLayout.setAlpha(1.0f);
        actionBarPopupWindowLayout.setPivotX(actionBarPopupWindowLayout.getMeasuredWidth());
        actionBarPopupWindowLayout.setPivotY(0.0f);
        int itemsCount = actionBarPopupWindowLayout.getItemsCount();
        actionBarPopupWindowLayout.positions.clear();
        int i = 0;
        for (int i2 = 0; i2 < itemsCount; i2++) {
            View itemAt = actionBarPopupWindowLayout.getItemAt(i2);
            if (!(itemAt instanceof GapView)) {
                itemAt.setAlpha(0.0f);
                if (itemAt.getVisibility() == 0) {
                    actionBarPopupWindowLayout.positions.put(itemAt, Integer.valueOf(i));
                    i++;
                }
            }
        }
        if (actionBarPopupWindowLayout.shownFromBottom) {
            actionBarPopupWindowLayout.lastStartedChild = itemsCount - 1;
        } else {
            actionBarPopupWindowLayout.lastStartedChild = 0;
        }
        if (actionBarPopupWindowLayout.getSwipeBack() != null) {
            actionBarPopupWindowLayout.getSwipeBack().invalidateTransforms();
            f = actionBarPopupWindowLayout.backScaleY;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.ActionBarPopupWindow$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ActionBarPopupWindow.lambda$startAnimation$2(ActionBarPopupWindow.ActionBarPopupWindowLayout.this, valueAnimator);
            }
        });
        actionBarPopupWindowLayout.updateAnimation = true;
        animatorSet.playTogether(ObjectAnimator.ofFloat(actionBarPopupWindowLayout, "backScaleY", 0.0f, f), ObjectAnimator.ofInt(actionBarPopupWindowLayout, "backAlpha", 0, 255), ofFloat);
        animatorSet.setDuration((i * 16) + ImageReceiver.DEFAULT_CROSSFADE_DURATION);
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.ActionBarPopupWindow.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ActionBarPopupWindowLayout.this.startAnimationPending = false;
                int itemsCount2 = ActionBarPopupWindowLayout.this.getItemsCount();
                for (int i3 = 0; i3 < itemsCount2; i3++) {
                    View itemAt2 = ActionBarPopupWindowLayout.this.getItemAt(i3);
                    if (!(itemAt2 instanceof GapView)) {
                        itemAt2.setAlpha(itemAt2.isEnabled() ? 1.0f : 0.5f);
                    }
                }
            }
        });
        animatorSet.start();
        return animatorSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startAnimation$2(ActionBarPopupWindowLayout actionBarPopupWindowLayout, ValueAnimator valueAnimator) {
        int itemsCount = actionBarPopupWindowLayout.getItemsCount();
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        for (int i = 0; i < itemsCount; i++) {
            View itemAt = actionBarPopupWindowLayout.getItemAt(i);
            if (!(itemAt instanceof GapView)) {
                itemAt.setTranslationY((1.0f - AndroidUtilities.cascade(floatValue, actionBarPopupWindowLayout.shownFromBottom ? (itemsCount - 1) - i : i, itemsCount, 4.0f)) * AndroidUtilities.dp(-6.0f));
            }
        }
    }

    public void startAnimation() {
        ActionBarPopupWindowLayout actionBarPopupWindowLayout;
        if (this.animationEnabled && this.windowAnimatorSet == null) {
            ViewGroup viewGroup = (ViewGroup) getContentView();
            ActionBarPopupWindowLayout actionBarPopupWindowLayout2 = null;
            if (viewGroup instanceof ActionBarPopupWindowLayout) {
                actionBarPopupWindowLayout = (ActionBarPopupWindowLayout) viewGroup;
                actionBarPopupWindowLayout.startAnimationPending = true;
            } else {
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    if (viewGroup.getChildAt(i) instanceof ActionBarPopupWindowLayout) {
                        actionBarPopupWindowLayout2 = (ActionBarPopupWindowLayout) viewGroup.getChildAt(i);
                        actionBarPopupWindowLayout2.startAnimationPending = true;
                    }
                }
                actionBarPopupWindowLayout = actionBarPopupWindowLayout2;
            }
            actionBarPopupWindowLayout.setTranslationY(0.0f);
            float f = 1.0f;
            actionBarPopupWindowLayout.setAlpha(1.0f);
            actionBarPopupWindowLayout.setPivotX(actionBarPopupWindowLayout.getMeasuredWidth());
            actionBarPopupWindowLayout.setPivotY(0.0f);
            int itemsCount = actionBarPopupWindowLayout.getItemsCount();
            actionBarPopupWindowLayout.positions.clear();
            int i2 = 0;
            for (int i3 = 0; i3 < itemsCount; i3++) {
                View itemAt = actionBarPopupWindowLayout.getItemAt(i3);
                itemAt.setAlpha(0.0f);
                if (itemAt.getVisibility() == 0) {
                    actionBarPopupWindowLayout.positions.put(itemAt, Integer.valueOf(i2));
                    i2++;
                }
            }
            if (actionBarPopupWindowLayout.shownFromBottom) {
                actionBarPopupWindowLayout.lastStartedChild = itemsCount - 1;
            } else {
                actionBarPopupWindowLayout.lastStartedChild = 0;
            }
            if (actionBarPopupWindowLayout.getSwipeBack() != null) {
                actionBarPopupWindowLayout.getSwipeBack().invalidateTransforms();
                f = actionBarPopupWindowLayout.backScaleY;
            }
            AnimatorSet animatorSet = new AnimatorSet();
            this.windowAnimatorSet = animatorSet;
            animatorSet.playTogether(ObjectAnimator.ofFloat(actionBarPopupWindowLayout, "backScaleY", 0.0f, f), ObjectAnimator.ofInt(actionBarPopupWindowLayout, "backAlpha", 0, 255));
            this.windowAnimatorSet.setDuration((i2 * 16) + ImageReceiver.DEFAULT_CROSSFADE_DURATION);
            this.windowAnimatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.ActionBarPopupWindow.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ActionBarPopupWindowLayout actionBarPopupWindowLayout3;
                    ActionBarPopupWindowLayout actionBarPopupWindowLayout4 = null;
                    ActionBarPopupWindow.this.windowAnimatorSet = null;
                    ViewGroup viewGroup2 = (ViewGroup) ActionBarPopupWindow.this.getContentView();
                    if (viewGroup2 instanceof ActionBarPopupWindowLayout) {
                        actionBarPopupWindowLayout3 = (ActionBarPopupWindowLayout) viewGroup2;
                        actionBarPopupWindowLayout3.startAnimationPending = false;
                    } else {
                        for (int i4 = 0; i4 < viewGroup2.getChildCount(); i4++) {
                            if (viewGroup2.getChildAt(i4) instanceof ActionBarPopupWindowLayout) {
                                actionBarPopupWindowLayout4 = (ActionBarPopupWindowLayout) viewGroup2.getChildAt(i4);
                                actionBarPopupWindowLayout4.startAnimationPending = false;
                            }
                        }
                        actionBarPopupWindowLayout3 = actionBarPopupWindowLayout4;
                    }
                    int itemsCount2 = actionBarPopupWindowLayout3.getItemsCount();
                    for (int i5 = 0; i5 < itemsCount2; i5++) {
                        View itemAt2 = actionBarPopupWindowLayout3.getItemAt(i5);
                        if (!(itemAt2 instanceof GapView)) {
                            itemAt2.setAlpha(itemAt2.isEnabled() ? 1.0f : 0.5f);
                        }
                    }
                }
            });
            this.windowAnimatorSet.start();
        }
    }

    @Override // android.widget.PopupWindow
    public void update(View view, int i, int i2, int i3, int i4) {
        super.update(view, i, i2, i3, i4);
        registerListener(view);
    }

    @Override // android.widget.PopupWindow
    public void update(View view, int i, int i2) {
        super.update(view, i, i2);
        registerListener(view);
    }

    @Override // android.widget.PopupWindow
    public void showAtLocation(View view, int i, int i2, int i3) {
        super.showAtLocation(view, i, i2, i3);
        unregisterListener();
    }

    @Override // android.widget.PopupWindow
    public void dismiss() {
        dismiss(true);
    }

    public void setPauseNotifications(boolean z) {
        this.pauseNotifications = z;
    }

    public void dismiss(boolean z) {
        setFocusable(false);
        dismissDim();
        AnimatorSet animatorSet = this.windowAnimatorSet;
        if (animatorSet != null) {
            if (z && this.isClosingAnimated) {
                return;
            }
            animatorSet.cancel();
            this.windowAnimatorSet = null;
        }
        this.isClosingAnimated = false;
        if (this.animationEnabled && z) {
            this.isClosingAnimated = true;
            ViewGroup viewGroup = (ViewGroup) getContentView();
            ActionBarPopupWindowLayout actionBarPopupWindowLayout = null;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i) instanceof ActionBarPopupWindowLayout) {
                    actionBarPopupWindowLayout = (ActionBarPopupWindowLayout) viewGroup.getChildAt(i);
                }
            }
            if (actionBarPopupWindowLayout != null && actionBarPopupWindowLayout.itemAnimators != null && !actionBarPopupWindowLayout.itemAnimators.isEmpty()) {
                int size = actionBarPopupWindowLayout.itemAnimators.size();
                for (int i2 = 0; i2 < size; i2++) {
                    AnimatorSet animatorSet2 = (AnimatorSet) actionBarPopupWindowLayout.itemAnimators.get(i2);
                    animatorSet2.removeAllListeners();
                    animatorSet2.cancel();
                }
                actionBarPopupWindowLayout.itemAnimators.clear();
            }
            AnimatorSet animatorSet3 = new AnimatorSet();
            this.windowAnimatorSet = animatorSet3;
            if (this.outEmptyTime > 0) {
                animatorSet3.playTogether(ValueAnimator.ofFloat(0.0f, 1.0f));
                this.windowAnimatorSet.setDuration(this.outEmptyTime);
            } else if (this.scaleOut) {
                animatorSet3.playTogether(ObjectAnimator.ofFloat(viewGroup, View.SCALE_Y, 0.8f), ObjectAnimator.ofFloat(viewGroup, View.SCALE_X, 0.8f), ObjectAnimator.ofFloat(viewGroup, View.ALPHA, 0.0f));
                this.windowAnimatorSet.setDuration(this.dismissAnimationDuration);
            } else {
                Animator[] animatorArr = new Animator[2];
                Property property = View.TRANSLATION_Y;
                float[] fArr = new float[1];
                fArr[0] = AndroidUtilities.dp((actionBarPopupWindowLayout == null || !actionBarPopupWindowLayout.shownFromBottom) ? -5.0f : 5.0f);
                animatorArr[0] = ObjectAnimator.ofFloat(viewGroup, property, fArr);
                animatorArr[1] = ObjectAnimator.ofFloat(viewGroup, View.ALPHA, 0.0f);
                animatorSet3.playTogether(animatorArr);
                this.windowAnimatorSet.setDuration(this.dismissAnimationDuration);
            }
            this.windowAnimatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.ActionBarPopupWindow.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ActionBarPopupWindow.this.windowAnimatorSet = null;
                    ActionBarPopupWindow.this.isClosingAnimated = false;
                    ActionBarPopupWindow.this.setFocusable(false);
                    try {
                        ActionBarPopupWindow.super.dismiss();
                    } catch (Exception unused) {
                    }
                    ActionBarPopupWindow.this.unregisterListener();
                    if (ActionBarPopupWindow.this.pauseNotifications) {
                        NotificationCenter.getInstance(ActionBarPopupWindow.this.currentAccount).onAnimationFinish(ActionBarPopupWindow.this.popupAnimationIndex);
                    }
                }
            });
            if (this.pauseNotifications) {
                this.popupAnimationIndex = NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(this.popupAnimationIndex, null);
            }
            this.windowAnimatorSet.start();
            return;
        }
        try {
            super.dismiss();
        } catch (Exception unused) {
        }
        unregisterListener();
    }

    /* loaded from: classes3.dex */
    public static class GapView extends FrameLayout {
        Drawable shadowDrawable;

        public GapView(Context context, Theme.ResourcesProvider resourcesProvider) {
            this(context, resourcesProvider, "actionBarDefaultSubmenuSeparator");
        }

        public GapView(Context context, int i, int i2) {
            super(context);
            this.shadowDrawable = Theme.getThemedDrawable(getContext(), R.drawable.greydivider, i2);
            setBackgroundColor(i);
        }

        public GapView(Context context, Theme.ResourcesProvider resourcesProvider, String str) {
            this(context, Theme.getColor(str, resourcesProvider), Theme.getColor("windowBackgroundGrayShadow", resourcesProvider));
        }

        public void setColor(int i) {
            setBackgroundColor(i);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Drawable drawable = this.shadowDrawable;
            if (drawable != null) {
                drawable.setBounds(0, 0, getWidth(), getHeight());
                this.shadowDrawable.draw(canvas);
            }
        }
    }
}
