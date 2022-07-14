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
    boolean[] skipPoints;
    float[] startFromY;
    private Matrix matrix = new Matrix();
    private float[] mapPoints = new float[2];
    Path ovalPath = new Path();

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

    /* JADX WARN: Removed duplicated region for block: B:151:0x051e  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x069a  */
    @Override // org.telegram.ui.Charts.BaseChartView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void drawChart(Canvas canvas) {
        boolean hasEmptyPoint;
        float p;
        int transitionAlpha;
        float cX;
        float transitionProgressHalf;
        int localStart;
        float offset;
        float fullWidth;
        int drawingLinesCount;
        int localEnd;
        float yPercentage;
        float startXPoint;
        float nextXPoint;
        LineViewData line;
        float yPoint;
        float angle;
        float yPoint2;
        float yPointZero;
        LineViewData line2;
        float dX;
        float angle2;
        int startQuarter;
        int endQuarter;
        float cX2;
        float localY;
        float localX;
        float x1;
        float angle3;
        float endXPoint;
        if (this.chartData != 0) {
            float fullWidth2 = this.chartWidth / (this.pickerDelegate.pickerEnd - this.pickerDelegate.pickerStart);
            float offset2 = (this.pickerDelegate.pickerStart * fullWidth2) - HORIZONTAL_PADDING;
            float cX3 = this.chartArea.centerX();
            float cY = this.chartArea.centerY() + AndroidUtilities.dp(16.0f);
            for (int k = 0; k < this.lines.size(); k++) {
                ((StackLinearViewData) this.lines.get(k)).chartPath.reset();
                ((StackLinearViewData) this.lines.get(k)).chartPathPicker.reset();
            }
            canvas.save();
            boolean[] zArr = this.skipPoints;
            if (zArr == null || zArr.length < ((StackLinearChartData) this.chartData).lines.size()) {
                this.skipPoints = new boolean[((StackLinearChartData) this.chartData).lines.size()];
                this.startFromY = new float[((StackLinearChartData) this.chartData).lines.size()];
            }
            int transitionAlpha2 = 255;
            float transitionProgressHalf2 = 0.0f;
            if (this.transitionMode == 2) {
                float transitionProgressHalf3 = this.transitionParams.progress / 0.6f;
                if (transitionProgressHalf3 <= 1.0f) {
                    transitionProgressHalf2 = transitionProgressHalf3;
                } else {
                    transitionProgressHalf2 = 1.0f;
                }
                this.ovalPath.reset();
                float radiusStart = this.chartArea.width() > this.chartArea.height() ? this.chartArea.width() : this.chartArea.height();
                float radiusEnd = (this.chartArea.width() > this.chartArea.height() ? this.chartArea.height() : this.chartArea.width()) * 0.45f;
                float radius = (((radiusStart - radiusEnd) / 2.0f) * (1.0f - this.transitionParams.progress)) + radiusEnd;
                RectF rectF = new RectF();
                hasEmptyPoint = false;
                rectF.set(cX3 - radius, cY - radius, cX3 + radius, cY + radius);
                this.ovalPath.addRoundRect(rectF, radius, radius, Path.Direction.CW);
                canvas.clipPath(this.ovalPath);
            } else {
                hasEmptyPoint = false;
                if (this.transitionMode == 3) {
                    transitionAlpha2 = (int) (this.transitionParams.progress * 255.0f);
                }
            }
            float y1 = 0.0f;
            if (((StackLinearChartData) this.chartData).xPercentage.length < 2) {
                p = 1.0f;
            } else {
                p = ((StackLinearChartData) this.chartData).xPercentage[1] * fullWidth2;
            }
            int localEnd2 = ((int) (HORIZONTAL_PADDING / p)) + 1;
            int localStart2 = Math.max(0, (this.startXIndex - localEnd2) - 1);
            int localEnd3 = Math.min(((StackLinearChartData) this.chartData).xPercentage.length - 1, this.endXIndex + localEnd2 + 1);
            float startXPoint2 = 0.0f;
            float endXPoint2 = 0.0f;
            int i = localStart2;
            while (i <= localEnd3) {
                float stackOffset = 0.0f;
                float sum = 0.0f;
                float y12 = y1;
                int lastEnabled = 0;
                float p2 = p;
                int drawingLinesCount2 = 0;
                int additionalPoints = localEnd2;
                int additionalPoints2 = 0;
                while (true) {
                    transitionAlpha = transitionAlpha2;
                    if (additionalPoints2 >= this.lines.size()) {
                        break;
                    }
                    LineViewData line3 = (LineViewData) this.lines.get(additionalPoints2);
                    if (line3.enabled || line3.alpha != 0.0f) {
                        if (line3.line.y[i] <= 0) {
                            endXPoint = endXPoint2;
                        } else {
                            endXPoint = endXPoint2;
                            float endXPoint3 = line3.alpha;
                            sum += line3.line.y[i] * endXPoint3;
                            drawingLinesCount2++;
                        }
                        lastEnabled = additionalPoints2;
                    } else {
                        endXPoint = endXPoint2;
                    }
                    additionalPoints2++;
                    transitionAlpha2 = transitionAlpha;
                    endXPoint2 = endXPoint;
                }
                int k2 = 0;
                while (k2 < this.lines.size()) {
                    LineViewData line4 = (LineViewData) this.lines.get(k2);
                    if (line4.enabled || line4.alpha != 0.0f) {
                        int[] y = line4.line.y;
                        float endXPoint4 = endXPoint2;
                        if (drawingLinesCount2 == 1) {
                            if (y[i] == 0) {
                                yPercentage = 0.0f;
                                drawingLinesCount = drawingLinesCount2;
                            } else {
                                yPercentage = line4.alpha;
                                drawingLinesCount = drawingLinesCount2;
                            }
                        } else if (sum == 0.0f) {
                            yPercentage = 0.0f;
                            drawingLinesCount = drawingLinesCount2;
                        } else {
                            drawingLinesCount = drawingLinesCount2;
                            yPercentage = (y[i] * line4.alpha) / sum;
                        }
                        float xPoint = (((StackLinearChartData) this.chartData).xPercentage[i] * fullWidth2) - offset2;
                        if (i == localEnd3) {
                            startXPoint = startXPoint2;
                            nextXPoint = getMeasuredWidth();
                        } else {
                            startXPoint = startXPoint2;
                            nextXPoint = (((StackLinearChartData) this.chartData).xPercentage[i + 1] * fullWidth2) - offset2;
                        }
                        if (yPercentage == 0.0f && k2 == lastEnabled) {
                            hasEmptyPoint = true;
                        }
                        fullWidth = fullWidth2;
                        float height = ((getMeasuredHeight() - this.chartBottom) - SIGNATURE_TEXT_HEIGHT) * yPercentage;
                        offset = offset2;
                        float yPoint3 = ((getMeasuredHeight() - this.chartBottom) - height) - stackOffset;
                        this.startFromY[k2] = yPoint3;
                        float yPointZero2 = getMeasuredHeight() - this.chartBottom;
                        float xPointZero = xPoint;
                        if (i == localEnd3) {
                            endXPoint4 = xPoint;
                        } else if (i == localStart2) {
                            startXPoint = xPoint;
                        }
                        int localEnd4 = localEnd3;
                        if (this.transitionMode != 2 || k2 == lastEnabled) {
                            line = line4;
                            transitionProgressHalf = transitionProgressHalf2;
                            yPoint = yPoint3;
                            angle = xPointZero;
                            yPoint2 = yPointZero2;
                            yPointZero = 0.0f;
                        } else {
                            if (xPoint < cX3) {
                                float x12 = this.transitionParams.startX[k2];
                                x1 = x12;
                                y12 = this.transitionParams.startY[k2];
                            } else {
                                float x13 = this.transitionParams.endX[k2];
                                x1 = x13;
                                y12 = this.transitionParams.endY[k2];
                            }
                            float dX2 = cX3 - x1;
                            float dY = cY - y12;
                            float yTo = (((xPoint - x1) * dY) / dX2) + y12;
                            float yPoint4 = ((1.0f - transitionProgressHalf2) * yPoint3) + (yTo * transitionProgressHalf2);
                            float yPointZero3 = ((1.0f - transitionProgressHalf2) * yPointZero2) + (yTo * transitionProgressHalf2);
                            float yPointZero4 = dY / dX2;
                            if (yPointZero4 > 0.0f) {
                                line = line4;
                                angle3 = (float) Math.toDegrees(-Math.atan(yPointZero4));
                            } else {
                                line = line4;
                                angle3 = (float) Math.toDegrees(Math.atan(Math.abs(yPointZero4)));
                            }
                            float angle4 = angle3 - 90.0f;
                            if (xPoint >= cX3) {
                                float[] fArr = this.mapPoints;
                                fArr[0] = xPoint;
                                fArr[1] = yPoint4;
                                this.matrix.reset();
                                this.matrix.postRotate(this.transitionParams.progress * angle4, cX3, cY);
                                this.matrix.mapPoints(this.mapPoints);
                                float[] fArr2 = this.mapPoints;
                                xPoint = fArr2[0];
                                float yPoint5 = fArr2[1];
                                if (xPoint < cX3) {
                                    xPoint = cX3;
                                }
                                fArr2[0] = xPointZero;
                                fArr2[1] = yPointZero3;
                                this.matrix.reset();
                                this.matrix.postRotate(this.transitionParams.progress * angle4, cX3, cY);
                                this.matrix.mapPoints(this.mapPoints);
                                float yPointZero5 = this.mapPoints[1];
                                if (xPointZero < cX3) {
                                    xPointZero = cX3;
                                }
                                yPoint = yPoint5;
                                transitionProgressHalf = transitionProgressHalf2;
                                yPoint2 = yPointZero5;
                                yPointZero = angle4;
                                angle = xPointZero;
                            } else if (nextXPoint >= cX3) {
                                float xPointZero2 = ((1.0f - transitionProgressHalf2) * xPoint) + (cX3 * transitionProgressHalf2);
                                xPoint = xPointZero2;
                                yPoint = ((1.0f - transitionProgressHalf2) * yPoint4) + (cY * transitionProgressHalf2);
                                transitionProgressHalf = transitionProgressHalf2;
                                yPoint2 = yPoint;
                                yPointZero = angle4;
                                angle = xPointZero2;
                            } else {
                                float[] fArr3 = this.mapPoints;
                                fArr3[0] = xPoint;
                                fArr3[1] = yPoint4;
                                this.matrix.reset();
                                transitionProgressHalf = transitionProgressHalf2;
                                this.matrix.postRotate((this.transitionParams.progress * angle4) + (this.transitionParams.progress * this.transitionParams.angle[k2]), cX3, cY);
                                this.matrix.mapPoints(this.mapPoints);
                                float[] fArr4 = this.mapPoints;
                                xPoint = fArr4[0];
                                yPoint = fArr4[1];
                                if (nextXPoint >= cX3) {
                                    fArr4[0] = ((1.0f - this.transitionParams.progress) * xPointZero) + (this.transitionParams.progress * cX3);
                                } else {
                                    fArr4[0] = xPointZero;
                                }
                                this.mapPoints[1] = yPointZero3;
                                this.matrix.reset();
                                this.matrix.postRotate((this.transitionParams.progress * angle4) + (this.transitionParams.progress * this.transitionParams.angle[k2]), cX3, cY);
                                this.matrix.mapPoints(this.mapPoints);
                                float[] fArr5 = this.mapPoints;
                                float xPointZero3 = fArr5[0];
                                yPoint2 = fArr5[1];
                                angle = xPointZero3;
                                yPointZero = angle4;
                            }
                        }
                        if (i == localStart2) {
                            float localY2 = getMeasuredHeight();
                            LineViewData line5 = line;
                            localStart = localStart2;
                            if (this.transitionMode == 2 && k2 != lastEnabled) {
                                float[] fArr6 = this.mapPoints;
                                fArr6[0] = 0.0f - cX3;
                                fArr6[1] = localY2;
                                this.matrix.reset();
                                this.matrix.postRotate((this.transitionParams.progress * yPointZero) + (this.transitionParams.progress * this.transitionParams.angle[k2]), cX3, cY);
                                this.matrix.mapPoints(this.mapPoints);
                                float[] fArr7 = this.mapPoints;
                                float localX2 = fArr7[0];
                                localY2 = fArr7[1];
                                localX = localX2;
                            } else {
                                localX = 0.0f;
                            }
                            line2 = line5;
                            line2.chartPath.moveTo(localX, localY2);
                            this.skipPoints[k2] = false;
                        } else {
                            line2 = line;
                            localStart = localStart2;
                        }
                        float transitionProgress = this.transitionParams == null ? 0.0f : this.transitionParams.progress;
                        if (yPercentage == 0.0f && i > 0 && y[i - 1] == 0) {
                            localEnd = localEnd4;
                            if (i < localEnd && y[i + 1] == 0 && this.transitionMode != 2) {
                                if (!this.skipPoints[k2]) {
                                    if (k2 == lastEnabled) {
                                        line2.chartPath.lineTo(angle, yPoint2 * (1.0f - transitionProgress));
                                    } else {
                                        line2.chartPath.lineTo(angle, yPoint2);
                                    }
                                }
                                this.skipPoints[k2] = true;
                                if (i != localEnd) {
                                    float localX3 = getMeasuredWidth();
                                    float localY3 = getMeasuredHeight();
                                    if (this.transitionMode == 2 && k2 != lastEnabled) {
                                        float[] fArr8 = this.mapPoints;
                                        fArr8[0] = localX3 + cX3;
                                        fArr8[1] = localY3;
                                        this.matrix.reset();
                                        this.matrix.postRotate(this.transitionParams.progress * this.transitionParams.angle[k2], cX3, cY);
                                        this.matrix.mapPoints(this.mapPoints);
                                        float[] fArr9 = this.mapPoints;
                                        float localX4 = fArr9[0];
                                        float localY4 = fArr9[1];
                                    } else {
                                        line2.chartPath.lineTo(localX3, localY3);
                                    }
                                    if (this.transitionMode != 2 || k2 == lastEnabled) {
                                        cX = cX3;
                                    } else {
                                        float x14 = this.transitionParams.startX[k2];
                                        float y13 = this.transitionParams.startY[k2];
                                        float dX3 = cX3 - x14;
                                        float x15 = (cY - y13) / dX3;
                                        if (x15 > 0.0f) {
                                            dX = dX3;
                                            angle2 = (float) Math.toDegrees(-Math.atan(x15));
                                        } else {
                                            dX = dX3;
                                            angle2 = (float) Math.toDegrees(Math.atan(Math.abs(x15)));
                                        }
                                        float localX5 = this.transitionParams.startX[k2];
                                        float localY5 = this.transitionParams.startY[k2];
                                        float[] fArr10 = this.mapPoints;
                                        fArr10[0] = localX5;
                                        fArr10[1] = localY5;
                                        this.matrix.reset();
                                        this.matrix.postRotate((this.transitionParams.progress * (angle2 - 90.0f)) + (this.transitionParams.progress * this.transitionParams.angle[k2]), cX3, cY);
                                        this.matrix.mapPoints(this.mapPoints);
                                        float[] fArr11 = this.mapPoints;
                                        float localX6 = fArr11[0];
                                        float localY6 = fArr11[1];
                                        float yPoint6 = yPoint;
                                        if (Math.abs(xPoint - localX6) >= 0.001d || ((localY6 >= cY || yPoint6 >= cY) && (localY6 <= cY || yPoint6 <= cY))) {
                                            endQuarter = quarterForPoint(xPoint, yPoint6);
                                            startQuarter = quarterForPoint(localX6, localY6);
                                        } else if (this.transitionParams.angle[k2] == -180.0f) {
                                            endQuarter = 0;
                                            startQuarter = 0;
                                        } else {
                                            endQuarter = 0;
                                            startQuarter = 3;
                                        }
                                        int q = endQuarter;
                                        while (q <= startQuarter) {
                                            if (q == 0) {
                                                localY = localY6;
                                                cX2 = cX3;
                                                line2.chartPath.lineTo(getMeasuredWidth(), 0.0f);
                                            } else {
                                                localY = localY6;
                                                cX2 = cX3;
                                                if (q == 1) {
                                                    line2.chartPath.lineTo(getMeasuredWidth(), getMeasuredHeight());
                                                } else if (q == 2) {
                                                    line2.chartPath.lineTo(0.0f, getMeasuredHeight());
                                                } else {
                                                    line2.chartPath.lineTo(0.0f, 0.0f);
                                                }
                                            }
                                            q++;
                                            localY6 = localY;
                                            cX3 = cX2;
                                        }
                                        cX = cX3;
                                        y12 = y13;
                                    }
                                } else {
                                    cX = cX3;
                                }
                                stackOffset += height;
                                endXPoint2 = endXPoint4;
                                startXPoint2 = startXPoint;
                            }
                        } else {
                            localEnd = localEnd4;
                        }
                        if (this.skipPoints[k2]) {
                            if (k2 == lastEnabled) {
                                line2.chartPath.lineTo(angle, yPoint2 * (1.0f - transitionProgress));
                            } else {
                                line2.chartPath.lineTo(angle, yPoint2);
                            }
                        }
                        if (k2 == lastEnabled) {
                            line2.chartPath.lineTo(xPoint, yPoint * (1.0f - transitionProgress));
                        } else {
                            line2.chartPath.lineTo(xPoint, yPoint);
                        }
                        this.skipPoints[k2] = false;
                        if (i != localEnd) {
                        }
                        stackOffset += height;
                        endXPoint2 = endXPoint4;
                        startXPoint2 = startXPoint;
                    } else {
                        fullWidth = fullWidth2;
                        offset = offset2;
                        cX = cX3;
                        localEnd = localEnd3;
                        transitionProgressHalf = transitionProgressHalf2;
                        drawingLinesCount = drawingLinesCount2;
                        localStart = localStart2;
                    }
                    k2++;
                    localEnd3 = localEnd;
                    drawingLinesCount2 = drawingLinesCount;
                    fullWidth2 = fullWidth;
                    offset2 = offset;
                    localStart2 = localStart;
                    transitionProgressHalf2 = transitionProgressHalf;
                    cX3 = cX;
                }
                i++;
                y1 = y12;
                p = p2;
                localEnd2 = additionalPoints;
                transitionAlpha2 = transitionAlpha;
            }
            int transitionAlpha3 = transitionAlpha2;
            canvas.save();
            canvas.clipRect(startXPoint2, SIGNATURE_TEXT_HEIGHT, endXPoint2, getMeasuredHeight() - this.chartBottom);
            if (hasEmptyPoint) {
                canvas.drawColor(Theme.getColor(Theme.key_statisticChartLineEmpty));
            }
            for (int k3 = this.lines.size() - 1; k3 >= 0; k3--) {
                LineViewData line6 = (LineViewData) this.lines.get(k3);
                line6.paint.setAlpha(transitionAlpha3);
                canvas.drawPath(line6.chartPath, line6.paint);
                line6.paint.setAlpha(255);
            }
            canvas.restore();
            canvas.restore();
        }
    }

    private int quarterForPoint(float x, float y) {
        float cX = this.chartArea.centerX();
        float cY = this.chartArea.centerY() + AndroidUtilities.dp(16.0f);
        if (x >= cX && y <= cY) {
            return 0;
        }
        if (x >= cX && y >= cY) {
            return 1;
        }
        if (x < cX && y >= cY) {
            return 2;
        }
        return 3;
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected void drawPickerChart(Canvas canvas) {
        float f;
        float sum;
        int nl;
        float yPercentage;
        boolean hasEmptyPoint;
        if (this.chartData != 0) {
            int nl2 = this.lines.size();
            for (int k = 0; k < nl2; k++) {
                ((StackLinearViewData) this.lines.get(k)).chartPathPicker.reset();
            }
            int n = ((StackLinearChartData) this.chartData).simplifiedSize;
            boolean[] zArr = this.skipPoints;
            if (zArr == null || zArr.length < ((StackLinearChartData) this.chartData).lines.size()) {
                this.skipPoints = new boolean[((StackLinearChartData) this.chartData).lines.size()];
            }
            boolean hasEmptyPoint2 = false;
            int i = 0;
            while (true) {
                int i2 = 1;
                if (i >= n) {
                    break;
                }
                float stackOffset = 0.0f;
                float sum2 = 0.0f;
                int lastEnabled = 0;
                int drawingLinesCount = 0;
                int k2 = 0;
                while (true) {
                    f = 0.0f;
                    if (k2 >= this.lines.size()) {
                        break;
                    }
                    LineViewData line = (LineViewData) this.lines.get(k2);
                    if (line.enabled || line.alpha != 0.0f) {
                        if (((StackLinearChartData) this.chartData).simplifiedY[k2][i] > 0) {
                            sum2 += ((StackLinearChartData) this.chartData).simplifiedY[k2][i] * line.alpha;
                            drawingLinesCount++;
                        }
                        lastEnabled = k2;
                    }
                    k2++;
                }
                float xPoint = (i / (n - 1)) * this.pickerWidth;
                int k3 = 0;
                while (k3 < this.lines.size()) {
                    LineViewData line2 = (LineViewData) this.lines.get(k3);
                    if (line2.enabled || line2.alpha != f) {
                        if (drawingLinesCount == i2) {
                            if (((StackLinearChartData) this.chartData).simplifiedY[k3][i] == 0) {
                                yPercentage = 0.0f;
                            } else {
                                yPercentage = line2.alpha;
                            }
                        } else if (sum2 == f) {
                            yPercentage = 0.0f;
                        } else {
                            yPercentage = (((StackLinearChartData) this.chartData).simplifiedY[k3][i] * line2.alpha) / sum2;
                        }
                        if (yPercentage == f && k3 == lastEnabled) {
                            hasEmptyPoint2 = true;
                        }
                        float height = this.pikerHeight * yPercentage;
                        float yPoint = (this.pikerHeight - height) - stackOffset;
                        if (i != 0) {
                            nl = nl2;
                            hasEmptyPoint = hasEmptyPoint2;
                            sum = sum2;
                        } else {
                            nl = nl2;
                            hasEmptyPoint = hasEmptyPoint2;
                            sum = sum2;
                            line2.chartPathPicker.moveTo(0.0f, this.pikerHeight);
                            this.skipPoints[k3] = false;
                        }
                        if (((StackLinearChartData) this.chartData).simplifiedY[k3][i] == 0 && i > 0 && ((StackLinearChartData) this.chartData).simplifiedY[k3][i - 1] == 0 && i < n - 1 && ((StackLinearChartData) this.chartData).simplifiedY[k3][i + 1] == 0) {
                            if (!this.skipPoints[k3]) {
                                line2.chartPathPicker.lineTo(xPoint, this.pikerHeight);
                            }
                            this.skipPoints[k3] = true;
                        } else {
                            if (this.skipPoints[k3]) {
                                line2.chartPathPicker.lineTo(xPoint, this.pikerHeight);
                            }
                            line2.chartPathPicker.lineTo(xPoint, yPoint);
                            this.skipPoints[k3] = false;
                        }
                        if (i == n - 1) {
                            line2.chartPathPicker.lineTo(this.pickerWidth, this.pikerHeight);
                        }
                        stackOffset += height;
                        hasEmptyPoint2 = hasEmptyPoint;
                    } else {
                        nl = nl2;
                        sum = sum2;
                    }
                    k3++;
                    nl2 = nl;
                    sum2 = sum;
                    i2 = 1;
                    f = 0.0f;
                }
                i++;
            }
            if (hasEmptyPoint2) {
                canvas.drawColor(Theme.getColor(Theme.key_statisticChartLineEmpty));
            }
            for (int k4 = this.lines.size() - 1; k4 >= 0; k4--) {
                LineViewData line3 = (LineViewData) this.lines.get(k4);
                canvas.drawPath(line3.chartPathPicker, line3.paint);
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
            if (this.tmpI < this.tmpN) {
                drawHorizontalLines(canvas, this.horizontalLines.get(this.tmpI));
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

    @Override // org.telegram.ui.Charts.BaseChartView
    public int findMaxValue(int startXIndex, int endXIndex) {
        return 100;
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    protected float getMinDistance() {
        return 0.1f;
    }

    @Override // org.telegram.ui.Charts.BaseChartView
    public void fillTransitionParams(TransitionParams params) {
        float p;
        float offset;
        float fullWidth;
        float p2;
        float yPercentage;
        if (this.chartData != 0) {
            float fullWidth2 = this.chartWidth / (this.pickerDelegate.pickerEnd - this.pickerDelegate.pickerStart);
            float offset2 = (this.pickerDelegate.pickerStart * fullWidth2) - HORIZONTAL_PADDING;
            int i = 2;
            int i2 = 1;
            if (((StackLinearChartData) this.chartData).xPercentage.length < 2) {
                p = 1.0f;
            } else {
                p = ((StackLinearChartData) this.chartData).xPercentage[1] * fullWidth2;
            }
            int additionalPoints = ((int) (HORIZONTAL_PADDING / p)) + 1;
            int localStart = Math.max(0, (this.startXIndex - additionalPoints) - 1);
            int localEnd = Math.min(((StackLinearChartData) this.chartData).xPercentage.length - 1, this.endXIndex + additionalPoints + 1);
            this.transitionParams.startX = new float[((StackLinearChartData) this.chartData).lines.size()];
            this.transitionParams.startY = new float[((StackLinearChartData) this.chartData).lines.size()];
            this.transitionParams.endX = new float[((StackLinearChartData) this.chartData).lines.size()];
            this.transitionParams.endY = new float[((StackLinearChartData) this.chartData).lines.size()];
            this.transitionParams.angle = new float[((StackLinearChartData) this.chartData).lines.size()];
            int j = 0;
            while (j < i) {
                int i3 = localStart;
                if (j == i2) {
                    i3 = localEnd;
                }
                int stackOffset = 0;
                float sum = 0.0f;
                int drawingLinesCount = 0;
                for (int k = 0; k < this.lines.size(); k++) {
                    LineViewData line = (LineViewData) this.lines.get(k);
                    if ((line.enabled || line.alpha != 0.0f) && line.line.y[i3] > 0) {
                        sum += line.line.y[i3] * line.alpha;
                        drawingLinesCount++;
                    }
                }
                int k2 = 0;
                while (k2 < this.lines.size()) {
                    LineViewData line2 = (LineViewData) this.lines.get(k2);
                    if (line2.enabled || line2.alpha != 0.0f) {
                        int[] y = line2.line.y;
                        if (drawingLinesCount == 1) {
                            if (y[i3] == 0) {
                                p2 = p;
                                yPercentage = 0.0f;
                            } else {
                                yPercentage = line2.alpha;
                                p2 = p;
                            }
                        } else if (sum == 0.0f) {
                            yPercentage = 0.0f;
                            p2 = p;
                        } else {
                            p2 = p;
                            yPercentage = (y[i3] * line2.alpha) / sum;
                        }
                        float xPoint = (((StackLinearChartData) this.chartData).xPercentage[i3] * fullWidth2) - offset2;
                        fullWidth = fullWidth2;
                        float height = ((getMeasuredHeight() - this.chartBottom) - SIGNATURE_TEXT_HEIGHT) * yPercentage;
                        offset = offset2;
                        float yPoint = ((getMeasuredHeight() - this.chartBottom) - height) - stackOffset;
                        int stackOffset2 = (int) (stackOffset + height);
                        if (j == 0) {
                            this.transitionParams.startX[k2] = xPoint;
                            this.transitionParams.startY[k2] = yPoint;
                        } else {
                            this.transitionParams.endX[k2] = xPoint;
                            this.transitionParams.endY[k2] = yPoint;
                        }
                        stackOffset = stackOffset2;
                    } else {
                        fullWidth = fullWidth2;
                        offset = offset2;
                        p2 = p;
                    }
                    k2++;
                    p = p2;
                    fullWidth2 = fullWidth;
                    offset2 = offset;
                }
                j++;
                i = 2;
                i2 = 1;
            }
        }
    }
}
