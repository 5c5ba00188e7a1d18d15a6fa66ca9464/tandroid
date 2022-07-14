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
import j$.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Components.Premium.CarouselView;
/* loaded from: classes5.dex */
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
    Comparator<DrawingObject> comparator = Comparator.CC.comparingInt(CarouselView$$ExternalSyntheticLambda2.INSTANCE);
    private Runnable autoScrollRunnable = new Runnable() { // from class: org.telegram.ui.Components.Premium.CarouselView.1
        @Override // java.lang.Runnable
        public void run() {
            if (!CarouselView.this.autoPlayEnabled) {
                return;
            }
            CarouselView carouselView = CarouselView.this;
            carouselView.scrollToInternal(carouselView.offsetAngle + (360.0f / CarouselView.this.drawingObjects.size()));
        }
    };
    OverScroller overScroller = new OverScroller(getContext(), sQuinticInterpolator);

    public static /* synthetic */ float lambda$static$0(float t) {
        float t2 = t - 1.0f;
        return (t2 * t2 * t2 * t2 * t2) + 1.0f;
    }

    public static /* synthetic */ int lambda$new$1(DrawingObject value) {
        return (int) (value.yRelative * 100.0f);
    }

    public CarouselView(Context context, final ArrayList<? extends DrawingObject> drawingObjects) {
        super(context);
        this.gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() { // from class: org.telegram.ui.Components.Premium.CarouselView.2
            double lastAngle;

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
                if (CarouselView.this.autoScrollAnimation != null) {
                    CarouselView.this.autoScrollAnimation.removeAllListeners();
                    CarouselView.this.autoScrollAnimation.cancel();
                    CarouselView.this.autoScrollAnimation = null;
                }
                AndroidUtilities.cancelRunOnUIThread(CarouselView.this.autoScrollRunnable);
                CarouselView.this.overScroller.abortAnimation();
                this.lastAngle = Math.atan2(motionEvent.getX() - CarouselView.this.cX, motionEvent.getY() - CarouselView.this.cY);
                float aStep = 360.0f / drawingObjects.size();
                CarouselView carouselView = CarouselView.this;
                carouselView.lastSelected = (int) (carouselView.offsetAngle / aStep);
                for (int i = 0; i < drawingObjects.size(); i++) {
                    ((DrawingObject) drawingObjects.get(i)).hideAnimation();
                }
                return true;
            }

            @Override // android.view.GestureDetector.OnGestureListener
            public void onShowPress(MotionEvent motionEvent) {
            }

            @Override // android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                for (int i = CarouselView.this.drawingObjectsSorted.size() - 1; i >= 0; i--) {
                    if (((DrawingObject) CarouselView.this.drawingObjectsSorted.get(i)).checkTap(x, y)) {
                        if (((DrawingObject) CarouselView.this.drawingObjectsSorted.get(i)).angle % 360.0d != 270.0d) {
                            double toAngle = ((270.0d - (((DrawingObject) CarouselView.this.drawingObjectsSorted.get(i)).angle % 360.0d)) + 180.0d) % 360.0d;
                            if (toAngle > 180.0d) {
                                toAngle = -(360.0d - toAngle);
                            }
                            CarouselView carouselView = CarouselView.this;
                            carouselView.scrollToInternal(carouselView.offsetAngle + ((float) toAngle));
                            CarouselView.this.performHapticFeedback(3);
                        }
                        return true;
                    }
                }
                return false;
            }

            @Override // android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float dx, float dy) {
                double angle = Math.atan2(motionEvent1.getX() - CarouselView.this.cX, motionEvent1.getY() - CarouselView.this.cY);
                double dAngle = this.lastAngle - angle;
                this.lastAngle = angle;
                CarouselView carouselView = CarouselView.this;
                double d = carouselView.offsetAngle;
                double degrees = Math.toDegrees(dAngle);
                Double.isNaN(d);
                carouselView.offsetAngle = (float) (d + degrees);
                CarouselView.this.checkSelectedHaptic();
                CarouselView.this.invalidate();
                return true;
            }

            @Override // android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent motionEvent) {
            }

            @Override // android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velocityX, float velocityY) {
                CarouselView carouselView = CarouselView.this;
                carouselView.lastFlingY = 0.0f;
                carouselView.lastFlingX = 0.0f;
                double angle = Math.atan2(motionEvent1.getX() - CarouselView.this.cX, motionEvent1.getY() - CarouselView.this.cY);
                double cos = Math.cos(angle);
                double d = velocityX;
                Double.isNaN(d);
                double d2 = cos * d;
                double sin = Math.sin(angle);
                double d3 = velocityY;
                Double.isNaN(d3);
                float xVelocity = (float) (d2 - (sin * d3));
                CarouselView.this.overScroller.fling(0, 0, (int) xVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
                if (CarouselView.this.overScroller.isFinished()) {
                    CarouselView.this.scheduleAutoscroll();
                }
                CarouselView.this.invalidate();
                return true;
            }
        });
        this.drawingObjects = drawingObjects;
        this.drawingObjectsSorted = new ArrayList<>(drawingObjects);
        for (int i = 0; i < drawingObjects.size() / 2; i++) {
            drawingObjects.get(i).y = drawingObjects.size() / i;
            drawingObjects.get((drawingObjects.size() - 1) - i).y = drawingObjects.size() / i;
        }
        Collections.sort(drawingObjects, this.comparator);
        for (int i2 = 0; i2 < drawingObjects.size(); i2++) {
            drawingObjects.get(i2).carouselView = this;
        }
    }

    public void checkSelectedHaptic() {
        float aStep = 360.0f / this.drawingObjects.size();
        int selected = (int) (this.offsetAngle / aStep);
        if (this.lastSelected != selected) {
            this.lastSelected = selected;
            performHapticFeedback(3);
        }
    }

    public void scrollToInternal(final float scrollTo) {
        if (Math.abs(scrollTo - this.offsetAngle) < 1.0f && this.autoScrollAnimation == null) {
            return;
        }
        AndroidUtilities.cancelRunOnUIThread(this.autoScrollRunnable);
        ValueAnimator valueAnimator = this.autoScrollAnimation;
        if (valueAnimator != null) {
            valueAnimator.removeAllListeners();
            this.autoScrollAnimation.cancel();
            this.autoScrollAnimation = null;
        }
        final float from = this.offsetAngle;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.autoScrollAnimation = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Premium.CarouselView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                CarouselView.this.m2878x2d4b5a8(from, scrollTo, valueAnimator2);
            }
        });
        this.autoScrollAnimation.addListener(new AnonymousClass3(scrollTo));
        this.autoScrollAnimation.setInterpolator(new OvershootInterpolator());
        this.autoScrollAnimation.setDuration(600L);
        this.autoScrollAnimation.start();
    }

    /* renamed from: lambda$scrollToInternal$2$org-telegram-ui-Components-Premium-CarouselView */
    public /* synthetic */ void m2878x2d4b5a8(float from, float scrollTo, ValueAnimator animation) {
        float f = ((Float) animation.getAnimatedValue()).floatValue();
        this.offsetAngle = ((1.0f - f) * from) + (scrollTo * f);
        invalidate();
    }

    /* renamed from: org.telegram.ui.Components.Premium.CarouselView$3 */
    /* loaded from: classes5.dex */
    public class AnonymousClass3 extends AnimatorListenerAdapter {
        final /* synthetic */ float val$scrollTo;

        AnonymousClass3(float f) {
            CarouselView.this = this$0;
            this.val$scrollTo = f;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animation) {
            CarouselView.this.offsetAngle = this.val$scrollTo;
            CarouselView.this.autoScrollAnimation = null;
            CarouselView.this.invalidate();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Premium.CarouselView$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    CarouselView.AnonymousClass3.this.m2879xec6166a6();
                }
            });
        }

        /* renamed from: lambda$onAnimationEnd$0$org-telegram-ui-Components-Premium-CarouselView$3 */
        public /* synthetic */ void m2879xec6166a6() {
            if (!CarouselView.this.drawingObjectsSorted.isEmpty()) {
                ((DrawingObject) CarouselView.this.drawingObjectsSorted.get(CarouselView.this.drawingObjectsSorted.size() - 1)).select();
            }
            CarouselView.this.scheduleAutoscroll();
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            this.scrolled = true;
        } else if (event.getAction() == 1 || event.getAction() == 3) {
            this.scrolled = false;
            getParent().requestDisallowInterceptTouchEvent(false);
            invalidate();
        }
        return this.gestureDetector.onTouchEvent(event);
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.cX = getMeasuredWidth() >> 1;
        this.cY = getMeasuredHeight() >> 1;
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < this.drawingObjectsSorted.size(); i++) {
                this.drawingObjectsSorted.get(i).onAttachToWindow(this, k);
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
        if (java.lang.Math.abs(r7 % r2) > 2.0d) goto L20;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        double size = this.drawingObjects.size();
        Double.isNaN(size);
        double aStep = 360.0d / size;
        if (this.overScroller.computeScrollOffset()) {
            int x = this.overScroller.getCurrX();
            float f = this.lastFlingX;
            float dx = f - x;
            if (f != 0.0f && Math.abs(dx * 0.08f) < 0.3f) {
                this.overScroller.abortAnimation();
            }
            this.lastFlingX = x;
            this.offsetAngle += 0.08f * dx;
            checkSelectedHaptic();
            invalidate();
            scheduleAutoscroll();
        } else {
            if (!this.firstScroll1 && !this.firstScroll) {
                if (!this.scrolled && this.autoScrollAnimation == null) {
                    double d = this.offsetAngle - 90.0f;
                    Double.isNaN(d);
                }
            }
            if (this.firstScroll1) {
                double d2 = this.offsetAngle;
                Double.isNaN(d2);
                this.offsetAngle = (float) (d2 + 90.0d + aStep);
            }
            double d3 = this.offsetAngle - 90.0f;
            Double.isNaN(d3);
            float dif = (float) (d3 % aStep);
            if (Math.abs(dif) > aStep / 2.0d) {
                if (dif < 0.0f) {
                    double d4 = dif;
                    Double.isNaN(d4);
                    dif = (float) (d4 + aStep);
                } else {
                    double d5 = dif;
                    Double.isNaN(d5);
                    dif = (float) (d5 - aStep);
                }
            }
            this.firstScroll1 = false;
            if (this.firstScroll && this.firstScrollEnabled) {
                this.firstScroll = false;
                float f2 = this.offsetAngle - 180.0f;
                this.offsetAngle = f2;
                scrollToInternal((f2 - dif) + 180.0f);
            } else {
                scrollToInternal(this.offsetAngle - dif);
            }
        }
        float r = (Math.min(getMeasuredWidth(), getMeasuredHeight() * 1.3f) - AndroidUtilities.dp(140.0f)) * 0.5f;
        float rY = 0.6f * r;
        for (int i = 0; i < this.drawingObjects.size(); i++) {
            DrawingObject object = this.drawingObjects.get(i);
            double d6 = this.offsetAngle;
            double d7 = i;
            Double.isNaN(d7);
            Double.isNaN(d6);
            object.angle = d6 + (d7 * aStep);
            double s = Math.cos(Math.toRadians(object.angle));
            double totalAngle = object.angle - (30.0d * s);
            object.x = (((float) Math.cos(Math.toRadians(totalAngle))) * r) + this.cX;
            object.yRelative = (float) Math.sin(Math.toRadians(totalAngle));
            object.y = (object.yRelative * rY) + this.cY;
        }
        Collections.sort(this.drawingObjectsSorted, this.comparator);
        for (int i2 = 0; i2 < this.drawingObjectsSorted.size(); i2++) {
            DrawingObject object2 = this.drawingObjectsSorted.get(i2);
            float s2 = (((object2.yRelative + 1.0f) * 0.7f) / 2.0f) + 0.2f;
            object2.draw(canvas, object2.x, object2.y, s2);
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
    public void setOffset(float translationX) {
        boolean z = true;
        if (translationX >= getMeasuredWidth() || translationX <= (-getMeasuredWidth())) {
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
        setAutoPlayEnabled(translationX == 0.0f);
        if (Math.abs(translationX) >= getMeasuredWidth() * 0.2f) {
            z = false;
        }
        setFirstScrollEnabled(z);
        float s = Utilities.clamp(Math.abs(translationX) / getMeasuredWidth(), 1.0f, 0.0f);
        setScaleX(1.0f - s);
        setScaleY(1.0f - s);
    }

    public void autoplayToNext() {
        ArrayList<? extends DrawingObject> arrayList;
        AndroidUtilities.cancelRunOnUIThread(this.autoScrollRunnable);
        if (!this.autoPlayEnabled) {
            return;
        }
        DrawingObject drawingObject = this.drawingObjectsSorted.get(arrayList.size() - 1);
        int i = this.drawingObjects.indexOf(drawingObject) - 1;
        if (i < 0) {
            i = this.drawingObjects.size() - 1;
        }
        this.drawingObjects.get(i).select();
        AndroidUtilities.runOnUIThread(this.autoScrollRunnable, 16L);
    }

    /* loaded from: classes5.dex */
    public static class DrawingObject {
        public double angle;
        CarouselView carouselView;
        public float x;
        public float y;
        float yRelative;

        public void onAttachToWindow(View parentView, int i) {
        }

        public void onDetachFromWindow() {
        }

        public void draw(Canvas canvas, float cX, float cY, float scale) {
        }

        public boolean checkTap(float x, float y) {
            return false;
        }

        public void select() {
        }

        public void hideAnimation() {
        }
    }

    void setAutoPlayEnabled(boolean autoPlayEnabled) {
        if (this.autoPlayEnabled != autoPlayEnabled) {
            this.autoPlayEnabled = autoPlayEnabled;
            if (autoPlayEnabled) {
                scheduleAutoscroll();
            } else {
                AndroidUtilities.cancelRunOnUIThread(this.autoScrollRunnable);
            }
            invalidate();
        }
    }

    void setFirstScrollEnabled(boolean b) {
        if (this.firstScrollEnabled != b) {
            this.firstScrollEnabled = b;
            invalidate();
        }
    }
}
