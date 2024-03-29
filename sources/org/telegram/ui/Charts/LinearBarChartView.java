package org.telegram.ui.Charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import org.telegram.ui.Charts.data.ChartData;
import org.telegram.ui.Charts.view_data.LineViewData;
import org.telegram.ui.Charts.view_data.TransitionParams;
/* loaded from: classes4.dex */
public class LinearBarChartView extends BaseChartView<ChartData, LineViewData> {
    public LinearBarChartView(Context context) {
        super(context);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Charts.BaseChartView
    public void init() {
        this.useMinHeight = true;
        super.init();
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x01d8  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01e0  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x01eb  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x01f4  */
    @Override // org.telegram.ui.Charts.BaseChartView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void drawChart(Canvas canvas) {
        float f;
        int i;
        float f2;
        int i2;
        float f3;
        if (this.chartData != 0) {
            float f4 = this.chartWidth;
            ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
            float f5 = chartPickerDelegate.pickerEnd;
            float f6 = chartPickerDelegate.pickerStart;
            float f7 = f4 / (f5 - f6);
            float f8 = (f6 * f7) - BaseChartView.HORIZONTAL_PADDING;
            int i3 = 0;
            int i4 = 0;
            while (i4 < this.lines.size()) {
                LineViewData lineViewData = (LineViewData) this.lines.get(i4);
                if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                    float[] fArr = this.chartData.xPercentage;
                    float f9 = fArr.length < 2 ? 0.0f : fArr[1] * f7;
                    long[] jArr = lineViewData.line.y;
                    int i5 = ((int) (BaseChartView.HORIZONTAL_PADDING / f9)) + 1;
                    lineViewData.chartPath.reset();
                    int max = Math.max(i3, this.startXIndex - i5);
                    int min = Math.min(this.chartData.xPercentage.length - 1, this.endXIndex + i5);
                    boolean z = true;
                    int i6 = 0;
                    while (max <= min) {
                        if (jArr[max] < 0) {
                            f3 = f7;
                            i2 = i4;
                        } else {
                            float f10 = (this.chartData.xPercentage[max] * f7) - f8;
                            i2 = i4;
                            float f11 = this.currentMinHeight;
                            float f12 = (((float) jArr[max]) - f11) / (this.currentMaxHeight - f11);
                            float strokeWidth = lineViewData.paint.getStrokeWidth() / 2.0f;
                            f3 = f7;
                            float measuredHeight = ((getMeasuredHeight() - this.chartBottom) - strokeWidth) - (f12 * (((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) - strokeWidth));
                            if (!BaseChartView.USE_LINES) {
                                if (z) {
                                    lineViewData.chartPath.moveTo(f10 - (f9 / 2.0f), measuredHeight);
                                    z = false;
                                } else {
                                    lineViewData.chartPath.lineTo(f10 - (f9 / 2.0f), measuredHeight);
                                }
                                lineViewData.chartPath.lineTo(f10 + (f9 / 2.0f), measuredHeight);
                            } else if (i6 == 0) {
                                float[] fArr2 = lineViewData.linesPath;
                                int i7 = i6 + 1;
                                float f13 = f9 / 2.0f;
                                fArr2[i6] = f10 - f13;
                                int i8 = i7 + 1;
                                fArr2[i7] = measuredHeight;
                                int i9 = i8 + 1;
                                float f14 = f10 + f13;
                                fArr2[i8] = f14;
                                int i10 = i9 + 1;
                                fArr2[i9] = measuredHeight;
                                int i11 = i10 + 1;
                                fArr2[i10] = f14;
                                i6 = i11 + 1;
                                fArr2[i11] = measuredHeight;
                            } else if (max == min) {
                                float[] fArr3 = lineViewData.linesPath;
                                int i12 = i6 + 1;
                                float f15 = f9 / 2.0f;
                                float f16 = f10 - f15;
                                fArr3[i6] = f16;
                                int i13 = i12 + 1;
                                fArr3[i12] = measuredHeight;
                                int i14 = i13 + 1;
                                fArr3[i13] = f16;
                                int i15 = i14 + 1;
                                fArr3[i14] = measuredHeight;
                                int i16 = i15 + 1;
                                float f17 = f10 + f15;
                                fArr3[i15] = f17;
                                int i17 = i16 + 1;
                                fArr3[i16] = measuredHeight;
                                int i18 = i17 + 1;
                                fArr3[i17] = f17;
                                int i19 = i18 + 1;
                                fArr3[i18] = measuredHeight;
                                int i20 = i19 + 1;
                                fArr3[i19] = f17;
                                i6 = i20 + 1;
                                fArr3[i20] = (getMeasuredHeight() - this.chartBottom) - strokeWidth;
                            } else {
                                float[] fArr4 = lineViewData.linesPath;
                                int i21 = i6 + 1;
                                float f18 = f9 / 2.0f;
                                float f19 = f10 - f18;
                                fArr4[i6] = f19;
                                int i22 = i21 + 1;
                                fArr4[i21] = measuredHeight;
                                int i23 = i22 + 1;
                                fArr4[i22] = f19;
                                int i24 = i23 + 1;
                                fArr4[i23] = measuredHeight;
                                int i25 = i24 + 1;
                                float f20 = f10 + f18;
                                fArr4[i24] = f20;
                                int i26 = i25 + 1;
                                fArr4[i25] = measuredHeight;
                                int i27 = i26 + 1;
                                fArr4[i26] = f20;
                                i6 = i27 + 1;
                                fArr4[i27] = measuredHeight;
                            }
                        }
                        max++;
                        i4 = i2;
                        f7 = f3;
                    }
                    f = f7;
                    i = i4;
                    canvas.save();
                    int i28 = this.transitionMode;
                    if (i28 == 2) {
                        TransitionParams transitionParams = this.transitionParams;
                        float f21 = transitionParams.progress;
                        f2 = f21 > 0.5f ? 0.0f : 1.0f - (f21 * 2.0f);
                        canvas.scale((f21 * 2.0f) + 1.0f, 1.0f, transitionParams.pX, transitionParams.pY);
                    } else if (i28 == 1) {
                        float f22 = this.transitionParams.progress;
                        f2 = f22 < 0.3f ? 0.0f : f22;
                        canvas.save();
                        TransitionParams transitionParams2 = this.transitionParams;
                        float f23 = transitionParams2.progress;
                        canvas.scale(f23, transitionParams2.needScaleY ? f23 : 1.0f, transitionParams2.pX, transitionParams2.pY);
                    } else {
                        if (i28 == 3) {
                            r4 = this.transitionParams.progress;
                        }
                        lineViewData.paint.setAlpha((int) (lineViewData.alpha * 255.0f * r4));
                        if (this.endXIndex - this.startXIndex <= 100) {
                            lineViewData.paint.setStrokeCap(Paint.Cap.SQUARE);
                        } else {
                            lineViewData.paint.setStrokeCap(Paint.Cap.ROUND);
                        }
                        if (BaseChartView.USE_LINES) {
                            canvas.drawPath(lineViewData.chartPath, lineViewData.paint);
                        } else {
                            canvas.drawLines(lineViewData.linesPath, 0, i6, lineViewData.paint);
                        }
                        canvas.restore();
                    }
                    r4 = f2;
                    lineViewData.paint.setAlpha((int) (lineViewData.alpha * 255.0f * r4));
                    if (this.endXIndex - this.startXIndex <= 100) {
                    }
                    if (BaseChartView.USE_LINES) {
                    }
                    canvas.restore();
                } else {
                    f = f7;
                    i = i4;
                }
                i4 = i + 1;
                f7 = f;
                i3 = 0;
            }
        }
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected void drawPickerChart(Canvas canvas) {
        int i;
        int i2;
        float f;
        float f2;
        LineViewData lineViewData;
        int i3;
        getMeasuredHeight();
        getMeasuredHeight();
        int size = this.lines.size();
        T t = this.chartData;
        if (t != 0) {
            float[] fArr = t.xPercentage;
            float f3 = fArr.length < 2 ? 1.0f : fArr[1] * this.pickerWidth;
            int i4 = 0;
            while (i4 < size) {
                LineViewData lineViewData2 = (LineViewData) this.lines.get(i4);
                if (lineViewData2.enabled || lineViewData2.alpha != 0.0f) {
                    lineViewData2.bottomLinePath.reset();
                    int length = this.chartData.xPercentage.length;
                    long[] jArr = lineViewData2.line.y;
                    lineViewData2.chartPath.reset();
                    int i5 = 0;
                    int i6 = 0;
                    while (i5 < length) {
                        if (jArr[i5] < 0) {
                            i2 = i4;
                            lineViewData = lineViewData2;
                        } else {
                            T t2 = this.chartData;
                            float f4 = t2.xPercentage[i5] * this.pickerWidth;
                            boolean z = BaseChartView.ANIMATE_PICKER_SIZES;
                            if (z) {
                                i2 = i4;
                                f = this.pickerMaxHeight;
                            } else {
                                i2 = i4;
                                f = (float) t2.maxValue;
                            }
                            if (z) {
                                f2 = this.pickerMinHeight;
                                lineViewData = lineViewData2;
                            } else {
                                f2 = (float) t2.minValue;
                                lineViewData = lineViewData2;
                            }
                            float f5 = (1.0f - ((((float) jArr[i5]) - f2) / (f - f2))) * this.pikerHeight;
                            if (i6 == 0) {
                                float[] fArr2 = lineViewData.linesPathBottom;
                                int i7 = i6 + 1;
                                float f6 = f3 / 2.0f;
                                fArr2[i6] = f4 - f6;
                                int i8 = i7 + 1;
                                fArr2[i7] = f5;
                                int i9 = i8 + 1;
                                float f7 = f4 + f6;
                                fArr2[i8] = f7;
                                int i10 = i9 + 1;
                                fArr2[i9] = f5;
                                int i11 = i10 + 1;
                                fArr2[i10] = f7;
                                i3 = i11 + 1;
                                fArr2[i11] = f5;
                            } else if (i5 == length - 1) {
                                float[] fArr3 = lineViewData.linesPathBottom;
                                int i12 = i6 + 1;
                                float f8 = f3 / 2.0f;
                                float f9 = f4 - f8;
                                fArr3[i6] = f9;
                                int i13 = i12 + 1;
                                fArr3[i12] = f5;
                                int i14 = i13 + 1;
                                fArr3[i13] = f9;
                                int i15 = i14 + 1;
                                fArr3[i14] = f5;
                                int i16 = i15 + 1;
                                float f10 = f4 + f8;
                                fArr3[i15] = f10;
                                int i17 = i16 + 1;
                                fArr3[i16] = f5;
                                int i18 = i17 + 1;
                                fArr3[i17] = f10;
                                int i19 = i18 + 1;
                                fArr3[i18] = f5;
                                int i20 = i19 + 1;
                                fArr3[i19] = f10;
                                i6 = i20 + 1;
                                fArr3[i20] = 0.0f;
                            } else {
                                float[] fArr4 = lineViewData.linesPathBottom;
                                int i21 = i6 + 1;
                                float f11 = f3 / 2.0f;
                                float f12 = f4 - f11;
                                fArr4[i6] = f12;
                                int i22 = i21 + 1;
                                fArr4[i21] = f5;
                                int i23 = i22 + 1;
                                fArr4[i22] = f12;
                                int i24 = i23 + 1;
                                fArr4[i23] = f5;
                                int i25 = i24 + 1;
                                float f13 = f4 + f11;
                                fArr4[i24] = f13;
                                int i26 = i25 + 1;
                                fArr4[i25] = f5;
                                int i27 = i26 + 1;
                                fArr4[i26] = f13;
                                i3 = i27 + 1;
                                fArr4[i27] = f5;
                            }
                            i6 = i3;
                        }
                        i5++;
                        lineViewData2 = lineViewData;
                        i4 = i2;
                    }
                    i = i4;
                    LineViewData lineViewData3 = lineViewData2;
                    lineViewData3.linesPathBottomSize = i6;
                    if (lineViewData3.enabled || lineViewData3.alpha != 0.0f) {
                        lineViewData3.bottomLinePaint.setAlpha((int) (lineViewData3.alpha * 255.0f));
                        canvas.drawLines(lineViewData3.linesPathBottom, 0, lineViewData3.linesPathBottomSize, lineViewData3.bottomLinePaint);
                        i4 = i + 1;
                    }
                } else {
                    i = i4;
                }
                i4 = i + 1;
            }
        }
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    public LineViewData createLineViewData(ChartData.Line line) {
        return new LineViewData(line, true);
    }
}
