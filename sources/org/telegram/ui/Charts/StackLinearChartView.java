package org.telegram.ui.Charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import org.telegram.messenger.AndroidUtilities;
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

    /* JADX WARN: Removed duplicated region for block: B:115:0x03d6  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0427  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x042d  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x042f  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0436 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:146:0x047a  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0490  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x0494  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x049e  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x04aa  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0218  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x021e  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x022f  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0238  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x026a  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x026d  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x027a A[ADDED_TO_REGION] */
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
        int i2;
        float f6;
        float f7;
        int i3;
        float f8;
        float measuredHeight;
        float f9;
        long[] jArr;
        float f10;
        int i4;
        float f11;
        float f12;
        float f13;
        int i5;
        int i6;
        float f14;
        int i7;
        double degrees;
        int quarterForPoint;
        int quarterForPoint2;
        boolean z;
        float f15;
        float f16;
        float f17;
        double degrees2;
        float f18;
        float f19;
        if (this.chartData != 0) {
            float f20 = this.chartWidth;
            ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
            float f21 = chartPickerDelegate.pickerEnd;
            float f22 = chartPickerDelegate.pickerStart;
            float f23 = f20 / (f21 - f22);
            float f24 = (f22 * f23) - BaseChartView.HORIZONTAL_PADDING;
            float centerX = this.chartArea.centerX();
            float centerY = this.chartArea.centerY() + AndroidUtilities.dp(16.0f);
            int i8 = 0;
            for (int i9 = 0; i9 < this.lines.size(); i9++) {
                ((StackLinearViewData) this.lines.get(i9)).chartPath.reset();
                ((StackLinearViewData) this.lines.get(i9)).chartPathPicker.reset();
            }
            canvas.save();
            boolean[] zArr = this.skipPoints;
            if (zArr == null || zArr.length < ((StackLinearChartData) this.chartData).lines.size()) {
                this.skipPoints = new boolean[((StackLinearChartData) this.chartData).lines.size()];
                this.startFromY = new float[((StackLinearChartData) this.chartData).lines.size()];
            }
            int i10 = this.transitionMode;
            if (i10 == 2) {
                float f25 = this.transitionParams.progress / 0.6f;
                if (f25 > 1.0f) {
                    f25 = 1.0f;
                }
                this.ovalPath.reset();
                float width = this.chartArea.width() > this.chartArea.height() ? this.chartArea.width() : this.chartArea.height();
                float height = (this.chartArea.width() > this.chartArea.height() ? this.chartArea.height() : this.chartArea.width()) * 0.45f;
                float f26 = height + (((width - height) / 2.0f) * (1.0f - this.transitionParams.progress));
                RectF rectF = new RectF();
                rectF.set(centerX - f26, centerY - f26, centerX + f26, centerY + f26);
                this.ovalPath.addRoundRect(rectF, f26, f26, Path.Direction.CW);
                canvas.clipPath(this.ovalPath);
                f = f25;
                i = 255;
            } else {
                i = i10 == 3 ? (int) (this.transitionParams.progress * 255.0f) : 255;
                f = 0.0f;
            }
            T t = this.chartData;
            int i11 = ((int) (BaseChartView.HORIZONTAL_PADDING / (((StackLinearChartData) t).xPercentage.length < 2 ? 1.0f : ((StackLinearChartData) t).xPercentage[1] * f23))) + 1;
            int max = Math.max(0, (this.startXIndex - i11) - 1);
            int min = Math.min(((StackLinearChartData) this.chartData).xPercentage.length - 1, this.endXIndex + i11 + 1);
            int i12 = max;
            float f27 = 0.0f;
            float f28 = 0.0f;
            boolean z2 = false;
            while (i12 <= min) {
                int i13 = 0;
                float f29 = 0.0f;
                int i14 = 0;
                while (i8 < this.lines.size()) {
                    LineViewData lineViewData = (LineViewData) this.lines.get(i8);
                    int i15 = i;
                    if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                        f18 = f27;
                        f19 = f;
                        long j = lineViewData.line.y[i12];
                        if (j > 0) {
                            f29 += ((float) j) * lineViewData.alpha;
                            i13++;
                        }
                        i14 = i8;
                    } else {
                        f18 = f27;
                        f19 = f;
                    }
                    i8++;
                    i = i15;
                    f = f19;
                    f27 = f18;
                }
                int i16 = i;
                float f30 = f;
                float f31 = 0.0f;
                int i17 = 0;
                while (i17 < this.lines.size()) {
                    LineViewData lineViewData2 = (LineViewData) this.lines.get(i17);
                    if (lineViewData2.enabled || lineViewData2.alpha != 0.0f) {
                        long[] jArr2 = lineViewData2.line.y;
                        f2 = f27;
                        if (i13 == 1) {
                            if (jArr2[i12] != 0) {
                                f4 = centerY;
                                f3 = centerX;
                                f5 = lineViewData2.alpha;
                                T t2 = this.chartData;
                                i2 = i13;
                                float f32 = (((StackLinearChartData) t2).xPercentage[i12] * f23) - f24;
                                if (i12 != min) {
                                    f6 = getMeasuredWidth();
                                } else {
                                    f6 = (((StackLinearChartData) t2).xPercentage[i12 + 1] * f23) - f24;
                                }
                                if (f5 != 0.0f) {
                                    f7 = f23;
                                    i3 = i14;
                                    if (i17 == i3) {
                                        z2 = true;
                                    }
                                } else {
                                    f7 = f23;
                                    i3 = i14;
                                }
                                f8 = f24;
                                float measuredHeight2 = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f5;
                                float f33 = f28;
                                measuredHeight = ((getMeasuredHeight() - this.chartBottom) - measuredHeight2) - f31;
                                this.startFromY[i17] = measuredHeight;
                                float measuredHeight3 = getMeasuredHeight() - this.chartBottom;
                                if (i12 != min) {
                                    f2 = f32;
                                } else if (i12 == max) {
                                    f33 = f32;
                                }
                                float f34 = f31;
                                if (this.transitionMode == 2 || i17 == i3) {
                                    f9 = f5;
                                    jArr = jArr2;
                                    f10 = f4;
                                    i4 = min;
                                    f11 = measuredHeight;
                                    f12 = 0.0f;
                                } else {
                                    if (f32 < f3) {
                                        TransitionParams transitionParams = this.transitionParams;
                                        f16 = transitionParams.startX[i17];
                                        f17 = transitionParams.startY[i17];
                                    } else {
                                        TransitionParams transitionParams2 = this.transitionParams;
                                        f16 = transitionParams2.endX[i17];
                                        f17 = transitionParams2.endY[i17];
                                    }
                                    float f35 = f3 - f16;
                                    float f36 = f4 - f17;
                                    float f37 = 1.0f - f30;
                                    float f38 = ((((f32 - f16) * f36) / f35) + f17) * f30;
                                    float f39 = (measuredHeight * f37) + f38;
                                    float f40 = (measuredHeight3 * f37) + f38;
                                    float f41 = f36 / f35;
                                    if (f41 > 0.0f) {
                                        f9 = f5;
                                        degrees2 = Math.toDegrees(-Math.atan(f41));
                                    } else {
                                        f9 = f5;
                                        degrees2 = Math.toDegrees(Math.atan(Math.abs(f41)));
                                    }
                                    float f42 = ((float) degrees2) - 90.0f;
                                    if (f32 >= f3) {
                                        float[] fArr = this.mapPoints;
                                        fArr[0] = f32;
                                        fArr[1] = f39;
                                        this.matrix.reset();
                                        float f43 = f4;
                                        this.matrix.postRotate(this.transitionParams.progress * f42, f3, f43);
                                        this.matrix.mapPoints(this.mapPoints);
                                        float[] fArr2 = this.mapPoints;
                                        float f44 = fArr2[0];
                                        float f45 = fArr2[1];
                                        float f46 = f44 < f3 ? f3 : f44;
                                        fArr2[0] = f32;
                                        fArr2[1] = f40;
                                        this.matrix.reset();
                                        this.matrix.postRotate(this.transitionParams.progress * f42, f3, f43);
                                        this.matrix.mapPoints(this.mapPoints);
                                        measuredHeight3 = this.mapPoints[1];
                                        f12 = f42;
                                        if (f32 < f3) {
                                            f32 = f3;
                                        }
                                        f10 = f43;
                                        f11 = f45;
                                        f13 = f46;
                                        i4 = min;
                                        jArr = jArr2;
                                    } else {
                                        f10 = f4;
                                        if (f6 >= f3) {
                                            f32 = (f32 * f37) + (f3 * f30);
                                            f12 = f42;
                                            i4 = min;
                                            jArr = jArr2;
                                            measuredHeight3 = (f39 * f37) + (f10 * f30);
                                            f11 = measuredHeight3;
                                        } else {
                                            i4 = min;
                                            float[] fArr3 = this.mapPoints;
                                            fArr3[0] = f32;
                                            fArr3[1] = f39;
                                            this.matrix.reset();
                                            Matrix matrix = this.matrix;
                                            TransitionParams transitionParams3 = this.transitionParams;
                                            jArr = jArr2;
                                            float f47 = transitionParams3.progress;
                                            matrix.postRotate((f47 * f42) + (f47 * transitionParams3.angle[i17]), f3, f10);
                                            this.matrix.mapPoints(this.mapPoints);
                                            float[] fArr4 = this.mapPoints;
                                            f13 = fArr4[0];
                                            float f48 = fArr4[1];
                                            if (f6 >= f3) {
                                                float f49 = this.transitionParams.progress;
                                                fArr4[0] = (f32 * (1.0f - f49)) + (f49 * f3);
                                            } else {
                                                fArr4[0] = f32;
                                            }
                                            fArr4[1] = f40;
                                            this.matrix.reset();
                                            Matrix matrix2 = this.matrix;
                                            TransitionParams transitionParams4 = this.transitionParams;
                                            float f50 = transitionParams4.progress;
                                            matrix2.postRotate((f50 * f42) + (f50 * transitionParams4.angle[i17]), f3, f10);
                                            this.matrix.mapPoints(this.mapPoints);
                                            float[] fArr5 = this.mapPoints;
                                            f32 = fArr5[0];
                                            measuredHeight3 = fArr5[1];
                                            f12 = f42;
                                            f11 = f48;
                                        }
                                    }
                                    if (i12 == max) {
                                        float measuredHeight4 = getMeasuredHeight();
                                        i5 = max;
                                        if (this.transitionMode != 2 || i17 == i3) {
                                            z = false;
                                            f15 = 0.0f;
                                        } else {
                                            float[] fArr6 = this.mapPoints;
                                            fArr6[0] = 0.0f - f3;
                                            fArr6[1] = measuredHeight4;
                                            this.matrix.reset();
                                            Matrix matrix3 = this.matrix;
                                            TransitionParams transitionParams5 = this.transitionParams;
                                            float f51 = transitionParams5.progress;
                                            matrix3.postRotate((f12 * f51) + (f51 * transitionParams5.angle[i17]), f3, f10);
                                            this.matrix.mapPoints(this.mapPoints);
                                            float[] fArr7 = this.mapPoints;
                                            z = false;
                                            float f52 = fArr7[0];
                                            measuredHeight4 = fArr7[1];
                                            f15 = f52;
                                        }
                                        lineViewData2.chartPath.moveTo(f15, measuredHeight4);
                                        this.skipPoints[i17] = z;
                                    } else {
                                        i5 = max;
                                    }
                                    TransitionParams transitionParams6 = this.transitionParams;
                                    float f53 = transitionParams6 == null ? 0.0f : transitionParams6.progress;
                                    if (f9 == 0.0f || i12 <= 0 || jArr[i12 - 1] != 0) {
                                        i6 = i4;
                                    } else {
                                        i6 = i4;
                                        if (i12 < i6 && jArr[i12 + 1] == 0 && this.transitionMode != 2) {
                                            if (!this.skipPoints[i17]) {
                                                if (i17 == i3) {
                                                    lineViewData2.chartPath.lineTo(f32, measuredHeight3 * (1.0f - f53));
                                                } else {
                                                    lineViewData2.chartPath.lineTo(f32, measuredHeight3);
                                                }
                                            }
                                            this.skipPoints[i17] = true;
                                            if (i12 == i6) {
                                                float measuredWidth = getMeasuredWidth();
                                                float measuredHeight5 = getMeasuredHeight();
                                                if (this.transitionMode == 2 && i17 != i3) {
                                                    float[] fArr8 = this.mapPoints;
                                                    fArr8[0] = measuredWidth + f3;
                                                    fArr8[1] = measuredHeight5;
                                                    this.matrix.reset();
                                                    Matrix matrix4 = this.matrix;
                                                    TransitionParams transitionParams7 = this.transitionParams;
                                                    matrix4.postRotate(transitionParams7.progress * transitionParams7.angle[i17], f3, f10);
                                                    this.matrix.mapPoints(this.mapPoints);
                                                    float[] fArr9 = this.mapPoints;
                                                    float f54 = fArr9[0];
                                                    float f55 = fArr9[1];
                                                } else {
                                                    lineViewData2.chartPath.lineTo(measuredWidth, measuredHeight5);
                                                }
                                                if (this.transitionMode == 2 && i17 != i3) {
                                                    TransitionParams transitionParams8 = this.transitionParams;
                                                    float f56 = (f10 - transitionParams8.startY[i17]) / (f3 - transitionParams8.startX[i17]);
                                                    if (f56 > 0.0f) {
                                                        degrees = Math.toDegrees(-Math.atan(f56));
                                                    } else {
                                                        degrees = Math.toDegrees(Math.atan(Math.abs(f56)));
                                                    }
                                                    TransitionParams transitionParams9 = this.transitionParams;
                                                    float f57 = transitionParams9.startX[i17];
                                                    float f58 = transitionParams9.startY[i17];
                                                    float[] fArr10 = this.mapPoints;
                                                    fArr10[0] = f57;
                                                    fArr10[1] = f58;
                                                    this.matrix.reset();
                                                    Matrix matrix5 = this.matrix;
                                                    TransitionParams transitionParams10 = this.transitionParams;
                                                    float f59 = transitionParams10.progress;
                                                    matrix5.postRotate(((((float) degrees) - 90.0f) * f59) + (f59 * transitionParams10.angle[i17]), f3, f10);
                                                    this.matrix.mapPoints(this.mapPoints);
                                                    float[] fArr11 = this.mapPoints;
                                                    float f60 = fArr11[0];
                                                    float f61 = fArr11[1];
                                                    i7 = i6;
                                                    if (Math.abs(f13 - f60) < 0.001d && ((f61 < f10 && f11 < f10) || (f61 > f10 && f11 > f10))) {
                                                        quarterForPoint2 = this.transitionParams.angle[i17] == -180.0f ? 0 : 3;
                                                        quarterForPoint = 0;
                                                    } else {
                                                        quarterForPoint = quarterForPoint(f13, f11);
                                                        quarterForPoint2 = quarterForPoint(f60, f61);
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
                                                    f31 = f34 + measuredHeight2;
                                                    f28 = f33;
                                                }
                                            }
                                            i7 = i6;
                                            f31 = f34 + measuredHeight2;
                                            f28 = f33;
                                        }
                                    }
                                    if (!this.skipPoints[i17]) {
                                        f14 = 1.0f;
                                    } else if (i17 == i3) {
                                        f14 = 1.0f;
                                        lineViewData2.chartPath.lineTo(f32, measuredHeight3 * (1.0f - f53));
                                    } else {
                                        f14 = 1.0f;
                                        lineViewData2.chartPath.lineTo(f32, measuredHeight3);
                                    }
                                    if (i17 == i3) {
                                        lineViewData2.chartPath.lineTo(f13, (f14 - f53) * f11);
                                    } else {
                                        lineViewData2.chartPath.lineTo(f13, f11);
                                    }
                                    this.skipPoints[i17] = false;
                                    if (i12 == i6) {
                                    }
                                    i7 = i6;
                                    f31 = f34 + measuredHeight2;
                                    f28 = f33;
                                }
                                f13 = f32;
                                if (i12 == max) {
                                }
                                TransitionParams transitionParams62 = this.transitionParams;
                                if (transitionParams62 == null) {
                                }
                                if (f9 == 0.0f) {
                                }
                                i6 = i4;
                                if (!this.skipPoints[i17]) {
                                }
                                if (i17 == i3) {
                                }
                                this.skipPoints[i17] = false;
                                if (i12 == i6) {
                                }
                                i7 = i6;
                                f31 = f34 + measuredHeight2;
                                f28 = f33;
                            }
                            f3 = centerX;
                            f4 = centerY;
                            f5 = 0.0f;
                            T t22 = this.chartData;
                            i2 = i13;
                            float f322 = (((StackLinearChartData) t22).xPercentage[i12] * f23) - f24;
                            if (i12 != min) {
                            }
                            if (f5 != 0.0f) {
                            }
                            f8 = f24;
                            float measuredHeight22 = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f5;
                            float f332 = f28;
                            measuredHeight = ((getMeasuredHeight() - this.chartBottom) - measuredHeight22) - f31;
                            this.startFromY[i17] = measuredHeight;
                            float measuredHeight32 = getMeasuredHeight() - this.chartBottom;
                            if (i12 != min) {
                            }
                            float f342 = f31;
                            if (this.transitionMode == 2) {
                            }
                            f9 = f5;
                            jArr = jArr2;
                            f10 = f4;
                            i4 = min;
                            f11 = measuredHeight;
                            f12 = 0.0f;
                            f13 = f322;
                            if (i12 == max) {
                            }
                            TransitionParams transitionParams622 = this.transitionParams;
                            if (transitionParams622 == null) {
                            }
                            if (f9 == 0.0f) {
                            }
                            i6 = i4;
                            if (!this.skipPoints[i17]) {
                            }
                            if (i17 == i3) {
                            }
                            this.skipPoints[i17] = false;
                            if (i12 == i6) {
                            }
                            i7 = i6;
                            f31 = f342 + measuredHeight22;
                            f28 = f332;
                        } else {
                            if (f29 != 0.0f) {
                                f3 = centerX;
                                f4 = centerY;
                                f5 = (((float) jArr2[i12]) * lineViewData2.alpha) / f29;
                                T t222 = this.chartData;
                                i2 = i13;
                                float f3222 = (((StackLinearChartData) t222).xPercentage[i12] * f23) - f24;
                                if (i12 != min) {
                                }
                                if (f5 != 0.0f) {
                                }
                                f8 = f24;
                                float measuredHeight222 = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f5;
                                float f3322 = f28;
                                measuredHeight = ((getMeasuredHeight() - this.chartBottom) - measuredHeight222) - f31;
                                this.startFromY[i17] = measuredHeight;
                                float measuredHeight322 = getMeasuredHeight() - this.chartBottom;
                                if (i12 != min) {
                                }
                                float f3422 = f31;
                                if (this.transitionMode == 2) {
                                }
                                f9 = f5;
                                jArr = jArr2;
                                f10 = f4;
                                i4 = min;
                                f11 = measuredHeight;
                                f12 = 0.0f;
                                f13 = f3222;
                                if (i12 == max) {
                                }
                                TransitionParams transitionParams6222 = this.transitionParams;
                                if (transitionParams6222 == null) {
                                }
                                if (f9 == 0.0f) {
                                }
                                i6 = i4;
                                if (!this.skipPoints[i17]) {
                                }
                                if (i17 == i3) {
                                }
                                this.skipPoints[i17] = false;
                                if (i12 == i6) {
                                }
                                i7 = i6;
                                f31 = f3422 + measuredHeight222;
                                f28 = f3322;
                            }
                            f3 = centerX;
                            f4 = centerY;
                            f5 = 0.0f;
                            T t2222 = this.chartData;
                            i2 = i13;
                            float f32222 = (((StackLinearChartData) t2222).xPercentage[i12] * f23) - f24;
                            if (i12 != min) {
                            }
                            if (f5 != 0.0f) {
                            }
                            f8 = f24;
                            float measuredHeight2222 = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f5;
                            float f33222 = f28;
                            measuredHeight = ((getMeasuredHeight() - this.chartBottom) - measuredHeight2222) - f31;
                            this.startFromY[i17] = measuredHeight;
                            float measuredHeight3222 = getMeasuredHeight() - this.chartBottom;
                            if (i12 != min) {
                            }
                            float f34222 = f31;
                            if (this.transitionMode == 2) {
                            }
                            f9 = f5;
                            jArr = jArr2;
                            f10 = f4;
                            i4 = min;
                            f11 = measuredHeight;
                            f12 = 0.0f;
                            f13 = f32222;
                            if (i12 == max) {
                            }
                            TransitionParams transitionParams62222 = this.transitionParams;
                            if (transitionParams62222 == null) {
                            }
                            if (f9 == 0.0f) {
                            }
                            i6 = i4;
                            if (!this.skipPoints[i17]) {
                            }
                            if (i17 == i3) {
                            }
                            this.skipPoints[i17] = false;
                            if (i12 == i6) {
                            }
                            i7 = i6;
                            f31 = f34222 + measuredHeight2222;
                            f28 = f33222;
                        }
                    } else {
                        f7 = f23;
                        f8 = f24;
                        f2 = f27;
                        i2 = i13;
                        i7 = min;
                        i5 = max;
                        i3 = i14;
                        f3 = centerX;
                        f10 = centerY;
                    }
                    i17++;
                    i14 = i3;
                    centerY = f10;
                    centerX = f3;
                    f27 = f2;
                    min = i7;
                    i13 = i2;
                    f23 = f7;
                    f24 = f8;
                    max = i5;
                }
                i12++;
                centerX = centerX;
                i = i16;
                f = f30;
                f27 = f27;
                min = min;
                f24 = f24;
                max = max;
                i8 = 0;
            }
            int i18 = i;
            canvas.save();
            canvas.clipRect(f28, BaseChartView.SIGNATURE_TEXT_HEIGHT, f27, getMeasuredHeight() - this.chartBottom);
            if (z2) {
                canvas.drawColor(Theme.getColor(Theme.key_statisticChartLineEmpty));
            }
            for (int size = this.lines.size() - 1; size >= 0; size--) {
                LineViewData lineViewData3 = (LineViewData) this.lines.get(size);
                lineViewData3.paint.setAlpha(i18);
                canvas.drawPath(lineViewData3.chartPath, lineViewData3.paint);
                lineViewData3.paint.setAlpha(255);
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
        float f;
        float f2;
        int i;
        long j;
        if (this.chartData != 0) {
            int size = this.lines.size();
            for (int i2 = 0; i2 < size; i2++) {
                ((StackLinearViewData) this.lines.get(i2)).chartPathPicker.reset();
            }
            T t = this.chartData;
            int i3 = ((StackLinearChartData) t).simplifiedSize;
            boolean[] zArr = this.skipPoints;
            if (zArr == null || zArr.length < ((StackLinearChartData) t).lines.size()) {
                this.skipPoints = new boolean[((StackLinearChartData) this.chartData).lines.size()];
            }
            int i4 = 0;
            boolean z = false;
            while (true) {
                int i5 = 1;
                if (i4 >= i3) {
                    break;
                }
                float f3 = 0.0f;
                float f4 = 0.0f;
                int i6 = 0;
                int i7 = 0;
                for (int i8 = 0; i8 < this.lines.size(); i8++) {
                    LineViewData lineViewData = (LineViewData) this.lines.get(i8);
                    if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                        T t2 = this.chartData;
                        if (((StackLinearChartData) t2).simplifiedY[i8][i4] > 0) {
                            f4 += ((float) ((StackLinearChartData) t2).simplifiedY[i8][i4]) * lineViewData.alpha;
                            i6++;
                        }
                        i7 = i8;
                    }
                }
                int i9 = i3 - 1;
                float f5 = (i4 / i9) * this.pickerWidth;
                int i10 = 0;
                float f6 = 0.0f;
                while (i10 < this.lines.size()) {
                    LineViewData lineViewData2 = (LineViewData) this.lines.get(i10);
                    if (lineViewData2.enabled || lineViewData2.alpha != f3) {
                        if (i6 == i5) {
                            if (((StackLinearChartData) this.chartData).simplifiedY[i10][i4] != 0) {
                                f = lineViewData2.alpha;
                                f2 = 0.0f;
                            }
                            f2 = 0.0f;
                            f = 0.0f;
                        } else {
                            if (f4 != f3) {
                                f = (((float) ((StackLinearChartData) this.chartData).simplifiedY[i10][i4]) * lineViewData2.alpha) / f4;
                                f2 = 0.0f;
                            }
                            f2 = 0.0f;
                            f = 0.0f;
                        }
                        if (f == f2 && i10 == i7) {
                            z = true;
                        }
                        int i11 = this.pikerHeight;
                        float f7 = f * i11;
                        float f8 = (i11 - f7) - f6;
                        if (i4 == 0) {
                            i = i3;
                            lineViewData2.chartPathPicker.moveTo(0.0f, i11);
                            this.skipPoints[i10] = false;
                        } else {
                            i = i3;
                        }
                        T t3 = this.chartData;
                        j = 0;
                        if (((StackLinearChartData) t3).simplifiedY[i10][i4] == 0 && i4 > 0 && ((StackLinearChartData) t3).simplifiedY[i10][i4 - 1] == 0 && i4 < i9 && ((StackLinearChartData) t3).simplifiedY[i10][i4 + 1] == 0) {
                            if (!this.skipPoints[i10]) {
                                lineViewData2.chartPathPicker.lineTo(f5, this.pikerHeight);
                            }
                            this.skipPoints[i10] = true;
                        } else {
                            if (this.skipPoints[i10]) {
                                lineViewData2.chartPathPicker.lineTo(f5, this.pikerHeight);
                            }
                            lineViewData2.chartPathPicker.lineTo(f5, f8);
                            this.skipPoints[i10] = false;
                        }
                        if (i4 == i9) {
                            lineViewData2.chartPathPicker.lineTo(this.pickerWidth, this.pikerHeight);
                        }
                        f6 += f7;
                    } else {
                        i = i3;
                        j = 0;
                    }
                    i10++;
                    i3 = i;
                    i5 = 1;
                    f3 = 0.0f;
                }
                i4++;
            }
            if (z) {
                canvas.drawColor(Theme.getColor(Theme.key_statisticChartLineEmpty));
            }
            for (int size2 = this.lines.size() - 1; size2 >= 0; size2--) {
                LineViewData lineViewData3 = (LineViewData) this.lines.get(size2);
                canvas.drawPath(lineViewData3.chartPathPicker, lineViewData3.paint);
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

    /* JADX WARN: Removed duplicated region for block: B:46:0x013c  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0147  */
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
        int i = 2;
        int i2 = 1;
        int i3 = ((int) (f6 / (((StackLinearChartData) t).xPercentage.length < 2 ? 1.0f : ((StackLinearChartData) t).xPercentage[1] * f5))) + 1;
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
