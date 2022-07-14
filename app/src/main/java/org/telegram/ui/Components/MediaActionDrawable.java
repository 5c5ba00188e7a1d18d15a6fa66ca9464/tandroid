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
/* loaded from: classes5.dex */
public class MediaActionDrawable extends Drawable {
    private static final float CANCEL_TO_CHECK_STAGE1 = 0.5f;
    private static final float CANCEL_TO_CHECK_STAGE2 = 0.5f;
    private static final float DOWNLOAD_TO_CANCEL_STAGE1 = 0.5f;
    private static final float DOWNLOAD_TO_CANCEL_STAGE2 = 0.2f;
    private static final float DOWNLOAD_TO_CANCEL_STAGE3 = 0.3f;
    private static final float EPS = 0.001f;
    public static final int ICON_CANCEL = 3;
    public static final int ICON_CANCEL_FILL = 14;
    public static final int ICON_CANCEL_NOPROFRESS = 12;
    public static final int ICON_CANCEL_PERCENT = 13;
    public static final int ICON_CHECK = 6;
    public static final int ICON_DOWNLOAD = 2;
    public static final int ICON_EMPTY = 10;
    public static final int ICON_EMPTY_NOPROGRESS = 11;
    public static final int ICON_FILE = 5;
    public static final int ICON_FIRE = 7;
    public static final int ICON_GIF = 8;
    public static final int ICON_NONE = 4;
    public static final int ICON_PAUSE = 1;
    public static final int ICON_PLAY = 0;
    public static final int ICON_SECRETCHECK = 9;
    public static final int ICON_UPDATE = 15;
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

    /* loaded from: classes5.dex */
    public interface MediaActionDrawableDelegate {
        void invalidate();
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

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
    }

    public void setOverrideAlpha(float alpha) {
        this.overrideAlpha = alpha;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
        this.paint2.setColorFilter(colorFilter);
        this.paint3.setColorFilter(colorFilter);
        this.textPaint.setColorFilter(colorFilter);
    }

    public void setColor(int value) {
        this.paint.setColor(value | (-16777216));
        this.paint2.setColor(value | (-16777216));
        this.paint3.setColor(value | (-16777216));
        this.textPaint.setColor((-16777216) | value);
        this.colorFilter = new PorterDuffColorFilter(value, PorterDuff.Mode.MULTIPLY);
    }

    public void setBackColor(int value) {
        this.backPaint.setColor((-16777216) | value);
    }

    public int getColor() {
        return this.paint.getColor();
    }

    public void setMini(boolean value) {
        this.isMini = value;
        this.paint.setStrokeWidth(AndroidUtilities.dp(value ? 2.0f : 3.0f));
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    public void setDelegate(MediaActionDrawableDelegate mediaActionDrawableDelegate) {
        this.delegate = mediaActionDrawableDelegate;
    }

    public boolean setIcon(int icon, boolean animated) {
        int i;
        int i2;
        if (this.currentIcon == icon && (i2 = this.nextIcon) != icon) {
            this.currentIcon = i2;
            this.transitionProgress = 1.0f;
        }
        if (animated) {
            int i3 = this.currentIcon;
            if (i3 == icon || (i = this.nextIcon) == icon) {
                return false;
            }
            if ((i3 == 0 && icon == 1) || (i3 == 1 && icon == 0)) {
                this.transitionAnimationTime = 300.0f;
            } else if (i3 == 2 && (icon == 3 || icon == 14)) {
                this.transitionAnimationTime = 400.0f;
            } else if (i3 != 4 && icon == 6) {
                this.transitionAnimationTime = 360.0f;
            } else if ((i3 == 4 && icon == 14) || (i3 == 14 && icon == 4)) {
                this.transitionAnimationTime = 160.0f;
            } else {
                this.transitionAnimationTime = 220.0f;
            }
            if (this.animatingTransition) {
                this.currentIcon = i;
            }
            this.animatingTransition = true;
            this.nextIcon = icon;
            this.savedTransitionProgress = this.transitionProgress;
            this.transitionProgress = 0.0f;
        } else if (this.currentIcon == icon) {
            return false;
        } else {
            this.animatingTransition = false;
            this.nextIcon = icon;
            this.currentIcon = icon;
            this.savedTransitionProgress = this.transitionProgress;
            this.transitionProgress = 1.0f;
        }
        if (icon == 3 || icon == 14) {
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

    public void setProgress(float value, boolean animated) {
        if (!animated) {
            this.animatedDownloadProgress = value;
            this.downloadProgressAnimationStart = value;
        } else {
            if (this.animatedDownloadProgress > value) {
                this.animatedDownloadProgress = value;
            }
            this.downloadProgressAnimationStart = this.animatedDownloadProgress;
        }
        this.downloadProgress = value;
        this.downloadProgressTime = 0.0f;
        invalidateSelf();
    }

    public float getProgress() {
        return this.downloadProgress;
    }

    public static float getCircleValue(float value) {
        while (value > 360.0f) {
            value -= 360.0f;
        }
        return value;
    }

    public float getProgressAlpha() {
        return 1.0f - this.transitionProgress;
    }

    public float getTransitionProgress() {
        if (this.animatingTransition) {
            return this.transitionProgress;
        }
        return 1.0f;
    }

    public void setBackgroundDrawable(Theme.MessageDrawable drawable) {
        this.messageDrawable = drawable;
    }

    public void setBackgroundGradientDrawable(LinearGradient drawable) {
        this.gradientDrawable = drawable;
        this.gradientMatrix = new Matrix();
    }

    public void setHasOverlayImage(boolean value) {
        this.hasOverlayImage = value;
    }

    @Override // android.graphics.drawable.Drawable
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        float intrinsicWidth = (right - left) / getIntrinsicWidth();
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

    private void applyShaderMatrix(boolean path) {
        Theme.MessageDrawable messageDrawable = this.messageDrawable;
        if (messageDrawable != null && messageDrawable.hasGradient() && !this.hasOverlayImage) {
            android.graphics.Rect bounds = getBounds();
            Shader shader = this.messageDrawable.getGradientShader();
            Matrix matrix = this.messageDrawable.getMatrix();
            matrix.reset();
            this.messageDrawable.applyMatrixScale();
            if (path) {
                matrix.postTranslate(-bounds.centerX(), (-this.messageDrawable.getTopY()) + bounds.top);
            } else {
                matrix.postTranslate(0.0f, -this.messageDrawable.getTopY());
            }
            shader.setLocalMatrix(matrix);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:334:0x0957, code lost:
        if (r44.nextIcon != 1) goto L336;
     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x0989, code lost:
        if (r2 == 1) goto L358;
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x0cee, code lost:
        if (r44.nextIcon == 14) goto L500;
     */
    /* JADX WARN: Removed duplicated region for block: B:229:0x0710  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x0717  */
    /* JADX WARN: Removed duplicated region for block: B:242:0x074c  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x074f  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x075c  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x0761  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x0772  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x0775  */
    /* JADX WARN: Removed duplicated region for block: B:261:0x0782  */
    /* JADX WARN: Removed duplicated region for block: B:262:0x0787  */
    /* JADX WARN: Removed duplicated region for block: B:273:0x07ba  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x07bd  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x07d9  */
    /* JADX WARN: Removed duplicated region for block: B:280:0x082b  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x0844  */
    /* JADX WARN: Removed duplicated region for block: B:288:0x0848  */
    /* JADX WARN: Removed duplicated region for block: B:294:0x085b  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x085e  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x0878  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x08b5  */
    /* JADX WARN: Removed duplicated region for block: B:308:0x08cc  */
    /* JADX WARN: Removed duplicated region for block: B:309:0x08cf  */
    /* JADX WARN: Removed duplicated region for block: B:315:0x08f7  */
    /* JADX WARN: Removed duplicated region for block: B:323:0x093d  */
    /* JADX WARN: Removed duplicated region for block: B:333:0x0954  */
    /* JADX WARN: Removed duplicated region for block: B:335:0x095a  */
    /* JADX WARN: Removed duplicated region for block: B:351:0x0980  */
    /* JADX WARN: Removed duplicated region for block: B:352:0x0983  */
    /* JADX WARN: Removed duplicated region for block: B:355:0x0988  */
    /* JADX WARN: Removed duplicated region for block: B:357:0x098c  */
    /* JADX WARN: Removed duplicated region for block: B:359:0x098f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:371:0x09e7  */
    /* JADX WARN: Removed duplicated region for block: B:372:0x09ea  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x09ed  */
    /* JADX WARN: Removed duplicated region for block: B:386:0x0a2c  */
    /* JADX WARN: Removed duplicated region for block: B:390:0x0a34  */
    /* JADX WARN: Removed duplicated region for block: B:391:0x0a41  */
    /* JADX WARN: Removed duplicated region for block: B:397:0x0a64  */
    /* JADX WARN: Removed duplicated region for block: B:410:0x0a9b  */
    /* JADX WARN: Removed duplicated region for block: B:419:0x0ac6  */
    /* JADX WARN: Removed duplicated region for block: B:426:0x0af8  */
    /* JADX WARN: Removed duplicated region for block: B:427:0x0b2b  */
    /* JADX WARN: Removed duplicated region for block: B:430:0x0b34  */
    /* JADX WARN: Removed duplicated region for block: B:432:0x0b52  */
    /* JADX WARN: Removed duplicated region for block: B:439:0x0b95  */
    /* JADX WARN: Removed duplicated region for block: B:441:0x0b99  */
    /* JADX WARN: Removed duplicated region for block: B:447:0x0bd5  */
    /* JADX WARN: Removed duplicated region for block: B:461:0x0c39  */
    /* JADX WARN: Removed duplicated region for block: B:463:0x0c41  */
    /* JADX WARN: Removed duplicated region for block: B:484:0x0cd8  */
    /* JADX WARN: Removed duplicated region for block: B:502:0x0d1e  */
    /* JADX WARN: Removed duplicated region for block: B:509:0x0d51  */
    /* JADX WARN: Removed duplicated region for block: B:513:0x0d5a  */
    /* JADX WARN: Removed duplicated region for block: B:521:0x0d7e  */
    /* JADX WARN: Removed duplicated region for block: B:523:? A[RETURN, SYNTHETIC] */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        int saveCount;
        int saveCount2;
        int i;
        float drawableScale;
        float drawableScale2;
        Path[] previousPath;
        Path[] nextPath;
        Drawable nextDrawable;
        Drawable previousDrawable;
        Drawable nextDrawable2;
        Path[] previousPath2;
        float previowsDrawableScale;
        Path[] nextPath2;
        Drawable nextDrawable3;
        int i2;
        int i3;
        Drawable nextDrawable4;
        Path[] previousPath3;
        Path[] nextPath3;
        long dt;
        int i4;
        int saveCount3;
        float progress1;
        float progress2;
        int y;
        int x;
        float progress12;
        float progress;
        float p;
        float ms;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        float transition;
        int newPercent;
        String format;
        int i11;
        int i12;
        float transition2;
        float f;
        float iconScale;
        float d;
        float rotation;
        float iconScaleX;
        float progressScale;
        float iconScale2;
        int alpha;
        float progressScaleX;
        float iconScale3;
        float rotation2;
        float iconScaleY;
        int alpha2;
        int i13;
        float f2;
        float progress3;
        float rotation3;
        float progressScale2;
        float iconScale4;
        float iconScaleY2;
        float progressScaleY;
        float iconScaleX2;
        float iconScaleY3;
        float progressScaleX2;
        float iconScaleX3;
        float d2;
        int alpha3;
        int alpha4;
        int i14;
        float yStart2;
        float transition3;
        float x1;
        float x2;
        float y2;
        float y3;
        float y1;
        float currentProgress;
        float currentBackProgress;
        float currentProgress2;
        float currentProgress3;
        float rotation4;
        int alpha5;
        float rotation5;
        float transition4;
        android.graphics.Rect bounds = getBounds();
        Theme.MessageDrawable messageDrawable = this.messageDrawable;
        if (messageDrawable == null || !messageDrawable.hasGradient() || this.hasOverlayImage) {
            if (this.gradientDrawable != null && !this.hasOverlayImage) {
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
        } else {
            Shader shader = this.messageDrawable.getGradientShader();
            this.paint.setShader(shader);
            this.paint2.setShader(shader);
            this.paint3.setShader(shader);
        }
        int cx = bounds.centerX();
        int cy = bounds.centerY();
        int i15 = this.nextIcon;
        if (i15 == 4) {
            int i16 = this.currentIcon;
            if (i16 != 3 && i16 != 14) {
                int saveCount4 = canvas.save();
                float progress4 = 1.0f - this.transitionProgress;
                canvas.scale(progress4, progress4, cx, cy);
                saveCount = saveCount4;
            }
            saveCount = 0;
        } else {
            if ((i15 == 6 || i15 == 10) && this.currentIcon == 4) {
                int saveCount5 = canvas.save();
                float f3 = this.transitionProgress;
                canvas.scale(f3, f3, cx, cy);
                saveCount = saveCount5;
            }
            saveCount = 0;
        }
        AndroidUtilities.dp(3.0f);
        if (this.currentIcon == 2 || this.nextIcon == 2) {
            applyShaderMatrix(false);
            float yStart = cy - (AndroidUtilities.dp(9.0f) * this.scale);
            float yEnd = cy + (AndroidUtilities.dp(9.0f) * this.scale);
            float yEnd2 = cy + (AndroidUtilities.dp(12.0f) * this.scale);
            int i17 = this.currentIcon;
            if ((i17 == 3 || i17 == 14) && this.nextIcon == 2) {
                this.paint.setAlpha((int) (Math.min(1.0f, this.transitionProgress / 0.5f) * 255.0f));
                transition3 = this.transitionProgress;
                yStart2 = cy + (AndroidUtilities.dp(12.0f) * this.scale);
            } else {
                int i18 = this.nextIcon;
                if (i18 != 3 && i18 != 14 && i18 != 2) {
                    this.paint.setAlpha((int) (Math.min(1.0f, this.savedTransitionProgress / 0.5f) * 255.0f * (1.0f - this.transitionProgress)));
                    transition4 = this.savedTransitionProgress;
                } else {
                    this.paint.setAlpha(255);
                    transition4 = this.transitionProgress;
                }
                transition3 = transition4;
                yStart2 = cy + (AndroidUtilities.dp(1.0f) * this.scale);
            }
            if (this.animatingTransition) {
                float progress5 = transition3;
                int i19 = this.nextIcon;
                if (i19 == 2) {
                    saveCount2 = saveCount;
                } else if (progress5 <= 0.5f) {
                    saveCount2 = saveCount;
                } else {
                    float f4 = this.scale;
                    float d3 = (AndroidUtilities.dp(13.0f) * f4 * f4) + (this.isMini ? AndroidUtilities.dp(2.0f) : 0);
                    float progress6 = progress5 - 0.5f;
                    float currentProgress32 = progress6 / 0.5f;
                    if (progress6 > 0.2f) {
                        currentProgress3 = 1.0f;
                        currentProgress2 = (progress6 - 0.2f) / 0.3f;
                    } else {
                        currentProgress3 = progress6 / 0.2f;
                        currentProgress2 = 0.0f;
                    }
                    this.rect.set(cx - d3, yEnd2 - (d3 / 2.0f), cx, yEnd2 + (d3 / 2.0f));
                    float start = currentProgress2 * 100.0f;
                    saveCount2 = saveCount;
                    canvas.drawArc(this.rect, start, (104.0f * currentProgress32) - start, false, this.paint);
                    y1 = yStart2 + ((yEnd2 - yStart2) * currentProgress3);
                    y3 = yEnd2;
                    y2 = yEnd2;
                    float f5 = cx;
                    x2 = f5;
                    x1 = f5;
                    if (currentProgress2 > 0.0f) {
                        if (this.nextIcon == 14) {
                            rotation4 = 0.0f;
                        } else {
                            rotation4 = (-45.0f) * (1.0f - currentProgress2);
                        }
                        float d4 = AndroidUtilities.dp(7.0f) * currentProgress2 * this.scale;
                        int alpha6 = (int) (currentProgress2 * 255.0f);
                        int i20 = this.nextIcon;
                        if (i20 != 3 && i20 != 14 && i20 != 2) {
                            alpha5 = (int) (alpha6 * (1.0f - Math.min(1.0f, this.transitionProgress / 0.5f)));
                        } else {
                            alpha5 = alpha6;
                        }
                        if (rotation4 != 0.0f) {
                            canvas.save();
                            canvas.rotate(rotation4, cx, cy);
                        }
                        if (alpha5 != 0) {
                            this.paint.setAlpha(alpha5);
                            if (this.nextIcon == 14) {
                                this.paint3.setAlpha(alpha5);
                                this.rect.set(cx - AndroidUtilities.dp(3.5f), cy - AndroidUtilities.dp(3.5f), AndroidUtilities.dp(3.5f) + cx, cy + AndroidUtilities.dp(3.5f));
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint3);
                                this.paint.setAlpha((int) (alpha5 * 0.15f));
                                int diff = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                                this.rect.set(bounds.left + diff, bounds.top + diff, bounds.right - diff, bounds.bottom - diff);
                                rotation5 = rotation4;
                                canvas.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                                this.paint.setAlpha(alpha5);
                            } else {
                                rotation5 = rotation4;
                                canvas.drawLine(cx - d4, cy - d4, cx + d4, cy + d4, this.paint);
                                canvas.drawLine(cx + d4, cy - d4, cx - d4, cy + d4, this.paint);
                            }
                        } else {
                            rotation5 = rotation4;
                        }
                        if (rotation5 != 0.0f) {
                            canvas.restore();
                        }
                    }
                }
                if (i19 == 2) {
                    currentBackProgress = transition3;
                    currentProgress = 1.0f - currentBackProgress;
                } else {
                    currentProgress = transition3 / 0.5f;
                    currentBackProgress = 1.0f - currentProgress;
                }
                y1 = yStart + ((yStart2 - yStart) * currentProgress);
                y2 = yEnd + ((yEnd2 - yEnd) * currentProgress);
                x1 = cx - ((AndroidUtilities.dp(8.0f) * currentBackProgress) * this.scale);
                x2 = cx + (AndroidUtilities.dp(8.0f) * currentBackProgress * this.scale);
                y3 = y2 - ((AndroidUtilities.dp(8.0f) * currentBackProgress) * this.scale);
            } else {
                saveCount2 = saveCount;
                y1 = yStart;
                y2 = yEnd;
                x1 = cx - (AndroidUtilities.dp(8.0f) * this.scale);
                x2 = cx + (AndroidUtilities.dp(8.0f) * this.scale);
                y3 = y2 - (AndroidUtilities.dp(8.0f) * this.scale);
            }
            if (y1 != y2) {
                canvas.drawLine(cx, y1, cx, y2, this.paint);
            }
            if (x1 != cx) {
                float f6 = y3;
                float f7 = y2;
                canvas.drawLine(x1, f6, cx, f7, this.paint);
                canvas.drawLine(x2, f6, cx, f7, this.paint);
            }
        } else {
            saveCount2 = saveCount;
        }
        int i21 = this.currentIcon;
        if (i21 != 3 && i21 != 14) {
            if (i21 != 4 || ((i14 = this.nextIcon) != 14 && i14 != 3)) {
                if (i21 == 10 || this.nextIcon == 10 || i21 == 13) {
                    int i22 = this.nextIcon;
                    if (i22 == 4 || i22 == 6) {
                        float progress7 = this.transitionProgress;
                        alpha4 = (int) ((1.0f - progress7) * 255.0f);
                    } else {
                        alpha4 = 255;
                    }
                    if (alpha4 != 0) {
                        applyShaderMatrix(false);
                        this.paint.setAlpha((int) (alpha4 * this.overrideAlpha));
                        float rad = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
                        int diff2 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
                        this.rect.set(bounds.left + diff2, bounds.top + diff2, bounds.right - diff2, bounds.bottom - diff2);
                        canvas.drawArc(this.rect, this.downloadRadOffset, rad, false, this.paint);
                    }
                }
                Drawable nextDrawable5 = null;
                Drawable previousDrawable2 = null;
                Path[] nextPath4 = null;
                Path[] previousPath4 = null;
                i = this.currentIcon;
                if (i != this.nextIcon) {
                    drawableScale = 1.0f;
                    drawableScale2 = 1.0f;
                } else {
                    if (i == 4 || i == 3) {
                        f = 1.0f;
                    } else if (i == 14) {
                        f = 1.0f;
                    } else {
                        drawableScale = Math.min(1.0f, this.transitionProgress / 0.5f);
                        drawableScale2 = Math.max(0.0f, 1.0f - (this.transitionProgress / 0.5f));
                    }
                    drawableScale = this.transitionProgress;
                    drawableScale2 = f - this.transitionProgress;
                }
                if (this.nextIcon != 15) {
                    nextPath4 = Theme.chat_updatePath;
                } else if (this.currentIcon == 15) {
                    previousPath4 = Theme.chat_updatePath;
                }
                if (this.nextIcon != 5) {
                    nextPath = Theme.chat_filePath;
                    previousPath = previousPath4;
                } else if (this.currentIcon != 5) {
                    nextPath = nextPath4;
                    previousPath = previousPath4;
                } else {
                    nextPath = nextPath4;
                    previousPath = Theme.chat_filePath;
                }
                if (this.nextIcon != 7) {
                    nextDrawable5 = Theme.chat_flameIcon;
                } else if (this.currentIcon == 7) {
                    previousDrawable2 = Theme.chat_flameIcon;
                }
                if (this.nextIcon != 8) {
                    nextDrawable = Theme.chat_gifIcon;
                    previousDrawable = previousDrawable2;
                } else if (this.currentIcon != 8) {
                    nextDrawable = nextDrawable5;
                    previousDrawable = previousDrawable2;
                } else {
                    nextDrawable = nextDrawable5;
                    previousDrawable = Theme.chat_gifIcon;
                }
                if (this.currentIcon != 9 || this.nextIcon == 9) {
                    applyShaderMatrix(false);
                    this.paint.setAlpha(this.currentIcon != this.nextIcon ? 255 : (int) (this.transitionProgress * 255.0f));
                    int y4 = cy + AndroidUtilities.dp(7.0f);
                    int x3 = cx - AndroidUtilities.dp(3.0f);
                    if (this.currentIcon != this.nextIcon) {
                        canvas.save();
                        float f8 = this.transitionProgress;
                        canvas.scale(f8, f8, cx, cy);
                    }
                    nextPath2 = nextPath;
                    previousPath2 = previousPath;
                    previowsDrawableScale = drawableScale2;
                    nextDrawable2 = nextDrawable;
                    nextDrawable3 = previousDrawable;
                    canvas.drawLine(x3 - AndroidUtilities.dp(6.0f), y4 - AndroidUtilities.dp(6.0f), x3, y4, this.paint);
                    canvas.drawLine(x3, y4, x3 + AndroidUtilities.dp(12.0f), y4 - AndroidUtilities.dp(12.0f), this.paint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas.restore();
                    }
                } else {
                    previowsDrawableScale = drawableScale2;
                    nextPath2 = nextPath;
                    previousPath2 = previousPath;
                    nextDrawable2 = nextDrawable;
                    nextDrawable3 = previousDrawable;
                }
                if (this.currentIcon != 12 || this.nextIcon == 12) {
                    applyShaderMatrix(false);
                    i11 = this.currentIcon;
                    i12 = this.nextIcon;
                    if (i11 != i12) {
                        transition2 = 1.0f;
                    } else if (i12 == 13) {
                        transition2 = this.transitionProgress;
                    } else {
                        transition2 = 1.0f - this.transitionProgress;
                    }
                    this.paint.setAlpha(i11 != i12 ? 255 : (int) (transition2 * 255.0f));
                    int dp = cy + AndroidUtilities.dp(7.0f);
                    int dp2 = cx - AndroidUtilities.dp(3.0f);
                    if (this.currentIcon != this.nextIcon) {
                        canvas.save();
                        canvas.scale(transition2, transition2, cx, cy);
                    }
                    float d5 = AndroidUtilities.dp(7.0f) * this.scale;
                    canvas.drawLine(cx - d5, cy - d5, cx + d5, cy + d5, this.paint);
                    canvas.drawLine(cx + d5, cy - d5, cx - d5, cy + d5, this.paint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas.restore();
                    }
                }
                if (this.currentIcon != 13 || this.nextIcon == 13) {
                    applyShaderMatrix(false);
                    i9 = this.currentIcon;
                    i10 = this.nextIcon;
                    if (i9 != i10) {
                        transition = 1.0f;
                    } else if (i10 == 13) {
                        transition = this.transitionProgress;
                    } else {
                        transition = 1.0f - this.transitionProgress;
                    }
                    this.textPaint.setAlpha((int) (transition * 255.0f));
                    int y5 = AndroidUtilities.dp(5.0f) + cy;
                    int x4 = cx - (this.percentStringWidth / 2);
                    if (this.currentIcon != this.nextIcon) {
                        canvas.save();
                        canvas.scale(transition, transition, cx, cy);
                    }
                    newPercent = (int) (this.animatedDownloadProgress * 100.0f);
                    if (this.percentString != null || newPercent != this.lastPercent) {
                        this.lastPercent = newPercent;
                        this.percentString = String.format("%d%%", Integer.valueOf(newPercent));
                        this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(format));
                    }
                    canvas.drawText(this.percentString, x4, y5, this.textPaint);
                    if (this.currentIcon != this.nextIcon) {
                        canvas.restore();
                    }
                }
                i2 = this.currentIcon;
                if (i2 != 0 || i2 == 1 || (i8 = this.nextIcon) == 0 || i8 == 1) {
                    i7 = i2 != 0 ? 1 : 1;
                    if (i2 == i7 || this.nextIcon != 0) {
                        p = i2 != 1 ? 1.0f : 0.0f;
                        int i23 = this.nextIcon;
                        int i24 = i23 != 0 ? 1 : 1;
                        if (i2 != 0 || i2 == i24) {
                            i3 = 255;
                            this.paint2.setAlpha(255);
                            applyShaderMatrix(true);
                            canvas.save();
                            canvas.translate(bounds.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - p)), bounds.centerY());
                            ms = 500.0f * p;
                            i5 = this.currentIcon;
                            float rotation6 = i5 != 1 ? 90.0f : 0.0f;
                            if (i5 != 0 && this.nextIcon == 1) {
                                if (ms < 384.0f) {
                                    rotation6 = CubicBezierInterpolator.EASE_BOTH.getInterpolation(ms / 384.0f) * 95.0f;
                                } else if (ms < 484.0f) {
                                    rotation6 = 95.0f - (CubicBezierInterpolator.EASE_BOTH.getInterpolation((ms - 384.0f) / 100.0f) * 5.0f);
                                } else {
                                    rotation6 = 90.0f;
                                }
                                ms += 100.0f;
                            } else if (i5 == 1 && this.nextIcon == 0) {
                                if (ms >= 100.0f) {
                                    rotation6 = CubicBezierInterpolator.EASE_BOTH.getInterpolation(ms / 100.0f) * (-5.0f);
                                } else if (ms < 484.0f) {
                                    rotation6 = (CubicBezierInterpolator.EASE_BOTH.getInterpolation((ms - 100.0f) / 384.0f) * 95.0f) - 5.0f;
                                } else {
                                    rotation6 = 90.0f;
                                }
                            }
                            canvas.rotate(rotation6);
                            i6 = this.currentIcon;
                            if ((i6 != 0 && i6 != 1) || i6 == 4) {
                                canvas.scale(drawableScale, drawableScale);
                            }
                            Theme.playPauseAnimator.draw(canvas, this.paint2, ms);
                            canvas.scale(1.0f, -1.0f);
                            Theme.playPauseAnimator.draw(canvas, this.paint2, ms);
                            canvas.restore();
                        }
                        if (i23 == 4) {
                            this.paint2.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                            i3 = 255;
                        } else {
                            this.paint2.setAlpha(i2 == i23 ? 255 : (int) (this.transitionProgress * 255.0f));
                            i3 = 255;
                        }
                        applyShaderMatrix(true);
                        canvas.save();
                        canvas.translate(bounds.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - p)), bounds.centerY());
                        ms = 500.0f * p;
                        i5 = this.currentIcon;
                        if (i5 != 1) {
                        }
                        if (i5 != 0) {
                        }
                        if (i5 == 1) {
                            if (ms >= 100.0f) {
                            }
                        }
                        canvas.rotate(rotation6);
                        i6 = this.currentIcon;
                        if (i6 != 0) {
                            canvas.scale(drawableScale, drawableScale);
                            Theme.playPauseAnimator.draw(canvas, this.paint2, ms);
                            canvas.scale(1.0f, -1.0f);
                            Theme.playPauseAnimator.draw(canvas, this.paint2, ms);
                            canvas.restore();
                        }
                        canvas.scale(drawableScale, drawableScale);
                        Theme.playPauseAnimator.draw(canvas, this.paint2, ms);
                        canvas.scale(1.0f, -1.0f);
                        Theme.playPauseAnimator.draw(canvas, this.paint2, ms);
                        canvas.restore();
                    }
                    if (this.animatingTransition) {
                        if (this.nextIcon == 0) {
                            p = 1.0f - this.transitionProgress;
                        } else {
                            p = this.transitionProgress;
                        }
                    } else {
                        p = this.nextIcon == 1 ? 1.0f : 0.0f;
                    }
                    int i232 = this.nextIcon;
                    if (i232 != 0) {
                    }
                    if (i2 != 0) {
                    }
                    i3 = 255;
                    this.paint2.setAlpha(255);
                    applyShaderMatrix(true);
                    canvas.save();
                    canvas.translate(bounds.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - p)), bounds.centerY());
                    ms = 500.0f * p;
                    i5 = this.currentIcon;
                    if (i5 != 1) {
                    }
                    if (i5 != 0) {
                    }
                    if (i5 == 1) {
                    }
                    canvas.rotate(rotation6);
                    i6 = this.currentIcon;
                    if (i6 != 0) {
                    }
                    canvas.scale(drawableScale, drawableScale);
                    Theme.playPauseAnimator.draw(canvas, this.paint2, ms);
                    canvas.scale(1.0f, -1.0f);
                    Theme.playPauseAnimator.draw(canvas, this.paint2, ms);
                    canvas.restore();
                } else {
                    i3 = 255;
                }
                if (this.currentIcon != 6 || this.nextIcon == 6) {
                    applyShaderMatrix(false);
                    if (this.currentIcon == 6) {
                        float f9 = this.transitionProgress;
                        if (f9 > 0.5f) {
                            float progress8 = (f9 - 0.5f) / 0.5f;
                            progress12 = 1.0f - Math.min(1.0f, progress8 / 0.5f);
                            progress = progress8 > 0.5f ? (progress8 - 0.5f) / 0.5f : 0.0f;
                        } else {
                            progress12 = 1.0f;
                            progress = 0.0f;
                        }
                        this.paint.setAlpha(i3);
                        progress2 = progress;
                        progress1 = progress12;
                    } else {
                        if (this.nextIcon != 6) {
                            this.paint.setAlpha((int) ((1.0f - this.transitionProgress) * 255.0f));
                        } else {
                            this.paint.setAlpha(i3);
                        }
                        progress2 = 1.0f;
                        progress1 = 0.0f;
                    }
                    int y6 = cy + AndroidUtilities.dp(7.0f);
                    int x5 = cx - AndroidUtilities.dp(3.0f);
                    if (progress1 >= 1.0f) {
                        x = x5;
                        y = y6;
                        canvas.drawLine(x5 - AndroidUtilities.dp(6.0f), y6 - AndroidUtilities.dp(6.0f), x5 - (AndroidUtilities.dp(6.0f) * progress1), y6 - (AndroidUtilities.dp(6.0f) * progress1), this.paint);
                    } else {
                        y = y6;
                        x = x5;
                    }
                    if (progress2 > 0.0f) {
                        canvas.drawLine(x, y, (AndroidUtilities.dp(12.0f) * progress2) + x, y - (AndroidUtilities.dp(12.0f) * progress2), this.paint);
                    }
                }
                if (nextDrawable3 == null) {
                    nextDrawable4 = nextDrawable2;
                    if (nextDrawable3 != nextDrawable4) {
                        int w = (int) (nextDrawable3.getIntrinsicWidth() * previowsDrawableScale);
                        int h = (int) (nextDrawable3.getIntrinsicHeight() * previowsDrawableScale);
                        nextDrawable3.setColorFilter(this.colorFilter);
                        nextDrawable3.setAlpha(this.currentIcon == this.nextIcon ? 255 : (int) ((1.0f - this.transitionProgress) * 255.0f));
                        nextDrawable3.setBounds(cx - (w / 2), cy - (h / 2), (w / 2) + cx, (h / 2) + cy);
                        nextDrawable3.draw(canvas);
                    }
                } else {
                    nextDrawable4 = nextDrawable2;
                }
                if (nextDrawable4 != null) {
                    int w2 = (int) (nextDrawable4.getIntrinsicWidth() * drawableScale);
                    int h2 = (int) (nextDrawable4.getIntrinsicHeight() * drawableScale);
                    nextDrawable4.setColorFilter(this.colorFilter);
                    nextDrawable4.setAlpha(this.currentIcon == this.nextIcon ? 255 : (int) (this.transitionProgress * 255.0f));
                    nextDrawable4.setBounds(cx - (w2 / 2), cy - (h2 / 2), (w2 / 2) + cx, (h2 / 2) + cy);
                    nextDrawable4.draw(canvas);
                }
                if (previousPath2 == null) {
                    nextPath3 = nextPath2;
                    previousPath3 = previousPath2;
                    if (previousPath3 != nextPath3) {
                        int size = AndroidUtilities.dp(24.0f);
                        this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                        this.paint2.setAlpha(this.currentIcon == this.nextIcon ? 255 : (int) ((1.0f - this.transitionProgress) * 255.0f));
                        applyShaderMatrix(true);
                        canvas.save();
                        canvas.translate(cx, cy);
                        float previowsDrawableScale2 = previowsDrawableScale;
                        canvas.scale(previowsDrawableScale2, previowsDrawableScale2);
                        canvas.translate((-size) / 2, (-size) / 2);
                        if (previousPath3[0] != null) {
                            canvas.drawPath(previousPath3[0], this.paint2);
                        }
                        if (previousPath3[1] != null) {
                            canvas.drawPath(previousPath3[1], this.backPaint);
                        }
                        canvas.restore();
                    }
                } else {
                    nextPath3 = nextPath2;
                    previousPath3 = previousPath2;
                }
                if (nextPath3 != null) {
                    int size2 = AndroidUtilities.dp(24.0f);
                    int alpha7 = this.currentIcon == this.nextIcon ? 255 : (int) (this.transitionProgress * 255.0f);
                    this.paint2.setStyle(Paint.Style.FILL_AND_STROKE);
                    this.paint2.setAlpha(alpha7);
                    applyShaderMatrix(true);
                    canvas.save();
                    canvas.translate(cx, cy);
                    canvas.scale(drawableScale, drawableScale);
                    canvas.translate((-size2) / 2, (-size2) / 2);
                    if (nextPath3[0] != null) {
                        canvas.drawPath(nextPath3[0], this.paint2);
                    }
                    if (nextPath3.length >= 3 && nextPath3[2] != null) {
                        canvas.drawPath(nextPath3[2], this.paint);
                    }
                    if (nextPath3[1] != null) {
                        if (alpha7 == 255) {
                            canvas.drawPath(nextPath3[1], this.backPaint);
                        } else {
                            int backgroundAlpha = this.backPaint.getAlpha();
                            this.backPaint.setAlpha((int) (backgroundAlpha * (alpha7 / 255.0f)));
                            canvas.drawPath(nextPath3[1], this.backPaint);
                            this.backPaint.setAlpha(backgroundAlpha);
                        }
                    }
                    canvas.restore();
                }
                long newTime = System.currentTimeMillis();
                dt = newTime - this.lastAnimationTime;
                if (dt > 17) {
                    dt = 17;
                }
                this.lastAnimationTime = newTime;
                i4 = this.currentIcon;
                if (i4 == 3 && i4 != 14) {
                    if (i4 != 4) {
                    }
                    if (i4 != 10 && i4 != 13) {
                        if (this.animatingTransition) {
                            float f10 = this.transitionProgress;
                            if (f10 < 1.0f) {
                                float f11 = f10 + (((float) dt) / this.transitionAnimationTime);
                                this.transitionProgress = f11;
                                if (f11 >= 1.0f) {
                                    this.currentIcon = this.nextIcon;
                                    this.transitionProgress = 1.0f;
                                    this.animatingTransition = false;
                                }
                                invalidateSelf();
                            }
                        }
                        saveCount3 = saveCount2;
                        if (saveCount3 >= 1) {
                            canvas.restoreToCount(saveCount3);
                            return;
                        }
                        return;
                    }
                }
                float f12 = this.downloadRadOffset + (((float) (dt * 360)) / 2500.0f);
                this.downloadRadOffset = f12;
                this.downloadRadOffset = getCircleValue(f12);
                if (this.nextIcon == 2) {
                    float f13 = this.downloadProgress;
                    float f14 = this.downloadProgressAnimationStart;
                    float progressDiff = f13 - f14;
                    if (progressDiff > 0.0f) {
                        float f15 = this.downloadProgressTime + ((float) dt);
                        this.downloadProgressTime = f15;
                        if (f15 < 200.0f) {
                            this.animatedDownloadProgress = f14 + (this.interpolator.getInterpolation(f15 / 200.0f) * progressDiff);
                        } else {
                            this.animatedDownloadProgress = f13;
                            this.downloadProgressAnimationStart = f13;
                            this.downloadProgressTime = 0.0f;
                        }
                    }
                }
                invalidateSelf();
                if (this.animatingTransition) {
                }
                saveCount3 = saveCount2;
                if (saveCount3 >= 1) {
                }
            }
        }
        applyShaderMatrix(false);
        float progressScaleY2 = 0.0f;
        int i25 = this.nextIcon;
        if (i25 == 2) {
            float f16 = this.transitionProgress;
            if (f16 <= 0.5f) {
                float progress9 = f16 / 0.5f;
                float backProgress = 1.0f - progress9;
                d2 = AndroidUtilities.dp(7.0f) * backProgress * this.scale;
                alpha3 = (int) (backProgress * 255.0f);
            } else {
                d2 = 0.0f;
                alpha3 = 0;
            }
            progressScale = 1.0f;
            rotation = 0.0f;
            progressScaleX = 0.0f;
            iconScale = d2;
            iconScaleX = 0.0f;
            d = 0.0f;
            alpha = alpha3;
            iconScale2 = 1.0f;
            iconScale3 = 1.0f;
        } else if (i25 == 15 || i25 == 0 || i25 == 1 || i25 == 5 || i25 == 8 || i25 == 9 || i25 == 7 || i25 == 6) {
            if (i25 == 6) {
                f2 = 1.0f;
                progress3 = Math.min(1.0f, this.transitionProgress / 0.5f);
            } else {
                f2 = 1.0f;
                progress3 = this.transitionProgress;
            }
            float backProgress2 = f2 - progress3;
            float iconScale5 = f2 - progress3;
            float progressScaleX3 = bounds.centerX();
            float iconScaleY4 = bounds.centerY();
            progressScaleY2 = iconScaleY4;
            float d6 = this.scale * AndroidUtilities.dp(7.0f) * backProgress2;
            iconScale3 = 1.0f;
            int alpha8 = (int) (Math.min(1.0f, backProgress2 * 2.0f) * 255.0f);
            progressScale = 1.0f;
            iconScaleX = progressScaleX3;
            rotation = progressScaleX3;
            progressScaleX = 0.0f;
            d = iconScaleY4;
            alpha = alpha8;
            iconScale2 = iconScale5;
            iconScale = d6;
        } else if (i25 == 4) {
            float progress10 = this.transitionProgress;
            float backProgress3 = 1.0f - progress10;
            float d7 = this.scale * AndroidUtilities.dp(7.0f);
            int alpha9 = (int) (backProgress3 * 255.0f);
            if (this.currentIcon == 14) {
                float iconScaleX4 = bounds.left;
                progressScaleX2 = iconScaleX4;
                float f17 = bounds.top;
                progressScaleY2 = f17;
                iconScaleY3 = f17;
                iconScaleX3 = iconScaleX4;
            } else {
                float iconScaleX5 = bounds.centerX();
                progressScaleX2 = iconScaleX5;
                float centerY = bounds.centerY();
                progressScaleY2 = centerY;
                iconScaleY3 = centerY;
                iconScaleX3 = iconScaleX5;
            }
            rotation = progressScaleX2;
            d = iconScaleY3;
            alpha = alpha9;
            progressScaleX = 0.0f;
            iconScale2 = backProgress3;
            iconScale3 = 1.0f;
            iconScaleX = iconScaleX3;
            iconScale = d7;
            progressScale = 1.0f;
        } else if (i25 == 14 || i25 == 3) {
            float progress11 = this.transitionProgress;
            float backProgress4 = 1.0f - progress11;
            if (this.currentIcon == 4) {
                rotation3 = 0.0f;
                progressScale2 = 1.0f;
                iconScale4 = progress11;
            } else {
                rotation3 = 45.0f * backProgress4;
                progressScale2 = 1.0f;
                iconScale4 = 1.0f;
            }
            float d8 = this.scale * AndroidUtilities.dp(7.0f);
            int alpha10 = (int) (progress11 * 255.0f);
            float iconScale6 = iconScale4;
            float progressScale3 = progressScale2;
            if (this.nextIcon == 14) {
                float iconScaleX6 = bounds.left;
                float progressScaleY3 = bounds.top;
                iconScaleY2 = progressScaleY3;
                progressScaleY2 = progressScaleY3;
                iconScaleX2 = iconScaleX6;
                progressScaleY = iconScaleX6;
            } else {
                float iconScaleX7 = bounds.centerX();
                float progressScaleY4 = bounds.centerY();
                iconScaleY2 = progressScaleY4;
                progressScaleY2 = progressScaleY4;
                iconScaleX2 = iconScaleX7;
                progressScaleY = iconScaleX7;
            }
            iconScaleX = iconScaleX2;
            iconScale2 = iconScale6;
            iconScale3 = 1.0f;
            iconScale = d8;
            d = iconScaleY2;
            alpha = alpha10;
            rotation = progressScaleY;
            progressScaleX = rotation3;
            progressScale = progressScale3;
        } else {
            progressScale = 1.0f;
            rotation = 0.0f;
            progressScaleX = 0.0f;
            iconScale = AndroidUtilities.dp(7.0f) * this.scale;
            iconScaleX = 0.0f;
            d = 0.0f;
            alpha = 255;
            iconScale2 = 1.0f;
            iconScale3 = 1.0f;
        }
        if (iconScale2 != iconScale3) {
            canvas.save();
            canvas.scale(iconScale2, iconScale2, iconScaleX, d);
        }
        if (progressScaleX != 0.0f) {
            canvas.save();
            canvas.rotate(progressScaleX, cx, cy);
        }
        if (alpha != 0) {
            this.paint.setAlpha((int) (alpha * this.overrideAlpha));
            if (this.currentIcon != 14) {
                if (this.nextIcon == 14) {
                    rotation2 = progressScaleX;
                    alpha2 = alpha;
                    iconScaleY = progressScaleY2;
                } else {
                    float iconScaleY5 = cy + iconScale;
                    rotation2 = progressScaleX;
                    alpha2 = alpha;
                    iconScaleY = progressScaleY2;
                    canvas.drawLine(cx - iconScale, cy - iconScale, cx + iconScale, iconScaleY5, this.paint);
                    canvas.drawLine(cx + iconScale, cy - iconScale, cx - iconScale, cy + iconScale, this.paint);
                }
            } else {
                rotation2 = progressScaleX;
                alpha2 = alpha;
                iconScaleY = progressScaleY2;
            }
            this.paint3.setAlpha((int) (alpha2 * this.overrideAlpha));
            this.rect.set(cx - AndroidUtilities.dp(3.5f), cy - AndroidUtilities.dp(3.5f), AndroidUtilities.dp(3.5f) + cx, AndroidUtilities.dp(3.5f) + cy);
            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), this.paint3);
        } else {
            rotation2 = progressScaleX;
            alpha2 = alpha;
            iconScaleY = progressScaleY2;
        }
        if (rotation2 != 0.0f) {
            canvas.restore();
        }
        if (iconScale2 != 1.0f) {
            canvas.restore();
        }
        if (progressScale != 1.0f) {
            canvas.save();
            canvas.scale(progressScale, progressScale, rotation, iconScaleY);
        }
        int i26 = this.currentIcon;
        if ((i26 == 3 || i26 == 14 || (i26 == 4 && ((i13 = this.nextIcon) == 14 || i13 == 3))) && alpha2 != 0) {
            float rad2 = Math.max(4.0f, this.animatedDownloadProgress * 360.0f);
            int diff3 = AndroidUtilities.dp(this.isMini ? 2.0f : 4.0f);
            this.rect.set(bounds.left + diff3, bounds.top + diff3, bounds.right - diff3, bounds.bottom - diff3);
            int i27 = this.currentIcon;
            if (i27 == 14 || (i27 == 4 && this.nextIcon == 14)) {
                this.paint.setAlpha((int) (alpha2 * 0.15f * this.overrideAlpha));
                canvas.drawArc(this.rect, 0.0f, 360.0f, false, this.paint);
                this.paint.setAlpha(alpha2);
            }
            canvas.drawArc(this.rect, this.downloadRadOffset, rad2, false, this.paint);
        }
        if (progressScale != 1.0f) {
            canvas.restore();
        }
        Drawable nextDrawable52 = null;
        Drawable previousDrawable22 = null;
        Path[] nextPath42 = null;
        Path[] previousPath42 = null;
        i = this.currentIcon;
        if (i != this.nextIcon) {
        }
        if (this.nextIcon != 15) {
        }
        if (this.nextIcon != 5) {
        }
        if (this.nextIcon != 7) {
        }
        if (this.nextIcon != 8) {
        }
        if (this.currentIcon != 9) {
        }
        applyShaderMatrix(false);
        this.paint.setAlpha(this.currentIcon != this.nextIcon ? 255 : (int) (this.transitionProgress * 255.0f));
        int y42 = cy + AndroidUtilities.dp(7.0f);
        int x32 = cx - AndroidUtilities.dp(3.0f);
        if (this.currentIcon != this.nextIcon) {
        }
        nextPath2 = nextPath;
        previousPath2 = previousPath;
        previowsDrawableScale = drawableScale2;
        nextDrawable2 = nextDrawable;
        nextDrawable3 = previousDrawable;
        canvas.drawLine(x32 - AndroidUtilities.dp(6.0f), y42 - AndroidUtilities.dp(6.0f), x32, y42, this.paint);
        canvas.drawLine(x32, y42, x32 + AndroidUtilities.dp(12.0f), y42 - AndroidUtilities.dp(12.0f), this.paint);
        if (this.currentIcon != this.nextIcon) {
        }
        if (this.currentIcon != 12) {
        }
        applyShaderMatrix(false);
        i11 = this.currentIcon;
        i12 = this.nextIcon;
        if (i11 != i12) {
        }
        this.paint.setAlpha(i11 != i12 ? 255 : (int) (transition2 * 255.0f));
        int dp3 = cy + AndroidUtilities.dp(7.0f);
        int dp22 = cx - AndroidUtilities.dp(3.0f);
        if (this.currentIcon != this.nextIcon) {
        }
        float d52 = AndroidUtilities.dp(7.0f) * this.scale;
        canvas.drawLine(cx - d52, cy - d52, cx + d52, cy + d52, this.paint);
        canvas.drawLine(cx + d52, cy - d52, cx - d52, cy + d52, this.paint);
        if (this.currentIcon != this.nextIcon) {
        }
        if (this.currentIcon != 13) {
        }
        applyShaderMatrix(false);
        i9 = this.currentIcon;
        i10 = this.nextIcon;
        if (i9 != i10) {
        }
        this.textPaint.setAlpha((int) (transition * 255.0f));
        int y52 = AndroidUtilities.dp(5.0f) + cy;
        int x42 = cx - (this.percentStringWidth / 2);
        if (this.currentIcon != this.nextIcon) {
        }
        newPercent = (int) (this.animatedDownloadProgress * 100.0f);
        if (this.percentString != null) {
        }
        this.lastPercent = newPercent;
        this.percentString = String.format("%d%%", Integer.valueOf(newPercent));
        this.percentStringWidth = (int) Math.ceil(this.textPaint.measureText(format));
        canvas.drawText(this.percentString, x42, y52, this.textPaint);
        if (this.currentIcon != this.nextIcon) {
        }
        i2 = this.currentIcon;
        if (i2 != 0) {
        }
        if (i2 != 0) {
        }
        if (i2 == i7) {
        }
        if (i2 != 1) {
        }
        int i2322 = this.nextIcon;
        if (i2322 != 0) {
        }
        if (i2 != 0) {
        }
        i3 = 255;
        this.paint2.setAlpha(255);
        applyShaderMatrix(true);
        canvas.save();
        canvas.translate(bounds.centerX() + (AndroidUtilities.dp(1.0f) * (1.0f - p)), bounds.centerY());
        ms = 500.0f * p;
        i5 = this.currentIcon;
        if (i5 != 1) {
        }
        if (i5 != 0) {
        }
        if (i5 == 1) {
        }
        canvas.rotate(rotation6);
        i6 = this.currentIcon;
        if (i6 != 0) {
        }
        canvas.scale(drawableScale, drawableScale);
        Theme.playPauseAnimator.draw(canvas, this.paint2, ms);
        canvas.scale(1.0f, -1.0f);
        Theme.playPauseAnimator.draw(canvas, this.paint2, ms);
        canvas.restore();
        if (this.currentIcon != 6) {
        }
        applyShaderMatrix(false);
        if (this.currentIcon == 6) {
        }
        int y62 = cy + AndroidUtilities.dp(7.0f);
        int x52 = cx - AndroidUtilities.dp(3.0f);
        if (progress1 >= 1.0f) {
        }
        if (progress2 > 0.0f) {
        }
        if (nextDrawable3 == null) {
        }
        if (nextDrawable4 != null) {
        }
        if (previousPath2 == null) {
        }
        if (nextPath3 != null) {
        }
        long newTime2 = System.currentTimeMillis();
        dt = newTime2 - this.lastAnimationTime;
        if (dt > 17) {
        }
        this.lastAnimationTime = newTime2;
        i4 = this.currentIcon;
        if (i4 == 3) {
        }
        float f122 = this.downloadRadOffset + (((float) (dt * 360)) / 2500.0f);
        this.downloadRadOffset = f122;
        this.downloadRadOffset = getCircleValue(f122);
        if (this.nextIcon == 2) {
        }
        invalidateSelf();
        if (this.animatingTransition) {
        }
        saveCount3 = saveCount2;
        if (saveCount3 >= 1) {
        }
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
