package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Property;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.BackButtonMenu;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.FloatingDebug.FloatingDebugProvider;
import org.telegram.ui.Components.GroupCallPip;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.EmptyBaseFragment;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.Stories.StoryViewer;
import org.telegram.ui.bots.BotWebViewSheet;
/* loaded from: classes4.dex */
public class ActionBarLayout extends FrameLayout implements INavigationLayout, FloatingDebugProvider {
    private static Drawable headerShadowDrawable;
    private static Drawable layerShadowDrawable;
    private static Paint scrimPaint;
    private AccelerateDecelerateInterpolator accelerateDecelerateInterpolator;
    private ArrayList animateEndColors;
    private int animateSetThemeAccentIdAfterAnimation;
    private Theme.ThemeInfo animateSetThemeAfterAnimation;
    private boolean animateSetThemeAfterAnimationApply;
    private boolean animateSetThemeNightAfterAnimation;
    private ArrayList animateStartColors;
    private boolean animateThemeAfterAnimation;
    protected boolean animationInProgress;
    private float animationProgress;
    public INavigationLayout.ThemeAnimationSettings.onAnimationProgress animationProgressListener;
    private Runnable animationRunnable;
    private boolean attached;
    private View backgroundView;
    private boolean beginTrackingSent;
    private BottomSheetTabs bottomSheetTabs;
    private ValueAnimator bottomTabsAnimator;
    private int bottomTabsHeight;
    private float bottomTabsProgress;
    private final Path clipPath;
    private final float[] clipRadius;
    private final RectF clipRect;
    private final Paint clipShadowPaint;
    public LayoutContainer containerView;
    public LayoutContainer containerViewBack;
    private ActionBar currentActionBar;
    private AnimatorSet currentAnimation;
    Runnable debugBlackScreenRunnable;
    private DecelerateInterpolator decelerateInterpolator;
    private boolean delayedAnimationResumed;
    private Runnable delayedOpenAnimationRunnable;
    private INavigationLayout.INavigationLayoutDelegate delegate;
    private DrawerLayoutContainer drawerLayoutContainer;
    private List fragmentsStack;
    public boolean highlightActionButtons;
    private boolean inActionMode;
    private boolean inBubbleMode;
    private boolean inPreviewMode;
    public float innerTranslationX;
    public boolean isKeyboardVisible;
    private boolean isSheet;
    ArrayList lastActions;
    private long lastFrameTime;
    private View layoutToIgnore;
    private final boolean main;
    private boolean maybeStartTracking;
    private int[] measureSpec;
    public Theme.MessageDrawable messageDrawableOutMediaStart;
    public Theme.MessageDrawable messageDrawableOutStart;
    private BaseFragment newFragment;
    AnimationNotificationsLocker notificationsLocker;
    private BaseFragment oldFragment;
    private Runnable onCloseAnimationEndRunnable;
    private Runnable onFragmentStackChangedListener;
    private Runnable onOpenAnimationEndRunnable;
    private Runnable overlayAction;
    private int overrideWidthOffset;
    private OvershootInterpolator overshootInterpolator;
    protected Activity parentActivity;
    private ArrayList presentingFragmentDescriptions;
    private ColorDrawable previewBackgroundDrawable;
    private ActionBarPopupWindow.ActionBarPopupWindowLayout previewMenu;
    private boolean previewOpenAnimationInProgress;
    private List pulledDialogs;
    private boolean rebuildAfterAnimation;
    private boolean rebuildLastAfterAnimation;
    private Rect rect;
    private boolean removeActionBarExtraHeight;
    private int savedBottomSheetTabsTop;
    public LayoutContainer sheetContainer;
    private EmptyBaseFragment sheetFragment;
    private boolean showLastAfterAnimation;
    INavigationLayout.StartColorsProvider startColorsProvider;
    protected boolean startedTracking;
    private int startedTrackingPointerId;
    private int startedTrackingX;
    private int startedTrackingY;
    private boolean tabsEvents;
    private float themeAnimationValue;
    private ArrayList themeAnimatorDelegate;
    private ArrayList themeAnimatorDescriptions;
    private AnimatorSet themeAnimatorSet;
    private String titleOverlayText;
    private int titleOverlayTextId;
    private boolean transitionAnimationInProgress;
    private boolean transitionAnimationPreviewMode;
    private long transitionAnimationStartTime;
    private boolean useAlphaAnimations;
    private VelocityTracker velocityTracker;
    private Runnable waitingForKeyboardCloseRunnable;
    private Window window;
    private boolean withShadow;

    /* loaded from: classes4.dex */
    public class LayoutContainer extends FrameLayout {
        private int backgroundColor;
        private Paint backgroundPaint;
        private int fragmentPanTranslationOffset;
        private boolean isKeyboardVisible;
        private Rect rect;
        private boolean wasPortrait;

        public LayoutContainer(Context context) {
            super(context);
            this.rect = new Rect();
            this.backgroundPaint = new Paint();
            setWillNotDraw(false);
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                if (motionEvent.getY() > getHeight() - (this.isKeyboardVisible ? 0 : ActionBarLayout.this.getBottomTabsHeight(true))) {
                    return false;
                }
            }
            boolean z = ActionBarLayout.this.inPreviewMode && ActionBarLayout.this.previewMenu == null;
            if ((!z && !ActionBarLayout.this.transitionAnimationPreviewMode) || (motionEvent.getActionMasked() != 0 && motionEvent.getActionMasked() != 5)) {
                if (z) {
                    try {
                        if (this != ActionBarLayout.this.containerView) {
                        }
                    } catch (Throwable th) {
                        FileLog.e(th);
                    }
                }
                return super.dispatchTouchEvent(motionEvent);
            }
            return false;
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            int i;
            int i2;
            BaseFragment baseFragment = !ActionBarLayout.this.fragmentsStack.isEmpty() ? (BaseFragment) ActionBarLayout.this.fragmentsStack.get(ActionBarLayout.this.fragmentsStack.size() - 1) : null;
            if (ActionBarLayout.this.sheetFragment != null && ActionBarLayout.this.sheetFragment.sheetsStack != null && !ActionBarLayout.this.sheetFragment.sheetsStack.isEmpty()) {
                baseFragment = ActionBarLayout.this.sheetFragment;
            }
            BaseFragment.AttachedSheet lastSheet = baseFragment != null ? baseFragment.getLastSheet() : null;
            if (lastSheet == null || !lastSheet.isFullyVisible() || lastSheet.getWindowView() == view) {
                if (view instanceof ActionBar) {
                    return super.drawChild(canvas, view, j);
                }
                int childCount = getChildCount();
                int i3 = 0;
                while (true) {
                    if (i3 >= childCount) {
                        break;
                    }
                    View childAt = getChildAt(i3);
                    if (childAt == view || !(childAt instanceof ActionBar) || childAt.getVisibility() != 0) {
                        i3++;
                    } else if (((ActionBar) childAt).getCastShadows()) {
                        i = childAt.getMeasuredHeight();
                        i2 = (int) childAt.getY();
                    }
                }
                i = 0;
                i2 = 0;
                boolean drawChild = super.drawChild(canvas, view, j);
                if (i != 0 && ActionBarLayout.headerShadowDrawable != null) {
                    int i4 = i2 + i;
                    ActionBarLayout.headerShadowDrawable.setBounds(0, i4, getMeasuredWidth(), ActionBarLayout.headerShadowDrawable.getIntrinsicHeight() + i4);
                    ActionBarLayout.headerShadowDrawable.draw(canvas);
                }
                return drawChild;
            }
            return true;
        }

        @Override // android.view.View
        public boolean hasOverlappingRendering() {
            return Build.VERSION.SDK_INT >= 28;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.fragmentPanTranslationOffset != 0) {
                int i = Theme.key_windowBackgroundWhite;
                if (this.backgroundColor != Theme.getColor(i)) {
                    Paint paint = this.backgroundPaint;
                    int color = Theme.getColor(i);
                    this.backgroundColor = color;
                    paint.setColor(color);
                }
                canvas.drawRect(0.0f, (getMeasuredHeight() - this.fragmentPanTranslationOffset) - 3, getMeasuredWidth(), getMeasuredHeight(), this.backgroundPaint);
            }
            super.onDraw(canvas);
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5;
            int i6;
            int i7;
            int measuredWidth;
            int i8;
            int childCount = getChildCount();
            int i9 = 0;
            while (true) {
                if (i9 >= childCount) {
                    i5 = 0;
                    break;
                }
                View childAt = getChildAt(i9);
                if (childAt instanceof ActionBar) {
                    i5 = childAt.getMeasuredHeight();
                    childAt.layout(0, 0, childAt.getMeasuredWidth(), i5);
                    break;
                }
                i9++;
            }
            for (int i10 = 0; i10 < childCount; i10++) {
                View childAt2 = getChildAt(i10);
                if (!(childAt2 instanceof ActionBar)) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt2.getLayoutParams();
                    if (childAt2.getFitsSystemWindows() || (childAt2 instanceof BaseFragment.AttachedSheetWindow)) {
                        i6 = layoutParams.leftMargin;
                        i7 = layoutParams.topMargin;
                        measuredWidth = childAt2.getMeasuredWidth() + i6;
                        i8 = layoutParams.topMargin;
                    } else {
                        i6 = layoutParams.leftMargin;
                        i7 = layoutParams.topMargin + i5;
                        measuredWidth = childAt2.getMeasuredWidth() + i6;
                        i8 = layoutParams.topMargin + i5;
                    }
                    childAt2.layout(i6, i7, measuredWidth, i8 + childAt2.getMeasuredHeight());
                }
            }
            View rootView = getRootView();
            getWindowVisibleDisplayFrame(this.rect);
            int height = (rootView.getHeight() - (this.rect.top != 0 ? AndroidUtilities.statusBarHeight : 0)) - AndroidUtilities.getViewInset(rootView);
            Rect rect = this.rect;
            this.isKeyboardVisible = height - (rect.bottom - rect.top) > 0;
            if (ActionBarLayout.this.waitingForKeyboardCloseRunnable != null) {
                ActionBarLayout actionBarLayout = ActionBarLayout.this;
                if (actionBarLayout.containerView.isKeyboardVisible || actionBarLayout.containerViewBack.isKeyboardVisible) {
                    return;
                }
                AndroidUtilities.cancelRunOnUIThread(actionBarLayout.waitingForKeyboardCloseRunnable);
                ActionBarLayout.this.waitingForKeyboardCloseRunnable.run();
                ActionBarLayout.this.waitingForKeyboardCloseRunnable = null;
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int i3;
            int i4;
            LayoutContainer layoutContainer;
            int i5;
            int i6;
            int i7;
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            boolean z = size2 > size;
            if (this.wasPortrait != z && ActionBarLayout.this.isInPreviewMode()) {
                ActionBarLayout.this.finishPreviewFragment();
            }
            this.wasPortrait = z;
            int childCount = getChildCount();
            View rootView = getRootView();
            getWindowVisibleDisplayFrame(this.rect);
            int height = (rootView.getHeight() - (this.rect.top != 0 ? AndroidUtilities.statusBarHeight : 0)) - AndroidUtilities.getViewInset(rootView);
            Rect rect = this.rect;
            int bottomTabsHeight = height - (rect.bottom - rect.top) > 0 ? 0 : ActionBarLayout.this.getBottomTabsHeight(false);
            int i8 = 0;
            while (true) {
                if (i8 >= childCount) {
                    i3 = 0;
                    break;
                }
                View childAt = getChildAt(i8);
                if (childAt instanceof ActionBar) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 0));
                    i3 = childAt.getMeasuredHeight();
                    break;
                }
                i8++;
            }
            for (int i9 = 0; i9 < childCount; i9++) {
                View childAt2 = getChildAt(i9);
                if (!(childAt2 instanceof ActionBar)) {
                    if (childAt2.getFitsSystemWindows() || (childAt2 instanceof BaseFragment.AttachedSheetWindow)) {
                        i4 = 0;
                        layoutContainer = this;
                        i5 = i;
                        i6 = i2;
                        i7 = bottomTabsHeight;
                    } else {
                        i7 = i3 + bottomTabsHeight;
                        i4 = 0;
                        layoutContainer = this;
                        i5 = i;
                        i6 = i2;
                    }
                    layoutContainer.measureChildWithMargins(childAt2, i5, i4, i6, i7);
                }
            }
            setMeasuredDimension(size, size2);
        }

        public void setFragmentPanTranslationOffset(int i) {
            this.fragmentPanTranslationOffset = i;
            invalidate();
        }
    }

    public ActionBarLayout(Context context, boolean z) {
        super(context);
        this.highlightActionButtons = false;
        this.decelerateInterpolator = new DecelerateInterpolator(1.5f);
        this.overshootInterpolator = new OvershootInterpolator(1.02f);
        this.accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
        this.animateStartColors = new ArrayList();
        this.animateEndColors = new ArrayList();
        this.startColorsProvider = new INavigationLayout.StartColorsProvider();
        this.themeAnimatorDescriptions = new ArrayList();
        this.themeAnimatorDelegate = new ArrayList();
        this.notificationsLocker = new AnimationNotificationsLocker();
        this.rect = new Rect();
        this.overrideWidthOffset = -1;
        this.measureSpec = new int[2];
        this.clipRect = new RectF();
        this.clipRadius = new float[8];
        this.clipPath = new Path();
        this.clipShadowPaint = new Paint(1);
        this.lastActions = new ArrayList();
        this.debugBlackScreenRunnable = new Runnable() { // from class: org.telegram.ui.ActionBar.ActionBarLayout$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                ActionBarLayout.this.lambda$new$9();
            }
        };
        this.parentActivity = (Activity) context;
        this.main = z;
        if (layerShadowDrawable == null) {
            layerShadowDrawable = getResources().getDrawable(R.drawable.layer_shadow);
            headerShadowDrawable = getResources().getDrawable(R.drawable.header_shadow).mutate();
            scrimPaint = new Paint();
        }
    }

    static /* synthetic */ float access$1216(ActionBarLayout actionBarLayout, float f) {
        float f2 = actionBarLayout.animationProgress + f;
        actionBarLayout.animationProgress = f2;
        return f2;
    }

    private void addEndDescriptions(ArrayList arrayList) {
        if (arrayList == null) {
            return;
        }
        int[] iArr = new int[arrayList.size()];
        this.animateEndColors.add(iArr);
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            iArr[i] = ((ThemeDescription) arrayList.get(i)).getSetColor();
        }
    }

    private void addStartDescriptions(ArrayList arrayList) {
        if (arrayList == null) {
            return;
        }
        this.themeAnimatorDescriptions.add(arrayList);
        int[] iArr = new int[arrayList.size()];
        this.animateStartColors.add(iArr);
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ThemeDescription themeDescription = (ThemeDescription) arrayList.get(i);
            iArr[i] = themeDescription.getSetColor();
            ThemeDescription.ThemeDescriptionDelegate delegateDisabled = themeDescription.setDelegateDisabled();
            if (delegateDisabled != null && !this.themeAnimatorDelegate.contains(delegateDisabled)) {
                this.themeAnimatorDelegate.add(delegateDisabled);
            }
        }
    }

    private void attachView(BaseFragment baseFragment) {
        View view = baseFragment.fragmentView;
        if (view == null) {
            view = baseFragment.createView(this.parentActivity);
        } else {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null) {
                baseFragment.onRemoveFromParent();
                viewGroup.removeView(view);
            }
        }
        if (!baseFragment.hasOwnBackground && view.getBackground() == null) {
            view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        }
        this.containerView.addView(view, LayoutHelper.createFrame(-1, -1.0f));
        ActionBar actionBar = baseFragment.actionBar;
        if (actionBar != null && actionBar.shouldAddToContainer()) {
            if (this.removeActionBarExtraHeight) {
                baseFragment.actionBar.setOccupyStatusBar(false);
            }
            ViewGroup viewGroup2 = (ViewGroup) baseFragment.actionBar.getParent();
            if (viewGroup2 != null) {
                viewGroup2.removeView(baseFragment.actionBar);
            }
            this.containerView.addView(baseFragment.actionBar);
            baseFragment.actionBar.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, this.overlayAction);
        }
        baseFragment.attachSheets(this.containerView);
    }

    private void attachViewTo(BaseFragment baseFragment, int i) {
        View view = baseFragment.fragmentView;
        if (view == null) {
            view = baseFragment.createView(this.parentActivity);
        } else {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null) {
                baseFragment.onRemoveFromParent();
                viewGroup.removeView(view);
            }
        }
        if (!baseFragment.hasOwnBackground && view.getBackground() == null) {
            view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        }
        LayoutContainer layoutContainer = this.containerView;
        layoutContainer.addView(view, Utilities.clamp(i, layoutContainer.getChildCount(), 0), LayoutHelper.createFrame(-1, -1.0f));
        ActionBar actionBar = baseFragment.actionBar;
        if (actionBar != null && actionBar.shouldAddToContainer()) {
            if (this.removeActionBarExtraHeight) {
                baseFragment.actionBar.setOccupyStatusBar(false);
            }
            ViewGroup viewGroup2 = (ViewGroup) baseFragment.actionBar.getParent();
            if (viewGroup2 != null) {
                viewGroup2.removeView(baseFragment.actionBar);
            }
            this.containerView.addView(baseFragment.actionBar);
            baseFragment.actionBar.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, this.overlayAction);
        }
        baseFragment.attachSheets(this.containerView);
    }

    private void checkNeedRebuild() {
        if (this.rebuildAfterAnimation) {
            rebuildAllFragmentViews(this.rebuildLastAfterAnimation, this.showLastAfterAnimation);
            this.rebuildAfterAnimation = false;
        } else if (this.animateThemeAfterAnimation) {
            INavigationLayout.ThemeAnimationSettings themeAnimationSettings = new INavigationLayout.ThemeAnimationSettings(this.animateSetThemeAfterAnimation, this.animateSetThemeAccentIdAfterAnimation, this.animateSetThemeNightAfterAnimation, false);
            boolean z = this.animateSetThemeAfterAnimationApply;
            if (!z) {
                themeAnimationSettings.applyTrulyTheme = z;
                themeAnimationSettings.applyTheme = z;
            }
            animateThemedValues(themeAnimationSettings, null);
            this.animateSetThemeAfterAnimation = null;
            this.animateThemeAfterAnimation = false;
        }
    }

    private void closeLastFragmentInternalRemoveOld(BaseFragment baseFragment) {
        baseFragment.finishing = true;
        baseFragment.onPause();
        baseFragment.onFragmentDestroy();
        baseFragment.setParentLayout(null);
        this.fragmentsStack.remove(baseFragment);
        this.containerViewBack.setVisibility(4);
        this.containerViewBack.setTranslationY(0.0f);
        bringChildToFront(this.containerView);
        LayoutContainer layoutContainer = this.sheetContainer;
        if (layoutContainer != null) {
            bringChildToFront(layoutContainer);
        }
        onFragmentStackChanged("closeLastFragmentInternalRemoveOld");
    }

    private void drawPreviewDrawables(Canvas canvas, ViewGroup viewGroup) {
        View childAt = viewGroup.getChildAt(0);
        if (childAt != null) {
            this.previewBackgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            this.previewBackgroundDrawable.draw(canvas);
            if (this.previewMenu == null) {
                int dp = AndroidUtilities.dp(32.0f);
                int i = dp / 2;
                int measuredWidth = (getMeasuredWidth() - dp) / 2;
                int top = (int) ((childAt.getTop() + viewGroup.getTranslationY()) - AndroidUtilities.dp((Build.VERSION.SDK_INT < 21 ? 20 : 0) + 12));
                Theme.moveUpDrawable.setBounds(measuredWidth, top, dp + measuredWidth, i + top);
                Theme.moveUpDrawable.draw(canvas);
            }
        }
    }

    public static View findScrollingChild(ViewGroup viewGroup, float f, float f2) {
        View findScrollingChild;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt.getVisibility() == 0) {
                Rect rect = AndroidUtilities.rectTmp2;
                childAt.getHitRect(rect);
                if (!rect.contains((int) f, (int) f2)) {
                    continue;
                } else if (childAt.canScrollHorizontally(-1)) {
                    return childAt;
                } else {
                    if ((childAt instanceof ViewGroup) && (findScrollingChild = findScrollingChild((ViewGroup) childAt, f - rect.left, f2 - rect.top)) != null) {
                        return findScrollingChild;
                    }
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0092  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$animateThemedValues$7(int i, final INavigationLayout.ThemeAnimationSettings themeAnimationSettings, Runnable runnable) {
        BaseFragment baseFragment;
        ArrayList<ThemeDescription> themeDescriptions;
        Dialog dialog;
        ArrayList<ThemeDescription> themeDescriptions2;
        Runnable runnable2;
        boolean z = false;
        for (int i2 = 0; i2 < i; i2++) {
            if (i2 == 0) {
                baseFragment = getLastFragment();
            } else {
                if ((this.inPreviewMode || this.transitionAnimationPreviewMode) && this.fragmentsStack.size() > 1) {
                    List list = this.fragmentsStack;
                    baseFragment = (BaseFragment) list.get(list.size() - 2);
                }
            }
            if (baseFragment != null) {
                if (themeAnimationSettings.resourcesProvider != null) {
                    if (this.messageDrawableOutStart == null) {
                        Theme.MessageDrawable messageDrawable = new Theme.MessageDrawable(0, true, false, this.startColorsProvider);
                        this.messageDrawableOutStart = messageDrawable;
                        messageDrawable.isCrossfadeBackground = true;
                        Theme.MessageDrawable messageDrawable2 = new Theme.MessageDrawable(1, true, false, this.startColorsProvider);
                        this.messageDrawableOutMediaStart = messageDrawable2;
                        messageDrawable2.isCrossfadeBackground = true;
                    }
                    this.startColorsProvider.saveColors(themeAnimationSettings.resourcesProvider);
                }
                ArrayList themeDescriptions3 = baseFragment.getThemeDescriptions();
                addStartDescriptions(themeDescriptions3);
                Dialog dialog2 = baseFragment.visibleDialog;
                if (dialog2 instanceof BottomSheet) {
                    themeDescriptions = ((BottomSheet) dialog2).getThemeDescriptions();
                } else {
                    if (dialog2 instanceof AlertDialog) {
                        themeDescriptions = ((AlertDialog) dialog2).getThemeDescriptions();
                    }
                    if (i2 == 0 && (runnable2 = themeAnimationSettings.afterStartDescriptionsAddedRunnable) != null) {
                        runnable2.run();
                    }
                    addEndDescriptions(themeDescriptions3);
                    dialog = baseFragment.visibleDialog;
                    if (dialog instanceof BottomSheet) {
                        if (dialog instanceof AlertDialog) {
                            themeDescriptions2 = ((AlertDialog) dialog).getThemeDescriptions();
                        }
                        z = true;
                    } else {
                        themeDescriptions2 = ((BottomSheet) dialog).getThemeDescriptions();
                    }
                    addEndDescriptions(themeDescriptions2);
                    z = true;
                }
                addStartDescriptions(themeDescriptions);
                if (i2 == 0) {
                    runnable2.run();
                }
                addEndDescriptions(themeDescriptions3);
                dialog = baseFragment.visibleDialog;
                if (dialog instanceof BottomSheet) {
                }
                addEndDescriptions(themeDescriptions2);
                z = true;
            }
        }
        if (z) {
            if (!themeAnimationSettings.onlyTopFragment) {
                int size = this.fragmentsStack.size() - ((this.inPreviewMode || this.transitionAnimationPreviewMode) ? 2 : 1);
                for (int i3 = 0; i3 < size; i3++) {
                    BaseFragment baseFragment2 = (BaseFragment) this.fragmentsStack.get(i3);
                    baseFragment2.clearViews();
                    baseFragment2.setParentLayout(this);
                }
            }
            if (themeAnimationSettings.instant) {
                setThemeAnimationValue(1.0f);
                this.themeAnimatorDescriptions.clear();
                this.animateStartColors.clear();
                this.animateEndColors.clear();
                this.themeAnimatorDelegate.clear();
                this.presentingFragmentDescriptions = null;
                this.animationProgressListener = null;
                Runnable runnable3 = themeAnimationSettings.afterAnimationRunnable;
                if (runnable3 != null) {
                    runnable3.run();
                }
                if (runnable != null) {
                    runnable.run();
                    return;
                }
                return;
            }
            Theme.setAnimatingColor(true);
            setThemeAnimationValue(0.0f);
            Runnable runnable4 = themeAnimationSettings.beforeAnimationRunnable;
            if (runnable4 != null) {
                runnable4.run();
            }
            INavigationLayout.ThemeAnimationSettings.onAnimationProgress onanimationprogress = themeAnimationSettings.animationProgress;
            this.animationProgressListener = onanimationprogress;
            if (onanimationprogress != null) {
                onanimationprogress.setProgress(0.0f);
            }
            this.notificationsLocker.lock();
            AnimatorSet animatorSet = new AnimatorSet();
            this.themeAnimatorSet = animatorSet;
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.ActionBarLayout.11
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    if (animator.equals(ActionBarLayout.this.themeAnimatorSet)) {
                        ActionBarLayout.this.themeAnimatorDescriptions.clear();
                        ActionBarLayout.this.animateStartColors.clear();
                        ActionBarLayout.this.animateEndColors.clear();
                        ActionBarLayout.this.themeAnimatorDelegate.clear();
                        Theme.setAnimatingColor(false);
                        ActionBarLayout.this.presentingFragmentDescriptions = null;
                        ActionBarLayout actionBarLayout = ActionBarLayout.this;
                        actionBarLayout.animationProgressListener = null;
                        actionBarLayout.themeAnimatorSet = null;
                        Runnable runnable5 = themeAnimationSettings.afterAnimationRunnable;
                        if (runnable5 != null) {
                            runnable5.run();
                        }
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ActionBarLayout.this.notificationsLocker.unlock();
                    if (animator.equals(ActionBarLayout.this.themeAnimatorSet)) {
                        ActionBarLayout.this.themeAnimatorDescriptions.clear();
                        ActionBarLayout.this.animateStartColors.clear();
                        ActionBarLayout.this.animateEndColors.clear();
                        ActionBarLayout.this.themeAnimatorDelegate.clear();
                        Theme.setAnimatingColor(false);
                        ActionBarLayout.this.presentingFragmentDescriptions = null;
                        ActionBarLayout actionBarLayout = ActionBarLayout.this;
                        actionBarLayout.animationProgressListener = null;
                        actionBarLayout.themeAnimatorSet = null;
                        Runnable runnable5 = themeAnimationSettings.afterAnimationRunnable;
                        if (runnable5 != null) {
                            runnable5.run();
                        }
                    }
                }
            });
            this.themeAnimatorSet.playTogether(ObjectAnimator.ofFloat(this, "themeAnimationValue", 0.0f, 1.0f));
            this.themeAnimatorSet.setDuration(themeAnimationSettings.duration);
            this.themeAnimatorSet.start();
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closeLastFragment$3(BaseFragment baseFragment, BaseFragment baseFragment2) {
        ViewGroup viewGroup;
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = this.previewMenu;
        if (actionBarPopupWindowLayout != null && (viewGroup = (ViewGroup) actionBarPopupWindowLayout.getParent()) != null) {
            viewGroup.removeView(this.previewMenu);
        }
        if (this.inPreviewMode || this.transitionAnimationPreviewMode) {
            this.containerViewBack.setScaleX(1.0f);
            this.containerViewBack.setScaleY(1.0f);
            this.inPreviewMode = false;
            this.previewMenu = null;
            this.transitionAnimationPreviewMode = false;
        } else {
            this.containerViewBack.setTranslationX(0.0f);
        }
        closeLastFragmentInternalRemoveOld(baseFragment);
        baseFragment.setRemovingFromStack(false);
        baseFragment.onTransitionAnimationEnd(false, true);
        baseFragment2.onTransitionAnimationEnd(true, true);
        baseFragment2.onBecomeFullyVisible();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closeLastFragment$4() {
        onAnimationEndCheck(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closeLastFragment$5(BaseFragment baseFragment) {
        removeFragmentFromStackInternal(baseFragment, false);
        setVisibility(8);
        View view = this.backgroundView;
        if (view != null) {
            view.setVisibility(8);
        }
        DrawerLayoutContainer drawerLayoutContainer = this.drawerLayoutContainer;
        if (drawerLayoutContainer != null) {
            drawerLayoutContainer.setAllowOpenDrawer(true, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$9() {
        if (this.attached && getLastFragment() != null && this.containerView.getChildCount() == 0) {
            if (BuildVars.DEBUG_VERSION) {
                FileLog.e(new RuntimeException(TextUtils.join(", ", this.lastActions)));
            }
            rebuildAllFragmentViews(true, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$presentFragment$0(BaseFragment baseFragment, BaseFragment baseFragment2) {
        if (baseFragment != null) {
            baseFragment.onTransitionAnimationEnd(false, false);
        }
        baseFragment2.onTransitionAnimationEnd(true, false);
        baseFragment2.onBecomeFullyVisible();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$presentFragment$1(boolean z, ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, boolean z2, BaseFragment baseFragment, BaseFragment baseFragment2) {
        if (z) {
            this.inPreviewMode = true;
            this.previewMenu = actionBarPopupWindowLayout;
            this.transitionAnimationPreviewMode = false;
            this.containerView.setScaleX(1.0f);
            this.containerView.setScaleY(1.0f);
        } else {
            presentFragmentInternalRemoveOld(z2, baseFragment);
            this.containerView.setTranslationX(0.0f);
        }
        if (baseFragment != null) {
            baseFragment.onTransitionAnimationEnd(false, false);
        }
        baseFragment2.onTransitionAnimationEnd(true, false);
        baseFragment2.onBecomeFullyVisible();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$presentFragment$2() {
        onAnimationEndCheck(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeAllFragments$6() {
        this.backgroundView.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateBottomTabsVisibility$10(ValueAnimator valueAnimator) {
        this.bottomTabsProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    private void observeDebugItemsFromView(List list, View view) {
        if (view instanceof FloatingDebugProvider) {
            list.addAll(((FloatingDebugProvider) view).onGetDebugItems());
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                observeDebugItemsFromView(list, viewGroup.getChildAt(i));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAnimationEndCheck(boolean z) {
        onCloseAnimationEnd();
        onOpenAnimationEnd();
        Runnable runnable = this.waitingForKeyboardCloseRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.waitingForKeyboardCloseRunnable = null;
        }
        AnimatorSet animatorSet = this.currentAnimation;
        if (animatorSet != null) {
            if (z) {
                animatorSet.cancel();
            }
            this.currentAnimation = null;
        }
        Runnable runnable2 = this.animationRunnable;
        if (runnable2 != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable2);
            this.animationRunnable = null;
        }
        setAlpha(1.0f);
        this.containerView.setAlpha(1.0f);
        this.containerView.setScaleX(1.0f);
        this.containerView.setScaleY(1.0f);
        this.containerViewBack.setAlpha(1.0f);
        this.containerViewBack.setScaleX(1.0f);
        this.containerViewBack.setScaleY(1.0f);
    }

    private void onCloseAnimationEnd() {
        if (!this.transitionAnimationInProgress || this.onCloseAnimationEndRunnable == null) {
            return;
        }
        AnimatorSet animatorSet = this.currentAnimation;
        if (animatorSet != null) {
            this.currentAnimation = null;
            animatorSet.cancel();
        }
        this.transitionAnimationInProgress = false;
        this.layoutToIgnore = null;
        this.transitionAnimationPreviewMode = false;
        this.transitionAnimationStartTime = 0L;
        this.newFragment = null;
        this.oldFragment = null;
        Runnable runnable = this.onCloseAnimationEndRunnable;
        this.onCloseAnimationEndRunnable = null;
        if (runnable != null) {
            runnable.run();
        }
        checkNeedRebuild();
        checkNeedRebuild();
    }

    private void onFragmentStackChanged(String str) {
        Runnable runnable = this.onFragmentStackChangedListener;
        if (runnable != null) {
            runnable.run();
        }
        ImageLoader.getInstance().onFragmentStackChanged();
        checkBlackScreen(str);
    }

    private void onOpenAnimationEnd() {
        Runnable runnable;
        if (!this.transitionAnimationInProgress || (runnable = this.onOpenAnimationEndRunnable) == null) {
            return;
        }
        this.transitionAnimationInProgress = false;
        this.layoutToIgnore = null;
        this.transitionAnimationPreviewMode = false;
        this.transitionAnimationStartTime = 0L;
        this.newFragment = null;
        this.oldFragment = null;
        this.onOpenAnimationEndRunnable = null;
        runnable.run();
        checkNeedRebuild();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSlideAnimationEnd(boolean z) {
        ViewGroup viewGroup;
        ViewGroup viewGroup2;
        if (z) {
            if (this.fragmentsStack.size() >= 2) {
                List list = this.fragmentsStack;
                ((BaseFragment) list.get(list.size() - 1)).prepareFragmentToSlide(true, false);
                List list2 = this.fragmentsStack;
                BaseFragment baseFragment = (BaseFragment) list2.get(list2.size() - 2);
                baseFragment.prepareFragmentToSlide(false, false);
                baseFragment.onPause();
                View view = baseFragment.fragmentView;
                if (view != null && (viewGroup2 = (ViewGroup) view.getParent()) != null) {
                    baseFragment.onRemoveFromParent();
                    viewGroup2.removeViewInLayout(baseFragment.fragmentView);
                }
                ActionBar actionBar = baseFragment.actionBar;
                if (actionBar != null && actionBar.shouldAddToContainer() && (viewGroup = (ViewGroup) baseFragment.actionBar.getParent()) != null) {
                    viewGroup.removeViewInLayout(baseFragment.actionBar);
                }
                baseFragment.detachSheets();
            }
            this.layoutToIgnore = null;
        } else if (this.fragmentsStack.size() < 2) {
            return;
        } else {
            List list3 = this.fragmentsStack;
            BaseFragment baseFragment2 = (BaseFragment) list3.get(list3.size() - 1);
            baseFragment2.prepareFragmentToSlide(true, false);
            baseFragment2.onPause();
            baseFragment2.onFragmentDestroy();
            baseFragment2.setParentLayout(null);
            List list4 = this.fragmentsStack;
            list4.remove(list4.size() - 1);
            onFragmentStackChanged("onSlideAnimationEnd");
            LayoutContainer layoutContainer = this.containerView;
            LayoutContainer layoutContainer2 = this.containerViewBack;
            this.containerView = layoutContainer2;
            this.containerViewBack = layoutContainer;
            bringChildToFront(layoutContainer2);
            View view2 = this.sheetContainer;
            if (view2 != null) {
                bringChildToFront(view2);
            }
            List list5 = this.fragmentsStack;
            BaseFragment baseFragment3 = (BaseFragment) list5.get(list5.size() - 1);
            this.currentActionBar = baseFragment3.actionBar;
            baseFragment3.onResume();
            baseFragment3.onBecomeFullyVisible();
            baseFragment3.prepareFragmentToSlide(false, false);
            this.layoutToIgnore = this.containerView;
        }
        this.containerViewBack.setVisibility(4);
        this.startedTracking = false;
        this.animationInProgress = false;
        this.containerView.setTranslationX(0.0f);
        this.containerViewBack.setTranslationX(0.0f);
        setInnerTranslationX(0.0f);
    }

    private void prepareForMoving(MotionEvent motionEvent) {
        this.maybeStartTracking = false;
        this.startedTracking = true;
        this.layoutToIgnore = this.containerViewBack;
        this.startedTrackingX = (int) motionEvent.getX();
        this.containerViewBack.setVisibility(0);
        this.beginTrackingSent = false;
        List list = this.fragmentsStack;
        BaseFragment baseFragment = (BaseFragment) list.get(list.size() - 2);
        View view = baseFragment.fragmentView;
        if (view == null) {
            view = baseFragment.createView(this.parentActivity);
        }
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            baseFragment.onRemoveFromParent();
            viewGroup.removeView(view);
        }
        this.containerViewBack.addView(view);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.leftMargin = 0;
        layoutParams.rightMargin = 0;
        layoutParams.bottomMargin = 0;
        layoutParams.topMargin = 0;
        view.setLayoutParams(layoutParams);
        ActionBar actionBar = baseFragment.actionBar;
        if (actionBar != null && actionBar.shouldAddToContainer()) {
            AndroidUtilities.removeFromParent(baseFragment.actionBar);
            if (this.removeActionBarExtraHeight) {
                baseFragment.actionBar.setOccupyStatusBar(false);
            }
            this.containerViewBack.addView(baseFragment.actionBar);
            baseFragment.actionBar.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, this.overlayAction);
        }
        baseFragment.attachSheets(this.containerViewBack);
        if (!baseFragment.hasOwnBackground && view.getBackground() == null) {
            view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        }
        baseFragment.onResume();
        if (this.themeAnimatorSet != null) {
            this.presentingFragmentDescriptions = baseFragment.getThemeDescriptions();
        }
        List list2 = this.fragmentsStack;
        ((BaseFragment) list2.get(list2.size() - 1)).prepareFragmentToSlide(true, true);
        baseFragment.prepareFragmentToSlide(false, true);
    }

    private void presentFragmentInternalRemoveOld(boolean z, BaseFragment baseFragment) {
        ViewGroup viewGroup;
        ViewGroup viewGroup2;
        if (baseFragment == null) {
            return;
        }
        baseFragment.onBecomeFullyHidden();
        baseFragment.onPause();
        if (z) {
            baseFragment.onFragmentDestroy();
            baseFragment.setParentLayout(null);
            this.fragmentsStack.remove(baseFragment);
            onFragmentStackChanged("presentFragmentInternalRemoveOld");
        } else {
            View view = baseFragment.fragmentView;
            if (view != null && (viewGroup2 = (ViewGroup) view.getParent()) != null) {
                baseFragment.onRemoveFromParent();
                try {
                    viewGroup2.removeViewInLayout(baseFragment.fragmentView);
                } catch (Exception e) {
                    FileLog.e(e);
                    try {
                        viewGroup2.removeView(baseFragment.fragmentView);
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                }
            }
            ActionBar actionBar = baseFragment.actionBar;
            if (actionBar != null && actionBar.shouldAddToContainer() && (viewGroup = (ViewGroup) baseFragment.actionBar.getParent()) != null) {
                viewGroup.removeViewInLayout(baseFragment.actionBar);
            }
            baseFragment.detachSheets();
        }
        this.containerViewBack.setVisibility(4);
    }

    private void removeFragmentFromStackInternal(BaseFragment baseFragment, boolean z) {
        if (this.fragmentsStack.contains(baseFragment)) {
            if (z) {
                List list = this.fragmentsStack;
                if (list.get(list.size() - 1) == baseFragment) {
                    baseFragment.finishFragment();
                    return;
                }
            }
            List list2 = this.fragmentsStack;
            if (list2.get(list2.size() - 1) == baseFragment && this.fragmentsStack.size() > 1) {
                baseFragment.finishFragment(false);
                return;
            }
            baseFragment.onPause();
            baseFragment.onFragmentDestroy();
            baseFragment.setParentLayout(null);
            this.fragmentsStack.remove(baseFragment);
            onFragmentStackChanged("removeFragmentFromStackInternal " + z);
        }
    }

    private boolean shouldOpenFragmentOverlay(Dialog dialog) {
        return dialog != null && dialog.isShowing() && ((dialog instanceof ChatAttachAlert) || (dialog instanceof BotWebViewSheet));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startLayoutAnimation(final boolean z, final boolean z2, final boolean z3) {
        if (z2) {
            this.animationProgress = 0.0f;
            this.lastFrameTime = System.nanoTime() / 1000000;
        }
        Runnable runnable = new Runnable() { // from class: org.telegram.ui.ActionBar.ActionBarLayout.2
            /* JADX WARN: Removed duplicated region for block: B:80:0x0268  */
            /* JADX WARN: Removed duplicated region for block: B:81:0x0272  */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void run() {
                LayoutContainer layoutContainer;
                float dp;
                Drawable drawable;
                int i;
                if (ActionBarLayout.this.animationRunnable != this) {
                    return;
                }
                ActionBarLayout.this.animationRunnable = null;
                if (z2) {
                    ActionBarLayout.this.transitionAnimationStartTime = System.currentTimeMillis();
                }
                long nanoTime = System.nanoTime() / 1000000;
                long j = nanoTime - ActionBarLayout.this.lastFrameTime;
                if (j > 40 && z2) {
                    j = 0;
                } else if (j > 18) {
                    j = 18;
                }
                ActionBarLayout.this.lastFrameTime = nanoTime;
                ActionBarLayout.access$1216(ActionBarLayout.this, ((float) j) / ((z3 && z) ? 190.0f : 150.0f));
                if (ActionBarLayout.this.animationProgress > 1.0f) {
                    ActionBarLayout.this.animationProgress = 1.0f;
                }
                if (ActionBarLayout.this.newFragment != null) {
                    ActionBarLayout.this.newFragment.onTransitionAnimationProgress(true, ActionBarLayout.this.animationProgress);
                }
                if (ActionBarLayout.this.oldFragment != null) {
                    ActionBarLayout.this.oldFragment.onTransitionAnimationProgress(false, ActionBarLayout.this.animationProgress);
                }
                Integer valueOf = ActionBarLayout.this.oldFragment != null ? Integer.valueOf(ActionBarLayout.this.oldFragment.getNavigationBarColor()) : null;
                Integer valueOf2 = ActionBarLayout.this.newFragment != null ? Integer.valueOf(ActionBarLayout.this.newFragment.getNavigationBarColor()) : null;
                if (ActionBarLayout.this.newFragment != null && valueOf != null) {
                    int blendARGB = ColorUtils.blendARGB(valueOf.intValue(), valueOf2.intValue(), MathUtils.clamp(ActionBarLayout.this.animationProgress * 4.0f, 0.0f, 1.0f));
                    if (ActionBarLayout.this.sheetFragment != null && ActionBarLayout.this.sheetFragment.sheetsStack != null) {
                        for (int i2 = 0; i2 < ActionBarLayout.this.sheetFragment.sheetsStack.size(); i2++) {
                            BaseFragment.AttachedSheet attachedSheet = (BaseFragment.AttachedSheet) ActionBarLayout.this.sheetFragment.sheetsStack.get(i2);
                            if (attachedSheet.attachedToParent()) {
                                blendARGB = attachedSheet.getNavigationBarColor(blendARGB);
                            }
                        }
                    }
                    ActionBarLayout.this.newFragment.setNavigationBarColor(blendARGB);
                }
                float interpolation = z3 ? z ? ActionBarLayout.this.overshootInterpolator.getInterpolation(ActionBarLayout.this.animationProgress) : CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(ActionBarLayout.this.animationProgress) : ActionBarLayout.this.decelerateInterpolator.getInterpolation(ActionBarLayout.this.animationProgress);
                if (z) {
                    float clamp = MathUtils.clamp(interpolation, 0.0f, 1.0f);
                    ActionBarLayout.this.containerView.setAlpha(clamp);
                    if (z3) {
                        float f = (0.3f * interpolation) + 0.7f;
                        ActionBarLayout.this.containerView.setScaleX(f);
                        ActionBarLayout.this.containerView.setScaleY(f);
                        if (ActionBarLayout.this.previewMenu != null) {
                            float f2 = 1.0f - interpolation;
                            ActionBarLayout.this.containerView.setTranslationY(AndroidUtilities.dp(40.0f) * f2);
                            ActionBarLayout.this.previewMenu.setTranslationY((-AndroidUtilities.dp(70.0f)) * f2);
                            float f3 = (interpolation * 0.05f) + 0.95f;
                            ActionBarLayout.this.previewMenu.setScaleX(f3);
                            ActionBarLayout.this.previewMenu.setScaleY(f3);
                        }
                        ActionBarLayout.this.previewBackgroundDrawable.setAlpha((int) (46.0f * clamp));
                        drawable = Theme.moveUpDrawable;
                        i = (int) (clamp * 255.0f);
                        drawable.setAlpha(i);
                        ActionBarLayout.this.containerView.invalidate();
                        ActionBarLayout.this.invalidate();
                        if (ActionBarLayout.this.animationProgress >= 1.0f) {
                            ActionBarLayout.this.startLayoutAnimation(z, false, z3);
                            return;
                        } else {
                            ActionBarLayout.this.onAnimationEndCheck(false);
                            return;
                        }
                    }
                    layoutContainer = ActionBarLayout.this.containerView;
                    dp = AndroidUtilities.dp(48.0f);
                    interpolation = 1.0f - interpolation;
                    layoutContainer.setTranslationX(dp * interpolation);
                    if (ActionBarLayout.this.animationProgress >= 1.0f) {
                    }
                } else {
                    float f4 = 1.0f - interpolation;
                    float clamp2 = MathUtils.clamp(f4, 0.0f, 1.0f);
                    ActionBarLayout.this.containerViewBack.setAlpha(clamp2);
                    if (z3) {
                        float f5 = (f4 * 0.1f) + 0.9f;
                        ActionBarLayout.this.containerViewBack.setScaleX(f5);
                        ActionBarLayout.this.containerViewBack.setScaleY(f5);
                        ActionBarLayout.this.previewBackgroundDrawable.setAlpha((int) (46.0f * clamp2));
                        if (ActionBarLayout.this.previewMenu == null) {
                            drawable = Theme.moveUpDrawable;
                            i = (int) (clamp2 * 255.0f);
                            drawable.setAlpha(i);
                        }
                        ActionBarLayout.this.containerView.invalidate();
                        ActionBarLayout.this.invalidate();
                        if (ActionBarLayout.this.animationProgress >= 1.0f) {
                        }
                    } else {
                        layoutContainer = ActionBarLayout.this.containerViewBack;
                        dp = AndroidUtilities.dp(48.0f);
                        layoutContainer.setTranslationX(dp * interpolation);
                        if (ActionBarLayout.this.animationProgress >= 1.0f) {
                        }
                    }
                }
            }
        };
        this.animationRunnable = runnable;
        AndroidUtilities.runOnUIThread(runnable);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* synthetic */ boolean addFragmentToStack(BaseFragment baseFragment) {
        return INavigationLayout.-CC.$default$addFragmentToStack(this, baseFragment);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public boolean addFragmentToStack(BaseFragment baseFragment, int i) {
        String str;
        ViewGroup viewGroup;
        ViewGroup viewGroup2;
        INavigationLayout.INavigationLayoutDelegate iNavigationLayoutDelegate = this.delegate;
        if ((iNavigationLayoutDelegate == null || iNavigationLayoutDelegate.needAddFragmentToStack(baseFragment, this)) && baseFragment.onFragmentCreate() && !this.fragmentsStack.contains(baseFragment)) {
            baseFragment.setParentLayout(this);
            if (i == -1 || i == -2) {
                if (!this.fragmentsStack.isEmpty()) {
                    List list = this.fragmentsStack;
                    BaseFragment baseFragment2 = (BaseFragment) list.get(list.size() - 1);
                    baseFragment2.onPause();
                    ActionBar actionBar = baseFragment2.actionBar;
                    if (actionBar != null && actionBar.shouldAddToContainer() && (viewGroup2 = (ViewGroup) baseFragment2.actionBar.getParent()) != null) {
                        viewGroup2.removeView(baseFragment2.actionBar);
                    }
                    View view = baseFragment2.fragmentView;
                    if (view != null && (viewGroup = (ViewGroup) view.getParent()) != null) {
                        baseFragment2.onRemoveFromParent();
                        viewGroup.removeView(baseFragment2.fragmentView);
                    }
                    baseFragment2.detachSheets();
                }
                this.fragmentsStack.add(baseFragment);
                if (i != -2) {
                    attachView(baseFragment);
                    baseFragment.onResume();
                    baseFragment.onTransitionAnimationEnd(false, true);
                    baseFragment.onTransitionAnimationEnd(true, true);
                    baseFragment.onBecomeFullyVisible();
                }
                str = "addFragmentToStack " + i;
            } else {
                if (i == -3) {
                    attachViewTo(baseFragment, 0);
                    i = 0;
                }
                this.fragmentsStack.add(i, baseFragment);
                str = "addFragmentToStack";
            }
            onFragmentStackChanged(str);
            if (!this.useAlphaAnimations) {
                setVisibility(0);
                View view2 = this.backgroundView;
                if (view2 != null) {
                    view2.setVisibility(0);
                }
            }
            return true;
        }
        return false;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public boolean allowSwipe() {
        EmptyBaseFragment emptyBaseFragment = this.sheetFragment;
        return emptyBaseFragment == null || emptyBaseFragment.getLastSheet() == null || !this.sheetFragment.getLastSheet().isShown();
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void animateThemedValues(final INavigationLayout.ThemeAnimationSettings themeAnimationSettings, final Runnable runnable) {
        Theme.ThemeInfo themeInfo;
        if (this.transitionAnimationInProgress || this.startedTracking) {
            this.animateThemeAfterAnimation = true;
            this.animateSetThemeAfterAnimation = themeAnimationSettings.theme;
            this.animateSetThemeNightAfterAnimation = themeAnimationSettings.nightTheme;
            this.animateSetThemeAccentIdAfterAnimation = themeAnimationSettings.accentId;
            this.animateSetThemeAfterAnimationApply = themeAnimationSettings.applyTrulyTheme;
            if (runnable != null) {
                runnable.run();
                return;
            }
            return;
        }
        AnimatorSet animatorSet = this.themeAnimatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.themeAnimatorSet = null;
        }
        final int size = themeAnimationSettings.onlyTopFragment ? 1 : this.fragmentsStack.size();
        final Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.ActionBar.ActionBarLayout$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                ActionBarLayout.this.lambda$animateThemedValues$7(size, themeAnimationSettings, runnable);
            }
        };
        if (size >= 1 && themeAnimationSettings.applyTheme && themeAnimationSettings.applyTrulyTheme) {
            int i = themeAnimationSettings.accentId;
            if (i != -1 && (themeInfo = themeAnimationSettings.theme) != null) {
                themeInfo.setCurrentAccentId(i);
                Theme.saveThemeAccents(themeAnimationSettings.theme, true, false, true, false);
            }
            if (runnable != null) {
                Theme.applyThemeInBackground(themeAnimationSettings.theme, themeAnimationSettings.nightTheme, new Runnable() { // from class: org.telegram.ui.ActionBar.ActionBarLayout$$ExternalSyntheticLambda12
                    @Override // java.lang.Runnable
                    public final void run() {
                        AndroidUtilities.runOnUIThread(runnable2);
                    }
                });
                return;
            }
            Theme.applyTheme(themeAnimationSettings.theme, themeAnimationSettings.nightTheme);
        }
        runnable2.run();
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* synthetic */ void animateThemedValues(Theme.ThemeInfo themeInfo, int i, boolean z, boolean z2) {
        INavigationLayout.-CC.$default$animateThemedValues(this, themeInfo, i, z, z2);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* synthetic */ void animateThemedValues(Theme.ThemeInfo themeInfo, int i, boolean z, boolean z2, Runnable runnable) {
        INavigationLayout.-CC.$default$animateThemedValues(this, themeInfo, i, z, z2, runnable);
    }

    public void bringToFront(int i) {
        ViewGroup viewGroup;
        ViewGroup viewGroup2;
        if (this.fragmentsStack.isEmpty()) {
            return;
        }
        if (this.fragmentsStack.isEmpty() || this.fragmentsStack.size() - 1 != i || ((BaseFragment) this.fragmentsStack.get(i)).fragmentView == null) {
            for (int i2 = 0; i2 < i; i2++) {
                BaseFragment baseFragment = (BaseFragment) this.fragmentsStack.get(i2);
                ActionBar actionBar = baseFragment.actionBar;
                if (actionBar != null && actionBar.shouldAddToContainer() && (viewGroup2 = (ViewGroup) baseFragment.actionBar.getParent()) != null) {
                    viewGroup2.removeView(baseFragment.actionBar);
                }
                View view = baseFragment.fragmentView;
                if (view != null && (viewGroup = (ViewGroup) view.getParent()) != null) {
                    baseFragment.onPause();
                    baseFragment.onRemoveFromParent();
                    viewGroup.removeView(baseFragment.fragmentView);
                }
            }
            BaseFragment baseFragment2 = (BaseFragment) this.fragmentsStack.get(i);
            baseFragment2.setParentLayout(this);
            View view2 = baseFragment2.fragmentView;
            if (view2 == null) {
                view2 = baseFragment2.createView(this.parentActivity);
            } else {
                ViewGroup viewGroup3 = (ViewGroup) view2.getParent();
                if (viewGroup3 != null) {
                    baseFragment2.onRemoveFromParent();
                    viewGroup3.removeView(view2);
                }
            }
            this.containerView.addView(view2, LayoutHelper.createFrame(-1, -1.0f));
            ActionBar actionBar2 = baseFragment2.actionBar;
            if (actionBar2 != null && actionBar2.shouldAddToContainer()) {
                if (this.removeActionBarExtraHeight) {
                    baseFragment2.actionBar.setOccupyStatusBar(false);
                }
                AndroidUtilities.removeFromParent(baseFragment2.actionBar);
                this.containerView.addView(baseFragment2.actionBar);
                baseFragment2.actionBar.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, this.overlayAction);
            }
            baseFragment2.attachSheets(this.containerView);
            baseFragment2.onResume();
            this.currentActionBar = baseFragment2.actionBar;
            if (baseFragment2.hasOwnBackground || view2.getBackground() != null) {
                return;
            }
            view2.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        }
    }

    public void checkBlackScreen(String str) {
        if (BuildVars.DEBUG_VERSION) {
            this.lastActions.add(0, str + " " + this.fragmentsStack.size());
            if (this.lastActions.size() > 20) {
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < 10; i++) {
                    arrayList.add((String) this.lastActions.get(i));
                }
                this.lastActions = arrayList;
            }
        }
        AndroidUtilities.cancelRunOnUIThread(this.debugBlackScreenRunnable);
        AndroidUtilities.runOnUIThread(this.debugBlackScreenRunnable, 500L);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public boolean checkTransitionAnimation() {
        if (this.transitionAnimationPreviewMode) {
            return false;
        }
        if (this.transitionAnimationInProgress && (this.transitionAnimationStartTime < System.currentTimeMillis() - 1500 || this.inPreviewMode)) {
            onAnimationEndCheck(true);
        }
        return this.transitionAnimationInProgress;
    }

    public void clipBottomSheetTabs(Canvas canvas, boolean z) {
        if (this.bottomSheetTabs == null) {
            return;
        }
        int bottomTabsHeight = this.isKeyboardVisible ? 0 : getBottomTabsHeight(true);
        int min = Math.min(1, bottomTabsHeight / AndroidUtilities.dp(60.0f)) * AndroidUtilities.dp(10.0f);
        if (bottomTabsHeight <= 0) {
            return;
        }
        float[] fArr = this.clipRadius;
        fArr[3] = 0.0f;
        fArr[2] = 0.0f;
        fArr[1] = 0.0f;
        fArr[0] = 0.0f;
        float f = min;
        fArr[7] = f;
        fArr[6] = f;
        fArr[5] = f;
        fArr[4] = f;
        this.clipPath.rewind();
        this.clipRect.set(0.0f, 0.0f, getWidth(), (this.bottomSheetTabs.getY() + this.bottomSheetTabs.getHeight()) - bottomTabsHeight);
        this.clipPath.addRoundRect(this.clipRect, this.clipRadius, Path.Direction.CW);
        this.clipShadowPaint.setAlpha(0);
        if (z) {
            this.clipShadowPaint.setShadowLayer(AndroidUtilities.dp(2.0f), 0.0f, AndroidUtilities.dp(1.0f), 268435456);
            canvas.drawPath(this.clipPath, this.clipShadowPaint);
        }
        canvas.clipPath(this.clipPath);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* synthetic */ void closeLastFragment() {
        INavigationLayout.-CC.$default$closeLastFragment(this);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void closeLastFragment(boolean z) {
        closeLastFragment(z, false);
    }

    public void closeLastFragment(boolean z, boolean z2) {
        final BaseFragment baseFragment;
        BaseFragment lastFragment = getLastFragment();
        if (lastFragment == null || !lastFragment.closeLastFragment()) {
            INavigationLayout.INavigationLayoutDelegate iNavigationLayoutDelegate = this.delegate;
            if ((iNavigationLayoutDelegate != null && !iNavigationLayoutDelegate.needCloseLastFragment(this)) || checkTransitionAnimation() || this.fragmentsStack.isEmpty()) {
                return;
            }
            if (this.parentActivity.getCurrentFocus() != null) {
                AndroidUtilities.hideKeyboard(this.parentActivity.getCurrentFocus());
            }
            setInnerTranslationX(0.0f);
            boolean z3 = !z2 && (this.inPreviewMode || this.transitionAnimationPreviewMode || (z && MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)));
            List list = this.fragmentsStack;
            final BaseFragment baseFragment2 = (BaseFragment) list.get(list.size() - 1);
            AnimatorSet animatorSet = null;
            if (this.fragmentsStack.size() > 1) {
                List list2 = this.fragmentsStack;
                baseFragment = (BaseFragment) list2.get(list2.size() - 2);
            } else {
                baseFragment = null;
            }
            if (baseFragment != null) {
                AndroidUtilities.setLightStatusBar(this.parentActivity.getWindow(), Theme.getColor(Theme.key_actionBarDefault) == -1 || (baseFragment.hasForceLightStatusBar() && !Theme.getCurrentTheme().isDark()), baseFragment.hasForceLightStatusBar());
                LayoutContainer layoutContainer = this.containerView;
                this.containerView = this.containerViewBack;
                this.containerViewBack = layoutContainer;
                baseFragment.setParentLayout(this);
                View view = baseFragment.fragmentView;
                if (view == null) {
                    view = baseFragment.createView(this.parentActivity);
                }
                if (!this.inPreviewMode) {
                    this.containerView.setVisibility(0);
                    ViewGroup viewGroup = (ViewGroup) view.getParent();
                    if (viewGroup != null) {
                        baseFragment.onRemoveFromParent();
                        try {
                            viewGroup.removeView(view);
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                    this.containerView.addView(view);
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                    layoutParams.width = -1;
                    layoutParams.height = -1;
                    layoutParams.leftMargin = 0;
                    layoutParams.rightMargin = 0;
                    layoutParams.bottomMargin = 0;
                    layoutParams.topMargin = 0;
                    view.setLayoutParams(layoutParams);
                    ActionBar actionBar = baseFragment.actionBar;
                    if (actionBar != null && actionBar.shouldAddToContainer()) {
                        if (this.removeActionBarExtraHeight) {
                            baseFragment.actionBar.setOccupyStatusBar(false);
                        }
                        AndroidUtilities.removeFromParent(baseFragment.actionBar);
                        this.containerView.addView(baseFragment.actionBar);
                        baseFragment.actionBar.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, this.overlayAction);
                    }
                    baseFragment.attachSheets(this.containerView);
                }
                this.newFragment = baseFragment;
                this.oldFragment = baseFragment2;
                baseFragment.onTransitionAnimationStart(true, true);
                baseFragment2.onTransitionAnimationStart(false, true);
                baseFragment.onResume();
                if (this.themeAnimatorSet != null) {
                    this.presentingFragmentDescriptions = baseFragment.getThemeDescriptions();
                }
                this.currentActionBar = baseFragment.actionBar;
                if (!baseFragment.hasOwnBackground && view.getBackground() == null) {
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                }
                if (z3) {
                    this.transitionAnimationStartTime = System.currentTimeMillis();
                    this.transitionAnimationInProgress = true;
                    this.layoutToIgnore = this.containerView;
                    baseFragment2.setRemovingFromStack(true);
                    this.onCloseAnimationEndRunnable = new Runnable() { // from class: org.telegram.ui.ActionBar.ActionBarLayout$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            ActionBarLayout.this.lambda$closeLastFragment$3(baseFragment2, baseFragment);
                        }
                    };
                    if (!this.inPreviewMode && !this.transitionAnimationPreviewMode) {
                        animatorSet = baseFragment2.onCustomTransitionAnimation(false, new Runnable() { // from class: org.telegram.ui.ActionBar.ActionBarLayout$$ExternalSyntheticLambda4
                            @Override // java.lang.Runnable
                            public final void run() {
                                ActionBarLayout.this.lambda$closeLastFragment$4();
                            }
                        });
                    }
                    if (animatorSet != null) {
                        this.currentAnimation = animatorSet;
                        if (Bulletin.getVisibleBulletin() != null && Bulletin.getVisibleBulletin().isShowing()) {
                            Bulletin.getVisibleBulletin().hide();
                        }
                    } else if (this.inPreviewMode || !(this.containerView.isKeyboardVisible || this.containerViewBack.isKeyboardVisible)) {
                        startLayoutAnimation(false, true, this.inPreviewMode || this.transitionAnimationPreviewMode);
                    } else {
                        Runnable runnable = new Runnable() { // from class: org.telegram.ui.ActionBar.ActionBarLayout.9
                            @Override // java.lang.Runnable
                            public void run() {
                                if (ActionBarLayout.this.waitingForKeyboardCloseRunnable != this) {
                                    return;
                                }
                                ActionBarLayout.this.waitingForKeyboardCloseRunnable = null;
                                ActionBarLayout.this.startLayoutAnimation(false, true, false);
                            }
                        };
                        this.waitingForKeyboardCloseRunnable = runnable;
                        AndroidUtilities.runOnUIThread(runnable, 200L);
                    }
                    onFragmentStackChanged("closeLastFragment");
                } else {
                    closeLastFragmentInternalRemoveOld(baseFragment2);
                    baseFragment2.onTransitionAnimationEnd(false, true);
                    baseFragment.onTransitionAnimationEnd(true, true);
                    baseFragment.onBecomeFullyVisible();
                }
            } else if (!this.useAlphaAnimations || z2) {
                removeFragmentFromStackInternal(baseFragment2, false);
                setVisibility(8);
                View view2 = this.backgroundView;
                if (view2 != null) {
                    view2.setVisibility(8);
                }
            } else {
                this.transitionAnimationStartTime = System.currentTimeMillis();
                this.transitionAnimationInProgress = true;
                this.layoutToIgnore = this.containerView;
                this.onCloseAnimationEndRunnable = new Runnable() { // from class: org.telegram.ui.ActionBar.ActionBarLayout$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        ActionBarLayout.this.lambda$closeLastFragment$5(baseFragment2);
                    }
                };
                ArrayList arrayList = new ArrayList();
                Property property = View.ALPHA;
                arrayList.add(ObjectAnimator.ofFloat(this, property, 1.0f, 0.0f));
                View view3 = this.backgroundView;
                if (view3 != null) {
                    arrayList.add(ObjectAnimator.ofFloat(view3, property, 1.0f, 0.0f));
                }
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.currentAnimation = animatorSet2;
                animatorSet2.playTogether(arrayList);
                this.currentAnimation.setInterpolator(this.accelerateDecelerateInterpolator);
                this.currentAnimation.setDuration(200L);
                this.currentAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.ActionBarLayout.10
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        ActionBarLayout.this.onAnimationEndCheck(false);
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationStart(Animator animator) {
                        ActionBarLayout.this.transitionAnimationStartTime = System.currentTimeMillis();
                    }
                });
                this.currentAnimation.start();
            }
            baseFragment2.onFragmentClosed();
        }
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* synthetic */ void dismissDialogs() {
        INavigationLayout.-CC.$default$dismissDialogs(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        this.withShadow = true;
        super.dispatchDraw(canvas);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEventPreIme(KeyEvent keyEvent) {
        if (keyEvent != null && keyEvent.getKeyCode() == 4 && keyEvent.getAction() == 1) {
            INavigationLayout.INavigationLayoutDelegate iNavigationLayoutDelegate = this.delegate;
            return (iNavigationLayoutDelegate != null && iNavigationLayoutDelegate.onPreIme()) || super.dispatchKeyEventPreIme(keyEvent);
        }
        return super.dispatchKeyEventPreIme(keyEvent);
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0032, code lost:
        if (r1.getWindowView() != null) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0059, code lost:
        if (r1.getWindowView() != null) goto L43;
     */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0060  */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        BaseFragment.AttachedSheet attachedSheet;
        boolean z = motionEvent.getY() > ((float) (getHeight() - getBottomTabsHeight(true)));
        EmptyBaseFragment emptyBaseFragment = this.sheetFragment;
        BaseFragment.AttachedSheet attachedSheet2 = null;
        if (emptyBaseFragment != null && emptyBaseFragment.getLastSheet() != null) {
            attachedSheet = this.sheetFragment.getLastSheet();
            if (attachedSheet.attachedToParent()) {
            }
        }
        attachedSheet = null;
        if (attachedSheet == null && getLastFragment() != null && getLastFragment().getLastSheet() != null) {
            attachedSheet = getLastFragment().getLastSheet();
            if (attachedSheet.attachedToParent()) {
            }
            if (attachedSheet2 != null) {
                if (motionEvent.getAction() == 0) {
                    this.tabsEvents = z;
                }
                if (!this.tabsEvents) {
                    if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                        this.tabsEvents = false;
                    }
                    return attachedSheet2.getWindowView().dispatchTouchEvent(motionEvent);
                }
            }
            if (motionEvent.getAction() != 1 || motionEvent.getAction() == 3) {
                this.tabsEvents = false;
            }
            return super.dispatchTouchEvent(motionEvent);
        }
        attachedSheet2 = attachedSheet;
        if (attachedSheet2 != null) {
        }
        if (motionEvent.getAction() != 1) {
        }
        this.tabsEvents = false;
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        LayoutContainer layoutContainer;
        float f;
        float height;
        Paint paint;
        Canvas canvas2;
        float f2;
        float f3;
        DrawerLayoutContainer drawerLayoutContainer = this.drawerLayoutContainer;
        if (drawerLayoutContainer != null && drawerLayoutContainer.isDrawCurrentPreviewFragmentAbove() && (this.inPreviewMode || this.transitionAnimationPreviewMode || this.previewOpenAnimationInProgress)) {
            BaseFragment baseFragment = this.oldFragment;
            if (view == ((baseFragment == null || !baseFragment.inPreviewMode) ? this.containerView : this.containerViewBack)) {
                this.drawerLayoutContainer.invalidate();
                return false;
            }
        }
        int width = (getWidth() - getPaddingLeft()) - getPaddingRight();
        int paddingRight = ((int) this.innerTranslationX) + getPaddingRight();
        int paddingLeft = getPaddingLeft();
        int paddingLeft2 = getPaddingLeft() + width;
        if (view == this.containerViewBack) {
            paddingLeft2 = AndroidUtilities.dp(1.0f) + paddingRight;
        } else if (view == this.containerView) {
            paddingLeft = paddingRight;
        }
        int save = canvas.save();
        if (view != this.bottomSheetTabs) {
            clipBottomSheetTabs(canvas, this.withShadow);
            this.withShadow = false;
        }
        int save2 = canvas.save();
        if (!isTransitionAnimationInProgress() && !this.inPreviewMode) {
            canvas.clipRect(paddingLeft, 0, paddingLeft2, getHeight());
        }
        if ((this.inPreviewMode || this.transitionAnimationPreviewMode) && view == (layoutContainer = this.containerView)) {
            drawPreviewDrawables(canvas, layoutContainer);
        }
        boolean drawChild = super.drawChild(canvas, view, j);
        canvas.restoreToCount(save2);
        if (paddingRight != 0 || this.overrideWidthOffset != -1) {
            int i = this.overrideWidthOffset;
            if (i == -1) {
                i = width - paddingRight;
            }
            if (view == this.containerView) {
                float clamp = MathUtils.clamp(i / AndroidUtilities.dp(20.0f), 0.0f, 1.0f);
                Drawable drawable = layerShadowDrawable;
                drawable.setBounds(paddingRight - drawable.getIntrinsicWidth(), view.getTop(), paddingRight, view.getBottom() - getBottomTabsHeight(true));
                layerShadowDrawable.setAlpha((int) (clamp * 255.0f));
                layerShadowDrawable.draw(canvas);
            } else if (view == this.containerViewBack) {
                scrimPaint.setColor(Color.argb((int) (MathUtils.clamp(i / width, 0.0f, 0.8f) * 153.0f), 0, 0, 0));
                if (this.overrideWidthOffset != -1) {
                    f = getWidth();
                    height = getHeight() * 1.5f;
                    paint = scrimPaint;
                    f2 = 0.0f;
                    f3 = 0.0f;
                    canvas2 = canvas;
                } else {
                    float f4 = paddingLeft;
                    f = paddingLeft2;
                    height = getHeight() * 1.5f;
                    paint = scrimPaint;
                    canvas2 = canvas;
                    f2 = f4;
                    f3 = 0.0f;
                }
                canvas2.drawRect(f2, f3, f, height, paint);
            }
        }
        canvas.restoreToCount(save);
        return drawChild;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void drawCurrentPreviewFragment(Canvas canvas, Drawable drawable) {
        if (this.inPreviewMode || this.transitionAnimationPreviewMode || this.previewOpenAnimationInProgress) {
            BaseFragment baseFragment = this.oldFragment;
            LayoutContainer layoutContainer = (baseFragment == null || !baseFragment.inPreviewMode) ? this.containerView : this.containerViewBack;
            drawPreviewDrawables(canvas, layoutContainer);
            if (layoutContainer.getAlpha() < 1.0f) {
                canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) (layoutContainer.getAlpha() * 255.0f), 31);
            } else {
                canvas.save();
            }
            canvas.concat(layoutContainer.getMatrix());
            layoutContainer.draw(canvas);
            if (drawable != null) {
                View childAt = layoutContainer.getChildAt(0);
                if (childAt != null) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
                    Rect rect = new Rect();
                    childAt.getLocalVisibleRect(rect);
                    rect.offset(marginLayoutParams.leftMargin, marginLayoutParams.topMargin);
                    rect.top += Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight - 1 : 0;
                    drawable.setAlpha((int) (layoutContainer.getAlpha() * 255.0f));
                    drawable.setBounds(rect);
                    drawable.draw(canvas);
                }
            }
            canvas.restore();
        }
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* synthetic */ void drawHeaderShadow(Canvas canvas, int i) {
        INavigationLayout.-CC.$default$drawHeaderShadow(this, canvas, i);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void drawHeaderShadow(Canvas canvas, int i, int i2) {
        if (headerShadowDrawable == null || !SharedConfig.drawActionBarShadow) {
            return;
        }
        int i3 = i / 2;
        if (headerShadowDrawable.getAlpha() != i3) {
            headerShadowDrawable.setAlpha(i3);
        }
        headerShadowDrawable.setBounds(0, i2, getMeasuredWidth(), headerShadowDrawable.getIntrinsicHeight() + i2);
        headerShadowDrawable.draw(canvas);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void expandPreviewFragment() {
        boolean z = true;
        this.previewOpenAnimationInProgress = true;
        this.inPreviewMode = false;
        List list = this.fragmentsStack;
        BaseFragment baseFragment = (BaseFragment) list.get(list.size() - 2);
        List list2 = this.fragmentsStack;
        final BaseFragment baseFragment2 = (BaseFragment) list2.get(list2.size() - 1);
        if (Build.VERSION.SDK_INT >= 21) {
            baseFragment2.fragmentView.setOutlineProvider(null);
            baseFragment2.fragmentView.setClipToOutline(false);
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) baseFragment2.fragmentView.getLayoutParams();
        layoutParams.leftMargin = 0;
        layoutParams.rightMargin = 0;
        layoutParams.bottomMargin = 0;
        layoutParams.topMargin = 0;
        layoutParams.height = -1;
        baseFragment2.fragmentView.setLayoutParams(layoutParams);
        presentFragmentInternalRemoveOld(false, baseFragment);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(baseFragment2.fragmentView, View.SCALE_X, 1.0f, 1.05f, 1.0f), ObjectAnimator.ofFloat(baseFragment2.fragmentView, View.SCALE_Y, 1.0f, 1.05f, 1.0f));
        animatorSet.setDuration(200L);
        animatorSet.setInterpolator(new CubicBezierInterpolator(0.42d, 0.0d, 0.58d, 1.0d));
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.ActionBarLayout.8
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ActionBarLayout.this.previewOpenAnimationInProgress = false;
                baseFragment2.onPreviewOpenAnimationEnd();
            }
        });
        animatorSet.start();
        performHapticFeedback(3);
        baseFragment2.setInPreviewMode(false);
        baseFragment2.setInMenuMode(false);
        try {
            Window window = this.parentActivity.getWindow();
            if (Theme.getColor(Theme.key_actionBarDefault) != -1 && (!baseFragment2.hasForceLightStatusBar() || Theme.getCurrentTheme().isDark())) {
                z = false;
            }
            AndroidUtilities.setLightStatusBar(window, z, baseFragment2.hasForceLightStatusBar());
        } catch (Exception unused) {
        }
    }

    public boolean extendActionMode(Menu menu) {
        if (!this.fragmentsStack.isEmpty()) {
            List list = this.fragmentsStack;
            if (((BaseFragment) list.get(list.size() - 1)).extendActionMode(menu)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void finishPreviewFragment() {
        if (this.inPreviewMode || this.transitionAnimationPreviewMode) {
            Runnable runnable = this.delayedOpenAnimationRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.delayedOpenAnimationRunnable = null;
            }
            closeLastFragment(true);
        }
    }

    public /* bridge */ /* synthetic */ BaseFragment getBackgroundFragment() {
        return INavigationLayout.-CC.$default$getBackgroundFragment(this);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* bridge */ /* synthetic */ BottomSheet getBottomSheet() {
        return INavigationLayout.-CC.$default$getBottomSheet(this);
    }

    public BottomSheetTabs getBottomSheetTabs() {
        return this.bottomSheetTabs;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public int getBottomTabsHeight(boolean z) {
        if (this.main) {
            return z ? (int) this.bottomTabsProgress : this.bottomTabsHeight;
        }
        return 0;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public float getCurrentPreviewFragmentAlpha() {
        if (this.inPreviewMode || this.transitionAnimationPreviewMode || this.previewOpenAnimationInProgress) {
            BaseFragment baseFragment = this.oldFragment;
            return ((baseFragment == null || !baseFragment.inPreviewMode) ? this.containerView : this.containerViewBack).getAlpha();
        }
        return 0.0f;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public DrawerLayoutContainer getDrawerLayoutContainer() {
        return this.drawerLayoutContainer;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public List<BaseFragment> getFragmentStack() {
        return this.fragmentsStack;
    }

    public float getInnerTranslationX() {
        return this.innerTranslationX;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public BaseFragment getLastFragment() {
        if (this.fragmentsStack.isEmpty()) {
            return null;
        }
        List list = this.fragmentsStack;
        return (BaseFragment) list.get(list.size() - 1);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public Theme.MessageDrawable getMessageDrawableOutMediaStart() {
        return this.messageDrawableOutMediaStart;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public Theme.MessageDrawable getMessageDrawableOutStart() {
        return this.messageDrawableOutStart;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public FrameLayout getOverlayContainerView() {
        return this;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* bridge */ /* synthetic */ Activity getParentActivity() {
        return INavigationLayout.-CC.$default$getParentActivity(this);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public List<BackButtonMenu.PulledDialog> getPulledDialogs() {
        return this.pulledDialogs;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* bridge */ /* synthetic */ BaseFragment getSafeLastFragment() {
        return INavigationLayout.-CC.$default$getSafeLastFragment(this);
    }

    public EmptyBaseFragment getSheetFragment() {
        return getSheetFragment(true);
    }

    public EmptyBaseFragment getSheetFragment(boolean z) {
        if (this.parentActivity == null) {
            return null;
        }
        if (this.sheetFragment == null) {
            EmptyBaseFragment emptyBaseFragment = new EmptyBaseFragment();
            this.sheetFragment = emptyBaseFragment;
            emptyBaseFragment.setParentLayout(this);
            EmptyBaseFragment emptyBaseFragment2 = this.sheetFragment;
            View view = emptyBaseFragment2.fragmentView;
            if (view == null) {
                view = emptyBaseFragment2.createView(this.parentActivity);
            }
            if (view.getParent() != this.sheetContainer) {
                AndroidUtilities.removeFromParent(view);
                this.sheetContainer.addView(view, LayoutHelper.createFrame(-1, -1.0f));
            }
            this.sheetFragment.onResume();
            this.sheetFragment.onBecomeFullyVisible();
        }
        return this.sheetFragment;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public float getThemeAnimationValue() {
        return this.themeAnimationValue;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* bridge */ /* synthetic */ ViewGroup getView() {
        return INavigationLayout.-CC.$default$getView(this);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public Window getWindow() {
        Window window = this.window;
        if (window != null) {
            return window;
        }
        if (getParentActivity() != null) {
            return getParentActivity().getWindow();
        }
        return null;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* synthetic */ boolean hasIntegratedBlurInPreview() {
        return INavigationLayout.-CC.$default$hasIntegratedBlurInPreview(this);
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* synthetic */ boolean isActionBarInCrossfade() {
        return INavigationLayout.-CC.$default$isActionBarInCrossfade(this);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public boolean isInBubbleMode() {
        return this.inBubbleMode;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public boolean isInPreviewMode() {
        return this.inPreviewMode || this.transitionAnimationPreviewMode;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public boolean isPreviewOpenAnimationInProgress() {
        return this.previewOpenAnimationInProgress;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public boolean isSheet() {
        return this.isSheet;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public boolean isSwipeInProgress() {
        return this.startedTracking;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public boolean isTransitionAnimationInProgress() {
        return this.transitionAnimationInProgress || this.animationInProgress;
    }

    public int measureKeyboardHeight() {
        View rootView = getRootView();
        getWindowVisibleDisplayFrame(this.rect);
        Rect rect = this.rect;
        if (rect.bottom == 0 && rect.top == 0) {
            return 0;
        }
        int height = (rootView.getHeight() - (this.rect.top != 0 ? AndroidUtilities.statusBarHeight : 0)) - AndroidUtilities.getViewInset(rootView);
        Rect rect2 = this.rect;
        return Math.max(0, height - (rect2.bottom - rect2.top));
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x002f  */
    /* JADX WARN: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    @Override // org.telegram.ui.ActionBar.INavigationLayout
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void movePreviewFragment(float f) {
        if (!this.inPreviewMode || this.previewMenu != null || this.transitionAnimationPreviewMode) {
            return;
        }
        float translationY = this.containerView.getTranslationY();
        float f2 = -f;
        if (f2 <= 0.0f) {
            if (f2 < (-AndroidUtilities.dp(60.0f))) {
                expandPreviewFragment();
            }
            if (translationY == f2) {
                this.containerView.setTranslationY(f2);
                invalidate();
                return;
            }
            return;
        }
        f2 = 0.0f;
        if (translationY == f2) {
        }
    }

    public void onActionModeFinished(Object obj) {
        ActionBar actionBar = this.currentActionBar;
        if (actionBar != null) {
            actionBar.setVisibility(0);
        }
        this.inActionMode = false;
    }

    public void onActionModeStarted(Object obj) {
        ActionBar actionBar = this.currentActionBar;
        if (actionBar != null) {
            actionBar.setVisibility(8);
        }
        this.inActionMode = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attached = true;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void onBackPressed() {
        ActionBar actionBar;
        if (this.transitionAnimationPreviewMode || this.startedTracking || checkTransitionAnimation() || this.fragmentsStack.isEmpty() || GroupCallPip.onBackPressed()) {
            return;
        }
        if (!storyViewerAttached() && (actionBar = this.currentActionBar) != null && !actionBar.isActionModeShowed()) {
            ActionBar actionBar2 = this.currentActionBar;
            if (actionBar2.isSearchFieldVisible) {
                actionBar2.closeSearchField();
                return;
            }
        }
        EmptyBaseFragment emptyBaseFragment = this.sheetFragment;
        if (emptyBaseFragment == null || emptyBaseFragment.onBackPressed()) {
            List list = this.fragmentsStack;
            if (!((BaseFragment) list.get(list.size() - 1)).onBackPressed() || this.fragmentsStack.isEmpty()) {
                return;
            }
            closeLastFragment(true);
        }
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.fragmentsStack.isEmpty()) {
            return;
        }
        int size = this.fragmentsStack.size();
        for (int i = 0; i < size; i++) {
            BaseFragment baseFragment = (BaseFragment) this.fragmentsStack.get(i);
            baseFragment.onConfigurationChanged(configuration);
            Dialog dialog = baseFragment.visibleDialog;
            if (dialog instanceof BottomSheet) {
                ((BottomSheet) dialog).onConfigurationChanged(configuration);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attached = false;
    }

    @Override // org.telegram.ui.Components.FloatingDebug.FloatingDebugProvider
    public List onGetDebugItems() {
        BaseFragment lastFragment = getLastFragment();
        if (lastFragment != null) {
            ArrayList arrayList = new ArrayList();
            if (lastFragment instanceof FloatingDebugProvider) {
                arrayList.addAll(((FloatingDebugProvider) lastFragment).onGetDebugItems());
            }
            observeDebugItemsFromView(arrayList, lastFragment.getFragmentView());
            return arrayList;
        }
        return Collections.emptyList();
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.animationInProgress || checkTransitionAnimation() || onTouchEvent(motionEvent);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        ActionBar actionBar;
        if (i == 82 && !checkTransitionAnimation() && !this.startedTracking && (actionBar = this.currentActionBar) != null) {
            actionBar.onMenuButtonPressed();
        }
        return super.onKeyUp(i, keyEvent);
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00b7  */
    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int childCount = getChildCount();
        int paddingLeft = getPaddingLeft();
        int paddingRight = (i3 - i) - getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = (i4 - i2) - getPaddingBottom();
        for (int i9 = 0; i9 < childCount; i9++) {
            View childAt = getChildAt(i9);
            if (childAt.getVisibility() != 8) {
                BottomSheetTabs bottomSheetTabs = this.bottomSheetTabs;
                if (childAt == bottomSheetTabs) {
                    bottomSheetTabs.updateCurrentAccount();
                }
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                int i10 = layoutParams.gravity;
                if (i10 == -1) {
                    i10 = 8388659;
                }
                int absoluteGravity = Gravity.getAbsoluteGravity(i10, getLayoutDirection());
                int i11 = i10 & 112;
                int i12 = absoluteGravity & 7;
                if (i12 == 1) {
                    i5 = (((paddingRight - paddingLeft) - measuredWidth) / 2) + paddingLeft + layoutParams.leftMargin;
                } else if (i12 != 5) {
                    i6 = layoutParams.leftMargin + paddingLeft;
                    if (i11 != 16) {
                        i7 = (((paddingBottom - paddingTop) - measuredHeight) / 2) + paddingTop + layoutParams.topMargin;
                    } else if (i11 == 48 || i11 != 80) {
                        i8 = paddingTop + layoutParams.topMargin;
                        if (childAt != this.bottomSheetTabs && this.savedBottomSheetTabsTop != 0 && (this.isKeyboardVisible || ((getParent() instanceof View) && ((View) getParent()).getHeight() > getHeight()))) {
                            i8 = this.savedBottomSheetTabsTop;
                        } else if (childAt == this.bottomSheetTabs) {
                            this.savedBottomSheetTabsTop = i8;
                        }
                        childAt.layout(i6, i8, measuredWidth + i6, measuredHeight + i8);
                    } else {
                        i7 = paddingBottom - measuredHeight;
                    }
                    i8 = i7 - layoutParams.bottomMargin;
                    if (childAt != this.bottomSheetTabs) {
                    }
                    if (childAt == this.bottomSheetTabs) {
                    }
                    childAt.layout(i6, i8, measuredWidth + i6, measuredHeight + i8);
                } else {
                    i5 = paddingRight - measuredWidth;
                }
                i6 = i5 - layoutParams.rightMargin;
                if (i11 != 16) {
                }
                i8 = i7 - layoutParams.bottomMargin;
                if (childAt != this.bottomSheetTabs) {
                }
                if (childAt == this.bottomSheetTabs) {
                }
                childAt.layout(i6, i8, measuredWidth + i6, measuredHeight + i8);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void onLowMemory() {
        for (BaseFragment baseFragment : this.fragmentsStack) {
            baseFragment.onLowMemory();
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        BaseFragment baseFragment;
        if (this.fragmentsStack.isEmpty()) {
            baseFragment = null;
        } else {
            List list = this.fragmentsStack;
            baseFragment = (BaseFragment) list.get(list.size() - 1);
        }
        if (baseFragment == null || !storyViewerAttached()) {
            INavigationLayout.INavigationLayoutDelegate iNavigationLayoutDelegate = this.delegate;
            if (iNavigationLayoutDelegate != null) {
                int[] iArr = this.measureSpec;
                iArr[0] = i;
                iArr[1] = i2;
                iNavigationLayoutDelegate.onMeasureOverride(iArr);
                int[] iArr2 = this.measureSpec;
                int i3 = iArr2[0];
                i2 = iArr2[1];
                i = i3;
            }
            this.isKeyboardVisible = measureKeyboardHeight() > AndroidUtilities.dp(20.0f);
        } else {
            int measureKeyboardHeight = measureKeyboardHeight();
            baseFragment.setKeyboardHeightFromParent(measureKeyboardHeight);
            i2 = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2) + measureKeyboardHeight, 1073741824);
        }
        super.onMeasure(i, i2);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void onPause() {
        if (!this.fragmentsStack.isEmpty()) {
            List list = this.fragmentsStack;
            ((BaseFragment) list.get(list.size() - 1)).onPause();
        }
        EmptyBaseFragment emptyBaseFragment = this.sheetFragment;
        if (emptyBaseFragment != null) {
            emptyBaseFragment.onPause();
        }
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void onResume() {
        if (!this.fragmentsStack.isEmpty()) {
            List list = this.fragmentsStack;
            ((BaseFragment) list.get(list.size() - 1)).onResume();
        }
        EmptyBaseFragment emptyBaseFragment = this.sheetFragment;
        if (emptyBaseFragment != null) {
            emptyBaseFragment.onResume();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:120:0x02b5, code lost:
        if (r14 != null) goto L116;
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x02c2, code lost:
        if (r14 != null) goto L116;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x02c4, code lost:
        r14.recycle();
        r13.velocityTracker = null;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        VelocityTracker velocityTracker;
        Animator customSlideTransition;
        LayoutContainer layoutContainer;
        if (checkTransitionAnimation() || this.inActionMode || this.animationInProgress) {
            return false;
        }
        if (this.fragmentsStack.size() > 1 && allowSwipe()) {
            if (motionEvent != null && motionEvent.getAction() == 0) {
                List list = this.fragmentsStack;
                if (!((BaseFragment) list.get(list.size() - 1)).isSwipeBackEnabled(motionEvent)) {
                    this.maybeStartTracking = false;
                    this.startedTracking = false;
                    return false;
                }
                this.startedTrackingPointerId = motionEvent.getPointerId(0);
                this.maybeStartTracking = true;
                this.startedTrackingX = (int) motionEvent.getX();
                this.startedTrackingY = (int) motionEvent.getY();
                VelocityTracker velocityTracker2 = this.velocityTracker;
                if (velocityTracker2 != null) {
                    velocityTracker2.clear();
                }
            } else if (motionEvent != null && motionEvent.getAction() == 2 && motionEvent.getPointerId(0) == this.startedTrackingPointerId) {
                if (this.velocityTracker == null) {
                    this.velocityTracker = VelocityTracker.obtain();
                }
                int max = Math.max(0, (int) (motionEvent.getX() - this.startedTrackingX));
                int abs = Math.abs(((int) motionEvent.getY()) - this.startedTrackingY);
                this.velocityTracker.addMovement(motionEvent);
                if (!this.transitionAnimationInProgress && !this.inPreviewMode && this.maybeStartTracking && !this.startedTracking && max >= AndroidUtilities.getPixelsInCM(0.4f, true) && Math.abs(max) / 3 > abs) {
                    List list2 = this.fragmentsStack;
                    if (((BaseFragment) list2.get(list2.size() - 1)).canBeginSlide() && findScrollingChild(this, motionEvent.getX(), motionEvent.getY()) == null) {
                        prepareForMoving(motionEvent);
                    } else {
                        this.maybeStartTracking = false;
                    }
                } else if (this.startedTracking) {
                    if (!this.beginTrackingSent) {
                        if (this.parentActivity.getCurrentFocus() != null) {
                            AndroidUtilities.hideKeyboard(this.parentActivity.getCurrentFocus());
                        }
                        List list3 = this.fragmentsStack;
                        ((BaseFragment) list3.get(list3.size() - 1)).onBeginSlide();
                        this.beginTrackingSent = true;
                    }
                    float f = max;
                    this.containerView.setTranslationX(f);
                    setInnerTranslationX(f);
                }
            } else if (motionEvent != null && motionEvent.getPointerId(0) == this.startedTrackingPointerId && (motionEvent.getAction() == 3 || motionEvent.getAction() == 1 || motionEvent.getAction() == 6)) {
                if (this.velocityTracker == null) {
                    this.velocityTracker = VelocityTracker.obtain();
                }
                this.velocityTracker.computeCurrentVelocity(1000);
                List list4 = this.fragmentsStack;
                BaseFragment baseFragment = (BaseFragment) list4.get(list4.size() - 1);
                if (!this.inPreviewMode && !this.transitionAnimationPreviewMode && !this.startedTracking && baseFragment.isSwipeBackEnabled(motionEvent)) {
                    float xVelocity = this.velocityTracker.getXVelocity();
                    float yVelocity = this.velocityTracker.getYVelocity();
                    if (xVelocity >= 3500.0f && xVelocity > Math.abs(yVelocity) && baseFragment.canBeginSlide()) {
                        prepareForMoving(motionEvent);
                        if (!this.beginTrackingSent) {
                            if (((Activity) getContext()).getCurrentFocus() != null) {
                                AndroidUtilities.hideKeyboard(((Activity) getContext()).getCurrentFocus());
                            }
                            this.beginTrackingSent = true;
                        }
                    }
                }
                if (this.startedTracking) {
                    float x = this.containerView.getX();
                    AnimatorSet animatorSet = new AnimatorSet();
                    float xVelocity2 = this.velocityTracker.getXVelocity();
                    final boolean z = x < ((float) this.containerView.getMeasuredWidth()) / 3.0f && (xVelocity2 < 3500.0f || xVelocity2 < this.velocityTracker.getYVelocity());
                    boolean shouldOverrideSlideTransition = baseFragment.shouldOverrideSlideTransition(false, z);
                    float measuredWidth = this.containerView.getMeasuredWidth();
                    if (z) {
                        int max2 = Math.max((int) ((320.0f / measuredWidth) * x), 120);
                        if (!shouldOverrideSlideTransition) {
                            long j = max2;
                            animatorSet.playTogether(ObjectAnimator.ofFloat(this.containerView, View.TRANSLATION_X, 0.0f).setDuration(j), ObjectAnimator.ofFloat(this, "innerTranslationX", 0.0f).setDuration(j));
                            animatorSet.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                        }
                    } else {
                        x = measuredWidth - x;
                        int max3 = Math.max((int) ((200.0f / this.containerView.getMeasuredWidth()) * x), 50);
                        if (!shouldOverrideSlideTransition) {
                            long j2 = max3;
                            animatorSet.playTogether(ObjectAnimator.ofFloat(this.containerView, View.TRANSLATION_X, layoutContainer.getMeasuredWidth()).setDuration(j2), ObjectAnimator.ofFloat(this, "innerTranslationX", this.containerView.getMeasuredWidth()).setDuration(j2));
                        }
                    }
                    Animator customSlideTransition2 = baseFragment.getCustomSlideTransition(false, z, x);
                    if (customSlideTransition2 != null) {
                        animatorSet.playTogether(customSlideTransition2);
                    }
                    List list5 = this.fragmentsStack;
                    BaseFragment baseFragment2 = (BaseFragment) list5.get(list5.size() - 2);
                    if (baseFragment2 != null && (customSlideTransition = baseFragment2.getCustomSlideTransition(false, z, x)) != null) {
                        animatorSet.playTogether(customSlideTransition);
                    }
                    animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.ActionBarLayout.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            ActionBarLayout.this.onSlideAnimationEnd(z);
                        }
                    });
                    animatorSet.start();
                    this.animationInProgress = true;
                    this.layoutToIgnore = this.containerViewBack;
                } else {
                    this.maybeStartTracking = false;
                    this.startedTracking = false;
                    this.layoutToIgnore = null;
                }
                velocityTracker = this.velocityTracker;
            } else if (motionEvent == null) {
                this.maybeStartTracking = false;
                this.startedTracking = false;
                this.layoutToIgnore = null;
                velocityTracker = this.velocityTracker;
            }
        }
        return this.startedTracking;
    }

    public void onUserLeaveHint() {
        if (!this.fragmentsStack.isEmpty()) {
            List list = this.fragmentsStack;
            ((BaseFragment) list.get(list.size() - 1)).onUserLeaveHint();
        }
        EmptyBaseFragment emptyBaseFragment = this.sheetFragment;
        if (emptyBaseFragment != null) {
            emptyBaseFragment.onUserLeaveHint();
        }
    }

    public void parentDraw(View view, Canvas canvas) {
        if (this.bottomSheetTabs == null || getHeight() >= view.getHeight()) {
            return;
        }
        canvas.save();
        canvas.translate(getX() + this.bottomSheetTabs.getX(), getY() + this.bottomSheetTabs.getY());
        this.bottomSheetTabs.draw(canvas);
        canvas.restore();
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* synthetic */ boolean presentFragment(BaseFragment baseFragment) {
        return INavigationLayout.-CC.$default$presentFragment(this, baseFragment);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* synthetic */ boolean presentFragment(BaseFragment baseFragment, boolean z) {
        return INavigationLayout.-CC.$default$presentFragment(this, baseFragment, z);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* synthetic */ boolean presentFragment(BaseFragment baseFragment, boolean z, boolean z2, boolean z3, boolean z4) {
        return INavigationLayout.-CC.$default$presentFragment(this, baseFragment, z, z2, z3, z4);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* synthetic */ boolean presentFragment(BaseFragment baseFragment, boolean z, boolean z2, boolean z3, boolean z4, ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout) {
        return INavigationLayout.-CC.$default$presentFragment(this, baseFragment, z, z2, z3, z4, actionBarPopupWindowLayout);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public boolean presentFragment(INavigationLayout.NavigationParams navigationParams) {
        INavigationLayout.INavigationLayoutDelegate iNavigationLayoutDelegate;
        final BaseFragment baseFragment;
        int i;
        Runnable runnable;
        long j;
        LaunchActivity launchActivity;
        final BaseFragment baseFragment2 = navigationParams.fragment;
        final boolean z = navigationParams.removeLast;
        boolean z2 = navigationParams.noAnimation;
        boolean z3 = navigationParams.checkPresentFromDelegate;
        final boolean z4 = navigationParams.preview;
        final ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = navigationParams.menuView;
        if (baseFragment2 == null || checkTransitionAnimation() || !(((iNavigationLayoutDelegate = this.delegate) == null || !z3 || iNavigationLayoutDelegate.needPresentFragment(this, navigationParams)) && baseFragment2.onFragmentCreate())) {
            return false;
        }
        BaseFragment lastFragment = getLastFragment();
        Dialog visibleDialog = lastFragment != null ? lastFragment.getVisibleDialog() : null;
        if (visibleDialog == null && (launchActivity = LaunchActivity.instance) != null && launchActivity.getVisibleDialog() != null) {
            visibleDialog = LaunchActivity.instance.getVisibleDialog();
        }
        if (lastFragment != null && shouldOpenFragmentOverlay(visibleDialog)) {
            BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
            bottomSheetParams.transitionFromLeft = true;
            bottomSheetParams.allowNestedScroll = false;
            lastFragment.showAsSheet(baseFragment2, bottomSheetParams);
            return true;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("present fragment " + baseFragment2.getClass().getSimpleName() + " args=" + baseFragment2.getArguments());
        }
        StoryViewer.closeGlobalInstances();
        LaunchActivity.dismissAllWeb();
        if (this.inPreviewMode && this.transitionAnimationPreviewMode) {
            Runnable runnable2 = this.delayedOpenAnimationRunnable;
            if (runnable2 != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable2);
                this.delayedOpenAnimationRunnable = null;
            }
            closeLastFragment(false, true);
        }
        baseFragment2.setInPreviewMode(z4);
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout2 = this.previewMenu;
        if (actionBarPopupWindowLayout2 != null) {
            if (actionBarPopupWindowLayout2.getParent() != null) {
                ((ViewGroup) this.previewMenu.getParent()).removeView(this.previewMenu);
            }
            this.previewMenu = null;
        }
        this.previewMenu = actionBarPopupWindowLayout;
        baseFragment2.setInMenuMode(actionBarPopupWindowLayout != null);
        if (this.parentActivity.getCurrentFocus() != null && baseFragment2.hideKeyboardOnShow() && !z4) {
            AndroidUtilities.hideKeyboard(this.parentActivity.getCurrentFocus());
        }
        boolean z5 = z4 || (!z2 && MessagesController.getGlobalMainSettings().getBoolean("view_animations", true));
        if (this.fragmentsStack.isEmpty()) {
            baseFragment = null;
        } else {
            List list = this.fragmentsStack;
            baseFragment = (BaseFragment) list.get(list.size() - 1);
        }
        baseFragment2.setParentLayout(this);
        View view = baseFragment2.fragmentView;
        if (view == null) {
            view = baseFragment2.createView(this.parentActivity);
        } else {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null) {
                baseFragment2.onRemoveFromParent();
                viewGroup.removeView(view);
            }
        }
        this.containerViewBack.addView(view);
        if (actionBarPopupWindowLayout != null) {
            this.containerViewBack.addView(actionBarPopupWindowLayout);
            actionBarPopupWindowLayout.measure(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), Integer.MIN_VALUE));
            i = actionBarPopupWindowLayout.getMeasuredHeight() + AndroidUtilities.dp(24.0f);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) actionBarPopupWindowLayout.getLayoutParams();
            layoutParams.width = -2;
            layoutParams.height = -2;
            layoutParams.topMargin = (getMeasuredHeight() - i) - AndroidUtilities.dp(6.0f);
            actionBarPopupWindowLayout.setLayoutParams(layoutParams);
        } else {
            i = 0;
        }
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams2.width = -1;
        layoutParams2.height = -1;
        if (z4) {
            int previewHeight = baseFragment2.getPreviewHeight();
            int i2 = Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0;
            if (previewHeight <= 0 || previewHeight >= getMeasuredHeight() - i2) {
                int dp = AndroidUtilities.dp(actionBarPopupWindowLayout != null ? 0.0f : 24.0f);
                layoutParams2.bottomMargin = dp;
                layoutParams2.topMargin = dp;
                layoutParams2.topMargin = dp + AndroidUtilities.statusBarHeight;
            } else {
                layoutParams2.height = previewHeight;
                layoutParams2.topMargin = i2 + (((getMeasuredHeight() - i2) - previewHeight) / 2);
            }
            if (actionBarPopupWindowLayout != null) {
                layoutParams2.bottomMargin += i + AndroidUtilities.dp(8.0f);
            }
            int dp2 = AndroidUtilities.dp(8.0f);
            layoutParams2.leftMargin = dp2;
            layoutParams2.rightMargin = dp2;
        } else {
            layoutParams2.leftMargin = 0;
            layoutParams2.rightMargin = 0;
            layoutParams2.bottomMargin = 0;
            layoutParams2.topMargin = 0;
        }
        view.setLayoutParams(layoutParams2);
        ActionBar actionBar = baseFragment2.actionBar;
        if (actionBar != null && actionBar.shouldAddToContainer()) {
            if (this.removeActionBarExtraHeight) {
                baseFragment2.actionBar.setOccupyStatusBar(false);
            }
            AndroidUtilities.removeFromParent(baseFragment2.actionBar);
            this.containerViewBack.addView(baseFragment2.actionBar);
            baseFragment2.actionBar.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, this.overlayAction);
        }
        baseFragment2.attachSheets(this.containerViewBack);
        this.fragmentsStack.add(baseFragment2);
        onFragmentStackChanged("presentFragment");
        baseFragment2.onResume();
        this.currentActionBar = baseFragment2.actionBar;
        if (!baseFragment2.hasOwnBackground && view.getBackground() == null) {
            view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        }
        LayoutContainer layoutContainer = this.containerView;
        LayoutContainer layoutContainer2 = this.containerViewBack;
        this.containerView = layoutContainer2;
        this.containerViewBack = layoutContainer;
        layoutContainer2.setVisibility(0);
        setInnerTranslationX(0.0f);
        this.containerView.setTranslationY(0.0f);
        if (z4) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.ActionBar.ActionBarLayout.3
                    @Override // android.view.ViewOutlineProvider
                    public void getOutline(View view2, Outline outline) {
                        outline.setRoundRect(0, AndroidUtilities.statusBarHeight, view2.getMeasuredWidth(), view2.getMeasuredHeight(), AndroidUtilities.dp(6.0f));
                    }
                });
                view.setClipToOutline(true);
                view.setElevation(AndroidUtilities.dp(4.0f));
            }
            if (this.previewBackgroundDrawable == null) {
                this.previewBackgroundDrawable = new ColorDrawable(771751936);
            }
            this.previewBackgroundDrawable.setAlpha(0);
            Theme.moveUpDrawable.setAlpha(0);
        }
        bringChildToFront(this.containerView);
        LayoutContainer layoutContainer3 = this.sheetContainer;
        if (layoutContainer3 != null) {
            bringChildToFront(layoutContainer3);
        }
        if (!z5) {
            presentFragmentInternalRemoveOld(z, baseFragment);
            View view2 = this.backgroundView;
            if (view2 != null) {
                view2.setVisibility(0);
            }
        }
        if (this.themeAnimatorSet != null) {
            this.presentingFragmentDescriptions = baseFragment2.getThemeDescriptions();
        }
        if (!z5 && !z4) {
            View view3 = this.backgroundView;
            if (view3 != null) {
                view3.setAlpha(1.0f);
                this.backgroundView.setVisibility(0);
            }
            if (baseFragment != null) {
                baseFragment.onTransitionAnimationStart(false, false);
                baseFragment.onTransitionAnimationEnd(false, false);
            }
            baseFragment2.onTransitionAnimationStart(true, false);
            baseFragment2.onTransitionAnimationEnd(true, false);
            baseFragment2.onBecomeFullyVisible();
            return true;
        }
        if (this.useAlphaAnimations && this.fragmentsStack.size() == 1) {
            presentFragmentInternalRemoveOld(z, baseFragment);
            this.transitionAnimationStartTime = System.currentTimeMillis();
            this.transitionAnimationInProgress = true;
            this.layoutToIgnore = this.containerView;
            this.onOpenAnimationEndRunnable = new Runnable() { // from class: org.telegram.ui.ActionBar.ActionBarLayout$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    ActionBarLayout.lambda$presentFragment$0(BaseFragment.this, baseFragment2);
                }
            };
            ArrayList arrayList = new ArrayList();
            Property property = View.ALPHA;
            arrayList.add(ObjectAnimator.ofFloat(this, property, 0.0f, 1.0f));
            View view4 = this.backgroundView;
            if (view4 != null) {
                view4.setVisibility(0);
                arrayList.add(ObjectAnimator.ofFloat(this.backgroundView, property, 0.0f, 1.0f));
            }
            if (baseFragment != null) {
                baseFragment.onTransitionAnimationStart(false, false);
            }
            baseFragment2.onTransitionAnimationStart(true, false);
            AnimatorSet animatorSet = new AnimatorSet();
            this.currentAnimation = animatorSet;
            animatorSet.playTogether(arrayList);
            this.currentAnimation.setInterpolator(this.accelerateDecelerateInterpolator);
            this.currentAnimation.setDuration(200L);
            this.currentAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.ActionBarLayout.4
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ActionBarLayout.this.onAnimationEndCheck(false);
                }
            });
            this.currentAnimation.start();
        } else {
            this.transitionAnimationPreviewMode = z4;
            this.transitionAnimationStartTime = System.currentTimeMillis();
            this.transitionAnimationInProgress = true;
            this.layoutToIgnore = this.containerView;
            final BaseFragment baseFragment3 = baseFragment;
            this.onOpenAnimationEndRunnable = new Runnable() { // from class: org.telegram.ui.ActionBar.ActionBarLayout$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    ActionBarLayout.this.lambda$presentFragment$1(z4, actionBarPopupWindowLayout, z, baseFragment3, baseFragment2);
                }
            };
            final boolean z6 = !baseFragment2.needDelayOpenAnimation();
            if (z6) {
                if (baseFragment != null) {
                    baseFragment.onTransitionAnimationStart(false, false);
                }
                baseFragment2.onTransitionAnimationStart(true, false);
            }
            this.delayedAnimationResumed = false;
            this.oldFragment = baseFragment;
            this.newFragment = baseFragment2;
            AnimatorSet onCustomTransitionAnimation = !z4 ? baseFragment2.onCustomTransitionAnimation(true, new Runnable() { // from class: org.telegram.ui.ActionBar.ActionBarLayout$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    ActionBarLayout.this.lambda$presentFragment$2();
                }
            }) : null;
            if (onCustomTransitionAnimation == null) {
                this.containerView.setAlpha(0.0f);
                LayoutContainer layoutContainer4 = this.containerView;
                if (z4) {
                    layoutContainer4.setTranslationX(0.0f);
                    this.containerView.setScaleX(0.9f);
                    this.containerView.setScaleY(0.9f);
                } else {
                    layoutContainer4.setTranslationX(48.0f);
                    this.containerView.setScaleX(1.0f);
                    this.containerView.setScaleY(1.0f);
                }
                if (this.containerView.isKeyboardVisible || this.containerViewBack.isKeyboardVisible) {
                    if (baseFragment != null && !z4) {
                        baseFragment.saveKeyboardPositionBeforeTransition();
                    }
                    final BaseFragment baseFragment4 = baseFragment;
                    this.waitingForKeyboardCloseRunnable = new Runnable() { // from class: org.telegram.ui.ActionBar.ActionBarLayout.5
                        @Override // java.lang.Runnable
                        public void run() {
                            if (ActionBarLayout.this.waitingForKeyboardCloseRunnable != this) {
                                return;
                            }
                            ActionBarLayout.this.waitingForKeyboardCloseRunnable = null;
                            if (z6) {
                                BaseFragment baseFragment5 = baseFragment4;
                                if (baseFragment5 != null) {
                                    baseFragment5.onTransitionAnimationStart(false, false);
                                }
                                baseFragment2.onTransitionAnimationStart(true, false);
                                ActionBarLayout.this.startLayoutAnimation(true, true, z4);
                            } else if (ActionBarLayout.this.delayedOpenAnimationRunnable != null) {
                                AndroidUtilities.cancelRunOnUIThread(ActionBarLayout.this.delayedOpenAnimationRunnable);
                                if (ActionBarLayout.this.delayedAnimationResumed) {
                                    ActionBarLayout.this.delayedOpenAnimationRunnable.run();
                                } else {
                                    AndroidUtilities.runOnUIThread(ActionBarLayout.this.delayedOpenAnimationRunnable, 200L);
                                }
                            }
                        }
                    };
                    if (baseFragment2.needDelayOpenAnimation()) {
                        this.delayedOpenAnimationRunnable = new Runnable() { // from class: org.telegram.ui.ActionBar.ActionBarLayout.6
                            @Override // java.lang.Runnable
                            public void run() {
                                if (ActionBarLayout.this.delayedOpenAnimationRunnable != this) {
                                    return;
                                }
                                ActionBarLayout.this.delayedOpenAnimationRunnable = null;
                                BaseFragment baseFragment5 = baseFragment;
                                if (baseFragment5 != null) {
                                    baseFragment5.onTransitionAnimationStart(false, false);
                                }
                                baseFragment2.onTransitionAnimationStart(true, false);
                                ActionBarLayout.this.startLayoutAnimation(true, true, z4);
                            }
                        };
                    }
                    runnable = this.waitingForKeyboardCloseRunnable;
                    j = 250;
                } else if (!baseFragment2.needDelayOpenAnimation()) {
                    startLayoutAnimation(true, true, z4);
                    return true;
                } else {
                    runnable = new Runnable() { // from class: org.telegram.ui.ActionBar.ActionBarLayout.7
                        @Override // java.lang.Runnable
                        public void run() {
                            if (ActionBarLayout.this.delayedOpenAnimationRunnable != this) {
                                return;
                            }
                            ActionBarLayout.this.delayedOpenAnimationRunnable = null;
                            baseFragment2.onTransitionAnimationStart(true, false);
                            ActionBarLayout.this.startLayoutAnimation(true, true, z4);
                        }
                    };
                    this.delayedOpenAnimationRunnable = runnable;
                    j = 200;
                }
                AndroidUtilities.runOnUIThread(runnable, j);
            } else {
                if (!z4 && ((this.containerView.isKeyboardVisible || this.containerViewBack.isKeyboardVisible) && baseFragment != null)) {
                    baseFragment.saveKeyboardPositionBeforeTransition();
                }
                this.currentAnimation = onCustomTransitionAnimation;
            }
        }
        return true;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* synthetic */ boolean presentFragmentAsPreview(BaseFragment baseFragment) {
        return INavigationLayout.-CC.$default$presentFragmentAsPreview(this, baseFragment);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* synthetic */ boolean presentFragmentAsPreviewWithMenu(BaseFragment baseFragment, ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout) {
        return INavigationLayout.-CC.$default$presentFragmentAsPreviewWithMenu(this, baseFragment, actionBarPopupWindowLayout);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void rebuildAllFragmentViews(boolean z, boolean z2) {
        if (this.transitionAnimationInProgress || this.startedTracking) {
            this.rebuildAfterAnimation = true;
            this.rebuildLastAfterAnimation = z;
            this.showLastAfterAnimation = z2;
            return;
        }
        int size = this.fragmentsStack.size();
        if (!z) {
            size--;
        }
        if (this.inPreviewMode) {
            size--;
        }
        for (int i = 0; i < size; i++) {
            ((BaseFragment) this.fragmentsStack.get(i)).clearViews();
            ((BaseFragment) this.fragmentsStack.get(i)).setParentLayout(this);
        }
        INavigationLayout.INavigationLayoutDelegate iNavigationLayoutDelegate = this.delegate;
        if (iNavigationLayoutDelegate != null) {
            iNavigationLayoutDelegate.onRebuildAllFragments(this, z);
        }
        if (z2) {
            showLastFragment();
        }
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* synthetic */ void rebuildFragments(int i) {
        INavigationLayout.-CC.$default$rebuildFragments(this, i);
    }

    public void rebuildLogout() {
        this.containerView.removeAllViews();
        this.containerViewBack.removeAllViews();
        this.currentActionBar = null;
        this.newFragment = null;
        this.oldFragment = null;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void removeAllFragments() {
        while (this.fragmentsStack.size() > 0) {
            removeFragmentFromStackInternal((BaseFragment) this.fragmentsStack.get(0), false);
        }
        View view = this.backgroundView;
        if (view != null) {
            view.animate().alpha(0.0f).setDuration(180L).withEndAction(new Runnable() { // from class: org.telegram.ui.ActionBar.ActionBarLayout$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    ActionBarLayout.this.lambda$removeAllFragments$6();
                }
            }).start();
        }
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* synthetic */ void removeFragmentFromStack(int i) {
        INavigationLayout.-CC.$default$removeFragmentFromStack(this, i);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public /* synthetic */ void removeFragmentFromStack(BaseFragment baseFragment) {
        INavigationLayout.-CC.$default$removeFragmentFromStack(this, baseFragment);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x002c, code lost:
        onOpenAnimationEnd();
        onCloseAnimationEnd();
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x0014, code lost:
        if (r0.get(r0.size() - 1) != r4) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x002a, code lost:
        if (r0.get(r0.size() - 2) == r4) goto L5;
     */
    @Override // org.telegram.ui.ActionBar.INavigationLayout
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void removeFragmentFromStack(BaseFragment baseFragment, boolean z) {
        boolean z2 = true;
        if (this.fragmentsStack.size() > 0) {
            List list = this.fragmentsStack;
        }
        if (this.fragmentsStack.size() > 1) {
            List list2 = this.fragmentsStack;
        }
        checkBlackScreen("removeFragmentFromStack " + z);
        if (this.useAlphaAnimations && this.fragmentsStack.size() == 1 && AndroidUtilities.isTablet()) {
            closeLastFragment(true);
            return;
        }
        if (this.delegate != null && this.fragmentsStack.size() == 1 && AndroidUtilities.isTablet()) {
            this.delegate.needCloseLastFragment(this);
        }
        removeFragmentFromStackInternal(baseFragment, (!baseFragment.allowFinishFragmentInsteadOfRemoveFromStack() || z) ? false : false);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z) {
        onTouchEvent(null);
        super.requestDisallowInterceptTouchEvent(z);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void resumeDelayedFragmentAnimation() {
        this.delayedAnimationResumed = true;
        Runnable runnable = this.delayedOpenAnimationRunnable;
        if (runnable == null || this.waitingForKeyboardCloseRunnable != null) {
            return;
        }
        AndroidUtilities.cancelRunOnUIThread(runnable);
        this.delayedOpenAnimationRunnable.run();
        this.delayedOpenAnimationRunnable = null;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void setBackgroundView(View view) {
        this.backgroundView = view;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void setDelegate(INavigationLayout.INavigationLayoutDelegate iNavigationLayoutDelegate) {
        this.delegate = iNavigationLayoutDelegate;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void setDrawerLayoutContainer(DrawerLayoutContainer drawerLayoutContainer) {
        this.drawerLayoutContainer = drawerLayoutContainer;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void setFragmentPanTranslationOffset(int i) {
        LayoutContainer layoutContainer = this.containerView;
        if (layoutContainer != null) {
            layoutContainer.setFragmentPanTranslationOffset(i);
        }
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void setFragmentStack(List<BaseFragment> list) {
        this.fragmentsStack = list;
        if (this.main) {
            BottomSheetTabs bottomSheetTabs = this.bottomSheetTabs;
            if (bottomSheetTabs != null) {
                AndroidUtilities.removeFromParent(bottomSheetTabs);
                this.bottomSheetTabs = null;
            }
            this.bottomSheetTabs = new BottomSheetTabs(this.parentActivity, this);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, AndroidUtilities.dp(76.0f));
            layoutParams.gravity = 87;
            addView(this.bottomSheetTabs, layoutParams);
            if (LaunchActivity.instance.getBottomSheetTabsOverlay() != null) {
                LaunchActivity.instance.getBottomSheetTabsOverlay().setTabsView(this.bottomSheetTabs);
            }
        }
        LayoutContainer layoutContainer = this.containerViewBack;
        if (layoutContainer != null) {
            AndroidUtilities.removeFromParent(layoutContainer);
        }
        LayoutContainer layoutContainer2 = new LayoutContainer(this.parentActivity);
        this.containerViewBack = layoutContainer2;
        addView(layoutContainer2);
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.containerViewBack.getLayoutParams();
        layoutParams2.width = -1;
        layoutParams2.height = -1;
        layoutParams2.gravity = 51;
        this.containerViewBack.setLayoutParams(layoutParams2);
        LayoutContainer layoutContainer3 = this.containerView;
        if (layoutContainer3 != null) {
            AndroidUtilities.removeFromParent(layoutContainer3);
        }
        LayoutContainer layoutContainer4 = new LayoutContainer(this.parentActivity);
        this.containerView = layoutContainer4;
        addView(layoutContainer4);
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) this.containerView.getLayoutParams();
        layoutParams3.width = -1;
        layoutParams3.height = -1;
        layoutParams3.gravity = 51;
        this.containerView.setLayoutParams(layoutParams3);
        LayoutContainer layoutContainer5 = this.sheetContainer;
        if (layoutContainer5 != null) {
            AndroidUtilities.removeFromParent(layoutContainer5);
        }
        LayoutContainer layoutContainer6 = new LayoutContainer(this.parentActivity);
        this.sheetContainer = layoutContainer6;
        addView(layoutContainer6);
        FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) this.sheetContainer.getLayoutParams();
        layoutParams4.width = -1;
        layoutParams4.height = -1;
        layoutParams4.gravity = 51;
        this.sheetContainer.setLayoutParams(layoutParams4);
        for (BaseFragment baseFragment : this.fragmentsStack) {
            baseFragment.setParentLayout(this);
        }
    }

    public void setFragmentStackChangedListener(Runnable runnable) {
        this.onFragmentStackChangedListener = runnable;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void setHighlightActionButtons(boolean z) {
        this.highlightActionButtons = z;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void setInBubbleMode(boolean z) {
        this.inBubbleMode = z;
    }

    public void setInnerTranslationX(float f) {
        int navigationBarColor;
        int navigationBarColor2;
        this.innerTranslationX = f;
        invalidate();
        if (this.fragmentsStack.size() < 2 || this.containerView.getMeasuredWidth() <= 0) {
            return;
        }
        float measuredWidth = f / this.containerView.getMeasuredWidth();
        List list = this.fragmentsStack;
        BaseFragment baseFragment = (BaseFragment) list.get(list.size() - 2);
        baseFragment.onSlideProgress(false, measuredWidth);
        List list2 = this.fragmentsStack;
        BaseFragment baseFragment2 = (BaseFragment) list2.get(list2.size() - 1);
        float clamp = MathUtils.clamp(measuredWidth * 2.0f, 0.0f, 1.0f);
        if (!baseFragment2.isBeginToShow() || (navigationBarColor = baseFragment2.getNavigationBarColor()) == (navigationBarColor2 = baseFragment.getNavigationBarColor())) {
            return;
        }
        baseFragment2.setNavigationBarColor(ColorUtils.blendARGB(navigationBarColor, navigationBarColor2, clamp));
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void setIsSheet(boolean z) {
        this.isSheet = z;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void setNavigationBarColor(int i) {
        BottomSheetTabs bottomSheetTabs = this.bottomSheetTabs;
        if (bottomSheetTabs != null) {
            bottomSheetTabs.setNavigationBarColor(i, (this.startedTracking || this.animationInProgress) ? false : true);
        }
    }

    public void setOverrideWidthOffset(int i) {
        this.overrideWidthOffset = i;
        invalidate();
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void setPulledDialogs(List<BackButtonMenu.PulledDialog> list) {
        this.pulledDialogs = list;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void setRemoveActionBarExtraHeight(boolean z) {
        this.removeActionBarExtraHeight = z;
    }

    public void setThemeAnimationValue(float f) {
        this.themeAnimationValue = f;
        int size = this.themeAnimatorDescriptions.size();
        for (int i = 0; i < size; i++) {
            ArrayList arrayList = (ArrayList) this.themeAnimatorDescriptions.get(i);
            int[] iArr = (int[]) this.animateStartColors.get(i);
            int[] iArr2 = (int[]) this.animateEndColors.get(i);
            int size2 = arrayList.size();
            int i2 = 0;
            while (i2 < size2) {
                int red = Color.red(iArr2[i2]);
                int green = Color.green(iArr2[i2]);
                int blue = Color.blue(iArr2[i2]);
                int alpha = Color.alpha(iArr2[i2]);
                int red2 = Color.red(iArr[i2]);
                int green2 = Color.green(iArr[i2]);
                int blue2 = Color.blue(iArr[i2]);
                int i3 = size;
                int alpha2 = Color.alpha(iArr[i2]);
                int argb = Color.argb(Math.min((int) NotificationCenter.didClearDatabase, (int) (alpha2 + ((alpha - alpha2) * f))), Math.min((int) NotificationCenter.didClearDatabase, (int) (red2 + ((red - red2) * f))), Math.min((int) NotificationCenter.didClearDatabase, (int) (green2 + ((green - green2) * f))), Math.min((int) NotificationCenter.didClearDatabase, (int) (blue2 + ((blue - blue2) * f))));
                ThemeDescription themeDescription = (ThemeDescription) arrayList.get(i2);
                themeDescription.setAnimatedColor(argb);
                themeDescription.setColor(argb, false, false);
                i2++;
                iArr = iArr;
                size = i3;
            }
        }
        int size3 = this.themeAnimatorDelegate.size();
        for (int i4 = 0; i4 < size3; i4++) {
            ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = (ThemeDescription.ThemeDescriptionDelegate) this.themeAnimatorDelegate.get(i4);
            if (themeDescriptionDelegate != null) {
                themeDescriptionDelegate.didSetColor();
                themeDescriptionDelegate.onAnimationProgress(f);
            }
        }
        ArrayList arrayList2 = this.presentingFragmentDescriptions;
        if (arrayList2 != null) {
            int size4 = arrayList2.size();
            for (int i5 = 0; i5 < size4; i5++) {
                ThemeDescription themeDescription2 = (ThemeDescription) this.presentingFragmentDescriptions.get(i5);
                themeDescription2.setColor(Theme.getColor(themeDescription2.getCurrentKey(), themeDescription2.resourcesProvider), false, false);
            }
        }
        INavigationLayout.ThemeAnimationSettings.onAnimationProgress onanimationprogress = this.animationProgressListener;
        if (onanimationprogress != null) {
            onanimationprogress.setProgress(f);
        }
        INavigationLayout.INavigationLayoutDelegate iNavigationLayoutDelegate = this.delegate;
        if (iNavigationLayoutDelegate != null) {
            iNavigationLayoutDelegate.onThemeProgress(f);
        }
    }

    public void setTitleOverlayText(String str, int i, Runnable runnable) {
        this.titleOverlayText = str;
        this.titleOverlayTextId = i;
        this.overlayAction = runnable;
        for (int i2 = 0; i2 < this.fragmentsStack.size(); i2++) {
            ActionBar actionBar = ((BaseFragment) this.fragmentsStack.get(i2)).actionBar;
            if (actionBar != null) {
                actionBar.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, runnable);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void setUseAlphaAnimations(boolean z) {
        this.useAlphaAnimations = z;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void setWindow(Window window) {
        this.window = window;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void showLastFragment() {
        if (this.fragmentsStack.isEmpty()) {
            return;
        }
        bringToFront(this.fragmentsStack.size() - 1);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout
    public void startActivityForResult(Intent intent, int i) {
        if (this.parentActivity == null) {
            return;
        }
        if (this.transitionAnimationInProgress) {
            AnimatorSet animatorSet = this.currentAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.currentAnimation = null;
            }
            if (this.onCloseAnimationEndRunnable != null) {
                onCloseAnimationEnd();
            } else if (this.onOpenAnimationEndRunnable != null) {
                onOpenAnimationEnd();
            }
            this.containerView.invalidate();
        }
        if (intent != null) {
            this.parentActivity.startActivityForResult(intent, i);
        }
    }

    public boolean storyViewerAttached() {
        BaseFragment baseFragment;
        if (this.fragmentsStack.isEmpty()) {
            baseFragment = null;
        } else {
            List list = this.fragmentsStack;
            baseFragment = (BaseFragment) list.get(list.size() - 1);
        }
        return (baseFragment == null || baseFragment.getLastStoryViewer() == null || !baseFragment.getLastStoryViewer().attachedToParent()) ? false : true;
    }

    public void updateBottomTabsVisibility(boolean z) {
        if (this.bottomSheetTabs == null) {
            return;
        }
        ValueAnimator valueAnimator = this.bottomTabsAnimator;
        if (valueAnimator != null) {
            this.bottomTabsAnimator = null;
            valueAnimator.cancel();
        }
        if (this.bottomTabsHeight == this.bottomSheetTabs.getExpandedHeight()) {
            return;
        }
        this.bottomTabsHeight = this.bottomSheetTabs.getExpandedHeight();
        requestLayout();
        this.containerView.requestLayout();
        this.containerViewBack.requestLayout();
        this.sheetContainer.requestLayout();
        if (!z) {
            this.bottomTabsProgress = this.bottomTabsHeight;
            return;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.bottomTabsProgress, this.bottomTabsHeight);
        this.bottomTabsAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.ActionBarLayout$$ExternalSyntheticLambda6
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ActionBarLayout.this.lambda$updateBottomTabsVisibility$10(valueAnimator2);
            }
        });
        this.bottomTabsAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.ActionBarLayout.12
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (ActionBarLayout.this.bottomTabsAnimator == animator) {
                    ActionBarLayout actionBarLayout = ActionBarLayout.this;
                    actionBarLayout.bottomTabsProgress = actionBarLayout.bottomTabsHeight;
                    ActionBarLayout.this.invalidate();
                }
            }
        });
        this.bottomTabsAnimator.setDuration(250L);
        this.bottomTabsAnimator.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
        this.bottomTabsAnimator.start();
    }

    public void updateTitleOverlay() {
        ActionBar actionBar;
        BaseFragment lastFragment = getLastFragment();
        if (lastFragment == null || (actionBar = lastFragment.actionBar) == null) {
            return;
        }
        actionBar.setTitleOverlayText(this.titleOverlayText, this.titleOverlayTextId, this.overlayAction);
    }
}
