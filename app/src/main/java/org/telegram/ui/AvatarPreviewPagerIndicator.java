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
/* loaded from: classes3.dex */
public class AvatarPreviewPagerIndicator extends View implements ProfileGalleryView.Callback {
    private final ValueAnimator animator;
    private final Paint backgroundPaint;
    private final Paint barPaint;
    private final GradientDrawable bottomOverlayGradient;
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

    @Override // org.telegram.ui.Components.ProfileGalleryView.Callback
    public void onPhotosLoaded() {
    }

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
            this.pressedOverlayGradient[i] = new GradientDrawable(i == 0 ? GradientDrawable.Orientation.LEFT_RIGHT : GradientDrawable.Orientation.RIGHT_LEFT, new int[]{838860800, 0});
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
                AvatarPreviewPagerIndicator.this.lambda$new$0(valueAnimator);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.AvatarPreviewPagerIndicator.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (!AvatarPreviewPagerIndicator.this.isOverlaysVisible) {
                    AvatarPreviewPagerIndicator.this.setVisibility(8);
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
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

    public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        setAlphaValue(AndroidUtilities.lerp(this.animatorValues, valueAnimator.getAnimatedFraction()), true);
    }

    public void saveCurrentPageProgress() {
        this.previousSelectedProgress = this.currentProgress;
        this.previousSelectedPotision = this.selectedPosition;
        this.currentLoadingAnimationProgress = 0.0f;
        this.currentLoadingAnimationDirection = 1;
    }

    public void setAlphaValue(float f, boolean z) {
        if (Build.VERSION.SDK_INT > 18) {
            int i = (int) (255.0f * f);
            this.topOverlayGradient.setAlpha(i);
            this.bottomOverlayGradient.setAlpha(i);
            this.backgroundPaint.setAlpha((int) (66.0f * f));
            this.barPaint.setAlpha((int) (85.0f * f));
            this.selectedBarPaint.setAlpha(i);
            this.alpha = f;
        } else {
            setAlpha(f);
        }
        invalidate();
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.path.reset();
        this.rectF.set(0.0f, 0.0f, getMeasuredHeight(), getMeasuredWidth());
        this.path.addRoundRect(this.rectF, new float[]{AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), 0.0f, 0.0f, 0.0f, 0.0f}, Path.Direction.CCW);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        int currentActionBarHeight = ActionBar.getCurrentActionBarHeight() + 0;
        this.topOverlayRect.set(0, 0, i, (int) (currentActionBarHeight * 0.5f));
        this.bottomOverlayRect.set(0, (int) (i2 - (AndroidUtilities.dp(72.0f) * 0.5f)), i, i2);
        this.topOverlayGradient.setBounds(0, this.topOverlayRect.bottom, i, currentActionBarHeight + AndroidUtilities.dp(16.0f));
        this.bottomOverlayGradient.setBounds(0, (i2 - AndroidUtilities.dp(72.0f)) - AndroidUtilities.dp(24.0f), i, this.bottomOverlayRect.top);
        int i5 = i / 5;
        this.pressedOverlayGradient[0].setBounds(0, 0, i5, i2);
        this.pressedOverlayGradient[1].setBounds(i - i5, 0, i, i2);
    }

    /* JADX WARN: Removed duplicated region for block: B:119:0x0304  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0307  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x01d2  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x01f3  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x020b  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x020e  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        boolean z;
        int i;
        float f;
        int i2;
        float f2;
        int i3;
        for (int i4 = 0; i4 < 2; i4++) {
            float[] fArr = this.pressedOverlayAlpha;
            if (fArr[i4] > 0.0f) {
                this.pressedOverlayGradient[i4].setAlpha((int) (fArr[i4] * 255.0f));
                this.pressedOverlayGradient[i4].draw(canvas);
            }
        }
        this.topOverlayGradient.draw(canvas);
        canvas.drawRect(this.topOverlayRect, this.backgroundPaint);
        int realCount = this.profileGalleryView.getRealCount();
        this.selectedPosition = this.profileGalleryView.getRealPosition();
        float[] fArr2 = this.alphas;
        if (fArr2 == null || fArr2.length != realCount) {
            float[] fArr3 = new float[realCount];
            this.alphas = fArr3;
            Arrays.fill(fArr3, 0.0f);
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = elapsedRealtime - this.lastTime;
        if (j < 0 || j > 20) {
            j = 17;
        }
        this.lastTime = elapsedRealtime;
        float f3 = 1.0f;
        if (realCount <= 1 || realCount > 20) {
            i = 20;
            z = false;
        } else {
            int i5 = this.overlayCountVisible;
            if (i5 == 0) {
                this.alpha = 0.0f;
                this.overlayCountVisible = 3;
            } else if (i5 == 1) {
                this.alpha = 0.0f;
                this.overlayCountVisible = 2;
            }
            if (this.overlayCountVisible == 2) {
                this.barPaint.setAlpha((int) (this.alpha * 85.0f));
                this.selectedBarPaint.setAlpha((int) (this.alpha * 255.0f));
            }
            int measuredWidth = ((getMeasuredWidth() - AndroidUtilities.dp(10.0f)) - AndroidUtilities.dp((realCount - 1) * 2)) / realCount;
            int dp = AndroidUtilities.dp(8.0f);
            int i6 = 0;
            z = false;
            while (i6 < realCount) {
                int dp2 = AndroidUtilities.dp((i6 * 2) + 5) + (measuredWidth * i6);
                if (i6 == this.previousSelectedPotision && Math.abs(this.previousSelectedProgress - f3) > 1.0E-4f) {
                    f2 = this.previousSelectedProgress;
                    canvas.save();
                    float f4 = dp2;
                    float f5 = dp;
                    float f6 = dp2 + measuredWidth;
                    canvas.clipRect((measuredWidth * f2) + f4, f5, f6, dp + AndroidUtilities.dp(2.0f));
                    this.rect.set(f4, f5, f6, AndroidUtilities.dp(2.0f) + dp);
                    this.barPaint.setAlpha((int) (this.alpha * 85.0f));
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), this.barPaint);
                    canvas.restore();
                    i2 = i6;
                } else {
                    if (i6 != this.selectedPosition) {
                        i2 = i6;
                    } else if (this.profileGalleryView.isCurrentItemVideo()) {
                        f2 = this.profileGalleryView.getCurrentItemProgress();
                        this.currentProgress = f2;
                        if ((f2 > 0.0f || !this.profileGalleryView.isLoadingCurrentVideo()) && this.currentLoadingAnimationProgress <= 0.0f) {
                            i2 = i6;
                        } else {
                            float f7 = this.currentLoadingAnimationProgress;
                            int i7 = this.currentLoadingAnimationDirection;
                            i2 = i6;
                            float f8 = f7 + (((float) (i7 * j)) / 500.0f);
                            this.currentLoadingAnimationProgress = f8;
                            if (f8 > 1.0f) {
                                this.currentLoadingAnimationProgress = 1.0f;
                                this.currentLoadingAnimationDirection = i7 * (-1);
                            } else if (f8 <= 0.0f) {
                                this.currentLoadingAnimationProgress = 0.0f;
                                this.currentLoadingAnimationDirection = i7 * (-1);
                            }
                        }
                        this.rect.set(dp2, dp, dp2 + measuredWidth, AndroidUtilities.dp(2.0f) + dp);
                        this.barPaint.setAlpha((int) (((this.currentLoadingAnimationProgress * 48.0f) + 85.0f) * this.alpha));
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), this.barPaint);
                    } else {
                        i2 = i6;
                        this.currentProgress = 1.0f;
                    }
                    i3 = 85;
                    f2 = 1.0f;
                    float f9 = dp2;
                    this.rect.set(f9, dp, (measuredWidth * f2) + f9, AndroidUtilities.dp(2.0f) + dp);
                    if (i2 == this.selectedPosition) {
                        if (this.overlayCountVisible == 3) {
                            this.barPaint.setAlpha((int) (AndroidUtilities.lerp(i3, 255, CubicBezierInterpolator.EASE_BOTH.getInterpolation(this.alphas[i2])) * this.alpha));
                        }
                    } else {
                        this.alphas[i2] = 0.75f;
                    }
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), i2 != this.selectedPosition ? this.selectedBarPaint : this.barPaint);
                    i6 = i2 + 1;
                    f3 = 1.0f;
                }
                i3 = 80;
                z = true;
                float f92 = dp2;
                this.rect.set(f92, dp, (measuredWidth * f2) + f92, AndroidUtilities.dp(2.0f) + dp);
                if (i2 == this.selectedPosition) {
                }
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), i2 != this.selectedPosition ? this.selectedBarPaint : this.barPaint);
                i6 = i2 + 1;
                f3 = 1.0f;
            }
            int i8 = this.overlayCountVisible;
            if (i8 == 2) {
                float f10 = this.alpha;
                if (f10 < 1.0f) {
                    float f11 = f10 + (((float) j) / 180.0f);
                    this.alpha = f11;
                    if (f11 > 1.0f) {
                        this.alpha = 1.0f;
                    }
                    i = 20;
                    z = true;
                } else {
                    this.overlayCountVisible = 3;
                }
            } else if (i8 == 3) {
                int i9 = 0;
                while (true) {
                    float[] fArr4 = this.alphas;
                    if (i9 >= fArr4.length) {
                        break;
                    }
                    if (i9 != this.selectedPosition && fArr4[i9] > 0.0f) {
                        fArr4[i9] = fArr4[i9] - (((float) j) / 500.0f);
                        if (fArr4[i9] <= 0.0f) {
                            fArr4[i9] = 0.0f;
                            if (i9 == this.previousSelectedPotision) {
                                this.previousSelectedPotision = -1;
                            }
                        }
                        z = true;
                    } else if (i9 == this.previousSelectedPotision) {
                        this.previousSelectedPotision = -1;
                    }
                    i9++;
                }
            }
            i = 20;
        }
        if (realCount > i || this.progressToCounter != 0.0f) {
            float measureText = this.textPaint.measureText(getCurrentTitle());
            this.indicatorRect.right = getMeasuredWidth() - AndroidUtilities.dp(8.0f);
            RectF rectF = this.indicatorRect;
            rectF.left = rectF.right - (measureText + AndroidUtilities.dpf2(16.0f));
            this.indicatorRect.top = AndroidUtilities.dp(8.0f);
            RectF rectF2 = this.indicatorRect;
            rectF2.bottom = rectF2.top + AndroidUtilities.dp(26.0f);
            float dpf2 = AndroidUtilities.dpf2(12.0f);
            canvas.save();
            boolean z2 = realCount > 20;
            if (z2) {
                float f12 = this.progressToCounter;
                if (f12 != 1.0f) {
                    this.progressToCounter = f12 + (((float) j) / 150.0f);
                    f = this.progressToCounter;
                    if (f < 1.0f) {
                        this.progressToCounter = 1.0f;
                    } else if (f <= 0.0f) {
                        this.progressToCounter = 0.0f;
                    } else {
                        invalidate();
                    }
                    float f13 = this.progressToCounter;
                    canvas.scale(f13, f13, this.indicatorRect.centerX(), this.indicatorRect.centerY());
                    canvas.drawRoundRect(this.indicatorRect, dpf2, dpf2, this.backgroundPaint);
                    canvas.drawText(getCurrentTitle(), this.indicatorRect.centerX(), this.indicatorRect.top + AndroidUtilities.dpf2(18.5f), this.textPaint);
                    canvas.restore();
                }
            }
            if (!z2) {
                float f14 = this.progressToCounter;
                if (f14 != 0.0f) {
                    this.progressToCounter = f14 - (((float) j) / 150.0f);
                }
            }
            f = this.progressToCounter;
            if (f < 1.0f) {
            }
            float f132 = this.progressToCounter;
            canvas.scale(f132, f132, this.indicatorRect.centerX(), this.indicatorRect.centerY());
            canvas.drawRoundRect(this.indicatorRect, dpf2, dpf2, this.backgroundPaint);
            canvas.drawText(getCurrentTitle(), this.indicatorRect.centerX(), this.indicatorRect.top + AndroidUtilities.dpf2(18.5f), this.textPaint);
            canvas.restore();
        }
        for (int i10 = 0; i10 < 2; i10++) {
            if (this.pressedOverlayVisible[i10]) {
                float[] fArr5 = this.pressedOverlayAlpha;
                if (fArr5[i10] < 1.0f) {
                    fArr5[i10] = fArr5[i10] + (((float) j) / 180.0f);
                    if (fArr5[i10] > 1.0f) {
                        fArr5[i10] = 1.0f;
                    }
                    z = true;
                }
            } else {
                float[] fArr6 = this.pressedOverlayAlpha;
                if (fArr6[i10] > 0.0f) {
                    fArr6[i10] = fArr6[i10] - (((float) j) / 180.0f);
                    if (fArr6[i10] < 0.0f) {
                        fArr6[i10] = 0.0f;
                    }
                    z = true;
                }
            }
        }
        if (z) {
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
    public void onDown(boolean z) {
        this.pressedOverlayVisible[!z ? 1 : 0] = true;
        postInvalidateOnAnimation();
    }

    @Override // org.telegram.ui.Components.ProfileGalleryView.Callback
    public void onRelease() {
        Arrays.fill(this.pressedOverlayVisible, false);
        postInvalidateOnAnimation();
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
