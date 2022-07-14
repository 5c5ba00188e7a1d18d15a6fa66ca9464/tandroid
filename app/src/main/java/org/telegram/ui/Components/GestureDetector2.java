package org.telegram.ui.Components;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
/* loaded from: classes5.dex */
public class GestureDetector2 {
    private static final int DOUBLE_TAP_MIN_TIME = 40;
    private static final int LONG_PRESS = 2;
    private static final int SHOW_PRESS = 1;
    private static final int TAP = 3;
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

    /* loaded from: classes5.dex */
    public interface OnGestureListener {
        boolean onDown(MotionEvent motionEvent);

        boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onLongPress(MotionEvent motionEvent);

        boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onShowPress(MotionEvent motionEvent);

        boolean onSingleTapUp(MotionEvent motionEvent);

        void onUp(MotionEvent motionEvent);
    }

    /* loaded from: classes5.dex */
    public interface OnDoubleTapListener {
        boolean canDoubleTap(MotionEvent motionEvent);

        boolean onDoubleTap(MotionEvent motionEvent);

        boolean onDoubleTapEvent(MotionEvent motionEvent);

        boolean onSingleTapConfirmed(MotionEvent motionEvent);

        /* renamed from: org.telegram.ui.Components.GestureDetector2$OnDoubleTapListener$-CC */
        /* loaded from: classes5.dex */
        public final /* synthetic */ class CC {
            public static boolean $default$canDoubleTap(OnDoubleTapListener _this, MotionEvent e) {
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public class GestureHandler extends Handler {
        GestureHandler() {
            GestureDetector2.this = r1;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        GestureHandler(Handler handler) {
            super(handler.getLooper());
            GestureDetector2.this = r1;
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    GestureDetector2.this.mListener.onShowPress(GestureDetector2.this.mCurrentDownEvent);
                    return;
                case 2:
                    GestureDetector2.this.dispatchLongPress();
                    return;
                case 3:
                    if (GestureDetector2.this.mDoubleTapListener != null) {
                        if (!GestureDetector2.this.mStillDown) {
                            GestureDetector2.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetector2.this.mCurrentDownEvent);
                            return;
                        } else {
                            GestureDetector2.this.mDeferConfirmSingleTap = true;
                            return;
                        }
                    }
                    return;
                default:
                    throw new RuntimeException("Unknown message " + msg);
            }
        }
    }

    @Deprecated
    public GestureDetector2(OnGestureListener listener, Handler handler) {
        this(null, listener, handler);
    }

    @Deprecated
    public GestureDetector2(OnGestureListener listener) {
        this(null, listener, null);
    }

    public GestureDetector2(Context context, OnGestureListener listener) {
        this(context, listener, null);
    }

    public GestureDetector2(Context context, OnGestureListener listener, Handler handler) {
        if (handler != null) {
            this.mHandler = new GestureHandler(handler);
        } else {
            this.mHandler = new GestureHandler();
        }
        this.mListener = listener;
        if (listener instanceof OnDoubleTapListener) {
            setOnDoubleTapListener((OnDoubleTapListener) listener);
        }
        init(context);
    }

    public GestureDetector2(Context context, OnGestureListener listener, Handler handler, boolean unused) {
        this(context, listener, handler);
    }

    private void init(Context context) {
        int doubleTapTouchSlop;
        int touchSlop;
        int touchSlop2;
        if (this.mListener == null) {
            throw new NullPointerException("OnGestureListener must not be null");
        }
        this.mIsLongpressEnabled = true;
        if (context == null) {
            touchSlop2 = ViewConfiguration.getTouchSlop();
            touchSlop = touchSlop2;
            doubleTapTouchSlop = 100;
            this.mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
            this.mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
        } else {
            ViewConfiguration configuration = ViewConfiguration.get(context);
            int touchSlop3 = configuration.getScaledTouchSlop();
            int doubleTapTouchSlop2 = configuration.getScaledTouchSlop();
            int doubleTapSlop = configuration.getScaledDoubleTapSlop();
            this.mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
            this.mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
            touchSlop2 = touchSlop3;
            touchSlop = doubleTapTouchSlop2;
            doubleTapTouchSlop = doubleTapSlop;
        }
        int doubleTapSlop2 = touchSlop2 * touchSlop2;
        this.mTouchSlopSquare = doubleTapSlop2;
        this.mDoubleTapTouchSlopSquare = touchSlop * touchSlop;
        this.mDoubleTapSlopSquare = doubleTapTouchSlop * doubleTapTouchSlop;
    }

    public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        this.mDoubleTapListener = onDoubleTapListener;
    }

    public void setIsLongpressEnabled(boolean isLongpressEnabled) {
        this.mIsLongpressEnabled = isLongpressEnabled;
    }

    public boolean isLongpressEnabled() {
        return this.mIsLongpressEnabled;
    }

    /* JADX WARN: Removed duplicated region for block: B:155:0x03b1  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x03c8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent ev) {
        boolean handled;
        boolean handled2;
        MotionEvent motionEvent;
        MotionEvent motionEvent2;
        boolean handled3;
        OnDoubleTapListener onDoubleTapListener;
        boolean hasPendingLongPress;
        boolean handled4;
        boolean handled5;
        int skipIndex;
        boolean pointerUp;
        int upIndex;
        int action = ev.getAction();
        MotionEvent motionEvent3 = this.mCurrentMotionEvent;
        if (motionEvent3 != null) {
            motionEvent3.recycle();
        }
        this.mCurrentMotionEvent = MotionEvent.obtain(ev);
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(ev);
        boolean pointerUp2 = (action & 255) == 6;
        int skipIndex2 = pointerUp2 ? ev.getActionIndex() : -1;
        float sumX = 0.0f;
        float sumY = 0.0f;
        int count = ev.getPointerCount();
        for (int i = 0; i < count; i++) {
            if (skipIndex2 != i) {
                sumX += ev.getX(i);
                sumY += ev.getY(i);
            }
        }
        int div = pointerUp2 ? count - 1 : count;
        float focusX = sumX / div;
        float focusY = sumY / div;
        switch (action & 255) {
            case 0:
                this.mDeferConfirmSingleTap = false;
                OnDoubleTapListener onDoubleTapListener2 = this.mDoubleTapListener;
                if (onDoubleTapListener2 != null) {
                    if (!onDoubleTapListener2.canDoubleTap(ev)) {
                        this.mDeferConfirmSingleTap = true;
                    } else {
                        boolean hadTapMessage = this.mHandler.hasMessages(3);
                        if (hadTapMessage) {
                            this.mHandler.removeMessages(3);
                        }
                        MotionEvent motionEvent4 = this.mCurrentDownEvent;
                        if (motionEvent4 != null && (motionEvent2 = this.mPreviousUpEvent) != null && hadTapMessage && isConsideredDoubleTap(motionEvent4, motionEvent2, ev)) {
                            this.mIsDoubleTapping = true;
                            boolean handled6 = false | this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent);
                            handled2 = handled6 | this.mDoubleTapListener.onDoubleTapEvent(ev);
                        } else {
                            this.mHandler.sendEmptyMessageDelayed(3, DOUBLE_TAP_TIMEOUT);
                            handled2 = false;
                        }
                        this.mLastFocusX = focusX;
                        this.mDownFocusX = focusX;
                        this.mLastFocusY = focusY;
                        this.mDownFocusY = focusY;
                        motionEvent = this.mCurrentDownEvent;
                        if (motionEvent != null) {
                            motionEvent.recycle();
                        }
                        this.mCurrentDownEvent = MotionEvent.obtain(ev);
                        this.mAlwaysInTapRegion = true;
                        this.mAlwaysInBiggerTapRegion = true;
                        this.mStillDown = true;
                        this.mInLongPress = false;
                        if (this.mIsLongpressEnabled) {
                            this.mHandler.removeMessages(2);
                            Handler handler = this.mHandler;
                            handler.sendMessageAtTime(handler.obtainMessage(2, 0, 0), this.mCurrentDownEvent.getDownTime() + ViewConfiguration.getLongPressTimeout());
                        }
                        this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + TAP_TIMEOUT);
                        return handled2 | this.mListener.onDown(ev);
                    }
                }
                handled2 = false;
                this.mLastFocusX = focusX;
                this.mDownFocusX = focusX;
                this.mLastFocusY = focusY;
                this.mDownFocusY = focusY;
                motionEvent = this.mCurrentDownEvent;
                if (motionEvent != null) {
                }
                this.mCurrentDownEvent = MotionEvent.obtain(ev);
                this.mAlwaysInTapRegion = true;
                this.mAlwaysInBiggerTapRegion = true;
                this.mStillDown = true;
                this.mInLongPress = false;
                if (this.mIsLongpressEnabled) {
                }
                this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + TAP_TIMEOUT);
                return handled2 | this.mListener.onDown(ev);
            case 1:
                this.mStillDown = false;
                this.mListener.onUp(ev);
                MotionEvent currentUpEvent = MotionEvent.obtain(ev);
                if (this.mIsDoubleTapping) {
                    OnDoubleTapListener onDoubleTapListener3 = this.mDoubleTapListener;
                    handled3 = false | (onDoubleTapListener3 != null && onDoubleTapListener3.onDoubleTapEvent(ev));
                } else {
                    if (this.mInLongPress) {
                        this.mHandler.removeMessages(3);
                        this.mInLongPress = false;
                    } else if (this.mAlwaysInTapRegion && !this.mIgnoreNextUpEvent) {
                        handled3 = this.mListener.onSingleTapUp(ev);
                        if (this.mDeferConfirmSingleTap && (onDoubleTapListener = this.mDoubleTapListener) != null) {
                            onDoubleTapListener.onSingleTapConfirmed(ev);
                        }
                    } else if (!this.mIgnoreNextUpEvent) {
                        VelocityTracker velocityTracker = this.mVelocityTracker;
                        int pointerId = ev.getPointerId(0);
                        velocityTracker.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
                        float velocityY = velocityTracker.getYVelocity(pointerId);
                        float velocityX = velocityTracker.getXVelocity(pointerId);
                        if (Math.abs(velocityY) > this.mMinimumFlingVelocity || Math.abs(velocityX) > this.mMinimumFlingVelocity) {
                            handled3 = this.mListener.onFling(this.mCurrentDownEvent, ev, velocityX, velocityY);
                        }
                    }
                    handled3 = false;
                }
                MotionEvent motionEvent5 = this.mPreviousUpEvent;
                if (motionEvent5 != null) {
                    motionEvent5.recycle();
                }
                this.mPreviousUpEvent = currentUpEvent;
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
                return handled3;
            case 2:
                if (!this.mInLongPress) {
                    if (this.mInContextClick) {
                        handled = false;
                        break;
                    } else {
                        int motionClassification = Build.VERSION.SDK_INT >= 29 ? ev.getClassification() : 0;
                        boolean hasPendingLongPress2 = this.mHandler.hasMessages(2);
                        float scrollX = this.mLastFocusX - focusX;
                        float scrollY = this.mLastFocusY - focusY;
                        if (this.mIsDoubleTapping) {
                            OnDoubleTapListener onDoubleTapListener4 = this.mDoubleTapListener;
                            hasPendingLongPress = hasPendingLongPress2;
                            handled4 = (onDoubleTapListener4 != null && onDoubleTapListener4.onDoubleTapEvent(ev)) | false;
                        } else {
                            boolean handled7 = this.mAlwaysInTapRegion;
                            if (handled7) {
                                int deltaX = (int) (focusX - this.mDownFocusX);
                                int deltaY = (int) (focusY - this.mDownFocusY);
                                int distance = (deltaX * deltaX) + (deltaY * deltaY);
                                int slopSquare = this.mTouchSlopSquare;
                                boolean ambiguousGesture = Build.VERSION.SDK_INT >= 29 && motionClassification == 1;
                                boolean shouldInhibitDefaultAction = hasPendingLongPress2 && ambiguousGesture;
                                if (!shouldInhibitDefaultAction) {
                                    hasPendingLongPress = hasPendingLongPress2;
                                    handled5 = false;
                                } else {
                                    if (distance > slopSquare) {
                                        this.mHandler.removeMessages(2);
                                        long longPressTimeout = ViewConfiguration.getLongPressTimeout();
                                        Handler handler2 = this.mHandler;
                                        handled5 = false;
                                        hasPendingLongPress = hasPendingLongPress2;
                                        handler2.sendMessageAtTime(handler2.obtainMessage(2, 0, 0), ev.getDownTime() + (((float) longPressTimeout) * 2.0f));
                                    } else {
                                        hasPendingLongPress = hasPendingLongPress2;
                                        handled5 = false;
                                    }
                                    slopSquare = (int) (slopSquare * 4.0f);
                                }
                                if (distance > slopSquare) {
                                    handled4 = this.mListener.onScroll(this.mCurrentDownEvent, ev, scrollX, scrollY);
                                    this.mLastFocusX = focusX;
                                    this.mLastFocusY = focusY;
                                    this.mAlwaysInTapRegion = false;
                                    this.mHandler.removeMessages(3);
                                    this.mHandler.removeMessages(1);
                                    this.mHandler.removeMessages(2);
                                } else {
                                    handled4 = handled5;
                                }
                                int doubleTapSlopSquare = this.mDoubleTapTouchSlopSquare;
                                if (distance > doubleTapSlopSquare) {
                                    this.mAlwaysInBiggerTapRegion = false;
                                }
                            } else {
                                hasPendingLongPress = hasPendingLongPress2;
                                if (Math.abs(scrollX) >= 1.0f || Math.abs(scrollY) >= 1.0f) {
                                    boolean handled8 = this.mListener.onScroll(this.mCurrentDownEvent, ev, scrollX, scrollY);
                                    this.mLastFocusX = focusX;
                                    this.mLastFocusY = focusY;
                                    handled4 = handled8;
                                } else {
                                    handled4 = false;
                                }
                            }
                        }
                        if (Build.VERSION.SDK_INT < 29) {
                            return handled4;
                        }
                        boolean deepPress = motionClassification == 2;
                        if (!deepPress || !hasPendingLongPress) {
                            return handled4;
                        }
                        this.mHandler.removeMessages(2);
                        Handler handler3 = this.mHandler;
                        handler3.sendMessage(handler3.obtainMessage(2, 0, 0));
                        return handled4;
                    }
                } else {
                    handled = false;
                    break;
                }
                break;
            case 3:
                cancel();
                handled = false;
                break;
            case 4:
            default:
                handled = false;
                break;
            case 5:
                this.mLastFocusX = focusX;
                this.mDownFocusX = focusX;
                this.mLastFocusY = focusY;
                this.mDownFocusY = focusY;
                cancelTaps();
                handled = false;
                break;
            case 6:
                this.mLastFocusX = focusX;
                this.mDownFocusX = focusX;
                this.mLastFocusY = focusY;
                this.mDownFocusY = focusY;
                this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
                int upIndex2 = ev.getActionIndex();
                int id1 = ev.getPointerId(upIndex2);
                float x1 = this.mVelocityTracker.getXVelocity(id1);
                float y1 = this.mVelocityTracker.getYVelocity(id1);
                int action2 = 0;
                while (true) {
                    if (action2 < count) {
                        if (action2 == upIndex2) {
                            pointerUp = pointerUp2;
                            skipIndex = skipIndex2;
                            upIndex = upIndex2;
                        } else {
                            pointerUp = pointerUp2;
                            int id2 = ev.getPointerId(action2);
                            skipIndex = skipIndex2;
                            float x = this.mVelocityTracker.getXVelocity(id2) * x1;
                            upIndex = upIndex2;
                            float y = this.mVelocityTracker.getYVelocity(id2) * y1;
                            float dot = x + y;
                            if (dot < 0.0f) {
                                this.mVelocityTracker.clear();
                            }
                        }
                        action2++;
                        upIndex2 = upIndex;
                        pointerUp2 = pointerUp;
                        skipIndex2 = skipIndex;
                    }
                }
                handled = false;
                break;
        }
        return handled;
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

    private boolean isConsideredDoubleTap(MotionEvent firstDown, MotionEvent firstUp, MotionEvent secondDown) {
        if (!this.mAlwaysInBiggerTapRegion) {
            return false;
        }
        long deltaTime = secondDown.getEventTime() - firstUp.getEventTime();
        if (deltaTime > DOUBLE_TAP_TIMEOUT || deltaTime < 40) {
            return false;
        }
        int deltaX = ((int) firstDown.getX()) - ((int) secondDown.getX());
        int deltaY = ((int) firstDown.getY()) - ((int) secondDown.getY());
        int slopSquare = this.mDoubleTapSlopSquare;
        return (deltaX * deltaX) + (deltaY * deltaY) < slopSquare;
    }

    public void dispatchLongPress() {
        this.mHandler.removeMessages(3);
        this.mDeferConfirmSingleTap = false;
        this.mInLongPress = true;
        this.mListener.onLongPress(this.mCurrentDownEvent);
    }
}
