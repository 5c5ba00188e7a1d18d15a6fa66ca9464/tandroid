package org.telegram.ui.Components;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import org.telegram.messenger.NotificationCenter;
/* loaded from: classes3.dex */
public class GestureDetector2 {
    private boolean mAlwaysInBiggerTapRegion;
    private boolean mAlwaysInTapRegion;
    private MotionEvent mCurrentDownEvent;
    private MotionEvent mCurrentMotionEvent;
    private boolean mDeferConfirmSingleTap;
    private OnDoubleTapListener mDoubleTapListener;
    private int mDoubleTapSlopSquare;
    private int mDoubleTapTouchSlopSquare;
    private float mDownFocusX;
    private float mDownFocusY;
    private final Handler mHandler;
    private boolean mIgnoreNextUpEvent;
    private boolean mInContextClick;
    private boolean mInLongPress;
    private boolean mIsDoubleTapping;
    private boolean mIsLongpressEnabled;
    private float mLastFocusX;
    private float mLastFocusY;
    private final OnGestureListener mListener;
    private int mMaximumFlingVelocity;
    private int mMinimumFlingVelocity;
    private MotionEvent mPreviousUpEvent;
    private boolean mStillDown;
    private int mTouchSlopSquare;
    private VelocityTracker mVelocityTracker;
    private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
    public static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();

    /* loaded from: classes3.dex */
    private class GestureHandler extends Handler {
        GestureHandler() {
        }

        GestureHandler(Handler handler) {
            super(handler.getLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                GestureDetector2.this.mListener.onShowPress(GestureDetector2.this.mCurrentDownEvent);
            } else if (i == 2) {
                GestureDetector2.this.dispatchLongPress();
            } else if (i != 3) {
                throw new RuntimeException("Unknown message " + message);
            } else if (GestureDetector2.this.mDoubleTapListener != null) {
                if (GestureDetector2.this.mStillDown) {
                    GestureDetector2.this.mDeferConfirmSingleTap = true;
                } else {
                    GestureDetector2.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetector2.this.mCurrentDownEvent);
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public interface OnDoubleTapListener {
        boolean canDoubleTap(MotionEvent motionEvent);

        boolean onDoubleTap(MotionEvent motionEvent);

        boolean onDoubleTapEvent(MotionEvent motionEvent);

        boolean onSingleTapConfirmed(MotionEvent motionEvent);
    }

    /* loaded from: classes3.dex */
    public interface OnGestureListener {
        boolean onDown(MotionEvent motionEvent);

        boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onLongPress(MotionEvent motionEvent);

        boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onShowPress(MotionEvent motionEvent);

        boolean onSingleTapUp(MotionEvent motionEvent);

        void onUp(MotionEvent motionEvent);
    }

    public GestureDetector2(Context context, OnGestureListener onGestureListener) {
        this(context, onGestureListener, null);
    }

    public GestureDetector2(Context context, OnGestureListener onGestureListener, Handler handler) {
        if (handler != null) {
            this.mHandler = new GestureHandler(handler);
        } else {
            this.mHandler = new GestureHandler();
        }
        this.mListener = onGestureListener;
        if (onGestureListener instanceof OnDoubleTapListener) {
            setOnDoubleTapListener((OnDoubleTapListener) onGestureListener);
        }
        init(context);
    }

    public GestureDetector2(OnGestureListener onGestureListener) {
        this(null, onGestureListener, null);
    }

    private void cancel() {
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(3);
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
        this.mIsDoubleTapping = false;
        this.mStillDown = false;
        this.mAlwaysInTapRegion = false;
        this.mAlwaysInBiggerTapRegion = false;
        this.mDeferConfirmSingleTap = false;
        this.mInLongPress = false;
        this.mInContextClick = false;
        this.mIgnoreNextUpEvent = false;
    }

    private void cancelTaps() {
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(3);
        this.mIsDoubleTapping = false;
        this.mAlwaysInTapRegion = false;
        this.mAlwaysInBiggerTapRegion = false;
        this.mDeferConfirmSingleTap = false;
        this.mInLongPress = false;
        this.mInContextClick = false;
        this.mIgnoreNextUpEvent = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchLongPress() {
        this.mHandler.removeMessages(3);
        this.mDeferConfirmSingleTap = false;
        this.mInLongPress = true;
        this.mListener.onLongPress(this.mCurrentDownEvent);
    }

    private void init(Context context) {
        int scaledTouchSlop;
        int scaledDoubleTapSlop;
        int i;
        if (this.mListener == null) {
            throw new NullPointerException("OnGestureListener must not be null");
        }
        this.mIsLongpressEnabled = true;
        if (context == null) {
            i = ViewConfiguration.getTouchSlop();
            this.mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
            this.mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
            scaledTouchSlop = i;
            scaledDoubleTapSlop = 100;
        } else {
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            scaledTouchSlop = viewConfiguration.getScaledTouchSlop();
            int scaledTouchSlop2 = viewConfiguration.getScaledTouchSlop();
            scaledDoubleTapSlop = viewConfiguration.getScaledDoubleTapSlop();
            this.mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
            this.mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
            i = scaledTouchSlop2;
        }
        this.mTouchSlopSquare = scaledTouchSlop * scaledTouchSlop;
        this.mDoubleTapTouchSlopSquare = i * i;
        this.mDoubleTapSlopSquare = scaledDoubleTapSlop * scaledDoubleTapSlop;
    }

    private boolean isConsideredDoubleTap(MotionEvent motionEvent, MotionEvent motionEvent2, MotionEvent motionEvent3) {
        if (this.mAlwaysInBiggerTapRegion) {
            long eventTime = motionEvent3.getEventTime() - motionEvent2.getEventTime();
            if (eventTime > DOUBLE_TAP_TIMEOUT || eventTime < 40) {
                return false;
            }
            int x = ((int) motionEvent.getX()) - ((int) motionEvent3.getX());
            int y = ((int) motionEvent.getY()) - ((int) motionEvent3.getY());
            return (x * x) + (y * y) < this.mDoubleTapSlopSquare;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:149:0x02bb  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x02d1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        MotionEvent motionEvent2;
        MotionEvent motionEvent3;
        boolean onFling;
        OnDoubleTapListener onDoubleTapListener;
        int i;
        boolean z2;
        int i2;
        int action = motionEvent.getAction();
        MotionEvent motionEvent4 = this.mCurrentMotionEvent;
        if (motionEvent4 != null) {
            motionEvent4.recycle();
        }
        this.mCurrentMotionEvent = MotionEvent.obtain(motionEvent);
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int i3 = action & NotificationCenter.messagePlayingSpeedChanged;
        boolean z3 = true;
        boolean z4 = i3 == 6;
        int actionIndex = z4 ? motionEvent.getActionIndex() : -1;
        int pointerCount = motionEvent.getPointerCount();
        float f = 0.0f;
        float f2 = 0.0f;
        for (int i4 = 0; i4 < pointerCount; i4++) {
            if (actionIndex != i4) {
                f2 += motionEvent.getX(i4);
                f += motionEvent.getY(i4);
            }
        }
        float f3 = z4 ? pointerCount - 1 : pointerCount;
        float f4 = f2 / f3;
        float f5 = f / f3;
        if (i3 == 0) {
            this.mDeferConfirmSingleTap = false;
            OnDoubleTapListener onDoubleTapListener2 = this.mDoubleTapListener;
            if (onDoubleTapListener2 != null) {
                if (onDoubleTapListener2.canDoubleTap(motionEvent)) {
                    boolean hasMessages = this.mHandler.hasMessages(3);
                    if (hasMessages) {
                        this.mHandler.removeMessages(3);
                    }
                    MotionEvent motionEvent5 = this.mCurrentDownEvent;
                    if (motionEvent5 != null && (motionEvent3 = this.mPreviousUpEvent) != null && hasMessages && isConsideredDoubleTap(motionEvent5, motionEvent3, motionEvent)) {
                        this.mIsDoubleTapping = true;
                        z = this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | this.mDoubleTapListener.onDoubleTapEvent(motionEvent);
                        this.mLastFocusX = f4;
                        this.mDownFocusX = f4;
                        this.mLastFocusY = f5;
                        this.mDownFocusY = f5;
                        motionEvent2 = this.mCurrentDownEvent;
                        if (motionEvent2 != null) {
                            motionEvent2.recycle();
                        }
                        this.mCurrentDownEvent = MotionEvent.obtain(motionEvent);
                        this.mAlwaysInTapRegion = true;
                        this.mAlwaysInBiggerTapRegion = true;
                        this.mStillDown = true;
                        this.mInLongPress = false;
                        if (this.mIsLongpressEnabled) {
                            this.mHandler.removeMessages(2);
                            Handler handler = this.mHandler;
                            handler.sendMessageDelayed(handler.obtainMessage(2, 0, 0), ViewConfiguration.getLongPressTimeout());
                        }
                        this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + TAP_TIMEOUT);
                        return z | this.mListener.onDown(motionEvent);
                    }
                    this.mHandler.sendEmptyMessageDelayed(3, DOUBLE_TAP_TIMEOUT);
                } else {
                    this.mDeferConfirmSingleTap = true;
                }
            }
            z = false;
            this.mLastFocusX = f4;
            this.mDownFocusX = f4;
            this.mLastFocusY = f5;
            this.mDownFocusY = f5;
            motionEvent2 = this.mCurrentDownEvent;
            if (motionEvent2 != null) {
            }
            this.mCurrentDownEvent = MotionEvent.obtain(motionEvent);
            this.mAlwaysInTapRegion = true;
            this.mAlwaysInBiggerTapRegion = true;
            this.mStillDown = true;
            this.mInLongPress = false;
            if (this.mIsLongpressEnabled) {
            }
            this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + TAP_TIMEOUT);
            return z | this.mListener.onDown(motionEvent);
        } else if (i3 == 1) {
            this.mStillDown = false;
            this.mListener.onUp(motionEvent);
            MotionEvent obtain = MotionEvent.obtain(motionEvent);
            if (this.mIsDoubleTapping) {
                OnDoubleTapListener onDoubleTapListener3 = this.mDoubleTapListener;
                if (onDoubleTapListener3 != null && onDoubleTapListener3.onDoubleTapEvent(motionEvent)) {
                    onFling = true;
                }
                onFling = false;
            } else {
                if (this.mInLongPress) {
                    this.mHandler.removeMessages(3);
                    this.mInLongPress = false;
                } else if (this.mAlwaysInTapRegion && !this.mIgnoreNextUpEvent) {
                    boolean onSingleTapUp = this.mListener.onSingleTapUp(motionEvent);
                    if (this.mDeferConfirmSingleTap && (onDoubleTapListener = this.mDoubleTapListener) != null) {
                        onDoubleTapListener.onSingleTapConfirmed(motionEvent);
                    }
                    onFling = onSingleTapUp;
                } else if (!this.mIgnoreNextUpEvent) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    int pointerId = motionEvent.getPointerId(0);
                    velocityTracker.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
                    float yVelocity = velocityTracker.getYVelocity(pointerId);
                    float xVelocity = velocityTracker.getXVelocity(pointerId);
                    if (Math.abs(yVelocity) > this.mMinimumFlingVelocity || Math.abs(xVelocity) > this.mMinimumFlingVelocity) {
                        onFling = this.mListener.onFling(this.mCurrentDownEvent, motionEvent, xVelocity, yVelocity);
                    }
                }
                onFling = false;
            }
            MotionEvent motionEvent6 = this.mPreviousUpEvent;
            if (motionEvent6 != null) {
                motionEvent6.recycle();
            }
            this.mPreviousUpEvent = obtain;
            VelocityTracker velocityTracker2 = this.mVelocityTracker;
            if (velocityTracker2 != null) {
                velocityTracker2.recycle();
                this.mVelocityTracker = null;
            }
            this.mIsDoubleTapping = false;
            this.mDeferConfirmSingleTap = false;
            this.mIgnoreNextUpEvent = false;
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            return onFling;
        } else if (i3 != 2) {
            if (i3 == 3) {
                cancel();
                return false;
            } else if (i3 == 5) {
                this.mLastFocusX = f4;
                this.mDownFocusX = f4;
                this.mLastFocusY = f5;
                this.mDownFocusY = f5;
                cancelTaps();
                return false;
            } else if (i3 != 6) {
                return false;
            } else {
                this.mLastFocusX = f4;
                this.mDownFocusX = f4;
                this.mLastFocusY = f5;
                this.mDownFocusY = f5;
                this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
                int actionIndex2 = motionEvent.getActionIndex();
                int pointerId2 = motionEvent.getPointerId(actionIndex2);
                float xVelocity2 = this.mVelocityTracker.getXVelocity(pointerId2);
                float yVelocity2 = this.mVelocityTracker.getYVelocity(pointerId2);
                for (int i5 = 0; i5 < pointerCount; i5++) {
                    if (i5 != actionIndex2) {
                        int pointerId3 = motionEvent.getPointerId(i5);
                        if ((this.mVelocityTracker.getXVelocity(pointerId3) * xVelocity2) + (this.mVelocityTracker.getYVelocity(pointerId3) * yVelocity2) < 0.0f) {
                            this.mVelocityTracker.clear();
                            return false;
                        }
                    }
                }
                return false;
            }
        } else if (this.mInLongPress || this.mInContextClick) {
            return false;
        } else {
            int i6 = Build.VERSION.SDK_INT;
            int classification = i6 >= 29 ? motionEvent.getClassification() : 0;
            boolean hasMessages2 = this.mHandler.hasMessages(2);
            float f6 = this.mLastFocusX - f4;
            float f7 = this.mLastFocusY - f5;
            if (this.mIsDoubleTapping) {
                OnDoubleTapListener onDoubleTapListener4 = this.mDoubleTapListener;
                if (onDoubleTapListener4 == null || !onDoubleTapListener4.onDoubleTapEvent(motionEvent)) {
                    i = classification;
                    z2 = hasMessages2;
                    i2 = 29;
                    z3 = false;
                } else {
                    i = classification;
                    z2 = hasMessages2;
                    i2 = 29;
                }
            } else {
                if (this.mAlwaysInTapRegion) {
                    int i7 = (int) (f4 - this.mDownFocusX);
                    int i8 = (int) (f5 - this.mDownFocusY);
                    int i9 = (i7 * i7) + (i8 * i8);
                    int i10 = this.mTouchSlopSquare;
                    boolean z5 = i6 >= 29 && classification == 1;
                    if (hasMessages2 && z5) {
                        if (i9 > i10) {
                            this.mHandler.removeMessages(2);
                            i = classification;
                            z2 = hasMessages2;
                            long longPressTimeout = ViewConfiguration.getLongPressTimeout();
                            Handler handler2 = this.mHandler;
                            handler2.sendMessageDelayed(handler2.obtainMessage(2, 0, 0), ((float) longPressTimeout) * 2.0f);
                        } else {
                            i = classification;
                            z2 = hasMessages2;
                        }
                        i10 = (int) (i10 * 4.0f);
                    } else {
                        i = classification;
                        z2 = hasMessages2;
                    }
                    if (i9 > i10) {
                        boolean onScroll = this.mListener.onScroll(this.mCurrentDownEvent, motionEvent, f6, f7);
                        this.mLastFocusX = f4;
                        this.mLastFocusY = f5;
                        this.mAlwaysInTapRegion = false;
                        this.mHandler.removeMessages(3);
                        this.mHandler.removeMessages(1);
                        this.mHandler.removeMessages(2);
                        z3 = onScroll;
                    } else {
                        z3 = false;
                    }
                    if (i9 > this.mDoubleTapTouchSlopSquare) {
                        this.mAlwaysInBiggerTapRegion = false;
                    }
                } else {
                    i = classification;
                    z2 = hasMessages2;
                    if (Math.abs(f6) >= 1.0f || Math.abs(f7) >= 1.0f) {
                        z3 = this.mListener.onScroll(this.mCurrentDownEvent, motionEvent, f6, f7);
                        this.mLastFocusX = f4;
                        this.mLastFocusY = f5;
                    }
                    i2 = 29;
                    z3 = false;
                }
                i2 = 29;
            }
            if (i6 >= i2 && i == 2 && z2) {
                this.mHandler.removeMessages(2);
                Handler handler3 = this.mHandler;
                handler3.sendMessage(handler3.obtainMessage(2, 0, 0));
            }
            return z3;
        }
    }

    public void setIsLongpressEnabled(boolean z) {
        this.mIsLongpressEnabled = z;
    }

    public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        this.mDoubleTapListener = onDoubleTapListener;
    }
}
