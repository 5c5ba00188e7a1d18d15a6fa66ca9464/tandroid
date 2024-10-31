package org.telegram.ui.ActionBar;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.Components.MediaActionDrawable;

/* loaded from: classes4.dex */
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

    public MenuDrawable() {
        this(TYPE_DEFAULT);
    }

    public MenuDrawable(int i) {
        this.paint = new Paint(1);
        this.backPaint = new Paint(1);
        this.rotateToBack = true;
        this.interpolator = new DecelerateInterpolator();
        this.rect = new RectF();
        this.alpha = NotificationCenter.playerDidStartPlaying;
        this.paint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        this.backPaint.setStrokeWidth(AndroidUtilities.density * 1.66f);
        this.backPaint.setStrokeCap(Paint.Cap.ROUND);
        this.backPaint.setStyle(Paint.Style.STROKE);
        this.previousType = TYPE_DEFAULT;
        this.type = i;
        this.typeAnimationProgress = 1.0f;
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x0109  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0376  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x03e3  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0400  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x044d  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0461  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x04ca  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0470  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0416  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0426  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0395  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0220  */
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
        float f4;
        Canvas canvas2;
        float f5;
        Paint paint;
        float f6;
        float f7;
        int i2;
        int i3;
        int i4;
        int i5;
        Paint paint2;
        int i6;
        int i7;
        int i8;
        Paint paint3;
        int i9;
        float f8;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.lastFrameTime;
        long j2 = elapsedRealtime - j;
        float f9 = this.currentRotation;
        float f10 = this.finalRotation;
        if (f9 != f10) {
            if (j != 0) {
                int i10 = (int) (this.currentAnimationTime + j2);
                this.currentAnimationTime = i10;
                if (i10 >= 200) {
                    this.currentRotation = f10;
                } else {
                    this.currentRotation = f9 < f10 ? this.interpolator.getInterpolation(i10 / 200.0f) * this.finalRotation : 1.0f - this.interpolator.getInterpolation(i10 / 200.0f);
                }
            }
            invalidateSelf();
        }
        float f11 = this.typeAnimationProgress;
        if (f11 < 1.0f) {
            float f12 = f11 + (((float) j2) / 200.0f);
            this.typeAnimationProgress = f12;
            if (f12 > 1.0f) {
                this.typeAnimationProgress = 1.0f;
            }
            invalidateSelf();
        }
        this.lastFrameTime = elapsedRealtime;
        canvas.save();
        canvas.translate(((getIntrinsicWidth() / 2) - AndroidUtilities.dp(9.0f)) - (AndroidUtilities.dp(1.0f) * this.currentRotation), getIntrinsicHeight() / 2);
        int i11 = this.iconColor;
        if (i11 == 0) {
            i11 = Theme.getColor(Theme.key_actionBarDefaultIcon);
        }
        int i12 = this.backColor;
        if (i12 == 0) {
            i12 = Theme.getColor(Theme.key_actionBarDefault);
        }
        int i13 = i12;
        int i14 = this.type;
        int i15 = TYPE_DEFAULT;
        if (i14 != i15) {
            if (this.previousType == i15) {
                dp = AndroidUtilities.dp(9.0f) * this.typeAnimationProgress * (1.0f - this.currentRotation);
                dp2 = AndroidUtilities.dp(7.0f) * this.typeAnimationProgress;
            } else {
                dp = AndroidUtilities.dp(9.0f) * (1.0f - this.currentRotation);
                dp2 = AndroidUtilities.dp(7.0f);
            }
            f = this.currentRotation;
        } else {
            if (this.previousType == i15) {
                f3 = 0.0f;
                f2 = 0.0f;
                if (this.rotateToBack) {
                    canvas.rotate(this.currentRotation * (this.reverseAngle ? -225 : NotificationCenter.fileUploadFailed), AndroidUtilities.dp(9.0f), 0.0f);
                    if (this.miniIcon) {
                        this.paint.setColor(i11);
                        this.paint.setAlpha(this.alpha);
                        canvas.drawLine((AndroidUtilities.dp(1.0f) * this.currentRotation) + (AndroidUtilities.dpf2(2.0f) * (1.0f - Math.abs(this.currentRotation))), 0.0f, ((AndroidUtilities.dpf2(16.0f) * (1.0f - this.currentRotation)) + (AndroidUtilities.dp(17.0f) * this.currentRotation)) - f3, 0.0f, this.paint);
                        dp3 = (AndroidUtilities.dpf2(5.0f) * (1.0f - Math.abs(this.currentRotation))) - (AndroidUtilities.dpf2(0.5f) * Math.abs(this.currentRotation));
                        dp4 = (AndroidUtilities.dpf2(16.0f) * (1.0f - Math.abs(this.currentRotation))) + (AndroidUtilities.dpf2(9.0f) * Math.abs(this.currentRotation));
                        dp5 = AndroidUtilities.dpf2(5.0f) + (AndroidUtilities.dpf2(3.0f) * Math.abs(this.currentRotation));
                        dp6 = AndroidUtilities.dpf2(2.0f) + (AndroidUtilities.dpf2(7.0f) * Math.abs(this.currentRotation));
                    } else {
                        int color = Theme.getColor(Theme.key_actionBarActionModeDefaultIcon);
                        i13 = AndroidUtilities.getOffsetColor(i13, Theme.getColor(Theme.key_actionBarActionModeDefault), this.currentRotation, 1.0f);
                        this.paint.setColor(AndroidUtilities.getOffsetColor(i11, color, this.currentRotation, 1.0f));
                        this.paint.setAlpha(this.alpha);
                        canvas.drawLine(this.currentRotation * AndroidUtilities.dp(1.0f), 0.0f, (AndroidUtilities.dp(18.0f) - (AndroidUtilities.dp(1.0f) * this.currentRotation)) - f3, 0.0f, this.paint);
                        dp3 = (AndroidUtilities.dp(5.0f) * (1.0f - Math.abs(this.currentRotation))) - (AndroidUtilities.dp(0.5f) * Math.abs(this.currentRotation));
                        dp4 = AndroidUtilities.dp(18.0f) - (AndroidUtilities.dp(9.0f) * Math.abs(this.currentRotation));
                        dp5 = AndroidUtilities.dp(5.0f) + (AndroidUtilities.dp(3.0f) * Math.abs(this.currentRotation));
                        dp6 = AndroidUtilities.dp(9.0f) * Math.abs(this.currentRotation);
                    }
                } else {
                    canvas.rotate(this.currentRotation * (this.reverseAngle ? -180 : NotificationCenter.updateBotMenuButton), AndroidUtilities.dp(9.0f), 0.0f);
                    this.paint.setColor(i11);
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
                }
                float f13 = dp3;
                float f14 = dp4;
                float f15 = dp6;
                int i16 = i13;
                float f16 = dp5;
                if (this.miniIcon) {
                    i = i16;
                    f4 = f13;
                    canvas2 = canvas;
                    f5 = f15;
                    canvas2.drawLine(f5, -f16, f14 - f2, -f13, this.paint);
                    paint = this.paint;
                    f6 = f16;
                    f7 = f14;
                } else {
                    canvas2 = canvas;
                    f5 = f15;
                    f7 = f14;
                    i = i16;
                    f4 = f13;
                    canvas2.drawLine(f5, -f16, f7, -f13, this.paint);
                    paint = this.paint;
                    f6 = f16;
                }
                canvas2.drawLine(f5, f6, f7, f4, paint);
                i2 = this.type;
                i3 = TYPE_DEFAULT;
                if ((i2 != i3 && this.currentRotation != 1.0f) || (this.previousType != i3 && this.typeAnimationProgress != 1.0f)) {
                    float dp8 = AndroidUtilities.dp(17.0f);
                    float f17 = -AndroidUtilities.dp(4.5f);
                    float f18 = AndroidUtilities.density * 5.5f;
                    float f19 = 1.0f - this.currentRotation;
                    canvas.scale(f19, f19, dp8, f17);
                    if (this.type == TYPE_DEFAULT) {
                        f18 *= 1.0f - this.typeAnimationProgress;
                    }
                    this.backPaint.setColor(i);
                    this.backPaint.setAlpha(this.alpha);
                    canvas.drawCircle(dp8, f17, f18, this.paint);
                    i4 = this.type;
                    i5 = TYPE_UDPATE_AVAILABLE;
                    if (i4 != i5 || this.previousType == i5) {
                        this.backPaint.setStrokeWidth(AndroidUtilities.density * 1.66f);
                        if (this.previousType == TYPE_UDPATE_AVAILABLE) {
                            paint2 = this.backPaint;
                            i6 = (int) (this.alpha * (1.0f - this.typeAnimationProgress));
                        } else {
                            paint2 = this.backPaint;
                            i6 = this.alpha;
                        }
                        paint2.setAlpha(i6);
                        canvas.drawLine(dp8, f17 - AndroidUtilities.dp(2.0f), dp8, f17, this.backPaint);
                        canvas.drawPoint(dp8, AndroidUtilities.dp(2.5f) + f17, this.backPaint);
                    }
                    i7 = this.type;
                    i8 = TYPE_UDPATE_DOWNLOADING;
                    if (i7 != i8 || this.previousType == i8) {
                        this.backPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
                        if (this.previousType == TYPE_UDPATE_DOWNLOADING) {
                            paint3 = this.backPaint;
                            i9 = (int) (this.alpha * (1.0f - this.typeAnimationProgress));
                        } else {
                            paint3 = this.backPaint;
                            i9 = this.alpha;
                        }
                        paint3.setAlpha(i9);
                        float max = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                        this.rect.set(dp8 - AndroidUtilities.dp(3.0f), f17 - AndroidUtilities.dp(3.0f), dp8 + AndroidUtilities.dp(3.0f), f17 + AndroidUtilities.dp(3.0f));
                        canvas.drawArc(this.rect, this.downloadRadOffset, max, false, this.backPaint);
                        float f20 = this.downloadRadOffset + (((float) (360 * j2)) / 2500.0f);
                        this.downloadRadOffset = f20;
                        this.downloadRadOffset = MediaActionDrawable.getCircleValue(f20);
                        float f21 = this.downloadProgress;
                        float f22 = this.downloadProgressAnimationStart;
                        f8 = f21 - f22;
                        if (f8 > 0.0f) {
                            float f23 = this.downloadProgressTime + ((float) j2);
                            this.downloadProgressTime = f23;
                            if (f23 >= 200.0f) {
                                this.animatedDownloadProgress = f21;
                                this.downloadProgressAnimationStart = f21;
                                this.downloadProgressTime = 0.0f;
                            } else {
                                this.animatedDownloadProgress = f22 + (f8 * this.interpolator.getInterpolation(f23 / 200.0f));
                            }
                        }
                        invalidateSelf();
                    }
                }
                canvas.restore();
            }
            dp = AndroidUtilities.dp(9.0f) * (1.0f - this.typeAnimationProgress);
            dp2 = AndroidUtilities.dp(7.0f);
            f = this.typeAnimationProgress;
        }
        f3 = dp2 * (1.0f - f);
        f2 = dp;
        if (this.rotateToBack) {
        }
        float f132 = dp3;
        float f142 = dp4;
        float f152 = dp6;
        int i162 = i13;
        float f162 = dp5;
        if (this.miniIcon) {
        }
        canvas2.drawLine(f5, f6, f7, f4, paint);
        i2 = this.type;
        i3 = TYPE_DEFAULT;
        if (i2 != i3) {
            float dp82 = AndroidUtilities.dp(17.0f);
            float f172 = -AndroidUtilities.dp(4.5f);
            float f182 = AndroidUtilities.density * 5.5f;
            float f192 = 1.0f - this.currentRotation;
            canvas.scale(f192, f192, dp82, f172);
            if (this.type == TYPE_DEFAULT) {
            }
            this.backPaint.setColor(i);
            this.backPaint.setAlpha(this.alpha);
            canvas.drawCircle(dp82, f172, f182, this.paint);
            i4 = this.type;
            i5 = TYPE_UDPATE_AVAILABLE;
            if (i4 != i5) {
            }
            this.backPaint.setStrokeWidth(AndroidUtilities.density * 1.66f);
            if (this.previousType == TYPE_UDPATE_AVAILABLE) {
            }
            paint2.setAlpha(i6);
            canvas.drawLine(dp82, f172 - AndroidUtilities.dp(2.0f), dp82, f172, this.backPaint);
            canvas.drawPoint(dp82, AndroidUtilities.dp(2.5f) + f172, this.backPaint);
            i7 = this.type;
            i8 = TYPE_UDPATE_DOWNLOADING;
            if (i7 != i8) {
            }
            this.backPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
            if (this.previousType == TYPE_UDPATE_DOWNLOADING) {
            }
            paint3.setAlpha(i9);
            float max2 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
            this.rect.set(dp82 - AndroidUtilities.dp(3.0f), f172 - AndroidUtilities.dp(3.0f), dp82 + AndroidUtilities.dp(3.0f), f172 + AndroidUtilities.dp(3.0f));
            canvas.drawArc(this.rect, this.downloadRadOffset, max2, false, this.backPaint);
            float f202 = this.downloadRadOffset + (((float) (360 * j2)) / 2500.0f);
            this.downloadRadOffset = f202;
            this.downloadRadOffset = MediaActionDrawable.getCircleValue(f202);
            float f212 = this.downloadProgress;
            float f222 = this.downloadProgressAnimationStart;
            f8 = f212 - f222;
            if (f8 > 0.0f) {
            }
            invalidateSelf();
            canvas.restore();
        }
        float dp822 = AndroidUtilities.dp(17.0f);
        float f1722 = -AndroidUtilities.dp(4.5f);
        float f1822 = AndroidUtilities.density * 5.5f;
        float f1922 = 1.0f - this.currentRotation;
        canvas.scale(f1922, f1922, dp822, f1722);
        if (this.type == TYPE_DEFAULT) {
        }
        this.backPaint.setColor(i);
        this.backPaint.setAlpha(this.alpha);
        canvas.drawCircle(dp822, f1722, f1822, this.paint);
        i4 = this.type;
        i5 = TYPE_UDPATE_AVAILABLE;
        if (i4 != i5) {
        }
        this.backPaint.setStrokeWidth(AndroidUtilities.density * 1.66f);
        if (this.previousType == TYPE_UDPATE_AVAILABLE) {
        }
        paint2.setAlpha(i6);
        canvas.drawLine(dp822, f1722 - AndroidUtilities.dp(2.0f), dp822, f1722, this.backPaint);
        canvas.drawPoint(dp822, AndroidUtilities.dp(2.5f) + f1722, this.backPaint);
        i7 = this.type;
        i8 = TYPE_UDPATE_DOWNLOADING;
        if (i7 != i8) {
        }
        this.backPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        if (this.previousType == TYPE_UDPATE_DOWNLOADING) {
        }
        paint3.setAlpha(i9);
        float max22 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
        this.rect.set(dp822 - AndroidUtilities.dp(3.0f), f1722 - AndroidUtilities.dp(3.0f), dp822 + AndroidUtilities.dp(3.0f), f1722 + AndroidUtilities.dp(3.0f));
        canvas.drawArc(this.rect, this.downloadRadOffset, max22, false, this.backPaint);
        float f2022 = this.downloadRadOffset + (((float) (360 * j2)) / 2500.0f);
        this.downloadRadOffset = f2022;
        this.downloadRadOffset = MediaActionDrawable.getCircleValue(f2022);
        float f2122 = this.downloadProgress;
        float f2222 = this.downloadProgressAnimationStart;
        f8 = f2122 - f2222;
        if (f8 > 0.0f) {
        }
        invalidateSelf();
        canvas.restore();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(24.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(24.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
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

    public void setBackColor(int i) {
        this.backColor = i;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public void setIconColor(int i) {
        this.iconColor = i;
    }

    public void setMiniIcon(boolean z) {
        this.miniIcon = z;
    }

    public void setRotateToBack(boolean z) {
        this.rotateToBack = z;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0035  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x001b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setRotation(float f, boolean z) {
        boolean z2;
        this.lastFrameTime = 0L;
        float f2 = this.currentRotation;
        if (f2 != 1.0f) {
            z2 = f2 != 0.0f;
            this.lastFrameTime = 0L;
            if (z) {
                this.currentRotation = f;
            } else {
                this.currentAnimationTime = f2 < f ? (int) (f2 * 200.0f) : (int) ((1.0f - f2) * 200.0f);
                this.lastFrameTime = SystemClock.elapsedRealtime();
            }
            this.finalRotation = f;
            invalidateSelf();
        }
        this.reverseAngle = z2;
        this.lastFrameTime = 0L;
        if (z) {
        }
        this.finalRotation = f;
        invalidateSelf();
    }

    public void setRoundCap() {
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.roundCap = true;
    }
}
