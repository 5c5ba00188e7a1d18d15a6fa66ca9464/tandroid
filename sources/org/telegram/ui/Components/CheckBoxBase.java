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
    private GenericProvider circlePaintProvider;
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

    private void cancelCheckAnimator() {
        ObjectAnimator objectAnimator = this.checkAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
            this.checkAnimator = null;
        }
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    private void invalidate() {
        if (this.parentView.getParent() != null) {
            ((View) this.parentView.getParent()).invalidate();
        }
        this.parentView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Paint lambda$new$0(Void r0) {
        return paint;
    }

    /* JADX WARN: Code restructure failed: missing block: B:41:0x00b8, code lost:
        if (r12 >= 0) goto L192;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00f7, code lost:
        if (r12 >= 0) goto L192;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00fa, code lost:
        r12 = r26.checkColorKey;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00fc, code lost:
        r10 = org.telegram.messenger.AndroidUtilities.getOffsetColor(16777215, getThemedColor(r12), r3, r26.backgroundAlpha);
     */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0295  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x003a  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x003d  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x02ea  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x02f8  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x035c  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x03d3  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x03db  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x0445  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0049  */
    /* JADX WARN: Removed duplicated region for block: B:214:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00bb  */
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
        Paint paint2;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        float f4;
        float f5;
        Paint paint3;
        int i7;
        float f6;
        int i8;
        float f7;
        float f8;
        int i9;
        float f9;
        float f10;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        float f11;
        float f12;
        Paint paint4;
        int themedColor;
        float dp = AndroidUtilities.dp(this.size / 2.0f);
        int i15 = this.backgroundType;
        if (i15 == 12 || i15 == 13) {
            dp = AndroidUtilities.dp(10.0f);
        } else if (i15 != 0 && i15 != 11) {
            f = dp;
            dp -= AndroidUtilities.dp(0.2f);
            f2 = !this.forbidden ? 1.0f : this.progress;
            f3 = f2 < 0.5f ? 1.0f : f2 / 0.5f;
            centerX = this.bounds.centerX();
            centerY = this.bounds.centerY();
            i = this.backgroundColorKey;
            if (i < 0) {
                if (this.drawUnchecked) {
                    int i16 = this.backgroundType;
                    if (i16 == 12 || i16 == 13) {
                        paint.setColor(getThemedColor(i));
                        paint.setAlpha((int) (this.backgroundAlpha * 255.0f));
                    } else {
                        if (i16 == 6 || i16 == 7) {
                            paint4 = paint;
                            themedColor = getThemedColor(this.background2ColorKey);
                        } else {
                            if (i16 != 10 && i16 != 14) {
                                paint4 = paint;
                                themedColor = (16777215 & Theme.getServiceMessageColor()) | 671088640;
                            }
                            paint2 = this.backgroundPaint;
                            i4 = this.background2ColorKey;
                            i3 = getThemedColor(i4);
                        }
                        paint4.setColor(themedColor);
                    }
                    paint2 = this.backgroundPaint;
                    i4 = this.checkColorKey;
                    i3 = getThemedColor(i4);
                } else {
                    paint2 = this.backgroundPaint;
                    i2 = this.background2ColorKey;
                }
            } else if (this.drawUnchecked) {
                paint.setColor(Color.argb((int) (this.backgroundAlpha * 25.0f), 0, 0, 0));
                if (this.backgroundType != 8) {
                    paint2 = this.backgroundPaint;
                    i3 = AndroidUtilities.getOffsetColor(-1, getThemedColor(this.checkColorKey), f2, this.backgroundAlpha);
                }
                paint2 = this.backgroundPaint;
                i4 = this.background2ColorKey;
                i3 = getThemedColor(i4);
            } else if (this.backgroundColor != 0) {
                paint2 = this.backgroundPaint;
                i3 = 0;
            } else {
                paint2 = this.backgroundPaint;
                i2 = this.background2ColorKey;
            }
            paint2.setColor(i3);
            if (this.drawUnchecked && (i14 = this.backgroundType) >= 0 && i14 != 12 && i14 != 13) {
                if (i14 != 8 || i14 == 10 || i14 == 14) {
                    f11 = centerX;
                    f12 = centerY;
                } else if (i14 == 6 || i14 == 7) {
                    f11 = centerX;
                    f12 = centerY;
                    canvas.drawCircle(f11, f12, f - AndroidUtilities.dp(1.0f), paint);
                } else {
                    canvas.drawCircle(centerX, centerY, f, paint);
                }
                canvas.drawCircle(f11, f12, f - AndroidUtilities.dp(1.5f), this.backgroundPaint);
            }
            paint.setColor(getThemedColor(this.checkColorKey));
            i5 = this.backgroundType;
            if (i5 != -1 || i5 == 7 || i5 == 8 || i5 == 9 || i5 == 10 || i5 == 14) {
                i6 = centerX;
                f4 = f2;
                f5 = f;
            } else if (i5 == 12 || i5 == 13) {
                i6 = centerX;
                f4 = f2;
                f5 = f;
                this.backgroundPaint.setStyle(Paint.Style.FILL);
                Theme.MessageDrawable messageDrawable = this.messageDrawable;
                if (messageDrawable == null || !messageDrawable.hasGradient()) {
                    this.backgroundPaint.setShader(null);
                } else {
                    Shader gradientShader = this.messageDrawable.getGradientShader();
                    Matrix matrix = this.messageDrawable.getMatrix();
                    matrix.reset();
                    this.messageDrawable.applyMatrixScale();
                    matrix.postTranslate(0.0f, (-this.messageDrawable.getTopY()) + this.bounds.top);
                    gradientShader.setLocalMatrix(matrix);
                    this.backgroundPaint.setShader(gradientShader);
                }
                canvas.drawCircle(i6, centerY, (f5 - AndroidUtilities.dp(1.0f)) * this.backgroundAlpha, this.backgroundPaint);
                this.backgroundPaint.setStyle(Paint.Style.STROKE);
            } else if (i5 == 0 || i5 == 11) {
                i6 = centerX;
                f4 = f2;
                f5 = f;
                canvas.drawCircle(i6, centerY, f5, this.backgroundPaint);
            } else {
                float f13 = centerX;
                float f14 = centerY;
                this.rect.set(f13 - dp, f14 - dp, f13 + dp, f14 + dp);
                int i17 = this.backgroundType;
                if (i17 == 6) {
                    i11 = (int) ((-360.0f) * f2);
                    i12 = 6;
                    i13 = 0;
                } else if (i17 == 1) {
                    i11 = (int) ((-270.0f) * f2);
                    i12 = 6;
                    i13 = -90;
                } else {
                    int i18 = (int) (270.0f * f2);
                    if (LocaleController.isRTL) {
                        i18 = -i18;
                    }
                    i11 = i18;
                    i12 = 6;
                    i13 = 90;
                }
                if (i17 == i12) {
                    int themedColor2 = getThemedColor(Theme.key_dialogBackground);
                    int alpha = Color.alpha(themedColor2);
                    this.backgroundPaint.setColor(themedColor2);
                    this.backgroundPaint.setAlpha((int) (alpha * f2));
                    i6 = centerX;
                    f4 = f2;
                    f5 = f;
                    canvas.drawArc(this.rect, i13, i11, false, this.backgroundPaint);
                    int themedColor3 = getThemedColor(Theme.key_chat_attachPhotoBackground);
                    int alpha2 = Color.alpha(themedColor3);
                    this.backgroundPaint.setColor(themedColor3);
                    this.backgroundPaint.setAlpha((int) (alpha2 * f4));
                } else {
                    i6 = centerX;
                    f4 = f2;
                    f5 = f;
                }
                canvas.drawArc(this.rect, i13, i11, false, this.backgroundPaint);
            }
            if (f3 > 0.0f) {
                return;
            }
            float f15 = f4 < 0.5f ? 0.0f : (f4 - 0.5f) / 0.5f;
            int i19 = this.backgroundType;
            if (i19 == 9) {
                paint3 = paint;
                i7 = this.background2ColorKey;
            } else if (i19 == 11 || i19 == 6 || i19 == 7 || i19 == 10 || ((!this.drawUnchecked && this.backgroundColorKey >= 0) || i19 == 14)) {
                paint3 = paint;
                i7 = this.backgroundColorKey;
            } else {
                int i20 = this.backgroundColor;
                if (i20 != 0) {
                    paint.setColor(i20);
                    if (this.forbidden) {
                        f6 = 1.0f;
                        if (this.alpha < 1.0f) {
                            paint.setColor(ColorUtils.blendARGB(this.backgroundPaint.getColor(), paint.getColor(), this.alpha));
                        }
                    } else {
                        paint.setColor(this.backgroundPaint.getColor());
                        f6 = 1.0f;
                    }
                    if (!this.useDefaultCheck || (i10 = this.checkColorKey) < 0) {
                        this.checkPaint.setColor(getThemedColor(Theme.key_checkboxCheck));
                    } else {
                        this.checkPaint.setColor(getThemedColor(i10));
                    }
                    if (this.alpha < f6 && Theme.isCurrentThemeDark()) {
                        this.checkPaint.setColor(ColorUtils.blendARGB(paint.getColor(), this.checkPaint.getColor(), this.alpha));
                    }
                    if (this.backgroundType == -1) {
                        float dp2 = AndroidUtilities.dp(this.size) / 2.0f;
                        int save = canvas.save();
                        canvas.translate(i6 - dp2, centerY - dp2);
                        i8 = i6;
                        canvas.saveLayerAlpha(0.0f, 0.0f, AndroidUtilities.dp(this.size), AndroidUtilities.dp(this.size), NotificationCenter.closeSearchByActiveAction, 31);
                        Paint paint5 = (Paint) this.circlePaintProvider.provide(null);
                        int i21 = this.backgroundType;
                        if (i21 == 12 || i21 == 13) {
                            int alpha3 = paint5.getAlpha();
                            paint5.setAlpha((int) (f3 * 255.0f));
                            canvas.drawCircle(dp2, dp2, f5 * f3, paint5);
                            if (paint5 != paint) {
                                paint5.setAlpha(alpha3);
                            }
                        } else {
                            float dp3 = f5 - AndroidUtilities.dp(0.5f);
                            canvas.drawCircle(dp2, dp2, dp3, paint5);
                            canvas.drawCircle(dp2, dp2, dp3 * (f6 - f3), eraser);
                        }
                        canvas.restoreToCount(save);
                    } else {
                        i8 = i6;
                    }
                    if (!this.forbidden) {
                        if (forbidPaint == null) {
                            Paint paint6 = new Paint(1);
                            forbidPaint = paint6;
                            paint6.setStyle(Paint.Style.STROKE);
                            forbidPaint.setStrokeCap(Paint.Cap.ROUND);
                            forbidPaint.setStrokeJoin(Paint.Join.ROUND);
                            forbidPaint.setPathEffect(new DashPathEffect(new float[]{AndroidUtilities.dp(0.66f), AndroidUtilities.dp(4.0f)}, 0.0f));
                        }
                        forbidPaint.setStrokeWidth(AndroidUtilities.dp(1.66f));
                        forbidPaint.setColor(getThemedColor(Theme.key_switchTrack));
                        canvas.drawCircle(i8, centerY, AndroidUtilities.dp(9.0f), forbidPaint);
                        return;
                    }
                    int i22 = i8;
                    if (f15 != 0.0f) {
                        if (this.checkedText != null) {
                            if (this.textPaint == null) {
                                i9 = 1;
                                TextPaint textPaint = new TextPaint(1);
                                this.textPaint = textPaint;
                                textPaint.setTypeface(AndroidUtilities.bold());
                            } else {
                                i9 = 1;
                            }
                            int length = this.checkedText.length();
                            if (length == 0 || length == i9 || length == 2) {
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
                            float f16 = i22;
                            canvas.scale(f15, f6, f16, centerY);
                            String str = this.checkedText;
                            canvas.drawText(str, f16 - (this.textPaint.measureText(str) / 2.0f), AndroidUtilities.dp(f10), this.textPaint);
                            canvas.restore();
                            return;
                        }
                        this.path.reset();
                        int i23 = this.backgroundType;
                        if (i23 == -1) {
                            f8 = 1.4f;
                        } else if (i23 != 5) {
                            f7 = 9.0f;
                            f8 = 1.0f;
                            float dp4 = AndroidUtilities.dp(f7 * f8) * f15;
                            float dp5 = AndroidUtilities.dp(f8 * 4.0f) * f15;
                            int dp6 = centerY + AndroidUtilities.dp(4.0f);
                            float sqrt = (float) Math.sqrt((dp5 * dp5) / 2.0f);
                            float dp7 = i22 - AndroidUtilities.dp(1.5f);
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
                        float dp72 = i22 - AndroidUtilities.dp(1.5f);
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
                paint3 = paint;
                i7 = this.enabled ? Theme.key_checkbox : Theme.key_checkboxDisabled;
            }
            paint3.setColor(getThemedColor(i7));
            if (this.forbidden) {
            }
            if (this.useDefaultCheck) {
            }
            this.checkPaint.setColor(getThemedColor(Theme.key_checkboxCheck));
            if (this.alpha < f6) {
                this.checkPaint.setColor(ColorUtils.blendARGB(paint.getColor(), this.checkPaint.getColor(), this.alpha));
            }
            if (this.backgroundType == -1) {
            }
            if (!this.forbidden) {
            }
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
        paint2.setColor(i3);
        if (this.drawUnchecked) {
            if (i14 != 8) {
            }
            f11 = centerX;
            f12 = centerY;
            canvas.drawCircle(f11, f12, f - AndroidUtilities.dp(1.5f), this.backgroundPaint);
        }
        paint.setColor(getThemedColor(this.checkColorKey));
        i5 = this.backgroundType;
        if (i5 != -1) {
        }
        i6 = centerX;
        f4 = f2;
        f5 = f;
        if (f3 > 0.0f) {
        }
    }

    public float getProgress() {
        return this.progress;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void onAttachedToWindow() {
        this.attachedToWindow = true;
    }

    public void onDetachedFromWindow() {
        this.attachedToWindow = false;
    }

    public void setAlpha(float f) {
        this.alpha = f;
        invalidate();
    }

    public void setBackgroundAlpha(float f) {
        this.backgroundAlpha = f;
    }

    public void setBackgroundColor(int i) {
        this.backgroundColor = i;
        invalidate();
    }

    public void setBackgroundDrawable(Theme.MessageDrawable messageDrawable) {
        this.messageDrawable = messageDrawable;
    }

    public void setBackgroundType(int i) {
        Paint paint2;
        float f;
        int i2;
        this.backgroundType = i;
        if (i != 12 && i != 13) {
            if (i == 4 || i == 5) {
                this.backgroundPaint.setStrokeWidth(AndroidUtilities.dp(1.9f));
                if (i != 5) {
                    return;
                }
                paint2 = this.checkPaint;
            } else if (i == 3) {
                paint2 = this.backgroundPaint;
                f = 3.0f;
            } else if (i == 0) {
                return;
            } else {
                paint2 = this.backgroundPaint;
            }
            i2 = AndroidUtilities.dp(1.5f);
            paint2.setStrokeWidth(i2);
        }
        paint2 = this.backgroundPaint;
        f = 1.0f;
        i2 = AndroidUtilities.dp(f);
        paint2.setStrokeWidth(i2);
    }

    public void setBounds(int i, int i2, int i3, int i4) {
        android.graphics.Rect rect = this.bounds;
        rect.left = i;
        rect.top = i2;
        rect.right = i + i3;
        rect.bottom = i2 + i4;
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

    public void setChecked(boolean z, boolean z2) {
        setChecked(-1, z, z2);
    }

    public void setCirclePaintProvider(GenericProvider genericProvider) {
        this.circlePaintProvider = genericProvider;
    }

    public void setColor(int i, int i2, int i3) {
        this.backgroundColorKey = i;
        this.background2ColorKey = i2;
        this.checkColorKey = i3;
        invalidate();
    }

    public void setDrawUnchecked(boolean z) {
        this.drawUnchecked = z;
    }

    public void setEnabled(boolean z) {
        this.enabled = z;
    }

    public void setForbidden(boolean z) {
        if (this.forbidden == z) {
            return;
        }
        this.forbidden = z;
        invalidate();
    }

    public void setNum(int i) {
        String str;
        if (i < 0) {
            str = this.checkAnimator == null ? null : null;
            invalidate();
        }
        str = "" + (i + 1);
        this.checkedText = str;
        invalidate();
    }

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

    public void setProgressDelegate(ProgressDelegate progressDelegate) {
        this.progressDelegate = progressDelegate;
    }

    public void setResourcesProvider(Theme.ResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
    }

    public void setUseDefaultCheck(boolean z) {
        this.useDefaultCheck = z;
    }
}
