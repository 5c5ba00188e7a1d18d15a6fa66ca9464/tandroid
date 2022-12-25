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

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    public MessageBackgroundDrawable(View view) {
        this.parentView = view;
    }

    public void setColor(int i) {
        this.paint.setColor(i);
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

    private void invalidate() {
        View view = this.parentView;
        if (view != null) {
            view.invalidate();
            if (this.parentView.getParent() != null) {
                ((ViewGroup) this.parentView.getParent()).invalidate();
            }
        }
    }

    private void calcRadius() {
        android.graphics.Rect bounds = getBounds();
        float centerX = bounds.centerX();
        float centerY = bounds.centerY();
        int i = bounds.left;
        int i2 = bounds.top;
        this.finalRadius = (float) Math.ceil(Math.sqrt(((i - centerX) * (i - centerX)) + ((i2 - centerY) * (i2 - centerY))));
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

    public float getTouchX() {
        return this.touchX;
    }

    public float getTouchY() {
        return this.touchY;
    }

    public long getLastTouchTime() {
        return this.lastTouchTime;
    }

    public boolean isAnimationInProgress() {
        return this.animationInProgress;
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

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.paint.setAlpha(i);
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0049, code lost:
        if (r6 >= 0.0f) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0056, code lost:
        if (r6 >= 0.0f) goto L36;
     */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        float interpolation;
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
            if (this.isSelected) {
                interpolation = CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(f2);
            } else {
                interpolation = 1.0f - CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(1.0f - f2);
            }
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
            boolean z = true;
            if (this.isSelected) {
                float f8 = this.currentAnimationProgress + (((float) j) / 240.0f);
                this.currentAnimationProgress = f8;
                if (f8 >= 1.0f) {
                    this.currentAnimationProgress = 1.0f;
                }
                z = false;
            } else {
                float f9 = this.currentAnimationProgress - (((float) j) / 240.0f);
                this.currentAnimationProgress = f9;
                if (f9 <= 0.0f) {
                    this.currentAnimationProgress = 0.0f;
                }
                z = false;
            }
            if (z) {
                this.touchX = -1.0f;
                this.touchY = -1.0f;
                this.touchOverrideX = -1.0f;
                this.touchOverrideY = -1.0f;
                this.animationInProgress = false;
            }
            invalidate();
        }
    }
}
