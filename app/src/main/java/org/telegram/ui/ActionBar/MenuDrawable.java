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
    /* JADX WARN: Removed duplicated region for block: B:46:0x019b  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x02fa  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0319  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x033b  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x036d  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x038a  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x03a0  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x03b0  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x03d9  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x03ed  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x03fc  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0458  */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        float f;
        float f2;
        float dp;
        float dp2;
        float dp3;
        float dp4;
        float abs;
        float dpf2;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        float f3;
        float f4;
        float f5;
        float f6;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.lastFrameTime;
        long j2 = elapsedRealtime - j;
        float f7 = this.currentRotation;
        float f8 = this.finalRotation;
        if (f7 != f8) {
            if (j != 0) {
                int i8 = (int) (this.currentAnimationTime + j2);
                this.currentAnimationTime = i8;
                if (i8 >= 200) {
                    this.currentRotation = f8;
                } else if (f7 < f8) {
                    this.currentRotation = this.interpolator.getInterpolation(i8 / 200.0f) * this.finalRotation;
                } else {
                    this.currentRotation = 1.0f - this.interpolator.getInterpolation(i8 / 200.0f);
                }
            }
            invalidateSelf();
        }
        float f9 = this.typeAnimationProgress;
        if (f9 < 1.0f) {
            float f10 = f9 + (((float) j2) / 200.0f);
            this.typeAnimationProgress = f10;
            if (f10 > 1.0f) {
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
                f6 = AndroidUtilities.dp(9.0f) * (1.0f - this.typeAnimationProgress);
                f5 = AndroidUtilities.dp(7.0f);
                f4 = this.typeAnimationProgress;
            } else {
                f2 = 0.0f;
                f = 0.0f;
                if (!this.rotateToBack) {
                    canvas.rotate(this.currentRotation * (this.reverseAngle ? -180 : 180), AndroidUtilities.dp(9.0f), 0.0f);
                    this.paint.setColor(i9);
                    this.paint.setAlpha(this.alpha);
                    canvas.drawLine(0.0f, 0.0f, (AndroidUtilities.dp(18.0f) - (AndroidUtilities.dp(3.0f) * this.currentRotation)) - f2, 0.0f, this.paint);
                    dp = (AndroidUtilities.dp(5.0f) * (1.0f - Math.abs(this.currentRotation))) - (AndroidUtilities.dp(0.5f) * Math.abs(this.currentRotation));
                    dp2 = AndroidUtilities.dp(18.0f) - (AndroidUtilities.dp(2.5f) * Math.abs(this.currentRotation));
                    dp3 = AndroidUtilities.dp(5.0f) + (AndroidUtilities.dp(2.0f) * Math.abs(this.currentRotation));
                    dp4 = AndroidUtilities.dp(7.5f);
                    abs = Math.abs(this.currentRotation);
                } else {
                    canvas.rotate(this.currentRotation * (this.reverseAngle ? -225 : 135), AndroidUtilities.dp(9.0f), 0.0f);
                    if (this.miniIcon) {
                        this.paint.setColor(i9);
                        this.paint.setAlpha(this.alpha);
                        canvas.drawLine((AndroidUtilities.dp(1.0f) * this.currentRotation) + (AndroidUtilities.dpf2(2.0f) * (1.0f - Math.abs(this.currentRotation))), 0.0f, ((AndroidUtilities.dpf2(16.0f) * (1.0f - this.currentRotation)) + (AndroidUtilities.dp(17.0f) * this.currentRotation)) - f2, 0.0f, this.paint);
                        dp = (AndroidUtilities.dpf2(5.0f) * (1.0f - Math.abs(this.currentRotation))) - (AndroidUtilities.dpf2(0.5f) * Math.abs(this.currentRotation));
                        dp2 = (AndroidUtilities.dpf2(16.0f) * (1.0f - Math.abs(this.currentRotation))) + (AndroidUtilities.dpf2(9.0f) * Math.abs(this.currentRotation));
                        dp3 = AndroidUtilities.dpf2(5.0f) + (AndroidUtilities.dpf2(3.0f) * Math.abs(this.currentRotation));
                        dpf2 = AndroidUtilities.dpf2(2.0f) + (AndroidUtilities.dpf2(7.0f) * Math.abs(this.currentRotation));
                        float f11 = dp;
                        float f12 = dp2;
                        float f13 = dpf2;
                        int i14 = i11;
                        float f14 = dp3;
                        if (this.miniIcon) {
                            i = i14;
                            canvas.drawLine(f13, -f14, f12, -f11, this.paint);
                            canvas.drawLine(f13, f14, f12, f11, this.paint);
                        } else {
                            i = i14;
                            canvas.drawLine(f13, -f14, f12 - f, -f11, this.paint);
                            canvas.drawLine(f13, f14, f12, f11, this.paint);
                        }
                        i2 = this.type;
                        i3 = TYPE_DEFAULT;
                        if ((i2 != i3 && this.currentRotation != 1.0f) || (this.previousType != i3 && this.typeAnimationProgress != 1.0f)) {
                            float dp5 = AndroidUtilities.dp(17.0f);
                            float f15 = -AndroidUtilities.dp(4.5f);
                            float f16 = AndroidUtilities.density * 5.5f;
                            float f17 = this.currentRotation;
                            canvas.scale(1.0f - f17, 1.0f - f17, dp5, f15);
                            if (this.type == TYPE_DEFAULT) {
                                f16 *= 1.0f - this.typeAnimationProgress;
                            }
                            this.backPaint.setColor(i);
                            this.backPaint.setAlpha(this.alpha);
                            canvas.drawCircle(dp5, f15, f16, this.paint);
                            i4 = this.type;
                            i5 = TYPE_UDPATE_AVAILABLE;
                            if (i4 != i5 || this.previousType == i5) {
                                this.backPaint.setStrokeWidth(AndroidUtilities.density * 1.66f);
                                if (this.previousType != TYPE_UDPATE_AVAILABLE) {
                                    this.backPaint.setAlpha((int) (this.alpha * (1.0f - this.typeAnimationProgress)));
                                } else {
                                    this.backPaint.setAlpha(this.alpha);
                                }
                                canvas.drawLine(dp5, f15 - AndroidUtilities.dp(2.0f), dp5, f15, this.backPaint);
                                canvas.drawPoint(dp5, AndroidUtilities.dp(2.5f) + f15, this.backPaint);
                            }
                            i6 = this.type;
                            i7 = TYPE_UDPATE_DOWNLOADING;
                            if (i6 != i7 || this.previousType == i7) {
                                this.backPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
                                if (this.previousType != TYPE_UDPATE_DOWNLOADING) {
                                    this.backPaint.setAlpha((int) (this.alpha * (1.0f - this.typeAnimationProgress)));
                                } else {
                                    this.backPaint.setAlpha(this.alpha);
                                }
                                float max = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                                this.rect.set(dp5 - AndroidUtilities.dp(3.0f), f15 - AndroidUtilities.dp(3.0f), dp5 + AndroidUtilities.dp(3.0f), f15 + AndroidUtilities.dp(3.0f));
                                canvas.drawArc(this.rect, this.downloadRadOffset, max, false, this.backPaint);
                                float f18 = this.downloadRadOffset + (((float) (360 * j2)) / 2500.0f);
                                this.downloadRadOffset = f18;
                                this.downloadRadOffset = MediaActionDrawable.getCircleValue(f18);
                                float f19 = this.downloadProgress;
                                float f20 = this.downloadProgressAnimationStart;
                                f3 = f19 - f20;
                                if (f3 > 0.0f) {
                                    float f21 = this.downloadProgressTime + ((float) j2);
                                    this.downloadProgressTime = f21;
                                    if (f21 >= 200.0f) {
                                        this.animatedDownloadProgress = f19;
                                        this.downloadProgressAnimationStart = f19;
                                        this.downloadProgressTime = 0.0f;
                                    } else {
                                        this.animatedDownloadProgress = f20 + (f3 * this.interpolator.getInterpolation(f21 / 200.0f));
                                    }
                                }
                                invalidateSelf();
                            }
                        }
                        canvas.restore();
                    }
                    int color = Theme.getColor("actionBarActionModeDefaultIcon");
                    i11 = AndroidUtilities.getOffsetColor(i11, Theme.getColor("actionBarActionModeDefault"), this.currentRotation, 1.0f);
                    this.paint.setColor(AndroidUtilities.getOffsetColor(i9, color, this.currentRotation, 1.0f));
                    this.paint.setAlpha(this.alpha);
                    canvas.drawLine(this.currentRotation * AndroidUtilities.dp(1.0f), 0.0f, (AndroidUtilities.dp(18.0f) - (AndroidUtilities.dp(1.0f) * this.currentRotation)) - f2, 0.0f, this.paint);
                    dp = (AndroidUtilities.dp(5.0f) * (1.0f - Math.abs(this.currentRotation))) - (AndroidUtilities.dp(0.5f) * Math.abs(this.currentRotation));
                    dp2 = AndroidUtilities.dp(18.0f) - (AndroidUtilities.dp(9.0f) * Math.abs(this.currentRotation));
                    dp3 = AndroidUtilities.dp(5.0f) + (AndroidUtilities.dp(3.0f) * Math.abs(this.currentRotation));
                    dp4 = AndroidUtilities.dp(9.0f);
                    abs = Math.abs(this.currentRotation);
                }
                dpf2 = dp4 * abs;
                float f112 = dp;
                float f122 = dp2;
                float f132 = dpf2;
                int i142 = i11;
                float f142 = dp3;
                if (this.miniIcon) {
                }
                i2 = this.type;
                i3 = TYPE_DEFAULT;
                if (i2 != i3) {
                    float dp52 = AndroidUtilities.dp(17.0f);
                    float f152 = -AndroidUtilities.dp(4.5f);
                    float f162 = AndroidUtilities.density * 5.5f;
                    float f172 = this.currentRotation;
                    canvas.scale(1.0f - f172, 1.0f - f172, dp52, f152);
                    if (this.type == TYPE_DEFAULT) {
                    }
                    this.backPaint.setColor(i);
                    this.backPaint.setAlpha(this.alpha);
                    canvas.drawCircle(dp52, f152, f162, this.paint);
                    i4 = this.type;
                    i5 = TYPE_UDPATE_AVAILABLE;
                    if (i4 != i5) {
                    }
                    this.backPaint.setStrokeWidth(AndroidUtilities.density * 1.66f);
                    if (this.previousType != TYPE_UDPATE_AVAILABLE) {
                    }
                    canvas.drawLine(dp52, f152 - AndroidUtilities.dp(2.0f), dp52, f152, this.backPaint);
                    canvas.drawPoint(dp52, AndroidUtilities.dp(2.5f) + f152, this.backPaint);
                    i6 = this.type;
                    i7 = TYPE_UDPATE_DOWNLOADING;
                    if (i6 != i7) {
                    }
                    this.backPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
                    if (this.previousType != TYPE_UDPATE_DOWNLOADING) {
                    }
                    float max2 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                    this.rect.set(dp52 - AndroidUtilities.dp(3.0f), f152 - AndroidUtilities.dp(3.0f), dp52 + AndroidUtilities.dp(3.0f), f152 + AndroidUtilities.dp(3.0f));
                    canvas.drawArc(this.rect, this.downloadRadOffset, max2, false, this.backPaint);
                    float f182 = this.downloadRadOffset + (((float) (360 * j2)) / 2500.0f);
                    this.downloadRadOffset = f182;
                    this.downloadRadOffset = MediaActionDrawable.getCircleValue(f182);
                    float f192 = this.downloadProgress;
                    float f202 = this.downloadProgressAnimationStart;
                    f3 = f192 - f202;
                    if (f3 > 0.0f) {
                    }
                    invalidateSelf();
                    canvas.restore();
                }
                float dp522 = AndroidUtilities.dp(17.0f);
                float f1522 = -AndroidUtilities.dp(4.5f);
                float f1622 = AndroidUtilities.density * 5.5f;
                float f1722 = this.currentRotation;
                canvas.scale(1.0f - f1722, 1.0f - f1722, dp522, f1522);
                if (this.type == TYPE_DEFAULT) {
                }
                this.backPaint.setColor(i);
                this.backPaint.setAlpha(this.alpha);
                canvas.drawCircle(dp522, f1522, f1622, this.paint);
                i4 = this.type;
                i5 = TYPE_UDPATE_AVAILABLE;
                if (i4 != i5) {
                }
                this.backPaint.setStrokeWidth(AndroidUtilities.density * 1.66f);
                if (this.previousType != TYPE_UDPATE_AVAILABLE) {
                }
                canvas.drawLine(dp522, f1522 - AndroidUtilities.dp(2.0f), dp522, f1522, this.backPaint);
                canvas.drawPoint(dp522, AndroidUtilities.dp(2.5f) + f1522, this.backPaint);
                i6 = this.type;
                i7 = TYPE_UDPATE_DOWNLOADING;
                if (i6 != i7) {
                }
                this.backPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
                if (this.previousType != TYPE_UDPATE_DOWNLOADING) {
                }
                float max22 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                this.rect.set(dp522 - AndroidUtilities.dp(3.0f), f1522 - AndroidUtilities.dp(3.0f), dp522 + AndroidUtilities.dp(3.0f), f1522 + AndroidUtilities.dp(3.0f));
                canvas.drawArc(this.rect, this.downloadRadOffset, max22, false, this.backPaint);
                float f1822 = this.downloadRadOffset + (((float) (360 * j2)) / 2500.0f);
                this.downloadRadOffset = f1822;
                this.downloadRadOffset = MediaActionDrawable.getCircleValue(f1822);
                float f1922 = this.downloadProgress;
                float f2022 = this.downloadProgressAnimationStart;
                f3 = f1922 - f2022;
                if (f3 > 0.0f) {
                }
                invalidateSelf();
                canvas.restore();
            }
        } else if (this.previousType == i13) {
            f6 = AndroidUtilities.dp(9.0f) * this.typeAnimationProgress * (1.0f - this.currentRotation);
            f5 = AndroidUtilities.dp(7.0f) * this.typeAnimationProgress;
            f4 = this.currentRotation;
        } else {
            f6 = AndroidUtilities.dp(9.0f) * (1.0f - this.currentRotation);
            f5 = AndroidUtilities.dp(7.0f);
            f4 = this.currentRotation;
        }
        f2 = f5 * (1.0f - f4);
        f = f6;
        if (!this.rotateToBack) {
        }
        dpf2 = dp4 * abs;
        float f1122 = dp;
        float f1222 = dp2;
        float f1322 = dpf2;
        int i1422 = i11;
        float f1422 = dp3;
        if (this.miniIcon) {
        }
        i2 = this.type;
        i3 = TYPE_DEFAULT;
        if (i2 != i3) {
        }
        float dp5222 = AndroidUtilities.dp(17.0f);
        float f15222 = -AndroidUtilities.dp(4.5f);
        float f16222 = AndroidUtilities.density * 5.5f;
        float f17222 = this.currentRotation;
        canvas.scale(1.0f - f17222, 1.0f - f17222, dp5222, f15222);
        if (this.type == TYPE_DEFAULT) {
        }
        this.backPaint.setColor(i);
        this.backPaint.setAlpha(this.alpha);
        canvas.drawCircle(dp5222, f15222, f16222, this.paint);
        i4 = this.type;
        i5 = TYPE_UDPATE_AVAILABLE;
        if (i4 != i5) {
        }
        this.backPaint.setStrokeWidth(AndroidUtilities.density * 1.66f);
        if (this.previousType != TYPE_UDPATE_AVAILABLE) {
        }
        canvas.drawLine(dp5222, f15222 - AndroidUtilities.dp(2.0f), dp5222, f15222, this.backPaint);
        canvas.drawPoint(dp5222, AndroidUtilities.dp(2.5f) + f15222, this.backPaint);
        i6 = this.type;
        i7 = TYPE_UDPATE_DOWNLOADING;
        if (i6 != i7) {
        }
        this.backPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        if (this.previousType != TYPE_UDPATE_DOWNLOADING) {
        }
        float max222 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
        this.rect.set(dp5222 - AndroidUtilities.dp(3.0f), f15222 - AndroidUtilities.dp(3.0f), dp5222 + AndroidUtilities.dp(3.0f), f15222 + AndroidUtilities.dp(3.0f));
        canvas.drawArc(this.rect, this.downloadRadOffset, max222, false, this.backPaint);
        float f18222 = this.downloadRadOffset + (((float) (360 * j2)) / 2500.0f);
        this.downloadRadOffset = f18222;
        this.downloadRadOffset = MediaActionDrawable.getCircleValue(f18222);
        float f19222 = this.downloadProgress;
        float f20222 = this.downloadProgressAnimationStart;
        f3 = f19222 - f20222;
        if (f3 > 0.0f) {
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
    }

    public void setMiniIcon(boolean z) {
        this.miniIcon = z;
    }
}
