package org.telegram.ui.Components.voip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RLottieImageView;
/* loaded from: classes5.dex */
public class VoIPToggleButton extends FrameLayout {
    private boolean animateBackground;
    int animateToBackgroundColor;
    public int animationDelay;
    private int backgroundCheck1;
    private int backgroundCheck2;
    int backgroundColor;
    private Paint bitmapPaint;
    private ValueAnimator checkAnimator;
    private boolean checkable;
    private boolean checkableForAccessibility;
    private boolean checked;
    private float checkedProgress;
    Paint circlePaint;
    private float crossOffset;
    private Paint crossPaint;
    private float crossProgress;
    int currentBackgroundColor;
    int currentIconColor;
    int currentIconRes;
    String currentText;
    private boolean drawBackground;
    private boolean drawCross;
    Drawable[] icon;
    private boolean iconChangeColor;
    private RLottieImageView lottieImageView;
    private float radius;
    ValueAnimator replaceAnimator;
    private int replaceColorFrom;
    float replaceProgress;
    Drawable rippleDrawable;
    FrameLayout textLayoutContainer;
    TextView[] textView;
    private Paint xRefPaint;

    public VoIPToggleButton(Context context) {
        this(context, 52.0f);
    }

    public VoIPToggleButton(Context context, float radius) {
        super(context);
        this.circlePaint = new Paint(1);
        this.drawBackground = true;
        this.icon = new Drawable[2];
        this.textView = new TextView[2];
        this.crossPaint = new Paint(1);
        this.xRefPaint = new Paint(1);
        this.bitmapPaint = new Paint(1);
        this.radius = radius;
        setWillNotDraw(false);
        FrameLayout frameLayout = new FrameLayout(context);
        this.textLayoutContainer = frameLayout;
        addView(frameLayout);
        for (int i = 0; i < 2; i++) {
            TextView textView = new TextView(context);
            textView.setGravity(1);
            textView.setTextSize(1, 11.0f);
            textView.setTextColor(-1);
            textView.setImportantForAccessibility(2);
            this.textLayoutContainer.addView(textView, LayoutHelper.createFrame(-1, -2.0f, 0, 0.0f, 4.0f + radius, 0.0f, 0.0f));
            this.textView[i] = textView;
        }
        this.textView[1].setVisibility(8);
        this.xRefPaint.setColor(-16777216);
        this.xRefPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.xRefPaint.setStrokeWidth(AndroidUtilities.dp(3.0f));
        this.crossPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        this.crossPaint.setStrokeCap(Paint.Cap.ROUND);
        this.bitmapPaint.setFilterBitmap(true);
    }

    public void setTextSize(int size) {
        for (int i = 0; i < 2; i++) {
            this.textView[i].setTextSize(1, size);
        }
    }

    public void setDrawBackground(boolean value) {
        this.drawBackground = value;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x01c7  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x029e  */
    /* JADX WARN: Removed duplicated region for block: B:76:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        if (this.animateBackground) {
            float f = this.replaceProgress;
            if (f != 0.0f) {
                this.circlePaint.setColor(ColorUtils.blendARGB(this.backgroundColor, this.animateToBackgroundColor, f));
                float f2 = 2.0f;
                float cx = getWidth() / 2.0f;
                float cy = AndroidUtilities.dp(this.radius) / 2.0f;
                float radius = AndroidUtilities.dp(this.radius) / 2.0f;
                if (this.drawBackground) {
                    canvas.drawCircle(cx, cy, AndroidUtilities.dp(this.radius) / 2.0f, this.circlePaint);
                }
                if (this.rippleDrawable == null) {
                    Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(this.radius), 0, -16777216);
                    this.rippleDrawable = createSimpleSelectorCircleDrawable;
                    createSimpleSelectorCircleDrawable.setCallback(this);
                }
                this.rippleDrawable.setBounds((int) (cx - radius), (int) (cy - radius), (int) (cx + radius), (int) (cy + radius));
                this.rippleDrawable.draw(canvas);
                if (this.currentIconRes == 0) {
                    if (this.drawCross || this.crossProgress != 0.0f) {
                        if (this.iconChangeColor) {
                            int color = ColorUtils.blendARGB(this.replaceColorFrom, this.currentIconColor, this.replaceProgress);
                            this.icon[0].setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
                            this.crossPaint.setColor(color);
                        }
                        this.icon[0].setAlpha(255);
                        float f3 = this.replaceProgress;
                        if (f3 != 0.0f && this.iconChangeColor) {
                            int color2 = ColorUtils.blendARGB(this.replaceColorFrom, this.currentIconColor, f3);
                            this.icon[0].setColorFilter(new PorterDuffColorFilter(color2, PorterDuff.Mode.MULTIPLY));
                            this.crossPaint.setColor(color2);
                        }
                        this.icon[0].setAlpha(255);
                        boolean z = this.drawCross;
                        if (z) {
                            float f4 = this.crossProgress;
                            if (f4 < 1.0f) {
                                float f5 = f4 + 0.08f;
                                this.crossProgress = f5;
                                if (f5 > 1.0f) {
                                    this.crossProgress = 1.0f;
                                } else {
                                    invalidate();
                                }
                                if (this.crossProgress <= 0.0f) {
                                    int left = (int) (cx - (this.icon[0].getIntrinsicWidth() / 2.0f));
                                    int top = (int) (cy - (this.icon[0].getIntrinsicHeight() / 2));
                                    float startX = left + AndroidUtilities.dpf2(8.0f) + this.crossOffset;
                                    float startY = top + AndroidUtilities.dpf2(8.0f);
                                    float endX = (startX - AndroidUtilities.dp(1.0f)) + (AndroidUtilities.dp(17.0f) * CubicBezierInterpolator.DEFAULT.getInterpolation(this.crossProgress));
                                    float endY = startY + (AndroidUtilities.dp(17.0f) * CubicBezierInterpolator.DEFAULT.getInterpolation(this.crossProgress));
                                    canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), 255, 31);
                                    Drawable[] drawableArr = this.icon;
                                    drawableArr[0].setBounds((int) (cx - (drawableArr[0].getIntrinsicWidth() / 2.0f)), (int) (cy - (this.icon[0].getIntrinsicHeight() / 2)), (int) ((this.icon[0].getIntrinsicWidth() / 2) + cx), (int) ((this.icon[0].getIntrinsicHeight() / 2) + cy));
                                    this.icon[0].draw(canvas);
                                    canvas.drawLine(startX, startY - AndroidUtilities.dp(2.0f), endX, endY - AndroidUtilities.dp(2.0f), this.xRefPaint);
                                    canvas.drawLine(startX, startY, endX, endY, this.crossPaint);
                                    canvas.restore();
                                    return;
                                }
                                Drawable[] drawableArr2 = this.icon;
                                drawableArr2[0].setBounds((int) (cx - (drawableArr2[0].getIntrinsicWidth() / 2.0f)), (int) (cy - (this.icon[0].getIntrinsicHeight() / 2)), (int) ((this.icon[0].getIntrinsicWidth() / 2) + cx), (int) ((this.icon[0].getIntrinsicHeight() / 2) + cy));
                                this.icon[0].draw(canvas);
                                return;
                            }
                        }
                        if (!z) {
                            float f6 = this.crossProgress - 0.08f;
                            this.crossProgress = f6;
                            if (f6 < 0.0f) {
                                this.crossProgress = 0.0f;
                            } else {
                                invalidate();
                            }
                        }
                        if (this.crossProgress <= 0.0f) {
                        }
                    } else {
                        int i = 0;
                        while (true) {
                            if (i < ((this.replaceProgress == 0.0f || this.iconChangeColor) ? 1 : 2)) {
                                if (this.icon[i] != null) {
                                    canvas.save();
                                    float p = this.replaceProgress;
                                    if (p != 0.0f && !this.iconChangeColor) {
                                        Drawable[] drawableArr3 = this.icon;
                                        if (drawableArr3[0] != null && drawableArr3[1] != null) {
                                            if (i == 0) {
                                                p = 1.0f - p;
                                            }
                                            canvas.scale(p, p, cx, cy);
                                            this.icon[i].setAlpha((int) (255.0f * p));
                                            Drawable[] drawableArr4 = this.icon;
                                            drawableArr4[i].setBounds((int) (cx - (drawableArr4[i].getIntrinsicWidth() / f2)), (int) (cy - (this.icon[i].getIntrinsicHeight() / 2)), (int) ((this.icon[i].getIntrinsicWidth() / 2) + cx), (int) ((this.icon[i].getIntrinsicHeight() / 2) + cy));
                                            this.icon[i].draw(canvas);
                                            canvas.restore();
                                        }
                                    }
                                    if (this.iconChangeColor) {
                                        int color3 = ColorUtils.blendARGB(this.replaceColorFrom, this.currentIconColor, p);
                                        this.icon[i].setColorFilter(new PorterDuffColorFilter(color3, PorterDuff.Mode.MULTIPLY));
                                        this.crossPaint.setColor(color3);
                                    }
                                    this.icon[i].setAlpha(255);
                                    Drawable[] drawableArr42 = this.icon;
                                    drawableArr42[i].setBounds((int) (cx - (drawableArr42[i].getIntrinsicWidth() / f2)), (int) (cy - (this.icon[i].getIntrinsicHeight() / 2)), (int) ((this.icon[i].getIntrinsicWidth() / 2) + cx), (int) ((this.icon[i].getIntrinsicHeight() / 2) + cy));
                                    this.icon[i].draw(canvas);
                                    canvas.restore();
                                }
                                i++;
                                f2 = 2.0f;
                            } else {
                                return;
                            }
                        }
                    }
                } else {
                    return;
                }
            }
        }
        this.circlePaint.setColor(this.backgroundColor);
        float f22 = 2.0f;
        float cx2 = getWidth() / 2.0f;
        float cy2 = AndroidUtilities.dp(this.radius) / 2.0f;
        float radius2 = AndroidUtilities.dp(this.radius) / 2.0f;
        if (this.drawBackground) {
        }
        if (this.rippleDrawable == null) {
        }
        this.rippleDrawable.setBounds((int) (cx2 - radius2), (int) (cy2 - radius2), (int) (cx2 + radius2), (int) (cy2 + radius2));
        this.rippleDrawable.draw(canvas);
        if (this.currentIconRes == 0) {
        }
    }

    public void setBackgroundColor(int backgroundColor, int backgroundColorChecked) {
        this.backgroundCheck1 = backgroundColor;
        this.backgroundCheck2 = backgroundColorChecked;
        this.backgroundColor = ColorUtils.blendARGB(backgroundColor, backgroundColorChecked, this.checkedProgress);
        invalidate();
    }

    public void setData(int iconRes, int iconColor, int backgroundColor, String text, boolean cross, boolean animated) {
        setData(iconRes, iconColor, backgroundColor, 1.0f, true, text, cross, animated);
    }

    public void setEnabled(boolean enabled, boolean animated) {
        super.setEnabled(enabled);
        float f = 1.0f;
        if (animated) {
            ViewPropertyAnimator animate = animate();
            if (!enabled) {
                f = 0.5f;
            }
            animate.alpha(f).setDuration(180L).start();
            return;
        }
        clearAnimation();
        if (!enabled) {
            f = 0.5f;
        }
        setAlpha(f);
    }

    public void setData(int iconRes, int iconColor, int backgroundColor, float selectorAlpha, boolean recreateRipple, String text, boolean cross, boolean animated) {
        String str;
        if (getVisibility() != 0) {
            animated = false;
            setVisibility(0);
        }
        if (this.currentIconRes == iconRes && this.currentIconColor == iconColor && ((this.checkable || this.currentBackgroundColor == backgroundColor) && (str = this.currentText) != null && str.equals(text) && cross == this.drawCross)) {
            return;
        }
        if (this.rippleDrawable == null || recreateRipple) {
            if (Color.alpha(backgroundColor) == 255 && AndroidUtilities.computePerceivedBrightness(backgroundColor) > 0.5d) {
                Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(this.radius), 0, ColorUtils.setAlphaComponent(-16777216, (int) (25.5f * selectorAlpha)));
                this.rippleDrawable = createSimpleSelectorCircleDrawable;
                createSimpleSelectorCircleDrawable.setCallback(this);
            } else {
                Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(this.radius), 0, ColorUtils.setAlphaComponent(-1, (int) (76.5f * selectorAlpha)));
                this.rippleDrawable = createSimpleSelectorCircleDrawable2;
                createSimpleSelectorCircleDrawable2.setCallback(this);
            }
        }
        ValueAnimator valueAnimator = this.replaceAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.animateBackground = this.currentBackgroundColor != backgroundColor;
        boolean z = this.currentIconRes == iconRes;
        this.iconChangeColor = z;
        if (z) {
            this.replaceColorFrom = this.currentIconColor;
        }
        this.currentIconRes = iconRes;
        this.currentIconColor = iconColor;
        this.currentBackgroundColor = backgroundColor;
        this.currentText = text;
        this.drawCross = cross;
        if (!animated) {
            if (iconRes != 0) {
                this.icon[0] = ContextCompat.getDrawable(getContext(), iconRes).mutate();
                this.icon[0].setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.MULTIPLY));
            }
            this.crossPaint.setColor(iconColor);
            if (!this.checkable) {
                this.backgroundColor = backgroundColor;
            }
            this.textView[0].setText(text);
            this.crossProgress = this.drawCross ? 1.0f : 0.0f;
            this.iconChangeColor = false;
            this.replaceProgress = 0.0f;
            invalidate();
            return;
        }
        if (!z && iconRes != 0) {
            this.icon[1] = ContextCompat.getDrawable(getContext(), iconRes).mutate();
            this.icon[1].setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.MULTIPLY));
        }
        if (!this.checkable) {
            this.animateToBackgroundColor = backgroundColor;
        }
        final boolean animateText = !this.textView[0].getText().toString().equals(text);
        if (!animateText) {
            this.textView[0].setText(text);
        } else {
            this.textView[1].setText(text);
            this.textView[1].setVisibility(0);
            this.textView[1].setAlpha(0.0f);
            this.textView[1].setScaleX(0.0f);
            this.textView[1].setScaleY(0.0f);
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.replaceAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.voip.VoIPToggleButton$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                VoIPToggleButton.this.m3274xb3bc3dbb(animateText, valueAnimator2);
            }
        });
        this.replaceAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.voip.VoIPToggleButton.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                VoIPToggleButton.this.replaceAnimator = null;
                if (animateText) {
                    TextView tv = VoIPToggleButton.this.textView[0];
                    VoIPToggleButton.this.textView[0] = VoIPToggleButton.this.textView[1];
                    VoIPToggleButton.this.textView[1] = tv;
                    VoIPToggleButton.this.textView[1].setVisibility(8);
                }
                if (!VoIPToggleButton.this.iconChangeColor && VoIPToggleButton.this.icon[1] != null) {
                    VoIPToggleButton.this.icon[0] = VoIPToggleButton.this.icon[1];
                    VoIPToggleButton.this.icon[1] = null;
                }
                VoIPToggleButton.this.iconChangeColor = false;
                if (!VoIPToggleButton.this.checkable) {
                    VoIPToggleButton voIPToggleButton = VoIPToggleButton.this;
                    voIPToggleButton.backgroundColor = voIPToggleButton.animateToBackgroundColor;
                }
                VoIPToggleButton.this.replaceProgress = 0.0f;
                VoIPToggleButton.this.invalidate();
            }
        });
        this.replaceAnimator.setDuration(150L).start();
        invalidate();
    }

    /* renamed from: lambda$setData$0$org-telegram-ui-Components-voip-VoIPToggleButton */
    public /* synthetic */ void m3274xb3bc3dbb(boolean animateText, ValueAnimator valueAnimator) {
        this.replaceProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
        if (animateText) {
            this.textView[0].setAlpha(1.0f - this.replaceProgress);
            this.textView[0].setScaleX(1.0f - this.replaceProgress);
            this.textView[0].setScaleY(1.0f - this.replaceProgress);
            this.textView[1].setAlpha(this.replaceProgress);
            this.textView[1].setScaleX(this.replaceProgress);
            this.textView[1].setScaleY(this.replaceProgress);
        }
    }

    public void setCrossOffset(float crossOffset) {
        this.crossOffset = crossOffset;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.rippleDrawable;
        if (drawable != null) {
            drawable.setState(getDrawableState());
        }
    }

    @Override // android.view.View
    public boolean verifyDrawable(Drawable drawable) {
        return this.rippleDrawable == drawable || super.verifyDrawable(drawable);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.rippleDrawable;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    public void setCheckableForAccessibility(boolean checkableForAccessibility) {
        this.checkableForAccessibility = checkableForAccessibility;
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    public void setChecked(boolean value, boolean animated) {
        if (this.checked == value) {
            return;
        }
        this.checked = value;
        if (this.checkable) {
            float f = 1.0f;
            if (animated) {
                ValueAnimator valueAnimator = this.checkAnimator;
                if (valueAnimator != null) {
                    valueAnimator.removeAllListeners();
                    this.checkAnimator.cancel();
                }
                float[] fArr = new float[2];
                fArr[0] = this.checkedProgress;
                if (!this.checked) {
                    f = 0.0f;
                }
                fArr[1] = f;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
                this.checkAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.voip.VoIPToggleButton$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        VoIPToggleButton.this.m3273xb7677e67(valueAnimator2);
                    }
                });
                this.checkAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.voip.VoIPToggleButton.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animation) {
                        VoIPToggleButton voIPToggleButton = VoIPToggleButton.this;
                        voIPToggleButton.checkedProgress = voIPToggleButton.checked ? 1.0f : 0.0f;
                        VoIPToggleButton voIPToggleButton2 = VoIPToggleButton.this;
                        voIPToggleButton2.setBackgroundColor(voIPToggleButton2.backgroundCheck1, VoIPToggleButton.this.backgroundCheck2);
                    }
                });
                this.checkAnimator.setDuration(150L);
                this.checkAnimator.start();
                return;
            }
            if (!value) {
                f = 0.0f;
            }
            this.checkedProgress = f;
            setBackgroundColor(this.backgroundCheck1, this.backgroundCheck2);
        }
    }

    /* renamed from: lambda$setChecked$1$org-telegram-ui-Components-voip-VoIPToggleButton */
    public /* synthetic */ void m3273xb7677e67(ValueAnimator valueAnimator) {
        this.checkedProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        setBackgroundColor(this.backgroundCheck1, this.backgroundCheck2);
    }

    public boolean isChecked() {
        return this.checked;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setText(this.currentText);
        if (this.checkable || this.checkableForAccessibility) {
            info.setClassName(ToggleButton.class.getName());
            info.setCheckable(true);
            info.setChecked(this.checked);
            return;
        }
        info.setClassName(Button.class.getName());
    }

    public void shakeView() {
        AndroidUtilities.shakeView(this.textView[0], 2.0f, 0);
        AndroidUtilities.shakeView(this.textView[1], 2.0f, 0);
    }

    public void showText(boolean show, boolean animated) {
        float a = 1.0f;
        if (animated) {
            if (!show) {
                a = 0.0f;
            }
            if (this.textLayoutContainer.getAlpha() != a) {
                this.textLayoutContainer.animate().alpha(a).start();
                return;
            }
            return;
        }
        this.textLayoutContainer.animate().cancel();
        FrameLayout frameLayout = this.textLayoutContainer;
        if (!show) {
            a = 0.0f;
        }
        frameLayout.setAlpha(a);
    }
}
