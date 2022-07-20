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
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
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
    private boolean isClosingAnimated;
    private ViewTreeObserver.OnScrollChangedListener mSuperScrollListener;
    private ViewTreeObserver mViewTreeObserver;
    private boolean pauseNotifications;
    private boolean scaleOut;
    private AnimatorSet windowAnimatorSet;
    private boolean animationEnabled = allowAnimation;
    private int dismissAnimationDuration = 150;
    private int currentAccount = UserConfig.selectedAccount;
    private long outEmptyTime = -1;
    private int popupAnimationIndex = -1;

    /* loaded from: classes3.dex */
    public interface OnDispatchKeyEventListener {
        void onDispatchKeyEvent(KeyEvent keyEvent);
    }

    /* loaded from: classes3.dex */
    public interface onSizeChangedListener {
        void onSizeChanged();
    }

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
        NOP = ActionBarPopupWindow$$ExternalSyntheticLambda0.INSTANCE;
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
        private final Theme.ResourcesProvider resourcesProvider;
        private ScrollView scrollView;
        private boolean shownFromBottom;
        private boolean startAnimationPending;
        public int subtractBackgroundHeight;
        public boolean swipeBackGravityRight;
        private PopupSwipeBackLayout swipeBackLayout;
        private View topView;
        public boolean updateAnimation;

        public ActionBarPopupWindowLayout(Context context) {
            this(context, null);
        }

        public ActionBarPopupWindowLayout(Context context, Theme.ResourcesProvider resourcesProvider) {
            this(context, 2131166086, resourcesProvider);
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
            int i3 = 80;
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
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(context);
            this.linearLayout = anonymousClass1;
            anonymousClass1.setOrientation(1);
            ScrollView scrollView2 = this.scrollView;
            if (scrollView2 != null) {
                scrollView2.addView(this.linearLayout, new FrameLayout.LayoutParams(-2, -2));
                return;
            }
            PopupSwipeBackLayout popupSwipeBackLayout3 = this.swipeBackLayout;
            if (popupSwipeBackLayout3 != null) {
                popupSwipeBackLayout3.addView(this.linearLayout, LayoutHelper.createFrame(-2, -2, !this.shownFromBottom ? 48 : i3));
            } else {
                addView(this.linearLayout, LayoutHelper.createFrame(-2, -2.0f));
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: org.telegram.ui.ActionBar.ActionBarPopupWindow$ActionBarPopupWindowLayout$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends LinearLayout {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context) {
                super(context);
                ActionBarPopupWindowLayout.this = r1;
            }

            @Override // android.widget.LinearLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                if (ActionBarPopupWindowLayout.this.fitItems) {
                    ActionBarPopupWindowLayout.this.gapStartY = -1000000;
                    ActionBarPopupWindowLayout.this.gapEndY = -1000000;
                    int childCount = getChildCount();
                    ArrayList arrayList = null;
                    int i3 = 0;
                    int i4 = 0;
                    for (int i5 = 0; i5 < childCount; i5++) {
                        View childAt = getChildAt(i5);
                        if (childAt.getVisibility() != 8) {
                            Object tag = childAt.getTag(2131230954);
                            Object tag2 = childAt.getTag(2131230875);
                            Object tag3 = childAt.getTag(2131230820);
                            if (tag != null) {
                                childAt.getLayoutParams().width = -2;
                            }
                            measureChildWithMargins(childAt, i, 0, i2, 0);
                            if (tag3 == null) {
                                boolean z = tag instanceof Integer;
                                if (!z && tag2 == null) {
                                    i3 = Math.max(i3, childAt.getMeasuredWidth());
                                } else if (z) {
                                    int max = Math.max(((Integer) tag).intValue(), childAt.getMeasuredWidth());
                                    ActionBarPopupWindowLayout.this.gapStartY = childAt.getMeasuredHeight();
                                    ActionBarPopupWindowLayout actionBarPopupWindowLayout = ActionBarPopupWindowLayout.this;
                                    actionBarPopupWindowLayout.gapEndY = actionBarPopupWindowLayout.gapStartY + AndroidUtilities.dp(6.0f);
                                    i4 = max;
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
                        for (int i6 = 0; i6 < size; i6++) {
                            ((View) arrayList.get(i6)).getLayoutParams().width = Math.max(i3, i4);
                        }
                    }
                }
                super.onMeasure(i, i2);
            }

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view, long j) {
                if (view instanceof GapView) {
                    return false;
                }
                return super.drawChild(canvas, view, j);
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
                if (onsizechangedlistener == null) {
                    return;
                }
                onsizechangedlistener.onSizeChanged();
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
                            if (itemAt.getVisibility() == 0 && !(itemAt instanceof GapView)) {
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
                if (onsizechangedlistener == null) {
                    return;
                }
                onsizechangedlistener.onSizeChanged();
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

        private void startChildAnimation(View view) {
            if (this.animationEnabled) {
                AnimatorSet animatorSet = new AnimatorSet();
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
                animatorSet.addListener(new AnonymousClass2(animatorSet));
                animatorSet.setInterpolator(ActionBarPopupWindow.decelerateInterpolator);
                animatorSet.start();
                if (this.itemAnimators == null) {
                    this.itemAnimators = new ArrayList<>();
                }
                this.itemAnimators.add(animatorSet);
            }
        }

        /* renamed from: org.telegram.ui.ActionBar.ActionBarPopupWindow$ActionBarPopupWindowLayout$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 extends AnimatorListenerAdapter {
            final /* synthetic */ AnimatorSet val$animatorSet;

            AnonymousClass2(AnimatorSet animatorSet) {
                ActionBarPopupWindowLayout.this = r1;
                this.val$animatorSet = animatorSet;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ActionBarPopupWindowLayout.this.itemAnimators.remove(this.val$animatorSet);
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

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
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
            super.dispatchDraw(canvas);
        }

        /* JADX WARN: Removed duplicated region for block: B:33:0x00a9  */
        /* JADX WARN: Removed duplicated region for block: B:34:0x00ac  */
        /* JADX WARN: Removed duplicated region for block: B:37:0x00b7  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x00d3  */
        /* JADX WARN: Removed duplicated region for block: B:62:0x0164  */
        /* JADX WARN: Removed duplicated region for block: B:77:0x01dc  */
        /* JADX WARN: Removed duplicated region for block: B:89:0x01df A[SYNTHETIC] */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onDraw(Canvas canvas) {
            int i;
            boolean z;
            int i2;
            boolean z2;
            boolean z3;
            if (this.backgroundDrawable != null) {
                int scrollY = this.gapStartY - this.scrollView.getScrollY();
                int scrollY2 = this.gapEndY - this.scrollView.getScrollY();
                int i3 = 0;
                while (true) {
                    i = 1;
                    if (i3 >= this.linearLayout.getChildCount()) {
                        z = false;
                        break;
                    } else if ((this.linearLayout.getChildAt(i3) instanceof GapView) && this.linearLayout.getChildAt(i3).getVisibility() == 0) {
                        z = true;
                        break;
                    } else {
                        i3++;
                    }
                }
                int i4 = 0;
                while (i4 < 2) {
                    if (i4 == i && scrollY < (-AndroidUtilities.dp(16.0f))) {
                        return;
                    }
                    if (z && this.backAlpha != 255) {
                        i2 = -1000000;
                        canvas.saveLayerAlpha(0.0f, this.bgPaddings.top, getMeasuredWidth(), getMeasuredHeight(), this.backAlpha, 31);
                        z3 = false;
                    } else {
                        i2 = -1000000;
                        if (this.gapStartY != -1000000) {
                            canvas.save();
                            canvas.clipRect(0, this.bgPaddings.top, getMeasuredWidth(), getMeasuredHeight());
                            z3 = true;
                        } else {
                            z3 = true;
                            z2 = false;
                            this.backgroundDrawable.setAlpha(!z3 ? this.backAlpha : 255);
                            if (!this.shownFromBottom) {
                                int measuredHeight = getMeasuredHeight();
                                this.backgroundDrawable.setBounds(0, (int) (measuredHeight * (1.0f - this.backScaleY)), (int) (getMeasuredWidth() * this.backScaleX), measuredHeight);
                            } else if (scrollY > (-AndroidUtilities.dp(16.0f))) {
                                int measuredHeight2 = (int) (getMeasuredHeight() * this.backScaleY);
                                if (i4 == 0) {
                                    Drawable drawable = this.backgroundDrawable;
                                    int dp = (-this.scrollView.getScrollY()) + (this.gapStartY != i2 ? AndroidUtilities.dp(1.0f) : 0);
                                    int measuredWidth = (int) (getMeasuredWidth() * this.backScaleX);
                                    if (this.gapStartY != i2) {
                                        measuredHeight2 = Math.min(measuredHeight2, AndroidUtilities.dp(16.0f) + scrollY);
                                    }
                                    drawable.setBounds(0, dp, measuredWidth, measuredHeight2 - this.subtractBackgroundHeight);
                                } else if (measuredHeight2 < scrollY2) {
                                    if (this.gapStartY != i2) {
                                        canvas.restore();
                                    }
                                    i4++;
                                    i = 1;
                                } else {
                                    this.backgroundDrawable.setBounds(0, scrollY2, (int) (getMeasuredWidth() * this.backScaleX), measuredHeight2 - this.subtractBackgroundHeight);
                                }
                            } else {
                                this.backgroundDrawable.setBounds(0, this.gapStartY < 0 ? 0 : -AndroidUtilities.dp(16.0f), (int) (getMeasuredWidth() * this.backScaleX), ((int) (getMeasuredHeight() * this.backScaleY)) - this.subtractBackgroundHeight);
                            }
                            this.backgroundDrawable.draw(canvas);
                            if (z) {
                                canvas.save();
                                Rect rect = AndroidUtilities.rectTmp2;
                                rect.set(this.backgroundDrawable.getBounds());
                                rect.inset(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                                canvas.clipRect(rect);
                                for (int i5 = 0; i5 < this.linearLayout.getChildCount(); i5++) {
                                    if ((this.linearLayout.getChildAt(i5) instanceof GapView) && this.linearLayout.getChildAt(i5).getVisibility() == 0) {
                                        canvas.save();
                                        GapView gapView = (GapView) this.linearLayout.getChildAt(i5);
                                        float f = 0.0f;
                                        View view = gapView;
                                        float f2 = 0.0f;
                                        while (view != this) {
                                            f += view.getX();
                                            f2 += view.getY();
                                            view = (View) view.getParent();
                                            if (view == null) {
                                                break;
                                            }
                                        }
                                        canvas.translate(f, f2 * this.scrollView.getScaleY());
                                        gapView.draw(canvas);
                                        canvas.restore();
                                    }
                                }
                                canvas.restore();
                            }
                            if (!z2) {
                                canvas.restore();
                            }
                            i4++;
                            i = 1;
                        }
                    }
                    z2 = true;
                    this.backgroundDrawable.setAlpha(!z3 ? this.backAlpha : 255);
                    if (!this.shownFromBottom) {
                    }
                    this.backgroundDrawable.draw(canvas);
                    if (z) {
                    }
                    if (!z2) {
                    }
                    i4++;
                    i = 1;
                }
            }
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
                    Object tag = childAt2.getTag(2131230875);
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

        @Override // android.widget.FrameLayout, android.view.View
        public void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            PopupSwipeBackLayout popupSwipeBackLayout = this.swipeBackLayout;
            if (popupSwipeBackLayout != null) {
                popupSwipeBackLayout.invalidateTransforms(!this.startAnimationPending);
            }
        }
    }

    public ActionBarPopupWindow() {
        init();
    }

    public ActionBarPopupWindow(Context context) {
        super(context);
        init();
    }

    public ActionBarPopupWindow(View view, int i, int i2) {
        super(view, i, i2);
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

    public void setDismissAnimationDuration(int i) {
        this.dismissAnimationDuration = i;
    }

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
            if (viewTreeObserver == viewTreeObserver2) {
                return;
            }
            if (viewTreeObserver2 != null && viewTreeObserver2.isAlive()) {
                this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
            }
            this.mViewTreeObserver = viewTreeObserver;
            if (viewTreeObserver == null) {
                return;
            }
            viewTreeObserver.addOnScrollChangedListener(this.mSuperScrollListener);
        }
    }

    public void dimBehind() {
        View rootView = getContentView().getRootView();
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) rootView.getLayoutParams();
        layoutParams.flags |= 2;
        layoutParams.dimAmount = 0.2f;
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
            if ((i & 2) == 0) {
                return;
            }
            layoutParams.flags = i & (-3);
            layoutParams.dimAmount = 0.0f;
            windowManager.updateViewLayout(rootView, layoutParams);
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

    public void startAnimation() {
        ActionBarPopupWindowLayout actionBarPopupWindowLayout;
        if (!this.animationEnabled || this.windowAnimatorSet != null) {
            return;
        }
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
        this.windowAnimatorSet.setDuration((i2 * 16) + 150);
        this.windowAnimatorSet.addListener(new AnonymousClass1());
        this.windowAnimatorSet.start();
    }

    /* renamed from: org.telegram.ui.ActionBar.ActionBarPopupWindow$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends AnimatorListenerAdapter {
        AnonymousClass1() {
            ActionBarPopupWindow.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ActionBarPopupWindowLayout actionBarPopupWindowLayout;
            ActionBarPopupWindowLayout actionBarPopupWindowLayout2 = null;
            ActionBarPopupWindow.this.windowAnimatorSet = null;
            ViewGroup viewGroup = (ViewGroup) ActionBarPopupWindow.this.getContentView();
            if (viewGroup instanceof ActionBarPopupWindowLayout) {
                actionBarPopupWindowLayout = (ActionBarPopupWindowLayout) viewGroup;
                actionBarPopupWindowLayout.startAnimationPending = false;
            } else {
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    if (viewGroup.getChildAt(i) instanceof ActionBarPopupWindowLayout) {
                        actionBarPopupWindowLayout2 = (ActionBarPopupWindowLayout) viewGroup.getChildAt(i);
                        actionBarPopupWindowLayout2.startAnimationPending = false;
                    }
                }
                actionBarPopupWindowLayout = actionBarPopupWindowLayout2;
            }
            int itemsCount = actionBarPopupWindowLayout.getItemsCount();
            for (int i2 = 0; i2 < itemsCount; i2++) {
                View itemAt = actionBarPopupWindowLayout.getItemAt(i2);
                if (!(itemAt instanceof GapView)) {
                    itemAt.setAlpha(itemAt.isEnabled() ? 1.0f : 0.5f);
                }
            }
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
            this.windowAnimatorSet.addListener(new AnonymousClass2());
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

    /* renamed from: org.telegram.ui.ActionBar.ActionBarPopupWindow$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends AnimatorListenerAdapter {
        AnonymousClass2() {
            ActionBarPopupWindow.this = r1;
        }

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
    }

    /* loaded from: classes3.dex */
    public static class GapView extends FrameLayout {
        Theme.ResourcesProvider resourcesProvider;

        public GapView(Context context, Theme.ResourcesProvider resourcesProvider, String str) {
            super(context);
            this.resourcesProvider = resourcesProvider;
            setBackgroundColor(getThemedColor(str));
        }

        private int getThemedColor(String str) {
            Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
            Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
            return color != null ? color.intValue() : Theme.getColor(str);
        }

        public void setColor(int i) {
            setBackgroundColor(i);
        }
    }
}
