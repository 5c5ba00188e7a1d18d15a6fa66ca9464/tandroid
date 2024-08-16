package org.telegram.ui.Charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Charts.data.ChartData;
import org.telegram.ui.Charts.data.StackLinearChartData;
import org.telegram.ui.Charts.view_data.LineViewData;
import org.telegram.ui.Charts.view_data.StackLinearViewData;
import org.telegram.ui.Charts.view_data.TransitionParams;
/* loaded from: classes4.dex */
public class StackLinearChartView<T extends StackLinearViewData> extends BaseChartView<StackLinearChartData, T> {
    private float[] mapPoints;
    private Matrix matrix;
    Path ovalPath;
    boolean[] skipPoints;
    float[] startFromY;

    @Override // org.telegram.ui.Charts.BaseChartView
    public long findMaxValue(int i, int i2) {
        return 100L;
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected float getMinDistance() {
        return 0.1f;
    }

    public StackLinearChartView(Context context) {
        super(context);
        this.matrix = new Matrix();
        this.mapPoints = new float[2];
        this.ovalPath = new Path();
        this.superDraw = true;
        this.useAlphaSignature = true;
        this.drawPointOnSelection = false;
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    public T createLineViewData(ChartData.Line line) {
        return (T) new StackLinearViewData(line);
    }

    /* JADX WARN: Removed duplicated region for block: B:117:0x03d0  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0423  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x042b  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x042e  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0435 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0477  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x048d  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0491  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x049d  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x04ab  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0214  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x021c  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0228  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0230  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0262  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0265  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0272 A[ADDED_TO_REGION] */
    @Override // org.telegram.ui.Charts.BaseChartView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void drawChart(Canvas canvas) {
        int i;
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        int i2;
        float f9;
        int i3;
        boolean z;
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        float f16;
        int i4;
        float f17;
        float f18;
        float f19;
        double degrees;
        int quarterForPoint;
        int quarterForPoint2;
        boolean z2;
        float f20;
        float f21;
        float f22;
        double degrees2;
        float f23;
        float f24;
        if (this.chartData != 0) {
            float f25 = this.chartWidth;
            ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
            float f26 = chartPickerDelegate.pickerEnd;
            float f27 = chartPickerDelegate.pickerStart;
            float f28 = f25 / (f26 - f27);
            float f29 = (f27 * f28) - BaseChartView.HORIZONTAL_PADDING;
            float centerX = this.chartArea.centerX();
            float centerY = this.chartArea.centerY() + AndroidUtilities.dp(16.0f);
            int i5 = 0;
            for (int i6 = 0; i6 < this.lines.size(); i6++) {
                ((StackLinearViewData) this.lines.get(i6)).chartPath.reset();
                ((StackLinearViewData) this.lines.get(i6)).chartPathPicker.reset();
            }
            canvas.save();
            boolean[] zArr = this.skipPoints;
            if (zArr == null || zArr.length < ((StackLinearChartData) this.chartData).lines.size()) {
                this.skipPoints = new boolean[((StackLinearChartData) this.chartData).lines.size()];
                this.startFromY = new float[((StackLinearChartData) this.chartData).lines.size()];
            }
            int i7 = this.transitionMode;
            if (i7 == 2) {
                float f30 = this.transitionParams.progress / 0.6f;
                if (f30 > 1.0f) {
                    f30 = 1.0f;
                }
                this.ovalPath.reset();
                float width = this.chartArea.width() > this.chartArea.height() ? this.chartArea.width() : this.chartArea.height();
                float height = (this.chartArea.width() > this.chartArea.height() ? this.chartArea.height() : this.chartArea.width()) * 0.45f;
                float f31 = height + (((width - height) / 2.0f) * (1.0f - this.transitionParams.progress));
                RectF rectF = new RectF();
                rectF.set(centerX - f31, centerY - f31, centerX + f31, centerY + f31);
                this.ovalPath.addRoundRect(rectF, f31, f31, Path.Direction.CW);
                canvas.clipPath(this.ovalPath);
                f = f30;
                i = NotificationCenter.voipServiceCreated;
            } else {
                i = i7 == 3 ? (int) (this.transitionParams.progress * 255.0f) : NotificationCenter.voipServiceCreated;
                f = 0.0f;
            }
            float[] fArr = ((StackLinearChartData) this.chartData).xPercentage;
            int i8 = ((int) (BaseChartView.HORIZONTAL_PADDING / (fArr.length < 2 ? 1.0f : fArr[1] * f28))) + 1;
            int max = Math.max(0, (this.startXIndex - i8) - 1);
            int min = Math.min(((StackLinearChartData) this.chartData).xPercentage.length - 1, this.endXIndex + i8 + 1);
            int i9 = max;
            float f32 = 0.0f;
            boolean z3 = false;
            float f33 = 0.0f;
            while (i9 <= min) {
                int i10 = 0;
                float f34 = 0.0f;
                int i11 = 0;
                while (i5 < this.lines.size()) {
                    LineViewData lineViewData = (LineViewData) this.lines.get(i5);
                    int i12 = i;
                    if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                        f23 = f32;
                        f24 = f;
                        long j = lineViewData.line.y[i9];
                        if (j > 0) {
                            f34 += ((float) j) * lineViewData.alpha;
                            i10++;
                        }
                        i11 = i5;
                    } else {
                        f23 = f32;
                        f24 = f;
                    }
                    i5++;
                    i = i12;
                    f = f24;
                    f32 = f23;
                }
                int i13 = i;
                float f35 = f;
                float f36 = 0.0f;
                int i14 = 0;
                while (i14 < this.lines.size()) {
                    LineViewData lineViewData2 = (LineViewData) this.lines.get(i14);
                    if (lineViewData2.enabled || lineViewData2.alpha != 0.0f) {
                        long[] jArr = lineViewData2.line.y;
                        f2 = f32;
                        if (i10 == 1) {
                            if (jArr[i9] != 0) {
                                f4 = centerY;
                                f3 = centerX;
                                f5 = lineViewData2.alpha;
                                float[] fArr2 = ((StackLinearChartData) this.chartData).xPercentage;
                                f6 = (fArr2[i9] * f28) - f29;
                                if (i9 != min) {
                                    f7 = getMeasuredWidth();
                                } else {
                                    f7 = (fArr2[i9 + 1] * f28) - f29;
                                }
                                if (f5 != 0.0f) {
                                    f8 = f28;
                                    i2 = i11;
                                    if (i14 == i2) {
                                        z3 = true;
                                    }
                                } else {
                                    f8 = f28;
                                    i2 = i11;
                                }
                                f9 = f29;
                                float measuredHeight = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f5;
                                i3 = i10;
                                float measuredHeight2 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight) - f36;
                                z = z3;
                                this.startFromY[i14] = measuredHeight2;
                                float measuredHeight3 = getMeasuredHeight() - this.chartBottom;
                                if (i9 != min) {
                                    f33 = f6;
                                } else if (i9 == max) {
                                    f2 = f6;
                                }
                                if (this.transitionMode == 2 || i14 == i2) {
                                    f10 = f5;
                                    f11 = f4;
                                    f12 = f36;
                                    f13 = f6;
                                    f14 = f13;
                                    f15 = 0.0f;
                                } else {
                                    if (f6 < f3) {
                                        TransitionParams transitionParams = this.transitionParams;
                                        f21 = transitionParams.startX[i14];
                                        f22 = transitionParams.startY[i14];
                                    } else {
                                        TransitionParams transitionParams2 = this.transitionParams;
                                        f21 = transitionParams2.endX[i14];
                                        f22 = transitionParams2.endY[i14];
                                    }
                                    float f37 = f3 - f21;
                                    float f38 = f4 - f22;
                                    float f39 = 1.0f - f35;
                                    float f40 = ((((f6 - f21) * f38) / f37) + f22) * f35;
                                    float f41 = (measuredHeight2 * f39) + f40;
                                    float f42 = (measuredHeight3 * f39) + f40;
                                    float f43 = f38 / f37;
                                    if (f43 > 0.0f) {
                                        f10 = f5;
                                        degrees2 = Math.toDegrees(-Math.atan(f43));
                                    } else {
                                        f10 = f5;
                                        degrees2 = Math.toDegrees(Math.atan(Math.abs(f43)));
                                    }
                                    f15 = ((float) degrees2) - 90.0f;
                                    if (f6 >= f3) {
                                        float[] fArr3 = this.mapPoints;
                                        fArr3[0] = f6;
                                        fArr3[1] = f41;
                                        this.matrix.reset();
                                        f11 = f4;
                                        this.matrix.postRotate(this.transitionParams.progress * f15, f3, f11);
                                        this.matrix.mapPoints(this.mapPoints);
                                        float[] fArr4 = this.mapPoints;
                                        float f44 = fArr4[0];
                                        float f45 = fArr4[1];
                                        if (f44 < f3) {
                                            f44 = f3;
                                        }
                                        fArr4[0] = f6;
                                        fArr4[1] = f42;
                                        this.matrix.reset();
                                        this.matrix.postRotate(this.transitionParams.progress * f15, f3, f11);
                                        this.matrix.mapPoints(this.mapPoints);
                                        measuredHeight3 = this.mapPoints[1];
                                        if (f6 < f3) {
                                            f14 = f3;
                                            f13 = f44;
                                            measuredHeight2 = f45;
                                        } else {
                                            f13 = f44;
                                            measuredHeight2 = f45;
                                            f14 = f6;
                                        }
                                        f12 = f36;
                                    } else {
                                        f11 = f4;
                                        if (f7 >= f3) {
                                            measuredHeight2 = (f41 * f39) + (f11 * f35);
                                            f12 = f36;
                                            measuredHeight3 = measuredHeight2;
                                            f13 = (f6 * f39) + (f3 * f35);
                                            f14 = f13;
                                        } else {
                                            float[] fArr5 = this.mapPoints;
                                            fArr5[0] = f6;
                                            fArr5[1] = f41;
                                            this.matrix.reset();
                                            Matrix matrix = this.matrix;
                                            TransitionParams transitionParams3 = this.transitionParams;
                                            f12 = f36;
                                            float f46 = transitionParams3.progress;
                                            matrix.postRotate((f46 * f15) + (f46 * transitionParams3.angle[i14]), f3, f11);
                                            this.matrix.mapPoints(this.mapPoints);
                                            float[] fArr6 = this.mapPoints;
                                            float f47 = fArr6[0];
                                            float f48 = fArr6[1];
                                            if (f7 >= f3) {
                                                float f49 = this.transitionParams.progress;
                                                fArr6[0] = (f6 * (1.0f - f49)) + (f49 * f3);
                                            } else {
                                                fArr6[0] = f6;
                                            }
                                            fArr6[1] = f42;
                                            this.matrix.reset();
                                            Matrix matrix2 = this.matrix;
                                            TransitionParams transitionParams4 = this.transitionParams;
                                            float f50 = transitionParams4.progress;
                                            matrix2.postRotate((f50 * f15) + (f50 * transitionParams4.angle[i14]), f3, f11);
                                            this.matrix.mapPoints(this.mapPoints);
                                            float[] fArr7 = this.mapPoints;
                                            float f51 = fArr7[0];
                                            measuredHeight3 = fArr7[1];
                                            f13 = f47;
                                            f14 = f51;
                                            measuredHeight2 = f48;
                                        }
                                    }
                                }
                                if (i9 != max) {
                                    float measuredHeight4 = getMeasuredHeight();
                                    i4 = max;
                                    f16 = f13;
                                    if (this.transitionMode != 2 || i14 == i2) {
                                        z2 = false;
                                        f20 = 0.0f;
                                    } else {
                                        float[] fArr8 = this.mapPoints;
                                        fArr8[0] = 0.0f - f3;
                                        fArr8[1] = measuredHeight4;
                                        this.matrix.reset();
                                        Matrix matrix3 = this.matrix;
                                        TransitionParams transitionParams5 = this.transitionParams;
                                        float f52 = transitionParams5.progress;
                                        matrix3.postRotate((f15 * f52) + (f52 * transitionParams5.angle[i14]), f3, f11);
                                        this.matrix.mapPoints(this.mapPoints);
                                        float[] fArr9 = this.mapPoints;
                                        z2 = false;
                                        float f53 = fArr9[0];
                                        measuredHeight4 = fArr9[1];
                                        f20 = f53;
                                    }
                                    lineViewData2.chartPath.moveTo(f20, measuredHeight4);
                                    this.skipPoints[i14] = z2;
                                } else {
                                    f16 = f13;
                                    i4 = max;
                                }
                                TransitionParams transitionParams6 = this.transitionParams;
                                float f54 = transitionParams6 != null ? 0.0f : transitionParams6.progress;
                                if (f10 != 0.0f && i9 > 0 && jArr[i9 - 1] == 0 && i9 < min && jArr[i9 + 1] == 0 && this.transitionMode != 2) {
                                    if (!this.skipPoints[i14]) {
                                        if (i14 == i2) {
                                            lineViewData2.chartPath.lineTo(f14, measuredHeight3 * (1.0f - f54));
                                        } else {
                                            lineViewData2.chartPath.lineTo(f14, measuredHeight3);
                                        }
                                    }
                                    this.skipPoints[i14] = true;
                                    f18 = f16;
                                } else {
                                    if (this.skipPoints[i14]) {
                                        f17 = 1.0f;
                                    } else if (i14 == i2) {
                                        f17 = 1.0f;
                                        lineViewData2.chartPath.lineTo(f14, measuredHeight3 * (1.0f - f54));
                                    } else {
                                        f17 = 1.0f;
                                        lineViewData2.chartPath.lineTo(f14, measuredHeight3);
                                    }
                                    if (i14 != i2) {
                                        f18 = f16;
                                        lineViewData2.chartPath.lineTo(f18, (f17 - f54) * measuredHeight2);
                                    } else {
                                        f18 = f16;
                                        lineViewData2.chartPath.lineTo(f18, measuredHeight2);
                                    }
                                    this.skipPoints[i14] = false;
                                }
                                if (i9 == min) {
                                    float measuredWidth = getMeasuredWidth();
                                    float measuredHeight5 = getMeasuredHeight();
                                    if (this.transitionMode == 2 && i14 != i2) {
                                        float[] fArr10 = this.mapPoints;
                                        fArr10[0] = measuredWidth + f3;
                                        fArr10[1] = measuredHeight5;
                                        this.matrix.reset();
                                        Matrix matrix4 = this.matrix;
                                        TransitionParams transitionParams7 = this.transitionParams;
                                        matrix4.postRotate(transitionParams7.progress * transitionParams7.angle[i14], f3, f11);
                                        this.matrix.mapPoints(this.mapPoints);
                                        float[] fArr11 = this.mapPoints;
                                        float f55 = fArr11[0];
                                        float f56 = fArr11[1];
                                    } else {
                                        lineViewData2.chartPath.lineTo(measuredWidth, measuredHeight5);
                                    }
                                    if (this.transitionMode == 2) {
                                        if (i14 != i2) {
                                            TransitionParams transitionParams8 = this.transitionParams;
                                            float f57 = (f11 - transitionParams8.startY[i14]) / (f3 - transitionParams8.startX[i14]);
                                            if (f57 > 0.0f) {
                                                degrees = Math.toDegrees(-Math.atan(f57));
                                            } else {
                                                degrees = Math.toDegrees(Math.atan(Math.abs(f57)));
                                            }
                                            TransitionParams transitionParams9 = this.transitionParams;
                                            float f58 = transitionParams9.startX[i14];
                                            float f59 = transitionParams9.startY[i14];
                                            float[] fArr12 = this.mapPoints;
                                            fArr12[0] = f58;
                                            fArr12[1] = f59;
                                            this.matrix.reset();
                                            Matrix matrix5 = this.matrix;
                                            TransitionParams transitionParams10 = this.transitionParams;
                                            float f60 = transitionParams10.progress;
                                            matrix5.postRotate(((((float) degrees) - 90.0f) * f60) + (f60 * transitionParams10.angle[i14]), f3, f11);
                                            this.matrix.mapPoints(this.mapPoints);
                                            float[] fArr13 = this.mapPoints;
                                            float f61 = fArr13[0];
                                            float f62 = fArr13[1];
                                            if (Math.abs(f18 - f61) < 0.001d && ((f62 < f11 && measuredHeight2 < f11) || (f62 > f11 && measuredHeight2 > f11))) {
                                                quarterForPoint2 = this.transitionParams.angle[i14] == -180.0f ? 0 : 3;
                                                quarterForPoint = 0;
                                            } else {
                                                quarterForPoint = quarterForPoint(f18, measuredHeight2);
                                                quarterForPoint2 = quarterForPoint(f61, f62);
                                            }
                                            while (quarterForPoint <= quarterForPoint2) {
                                                if (quarterForPoint == 0) {
                                                    lineViewData2.chartPath.lineTo(getMeasuredWidth(), 0.0f);
                                                } else if (quarterForPoint == 1) {
                                                    lineViewData2.chartPath.lineTo(getMeasuredWidth(), getMeasuredHeight());
                                                } else {
                                                    if (quarterForPoint == 2) {
                                                        lineViewData2.chartPath.lineTo(0.0f, getMeasuredHeight());
                                                    } else {
                                                        lineViewData2.chartPath.lineTo(0.0f, 0.0f);
                                                    }
                                                    quarterForPoint++;
                                                }
                                                quarterForPoint++;
                                            }
                                            f19 = f12 + measuredHeight;
                                        }
                                    }
                                    f19 = f12 + measuredHeight;
                                }
                                f19 = f12 + measuredHeight;
                            }
                            f3 = centerX;
                            f4 = centerY;
                            f5 = 0.0f;
                            float[] fArr22 = ((StackLinearChartData) this.chartData).xPercentage;
                            f6 = (fArr22[i9] * f28) - f29;
                            if (i9 != min) {
                            }
                            if (f5 != 0.0f) {
                            }
                            f9 = f29;
                            float measuredHeight6 = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f5;
                            i3 = i10;
                            float measuredHeight22 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight6) - f36;
                            z = z3;
                            this.startFromY[i14] = measuredHeight22;
                            float measuredHeight32 = getMeasuredHeight() - this.chartBottom;
                            if (i9 != min) {
                            }
                            if (this.transitionMode == 2) {
                            }
                            f10 = f5;
                            f11 = f4;
                            f12 = f36;
                            f13 = f6;
                            f14 = f13;
                            f15 = 0.0f;
                            if (i9 != max) {
                            }
                            TransitionParams transitionParams62 = this.transitionParams;
                            if (transitionParams62 != null) {
                            }
                            if (f10 != 0.0f) {
                            }
                            if (this.skipPoints[i14]) {
                            }
                            if (i14 != i2) {
                            }
                            this.skipPoints[i14] = false;
                            if (i9 == min) {
                            }
                            f19 = f12 + measuredHeight6;
                        } else {
                            if (f34 != 0.0f) {
                                f3 = centerX;
                                f4 = centerY;
                                f5 = (((float) jArr[i9]) * lineViewData2.alpha) / f34;
                                float[] fArr222 = ((StackLinearChartData) this.chartData).xPercentage;
                                f6 = (fArr222[i9] * f28) - f29;
                                if (i9 != min) {
                                }
                                if (f5 != 0.0f) {
                                }
                                f9 = f29;
                                float measuredHeight62 = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f5;
                                i3 = i10;
                                float measuredHeight222 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight62) - f36;
                                z = z3;
                                this.startFromY[i14] = measuredHeight222;
                                float measuredHeight322 = getMeasuredHeight() - this.chartBottom;
                                if (i9 != min) {
                                }
                                if (this.transitionMode == 2) {
                                }
                                f10 = f5;
                                f11 = f4;
                                f12 = f36;
                                f13 = f6;
                                f14 = f13;
                                f15 = 0.0f;
                                if (i9 != max) {
                                }
                                TransitionParams transitionParams622 = this.transitionParams;
                                if (transitionParams622 != null) {
                                }
                                if (f10 != 0.0f) {
                                }
                                if (this.skipPoints[i14]) {
                                }
                                if (i14 != i2) {
                                }
                                this.skipPoints[i14] = false;
                                if (i9 == min) {
                                }
                                f19 = f12 + measuredHeight62;
                            }
                            f3 = centerX;
                            f4 = centerY;
                            f5 = 0.0f;
                            float[] fArr2222 = ((StackLinearChartData) this.chartData).xPercentage;
                            f6 = (fArr2222[i9] * f28) - f29;
                            if (i9 != min) {
                            }
                            if (f5 != 0.0f) {
                            }
                            f9 = f29;
                            float measuredHeight622 = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f5;
                            i3 = i10;
                            float measuredHeight2222 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight622) - f36;
                            z = z3;
                            this.startFromY[i14] = measuredHeight2222;
                            float measuredHeight3222 = getMeasuredHeight() - this.chartBottom;
                            if (i9 != min) {
                            }
                            if (this.transitionMode == 2) {
                            }
                            f10 = f5;
                            f11 = f4;
                            f12 = f36;
                            f13 = f6;
                            f14 = f13;
                            f15 = 0.0f;
                            if (i9 != max) {
                            }
                            TransitionParams transitionParams6222 = this.transitionParams;
                            if (transitionParams6222 != null) {
                            }
                            if (f10 != 0.0f) {
                            }
                            if (this.skipPoints[i14]) {
                            }
                            if (i14 != i2) {
                            }
                            this.skipPoints[i14] = false;
                            if (i9 == min) {
                            }
                            f19 = f12 + measuredHeight622;
                        }
                    } else {
                        f8 = f28;
                        f9 = f29;
                        f19 = f36;
                        f2 = f32;
                        i3 = i10;
                        i4 = max;
                        i2 = i11;
                        f3 = centerX;
                        z = z3;
                        f11 = centerY;
                    }
                    i14++;
                    f36 = f19;
                    centerX = f3;
                    centerY = f11;
                    z3 = z;
                    f32 = f2;
                    max = i4;
                    f29 = f9;
                    i10 = i3;
                    i11 = i2;
                    f28 = f8;
                }
                i9++;
                centerY = centerY;
                i = i13;
                f = f35;
                f32 = f32;
                f29 = f29;
                i5 = 0;
            }
            int i15 = i;
            canvas.save();
            canvas.clipRect(f32, BaseChartView.SIGNATURE_TEXT_HEIGHT, f33, getMeasuredHeight() - this.chartBottom);
            if (z3) {
                canvas.drawColor(Theme.getColor(Theme.key_statisticChartLineEmpty));
            }
            for (int size = this.lines.size() - 1; size >= 0; size--) {
                LineViewData lineViewData3 = (LineViewData) this.lines.get(size);
                lineViewData3.paint.setAlpha(i15);
                canvas.drawPath(lineViewData3.chartPath, lineViewData3.paint);
                lineViewData3.paint.setAlpha(NotificationCenter.voipServiceCreated);
            }
            canvas.restore();
            canvas.restore();
        }
    }

    private int quarterForPoint(float f, float f2) {
        float centerX = this.chartArea.centerX();
        float centerY = this.chartArea.centerY() + AndroidUtilities.dp(16.0f);
        if (f < centerX || f2 > centerY) {
            if (f < centerX || f2 < centerY) {
                return (f >= centerX || f2 < centerY) ? 3 : 2;
            }
            return 1;
        }
        return 0;
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected void drawPickerChart(Canvas canvas) {
        long j;
        LineViewData lineViewData;
        float f;
        float f2;
        int i;
        boolean z;
        long j2;
        if (this.chartData != 0) {
            int size = this.lines.size();
            for (int i2 = 0; i2 < size; i2++) {
                ((StackLinearViewData) this.lines.get(i2)).chartPathPicker.reset();
            }
            StackLinearChartData stackLinearChartData = (StackLinearChartData) this.chartData;
            int i3 = stackLinearChartData.simplifiedSize;
            boolean[] zArr = this.skipPoints;
            if (zArr == null || zArr.length < stackLinearChartData.lines.size()) {
                this.skipPoints = new boolean[((StackLinearChartData) this.chartData).lines.size()];
            }
            boolean z2 = false;
            for (int i4 = 0; i4 < i3; i4++) {
                float f3 = 0.0f;
                int i5 = 0;
                float f4 = 0.0f;
                int i6 = 0;
                int i7 = 0;
                while (true) {
                    j = 0;
                    if (i5 >= this.lines.size()) {
                        break;
                    }
                    LineViewData lineViewData2 = (LineViewData) this.lines.get(i5);
                    if (lineViewData2.enabled || lineViewData2.alpha != f3) {
                        long j3 = ((StackLinearChartData) this.chartData).simplifiedY[i5][i4];
                        if (j3 > 0) {
                            f4 += ((float) j3) * lineViewData2.alpha;
                            i6++;
                        }
                        i7 = i5;
                    }
                    i5++;
                    f3 = 0.0f;
                }
                int i8 = i3 - 1;
                float f5 = (i4 / i8) * this.pickerWidth;
                int i9 = 0;
                float f6 = 0.0f;
                while (i9 < this.lines.size()) {
                    LineViewData lineViewData3 = (LineViewData) this.lines.get(i9);
                    if (lineViewData3.enabled || lineViewData3.alpha != 0.0f) {
                        if (i6 == 1) {
                            lineViewData = lineViewData3;
                            if (((StackLinearChartData) this.chartData).simplifiedY[i9][i4] != j) {
                                f = lineViewData.alpha;
                                f2 = 0.0f;
                            }
                            f2 = 0.0f;
                            f = 0.0f;
                        } else {
                            lineViewData = lineViewData3;
                            if (f4 != 0.0f) {
                                f = (((float) ((StackLinearChartData) this.chartData).simplifiedY[i9][i4]) * lineViewData.alpha) / f4;
                                f2 = 0.0f;
                            }
                            f2 = 0.0f;
                            f = 0.0f;
                        }
                        if (f == f2 && i9 == i7) {
                            z2 = true;
                        }
                        float f7 = this.pikerHeight;
                        float f8 = f * f7;
                        float f9 = (f7 - f8) - f6;
                        i = i3;
                        if (i4 == 0) {
                            z = z2;
                            lineViewData.chartPathPicker.moveTo(0.0f, f7);
                            this.skipPoints[i9] = false;
                        } else {
                            z = z2;
                        }
                        long[] jArr = ((StackLinearChartData) this.chartData).simplifiedY[i9];
                        j2 = 0;
                        if (jArr[i4] == 0 && i4 > 0 && jArr[i4 - 1] == 0 && i4 < i8 && jArr[i4 + 1] == 0) {
                            if (!this.skipPoints[i9]) {
                                lineViewData.chartPathPicker.lineTo(f5, this.pikerHeight);
                            }
                            this.skipPoints[i9] = true;
                        } else {
                            if (this.skipPoints[i9]) {
                                lineViewData.chartPathPicker.lineTo(f5, this.pikerHeight);
                            }
                            lineViewData.chartPathPicker.lineTo(f5, f9);
                            this.skipPoints[i9] = false;
                        }
                        if (i4 == i8) {
                            lineViewData.chartPathPicker.lineTo(this.pickerWidth, this.pikerHeight);
                        }
                        f6 += f8;
                        z2 = z;
                    } else {
                        i = i3;
                        j2 = j;
                    }
                    i9++;
                    j = j2;
                    i3 = i;
                }
            }
            if (z2) {
                canvas.drawColor(Theme.getColor(Theme.key_statisticChartLineEmpty));
            }
            for (int size2 = this.lines.size() - 1; size2 >= 0; size2--) {
                LineViewData lineViewData4 = (LineViewData) this.lines.get(size2);
                canvas.drawPath(lineViewData4.chartPathPicker, lineViewData4.paint);
            }
        }
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

    /* JADX WARN: Removed duplicated region for block: B:46:0x0137  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0142  */
    @Override // org.telegram.ui.Charts.BaseChartView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void fillTransitionParams(TransitionParams transitionParams) {
        float f;
        T t = this.chartData;
        if (t == 0) {
            return;
        }
        float f2 = this.chartWidth;
        ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
        float f3 = chartPickerDelegate.pickerEnd;
        float f4 = chartPickerDelegate.pickerStart;
        float f5 = f2 / (f3 - f4);
        float f6 = BaseChartView.HORIZONTAL_PADDING;
        float f7 = (f4 * f5) - f6;
        float[] fArr = ((StackLinearChartData) t).xPercentage;
        int i = 2;
        int i2 = 1;
        int i3 = ((int) (f6 / (fArr.length < 2 ? 1.0f : fArr[1] * f5))) + 1;
        int max = Math.max(0, (this.startXIndex - i3) - 1);
        int min = Math.min(((StackLinearChartData) this.chartData).xPercentage.length - 1, this.endXIndex + i3 + 1);
        this.transitionParams.startX = new float[((StackLinearChartData) this.chartData).lines.size()];
        this.transitionParams.startY = new float[((StackLinearChartData) this.chartData).lines.size()];
        this.transitionParams.endX = new float[((StackLinearChartData) this.chartData).lines.size()];
        this.transitionParams.endY = new float[((StackLinearChartData) this.chartData).lines.size()];
        this.transitionParams.angle = new float[((StackLinearChartData) this.chartData).lines.size()];
        int i4 = 0;
        while (i4 < i) {
            int i5 = i4 == i2 ? min : max;
            float f8 = 0.0f;
            float f9 = 0.0f;
            int i6 = 0;
            for (int i7 = 0; i7 < this.lines.size(); i7++) {
                LineViewData lineViewData = (LineViewData) this.lines.get(i7);
                if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                    long j = lineViewData.line.y[i5];
                    if (j > 0) {
                        f9 += ((float) j) * lineViewData.alpha;
                        i6++;
                    }
                }
            }
            int i8 = 0;
            int i9 = 0;
            while (i8 < this.lines.size()) {
                LineViewData lineViewData2 = (LineViewData) this.lines.get(i8);
                if (lineViewData2.enabled || lineViewData2.alpha != f8) {
                    long[] jArr = lineViewData2.line.y;
                    if (i6 == 1) {
                        if (jArr[i5] != 0) {
                            f = lineViewData2.alpha;
                            float f10 = (((StackLinearChartData) this.chartData).xPercentage[i5] * f5) - f7;
                            float measuredHeight = f * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT);
                            float f11 = i9;
                            float measuredHeight2 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight) - f11;
                            i9 = (int) (f11 + measuredHeight);
                            if (i4 != 0) {
                                TransitionParams transitionParams2 = this.transitionParams;
                                transitionParams2.startX[i8] = f10;
                                transitionParams2.startY[i8] = measuredHeight2;
                            } else {
                                TransitionParams transitionParams3 = this.transitionParams;
                                transitionParams3.endX[i8] = f10;
                                transitionParams3.endY[i8] = measuredHeight2;
                            }
                        }
                        f = 0.0f;
                        float f102 = (((StackLinearChartData) this.chartData).xPercentage[i5] * f5) - f7;
                        float measuredHeight3 = f * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT);
                        float f112 = i9;
                        float measuredHeight22 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight3) - f112;
                        i9 = (int) (f112 + measuredHeight3);
                        if (i4 != 0) {
                        }
                    } else {
                        if (f9 != f8) {
                            f = (((float) jArr[i5]) * lineViewData2.alpha) / f9;
                            float f1022 = (((StackLinearChartData) this.chartData).xPercentage[i5] * f5) - f7;
                            float measuredHeight32 = f * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT);
                            float f1122 = i9;
                            float measuredHeight222 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight32) - f1122;
                            i9 = (int) (f1122 + measuredHeight32);
                            if (i4 != 0) {
                            }
                        }
                        f = 0.0f;
                        float f10222 = (((StackLinearChartData) this.chartData).xPercentage[i5] * f5) - f7;
                        float measuredHeight322 = f * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT);
                        float f11222 = i9;
                        float measuredHeight2222 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight322) - f11222;
                        i9 = (int) (f11222 + measuredHeight322);
                        if (i4 != 0) {
                        }
                    }
                }
                i8++;
                f8 = 0.0f;
            }
            i4++;
            i = 2;
            i2 = 1;
        }
    }
}
