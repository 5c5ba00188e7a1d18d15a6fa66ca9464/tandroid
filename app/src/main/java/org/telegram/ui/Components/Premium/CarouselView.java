package org.telegram.ui.Components.Premium;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.OverScroller;
import j$.util.Comparator$CC;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Utilities;
/* loaded from: classes3.dex */
public class CarouselView extends View implements PagerHeaderView {
    static final Interpolator sQuinticInterpolator = CarouselView$$ExternalSyntheticLambda1.INSTANCE;
    ValueAnimator autoScrollAnimation;
    int cX;
    int cY;
    private final ArrayList<? extends DrawingObject> drawingObjects;
    private final ArrayList<? extends DrawingObject> drawingObjectsSorted;
    GestureDetector gestureDetector;
    float lastFlingX;
    float lastFlingY;
    int lastSelected;
    boolean scrolled;
    float offsetAngle = 0.0f;
    boolean firstScroll = true;
    boolean firstScroll1 = true;
    boolean firstScrollEnabled = true;
    boolean autoPlayEnabled = true;
    Comparator<DrawingObject> comparator = Comparator$CC.comparingInt(CarouselView$$ExternalSyntheticLambda2.INSTANCE);
    private Runnable autoScrollRunnable = new AnonymousClass1();
    OverScroller overScroller = new OverScroller(getContext(), sQuinticInterpolator);

    /* loaded from: classes3.dex */
    public static class DrawingObject {
        public double angle;
        CarouselView carouselView;
        public float x;
        public float y;
        float yRelative;

        public boolean checkTap(float f, float f2) {
            return false;
        }

        public void draw(Canvas canvas, float f, float f2, float f3) {
        }

        public void hideAnimation() {
        }

        public void onAttachToWindow(View view, int i) {
        }

        public void onDetachFromWindow() {
        }

        public void select() {
        }
    }

    public static /* synthetic */ float lambda$static$0(float f) {
        float f2 = f - 1.0f;
        return (f2 * f2 * f2 * f2 * f2) + 1.0f;
    }

    public static /* synthetic */ int lambda$new$1(DrawingObject drawingObject) {
        return (int) (drawingObject.yRelative * 100.0f);
    }

    /* renamed from: org.telegram.ui.Components.Premium.CarouselView$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
            CarouselView.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            CarouselView carouselView = CarouselView.this;
            if (!carouselView.autoPlayEnabled) {
                return;
            }
            carouselView.scrollToInternal(carouselView.offsetAngle + (360.0f / carouselView.drawingObjects.size()));
        }
    }

    public CarouselView(Context context, ArrayList<? extends DrawingObject> arrayList) {
        super(context);
        this.gestureDetector = new GestureDetector(context, new AnonymousClass2(arrayList));
        this.drawingObjects = arrayList;
        this.drawingObjectsSorted = new ArrayList<>(arrayList);
        for (int i = 0; i < arrayList.size() / 2; i++) {
            float f = i;
            arrayList.get(i).y = arrayList.size() / f;
            arrayList.get((arrayList.size() - 1) - i).y = arrayList.size() / f;
        }
        Collections.sort(arrayList, this.comparator);
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList.get(i2).carouselView = this;
        }
    }

    /* renamed from: org.telegram.ui.Components.Premium.CarouselView$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements GestureDetector.OnGestureListener {
        double lastAngle;
        final /* synthetic */ ArrayList val$drawingObjects;

        @Override // android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onShowPress(MotionEvent motionEvent) {
        }

        AnonymousClass2(ArrayList arrayList) {
            CarouselView.this = r1;
            this.val$drawingObjects = arrayList;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            double measuredHeight = CarouselView.this.getMeasuredHeight();
            Double.isNaN(measuredHeight);
            if (motionEvent.getY() > measuredHeight * 0.2d) {
                double measuredHeight2 = CarouselView.this.getMeasuredHeight();
                Double.isNaN(measuredHeight2);
                if (motionEvent.getY() < measuredHeight2 * 0.9d) {
                    CarouselView.this.getParent().requestDisallowInterceptTouchEvent(true);
                }
            }
            ValueAnimator valueAnimator = CarouselView.this.autoScrollAnimation;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                CarouselView.this.autoScrollAnimation.cancel();
                CarouselView.this.autoScrollAnimation = null;
            }
            AndroidUtilities.cancelRunOnUIThread(CarouselView.this.autoScrollRunnable);
            CarouselView.this.overScroller.abortAnimation();
            this.lastAngle = Math.atan2(motionEvent.getX() - CarouselView.this.cX, motionEvent.getY() - CarouselView.this.cY);
            CarouselView carouselView = CarouselView.this;
            carouselView.lastSelected = (int) (carouselView.offsetAngle / (360.0f / this.val$drawingObjects.size()));
            for (int i = 0; i < this.val$drawingObjects.size(); i++) {
                ((DrawingObject) this.val$drawingObjects.get(i)).hideAnimation();
            }
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            for (int size = CarouselView.this.drawingObjectsSorted.size() - 1; size >= 0; size--) {
                if (((DrawingObject) CarouselView.this.drawingObjectsSorted.get(size)).checkTap(x, y)) {
                    if (((DrawingObject) CarouselView.this.drawingObjectsSorted.get(size)).angle % 360.0d != 270.0d) {
                        double d = ((270.0d - (((DrawingObject) CarouselView.this.drawingObjectsSorted.get(size)).angle % 360.0d)) + 180.0d) % 360.0d;
                        if (d > 180.0d) {
                            d = -(360.0d - d);
                        }
                        CarouselView carouselView = CarouselView.this;
                        carouselView.scrollToInternal(carouselView.offsetAngle + ((float) d));
                        CarouselView.this.performHapticFeedback(3);
                    }
                    return true;
                }
            }
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            double atan2 = Math.atan2(motionEvent2.getX() - CarouselView.this.cX, motionEvent2.getY() - CarouselView.this.cY);
            double d = this.lastAngle - atan2;
            this.lastAngle = atan2;
            CarouselView carouselView = CarouselView.this;
            double d2 = carouselView.offsetAngle;
            double degrees = Math.toDegrees(d);
            Double.isNaN(d2);
            carouselView.offsetAngle = (float) (d2 + degrees);
            CarouselView.this.checkSelectedHaptic();
            CarouselView.this.invalidate();
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            double d;
            double d2;
            CarouselView carouselView = CarouselView.this;
            carouselView.lastFlingY = 0.0f;
            carouselView.lastFlingX = 0.0f;
            double atan2 = Math.atan2(motionEvent2.getX() - CarouselView.this.cX, motionEvent2.getY() - CarouselView.this.cY);
            double cos = Math.cos(atan2);
            Double.isNaN(f);
            double sin = Math.sin(atan2);
            Double.isNaN(f2);
            CarouselView.this.overScroller.fling(0, 0, (int) ((cos * d) - (sin * d2)), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (CarouselView.this.overScroller.isFinished()) {
                CarouselView.this.scheduleAutoscroll();
            }
            CarouselView.this.invalidate();
            return true;
        }
    }

    public void checkSelectedHaptic() {
        int size = (int) (this.offsetAngle / (360.0f / this.drawingObjects.size()));
        if (this.lastSelected != size) {
            this.lastSelected = size;
            performHapticFeedback(3);
        }
    }

    public void scrollToInternal(float f) {
        if (Math.abs(f - this.offsetAngle) >= 1.0f || this.autoScrollAnimation != null) {
            AndroidUtilities.cancelRunOnUIThread(this.autoScrollRunnable);
            ValueAnimator valueAnimator = this.autoScrollAnimation;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.autoScrollAnimation.cancel();
                this.autoScrollAnimation = null;
            }
            float f2 = this.offsetAngle;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.autoScrollAnimation = ofFloat;
            ofFloat.addUpdateListener(new CarouselView$$ExternalSyntheticLambda0(this, f2, f));
            this.autoScrollAnimation.addListener(new AnonymousClass3(f));
            this.autoScrollAnimation.setInterpolator(new OvershootInterpolator());
            this.autoScrollAnimation.setDuration(600L);
            this.autoScrollAnimation.start();
        }
    }

    public /* synthetic */ void lambda$scrollToInternal$2(float f, float f2, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.offsetAngle = (f * (1.0f - floatValue)) + (f2 * floatValue);
        invalidate();
    }

    /* renamed from: org.telegram.ui.Components.Premium.CarouselView$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends AnimatorListenerAdapter {
        final /* synthetic */ float val$scrollTo;

        AnonymousClass3(float f) {
            CarouselView.this = r1;
            this.val$scrollTo = f;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            CarouselView carouselView = CarouselView.this;
            carouselView.offsetAngle = this.val$scrollTo;
            carouselView.autoScrollAnimation = null;
            carouselView.invalidate();
            AndroidUtilities.runOnUIThread(new CarouselView$3$$ExternalSyntheticLambda0(this));
        }

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
        if (java.lang.Math.abs(r8 % r2) > 2.0d) goto L20;
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
        if (!this.autoPlayEnabled) {
            return;
        }
        AndroidUtilities.runOnUIThread(this.autoScrollRunnable, 3000L);
    }

    @Override // org.telegram.ui.Components.Premium.PagerHeaderView
    public void setOffset(float f) {
        boolean z = true;
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
        if (Math.abs(f) >= getMeasuredWidth() * 0.2f) {
            z = false;
        }
        setFirstScrollEnabled(z);
        float clamp = 1.0f - Utilities.clamp(Math.abs(f) / getMeasuredWidth(), 1.0f, 0.0f);
        setScaleX(clamp);
        setScaleY(clamp);
    }

    public void autoplayToNext() {
        ArrayList<? extends DrawingObject> arrayList;
        AndroidUtilities.cancelRunOnUIThread(this.autoScrollRunnable);
        if (!this.autoPlayEnabled) {
            return;
        }
        int indexOf = this.drawingObjects.indexOf(this.drawingObjectsSorted.get(arrayList.size() - 1)) - 1;
        if (indexOf < 0) {
            indexOf = this.drawingObjects.size() - 1;
        }
        this.drawingObjects.get(indexOf).select();
        AndroidUtilities.runOnUIThread(this.autoScrollRunnable, 16L);
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
