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

    public static float getCircleValue(float f) {
        while (f > 360.0f) {
            f -= 360.0f;
        }
        return f;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
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

    public void setOverrideAlpha(float f) {
        this.overrideAlpha = f;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
        this.paint2.setColorFilter(colorFilter);
        this.paint3.setColorFilter(colorFilter);
        this.textPaint.setColorFilter(colorFilter);
    }

    public void setColor(int i) {
        int i2 = (-16777216) | i;
        this.paint.setColor(i2);
        this.paint2.setColor(i2);
        this.paint3.setColor(i2);
        this.textPaint.setColor(i2);
        this.colorFilter = new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY);
    }

    public void setBackColor(int i) {
        this.backPaint.setColor(i | (-16777216));
    }

    public void setMini(boolean z) {
        this.isMini = z;
        this.paint.setStrokeWidth(AndroidUtilities.dp(z ? 2.0f : 3.0f));
    }

    public void setDelegate(MediaActionDrawableDelegate mediaActionDrawableDelegate) {
        this.delegate = mediaActionDrawableDelegate;
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
            if ((i4 == 0 && i == 1) || (i4 == 1 && i == 0)) {
                this.transitionAnimationTime = 300.0f;
            } else if (i4 == 2 && (i == 3 || i == 14)) {
                this.transitionAnimationTime = 400.0f;
            } else if (i4 != 4 && i == 6) {
                this.transitionAnimationTime = 360.0f;
            } else if ((i4 == 4 && i == 14) || (i4 == 14 && i == 4)) {
                this.transitionAnimationTime = 160.0f;
            } else {
                this.transitionAnimationTime = 220.0f;
            }
            if (this.animatingTransition) {
                this.currentIcon = i2;
            }
            this.animatingTransition = true;
            this.nextIcon = i;
            this.savedTransitionProgress = this.transitionProgress;
            this.transitionProgress = 0.0f;
        } else if (this.currentIcon == i) {
            return false;
        } else {
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

    public int getCurrentIcon() {
        return this.nextIcon;
    }

    public int getPreviousIcon() {
        return this.currentIcon;
    }

    public void setProgress(float f, boolean z) {
        if (this.downloadProgress == f) {
            return;
        }
        if (!z) {
            this.animatedDownloadProgress = f;
            this.downloadProgressAnimationStart = f;
        } else {
            if (this.animatedDownloadProgress > f) {
                this.animatedDownloadProgress = f;
            }
            this.downloadProgressAnimationStart = this.animatedDownloadProgress;
        }
        this.downloadProgress = f;
        this.downloadProgressTime = 0.0f;
        invalidateSelf();
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

    public void setBackgroundDrawable(Theme.MessageDrawable messageDrawable) {
        this.messageDrawable = messageDrawable;
    }

    public void setBackgroundGradientDrawable(LinearGradient linearGradient) {
        this.gradientDrawable = linearGradient;
        this.gradientMatrix = new Matrix();
    }

    public void setHasOverlayImage(boolean z) {
        this.hasOverlayImage = z;
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

    @Override // android.graphics.drawable.Drawable
    public void invalidateSelf() {
        super.invalidateSelf();
        MediaActionDrawableDelegate mediaActionDrawableDelegate = this.delegate;
        if (mediaActionDrawableDelegate != null) {
            mediaActionDrawableDelegate.invalidate();
        }
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

    /* JADX WARN: Code restructure failed: missing block: B:352:0x087c, code lost:
        if (r42.nextIcon == 1) goto L246;
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x0880, code lost:
        if (r1 != 1) goto L245;
     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x0882, code lost:
        r2 = 1.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x0885, code lost:
        r2 = 0.0f;
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0351  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0360  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x04ea  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x04f5  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x0693  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x0698  */
    /* JADX WARN: Removed duplicated region for block: B:261:0x06a1  */
    /* JADX WARN: Removed duplicated region for block: B:262:0x06a7  */
    /* JADX WARN: Removed duplicated region for block: B:268:0x06b4  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x06ba  */
    /* JADX WARN: Removed duplicated region for block: B:280:0x06de  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x06e1  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x06fb  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x0748  */
    /* JADX WARN: Removed duplicated region for block: B:294:0x075f  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x0762  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x0773  */
    /* JADX WARN: Removed duplicated region for block: B:302:0x0776  */
    /* JADX WARN: Removed duplicated region for block: B:305:0x0788  */
    /* JADX WARN: Removed duplicated region for block: B:308:0x07be  */
    /* JADX WARN: Removed duplicated region for block: B:315:0x07d5  */
    /* JADX WARN: Removed duplicated region for block: B:316:0x07d8  */
    /* JADX WARN: Removed duplicated region for block: B:321:0x07ff  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x0845  */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        int save;
        int i;
        float f;
        float dp;
        int i2;
        android.graphics.Rect rect;
        int i3;
        int i4;
        float dp2;
        float dp3;
        float dp4;
        float f2;
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
        float f24;
        int i16;
        String format;
        int i17;
        int i18;
        float f25;
        int i19;
        boolean z;
        int i20;
        float f26;
        float f27;
        int i21;
        float f28;
        int i22;
        float f29;
        Path path;
        int i23;
        int i24;
        float f30 = 90.0f;
        android.graphics.Rect bounds = getBounds();
        Theme.MessageDrawable messageDrawable = this.messageDrawable;
        if (messageDrawable != null && messageDrawable.hasGradient() && !this.hasOverlayImage) {
            Shader gradientShader = this.messageDrawable.getGradientShader();
            this.paint.setShader(gradientShader);
            this.paint2.setShader(gradientShader);
            this.paint3.setShader(gradientShader);
        } else if (this.gradientDrawable != null && !this.hasOverlayImage) {
            this.gradientMatrix.reset();
            this.gradientMatrix.setTranslate(0.0f, bounds.top);
            this.gradientDrawable.setLocalMatrix(this.gradientMatrix);
            this.paint.setShader(this.gradientDrawable);
            this.paint2.setShader(this.gradientDrawable);
            this.paint3.setShader(this.gradientDrawable);
        } else {
            this.paint.setShader(null);
            this.paint2.setShader(null);
            this.paint3.setShader(null);
        }
        int centerX2 = bounds.centerX();
        int centerY4 = bounds.centerY();
        int i25 = this.nextIcon;
        if (i25 == 4) {
            int i26 = this.currentIcon;
            if (i26 != 3 && i26 != 14) {
                save = canvas.save();
                float f31 = 1.0f - this.transitionProgress;
                canvas.scale(f31, f31, centerX2, centerY4);
                i = save;
            }
            i = 0;
        } else {
            if ((i25 == 6 || i25 == 10) && this.currentIcon == 4) {
                save = canvas.save();
                float f32 = this.transitionProgress;
                canvas.scale(f32, f32, centerX2, centerY4);
                i = save;
            }
            i = 0;
        }
        AndroidUtilities.dp(3.0f);
        if (this.currentIcon == 2 || this.nextIcon == 2) {
            applyShaderMatrix(false);
            float f33 = centerY4;
            float dp6 = f33 - (AndroidUtilities.dp(9.0f) * this.scale);
            float dp7 = (AndroidUtilities.dp(9.0f) * this.scale) + f33;
            float dp8 = (AndroidUtilities.dp(12.0f) * this.scale) + f33;
            int i27 = this.currentIcon;
            if ((i27 == 3 || i27 == 14) && this.nextIcon == 2) {
                this.paint.setAlpha((int) (Math.min(1.0f, this.transitionProgress / 0.5f) * 255.0f));
                f = this.transitionProgress;
                dp = (AndroidUtilities.dp(12.0f) * this.scale) + f33;
            } else {
                int i28 = this.nextIcon;
                if (i28 != 3 && i28 != 14 && i28 != 2) {
                    this.paint.setAlpha((int) (Math.min(1.0f, this.savedTransitionProgress / 0.5f) * 255.0f * (1.0f - this.transitionProgress)));
                    f = this.savedTransitionProgress;
                } else {
                    this.paint.setAlpha(NotificationCenter.voipServiceCreated);
                    f = this.transitionProgress;
                }
                dp = (AndroidUtilities.dp(1.0f) * this.scale) + f33;
            }
            if (this.animatingTransition) {
                int i29 = this.nextIcon;
                if (i29 == 2 || f <= 0.5f) {
                    i2 = centerX2;
                    rect = bounds;
                    i3 = i;
                    i4 = centerY4;
                    if (i29 == 2) {
                        f8 = 1.0f - f;
                    } else {
                        f8 = f / 0.5f;
                        f = 1.0f - f8;
                    }
                    dp6 += (dp - dp6) * f8;
                    dp7 += (dp8 - dp7) * f8;
                    float f34 = i2;
                    dp2 = f34 - ((AndroidUtilities.dp(8.0f) * f) * this.scale);
                    dp3 = f34 + (AndroidUtilities.dp(8.0f) * f * this.scale);
                    dp4 = AndroidUtilities.dp(8.0f) * f;
                    f2 = this.scale;
                } else {
                    float f35 = this.scale;
                    float dp9 = (AndroidUtilities.dp(13.0f) * f35 * f35) + (this.isMini ? AndroidUtilities.dp(2.0f) : 0);
                    float f36 = f - 0.5f;
                    float f37 = f36 / 0.5f;
                    if (f36 > 0.2f) {
                        f10 = (f36 - 0.2f) / 0.3f;
                        f9 = 1.0f;
                    } else {
                        f9 = f36 / 0.2f;
                        f10 = 0.0f;
                    }
                    float f38 = centerX2;
                    float f39 = f38 - dp9;
                    float f40 = dp9 / 2.0f;
                    this.rect.set(f39, dp8 - f40, f38, f40 + dp8);
                    float f41 = f10 * 100.0f;
                    i3 = i;
                    i4 = centerY4;
                    i2 = centerX2;
                    canvas.drawArc(this.rect, f41, (f37 * 104.0f) - f41, false, this.paint);
                    float f42 = dp + ((dp8 - dp) * f9);
                    if (f10 > 0.0f) {
                        float f43 = this.nextIcon == 14 ? 0.0f : (-45.0f) * (1.0f - f10);
                        float dp10 = AndroidUtilities.dp(7.0f) * f10 * this.scale;
                        int i30 = (int) (f10 * 255.0f);
                        int i31 = this.nextIcon;
                        if (i31 != 3 && i31 != 14 && i31 != 2) {
                            i30 = (int) (i30 * (1.0f - Math.min(1.0f, this.transitionProgress / 0.5f)));
                        }
                        int i32 = i30;
                        if (f43 != 0.0f) {
                            canvas.save();
                            canvas.rotate(f43, f38, f33);
                        }
                        if (i32 != 0) {
                            this.paint.setAlpha(i32);
                            if (this.nextIcon == 14) {
                                this.paint3.setAlpha(i32);
                                this.rect.set(i2 - AndroidUtilities.dp(3.5f), i4 - AndroidUtilities.dp(3.5f), AndroidUtilities.dp(3.5f) + i2, AndroidUtilities.dp(3.5f) + i4);
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint3);
                                this.paint.setAlpha((int) (i32 * 0.15f));
                                int dp11 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                                this.rect.set(bounds.left + dp11, bounds.top + dp11, bounds.right - dp11, bounds.bottom - dp11);
                                canvas.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                                this.paint.setAlpha(i32);
                                rect2 = bounds;
                            } else {
                                rect2 = bounds;
                                float f44 = f38 - dp10;
                                float f45 = f33 - dp10;
                                float f46 = f38 + dp10;
                                float f47 = f33 + dp10;
                                canvas.drawLine(f44, f45, f46, f47, this.paint);
                                canvas.drawLine(f46, f45, f44, f47, this.paint);
                            }
                        } else {
                            rect2 = bounds;
                        }
                        if (f43 != 0.0f) {
                            canvas.restore();
                        }
                    } else {
                        rect2 = bounds;
                    }
                    f4 = f38;
                    f6 = f42;
                    f5 = f4;
                    rect = rect2;
                    f3 = dp8;
                    if (f6 != f3) {
                        float f48 = i2;
                        canvas.drawLine(f48, f6, f48, f3, this.paint);
                    }
                    f7 = i2;
                    if (f5 != f7) {
                        float f49 = dp8;
                        float f50 = f3;
                        canvas.drawLine(f5, f49, f7, f50, this.paint);
                        canvas.drawLine(f4, f49, f7, f50, this.paint);
                    }
                }
            } else {
                i2 = centerX2;
                rect = bounds;
                i3 = i;
                i4 = centerY4;
                float f51 = i2;
                dp2 = f51 - (AndroidUtilities.dp(8.0f) * this.scale);
                dp3 = f51 + (AndroidUtilities.dp(8.0f) * this.scale);
                dp4 = AndroidUtilities.dp(8.0f);
                f2 = this.scale;
            }
            f3 = dp7;
            f4 = dp3;
            f5 = dp2;
            dp8 = dp7 - (dp4 * f2);
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
        int i33 = this.currentIcon;
        if (i33 == 3 || i33 == 14 || (i33 == 4 && ((i24 = this.nextIcon) == 14 || i24 == 3))) {
            applyShaderMatrix(false);
            int i34 = this.nextIcon;
            if (i34 == 2) {
                float f52 = this.transitionProgress;
                if (f52 <= 0.5f) {
                    float f53 = 1.0f - (f52 / 0.5f);
                    f16 = AndroidUtilities.dp(7.0f) * f53 * this.scale;
                    i11 = (int) (f53 * 255.0f);
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
                if (i34 != 15 && i34 != 0) {
                    if (i34 != 1 && i34 != 5) {
                        i5 = 8;
                        i6 = 9;
                        if (i34 != 8 && i34 != 9 && i34 != 7) {
                            i7 = 6;
                            if (i34 != 6) {
                                if (i34 == 4) {
                                    float f54 = 1.0f - this.transitionProgress;
                                    float dp12 = AndroidUtilities.dp(7.0f) * this.scale;
                                    int i35 = (int) (f54 * 255.0f);
                                    if (this.currentIcon == 14) {
                                        f15 = rect.left;
                                        centerY3 = rect.top;
                                    } else {
                                        f15 = rect.centerX();
                                        centerY3 = rect.centerY();
                                    }
                                    float f55 = centerY3;
                                    f13 = f54;
                                    f16 = dp12;
                                    f11 = 1.0f;
                                    f14 = 0.0f;
                                    min = i35;
                                    centerY = f55;
                                } else if (i34 == 14 || i34 == 3) {
                                    float f56 = this.transitionProgress;
                                    float f57 = 1.0f - f56;
                                    if (this.currentIcon == 4) {
                                        f18 = f56;
                                        f17 = 0.0f;
                                    } else {
                                        f17 = 45.0f * f57;
                                        f18 = 1.0f;
                                    }
                                    dp5 = AndroidUtilities.dp(7.0f) * this.scale;
                                    min = (int) (f56 * 255.0f);
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
                                    float f58 = dp5;
                                    f15 = centerX;
                                    f16 = f58;
                                } else {
                                    f16 = AndroidUtilities.dp(7.0f) * this.scale;
                                    f11 = 1.0f;
                                    centerY = 0.0f;
                                    f15 = 0.0f;
                                    f14 = 0.0f;
                                    min = NotificationCenter.voipServiceCreated;
                                    f13 = 1.0f;
                                }
                            }
                            if (i34 == i7) {
                                f11 = 1.0f;
                                f12 = Math.min(1.0f, this.transitionProgress / 0.5f);
                            } else {
                                f11 = 1.0f;
                                f12 = this.transitionProgress;
                            }
                            float f59 = f11 - f12;
                            centerX = rect.centerX();
                            centerY = rect.centerY();
                            dp5 = AndroidUtilities.dp(7.0f) * f59 * this.scale;
                            min = (int) (Math.min(f11, f59 * 2.0f) * 255.0f);
                            f13 = f59;
                            f14 = 0.0f;
                            float f582 = dp5;
                            f15 = centerX;
                            f16 = f582;
                        }
                        i7 = 6;
                        if (i34 == i7) {
                        }
                        float f592 = f11 - f12;
                        centerX = rect.centerX();
                        centerY = rect.centerY();
                        dp5 = AndroidUtilities.dp(7.0f) * f592 * this.scale;
                        min = (int) (Math.min(f11, f592 * 2.0f) * 255.0f);
                        f13 = f592;
                        f14 = 0.0f;
                        float f5822 = dp5;
                        f15 = centerX;
                        f16 = f5822;
                    }
                }
                i5 = 8;
                i6 = 9;
                i7 = 6;
                if (i34 == i7) {
                }
                float f5922 = f11 - f12;
                centerX = rect.centerX();
                centerY = rect.centerY();
                dp5 = AndroidUtilities.dp(7.0f) * f5922 * this.scale;
                min = (int) (Math.min(f11, f5922 * 2.0f) * 255.0f);
                f13 = f5922;
                f14 = 0.0f;
                float f58222 = dp5;
                f15 = centerX;
                f16 = f58222;
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
                float f60 = min;
                this.paint.setAlpha((int) (this.overrideAlpha * f60));
                if (this.currentIcon == 14 || this.nextIcon == 14) {
                    f19 = f14;
                    i8 = min;
                    i9 = 7;
                    this.paint3.setAlpha((int) (f60 * this.overrideAlpha));
                    this.rect.set(i2 - AndroidUtilities.dp(3.5f), i4 - AndroidUtilities.dp(3.5f), AndroidUtilities.dp(3.5f) + i2, AndroidUtilities.dp(3.5f) + i4);
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint3);
                } else {
                    float f61 = i2;
                    float f62 = f61 - f16;
                    float f63 = i4;
                    float f64 = f63 - f16;
                    float f65 = f61 + f16;
                    float f66 = f63 + f16;
                    f19 = f14;
                    i8 = min;
                    i9 = 7;
                    canvas.drawLine(f62, f64, f65, f66, this.paint);
                    canvas.drawLine(f65, f64, f62, f66, this.paint);
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
            int i36 = this.currentIcon;
            if ((i36 == 3 || i36 == 14 || (i36 == 4 && ((i10 = this.nextIcon) == 14 || i10 == 3))) && i8 != 0) {
                float max = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                int dp13 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                this.rect.set(rect.left + dp13, rect.top + dp13, rect.right - dp13, rect.bottom - dp13);
                int i37 = this.currentIcon;
                if (i37 == 14 || (i37 == 4 && this.nextIcon == 14)) {
                    this.paint.setAlpha((int) (i8 * 0.15f * this.overrideAlpha));
                    canvas.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                    this.paint.setAlpha(i8);
                }
                canvas.drawArc(this.rect, this.downloadRadOffset, max, false, this.paint);
            }
        } else {
            if (i33 == 10 || this.nextIcon == 10 || i33 == 13) {
                int i38 = this.nextIcon;
                int i39 = (i38 == 4 || i38 == 6) ? (int) ((1.0f - this.transitionProgress) * 255.0f) : NotificationCenter.voipServiceCreated;
                if (i39 != 0) {
                    applyShaderMatrix(false);
                    this.paint.setAlpha((int) (i39 * this.overrideAlpha));
                    float max2 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                    int dp14 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                    this.rect.set(rect.left + dp14, rect.top + dp14, rect.right - dp14, rect.bottom - dp14);
                    canvas.drawArc(this.rect, this.downloadRadOffset, max2, false, this.paint);
                }
            }
            i9 = 7;
            i5 = 8;
            i6 = 9;
        }
        int i40 = this.currentIcon;
        if (i40 == this.nextIcon) {
            f20 = 1.0f;
            f21 = 1.0f;
        } else if (i40 == 4 || i40 == 3 || i40 == 14) {
            float f67 = this.transitionProgress;
            f20 = f67;
            f21 = 1.0f - f67;
        } else {
            float min2 = Math.min(1.0f, this.transitionProgress / 0.5f);
            f21 = Math.max(0.0f, 1.0f - (this.transitionProgress / 0.5f));
            f20 = min2;
        }
        int i41 = this.nextIcon;
        if (i41 == 15) {
            pathArr = Theme.chat_updatePath;
        } else if (this.currentIcon == 15) {
            pathArr2 = Theme.chat_updatePath;
            pathArr = null;
            if (i41 != 5) {
                pathArr = Theme.chat_filePath;
            } else if (this.currentIcon == 5) {
                pathArr2 = Theme.chat_filePath;
            }
            pathArr3 = pathArr;
            pathArr4 = pathArr2;
            if (i41 != i9) {
                drawable2 = Theme.chat_flameIcon;
                drawable = null;
            } else {
                drawable = this.currentIcon == i9 ? Theme.chat_flameIcon : null;
                drawable2 = null;
            }
            if (i41 != i5) {
                drawable2 = Theme.chat_gifIcon;
            } else if (this.currentIcon == i5) {
                drawable = Theme.chat_gifIcon;
            }
            Drawable drawable3 = drawable;
            Drawable drawable4 = drawable2;
            if (this.currentIcon != i6 || i41 == i6) {
                applyShaderMatrix(false);
                this.paint.setAlpha(this.currentIcon != this.nextIcon ? NotificationCenter.voipServiceCreated : (int) (this.transitionProgress * 255.0f));
                int dp15 = i4 + AndroidUtilities.dp(7.0f);
                int dp16 = i2 - AndroidUtilities.dp(3.0f);
                if (this.currentIcon != this.nextIcon) {
                    canvas.save();
                    float f68 = this.transitionProgress;
                    canvas.scale(f68, f68, i2, i4);
                }
                float f69 = dp16;
                f22 = f21;
                float f70 = dp15;
                pathArr5 = pathArr4;
                pathArr6 = pathArr3;
                canvas.drawLine(dp16 - AndroidUtilities.dp(6.0f), dp15 - AndroidUtilities.dp(6.0f), f69, f70, this.paint);
                canvas.drawLine(f69, f70, dp16 + AndroidUtilities.dp(12.0f), dp15 - AndroidUtilities.dp(12.0f), this.paint);
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
                i12 = this.currentIcon;
                i13 = this.nextIcon;
                if (i12 != i13) {
                    f23 = 1.0f;
                } else if (i13 == 13) {
                    f23 = this.transitionProgress;
                } else {
                    f23 = 1.0f - this.transitionProgress;
                }
                this.paint.setAlpha(i12 != i13 ? NotificationCenter.voipServiceCreated : (int) (f23 * 255.0f));
                AndroidUtilities.dp(7.0f);
                AndroidUtilities.dp(3.0f);
                if (this.currentIcon != this.nextIcon) {
                    canvas.save();
                    canvas.scale(f23, f23, i2, i4);
                }
                float dp17 = AndroidUtilities.dp(7.0f) * this.scale;
                float f71 = i2;
                float f72 = f71 - dp17;
                float f73 = i4;
                float f74 = f73 - dp17;
                float f75 = f71 + dp17;
                float f76 = f73 + dp17;
                canvas.drawLine(f72, f74, f75, f76, this.paint);
                canvas.drawLine(f75, f74, f72, f76, this.paint);
                if (this.currentIcon != this.nextIcon) {
                    canvas.restore();
                }
            }
            if (this.currentIcon != 13 || this.nextIcon == 13) {
                applyShaderMatrix(false);
                i14 = this.currentIcon;
                i15 = this.nextIcon;
                if (i14 != i15) {
                    f24 = 1.0f;
                } else if (i15 == 13) {
                    f24 = this.transitionProgress;
                } else {
                    f24 = 1.0f - this.transitionProgress;
                }
                this.textPaint.setAlpha((int) (f24 * 255.0f));
                int dp18 = i4 + AndroidUtilities.dp(5.0f);
                int i42 = i2 - (this.percentStringWidth / 2);
                if (this.currentIcon != this.nextIcon) {
                    canvas.save();
                    canvas.scale(f24, f24, i2, i4);
                }
                i16 = (int) (this.animatedDownloadProgress * 100.0f);
                if (this.percentString != null || i16 != this.lastPercent) {
                    this.lastPercent = i16;
                    this.percentString = String.format("%d%%", Integer.valueOf(i16));
                    this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(format));
                }
                canvas.drawText(this.percentString, i42, dp18, this.textPaint);
                if (this.currentIcon != this.nextIcon) {
                    canvas.restore();
                }
            }
            i17 = this.currentIcon;
            if (i17 != 0 || i17 == 1 || (i23 = this.nextIcon) == 0 || i23 == 1) {
                if ((i17 == 0 || this.nextIcon != 1) && !(i17 == 1 && this.nextIcon == 0)) {
                    i18 = 1;
                } else if (this.animatingTransition) {
                    if (this.nextIcon == 0) {
                        f25 = 1.0f - this.transitionProgress;
                    } else {
                        f25 = this.transitionProgress;
                    }
                    i18 = 1;
                } else {
                    i18 = 1;
                }
                int i43 = this.nextIcon;
                if ((i43 == 0 || i43 == i18) && (i17 == 0 || i17 == i18)) {
                    Paint paint = this.paint2;
                    i19 = NotificationCenter.voipServiceCreated;
                    paint.setAlpha(NotificationCenter.voipServiceCreated);
                    z = true;
                } else {
                    if (i43 == 4) {
                        this.paint2.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                    } else {
                        this.paint2.setAlpha(i17 == i43 ? NotificationCenter.voipServiceCreated : (int) (this.transitionProgress * 255.0f));
                    }
                    z = true;
                    i19 = NotificationCenter.voipServiceCreated;
                }
                applyShaderMatrix(z);
                canvas.save();
                canvas.translate(rect.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f25)), rect.centerY());
                float f77 = f25 * 500.0f;
                int i44 = this.currentIcon;
                float f78 = i44 == 1 ? 90.0f : 0.0f;
                if (i44 == 0 && this.nextIcon == 1) {
                    if (f77 < 384.0f) {
                        f30 = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f77 / 384.0f) * 95.0f;
                    } else if (f77 < 484.0f) {
                        f30 = 95.0f - (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f77 - 384.0f) / 100.0f) * 5.0f);
                    }
                    f77 += 100.0f;
                } else {
                    if (i44 == 1 && this.nextIcon == 0) {
                        if (f77 < 100.0f) {
                            f30 = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f77 / 100.0f) * (-5.0f);
                        } else if (f77 < 484.0f) {
                            f30 = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f77 - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                        } else {
                            f78 = 90.0f;
                        }
                    }
                    canvas.rotate(f78);
                    i20 = this.currentIcon;
                    if ((i20 != 0 && i20 != 1) || i20 == 4) {
                        canvas.scale(f20, f20);
                    }
                    Theme.playPauseAnimator.draw(canvas, this.paint2, f77);
                    canvas.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas, this.paint2, f77);
                    canvas.restore();
                }
                f78 = f30;
                canvas.rotate(f78);
                i20 = this.currentIcon;
                if (i20 != 0) {
                    canvas.scale(f20, f20);
                    Theme.playPauseAnimator.draw(canvas, this.paint2, f77);
                    canvas.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas, this.paint2, f77);
                    canvas.restore();
                }
                canvas.scale(f20, f20);
                Theme.playPauseAnimator.draw(canvas, this.paint2, f77);
                canvas.scale(1.0f, -1.0f);
                Theme.playPauseAnimator.draw(canvas, this.paint2, f77);
                canvas.restore();
            } else {
                i19 = NotificationCenter.voipServiceCreated;
            }
            if (this.currentIcon == 6 || this.nextIcon == 6) {
                applyShaderMatrix(false);
                if (this.currentIcon != 6) {
                    float f79 = this.transitionProgress;
                    if (f79 > 0.5f) {
                        float f80 = (f79 - 0.5f) / 0.5f;
                        f27 = 1.0f - Math.min(1.0f, f80 / 0.5f);
                        f29 = f80 > 0.5f ? (f80 - 0.5f) / 0.5f : 0.0f;
                    } else {
                        f29 = 0.0f;
                        f27 = 1.0f;
                    }
                    this.paint.setAlpha(i19);
                    f26 = f29;
                } else {
                    if (this.nextIcon != 6) {
                        this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                    } else {
                        this.paint.setAlpha(i19);
                    }
                    f26 = 1.0f;
                    f27 = 0.0f;
                }
                int dp19 = i4 + AndroidUtilities.dp(7.0f);
                int dp20 = i2 - AndroidUtilities.dp(3.0f);
                if (f27 < 1.0f) {
                    f28 = f20;
                    i22 = dp20;
                    i21 = dp19;
                    canvas.drawLine(dp20 - AndroidUtilities.dp(6.0f), dp19 - AndroidUtilities.dp(6.0f), dp20 - (AndroidUtilities.dp(6.0f) * f27), dp19 - (AndroidUtilities.dp(6.0f) * f27), this.paint);
                } else {
                    i21 = dp19;
                    f28 = f20;
                    i22 = dp20;
                }
                if (f26 > 0.0f) {
                    float f81 = i22;
                    float f82 = i21;
                    canvas.drawLine(f81, f82, f81 + (AndroidUtilities.dp(12.0f) * f26), f82 - (AndroidUtilities.dp(12.0f) * f26), this.paint);
                }
            } else {
                f28 = f20;
            }
            if (drawable3 != null && drawable3 != drawable4) {
                int intrinsicWidth = (int) (drawable3.getIntrinsicWidth() * f22);
                int intrinsicHeight = (int) (drawable3.getIntrinsicHeight() * f22);
                drawable3.setColorFilter(this.colorFilter);
                drawable3.setAlpha(this.currentIcon == this.nextIcon ? NotificationCenter.voipServiceCreated : (int) ((1.0f - this.transitionProgress) * 255.0f));
                int i45 = intrinsicWidth / 2;
                int i46 = intrinsicHeight / 2;
                drawable3.setBounds(i2 - i45, i4 - i46, i45 + i2, i46 + i4);
                drawable3.draw(canvas);
            }
            if (drawable4 != null) {
                int intrinsicWidth2 = (int) (drawable4.getIntrinsicWidth() * f28);
                int intrinsicHeight2 = (int) (drawable4.getIntrinsicHeight() * f28);
                drawable4.setColorFilter(this.colorFilter);
                drawable4.setAlpha(this.currentIcon == this.nextIcon ? NotificationCenter.voipServiceCreated : (int) (this.transitionProgress * 255.0f));
                int i47 = intrinsicWidth2 / 2;
                int i48 = intrinsicHeight2 / 2;
                drawable4.setBounds(i2 - i47, i4 - i48, i47 + i2, i48 + i4);
                drawable4.draw(canvas);
            }
            Path[] pathArr7 = pathArr5;
            Path[] pathArr8 = pathArr6;
            if (pathArr7 != null && pathArr7 != pathArr8) {
                int dp21 = AndroidUtilities.dp(24.0f);
                this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                this.paint2.setAlpha(this.currentIcon == this.nextIcon ? NotificationCenter.voipServiceCreated : (int) ((1.0f - this.transitionProgress) * 255.0f));
                applyShaderMatrix(true);
                canvas.save();
                canvas.translate(i2, i4);
                float f83 = f22;
                canvas.scale(f83, f83);
                float f84 = (-dp21) / 2;
                canvas.translate(f84, f84);
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
                int dp22 = AndroidUtilities.dp(24.0f);
                int i49 = this.currentIcon == this.nextIcon ? NotificationCenter.voipServiceCreated : (int) (this.transitionProgress * 255.0f);
                this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                this.paint2.setAlpha(i49);
                applyShaderMatrix(true);
                canvas.save();
                canvas.translate(i2, i4);
                float f85 = f28;
                canvas.scale(f85, f85);
                float f86 = (-dp22) / 2;
                canvas.translate(f86, f86);
                Path path4 = pathArr8[0];
                if (path4 != null) {
                    canvas.drawPath(path4, this.paint2);
                }
                if (pathArr8.length >= 3 && (path = pathArr8[2]) != null) {
                    canvas.drawPath(path, this.paint);
                }
                Path path5 = pathArr8[1];
                if (path5 != null) {
                    if (i49 != 255) {
                        int alpha = this.backPaint.getAlpha();
                        this.backPaint.setAlpha((int) (alpha * (i49 / 255.0f)));
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
            int i50 = this.currentIcon;
            if (i50 == 3 || i50 == 14 || ((i50 == 4 && this.nextIcon == 14) || i50 == 10 || i50 == 13)) {
                float f87 = this.downloadRadOffset + (((float) (360 * j2)) / 2500.0f);
                this.downloadRadOffset = f87;
                this.downloadRadOffset = getCircleValue(f87);
                if (this.nextIcon != 2) {
                    float f88 = this.downloadProgress;
                    float f89 = this.downloadProgressAnimationStart;
                    float f90 = f88 - f89;
                    if (f90 > 0.0f) {
                        float f91 = this.downloadProgressTime + ((float) j2);
                        this.downloadProgressTime = f91;
                        if (f91 >= 200.0f) {
                            this.animatedDownloadProgress = f88;
                            this.downloadProgressAnimationStart = f88;
                            this.downloadProgressTime = 0.0f;
                        } else {
                            this.animatedDownloadProgress = f89 + (f90 * this.interpolator.getInterpolation(f91 / 200.0f));
                        }
                    }
                }
                invalidateSelf();
            }
            if (this.animatingTransition) {
                float f92 = this.transitionProgress;
                if (f92 < 1.0f) {
                    float f93 = f92 + (((float) j2) / this.transitionAnimationTime);
                    this.transitionProgress = f93;
                    if (f93 >= 1.0f) {
                        this.currentIcon = this.nextIcon;
                        this.transitionProgress = 1.0f;
                        this.animatingTransition = false;
                    }
                    invalidateSelf();
                }
            }
            int i51 = i3;
            if (i51 >= 1) {
                canvas.restoreToCount(i51);
                return;
            }
            return;
        } else {
            pathArr = null;
        }
        pathArr2 = null;
        if (i41 != 5) {
        }
        pathArr3 = pathArr;
        pathArr4 = pathArr2;
        if (i41 != i9) {
        }
        if (i41 != i5) {
        }
        Drawable drawable32 = drawable;
        Drawable drawable42 = drawable2;
        if (this.currentIcon != i6) {
        }
        applyShaderMatrix(false);
        this.paint.setAlpha(this.currentIcon != this.nextIcon ? NotificationCenter.voipServiceCreated : (int) (this.transitionProgress * 255.0f));
        int dp152 = i4 + AndroidUtilities.dp(7.0f);
        int dp162 = i2 - AndroidUtilities.dp(3.0f);
        if (this.currentIcon != this.nextIcon) {
        }
        float f692 = dp162;
        f22 = f21;
        float f702 = dp152;
        pathArr5 = pathArr4;
        pathArr6 = pathArr3;
        canvas.drawLine(dp162 - AndroidUtilities.dp(6.0f), dp152 - AndroidUtilities.dp(6.0f), f692, f702, this.paint);
        canvas.drawLine(f692, f702, dp162 + AndroidUtilities.dp(12.0f), dp152 - AndroidUtilities.dp(12.0f), this.paint);
        if (this.currentIcon != this.nextIcon) {
        }
        if (this.currentIcon != 12) {
        }
        applyShaderMatrix(false);
        i12 = this.currentIcon;
        i13 = this.nextIcon;
        if (i12 != i13) {
        }
        this.paint.setAlpha(i12 != i13 ? NotificationCenter.voipServiceCreated : (int) (f23 * 255.0f));
        AndroidUtilities.dp(7.0f);
        AndroidUtilities.dp(3.0f);
        if (this.currentIcon != this.nextIcon) {
        }
        float dp172 = AndroidUtilities.dp(7.0f) * this.scale;
        float f712 = i2;
        float f722 = f712 - dp172;
        float f732 = i4;
        float f742 = f732 - dp172;
        float f752 = f712 + dp172;
        float f762 = f732 + dp172;
        canvas.drawLine(f722, f742, f752, f762, this.paint);
        canvas.drawLine(f752, f742, f722, f762, this.paint);
        if (this.currentIcon != this.nextIcon) {
        }
        if (this.currentIcon != 13) {
        }
        applyShaderMatrix(false);
        i14 = this.currentIcon;
        i15 = this.nextIcon;
        if (i14 != i15) {
        }
        this.textPaint.setAlpha((int) (f24 * 255.0f));
        int dp182 = i4 + AndroidUtilities.dp(5.0f);
        int i422 = i2 - (this.percentStringWidth / 2);
        if (this.currentIcon != this.nextIcon) {
        }
        i16 = (int) (this.animatedDownloadProgress * 100.0f);
        if (this.percentString != null) {
        }
        this.lastPercent = i16;
        this.percentString = String.format("%d%%", Integer.valueOf(i16));
        this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(format));
        canvas.drawText(this.percentString, i422, dp182, this.textPaint);
        if (this.currentIcon != this.nextIcon) {
        }
        i17 = this.currentIcon;
        if (i17 != 0) {
        }
        if (i17 == 0) {
        }
        i18 = 1;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(48.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(48.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumWidth() {
        return AndroidUtilities.dp(48.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumHeight() {
        return AndroidUtilities.dp(48.0f);
    }
}
