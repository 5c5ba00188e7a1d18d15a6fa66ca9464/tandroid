package com.google.android.exoplayer2.ui;

import android.content.Context;
import android.graphics.Matrix;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;

/* loaded from: classes.dex */
public class AspectRatioFrameLayout extends FrameLayout {
    private final AspectRatioUpdateDispatcher aspectRatioUpdateDispatcher;
    private boolean drawingReady;
    private Matrix matrix;
    private int resizeMode;
    private int rotation;
    private float videoAspectRatio;

    /* loaded from: classes.dex */
    public interface AspectRatioListener {
    }

    /* loaded from: classes.dex */
    private final class AspectRatioUpdateDispatcher implements Runnable {
        private boolean aspectRatioMismatch;
        private boolean isScheduled;
        private float naturalAspectRatio;
        private float targetAspectRatio;

        private AspectRatioUpdateDispatcher() {
        }

        @Override // java.lang.Runnable
        public void run() {
            this.isScheduled = false;
            AspectRatioFrameLayout.access$100(AspectRatioFrameLayout.this);
        }

        public void scheduleUpdate(float f, float f2, boolean z) {
            this.targetAspectRatio = f;
            this.naturalAspectRatio = f2;
            this.aspectRatioMismatch = z;
            if (this.isScheduled) {
                return;
            }
            this.isScheduled = true;
            AspectRatioFrameLayout.this.post(this);
        }
    }

    public AspectRatioFrameLayout(Context context) {
        super(context);
        this.matrix = new Matrix();
        this.resizeMode = 0;
        this.aspectRatioUpdateDispatcher = new AspectRatioUpdateDispatcher();
    }

    static /* synthetic */ AspectRatioListener access$100(AspectRatioFrameLayout aspectRatioFrameLayout) {
        aspectRatioFrameLayout.getClass();
        return null;
    }

    public float getAspectRatio() {
        return this.videoAspectRatio;
    }

    public int getResizeMode() {
        return this.resizeMode;
    }

    public int getVideoRotation() {
        return this.rotation;
    }

    public boolean isDrawingReady() {
        return this.drawingReady;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0044, code lost:
    
        if (r4 > 0.0f) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0049, code lost:
    
        if (r4 <= 0.0f) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x004e, code lost:
    
        if (r4 > 0.0f) goto L26;
     */
    @Override // android.widget.FrameLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.videoAspectRatio <= 0.0f) {
            return;
        }
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        float f = measuredWidth;
        float f2 = measuredHeight;
        float f3 = f / f2;
        float f4 = (this.videoAspectRatio / f3) - 1.0f;
        if (Math.abs(f4) <= 0.01f) {
            this.aspectRatioUpdateDispatcher.scheduleUpdate(this.videoAspectRatio, f3, false);
            return;
        }
        int i3 = this.resizeMode;
        if (i3 != 0) {
            if (i3 != 1) {
                if (i3 != 2) {
                    if (i3 != 3) {
                        if (i3 == 4) {
                        }
                    }
                }
                measuredWidth = (int) (f2 * this.videoAspectRatio);
            }
            measuredHeight = (int) (f / this.videoAspectRatio);
        }
        this.aspectRatioUpdateDispatcher.scheduleUpdate(this.videoAspectRatio, f3, true);
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(measuredHeight, 1073741824));
        int childCount = getChildCount();
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = getChildAt(i4);
            if (childAt instanceof TextureView) {
                this.matrix.reset();
                float width = getWidth() / 2;
                float height = getHeight() / 2;
                this.matrix.postRotate(this.rotation, width, height);
                int i5 = this.rotation;
                if (i5 == 90 || i5 == 270) {
                    float height2 = getHeight() / getWidth();
                    this.matrix.postScale(1.0f / height2, height2, width, height);
                }
                ((TextureView) childAt).setTransform(this.matrix);
                return;
            }
        }
    }

    public void setAspectRatio(float f, int i) {
        if (this.videoAspectRatio != f) {
            this.videoAspectRatio = f;
            this.rotation = i;
            requestLayout();
        }
    }

    public void setAspectRatioListener(AspectRatioListener aspectRatioListener) {
    }

    public void setDrawingReady(boolean z) {
        if (this.drawingReady == z) {
            return;
        }
        this.drawingReady = z;
    }

    public void setResizeMode(int i) {
        if (this.resizeMode != i) {
            this.resizeMode = i;
            requestLayout();
        }
    }
}
