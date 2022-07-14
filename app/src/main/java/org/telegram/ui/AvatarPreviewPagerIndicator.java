package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextPaint;
import android.view.View;
import java.util.Arrays;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.ProfileGalleryView;
/* loaded from: classes4.dex */
public class AvatarPreviewPagerIndicator extends View implements ProfileGalleryView.Callback {
    private final ValueAnimator animator;
    private final Paint backgroundPaint;
    private final Paint barPaint;
    private final GradientDrawable bottomOverlayGradient;
    private float currentAnimationValue;
    private float currentLoadingAnimationProgress;
    private float currentProgress;
    private boolean isOverlaysVisible;
    private long lastTime;
    private float previousSelectedProgress;
    protected ProfileGalleryView profileGalleryView;
    private float progressToCounter;
    private final Paint selectedBarPaint;
    private int selectedPosition;
    TextPaint textPaint;
    String title;
    private final GradientDrawable topOverlayGradient;
    private final RectF indicatorRect = new RectF();
    private final int statusBarHeight = 0;
    private int overlayCountVisible = 1;
    private final Rect topOverlayRect = new Rect();
    private final Rect bottomOverlayRect = new Rect();
    private final RectF rect = new RectF();
    private final float[] animatorValues = {0.0f, 1.0f};
    Path path = new Path();
    RectF rectF = new RectF();
    private final GradientDrawable[] pressedOverlayGradient = new GradientDrawable[2];
    private final boolean[] pressedOverlayVisible = new boolean[2];
    private final float[] pressedOverlayAlpha = new float[2];
    private float alpha = 0.0f;
    private float[] alphas = null;
    private int previousSelectedPotision = -1;
    private int currentLoadingAnimationDirection = 1;
    int lastCurrentItem = -1;

    public AvatarPreviewPagerIndicator(Context context) {
        super(context);
        Paint paint = new Paint(1);
        this.barPaint = paint;
        paint.setColor(1442840575);
        Paint paint2 = new Paint(1);
        this.selectedBarPaint = paint2;
        paint2.setColor(-1);
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{1107296256, 0});
        this.topOverlayGradient = gradientDrawable;
        gradientDrawable.setShape(0);
        GradientDrawable gradientDrawable2 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{1107296256, 0});
        this.bottomOverlayGradient = gradientDrawable2;
        gradientDrawable2.setShape(0);
        int i = 0;
        while (i < 2) {
            GradientDrawable.Orientation orientation = i == 0 ? GradientDrawable.Orientation.LEFT_RIGHT : GradientDrawable.Orientation.RIGHT_LEFT;
            this.pressedOverlayGradient[i] = new GradientDrawable(orientation, new int[]{838860800, 0});
            this.pressedOverlayGradient[i].setShape(0);
            i++;
        }
        Paint paint3 = new Paint(1);
        this.backgroundPaint = paint3;
        paint3.setColor(-16777216);
        paint3.setAlpha(66);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.animator = ofFloat;
        ofFloat.setDuration(250L);
        ofFloat.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.AvatarPreviewPagerIndicator$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                AvatarPreviewPagerIndicator.this.m1564lambda$new$0$orgtelegramuiAvatarPreviewPagerIndicator(valueAnimator);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.AvatarPreviewPagerIndicator.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                if (!AvatarPreviewPagerIndicator.this.isOverlaysVisible) {
                    AvatarPreviewPagerIndicator.this.setVisibility(8);
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
                AvatarPreviewPagerIndicator.this.setVisibility(0);
            }
        });
        TextPaint textPaint = new TextPaint(1);
        this.textPaint = textPaint;
        textPaint.setColor(-1);
        this.textPaint.setTypeface(Typeface.SANS_SERIF);
        this.textPaint.setTextAlign(Paint.Align.CENTER);
        this.textPaint.setTextSize(AndroidUtilities.dpf2(15.0f));
    }

    /* renamed from: lambda$new$0$org-telegram-ui-AvatarPreviewPagerIndicator */
    public /* synthetic */ void m1564lambda$new$0$orgtelegramuiAvatarPreviewPagerIndicator(ValueAnimator anim) {
        float[] fArr = this.animatorValues;
        float animatedFraction = anim.getAnimatedFraction();
        this.currentAnimationValue = animatedFraction;
        float value = AndroidUtilities.lerp(fArr, animatedFraction);
        setAlphaValue(value, true);
    }

    public void saveCurrentPageProgress() {
        this.previousSelectedProgress = this.currentProgress;
        this.previousSelectedPotision = this.selectedPosition;
        this.currentLoadingAnimationProgress = 0.0f;
        this.currentLoadingAnimationDirection = 1;
    }

    public void setAlphaValue(float value, boolean self) {
        if (Build.VERSION.SDK_INT > 18) {
            int alpha = (int) (255.0f * value);
            this.topOverlayGradient.setAlpha(alpha);
            this.bottomOverlayGradient.setAlpha(alpha);
            this.backgroundPaint.setAlpha((int) (66.0f * value));
            this.barPaint.setAlpha((int) (85.0f * value));
            this.selectedBarPaint.setAlpha(alpha);
            this.alpha = value;
        } else {
            setAlpha(value);
        }
        if (!self) {
            this.currentAnimationValue = value;
        }
        invalidate();
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.path.reset();
        this.rectF.set(0.0f, 0.0f, getMeasuredHeight(), getMeasuredWidth());
        this.path.addRoundRect(this.rectF, new float[]{AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), 0.0f, 0.0f, 0.0f, 0.0f}, Path.Direction.CCW);
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int actionBarHeight = ActionBar.getCurrentActionBarHeight() + 0;
        this.topOverlayRect.set(0, 0, w, (int) (actionBarHeight * 0.5f));
        this.bottomOverlayRect.set(0, (int) (h - (AndroidUtilities.dp(72.0f) * 0.5f)), w, h);
        this.topOverlayGradient.setBounds(0, this.topOverlayRect.bottom, w, AndroidUtilities.dp(16.0f) + actionBarHeight);
        this.bottomOverlayGradient.setBounds(0, (h - AndroidUtilities.dp(72.0f)) - AndroidUtilities.dp(24.0f), w, this.bottomOverlayRect.top);
        this.pressedOverlayGradient[0].setBounds(0, 0, w / 5, h);
        this.pressedOverlayGradient[1].setBounds(w - (w / 5), 0, w, h);
    }

    /* JADX WARN: Removed duplicated region for block: B:118:0x032b  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x032e  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        float f;
        long newTime;
        boolean invalidate;
        int baseAlpha;
        float progress;
        for (int i = 0; i < 2; i++) {
            float[] fArr = this.pressedOverlayAlpha;
            if (fArr[i] > 0.0f) {
                this.pressedOverlayGradient[i].setAlpha((int) (fArr[i] * 255.0f));
                this.pressedOverlayGradient[i].draw(canvas);
            }
        }
        this.topOverlayGradient.draw(canvas);
        canvas.drawRect(this.topOverlayRect, this.backgroundPaint);
        int count = this.profileGalleryView.getRealCount();
        this.selectedPosition = this.profileGalleryView.getRealPosition();
        float[] fArr2 = this.alphas;
        if (fArr2 == null || fArr2.length != count) {
            float[] fArr3 = new float[count];
            this.alphas = fArr3;
            Arrays.fill(fArr3, 0.0f);
        }
        boolean invalidate2 = false;
        long newTime2 = SystemClock.elapsedRealtime();
        long dt = newTime2 - this.lastTime;
        if (dt < 0 || dt > 20) {
            dt = 17;
        }
        this.lastTime = newTime2;
        float f2 = 1.0f;
        if (count > 1 && count <= 20) {
            int i2 = this.overlayCountVisible;
            if (i2 == 0) {
                this.alpha = 0.0f;
                this.overlayCountVisible = 3;
            } else if (i2 == 1) {
                this.alpha = 0.0f;
                this.overlayCountVisible = 2;
            }
            if (this.overlayCountVisible == 2) {
                this.barPaint.setAlpha((int) (this.alpha * 85.0f));
                this.selectedBarPaint.setAlpha((int) (this.alpha * 255.0f));
            }
            int width = ((getMeasuredWidth() - AndroidUtilities.dp(10.0f)) - AndroidUtilities.dp((count - 1) * 2)) / count;
            int y = AndroidUtilities.dp(8.0f);
            int a = 0;
            while (a < count) {
                int x = AndroidUtilities.dp((a * 2) + 5) + (width * a);
                if (a != this.previousSelectedPotision || Math.abs(this.previousSelectedProgress - f2) <= 1.0E-4f) {
                    boolean invalidate3 = invalidate2;
                    if (a == this.selectedPosition) {
                        if (this.profileGalleryView.isCurrentItemVideo()) {
                            progress = this.profileGalleryView.getCurrentItemProgress();
                            this.currentProgress = progress;
                            if ((progress > 0.0f || !this.profileGalleryView.isLoadingCurrentVideo()) && this.currentLoadingAnimationProgress <= 0.0f) {
                                newTime = newTime2;
                            } else {
                                float f3 = this.currentLoadingAnimationProgress;
                                int i3 = this.currentLoadingAnimationDirection;
                                newTime = newTime2;
                                float f4 = f3 + (((float) (i3 * dt)) / 500.0f);
                                this.currentLoadingAnimationProgress = f4;
                                if (f4 > 1.0f) {
                                    this.currentLoadingAnimationProgress = 1.0f;
                                    this.currentLoadingAnimationDirection = i3 * (-1);
                                } else if (f4 <= 0.0f) {
                                    this.currentLoadingAnimationProgress = 0.0f;
                                    this.currentLoadingAnimationDirection = i3 * (-1);
                                }
                            }
                            this.rect.set(x, y, x + width, AndroidUtilities.dp(2.0f) + y);
                            this.barPaint.setAlpha((int) (((this.currentLoadingAnimationProgress * 48.0f) + 85.0f) * this.alpha));
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), this.barPaint);
                            invalidate = true;
                            baseAlpha = 80;
                        } else {
                            newTime = newTime2;
                            this.currentProgress = 1.0f;
                            progress = 1.0f;
                            baseAlpha = 85;
                            invalidate = invalidate3;
                        }
                    } else {
                        newTime = newTime2;
                        progress = 1.0f;
                        baseAlpha = 85;
                        invalidate = invalidate3;
                    }
                } else {
                    float progress2 = this.previousSelectedProgress;
                    canvas.save();
                    canvas.clipRect(x + (width * progress2), y, x + width, y + AndroidUtilities.dp(2.0f));
                    this.rect.set(x, y, x + width, y + AndroidUtilities.dp(2.0f));
                    this.barPaint.setAlpha((int) (this.alpha * 85.0f));
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), this.barPaint);
                    canvas.restore();
                    invalidate = true;
                    newTime = newTime2;
                    baseAlpha = 80;
                    progress = progress2;
                }
                boolean invalidate4 = invalidate;
                this.rect.set(x, y, x + (width * progress), AndroidUtilities.dp(2.0f) + y);
                if (a != this.selectedPosition) {
                    if (this.overlayCountVisible == 3) {
                        this.barPaint.setAlpha((int) (AndroidUtilities.lerp(baseAlpha, 255, CubicBezierInterpolator.EASE_BOTH.getInterpolation(this.alphas[a])) * this.alpha));
                    }
                } else {
                    this.alphas[a] = 0.75f;
                }
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), a == this.selectedPosition ? this.selectedBarPaint : this.barPaint);
                a++;
                invalidate2 = invalidate4;
                newTime2 = newTime;
                f2 = 1.0f;
            }
            boolean invalidate5 = invalidate2;
            int i4 = this.overlayCountVisible;
            if (i4 == 2) {
                float f5 = this.alpha;
                if (f5 >= 1.0f) {
                    this.overlayCountVisible = 3;
                    invalidate2 = invalidate5;
                } else {
                    float f6 = f5 + (((float) dt) / 180.0f);
                    this.alpha = f6;
                    if (f6 > 1.0f) {
                        this.alpha = 1.0f;
                    }
                    invalidate2 = true;
                }
            } else {
                if (i4 == 3) {
                    int i5 = 0;
                    invalidate2 = invalidate5;
                    while (true) {
                        float[] fArr4 = this.alphas;
                        if (i5 >= fArr4.length) {
                            break;
                        }
                        if (i5 != this.selectedPosition && fArr4[i5] > 0.0f) {
                            fArr4[i5] = fArr4[i5] - (((float) dt) / 500.0f);
                            if (fArr4[i5] <= 0.0f) {
                                fArr4[i5] = 0.0f;
                                if (i5 == this.previousSelectedPotision) {
                                    this.previousSelectedPotision = -1;
                                }
                            }
                            invalidate2 = true;
                        } else if (i5 == this.previousSelectedPotision) {
                            this.previousSelectedPotision = -1;
                        }
                        i5++;
                    }
                }
                invalidate2 = invalidate5;
            }
        }
        if (count > 20 || this.progressToCounter != 0.0f) {
            float textWidth = this.textPaint.measureText(getCurrentTitle());
            this.indicatorRect.right = getMeasuredWidth() - AndroidUtilities.dp(8.0f);
            RectF rectF = this.indicatorRect;
            rectF.left = rectF.right - (AndroidUtilities.dpf2(16.0f) + textWidth);
            this.indicatorRect.top = AndroidUtilities.dp(8.0f);
            RectF rectF2 = this.indicatorRect;
            rectF2.bottom = rectF2.top + AndroidUtilities.dp(26.0f);
            float radius = AndroidUtilities.dpf2(12.0f);
            canvas.save();
            boolean showCounter = count > 20;
            if (showCounter) {
                float f7 = this.progressToCounter;
                if (f7 != 1.0f) {
                    this.progressToCounter = f7 + (((float) dt) / 150.0f);
                    f = this.progressToCounter;
                    if (f < 1.0f) {
                        this.progressToCounter = 1.0f;
                    } else if (f <= 0.0f) {
                        this.progressToCounter = 0.0f;
                    } else {
                        invalidate();
                    }
                    float f8 = this.progressToCounter;
                    canvas.scale(f8, f8, this.indicatorRect.centerX(), this.indicatorRect.centerY());
                    canvas.drawRoundRect(this.indicatorRect, radius, radius, this.backgroundPaint);
                    canvas.drawText(getCurrentTitle(), this.indicatorRect.centerX(), this.indicatorRect.top + AndroidUtilities.dpf2(18.5f), this.textPaint);
                    canvas.restore();
                }
            }
            if (!showCounter) {
                float f9 = this.progressToCounter;
                if (f9 != 0.0f) {
                    this.progressToCounter = f9 - (((float) dt) / 150.0f);
                }
            }
            f = this.progressToCounter;
            if (f < 1.0f) {
            }
            float f82 = this.progressToCounter;
            canvas.scale(f82, f82, this.indicatorRect.centerX(), this.indicatorRect.centerY());
            canvas.drawRoundRect(this.indicatorRect, radius, radius, this.backgroundPaint);
            canvas.drawText(getCurrentTitle(), this.indicatorRect.centerX(), this.indicatorRect.top + AndroidUtilities.dpf2(18.5f), this.textPaint);
            canvas.restore();
        }
        for (int i6 = 0; i6 < 2; i6++) {
            if (!this.pressedOverlayVisible[i6]) {
                float[] fArr5 = this.pressedOverlayAlpha;
                if (fArr5[i6] > 0.0f) {
                    fArr5[i6] = fArr5[i6] - (((float) dt) / 180.0f);
                    if (fArr5[i6] < 0.0f) {
                        fArr5[i6] = 0.0f;
                    }
                    invalidate2 = true;
                }
            } else {
                float[] fArr6 = this.pressedOverlayAlpha;
                if (fArr6[i6] < 1.0f) {
                    fArr6[i6] = fArr6[i6] + (((float) dt) / 180.0f);
                    if (fArr6[i6] > 1.0f) {
                        fArr6[i6] = 1.0f;
                    }
                    invalidate2 = true;
                }
            }
        }
        if (invalidate2) {
            postInvalidateOnAnimation();
        }
    }

    private String getCurrentTitle() {
        if (this.lastCurrentItem != this.profileGalleryView.getCurrentItem()) {
            this.title = this.profileGalleryView.getAdapter().getPageTitle(this.profileGalleryView.getCurrentItem()).toString();
            this.lastCurrentItem = this.profileGalleryView.getCurrentItem();
        }
        return this.title;
    }

    @Override // org.telegram.ui.Components.ProfileGalleryView.Callback
    public void onDown(boolean left) {
        this.pressedOverlayVisible[!left ? 1 : 0] = true;
        postInvalidateOnAnimation();
    }

    @Override // org.telegram.ui.Components.ProfileGalleryView.Callback
    public void onRelease() {
        Arrays.fill(this.pressedOverlayVisible, false);
        postInvalidateOnAnimation();
    }

    @Override // org.telegram.ui.Components.ProfileGalleryView.Callback
    public void onPhotosLoaded() {
    }

    @Override // org.telegram.ui.Components.ProfileGalleryView.Callback
    public void onVideoSet() {
        invalidate();
    }

    public void setProfileGalleryView(ProfileGalleryView profileGalleryView) {
        this.profileGalleryView = profileGalleryView;
    }

    public ProfileGalleryView getProfileGalleryView() {
        return this.profileGalleryView;
    }
}
