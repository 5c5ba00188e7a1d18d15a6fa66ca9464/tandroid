package androidx.core.content.res;

import androidx.core.graphics.ColorUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class CamColor {
    private final float mAstar;
    private final float mBstar;
    private final float mChroma;
    private final float mHue;
    private final float mJ;
    private final float mJstar;
    private final float mM;
    private final float mQ;
    private final float mS;

    CamColor(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        this.mHue = f;
        this.mChroma = f2;
        this.mJ = f3;
        this.mQ = f4;
        this.mM = f5;
        this.mS = f6;
        this.mJstar = f7;
        this.mAstar = f8;
        this.mBstar = f9;
    }

    private static CamColor findCamByJ(float f, float f2, float f3) {
        float f4 = 100.0f;
        float f5 = 1000.0f;
        CamColor camColor = null;
        float f6 = 1000.0f;
        float f7 = 0.0f;
        while (Math.abs(f7 - f4) > 0.01f) {
            float f8 = ((f4 - f7) / 2.0f) + f7;
            int viewedInSrgb = fromJch(f8, f2, f).viewedInSrgb();
            float lStarFromInt = CamUtils.lStarFromInt(viewedInSrgb);
            float abs = Math.abs(f3 - lStarFromInt);
            if (abs < 0.2f) {
                CamColor fromColor = fromColor(viewedInSrgb);
                float distance = fromColor.distance(fromJch(fromColor.getJ(), fromColor.getChroma(), f));
                if (distance <= 1.0f) {
                    camColor = fromColor;
                    f5 = abs;
                    f6 = distance;
                }
            }
            if (f5 == 0.0f && f6 == 0.0f) {
                break;
            }
            if (lStarFromInt < f3) {
                f7 = f8;
            } else {
                f4 = f8;
            }
        }
        return camColor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CamColor fromColor(int i) {
        return fromColorInViewingConditions(i, ViewingConditions.DEFAULT);
    }

    static CamColor fromColorInViewingConditions(int i, ViewingConditions viewingConditions) {
        float[] xyzFromInt = CamUtils.xyzFromInt(i);
        float[][] fArr = CamUtils.XYZ_TO_CAM16RGB;
        float f = xyzFromInt[0];
        float[] fArr2 = fArr[0];
        float f2 = fArr2[0] * f;
        float f3 = xyzFromInt[1];
        float f4 = f2 + (fArr2[1] * f3);
        float f5 = xyzFromInt[2];
        float f6 = f4 + (fArr2[2] * f5);
        float[] fArr3 = fArr[1];
        float f7 = (fArr3[0] * f) + (fArr3[1] * f3) + (fArr3[2] * f5);
        float[] fArr4 = fArr[2];
        float f8 = (f * fArr4[0]) + (f3 * fArr4[1]) + (f5 * fArr4[2]);
        float f9 = viewingConditions.getRgbD()[0] * f6;
        float f10 = viewingConditions.getRgbD()[1] * f7;
        float f11 = viewingConditions.getRgbD()[2] * f8;
        double fl = viewingConditions.getFl() * Math.abs(f9);
        Double.isNaN(fl);
        float pow = (float) Math.pow(fl / 100.0d, 0.42d);
        double fl2 = viewingConditions.getFl() * Math.abs(f10);
        Double.isNaN(fl2);
        float pow2 = (float) Math.pow(fl2 / 100.0d, 0.42d);
        double fl3 = viewingConditions.getFl() * Math.abs(f11);
        Double.isNaN(fl3);
        float pow3 = (float) Math.pow(fl3 / 100.0d, 0.42d);
        float signum = ((Math.signum(f9) * 400.0f) * pow) / (pow + 27.13f);
        float signum2 = ((Math.signum(f10) * 400.0f) * pow2) / (pow2 + 27.13f);
        float signum3 = ((Math.signum(f11) * 400.0f) * pow3) / (pow3 + 27.13f);
        double d = signum;
        Double.isNaN(d);
        double d2 = signum2;
        Double.isNaN(d2);
        double d3 = signum3;
        Double.isNaN(d3);
        float f12 = ((float) (((d * 11.0d) + (d2 * (-12.0d))) + d3)) / 11.0f;
        double d4 = signum + signum2;
        Double.isNaN(d3);
        Double.isNaN(d4);
        float f13 = ((float) (d4 - (d3 * 2.0d))) / 9.0f;
        float f14 = signum2 * 20.0f;
        float f15 = (((signum * 20.0f) + f14) + (21.0f * signum3)) / 20.0f;
        float f16 = (((signum * 40.0f) + f14) + signum3) / 20.0f;
        float atan2 = (((float) Math.atan2(f13, f12)) * 180.0f) / 3.1415927f;
        if (atan2 < 0.0f) {
            atan2 += 360.0f;
        } else if (atan2 >= 360.0f) {
            atan2 -= 360.0f;
        }
        float f17 = atan2;
        float f18 = (3.1415927f * f17) / 180.0f;
        float pow4 = ((float) Math.pow((f16 * viewingConditions.getNbb()) / viewingConditions.getAw(), viewingConditions.getC() * viewingConditions.getZ())) * 100.0f;
        float flRoot = viewingConditions.getFlRoot() * (4.0f / viewingConditions.getC()) * ((float) Math.sqrt(pow4 / 100.0f)) * (viewingConditions.getAw() + 4.0f);
        Double.isNaN(((double) f17) < 20.14d ? 360.0f + f17 : f17);
        float pow5 = ((float) Math.pow(1.64d - Math.pow(0.29d, viewingConditions.getN()), 0.73d)) * ((float) Math.pow((((((((float) (Math.cos(((r9 * 3.141592653589793d) / 180.0d) + 2.0d) + 3.8d)) * 0.25f) * 3846.1538f) * viewingConditions.getNc()) * viewingConditions.getNcb()) * ((float) Math.sqrt((f12 * f12) + (f13 * f13)))) / (f15 + 0.305f), 0.9d));
        double d5 = pow4;
        Double.isNaN(d5);
        float sqrt = pow5 * ((float) Math.sqrt(d5 / 100.0d));
        float flRoot2 = sqrt * viewingConditions.getFlRoot();
        float sqrt2 = ((float) Math.sqrt((pow5 * viewingConditions.getC()) / (viewingConditions.getAw() + 4.0f))) * 50.0f;
        float f19 = (1.7f * pow4) / ((0.007f * pow4) + 1.0f);
        float log = ((float) Math.log((0.0228f * flRoot2) + 1.0f)) * 43.85965f;
        double d6 = f18;
        return new CamColor(f17, sqrt, pow4, flRoot, flRoot2, sqrt2, f19, log * ((float) Math.cos(d6)), log * ((float) Math.sin(d6)));
    }

    private static CamColor fromJch(float f, float f2, float f3) {
        return fromJchInFrame(f, f2, f3, ViewingConditions.DEFAULT);
    }

    private static CamColor fromJchInFrame(float f, float f2, float f3, ViewingConditions viewingConditions) {
        float c = 4.0f / viewingConditions.getC();
        double d = f;
        Double.isNaN(d);
        float sqrt = c * ((float) Math.sqrt(d / 100.0d)) * (viewingConditions.getAw() + 4.0f) * viewingConditions.getFlRoot();
        float flRoot = f2 * viewingConditions.getFlRoot();
        float sqrt2 = ((float) Math.sqrt(((f2 / ((float) Math.sqrt(r4))) * viewingConditions.getC()) / (viewingConditions.getAw() + 4.0f))) * 50.0f;
        float f4 = (1.7f * f) / ((0.007f * f) + 1.0f);
        double d2 = flRoot;
        Double.isNaN(d2);
        float log = ((float) Math.log((d2 * 0.0228d) + 1.0d)) * 43.85965f;
        double d3 = (3.1415927f * f3) / 180.0f;
        return new CamColor(f3, f2, f, sqrt, flRoot, sqrt2, f4, log * ((float) Math.cos(d3)), log * ((float) Math.sin(d3)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int toColor(float f, float f2, float f3) {
        return toColor(f, f2, f3, ViewingConditions.DEFAULT);
    }

    static int toColor(float f, float f2, float f3, ViewingConditions viewingConditions) {
        if (f2 < 1.0d || Math.round(f3) <= 0.0d || Math.round(f3) >= 100.0d) {
            return CamUtils.intFromLStar(f3);
        }
        float min = f < 0.0f ? 0.0f : Math.min(360.0f, f);
        float f4 = f2;
        CamColor camColor = null;
        float f5 = 0.0f;
        boolean z = true;
        while (Math.abs(f5 - f2) >= 0.4f) {
            CamColor findCamByJ = findCamByJ(min, f4, f3);
            if (!z) {
                if (findCamByJ == null) {
                    f2 = f4;
                } else {
                    f5 = f4;
                    camColor = findCamByJ;
                }
                f4 = ((f2 - f5) / 2.0f) + f5;
            } else {
                if (findCamByJ != null) {
                    return findCamByJ.viewed(viewingConditions);
                }
                f4 = ((f2 - f5) / 2.0f) + f5;
                z = false;
            }
        }
        return camColor == null ? CamUtils.intFromLStar(f3) : camColor.viewed(viewingConditions);
    }

    float distance(CamColor camColor) {
        float jStar = getJStar() - camColor.getJStar();
        float aStar = getAStar() - camColor.getAStar();
        float bStar = getBStar() - camColor.getBStar();
        return (float) (Math.pow(Math.sqrt((jStar * jStar) + (aStar * aStar) + (bStar * bStar)), 0.63d) * 1.41d);
    }

    float getAStar() {
        return this.mAstar;
    }

    float getBStar() {
        return this.mBstar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getChroma() {
        return this.mChroma;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getHue() {
        return this.mHue;
    }

    float getJ() {
        return this.mJ;
    }

    float getJStar() {
        return this.mJstar;
    }

    int viewed(ViewingConditions viewingConditions) {
        float f;
        if (getChroma() == 0.0d || getJ() == 0.0d) {
            f = 0.0f;
        } else {
            float chroma = getChroma();
            double j = getJ();
            Double.isNaN(j);
            f = chroma / ((float) Math.sqrt(j / 100.0d));
        }
        double d = f;
        double pow = Math.pow(1.64d - Math.pow(0.29d, viewingConditions.getN()), 0.73d);
        Double.isNaN(d);
        float pow2 = (float) Math.pow(d / pow, 1.1111111111111112d);
        double hue = (getHue() * 3.1415927f) / 180.0f;
        Double.isNaN(hue);
        float cos = ((float) (Math.cos(2.0d + hue) + 3.8d)) * 0.25f;
        float aw = viewingConditions.getAw();
        double j2 = getJ();
        Double.isNaN(j2);
        double c = viewingConditions.getC();
        Double.isNaN(c);
        double d2 = 1.0d / c;
        double z = viewingConditions.getZ();
        Double.isNaN(z);
        float pow3 = aw * ((float) Math.pow(j2 / 100.0d, d2 / z));
        float nc = cos * 3846.1538f * viewingConditions.getNc() * viewingConditions.getNcb();
        float nbb = pow3 / viewingConditions.getNbb();
        float sin = (float) Math.sin(hue);
        float cos2 = (float) Math.cos(hue);
        float f2 = (((0.305f + nbb) * 23.0f) * pow2) / (((nc * 23.0f) + ((11.0f * pow2) * cos2)) + ((pow2 * 108.0f) * sin));
        float f3 = cos2 * f2;
        float f4 = f2 * sin;
        float f5 = nbb * 460.0f;
        float f6 = (((451.0f * f3) + f5) + (288.0f * f4)) / 1403.0f;
        float f7 = ((f5 - (891.0f * f3)) - (261.0f * f4)) / 1403.0f;
        float f8 = ((f5 - (f3 * 220.0f)) - (f4 * 6300.0f)) / 1403.0f;
        Double.isNaN(Math.abs(f6));
        Double.isNaN(Math.abs(f6));
        float signum = Math.signum(f6) * (100.0f / viewingConditions.getFl()) * ((float) Math.pow((float) Math.max(0.0d, (r6 * 27.13d) / (400.0d - r11)), 2.380952380952381d));
        Double.isNaN(Math.abs(f7));
        Double.isNaN(Math.abs(f7));
        float signum2 = Math.signum(f7) * (100.0f / viewingConditions.getFl()) * ((float) Math.pow((float) Math.max(0.0d, (r11 * 27.13d) / (400.0d - r9)), 2.380952380952381d));
        Double.isNaN(Math.abs(f8));
        Double.isNaN(Math.abs(f8));
        float signum3 = Math.signum(f8) * (100.0f / viewingConditions.getFl()) * ((float) Math.pow((float) Math.max(0.0d, (r9 * 27.13d) / (400.0d - r11)), 2.380952380952381d));
        float f9 = signum / viewingConditions.getRgbD()[0];
        float f10 = signum2 / viewingConditions.getRgbD()[1];
        float f11 = signum3 / viewingConditions.getRgbD()[2];
        float[][] fArr = CamUtils.CAM16RGB_TO_XYZ;
        float[] fArr2 = fArr[0];
        float f12 = (fArr2[0] * f9) + (fArr2[1] * f10) + (fArr2[2] * f11);
        float[] fArr3 = fArr[1];
        float f13 = (fArr3[0] * f9) + (fArr3[1] * f10) + (fArr3[2] * f11);
        float[] fArr4 = fArr[2];
        return ColorUtils.XYZToColor(f12, f13, (f9 * fArr4[0]) + (f10 * fArr4[1]) + (f11 * fArr4[2]));
    }

    int viewedInSrgb() {
        return viewed(ViewingConditions.DEFAULT);
    }
}
