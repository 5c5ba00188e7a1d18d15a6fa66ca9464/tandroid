package org.telegram.ui.Components;

import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Property;
import android.view.animation.OvershootInterpolator;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public class AnimationProperties {
    public static OvershootInterpolator overshootInterpolator = new OvershootInterpolator(1.9f);
    public static final Property<Paint, Integer> PAINT_ALPHA = new AnonymousClass1("alpha");
    public static final Property<ColorDrawable, Integer> COLOR_DRAWABLE_ALPHA = new AnonymousClass3("alpha");
    public static final Property<ShapeDrawable, Integer> SHAPE_DRAWABLE_ALPHA = new AnonymousClass4("alpha");
    public static final Property<ClippingImageView, Float> CLIPPING_IMAGE_VIEW_PROGRESS = new AnonymousClass5("animationProgress");
    public static final Property<PhotoViewer, Float> PHOTO_VIEWER_ANIMATION_VALUE = new AnonymousClass6("animationValue");
    public static final Property<DialogCell, Float> CLIP_DIALOG_CELL_PROGRESS = new AnonymousClass7("clipProgress");

    static {
        new AnonymousClass2("color");
    }

    /* loaded from: classes3.dex */
    public static abstract class FloatProperty<T> extends Property<T, Float> {
        public abstract void setValue(T t, float f);

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.util.Property
        public /* bridge */ /* synthetic */ void set(Object obj, Float f) {
            set2((FloatProperty<T>) obj, f);
        }

        public FloatProperty(String str) {
            super(Float.class, str);
        }

        /* renamed from: set */
        public final void set2(T t, Float f) {
            setValue(t, f.floatValue());
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class IntProperty<T> extends Property<T, Integer> {
        public abstract void setValue(T t, int i);

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.util.Property
        public /* bridge */ /* synthetic */ void set(Object obj, Integer num) {
            set2((IntProperty<T>) obj, num);
        }

        public IntProperty(String str) {
            super(Integer.class, str);
        }

        /* renamed from: set */
        public final void set2(T t, Integer num) {
            setValue(t, num.intValue());
        }
    }

    /* renamed from: org.telegram.ui.Components.AnimationProperties$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends IntProperty<Paint> {
        AnonymousClass1(String str) {
            super(str);
        }

        public void setValue(Paint paint, int i) {
            paint.setAlpha(i);
        }

        public Integer get(Paint paint) {
            return Integer.valueOf(paint.getAlpha());
        }
    }

    /* renamed from: org.telegram.ui.Components.AnimationProperties$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends IntProperty<Paint> {
        AnonymousClass2(String str) {
            super(str);
        }

        public void setValue(Paint paint, int i) {
            paint.setColor(i);
        }

        public Integer get(Paint paint) {
            return Integer.valueOf(paint.getColor());
        }
    }

    /* renamed from: org.telegram.ui.Components.AnimationProperties$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends IntProperty<ColorDrawable> {
        AnonymousClass3(String str) {
            super(str);
        }

        public void setValue(ColorDrawable colorDrawable, int i) {
            colorDrawable.setAlpha(i);
        }

        public Integer get(ColorDrawable colorDrawable) {
            return Integer.valueOf(colorDrawable.getAlpha());
        }
    }

    /* renamed from: org.telegram.ui.Components.AnimationProperties$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 extends IntProperty<ShapeDrawable> {
        AnonymousClass4(String str) {
            super(str);
        }

        public void setValue(ShapeDrawable shapeDrawable, int i) {
            shapeDrawable.getPaint().setAlpha(i);
        }

        public Integer get(ShapeDrawable shapeDrawable) {
            return Integer.valueOf(shapeDrawable.getPaint().getAlpha());
        }
    }

    /* renamed from: org.telegram.ui.Components.AnimationProperties$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 extends FloatProperty<ClippingImageView> {
        AnonymousClass5(String str) {
            super(str);
        }

        public void setValue(ClippingImageView clippingImageView, float f) {
            clippingImageView.setAnimationProgress(f);
        }

        public Float get(ClippingImageView clippingImageView) {
            return Float.valueOf(clippingImageView.getAnimationProgress());
        }
    }

    /* renamed from: org.telegram.ui.Components.AnimationProperties$6 */
    /* loaded from: classes3.dex */
    class AnonymousClass6 extends FloatProperty<PhotoViewer> {
        AnonymousClass6(String str) {
            super(str);
        }

        public void setValue(PhotoViewer photoViewer, float f) {
            photoViewer.setAnimationValue(f);
        }

        public Float get(PhotoViewer photoViewer) {
            return Float.valueOf(photoViewer.getAnimationValue());
        }
    }

    /* renamed from: org.telegram.ui.Components.AnimationProperties$7 */
    /* loaded from: classes3.dex */
    class AnonymousClass7 extends FloatProperty<DialogCell> {
        AnonymousClass7(String str) {
            super(str);
        }

        public void setValue(DialogCell dialogCell, float f) {
            dialogCell.setClipProgress(f);
        }

        public Float get(DialogCell dialogCell) {
            return Float.valueOf(dialogCell.getClipProgress());
        }
    }
}
