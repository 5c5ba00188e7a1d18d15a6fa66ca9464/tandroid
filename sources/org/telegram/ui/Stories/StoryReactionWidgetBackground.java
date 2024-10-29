package org.telegram.ui.Stories;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.CubicBezierInterpolator;

/* loaded from: classes4.dex */
public class StoryReactionWidgetBackground extends Drawable {
    Paint backgroundPaint;
    private boolean mirror;
    private final View parent;
    AnimatedFloat progressToMirrored;
    Paint shadowPaint;
    int style;
    private Paint xRefPaint;
    private final int STYLE_FILLED = 0;
    private final int STYLE_TRANSCLUENT = 1;
    int alpha = NotificationCenter.playerDidStartPlaying;
    float[] points = new float[15];
    Path path = new Path();

    public StoryReactionWidgetBackground(View view) {
        this.parent = view;
        this.progressToMirrored = new AnimatedFloat(view, 350L, CubicBezierInterpolator.EASE_OUT_QUINT);
        Paint paint = new Paint(1);
        this.shadowPaint = paint;
        paint.setShadowLayer(AndroidUtilities.dp(4.0f), 0.0f, 0.0f, 1593835520);
        Paint paint2 = new Paint(1);
        this.backgroundPaint = paint2;
        paint2.setColor(-1);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0205  */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        Paint paint;
        int alphaComponent;
        int i;
        Path path;
        float f;
        float f2;
        float f3;
        this.points[0] = getBounds().centerX();
        int i2 = 1;
        this.points[1] = getBounds().centerY();
        this.points[2] = getBounds().height() / 2.0f;
        int i3 = 3;
        this.points[3] = getBounds().left + (getBounds().width() * 1.027f);
        this.points[4] = getBounds().top + (getBounds().height() * 0.956f);
        this.points[5] = getBounds().height() * 0.055f;
        this.points[6] = getBounds().left + (getBounds().width() * 0.843f);
        this.points[7] = getBounds().top + (getBounds().height() * 0.812f);
        this.points[8] = getBounds().height() * 0.132f;
        this.points[9] = getBounds().left + (getBounds().width() * (-0.02699995f));
        this.points[10] = getBounds().top + (getBounds().height() * 0.956f);
        this.points[11] = getBounds().height() * 0.055f;
        this.points[12] = getBounds().left + (getBounds().width() * 0.157f);
        this.points[13] = getBounds().top + (getBounds().height() * 0.812f);
        this.points[14] = getBounds().height() * 0.132f;
        float f4 = this.progressToMirrored.set(this.mirror ? 1.0f : 0.0f);
        int i4 = this.style;
        if (i4 != 0) {
            if (i4 == 1) {
                if (this.xRefPaint == null) {
                    Paint paint2 = new Paint(1);
                    this.xRefPaint = paint2;
                    paint2.setColor(-16777216);
                    this.xRefPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    this.xRefPaint.setStrokeWidth(AndroidUtilities.dp(3.0f));
                }
                paint = this.backgroundPaint;
                alphaComponent = ColorUtils.setAlphaComponent(-16777216, NotificationCenter.dialogTranslate);
            }
            if (this.alpha == 255 || this.style == 1) {
                canvas.saveLayerAlpha(getBounds().left - (getBounds().width() * 0.2f), getBounds().top, getBounds().right + (getBounds().width() * 0.2f), getBounds().bottom + (getBounds().height() * 0.2f), this.alpha, 31);
            } else {
                canvas.save();
            }
            this.path.rewind();
            i = 0;
            while (i < 2) {
                if (this.style != i2 || i != 0) {
                    Paint paint3 = i == 0 ? this.shadowPaint : this.backgroundPaint;
                    int i5 = i == 0 ? 1 : 0;
                    int i6 = 0;
                    while (i6 < 5) {
                        if (i6 == i2 || i6 == 2) {
                            if (f4 != 1.0f) {
                                path = this.path;
                                float[] fArr = this.points;
                                int i7 = i6 * 3;
                                f = fArr[i7];
                                f2 = fArr[i7 + 1];
                                f3 = fArr[i7 + 2] * (1.0f - f4);
                                path.addCircle(f, f2, f3 - i5, Path.Direction.CW);
                            }
                        } else if (i6 != i3 && i6 != 4) {
                            Path path2 = this.path;
                            float[] fArr2 = this.points;
                            int i8 = i6 * 3;
                            path2.addCircle(fArr2[i8], fArr2[i8 + 1], fArr2[i8 + 2] - i5, Path.Direction.CW);
                        } else if (f4 != 0.0f) {
                            path = this.path;
                            float[] fArr3 = this.points;
                            int i9 = i6 * 3;
                            f = fArr3[i9];
                            f2 = fArr3[i9 + 1];
                            f3 = fArr3[i9 + 2] * f4;
                            path.addCircle(f, f2, f3 - i5, Path.Direction.CW);
                        }
                        i6++;
                        i2 = 1;
                        i3 = 3;
                    }
                    canvas.drawPath(this.path, paint3);
                }
                i++;
                i2 = 1;
                i3 = 3;
            }
            canvas.restore();
        }
        paint = this.backgroundPaint;
        alphaComponent = -1;
        paint.setColor(alphaComponent);
        if (this.alpha == 255) {
        }
        canvas.saveLayerAlpha(getBounds().left - (getBounds().width() * 0.2f), getBounds().top, getBounds().right + (getBounds().width() * 0.2f), getBounds().bottom + (getBounds().height() * 0.2f), this.alpha, 31);
        this.path.rewind();
        i = 0;
        while (i < 2) {
        }
        canvas.restore();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    public boolean isDarkStyle() {
        return this.style == 1;
    }

    public void nextStyle() {
        int i = this.style + 1;
        this.style = i;
        if (i >= 2) {
            this.style = 0;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.alpha = i;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public void setMirror(boolean z, boolean z2) {
        this.mirror = z;
        if (z2) {
            this.parent.invalidate();
        } else {
            this.progressToMirrored.set(z ? 1.0f : 0.0f, true);
        }
    }

    public void updateShadowLayer(float f) {
        this.shadowPaint.setShadowLayer(AndroidUtilities.dp(2.0f) / f, 0.0f, AndroidUtilities.dpf2(0.7f) / f, ColorUtils.setAlphaComponent(-16777216, 45));
    }
}
