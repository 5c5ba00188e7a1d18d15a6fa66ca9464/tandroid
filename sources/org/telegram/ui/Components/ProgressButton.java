package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.widget.Button;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class ProgressButton extends Button {
    private int angle;
    private boolean drawProgress;
    private long lastUpdateTime;
    private float progressAlpha;
    private final Paint progressPaint;
    private final RectF progressRect;

    public ProgressButton(Context context) {
        super(context);
        setAllCaps(false);
        setTextSize(1, 14.0f);
        setTypeface(AndroidUtilities.bold());
        if (Build.VERSION.SDK_INT >= 21) {
            setOutlineProvider(null);
        }
        ViewHelper.setPadding(this, 8.0f, 0.0f, 8.0f, 0.0f);
        int dp = AndroidUtilities.dp(60.0f);
        setMinWidth(dp);
        setMinimumWidth(dp);
        this.progressRect = new RectF();
        Paint paint = new Paint(1);
        this.progressPaint = paint;
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(AndroidUtilities.dp(2.0f));
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0095, code lost:
        if (r10 > 1.0f) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0097, code lost:
        r9.progressAlpha = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x00a7, code lost:
        if (r10 < 0.0f) goto L15;
     */
    @Override // android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float f = 0.0f;
        if (this.drawProgress || this.progressAlpha != 0.0f) {
            int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp(11.0f);
            this.progressRect.set(measuredWidth, AndroidUtilities.dp(3.0f), measuredWidth + AndroidUtilities.dp(8.0f), AndroidUtilities.dp(11.0f));
            this.progressPaint.setAlpha(Math.min((int) NotificationCenter.closeSearchByActiveAction, (int) (this.progressAlpha * 255.0f)));
            canvas.drawArc(this.progressRect, this.angle, 220.0f, false, this.progressPaint);
            long currentTimeMillis = System.currentTimeMillis();
            if (Math.abs(this.lastUpdateTime - System.currentTimeMillis()) < 1000) {
                long j = currentTimeMillis - this.lastUpdateTime;
                int i = (int) (this.angle + (((float) (360 * j)) / 2000.0f));
                this.angle = i - ((i / 360) * 360);
                if (this.drawProgress) {
                    float f2 = this.progressAlpha;
                    f = 1.0f;
                    if (f2 < 1.0f) {
                        float f3 = f2 + (((float) j) / 200.0f);
                        this.progressAlpha = f3;
                    }
                } else {
                    float f4 = this.progressAlpha;
                    if (f4 > 0.0f) {
                        float f5 = f4 - (((float) j) / 200.0f);
                        this.progressAlpha = f5;
                    }
                }
            }
            this.lastUpdateTime = currentTimeMillis;
            postInvalidateOnAnimation();
        }
    }

    public void setBackgroundRoundRect(int i, int i2) {
        setBackgroundRoundRect(i, i2, 4.0f);
    }

    public void setBackgroundRoundRect(int i, int i2, float f) {
        setBackground(Theme.AdaptiveRipple.filledRect(i, f));
    }

    public void setDrawProgress(boolean z, boolean z2) {
        if (this.drawProgress != z) {
            this.drawProgress = z;
            if (!z2) {
                this.progressAlpha = z ? 1.0f : 0.0f;
            }
            this.lastUpdateTime = System.currentTimeMillis();
            invalidate();
        }
    }

    public void setProgressColor(int i) {
        this.progressPaint.setColor(i);
    }
}
