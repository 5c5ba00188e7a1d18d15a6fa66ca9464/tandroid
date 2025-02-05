package org.telegram.ui.Components;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.StateSet;
import android.view.MotionEvent;

/* loaded from: classes3.dex */
public class ClickableAnimatedTextView extends AnimatedTextView {
    private Drawable backgroundDrawable;
    private final android.graphics.Rect bounds;
    private boolean pressed;

    public ClickableAnimatedTextView(Context context) {
        super(context);
        this.bounds = new android.graphics.Rect();
    }

    public android.graphics.Rect getClickBounds() {
        return this.bounds;
    }

    @Override // org.telegram.ui.Components.AnimatedTextView, android.view.View
    protected void onDraw(Canvas canvas) {
        android.graphics.Rect rect;
        int i;
        if (this.backgroundDrawable != null) {
            this.bounds.set(getDrawable().getBounds());
            int ceil = (int) Math.ceil(getDrawable().getCurrentWidth());
            if (getDrawable().getGravity() == 3) {
                rect = this.bounds;
                i = rect.left;
            } else {
                if (getDrawable().getGravity() == 5) {
                    android.graphics.Rect rect2 = this.bounds;
                    rect2.left = rect2.right - ceil;
                } else if (getDrawable().getGravity() == 17) {
                    rect = this.bounds;
                    i = (rect.left + rect.right) / 2;
                    ceil /= 2;
                    rect.left = i - ceil;
                }
                this.bounds.left -= getPaddingLeft();
                this.bounds.top -= getPaddingTop();
                this.bounds.right += getPaddingRight();
                this.bounds.bottom += getPaddingBottom();
                this.backgroundDrawable.setBounds(this.bounds);
                this.backgroundDrawable.draw(canvas);
            }
            rect.right = i + ceil;
            this.bounds.left -= getPaddingLeft();
            this.bounds.top -= getPaddingTop();
            this.bounds.right += getPaddingRight();
            this.bounds.bottom += getPaddingBottom();
            this.backgroundDrawable.setBounds(this.bounds);
            this.backgroundDrawable.draw(canvas);
        }
        super.onDraw(canvas);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean contains = getClickBounds().contains((int) motionEvent.getX(), (int) motionEvent.getY());
        if (motionEvent.getAction() == 0 && contains) {
            this.pressed = true;
            Drawable drawable = this.backgroundDrawable;
            if (drawable != null) {
                if (Build.VERSION.SDK_INT >= 21) {
                    drawable.setHotspot(motionEvent.getX(), motionEvent.getY());
                }
                this.backgroundDrawable.setState(new int[]{R.attr.state_pressed, R.attr.state_enabled});
            }
            invalidate();
        } else if (motionEvent.getAction() == 1) {
            if (this.pressed && contains) {
                callOnClick();
            }
            this.pressed = false;
            Drawable drawable2 = this.backgroundDrawable;
            if (drawable2 != null) {
                drawable2.setState(StateSet.NOTHING);
            }
        } else if (motionEvent.getAction() == 3) {
            this.pressed = false;
            Drawable drawable3 = this.backgroundDrawable;
            if (drawable3 != null) {
                drawable3.setState(StateSet.NOTHING);
            }
        }
        return contains;
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        Drawable drawable2 = this.backgroundDrawable;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.backgroundDrawable = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        invalidate();
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        Drawable drawable2 = this.backgroundDrawable;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.backgroundDrawable = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        invalidate();
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return drawable == this.backgroundDrawable || super.verifyDrawable(drawable);
    }
}
