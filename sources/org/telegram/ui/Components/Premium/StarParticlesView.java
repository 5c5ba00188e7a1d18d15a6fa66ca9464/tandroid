package org.telegram.ui.Components.Premium;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.GLIconSettingsView;
/* loaded from: classes3.dex */
public class StarParticlesView extends View {
    private LinearGradient clipGradient;
    private Matrix clipGradientMatrix;
    private Paint clipGradientPaint;
    public boolean doNotFling;
    public Drawable drawable;
    int size;

    /* loaded from: classes3.dex */
    public static class Drawable {
        float a;
        float a1;
        float a2;
        public final int count;
        public boolean distributionAlgorithm;
        public Utilities.CallbackReturn getPaint;
        private int lastColor;
        public Paint overridePaint;
        public boolean paused;
        public long pausedTime;
        float[] points1;
        float[] points2;
        float[] points3;
        int pointsCount1;
        int pointsCount2;
        int pointsCount3;
        private long prevTime;
        public Theme.ResourcesProvider resourcesProvider;
        public boolean startFromCenter;
        public boolean useGradient;
        public boolean useRotate;
        public boolean useScale;
        public RectF rect = new RectF();
        public RectF rect2 = new RectF();
        public RectF excludeRect = new RectF();
        private final Bitmap[] stars = new Bitmap[3];
        public Paint paint = new Paint();
        public float excludeRadius = 0.0f;
        public float centerOffsetX = 0.0f;
        public float centerOffsetY = 0.0f;
        public ArrayList particles = new ArrayList();
        public float speedScale = 1.0f;
        public int size1 = 14;
        public int size2 = 12;
        public int size3 = 10;
        public float k1 = 0.85f;
        public float k2 = 0.85f;
        public float k3 = 0.9f;
        public long minLifeTime = 2000;
        public int randLifeTime = 1000;
        private final float dt = 1000.0f / AndroidUtilities.screenRefreshRate;
        Matrix matrix = new Matrix();
        Matrix matrix2 = new Matrix();
        Matrix matrix3 = new Matrix();
        public boolean checkBounds = false;
        public boolean checkTime = true;
        public boolean isCircle = true;
        public boolean useBlur = false;
        public boolean forceMaxAlpha = false;
        public boolean roundEffect = true;
        public int type = -1;
        public int colorKey = Theme.key_premiumStartSmallStarsColor;
        public final boolean[] svg = new boolean[3];
        public final boolean[] flip = new boolean[3];
        private int lastParticleI = 0;

        /* loaded from: classes3.dex */
        public class Particle {
            private int alpha;
            private float drawingX;
            private float drawingY;
            float flipProgress;
            private int i;
            float inProgress;
            public long lifeTime;
            private float randomRotate;
            private int starIndex;
            private float vecX;
            private float vecY;
            private float x;
            private float x2;
            private float y;
            private float y2;
            private float scale = 1.0f;
            private boolean first = true;

            public Particle() {
                this.i = Drawable.access$208(Drawable.this);
            }

            /* JADX WARN: Removed duplicated region for block: B:34:0x00e1  */
            /* JADX WARN: Removed duplicated region for block: B:38:0x0111  */
            /* JADX WARN: Removed duplicated region for block: B:44:0x0142  */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void draw(Canvas canvas, long j, float f) {
                float f2;
                float f3;
                Drawable drawable;
                Paint paint;
                Drawable drawable2 = Drawable.this;
                if (drawable2.useRotate) {
                    int i = this.starIndex;
                    if (i == 0) {
                        float[] fArr = drawable2.points1;
                        int i2 = drawable2.pointsCount1;
                        int i3 = i2 * 2;
                        this.drawingX = fArr[i3];
                        this.drawingY = fArr[i3 + 1];
                        drawable2.pointsCount1 = i2 + 1;
                    } else if (i == 1) {
                        float[] fArr2 = drawable2.points2;
                        int i4 = drawable2.pointsCount2;
                        int i5 = i4 * 2;
                        this.drawingX = fArr2[i5];
                        this.drawingY = fArr2[i5 + 1];
                        drawable2.pointsCount2 = i4 + 1;
                    } else if (i == 2) {
                        float[] fArr3 = drawable2.points3;
                        int i6 = drawable2.pointsCount3;
                        int i7 = i6 * 2;
                        this.drawingX = fArr3[i7];
                        this.drawingY = fArr3[i7 + 1];
                        drawable2.pointsCount3 = i6 + 1;
                    }
                } else {
                    this.drawingX = this.x;
                    this.drawingY = this.y;
                }
                if (drawable2.excludeRect.isEmpty() || !Drawable.this.excludeRect.contains(this.drawingX, this.drawingY)) {
                    canvas.save();
                    canvas.translate(this.drawingX, this.drawingY);
                    float f4 = this.randomRotate;
                    if (f4 != 0.0f) {
                        canvas.rotate(f4, Drawable.this.stars[this.starIndex].getWidth() / 2.0f, Drawable.this.stars[this.starIndex].getHeight() / 2.0f);
                    }
                    if (Drawable.this.checkTime) {
                        long j2 = this.lifeTime - j;
                        if (j2 < 200) {
                            f2 = Utilities.clamp(1.0f - (((float) j2) / 150.0f), 1.0f, 0.0f);
                            f3 = this.inProgress;
                            if (f3 >= 1.0f || GLIconSettingsView.smallStarsSize != 1.0f) {
                                float interpolation = AndroidUtilities.overshootInterpolator.getInterpolation(f3) * GLIconSettingsView.smallStarsSize;
                                canvas.scale(interpolation, interpolation, 0.0f, 0.0f);
                            }
                            drawable = Drawable.this;
                            if (drawable.flip[this.starIndex]) {
                                float min = this.flipProgress + ((drawable.dt / 1000.0f) * Math.min(Drawable.this.speedScale, 3.5f));
                                this.flipProgress = min;
                                double d = min;
                                Double.isNaN(d);
                                canvas.scale((float) Math.cos(d * 3.141592653589793d), 1.0f, 0.0f, 0.0f);
                            }
                            Drawable drawable3 = Drawable.this;
                            paint = drawable3.overridePaint;
                            if (paint == null) {
                                Utilities.CallbackReturn callbackReturn = drawable3.getPaint;
                                paint = callbackReturn != null ? (Paint) callbackReturn.run(Integer.valueOf(this.i)) : drawable3.paint;
                            }
                            float f5 = 1.0f - f2;
                            paint.setAlpha((int) (this.alpha * f5 * f));
                            Bitmap bitmap = Drawable.this.stars[this.starIndex];
                            if (Drawable.this.useScale) {
                                float f6 = this.scale * f5 * f * this.inProgress;
                                canvas.scale(f6, f6);
                            }
                            canvas.drawBitmap(bitmap, -(bitmap.getWidth() >> 1), -(bitmap.getHeight() >> 1), paint);
                            canvas.restore();
                        }
                    }
                    f2 = 0.0f;
                    f3 = this.inProgress;
                    if (f3 >= 1.0f) {
                    }
                    float interpolation2 = AndroidUtilities.overshootInterpolator.getInterpolation(f3) * GLIconSettingsView.smallStarsSize;
                    canvas.scale(interpolation2, interpolation2, 0.0f, 0.0f);
                    drawable = Drawable.this;
                    if (drawable.flip[this.starIndex]) {
                    }
                    Drawable drawable32 = Drawable.this;
                    paint = drawable32.overridePaint;
                    if (paint == null) {
                    }
                    float f52 = 1.0f - f2;
                    paint.setAlpha((int) (this.alpha * f52 * f));
                    Bitmap bitmap2 = Drawable.this.stars[this.starIndex];
                    if (Drawable.this.useScale) {
                    }
                    canvas.drawBitmap(bitmap2, -(bitmap2.getWidth() >> 1), -(bitmap2.getHeight() >> 1), paint);
                    canvas.restore();
                }
                if (Drawable.this.paused) {
                    return;
                }
                float dp = AndroidUtilities.dp(4.0f) * (Drawable.this.dt / 660.0f);
                Drawable drawable4 = Drawable.this;
                float min2 = drawable4.flip[this.starIndex] ? dp * Math.min(drawable4.speedScale, 3.5f) * 4.0f : dp * drawable4.speedScale;
                this.x += this.vecX * min2;
                this.y += this.vecY * min2;
                float f7 = this.inProgress;
                if (f7 != 1.0f) {
                    float f8 = f7 + (Drawable.this.dt / 200.0f);
                    this.inProgress = f8;
                    if (f8 > 1.0f) {
                        this.inProgress = 1.0f;
                    }
                }
            }

            /* JADX WARN: Removed duplicated region for block: B:12:0x0056  */
            /* JADX WARN: Removed duplicated region for block: B:13:0x0058  */
            /* JADX WARN: Removed duplicated region for block: B:16:0x006f  */
            /* JADX WARN: Removed duplicated region for block: B:19:0x0083  */
            /* JADX WARN: Removed duplicated region for block: B:39:0x014d  */
            /* JADX WARN: Removed duplicated region for block: B:52:0x0223  */
            /* JADX WARN: Removed duplicated region for block: B:55:0x023b  */
            /* JADX WARN: Removed duplicated region for block: B:56:0x024e  */
            /* JADX WARN: Removed duplicated region for block: B:62:0x02a2  */
            /* JADX WARN: Removed duplicated region for block: B:64:0x02b3  */
            /* JADX WARN: Removed duplicated region for block: B:87:0x02ff  */
            /* JADX WARN: Removed duplicated region for block: B:90:0x0305  */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void genPosition(long j) {
                int abs;
                Drawable drawable;
                float f;
                float abs2;
                float f2;
                Drawable drawable2;
                double atan2;
                float nextInt;
                float f3;
                int i;
                Drawable drawable3;
                int i2;
                float f4;
                float f5;
                float nextFloat;
                if (Drawable.this.type == 28) {
                    if (Utilities.fastRandom.nextFloat() < 0.13f) {
                        this.starIndex = 0;
                        Drawable drawable4 = Drawable.this;
                        this.lifeTime = j + drawable4.minLifeTime + Utilities.fastRandom.nextInt(drawable4.randLifeTime * (!drawable4.flip[this.starIndex] ? 3 : 1));
                        this.randomRotate = 0.0f;
                        if (Drawable.this.useScale) {
                            this.scale = (Utilities.fastRandom.nextFloat() * 0.6f) + 0.4f;
                        }
                        drawable = Drawable.this;
                        if (drawable.distributionAlgorithm) {
                            if (drawable.isCircle) {
                                float width = Drawable.this.rect.width();
                                float f6 = Drawable.this.excludeRadius;
                                float abs3 = ((Math.abs(Utilities.fastRandom.nextInt() % 1000) / 1000.0f) * (width - f6)) + f6;
                                float abs4 = Math.abs(Utilities.fastRandom.nextInt() % 360);
                                if (!Drawable.this.flip[this.starIndex] || this.first) {
                                    f2 = 0.0f;
                                } else {
                                    abs3 = Math.min(abs3, AndroidUtilities.dp(10.0f));
                                    f2 = AndroidUtilities.dp(30.0f) + 0.0f;
                                }
                                float centerX = Drawable.this.rect.centerX() + Drawable.this.centerOffsetX;
                                double d = abs3;
                                double d2 = abs4;
                                double sin = Math.sin(Math.toRadians(d2));
                                Double.isNaN(d);
                                this.x = centerX + ((float) (sin * d));
                                f = Drawable.this.rect.centerY() + f2 + Drawable.this.centerOffsetY;
                                double cos = Math.cos(Math.toRadians(d2));
                                Double.isNaN(d);
                                abs2 = (float) (d * cos);
                            } else {
                                this.x = drawable.rect.left + Math.abs(Utilities.fastRandom.nextInt() % Drawable.this.rect.width());
                                f = Drawable.this.rect.top;
                                abs2 = Math.abs(Utilities.fastRandom.nextInt() % Drawable.this.rect.height());
                            }
                            this.y = f + abs2;
                        } else {
                            float abs5 = drawable.rect.left + Math.abs(Utilities.fastRandom.nextInt() % Drawable.this.rect.width());
                            float abs6 = Drawable.this.rect.top + Math.abs(Utilities.fastRandom.nextInt() % Drawable.this.rect.height());
                            float f7 = 0.0f;
                            for (int i3 = 0; i3 < 10; i3++) {
                                float abs7 = Drawable.this.rect.left + Math.abs(Utilities.fastRandom.nextInt() % Drawable.this.rect.width());
                                float abs8 = Drawable.this.rect.top + Math.abs(Utilities.fastRandom.nextInt() % Drawable.this.rect.height());
                                float f8 = 2.14748365E9f;
                                for (int i4 = 0; i4 < Drawable.this.particles.size(); i4++) {
                                    Drawable drawable5 = Drawable.this;
                                    boolean z = drawable5.startFromCenter;
                                    Particle particle = (Particle) drawable5.particles.get(i4);
                                    if (z) {
                                        f4 = particle.x2 - abs7;
                                        f5 = ((Particle) Drawable.this.particles.get(i4)).y2;
                                    } else {
                                        f4 = particle.x - abs7;
                                        f5 = ((Particle) Drawable.this.particles.get(i4)).y;
                                    }
                                    float f9 = f5 - abs8;
                                    float f10 = (f4 * f4) + (f9 * f9);
                                    if (f10 < f8) {
                                        f8 = f10;
                                    }
                                }
                                if (f8 > f7) {
                                    abs5 = abs7;
                                    abs6 = abs8;
                                    f7 = f8;
                                }
                            }
                            this.x = abs5;
                            this.y = abs6;
                        }
                        if (Drawable.this.flip[this.starIndex]) {
                            this.flipProgress = Math.abs(Utilities.fastRandom.nextFloat() * 2.0f);
                        }
                        drawable2 = Drawable.this;
                        if (!drawable2.flip[this.starIndex]) {
                            atan2 = Math.toRadians(280.0f - (Utilities.fastRandom.nextFloat() * 200.0f));
                        } else if (drawable2.startFromCenter) {
                            atan2 = Utilities.fastRandom.nextDouble() * 3.141592653589793d * 2.0d;
                        } else {
                            float f11 = this.y;
                            float centerY = drawable2.rect.centerY();
                            Drawable drawable6 = Drawable.this;
                            atan2 = Math.atan2(f11 - (centerY + drawable6.centerOffsetY), this.x - (drawable6.rect.centerX() + Drawable.this.centerOffsetX));
                        }
                        this.vecX = (float) Math.cos(atan2);
                        this.vecY = (float) Math.sin(atan2);
                        if (Drawable.this.svg[this.starIndex]) {
                            nextInt = (Utilities.fastRandom.nextInt(50) + 50) / 100.0f;
                            f3 = 255.0f;
                        } else {
                            nextInt = (Utilities.fastRandom.nextInt(50) + 50) / 100.0f;
                            f3 = 120.0f;
                        }
                        this.alpha = (int) (nextInt * f3);
                        i = Drawable.this.type;
                        if ((i == 6 && ((i2 = this.starIndex) == 1 || i2 == 2)) || i == 9 || i == 3 || i == 7 || i == 24 || i == 11 || i == 22 || i == 4) {
                            this.randomRotate = (int) (((Utilities.fastRandom.nextInt() % 100) / 100.0f) * 45.0f);
                        }
                        drawable3 = Drawable.this;
                        if (drawable3.type != 101) {
                            this.inProgress = 0.0f;
                        }
                        if (drawable3.startFromCenter) {
                            float nextFloat2 = (((Utilities.fastRandom.nextFloat() * 1.2f) + 0.6f) * Math.min(Drawable.this.rect.width(), Drawable.this.rect.height())) / 2.0f;
                            float centerX2 = Drawable.this.rect.centerX() + Drawable.this.centerOffsetX + (((float) Math.cos(atan2)) * nextFloat2);
                            this.x = centerX2;
                            this.x2 = centerX2;
                            float centerY2 = Drawable.this.rect.centerY() + Drawable.this.centerOffsetY + (((float) Math.sin(atan2)) * nextFloat2);
                            this.y = centerY2;
                            this.y2 = centerY2;
                        }
                        this.first = false;
                    }
                    abs = (int) Math.floor((nextFloat * (Drawable.this.stars.length - 1)) + 1.0f);
                } else {
                    abs = Math.abs(Utilities.fastRandom.nextInt() % Drawable.this.stars.length);
                }
                this.starIndex = abs;
                Drawable drawable42 = Drawable.this;
                this.lifeTime = j + drawable42.minLifeTime + Utilities.fastRandom.nextInt(drawable42.randLifeTime * (!drawable42.flip[this.starIndex] ? 3 : 1));
                this.randomRotate = 0.0f;
                if (Drawable.this.useScale) {
                }
                drawable = Drawable.this;
                if (drawable.distributionAlgorithm) {
                }
                if (Drawable.this.flip[this.starIndex]) {
                }
                drawable2 = Drawable.this;
                if (!drawable2.flip[this.starIndex]) {
                }
                this.vecX = (float) Math.cos(atan2);
                this.vecY = (float) Math.sin(atan2);
                if (Drawable.this.svg[this.starIndex]) {
                }
                this.alpha = (int) (nextInt * f3);
                i = Drawable.this.type;
                if (i == 6) {
                    this.randomRotate = (int) (((Utilities.fastRandom.nextInt() % 100) / 100.0f) * 45.0f);
                    drawable3 = Drawable.this;
                    if (drawable3.type != 101) {
                    }
                    if (drawable3.startFromCenter) {
                    }
                    this.first = false;
                }
                this.randomRotate = (int) (((Utilities.fastRandom.nextInt() % 100) / 100.0f) * 45.0f);
                drawable3 = Drawable.this;
                if (drawable3.type != 101) {
                }
                if (drawable3.startFromCenter) {
                }
                this.first = false;
            }

            public void updatePoint() {
                int i = this.starIndex;
                if (i == 0) {
                    Drawable drawable = Drawable.this;
                    float[] fArr = drawable.points1;
                    int i2 = drawable.pointsCount1;
                    int i3 = i2 * 2;
                    fArr[i3] = this.x;
                    fArr[i3 + 1] = this.y;
                    drawable.pointsCount1 = i2 + 1;
                } else if (i == 1) {
                    Drawable drawable2 = Drawable.this;
                    float[] fArr2 = drawable2.points2;
                    int i4 = drawable2.pointsCount2;
                    int i5 = i4 * 2;
                    fArr2[i5] = this.x;
                    fArr2[i5 + 1] = this.y;
                    drawable2.pointsCount2 = i4 + 1;
                } else if (i == 2) {
                    Drawable drawable3 = Drawable.this;
                    float[] fArr3 = drawable3.points3;
                    int i6 = drawable3.pointsCount3;
                    int i7 = i6 * 2;
                    fArr3[i7] = this.x;
                    fArr3[i7 + 1] = this.y;
                    drawable3.pointsCount3 = i6 + 1;
                }
            }
        }

        public Drawable(int i) {
            this.count = i;
            this.distributionAlgorithm = i < 50;
        }

        static /* synthetic */ int access$208(Drawable drawable) {
            int i = drawable.lastParticleI;
            drawable.lastParticleI = i + 1;
            return i;
        }

        /* JADX WARN: Removed duplicated region for block: B:100:0x0266  */
        /* JADX WARN: Removed duplicated region for block: B:121:0x028e A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:77:0x01ec  */
        /* JADX WARN: Removed duplicated region for block: B:94:0x0243  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void generateBitmaps() {
            int i;
            int i2 = 0;
            while (i2 < 3) {
                float f = this.k1;
                if (i2 == 0) {
                    i = this.size1;
                } else if (i2 == 1) {
                    f = this.k2;
                    i = this.size2;
                } else {
                    f = this.k3;
                    i = this.size3;
                }
                int dp = AndroidUtilities.dp(i);
                int i3 = this.type;
                if (i3 == 9) {
                    this.stars[i2] = SvgHelper.getBitmap(i2 == 0 ? R.raw.premium_object_folder : i2 == 1 ? R.raw.premium_object_bubble : R.raw.premium_object_settings, dp, dp, ColorUtils.setAlphaComponent(Theme.getColor(this.colorKey, this.resourcesProvider), 30));
                    this.svg[i2] = true;
                } else if (i3 == 11 || i3 == 4) {
                    this.stars[i2] = SvgHelper.getBitmap(i2 == 0 ? R.raw.premium_object_smile1 : i2 == 1 ? R.raw.premium_object_smile2 : R.raw.premium_object_like, dp, dp, ColorUtils.setAlphaComponent(Theme.getColor(this.colorKey, this.resourcesProvider), 30));
                    this.svg[i2] = true;
                } else if (i3 == 22) {
                    this.stars[i2] = SvgHelper.getBitmap(i2 == 0 ? R.raw.premium_object_user : i2 == 1 ? R.raw.cache_photos : R.raw.cache_profile_photos, dp, dp, ColorUtils.setAlphaComponent(Theme.getColor(this.colorKey, this.resourcesProvider), 30));
                    this.svg[i2] = true;
                } else if (i3 == 3) {
                    this.stars[i2] = SvgHelper.getBitmap(i2 == 0 ? R.raw.premium_object_adsbubble : i2 == 1 ? R.raw.premium_object_like : R.raw.premium_object_noads, dp, dp, ColorUtils.setAlphaComponent(Theme.getColor(this.colorKey, this.resourcesProvider), 30));
                    this.svg[i2] = true;
                } else if (i3 == 7) {
                    this.stars[i2] = SvgHelper.getBitmap(i2 == 0 ? R.raw.premium_object_video2 : i2 == 1 ? R.raw.premium_object_video : R.raw.premium_object_user, dp, dp, ColorUtils.setAlphaComponent(Theme.getColor(this.colorKey, this.resourcesProvider), 30));
                    this.svg[i2] = true;
                } else if (i3 == 1001) {
                    this.stars[i2] = SvgHelper.getBitmap(R.raw.premium_object_fire, dp, dp, ColorUtils.setAlphaComponent(Theme.getColor(this.colorKey, this.resourcesProvider), 30));
                    this.svg[i2] = true;
                } else if (i3 == 1002) {
                    this.stars[i2] = SvgHelper.getBitmap(R.raw.premium_object_star2, dp, dp, ColorUtils.setAlphaComponent(Theme.getColor(this.colorKey, this.resourcesProvider), 30));
                    this.svg[i2] = true;
                } else if (i3 == 24) {
                    this.stars[i2] = SvgHelper.getBitmap(i2 == 0 ? R.raw.premium_object_tag : i2 == 1 ? R.raw.premium_object_check : R.raw.premium_object_star, dp, dp, ColorUtils.setAlphaComponent(Theme.getColor(this.colorKey, this.resourcesProvider), 30));
                    this.svg[i2] = true;
                } else if (i3 == 28) {
                    if (i2 == 0) {
                        this.stars[i2] = SvgHelper.getBitmap(R.raw.filled_premium_dollar, dp, dp, ColorUtils.setAlphaComponent(Theme.getColor(this.colorKey, this.resourcesProvider), NotificationCenter.didClearDatabase));
                        this.flip[i2] = true;
                    }
                    Bitmap createBitmap = Bitmap.createBitmap(dp, dp, Bitmap.Config.ARGB_8888);
                    this.stars[i2] = createBitmap;
                    Canvas canvas = new Canvas(createBitmap);
                    if (this.type == 6 || !(i2 == 1 || i2 == 2)) {
                        Path path = new Path();
                        float f2 = dp >> 1;
                        int i4 = (int) (f * f2);
                        path.moveTo(0.0f, f2);
                        float f3 = i4;
                        path.lineTo(f3, f3);
                        path.lineTo(f2, 0.0f);
                        float f4 = dp - i4;
                        path.lineTo(f4, f3);
                        float f5 = dp;
                        path.lineTo(f5, f2);
                        path.lineTo(f4, f4);
                        path.lineTo(f2, f5);
                        path.lineTo(f3, f4);
                        path.lineTo(0.0f, f2);
                        path.close();
                        Paint paint = new Paint();
                        if (this.useGradient) {
                            paint.setColor(getPathColor(i2));
                            if (this.roundEffect) {
                                paint.setPathEffect(new CornerPathEffect(AndroidUtilities.dpf2(this.size1 / 5.0f)));
                            }
                            canvas.drawPath(path, paint);
                        } else {
                            PremiumGradient.getInstance().updateMainGradientMatrix(0, 0, dp, dp, dp >= AndroidUtilities.dp(10.0f) ? dp * (-2) : dp * (-4), 0.0f);
                            Paint mainGradientPaint = PremiumGradient.getInstance().getMainGradientPaint();
                            if (this.roundEffect) {
                                mainGradientPaint.setPathEffect(new CornerPathEffect(AndroidUtilities.dpf2(this.size1 / 5.0f)));
                            }
                            if (this.forceMaxAlpha) {
                                mainGradientPaint.setAlpha(NotificationCenter.didClearDatabase);
                            } else {
                                mainGradientPaint.setAlpha(this.useBlur ? 60 : 120);
                            }
                            canvas.drawPath(path, mainGradientPaint);
                            mainGradientPaint.setPathEffect(null);
                            mainGradientPaint.setAlpha(NotificationCenter.didClearDatabase);
                        }
                        if (!this.useBlur) {
                            Utilities.stackBlurBitmap(createBitmap, 2);
                        }
                    } else {
                        android.graphics.drawable.Drawable drawable = ContextCompat.getDrawable(ApplicationLoader.applicationContext, R.drawable.msg_premium_liststar);
                        drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(this.colorKey, this.resourcesProvider), PorterDuff.Mode.MULTIPLY));
                        drawable.setBounds(0, 0, dp, dp);
                        drawable.draw(canvas);
                    }
                } else {
                    if (i3 == 105 && i2 == 0) {
                        this.stars[i2] = SvgHelper.getBitmap(R.raw.premium_object_star2, dp, dp, getPathColor(i2));
                    }
                    Bitmap createBitmap2 = Bitmap.createBitmap(dp, dp, Bitmap.Config.ARGB_8888);
                    this.stars[i2] = createBitmap2;
                    Canvas canvas2 = new Canvas(createBitmap2);
                    if (this.type == 6) {
                    }
                    Path path2 = new Path();
                    float f22 = dp >> 1;
                    int i42 = (int) (f * f22);
                    path2.moveTo(0.0f, f22);
                    float f32 = i42;
                    path2.lineTo(f32, f32);
                    path2.lineTo(f22, 0.0f);
                    float f42 = dp - i42;
                    path2.lineTo(f42, f32);
                    float f52 = dp;
                    path2.lineTo(f52, f22);
                    path2.lineTo(f42, f42);
                    path2.lineTo(f22, f52);
                    path2.lineTo(f32, f42);
                    path2.lineTo(0.0f, f22);
                    path2.close();
                    Paint paint2 = new Paint();
                    if (this.useGradient) {
                    }
                    if (!this.useBlur) {
                    }
                }
                i2++;
            }
        }

        protected int getPathColor(int i) {
            return this.type == 100 ? ColorUtils.setAlphaComponent(Theme.getColor(this.colorKey, this.resourcesProvider), NotificationCenter.storyQualityUpdate) : Theme.getColor(this.colorKey, this.resourcesProvider);
        }

        public void init() {
            if (this.useRotate) {
                int i = this.count * 2;
                this.points1 = new float[i];
                this.points2 = new float[i];
                this.points3 = new float[i];
            }
            generateBitmaps();
            if (this.particles.isEmpty()) {
                for (int i2 = 0; i2 < this.count; i2++) {
                    this.particles.add(new Particle());
                }
            }
        }

        public void onDraw(Canvas canvas) {
            onDraw(canvas, 1.0f);
        }

        public void onDraw(Canvas canvas, float f) {
            long currentTimeMillis = System.currentTimeMillis();
            long clamp = MathUtils.clamp(currentTimeMillis - this.prevTime, 4L, 50L);
            if (this.useRotate) {
                this.matrix.reset();
                float f2 = (float) clamp;
                float f3 = this.a + ((f2 / 40000.0f) * 360.0f);
                this.a = f3;
                this.a1 += (f2 / 50000.0f) * 360.0f;
                this.a2 += (f2 / 60000.0f) * 360.0f;
                this.matrix.setRotate(f3, this.rect.centerX() + this.centerOffsetX, this.rect.centerY() + this.centerOffsetY);
                this.matrix2.setRotate(this.a1, this.rect.centerX() + this.centerOffsetX, this.rect.centerY() + this.centerOffsetY);
                this.matrix3.setRotate(this.a2, this.rect.centerX() + this.centerOffsetX, this.rect.centerY() + this.centerOffsetY);
                this.pointsCount1 = 0;
                this.pointsCount2 = 0;
                this.pointsCount3 = 0;
                for (int i = 0; i < this.particles.size(); i++) {
                    ((Particle) this.particles.get(i)).updatePoint();
                }
                Matrix matrix = this.matrix;
                float[] fArr = this.points1;
                matrix.mapPoints(fArr, 0, fArr, 0, this.pointsCount1);
                Matrix matrix2 = this.matrix2;
                float[] fArr2 = this.points2;
                matrix2.mapPoints(fArr2, 0, fArr2, 0, this.pointsCount2);
                Matrix matrix3 = this.matrix3;
                float[] fArr3 = this.points3;
                matrix3.mapPoints(fArr3, 0, fArr3, 0, this.pointsCount3);
                this.pointsCount1 = 0;
                this.pointsCount2 = 0;
                this.pointsCount3 = 0;
            }
            for (int i2 = 0; i2 < this.particles.size(); i2++) {
                Particle particle = (Particle) this.particles.get(i2);
                if (this.paused) {
                    particle.draw(canvas, this.pausedTime, f);
                } else {
                    particle.draw(canvas, currentTimeMillis, f);
                }
                if (this.checkTime && currentTimeMillis > particle.lifeTime) {
                    particle.genPosition(currentTimeMillis);
                }
                if (this.checkBounds && !this.rect2.contains(particle.drawingX, particle.drawingY)) {
                    particle.genPosition(currentTimeMillis);
                }
            }
            this.prevTime = currentTimeMillis;
        }

        public void resetPositions() {
            long currentTimeMillis = System.currentTimeMillis();
            for (int i = 0; i < this.particles.size(); i++) {
                ((Particle) this.particles.get(i)).genPosition(currentTimeMillis);
            }
        }

        public void updateColors() {
            int color = Theme.getColor(this.colorKey, this.resourcesProvider);
            if (this.lastColor != color) {
                this.lastColor = color;
                generateBitmaps();
            }
        }
    }

    public StarParticlesView(Context context) {
        this(context, SharedConfig.getDevicePerformanceClass() == 2 ? NotificationCenter.storyQualityUpdate : SharedConfig.getDevicePerformanceClass() == 1 ? 100 : 50);
    }

    public StarParticlesView(Context context, int i) {
        super(context);
        this.drawable = new Drawable(i);
        configure();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$flingParticles$0(ValueAnimator valueAnimator) {
        this.drawable.speedScale = ((Float) valueAnimator.getAnimatedValue()).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void configure() {
        Drawable drawable = this.drawable;
        drawable.type = 100;
        drawable.roundEffect = true;
        drawable.useRotate = true;
        drawable.useBlur = true;
        drawable.checkBounds = true;
        drawable.size1 = 4;
        drawable.k3 = 0.98f;
        drawable.k2 = 0.98f;
        drawable.k1 = 0.98f;
        drawable.init();
    }

    public void flingParticles(float f) {
        if (this.doNotFling) {
            return;
        }
        float f2 = f < 60.0f ? 5.0f : f < 180.0f ? 9.0f : 15.0f;
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.Premium.StarParticlesView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                StarParticlesView.this.lambda$flingParticles$0(valueAnimator);
            }
        };
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, f2);
        ofFloat.addUpdateListener(animatorUpdateListener);
        ofFloat.setDuration(600L);
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(f2, 1.0f);
        ofFloat2.addUpdateListener(animatorUpdateListener);
        ofFloat2.setDuration(2000L);
        animatorSet.playTogether(ofFloat, ofFloat2);
        animatorSet.start();
    }

    protected int getStarsRectWidth() {
        return AndroidUtilities.dp(140.0f);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.clipGradientPaint != null) {
            canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), NotificationCenter.didClearDatabase, 31);
        }
        this.drawable.onDraw(canvas);
        if (this.clipGradientPaint != null) {
            canvas.save();
            this.clipGradientMatrix.reset();
            this.clipGradientMatrix.postTranslate(0.0f, (getHeight() + 1) - AndroidUtilities.dp(12.0f));
            this.clipGradient.setLocalMatrix(this.clipGradientMatrix);
            canvas.drawRect(0.0f, getHeight() - AndroidUtilities.dp(12.0f), getWidth(), getHeight(), this.clipGradientPaint);
            this.clipGradientMatrix.reset();
            this.clipGradientMatrix.postRotate(180.0f);
            this.clipGradientMatrix.postTranslate(0.0f, AndroidUtilities.dp(12.0f));
            this.clipGradient.setLocalMatrix(this.clipGradientMatrix);
            canvas.drawRect(0.0f, 0.0f, getWidth(), AndroidUtilities.dp(12.0f), this.clipGradientPaint);
            canvas.restore();
            canvas.restore();
        }
        if (this.drawable.paused) {
            return;
        }
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int measuredWidth = getMeasuredWidth() << (getMeasuredHeight() + 16);
        this.drawable.rect.set(0.0f, 0.0f, getStarsRectWidth(), AndroidUtilities.dp(140.0f));
        this.drawable.rect.offset((getMeasuredWidth() - this.drawable.rect.width()) / 2.0f, (getMeasuredHeight() - this.drawable.rect.height()) / 2.0f);
        this.drawable.rect2.set(-AndroidUtilities.dp(15.0f), -AndroidUtilities.dp(15.0f), getMeasuredWidth() + AndroidUtilities.dp(15.0f), getMeasuredHeight() + AndroidUtilities.dp(15.0f));
        if (this.size != measuredWidth) {
            this.size = measuredWidth;
            this.drawable.resetPositions();
        }
    }

    public void setClipWithGradient() {
        Paint paint = new Paint(1);
        this.clipGradientPaint = paint;
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, AndroidUtilities.dp(12.0f), new int[]{16777215, -1}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
        this.clipGradient = linearGradient;
        this.clipGradientPaint.setShader(linearGradient);
        this.clipGradientMatrix = new Matrix();
    }

    public void setPaused(boolean z) {
        Drawable drawable = this.drawable;
        if (z == drawable.paused) {
            return;
        }
        drawable.paused = z;
        if (z) {
            drawable.pausedTime = System.currentTimeMillis();
            return;
        }
        for (int i = 0; i < this.drawable.particles.size(); i++) {
            ((Drawable.Particle) this.drawable.particles.get(i)).lifeTime += System.currentTimeMillis() - this.drawable.pausedTime;
        }
        invalidate();
    }
}
