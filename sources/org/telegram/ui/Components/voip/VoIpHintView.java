package org.telegram.ui.Components.voip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import org.telegram.ui.Stories.recorder.HintView2;

/* loaded from: classes3.dex */
public class VoIpHintView extends HintView2 {
    private final VoIPBackgroundProvider backgroundProvider;
    private final Paint mainPaint;

    public VoIpHintView(Context context, int i, VoIPBackgroundProvider voIPBackgroundProvider, boolean z) {
        super(context, i);
        Paint paint = new Paint(1);
        this.mainPaint = paint;
        this.backgroundProvider = voIPBackgroundProvider;
        voIPBackgroundProvider.attach(this);
        paint.setPathEffect(new CornerPathEffect(this.rounding));
        if (z) {
            setCloseButton(true);
        }
    }

    @Override // org.telegram.ui.Stories.recorder.HintView2, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        this.backgroundProvider.setDarkTranslation(getX(), getY());
        super.dispatchDraw(canvas);
    }

    @Override // org.telegram.ui.Stories.recorder.HintView2
    protected void drawBgPath(Canvas canvas) {
        this.mainPaint.setShader(this.backgroundProvider.getDarkPaint().getShader());
        canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), Math.min(this.backgroundPaint.getAlpha(), this.backgroundProvider.getDarkPaint().getAlpha()), 31);
        canvas.drawPath(this.path, this.mainPaint);
        if (this.backgroundProvider.isReveal()) {
            this.mainPaint.setShader(this.backgroundProvider.getRevealDarkPaint().getShader());
            canvas.drawPath(this.path, this.mainPaint);
        }
        canvas.restore();
    }
}
