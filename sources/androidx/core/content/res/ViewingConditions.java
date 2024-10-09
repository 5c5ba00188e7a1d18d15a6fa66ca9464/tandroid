package androidx.core.content.res;

/* loaded from: classes.dex */
final class ViewingConditions {
    static final ViewingConditions DEFAULT;
    private final float mAw;
    private final float mC;
    private final float mFl;
    private final float mFlRoot;
    private final float mN;
    private final float mNbb;
    private final float mNc;
    private final float mNcb;
    private final float[] mRgbD;
    private final float mZ;

    static {
        float[] fArr = CamUtils.WHITE_POINT_D65;
        double yFromLStar = CamUtils.yFromLStar(50.0f);
        Double.isNaN(yFromLStar);
        DEFAULT = make(fArr, (float) ((yFromLStar * 63.66197723675813d) / 100.0d), 50.0f, 2.0f, false);
    }

    private ViewingConditions(float f, float f2, float f3, float f4, float f5, float f6, float[] fArr, float f7, float f8, float f9) {
        this.mN = f;
        this.mAw = f2;
        this.mNbb = f3;
        this.mNcb = f4;
        this.mC = f5;
        this.mNc = f6;
        this.mRgbD = fArr;
        this.mFl = f7;
        this.mFlRoot = f8;
        this.mZ = f9;
    }

    static ViewingConditions make(float[] fArr, float f, float f2, float f3, boolean z) {
        float[][] fArr2 = CamUtils.XYZ_TO_CAM16RGB;
        float f4 = fArr[0];
        float[] fArr3 = fArr2[0];
        float f5 = fArr3[0] * f4;
        float f6 = fArr[1];
        float f7 = f5 + (fArr3[1] * f6);
        float f8 = fArr[2];
        float f9 = f7 + (fArr3[2] * f8);
        float[] fArr4 = fArr2[1];
        float f10 = (fArr4[0] * f4) + (fArr4[1] * f6) + (fArr4[2] * f8);
        float[] fArr5 = fArr2[2];
        float f11 = (f4 * fArr5[0]) + (f6 * fArr5[1]) + (f8 * fArr5[2]);
        float f12 = (f3 / 10.0f) + 0.8f;
        float lerp = ((double) f12) >= 0.9d ? CamUtils.lerp(0.59f, 0.69f, (f12 - 0.9f) * 10.0f) : CamUtils.lerp(0.525f, 0.59f, (f12 - 0.8f) * 10.0f);
        float exp = z ? 1.0f : (1.0f - (((float) Math.exp(((-f) - 42.0f) / 92.0f)) * 0.2777778f)) * f12;
        double d = exp;
        if (d > 1.0d) {
            exp = 1.0f;
        } else if (d < 0.0d) {
            exp = 0.0f;
        }
        float[] fArr6 = {(((100.0f / f9) * exp) + 1.0f) - exp, (((100.0f / f10) * exp) + 1.0f) - exp, (((100.0f / f11) * exp) + 1.0f) - exp};
        float f13 = 1.0f / ((5.0f * f) + 1.0f);
        float f14 = f13 * f13 * f13 * f13;
        float f15 = 1.0f - f14;
        double d2 = f;
        Double.isNaN(d2);
        float cbrt = (f14 * f) + (0.1f * f15 * f15 * ((float) Math.cbrt(d2 * 5.0d)));
        float yFromLStar = CamUtils.yFromLStar(f2) / fArr[1];
        double d3 = yFromLStar;
        float sqrt = ((float) Math.sqrt(d3)) + 1.48f;
        float pow = 0.725f / ((float) Math.pow(d3, 0.2d));
        double d4 = fArr6[0] * cbrt * f9;
        Double.isNaN(d4);
        float pow2 = (float) Math.pow(d4 / 100.0d, 0.42d);
        double d5 = fArr6[1] * cbrt * f10;
        Double.isNaN(d5);
        float pow3 = (float) Math.pow(d5 / 100.0d, 0.42d);
        double d6 = fArr6[2] * cbrt * f11;
        Double.isNaN(d6);
        float[] fArr7 = {pow2, pow3, (float) Math.pow(d6 / 100.0d, 0.42d)};
        float f16 = fArr7[0];
        float f17 = (f16 * 400.0f) / (f16 + 27.13f);
        float f18 = fArr7[1];
        float f19 = (f18 * 400.0f) / (f18 + 27.13f);
        float f20 = fArr7[2];
        float[] fArr8 = {f17, f19, (400.0f * f20) / (f20 + 27.13f)};
        return new ViewingConditions(yFromLStar, ((fArr8[0] * 2.0f) + fArr8[1] + (fArr8[2] * 0.05f)) * pow, pow, pow, lerp, f12, fArr6, cbrt, (float) Math.pow(cbrt, 0.25d), sqrt);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getAw() {
        return this.mAw;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getC() {
        return this.mC;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getFl() {
        return this.mFl;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getFlRoot() {
        return this.mFlRoot;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getN() {
        return this.mN;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getNbb() {
        return this.mNbb;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getNc() {
        return this.mNc;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getNcb() {
        return this.mNcb;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float[] getRgbD() {
        return this.mRgbD;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getZ() {
        return this.mZ;
    }
}
