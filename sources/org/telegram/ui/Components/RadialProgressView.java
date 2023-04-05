package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.Keep;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes4.dex */
public class RadialProgressView extends View {
    private AccelerateInterpolator accelerateInterpolator;
    private float animatedProgress;
    private RectF cicleRect;
    private float currentCircleLength;
    private float currentProgress;
    private float currentProgressTime;
    private DecelerateInterpolator decelerateInterpolator;
    private float drawingCircleLenght;
    private long lastUpdateTime;
    private boolean noProgress;
    private float progressAnimationStart;
    private int progressColor;
    private Paint progressPaint;
    private int progressTime;
    private float radOffset;
    private final Theme.ResourcesProvider resourcesProvider;
    private boolean risingCircleLength;
    private int size;
    private boolean toCircle;
    private float toCircleProgress;
    private boolean useSelfAlpha;

    public RadialProgressView(Context context) {
        this(context, null);
    }

    public RadialProgressView(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.cicleRect = new RectF();
        this.noProgress = true;
        this.resourcesProvider = resourcesProvider;
        this.size = AndroidUtilities.dp(40.0f);
        this.progressColor = getThemedColor("progressCircle");
        this.decelerateInterpolator = new DecelerateInterpolator();
        this.accelerateInterpolator = new AccelerateInterpolator();
        Paint paint = new Paint(1);
        this.progressPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.progressPaint.setStrokeCap(Paint.Cap.ROUND);
        this.progressPaint.setStrokeWidth(AndroidUtilities.dp(3.0f));
        this.progressPaint.setColor(this.progressColor);
    }

    public void setUseSelfAlpha(boolean z) {
        this.useSelfAlpha = z;
    }

    @Override // android.view.View
    @Keep
    public void setAlpha(float f) {
        super.setAlpha(f);
        if (this.useSelfAlpha) {
            Drawable background = getBackground();
            int i = (int) (f * 255.0f);
            if (background != null) {
                background.setAlpha(i);
            }
            this.progressPaint.setAlpha(i);
        }
    }

    public void setNoProgress(boolean z) {
        this.noProgress = z;
    }

    public void setProgress(float f) {
        this.currentProgress = f;
        if (this.animatedProgress > f) {
            this.animatedProgress = f;
        }
        this.progressAnimationStart = this.animatedProgress;
        this.progressTime = 0;
    }

    public void sync(RadialProgressView radialProgressView) {
        this.lastUpdateTime = radialProgressView.lastUpdateTime;
        this.radOffset = radialProgressView.radOffset;
        this.toCircle = radialProgressView.toCircle;
        this.toCircleProgress = radialProgressView.toCircleProgress;
        this.noProgress = radialProgressView.noProgress;
        this.currentCircleLength = radialProgressView.currentCircleLength;
        this.drawingCircleLenght = radialProgressView.drawingCircleLenght;
        this.currentProgressTime = radialProgressView.currentProgressTime;
        this.currentProgress = radialProgressView.currentProgress;
        this.progressTime = radialProgressView.progressTime;
        this.animatedProgress = radialProgressView.animatedProgress;
        this.risingCircleLength = radialProgressView.risingCircleLength;
        this.progressAnimationStart = radialProgressView.progressAnimationStart;
        updateAnimation(85L);
    }

    private void updateAnimation() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = currentTimeMillis - this.lastUpdateTime;
        if (j > 17) {
            j = 17;
        }
        this.lastUpdateTime = currentTimeMillis;
        updateAnimation(j);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00f0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateAnimation(long j) {
        float f = this.radOffset + (((float) (360 * j)) / 2000.0f);
        this.radOffset = f;
        this.radOffset = f - (((int) (f / 360.0f)) * 360);
        boolean z = this.toCircle;
        if (z) {
            float f2 = this.toCircleProgress;
            if (f2 != 1.0f) {
                float f3 = f2 + 0.07272727f;
                this.toCircleProgress = f3;
                if (f3 > 1.0f) {
                    this.toCircleProgress = 1.0f;
                }
                if (!this.noProgress) {
                    if (this.toCircleProgress == 0.0f) {
                        float f4 = this.currentProgressTime + ((float) j);
                        this.currentProgressTime = f4;
                        if (f4 >= 500.0f) {
                            this.currentProgressTime = 500.0f;
                        }
                        if (this.risingCircleLength) {
                            this.currentCircleLength = (this.accelerateInterpolator.getInterpolation(this.currentProgressTime / 500.0f) * 266.0f) + 4.0f;
                        } else {
                            this.currentCircleLength = 4.0f - ((1.0f - this.decelerateInterpolator.getInterpolation(this.currentProgressTime / 500.0f)) * 270.0f);
                        }
                        if (this.currentProgressTime == 500.0f) {
                            boolean z2 = this.risingCircleLength;
                            if (z2) {
                                this.radOffset += 270.0f;
                                this.currentCircleLength = -266.0f;
                            }
                            this.risingCircleLength = !z2;
                            this.currentProgressTime = 0.0f;
                        }
                    } else if (this.risingCircleLength) {
                        float f5 = this.currentCircleLength;
                        float interpolation = (this.accelerateInterpolator.getInterpolation(this.currentProgressTime / 500.0f) * 266.0f) + 4.0f;
                        this.currentCircleLength = interpolation;
                        float f6 = interpolation + (this.toCircleProgress * 360.0f);
                        this.currentCircleLength = f6;
                        if (f5 - f6 > 0.0f) {
                            this.radOffset += f5 - f6;
                        }
                    } else {
                        float f7 = this.currentCircleLength;
                        float interpolation2 = 4.0f - ((1.0f - this.decelerateInterpolator.getInterpolation(this.currentProgressTime / 500.0f)) * 270.0f);
                        this.currentCircleLength = interpolation2;
                        float f8 = interpolation2 - (this.toCircleProgress * 364.0f);
                        this.currentCircleLength = f8;
                        if (f7 - f8 > 0.0f) {
                            this.radOffset += f7 - f8;
                        }
                    }
                } else {
                    float f9 = this.currentProgress;
                    float f10 = this.progressAnimationStart;
                    float f11 = f9 - f10;
                    if (f11 > 0.0f) {
                        int i = (int) (this.progressTime + j);
                        this.progressTime = i;
                        if (i >= 200.0f) {
                            this.progressAnimationStart = f9;
                            this.animatedProgress = f9;
                            this.progressTime = 0;
                        } else {
                            this.animatedProgress = f10 + (f11 * AndroidUtilities.decelerateInterpolator.getInterpolation(i / 200.0f));
                        }
                    }
                    this.currentCircleLength = Math.max(4.0f, this.animatedProgress * 360.0f);
                }
                invalidate();
            }
        }
        if (!z) {
            float f12 = this.toCircleProgress;
            if (f12 != 0.0f) {
                float f13 = f12 - 0.04f;
                this.toCircleProgress = f13;
                if (f13 < 0.0f) {
                    this.toCircleProgress = 0.0f;
                }
            }
        }
        if (!this.noProgress) {
        }
        invalidate();
    }

    public void setSize(int i) {
        this.size = i;
        invalidate();
    }

    public void setStrokeWidth(float f) {
        this.progressPaint.setStrokeWidth(AndroidUtilities.dp(f));
    }

    public void setProgressColor(int i) {
        this.progressColor = i;
        this.progressPaint.setColor(i);
    }

    public void toCircle(boolean z, boolean z2) {
        this.toCircle = z;
        if (z2) {
            return;
        }
        this.toCircleProgress = z ? 1.0f : 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        int measuredWidth = (getMeasuredWidth() - this.size) / 2;
        int measuredHeight = getMeasuredHeight();
        int i = this.size;
        int i2 = (measuredHeight - i) / 2;
        this.cicleRect.set(measuredWidth, i2, measuredWidth + i, i2 + i);
        RectF rectF = this.cicleRect;
        float f = this.radOffset;
        float f2 = this.currentCircleLength;
        this.drawingCircleLenght = f2;
        canvas.drawArc(rectF, f, f2, false, this.progressPaint);
        updateAnimation();
    }

    public void draw(Canvas canvas, float f, float f2) {
        RectF rectF = this.cicleRect;
        int i = this.size;
        rectF.set(f - (i / 2.0f), f2 - (i / 2.0f), f + (i / 2.0f), f2 + (i / 2.0f));
        RectF rectF2 = this.cicleRect;
        float f3 = this.radOffset;
        float f4 = this.currentCircleLength;
        this.drawingCircleLenght = f4;
        canvas.drawArc(rectF2, f3, f4, false, this.progressPaint);
        updateAnimation();
    }

    public boolean isCircle() {
        return Math.abs(this.drawingCircleLenght) >= 360.0f;
    }

    private int getThemedColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
        return color != null ? color.intValue() : Theme.getColor(str);
    }
}
