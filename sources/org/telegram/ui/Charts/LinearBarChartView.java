package org.telegram.ui.Charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import org.telegram.ui.Charts.data.ChartData;
import org.telegram.ui.Charts.view_data.LineViewData;
import org.telegram.ui.Charts.view_data.TransitionParams;

/* loaded from: classes4.dex */
public class LinearBarChartView extends BaseChartView {
    public LinearBarChartView(Context context) {
        super(context);
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    public LineViewData createLineViewData(ChartData.Line line) {
        return new LineViewData(line, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x01be  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x01cf  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01d8  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x01c6  */
    @Override // org.telegram.ui.Charts.BaseChartView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void drawChart(Canvas canvas) {
        float f;
        int i;
        float f2;
        TransitionParams transitionParams;
        float f3;
        Paint paint;
        Paint.Cap cap;
        float f4;
        if (this.chartData != null) {
            float f5 = this.chartWidth;
            ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
            float f6 = chartPickerDelegate.pickerEnd;
            float f7 = chartPickerDelegate.pickerStart;
            float f8 = f5 / (f6 - f7);
            float f9 = (f7 * f8) - BaseChartView.HORIZONTAL_PADDING;
            int i2 = 0;
            int i3 = 0;
            while (i3 < this.lines.size()) {
                LineViewData lineViewData = (LineViewData) this.lines.get(i3);
                if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                    float[] fArr = this.chartData.xPercentage;
                    float f10 = fArr.length < 2 ? 0.0f : fArr[1] * f8;
                    long[] jArr = lineViewData.line.y;
                    int i4 = ((int) (BaseChartView.HORIZONTAL_PADDING / f10)) + 1;
                    lineViewData.chartPath.reset();
                    int max = Math.max(i2, this.startXIndex - i4);
                    int min = Math.min(this.chartData.xPercentage.length - 1, this.endXIndex + i4);
                    boolean z = true;
                    int i5 = 0;
                    while (max <= min) {
                        int i6 = i3;
                        long j = jArr[max];
                        if (j < 0) {
                            f4 = f8;
                        } else {
                            float f11 = (this.chartData.xPercentage[max] * f8) - f9;
                            float f12 = this.currentMinHeight;
                            float f13 = (((float) j) - f12) / (this.currentMaxHeight - f12);
                            float strokeWidth = lineViewData.paint.getStrokeWidth() / 2.0f;
                            f4 = f8;
                            float measuredHeight = ((getMeasuredHeight() - this.chartBottom) - strokeWidth) - (f13 * (((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) - strokeWidth));
                            if (BaseChartView.USE_LINES) {
                                float[] fArr2 = lineViewData.linesPath;
                                int i7 = i5 + 1;
                                if (i5 == 0) {
                                    float f14 = f10 / 2.0f;
                                    fArr2[i5] = f11 - f14;
                                    fArr2[i7] = measuredHeight;
                                    float f15 = f11 + f14;
                                    fArr2[i5 + 2] = f15;
                                    fArr2[i5 + 3] = measuredHeight;
                                    int i8 = i5 + 5;
                                    fArr2[i5 + 4] = f15;
                                    i5 += 6;
                                    fArr2[i8] = measuredHeight;
                                } else if (max == min) {
                                    float f16 = f10 / 2.0f;
                                    float f17 = f11 - f16;
                                    fArr2[i5] = f17;
                                    fArr2[i7] = measuredHeight;
                                    fArr2[i5 + 2] = f17;
                                    fArr2[i5 + 3] = measuredHeight;
                                    float f18 = f11 + f16;
                                    fArr2[i5 + 4] = f18;
                                    fArr2[i5 + 5] = measuredHeight;
                                    fArr2[i5 + 6] = f18;
                                    fArr2[i5 + 7] = measuredHeight;
                                    int i9 = i5 + 9;
                                    fArr2[i5 + 8] = f18;
                                    i5 += 10;
                                    fArr2[i9] = (getMeasuredHeight() - this.chartBottom) - strokeWidth;
                                } else {
                                    float f19 = f10 / 2.0f;
                                    float f20 = f11 - f19;
                                    fArr2[i5] = f20;
                                    fArr2[i7] = measuredHeight;
                                    fArr2[i5 + 2] = f20;
                                    fArr2[i5 + 3] = measuredHeight;
                                    float f21 = f11 + f19;
                                    fArr2[i5 + 4] = f21;
                                    fArr2[i5 + 5] = measuredHeight;
                                    int i10 = i5 + 7;
                                    fArr2[i5 + 6] = f21;
                                    i5 += 8;
                                    fArr2[i10] = measuredHeight;
                                }
                            } else {
                                Path path = lineViewData.chartPath;
                                float f22 = f11 - (f10 / 2.0f);
                                if (z) {
                                    path.moveTo(f22, measuredHeight);
                                    z = false;
                                } else {
                                    path.lineTo(f22, measuredHeight);
                                }
                                lineViewData.chartPath.lineTo(f11 + (f10 / 2.0f), measuredHeight);
                            }
                        }
                        max++;
                        i3 = i6;
                        f8 = f4;
                    }
                    f = f8;
                    i = i3;
                    canvas.save();
                    int i11 = this.transitionMode;
                    float f23 = 1.0f;
                    if (i11 == 2) {
                        transitionParams = this.transitionParams;
                        float f24 = transitionParams.progress;
                        f2 = f24 > 0.5f ? 0.0f : 1.0f - (f24 * 2.0f);
                        f3 = (f24 * 2.0f) + 1.0f;
                    } else if (i11 == 1) {
                        float f25 = this.transitionParams.progress;
                        f2 = f25 < 0.3f ? 0.0f : f25;
                        canvas.save();
                        transitionParams = this.transitionParams;
                        f3 = transitionParams.progress;
                        if (transitionParams.needScaleY) {
                            f23 = f3;
                        }
                    } else {
                        if (i11 == 3) {
                            f23 = this.transitionParams.progress;
                        }
                        lineViewData.paint.setAlpha((int) (lineViewData.alpha * 255.0f * f23));
                        if (this.endXIndex - this.startXIndex <= 100) {
                            paint = lineViewData.paint;
                            cap = Paint.Cap.SQUARE;
                        } else {
                            paint = lineViewData.paint;
                            cap = Paint.Cap.ROUND;
                        }
                        paint.setStrokeCap(cap);
                        if (BaseChartView.USE_LINES) {
                            canvas.drawPath(lineViewData.chartPath, lineViewData.paint);
                        } else {
                            canvas.drawLines(lineViewData.linesPath, 0, i5, lineViewData.paint);
                        }
                        canvas.restore();
                    }
                    canvas.scale(f3, f23, transitionParams.pX, transitionParams.pY);
                    f23 = f2;
                    lineViewData.paint.setAlpha((int) (lineViewData.alpha * 255.0f * f23));
                    if (this.endXIndex - this.startXIndex <= 100) {
                    }
                    paint.setStrokeCap(cap);
                    if (BaseChartView.USE_LINES) {
                    }
                    canvas.restore();
                } else {
                    f = f8;
                    i = i3;
                }
                i3 = i + 1;
                f8 = f;
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
        ChartData chartData = this.chartData;
        if (chartData != null) {
            float[] fArr = chartData.xPercentage;
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
                            ChartData chartData2 = this.chartData;
                            float f4 = chartData2.xPercentage[i4] * this.pickerWidth;
                            boolean z = BaseChartView.ANIMATE_PICKER_SIZES;
                            if (z) {
                                f = this.pickerMaxHeight;
                                i2 = i3;
                            } else {
                                i2 = i3;
                                f = (float) chartData2.maxValue;
                            }
                            if (z) {
                                f2 = this.pickerMinHeight;
                                jArr = jArr2;
                            } else {
                                jArr = jArr2;
                                f2 = (float) chartData2.minValue;
                            }
                            float f5 = (1.0f - ((((float) j) - f2) / (f - f2))) * this.pikerHeight;
                            if (!BaseChartView.USE_LINES) {
                                Path path = lineViewData.bottomLinePath;
                                float f6 = f4 - (f3 / 2.0f);
                                if (i4 == 0) {
                                    path.moveTo(f6, f5);
                                } else {
                                    path.lineTo(f6, f5);
                                }
                                lineViewData.bottomLinePath.lineTo(f4 + (f3 / 2.0f), f5);
                            } else if (i5 == 0) {
                                float[] fArr2 = lineViewData.linesPathBottom;
                                float f7 = f3 / 2.0f;
                                fArr2[i5] = f4 - f7;
                                fArr2[i5 + 1] = f5;
                                float f8 = f4 + f7;
                                fArr2[i5 + 2] = f8;
                                fArr2[i5 + 3] = f5;
                                int i6 = i5 + 5;
                                fArr2[i5 + 4] = f8;
                                i5 += 6;
                                fArr2[i6] = f5;
                            } else if (i4 == length - 1) {
                                float[] fArr3 = lineViewData.linesPathBottom;
                                float f9 = f3 / 2.0f;
                                float f10 = f4 - f9;
                                fArr3[i5] = f10;
                                fArr3[i5 + 1] = f5;
                                fArr3[i5 + 2] = f10;
                                fArr3[i5 + 3] = f5;
                                float f11 = f4 + f9;
                                fArr3[i5 + 4] = f11;
                                fArr3[i5 + 5] = f5;
                                fArr3[i5 + 6] = f11;
                                fArr3[i5 + 7] = f5;
                                int i7 = i5 + 9;
                                fArr3[i5 + 8] = f11;
                                i5 += 10;
                                fArr3[i7] = 0.0f;
                            } else {
                                float[] fArr4 = lineViewData.linesPathBottom;
                                float f12 = f3 / 2.0f;
                                float f13 = f4 - f12;
                                fArr4[i5] = f13;
                                fArr4[i5 + 1] = f5;
                                fArr4[i5 + 2] = f13;
                                fArr4[i5 + 3] = f5;
                                float f14 = f4 + f12;
                                fArr4[i5 + 4] = f14;
                                fArr4[i5 + 5] = f5;
                                int i8 = i5 + 7;
                                fArr4[i5 + 6] = f14;
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Charts.BaseChartView
    public void init() {
        this.useMinHeight = true;
        super.init();
    }
}
