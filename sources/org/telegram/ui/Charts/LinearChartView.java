package org.telegram.ui.Charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import org.telegram.ui.Charts.data.ChartData;
import org.telegram.ui.Charts.view_data.LineViewData;
import org.telegram.ui.Charts.view_data.TransitionParams;

/* loaded from: classes4.dex */
public class LinearChartView extends BaseChartView {
    public LinearChartView(Context context) {
        super(context);
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    public LineViewData createLineViewData(ChartData.Line line) {
        return new LineViewData(line, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0153  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x014a  */
    @Override // org.telegram.ui.Charts.BaseChartView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void drawChart(Canvas canvas) {
        int i;
        float f;
        TransitionParams transitionParams;
        float f2;
        Paint paint;
        Paint.Cap cap;
        if (this.chartData != null) {
            float f3 = this.chartWidth;
            ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
            float f4 = chartPickerDelegate.pickerEnd;
            float f5 = chartPickerDelegate.pickerStart;
            float f6 = f3 / (f4 - f5);
            float f7 = (f5 * f6) - BaseChartView.HORIZONTAL_PADDING;
            int i2 = 0;
            int i3 = 0;
            while (i3 < this.lines.size()) {
                LineViewData lineViewData = (LineViewData) this.lines.get(i3);
                if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                    float[] fArr = this.chartData.xPercentage;
                    float f8 = fArr.length < 2 ? 0.0f : fArr[1] * f6;
                    long[] jArr = lineViewData.line.y;
                    int i4 = ((int) (BaseChartView.HORIZONTAL_PADDING / f8)) + 1;
                    lineViewData.chartPath.reset();
                    int max = Math.max(i2, this.startXIndex - i4);
                    int min = Math.min(this.chartData.xPercentage.length - 1, this.endXIndex + i4);
                    boolean z = true;
                    int i5 = 0;
                    while (true) {
                        i = i3;
                        if (max > min) {
                            break;
                        }
                        long j = jArr[max];
                        if (j >= 0) {
                            float f9 = (this.chartData.xPercentage[max] * f6) - f7;
                            float f10 = this.currentMinHeight;
                            float f11 = (j - f10) / (this.currentMaxHeight - f10);
                            float strokeWidth = lineViewData.paint.getStrokeWidth() / 2.0f;
                            float measuredHeight = ((getMeasuredHeight() - this.chartBottom) - strokeWidth) - (f11 * (((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) - strokeWidth));
                            if (BaseChartView.USE_LINES) {
                                float[] fArr2 = lineViewData.linesPath;
                                int i6 = i5 + 1;
                                if (i5 == 0) {
                                    fArr2[i5] = f9;
                                    i5 += 2;
                                    fArr2[i6] = measuredHeight;
                                } else {
                                    fArr2[i5] = f9;
                                    fArr2[i6] = measuredHeight;
                                    int i7 = i5 + 3;
                                    fArr2[i5 + 2] = f9;
                                    i5 += 4;
                                    fArr2[i7] = measuredHeight;
                                }
                            } else {
                                Path path = lineViewData.chartPath;
                                if (z) {
                                    path.moveTo(f9, measuredHeight);
                                    z = false;
                                } else {
                                    path.lineTo(f9, measuredHeight);
                                }
                            }
                        }
                        max++;
                        i3 = i;
                    }
                    canvas.save();
                    int i8 = this.transitionMode;
                    float f12 = 1.0f;
                    if (i8 == 2) {
                        transitionParams = this.transitionParams;
                        float f13 = transitionParams.progress;
                        f = f13 > 0.5f ? 0.0f : 1.0f - (f13 * 2.0f);
                        f2 = (f13 * 2.0f) + 1.0f;
                    } else if (i8 == 1) {
                        float f14 = this.transitionParams.progress;
                        f = f14 < 0.3f ? 0.0f : f14;
                        canvas.save();
                        transitionParams = this.transitionParams;
                        f2 = transitionParams.progress;
                        if (transitionParams.needScaleY) {
                            f12 = f2;
                        }
                    } else {
                        if (i8 == 3) {
                            f12 = this.transitionParams.progress;
                        }
                        lineViewData.paint.setAlpha((int) (lineViewData.alpha * 255.0f * f12));
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
                    canvas.scale(f2, f12, transitionParams.pX, transitionParams.pY);
                    f12 = f;
                    lineViewData.paint.setAlpha((int) (lineViewData.alpha * 255.0f * f12));
                    if (this.endXIndex - this.startXIndex <= 100) {
                    }
                    paint.setStrokeCap(cap);
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
        if (this.chartData != null) {
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
                            ChartData chartData = this.chartData;
                            float f3 = chartData.xPercentage[i4] * this.pickerWidth;
                            boolean z = BaseChartView.ANIMATE_PICKER_SIZES;
                            if (z) {
                                f = this.pickerMaxHeight;
                                jArr = jArr2;
                            } else {
                                jArr = jArr2;
                                f = chartData.maxValue;
                            }
                            if (z) {
                                f2 = this.pickerMinHeight;
                                i2 = i3;
                            } else {
                                i2 = i3;
                                f2 = chartData.minValue;
                            }
                            float f4 = (1.0f - ((j - f2) / (f - f2))) * this.pikerHeight;
                            if (BaseChartView.USE_LINES) {
                                float[] fArr = lineViewData.linesPathBottom;
                                int i6 = i5 + 1;
                                if (i5 == 0) {
                                    fArr[i5] = f3;
                                    i5 += 2;
                                    fArr[i6] = f4;
                                } else {
                                    fArr[i5] = f3;
                                    fArr[i6] = f4;
                                    int i7 = i5 + 3;
                                    fArr[i5 + 2] = f3;
                                    i5 += 4;
                                    fArr[i7] = f4;
                                }
                            } else {
                                Path path = lineViewData.bottomLinePath;
                                if (i4 == 0) {
                                    path.moveTo(f3, f4);
                                } else {
                                    path.lineTo(f3, f4);
                                }
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
    protected void init() {
        this.useMinHeight = true;
        super.init();
    }
}
