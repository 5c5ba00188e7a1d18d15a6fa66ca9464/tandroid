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
    private Paint paint = new Paint(1);
    private Paint backPaint = new Paint(1);
    private Paint paint2 = new Paint(1);
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
        this.textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
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

    private void applyShaderMatrix(boolean z) {
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

    /* JADX WARN: Code restructure failed: missing block: B:351:0x0885, code lost:
        if (r33.nextIcon == 1) goto L242;
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x0887, code lost:
        r4 = 1.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x088a, code lost:
        r4 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x088d, code lost:
        if (r1 != 1) goto L243;
     */
    /* JADX WARN: Removed duplicated region for block: B:188:0x050f  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x051a  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x0524  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x05a2  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x05aa  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x05b3  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x05bb  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x05d8  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x05db  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x05fc  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x067f  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x0682  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x068e  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x0694  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x06a3  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x06a6  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x06d3  */
    /* JADX WARN: Removed duplicated region for block: B:279:0x06d6  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x06f2  */
    /* JADX WARN: Removed duplicated region for block: B:283:0x06ff  */
    /* JADX WARN: Removed duplicated region for block: B:286:0x074c  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x0765  */
    /* JADX WARN: Removed duplicated region for block: B:294:0x0768  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x077a  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x077d  */
    /* JADX WARN: Removed duplicated region for block: B:304:0x0791  */
    /* JADX WARN: Removed duplicated region for block: B:307:0x07c8  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x07df  */
    /* JADX WARN: Removed duplicated region for block: B:315:0x07e2  */
    /* JADX WARN: Removed duplicated region for block: B:320:0x0808  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x084e  */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        int save;
        int i;
        float f;
        float dp;
        float dp2;
        float f2;
        float f3;
        float f4;
        float f5;
        float dp3;
        float dp4;
        float f6;
        float f7;
        float f8;
        float f9;
        float f10;
        float f11;
        boolean z;
        int i2;
        float f12;
        float f13;
        float f14;
        int min;
        float f15;
        float centerY;
        float centerX;
        float f16;
        float f17;
        float f18;
        float f19;
        float centerX2;
        int centerY2;
        int centerY3;
        float f20;
        int i3;
        int i4;
        int i5;
        int i6;
        float f21;
        float f22;
        float f23;
        float f24;
        Path[] pathArr;
        Path[] pathArr2;
        Path[] pathArr3;
        Path[] pathArr4;
        Drawable drawable;
        Drawable drawable2;
        Drawable drawable3;
        Drawable drawable4;
        int i7;
        Drawable drawable5;
        Path[] pathArr5;
        Drawable drawable6;
        Path[] pathArr6;
        int i8;
        int i9;
        float f25;
        int i10;
        int i11;
        float f26;
        int i12;
        String format;
        int i13;
        int i14;
        float f27;
        int i15;
        boolean z2;
        float f28;
        float f29;
        int i16;
        float f30;
        int i17;
        float f31;
        int i18;
        int i19;
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
        int centerX3 = bounds.centerX();
        int centerY4 = bounds.centerY();
        int i20 = this.nextIcon;
        if (i20 == 4) {
            int i21 = this.currentIcon;
            if (i21 != 3 && i21 != 14) {
                save = canvas.save();
                float f32 = 1.0f - this.transitionProgress;
                canvas.scale(f32, f32, centerX3, centerY4);
                i = save;
            }
            i = 0;
        } else {
            if ((i20 == 6 || i20 == 10) && this.currentIcon == 4) {
                save = canvas.save();
                float f33 = this.transitionProgress;
                canvas.scale(f33, f33, centerX3, centerY4);
                i = save;
            }
            i = 0;
        }
        AndroidUtilities.dp(3.0f);
        if (this.currentIcon == 2 || this.nextIcon == 2) {
            applyShaderMatrix(false);
            float f34 = centerY4;
            float dp5 = f34 - (AndroidUtilities.dp(9.0f) * this.scale);
            float dp6 = (AndroidUtilities.dp(9.0f) * this.scale) + f34;
            float dp7 = (AndroidUtilities.dp(12.0f) * this.scale) + f34;
            int i22 = this.currentIcon;
            if ((i22 == 3 || i22 == 14) && this.nextIcon == 2) {
                this.paint.setAlpha((int) (Math.min(1.0f, this.transitionProgress / 0.5f) * 255.0f));
                f = this.transitionProgress;
                dp = (AndroidUtilities.dp(12.0f) * this.scale) + f34;
            } else {
                int i23 = this.nextIcon;
                if (i23 != 3 && i23 != 14 && i23 != 2) {
                    this.paint.setAlpha((int) (Math.min(1.0f, this.savedTransitionProgress / 0.5f) * 255.0f * (1.0f - this.transitionProgress)));
                    f = this.savedTransitionProgress;
                } else {
                    this.paint.setAlpha(255);
                    f = this.transitionProgress;
                }
                dp = f34 + (AndroidUtilities.dp(1.0f) * this.scale);
            }
            if (this.animatingTransition) {
                int i24 = this.nextIcon;
                if (i24 == 2 || f <= 0.5f) {
                    if (i24 == 2) {
                        f4 = 1.0f - f;
                    } else {
                        f4 = f / 0.5f;
                        f = 1.0f - f4;
                    }
                    f5 = dp5 + ((dp - dp5) * f4);
                    float f35 = ((dp7 - dp6) * f4) + dp6;
                    float f36 = centerX3;
                    dp3 = f36 - ((AndroidUtilities.dp(8.0f) * f) * this.scale);
                    dp4 = f36 + (AndroidUtilities.dp(8.0f) * f * this.scale);
                    dp7 = f35 - ((AndroidUtilities.dp(8.0f) * f) * this.scale);
                    f6 = f35;
                } else {
                    float f37 = this.scale;
                    float dp8 = (AndroidUtilities.dp(13.0f) * f37 * f37) + (this.isMini ? AndroidUtilities.dp(2.0f) : 0);
                    float f38 = f - 0.5f;
                    float f39 = f38 / 0.5f;
                    if (f38 > 0.2f) {
                        f8 = (f38 - 0.2f) / 0.3f;
                        f7 = 1.0f;
                    } else {
                        f7 = f38 / 0.2f;
                        f8 = 0.0f;
                    }
                    float f40 = centerX3;
                    float f41 = f40 - dp8;
                    float f42 = dp8 / 2.0f;
                    this.rect.set(f41, dp7 - f42, f40, f42 + dp7);
                    float f43 = f8 * 100.0f;
                    canvas.drawArc(this.rect, f43, (f39 * 104.0f) - f43, false, this.paint);
                    f5 = dp + ((dp7 - dp) * f7);
                    if (f8 > 0.0f) {
                        float f44 = this.nextIcon == 14 ? 0.0f : (-45.0f) * (1.0f - f8);
                        float dp9 = AndroidUtilities.dp(7.0f) * f8 * this.scale;
                        int i25 = (int) (f8 * 255.0f);
                        int i26 = this.nextIcon;
                        if (i26 != 3 && i26 != 14 && i26 != 2) {
                            i25 = (int) (i25 * (1.0f - Math.min(1.0f, this.transitionProgress / 0.5f)));
                        }
                        int i27 = i25;
                        if (f44 != 0.0f) {
                            canvas.save();
                            f10 = f34;
                            f11 = f40;
                            canvas.rotate(f44, f11, f10);
                        } else {
                            f10 = f34;
                            f11 = f40;
                        }
                        if (i27 != 0) {
                            this.paint.setAlpha(i27);
                            if (this.nextIcon == 14) {
                                this.paint3.setAlpha(i27);
                                this.rect.set(centerX3 - AndroidUtilities.dp(3.5f), centerY4 - AndroidUtilities.dp(3.5f), AndroidUtilities.dp(3.5f) + centerX3, centerY4 + AndroidUtilities.dp(3.5f));
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint3);
                                this.paint.setAlpha((int) (i27 * 0.15f));
                                int dp10 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                                this.rect.set(bounds.left + dp10, bounds.top + dp10, bounds.right - dp10, bounds.bottom - dp10);
                                f9 = f11;
                                canvas.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                                this.paint.setAlpha(i27);
                            } else {
                                f9 = f11;
                                float f45 = f9 - dp9;
                                float f46 = f10 - dp9;
                                float f47 = f9 + dp9;
                                float f48 = f10 + dp9;
                                canvas.drawLine(f45, f46, f47, f48, this.paint);
                                canvas.drawLine(f47, f46, f45, f48, this.paint);
                            }
                        } else {
                            f9 = f11;
                        }
                        if (f44 != 0.0f) {
                            canvas.restore();
                        }
                    } else {
                        f9 = f40;
                    }
                    f6 = dp7;
                    dp4 = f9;
                    dp3 = dp4;
                }
                dp2 = dp4;
                f3 = f6;
                f2 = dp3;
                dp5 = f5;
            } else {
                float f49 = centerX3;
                float dp11 = f49 - (AndroidUtilities.dp(8.0f) * this.scale);
                dp2 = f49 + (AndroidUtilities.dp(8.0f) * this.scale);
                f2 = dp11;
                dp7 = dp6 - (AndroidUtilities.dp(8.0f) * this.scale);
                f3 = dp6;
            }
            if (dp5 != f3) {
                float f50 = centerX3;
                canvas.drawLine(f50, dp5, f50, f3, this.paint);
            }
            float f51 = centerX3;
            if (f2 != f51) {
                float f52 = dp7;
                float f53 = f3;
                canvas.drawLine(f2, f52, f51, f53, this.paint);
                canvas.drawLine(dp2, f52, f51, f53, this.paint);
            }
        }
        int i28 = this.currentIcon;
        if (i28 == 3 || i28 == 14 || (i28 == 4 && ((i19 = this.nextIcon) == 14 || i19 == 3))) {
            z = false;
            applyShaderMatrix(false);
            int i29 = this.nextIcon;
            if (i29 == 2) {
                float f54 = this.transitionProgress;
                if (f54 <= 0.5f) {
                    float f55 = 1.0f - (f54 / 0.5f);
                    f16 = AndroidUtilities.dp(7.0f) * f55 * this.scale;
                    min = (int) (f55 * 255.0f);
                } else {
                    f16 = 0.0f;
                    min = 0;
                }
                f12 = 1.0f;
                centerX = 0.0f;
                f18 = 0.0f;
                f17 = 1.0f;
            } else {
                if (i29 == 15 || i29 == 0 || i29 == 1 || i29 == 5 || i29 == 8 || i29 == 9 || i29 == 7) {
                    i2 = 6;
                } else {
                    i2 = 6;
                    if (i29 != 6) {
                        if (i29 == 4) {
                            f14 = 1.0f - this.transitionProgress;
                            f16 = AndroidUtilities.dp(7.0f) * this.scale;
                            int i30 = (int) (f14 * 255.0f);
                            if (this.currentIcon == 14) {
                                centerX = bounds.left;
                                centerY3 = bounds.top;
                            } else {
                                centerX = bounds.centerX();
                                centerY3 = bounds.centerY();
                            }
                            min = i30;
                            f12 = 1.0f;
                            f15 = 0.0f;
                            centerY = centerY3;
                        } else if (i29 == 14 || i29 == 3) {
                            float f56 = this.transitionProgress;
                            float f57 = 1.0f - f56;
                            if (this.currentIcon == 4) {
                                f14 = f56;
                                f19 = 0.0f;
                            } else {
                                f19 = f57 * 45.0f;
                                f14 = 1.0f;
                            }
                            float dp12 = AndroidUtilities.dp(7.0f) * this.scale;
                            int i31 = (int) (f56 * 255.0f);
                            if (this.nextIcon == 14) {
                                centerX2 = bounds.left;
                                centerY2 = bounds.top;
                            } else {
                                centerX2 = bounds.centerX();
                                centerY2 = bounds.centerY();
                            }
                            f15 = f19;
                            f12 = 1.0f;
                            centerY = centerY2;
                            min = i31;
                            f16 = dp12;
                            centerX = centerX2;
                        } else {
                            f16 = AndroidUtilities.dp(7.0f) * this.scale;
                            f12 = 1.0f;
                            centerX = 0.0f;
                            f18 = 0.0f;
                            f17 = 1.0f;
                            min = 255;
                        }
                        f17 = f14;
                        f18 = centerY;
                        if (f17 != f12) {
                            canvas.save();
                            canvas.scale(f17, f17, centerX, f18);
                        }
                        if (f15 != 0.0f) {
                            canvas.save();
                            canvas.rotate(f15, centerX3, centerY4);
                        }
                        if (min != 0) {
                            float f58 = min;
                            this.paint.setAlpha((int) (this.overrideAlpha * f58));
                            if (this.currentIcon == 14 || this.nextIcon == 14) {
                                f20 = f17;
                                i3 = min;
                                this.paint3.setAlpha((int) (f58 * this.overrideAlpha));
                                this.rect.set(centerX3 - AndroidUtilities.dp(3.5f), centerY4 - AndroidUtilities.dp(3.5f), AndroidUtilities.dp(3.5f) + centerX3, AndroidUtilities.dp(3.5f) + centerY4);
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint3);
                            } else {
                                float f59 = centerX3;
                                float f60 = f59 - f16;
                                float f61 = centerY4;
                                float f62 = f61 - f16;
                                float f63 = f59 + f16;
                                float f64 = f61 + f16;
                                f20 = f17;
                                i3 = min;
                                canvas.drawLine(f60, f62, f63, f64, this.paint);
                                canvas.drawLine(f63, f62, f60, f64, this.paint);
                            }
                        } else {
                            f20 = f17;
                            i3 = min;
                        }
                        if (f15 != 0.0f) {
                            canvas.restore();
                        }
                        if (f20 != 1.0f) {
                            canvas.restore();
                        }
                        i4 = this.currentIcon;
                        if ((i4 != 3 || i4 == 14 || (i4 == 4 && ((i6 = this.nextIcon) == 14 || i6 == 3))) && i3 != 0) {
                            float max = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                            int dp13 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                            this.rect.set(bounds.left + dp13, bounds.top + dp13, bounds.right - dp13, bounds.bottom - dp13);
                            i5 = this.currentIcon;
                            if (i5 != 14 || (i5 == 4 && this.nextIcon == 14)) {
                                this.paint.setAlpha((int) (i3 * 0.15f * this.overrideAlpha));
                                canvas.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                                this.paint.setAlpha(i3);
                            }
                            canvas.drawArc(this.rect, this.downloadRadOffset, max, false, this.paint);
                        }
                    }
                }
                if (i29 == i2) {
                    f12 = 1.0f;
                    f13 = Math.min(1.0f, this.transitionProgress / 0.5f);
                } else {
                    f12 = 1.0f;
                    f13 = this.transitionProgress;
                }
                f14 = f12 - f13;
                float dp14 = AndroidUtilities.dp(7.0f) * f14 * this.scale;
                min = (int) (Math.min(f12, f14 * 2.0f) * 255.0f);
                f15 = 0.0f;
                centerY = bounds.centerY();
                centerX = bounds.centerX();
                f16 = dp14;
                f17 = f14;
                f18 = centerY;
                if (f17 != f12) {
                }
                if (f15 != 0.0f) {
                }
                if (min != 0) {
                }
                if (f15 != 0.0f) {
                }
                if (f20 != 1.0f) {
                }
                i4 = this.currentIcon;
                if (i4 != 3) {
                }
                float max2 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                int dp132 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                this.rect.set(bounds.left + dp132, bounds.top + dp132, bounds.right - dp132, bounds.bottom - dp132);
                i5 = this.currentIcon;
                if (i5 != 14) {
                }
                this.paint.setAlpha((int) (i3 * 0.15f * this.overrideAlpha));
                canvas.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                this.paint.setAlpha(i3);
                canvas.drawArc(this.rect, this.downloadRadOffset, max2, false, this.paint);
            }
            f15 = 0.0f;
            if (f17 != f12) {
            }
            if (f15 != 0.0f) {
            }
            if (min != 0) {
            }
            if (f15 != 0.0f) {
            }
            if (f20 != 1.0f) {
            }
            i4 = this.currentIcon;
            if (i4 != 3) {
            }
            float max22 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
            int dp1322 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
            this.rect.set(bounds.left + dp1322, bounds.top + dp1322, bounds.right - dp1322, bounds.bottom - dp1322);
            i5 = this.currentIcon;
            if (i5 != 14) {
            }
            this.paint.setAlpha((int) (i3 * 0.15f * this.overrideAlpha));
            canvas.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
            this.paint.setAlpha(i3);
            canvas.drawArc(this.rect, this.downloadRadOffset, max22, false, this.paint);
        } else {
            if (i28 == 10 || this.nextIcon == 10 || i28 == 13) {
                int i32 = this.nextIcon;
                int i33 = (i32 == 4 || i32 == 6) ? (int) ((1.0f - this.transitionProgress) * 255.0f) : 255;
                if (i33 != 0) {
                    z = false;
                    applyShaderMatrix(false);
                    this.paint.setAlpha((int) (i33 * this.overrideAlpha));
                    float max3 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                    int dp15 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                    this.rect.set(bounds.left + dp15, bounds.top + dp15, bounds.right - dp15, bounds.bottom - dp15);
                    canvas.drawArc(this.rect, this.downloadRadOffset, max3, false, this.paint);
                }
            }
            z = false;
        }
        int i34 = this.currentIcon;
        if (i34 == this.nextIcon) {
            f24 = 1.0f;
            f23 = 1.0f;
        } else {
            if (i34 == 4 || i34 == 3 || i34 == 14) {
                f21 = this.transitionProgress;
                f22 = 1.0f - f21;
            } else {
                f21 = Math.min(1.0f, this.transitionProgress / 0.5f);
                f22 = Math.max(0.0f, 1.0f - (this.transitionProgress / 0.5f));
            }
            f23 = f22;
            f24 = f21;
        }
        int i35 = this.nextIcon;
        if (i35 == 15) {
            pathArr = Theme.chat_updatePath;
        } else if (this.currentIcon == 15) {
            pathArr2 = Theme.chat_updatePath;
            pathArr = null;
            if (i35 != 5) {
                pathArr = Theme.chat_filePath;
            } else if (this.currentIcon == 5) {
                pathArr2 = Theme.chat_filePath;
            }
            pathArr3 = pathArr;
            pathArr4 = pathArr2;
            if (i35 != 7) {
                drawable2 = Theme.chat_flameIcon;
                drawable = null;
            } else {
                drawable = this.currentIcon == 7 ? Theme.chat_flameIcon : null;
                drawable2 = null;
            }
            if (i35 != 8) {
                drawable2 = Theme.chat_gifIcon;
            } else if (this.currentIcon == 8) {
                drawable = Theme.chat_gifIcon;
            }
            drawable3 = drawable;
            Drawable drawable7 = drawable2;
            if (this.currentIcon != 9 || i35 == 9) {
                applyShaderMatrix(z);
                this.paint.setAlpha(this.currentIcon != this.nextIcon ? 255 : (int) (this.transitionProgress * 255.0f));
                int dp16 = centerY4 + AndroidUtilities.dp(7.0f);
                int dp17 = centerX3 - AndroidUtilities.dp(3.0f);
                if (this.currentIcon == this.nextIcon) {
                    canvas.save();
                    float f65 = this.transitionProgress;
                    drawable4 = drawable7;
                    canvas.scale(f65, f65, centerX3, centerY4);
                } else {
                    drawable4 = drawable7;
                }
                float f66 = dp17;
                i7 = i;
                float f67 = dp16;
                drawable5 = drawable4;
                pathArr5 = pathArr3;
                drawable6 = drawable3;
                pathArr6 = pathArr4;
                canvas.drawLine(dp17 - AndroidUtilities.dp(6.0f), dp16 - AndroidUtilities.dp(6.0f), f66, f67, this.paint);
                canvas.drawLine(f66, f67, dp17 + AndroidUtilities.dp(12.0f), dp16 - AndroidUtilities.dp(12.0f), this.paint);
                if (this.currentIcon != this.nextIcon) {
                    canvas.restore();
                }
            } else {
                drawable5 = drawable7;
                pathArr6 = pathArr4;
                i7 = i;
                pathArr5 = pathArr3;
                drawable6 = drawable3;
            }
            if (this.currentIcon != 12 || this.nextIcon == 12) {
                applyShaderMatrix(false);
                i8 = this.currentIcon;
                i9 = this.nextIcon;
                if (i8 != i9) {
                    f25 = 1.0f;
                } else if (i9 == 13) {
                    f25 = this.transitionProgress;
                } else {
                    f25 = 1.0f - this.transitionProgress;
                }
                this.paint.setAlpha(i8 != i9 ? 255 : (int) (f25 * 255.0f));
                AndroidUtilities.dp(7.0f);
                AndroidUtilities.dp(3.0f);
                if (this.currentIcon != this.nextIcon) {
                    canvas.save();
                    canvas.scale(f25, f25, centerX3, centerY4);
                }
                float dp18 = AndroidUtilities.dp(7.0f) * this.scale;
                float f68 = centerX3;
                float f69 = f68 - dp18;
                float f70 = centerY4;
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
                i10 = this.currentIcon;
                i11 = this.nextIcon;
                if (i10 != i11) {
                    f26 = 1.0f;
                } else if (i11 == 13) {
                    f26 = this.transitionProgress;
                } else {
                    f26 = 1.0f - this.transitionProgress;
                }
                this.textPaint.setAlpha((int) (f26 * 255.0f));
                int dp19 = AndroidUtilities.dp(5.0f) + centerY4;
                int i36 = centerX3 - (this.percentStringWidth / 2);
                if (this.currentIcon != this.nextIcon) {
                    canvas.save();
                    canvas.scale(f26, f26, centerX3, centerY4);
                }
                i12 = (int) (this.animatedDownloadProgress * 100.0f);
                if (this.percentString != null || i12 != this.lastPercent) {
                    this.lastPercent = i12;
                    this.percentString = String.format("%d%%", Integer.valueOf(i12));
                    this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(format));
                }
                canvas.drawText(this.percentString, i36, dp19, this.textPaint);
                if (this.currentIcon != this.nextIcon) {
                    canvas.restore();
                }
            }
            i13 = this.currentIcon;
            if (i13 != 0 || i13 == 1 || (i18 = this.nextIcon) == 0 || i18 == 1) {
                if ((i13 == 0 || this.nextIcon != 1) && !(i13 == 1 && this.nextIcon == 0)) {
                    i14 = 1;
                } else if (this.animatingTransition) {
                    if (this.nextIcon == 0) {
                        f27 = 1.0f - this.transitionProgress;
                    } else {
                        f27 = this.transitionProgress;
                    }
                    i14 = 1;
                } else {
                    i14 = 1;
                }
                int i37 = this.nextIcon;
                if ((i37 == 0 || i37 == i14) && (i13 == 0 || i13 == i14)) {
                    i15 = 255;
                    this.paint2.setAlpha(255);
                    z2 = true;
                } else {
                    if (i37 == 4) {
                        this.paint2.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                    } else {
                        this.paint2.setAlpha(i13 == i37 ? 255 : (int) (this.transitionProgress * 255.0f));
                    }
                    z2 = true;
                    i15 = 255;
                }
                applyShaderMatrix(z2);
                canvas.save();
                canvas.translate(bounds.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f27)), bounds.centerY());
                float f74 = f27 * 500.0f;
                int i38 = this.currentIcon;
                float f75 = i38 == 1 ? 90.0f : 0.0f;
                if (i38 == 0 && this.nextIcon == 1) {
                    if (f74 < 384.0f) {
                        f75 = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f74 / 384.0f) * 95.0f;
                    } else {
                        f75 = f74 < 484.0f ? 95.0f - (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f74 - 384.0f) / 100.0f) * 5.0f) : 90.0f;
                    }
                    f74 += 100.0f;
                } else if (i38 == 1 && this.nextIcon == 0) {
                    if (f74 < 100.0f) {
                        f75 = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f74 / 100.0f) * (-5.0f);
                    } else {
                        f75 = f74 < 484.0f ? (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f74 - 100.0f) / 384.0f) * 95.0f) - 5.0f : 90.0f;
                    }
                }
                canvas.rotate(f75);
                int i39 = this.currentIcon;
                if ((i39 != 0 && i39 != 1) || i39 == 4) {
                    canvas.scale(f24, f24);
                }
                Theme.playPauseAnimator.draw(canvas, this.paint2, f74);
                canvas.scale(1.0f, -1.0f);
                Theme.playPauseAnimator.draw(canvas, this.paint2, f74);
                canvas.restore();
            } else {
                i15 = 255;
            }
            if (this.currentIcon == 6 || this.nextIcon == 6) {
                applyShaderMatrix(false);
                if (this.currentIcon != 6) {
                    float f76 = this.transitionProgress;
                    if (f76 > 0.5f) {
                        float f77 = (f76 - 0.5f) / 0.5f;
                        f28 = 1.0f - Math.min(1.0f, f77 / 0.5f);
                        f31 = f77 > 0.5f ? (f77 - 0.5f) / 0.5f : 0.0f;
                    } else {
                        f31 = 0.0f;
                        f28 = 1.0f;
                    }
                    this.paint.setAlpha(i15);
                    f29 = f31;
                } else {
                    if (this.nextIcon != 6) {
                        this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                    } else {
                        this.paint.setAlpha(i15);
                    }
                    f28 = 0.0f;
                    f29 = 1.0f;
                }
                int dp20 = centerY4 + AndroidUtilities.dp(7.0f);
                int dp21 = centerX3 - AndroidUtilities.dp(3.0f);
                if (f28 < 1.0f) {
                    i16 = dp21;
                    f30 = f24;
                    i17 = dp20;
                    canvas.drawLine(dp21 - AndroidUtilities.dp(6.0f), dp20 - AndroidUtilities.dp(6.0f), dp21 - (AndroidUtilities.dp(6.0f) * f28), dp20 - (AndroidUtilities.dp(6.0f) * f28), this.paint);
                } else {
                    i16 = dp21;
                    f30 = f24;
                    i17 = dp20;
                }
                if (f29 > 0.0f) {
                    float f78 = i16;
                    float f79 = i17;
                    canvas.drawLine(f78, f79, f78 + (AndroidUtilities.dp(12.0f) * f29), f79 - (AndroidUtilities.dp(12.0f) * f29), this.paint);
                }
            } else {
                f30 = f24;
            }
            if (drawable6 != null && drawable6 != drawable5) {
                int intrinsicWidth = (int) (drawable6.getIntrinsicWidth() * f23);
                int intrinsicHeight = (int) (drawable6.getIntrinsicHeight() * f23);
                drawable6.setColorFilter(this.colorFilter);
                drawable6.setAlpha(this.currentIcon == this.nextIcon ? 255 : (int) ((1.0f - this.transitionProgress) * 255.0f));
                int i40 = intrinsicWidth / 2;
                int i41 = intrinsicHeight / 2;
                drawable6.setBounds(centerX3 - i40, centerY4 - i41, i40 + centerX3, i41 + centerY4);
                drawable6.draw(canvas);
            }
            if (drawable5 != null) {
                int intrinsicWidth2 = (int) (drawable5.getIntrinsicWidth() * f30);
                int intrinsicHeight2 = (int) (drawable5.getIntrinsicHeight() * f30);
                drawable5.setColorFilter(this.colorFilter);
                drawable5.setAlpha(this.currentIcon == this.nextIcon ? 255 : (int) (this.transitionProgress * 255.0f));
                int i42 = intrinsicWidth2 / 2;
                int i43 = intrinsicHeight2 / 2;
                drawable5.setBounds(centerX3 - i42, centerY4 - i43, i42 + centerX3, i43 + centerY4);
                drawable5.draw(canvas);
            }
            Path[] pathArr7 = pathArr6;
            Path[] pathArr8 = pathArr5;
            if (pathArr7 != null && pathArr7 != pathArr8) {
                int dp22 = AndroidUtilities.dp(24.0f);
                this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                this.paint2.setAlpha(this.currentIcon == this.nextIcon ? 255 : (int) ((1.0f - this.transitionProgress) * 255.0f));
                applyShaderMatrix(true);
                canvas.save();
                canvas.translate(centerX3, centerY4);
                canvas.scale(f23, f23);
                float f80 = (-dp22) / 2;
                canvas.translate(f80, f80);
                if (pathArr7[0] != null) {
                    canvas.drawPath(pathArr7[0], this.paint2);
                }
                if (pathArr7[1] != null) {
                    canvas.drawPath(pathArr7[1], this.backPaint);
                }
                canvas.restore();
            }
            if (pathArr8 != null) {
                int dp23 = AndroidUtilities.dp(24.0f);
                int i44 = this.currentIcon == this.nextIcon ? 255 : (int) (this.transitionProgress * 255.0f);
                this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                this.paint2.setAlpha(i44);
                applyShaderMatrix(true);
                canvas.save();
                canvas.translate(centerX3, centerY4);
                float f81 = f30;
                canvas.scale(f81, f81);
                float f82 = (-dp23) / 2;
                canvas.translate(f82, f82);
                if (pathArr8[0] != null) {
                    canvas.drawPath(pathArr8[0], this.paint2);
                }
                if (pathArr8.length >= 3 && pathArr8[2] != null) {
                    canvas.drawPath(pathArr8[2], this.paint);
                }
                if (pathArr8[1] != null) {
                    if (i44 != 255) {
                        int alpha = this.backPaint.getAlpha();
                        this.backPaint.setAlpha((int) (alpha * (i44 / 255.0f)));
                        canvas.drawPath(pathArr8[1], this.backPaint);
                        this.backPaint.setAlpha(alpha);
                    } else {
                        canvas.drawPath(pathArr8[1], this.backPaint);
                    }
                }
                canvas.restore();
            }
            long currentTimeMillis = System.currentTimeMillis();
            long j = currentTimeMillis - this.lastAnimationTime;
            if (j > 17) {
                j = 17;
            }
            this.lastAnimationTime = currentTimeMillis;
            int i45 = this.currentIcon;
            if (i45 == 3 || i45 == 14 || ((i45 == 4 && this.nextIcon == 14) || i45 == 10 || i45 == 13)) {
                float f83 = this.downloadRadOffset + (((float) (360 * j)) / 2500.0f);
                this.downloadRadOffset = f83;
                this.downloadRadOffset = getCircleValue(f83);
                if (this.nextIcon != 2) {
                    float f84 = this.downloadProgress;
                    float f85 = this.downloadProgressAnimationStart;
                    float f86 = f84 - f85;
                    if (f86 > 0.0f) {
                        float f87 = this.downloadProgressTime + ((float) j);
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
                    float f89 = f88 + (((float) j) / this.transitionAnimationTime);
                    this.transitionProgress = f89;
                    if (f89 >= 1.0f) {
                        this.currentIcon = this.nextIcon;
                        this.transitionProgress = 1.0f;
                        this.animatingTransition = false;
                    }
                    invalidateSelf();
                }
            }
            int i46 = i7;
            if (i46 >= 1) {
                canvas.restoreToCount(i46);
                return;
            }
            return;
        } else {
            pathArr = null;
        }
        pathArr2 = null;
        if (i35 != 5) {
        }
        pathArr3 = pathArr;
        pathArr4 = pathArr2;
        if (i35 != 7) {
        }
        if (i35 != 8) {
        }
        drawable3 = drawable;
        Drawable drawable72 = drawable2;
        if (this.currentIcon != 9) {
        }
        applyShaderMatrix(z);
        this.paint.setAlpha(this.currentIcon != this.nextIcon ? 255 : (int) (this.transitionProgress * 255.0f));
        int dp162 = centerY4 + AndroidUtilities.dp(7.0f);
        int dp172 = centerX3 - AndroidUtilities.dp(3.0f);
        if (this.currentIcon == this.nextIcon) {
        }
        float f662 = dp172;
        i7 = i;
        float f672 = dp162;
        drawable5 = drawable4;
        pathArr5 = pathArr3;
        drawable6 = drawable3;
        pathArr6 = pathArr4;
        canvas.drawLine(dp172 - AndroidUtilities.dp(6.0f), dp162 - AndroidUtilities.dp(6.0f), f662, f672, this.paint);
        canvas.drawLine(f662, f672, dp172 + AndroidUtilities.dp(12.0f), dp162 - AndroidUtilities.dp(12.0f), this.paint);
        if (this.currentIcon != this.nextIcon) {
        }
        if (this.currentIcon != 12) {
        }
        applyShaderMatrix(false);
        i8 = this.currentIcon;
        i9 = this.nextIcon;
        if (i8 != i9) {
        }
        this.paint.setAlpha(i8 != i9 ? 255 : (int) (f25 * 255.0f));
        AndroidUtilities.dp(7.0f);
        AndroidUtilities.dp(3.0f);
        if (this.currentIcon != this.nextIcon) {
        }
        float dp182 = AndroidUtilities.dp(7.0f) * this.scale;
        float f682 = centerX3;
        float f692 = f682 - dp182;
        float f702 = centerY4;
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
        i10 = this.currentIcon;
        i11 = this.nextIcon;
        if (i10 != i11) {
        }
        this.textPaint.setAlpha((int) (f26 * 255.0f));
        int dp192 = AndroidUtilities.dp(5.0f) + centerY4;
        int i362 = centerX3 - (this.percentStringWidth / 2);
        if (this.currentIcon != this.nextIcon) {
        }
        i12 = (int) (this.animatedDownloadProgress * 100.0f);
        if (this.percentString != null) {
        }
        this.lastPercent = i12;
        this.percentString = String.format("%d%%", Integer.valueOf(i12));
        this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(format));
        canvas.drawText(this.percentString, i362, dp192, this.textPaint);
        if (this.currentIcon != this.nextIcon) {
        }
        i13 = this.currentIcon;
        if (i13 != 0) {
        }
        if (i13 == 0) {
        }
        i14 = 1;
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
