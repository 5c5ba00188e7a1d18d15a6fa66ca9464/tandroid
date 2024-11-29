package org.telegram.ui.Components;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import com.google.zxing.common.detector.MathUtils;
import java.util.Arrays;
import java.util.Comparator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SvgHelper;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.CacheChart;
import org.telegram.ui.Components.Premium.StarParticlesView;

/* loaded from: classes3.dex */
public abstract class CacheChart extends View {
    private static final int[] DEFAULT_COLORS;
    private static final int[] DEFAULT_PARTICLES;
    private static Long loadedStart;
    private static long particlesStart;
    private static Long start;
    private AnimatedTextView.AnimatedTextDrawable bottomCompleteText;
    private AnimatedTextView.AnimatedTextDrawable bottomText;
    private RectF chartBounds;
    private RectF chartInnerBounds;
    private RectF chartMeasureBounds;
    private final int[] colorKeys;
    private boolean complete;
    private StarParticlesView.Drawable completeDrawable;
    private AnimatedFloat completeFloat;
    private LinearGradient completeGradient;
    private Matrix completeGradientMatrix;
    private Paint completePaint;
    private Paint completePaintStroke;
    private Path completePath;
    private RectF completePathBounds;
    private LinearGradient completeTextGradient;
    private Matrix completeTextGradientMatrix;
    private boolean interceptTouch;
    private boolean isAttached;
    private boolean loading;
    private Paint loadingBackgroundPaint;
    public AnimatedFloat loadingFloat;
    private final int[] particles;
    private RectF roundingRect;
    private final int sectionsCount;
    private Sector[] sectors;
    private float[] segmentsTmp;
    private int selectedIndex;
    private final boolean svgParticles;
    private float[] tempFloat;
    private int[] tempPercents;
    private AnimatedTextView.AnimatedTextDrawable topCompleteText;
    private AnimatedTextView.AnimatedTextDrawable topText;
    private final int type;

    class Sector {
        float angleCenter;
        AnimatedFloat angleCenterAnimated;
        float angleSize;
        AnimatedFloat angleSizeAnimated;
        Paint cut;
        RadialGradient gradient;
        Matrix gradientMatrix;
        int gradientWidth;
        private float lastAngleCenter;
        private float lastAngleSize;
        private float lastCx;
        private float lastCy;
        private float lastRounding;
        private float lastThickness;
        private float lastWidth;
        Paint paint;
        Bitmap particle;
        Paint particlePaint;
        float particlesAlpha;
        AnimatedFloat particlesAlphaAnimated;
        Path path;
        RectF pathBounds;
        RectF rectF;
        boolean selected;
        AnimatedFloat selectedAnimated;
        AnimatedTextView.AnimatedTextDrawable text;
        float textAlpha;
        AnimatedFloat textAlphaAnimated;
        float textScale;
        AnimatedFloat textScaleAnimated;
        Paint uncut;

        Sector() {
            Paint paint = new Paint(3);
            this.particlePaint = paint;
            paint.setColor(-1);
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            this.angleCenterAnimated = new AnimatedFloat(CacheChart.this, 650L, cubicBezierInterpolator);
            this.angleSizeAnimated = new AnimatedFloat(CacheChart.this, 650L, cubicBezierInterpolator);
            CubicBezierInterpolator cubicBezierInterpolator2 = CubicBezierInterpolator.EASE_OUT;
            this.textAlphaAnimated = new AnimatedFloat(CacheChart.this, 0L, 150L, cubicBezierInterpolator2);
            this.textScale = 1.0f;
            this.textScaleAnimated = new AnimatedFloat(CacheChart.this, 0L, 150L, cubicBezierInterpolator2);
            this.text = new AnimatedTextView.AnimatedTextDrawable(false, true, true);
            this.particlesAlphaAnimated = new AnimatedFloat(CacheChart.this, 0L, 150L, cubicBezierInterpolator2);
            this.selectedAnimated = new AnimatedFloat(CacheChart.this, 0L, 200L, cubicBezierInterpolator);
            this.text.setTextColor(-1);
            this.text.setAnimationProperties(0.35f, 0L, 200L, cubicBezierInterpolator);
            this.text.setTypeface(AndroidUtilities.bold());
            this.text.setTextSize(AndroidUtilities.dp(15.0f));
            this.text.setGravity(17);
            this.path = new Path();
            this.paint = new Paint(1);
            this.pathBounds = new RectF();
            this.uncut = new Paint(1);
            Paint paint2 = new Paint(1);
            this.cut = paint2;
            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            this.particlePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            this.rectF = new RectF();
        }

        private void drawParticles(Canvas canvas, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
            if (f10 <= 0.0f || !LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            float sqrt = (float) Math.sqrt(2.0d);
            if (CacheChart.particlesStart < 0) {
                long unused = CacheChart.particlesStart = currentTimeMillis;
            }
            float f11 = (currentTimeMillis - CacheChart.particlesStart) / 10000.0f;
            Bitmap bitmap = this.particle;
            if (bitmap != null) {
                int width = bitmap.getWidth();
                float f12 = width;
                float dpf2 = AndroidUtilities.dpf2(15.0f) / f12;
                float f13 = 7.0f;
                int floor = (int) Math.floor((f5 % 360.0f) / 7.0f);
                int ceil = (int) Math.ceil((f6 % 360.0f) / 7.0f);
                while (floor <= ceil) {
                    float f14 = floor * f13;
                    double d = 100.0f + f11;
                    double sin = ((Math.sin(2000.0f * f14) + 1.0d) * 0.25d) + 1.0d;
                    Double.isNaN(d);
                    float f15 = (float) ((d * sin) % 1.0d);
                    float f16 = f12 * sqrt;
                    double d2 = f;
                    float f17 = f12;
                    int i = ceil;
                    double lerp = AndroidUtilities.lerp(f7 - f16, f8 + f16, f15);
                    double cos = Math.cos(CacheChart.toRad(f14));
                    Double.isNaN(lerp);
                    Double.isNaN(d2);
                    float f18 = (float) (d2 + (cos * lerp));
                    double d3 = f2;
                    double sin2 = Math.sin(CacheChart.toRad(f14));
                    Double.isNaN(lerp);
                    Double.isNaN(d3);
                    float f19 = (float) (d3 + (lerp * sin2));
                    float abs = 0.65f * f10 * ((Math.abs(f15 - 0.5f) * (-1.75f)) + 1.0f);
                    double d4 = f15;
                    Double.isNaN(d4);
                    double d5 = d4 * 3.141592653589793d;
                    this.particlePaint.setAlpha((int) (Math.max(0.0f, Math.min(1.0f, abs * ((((float) (Math.sin(d5) - 1.0d)) * 0.25f) + 1.0f) * AndroidUtilities.lerp(1.0f, Math.min(MathUtils.distance(f18, f19, f3, f4) / AndroidUtilities.dpf2(64.0f), 1.0f), f9))) * 255.0f));
                    double sin3 = ((((float) (Math.sin(d5) - 1.0d)) * 0.25f) + 1.0f) * 0.75f;
                    double sin4 = ((Math.sin(f14) + 1.0d) * 0.25d) + 0.800000011920929d;
                    Double.isNaN(sin3);
                    float f20 = ((float) (sin3 * sin4)) * dpf2;
                    canvas.save();
                    canvas.translate(f18, f19);
                    canvas.scale(f20, f20);
                    float f21 = -(width >> 1);
                    canvas.drawBitmap(this.particle, f21, f21, this.particlePaint);
                    canvas.restore();
                    floor++;
                    ceil = i;
                    f11 = f11;
                    f12 = f17;
                    f13 = 7.0f;
                }
            }
        }

        private void setGradientBounds(float f, float f2, float f3, float f4) {
            this.gradientMatrix.reset();
            this.gradientMatrix.setTranslate(f, f2);
            this.gradient.setLocalMatrix(this.gradientMatrix);
        }

        private void setupPath(RectF rectF, RectF rectF2, float f, float f2, float f3) {
            float f4;
            float f5;
            float min = Math.min(f3, (rectF.width() - rectF2.width()) / 4.0f);
            double d = f2 / 180.0f;
            Double.isNaN(d);
            double width = rectF2.width() / 2.0f;
            Double.isNaN(width);
            float min2 = Math.min(min, (float) (d * 3.141592653589793d * width));
            float width2 = (rectF.width() - rectF2.width()) / 2.0f;
            if (this.lastAngleCenter == f && this.lastAngleSize == f2 && this.lastRounding == min2 && this.lastThickness == width2 && this.lastWidth == rectF.width() && this.lastCx == rectF.centerX() && this.lastCy == rectF.centerY()) {
                return;
            }
            this.lastAngleCenter = f;
            this.lastAngleSize = f2;
            this.lastRounding = min2;
            this.lastThickness = width2;
            this.lastWidth = rectF.width();
            this.lastCx = rectF.centerX();
            this.lastCy = rectF.centerY();
            float f6 = f - f2;
            float f7 = f + f2;
            boolean z = min2 > 0.0f;
            float f8 = min2 * 2.0f;
            double width3 = rectF.width() - f8;
            Double.isNaN(width3);
            float f9 = (min2 / ((float) (width3 * 3.141592653589793d))) * 360.0f;
            double width4 = rectF2.width() + f8;
            Double.isNaN(width4);
            float f10 = ((min2 / ((float) (width4 * 3.141592653589793d))) * 360.0f) + ((f2 > 175.0f ? 0 : 1) * 0.5f);
            float width5 = (rectF.width() / 2.0f) - min2;
            float width6 = (rectF2.width() / 2.0f) + min2;
            this.path.rewind();
            float f11 = f7 - f6;
            if (f11 < 0.5f) {
                return;
            }
            if (z) {
                RectF rectF3 = CacheChart.this.roundingRect;
                double centerX = rectF.centerX();
                double d2 = width5;
                f4 = width6;
                double cos = Math.cos(CacheChart.toRad(r19));
                Double.isNaN(d2);
                Double.isNaN(centerX);
                double d3 = centerX + (cos * d2);
                double centerY = rectF.centerY();
                double sin = Math.sin(CacheChart.toRad(r19));
                Double.isNaN(d2);
                Double.isNaN(centerY);
                CacheChart.setCircleBounds(rectF3, d3, (d2 * sin) + centerY, min2);
                this.path.arcTo(CacheChart.this.roundingRect, (f6 + f9) - 90.0f, 90.0f);
            } else {
                f4 = width6;
            }
            this.path.arcTo(rectF, f6 + f9, f11 - (f9 * 2.0f));
            if (z) {
                RectF rectF4 = CacheChart.this.roundingRect;
                double centerX2 = rectF.centerX();
                double d4 = width5;
                float f12 = f7 - f9;
                double cos2 = Math.cos(CacheChart.toRad(f12));
                Double.isNaN(d4);
                Double.isNaN(centerX2);
                double d5 = centerX2 + (cos2 * d4);
                double centerY2 = rectF.centerY();
                f5 = f6;
                double sin2 = Math.sin(CacheChart.toRad(f12));
                Double.isNaN(d4);
                Double.isNaN(centerY2);
                CacheChart.setCircleBounds(rectF4, d5, centerY2 + (d4 * sin2), min2);
                this.path.arcTo(CacheChart.this.roundingRect, f12, 90.0f);
                RectF rectF5 = CacheChart.this.roundingRect;
                double centerX3 = rectF2.centerX();
                double d6 = f4;
                double cos3 = Math.cos(CacheChart.toRad(r2));
                Double.isNaN(d6);
                Double.isNaN(centerX3);
                double d7 = centerX3 + (cos3 * d6);
                double centerY3 = rectF2.centerY();
                double sin3 = Math.sin(CacheChart.toRad(r2));
                Double.isNaN(d6);
                Double.isNaN(centerY3);
                CacheChart.setCircleBounds(rectF5, d7, centerY3 + (d6 * sin3), min2);
                this.path.arcTo(CacheChart.this.roundingRect, (f7 - f10) + 90.0f, 90.0f);
            } else {
                f5 = f6;
            }
            this.path.arcTo(rectF2, f7 - f10, -(f11 - (f10 * 2.0f)));
            if (z) {
                RectF rectF6 = CacheChart.this.roundingRect;
                double centerX4 = rectF2.centerX();
                double d8 = f4;
                double cos4 = Math.cos(CacheChart.toRad(r4));
                Double.isNaN(d8);
                Double.isNaN(centerX4);
                double d9 = centerX4 + (cos4 * d8);
                double centerY4 = rectF2.centerY();
                double sin4 = Math.sin(CacheChart.toRad(r4));
                Double.isNaN(d8);
                Double.isNaN(centerY4);
                CacheChart.setCircleBounds(rectF6, d9, centerY4 + (d8 * sin4), min2);
                this.path.arcTo(CacheChart.this.roundingRect, f5 + f10 + 180.0f, 90.0f);
            }
            this.path.close();
            this.path.computeBounds(this.pathBounds, false);
        }

        void draw(Canvas canvas, RectF rectF, RectF rectF2, float f, float f2, float f3, float f4, float f5) {
            float f6;
            float f7;
            float f8 = this.selectedAnimated.set(this.selected ? 1.0f : 0.0f);
            this.rectF.set(rectF);
            this.rectF.inset((-AndroidUtilities.dp(9.0f)) * f8, f8 * (-AndroidUtilities.dp(9.0f)));
            double centerX = this.rectF.centerX();
            double cos = Math.cos(CacheChart.toRad(f));
            double width = this.rectF.width() + rectF2.width();
            Double.isNaN(width);
            Double.isNaN(centerX);
            float f9 = (float) (centerX + ((cos * width) / 4.0d));
            double centerY = this.rectF.centerY();
            double sin = Math.sin(CacheChart.toRad(f));
            double width2 = this.rectF.width() + rectF2.width();
            Double.isNaN(width2);
            Double.isNaN(centerY);
            float f10 = (float) (centerY + ((sin * width2) / 4.0d));
            float f11 = f5 * this.textAlphaAnimated.set(this.textAlpha) * f4;
            float f12 = this.particlesAlphaAnimated.set(this.particlesAlpha);
            this.paint.setAlpha((int) (f4 * 255.0f));
            if (f2 * 2.0f >= 359.0f) {
                canvas.saveLayerAlpha(this.rectF, NotificationCenter.newLocationAvailable, 31);
                canvas.drawCircle(this.rectF.centerX(), this.rectF.centerY(), this.rectF.width() / 2.0f, this.uncut);
                canvas.drawRect(this.rectF, this.paint);
                f6 = f10;
                f7 = f9;
                drawParticles(canvas, this.rectF.centerX(), this.rectF.centerY(), f9, f10, 0.0f, 359.0f, rectF2.width() / 2.0f, this.rectF.width() / 2.0f, f11, Math.max(0.0f, (f5 / 0.75f) - 0.75f) * f12);
                canvas.drawCircle(rectF2.centerX(), rectF2.centerY(), rectF2.width() / 2.0f, this.cut);
            } else {
                f6 = f10;
                f7 = f9;
                setupPath(this.rectF, rectF2, f, f2, f3);
                setGradientBounds(this.rectF.centerX(), rectF.centerY(), this.rectF.width() / 2.0f, f);
                canvas.saveLayerAlpha(this.rectF, NotificationCenter.newLocationAvailable, 31);
                canvas.drawPath(this.path, this.uncut);
                canvas.drawRect(this.rectF, this.paint);
                drawParticles(canvas, this.rectF.centerX(), this.rectF.centerY(), f7, f6, f - f2, f + f2, rectF2.width() / 2.0f, this.rectF.width() / 2.0f, f11, Math.max(0.0f, (f5 / 0.75f) - 0.75f) * f12);
            }
            canvas.restore();
            float f13 = this.textScaleAnimated.set(this.textScale);
            CacheChart.setCircleBounds(CacheChart.this.roundingRect, f7, f6, 0.0f);
            if (f13 != 1.0f) {
                canvas.save();
                canvas.scale(f13, f13, CacheChart.this.roundingRect.centerX(), CacheChart.this.roundingRect.centerY());
            }
            this.text.setAlpha((int) (f11 * 255.0f));
            this.text.setBounds((int) CacheChart.this.roundingRect.left, (int) CacheChart.this.roundingRect.top, (int) CacheChart.this.roundingRect.right, (int) CacheChart.this.roundingRect.bottom);
            this.text.draw(canvas);
            if (f13 != 1.0f) {
                canvas.restore();
            }
        }
    }

    public static class SegmentSize {
        int index;
        public boolean selected;
        public long size;

        public static SegmentSize of(long j, boolean z) {
            SegmentSize segmentSize = new SegmentSize();
            segmentSize.size = j;
            segmentSize.selected = z;
            return segmentSize;
        }
    }

    static {
        int i = Theme.key_statisticChartLine_lightblue;
        int i2 = Theme.key_statisticChartLine_blue;
        int i3 = Theme.key_statisticChartLine_green;
        int i4 = Theme.key_statisticChartLine_purple;
        int i5 = Theme.key_statisticChartLine_lightgreen;
        int i6 = Theme.key_statisticChartLine_red;
        int i7 = Theme.key_statisticChartLine_orange;
        int i8 = Theme.key_statisticChartLine_cyan;
        int i9 = Theme.key_statisticChartLine_golden;
        DEFAULT_COLORS = new int[]{i, i2, i3, i4, i5, i6, i7, i8, i4, i9, i9};
        int i10 = R.raw.cache_photos;
        int i11 = R.raw.cache_videos;
        int i12 = R.raw.cache_documents;
        int i13 = R.raw.cache_music;
        int i14 = R.raw.cache_stickers;
        int i15 = R.raw.cache_profile_photos;
        int i16 = R.raw.cache_other;
        DEFAULT_PARTICLES = new int[]{i10, i11, i12, i13, i11, i13, i14, i15, i16, i16, i12};
        particlesStart = -1L;
    }

    public CacheChart(Context context) {
        this(context, 11, DEFAULT_COLORS, 0, DEFAULT_PARTICLES);
    }

    public CacheChart(Context context, int i, int[] iArr, int i2, int[] iArr2) {
        super(context);
        this.chartMeasureBounds = new RectF();
        this.chartBounds = new RectF();
        this.chartInnerBounds = new RectF();
        this.loading = true;
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.loadingFloat = new AnimatedFloat(this, 750L, cubicBezierInterpolator);
        int i3 = 0;
        this.complete = false;
        this.completeFloat = new AnimatedFloat(this, 650L, cubicBezierInterpolator);
        this.segmentsTmp = new float[2];
        this.roundingRect = new RectF();
        this.loadingBackgroundPaint = new Paint(1);
        this.completePath = new Path();
        this.completePaintStroke = new Paint(1);
        this.completePaint = new Paint(1);
        this.topText = new AnimatedTextView.AnimatedTextDrawable(false, true, true);
        this.bottomText = new AnimatedTextView.AnimatedTextDrawable(false, true, true);
        this.topCompleteText = new AnimatedTextView.AnimatedTextDrawable(false, true, true);
        this.bottomCompleteText = new AnimatedTextView.AnimatedTextDrawable(false, true, true);
        this.interceptTouch = true;
        this.selectedIndex = -1;
        setLayerType(2, null);
        this.sectionsCount = i;
        this.colorKeys = iArr;
        this.particles = iArr2;
        this.type = i2;
        this.svgParticles = i2 == 0;
        this.sectors = new Sector[i];
        Paint paint = this.loadingBackgroundPaint;
        Paint.Style style = Paint.Style.STROKE;
        paint.setStyle(style);
        this.loadingBackgroundPaint.setColor(Theme.getColor(Theme.key_listSelector));
        this.completePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        this.completeGradient = new LinearGradient(0.0f, 0.0f, 0.0f, AndroidUtilities.dp(200.0f), new int[]{7263574, -9513642, -12469647, 4307569}, new float[]{0.0f, 0.07f, 0.93f, 1.0f}, tileMode);
        this.completeTextGradient = new LinearGradient(0.0f, 0.0f, 0.0f, AndroidUtilities.dp(200.0f), new int[]{7263574, -9513642, -12469647, 4307569}, new float[]{0.0f, 0.07f, 0.93f, 1.0f}, tileMode);
        this.completeGradientMatrix = new Matrix();
        this.completeTextGradientMatrix = new Matrix();
        this.completePaintStroke.setShader(this.completeGradient);
        this.completePaint.setShader(this.completeGradient);
        this.completePaintStroke.setStyle(style);
        this.completePaintStroke.setStrokeCap(Paint.Cap.ROUND);
        this.completePaintStroke.setStrokeJoin(Paint.Join.ROUND);
        this.topText.setAnimationProperties(0.2f, 0L, 450L, cubicBezierInterpolator);
        this.topText.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.topText.setTypeface(AndroidUtilities.bold());
        this.topText.setTextSize(AndroidUtilities.dp(32.0f));
        this.topText.setGravity(17);
        this.bottomText.setAnimationProperties(0.6f, 0L, 450L, cubicBezierInterpolator);
        this.bottomText.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        this.bottomText.setTextSize(AndroidUtilities.dp(12.0f));
        this.bottomText.setGravity(17);
        this.topCompleteText.setAnimationProperties(0.2f, 0L, 450L, cubicBezierInterpolator);
        this.topCompleteText.getPaint().setShader(this.completeTextGradient);
        this.topCompleteText.setTypeface(AndroidUtilities.bold());
        this.topCompleteText.setTextSize(AndroidUtilities.dp(32.0f));
        this.topCompleteText.setGravity(17);
        this.bottomCompleteText.setAnimationProperties(0.6f, 0L, 450L, cubicBezierInterpolator);
        this.bottomCompleteText.getPaint().setShader(this.completeTextGradient);
        this.bottomCompleteText.setTypeface(AndroidUtilities.bold());
        this.bottomCompleteText.setTextSize(AndroidUtilities.dp(12.0f));
        this.bottomCompleteText.setGravity(17);
        while (true) {
            Sector[] sectorArr = this.sectors;
            if (i3 >= sectorArr.length) {
                return;
            }
            Sector sector = new Sector();
            sectorArr[i3] = sector;
            int blendOver = Theme.blendOver(Theme.getColor(iArr[i3]), ConnectionsManager.FileTypeAudio);
            int blendOver2 = Theme.blendOver(Theme.getColor(iArr[i3]), 822083583);
            sector.gradientWidth = AndroidUtilities.dp(50.0f);
            RadialGradient radialGradient = new RadialGradient(0.0f, 0.0f, AndroidUtilities.dp(86.0f), new int[]{blendOver2, blendOver}, new float[]{0.3f, 1.0f}, Shader.TileMode.CLAMP);
            sector.gradient = radialGradient;
            Matrix matrix = new Matrix();
            sector.gradientMatrix = matrix;
            radialGradient.setLocalMatrix(matrix);
            sector.paint.setShader(sector.gradient);
            i3++;
        }
    }

    private boolean drawAnimatedText(Canvas canvas, AnimatedTextView.AnimatedTextDrawable animatedTextDrawable, float f, float f2, float f3, float f4) {
        if (f4 <= 0.0f) {
            return false;
        }
        animatedTextDrawable.setAlpha((int) (f4 * 255.0f));
        animatedTextDrawable.setBounds(0, 0, 0, 0);
        canvas.save();
        canvas.translate(f, f2);
        canvas.scale(f3, f3);
        animatedTextDrawable.draw(canvas);
        canvas.restore();
        return animatedTextDrawable.isAnimating();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$setSegments$0(SegmentSize segmentSize, SegmentSize segmentSize2) {
        return Long.compare(segmentSize.size, segmentSize2.size);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setCircleBounds(RectF rectF, double d, double d2, float f) {
        setCircleBounds(rectF, (float) d, (float) d2, f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setCircleBounds(RectF rectF, float f, float f2, float f3) {
        rectF.set(f - f3, f2 - f3, f + f3, f2 + f3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static float toRad(float f) {
        double d = f / 180.0f;
        Double.isNaN(d);
        return (float) (d * 3.141592653589793d);
    }

    /* JADX WARN: Code restructure failed: missing block: B:86:0x0328, code lost:
    
        if (r0.equals(r25.chartMeasureBounds) != false) goto L92;
     */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0098  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0093  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x01cd A[EDGE_INSN: B:54:0x01cd->B:55:0x01cd BREAK  A[LOOP:0: B:28:0x0101->B:36:0x01c2], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x01d5  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x02e5  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0575  */
    /* JADX WARN: Removed duplicated region for block: B:92:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0222  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void dispatchDraw(Canvas canvas) {
        Long l;
        float f;
        char c;
        float f2;
        int i;
        Sector[] sectorArr;
        int i2;
        float f3;
        float f4;
        Canvas canvas2;
        float f5;
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable;
        CacheChart cacheChart;
        Canvas canvas3;
        float f6;
        float f7;
        float f8;
        float f9;
        Path path;
        float width;
        float height;
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15 = 0.0f;
        float f16 = this.loadingFloat.set(this.loading ? 1.0f : 0.0f);
        float f17 = this.completeFloat.set(this.complete ? 1.0f : 0.0f);
        this.chartBounds.set(this.chartMeasureBounds);
        float lerp = AndroidUtilities.lerp(0.0f, AndroidUtilities.dpf2(padInsideDp()), f17);
        this.chartBounds.inset(lerp, lerp);
        this.chartInnerBounds.set(this.chartBounds);
        float lerp2 = AndroidUtilities.lerp(AndroidUtilities.dpf2(38.0f), AndroidUtilities.dpf2(10.0f), Math.max(f16, f17));
        this.chartInnerBounds.inset(lerp2, lerp2);
        char c2 = 0;
        float lerp3 = AndroidUtilities.lerp(0, AndroidUtilities.dp(60.0f), f16);
        if (start == null) {
            start = Long.valueOf(System.currentTimeMillis());
        }
        boolean z = this.loading;
        if (z || loadedStart != null) {
            if (z && loadedStart != null) {
                l = null;
            }
            float currentTimeMillis = ((loadedStart != null ? System.currentTimeMillis() : r8.longValue()) - start.longValue()) * 0.6f;
            CircularProgressDrawable.getSegments(currentTimeMillis % 5400.0f, this.segmentsTmp);
            float[] fArr = this.segmentsTmp;
            f = fArr[0];
            c = 1;
            f2 = fArr[1];
            if (f16 > 0.0f) {
                this.loadingBackgroundPaint.setStrokeWidth(lerp2);
                int alpha = this.loadingBackgroundPaint.getAlpha();
                this.loadingBackgroundPaint.setAlpha((int) (alpha * f16));
                canvas.drawCircle(this.chartBounds.centerX(), this.chartBounds.centerY(), (this.chartBounds.width() - lerp2) / 2.0f, this.loadingBackgroundPaint);
                this.loadingBackgroundPaint.setAlpha(alpha);
            }
            boolean z2 = f16 <= 0.0f || f17 > 0.0f;
            i = 0;
            while (true) {
                sectorArr = this.sectors;
                if (i < sectorArr.length) {
                    break;
                }
                Sector sector = sectorArr[i];
                CircularProgressDrawable.getSegments((currentTimeMillis + (i * 80)) % 5400.0f, this.segmentsTmp);
                float min = Math.min(Math.max(this.segmentsTmp[c2], f), f2);
                float min2 = Math.min(Math.max(this.segmentsTmp[c], f), f2);
                if (f16 < 1.0f || min < min2) {
                    float f18 = (min + min2) / 2.0f;
                    float abs = Math.abs(min2 - min) / 2.0f;
                    if (f16 <= f15) {
                        float f19 = sector.angleCenterAnimated.set(sector.angleCenter);
                        f13 = sector.angleSizeAnimated.set(sector.angleSize);
                        f11 = f2;
                        f12 = f19;
                    } else {
                        if (f16 < 1.0f) {
                            f11 = f2;
                            float lerp4 = AndroidUtilities.lerp(sector.angleCenterAnimated.set(sector.angleCenter) + (((float) Math.floor(f2 / 360.0f)) * 360.0f), f18, f16);
                            abs = AndroidUtilities.lerp(sector.angleSizeAnimated.set(sector.angleSize), abs, f16);
                            f12 = lerp4;
                        } else {
                            f11 = f2;
                            f12 = f18;
                        }
                        f13 = abs;
                    }
                    boolean z3 = sector.angleCenterAnimated.isInProgress() || sector.angleSizeAnimated.isInProgress() || z2;
                    f14 = f;
                    sector.draw(canvas, this.chartBounds, this.chartInnerBounds, f12, f13, lerp3, 1.0f - f17, 1.0f - f16);
                    z2 = z3;
                } else {
                    f11 = f2;
                    f14 = f;
                }
                i++;
                f = f14;
                f2 = f11;
                f15 = 0.0f;
                c = 1;
                c2 = 0;
            }
            i2 = this.type;
            if (i2 == 0) {
                f3 = lerp2;
                f4 = f17;
                canvas2 = canvas;
                f5 = 0.0f;
                if (i2 == 1) {
                    float f20 = 1.0f - f16;
                    float centerX = this.chartBounds.centerX() - AndroidUtilities.lerp(0.0f, AndroidUtilities.dpf2(4.0f), f4);
                    float centerY = this.chartBounds.centerY() - AndroidUtilities.lerp(AndroidUtilities.dpf2(5.0f), 0.0f, f4);
                    float lerp5 = AndroidUtilities.lerp(1.0f, 2.25f, f4);
                    float f21 = f20 * f4;
                    boolean z4 = drawAnimatedText(canvas, this.topCompleteText, centerX, centerY, lerp5, f21) || z2;
                    float f22 = f20 * (1.0f - f4);
                    if (drawAnimatedText(canvas, this.topText, centerX, centerY, lerp5, f22) || z4) {
                    }
                    float centerX2 = this.chartBounds.centerX() + AndroidUtilities.lerp(0.0f, AndroidUtilities.dpf2(26.0f), f4);
                    float centerY2 = this.chartBounds.centerY() + AndroidUtilities.lerp(AndroidUtilities.dpf2(22.0f), -AndroidUtilities.dpf2(18.0f), f4);
                    float lerp6 = AndroidUtilities.lerp(1.0f, 1.4f, f4);
                    drawAnimatedText(canvas, this.bottomCompleteText, centerX2, centerY2, lerp6, f21);
                    animatedTextDrawable = this.bottomText;
                    cacheChart = this;
                    canvas3 = canvas;
                    f6 = centerX2;
                    f7 = centerY2;
                    f8 = lerp6;
                    f9 = f22;
                }
                if (f4 > f5) {
                    if (this.completeDrawable == null) {
                        StarParticlesView.Drawable drawable = new StarParticlesView.Drawable(25);
                        this.completeDrawable = drawable;
                        drawable.type = 100;
                        drawable.roundEffect = true;
                        drawable.useRotate = true;
                        drawable.useBlur = false;
                        drawable.checkBounds = true;
                        drawable.size1 = 18;
                        drawable.distributionAlgorithm = false;
                        drawable.excludeRadius = AndroidUtilities.dp(80.0f);
                        StarParticlesView.Drawable drawable2 = this.completeDrawable;
                        drawable2.k3 = 0.85f;
                        drawable2.k2 = 0.85f;
                        drawable2.k1 = 0.85f;
                        drawable2.init();
                    } else {
                        RectF rectF = this.completePathBounds;
                        if (rectF != null) {
                        }
                    }
                    float min3 = Math.min(getMeasuredHeight(), Math.min(getMeasuredWidth(), AndroidUtilities.dp(150.0f)));
                    this.completeDrawable.rect.set(f5, f5, min3, min3);
                    this.completeDrawable.rect.offset((getMeasuredWidth() - this.completeDrawable.rect.width()) / 2.0f, (getMeasuredHeight() - this.completeDrawable.rect.height()) / 2.0f);
                    this.completeDrawable.rect2.set(f5, f5, getMeasuredWidth(), getMeasuredHeight());
                    this.completeDrawable.resetPositions();
                    canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), NotificationCenter.newLocationAvailable, 31);
                    this.completeDrawable.onDraw(canvas2, f4);
                    int i3 = (int) (f4 * 255.0f);
                    this.completePaint.setAlpha(i3);
                    canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight(), this.completePaint);
                    canvas.restore();
                    this.completePaintStroke.setStrokeWidth(f3);
                    this.completePaintStroke.setAlpha(i3);
                    canvas2.drawCircle(this.chartBounds.centerX(), this.chartBounds.centerY(), (this.chartBounds.width() - f3) / 2.0f, this.completePaintStroke);
                    RectF rectF2 = this.completePathBounds;
                    if (rectF2 == null || !rectF2.equals(this.chartMeasureBounds)) {
                        if (this.completePathBounds == null) {
                            this.completePathBounds = new RectF();
                        }
                        this.completePathBounds.set(this.chartMeasureBounds);
                        this.completePath.rewind();
                        int i4 = this.type;
                        if (i4 == 0) {
                            this.completePath.moveTo(this.chartBounds.width() * 0.348f, this.chartBounds.height() * 0.538f);
                            this.completePath.lineTo(this.chartBounds.width() * 0.447f, this.chartBounds.height() * 0.636f);
                            path = this.completePath;
                            width = this.chartBounds.width() * 0.678f;
                            height = this.chartBounds.height();
                            f10 = 0.402f;
                        } else {
                            if (i4 == 1) {
                                this.completePath.moveTo(this.chartBounds.width() * 0.2929f, this.chartBounds.height() * 0.4369f);
                                this.completePath.lineTo(this.chartBounds.width() * 0.381f, this.chartBounds.height() * 0.35f);
                                this.completePath.lineTo(this.chartBounds.width() * 0.4691f, this.chartBounds.height() * 0.4369f);
                                this.completePath.moveTo(this.chartBounds.width() * 0.381f, this.chartBounds.height() * 0.35f);
                                this.completePath.lineTo(this.chartBounds.width() * 0.381f, this.chartBounds.height() * 0.6548f);
                                this.completePath.moveTo(this.chartBounds.width() * 0.5214f, this.chartBounds.height() * 0.5821f);
                                this.completePath.lineTo(this.chartBounds.width() * 0.6095f, this.chartBounds.height() * 0.669f);
                                this.completePath.lineTo(this.chartBounds.width() * 0.6976f, this.chartBounds.height() * 0.5821f);
                                this.completePath.moveTo(this.chartBounds.width() * 0.6095f, this.chartBounds.height() * 0.669f);
                                path = this.completePath;
                                width = this.chartBounds.width() * 0.6095f;
                                height = this.chartBounds.height();
                                f10 = 0.3643f;
                            }
                            Path path2 = this.completePath;
                            RectF rectF3 = this.chartBounds;
                            path2.offset(rectF3.left, rectF3.top);
                        }
                        path.lineTo(width, height * f10);
                        Path path22 = this.completePath;
                        RectF rectF32 = this.chartBounds;
                        path22.offset(rectF32.left, rectF32.top);
                    }
                    if (this.type == 0) {
                        this.completePaintStroke.setStrokeWidth(AndroidUtilities.dpf2(10.0f));
                        canvas2.drawPath(this.completePath, this.completePaintStroke);
                    }
                }
                if (this.isAttached) {
                    invalidate();
                    return;
                }
                return;
            }
            float f23 = (1.0f - f16) * (1.0f - f17);
            f3 = lerp2;
            f4 = f17;
            f5 = 0.0f;
            canvas2 = canvas;
            drawAnimatedText(canvas, this.topText, this.chartBounds.centerX(), this.chartBounds.centerY() - AndroidUtilities.dpf2(5.0f), 1.0f, f23);
            f6 = this.chartBounds.centerX();
            f7 = this.chartBounds.centerY() + AndroidUtilities.dpf2(22.0f);
            animatedTextDrawable = this.bottomText;
            f8 = 1.0f;
            cacheChart = this;
            canvas3 = canvas;
            f9 = f23;
            cacheChart.drawAnimatedText(canvas3, animatedTextDrawable, f6, f7, f8, f9);
            if (f4 > f5) {
            }
            if (this.isAttached) {
            }
        } else {
            l = Long.valueOf(System.currentTimeMillis());
        }
        loadedStart = l;
        float currentTimeMillis2 = ((loadedStart != null ? System.currentTimeMillis() : r8.longValue()) - start.longValue()) * 0.6f;
        CircularProgressDrawable.getSegments(currentTimeMillis2 % 5400.0f, this.segmentsTmp);
        float[] fArr2 = this.segmentsTmp;
        f = fArr2[0];
        c = 1;
        f2 = fArr2[1];
        if (f16 > 0.0f) {
        }
        boolean z22 = f16 <= 0.0f || f17 > 0.0f;
        i = 0;
        while (true) {
            sectorArr = this.sectors;
            if (i < sectorArr.length) {
            }
            i++;
            f = f14;
            f2 = f11;
            f15 = 0.0f;
            c = 1;
            c2 = 0;
        }
        i2 = this.type;
        if (i2 == 0) {
        }
        cacheChart.drawAnimatedText(canvas3, animatedTextDrawable, f6, f7, f8, f9);
        if (f4 > f5) {
        }
        if (this.isAttached) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00a4  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int i;
        boolean z;
        float distance = MathUtils.distance(this.chartBounds.centerX(), this.chartBounds.centerY(), motionEvent.getX(), motionEvent.getY());
        float atan2 = (float) ((Math.atan2(r1 - this.chartBounds.centerY(), r0 - this.chartBounds.centerX()) / 3.141592653589793d) * 180.0d);
        if (atan2 < 0.0f) {
            atan2 += 360.0f;
        }
        if (distance > this.chartInnerBounds.width() / 2.0f && distance < (this.chartBounds.width() / 2.0f) + AndroidUtilities.dp(14.0f)) {
            i = 0;
            while (true) {
                Sector[] sectorArr = this.sectors;
                if (i >= sectorArr.length) {
                    break;
                }
                Sector sector = sectorArr[i];
                float f = sector.angleCenter;
                float f2 = sector.angleSize;
                if (atan2 >= f - f2 && atan2 <= f + f2) {
                    break;
                }
                i++;
            }
            if (motionEvent.getAction() != 0) {
                setSelected(i);
                if (i >= 0) {
                    onSectionDown(i, i != -1);
                    if (getParent() != null && this.interceptTouch) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                return true;
            }
            if (motionEvent.getAction() == 2) {
                onSectionDown(i, i != -1);
                setSelected(i);
                if (i != -1) {
                    return true;
                }
            } else if (motionEvent.getAction() == 1) {
                if (i != -1) {
                    onSectionClick(i);
                    z = true;
                } else {
                    z = false;
                }
                setSelected(-1);
                onSectionDown(i, false);
                if (z) {
                    return true;
                }
            } else if (motionEvent.getAction() == 3) {
                setSelected(-1);
                onSectionDown(i, false);
            }
            return super.dispatchTouchEvent(motionEvent);
        }
        i = -1;
        if (motionEvent.getAction() != 0) {
        }
    }

    protected int heightDp() {
        return NotificationCenter.storyQualityUpdate;
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.isAttached = true;
        int i = 0;
        while (true) {
            Sector[] sectorArr = this.sectors;
            if (i >= sectorArr.length) {
                return;
            }
            Sector sector = sectorArr[i];
            if (sector.particle == null) {
                sector.particle = this.svgParticles ? SvgHelper.getBitmap(this.particles[i], AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), -1) : BitmapFactory.decodeResource(getContext().getResources(), this.particles[i]);
            }
            i++;
        }
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        requestLayout();
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        int i = 0;
        this.isAttached = false;
        while (true) {
            Sector[] sectorArr = this.sectors;
            if (i >= sectorArr.length) {
                return;
            }
            Bitmap bitmap = sectorArr[i].particle;
            if (bitmap != null) {
                bitmap.recycle();
                this.sectors[i].particle = null;
            }
            i++;
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int dp = AndroidUtilities.dp(heightDp());
        int dp2 = AndroidUtilities.dp(172.0f);
        this.chartMeasureBounds.set((size - dp2) / 2.0f, (dp - dp2) / 2.0f, (size + dp2) / 2.0f, (dp2 + dp) / 2.0f);
        this.completeGradientMatrix.reset();
        this.completeGradientMatrix.setTranslate(this.chartMeasureBounds.left, 0.0f);
        this.completeGradient.setLocalMatrix(this.completeGradientMatrix);
        this.completeTextGradientMatrix.reset();
        Matrix matrix = this.completeTextGradientMatrix;
        RectF rectF = this.chartMeasureBounds;
        matrix.setTranslate(rectF.left, -rectF.centerY());
        this.completeTextGradient.setLocalMatrix(this.completeTextGradientMatrix);
        StarParticlesView.Drawable drawable = this.completeDrawable;
        if (drawable != null) {
            drawable.rect.set(0.0f, 0.0f, AndroidUtilities.dp(140.0f), AndroidUtilities.dp(140.0f));
            this.completeDrawable.rect.offset((getMeasuredWidth() - this.completeDrawable.rect.width()) / 2.0f, (getMeasuredHeight() - this.completeDrawable.rect.height()) / 2.0f);
            this.completeDrawable.rect2.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            this.completeDrawable.resetPositions();
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(dp, 1073741824));
    }

    protected void onSectionClick(int i) {
    }

    protected abstract void onSectionDown(int i, boolean z);

    protected int padInsideDp() {
        return 0;
    }

    public void setInterceptTouch(boolean z) {
        this.interceptTouch = z;
    }

    public void setSegments(long j, boolean z, SegmentSize... segmentSizeArr) {
        int i;
        char c;
        String str;
        float f;
        float f2;
        int i2;
        int i3;
        int i4;
        SegmentSize[] segmentSizeArr2 = segmentSizeArr;
        if (segmentSizeArr2 == null || segmentSizeArr2.length == 0) {
            this.loading = false;
            this.complete = j == 0;
            if (!z) {
                this.loadingFloat.set(0.0f, true);
                this.completeFloat.set(this.complete ? 1.0f : 0.0f, true);
            }
            this.topCompleteText.setText(this.topText.getText(), false);
            this.topText.setText("0", z);
            this.topCompleteText.setText("0", z);
            this.bottomCompleteText.setText(this.bottomText.getText(), false);
            this.bottomText.setText("KB", z);
            this.bottomCompleteText.setText("KB", z);
            int i5 = 0;
            while (true) {
                Sector[] sectorArr = this.sectors;
                if (i5 >= sectorArr.length) {
                    invalidate();
                    return;
                }
                Sector sector = sectorArr[i5];
                sector.textAlpha = 0.0f;
                if (z) {
                    i = 1;
                } else {
                    i = 1;
                    sector.textAlphaAnimated.set(0.0f, true);
                }
                i5 += i;
            }
        } else {
            this.loading = false;
            if (!z) {
                this.loadingFloat.set(0.0f, true);
            }
            SpannableString spannableString = new SpannableString("%");
            int length = segmentSizeArr2.length;
            long j2 = 0;
            for (int i6 = 0; i6 < segmentSizeArr2.length; i6++) {
                if (segmentSizeArr2[i6] == null) {
                    SegmentSize segmentSize = new SegmentSize();
                    segmentSizeArr2[i6] = segmentSize;
                    segmentSize.size = 0L;
                }
                SegmentSize segmentSize2 = segmentSizeArr2[i6];
                segmentSize2.index = i6;
                boolean z2 = segmentSize2.selected;
                if (z2) {
                    j2 += segmentSize2.size;
                }
                if (segmentSize2.size <= 0 || !z2) {
                    length--;
                }
            }
            if (j2 > 0) {
                float f3 = 0.0f;
                int i7 = 0;
                int i8 = 0;
                while (i7 < segmentSizeArr2.length) {
                    SegmentSize segmentSize3 = segmentSizeArr2[i7];
                    float f4 = (segmentSize3 == null || !segmentSize3.selected) ? 0.0f : segmentSize3.size / j2;
                    if (f4 <= 0.0f || f4 >= 0.02f) {
                        i3 = 1;
                    } else {
                        i3 = 1;
                        i8++;
                        f3 += f4;
                    }
                    i7 += i3;
                }
                Math.min(segmentSizeArr2.length, this.sectors.length);
                int[] iArr = this.tempPercents;
                if (iArr == null || iArr.length != segmentSizeArr2.length) {
                    this.tempPercents = new int[segmentSizeArr2.length];
                }
                float[] fArr = this.tempFloat;
                if (fArr == null || fArr.length != segmentSizeArr2.length) {
                    this.tempFloat = new float[segmentSizeArr2.length];
                }
                for (int i9 = 0; i9 < segmentSizeArr2.length; i9++) {
                    float[] fArr2 = this.tempFloat;
                    SegmentSize segmentSize4 = segmentSizeArr2[i9];
                    fArr2[i9] = (segmentSize4 == null || !segmentSize4.selected) ? 0.0f : segmentSize4.size / j2;
                }
                AndroidUtilities.roundPercents(this.tempFloat, this.tempPercents);
                if (this.type == 0) {
                    Arrays.sort(segmentSizeArr2, new Comparator() { // from class: org.telegram.ui.Components.CacheChart$$ExternalSyntheticLambda0
                        @Override // java.util.Comparator
                        public final int compare(Object obj, Object obj2) {
                            int lambda$setSegments$0;
                            lambda$setSegments$0 = CacheChart.lambda$setSegments$0((CacheChart.SegmentSize) obj, (CacheChart.SegmentSize) obj2);
                            return lambda$setSegments$0;
                        }
                    });
                    int i10 = 0;
                    while (true) {
                        if (i10 > segmentSizeArr2.length) {
                            break;
                        }
                        SegmentSize segmentSize5 = segmentSizeArr2[i10];
                        if (segmentSize5.index == segmentSizeArr2.length - 1) {
                            SegmentSize segmentSize6 = segmentSizeArr2[0];
                            segmentSizeArr2[0] = segmentSize5;
                            segmentSizeArr2[i10] = segmentSize6;
                            break;
                        }
                        i10++;
                    }
                }
                if (length < 2) {
                    length = 0;
                }
                float f5 = 360.0f - (length * 2.0f);
                float f6 = 0.0f;
                int i11 = 0;
                int i12 = 0;
                while (i11 < segmentSizeArr2.length) {
                    SegmentSize segmentSize7 = segmentSizeArr2[i11];
                    int i13 = segmentSize7.index;
                    if (segmentSize7.selected) {
                        f = f5;
                        f2 = segmentSize7.size / j2;
                    } else {
                        f = f5;
                        f2 = 0.0f;
                    }
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    long j3 = j2;
                    spannableStringBuilder.append((CharSequence) String.format("%d", Integer.valueOf(this.tempPercents[i13])));
                    spannableStringBuilder.append((CharSequence) spannableString);
                    Sector sector2 = this.sectors[i13];
                    float f7 = (((double) f2) <= 0.05d || f2 >= 1.0f) ? 0.0f : 1.0f;
                    sector2.textAlpha = f7;
                    sector2.textScale = (f2 < 0.08f || this.tempPercents[i13] >= 100) ? 0.85f : 1.0f;
                    sector2.particlesAlpha = 1.0f;
                    if (!z) {
                        sector2.textAlphaAnimated.set(f7, true);
                        Sector sector3 = this.sectors[i13];
                        sector3.textScaleAnimated.set(sector3.textScale, true);
                        Sector sector4 = this.sectors[i13];
                        sector4.particlesAlphaAnimated.set(sector4.particlesAlpha, true);
                    }
                    Sector sector5 = this.sectors[i13];
                    if (sector5.textAlpha > 0.0f) {
                        sector5.text.setText(spannableStringBuilder, z);
                    }
                    float f8 = (f2 >= 0.02f || f2 <= 0.0f) ? f2 * (1.0f - ((i8 * 0.02f) - f3)) : 0.02f;
                    float f9 = (f6 * f) + (i12 * 2.0f);
                    float f10 = (f8 * f) + f9;
                    if (f8 <= 0.0f) {
                        Sector sector6 = this.sectors[i13];
                        sector6.angleCenter = (f9 + f10) / 2.0f;
                        sector6.angleSize = Math.abs(f10 - f9) / 2.0f;
                        Sector sector7 = this.sectors[i13];
                        sector7.textAlpha = 0.0f;
                        if (!z) {
                            sector7.angleCenterAnimated.set(sector7.angleCenter, true);
                            Sector sector8 = this.sectors[i13];
                            sector8.angleSizeAnimated.set(sector8.angleSize, true);
                            Sector sector9 = this.sectors[i13];
                            sector9.textAlphaAnimated.set(sector9.textAlpha, true);
                        }
                        i2 = 1;
                    } else {
                        Sector sector10 = this.sectors[i13];
                        sector10.angleCenter = (f9 + f10) / 2.0f;
                        sector10.angleSize = Math.abs(f10 - f9) / 2.0f;
                        if (z) {
                            i2 = 1;
                        } else {
                            Sector sector11 = this.sectors[i13];
                            i2 = 1;
                            sector11.angleCenterAnimated.set(sector11.angleCenter, true);
                            Sector sector12 = this.sectors[i13];
                            sector12.angleSizeAnimated.set(sector12.angleSize, true);
                        }
                        f6 += f8;
                        i12 += i2;
                    }
                    i11 += i2;
                    segmentSizeArr2 = segmentSizeArr;
                    f5 = f;
                    j2 = j3;
                }
                long j4 = j2;
                String[] split = AndroidUtilities.formatFileSize(j4, true, true).split(" ");
                if (split.length > 0) {
                    c = 0;
                    str = split[0];
                } else {
                    c = 0;
                    str = "";
                }
                if (str.length() >= 4 && j4 < 1073741824) {
                    str = str.split("\\.")[c];
                }
                this.topText.setText(str, z);
                this.bottomText.setText(split.length > 1 ? split[1] : "", z);
                if (this.completeFloat.get() > 0.0f) {
                    this.topCompleteText.setText(this.topText.getText(), z);
                    this.bottomCompleteText.setText(this.bottomText.getText(), z);
                }
                this.complete = false;
                if (!z) {
                    this.completeFloat.set(0.0f, true);
                }
                invalidate();
                return;
            }
            this.loading = false;
            this.complete = j <= 0;
            if (!z) {
                this.loadingFloat.set(0.0f, true);
                this.completeFloat.set(this.complete ? 1.0f : 0.0f, true);
            }
            this.topCompleteText.setText(this.topText.getText(), false);
            this.topText.setText("0", z);
            this.topCompleteText.setText("0", z);
            this.bottomCompleteText.setText(this.bottomText.getText(), false);
            this.bottomText.setText("KB", z);
            this.bottomCompleteText.setText("KB", z);
            int i14 = 0;
            while (true) {
                Sector[] sectorArr2 = this.sectors;
                if (i14 >= sectorArr2.length) {
                    invalidate();
                    return;
                }
                Sector sector13 = sectorArr2[i14];
                sector13.textAlpha = 0.0f;
                if (z) {
                    i4 = 1;
                } else {
                    i4 = 1;
                    sector13.textAlphaAnimated.set(0.0f, true);
                }
                i14 += i4;
            }
        }
    }

    public void setSelected(int i) {
        if (i == this.selectedIndex) {
            return;
        }
        int i2 = 0;
        while (true) {
            Sector[] sectorArr = this.sectors;
            if (i2 >= sectorArr.length) {
                this.selectedIndex = i;
                invalidate();
                return;
            }
            if (i == i2 && sectorArr[i2].angleSize <= 0.0f) {
                i = -1;
            }
            sectorArr[i2].selected = i == i2;
            i2++;
        }
    }
}
