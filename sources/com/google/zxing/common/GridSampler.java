package com.google.zxing.common;

import com.google.zxing.NotFoundException;

/* loaded from: classes.dex */
public abstract class GridSampler {
    private static GridSampler gridSampler = new DefaultGridSampler();

    /* JADX WARN: Removed duplicated region for block: B:14:0x0035  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0039  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0076  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected static void checkAndNudgePoints(BitMatrix bitMatrix, float[] fArr) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        int length = fArr.length - 1;
        boolean z = true;
        for (int i = 0; i < length && z; i += 2) {
            int i2 = (int) fArr[i];
            int i3 = i + 1;
            int i4 = (int) fArr[i3];
            if (i2 < -1 || i2 > width || i4 < -1 || i4 > height) {
                throw NotFoundException.getNotFoundInstance();
            }
            if (i2 == -1) {
                fArr[i] = 0.0f;
            } else if (i2 == width) {
                fArr[i] = width - 1;
            } else {
                z = false;
                if (i4 != -1) {
                    fArr[i3] = 0.0f;
                } else if (i4 == height) {
                    fArr[i3] = height - 1;
                }
                z = true;
            }
            z = true;
            if (i4 != -1) {
            }
            z = true;
        }
        boolean z2 = true;
        for (int length2 = fArr.length - 2; length2 >= 0 && z2; length2 -= 2) {
            int i5 = (int) fArr[length2];
            int i6 = length2 + 1;
            int i7 = (int) fArr[i6];
            if (i5 < -1 || i5 > width || i7 < -1 || i7 > height) {
                throw NotFoundException.getNotFoundInstance();
            }
            if (i5 == -1) {
                fArr[length2] = 0.0f;
            } else if (i5 == width) {
                fArr[length2] = width - 1;
            } else {
                z2 = false;
                if (i7 != -1) {
                    fArr[i6] = 0.0f;
                } else if (i7 == height) {
                    fArr[i6] = height - 1;
                }
                z2 = true;
            }
            z2 = true;
            if (i7 != -1) {
            }
            z2 = true;
        }
    }

    public static GridSampler getInstance() {
        return gridSampler;
    }

    public abstract BitMatrix sampleGrid(BitMatrix bitMatrix, int i, int i2, PerspectiveTransform perspectiveTransform);
}
