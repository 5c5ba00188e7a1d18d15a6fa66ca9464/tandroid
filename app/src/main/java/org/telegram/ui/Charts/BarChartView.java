package org.telegram.ui.Charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.graphics.ColorUtils;
import org.telegram.ui.Charts.data.ChartData;
import org.telegram.ui.Charts.view_data.BarViewData;
import org.telegram.ui.Charts.view_data.TransitionParams;
/* loaded from: classes3.dex */
public class BarChartView extends BaseChartView<ChartData, BarViewData> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Charts.BaseChartView
    public void drawSelection(Canvas canvas) {
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected float getMinDistance() {
        return 0.1f;
    }

    public BarChartView(Context context) {
        super(context);
        this.superDraw = true;
        this.useAlphaSignature = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0093  */
    @Override // org.telegram.ui.Charts.BaseChartView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void drawChart(Canvas canvas) {
        float f;
        float f2;
        int i;
        int i2;
        T t = this.chartData;
        if (t != 0) {
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
            if (i6 > t.lines.get(0).y.length - 1) {
                i6 = this.chartData.lines.get(0).y.length - 1;
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
            } else if (i8 == 1) {
                TransitionParams transitionParams2 = this.transitionParams;
                f2 = transitionParams2.progress;
                canvas.scale(f2, 1.0f, transitionParams2.pX, transitionParams2.pY);
            } else {
                f = 1.0f;
                i = 0;
                while (i < this.lines.size()) {
                    BarViewData barViewData = (BarViewData) this.lines.get(i);
                    if (barViewData.enabled || barViewData.alpha != f8) {
                        float[] fArr = this.chartData.xPercentage;
                        float f11 = fArr.length < i9 ? 1.0f : fArr[c] * f6;
                        int[] iArr = barViewData.line.y;
                        float f12 = barViewData.alpha;
                        int i10 = i5;
                        float f13 = 0.0f;
                        float f14 = 0.0f;
                        boolean z = false;
                        while (i10 <= i7) {
                            float f15 = ((f11 / f9) + (this.chartData.xPercentage[i10] * f6)) - f7;
                            int[] iArr2 = iArr;
                            float measuredHeight = (getMeasuredHeight() - this.chartBottom) - (((iArr[i10] / this.currentMaxHeight) * f12) * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT));
                            if (i10 != this.selectedIndex || !this.legendShowing) {
                                float[] fArr2 = barViewData.linesPath;
                                int i11 = i4 + 1;
                                fArr2[i4] = f15;
                                int i12 = i11 + 1;
                                fArr2[i11] = measuredHeight;
                                int i13 = i12 + 1;
                                fArr2[i12] = f15;
                                i4 = i13 + 1;
                                fArr2[i13] = getMeasuredHeight() - this.chartBottom;
                            } else {
                                f13 = measuredHeight;
                                f14 = f15;
                                z = true;
                            }
                            i10++;
                            iArr = iArr2;
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
                        int i14 = (int) (255.0f * f);
                        paint.setAlpha(i14);
                        canvas.drawLines(barViewData.linesPath, 0, i4, paint);
                        if (z) {
                            barViewData.paint.setStrokeWidth(f11);
                            barViewData.paint.setAlpha(i14);
                            Paint paint2 = barViewData.paint;
                            float f16 = f13;
                            i2 = i;
                            canvas.drawLine(f14, f16, f14, getMeasuredHeight() - this.chartBottom, paint2);
                            barViewData.paint.setAlpha(255);
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
        int measuredHeight = getMeasuredHeight();
        int i = BaseChartView.PICKER_PADDING;
        int i2 = measuredHeight - i;
        int measuredHeight2 = (getMeasuredHeight() - this.pikerHeight) - i;
        int size = this.lines.size();
        if (this.chartData != 0) {
            for (int i3 = 0; i3 < size; i3++) {
                BarViewData barViewData = (BarViewData) this.lines.get(i3);
                if (barViewData.enabled || barViewData.alpha != 0.0f) {
                    barViewData.bottomLinePath.reset();
                    float[] fArr = this.chartData.xPercentage;
                    int length = fArr.length;
                    float f = 1.0f;
                    float f2 = fArr.length < 2 ? 1.0f : fArr[1] * this.pickerWidth;
                    int[] iArr = barViewData.line.y;
                    float f3 = barViewData.alpha;
                    int i4 = 0;
                    int i5 = 0;
                    while (i4 < length) {
                        if (iArr[i4] >= 0) {
                            T t = this.chartData;
                            float f4 = t.xPercentage[i4] * this.pickerWidth;
                            float f5 = (f - ((iArr[i4] / (BaseChartView.ANIMATE_PICKER_SIZES ? this.pickerMaxHeight : t.maxValue)) * f3)) * (i2 - measuredHeight2);
                            float[] fArr2 = barViewData.linesPath;
                            int i6 = i5 + 1;
                            fArr2[i5] = f4;
                            int i7 = i6 + 1;
                            fArr2[i6] = f5;
                            int i8 = i7 + 1;
                            fArr2[i7] = f4;
                            i5 = i8 + 1;
                            fArr2[i8] = getMeasuredHeight() - this.chartBottom;
                        }
                        i4++;
                        f = 1.0f;
                    }
                    barViewData.paint.setStrokeWidth(f2 + 2.0f);
                    canvas.drawLines(barViewData.linesPath, 0, i5, barViewData.paint);
                }
            }
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.telegram.ui.Charts.BaseChartView
    /* renamed from: createLineViewData */
    public BarViewData mo1029createLineViewData(ChartData.Line line) {
        return new BarViewData(line);
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
            if (i2 < this.tmpN) {
                drawHorizontalLines(canvas, this.horizontalLines.get(i2));
                drawSignaturesToHorizontalLines(canvas, this.horizontalLines.get(this.tmpI));
                i = this.tmpI + 1;
            } else {
                drawBottomSignature(canvas);
                drawPicker(canvas);
                drawSelection(canvas);
                super.onDraw(canvas);
                return;
            }
        }
    }
}
