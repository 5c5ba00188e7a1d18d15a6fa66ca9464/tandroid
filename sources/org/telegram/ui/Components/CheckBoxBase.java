package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextPaint;
import android.view.View;
import androidx.annotation.Keep;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.GenericProvider;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class CheckBoxBase {
    private static Paint eraser;
    private static Paint forbidPaint;
    private static Paint paint;
    public long animationDuration;
    private boolean attachedToWindow;
    private int background2ColorKey;
    private int backgroundColor;
    private int backgroundColorKey;
    private Paint backgroundPaint;
    private int backgroundType;
    private ObjectAnimator checkAnimator;
    private Paint checkPaint;
    private String checkedText;
    private GenericProvider<Void, Paint> circlePaintProvider;
    private boolean drawUnchecked;
    private boolean forbidden;
    private boolean isChecked;
    private Theme.MessageDrawable messageDrawable;
    private View parentView;
    private float progress;
    private ProgressDelegate progressDelegate;
    private Theme.ResourcesProvider resourcesProvider;
    private float size;
    private TextPaint textPaint;
    private boolean useDefaultCheck;
    public android.graphics.Rect bounds = new android.graphics.Rect();
    private RectF rect = new RectF();
    private float alpha = 1.0f;
    private Path path = new Path();
    private boolean enabled = true;
    private float backgroundAlpha = 1.0f;
    private int checkColorKey = Theme.key_checkboxCheck;

    /* loaded from: classes3.dex */
    public interface ProgressDelegate {
        void setProgress(float f);
    }

    public void setAlpha(float f) {
        this.alpha = f;
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Paint lambda$new$0(Void r0) {
        return paint;
    }

    public CheckBoxBase(View view, int i, Theme.ResourcesProvider resourcesProvider) {
        int i2 = Theme.key_chat_serviceBackground;
        this.backgroundColorKey = i2;
        this.background2ColorKey = i2;
        this.drawUnchecked = true;
        this.circlePaintProvider = new GenericProvider() { // from class: org.telegram.ui.Components.CheckBoxBase$$ExternalSyntheticLambda0
            @Override // org.telegram.messenger.GenericProvider
            public final Object provide(Object obj) {
                Paint lambda$new$0;
                lambda$new$0 = CheckBoxBase.lambda$new$0((Void) obj);
                return lambda$new$0;
            }
        };
        this.animationDuration = 200L;
        this.resourcesProvider = resourcesProvider;
        this.parentView = view;
        this.size = i;
        if (paint == null) {
            paint = new Paint(1);
            Paint paint2 = new Paint(1);
            eraser = paint2;
            paint2.setColor(0);
            eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        Paint paint3 = new Paint(1);
        this.checkPaint = paint3;
        paint3.setStrokeCap(Paint.Cap.ROUND);
        Paint paint4 = this.checkPaint;
        Paint.Style style = Paint.Style.STROKE;
        paint4.setStyle(style);
        this.checkPaint.setStrokeJoin(Paint.Join.ROUND);
        this.checkPaint.setStrokeWidth(AndroidUtilities.dp(1.9f));
        Paint paint5 = new Paint(1);
        this.backgroundPaint = paint5;
        paint5.setStyle(style);
        this.backgroundPaint.setStrokeWidth(AndroidUtilities.dp(1.2f));
    }

    public void setResourcesProvider(Theme.ResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
    }

    public void onAttachedToWindow() {
        this.attachedToWindow = true;
    }

    public void onDetachedFromWindow() {
        this.attachedToWindow = false;
    }

    public void setBounds(int i, int i2, int i3, int i4) {
        android.graphics.Rect rect = this.bounds;
        rect.left = i;
        rect.top = i2;
        rect.right = i + i3;
        rect.bottom = i2 + i4;
    }

    public void setDrawUnchecked(boolean z) {
        this.drawUnchecked = z;
    }

    @Keep
    public void setProgress(float f) {
        if (this.progress == f) {
            return;
        }
        this.progress = f;
        invalidate();
        ProgressDelegate progressDelegate = this.progressDelegate;
        if (progressDelegate != null) {
            progressDelegate.setProgress(f);
        }
    }

    public void setForbidden(boolean z) {
        if (this.forbidden == z) {
            return;
        }
        this.forbidden = z;
        invalidate();
    }

    private void invalidate() {
        if (this.parentView.getParent() != null) {
            ((View) this.parentView.getParent()).invalidate();
        }
        this.parentView.invalidate();
    }

    public void setProgressDelegate(ProgressDelegate progressDelegate) {
        this.progressDelegate = progressDelegate;
    }

    @Keep
    public float getProgress() {
        return this.progress;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setEnabled(boolean z) {
        this.enabled = z;
    }

    public void setBackgroundType(int i) {
        this.backgroundType = i;
        if (i == 12 || i == 13) {
            this.backgroundPaint.setStrokeWidth(AndroidUtilities.dp(1.0f));
        } else if (i == 4 || i == 5) {
            this.backgroundPaint.setStrokeWidth(AndroidUtilities.dp(1.9f));
            if (i == 5) {
                this.checkPaint.setStrokeWidth(AndroidUtilities.dp(1.5f));
            }
        } else if (i == 3) {
            this.backgroundPaint.setStrokeWidth(AndroidUtilities.dp(3.0f));
        } else if (i != 0) {
            this.backgroundPaint.setStrokeWidth(AndroidUtilities.dp(1.5f));
        }
    }

    private void cancelCheckAnimator() {
        ObjectAnimator objectAnimator = this.checkAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
            this.checkAnimator = null;
        }
    }

    private void animateToCheckedState(boolean z) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "progress", z ? 1.0f : 0.0f);
        this.checkAnimator = ofFloat;
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.CheckBoxBase.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (animator.equals(CheckBoxBase.this.checkAnimator)) {
                    CheckBoxBase.this.checkAnimator = null;
                }
                if (CheckBoxBase.this.isChecked) {
                    return;
                }
                CheckBoxBase.this.checkedText = null;
            }
        });
        this.checkAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
        this.checkAnimator.setDuration(this.animationDuration);
        this.checkAnimator.start();
    }

    public void setColor(int i, int i2, int i3) {
        this.backgroundColorKey = i;
        this.background2ColorKey = i2;
        this.checkColorKey = i3;
        invalidate();
    }

    public void setBackgroundColor(int i) {
        this.backgroundColor = i;
        invalidate();
    }

    public void setBackgroundDrawable(Theme.MessageDrawable messageDrawable) {
        this.messageDrawable = messageDrawable;
    }

    public void setUseDefaultCheck(boolean z) {
        this.useDefaultCheck = z;
    }

    public void setBackgroundAlpha(float f) {
        this.backgroundAlpha = f;
    }

    public void setNum(int i) {
        if (i >= 0) {
            this.checkedText = "" + (i + 1);
        } else if (this.checkAnimator == null) {
            this.checkedText = null;
        }
        invalidate();
    }

    public void setChecked(boolean z, boolean z2) {
        setChecked(-1, z, z2);
    }

    public void setChecked(int i, boolean z, boolean z2) {
        if (i >= 0) {
            this.checkedText = "" + (i + 1);
            invalidate();
        }
        if (z == this.isChecked) {
            return;
        }
        this.isChecked = z;
        if (this.attachedToWindow && z2) {
            animateToCheckedState(z);
            return;
        }
        cancelCheckAnimator();
        setProgress(z ? 1.0f : 0.0f);
    }

    /* JADX WARN: Removed duplicated region for block: B:116:0x02e7  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x003a  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x003d  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0049  */
    /* JADX WARN: Removed duplicated region for block: B:212:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00f3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        float f;
        float f2;
        float f3;
        int centerX;
        int centerY;
        int i;
        int i2;
        int i3;
        float f4;
        float f5;
        float f6;
        int i4;
        float f7;
        float f8;
        int i5;
        float f9;
        float f10;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        float dp = AndroidUtilities.dp(this.size / 2.0f);
        int i11 = this.backgroundType;
        if (i11 == 12 || i11 == 13) {
            dp = AndroidUtilities.dp(10.0f);
        } else if (i11 != 0 && i11 != 11) {
            f = dp;
            dp -= AndroidUtilities.dp(0.2f);
            f2 = !this.forbidden ? 1.0f : this.progress;
            f3 = f2 < 0.5f ? 1.0f : f2 / 0.5f;
            centerX = this.bounds.centerX();
            centerY = this.bounds.centerY();
            i = this.backgroundColorKey;
            if (i < 0) {
                if (this.drawUnchecked) {
                    int i12 = this.backgroundType;
                    if (i12 == 12 || i12 == 13) {
                        paint.setColor(getThemedColor(i));
                        paint.setAlpha((int) (this.backgroundAlpha * 255.0f));
                        this.backgroundPaint.setColor(getThemedColor(this.checkColorKey));
                    } else if (i12 == 6 || i12 == 7) {
                        paint.setColor(getThemedColor(this.background2ColorKey));
                        this.backgroundPaint.setColor(getThemedColor(this.checkColorKey));
                    } else if (i12 == 10 || i12 == 14) {
                        this.backgroundPaint.setColor(getThemedColor(this.background2ColorKey));
                    } else {
                        paint.setColor((Theme.getServiceMessageColor() & 16777215) | 671088640);
                        this.backgroundPaint.setColor(getThemedColor(this.checkColorKey));
                    }
                } else {
                    Paint paint2 = this.backgroundPaint;
                    int i13 = this.background2ColorKey;
                    if (i13 < 0) {
                        i13 = this.checkColorKey;
                    }
                    paint2.setColor(AndroidUtilities.getOffsetColor(16777215, getThemedColor(i13), f2, this.backgroundAlpha));
                }
            } else if (this.drawUnchecked) {
                paint.setColor(Color.argb((int) (this.backgroundAlpha * 25.0f), 0, 0, 0));
                if (this.backgroundType == 8) {
                    this.backgroundPaint.setColor(getThemedColor(this.background2ColorKey));
                } else {
                    this.backgroundPaint.setColor(AndroidUtilities.getOffsetColor(-1, getThemedColor(this.checkColorKey), f2, this.backgroundAlpha));
                }
            } else if (this.backgroundColor != 0) {
                this.backgroundPaint.setColor(0);
            } else {
                Paint paint3 = this.backgroundPaint;
                int i14 = this.background2ColorKey;
                if (i14 < 0) {
                    i14 = this.checkColorKey;
                }
                paint3.setColor(AndroidUtilities.getOffsetColor(16777215, getThemedColor(i14), f2, this.backgroundAlpha));
            }
            if (this.drawUnchecked && (i10 = this.backgroundType) >= 0 && i10 != 12 && i10 != 13) {
                if (i10 != 8 || i10 == 10 || i10 == 14) {
                    canvas.drawCircle(centerX, centerY, f - AndroidUtilities.dp(1.5f), this.backgroundPaint);
                } else if (i10 == 6 || i10 == 7) {
                    float f11 = centerX;
                    float f12 = centerY;
                    canvas.drawCircle(f11, f12, f - AndroidUtilities.dp(1.0f), paint);
                    canvas.drawCircle(f11, f12, f - AndroidUtilities.dp(1.5f), this.backgroundPaint);
                } else {
                    canvas.drawCircle(centerX, centerY, f, paint);
                }
            }
            paint.setColor(getThemedColor(this.checkColorKey));
            i2 = this.backgroundType;
            if (i2 != -1 || i2 == 7 || i2 == 8 || i2 == 9 || i2 == 10 || i2 == 14) {
                i3 = centerX;
                f4 = f2;
                f5 = f;
            } else if (i2 == 12 || i2 == 13) {
                i3 = centerX;
                f4 = f2;
                f5 = f;
                this.backgroundPaint.setStyle(Paint.Style.FILL);
                Theme.MessageDrawable messageDrawable = this.messageDrawable;
                if (messageDrawable != null && messageDrawable.hasGradient()) {
                    Shader gradientShader = this.messageDrawable.getGradientShader();
                    Matrix matrix = this.messageDrawable.getMatrix();
                    matrix.reset();
                    this.messageDrawable.applyMatrixScale();
                    matrix.postTranslate(0.0f, (-this.messageDrawable.getTopY()) + this.bounds.top);
                    gradientShader.setLocalMatrix(matrix);
                    this.backgroundPaint.setShader(gradientShader);
                } else {
                    this.backgroundPaint.setShader(null);
                }
                canvas.drawCircle(i3, centerY, (f5 - AndroidUtilities.dp(1.0f)) * this.backgroundAlpha, this.backgroundPaint);
                this.backgroundPaint.setStyle(Paint.Style.STROKE);
            } else if (i2 == 0 || i2 == 11) {
                i3 = centerX;
                f4 = f2;
                f5 = f;
                canvas.drawCircle(i3, centerY, f5, this.backgroundPaint);
            } else {
                float f13 = centerX;
                float f14 = centerY;
                this.rect.set(f13 - dp, f14 - dp, f13 + dp, f14 + dp);
                int i15 = this.backgroundType;
                if (i15 == 6) {
                    i7 = (int) ((-360.0f) * f2);
                    i8 = 6;
                    i9 = 0;
                } else if (i15 == 1) {
                    i7 = (int) ((-270.0f) * f2);
                    i8 = 6;
                    i9 = -90;
                } else {
                    int i16 = (int) (270.0f * f2);
                    if (LocaleController.isRTL) {
                        i16 = -i16;
                    }
                    i7 = i16;
                    i8 = 6;
                    i9 = 90;
                }
                if (i15 == i8) {
                    int themedColor = getThemedColor(Theme.key_dialogBackground);
                    int alpha = Color.alpha(themedColor);
                    this.backgroundPaint.setColor(themedColor);
                    this.backgroundPaint.setAlpha((int) (alpha * f2));
                    i3 = centerX;
                    f4 = f2;
                    f5 = f;
                    canvas.drawArc(this.rect, i9, i7, false, this.backgroundPaint);
                    int themedColor2 = getThemedColor(Theme.key_chat_attachPhotoBackground);
                    int alpha2 = Color.alpha(themedColor2);
                    this.backgroundPaint.setColor(themedColor2);
                    this.backgroundPaint.setAlpha((int) (alpha2 * f4));
                } else {
                    i3 = centerX;
                    f4 = f2;
                    f5 = f;
                }
                canvas.drawArc(this.rect, i9, i7, false, this.backgroundPaint);
            }
            if (f3 <= 0.0f) {
                float f15 = f4 < 0.5f ? 0.0f : (f4 - 0.5f) / 0.5f;
                int i17 = this.backgroundType;
                if (i17 == 9) {
                    paint.setColor(getThemedColor(this.background2ColorKey));
                } else if (i17 == 11 || i17 == 6 || i17 == 7 || i17 == 10 || ((!this.drawUnchecked && this.backgroundColorKey >= 0) || i17 == 14)) {
                    paint.setColor(getThemedColor(this.backgroundColorKey));
                } else {
                    int i18 = this.backgroundColor;
                    if (i18 != 0) {
                        paint.setColor(i18);
                    } else {
                        paint.setColor(getThemedColor(this.enabled ? Theme.key_checkbox : Theme.key_checkboxDisabled));
                    }
                }
                if (this.forbidden) {
                    paint.setColor(this.backgroundPaint.getColor());
                    f6 = 1.0f;
                } else {
                    f6 = 1.0f;
                    if (this.alpha < 1.0f) {
                        paint.setColor(ColorUtils.blendARGB(this.backgroundPaint.getColor(), paint.getColor(), this.alpha));
                    }
                }
                if (!this.useDefaultCheck && (i6 = this.checkColorKey) >= 0) {
                    this.checkPaint.setColor(getThemedColor(i6));
                } else {
                    this.checkPaint.setColor(getThemedColor(Theme.key_checkboxCheck));
                }
                if (this.alpha < f6 && Theme.isCurrentThemeDark()) {
                    this.checkPaint.setColor(ColorUtils.blendARGB(paint.getColor(), this.checkPaint.getColor(), this.alpha));
                }
                if (this.backgroundType != -1) {
                    float dp2 = AndroidUtilities.dp(this.size) / 2.0f;
                    int save = canvas.save();
                    canvas.translate(i3 - dp2, centerY - dp2);
                    i4 = i3;
                    canvas.saveLayerAlpha(0.0f, 0.0f, AndroidUtilities.dp(this.size), AndroidUtilities.dp(this.size), NotificationCenter.voipServiceCreated, 31);
                    Paint provide = this.circlePaintProvider.provide(null);
                    int i19 = this.backgroundType;
                    if (i19 == 12 || i19 == 13) {
                        int alpha3 = provide.getAlpha();
                        provide.setAlpha((int) (f3 * 255.0f));
                        canvas.drawCircle(dp2, dp2, f5 * f3, provide);
                        if (provide != paint) {
                            provide.setAlpha(alpha3);
                        }
                    } else {
                        float dp3 = f5 - AndroidUtilities.dp(0.5f);
                        canvas.drawCircle(dp2, dp2, dp3, provide);
                        canvas.drawCircle(dp2, dp2, dp3 * (f6 - f3), eraser);
                    }
                    canvas.restoreToCount(save);
                } else {
                    i4 = i3;
                }
                if (this.forbidden) {
                    if (forbidPaint == null) {
                        Paint paint4 = new Paint(1);
                        forbidPaint = paint4;
                        paint4.setStyle(Paint.Style.STROKE);
                        forbidPaint.setStrokeCap(Paint.Cap.ROUND);
                        forbidPaint.setStrokeJoin(Paint.Join.ROUND);
                        forbidPaint.setPathEffect(new DashPathEffect(new float[]{AndroidUtilities.dp(0.66f), AndroidUtilities.dp(4.0f)}, 0.0f));
                    }
                    forbidPaint.setStrokeWidth(AndroidUtilities.dp(1.66f));
                    forbidPaint.setColor(getThemedColor(Theme.key_switchTrack));
                    canvas.drawCircle(i4, centerY, AndroidUtilities.dp(9.0f), forbidPaint);
                    return;
                }
                int i20 = i4;
                if (f15 != 0.0f) {
                    if (this.checkedText != null) {
                        if (this.textPaint == null) {
                            i5 = 1;
                            TextPaint textPaint = new TextPaint(1);
                            this.textPaint = textPaint;
                            textPaint.setTypeface(AndroidUtilities.bold());
                        } else {
                            i5 = 1;
                        }
                        int length = this.checkedText.length();
                        if (length == 0 || length == i5 || length == 2) {
                            f9 = 14.0f;
                            f10 = 18.0f;
                        } else if (length != 3) {
                            f9 = 8.0f;
                            f10 = 15.75f;
                        } else {
                            f10 = 16.5f;
                            f9 = 10.0f;
                        }
                        this.textPaint.setTextSize(AndroidUtilities.dp(f9));
                        this.textPaint.setColor(getThemedColor(this.checkColorKey));
                        canvas.save();
                        float f16 = i20;
                        canvas.scale(f15, f6, f16, centerY);
                        String str = this.checkedText;
                        canvas.drawText(str, f16 - (this.textPaint.measureText(str) / 2.0f), AndroidUtilities.dp(f10), this.textPaint);
                        canvas.restore();
                        return;
                    }
                    this.path.reset();
                    int i21 = this.backgroundType;
                    if (i21 == -1) {
                        f8 = 1.4f;
                    } else if (i21 != 5) {
                        f7 = 9.0f;
                        f8 = 1.0f;
                        float dp4 = AndroidUtilities.dp(f7 * f8) * f15;
                        float dp5 = AndroidUtilities.dp(f8 * 4.0f) * f15;
                        int dp6 = centerY + AndroidUtilities.dp(4.0f);
                        float sqrt = (float) Math.sqrt((dp5 * dp5) / 2.0f);
                        float dp7 = i20 - AndroidUtilities.dp(1.5f);
                        float f17 = dp6;
                        this.path.moveTo(dp7 - sqrt, f17 - sqrt);
                        this.path.lineTo(dp7, f17);
                        float sqrt2 = (float) Math.sqrt((dp4 * dp4) / 2.0f);
                        this.path.lineTo(dp7 + sqrt2, f17 - sqrt2);
                        canvas.drawPath(this.path, this.checkPaint);
                        return;
                    } else {
                        f8 = 0.8f;
                    }
                    f7 = 9.0f;
                    float dp42 = AndroidUtilities.dp(f7 * f8) * f15;
                    float dp52 = AndroidUtilities.dp(f8 * 4.0f) * f15;
                    int dp62 = centerY + AndroidUtilities.dp(4.0f);
                    float sqrt3 = (float) Math.sqrt((dp52 * dp52) / 2.0f);
                    float dp72 = i20 - AndroidUtilities.dp(1.5f);
                    float f172 = dp62;
                    this.path.moveTo(dp72 - sqrt3, f172 - sqrt3);
                    this.path.lineTo(dp72, f172);
                    float sqrt22 = (float) Math.sqrt((dp42 * dp42) / 2.0f);
                    this.path.lineTo(dp72 + sqrt22, f172 - sqrt22);
                    canvas.drawPath(this.path, this.checkPaint);
                    return;
                }
                return;
            }
            return;
        }
        f = dp;
        if (!this.forbidden) {
        }
        if (f2 < 0.5f) {
        }
        centerX = this.bounds.centerX();
        centerY = this.bounds.centerY();
        i = this.backgroundColorKey;
        if (i < 0) {
        }
        if (this.drawUnchecked) {
            if (i10 != 8) {
            }
            canvas.drawCircle(centerX, centerY, f - AndroidUtilities.dp(1.5f), this.backgroundPaint);
        }
        paint.setColor(getThemedColor(this.checkColorKey));
        i2 = this.backgroundType;
        if (i2 != -1) {
        }
        i3 = centerX;
        f4 = f2;
        f5 = f;
        if (f3 <= 0.0f) {
        }
    }

    public void setCirclePaintProvider(GenericProvider<Void, Paint> genericProvider) {
        this.circlePaintProvider = genericProvider;
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }
}
