package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.annotation.Keep;
import org.telegram.messenger.AndroidUtilities;
/* loaded from: classes3.dex */
public class CheckBox extends View {
    private static Paint backgroundPaint;
    private static Paint eraser;
    private static Paint eraser2;
    private static Paint paint;
    private boolean attachedToWindow;
    private Canvas bitmapCanvas;
    private ObjectAnimator checkAnimator;
    private Bitmap checkBitmap;
    private Canvas checkCanvas;
    private Drawable checkDrawable;
    private int checkOffset;
    private String checkedText;
    private int color;
    private boolean drawBackground;
    private Bitmap drawBitmap;
    private boolean hasBorder;
    private boolean isCheckAnimation;
    private boolean isChecked;
    private float progress;
    private int size;
    private TextPaint textPaint;

    public CheckBox(Context context, int i) {
        super(context);
        this.isCheckAnimation = true;
        this.size = 22;
        if (paint == null) {
            paint = new Paint(1);
            Paint paint2 = new Paint(1);
            eraser = paint2;
            paint2.setColor(0);
            Paint paint3 = eraser;
            PorterDuff.Mode mode = PorterDuff.Mode.CLEAR;
            paint3.setXfermode(new PorterDuffXfermode(mode));
            Paint paint4 = new Paint(1);
            eraser2 = paint4;
            paint4.setColor(0);
            Paint paint5 = eraser2;
            Paint.Style style = Paint.Style.STROKE;
            paint5.setStyle(style);
            eraser2.setXfermode(new PorterDuffXfermode(mode));
            Paint paint6 = new Paint(1);
            backgroundPaint = paint6;
            paint6.setColor(-1);
            backgroundPaint.setStyle(style);
        }
        eraser2.setStrokeWidth(AndroidUtilities.dp(28.0f));
        backgroundPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        TextPaint textPaint = new TextPaint(1);
        this.textPaint = textPaint;
        textPaint.setTextSize(AndroidUtilities.dp(18.0f));
        this.textPaint.setTypeface(AndroidUtilities.bold());
        this.checkDrawable = context.getResources().getDrawable(i).mutate();
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        super.setVisibility(i);
        if (i == 0 && this.drawBitmap == null) {
            try {
                int dp = AndroidUtilities.dp(this.size);
                int dp2 = AndroidUtilities.dp(this.size);
                Bitmap.Config config = Bitmap.Config.ARGB_4444;
                this.drawBitmap = Bitmap.createBitmap(dp, dp2, config);
                this.bitmapCanvas = new Canvas(this.drawBitmap);
                this.checkBitmap = Bitmap.createBitmap(AndroidUtilities.dp(this.size), AndroidUtilities.dp(this.size), config);
                this.checkCanvas = new Canvas(this.checkBitmap);
            } catch (Throwable unused) {
            }
        }
    }

    @Keep
    public void setProgress(float f) {
        if (this.progress == f) {
            return;
        }
        this.progress = f;
        invalidate();
    }

    public void setDrawBackground(boolean z) {
        this.drawBackground = z;
    }

    public void setHasBorder(boolean z) {
        this.hasBorder = z;
    }

    public void setCheckOffset(int i) {
        this.checkOffset = i;
    }

    public void setSize(int i) {
        this.size = i;
        if (i == 40) {
            this.textPaint.setTextSize(AndroidUtilities.dp(24.0f));
        }
    }

    public void setStrokeWidth(int i) {
        backgroundPaint.setStrokeWidth(i);
    }

    @Keep
    public float getProgress() {
        return this.progress;
    }

    public void setColor(int i, int i2) {
        this.color = i;
        this.checkDrawable.setColorFilter(new PorterDuffColorFilter(i2, PorterDuff.Mode.MULTIPLY));
        this.textPaint.setColor(i2);
        invalidate();
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        this.color = i;
        invalidate();
    }

    public void setCheckColor(int i) {
        this.checkDrawable.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
        this.textPaint.setColor(i);
        invalidate();
    }

    private void cancelCheckAnimator() {
        ObjectAnimator objectAnimator = this.checkAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
            this.checkAnimator = null;
        }
    }

    private void animateToCheckedState(boolean z) {
        this.isCheckAnimation = z;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "progress", z ? 1.0f : 0.0f);
        this.checkAnimator = ofFloat;
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.CheckBox.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (animator.equals(CheckBox.this.checkAnimator)) {
                    CheckBox.this.checkAnimator = null;
                }
                if (CheckBox.this.isChecked) {
                    return;
                }
                CheckBox.this.checkedText = null;
            }
        });
        this.checkAnimator.setDuration(300L);
        this.checkAnimator.start();
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attachedToWindow = true;
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attachedToWindow = false;
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
    }

    public void setChecked(boolean z, boolean z2) {
        setChecked(-1, z, z2);
    }

    public void setNum(int i) {
        if (i >= 0) {
            this.checkedText = "" + (i + 1);
        } else if (this.checkAnimator == null) {
            this.checkedText = null;
        }
        invalidate();
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

    public boolean isChecked() {
        return this.isChecked;
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00ca  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x010d  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x013b  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        float dp;
        String str;
        if (getVisibility() != 0 || this.drawBitmap == null || this.checkBitmap == null) {
            return;
        }
        if (this.drawBackground || this.progress != 0.0f) {
            eraser2.setStrokeWidth(AndroidUtilities.dp(this.size + 6));
            this.drawBitmap.eraseColor(0);
            float measuredWidth = getMeasuredWidth() / 2;
            float f = this.progress;
            float f2 = f >= 0.5f ? 1.0f : f / 0.5f;
            float f3 = f < 0.5f ? 0.0f : (f - 0.5f) / 0.5f;
            if (!this.isCheckAnimation) {
                f = 1.0f - f;
            }
            if (f < 0.2f) {
                dp = (AndroidUtilities.dp(2.0f) * f) / 0.2f;
            } else {
                if (f < 0.4f) {
                    dp = AndroidUtilities.dp(2.0f) - ((AndroidUtilities.dp(2.0f) * (f - 0.2f)) / 0.2f);
                }
                if (this.drawBackground) {
                    paint.setColor(1140850688);
                    canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, measuredWidth - AndroidUtilities.dp(1.0f), paint);
                    canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, measuredWidth - AndroidUtilities.dp(1.0f), backgroundPaint);
                }
                paint.setColor(this.color);
                if (this.hasBorder) {
                    measuredWidth -= AndroidUtilities.dp(2.0f);
                }
                this.bitmapCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, measuredWidth, paint);
                this.bitmapCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, measuredWidth * (1.0f - f2), eraser);
                canvas.drawBitmap(this.drawBitmap, 0.0f, 0.0f, (Paint) null);
                this.checkBitmap.eraseColor(0);
                if (this.checkedText == null) {
                    this.checkCanvas.drawText(this.checkedText, (getMeasuredWidth() - ((int) Math.ceil(this.textPaint.measureText(str)))) / 2, AndroidUtilities.dp(this.size == 40 ? 28.0f : 21.0f), this.textPaint);
                } else {
                    int intrinsicWidth = this.checkDrawable.getIntrinsicWidth();
                    int intrinsicHeight = this.checkDrawable.getIntrinsicHeight();
                    int measuredWidth2 = (getMeasuredWidth() - intrinsicWidth) / 2;
                    int measuredHeight = (getMeasuredHeight() - intrinsicHeight) / 2;
                    Drawable drawable = this.checkDrawable;
                    int i = this.checkOffset;
                    drawable.setBounds(measuredWidth2, measuredHeight + i, intrinsicWidth + measuredWidth2, measuredHeight + intrinsicHeight + i);
                    this.checkDrawable.draw(this.checkCanvas);
                }
                this.checkCanvas.drawCircle((getMeasuredWidth() / 2) - AndroidUtilities.dp(2.5f), (getMeasuredHeight() / 2) + AndroidUtilities.dp(4.0f), ((getMeasuredWidth() + AndroidUtilities.dp(6.0f)) / 2) * (1.0f - f3), eraser2);
                canvas.drawBitmap(this.checkBitmap, 0.0f, 0.0f, (Paint) null);
            }
            measuredWidth -= dp;
            if (this.drawBackground) {
            }
            paint.setColor(this.color);
            if (this.hasBorder) {
            }
            this.bitmapCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, measuredWidth, paint);
            this.bitmapCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, measuredWidth * (1.0f - f2), eraser);
            canvas.drawBitmap(this.drawBitmap, 0.0f, 0.0f, (Paint) null);
            this.checkBitmap.eraseColor(0);
            if (this.checkedText == null) {
            }
            this.checkCanvas.drawCircle((getMeasuredWidth() / 2) - AndroidUtilities.dp(2.5f), (getMeasuredHeight() / 2) + AndroidUtilities.dp(4.0f), ((getMeasuredWidth() + AndroidUtilities.dp(6.0f)) / 2) * (1.0f - f3), eraser2);
            canvas.drawBitmap(this.checkBitmap, 0.0f, 0.0f, (Paint) null);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("android.widget.CheckBox");
        accessibilityNodeInfo.setCheckable(true);
        accessibilityNodeInfo.setChecked(this.isChecked);
    }
}
