package org.telegram.ui.Components;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import java.lang.ref.WeakReference;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.GenericProvider;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
/* loaded from: classes3.dex */
public class MotionBackgroundDrawable extends Drawable {
    private static boolean errorWhileGenerateLegacyBitmap;
    private static float legacyBitmapScale;
    private static final boolean useLegacyBitmap;
    private static final boolean useSoftLight;
    private int alpha;
    private GenericProvider animationProgressProvider;
    private float backgroundAlpha;
    private int bitmapHeight;
    private BitmapShader bitmapShader;
    private int bitmapWidth;
    private int[] colors;
    private Bitmap currentBitmap;
    private boolean disableGradientShaderScaling;
    private boolean fastAnimation;
    private Canvas gradientCanvas;
    private GradientDrawable gradientDrawable;
    private Bitmap gradientFromBitmap;
    private Canvas gradientFromCanvas;
    private BitmapShader gradientShader;
    private Bitmap[] gradientToBitmap;
    private boolean ignoreInterpolator;
    private float indeterminateSpeedScale;
    private int intensity;
    private final CubicBezierInterpolator interpolator;
    private boolean invalidateLegacy;
    private boolean isIndeterminateAnimation;
    public boolean isPreview;
    private long lastUpdateTime;
    private Bitmap legacyBitmap;
    private Bitmap legacyBitmap2;
    private int legacyBitmapColor;
    private ColorFilter legacyBitmapColorFilter;
    private Canvas legacyCanvas;
    private Canvas legacyCanvas2;
    private Matrix matrix;
    private Paint overrideBitmapPaint;
    private Paint paint;
    private Paint paint2;
    private Paint paint3;
    private WeakReference parentView;
    private float patternAlpha;
    private Bitmap patternBitmap;
    private android.graphics.Rect patternBounds;
    private ColorFilter patternColorFilter;
    private int phase;
    public float posAnimationProgress;
    private boolean postInvalidateParent;
    private RectF rect;
    private boolean rotatingPreview;
    private boolean rotationBack;
    private int roundRadius;
    private int translationY;
    private Runnable updateAnimationRunnable;

    static {
        int i = Build.VERSION.SDK_INT;
        useLegacyBitmap = i < 28;
        useSoftLight = i >= 29;
        errorWhileGenerateLegacyBitmap = false;
        legacyBitmapScale = 0.7f;
    }

    public MotionBackgroundDrawable() {
        this.colors = new int[]{-12423849, -531317, -7888252, -133430};
        this.interpolator = new CubicBezierInterpolator(0.33d, 0.0d, 0.0d, 1.0d);
        this.posAnimationProgress = 1.0f;
        this.rect = new RectF();
        this.gradientToBitmap = new Bitmap[3];
        this.paint = new Paint(2);
        this.paint2 = new Paint(2);
        this.paint3 = new Paint();
        this.intensity = 100;
        this.gradientDrawable = new GradientDrawable();
        this.updateAnimationRunnable = new Runnable() { // from class: org.telegram.ui.Components.MotionBackgroundDrawable$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MotionBackgroundDrawable.this.lambda$new$0();
            }
        };
        this.patternBounds = new android.graphics.Rect();
        this.patternAlpha = 1.0f;
        this.backgroundAlpha = 1.0f;
        this.alpha = NotificationCenter.didClearDatabase;
        this.indeterminateSpeedScale = 1.0f;
        this.bitmapWidth = 60;
        this.bitmapHeight = 80;
        init();
    }

    public MotionBackgroundDrawable(int i, int i2, int i3, int i4, int i5, boolean z) {
        this(i, i2, i3, i4, i5, z, false);
    }

    public MotionBackgroundDrawable(int i, int i2, int i3, int i4, int i5, boolean z, boolean z2) {
        this.colors = new int[]{-12423849, -531317, -7888252, -133430};
        this.interpolator = new CubicBezierInterpolator(0.33d, 0.0d, 0.0d, 1.0d);
        this.posAnimationProgress = 1.0f;
        this.rect = new RectF();
        this.gradientToBitmap = new Bitmap[3];
        this.paint = new Paint(2);
        this.paint2 = new Paint(2);
        this.paint3 = new Paint();
        this.intensity = 100;
        this.gradientDrawable = new GradientDrawable();
        this.updateAnimationRunnable = new Runnable() { // from class: org.telegram.ui.Components.MotionBackgroundDrawable$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MotionBackgroundDrawable.this.lambda$new$0();
            }
        };
        this.patternBounds = new android.graphics.Rect();
        this.patternAlpha = 1.0f;
        this.backgroundAlpha = 1.0f;
        this.alpha = NotificationCenter.didClearDatabase;
        this.indeterminateSpeedScale = 1.0f;
        this.bitmapWidth = 60;
        this.bitmapHeight = 80;
        if (z2) {
            this.bitmapWidth = 80;
            this.bitmapHeight = 80;
        }
        this.isPreview = z;
        setColors(i, i2, i3, i4, i5, false);
        init();
    }

    public MotionBackgroundDrawable(int i, int i2, int i3, int i4, boolean z) {
        this(i, i2, i3, i4, 0, z);
    }

    private void createLegacyBitmap() {
        if (!useLegacyBitmap || this.intensity >= 0 || errorWhileGenerateLegacyBitmap) {
            return;
        }
        int width = (int) (this.patternBounds.width() * legacyBitmapScale);
        int height = (int) (this.patternBounds.height() * legacyBitmapScale);
        if (width <= 0 || height <= 0) {
            return;
        }
        Bitmap bitmap = this.legacyBitmap;
        if (bitmap != null && bitmap.getWidth() == width && this.legacyBitmap.getHeight() == height) {
            return;
        }
        Bitmap bitmap2 = this.legacyBitmap;
        if (bitmap2 != null) {
            bitmap2.recycle();
        }
        Bitmap bitmap3 = this.legacyBitmap2;
        if (bitmap3 != null) {
            bitmap3.recycle();
            this.legacyBitmap2 = null;
        }
        try {
            this.legacyBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            this.legacyCanvas = new Canvas(this.legacyBitmap);
            this.invalidateLegacy = true;
        } catch (Throwable th) {
            Bitmap bitmap4 = this.legacyBitmap;
            if (bitmap4 != null) {
                bitmap4.recycle();
                this.legacyBitmap = null;
            }
            FileLog.e(th);
            errorWhileGenerateLegacyBitmap = true;
            this.paint2.setXfermode(null);
        }
    }

    public static int getPatternColor(int i, int i2, int i3, int i4) {
        if (isDark(i, i2, i3, i4)) {
            if (useSoftLight) {
                return -1;
            }
            return ConnectionsManager.DEFAULT_DATACENTER_ID;
        } else if (useSoftLight) {
            return -16777216;
        } else {
            int averageColor = AndroidUtilities.getAverageColor(i3, AndroidUtilities.getAverageColor(i, i2));
            if (i4 != 0) {
                averageColor = AndroidUtilities.getAverageColor(i4, averageColor);
            }
            return (AndroidUtilities.getPatternColor(averageColor, true) & 16777215) | 1677721600;
        }
    }

    private void init() {
        BlendMode blendMode;
        this.currentBitmap = Bitmap.createBitmap(this.bitmapWidth, this.bitmapHeight, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < 3; i++) {
            this.gradientToBitmap[i] = Bitmap.createBitmap(this.bitmapWidth, this.bitmapHeight, Bitmap.Config.ARGB_8888);
        }
        this.gradientCanvas = new Canvas(this.currentBitmap);
        this.gradientFromBitmap = Bitmap.createBitmap(this.bitmapWidth, this.bitmapHeight, Bitmap.Config.ARGB_8888);
        this.gradientFromCanvas = new Canvas(this.gradientFromBitmap);
        Utilities.generateGradient(this.currentBitmap, true, this.phase, this.interpolator.getInterpolation(this.posAnimationProgress), this.currentBitmap.getWidth(), this.currentBitmap.getHeight(), this.currentBitmap.getRowBytes(), this.colors);
        if (useSoftLight) {
            Paint paint = this.paint2;
            blendMode = BlendMode.SOFT_LIGHT;
            paint.setBlendMode(blendMode);
        }
    }

    private void invalidateParent() {
        invalidateSelf();
        WeakReference weakReference = this.parentView;
        if (weakReference != null && weakReference.get() != null) {
            ((View) this.parentView.get()).invalidate();
        }
        if (this.postInvalidateParent) {
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.invalidateMotionBackground, new Object[0]);
            updateAnimation(false);
            AndroidUtilities.cancelRunOnUIThread(this.updateAnimationRunnable);
            AndroidUtilities.runOnUIThread(this.updateAnimationRunnable, 16L);
        }
    }

    public static boolean isDark(int i, int i2, int i3, int i4) {
        int averageColor = AndroidUtilities.getAverageColor(i, i2);
        if (i3 != 0) {
            averageColor = AndroidUtilities.getAverageColor(averageColor, i3);
        }
        if (i4 != 0) {
            averageColor = AndroidUtilities.getAverageColor(averageColor, i4);
        }
        return AndroidUtilities.RGBtoHSB(Color.red(averageColor), Color.green(averageColor), Color.blue(averageColor))[2] < 0.3f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        updateAnimation(true);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Paint paint;
        RectF rectF;
        Bitmap bitmap;
        float f;
        float f2;
        Bitmap bitmap2;
        android.graphics.Rect bounds = getBounds();
        canvas.save();
        float f3 = this.patternBitmap != null ? bounds.top : this.translationY;
        int width = this.currentBitmap.getWidth();
        int height = this.currentBitmap.getHeight();
        float width2 = bounds.width();
        float height2 = bounds.height();
        float f4 = width;
        float f5 = height;
        float max = Math.max(width2 / f4, height2 / f5);
        float f6 = f4 * max;
        float f7 = f5 * max;
        float f8 = (width2 - f6) / 2.0f;
        float f9 = (height2 - f7) / 2.0f;
        if (this.isPreview) {
            int i = bounds.left;
            f8 += i;
            int i2 = bounds.top;
            f9 += i2;
            canvas.clipRect(i, i2, bounds.right, bounds.bottom);
        }
        if (this.intensity >= 0) {
            if (this.roundRadius != 0) {
                this.matrix.reset();
                this.matrix.setTranslate(f8, f9);
                float min = 1.0f / Math.min(this.currentBitmap.getWidth() / bounds.width(), this.currentBitmap.getHeight() / bounds.height());
                this.matrix.preScale(min, min);
                this.bitmapShader.setLocalMatrix(this.matrix);
                this.rect.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
                RectF rectF2 = this.rect;
                float f10 = this.roundRadius;
                canvas.drawRoundRect(rectF2, f10, f10, this.paint);
            } else {
                canvas.translate(0.0f, f3);
                GradientDrawable gradientDrawable = this.gradientDrawable;
                if (gradientDrawable != null) {
                    gradientDrawable.setBounds((int) f8, (int) f9, (int) (f8 + f6), (int) (f9 + f7));
                    this.gradientDrawable.setAlpha((int) (this.backgroundAlpha * 255.0f));
                    this.gradientDrawable.draw(canvas);
                } else {
                    this.rect.set(f8, f9, f6 + f8, f7 + f9);
                    Paint paint2 = this.overrideBitmapPaint;
                    if (paint2 == null) {
                        paint2 = this.paint;
                    }
                    int alpha = paint2.getAlpha();
                    paint2.setAlpha((int) (alpha * this.backgroundAlpha));
                    canvas.drawBitmap(this.currentBitmap, (android.graphics.Rect) null, this.rect, paint2);
                    paint2.setAlpha(alpha);
                }
            }
            Bitmap bitmap3 = this.patternBitmap;
            if (bitmap3 != null) {
                float width3 = bitmap3.getWidth();
                float height3 = this.patternBitmap.getHeight();
                float max2 = Math.max(width2 / width3, height2 / height3);
                float f11 = width3 * max2;
                float f12 = height3 * max2;
                float f13 = (width2 - f11) / 2.0f;
                float f14 = (height2 - f12) / 2.0f;
                this.rect.set(f13, f14, f11 + f13, f12 + f14);
                this.paint2.setColorFilter(this.patternColorFilter);
                this.paint2.setAlpha((int) ((Math.abs(this.intensity) / 100.0f) * this.alpha * this.patternAlpha));
                bitmap = this.patternBitmap;
                rectF = this.rect;
                paint = this.paint2;
            }
            canvas.restore();
            updateAnimation(true);
        }
        canvas.drawColor(ColorUtils.setAlphaComponent(-16777216, (int) (this.alpha * this.backgroundAlpha)));
        Bitmap bitmap4 = this.patternBitmap;
        if (bitmap4 != null) {
            if (!useLegacyBitmap) {
                if (this.matrix == null) {
                    this.matrix = new Matrix();
                }
                this.matrix.reset();
                this.matrix.setTranslate(f8, f9 + f3);
                float min2 = 1.0f / Math.min(this.currentBitmap.getWidth() / bounds.width(), this.currentBitmap.getHeight() / bounds.height());
                this.matrix.preScale(min2, min2);
                this.bitmapShader.setLocalMatrix(this.matrix);
                this.matrix.reset();
                float max3 = Math.max(width2 / this.patternBitmap.getWidth(), height2 / this.patternBitmap.getHeight());
                this.matrix.setTranslate((int) ((width2 - (f * max3)) / 2.0f), (int) (((height2 - (f2 * max3)) / 2.0f) + f3));
                if (!this.disableGradientShaderScaling || max3 > 1.4f || max3 < 0.8f) {
                    this.matrix.preScale(max3, max3);
                }
                this.gradientShader.setLocalMatrix(this.matrix);
                this.paint2.setColorFilter(null);
                this.paint2.setAlpha((int) ((Math.abs(this.intensity) / 100.0f) * this.alpha * this.patternAlpha));
                this.rect.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
                RectF rectF3 = this.rect;
                float f15 = this.roundRadius;
                canvas.drawRoundRect(rectF3, f15, f15, this.paint2);
            } else if (errorWhileGenerateLegacyBitmap) {
                float width4 = bitmap4.getWidth();
                float height4 = this.patternBitmap.getHeight();
                float max4 = Math.max(width2 / width4, height2 / height4);
                float f16 = width4 * max4;
                float f17 = height4 * max4;
                float f18 = (width2 - f16) / 2.0f;
                float f19 = (height2 - f17) / 2.0f;
                this.rect.set(f18, f19, f16 + f18, f17 + f19);
                int[] iArr = this.colors;
                int averageColor = AndroidUtilities.getAverageColor(iArr[2], AndroidUtilities.getAverageColor(iArr[0], iArr[1]));
                int i3 = this.colors[3];
                if (i3 != 0) {
                    averageColor = AndroidUtilities.getAverageColor(i3, averageColor);
                }
                if (this.legacyBitmapColorFilter == null || averageColor != this.legacyBitmapColor) {
                    this.legacyBitmapColor = averageColor;
                    this.legacyBitmapColorFilter = new PorterDuffColorFilter(averageColor, PorterDuff.Mode.SRC_IN);
                }
                this.paint2.setColorFilter(this.legacyBitmapColorFilter);
                this.paint2.setAlpha((int) ((Math.abs(this.intensity) / 100.0f) * this.alpha * this.patternAlpha));
                canvas.translate(0.0f, f3);
                bitmap = this.patternBitmap;
                rectF = this.rect;
                paint = this.paint2;
            } else {
                if (this.legacyBitmap != null) {
                    if (this.invalidateLegacy) {
                        this.rect.set(0.0f, 0.0f, bitmap2.getWidth(), this.legacyBitmap.getHeight());
                        int alpha2 = this.paint.getAlpha();
                        this.paint.setAlpha(NotificationCenter.didClearDatabase);
                        this.legacyCanvas.drawBitmap(this.currentBitmap, (android.graphics.Rect) null, this.rect, this.paint);
                        this.paint.setAlpha(alpha2);
                        float width5 = this.patternBitmap.getWidth();
                        float height5 = this.patternBitmap.getHeight();
                        float max5 = Math.max(width2 / width5, height2 / height5);
                        float f20 = width5 * max5;
                        float f21 = height5 * max5;
                        float f22 = (width2 - f20) / 2.0f;
                        float f23 = (height2 - f21) / 2.0f;
                        this.rect.set(f22, f23, f20 + f22, f21 + f23);
                        this.paint2.setColorFilter(null);
                        this.paint2.setAlpha((int) ((Math.abs(this.intensity) / 100.0f) * 255.0f));
                        this.legacyCanvas.save();
                        Canvas canvas2 = this.legacyCanvas;
                        float f24 = legacyBitmapScale;
                        canvas2.scale(f24, f24);
                        this.legacyCanvas.drawBitmap(this.patternBitmap, (android.graphics.Rect) null, this.rect, this.paint2);
                        this.legacyCanvas.restore();
                        this.invalidateLegacy = false;
                    }
                    this.rect.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
                    if (this.legacyBitmap2 != null) {
                        float f25 = this.posAnimationProgress;
                        if (f25 != 1.0f) {
                            this.paint.setAlpha((int) (this.alpha * this.patternAlpha * (1.0f - f25)));
                            canvas.drawBitmap(this.legacyBitmap2, (android.graphics.Rect) null, this.rect, this.paint);
                            this.paint.setAlpha((int) (this.alpha * this.patternAlpha * this.posAnimationProgress));
                            canvas.drawBitmap(this.legacyBitmap, (android.graphics.Rect) null, this.rect, this.paint);
                            this.paint.setAlpha(this.alpha);
                        }
                    }
                    bitmap = this.legacyBitmap;
                    rectF = this.rect;
                    paint = this.paint;
                }
            }
        }
        canvas.restore();
        updateAnimation(true);
        canvas.drawBitmap(bitmap, (android.graphics.Rect) null, rectF, paint);
        canvas.restore();
        updateAnimation(true);
    }

    public void drawBackground(Canvas canvas) {
        android.graphics.Rect bounds = getBounds();
        canvas.save();
        float f = this.patternBitmap != null ? bounds.top : this.translationY;
        int width = this.currentBitmap.getWidth();
        int height = this.currentBitmap.getHeight();
        float width2 = bounds.width();
        float height2 = bounds.height();
        float f2 = width;
        float f3 = height;
        float max = Math.max(width2 / f2, height2 / f3);
        float f4 = f2 * max;
        float f5 = f3 * max;
        float f6 = (width2 - f4) / 2.0f;
        float f7 = (height2 - f5) / 2.0f;
        if (this.isPreview) {
            int i = bounds.left;
            f6 += i;
            int i2 = bounds.top;
            f7 += i2;
            canvas.clipRect(i, i2, bounds.right, bounds.bottom);
        }
        if (this.intensity < 0) {
            canvas.drawColor(ColorUtils.setAlphaComponent(-16777216, (int) (this.alpha * this.backgroundAlpha)));
        } else if (this.roundRadius != 0) {
            this.matrix.reset();
            this.matrix.setTranslate(f6, f7);
            float min = 1.0f / Math.min(this.currentBitmap.getWidth() / bounds.width(), this.currentBitmap.getHeight() / bounds.height());
            this.matrix.preScale(min, min);
            this.bitmapShader.setLocalMatrix(this.matrix);
            this.rect.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
            int alpha = this.paint.getAlpha();
            this.paint.setAlpha((int) (alpha * this.backgroundAlpha));
            RectF rectF = this.rect;
            float f8 = this.roundRadius;
            canvas.drawRoundRect(rectF, f8, f8, this.paint);
            this.paint.setAlpha(alpha);
        } else {
            canvas.translate(0.0f, f);
            GradientDrawable gradientDrawable = this.gradientDrawable;
            if (gradientDrawable != null) {
                gradientDrawable.setBounds((int) f6, (int) f7, (int) (f6 + f4), (int) (f7 + f5));
                this.gradientDrawable.setAlpha((int) (this.backgroundAlpha * 255.0f));
                this.gradientDrawable.draw(canvas);
            } else {
                this.rect.set(f6, f7, f4 + f6, f5 + f7);
                Paint paint = this.overrideBitmapPaint;
                if (paint == null) {
                    paint = this.paint;
                }
                int alpha2 = paint.getAlpha();
                paint.setAlpha((int) (alpha2 * this.backgroundAlpha));
                canvas.drawBitmap(this.currentBitmap, (android.graphics.Rect) null, this.rect, paint);
                paint.setAlpha(alpha2);
            }
        }
        canvas.restore();
        updateAnimation(true);
    }

    public void drawPattern(Canvas canvas) {
        Paint paint;
        RectF rectF;
        Bitmap bitmap;
        float f;
        float f2;
        Bitmap bitmap2;
        android.graphics.Rect bounds = getBounds();
        canvas.save();
        float f3 = this.patternBitmap != null ? bounds.top : this.translationY;
        int width = this.currentBitmap.getWidth();
        int height = this.currentBitmap.getHeight();
        float width2 = bounds.width();
        float height2 = bounds.height();
        float f4 = width;
        float f5 = height;
        float max = Math.max(width2 / f4, height2 / f5);
        float f6 = (width2 - (f4 * max)) / 2.0f;
        float f7 = (height2 - (f5 * max)) / 2.0f;
        if (this.isPreview) {
            int i = bounds.left;
            f6 += i;
            int i2 = bounds.top;
            f7 += i2;
            canvas.clipRect(i, i2, bounds.right, bounds.bottom);
        }
        if (this.intensity >= 0) {
            Bitmap bitmap3 = this.patternBitmap;
            if (bitmap3 != null) {
                float width3 = bitmap3.getWidth();
                float height3 = this.patternBitmap.getHeight();
                float max2 = Math.max(width2 / width3, height2 / height3);
                float f8 = width3 * max2;
                float f9 = height3 * max2;
                float f10 = (width2 - f8) / 2.0f;
                float f11 = (height2 - f9) / 2.0f;
                this.rect.set(f10, f11, f8 + f10, f9 + f11);
                this.paint2.setColorFilter(this.patternColorFilter);
                this.paint2.setAlpha((int) ((Math.abs(this.intensity) / 100.0f) * this.alpha * this.patternAlpha));
                bitmap = this.patternBitmap;
                rectF = this.rect;
                paint = this.paint2;
            }
            canvas.restore();
            updateAnimation(true);
        }
        Bitmap bitmap4 = this.patternBitmap;
        if (bitmap4 != null) {
            if (!useLegacyBitmap) {
                if (this.matrix == null) {
                    this.matrix = new Matrix();
                }
                this.matrix.reset();
                this.matrix.setTranslate(f6, f7 + f3);
                float min = 1.0f / Math.min(this.currentBitmap.getWidth() / bounds.width(), this.currentBitmap.getHeight() / bounds.height());
                this.matrix.preScale(min, min);
                this.bitmapShader.setLocalMatrix(this.matrix);
                this.matrix.reset();
                float max3 = Math.max(width2 / this.patternBitmap.getWidth(), height2 / this.patternBitmap.getHeight());
                this.matrix.setTranslate((int) ((width2 - (f * max3)) / 2.0f), (int) (((height2 - (f2 * max3)) / 2.0f) + f3));
                if (!this.disableGradientShaderScaling || max3 > 1.4f || max3 < 0.8f) {
                    this.matrix.preScale(max3, max3);
                }
                this.gradientShader.setLocalMatrix(this.matrix);
                this.paint2.setColorFilter(null);
                this.paint2.setAlpha((int) ((Math.abs(this.intensity) / 100.0f) * this.alpha * this.patternAlpha));
                this.rect.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
                RectF rectF2 = this.rect;
                float f12 = this.roundRadius;
                canvas.drawRoundRect(rectF2, f12, f12, this.paint2);
            } else if (errorWhileGenerateLegacyBitmap) {
                float width4 = bitmap4.getWidth();
                float height4 = this.patternBitmap.getHeight();
                float max4 = Math.max(width2 / width4, height2 / height4);
                float f13 = width4 * max4;
                float f14 = height4 * max4;
                float f15 = (width2 - f13) / 2.0f;
                float f16 = (height2 - f14) / 2.0f;
                this.rect.set(f15, f16, f13 + f15, f14 + f16);
                int[] iArr = this.colors;
                int averageColor = AndroidUtilities.getAverageColor(iArr[2], AndroidUtilities.getAverageColor(iArr[0], iArr[1]));
                int i3 = this.colors[3];
                if (i3 != 0) {
                    averageColor = AndroidUtilities.getAverageColor(i3, averageColor);
                }
                if (this.legacyBitmapColorFilter == null || averageColor != this.legacyBitmapColor) {
                    this.legacyBitmapColor = averageColor;
                    this.legacyBitmapColorFilter = new PorterDuffColorFilter(averageColor, PorterDuff.Mode.SRC_IN);
                }
                this.paint2.setColorFilter(this.legacyBitmapColorFilter);
                this.paint2.setAlpha((int) ((Math.abs(this.intensity) / 100.0f) * this.alpha * this.patternAlpha));
                canvas.translate(0.0f, f3);
                bitmap = this.patternBitmap;
                rectF = this.rect;
                paint = this.paint2;
            } else {
                if (this.legacyBitmap != null) {
                    if (this.invalidateLegacy) {
                        this.rect.set(0.0f, 0.0f, bitmap2.getWidth(), this.legacyBitmap.getHeight());
                        int alpha = this.paint.getAlpha();
                        this.paint.setAlpha(NotificationCenter.didClearDatabase);
                        this.legacyCanvas.drawBitmap(this.currentBitmap, (android.graphics.Rect) null, this.rect, this.paint);
                        this.paint.setAlpha(alpha);
                        float width5 = this.patternBitmap.getWidth();
                        float height5 = this.patternBitmap.getHeight();
                        float max5 = Math.max(width2 / width5, height2 / height5);
                        float f17 = width5 * max5;
                        float f18 = height5 * max5;
                        float f19 = (width2 - f17) / 2.0f;
                        float f20 = (height2 - f18) / 2.0f;
                        this.rect.set(f19, f20, f17 + f19, f18 + f20);
                        this.paint2.setColorFilter(null);
                        this.paint2.setAlpha((int) ((Math.abs(this.intensity) / 100.0f) * 255.0f));
                        this.legacyCanvas.save();
                        Canvas canvas2 = this.legacyCanvas;
                        float f21 = legacyBitmapScale;
                        canvas2.scale(f21, f21);
                        this.legacyCanvas.drawBitmap(this.patternBitmap, (android.graphics.Rect) null, this.rect, this.paint2);
                        this.legacyCanvas.restore();
                        this.invalidateLegacy = false;
                    }
                    this.rect.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
                    if (this.legacyBitmap2 != null) {
                        float f22 = this.posAnimationProgress;
                        if (f22 != 1.0f) {
                            this.paint.setAlpha((int) (this.alpha * this.patternAlpha * (1.0f - f22)));
                            canvas.drawBitmap(this.legacyBitmap2, (android.graphics.Rect) null, this.rect, this.paint);
                            this.paint.setAlpha((int) (this.alpha * this.patternAlpha * this.posAnimationProgress));
                            canvas.drawBitmap(this.legacyBitmap, (android.graphics.Rect) null, this.rect, this.paint);
                            this.paint.setAlpha(this.alpha);
                        }
                    }
                    bitmap = this.legacyBitmap;
                    rectF = this.rect;
                    paint = this.paint;
                }
            }
        }
        canvas.restore();
        updateAnimation(true);
        canvas.drawBitmap(bitmap, (android.graphics.Rect) null, rectF, paint);
        canvas.restore();
        updateAnimation(true);
    }

    public void generateNextGradient() {
        if (useLegacyBitmap && this.intensity < 0) {
            try {
                if (this.legacyBitmap != null) {
                    Bitmap bitmap = this.legacyBitmap2;
                    if (bitmap != null && bitmap.getHeight() == this.legacyBitmap.getHeight() && this.legacyBitmap2.getWidth() == this.legacyBitmap.getWidth()) {
                        this.legacyBitmap2.eraseColor(0);
                        this.legacyCanvas2.drawBitmap(this.legacyBitmap, 0.0f, 0.0f, (Paint) null);
                    }
                    Bitmap bitmap2 = this.legacyBitmap2;
                    if (bitmap2 != null) {
                        bitmap2.recycle();
                    }
                    this.legacyBitmap2 = Bitmap.createBitmap(this.legacyBitmap.getWidth(), this.legacyBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                    this.legacyCanvas2 = new Canvas(this.legacyBitmap2);
                    this.legacyCanvas2.drawBitmap(this.legacyBitmap, 0.0f, 0.0f, (Paint) null);
                }
            } catch (Throwable th) {
                FileLog.e(th);
                Bitmap bitmap3 = this.legacyBitmap2;
                if (bitmap3 != null) {
                    bitmap3.recycle();
                    this.legacyBitmap2 = null;
                }
            }
            Bitmap bitmap4 = this.currentBitmap;
            Utilities.generateGradient(bitmap4, true, this.phase, 1.0f, bitmap4.getWidth(), this.currentBitmap.getHeight(), this.currentBitmap.getRowBytes(), this.colors);
            this.invalidateLegacy = true;
        }
        int i = -1;
        while (i < 3) {
            int i2 = i + 1;
            Utilities.generateGradient(i < 0 ? this.gradientFromBitmap : this.gradientToBitmap[i], true, this.phase, i2 / 3.0f, this.currentBitmap.getWidth(), this.currentBitmap.getHeight(), this.currentBitmap.getRowBytes(), this.colors);
            i = i2;
        }
    }

    public Bitmap getBitmap() {
        return this.currentBitmap;
    }

    public BitmapShader getBitmapShader() {
        return this.bitmapShader;
    }

    public int[] getColors() {
        return this.colors;
    }

    public int getIntensity() {
        return this.intensity;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        Bitmap bitmap = this.patternBitmap;
        return bitmap != null ? bitmap.getHeight() : super.getIntrinsicHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        Bitmap bitmap = this.patternBitmap;
        return bitmap != null ? bitmap.getWidth() : super.getIntrinsicWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    public Bitmap getPatternBitmap() {
        return this.patternBitmap;
    }

    public int getPatternColor() {
        int[] iArr = this.colors;
        return getPatternColor(iArr[0], iArr[1], iArr[2], iArr[3]);
    }

    public int getPhase() {
        return this.phase;
    }

    public float getPosAnimationProgress() {
        return this.posAnimationProgress;
    }

    public boolean hasPattern() {
        return this.patternBitmap != null;
    }

    public boolean isIndeterminateAnimation() {
        return this.isIndeterminateAnimation;
    }

    public boolean isOneColor() {
        int[] iArr = this.colors;
        int i = iArr[0];
        return i == iArr[1] && i == iArr[2] && i == iArr[3];
    }

    public void rotatePreview(boolean z) {
        if (this.posAnimationProgress < 1.0f) {
            return;
        }
        this.rotatingPreview = true;
        this.posAnimationProgress = 0.0f;
        this.rotationBack = z;
        invalidateParent();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.alpha = i;
        this.paint.setAlpha(i);
        this.paint2.setAlpha(i);
    }

    public void setAnimationProgressProvider(GenericProvider genericProvider) {
        this.animationProgressProvider = genericProvider;
        updateAnimation(true);
    }

    public void setBackgroundAlpha(float f) {
        this.backgroundAlpha = f;
        invalidateParent();
    }

    @Override // android.graphics.drawable.Drawable
    public void setBounds(int i, int i2, int i3, int i4) {
        super.setBounds(i, i2, i3, i4);
        this.patternBounds.set(i, i2, i3, i4);
        createLegacyBitmap();
    }

    @Override // android.graphics.drawable.Drawable
    public void setBounds(android.graphics.Rect rect) {
        super.setBounds(rect);
        this.patternBounds.set(rect);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public void setColors(int i, int i2, int i3, int i4) {
        setColors(i, i2, i3, i4, 0, true);
    }

    public void setColors(int i, int i2, int i3, int i4, int i5, boolean z) {
        this.gradientDrawable = (this.isPreview && i3 == 0 && i4 == 0) ? new GradientDrawable(BackgroundGradientDrawable.getGradientOrientation(i5), new int[]{i, i2}) : null;
        int[] iArr = this.colors;
        if (iArr[0] == i && iArr[1] == i2 && iArr[2] == i3 && iArr[3] == i4) {
            return;
        }
        iArr[0] = i;
        iArr[1] = i2;
        iArr[2] = i3;
        iArr[3] = i4;
        Bitmap bitmap = this.currentBitmap;
        if (bitmap != null) {
            Utilities.generateGradient(bitmap, true, this.phase, this.interpolator.getInterpolation(this.posAnimationProgress), this.currentBitmap.getWidth(), this.currentBitmap.getHeight(), this.currentBitmap.getRowBytes(), this.colors);
            if (z) {
                invalidateParent();
            }
        }
    }

    public void setColors(int i, int i2, int i3, int i4, Bitmap bitmap) {
        int[] iArr = this.colors;
        iArr[0] = i;
        iArr[1] = i2;
        iArr[2] = i3;
        iArr[3] = i4;
        Utilities.generateGradient(bitmap, true, this.phase, this.interpolator.getInterpolation(this.posAnimationProgress), this.currentBitmap.getWidth(), this.currentBitmap.getHeight(), this.currentBitmap.getRowBytes(), this.colors);
    }

    public void setIndeterminateAnimation(boolean z) {
        if (!z && this.isIndeterminateAnimation) {
            float f = this.posAnimationProgress;
            this.posAnimationProgress = 1.0f - ((f - (((int) (f / 0.125f)) * 0.125f)) / 0.125f);
            this.ignoreInterpolator = true;
        }
        this.isIndeterminateAnimation = z;
    }

    public void setIndeterminateSpeedScale(float f) {
        this.indeterminateSpeedScale = f;
    }

    public void setParentView(View view) {
        this.parentView = new WeakReference(view);
    }

    public void setPatternAlpha(float f) {
        this.patternAlpha = f;
        invalidateParent();
    }

    public void setPatternBitmap(int i) {
        setPatternBitmap(i, this.patternBitmap, true);
    }

    public void setPatternBitmap(int i, Bitmap bitmap) {
        setPatternBitmap(i, bitmap, true);
    }

    public void setPatternBitmap(int i, Bitmap bitmap, boolean z) {
        BlendMode blendMode;
        this.intensity = i;
        this.patternBitmap = bitmap;
        this.invalidateLegacy = true;
        if (bitmap == null) {
            return;
        }
        if (useSoftLight) {
            Paint paint = this.paint2;
            if (i >= 0) {
                blendMode = BlendMode.SOFT_LIGHT;
                paint.setBlendMode(blendMode);
            } else {
                paint.setBlendMode(null);
            }
        }
        if (i < 0) {
            if (!useLegacyBitmap) {
                Bitmap bitmap2 = this.currentBitmap;
                Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                this.bitmapShader = new BitmapShader(bitmap2, tileMode, tileMode);
                Bitmap bitmap3 = this.patternBitmap;
                Shader.TileMode tileMode2 = Shader.TileMode.REPEAT;
                this.gradientShader = new BitmapShader(bitmap3, tileMode2, tileMode2);
                this.disableGradientShaderScaling = z;
                this.paint2.setShader(new ComposeShader(this.bitmapShader, this.gradientShader, PorterDuff.Mode.DST_IN));
                this.paint2.setFilterBitmap(true);
                this.matrix = new Matrix();
                return;
            }
            createLegacyBitmap();
            if (!errorWhileGenerateLegacyBitmap) {
                this.paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                return;
            }
        } else if (!useLegacyBitmap) {
            return;
        }
        this.paint2.setXfermode(null);
    }

    public void setPatternColorFilter(int i) {
        this.patternColorFilter = new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN);
        invalidateParent();
    }

    public void setPhase(int i) {
        this.phase = i;
        if (i < 0) {
            this.phase = 0;
        } else if (i > 7) {
            this.phase = 7;
        }
        Utilities.generateGradient(this.currentBitmap, true, this.phase, this.interpolator.getInterpolation(this.posAnimationProgress), this.currentBitmap.getWidth(), this.currentBitmap.getHeight(), this.currentBitmap.getRowBytes(), this.colors);
    }

    public void setPosAnimationProgress(float f) {
        this.posAnimationProgress = f;
        updateAnimation(true);
    }

    public void setPostInvalidateParent(boolean z) {
        this.postInvalidateParent = z;
    }

    public void setRoundRadius(int i) {
        this.roundRadius = i;
        this.matrix = new Matrix();
        Bitmap bitmap = this.currentBitmap;
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
        this.bitmapShader = bitmapShader;
        this.paint.setShader(bitmapShader);
        invalidateParent();
    }

    public void setTranslationY(int i) {
        this.translationY = i;
    }

    public void switchToNextPosition() {
        switchToNextPosition(false);
    }

    public void switchToNextPosition(boolean z) {
        if (this.posAnimationProgress < 1.0f || !LiteMode.isEnabled(32)) {
            return;
        }
        this.rotatingPreview = false;
        this.rotationBack = false;
        this.fastAnimation = z;
        this.posAnimationProgress = 0.0f;
        int i = this.phase - 1;
        this.phase = i;
        if (i < 0) {
            this.phase = 7;
        }
        invalidateParent();
        this.gradientFromCanvas.drawBitmap(this.currentBitmap, 0.0f, 0.0f, (Paint) null);
        generateNextGradient();
    }

    public void switchToPrevPosition(boolean z) {
        if (this.posAnimationProgress < 1.0f) {
            return;
        }
        this.rotatingPreview = false;
        this.fastAnimation = z;
        this.rotationBack = true;
        this.posAnimationProgress = 0.0f;
        invalidateParent();
        Utilities.generateGradient(this.gradientFromBitmap, true, this.phase, 0.0f, this.currentBitmap.getWidth(), this.currentBitmap.getHeight(), this.currentBitmap.getRowBytes(), this.colors);
        generateNextGradient();
    }

    /* JADX WARN: Code restructure failed: missing block: B:128:0x0182, code lost:
        if (r1 > 7) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x0184, code lost:
        r20.phase = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x0186, code lost:
        r15 = 1.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0124, code lost:
        if (r1 > 7) goto L87;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateAnimation(boolean z) {
        float interpolation;
        float f;
        Canvas canvas;
        Bitmap bitmap;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = elapsedRealtime - this.lastUpdateTime;
        if (j > 20) {
            j = 17;
        }
        this.lastUpdateTime = elapsedRealtime;
        if (j <= 1) {
            return;
        }
        boolean z2 = this.isIndeterminateAnimation;
        if (z2 && this.posAnimationProgress == 1.0f) {
            this.posAnimationProgress = 0.0f;
        }
        float f2 = this.posAnimationProgress;
        if (f2 < 1.0f) {
            boolean z3 = this.postInvalidateParent || this.rotatingPreview;
            if (z2) {
                float f3 = f2 + ((((float) j) / 12000.0f) * this.indeterminateSpeedScale);
                this.posAnimationProgress = f3;
                if (f3 >= 1.0f) {
                    this.posAnimationProgress = 0.0f;
                }
                float f4 = this.posAnimationProgress;
                int i = (int) (f4 / 0.125f);
                this.phase = i;
                f = 1.0f - ((f4 - (i * 0.125f)) / 0.125f);
                z3 = true;
            } else if (this.rotatingPreview) {
                float interpolation2 = this.interpolator.getInterpolation(f2);
                char c = interpolation2 <= 0.25f ? (char) 0 : interpolation2 <= 0.5f ? (char) 1 : interpolation2 <= 0.75f ? (char) 2 : (char) 3;
                GenericProvider genericProvider = this.animationProgressProvider;
                if (genericProvider != null) {
                    this.posAnimationProgress = ((Float) genericProvider.provide(this)).floatValue();
                } else {
                    this.posAnimationProgress += ((float) j) / (this.rotationBack ? 1000.0f : 2000.0f);
                }
                if (this.posAnimationProgress > 1.0f) {
                    this.posAnimationProgress = 1.0f;
                }
                float interpolation3 = (this.animationProgressProvider != null || this.ignoreInterpolator) ? this.posAnimationProgress : this.interpolator.getInterpolation(this.posAnimationProgress);
                if (this.ignoreInterpolator && (interpolation3 == 0.0f || interpolation3 == 1.0f)) {
                    this.ignoreInterpolator = false;
                }
                if ((c == 0 && interpolation3 > 0.25f) || ((c == 1 && interpolation3 > 0.5f) || (c == 2 && interpolation3 > 0.75f))) {
                    if (this.rotationBack) {
                        int i2 = this.phase + 1;
                        this.phase = i2;
                        if (i2 > 7) {
                            this.phase = 0;
                        }
                    } else {
                        int i3 = this.phase - 1;
                        this.phase = i3;
                        if (i3 < 0) {
                            this.phase = 7;
                        }
                    }
                }
                if (interpolation3 > 0.25f) {
                    interpolation3 = interpolation3 <= 0.5f ? interpolation3 - 0.25f : interpolation3 <= 0.75f ? interpolation3 - 0.5f : interpolation3 - 0.75f;
                }
                interpolation = interpolation3 / 0.25f;
                if (this.rotationBack) {
                    interpolation = 1.0f - interpolation;
                    if (this.posAnimationProgress >= 1.0f) {
                        int i4 = this.phase + 1;
                        this.phase = i4;
                    }
                }
                f = interpolation;
            } else {
                GenericProvider genericProvider2 = this.animationProgressProvider;
                if (genericProvider2 != null) {
                    this.posAnimationProgress = ((Float) genericProvider2.provide(this)).floatValue();
                } else {
                    this.posAnimationProgress = f2 + (((float) j) / (this.fastAnimation ? 300.0f : 500.0f));
                }
                if (this.posAnimationProgress > 1.0f) {
                    this.posAnimationProgress = 1.0f;
                }
                interpolation = (this.animationProgressProvider != null || this.ignoreInterpolator) ? this.posAnimationProgress : this.interpolator.getInterpolation(this.posAnimationProgress);
                if (this.ignoreInterpolator && (interpolation == 0.0f || interpolation == 1.0f)) {
                    this.ignoreInterpolator = false;
                }
                if (this.rotationBack) {
                    interpolation = 1.0f - interpolation;
                    if (this.posAnimationProgress >= 1.0f) {
                        int i5 = this.phase + 1;
                        this.phase = i5;
                    }
                }
                f = interpolation;
            }
            if (z3) {
                Bitmap bitmap2 = this.currentBitmap;
                Utilities.generateGradient(bitmap2, true, this.phase, f, bitmap2.getWidth(), this.currentBitmap.getHeight(), this.currentBitmap.getRowBytes(), this.colors);
                this.invalidateLegacy = true;
            } else if (!useLegacyBitmap || this.intensity >= 0) {
                if (f != 1.0f) {
                    int i6 = (int) (f / 0.33333334f);
                    this.gradientCanvas.drawBitmap(i6 == 0 ? this.gradientFromBitmap : this.gradientToBitmap[i6 - 1], 0.0f, 0.0f, (Paint) null);
                    this.paint3.setAlpha((int) (((f - (i6 * 0.33333334f)) / 0.33333334f) * 255.0f));
                    canvas = this.gradientCanvas;
                    bitmap = this.gradientToBitmap[i6];
                } else {
                    canvas = this.gradientCanvas;
                    bitmap = this.gradientToBitmap[2];
                }
                canvas.drawBitmap(bitmap, 0.0f, 0.0f, this.paint3);
            }
            if (z) {
                invalidateParent();
            }
        }
    }
}
