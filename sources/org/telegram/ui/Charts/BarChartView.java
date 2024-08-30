package org.telegram.ui.Charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.Charts.data.ChartData;
import org.telegram.ui.Charts.view_data.BarViewData;
import org.telegram.ui.Charts.view_data.ChartHorizontalLinesData;
import org.telegram.ui.Charts.view_data.TransitionParams;
/* loaded from: classes4.dex */
public class BarChartView extends BaseChartView {
    public BarChartView(Context context) {
        super(context);
        this.superDraw = true;
        this.useAlphaSignature = true;
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    public BarViewData createLineViewData(ChartData.Line line) {
        return new BarViewData(line, this.resourcesProvider);
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0093  */
    @Override // org.telegram.ui.Charts.BaseChartView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void drawChart(Canvas canvas) {
        float f;
        float f2;
        int i;
        int i2;
        ChartData chartData = this.chartData;
        if (chartData != null) {
            float f3 = this.chartWidth;
            ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
            float f4 = chartPickerDelegate.pickerEnd;
            float f5 = chartPickerDelegate.pickerStart;
            float f6 = f3 / (f4 - f5);
            float f7 = (f5 * f6) - BaseChartView.HORIZONTAL_PADDING;
            char c = 1;
            int i3 = this.startXIndex - 1;
            int i4 = 0;
            int i5 = i3 < 0 ? 0 : i3;
            int i6 = this.endXIndex + 1;
            if (i6 > ((ChartData.Line) chartData.lines.get(0)).y.length - 1) {
                i6 = ((ChartData.Line) this.chartData.lines.get(0)).y.length - 1;
            }
            int i7 = i6;
            canvas.save();
            float f8 = 0.0f;
            canvas.clipRect(this.chartStart, 0.0f, this.chartEnd, getMeasuredHeight() - this.chartBottom);
            canvas.save();
            int i8 = this.transitionMode;
            float f9 = 2.0f;
            int i9 = 2;
            if (i8 == 2) {
                this.postTransition = true;
                this.selectionA = 0.0f;
                TransitionParams transitionParams = this.transitionParams;
                float f10 = transitionParams.progress;
                f2 = 1.0f - f10;
                canvas.scale((f10 * 2.0f) + 1.0f, 1.0f, transitionParams.pX, transitionParams.pY);
            } else if (i8 != 1) {
                f = 1.0f;
                i = 0;
                while (i < this.lines.size()) {
                    BarViewData barViewData = (BarViewData) this.lines.get(i);
                    if (barViewData.enabled || barViewData.alpha != f8) {
                        float[] fArr = this.chartData.xPercentage;
                        float f11 = fArr.length < i9 ? 1.0f : fArr[c] * f6;
                        long[] jArr = barViewData.line.y;
                        float f12 = barViewData.alpha;
                        int i10 = i5;
                        float f13 = 0.0f;
                        float f14 = 0.0f;
                        boolean z = false;
                        while (i10 <= i7) {
                            float f15 = ((f11 / f9) + (this.chartData.xPercentage[i10] * f6)) - f7;
                            long[] jArr2 = jArr;
                            float measuredHeight = (getMeasuredHeight() - this.chartBottom) - (((((float) jArr[i10]) / this.currentMaxHeight) * f12) * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT));
                            if (i10 == this.selectedIndex && this.legendShowing) {
                                f13 = measuredHeight;
                                f14 = f15;
                                z = true;
                            } else {
                                float[] fArr2 = barViewData.linesPath;
                                fArr2[i4] = f15;
                                fArr2[i4 + 1] = measuredHeight;
                                int i11 = i4 + 3;
                                fArr2[i4 + 2] = f15;
                                i4 += 4;
                                fArr2[i11] = getMeasuredHeight() - this.chartBottom;
                            }
                            i10++;
                            jArr = jArr2;
                            f9 = 2.0f;
                        }
                        Paint paint = (z || this.postTransition) ? barViewData.unselectedPaint : barViewData.paint;
                        paint.setStrokeWidth(f11);
                        if (z) {
                            barViewData.unselectedPaint.setColor(ColorUtils.blendARGB(barViewData.lineColor, barViewData.blendColor, 1.0f - this.selectionA));
                        }
                        if (this.postTransition) {
                            f8 = 0.0f;
                            barViewData.unselectedPaint.setColor(ColorUtils.blendARGB(barViewData.lineColor, barViewData.blendColor, 0.0f));
                        } else {
                            f8 = 0.0f;
                        }
                        int i12 = (int) (255.0f * f);
                        paint.setAlpha(i12);
                        canvas.drawLines(barViewData.linesPath, 0, i4, paint);
                        if (z) {
                            barViewData.paint.setStrokeWidth(f11);
                            barViewData.paint.setAlpha(i12);
                            Paint paint2 = barViewData.paint;
                            float f16 = f13;
                            i2 = i;
                            canvas.drawLine(f14, f16, f14, getMeasuredHeight() - this.chartBottom, paint2);
                            barViewData.paint.setAlpha(NotificationCenter.voipServiceCreated);
                            i = i2 + 1;
                            i9 = 2;
                            c = 1;
                            i4 = 0;
                            f9 = 2.0f;
                        } else {
                            i2 = i;
                        }
                    } else {
                        i2 = i;
                    }
                    i = i2 + 1;
                    i9 = 2;
                    c = 1;
                    i4 = 0;
                    f9 = 2.0f;
                }
                canvas.restore();
                canvas.restore();
            } else {
                TransitionParams transitionParams2 = this.transitionParams;
                f2 = transitionParams2.progress;
                canvas.scale(f2, 1.0f, transitionParams2.pX, transitionParams2.pY);
            }
            f = f2;
            i = 0;
            while (i < this.lines.size()) {
            }
            canvas.restore();
            canvas.restore();
        }
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected void drawPickerChart(Canvas canvas) {
        int i;
        int i2;
        int i3;
        long[] jArr;
        float f;
        int measuredHeight = getMeasuredHeight();
        int i4 = BaseChartView.PICKER_PADDING;
        int i5 = measuredHeight - i4;
        int measuredHeight2 = (getMeasuredHeight() - this.pikerHeight) - i4;
        int size = this.lines.size();
        if (this.chartData != null) {
            int i6 = 0;
            while (i6 < size) {
                BarViewData barViewData = (BarViewData) this.lines.get(i6);
                if (barViewData.enabled || barViewData.alpha != 0.0f) {
                    barViewData.bottomLinePath.reset();
                    float[] fArr = this.chartData.xPercentage;
                    int length = fArr.length;
                    float f2 = fArr.length < 2 ? 1.0f : fArr[1] * this.pickerWidth;
                    long[] jArr2 = barViewData.line.y;
                    float f3 = barViewData.alpha;
                    int i7 = 0;
                    int i8 = 0;
                    while (i7 < length) {
                        long j = jArr2[i7];
                        if (j < 0) {
                            i2 = size;
                            i3 = length;
                            jArr = jArr2;
                        } else {
                            ChartData chartData = this.chartData;
                            i2 = size;
                            float f4 = chartData.xPercentage[i7] * this.pickerWidth;
                            if (BaseChartView.ANIMATE_PICKER_SIZES) {
                                f = this.pickerMaxHeight;
                                i3 = length;
                                jArr = jArr2;
                            } else {
                                i3 = length;
                                jArr = jArr2;
                                f = (float) chartData.maxValue;
                            }
                            float[] fArr2 = barViewData.linesPath;
                            fArr2[i8] = f4;
                            fArr2[i8 + 1] = (1.0f - ((((float) j) / f) * f3)) * (i5 - measuredHeight2);
                            int i9 = i8 + 3;
                            fArr2[i8 + 2] = f4;
                            i8 += 4;
                            fArr2[i9] = getMeasuredHeight() - this.chartBottom;
                        }
                        i7++;
                        length = i3;
                        size = i2;
                        jArr2 = jArr;
                    }
                    i = size;
                    barViewData.paint.setStrokeWidth(f2 + 2.0f);
                    canvas.drawLines(barViewData.linesPath, 0, i8, barViewData.paint);
                } else {
                    i = size;
                }
                i6++;
                size = i;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Charts.BaseChartView
    public void drawSelection(Canvas canvas) {
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected float getMinDistance() {
        return 0.1f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Charts.BaseChartView, android.view.View
    public void onDraw(Canvas canvas) {
        tick();
        drawChart(canvas);
        drawBottomLine(canvas);
        this.tmpN = this.horizontalLines.size();
        int i = 0;
        while (true) {
            this.tmpI = i;
            int i2 = this.tmpI;
            if (i2 >= this.tmpN) {
                drawBottomSignature(canvas);
                drawPicker(canvas);
                drawSelection(canvas);
                super.onDraw(canvas);
                return;
            }
            drawHorizontalLines(canvas, (ChartHorizontalLinesData) this.horizontalLines.get(i2));
            drawSignaturesToHorizontalLines(canvas, (ChartHorizontalLinesData) this.horizontalLines.get(this.tmpI));
            i = this.tmpI + 1;
        }
    }
}
