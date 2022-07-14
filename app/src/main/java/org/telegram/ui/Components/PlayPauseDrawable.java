package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AnimationUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes5.dex */
public class PlayPauseDrawable extends Drawable {
    private int alpha = 255;
    float duration = 300.0f;
    private long lastUpdateTime;
    private final Paint paint;
    private View parent;
    private boolean pause;
    private float progress;
    private final int size;

    public PlayPauseDrawable(int size) {
        this.size = AndroidUtilities.dp(size);
        Paint paint = new Paint(1);
        this.paint = paint;
        paint.setColor(-1);
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00ac  */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        float ms;
        float rotation;
        long newUpdateTime = AnimationUtils.currentAnimationTimeMillis();
        long dt = newUpdateTime - this.lastUpdateTime;
        this.lastUpdateTime = newUpdateTime;
        if (dt > 18) {
            dt = 16;
        }
        boolean z = this.pause;
        if (z) {
            float f = this.progress;
            if (f < 1.0f) {
                float f2 = f + (((float) dt) / this.duration);
                this.progress = f2;
                if (f2 >= 1.0f) {
                    this.progress = 1.0f;
                } else {
                    View view = this.parent;
                    if (view != null) {
                        view.invalidate();
                    }
                    invalidateSelf();
                }
                android.graphics.Rect bounds = getBounds();
                if (this.alpha != 255) {
                    canvas.save();
                } else {
                    canvas.saveLayerAlpha(bounds.left, bounds.top, bounds.right, bounds.bottom, this.alpha, 31);
                }
                canvas.translate(bounds.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - this.progress)), bounds.centerY());
                ms = this.progress * 500.0f;
                if (ms >= 100.0f) {
                    rotation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(ms / 100.0f) * (-5.0f);
                } else {
                    rotation = ms < 484.0f ? (CubicBezierInterpolator.EASE_BOTH.getInterpolation((ms - 100.0f) / 384.0f) * 95.0f) - 5.0f : 90.0f;
                }
                canvas.scale((this.size * 1.45f) / AndroidUtilities.dp(28.0f), (this.size * 1.5f) / AndroidUtilities.dp(28.0f));
                canvas.rotate(rotation);
                Theme.playPauseAnimator.draw(canvas, this.paint, ms);
                canvas.scale(1.0f, -1.0f);
                Theme.playPauseAnimator.draw(canvas, this.paint, ms);
                canvas.restore();
            }
        }
        if (!z) {
            float f3 = this.progress;
            if (f3 > 0.0f) {
                float f4 = f3 - (((float) dt) / this.duration);
                this.progress = f4;
                if (f4 <= 0.0f) {
                    this.progress = 0.0f;
                } else {
                    View view2 = this.parent;
                    if (view2 != null) {
                        view2.invalidate();
                    }
                    invalidateSelf();
                }
            }
        }
        android.graphics.Rect bounds2 = getBounds();
        if (this.alpha != 255) {
        }
        canvas.translate(bounds2.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - this.progress)), bounds2.centerY());
        ms = this.progress * 500.0f;
        if (ms >= 100.0f) {
        }
        canvas.scale((this.size * 1.45f) / AndroidUtilities.dp(28.0f), (this.size * 1.5f) / AndroidUtilities.dp(28.0f));
        canvas.rotate(rotation);
        Theme.playPauseAnimator.draw(canvas, this.paint, ms);
        canvas.scale(1.0f, -1.0f);
        Theme.playPauseAnimator.draw(canvas, this.paint, ms);
        canvas.restore();
    }

    public void setPause(boolean pause) {
        setPause(pause, true);
    }

    public void setPause(boolean pause, boolean animated) {
        if (this.pause != pause) {
            this.pause = pause;
            if (!animated) {
                this.progress = pause ? 1.0f : 0.0f;
            }
            this.lastUpdateTime = AnimationUtils.currentAnimationTimeMillis();
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.alpha = i;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.size;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.size;
    }

    public void setParent(View parent) {
        this.parent = parent;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
