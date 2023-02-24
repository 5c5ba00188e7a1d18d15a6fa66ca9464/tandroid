package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import androidx.core.math.MathUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.MotionBackgroundDrawable;
/* loaded from: classes3.dex */
public class ActionBarMenuSlider extends FrameLayout {
    private Bitmap blurBitmap;
    private AnimatedFloat blurBitmapAlpha;
    private Matrix blurBitmapMatrix;
    private BitmapShader blurBitmapShader;
    private boolean blurIsInChat;
    private Paint blurPaint;
    private Paint brightenBlurPaint;
    private Paint darkenBlurPaint;
    private boolean dragging;
    private boolean drawShadow;
    private Paint fillPaint;
    private float fromValue;
    private float fromX;
    private int[] location;
    private Utilities.Callback2<Float, Boolean> onValueChange;
    private Runnable prepareBlur;
    private boolean preparingBlur;
    private int pseudoBlurColor1;
    private int pseudoBlurColor2;
    private LinearGradient pseudoBlurGradient;
    private Matrix pseudoBlurMatrix;
    private Paint pseudoBlurPaint;
    private int pseudoBlurWidth;
    private Theme.ResourcesProvider resourcesProvider;
    private float roundRadiusDp;
    private Paint shadowPaint;
    private long tapStart;
    private float value;
    private ValueAnimator valueAnimator;

    public ActionBarMenuSlider(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.value = 0.5f;
        this.blurBitmapAlpha = new AnimatedFloat(1.0f, this, 0L, 320L, CubicBezierInterpolator.EASE_OUT_QUINT);
        this.location = new int[2];
        this.roundRadiusDp = 6.0f;
        this.shadowPaint = new Paint(1);
        this.blurPaint = new Paint(1);
        this.brightenBlurPaint = new Paint(1);
        this.darkenBlurPaint = new Paint(1);
        this.pseudoBlurPaint = new Paint(1);
        this.fillPaint = new Paint(1);
        this.blurIsInChat = true;
        this.preparingBlur = false;
        this.prepareBlur = new Runnable() { // from class: org.telegram.ui.ActionBar.ActionBarMenuSlider$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ActionBarMenuSlider.this.lambda$new$2();
            }
        };
        this.resourcesProvider = resourcesProvider;
        setWillNotDraw(false);
        this.shadowPaint.setColor(0);
        this.shadowPaint.setShadowLayer(AndroidUtilities.dpf2(1.33f), 0.0f, AndroidUtilities.dpf2(0.33f), 1056964608);
        setDrawShadow(true);
        ColorMatrix colorMatrix = new ColorMatrix();
        AndroidUtilities.adjustSaturationColorMatrix(colorMatrix, -0.4f);
        AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, 0.1f);
        this.pseudoBlurPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        this.darkenBlurPaint.setColor(Theme.multAlpha(-16777216, 0.025f));
        this.brightenBlurPaint.setColor(Theme.multAlpha(-1, 0.35f));
    }

    public void setValue(float f, boolean z) {
        ValueAnimator valueAnimator = this.valueAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.valueAnimator = null;
        }
        if (!z) {
            this.value = MathUtils.clamp(f, 0.0f, 1.0f);
            invalidate();
            return;
        }
        final float clamp = MathUtils.clamp(f, 0.0f, 1.0f);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.value, clamp);
        this.valueAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ActionBar.ActionBarMenuSlider$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ActionBarMenuSlider.this.lambda$setValue$0(valueAnimator2);
            }
        });
        this.valueAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.ActionBarMenuSlider.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ActionBarMenuSlider.this.valueAnimator = null;
                ActionBarMenuSlider.this.value = clamp;
                ActionBarMenuSlider.this.invalidate();
            }
        });
        this.valueAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.valueAnimator.setDuration(220L);
        this.valueAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setValue$0(ValueAnimator valueAnimator) {
        this.value = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    private void updateValue(float f, boolean z) {
        setValue(f, false);
        Utilities.Callback2<Float, Boolean> callback2 = this.onValueChange;
        if (callback2 != null) {
            callback2.run(Float.valueOf(this.value), Boolean.valueOf(z));
        }
    }

    public void setOnValueChange(Utilities.Callback2<Float, Boolean> callback2) {
        this.onValueChange = callback2;
    }

    public void setDrawShadow(boolean z) {
        this.drawShadow = z;
        int dp = z ? AndroidUtilities.dp(8.0f) : 0;
        setPadding(dp, dp, dp, dp);
        invalidate();
    }

    public void setRoundRadiusDp(float f) {
        this.roundRadiusDp = f;
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        this.preparingBlur = true;
        AndroidUtilities.makeGlobalBlurBitmap(new Utilities.Callback() { // from class: org.telegram.ui.ActionBar.ActionBarMenuSlider$$ExternalSyntheticLambda2
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                ActionBarMenuSlider.this.lambda$new$1((Bitmap) obj);
            }
        }, 8.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Bitmap bitmap) {
        this.preparingBlur = false;
        this.blurBitmap = bitmap;
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        this.blurBitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
        Matrix matrix = this.blurBitmapMatrix;
        if (matrix == null) {
            this.blurBitmapMatrix = new Matrix();
        } else {
            matrix.reset();
        }
        this.blurBitmapMatrix.postScale(8.0f, 8.0f);
        Matrix matrix2 = this.blurBitmapMatrix;
        int[] iArr = this.location;
        matrix2.postTranslate(-iArr[0], -iArr[1]);
        this.blurBitmapShader.setLocalMatrix(this.blurBitmapMatrix);
        this.blurPaint.setShader(this.blurBitmapShader);
        ColorMatrix colorMatrix = new ColorMatrix();
        AndroidUtilities.adjustSaturationColorMatrix(colorMatrix, -0.2f);
        this.blurPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        invalidate();
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i) + getPaddingRight() + getPaddingLeft(), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44.0f) + getPaddingTop() + getPaddingBottom(), 1073741824));
        boolean z = SharedConfig.getDevicePerformanceClass() >= 2 && LiteMode.isEnabled(LiteMode.FLAG_CHAT_BLUR);
        if (this.blurBitmap == null && !this.preparingBlur && z) {
            this.prepareBlur.run();
        }
    }

    public void invalidateBlur(boolean z) {
        this.blurIsInChat = z;
        this.blurPaint.setShader(null);
        this.blurBitmapShader = null;
        Bitmap bitmap = this.blurBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.blurBitmap = null;
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        getLocationOnScreen(this.location);
        Matrix matrix = this.blurBitmapMatrix;
        if (matrix != null) {
            matrix.reset();
            this.blurBitmapMatrix.postScale(8.0f, 8.0f);
            Matrix matrix2 = this.blurBitmapMatrix;
            int[] iArr = this.location;
            matrix2.postTranslate(-iArr[0], -iArr[1]);
            BitmapShader bitmapShader = this.blurBitmapShader;
            if (bitmapShader != null) {
                bitmapShader.setLocalMatrix(this.blurBitmapMatrix);
                invalidate();
            }
        }
        updatePseudoBlurColors();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = AndroidUtilities.rectTmp;
        rectF.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        if (this.drawShadow) {
            canvas.drawRoundRect(rectF, AndroidUtilities.dp(this.roundRadiusDp), AndroidUtilities.dp(this.roundRadiusDp), this.shadowPaint);
        }
        float f = this.blurBitmapAlpha.set(this.blurBitmap != null ? 1.0f : 0.0f);
        if (f < 1.0f) {
            if (this.pseudoBlurMatrix == null || this.pseudoBlurWidth != ((int) rectF.width())) {
                Matrix matrix = this.pseudoBlurMatrix;
                if (matrix == null) {
                    this.pseudoBlurMatrix = new Matrix();
                } else {
                    matrix.reset();
                }
                Matrix matrix2 = this.pseudoBlurMatrix;
                int width = (int) rectF.width();
                this.pseudoBlurWidth = width;
                matrix2.postScale(width, 1.0f);
                this.pseudoBlurGradient.setLocalMatrix(this.pseudoBlurMatrix);
            }
            this.pseudoBlurPaint.setAlpha((int) ((1.0f - f) * 255.0f));
            canvas.drawRoundRect(rectF, AndroidUtilities.dp(this.roundRadiusDp), AndroidUtilities.dp(this.roundRadiusDp), this.pseudoBlurPaint);
        }
        if (this.blurBitmap != null && this.value < 1.0f && f > 0.0f) {
            this.blurPaint.setAlpha((int) (f * 255.0f));
            canvas.drawRoundRect(rectF, AndroidUtilities.dp(this.roundRadiusDp), AndroidUtilities.dp(this.roundRadiusDp), this.blurPaint);
        }
        canvas.drawRoundRect(rectF, AndroidUtilities.dp(this.roundRadiusDp), AndroidUtilities.dp(this.roundRadiusDp), this.brightenBlurPaint);
        canvas.drawRoundRect(rectF, AndroidUtilities.dp(this.roundRadiusDp), AndroidUtilities.dp(this.roundRadiusDp), this.darkenBlurPaint);
        this.fillPaint.setColor(-1);
        if (this.value < 1.0f) {
            canvas.save();
            canvas.clipRect(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + (((getWidth() - getPaddingLeft()) - getPaddingRight()) * this.value), getHeight() - getPaddingBottom());
        }
        canvas.drawRoundRect(rectF, AndroidUtilities.dp(this.roundRadiusDp), AndroidUtilities.dp(this.roundRadiusDp), this.fillPaint);
        if (this.value < 1.0f) {
            canvas.restore();
        }
    }

    private Pair<Integer, Integer> getBitmapGradientColors(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int[] iArr = this.location;
        float f = iArr[0] / AndroidUtilities.displaySize.x;
        float measuredWidth = (iArr[0] + getMeasuredWidth()) / AndroidUtilities.displaySize.x;
        float currentActionBarHeight = ((this.location[1] - AndroidUtilities.statusBarHeight) - ActionBar.getCurrentActionBarHeight()) / AndroidUtilities.displaySize.y;
        int width = (int) (f * bitmap.getWidth());
        int width2 = (int) (measuredWidth * bitmap.getWidth());
        int height = (int) (currentActionBarHeight * bitmap.getHeight());
        if (width < 0 || width >= bitmap.getWidth() || width2 < 0 || width2 >= bitmap.getWidth() || height < 0 || height >= bitmap.getHeight()) {
            return null;
        }
        return new Pair<>(Integer.valueOf(bitmap.getPixel(width, height)), Integer.valueOf(bitmap.getPixel(width2, height)));
    }

    private void updatePseudoBlurColors() {
        int color;
        int i;
        if (this.blurIsInChat) {
            Drawable cachedWallpaper = Theme.getCachedWallpaper();
            if (cachedWallpaper instanceof ColorDrawable) {
                color = ((ColorDrawable) cachedWallpaper).getColor();
            } else {
                Bitmap bitmap = null;
                if (cachedWallpaper instanceof MotionBackgroundDrawable) {
                    bitmap = ((MotionBackgroundDrawable) cachedWallpaper).getBitmap();
                } else if (cachedWallpaper instanceof BitmapDrawable) {
                    bitmap = ((BitmapDrawable) cachedWallpaper).getBitmap();
                }
                Pair<Integer, Integer> bitmapGradientColors = getBitmapGradientColors(bitmap);
                if (bitmapGradientColors != null) {
                    int intValue = ((Integer) bitmapGradientColors.first).intValue();
                    i = ((Integer) bitmapGradientColors.second).intValue();
                    color = intValue;
                    if (this.pseudoBlurGradient == null && this.pseudoBlurColor1 == color && this.pseudoBlurColor2 == i) {
                        return;
                    }
                    this.pseudoBlurColor1 = color;
                    this.pseudoBlurColor2 = i;
                    LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 1.0f, 0.0f, new int[]{color, i}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                    this.pseudoBlurGradient = linearGradient;
                    this.pseudoBlurPaint.setShader(linearGradient);
                }
                color = Theme.multAlpha(Theme.getColor("windowBackgroundWhite", this.resourcesProvider), 0.25f);
            }
        } else {
            color = Theme.getColor("windowBackgroundWhite", this.resourcesProvider);
            if (!Theme.isCurrentThemeDark()) {
                color = Theme.blendOver(color, Theme.multAlpha(-16777216, 0.18f));
            }
        }
        i = color;
        if (this.pseudoBlurGradient == null) {
        }
        this.pseudoBlurColor1 = color;
        this.pseudoBlurColor2 = i;
        LinearGradient linearGradient2 = new LinearGradient(0.0f, 0.0f, 1.0f, 0.0f, new int[]{color, i}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
        this.pseudoBlurGradient = linearGradient2;
        this.pseudoBlurPaint.setShader(linearGradient2);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX() - getPaddingLeft();
        int action = motionEvent.getAction();
        if (action == 0) {
            this.dragging = true;
            this.fromX = x;
            this.fromValue = this.value;
            this.tapStart = System.currentTimeMillis();
            return true;
        } else if (action == 2 || action == 1) {
            if (action == 1) {
                this.dragging = false;
                if (System.currentTimeMillis() - this.tapStart < ViewConfiguration.getTapTimeout()) {
                    float paddingLeft = (x - getPaddingLeft()) / ((getWidth() - getPaddingLeft()) - getPaddingRight());
                    Utilities.Callback2<Float, Boolean> callback2 = this.onValueChange;
                    if (callback2 != null) {
                        callback2.run(Float.valueOf(paddingLeft), Boolean.TRUE);
                    }
                    return true;
                }
            }
            updateValue(this.fromValue + ((x - this.fromX) / Math.max(1, (getWidth() - getPaddingLeft()) - getPaddingRight())), !this.dragging);
            return true;
        } else {
            return super.onTouchEvent(motionEvent);
        }
    }
}
