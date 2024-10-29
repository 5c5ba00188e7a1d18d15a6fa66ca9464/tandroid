package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AnimationUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.Theme;

/* loaded from: classes3.dex */
public class PlayPauseDrawable extends Drawable {
    private int alpha = NotificationCenter.playerDidStartPlaying;
    float duration = 300.0f;
    private long lastUpdateTime;
    private final Paint paint;
    private View parent;
    private boolean pause;
    private float progress;
    private final int size;

    public PlayPauseDrawable(int i) {
        this.size = AndroidUtilities.dp(i);
        Paint paint = new Paint(1);
        this.paint = paint;
        paint.setColor(-1);
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x002e, code lost:
    
        if (r0 != null) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x004f, code lost:
    
        invalidateSelf();
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x004c, code lost:
    
        r0.invalidate();
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x004a, code lost:
    
        if (r0 != null) goto L23;
     */
    /* JADX WARN: Removed duplicated region for block: B:13:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0099  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00e4  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00a4  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0060  */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        View view;
        int i;
        PathAnimator pathAnimator;
        long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
        long j = currentAnimationTimeMillis - this.lastUpdateTime;
        this.lastUpdateTime = currentAnimationTimeMillis;
        if (j > 18) {
            j = 16;
        }
        boolean z = this.pause;
        if (z) {
            float f = this.progress;
            if (f < 1.0f) {
                float f2 = f + (((float) j) / this.duration);
                this.progress = f2;
                if (f2 >= 1.0f) {
                    this.progress = 1.0f;
                    android.graphics.Rect bounds = getBounds();
                    i = this.alpha;
                    if (i != 255) {
                        canvas.save();
                    } else {
                        canvas.saveLayerAlpha(bounds.left, bounds.top, bounds.right, bounds.bottom, i, 31);
                    }
                    canvas.translate(bounds.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - this.progress)), bounds.centerY());
                    float f3 = this.progress * 500.0f;
                    float interpolation = f3 >= 100.0f ? CubicBezierInterpolator.EASE_BOTH.getInterpolation(f3 / 100.0f) * (-5.0f) : f3 < 484.0f ? (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f3 - 100.0f) / 384.0f) * 95.0f) - 5.0f : 90.0f;
                    canvas.scale((this.size * 1.45f) / AndroidUtilities.dp(28.0f), (this.size * 1.5f) / AndroidUtilities.dp(28.0f));
                    canvas.rotate(interpolation);
                    pathAnimator = Theme.playPauseAnimator;
                    if (pathAnimator != null) {
                        pathAnimator.draw(canvas, this.paint, f3);
                        canvas.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas, this.paint, f3);
                    }
                    canvas.restore();
                }
                view = this.parent;
            }
        }
        if (!z) {
            float f4 = this.progress;
            if (f4 > 0.0f) {
                float f5 = f4 - (((float) j) / this.duration);
                this.progress = f5;
                if (f5 <= 0.0f) {
                    this.progress = 0.0f;
                } else {
                    view = this.parent;
                }
            }
        }
        android.graphics.Rect bounds2 = getBounds();
        i = this.alpha;
        if (i != 255) {
        }
        canvas.translate(bounds2.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - this.progress)), bounds2.centerY());
        float f32 = this.progress * 500.0f;
        if (f32 >= 100.0f) {
        }
        canvas.scale((this.size * 1.45f) / AndroidUtilities.dp(28.0f), (this.size * 1.5f) / AndroidUtilities.dp(28.0f));
        canvas.rotate(interpolation);
        pathAnimator = Theme.playPauseAnimator;
        if (pathAnimator != null) {
        }
        canvas.restore();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.size;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.size;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.alpha = i;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }

    public void setDuration(int i) {
        this.duration = i;
    }

    public void setParent(View view) {
        this.parent = view;
    }

    public void setPause(boolean z) {
        setPause(z, true);
    }

    public void setPause(boolean z, boolean z2) {
        if (this.pause != z) {
            this.pause = z;
            if (!z2) {
                this.progress = z ? 1.0f : 0.0f;
            }
            this.lastUpdateTime = AnimationUtils.currentAnimationTimeMillis();
            invalidateSelf();
        }
    }
}
