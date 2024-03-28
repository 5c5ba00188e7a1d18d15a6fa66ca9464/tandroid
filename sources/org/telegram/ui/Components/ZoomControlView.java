package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;
import org.telegram.ui.Components.AnimationProperties;
/* loaded from: classes3.dex */
public class ZoomControlView extends View {
    public final Property<ZoomControlView, Float> ZOOM_PROPERTY;
    private float animatingToZoom;
    private AnimatorSet animatorSet;
    private ZoomControlViewDelegate delegate;
    public boolean enabledTouch;
    private Drawable filledProgressDrawable;
    private Drawable knobDrawable;
    private boolean knobPressed;
    private float knobStartX;
    private float knobStartY;
    private int minusCx;
    private int minusCy;
    private Drawable minusDrawable;
    private int plusCx;
    private int plusCy;
    private Drawable plusDrawable;
    private boolean pressed;
    private Drawable pressedKnobDrawable;
    private Drawable progressDrawable;
    private int progressEndX;
    private int progressEndY;
    private int progressStartX;
    private int progressStartY;
    private float zoom;

    /* loaded from: classes3.dex */
    public interface ZoomControlViewDelegate {
        void didSetZoom(float f);
    }

    public ZoomControlView(Context context) {
        super(context);
        this.enabledTouch = true;
        this.ZOOM_PROPERTY = new AnimationProperties.FloatProperty<ZoomControlView>("clipProgress") { // from class: org.telegram.ui.Components.ZoomControlView.1
            @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
            public void setValue(ZoomControlView zoomControlView, float f) {
                ZoomControlView.this.zoom = f;
                if (ZoomControlView.this.delegate != null) {
                    ZoomControlView.this.delegate.didSetZoom(ZoomControlView.this.zoom);
                }
                ZoomControlView.this.invalidate();
            }

            @Override // android.util.Property
            public Float get(ZoomControlView zoomControlView) {
                return Float.valueOf(ZoomControlView.this.zoom);
            }
        };
        this.minusDrawable = context.getResources().getDrawable(R.drawable.zoom_minus);
        this.plusDrawable = context.getResources().getDrawable(R.drawable.zoom_plus);
        this.progressDrawable = context.getResources().getDrawable(R.drawable.zoom_slide);
        this.filledProgressDrawable = context.getResources().getDrawable(R.drawable.zoom_slide_a);
        this.knobDrawable = context.getResources().getDrawable(R.drawable.zoom_round);
        this.pressedKnobDrawable = context.getResources().getDrawable(R.drawable.zoom_round_b);
    }

    public float getZoom() {
        if (this.animatorSet != null) {
            return this.animatingToZoom;
        }
        return this.zoom;
    }

    public void setZoom(float f, boolean z) {
        ZoomControlViewDelegate zoomControlViewDelegate;
        if (f == this.zoom) {
            return;
        }
        if (f < 0.0f) {
            f = 0.0f;
        } else if (f > 1.0f) {
            f = 1.0f;
        }
        this.zoom = f;
        if (z && (zoomControlViewDelegate = this.delegate) != null) {
            zoomControlViewDelegate.didSetZoom(f);
        }
        invalidate();
    }

    public void setDelegate(ZoomControlViewDelegate zoomControlViewDelegate) {
        this.delegate = zoomControlViewDelegate;
    }

    /* JADX WARN: Removed duplicated region for block: B:95:0x01df  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x01e8  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        if (!this.enabledTouch) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int action = motionEvent.getAction();
        boolean z2 = getMeasuredWidth() > getMeasuredHeight();
        int i = this.progressStartX;
        int i2 = this.progressEndX;
        float f = this.zoom;
        int i3 = (int) (i + ((i2 - i) * f));
        int i4 = this.progressStartY;
        int i5 = this.progressEndY;
        int i6 = (int) (i4 + ((i5 - i4) * f));
        if (action == 1 || action == 0) {
            if (x < i3 - AndroidUtilities.dp(20.0f) || x > AndroidUtilities.dp(20.0f) + i3 || y < i6 - AndroidUtilities.dp(25.0f) || y > AndroidUtilities.dp(25.0f) + i6) {
                if (x >= this.minusCx - AndroidUtilities.dp(16.0f) && x <= this.minusCx + AndroidUtilities.dp(16.0f) && y >= this.minusCy - AndroidUtilities.dp(16.0f) && y <= this.minusCy + AndroidUtilities.dp(16.0f)) {
                    if (action == 1 && animateToZoom((((float) Math.floor(getZoom() / 0.25f)) * 0.25f) - 0.25f)) {
                        performHapticFeedback(3);
                    } else {
                        this.pressed = true;
                    }
                } else if (x < this.plusCx - AndroidUtilities.dp(16.0f) || x > this.plusCx + AndroidUtilities.dp(16.0f) || y < this.plusCy - AndroidUtilities.dp(16.0f) || y > this.plusCy + AndroidUtilities.dp(16.0f)) {
                    if (z2) {
                        if (x >= this.progressStartX && x <= this.progressEndX) {
                            if (action == 0) {
                                this.knobStartX = x;
                                this.pressed = true;
                            } else if (Math.abs(this.knobStartX - x) <= AndroidUtilities.dp(10.0f)) {
                                int i7 = this.progressStartX;
                                float f2 = (x - i7) / (this.progressEndX - i7);
                                this.zoom = f2;
                                ZoomControlViewDelegate zoomControlViewDelegate = this.delegate;
                                if (zoomControlViewDelegate != null) {
                                    zoomControlViewDelegate.didSetZoom(f2);
                                }
                                invalidate();
                            }
                        }
                    } else if (y >= this.progressStartY && y <= this.progressEndY) {
                        if (action == 1) {
                            this.knobStartY = y;
                            this.pressed = true;
                        } else if (Math.abs(this.knobStartY - y) <= AndroidUtilities.dp(10.0f)) {
                            int i8 = this.progressStartY;
                            float f3 = (y - i8) / (this.progressEndY - i8);
                            this.zoom = f3;
                            ZoomControlViewDelegate zoomControlViewDelegate2 = this.delegate;
                            if (zoomControlViewDelegate2 != null) {
                                zoomControlViewDelegate2.didSetZoom(f3);
                            }
                            invalidate();
                        }
                    }
                    if (action == 1) {
                        this.pressed = false;
                        this.knobPressed = false;
                        invalidate();
                    }
                    return !z || this.pressed || this.knobPressed || super.onTouchEvent(motionEvent);
                } else if (action == 1 && animateToZoom((((float) Math.floor(getZoom() / 0.25f)) * 0.25f) + 0.25f)) {
                    performHapticFeedback(3);
                } else {
                    this.pressed = true;
                }
            } else if (action == 0) {
                this.knobPressed = true;
                this.knobStartX = x - i3;
                this.knobStartY = y - i6;
                invalidate();
            }
            z = true;
            if (action == 1) {
            }
            if (z) {
            }
        } else if (action == 2 && this.knobPressed) {
            if (z2) {
                this.zoom = ((x + this.knobStartX) - i) / (i2 - i);
            } else {
                this.zoom = ((y + this.knobStartY) - i4) / (i5 - i4);
            }
            float f4 = this.zoom;
            if (f4 < 0.0f) {
                this.zoom = 0.0f;
            } else if (f4 > 1.0f) {
                this.zoom = 1.0f;
            }
            ZoomControlViewDelegate zoomControlViewDelegate3 = this.delegate;
            if (zoomControlViewDelegate3 != null) {
                zoomControlViewDelegate3.didSetZoom(this.zoom);
            }
            invalidate();
        }
        z = false;
        if (action == 1) {
        }
        if (z) {
        }
    }

    public boolean isTouch() {
        return this.pressed || this.knobPressed;
    }

    private boolean animateToZoom(float f) {
        if (f < 0.0f || f > 1.0f) {
            return false;
        }
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        this.animatingToZoom = f;
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.animatorSet = animatorSet2;
        animatorSet2.playTogether(ObjectAnimator.ofFloat(this, this.ZOOM_PROPERTY, f));
        this.animatorSet.setDuration(180L);
        this.animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ZoomControlView.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ZoomControlView.this.animatorSet = null;
            }
        });
        this.animatorSet.start();
        return true;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int measuredWidth = getMeasuredWidth() / 2;
        int measuredHeight = getMeasuredHeight() / 2;
        boolean z = getMeasuredWidth() > getMeasuredHeight();
        if (z) {
            this.minusCx = AndroidUtilities.dp(41.0f);
            this.minusCy = measuredHeight;
            this.plusCx = getMeasuredWidth() - AndroidUtilities.dp(41.0f);
            this.plusCy = measuredHeight;
            this.progressStartX = this.minusCx + AndroidUtilities.dp(18.0f);
            this.progressStartY = measuredHeight;
            this.progressEndX = this.plusCx - AndroidUtilities.dp(18.0f);
            this.progressEndY = measuredHeight;
        } else {
            this.minusCx = measuredWidth;
            this.minusCy = AndroidUtilities.dp(41.0f);
            this.plusCx = measuredWidth;
            this.plusCy = getMeasuredHeight() - AndroidUtilities.dp(41.0f);
            this.progressStartX = measuredWidth;
            this.progressStartY = this.minusCy + AndroidUtilities.dp(18.0f);
            this.progressEndX = measuredWidth;
            this.progressEndY = this.plusCy - AndroidUtilities.dp(18.0f);
        }
        this.minusDrawable.setBounds(this.minusCx - AndroidUtilities.dp(7.0f), this.minusCy - AndroidUtilities.dp(7.0f), this.minusCx + AndroidUtilities.dp(7.0f), this.minusCy + AndroidUtilities.dp(7.0f));
        this.minusDrawable.draw(canvas);
        this.plusDrawable.setBounds(this.plusCx - AndroidUtilities.dp(7.0f), this.plusCy - AndroidUtilities.dp(7.0f), this.plusCx + AndroidUtilities.dp(7.0f), this.plusCy + AndroidUtilities.dp(7.0f));
        this.plusDrawable.draw(canvas);
        int i = this.progressEndX;
        int i2 = this.progressStartX;
        int i3 = this.progressEndY;
        int i4 = this.progressStartY;
        float f = this.zoom;
        int i5 = (int) (i2 + ((i - i2) * f));
        int i6 = (int) (i4 + ((i3 - i4) * f));
        if (z) {
            this.progressDrawable.setBounds(i2, i4 - AndroidUtilities.dp(3.0f), this.progressEndX, this.progressStartY + AndroidUtilities.dp(3.0f));
            this.filledProgressDrawable.setBounds(this.progressStartX, this.progressStartY - AndroidUtilities.dp(3.0f), i5, this.progressStartY + AndroidUtilities.dp(3.0f));
        } else {
            this.progressDrawable.setBounds(i4, 0, i3, AndroidUtilities.dp(6.0f));
            this.filledProgressDrawable.setBounds(this.progressStartY, 0, i6, AndroidUtilities.dp(6.0f));
            canvas.save();
            canvas.rotate(90.0f);
            canvas.translate(0.0f, (-this.progressStartX) - AndroidUtilities.dp(3.0f));
        }
        this.progressDrawable.draw(canvas);
        this.filledProgressDrawable.draw(canvas);
        if (!z) {
            canvas.restore();
        }
        Drawable drawable = this.knobPressed ? this.pressedKnobDrawable : this.knobDrawable;
        int intrinsicWidth = drawable.getIntrinsicWidth() / 2;
        drawable.setBounds(i5 - intrinsicWidth, i6 - intrinsicWidth, i5 + intrinsicWidth, i6 + intrinsicWidth);
        drawable.draw(canvas);
    }
}
