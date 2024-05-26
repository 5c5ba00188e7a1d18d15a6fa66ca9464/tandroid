package com.airbnb.lottie.animation.content;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.model.content.PolystarShape;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import java.util.List;
/* loaded from: classes.dex */
public class PolystarContent implements PathContent, BaseKeyframeAnimation.AnimationListener, Content {
    private final boolean hidden;
    private final BaseKeyframeAnimation<?, Float> innerRadiusAnimation;
    private final BaseKeyframeAnimation<?, Float> innerRoundednessAnimation;
    private boolean isPathValid;
    private final boolean isReversed;
    private final LottieDrawable lottieDrawable;
    private final BaseKeyframeAnimation<?, Float> outerRadiusAnimation;
    private final BaseKeyframeAnimation<?, Float> outerRoundednessAnimation;
    private final BaseKeyframeAnimation<?, Float> pointsAnimation;
    private final BaseKeyframeAnimation<?, PointF> positionAnimation;
    private final BaseKeyframeAnimation<?, Float> rotationAnimation;
    private final PolystarShape.Type type;
    private final Path path = new Path();
    private final Path lastSegmentPath = new Path();
    private final PathMeasure lastSegmentPathMeasure = new PathMeasure();
    private final float[] lastSegmentPosition = new float[2];
    private final CompoundTrimPathContent trimPaths = new CompoundTrimPathContent();

    public PolystarContent(LottieDrawable lottieDrawable, BaseLayer baseLayer, PolystarShape polystarShape) {
        this.lottieDrawable = lottieDrawable;
        polystarShape.getName();
        PolystarShape.Type type = polystarShape.getType();
        this.type = type;
        this.hidden = polystarShape.isHidden();
        this.isReversed = polystarShape.isReversed();
        BaseKeyframeAnimation<Float, Float> createAnimation = polystarShape.getPoints().createAnimation();
        this.pointsAnimation = createAnimation;
        BaseKeyframeAnimation<PointF, PointF> createAnimation2 = polystarShape.getPosition().createAnimation();
        this.positionAnimation = createAnimation2;
        BaseKeyframeAnimation<Float, Float> createAnimation3 = polystarShape.getRotation().createAnimation();
        this.rotationAnimation = createAnimation3;
        BaseKeyframeAnimation<Float, Float> createAnimation4 = polystarShape.getOuterRadius().createAnimation();
        this.outerRadiusAnimation = createAnimation4;
        BaseKeyframeAnimation<Float, Float> createAnimation5 = polystarShape.getOuterRoundedness().createAnimation();
        this.outerRoundednessAnimation = createAnimation5;
        PolystarShape.Type type2 = PolystarShape.Type.STAR;
        if (type == type2) {
            this.innerRadiusAnimation = polystarShape.getInnerRadius().createAnimation();
            this.innerRoundednessAnimation = polystarShape.getInnerRoundedness().createAnimation();
        } else {
            this.innerRadiusAnimation = null;
            this.innerRoundednessAnimation = null;
        }
        baseLayer.addAnimation(createAnimation);
        baseLayer.addAnimation(createAnimation2);
        baseLayer.addAnimation(createAnimation3);
        baseLayer.addAnimation(createAnimation4);
        baseLayer.addAnimation(createAnimation5);
        if (type == type2) {
            baseLayer.addAnimation(this.innerRadiusAnimation);
            baseLayer.addAnimation(this.innerRoundednessAnimation);
        }
        createAnimation.addUpdateListener(this);
        createAnimation2.addUpdateListener(this);
        createAnimation3.addUpdateListener(this);
        createAnimation4.addUpdateListener(this);
        createAnimation5.addUpdateListener(this);
        if (type == type2) {
            this.innerRadiusAnimation.addUpdateListener(this);
            this.innerRoundednessAnimation.addUpdateListener(this);
        }
    }

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener
    public void onValueChanged() {
        invalidate();
    }

    private void invalidate() {
        this.isPathValid = false;
        this.lottieDrawable.invalidateSelf();
    }

    @Override // com.airbnb.lottie.animation.content.Content
    public void setContents(List<Content> list, List<Content> list2) {
        for (int i = 0; i < list.size(); i++) {
            Content content = list.get(i);
            if (content instanceof TrimPathContent) {
                TrimPathContent trimPathContent = (TrimPathContent) content;
                if (trimPathContent.getType() == ShapeTrimPath.Type.SIMULTANEOUSLY) {
                    this.trimPaths.addTrimPath(trimPathContent);
                    trimPathContent.addListener(this);
                }
            }
        }
    }

    @Override // com.airbnb.lottie.animation.content.PathContent
    public Path getPath() {
        if (this.isPathValid) {
            return this.path;
        }
        this.path.reset();
        if (this.hidden) {
            this.isPathValid = true;
            return this.path;
        }
        int i = 1.$SwitchMap$com$airbnb$lottie$model$content$PolystarShape$Type[this.type.ordinal()];
        if (i == 1) {
            createStarPath();
        } else if (i == 2) {
            createPolygonPath();
        }
        this.path.close();
        this.trimPaths.apply(this.path);
        this.isPathValid = true;
        return this.path;
    }

    /* loaded from: classes.dex */
    static /* synthetic */ class 1 {
        static final /* synthetic */ int[] $SwitchMap$com$airbnb$lottie$model$content$PolystarShape$Type;

        static {
            int[] iArr = new int[PolystarShape.Type.values().length];
            $SwitchMap$com$airbnb$lottie$model$content$PolystarShape$Type = iArr;
            try {
                iArr[PolystarShape.Type.STAR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$airbnb$lottie$model$content$PolystarShape$Type[PolystarShape.Type.POLYGON.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    private void createStarPath() {
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation;
        double d;
        float f;
        float f2;
        float f3;
        float f4;
        double d2;
        float f5;
        float f6;
        float f7;
        float f8;
        float f9;
        float f10;
        double d3;
        float f11;
        float f12;
        float floatValue = this.pointsAnimation.getValue().floatValue();
        double radians = Math.toRadians((this.rotationAnimation == null ? 0.0d : baseKeyframeAnimation.getValue().floatValue()) - 90.0d);
        double d4 = floatValue;
        Double.isNaN(d4);
        float f13 = (float) (6.283185307179586d / d4);
        if (this.isReversed) {
            f13 *= -1.0f;
        }
        float f14 = f13 / 2.0f;
        float f15 = floatValue - ((int) floatValue);
        if (f15 != 0.0f) {
            double d5 = (1.0f - f15) * f14;
            Double.isNaN(d5);
            radians += d5;
        }
        float floatValue2 = this.outerRadiusAnimation.getValue().floatValue();
        float floatValue3 = this.innerRadiusAnimation.getValue().floatValue();
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation2 = this.innerRoundednessAnimation;
        float floatValue4 = baseKeyframeAnimation2 != null ? baseKeyframeAnimation2.getValue().floatValue() / 100.0f : 0.0f;
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation3 = this.outerRoundednessAnimation;
        float floatValue5 = baseKeyframeAnimation3 != null ? baseKeyframeAnimation3.getValue().floatValue() / 100.0f : 0.0f;
        if (f15 != 0.0f) {
            float f16 = ((floatValue2 - floatValue3) * f15) + floatValue3;
            double d6 = f16;
            double cos = Math.cos(radians);
            Double.isNaN(d6);
            d = d4;
            float f17 = (float) (d6 * cos);
            double sin = Math.sin(radians);
            Double.isNaN(d6);
            float f18 = (float) (d6 * sin);
            this.path.moveTo(f17, f18);
            double d7 = (f13 * f15) / 2.0f;
            Double.isNaN(d7);
            d2 = radians + d7;
            f3 = f17;
            f5 = f16;
            f = floatValue2;
            f4 = f18;
            f2 = f14;
        } else {
            d = d4;
            f = floatValue2;
            double d8 = f;
            double cos2 = Math.cos(radians);
            Double.isNaN(d8);
            f2 = f14;
            f3 = (float) (d8 * cos2);
            double sin2 = Math.sin(radians);
            Double.isNaN(d8);
            f4 = (float) (d8 * sin2);
            this.path.moveTo(f3, f4);
            double d9 = f2;
            Double.isNaN(d9);
            d2 = radians + d9;
            f5 = 0.0f;
        }
        double ceil = Math.ceil(d) * 2.0d;
        int i = 0;
        float f19 = floatValue5;
        boolean z = false;
        while (true) {
            double d10 = i;
            if (d10 < ceil) {
                float f20 = z ? f : floatValue3;
                float f21 = (f5 == 0.0f || d10 != ceil - 2.0d) ? f2 : (f13 * f15) / 2.0f;
                if (f5 == 0.0f || d10 != ceil - 1.0d) {
                    f6 = f13;
                    f7 = f20;
                    f8 = f;
                } else {
                    f6 = f13;
                    f8 = f;
                    f7 = f5;
                }
                double d11 = f7;
                double cos3 = Math.cos(d2);
                Double.isNaN(d11);
                float f22 = (float) (d11 * cos3);
                double sin3 = Math.sin(d2);
                Double.isNaN(d11);
                float f23 = (float) (d11 * sin3);
                if (floatValue4 == 0.0f && f19 == 0.0f) {
                    this.path.lineTo(f22, f23);
                    d3 = d2;
                    f9 = floatValue3;
                    f10 = floatValue4;
                    f11 = f2;
                    f12 = f21;
                } else {
                    f9 = floatValue3;
                    f10 = floatValue4;
                    double atan2 = (float) (Math.atan2(f4, f3) - 1.5707963267948966d);
                    float cos4 = (float) Math.cos(atan2);
                    float sin4 = (float) Math.sin(atan2);
                    d3 = d2;
                    f11 = f2;
                    f12 = f21;
                    double atan22 = (float) (Math.atan2(f23, f22) - 1.5707963267948966d);
                    float cos5 = (float) Math.cos(atan22);
                    float sin5 = (float) Math.sin(atan22);
                    float f24 = z ? f10 : f19;
                    float f25 = z ? f19 : f10;
                    float f26 = (z ? f9 : f8) * f24 * 0.47829f;
                    float f27 = cos4 * f26;
                    float f28 = f26 * sin4;
                    float f29 = (z ? f8 : f9) * f25 * 0.47829f;
                    float f30 = cos5 * f29;
                    float f31 = f29 * sin5;
                    if (f15 != 0.0f) {
                        if (i == 0) {
                            f27 *= f15;
                            f28 *= f15;
                        } else if (d10 == ceil - 1.0d) {
                            f30 *= f15;
                            f31 *= f15;
                        }
                    }
                    this.path.cubicTo(f3 - f27, f4 - f28, f22 + f30, f23 + f31, f22, f23);
                }
                double d12 = f12;
                Double.isNaN(d12);
                z = !z;
                i++;
                f4 = f23;
                d2 = d3 + d12;
                f2 = f11;
                f3 = f22;
                f = f8;
                f13 = f6;
                floatValue3 = f9;
                floatValue4 = f10;
            } else {
                PointF value = this.positionAnimation.getValue();
                this.path.offset(value.x, value.y);
                this.path.close();
                return;
            }
        }
    }

    private void createPolygonPath() {
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation;
        double d;
        float f;
        PolystarContent polystarContent;
        PolystarContent polystarContent2 = this;
        int floor = (int) Math.floor(polystarContent2.pointsAnimation.getValue().floatValue());
        double radians = Math.toRadians((polystarContent2.rotationAnimation == null ? 0.0d : baseKeyframeAnimation.getValue().floatValue()) - 90.0d);
        double d2 = floor;
        Double.isNaN(d2);
        float floatValue = polystarContent2.outerRoundednessAnimation.getValue().floatValue() / 100.0f;
        float floatValue2 = polystarContent2.outerRadiusAnimation.getValue().floatValue();
        double d3 = floatValue2;
        double cos = Math.cos(radians);
        Double.isNaN(d3);
        float f2 = (float) (cos * d3);
        double sin = Math.sin(radians);
        Double.isNaN(d3);
        float f3 = (float) (sin * d3);
        polystarContent2.path.moveTo(f2, f3);
        double d4 = (float) (6.283185307179586d / d2);
        Double.isNaN(d4);
        double ceil = Math.ceil(d2);
        double d5 = radians + d4;
        int i = 0;
        while (true) {
            double d6 = i;
            if (d6 < ceil) {
                double cos2 = Math.cos(d5);
                Double.isNaN(d3);
                int i2 = i;
                float f4 = (float) (d3 * cos2);
                double sin2 = Math.sin(d5);
                Double.isNaN(d3);
                double d7 = d4;
                float f5 = (float) (d3 * sin2);
                if (floatValue != 0.0f) {
                    d = d3;
                    double atan2 = (float) (Math.atan2(f3, f2) - 1.5707963267948966d);
                    float cos3 = (float) Math.cos(atan2);
                    f = f5;
                    double atan22 = (float) (Math.atan2(f5, f4) - 1.5707963267948966d);
                    float f6 = floatValue2 * floatValue * 0.25f;
                    float f7 = cos3 * f6;
                    float sin3 = ((float) Math.sin(atan2)) * f6;
                    float cos4 = ((float) Math.cos(atan22)) * f6;
                    float sin4 = f6 * ((float) Math.sin(atan22));
                    if (d6 == ceil - 1.0d) {
                        polystarContent = this;
                        polystarContent.lastSegmentPath.reset();
                        polystarContent.lastSegmentPath.moveTo(f2, f3);
                        float f8 = f2 - f7;
                        float f9 = f3 - sin3;
                        float f10 = f4 + cos4;
                        float f11 = sin4 + f;
                        polystarContent.lastSegmentPath.cubicTo(f8, f9, f10, f11, f4, f);
                        polystarContent.lastSegmentPathMeasure.setPath(polystarContent.lastSegmentPath, false);
                        PathMeasure pathMeasure = polystarContent.lastSegmentPathMeasure;
                        pathMeasure.getPosTan(pathMeasure.getLength() * 0.9999f, polystarContent.lastSegmentPosition, null);
                        Path path = polystarContent.path;
                        float[] fArr = polystarContent.lastSegmentPosition;
                        path.cubicTo(f8, f9, f10, f11, fArr[0], fArr[1]);
                    } else {
                        polystarContent = this;
                        polystarContent.path.cubicTo(f2 - f7, f3 - sin3, f4 + cos4, f + sin4, f4, f);
                    }
                } else {
                    d = d3;
                    f = f5;
                    polystarContent = polystarContent2;
                    if (d6 == ceil - 1.0d) {
                        f3 = f;
                        f2 = f4;
                        d4 = d7;
                        i = i2 + 1;
                        polystarContent2 = polystarContent;
                        d3 = d;
                    } else {
                        polystarContent.path.lineTo(f4, f);
                    }
                }
                Double.isNaN(d7);
                d5 += d7;
                f3 = f;
                f2 = f4;
                d4 = d7;
                i = i2 + 1;
                polystarContent2 = polystarContent;
                d3 = d;
            } else {
                PolystarContent polystarContent3 = polystarContent2;
                PointF value = polystarContent3.positionAnimation.getValue();
                polystarContent3.path.offset(value.x, value.y);
                polystarContent3.path.close();
                return;
            }
        }
    }
}
