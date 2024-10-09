package org.telegram.ui.Components;

import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Property;
import android.view.animation.OvershootInterpolator;
import org.telegram.messenger.ImageReceiver;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.PhotoViewer;

/* loaded from: classes3.dex */
public abstract class AnimationProperties {
    public static final Property COLOR_DRAWABLE_ALPHA;
    public static final Property PAINT_ALPHA;
    public static final Property SHAPE_DRAWABLE_ALPHA;
    public static OvershootInterpolator overshootInterpolator = new OvershootInterpolator(1.9f);
    public static final Property PAINT_COLOR = new IntProperty("color") { // from class: org.telegram.ui.Components.AnimationProperties.2
        @Override // android.util.Property
        public Integer get(Paint paint) {
            return Integer.valueOf(paint.getColor());
        }

        @Override // org.telegram.ui.Components.AnimationProperties.IntProperty
        public void setValue(Paint paint, int i) {
            paint.setColor(i);
        }
    };
    public static final Property IMAGE_RECEIVER_ALPHA = new FloatProperty("currentAlpha") { // from class: org.telegram.ui.Components.AnimationProperties.3
        @Override // android.util.Property
        public Float get(ImageReceiver imageReceiver) {
            return Float.valueOf(imageReceiver.getCurrentAlpha());
        }

        @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
        public void setValue(ImageReceiver imageReceiver, float f) {
            imageReceiver.setCurrentAlpha(f);
        }
    };
    public static final Property CLIPPING_IMAGE_VIEW_PROGRESS = new FloatProperty("animationProgress") { // from class: org.telegram.ui.Components.AnimationProperties.6
        @Override // android.util.Property
        public Float get(ClippingImageView clippingImageView) {
            return Float.valueOf(clippingImageView.getAnimationProgress());
        }

        @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
        public void setValue(ClippingImageView clippingImageView, float f) {
            clippingImageView.setAnimationProgress(f);
        }
    };
    public static final Property PHOTO_VIEWER_ANIMATION_VALUE = new FloatProperty("animationValue") { // from class: org.telegram.ui.Components.AnimationProperties.7
        @Override // android.util.Property
        public Float get(PhotoViewer photoViewer) {
            return Float.valueOf(photoViewer.getAnimationValue());
        }

        @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
        public void setValue(PhotoViewer photoViewer, float f) {
            photoViewer.setAnimationValue(f);
        }
    };
    public static final Property CLIP_DIALOG_CELL_PROGRESS = new FloatProperty("clipProgress") { // from class: org.telegram.ui.Components.AnimationProperties.8
        @Override // android.util.Property
        public Float get(DialogCell dialogCell) {
            return Float.valueOf(dialogCell.getClipProgress());
        }

        @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
        public void setValue(DialogCell dialogCell, float f) {
            dialogCell.setClipProgress(f);
        }
    };

    /* loaded from: classes3.dex */
    public static abstract class FloatProperty extends Property {
        public FloatProperty(String str) {
            super(Float.class, str);
        }

        @Override // android.util.Property
        public final void set(Object obj, Float f) {
            setValue(obj, f.floatValue());
        }

        public abstract void setValue(Object obj, float f);
    }

    /* loaded from: classes3.dex */
    public static abstract class IntProperty extends Property {
        public IntProperty(String str) {
            super(Integer.class, str);
        }

        @Override // android.util.Property
        public final void set(Object obj, Integer num) {
            setValue(obj, num.intValue());
        }

        public abstract void setValue(Object obj, int i);
    }

    static {
        String str = "alpha";
        PAINT_ALPHA = new IntProperty(str) { // from class: org.telegram.ui.Components.AnimationProperties.1
            @Override // android.util.Property
            public Integer get(Paint paint) {
                return Integer.valueOf(paint.getAlpha());
            }

            @Override // org.telegram.ui.Components.AnimationProperties.IntProperty
            public void setValue(Paint paint, int i) {
                paint.setAlpha(i);
            }
        };
        COLOR_DRAWABLE_ALPHA = new IntProperty(str) { // from class: org.telegram.ui.Components.AnimationProperties.4
            @Override // android.util.Property
            public Integer get(ColorDrawable colorDrawable) {
                return Integer.valueOf(colorDrawable.getAlpha());
            }

            @Override // org.telegram.ui.Components.AnimationProperties.IntProperty
            public void setValue(ColorDrawable colorDrawable, int i) {
                colorDrawable.setAlpha(i);
            }
        };
        SHAPE_DRAWABLE_ALPHA = new IntProperty(str) { // from class: org.telegram.ui.Components.AnimationProperties.5
            @Override // android.util.Property
            public Integer get(ShapeDrawable shapeDrawable) {
                return Integer.valueOf(shapeDrawable.getPaint().getAlpha());
            }

            @Override // org.telegram.ui.Components.AnimationProperties.IntProperty
            public void setValue(ShapeDrawable shapeDrawable, int i) {
                shapeDrawable.getPaint().setAlpha(i);
            }
        };
    }
}
