package org.telegram.ui.Stars;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Outline;
import android.graphics.Path;
import android.os.Build;
import android.view.RoundedCorner;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.WindowInsets;
import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.CubicBezierInterpolator;

/* loaded from: classes4.dex */
public class SuperRippleFallback extends ISuperRipple {
    public final int MAX_COUNT;
    public final ArrayList effects;
    private final ViewOutlineProvider outlineProvider;
    private final Path outlineProviderPath;
    public final float[] radii;

    public static class Effect {
        public final ValueAnimator animator;
        public final float cx;
        public final float cy;
        public float duration;
        public final float intensity;
        public float t;

        private Effect(float f, float f2, float f3, ValueAnimator valueAnimator) {
            this.cx = f;
            this.cy = f2;
            this.intensity = f3;
            this.animator = valueAnimator;
        }
    }

    public SuperRippleFallback(View view) {
        super(view);
        WindowInsets rootWindowInsets;
        RoundedCorner roundedCorner;
        RoundedCorner roundedCorner2;
        RoundedCorner roundedCorner3;
        RoundedCorner roundedCorner4;
        int radius;
        float f;
        int radius2;
        float f2;
        float f3;
        int radius3;
        int radius4;
        float[] fArr = new float[8];
        this.radii = fArr;
        Path path = new Path();
        this.outlineProviderPath = path;
        this.outlineProvider = new ViewOutlineProvider() { // from class: org.telegram.ui.Stars.SuperRippleFallback.1
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view2, Outline outline) {
                if (Build.VERSION.SDK_INT >= 30) {
                    outline.setRoundRect(0, 0, view2.getWidth(), view2.getHeight(), SuperRippleFallback.this.radii[0]);
                }
            }
        };
        this.effects = new ArrayList();
        this.MAX_COUNT = 10;
        int i = Build.VERSION.SDK_INT;
        if (i >= 23) {
            rootWindowInsets = view.getRootWindowInsets();
            if (i >= 31) {
                roundedCorner = rootWindowInsets.getRoundedCorner(0);
                roundedCorner2 = rootWindowInsets.getRoundedCorner(1);
                roundedCorner3 = rootWindowInsets.getRoundedCorner(3);
                roundedCorner4 = rootWindowInsets.getRoundedCorner(2);
                float f4 = 0.0f;
                if (roundedCorner == null) {
                    f = 0.0f;
                } else {
                    radius = roundedCorner.getRadius();
                    f = radius;
                }
                fArr[1] = f;
                fArr[0] = f;
                if (roundedCorner2 == null) {
                    f2 = 0.0f;
                } else {
                    radius2 = roundedCorner2.getRadius();
                    f2 = radius2;
                }
                fArr[3] = f2;
                fArr[2] = f2;
                if (roundedCorner4 == null || (view != view.getRootView() && AndroidUtilities.navigationBarHeight > 0)) {
                    f3 = 0.0f;
                } else {
                    radius4 = roundedCorner4.getRadius();
                    f3 = radius4;
                }
                fArr[5] = f3;
                fArr[4] = f3;
                if (roundedCorner3 != null && (view == view.getRootView() || AndroidUtilities.navigationBarHeight <= 0)) {
                    radius3 = roundedCorner3.getRadius();
                    f4 = radius3;
                }
                fArr[7] = f4;
                fArr[6] = f4;
            }
        }
        path.rewind();
        path.addRoundRect(0.0f, 0.0f, view.getWidth(), view.getHeight(), fArr, Path.Direction.CW);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animate$0(Effect effect, ValueAnimator valueAnimator) {
        effect.t = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateProperties();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProperties() {
        ViewOutlineProvider outlineProvider;
        Iterator it = this.effects.iterator();
        float f = 1.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 1.0f;
        while (it.hasNext()) {
            Effect effect = (Effect) it.next();
            double d = effect.t / effect.duration;
            Double.isNaN(d);
            float sin = 1.0f - ((float) Math.sin(d * 3.141592653589793d));
            float f6 = effect.intensity * 0.04f;
            f5 *= (1.0f - f6) + (f6 * sin);
            f4 += effect.cx * 1.0f;
            f3 += effect.cy * 1.0f;
            f2 += 1.0f;
        }
        if (f2 < 1.0f) {
            float f7 = 1.0f - f2;
            f4 += (this.view.getWidth() / 2.0f) * f7;
            f3 += (this.view.getHeight() / 2.0f) * f7;
        } else {
            f = f2;
        }
        this.view.setScaleX(f5);
        this.view.setScaleY(f5);
        this.view.setPivotX(f4 / f);
        this.view.setPivotY(f3 / f);
        outlineProvider = this.view.getOutlineProvider();
        if (outlineProvider != (this.effects.isEmpty() ? null : this.outlineProvider)) {
            this.view.setOutlineProvider(this.effects.isEmpty() ? null : this.outlineProvider);
            this.view.invalidate();
        }
    }

    @Override // org.telegram.ui.Stars.ISuperRipple
    public void animate(float f, float f2, float f3) {
        if (this.effects.size() >= 10) {
            return;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 0.5f);
        final Effect effect = new Effect(f, f2, f3, ofFloat);
        effect.duration = 0.5f;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stars.SuperRippleFallback$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SuperRippleFallback.this.lambda$animate$0(effect, valueAnimator);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stars.SuperRippleFallback.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SuperRippleFallback.this.effects.remove(effect);
                SuperRippleFallback.this.updateProperties();
            }
        });
        ofFloat.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        ofFloat.setDuration(500L);
        this.effects.add(effect);
        updateProperties();
        ofFloat.start();
    }
}
