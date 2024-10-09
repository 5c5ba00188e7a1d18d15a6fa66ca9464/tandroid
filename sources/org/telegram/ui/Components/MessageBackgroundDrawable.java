package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes3.dex */
public class MessageBackgroundDrawable extends Drawable {
    private boolean animationInProgress;
    private float currentAnimationProgress;
    private float finalRadius;
    private boolean isSelected;
    private long lastAnimationTime;
    private long lastTouchTime;
    private View parentView;
    private Paint paint = new Paint(1);
    private Paint customPaint = null;
    private float touchX = -1.0f;
    private float touchY = -1.0f;
    private float touchOverrideX = -1.0f;
    private float touchOverrideY = -1.0f;

    public MessageBackgroundDrawable(View view) {
        this.parentView = view;
    }

    private void calcRadius() {
        android.graphics.Rect bounds = getBounds();
        float centerX = bounds.centerX();
        float f = bounds.left - centerX;
        float centerY = bounds.top - bounds.centerY();
        this.finalRadius = (float) Math.ceil(Math.sqrt((f * f) + (centerY * centerY)));
    }

    private void invalidate() {
        View view = this.parentView;
        if (view != null) {
            view.invalidate();
            if (this.parentView.getParent() != null) {
                ((ViewGroup) this.parentView.getParent()).invalidate();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0049, code lost:
    
        if (r6 >= 0.0f) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0056, code lost:
    
        if (r6 >= 0.0f) goto L26;
     */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        float f;
        float f2 = this.currentAnimationProgress;
        if (f2 == 1.0f) {
            android.graphics.Rect bounds = getBounds();
            Paint paint = this.customPaint;
            if (paint == null) {
                paint = this.paint;
            }
            canvas.drawRect(bounds, paint);
        } else if (f2 != 0.0f) {
            float interpolation = this.isSelected ? CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(f2) : 1.0f - CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(1.0f - f2);
            android.graphics.Rect bounds2 = getBounds();
            float centerX = bounds2.centerX();
            float centerY = bounds2.centerY();
            float f3 = this.touchOverrideX;
            if (f3 >= 0.0f) {
                f = this.touchOverrideY;
            }
            f3 = this.touchX;
            if (f3 >= 0.0f) {
                f = this.touchY;
            }
            f = centerY;
            f3 = centerX;
            float f4 = 1.0f - interpolation;
            float f5 = centerX + ((f3 - centerX) * f4);
            float f6 = centerY + (f4 * (f - centerY));
            float f7 = this.finalRadius * interpolation;
            Paint paint2 = this.customPaint;
            if (paint2 == null) {
                paint2 = this.paint;
            }
            canvas.drawCircle(f5, f6, f7, paint2);
        }
        if (this.animationInProgress) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j = elapsedRealtime - this.lastAnimationTime;
            if (j > 20) {
                j = 17;
            }
            this.lastAnimationTime = elapsedRealtime;
            if (this.isSelected) {
                float f8 = this.currentAnimationProgress + (((float) j) / 240.0f);
                this.currentAnimationProgress = f8;
                if (f8 >= 1.0f) {
                    this.currentAnimationProgress = 1.0f;
                    this.touchX = -1.0f;
                    this.touchY = -1.0f;
                    this.touchOverrideX = -1.0f;
                    this.touchOverrideY = -1.0f;
                    this.animationInProgress = false;
                }
                invalidate();
            }
            float f9 = this.currentAnimationProgress - (((float) j) / 240.0f);
            this.currentAnimationProgress = f9;
            if (f9 <= 0.0f) {
                this.currentAnimationProgress = 0.0f;
                this.touchX = -1.0f;
                this.touchY = -1.0f;
                this.touchOverrideX = -1.0f;
                this.touchOverrideY = -1.0f;
                this.animationInProgress = false;
            }
            invalidate();
        }
    }

    public long getLastTouchTime() {
        return this.lastTouchTime;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    public float getTouchX() {
        return this.touchX;
    }

    public float getTouchY() {
        return this.touchY;
    }

    public boolean isAnimationInProgress() {
        return this.animationInProgress;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.paint.setAlpha(i);
    }

    @Override // android.graphics.drawable.Drawable
    public void setBounds(int i, int i2, int i3, int i4) {
        super.setBounds(i, i2, i3, i4);
        calcRadius();
    }

    @Override // android.graphics.drawable.Drawable
    public void setBounds(android.graphics.Rect rect) {
        super.setBounds(rect);
        calcRadius();
    }

    public void setColor(int i) {
        this.paint.setColor(i);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }

    public void setCustomPaint(Paint paint) {
        this.customPaint = paint;
    }

    public void setSelected(boolean z, boolean z2) {
        if (this.isSelected == z) {
            if (this.animationInProgress == z2 || z2) {
                return;
            }
            this.currentAnimationProgress = z ? 1.0f : 0.0f;
            this.animationInProgress = false;
            return;
        }
        this.isSelected = z;
        this.animationInProgress = z2;
        if (z2) {
            this.lastAnimationTime = SystemClock.elapsedRealtime();
        } else {
            this.currentAnimationProgress = z ? 1.0f : 0.0f;
        }
        calcRadius();
        invalidate();
    }

    public void setTouchCoords(float f, float f2) {
        this.touchX = f;
        this.touchY = f2;
        this.lastTouchTime = SystemClock.elapsedRealtime();
    }

    public void setTouchCoordsOverride(float f, float f2) {
        this.touchOverrideX = f;
        this.touchOverrideY = f2;
    }
}
