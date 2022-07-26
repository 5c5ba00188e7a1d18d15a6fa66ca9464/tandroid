package org.telegram.ui.Charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import org.telegram.ui.Charts.data.ChartData;
import org.telegram.ui.Charts.view_data.LineViewData;
import org.telegram.ui.Charts.view_data.TransitionParams;
/* loaded from: classes3.dex */
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

    /* JADX WARN: Removed duplicated region for block: B:46:0x0151  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0164  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x016d  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0159  */
    @Override // org.telegram.ui.Charts.BaseChartView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void drawChart(Canvas canvas) {
        float f;
        float f2;
        float f3;
        if (this.chartData != 0) {
            float f4 = this.chartWidth;
            ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
            float f5 = chartPickerDelegate.pickerEnd;
            float f6 = chartPickerDelegate.pickerStart;
            float f7 = f4 / (f5 - f6);
            float f8 = (f6 * f7) - BaseChartView.HORIZONTAL_PADDING;
            int i = 0;
            int i2 = 0;
            while (i2 < this.lines.size()) {
                LineViewData lineViewData = (LineViewData) this.lines.get(i2);
                if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                    float[] fArr = this.chartData.xPercentage;
                    float f9 = fArr.length < 2 ? 0.0f : fArr[1] * f7;
                    int[] iArr = lineViewData.line.y;
                    int i3 = ((int) (BaseChartView.HORIZONTAL_PADDING / f9)) + 1;
                    lineViewData.chartPath.reset();
                    int max = Math.max(i, this.startXIndex - i3);
                    int min = Math.min(this.chartData.xPercentage.length - 1, this.endXIndex + i3);
                    boolean z = true;
                    int i4 = 0;
                    while (max <= min) {
                        if (iArr[max] < 0) {
                            f3 = f7;
                        } else {
                            float f10 = (this.chartData.xPercentage[max] * f7) - f8;
                            float f11 = this.currentMinHeight;
                            float f12 = (iArr[max] - f11) / (this.currentMaxHeight - f11);
                            float strokeWidth = lineViewData.paint.getStrokeWidth() / 2.0f;
                            f3 = f7;
                            float measuredHeight = ((getMeasuredHeight() - this.chartBottom) - strokeWidth) - (f12 * (((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) - strokeWidth));
                            if (BaseChartView.USE_LINES) {
                                if (i4 == 0) {
                                    float[] fArr2 = lineViewData.linesPath;
                                    int i5 = i4 + 1;
                                    fArr2[i4] = f10;
                                    i4 = i5 + 1;
                                    fArr2[i5] = measuredHeight;
                                } else {
                                    float[] fArr3 = lineViewData.linesPath;
                                    int i6 = i4 + 1;
                                    fArr3[i4] = f10;
                                    int i7 = i6 + 1;
                                    fArr3[i6] = measuredHeight;
                                    int i8 = i7 + 1;
                                    fArr3[i7] = f10;
                                    i4 = i8 + 1;
                                    fArr3[i8] = measuredHeight;
                                }
                            } else if (z) {
                                lineViewData.chartPath.moveTo(f10, measuredHeight);
                                z = false;
                            } else {
                                lineViewData.chartPath.lineTo(f10, measuredHeight);
                            }
                        }
                        max++;
                        f7 = f3;
                    }
                    f = f7;
                    canvas.save();
                    int i9 = this.transitionMode;
                    float f13 = 1.0f;
                    if (i9 == 2) {
                        TransitionParams transitionParams = this.transitionParams;
                        float f14 = transitionParams.progress;
                        f2 = f14 > 0.5f ? 0.0f : 1.0f - (f14 * 2.0f);
                        canvas.scale((f14 * 2.0f) + 1.0f, 1.0f, transitionParams.pX, transitionParams.pY);
                    } else if (i9 == 1) {
                        float f15 = this.transitionParams.progress;
                        f2 = f15 < 0.3f ? 0.0f : f15;
                        canvas.save();
                        TransitionParams transitionParams2 = this.transitionParams;
                        float f16 = transitionParams2.progress;
                        if (transitionParams2.needScaleY) {
                            f13 = f16;
                        }
                        canvas.scale(f16, f13, transitionParams2.pX, transitionParams2.pY);
                    } else {
                        if (i9 == 3) {
                            f13 = this.transitionParams.progress;
                        }
                        lineViewData.paint.setAlpha((int) (lineViewData.alpha * 255.0f * f13));
                        if (this.endXIndex - this.startXIndex <= 100) {
                            lineViewData.paint.setStrokeCap(Paint.Cap.SQUARE);
                        } else {
                            lineViewData.paint.setStrokeCap(Paint.Cap.ROUND);
                        }
                        if (BaseChartView.USE_LINES) {
                            canvas.drawPath(lineViewData.chartPath, lineViewData.paint);
                        } else {
                            canvas.drawLines(lineViewData.linesPath, 0, i4, lineViewData.paint);
                        }
                        canvas.restore();
                    }
                    f13 = f2;
                    lineViewData.paint.setAlpha((int) (lineViewData.alpha * 255.0f * f13));
                    if (this.endXIndex - this.startXIndex <= 100) {
                    }
                    if (BaseChartView.USE_LINES) {
                    }
                    canvas.restore();
                } else {
                    f = f7;
                }
                i2++;
                f7 = f;
                i = 0;
            }
        }
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected void drawPickerChart(Canvas canvas) {
        getMeasuredHeight();
        getMeasuredHeight();
        int size = this.lines.size();
        if (this.chartData != 0) {
            for (int i = 0; i < size; i++) {
                LineViewData lineViewData = (LineViewData) this.lines.get(i);
                if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                    lineViewData.bottomLinePath.reset();
                    int length = this.chartData.xPercentage.length;
                    int[] iArr = lineViewData.line.y;
                    lineViewData.chartPath.reset();
                    int i2 = 0;
                    for (int i3 = 0; i3 < length; i3++) {
                        if (iArr[i3] >= 0) {
                            T t = this.chartData;
                            float f = t.xPercentage[i3] * this.pickerWidth;
                            boolean z = BaseChartView.ANIMATE_PICKER_SIZES;
                            float f2 = z ? this.pickerMaxHeight : t.maxValue;
                            float f3 = z ? this.pickerMinHeight : t.minValue;
                            float f4 = (1.0f - ((iArr[i3] - f3) / (f2 - f3))) * this.pikerHeight;
                            if (BaseChartView.USE_LINES) {
                                if (i2 == 0) {
                                    float[] fArr = lineViewData.linesPathBottom;
                                    int i4 = i2 + 1;
                                    fArr[i2] = f;
                                    i2 = i4 + 1;
                                    fArr[i4] = f4;
                                } else {
                                    float[] fArr2 = lineViewData.linesPathBottom;
                                    int i5 = i2 + 1;
                                    fArr2[i2] = f;
                                    int i6 = i5 + 1;
                                    fArr2[i5] = f4;
                                    int i7 = i6 + 1;
                                    fArr2[i6] = f;
                                    i2 = i7 + 1;
                                    fArr2[i7] = f4;
                                }
                            } else if (i3 == 0) {
                                lineViewData.bottomLinePath.moveTo(f, f4);
                            } else {
                                lineViewData.bottomLinePath.lineTo(f, f4);
                            }
                        }
                    }
                    lineViewData.linesPathBottomSize = i2;
                    if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                        lineViewData.bottomLinePaint.setAlpha((int) (lineViewData.alpha * 255.0f));
                        if (BaseChartView.USE_LINES) {
                            canvas.drawLines(lineViewData.linesPathBottom, 0, lineViewData.linesPathBottomSize, lineViewData.bottomLinePaint);
                        } else {
                            canvas.drawPath(lineViewData.bottomLinePath, lineViewData.bottomLinePaint);
                        }
                    }
                }
            }
        }
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    /* renamed from: createLineViewData */
    public LineViewData mo1010createLineViewData(ChartData.Line line) {
        return new LineViewData(line);
    }
}
