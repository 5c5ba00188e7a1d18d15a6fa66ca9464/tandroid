package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.Theme;

/* loaded from: classes3.dex */
public class MediaActionDrawable extends Drawable {
    private float animatedDownloadProgress;
    private boolean animatingTransition;
    private ColorFilter colorFilter;
    private int currentIcon;
    private MediaActionDrawableDelegate delegate;
    private float downloadProgress;
    private float downloadProgressAnimationStart;
    private float downloadProgressTime;
    private float downloadRadOffset;
    private LinearGradient gradientDrawable;
    private Matrix gradientMatrix;
    private boolean hasOverlayImage;
    private boolean isMini;
    private long lastAnimationTime;
    private Theme.MessageDrawable messageDrawable;
    private int nextIcon;
    private String percentString;
    private int percentStringWidth;
    private float savedTransitionProgress;
    private TextPaint textPaint = new TextPaint(1);
    public Paint paint = new Paint(1);
    private Paint backPaint = new Paint(1);
    public Paint paint2 = new Paint(1);
    private Paint paint3 = new Paint(1);
    private RectF rect = new RectF();
    private float scale = 1.0f;
    private DecelerateInterpolator interpolator = new DecelerateInterpolator();
    private float transitionAnimationTime = 400.0f;
    private int lastPercent = -1;
    private float overrideAlpha = 1.0f;
    private float transitionProgress = 1.0f;

    /* loaded from: classes3.dex */
    public interface MediaActionDrawableDelegate {
        void invalidate();
    }

    public MediaActionDrawable() {
        this.paint.setColor(-1);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setStrokeWidth(AndroidUtilities.dp(3.0f));
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint3.setColor(-1);
        this.textPaint.setTypeface(AndroidUtilities.bold());
        this.textPaint.setTextSize(AndroidUtilities.dp(13.0f));
        this.textPaint.setColor(-1);
        this.paint2.setColor(-1);
    }

    public static float getCircleValue(float f) {
        while (f > 360.0f) {
            f -= 360.0f;
        }
        return f;
    }

    public void applyShaderMatrix(boolean z) {
        Theme.MessageDrawable messageDrawable = this.messageDrawable;
        if (messageDrawable == null || !messageDrawable.hasGradient() || this.hasOverlayImage) {
            return;
        }
        android.graphics.Rect bounds = getBounds();
        Shader gradientShader = this.messageDrawable.getGradientShader();
        Matrix matrix = this.messageDrawable.getMatrix();
        matrix.reset();
        this.messageDrawable.applyMatrixScale();
        if (z) {
            matrix.postTranslate(-bounds.centerX(), (-this.messageDrawable.getTopY()) + bounds.top);
        } else {
            matrix.postTranslate(0.0f, -this.messageDrawable.getTopY());
        }
        gradientShader.setLocalMatrix(matrix);
    }

    /* JADX WARN: Code restructure failed: missing block: B:241:0x0872, code lost:
    
        if (r42.nextIcon == 1) goto L357;
     */
    /* JADX WARN: Code restructure failed: missing block: B:242:0x087b, code lost:
    
        r2 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x0878, code lost:
    
        r2 = 1.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:248:0x0876, code lost:
    
        if (r1 != 1) goto L358;
     */
    /* JADX WARN: Removed duplicated region for block: B:170:0x09d0  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x0a0a  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x0a04  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x07cd  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x07f5  */
    /* JADX WARN: Removed duplicated region for block: B:261:0x083b  */
    /* JADX WARN: Removed duplicated region for block: B:263:0x07d0  */
    /* JADX WARN: Removed duplicated region for block: B:268:0x0757  */
    /* JADX WARN: Removed duplicated region for block: B:271:0x076b  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x0780  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x07b6  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x076e  */
    /* JADX WARN: Removed duplicated region for block: B:279:0x075a  */
    /* JADX WARN: Removed duplicated region for block: B:286:0x06d6  */
    /* JADX WARN: Removed duplicated region for block: B:289:0x06f3  */
    /* JADX WARN: Removed duplicated region for block: B:292:0x0740  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x06d9  */
    /* JADX WARN: Removed duplicated region for block: B:294:0x06b2  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x069f  */
    /* JADX WARN: Removed duplicated region for block: B:302:0x0690  */
    /* JADX WARN: Removed duplicated region for block: B:419:0x04e2  */
    /* JADX WARN: Removed duplicated region for block: B:421:0x04ed  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x068b  */
    /* JADX WARN: Removed duplicated region for block: B:476:0x0349  */
    /* JADX WARN: Removed duplicated region for block: B:479:0x0358  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0699  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x06ac  */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        int save;
        float f;
        int i;
        float f2;
        float dp;
        int i2;
        android.graphics.Rect rect;
        int i3;
        int i4;
        float dp2;
        float dp3;
        float dp4;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        float f9;
        float f10;
        android.graphics.Rect rect2;
        int i5;
        int i6;
        int i7;
        float f11;
        float f12;
        float centerX;
        float centerY;
        float dp5;
        int min;
        float f13;
        float f14;
        float f15;
        float f16;
        float f17;
        float f18;
        int centerY2;
        int centerY3;
        float f19;
        int i8;
        int i9;
        int i10;
        int i11;
        float f20;
        float f21;
        Path[] pathArr;
        Path[] pathArr2;
        Path[] pathArr3;
        Path[] pathArr4;
        Drawable drawable;
        Drawable drawable2;
        float f22;
        Path[] pathArr5;
        Path[] pathArr6;
        int i12;
        int i13;
        float f23;
        int i14;
        int i15;
        int i16;
        float f24;
        int i17;
        boolean z;
        int i18;
        float f25;
        float f26;
        float f27;
        int i19;
        Path path;
        int i20;
        int i21;
        float f28 = 90.0f;
        android.graphics.Rect bounds = getBounds();
        Theme.MessageDrawable messageDrawable = this.messageDrawable;
        if (messageDrawable != null && messageDrawable.hasGradient() && !this.hasOverlayImage) {
            Shader gradientShader = this.messageDrawable.getGradientShader();
            this.paint.setShader(gradientShader);
            this.paint2.setShader(gradientShader);
            this.paint3.setShader(gradientShader);
        } else if (this.gradientDrawable == null || this.hasOverlayImage) {
            this.paint.setShader(null);
            this.paint2.setShader(null);
            this.paint3.setShader(null);
        } else {
            this.gradientMatrix.reset();
            this.gradientMatrix.setTranslate(0.0f, bounds.top);
            this.gradientDrawable.setLocalMatrix(this.gradientMatrix);
            this.paint.setShader(this.gradientDrawable);
            this.paint2.setShader(this.gradientDrawable);
            this.paint3.setShader(this.gradientDrawable);
        }
        int centerX2 = bounds.centerX();
        int centerY4 = bounds.centerY();
        int i22 = this.nextIcon;
        if (i22 == 4) {
            int i23 = this.currentIcon;
            if (i23 != 3 && i23 != 14) {
                save = canvas.save();
                f = 1.0f - this.transitionProgress;
                canvas.scale(f, f, centerX2, centerY4);
                i = save;
            }
            i = 0;
        } else {
            if ((i22 == 6 || i22 == 10) && this.currentIcon == 4) {
                save = canvas.save();
                f = this.transitionProgress;
                canvas.scale(f, f, centerX2, centerY4);
                i = save;
            }
            i = 0;
        }
        AndroidUtilities.dp(3.0f);
        if (this.currentIcon == 2 || this.nextIcon == 2) {
            applyShaderMatrix(false);
            float f29 = centerY4;
            float dp6 = f29 - (AndroidUtilities.dp(9.0f) * this.scale);
            float dp7 = (AndroidUtilities.dp(9.0f) * this.scale) + f29;
            float dp8 = (AndroidUtilities.dp(12.0f) * this.scale) + f29;
            int i24 = this.currentIcon;
            if ((i24 == 3 || i24 == 14) && this.nextIcon == 2) {
                this.paint.setAlpha((int) (Math.min(1.0f, this.transitionProgress / 0.5f) * 255.0f));
                f2 = this.transitionProgress;
                dp = (AndroidUtilities.dp(12.0f) * this.scale) + f29;
            } else {
                int i25 = this.nextIcon;
                if (i25 == 3 || i25 == 14 || i25 == 2) {
                    this.paint.setAlpha(NotificationCenter.playerDidStartPlaying);
                    f2 = this.transitionProgress;
                } else {
                    this.paint.setAlpha((int) (Math.min(1.0f, this.savedTransitionProgress / 0.5f) * 255.0f * (1.0f - this.transitionProgress)));
                    f2 = this.savedTransitionProgress;
                }
                dp = (AndroidUtilities.dp(1.0f) * this.scale) + f29;
            }
            if (this.animatingTransition) {
                int i26 = this.nextIcon;
                if (i26 == 2 || f2 <= 0.5f) {
                    i2 = centerX2;
                    rect = bounds;
                    i3 = i;
                    i4 = centerY4;
                    if (i26 == 2) {
                        f8 = 1.0f - f2;
                    } else {
                        f8 = f2 / 0.5f;
                        f2 = 1.0f - f8;
                    }
                    dp6 += (dp - dp6) * f8;
                    dp7 += (dp8 - dp7) * f8;
                    float f30 = i2;
                    dp2 = f30 - ((AndroidUtilities.dp(8.0f) * f2) * this.scale);
                    dp3 = f30 + (AndroidUtilities.dp(8.0f) * f2 * this.scale);
                    dp4 = AndroidUtilities.dp(8.0f) * f2;
                } else {
                    float dp9 = AndroidUtilities.dp(13.0f);
                    float f31 = this.scale;
                    float dp10 = (dp9 * f31 * f31) + (this.isMini ? AndroidUtilities.dp(2.0f) : 0);
                    float f32 = f2 - 0.5f;
                    float f33 = f32 / 0.5f;
                    if (f32 > 0.2f) {
                        f10 = (f32 - 0.2f) / 0.3f;
                        f9 = 1.0f;
                    } else {
                        f9 = f32 / 0.2f;
                        f10 = 0.0f;
                    }
                    float f34 = centerX2;
                    float f35 = f34 - dp10;
                    float f36 = dp10 / 2.0f;
                    this.rect.set(f35, dp8 - f36, f34, f36 + dp8);
                    float f37 = f10 * 100.0f;
                    i3 = i;
                    i4 = centerY4;
                    i2 = centerX2;
                    canvas.drawArc(this.rect, f37, (f33 * 104.0f) - f37, false, this.paint);
                    float f38 = dp + ((dp8 - dp) * f9);
                    if (f10 > 0.0f) {
                        float f39 = this.nextIcon == 14 ? 0.0f : (-45.0f) * (1.0f - f10);
                        float dp11 = AndroidUtilities.dp(7.0f) * f10 * this.scale;
                        int i27 = (int) (f10 * 255.0f);
                        int i28 = this.nextIcon;
                        if (i28 != 3 && i28 != 14 && i28 != 2) {
                            i27 = (int) (i27 * (1.0f - Math.min(1.0f, this.transitionProgress / 0.5f)));
                        }
                        int i29 = i27;
                        if (f39 != 0.0f) {
                            canvas.save();
                            canvas.rotate(f39, f34, f29);
                        }
                        if (i29 != 0) {
                            this.paint.setAlpha(i29);
                            if (this.nextIcon == 14) {
                                this.paint3.setAlpha(i29);
                                this.rect.set(i2 - AndroidUtilities.dp(3.5f), i4 - AndroidUtilities.dp(3.5f), AndroidUtilities.dp(3.5f) + i2, AndroidUtilities.dp(3.5f) + i4);
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint3);
                                this.paint.setAlpha((int) (i29 * 0.15f));
                                int dp12 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                                this.rect.set(bounds.left + dp12, bounds.top + dp12, bounds.right - dp12, bounds.bottom - dp12);
                                canvas.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                                this.paint.setAlpha(i29);
                                rect2 = bounds;
                            } else {
                                rect2 = bounds;
                                float f40 = f34 - dp11;
                                float f41 = f29 - dp11;
                                float f42 = f34 + dp11;
                                float f43 = f29 + dp11;
                                canvas.drawLine(f40, f41, f42, f43, this.paint);
                                canvas.drawLine(f42, f41, f40, f43, this.paint);
                            }
                        } else {
                            rect2 = bounds;
                        }
                        if (f39 != 0.0f) {
                            canvas.restore();
                        }
                    } else {
                        rect2 = bounds;
                    }
                    f4 = f34;
                    f6 = f38;
                    f5 = f4;
                    rect = rect2;
                    f3 = dp8;
                    if (f6 != f3) {
                        float f44 = i2;
                        canvas.drawLine(f44, f6, f44, f3, this.paint);
                    }
                    f7 = i2;
                    if (f5 != f7) {
                        float f45 = dp8;
                        float f46 = f3;
                        canvas.drawLine(f5, f45, f7, f46, this.paint);
                        canvas.drawLine(f4, f45, f7, f46, this.paint);
                    }
                }
            } else {
                i2 = centerX2;
                rect = bounds;
                i3 = i;
                i4 = centerY4;
                float f47 = i2;
                dp2 = f47 - (AndroidUtilities.dp(8.0f) * this.scale);
                dp3 = f47 + (AndroidUtilities.dp(8.0f) * this.scale);
                dp4 = AndroidUtilities.dp(8.0f);
            }
            f3 = dp7;
            f4 = dp3;
            f5 = dp2;
            dp8 = dp7 - (dp4 * this.scale);
            f6 = dp6;
            if (f6 != f3) {
            }
            f7 = i2;
            if (f5 != f7) {
            }
        } else {
            i2 = centerX2;
            rect = bounds;
            i3 = i;
            i4 = centerY4;
        }
        int i30 = this.currentIcon;
        if (i30 == 3 || i30 == 14 || (i30 == 4 && ((i21 = this.nextIcon) == 14 || i21 == 3))) {
            applyShaderMatrix(false);
            int i31 = this.nextIcon;
            if (i31 == 2) {
                float f48 = this.transitionProgress;
                if (f48 <= 0.5f) {
                    float f49 = 1.0f - (f48 / 0.5f);
                    f16 = AndroidUtilities.dp(7.0f) * f49 * this.scale;
                    i11 = (int) (f49 * 255.0f);
                } else {
                    f16 = 0.0f;
                    i11 = 0;
                }
                min = i11;
                f11 = 1.0f;
                centerY = 0.0f;
                f15 = 0.0f;
                f14 = 0.0f;
                f13 = 1.0f;
                i5 = 8;
                i6 = 9;
            } else {
                if (i31 != 15 && i31 != 0) {
                    if (i31 != 1 && i31 != 5) {
                        i5 = 8;
                        i6 = 9;
                        if (i31 != 8 && i31 != 9 && i31 != 7) {
                            i7 = 6;
                            if (i31 != 6) {
                                if (i31 == 4) {
                                    float f50 = 1.0f - this.transitionProgress;
                                    float dp13 = AndroidUtilities.dp(7.0f) * this.scale;
                                    int i32 = (int) (f50 * 255.0f);
                                    if (this.currentIcon == 14) {
                                        f15 = rect.left;
                                        centerY3 = rect.top;
                                    } else {
                                        f15 = rect.centerX();
                                        centerY3 = rect.centerY();
                                    }
                                    float f51 = centerY3;
                                    f13 = f50;
                                    f16 = dp13;
                                    f11 = 1.0f;
                                    f14 = 0.0f;
                                    min = i32;
                                    centerY = f51;
                                } else if (i31 == 14 || i31 == 3) {
                                    float f52 = this.transitionProgress;
                                    float f53 = 1.0f - f52;
                                    if (this.currentIcon == 4) {
                                        f18 = f52;
                                        f17 = 0.0f;
                                    } else {
                                        f17 = 45.0f * f53;
                                        f18 = 1.0f;
                                    }
                                    dp5 = AndroidUtilities.dp(7.0f) * this.scale;
                                    min = (int) (f52 * 255.0f);
                                    if (this.nextIcon == 14) {
                                        centerX = rect.left;
                                        centerY2 = rect.top;
                                    } else {
                                        centerX = rect.centerX();
                                        centerY2 = rect.centerY();
                                    }
                                    f13 = f18;
                                    centerY = centerY2;
                                    f14 = f17;
                                    f11 = 1.0f;
                                    float f54 = dp5;
                                    f15 = centerX;
                                    f16 = f54;
                                } else {
                                    f16 = AndroidUtilities.dp(7.0f) * this.scale;
                                    f11 = 1.0f;
                                    centerY = 0.0f;
                                    f15 = 0.0f;
                                    f14 = 0.0f;
                                    min = NotificationCenter.playerDidStartPlaying;
                                    f13 = 1.0f;
                                }
                            }
                            if (i31 == i7) {
                                f11 = 1.0f;
                                f12 = Math.min(1.0f, this.transitionProgress / 0.5f);
                            } else {
                                f11 = 1.0f;
                                f12 = this.transitionProgress;
                            }
                            float f55 = f11 - f12;
                            centerX = rect.centerX();
                            centerY = rect.centerY();
                            dp5 = AndroidUtilities.dp(7.0f) * f55 * this.scale;
                            min = (int) (Math.min(f11, f55 * 2.0f) * 255.0f);
                            f13 = f55;
                            f14 = 0.0f;
                            float f542 = dp5;
                            f15 = centerX;
                            f16 = f542;
                        }
                        i7 = 6;
                        if (i31 == i7) {
                        }
                        float f552 = f11 - f12;
                        centerX = rect.centerX();
                        centerY = rect.centerY();
                        dp5 = AndroidUtilities.dp(7.0f) * f552 * this.scale;
                        min = (int) (Math.min(f11, f552 * 2.0f) * 255.0f);
                        f13 = f552;
                        f14 = 0.0f;
                        float f5422 = dp5;
                        f15 = centerX;
                        f16 = f5422;
                    }
                }
                i5 = 8;
                i6 = 9;
                i7 = 6;
                if (i31 == i7) {
                }
                float f5522 = f11 - f12;
                centerX = rect.centerX();
                centerY = rect.centerY();
                dp5 = AndroidUtilities.dp(7.0f) * f5522 * this.scale;
                min = (int) (Math.min(f11, f5522 * 2.0f) * 255.0f);
                f13 = f5522;
                f14 = 0.0f;
                float f54222 = dp5;
                f15 = centerX;
                f16 = f54222;
            }
            if (f13 != f11) {
                canvas.save();
                canvas.scale(f13, f13, f15, centerY);
            }
            if (f14 != 0.0f) {
                canvas.save();
                canvas.rotate(f14, i2, i4);
            }
            if (min != 0) {
                float f56 = min;
                this.paint.setAlpha((int) (this.overrideAlpha * f56));
                if (this.currentIcon == 14 || this.nextIcon == 14) {
                    f19 = f14;
                    i8 = min;
                    i9 = 7;
                    this.paint3.setAlpha((int) (f56 * this.overrideAlpha));
                    this.rect.set(i2 - AndroidUtilities.dp(3.5f), i4 - AndroidUtilities.dp(3.5f), AndroidUtilities.dp(3.5f) + i2, AndroidUtilities.dp(3.5f) + i4);
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint3);
                } else {
                    float f57 = i2;
                    float f58 = f57 - f16;
                    float f59 = i4;
                    float f60 = f59 - f16;
                    float f61 = f57 + f16;
                    float f62 = f59 + f16;
                    f19 = f14;
                    i8 = min;
                    i9 = 7;
                    canvas.drawLine(f58, f60, f61, f62, this.paint);
                    canvas.drawLine(f61, f60, f58, f62, this.paint);
                }
            } else {
                f19 = f14;
                i8 = min;
                i9 = 7;
            }
            if (f19 != 0.0f) {
                canvas.restore();
            }
            if (f13 != 1.0f) {
                canvas.restore();
            }
            int i33 = this.currentIcon;
            if ((i33 == 3 || i33 == 14 || (i33 == 4 && ((i10 = this.nextIcon) == 14 || i10 == 3))) && i8 != 0) {
                float max = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                int dp14 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                this.rect.set(rect.left + dp14, rect.top + dp14, rect.right - dp14, rect.bottom - dp14);
                int i34 = this.currentIcon;
                if (i34 == 14 || (i34 == 4 && this.nextIcon == 14)) {
                    this.paint.setAlpha((int) (i8 * 0.15f * this.overrideAlpha));
                    canvas.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                    this.paint.setAlpha(i8);
                }
                canvas.drawArc(this.rect, this.downloadRadOffset, max, false, this.paint);
            }
        } else {
            if (i30 == 10 || this.nextIcon == 10 || i30 == 13) {
                int i35 = this.nextIcon;
                int i36 = (i35 == 4 || i35 == 6) ? (int) ((1.0f - this.transitionProgress) * 255.0f) : NotificationCenter.playerDidStartPlaying;
                if (i36 != 0) {
                    applyShaderMatrix(false);
                    this.paint.setAlpha((int) (i36 * this.overrideAlpha));
                    float max2 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                    int dp15 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                    this.rect.set(rect.left + dp15, rect.top + dp15, rect.right - dp15, rect.bottom - dp15);
                    canvas.drawArc(this.rect, this.downloadRadOffset, max2, false, this.paint);
                }
            }
            i9 = 7;
            i5 = 8;
            i6 = 9;
        }
        int i37 = this.currentIcon;
        if (i37 == this.nextIcon) {
            f20 = 1.0f;
            f21 = 1.0f;
        } else if (i37 == 4 || i37 == 3 || i37 == 14) {
            float f63 = this.transitionProgress;
            f20 = f63;
            f21 = 1.0f - f63;
        } else {
            float min2 = Math.min(1.0f, this.transitionProgress / 0.5f);
            f21 = Math.max(0.0f, 1.0f - (this.transitionProgress / 0.5f));
            f20 = min2;
        }
        int i38 = this.nextIcon;
        if (i38 == 15) {
            pathArr = Theme.chat_updatePath;
        } else {
            if (this.currentIcon == 15) {
                pathArr2 = Theme.chat_updatePath;
                pathArr = null;
                if (i38 != 5) {
                    pathArr = Theme.chat_filePath;
                } else if (this.currentIcon == 5) {
                    pathArr2 = Theme.chat_filePath;
                }
                pathArr3 = pathArr;
                pathArr4 = pathArr2;
                if (i38 != i9) {
                    drawable2 = Theme.chat_flameIcon;
                    drawable = null;
                } else {
                    drawable = this.currentIcon == i9 ? Theme.chat_flameIcon : null;
                    drawable2 = null;
                }
                if (i38 != i5) {
                    drawable2 = Theme.chat_gifIcon;
                } else if (this.currentIcon == i5) {
                    drawable = Theme.chat_gifIcon;
                }
                Drawable drawable3 = drawable;
                Drawable drawable4 = drawable2;
                if (this.currentIcon != i6 || i38 == i6) {
                    applyShaderMatrix(false);
                    this.paint.setAlpha(this.currentIcon != this.nextIcon ? NotificationCenter.playerDidStartPlaying : (int) (this.transitionProgress * 255.0f));
                    int dp16 = i4 + AndroidUtilities.dp(7.0f);
                    int dp17 = i2 - AndroidUtilities.dp(3.0f);
                    if (this.currentIcon != this.nextIcon) {
                        canvas.save();
                        float f64 = this.transitionProgress;
                        canvas.scale(f64, f64, i2, i4);
                    }
                    float f65 = dp17;
                    f22 = f21;
                    float f66 = dp16;
                    pathArr5 = pathArr4;
                    pathArr6 = pathArr3;
                    canvas.drawLine(dp17 - AndroidUtilities.dp(6.0f), dp16 - AndroidUtilities.dp(6.0f), f65, f66, this.paint);
                    canvas.drawLine(f65, f66, dp17 + AndroidUtilities.dp(12.0f), dp16 - AndroidUtilities.dp(12.0f), this.paint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas.restore();
                    }
                } else {
                    pathArr5 = pathArr4;
                    pathArr6 = pathArr3;
                    f22 = f21;
                }
                if (this.currentIcon != 12 || this.nextIcon == 12) {
                    applyShaderMatrix(false);
                    int i39 = this.currentIcon;
                    int i40 = this.nextIcon;
                    float f67 = i39 != i40 ? 1.0f : i40 == 13 ? this.transitionProgress : 1.0f - this.transitionProgress;
                    this.paint.setAlpha(i39 != i40 ? NotificationCenter.playerDidStartPlaying : (int) (f67 * 255.0f));
                    AndroidUtilities.dp(7.0f);
                    AndroidUtilities.dp(3.0f);
                    if (this.currentIcon != this.nextIcon) {
                        canvas.save();
                        canvas.scale(f67, f67, i2, i4);
                    }
                    float dp18 = AndroidUtilities.dp(7.0f) * this.scale;
                    float f68 = i2;
                    float f69 = f68 - dp18;
                    float f70 = i4;
                    float f71 = f70 - dp18;
                    float f72 = f68 + dp18;
                    float f73 = f70 + dp18;
                    canvas.drawLine(f69, f71, f72, f73, this.paint);
                    canvas.drawLine(f72, f71, f69, f73, this.paint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas.restore();
                    }
                }
                if (this.currentIcon != 13 || this.nextIcon == 13) {
                    applyShaderMatrix(false);
                    i12 = this.currentIcon;
                    i13 = this.nextIcon;
                    if (i12 != i13) {
                        f23 = 1.0f;
                    } else {
                        f23 = this.transitionProgress;
                        if (i13 != 13) {
                            f23 = 1.0f - f23;
                        }
                    }
                    this.textPaint.setAlpha((int) (f23 * 255.0f));
                    int dp19 = i4 + AndroidUtilities.dp(5.0f);
                    int i41 = i2 - (this.percentStringWidth / 2);
                    if (this.currentIcon != this.nextIcon) {
                        canvas.save();
                        canvas.scale(f23, f23, i2, i4);
                    }
                    i14 = (int) (this.animatedDownloadProgress * 100.0f);
                    if (this.percentString != null || i14 != this.lastPercent) {
                        this.lastPercent = i14;
                        this.percentString = String.format("%d%%", Integer.valueOf(i14));
                        this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(r1));
                    }
                    canvas.drawText(this.percentString, i41, dp19, this.textPaint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas.restore();
                    }
                }
                i15 = this.currentIcon;
                if (i15 != 0 || i15 == 1 || (i20 = this.nextIcon) == 0 || i20 == 1) {
                    if ((i15 == 0 || this.nextIcon != 1) && !(i15 == 1 && this.nextIcon == 0)) {
                        i16 = 1;
                    } else if (this.animatingTransition) {
                        f24 = this.nextIcon == 0 ? 1.0f - this.transitionProgress : this.transitionProgress;
                        i16 = 1;
                    } else {
                        i16 = 1;
                    }
                    int i42 = this.nextIcon;
                    if ((i42 == 0 || i42 == i16) && (i15 == 0 || i15 == i16)) {
                        Paint paint = this.paint2;
                        i17 = NotificationCenter.playerDidStartPlaying;
                        paint.setAlpha(NotificationCenter.playerDidStartPlaying);
                        z = true;
                    } else {
                        if (i42 == 4) {
                            this.paint2.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                        } else {
                            this.paint2.setAlpha(i15 == i42 ? NotificationCenter.playerDidStartPlaying : (int) (this.transitionProgress * 255.0f));
                        }
                        z = true;
                        i17 = NotificationCenter.playerDidStartPlaying;
                    }
                    applyShaderMatrix(z);
                    canvas.save();
                    canvas.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f24)), rect.centerY());
                    float f74 = f24 * 500.0f;
                    int i43 = this.currentIcon;
                    float f75 = i43 == 1 ? 90.0f : 0.0f;
                    if (i43 == 0 && this.nextIcon == 1) {
                        if (f74 < 384.0f) {
                            f28 = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f74 / 384.0f) * 95.0f;
                        } else if (f74 < 484.0f) {
                            f28 = 95.0f - (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f74 - 384.0f) / 100.0f) * 5.0f);
                        }
                        f74 += 100.0f;
                    } else {
                        if (i43 == 1 && this.nextIcon == 0) {
                            if (f74 < 100.0f) {
                                f28 = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f74 / 100.0f) * (-5.0f);
                            } else if (f74 < 484.0f) {
                                f28 = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f74 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                            } else {
                                f75 = 90.0f;
                            }
                        }
                        canvas.rotate(f75);
                        i18 = this.currentIcon;
                        if ((i18 != 0 && i18 != 1) || i18 == 4) {
                            canvas.scale(f20, f20);
                        }
                        Theme.playPauseAnimator.draw(canvas, this.paint2, f74);
                        canvas.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas, this.paint2, f74);
                        canvas.restore();
                    }
                    f75 = f28;
                    canvas.rotate(f75);
                    i18 = this.currentIcon;
                    if (i18 != 0) {
                        canvas.scale(f20, f20);
                        Theme.playPauseAnimator.draw(canvas, this.paint2, f74);
                        canvas.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas, this.paint2, f74);
                        canvas.restore();
                    }
                    canvas.scale(f20, f20);
                    Theme.playPauseAnimator.draw(canvas, this.paint2, f74);
                    canvas.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas, this.paint2, f74);
                    canvas.restore();
                } else {
                    i17 = NotificationCenter.playerDidStartPlaying;
                }
                if (this.currentIcon == 6 || this.nextIcon == 6) {
                    applyShaderMatrix(false);
                    if (this.currentIcon != 6) {
                        float f76 = this.transitionProgress;
                        if (f76 > 0.5f) {
                            float f77 = (f76 - 0.5f) / 0.5f;
                            f26 = 1.0f - Math.min(1.0f, f77 / 0.5f);
                            f25 = f77 > 0.5f ? (f77 - 0.5f) / 0.5f : 0.0f;
                        } else {
                            f25 = 0.0f;
                            f26 = 1.0f;
                        }
                    } else if (this.nextIcon != 6) {
                        this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                        f26 = 0.0f;
                        f27 = 1.0f;
                        int dp20 = i4 + AndroidUtilities.dp(7.0f);
                        int dp21 = i2 - AndroidUtilities.dp(3.0f);
                        if (f26 >= 1.0f) {
                            i19 = dp21;
                            canvas.drawLine(dp21 - AndroidUtilities.dp(6.0f), dp20 - AndroidUtilities.dp(6.0f), dp21 - (AndroidUtilities.dp(6.0f) * f26), dp20 - (AndroidUtilities.dp(6.0f) * f26), this.paint);
                        } else {
                            i19 = dp21;
                        }
                        if (f27 > 0.0f) {
                            float f78 = i19;
                            float f79 = dp20;
                            canvas.drawLine(f78, f79, f78 + (AndroidUtilities.dp(12.0f) * f27), f79 - (AndroidUtilities.dp(12.0f) * f27), this.paint);
                        }
                    } else {
                        f25 = 1.0f;
                        f26 = 0.0f;
                    }
                    this.paint.setAlpha(i17);
                    f27 = f25;
                    int dp202 = i4 + AndroidUtilities.dp(7.0f);
                    int dp212 = i2 - AndroidUtilities.dp(3.0f);
                    if (f26 >= 1.0f) {
                    }
                    if (f27 > 0.0f) {
                    }
                }
                if (drawable3 != null && drawable3 != drawable4) {
                    int intrinsicWidth = (int) (drawable3.getIntrinsicWidth() * f22);
                    int intrinsicHeight = (int) (drawable3.getIntrinsicHeight() * f22);
                    drawable3.setColorFilter(this.colorFilter);
                    drawable3.setAlpha(this.currentIcon == this.nextIcon ? NotificationCenter.playerDidStartPlaying : (int) ((1.0f - this.transitionProgress) * 255.0f));
                    int i44 = intrinsicWidth / 2;
                    int i45 = intrinsicHeight / 2;
                    drawable3.setBounds(i2 - i44, i4 - i45, i44 + i2, i45 + i4);
                    drawable3.draw(canvas);
                }
                if (drawable4 != null) {
                    int intrinsicWidth2 = (int) (drawable4.getIntrinsicWidth() * f20);
                    int intrinsicHeight2 = (int) (drawable4.getIntrinsicHeight() * f20);
                    drawable4.setColorFilter(this.colorFilter);
                    drawable4.setAlpha(this.currentIcon == this.nextIcon ? NotificationCenter.playerDidStartPlaying : (int) (this.transitionProgress * 255.0f));
                    int i46 = intrinsicWidth2 / 2;
                    int i47 = intrinsicHeight2 / 2;
                    drawable4.setBounds(i2 - i46, i4 - i47, i46 + i2, i47 + i4);
                    drawable4.draw(canvas);
                }
                Path[] pathArr7 = pathArr5;
                Path[] pathArr8 = pathArr6;
                if (pathArr7 != null && pathArr7 != pathArr8) {
                    int dp22 = AndroidUtilities.dp(24.0f);
                    this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                    this.paint2.setAlpha(this.currentIcon == this.nextIcon ? NotificationCenter.playerDidStartPlaying : (int) ((1.0f - this.transitionProgress) * 255.0f));
                    applyShaderMatrix(true);
                    canvas.save();
                    canvas.translate(i2, i4);
                    float f80 = f22;
                    canvas.scale(f80, f80);
                    float f81 = (-dp22) / 2;
                    canvas.translate(f81, f81);
                    Path path2 = pathArr7[0];
                    if (path2 != null) {
                        canvas.drawPath(path2, this.paint2);
                    }
                    Path path3 = pathArr7[1];
                    if (path3 != null) {
                        canvas.drawPath(path3, this.backPaint);
                    }
                    canvas.restore();
                }
                if (pathArr8 != null) {
                    int dp23 = AndroidUtilities.dp(24.0f);
                    int i48 = this.currentIcon == this.nextIcon ? NotificationCenter.playerDidStartPlaying : (int) (this.transitionProgress * 255.0f);
                    this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                    this.paint2.setAlpha(i48);
                    applyShaderMatrix(true);
                    canvas.save();
                    canvas.translate(i2, i4);
                    canvas.scale(f20, f20);
                    float f82 = (-dp23) / 2;
                    canvas.translate(f82, f82);
                    Path path4 = pathArr8[0];
                    if (path4 != null) {
                        canvas.drawPath(path4, this.paint2);
                    }
                    if (pathArr8.length >= 3 && (path = pathArr8[2]) != null) {
                        canvas.drawPath(path, this.paint);
                    }
                    Path path5 = pathArr8[1];
                    if (path5 != null) {
                        if (i48 != 255) {
                            int alpha = this.backPaint.getAlpha();
                            this.backPaint.setAlpha((int) (alpha * (i48 / 255.0f)));
                            canvas.drawPath(pathArr8[1], this.backPaint);
                            this.backPaint.setAlpha(alpha);
                        } else {
                            canvas.drawPath(path5, this.backPaint);
                        }
                    }
                    canvas.restore();
                }
                long currentTimeMillis = System.currentTimeMillis();
                long j = currentTimeMillis - this.lastAnimationTime;
                long j2 = j > 17 ? 17L : j;
                this.lastAnimationTime = currentTimeMillis;
                int i49 = this.currentIcon;
                if (i49 == 3 || i49 == 14 || ((i49 == 4 && this.nextIcon == 14) || i49 == 10 || i49 == 13)) {
                    float f83 = this.downloadRadOffset + (((float) (360 * j2)) / 2500.0f);
                    this.downloadRadOffset = f83;
                    this.downloadRadOffset = getCircleValue(f83);
                    if (this.nextIcon != 2) {
                        float f84 = this.downloadProgress;
                        float f85 = this.downloadProgressAnimationStart;
                        float f86 = f84 - f85;
                        if (f86 > 0.0f) {
                            float f87 = this.downloadProgressTime + ((float) j2);
                            this.downloadProgressTime = f87;
                            if (f87 >= 200.0f) {
                                this.animatedDownloadProgress = f84;
                                this.downloadProgressAnimationStart = f84;
                                this.downloadProgressTime = 0.0f;
                            } else {
                                this.animatedDownloadProgress = f85 + (f86 * this.interpolator.getInterpolation(f87 / 200.0f));
                            }
                        }
                    }
                    invalidateSelf();
                }
                if (this.animatingTransition) {
                    float f88 = this.transitionProgress;
                    if (f88 < 1.0f) {
                        float f89 = f88 + (((float) j2) / this.transitionAnimationTime);
                        this.transitionProgress = f89;
                        if (f89 >= 1.0f) {
                            this.currentIcon = this.nextIcon;
                            this.transitionProgress = 1.0f;
                            this.animatingTransition = false;
                        }
                        invalidateSelf();
                    }
                }
                int i50 = i3;
                if (i50 >= 1) {
                    canvas.restoreToCount(i50);
                    return;
                }
                return;
            }
            pathArr = null;
        }
        pathArr2 = null;
        if (i38 != 5) {
        }
        pathArr3 = pathArr;
        pathArr4 = pathArr2;
        if (i38 != i9) {
        }
        if (i38 != i5) {
        }
        Drawable drawable32 = drawable;
        Drawable drawable42 = drawable2;
        if (this.currentIcon != i6) {
        }
        applyShaderMatrix(false);
        this.paint.setAlpha(this.currentIcon != this.nextIcon ? NotificationCenter.playerDidStartPlaying : (int) (this.transitionProgress * 255.0f));
        int dp162 = i4 + AndroidUtilities.dp(7.0f);
        int dp172 = i2 - AndroidUtilities.dp(3.0f);
        if (this.currentIcon != this.nextIcon) {
        }
        float f652 = dp172;
        f22 = f21;
        float f662 = dp162;
        pathArr5 = pathArr4;
        pathArr6 = pathArr3;
        canvas.drawLine(dp172 - AndroidUtilities.dp(6.0f), dp162 - AndroidUtilities.dp(6.0f), f652, f662, this.paint);
        canvas.drawLine(f652, f662, dp172 + AndroidUtilities.dp(12.0f), dp162 - AndroidUtilities.dp(12.0f), this.paint);
        if (this.currentIcon != this.nextIcon) {
        }
        if (this.currentIcon != 12) {
        }
        applyShaderMatrix(false);
        int i392 = this.currentIcon;
        int i402 = this.nextIcon;
        if (i392 != i402) {
        }
        this.paint.setAlpha(i392 != i402 ? NotificationCenter.playerDidStartPlaying : (int) (f67 * 255.0f));
        AndroidUtilities.dp(7.0f);
        AndroidUtilities.dp(3.0f);
        if (this.currentIcon != this.nextIcon) {
        }
        float dp182 = AndroidUtilities.dp(7.0f) * this.scale;
        float f682 = i2;
        float f692 = f682 - dp182;
        float f702 = i4;
        float f712 = f702 - dp182;
        float f722 = f682 + dp182;
        float f732 = f702 + dp182;
        canvas.drawLine(f692, f712, f722, f732, this.paint);
        canvas.drawLine(f722, f712, f692, f732, this.paint);
        if (this.currentIcon != this.nextIcon) {
        }
        if (this.currentIcon != 13) {
        }
        applyShaderMatrix(false);
        i12 = this.currentIcon;
        i13 = this.nextIcon;
        if (i12 != i13) {
        }
        this.textPaint.setAlpha((int) (f23 * 255.0f));
        int dp192 = i4 + AndroidUtilities.dp(5.0f);
        int i412 = i2 - (this.percentStringWidth / 2);
        if (this.currentIcon != this.nextIcon) {
        }
        i14 = (int) (this.animatedDownloadProgress * 100.0f);
        if (this.percentString != null) {
        }
        this.lastPercent = i14;
        this.percentString = String.format("%d%%", Integer.valueOf(i14));
        this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(r1));
        canvas.drawText(this.percentString, i412, dp192, this.textPaint);
        if (this.currentIcon != this.nextIcon) {
        }
        i15 = this.currentIcon;
        if (i15 != 0) {
        }
        if (i15 == 0) {
        }
        i16 = 1;
    }

    public int getCurrentIcon() {
        return this.nextIcon;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(48.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(48.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumHeight() {
        return AndroidUtilities.dp(48.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumWidth() {
        return AndroidUtilities.dp(48.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    public int getPreviousIcon() {
        return this.currentIcon;
    }

    public float getProgress() {
        return this.downloadProgress;
    }

    public float getTransitionProgress() {
        if (this.animatingTransition) {
            return this.transitionProgress;
        }
        return 1.0f;
    }

    @Override // android.graphics.drawable.Drawable
    public void invalidateSelf() {
        super.invalidateSelf();
        MediaActionDrawableDelegate mediaActionDrawableDelegate = this.delegate;
        if (mediaActionDrawableDelegate != null) {
            mediaActionDrawableDelegate.invalidate();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    public void setBackColor(int i) {
        this.backPaint.setColor(i | (-16777216));
    }

    public void setBackgroundDrawable(Theme.MessageDrawable messageDrawable) {
        this.messageDrawable = messageDrawable;
    }

    public void setBackgroundGradientDrawable(LinearGradient linearGradient) {
        this.gradientDrawable = linearGradient;
        this.gradientMatrix = new Matrix();
    }

    @Override // android.graphics.drawable.Drawable
    public void setBounds(int i, int i2, int i3, int i4) {
        super.setBounds(i, i2, i3, i4);
        float intrinsicWidth = (i3 - i) / getIntrinsicWidth();
        this.scale = intrinsicWidth;
        if (intrinsicWidth < 0.7f) {
            this.paint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        }
    }

    public void setColor(int i) {
        int i2 = (-16777216) | i;
        this.paint.setColor(i2);
        this.paint2.setColor(i2);
        this.paint3.setColor(i2);
        this.textPaint.setColor(i2);
        this.colorFilter = new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
        this.paint2.setColorFilter(colorFilter);
        this.paint3.setColorFilter(colorFilter);
        this.textPaint.setColorFilter(colorFilter);
    }

    public void setDelegate(MediaActionDrawableDelegate mediaActionDrawableDelegate) {
        this.delegate = mediaActionDrawableDelegate;
    }

    public void setHasOverlayImage(boolean z) {
        this.hasOverlayImage = z;
    }

    public boolean setIcon(int i, boolean z) {
        int i2;
        int i3;
        if (this.currentIcon == i && (i3 = this.nextIcon) != i) {
            this.currentIcon = i3;
            this.transitionProgress = 1.0f;
        }
        if (z) {
            int i4 = this.currentIcon;
            if (i4 == i || (i2 = this.nextIcon) == i) {
                return false;
            }
            this.transitionAnimationTime = ((i4 == 0 && i == 1) || (i4 == 1 && i == 0)) ? 300.0f : (i4 == 2 && (i == 3 || i == 14)) ? 400.0f : (i4 == 4 || i != 6) ? ((i4 == 4 && i == 14) || (i4 == 14 && i == 4)) ? 160.0f : 220.0f : 360.0f;
            if (this.animatingTransition) {
                this.currentIcon = i2;
            }
            this.animatingTransition = true;
            this.nextIcon = i;
            this.savedTransitionProgress = this.transitionProgress;
            this.transitionProgress = 0.0f;
        } else {
            if (this.currentIcon == i) {
                return false;
            }
            this.animatingTransition = false;
            this.nextIcon = i;
            this.currentIcon = i;
            this.savedTransitionProgress = this.transitionProgress;
            this.transitionProgress = 1.0f;
        }
        if (i == 3 || i == 14) {
            this.downloadRadOffset = 112.0f;
            this.animatedDownloadProgress = 0.0f;
            this.downloadProgressAnimationStart = 0.0f;
            this.downloadProgressTime = 0.0f;
        }
        invalidateSelf();
        return true;
    }

    public void setMini(boolean z) {
        this.isMini = z;
        this.paint.setStrokeWidth(AndroidUtilities.dp(z ? 2.0f : 3.0f));
    }

    public void setOverrideAlpha(float f) {
        this.overrideAlpha = f;
    }

    public void setProgress(float f, boolean z) {
        if (this.downloadProgress == f) {
            return;
        }
        if (z) {
            if (this.animatedDownloadProgress > f) {
                this.animatedDownloadProgress = f;
            }
            this.downloadProgressAnimationStart = this.animatedDownloadProgress;
        } else {
            this.animatedDownloadProgress = f;
            this.downloadProgressAnimationStart = f;
        }
        this.downloadProgress = f;
        this.downloadProgressTime = 0.0f;
        invalidateSelf();
    }
}
