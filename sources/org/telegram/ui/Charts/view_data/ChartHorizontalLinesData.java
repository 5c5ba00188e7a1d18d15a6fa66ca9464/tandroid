package org.telegram.ui.Charts.view_data;

import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BillingController;
import org.telegram.ui.ChannelMonetizationLayout;
/* loaded from: classes4.dex */
public class ChartHorizontalLinesData {
    public int alpha;
    public int fixedAlpha = 255;
    private DecimalFormat formatterTON;
    private StaticLayout[] layouts;
    private StaticLayout[] layouts2;
    public int[] values;
    public CharSequence[] valuesStr;
    public CharSequence[] valuesStr2;

    /* JADX WARN: Removed duplicated region for block: B:19:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x006c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ChartHorizontalLinesData(int i, int i2, boolean z, float f, int i3, TextPaint textPaint, TextPaint textPaint2) {
        float f2;
        int i4;
        int i5;
        int max;
        int i6;
        int i7;
        int i8;
        int[] iArr;
        int i9 = i;
        if (!z) {
            i9 = i9 > 100 ? round(i) : i9;
            double d = i9;
            Double.isNaN(d);
            int max2 = Math.max(1, (int) Math.ceil(d / 5.0d));
            if (i9 < 6) {
                i7 = Math.max(2, i9 + 1);
            } else {
                int i10 = i9 / 2;
                if (i10 < 6) {
                    i7 = i10 + 1;
                    if (i9 % 2 != 0) {
                        i7++;
                    }
                } else {
                    i6 = 6;
                    this.values = new int[i6];
                    this.valuesStr = new CharSequence[i6];
                    this.layouts = new StaticLayout[i6];
                    if (f > 0.0f) {
                        this.valuesStr2 = new CharSequence[i6];
                        this.layouts2 = new StaticLayout[i6];
                    }
                    boolean z2 = ((float) max2) / f >= 1.0f;
                    for (i8 = 1; i8 < i6; i8++) {
                        this.values[i8] = i8 * max2;
                        this.valuesStr[i8] = format(0, textPaint, iArr[i8], i3);
                        if (f > 0.0f) {
                            float f3 = this.values[i8] / f;
                            if (!z2) {
                                this.valuesStr2[i8] = format(1, textPaint2, f3, i3);
                            } else if (f3 - ((int) f3) < 0.01f) {
                                this.valuesStr2[i8] = format(1, textPaint2, f3, i3);
                            } else {
                                this.valuesStr2[i8] = "";
                            }
                        }
                    }
                    return;
                }
            }
            i6 = i7;
            this.values = new int[i6];
            this.valuesStr = new CharSequence[i6];
            this.layouts = new StaticLayout[i6];
            if (f > 0.0f) {
            }
            if (((float) max2) / f >= 1.0f) {
            }
            while (i8 < i6) {
            }
            return;
        }
        int i11 = i9 - i2;
        if (i11 == 0) {
            i5 = i2 - 1;
            f2 = 1.0f;
            i4 = 3;
        } else {
            if (i11 < 6) {
                max = Math.max(2, i11 + 1);
            } else {
                int i12 = i11 / 2;
                if (i12 < 6) {
                    int i13 = i12 + (i11 % 2) + 1;
                    i5 = i2;
                    i4 = i13;
                    f2 = 2.0f;
                } else {
                    float f4 = i11 / 5.0f;
                    if (f4 <= 0.0f) {
                        max = Math.max(2, i11 + 1);
                    } else {
                        f2 = f4;
                        i4 = 6;
                        i5 = i2;
                    }
                }
            }
            i5 = i2;
            i4 = max;
            f2 = 1.0f;
        }
        this.values = new int[i4];
        this.valuesStr = new CharSequence[i4];
        this.layouts = new StaticLayout[i4];
        if (f > 0.0f) {
            this.valuesStr2 = new CharSequence[i4];
            this.layouts2 = new StaticLayout[i4];
        }
        boolean z3 = f2 / f < 1.0f;
        for (int i14 = 0; i14 < i4; i14++) {
            float f5 = i14 * f2;
            this.values[i14] = ((int) f5) + i5;
            this.valuesStr[i14] = format(0, textPaint, i5 + f5, i3);
            if (f > 0.0f) {
                float f6 = this.values[i14] / f;
                if (!z3) {
                    this.valuesStr2[i14] = format(1, textPaint2, f6, i3);
                } else if (f6 - ((int) f6) < 0.01f) {
                    this.valuesStr2[i14] = format(1, textPaint2, f6, i3);
                } else {
                    this.valuesStr2[i14] = "";
                }
            }
        }
    }

    public CharSequence format(int i, TextPaint textPaint, long j, int i2) {
        if (i2 == 1) {
            if (i == 1) {
                return BillingController.getInstance().formatCurrency(j, "USD");
            }
            if (this.formatterTON == null) {
                DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.US);
                decimalFormatSymbols.setDecimalSeparator('.');
                DecimalFormat decimalFormat = new DecimalFormat("#.##", decimalFormatSymbols);
                this.formatterTON = decimalFormat;
                decimalFormat.setMinimumFractionDigits(2);
                this.formatterTON.setMaximumFractionDigits(6);
                this.formatterTON.setGroupingUsed(false);
            }
            this.formatterTON.setMaximumFractionDigits(j > 1000000000 ? 2 : 6);
            StringBuilder sb = new StringBuilder();
            sb.append("TON ");
            DecimalFormat decimalFormat2 = this.formatterTON;
            double d = j;
            Double.isNaN(d);
            sb.append(decimalFormat2.format(d / 1.0E9d));
            return ChannelMonetizationLayout.replaceTON(sb.toString(), textPaint, false);
        }
        return AndroidUtilities.formatWholeNumber((int) j, 0);
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

    public void drawText(Canvas canvas, int i, int i2, float f, float f2, TextPaint textPaint) {
        StaticLayout staticLayout = (i == 0 ? this.layouts : this.layouts2)[i2];
        if (staticLayout == null) {
            CharSequence charSequence = (i == 0 ? this.valuesStr : this.valuesStr2)[i2];
            StaticLayout[] staticLayoutArr = i == 0 ? this.layouts : this.layouts2;
            staticLayout = new StaticLayout(charSequence, textPaint, AndroidUtilities.displaySize.x, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            staticLayoutArr[i2] = staticLayout;
        }
        canvas.save();
        canvas.translate(f, f2 + textPaint.ascent());
        staticLayout.draw(canvas);
        canvas.restore();
    }
}
