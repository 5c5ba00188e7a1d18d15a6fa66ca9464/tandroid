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
/* loaded from: classes3.dex */
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

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public CrossOutDrawable(Context context, int i, String str) {
        Paint paint = new Paint(1);
        this.xRefPaint = paint;
        this.iconDrawable = ContextCompat.getDrawable(context, i);
        this.colorKey = str;
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(AndroidUtilities.dpf2(1.7f));
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(-16777216);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(AndroidUtilities.dpf2(2.5f));
    }

    public void setCrossOut(boolean z, boolean z2) {
        if (this.cross != z) {
            this.cross = z;
            if (!z2) {
                this.progress = z ? 1.0f : 0.0f;
            } else {
                this.progress = z ? 0.0f : 1.0f;
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
        int color;
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
                color = str != null ? -1 : Theme.getColor(str);
                if (this.color != color) {
                    this.color = color;
                    this.paint.setColor(color);
                    this.iconDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
                }
                if (this.progress != 0.0f) {
                    this.iconDrawable.draw(canvas);
                    return;
                }
                this.rectF.set(this.iconDrawable.getBounds());
                canvas.saveLayerAlpha(this.rectF, 255, 31);
                this.iconDrawable.draw(canvas);
                float dpf2 = this.rectF.left + AndroidUtilities.dpf2(4.5f) + this.xOffset + this.lenOffsetTop;
                float dpf22 = ((this.rectF.top + AndroidUtilities.dpf2(4.5f)) - AndroidUtilities.dp(1.0f)) + this.lenOffsetTop;
                float dp = ((this.rectF.right - AndroidUtilities.dp(3.0f)) + this.xOffset) - this.lenOffsetBottom;
                float dp2 = ((this.rectF.bottom - AndroidUtilities.dp(1.0f)) - AndroidUtilities.dp(3.0f)) - this.lenOffsetBottom;
                if (this.cross) {
                    float f2 = this.progress;
                    dp = ((dp - dpf2) * f2) + dpf2;
                    dp2 = ((dp2 - dpf22) * f2) + dpf22;
                } else {
                    float f3 = this.progress;
                    dpf2 += (dp - dpf2) * (1.0f - f3);
                    dpf22 += (dp2 - dpf22) * (1.0f - f3);
                }
                float f4 = dpf2;
                float f5 = dp;
                canvas.drawLine(f4, dpf22 - this.paint.getStrokeWidth(), f5, dp2 - this.paint.getStrokeWidth(), this.xRefPaint);
                float strokeWidth = ((this.xRefPaint.getStrokeWidth() - this.paint.getStrokeWidth()) / 2.0f) + 1.0f;
                canvas.drawLine(f4, dpf22 - strokeWidth, f5, dp2 - strokeWidth, this.xRefPaint);
                canvas.drawLine(f4, dpf22, f5, dp2, this.paint);
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
        if (this.color != color) {
        }
        if (this.progress != 0.0f) {
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setBounds(int i, int i2, int i3, int i4) {
        super.setBounds(i, i2, i3, i4);
        this.iconDrawable.setBounds(i, i2, i3, i4);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.iconDrawable.getIntrinsicHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.iconDrawable.getIntrinsicWidth();
    }

    public void setOffsets(float f, float f2, float f3) {
        this.xOffset = f;
        this.lenOffsetTop = f2;
        this.lenOffsetBottom = f3;
        invalidateSelf();
    }

    public void setStrokeWidth(float f) {
        this.paint.setStrokeWidth(f);
        this.xRefPaint.setStrokeWidth(f * 1.47f);
    }

    public float getProgress() {
        return this.progress;
    }
}
