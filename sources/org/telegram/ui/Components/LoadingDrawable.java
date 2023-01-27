package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class LoadingDrawable extends Drawable {
    private boolean appearByGradient;
    private LinearGradient appearGradient;
    private int appearGradientWidth;
    private Matrix appearMatrix;
    private Paint appearPaint;
    public Paint backgroundPaint;
    public Integer color1;
    public Integer color2;
    public String colorKey1;
    public String colorKey2;
    private LinearGradient disappearGradient;
    private int disappearGradientWidth;
    private Matrix disappearMatrix;
    private Paint disappearPaint;
    private long disappearStart;
    private LinearGradient gradient;
    private int gradientColor1;
    private int gradientColor2;
    private int gradientStrokeColor1;
    private int gradientStrokeColor2;
    private int gradientWidth;
    private float gradientWidthScale;
    private android.graphics.Rect lastBounds;
    private Matrix matrix;
    public Paint paint;
    private Path path;
    private float[] radii;
    private RectF rectF;
    public Theme.ResourcesProvider resourcesProvider;
    private float speed;
    private long start;
    public boolean stroke;
    public Integer strokeColor1;
    public Integer strokeColor2;
    private LinearGradient strokeGradient;
    private Matrix strokeMatrix;
    public Paint strokePaint;
    private Path usePath;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    public LoadingDrawable(Theme.ResourcesProvider resourcesProvider) {
        this();
        this.resourcesProvider = resourcesProvider;
    }

    public LoadingDrawable() {
        this.start = -1L;
        this.disappearStart = -1L;
        this.matrix = new Matrix();
        this.strokeMatrix = new Matrix();
        this.colorKey1 = "dialogBackground";
        this.colorKey2 = "dialogBackgroundGray";
        this.gradientWidthScale = 1.0f;
        this.speed = 1.0f;
        this.paint = new Paint(1);
        this.strokePaint = new Paint(1);
        this.path = new Path();
        this.radii = new float[8];
        this.rectF = new RectF();
        this.strokePaint.setStyle(Paint.Style.STROKE);
        this.strokePaint.setStrokeWidth(AndroidUtilities.density > 2.0f ? 2.0f : 1.0f);
    }

    public void setColors(int i, int i2) {
        this.color1 = Integer.valueOf(i);
        this.color2 = Integer.valueOf(i2);
        this.stroke = false;
    }

    public void setColors(int i, int i2, int i3, int i4) {
        this.color1 = Integer.valueOf(i);
        this.color2 = Integer.valueOf(i2);
        this.stroke = true;
        this.strokeColor1 = Integer.valueOf(i3);
        this.strokeColor2 = Integer.valueOf(i4);
    }

    public boolean isDisappearing() {
        return this.disappearStart > 0 && ((float) (SystemClock.elapsedRealtime() - this.disappearStart)) < 320.0f;
    }

    public boolean isDisappeared() {
        return this.disappearStart > 0 && ((float) (SystemClock.elapsedRealtime() - this.disappearStart)) >= 320.0f;
    }

    public long timeToDisappear() {
        if (this.disappearStart > 0) {
            return 320 - (SystemClock.elapsedRealtime() - this.disappearStart);
        }
        return 0L;
    }

    public void usePath(Path path) {
        this.usePath = path;
    }

    public void setGradientScale(float f) {
        this.gradientWidthScale = f;
    }

    public void setSpeed(float f) {
        this.speed = f;
    }

    public void setAppearByGradient(boolean z) {
        this.appearByGradient = z;
    }

    public void setRadiiDp(float f) {
        if (this.usePath != null) {
            this.paint.setPathEffect(new CornerPathEffect(AndroidUtilities.dp(f)));
            this.strokePaint.setPathEffect(new CornerPathEffect(AndroidUtilities.dp(f)));
            return;
        }
        setRadiiDp(f, f, f, f);
    }

    public void setRadiiDp(float f, float f2, float f3, float f4) {
        setRadii(AndroidUtilities.dp(f), AndroidUtilities.dp(f2), AndroidUtilities.dp(f3), AndroidUtilities.dp(f4));
    }

    public void setRadii(float f, float f2, float f3, float f4) {
        float[] fArr = this.radii;
        boolean z = (fArr[0] == f && fArr[2] == f2 && fArr[4] == f3 && fArr[6] == f4) ? false : true;
        fArr[1] = f;
        fArr[0] = f;
        fArr[3] = f2;
        fArr[2] = f2;
        fArr[5] = f3;
        fArr[4] = f3;
        fArr[7] = f4;
        fArr[6] = f4;
        if (this.lastBounds == null || !z) {
            return;
        }
        this.path.rewind();
        this.rectF.set(this.lastBounds);
        this.path.addRoundRect(this.rectF, this.radii, Path.Direction.CW);
    }

    public void setBounds(RectF rectF) {
        super.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
    }

    public void reset() {
        this.start = -1L;
    }

    public void disappear() {
        if (isDisappeared() || isDisappearing()) {
            return;
        }
        this.disappearStart = SystemClock.elapsedRealtime();
    }

    public void resetDisappear() {
        this.disappearStart = -1L;
    }

    /* JADX WARN: Removed duplicated region for block: B:61:0x022c  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x02fc  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0323  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x032f  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x03d8  */
    /* JADX WARN: Removed duplicated region for block: B:99:? A[RETURN, SYNTHETIC] */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        Canvas canvas2;
        float f;
        Path path;
        Paint paint;
        if (isDisappeared()) {
            return;
        }
        android.graphics.Rect bounds = getBounds();
        if (getPaintAlpha() <= 0) {
            return;
        }
        int width = bounds.width();
        if (width <= 0) {
            width = AndroidUtilities.dp(200.0f);
        }
        int min = (int) (Math.min(AndroidUtilities.dp(400.0f), width) * this.gradientWidthScale);
        Integer num = this.color1;
        int intValue = num != null ? num.intValue() : Theme.getColor(this.colorKey1, this.resourcesProvider);
        Integer num2 = this.color2;
        int intValue2 = num2 != null ? num2.intValue() : Theme.getColor(this.colorKey2, this.resourcesProvider);
        Integer num3 = this.strokeColor1;
        int intValue3 = num3 != null ? num3.intValue() : Theme.getColor(this.colorKey1, this.resourcesProvider);
        Integer num4 = this.strokeColor2;
        int intValue4 = num4 != null ? num4.intValue() : Theme.getColor(this.colorKey2, this.resourcesProvider);
        if (this.gradient == null || min != this.gradientWidth || intValue != this.gradientColor1 || intValue2 != this.gradientColor2 || intValue3 != this.gradientStrokeColor1 || intValue4 != this.gradientStrokeColor2) {
            this.gradientWidth = min;
            this.gradientColor1 = intValue;
            this.gradientColor2 = intValue2;
            int i = this.gradientColor1;
            LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, this.gradientWidth, 0.0f, new int[]{i, this.gradientColor2, i}, new float[]{0.0f, 0.67f, 1.0f}, Shader.TileMode.REPEAT);
            this.gradient = linearGradient;
            linearGradient.setLocalMatrix(this.matrix);
            this.paint.setShader(this.gradient);
            this.gradientStrokeColor1 = intValue3;
            this.gradientStrokeColor2 = intValue4;
            int i2 = this.gradientStrokeColor1;
            LinearGradient linearGradient2 = new LinearGradient(0.0f, 0.0f, this.gradientWidth, 0.0f, new int[]{i2, i2, this.gradientStrokeColor2, i2}, new float[]{0.0f, 0.4f, 0.67f, 1.0f}, Shader.TileMode.REPEAT);
            this.strokeGradient = linearGradient2;
            linearGradient2.setLocalMatrix(this.strokeMatrix);
            this.strokePaint.setShader(this.strokeGradient);
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (this.start < 0) {
            this.start = elapsedRealtime;
        }
        float pow = ((float) Math.pow(((((float) (elapsedRealtime - this.start)) / 2000.0f) * this.speed) / 4.0f, 0.8500000238418579d)) * 4.0f * AndroidUtilities.density;
        int i3 = this.gradientWidth;
        float f2 = (pow * i3) % i3;
        float f3 = ((float) (elapsedRealtime - this.start)) / 550.0f;
        long j = this.disappearStart;
        float interpolation = j > 0 ? 1.0f - CubicBezierInterpolator.EASE_OUT.getInterpolation(Math.min(1.0f, ((float) (elapsedRealtime - j)) / 320.0f)) : 0.0f;
        if (isDisappearing()) {
            int max = Math.max(AndroidUtilities.dp(200.0f), bounds.width() / 3);
            if (interpolation < 1.0f) {
                if (this.disappearPaint == null) {
                    f = 1.0f;
                    this.disappearPaint = new Paint(1);
                    this.disappearGradientWidth = max;
                    this.disappearGradient = new LinearGradient(0.0f, 0.0f, max, 0.0f, new int[]{-1, 16777215}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                    Matrix matrix = new Matrix();
                    this.disappearMatrix = matrix;
                    this.disappearGradient.setLocalMatrix(matrix);
                    this.disappearPaint.setShader(this.disappearGradient);
                    this.disappearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                } else {
                    f = 1.0f;
                    if (this.disappearGradientWidth != max) {
                        this.disappearGradientWidth = max;
                        LinearGradient linearGradient3 = new LinearGradient(0.0f, 0.0f, max, 0.0f, new int[]{-1, 16777215}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                        this.disappearGradient = linearGradient3;
                        linearGradient3.setLocalMatrix(this.disappearMatrix);
                        this.disappearPaint.setShader(this.disappearGradient);
                    }
                }
                this.rectF.set(bounds);
                this.rectF.inset(-this.strokePaint.getStrokeWidth(), -this.strokePaint.getStrokeWidth());
                canvas2 = canvas;
                canvas2.saveLayerAlpha(this.rectF, 255, 31);
                if (this.appearByGradient) {
                    int max2 = Math.max(AndroidUtilities.dp(200.0f), bounds.width() / 3);
                    if (f3 < f) {
                        if (this.appearPaint == null) {
                            this.appearPaint = new Paint(1);
                            this.appearGradientWidth = max2;
                            this.appearGradient = new LinearGradient(0.0f, 0.0f, max2, 0.0f, new int[]{16777215, -1}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                            Matrix matrix2 = new Matrix();
                            this.appearMatrix = matrix2;
                            this.appearGradient.setLocalMatrix(matrix2);
                            this.appearPaint.setShader(this.appearGradient);
                            this.appearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                        } else if (this.appearGradientWidth != max2) {
                            this.appearGradientWidth = max2;
                            LinearGradient linearGradient4 = new LinearGradient(0.0f, 0.0f, max2, 0.0f, new int[]{16777215, -1}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                            this.appearGradient = linearGradient4;
                            linearGradient4.setLocalMatrix(this.appearMatrix);
                            this.appearPaint.setShader(this.appearGradient);
                        }
                        this.rectF.set(bounds);
                        this.rectF.inset(-this.strokePaint.getStrokeWidth(), -this.strokePaint.getStrokeWidth());
                        canvas2.saveLayerAlpha(this.rectF, 255, 31);
                    }
                }
                this.matrix.setTranslate(f2, 0.0f);
                this.gradient.setLocalMatrix(this.matrix);
                this.strokeMatrix.setTranslate(f2, 0.0f);
                this.strokeGradient.setLocalMatrix(this.strokeMatrix);
                path = this.usePath;
                if (path == null) {
                    android.graphics.Rect rect = this.lastBounds;
                    if (rect == null || !rect.equals(bounds)) {
                        this.path.rewind();
                        RectF rectF = this.rectF;
                        this.lastBounds = bounds;
                        rectF.set(bounds);
                        this.path.addRoundRect(this.rectF, this.radii, Path.Direction.CW);
                    }
                    path = this.path;
                }
                paint = this.backgroundPaint;
                if (paint != null) {
                    canvas2.drawPath(path, paint);
                }
                canvas2.drawPath(path, this.paint);
                if (this.stroke) {
                    canvas2.drawPath(path, this.strokePaint);
                }
                if (isDisappearing() && interpolation < f) {
                    canvas.save();
                    int width2 = this.disappearGradientWidth + bounds.width();
                    int i4 = this.disappearGradientWidth;
                    this.disappearMatrix.setTranslate(bounds.right - ((interpolation * (width2 + i4)) - i4), 0.0f);
                    this.disappearGradient.setLocalMatrix(this.disappearMatrix);
                    int strokeWidth = (int) this.strokePaint.getStrokeWidth();
                    canvas.drawRect(bounds.left - strokeWidth, bounds.top - strokeWidth, bounds.right + strokeWidth, bounds.bottom + strokeWidth, this.disappearPaint);
                    canvas.restore();
                    canvas.restore();
                }
                if (this.appearByGradient && f3 < f) {
                    canvas.save();
                    int width3 = this.appearGradientWidth + bounds.width();
                    int i5 = this.appearGradientWidth;
                    this.appearMatrix.setTranslate(bounds.left + ((f3 * (width3 + i5)) - i5), 0.0f);
                    this.appearGradient.setLocalMatrix(this.appearMatrix);
                    int strokeWidth2 = (int) this.strokePaint.getStrokeWidth();
                    canvas.drawRect(bounds.left - strokeWidth2, bounds.top - strokeWidth2, bounds.right + strokeWidth2, bounds.bottom + strokeWidth2, this.appearPaint);
                    canvas.restore();
                    canvas.restore();
                }
                if (isDisappeared()) {
                    invalidateSelf();
                    return;
                }
                return;
            }
        }
        canvas2 = canvas;
        f = 1.0f;
        if (this.appearByGradient) {
        }
        this.matrix.setTranslate(f2, 0.0f);
        this.gradient.setLocalMatrix(this.matrix);
        this.strokeMatrix.setTranslate(f2, 0.0f);
        this.strokeGradient.setLocalMatrix(this.strokeMatrix);
        path = this.usePath;
        if (path == null) {
        }
        paint = this.backgroundPaint;
        if (paint != null) {
        }
        canvas2.drawPath(path, this.paint);
        if (this.stroke) {
        }
        if (isDisappearing()) {
            canvas.save();
            int width22 = this.disappearGradientWidth + bounds.width();
            int i42 = this.disappearGradientWidth;
            this.disappearMatrix.setTranslate(bounds.right - ((interpolation * (width22 + i42)) - i42), 0.0f);
            this.disappearGradient.setLocalMatrix(this.disappearMatrix);
            int strokeWidth3 = (int) this.strokePaint.getStrokeWidth();
            canvas.drawRect(bounds.left - strokeWidth3, bounds.top - strokeWidth3, bounds.right + strokeWidth3, bounds.bottom + strokeWidth3, this.disappearPaint);
            canvas.restore();
            canvas.restore();
        }
        if (this.appearByGradient) {
            canvas.save();
            int width32 = this.appearGradientWidth + bounds.width();
            int i52 = this.appearGradientWidth;
            this.appearMatrix.setTranslate(bounds.left + ((f3 * (width32 + i52)) - i52), 0.0f);
            this.appearGradient.setLocalMatrix(this.appearMatrix);
            int strokeWidth22 = (int) this.strokePaint.getStrokeWidth();
            canvas.drawRect(bounds.left - strokeWidth22, bounds.top - strokeWidth22, bounds.right + strokeWidth22, bounds.bottom + strokeWidth22, this.appearPaint);
            canvas.restore();
            canvas.restore();
        }
        if (isDisappeared()) {
        }
    }

    public void updateBounds() {
        Path path = this.usePath;
        if (path != null) {
            RectF rectF = AndroidUtilities.rectTmp;
            path.computeBounds(rectF, false);
            setBounds(rectF);
        }
    }

    public int getPaintAlpha() {
        return this.paint.getAlpha();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.paint.setAlpha(i);
        if (i > 0) {
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }
}
