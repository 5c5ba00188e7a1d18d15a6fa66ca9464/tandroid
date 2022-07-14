package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes5.dex */
public class RadialProgressView extends View {
    private static final float risingTime = 500.0f;
    private static final float rotationTime = 2000.0f;
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
        this.progressColor = getThemedColor(Theme.key_progressCircle);
        this.decelerateInterpolator = new DecelerateInterpolator();
        this.accelerateInterpolator = new AccelerateInterpolator();
        Paint paint = new Paint(1);
        this.progressPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.progressPaint.setStrokeCap(Paint.Cap.ROUND);
        this.progressPaint.setStrokeWidth(AndroidUtilities.dp(3.0f));
        this.progressPaint.setColor(this.progressColor);
    }

    public void setUseSelfAlpha(boolean value) {
        this.useSelfAlpha = value;
    }

    @Override // android.view.View
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
        if (this.useSelfAlpha) {
            Drawable background = getBackground();
            int a = (int) (255.0f * alpha);
            if (background != null) {
                background.setAlpha(a);
            }
            this.progressPaint.setAlpha(a);
        }
    }

    public void setNoProgress(boolean value) {
        this.noProgress = value;
    }

    public void setProgress(float value) {
        this.currentProgress = value;
        if (this.animatedProgress > value) {
            this.animatedProgress = value;
        }
        this.progressAnimationStart = this.animatedProgress;
        this.progressTime = 0;
    }

    public void sync(RadialProgressView from) {
        this.lastUpdateTime = from.lastUpdateTime;
        this.radOffset = from.radOffset;
        this.toCircle = from.toCircle;
        this.toCircleProgress = from.toCircleProgress;
        this.noProgress = from.noProgress;
        this.currentCircleLength = from.currentCircleLength;
        this.drawingCircleLenght = from.drawingCircleLenght;
        this.currentProgressTime = from.currentProgressTime;
        this.currentProgress = from.currentProgress;
        this.progressTime = from.progressTime;
        this.animatedProgress = from.animatedProgress;
        this.risingCircleLength = from.risingCircleLength;
        this.progressAnimationStart = from.progressAnimationStart;
        updateAnimation(85L);
    }

    private void updateAnimation() {
        long newTime = System.currentTimeMillis();
        long dt = newTime - this.lastUpdateTime;
        if (dt > 17) {
            dt = 17;
        }
        this.lastUpdateTime = newTime;
        updateAnimation(dt);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00f2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateAnimation(long dt) {
        float f = this.radOffset + (((float) (360 * dt)) / rotationTime);
        this.radOffset = f;
        int count = (int) (f / 360.0f);
        this.radOffset = f - (count * 360);
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
                        float f4 = this.currentProgressTime + ((float) dt);
                        this.currentProgressTime = f4;
                        if (f4 >= risingTime) {
                            this.currentProgressTime = risingTime;
                        }
                        if (this.risingCircleLength) {
                            this.currentCircleLength = (this.accelerateInterpolator.getInterpolation(this.currentProgressTime / risingTime) * 266.0f) + 4.0f;
                        } else {
                            this.currentCircleLength = 4.0f - ((1.0f - this.decelerateInterpolator.getInterpolation(this.currentProgressTime / risingTime)) * 270.0f);
                        }
                        if (this.currentProgressTime == risingTime) {
                            boolean z2 = this.risingCircleLength;
                            if (z2) {
                                this.radOffset += 270.0f;
                                this.currentCircleLength = -266.0f;
                            }
                            this.risingCircleLength = !z2;
                            this.currentProgressTime = 0.0f;
                        }
                    } else if (this.risingCircleLength) {
                        float old = this.currentCircleLength;
                        float interpolation = (this.accelerateInterpolator.getInterpolation(this.currentProgressTime / risingTime) * 266.0f) + 4.0f;
                        this.currentCircleLength = interpolation;
                        float f5 = interpolation + (this.toCircleProgress * 360.0f);
                        this.currentCircleLength = f5;
                        float dx = old - f5;
                        if (dx > 0.0f) {
                            this.radOffset += old - f5;
                        }
                    } else {
                        float old2 = this.currentCircleLength;
                        float interpolation2 = 4.0f - ((1.0f - this.decelerateInterpolator.getInterpolation(this.currentProgressTime / risingTime)) * 270.0f);
                        this.currentCircleLength = interpolation2;
                        float f6 = interpolation2 - (this.toCircleProgress * 364.0f);
                        this.currentCircleLength = f6;
                        float dx2 = old2 - f6;
                        if (dx2 > 0.0f) {
                            this.radOffset += old2 - f6;
                        }
                    }
                } else {
                    float f7 = this.currentProgress;
                    float f8 = this.progressAnimationStart;
                    float progressDiff = f7 - f8;
                    if (progressDiff > 0.0f) {
                        int i = (int) (this.progressTime + dt);
                        this.progressTime = i;
                        if (i >= 200.0f) {
                            this.progressAnimationStart = f7;
                            this.animatedProgress = f7;
                            this.progressTime = 0;
                        } else {
                            this.animatedProgress = f8 + (AndroidUtilities.decelerateInterpolator.getInterpolation(this.progressTime / 200.0f) * progressDiff);
                        }
                    }
                    this.currentCircleLength = Math.max(4.0f, this.animatedProgress * 360.0f);
                }
                invalidate();
            }
        }
        if (!z) {
            float f9 = this.toCircleProgress;
            if (f9 != 0.0f) {
                float f10 = f9 - 0.04f;
                this.toCircleProgress = f10;
                if (f10 < 0.0f) {
                    this.toCircleProgress = 0.0f;
                }
            }
        }
        if (!this.noProgress) {
        }
        invalidate();
    }

    public void setSize(int value) {
        this.size = value;
        invalidate();
    }

    public void setStrokeWidth(float value) {
        this.progressPaint.setStrokeWidth(AndroidUtilities.dp(value));
    }

    public void setProgressColor(int color) {
        this.progressColor = color;
        this.progressPaint.setColor(color);
    }

    public void toCircle(boolean toCircle, boolean animated) {
        this.toCircle = toCircle;
        if (!animated) {
            this.toCircleProgress = toCircle ? 1.0f : 0.0f;
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        int x = (getMeasuredWidth() - this.size) / 2;
        int measuredHeight = getMeasuredHeight();
        int i = this.size;
        int y = (measuredHeight - i) / 2;
        this.cicleRect.set(x, y, x + i, i + y);
        RectF rectF = this.cicleRect;
        float f = this.radOffset;
        float f2 = this.currentCircleLength;
        this.drawingCircleLenght = f2;
        canvas.drawArc(rectF, f, f2, false, this.progressPaint);
        updateAnimation();
    }

    public void draw(Canvas canvas, float cx, float cy) {
        RectF rectF = this.cicleRect;
        int i = this.size;
        rectF.set(cx - (i / 2.0f), cy - (i / 2.0f), (i / 2.0f) + cx, (i / 2.0f) + cy);
        RectF rectF2 = this.cicleRect;
        float f = this.radOffset;
        float f2 = this.currentCircleLength;
        this.drawingCircleLenght = f2;
        canvas.drawArc(rectF2, f, f2, false, this.progressPaint);
        updateAnimation();
    }

    public boolean isCircle() {
        return Math.abs(this.drawingCircleLenght) >= 360.0f;
    }

    private int getThemedColor(String key) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(key) : null;
        return color != null ? color.intValue() : Theme.getColor(key);
    }
}
