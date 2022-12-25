package org.telegram.ui.Charts.view_data;

import org.telegram.messenger.AndroidUtilities;
/* loaded from: classes3.dex */
public class ChartHorizontalLinesData {
    public int alpha;
    public int fixedAlpha;
    public int[] values;
    public String[] valuesStr;
    public String[] valuesStr2;

    public ChartHorizontalLinesData(int i, int i2, boolean z) {
        this(i, i2, z, 0.0f);
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x008e  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0099  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x009c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ChartHorizontalLinesData(int i, int i2, boolean z, float f) {
        float f2;
        this.fixedAlpha = 255;
        int i3 = 6;
        if (!z) {
            i = i > 100 ? round(i) : i;
            int max = Math.max(1, (int) Math.ceil(i / 5.0f));
            if (i < 6) {
                i3 = Math.max(2, i + 1);
            } else {
                int i4 = i / 2;
                if (i4 < 6) {
                    i3 = i4 + 1;
                    if (i % 2 != 0) {
                        i3++;
                    }
                }
            }
            this.values = new int[i3];
            this.valuesStr = new String[i3];
            while (r4 < i3) {
                int[] iArr = this.values;
                iArr[r4] = r4 * max;
                this.valuesStr[r4] = AndroidUtilities.formatWholeNumber(iArr[r4], 0);
                r4++;
            }
            return;
        }
        int i5 = i - i2;
        if (i5 == 0) {
            i2--;
            i3 = 3;
        } else if (i5 < 6) {
            i3 = Math.max(2, i5 + 1);
        } else {
            int i6 = i5 / 2;
            if (i6 < 6) {
                i3 = i6 + (i5 % 2) + 1;
                f2 = 2.0f;
            } else {
                f2 = i5 / 5.0f;
                if (f2 <= 0.0f) {
                    i3 = Math.max(2, i5 + 1);
                }
            }
            this.values = new int[i3];
            this.valuesStr = new String[i3];
            if (f > 0.0f) {
                this.valuesStr2 = new String[i3];
            }
            r4 = f2 / f >= 1.0f ? 0 : 1;
            for (int i7 = 0; i7 < i3; i7++) {
                int[] iArr2 = this.values;
                iArr2[i7] = ((int) (i7 * f2)) + i2;
                this.valuesStr[i7] = AndroidUtilities.formatWholeNumber(iArr2[i7], i5);
                if (f > 0.0f) {
                    float f3 = this.values[i7] / f;
                    if (r4 != 0) {
                        int i8 = (int) f3;
                        if (f3 - i8 < 0.01f) {
                            this.valuesStr2[i7] = AndroidUtilities.formatWholeNumber(i8, (int) (i5 / f));
                        } else {
                            this.valuesStr2[i7] = "";
                        }
                    } else {
                        this.valuesStr2[i7] = AndroidUtilities.formatWholeNumber((int) f3, (int) (i5 / f));
                    }
                }
            }
        }
        f2 = 1.0f;
        this.values = new int[i3];
        this.valuesStr = new String[i3];
        if (f > 0.0f) {
        }
        if (f2 / f >= 1.0f) {
        }
        while (i7 < i3) {
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
