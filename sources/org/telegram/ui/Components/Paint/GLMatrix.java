package org.telegram.ui.Components.Paint;

import android.graphics.Matrix;
/* loaded from: classes3.dex */
public abstract class GLMatrix {
    public static float[] LoadGraphicsMatrix(Matrix matrix) {
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        return new float[]{fArr[0], fArr[1], 0.0f, 0.0f, fArr[3], fArr[4], 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, fArr[2], fArr[5], 0.0f, 1.0f};
    }

    public static float[] LoadOrtho(float f, float f2, float f3, float f4, float f5, float f6) {
        float f7 = f2 - f;
        float f8 = f4 - f3;
        float f9 = f6 - f5;
        return new float[]{2.0f / f7, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f / f8, 0.0f, 0.0f, 0.0f, 0.0f, (-2.0f) / f9, 0.0f, (-(f2 + f)) / f7, (-(f4 + f3)) / f8, (-(f6 + f5)) / f9, 1.0f};
    }

    public static float[] MultiplyMat4f(float[] fArr, float[] fArr2) {
        float f = fArr[0];
        float f2 = fArr2[0];
        float f3 = fArr[4];
        float f4 = fArr2[1];
        float f5 = fArr[8];
        float f6 = fArr2[2];
        float f7 = fArr[12];
        float f8 = fArr2[3];
        float f9 = (f * f2) + (f3 * f4) + (f5 * f6) + (f7 * f8);
        float f10 = fArr[1];
        float f11 = fArr[5];
        float f12 = fArr[9];
        float f13 = fArr[13];
        float f14 = (f10 * f2) + (f11 * f4) + (f12 * f6) + (f13 * f8);
        float f15 = fArr[2];
        float f16 = fArr[6];
        float f17 = fArr[10];
        float f18 = fArr[14];
        float f19 = (f15 * f2) + (f16 * f4) + (f17 * f6) + (f18 * f8);
        float f20 = fArr[3];
        float f21 = fArr[7];
        float f22 = fArr[11];
        float f23 = fArr[15];
        float f24 = (f2 * f20) + (f4 * f21) + (f6 * f22) + (f8 * f23);
        float f25 = fArr2[4];
        float f26 = fArr2[5];
        float f27 = fArr2[6];
        float f28 = fArr2[7];
        float f29 = (f * f25) + (f3 * f26) + (f5 * f27) + (f7 * f28);
        float f30 = (f10 * f25) + (f11 * f26) + (f12 * f27) + (f13 * f28);
        float f31 = (f15 * f25) + (f16 * f26) + (f17 * f27) + (f18 * f28);
        float f32 = (f25 * f20) + (f26 * f21) + (f27 * f22) + (f28 * f23);
        float f33 = fArr2[8];
        float f34 = fArr2[9];
        float f35 = fArr2[10];
        float f36 = fArr2[11];
        float f37 = (f10 * f33) + (f11 * f34) + (f12 * f35) + (f13 * f36);
        float f38 = fArr2[12];
        float f39 = fArr2[13];
        float f40 = (f * f38) + (f3 * f39);
        float f41 = fArr2[14];
        float f42 = f40 + (f5 * f41);
        float f43 = fArr2[15];
        return new float[]{f9, f14, f19, f24, f29, f30, f31, f32, (f * f33) + (f3 * f34) + (f5 * f35) + (f7 * f36), f37, (f15 * f33) + (f16 * f34) + (f17 * f35) + (f18 * f36), (f33 * f20) + (f34 * f21) + (f35 * f22) + (f36 * f23), f42 + (f7 * f43), (f10 * f38) + (f11 * f39) + (f12 * f41) + (f13 * f43), (f15 * f38) + (f16 * f39) + (f17 * f41) + (f18 * f43), (f20 * f38) + (f21 * f39) + (f22 * f41) + (f23 * f43)};
    }
}
