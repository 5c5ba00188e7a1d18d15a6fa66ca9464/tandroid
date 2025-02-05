package org.telegram.ui.Components;

import android.animation.TimeInterpolator;
import android.os.SystemClock;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;

/* loaded from: classes3.dex */
public class AnimatedColor {
    private boolean firstSet;
    private Runnable invalidate;
    private View parent;
    private int startValue;
    private int targetValue;
    private boolean transition;
    private long transitionDelay;
    private long transitionDuration;
    private TimeInterpolator transitionInterpolator;
    private long transitionStart;
    private int value;

    public AnimatedColor(View view) {
        this.transitionDelay = 0L;
        this.transitionDuration = 200L;
        this.transitionInterpolator = CubicBezierInterpolator.DEFAULT;
        this.parent = view;
        this.firstSet = true;
    }

    public AnimatedColor(View view, long j, long j2, TimeInterpolator timeInterpolator) {
        this.transitionDelay = 0L;
        this.transitionDuration = 200L;
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
        this.parent = view;
        this.transitionDelay = j;
        this.transitionDuration = j2;
        this.transitionInterpolator = timeInterpolator;
        this.firstSet = true;
    }

    public AnimatedColor(View view, long j, TimeInterpolator timeInterpolator) {
        this.transitionDelay = 0L;
        this.transitionDuration = 200L;
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
        this.parent = view;
        this.transitionDuration = j;
        this.transitionInterpolator = timeInterpolator;
        this.firstSet = true;
    }

    public AnimatedColor(Runnable runnable, long j, TimeInterpolator timeInterpolator) {
        this.transitionDelay = 0L;
        this.transitionDuration = 200L;
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
        this.invalidate = runnable;
        this.transitionDuration = j;
        this.transitionInterpolator = timeInterpolator;
        this.firstSet = true;
    }

    public int get() {
        return this.value;
    }

    public int set(int i) {
        return set(i, false);
    }

    public int set(int i, boolean z) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (z || this.transitionDuration <= 0 || this.firstSet) {
            this.targetValue = i;
            this.value = i;
            this.transition = false;
            this.firstSet = false;
        } else if (this.targetValue != i) {
            this.transition = true;
            this.targetValue = i;
            this.startValue = this.value;
            this.transitionStart = elapsedRealtime;
        }
        if (this.transition) {
            float clamp = MathUtils.clamp(((elapsedRealtime - this.transitionStart) - this.transitionDelay) / this.transitionDuration, 0.0f, 1.0f);
            if (elapsedRealtime - this.transitionStart >= this.transitionDelay) {
                TimeInterpolator timeInterpolator = this.transitionInterpolator;
                this.value = timeInterpolator == null ? ColorUtils.blendARGB(this.startValue, this.targetValue, clamp) : ColorUtils.blendARGB(this.startValue, this.targetValue, timeInterpolator.getInterpolation(clamp));
            }
            if (clamp >= 1.0f) {
                this.transition = false;
            } else {
                View view = this.parent;
                if (view != null) {
                    view.invalidate();
                }
                Runnable runnable = this.invalidate;
                if (runnable != null) {
                    runnable.run();
                }
            }
        }
        return this.value;
    }
}
