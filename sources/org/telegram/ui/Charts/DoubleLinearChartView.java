package org.telegram.ui.Charts;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import java.util.Iterator;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Charts.data.ChartData;
import org.telegram.ui.Charts.data.DoubleLinearChartData;
import org.telegram.ui.Charts.view_data.ChartHorizontalLinesData;
import org.telegram.ui.Charts.view_data.LineViewData;
import org.telegram.ui.Charts.view_data.TransitionParams;

/* loaded from: classes4.dex */
public class DoubleLinearChartView extends BaseChartView {
    public DoubleLinearChartView(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context, resourcesProvider);
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected ChartHorizontalLinesData createHorizontalLinesData(long j, long j2, int i) {
        float f;
        float[] fArr = ((DoubleLinearChartData) this.chartData).linesK;
        if (fArr.length < 2) {
            f = 1.0f;
        } else {
            f = fArr[fArr[0] == 1.0f ? (char) 1 : (char) 0];
        }
        return new ChartHorizontalLinesData(j, j2, this.useMinHeight, f, i, this.signaturePaint, this.signaturePaint2);
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    public LineViewData createLineViewData(ChartData.Line line) {
        return new LineViewData(line, false, this.resourcesProvider);
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected void drawChart(Canvas canvas) {
        float f;
        float f2;
        Paint paint;
        Paint.Cap cap;
        float f3;
        if (this.chartData != null) {
            float f4 = this.chartWidth;
            ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
            float f5 = chartPickerDelegate.pickerEnd;
            float f6 = chartPickerDelegate.pickerStart;
            float f7 = f4 / (f5 - f6);
            float f8 = (f6 * f7) - BaseChartView.HORIZONTAL_PADDING;
            canvas.save();
            int i = this.transitionMode;
            float f9 = 0.0f;
            int i2 = 2;
            int i3 = 1;
            if (i == 2) {
                TransitionParams transitionParams = this.transitionParams;
                float f10 = transitionParams.progress;
                f = f10 > 0.5f ? 0.0f : 1.0f - (f10 * 2.0f);
                canvas.scale((f10 * 2.0f) + 1.0f, 1.0f, transitionParams.pX, transitionParams.pY);
            } else if (i == 1) {
                float f11 = this.transitionParams.progress;
                f = f11 < 0.3f ? 0.0f : f11;
                canvas.save();
                TransitionParams transitionParams2 = this.transitionParams;
                float f12 = transitionParams2.progress;
                canvas.scale(f12, f12, transitionParams2.pX, transitionParams2.pY);
            } else {
                f = i == 3 ? this.transitionParams.progress : 1.0f;
            }
            int i4 = 0;
            int i5 = 0;
            while (i5 < this.lines.size()) {
                LineViewData lineViewData = (LineViewData) this.lines.get(i5);
                if (lineViewData.enabled || lineViewData.alpha != f9) {
                    long[] jArr = lineViewData.line.y;
                    lineViewData.chartPath.reset();
                    float[] fArr = ((DoubleLinearChartData) this.chartData).xPercentage;
                    int i6 = ((int) (BaseChartView.HORIZONTAL_PADDING / (fArr.length < i2 ? 1.0f : fArr[i3] * f7))) + i3;
                    int max = Math.max(i4, this.startXIndex - i6);
                    int min = Math.min(((DoubleLinearChartData) this.chartData).xPercentage.length - i3, this.endXIndex + i6);
                    boolean z = true;
                    int i7 = 0;
                    while (max <= min) {
                        long j = jArr[max];
                        if (j < 0) {
                            f3 = f7;
                        } else {
                            DoubleLinearChartData doubleLinearChartData = (DoubleLinearChartData) this.chartData;
                            float f13 = (doubleLinearChartData.xPercentage[max] * f7) - f8;
                            float f14 = ((float) j) * doubleLinearChartData.linesK[i5];
                            float f15 = this.currentMinHeight;
                            float f16 = (f14 - f15) / (this.currentMaxHeight - f15);
                            float strokeWidth = lineViewData.paint.getStrokeWidth() / 2.0f;
                            f3 = f7;
                            float measuredHeight = ((getMeasuredHeight() - this.chartBottom) - strokeWidth) - (f16 * (((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) - strokeWidth));
                            if (BaseChartView.USE_LINES) {
                                float[] fArr2 = lineViewData.linesPath;
                                int i8 = i7 + 1;
                                if (i7 == 0) {
                                    fArr2[i7] = f13;
                                    i7 += 2;
                                    fArr2[i8] = measuredHeight;
                                } else {
                                    fArr2[i7] = f13;
                                    fArr2[i8] = measuredHeight;
                                    int i9 = i7 + 3;
                                    fArr2[i7 + 2] = f13;
                                    i7 += 4;
                                    fArr2[i9] = measuredHeight;
                                }
                            } else {
                                Path path = lineViewData.chartPath;
                                if (z) {
                                    path.moveTo(f13, measuredHeight);
                                    z = false;
                                } else {
                                    path.lineTo(f13, measuredHeight);
                                }
                            }
                        }
                        max++;
                        f7 = f3;
                    }
                    f2 = f7;
                    if (this.endXIndex - this.startXIndex > 100) {
                        paint = lineViewData.paint;
                        cap = Paint.Cap.SQUARE;
                    } else {
                        paint = lineViewData.paint;
                        cap = Paint.Cap.ROUND;
                    }
                    paint.setStrokeCap(cap);
                    lineViewData.paint.setAlpha((int) (lineViewData.alpha * 255.0f * f));
                    if (BaseChartView.USE_LINES) {
                        canvas.drawLines(lineViewData.linesPath, 0, i7, lineViewData.paint);
                        i5++;
                        f7 = f2;
                        i4 = 0;
                        f9 = 0.0f;
                        i2 = 2;
                        i3 = 1;
                    } else {
                        canvas.drawPath(lineViewData.chartPath, lineViewData.paint);
                    }
                } else {
                    f2 = f7;
                }
                i5++;
                f7 = f2;
                i4 = 0;
                f9 = 0.0f;
                i2 = 2;
                i3 = 1;
            }
            canvas.restore();
        }
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected void drawPickerChart(Canvas canvas) {
        int i;
        float f;
        int measuredHeight = getMeasuredHeight();
        int i2 = BaseChartView.PICKER_PADDING;
        int i3 = measuredHeight - i2;
        int measuredHeight2 = (getMeasuredHeight() - this.pikerHeight) - i2;
        int size = this.lines.size();
        if (this.chartData != null) {
            for (int i4 = 0; i4 < size; i4++) {
                LineViewData lineViewData = (LineViewData) this.lines.get(i4);
                if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                    lineViewData.bottomLinePath.reset();
                    int length = ((DoubleLinearChartData) this.chartData).xPercentage.length;
                    long[] jArr = lineViewData.line.y;
                    lineViewData.chartPath.reset();
                    int i5 = 0;
                    int i6 = 0;
                    while (i5 < length) {
                        long j = jArr[i5];
                        if (j < 0) {
                            i = length;
                        } else {
                            DoubleLinearChartData doubleLinearChartData = (DoubleLinearChartData) this.chartData;
                            float f2 = doubleLinearChartData.xPercentage[i5] * this.pickerWidth;
                            if (BaseChartView.ANIMATE_PICKER_SIZES) {
                                f = this.pickerMaxHeight;
                                i = length;
                            } else {
                                i = length;
                                f = (float) doubleLinearChartData.maxValue;
                            }
                            float f3 = (1.0f - ((((float) j) * doubleLinearChartData.linesK[i4]) / f)) * (i3 - measuredHeight2);
                            if (BaseChartView.USE_LINES) {
                                float[] fArr = lineViewData.linesPathBottom;
                                int i7 = i6 + 1;
                                if (i6 == 0) {
                                    fArr[i6] = f2;
                                    i6 += 2;
                                    fArr[i7] = f3;
                                } else {
                                    fArr[i6] = f2;
                                    fArr[i7] = f3;
                                    int i8 = i6 + 3;
                                    fArr[i6 + 2] = f2;
                                    i6 += 4;
                                    fArr[i8] = f3;
                                }
                            } else {
                                Path path = lineViewData.bottomLinePath;
                                if (i5 == 0) {
                                    path.moveTo(f2, f3);
                                } else {
                                    path.lineTo(f2, f3);
                                }
                            }
                        }
                        i5++;
                        length = i;
                    }
                    lineViewData.linesPathBottomSize = i6;
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Charts.BaseChartView
    public void drawSelection(Canvas canvas) {
        int i = this.selectedIndex;
        if (i < 0 || !this.legendShowing) {
            return;
        }
        int i2 = (int) (this.chartActiveLineAlpha * this.selectionA);
        float f = this.chartWidth;
        ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
        float f2 = chartPickerDelegate.pickerEnd;
        float f3 = chartPickerDelegate.pickerStart;
        float f4 = f / (f2 - f3);
        float f5 = (((DoubleLinearChartData) this.chartData).xPercentage[i] * f4) - ((f3 * f4) - BaseChartView.HORIZONTAL_PADDING);
        this.selectedLinePaint.setAlpha(i2);
        canvas.drawLine(f5, 0.0f, f5, this.chartArea.bottom, this.selectedLinePaint);
        this.tmpN = this.lines.size();
        int i3 = 0;
        while (true) {
            this.tmpI = i3;
            int i4 = this.tmpI;
            if (i4 >= this.tmpN) {
                return;
            }
            LineViewData lineViewData = (LineViewData) this.lines.get(i4);
            if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                float f6 = ((float) lineViewData.line.y[this.selectedIndex]) * ((DoubleLinearChartData) this.chartData).linesK[this.tmpI];
                float f7 = this.currentMinHeight;
                float measuredHeight = (getMeasuredHeight() - this.chartBottom) - (((f6 - f7) / (this.currentMaxHeight - f7)) * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT));
                lineViewData.selectionPaint.setAlpha((int) (lineViewData.alpha * 255.0f * this.selectionA));
                this.selectionBackgroundPaint.setAlpha((int) (lineViewData.alpha * 255.0f * this.selectionA));
                canvas.drawPoint(f5, measuredHeight, lineViewData.selectionPaint);
                canvas.drawPoint(f5, measuredHeight, this.selectionBackgroundPaint);
            }
            i3 = this.tmpI + 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x004a  */
    @Override // org.telegram.ui.Charts.BaseChartView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void drawSignaturesToHorizontalLines(Canvas canvas, ChartHorizontalLinesData chartHorizontalLinesData) {
        float f;
        int i;
        float f2;
        float f3;
        int i2;
        int i3;
        int i4;
        int i5;
        TextPaint textPaint;
        float f4;
        float f5;
        long[] jArr = chartHorizontalLinesData.values;
        int length = jArr.length;
        int i6 = ((DoubleLinearChartData) this.chartData).linesK[0] == 1.0f ? 1 : 0;
        int i7 = (i6 + 1) % 2;
        if (length > 2) {
            float f6 = ((float) (jArr[1] - jArr[0])) / (this.currentMaxHeight - this.currentMinHeight);
            if (f6 < 0.1d) {
                f = f6 / 0.1f;
                i = this.transitionMode;
                if (i != 2) {
                    f2 = 1.0f - this.transitionParams.progress;
                } else {
                    if (i != 1 && i != 3) {
                        f3 = 1.0f;
                        this.linePaint.setAlpha((int) (chartHorizontalLinesData.alpha * 0.1f * f3));
                        int measuredHeight = getMeasuredHeight() - this.chartBottom;
                        int i8 = BaseChartView.SIGNATURE_TEXT_HEIGHT;
                        int i9 = measuredHeight - i8;
                        int textSize = (int) (i8 - this.signaturePaint.getTextSize());
                        i2 = 0;
                        while (i2 < length) {
                            float measuredHeight2 = getMeasuredHeight() - this.chartBottom;
                            int i10 = i6;
                            float f7 = (float) chartHorizontalLinesData.values[i2];
                            float f8 = this.currentMinHeight;
                            int i11 = (int) (measuredHeight2 - (i9 * ((f7 - f8) / (this.currentMaxHeight - f8))));
                            if (chartHorizontalLinesData.valuesStr == null || this.lines.size() <= 0) {
                                i3 = i2;
                                i4 = textSize;
                                i5 = i9;
                            } else {
                                if (chartHorizontalLinesData.valuesStr2 == null || this.lines.size() < 2) {
                                    this.signaturePaint.setColor(Theme.getColor(Theme.key_statisticChartSignature, this.resourcesProvider));
                                    textPaint = this.signaturePaint;
                                    f4 = chartHorizontalLinesData.alpha;
                                    f5 = this.signaturePaintAlpha;
                                } else {
                                    this.signaturePaint.setColor(((LineViewData) this.lines.get(i7)).lineColor);
                                    textPaint = this.signaturePaint;
                                    f4 = chartHorizontalLinesData.alpha;
                                    f5 = ((LineViewData) this.lines.get(i7)).alpha;
                                }
                                textPaint.setAlpha((int) (f4 * f5 * f3 * f));
                                i3 = i2;
                                i4 = textSize;
                                i5 = i9;
                                chartHorizontalLinesData.drawText(canvas, 0, i2, BaseChartView.HORIZONTAL_PADDING, i11 - textSize, this.signaturePaint);
                            }
                            if (chartHorizontalLinesData.valuesStr2 != null && this.lines.size() > 1) {
                                this.signaturePaint2.setColor(((LineViewData) this.lines.get(i10)).lineColor);
                                this.signaturePaint2.setAlpha((int) (chartHorizontalLinesData.alpha * ((LineViewData) this.lines.get(i10)).alpha * f3 * f));
                                chartHorizontalLinesData.drawText(canvas, 1, i3, getMeasuredWidth() - BaseChartView.HORIZONTAL_PADDING, i11 - i4, this.signaturePaint2);
                            }
                            i2 = i3 + 1;
                            textSize = i4;
                            i6 = i10;
                            i9 = i5;
                        }
                    }
                    f2 = this.transitionParams.progress;
                }
                f3 = f2;
                this.linePaint.setAlpha((int) (chartHorizontalLinesData.alpha * 0.1f * f3));
                int measuredHeight3 = getMeasuredHeight() - this.chartBottom;
                int i82 = BaseChartView.SIGNATURE_TEXT_HEIGHT;
                int i92 = measuredHeight3 - i82;
                int textSize2 = (int) (i82 - this.signaturePaint.getTextSize());
                i2 = 0;
                while (i2 < length) {
                }
            }
        }
        f = 1.0f;
        i = this.transitionMode;
        if (i != 2) {
        }
        f3 = f2;
        this.linePaint.setAlpha((int) (chartHorizontalLinesData.alpha * 0.1f * f3));
        int measuredHeight32 = getMeasuredHeight() - this.chartBottom;
        int i822 = BaseChartView.SIGNATURE_TEXT_HEIGHT;
        int i922 = measuredHeight32 - i822;
        int textSize22 = (int) (i822 - this.signaturePaint.getTextSize());
        i2 = 0;
        while (i2 < length) {
        }
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    public long findMaxValue(int i, int i2) {
        if (this.lines.isEmpty()) {
            return 0L;
        }
        int size = this.lines.size();
        long j = 0;
        for (int i3 = 0; i3 < size; i3++) {
            long rMaxQ = ((LineViewData) this.lines.get(i3)).enabled ? ((float) ((ChartData.Line) ((DoubleLinearChartData) this.chartData).lines.get(i3)).segmentTree.rMaxQ(i, i2)) * ((DoubleLinearChartData) this.chartData).linesK[i3] : 0L;
            if (rMaxQ > j) {
                j = rMaxQ;
            }
        }
        return j;
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    public long findMinValue(int i, int i2) {
        if (this.lines.isEmpty()) {
            return 0L;
        }
        int size = this.lines.size();
        long j = Long.MAX_VALUE;
        for (int i3 = 0; i3 < size; i3++) {
            long rMinQ = ((LineViewData) this.lines.get(i3)).enabled ? (int) (((float) ((ChartData.Line) ((DoubleLinearChartData) this.chartData).lines.get(i3)).segmentTree.rMinQ(i, i2)) * ((DoubleLinearChartData) this.chartData).linesK[i3]) : 2147483647L;
            if (rMinQ < j) {
                j = rMinQ;
            }
        }
        return j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Charts.BaseChartView
    public void init() {
        this.useMinHeight = true;
        super.init();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Charts.BaseChartView
    public void updatePickerMinMaxHeight() {
        if (BaseChartView.ANIMATE_PICKER_SIZES) {
            if (((LineViewData) this.lines.get(0)).enabled) {
                super.updatePickerMinMaxHeight();
                return;
            }
            Iterator it = this.lines.iterator();
            long j = 0;
            while (it.hasNext()) {
                LineViewData lineViewData = (LineViewData) it.next();
                if (lineViewData.enabled) {
                    long j2 = lineViewData.line.maxValue;
                    if (j2 > j) {
                        j = j2;
                    }
                }
            }
            if (this.lines.size() > 1) {
                j = ((float) j) * ((DoubleLinearChartData) this.chartData).linesK[1];
            }
            if (j > 0) {
                float f = (float) j;
                if (f != this.animatedToPickerMaxHeight) {
                    this.animatedToPickerMaxHeight = f;
                    Animator animator = this.pickerAnimator;
                    if (animator != null) {
                        animator.cancel();
                    }
                    ValueAnimator createAnimator = createAnimator(this.pickerMaxHeight, this.animatedToPickerMaxHeight, new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Charts.DoubleLinearChartView.1
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            DoubleLinearChartView.this.pickerMaxHeight = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                            DoubleLinearChartView doubleLinearChartView = DoubleLinearChartView.this;
                            doubleLinearChartView.invalidatePickerChart = true;
                            doubleLinearChartView.invalidate();
                        }
                    });
                    this.pickerAnimator = createAnimator;
                    createAnimator.start();
                }
            }
        }
    }
}
