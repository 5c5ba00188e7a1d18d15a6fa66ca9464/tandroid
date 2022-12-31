package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Bitmap;
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
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;
import org.telegram.messenger.SvgHelper;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.Premium.StarParticlesView;
/* loaded from: classes3.dex */
public class CacheChart extends View {
    private static final String[] colorKeys = {"statisticChartLine_lightblue", "statisticChartLine_blue", "statisticChartLine_green", "statisticChartLine_red", "statisticChartLine_lightgreen", "statisticChartLine_orange", "statisticChartLine_cyan", "statisticChartLine_purple", "statisticChartLine_golden"};
    private static Long loadedStart;
    private static final int[] particles;
    private static long particlesStart;
    private static Long start;
    private AnimatedTextView.AnimatedTextDrawable bottomText;
    private RectF chartBounds;
    private RectF chartInnerBounds;
    private boolean complete;
    private StarParticlesView.Drawable completeDrawable;
    private AnimatedFloat completeFloat;
    private LinearGradient completeGradient;
    private Matrix completeGradientMatrix;
    private Paint completePaint;
    private Paint completePaintStroke;
    private Path completePath;
    private RectF completePathBounds;
    private boolean loading;
    private Paint loadingBackgroundPaint;
    public AnimatedFloat loadingFloat;
    private RectF roundingRect;
    private Sector[] sectors;
    private float[] segmentsTmp;
    private int selectedIndex;
    private float[] tempFloat;
    private int[] tempPercents;
    private AnimatedTextView.AnimatedTextDrawable topText;

    protected int heightDp() {
        return 200;
    }

    protected void onSectionClick(int i) {
    }

    protected void onSectionDown(int i, boolean z) {
    }

    static {
        int i = R.raw.cache_videos;
        int i2 = R.raw.cache_other;
        particles = new int[]{R.raw.cache_photos, i, R.raw.cache_documents, R.raw.cache_music, i, R.raw.cache_stickers, R.raw.cache_profile_photos, i2, i2};
        particlesStart = -1L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class Sector {
        float angleCenter;
        AnimatedFloat angleCenterAnimated;
        float angleSize;
        AnimatedFloat angleSizeAnimated;
        Paint cut;
        RadialGradient gradient;
        Matrix gradientMatrix;
        private float lastAngleCenter;
        private float lastAngleSize;
        private float lastCx;
        private float lastCy;
        private float lastRounding;
        private float lastThickness;
        private float lastWidth;
        Paint paint;
        Bitmap particle;
        Paint particlePaint = new Paint(3);
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
            this.text.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.text.setTextSize(AndroidUtilities.dp(15.0f));
            this.text.setGravity(17);
            this.path = new Path();
            this.paint = new Paint(1);
            this.pathBounds = new RectF();
            this.uncut = new Paint(1);
            Paint paint = new Paint(1);
            this.cut = paint;
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            this.particlePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            this.rectF = new RectF();
        }

        private void setupPath(RectF rectF, RectF rectF2, float f, float f2, float f3) {
            float f4;
            float f5;
            float f6;
            float f7;
            float f8;
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
            float f9 = f - f2;
            float f10 = f + f2;
            boolean z = min2 > 0.0f;
            float f11 = min2 * 2.0f;
            double width3 = rectF.width() - f11;
            Double.isNaN(width3);
            float f12 = (min2 / ((float) (width3 * 3.141592653589793d))) * 360.0f;
            double width4 = rectF2.width() + f11;
            Double.isNaN(width4);
            float f13 = ((min2 / ((float) (width4 * 3.141592653589793d))) * 360.0f) + ((f2 > 175.0f ? 0 : 1) * 0.5f);
            float width5 = (rectF.width() / 2.0f) - min2;
            float width6 = (rectF2.width() / 2.0f) + min2;
            this.path.rewind();
            float f14 = f10 - f9;
            if (f14 < 0.5f) {
                return;
            }
            if (z) {
                RectF rectF3 = CacheChart.this.roundingRect;
                double centerX = rectF.centerX();
                double d2 = width5;
                f4 = width6;
                double cos = Math.cos(CacheChart.toRad(f8));
                Double.isNaN(d2);
                Double.isNaN(centerX);
                double d3 = centerX + (cos * d2);
                double centerY = rectF.centerY();
                double sin = Math.sin(CacheChart.toRad(f8));
                Double.isNaN(d2);
                Double.isNaN(centerY);
                CacheChart.setCircleBounds(rectF3, d3, (d2 * sin) + centerY, min2);
                this.path.arcTo(CacheChart.this.roundingRect, (f9 + f12) - 90.0f, 90.0f);
            } else {
                f4 = width6;
            }
            this.path.arcTo(rectF, f9 + f12, f14 - (f12 * 2.0f));
            if (z) {
                RectF rectF4 = CacheChart.this.roundingRect;
                double centerX2 = rectF.centerX();
                double d4 = width5;
                float f15 = f10 - f12;
                double cos2 = Math.cos(CacheChart.toRad(f15));
                Double.isNaN(d4);
                Double.isNaN(centerX2);
                double d5 = centerX2 + (cos2 * d4);
                double centerY2 = rectF.centerY();
                f5 = f9;
                double sin2 = Math.sin(CacheChart.toRad(f15));
                Double.isNaN(d4);
                Double.isNaN(centerY2);
                CacheChart.setCircleBounds(rectF4, d5, centerY2 + (d4 * sin2), min2);
                this.path.arcTo(CacheChart.this.roundingRect, f15, 90.0f);
                RectF rectF5 = CacheChart.this.roundingRect;
                double centerX3 = rectF2.centerX();
                double d6 = f4;
                double cos3 = Math.cos(CacheChart.toRad(f7));
                Double.isNaN(d6);
                Double.isNaN(centerX3);
                double d7 = centerX3 + (cos3 * d6);
                double centerY3 = rectF2.centerY();
                double sin3 = Math.sin(CacheChart.toRad(f7));
                Double.isNaN(d6);
                Double.isNaN(centerY3);
                CacheChart.setCircleBounds(rectF5, d7, centerY3 + (d6 * sin3), min2);
                this.path.arcTo(CacheChart.this.roundingRect, (f10 - f13) + 90.0f, 90.0f);
            } else {
                f5 = f9;
            }
            this.path.arcTo(rectF2, f10 - f13, -(f14 - (f13 * 2.0f)));
            if (z) {
                RectF rectF6 = CacheChart.this.roundingRect;
                double centerX4 = rectF2.centerX();
                double d8 = f4;
                double cos4 = Math.cos(CacheChart.toRad(f6));
                Double.isNaN(d8);
                Double.isNaN(centerX4);
                double d9 = centerX4 + (cos4 * d8);
                double centerY4 = rectF2.centerY();
                double sin4 = Math.sin(CacheChart.toRad(f6));
                Double.isNaN(d8);
                Double.isNaN(centerY4);
                CacheChart.setCircleBounds(rectF6, d9, centerY4 + (d8 * sin4), min2);
                this.path.arcTo(CacheChart.this.roundingRect, f5 + f13 + 180.0f, 90.0f);
            }
            this.path.close();
            this.path.computeBounds(this.pathBounds, false);
        }

        private void setGradientBounds(float f, float f2, float f3, float f4) {
            this.gradientMatrix.reset();
            this.gradientMatrix.setTranslate(f, f2);
            this.gradient.setLocalMatrix(this.gradientMatrix);
        }

        private void drawParticles(Canvas canvas, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
            if (f10 <= 0.0f) {
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            float sqrt = (float) Math.sqrt(2.0d);
            if (CacheChart.particlesStart < 0) {
                long unused = CacheChart.particlesStart = currentTimeMillis;
            }
            float f11 = ((float) (currentTimeMillis - CacheChart.particlesStart)) / 10000.0f;
            Bitmap bitmap = this.particle;
            if (bitmap != null) {
                int width = bitmap.getWidth();
                float f12 = 7.0f;
                int floor = (int) Math.floor((f5 % 360.0f) / 7.0f);
                int ceil = (int) Math.ceil((f6 % 360.0f) / 7.0f);
                while (floor <= ceil) {
                    float f13 = floor * f12;
                    double d = 100.0f + f11;
                    Double.isNaN(d);
                    float sin = (float) ((d * (((Math.sin(2000.0f * f13) + 1.0d) * 0.25d) + 1.0d)) % 1.0d);
                    float f14 = width * sqrt;
                    double d2 = f;
                    float f15 = f11;
                    double lerp = AndroidUtilities.lerp(f7 - f14, f8 + f14, sin);
                    double cos = Math.cos(CacheChart.toRad(f13));
                    Double.isNaN(lerp);
                    Double.isNaN(d2);
                    float f16 = (float) (d2 + (cos * lerp));
                    double d3 = f2;
                    double sin2 = Math.sin(CacheChart.toRad(f13));
                    Double.isNaN(lerp);
                    Double.isNaN(d3);
                    float f17 = (float) (d3 + (lerp * sin2));
                    float abs = 0.65f * f10 * ((Math.abs(sin - 0.5f) * (-1.75f)) + 1.0f);
                    double d4 = sin;
                    Double.isNaN(d4);
                    double d5 = d4 * 3.141592653589793d;
                    this.particlePaint.setAlpha((int) (Math.max(0.0f, Math.min(1.0f, abs * ((((float) (Math.sin(d5) - 1.0d)) * 0.25f) + 1.0f) * AndroidUtilities.lerp(1.0f, Math.min(MathUtils.distance(f16, f17, f3, f4) / AndroidUtilities.dpf2(64.0f), 1.0f), f9))) * 255.0f));
                    double sin3 = ((((float) (Math.sin(d5) - 1.0d)) * 0.25f) + 1.0f) * 0.75f;
                    Double.isNaN(sin3);
                    float sin4 = (float) (sin3 * (((Math.sin(f13) + 1.0d) * 0.25d) + 0.800000011920929d));
                    canvas.save();
                    canvas.translate(f16, f17);
                    canvas.scale(sin4, sin4);
                    float f18 = -(width >> 1);
                    canvas.drawBitmap(this.particle, f18, f18, this.particlePaint);
                    canvas.restore();
                    floor++;
                    ceil = ceil;
                    f11 = f15;
                    sqrt = sqrt;
                    f12 = 7.0f;
                }
            }
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
                canvas.saveLayerAlpha(this.rectF, 255, 31);
                canvas.drawCircle(this.rectF.centerX(), this.rectF.centerY(), this.rectF.width() / 2.0f, this.uncut);
                canvas.drawRect(this.rectF, this.paint);
                f6 = f10;
                f7 = f9;
                drawParticles(canvas, this.rectF.centerX(), this.rectF.centerY(), f9, f10, 0.0f, 359.0f, rectF2.width() / 2.0f, this.rectF.width() / 2.0f, f11, Math.max(0.0f, (f5 / 0.75f) - 0.75f) * f12);
                canvas.drawCircle(rectF2.centerX(), rectF2.centerY(), rectF2.width() / 2.0f, this.cut);
                canvas.restore();
            } else {
                f6 = f10;
                f7 = f9;
                setupPath(this.rectF, rectF2, f, f2, f3);
                setGradientBounds(this.rectF.centerX(), rectF.centerY(), this.rectF.width() / 2.0f, f);
                canvas.saveLayerAlpha(this.rectF, 255, 31);
                canvas.drawPath(this.path, this.uncut);
                canvas.drawRect(this.rectF, this.paint);
                drawParticles(canvas, this.rectF.centerX(), this.rectF.centerY(), f7, f6, f - f2, f + f2, rectF2.width() / 2.0f, this.rectF.width() / 2.0f, f11, Math.max(0.0f, (f5 / 0.75f) - 0.75f) * f12);
                canvas.restore();
            }
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

    public CacheChart(Context context) {
        super(context);
        this.chartBounds = new RectF();
        this.chartInnerBounds = new RectF();
        this.loading = true;
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.loadingFloat = new AnimatedFloat(this, 750L, cubicBezierInterpolator);
        this.complete = false;
        this.completeFloat = new AnimatedFloat(this, 750L, cubicBezierInterpolator);
        this.sectors = new Sector[9];
        this.segmentsTmp = new float[2];
        this.roundingRect = new RectF();
        this.loadingBackgroundPaint = new Paint(1);
        this.completePath = new Path();
        this.completePaintStroke = new Paint(1);
        this.completePaint = new Paint(1);
        this.topText = new AnimatedTextView.AnimatedTextDrawable(false, true, true);
        this.bottomText = new AnimatedTextView.AnimatedTextDrawable(false, true, true);
        this.selectedIndex = -1;
        this.loadingBackgroundPaint.setStyle(Paint.Style.STROKE);
        this.loadingBackgroundPaint.setColor(Theme.getColor("listSelectorSDK21"));
        this.completePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        this.completeGradient = new LinearGradient(0.0f, 0.0f, 0.0f, AndroidUtilities.dp(200.0f), new int[]{7263574, -9513642, -12469647, 4307569}, new float[]{0.0f, 0.07f, 0.93f, 1.0f}, Shader.TileMode.CLAMP);
        this.completeGradientMatrix = new Matrix();
        this.completePaintStroke.setShader(this.completeGradient);
        this.completePaint.setShader(this.completeGradient);
        this.completePaintStroke.setStyle(Paint.Style.STROKE);
        this.completePaintStroke.setStrokeCap(Paint.Cap.ROUND);
        this.completePaintStroke.setStrokeJoin(Paint.Join.ROUND);
        this.topText.setAnimationProperties(0.2f, 0L, 450L, cubicBezierInterpolator);
        this.topText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.topText.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.topText.setTextSize(AndroidUtilities.dp(32.0f));
        this.topText.setGravity(17);
        this.bottomText.setAnimationProperties(0.6f, 0L, 450L, cubicBezierInterpolator);
        this.bottomText.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
        this.bottomText.setTextSize(AndroidUtilities.dp(12.0f));
        this.bottomText.setGravity(17);
        for (int i = 0; i < 9; i++) {
            Sector[] sectorArr = this.sectors;
            Sector sector = new Sector();
            sectorArr[i] = sector;
            String[] strArr = colorKeys;
            int blendOver = Theme.blendOver(Theme.getColor(strArr[i]), ConnectionsManager.FileTypeAudio);
            int blendOver2 = Theme.blendOver(Theme.getColor(strArr[i]), 822083583);
            AndroidUtilities.dp(50.0f);
            RadialGradient radialGradient = new RadialGradient(0.0f, 0.0f, AndroidUtilities.dp(86.0f), new int[]{blendOver2, blendOver}, new float[]{0.3f, 1.0f}, Shader.TileMode.CLAMP);
            sector.gradient = radialGradient;
            Matrix matrix = new Matrix();
            sector.gradientMatrix = matrix;
            radialGradient.setLocalMatrix(matrix);
            sector.paint.setShader(sector.gradient);
            sector.particle = SvgHelper.getBitmap(particles[i], AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), -1);
        }
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int i;
        boolean z;
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        float distance = MathUtils.distance(this.chartBounds.centerX(), this.chartBounds.centerY(), x, y);
        float atan2 = (float) ((Math.atan2(y - this.chartBounds.centerY(), x - this.chartBounds.centerX()) / 3.141592653589793d) * 180.0d);
        if (atan2 < 0.0f) {
            atan2 += 360.0f;
        }
        if (distance > this.chartInnerBounds.width() / 2.0f && distance < (this.chartBounds.width() / 2.0f) + AndroidUtilities.dp(14.0f)) {
            i = 0;
            while (true) {
                Sector[] sectorArr = this.sectors;
                if (i >= sectorArr.length) {
                    break;
                } else if (atan2 >= sectorArr[i].angleCenter - sectorArr[i].angleSize && atan2 <= sectorArr[i].angleCenter + sectorArr[i].angleSize) {
                    break;
                } else {
                    i++;
                }
            }
        }
        i = -1;
        if (motionEvent.getAction() == 0) {
            setSelected(i);
            if (i >= 0) {
                onSectionDown(i, i != -1);
                if (getParent() != null) {
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

    public void setSelected(int i) {
        if (i == this.selectedIndex) {
            return;
        }
        int i2 = 0;
        while (true) {
            Sector[] sectorArr = this.sectors;
            if (i2 < sectorArr.length) {
                if (i == i2 && sectorArr[i2].angleSize <= 0.0f) {
                    i = -1;
                }
                sectorArr[i2].selected = i == i2;
                i2++;
            } else {
                this.selectedIndex = i;
                invalidate();
                return;
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class SegmentSize {
        int index;
        boolean selected;
        long size;

        public static SegmentSize of(long j, boolean z) {
            SegmentSize segmentSize = new SegmentSize();
            segmentSize.size = j;
            segmentSize.selected = z;
            return segmentSize;
        }
    }

    public void setSegments(long j, SegmentSize... segmentSizeArr) {
        char c;
        String str;
        int i;
        float f;
        SegmentSize[] segmentSizeArr2 = segmentSizeArr;
        String str2 = "";
        int i2 = 0;
        if (segmentSizeArr2 == null || segmentSizeArr2.length == 0) {
            this.loading = true;
            this.complete = j == 0;
            this.topText.setText("");
            this.bottomText.setText("");
            int i3 = 0;
            while (true) {
                Sector[] sectorArr = this.sectors;
                if (i3 < sectorArr.length) {
                    sectorArr[i3].textAlpha = 0.0f;
                    i3++;
                } else {
                    invalidate();
                    return;
                }
            }
        } else {
            this.loading = false;
            SpannableString spannableString = new SpannableString("%");
            int length = segmentSizeArr2.length;
            long j2 = 0;
            for (int i4 = 0; i4 < segmentSizeArr2.length; i4++) {
                if (segmentSizeArr2[i4] == null) {
                    segmentSizeArr2[i4] = new SegmentSize();
                    segmentSizeArr2[i4].size = 0L;
                }
                segmentSizeArr2[i4].index = i4;
                if (segmentSizeArr2[i4] != null && segmentSizeArr2[i4].selected) {
                    j2 += segmentSizeArr2[i4].size;
                }
                if (segmentSizeArr2[i4] == null || segmentSizeArr2[i4].size <= 0 || !segmentSizeArr2[i4].selected) {
                    length--;
                }
            }
            if (j2 <= 0) {
                this.loading = true;
                this.complete = j <= 0;
                this.topText.setText("");
                this.bottomText.setText("");
                while (true) {
                    Sector[] sectorArr2 = this.sectors;
                    if (i2 < sectorArr2.length) {
                        sectorArr2[i2].textAlpha = 0.0f;
                        i2++;
                    } else {
                        invalidate();
                        return;
                    }
                }
            } else {
                int i5 = 0;
                float f2 = 0.0f;
                for (int i6 = 0; i6 < segmentSizeArr2.length; i6++) {
                    float f3 = (segmentSizeArr2[i6] == null || !segmentSizeArr2[i6].selected) ? 0.0f : ((float) segmentSizeArr2[i6].size) / ((float) j2);
                    if (f3 > 0.0f && f3 < 0.02f) {
                        i5++;
                        f2 += f3;
                    }
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
                for (int i7 = 0; i7 < segmentSizeArr2.length; i7++) {
                    this.tempFloat[i7] = (segmentSizeArr2[i7] == null || !segmentSizeArr2[i7].selected) ? 0.0f : ((float) segmentSizeArr2[i7].size) / ((float) j2);
                }
                AndroidUtilities.roundPercents(this.tempFloat, this.tempPercents);
                Arrays.sort(segmentSizeArr2, CacheChart$$ExternalSyntheticLambda0.INSTANCE);
                int i8 = 0;
                while (true) {
                    if (i8 >= segmentSizeArr2.length - 1) {
                        break;
                    } else if (segmentSizeArr2[i8].index == segmentSizeArr2.length - 1) {
                        SegmentSize segmentSize = segmentSizeArr2[0];
                        segmentSizeArr2[0] = segmentSizeArr2[i8];
                        segmentSizeArr2[i8] = segmentSize;
                        break;
                    } else {
                        i8++;
                    }
                }
                if (length < 2) {
                    length = 0;
                }
                float f4 = 360.0f - (length * 2.0f);
                int i9 = 0;
                int i10 = 0;
                float f5 = 0.0f;
                while (i9 < segmentSizeArr2.length) {
                    int i11 = segmentSizeArr2[i9].index;
                    if (segmentSizeArr2[i9] == null || !segmentSizeArr2[i9].selected) {
                        i = i9;
                        f = 0.0f;
                    } else {
                        i = i9;
                        f = ((float) segmentSizeArr2[i9].size) / ((float) j2);
                    }
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    spannableStringBuilder.append((CharSequence) String.format("%d", Integer.valueOf(this.tempPercents[i11])));
                    spannableStringBuilder.append((CharSequence) spannableString);
                    Sector[] sectorArr3 = this.sectors;
                    String str3 = str2;
                    SpannableString spannableString2 = spannableString;
                    sectorArr3[i11].textAlpha = (((double) f) <= 0.05d || f >= 1.0f) ? 0.0f : 1.0f;
                    sectorArr3[i11].textScale = (f < 0.08f || this.tempPercents[i11] >= 100) ? 0.85f : 1.0f;
                    sectorArr3[i11].particlesAlpha = 1.0f;
                    if (sectorArr3[i11].textAlpha > 0.0f) {
                        sectorArr3[i11].text.setText(spannableStringBuilder);
                    }
                    float f6 = (f >= 0.02f || f <= 0.0f) ? f * (1.0f - ((i5 * 0.02f) - f2)) : 0.02f;
                    float f7 = (f5 * f4) + (i10 * 2.0f);
                    float f8 = (f6 * f4) + f7;
                    if (f6 <= 0.0f) {
                        Sector[] sectorArr4 = this.sectors;
                        sectorArr4[i11].angleCenter = (f7 + f8) / 2.0f;
                        sectorArr4[i11].angleSize = Math.abs(f8 - f7) / 2.0f;
                        this.sectors[i11].textAlpha = 0.0f;
                    } else {
                        Sector[] sectorArr5 = this.sectors;
                        sectorArr5[i11].angleCenter = (f7 + f8) / 2.0f;
                        sectorArr5[i11].angleSize = Math.abs(f8 - f7) / 2.0f;
                        f5 += f6;
                        i10++;
                    }
                    i9 = i + 1;
                    segmentSizeArr2 = segmentSizeArr;
                    str2 = str3;
                    spannableString = spannableString2;
                }
                String str4 = str2;
                String[] split = AndroidUtilities.formatFileSize(j2).split(" ");
                if (split.length > 0) {
                    c = 0;
                    str = split[0];
                } else {
                    c = 0;
                    str = str4;
                }
                if (str.length() >= 4 && j2 < 1073741824) {
                    str = str.split("\\.")[c];
                }
                this.topText.setText(str);
                this.bottomText.setText(split.length > 1 ? split[1] : str4);
                invalidate();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$setSegments$0(SegmentSize segmentSize, SegmentSize segmentSize2) {
        return Long.compare(segmentSize.size, segmentSize2.size);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static float toRad(float f) {
        double d = f / 180.0f;
        Double.isNaN(d);
        return (float) (d * 3.141592653589793d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setCircleBounds(RectF rectF, float f, float f2, float f3) {
        rectF.set(f - f3, f2 - f3, f + f3, f2 + f3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setCircleBounds(RectF rectF, double d, double d2, float f) {
        setCircleBounds(rectF, (float) d, (float) d2, f);
    }

    @Override // android.view.View
    protected void dispatchDraw(Canvas canvas) {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7 = 0.0f;
        float f8 = this.loadingFloat.set(this.loading ? 1.0f : 0.0f);
        float f9 = this.completeFloat.set(this.complete ? 1.0f : 0.0f);
        this.chartInnerBounds.set(this.chartBounds);
        float lerp = AndroidUtilities.lerp(AndroidUtilities.dpf2(38.0f), AndroidUtilities.dpf2(10.0f), f8);
        this.chartInnerBounds.inset(lerp, lerp);
        char c = 0;
        float lerp2 = AndroidUtilities.lerp(0, AndroidUtilities.dp(60.0f), f8);
        if (start == null) {
            start = Long.valueOf(System.currentTimeMillis());
        }
        boolean z = this.loading;
        if (!z && loadedStart == null) {
            loadedStart = Long.valueOf(System.currentTimeMillis());
        } else if (z && loadedStart != null) {
            loadedStart = null;
        }
        Long l = loadedStart;
        float currentTimeMillis = ((float) ((l == null ? System.currentTimeMillis() : l.longValue()) - start.longValue())) * 0.6f;
        CircularProgressDrawable.getSegments(currentTimeMillis % 5400.0f, this.segmentsTmp);
        float[] fArr = this.segmentsTmp;
        float f10 = fArr[0];
        char c2 = 1;
        float f11 = fArr[1];
        if (f8 > 0.0f) {
            this.loadingBackgroundPaint.setStrokeWidth(lerp);
            int alpha = this.loadingBackgroundPaint.getAlpha();
            this.loadingBackgroundPaint.setAlpha((int) (alpha * f8));
            canvas.drawCircle(this.chartBounds.centerX(), this.chartBounds.centerY(), (this.chartBounds.width() - lerp) / 2.0f, this.loadingBackgroundPaint);
            this.loadingBackgroundPaint.setAlpha(alpha);
        }
        boolean z2 = f8 > 0.0f || f9 > 0.0f;
        int i = 0;
        while (i < 9) {
            Sector sector = this.sectors[i];
            CircularProgressDrawable.getSegments((currentTimeMillis + (i * 80)) % 5400.0f, this.segmentsTmp);
            float min = Math.min(Math.max(this.segmentsTmp[c], f10), f11);
            float min2 = Math.min(Math.max(this.segmentsTmp[c2], f10), f11);
            if (f8 < 1.0f || min < min2) {
                float f12 = (min + min2) / 2.0f;
                float abs = Math.abs(min2 - min) / 2.0f;
                if (f8 <= f7) {
                    float f13 = sector.angleCenterAnimated.set(sector.angleCenter);
                    f = f10;
                    f2 = lerp2;
                    f3 = sector.angleSizeAnimated.set(sector.angleSize);
                    f12 = f13;
                } else {
                    if (f8 < 1.0f) {
                        f = f10;
                        f2 = lerp2;
                        f12 = AndroidUtilities.lerp(sector.angleCenterAnimated.set(sector.angleCenter) + (((float) Math.floor(f11 / 360.0f)) * 360.0f), f12, f8);
                        abs = AndroidUtilities.lerp(sector.angleSizeAnimated.set(sector.angleSize), abs, f8);
                    } else {
                        f = f10;
                        f2 = lerp2;
                    }
                    f3 = abs;
                }
                boolean z3 = sector.angleCenterAnimated.isInProgress() || sector.angleSizeAnimated.isInProgress() || z2;
                f4 = f11;
                f5 = f;
                f6 = lerp;
                sector.draw(canvas, this.chartBounds, this.chartInnerBounds, f12, f3, f2, 1.0f - f9, 1.0f - f8);
                z2 = z3;
            } else {
                f4 = f11;
                f5 = f10;
                f2 = lerp2;
                f6 = lerp;
            }
            i++;
            f10 = f5;
            lerp2 = f2;
            f11 = f4;
            lerp = f6;
            c2 = 1;
            c = 0;
            f7 = 0.0f;
        }
        float f14 = lerp;
        int i2 = (int) ((1.0f - f8) * 255.0f * (1.0f - f9));
        this.topText.setAlpha(i2);
        this.topText.setBounds((int) this.chartBounds.centerX(), (int) (this.chartBounds.centerY() - AndroidUtilities.dp(5.0f)), (int) this.chartBounds.centerX(), (int) (this.chartBounds.centerY() - AndroidUtilities.dp(3.0f)));
        this.topText.draw(canvas);
        if (this.topText.isAnimating() || z2) {
        }
        this.bottomText.setAlpha(i2);
        this.bottomText.setBounds((int) this.chartBounds.centerX(), (int) (this.chartBounds.centerY() + AndroidUtilities.dp(22.0f)), (int) this.chartBounds.centerX(), (int) (this.chartBounds.centerY() + AndroidUtilities.dp(22.0f)));
        this.bottomText.draw(canvas);
        this.bottomText.isAnimating();
        if (f9 > 0.0f) {
            if (this.completeDrawable == null) {
                StarParticlesView.Drawable drawable = new StarParticlesView.Drawable(25);
                this.completeDrawable = drawable;
                drawable.type = 100;
                drawable.roundEffect = false;
                drawable.useRotate = true;
                drawable.useBlur = false;
                drawable.checkBounds = true;
                drawable.size1 = 18;
                drawable.distributionAlgorithm = false;
                drawable.excludeRadius = AndroidUtilities.dp(80.0f);
                StarParticlesView.Drawable drawable2 = this.completeDrawable;
                drawable2.k3 = 0.7f;
                drawable2.k2 = 0.7f;
                drawable2.k1 = 0.7f;
                drawable2.init();
                float min3 = Math.min(getMeasuredHeight(), Math.min(getMeasuredWidth(), AndroidUtilities.dp(150.0f)));
                this.completeDrawable.rect.set(0.0f, 0.0f, min3, min3);
                this.completeDrawable.rect.offset((getMeasuredWidth() - this.completeDrawable.rect.width()) / 2.0f, (getMeasuredHeight() - this.completeDrawable.rect.height()) / 2.0f);
                this.completeDrawable.rect2.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                this.completeDrawable.resetPositions();
            }
            canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), 255, 31);
            this.completeDrawable.onDraw(canvas, f9);
            int i3 = (int) (f9 * 255.0f);
            this.completePaint.setAlpha(i3);
            canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight(), this.completePaint);
            canvas.restore();
            this.completePaintStroke.setStrokeWidth(f14);
            this.completePaintStroke.setAlpha(i3);
            canvas.drawCircle(this.chartBounds.centerX(), this.chartBounds.centerY(), (this.chartBounds.width() - f14) / 2.0f, this.completePaintStroke);
            RectF rectF = this.completePathBounds;
            if (rectF == null || rectF.equals(this.chartBounds)) {
                if (this.completePathBounds == null) {
                    this.completePathBounds = new RectF();
                }
                this.completePathBounds.set(this.chartBounds);
                this.completePath.rewind();
                Path path = this.completePath;
                RectF rectF2 = this.chartBounds;
                float width = rectF2.left + (rectF2.width() * 0.348f);
                RectF rectF3 = this.chartBounds;
                path.moveTo(width, rectF3.top + (rectF3.height() * 0.538f));
                Path path2 = this.completePath;
                RectF rectF4 = this.chartBounds;
                float width2 = rectF4.left + (rectF4.width() * 0.447f);
                RectF rectF5 = this.chartBounds;
                path2.lineTo(width2, rectF5.top + (rectF5.height() * 0.636f));
                Path path3 = this.completePath;
                RectF rectF6 = this.chartBounds;
                float width3 = rectF6.left + (rectF6.width() * 0.678f);
                RectF rectF7 = this.chartBounds;
                path3.lineTo(width3, rectF7.top + (rectF7.height() * 0.402f));
            }
            this.completePaintStroke.setStrokeWidth(AndroidUtilities.dp(10.0f));
            canvas.drawPath(this.completePath, this.completePaintStroke);
        }
        invalidate();
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int dp = AndroidUtilities.dp(heightDp());
        int dp2 = AndroidUtilities.dp(172.0f);
        this.chartBounds.set((size - dp2) / 2.0f, (dp - dp2) / 2.0f, (size + dp2) / 2.0f, (dp2 + dp) / 2.0f);
        this.completeGradientMatrix.reset();
        this.completeGradientMatrix.setTranslate(this.chartBounds.left, 0.0f);
        this.completeGradient.setLocalMatrix(this.completeGradientMatrix);
        StarParticlesView.Drawable drawable = this.completeDrawable;
        if (drawable != null) {
            drawable.rect.set(0.0f, 0.0f, AndroidUtilities.dp(140.0f), AndroidUtilities.dp(140.0f));
            this.completeDrawable.rect.offset((getMeasuredWidth() - this.completeDrawable.rect.width()) / 2.0f, (getMeasuredHeight() - this.completeDrawable.rect.height()) / 2.0f);
            this.completeDrawable.rect2.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            this.completeDrawable.resetPositions();
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(dp, 1073741824));
    }
}
