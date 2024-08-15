package org.telegram.ui.Components.Premium;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.OverScroller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Components.Premium.CarouselView;
/* loaded from: classes3.dex */
public class CarouselView extends View implements PagerHeaderView {
    static final Interpolator sQuinticInterpolator = new Interpolator() { // from class: org.telegram.ui.Components.Premium.CarouselView$$ExternalSyntheticLambda1
        @Override // android.animation.TimeInterpolator
        public final float getInterpolation(float f) {
            float lambda$static$0;
            lambda$static$0 = CarouselView.lambda$static$0(f);
            return lambda$static$0;
        }
    };
    boolean autoPlayEnabled;
    ValueAnimator autoScrollAnimation;
    private Runnable autoScrollRunnable;
    int cX;
    int cY;
    Comparator<DrawingObject> comparator;
    private final ArrayList<? extends DrawingObject> drawingObjects;
    private final ArrayList<? extends DrawingObject> drawingObjectsSorted;
    boolean firstScroll;
    boolean firstScroll1;
    boolean firstScrollEnabled;
    GestureDetector gestureDetector;
    float lastFlingX;
    int lastSelected;
    float offsetAngle;
    OverScroller overScroller;
    boolean scrolled;

    /* loaded from: classes3.dex */
    public static class DrawingObject {
        public double angle;
        public float x;
        public float y;
        float yRelative;

        public void draw(Canvas canvas, float f, float f2, float f3) {
        }

        public void onAttachToWindow(View view, int i) {
        }

        public void onDetachFromWindow() {
        }

        public void select() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ float lambda$static$0(float f) {
        float f2 = f - 1.0f;
        return (f2 * f2 * f2 * f2 * f2) + 1.0f;
    }

    private void checkSelectedHaptic() {
        int size = (int) (this.offsetAngle / (360.0f / this.drawingObjects.size()));
        if (this.lastSelected != size) {
            this.lastSelected = size;
            performHapticFeedback(3);
        }
    }

    private void scrollToInternal(final float f) {
        if (Math.abs(f - this.offsetAngle) >= 1.0f || this.autoScrollAnimation != null) {
            AndroidUtilities.cancelRunOnUIThread(this.autoScrollRunnable);
            ValueAnimator valueAnimator = this.autoScrollAnimation;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.autoScrollAnimation.cancel();
                this.autoScrollAnimation = null;
            }
            final float f2 = this.offsetAngle;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.autoScrollAnimation = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Premium.CarouselView$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    CarouselView.this.lambda$scrollToInternal$2(f2, f, valueAnimator2);
                }
            });
            this.autoScrollAnimation.addListener(new 3(f));
            this.autoScrollAnimation.setInterpolator(new OvershootInterpolator());
            this.autoScrollAnimation.setDuration(600L);
            this.autoScrollAnimation.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scrollToInternal$2(float f, float f2, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.offsetAngle = (f * (1.0f - floatValue)) + (f2 * floatValue);
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 3 extends AnimatorListenerAdapter {
        final /* synthetic */ float val$scrollTo;

        3(float f) {
            this.val$scrollTo = f;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            CarouselView carouselView = CarouselView.this;
            carouselView.offsetAngle = this.val$scrollTo;
            carouselView.autoScrollAnimation = null;
            carouselView.invalidate();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Premium.CarouselView$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    CarouselView.3.this.lambda$onAnimationEnd$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$0() {
            if (!CarouselView.this.drawingObjectsSorted.isEmpty()) {
                ((DrawingObject) CarouselView.this.drawingObjectsSorted.get(CarouselView.this.drawingObjectsSorted.size() - 1)).select();
            }
            CarouselView.this.scheduleAutoscroll();
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.scrolled = true;
        } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            this.scrolled = false;
            getParent().requestDisallowInterceptTouchEvent(false);
            invalidate();
        }
        return this.gestureDetector.onTouchEvent(motionEvent);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.cX = getMeasuredWidth() >> 1;
        this.cY = getMeasuredHeight() >> 1;
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (int i = 0; i < 2; i++) {
            for (int i2 = 0; i2 < this.drawingObjectsSorted.size(); i2++) {
                this.drawingObjectsSorted.get(i2).onAttachToWindow(this, i);
            }
        }
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (int i = 0; i < this.drawingObjects.size(); i++) {
            this.drawingObjects.get(i).onDetachFromWindow();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0074, code lost:
        if (java.lang.Math.abs(r8 % r2) > 2.0d) goto L32;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        double d;
        double size = this.drawingObjects.size();
        Double.isNaN(size);
        double d2 = 360.0d / size;
        if (this.overScroller.computeScrollOffset()) {
            int currX = this.overScroller.getCurrX();
            float f = this.lastFlingX;
            float f2 = currX;
            float f3 = f - f2;
            if (f != 0.0f && Math.abs(f3 * 0.08f) < 0.3f) {
                this.overScroller.abortAnimation();
            }
            this.lastFlingX = f2;
            this.offsetAngle += f3 * 0.08f;
            checkSelectedHaptic();
            invalidate();
            scheduleAutoscroll();
        } else {
            if (!this.firstScroll1 && !this.firstScroll) {
                if (!this.scrolled && this.autoScrollAnimation == null) {
                    double d3 = this.offsetAngle - 90.0f;
                    Double.isNaN(d3);
                }
            }
            if (this.firstScroll1) {
                double d4 = this.offsetAngle;
                Double.isNaN(d4);
                this.offsetAngle = (float) (d4 + 90.0d + d2);
            }
            double d5 = this.offsetAngle - 90.0f;
            Double.isNaN(d5);
            float f4 = (float) (d5 % d2);
            if (Math.abs(f4) > d2 / 2.0d) {
                if (f4 < 0.0f) {
                    double d6 = f4;
                    Double.isNaN(d6);
                    d = d6 + d2;
                } else {
                    double d7 = f4;
                    Double.isNaN(d7);
                    d = d7 - d2;
                }
                f4 = (float) d;
            }
            this.firstScroll1 = false;
            if (this.firstScroll && this.firstScrollEnabled) {
                this.firstScroll = false;
                float f5 = this.offsetAngle - 180.0f;
                this.offsetAngle = f5;
                scrollToInternal((f5 - f4) + 180.0f);
            } else {
                scrollToInternal(this.offsetAngle - f4);
            }
        }
        float min = (Math.min(getMeasuredWidth(), getMeasuredHeight() * 1.3f) - AndroidUtilities.dp(140.0f)) * 0.5f;
        float f6 = 0.6f * min;
        for (int i = 0; i < this.drawingObjects.size(); i++) {
            DrawingObject drawingObject = this.drawingObjects.get(i);
            double d8 = this.offsetAngle;
            double d9 = i;
            Double.isNaN(d9);
            Double.isNaN(d8);
            double d10 = d8 + (d9 * d2);
            drawingObject.angle = d10;
            double cos = drawingObject.angle - (Math.cos(Math.toRadians(d10)) * 30.0d);
            drawingObject.x = (((float) Math.cos(Math.toRadians(cos))) * min) + this.cX;
            float sin = (float) Math.sin(Math.toRadians(cos));
            drawingObject.yRelative = sin;
            drawingObject.y = (sin * f6) + this.cY;
        }
        Collections.sort(this.drawingObjectsSorted, this.comparator);
        for (int i2 = 0; i2 < this.drawingObjectsSorted.size(); i2++) {
            DrawingObject drawingObject2 = this.drawingObjectsSorted.get(i2);
            drawingObject2.draw(canvas, drawingObject2.x, drawingObject2.y, (((drawingObject2.yRelative + 1.0f) * 0.7f) / 2.0f) + 0.2f);
        }
        invalidate();
    }

    void scheduleAutoscroll() {
        AndroidUtilities.cancelRunOnUIThread(this.autoScrollRunnable);
        if (this.autoPlayEnabled) {
            AndroidUtilities.runOnUIThread(this.autoScrollRunnable, 3000L);
        }
    }

    @Override // org.telegram.ui.Components.Premium.PagerHeaderView
    public void setOffset(float f) {
        if (f >= getMeasuredWidth() || f <= (-getMeasuredWidth())) {
            this.overScroller.abortAnimation();
            ValueAnimator valueAnimator = this.autoScrollAnimation;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.autoScrollAnimation.cancel();
                this.autoScrollAnimation = null;
            }
            this.firstScroll = true;
            this.firstScroll1 = true;
            this.offsetAngle = 0.0f;
        }
        setAutoPlayEnabled(f == 0.0f);
        setFirstScrollEnabled(Math.abs(f) < ((float) getMeasuredWidth()) * 0.2f);
        float clamp = 1.0f - Utilities.clamp(Math.abs(f) / getMeasuredWidth(), 1.0f, 0.0f);
        setScaleX(clamp);
        setScaleY(clamp);
    }

    void setAutoPlayEnabled(boolean z) {
        if (this.autoPlayEnabled != z) {
            this.autoPlayEnabled = z;
            if (z) {
                scheduleAutoscroll();
            } else {
                AndroidUtilities.cancelRunOnUIThread(this.autoScrollRunnable);
            }
            invalidate();
        }
    }

    void setFirstScrollEnabled(boolean z) {
        if (this.firstScrollEnabled != z) {
            this.firstScrollEnabled = z;
            invalidate();
        }
    }
}
