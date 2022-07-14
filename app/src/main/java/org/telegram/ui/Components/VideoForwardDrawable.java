package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
/* loaded from: classes5.dex */
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

    /* loaded from: classes5.dex */
    public interface VideoForwardDrawableDelegate {
        void invalidate();

        void onAnimationEnd();
    }

    public void setTime(long dt) {
        this.time = dt;
        if (dt >= 1000) {
            this.timeStr = LocaleController.formatPluralString("Seconds", (int) (dt / 1000), new Object[0]);
        } else {
            this.timeStr = null;
        }
    }

    public VideoForwardDrawable(boolean isRound) {
        this.isRound = isRound;
        this.paint.setColor(-1);
        this.textPaint.setColor(-1);
        this.textPaint.setTextSize(AndroidUtilities.dp(12.0f));
        this.textPaint.setTextAlign(Paint.Align.CENTER);
        this.path1.reset();
        int a = 0;
        while (true) {
            int[] iArr = playPath;
            if (a < iArr.length / 2) {
                if (a == 0) {
                    this.path1.moveTo(AndroidUtilities.dp(iArr[a * 2]), AndroidUtilities.dp(iArr[(a * 2) + 1]));
                } else {
                    this.path1.lineTo(AndroidUtilities.dp(iArr[a * 2]), AndroidUtilities.dp(iArr[(a * 2) + 1]));
                }
                a++;
            } else {
                this.path1.close();
                return;
            }
        }
    }

    public void setPlayScaleFactor(float playScaleFactor) {
        this.playScaleFactor = playScaleFactor;
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

    public void setOneShootAnimation(boolean isOneShootAnimation) {
        if (this.isOneShootAnimation != isOneShootAnimation) {
            this.isOneShootAnimation = isOneShootAnimation;
            this.timeStr = null;
            this.time = 0L;
            this.animationProgress = 0.0f;
        }
    }

    public void setLeftSide(boolean value) {
        boolean z = this.leftSide;
        if (z == value && this.animationProgress >= 1.0f && this.isOneShootAnimation) {
            return;
        }
        if (z != value) {
            this.time = 0L;
            this.timeStr = null;
        }
        this.leftSide = value;
        startAnimation();
    }

    public void setDelegate(VideoForwardDrawableDelegate videoForwardDrawableDelegate) {
        this.delegate = videoForwardDrawableDelegate;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        this.paint.setAlpha(alpha);
        this.textPaint.setAlpha(alpha);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }

    public void setColor(int value) {
        this.paint.setColor(value);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    /* JADX WARN: Removed duplicated region for block: B:104:0x02af  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x02b2  */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        int x;
        float f;
        int a;
        int a2;
        int a3;
        android.graphics.Rect rect = getBounds();
        int x2 = rect.left + ((rect.width() - getIntrinsicWidth()) / 2);
        int y = rect.top + ((rect.height() - getIntrinsicHeight()) / 2);
        if (this.leftSide) {
            x = x2 - ((rect.width() / 4) - AndroidUtilities.dp(16.0f));
        } else {
            x = x2 + (rect.width() / 4) + AndroidUtilities.dp(16.0f);
        }
        canvas.save();
        if (this.isRound) {
            if (this.clippingPath == null) {
                this.clippingPath = new Path();
            }
            int clippingPathHash = rect.left + (rect.top << 8) + (rect.bottom << 16) + (rect.right << 24);
            if (this.lastClippingPath != clippingPathHash) {
                this.clippingPath.reset();
                AndroidUtilities.rectTmp.set(rect);
                this.clippingPath.addOval(AndroidUtilities.rectTmp, Path.Direction.CCW);
                this.lastClippingPath = clippingPathHash;
            }
            canvas.clipPath(this.clippingPath);
        } else {
            canvas.clipRect(rect.left, rect.top, rect.right, rect.bottom);
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
        int i = -1;
        canvas.drawCircle(((Math.max(rect.width(), rect.height()) / 4) * (this.leftSide ? -1 : 1)) + x, AndroidUtilities.dp(16.0f) + y, Math.max(rect.width(), rect.height()) / 2, this.paint);
        canvas.restore();
        String str = this.timeStr;
        if (str != null) {
            int intrinsicWidth = getIntrinsicWidth();
            if (!this.leftSide) {
                i = 1;
            }
            canvas.drawText(str, (intrinsicWidth * i) + x, getIntrinsicHeight() + y + AndroidUtilities.dp(15.0f), this.textPaint);
        }
        canvas.save();
        float f3 = this.playScaleFactor;
        canvas.scale(f3, f3, x, y + (getIntrinsicHeight() / 2.0f));
        if (this.leftSide) {
            canvas.rotate(180.0f, x, (getIntrinsicHeight() / 2) + y);
        }
        canvas.translate(x, y);
        float f4 = this.animationProgress;
        if (f4 <= 0.6f) {
            if (f4 < 0.4f) {
                a3 = Math.min(255, (int) ((f4 * 255.0f) / 0.2f));
            } else {
                a3 = (int) ((1.0f - ((f4 - 0.4f) / 0.2f)) * 255.0f);
            }
            if (!this.isOneShootAnimation) {
                a3 = (int) (a3 * this.enterAnimationProgress);
            }
            this.paint.setAlpha(a3);
            canvas.drawPath(this.path1, this.paint);
        }
        canvas.translate(AndroidUtilities.dp(18.0f), 0.0f);
        float f5 = this.animationProgress;
        if (f5 >= 0.2f && f5 <= 0.8f) {
            float progress = f5 - 0.2f;
            if (progress < 0.4f) {
                a2 = Math.min(255, (int) ((progress * 255.0f) / 0.2f));
            } else {
                a2 = (int) ((1.0f - ((progress - 0.4f) / 0.2f)) * 255.0f);
            }
            if (!this.isOneShootAnimation) {
                a2 = (int) (a2 * this.enterAnimationProgress);
            }
            this.paint.setAlpha(a2);
            canvas.drawPath(this.path1, this.paint);
        }
        canvas.translate(AndroidUtilities.dp(18.0f), 0.0f);
        float f6 = this.animationProgress;
        if (f6 >= 0.4f && f6 <= 1.0f) {
            float progress2 = f6 - 0.4f;
            if (progress2 < 0.4f) {
                a = Math.min(255, (int) ((255.0f * progress2) / 0.2f));
            } else {
                a = (int) ((1.0f - ((progress2 - 0.4f) / 0.2f)) * 255.0f);
            }
            if (!this.isOneShootAnimation) {
                a = (int) (a * this.enterAnimationProgress);
            }
            this.paint.setAlpha(a);
            canvas.drawPath(this.path1, this.paint);
        }
        canvas.restore();
        if (this.animating) {
            long newTime = System.currentTimeMillis();
            long dt = newTime - this.lastAnimationTime;
            if (dt > 17) {
                dt = 17;
            }
            this.lastAnimationTime = newTime;
            float f7 = this.animationProgress;
            if (f7 < 1.0f) {
                float f8 = f7 + (((float) dt) / 800.0f);
                this.animationProgress = f8;
                if (!this.isOneShootAnimation) {
                    if (f8 >= 1.0f) {
                        if (this.showing) {
                            this.animationProgress = 0.0f;
                        } else {
                            this.animationProgress = 1.0f;
                        }
                    }
                } else if (f8 >= 1.0f) {
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
            if (!this.isOneShootAnimation) {
                boolean z = this.showing;
                if (z) {
                    float f9 = this.enterAnimationProgress;
                    if (f9 != 1.0f) {
                        this.enterAnimationProgress = f9 + 0.10666667f;
                        invalidate();
                        f = this.enterAnimationProgress;
                        if (f >= 0.0f) {
                            this.enterAnimationProgress = 0.0f;
                            return;
                        } else if (f > 1.0f) {
                            this.enterAnimationProgress = 1.0f;
                            return;
                        } else {
                            return;
                        }
                    }
                }
                if (!z) {
                    float f10 = this.enterAnimationProgress;
                    if (f10 != 0.0f) {
                        this.enterAnimationProgress = f10 - 0.10666667f;
                        invalidate();
                    }
                }
                f = this.enterAnimationProgress;
                if (f >= 0.0f) {
                }
            }
        }
    }

    public void setShowing(boolean showing) {
        this.showing = showing;
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

    public void addTime(long time) {
        long j = this.time + time;
        this.time = j;
        this.timeStr = LocaleController.formatPluralString("Seconds", (int) (j / 1000), new Object[0]);
    }
}
