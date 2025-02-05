package org.telegram.ui.Components.voip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.animation.LinearInterpolator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.Components.BitmapShaderTools;

/* loaded from: classes3.dex */
public class VoIPBackgroundProvider {
    private final Paint darkPaint;
    private final BitmapShaderTools darkShaderTools;
    private final Paint darkVideoPaint;
    private int degree;
    private boolean hasVideo;
    private boolean isReveal;
    private final BitmapShaderTools lightShaderTools;
    private BitmapShaderTools revealDarkShaderTools;
    private BitmapShaderTools revealShaderTools;
    public final float scale;
    private int totalHeight;
    private int totalWidth;
    private final List views;
    private final Paint whiteVideoPaint;

    public VoIPBackgroundProvider() {
        BitmapShaderTools bitmapShaderTools = new BitmapShaderTools(80, 80);
        this.lightShaderTools = bitmapShaderTools;
        BitmapShaderTools bitmapShaderTools2 = new BitmapShaderTools(80, 80);
        this.darkShaderTools = bitmapShaderTools2;
        this.totalWidth = 0;
        this.totalHeight = 0;
        Paint paint = new Paint(1);
        this.whiteVideoPaint = paint;
        Paint paint2 = new Paint(1);
        this.darkVideoPaint = paint2;
        Paint paint3 = new Paint(1);
        this.darkPaint = paint3;
        this.views = new ArrayList();
        this.scale = 1.12f;
        bitmapShaderTools2.setBounds(0.0f, 0.0f, 80.0f, 80.0f);
        bitmapShaderTools.setBounds(0.0f, 0.0f, 80.0f, 80.0f);
        paint.setColor(-1);
        paint.setAlpha(35);
        paint2.setColor(-16777216);
        paint2.setAlpha(102);
        paint3.setColor(-16777216);
        paint3.setAlpha(35);
        bitmapShaderTools2.paint.setAlpha(NotificationCenter.updateBotMenuButton);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setHasVideo$0(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        int i = (int) (35.0f * floatValue);
        this.darkPaint.setAlpha(i);
        this.darkVideoPaint.setAlpha((int) (floatValue * 102.0f));
        this.whiteVideoPaint.setAlpha(i);
        invalidateViews();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setHasVideo$1(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.darkShaderTools.paint.setAlpha((int) (180.0f * floatValue));
        this.lightShaderTools.paint.setAlpha((int) (floatValue * 255.0f));
        invalidateViews();
    }

    public void attach(View view) {
        this.views.add(view);
    }

    public void detach(View view) {
        this.views.remove(view);
    }

    public Canvas getDarkCanvas() {
        return this.darkShaderTools.getCanvas();
    }

    public Paint getDarkPaint() {
        return this.hasVideo ? this.darkVideoPaint : this.darkShaderTools.paint;
    }

    public Paint getDarkPaint(boolean z) {
        return z ? this.darkPaint : getDarkPaint();
    }

    public int getDegree() {
        return this.degree;
    }

    public Canvas getLightCanvas() {
        return this.lightShaderTools.getCanvas();
    }

    public Paint getLightPaint() {
        return this.hasVideo ? this.whiteVideoPaint : this.lightShaderTools.paint;
    }

    public Canvas getRevealCanvas() {
        return this.revealShaderTools.getCanvas();
    }

    public Paint getRevealDarkPaint() {
        return this.revealDarkShaderTools.paint;
    }

    public Canvas getRevealDrakCanvas() {
        return this.revealDarkShaderTools.getCanvas();
    }

    public Paint getRevealPaint() {
        return this.revealShaderTools.paint;
    }

    public void invalidateViews() {
        Iterator it = this.views.iterator();
        while (it.hasNext()) {
            ((View) it.next()).invalidate();
        }
    }

    public boolean isReveal() {
        return this.isReveal;
    }

    public void setDarkTranslation(float f, float f2) {
        float f3 = this.totalHeight * 1.12f;
        float f4 = -f;
        float f5 = -f2;
        this.darkShaderTools.setMatrix(f4 - ((f3 - this.totalWidth) / 2.0f), f5 - ((f3 - this.totalHeight) / 2.0f), f3 / this.darkShaderTools.getBitmap().getHeight(), this.degree);
        this.revealDarkShaderTools.setBounds(f4, f5, this.totalWidth - f, this.totalHeight - f2);
    }

    public void setDegree(int i) {
        this.degree = i;
        invalidateViews();
    }

    public void setHasVideo(boolean z) {
        if (!this.hasVideo || z) {
            this.hasVideo = z;
        } else {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.voip.VoIPBackgroundProvider$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    VoIPBackgroundProvider.this.lambda$setHasVideo$0(valueAnimator);
                }
            });
            ofFloat.setInterpolator(new LinearInterpolator());
            ofFloat.setDuration(80L);
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.voip.VoIPBackgroundProvider.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    VoIPBackgroundProvider.this.hasVideo = false;
                    VoIPBackgroundProvider.this.darkPaint.setAlpha(35);
                    VoIPBackgroundProvider.this.darkVideoPaint.setAlpha(102);
                    VoIPBackgroundProvider.this.whiteVideoPaint.setAlpha(35);
                    VoIPBackgroundProvider.this.invalidateViews();
                }
            });
            ofFloat.start();
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.voip.VoIPBackgroundProvider$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    VoIPBackgroundProvider.this.lambda$setHasVideo$1(valueAnimator);
                }
            });
            ofFloat2.setInterpolator(new LinearInterpolator());
            ofFloat2.setStartDelay(80L);
            ofFloat2.setDuration(80L);
            ofFloat2.start();
        }
        invalidateViews();
    }

    public void setLightTranslation(float f, float f2) {
        float f3 = this.totalHeight;
        float f4 = 1.12f * f3;
        float f5 = -f;
        float f6 = -f2;
        this.lightShaderTools.setMatrix(f5 - ((f4 - this.totalWidth) / 2.0f), f6 - ((f4 - f3) / 2.0f), (this.totalHeight * 1.12f) / this.lightShaderTools.getBitmap().getHeight(), this.degree);
        this.revealShaderTools.setBounds(f5, f6, this.totalWidth - f, this.totalHeight - f2);
    }

    public void setReveal(boolean z) {
        this.isReveal = z;
    }

    public void setTotalSize(int i, int i2) {
        this.totalWidth = i;
        this.totalHeight = i2;
        int i3 = i / 4;
        int i4 = i2 / 4;
        this.revealShaderTools = new BitmapShaderTools(i3, i4);
        BitmapShaderTools bitmapShaderTools = new BitmapShaderTools(i3, i4);
        this.revealDarkShaderTools = bitmapShaderTools;
        bitmapShaderTools.paint.setAlpha(NotificationCenter.updateBotMenuButton);
    }
}
