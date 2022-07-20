package org.telegram.ui.ActionBar;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.MediaActionDrawable;
/* loaded from: classes3.dex */
public class MenuDrawable extends Drawable {
    public static int TYPE_DEFAULT = 0;
    public static int TYPE_UDPATE_AVAILABLE = 1;
    public static int TYPE_UDPATE_DOWNLOADING = 2;
    private int alpha;
    private float animatedDownloadProgress;
    private int backColor;
    private Paint backPaint;
    private int currentAnimationTime;
    private float currentRotation;
    private float downloadProgress;
    private float downloadProgressAnimationStart;
    private float downloadProgressTime;
    private float downloadRadOffset;
    private float finalRotation;
    private int iconColor;
    private DecelerateInterpolator interpolator;
    private long lastFrameTime;
    private boolean miniIcon;
    private Paint paint;
    private int previousType;
    private RectF rect;
    private boolean reverseAngle;
    private boolean rotateToBack;
    private boolean roundCap;
    private int type;
    private float typeAnimationProgress;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public MenuDrawable() {
        this(TYPE_DEFAULT);
    }

    public MenuDrawable(int i) {
        this.paint = new Paint(1);
        this.backPaint = new Paint(1);
        this.rotateToBack = true;
        this.interpolator = new DecelerateInterpolator();
        this.rect = new RectF();
        this.alpha = 255;
        this.paint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        this.backPaint.setStrokeWidth(AndroidUtilities.density * 1.66f);
        this.backPaint.setStrokeCap(Paint.Cap.ROUND);
        this.backPaint.setStyle(Paint.Style.STROKE);
        this.previousType = TYPE_DEFAULT;
        this.type = i;
        this.typeAnimationProgress = 1.0f;
    }

    public void setRotateToBack(boolean z) {
        this.rotateToBack = z;
    }

    public void setRotation(float f, boolean z) {
        this.lastFrameTime = 0L;
        float f2 = this.currentRotation;
        if (f2 == 1.0f) {
            this.reverseAngle = true;
        } else if (f2 == 0.0f) {
            this.reverseAngle = false;
        }
        this.lastFrameTime = 0L;
        if (z) {
            if (f2 < f) {
                this.currentAnimationTime = (int) (f2 * 200.0f);
            } else {
                this.currentAnimationTime = (int) ((1.0f - f2) * 200.0f);
            }
            this.lastFrameTime = SystemClock.elapsedRealtime();
            this.finalRotation = f;
        } else {
            this.currentRotation = f;
            this.finalRotation = f;
        }
        invalidateSelf();
    }

    public void setType(int i, boolean z) {
        int i2 = this.type;
        if (i2 == i) {
            return;
        }
        this.previousType = i2;
        this.type = i;
        if (z) {
            this.typeAnimationProgress = 0.0f;
        } else {
            this.typeAnimationProgress = 1.0f;
        }
        invalidateSelf();
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x0101  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x01c9  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x032d  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x034c  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x03a0  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x03bd  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x03d3  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x03e3  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x040c  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0420  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x042f  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x048b  */
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
        float f6;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        float f7;
        float f8;
        float f9;
        float f10;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.lastFrameTime;
        long j2 = elapsedRealtime - j;
        float f11 = this.currentRotation;
        float f12 = this.finalRotation;
        if (f11 != f12) {
            if (j != 0) {
                int i8 = (int) (this.currentAnimationTime + j2);
                this.currentAnimationTime = i8;
                if (i8 >= 200) {
                    this.currentRotation = f12;
                } else if (f11 < f12) {
                    this.currentRotation = this.interpolator.getInterpolation(i8 / 200.0f) * this.finalRotation;
                } else {
                    this.currentRotation = 1.0f - this.interpolator.getInterpolation(i8 / 200.0f);
                }
            }
            invalidateSelf();
        }
        float f13 = this.typeAnimationProgress;
        if (f13 < 1.0f) {
            float f14 = f13 + (((float) j2) / 200.0f);
            this.typeAnimationProgress = f14;
            if (f14 > 1.0f) {
                this.typeAnimationProgress = 1.0f;
            }
            invalidateSelf();
        }
        this.lastFrameTime = elapsedRealtime;
        canvas.save();
        canvas.translate((getIntrinsicWidth() / 2) - AndroidUtilities.dp(9.0f), getIntrinsicHeight() / 2);
        int i9 = this.iconColor;
        if (i9 == 0) {
            i9 = Theme.getColor("actionBarDefaultIcon");
        }
        int i10 = this.backColor;
        if (i10 == 0) {
            i10 = Theme.getColor("actionBarDefault");
        }
        int i11 = i10;
        int i12 = this.type;
        int i13 = TYPE_DEFAULT;
        if (i12 == i13) {
            if (this.previousType != i13) {
                f10 = AndroidUtilities.dp(9.0f) * (1.0f - this.typeAnimationProgress);
                f9 = AndroidUtilities.dp(7.0f);
                f8 = this.typeAnimationProgress;
            } else {
                f2 = 0.0f;
                f = 0.0f;
                if (!this.rotateToBack) {
                    canvas.rotate(this.currentRotation * (this.reverseAngle ? -180 : 180), AndroidUtilities.dp(9.0f), 0.0f);
                    this.paint.setColor(i9);
                    this.paint.setAlpha(this.alpha);
                    canvas.drawLine(this.roundCap ? AndroidUtilities.dp(0.25f) * this.currentRotation : 0.0f, 0.0f, (AndroidUtilities.dp(18.0f) - (AndroidUtilities.dp(3.0f) * this.currentRotation)) - f2, 0.0f, this.paint);
                    f6 = (AndroidUtilities.dp(5.0f) * (1.0f - Math.abs(this.currentRotation))) - (AndroidUtilities.dp(0.5f) * Math.abs(this.currentRotation));
                    f5 = AndroidUtilities.dp(18.0f) - (AndroidUtilities.dp(2.5f) * Math.abs(this.currentRotation));
                    f4 = AndroidUtilities.dp(5.0f) + (AndroidUtilities.dp(2.0f) * Math.abs(this.currentRotation));
                    f3 = AndroidUtilities.dp(7.5f) * Math.abs(this.currentRotation);
                    if (this.roundCap) {
                        f6 += AndroidUtilities.dp(0.5f) * this.currentRotation;
                        f5 += AndroidUtilities.dp(0.5f) * this.currentRotation;
                        f4 -= AndroidUtilities.dp(0.25f) * this.currentRotation;
                    }
                } else {
                    canvas.rotate(this.currentRotation * (this.reverseAngle ? -225 : 135), AndroidUtilities.dp(9.0f), 0.0f);
                    if (this.miniIcon) {
                        this.paint.setColor(i9);
                        this.paint.setAlpha(this.alpha);
                        canvas.drawLine((AndroidUtilities.dp(1.0f) * this.currentRotation) + (AndroidUtilities.dpf2(2.0f) * (1.0f - Math.abs(this.currentRotation))), 0.0f, ((AndroidUtilities.dpf2(16.0f) * (1.0f - this.currentRotation)) + (AndroidUtilities.dp(17.0f) * this.currentRotation)) - f2, 0.0f, this.paint);
                        f6 = (AndroidUtilities.dpf2(5.0f) * (1.0f - Math.abs(this.currentRotation))) - (AndroidUtilities.dpf2(0.5f) * Math.abs(this.currentRotation));
                        f5 = (AndroidUtilities.dpf2(16.0f) * (1.0f - Math.abs(this.currentRotation))) + (AndroidUtilities.dpf2(9.0f) * Math.abs(this.currentRotation));
                        f4 = AndroidUtilities.dpf2(5.0f) + (AndroidUtilities.dpf2(3.0f) * Math.abs(this.currentRotation));
                        f3 = AndroidUtilities.dpf2(2.0f) + (AndroidUtilities.dpf2(7.0f) * Math.abs(this.currentRotation));
                    } else {
                        int color = Theme.getColor("actionBarActionModeDefaultIcon");
                        i11 = AndroidUtilities.getOffsetColor(i11, Theme.getColor("actionBarActionModeDefault"), this.currentRotation, 1.0f);
                        this.paint.setColor(AndroidUtilities.getOffsetColor(i9, color, this.currentRotation, 1.0f));
                        this.paint.setAlpha(this.alpha);
                        canvas.drawLine(this.currentRotation * AndroidUtilities.dp(1.0f), 0.0f, (AndroidUtilities.dp(18.0f) - (AndroidUtilities.dp(1.0f) * this.currentRotation)) - f2, 0.0f, this.paint);
                        f6 = (AndroidUtilities.dp(5.0f) * (1.0f - Math.abs(this.currentRotation))) - (AndroidUtilities.dp(0.5f) * Math.abs(this.currentRotation));
                        f5 = AndroidUtilities.dp(18.0f) - (AndroidUtilities.dp(9.0f) * Math.abs(this.currentRotation));
                        f4 = AndroidUtilities.dp(5.0f) + (AndroidUtilities.dp(3.0f) * Math.abs(this.currentRotation));
                        f3 = AndroidUtilities.dp(9.0f) * Math.abs(this.currentRotation);
                    }
                }
                float f15 = f6;
                float f16 = f5;
                float f17 = f3;
                int i14 = i11;
                float f18 = f4;
                if (!this.miniIcon) {
                    i = i14;
                    canvas.drawLine(f17, -f18, f16, -f15, this.paint);
                    canvas.drawLine(f17, f18, f16, f15, this.paint);
                } else {
                    i = i14;
                    canvas.drawLine(f17, -f18, f16 - f, -f15, this.paint);
                    canvas.drawLine(f17, f18, f16, f15, this.paint);
                }
                i2 = this.type;
                i3 = TYPE_DEFAULT;
                if ((i2 != i3 && this.currentRotation != 1.0f) || (this.previousType != i3 && this.typeAnimationProgress != 1.0f)) {
                    float dp = AndroidUtilities.dp(17.0f);
                    float f19 = -AndroidUtilities.dp(4.5f);
                    float f20 = AndroidUtilities.density * 5.5f;
                    float f21 = this.currentRotation;
                    canvas.scale(1.0f - f21, 1.0f - f21, dp, f19);
                    if (this.type == TYPE_DEFAULT) {
                        f20 *= 1.0f - this.typeAnimationProgress;
                    }
                    this.backPaint.setColor(i);
                    this.backPaint.setAlpha(this.alpha);
                    canvas.drawCircle(dp, f19, f20, this.paint);
                    i4 = this.type;
                    i5 = TYPE_UDPATE_AVAILABLE;
                    if (i4 != i5 || this.previousType == i5) {
                        this.backPaint.setStrokeWidth(AndroidUtilities.density * 1.66f);
                        if (this.previousType == TYPE_UDPATE_AVAILABLE) {
                            this.backPaint.setAlpha((int) (this.alpha * (1.0f - this.typeAnimationProgress)));
                        } else {
                            this.backPaint.setAlpha(this.alpha);
                        }
                        canvas.drawLine(dp, f19 - AndroidUtilities.dp(2.0f), dp, f19, this.backPaint);
                        canvas.drawPoint(dp, AndroidUtilities.dp(2.5f) + f19, this.backPaint);
                    }
                    i6 = this.type;
                    i7 = TYPE_UDPATE_DOWNLOADING;
                    if (i6 != i7 || this.previousType == i7) {
                        this.backPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
                        if (this.previousType == TYPE_UDPATE_DOWNLOADING) {
                            this.backPaint.setAlpha((int) (this.alpha * (1.0f - this.typeAnimationProgress)));
                        } else {
                            this.backPaint.setAlpha(this.alpha);
                        }
                        float max = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                        this.rect.set(dp - AndroidUtilities.dp(3.0f), f19 - AndroidUtilities.dp(3.0f), dp + AndroidUtilities.dp(3.0f), f19 + AndroidUtilities.dp(3.0f));
                        canvas.drawArc(this.rect, this.downloadRadOffset, max, false, this.backPaint);
                        float f22 = this.downloadRadOffset + (((float) (360 * j2)) / 2500.0f);
                        this.downloadRadOffset = f22;
                        this.downloadRadOffset = MediaActionDrawable.getCircleValue(f22);
                        float f23 = this.downloadProgress;
                        float f24 = this.downloadProgressAnimationStart;
                        f7 = f23 - f24;
                        if (f7 > 0.0f) {
                            float f25 = this.downloadProgressTime + ((float) j2);
                            this.downloadProgressTime = f25;
                            if (f25 >= 200.0f) {
                                this.animatedDownloadProgress = f23;
                                this.downloadProgressAnimationStart = f23;
                                this.downloadProgressTime = 0.0f;
                            } else {
                                this.animatedDownloadProgress = f24 + (f7 * this.interpolator.getInterpolation(f25 / 200.0f));
                            }
                        }
                        invalidateSelf();
                    }
                }
                canvas.restore();
            }
        } else if (this.previousType == i13) {
            f10 = AndroidUtilities.dp(9.0f) * this.typeAnimationProgress * (1.0f - this.currentRotation);
            f9 = AndroidUtilities.dp(7.0f) * this.typeAnimationProgress;
            f8 = this.currentRotation;
        } else {
            f10 = AndroidUtilities.dp(9.0f) * (1.0f - this.currentRotation);
            f9 = AndroidUtilities.dp(7.0f);
            f8 = this.currentRotation;
        }
        f2 = f9 * (1.0f - f8);
        f = f10;
        if (!this.rotateToBack) {
        }
        float f152 = f6;
        float f162 = f5;
        float f172 = f3;
        int i142 = i11;
        float f182 = f4;
        if (!this.miniIcon) {
        }
        i2 = this.type;
        i3 = TYPE_DEFAULT;
        if (i2 != i3) {
            float dp2 = AndroidUtilities.dp(17.0f);
            float f192 = -AndroidUtilities.dp(4.5f);
            float f202 = AndroidUtilities.density * 5.5f;
            float f212 = this.currentRotation;
            canvas.scale(1.0f - f212, 1.0f - f212, dp2, f192);
            if (this.type == TYPE_DEFAULT) {
            }
            this.backPaint.setColor(i);
            this.backPaint.setAlpha(this.alpha);
            canvas.drawCircle(dp2, f192, f202, this.paint);
            i4 = this.type;
            i5 = TYPE_UDPATE_AVAILABLE;
            if (i4 != i5) {
            }
            this.backPaint.setStrokeWidth(AndroidUtilities.density * 1.66f);
            if (this.previousType == TYPE_UDPATE_AVAILABLE) {
            }
            canvas.drawLine(dp2, f192 - AndroidUtilities.dp(2.0f), dp2, f192, this.backPaint);
            canvas.drawPoint(dp2, AndroidUtilities.dp(2.5f) + f192, this.backPaint);
            i6 = this.type;
            i7 = TYPE_UDPATE_DOWNLOADING;
            if (i6 != i7) {
            }
            this.backPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
            if (this.previousType == TYPE_UDPATE_DOWNLOADING) {
            }
            float max2 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
            this.rect.set(dp2 - AndroidUtilities.dp(3.0f), f192 - AndroidUtilities.dp(3.0f), dp2 + AndroidUtilities.dp(3.0f), f192 + AndroidUtilities.dp(3.0f));
            canvas.drawArc(this.rect, this.downloadRadOffset, max2, false, this.backPaint);
            float f222 = this.downloadRadOffset + (((float) (360 * j2)) / 2500.0f);
            this.downloadRadOffset = f222;
            this.downloadRadOffset = MediaActionDrawable.getCircleValue(f222);
            float f232 = this.downloadProgress;
            float f242 = this.downloadProgressAnimationStart;
            f7 = f232 - f242;
            if (f7 > 0.0f) {
            }
            invalidateSelf();
            canvas.restore();
        }
        float dp22 = AndroidUtilities.dp(17.0f);
        float f1922 = -AndroidUtilities.dp(4.5f);
        float f2022 = AndroidUtilities.density * 5.5f;
        float f2122 = this.currentRotation;
        canvas.scale(1.0f - f2122, 1.0f - f2122, dp22, f1922);
        if (this.type == TYPE_DEFAULT) {
        }
        this.backPaint.setColor(i);
        this.backPaint.setAlpha(this.alpha);
        canvas.drawCircle(dp22, f1922, f2022, this.paint);
        i4 = this.type;
        i5 = TYPE_UDPATE_AVAILABLE;
        if (i4 != i5) {
        }
        this.backPaint.setStrokeWidth(AndroidUtilities.density * 1.66f);
        if (this.previousType == TYPE_UDPATE_AVAILABLE) {
        }
        canvas.drawLine(dp22, f1922 - AndroidUtilities.dp(2.0f), dp22, f1922, this.backPaint);
        canvas.drawPoint(dp22, AndroidUtilities.dp(2.5f) + f1922, this.backPaint);
        i6 = this.type;
        i7 = TYPE_UDPATE_DOWNLOADING;
        if (i6 != i7) {
        }
        this.backPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        if (this.previousType == TYPE_UDPATE_DOWNLOADING) {
        }
        float max22 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
        this.rect.set(dp22 - AndroidUtilities.dp(3.0f), f1922 - AndroidUtilities.dp(3.0f), dp22 + AndroidUtilities.dp(3.0f), f1922 + AndroidUtilities.dp(3.0f));
        canvas.drawArc(this.rect, this.downloadRadOffset, max22, false, this.backPaint);
        float f2222 = this.downloadRadOffset + (((float) (360 * j2)) / 2500.0f);
        this.downloadRadOffset = f2222;
        this.downloadRadOffset = MediaActionDrawable.getCircleValue(f2222);
        float f2322 = this.downloadProgress;
        float f2422 = this.downloadProgressAnimationStart;
        f7 = f2322 - f2422;
        if (f7 > 0.0f) {
        }
        invalidateSelf();
        canvas.restore();
    }

    public void setUpdateDownloadProgress(float f, boolean z) {
        if (!z) {
            this.animatedDownloadProgress = f;
            this.downloadProgressAnimationStart = f;
        } else {
            if (this.animatedDownloadProgress > f) {
                this.animatedDownloadProgress = f;
            }
            this.downloadProgressAnimationStart = this.animatedDownloadProgress;
        }
        this.downloadProgress = f;
        this.downloadProgressTime = 0.0f;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        if (this.alpha != i) {
            this.alpha = i;
            this.paint.setAlpha(i);
            this.backPaint.setAlpha(i);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(24.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(24.0f);
    }

    public void setIconColor(int i) {
        this.iconColor = i;
    }

    public void setBackColor(int i) {
        this.backColor = i;
    }

    public void setRoundCap() {
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.roundCap = true;
    }

    public void setMiniIcon(boolean z) {
        this.miniIcon = z;
    }
}
