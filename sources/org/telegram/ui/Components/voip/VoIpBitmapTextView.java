package org.telegram.ui.Components.voip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Utilities;

/* loaded from: classes3.dex */
public class VoIpBitmapTextView extends View {
    private volatile Bitmap bitmap;
    private final Paint paint;
    private final String text;
    private final TextPaint textPaint;
    private final float textWidth;

    public VoIpBitmapTextView(Context context, String str) {
        super(context);
        TextPaint textPaint = new TextPaint(1);
        this.textPaint = textPaint;
        this.paint = new Paint(1);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(AndroidUtilities.dp(13.0f));
        textPaint.setColor(-1);
        textPaint.setTypeface(AndroidUtilities.bold());
        this.textWidth = textPaint.measureText(str);
        this.text = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onLayout$0() {
        this.bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        new Canvas(this.bitmap).drawText(this.text, getMeasuredWidth() / 2, (int) ((getMeasuredHeight() / 2) - ((this.textPaint.descent() + this.textPaint.ascent()) / 2.0f)), this.textPaint);
        postInvalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.bitmap != null) {
            canvas.drawBitmap(this.bitmap, 0.0f, 0.0f, this.paint);
        }
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.voip.VoIpBitmapTextView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    VoIpBitmapTextView.this.lambda$onLayout$0();
                }
            });
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(((int) this.textWidth) + getPaddingLeft() + getPaddingRight(), 1073741824), View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824));
    }
}
