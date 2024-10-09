package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BillingController$$ExternalSyntheticLambda10;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.EffectsTextView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LineProgressView;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.spoilers.SpoilersTextView;

/* loaded from: classes4.dex */
public class AlertDialog extends Dialog implements Drawable.Callback, NotificationCenter.NotificationCenterDelegate {
    private View aboveMessageView;
    private int additioanalHorizontalPadding;
    private float aspectRatio;
    private int backgroundColor;
    private Rect backgroundPaddings;
    float blurAlpha;
    private boolean blurBehind;
    private Bitmap blurBitmap;
    private Matrix blurMatrix;
    private float blurOpacity;
    private Paint blurPaint;
    private BitmapShader blurShader;
    private boolean blurredBackground;
    private boolean blurredNativeBackground;
    private View bottomView;
    protected ViewGroup buttonsLayout;
    private boolean canCacnel;
    private AlertDialog cancelDialog;
    private boolean checkFocusable;
    private AlertDialogView containerView;
    private int[] containerViewLocation;
    private ScrollView contentScrollView;
    private int currentProgress;
    private boolean customMaxHeight;
    private View customView;
    private int customViewHeight;
    private int customViewOffset;
    private int customWidth;
    private int dialogButtonColorKey;
    private float dimAlpha;
    private Paint dimBlurPaint;
    private boolean dimCustom;
    private boolean dimEnabled;
    private boolean dismissDialogByButtons;
    private Runnable dismissRunnable;
    private boolean dismissed;
    private boolean drawBackground;
    private boolean focusable;
    private int[] itemIcons;
    private ArrayList itemViews;
    private CharSequence[] items;
    private int lastScreenWidth;
    private LineProgressView lineProgressView;
    private TextView lineProgressViewPercent;
    private CharSequence message;
    private TextView messageTextView;
    private boolean messageTextViewClickable;
    private DialogInterface.OnClickListener negativeButtonListener;
    private CharSequence negativeButtonText;
    private DialogInterface.OnClickListener neutralButtonListener;
    private CharSequence neutralButtonText;
    private boolean notDrawBackgroundOnTopView;
    private DialogInterface.OnClickListener onBackButtonListener;
    private DialogInterface.OnCancelListener onCancelListener;
    private DialogInterface.OnClickListener onClickListener;
    private DialogInterface.OnDismissListener onDismissListener;
    private ViewTreeObserver.OnScrollChangedListener onScrollChangedListener;
    private Utilities.Callback overridenDissmissListener;
    private DialogInterface.OnClickListener positiveButtonListener;
    private CharSequence positiveButtonText;
    private FrameLayout progressViewContainer;
    private int progressViewStyle;
    private final Theme.ResourcesProvider resourcesProvider;
    private LinearLayout scrollContainer;
    private CharSequence secondTitle;
    private TextView secondTitleTextView;
    private BitmapDrawable[] shadow;
    private AnimatorSet[] shadowAnimation;
    private Drawable shadowDrawable;
    private boolean[] shadowVisibility;
    private Runnable showRunnable;
    private long shownAt;
    private CharSequence subtitle;
    private TextView subtitleTextView;
    private CharSequence title;
    private FrameLayout titleContainer;
    private SpoilersTextView titleTextView;
    private boolean topAnimationAutoRepeat;
    private int topAnimationId;
    private boolean topAnimationIsNew;
    private Map topAnimationLayerColors;
    private int topAnimationSize;
    private int topBackgroundColor;
    private Drawable topDrawable;
    private int topHeight;
    private RLottieImageView topImageView;
    private int topResId;
    private View topView;
    private boolean verticalButtons;

    /* loaded from: classes4.dex */
    public static class AlertDialogCell extends FrameLayout {
        private ImageView imageView;
        private final Theme.ResourcesProvider resourcesProvider;
        private TextView textView;

        public AlertDialogCell(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.resourcesProvider = resourcesProvider;
            setBackground(Theme.createSelectorDrawable(getThemedColor(Theme.key_dialogButtonSelector), 2));
            setPadding(AndroidUtilities.dp(23.0f), 0, AndroidUtilities.dp(23.0f), 0);
            ImageView imageView = new ImageView(context);
            this.imageView = imageView;
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            this.imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_dialogIcon), PorterDuff.Mode.MULTIPLY));
            addView(this.imageView, LayoutHelper.createFrame(-2, 40, (LocaleController.isRTL ? 5 : 3) | 16));
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setLines(1);
            this.textView.setSingleLine(true);
            this.textView.setGravity(1);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            this.textView.setTextColor(getThemedColor(Theme.key_dialogTextBlack));
            this.textView.setTextSize(1, 16.0f);
            addView(this.textView, LayoutHelper.createFrame(-2, -2, (LocaleController.isRTL ? 5 : 3) | 16));
        }

        protected int getThemedColor(int i) {
            return Theme.getColor(i, this.resourcesProvider);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
        }

        public void setGravity(int i) {
            this.textView.setGravity(i);
        }

        public void setTextAndIcon(CharSequence charSequence, int i) {
            this.textView.setText(charSequence);
            if (i == 0) {
                this.imageView.setVisibility(4);
                this.textView.setPadding(0, 0, 0, 0);
            } else {
                this.imageView.setImageResource(i);
                this.imageView.setVisibility(0);
                this.textView.setPadding(LocaleController.isRTL ? 0 : AndroidUtilities.dp(56.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(56.0f) : 0, 0);
            }
        }

        public void setTextColor(int i) {
            this.textView.setTextColor(i);
        }
    }

    /* loaded from: classes4.dex */
    public class AlertDialogView extends LinearLayout {
        private Paint backgroundPaint;
        private AnimatedFloat blurPaintAlpha;
        private boolean inLayout;

        public AlertDialogView(Context context) {
            super(context);
            this.blurPaintAlpha = new AnimatedFloat(0.0f, this);
            this.backgroundPaint = new Paint(1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLayout$1() {
            AlertDialog alertDialog = AlertDialog.this;
            boolean z = false;
            alertDialog.runShadowAnimation(0, alertDialog.titleTextView != null && AlertDialog.this.contentScrollView.getScrollY() > AlertDialog.this.scrollContainer.getTop());
            AlertDialog alertDialog2 = AlertDialog.this;
            if (alertDialog2.buttonsLayout != null && alertDialog2.contentScrollView.getScrollY() + AlertDialog.this.contentScrollView.getHeight() < AlertDialog.this.scrollContainer.getBottom()) {
                z = true;
            }
            alertDialog2.runShadowAnimation(1, z);
            AlertDialog.this.contentScrollView.invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMeasure$0() {
            AlertDialog.this.lastScreenWidth = AndroidUtilities.displaySize.x;
            int dp = AndroidUtilities.displaySize.x - AndroidUtilities.dp(56.0f);
            int dp2 = AndroidUtilities.dp(AndroidUtilities.isTablet() ? AndroidUtilities.isSmallTablet() ? 446.0f : 496.0f : 356.0f);
            Window window = AlertDialog.this.getWindow();
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = Math.min(dp2, dp) + AlertDialog.this.backgroundPaddings.left + AlertDialog.this.backgroundPaddings.right;
            try {
                window.setAttributes(layoutParams);
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            if (AlertDialog.this.drawBackground && !AlertDialog.this.blurredBackground) {
                AlertDialog.this.shadowDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                if (AlertDialog.this.topView == null || !AlertDialog.this.notDrawBackgroundOnTopView) {
                    AlertDialog.this.shadowDrawable.draw(canvas);
                } else {
                    int bottom = AlertDialog.this.topView.getBottom();
                    canvas.save();
                    canvas.clipRect(0, bottom, getMeasuredWidth(), getMeasuredHeight());
                    AlertDialog.this.shadowDrawable.draw(canvas);
                    canvas.restore();
                }
            }
            super.dispatchDraw(canvas);
        }

        @Override // android.view.View
        public void draw(Canvas canvas) {
            float dp;
            if (AlertDialog.this.blurredBackground && !AlertDialog.this.blurredNativeBackground) {
                if (AlertDialog.this.progressViewStyle != 3 || AlertDialog.this.progressViewContainer == null) {
                    dp = AndroidUtilities.dp(10.0f);
                    AndroidUtilities.rectTmp.set(getPaddingLeft(), getPaddingTop(), getMeasuredWidth() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom());
                } else {
                    dp = AndroidUtilities.dp(18.0f);
                    float width = AlertDialog.this.progressViewContainer.getWidth() * AlertDialog.this.progressViewContainer.getScaleX();
                    float height = AlertDialog.this.progressViewContainer.getHeight() * AlertDialog.this.progressViewContainer.getScaleY();
                    AndroidUtilities.rectTmp.set((getWidth() - width) / 2.0f, (getHeight() - height) / 2.0f, (getWidth() + width) / 2.0f, (getHeight() + height) / 2.0f);
                }
                float f = this.blurPaintAlpha.set(AlertDialog.this.blurPaint != null ? 1.0f : 0.0f);
                if (AlertDialog.this.blurPaint != null) {
                    AlertDialog.this.blurPaint.setAlpha((int) (f * 255.0f));
                    canvas.drawRoundRect(AndroidUtilities.rectTmp, dp, dp, AlertDialog.this.blurPaint);
                }
                if (AlertDialog.this.dimBlurPaint == null) {
                    AlertDialog.this.dimBlurPaint = new Paint(1);
                    AlertDialog.this.dimBlurPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (AlertDialog.this.dimAlpha * 255.0f)));
                }
                RectF rectF = AndroidUtilities.rectTmp;
                canvas.drawRoundRect(rectF, dp, dp, AlertDialog.this.dimBlurPaint);
                this.backgroundPaint.setColor(AlertDialog.this.backgroundColor);
                this.backgroundPaint.setAlpha((int) (r4.getAlpha() * ((f * (AlertDialog.this.blurOpacity - 1.0f)) + 1.0f)));
                canvas.drawRoundRect(rectF, dp, dp, this.backgroundPaint);
            }
            super.draw(canvas);
        }

        @Override // android.view.View
        public boolean hasOverlappingRendering() {
            return false;
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (AlertDialog.this.progressViewStyle != 3) {
                return super.onInterceptTouchEvent(motionEvent);
            }
            AlertDialog.this.showCancelAlert();
            return false;
        }

        @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            if (AlertDialog.this.progressViewStyle == 3) {
                int measuredWidth = ((i3 - i) - AlertDialog.this.progressViewContainer.getMeasuredWidth()) / 2;
                int measuredHeight = ((i4 - i2) - AlertDialog.this.progressViewContainer.getMeasuredHeight()) / 2;
                AlertDialog.this.progressViewContainer.layout(measuredWidth, measuredHeight, AlertDialog.this.progressViewContainer.getMeasuredWidth() + measuredWidth, AlertDialog.this.progressViewContainer.getMeasuredHeight() + measuredHeight);
            } else if (AlertDialog.this.contentScrollView != null) {
                if (AlertDialog.this.onScrollChangedListener == null) {
                    AlertDialog.this.onScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() { // from class: org.telegram.ui.ActionBar.AlertDialog$AlertDialogView$$ExternalSyntheticLambda0
                        @Override // android.view.ViewTreeObserver.OnScrollChangedListener
                        public final void onScrollChanged() {
                            AlertDialog.AlertDialogView.this.lambda$onLayout$1();
                        }
                    };
                    AlertDialog.this.contentScrollView.getViewTreeObserver().addOnScrollChangedListener(AlertDialog.this.onScrollChangedListener);
                }
                AlertDialog.this.onScrollChangedListener.onScrollChanged();
            }
            getLocationOnScreen(AlertDialog.this.containerViewLocation);
            if (AlertDialog.this.blurMatrix == null || AlertDialog.this.blurShader == null) {
                return;
            }
            AlertDialog.this.blurMatrix.reset();
            AlertDialog.this.blurMatrix.postScale(8.0f, 8.0f);
            AlertDialog.this.blurMatrix.postTranslate(-AlertDialog.this.containerViewLocation[0], -AlertDialog.this.containerViewLocation[1]);
            AlertDialog.this.blurShader.setLocalMatrix(AlertDialog.this.blurMatrix);
        }

        /* JADX WARN: Removed duplicated region for block: B:96:0x0333  */
        @Override // android.widget.LinearLayout, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onMeasure(int i, int i2) {
            int i3;
            LinearLayout.LayoutParams layoutParams;
            View view;
            int dp;
            float f;
            float f2;
            if (AlertDialog.this.progressViewStyle == 3) {
                AlertDialog.this.progressViewContainer.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(86.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(86.0f), 1073741824));
                setMeasuredDimension(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
                return;
            }
            this.inLayout = true;
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            if (AlertDialog.this.customWidth > 0) {
                size = AlertDialog.this.customWidth + AlertDialog.this.backgroundPaddings.left + AlertDialog.this.backgroundPaddings.right;
            }
            int paddingTop = (size2 - getPaddingTop()) - getPaddingBottom();
            int paddingLeft = (size - getPaddingLeft()) - getPaddingRight();
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(paddingLeft - AndroidUtilities.dp(48.0f), 1073741824);
            int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(paddingLeft, 1073741824);
            ViewGroup viewGroup = AlertDialog.this.buttonsLayout;
            if (viewGroup != null) {
                int childCount = viewGroup.getChildCount();
                for (int i4 = 0; i4 < childCount; i4++) {
                    View childAt = AlertDialog.this.buttonsLayout.getChildAt(i4);
                    if (childAt instanceof TextView) {
                        ((TextView) childAt).setMaxWidth(AndroidUtilities.dp((paddingLeft - AndroidUtilities.dp(24.0f)) / 2));
                    }
                }
                AlertDialog.this.buttonsLayout.measure(makeMeasureSpec2, i2);
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) AlertDialog.this.buttonsLayout.getLayoutParams();
                i3 = paddingTop - ((AlertDialog.this.buttonsLayout.getMeasuredHeight() + layoutParams2.bottomMargin) + layoutParams2.topMargin);
            } else {
                i3 = paddingTop;
            }
            if (AlertDialog.this.secondTitleTextView != null) {
                AlertDialog.this.secondTitleTextView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(makeMeasureSpec), Integer.MIN_VALUE), i2);
            }
            if (AlertDialog.this.titleTextView != null) {
                if (AlertDialog.this.secondTitleTextView != null) {
                    AlertDialog.this.titleTextView.measure(View.MeasureSpec.makeMeasureSpec((View.MeasureSpec.getSize(makeMeasureSpec) - AlertDialog.this.secondTitleTextView.getMeasuredWidth()) - AndroidUtilities.dp(8.0f), 1073741824), i2);
                } else {
                    AlertDialog.this.titleTextView.measure(makeMeasureSpec, i2);
                }
            }
            if (AlertDialog.this.titleContainer != null) {
                AlertDialog.this.titleContainer.measure(makeMeasureSpec, i2);
                LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) AlertDialog.this.titleContainer.getLayoutParams();
                i3 -= (AlertDialog.this.titleContainer.getMeasuredHeight() + layoutParams3.bottomMargin) + layoutParams3.topMargin;
            }
            if (AlertDialog.this.subtitleTextView != null) {
                AlertDialog.this.subtitleTextView.measure(makeMeasureSpec, i2);
                LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) AlertDialog.this.subtitleTextView.getLayoutParams();
                i3 -= (AlertDialog.this.subtitleTextView.getMeasuredHeight() + layoutParams4.bottomMargin) + layoutParams4.topMargin;
            }
            if (AlertDialog.this.topImageView != null) {
                AlertDialog.this.topImageView.measure(View.MeasureSpec.makeMeasureSpec(paddingLeft, 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(AlertDialog.this.topHeight), 1073741824));
                i3 -= AlertDialog.this.topImageView.getMeasuredHeight();
            }
            if (AlertDialog.this.topView != null) {
                if (AlertDialog.this.aspectRatio == 0.0f) {
                    f = size / 936.0f;
                    f2 = 354.0f;
                } else {
                    f = size;
                    f2 = AlertDialog.this.aspectRatio;
                }
                int i5 = (int) (f * f2);
                AlertDialog.this.topView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(i5, 1073741824));
                AlertDialog.this.topView.getLayoutParams().height = i5;
                i3 -= AlertDialog.this.topView.getMeasuredHeight();
            }
            if (AlertDialog.this.progressViewStyle == 0) {
                LinearLayout.LayoutParams layoutParams5 = (LinearLayout.LayoutParams) AlertDialog.this.contentScrollView.getLayoutParams();
                if (AlertDialog.this.customView != null) {
                    layoutParams5.topMargin = (AlertDialog.this.titleTextView == null && AlertDialog.this.messageTextView.getVisibility() == 8 && AlertDialog.this.items == null) ? AndroidUtilities.dp(16.0f) : 0;
                    if (AlertDialog.this.buttonsLayout != null) {
                        dp = 0;
                        layoutParams5.bottomMargin = dp;
                        int i6 = i3 - (layoutParams5.bottomMargin + layoutParams5.topMargin);
                        AlertDialog.this.contentScrollView.measure(makeMeasureSpec2, View.MeasureSpec.makeMeasureSpec(i6, Integer.MIN_VALUE));
                        i3 = i6 - AlertDialog.this.contentScrollView.getMeasuredHeight();
                    }
                    dp = AndroidUtilities.dp(8.0f);
                    layoutParams5.bottomMargin = dp;
                    int i62 = i3 - (layoutParams5.bottomMargin + layoutParams5.topMargin);
                    AlertDialog.this.contentScrollView.measure(makeMeasureSpec2, View.MeasureSpec.makeMeasureSpec(i62, Integer.MIN_VALUE));
                    i3 = i62 - AlertDialog.this.contentScrollView.getMeasuredHeight();
                } else if (AlertDialog.this.items != null) {
                    layoutParams5.topMargin = (AlertDialog.this.titleTextView == null && AlertDialog.this.messageTextView.getVisibility() == 8) ? AndroidUtilities.dp(8.0f) : 0;
                    dp = AndroidUtilities.dp(8.0f);
                    layoutParams5.bottomMargin = dp;
                    int i622 = i3 - (layoutParams5.bottomMargin + layoutParams5.topMargin);
                    AlertDialog.this.contentScrollView.measure(makeMeasureSpec2, View.MeasureSpec.makeMeasureSpec(i622, Integer.MIN_VALUE));
                    i3 = i622 - AlertDialog.this.contentScrollView.getMeasuredHeight();
                } else {
                    if (AlertDialog.this.messageTextView.getVisibility() == 0) {
                        layoutParams5.topMargin = AlertDialog.this.titleTextView == null ? AndroidUtilities.dp(19.0f) : 0;
                        dp = AndroidUtilities.dp(20.0f);
                        layoutParams5.bottomMargin = dp;
                    }
                    int i6222 = i3 - (layoutParams5.bottomMargin + layoutParams5.topMargin);
                    AlertDialog.this.contentScrollView.measure(makeMeasureSpec2, View.MeasureSpec.makeMeasureSpec(i6222, Integer.MIN_VALUE));
                    i3 = i6222 - AlertDialog.this.contentScrollView.getMeasuredHeight();
                }
            } else {
                if (AlertDialog.this.progressViewContainer != null) {
                    AlertDialog.this.progressViewContainer.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(i3, Integer.MIN_VALUE));
                    layoutParams = (LinearLayout.LayoutParams) AlertDialog.this.progressViewContainer.getLayoutParams();
                    view = AlertDialog.this.progressViewContainer;
                } else {
                    if (AlertDialog.this.messageTextView != null) {
                        AlertDialog.this.messageTextView.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(i3, Integer.MIN_VALUE));
                        if (AlertDialog.this.messageTextView.getVisibility() != 8) {
                            layoutParams = (LinearLayout.LayoutParams) AlertDialog.this.messageTextView.getLayoutParams();
                            view = AlertDialog.this.messageTextView;
                        }
                    }
                    if (AlertDialog.this.lineProgressView != null) {
                        AlertDialog.this.lineProgressView.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(4.0f), 1073741824));
                        LinearLayout.LayoutParams layoutParams6 = (LinearLayout.LayoutParams) AlertDialog.this.lineProgressView.getLayoutParams();
                        int measuredHeight = i3 - ((AlertDialog.this.lineProgressView.getMeasuredHeight() + layoutParams6.bottomMargin) + layoutParams6.topMargin);
                        AlertDialog.this.lineProgressViewPercent.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(measuredHeight, Integer.MIN_VALUE));
                        LinearLayout.LayoutParams layoutParams7 = (LinearLayout.LayoutParams) AlertDialog.this.lineProgressViewPercent.getLayoutParams();
                        i3 = measuredHeight - ((AlertDialog.this.lineProgressViewPercent.getMeasuredHeight() + layoutParams7.bottomMargin) + layoutParams7.topMargin);
                    }
                }
                i3 -= (view.getMeasuredHeight() + layoutParams.bottomMargin) + layoutParams.topMargin;
                if (AlertDialog.this.lineProgressView != null) {
                }
            }
            setMeasuredDimension(size, (((paddingTop - i3) + getPaddingTop()) + getPaddingBottom()) - (AlertDialog.this.topAnimationIsNew ? AndroidUtilities.dp(8.0f) : 0));
            this.inLayout = false;
            if (AlertDialog.this.lastScreenWidth != AndroidUtilities.displaySize.x) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.AlertDialog$AlertDialogView$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        AlertDialog.AlertDialogView.this.lambda$onMeasure$0();
                    }
                });
            }
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (AlertDialog.this.progressViewStyle != 3) {
                return super.onTouchEvent(motionEvent);
            }
            AlertDialog.this.showCancelAlert();
            return false;
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.inLayout) {
                return;
            }
            super.requestLayout();
        }
    }

    /* loaded from: classes4.dex */
    public static class Builder {
        private AlertDialog alertDialog;
        private final boolean[] red;

        public Builder(Context context) {
            this(context, null);
        }

        public Builder(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
            this.red = new boolean[3];
            this.alertDialog = createAlertDialog(context, i, resourcesProvider);
        }

        public Builder(Context context, Theme.ResourcesProvider resourcesProvider) {
            this(context, 0, resourcesProvider);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public Builder(AlertDialog alertDialog) {
            this.red = new boolean[3];
            this.alertDialog = alertDialog;
        }

        public Builder aboveMessageView(View view) {
            this.alertDialog.aboveMessageView = view;
            return this;
        }

        public Builder addBottomView(View view) {
            this.alertDialog.bottomView = view;
            return this;
        }

        public AlertDialog create() {
            return this.alertDialog;
        }

        protected AlertDialog createAlertDialog(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
            return new AlertDialog(context, i, resourcesProvider);
        }

        public Context getContext() {
            return this.alertDialog.getContext();
        }

        public Runnable getDismissRunnable() {
            return this.alertDialog.dismissRunnable;
        }

        public Builder makeCustomMaxHeight() {
            this.alertDialog.customMaxHeight = true;
            return this;
        }

        public Builder makeRed(int i) {
            int i2 = (-i) - 1;
            if (i2 >= 0) {
                boolean[] zArr = this.red;
                if (i2 < zArr.length) {
                    zArr[i2] = true;
                }
            }
            return this;
        }

        public void notDrawBackgroundOnTopView(boolean z) {
            this.alertDialog.notDrawBackgroundOnTopView = z;
            this.alertDialog.blurredBackground = false;
        }

        public Builder overrideDismissListener(Utilities.Callback callback) {
            this.alertDialog.overridenDissmissListener = callback;
            return this;
        }

        public Builder setAdditionalHorizontalPadding(int i) {
            this.alertDialog.additioanalHorizontalPadding = i;
            return this;
        }

        public void setButtonsVertical(boolean z) {
            this.alertDialog.verticalButtons = z;
        }

        public Builder setCheckFocusable(boolean z) {
            this.alertDialog.checkFocusable = z;
            return this;
        }

        public Builder setCustomViewOffset(int i) {
            this.alertDialog.customViewOffset = i;
            return this;
        }

        public Builder setDialogButtonColorKey(int i) {
            this.alertDialog.dialogButtonColorKey = i;
            return this;
        }

        public Builder setDimAlpha(float f) {
            this.alertDialog.dimAlpha = f;
            return this;
        }

        public Builder setDimEnabled(boolean z) {
            this.alertDialog.dimEnabled = z;
            return this;
        }

        public Builder setItems(CharSequence[] charSequenceArr, DialogInterface.OnClickListener onClickListener) {
            this.alertDialog.items = charSequenceArr;
            this.alertDialog.onClickListener = onClickListener;
            return this;
        }

        public Builder setItems(CharSequence[] charSequenceArr, int[] iArr, DialogInterface.OnClickListener onClickListener) {
            this.alertDialog.items = charSequenceArr;
            this.alertDialog.itemIcons = iArr;
            this.alertDialog.onClickListener = onClickListener;
            return this;
        }

        public Builder setMessage(CharSequence charSequence) {
            this.alertDialog.message = charSequence;
            return this;
        }

        public Builder setMessageTextViewClickable(boolean z) {
            this.alertDialog.messageTextViewClickable = z;
            return this;
        }

        public Builder setNegativeButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            this.alertDialog.negativeButtonText = charSequence;
            this.alertDialog.negativeButtonListener = onClickListener;
            return this;
        }

        public Builder setNeutralButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            this.alertDialog.neutralButtonText = charSequence;
            this.alertDialog.neutralButtonListener = onClickListener;
            return this;
        }

        public Builder setOnBackButtonListener(DialogInterface.OnClickListener onClickListener) {
            this.alertDialog.onBackButtonListener = onClickListener;
            return this;
        }

        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            this.alertDialog.setOnCancelListener(onCancelListener);
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.alertDialog.setOnDismissListener(onDismissListener);
            return this;
        }

        public Builder setOnPreDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.alertDialog.onDismissListener = onDismissListener;
            return this;
        }

        public Builder setPositiveButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            this.alertDialog.positiveButtonText = charSequence;
            this.alertDialog.positiveButtonListener = onClickListener;
            return this;
        }

        public Builder setSubtitle(CharSequence charSequence) {
            this.alertDialog.subtitle = charSequence;
            return this;
        }

        public Builder setTitle(CharSequence charSequence) {
            this.alertDialog.title = charSequence;
            return this;
        }

        public Builder setTopAnimation(int i, int i2, boolean z, int i3) {
            return setTopAnimation(i, i2, z, i3, null);
        }

        public Builder setTopAnimation(int i, int i2, boolean z, int i3, Map map) {
            this.alertDialog.topAnimationId = i;
            this.alertDialog.topAnimationSize = i2;
            this.alertDialog.topAnimationAutoRepeat = z;
            this.alertDialog.topBackgroundColor = i3;
            this.alertDialog.topAnimationLayerColors = map;
            return this;
        }

        public Builder setTopAnimationIsNew(boolean z) {
            this.alertDialog.topAnimationIsNew = z;
            return this;
        }

        public Builder setTopImage(Drawable drawable, int i) {
            this.alertDialog.topDrawable = drawable;
            this.alertDialog.topBackgroundColor = i;
            return this;
        }

        public Builder setTopView(View view) {
            this.alertDialog.topView = view;
            return this;
        }

        public void setTopViewAspectRatio(float f) {
            this.alertDialog.aspectRatio = f;
        }

        public Builder setView(View view) {
            return setView(view, -2);
        }

        public Builder setView(View view, int i) {
            this.alertDialog.customView = view;
            this.alertDialog.customViewHeight = i;
            return this;
        }

        public Builder setWidth(int i) {
            this.alertDialog.customWidth = i;
            return this;
        }

        public AlertDialog show() {
            TextView textView;
            this.alertDialog.show();
            int i = 0;
            while (true) {
                boolean[] zArr = this.red;
                if (i >= zArr.length) {
                    return this.alertDialog;
                }
                if (zArr[i] && (textView = (TextView) this.alertDialog.getButton(-(i + 1))) != null) {
                    textView.setTextColor(this.alertDialog.getThemedColor(Theme.key_text_RedBold));
                }
                i++;
            }
        }
    }

    public AlertDialog(Context context, int i) {
        this(context, i, null);
    }

    public AlertDialog(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
        super(context, R.style.TransparentDialog);
        this.customWidth = -1;
        this.customViewHeight = -2;
        this.shadow = new BitmapDrawable[2];
        this.shadowVisibility = new boolean[2];
        this.shadowAnimation = new AnimatorSet[2];
        this.customViewOffset = 12;
        this.dialogButtonColorKey = Theme.key_dialogButton;
        this.topHeight = NotificationCenter.httpFileDidFailedLoad;
        this.messageTextViewClickable = true;
        this.canCacnel = true;
        this.dismissDialogByButtons = true;
        this.containerViewLocation = new int[2];
        this.checkFocusable = true;
        this.dismissRunnable = new BillingController$$ExternalSyntheticLambda10(this);
        this.showRunnable = new Runnable() { // from class: org.telegram.ui.ActionBar.AlertDialog$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                AlertDialog.this.lambda$new$0();
            }
        };
        this.itemViews = new ArrayList();
        this.dimEnabled = true;
        this.dimAlpha = 0.5f;
        this.dimCustom = false;
        this.topAnimationAutoRepeat = true;
        float f = 0.8f;
        this.blurAlpha = 0.8f;
        this.resourcesProvider = resourcesProvider;
        int themedColor = getThemedColor(Theme.key_dialogBackground);
        this.backgroundColor = themedColor;
        boolean z = AndroidUtilities.computePerceivedBrightness(themedColor) < 0.721f;
        boolean z2 = supportsNativeBlur() && this.progressViewStyle == 0;
        this.blurredNativeBackground = z2;
        this.blurredBackground = (z2 || (!supportsNativeBlur() && SharedConfig.getDevicePerformanceClass() >= 2 && LiteMode.isEnabled(256))) && z;
        this.backgroundPaddings = new Rect();
        if (i != 3 || this.blurredBackground) {
            Drawable mutate = context.getResources().getDrawable(R.drawable.popup_fixed_alert3).mutate();
            this.shadowDrawable = mutate;
            if (i == 3) {
                f = 0.55f;
            } else if (!z) {
                f = 0.985f;
            }
            this.blurOpacity = f;
            mutate.setColorFilter(new PorterDuffColorFilter(this.backgroundColor, PorterDuff.Mode.MULTIPLY));
            this.shadowDrawable.getPadding(this.backgroundPaddings);
        }
        this.progressViewStyle = i;
    }

    private boolean canTextInput(View view) {
        if (view.onCheckIsTextEditor()) {
            return true;
        }
        if (!(view instanceof ViewGroup)) {
            return false;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        while (childCount > 0) {
            childCount--;
            if (canTextInput(viewGroup.getChildAt(childCount))) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$inflateContent$1(View view) {
        DialogInterface.OnClickListener onClickListener = this.onClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this, ((Integer) view.getTag()).intValue());
        }
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$inflateContent$2(View view) {
        DialogInterface.OnClickListener onClickListener = this.positiveButtonListener;
        if (onClickListener != null) {
            onClickListener.onClick(this, -1);
        }
        if (this.dismissDialogByButtons) {
            dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$inflateContent$3(View view) {
        DialogInterface.OnClickListener onClickListener = this.negativeButtonListener;
        if (onClickListener != null) {
            onClickListener.onClick(this, -2);
        }
        if (this.dismissDialogByButtons) {
            cancel();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$inflateContent$4(View view) {
        DialogInterface.OnClickListener onClickListener = this.neutralButtonListener;
        if (onClickListener != null) {
            onClickListener.onClick(this, -2);
        }
        if (this.dismissDialogByButtons) {
            dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$inflateContent$5(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        if (this.blurPaint == null) {
            this.blurPaint = new Paint(1);
        }
        this.blurBitmap = bitmap;
        Bitmap bitmap2 = this.blurBitmap;
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(bitmap2, tileMode, tileMode);
        this.blurShader = bitmapShader;
        this.blurPaint.setShader(bitmapShader);
        Matrix matrix = new Matrix();
        this.blurMatrix = matrix;
        matrix.postScale(8.0f, 8.0f);
        Matrix matrix2 = this.blurMatrix;
        int[] iArr = this.containerViewLocation;
        matrix2.postTranslate(-iArr[0], -iArr[1]);
        this.blurShader.setLocalMatrix(this.blurMatrix);
        this.containerView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (isShowing()) {
            return;
        }
        try {
            show();
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showCancelAlert$6(DialogInterface dialogInterface, int i) {
        DialogInterface.OnCancelListener onCancelListener = this.onCancelListener;
        if (onCancelListener != null) {
            onCancelListener.onCancel(this);
        }
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showCancelAlert$7(DialogInterface dialogInterface) {
        this.cancelDialog = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runShadowAnimation(final int i, boolean z) {
        if ((!z || this.shadowVisibility[i]) && (z || !this.shadowVisibility[i])) {
            return;
        }
        this.shadowVisibility[i] = z;
        AnimatorSet animatorSet = this.shadowAnimation[i];
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        this.shadowAnimation[i] = new AnimatorSet();
        BitmapDrawable bitmapDrawable = this.shadow[i];
        if (bitmapDrawable != null) {
            this.shadowAnimation[i].playTogether(ObjectAnimator.ofInt(bitmapDrawable, "alpha", z ? NotificationCenter.closeSearchByActiveAction : 0));
        }
        this.shadowAnimation[i].setDuration(150L);
        this.shadowAnimation[i].addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.AlertDialog.7
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                if (AlertDialog.this.shadowAnimation[i] == null || !AlertDialog.this.shadowAnimation[i].equals(animator)) {
                    return;
                }
                AlertDialog.this.shadowAnimation[i] = null;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (AlertDialog.this.shadowAnimation[i] == null || !AlertDialog.this.shadowAnimation[i].equals(animator)) {
                    return;
                }
                AlertDialog.this.shadowAnimation[i] = null;
            }
        });
        try {
            this.shadowAnimation[i].start();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showCancelAlert() {
        if (this.canCacnel && this.cancelDialog == null) {
            Builder builder = new Builder(getContext(), this.resourcesProvider);
            builder.setTitle(LocaleController.getString(R.string.StopLoadingTitle));
            builder.setMessage(LocaleController.getString(R.string.StopLoading));
            builder.setPositiveButton(LocaleController.getString(R.string.WaitMore), null);
            builder.setNegativeButton(LocaleController.getString(R.string.Stop), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ActionBar.AlertDialog$$ExternalSyntheticLambda9
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    AlertDialog.this.lambda$showCancelAlert$6(dialogInterface, i);
                }
            });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.ActionBar.AlertDialog$$ExternalSyntheticLambda10
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    AlertDialog.this.lambda$showCancelAlert$7(dialogInterface);
                }
            });
            try {
                this.cancelDialog = builder.show();
            } catch (Exception unused) {
            }
        }
    }

    private void updateLineProgressTextView() {
        this.lineProgressViewPercent.setText(String.format("%d%%", Integer.valueOf(this.currentProgress)));
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        TextView textView;
        if (i != NotificationCenter.emojiLoaded || (textView = this.messageTextView) == null) {
            return;
        }
        textView.invalidate();
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        Bitmap bitmap;
        Utilities.Callback callback = this.overridenDissmissListener;
        if (callback != null) {
            this.overridenDissmissListener = null;
            callback.run(new BillingController$$ExternalSyntheticLambda10(this));
            return;
        }
        if (this.dismissed) {
            return;
        }
        this.dismissed = true;
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        DialogInterface.OnDismissListener onDismissListener = this.onDismissListener;
        if (onDismissListener != null) {
            onDismissListener.onDismiss(this);
        }
        AlertDialog alertDialog = this.cancelDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        try {
            super.dismiss();
        } catch (Throwable unused) {
        }
        AndroidUtilities.cancelRunOnUIThread(this.showRunnable);
        if (this.blurShader == null || (bitmap = this.blurBitmap) == null) {
            return;
        }
        bitmap.recycle();
        this.blurShader = null;
        this.blurPaint = null;
        this.blurBitmap = null;
    }

    public void dismissUnless(long j) {
        long currentTimeMillis = System.currentTimeMillis() - this.shownAt;
        if (currentTimeMillis < j) {
            AndroidUtilities.runOnUIThread(new BillingController$$ExternalSyntheticLambda10(this), currentTimeMillis - j);
        } else {
            dismiss();
        }
    }

    public View getButton(int i) {
        ViewGroup viewGroup = this.buttonsLayout;
        if (viewGroup != null) {
            return viewGroup.findViewWithTag(Integer.valueOf(i));
        }
        return null;
    }

    public ViewGroup getButtonsLayout() {
        return this.buttonsLayout;
    }

    public AlertDialogView getContainerView() {
        return this.containerView;
    }

    public int getItemsCount() {
        return this.itemViews.size();
    }

    public ArrayList getThemeDescriptions() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:100:0x03fb  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x040d  */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x059c  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x05b2  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x05fe  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0624  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x08bc  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x0933  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x093a  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x08bf  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x05a9  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x04ca  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x03fd  */
    /* JADX WARN: Removed duplicated region for block: B:289:0x03c4  */
    /* JADX WARN: Removed duplicated region for block: B:292:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x014d  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x0183  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x018e  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x0170  */
    /* JADX WARN: Removed duplicated region for block: B:302:0x00f1  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x01b7  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x028d  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x029d  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x029f  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x028f  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x02bb  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0317  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x03c1  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x03eb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View inflateContent(boolean z) {
        boolean z2;
        Drawable drawable;
        int i;
        ViewGroup.LayoutParams createLinear;
        ViewGroup viewGroup;
        View view;
        View view2;
        WindowManager.LayoutParams layoutParams;
        int i2;
        View view3;
        FrameLayout frameLayout;
        AlertDialogView alertDialogView = new AlertDialogView(getContext());
        this.containerView = alertDialogView;
        alertDialogView.setOrientation(1);
        if ((this.blurredBackground || this.progressViewStyle == 3) && this.progressViewStyle != 2) {
            this.containerView.setBackgroundDrawable(null);
            this.containerView.setPadding(0, 0, 0, 0);
            if (this.blurredBackground && !this.blurredNativeBackground) {
                this.containerView.setWillNotDraw(false);
            }
        } else {
            if (this.notDrawBackgroundOnTopView) {
                Rect rect = new Rect();
                this.shadowDrawable.getPadding(rect);
                this.containerView.setPadding(rect.left, rect.top, rect.right, rect.bottom);
                this.drawBackground = true;
                this.containerView.setFitsSystemWindows(Build.VERSION.SDK_INT < 21);
                if (z) {
                    if (this.customWidth > 0) {
                        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-2, -2);
                        layoutParams2.gravity = 17;
                        setContentView(this.containerView, layoutParams2);
                    } else {
                        setContentView(this.containerView);
                    }
                }
                z2 = (this.positiveButtonText != null && this.negativeButtonText == null && this.neutralButtonText == null) ? false : true;
                if (this.topResId != 0 && this.topAnimationId == 0 && this.topDrawable == null) {
                    View view4 = this.topView;
                    if (view4 != null) {
                        view4.setPadding(0, 0, 0, 0);
                        this.containerView.addView(this.topView, LayoutHelper.createLinear(-1, this.topHeight, 51, 0, 0, 0, 0));
                    }
                } else {
                    RLottieImageView rLottieImageView = new RLottieImageView(getContext());
                    this.topImageView = rLottieImageView;
                    drawable = this.topDrawable;
                    if (drawable == null) {
                        rLottieImageView.setImageDrawable(drawable);
                    } else {
                        int i3 = this.topResId;
                        if (i3 != 0) {
                            rLottieImageView.setImageResource(i3);
                        } else {
                            rLottieImageView.setAutoRepeat(this.topAnimationAutoRepeat);
                            RLottieImageView rLottieImageView2 = this.topImageView;
                            int i4 = this.topAnimationId;
                            int i5 = this.topAnimationSize;
                            rLottieImageView2.setAnimation(i4, i5, i5);
                            if (this.topAnimationLayerColors != null) {
                                RLottieDrawable animatedDrawable = this.topImageView.getAnimatedDrawable();
                                for (Map.Entry entry : this.topAnimationLayerColors.entrySet()) {
                                    animatedDrawable.setLayerColor((String) entry.getKey(), ((Integer) entry.getValue()).intValue());
                                }
                            }
                            this.topImageView.playAnimation();
                        }
                    }
                    this.topImageView.setScaleType(ImageView.ScaleType.CENTER);
                    if (this.topAnimationIsNew) {
                        this.topImageView.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(10.0f), 0, this.topBackgroundColor));
                    } else {
                        final GradientDrawable gradientDrawable = new GradientDrawable();
                        gradientDrawable.setColor(this.topBackgroundColor);
                        gradientDrawable.setCornerRadius(AndroidUtilities.dp(128.0f));
                        this.topImageView.setBackground(new Drawable() { // from class: org.telegram.ui.ActionBar.AlertDialog.1
                            int size;

                            {
                                this.size = AlertDialog.this.topAnimationSize + AndroidUtilities.dp(52.0f);
                            }

                            @Override // android.graphics.drawable.Drawable
                            public void draw(Canvas canvas) {
                                gradientDrawable.setBounds((int) ((AlertDialog.this.topImageView.getWidth() - this.size) / 2.0f), (int) ((AlertDialog.this.topImageView.getHeight() - this.size) / 2.0f), (int) ((AlertDialog.this.topImageView.getWidth() + this.size) / 2.0f), (int) ((AlertDialog.this.topImageView.getHeight() + this.size) / 2.0f));
                                gradientDrawable.draw(canvas);
                            }

                            @Override // android.graphics.drawable.Drawable
                            public int getOpacity() {
                                return gradientDrawable.getOpacity();
                            }

                            @Override // android.graphics.drawable.Drawable
                            public void setAlpha(int i6) {
                                gradientDrawable.setAlpha(i6);
                            }

                            @Override // android.graphics.drawable.Drawable
                            public void setColorFilter(ColorFilter colorFilter) {
                                gradientDrawable.setColorFilter(colorFilter);
                            }
                        });
                        this.topHeight = 92;
                    }
                    if (this.topAnimationIsNew) {
                        this.topImageView.setTranslationY(0.0f);
                    } else {
                        this.topImageView.setTranslationY(AndroidUtilities.dp(16.0f));
                    }
                    this.topImageView.setPadding(0, 0, 0, 0);
                    this.containerView.addView(this.topImageView, LayoutHelper.createLinear(-1, this.topHeight, 51, 0, 0, 0, 0));
                }
                if (this.title != null) {
                    FrameLayout frameLayout2 = new FrameLayout(getContext());
                    this.titleContainer = frameLayout2;
                    this.containerView.addView(frameLayout2, LayoutHelper.createLinear(-2, -2, this.topAnimationIsNew ? 1 : 0, 24, 0, 24, 0));
                    SpoilersTextView spoilersTextView = new SpoilersTextView(getContext(), false);
                    this.titleTextView = spoilersTextView;
                    NotificationCenter.listenEmojiLoading(spoilersTextView);
                    SpoilersTextView spoilersTextView2 = this.titleTextView;
                    spoilersTextView2.cacheType = 3;
                    spoilersTextView2.setText(this.title);
                    this.titleTextView.setTextColor(getThemedColor(Theme.key_dialogTextBlack));
                    this.titleTextView.setTextSize(1, 20.0f);
                    this.titleTextView.setTypeface(AndroidUtilities.bold());
                    this.titleTextView.setGravity((this.topAnimationIsNew ? 1 : LocaleController.isRTL ? 5 : 3) | 48);
                    FrameLayout frameLayout3 = this.titleContainer;
                    SpoilersTextView spoilersTextView3 = this.titleTextView;
                    boolean z3 = this.topAnimationIsNew;
                    frameLayout3.addView(spoilersTextView3, LayoutHelper.createFrame(-2, -2.0f, (z3 ? 1 : LocaleController.isRTL ? 5 : 3) | 48, 0.0f, 19.0f, 0.0f, z3 ? 4.0f : this.subtitle != null ? 2 : this.items != null ? 14 : 10));
                }
                if (this.secondTitle != null && this.title != null) {
                    TextView textView = new TextView(getContext());
                    this.secondTitleTextView = textView;
                    textView.setText(this.secondTitle);
                    this.secondTitleTextView.setTextColor(getThemedColor(Theme.key_dialogTextGray3));
                    this.secondTitleTextView.setTextSize(1, 18.0f);
                    this.secondTitleTextView.setGravity((!LocaleController.isRTL ? 3 : 5) | 48);
                    this.titleContainer.addView(this.secondTitleTextView, LayoutHelper.createFrame(-2, -2.0f, (!LocaleController.isRTL ? 3 : 5) | 48, 0.0f, 21.0f, 0.0f, 0.0f));
                }
                if (this.subtitle != null) {
                    TextView textView2 = new TextView(getContext());
                    this.subtitleTextView = textView2;
                    textView2.setText(this.subtitle);
                    this.subtitleTextView.setTextColor(getThemedColor(Theme.key_dialogIcon));
                    this.subtitleTextView.setTextSize(1, 14.0f);
                    this.subtitleTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
                    this.containerView.addView(this.subtitleTextView, LayoutHelper.createLinear(-2, -2, (LocaleController.isRTL ? 5 : 3) | 48, 24, 0, 24, this.items != null ? 14 : 10));
                }
                if (this.progressViewStyle == 0) {
                    this.shadow[0] = (BitmapDrawable) getContext().getResources().getDrawable(R.drawable.header_shadow).mutate();
                    this.shadow[1] = (BitmapDrawable) getContext().getResources().getDrawable(R.drawable.header_shadow_reverse).mutate();
                    this.shadow[0].setAlpha(0);
                    this.shadow[1].setAlpha(0);
                    this.shadow[0].setCallback(this);
                    this.shadow[1].setCallback(this);
                    ScrollView scrollView = new ScrollView(getContext()) { // from class: org.telegram.ui.ActionBar.AlertDialog.2
                        @Override // android.view.ViewGroup
                        protected boolean drawChild(Canvas canvas, View view5, long j) {
                            boolean drawChild = super.drawChild(canvas, view5, j);
                            if (AlertDialog.this.shadow[0].getPaint().getAlpha() != 0) {
                                AlertDialog.this.shadow[0].setBounds(0, getScrollY(), getMeasuredWidth(), getScrollY() + AndroidUtilities.dp(3.0f));
                                AlertDialog.this.shadow[0].draw(canvas);
                            }
                            if (AlertDialog.this.shadow[1].getPaint().getAlpha() != 0) {
                                AlertDialog.this.shadow[1].setBounds(0, (getScrollY() + getMeasuredHeight()) - AndroidUtilities.dp(3.0f), getMeasuredWidth(), getScrollY() + getMeasuredHeight());
                                AlertDialog.this.shadow[1].draw(canvas);
                            }
                            return drawChild;
                        }
                    };
                    this.contentScrollView = scrollView;
                    scrollView.setVerticalScrollBarEnabled(false);
                    AndroidUtilities.setScrollViewEdgeEffectColor(this.contentScrollView, getThemedColor(Theme.key_dialogScrollGlow));
                    this.containerView.addView(this.contentScrollView, LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, 0.0f));
                    LinearLayout linearLayout = new LinearLayout(getContext());
                    this.scrollContainer = linearLayout;
                    linearLayout.setOrientation(1);
                    this.contentScrollView.addView(this.scrollContainer, new FrameLayout.LayoutParams(-1, -2));
                }
                EffectsTextView effectsTextView = new EffectsTextView(getContext());
                this.messageTextView = effectsTextView;
                NotificationCenter.listenEmojiLoading(effectsTextView);
                this.messageTextView.setTextColor(getThemedColor(!this.topAnimationIsNew ? Theme.key_windowBackgroundWhiteGrayText : Theme.key_dialogTextBlack));
                this.messageTextView.setTextSize(1, 16.0f);
                this.messageTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                this.messageTextView.setLinkTextColor(getThemedColor(Theme.key_dialogTextLink));
                if (!this.messageTextViewClickable) {
                    this.messageTextView.setClickable(false);
                    this.messageTextView.setEnabled(false);
                }
                this.messageTextView.setGravity((!this.topAnimationIsNew ? 1 : LocaleController.isRTL ? 5 : 3) | 48);
                i = this.progressViewStyle;
                if (i != 2) {
                    this.containerView.addView(this.messageTextView, LayoutHelper.createLinear(-2, -2, (LocaleController.isRTL ? 5 : 3) | 48, 24, this.title == null ? 19 : 0, 24, 20));
                    LineProgressView lineProgressView = new LineProgressView(getContext());
                    this.lineProgressView = lineProgressView;
                    lineProgressView.setProgress(this.currentProgress / 100.0f, false);
                    this.lineProgressView.setProgressColor(getThemedColor(Theme.key_dialogLineProgress));
                    this.lineProgressView.setBackColor(getThemedColor(Theme.key_dialogLineProgressBackground));
                    this.containerView.addView(this.lineProgressView, LayoutHelper.createLinear(-1, 4, 19, 24, 0, 24, 0));
                    TextView textView3 = new TextView(getContext());
                    this.lineProgressViewPercent = textView3;
                    textView3.setTypeface(AndroidUtilities.bold());
                    this.lineProgressViewPercent.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
                    this.lineProgressViewPercent.setTextColor(getThemedColor(Theme.key_dialogTextGray2));
                    this.lineProgressViewPercent.setTextSize(1, 14.0f);
                    this.containerView.addView(this.lineProgressViewPercent, LayoutHelper.createLinear(-2, -2, (LocaleController.isRTL ? 5 : 3) | 48, 23, 4, 23, 24));
                    updateLineProgressTextView();
                } else {
                    if (i == 3) {
                        setCanceledOnTouchOutside(false);
                        setCancelable(false);
                        this.progressViewContainer = new FrameLayout(getContext());
                        this.backgroundColor = getThemedColor(Theme.key_dialog_inlineProgressBackground);
                        if (!this.blurredBackground || this.blurredNativeBackground) {
                            this.progressViewContainer.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0f), this.backgroundColor));
                        }
                        this.containerView.addView(this.progressViewContainer, LayoutHelper.createLinear(86, 86, 17));
                        RadialProgressView radialProgressView = new RadialProgressView(getContext(), this.resourcesProvider);
                        radialProgressView.setSize(AndroidUtilities.dp(32.0f));
                        radialProgressView.setProgressColor(getThemedColor(Theme.key_dialog_inlineProgress));
                        ViewGroup viewGroup2 = this.progressViewContainer;
                        createLinear = LayoutHelper.createFrame(86, 86, 17);
                        view = radialProgressView;
                        viewGroup = viewGroup2;
                    } else {
                        View view5 = this.aboveMessageView;
                        if (view5 != null) {
                            this.scrollContainer.addView(view5, LayoutHelper.createLinear(-1, -2, 22.0f, 4.0f, 22.0f, 12.0f));
                        }
                        this.scrollContainer.addView(this.messageTextView, LayoutHelper.createLinear(-2, -2, (this.topAnimationIsNew ? 1 : LocaleController.isRTL ? 5 : 3) | 48, 24, 0, 24, (this.customView == null && this.items == null) ? 0 : this.customViewOffset));
                        View view6 = this.bottomView;
                        if (view6 != null) {
                            ViewGroup viewGroup3 = this.scrollContainer;
                            createLinear = LayoutHelper.createLinear(-1, -2, 22.0f, 12.0f, 22.0f, 0.0f);
                            view = view6;
                            viewGroup = viewGroup3;
                        }
                    }
                    viewGroup.addView(view, createLinear);
                }
                if (TextUtils.isEmpty(this.message)) {
                    this.messageTextView.setText(this.message);
                    this.messageTextView.setVisibility(0);
                } else {
                    this.messageTextView.setVisibility(8);
                }
                if (this.items != null) {
                    int i6 = 0;
                    while (true) {
                        CharSequence[] charSequenceArr = this.items;
                        if (i6 >= charSequenceArr.length) {
                            break;
                        }
                        if (charSequenceArr[i6] != null) {
                            AlertDialogCell alertDialogCell = new AlertDialogCell(getContext(), this.resourcesProvider);
                            CharSequence charSequence = this.items[i6];
                            int[] iArr = this.itemIcons;
                            alertDialogCell.setTextAndIcon(charSequence, iArr != null ? iArr[i6] : 0);
                            alertDialogCell.setTag(Integer.valueOf(i6));
                            this.itemViews.add(alertDialogCell);
                            this.scrollContainer.addView(alertDialogCell, LayoutHelper.createLinear(-1, 50));
                            alertDialogCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ActionBar.AlertDialog$$ExternalSyntheticLambda3
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view7) {
                                    AlertDialog.this.lambda$inflateContent$1(view7);
                                }
                            });
                        }
                        i6++;
                    }
                }
                view2 = this.customView;
                if (view2 != null) {
                    if (view2.getParent() != null) {
                        ((ViewGroup) this.customView.getParent()).removeView(this.customView);
                    }
                    this.scrollContainer.addView(this.customView, LayoutHelper.createLinear(-1, this.customViewHeight));
                }
                if (z2) {
                    if (!this.verticalButtons) {
                        TextPaint textPaint = new TextPaint();
                        textPaint.setTextSize(AndroidUtilities.dp(14.0f));
                        CharSequence charSequence2 = this.positiveButtonText;
                        int measureText = charSequence2 != null ? (int) (0 + textPaint.measureText(charSequence2, 0, charSequence2.length()) + AndroidUtilities.dp(10.0f)) : 0;
                        CharSequence charSequence3 = this.negativeButtonText;
                        if (charSequence3 != null) {
                            measureText = (int) (measureText + textPaint.measureText(charSequence3, 0, charSequence3.length()) + AndroidUtilities.dp(10.0f));
                        }
                        CharSequence charSequence4 = this.neutralButtonText;
                        if (charSequence4 != null) {
                            measureText = (int) (measureText + textPaint.measureText(charSequence4, 0, charSequence4.length()) + AndroidUtilities.dp(10.0f));
                        }
                        if (measureText > AndroidUtilities.displaySize.x - AndroidUtilities.dp(110.0f)) {
                            this.verticalButtons = true;
                        }
                    }
                    if (this.verticalButtons) {
                        LinearLayout linearLayout2 = new LinearLayout(getContext());
                        linearLayout2.setOrientation(1);
                        frameLayout = linearLayout2;
                    } else {
                        frameLayout = new FrameLayout(getContext()) { // from class: org.telegram.ui.ActionBar.AlertDialog.3
                            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
                            protected void onLayout(boolean z4, int i7, int i8, int i9, int i10) {
                                int i11;
                                int i12;
                                int paddingLeft;
                                int paddingTop;
                                int paddingLeft2;
                                int paddingRight;
                                int paddingTop2;
                                int paddingRight2;
                                int childCount = getChildCount();
                                int i13 = i9 - i7;
                                View view7 = null;
                                for (int i14 = 0; i14 < childCount; i14++) {
                                    View childAt = getChildAt(i14);
                                    Integer num = (Integer) childAt.getTag();
                                    if (num == null) {
                                        int measuredWidth = childAt.getMeasuredWidth();
                                        int measuredHeight = childAt.getMeasuredHeight();
                                        if (view7 != null) {
                                            i11 = view7.getLeft() + ((view7.getMeasuredWidth() - measuredWidth) / 2);
                                            i12 = view7.getTop() + ((view7.getMeasuredHeight() - measuredHeight) / 2);
                                        } else {
                                            i11 = 0;
                                            i12 = 0;
                                        }
                                        childAt.layout(i11, i12, measuredWidth + i11, measuredHeight + i12);
                                    } else if (num.intValue() == -1) {
                                        if (LocaleController.isRTL) {
                                            paddingRight = getPaddingLeft();
                                            paddingTop2 = getPaddingTop();
                                            paddingRight2 = getPaddingLeft() + childAt.getMeasuredWidth();
                                        } else {
                                            paddingRight = (i13 - getPaddingRight()) - childAt.getMeasuredWidth();
                                            paddingTop2 = getPaddingTop();
                                            paddingRight2 = i13 - getPaddingRight();
                                        }
                                        childAt.layout(paddingRight, paddingTop2, paddingRight2, getPaddingTop() + childAt.getMeasuredHeight());
                                        view7 = childAt;
                                    } else {
                                        if (num.intValue() == -2) {
                                            if (LocaleController.isRTL) {
                                                paddingLeft = getPaddingLeft();
                                                if (view7 != null) {
                                                    paddingLeft += view7.getMeasuredWidth() + AndroidUtilities.dp(8.0f);
                                                }
                                            } else {
                                                paddingLeft = (i13 - getPaddingRight()) - childAt.getMeasuredWidth();
                                                if (view7 != null) {
                                                    paddingLeft -= view7.getMeasuredWidth() + AndroidUtilities.dp(8.0f);
                                                }
                                            }
                                            paddingTop = getPaddingTop();
                                            paddingLeft2 = childAt.getMeasuredWidth() + paddingLeft;
                                        } else if (num.intValue() == -3) {
                                            if (LocaleController.isRTL) {
                                                paddingLeft = (i13 - getPaddingRight()) - childAt.getMeasuredWidth();
                                                paddingTop = getPaddingTop();
                                                paddingLeft2 = i13 - getPaddingRight();
                                            } else {
                                                paddingLeft = getPaddingLeft();
                                                paddingTop = getPaddingTop();
                                                paddingLeft2 = getPaddingLeft() + childAt.getMeasuredWidth();
                                            }
                                        }
                                        childAt.layout(paddingLeft, paddingTop, paddingLeft2, getPaddingTop() + childAt.getMeasuredHeight());
                                    }
                                }
                            }

                            @Override // android.widget.FrameLayout, android.view.View
                            protected void onMeasure(int i7, int i8) {
                                super.onMeasure(i7, i8);
                                int measuredWidth = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
                                int childCount = getChildCount();
                                int i9 = 0;
                                for (int i10 = 0; i10 < childCount; i10++) {
                                    View childAt = getChildAt(i10);
                                    if ((childAt instanceof TextView) && childAt.getTag() != null) {
                                        i9 += childAt.getMeasuredWidth();
                                    }
                                }
                                if (i9 > measuredWidth) {
                                    View findViewWithTag = findViewWithTag(-2);
                                    View findViewWithTag2 = findViewWithTag(-3);
                                    if (findViewWithTag == null || findViewWithTag2 == null) {
                                        return;
                                    }
                                    if (findViewWithTag.getMeasuredWidth() < findViewWithTag2.getMeasuredWidth()) {
                                        findViewWithTag2.measure(View.MeasureSpec.makeMeasureSpec(findViewWithTag2.getMeasuredWidth() - (i9 - measuredWidth), 1073741824), View.MeasureSpec.makeMeasureSpec(findViewWithTag2.getMeasuredHeight(), 1073741824));
                                    } else {
                                        findViewWithTag.measure(View.MeasureSpec.makeMeasureSpec(findViewWithTag.getMeasuredWidth() - (i9 - measuredWidth), 1073741824), View.MeasureSpec.makeMeasureSpec(findViewWithTag.getMeasuredHeight(), 1073741824));
                                    }
                                }
                            }
                        };
                    }
                    this.buttonsLayout = frameLayout;
                    if (this.bottomView != null) {
                        this.buttonsLayout.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(4.0f));
                        this.buttonsLayout.setTranslationY(-AndroidUtilities.dp(6.0f));
                    } else {
                        this.buttonsLayout.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                    }
                    this.containerView.addView(this.buttonsLayout, LayoutHelper.createLinear(-1, 52));
                    if (this.topAnimationIsNew) {
                        this.buttonsLayout.setTranslationY(-AndroidUtilities.dp(8.0f));
                    }
                    if (this.positiveButtonText != null) {
                        TextView textView4 = new TextView(getContext()) { // from class: org.telegram.ui.ActionBar.AlertDialog.4
                            @Override // android.widget.TextView, android.view.View
                            public void setEnabled(boolean z4) {
                                super.setEnabled(z4);
                                setAlpha(z4 ? 1.0f : 0.5f);
                            }

                            @Override // android.widget.TextView
                            public void setTextColor(int i7) {
                                super.setTextColor(i7);
                                setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(AndroidUtilities.dp(6.0f), i7));
                            }
                        };
                        textView4.setMinWidth(AndroidUtilities.dp(64.0f));
                        textView4.setTag(-1);
                        textView4.setTextSize(1, 16.0f);
                        textView4.setTextColor(getThemedColor(this.dialogButtonColorKey));
                        textView4.setGravity(17);
                        textView4.setTypeface(AndroidUtilities.bold());
                        textView4.setText(this.positiveButtonText.toString());
                        textView4.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(AndroidUtilities.dp(6.0f), getThemedColor(this.dialogButtonColorKey)));
                        textView4.setPadding(AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f), 0);
                        if (this.verticalButtons) {
                            this.buttonsLayout.addView(textView4, LayoutHelper.createLinear(-2, 36, LocaleController.isRTL ? 3 : 5));
                        } else {
                            this.buttonsLayout.addView(textView4, LayoutHelper.createFrame(-2, 36, 53));
                        }
                        textView4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ActionBar.AlertDialog$$ExternalSyntheticLambda4
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view7) {
                                AlertDialog.this.lambda$inflateContent$2(view7);
                            }
                        });
                    }
                    if (this.negativeButtonText != null) {
                        TextView textView5 = new TextView(getContext()) { // from class: org.telegram.ui.ActionBar.AlertDialog.5
                            @Override // android.widget.TextView, android.view.View
                            public void setEnabled(boolean z4) {
                                super.setEnabled(z4);
                                setAlpha(z4 ? 1.0f : 0.5f);
                            }

                            @Override // android.widget.TextView
                            public void setTextColor(int i7) {
                                super.setTextColor(i7);
                                setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(AndroidUtilities.dp(6.0f), i7));
                            }
                        };
                        textView5.setMinWidth(AndroidUtilities.dp(64.0f));
                        textView5.setTag(-2);
                        textView5.setTextSize(1, 16.0f);
                        textView5.setTextColor(getThemedColor(this.dialogButtonColorKey));
                        textView5.setGravity(17);
                        textView5.setTypeface(AndroidUtilities.bold());
                        textView5.setEllipsize(TextUtils.TruncateAt.END);
                        textView5.setSingleLine(true);
                        textView5.setText(this.negativeButtonText.toString());
                        textView5.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(AndroidUtilities.dp(6.0f), getThemedColor(this.dialogButtonColorKey)));
                        textView5.setPadding(AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f), 0);
                        if (this.verticalButtons) {
                            this.buttonsLayout.addView(textView5, 0, LayoutHelper.createLinear(-2, 36, LocaleController.isRTL ? 3 : 5));
                        } else {
                            this.buttonsLayout.addView(textView5, LayoutHelper.createFrame(-2, 36, 53));
                        }
                        textView5.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ActionBar.AlertDialog$$ExternalSyntheticLambda5
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view7) {
                                AlertDialog.this.lambda$inflateContent$3(view7);
                            }
                        });
                    }
                    if (this.neutralButtonText != null) {
                        TextView textView6 = new TextView(getContext()) { // from class: org.telegram.ui.ActionBar.AlertDialog.6
                            @Override // android.widget.TextView, android.view.View
                            public void setEnabled(boolean z4) {
                                super.setEnabled(z4);
                                setAlpha(z4 ? 1.0f : 0.5f);
                            }

                            @Override // android.widget.TextView
                            public void setTextColor(int i7) {
                                super.setTextColor(i7);
                                setBackgroundDrawable(Theme.getRoundRectSelectorDrawable(AndroidUtilities.dp(6.0f), i7));
                            }
                        };
                        textView6.setMinWidth(AndroidUtilities.dp(64.0f));
                        textView6.setTag(-3);
                        textView6.setTextSize(1, 16.0f);
                        textView6.setTextColor(getThemedColor(this.dialogButtonColorKey));
                        textView6.setGravity(17);
                        textView6.setTypeface(AndroidUtilities.bold());
                        textView6.setEllipsize(TextUtils.TruncateAt.END);
                        textView6.setSingleLine(true);
                        textView6.setText(this.neutralButtonText.toString());
                        textView6.setBackground(Theme.getRoundRectSelectorDrawable(AndroidUtilities.dp(6.0f), getThemedColor(this.dialogButtonColorKey)));
                        textView6.setPadding(AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f), 0);
                        if (this.verticalButtons) {
                            this.buttonsLayout.addView(textView6, 1, LayoutHelper.createLinear(-2, 36, LocaleController.isRTL ? 3 : 5));
                        } else {
                            this.buttonsLayout.addView(textView6, LayoutHelper.createFrame(-2, 36, 51));
                        }
                        textView6.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ActionBar.AlertDialog$$ExternalSyntheticLambda6
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view7) {
                                AlertDialog.this.lambda$inflateContent$4(view7);
                            }
                        });
                    }
                    if (this.verticalButtons) {
                        for (int i7 = 1; i7 < this.buttonsLayout.getChildCount(); i7++) {
                            ((ViewGroup.MarginLayoutParams) this.buttonsLayout.getChildAt(i7).getLayoutParams()).topMargin = AndroidUtilities.dp(6.0f);
                        }
                    }
                }
                Window window = getWindow();
                layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(window.getAttributes());
                if (this.progressViewStyle != 3) {
                    layoutParams.width = -1;
                } else {
                    if (!this.dimEnabled || this.dimCustom) {
                        layoutParams.dimAmount = 0.0f;
                        i2 = layoutParams.flags ^ 2;
                    } else {
                        layoutParams.dimAmount = this.dimAlpha;
                        i2 = layoutParams.flags | 2;
                    }
                    layoutParams.flags = i2;
                    int i8 = AndroidUtilities.displaySize.x;
                    this.lastScreenWidth = i8;
                    int min = Math.min(AndroidUtilities.dp(AndroidUtilities.isTablet() ? AndroidUtilities.isSmallTablet() ? 446.0f : 496.0f : 356.0f), (i8 - AndroidUtilities.dp(48.0f)) - (this.additioanalHorizontalPadding * 2));
                    Rect rect2 = this.backgroundPaddings;
                    layoutParams.width = min + rect2.left + rect2.right;
                }
                view3 = this.customView;
                if (view3 == null && this.checkFocusable && canTextInput(view3)) {
                    layoutParams.softInputMode = 4;
                } else {
                    layoutParams.flags |= 131072;
                }
                if (Build.VERSION.SDK_INT >= 28) {
                    layoutParams.layoutInDisplayCutoutMode = 0;
                }
                if (this.blurredBackground) {
                    if (!supportsNativeBlur()) {
                        AndroidUtilities.makeGlobalBlurBitmap(new Utilities.Callback() { // from class: org.telegram.ui.ActionBar.AlertDialog$$ExternalSyntheticLambda7
                            @Override // org.telegram.messenger.Utilities.Callback
                            public final void run(Object obj) {
                                AlertDialog.this.lambda$inflateContent$5((Bitmap) obj);
                            }
                        }, 8.0f);
                    } else if (this.progressViewStyle == 0) {
                        this.blurredNativeBackground = true;
                        window.setBackgroundBlurRadius(50);
                        float dp = AndroidUtilities.dp(12.0f);
                        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{dp, dp, dp, dp, dp, dp, dp, dp}, null, null));
                        shapeDrawable.getPaint().setColor(ColorUtils.setAlphaComponent(this.backgroundColor, (int) (this.blurAlpha * 255.0f)));
                        window.setBackgroundDrawable(shapeDrawable);
                        if (this.blurBehind) {
                            layoutParams.flags |= 4;
                            layoutParams.setBlurBehindRadius(20);
                        }
                    }
                }
                window.setAttributes(layoutParams);
                return this.containerView;
            }
            this.containerView.setBackgroundDrawable(null);
            this.containerView.setPadding(0, 0, 0, 0);
            this.containerView.setBackgroundDrawable(this.shadowDrawable);
        }
        this.drawBackground = false;
        this.containerView.setFitsSystemWindows(Build.VERSION.SDK_INT < 21);
        if (z) {
        }
        if (this.positiveButtonText != null) {
        }
        if (this.topResId != 0) {
        }
        RLottieImageView rLottieImageView3 = new RLottieImageView(getContext());
        this.topImageView = rLottieImageView3;
        drawable = this.topDrawable;
        if (drawable == null) {
        }
        this.topImageView.setScaleType(ImageView.ScaleType.CENTER);
        if (this.topAnimationIsNew) {
        }
        if (this.topAnimationIsNew) {
        }
        this.topImageView.setPadding(0, 0, 0, 0);
        this.containerView.addView(this.topImageView, LayoutHelper.createLinear(-1, this.topHeight, 51, 0, 0, 0, 0));
        if (this.title != null) {
        }
        if (this.secondTitle != null) {
            TextView textView7 = new TextView(getContext());
            this.secondTitleTextView = textView7;
            textView7.setText(this.secondTitle);
            this.secondTitleTextView.setTextColor(getThemedColor(Theme.key_dialogTextGray3));
            this.secondTitleTextView.setTextSize(1, 18.0f);
            this.secondTitleTextView.setGravity((!LocaleController.isRTL ? 3 : 5) | 48);
            this.titleContainer.addView(this.secondTitleTextView, LayoutHelper.createFrame(-2, -2.0f, (!LocaleController.isRTL ? 3 : 5) | 48, 0.0f, 21.0f, 0.0f, 0.0f));
        }
        if (this.subtitle != null) {
        }
        if (this.progressViewStyle == 0) {
        }
        EffectsTextView effectsTextView2 = new EffectsTextView(getContext());
        this.messageTextView = effectsTextView2;
        NotificationCenter.listenEmojiLoading(effectsTextView2);
        this.messageTextView.setTextColor(getThemedColor(!this.topAnimationIsNew ? Theme.key_windowBackgroundWhiteGrayText : Theme.key_dialogTextBlack));
        this.messageTextView.setTextSize(1, 16.0f);
        this.messageTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
        this.messageTextView.setLinkTextColor(getThemedColor(Theme.key_dialogTextLink));
        if (!this.messageTextViewClickable) {
        }
        this.messageTextView.setGravity((!this.topAnimationIsNew ? 1 : LocaleController.isRTL ? 5 : 3) | 48);
        i = this.progressViewStyle;
        if (i != 2) {
        }
        if (TextUtils.isEmpty(this.message)) {
        }
        if (this.items != null) {
        }
        view2 = this.customView;
        if (view2 != null) {
        }
        if (z2) {
        }
        Window window2 = getWindow();
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(window2.getAttributes());
        if (this.progressViewStyle != 3) {
        }
        view3 = this.customView;
        if (view3 == null) {
        }
        layoutParams.flags |= 131072;
        if (Build.VERSION.SDK_INT >= 28) {
        }
        if (this.blurredBackground) {
        }
        window2.setAttributes(layoutParams);
        return this.containerView;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        this.contentScrollView.invalidate();
        this.scrollContainer.invalidate();
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        super.onBackPressed();
        DialogInterface.OnClickListener onClickListener = this.onBackButtonListener;
        if (onClickListener != null) {
            onClickListener.onClick(this, -2);
        }
    }

    @Override // android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        inflateContent(true);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
    }

    public void redPositive() {
        TextView textView = (TextView) getButton(-1);
        if (textView != null) {
            textView.setTextColor(getThemedColor(Theme.key_text_RedBold));
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
        ScrollView scrollView = this.contentScrollView;
        if (scrollView != null) {
            scrollView.postDelayed(runnable, j);
        }
    }

    public void setBackgroundColor(int i) {
        this.backgroundColor = i;
        Drawable drawable = this.shadowDrawable;
        if (drawable != null) {
            drawable.setColorFilter(new PorterDuffColorFilter(this.backgroundColor, PorterDuff.Mode.MULTIPLY));
        }
    }

    public void setBlurParams(float f, boolean z, boolean z2) {
        this.blurAlpha = f;
        this.blurBehind = z;
        this.blurredBackground = z2;
    }

    public void setCanCancel(boolean z) {
        this.canCacnel = z;
    }

    @Override // android.app.Dialog
    public void setCanceledOnTouchOutside(boolean z) {
        super.setCanceledOnTouchOutside(z);
    }

    public void setDismissDialogByButtons(boolean z) {
        this.dismissDialogByButtons = z;
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

    public void setItemColor(int i, int i2, int i3) {
        if (i < 0 || i >= this.itemViews.size()) {
            return;
        }
        AlertDialogCell alertDialogCell = (AlertDialogCell) this.itemViews.get(i);
        alertDialogCell.textView.setTextColor(i2);
        alertDialogCell.imageView.setColorFilter(new PorterDuffColorFilter(i3, PorterDuff.Mode.MULTIPLY));
    }

    public void setMessage(CharSequence charSequence) {
        TextView textView;
        int i;
        this.message = charSequence;
        if (this.messageTextView != null) {
            if (TextUtils.isEmpty(charSequence)) {
                textView = this.messageTextView;
                i = 8;
            } else {
                this.messageTextView.setText(this.message);
                textView = this.messageTextView;
                i = 0;
            }
            textView.setVisibility(i);
        }
    }

    public void setMessageLineSpacing(float f) {
        TextView textView = this.messageTextView;
        if (textView != null) {
            textView.setLineSpacing(AndroidUtilities.dp(f), 1.0f);
        }
    }

    public void setNegativeButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        this.negativeButtonText = charSequence;
        this.negativeButtonListener = onClickListener;
    }

    public void setNeutralButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        this.neutralButtonText = charSequence;
        this.neutralButtonListener = onClickListener;
    }

    @Override // android.app.Dialog
    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
        super.setOnCancelListener(onCancelListener);
    }

    public void setPositiveButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        this.positiveButtonText = charSequence;
        this.positiveButtonListener = onClickListener;
    }

    public void setPositiveButtonListener(DialogInterface.OnClickListener onClickListener) {
        this.positiveButtonListener = onClickListener;
    }

    public void setProgress(int i) {
        this.currentProgress = i;
        LineProgressView lineProgressView = this.lineProgressView;
        if (lineProgressView != null) {
            lineProgressView.setProgress(i / 100.0f, true);
            updateLineProgressTextView();
        }
    }

    public void setTextColor(int i) {
        SpoilersTextView spoilersTextView = this.titleTextView;
        if (spoilersTextView != null) {
            spoilersTextView.setTextColor(i);
        }
        TextView textView = this.messageTextView;
        if (textView != null) {
            textView.setTextColor(i);
        }
    }

    public void setTextSize(int i, int i2) {
        SpoilersTextView spoilersTextView = this.titleTextView;
        if (spoilersTextView != null) {
            spoilersTextView.setTextSize(1, i);
        }
        TextView textView = this.messageTextView;
        if (textView != null) {
            textView.setTextSize(1, i2);
        }
    }

    @Override // android.app.Dialog
    public void setTitle(CharSequence charSequence) {
        this.title = charSequence;
        SpoilersTextView spoilersTextView = this.titleTextView;
        if (spoilersTextView != null) {
            spoilersTextView.setText(charSequence);
        }
    }

    @Override // android.app.Dialog
    public void show() {
        if (AndroidUtilities.isSafeToShow(getContext())) {
            this.dismissed = false;
            super.show();
            FrameLayout frameLayout = this.progressViewContainer;
            if (frameLayout != null && this.progressViewStyle == 3) {
                frameLayout.setScaleX(0.0f);
                this.progressViewContainer.setScaleY(0.0f);
                this.progressViewContainer.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(new OvershootInterpolator(1.3f)).setDuration(190L).start();
            }
            this.shownAt = System.currentTimeMillis();
        }
    }

    public void showDelayed(long j) {
        AndroidUtilities.cancelRunOnUIThread(this.showRunnable);
        AndroidUtilities.runOnUIThread(this.showRunnable, j);
    }

    protected boolean supportsNativeBlur() {
        return false;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        ScrollView scrollView = this.contentScrollView;
        if (scrollView != null) {
            scrollView.removeCallbacks(runnable);
        }
    }
}
