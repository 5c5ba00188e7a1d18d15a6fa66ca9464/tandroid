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
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ChannelMonetizationLayout;
import org.telegram.ui.Stars.StarsIntroActivity;
/* loaded from: classes4.dex */
public class ChartHorizontalLinesData {
    public int alpha;
    public int fixedAlpha = NotificationCenter.closeSearchByActiveAction;
    private DecimalFormat formatterTON;
    private StaticLayout[] layouts;
    private StaticLayout[] layouts2;
    public long[] values;
    public CharSequence[] valuesStr;
    public CharSequence[] valuesStr2;

    /* JADX WARN: Removed duplicated region for block: B:20:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0134  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0144  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x014a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ChartHorizontalLinesData(long j, long j2, boolean z, float f, int i, TextPaint textPaint, TextPaint textPaint2) {
        long j3;
        float f2;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        if (!z) {
            long round = j > 100 ? round(j) : j;
            double d = round;
            Double.isNaN(d);
            long max = Math.max(1L, (long) Math.ceil(d / 5.0d));
            if (round < 6) {
                i5 = (int) Math.max(2L, round + 1);
            } else {
                long j4 = round / 2;
                if (j4 >= 6) {
                    i4 = 6;
                    this.values = new long[i4];
                    this.valuesStr = new CharSequence[i4];
                    this.layouts = new StaticLayout[i4];
                    if (f > 0.0f) {
                        this.valuesStr2 = new CharSequence[i4];
                        this.layouts2 = new StaticLayout[i4];
                    }
                    boolean z2 = ((float) max) / f >= 1.0f;
                    i6 = 1;
                    while (i6 < i4) {
                        long j5 = i6 * max;
                        this.values[i6] = j5;
                        long j6 = max;
                        this.valuesStr[i6] = format(0, textPaint, j5, i);
                        if (f > 0.0f) {
                            float f3 = ((float) this.values[i6]) / f;
                            if (z2) {
                                long j7 = f3;
                                if (f3 - ((float) j7) < 0.01f || i == 1 || i == 2) {
                                    this.valuesStr2[i6] = format(1, textPaint2, j7, i);
                                } else {
                                    this.valuesStr2[i6] = "";
                                }
                            } else {
                                this.valuesStr2[i6] = format(1, textPaint2, f3, i);
                            }
                        }
                        i6++;
                        max = j6;
                    }
                    return;
                }
                i5 = (int) (j4 + 1);
                if (round % 2 != 0) {
                    i5++;
                }
            }
            i4 = i5;
            this.values = new long[i4];
            this.valuesStr = new CharSequence[i4];
            this.layouts = new StaticLayout[i4];
            if (f > 0.0f) {
            }
            if (((float) max) / f >= 1.0f) {
            }
            i6 = 1;
            while (i6 < i4) {
            }
            return;
        }
        long j8 = j - j2;
        if (j8 == 0) {
            j3 = j2 - 1;
            i2 = 3;
        } else {
            if (j8 >= 6) {
                long j9 = j8 / 2;
                if (j9 < 6) {
                    j3 = j2;
                    i2 = (int) (j9 + (j8 % 2) + 1);
                    f2 = 2.0f;
                } else {
                    float f4 = ((float) j8) / 5.0f;
                    if (f4 > 0.0f) {
                        j3 = j2;
                        f2 = f4;
                        i2 = 6;
                    }
                }
                this.values = new long[i2];
                this.valuesStr = new CharSequence[i2];
                this.layouts = new StaticLayout[i2];
                if (f > 0.0f) {
                    this.valuesStr2 = new CharSequence[i2];
                    this.layouts2 = new StaticLayout[i2];
                }
                boolean z3 = f2 / f >= 1.0f;
                i3 = 0;
                while (i3 < i2) {
                    long j10 = j3 + (i3 * f2);
                    this.values[i3] = j10;
                    int i7 = i3;
                    this.valuesStr[i7] = format(0, textPaint, j10, i);
                    if (f > 0.0f) {
                        float f5 = ((float) this.values[i7]) / f;
                        if (z3) {
                            long j11 = f5;
                            if (f5 - ((float) j11) >= 0.01f && i != 1) {
                                if (i != 2) {
                                    this.valuesStr2[i7] = "";
                                }
                            }
                            this.valuesStr2[i7] = format(1, textPaint2, j11, i);
                        } else {
                            this.valuesStr2[i7] = format(1, textPaint2, f5, i);
                        }
                    }
                    i3 = i7 + 1;
                }
            }
            j3 = j2;
            i2 = (int) Math.max(2L, j8 + 1);
        }
        f2 = 1.0f;
        this.values = new long[i2];
        this.valuesStr = new CharSequence[i2];
        this.layouts = new StaticLayout[i2];
        if (f > 0.0f) {
        }
        if (f2 / f >= 1.0f) {
        }
        i3 = 0;
        while (i3 < i2) {
        }
    }

    public static long lookupHeight(long j) {
        if (j > 100) {
            j = round(j);
        }
        return ((long) Math.ceil(((float) j) / 5.0f)) * 5;
    }

    private static long round(long j) {
        return ((float) (j / 5)) % 10.0f == 0.0f ? j : ((j / 10) + 1) * 10;
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

    public CharSequence format(int i, TextPaint textPaint, long j, int i2) {
        if (i2 != 1) {
            if (i2 == 2) {
                if (i == 1) {
                    return "≈" + BillingController.getInstance().formatCurrency(j, "USD");
                }
                return StarsIntroActivity.replaceStarsWithPlain("XTR " + LocaleController.formatNumber(j, ' '), 0.65f);
            }
            return AndroidUtilities.formatWholeNumber((int) j, 0);
        } else if (i == 1) {
            return "≈" + BillingController.getInstance().formatCurrency(j, "USD");
        } else {
            if (this.formatterTON == null) {
                DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.US);
                decimalFormatSymbols.setDecimalSeparator('.');
                DecimalFormat decimalFormat = new DecimalFormat("#.##", decimalFormatSymbols);
                this.formatterTON = decimalFormat;
                decimalFormat.setMinimumFractionDigits(2);
                this.formatterTON.setMaximumFractionDigits(6);
                this.formatterTON.setGroupingUsed(false);
            }
            this.formatterTON.setMaximumFractionDigits(j <= 1000000000 ? 6 : 2);
            StringBuilder sb = new StringBuilder();
            sb.append("TON ");
            DecimalFormat decimalFormat2 = this.formatterTON;
            double d = j;
            Double.isNaN(d);
            sb.append(decimalFormat2.format(d / 1.0E9d));
            return ChannelMonetizationLayout.replaceTON(sb.toString(), textPaint, 0.8f, -AndroidUtilities.dp(0.66f), false);
        }
    }
}
