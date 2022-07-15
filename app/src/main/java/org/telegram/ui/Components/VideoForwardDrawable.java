package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
/* loaded from: classes3.dex */
public class VideoForwardDrawable extends Drawable {
    private static final int[] playPath = {10, 7, 26, 16, 10, 25};
    private boolean animating;
    private float animationProgress;
    private Path clippingPath;
    private VideoForwardDrawableDelegate delegate;
    private float enterAnimationProgress;
    private boolean isOneShootAnimation;
    private boolean isRound;
    private long lastAnimationTime;
    private int lastClippingPath;
    private boolean leftSide;
    private boolean showing;
    private long time;
    private String timeStr;
    private Paint paint = new Paint(1);
    private TextPaint textPaint = new TextPaint(1);
    private Path path1 = new Path();
    private float playScaleFactor = 1.0f;

    /* loaded from: classes3.dex */
    public interface VideoForwardDrawableDelegate {
        void invalidate();

        void onAnimationEnd();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    public void setTime(long j) {
        this.time = j;
        if (j >= 1000) {
            this.timeStr = LocaleController.formatPluralString("Seconds", (int) (j / 1000), new Object[0]);
        } else {
            this.timeStr = null;
        }
    }

    public VideoForwardDrawable(boolean z) {
        this.isRound = z;
        this.paint.setColor(-1);
        this.textPaint.setColor(-1);
        this.textPaint.setTextSize(AndroidUtilities.dp(12.0f));
        this.textPaint.setTextAlign(Paint.Align.CENTER);
        this.path1.reset();
        int i = 0;
        while (true) {
            int[] iArr = playPath;
            if (i >= iArr.length / 2) {
                this.path1.close();
                return;
            }
            if (i == 0) {
                int i2 = i * 2;
                this.path1.moveTo(AndroidUtilities.dp(iArr[i2]), AndroidUtilities.dp(iArr[i2 + 1]));
            } else {
                int i3 = i * 2;
                this.path1.lineTo(AndroidUtilities.dp(iArr[i3]), AndroidUtilities.dp(iArr[i3 + 1]));
            }
            i++;
        }
    }

    public void setPlayScaleFactor(float f) {
        this.playScaleFactor = f;
        invalidate();
    }

    public boolean isAnimating() {
        return this.animating;
    }

    public void startAnimation() {
        this.animating = true;
        this.animationProgress = 0.0f;
        invalidateSelf();
    }

    public void setOneShootAnimation(boolean z) {
        if (this.isOneShootAnimation != z) {
            this.isOneShootAnimation = z;
            this.timeStr = null;
            this.time = 0L;
            this.animationProgress = 0.0f;
        }
    }

    public void setLeftSide(boolean z) {
        boolean z2 = this.leftSide;
        if (z2 != z || this.animationProgress < 1.0f || !this.isOneShootAnimation) {
            if (z2 != z) {
                this.time = 0L;
                this.timeStr = null;
            }
            this.leftSide = z;
            startAnimation();
        }
    }

    public void setDelegate(VideoForwardDrawableDelegate videoForwardDrawableDelegate) {
        this.delegate = videoForwardDrawableDelegate;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.paint.setAlpha(i);
        this.textPaint.setAlpha(i);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }

    /* JADX WARN: Removed duplicated region for block: B:104:0x02a7  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x02aa  */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        int i;
        float f;
        android.graphics.Rect bounds = getBounds();
        int width = bounds.left + ((bounds.width() - getIntrinsicWidth()) / 2);
        int height = bounds.top + ((bounds.height() - getIntrinsicHeight()) / 2);
        if (this.leftSide) {
            i = width - ((bounds.width() / 4) - AndroidUtilities.dp(16.0f));
        } else {
            i = width + (bounds.width() / 4) + AndroidUtilities.dp(16.0f);
        }
        canvas.save();
        if (this.isRound) {
            if (this.clippingPath == null) {
                this.clippingPath = new Path();
            }
            int i2 = bounds.left + (bounds.top << 8) + (bounds.bottom << 16) + (bounds.right << 24);
            if (this.lastClippingPath != i2) {
                this.clippingPath.reset();
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(bounds);
                this.clippingPath.addOval(rectF, Path.Direction.CCW);
                this.lastClippingPath = i2;
            }
            canvas.clipPath(this.clippingPath);
        } else {
            canvas.clipRect(bounds.left, bounds.top, bounds.right, bounds.bottom);
        }
        if (!this.isOneShootAnimation) {
            this.paint.setAlpha((int) (this.enterAnimationProgress * 80.0f));
            this.textPaint.setAlpha((int) (this.enterAnimationProgress * 255.0f));
        } else {
            float f2 = this.animationProgress;
            if (f2 <= 0.7f) {
                this.paint.setAlpha((int) (Math.min(1.0f, f2 / 0.3f) * 80.0f));
                this.textPaint.setAlpha((int) (Math.min(1.0f, this.animationProgress / 0.3f) * 255.0f));
            } else {
                this.paint.setAlpha((int) ((1.0f - ((f2 - 0.7f) / 0.3f)) * 80.0f));
                this.textPaint.setAlpha((int) ((1.0f - ((this.animationProgress - 0.7f) / 0.3f)) * 255.0f));
            }
        }
        int i3 = -1;
        canvas.drawCircle(((Math.max(bounds.width(), bounds.height()) / 4) * (this.leftSide ? -1 : 1)) + i, AndroidUtilities.dp(16.0f) + height, Math.max(bounds.width(), bounds.height()) / 2, this.paint);
        canvas.restore();
        String str = this.timeStr;
        if (str != null) {
            int intrinsicWidth = getIntrinsicWidth();
            if (!this.leftSide) {
                i3 = 1;
            }
            canvas.drawText(str, (intrinsicWidth * i3) + i, getIntrinsicHeight() + height + AndroidUtilities.dp(15.0f), this.textPaint);
        }
        canvas.save();
        float f3 = this.playScaleFactor;
        float f4 = i;
        float f5 = height;
        canvas.scale(f3, f3, f4, (getIntrinsicHeight() / 2.0f) + f5);
        if (this.leftSide) {
            canvas.rotate(180.0f, f4, height + (getIntrinsicHeight() / 2));
        }
        canvas.translate(f4, f5);
        float f6 = this.animationProgress;
        if (f6 <= 0.6f) {
            int min = f6 < 0.4f ? Math.min(255, (int) ((f6 * 255.0f) / 0.2f)) : (int) ((1.0f - ((f6 - 0.4f) / 0.2f)) * 255.0f);
            if (!this.isOneShootAnimation) {
                min = (int) (min * this.enterAnimationProgress);
            }
            this.paint.setAlpha(min);
            canvas.drawPath(this.path1, this.paint);
        }
        canvas.translate(AndroidUtilities.dp(18.0f), 0.0f);
        float f7 = this.animationProgress;
        if (f7 >= 0.2f && f7 <= 0.8f) {
            float f8 = f7 - 0.2f;
            int min2 = f8 < 0.4f ? Math.min(255, (int) ((f8 * 255.0f) / 0.2f)) : (int) ((1.0f - ((f8 - 0.4f) / 0.2f)) * 255.0f);
            if (!this.isOneShootAnimation) {
                min2 = (int) (min2 * this.enterAnimationProgress);
            }
            this.paint.setAlpha(min2);
            canvas.drawPath(this.path1, this.paint);
        }
        canvas.translate(AndroidUtilities.dp(18.0f), 0.0f);
        float f9 = this.animationProgress;
        if (f9 >= 0.4f && f9 <= 1.0f) {
            float f10 = f9 - 0.4f;
            int min3 = f10 < 0.4f ? Math.min(255, (int) ((f10 * 255.0f) / 0.2f)) : (int) ((1.0f - ((f10 - 0.4f) / 0.2f)) * 255.0f);
            if (!this.isOneShootAnimation) {
                min3 = (int) (min3 * this.enterAnimationProgress);
            }
            this.paint.setAlpha(min3);
            canvas.drawPath(this.path1, this.paint);
        }
        canvas.restore();
        if (this.animating) {
            long currentTimeMillis = System.currentTimeMillis();
            long j = currentTimeMillis - this.lastAnimationTime;
            if (j > 17) {
                j = 17;
            }
            this.lastAnimationTime = currentTimeMillis;
            float f11 = this.animationProgress;
            if (f11 < 1.0f) {
                float f12 = f11 + (((float) j) / 800.0f);
                this.animationProgress = f12;
                if (!this.isOneShootAnimation) {
                    if (f12 >= 1.0f) {
                        if (this.showing) {
                            this.animationProgress = 0.0f;
                        } else {
                            this.animationProgress = 1.0f;
                        }
                    }
                } else if (f12 >= 1.0f) {
                    this.animationProgress = 0.0f;
                    this.animating = false;
                    this.time = 0L;
                    this.timeStr = null;
                    VideoForwardDrawableDelegate videoForwardDrawableDelegate = this.delegate;
                    if (videoForwardDrawableDelegate != null) {
                        videoForwardDrawableDelegate.onAnimationEnd();
                    }
                }
                invalidate();
            }
            if (this.isOneShootAnimation) {
                return;
            }
            boolean z = this.showing;
            if (z) {
                float f13 = this.enterAnimationProgress;
                if (f13 != 1.0f) {
                    this.enterAnimationProgress = f13 + 0.10666667f;
                    invalidate();
                    f = this.enterAnimationProgress;
                    if (f >= 0.0f) {
                        this.enterAnimationProgress = 0.0f;
                        return;
                    } else if (f <= 1.0f) {
                        return;
                    } else {
                        this.enterAnimationProgress = 1.0f;
                        return;
                    }
                }
            }
            if (!z) {
                float f14 = this.enterAnimationProgress;
                if (f14 != 0.0f) {
                    this.enterAnimationProgress = f14 - 0.10666667f;
                    invalidate();
                }
            }
            f = this.enterAnimationProgress;
            if (f >= 0.0f) {
            }
        }
    }

    public void setShowing(boolean z) {
        this.showing = z;
        invalidate();
    }

    private void invalidate() {
        VideoForwardDrawableDelegate videoForwardDrawableDelegate = this.delegate;
        if (videoForwardDrawableDelegate != null) {
            videoForwardDrawableDelegate.invalidate();
        } else {
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(32.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(32.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumWidth() {
        return AndroidUtilities.dp(32.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumHeight() {
        return AndroidUtilities.dp(32.0f);
    }

    public void addTime(long j) {
        long j2 = this.time + j;
        this.time = j2;
        this.timeStr = LocaleController.formatPluralString("Seconds", (int) (j2 / 1000), new Object[0]);
    }
}
