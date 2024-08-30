package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.BubbleActivity;
/* loaded from: classes3.dex */
public class PhotoFilterBlurControl extends FrameLayout {
    private static final float BlurInsetProximity = AndroidUtilities.dp(20.0f);
    private static final float BlurViewCenterInset = AndroidUtilities.dp(30.0f);
    private static final float BlurViewRadiusInset = AndroidUtilities.dp(30.0f);
    private final int GestureStateBegan;
    private final int GestureStateCancelled;
    private final int GestureStateChanged;
    private final int GestureStateEnded;
    private final int GestureStateFailed;
    private BlurViewActiveControl activeControl;
    private Size actualAreaSize;
    private float angle;
    private Paint arcPaint;
    private RectF arcRect;
    private Point centerPoint;
    private boolean checkForMoving;
    private boolean checkForZooming;
    private PhotoFilterLinearBlurControlDelegate delegate;
    private float falloff;
    private boolean inBubbleMode;
    private boolean isMoving;
    private boolean isZooming;
    private Paint paint;
    private float pointerScale;
    private float pointerStartX;
    private float pointerStartY;
    private float size;
    private Point startCenterPoint;
    private float startDistance;
    private float startPointerDistance;
    private float startRadius;
    private int type;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class 1 {
        static final /* synthetic */ int[] $SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl;

        static {
            int[] iArr = new int[BlurViewActiveControl.values().length];
            $SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl = iArr;
            try {
                iArr[BlurViewActiveControl.BlurViewActiveControlCenter.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl[BlurViewActiveControl.BlurViewActiveControlInnerRadius.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl[BlurViewActiveControl.BlurViewActiveControlOuterRadius.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl[BlurViewActiveControl.BlurViewActiveControlRotation.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public enum BlurViewActiveControl {
        BlurViewActiveControlNone,
        BlurViewActiveControlCenter,
        BlurViewActiveControlInnerRadius,
        BlurViewActiveControlOuterRadius,
        BlurViewActiveControlWholeArea,
        BlurViewActiveControlRotation
    }

    /* loaded from: classes3.dex */
    public interface PhotoFilterLinearBlurControlDelegate {
        void valueChanged(Point point, float f, float f2, float f3);
    }

    public PhotoFilterBlurControl(Context context) {
        super(context);
        this.GestureStateBegan = 1;
        this.GestureStateChanged = 2;
        this.GestureStateEnded = 3;
        this.GestureStateCancelled = 4;
        this.GestureStateFailed = 5;
        this.startCenterPoint = new Point();
        this.actualAreaSize = new Size();
        this.centerPoint = new Point(0.5f, 0.5f);
        this.falloff = 0.15f;
        this.size = 0.35f;
        this.arcRect = new RectF();
        this.pointerScale = 1.0f;
        this.checkForMoving = true;
        this.paint = new Paint(1);
        this.arcPaint = new Paint(1);
        setWillNotDraw(false);
        this.paint.setColor(-1);
        this.arcPaint.setColor(-1);
        this.arcPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        this.arcPaint.setStyle(Paint.Style.STROKE);
        this.inBubbleMode = context instanceof BubbleActivity;
    }

    private float degreesToRadians(float f) {
        return (f * 3.1415927f) / 180.0f;
    }

    private Point getActualCenterPoint() {
        float f = this.actualAreaSize.width;
        float width = ((getWidth() - f) / 2.0f) + (this.centerPoint.x * f);
        int i = (Build.VERSION.SDK_INT < 21 || this.inBubbleMode) ? 0 : AndroidUtilities.statusBarHeight;
        Size size = this.actualAreaSize;
        float f2 = size.height;
        float height = i + ((getHeight() - f2) / 2.0f);
        float f3 = size.width;
        return new Point(width, (height - ((f3 - f2) / 2.0f)) + (this.centerPoint.y * f3));
    }

    private float getActualInnerRadius() {
        Size size = this.actualAreaSize;
        return Math.min(size.width, size.height) * this.falloff;
    }

    private float getActualOuterRadius() {
        Size size = this.actualAreaSize;
        return Math.min(size.width, size.height) * this.size;
    }

    private float getDistance(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() != 2) {
            return 0.0f;
        }
        float x = motionEvent.getX(0);
        float y = motionEvent.getY(0);
        float x2 = x - motionEvent.getX(1);
        float y2 = y - motionEvent.getY(1);
        return (float) Math.sqrt((x2 * x2) + (y2 * y2));
    }

    private void handlePan(int i, MotionEvent motionEvent) {
        Point point;
        float f;
        float f2;
        float f3;
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        Point actualCenterPoint = getActualCenterPoint();
        float f4 = x - actualCenterPoint.x;
        float f5 = y - actualCenterPoint.y;
        float sqrt = (float) Math.sqrt((f4 * f4) + (f5 * f5));
        Size size = this.actualAreaSize;
        float min = Math.min(size.width, size.height);
        float f6 = this.falloff * min;
        float f7 = this.size * min;
        double d = f4;
        double degreesToRadians = degreesToRadians(this.angle);
        Double.isNaN(degreesToRadians);
        double cos = Math.cos(degreesToRadians + 1.5707963267948966d);
        Double.isNaN(d);
        double d2 = d * cos;
        double d3 = f5;
        double degreesToRadians2 = degreesToRadians(this.angle);
        Double.isNaN(degreesToRadians2);
        double sin = Math.sin(degreesToRadians2 + 1.5707963267948966d);
        Double.isNaN(d3);
        float abs = (float) Math.abs(d2 + (d3 * sin));
        if (i == 1) {
            this.pointerStartX = motionEvent.getX();
            this.pointerStartY = motionEvent.getY();
            r6 = Math.abs(f7 - f6) < BlurInsetProximity ? 1 : 0;
            float f8 = r6 != 0 ? 0.0f : BlurViewRadiusInset;
            float f9 = r6 == 0 ? BlurViewRadiusInset : 0.0f;
            int i2 = this.type;
            if (i2 == 0) {
                if (sqrt >= BlurViewCenterInset) {
                    float f10 = BlurViewRadiusInset;
                    float f11 = f6 - f10;
                    if (abs > f11 && abs < f8 + f6) {
                        this.activeControl = BlurViewActiveControl.BlurViewActiveControlInnerRadius;
                        this.startDistance = abs;
                        this.startRadius = f6;
                    } else if (abs > f7 - f9 && abs < f7 + f10) {
                        this.activeControl = BlurViewActiveControl.BlurViewActiveControlOuterRadius;
                        this.startDistance = abs;
                        this.startRadius = f7;
                    } else if (abs <= f11 || abs >= f7 + f10) {
                        this.activeControl = BlurViewActiveControl.BlurViewActiveControlRotation;
                    }
                }
                this.activeControl = BlurViewActiveControl.BlurViewActiveControlCenter;
                this.startCenterPoint = actualCenterPoint;
            } else if (i2 == 1) {
                if (sqrt >= BlurViewCenterInset) {
                    float f12 = BlurViewRadiusInset;
                    if (sqrt > f6 - f12 && sqrt < f8 + f6) {
                        this.activeControl = BlurViewActiveControl.BlurViewActiveControlInnerRadius;
                        this.startDistance = sqrt;
                        this.startRadius = f6;
                    } else if (sqrt > f7 - f9 && sqrt < f12 + f7) {
                        this.activeControl = BlurViewActiveControl.BlurViewActiveControlOuterRadius;
                        this.startDistance = sqrt;
                        this.startRadius = f7;
                    }
                }
                this.activeControl = BlurViewActiveControl.BlurViewActiveControlCenter;
                this.startCenterPoint = actualCenterPoint;
            }
            setSelected(true, true);
        } else if (i != 2) {
            if (i == 3 || i == 4 || i == 5) {
                this.activeControl = BlurViewActiveControl.BlurViewActiveControlNone;
                setSelected(false, true);
            }
        } else {
            int i3 = this.type;
            if (i3 == 0) {
                int i4 = 1.$SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl[this.activeControl.ordinal()];
                if (i4 == 1) {
                    float f13 = x - this.pointerStartX;
                    float f14 = y - this.pointerStartY;
                    float width = (getWidth() - this.actualAreaSize.width) / 2.0f;
                    if (Build.VERSION.SDK_INT >= 21 && !this.inBubbleMode) {
                        r6 = AndroidUtilities.statusBarHeight;
                    }
                    Size size2 = this.actualAreaSize;
                    float f15 = size2.height;
                    Rect rect = new Rect(width, r6 + ((getHeight() - f15) / 2.0f), size2.width, f15);
                    float f16 = rect.x;
                    float max = Math.max(f16, Math.min(rect.width + f16, this.startCenterPoint.x + f13));
                    float f17 = rect.y;
                    Point point2 = new Point(max, Math.max(f17, Math.min(rect.height + f17, this.startCenterPoint.y + f14)));
                    float f18 = point2.x - rect.x;
                    Size size3 = this.actualAreaSize;
                    float f19 = size3.width;
                    point = new Point(f18 / f19, ((point2.y - rect.y) + ((f19 - size3.height) / 2.0f)) / f19);
                    this.centerPoint = point;
                } else if (i4 == 2) {
                    f = this.startRadius + (abs - this.startDistance);
                    this.falloff = Math.min(Math.max(0.1f, f / min), this.size - 0.02f);
                } else if (i4 == 3) {
                    float f20 = abs - this.startDistance;
                    f2 = this.falloff + 0.02f;
                    f3 = this.startRadius + f20;
                    this.size = Math.max(f2, f3 / min);
                } else if (i4 == 4) {
                    float f21 = x - this.pointerStartX;
                    float f22 = y - this.pointerStartY;
                    boolean z = x > actualCenterPoint.x;
                    boolean z2 = y > actualCenterPoint.y;
                    boolean z3 = Math.abs(f22) > Math.abs(f21);
                    if (z || z2 ? !(!z || z2 ? !z || !z2 ? !z3 ? f21 >= 0.0f : f22 >= 0.0f : !z3 ? f21 >= 0.0f : f22 <= 0.0f : !z3 ? f21 <= 0.0f : f22 <= 0.0f) : !(!z3 ? f21 <= 0.0f : f22 >= 0.0f)) {
                        r6 = 1;
                    }
                    this.angle += ((((float) Math.sqrt((f21 * f21) + (f22 * f22))) * ((r6 * 2) - 1)) / 3.1415927f) / 1.15f;
                    this.pointerStartX = x;
                    this.pointerStartY = y;
                }
            } else if (i3 == 1) {
                int i5 = 1.$SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl[this.activeControl.ordinal()];
                if (i5 == 1) {
                    float f23 = x - this.pointerStartX;
                    float f24 = y - this.pointerStartY;
                    float width2 = (getWidth() - this.actualAreaSize.width) / 2.0f;
                    if (Build.VERSION.SDK_INT >= 21 && !this.inBubbleMode) {
                        r6 = AndroidUtilities.statusBarHeight;
                    }
                    Size size4 = this.actualAreaSize;
                    float f25 = size4.height;
                    Rect rect2 = new Rect(width2, r6 + ((getHeight() - f25) / 2.0f), size4.width, f25);
                    float f26 = rect2.x;
                    float max2 = Math.max(f26, Math.min(rect2.width + f26, this.startCenterPoint.x + f23));
                    float f27 = rect2.y;
                    Point point3 = new Point(max2, Math.max(f27, Math.min(rect2.height + f27, this.startCenterPoint.y + f24)));
                    float f28 = point3.x - rect2.x;
                    Size size5 = this.actualAreaSize;
                    float f29 = size5.width;
                    point = new Point(f28 / f29, ((point3.y - rect2.y) + ((f29 - size5.height) / 2.0f)) / f29);
                    this.centerPoint = point;
                } else if (i5 == 2) {
                    f = this.startRadius + (sqrt - this.startDistance);
                    this.falloff = Math.min(Math.max(0.1f, f / min), this.size - 0.02f);
                } else if (i5 == 3) {
                    float f30 = sqrt - this.startDistance;
                    f2 = this.falloff + 0.02f;
                    f3 = this.startRadius + f30;
                    this.size = Math.max(f2, f3 / min);
                }
            }
            invalidate();
            PhotoFilterLinearBlurControlDelegate photoFilterLinearBlurControlDelegate = this.delegate;
            if (photoFilterLinearBlurControlDelegate != null) {
                photoFilterLinearBlurControlDelegate.valueChanged(this.centerPoint, this.falloff, this.size, degreesToRadians(this.angle) + 1.5707964f);
            }
        }
    }

    private void handlePinch(int i, MotionEvent motionEvent) {
        if (i == 1) {
            this.startPointerDistance = getDistance(motionEvent);
            this.pointerScale = 1.0f;
            this.activeControl = BlurViewActiveControl.BlurViewActiveControlWholeArea;
            setSelected(true, true);
        } else if (i != 2) {
            if (i == 3 || i == 4 || i == 5) {
                this.activeControl = BlurViewActiveControl.BlurViewActiveControlNone;
                setSelected(false, true);
                return;
            }
            return;
        }
        float distance = getDistance(motionEvent);
        float f = this.pointerScale + (((distance - this.startPointerDistance) / AndroidUtilities.density) * 0.01f);
        this.pointerScale = f;
        float max = Math.max(0.1f, this.falloff * f);
        this.falloff = max;
        this.size = Math.max(max + 0.02f, this.size * this.pointerScale);
        this.pointerScale = 1.0f;
        this.startPointerDistance = distance;
        invalidate();
        PhotoFilterLinearBlurControlDelegate photoFilterLinearBlurControlDelegate = this.delegate;
        if (photoFilterLinearBlurControlDelegate != null) {
            photoFilterLinearBlurControlDelegate.valueChanged(this.centerPoint, this.falloff, this.size, degreesToRadians(this.angle) + 1.5707964f);
        }
    }

    private void setSelected(boolean z, boolean z2) {
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        super.onDraw(canvas);
        Point actualCenterPoint = getActualCenterPoint();
        float actualInnerRadius = getActualInnerRadius();
        float actualOuterRadius = getActualOuterRadius();
        canvas.translate(actualCenterPoint.x, actualCenterPoint.y);
        int i2 = this.type;
        if (i2 == 0) {
            canvas.rotate(this.angle);
            float dp = AndroidUtilities.dp(6.0f);
            float dp2 = AndroidUtilities.dp(12.0f);
            float dp3 = AndroidUtilities.dp(1.5f);
            for (int i3 = 0; i3 < 30; i3++) {
                float f = dp2 + dp;
                float f2 = i3 * f;
                float f3 = -actualInnerRadius;
                float f4 = f2 + dp2;
                float f5 = dp3 - actualInnerRadius;
                canvas.drawRect(f2, f3, f4, f5, this.paint);
                float f6 = ((-i) * f) - dp;
                float f7 = f6 - dp2;
                canvas.drawRect(f7, f3, f6, f5, this.paint);
                float f8 = dp3 + actualInnerRadius;
                canvas.drawRect(f2, actualInnerRadius, f4, f8, this.paint);
                canvas.drawRect(f7, actualInnerRadius, f6, f8, this.paint);
            }
            float dp4 = AndroidUtilities.dp(6.0f);
            for (int i4 = 0; i4 < 64; i4++) {
                float f9 = dp4 + dp;
                float f10 = i4 * f9;
                float f11 = -actualOuterRadius;
                float f12 = dp4 + f10;
                float f13 = dp3 - actualOuterRadius;
                canvas.drawRect(f10, f11, f12, f13, this.paint);
                float f14 = ((-i4) * f9) - dp;
                float f15 = f14 - dp4;
                canvas.drawRect(f15, f11, f14, f13, this.paint);
                float f16 = dp3 + actualOuterRadius;
                canvas.drawRect(f10, actualOuterRadius, f12, f16, this.paint);
                canvas.drawRect(f15, actualOuterRadius, f14, f16, this.paint);
            }
        } else if (i2 == 1) {
            float f17 = -actualInnerRadius;
            this.arcRect.set(f17, f17, actualInnerRadius, actualInnerRadius);
            for (int i5 = 0; i5 < 22; i5++) {
                canvas.drawArc(this.arcRect, 16.35f * i5, 10.2f, false, this.arcPaint);
            }
            float f18 = -actualOuterRadius;
            this.arcRect.set(f18, f18, actualOuterRadius, actualOuterRadius);
            for (int i6 = 0; i6 < 64; i6++) {
                canvas.drawArc(this.arcRect, 5.62f * i6, 3.6f, false, this.arcPaint);
            }
        }
        canvas.drawCircle(0.0f, 0.0f, AndroidUtilities.dp(8.0f), this.paint);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0018, code lost:
        if (r2 != 6) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00ed, code lost:
        if (r7 < (r3 + r10)) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00fa, code lost:
        if (r7 < (r4 + r2)) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0104, code lost:
        if (r7 >= (r4 + r2)) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x011b, code lost:
        if (r2 < (r3 + r10)) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0127, code lost:
        if (r2 < (r4 + r7)) goto L58;
     */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0131  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        if (actionMasked != 5) {
                        }
                    }
                } else if (this.isMoving) {
                    handlePan(2, motionEvent);
                } else if (this.isZooming) {
                    handlePinch(2, motionEvent);
                }
                return true;
            }
            if (this.isMoving) {
                handlePan(3, motionEvent);
                this.isMoving = false;
            } else if (this.isZooming) {
                handlePinch(3, motionEvent);
                this.isZooming = false;
            }
            this.checkForMoving = true;
            this.checkForZooming = true;
            return true;
        }
        if (motionEvent.getPointerCount() != 1) {
            if (this.isMoving) {
                handlePan(3, motionEvent);
                this.checkForMoving = true;
                this.isMoving = false;
            }
            if (motionEvent.getPointerCount() != 2) {
                handlePinch(3, motionEvent);
                this.checkForZooming = true;
                this.isZooming = false;
            } else if (this.checkForZooming && !this.isZooming) {
                handlePinch(1, motionEvent);
                this.isZooming = true;
            }
        } else if (this.checkForMoving && !this.isMoving) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            Point actualCenterPoint = getActualCenterPoint();
            Point point = new Point(x - actualCenterPoint.x, y - actualCenterPoint.y);
            float f = point.x;
            float f2 = point.y;
            float sqrt = (float) Math.sqrt((f * f) + (f2 * f2));
            float actualInnerRadius = getActualInnerRadius();
            float actualOuterRadius = getActualOuterRadius();
            boolean z = Math.abs(actualOuterRadius - actualInnerRadius) < BlurInsetProximity;
            float f3 = z ? 0.0f : BlurViewRadiusInset;
            float f4 = z ? 0.0f : BlurViewRadiusInset;
            int i = this.type;
            if (i == 0) {
                double d = point.x;
                double degreesToRadians = degreesToRadians(this.angle);
                Double.isNaN(degreesToRadians);
                double cos = Math.cos(degreesToRadians + 1.5707963267948966d);
                Double.isNaN(d);
                double d2 = d * cos;
                double d3 = point.y;
                double degreesToRadians2 = degreesToRadians(this.angle);
                Double.isNaN(degreesToRadians2);
                double sin = Math.sin(degreesToRadians2 + 1.5707963267948966d);
                Double.isNaN(d3);
                float abs = (float) Math.abs(d2 + (d3 * sin));
                if (sqrt >= BlurViewCenterInset) {
                    float f5 = BlurViewRadiusInset;
                    float f6 = actualInnerRadius - f5;
                    if (abs > f6) {
                    }
                    if (abs > actualOuterRadius - f4) {
                    }
                    if (abs > f6) {
                    }
                }
                this.isMoving = true;
                this.checkForMoving = false;
                if (this.isMoving) {
                    handlePan(1, motionEvent);
                }
            } else {
                if (i == 1) {
                    if (sqrt >= BlurViewCenterInset) {
                        float f7 = BlurViewRadiusInset;
                        if (sqrt > actualInnerRadius - f7) {
                        }
                        if (sqrt > actualOuterRadius - f4) {
                        }
                    }
                    this.isMoving = true;
                }
                this.checkForMoving = false;
                if (this.isMoving) {
                }
            }
        }
        return true;
    }

    public void setActualAreaSize(float f, float f2) {
        Size size = this.actualAreaSize;
        size.width = f;
        size.height = f2;
    }

    public void setDelegate(PhotoFilterLinearBlurControlDelegate photoFilterLinearBlurControlDelegate) {
        this.delegate = photoFilterLinearBlurControlDelegate;
    }

    public void setType(int i) {
        this.type = i;
        invalidate();
    }
}
