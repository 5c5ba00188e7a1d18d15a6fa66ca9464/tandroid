package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.view.View;
import java.util.ArrayList;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.Components.Reactions.HwEmojis;

/* loaded from: classes3.dex */
public class EllipsizeSpanAnimator {
    boolean attachedToWindow;
    private final AnimatorSet ellAnimator;
    private final TextAlphaSpan[] ellSpans;
    public ArrayList ellipsizedViews;

    /* JADX INFO: Access modifiers changed from: private */
    static class TextAlphaSpan extends CharacterStyle {
        private int alpha = 0;

        public void setAlpha(int i) {
            this.alpha = i;
        }

        @Override // android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            textPaint.setAlpha((int) (textPaint.getAlpha() * (this.alpha / 255.0f)));
        }
    }

    public EllipsizeSpanAnimator(final View view) {
        TextAlphaSpan[] textAlphaSpanArr = {new TextAlphaSpan(), new TextAlphaSpan(), new TextAlphaSpan()};
        this.ellSpans = textAlphaSpanArr;
        this.ellipsizedViews = new ArrayList();
        AnimatorSet animatorSet = new AnimatorSet();
        this.ellAnimator = animatorSet;
        animatorSet.playTogether(createEllipsizeAnimator(textAlphaSpanArr[0], 0, NotificationCenter.liveLocationsChanged, 0, NotificationCenter.chatlistFolderUpdate), createEllipsizeAnimator(textAlphaSpanArr[1], 0, NotificationCenter.liveLocationsChanged, 150, NotificationCenter.chatlistFolderUpdate), createEllipsizeAnimator(textAlphaSpanArr[2], 0, NotificationCenter.liveLocationsChanged, NotificationCenter.chatlistFolderUpdate, NotificationCenter.chatlistFolderUpdate), createEllipsizeAnimator(textAlphaSpanArr[0], NotificationCenter.liveLocationsChanged, 0, MediaDataController.MAX_STYLE_RUNS_COUNT, 400), createEllipsizeAnimator(textAlphaSpanArr[1], NotificationCenter.liveLocationsChanged, 0, MediaDataController.MAX_STYLE_RUNS_COUNT, 400), createEllipsizeAnimator(textAlphaSpanArr[2], NotificationCenter.liveLocationsChanged, 0, MediaDataController.MAX_STYLE_RUNS_COUNT, 400));
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.EllipsizeSpanAnimator.1
            private Runnable restarter = new Runnable() { // from class: org.telegram.ui.Components.EllipsizeSpanAnimator.1.1
                @Override // java.lang.Runnable
                public void run() {
                    EllipsizeSpanAnimator ellipsizeSpanAnimator = EllipsizeSpanAnimator.this;
                    if (!ellipsizeSpanAnimator.attachedToWindow || ellipsizeSpanAnimator.ellipsizedViews.isEmpty() || EllipsizeSpanAnimator.this.ellAnimator.isRunning()) {
                        return;
                    }
                    try {
                        EllipsizeSpanAnimator.this.ellAnimator.start();
                    } catch (Exception unused) {
                    }
                }
            };

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (EllipsizeSpanAnimator.this.attachedToWindow) {
                    view.postDelayed(this.restarter, 300L);
                }
            }
        });
    }

    private Animator createEllipsizeAnimator(final TextAlphaSpan textAlphaSpan, int i, int i2, int i3, int i4) {
        ValueAnimator ofInt = ValueAnimator.ofInt(i, i2);
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.EllipsizeSpanAnimator$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                EllipsizeSpanAnimator.this.lambda$createEllipsizeAnimator$0(textAlphaSpan, valueAnimator);
            }
        });
        ofInt.setDuration(i4);
        ofInt.setStartDelay(i3);
        ofInt.setInterpolator(CubicBezierInterpolator.DEFAULT);
        return ofInt;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createEllipsizeAnimator$0(TextAlphaSpan textAlphaSpan, ValueAnimator valueAnimator) {
        textAlphaSpan.setAlpha(((Integer) valueAnimator.getAnimatedValue()).intValue());
        for (int i = 0; i < this.ellipsizedViews.size(); i++) {
            if (!HwEmojis.isHwEnabled()) {
                ((View) this.ellipsizedViews.get(i)).invalidate();
            }
        }
    }

    public void addView(View view) {
        if (this.ellipsizedViews.isEmpty()) {
            this.ellAnimator.start();
        }
        if (this.ellipsizedViews.contains(view)) {
            return;
        }
        this.ellipsizedViews.add(view);
    }

    public void onAttachedToWindow() {
        this.attachedToWindow = true;
        if (this.ellAnimator.isRunning()) {
            return;
        }
        this.ellAnimator.start();
    }

    public void onDetachedFromWindow() {
        this.attachedToWindow = false;
        this.ellAnimator.cancel();
    }

    public void removeView(View view) {
        this.ellipsizedViews.remove(view);
        if (this.ellipsizedViews.isEmpty()) {
            this.ellAnimator.cancel();
        }
    }

    public void wrap(SpannableString spannableString, int i) {
        int i2 = i + 1;
        spannableString.setSpan(this.ellSpans[0], i, i2, 0);
        int i3 = i + 2;
        spannableString.setSpan(this.ellSpans[1], i2, i3, 0);
        spannableString.setSpan(this.ellSpans[2], i3, i + 3, 0);
    }
}
