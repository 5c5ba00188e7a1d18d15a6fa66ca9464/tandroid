package org.telegram.ui.Components.Reactions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class AddReactionsSpan extends ReplacementSpan {
    private int alpha;
    private float height;
    private StaticLayout layout;
    private final RectF rectF;
    private final TextPaint textPaint;
    private float width;

    public AddReactionsSpan(float f, Theme.ResourcesProvider resourcesProvider) {
        TextPaint textPaint = new TextPaint(1);
        this.textPaint = textPaint;
        this.rectF = new RectF();
        textPaint.setTextSize(AndroidUtilities.dp(f));
        textPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText5, resourcesProvider));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hide$1(View view, ValueAnimator valueAnimator) {
        this.alpha = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        view.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$show$0(View view, ValueAnimator valueAnimator) {
        this.alpha = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        view.invalidate();
    }

    @Override // android.text.style.ReplacementSpan
    public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
        makeLayout();
        this.rectF.set(canvas.getClipBounds());
        canvas.saveLayerAlpha(this.rectF, this.alpha, 31);
        canvas.translate(f + AndroidUtilities.dp(4.0f), (i3 + ((i5 - i3) / 2.0f)) - (this.height / 2.0f));
        this.layout.draw(canvas);
        canvas.restore();
    }

    @Override // android.text.style.ReplacementSpan
    public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
        makeLayout();
        return (int) (AndroidUtilities.dp(8.0f) + this.width);
    }

    public void hide(final View view, final Runnable runnable) {
        ValueAnimator ofInt = ValueAnimator.ofInt(this.alpha, 0);
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Reactions.AddReactionsSpan$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                AddReactionsSpan.this.lambda$hide$1(view, valueAnimator);
            }
        });
        ofInt.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Reactions.AddReactionsSpan.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                runnable.run();
            }
        });
        ofInt.setDuration(200L);
        ofInt.start();
    }

    public void makeLayout() {
        if (this.layout == null) {
            StaticLayout staticLayout = new StaticLayout(LocaleController.getString(R.string.ReactionAddReactionsHint), this.textPaint, AndroidUtilities.displaySize.x, LocaleController.isRTL ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.layout = staticLayout;
            this.width = staticLayout.getLineWidth(0);
            this.height = this.layout.getHeight();
        }
    }

    public void show(final View view) {
        ValueAnimator ofInt = ValueAnimator.ofInt(this.alpha, NotificationCenter.voipServiceCreated);
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Reactions.AddReactionsSpan$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                AddReactionsSpan.this.lambda$show$0(view, valueAnimator);
            }
        });
        ofInt.setDuration(200L);
        ofInt.start();
    }
}
