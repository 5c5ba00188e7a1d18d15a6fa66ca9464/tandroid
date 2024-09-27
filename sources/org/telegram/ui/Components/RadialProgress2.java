package org.telegram.ui.Components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import java.util.Locale;
import java.util.Objects;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.MediaActionDrawable;
/* loaded from: classes3.dex */
public class RadialProgress2 {
    private int backgroundStroke;
    private float circleCheckProgress;
    private int circleColor;
    private int circleColorKey;
    private int circleCrossfadeColorKey;
    private float circleCrossfadeColorProgress;
    private Paint circleMiniPaint;
    public Paint circlePaint;
    private int circlePressedColor;
    private int circlePressedColorKey;
    private int circleRadius;
    private boolean drawBackground;
    private boolean drawMiniIcon;
    public int iconColor;
    public int iconColorKey;
    private int iconPressedColor;
    private int iconPressedColorKey;
    public float iconScale;
    private boolean isPressed;
    private boolean isPressedMini;
    private int maxIconSize;
    public MediaActionDrawable mediaActionDrawable;
    private Bitmap miniDrawBitmap;
    private Canvas miniDrawCanvas;
    private float miniIconScale;
    private MediaActionDrawable miniMediaActionDrawable;
    private Paint miniProgressBackgroundPaint;
    private float overlayImageAlpha;
    public ImageReceiver overlayImageView;
    private Paint overlayPaint;
    private float overrideAlpha;
    public float overrideCircleAlpha;
    private View parent;
    private int progressColor;
    public RectF progressRect;
    private Theme.ResourcesProvider resourcesProvider;

    public RadialProgress2(View view) {
        this(view, null);
    }

    public RadialProgress2(final View view, Theme.ResourcesProvider resourcesProvider) {
        this.progressRect = new RectF();
        this.progressColor = -1;
        this.overlayPaint = new Paint(1);
        this.circlePaint = new Paint(1);
        this.circleMiniPaint = new Paint(1);
        this.miniIconScale = 1.0f;
        this.circleColorKey = -1;
        this.circleCrossfadeColorKey = -1;
        this.circleCheckProgress = 1.0f;
        this.circlePressedColorKey = -1;
        this.iconColorKey = -1;
        this.iconPressedColorKey = -1;
        this.overrideCircleAlpha = 1.0f;
        this.drawBackground = true;
        this.overrideAlpha = 1.0f;
        this.overlayImageAlpha = 1.0f;
        this.iconScale = 1.0f;
        this.resourcesProvider = resourcesProvider;
        this.miniProgressBackgroundPaint = new Paint(1);
        this.parent = view;
        ImageReceiver imageReceiver = new ImageReceiver(view);
        this.overlayImageView = imageReceiver;
        imageReceiver.setInvalidateAll(true);
        MediaActionDrawable mediaActionDrawable = new MediaActionDrawable();
        this.mediaActionDrawable = mediaActionDrawable;
        Objects.requireNonNull(view);
        mediaActionDrawable.setDelegate(new MediaActionDrawable.MediaActionDrawableDelegate() { // from class: org.telegram.ui.Components.RadialProgress2$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.MediaActionDrawable.MediaActionDrawableDelegate
            public final void invalidate() {
                view.invalidate();
            }
        });
        MediaActionDrawable mediaActionDrawable2 = new MediaActionDrawable();
        this.miniMediaActionDrawable = mediaActionDrawable2;
        mediaActionDrawable2.setDelegate(new MediaActionDrawable.MediaActionDrawableDelegate() { // from class: org.telegram.ui.Components.RadialProgress2$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.MediaActionDrawable.MediaActionDrawableDelegate
            public final void invalidate() {
                view.invalidate();
            }
        });
        this.miniMediaActionDrawable.setMini(true);
        this.miniMediaActionDrawable.setIcon(4, false);
        int dp = AndroidUtilities.dp(22.0f);
        this.circleRadius = dp;
        this.overlayImageView.setRoundRadius(dp);
        this.overlayPaint.setColor(1677721600);
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    private void invalidateParent() {
        int dp = AndroidUtilities.dp(2.0f);
        View view = this.parent;
        RectF rectF = this.progressRect;
        int i = ((int) rectF.left) - dp;
        int i2 = ((int) rectF.top) - dp;
        int i3 = dp * 2;
        view.invalidate(i, i2, ((int) rectF.right) + i3, ((int) rectF.bottom) + i3);
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0036, code lost:
        if (r5 == 3) goto L176;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0051, code lost:
        if (r2 != 4) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0053, code lost:
        r5 = 1.0f;
     */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0346  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x0365  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0387  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x03a5  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x03ab  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x03cd  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x03e4  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x03fc  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x0406  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x0425  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x0457  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0462  */
    /* JADX WARN: Removed duplicated region for block: B:194:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        float transitionProgress;
        int i;
        Paint paint;
        int i2;
        Paint paint2;
        int offsetColor;
        int i3;
        int i4;
        Paint paint3;
        int i5;
        int ceil;
        int ceil2;
        boolean z;
        int i6;
        float centerX;
        float centerY;
        int i7;
        float f;
        Canvas canvas2;
        int i8;
        Canvas canvas3;
        Canvas canvas4;
        Canvas canvas5;
        float f2;
        float f3;
        int i9;
        int alpha;
        int argb;
        int i10;
        if ((this.mediaActionDrawable.getCurrentIcon() == 4 && this.mediaActionDrawable.getTransitionProgress() >= 1.0f) || this.progressRect.isEmpty()) {
            return;
        }
        int currentIcon = this.mediaActionDrawable.getCurrentIcon();
        int previousIcon = this.mediaActionDrawable.getPreviousIcon();
        if (this.backgroundStroke != 0) {
            if (currentIcon != 3) {
            }
            transitionProgress = 1.0f - this.mediaActionDrawable.getTransitionProgress();
        } else {
            if ((currentIcon != 3 && currentIcon != 6 && currentIcon != 10 && currentIcon != 8 && currentIcon != 0) || previousIcon != 4) {
            }
            transitionProgress = this.mediaActionDrawable.getTransitionProgress();
        }
        if (!this.isPressedMini || this.circleCrossfadeColorKey >= 0) {
            int i11 = this.iconColorKey;
            if (i11 >= 0) {
                this.miniMediaActionDrawable.setColor(getThemedColor(i11));
            } else {
                this.miniMediaActionDrawable.setColor(this.iconColor);
            }
            i = this.circleColorKey;
            if (i >= 0) {
                if (this.circleCrossfadeColorKey >= 0) {
                    paint2 = this.circleMiniPaint;
                    offsetColor = AndroidUtilities.getOffsetColor(getThemedColor(i), getThemedColor(this.circleCrossfadeColorKey), this.circleCrossfadeColorProgress, this.circleCheckProgress);
                    paint2.setColor(offsetColor);
                }
                paint2 = this.circleMiniPaint;
                offsetColor = getThemedColor(i);
                paint2.setColor(offsetColor);
            } else {
                paint = this.circleMiniPaint;
                i2 = this.circleColor;
                paint.setColor(i2);
            }
        } else {
            int i12 = this.iconPressedColorKey;
            if (i12 >= 0) {
                this.miniMediaActionDrawable.setColor(getThemedColor(i12));
            } else {
                this.miniMediaActionDrawable.setColor(this.iconPressedColor);
            }
            i = this.circlePressedColorKey;
            if (i < 0) {
                paint = this.circleMiniPaint;
                i2 = this.circlePressedColor;
                paint.setColor(i2);
            }
            paint2 = this.circleMiniPaint;
            offsetColor = getThemedColor(i);
            paint2.setColor(offsetColor);
        }
        if (this.isPressed) {
            int i13 = this.iconPressedColorKey;
            if (i13 >= 0) {
                MediaActionDrawable mediaActionDrawable = this.mediaActionDrawable;
                i3 = getThemedColor(i13);
                mediaActionDrawable.setColor(i3);
                this.mediaActionDrawable.setBackColor(getThemedColor(this.circlePressedColorKey));
            } else {
                MediaActionDrawable mediaActionDrawable2 = this.mediaActionDrawable;
                int i14 = this.iconPressedColor;
                mediaActionDrawable2.setColor(i14);
                this.mediaActionDrawable.setBackColor(this.circlePressedColor);
                i3 = i14;
            }
            i4 = this.circlePressedColorKey;
            if (i4 < 0) {
                paint3 = this.circlePaint;
                i5 = this.circlePressedColor;
                paint3.setColor(i5);
            }
            this.circlePaint.setColor(getThemedColor(i4));
        } else {
            int i15 = this.iconColorKey;
            if (i15 >= 0) {
                MediaActionDrawable mediaActionDrawable3 = this.mediaActionDrawable;
                i3 = getThemedColor(i15);
                mediaActionDrawable3.setColor(i3);
                this.mediaActionDrawable.setBackColor(getThemedColor(this.circleColorKey));
            } else {
                MediaActionDrawable mediaActionDrawable4 = this.mediaActionDrawable;
                int i16 = this.iconColor;
                mediaActionDrawable4.setColor(i16);
                this.mediaActionDrawable.setBackColor(this.circleColor);
                i3 = i16;
            }
            i4 = this.circleColorKey;
            if (i4 < 0) {
                paint3 = this.circlePaint;
                i5 = this.circleColor;
                paint3.setColor(i5);
            }
            this.circlePaint.setColor(getThemedColor(i4));
        }
        if ((this.drawMiniIcon || this.circleCrossfadeColorKey >= 0) && this.miniDrawCanvas != null) {
            this.miniDrawBitmap.eraseColor(0);
        }
        this.circlePaint.setAlpha((int) (this.circlePaint.getAlpha() * transitionProgress * this.overrideAlpha * this.overrideCircleAlpha));
        this.circleMiniPaint.setAlpha((int) (this.circleMiniPaint.getAlpha() * transitionProgress * this.overrideAlpha));
        if ((this.drawMiniIcon || this.circleCrossfadeColorKey >= 0) && this.miniDrawCanvas != null) {
            ceil = (int) Math.ceil(this.progressRect.width() / 2.0f);
            ceil2 = (int) Math.ceil(this.progressRect.height() / 2.0f);
        } else {
            ceil = (int) this.progressRect.centerX();
            ceil2 = (int) this.progressRect.centerY();
        }
        int i17 = 2;
        if (this.overlayImageView.hasBitmapImage()) {
            float currentAlpha = this.overlayImageView.getCurrentAlpha();
            this.overlayPaint.setAlpha((int) (100.0f * currentAlpha * transitionProgress * this.overrideAlpha));
            if (currentAlpha >= 1.0f) {
                argb = -1;
                z = false;
            } else {
                int red = Color.red(i3);
                int green = Color.green(i3);
                int blue = Color.blue(i3);
                argb = Color.argb(Color.alpha(i3) + ((int) ((255 - alpha) * currentAlpha)), red + ((int) ((255 - red) * currentAlpha)), green + ((int) ((255 - green) * currentAlpha)), blue + ((int) ((255 - blue) * currentAlpha)));
                z = true;
            }
            this.mediaActionDrawable.setColor(argb);
            ImageReceiver imageReceiver = this.overlayImageView;
            float f4 = this.circleRadius * 2;
            imageReceiver.setImageCoords(ceil - i10, ceil2 - i10, f4, f4);
        } else {
            z = true;
        }
        Canvas canvas6 = this.miniDrawCanvas;
        if (canvas6 == null || this.circleCrossfadeColorKey < 0 || this.circleCheckProgress == 1.0f) {
            i6 = Integer.MIN_VALUE;
        } else {
            i6 = canvas6.save();
            float f5 = 1.0f - ((1.0f - this.circleCheckProgress) * 0.1f);
            this.miniDrawCanvas.scale(f5, f5, ceil, ceil2);
        }
        if (z && this.drawBackground) {
            if ((this.drawMiniIcon || this.circleCrossfadeColorKey >= 0) && (canvas5 = this.miniDrawCanvas) != null) {
                canvas5.drawCircle(ceil, ceil2, this.circleRadius, this.circlePaint);
            } else if (currentIcon != 4 || transitionProgress != 0.0f) {
                if (this.backgroundStroke != 0) {
                    f2 = ceil;
                    f3 = ceil2;
                    i9 = this.circleRadius - AndroidUtilities.dp(3.5f);
                } else {
                    f2 = ceil;
                    f3 = ceil2;
                    i9 = this.circleRadius;
                }
                canvas.drawCircle(f2, f3, i9, this.circlePaint);
            }
        }
        if (this.overlayImageView.hasBitmapImage()) {
            this.overlayImageView.setAlpha(transitionProgress * this.overrideAlpha * this.overlayImageAlpha);
            if ((this.drawMiniIcon || this.circleCrossfadeColorKey >= 0) && (canvas4 = this.miniDrawCanvas) != null) {
                this.overlayImageView.draw(canvas4);
                this.miniDrawCanvas.drawCircle(ceil, ceil2, this.circleRadius, this.overlayPaint);
            } else {
                this.overlayImageView.draw(canvas);
                canvas.drawCircle(ceil, ceil2, this.circleRadius, this.overlayPaint);
            }
        }
        int i18 = this.circleRadius;
        int i19 = this.maxIconSize;
        if (i19 > 0 && i18 > i19) {
            i18 = i19;
        }
        if (this.iconScale != 1.0f) {
            canvas.save();
            float f6 = this.iconScale;
            canvas.scale(f6, f6, ceil, ceil2);
        }
        this.mediaActionDrawable.setBounds(ceil - i18, ceil2 - i18, ceil + i18, ceil2 + i18);
        this.mediaActionDrawable.setHasOverlayImage(this.overlayImageView.hasBitmapImage());
        if (this.drawMiniIcon || this.circleCrossfadeColorKey >= 0) {
            Canvas canvas7 = this.miniDrawCanvas;
            if (canvas7 != null) {
                this.mediaActionDrawable.draw(canvas7);
                if (i6 != Integer.MIN_VALUE && (canvas3 = this.miniDrawCanvas) != null) {
                    canvas3.restoreToCount(i6);
                }
                if (!this.drawMiniIcon || this.circleCrossfadeColorKey >= 0) {
                    if (Math.abs(this.progressRect.width() - AndroidUtilities.dp(44.0f)) >= AndroidUtilities.density) {
                        float f7 = 16;
                        centerX = this.progressRect.centerX() + AndroidUtilities.dp(f7);
                        centerY = this.progressRect.centerY() + AndroidUtilities.dp(f7);
                        i7 = 20;
                        i17 = 0;
                    } else {
                        centerX = this.progressRect.centerX() + AndroidUtilities.dp(18.0f);
                        centerY = this.progressRect.centerY() + AndroidUtilities.dp(18.0f);
                        i7 = 22;
                    }
                    int i20 = i7 / 2;
                    if (this.drawMiniIcon) {
                        f = 1.0f;
                    } else {
                        f = this.miniMediaActionDrawable.getCurrentIcon() != 4 ? 1.0f : 1.0f - this.miniMediaActionDrawable.getTransitionProgress();
                        if (f == 0.0f) {
                            this.drawMiniIcon = false;
                        }
                    }
                    canvas2 = this.miniDrawCanvas;
                    if (canvas2 == null) {
                        float f8 = i7 + 18 + i17;
                        canvas2.drawCircle(AndroidUtilities.dp(f8), AndroidUtilities.dp(f8), AndroidUtilities.dp(i20 + 1) * f * this.miniIconScale, Theme.checkboxSquare_eraserPaint);
                    } else {
                        this.miniProgressBackgroundPaint.setColor(this.progressColor);
                        canvas.drawCircle(centerX, centerY, AndroidUtilities.dp(12.0f), this.miniProgressBackgroundPaint);
                    }
                    if (this.miniDrawCanvas != null) {
                        Bitmap bitmap = this.miniDrawBitmap;
                        RectF rectF = this.progressRect;
                        canvas.drawBitmap(bitmap, (int) rectF.left, (int) rectF.top, (Paint) null);
                    }
                    if (this.miniIconScale >= 1.0f) {
                        i8 = canvas.save();
                        float f9 = this.miniIconScale;
                        canvas.scale(f9, f9, centerX, centerY);
                    } else {
                        i8 = Integer.MIN_VALUE;
                    }
                    float f10 = i20;
                    canvas.drawCircle(centerX, centerY, (AndroidUtilities.dp(f10) * f) + (AndroidUtilities.dp(1.0f) * (1.0f - this.circleCheckProgress)), this.circleMiniPaint);
                    if (this.drawMiniIcon) {
                        this.miniMediaActionDrawable.setBounds((int) (centerX - (AndroidUtilities.dp(f10) * f)), (int) (centerY - (AndroidUtilities.dp(f10) * f)), (int) (centerX + (AndroidUtilities.dp(f10) * f)), (int) (centerY + (AndroidUtilities.dp(f10) * f)));
                        this.miniMediaActionDrawable.draw(canvas);
                    }
                    if (i8 != Integer.MIN_VALUE) {
                        canvas.restoreToCount(i8);
                    }
                }
                if (this.iconScale == 1.0f) {
                    canvas.restore();
                    return;
                }
                return;
            }
        } else {
            this.mediaActionDrawable.setOverrideAlpha(this.overrideAlpha);
        }
        this.mediaActionDrawable.draw(canvas);
        if (i6 != Integer.MIN_VALUE) {
            canvas3.restoreToCount(i6);
        }
        if (!this.drawMiniIcon) {
        }
        if (Math.abs(this.progressRect.width() - AndroidUtilities.dp(44.0f)) >= AndroidUtilities.density) {
        }
        int i202 = i7 / 2;
        if (this.drawMiniIcon) {
        }
        canvas2 = this.miniDrawCanvas;
        if (canvas2 == null) {
        }
        if (this.miniDrawCanvas != null) {
        }
        if (this.miniIconScale >= 1.0f) {
        }
        float f102 = i202;
        canvas.drawCircle(centerX, centerY, (AndroidUtilities.dp(f102) * f) + (AndroidUtilities.dp(1.0f) * (1.0f - this.circleCheckProgress)), this.circleMiniPaint);
        if (this.drawMiniIcon) {
        }
        if (i8 != Integer.MIN_VALUE) {
        }
        if (this.iconScale == 1.0f) {
        }
    }

    public int getCircleColorKey() {
        return this.circleColorKey;
    }

    public int getIcon() {
        return this.mediaActionDrawable.getCurrentIcon();
    }

    public int getMiniIcon() {
        return this.miniMediaActionDrawable.getCurrentIcon();
    }

    public float getOverrideAlpha() {
        return this.overrideAlpha;
    }

    public float getProgress() {
        return (this.drawMiniIcon ? this.miniMediaActionDrawable : this.mediaActionDrawable).getProgress();
    }

    public RectF getProgressRect() {
        return this.progressRect;
    }

    public int getRadius() {
        return this.circleRadius;
    }

    public float getTransitionProgress() {
        return (this.drawMiniIcon ? this.miniMediaActionDrawable : this.mediaActionDrawable).getTransitionProgress();
    }

    public void initMiniIcons() {
        if (this.miniDrawBitmap == null) {
            try {
                this.miniDrawBitmap = Bitmap.createBitmap(AndroidUtilities.dp(48.0f), AndroidUtilities.dp(48.0f), Bitmap.Config.ARGB_8888);
                this.miniDrawCanvas = new Canvas(this.miniDrawBitmap);
            } catch (Throwable unused) {
            }
        }
    }

    public void onAttachedToWindow() {
        this.overlayImageView.onAttachedToWindow();
    }

    public void onDetachedFromWindow() {
        this.overlayImageView.onDetachedFromWindow();
    }

    public void setAsMini() {
        this.mediaActionDrawable.setMini(true);
    }

    public void setBackgroundDrawable(Theme.MessageDrawable messageDrawable) {
        this.mediaActionDrawable.setBackgroundDrawable(messageDrawable);
        this.miniMediaActionDrawable.setBackgroundDrawable(messageDrawable);
    }

    public void setBackgroundGradientDrawable(LinearGradient linearGradient) {
        this.mediaActionDrawable.setBackgroundGradientDrawable(linearGradient);
        this.miniMediaActionDrawable.setBackgroundGradientDrawable(linearGradient);
    }

    public void setCircleCrossfadeColor(int i, float f, float f2) {
        this.circleCrossfadeColorKey = i;
        this.circleCrossfadeColorProgress = f;
        this.circleCheckProgress = f2;
        this.miniIconScale = 1.0f;
        if (i >= 0) {
            initMiniIcons();
        }
    }

    public void setCircleRadius(int i) {
        this.circleRadius = i;
        this.overlayImageView.setRoundRadius(i);
    }

    public void setColorKeys(int i, int i2, int i3, int i4) {
        this.circleColorKey = i;
        this.circlePressedColorKey = i2;
        this.iconColorKey = i3;
        this.iconPressedColorKey = i4;
    }

    public void setColors(int i, int i2, int i3, int i4) {
        this.circleColor = i;
        this.circlePressedColor = i2;
        this.iconColor = i3;
        this.iconPressedColor = i4;
        this.circleColorKey = -1;
        this.circlePressedColorKey = -1;
        this.iconColorKey = -1;
        this.iconPressedColorKey = -1;
    }

    public void setDrawBackground(boolean z) {
        this.drawBackground = z;
    }

    public void setIcon(int i, boolean z, boolean z2) {
        if (z && i == this.mediaActionDrawable.getCurrentIcon()) {
            return;
        }
        this.mediaActionDrawable.setIcon(i, z2);
        if (z2) {
            invalidateParent();
        } else {
            this.parent.invalidate();
        }
    }

    public void setImageOverlay(String str) {
        this.overlayImageView.setImage(str, str != null ? String.format(Locale.US, "%d_%d", Integer.valueOf(this.circleRadius * 2), Integer.valueOf(this.circleRadius * 2)) : null, null, null, -1L);
    }

    public void setImageOverlay(TLRPC.PhotoSize photoSize, TLRPC.Document document, Object obj) {
        Locale locale = Locale.US;
        String.format(locale, "%d_%d", Integer.valueOf(this.circleRadius * 2), Integer.valueOf(this.circleRadius * 2));
        this.overlayImageView.setImage(ImageLocation.getForDocument(photoSize, document), String.format(locale, "%d_%d", Integer.valueOf(this.circleRadius * 2), Integer.valueOf(this.circleRadius * 2)), null, null, obj, 1);
    }

    public void setImageOverlay(TLRPC.PhotoSize photoSize, TLRPC.PhotoSize photoSize2, TLRPC.Document document, Object obj) {
        String format = String.format(Locale.US, "%d_%d", Integer.valueOf(this.circleRadius * 2), Integer.valueOf(this.circleRadius * 2));
        this.overlayImageView.setImage(photoSize == null ? null : ImageLocation.getForDocument(photoSize, document), format, photoSize2 == null ? null : ImageLocation.getForDocument(photoSize2, document), format, null, 0L, null, obj, 1);
    }

    public void setMaxIconSize(int i) {
        this.maxIconSize = i;
    }

    public void setMiniIcon(int i, boolean z, boolean z2) {
        if (i == 2 || i == 3 || i == 4) {
            if (z && i == this.miniMediaActionDrawable.getCurrentIcon()) {
                return;
            }
            this.miniMediaActionDrawable.setIcon(i, z2);
            boolean z3 = i != 4 || this.miniMediaActionDrawable.getTransitionProgress() < 1.0f;
            this.drawMiniIcon = z3;
            if (z3) {
                initMiniIcons();
            }
            if (z2) {
                invalidateParent();
            } else {
                this.parent.invalidate();
            }
        }
    }

    public void setMiniIconScale(float f) {
        this.miniIconScale = f;
    }

    public void setMiniProgressBackgroundColor(int i) {
        this.miniProgressBackgroundPaint.setColor(i);
    }

    public void setOverlayImageAlpha(float f) {
        this.overlayImageAlpha = f;
    }

    public void setOverrideAlpha(float f) {
        this.overrideAlpha = f;
    }

    public void setPressed(boolean z, boolean z2) {
        if (z2) {
            this.isPressedMini = z;
        } else {
            this.isPressed = z;
        }
        invalidateParent();
    }

    public void setProgress(float f, boolean z) {
        (this.drawMiniIcon ? this.miniMediaActionDrawable : this.mediaActionDrawable).setProgress(f, z);
    }

    public void setProgressColor(int i) {
        this.progressColor = i;
    }

    public void setProgressRect(float f, float f2, float f3, float f4) {
        this.progressRect.set(f, f2, f3, f4);
    }

    public void setProgressRect(int i, int i2, int i3, int i4) {
        this.progressRect.set(i, i2, i3, i4);
    }

    public void setResourcesProvider(Theme.ResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
    }
}
