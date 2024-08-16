package org.telegram.ui.Charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import org.telegram.ui.Charts.data.ChartData;
import org.telegram.ui.Charts.view_data.LineViewData;
import org.telegram.ui.Charts.view_data.TransitionParams;
/* loaded from: classes4.dex */
public class LinearChartView extends BaseChartView<ChartData, LineViewData> {
    public LinearChartView(Context context) {
        super(context);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Charts.BaseChartView
    public void init() {
        this.useMinHeight = true;
        super.init();
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x0151  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0159  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0164  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x016d  */
    @Override // org.telegram.ui.Charts.BaseChartView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void drawChart(Canvas canvas) {
        int i;
        float f;
        if (this.chartData != 0) {
            float f2 = this.chartWidth;
            ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
            float f3 = chartPickerDelegate.pickerEnd;
            float f4 = chartPickerDelegate.pickerStart;
            float f5 = f2 / (f3 - f4);
            float f6 = (f4 * f5) - BaseChartView.HORIZONTAL_PADDING;
            int i2 = 0;
            int i3 = 0;
            while (i3 < this.lines.size()) {
                LineViewData lineViewData = (LineViewData) this.lines.get(i3);
                if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                    float[] fArr = this.chartData.xPercentage;
                    float f7 = fArr.length < 2 ? 0.0f : fArr[1] * f5;
                    long[] jArr = lineViewData.line.y;
                    int i4 = ((int) (BaseChartView.HORIZONTAL_PADDING / f7)) + 1;
                    lineViewData.chartPath.reset();
                    int max = Math.max(i2, this.startXIndex - i4);
                    int min = Math.min(this.chartData.xPercentage.length - 1, this.endXIndex + i4);
                    boolean z = true;
                    int i5 = 0;
                    while (max <= min) {
                        int i6 = i3;
                        long j = jArr[max];
                        if (j >= 0) {
                            float f8 = (this.chartData.xPercentage[max] * f5) - f6;
                            float f9 = this.currentMinHeight;
                            float f10 = (((float) j) - f9) / (this.currentMaxHeight - f9);
                            float strokeWidth = lineViewData.paint.getStrokeWidth() / 2.0f;
                            float measuredHeight = ((getMeasuredHeight() - this.chartBottom) - strokeWidth) - (f10 * (((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) - strokeWidth));
                            if (BaseChartView.USE_LINES) {
                                if (i5 == 0) {
                                    float[] fArr2 = lineViewData.linesPath;
                                    int i7 = i5 + 1;
                                    fArr2[i5] = f8;
                                    i5 += 2;
                                    fArr2[i7] = measuredHeight;
                                } else {
                                    float[] fArr3 = lineViewData.linesPath;
                                    fArr3[i5] = f8;
                                    fArr3[i5 + 1] = measuredHeight;
                                    int i8 = i5 + 3;
                                    fArr3[i5 + 2] = f8;
                                    i5 += 4;
                                    fArr3[i8] = measuredHeight;
                                }
                            } else if (z) {
                                lineViewData.chartPath.moveTo(f8, measuredHeight);
                                z = false;
                            } else {
                                lineViewData.chartPath.lineTo(f8, measuredHeight);
                            }
                        }
                        max++;
                        i3 = i6;
                    }
                    i = i3;
                    canvas.save();
                    int i9 = this.transitionMode;
                    if (i9 == 2) {
                        TransitionParams transitionParams = this.transitionParams;
                        float f11 = transitionParams.progress;
                        f = f11 > 0.5f ? 0.0f : 1.0f - (f11 * 2.0f);
                        canvas.scale((f11 * 2.0f) + 1.0f, 1.0f, transitionParams.pX, transitionParams.pY);
                    } else if (i9 == 1) {
                        float f12 = this.transitionParams.progress;
                        f = f12 < 0.3f ? 0.0f : f12;
                        canvas.save();
                        TransitionParams transitionParams2 = this.transitionParams;
                        float f13 = transitionParams2.progress;
                        canvas.scale(f13, transitionParams2.needScaleY ? f13 : 1.0f, transitionParams2.pX, transitionParams2.pY);
                    } else {
                        if (i9 == 3) {
                            r5 = this.transitionParams.progress;
                        }
                        lineViewData.paint.setAlpha((int) (lineViewData.alpha * 255.0f * r5));
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
                    r5 = f;
                    lineViewData.paint.setAlpha((int) (lineViewData.alpha * 255.0f * r5));
                    if (this.endXIndex - this.startXIndex <= 100) {
                    }
                    if (BaseChartView.USE_LINES) {
                    }
                    canvas.restore();
                } else {
                    i = i3;
                }
                i3 = i + 1;
                i2 = 0;
            }
        }
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected void drawPickerChart(Canvas canvas) {
        int i;
        long[] jArr;
        float f;
        int i2;
        float f2;
        getMeasuredHeight();
        getMeasuredHeight();
        int size = this.lines.size();
        if (this.chartData != 0) {
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
                            jArr = jArr2;
                            i2 = i3;
                        } else {
                            T t = this.chartData;
                            float f3 = t.xPercentage[i4] * this.pickerWidth;
                            boolean z = BaseChartView.ANIMATE_PICKER_SIZES;
                            if (z) {
                                f = this.pickerMaxHeight;
                                jArr = jArr2;
                            } else {
                                jArr = jArr2;
                                f = (float) t.maxValue;
                            }
                            if (z) {
                                f2 = this.pickerMinHeight;
                                i2 = i3;
                            } else {
                                i2 = i3;
                                f2 = (float) t.minValue;
                            }
                            float f4 = (1.0f - ((((float) j) - f2) / (f - f2))) * this.pikerHeight;
                            if (BaseChartView.USE_LINES) {
                                if (i5 == 0) {
                                    float[] fArr = lineViewData.linesPathBottom;
                                    int i6 = i5 + 1;
                                    fArr[i5] = f3;
                                    i5 += 2;
                                    fArr[i6] = f4;
                                } else {
                                    float[] fArr2 = lineViewData.linesPathBottom;
                                    fArr2[i5] = f3;
                                    fArr2[i5 + 1] = f4;
                                    int i7 = i5 + 3;
                                    fArr2[i5 + 2] = f3;
                                    i5 += 4;
                                    fArr2[i7] = f4;
                                }
                            } else if (i4 == 0) {
                                lineViewData.bottomLinePath.moveTo(f3, f4);
                            } else {
                                lineViewData.bottomLinePath.lineTo(f3, f4);
                            }
                        }
                        i4++;
                        i3 = i2;
                        jArr2 = jArr;
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
        return new LineViewData(line, false);
    }
}
