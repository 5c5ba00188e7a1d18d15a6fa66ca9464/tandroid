package org.telegram.ui.Components.Paint;

import android.graphics.RectF;
import org.telegram.ui.Components.Paint.Brush;

/* loaded from: classes3.dex */
public class Shape {
    public float arrowTriangleLength;
    public final Brush.Shape brush;
    public float centerX;
    public float centerY;
    public boolean fill;
    public float middleX;
    public float middleY;
    public float radiusX;
    public float radiusY;
    public float rotation;
    public float rounding;
    public float thickness;

    public Shape(Brush.Shape shape) {
        this.brush = shape;
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x004c, code lost:
    
        if (getType() == 3) goto L5;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void getBounds(RectF rectF) {
        if (getType() == 4) {
            float f = this.centerX;
            float f2 = this.arrowTriangleLength;
            float f3 = this.centerY;
            rectF.set(f - f2, f3 - f2, f + f2, f3 + f2);
            rectF.union(this.radiusX, this.radiusY);
        } else {
            float max = Math.max(Math.abs(this.radiusX), Math.abs(this.radiusY));
            float f4 = this.centerX;
            float f5 = max * 1.42f;
            float f6 = this.centerY;
            rectF.set(f4 - f5, f6 - f5, f4 + f5, f6 + f5);
        }
        rectF.union(this.middleX, this.middleY);
        float f7 = (-this.thickness) - 3.0f;
        rectF.inset(f7, f7);
    }

    public int getType() {
        return this.brush.getShapeShaderType();
    }
}
