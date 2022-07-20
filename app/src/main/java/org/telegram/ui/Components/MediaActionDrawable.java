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
        this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
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

    /* JADX WARN: Code restructure failed: missing block: B:341:0x0885, code lost:
        if (r33.nextIcon == 1) goto L342;
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x0887, code lost:
        r4 = 1.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x088a, code lost:
        r4 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x088d, code lost:
        if (r1 != 1) goto L343;
     */
    /* JADX WARN: Removed duplicated region for block: B:182:0x050f  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x051a  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x0524  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x05a2  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x05aa  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x05b3  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x05bb  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x05d8  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x05db  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x05fc  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x067f  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x0682  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x068e  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x0694  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x06a3  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x06a6  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x06d3  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x06d6  */
    /* JADX WARN: Removed duplicated region for block: B:273:0x06f2  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x06ff  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x074c  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x0765  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x0768  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x077a  */
    /* JADX WARN: Removed duplicated region for block: B:292:0x077d  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x0791  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x07c8  */
    /* JADX WARN: Removed duplicated region for block: B:305:0x07df  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x07e2  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x0808  */
    /* JADX WARN: Removed duplicated region for block: B:319:0x084e  */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        int i;
        boolean z;
        float f;
        float f2;
        Path[] pathArr;
        Path[] pathArr2;
        Path[] pathArr3;
        Path[] pathArr4;
        Drawable drawable;
        Drawable drawable2;
        Drawable drawable3;
        Path[] pathArr5;
        int i2;
        Path[] pathArr6;
        Drawable drawable4;
        Drawable drawable5;
        int i3;
        int i4;
        float f3;
        float f4;
        float f5;
        int i5;
        int i6;
        float f6;
        float f7;
        int i7;
        boolean z2;
        int i8;
        int i9;
        int i10;
        float f8;
        int i11;
        String format;
        int i12;
        int i13;
        float f9;
        Drawable drawable6;
        float f10;
        float f11;
        float f12;
        int i14;
        float f13;
        float f14;
        float f15;
        float f16;
        float f17;
        float f18;
        int i15;
        int i16;
        int i17;
        int i18;
        float f19;
        float f20;
        int i19;
        float f21;
        float f22;
        int i20;
        float f23;
        int i21;
        int i22;
        float f24;
        float f25;
        float f26;
        float f27;
        float f28;
        float f29;
        float f30;
        float f31;
        float f32;
        float f33;
        float f34;
        float f35;
        float f36;
        float f37;
        float f38;
        int i23;
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
        int centerX = bounds.centerX();
        int centerY = bounds.centerY();
        int i24 = this.nextIcon;
        if (i24 == 4) {
            int i25 = this.currentIcon;
            if (i25 != 3 && i25 != 14) {
                i23 = canvas.save();
                float f39 = 1.0f - this.transitionProgress;
                canvas.scale(f39, f39, centerX, centerY);
                i = i23;
            }
            i = 0;
        } else {
            if ((i24 == 6 || i24 == 10) && this.currentIcon == 4) {
                i23 = canvas.save();
                float f40 = this.transitionProgress;
                canvas.scale(f40, f40, centerX, centerY);
                i = i23;
            }
            i = 0;
        }
        AndroidUtilities.dp(3.0f);
        if (this.currentIcon == 2 || this.nextIcon == 2) {
            applyShaderMatrix(false);
            float f41 = centerY;
            float dp = f41 - (AndroidUtilities.dp(9.0f) * this.scale);
            float dp2 = (AndroidUtilities.dp(9.0f) * this.scale) + f41;
            float dp3 = (AndroidUtilities.dp(12.0f) * this.scale) + f41;
            int i26 = this.currentIcon;
            if ((i26 == 3 || i26 == 14) && this.nextIcon == 2) {
                this.paint.setAlpha((int) (Math.min(1.0f, this.transitionProgress / 0.5f) * 255.0f));
                f25 = this.transitionProgress;
                f24 = (AndroidUtilities.dp(12.0f) * this.scale) + f41;
            } else {
                int i27 = this.nextIcon;
                if (i27 != 3 && i27 != 14 && i27 != 2) {
                    this.paint.setAlpha((int) (Math.min(1.0f, this.savedTransitionProgress / 0.5f) * 255.0f * (1.0f - this.transitionProgress)));
                    f25 = this.savedTransitionProgress;
                } else {
                    this.paint.setAlpha(255);
                    f25 = this.transitionProgress;
                }
                f24 = f41 + (AndroidUtilities.dp(1.0f) * this.scale);
            }
            if (this.animatingTransition) {
                int i28 = this.nextIcon;
                if (i28 == 2 || f25 <= 0.5f) {
                    if (i28 == 2) {
                        f33 = 1.0f - f25;
                    } else {
                        f33 = f25 / 0.5f;
                        f25 = 1.0f - f33;
                    }
                    f29 = dp + ((f24 - dp) * f33);
                    float f42 = ((dp3 - dp2) * f33) + dp2;
                    float f43 = centerX;
                    f30 = f43 - ((AndroidUtilities.dp(8.0f) * f25) * this.scale);
                    f32 = f43 + (AndroidUtilities.dp(8.0f) * f25 * this.scale);
                    dp3 = f42 - ((AndroidUtilities.dp(8.0f) * f25) * this.scale);
                    f31 = f42;
                } else {
                    float f44 = this.scale;
                    float dp4 = (AndroidUtilities.dp(13.0f) * f44 * f44) + (this.isMini ? AndroidUtilities.dp(2.0f) : 0);
                    float f45 = f25 - 0.5f;
                    float f46 = f45 / 0.5f;
                    if (f45 > 0.2f) {
                        f34 = (f45 - 0.2f) / 0.3f;
                        f35 = 1.0f;
                    } else {
                        f35 = f45 / 0.2f;
                        f34 = 0.0f;
                    }
                    float f47 = centerX;
                    float f48 = f47 - dp4;
                    float f49 = dp4 / 2.0f;
                    this.rect.set(f48, dp3 - f49, f47, f49 + dp3);
                    float f50 = f34 * 100.0f;
                    canvas.drawArc(this.rect, f50, (f46 * 104.0f) - f50, false, this.paint);
                    f29 = f24 + ((dp3 - f24) * f35);
                    if (f34 > 0.0f) {
                        float f51 = this.nextIcon == 14 ? 0.0f : (-45.0f) * (1.0f - f34);
                        float dp5 = AndroidUtilities.dp(7.0f) * f34 * this.scale;
                        int i29 = (int) (f34 * 255.0f);
                        int i30 = this.nextIcon;
                        if (i30 != 3 && i30 != 14 && i30 != 2) {
                            i29 = (int) (i29 * (1.0f - Math.min(1.0f, this.transitionProgress / 0.5f)));
                        }
                        int i31 = i29;
                        if (f51 != 0.0f) {
                            canvas.save();
                            f38 = f41;
                            f37 = f47;
                            canvas.rotate(f51, f37, f38);
                        } else {
                            f38 = f41;
                            f37 = f47;
                        }
                        if (i31 != 0) {
                            this.paint.setAlpha(i31);
                            if (this.nextIcon == 14) {
                                this.paint3.setAlpha(i31);
                                this.rect.set(centerX - AndroidUtilities.dp(3.5f), centerY - AndroidUtilities.dp(3.5f), AndroidUtilities.dp(3.5f) + centerX, centerY + AndroidUtilities.dp(3.5f));
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint3);
                                this.paint.setAlpha((int) (i31 * 0.15f));
                                int dp6 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                                this.rect.set(bounds.left + dp6, bounds.top + dp6, bounds.right - dp6, bounds.bottom - dp6);
                                f36 = f37;
                                canvas.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                                this.paint.setAlpha(i31);
                            } else {
                                f36 = f37;
                                float f52 = f36 - dp5;
                                float f53 = f38 - dp5;
                                float f54 = f36 + dp5;
                                float f55 = f38 + dp5;
                                canvas.drawLine(f52, f53, f54, f55, this.paint);
                                canvas.drawLine(f54, f53, f52, f55, this.paint);
                            }
                        } else {
                            f36 = f37;
                        }
                        if (f51 != 0.0f) {
                            canvas.restore();
                        }
                    } else {
                        f36 = f47;
                    }
                    f31 = dp3;
                    f32 = f36;
                    f30 = f32;
                }
                f27 = f32;
                f26 = f31;
                f28 = f30;
                dp = f29;
            } else {
                float f56 = centerX;
                float dp7 = f56 - (AndroidUtilities.dp(8.0f) * this.scale);
                f27 = f56 + (AndroidUtilities.dp(8.0f) * this.scale);
                f28 = dp7;
                dp3 = dp2 - (AndroidUtilities.dp(8.0f) * this.scale);
                f26 = dp2;
            }
            if (dp != f26) {
                float f57 = centerX;
                canvas.drawLine(f57, dp, f57, f26, this.paint);
            }
            float f58 = centerX;
            if (f28 != f58) {
                float f59 = dp3;
                float f60 = f26;
                canvas.drawLine(f28, f59, f58, f60, this.paint);
                canvas.drawLine(f27, f59, f58, f60, this.paint);
            }
        }
        int i32 = this.currentIcon;
        if (i32 == 3 || i32 == 14 || (i32 == 4 && ((i22 = this.nextIcon) == 14 || i22 == 3))) {
            z = false;
            applyShaderMatrix(false);
            int i33 = this.nextIcon;
            if (i33 == 2) {
                float f61 = this.transitionProgress;
                if (f61 <= 0.5f) {
                    float f62 = 1.0f - (f61 / 0.5f);
                    f17 = AndroidUtilities.dp(7.0f) * f62 * this.scale;
                    i14 = (int) (f62 * 255.0f);
                } else {
                    f17 = 0.0f;
                    i14 = 0;
                }
                f16 = 1.0f;
                f15 = 0.0f;
                f14 = 0.0f;
                f13 = 1.0f;
            } else {
                if (i33 == 15 || i33 == 0 || i33 == 1 || i33 == 5 || i33 == 8 || i33 == 9 || i33 == 7) {
                    i19 = 6;
                } else {
                    i19 = 6;
                    if (i33 != 6) {
                        if (i33 == 4) {
                            f20 = 1.0f - this.transitionProgress;
                            f17 = AndroidUtilities.dp(7.0f) * this.scale;
                            int i34 = (int) (f20 * 255.0f);
                            if (this.currentIcon == 14) {
                                f15 = bounds.left;
                                i21 = bounds.top;
                            } else {
                                f15 = bounds.centerX();
                                i21 = bounds.centerY();
                            }
                            i14 = i34;
                            f16 = 1.0f;
                            f12 = 0.0f;
                            f19 = i21;
                        } else if (i33 == 14 || i33 == 3) {
                            float f63 = this.transitionProgress;
                            float f64 = 1.0f - f63;
                            if (this.currentIcon == 4) {
                                f20 = f63;
                                f22 = 0.0f;
                            } else {
                                f22 = f64 * 45.0f;
                                f20 = 1.0f;
                            }
                            float dp8 = AndroidUtilities.dp(7.0f) * this.scale;
                            int i35 = (int) (f63 * 255.0f);
                            if (this.nextIcon == 14) {
                                f23 = bounds.left;
                                i20 = bounds.top;
                            } else {
                                f23 = bounds.centerX();
                                i20 = bounds.centerY();
                            }
                            f12 = f22;
                            f16 = 1.0f;
                            f19 = i20;
                            i14 = i35;
                            f17 = dp8;
                            f15 = f23;
                        } else {
                            f17 = AndroidUtilities.dp(7.0f) * this.scale;
                            f16 = 1.0f;
                            f15 = 0.0f;
                            f14 = 0.0f;
                            f13 = 1.0f;
                            i14 = 255;
                        }
                        f13 = f20;
                        f14 = f19;
                        if (f13 != f16) {
                            canvas.save();
                            canvas.scale(f13, f13, f15, f14);
                        }
                        if (f12 != 0.0f) {
                            canvas.save();
                            canvas.rotate(f12, centerX, centerY);
                        }
                        if (i14 != 0) {
                            float f65 = i14;
                            this.paint.setAlpha((int) (this.overrideAlpha * f65));
                            if (this.currentIcon == 14 || this.nextIcon == 14) {
                                f18 = f13;
                                i15 = i14;
                                this.paint3.setAlpha((int) (f65 * this.overrideAlpha));
                                this.rect.set(centerX - AndroidUtilities.dp(3.5f), centerY - AndroidUtilities.dp(3.5f), AndroidUtilities.dp(3.5f) + centerX, AndroidUtilities.dp(3.5f) + centerY);
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint3);
                            } else {
                                float f66 = centerX;
                                float f67 = f66 - f17;
                                float f68 = centerY;
                                float f69 = f68 - f17;
                                float f70 = f66 + f17;
                                float f71 = f68 + f17;
                                f18 = f13;
                                i15 = i14;
                                canvas.drawLine(f67, f69, f70, f71, this.paint);
                                canvas.drawLine(f70, f69, f67, f71, this.paint);
                            }
                        } else {
                            f18 = f13;
                            i15 = i14;
                        }
                        if (f12 != 0.0f) {
                            canvas.restore();
                        }
                        if (f18 != 1.0f) {
                            canvas.restore();
                        }
                        i16 = this.currentIcon;
                        if ((i16 != 3 || i16 == 14 || (i16 == 4 && ((i18 = this.nextIcon) == 14 || i18 == 3))) && i15 != 0) {
                            float max = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                            int dp9 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                            this.rect.set(bounds.left + dp9, bounds.top + dp9, bounds.right - dp9, bounds.bottom - dp9);
                            i17 = this.currentIcon;
                            if (i17 != 14 || (i17 == 4 && this.nextIcon == 14)) {
                                this.paint.setAlpha((int) (i15 * 0.15f * this.overrideAlpha));
                                canvas.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                                this.paint.setAlpha(i15);
                            }
                            canvas.drawArc(this.rect, this.downloadRadOffset, max, false, this.paint);
                        }
                    }
                }
                if (i33 == i19) {
                    f16 = 1.0f;
                    f21 = Math.min(1.0f, this.transitionProgress / 0.5f);
                } else {
                    f16 = 1.0f;
                    f21 = this.transitionProgress;
                }
                f20 = f16 - f21;
                float dp10 = AndroidUtilities.dp(7.0f) * f20 * this.scale;
                i14 = (int) (Math.min(f16, f20 * 2.0f) * 255.0f);
                f12 = 0.0f;
                f19 = bounds.centerY();
                f15 = bounds.centerX();
                f17 = dp10;
                f13 = f20;
                f14 = f19;
                if (f13 != f16) {
                }
                if (f12 != 0.0f) {
                }
                if (i14 != 0) {
                }
                if (f12 != 0.0f) {
                }
                if (f18 != 1.0f) {
                }
                i16 = this.currentIcon;
                if (i16 != 3) {
                }
                float max2 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                int dp92 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                this.rect.set(bounds.left + dp92, bounds.top + dp92, bounds.right - dp92, bounds.bottom - dp92);
                i17 = this.currentIcon;
                if (i17 != 14) {
                }
                this.paint.setAlpha((int) (i15 * 0.15f * this.overrideAlpha));
                canvas.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                this.paint.setAlpha(i15);
                canvas.drawArc(this.rect, this.downloadRadOffset, max2, false, this.paint);
            }
            f12 = 0.0f;
            if (f13 != f16) {
            }
            if (f12 != 0.0f) {
            }
            if (i14 != 0) {
            }
            if (f12 != 0.0f) {
            }
            if (f18 != 1.0f) {
            }
            i16 = this.currentIcon;
            if (i16 != 3) {
            }
            float max22 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
            int dp922 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
            this.rect.set(bounds.left + dp922, bounds.top + dp922, bounds.right - dp922, bounds.bottom - dp922);
            i17 = this.currentIcon;
            if (i17 != 14) {
            }
            this.paint.setAlpha((int) (i15 * 0.15f * this.overrideAlpha));
            canvas.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
            this.paint.setAlpha(i15);
            canvas.drawArc(this.rect, this.downloadRadOffset, max22, false, this.paint);
        } else {
            if (i32 == 10 || this.nextIcon == 10 || i32 == 13) {
                int i36 = this.nextIcon;
                int i37 = (i36 == 4 || i36 == 6) ? (int) ((1.0f - this.transitionProgress) * 255.0f) : 255;
                if (i37 != 0) {
                    z = false;
                    applyShaderMatrix(false);
                    this.paint.setAlpha((int) (i37 * this.overrideAlpha));
                    float max3 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                    int dp11 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                    this.rect.set(bounds.left + dp11, bounds.top + dp11, bounds.right - dp11, bounds.bottom - dp11);
                    canvas.drawArc(this.rect, this.downloadRadOffset, max3, false, this.paint);
                }
            }
            z = false;
        }
        int i38 = this.currentIcon;
        if (i38 == this.nextIcon) {
            f2 = 1.0f;
            f = 1.0f;
        } else {
            if (i38 == 4 || i38 == 3 || i38 == 14) {
                f10 = this.transitionProgress;
                f11 = 1.0f - f10;
            } else {
                f10 = Math.min(1.0f, this.transitionProgress / 0.5f);
                f11 = Math.max(0.0f, 1.0f - (this.transitionProgress / 0.5f));
            }
            f = f11;
            f2 = f10;
        }
        int i39 = this.nextIcon;
        if (i39 == 15) {
            pathArr2 = Theme.chat_updatePath;
        } else if (this.currentIcon == 15) {
            pathArr = Theme.chat_updatePath;
            pathArr2 = null;
            if (i39 != 5) {
                pathArr2 = Theme.chat_filePath;
            } else if (this.currentIcon == 5) {
                pathArr = Theme.chat_filePath;
            }
            pathArr3 = pathArr2;
            pathArr4 = pathArr;
            if (i39 != 7) {
                drawable = Theme.chat_flameIcon;
                drawable2 = null;
            } else {
                drawable2 = this.currentIcon == 7 ? Theme.chat_flameIcon : null;
                drawable = null;
            }
            if (i39 != 8) {
                drawable = Theme.chat_gifIcon;
            } else if (this.currentIcon == 8) {
                drawable2 = Theme.chat_gifIcon;
            }
            drawable3 = drawable2;
            Drawable drawable7 = drawable;
            if (this.currentIcon != 9 || i39 == 9) {
                applyShaderMatrix(z);
                this.paint.setAlpha(this.currentIcon != this.nextIcon ? 255 : (int) (this.transitionProgress * 255.0f));
                int dp12 = centerY + AndroidUtilities.dp(7.0f);
                int dp13 = centerX - AndroidUtilities.dp(3.0f);
                if (this.currentIcon == this.nextIcon) {
                    canvas.save();
                    float f72 = this.transitionProgress;
                    drawable6 = drawable7;
                    canvas.scale(f72, f72, centerX, centerY);
                } else {
                    drawable6 = drawable7;
                }
                float f73 = dp13;
                i2 = i;
                float f74 = dp12;
                drawable5 = drawable6;
                pathArr6 = pathArr3;
                drawable4 = drawable3;
                pathArr5 = pathArr4;
                canvas.drawLine(dp13 - AndroidUtilities.dp(6.0f), dp12 - AndroidUtilities.dp(6.0f), f73, f74, this.paint);
                canvas.drawLine(f73, f74, dp13 + AndroidUtilities.dp(12.0f), dp12 - AndroidUtilities.dp(12.0f), this.paint);
                if (this.currentIcon != this.nextIcon) {
                    canvas.restore();
                }
            } else {
                drawable5 = drawable7;
                pathArr5 = pathArr4;
                i2 = i;
                pathArr6 = pathArr3;
                drawable4 = drawable3;
            }
            if (this.currentIcon != 12 || this.nextIcon == 12) {
                applyShaderMatrix(false);
                i12 = this.currentIcon;
                i13 = this.nextIcon;
                if (i12 != i13) {
                    f9 = 1.0f;
                } else if (i13 == 13) {
                    f9 = this.transitionProgress;
                } else {
                    f9 = 1.0f - this.transitionProgress;
                }
                this.paint.setAlpha(i12 != i13 ? 255 : (int) (f9 * 255.0f));
                AndroidUtilities.dp(7.0f);
                AndroidUtilities.dp(3.0f);
                if (this.currentIcon != this.nextIcon) {
                    canvas.save();
                    canvas.scale(f9, f9, centerX, centerY);
                }
                float dp14 = AndroidUtilities.dp(7.0f) * this.scale;
                float f75 = centerX;
                float f76 = f75 - dp14;
                float f77 = centerY;
                float f78 = f77 - dp14;
                float f79 = f75 + dp14;
                float f80 = f77 + dp14;
                canvas.drawLine(f76, f78, f79, f80, this.paint);
                canvas.drawLine(f79, f78, f76, f80, this.paint);
                if (this.currentIcon != this.nextIcon) {
                    canvas.restore();
                }
            }
            if (this.currentIcon != 13 || this.nextIcon == 13) {
                applyShaderMatrix(false);
                i9 = this.currentIcon;
                i10 = this.nextIcon;
                if (i9 != i10) {
                    f8 = 1.0f;
                } else if (i10 == 13) {
                    f8 = this.transitionProgress;
                } else {
                    f8 = 1.0f - this.transitionProgress;
                }
                this.textPaint.setAlpha((int) (f8 * 255.0f));
                int dp15 = AndroidUtilities.dp(5.0f) + centerY;
                int i40 = centerX - (this.percentStringWidth / 2);
                if (this.currentIcon != this.nextIcon) {
                    canvas.save();
                    canvas.scale(f8, f8, centerX, centerY);
                }
                i11 = (int) (this.animatedDownloadProgress * 100.0f);
                if (this.percentString != null || i11 != this.lastPercent) {
                    this.lastPercent = i11;
                    this.percentString = String.format("%d%%", Integer.valueOf(i11));
                    this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(format));
                }
                canvas.drawText(this.percentString, i40, dp15, this.textPaint);
                if (this.currentIcon != this.nextIcon) {
                    canvas.restore();
                }
            }
            i3 = this.currentIcon;
            if (i3 != 0 || i3 == 1 || (i8 = this.nextIcon) == 0 || i8 == 1) {
                if ((i3 == 0 || this.nextIcon != 1) && !(i3 == 1 && this.nextIcon == 0)) {
                    i7 = 1;
                } else if (this.animatingTransition) {
                    if (this.nextIcon == 0) {
                        f7 = 1.0f - this.transitionProgress;
                    } else {
                        f7 = this.transitionProgress;
                    }
                    i7 = 1;
                } else {
                    i7 = 1;
                }
                int i41 = this.nextIcon;
                if ((i41 == 0 || i41 == i7) && (i3 == 0 || i3 == i7)) {
                    i4 = 255;
                    this.paint2.setAlpha(255);
                    z2 = true;
                } else {
                    if (i41 == 4) {
                        this.paint2.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                    } else {
                        this.paint2.setAlpha(i3 == i41 ? 255 : (int) (this.transitionProgress * 255.0f));
                    }
                    z2 = true;
                    i4 = 255;
                }
                applyShaderMatrix(z2);
                canvas.save();
                canvas.translate(bounds.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - f7)), bounds.centerY());
                float f81 = f7 * 500.0f;
                int i42 = this.currentIcon;
                float f82 = i42 == 1 ? 90.0f : 0.0f;
                if (i42 == 0 && this.nextIcon == 1) {
                    if (f81 < 384.0f) {
                        f82 = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f81 / 384.0f) * 95.0f;
                    } else {
                        f82 = f81 < 484.0f ? 95.0f - (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f81 - 384.0f) / 100.0f) * 5.0f) : 90.0f;
                    }
                    f81 += 100.0f;
                } else if (i42 == 1 && this.nextIcon == 0) {
                    if (f81 < 100.0f) {
                        f82 = CubicBezierInterpolator.EASE_BOTH.getInterpolation(f81 / 100.0f) * (-5.0f);
                    } else {
                        f82 = f81 < 484.0f ? (CubicBezierInterpolator.EASE_BOTH.getInterpolation((f81 - 100.0f) / 384.0f) * 95.0f) - 5.0f : 90.0f;
                    }
                }
                canvas.rotate(f82);
                int i43 = this.currentIcon;
                if ((i43 != 0 && i43 != 1) || i43 == 4) {
                    canvas.scale(f2, f2);
                }
                Theme.playPauseAnimator.draw(canvas, this.paint2, f81);
                canvas.scale(1.0f, -1.0f);
                Theme.playPauseAnimator.draw(canvas, this.paint2, f81);
                canvas.restore();
            } else {
                i4 = 255;
            }
            if (this.currentIcon == 6 || this.nextIcon == 6) {
                applyShaderMatrix(false);
                if (this.currentIcon != 6) {
                    float f83 = this.transitionProgress;
                    if (f83 > 0.5f) {
                        float f84 = (f83 - 0.5f) / 0.5f;
                        f5 = 1.0f - Math.min(1.0f, f84 / 0.5f);
                        f6 = f84 > 0.5f ? (f84 - 0.5f) / 0.5f : 0.0f;
                    } else {
                        f6 = 0.0f;
                        f5 = 1.0f;
                    }
                    this.paint.setAlpha(i4);
                    f4 = f6;
                } else {
                    if (this.nextIcon != 6) {
                        this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                    } else {
                        this.paint.setAlpha(i4);
                    }
                    f5 = 0.0f;
                    f4 = 1.0f;
                }
                int dp16 = centerY + AndroidUtilities.dp(7.0f);
                int dp17 = centerX - AndroidUtilities.dp(3.0f);
                if (f5 < 1.0f) {
                    i6 = dp17;
                    f3 = f2;
                    i5 = dp16;
                    canvas.drawLine(dp17 - AndroidUtilities.dp(6.0f), dp16 - AndroidUtilities.dp(6.0f), dp17 - (AndroidUtilities.dp(6.0f) * f5), dp16 - (AndroidUtilities.dp(6.0f) * f5), this.paint);
                } else {
                    i6 = dp17;
                    f3 = f2;
                    i5 = dp16;
                }
                if (f4 > 0.0f) {
                    float f85 = i6;
                    float f86 = i5;
                    canvas.drawLine(f85, f86, f85 + (AndroidUtilities.dp(12.0f) * f4), f86 - (AndroidUtilities.dp(12.0f) * f4), this.paint);
                }
            } else {
                f3 = f2;
            }
            if (drawable4 != null && drawable4 != drawable5) {
                int intrinsicWidth = (int) (drawable4.getIntrinsicWidth() * f);
                int intrinsicHeight = (int) (drawable4.getIntrinsicHeight() * f);
                drawable4.setColorFilter(this.colorFilter);
                drawable4.setAlpha(this.currentIcon == this.nextIcon ? 255 : (int) ((1.0f - this.transitionProgress) * 255.0f));
                int i44 = intrinsicWidth / 2;
                int i45 = intrinsicHeight / 2;
                drawable4.setBounds(centerX - i44, centerY - i45, i44 + centerX, i45 + centerY);
                drawable4.draw(canvas);
            }
            if (drawable5 != null) {
                int intrinsicWidth2 = (int) (drawable5.getIntrinsicWidth() * f3);
                int intrinsicHeight2 = (int) (drawable5.getIntrinsicHeight() * f3);
                drawable5.setColorFilter(this.colorFilter);
                drawable5.setAlpha(this.currentIcon == this.nextIcon ? 255 : (int) (this.transitionProgress * 255.0f));
                int i46 = intrinsicWidth2 / 2;
                int i47 = intrinsicHeight2 / 2;
                drawable5.setBounds(centerX - i46, centerY - i47, i46 + centerX, i47 + centerY);
                drawable5.draw(canvas);
            }
            Path[] pathArr7 = pathArr5;
            Path[] pathArr8 = pathArr6;
            if (pathArr7 != null && pathArr7 != pathArr8) {
                int dp18 = AndroidUtilities.dp(24.0f);
                this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                this.paint2.setAlpha(this.currentIcon == this.nextIcon ? 255 : (int) ((1.0f - this.transitionProgress) * 255.0f));
                applyShaderMatrix(true);
                canvas.save();
                canvas.translate(centerX, centerY);
                canvas.scale(f, f);
                float f87 = (-dp18) / 2;
                canvas.translate(f87, f87);
                if (pathArr7[0] != null) {
                    canvas.drawPath(pathArr7[0], this.paint2);
                }
                if (pathArr7[1] != null) {
                    canvas.drawPath(pathArr7[1], this.backPaint);
                }
                canvas.restore();
            }
            if (pathArr8 != null) {
                int dp19 = AndroidUtilities.dp(24.0f);
                int i48 = this.currentIcon == this.nextIcon ? 255 : (int) (this.transitionProgress * 255.0f);
                this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                this.paint2.setAlpha(i48);
                applyShaderMatrix(true);
                canvas.save();
                canvas.translate(centerX, centerY);
                float f88 = f3;
                canvas.scale(f88, f88);
                float f89 = (-dp19) / 2;
                canvas.translate(f89, f89);
                if (pathArr8[0] != null) {
                    canvas.drawPath(pathArr8[0], this.paint2);
                }
                if (pathArr8.length >= 3 && pathArr8[2] != null) {
                    canvas.drawPath(pathArr8[2], this.paint);
                }
                if (pathArr8[1] != null) {
                    if (i48 != 255) {
                        int alpha = this.backPaint.getAlpha();
                        this.backPaint.setAlpha((int) (alpha * (i48 / 255.0f)));
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
            int i49 = this.currentIcon;
            if (i49 == 3 || i49 == 14 || ((i49 == 4 && this.nextIcon == 14) || i49 == 10 || i49 == 13)) {
                float f90 = this.downloadRadOffset + (((float) (360 * j)) / 2500.0f);
                this.downloadRadOffset = f90;
                this.downloadRadOffset = getCircleValue(f90);
                if (this.nextIcon != 2) {
                    float f91 = this.downloadProgress;
                    float f92 = this.downloadProgressAnimationStart;
                    float f93 = f91 - f92;
                    if (f93 > 0.0f) {
                        float f94 = this.downloadProgressTime + ((float) j);
                        this.downloadProgressTime = f94;
                        if (f94 >= 200.0f) {
                            this.animatedDownloadProgress = f91;
                            this.downloadProgressAnimationStart = f91;
                            this.downloadProgressTime = 0.0f;
                        } else {
                            this.animatedDownloadProgress = f92 + (f93 * this.interpolator.getInterpolation(f94 / 200.0f));
                        }
                    }
                }
                invalidateSelf();
            }
            if (this.animatingTransition) {
                float f95 = this.transitionProgress;
                if (f95 < 1.0f) {
                    float f96 = f95 + (((float) j) / this.transitionAnimationTime);
                    this.transitionProgress = f96;
                    if (f96 >= 1.0f) {
                        this.currentIcon = this.nextIcon;
                        this.transitionProgress = 1.0f;
                        this.animatingTransition = false;
                    }
                    invalidateSelf();
                }
            }
            int i50 = i2;
            if (i50 < 1) {
                return;
            }
            canvas.restoreToCount(i50);
            return;
        } else {
            pathArr2 = null;
        }
        pathArr = null;
        if (i39 != 5) {
        }
        pathArr3 = pathArr2;
        pathArr4 = pathArr;
        if (i39 != 7) {
        }
        if (i39 != 8) {
        }
        drawable3 = drawable2;
        Drawable drawable72 = drawable;
        if (this.currentIcon != 9) {
        }
        applyShaderMatrix(z);
        this.paint.setAlpha(this.currentIcon != this.nextIcon ? 255 : (int) (this.transitionProgress * 255.0f));
        int dp122 = centerY + AndroidUtilities.dp(7.0f);
        int dp132 = centerX - AndroidUtilities.dp(3.0f);
        if (this.currentIcon == this.nextIcon) {
        }
        float f732 = dp132;
        i2 = i;
        float f742 = dp122;
        drawable5 = drawable6;
        pathArr6 = pathArr3;
        drawable4 = drawable3;
        pathArr5 = pathArr4;
        canvas.drawLine(dp132 - AndroidUtilities.dp(6.0f), dp122 - AndroidUtilities.dp(6.0f), f732, f742, this.paint);
        canvas.drawLine(f732, f742, dp132 + AndroidUtilities.dp(12.0f), dp122 - AndroidUtilities.dp(12.0f), this.paint);
        if (this.currentIcon != this.nextIcon) {
        }
        if (this.currentIcon != 12) {
        }
        applyShaderMatrix(false);
        i12 = this.currentIcon;
        i13 = this.nextIcon;
        if (i12 != i13) {
        }
        this.paint.setAlpha(i12 != i13 ? 255 : (int) (f9 * 255.0f));
        AndroidUtilities.dp(7.0f);
        AndroidUtilities.dp(3.0f);
        if (this.currentIcon != this.nextIcon) {
        }
        float dp142 = AndroidUtilities.dp(7.0f) * this.scale;
        float f752 = centerX;
        float f762 = f752 - dp142;
        float f772 = centerY;
        float f782 = f772 - dp142;
        float f792 = f752 + dp142;
        float f802 = f772 + dp142;
        canvas.drawLine(f762, f782, f792, f802, this.paint);
        canvas.drawLine(f792, f782, f762, f802, this.paint);
        if (this.currentIcon != this.nextIcon) {
        }
        if (this.currentIcon != 13) {
        }
        applyShaderMatrix(false);
        i9 = this.currentIcon;
        i10 = this.nextIcon;
        if (i9 != i10) {
        }
        this.textPaint.setAlpha((int) (f8 * 255.0f));
        int dp152 = AndroidUtilities.dp(5.0f) + centerY;
        int i402 = centerX - (this.percentStringWidth / 2);
        if (this.currentIcon != this.nextIcon) {
        }
        i11 = (int) (this.animatedDownloadProgress * 100.0f);
        if (this.percentString != null) {
        }
        this.lastPercent = i11;
        this.percentString = String.format("%d%%", Integer.valueOf(i11));
        this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(format));
        canvas.drawText(this.percentString, i402, dp152, this.textPaint);
        if (this.currentIcon != this.nextIcon) {
        }
        i3 = this.currentIcon;
        if (i3 != 0) {
        }
        if (i3 == 0) {
        }
        i7 = 1;
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
