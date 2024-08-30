package org.telegram.ui.Components;

import androidx.dynamicanimation.animation.FloatPropertyCompat;
/* loaded from: classes3.dex */
public class SimpleFloatPropertyCompat extends FloatPropertyCompat {
    private Getter getter;
    private float multiplier;
    private Setter setter;

    /* loaded from: classes3.dex */
    public interface Getter {
        float get(Object obj);
    }

    /* loaded from: classes3.dex */
    public interface Setter {
        void set(Object obj, float f);
    }

    public SimpleFloatPropertyCompat(String str, Getter getter, Setter setter) {
        super(str);
        this.multiplier = 1.0f;
        this.getter = getter;
        this.setter = setter;
    }

    @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
    public float getValue(Object obj) {
        return this.getter.get(obj) * this.multiplier;
    }

    public SimpleFloatPropertyCompat setMultiplier(float f) {
        this.multiplier = f;
        return this;
    }

    @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
    public void setValue(Object obj, float f) {
        this.setter.set(obj, f / this.multiplier);
    }
}
