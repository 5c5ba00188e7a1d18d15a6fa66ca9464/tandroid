package org.telegram.ui.Components.Paint.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.math.MathUtils;
import androidx.core.view.GestureDetectorCompat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.Paint.RenderView;
import org.telegram.ui.Components.Paint.Swatch;
/* loaded from: classes3.dex */
public class PaintWeightChooserView extends View {
    private AnimatedFloat animatedMax;
    private AnimatedFloat animatedMin;
    private AnimatedFloat animatedWeight;
    private Paint backgroundPaint;
    private Paint colorPaint;
    private Swatch colorSwatch;
    private boolean drawCenter;
    private GestureDetectorCompat gestureDetector;
    private float hideProgress;
    private boolean isPanTransitionInProgress;
    private boolean isTouchInProgress;
    private boolean isViewHidden;
    private long lastUpdate;
    private float max;
    private float min;
    private Runnable onUpdate;
    private Path path;
    private RenderView renderView;
    private boolean showPreview;
    private float showProgress;
    private RectF touchRect;
    private ValueOverride valueOverride;

    /* loaded from: classes3.dex */
    public interface ValueOverride {
        float get();

        void set(float f);
    }

    public PaintWeightChooserView(Context context) {
        super(context);
        this.backgroundPaint = new Paint(1);
        this.colorPaint = new Paint(1);
        this.path = new Path();
        this.touchRect = new RectF();
        this.showPreview = true;
        this.animatedWeight = new AnimatedFloat(this);
        this.animatedMin = new AnimatedFloat(this);
        this.animatedMax = new AnimatedFloat(this);
        this.colorSwatch = new Swatch(-1, 1.0f, 0.016773745f);
        this.drawCenter = true;
        this.gestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() { // from class: org.telegram.ui.Components.Paint.Views.PaintWeightChooserView.1
            float startDeltaY;
            float startWeight;
            boolean startedY;

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                boolean contains = PaintWeightChooserView.this.touchRect.contains(motionEvent.getX(), motionEvent.getY());
                if (PaintWeightChooserView.this.isTouchInProgress != contains) {
                    PaintWeightChooserView.this.isTouchInProgress = contains;
                    PaintWeightChooserView.this.invalidate();
                    if (contains) {
                        this.startWeight = PaintWeightChooserView.this.valueOverride != null ? PaintWeightChooserView.this.valueOverride.get() : PaintWeightChooserView.this.colorSwatch.brushWeight;
                        this.startedY = false;
                    }
                }
                return PaintWeightChooserView.this.isTouchInProgress;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (PaintWeightChooserView.this.isTouchInProgress) {
                    if (!this.startedY) {
                        this.startDeltaY = motionEvent.getY() - motionEvent2.getY();
                        this.startedY = true;
                    }
                    float clamp = MathUtils.clamp(this.startWeight + ((((motionEvent.getY() - motionEvent2.getY()) - this.startDeltaY) / PaintWeightChooserView.this.touchRect.height()) * (PaintWeightChooserView.this.max - PaintWeightChooserView.this.min)), PaintWeightChooserView.this.min, PaintWeightChooserView.this.max);
                    if (PaintWeightChooserView.this.valueOverride != null) {
                        PaintWeightChooserView.this.valueOverride.set(clamp);
                    } else {
                        PaintWeightChooserView.this.colorSwatch.brushWeight = clamp;
                    }
                    PaintWeightChooserView.this.animatedWeight.set(clamp, true);
                    PaintWeightChooserView.this.onUpdate.run();
                    PaintWeightChooserView.this.invalidate();
                }
                return PaintWeightChooserView.this.isTouchInProgress;
            }
        });
        this.colorPaint.setColor(-1);
        this.colorPaint.setShadowLayer(AndroidUtilities.dp(4.0f), 0.0f, AndroidUtilities.dp(2.0f), 1342177280);
        this.backgroundPaint.setColor(1090519039);
        this.backgroundPaint.setShadowLayer(AndroidUtilities.dp(3.0f), 0.0f, AndroidUtilities.dp(1.0f), 637534208);
    }

    public void setShowPreview(boolean z) {
        this.showPreview = z;
        invalidate();
    }

    public void setValueOverride(ValueOverride valueOverride) {
        this.valueOverride = valueOverride;
        invalidate();
    }

    public void stopPanTransition() {
        this.isPanTransitionInProgress = false;
        invalidate();
    }

    public void setRenderView(RenderView renderView) {
        this.renderView = renderView;
    }

    public void setColorSwatch(Swatch swatch) {
        this.colorSwatch = swatch;
        invalidate();
    }

    public void setDrawCenter(boolean z) {
        this.drawCenter = z;
        invalidate();
    }

    public void setMinMax(float f, float f2) {
        this.min = f;
        this.max = f2;
        invalidate();
    }

    public void setOnUpdate(Runnable runnable) {
        this.onUpdate = runnable;
    }

    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = this.gestureDetector.onTouchEvent(motionEvent);
        if (motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 3) {
            this.isTouchInProgress = false;
            invalidate();
        }
        return onTouchEvent;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.isPanTransitionInProgress) {
            return;
        }
        int height = (int) (getHeight() * 0.3f);
        this.touchRect.set(0.0f, (getHeight() - height) / 2.0f, AndroidUtilities.dp(32.0f), (getHeight() + height) / 2.0f);
    }

    public void setViewHidden(boolean z) {
        this.isViewHidden = z;
        invalidate();
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x013e  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x01d9  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x021f  */
    /* JADX WARN: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        boolean z;
        super.onDraw(canvas);
        long min = Math.min(16L, System.currentTimeMillis() - this.lastUpdate);
        this.lastUpdate = System.currentTimeMillis();
        AnimatedFloat animatedFloat = this.animatedWeight;
        ValueOverride valueOverride = this.valueOverride;
        float f = animatedFloat.set(valueOverride != null ? valueOverride.get() : this.colorSwatch.brushWeight);
        float f2 = this.animatedMin.set(this.min);
        float f3 = this.animatedMax.set(this.max);
        boolean z2 = this.isViewHidden;
        if (z2) {
            float f4 = this.hideProgress;
            if (f4 != 1.0f) {
                this.hideProgress = Math.min(1.0f, f4 + (((float) min) / 200.0f));
                invalidate();
                z = this.isTouchInProgress;
                if (z) {
                    float f5 = this.showProgress;
                    if (f5 != 1.0f) {
                        this.showProgress = Math.min(1.0f, f5 + (((float) min) / 200.0f));
                        invalidate();
                        float height = this.touchRect.height();
                        int dp = AndroidUtilities.dp(16.0f);
                        int dp2 = AndroidUtilities.dp(3.0f);
                        int dp3 = AndroidUtilities.dp(3.0f);
                        this.path.rewind();
                        this.path.moveTo(0.0f, 0.0f);
                        RectF rectF = AndroidUtilities.rectTmp;
                        int i = -dp3;
                        int i2 = -dp;
                        rectF.set(AndroidUtilities.lerp(i, i2, this.showProgress), 0.0f, AndroidUtilities.lerp(dp3, dp, this.showProgress), AndroidUtilities.lerp(dp3, dp, this.showProgress) * 2);
                        this.path.arcTo(rectF, -90.0f, 90.0f);
                        this.path.lineTo(AndroidUtilities.lerp(dp3, dp2, this.showProgress), height);
                        rectF.set(AndroidUtilities.lerp(i, -dp2, this.showProgress), height - (dp2 * 2), AndroidUtilities.lerp(dp3, dp2, this.showProgress), height);
                        this.path.arcTo(rectF, 0.0f, 180.0f);
                        float f6 = dp;
                        this.path.lineTo(AndroidUtilities.lerp(i, i2, this.showProgress), f6);
                        rectF.set(AndroidUtilities.lerp(i, i2, this.showProgress), 0.0f, AndroidUtilities.lerp(dp3, dp, this.showProgress), dp * 2);
                        this.path.arcTo(rectF, -180.0f, 90.0f);
                        this.path.close();
                        if (this.hideProgress != 0.0f) {
                            rectF.set(0.0f, 0.0f, getWidth(), getHeight());
                            canvas.saveLayerAlpha(rectF, (int) ((1.0f - this.hideProgress) * 255.0f), 31);
                        }
                        canvas.save();
                        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
                        canvas.translate(AndroidUtilities.dp(32.0f) * cubicBezierInterpolator.getInterpolation(this.showProgress), this.touchRect.top);
                        canvas.drawPath(this.path, this.backgroundPaint);
                        canvas.restore();
                        float dp4 = AndroidUtilities.dp(32.0f) * cubicBezierInterpolator.getInterpolation(this.showProgress);
                        RectF rectF2 = this.touchRect;
                        float f7 = (f - f2) / (f3 - f2);
                        float height2 = rectF2.top + (rectF2.height() * (1.0f - f7));
                        RectF rectF3 = this.touchRect;
                        float f8 = dp2 * 1.5f;
                        drawCircleWithShadow(canvas, dp4, MathUtils.clamp(height2, rectF3.top + f6, rectF3.bottom - Math.min(f8, f6)), AndroidUtilities.lerp(AndroidUtilities.dp(12.0f), AndroidUtilities.lerp(Math.min(f8, f6), f6, f7), this.showProgress), false);
                        if (this.drawCenter && this.showProgress != 0.0f && this.showPreview) {
                            drawCircleWithShadow(canvas, getWidth() / 2.0f, getHeight() / 2.0f, this.renderView.brushWeightForSize(f) * this.renderView.getCurrentBrush().getScale() * this.renderView.getCurrentBrush().getPreviewScale(), true);
                        }
                        if (this.hideProgress != 0.0f) {
                            canvas.restore();
                            return;
                        }
                        return;
                    }
                }
                if (!z) {
                    float f9 = this.showProgress;
                    if (f9 != 0.0f) {
                        this.showProgress = Math.max(0.0f, f9 - (((float) min) / 200.0f));
                        invalidate();
                    }
                }
                float height3 = this.touchRect.height();
                int dp5 = AndroidUtilities.dp(16.0f);
                int dp22 = AndroidUtilities.dp(3.0f);
                int dp32 = AndroidUtilities.dp(3.0f);
                this.path.rewind();
                this.path.moveTo(0.0f, 0.0f);
                RectF rectF4 = AndroidUtilities.rectTmp;
                int i3 = -dp32;
                int i22 = -dp5;
                rectF4.set(AndroidUtilities.lerp(i3, i22, this.showProgress), 0.0f, AndroidUtilities.lerp(dp32, dp5, this.showProgress), AndroidUtilities.lerp(dp32, dp5, this.showProgress) * 2);
                this.path.arcTo(rectF4, -90.0f, 90.0f);
                this.path.lineTo(AndroidUtilities.lerp(dp32, dp22, this.showProgress), height3);
                rectF4.set(AndroidUtilities.lerp(i3, -dp22, this.showProgress), height3 - (dp22 * 2), AndroidUtilities.lerp(dp32, dp22, this.showProgress), height3);
                this.path.arcTo(rectF4, 0.0f, 180.0f);
                float f62 = dp5;
                this.path.lineTo(AndroidUtilities.lerp(i3, i22, this.showProgress), f62);
                rectF4.set(AndroidUtilities.lerp(i3, i22, this.showProgress), 0.0f, AndroidUtilities.lerp(dp32, dp5, this.showProgress), dp5 * 2);
                this.path.arcTo(rectF4, -180.0f, 90.0f);
                this.path.close();
                if (this.hideProgress != 0.0f) {
                }
                canvas.save();
                CubicBezierInterpolator cubicBezierInterpolator2 = CubicBezierInterpolator.DEFAULT;
                canvas.translate(AndroidUtilities.dp(32.0f) * cubicBezierInterpolator2.getInterpolation(this.showProgress), this.touchRect.top);
                canvas.drawPath(this.path, this.backgroundPaint);
                canvas.restore();
                float dp42 = AndroidUtilities.dp(32.0f) * cubicBezierInterpolator2.getInterpolation(this.showProgress);
                RectF rectF22 = this.touchRect;
                float f72 = (f - f2) / (f3 - f2);
                float height22 = rectF22.top + (rectF22.height() * (1.0f - f72));
                RectF rectF32 = this.touchRect;
                float f82 = dp22 * 1.5f;
                drawCircleWithShadow(canvas, dp42, MathUtils.clamp(height22, rectF32.top + f62, rectF32.bottom - Math.min(f82, f62)), AndroidUtilities.lerp(AndroidUtilities.dp(12.0f), AndroidUtilities.lerp(Math.min(f82, f62), f62, f72), this.showProgress), false);
                if (this.drawCenter) {
                    drawCircleWithShadow(canvas, getWidth() / 2.0f, getHeight() / 2.0f, this.renderView.brushWeightForSize(f) * this.renderView.getCurrentBrush().getScale() * this.renderView.getCurrentBrush().getPreviewScale(), true);
                }
                if (this.hideProgress != 0.0f) {
                }
            }
        }
        if (!z2) {
            float f10 = this.hideProgress;
            if (f10 != 0.0f) {
                this.hideProgress = Math.max(0.0f, f10 - (((float) min) / 200.0f));
                invalidate();
            }
        }
        z = this.isTouchInProgress;
        if (z) {
        }
        if (!z) {
        }
        float height32 = this.touchRect.height();
        int dp52 = AndroidUtilities.dp(16.0f);
        int dp222 = AndroidUtilities.dp(3.0f);
        int dp322 = AndroidUtilities.dp(3.0f);
        this.path.rewind();
        this.path.moveTo(0.0f, 0.0f);
        RectF rectF42 = AndroidUtilities.rectTmp;
        int i32 = -dp322;
        int i222 = -dp52;
        rectF42.set(AndroidUtilities.lerp(i32, i222, this.showProgress), 0.0f, AndroidUtilities.lerp(dp322, dp52, this.showProgress), AndroidUtilities.lerp(dp322, dp52, this.showProgress) * 2);
        this.path.arcTo(rectF42, -90.0f, 90.0f);
        this.path.lineTo(AndroidUtilities.lerp(dp322, dp222, this.showProgress), height32);
        rectF42.set(AndroidUtilities.lerp(i32, -dp222, this.showProgress), height32 - (dp222 * 2), AndroidUtilities.lerp(dp322, dp222, this.showProgress), height32);
        this.path.arcTo(rectF42, 0.0f, 180.0f);
        float f622 = dp52;
        this.path.lineTo(AndroidUtilities.lerp(i32, i222, this.showProgress), f622);
        rectF42.set(AndroidUtilities.lerp(i32, i222, this.showProgress), 0.0f, AndroidUtilities.lerp(dp322, dp52, this.showProgress), dp52 * 2);
        this.path.arcTo(rectF42, -180.0f, 90.0f);
        this.path.close();
        if (this.hideProgress != 0.0f) {
        }
        canvas.save();
        CubicBezierInterpolator cubicBezierInterpolator22 = CubicBezierInterpolator.DEFAULT;
        canvas.translate(AndroidUtilities.dp(32.0f) * cubicBezierInterpolator22.getInterpolation(this.showProgress), this.touchRect.top);
        canvas.drawPath(this.path, this.backgroundPaint);
        canvas.restore();
        float dp422 = AndroidUtilities.dp(32.0f) * cubicBezierInterpolator22.getInterpolation(this.showProgress);
        RectF rectF222 = this.touchRect;
        float f722 = (f - f2) / (f3 - f2);
        float height222 = rectF222.top + (rectF222.height() * (1.0f - f722));
        RectF rectF322 = this.touchRect;
        float f822 = dp222 * 1.5f;
        drawCircleWithShadow(canvas, dp422, MathUtils.clamp(height222, rectF322.top + f622, rectF322.bottom - Math.min(f822, f622)), AndroidUtilities.lerp(AndroidUtilities.dp(12.0f), AndroidUtilities.lerp(Math.min(f822, f622), f622, f722), this.showProgress), false);
        if (this.drawCenter) {
        }
        if (this.hideProgress != 0.0f) {
        }
    }

    private void drawCircleWithShadow(Canvas canvas, float f, float f2, float f3, boolean z) {
        if (z) {
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set((f - f3) - AndroidUtilities.dp(6.0f), (f2 - f3) - AndroidUtilities.dp(6.0f), f + f3 + AndroidUtilities.dp(6.0f), f2 + f3 + AndroidUtilities.dp(6.0f));
            canvas.saveLayerAlpha(rectF, (int) (this.showProgress * 255.0f), 31);
        }
        canvas.drawCircle(f, f2, f3, this.colorPaint);
        if (z) {
            canvas.restore();
        }
    }
}
