package org.telegram.ui.Stars;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.RenderEffect;
import android.graphics.RuntimeShader;
import android.view.RoundedCorner;
import android.view.View;
import android.view.WindowInsets;
import com.google.zxing.common.detector.MathUtils;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationBadge;
import org.telegram.messenger.R;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.RLottieDrawable;
/* loaded from: classes3.dex */
public class SuperRipple extends ISuperRipple {
    public final float[] centerX;
    public final float[] centerY;
    public int count;
    public float density;
    public RenderEffect effect;
    public final ArrayList<Effect> effects;
    public int height;
    public final float[] intensity;
    public final RuntimeShader shader;
    public final float[] t;
    public int width;

    /* loaded from: classes3.dex */
    public static class Effect {
        public final float cx;
        public final float cy;
        public final float intensity;
        public float t;

        private Effect(float f, float f2, float f3, ValueAnimator valueAnimator) {
            this.cx = f;
            this.cy = f2;
            this.intensity = f3;
        }
    }

    public SuperRipple(View view) {
        super(view);
        this.effects = new ArrayList<>();
        this.t = new float[7];
        this.centerX = new float[7];
        this.centerY = new float[7];
        this.intensity = new float[7];
        RuntimeShader runtimeShader = new RuntimeShader(RLottieDrawable.readRes(null, R.raw.superripple_effect));
        this.shader = runtimeShader;
        setupSizeUniforms(true);
        this.effect = RenderEffect.createRuntimeShaderEffect(runtimeShader, "img");
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x0091  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0093  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00b2  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00b4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setupSizeUniforms(boolean z) {
        float f;
        float f2;
        if (z || this.width != this.view.getWidth() || this.height != this.view.getHeight() || Math.abs(this.density - AndroidUtilities.density) > 0.01f) {
            RuntimeShader runtimeShader = this.shader;
            int width = this.view.getWidth();
            this.width = width;
            int height = this.view.getHeight();
            this.height = height;
            runtimeShader.setFloatUniform("size", width, height);
            RuntimeShader runtimeShader2 = this.shader;
            float f3 = AndroidUtilities.density;
            this.density = f3;
            runtimeShader2.setFloatUniform("density", f3);
            WindowInsets rootWindowInsets = this.view.getRootWindowInsets();
            RoundedCorner roundedCorner = rootWindowInsets == null ? null : rootWindowInsets.getRoundedCorner(0);
            RoundedCorner roundedCorner2 = rootWindowInsets == null ? null : rootWindowInsets.getRoundedCorner(1);
            RoundedCorner roundedCorner3 = rootWindowInsets == null ? null : rootWindowInsets.getRoundedCorner(3);
            RoundedCorner roundedCorner4 = rootWindowInsets != null ? rootWindowInsets.getRoundedCorner(2) : null;
            RuntimeShader runtimeShader3 = this.shader;
            if (roundedCorner4 != null) {
                View view = this.view;
                if (view == view.getRootView() || AndroidUtilities.navigationBarHeight <= 0) {
                    f = roundedCorner4.getRadius();
                    float radius = roundedCorner2 != null ? 0.0f : roundedCorner2.getRadius();
                    if (roundedCorner3 != null) {
                        View view2 = this.view;
                        if (view2 == view2.getRootView() || AndroidUtilities.navigationBarHeight <= 0) {
                            f2 = roundedCorner3.getRadius();
                            runtimeShader3.setFloatUniform("radius", f, radius, f2, roundedCorner == null ? 0.0f : roundedCorner.getRadius());
                        }
                    }
                    f2 = 0.0f;
                    runtimeShader3.setFloatUniform("radius", f, radius, f2, roundedCorner == null ? 0.0f : roundedCorner.getRadius());
                }
            }
            f = 0.0f;
            if (roundedCorner2 != null) {
            }
            if (roundedCorner3 != null) {
            }
            f2 = 0.0f;
            runtimeShader3.setFloatUniform("radius", f, radius, f2, roundedCorner == null ? 0.0f : roundedCorner.getRadius());
        }
    }

    @Override // org.telegram.ui.Stars.ISuperRipple
    public void animate(float f, float f2, float f3) {
        if (this.effects.size() >= 7) {
            return;
        }
        float max = (Math.max(Math.max(MathUtils.distance(0.0f, 0.0f, f, f2), MathUtils.distance(this.view.getWidth(), 0.0f, f, f2)), Math.max(MathUtils.distance(0.0f, this.view.getHeight(), f, f2), MathUtils.distance(this.view.getWidth(), this.view.getHeight(), f, f2))) * 2.0f) / (AndroidUtilities.density * 1200.0f);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, max);
        final Effect effect = new Effect(f, f2, f3, ofFloat);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stars.SuperRipple$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SuperRipple.this.lambda$animate$0(effect, valueAnimator);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stars.SuperRipple.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SuperRipple.this.effects.remove(effect);
                SuperRipple.this.updateProperties();
            }
        });
        ofFloat.setInterpolator(CubicBezierInterpolator.EASE_OUT);
        ofFloat.setDuration(max * 1000.0f);
        this.effects.add(effect);
        updateProperties();
        ofFloat.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animate$0(Effect effect, ValueAnimator valueAnimator) {
        effect.t = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateProperties();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProperties() {
        boolean z = false;
        if (!this.effects.isEmpty()) {
            boolean z2 = true;
            boolean z3 = this.count != Math.min(7, this.effects.size());
            this.count = Math.min(7, this.effects.size());
            for (int i = 0; i < this.count; i++) {
                Effect effect = this.effects.get(i);
                boolean z4 = z3 || Math.abs(this.t[i] - effect.t) > 0.001f;
                this.t[i] = effect.t;
                boolean z5 = z4 || Math.abs(this.centerX[i] - effect.cx) > 0.001f;
                this.centerX[i] = effect.cx;
                boolean z6 = z5 || Math.abs(this.centerY[i] - effect.cy) > 0.001f;
                this.centerY[i] = effect.cy;
                z3 = z6 || Math.abs(this.intensity[i] - effect.intensity) > 0.001f;
                this.intensity[i] = effect.intensity;
            }
            if (!z3 && this.width == this.view.getWidth() && this.height == this.view.getHeight() && Math.abs(this.density - AndroidUtilities.density) <= 0.01f) {
                z2 = false;
            }
            if (z2) {
                this.shader.setIntUniform(NotificationBadge.NewHtcHomeBadger.COUNT, this.count);
                this.shader.setFloatUniform("t", this.t);
                this.shader.setFloatUniform("centerX", this.centerX);
                this.shader.setFloatUniform("centerY", this.centerY);
                this.shader.setFloatUniform("intensity", this.intensity);
                setupSizeUniforms(false);
                this.effect = RenderEffect.createRuntimeShaderEffect(this.shader, "img");
            }
            z = z2;
        }
        this.view.setRenderEffect(this.effects.isEmpty() ? null : this.effect);
        if (z) {
            this.view.invalidate();
        }
    }
}
