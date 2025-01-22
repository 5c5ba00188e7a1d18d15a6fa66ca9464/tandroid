package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.camera.CameraView;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.LaunchActivity;

/* loaded from: classes4.dex */
public class BottomSheet extends Dialog implements BaseFragment.AttachedSheet {
    private static final boolean AVOID_SYSTEM_CUTOUT_FULLSCREEN = false;
    private boolean allowCustomAnimation;
    private boolean allowDrawContent;
    protected boolean allowNestedScroll;
    private boolean applyBottomPadding;
    private boolean applyTopPadding;
    public BaseFragment attachedFragment;
    protected ColorDrawable backDrawable;
    protected int backgroundPaddingLeft;
    protected int backgroundPaddingTop;
    protected int behindKeyboardColor;
    protected int behindKeyboardColorKey;
    private boolean bigTitle;
    private int bottomInset;
    protected boolean calcMandatoryInsets;
    private boolean canDismissWithSwipe;
    private boolean canDismissWithTouchOutside;
    private int cellType;
    public ContainerView container;
    protected ViewGroup containerView;
    protected int currentAccount;
    private float currentPanTranslationY;
    protected AnimatorSet currentSheetAnimation;
    protected int currentSheetAnimationType;
    private View customView;
    protected int customViewGravity;
    protected BottomSheetDelegateInterface delegate;
    protected boolean dimBehind;
    protected int dimBehindAlpha;
    private boolean disableScroll;
    private Runnable dismissRunnable;
    private boolean dismissed;
    public boolean drawDoubleNavigationBar;
    public boolean drawNavigationBar;
    private boolean focusable;
    private boolean forceKeyboardOnDismiss;
    private boolean fullHeight;
    protected boolean fullWidth;
    private float hideSystemVerticalInsetsProgress;
    protected boolean isFullscreen;
    protected boolean isPortrait;
    private int[] itemIcons;
    private ArrayList<BottomSheetCell> itemViews;
    private CharSequence[] items;
    private ValueAnimator keyboardContentAnimator;
    protected int keyboardHeight;
    protected boolean keyboardVisible;
    private WindowInsets lastInsets;
    private int lastKeyboardHeight;
    private int layoutCount;
    private int leftInset;
    private boolean multipleLinesTitle;
    protected int navBarColor;
    protected int navBarColorKey;
    protected float navigationBarAlpha;
    protected ValueAnimator navigationBarAnimation;
    protected int navigationBarHeight;
    protected View nestedScrollChild;
    private AnimationNotificationsLocker notificationsLocker;
    public boolean occupyNavigationBar;
    public boolean occupyNavigationBarWithoutKeyboard;
    private DialogInterface.OnClickListener onClickListener;
    private DialogInterface.OnDismissListener onHideListener;
    protected Interpolator openInterpolator;
    private boolean openNoDelay;
    protected int openedLayerNum;
    private int overlayDrawNavBarColor;
    public boolean pauseAllHeavyOperations;
    protected int playingImagesLayerNum;
    protected Theme.ResourcesProvider resourcesProvider;
    private int rightInset;
    public boolean scrollNavBar;
    private Integer selectedPos;
    protected Drawable shadowDrawable;
    private boolean showWithoutAnimation;
    boolean showing;
    private boolean skipDismissAnimation;
    private long smoothContainerViewLayoutUntil;
    public boolean smoothKeyboardAnimationEnabled;
    public boolean smoothKeyboardByBottom;
    protected Runnable startAnimationRunnable;
    private int statusBarHeight;
    private int tag;
    private CharSequence title;
    private TextView titleView;
    public FrameLayout topBulletinContainer;
    private int touchSlop;
    private boolean transitionFromRight;
    public boolean useBackgroundTopPadding;
    private boolean useFastDismiss;
    protected boolean useHardwareLayer;
    protected boolean useLightNavBar;
    protected boolean useLightStatusBar;
    protected boolean useSmoothKeyboard;
    protected boolean waitingKeyboard;

    class 7 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$item;

        7(int i) {
            this.val$item = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$0() {
            if (BottomSheet.this.onHideListener != null) {
                BottomSheet.this.onHideListener.onDismiss(BottomSheet.this);
            }
            try {
                BottomSheet.this.dismissInternal();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            AnimatorSet animatorSet = BottomSheet.this.currentSheetAnimation;
            if (animatorSet == null || !animatorSet.equals(animator)) {
                return;
            }
            BottomSheet bottomSheet = BottomSheet.this;
            bottomSheet.currentSheetAnimation = null;
            bottomSheet.currentSheetAnimationType = 0;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            AnimatorSet animatorSet = BottomSheet.this.currentSheetAnimation;
            if (animatorSet != null && animatorSet.equals(animator)) {
                BottomSheet bottomSheet = BottomSheet.this;
                bottomSheet.currentSheetAnimation = null;
                bottomSheet.currentSheetAnimationType = 0;
                if (bottomSheet.onClickListener != null) {
                    BottomSheet.this.onClickListener.onClick(BottomSheet.this, this.val$item);
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.BottomSheet$7$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        BottomSheet.7.this.lambda$onAnimationEnd$0();
                    }
                });
            }
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 512);
        }
    }

    class 8 extends AnimatorListenerAdapter {
        8() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$0() {
            try {
                BottomSheet.this.dismissInternal();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            AnimatorSet animatorSet = BottomSheet.this.currentSheetAnimation;
            if (animatorSet == null || !animatorSet.equals(animator)) {
                return;
            }
            BottomSheet bottomSheet = BottomSheet.this;
            bottomSheet.currentSheetAnimation = null;
            bottomSheet.currentSheetAnimationType = 0;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            AnimatorSet animatorSet = BottomSheet.this.currentSheetAnimation;
            if (animatorSet != null && animatorSet.equals(animator)) {
                BottomSheet bottomSheet = BottomSheet.this;
                bottomSheet.currentSheetAnimation = null;
                bottomSheet.currentSheetAnimationType = 0;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.BottomSheet$8$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        BottomSheet.8.this.lambda$onAnimationEnd$0();
                    }
                });
            }
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 512);
        }
    }

    public static class BottomSheetCell extends FrameLayout {
        private boolean checked;
        int currentType;
        private ImageView imageView;
        private ImageView imageView2;
        public boolean isSelected;
        private final Theme.ResourcesProvider resourcesProvider;
        private AnimatedEmojiSpan.TextViewEmojis textView;

        public BottomSheetCell(Context context, int i) {
            this(context, i, null);
        }

        public BottomSheetCell(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            AnimatedEmojiSpan.TextViewEmojis textViewEmojis;
            FrameLayout.LayoutParams createFrame;
            this.isSelected = false;
            this.resourcesProvider = resourcesProvider;
            this.currentType = i;
            if (i != Builder.CELL_TYPE_CALL) {
                setBackgroundDrawable(Theme.getSelectorDrawable(false, resourcesProvider));
            }
            ImageView imageView = new ImageView(context);
            this.imageView = imageView;
            ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER;
            imageView.setScaleType(scaleType);
            this.imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_dialogIcon), PorterDuff.Mode.MULTIPLY));
            addView(this.imageView, LayoutHelper.createFrame(56, 48, (LocaleController.isRTL ? 5 : 3) | 16));
            ImageView imageView2 = new ImageView(context);
            this.imageView2 = imageView2;
            imageView2.setScaleType(scaleType);
            this.imageView2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_radioBackgroundChecked, resourcesProvider), PorterDuff.Mode.SRC_IN));
            addView(this.imageView2, LayoutHelper.createFrame(56, 48, (LocaleController.isRTL ? 3 : 5) | 16));
            AnimatedEmojiSpan.TextViewEmojis textViewEmojis2 = new AnimatedEmojiSpan.TextViewEmojis(context);
            this.textView = textViewEmojis2;
            textViewEmojis2.setLines(1);
            this.textView.setSingleLine(true);
            this.textView.setGravity(1);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            if (i == 0 || i == Builder.CELL_TYPE_CALL) {
                this.textView.setTextColor(getThemedColor(Theme.key_dialogTextBlack));
                this.textView.setTextSize(1, 16.0f);
                textViewEmojis = this.textView;
                createFrame = LayoutHelper.createFrame(-2, -2, (LocaleController.isRTL ? 5 : 3) | 16);
            } else if (i == 1) {
                this.textView.setGravity(17);
                this.textView.setTextColor(getThemedColor(Theme.key_dialogTextBlack));
                this.textView.setTextSize(1, 14.0f);
                this.textView.setTypeface(AndroidUtilities.bold());
                textViewEmojis = this.textView;
                createFrame = LayoutHelper.createFrame(-1, -1.0f);
            } else {
                if (i != 2) {
                    return;
                }
                this.textView.setGravity(17);
                this.textView.setTextColor(getThemedColor(Theme.key_featuredStickers_buttonText));
                this.textView.setTextSize(1, 14.0f);
                this.textView.setTypeface(AndroidUtilities.bold());
                this.textView.setBackground(Theme.AdaptiveRipple.filledRect(getThemedColor(Theme.key_featuredStickers_addButton), 6.0f));
                textViewEmojis = this.textView;
                createFrame = LayoutHelper.createFrame(-1, -1.0f, 0, 16.0f, 16.0f, 16.0f, 16.0f);
            }
            addView(textViewEmojis, createFrame);
        }

        public ImageView getImageView() {
            return this.imageView;
        }

        public AnimatedEmojiSpan.TextViewEmojis getTextView() {
            return this.textView;
        }

        protected int getThemedColor(int i) {
            return Theme.getColor(i, this.resourcesProvider);
        }

        public boolean isChecked() {
            return this.checked;
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            if (this.isSelected) {
                accessibilityNodeInfo.setSelected(true);
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int i3 = this.currentType;
            int i4 = i3 == 2 ? 80 : 48;
            if (i3 == 0) {
                i = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824);
            }
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(i4), 1073741824));
        }

        public void setChecked(boolean z) {
            ImageView imageView = this.imageView2;
            this.checked = z;
            imageView.setImageResource(z ? R.drawable.checkbig : 0);
        }

        public void setGravity(int i) {
            this.textView.setGravity(i);
        }

        public void setIconColor(int i) {
            this.imageView.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
        }

        public void setTextAndIcon(CharSequence charSequence, int i) {
            setTextAndIcon(charSequence, i, null, false);
        }

        public void setTextAndIcon(CharSequence charSequence, int i, Drawable drawable, boolean z) {
            this.textView.setText(charSequence);
            if (i == 0 && drawable == null) {
                this.imageView.setVisibility(4);
                this.textView.setPadding(AndroidUtilities.dp(z ? 21.0f : 16.0f), 0, AndroidUtilities.dp(z ? 21.0f : 16.0f), 0);
                return;
            }
            if (drawable != null) {
                this.imageView.setImageDrawable(drawable);
            } else {
                this.imageView.setImageResource(i);
            }
            this.imageView.setVisibility(0);
            if (z) {
                this.textView.setPadding(AndroidUtilities.dp(LocaleController.isRTL ? 21.0f : 72.0f), 0, AndroidUtilities.dp(LocaleController.isRTL ? 72.0f : 21.0f), 0);
                this.imageView.setPadding(LocaleController.isRTL ? 0 : AndroidUtilities.dp(5.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(5.0f) : 5, 0);
            } else {
                this.textView.setPadding(AndroidUtilities.dp(LocaleController.isRTL ? 16.0f : 72.0f), 0, AndroidUtilities.dp(LocaleController.isRTL ? 72.0f : 16.0f), 0);
                this.imageView.setPadding(0, 0, 0, 0);
            }
        }

        public void setTextAndIcon(CharSequence charSequence, Drawable drawable) {
            setTextAndIcon(charSequence, 0, drawable, false);
        }

        public void setTextColor(int i) {
            this.textView.setTextColor(i);
        }
    }

    public static class BottomSheetDelegate implements BottomSheetDelegateInterface {
        @Override // org.telegram.ui.ActionBar.BottomSheet.BottomSheetDelegateInterface
        public boolean canDismiss() {
            return true;
        }

        @Override // org.telegram.ui.ActionBar.BottomSheet.BottomSheetDelegateInterface
        public void onOpenAnimationEnd() {
        }
    }

    public interface BottomSheetDelegateInterface {
        boolean canDismiss();

        void onOpenAnimationEnd();
    }

    public static class Builder {
        public static int CELL_TYPE_CALL = 4;
        private BottomSheet bottomSheet;

        public Builder(Context context) {
            this(context, false);
        }

        public Builder(Context context, boolean z) {
            this(context, z, null);
        }

        public Builder(Context context, boolean z, Theme.ResourcesProvider resourcesProvider) {
            BottomSheet bottomSheet = new BottomSheet(context, z, resourcesProvider);
            this.bottomSheet = bottomSheet;
            bottomSheet.fixNavigationBar();
        }

        public Builder(Context context, boolean z, Theme.ResourcesProvider resourcesProvider, int i) {
            BottomSheet bottomSheet = new BottomSheet(context, z, resourcesProvider);
            this.bottomSheet = bottomSheet;
            bottomSheet.setBackgroundColor(i);
            this.bottomSheet.fixNavigationBar(i);
        }

        public BottomSheet create() {
            return this.bottomSheet;
        }

        public Runnable getDismissRunnable() {
            return this.bottomSheet.dismissRunnable;
        }

        public Builder selectedPos(Integer num) {
            this.bottomSheet.selectedPos = num;
            return this;
        }

        public Builder setApplyBottomPadding(boolean z) {
            this.bottomSheet.applyBottomPadding = z;
            return this;
        }

        public Builder setApplyTopPadding(boolean z) {
            this.bottomSheet.applyTopPadding = z;
            return this;
        }

        public Builder setCellType(int i) {
            this.bottomSheet.cellType = i;
            return this;
        }

        public Builder setCustomView(View view) {
            this.bottomSheet.customView = view;
            return this;
        }

        public Builder setCustomView(View view, int i) {
            this.bottomSheet.customView = view;
            this.bottomSheet.customViewGravity = i;
            return this;
        }

        public Builder setDelegate(BottomSheetDelegate bottomSheetDelegate) {
            this.bottomSheet.setDelegate(bottomSheetDelegate);
            return this;
        }

        public BottomSheet setDimBehind(boolean z) {
            BottomSheet bottomSheet = this.bottomSheet;
            bottomSheet.dimBehind = z;
            return bottomSheet;
        }

        public Builder setItems(CharSequence[] charSequenceArr, DialogInterface.OnClickListener onClickListener) {
            this.bottomSheet.items = charSequenceArr;
            this.bottomSheet.onClickListener = onClickListener;
            return this;
        }

        public Builder setItems(CharSequence[] charSequenceArr, int[] iArr, DialogInterface.OnClickListener onClickListener) {
            this.bottomSheet.items = charSequenceArr;
            this.bottomSheet.itemIcons = iArr;
            this.bottomSheet.onClickListener = onClickListener;
            return this;
        }

        public Builder setOnPreDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.bottomSheet.setOnHideListener(onDismissListener);
            return this;
        }

        public Builder setTitle(CharSequence charSequence) {
            return setTitle(charSequence, false);
        }

        public Builder setTitle(CharSequence charSequence, boolean z) {
            this.bottomSheet.title = charSequence;
            this.bottomSheet.bigTitle = z;
            return this;
        }

        public Builder setTitleMultipleLines(boolean z) {
            this.bottomSheet.multipleLinesTitle = z;
            return this;
        }

        public BottomSheet show() {
            this.bottomSheet.show();
            return this.bottomSheet;
        }
    }

    public class ContainerView extends FrameLayout implements NestedScrollingParent {
        private boolean allowedSwipeToBack;
        private Paint backgroundPaint;
        private AnimatorSet currentAnimation;
        private boolean keyboardChanged;
        private boolean maybeStartTracking;
        private NestedScrollingParentHelper nestedScrollingParentHelper;
        private Rect rect;
        private boolean startedTracking;
        private int startedTrackingPointerId;
        private int startedTrackingX;
        private int startedTrackingY;
        private float swipeBackX;
        private VelocityTracker velocityTracker;
        private float y;

        public ContainerView(Context context) {
            super(context);
            this.velocityTracker = null;
            this.startedTrackingPointerId = -1;
            this.maybeStartTracking = false;
            this.startedTracking = false;
            this.currentAnimation = null;
            this.rect = new Rect();
            this.backgroundPaint = new Paint();
            this.y = 0.0f;
            this.swipeBackX = 0.0f;
            this.nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
            setWillNotDraw(false);
        }

        private void cancelCurrentAnimation() {
            AnimatorSet animatorSet = this.currentAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.currentAnimation = null;
            }
            BottomSheet.this.onSwipeStarts();
        }

        private void checkDismiss(float f, float f2) {
            if ((BottomSheet.this.containerView.getTranslationY() >= AndroidUtilities.getPixelsInCM(0.8f, false) || (f2 >= 3500.0f && Math.abs(f2) >= Math.abs(f))) && (f2 >= 0.0f || Math.abs(f2) < 3500.0f)) {
                boolean z = BottomSheet.this.allowCustomAnimation;
                BottomSheet.this.allowCustomAnimation = false;
                BottomSheet.this.useFastDismiss = true;
                BottomSheet.this.dismiss();
                BottomSheet.this.allowCustomAnimation = z;
                return;
            }
            this.maybeStartTracking = false;
            this.currentAnimation = new AnimatorSet();
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.BottomSheet$ContainerView$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    BottomSheet.ContainerView.this.lambda$checkDismiss$0(valueAnimator);
                }
            });
            this.currentAnimation.playTogether(ObjectAnimator.ofFloat(BottomSheet.this.containerView, "translationY", 0.0f), ofFloat);
            this.currentAnimation.setDuration((int) ((Math.max(0.0f, r2) / AndroidUtilities.getPixelsInCM(0.8f, false)) * 250.0f));
            this.currentAnimation.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.currentAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.BottomSheet.ContainerView.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (ContainerView.this.currentAnimation != null && ContainerView.this.currentAnimation.equals(animator)) {
                        ContainerView.this.currentAnimation = null;
                    }
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 512);
                }
            });
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, 512);
            this.currentAnimation.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$checkDismiss$0(ValueAnimator valueAnimator) {
            ContainerView containerView = BottomSheet.this.container;
            if (containerView != null) {
                containerView.invalidate();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLayout$4(ValueAnimator valueAnimator) {
            BottomSheet.this.containerView.setTranslationY(((Float) valueAnimator.getAnimatedValue()).floatValue());
            BottomSheet bottomSheet = BottomSheet.this;
            bottomSheet.onSmoothContainerViewLayout(bottomSheet.containerView.getTranslationY());
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$processTouchEvent$1(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.swipeBackX = floatValue;
            BottomSheet.this.containerView.setTranslationX(floatValue);
            BottomSheet.this.container.invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$processTouchEvent$2(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.swipeBackX = floatValue;
            BottomSheet.this.containerView.setTranslationX(floatValue);
            BottomSheet.this.container.invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$processTouchEvent$3(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            BottomSheet bottomSheet = BottomSheet.this;
            bottomSheet.backDrawable.setAlpha(bottomSheet.dimBehind ? (int) (bottomSheet.dimBehindAlpha * floatValue) : 0);
        }

        /* JADX WARN: Removed duplicated region for block: B:28:0x00cf  */
        /* JADX WARN: Removed duplicated region for block: B:40:0x00ff  */
        /* JADX WARN: Removed duplicated region for block: B:51:0x0154  */
        /* JADX WARN: Removed duplicated region for block: B:56:0x0188  */
        /* JADX WARN: Removed duplicated region for block: B:63:? A[RETURN, SYNTHETIC] */
        @Override // android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void dispatchDraw(Canvas canvas) {
            BottomSheet bottomSheet;
            BottomSheet bottomSheet2;
            BottomSheet bottomSheet3;
            BottomSheet bottomSheet4;
            int i = Build.VERSION.SDK_INT;
            if (i >= 26) {
                BottomSheet bottomSheet5 = BottomSheet.this;
                int i2 = bottomSheet5.navBarColorKey;
                if (i2 >= 0) {
                    this.backgroundPaint.setColor(bottomSheet5.getThemedColor(i2));
                } else {
                    this.backgroundPaint.setColor(bottomSheet5.navBarColor);
                }
            } else {
                this.backgroundPaint.setColor(-16777216);
            }
            BottomSheet bottomSheet6 = BottomSheet.this;
            if (bottomSheet6.drawDoubleNavigationBar && !bottomSheet6.shouldOverlayCameraViewOverNavBar()) {
                drawNavigationBar(canvas, 1.0f);
            }
            if (this.backgroundPaint.getAlpha() < 255) {
                BottomSheet bottomSheet7 = BottomSheet.this;
                if (bottomSheet7.drawNavigationBar) {
                    float max = (bottomSheet7.scrollNavBar || (i >= 29 && bottomSheet7.getAdditionalMandatoryOffsets() > 0)) ? Math.max(0.0f, BottomSheet.this.getBottomInset() - (BottomSheet.this.containerView.getMeasuredHeight() - BottomSheet.this.containerView.getTranslationY())) : 0.0f;
                    BottomSheet bottomSheet8 = BottomSheet.this;
                    int bottomInset = bottomSheet8.drawNavigationBar ? bottomSheet8.getBottomInset() : 0;
                    canvas.save();
                    canvas.clipRect(BottomSheet.this.containerView.getLeft() + BottomSheet.this.backgroundPaddingLeft, ((getMeasuredHeight() - bottomInset) + max) - BottomSheet.this.currentPanTranslationY, BottomSheet.this.containerView.getRight() - BottomSheet.this.backgroundPaddingLeft, getMeasuredHeight() + max, Region.Op.DIFFERENCE);
                    super.dispatchDraw(canvas);
                    canvas.restore();
                    if (!BottomSheet.this.shouldOverlayCameraViewOverNavBar()) {
                        BottomSheet bottomSheet9 = BottomSheet.this;
                        drawNavigationBar(canvas, bottomSheet9.drawDoubleNavigationBar ? bottomSheet9.navigationBarAlpha * 0.7f : 1.0f);
                    }
                    bottomSheet = BottomSheet.this;
                    if (bottomSheet.drawNavigationBar && bottomSheet.rightInset != 0 && BottomSheet.this.rightInset > BottomSheet.this.leftInset) {
                        bottomSheet4 = BottomSheet.this;
                        if (bottomSheet4.fullWidth) {
                            Point point = AndroidUtilities.displaySize;
                            if (point.x > point.y) {
                                int right = bottomSheet4.containerView.getRight();
                                canvas.drawRect(right - r1.backgroundPaddingLeft, BottomSheet.this.containerView.getTranslationY(), BottomSheet.this.containerView.getRight() + BottomSheet.this.rightInset, getMeasuredHeight(), this.backgroundPaint);
                            }
                        }
                    }
                    bottomSheet2 = BottomSheet.this;
                    if (bottomSheet2.drawNavigationBar && bottomSheet2.leftInset != 0 && BottomSheet.this.leftInset > BottomSheet.this.rightInset) {
                        bottomSheet3 = BottomSheet.this;
                        if (bottomSheet3.fullWidth) {
                            Point point2 = AndroidUtilities.displaySize;
                            if (point2.x > point2.y) {
                                canvas.drawRect(0.0f, bottomSheet3.containerView.getTranslationY(), BottomSheet.this.containerView.getLeft() + BottomSheet.this.backgroundPaddingLeft, getMeasuredHeight(), this.backgroundPaint);
                            }
                        }
                    }
                    if (BottomSheet.this.containerView.getTranslationY() >= 0.0f) {
                        Paint paint = this.backgroundPaint;
                        BottomSheet bottomSheet10 = BottomSheet.this;
                        int i3 = bottomSheet10.behindKeyboardColorKey;
                        paint.setColor(i3 >= 0 ? bottomSheet10.getThemedColor(i3) : bottomSheet10.behindKeyboardColor);
                        int left = BottomSheet.this.containerView.getLeft();
                        canvas.drawRect(left + r1.backgroundPaddingLeft, BottomSheet.this.containerView.getY() + BottomSheet.this.containerView.getMeasuredHeight(), BottomSheet.this.containerView.getRight() - BottomSheet.this.backgroundPaddingLeft, getMeasuredHeight(), this.backgroundPaint);
                        return;
                    }
                    return;
                }
            }
            super.dispatchDraw(canvas);
            if (!BottomSheet.this.shouldOverlayCameraViewOverNavBar()) {
            }
            bottomSheet = BottomSheet.this;
            if (bottomSheet.drawNavigationBar) {
                bottomSheet4 = BottomSheet.this;
                if (bottomSheet4.fullWidth) {
                }
            }
            bottomSheet2 = BottomSheet.this;
            if (bottomSheet2.drawNavigationBar) {
                bottomSheet3 = BottomSheet.this;
                if (bottomSheet3.fullWidth) {
                }
            }
            if (BottomSheet.this.containerView.getTranslationY() >= 0.0f) {
            }
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (!(view instanceof CameraView)) {
                return super.drawChild(canvas, view, j);
            }
            if (BottomSheet.this.shouldOverlayCameraViewOverNavBar()) {
                drawNavigationBar(canvas, 1.0f);
            }
            return super.drawChild(canvas, view, j);
        }

        public void drawNavigationBar(Canvas canvas, float f) {
            float max;
            int i = Build.VERSION.SDK_INT;
            if (i >= 26) {
                BottomSheet bottomSheet = BottomSheet.this;
                int i2 = bottomSheet.navBarColorKey;
                if (i2 >= 0) {
                    this.backgroundPaint.setColor(bottomSheet.getThemedColor(i2));
                } else {
                    this.backgroundPaint.setColor(bottomSheet.navBarColor);
                }
            } else {
                this.backgroundPaint.setColor(-16777216);
            }
            if (!BottomSheet.this.transitionFromRight || BottomSheet.this.containerView.getVisibility() == 0) {
                BottomSheet bottomSheet2 = BottomSheet.this;
                float f2 = 0.0f;
                if ((!bottomSheet2.drawNavigationBar || bottomSheet2.bottomInset == 0) && BottomSheet.this.currentPanTranslationY == 0.0f) {
                    return;
                }
                BottomSheet bottomSheet3 = BottomSheet.this;
                int bottomInset = bottomSheet3.drawNavigationBar ? bottomSheet3.getBottomInset() : 0;
                BottomSheet bottomSheet4 = BottomSheet.this;
                if (bottomSheet4.scrollNavBar || (i >= 29 && bottomSheet4.getAdditionalMandatoryOffsets() > 0)) {
                    BottomSheet bottomSheet5 = BottomSheet.this;
                    max = bottomSheet5.drawDoubleNavigationBar ? Math.max(0.0f, Math.min(bottomInset - bottomSheet5.currentPanTranslationY, BottomSheet.this.containerView.getTranslationY())) : Math.max(0.0f, BottomSheet.this.getBottomInset() - (bottomSheet5.containerView.getMeasuredHeight() - BottomSheet.this.containerView.getTranslationY()));
                } else {
                    max = 0.0f;
                }
                int alpha = this.backgroundPaint.getAlpha();
                if (BottomSheet.this.transitionFromRight) {
                    f *= BottomSheet.this.containerView.getAlpha();
                }
                int x = BottomSheet.this.transitionFromRight ? (int) BottomSheet.this.containerView.getX() : BottomSheet.this.containerView.getLeft();
                if (f < 1.0f) {
                    this.backgroundPaint.setAlpha((int) (alpha * f));
                }
                canvas.drawRect(BottomSheet.this.backgroundPaddingLeft + x, ((getMeasuredHeight() - bottomInset) + max) - BottomSheet.this.currentPanTranslationY, BottomSheet.this.containerView.getRight() - BottomSheet.this.backgroundPaddingLeft, getMeasuredHeight() + max, this.backgroundPaint);
                this.backgroundPaint.setAlpha(alpha);
                if (BottomSheet.this.overlayDrawNavBarColor != 0) {
                    this.backgroundPaint.setColor(BottomSheet.this.overlayDrawNavBarColor);
                    int alpha2 = this.backgroundPaint.getAlpha();
                    if (f < 1.0f) {
                        this.backgroundPaint.setAlpha((int) (alpha2 * f));
                    } else {
                        f2 = max;
                    }
                    canvas.drawRect(x + BottomSheet.this.backgroundPaddingLeft, ((getMeasuredHeight() - bottomInset) + f2) - BottomSheet.this.currentPanTranslationY, BottomSheet.this.containerView.getRight() - BottomSheet.this.backgroundPaddingLeft, getMeasuredHeight() + f2, this.backgroundPaint);
                    this.backgroundPaint.setAlpha(alpha2);
                }
            }
        }

        @Override // android.view.ViewGroup
        public int getNestedScrollAxes() {
            return this.nestedScrollingParentHelper.getNestedScrollAxes();
        }

        @Override // android.view.View
        public boolean hasOverlappingRendering() {
            return false;
        }

        /* JADX WARN: Removed duplicated region for block: B:21:0x009f  */
        /* JADX WARN: Removed duplicated region for block: B:35:0x00fd  */
        /* JADX WARN: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onDraw(Canvas canvas) {
            boolean z;
            BottomSheet bottomSheet;
            BottomSheet bottomSheet2;
            if (this.backgroundPaint.getAlpha() < 255) {
                BottomSheet bottomSheet3 = BottomSheet.this;
                if (bottomSheet3.drawNavigationBar) {
                    float f = 0.0f;
                    if (bottomSheet3.scrollNavBar || (Build.VERSION.SDK_INT >= 29 && bottomSheet3.getAdditionalMandatoryOffsets() > 0)) {
                        f = Math.max(0.0f, BottomSheet.this.getBottomInset() - (BottomSheet.this.containerView.getMeasuredHeight() - BottomSheet.this.containerView.getTranslationY()));
                    }
                    BottomSheet bottomSheet4 = BottomSheet.this;
                    int bottomInset = bottomSheet4.drawNavigationBar ? bottomSheet4.getBottomInset() : 0;
                    canvas.save();
                    canvas.clipRect(BottomSheet.this.containerView.getLeft() + BottomSheet.this.backgroundPaddingLeft, ((getMeasuredHeight() - bottomInset) + f) - BottomSheet.this.currentPanTranslationY, BottomSheet.this.containerView.getRight() - BottomSheet.this.backgroundPaddingLeft, getMeasuredHeight() + f, Region.Op.DIFFERENCE);
                    z = true;
                    super.onDraw(canvas);
                    bottomSheet = BottomSheet.this;
                    if (bottomSheet.drawNavigationBar && bottomSheet.lastInsets != null) {
                        bottomSheet2 = BottomSheet.this;
                        if (bottomSheet2.keyboardHeight != 0) {
                            Paint paint = this.backgroundPaint;
                            int i = bottomSheet2.behindKeyboardColorKey;
                            paint.setColor(i >= 0 ? bottomSheet2.getThemedColor(i) : bottomSheet2.behindKeyboardColor);
                            float left = BottomSheet.this.containerView.getLeft() + BottomSheet.this.backgroundPaddingLeft;
                            int measuredHeight = getMeasuredHeight();
                            BottomSheet bottomSheet5 = BottomSheet.this;
                            float bottomInset2 = (measuredHeight - bottomSheet5.keyboardHeight) - (bottomSheet5.drawNavigationBar ? bottomSheet5.getBottomInset() : 0);
                            float right = BottomSheet.this.containerView.getRight() - BottomSheet.this.backgroundPaddingLeft;
                            int measuredHeight2 = getMeasuredHeight();
                            canvas.drawRect(left, bottomInset2, right, measuredHeight2 - (BottomSheet.this.drawNavigationBar ? r3.getBottomInset() : 0), this.backgroundPaint);
                        }
                    }
                    BottomSheet.this.onContainerDraw(canvas);
                    if (z) {
                        return;
                    }
                    canvas.restore();
                    return;
                }
            }
            z = false;
            super.onDraw(canvas);
            bottomSheet = BottomSheet.this;
            if (bottomSheet.drawNavigationBar) {
                bottomSheet2 = BottomSheet.this;
                if (bottomSheet2.keyboardHeight != 0) {
                }
            }
            BottomSheet.this.onContainerDraw(canvas);
            if (z) {
            }
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return (BottomSheet.this.canDismissWithSwipe() || BottomSheet.this.canSwipeToBack(motionEvent)) ? processTouchEvent(motionEvent, true) : super.onInterceptTouchEvent(motionEvent);
        }

        /* JADX WARN: Code restructure failed: missing block: B:103:0x00df, code lost:
        
            if (java.lang.System.currentTimeMillis() < r16.this$0.smoothContainerViewLayoutUntil) goto L45;
         */
        /* JADX WARN: Removed duplicated region for block: B:29:0x00e9  */
        /* JADX WARN: Removed duplicated region for block: B:33:0x0115  */
        /* JADX WARN: Removed duplicated region for block: B:57:0x01f2  */
        /* JADX WARN: Removed duplicated region for block: B:62:0x0211  */
        /* JADX WARN: Removed duplicated region for block: B:70:0x0201  */
        /* JADX WARN: Removed duplicated region for block: B:97:0x00f9  */
        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5;
            int i6;
            BottomSheet bottomSheet;
            Runnable runnable;
            int i7;
            int i8;
            int i9;
            int i10;
            int i11;
            int i12;
            int i13;
            int systemWindowInsetBottom;
            BottomSheet.access$1510(BottomSheet.this);
            ViewGroup viewGroup = BottomSheet.this.containerView;
            if (viewGroup != null) {
                int measuredHeight = (i4 - i2) - viewGroup.getMeasuredHeight();
                if (BottomSheet.this.lastInsets == null || (i13 = Build.VERSION.SDK_INT) < 21) {
                    i11 = i;
                    i12 = i3;
                } else {
                    i11 = i + BottomSheet.this.getLeftInset();
                    i12 = i3 - BottomSheet.this.getRightInset();
                    BottomSheet bottomSheet2 = BottomSheet.this;
                    if (bottomSheet2.useSmoothKeyboard) {
                        measuredHeight = 0;
                    } else if (!bottomSheet2.occupyNavigationBar) {
                        float f = measuredHeight;
                        systemWindowInsetBottom = bottomSheet2.lastInsets.getSystemWindowInsetBottom();
                        measuredHeight = (int) (f - ((systemWindowInsetBottom * (1.0f - BottomSheet.this.hideSystemVerticalInsetsProgress)) - (BottomSheet.this.drawNavigationBar ? 0 : r6.getBottomInset())));
                        if (i13 >= 29) {
                            measuredHeight -= BottomSheet.this.getAdditionalMandatoryOffsets();
                        }
                    }
                }
                int measuredWidth = ((i12 - i11) - BottomSheet.this.containerView.getMeasuredWidth()) / 2;
                if (BottomSheet.this.lastInsets != null && Build.VERSION.SDK_INT >= 21) {
                    measuredWidth += BottomSheet.this.getLeftInset();
                }
                BottomSheet bottomSheet3 = BottomSheet.this;
                if (bottomSheet3.smoothKeyboardAnimationEnabled && bottomSheet3.startAnimationRunnable == null && this.keyboardChanged && !bottomSheet3.dismissed) {
                    BottomSheet bottomSheet4 = BottomSheet.this;
                    boolean z2 = bottomSheet4.smoothKeyboardByBottom;
                    ViewGroup viewGroup2 = bottomSheet4.containerView;
                    if (z2) {
                        BottomSheet bottomSheet5 = BottomSheet.this;
                        bottomSheet5.containerView.setTranslationY(!bottomSheet5.smoothKeyboardByBottom ? r6.getBottom() - (BottomSheet.this.containerView.getMeasuredHeight() + measuredHeight) : r6.getTop() - measuredHeight);
                        BottomSheet bottomSheet6 = BottomSheet.this;
                        bottomSheet6.onSmoothContainerViewLayout(bottomSheet6.containerView.getTranslationY());
                        if (BottomSheet.this.keyboardContentAnimator != null) {
                        }
                        BottomSheet bottomSheet7 = BottomSheet.this;
                        bottomSheet7.keyboardContentAnimator = ValueAnimator.ofFloat(bottomSheet7.containerView.getTranslationY(), 0.0f);
                        BottomSheet.this.keyboardContentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.BottomSheet$ContainerView$$ExternalSyntheticLambda0
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                BottomSheet.ContainerView.this.lambda$onLayout$4(valueAnimator);
                            }
                        });
                        BottomSheet.this.keyboardContentAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.BottomSheet.ContainerView.4
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                BottomSheet.this.containerView.setTranslationY(0.0f);
                                BottomSheet bottomSheet8 = BottomSheet.this;
                                bottomSheet8.onSmoothContainerViewLayout(bottomSheet8.containerView.getTranslationY());
                                ContainerView.this.invalidate();
                            }
                        });
                        BottomSheet.this.keyboardContentAnimator.setDuration(250L).setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                        BottomSheet.this.keyboardContentAnimator.start();
                        BottomSheet.this.smoothContainerViewLayoutUntil = -1L;
                        ViewGroup viewGroup3 = BottomSheet.this.containerView;
                        viewGroup3.layout(measuredWidth, measuredHeight, viewGroup3.getMeasuredWidth() + measuredWidth, BottomSheet.this.containerView.getMeasuredHeight() + measuredHeight);
                        i5 = i11;
                        i6 = i12;
                    } else {
                        BottomSheet bottomSheet52 = BottomSheet.this;
                        bottomSheet52.containerView.setTranslationY(!bottomSheet52.smoothKeyboardByBottom ? r6.getBottom() - (BottomSheet.this.containerView.getMeasuredHeight() + measuredHeight) : r6.getTop() - measuredHeight);
                        BottomSheet bottomSheet62 = BottomSheet.this;
                        bottomSheet62.onSmoothContainerViewLayout(bottomSheet62.containerView.getTranslationY());
                        if (BottomSheet.this.keyboardContentAnimator != null) {
                            BottomSheet.this.keyboardContentAnimator.cancel();
                        }
                        BottomSheet bottomSheet72 = BottomSheet.this;
                        bottomSheet72.keyboardContentAnimator = ValueAnimator.ofFloat(bottomSheet72.containerView.getTranslationY(), 0.0f);
                        BottomSheet.this.keyboardContentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.BottomSheet$ContainerView$$ExternalSyntheticLambda0
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                BottomSheet.ContainerView.this.lambda$onLayout$4(valueAnimator);
                            }
                        });
                        BottomSheet.this.keyboardContentAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.BottomSheet.ContainerView.4
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                BottomSheet.this.containerView.setTranslationY(0.0f);
                                BottomSheet bottomSheet8 = BottomSheet.this;
                                bottomSheet8.onSmoothContainerViewLayout(bottomSheet8.containerView.getTranslationY());
                                ContainerView.this.invalidate();
                            }
                        });
                        BottomSheet.this.keyboardContentAnimator.setDuration(250L).setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                        BottomSheet.this.keyboardContentAnimator.start();
                        BottomSheet.this.smoothContainerViewLayoutUntil = -1L;
                        ViewGroup viewGroup32 = BottomSheet.this.containerView;
                        viewGroup32.layout(measuredWidth, measuredHeight, viewGroup32.getMeasuredWidth() + measuredWidth, BottomSheet.this.containerView.getMeasuredHeight() + measuredHeight);
                        i5 = i11;
                        i6 = i12;
                    }
                }
                if (BottomSheet.this.smoothContainerViewLayoutUntil > 0) {
                }
                ViewGroup viewGroup322 = BottomSheet.this.containerView;
                viewGroup322.layout(measuredWidth, measuredHeight, viewGroup322.getMeasuredWidth() + measuredWidth, BottomSheet.this.containerView.getMeasuredHeight() + measuredHeight);
                i5 = i11;
                i6 = i12;
            } else {
                i5 = i;
                i6 = i3;
            }
            int childCount = getChildCount();
            for (int i14 = 0; i14 < childCount; i14++) {
                View childAt = getChildAt(i14);
                if (childAt.getVisibility() != 8) {
                    BottomSheet bottomSheet8 = BottomSheet.this;
                    if (childAt != bottomSheet8.containerView) {
                        if (!bottomSheet8.onCustomLayout(childAt, i5, i2, i6, i4 - (bottomSheet8.drawNavigationBar ? bottomSheet8.getBottomInset() : 0))) {
                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                            int measuredWidth2 = childAt.getMeasuredWidth();
                            int measuredHeight2 = childAt.getMeasuredHeight();
                            int i15 = layoutParams.gravity;
                            if (i15 == -1) {
                                i15 = 51;
                            }
                            int i16 = i15 & 112;
                            int i17 = i15 & 7;
                            if (i17 == 1) {
                                i7 = (((i6 - i5) - measuredWidth2) / 2) + layoutParams.leftMargin;
                            } else if (i17 != 5) {
                                i8 = layoutParams.leftMargin;
                                if (i16 != 16) {
                                    i9 = (((i4 - i2) - measuredHeight2) / 2) + layoutParams.topMargin;
                                } else if (i16 != 80) {
                                    i10 = layoutParams.topMargin;
                                    if (BottomSheet.this.lastInsets != null && Build.VERSION.SDK_INT >= 21) {
                                        i8 += BottomSheet.this.getLeftInset();
                                    }
                                    childAt.layout(i8, i10, measuredWidth2 + i8, measuredHeight2 + i10);
                                } else {
                                    i9 = (i4 - i2) - measuredHeight2;
                                }
                                i10 = i9 - layoutParams.bottomMargin;
                                if (BottomSheet.this.lastInsets != null) {
                                    i8 += BottomSheet.this.getLeftInset();
                                }
                                childAt.layout(i8, i10, measuredWidth2 + i8, measuredHeight2 + i10);
                            } else {
                                i7 = i6 - measuredWidth2;
                            }
                            i8 = i7 - layoutParams.rightMargin;
                            if (i16 != 16) {
                            }
                            i10 = i9 - layoutParams.bottomMargin;
                            if (BottomSheet.this.lastInsets != null) {
                            }
                            childAt.layout(i8, i10, measuredWidth2 + i8, measuredHeight2 + i10);
                        }
                    }
                }
            }
            if (BottomSheet.this.layoutCount == 0 && (runnable = (bottomSheet = BottomSheet.this).startAnimationRunnable) != null && !bottomSheet.waitingKeyboard) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                BottomSheet.this.startAnimationRunnable.run();
                BottomSheet.this.startAnimationRunnable = null;
            }
            BottomSheet bottomSheet9 = BottomSheet.this;
            if (bottomSheet9.waitingKeyboard && bottomSheet9.keyboardVisible) {
                Runnable runnable2 = bottomSheet9.startAnimationRunnable;
                if (runnable2 != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable2);
                    BottomSheet.this.startAnimationRunnable.run();
                }
                BottomSheet.this.waitingKeyboard = false;
            }
            this.keyboardChanged = false;
        }

        /* JADX WARN: Removed duplicated region for block: B:45:0x0118  */
        /* JADX WARN: Removed duplicated region for block: B:61:0x0171  */
        /* JADX WARN: Removed duplicated region for block: B:64:0x017a  */
        /* JADX WARN: Removed duplicated region for block: B:76:0x01c8  */
        @Override // android.widget.FrameLayout, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onMeasure(int i, int i2) {
            int i3;
            BottomSheet bottomSheet;
            ViewGroup viewGroup;
            int childCount;
            int makeMeasureSpec;
            int bottomSheetWidth;
            int i4;
            BottomSheet bottomSheet2;
            int systemWindowInsetBottom;
            int i5;
            int systemWindowInsetBottom2;
            int systemWindowInsetLeft;
            int systemWindowInsetRight;
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            View rootView = getRootView();
            getWindowVisibleDisplayFrame(this.rect);
            BottomSheet bottomSheet3 = BottomSheet.this;
            int i6 = bottomSheet3.keyboardHeight;
            Rect rect = this.rect;
            if (rect.bottom == 0 || rect.top == 0) {
                bottomSheet3.keyboardHeight = 0;
            } else {
                int height = (int) ((rootView.getHeight() - (this.rect.top != 0 ? AndroidUtilities.statusBarHeight * (1.0f - BottomSheet.this.hideSystemVerticalInsetsProgress) : 0.0f)) - (AndroidUtilities.getViewInset(rootView) * (1.0f - BottomSheet.this.hideSystemVerticalInsetsProgress)));
                BottomSheet bottomSheet4 = BottomSheet.this;
                Rect rect2 = this.rect;
                bottomSheet4.keyboardHeight = Math.max(0, height - (rect2.bottom - rect2.top));
                if (BottomSheet.this.keyboardHeight < AndroidUtilities.dp(20.0f)) {
                    BottomSheet.this.keyboardHeight = 0;
                } else {
                    BottomSheet bottomSheet5 = BottomSheet.this;
                    bottomSheet5.lastKeyboardHeight = bottomSheet5.keyboardHeight;
                }
                BottomSheet bottomSheet6 = BottomSheet.this;
                BottomSheet.access$1020(bottomSheet6, bottomSheet6.keyboardHeight);
            }
            BottomSheet bottomSheet7 = BottomSheet.this;
            int i7 = bottomSheet7.keyboardHeight;
            if (i6 != i7) {
                this.keyboardChanged = true;
            }
            bottomSheet7.keyboardVisible = i7 > AndroidUtilities.dp(20.0f);
            if (BottomSheet.this.lastInsets != null && (i5 = Build.VERSION.SDK_INT) >= 21) {
                BottomSheet bottomSheet8 = BottomSheet.this;
                systemWindowInsetBottom2 = bottomSheet8.lastInsets.getSystemWindowInsetBottom();
                bottomSheet8.bottomInset = systemWindowInsetBottom2;
                BottomSheet bottomSheet9 = BottomSheet.this;
                systemWindowInsetLeft = bottomSheet9.lastInsets.getSystemWindowInsetLeft();
                bottomSheet9.leftInset = systemWindowInsetLeft;
                BottomSheet bottomSheet10 = BottomSheet.this;
                systemWindowInsetRight = bottomSheet10.lastInsets.getSystemWindowInsetRight();
                bottomSheet10.rightInset = systemWindowInsetRight;
                if (i5 >= 29) {
                    BottomSheet bottomSheet11 = BottomSheet.this;
                    BottomSheet.access$1012(bottomSheet11, bottomSheet11.getAdditionalMandatoryOffsets());
                }
                BottomSheet bottomSheet12 = BottomSheet.this;
                if (bottomSheet12.keyboardVisible) {
                    Rect rect3 = this.rect;
                    if (rect3.bottom != 0 && rect3.top != 0) {
                        BottomSheet.access$1020(bottomSheet12, bottomSheet12.keyboardHeight);
                    }
                }
                BottomSheet bottomSheet13 = BottomSheet.this;
                if (!bottomSheet13.drawNavigationBar && !bottomSheet13.occupyNavigationBar && !bottomSheet13.occupyNavigationBarWithoutKeyboard) {
                    i3 = size2 - bottomSheet13.getBottomInset();
                    setMeasuredDimension(size, i3);
                    bottomSheet = BottomSheet.this;
                    bottomSheet.navigationBarHeight = 0;
                    if (bottomSheet.lastInsets != null && (i4 = Build.VERSION.SDK_INT) >= 21) {
                        bottomSheet2 = BottomSheet.this;
                        if (!bottomSheet2.occupyNavigationBar) {
                            systemWindowInsetBottom = bottomSheet2.lastInsets.getSystemWindowInsetBottom();
                            int i8 = (int) (systemWindowInsetBottom * (1.0f - BottomSheet.this.hideSystemVerticalInsetsProgress));
                            if (i4 >= 29) {
                                i8 += BottomSheet.this.getAdditionalMandatoryOffsets();
                            }
                            if (!BottomSheet.this.occupyNavigationBarWithoutKeyboard || i8 > AndroidUtilities.navigationBarHeight + AndroidUtilities.dp(10.0f)) {
                                size2 -= i8;
                            }
                            BottomSheet.this.navigationBarHeight = Math.min(i8, AndroidUtilities.navigationBarHeight);
                        }
                    }
                    int i9 = size2;
                    if (BottomSheet.this.lastInsets != null && Build.VERSION.SDK_INT >= 21) {
                        size -= BottomSheet.this.getRightInset() + BottomSheet.this.getLeftInset();
                    }
                    int i10 = size;
                    BottomSheet bottomSheet14 = BottomSheet.this;
                    bottomSheet14.isPortrait = i10 < i9;
                    viewGroup = bottomSheet14.containerView;
                    if (viewGroup != null) {
                        if (bottomSheet14.fullWidth) {
                            makeMeasureSpec = View.MeasureSpec.makeMeasureSpec((bottomSheet14.backgroundPaddingLeft * 2) + i10, 1073741824);
                        } else {
                            if (AndroidUtilities.isTablet()) {
                                Point point = AndroidUtilities.displaySize;
                                bottomSheetWidth = (int) (Math.min(point.x, point.y) * 0.8f);
                            } else {
                                BottomSheet bottomSheet15 = BottomSheet.this;
                                bottomSheetWidth = bottomSheet15.getBottomSheetWidth(bottomSheet15.isPortrait, i10, i9);
                            }
                            makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(bottomSheetWidth + (BottomSheet.this.backgroundPaddingLeft * 2), 1073741824);
                            viewGroup = BottomSheet.this.containerView;
                        }
                        viewGroup.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(i9, Integer.MIN_VALUE));
                    }
                    childCount = getChildCount();
                    for (int i11 = 0; i11 < childCount; i11++) {
                        View childAt = getChildAt(i11);
                        if (childAt.getVisibility() != 8) {
                            BottomSheet bottomSheet16 = BottomSheet.this;
                            if (childAt != bottomSheet16.containerView && !bottomSheet16.onCustomMeasure(childAt, i10, i9)) {
                                measureChildWithMargins(childAt, View.MeasureSpec.makeMeasureSpec(i10, 1073741824), 0, View.MeasureSpec.makeMeasureSpec(i9, 1073741824), 0);
                            }
                        }
                    }
                }
            }
            i3 = size2;
            setMeasuredDimension(size, i3);
            bottomSheet = BottomSheet.this;
            bottomSheet.navigationBarHeight = 0;
            if (bottomSheet.lastInsets != null) {
                bottomSheet2 = BottomSheet.this;
                if (!bottomSheet2.occupyNavigationBar) {
                }
            }
            int i92 = size2;
            if (BottomSheet.this.lastInsets != null) {
                size -= BottomSheet.this.getRightInset() + BottomSheet.this.getLeftInset();
            }
            int i102 = size;
            BottomSheet bottomSheet142 = BottomSheet.this;
            bottomSheet142.isPortrait = i102 < i92;
            viewGroup = bottomSheet142.containerView;
            if (viewGroup != null) {
            }
            childCount = getChildCount();
            while (i11 < childCount) {
            }
        }

        @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
        public boolean onNestedFling(View view, float f, float f2, boolean z) {
            return false;
        }

        @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
        public boolean onNestedPreFling(View view, float f, float f2) {
            return false;
        }

        @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
        public void onNestedPreScroll(View view, int i, int i2, int[] iArr) {
            if (BottomSheet.this.dismissed || !BottomSheet.this.allowNestedScroll) {
                return;
            }
            cancelCurrentAnimation();
            float translationY = BottomSheet.this.containerView.getTranslationY();
            if (translationY <= 0.0f || i2 <= 0) {
                return;
            }
            float f = translationY - i2;
            iArr[1] = i2;
            BottomSheet.this.containerView.setTranslationY(f >= 0.0f ? f : 0.0f);
            BottomSheet.this.container.invalidate();
        }

        @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
        public void onNestedScroll(View view, int i, int i2, int i3, int i4) {
            if (BottomSheet.this.dismissed || !BottomSheet.this.allowNestedScroll) {
                return;
            }
            cancelCurrentAnimation();
            if (i4 != 0) {
                float translationY = BottomSheet.this.containerView.getTranslationY() - i4;
                if (translationY < 0.0f) {
                    translationY = 0.0f;
                }
                BottomSheet.this.containerView.setTranslationY(translationY);
                BottomSheet.this.container.invalidate();
            }
        }

        @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
        public void onNestedScrollAccepted(View view, View view2, int i) {
            this.nestedScrollingParentHelper.onNestedScrollAccepted(view, view2, i);
            if (BottomSheet.this.dismissed || !BottomSheet.this.allowNestedScroll) {
                return;
            }
            cancelCurrentAnimation();
        }

        @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
        public boolean onStartNestedScroll(View view, View view2, int i) {
            BottomSheet bottomSheet = BottomSheet.this;
            View view3 = bottomSheet.nestedScrollChild;
            if ((view3 == null || view == view3) && !bottomSheet.dismissed) {
                BottomSheet bottomSheet2 = BottomSheet.this;
                if (bottomSheet2.allowNestedScroll && i == 2 && !bottomSheet2.canDismissWithSwipe()) {
                    return true;
                }
            }
            return false;
        }

        @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
        public void onStopNestedScroll(View view) {
            this.nestedScrollingParentHelper.onStopNestedScroll(view);
            if (BottomSheet.this.dismissed) {
                return;
            }
            BottomSheet bottomSheet = BottomSheet.this;
            if (bottomSheet.allowNestedScroll) {
                bottomSheet.containerView.getTranslationY();
                checkDismiss(0.0f, 0.0f);
            }
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return processTouchEvent(motionEvent, false);
        }

        public boolean processTouchEvent(MotionEvent motionEvent, boolean z) {
            ValueAnimator ofFloat;
            if (BottomSheet.this.dismissed) {
                return false;
            }
            if (BottomSheet.this.onContainerTouchEvent(motionEvent)) {
                return true;
            }
            if (BottomSheet.this.canSwipeToBack(motionEvent) || this.allowedSwipeToBack) {
                if (motionEvent != null && ((motionEvent.getAction() == 0 || motionEvent.getAction() == 2) && !this.startedTracking && !this.maybeStartTracking && motionEvent.getPointerCount() == 1)) {
                    this.allowedSwipeToBack = true;
                    this.startedTrackingX = (int) motionEvent.getX();
                    this.startedTrackingY = (int) motionEvent.getY();
                    this.startedTrackingPointerId = motionEvent.getPointerId(0);
                    this.maybeStartTracking = true;
                    cancelCurrentAnimation();
                } else if (motionEvent != null && motionEvent.getAction() == 2 && motionEvent.getPointerId(0) == this.startedTrackingPointerId) {
                    float x = motionEvent.getX() - this.startedTrackingX;
                    float y = motionEvent.getY() - this.startedTrackingY;
                    if (this.velocityTracker == null) {
                        this.velocityTracker = VelocityTracker.obtain();
                    }
                    this.velocityTracker.addMovement(motionEvent);
                    if (!BottomSheet.this.disableScroll && this.maybeStartTracking && !this.startedTracking && x > 0.0f && x / 3.0f > Math.abs(y) && Math.abs(x) >= BottomSheet.this.touchSlop) {
                        this.startedTrackingX = (int) motionEvent.getX();
                        this.maybeStartTracking = false;
                        this.startedTracking = true;
                    } else if (this.startedTracking) {
                        float f = this.swipeBackX + x;
                        this.swipeBackX = f;
                        BottomSheet.this.containerView.setTranslationX(Math.max(f, 0.0f));
                        this.startedTrackingX = (int) motionEvent.getX();
                        BottomSheet.this.container.invalidate();
                    }
                } else if (motionEvent == null || (motionEvent.getPointerId(0) == this.startedTrackingPointerId && (motionEvent.getAction() == 3 || motionEvent.getAction() == 1 || motionEvent.getAction() == 6))) {
                    if (this.velocityTracker == null) {
                        this.velocityTracker = VelocityTracker.obtain();
                    }
                    float xVelocity = this.velocityTracker.getXVelocity();
                    float yVelocity = this.velocityTracker.getYVelocity();
                    if (this.swipeBackX >= BottomSheet.this.containerView.getMeasuredWidth() / 3.0f || (xVelocity >= 3500.0f && xVelocity >= yVelocity)) {
                        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.swipeBackX, getMeasuredWidth());
                        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.BottomSheet$ContainerView$$ExternalSyntheticLambda3
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                BottomSheet.ContainerView.this.lambda$processTouchEvent$2(valueAnimator);
                            }
                        });
                        ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.BottomSheet.ContainerView.3
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                BottomSheet.this.skipDismissAnimation = true;
                                BottomSheet.this.containerView.setTranslationX(r2.getMeasuredWidth());
                                BottomSheet.this.dismiss();
                                BottomSheet.this.container.invalidate();
                            }
                        });
                        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
                        ofFloat2.setInterpolator(cubicBezierInterpolator);
                        ofFloat2.setDuration(320L);
                        ofFloat2.start();
                        ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
                        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.BottomSheet$ContainerView$$ExternalSyntheticLambda4
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                BottomSheet.ContainerView.this.lambda$processTouchEvent$3(valueAnimator);
                            }
                        });
                        ofFloat.setInterpolator(cubicBezierInterpolator);
                        ofFloat.setDuration(320L);
                    } else {
                        float max = Math.max(this.swipeBackX, 0.0f);
                        this.swipeBackX = max;
                        ofFloat = ValueAnimator.ofFloat(max, 0.0f);
                        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.BottomSheet$ContainerView$$ExternalSyntheticLambda2
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                BottomSheet.ContainerView.this.lambda$processTouchEvent$1(valueAnimator);
                            }
                        });
                        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.BottomSheet.ContainerView.2
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                ContainerView.this.swipeBackX = 0.0f;
                                BottomSheet.this.containerView.setTranslationX(0.0f);
                                BottomSheet.this.container.invalidate();
                            }
                        });
                        ofFloat.setInterpolator(CubicBezierInterpolator.DEFAULT);
                        ofFloat.setDuration(220L);
                    }
                    ofFloat.start();
                    this.maybeStartTracking = false;
                    this.startedTracking = false;
                    this.startedTrackingPointerId = -1;
                    this.allowedSwipeToBack = false;
                }
            } else if (BottomSheet.this.canDismissWithTouchOutside() && motionEvent != null && ((motionEvent.getAction() == 0 || motionEvent.getAction() == 2) && !this.startedTracking && !this.maybeStartTracking && motionEvent.getPointerCount() == 1)) {
                this.startedTrackingX = (int) motionEvent.getX();
                int y2 = (int) motionEvent.getY();
                this.startedTrackingY = y2;
                if (y2 < BottomSheet.this.containerView.getTop() || this.startedTrackingX < BottomSheet.this.containerView.getLeft() || this.startedTrackingX > BottomSheet.this.containerView.getRight()) {
                    BottomSheet.this.onDismissWithTouchOutside();
                    return true;
                }
                BottomSheet.this.onScrollUpBegin(this.y);
                this.startedTrackingPointerId = motionEvent.getPointerId(0);
                this.maybeStartTracking = true;
                cancelCurrentAnimation();
                VelocityTracker velocityTracker = this.velocityTracker;
                if (velocityTracker != null) {
                    velocityTracker.clear();
                }
            } else if (BottomSheet.this.canDismissWithSwipe() && motionEvent != null && motionEvent.getAction() == 2 && motionEvent.getPointerId(0) == this.startedTrackingPointerId) {
                if (this.velocityTracker == null) {
                    this.velocityTracker = VelocityTracker.obtain();
                }
                float abs = Math.abs((int) (motionEvent.getX() - this.startedTrackingX));
                float y3 = ((int) motionEvent.getY()) - this.startedTrackingY;
                boolean onScrollUp = BottomSheet.this.onScrollUp(this.y + y3);
                this.velocityTracker.addMovement(motionEvent);
                if (!BottomSheet.this.disableScroll && this.maybeStartTracking && !this.startedTracking && y3 > 0.0f && y3 / 3.0f > Math.abs(abs) && Math.abs(y3) >= BottomSheet.this.touchSlop) {
                    this.startedTrackingY = (int) motionEvent.getY();
                    this.maybeStartTracking = false;
                    this.startedTracking = true;
                    requestDisallowInterceptTouchEvent(true);
                } else if (this.startedTracking) {
                    float f2 = this.y + y3;
                    this.y = f2;
                    if (!onScrollUp) {
                        this.y = Math.max(f2, 0.0f);
                    }
                    BottomSheet.this.containerView.setTranslationY(Math.max(this.y, 0.0f));
                    this.startedTrackingY = (int) motionEvent.getY();
                    BottomSheet.this.container.invalidate();
                }
            } else if (motionEvent == null || (motionEvent.getPointerId(0) == this.startedTrackingPointerId && (motionEvent.getAction() == 3 || motionEvent.getAction() == 1 || motionEvent.getAction() == 6))) {
                if (this.velocityTracker == null) {
                    this.velocityTracker = VelocityTracker.obtain();
                }
                this.velocityTracker.computeCurrentVelocity(MediaDataController.MAX_STYLE_RUNS_COUNT);
                BottomSheet.this.onScrollUpEnd(this.y);
                if (this.startedTracking || this.y > 0.0f) {
                    checkDismiss(this.velocityTracker.getXVelocity(), this.velocityTracker.getYVelocity());
                } else {
                    this.maybeStartTracking = false;
                }
                this.startedTracking = false;
                VelocityTracker velocityTracker2 = this.velocityTracker;
                if (velocityTracker2 != null) {
                    velocityTracker2.recycle();
                    this.velocityTracker = null;
                }
                this.startedTrackingPointerId = -1;
            }
            if ((z || !this.maybeStartTracking) && !this.startedTracking) {
                return (BottomSheet.this.canDismissWithSwipe() || BottomSheet.this.canSwipeToBack(motionEvent)) ? false : true;
            }
            return true;
        }

        @Override // android.view.ViewGroup, android.view.ViewParent
        public void requestDisallowInterceptTouchEvent(boolean z) {
            if (this.maybeStartTracking && !this.startedTracking) {
                onTouchEvent(null);
            }
            super.requestDisallowInterceptTouchEvent(z);
        }
    }

    public BottomSheet(Context context, boolean z) {
        this(context, z, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x00d5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public BottomSheet(Context context, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context, R.style.TransparentDialog);
        Window window;
        int i;
        this.currentAccount = UserConfig.selectedAccount;
        this.allowDrawContent = true;
        this.useHardwareLayer = true;
        this.backDrawable = new ColorDrawable(-16777216) { // from class: org.telegram.ui.ActionBar.BottomSheet.1
            @Override // android.graphics.drawable.ColorDrawable, android.graphics.drawable.Drawable
            public void setAlpha(int i2) {
                super.setAlpha(i2);
                BottomSheet.this.container.invalidate();
            }
        };
        this.useLightStatusBar = true;
        int i2 = Theme.key_dialogBackground;
        this.behindKeyboardColorKey = i2;
        this.canDismissWithSwipe = true;
        this.canDismissWithTouchOutside = true;
        this.allowCustomAnimation = true;
        this.statusBarHeight = AndroidUtilities.statusBarHeight;
        this.openInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.dimBehind = true;
        this.dimBehindAlpha = 51;
        this.allowNestedScroll = true;
        this.applyTopPadding = true;
        this.applyBottomPadding = true;
        this.itemViews = new ArrayList<>();
        this.dismissRunnable = new BottomSheet$$ExternalSyntheticLambda11(this);
        this.navigationBarAlpha = 0.0f;
        this.navBarColorKey = Theme.key_windowBackgroundGray;
        this.pauseAllHeavyOperations = true;
        this.notificationsLocker = new AnimationNotificationsLocker();
        this.useBackgroundTopPadding = true;
        this.customViewGravity = 51;
        this.smoothContainerViewLayoutUntil = -1L;
        this.resourcesProvider = resourcesProvider;
        int i3 = Build.VERSION.SDK_INT;
        if (i3 < 30) {
            if (i3 >= 21) {
                window = getWindow();
                i = -2147417856;
            }
            this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
            Rect rect = new Rect();
            Drawable mutate = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
            this.shadowDrawable = mutate;
            mutate.setColorFilter(new PorterDuffColorFilter(getThemedColor(i2), PorterDuff.Mode.MULTIPLY));
            this.shadowDrawable.getPadding(rect);
            this.backgroundPaddingLeft = rect.left;
            this.backgroundPaddingTop = rect.top;
            ContainerView containerView = new ContainerView(getContext()) { // from class: org.telegram.ui.ActionBar.BottomSheet.2
                @Override // org.telegram.ui.ActionBar.BottomSheet.ContainerView, android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    super.dispatchDraw(canvas);
                    BottomSheet.this.mainContainerDispatchDraw(canvas);
                }

                @Override // org.telegram.ui.ActionBar.BottomSheet.ContainerView, android.view.ViewGroup
                public boolean drawChild(Canvas canvas, View view, long j) {
                    try {
                        if (BottomSheet.this.allowDrawContent) {
                            if (super.drawChild(canvas, view, j)) {
                                return true;
                            }
                        }
                        return false;
                    } catch (Exception e) {
                        FileLog.e(e);
                        return true;
                    }
                }

                @Override // android.view.ViewGroup, android.view.View
                protected void onAttachedToWindow() {
                    super.onAttachedToWindow();
                    Bulletin.addDelegate(this, new Bulletin.Delegate() { // from class: org.telegram.ui.ActionBar.BottomSheet.2.1
                        @Override // org.telegram.ui.Components.Bulletin.Delegate
                        public /* synthetic */ boolean allowLayoutChanges() {
                            return Bulletin.Delegate.-CC.$default$allowLayoutChanges(this);
                        }

                        @Override // org.telegram.ui.Components.Bulletin.Delegate
                        public /* synthetic */ boolean bottomOffsetAnimated() {
                            return Bulletin.Delegate.-CC.$default$bottomOffsetAnimated(this);
                        }

                        @Override // org.telegram.ui.Components.Bulletin.Delegate
                        public /* synthetic */ boolean clipWithGradient(int i4) {
                            return Bulletin.Delegate.-CC.$default$clipWithGradient(this, i4);
                        }

                        @Override // org.telegram.ui.Components.Bulletin.Delegate
                        public /* synthetic */ int getBottomOffset(int i4) {
                            return Bulletin.Delegate.-CC.$default$getBottomOffset(this, i4);
                        }

                        @Override // org.telegram.ui.Components.Bulletin.Delegate
                        public int getTopOffset(int i4) {
                            return AndroidUtilities.statusBarHeight;
                        }

                        @Override // org.telegram.ui.Components.Bulletin.Delegate
                        public /* synthetic */ void onBottomOffsetChange(float f) {
                            Bulletin.Delegate.-CC.$default$onBottomOffsetChange(this, f);
                        }

                        @Override // org.telegram.ui.Components.Bulletin.Delegate
                        public /* synthetic */ void onHide(Bulletin bulletin) {
                            Bulletin.Delegate.-CC.$default$onHide(this, bulletin);
                        }

                        @Override // org.telegram.ui.Components.Bulletin.Delegate
                        public /* synthetic */ void onShow(Bulletin bulletin) {
                            Bulletin.Delegate.-CC.$default$onShow(this, bulletin);
                        }
                    });
                }

                @Override // android.view.View
                protected void onConfigurationChanged(Configuration configuration) {
                    BottomSheet.this.lastInsets = null;
                    if (Build.VERSION.SDK_INT >= 21) {
                        BottomSheet.this.container.requestApplyInsets();
                    }
                }

                @Override // android.view.ViewGroup, android.view.View
                protected void onDetachedFromWindow() {
                    super.onDetachedFromWindow();
                    Bulletin.removeDelegate(this);
                }
            };
            this.container = containerView;
            containerView.setClipChildren(false);
            this.container.setClipToPadding(false);
            this.container.setBackground(this.backDrawable);
            this.focusable = z;
            if (i3 >= 21) {
                this.container.setFitsSystemWindows(true);
                this.container.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: org.telegram.ui.ActionBar.BottomSheet$$ExternalSyntheticLambda12
                    @Override // android.view.View.OnApplyWindowInsetsListener
                    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                        WindowInsets lambda$new$1;
                        lambda$new$1 = BottomSheet.this.lambda$new$1(view, windowInsets);
                        return lambda$new$1;
                    }
                });
                this.container.setSystemUiVisibility(i3 >= 30 ? 1792 : 1280);
            }
            this.backDrawable.setAlpha(0);
        }
        window = getWindow();
        i = -2147483392;
        window.addFlags(i);
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        Rect rect2 = new Rect();
        Drawable mutate2 = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
        this.shadowDrawable = mutate2;
        mutate2.setColorFilter(new PorterDuffColorFilter(getThemedColor(i2), PorterDuff.Mode.MULTIPLY));
        this.shadowDrawable.getPadding(rect2);
        this.backgroundPaddingLeft = rect2.left;
        this.backgroundPaddingTop = rect2.top;
        ContainerView containerView2 = new ContainerView(getContext()) { // from class: org.telegram.ui.ActionBar.BottomSheet.2
            @Override // org.telegram.ui.ActionBar.BottomSheet.ContainerView, android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                BottomSheet.this.mainContainerDispatchDraw(canvas);
            }

            @Override // org.telegram.ui.ActionBar.BottomSheet.ContainerView, android.view.ViewGroup
            public boolean drawChild(Canvas canvas, View view, long j) {
                try {
                    if (BottomSheet.this.allowDrawContent) {
                        if (super.drawChild(canvas, view, j)) {
                            return true;
                        }
                    }
                    return false;
                } catch (Exception e) {
                    FileLog.e(e);
                    return true;
                }
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                Bulletin.addDelegate(this, new Bulletin.Delegate() { // from class: org.telegram.ui.ActionBar.BottomSheet.2.1
                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public /* synthetic */ boolean allowLayoutChanges() {
                        return Bulletin.Delegate.-CC.$default$allowLayoutChanges(this);
                    }

                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public /* synthetic */ boolean bottomOffsetAnimated() {
                        return Bulletin.Delegate.-CC.$default$bottomOffsetAnimated(this);
                    }

                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public /* synthetic */ boolean clipWithGradient(int i4) {
                        return Bulletin.Delegate.-CC.$default$clipWithGradient(this, i4);
                    }

                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public /* synthetic */ int getBottomOffset(int i4) {
                        return Bulletin.Delegate.-CC.$default$getBottomOffset(this, i4);
                    }

                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public int getTopOffset(int i4) {
                        return AndroidUtilities.statusBarHeight;
                    }

                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public /* synthetic */ void onBottomOffsetChange(float f) {
                        Bulletin.Delegate.-CC.$default$onBottomOffsetChange(this, f);
                    }

                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public /* synthetic */ void onHide(Bulletin bulletin) {
                        Bulletin.Delegate.-CC.$default$onHide(this, bulletin);
                    }

                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public /* synthetic */ void onShow(Bulletin bulletin) {
                        Bulletin.Delegate.-CC.$default$onShow(this, bulletin);
                    }
                });
            }

            @Override // android.view.View
            protected void onConfigurationChanged(Configuration configuration) {
                BottomSheet.this.lastInsets = null;
                if (Build.VERSION.SDK_INT >= 21) {
                    BottomSheet.this.container.requestApplyInsets();
                }
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                Bulletin.removeDelegate(this);
            }
        };
        this.container = containerView2;
        containerView2.setClipChildren(false);
        this.container.setClipToPadding(false);
        this.container.setBackground(this.backDrawable);
        this.focusable = z;
        if (i3 >= 21) {
        }
        this.backDrawable.setAlpha(0);
    }

    static /* synthetic */ int access$1012(BottomSheet bottomSheet, int i) {
        int i2 = bottomSheet.bottomInset + i;
        bottomSheet.bottomInset = i2;
        return i2;
    }

    static /* synthetic */ int access$1020(BottomSheet bottomSheet, int i) {
        int i2 = bottomSheet.bottomInset - i;
        bottomSheet.bottomInset = i2;
        return i2;
    }

    static /* synthetic */ int access$1510(BottomSheet bottomSheet) {
        int i = bottomSheet.layoutCount;
        bottomSheet.layoutCount = i - 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getAdditionalMandatoryOffsets() {
        WindowInsets windowInsets;
        Insets systemGestureInsets;
        int i;
        int i2;
        int i3;
        if (!this.calcMandatoryInsets || (windowInsets = this.lastInsets) == null) {
            return 0;
        }
        systemGestureInsets = windowInsets.getSystemGestureInsets();
        if (this.keyboardVisible || !this.drawNavigationBar || systemGestureInsets == null) {
            return 0;
        }
        i = systemGestureInsets.left;
        if (i == 0) {
            i3 = systemGestureInsets.right;
            if (i3 == 0) {
                return 0;
            }
        }
        i2 = systemGestureInsets.bottom;
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismiss$8() {
        try {
            dismissInternal();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismiss$9(ValueAnimator valueAnimator) {
        this.navigationBarAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        ContainerView containerView = this.container;
        if (containerView != null) {
            containerView.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissWithButtonClick$6(ValueAnimator valueAnimator) {
        int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        setItemColor(this.selectedPos.intValue(), intValue, intValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissWithButtonClick$7(int i, ValueAnimator valueAnimator) {
        int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        setItemColor(i, intValue, intValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ WindowInsets lambda$new$1(View view, WindowInsets windowInsets) {
        int systemWindowInsetTop;
        WindowInsets consumeSystemWindowInsets;
        WindowInsets windowInsets2;
        systemWindowInsetTop = windowInsets.getSystemWindowInsetTop();
        if ((systemWindowInsetTop != 0 || AndroidUtilities.isInMultiwindow) && this.statusBarHeight != systemWindowInsetTop) {
            this.statusBarHeight = systemWindowInsetTop;
        }
        this.lastInsets = windowInsets;
        view.requestLayout();
        onInsetsChanged();
        if (Build.VERSION.SDK_INT >= 30) {
            windowInsets2 = WindowInsets.CONSUMED;
            return windowInsets2;
        }
        consumeSystemWindowInsets = windowInsets.consumeSystemWindowInsets();
        return consumeSystemWindowInsets;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateInternal$2() {
        AndroidUtilities.removeFromParent(this.container);
        this.attachedFragment.getLayoutContainer().addView(this.container);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onCreateInternal$3(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateInternal$4(View view) {
        dismissWithButtonClick(((Integer) view.getTag()).intValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setHideSystemVerticalInsets$0(ValueAnimator valueAnimator) {
        this.hideSystemVerticalInsetsProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.container.requestLayout();
        this.containerView.requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOpenAnimation$5(ValueAnimator valueAnimator) {
        this.navigationBarAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        ContainerView containerView = this.container;
        if (containerView != null) {
            containerView.invalidate();
        }
    }

    private void onCreateInternal() {
        Window window;
        int i;
        float f;
        TextView textView;
        int dp;
        float f2;
        TextView textView2;
        TextUtils.TruncateAt truncateAt;
        BaseFragment baseFragment = this.attachedFragment;
        if (baseFragment != null) {
            baseFragment.addSheet(this);
            if (this.attachedFragment.getLayoutContainer() == null) {
                return;
            }
            if (((InputMethodManager) getContext().getSystemService("input_method")).hideSoftInputFromWindow(this.attachedFragment.getLayoutContainer().getWindowToken(), 2)) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.BottomSheet$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        BottomSheet.this.lambda$onCreateInternal$2();
                    }
                }, 80L);
            } else {
                AndroidUtilities.removeFromParent(this.container);
                this.attachedFragment.getLayoutContainer().addView(this.container);
            }
            window = null;
        } else {
            window = getWindow();
            window.setWindowAnimations(R.style.DialogNoAnimation);
            setContentView(this.container, new ViewGroup.LayoutParams(-1, -1));
        }
        if (this.useLightStatusBar && Build.VERSION.SDK_INT >= 23 && Theme.getColor(Theme.key_actionBarDefault, null, true) == -1) {
            this.container.setSystemUiVisibility(this.container.getSystemUiVisibility() | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM);
        }
        if (this.useLightNavBar && Build.VERSION.SDK_INT >= 26) {
            AndroidUtilities.setLightNavigationBar(getWindow(), false);
        }
        if (this.containerView == null) {
            FrameLayout frameLayout = new FrameLayout(getContext()) { // from class: org.telegram.ui.ActionBar.BottomSheet.3
                @Override // android.view.View
                public boolean hasOverlappingRendering() {
                    return false;
                }

                @Override // android.view.View
                public void setTranslationY(float f3) {
                    super.setTranslationY(f3);
                    FrameLayout frameLayout2 = BottomSheet.this.topBulletinContainer;
                    if (frameLayout2 != null) {
                        frameLayout2.setTranslationY((-(r0.container.getHeight() - BottomSheet.this.containerView.getY())) + BottomSheet.this.backgroundPaddingTop);
                    }
                    BottomSheet.this.onContainerTranslationYChanged(f3);
                }
            };
            this.containerView = frameLayout;
            frameLayout.setBackgroundDrawable(this.shadowDrawable);
            this.containerView.setPadding(this.backgroundPaddingLeft, ((this.applyTopPadding ? AndroidUtilities.dp(8.0f) : 0) + this.backgroundPaddingTop) - 1, this.backgroundPaddingLeft, this.applyBottomPadding ? AndroidUtilities.dp(8.0f) : 0);
        }
        this.containerView.setVisibility(4);
        this.container.addView(this.containerView, 0, LayoutHelper.createFrame(-1, -2, 80));
        if (this.topBulletinContainer == null) {
            FrameLayout frameLayout2 = new FrameLayout(getContext());
            this.topBulletinContainer = frameLayout2;
            ContainerView containerView = this.container;
            containerView.addView(frameLayout2, containerView.indexOfChild(this.containerView) + 1, LayoutHelper.createFrame(-1, -2, 80));
        }
        if (this.title != null) {
            TextView textView3 = new TextView(getContext()) { // from class: org.telegram.ui.ActionBar.BottomSheet.4
                @Override // android.widget.TextView, android.view.View
                protected void onMeasure(int i2, int i3) {
                    super.onMeasure(i2, i3);
                    if (BottomSheet.this.multipleLinesTitle) {
                        int measuredHeight = getMeasuredHeight();
                        if (BottomSheet.this.customView != null) {
                            ((ViewGroup.MarginLayoutParams) BottomSheet.this.customView.getLayoutParams()).topMargin = measuredHeight;
                            return;
                        }
                        if (BottomSheet.this.containerView != null) {
                            for (int i4 = 1; i4 < BottomSheet.this.containerView.getChildCount(); i4++) {
                                View childAt = BottomSheet.this.containerView.getChildAt(i4);
                                if (childAt instanceof BottomSheetCell) {
                                    ((ViewGroup.MarginLayoutParams) childAt.getLayoutParams()).topMargin = measuredHeight;
                                    measuredHeight += AndroidUtilities.dp(48.0f);
                                }
                            }
                        }
                    }
                }
            };
            this.titleView = textView3;
            textView3.setText(this.title);
            if (this.bigTitle) {
                this.titleView.setTextColor(getThemedColor(Theme.key_dialogTextBlack));
                this.titleView.setTextSize(1, 20.0f);
                this.titleView.setTypeface(AndroidUtilities.bold());
                textView = this.titleView;
                f = 21.0f;
                dp = AndroidUtilities.dp(21.0f);
                f2 = this.multipleLinesTitle ? 14.0f : 6.0f;
            } else {
                this.titleView.setTextColor(getThemedColor(Theme.key_dialogTextGray2));
                f = 16.0f;
                this.titleView.setTextSize(1, 16.0f);
                textView = this.titleView;
                dp = AndroidUtilities.dp(16.0f);
                f2 = this.multipleLinesTitle ? 8.0f : 0.0f;
            }
            textView.setPadding(dp, AndroidUtilities.dp(f2), AndroidUtilities.dp(f), AndroidUtilities.dp(8.0f));
            if (this.multipleLinesTitle) {
                this.titleView.setSingleLine(false);
                this.titleView.setMaxLines(5);
                textView2 = this.titleView;
                truncateAt = TextUtils.TruncateAt.END;
            } else {
                this.titleView.setLines(1);
                this.titleView.setSingleLine(true);
                textView2 = this.titleView;
                truncateAt = TextUtils.TruncateAt.MIDDLE;
            }
            textView2.setEllipsize(truncateAt);
            this.titleView.setGravity(16);
            i = 48;
            this.containerView.addView(this.titleView, LayoutHelper.createFrame(-1, this.multipleLinesTitle ? -2.0f : 48));
            this.titleView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.ActionBar.BottomSheet$$ExternalSyntheticLambda5
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    boolean lambda$onCreateInternal$3;
                    lambda$onCreateInternal$3 = BottomSheet.lambda$onCreateInternal$3(view, motionEvent);
                    return lambda$onCreateInternal$3;
                }
            });
        } else {
            i = 0;
        }
        View view = this.customView;
        if (view != null) {
            if (view.getParent() != null) {
                ((ViewGroup) this.customView.getParent()).removeView(this.customView);
            }
            if (this.useBackgroundTopPadding) {
                this.containerView.addView(this.customView, LayoutHelper.createFrame(-1, -2.0f, this.customViewGravity, 0.0f, i, 0.0f, 0.0f));
            } else {
                this.containerView.setClipToPadding(false);
                this.containerView.setClipChildren(false);
                this.container.setClipToPadding(false);
                this.container.setClipChildren(false);
                float f3 = i;
                this.containerView.addView(this.customView, LayoutHelper.createFrame(-1, -2.0f, this.customViewGravity, 0.0f, f3, 0.0f, 0.0f));
                ((ViewGroup.MarginLayoutParams) this.customView.getLayoutParams()).topMargin = (-this.backgroundPaddingTop) + AndroidUtilities.dp(f3);
            }
        } else if (this.items != null) {
            int i2 = 0;
            while (true) {
                CharSequence[] charSequenceArr = this.items;
                if (i2 >= charSequenceArr.length) {
                    break;
                }
                if (charSequenceArr[i2] != null) {
                    BottomSheetCell bottomSheetCell = new BottomSheetCell(getContext(), this.cellType, this.resourcesProvider);
                    CharSequence charSequence = this.items[i2];
                    int[] iArr = this.itemIcons;
                    bottomSheetCell.setTextAndIcon(charSequence, iArr != null ? iArr[i2] : 0, null, this.bigTitle);
                    this.containerView.addView(bottomSheetCell, LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, i, 0.0f, 0.0f));
                    i += 48;
                    bottomSheetCell.setTag(Integer.valueOf(i2));
                    bottomSheetCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ActionBar.BottomSheet$$ExternalSyntheticLambda6
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            BottomSheet.this.lambda$onCreateInternal$4(view2);
                        }
                    });
                    this.itemViews.add(bottomSheetCell);
                }
                i2++;
            }
        }
        if (this.attachedFragment == null && window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = -1;
            attributes.gravity = 51;
            attributes.dimAmount = 0.0f;
            int i3 = attributes.flags & (-3);
            attributes.flags = i3;
            if (this.focusable) {
                attributes.softInputMode = 16;
            } else {
                attributes.flags = i3 | 131072;
            }
            if (this.isFullscreen) {
                if (Build.VERSION.SDK_INT >= 21) {
                    attributes.flags |= -2147417856;
                }
                attributes.flags |= 1024;
                this.container.setSystemUiVisibility(1284);
            }
            attributes.height = -1;
            if (Build.VERSION.SDK_INT >= 28) {
                attributes.layoutInDisplayCutoutMode = 1;
            }
            window.setAttributes(attributes);
        }
    }

    private void setShowing(boolean z) {
        if (this.showing == z) {
            return;
        }
        this.showing = z;
        if (this.openedLayerNum > 0) {
            if (z) {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, Integer.valueOf(this.openedLayerNum));
            } else {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, Integer.valueOf(this.openedLayerNum));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startOpenAnimation() {
        AnimatorSet animatorSet;
        TimeInterpolator timeInterpolator;
        if (this.dismissed) {
            return;
        }
        this.containerView.setVisibility(0);
        if (onCustomOpenAnimation()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 20 && this.useHardwareLayer) {
            this.container.setLayerType(2, null);
        }
        if (this.transitionFromRight) {
            this.containerView.setTranslationX(AndroidUtilities.dp(48.0f));
            this.containerView.setAlpha(0.0f);
            this.containerView.setTranslationY(0.0f);
        } else {
            this.containerView.setTranslationY(getContainerViewHeight() + this.keyboardHeight + AndroidUtilities.dp(10.0f) + (this.scrollNavBar ? Math.max(0, Math.min(AndroidUtilities.navigationBarHeight, getBottomInset())) : 0));
        }
        this.currentSheetAnimationType = 1;
        ValueAnimator valueAnimator = this.navigationBarAnimation;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.navigationBarAlpha, 1.0f);
        this.navigationBarAnimation = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.BottomSheet$$ExternalSyntheticLambda10
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                BottomSheet.this.lambda$startOpenAnimation$5(valueAnimator2);
            }
        });
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.currentSheetAnimation = animatorSet2;
        animatorSet2.playTogether(ObjectAnimator.ofFloat(this.containerView, (Property<ViewGroup, Float>) View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.containerView, (Property<ViewGroup, Float>) View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.containerView, (Property<ViewGroup, Float>) View.TRANSLATION_Y, 0.0f), ObjectAnimator.ofInt(this.backDrawable, (Property<ColorDrawable, Integer>) AnimationProperties.COLOR_DRAWABLE_ALPHA, this.dimBehind ? this.dimBehindAlpha : 0), this.navigationBarAnimation);
        if (this.transitionFromRight) {
            this.currentSheetAnimation.setDuration(250L);
            animatorSet = this.currentSheetAnimation;
            timeInterpolator = CubicBezierInterpolator.DEFAULT;
        } else {
            this.currentSheetAnimation.setDuration(400L);
            animatorSet = this.currentSheetAnimation;
            timeInterpolator = this.openInterpolator;
        }
        animatorSet.setInterpolator(timeInterpolator);
        this.currentSheetAnimation.setStartDelay(this.waitingKeyboard ? 0L : 20L);
        this.currentSheetAnimation.setInterpolator(this.openInterpolator);
        this.notificationsLocker.lock();
        this.currentSheetAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.BottomSheet.6
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                AnimatorSet animatorSet3 = BottomSheet.this.currentSheetAnimation;
                if (animatorSet3 == null || !animatorSet3.equals(animator)) {
                    return;
                }
                BottomSheet bottomSheet = BottomSheet.this;
                bottomSheet.currentSheetAnimation = null;
                bottomSheet.currentSheetAnimationType = 0;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                AnimatorSet animatorSet3 = BottomSheet.this.currentSheetAnimation;
                if (animatorSet3 != null && animatorSet3.equals(animator)) {
                    BottomSheet bottomSheet = BottomSheet.this;
                    bottomSheet.currentSheetAnimation = null;
                    bottomSheet.currentSheetAnimationType = 0;
                    bottomSheet.onOpenAnimationEnd();
                    BottomSheetDelegateInterface bottomSheetDelegateInterface = BottomSheet.this.delegate;
                    if (bottomSheetDelegateInterface != null) {
                        bottomSheetDelegateInterface.onOpenAnimationEnd();
                    }
                    BottomSheet bottomSheet2 = BottomSheet.this;
                    if (bottomSheet2.useHardwareLayer) {
                        bottomSheet2.container.setLayerType(0, null);
                    }
                    BottomSheet bottomSheet3 = BottomSheet.this;
                    if (bottomSheet3.isFullscreen) {
                        WindowManager.LayoutParams attributes = bottomSheet3.getWindow().getAttributes();
                        attributes.flags &= -1025;
                        BottomSheet.this.getWindow().setAttributes(attributes);
                    }
                }
                if (BottomSheet.this.pauseAllHeavyOperations) {
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 512);
                }
                BottomSheet.this.notificationsLocker.unlock();
            }
        });
        if (this.pauseAllHeavyOperations) {
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, 512);
        }
        this.currentSheetAnimation.start();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public boolean attachedToParent() {
        ContainerView containerView = this.container;
        return containerView != null && containerView.isAttachedToWindow();
    }

    protected boolean canDismissWithSwipe() {
        return this.canDismissWithSwipe;
    }

    protected boolean canDismissWithTouchOutside() {
        return this.canDismissWithTouchOutside;
    }

    protected boolean canSwipeToBack(MotionEvent motionEvent) {
        return false;
    }

    protected void cancelSheetAnimation() {
        AnimatorSet animatorSet = this.currentSheetAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.currentSheetAnimation = null;
        }
        this.currentSheetAnimationType = 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x013e  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0149  */
    @Override // android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void dismiss() {
        long j;
        ObjectAnimator ofFloat;
        Bulletin visibleBulletin;
        BottomSheetDelegateInterface bottomSheetDelegateInterface = this.delegate;
        if ((bottomSheetDelegateInterface == null || bottomSheetDelegateInterface.canDismiss()) && !this.dismissed) {
            this.dismissed = true;
            DialogInterface.OnDismissListener onDismissListener = this.onHideListener;
            if (onDismissListener != null) {
                onDismissListener.onDismiss(this);
            }
            cancelSheetAnimation();
            onDismissAnimationStart();
            if (this.skipDismissAnimation) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.BottomSheet$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        BottomSheet.this.lambda$dismiss$8();
                    }
                });
            } else if (!this.allowCustomAnimation || !onCustomCloseAnimation()) {
                this.currentSheetAnimationType = 2;
                ValueAnimator valueAnimator = this.navigationBarAnimation;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.navigationBarAlpha, 0.0f);
                this.navigationBarAnimation = ofFloat2;
                ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.BottomSheet$$ExternalSyntheticLambda8
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        BottomSheet.this.lambda$dismiss$9(valueAnimator2);
                    }
                });
                this.currentSheetAnimation = new AnimatorSet();
                ArrayList arrayList = new ArrayList();
                ViewGroup viewGroup = this.containerView;
                if (viewGroup != null) {
                    if (this.transitionFromRight) {
                        arrayList.add(ObjectAnimator.ofFloat(viewGroup, (Property<ViewGroup, Float>) View.TRANSLATION_X, AndroidUtilities.dp(48.0f)));
                        ofFloat = ObjectAnimator.ofFloat(this.containerView, (Property<ViewGroup, Float>) View.ALPHA, 0.0f);
                    } else {
                        ofFloat = ObjectAnimator.ofFloat(viewGroup, (Property<ViewGroup, Float>) View.TRANSLATION_Y, getContainerViewHeight() + (this.forceKeyboardOnDismiss ? this.lastKeyboardHeight : this.keyboardHeight) + AndroidUtilities.dp(10.0f) + (this.scrollNavBar ? Math.max(0, Math.min(AndroidUtilities.navigationBarHeight, getBottomInset())) : 0));
                    }
                    arrayList.add(ofFloat);
                }
                arrayList.add(ObjectAnimator.ofInt(this.backDrawable, (Property<ColorDrawable, Integer>) AnimationProperties.COLOR_DRAWABLE_ALPHA, 0));
                arrayList.add(this.navigationBarAnimation);
                this.currentSheetAnimation.playTogether(arrayList);
                if (this.transitionFromRight) {
                    this.currentSheetAnimation.setDuration(200L);
                    this.currentSheetAnimation.setInterpolator(CubicBezierInterpolator.DEFAULT);
                    j = 0;
                } else {
                    j = 250;
                    this.currentSheetAnimation.setDuration(250L);
                    this.currentSheetAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT);
                }
                this.currentSheetAnimation.addListener(new 8());
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, 512);
                this.currentSheetAnimation.start();
                visibleBulletin = Bulletin.getVisibleBulletin();
                if (visibleBulletin != null && visibleBulletin.isShowing() && visibleBulletin.hideAfterBottomSheet) {
                    if (j <= 0) {
                        visibleBulletin.hide((long) (j * 0.6f));
                    } else {
                        visibleBulletin.hide();
                    }
                }
                setShowing(false);
            }
            j = 0;
            visibleBulletin = Bulletin.getVisibleBulletin();
            if (visibleBulletin != null) {
                if (j <= 0) {
                }
            }
            setShowing(false);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public void dismiss(boolean z) {
        dismiss();
    }

    public void dismissInternal() {
        BaseFragment baseFragment = this.attachedFragment;
        if (baseFragment != null) {
            baseFragment.removeSheet(this);
            AndroidUtilities.removeFromParent(this.container);
        } else {
            try {
                super.dismiss();
            } catch (Exception e) {
                FileLog.e((Throwable) e, false);
            }
        }
    }

    public void dismissWithButtonClick(final int i) {
        ValueAnimator ofArgb;
        ValueAnimator ofArgb2;
        if (this.dismissed) {
            return;
        }
        this.dismissed = true;
        cancelSheetAnimation();
        this.currentSheetAnimationType = 2;
        AnimatorSet animatorSet = new AnimatorSet();
        this.currentSheetAnimation = animatorSet;
        animatorSet.playTogether(ObjectAnimator.ofFloat(this.containerView, (Property<ViewGroup, Float>) View.TRANSLATION_Y, getContainerViewHeight() + this.keyboardHeight + AndroidUtilities.dp(10.0f) + (this.scrollNavBar ? Math.max(0, Math.min(AndroidUtilities.navigationBarHeight, getBottomInset())) : 0)), ObjectAnimator.ofInt(this.backDrawable, (Property<ColorDrawable, Integer>) AnimationProperties.COLOR_DRAWABLE_ALPHA, 0));
        this.currentSheetAnimation.setDuration(this.cellType == Builder.CELL_TYPE_CALL ? 330L : 180L);
        this.currentSheetAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT);
        this.currentSheetAnimation.addListener(new 7(i));
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, 512);
        this.currentSheetAnimation.start();
        if (this.cellType != Builder.CELL_TYPE_CALL || this.selectedPos == null || Build.VERSION.SDK_INT < 21) {
            return;
        }
        int currentTextColor = getItemViews().get(this.selectedPos.intValue()).getTextView().getCurrentTextColor();
        int currentTextColor2 = getItemViews().get(i).getTextView().getCurrentTextColor();
        ofArgb = ValueAnimator.ofArgb(currentTextColor, currentTextColor2);
        ofArgb.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.BottomSheet$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                BottomSheet.this.lambda$dismissWithButtonClick$6(valueAnimator);
            }
        });
        ofArgb.setDuration(130L);
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
        ofArgb.setInterpolator(cubicBezierInterpolator);
        ofArgb.start();
        ofArgb2 = ValueAnimator.ofArgb(currentTextColor2, currentTextColor);
        ofArgb2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.BottomSheet$$ExternalSyntheticLambda2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                BottomSheet.this.lambda$dismissWithButtonClick$7(i, valueAnimator);
            }
        });
        ofArgb2.setDuration(130L);
        ofArgb2.setInterpolator(cubicBezierInterpolator);
        ofArgb2.start();
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.dismissed) {
            return false;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void fixNavigationBar() {
        fixNavigationBar(getThemedColor(Theme.key_windowBackgroundGray));
    }

    public void fixNavigationBar(int i) {
        this.drawNavigationBar = !this.occupyNavigationBar;
        this.drawDoubleNavigationBar = true;
        this.scrollNavBar = true;
        this.navBarColorKey = -1;
        this.navBarColor = i;
        setOverlayNavBarColor(i);
    }

    public void forceKeyboardOnDismiss() {
        this.forceKeyboardOnDismiss = true;
    }

    public ColorDrawable getBackDrawable() {
        return this.backDrawable;
    }

    public int getBackgroundPaddingLeft() {
        return this.backgroundPaddingLeft;
    }

    public int getBackgroundPaddingTop() {
        return this.backgroundPaddingTop;
    }

    public int getBottomInset() {
        return (int) (this.bottomInset * (1.0f - this.hideSystemVerticalInsetsProgress));
    }

    protected int getBottomSheetWidth(boolean z, int i, int i2) {
        return z ? i : (int) Math.max(i * 0.8f, Math.min(AndroidUtilities.dp(480.0f), i));
    }

    public ContainerView getContainer() {
        return this.container;
    }

    public ViewGroup getContainerView() {
        return this.containerView;
    }

    public int getContainerViewHeight() {
        ViewGroup viewGroup = this.containerView;
        if (viewGroup == null) {
            return 0;
        }
        return viewGroup.getMeasuredHeight();
    }

    public int getCurrentAccount() {
        return this.currentAccount;
    }

    public ArrayList<BottomSheetCell> getItemViews() {
        return this.itemViews;
    }

    public int getLeftInset() {
        int systemWindowInsetLeft;
        WindowInsets windowInsets = this.lastInsets;
        if (windowInsets == null || Build.VERSION.SDK_INT < 21) {
            return 0;
        }
        systemWindowInsetLeft = windowInsets.getSystemWindowInsetLeft();
        return (int) (systemWindowInsetLeft * (1.0f - this.hideSystemVerticalInsetsProgress));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public int getNavigationBarColor(int i) {
        float f;
        ViewGroup viewGroup;
        if (!attachedToParent() || (viewGroup = this.containerView) == null) {
            f = 0.0f;
        } else if (this.transitionFromRight) {
            f = viewGroup.getAlpha();
        } else {
            f = Utilities.clamp01(1.0f - (this.containerView.getTranslationY() / (((getContainerViewHeight() + this.keyboardHeight) + AndroidUtilities.dp(10.0f)) + (this.scrollNavBar ? Math.max(0, Math.min(AndroidUtilities.navigationBarHeight, getBottomInset())) : 0))));
        }
        return ColorUtils.blendARGB(i, this.navBarColor, f);
    }

    public Theme.ResourcesProvider getResourcesProvider() {
        return this.resourcesProvider;
    }

    public int getRightInset() {
        int systemWindowInsetRight;
        WindowInsets windowInsets = this.lastInsets;
        if (windowInsets == null || Build.VERSION.SDK_INT < 21) {
            return 0;
        }
        systemWindowInsetRight = windowInsets.getSystemWindowInsetRight();
        return (int) (systemWindowInsetRight * (1.0f - this.hideSystemVerticalInsetsProgress));
    }

    public int getSheetAnimationType() {
        return this.currentSheetAnimationType;
    }

    public ViewGroup getSheetContainer() {
        return this.containerView;
    }

    public int getStatusBarHeight() {
        return (int) (this.statusBarHeight * (1.0f - this.hideSystemVerticalInsetsProgress));
    }

    public int getTag() {
        return this.tag;
    }

    protected int getTargetOpenTranslationY() {
        return 0;
    }

    public ArrayList<ThemeDescription> getThemeDescriptions() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    public TextView getTitleView() {
        return this.titleView;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public View getWindowView() {
        return this.container;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public boolean isAttachedLightStatusBar() {
        return this.useLightStatusBar;
    }

    public boolean isDismissed() {
        return this.dismissed;
    }

    public boolean isFocusable() {
        return this.focusable;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public boolean isFullyVisible() {
        return false;
    }

    public boolean isKeyboardVisible() {
        return this.keyboardVisible;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public boolean isShown() {
        return !this.dismissed;
    }

    protected void mainContainerDispatchDraw(Canvas canvas) {
    }

    public void makeAttached(BaseFragment baseFragment) {
        if (AndroidUtilities.isTablet()) {
            return;
        }
        this.attachedFragment = baseFragment;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public boolean onAttachedBackPressed() {
        onBackPressed();
        return true;
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        if (this.attachedFragment == null) {
            super.onBackPressed();
        } else {
            dismiss();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
    }

    public void onContainerDraw(Canvas canvas) {
    }

    protected boolean onContainerTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onContainerTranslationYChanged(float f) {
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        onCreateInternal();
    }

    protected boolean onCustomCloseAnimation() {
        return false;
    }

    protected boolean onCustomLayout(View view, int i, int i2, int i3, int i4) {
        return false;
    }

    protected boolean onCustomMeasure(View view, int i, int i2) {
        return false;
    }

    protected boolean onCustomOpenAnimation() {
        return false;
    }

    public void onDismissAnimationStart() {
    }

    protected void onDismissWithTouchOutside() {
        dismiss();
    }

    protected void onInsetsChanged() {
    }

    public void onOpenAnimationEnd() {
    }

    protected boolean onScrollUp(float f) {
        return false;
    }

    protected void onScrollUpBegin(float f) {
    }

    protected void onScrollUpEnd(float f) {
    }

    protected void onSmoothContainerViewLayout(float f) {
    }

    @Override // android.app.Dialog
    protected void onStart() {
        super.onStart();
    }

    protected void onSwipeStarts() {
    }

    public void release() {
        dismissInternal();
    }

    public void setAllowDrawContent(boolean z) {
        if (this.allowDrawContent != z) {
            this.allowDrawContent = z;
            this.container.setBackgroundDrawable(z ? this.backDrawable : null);
            this.container.invalidate();
        }
    }

    public void setAllowNestedScroll(boolean z) {
        this.allowNestedScroll = z;
        if (z) {
            return;
        }
        this.containerView.setTranslationY(0.0f);
    }

    public void setApplyBottomPadding(boolean z) {
        this.applyBottomPadding = z;
    }

    public void setApplyTopPadding(boolean z) {
        this.applyTopPadding = z;
    }

    public void setBackgroundColor(int i) {
        this.shadowDrawable.setColorFilter(i, PorterDuff.Mode.MULTIPLY);
    }

    public void setCalcMandatoryInsets(boolean z) {
        this.calcMandatoryInsets = z;
        this.drawNavigationBar = z;
    }

    public void setCanDismissWithSwipe(boolean z) {
        this.canDismissWithSwipe = z;
    }

    public void setCanDismissWithTouchOutside(boolean z) {
        this.canDismissWithTouchOutside = z;
    }

    public void setCurrentPanTranslationY(float f) {
        this.currentPanTranslationY = f;
        this.container.invalidate();
    }

    public void setCustomView(View view) {
        this.customView = view;
    }

    public void setDelegate(BottomSheetDelegateInterface bottomSheetDelegateInterface) {
        this.delegate = bottomSheetDelegateInterface;
    }

    public void setDimBehind(boolean z) {
        this.dimBehind = z;
    }

    public void setDimBehindAlpha(int i) {
        this.dimBehindAlpha = i;
    }

    public void setDisableScroll(boolean z) {
        this.disableScroll = z;
    }

    public void setFocusable(boolean z) {
        int i;
        if (this.focusable == z) {
            return;
        }
        this.focusable = z;
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (this.focusable) {
            attributes.softInputMode = 16;
            i = attributes.flags & (-131073);
        } else {
            attributes.softInputMode = 48;
            i = attributes.flags | 131072;
        }
        attributes.flags = i;
        window.setAttributes(attributes);
    }

    public void setHideSystemVerticalInsets(boolean z) {
        ValueAnimator duration = ValueAnimator.ofFloat(this.hideSystemVerticalInsetsProgress, z ? 1.0f : 0.0f).setDuration(180L);
        duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
        duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.BottomSheet$$ExternalSyntheticLambda9
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                BottomSheet.this.lambda$setHideSystemVerticalInsets$0(valueAnimator);
            }
        });
        duration.start();
    }

    public void setImageReceiverNumLevel(int i, int i2) {
        this.playingImagesLayerNum = i;
        this.openedLayerNum = i2;
    }

    public void setItemColor(int i, int i2, int i3) {
        if (i < 0 || i >= this.itemViews.size()) {
            return;
        }
        BottomSheetCell bottomSheetCell = this.itemViews.get(i);
        bottomSheetCell.textView.setTextColor(i2);
        bottomSheetCell.imageView.setColorFilter(new PorterDuffColorFilter(i3, PorterDuff.Mode.MULTIPLY));
    }

    public void setItemText(int i, CharSequence charSequence) {
        if (i < 0 || i >= this.itemViews.size()) {
            return;
        }
        this.itemViews.get(i).textView.setText(charSequence);
    }

    public void setItems(CharSequence[] charSequenceArr, int[] iArr, DialogInterface.OnClickListener onClickListener) {
        this.items = charSequenceArr;
        this.itemIcons = iArr;
        this.onClickListener = onClickListener;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public void setKeyboardHeightFromParent(int i) {
    }

    public /* synthetic */ void setLastVisible(boolean z) {
        BaseFragment.AttachedSheet.-CC.$default$setLastVisible(this, z);
    }

    @Override // android.app.Dialog
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public void setOnDismissListener(final Runnable runnable) {
        if (runnable != null) {
            setOnHideListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.ActionBar.BottomSheet$$ExternalSyntheticLambda3
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    runnable.run();
                }
            });
        }
    }

    public void setOnHideListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onHideListener = onDismissListener;
    }

    public void setOpenNoDelay(boolean z) {
        this.openNoDelay = z;
    }

    public void setOverlayNavBarColor(int i) {
        this.overlayDrawNavBarColor = i;
        ContainerView containerView = this.container;
        if (containerView != null) {
            containerView.invalidate();
        }
        if (this.attachedFragment != null) {
            LaunchActivity.instance.checkSystemBarColors(true, true, true, false);
            AndroidUtilities.setLightNavigationBar(getWindowView(), AndroidUtilities.computePerceivedBrightness(getNavigationBarColor(getThemedColor(Theme.key_windowBackgroundGray))) >= 0.721f);
        } else {
            AndroidUtilities.setNavigationBarColor(getWindow(), this.overlayDrawNavBarColor);
            AndroidUtilities.setLightNavigationBar(getWindow(), ((double) AndroidUtilities.computePerceivedBrightness(this.overlayDrawNavBarColor)) > 0.721d);
        }
    }

    public void setShowWithoutAnimation(boolean z) {
        this.showWithoutAnimation = z;
    }

    @Override // android.app.Dialog
    public void setTitle(CharSequence charSequence) {
        setTitle(charSequence, false);
    }

    public void setTitle(CharSequence charSequence, boolean z) {
        this.title = charSequence;
        this.bigTitle = z;
    }

    public void setTitleColor(int i) {
        TextView textView = this.titleView;
        if (textView == null) {
            return;
        }
        textView.setTextColor(i);
    }

    public void setUseLightStatusBar(boolean z) {
        this.useLightStatusBar = z;
        if (Build.VERSION.SDK_INT >= 23) {
            int color = Theme.getColor(Theme.key_actionBarDefault, null, true);
            int systemUiVisibility = this.container.getSystemUiVisibility();
            this.container.setSystemUiVisibility((this.useLightStatusBar && color == -1) ? systemUiVisibility | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : systemUiVisibility & (-8193));
        }
        if (this.attachedFragment != null) {
            LaunchActivity.instance.checkSystemBarColors(true, true, true, false);
        }
    }

    protected boolean shouldOverlayCameraViewOverNavBar() {
        return false;
    }

    @Override // android.app.Dialog
    public void show() {
        if (AndroidUtilities.isSafeToShow(getContext())) {
            if (this.attachedFragment != null) {
                onCreateInternal();
            } else {
                super.show();
            }
            setShowing(true);
            if (this.focusable) {
                getWindow().setSoftInputMode(16);
            }
            this.dismissed = false;
            cancelSheetAnimation();
            this.containerView.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x + (this.backgroundPaddingLeft * 2), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.y, Integer.MIN_VALUE));
            if (this.showWithoutAnimation) {
                this.backDrawable.setAlpha(this.dimBehind ? this.dimBehindAlpha : 0);
                this.containerView.setTranslationY(0.0f);
                return;
            }
            this.backDrawable.setAlpha(0);
            this.layoutCount = 2;
            this.containerView.setTranslationY((Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight * (1.0f - this.hideSystemVerticalInsetsProgress) : 0.0f) + r1.getMeasuredHeight() + (this.scrollNavBar ? Math.max(0, Math.min(AndroidUtilities.navigationBarHeight, getBottomInset())) : 0));
            long j = this.openNoDelay ? 0L : 150L;
            if (this.waitingKeyboard) {
                j = 500;
            }
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.ActionBar.BottomSheet.5
                @Override // java.lang.Runnable
                public void run() {
                    BottomSheet bottomSheet = BottomSheet.this;
                    if (bottomSheet.startAnimationRunnable != this || bottomSheet.dismissed) {
                        return;
                    }
                    BottomSheet bottomSheet2 = BottomSheet.this;
                    bottomSheet2.startAnimationRunnable = null;
                    bottomSheet2.startOpenAnimation();
                }
            };
            this.startAnimationRunnable = runnable;
            AndroidUtilities.runOnUIThread(runnable, j);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public boolean showDialog(Dialog dialog) {
        return false;
    }

    public void smoothContainerViewLayout() {
        this.smoothContainerViewLayoutUntil = System.currentTimeMillis() + 80;
    }

    public void transitionFromRight(boolean z) {
        this.transitionFromRight = z;
    }
}
