package androidx.dynamicanimation.animation;

import android.os.Looper;
import android.util.AndroidRuntimeException;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.dynamicanimation.animation.AnimationHandler;
import androidx.dynamicanimation.animation.DynamicAnimation;
import java.util.ArrayList;
/* loaded from: classes.dex */
public abstract class DynamicAnimation<T extends DynamicAnimation<T>> implements AnimationHandler.AnimationFrameCallback {
    private final ArrayList<OnAnimationEndListener> mEndListeners;
    private long mLastFrameTime;
    float mMaxValue;
    float mMinValue;
    private float mMinVisibleChange;
    final FloatPropertyCompat mProperty;
    boolean mRunning;
    boolean mStartValueIsSet;
    final Object mTarget;
    private final ArrayList<OnAnimationUpdateListener> mUpdateListeners;
    float mValue;
    float mVelocity;
    public static final ViewProperty TRANSLATION_X = new AnonymousClass1("translationX");
    public static final ViewProperty TRANSLATION_Y = new AnonymousClass2("translationY");
    public static final ViewProperty SCALE_X = new AnonymousClass4("scaleX");
    public static final ViewProperty SCALE_Y = new AnonymousClass5("scaleY");
    public static final ViewProperty ROTATION = new AnonymousClass6("rotation");
    public static final ViewProperty ROTATION_X = new AnonymousClass7("rotationX");
    public static final ViewProperty ROTATION_Y = new AnonymousClass8("rotationY");
    public static final ViewProperty ALPHA = new AnonymousClass12("alpha");

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class MassState {
        float mValue;
        float mVelocity;
    }

    /* loaded from: classes.dex */
    public interface OnAnimationEndListener {
        void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2);
    }

    /* loaded from: classes.dex */
    public interface OnAnimationUpdateListener {
        void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2);
    }

    abstract void setValueThreshold(float f);

    abstract boolean updateValueAndVelocity(long j);

    /* loaded from: classes.dex */
    public static abstract class ViewProperty extends FloatPropertyCompat<View> {
        /* synthetic */ ViewProperty(String str, AnonymousClass1 anonymousClass1) {
            this(str);
        }

        private ViewProperty(String str) {
            super(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.dynamicanimation.animation.DynamicAnimation$1 */
    /* loaded from: classes.dex */
    public static class AnonymousClass1 extends ViewProperty {
        AnonymousClass1(String str) {
            super(str, null);
        }

        public void setValue(View view, float f) {
            view.setTranslationX(f);
        }

        public float getValue(View view) {
            return view.getTranslationX();
        }
    }

    static {
        new AnonymousClass3("translationZ");
        new AnonymousClass9("x");
        new AnonymousClass10("y");
        new AnonymousClass11("z");
        new AnonymousClass13("scrollX");
        new AnonymousClass14("scrollY");
    }

    /* renamed from: androidx.dynamicanimation.animation.DynamicAnimation$2 */
    /* loaded from: classes.dex */
    static class AnonymousClass2 extends ViewProperty {
        AnonymousClass2(String str) {
            super(str, null);
        }

        public void setValue(View view, float f) {
            view.setTranslationY(f);
        }

        public float getValue(View view) {
            return view.getTranslationY();
        }
    }

    /* renamed from: androidx.dynamicanimation.animation.DynamicAnimation$3 */
    /* loaded from: classes.dex */
    static class AnonymousClass3 extends ViewProperty {
        AnonymousClass3(String str) {
            super(str, null);
        }

        public void setValue(View view, float f) {
            ViewCompat.setTranslationZ(view, f);
        }

        public float getValue(View view) {
            return ViewCompat.getTranslationZ(view);
        }
    }

    /* renamed from: androidx.dynamicanimation.animation.DynamicAnimation$4 */
    /* loaded from: classes.dex */
    static class AnonymousClass4 extends ViewProperty {
        AnonymousClass4(String str) {
            super(str, null);
        }

        public void setValue(View view, float f) {
            view.setScaleX(f);
        }

        public float getValue(View view) {
            return view.getScaleX();
        }
    }

    /* renamed from: androidx.dynamicanimation.animation.DynamicAnimation$5 */
    /* loaded from: classes.dex */
    static class AnonymousClass5 extends ViewProperty {
        AnonymousClass5(String str) {
            super(str, null);
        }

        public void setValue(View view, float f) {
            view.setScaleY(f);
        }

        public float getValue(View view) {
            return view.getScaleY();
        }
    }

    /* renamed from: androidx.dynamicanimation.animation.DynamicAnimation$6 */
    /* loaded from: classes.dex */
    static class AnonymousClass6 extends ViewProperty {
        AnonymousClass6(String str) {
            super(str, null);
        }

        public void setValue(View view, float f) {
            view.setRotation(f);
        }

        public float getValue(View view) {
            return view.getRotation();
        }
    }

    /* renamed from: androidx.dynamicanimation.animation.DynamicAnimation$7 */
    /* loaded from: classes.dex */
    static class AnonymousClass7 extends ViewProperty {
        AnonymousClass7(String str) {
            super(str, null);
        }

        public void setValue(View view, float f) {
            view.setRotationX(f);
        }

        public float getValue(View view) {
            return view.getRotationX();
        }
    }

    /* renamed from: androidx.dynamicanimation.animation.DynamicAnimation$8 */
    /* loaded from: classes.dex */
    static class AnonymousClass8 extends ViewProperty {
        AnonymousClass8(String str) {
            super(str, null);
        }

        public void setValue(View view, float f) {
            view.setRotationY(f);
        }

        public float getValue(View view) {
            return view.getRotationY();
        }
    }

    /* renamed from: androidx.dynamicanimation.animation.DynamicAnimation$9 */
    /* loaded from: classes.dex */
    static class AnonymousClass9 extends ViewProperty {
        AnonymousClass9(String str) {
            super(str, null);
        }

        public void setValue(View view, float f) {
            view.setX(f);
        }

        public float getValue(View view) {
            return view.getX();
        }
    }

    /* renamed from: androidx.dynamicanimation.animation.DynamicAnimation$10 */
    /* loaded from: classes.dex */
    static class AnonymousClass10 extends ViewProperty {
        AnonymousClass10(String str) {
            super(str, null);
        }

        public void setValue(View view, float f) {
            view.setY(f);
        }

        public float getValue(View view) {
            return view.getY();
        }
    }

    /* renamed from: androidx.dynamicanimation.animation.DynamicAnimation$11 */
    /* loaded from: classes.dex */
    static class AnonymousClass11 extends ViewProperty {
        AnonymousClass11(String str) {
            super(str, null);
        }

        public void setValue(View view, float f) {
            ViewCompat.setZ(view, f);
        }

        public float getValue(View view) {
            return ViewCompat.getZ(view);
        }
    }

    /* renamed from: androidx.dynamicanimation.animation.DynamicAnimation$12 */
    /* loaded from: classes.dex */
    static class AnonymousClass12 extends ViewProperty {
        AnonymousClass12(String str) {
            super(str, null);
        }

        public void setValue(View view, float f) {
            view.setAlpha(f);
        }

        public float getValue(View view) {
            return view.getAlpha();
        }
    }

    /* renamed from: androidx.dynamicanimation.animation.DynamicAnimation$13 */
    /* loaded from: classes.dex */
    static class AnonymousClass13 extends ViewProperty {
        AnonymousClass13(String str) {
            super(str, null);
        }

        public void setValue(View view, float f) {
            view.setScrollX((int) f);
        }

        public float getValue(View view) {
            return view.getScrollX();
        }
    }

    /* renamed from: androidx.dynamicanimation.animation.DynamicAnimation$14 */
    /* loaded from: classes.dex */
    static class AnonymousClass14 extends ViewProperty {
        AnonymousClass14(String str) {
            super(str, null);
        }

        public void setValue(View view, float f) {
            view.setScrollY((int) f);
        }

        public float getValue(View view) {
            return view.getScrollY();
        }
    }

    public DynamicAnimation(FloatValueHolder floatValueHolder) {
        this.mVelocity = 0.0f;
        this.mValue = Float.MAX_VALUE;
        this.mStartValueIsSet = false;
        this.mRunning = false;
        this.mMaxValue = Float.MAX_VALUE;
        this.mMinValue = -Float.MAX_VALUE;
        this.mLastFrameTime = 0L;
        this.mEndListeners = new ArrayList<>();
        this.mUpdateListeners = new ArrayList<>();
        this.mTarget = null;
        this.mProperty = new AnonymousClass15(this, "FloatValueHolder", floatValueHolder);
        this.mMinVisibleChange = 1.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.dynamicanimation.animation.DynamicAnimation$15 */
    /* loaded from: classes.dex */
    public class AnonymousClass15 extends FloatPropertyCompat {
        final /* synthetic */ FloatValueHolder val$floatValueHolder;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass15(DynamicAnimation dynamicAnimation, String str, FloatValueHolder floatValueHolder) {
            super(str);
            this.val$floatValueHolder = floatValueHolder;
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(Object obj) {
            return this.val$floatValueHolder.getValue();
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(Object obj, float f) {
            this.val$floatValueHolder.setValue(f);
        }
    }

    public <K> DynamicAnimation(K k, FloatPropertyCompat<K> floatPropertyCompat) {
        this.mVelocity = 0.0f;
        this.mValue = Float.MAX_VALUE;
        this.mStartValueIsSet = false;
        this.mRunning = false;
        this.mMaxValue = Float.MAX_VALUE;
        this.mMinValue = -Float.MAX_VALUE;
        this.mLastFrameTime = 0L;
        this.mEndListeners = new ArrayList<>();
        this.mUpdateListeners = new ArrayList<>();
        this.mTarget = k;
        this.mProperty = floatPropertyCompat;
        if (floatPropertyCompat == ROTATION || floatPropertyCompat == ROTATION_X || floatPropertyCompat == ROTATION_Y) {
            this.mMinVisibleChange = 0.1f;
        } else if (floatPropertyCompat == ALPHA) {
            this.mMinVisibleChange = 0.00390625f;
        } else if (floatPropertyCompat == SCALE_X || floatPropertyCompat == SCALE_Y) {
            this.mMinVisibleChange = 0.00390625f;
        } else {
            this.mMinVisibleChange = 1.0f;
        }
    }

    public T setStartValue(float f) {
        this.mValue = f;
        this.mStartValueIsSet = true;
        return this;
    }

    public T setStartVelocity(float f) {
        this.mVelocity = f;
        return this;
    }

    public T setMaxValue(float f) {
        this.mMaxValue = f;
        return this;
    }

    public T setMinValue(float f) {
        this.mMinValue = f;
        return this;
    }

    public T addEndListener(OnAnimationEndListener onAnimationEndListener) {
        if (!this.mEndListeners.contains(onAnimationEndListener)) {
            this.mEndListeners.add(onAnimationEndListener);
        }
        return this;
    }

    public void removeEndListener(OnAnimationEndListener onAnimationEndListener) {
        removeEntry(this.mEndListeners, onAnimationEndListener);
    }

    public T addUpdateListener(OnAnimationUpdateListener onAnimationUpdateListener) {
        if (isRunning()) {
            throw new UnsupportedOperationException("Error: Update listeners must be added beforethe animation.");
        }
        if (!this.mUpdateListeners.contains(onAnimationUpdateListener)) {
            this.mUpdateListeners.add(onAnimationUpdateListener);
        }
        return this;
    }

    public T setMinimumVisibleChange(float f) {
        if (f <= 0.0f) {
            throw new IllegalArgumentException("Minimum visible change must be positive.");
        }
        this.mMinVisibleChange = f;
        setValueThreshold(f * 0.75f);
        return this;
    }

    private static <T> void removeNullEntries(ArrayList<T> arrayList) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            if (arrayList.get(size) == null) {
                arrayList.remove(size);
            }
        }
    }

    private static <T> void removeEntry(ArrayList<T> arrayList, T t) {
        int indexOf = arrayList.indexOf(t);
        if (indexOf >= 0) {
            arrayList.set(indexOf, null);
        }
    }

    public void start() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new AndroidRuntimeException("Animations may only be started on the main thread");
        }
        if (this.mRunning) {
            return;
        }
        startAnimationInternal();
    }

    public void cancel() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new AndroidRuntimeException("Animations may only be canceled on the main thread");
        }
        if (!this.mRunning) {
            return;
        }
        endAnimationInternal(true);
    }

    public boolean isRunning() {
        return this.mRunning;
    }

    private void startAnimationInternal() {
        if (!this.mRunning) {
            this.mRunning = true;
            if (!this.mStartValueIsSet) {
                this.mValue = getPropertyValue();
            }
            float f = this.mValue;
            if (f > this.mMaxValue || f < this.mMinValue) {
                throw new IllegalArgumentException("Starting value need to be in between min value and max value");
            }
            AnimationHandler.getInstance().addAnimationFrameCallback(this, 0L);
        }
    }

    @Override // androidx.dynamicanimation.animation.AnimationHandler.AnimationFrameCallback
    public boolean doAnimationFrame(long j) {
        long j2 = this.mLastFrameTime;
        if (j2 == 0) {
            this.mLastFrameTime = j;
            setPropertyValue(this.mValue);
            return false;
        }
        this.mLastFrameTime = j;
        boolean updateValueAndVelocity = updateValueAndVelocity(j - j2);
        float min = Math.min(this.mValue, this.mMaxValue);
        this.mValue = min;
        float max = Math.max(min, this.mMinValue);
        this.mValue = max;
        setPropertyValue(max);
        if (updateValueAndVelocity) {
            endAnimationInternal(false);
        }
        return updateValueAndVelocity;
    }

    private void endAnimationInternal(boolean z) {
        this.mRunning = false;
        AnimationHandler.getInstance().removeCallback(this);
        this.mLastFrameTime = 0L;
        this.mStartValueIsSet = false;
        for (int i = 0; i < this.mEndListeners.size(); i++) {
            if (this.mEndListeners.get(i) != null) {
                this.mEndListeners.get(i).onAnimationEnd(this, z, this.mValue, this.mVelocity);
            }
        }
        removeNullEntries(this.mEndListeners);
    }

    void setPropertyValue(float f) {
        this.mProperty.setValue(this.mTarget, f);
        for (int i = 0; i < this.mUpdateListeners.size(); i++) {
            if (this.mUpdateListeners.get(i) != null) {
                this.mUpdateListeners.get(i).onAnimationUpdate(this, this.mValue, this.mVelocity);
            }
        }
        removeNullEntries(this.mUpdateListeners);
    }

    public float getValueThreshold() {
        return this.mMinVisibleChange * 0.75f;
    }

    private float getPropertyValue() {
        return this.mProperty.getValue(this.mTarget);
    }
}
