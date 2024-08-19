package org.telegram.ui.Components.Premium;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.math.MathUtils;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.tl.TL_stories$TL_premium_boostsStatus;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EmptyStubSpan;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.LimitPreviewView;
import org.telegram.ui.Components.Premium.PremiumGradient;
/* loaded from: classes3.dex */
public class LimitPreviewView extends LinearLayout {
    private boolean animateIncrease;
    private int animateIncreaseWidth;
    private boolean animatingRotation;
    boolean animationCanPlay;
    private ValueAnimator arrowAnimator;
    private int currentValue;
    private DarkGradientProvider darkGradientProvider;
    TextView defaultCount;
    private final TextView defaultText;
    public int gradientTotalHeight;
    int gradientYOffset;
    int icon;
    boolean inc;
    public boolean invalidationEnabled;
    private boolean isBoostsStyle;
    private boolean isSimpleStyle;
    public boolean isStatistic;
    CounterView limitIcon;
    float limitIconRotation;
    FrameLayout limitsContainer;
    private View parentVideForGradient;
    private float percent;
    private float position;
    TextView premiumCount;
    private final int premiumLimit;
    private boolean premiumLocked;
    private final TextView premiumText;
    float progress;
    Theme.ResourcesProvider resourcesProvider;
    PremiumGradient.PremiumGradientTools staticGradient;
    boolean wasAnimation;
    boolean wasHaptic;
    int width1;

    /* loaded from: classes3.dex */
    public interface DarkGradientProvider {
        Paint setDarkGradientLocation(float f, float f2);
    }

    public LimitPreviewView(Context context, int i, int i2, int i3, Theme.ResourcesProvider resourcesProvider) {
        this(context, i, i2, i3, 0.5f, resourcesProvider);
    }

    @SuppressLint({"SetTextI18n"})
    public LimitPreviewView(Context context, int i, int i2, int i3, float f, final Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.animationCanPlay = true;
        this.invalidationEnabled = true;
        this.resourcesProvider = resourcesProvider;
        this.percent = MathUtils.clamp(f, 0.1f, 0.9f);
        this.icon = i;
        this.currentValue = i2;
        this.premiumLimit = i3;
        setOrientation(1);
        setClipChildren(false);
        setClipToPadding(false);
        if (i != 0) {
            setPadding(0, AndroidUtilities.dp(16.0f), 0, 0);
            this.limitIcon = new CounterView(context);
            setIconValue(i2, false);
            this.limitIcon.setPadding(AndroidUtilities.dp(19.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(19.0f), AndroidUtilities.dp(14.0f));
            addView(this.limitIcon, LayoutHelper.createLinear(-2, -2, 0.0f, 3));
        }
        final TextViewHolder textViewHolder = new TextViewHolder(context, true);
        TextView textView = new TextView(context);
        this.defaultText = textView;
        textView.setTypeface(AndroidUtilities.bold());
        textView.setText(LocaleController.getString(R.string.LimitFree));
        textView.setGravity(16);
        int i4 = Theme.key_windowBackgroundWhiteBlackText;
        textView.setTextColor(Theme.getColor(i4, resourcesProvider));
        TextView textView2 = new TextView(context);
        this.defaultCount = textView2;
        textView2.setTypeface(AndroidUtilities.bold());
        this.defaultCount.setText(String.format("%d", Integer.valueOf(i3)));
        this.defaultCount.setGravity(16);
        this.defaultCount.setTextColor(Theme.getColor(i4, resourcesProvider));
        if (LocaleController.isRTL) {
            textViewHolder.addView(textView, LayoutHelper.createFrame(-1, 30.0f, 5, 12.0f, 0.0f, 12.0f, 0.0f));
            textViewHolder.addView(this.defaultCount, LayoutHelper.createFrame(-2, 30.0f, 3, 12.0f, 0.0f, 12.0f, 0.0f));
        } else {
            textViewHolder.addView(textView, LayoutHelper.createFrame(-1, 30.0f, 3, 12.0f, 0.0f, 12.0f, 0.0f));
            textViewHolder.addView(this.defaultCount, LayoutHelper.createFrame(-2, 30.0f, 5, 12.0f, 0.0f, 12.0f, 0.0f));
        }
        final TextViewHolder textViewHolder2 = new TextViewHolder(context, false);
        TextView textView3 = new TextView(context);
        this.premiumText = textView3;
        textView3.setTypeface(AndroidUtilities.bold());
        textView3.setText(LocaleController.getString(R.string.LimitPremium));
        textView3.setGravity(16);
        textView3.setTextColor(-1);
        TextView textView4 = new TextView(context) { // from class: org.telegram.ui.Components.Premium.LimitPreviewView.1
            @Override // android.view.View
            public void setAlpha(float f2) {
                super.setAlpha(f2);
            }

            @Override // android.widget.TextView
            public void setTextColor(int i5) {
                super.setTextColor(i5);
            }

            @Override // android.widget.TextView
            public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
                super.setText(charSequence, bufferType);
            }
        };
        this.premiumCount = textView4;
        textView4.setTypeface(AndroidUtilities.bold());
        this.premiumCount.setText(String.format("%d", Integer.valueOf(i3)));
        this.premiumCount.setGravity(16);
        this.premiumCount.setTextColor(-1);
        if (LocaleController.isRTL) {
            textViewHolder2.addView(textView3, LayoutHelper.createFrame(-1, 30.0f, 5, 12.0f, 0.0f, 12.0f, 0.0f));
            textViewHolder2.addView(this.premiumCount, LayoutHelper.createFrame(-2, 30.0f, 3, 12.0f, 0.0f, 12.0f, 0.0f));
        } else {
            textViewHolder2.addView(textView3, LayoutHelper.createFrame(-1, 30.0f, 3, 12.0f, 0.0f, 12.0f, 0.0f));
            textViewHolder2.addView(this.premiumCount, LayoutHelper.createFrame(-2, 30.0f, 5, 12.0f, 0.0f, 12.0f, 0.0f));
        }
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.Components.Premium.LimitPreviewView.2
            Paint grayPaint = new Paint();
            Paint whitePaint;

            {
                Paint paint = new Paint();
                this.whitePaint = paint;
                paint.setColor(-1);
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                if (LimitPreviewView.this.isBoostsStyle) {
                    if (LimitPreviewView.this.isStatistic) {
                        this.grayPaint.setColor(Theme.getColor(Theme.key_listSelector, resourcesProvider));
                    } else {
                        this.grayPaint.setColor(Theme.getColor(Theme.key_graySection, resourcesProvider));
                    }
                } else {
                    this.grayPaint.setColor(Theme.getColor(Theme.key_windowBackgroundGray, resourcesProvider));
                }
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                if (LimitPreviewView.this.hasDarkGradientProvider()) {
                    canvas.drawRoundRect(rectF, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), LimitPreviewView.this.darkGradientProvider.setDarkGradientLocation(((ViewGroup) getParent()).getX() + getX(), ((ViewGroup) getParent()).getY() + getY()));
                } else {
                    canvas.drawRoundRect(rectF, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.grayPaint);
                }
                canvas.save();
                if (!LimitPreviewView.this.isBoostsStyle) {
                    canvas.clipRect(LimitPreviewView.this.width1, 0, getMeasuredWidth(), getMeasuredHeight());
                }
                Paint mainGradientPaint = LimitPreviewView.this.hasDarkGradientProvider() ? this.whitePaint : PremiumGradient.getInstance().getMainGradientPaint();
                if (LimitPreviewView.this.parentVideForGradient != null) {
                    View view = LimitPreviewView.this.parentVideForGradient;
                    LimitPreviewView limitPreviewView = LimitPreviewView.this;
                    PremiumGradient.PremiumGradientTools premiumGradientTools = limitPreviewView.staticGradient;
                    if (premiumGradientTools == null) {
                        float f2 = 0.0f;
                        for (View view2 = this; view2 != view; view2 = (View) view2.getParent()) {
                            f2 += view2.getY();
                        }
                        PremiumGradient.getInstance().updateMainGradientMatrix(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight(), LimitPreviewView.this.getGlobalXOffset() - getLeft(), -f2);
                    } else {
                        mainGradientPaint = premiumGradientTools.paint;
                        premiumGradientTools.gradientMatrixLinear(limitPreviewView.gradientTotalHeight, -limitPreviewView.gradientYOffset);
                    }
                } else {
                    PremiumGradient.getInstance().updateMainGradientMatrix(0, 0, LimitPreviewView.this.getMeasuredWidth(), LimitPreviewView.this.getMeasuredHeight(), LimitPreviewView.this.getGlobalXOffset() - getLeft(), -getTop());
                }
                if (LimitPreviewView.this.isBoostsStyle) {
                    AndroidUtilities.rectTmp.set(0.0f, 0.0f, LimitPreviewView.this.width1, getMeasuredHeight());
                }
                canvas.drawRoundRect(AndroidUtilities.rectTmp, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), mainGradientPaint);
                canvas.restore();
                LimitPreviewView limitPreviewView2 = LimitPreviewView.this;
                if (limitPreviewView2.staticGradient == null && limitPreviewView2.invalidationEnabled) {
                    invalidate();
                }
                super.dispatchDraw(canvas);
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i5, int i6) {
                if (getChildCount() == 2) {
                    int size = View.MeasureSpec.getSize(i5);
                    int size2 = View.MeasureSpec.getSize(i6);
                    textViewHolder.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                    int max = Math.max(textViewHolder.getMeasuredWidth(), AndroidUtilities.dp(24.0f) + LimitPreviewView.this.defaultText.getMeasuredWidth() + (LimitPreviewView.this.defaultCount.getVisibility() == 0 ? AndroidUtilities.dp(24.0f) + LimitPreviewView.this.defaultCount.getMeasuredWidth() : 0));
                    textViewHolder2.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                    if (LimitPreviewView.this.isBoostsStyle) {
                        if (LimitPreviewView.this.percent != 0.0f) {
                            if (LimitPreviewView.this.percent < 1.0f) {
                                float measuredWidth = textViewHolder.getMeasuredWidth() - AndroidUtilities.dp(8.0f);
                                LimitPreviewView limitPreviewView = LimitPreviewView.this;
                                limitPreviewView.width1 = (int) (measuredWidth + (((size - measuredWidth) - (textViewHolder2.getMeasuredWidth() - AndroidUtilities.dp(8.0f))) * limitPreviewView.percent));
                                LimitPreviewView limitPreviewView2 = LimitPreviewView.this;
                                limitPreviewView2.premiumCount.setTextColor(limitPreviewView2.hasDarkGradientProvider() ? -1 : Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
                                LimitPreviewView.this.defaultText.setTextColor(-1);
                            } else {
                                LimitPreviewView limitPreviewView3 = LimitPreviewView.this;
                                limitPreviewView3.width1 = size;
                                limitPreviewView3.premiumCount.setTextColor(-1);
                                LimitPreviewView.this.defaultText.setTextColor(-1);
                            }
                        } else {
                            LimitPreviewView limitPreviewView4 = LimitPreviewView.this;
                            limitPreviewView4.width1 = 0;
                            limitPreviewView4.premiumCount.setTextColor(limitPreviewView4.hasDarkGradientProvider() ? -1 : Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
                            LimitPreviewView.this.defaultText.setTextColor(LimitPreviewView.this.hasDarkGradientProvider() ? -1 : Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
                        }
                    } else {
                        int max2 = Math.max(textViewHolder2.getMeasuredWidth(), AndroidUtilities.dp(24.0f) + LimitPreviewView.this.premiumText.getMeasuredWidth() + (LimitPreviewView.this.premiumCount.getVisibility() == 0 ? AndroidUtilities.dp(24.0f) + LimitPreviewView.this.premiumCount.getMeasuredWidth() : 0));
                        LimitPreviewView limitPreviewView5 = LimitPreviewView.this;
                        limitPreviewView5.width1 = (int) Utilities.clamp(size * limitPreviewView5.percent, size - max2, max);
                        textViewHolder.measure(View.MeasureSpec.makeMeasureSpec(LimitPreviewView.this.width1, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                        textViewHolder2.measure(View.MeasureSpec.makeMeasureSpec(size - LimitPreviewView.this.width1, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                    }
                    setMeasuredDimension(size, size2);
                    return;
                }
                super.onMeasure(i5, i6);
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i5, int i6, int i7, int i8) {
                if (getChildCount() == 2) {
                    View childAt = getChildAt(0);
                    View childAt2 = getChildAt(1);
                    int measuredWidth = childAt.getMeasuredWidth();
                    int i9 = i8 - i6;
                    childAt.layout(0, 0, measuredWidth, i9);
                    childAt2.layout(measuredWidth, 0, i7 - i5, i9);
                    return;
                }
                super.onLayout(z, i5, i6, i7, i8);
            }
        };
        this.limitsContainer = frameLayout;
        frameLayout.addView(textViewHolder, LayoutHelper.createFrame(-1, 30.0f));
        this.limitsContainer.addView(textViewHolder2, LayoutHelper.createFrame(-1, 30.0f));
        addView(this.limitsContainer, LayoutHelper.createLinear(-1, 30, 0.0f, 0, 14, i == 0 ? 0 : 12, 14, 0));
    }

    public void setDarkGradientProvider(DarkGradientProvider darkGradientProvider) {
        this.darkGradientProvider = darkGradientProvider;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasDarkGradientProvider() {
        return this.darkGradientProvider != null;
    }

    public void setIconValue(int i, boolean z) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append((CharSequence) "d").setSpan(new ColoredImageSpan(this.icon), 0, 1, 0);
        spannableStringBuilder.append((CharSequence) " ").setSpan(new RelativeSizeSpan(0.8f), 1, 2, 0);
        spannableStringBuilder.append((CharSequence) Integer.toString(i));
        this.limitIcon.setText(spannableStringBuilder, z);
        this.limitIcon.requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getGlobalXOffset() {
        return (((-getMeasuredWidth()) * 0.1f) * this.progress) - (getMeasuredWidth() * 0.2f);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (this.staticGradient == null) {
            if (this.inc) {
                float f = this.progress + 0.016f;
                this.progress = f;
                if (f > 3.0f) {
                    this.inc = false;
                }
            } else {
                float f2 = this.progress - 0.016f;
                this.progress = f2;
                if (f2 < 1.0f) {
                    this.inc = true;
                }
            }
            invalidate();
        }
        super.dispatchDraw(canvas);
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x017c  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x019b  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x01d2  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x01f3  */
    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float measuredWidth;
        CounterView counterView;
        CounterView counterView2;
        int dp;
        super.onLayout(z, i, i2, i3, i4);
        if (this.animateIncrease || (!this.wasAnimation && this.limitIcon != null && this.animationCanPlay && !this.premiumLocked)) {
            int dp2 = AndroidUtilities.dp(14.0f);
            final boolean z2 = this.animateIncrease;
            this.animateIncrease = false;
            float translationX = z2 ? this.limitIcon.getTranslationX() : 0.0f;
            float f6 = dp2;
            int i5 = dp2 * 2;
            float max = (Math.max(this.width1, (getMeasuredWidth() - i5) * this.position) + f6) - (this.limitIcon.getMeasuredWidth() / 2.0f);
            if (this.isSimpleStyle) {
                f2 = this.limitIcon.getArrowCenter();
                measuredWidth = Utilities.clamp(max, (getMeasuredWidth() - dp2) - this.limitIcon.getMeasuredWidth(), f6);
                int i6 = this.width1;
                if (i6 <= 0) {
                    f4 = f2;
                    f3 = 0.0f;
                } else if (i6 < getMeasuredWidth() - i5) {
                    f3 = Utilities.clamp((this.width1 - (measuredWidth - f6)) / this.limitIcon.getMeasuredWidth(), 1.0f, 0.0f);
                    f4 = f2;
                }
                f5 = measuredWidth;
                this.limitIcon.setAlpha(1.0f);
                this.limitIcon.setTranslationX(translationX);
                this.limitIcon.setPivotX(counterView.getMeasuredWidth() / 2.0f);
                this.limitIcon.setPivotY(counterView2.getMeasuredHeight());
                if (!z2) {
                    this.limitIcon.setScaleX(0.0f);
                    this.limitIcon.setScaleY(0.0f);
                    this.limitIcon.createAnimationLayouts();
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.arrowAnimator = ofFloat;
                final float f7 = this.width1;
                if (z2) {
                    this.width1 = this.animateIncreaseWidth;
                }
                final boolean z3 = !this.animatingRotation;
                this.animatingRotation = true;
                final float f8 = translationX;
                final float f9 = f5;
                final float f10 = f4;
                final float f11 = f3;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Premium.LimitPreviewView$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        LimitPreviewView.this.lambda$onLayout$0(z3, f8, f9, f10, f11, z2, f7, valueAnimator);
                    }
                });
                this.arrowAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Premium.LimitPreviewView.3
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (z3) {
                            LimitPreviewView.this.animatingRotation = false;
                        }
                    }
                });
                this.arrowAnimator.setInterpolator(new OvershootInterpolator());
                if (z2) {
                    ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                    ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Premium.LimitPreviewView$$ExternalSyntheticLambda1
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                            LimitPreviewView.this.lambda$onLayout$1(valueAnimator);
                        }
                    });
                    ofFloat2.setDuration(500L);
                    ofFloat2.start();
                    this.arrowAnimator.setDuration(600L);
                } else {
                    this.arrowAnimator.setDuration(1000L);
                    this.arrowAnimator.setStartDelay(200L);
                }
                this.arrowAnimator.start();
                this.wasAnimation = true;
            }
            if (max < f6) {
                f = 0.0f;
                f2 = 0.0f;
            } else {
                f6 = max;
                f = 0.5f;
                f2 = 0.5f;
            }
            if (f6 > (getMeasuredWidth() - dp2) - this.limitIcon.getMeasuredWidth()) {
                measuredWidth = (getMeasuredWidth() - dp2) - this.limitIcon.getMeasuredWidth();
            } else {
                f3 = f;
                f4 = f2;
                f5 = f6;
                this.limitIcon.setAlpha(1.0f);
                this.limitIcon.setTranslationX(translationX);
                this.limitIcon.setPivotX(counterView.getMeasuredWidth() / 2.0f);
                this.limitIcon.setPivotY(counterView2.getMeasuredHeight());
                if (!z2) {
                }
                ValueAnimator ofFloat3 = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.arrowAnimator = ofFloat3;
                final float f72 = this.width1;
                if (z2) {
                }
                final boolean z32 = !this.animatingRotation;
                this.animatingRotation = true;
                final float f82 = translationX;
                final float f92 = f5;
                final float f102 = f4;
                final float f112 = f3;
                ofFloat3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Premium.LimitPreviewView$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        LimitPreviewView.this.lambda$onLayout$0(z32, f82, f92, f102, f112, z2, f72, valueAnimator);
                    }
                });
                this.arrowAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Premium.LimitPreviewView.3
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (z32) {
                            LimitPreviewView.this.animatingRotation = false;
                        }
                    }
                });
                this.arrowAnimator.setInterpolator(new OvershootInterpolator());
                if (z2) {
                }
                this.arrowAnimator.start();
                this.wasAnimation = true;
            }
            f4 = f2;
            f3 = 1.0f;
            f5 = measuredWidth;
            this.limitIcon.setAlpha(1.0f);
            this.limitIcon.setTranslationX(translationX);
            this.limitIcon.setPivotX(counterView.getMeasuredWidth() / 2.0f);
            this.limitIcon.setPivotY(counterView2.getMeasuredHeight());
            if (!z2) {
            }
            ValueAnimator ofFloat32 = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.arrowAnimator = ofFloat32;
            final float f722 = this.width1;
            if (z2) {
            }
            final boolean z322 = !this.animatingRotation;
            this.animatingRotation = true;
            final float f822 = translationX;
            final float f922 = f5;
            final float f1022 = f4;
            final float f1122 = f3;
            ofFloat32.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Premium.LimitPreviewView$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    LimitPreviewView.this.lambda$onLayout$0(z322, f822, f922, f1022, f1122, z2, f722, valueAnimator);
                }
            });
            this.arrowAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Premium.LimitPreviewView.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (z322) {
                        LimitPreviewView.this.animatingRotation = false;
                    }
                }
            });
            this.arrowAnimator.setInterpolator(new OvershootInterpolator());
            if (z2) {
            }
            this.arrowAnimator.start();
            this.wasAnimation = true;
        } else if (this.isBoostsStyle) {
            this.limitIcon.setAlpha(1.0f);
            this.limitIcon.setScaleX(1.0f);
            this.limitIcon.setScaleY(1.0f);
        } else if (this.premiumLocked) {
            float dp3 = (AndroidUtilities.dp(14.0f) + ((getMeasuredWidth() - (dp * 2)) * 0.5f)) - (this.limitIcon.getMeasuredWidth() / 2.0f);
            boolean z4 = this.wasAnimation;
            if (!z4 && this.animationCanPlay) {
                this.wasAnimation = true;
                this.limitIcon.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(200L).setInterpolator(new OvershootInterpolator()).start();
            } else if (!z4) {
                this.limitIcon.setAlpha(0.0f);
                this.limitIcon.setScaleX(0.0f);
                this.limitIcon.setScaleY(0.0f);
            } else {
                this.limitIcon.setAlpha(1.0f);
                this.limitIcon.setScaleX(1.0f);
                this.limitIcon.setScaleY(1.0f);
            }
            this.limitIcon.setTranslationX(dp3);
        } else {
            CounterView counterView3 = this.limitIcon;
            if (counterView3 != null) {
                counterView3.setAlpha(0.0f);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onLayout$0(boolean z, float f, float f2, float f3, float f4, boolean z2, float f5, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        float min = Math.min(1.0f, floatValue);
        if (floatValue > 1.0f && z) {
            if (!this.wasHaptic) {
                this.wasHaptic = true;
                this.limitIcon.performHapticFeedback(3);
            }
            this.limitIcon.setRotation(this.limitIconRotation + ((floatValue - 1.0f) * 60.0f));
        } else if (!this.animatingRotation) {
            this.limitIcon.setRotation(this.limitIconRotation);
        }
        if (valueAnimator == this.arrowAnimator) {
            float f6 = 1.0f - min;
            this.limitIcon.setTranslationX((f * f6) + (f2 * min));
            float f7 = (f3 * f6) + (f4 * min);
            this.limitIcon.setArrowCenter(f7);
            CounterView counterView = this.limitIcon;
            counterView.setPivotX(counterView.getMeasuredWidth() * f7);
        }
        float min2 = Math.min(1.0f, 2.0f * min);
        if (!z2) {
            this.limitIcon.setScaleX(min2);
            this.limitIcon.setScaleY(min2);
            return;
        }
        this.width1 = (int) AndroidUtilities.lerp(this.animateIncreaseWidth, f5, min);
        this.limitsContainer.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onLayout$1(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.limitIconRotation = floatValue < 0.5f ? (floatValue / 0.5f) * (-7.0f) : (1.0f - ((floatValue - 0.5f) / 0.5f)) * (-7.0f);
    }

    public void setType(int i) {
        if (i == 6) {
            if (this.limitIcon != null) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                spannableStringBuilder.append((CharSequence) "d ").setSpan(new ColoredImageSpan(this.icon), 0, 1, 0);
                spannableStringBuilder.append((CharSequence) (UserConfig.getInstance(UserConfig.selectedAccount).isPremium() ? "4 GB" : "2 GB"));
                this.limitIcon.setText(spannableStringBuilder, false);
            }
            this.premiumCount.setText("4 GB");
        } else if (i == 11) {
            if (this.limitIcon != null) {
                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
                spannableStringBuilder2.append((CharSequence) "d").setSpan(new ColoredImageSpan(this.icon), 0, 1, 0);
                this.limitIcon.setText(spannableStringBuilder2, false);
            }
            this.premiumCount.setText("");
        }
    }

    public void setBagePosition(float f) {
        this.position = MathUtils.clamp(f, 0.1f, 0.9f);
    }

    public void setParentViewForGradien(ViewGroup viewGroup) {
        this.parentVideForGradient = viewGroup;
    }

    public void setStaticGradinet(PremiumGradient.PremiumGradientTools premiumGradientTools) {
        this.staticGradient = premiumGradientTools;
    }

    public void setDelayedAnimation() {
        this.animationCanPlay = false;
    }

    public void startDelayedAnimation() {
        this.animationCanPlay = true;
        requestLayout();
    }

    public void setPremiumLocked() {
        this.limitsContainer.setVisibility(8);
        CounterView counterView = this.limitIcon;
        if (counterView != null) {
            counterView.setPadding(AndroidUtilities.dp(24.0f), AndroidUtilities.dp(3.0f), AndroidUtilities.dp(24.0f), AndroidUtilities.dp(3.0f));
        }
        this.premiumLocked = true;
    }

    public void setBoosts(TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus, boolean z) {
        int i;
        int i2 = tL_stories$TL_premium_boostsStatus.current_level_boosts;
        int i3 = tL_stories$TL_premium_boostsStatus.boosts;
        if ((i2 == i3 && z) || (i = tL_stories$TL_premium_boostsStatus.next_level_boosts) == 0) {
            this.percent = 1.0f;
            TextView textView = this.defaultText;
            int i4 = R.string.BoostsLevel;
            textView.setText(LocaleController.formatString("BoostsLevel", i4, Integer.valueOf(tL_stories$TL_premium_boostsStatus.level - 1)));
            this.premiumCount.setText(LocaleController.formatString("BoostsLevel", i4, Integer.valueOf(tL_stories$TL_premium_boostsStatus.level)));
        } else {
            this.percent = MathUtils.clamp((i3 - i2) / (i - i2), 0.0f, 1.0f);
            TextView textView2 = this.defaultText;
            int i5 = R.string.BoostsLevel;
            textView2.setText(LocaleController.formatString("BoostsLevel", i5, Integer.valueOf(tL_stories$TL_premium_boostsStatus.level)));
            this.premiumCount.setText(LocaleController.formatString("BoostsLevel", i5, Integer.valueOf(tL_stories$TL_premium_boostsStatus.level + 1)));
        }
        ((FrameLayout.LayoutParams) this.premiumCount.getLayoutParams()).gravity = 5;
        setType(17);
        this.defaultCount.setVisibility(8);
        this.premiumText.setVisibility(8);
        this.premiumCount.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
        this.defaultText.setTextColor(-1);
        setIconValue(tL_stories$TL_premium_boostsStatus.boosts, false);
        this.isBoostsStyle = true;
    }

    public void increaseCurrentValue(int i, int i2, int i3) {
        this.currentValue++;
        this.percent = MathUtils.clamp(i2 / i3, 0.0f, 1.0f);
        this.animateIncrease = true;
        this.animateIncreaseWidth = this.width1;
        setIconValue(i, true);
        this.limitsContainer.requestLayout();
        requestLayout();
    }

    /* loaded from: classes3.dex */
    private class TextViewHolder extends FrameLayout {
        private final boolean isLeft;
        private final Paint paint;

        public TextViewHolder(Context context, boolean z) {
            super(context);
            Paint paint = new Paint();
            this.paint = paint;
            setLayerType(2, null);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            this.isLeft = z;
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (view instanceof TextView) {
                boolean drawChild = super.drawChild(canvas, view, j);
                boolean z = false;
                boolean z2 = LimitPreviewView.this.percent != 0.0f && LimitPreviewView.this.percent <= 1.0f && this.isLeft;
                if (LimitPreviewView.this.percent == 1.0f && !this.isLeft) {
                    z = true;
                }
                if ((z2 || z) && LimitPreviewView.this.hasDarkGradientProvider()) {
                    canvas.saveLayer(view.getLeft(), view.getTop(), view.getRight(), view.getBottom(), this.paint, 31);
                    canvas.drawRect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom(), LimitPreviewView.this.darkGradientProvider.setDarkGradientLocation(((ViewGroup) getParent()).getX() + getX(), ((ViewGroup) getParent()).getY() + getY()));
                    canvas.restore();
                    invalidate();
                }
                return drawChild;
            }
            return super.drawChild(canvas, view, j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class CounterView extends View {
        ArrayList<AnimatedLayout> animatedLayouts;
        StaticLayout animatedStableLayout;
        boolean animationInProgress;
        float arrowCenter;
        Paint dstOutPaint;
        boolean invalidatePath;
        Paint overlayPaint;
        Path path;
        PathEffect pathEffect;
        CharSequence text;
        StaticLayout textLayout;
        TextPaint textPaint;
        float textWidth;

        public CounterView(Context context) {
            super(context);
            this.path = new Path();
            this.pathEffect = new CornerPathEffect(AndroidUtilities.dp(6.0f));
            this.textPaint = new TextPaint(1);
            this.animatedLayouts = new ArrayList<>();
            this.dstOutPaint = new Paint();
            this.overlayPaint = new Paint();
            this.textPaint.setTypeface(AndroidUtilities.bold());
            this.textPaint.setTextSize(AndroidUtilities.dp(22.0f));
            this.textPaint.setColor(-1);
            this.dstOutPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            this.overlayPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            TextPaint textPaint = this.textPaint;
            CharSequence charSequence = this.text;
            this.textWidth = textPaint.measureText(charSequence, 0, charSequence.length());
            this.textLayout = new StaticLayout(this.text, this.textPaint, ((int) this.textWidth) + AndroidUtilities.dp(12.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            setMeasuredDimension((int) (this.textWidth + getPaddingRight() + getPaddingLeft()), AndroidUtilities.dp(44.0f) + AndroidUtilities.dp(8.0f));
            updatePath();
        }

        private void updatePath() {
            int measuredHeight = getMeasuredHeight() - AndroidUtilities.dp(8.0f);
            float measuredWidth = getMeasuredWidth() * this.arrowCenter;
            float clamp = Utilities.clamp(AndroidUtilities.dp(8.0f) + measuredWidth, getMeasuredWidth(), 0.0f);
            float clamp2 = Utilities.clamp(AndroidUtilities.dp(10.0f) + measuredWidth, getMeasuredWidth(), AndroidUtilities.dp(24.0f));
            float clamp3 = Utilities.clamp(measuredWidth - AndroidUtilities.dp(this.arrowCenter >= 0.7f ? 24.0f : 10.0f), getMeasuredWidth(), 0.0f);
            float clamp4 = Utilities.clamp(measuredWidth - AndroidUtilities.dp(8.0f), getMeasuredWidth(), 0.0f);
            this.path.rewind();
            float f = measuredHeight;
            float f2 = f - (f / 2.0f);
            this.path.moveTo(clamp3, f2 - AndroidUtilities.dp(2.0f));
            this.path.lineTo(clamp3, f);
            this.path.lineTo(clamp4, f);
            this.path.lineTo(measuredWidth, measuredHeight + AndroidUtilities.dp(8.0f));
            if (this.arrowCenter < 0.7f) {
                this.path.lineTo(clamp, f);
            }
            this.path.lineTo(clamp2, f);
            this.path.lineTo(clamp2, f2 - AndroidUtilities.dp(2.0f));
            this.path.close();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            int measuredHeight = getMeasuredHeight() - AndroidUtilities.dp(8.0f);
            if (LimitPreviewView.this.premiumLocked) {
                measuredHeight = getMeasuredHeight();
                PremiumGradient.getInstance().updateMainGradientMatrix(0, 0, LimitPreviewView.this.getMeasuredWidth(), LimitPreviewView.this.getMeasuredHeight(), LimitPreviewView.this.getGlobalXOffset() - getX(), -getTop());
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, AndroidUtilities.dp(3.0f), getMeasuredWidth(), measuredHeight - AndroidUtilities.dp(3.0f));
                float f = measuredHeight / 2.0f;
                canvas.drawRoundRect(rectF, f, f, PremiumGradient.getInstance().getPremiumLocakedPaint());
            } else {
                if (this.invalidatePath) {
                    this.invalidatePath = false;
                    updatePath();
                }
                PremiumGradient.getInstance().updateMainGradientMatrix(0, 0, LimitPreviewView.this.getMeasuredWidth(), LimitPreviewView.this.getMeasuredHeight(), LimitPreviewView.this.getGlobalXOffset() - getX(), -getTop());
                RectF rectF2 = AndroidUtilities.rectTmp;
                float f2 = measuredHeight;
                rectF2.set(0.0f, 0.0f, getMeasuredWidth(), f2);
                float f3 = f2 / 2.0f;
                canvas.drawRoundRect(rectF2, f3, f3, LimitPreviewView.this.hasDarkGradientProvider() ? this.textPaint : PremiumGradient.getInstance().getMainGradientPaint());
                PremiumGradient.getInstance().getMainGradientPaint().setPathEffect(this.pathEffect);
                if (LimitPreviewView.this.hasDarkGradientProvider()) {
                    this.textPaint.setPathEffect(this.pathEffect);
                }
                canvas.drawPath(this.path, LimitPreviewView.this.hasDarkGradientProvider() ? this.textPaint : PremiumGradient.getInstance().getMainGradientPaint());
                PremiumGradient.getInstance().getMainGradientPaint().setPathEffect(null);
                if (LimitPreviewView.this.hasDarkGradientProvider()) {
                    this.textPaint.setPathEffect(null);
                }
                if (LimitPreviewView.this.invalidationEnabled) {
                    invalidate();
                }
            }
            if (LimitPreviewView.this.hasDarkGradientProvider()) {
                canvas.saveLayer(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.dstOutPaint, 31);
            }
            float measuredWidth = (getMeasuredWidth() - this.textLayout.getWidth()) / 2.0f;
            float height = (measuredHeight - this.textLayout.getHeight()) / 2.0f;
            if (!this.animationInProgress) {
                if (this.textLayout != null) {
                    canvas.save();
                    canvas.translate(measuredWidth, height);
                    this.textLayout.draw(canvas);
                    canvas.restore();
                }
            } else {
                canvas.save();
                canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight() - AndroidUtilities.dp(8.0f));
                if (this.animatedStableLayout != null) {
                    canvas.save();
                    canvas.translate(measuredWidth, height);
                    this.animatedStableLayout.draw(canvas);
                    canvas.restore();
                }
                for (int i = 0; i < this.animatedLayouts.size(); i++) {
                    AnimatedLayout animatedLayout = this.animatedLayouts.get(i);
                    canvas.save();
                    if (animatedLayout.replace) {
                        canvas.translate(animatedLayout.x + measuredWidth, ((measuredHeight * animatedLayout.progress) + height) - ((1 - animatedLayout.staticLayouts.size()) * measuredHeight));
                        for (int i2 = 0; i2 < animatedLayout.staticLayouts.size(); i2++) {
                            canvas.translate(0.0f, -measuredHeight);
                            animatedLayout.staticLayouts.get(i2).draw(canvas);
                        }
                    } else if (animatedLayout.direction) {
                        canvas.translate(animatedLayout.x + measuredWidth, (height - ((measuredHeight * 10) * animatedLayout.progress)) + ((10 - animatedLayout.staticLayouts.size()) * measuredHeight));
                        for (int i3 = 0; i3 < animatedLayout.staticLayouts.size(); i3++) {
                            canvas.translate(0.0f, measuredHeight);
                            animatedLayout.staticLayouts.get(i3).draw(canvas);
                        }
                    } else {
                        canvas.translate(animatedLayout.x + measuredWidth, (((measuredHeight * 10) * animatedLayout.progress) + height) - ((10 - animatedLayout.staticLayouts.size()) * measuredHeight));
                        for (int i4 = 0; i4 < animatedLayout.staticLayouts.size(); i4++) {
                            canvas.translate(0.0f, -measuredHeight);
                            animatedLayout.staticLayouts.get(i4).draw(canvas);
                        }
                    }
                    canvas.restore();
                }
                canvas.restore();
            }
            if (LimitPreviewView.this.hasDarkGradientProvider()) {
                canvas.restore();
                canvas.saveLayer(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), this.overlayPaint, 31);
                canvas.drawRect(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(10.0f), getMeasuredWidth() - AndroidUtilities.dp(12.0f), getMeasuredHeight() - AndroidUtilities.dp(10.0f), LimitPreviewView.this.darkGradientProvider.setDarkGradientLocation(getX(), getY()));
                canvas.restore();
            }
        }

        @Override // android.view.View
        public void setTranslationX(float f) {
            if (f != getTranslationX()) {
                super.setTranslationX(f);
                invalidate();
            }
        }

        void createAnimationLayouts() {
            this.animatedLayouts.clear();
            if (LimitPreviewView.this.isBoostsStyle && LimitPreviewView.this.currentValue == 0) {
                return;
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.text);
            int i = 0;
            boolean z = true;
            for (int i2 = 0; i2 < this.text.length(); i2++) {
                if (Character.isDigit(this.text.charAt(i2))) {
                    AnimatedLayout animatedLayout = new AnimatedLayout();
                    this.animatedLayouts.add(animatedLayout);
                    animatedLayout.x = this.textLayout.getSecondaryHorizontal(i2);
                    animatedLayout.direction = z;
                    if (i >= 1) {
                        z = !z;
                        i = 0;
                    }
                    i++;
                    int charAt = this.text.charAt(i2) - '0';
                    if (charAt == 0) {
                        charAt = 10;
                    }
                    int i3 = 1;
                    while (i3 <= charAt) {
                        animatedLayout.staticLayouts.add(new StaticLayout("" + (i3 == 10 ? 0 : i3), this.textPaint, (int) this.textWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false));
                        i3++;
                    }
                    spannableStringBuilder.setSpan(new EmptyStubSpan(), i2, i2 + 1, 0);
                }
            }
            this.animatedStableLayout = new StaticLayout(spannableStringBuilder, this.textPaint, AndroidUtilities.dp(12.0f) + ((int) this.textWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            for (int i4 = 0; i4 < this.animatedLayouts.size(); i4++) {
                this.animationInProgress = true;
                final AnimatedLayout animatedLayout2 = this.animatedLayouts.get(i4);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                animatedLayout2.valueAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Premium.LimitPreviewView$CounterView$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        LimitPreviewView.CounterView.this.lambda$createAnimationLayouts$0(animatedLayout2, valueAnimator);
                    }
                });
                animatedLayout2.valueAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Premium.LimitPreviewView.CounterView.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        animatedLayout2.valueAnimator = null;
                        CounterView.this.checkAnimationComplete();
                    }
                });
                animatedLayout2.valueAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
                animatedLayout2.valueAnimator.setDuration(750L);
                animatedLayout2.valueAnimator.setStartDelay(((this.animatedLayouts.size() - 1) - i4) * 60);
                animatedLayout2.valueAnimator.start();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$createAnimationLayouts$0(AnimatedLayout animatedLayout, ValueAnimator valueAnimator) {
            animatedLayout.progress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidate();
        }

        void createAnimationLayoutsDiff(CharSequence charSequence) {
            if (this.textLayout == null) {
                return;
            }
            this.animatedLayouts.clear();
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.text);
            int length = this.text.length() - 1;
            int i = 0;
            while (length >= 0) {
                char charAt = length < charSequence.length() ? charSequence.charAt(length) : ' ';
                if (charAt != this.text.charAt(length) && Character.isDigit(this.text.charAt(length))) {
                    AnimatedLayout animatedLayout = new AnimatedLayout();
                    this.animatedLayouts.add(animatedLayout);
                    animatedLayout.x = this.textLayout.getSecondaryHorizontal(length);
                    animatedLayout.replace = true;
                    if (i >= 1) {
                        i = 0;
                    }
                    i++;
                    Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
                    animatedLayout.staticLayouts.add(new StaticLayout("" + charAt, this.textPaint, (int) this.textWidth, alignment, 1.0f, 0.0f, false));
                    animatedLayout.staticLayouts.add(new StaticLayout("" + this.text.charAt(length), this.textPaint, (int) this.textWidth, alignment, 1.0f, 0.0f, false));
                    spannableStringBuilder.setSpan(new EmptyStubSpan(), length, length + 1, 0);
                }
                length--;
            }
            this.animatedStableLayout = new StaticLayout(spannableStringBuilder, this.textPaint, AndroidUtilities.dp(12.0f) + ((int) this.textWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            for (int i2 = 0; i2 < this.animatedLayouts.size(); i2++) {
                this.animationInProgress = true;
                final AnimatedLayout animatedLayout2 = this.animatedLayouts.get(i2);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                animatedLayout2.valueAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Premium.LimitPreviewView$CounterView$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        LimitPreviewView.CounterView.this.lambda$createAnimationLayoutsDiff$1(animatedLayout2, valueAnimator);
                    }
                });
                animatedLayout2.valueAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Premium.LimitPreviewView.CounterView.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        animatedLayout2.valueAnimator = null;
                        CounterView.this.checkAnimationComplete();
                    }
                });
                animatedLayout2.valueAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
                animatedLayout2.valueAnimator.setDuration(250L);
                animatedLayout2.valueAnimator.setStartDelay(((this.animatedLayouts.size() - 1) - i2) * 60);
                animatedLayout2.valueAnimator.start();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$createAnimationLayoutsDiff$1(AnimatedLayout animatedLayout, ValueAnimator valueAnimator) {
            animatedLayout.progress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void checkAnimationComplete() {
            for (int i = 0; i < this.animatedLayouts.size(); i++) {
                if (this.animatedLayouts.get(i).valueAnimator != null) {
                    return;
                }
            }
            this.animatedLayouts.clear();
            this.animationInProgress = false;
            invalidate();
        }

        public void setText(CharSequence charSequence, boolean z) {
            if (!z) {
                this.text = charSequence;
                return;
            }
            CharSequence charSequence2 = this.text;
            this.text = charSequence;
            createAnimationLayoutsDiff(charSequence2);
        }

        public void setArrowCenter(float f) {
            if (this.arrowCenter != f) {
                this.arrowCenter = f;
                this.invalidatePath = true;
                invalidate();
            }
        }

        public float getArrowCenter() {
            return this.arrowCenter;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public class AnimatedLayout {
            public boolean direction;
            float progress;
            public boolean replace;
            ArrayList<StaticLayout> staticLayouts;
            ValueAnimator valueAnimator;
            float x;

            private AnimatedLayout() {
                this.staticLayouts = new ArrayList<>();
            }
        }
    }
}
