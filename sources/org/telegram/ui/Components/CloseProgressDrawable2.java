package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
/* loaded from: classes3.dex */
public class CloseProgressDrawable2 extends Drawable {
    private float angle;
    private boolean animating;
    private int currentColor;
    private int globalColorAlpha;
    private long lastFrameTime;
    private Paint paint;
    private RectF rect;
    private int side;

    protected int getCurrentColor() {
        throw null;
    }

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

    public CloseProgressDrawable2() {
        this(2.0f);
    }

    public CloseProgressDrawable2(float f) {
        this.paint = new Paint(1);
        new DecelerateInterpolator();
        this.rect = new RectF();
        this.globalColorAlpha = 255;
        this.paint.setColor(-1);
        this.paint.setStrokeWidth(AndroidUtilities.dp(f));
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setStyle(Paint.Style.STROKE);
        this.side = AndroidUtilities.dp(8.0f);
    }

    public void startAnimation() {
        this.animating = true;
        this.lastFrameTime = System.currentTimeMillis();
        invalidateSelf();
    }

    public void stopAnimation() {
        this.animating = false;
    }

    public boolean isAnimating() {
        return this.animating;
    }

    private void setColor(int i) {
        if (this.currentColor != i) {
            this.globalColorAlpha = Color.alpha(i);
            this.paint.setColor(ColorUtils.setAlphaComponent(i, 255));
        }
    }

    public void setSide(int i) {
        this.side = i;
    }

    /* JADX WARN: Removed duplicated region for block: B:72:0x0132  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0145  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0159  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x016d  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01a9  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01b3  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01b5  */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        long currentTimeMillis = System.currentTimeMillis();
        setColor(getCurrentColor());
        long j = this.lastFrameTime;
        if (j != 0) {
            long j2 = currentTimeMillis - j;
            boolean z = this.animating;
            if (z || this.angle != 0.0f) {
                float f6 = this.angle + (((float) (j2 * 360)) / 500.0f);
                this.angle = f6;
                if (!z && f6 >= 720.0f) {
                    this.angle = 0.0f;
                } else {
                    this.angle = f6 - (((int) (f6 / 720.0f)) * 720);
                }
                invalidateSelf();
            }
        }
        if (this.globalColorAlpha == 255 || getBounds() == null || getBounds().isEmpty()) {
            canvas.save();
        } else {
            canvas.saveLayerAlpha(getBounds().left, getBounds().top, getBounds().right, getBounds().bottom, this.globalColorAlpha, 31);
        }
        canvas.translate(getIntrinsicWidth() / 2, getIntrinsicHeight() / 2);
        canvas.rotate(-45.0f);
        float f7 = this.angle;
        if (f7 < 0.0f || f7 >= 90.0f) {
            if (f7 < 90.0f || f7 >= 180.0f) {
                if (f7 < 180.0f || f7 >= 270.0f) {
                    if (f7 >= 270.0f && f7 < 360.0f) {
                        f4 = (f7 - 270.0f) / 90.0f;
                    } else if (f7 < 360.0f || f7 >= 450.0f) {
                        if (f7 >= 450.0f && f7 < 540.0f) {
                            f = (f7 - 450.0f) / 90.0f;
                            f3 = 0.0f;
                        } else if (f7 >= 540.0f && f7 < 630.0f) {
                            f3 = (f7 - 540.0f) / 90.0f;
                            f = 1.0f;
                        } else if (f7 < 630.0f || f7 >= 720.0f) {
                            f = 1.0f;
                        } else {
                            f2 = (f7 - 630.0f) / 90.0f;
                            f = 1.0f;
                            f3 = 1.0f;
                        }
                        f2 = 0.0f;
                    } else {
                        f4 = 1.0f - ((f7 - 360.0f) / 90.0f);
                    }
                    f5 = f4;
                    f = 0.0f;
                    f3 = 0.0f;
                    f2 = 0.0f;
                    if (f != 0.0f) {
                        canvas.drawLine(0.0f, 0.0f, 0.0f, this.side * f, this.paint);
                    }
                    if (f3 != 0.0f) {
                        canvas.drawLine((-this.side) * f3, 0.0f, 0.0f, 0.0f, this.paint);
                    }
                    if (f2 != 0.0f) {
                        canvas.drawLine(0.0f, (-this.side) * f2, 0.0f, 0.0f, this.paint);
                    }
                    if (f5 != 1.0f) {
                        int i = this.side;
                        canvas.drawLine(i * f5, 0.0f, i, 0.0f, this.paint);
                    }
                    canvas.restore();
                    int centerX = getBounds().centerX();
                    int centerY = getBounds().centerY();
                    RectF rectF = this.rect;
                    int i2 = this.side;
                    rectF.set(centerX - i2, centerY - i2, centerX + i2, centerY + i2);
                    RectF rectF2 = this.rect;
                    float f8 = this.angle;
                    canvas.drawArc(rectF2, (f8 >= 360.0f ? f8 - 360.0f : 0.0f) - 45.0f, f8 >= 360.0f ? f8 : 720.0f - f8, false, this.paint);
                    this.lastFrameTime = currentTimeMillis;
                }
                f2 = 1.0f - ((f7 - 180.0f) / 90.0f);
                f = 0.0f;
                f3 = 0.0f;
                f5 = 0.0f;
                if (f != 0.0f) {
                }
                if (f3 != 0.0f) {
                }
                if (f2 != 0.0f) {
                }
                if (f5 != 1.0f) {
                }
                canvas.restore();
                int centerX2 = getBounds().centerX();
                int centerY2 = getBounds().centerY();
                RectF rectF3 = this.rect;
                int i22 = this.side;
                rectF3.set(centerX2 - i22, centerY2 - i22, centerX2 + i22, centerY2 + i22);
                RectF rectF22 = this.rect;
                float f82 = this.angle;
                canvas.drawArc(rectF22, (f82 >= 360.0f ? f82 - 360.0f : 0.0f) - 45.0f, f82 >= 360.0f ? f82 : 720.0f - f82, false, this.paint);
                this.lastFrameTime = currentTimeMillis;
            }
            f3 = 1.0f - ((f7 - 90.0f) / 90.0f);
            f = 0.0f;
            f2 = 1.0f;
            f5 = 0.0f;
            if (f != 0.0f) {
            }
            if (f3 != 0.0f) {
            }
            if (f2 != 0.0f) {
            }
            if (f5 != 1.0f) {
            }
            canvas.restore();
            int centerX22 = getBounds().centerX();
            int centerY22 = getBounds().centerY();
            RectF rectF32 = this.rect;
            int i222 = this.side;
            rectF32.set(centerX22 - i222, centerY22 - i222, centerX22 + i222, centerY22 + i222);
            RectF rectF222 = this.rect;
            float f822 = this.angle;
            canvas.drawArc(rectF222, (f822 >= 360.0f ? f822 - 360.0f : 0.0f) - 45.0f, f822 >= 360.0f ? f822 : 720.0f - f822, false, this.paint);
            this.lastFrameTime = currentTimeMillis;
        }
        f = 1.0f - (f7 / 90.0f);
        f3 = 1.0f;
        f2 = 1.0f;
        f5 = 0.0f;
        if (f != 0.0f) {
        }
        if (f3 != 0.0f) {
        }
        if (f2 != 0.0f) {
        }
        if (f5 != 1.0f) {
        }
        canvas.restore();
        int centerX222 = getBounds().centerX();
        int centerY222 = getBounds().centerY();
        RectF rectF322 = this.rect;
        int i2222 = this.side;
        rectF322.set(centerX222 - i2222, centerY222 - i2222, centerX222 + i2222, centerY222 + i2222);
        RectF rectF2222 = this.rect;
        float f8222 = this.angle;
        canvas.drawArc(rectF2222, (f8222 >= 360.0f ? f8222 - 360.0f : 0.0f) - 45.0f, f8222 >= 360.0f ? f8222 : 720.0f - f8222, false, this.paint);
        this.lastFrameTime = currentTimeMillis;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(24.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(24.0f);
    }
}
