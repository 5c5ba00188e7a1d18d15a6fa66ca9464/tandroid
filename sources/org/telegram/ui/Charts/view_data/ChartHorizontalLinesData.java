package org.telegram.ui.Charts.view_data;

import org.telegram.messenger.AndroidUtilities;
/* loaded from: classes4.dex */
public class ChartHorizontalLinesData {
    public int alpha;
    public int fixedAlpha;
    public int[] values;
    public String[] valuesStr;
    public String[] valuesStr2;

    public ChartHorizontalLinesData(int i, int i2, boolean z) {
        this(i, i2, z, 0.0f);
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x008c  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x009b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ChartHorizontalLinesData(int i, int i2, boolean z, float f) {
        float f2;
        int i3;
        this.fixedAlpha = 255;
        int i4 = 6;
        if (!z) {
            i = i > 100 ? round(i) : i;
            int max = Math.max(1, (int) Math.ceil(i / 5.0f));
            if (i < 6) {
                i4 = Math.max(2, i + 1);
            } else {
                int i5 = i / 2;
                if (i5 < 6) {
                    i4 = i5 + 1;
                    if (i % 2 != 0) {
                        i4++;
                    }
                }
            }
            this.values = new int[i4];
            this.valuesStr = new String[i4];
            while (r4 < i4) {
                int[] iArr = this.values;
                iArr[r4] = r4 * max;
                this.valuesStr[r4] = AndroidUtilities.formatWholeNumber(iArr[r4], 0);
                r4++;
            }
            return;
        }
        int i6 = i - i2;
        if (i6 == 0) {
            i2--;
            i4 = 3;
        } else if (i6 < 6) {
            i4 = Math.max(2, i6 + 1);
        } else {
            int i7 = i6 / 2;
            if (i7 < 6) {
                i4 = i7 + (i6 % 2) + 1;
                f2 = 2.0f;
            } else {
                float f3 = i6 / 5.0f;
                if (f3 <= 0.0f) {
                    i4 = Math.max(2, i6 + 1);
                } else {
                    f2 = f3;
                }
            }
            this.values = new int[i4];
            this.valuesStr = new String[i4];
            if (f > 0.0f) {
                this.valuesStr2 = new String[i4];
            }
            r4 = f2 / f >= 1.0f ? 0 : 1;
            for (i3 = 0; i3 < i4; i3++) {
                int[] iArr2 = this.values;
                iArr2[i3] = ((int) (i3 * f2)) + i2;
                this.valuesStr[i3] = AndroidUtilities.formatWholeNumber(iArr2[i3], 0);
                if (f > 0.0f) {
                    float f4 = this.values[i3] / f;
                    if (r4 != 0) {
                        int i8 = (int) f4;
                        if (f4 - i8 < 0.01f) {
                            this.valuesStr2[i3] = AndroidUtilities.formatWholeNumber(i8, 0);
                        } else {
                            this.valuesStr2[i3] = "";
                        }
                    } else {
                        this.valuesStr2[i3] = AndroidUtilities.formatWholeNumber((int) f4, 0);
                    }
                }
            }
        }
        f2 = 1.0f;
        this.values = new int[i4];
        this.valuesStr = new String[i4];
        if (f > 0.0f) {
        }
        if (f2 / f >= 1.0f) {
        }
        while (i3 < i4) {
        }
    }

    public static int lookupHeight(int i) {
        if (i > 100) {
            i = round(i);
        }
        return ((int) Math.ceil(i / 5.0f)) * 5;
    }

    private static int round(int i) {
        return ((float) (i / 5)) % 10.0f == 0.0f ? i : ((i / 10) + 1) * 10;
    }
}
