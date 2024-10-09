package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import org.telegram.messenger.AndroidUtilities;

/* loaded from: classes3.dex */
public class AnimatedArrowDrawable extends Drawable {
    private float animProgress;
    private float animateToProgress;
    private boolean isSmall;
    private long lastUpdateTime;
    private Paint paint;
    private Path path = new Path();

    public AnimatedArrowDrawable(int i, boolean z) {
        Paint paint = new Paint(1);
        this.paint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        this.paint.setColor(i);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        this.isSmall = z;
        updatePath();
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x002d, code lost:
    
        r6.animProgress = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x002b, code lost:
    
        if (r0 < r1) goto L11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0023, code lost:
    
        if (r0 > r1) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void checkAnimation() {
        if (this.animateToProgress != this.animProgress) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j = elapsedRealtime - this.lastUpdateTime;
            this.lastUpdateTime = elapsedRealtime;
            float f = this.animProgress;
            float f2 = this.animateToProgress;
            float f3 = ((float) j) / 180.0f;
            if (f < f2) {
                float f4 = f + f3;
                this.animProgress = f4;
            } else {
                float f5 = f - f3;
                this.animProgress = f5;
            }
            updatePath();
            invalidateSelf();
        }
    }

    private void updatePath() {
        this.path.reset();
        float f = (this.animProgress * 2.0f) - 1.0f;
        if (this.isSmall) {
            this.path.moveTo(AndroidUtilities.dp(3.0f), AndroidUtilities.dp(6.0f) - (AndroidUtilities.dp(2.0f) * f));
            this.path.lineTo(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(6.0f) + (AndroidUtilities.dp(2.0f) * f));
            this.path.lineTo(AndroidUtilities.dp(13.0f), AndroidUtilities.dp(6.0f) - (AndroidUtilities.dp(2.0f) * f));
        } else {
            this.path.moveTo(AndroidUtilities.dp(4.5f), AndroidUtilities.dp(12.0f) - (AndroidUtilities.dp(4.0f) * f));
            this.path.lineTo(AndroidUtilities.dp(13.0f), AndroidUtilities.dp(12.0f) + (AndroidUtilities.dp(4.0f) * f));
            this.path.lineTo(AndroidUtilities.dp(21.5f), AndroidUtilities.dp(12.0f) - (AndroidUtilities.dp(4.0f) * f));
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        canvas.drawPath(this.path, this.paint);
        checkAnimation();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(26.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(26.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    public void setAnimationProgress(float f) {
        this.animProgress = f;
        this.animateToProgress = f;
        updatePath();
        invalidateSelf();
    }

    public void setAnimationProgressAnimated(float f) {
        if (this.animateToProgress == f) {
            return;
        }
        this.animateToProgress = f;
        this.lastUpdateTime = SystemClock.elapsedRealtime();
        invalidateSelf();
    }

    public void setColor(int i) {
        this.paint.setColor(i);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }
}
