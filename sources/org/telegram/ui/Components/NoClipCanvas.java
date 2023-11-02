package org.telegram.ui.Components;

import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RenderNode;
import android.graphics.fonts.Font;
import android.graphics.text.MeasuredText;
import android.os.Build;
/* loaded from: classes4.dex */
public class NoClipCanvas extends Canvas {
    public Canvas canvas;

    @Override // android.graphics.Canvas
    public boolean clipRect(float f, float f2, float f3, float f4) {
        return false;
    }

    @Override // android.graphics.Canvas
    public boolean clipRect(float f, float f2, float f3, float f4, Region.Op op) {
        return false;
    }

    @Override // android.graphics.Canvas
    public boolean clipRect(int i, int i2, int i3, int i4) {
        return false;
    }

    @Override // android.graphics.Canvas
    public boolean clipRect(android.graphics.Rect rect) {
        return false;
    }

    @Override // android.graphics.Canvas
    public boolean clipRect(android.graphics.Rect rect, Region.Op op) {
        return false;
    }

    @Override // android.graphics.Canvas
    public boolean clipRect(RectF rectF) {
        return false;
    }

    @Override // android.graphics.Canvas
    public boolean clipRect(RectF rectF, Region.Op op) {
        return false;
    }

    @Override // android.graphics.Canvas
    public int save() {
        return this.canvas.save();
    }

    @Override // android.graphics.Canvas
    public void translate(float f, float f2) {
        this.canvas.translate(f, f2);
    }

    @Override // android.graphics.Canvas
    public void scale(float f, float f2) {
        this.canvas.scale(f, f2);
    }

    @Override // android.graphics.Canvas
    public void rotate(float f) {
        this.canvas.rotate(f);
    }

    @Override // android.graphics.Canvas
    public void restore() {
        this.canvas.restore();
    }

    @Override // android.graphics.Canvas
    public void restoreToCount(int i) {
        this.canvas.restoreToCount(i);
    }

    @Override // android.graphics.Canvas
    public int getSaveCount() {
        return this.canvas.getSaveCount();
    }

    @Override // android.graphics.Canvas
    public void drawPath(Path path, Paint paint) {
        this.canvas.drawPath(path, paint);
    }

    @Override // android.graphics.Canvas
    public void drawText(String str, float f, float f2, Paint paint) {
        this.canvas.drawText(str, f, f2, paint);
    }

    @Override // android.graphics.Canvas
    public void drawText(String str, int i, int i2, float f, float f2, Paint paint) {
        this.canvas.drawText(str, i, i2, f, f2, paint);
    }

    @Override // android.graphics.Canvas
    public void drawText(char[] cArr, int i, int i2, float f, float f2, Paint paint) {
        this.canvas.drawText(cArr, i, i2, f, f2, paint);
    }

    @Override // android.graphics.Canvas
    public void drawTextOnPath(char[] cArr, int i, int i2, Path path, float f, float f2, Paint paint) {
        this.canvas.drawTextOnPath(cArr, i, i2, path, f, f2, paint);
    }

    @Override // android.graphics.Canvas
    public void drawText(CharSequence charSequence, int i, int i2, float f, float f2, Paint paint) {
        this.canvas.drawText(charSequence, i, i2, f, f2, paint);
    }

    @Override // android.graphics.Canvas
    public void drawLines(float[] fArr, int i, int i2, Paint paint) {
        this.canvas.drawLines(fArr, i, i2, paint);
    }

    @Override // android.graphics.Canvas
    public void drawLines(float[] fArr, Paint paint) {
        this.canvas.drawLines(fArr, paint);
    }

    @Override // android.graphics.Canvas
    public void drawRect(android.graphics.Rect rect, Paint paint) {
        this.canvas.drawRect(rect, paint);
    }

    @Override // android.graphics.Canvas
    public void drawRect(RectF rectF, Paint paint) {
        this.canvas.drawRect(rectF, paint);
    }

    @Override // android.graphics.Canvas
    public void drawCircle(float f, float f2, float f3, Paint paint) {
        this.canvas.drawCircle(f, f2, f3, paint);
    }

    @Override // android.graphics.Canvas
    public void drawRect(float f, float f2, float f3, float f4, Paint paint) {
        this.canvas.drawRect(f, f2, f3, f4, paint);
    }

    @Override // android.graphics.Canvas
    public void drawRoundRect(RectF rectF, float f, float f2, Paint paint) {
        this.canvas.drawRoundRect(rectF, f, f2, paint);
    }

    @Override // android.graphics.Canvas
    public void drawRoundRect(float f, float f2, float f3, float f4, float f5, float f6, Paint paint) {
        this.canvas.drawRoundRect(f, f2, f3, f4, f5, f6, paint);
    }

    @Override // android.graphics.Canvas
    public void drawTextRun(char[] cArr, int i, int i2, int i3, int i4, float f, float f2, boolean z, Paint paint) {
        this.canvas.drawTextRun(cArr, i, i2, i3, i4, f, f2, z, paint);
    }

    @Override // android.graphics.Canvas
    public void drawTextRun(MeasuredText measuredText, int i, int i2, int i3, int i4, float f, float f2, boolean z, Paint paint) {
        this.canvas.drawTextRun(measuredText, i, i2, i3, i4, f, f2, z, paint);
    }

    @Override // android.graphics.Canvas
    public void drawTextRun(CharSequence charSequence, int i, int i2, int i3, int i4, float f, float f2, boolean z, Paint paint) {
        this.canvas.drawTextRun(charSequence, i, i2, i3, i4, f, f2, z, paint);
    }

    @Override // android.graphics.Canvas
    public void drawTextOnPath(String str, Path path, float f, float f2, Paint paint) {
        this.canvas.drawTextOnPath(str, path, f, f2, paint);
    }

    @Override // android.graphics.Canvas
    public void drawBitmap(Bitmap bitmap, Matrix matrix, Paint paint) {
        this.canvas.drawBitmap(bitmap, matrix, paint);
    }

    @Override // android.graphics.Canvas
    public void drawBitmap(Bitmap bitmap, android.graphics.Rect rect, android.graphics.Rect rect2, Paint paint) {
        this.canvas.drawBitmap(bitmap, rect, rect2, paint);
    }

    @Override // android.graphics.Canvas
    public void drawBitmap(int[] iArr, int i, int i2, float f, float f2, int i3, int i4, boolean z, Paint paint) {
        this.canvas.drawBitmap(iArr, i, i2, f, f2, i3, i4, z, paint);
    }

    @Override // android.graphics.Canvas
    public void drawBitmap(int[] iArr, int i, int i2, int i3, int i4, int i5, int i6, boolean z, Paint paint) {
        this.canvas.drawBitmap(iArr, i, i2, i3, i4, i5, i6, z, paint);
    }

    @Override // android.graphics.Canvas
    public void drawArc(RectF rectF, float f, float f2, boolean z, Paint paint) {
        this.canvas.drawArc(rectF, f, f2, z, paint);
    }

    @Override // android.graphics.Canvas
    public void drawLine(float f, float f2, float f3, float f4, Paint paint) {
        this.canvas.drawLine(f, f2, f3, f4, paint);
    }

    @Override // android.graphics.Canvas
    public void drawVertices(Canvas.VertexMode vertexMode, int i, float[] fArr, int i2, float[] fArr2, int i3, int[] iArr, int i4, short[] sArr, int i5, int i6, Paint paint) {
        this.canvas.drawVertices(vertexMode, i, fArr, i2, fArr2, i3, iArr, i4, sArr, i5, i6, paint);
    }

    @Override // android.graphics.Canvas
    public void drawBitmap(Bitmap bitmap, android.graphics.Rect rect, RectF rectF, Paint paint) {
        this.canvas.drawBitmap(bitmap, rect, rectF, paint);
    }

    @Override // android.graphics.Canvas
    public void drawBitmap(Bitmap bitmap, float f, float f2, Paint paint) {
        this.canvas.drawBitmap(bitmap, f, f2, paint);
    }

    @Override // android.graphics.Canvas
    public void drawBitmapMesh(Bitmap bitmap, int i, int i2, float[] fArr, int i3, int[] iArr, int i4, Paint paint) {
        this.canvas.drawBitmapMesh(bitmap, i, i2, fArr, i3, iArr, i4, paint);
    }

    @Override // android.graphics.Canvas
    public void drawGlyphs(int[] iArr, int i, float[] fArr, int i2, int i3, Font font, Paint paint) {
        this.canvas.drawGlyphs(iArr, i, fArr, i2, i3, font, paint);
    }

    @Override // android.graphics.Canvas
    public void setBitmap(Bitmap bitmap) {
        this.canvas.setBitmap(bitmap);
    }

    @Override // android.graphics.Canvas
    public int getMaximumBitmapWidth() {
        return this.canvas.getMaximumBitmapWidth();
    }

    @Override // android.graphics.Canvas
    public void setMatrix(Matrix matrix) {
        this.canvas.setMatrix(matrix);
    }

    @Override // android.graphics.Canvas
    public int getMaximumBitmapHeight() {
        return this.canvas.getMaximumBitmapHeight();
    }

    @Override // android.graphics.Canvas
    public boolean getClipBounds(android.graphics.Rect rect) {
        return this.canvas.getClipBounds(rect);
    }

    @Override // android.graphics.Canvas
    public void getMatrix(Matrix matrix) {
        this.canvas.getMatrix(matrix);
    }

    @Override // android.graphics.Canvas
    public int getWidth() {
        return this.canvas.getWidth();
    }

    @Override // android.graphics.Canvas
    public int getHeight() {
        return this.canvas.getHeight();
    }

    @Override // android.graphics.Canvas
    public void drawColor(long j, BlendMode blendMode) {
        if (Build.VERSION.SDK_INT >= 29) {
            this.canvas.drawColor(j, blendMode);
        }
    }

    @Override // android.graphics.Canvas
    public void drawOval(RectF rectF, Paint paint) {
        this.canvas.drawOval(rectF, paint);
    }

    @Override // android.graphics.Canvas
    public void drawColor(int i, BlendMode blendMode) {
        if (Build.VERSION.SDK_INT >= 29) {
            this.canvas.drawColor(i, blendMode);
        }
    }

    @Override // android.graphics.Canvas
    public void drawPatch(NinePatch ninePatch, RectF rectF, Paint paint) {
        if (Build.VERSION.SDK_INT >= 31) {
            this.canvas.drawPatch(ninePatch, rectF, paint);
        }
    }

    @Override // android.graphics.Canvas
    public void drawPosText(String str, float[] fArr, Paint paint) {
        this.canvas.drawPosText(str, fArr, paint);
    }

    @Override // android.graphics.Canvas
    public void drawPosText(char[] cArr, int i, int i2, float[] fArr, Paint paint) {
        this.canvas.drawPosText(cArr, i, i2, fArr, paint);
    }

    @Override // android.graphics.Canvas
    public void drawColor(int i) {
        this.canvas.drawColor(i);
    }

    @Override // android.graphics.Canvas
    public void drawDoubleRoundRect(RectF rectF, float[] fArr, RectF rectF2, float[] fArr2, Paint paint) {
        if (Build.VERSION.SDK_INT >= 29) {
            this.canvas.drawDoubleRoundRect(rectF, fArr, rectF2, fArr2, paint);
        }
    }

    @Override // android.graphics.Canvas
    public void drawPicture(Picture picture, RectF rectF) {
        this.canvas.drawPicture(picture, rectF);
    }

    @Override // android.graphics.Canvas
    public void drawARGB(int i, int i2, int i3, int i4) {
        this.canvas.drawARGB(i, i2, i3, i4);
    }

    @Override // android.graphics.Canvas
    public void drawArc(float f, float f2, float f3, float f4, float f5, float f6, boolean z, Paint paint) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.canvas.drawArc(f, f2, f3, f4, f5, f6, z, paint);
        }
    }

    @Override // android.graphics.Canvas
    public void drawPatch(NinePatch ninePatch, android.graphics.Rect rect, Paint paint) {
        if (Build.VERSION.SDK_INT >= 31) {
            this.canvas.drawPatch(ninePatch, rect, paint);
        }
    }

    @Override // android.graphics.Canvas
    public void drawColor(int i, PorterDuff.Mode mode) {
        this.canvas.drawColor(i, mode);
    }

    @Override // android.graphics.Canvas
    public void drawRGB(int i, int i2, int i3) {
        this.canvas.drawRGB(i, i2, i3);
    }

    @Override // android.graphics.Canvas
    public void drawPoints(float[] fArr, int i, int i2, Paint paint) {
        this.canvas.drawPoints(fArr, i, i2, paint);
    }

    @Override // android.graphics.Canvas
    public void drawPoints(float[] fArr, Paint paint) {
        this.canvas.drawPoints(fArr, paint);
    }

    @Override // android.graphics.Canvas
    public void drawRenderNode(RenderNode renderNode) {
        if (Build.VERSION.SDK_INT >= 29) {
            this.canvas.drawRenderNode(renderNode);
        }
    }

    @Override // android.graphics.Canvas
    public void drawOval(float f, float f2, float f3, float f4, Paint paint) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.canvas.drawOval(f, f2, f3, f4, paint);
        }
    }

    @Override // android.graphics.Canvas
    public void drawDoubleRoundRect(RectF rectF, float f, float f2, RectF rectF2, float f3, float f4, Paint paint) {
        if (Build.VERSION.SDK_INT >= 29) {
            this.canvas.drawDoubleRoundRect(rectF, f, f2, rectF2, f3, f4, paint);
        }
    }

    @Override // android.graphics.Canvas
    public void drawPicture(Picture picture) {
        this.canvas.drawPicture(picture);
    }

    @Override // android.graphics.Canvas
    public void drawPicture(Picture picture, android.graphics.Rect rect) {
        this.canvas.drawPicture(picture, rect);
    }

    @Override // android.graphics.Canvas
    public void drawColor(long j) {
        if (Build.VERSION.SDK_INT >= 29) {
            this.canvas.drawColor(j);
        }
    }

    @Override // android.graphics.Canvas
    public void drawPaint(Paint paint) {
        this.canvas.drawPaint(paint);
    }

    @Override // android.graphics.Canvas
    public void drawPoint(float f, float f2, Paint paint) {
        this.canvas.drawPoint(f, f2, paint);
    }

    @Override // android.graphics.Canvas
    public DrawFilter getDrawFilter() {
        return this.canvas.getDrawFilter();
    }

    @Override // android.graphics.Canvas
    public void setDrawFilter(DrawFilter drawFilter) {
        this.canvas.setDrawFilter(drawFilter);
    }

    @Override // android.graphics.Canvas
    public int getDensity() {
        return this.canvas.getDensity();
    }

    @Override // android.graphics.Canvas
    public void setDensity(int i) {
        this.canvas.setDensity(i);
    }

    @Override // android.graphics.Canvas
    public int saveLayerAlpha(RectF rectF, int i, int i2) {
        return this.canvas.saveLayerAlpha(rectF, i, i2);
    }

    @Override // android.graphics.Canvas
    public int saveLayerAlpha(float f, float f2, float f3, float f4, int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.canvas.saveLayerAlpha(f, f2, f3, f4, i);
        }
        return getSaveCount();
    }

    @Override // android.graphics.Canvas
    public int saveLayerAlpha(RectF rectF, int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.canvas.saveLayerAlpha(rectF, i);
        }
        return getSaveCount();
    }

    @Override // android.graphics.Canvas
    public int saveLayer(float f, float f2, float f3, float f4, Paint paint) {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.canvas.saveLayer(f, f2, f3, f4, paint);
        }
        return getSaveCount();
    }

    @Override // android.graphics.Canvas
    public int saveLayer(RectF rectF, Paint paint) {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.canvas.saveLayer(rectF, paint);
        }
        return getSaveCount();
    }

    @Override // android.graphics.Canvas
    public int saveLayer(float f, float f2, float f3, float f4, Paint paint, int i) {
        return this.canvas.saveLayer(f, f2, f3, f4, paint, i);
    }

    @Override // android.graphics.Canvas
    public int saveLayer(RectF rectF, Paint paint, int i) {
        return this.canvas.saveLayer(rectF, paint, i);
    }

    @Override // android.graphics.Canvas
    public int saveLayerAlpha(float f, float f2, float f3, float f4, int i, int i2) {
        return this.canvas.saveLayerAlpha(f, f2, f3, f4, i, i2);
    }

    @Override // android.graphics.Canvas
    public boolean clipOutRect(float f, float f2, float f3, float f4) {
        if (Build.VERSION.SDK_INT >= 26) {
            return this.canvas.clipOutRect(f, f2, f3, f4);
        }
        return false;
    }

    @Override // android.graphics.Canvas
    public boolean clipOutRect(int i, int i2, int i3, int i4) {
        if (Build.VERSION.SDK_INT >= 26) {
            return this.canvas.clipOutRect(i, i2, i3, i4);
        }
        return false;
    }

    @Override // android.graphics.Canvas
    public boolean clipOutRect(RectF rectF) {
        if (Build.VERSION.SDK_INT >= 26) {
            return this.canvas.clipOutRect(rectF);
        }
        return false;
    }

    @Override // android.graphics.Canvas
    public boolean clipPath(Path path) {
        return this.canvas.clipPath(path);
    }

    @Override // android.graphics.Canvas
    public boolean clipOutPath(Path path) {
        if (Build.VERSION.SDK_INT >= 26) {
            return this.canvas.clipOutPath(path);
        }
        return false;
    }

    @Override // android.graphics.Canvas
    public boolean clipOutRect(android.graphics.Rect rect) {
        if (Build.VERSION.SDK_INT >= 26) {
            return this.canvas.clipOutRect(rect);
        }
        return false;
    }

    @Override // android.graphics.Canvas
    public boolean clipPath(Path path, Region.Op op) {
        return this.canvas.clipPath(path, op);
    }

    @Override // android.graphics.Canvas
    public void skew(float f, float f2) {
        this.canvas.skew(f, f2);
    }

    @Override // android.graphics.Canvas
    public void disableZ() {
        if (Build.VERSION.SDK_INT >= 29) {
            this.canvas.disableZ();
        }
    }

    @Override // android.graphics.Canvas
    public void enableZ() {
        if (Build.VERSION.SDK_INT >= 29) {
            this.canvas.enableZ();
        }
    }

    @Override // android.graphics.Canvas
    public boolean quickReject(float f, float f2, float f3, float f4) {
        if (Build.VERSION.SDK_INT >= 30) {
            return this.canvas.quickReject(f, f2, f3, f4);
        }
        return false;
    }

    @Override // android.graphics.Canvas
    public boolean quickReject(RectF rectF) {
        if (Build.VERSION.SDK_INT >= 30) {
            return this.canvas.quickReject(rectF);
        }
        return false;
    }

    @Override // android.graphics.Canvas
    public boolean quickReject(Path path) {
        if (Build.VERSION.SDK_INT >= 30) {
            return this.canvas.quickReject(path);
        }
        return false;
    }

    @Override // android.graphics.Canvas
    public boolean quickReject(RectF rectF, Canvas.EdgeType edgeType) {
        return this.canvas.quickReject(rectF, edgeType);
    }

    @Override // android.graphics.Canvas
    public boolean quickReject(Path path, Canvas.EdgeType edgeType) {
        return this.canvas.quickReject(path, edgeType);
    }

    @Override // android.graphics.Canvas
    public boolean quickReject(float f, float f2, float f3, float f4, Canvas.EdgeType edgeType) {
        return this.canvas.quickReject(f, f2, f3, f4, edgeType);
    }
}
