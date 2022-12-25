package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
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
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.GenericProvider;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class CheckBoxBase {
    private static Paint eraser;
    private static Paint paint;
    private boolean attachedToWindow;
    private Paint backgroundPaint;
    private int backgroundType;
    private ObjectAnimator checkAnimator;
    private Paint checkPaint;
    private String checkedText;
    private boolean isChecked;
    private Theme.MessageDrawable messageDrawable;
    private View parentView;
    private float progress;
    private ProgressDelegate progressDelegate;
    private final Theme.ResourcesProvider resourcesProvider;
    private float size;
    private TextPaint textPaint;
    private boolean useDefaultCheck;
    public android.graphics.Rect bounds = new android.graphics.Rect();
    private RectF rect = new RectF();
    private Path path = new Path();
    private boolean enabled = true;
    private float backgroundAlpha = 1.0f;
    private String checkColorKey = "checkboxCheck";
    private String backgroundColorKey = "chat_serviceBackground";
    private String background2ColorKey = "chat_serviceBackground";
    private boolean drawUnchecked = true;
    private GenericProvider<Void, Paint> circlePaintProvider = CheckBoxBase$$ExternalSyntheticLambda0.INSTANCE;
    public long animationDuration = 200;

    /* loaded from: classes3.dex */
    public interface ProgressDelegate {
        void setProgress(float f);
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
            this.backgroundPaint.setStrokeWidth(AndroidUtilities.dp(1.2f));
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

    /* JADX WARN: Removed duplicated region for block: B:106:0x02c7  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x003c  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x003f  */
    /* JADX WARN: Removed duplicated region for block: B:178:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x005e  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00ea  */
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
        int i4;
        float f3;
        float f4;
        String str2;
        int i5;
        int i6;
        int i7;
        int i8;
        float dp = AndroidUtilities.dp(this.size / 2.0f);
        int i9 = this.backgroundType;
        if (i9 == 12 || i9 == 13) {
            dp = AndroidUtilities.dp(10.0f);
        } else if (i9 != 0 && i9 != 11) {
            f = dp;
            dp -= AndroidUtilities.dp(0.2f);
            float f5 = this.progress;
            f2 = f5 < 0.5f ? 1.0f : f5 / 0.5f;
            centerX = this.bounds.centerX();
            centerY = this.bounds.centerY();
            str = this.backgroundColorKey;
            if (str == null) {
                if (this.drawUnchecked) {
                    int i10 = this.backgroundType;
                    if (i10 == 12 || i10 == 13) {
                        paint.setColor(getThemedColor(str));
                        paint.setAlpha((int) (this.backgroundAlpha * 255.0f));
                        this.backgroundPaint.setColor(getThemedColor(this.checkColorKey));
                    } else if (i10 == 6 || i10 == 7) {
                        paint.setColor(getThemedColor(this.background2ColorKey));
                        this.backgroundPaint.setColor(getThemedColor(this.checkColorKey));
                    } else if (i10 == 10 || i10 == 14) {
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
                    paint2.setColor(AndroidUtilities.getOffsetColor(16777215, getThemedColor(str3), this.progress, this.backgroundAlpha));
                }
            } else if (this.drawUnchecked) {
                paint.setColor(Color.argb((int) (this.backgroundAlpha * 25.0f), 0, 0, 0));
                if (this.backgroundType == 8) {
                    this.backgroundPaint.setColor(getThemedColor(this.background2ColorKey));
                } else {
                    this.backgroundPaint.setColor(AndroidUtilities.getOffsetColor(-1, getThemedColor(this.checkColorKey), this.progress, this.backgroundAlpha));
                }
            } else {
                Paint paint3 = this.backgroundPaint;
                String str4 = this.background2ColorKey;
                if (str4 == null) {
                    str4 = this.checkColorKey;
                }
                paint3.setColor(AndroidUtilities.getOffsetColor(16777215, getThemedColor(str4), this.progress, this.backgroundAlpha));
            }
            if (this.drawUnchecked && (i8 = this.backgroundType) >= 0 && i8 != 12 && i8 != 13) {
                if (i8 != 8 || i8 == 10 || i8 == 14) {
                    canvas.drawCircle(centerX, centerY, f - AndroidUtilities.dp(1.5f), this.backgroundPaint);
                } else if (i8 == 6 || i8 == 7) {
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
            } else if (i == 12 || i == 13) {
                i2 = centerY;
                i3 = centerX;
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
                canvas.drawCircle(i3, i2, (f - AndroidUtilities.dp(1.0f)) * this.backgroundAlpha, this.backgroundPaint);
                this.backgroundPaint.setStyle(Paint.Style.STROKE);
            } else if (i == 0 || i == 11) {
                i2 = centerY;
                i3 = centerX;
                canvas.drawCircle(i3, i2, f, this.backgroundPaint);
            } else {
                float f8 = centerX;
                float f9 = centerY;
                this.rect.set(f8 - dp, f9 - dp, f8 + dp, f9 + dp);
                int i11 = this.backgroundType;
                if (i11 == 6) {
                    i5 = (int) (this.progress * (-360.0f));
                    i6 = 6;
                    i7 = 0;
                } else if (i11 == 1) {
                    i5 = (int) (this.progress * (-270.0f));
                    i6 = 6;
                    i7 = -90;
                } else {
                    i5 = (int) (this.progress * 270.0f);
                    i6 = 6;
                    i7 = 90;
                }
                if (i11 == i6) {
                    int themedColor = getThemedColor("dialogBackground");
                    int alpha = Color.alpha(themedColor);
                    this.backgroundPaint.setColor(themedColor);
                    this.backgroundPaint.setAlpha((int) (alpha * this.progress));
                    i2 = centerY;
                    i3 = centerX;
                    canvas.drawArc(this.rect, i7, i5, false, this.backgroundPaint);
                    int themedColor2 = getThemedColor("chat_attachPhotoBackground");
                    int alpha2 = Color.alpha(themedColor2);
                    this.backgroundPaint.setColor(themedColor2);
                    this.backgroundPaint.setAlpha((int) (alpha2 * this.progress));
                } else {
                    i2 = centerY;
                    i3 = centerX;
                }
                canvas.drawArc(this.rect, i7, i5, false, this.backgroundPaint);
            }
            if (f2 <= 0.0f) {
                float f10 = this.progress;
                float f11 = f10 < 0.5f ? 0.0f : (f10 - 0.5f) / 0.5f;
                int i12 = this.backgroundType;
                if (i12 == 9) {
                    paint.setColor(getThemedColor(this.background2ColorKey));
                } else if (i12 == 11 || i12 == 6 || i12 == 7 || i12 == 10 || (!this.drawUnchecked && this.backgroundColorKey != null)) {
                    paint.setColor(getThemedColor(this.backgroundColorKey));
                } else {
                    paint.setColor(getThemedColor(this.enabled ? "checkbox" : "checkboxDisabled"));
                }
                if (!this.useDefaultCheck && (str2 = this.checkColorKey) != null) {
                    this.checkPaint.setColor(getThemedColor(str2));
                } else {
                    this.checkPaint.setColor(getThemedColor("checkboxCheck"));
                }
                if (this.backgroundType != -1) {
                    float dp2 = AndroidUtilities.dp(this.size) / 2.0f;
                    int save = canvas.save();
                    canvas.translate(i3 - dp2, i2 - dp2);
                    canvas.saveLayerAlpha(0.0f, 0.0f, AndroidUtilities.dp(this.size), AndroidUtilities.dp(this.size), 255, 31);
                    Paint provide = this.circlePaintProvider.provide(null);
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
                if (f11 != 0.0f) {
                    if (this.checkedText != null) {
                        if (this.textPaint == null) {
                            i4 = 1;
                            TextPaint textPaint = new TextPaint(1);
                            this.textPaint = textPaint;
                            textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                        } else {
                            i4 = 1;
                        }
                        int length = this.checkedText.length();
                        if (length == 0 || length == i4 || length == 2) {
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
                        float f12 = i3;
                        canvas.scale(f11, 1.0f, f12, i2);
                        String str5 = this.checkedText;
                        canvas.drawText(str5, f12 - (this.textPaint.measureText(str5) / 2.0f), AndroidUtilities.dp(f4), this.textPaint);
                        canvas.restore();
                        return;
                    }
                    this.path.reset();
                    int i14 = this.backgroundType;
                    float f13 = i14 == -1 ? 1.4f : i14 == 5 ? 0.8f : 1.0f;
                    float dp4 = AndroidUtilities.dp(9.0f * f13) * f11;
                    float dp5 = AndroidUtilities.dp(f13 * 4.0f) * f11;
                    int dp6 = i2 + AndroidUtilities.dp(4.0f);
                    float sqrt = (float) Math.sqrt((dp5 * dp5) / 2.0f);
                    float dp7 = i3 - AndroidUtilities.dp(1.5f);
                    float f14 = dp6;
                    this.path.moveTo(dp7 - sqrt, f14 - sqrt);
                    this.path.lineTo(dp7, f14);
                    float sqrt2 = (float) Math.sqrt((dp4 * dp4) / 2.0f);
                    this.path.lineTo(dp7 + sqrt2, f14 - sqrt2);
                    canvas.drawPath(this.path, this.checkPaint);
                    return;
                }
                return;
            }
            return;
        }
        f = dp;
        float f52 = this.progress;
        if (f52 < 0.5f) {
        }
        centerX = this.bounds.centerX();
        centerY = this.bounds.centerY();
        str = this.backgroundColorKey;
        if (str == null) {
        }
        if (this.drawUnchecked) {
            if (i8 != 8) {
            }
            canvas.drawCircle(centerX, centerY, f - AndroidUtilities.dp(1.5f), this.backgroundPaint);
        }
        paint.setColor(getThemedColor(this.checkColorKey));
        i = this.backgroundType;
        if (i != -1) {
        }
        i2 = centerY;
        i3 = centerX;
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
