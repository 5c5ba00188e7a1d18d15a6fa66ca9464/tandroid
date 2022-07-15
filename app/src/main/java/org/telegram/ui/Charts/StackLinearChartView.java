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
/* loaded from: classes3.dex */
public class StackLinearChartView<T extends StackLinearViewData> extends BaseChartView<StackLinearChartData, T> {
    boolean[] skipPoints;
    float[] startFromY;
    private Matrix matrix = new Matrix();
    private float[] mapPoints = new float[2];
    Path ovalPath = new Path();

    @Override // org.telegram.ui.Charts.BaseChartView
    public int findMaxValue(int i, int i2) {
        return 100;
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected float getMinDistance() {
        return 0.1f;
    }

    public StackLinearChartView(Context context) {
        super(context);
        this.superDraw = true;
        this.useAlphaSignature = true;
        this.drawPointOnSelection = false;
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    public T createLineViewData(ChartData.Line line) {
        return (T) new StackLinearViewData(line);
    }

    /* JADX WARN: Removed duplicated region for block: B:111:0x03a4  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x03f3  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x03fb  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x03fd  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0404 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:142:0x0444  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x045a  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x045e  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0468  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x0474  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x01fd  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0203  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0214  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x021d  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0251  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0254  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0261 A[ADDED_TO_REGION] */
    @Override // org.telegram.ui.Charts.BaseChartView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void drawChart(Canvas canvas) {
        float f;
        int i;
        int i2;
        float f2;
        float f3;
        float f4;
        int i3;
        int i4;
        int i5;
        float f5;
        float f6;
        float f7;
        float f8;
        int i6;
        float f9;
        int i7;
        float f10;
        double d;
        LineViewData lineViewData;
        int i8;
        int i9;
        float f11;
        float f12;
        boolean z;
        float f13;
        float f14;
        int i10;
        double d2;
        float f15;
        int[] iArr;
        if (this.chartData != 0) {
            float f16 = this.chartWidth;
            ChartPickerDelegate chartPickerDelegate = this.pickerDelegate;
            float f17 = chartPickerDelegate.pickerEnd;
            float f18 = chartPickerDelegate.pickerStart;
            float f19 = f16 / (f17 - f18);
            float f20 = (f18 * f19) - BaseChartView.HORIZONTAL_PADDING;
            float centerX = this.chartArea.centerX();
            float centerY = this.chartArea.centerY() + AndroidUtilities.dp(16.0f);
            int i11 = 0;
            for (int i12 = 0; i12 < this.lines.size(); i12++) {
                ((StackLinearViewData) this.lines.get(i12)).chartPath.reset();
                ((StackLinearViewData) this.lines.get(i12)).chartPathPicker.reset();
            }
            canvas.save();
            boolean[] zArr = this.skipPoints;
            if (zArr == null || zArr.length < ((StackLinearChartData) this.chartData).lines.size()) {
                this.skipPoints = new boolean[((StackLinearChartData) this.chartData).lines.size()];
                this.startFromY = new float[((StackLinearChartData) this.chartData).lines.size()];
            }
            int i13 = this.transitionMode;
            if (i13 == 2) {
                float f21 = this.transitionParams.progress / 0.6f;
                if (f21 > 1.0f) {
                    f21 = 1.0f;
                }
                this.ovalPath.reset();
                float width = this.chartArea.width() > this.chartArea.height() ? this.chartArea.width() : this.chartArea.height();
                float height = (this.chartArea.width() > this.chartArea.height() ? this.chartArea.height() : this.chartArea.width()) * 0.45f;
                float f22 = height + (((width - height) / 2.0f) * (1.0f - this.transitionParams.progress));
                RectF rectF = new RectF();
                rectF.set(centerX - f22, centerY - f22, centerX + f22, centerY + f22);
                this.ovalPath.addRoundRect(rectF, f22, f22, Path.Direction.CW);
                canvas.clipPath(this.ovalPath);
                f = f21;
                i = 255;
            } else {
                i = i13 == 3 ? (int) (this.transitionParams.progress * 255.0f) : 255;
                f = 0.0f;
            }
            T t = this.chartData;
            int i14 = ((int) (BaseChartView.HORIZONTAL_PADDING / (((StackLinearChartData) t).xPercentage.length < 2 ? 1.0f : ((StackLinearChartData) t).xPercentage[1] * f19))) + 1;
            int max = Math.max(0, (this.startXIndex - i14) - 1);
            int min = Math.min(((StackLinearChartData) this.chartData).xPercentage.length - 1, this.endXIndex + i14 + 1);
            int i15 = max;
            float f23 = 0.0f;
            float f24 = 0.0f;
            boolean z2 = false;
            while (i15 <= min) {
                int i16 = 0;
                float f25 = 0.0f;
                int i17 = 0;
                while (i11 < this.lines.size()) {
                    LineViewData lineViewData2 = (LineViewData) this.lines.get(i11);
                    int i18 = i;
                    if (lineViewData2.enabled || lineViewData2.alpha != 0.0f) {
                        if (lineViewData2.line.y[i15] > 0) {
                            f25 += iArr[i15] * lineViewData2.alpha;
                            i16++;
                        }
                        i17 = i11;
                    }
                    i11++;
                    i = i18;
                }
                int i19 = i;
                float f26 = 0.0f;
                int i20 = 0;
                while (i20 < this.lines.size()) {
                    LineViewData lineViewData3 = (LineViewData) this.lines.get(i20);
                    float f27 = f23;
                    if (lineViewData3.enabled || lineViewData3.alpha != 0.0f) {
                        int[] iArr2 = lineViewData3.line.y;
                        float f28 = f24;
                        if (i16 == 1) {
                            if (iArr2[i15] != 0) {
                                f6 = lineViewData3.alpha;
                                i3 = i16;
                                T t2 = this.chartData;
                                f7 = (((StackLinearChartData) t2).xPercentage[i15] * f19) - f20;
                                if (i15 != min) {
                                    f8 = getMeasuredWidth();
                                } else {
                                    f8 = (((StackLinearChartData) t2).xPercentage[i15 + 1] * f19) - f20;
                                }
                                if (f6 != 0.0f) {
                                    f4 = f19;
                                    i5 = i17;
                                    if (i20 == i5) {
                                        z2 = true;
                                    }
                                } else {
                                    f4 = f19;
                                    i5 = i17;
                                }
                                f3 = f20;
                                float measuredHeight = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f6;
                                float measuredHeight2 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight) - f26;
                                this.startFromY[i20] = measuredHeight2;
                                float f29 = f26;
                                float measuredHeight3 = getMeasuredHeight() - this.chartBottom;
                                if (i15 != min) {
                                    f27 = f7;
                                } else if (i15 == max) {
                                    f28 = f7;
                                }
                                i6 = min;
                                if (this.transitionMode == 2 || i20 == i5) {
                                    f9 = f7;
                                    i7 = max;
                                    f10 = 0.0f;
                                } else {
                                    if (f7 < centerX) {
                                        TransitionParams transitionParams = this.transitionParams;
                                        f13 = transitionParams.startX[i20];
                                        f14 = transitionParams.startY[i20];
                                    } else {
                                        TransitionParams transitionParams2 = this.transitionParams;
                                        f13 = transitionParams2.endX[i20];
                                        f14 = transitionParams2.endY[i20];
                                    }
                                    float f30 = centerX - f13;
                                    float f31 = centerY - f14;
                                    float f32 = 1.0f - f;
                                    float f33 = ((((f7 - f13) * f31) / f30) + f14) * f;
                                    float f34 = (measuredHeight2 * f32) + f33;
                                    float f35 = (measuredHeight3 * f32) + f33;
                                    float f36 = f31 / f30;
                                    if (f36 > 0.0f) {
                                        i10 = max;
                                        d2 = Math.toDegrees(-Math.atan(f36));
                                    } else {
                                        i10 = max;
                                        d2 = Math.toDegrees(Math.atan(Math.abs(f36)));
                                    }
                                    f10 = ((float) d2) - 90.0f;
                                    if (f7 >= centerX) {
                                        float[] fArr = this.mapPoints;
                                        fArr[0] = f7;
                                        fArr[1] = f34;
                                        this.matrix.reset();
                                        this.matrix.postRotate(this.transitionParams.progress * f10, centerX, centerY);
                                        this.matrix.mapPoints(this.mapPoints);
                                        float[] fArr2 = this.mapPoints;
                                        f9 = fArr2[0];
                                        f15 = fArr2[1];
                                        if (f9 < centerX) {
                                            f9 = centerX;
                                        }
                                        fArr2[0] = f7;
                                        fArr2[1] = f35;
                                        this.matrix.reset();
                                        this.matrix.postRotate(this.transitionParams.progress * f10, centerX, centerY);
                                        this.matrix.mapPoints(this.mapPoints);
                                        measuredHeight3 = this.mapPoints[1];
                                        if (f7 < centerX) {
                                            f7 = centerX;
                                        }
                                    } else if (f8 >= centerX) {
                                        f7 = (f7 * f32) + (centerX * f);
                                        measuredHeight2 = (f34 * f32) + (centerY * f);
                                        f9 = f7;
                                        measuredHeight3 = measuredHeight2;
                                        i7 = i10;
                                    } else {
                                        float[] fArr3 = this.mapPoints;
                                        fArr3[0] = f7;
                                        fArr3[1] = f34;
                                        this.matrix.reset();
                                        Matrix matrix = this.matrix;
                                        TransitionParams transitionParams3 = this.transitionParams;
                                        float f37 = transitionParams3.progress;
                                        matrix.postRotate((f37 * f10) + (f37 * transitionParams3.angle[i20]), centerX, centerY);
                                        this.matrix.mapPoints(this.mapPoints);
                                        float[] fArr4 = this.mapPoints;
                                        float f38 = fArr4[0];
                                        f15 = fArr4[1];
                                        if (f8 >= centerX) {
                                            float f39 = this.transitionParams.progress;
                                            fArr4[0] = (f7 * (1.0f - f39)) + (f39 * centerX);
                                        } else {
                                            fArr4[0] = f7;
                                        }
                                        fArr4[1] = f35;
                                        this.matrix.reset();
                                        Matrix matrix2 = this.matrix;
                                        TransitionParams transitionParams4 = this.transitionParams;
                                        float f40 = transitionParams4.progress;
                                        matrix2.postRotate((f40 * f10) + (f40 * transitionParams4.angle[i20]), centerX, centerY);
                                        this.matrix.mapPoints(this.mapPoints);
                                        float[] fArr5 = this.mapPoints;
                                        float f41 = fArr5[0];
                                        measuredHeight3 = fArr5[1];
                                        f7 = f41;
                                        f9 = f38;
                                    }
                                    i7 = i10;
                                    measuredHeight2 = f15;
                                }
                                if (i15 != i7) {
                                    float measuredHeight4 = getMeasuredHeight();
                                    f2 = f;
                                    i2 = i7;
                                    if (this.transitionMode != 2 || i20 == i5) {
                                        z = false;
                                        f12 = 0.0f;
                                    } else {
                                        float[] fArr6 = this.mapPoints;
                                        fArr6[0] = 0.0f - centerX;
                                        fArr6[1] = measuredHeight4;
                                        this.matrix.reset();
                                        Matrix matrix3 = this.matrix;
                                        TransitionParams transitionParams5 = this.transitionParams;
                                        float f42 = transitionParams5.progress;
                                        matrix3.postRotate((f10 * f42) + (f42 * transitionParams5.angle[i20]), centerX, centerY);
                                        this.matrix.mapPoints(this.mapPoints);
                                        float[] fArr7 = this.mapPoints;
                                        z = false;
                                        f12 = fArr7[0];
                                        measuredHeight4 = fArr7[1];
                                    }
                                    lineViewData3.chartPath.moveTo(f12, measuredHeight4);
                                    this.skipPoints[i20] = z;
                                } else {
                                    f2 = f;
                                    i2 = i7;
                                }
                                TransitionParams transitionParams6 = this.transitionParams;
                                float f43 = transitionParams6 != null ? 0.0f : transitionParams6.progress;
                                if (f6 == 0.0f || i15 <= 0 || iArr2[i15 - 1] != 0) {
                                    i4 = i6;
                                } else {
                                    i4 = i6;
                                    if (i15 < i4 && iArr2[i15 + 1] == 0 && this.transitionMode != 2) {
                                        if (!this.skipPoints[i20]) {
                                            if (i20 == i5) {
                                                lineViewData3.chartPath.lineTo(f7, measuredHeight3 * (1.0f - f43));
                                            } else {
                                                lineViewData3.chartPath.lineTo(f7, measuredHeight3);
                                            }
                                        }
                                        this.skipPoints[i20] = true;
                                        if (i15 == i4) {
                                            float measuredWidth = getMeasuredWidth();
                                            float measuredHeight5 = getMeasuredHeight();
                                            if (this.transitionMode == 2 && i20 != i5) {
                                                float[] fArr8 = this.mapPoints;
                                                fArr8[0] = measuredWidth + centerX;
                                                fArr8[1] = measuredHeight5;
                                                this.matrix.reset();
                                                Matrix matrix4 = this.matrix;
                                                TransitionParams transitionParams7 = this.transitionParams;
                                                matrix4.postRotate(transitionParams7.progress * transitionParams7.angle[i20], centerX, centerY);
                                                this.matrix.mapPoints(this.mapPoints);
                                                float[] fArr9 = this.mapPoints;
                                                float f44 = fArr9[0];
                                                float f45 = fArr9[1];
                                            } else {
                                                lineViewData3.chartPath.lineTo(measuredWidth, measuredHeight5);
                                            }
                                            if (this.transitionMode == 2 && i20 != i5) {
                                                TransitionParams transitionParams8 = this.transitionParams;
                                                float f46 = (centerY - transitionParams8.startY[i20]) / (centerX - transitionParams8.startX[i20]);
                                                if (f46 > 0.0f) {
                                                    lineViewData = lineViewData3;
                                                    d = Math.toDegrees(-Math.atan(f46));
                                                } else {
                                                    lineViewData = lineViewData3;
                                                    d = Math.toDegrees(Math.atan(Math.abs(f46)));
                                                }
                                                TransitionParams transitionParams9 = this.transitionParams;
                                                float f47 = transitionParams9.startX[i20];
                                                float f48 = transitionParams9.startY[i20];
                                                float[] fArr10 = this.mapPoints;
                                                fArr10[0] = f47;
                                                fArr10[1] = f48;
                                                this.matrix.reset();
                                                Matrix matrix5 = this.matrix;
                                                TransitionParams transitionParams10 = this.transitionParams;
                                                float f49 = transitionParams10.progress;
                                                matrix5.postRotate(((((float) d) - 90.0f) * f49) + (f49 * transitionParams10.angle[i20]), centerX, centerY);
                                                this.matrix.mapPoints(this.mapPoints);
                                                float[] fArr11 = this.mapPoints;
                                                float f50 = fArr11[0];
                                                float f51 = fArr11[1];
                                                if (Math.abs(f9 - f50) < 0.001d && ((f51 < centerY && measuredHeight2 < centerY) || (f51 > centerY && measuredHeight2 > centerY))) {
                                                    i9 = this.transitionParams.angle[i20] == -180.0f ? 0 : 3;
                                                    i8 = 0;
                                                } else {
                                                    i8 = quarterForPoint(f9, measuredHeight2);
                                                    i9 = quarterForPoint(f50, f51);
                                                }
                                                while (i8 <= i9) {
                                                    if (i8 == 0) {
                                                        lineViewData.chartPath.lineTo(getMeasuredWidth(), 0.0f);
                                                    } else if (i8 == 1) {
                                                        lineViewData.chartPath.lineTo(getMeasuredWidth(), getMeasuredHeight());
                                                    } else {
                                                        if (i8 == 2) {
                                                            lineViewData.chartPath.lineTo(0.0f, getMeasuredHeight());
                                                        } else {
                                                            lineViewData.chartPath.lineTo(0.0f, 0.0f);
                                                        }
                                                        i8++;
                                                    }
                                                    i8++;
                                                }
                                                f5 = f29 + measuredHeight;
                                                f23 = f27;
                                                f24 = f28;
                                            }
                                        }
                                        f5 = f29 + measuredHeight;
                                        f23 = f27;
                                        f24 = f28;
                                    }
                                }
                                if (this.skipPoints[i20]) {
                                    f11 = 1.0f;
                                } else if (i20 == i5) {
                                    f11 = 1.0f;
                                    lineViewData3.chartPath.lineTo(f7, measuredHeight3 * (1.0f - f43));
                                } else {
                                    f11 = 1.0f;
                                    lineViewData3.chartPath.lineTo(f7, measuredHeight3);
                                }
                                if (i20 != i5) {
                                    lineViewData3.chartPath.lineTo(f9, (f11 - f43) * measuredHeight2);
                                } else {
                                    lineViewData3.chartPath.lineTo(f9, measuredHeight2);
                                }
                                this.skipPoints[i20] = false;
                                if (i15 == i4) {
                                }
                                f5 = f29 + measuredHeight;
                                f23 = f27;
                                f24 = f28;
                            }
                            i3 = i16;
                            f6 = 0.0f;
                            T t22 = this.chartData;
                            f7 = (((StackLinearChartData) t22).xPercentage[i15] * f19) - f20;
                            if (i15 != min) {
                            }
                            if (f6 != 0.0f) {
                            }
                            f3 = f20;
                            float measuredHeight6 = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f6;
                            float measuredHeight22 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight6) - f26;
                            this.startFromY[i20] = measuredHeight22;
                            float f292 = f26;
                            float measuredHeight32 = getMeasuredHeight() - this.chartBottom;
                            if (i15 != min) {
                            }
                            i6 = min;
                            if (this.transitionMode == 2) {
                            }
                            f9 = f7;
                            i7 = max;
                            f10 = 0.0f;
                            if (i15 != i7) {
                            }
                            TransitionParams transitionParams62 = this.transitionParams;
                            if (transitionParams62 != null) {
                            }
                            if (f6 == 0.0f) {
                            }
                            i4 = i6;
                            if (this.skipPoints[i20]) {
                            }
                            if (i20 != i5) {
                            }
                            this.skipPoints[i20] = false;
                            if (i15 == i4) {
                            }
                            f5 = f292 + measuredHeight6;
                            f23 = f27;
                            f24 = f28;
                        } else {
                            if (f25 != 0.0f) {
                                i3 = i16;
                                f6 = (iArr2[i15] * lineViewData3.alpha) / f25;
                                T t222 = this.chartData;
                                f7 = (((StackLinearChartData) t222).xPercentage[i15] * f19) - f20;
                                if (i15 != min) {
                                }
                                if (f6 != 0.0f) {
                                }
                                f3 = f20;
                                float measuredHeight62 = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f6;
                                float measuredHeight222 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight62) - f26;
                                this.startFromY[i20] = measuredHeight222;
                                float f2922 = f26;
                                float measuredHeight322 = getMeasuredHeight() - this.chartBottom;
                                if (i15 != min) {
                                }
                                i6 = min;
                                if (this.transitionMode == 2) {
                                }
                                f9 = f7;
                                i7 = max;
                                f10 = 0.0f;
                                if (i15 != i7) {
                                }
                                TransitionParams transitionParams622 = this.transitionParams;
                                if (transitionParams622 != null) {
                                }
                                if (f6 == 0.0f) {
                                }
                                i4 = i6;
                                if (this.skipPoints[i20]) {
                                }
                                if (i20 != i5) {
                                }
                                this.skipPoints[i20] = false;
                                if (i15 == i4) {
                                }
                                f5 = f2922 + measuredHeight62;
                                f23 = f27;
                                f24 = f28;
                            }
                            i3 = i16;
                            f6 = 0.0f;
                            T t2222 = this.chartData;
                            f7 = (((StackLinearChartData) t2222).xPercentage[i15] * f19) - f20;
                            if (i15 != min) {
                            }
                            if (f6 != 0.0f) {
                            }
                            f3 = f20;
                            float measuredHeight622 = ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT) * f6;
                            float measuredHeight2222 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight622) - f26;
                            this.startFromY[i20] = measuredHeight2222;
                            float f29222 = f26;
                            float measuredHeight3222 = getMeasuredHeight() - this.chartBottom;
                            if (i15 != min) {
                            }
                            i6 = min;
                            if (this.transitionMode == 2) {
                            }
                            f9 = f7;
                            i7 = max;
                            f10 = 0.0f;
                            if (i15 != i7) {
                            }
                            TransitionParams transitionParams6222 = this.transitionParams;
                            if (transitionParams6222 != null) {
                            }
                            if (f6 == 0.0f) {
                            }
                            i4 = i6;
                            if (this.skipPoints[i20]) {
                            }
                            if (i20 != i5) {
                            }
                            this.skipPoints[i20] = false;
                            if (i15 == i4) {
                            }
                            f5 = f29222 + measuredHeight622;
                            f23 = f27;
                            f24 = f28;
                        }
                    } else {
                        f4 = f19;
                        f3 = f20;
                        f5 = f26;
                        f2 = f;
                        i3 = i16;
                        i4 = min;
                        i2 = max;
                        i5 = i17;
                        f23 = f27;
                    }
                    i20++;
                    f26 = f5;
                    i17 = i5;
                    min = i4;
                    i16 = i3;
                    f19 = f4;
                    f20 = f3;
                    f = f2;
                    max = i2;
                }
                i11 = 0;
                i15++;
                i = i19;
                f = f;
            }
            int i21 = i;
            canvas.save();
            canvas.clipRect(f24, BaseChartView.SIGNATURE_TEXT_HEIGHT, f23, getMeasuredHeight() - this.chartBottom);
            if (z2) {
                canvas.drawColor(Theme.getColor("statisticChartLineEmpty"));
            }
            for (int size = this.lines.size() - 1; size >= 0; size--) {
                LineViewData lineViewData4 = (LineViewData) this.lines.get(size);
                lineViewData4.paint.setAlpha(i21);
                canvas.drawPath(lineViewData4.chartPath, lineViewData4.paint);
                lineViewData4.paint.setAlpha(255);
            }
            canvas.restore();
            canvas.restore();
        }
    }

    private int quarterForPoint(float f, float f2) {
        float centerX = this.chartArea.centerX();
        float centerY = this.chartArea.centerY() + AndroidUtilities.dp(16.0f);
        if (f < centerX || f2 > centerY) {
            if (f >= centerX && f2 >= centerY) {
                return 1;
            }
            return (f >= centerX || f2 < centerY) ? 3 : 2;
        }
        return 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x00e4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0112 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x014a  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x015e  */
    @Override // org.telegram.ui.Charts.BaseChartView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void drawPickerChart(Canvas canvas) {
        boolean z;
        int i;
        float f;
        T t;
        T t2;
        if (this.chartData != 0) {
            int size = this.lines.size();
            for (int i2 = 0; i2 < size; i2++) {
                ((StackLinearViewData) this.lines.get(i2)).chartPathPicker.reset();
            }
            T t3 = this.chartData;
            int i3 = ((StackLinearChartData) t3).simplifiedSize;
            boolean[] zArr = this.skipPoints;
            if (zArr == null || zArr.length < ((StackLinearChartData) t3).lines.size()) {
                this.skipPoints = new boolean[((StackLinearChartData) this.chartData).lines.size()];
            }
            int i4 = 0;
            boolean z2 = false;
            while (true) {
                int i5 = 1;
                if (i4 >= i3) {
                    break;
                }
                float f2 = 0.0f;
                float f3 = 0.0f;
                int i6 = 0;
                int i7 = 0;
                for (int i8 = 0; i8 < this.lines.size(); i8++) {
                    LineViewData lineViewData = (LineViewData) this.lines.get(i8);
                    if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                        if (((StackLinearChartData) this.chartData).simplifiedY[i8][i4] > 0) {
                            f3 += ((StackLinearChartData) t2).simplifiedY[i8][i4] * lineViewData.alpha;
                            i6++;
                        }
                        i7 = i8;
                    }
                }
                int i9 = i3 - 1;
                float f4 = (i4 / i9) * this.pickerWidth;
                int i10 = 0;
                float f5 = 0.0f;
                while (i10 < this.lines.size()) {
                    LineViewData lineViewData2 = (LineViewData) this.lines.get(i10);
                    if (!lineViewData2.enabled && lineViewData2.alpha == f2) {
                        i = i3;
                        z = z2;
                    } else if (i6 == i5) {
                        if (((StackLinearChartData) this.chartData).simplifiedY[i10][i4] != 0) {
                            f = lineViewData2.alpha;
                            if (f == f2 && i10 == i7) {
                                z2 = true;
                            }
                            int i11 = this.pikerHeight;
                            float f6 = f * i11;
                            float f7 = (i11 - f6) - f5;
                            i = i3;
                            if (i4 != 0) {
                                z = z2;
                                lineViewData2.chartPathPicker.moveTo(0.0f, i11);
                                this.skipPoints[i10] = false;
                            } else {
                                z = z2;
                            }
                            t = this.chartData;
                            if (((StackLinearChartData) t).simplifiedY[i10][i4] != 0 && i4 > 0 && ((StackLinearChartData) t).simplifiedY[i10][i4 - 1] == 0 && i4 < i9 && ((StackLinearChartData) t).simplifiedY[i10][i4 + 1] == 0) {
                                if (!this.skipPoints[i10]) {
                                    lineViewData2.chartPathPicker.lineTo(f4, this.pikerHeight);
                                }
                                this.skipPoints[i10] = true;
                            } else {
                                if (this.skipPoints[i10]) {
                                    lineViewData2.chartPathPicker.lineTo(f4, this.pikerHeight);
                                }
                                lineViewData2.chartPathPicker.lineTo(f4, f7);
                                this.skipPoints[i10] = false;
                            }
                            if (i4 == i9) {
                                lineViewData2.chartPathPicker.lineTo(this.pickerWidth, this.pikerHeight);
                            }
                            f5 += f6;
                        }
                        f = 0.0f;
                        if (f == f2) {
                            z2 = true;
                        }
                        int i112 = this.pikerHeight;
                        float f62 = f * i112;
                        float f72 = (i112 - f62) - f5;
                        i = i3;
                        if (i4 != 0) {
                        }
                        t = this.chartData;
                        if (((StackLinearChartData) t).simplifiedY[i10][i4] != 0) {
                        }
                        if (this.skipPoints[i10]) {
                        }
                        lineViewData2.chartPathPicker.lineTo(f4, f72);
                        this.skipPoints[i10] = false;
                        if (i4 == i9) {
                        }
                        f5 += f62;
                    } else {
                        if (f3 != f2) {
                            f = (((StackLinearChartData) this.chartData).simplifiedY[i10][i4] * lineViewData2.alpha) / f3;
                            if (f == f2) {
                            }
                            int i1122 = this.pikerHeight;
                            float f622 = f * i1122;
                            float f722 = (i1122 - f622) - f5;
                            i = i3;
                            if (i4 != 0) {
                            }
                            t = this.chartData;
                            if (((StackLinearChartData) t).simplifiedY[i10][i4] != 0) {
                            }
                            if (this.skipPoints[i10]) {
                            }
                            lineViewData2.chartPathPicker.lineTo(f4, f722);
                            this.skipPoints[i10] = false;
                            if (i4 == i9) {
                            }
                            f5 += f622;
                        }
                        f = 0.0f;
                        if (f == f2) {
                        }
                        int i11222 = this.pikerHeight;
                        float f6222 = f * i11222;
                        float f7222 = (i11222 - f6222) - f5;
                        i = i3;
                        if (i4 != 0) {
                        }
                        t = this.chartData;
                        if (((StackLinearChartData) t).simplifiedY[i10][i4] != 0) {
                        }
                        if (this.skipPoints[i10]) {
                        }
                        lineViewData2.chartPathPicker.lineTo(f4, f7222);
                        this.skipPoints[i10] = false;
                        if (i4 == i9) {
                        }
                        f5 += f6222;
                    }
                    i10++;
                    i3 = i;
                    z2 = z;
                    i5 = 1;
                    f2 = 0.0f;
                }
                i4++;
            }
            if (z2) {
                canvas.drawColor(Theme.getColor("statisticChartLineEmpty"));
            }
            for (int size2 = this.lines.size() - 1; size2 >= 0; size2--) {
                LineViewData lineViewData3 = (LineViewData) this.lines.get(size2);
                canvas.drawPath(lineViewData3.chartPathPicker, lineViewData3.paint);
            }
        }
    }

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

    /* JADX WARN: Removed duplicated region for block: B:43:0x0135  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0140  */
    @Override // org.telegram.ui.Charts.BaseChartView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void fillTransitionParams(TransitionParams transitionParams) {
        float f;
        int[] iArr;
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
            int i6 = 0;
            for (int i7 = 0; i7 < this.lines.size(); i7++) {
                LineViewData lineViewData = (LineViewData) this.lines.get(i7);
                if (lineViewData.enabled || lineViewData.alpha != 0.0f) {
                    if (lineViewData.line.y[i5] > 0) {
                        f8 += iArr[i5] * lineViewData.alpha;
                        i6++;
                    }
                }
            }
            int i8 = 0;
            int i9 = 0;
            while (i8 < this.lines.size()) {
                LineViewData lineViewData2 = (LineViewData) this.lines.get(i8);
                if (lineViewData2.enabled || lineViewData2.alpha != 0.0f) {
                    int[] iArr2 = lineViewData2.line.y;
                    if (i6 == i2) {
                        if (iArr2[i5] != 0) {
                            f = lineViewData2.alpha;
                            float f9 = (((StackLinearChartData) this.chartData).xPercentage[i5] * f5) - f7;
                            float measuredHeight = f * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT);
                            float f10 = i9;
                            float measuredHeight2 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight) - f10;
                            i9 = (int) (f10 + measuredHeight);
                            if (i4 != 0) {
                                TransitionParams transitionParams2 = this.transitionParams;
                                transitionParams2.startX[i8] = f9;
                                transitionParams2.startY[i8] = measuredHeight2;
                            } else {
                                TransitionParams transitionParams3 = this.transitionParams;
                                transitionParams3.endX[i8] = f9;
                                transitionParams3.endY[i8] = measuredHeight2;
                            }
                        }
                        f = 0.0f;
                        float f92 = (((StackLinearChartData) this.chartData).xPercentage[i5] * f5) - f7;
                        float measuredHeight3 = f * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT);
                        float f102 = i9;
                        float measuredHeight22 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight3) - f102;
                        i9 = (int) (f102 + measuredHeight3);
                        if (i4 != 0) {
                        }
                    } else {
                        if (f8 != 0.0f) {
                            f = (iArr2[i5] * lineViewData2.alpha) / f8;
                            float f922 = (((StackLinearChartData) this.chartData).xPercentage[i5] * f5) - f7;
                            float measuredHeight32 = f * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT);
                            float f1022 = i9;
                            float measuredHeight222 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight32) - f1022;
                            i9 = (int) (f1022 + measuredHeight32);
                            if (i4 != 0) {
                            }
                        }
                        f = 0.0f;
                        float f9222 = (((StackLinearChartData) this.chartData).xPercentage[i5] * f5) - f7;
                        float measuredHeight322 = f * ((getMeasuredHeight() - this.chartBottom) - BaseChartView.SIGNATURE_TEXT_HEIGHT);
                        float f10222 = i9;
                        float measuredHeight2222 = ((getMeasuredHeight() - this.chartBottom) - measuredHeight322) - f10222;
                        i9 = (int) (f10222 + measuredHeight322);
                        if (i4 != 0) {
                        }
                    }
                }
                i8++;
                i2 = 1;
            }
            i4++;
            i = 2;
            i2 = 1;
        }
    }
}
