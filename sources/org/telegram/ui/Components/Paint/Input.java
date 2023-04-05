package org.telegram.ui.Components.Paint;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.google.zxing.common.detector.MathUtils;
import java.util.List;
import java.util.Vector;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.Paint.Brush;
import org.telegram.ui.Components.Size;
/* loaded from: classes4.dex */
public class Input {
    private static final CubicBezierInterpolator PRESSURE_INTERPOLATOR = new CubicBezierInterpolator(0.0d, 0.5d, 0.0d, 1.0d);
    private ValueAnimator arrowAnimator;
    private boolean beganDrawing;
    private boolean canFill;
    private boolean clearBuffer;
    private final ShapeDetector detector;
    private long drawingStart;
    private ValueAnimator fillAnimator;
    private boolean hasMoved;
    private boolean ignore;
    private Matrix invertMatrix;
    private boolean isFirst;
    private float lastAngle;
    private boolean lastAngleSet;
    private Point lastLocation;
    private double lastRemainder;
    private Point lastThickLocation;
    private long lastVelocityUpdate;
    private int pointsCount;
    private int realPointsCount;
    private RenderView renderView;
    private Brush switchedBrushByStylusFrom;
    private double thicknessCount;
    private double thicknessSum;
    private float velocity;
    private Point[] points = new Point[3];
    private float[] tempPoint = new float[2];
    private final Runnable fillWithCurrentBrush = new Runnable() { // from class: org.telegram.ui.Components.Paint.Input$$ExternalSyntheticLambda3
        @Override // java.lang.Runnable
        public final void run() {
            Input.this.lambda$new$1();
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public void setShapeHelper(Shape shape) {
        if (shape != null) {
            float currentWeight = this.renderView.getCurrentWeight();
            shape.thickness = currentWeight;
            double d = this.thicknessSum;
            if (d > 0.0d) {
                double d2 = currentWeight;
                Double.isNaN(d2);
                shape.thickness = (float) (d2 * (d / this.thicknessCount));
            }
            if (shape.getType() == 4) {
                shape.arrowTriangleLength *= shape.thickness;
            }
        }
        this.renderView.getPainting().setHelperShape(shape);
    }

    public Input(RenderView renderView) {
        this.renderView = renderView;
        this.detector = new ShapeDetector(renderView.getContext(), new Utilities.Callback() { // from class: org.telegram.ui.Components.Paint.Input$$ExternalSyntheticLambda6
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                Input.this.setShapeHelper((Shape) obj);
            }
        });
    }

    public void setMatrix(Matrix matrix) {
        Matrix matrix2 = new Matrix();
        this.invertMatrix = matrix2;
        matrix.invert(matrix2);
    }

    private void fill(Brush brush, final boolean z, final Runnable runnable) {
        if (!this.canFill || this.lastLocation == null) {
            return;
        }
        if (brush == null) {
            brush = this.renderView.getCurrentBrush();
        }
        if ((brush instanceof Brush.Elliptical) || (brush instanceof Brush.Neon)) {
            brush = new Brush.Radial();
        }
        final Brush brush2 = brush;
        this.canFill = false;
        this.renderView.getPainting().clearStroke();
        this.pointsCount = 0;
        this.realPointsCount = 0;
        this.lastAngleSet = false;
        this.beganDrawing = false;
        if (z) {
            this.renderView.onBeganDrawing();
        }
        Size size = this.renderView.getPainting().getSize();
        Point point = this.lastLocation;
        float distance = MathUtils.distance((float) point.x, (float) point.y, 0.0f, 0.0f);
        Point point2 = this.lastLocation;
        float max = Math.max(distance, MathUtils.distance((float) point2.x, (float) point2.y, size.width, 0.0f));
        Point point3 = this.lastLocation;
        float distance2 = MathUtils.distance((float) point3.x, (float) point3.y, 0.0f, size.height);
        Point point4 = this.lastLocation;
        final float max2 = Math.max(max, Math.max(distance2, MathUtils.distance((float) point4.x, (float) point4.y, size.width, size.height))) / 0.84f;
        ValueAnimator valueAnimator = this.arrowAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.arrowAnimator = null;
        }
        ValueAnimator valueAnimator2 = this.fillAnimator;
        if (valueAnimator2 != null) {
            valueAnimator2.cancel();
            this.fillAnimator = null;
        }
        Point point5 = this.lastLocation;
        final Point point6 = new Point(point5.x, point5.y, 1.0d);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.fillAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Paint.Input$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                Input.this.lambda$fill$0(point6, brush2, max2, valueAnimator3);
            }
        });
        this.fillAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Paint.Input.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                Input.this.fillAnimator = null;
                Path path = new Path(new Point[]{point6});
                path.setup(Input.this.renderView.getCurrentColor(), max2 * 1.0f, brush2);
                Input.this.renderView.getPainting().commitPath(path, brush2.isEraser() ? -1 : Input.this.renderView.getCurrentColor(), z, null);
                if (z) {
                    Input.this.renderView.onFinishedDrawing(true);
                }
                Runnable runnable2 = runnable;
                if (runnable2 != null) {
                    runnable2.run();
                }
            }
        });
        this.fillAnimator.setDuration(450L);
        this.fillAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.fillAnimator.start();
        if (z) {
            BotWebViewVibrationEffect.IMPACT_HEAVY.vibrate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fill$0(Point point, Brush brush, float f, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        Path path = new Path(new Point[]{point});
        path.setup(brush.isEraser() ? -1 : this.renderView.getCurrentColor(), floatValue * f, brush);
        this.renderView.getPainting().paintStroke(path, true, true, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        fill(null, true, null);
    }

    public void clear(Runnable runnable) {
        this.lastLocation = new Point(this.renderView.getPainting().getSize().width, 0.0d, 1.0d);
        this.canFill = true;
        fill(new Brush.Eraser(), false, runnable);
    }

    public void ignoreOnce() {
        this.ignore = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x00b7  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0206 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0207  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void process(MotionEvent motionEvent, float f) {
        boolean z;
        boolean z2;
        if (this.fillAnimator != null || this.arrowAnimator != null) {
            return;
        }
        int actionMasked = motionEvent.getActionMasked();
        float x = motionEvent.getX();
        float height = this.renderView.getHeight() - motionEvent.getY();
        float[] fArr = this.tempPoint;
        fArr[0] = x;
        fArr[1] = height;
        this.invertMatrix.mapPoints(fArr);
        float currentTimeMillis = (float) (System.currentTimeMillis() - this.lastVelocityUpdate);
        this.velocity = androidx.core.math.MathUtils.clamp(this.velocity - (currentTimeMillis / 125.0f), 0.6f, 1.0f);
        this.lastVelocityUpdate = System.currentTimeMillis();
        float f2 = this.velocity;
        if (motionEvent.getToolType(motionEvent.getActionIndex()) == 2) {
            f2 = Math.max(0.1f, PRESSURE_INTERPOLATOR.getInterpolation(motionEvent.getPressure()));
            if ((motionEvent.getButtonState() & 32) == 32) {
                z = true;
                if (this.renderView.getCurrentBrush() != null) {
                    f2 = ((f2 - 1.0f) * AndroidUtilities.lerp(1.0f, this.renderView.getCurrentBrush().getSmoothThicknessRate(), androidx.core.math.MathUtils.clamp(this.realPointsCount / 16.0f, 0.0f, 1.0f))) + 1.0f;
                }
                float[] fArr2 = this.tempPoint;
                boolean z3 = z;
                Point point = new Point(fArr2[0], fArr2[1], f2);
                if (actionMasked != 0) {
                    if (actionMasked == 1) {
                        if (this.ignore) {
                            this.ignore = false;
                            return;
                        }
                        this.canFill = false;
                        this.detector.clear();
                        AndroidUtilities.cancelRunOnUIThread(this.fillWithCurrentBrush);
                        if (!this.renderView.getPainting().applyHelperShape()) {
                            if (!this.hasMoved) {
                                if (this.renderView.shouldDraw()) {
                                    point.edge = true;
                                    paintPath(new Path(point));
                                }
                                reset();
                            } else if (this.pointsCount > 0) {
                                smoothenAndPaintPoints(true, this.renderView.getCurrentBrush().getSmoothThicknessRate());
                                if (this.renderView.getCurrentBrush() instanceof Brush.Arrow) {
                                    final float f3 = this.lastAngle;
                                    final Point point2 = this.points[this.pointsCount - 1];
                                    Point point3 = this.lastThickLocation;
                                    final double d = point3 == null ? point.z : point3.z;
                                    final float currentWeight = this.renderView.getCurrentWeight() * ((float) d) * 4.5f;
                                    ValueAnimator valueAnimator = this.arrowAnimator;
                                    if (valueAnimator != null) {
                                        valueAnimator.cancel();
                                    }
                                    final float[] fArr3 = new float[1];
                                    final boolean[] zArr = new boolean[1];
                                    ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                                    this.arrowAnimator = ofFloat;
                                    ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Paint.Input$$ExternalSyntheticLambda0
                                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                                        public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                                            Input.this.lambda$process$2(point2, f3, currentWeight, fArr3, d, zArr, valueAnimator2);
                                        }
                                    });
                                    this.arrowAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Paint.Input.2
                                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                        public void onAnimationEnd(Animator animator) {
                                            if (!Input.this.renderView.getCurrentBrush().isEraser() || Input.this.renderView.getUndoStore().canUndo()) {
                                                Input.this.renderView.getPainting().commitPath(null, Input.this.renderView.getCurrentColor());
                                            }
                                            Input.this.arrowAnimator = null;
                                        }
                                    });
                                    this.arrowAnimator.setDuration(240L);
                                    this.arrowAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                                    this.arrowAnimator.start();
                                    z2 = false;
                                    if (z2 && (!this.renderView.getCurrentBrush().isEraser() || this.renderView.getUndoStore().canUndo())) {
                                        this.renderView.getPainting().commitPath(null, this.renderView.getCurrentColor(), true, new Runnable() { // from class: org.telegram.ui.Components.Paint.Input$$ExternalSyntheticLambda2
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                Input.this.lambda$process$3();
                                            }
                                        });
                                    }
                                }
                            }
                            z2 = true;
                            if (z2) {
                                this.renderView.getPainting().commitPath(null, this.renderView.getCurrentColor(), true, new Runnable() { // from class: org.telegram.ui.Components.Paint.Input$$ExternalSyntheticLambda2
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        Input.this.lambda$process$3();
                                    }
                                });
                            }
                        }
                        this.pointsCount = 0;
                        this.realPointsCount = 0;
                        this.lastAngleSet = false;
                        this.beganDrawing = false;
                        this.thicknessSum = 0.0d;
                        this.thicknessCount = 0.0d;
                        this.renderView.onFinishedDrawing(this.hasMoved);
                        return;
                    } else if (actionMasked != 2) {
                        if (actionMasked != 3) {
                            return;
                        }
                        if (this.ignore) {
                            this.ignore = false;
                            return;
                        }
                        this.canFill = false;
                        this.detector.clear();
                        this.renderView.getPainting().setHelperShape(null);
                        AndroidUtilities.cancelRunOnUIThread(this.fillWithCurrentBrush);
                        this.renderView.getPainting().clearStroke();
                        this.pointsCount = 0;
                        this.realPointsCount = 0;
                        this.lastAngleSet = false;
                        this.beganDrawing = false;
                        this.thicknessSum = 0.0d;
                        this.thicknessCount = 0.0d;
                        Brush brush = this.switchedBrushByStylusFrom;
                        if (brush != null) {
                            this.renderView.selectBrush(brush);
                            this.switchedBrushByStylusFrom = null;
                            return;
                        }
                        return;
                    }
                }
                if (this.ignore) {
                    if (!this.beganDrawing) {
                        this.beganDrawing = true;
                        this.hasMoved = false;
                        this.isFirst = true;
                        this.lastLocation = point;
                        this.drawingStart = System.currentTimeMillis();
                        this.points[0] = point;
                        this.pointsCount = 1;
                        this.realPointsCount = 1;
                        this.lastAngleSet = false;
                        this.clearBuffer = true;
                        this.canFill = true;
                        AndroidUtilities.runOnUIThread(this.fillWithCurrentBrush, ViewConfiguration.getLongPressTimeout());
                        return;
                    }
                    float distanceTo = point.getDistanceTo(this.lastLocation);
                    if (distanceTo < AndroidUtilities.dp(5.0f) / f) {
                        return;
                    }
                    if (this.canFill && (distanceTo > AndroidUtilities.dp(6.0f) / f || this.pointsCount > 4)) {
                        this.canFill = false;
                        AndroidUtilities.cancelRunOnUIThread(this.fillWithCurrentBrush);
                    }
                    if (!this.hasMoved) {
                        this.renderView.onBeganDrawing();
                        this.hasMoved = true;
                        if (z3 && (this.renderView.getCurrentBrush() instanceof Brush.Radial)) {
                            this.switchedBrushByStylusFrom = this.renderView.getCurrentBrush();
                            RenderView renderView = this.renderView;
                            List<Brush> list = Brush.BRUSHES_LIST;
                            renderView.selectBrush(list.get(list.size() - 1));
                        }
                    }
                    this.points[this.pointsCount] = point;
                    if (System.currentTimeMillis() - this.drawingStart > 3000) {
                        this.detector.clear();
                        this.renderView.getPainting().setHelperShape(null);
                    } else if ((this.renderView.getCurrentBrush() instanceof Brush.Radial) || (this.renderView.getCurrentBrush() instanceof Brush.Elliptical)) {
                        this.detector.append(point.x, point.y, distanceTo > ((float) AndroidUtilities.dp(6.0f)) / f);
                    }
                    int i = this.pointsCount + 1;
                    this.pointsCount = i;
                    this.realPointsCount++;
                    if (i == 3) {
                        Point[] pointArr = this.points;
                        float atan2 = (float) Math.atan2(pointArr[2].y - pointArr[1].y, pointArr[2].x - pointArr[1].x);
                        if (!this.lastAngleSet) {
                            this.lastAngle = atan2;
                            this.lastAngleSet = true;
                        } else {
                            float clamp = androidx.core.math.MathUtils.clamp(distanceTo / (AndroidUtilities.dp(16.0f) / f), 0.0f, 1.0f);
                            if (clamp > 0.4f) {
                                this.lastAngle = lerpAngle(this.lastAngle, atan2, clamp);
                            }
                        }
                        smoothenAndPaintPoints(false, this.renderView.getCurrentBrush().getSmoothThicknessRate());
                    }
                    this.lastLocation = point;
                    if (distanceTo > AndroidUtilities.dp(8.0f) / f) {
                        this.lastThickLocation = point;
                    }
                    this.velocity = androidx.core.math.MathUtils.clamp(this.velocity + (currentTimeMillis / 75.0f), 0.6f, 1.0f);
                    return;
                }
                return;
            }
        }
        z = false;
        if (this.renderView.getCurrentBrush() != null) {
        }
        float[] fArr22 = this.tempPoint;
        boolean z32 = z;
        Point point4 = new Point(fArr22[0], fArr22[1], f2);
        if (actionMasked != 0) {
        }
        if (this.ignore) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$process$2(Point point, float f, float f2, float[] fArr, double d, boolean[] zArr, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        double d2 = point.x;
        double d3 = f;
        Double.isNaN(d3);
        double d4 = d3 - 2.356194490192345d;
        double cos = Math.cos(d4);
        double d5 = f2;
        Double.isNaN(d5);
        double d6 = fArr[0];
        Double.isNaN(d6);
        double d7 = d2 + (cos * d5 * d6);
        double d8 = point.y;
        Double.isNaN(d3);
        double d9 = d3 - 2.5132741228718345d;
        double sin = Math.sin(d9);
        Double.isNaN(d5);
        double d10 = sin * d5;
        double d11 = fArr[0];
        Double.isNaN(d11);
        double d12 = point.x;
        double cos2 = Math.cos(d4);
        Double.isNaN(d5);
        double d13 = cos2 * d5;
        double d14 = floatValue;
        Double.isNaN(d14);
        double d15 = d12 + (d13 * d14);
        double d16 = point.y;
        double sin2 = Math.sin(d9);
        Double.isNaN(d5);
        Double.isNaN(d14);
        paintPath(new Path(new Point[]{new Point(d7, (d10 * d11) + d8, d), new Point(d15, d16 + (sin2 * d5 * d14), d, true)}));
        double d17 = point.x;
        Double.isNaN(d3);
        double d18 = d3 + 2.356194490192345d;
        double cos3 = Math.cos(d18);
        Double.isNaN(d5);
        double d19 = fArr[0];
        Double.isNaN(d19);
        double d20 = d17 + (cos3 * d5 * d19);
        double d21 = point.y;
        Double.isNaN(d3);
        double d22 = d3 + 2.5132741228718345d;
        double sin3 = Math.sin(d22);
        Double.isNaN(d5);
        double d23 = fArr[0];
        Double.isNaN(d23);
        double d24 = point.x;
        double cos4 = Math.cos(d18);
        Double.isNaN(d5);
        Double.isNaN(d14);
        double d25 = d24 + (cos4 * d5 * d14);
        double d26 = point.y;
        double sin4 = Math.sin(d22);
        Double.isNaN(d5);
        Double.isNaN(d14);
        paintPath(new Path(new Point[]{new Point(d20, d21 + (sin3 * d5 * d23), d), new Point(d25, d26 + (sin4 * d5 * d14), d, true)}));
        if (!zArr[0] && floatValue > 0.4f) {
            zArr[0] = true;
            BotWebViewVibrationEffect.SELECTION_CHANGE.vibrate();
        }
        fArr[0] = floatValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$process$3() {
        Brush brush = this.switchedBrushByStylusFrom;
        if (brush != null) {
            this.renderView.selectBrush(brush);
            this.switchedBrushByStylusFrom = null;
        }
    }

    private float lerpAngle(float f, float f2, float f3) {
        double d = 1.0f - f3;
        double d2 = f;
        double sin = Math.sin(d2);
        Double.isNaN(d);
        double d3 = f3;
        double d4 = f2;
        double sin2 = Math.sin(d4);
        Double.isNaN(d3);
        double cos = Math.cos(d2);
        Double.isNaN(d);
        double cos2 = Math.cos(d4);
        Double.isNaN(d3);
        return (float) Math.atan2((sin * d) + (sin2 * d3), (d * cos) + (d3 * cos2));
    }

    private void reset() {
        this.pointsCount = 0;
    }

    private void smoothenAndPaintPoints(boolean z, float f) {
        int i = this.pointsCount;
        if (i > 2) {
            Vector vector = new Vector();
            Point[] pointArr = this.points;
            Point point = pointArr[0];
            Point point2 = pointArr[1];
            Point point3 = pointArr[2];
            if (point3 == null || point2 == null || point == null) {
                return;
            }
            Point multiplySum = point2.multiplySum(point, 0.5d);
            Point multiplySum2 = point3.multiplySum(point2, 0.5d);
            int min = (int) Math.min(48.0d, Math.max(Math.floor(multiplySum.getDistanceTo(multiplySum2) / 1), 24.0d));
            float f2 = 1.0f / min;
            int i2 = 0;
            float f3 = 0.0f;
            while (i2 < min) {
                int i3 = i2;
                Point smoothPoint = smoothPoint(multiplySum, multiplySum2, point2, f3, f);
                if (this.isFirst) {
                    smoothPoint.edge = true;
                    this.isFirst = false;
                }
                vector.add(smoothPoint);
                this.thicknessSum += smoothPoint.z;
                this.thicknessCount += 1.0d;
                f3 += f2;
                i2 = i3 + 1;
            }
            if (z) {
                multiplySum2.edge = true;
            }
            vector.add(multiplySum2);
            Point[] pointArr2 = new Point[vector.size()];
            vector.toArray(pointArr2);
            paintPath(new Path(pointArr2));
            Point[] pointArr3 = this.points;
            System.arraycopy(pointArr3, 1, pointArr3, 0, 2);
            if (z) {
                this.pointsCount = 0;
                return;
            } else {
                this.pointsCount = 2;
                return;
            }
        }
        Point[] pointArr4 = new Point[i];
        System.arraycopy(this.points, 0, pointArr4, 0, i);
        paintPath(new Path(pointArr4));
    }

    private Point smoothPoint(Point point, Point point2, Point point3, float f, float f2) {
        float f3 = 1.0f - f;
        double d = f3;
        double pow = Math.pow(d, 2.0d);
        double d2 = 2.0f * f3 * f;
        double d3 = f * f;
        double d4 = point.x;
        double d5 = f3 * f3;
        Double.isNaN(d5);
        double d6 = f;
        Double.isNaN(d6);
        Double.isNaN(d);
        double d7 = (d4 * d5) + (point3.x * 2.0d * d6 * d);
        double d8 = point2.x;
        Double.isNaN(d3);
        double d9 = d7 + (d8 * d3);
        double d10 = point.y;
        Double.isNaN(d5);
        Double.isNaN(d6);
        Double.isNaN(d);
        double d11 = (d10 * d5) + (point3.y * 2.0d * d6 * d);
        double d12 = point2.y;
        Double.isNaN(d3);
        double d13 = point3.z;
        Double.isNaN(d2);
        double d14 = point2.z;
        Double.isNaN(d3);
        double lerp = AndroidUtilities.lerp(1.0f, f2, androidx.core.math.MathUtils.clamp(this.realPointsCount / 16.0f, 0.0f, 1.0f));
        Double.isNaN(lerp);
        return new Point(d9, d11 + (d12 * d3), (((((point.z * pow) + (d13 * d2)) + (d14 * d3)) - 1.0d) * lerp) + 1.0d);
    }

    private void paintPath(final Path path) {
        path.setup(this.renderView.getCurrentColor(), this.renderView.getCurrentWeight(), this.renderView.getCurrentBrush());
        if (this.clearBuffer) {
            this.lastRemainder = 0.0d;
        }
        path.remainder = this.lastRemainder;
        this.renderView.getPainting().paintStroke(path, this.clearBuffer, false, new Runnable() { // from class: org.telegram.ui.Components.Paint.Input$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                Input.this.lambda$paintPath$5(path);
            }
        });
        this.clearBuffer = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$paintPath$4(Path path) {
        this.lastRemainder = path.remainder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$paintPath$5(final Path path) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Paint.Input$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                Input.this.lambda$paintPath$4(path);
            }
        });
    }
}
