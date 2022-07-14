package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes5.dex */
public class CrossOutDrawable extends Drawable {
    int color;
    String colorKey;
    boolean cross;
    Drawable iconDrawable;
    private float lenOffsetBottom;
    private float lenOffsetTop;
    float progress;
    private float xOffset;
    final Paint xRefPaint;
    RectF rectF = new RectF();
    Paint paint = new Paint(1);

    public CrossOutDrawable(Context context, int iconRes, String colorKey) {
        Paint paint = new Paint(1);
        this.xRefPaint = paint;
        this.iconDrawable = ContextCompat.getDrawable(context, iconRes);
        this.colorKey = colorKey;
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(AndroidUtilities.dpf2(1.7f));
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(-16777216);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(AndroidUtilities.dpf2(2.5f));
    }

    public void setCrossOut(boolean cross, boolean animated) {
        if (this.cross != cross) {
            this.cross = cross;
            float f = 0.0f;
            if (!animated) {
                if (cross) {
                    f = 1.0f;
                }
                this.progress = f;
            } else {
                if (!cross) {
                    f = 1.0f;
                }
                this.progress = f;
            }
            invalidateSelf();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0039  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0062  */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        int newColor;
        boolean z = this.cross;
        if (z) {
            float f = this.progress;
            if (f != 1.0f) {
                this.progress = f + 0.10666667f;
                invalidateSelf();
                if (this.progress > 1.0f) {
                    this.progress = 1.0f;
                }
                String str = this.colorKey;
                newColor = str != null ? -1 : Theme.getColor(str);
                if (this.color != newColor) {
                    this.color = newColor;
                    this.paint.setColor(newColor);
                    this.iconDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.MULTIPLY));
                }
                if (this.progress != 0.0f) {
                    this.iconDrawable.draw(canvas);
                    return;
                }
                this.rectF.set(this.iconDrawable.getBounds());
                canvas.saveLayerAlpha(this.rectF, 255, 31);
                this.iconDrawable.draw(canvas);
                float startX = this.rectF.left + AndroidUtilities.dpf2(4.5f) + this.xOffset + this.lenOffsetTop;
                float startY = ((this.rectF.top + AndroidUtilities.dpf2(4.5f)) - AndroidUtilities.dp(1.0f)) + this.lenOffsetTop;
                float stopX = ((this.rectF.right - AndroidUtilities.dp(3.0f)) + this.xOffset) - this.lenOffsetBottom;
                float stopY = ((this.rectF.bottom - AndroidUtilities.dp(1.0f)) - AndroidUtilities.dp(3.0f)) - this.lenOffsetBottom;
                if (this.cross) {
                    float f2 = this.progress;
                    stopX = startX + ((stopX - startX) * f2);
                    stopY = startY + ((stopY - startY) * f2);
                } else {
                    float f3 = this.progress;
                    startX += (stopX - startX) * (1.0f - f3);
                    startY += (stopY - startY) * (1.0f - f3);
                }
                float f4 = startX;
                float f5 = stopX;
                canvas.drawLine(f4, startY - this.paint.getStrokeWidth(), f5, stopY - this.paint.getStrokeWidth(), this.xRefPaint);
                canvas.drawLine(f4, startY, f5, stopY, this.paint);
                canvas.restore();
                return;
            }
        }
        if (!z) {
            float f6 = this.progress;
            if (f6 != 0.0f) {
                this.progress = f6 - 0.10666667f;
                invalidateSelf();
                if (this.progress < 0.0f) {
                    this.progress = 0.0f;
                }
            }
        }
        String str2 = this.colorKey;
        if (str2 != null) {
        }
        if (this.color != newColor) {
        }
        if (this.progress != 0.0f) {
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        this.iconDrawable.setBounds(left, top, right, bottom);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.iconDrawable.getIntrinsicHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.iconDrawable.getIntrinsicWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    public void setColorKey(String colorKey) {
        this.colorKey = colorKey;
    }

    public void setOffsets(float xOffset, float lenOffsetTop, float lenOffsetBottom) {
        this.xOffset = xOffset;
        this.lenOffsetTop = lenOffsetTop;
        this.lenOffsetBottom = lenOffsetBottom;
        invalidateSelf();
    }

    public void setStrokeWidth(float w) {
        this.paint.setStrokeWidth(w);
        this.xRefPaint.setStrokeWidth(1.47f * w);
    }

    public float getProgress() {
        return this.progress;
    }
}
