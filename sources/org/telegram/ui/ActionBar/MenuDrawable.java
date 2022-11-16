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

    /* JADX WARN: Removed duplicated region for block: B:33:0x0101  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x037b  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x03ee  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x040b  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x045a  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x046e  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x04d9  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x047d  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0421  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0431  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x039a  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0217  */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        float dp;
        float dp2;
        float f;
        float f2;
        float f3;
        float dp3;
        float dp4;
        float dp5;
        float dp6;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        float f4;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.lastFrameTime;
        long j2 = elapsedRealtime - j;
        float f5 = this.currentRotation;
        float f6 = this.finalRotation;
        if (f5 != f6) {
            if (j != 0) {
                int i8 = (int) (this.currentAnimationTime + j2);
                this.currentAnimationTime = i8;
                if (i8 >= 200) {
                    this.currentRotation = f6;
                } else if (f5 < f6) {
                    this.currentRotation = this.interpolator.getInterpolation(i8 / 200.0f) * this.finalRotation;
                } else {
                    this.currentRotation = 1.0f - this.interpolator.getInterpolation(i8 / 200.0f);
                }
            }
            invalidateSelf();
        }
        float f7 = this.typeAnimationProgress;
        if (f7 < 1.0f) {
            float f8 = f7 + (((float) j2) / 200.0f);
            this.typeAnimationProgress = f8;
            if (f8 > 1.0f) {
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
                dp = AndroidUtilities.dp(9.0f) * (1.0f - this.typeAnimationProgress);
                dp2 = AndroidUtilities.dp(7.0f);
                f = this.typeAnimationProgress;
            } else {
                f3 = 0.0f;
                f2 = 0.0f;
                if (!this.rotateToBack) {
                    canvas.rotate(this.currentRotation * (this.reverseAngle ? -180 : 180), AndroidUtilities.dp(9.0f), 0.0f);
                    this.paint.setColor(i9);
                    this.paint.setAlpha(this.alpha);
                    canvas.drawLine(this.roundCap ? (AndroidUtilities.dp(0.5f) * this.currentRotation) + ((this.paint.getStrokeWidth() / 2.0f) * (1.0f - this.currentRotation)) : 0.0f, 0.0f, ((AndroidUtilities.dp(18.0f) - (AndroidUtilities.dp(3.0f) * this.currentRotation)) - f3) - (this.roundCap ? (this.paint.getStrokeWidth() / 2.0f) * (1.0f - this.currentRotation) : 0.0f), 0.0f, this.paint);
                    dp3 = (AndroidUtilities.dp(5.0f) * (1.0f - Math.abs(this.currentRotation))) - (AndroidUtilities.dp(0.5f) * Math.abs(this.currentRotation));
                    dp4 = AndroidUtilities.dp(18.0f) - (AndroidUtilities.dp(2.5f) * Math.abs(this.currentRotation));
                    dp5 = AndroidUtilities.dp(5.0f) + (AndroidUtilities.dp(2.0f) * Math.abs(this.currentRotation));
                    dp6 = AndroidUtilities.dp(7.5f) * Math.abs(this.currentRotation);
                    if (this.roundCap) {
                        dp6 += (this.paint.getStrokeWidth() / 2.0f) * (1.0f - this.currentRotation);
                        float dp7 = dp3 + (AndroidUtilities.dp(0.5f) * this.currentRotation);
                        dp4 -= (AndroidUtilities.dp(0.5f) * this.currentRotation) + ((this.paint.getStrokeWidth() / 2.0f) * (1.0f - this.currentRotation));
                        dp5 -= AndroidUtilities.dp(0.75f) * this.currentRotation;
                        dp3 = dp7 + (AndroidUtilities.dp(0.25f) * this.currentRotation);
                    }
                } else {
                    canvas.rotate(this.currentRotation * (this.reverseAngle ? -225 : 135), AndroidUtilities.dp(9.0f), 0.0f);
                    if (this.miniIcon) {
                        this.paint.setColor(i9);
                        this.paint.setAlpha(this.alpha);
                        canvas.drawLine((AndroidUtilities.dp(1.0f) * this.currentRotation) + (AndroidUtilities.dpf2(2.0f) * (1.0f - Math.abs(this.currentRotation))), 0.0f, ((AndroidUtilities.dpf2(16.0f) * (1.0f - this.currentRotation)) + (AndroidUtilities.dp(17.0f) * this.currentRotation)) - f3, 0.0f, this.paint);
                        dp3 = (AndroidUtilities.dpf2(5.0f) * (1.0f - Math.abs(this.currentRotation))) - (AndroidUtilities.dpf2(0.5f) * Math.abs(this.currentRotation));
                        dp4 = (AndroidUtilities.dpf2(16.0f) * (1.0f - Math.abs(this.currentRotation))) + (AndroidUtilities.dpf2(9.0f) * Math.abs(this.currentRotation));
                        dp5 = AndroidUtilities.dpf2(5.0f) + (AndroidUtilities.dpf2(3.0f) * Math.abs(this.currentRotation));
                        dp6 = AndroidUtilities.dpf2(2.0f) + (AndroidUtilities.dpf2(7.0f) * Math.abs(this.currentRotation));
                    } else {
                        int color = Theme.getColor("actionBarActionModeDefaultIcon");
                        i11 = AndroidUtilities.getOffsetColor(i11, Theme.getColor("actionBarActionModeDefault"), this.currentRotation, 1.0f);
                        this.paint.setColor(AndroidUtilities.getOffsetColor(i9, color, this.currentRotation, 1.0f));
                        this.paint.setAlpha(this.alpha);
                        canvas.drawLine(this.currentRotation * AndroidUtilities.dp(1.0f), 0.0f, (AndroidUtilities.dp(18.0f) - (AndroidUtilities.dp(1.0f) * this.currentRotation)) - f3, 0.0f, this.paint);
                        dp3 = (AndroidUtilities.dp(5.0f) * (1.0f - Math.abs(this.currentRotation))) - (AndroidUtilities.dp(0.5f) * Math.abs(this.currentRotation));
                        dp4 = AndroidUtilities.dp(18.0f) - (AndroidUtilities.dp(9.0f) * Math.abs(this.currentRotation));
                        dp5 = AndroidUtilities.dp(5.0f) + (AndroidUtilities.dp(3.0f) * Math.abs(this.currentRotation));
                        dp6 = AndroidUtilities.dp(9.0f) * Math.abs(this.currentRotation);
                    }
                }
                float f9 = dp3;
                float f10 = dp4;
                float f11 = dp6;
                int i14 = i11;
                float f12 = dp5;
                if (!this.miniIcon) {
                    i = i14;
                    canvas.drawLine(f11, -f12, f10, -f9, this.paint);
                    canvas.drawLine(f11, f12, f10, f9, this.paint);
                } else {
                    i = i14;
                    canvas.drawLine(f11, -f12, f10 - f2, -f9, this.paint);
                    canvas.drawLine(f11, f12, f10, f9, this.paint);
                }
                i2 = this.type;
                i3 = TYPE_DEFAULT;
                if ((i2 != i3 && this.currentRotation != 1.0f) || (this.previousType != i3 && this.typeAnimationProgress != 1.0f)) {
                    float dp8 = AndroidUtilities.dp(17.0f);
                    float f13 = -AndroidUtilities.dp(4.5f);
                    float f14 = AndroidUtilities.density * 5.5f;
                    float f15 = this.currentRotation;
                    canvas.scale(1.0f - f15, 1.0f - f15, dp8, f13);
                    if (this.type == TYPE_DEFAULT) {
                        f14 *= 1.0f - this.typeAnimationProgress;
                    }
                    this.backPaint.setColor(i);
                    this.backPaint.setAlpha(this.alpha);
                    canvas.drawCircle(dp8, f13, f14, this.paint);
                    i4 = this.type;
                    i5 = TYPE_UDPATE_AVAILABLE;
                    if (i4 != i5 || this.previousType == i5) {
                        this.backPaint.setStrokeWidth(AndroidUtilities.density * 1.66f);
                        if (this.previousType == TYPE_UDPATE_AVAILABLE) {
                            this.backPaint.setAlpha((int) (this.alpha * (1.0f - this.typeAnimationProgress)));
                        } else {
                            this.backPaint.setAlpha(this.alpha);
                        }
                        canvas.drawLine(dp8, f13 - AndroidUtilities.dp(2.0f), dp8, f13, this.backPaint);
                        canvas.drawPoint(dp8, AndroidUtilities.dp(2.5f) + f13, this.backPaint);
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
                        this.rect.set(dp8 - AndroidUtilities.dp(3.0f), f13 - AndroidUtilities.dp(3.0f), dp8 + AndroidUtilities.dp(3.0f), f13 + AndroidUtilities.dp(3.0f));
                        canvas.drawArc(this.rect, this.downloadRadOffset, max, false, this.backPaint);
                        float f16 = this.downloadRadOffset + (((float) (360 * j2)) / 2500.0f);
                        this.downloadRadOffset = f16;
                        this.downloadRadOffset = MediaActionDrawable.getCircleValue(f16);
                        float f17 = this.downloadProgress;
                        float f18 = this.downloadProgressAnimationStart;
                        f4 = f17 - f18;
                        if (f4 > 0.0f) {
                            float f19 = this.downloadProgressTime + ((float) j2);
                            this.downloadProgressTime = f19;
                            if (f19 >= 200.0f) {
                                this.animatedDownloadProgress = f17;
                                this.downloadProgressAnimationStart = f17;
                                this.downloadProgressTime = 0.0f;
                            } else {
                                this.animatedDownloadProgress = f18 + (f4 * this.interpolator.getInterpolation(f19 / 200.0f));
                            }
                        }
                        invalidateSelf();
                    }
                }
                canvas.restore();
            }
        } else if (this.previousType == i13) {
            dp = AndroidUtilities.dp(9.0f) * this.typeAnimationProgress * (1.0f - this.currentRotation);
            dp2 = AndroidUtilities.dp(7.0f) * this.typeAnimationProgress;
            f = this.currentRotation;
        } else {
            dp = AndroidUtilities.dp(9.0f) * (1.0f - this.currentRotation);
            dp2 = AndroidUtilities.dp(7.0f);
            f = this.currentRotation;
        }
        f3 = dp2 * (1.0f - f);
        f2 = dp;
        if (!this.rotateToBack) {
        }
        float f92 = dp3;
        float f102 = dp4;
        float f112 = dp6;
        int i142 = i11;
        float f122 = dp5;
        if (!this.miniIcon) {
        }
        i2 = this.type;
        i3 = TYPE_DEFAULT;
        if (i2 != i3) {
            float dp82 = AndroidUtilities.dp(17.0f);
            float f132 = -AndroidUtilities.dp(4.5f);
            float f142 = AndroidUtilities.density * 5.5f;
            float f152 = this.currentRotation;
            canvas.scale(1.0f - f152, 1.0f - f152, dp82, f132);
            if (this.type == TYPE_DEFAULT) {
            }
            this.backPaint.setColor(i);
            this.backPaint.setAlpha(this.alpha);
            canvas.drawCircle(dp82, f132, f142, this.paint);
            i4 = this.type;
            i5 = TYPE_UDPATE_AVAILABLE;
            if (i4 != i5) {
            }
            this.backPaint.setStrokeWidth(AndroidUtilities.density * 1.66f);
            if (this.previousType == TYPE_UDPATE_AVAILABLE) {
            }
            canvas.drawLine(dp82, f132 - AndroidUtilities.dp(2.0f), dp82, f132, this.backPaint);
            canvas.drawPoint(dp82, AndroidUtilities.dp(2.5f) + f132, this.backPaint);
            i6 = this.type;
            i7 = TYPE_UDPATE_DOWNLOADING;
            if (i6 != i7) {
            }
            this.backPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
            if (this.previousType == TYPE_UDPATE_DOWNLOADING) {
            }
            float max2 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
            this.rect.set(dp82 - AndroidUtilities.dp(3.0f), f132 - AndroidUtilities.dp(3.0f), dp82 + AndroidUtilities.dp(3.0f), f132 + AndroidUtilities.dp(3.0f));
            canvas.drawArc(this.rect, this.downloadRadOffset, max2, false, this.backPaint);
            float f162 = this.downloadRadOffset + (((float) (360 * j2)) / 2500.0f);
            this.downloadRadOffset = f162;
            this.downloadRadOffset = MediaActionDrawable.getCircleValue(f162);
            float f172 = this.downloadProgress;
            float f182 = this.downloadProgressAnimationStart;
            f4 = f172 - f182;
            if (f4 > 0.0f) {
            }
            invalidateSelf();
            canvas.restore();
        }
        float dp822 = AndroidUtilities.dp(17.0f);
        float f1322 = -AndroidUtilities.dp(4.5f);
        float f1422 = AndroidUtilities.density * 5.5f;
        float f1522 = this.currentRotation;
        canvas.scale(1.0f - f1522, 1.0f - f1522, dp822, f1322);
        if (this.type == TYPE_DEFAULT) {
        }
        this.backPaint.setColor(i);
        this.backPaint.setAlpha(this.alpha);
        canvas.drawCircle(dp822, f1322, f1422, this.paint);
        i4 = this.type;
        i5 = TYPE_UDPATE_AVAILABLE;
        if (i4 != i5) {
        }
        this.backPaint.setStrokeWidth(AndroidUtilities.density * 1.66f);
        if (this.previousType == TYPE_UDPATE_AVAILABLE) {
        }
        canvas.drawLine(dp822, f1322 - AndroidUtilities.dp(2.0f), dp822, f1322, this.backPaint);
        canvas.drawPoint(dp822, AndroidUtilities.dp(2.5f) + f1322, this.backPaint);
        i6 = this.type;
        i7 = TYPE_UDPATE_DOWNLOADING;
        if (i6 != i7) {
        }
        this.backPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        if (this.previousType == TYPE_UDPATE_DOWNLOADING) {
        }
        float max22 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
        this.rect.set(dp822 - AndroidUtilities.dp(3.0f), f1322 - AndroidUtilities.dp(3.0f), dp822 + AndroidUtilities.dp(3.0f), f1322 + AndroidUtilities.dp(3.0f));
        canvas.drawArc(this.rect, this.downloadRadOffset, max22, false, this.backPaint);
        float f1622 = this.downloadRadOffset + (((float) (360 * j2)) / 2500.0f);
        this.downloadRadOffset = f1622;
        this.downloadRadOffset = MediaActionDrawable.getCircleValue(f1622);
        float f1722 = this.downloadProgress;
        float f1822 = this.downloadProgressAnimationStart;
        f4 = f1722 - f1822;
        if (f4 > 0.0f) {
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
