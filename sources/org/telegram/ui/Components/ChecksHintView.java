package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.ChecksHintView;
/* loaded from: classes3.dex */
public class ChecksHintView extends FrameLayout {
    private AnimatorSet animatorSet;
    private ImageView arrowImageView;
    private View currentView;
    private Runnable hideRunnable;
    private RLottieImageView[] imageView;
    private ChatMessageCell messageCell;
    private final Theme.ResourcesProvider resourcesProvider;
    private long showingDuration;
    private TextView[] textView;
    private float translationY;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 1 extends AnimatorListenerAdapter {
        1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$0() {
            ChecksHintView.this.hide();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ChecksHintView.this.animatorSet = null;
            AndroidUtilities.runOnUIThread(ChecksHintView.this.hideRunnable = new Runnable() { // from class: org.telegram.ui.Components.ChecksHintView$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ChecksHintView.1.this.lambda$onAnimationEnd$0();
                }
            }, 3000L);
        }
    }

    public ChecksHintView(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        TextView textView;
        int i;
        this.textView = new TextView[2];
        this.imageView = new RLottieImageView[2];
        this.showingDuration = 2000L;
        this.resourcesProvider = resourcesProvider;
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), getThemedColor(Theme.key_chat_gifSaveHintBackground)));
        int i2 = 0;
        frameLayout.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f));
        addView(frameLayout, LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 6.0f));
        while (i2 < 2) {
            this.imageView[i2] = new RLottieImageView(context);
            this.imageView[i2].setScaleType(ImageView.ScaleType.CENTER);
            frameLayout.addView(this.imageView[i2], LayoutHelper.createFrame(24, 24.0f, 51, 0.0f, i2 == 0 ? 0.0f : 24.0f, 0.0f, 0.0f));
            this.textView[i2] = new TextView(context);
            this.textView[i2].setTextColor(getThemedColor(Theme.key_chat_gifSaveHintText));
            this.textView[i2].setTextSize(1, 14.0f);
            this.textView[i2].setMaxLines(1);
            this.textView[i2].setSingleLine(true);
            this.textView[i2].setMaxWidth(AndroidUtilities.dp(250.0f));
            this.textView[i2].setGravity(51);
            this.textView[i2].setPivotX(0.0f);
            frameLayout.addView(this.textView[i2], LayoutHelper.createFrame(-2, -2.0f, 51, 32.0f, i2 == 0 ? 2.0f : 26.0f, 10.0f, 0.0f));
            RLottieImageView[] rLottieImageViewArr = this.imageView;
            if (i2 == 0) {
                rLottieImageViewArr[i2].setAnimation(R.raw.ticks_single, 24, 24);
                textView = this.textView[i2];
                i = R.string.HintSent;
            } else {
                rLottieImageViewArr[i2].setAnimation(R.raw.ticks_double, 24, 24);
                textView = this.textView[i2];
                i = R.string.HintRead;
            }
            textView.setText(LocaleController.getString(i));
            this.imageView[i2].playAnimation();
            i2++;
        }
        ImageView imageView = new ImageView(context);
        this.arrowImageView = imageView;
        imageView.setImageResource(R.drawable.tooltip_arrow);
        this.arrowImageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_gifSaveHintBackground), PorterDuff.Mode.MULTIPLY));
        addView(this.arrowImageView, LayoutHelper.createFrame(14, 6.0f, 83, 0.0f, 0.0f, 0.0f, 0.0f));
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    public float getBaseTranslationY() {
        return this.translationY;
    }

    public void hide() {
        if (getTag() == null) {
            return;
        }
        setTag(null);
        Runnable runnable = this.hideRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.hideRunnable = null;
        }
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.animatorSet = null;
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.animatorSet = animatorSet2;
        animatorSet2.playTogether(ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this, View.SCALE_X, 0.0f), ObjectAnimator.ofFloat(this, View.SCALE_Y, 0.0f));
        this.animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChecksHintView.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ChecksHintView.this.setVisibility(4);
                ChecksHintView.this.currentView = null;
                ChecksHintView.this.messageCell = null;
                ChecksHintView.this.animatorSet = null;
            }
        });
        this.animatorSet.setDuration(180L);
        this.animatorSet.start();
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x00b3, code lost:
        if (r6 < org.telegram.messenger.AndroidUtilities.dp(10.0f)) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00e5, code lost:
        if (r6 < org.telegram.messenger.AndroidUtilities.dp(10.0f)) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00e7, code lost:
        r3 = r6 - org.telegram.messenger.AndroidUtilities.dp(10.0f);
        setTranslationX(getTranslationX() + r3);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean showForMessageCell(ChatMessageCell chatMessageCell, boolean z) {
        float measuredWidth;
        final int i = 0;
        Runnable runnable = this.hideRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.hideRunnable = null;
        }
        int[] iArr = new int[2];
        chatMessageCell.getLocationInWindow(iArr);
        int i2 = iArr[1];
        ((View) getParent()).getLocationInWindow(iArr);
        int i3 = i2 - iArr[1];
        View view = (View) chatMessageCell.getParent();
        measure(View.MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(1000, Integer.MIN_VALUE));
        if (i3 <= getMeasuredHeight() + AndroidUtilities.dp(10.0f)) {
            return false;
        }
        int checksY = i3 + chatMessageCell.getChecksY() + AndroidUtilities.dp(6.0f);
        int checksX = chatMessageCell.getChecksX() + AndroidUtilities.dp(5.0f);
        int measuredWidth2 = view.getMeasuredWidth();
        float measuredHeight = checksY - getMeasuredHeight();
        this.translationY = measuredHeight;
        setTranslationY(measuredHeight);
        int left = chatMessageCell.getLeft() + checksX;
        int dp = AndroidUtilities.dp(15.0f);
        if (left > view.getMeasuredWidth() / 2) {
            int measuredWidth3 = (measuredWidth2 - getMeasuredWidth()) - AndroidUtilities.dp(20.0f);
            setTranslationX(measuredWidth3);
            dp += measuredWidth3;
        } else {
            setTranslationX(0.0f);
        }
        float left2 = ((chatMessageCell.getLeft() + checksX) - dp) - (this.arrowImageView.getMeasuredWidth() / 2);
        this.arrowImageView.setTranslationX(left2);
        if (left <= view.getMeasuredWidth() / 2) {
            if (left2 > getMeasuredWidth() - AndroidUtilities.dp(24.0f)) {
                measuredWidth = (left2 - getMeasuredWidth()) + AndroidUtilities.dp(24.0f);
                setTranslationX(measuredWidth);
            }
        }
        this.arrowImageView.setTranslationX(left2 - measuredWidth);
        setPivotX(left2);
        setPivotY(getMeasuredHeight());
        this.messageCell = chatMessageCell;
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.animatorSet = null;
        }
        setTag(1);
        setVisibility(0);
        if (z) {
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.animatorSet = animatorSet2;
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f, 1.0f), ObjectAnimator.ofFloat(this, View.SCALE_X, 0.0f, 1.0f), ObjectAnimator.ofFloat(this, View.SCALE_Y, 0.0f, 1.0f));
            this.animatorSet.addListener(new 1());
            this.animatorSet.setDuration(180L);
            this.animatorSet.start();
            while (i < 2) {
                this.textView[i].animate().scaleX(1.04f).scaleY(1.04f).setInterpolator(CubicBezierInterpolator.EASE_IN).setStartDelay((i == 0 ? NotificationCenter.httpFileDidFailedLoad : 500) + NotificationCenter.filePreparingStarted).setDuration(100L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChecksHintView.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        ChecksHintView.this.textView[i].animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(CubicBezierInterpolator.EASE_OUT).setStartDelay(0L).setDuration(100L).start();
                    }
                }).start();
                i++;
            }
        } else {
            setAlpha(1.0f);
        }
        return true;
    }
}
