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
import org.telegram.ui.Charts.view_data.ChartHorizontalLinesData;
import org.telegram.ui.Charts.view_data.LineViewData;
import org.telegram.ui.Charts.view_data.StackLinearViewData;
import org.telegram.ui.Charts.view_data.TransitionParams;

/* loaded from: classes4.dex */
public class StackLinearChartView extends BaseChartView {
    private float[] mapPoints;
    private Matrix matrix;
    Path ovalPath;
    boolean[] skipPoints;
    float[] startFromY;

    public StackLinearChartView(Context context) {
        super(context);
        this.matrix = new Matrix();
        this.mapPoints = new float[2];
        this.ovalPath = new Path();
        this.superDraw = true;
        this.useAlphaSignature = true;
        this.drawPointOnSelection = false;
    }

    private int quarterForPoint(float f, float f2) {
        float centerX = this.chartArea.centerX();
        float centerY = this.chartArea.centerY() + AndroidUtilities.dp(16.0f);
        if (f >= centerX && f2 <= centerY) {
            return 0;
        }
        if (f < centerX || f2 < centerY) {
            return (f >= centerX || f2 < centerY) ? 3 : 2;
        }
        return 1;
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    public StackLinearViewData createLineViewData(ChartData.Line line) {
        return new StackLinearViewData(line);
    }

    /* JADX WARN: Removed duplicated region for block: B:103:0x0431 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:118:0x04a4  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x0473  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x048a  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x0496  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0486  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x042a  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x041f  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0265  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0230  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x021c  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0214  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0228  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0262  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0272 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x03cc  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0427  */
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
        int i2;
        float f8;
        int i3;
        boolean z;
        float f9;
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        int i4;
        float f16;
        float f17;
        Path path;
        float f18;
        int quarterForPoint;
        int quarterForPoint2;
        Path path2;
        float measuredWidth;
        float measuredHeight;
        boolean z2;
        float f19;
        float f20;
        float f21;
        double atan;
        float f22;
        float f23;
        if (this.chartData != null) {
            float f24 = this.chartWidth;
            ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
            float f25 = chartPickerDelegate.pickerEnd;
            float f26 = chartPickerDelegate.pickerStart;
            float f27 = f24 / (f25 - f26);
            float f28 = (f26 * f27) - BaseChartView.HORIZONTAL_PADDING;
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
                float f29 = this.transitionParams.progress / 0.6f;
                if (f29 > 1.0f) {
                    f29 = 1.0f;
                }
                this.ovalPath.reset();
                float width = this.chartArea.width() > this.chartArea.height() ? this.chartArea.width() : this.chartArea.height();
                float height = (this.chartArea.width() > this.chartArea.height() ? this.chartArea.height() : this.chartArea.width()) * 0.45f;
                float f30 = height + (((width - height) / 2.0f) * (1.0f - this.transitionParams.progress));
                RectF rectF = new RectF();
                rectF.set(centerX - f30, centerY - f30, centerX + f30, centerY + f30);
                this.ovalPath.addRoundRect(rectF, f30, f30, Path.Direction.CW);
                canvas.clipPath(this.ovalPath);
                f = f29;
                i = NotificationCenter.liveLocationsChanged;
            } else {
                i = i7 == 3 ? (int) (this.transitionParams.progress * 255.0f) : NotificationCenter.liveLocationsChanged;
                f = 0.0f;
            }
            float[] fArr = ((StackLinearChartData) this.chartData).xPercentage;
            int i8 = ((int) (BaseChartView.HORIZONTAL_PADDING / (fArr.length < 2 ? 1.0f : fArr[1] * f27))) + 1;
            int max = Math.max(0, (this.startXIndex - i8) - 1);
            int min = Math.min(((StackLinearChartData) this.chartData).xPercentage.length - 1, this.endXIndex + i8 + 1);
            int i9 = max;
            float f31 = 0.0f;
            boolean z3 = false;
            float f32 = 0.0f;
            while (i9 <= min) {
                int i10 = 0;
                float f33 = 0.0f;
                int i11 = 0;
                while (i5 < this.lines.size()) {
                    LineViewData lineViewData = (LineViewData) this.lines.get(i5);
                    int i12 = i;
                    if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                        f22 = f31;
                        f23 = f;
                        long j = lineViewData.line.y[i9];
                        if (j > 0) {
                            f33 += j * lineViewData.alpha;
                            i10++;
                        }
                        i11 = i5;
                    } else {
                        f22 = f31;
                        f23 = f;
                    }
                    i5++;
                    i = i12;
                    f = f23;
                    f31 = f22;
                }
                int i13 = i;
                float f34 = f;
                float f35 = 0.0f;
                int i14 = 0;
                while (i14 < this.lines.size()) {
                    LineViewData lineViewData2 = (LineViewData) this.lines.get(i14);
                    if (lineViewData2.enabled || lineViewData2.alpha != 0.0f) {
                        long[] jArr = lineViewData2.line.y;
                        f2 = f31;
                        if (i10 == 1) {
                            if (jArr[i9] != 0) {
                                f4 = centerY;
                                f3 = centerX;
                                f5 = lineViewData2.alpha;
                                float[] fArr2 = ((StackLinearChartData) this.chartData).xPercentage;
                                f6 = (fArr2[i9] * f27) - f28;
                                float measuredWidth2 = i9 != min ? getMeasuredWidth() : (fArr2[i9 + 1] * f27) - f28;
                                if (f5 != 0.0f) {
                                    f7 = f27;
                                    i2 = i11;
                                    if (i14 == i2) {
                                        z3 = true;
                                    }
                                } else {
                                    f7 = f27;
                                    i2 = i11;
                                }
                                f8 = f28;
                                float measuredHeight2 = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f5;
                                i3 = i10;
                                float measuredHeight3 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight2) - f35;
                                z = z3;
                                this.startFromY[i14] = measuredHeight3;
                                float measuredHeight4 = getMeasuredHeight() - this.chartBottom;
                                if (i9 != min) {
                                    f32 = f6;
                                } else if (i9 == max) {
                                    f2 = f6;
                                }
                                if (this.transitionMode == 2 || i14 == i2) {
                                    f9 = f5;
                                    f10 = f4;
                                    f11 = f35;
                                    f12 = f6;
                                    f13 = f12;
                                    f14 = 0.0f;
                                } else {
                                    if (f6 < f3) {
                                        TransitionParams transitionParams = this.transitionParams;
                                        f20 = transitionParams.startX[i14];
                                        f21 = transitionParams.startY[i14];
                                    } else {
                                        TransitionParams transitionParams2 = this.transitionParams;
                                        f20 = transitionParams2.endX[i14];
                                        f21 = transitionParams2.endY[i14];
                                    }
                                    float f36 = f3 - f20;
                                    float f37 = f4 - f21;
                                    float f38 = 1.0f - f34;
                                    float f39 = ((((f6 - f20) * f37) / f36) + f21) * f34;
                                    float f40 = (measuredHeight3 * f38) + f39;
                                    float f41 = (measuredHeight4 * f38) + f39;
                                    float f42 = f37 / f36;
                                    if (f42 > 0.0f) {
                                        f9 = f5;
                                        atan = -Math.atan(f42);
                                    } else {
                                        f9 = f5;
                                        atan = Math.atan(Math.abs(f42));
                                    }
                                    f14 = ((float) Math.toDegrees(atan)) - 90.0f;
                                    if (f6 >= f3) {
                                        float[] fArr3 = this.mapPoints;
                                        fArr3[0] = f6;
                                        fArr3[1] = f40;
                                        this.matrix.reset();
                                        f10 = f4;
                                        this.matrix.postRotate(this.transitionParams.progress * f14, f3, f10);
                                        this.matrix.mapPoints(this.mapPoints);
                                        float[] fArr4 = this.mapPoints;
                                        float f43 = fArr4[0];
                                        float f44 = fArr4[1];
                                        if (f43 < f3) {
                                            f43 = f3;
                                        }
                                        fArr4[0] = f6;
                                        fArr4[1] = f41;
                                        this.matrix.reset();
                                        this.matrix.postRotate(this.transitionParams.progress * f14, f3, f10);
                                        this.matrix.mapPoints(this.mapPoints);
                                        measuredHeight4 = this.mapPoints[1];
                                        if (f6 < f3) {
                                            f13 = f3;
                                            f12 = f43;
                                            measuredHeight3 = f44;
                                        } else {
                                            f12 = f43;
                                            measuredHeight3 = f44;
                                            f13 = f6;
                                        }
                                        f11 = f35;
                                    } else {
                                        f10 = f4;
                                        if (measuredWidth2 >= f3) {
                                            measuredHeight3 = (f40 * f38) + (f10 * f34);
                                            f11 = f35;
                                            measuredHeight4 = measuredHeight3;
                                            f12 = (f6 * f38) + (f3 * f34);
                                            f13 = f12;
                                        } else {
                                            float[] fArr5 = this.mapPoints;
                                            fArr5[0] = f6;
                                            fArr5[1] = f40;
                                            this.matrix.reset();
                                            Matrix matrix = this.matrix;
                                            TransitionParams transitionParams3 = this.transitionParams;
                                            f11 = f35;
                                            float f45 = transitionParams3.progress;
                                            matrix.postRotate((f45 * f14) + (f45 * transitionParams3.angle[i14]), f3, f10);
                                            this.matrix.mapPoints(this.mapPoints);
                                            float[] fArr6 = this.mapPoints;
                                            float f46 = fArr6[0];
                                            float f47 = fArr6[1];
                                            if (measuredWidth2 >= f3) {
                                                float f48 = this.transitionParams.progress;
                                                fArr6[0] = (f6 * (1.0f - f48)) + (f48 * f3);
                                            } else {
                                                fArr6[0] = f6;
                                            }
                                            fArr6[1] = f41;
                                            this.matrix.reset();
                                            Matrix matrix2 = this.matrix;
                                            TransitionParams transitionParams4 = this.transitionParams;
                                            float f49 = transitionParams4.progress;
                                            matrix2.postRotate((f49 * f14) + (f49 * transitionParams4.angle[i14]), f3, f10);
                                            this.matrix.mapPoints(this.mapPoints);
                                            float[] fArr7 = this.mapPoints;
                                            float f50 = fArr7[0];
                                            measuredHeight4 = fArr7[1];
                                            f12 = f46;
                                            f13 = f50;
                                            measuredHeight3 = f47;
                                        }
                                    }
                                }
                                if (i9 != max) {
                                    float measuredHeight5 = getMeasuredHeight();
                                    i4 = max;
                                    f15 = f12;
                                    if (this.transitionMode != 2 || i14 == i2) {
                                        z2 = false;
                                        f19 = 0.0f;
                                    } else {
                                        float[] fArr8 = this.mapPoints;
                                        fArr8[0] = 0.0f - f3;
                                        fArr8[1] = measuredHeight5;
                                        this.matrix.reset();
                                        Matrix matrix3 = this.matrix;
                                        TransitionParams transitionParams5 = this.transitionParams;
                                        float f51 = transitionParams5.progress;
                                        matrix3.postRotate((f14 * f51) + (f51 * transitionParams5.angle[i14]), f3, f10);
                                        this.matrix.mapPoints(this.mapPoints);
                                        float[] fArr9 = this.mapPoints;
                                        z2 = false;
                                        float f52 = fArr9[0];
                                        measuredHeight5 = fArr9[1];
                                        f19 = f52;
                                    }
                                    lineViewData2.chartPath.moveTo(f19, measuredHeight5);
                                    this.skipPoints[i14] = z2;
                                } else {
                                    f15 = f12;
                                    i4 = max;
                                }
                                TransitionParams transitionParams6 = this.transitionParams;
                                float f53 = transitionParams6 != null ? 0.0f : transitionParams6.progress;
                                if (f9 == 0.0f || i9 <= 0 || jArr[i9 - 1] != 0 || i9 >= min || jArr[i9 + 1] != 0 || this.transitionMode == 2) {
                                    if (this.skipPoints[i14]) {
                                        f16 = 1.0f;
                                    } else {
                                        if (i14 == i2) {
                                            path = lineViewData2.chartPath;
                                            f16 = 1.0f;
                                            measuredHeight4 *= 1.0f - f53;
                                        } else {
                                            f16 = 1.0f;
                                            path = lineViewData2.chartPath;
                                        }
                                        path.lineTo(f13, measuredHeight4);
                                    }
                                    if (i14 != i2) {
                                        f17 = f15;
                                        lineViewData2.chartPath.lineTo(f17, (f16 - f53) * measuredHeight3);
                                    } else {
                                        f17 = f15;
                                        lineViewData2.chartPath.lineTo(f17, measuredHeight3);
                                    }
                                    this.skipPoints[i14] = false;
                                } else {
                                    if (!this.skipPoints[i14]) {
                                        if (i14 == i2) {
                                            lineViewData2.chartPath.lineTo(f13, measuredHeight4 * (1.0f - f53));
                                        } else {
                                            lineViewData2.chartPath.lineTo(f13, measuredHeight4);
                                        }
                                    }
                                    this.skipPoints[i14] = true;
                                    f17 = f15;
                                }
                                if (i9 == min) {
                                    float measuredWidth3 = getMeasuredWidth();
                                    float measuredHeight6 = getMeasuredHeight();
                                    if (this.transitionMode != 2 || i14 == i2) {
                                        lineViewData2.chartPath.lineTo(measuredWidth3, measuredHeight6);
                                    } else {
                                        float[] fArr10 = this.mapPoints;
                                        fArr10[0] = measuredWidth3 + f3;
                                        fArr10[1] = measuredHeight6;
                                        this.matrix.reset();
                                        Matrix matrix4 = this.matrix;
                                        TransitionParams transitionParams7 = this.transitionParams;
                                        matrix4.postRotate(transitionParams7.progress * transitionParams7.angle[i14], f3, f10);
                                        this.matrix.mapPoints(this.mapPoints);
                                        float[] fArr11 = this.mapPoints;
                                        float f54 = fArr11[0];
                                        float f55 = fArr11[1];
                                    }
                                    if (this.transitionMode == 2) {
                                        if (i14 != i2) {
                                            TransitionParams transitionParams8 = this.transitionParams;
                                            float f56 = (f10 - transitionParams8.startY[i14]) / (f3 - transitionParams8.startX[i14]);
                                            float degrees = ((float) Math.toDegrees(f56 > 0.0f ? -Math.atan(f56) : Math.atan(Math.abs(f56)))) - 90.0f;
                                            TransitionParams transitionParams9 = this.transitionParams;
                                            float f57 = transitionParams9.startX[i14];
                                            float f58 = transitionParams9.startY[i14];
                                            float[] fArr12 = this.mapPoints;
                                            fArr12[0] = f57;
                                            fArr12[1] = f58;
                                            this.matrix.reset();
                                            Matrix matrix5 = this.matrix;
                                            TransitionParams transitionParams10 = this.transitionParams;
                                            float f59 = transitionParams10.progress;
                                            matrix5.postRotate((degrees * f59) + (f59 * transitionParams10.angle[i14]), f3, f10);
                                            this.matrix.mapPoints(this.mapPoints);
                                            float[] fArr13 = this.mapPoints;
                                            float f60 = fArr13[0];
                                            float f61 = fArr13[1];
                                            if (Math.abs(f17 - f60) >= 0.001d || ((f61 >= f10 || measuredHeight3 >= f10) && (f61 <= f10 || measuredHeight3 <= f10))) {
                                                quarterForPoint = quarterForPoint(f17, measuredHeight3);
                                                quarterForPoint2 = quarterForPoint(f60, f61);
                                            } else {
                                                quarterForPoint2 = this.transitionParams.angle[i14] == -180.0f ? 0 : 3;
                                                quarterForPoint = 0;
                                            }
                                            while (quarterForPoint <= quarterForPoint2) {
                                                if (quarterForPoint == 0) {
                                                    path2 = lineViewData2.chartPath;
                                                    measuredWidth = getMeasuredWidth();
                                                    measuredHeight = 0.0f;
                                                } else if (quarterForPoint == 1) {
                                                    path2 = lineViewData2.chartPath;
                                                    measuredWidth = getMeasuredWidth();
                                                    measuredHeight = getMeasuredHeight();
                                                } else {
                                                    if (quarterForPoint == 2) {
                                                        lineViewData2.chartPath.lineTo(0.0f, getMeasuredHeight());
                                                    } else {
                                                        lineViewData2.chartPath.lineTo(0.0f, 0.0f);
                                                    }
                                                    quarterForPoint++;
                                                }
                                                path2.lineTo(measuredWidth, measuredHeight);
                                                quarterForPoint++;
                                            }
                                            f18 = f11 + measuredHeight2;
                                        }
                                    }
                                    f18 = f11 + measuredHeight2;
                                }
                                f18 = f11 + measuredHeight2;
                            }
                            f3 = centerX;
                            f4 = centerY;
                            f5 = 0.0f;
                            float[] fArr22 = ((StackLinearChartData) this.chartData).xPercentage;
                            f6 = (fArr22[i9] * f27) - f28;
                            if (i9 != min) {
                            }
                            if (f5 != 0.0f) {
                            }
                            f8 = f28;
                            float measuredHeight22 = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f5;
                            i3 = i10;
                            float measuredHeight32 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight22) - f35;
                            z = z3;
                            this.startFromY[i14] = measuredHeight32;
                            float measuredHeight42 = getMeasuredHeight() - this.chartBottom;
                            if (i9 != min) {
                            }
                            if (this.transitionMode == 2) {
                            }
                            f9 = f5;
                            f10 = f4;
                            f11 = f35;
                            f12 = f6;
                            f13 = f12;
                            f14 = 0.0f;
                            if (i9 != max) {
                            }
                            TransitionParams transitionParams62 = this.transitionParams;
                            if (transitionParams62 != null) {
                            }
                            if (f9 == 0.0f) {
                            }
                            if (this.skipPoints[i14]) {
                            }
                            if (i14 != i2) {
                            }
                            this.skipPoints[i14] = false;
                            if (i9 == min) {
                            }
                            f18 = f11 + measuredHeight22;
                        } else {
                            if (f33 != 0.0f) {
                                f3 = centerX;
                                f4 = centerY;
                                f5 = (jArr[i9] * lineViewData2.alpha) / f33;
                                float[] fArr222 = ((StackLinearChartData) this.chartData).xPercentage;
                                f6 = (fArr222[i9] * f27) - f28;
                                if (i9 != min) {
                                }
                                if (f5 != 0.0f) {
                                }
                                f8 = f28;
                                float measuredHeight222 = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f5;
                                i3 = i10;
                                float measuredHeight322 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight222) - f35;
                                z = z3;
                                this.startFromY[i14] = measuredHeight322;
                                float measuredHeight422 = getMeasuredHeight() - this.chartBottom;
                                if (i9 != min) {
                                }
                                if (this.transitionMode == 2) {
                                }
                                f9 = f5;
                                f10 = f4;
                                f11 = f35;
                                f12 = f6;
                                f13 = f12;
                                f14 = 0.0f;
                                if (i9 != max) {
                                }
                                TransitionParams transitionParams622 = this.transitionParams;
                                if (transitionParams622 != null) {
                                }
                                if (f9 == 0.0f) {
                                }
                                if (this.skipPoints[i14]) {
                                }
                                if (i14 != i2) {
                                }
                                this.skipPoints[i14] = false;
                                if (i9 == min) {
                                }
                                f18 = f11 + measuredHeight222;
                            }
                            f3 = centerX;
                            f4 = centerY;
                            f5 = 0.0f;
                            float[] fArr2222 = ((StackLinearChartData) this.chartData).xPercentage;
                            f6 = (fArr2222[i9] * f27) - f28;
                            if (i9 != min) {
                            }
                            if (f5 != 0.0f) {
                            }
                            f8 = f28;
                            float measuredHeight2222 = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f5;
                            i3 = i10;
                            float measuredHeight3222 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight2222) - f35;
                            z = z3;
                            this.startFromY[i14] = measuredHeight3222;
                            float measuredHeight4222 = getMeasuredHeight() - this.chartBottom;
                            if (i9 != min) {
                            }
                            if (this.transitionMode == 2) {
                            }
                            f9 = f5;
                            f10 = f4;
                            f11 = f35;
                            f12 = f6;
                            f13 = f12;
                            f14 = 0.0f;
                            if (i9 != max) {
                            }
                            TransitionParams transitionParams6222 = this.transitionParams;
                            if (transitionParams6222 != null) {
                            }
                            if (f9 == 0.0f) {
                            }
                            if (this.skipPoints[i14]) {
                            }
                            if (i14 != i2) {
                            }
                            this.skipPoints[i14] = false;
                            if (i9 == min) {
                            }
                            f18 = f11 + measuredHeight2222;
                        }
                    } else {
                        f7 = f27;
                        f8 = f28;
                        f18 = f35;
                        f2 = f31;
                        i3 = i10;
                        i4 = max;
                        i2 = i11;
                        f3 = centerX;
                        z = z3;
                        f10 = centerY;
                    }
                    i14++;
                    f35 = f18;
                    centerX = f3;
                    centerY = f10;
                    z3 = z;
                    f31 = f2;
                    max = i4;
                    f28 = f8;
                    i10 = i3;
                    i11 = i2;
                    f27 = f7;
                }
                i9++;
                centerY = centerY;
                i = i13;
                f = f34;
                f31 = f31;
                f28 = f28;
                i5 = 0;
            }
            int i15 = i;
            canvas.save();
            canvas.clipRect(f31, BaseChartView.SIGNATURE_TEXT_HEIGHT, f32, getMeasuredHeight() - this.chartBottom);
            if (z3) {
                canvas.drawColor(Theme.getColor(Theme.key_statisticChartLineEmpty));
            }
            for (int size = this.lines.size() - 1; size >= 0; size--) {
                LineViewData lineViewData3 = (LineViewData) this.lines.get(size);
                lineViewData3.paint.setAlpha(i15);
                canvas.drawPath(lineViewData3.chartPath, lineViewData3.paint);
                lineViewData3.paint.setAlpha(NotificationCenter.liveLocationsChanged);
            }
            canvas.restore();
            canvas.restore();
        }
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
        if (this.chartData != null) {
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
                            f4 += j3 * lineViewData2.alpha;
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
                                f = (((StackLinearChartData) this.chartData).simplifiedY[i9][i4] * lineViewData.alpha) / f4;
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

    /* JADX WARN: Removed duplicated region for block: B:43:0x0139  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0142  */
    @Override // org.telegram.ui.Charts.BaseChartView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void fillTransitionParams(TransitionParams transitionParams) {
        float f;
        ChartData chartData = this.chartData;
        if (chartData == null) {
            return;
        }
        float f2 = this.chartWidth;
        ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
        float f3 = chartPickerDelegate.pickerEnd;
        float f4 = chartPickerDelegate.pickerStart;
        float f5 = f2 / (f3 - f4);
        float f6 = BaseChartView.HORIZONTAL_PADDING;
        float f7 = (f4 * f5) - f6;
        float[] fArr = ((StackLinearChartData) chartData).xPercentage;
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
                        f9 += j * lineViewData.alpha;
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
                            TransitionParams transitionParams2 = this.transitionParams;
                            if (i4 != 0) {
                                transitionParams2.startX[i8] = f10;
                                transitionParams2.startY[i8] = measuredHeight2;
                            } else {
                                transitionParams2.endX[i8] = f10;
                                transitionParams2.endY[i8] = measuredHeight2;
                            }
                        }
                        f = 0.0f;
                        float f102 = (((StackLinearChartData) this.chartData).xPercentage[i5] * f5) - f7;
                        float measuredHeight3 = f * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT);
                        float f112 = i9;
                        float measuredHeight22 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight3) - f112;
                        i9 = (int) (f112 + measuredHeight3);
                        TransitionParams transitionParams22 = this.transitionParams;
                        if (i4 != 0) {
                        }
                    } else {
                        if (f9 != f8) {
                            f = (jArr[i5] * lineViewData2.alpha) / f9;
                            float f1022 = (((StackLinearChartData) this.chartData).xPercentage[i5] * f5) - f7;
                            float measuredHeight32 = f * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT);
                            float f1122 = i9;
                            float measuredHeight222 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight32) - f1122;
                            i9 = (int) (f1122 + measuredHeight32);
                            TransitionParams transitionParams222 = this.transitionParams;
                            if (i4 != 0) {
                            }
                        }
                        f = 0.0f;
                        float f10222 = (((StackLinearChartData) this.chartData).xPercentage[i5] * f5) - f7;
                        float measuredHeight322 = f * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT);
                        float f11222 = i9;
                        float measuredHeight2222 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight322) - f11222;
                        i9 = (int) (f11222 + measuredHeight322);
                        TransitionParams transitionParams2222 = this.transitionParams;
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

    @Override // org.telegram.ui.Charts.BaseChartView
    public long findMaxValue(int i, int i2) {
        return 100L;
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected float getMinDistance() {
        return 0.1f;
    }

    @Override // org.telegram.ui.Charts.BaseChartView, android.view.View
    protected void onDraw(Canvas canvas) {
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
