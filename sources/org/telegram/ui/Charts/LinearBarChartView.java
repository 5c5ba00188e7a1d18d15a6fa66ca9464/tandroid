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

    /* JADX WARN: Removed duplicated region for block: B:57:0x01d4  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01dc  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x01e7  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x01f0  */
    @Override // org.telegram.ui.Charts.BaseChartView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void drawChart(Canvas canvas) {
        float f;
        int i;
        float f2;
        float f3;
        if (this.chartData != 0) {
            float f4 = this.chartWidth;
            ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
            float f5 = chartPickerDelegate.pickerEnd;
            float f6 = chartPickerDelegate.pickerStart;
            float f7 = f4 / (f5 - f6);
            float f8 = (f6 * f7) - BaseChartView.HORIZONTAL_PADDING;
            int i2 = 0;
            int i3 = 0;
            while (i3 < this.lines.size()) {
                LineViewData lineViewData = (LineViewData) this.lines.get(i3);
                if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                    float[] fArr = this.chartData.xPercentage;
                    float f9 = fArr.length < 2 ? 0.0f : fArr[1] * f7;
                    long[] jArr = lineViewData.line.y;
                    int i4 = ((int) (BaseChartView.HORIZONTAL_PADDING / f9)) + 1;
                    lineViewData.chartPath.reset();
                    int max = Math.max(i2, this.startXIndex - i4);
                    int min = Math.min(this.chartData.xPercentage.length - 1, this.endXIndex + i4);
                    boolean z = true;
                    int i5 = 0;
                    while (max <= min) {
                        int i6 = i3;
                        long j = jArr[max];
                        if (j < 0) {
                            f3 = f7;
                        } else {
                            float f10 = (this.chartData.xPercentage[max] * f7) - f8;
                            float f11 = this.currentMinHeight;
                            float f12 = (((float) j) - f11) / (this.currentMaxHeight - f11);
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
                            } else if (i5 == 0) {
                                float[] fArr2 = lineViewData.linesPath;
                                float f13 = f9 / 2.0f;
                                fArr2[i5] = f10 - f13;
                                fArr2[i5 + 1] = measuredHeight;
                                float f14 = f10 + f13;
                                fArr2[i5 + 2] = f14;
                                fArr2[i5 + 3] = measuredHeight;
                                int i7 = i5 + 5;
                                fArr2[i5 + 4] = f14;
                                i5 += 6;
                                fArr2[i7] = measuredHeight;
                            } else if (max == min) {
                                float[] fArr3 = lineViewData.linesPath;
                                float f15 = f9 / 2.0f;
                                float f16 = f10 - f15;
                                fArr3[i5] = f16;
                                fArr3[i5 + 1] = measuredHeight;
                                fArr3[i5 + 2] = f16;
                                fArr3[i5 + 3] = measuredHeight;
                                float f17 = f10 + f15;
                                fArr3[i5 + 4] = f17;
                                fArr3[i5 + 5] = measuredHeight;
                                fArr3[i5 + 6] = f17;
                                fArr3[i5 + 7] = measuredHeight;
                                int i8 = i5 + 9;
                                fArr3[i5 + 8] = f17;
                                i5 += 10;
                                fArr3[i8] = (getMeasuredHeight() - this.chartBottom) - strokeWidth;
                            } else {
                                float[] fArr4 = lineViewData.linesPath;
                                float f18 = f9 / 2.0f;
                                float f19 = f10 - f18;
                                fArr4[i5] = f19;
                                fArr4[i5 + 1] = measuredHeight;
                                fArr4[i5 + 2] = f19;
                                fArr4[i5 + 3] = measuredHeight;
                                float f20 = f10 + f18;
                                fArr4[i5 + 4] = f20;
                                fArr4[i5 + 5] = measuredHeight;
                                int i9 = i5 + 7;
                                fArr4[i5 + 6] = f20;
                                i5 += 8;
                                fArr4[i9] = measuredHeight;
                            }
                        }
                        max++;
                        i3 = i6;
                        f7 = f3;
                    }
                    f = f7;
                    i = i3;
                    canvas.save();
                    int i10 = this.transitionMode;
                    if (i10 == 2) {
                        TransitionParams transitionParams = this.transitionParams;
                        float f21 = transitionParams.progress;
                        f2 = f21 > 0.5f ? 0.0f : 1.0f - (f21 * 2.0f);
                        canvas.scale((f21 * 2.0f) + 1.0f, 1.0f, transitionParams.pX, transitionParams.pY);
                    } else if (i10 == 1) {
                        float f22 = this.transitionParams.progress;
                        f2 = f22 < 0.3f ? 0.0f : f22;
                        canvas.save();
                        TransitionParams transitionParams2 = this.transitionParams;
                        float f23 = transitionParams2.progress;
                        canvas.scale(f23, transitionParams2.needScaleY ? f23 : 1.0f, transitionParams2.pX, transitionParams2.pY);
                    } else {
                        if (i10 == 3) {
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
                            canvas.drawLines(lineViewData.linesPath, 0, i5, lineViewData.paint);
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
                    i = i3;
                }
                i3 = i + 1;
                f7 = f;
                i2 = 0;
            }
        }
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected void drawPickerChart(Canvas canvas) {
        int i;
        int i2;
        float f;
        long[] jArr;
        float f2;
        getMeasuredHeight();
        getMeasuredHeight();
        int size = this.lines.size();
        T t = this.chartData;
        if (t != 0) {
            float[] fArr = t.xPercentage;
            float f3 = fArr.length < 2 ? 1.0f : fArr[1] * this.pickerWidth;
            int i3 = 0;
            while (i3 < size) {
                LineViewData lineViewData = (LineViewData) this.lines.get(i3);
                if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                    lineViewData.bottomLinePath.reset();
                    int length = this.chartData.xPercentage.length;
                    long[] jArr2 = lineViewData.line.y;
                    lineViewData.chartPath.reset();
                    int i4 = 0;
                    int i5 = 0;
                    while (i4 < length) {
                        long j = jArr2[i4];
                        if (j < 0) {
                            i2 = i3;
                            jArr = jArr2;
                        } else {
                            T t2 = this.chartData;
                            float f4 = t2.xPercentage[i4] * this.pickerWidth;
                            boolean z = BaseChartView.ANIMATE_PICKER_SIZES;
                            if (z) {
                                f = this.pickerMaxHeight;
                                i2 = i3;
                            } else {
                                i2 = i3;
                                f = (float) t2.maxValue;
                            }
                            if (z) {
                                f2 = this.pickerMinHeight;
                                jArr = jArr2;
                            } else {
                                jArr = jArr2;
                                f2 = (float) t2.minValue;
                            }
                            float f5 = (1.0f - ((((float) j) - f2) / (f - f2))) * this.pikerHeight;
                            if (!BaseChartView.USE_LINES) {
                                if (i4 == 0) {
                                    lineViewData.bottomLinePath.moveTo(f4 - (f3 / 2.0f), f5);
                                } else {
                                    lineViewData.bottomLinePath.lineTo(f4 - (f3 / 2.0f), f5);
                                }
                                lineViewData.bottomLinePath.lineTo(f4 + (f3 / 2.0f), f5);
                            } else if (i5 == 0) {
                                float[] fArr2 = lineViewData.linesPathBottom;
                                float f6 = f3 / 2.0f;
                                fArr2[i5] = f4 - f6;
                                fArr2[i5 + 1] = f5;
                                float f7 = f4 + f6;
                                fArr2[i5 + 2] = f7;
                                fArr2[i5 + 3] = f5;
                                int i6 = i5 + 5;
                                fArr2[i5 + 4] = f7;
                                i5 += 6;
                                fArr2[i6] = f5;
                            } else if (i4 == length - 1) {
                                float[] fArr3 = lineViewData.linesPathBottom;
                                float f8 = f3 / 2.0f;
                                float f9 = f4 - f8;
                                fArr3[i5] = f9;
                                fArr3[i5 + 1] = f5;
                                fArr3[i5 + 2] = f9;
                                fArr3[i5 + 3] = f5;
                                float f10 = f4 + f8;
                                fArr3[i5 + 4] = f10;
                                fArr3[i5 + 5] = f5;
                                fArr3[i5 + 6] = f10;
                                fArr3[i5 + 7] = f5;
                                int i7 = i5 + 9;
                                fArr3[i5 + 8] = f10;
                                i5 += 10;
                                fArr3[i7] = 0.0f;
                            } else {
                                float[] fArr4 = lineViewData.linesPathBottom;
                                float f11 = f3 / 2.0f;
                                float f12 = f4 - f11;
                                fArr4[i5] = f12;
                                fArr4[i5 + 1] = f5;
                                fArr4[i5 + 2] = f12;
                                fArr4[i5 + 3] = f5;
                                float f13 = f4 + f11;
                                fArr4[i5 + 4] = f13;
                                fArr4[i5 + 5] = f5;
                                int i8 = i5 + 7;
                                fArr4[i5 + 6] = f13;
                                i5 += 8;
                                fArr4[i8] = f5;
                            }
                        }
                        i4++;
                        jArr2 = jArr;
                        i3 = i2;
                    }
                    i = i3;
                    lineViewData.linesPathBottomSize = i5;
                    if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                        lineViewData.bottomLinePaint.setAlpha((int) (lineViewData.alpha * 255.0f));
                        if (BaseChartView.USE_LINES) {
                            canvas.drawLines(lineViewData.linesPathBottom, 0, lineViewData.linesPathBottomSize, lineViewData.bottomLinePaint);
                        } else {
                            canvas.drawPath(lineViewData.bottomLinePath, lineViewData.bottomLinePaint);
                        }
                        i3 = i + 1;
                    }
                } else {
                    i = i3;
                }
                i3 = i + 1;
            }
        }
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    public LineViewData createLineViewData(ChartData.Line line) {
        return new LineViewData(line, true);
    }
}
