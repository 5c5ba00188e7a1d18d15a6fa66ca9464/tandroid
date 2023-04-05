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
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes4.dex */
public class CheckBoxBase {
    private static Paint eraser;
    private static Paint forbidPaint;
    private static Paint paint;
    private boolean attachedToWindow;
    private Paint backgroundPaint;
    private int backgroundType;
    private ObjectAnimator checkAnimator;
    private Paint checkPaint;
    private String checkedText;
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
    private String checkColorKey = "checkboxCheck";
    private String backgroundColorKey = "chat_serviceBackground";
    private String background2ColorKey = "chat_serviceBackground";
    private boolean drawUnchecked = true;
    private GenericProvider<Void, Paint> circlePaintProvider = CheckBoxBase$$ExternalSyntheticLambda0.INSTANCE;
    public long animationDuration = 200;

    /* loaded from: classes4.dex */
    public interface ProgressDelegate {
        void setProgress(float f);
    }

    public void setAlpha(float f) {
        this.alpha = f;
        invalidate();
    }

    public CheckBoxBase(View view, int i, Theme.ResourcesProvider resourcesProvider) {
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
        this.checkPaint.setStyle(Paint.Style.STROKE);
        this.checkPaint.setStrokeJoin(Paint.Join.ROUND);
        this.checkPaint.setStrokeWidth(AndroidUtilities.dp(1.9f));
        Paint paint4 = new Paint(1);
        this.backgroundPaint = paint4;
        paint4.setStyle(Paint.Style.STROKE);
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
        float[] fArr = new float[1];
        fArr[0] = z ? 1.0f : 0.0f;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "progress", fArr);
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

    public void setColor(String str, String str2, String str3) {
        this.backgroundColorKey = str;
        this.background2ColorKey = str2;
        this.checkColorKey = str3;
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

    /* JADX WARN: Removed duplicated region for block: B:113:0x02d9  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0038  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0044  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:202:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00f0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        float f;
        float f2;
        int centerX;
        int centerY;
        String str;
        int i;
        int i2;
        int i3;
        Void r7;
        int i4;
        char c;
        float f3;
        float f4;
        String str2;
        int i5;
        int i6;
        int i7;
        float dp = AndroidUtilities.dp(this.size / 2.0f);
        int i8 = this.backgroundType;
        if (i8 == 12 || i8 == 13) {
            dp = AndroidUtilities.dp(10.0f);
        } else if (i8 != 0 && i8 != 11) {
            f = dp;
            dp -= AndroidUtilities.dp(0.2f);
            float f5 = !this.forbidden ? 1.0f : this.progress;
            f2 = f5 < 0.5f ? 1.0f : f5 / 0.5f;
            centerX = this.bounds.centerX();
            centerY = this.bounds.centerY();
            str = this.backgroundColorKey;
            if (str == null) {
                if (this.drawUnchecked) {
                    int i9 = this.backgroundType;
                    if (i9 == 12 || i9 == 13) {
                        paint.setColor(getThemedColor(str));
                        paint.setAlpha((int) (this.backgroundAlpha * 255.0f));
                        this.backgroundPaint.setColor(getThemedColor(this.checkColorKey));
                    } else if (i9 == 6 || i9 == 7) {
                        paint.setColor(getThemedColor(this.background2ColorKey));
                        this.backgroundPaint.setColor(getThemedColor(this.checkColorKey));
                    } else if (i9 == 10 || i9 == 14) {
                        this.backgroundPaint.setColor(getThemedColor(this.background2ColorKey));
                    } else {
                        paint.setColor((Theme.getServiceMessageColor() & 16777215) | 671088640);
                        this.backgroundPaint.setColor(getThemedColor(this.checkColorKey));
                    }
                } else {
                    Paint paint2 = this.backgroundPaint;
                    String str3 = this.background2ColorKey;
                    if (str3 == null) {
                        str3 = this.checkColorKey;
                    }
                    paint2.setColor(AndroidUtilities.getOffsetColor(16777215, getThemedColor(str3), f5, this.backgroundAlpha));
                }
            } else if (this.drawUnchecked) {
                paint.setColor(Color.argb((int) (this.backgroundAlpha * 25.0f), 0, 0, 0));
                if (this.backgroundType == 8) {
                    this.backgroundPaint.setColor(getThemedColor(this.background2ColorKey));
                } else {
                    this.backgroundPaint.setColor(AndroidUtilities.getOffsetColor(-1, getThemedColor(this.checkColorKey), f5, this.backgroundAlpha));
                }
            } else {
                Paint paint3 = this.backgroundPaint;
                String str4 = this.background2ColorKey;
                if (str4 == null) {
                    str4 = this.checkColorKey;
                }
                paint3.setColor(AndroidUtilities.getOffsetColor(16777215, getThemedColor(str4), f5, this.backgroundAlpha));
            }
            if (this.drawUnchecked && (i7 = this.backgroundType) >= 0 && i7 != 12 && i7 != 13) {
                if (i7 != 8 || i7 == 10 || i7 == 14) {
                    canvas.drawCircle(centerX, centerY, f - AndroidUtilities.dp(1.5f), this.backgroundPaint);
                } else if (i7 == 6 || i7 == 7) {
                    float f6 = centerX;
                    float f7 = centerY;
                    canvas.drawCircle(f6, f7, f - AndroidUtilities.dp(1.0f), paint);
                    canvas.drawCircle(f6, f7, f - AndroidUtilities.dp(1.5f), this.backgroundPaint);
                } else {
                    canvas.drawCircle(centerX, centerY, f, paint);
                }
            }
            paint.setColor(getThemedColor(this.checkColorKey));
            i = this.backgroundType;
            if (i != -1 || i == 7 || i == 8 || i == 9 || i == 10 || i == 14) {
                i2 = centerY;
                i3 = centerX;
                r7 = null;
                i4 = 9;
                c = 0;
            } else if (i == 12 || i == 13) {
                i2 = centerY;
                i3 = centerX;
                i4 = 9;
                c = 0;
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
                    r7 = null;
                } else {
                    r7 = null;
                    this.backgroundPaint.setShader(null);
                }
                canvas.drawCircle(i3, i2, (f - AndroidUtilities.dp(1.0f)) * this.backgroundAlpha, this.backgroundPaint);
                this.backgroundPaint.setStyle(Paint.Style.STROKE);
            } else {
                if (i == 0 || i == 11) {
                    i2 = centerY;
                    i3 = centerX;
                    i4 = 9;
                    c = 0;
                    canvas.drawCircle(i3, i2, f, this.backgroundPaint);
                } else {
                    float f8 = centerX;
                    float f9 = centerY;
                    this.rect.set(f8 - dp, f9 - dp, f8 + dp, f9 + dp);
                    int i10 = this.backgroundType;
                    if (i10 == 6) {
                        i5 = (int) ((-360.0f) * f5);
                        i6 = 0;
                    } else if (i10 == 1) {
                        i5 = (int) ((-270.0f) * f5);
                        i6 = -90;
                    } else {
                        int i11 = (int) (270.0f * f5);
                        if (LocaleController.isRTL) {
                            i11 = -i11;
                        }
                        i5 = i11;
                        i6 = 90;
                    }
                    if (i10 == 6) {
                        int themedColor = getThemedColor("dialogBackground");
                        int alpha = Color.alpha(themedColor);
                        this.backgroundPaint.setColor(themedColor);
                        this.backgroundPaint.setAlpha((int) (alpha * f5));
                        c = 0;
                        i4 = 9;
                        i2 = centerY;
                        i3 = centerX;
                        canvas.drawArc(this.rect, i6, i5, false, this.backgroundPaint);
                        int themedColor2 = getThemedColor("chat_attachPhotoBackground");
                        int alpha2 = Color.alpha(themedColor2);
                        this.backgroundPaint.setColor(themedColor2);
                        this.backgroundPaint.setAlpha((int) (alpha2 * f5));
                    } else {
                        i2 = centerY;
                        i3 = centerX;
                        i4 = 9;
                        c = 0;
                    }
                    canvas.drawArc(this.rect, i6, i5, false, this.backgroundPaint);
                }
                r7 = null;
            }
            if (f2 <= 0.0f) {
                float f10 = f5 < 0.5f ? 0.0f : (f5 - 0.5f) / 0.5f;
                int i12 = this.backgroundType;
                if (i12 == i4) {
                    paint.setColor(getThemedColor(this.background2ColorKey));
                } else if (i12 == 11 || i12 == 6 || i12 == 7 || i12 == 10 || ((!this.drawUnchecked && this.backgroundColorKey != null) || i12 == 14)) {
                    paint.setColor(getThemedColor(this.backgroundColorKey));
                } else {
                    paint.setColor(getThemedColor(this.enabled ? "checkbox" : "checkboxDisabled"));
                }
                if (this.forbidden) {
                    paint.setColor(this.backgroundPaint.getColor());
                } else if (this.alpha < 1.0f) {
                    paint.setColor(ColorUtils.blendARGB(this.backgroundPaint.getColor(), paint.getColor(), this.alpha));
                }
                if (!this.useDefaultCheck && (str2 = this.checkColorKey) != null) {
                    this.checkPaint.setColor(getThemedColor(str2));
                } else {
                    this.checkPaint.setColor(getThemedColor("checkboxCheck"));
                }
                if (this.alpha < 1.0f && Theme.isCurrentThemeDark()) {
                    this.checkPaint.setColor(ColorUtils.blendARGB(paint.getColor(), this.checkPaint.getColor(), this.alpha));
                }
                if (this.backgroundType != -1) {
                    float dp2 = AndroidUtilities.dp(this.size) / 2.0f;
                    int save = canvas.save();
                    canvas.translate(i3 - dp2, i2 - dp2);
                    canvas.saveLayerAlpha(0.0f, 0.0f, AndroidUtilities.dp(this.size), AndroidUtilities.dp(this.size), 255, 31);
                    Paint provide = this.circlePaintProvider.provide(r7);
                    int i13 = this.backgroundType;
                    if (i13 == 12 || i13 == 13) {
                        int alpha3 = provide.getAlpha();
                        provide.setAlpha((int) (f2 * 255.0f));
                        canvas.drawCircle(dp2, dp2, f * f2, provide);
                        if (provide != paint) {
                            provide.setAlpha(alpha3);
                        }
                    } else {
                        float dp3 = f - AndroidUtilities.dp(0.5f);
                        canvas.drawCircle(dp2, dp2, dp3, provide);
                        canvas.drawCircle(dp2, dp2, dp3 * (1.0f - f2), eraser);
                    }
                    canvas.restoreToCount(save);
                }
                if (this.forbidden) {
                    if (forbidPaint == null) {
                        Paint paint4 = new Paint(1);
                        forbidPaint = paint4;
                        paint4.setStyle(Paint.Style.STROKE);
                        forbidPaint.setStrokeCap(Paint.Cap.ROUND);
                        forbidPaint.setStrokeJoin(Paint.Join.ROUND);
                        Paint paint5 = forbidPaint;
                        float[] fArr = new float[2];
                        fArr[c] = AndroidUtilities.dp(0.66f);
                        fArr[1] = AndroidUtilities.dp(4.0f);
                        paint5.setPathEffect(new DashPathEffect(fArr, 0.0f));
                    }
                    forbidPaint.setStrokeWidth(AndroidUtilities.dp(1.66f));
                    forbidPaint.setColor(getThemedColor("switchTrack"));
                    canvas.drawCircle(i3, i2, AndroidUtilities.dp(9.0f), forbidPaint);
                    return;
                } else if (f10 != 0.0f) {
                    if (this.checkedText != null) {
                        if (this.textPaint == null) {
                            TextPaint textPaint = new TextPaint(1);
                            this.textPaint = textPaint;
                            textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                        }
                        int length = this.checkedText.length();
                        if (length == 0 || length == 1 || length == 2) {
                            f3 = 14.0f;
                            f4 = 18.0f;
                        } else if (length != 3) {
                            f3 = 8.0f;
                            f4 = 15.75f;
                        } else {
                            f4 = 16.5f;
                            f3 = 10.0f;
                        }
                        this.textPaint.setTextSize(AndroidUtilities.dp(f3));
                        this.textPaint.setColor(getThemedColor(this.checkColorKey));
                        canvas.save();
                        float f11 = i3;
                        canvas.scale(f10, 1.0f, f11, i2);
                        String str5 = this.checkedText;
                        canvas.drawText(str5, f11 - (this.textPaint.measureText(str5) / 2.0f), AndroidUtilities.dp(f4), this.textPaint);
                        canvas.restore();
                        return;
                    }
                    this.path.reset();
                    int i14 = this.backgroundType;
                    float f12 = i14 == -1 ? 1.4f : i14 == 5 ? 0.8f : 1.0f;
                    float dp4 = AndroidUtilities.dp(9.0f * f12) * f10;
                    float dp5 = AndroidUtilities.dp(f12 * 4.0f) * f10;
                    int dp6 = i2 + AndroidUtilities.dp(4.0f);
                    float sqrt = (float) Math.sqrt((dp5 * dp5) / 2.0f);
                    float dp7 = i3 - AndroidUtilities.dp(1.5f);
                    float f13 = dp6;
                    this.path.moveTo(dp7 - sqrt, f13 - sqrt);
                    this.path.lineTo(dp7, f13);
                    float sqrt2 = (float) Math.sqrt((dp4 * dp4) / 2.0f);
                    this.path.lineTo(dp7 + sqrt2, f13 - sqrt2);
                    canvas.drawPath(this.path, this.checkPaint);
                    return;
                } else {
                    return;
                }
            }
            return;
        }
        f = dp;
        if (!this.forbidden) {
        }
        if (f5 < 0.5f) {
        }
        centerX = this.bounds.centerX();
        centerY = this.bounds.centerY();
        str = this.backgroundColorKey;
        if (str == null) {
        }
        if (this.drawUnchecked) {
            if (i7 != 8) {
            }
            canvas.drawCircle(centerX, centerY, f - AndroidUtilities.dp(1.5f), this.backgroundPaint);
        }
        paint.setColor(getThemedColor(this.checkColorKey));
        i = this.backgroundType;
        if (i != -1) {
        }
        i2 = centerY;
        i3 = centerX;
        r7 = null;
        i4 = 9;
        c = 0;
        if (f2 <= 0.0f) {
        }
    }

    public void setCirclePaintProvider(GenericProvider<Void, Paint> genericProvider) {
        this.circlePaintProvider = genericProvider;
    }

    private int getThemedColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
        return color != null ? color.intValue() : Theme.getColor(str);
    }
}
