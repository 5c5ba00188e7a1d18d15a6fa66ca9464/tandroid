package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.PhotoFilterView;

/* loaded from: classes3.dex */
public class PhotoFilterCurvesControl extends View {
    private int activeSegment;
    private Rect actualArea;
    private boolean checkForMoving;
    private PhotoFilterView.CurvesToolValue curveValue;
    private PhotoFilterCurvesControlDelegate delegate;
    private boolean isMoving;
    private float lastX;
    private float lastY;
    private Paint paint;
    private Paint paintCurve;
    private Paint paintDash;
    private Path path;
    private TextPaint textPaint;

    public interface PhotoFilterCurvesControlDelegate {
        void valueChanged();
    }

    public PhotoFilterCurvesControl(Context context, PhotoFilterView.CurvesToolValue curvesToolValue) {
        super(context);
        this.activeSegment = 0;
        this.checkForMoving = true;
        this.actualArea = new Rect();
        this.paint = new Paint(1);
        this.paintDash = new Paint(1);
        this.paintCurve = new Paint(1);
        this.textPaint = new TextPaint(1);
        this.path = new Path();
        setWillNotDraw(false);
        this.curveValue = curvesToolValue;
        this.paint.setColor(-1711276033);
        this.paint.setStrokeWidth(AndroidUtilities.dp(1.0f));
        Paint paint = this.paint;
        Paint.Style style = Paint.Style.STROKE;
        paint.setStyle(style);
        this.paintDash.setColor(-1711276033);
        this.paintDash.setStrokeWidth(AndroidUtilities.dp(2.0f));
        this.paintDash.setStyle(style);
        this.paintCurve.setColor(-1);
        this.paintCurve.setStrokeWidth(AndroidUtilities.dp(2.0f));
        this.paintCurve.setStyle(style);
        this.textPaint.setColor(-4210753);
        this.textPaint.setTextSize(AndroidUtilities.dp(13.0f));
    }

    private void handlePan(int i, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (i == 1) {
            selectSegmentWithPoint(x);
            return;
        }
        if (i != 2) {
            if (i == 3 || i == 4 || i == 5) {
                unselectSegments();
                return;
            }
            return;
        }
        float min = Math.min(2.0f, (this.lastY - y) / 8.0f);
        PhotoFilterView.CurvesToolValue curvesToolValue = this.curveValue;
        int i2 = curvesToolValue.activeType;
        PhotoFilterView.CurvesValue curvesValue = i2 != 0 ? i2 != 1 ? i2 != 2 ? i2 != 3 ? null : curvesToolValue.blueCurve : curvesToolValue.greenCurve : curvesToolValue.redCurve : curvesToolValue.luminanceCurve;
        int i3 = this.activeSegment;
        if (i3 == 1) {
            curvesValue.blacksLevel = Math.max(0.0f, Math.min(100.0f, curvesValue.blacksLevel + min));
        } else if (i3 == 2) {
            curvesValue.shadowsLevel = Math.max(0.0f, Math.min(100.0f, curvesValue.shadowsLevel + min));
        } else if (i3 == 3) {
            curvesValue.midtonesLevel = Math.max(0.0f, Math.min(100.0f, curvesValue.midtonesLevel + min));
        } else if (i3 == 4) {
            curvesValue.highlightsLevel = Math.max(0.0f, Math.min(100.0f, curvesValue.highlightsLevel + min));
        } else if (i3 == 5) {
            curvesValue.whitesLevel = Math.max(0.0f, Math.min(100.0f, curvesValue.whitesLevel + min));
        }
        invalidate();
        PhotoFilterCurvesControlDelegate photoFilterCurvesControlDelegate = this.delegate;
        if (photoFilterCurvesControlDelegate != null) {
            photoFilterCurvesControlDelegate.valueChanged();
        }
        this.lastX = x;
        this.lastY = y;
    }

    private void selectSegmentWithPoint(float f) {
        if (this.activeSegment != 0) {
            return;
        }
        Rect rect = this.actualArea;
        this.activeSegment = (int) Math.floor(((f - rect.x) / (rect.width / 5.0f)) + 1.0f);
    }

    private void unselectSegments() {
        if (this.activeSegment == 0) {
            return;
        }
        this.activeSegment = 0;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        PhotoFilterView.CurvesValue curvesValue;
        float f = this.actualArea.width / 5.0f;
        for (int i = 0; i < 4; i++) {
            Rect rect = this.actualArea;
            float f2 = rect.x + f + (i * f);
            float f3 = rect.y;
            canvas.drawLine(f2, f3, f2, f3 + rect.height, this.paint);
        }
        Rect rect2 = this.actualArea;
        float f4 = rect2.x;
        float f5 = rect2.y;
        canvas.drawLine(f4, f5 + rect2.height, f4 + rect2.width, f5, this.paintDash);
        int i2 = this.curveValue.activeType;
        if (i2 == 0) {
            this.paintCurve.setColor(-1);
            curvesValue = this.curveValue.luminanceCurve;
        } else if (i2 == 1) {
            this.paintCurve.setColor(-1229492);
            curvesValue = this.curveValue.redCurve;
        } else if (i2 == 2) {
            this.paintCurve.setColor(-15667555);
            curvesValue = this.curveValue.greenCurve;
        } else if (i2 != 3) {
            curvesValue = null;
        } else {
            this.paintCurve.setColor(-13404165);
            curvesValue = this.curveValue.blueCurve;
        }
        int i3 = 0;
        while (i3 < 5) {
            String format = i3 != 0 ? i3 != 1 ? i3 != 2 ? i3 != 3 ? i3 != 4 ? "" : String.format(Locale.US, "%.2f", Float.valueOf(curvesValue.whitesLevel / 100.0f)) : String.format(Locale.US, "%.2f", Float.valueOf(curvesValue.highlightsLevel / 100.0f)) : String.format(Locale.US, "%.2f", Float.valueOf(curvesValue.midtonesLevel / 100.0f)) : String.format(Locale.US, "%.2f", Float.valueOf(curvesValue.shadowsLevel / 100.0f)) : String.format(Locale.US, "%.2f", Float.valueOf(curvesValue.blacksLevel / 100.0f));
            float measureText = this.textPaint.measureText(format);
            Rect rect3 = this.actualArea;
            canvas.drawText(format, rect3.x + ((f - measureText) / 2.0f) + (i3 * f), (rect3.y + rect3.height) - AndroidUtilities.dp(4.0f), this.textPaint);
            i3++;
        }
        float[] interpolateCurve = curvesValue.interpolateCurve();
        invalidate();
        this.path.reset();
        for (int i4 = 0; i4 < interpolateCurve.length / 2; i4++) {
            Path path = this.path;
            Rect rect4 = this.actualArea;
            float f6 = rect4.x;
            int i5 = i4 * 2;
            if (i4 == 0) {
                path.moveTo(f6 + (interpolateCurve[i5] * rect4.width), rect4.y + ((1.0f - interpolateCurve[i5 + 1]) * rect4.height));
            } else {
                path.lineTo(f6 + (interpolateCurve[i5] * rect4.width), rect4.y + ((1.0f - interpolateCurve[i5 + 1]) * rect4.height));
            }
        }
        canvas.drawPath(this.path, this.paintCurve);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0014, code lost:
    
        if (r0 != 6) goto L41;
     */
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
                }
                return true;
            }
            if (this.isMoving) {
                handlePan(3, motionEvent);
                this.isMoving = false;
            }
            this.checkForMoving = true;
            return true;
        }
        if (motionEvent.getPointerCount() == 1) {
            if (this.checkForMoving && !this.isMoving) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                this.lastX = x;
                this.lastY = y;
                Rect rect = this.actualArea;
                float f = rect.x;
                if (x >= f && x <= f + rect.width) {
                    float f2 = rect.y;
                    if (y >= f2 && y <= f2 + rect.height) {
                        this.isMoving = true;
                    }
                }
                this.checkForMoving = false;
                if (this.isMoving) {
                    handlePan(1, motionEvent);
                }
            }
        } else if (this.isMoving) {
            handlePan(3, motionEvent);
            this.checkForMoving = true;
            this.isMoving = false;
        }
        return true;
    }

    public void setActualArea(float f, float f2, float f3, float f4) {
        Rect rect = this.actualArea;
        rect.x = f;
        rect.y = f2;
        rect.width = f3;
        rect.height = f4;
    }

    public void setDelegate(PhotoFilterCurvesControlDelegate photoFilterCurvesControlDelegate) {
        this.delegate = photoFilterCurvesControlDelegate;
    }
}
