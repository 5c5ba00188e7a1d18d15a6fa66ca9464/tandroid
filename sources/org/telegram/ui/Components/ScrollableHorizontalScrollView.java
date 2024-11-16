package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import androidx.core.math.MathUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.EmojiTabsStrip;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public abstract class ScrollableHorizontalScrollView extends HorizontalScrollView {
    public LinearLayout contentView;
    private ValueAnimator scrollAnimator;
    protected boolean scrollingAnimation;
    private int scrollingTo;
    ValueAnimator showAnimator;
    private boolean touch;
    boolean touching;

    public ScrollableHorizontalScrollView(Context context) {
        super(context);
        this.scrollingTo = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scrollTo$0(ValueAnimator valueAnimator) {
        setScrollX((int) ((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateButtonsVisibility();
    }

    @Override // android.view.View
    protected void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        if ((Math.abs(i2 - i4) < 2 || i2 >= getMeasuredHeight() || i2 == 0) && !this.touching) {
            requestDisallowInterceptTouchEvent(false);
        }
        updateButtonsVisibility();
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.touch = true;
        } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            this.touch = false;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void resetScrollTo() {
        this.scrollingTo = -1;
    }

    public void scrollTo(int i) {
        if (this.scrollingTo == i) {
            return;
        }
        this.scrollingTo = i;
        ValueAnimator valueAnimator = this.scrollAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (getScrollX() == i) {
            return;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(getScrollX(), i);
        this.scrollAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ScrollableHorizontalScrollView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ScrollableHorizontalScrollView.this.lambda$scrollTo$0(valueAnimator2);
            }
        });
        this.scrollAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.scrollAnimator.setDuration(250L);
        this.scrollAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ScrollableHorizontalScrollView.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ScrollableHorizontalScrollView.this.scrollingAnimation = false;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                ScrollableHorizontalScrollView scrollableHorizontalScrollView = ScrollableHorizontalScrollView.this;
                scrollableHorizontalScrollView.scrollingAnimation = true;
                if (scrollableHorizontalScrollView.getParent() instanceof HorizontalScrollView) {
                    ((HorizontalScrollView) ScrollableHorizontalScrollView.this.getParent()).requestDisallowInterceptTouchEvent(false);
                }
            }
        });
        this.scrollAnimator.start();
    }

    public boolean scrollToVisible(int i, int i2) {
        int measuredWidth;
        if (getChildCount() <= 0) {
            return false;
        }
        int dp = AndroidUtilities.dp(50.0f);
        if (i < getScrollX() + dp) {
            measuredWidth = i - dp;
        } else {
            if (i2 <= getScrollX() + (getMeasuredWidth() - dp)) {
                return false;
            }
            measuredWidth = (i2 - getMeasuredWidth()) + dp;
        }
        scrollTo(MathUtils.clamp(measuredWidth, 0, getChildAt(0).getMeasuredWidth() - getMeasuredWidth()));
        return true;
    }

    void updateButtonsVisibility() {
        ValueAnimator valueAnimator;
        int childCount = this.contentView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.contentView.getChildAt(i);
            if (childAt instanceof EmojiTabsStrip.EmojiTabButton) {
                EmojiTabsStrip.EmojiTabButton emojiTabButton = (EmojiTabsStrip.EmojiTabButton) childAt;
                boolean z = true;
                boolean z2 = childAt.getRight() - getScrollX() > 0 && childAt.getLeft() - getScrollX() < getMeasuredWidth();
                if (!this.scrollingAnimation || ((valueAnimator = this.showAnimator) != null && valueAnimator.isRunning())) {
                    z = false;
                }
                emojiTabButton.updateVisibilityInbounds(z2, z);
            }
        }
    }
}
