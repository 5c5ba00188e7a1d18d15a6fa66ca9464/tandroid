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
import android.os.SystemClock;
import android.text.TextPaint;
import android.view.View;
import java.util.Arrays;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.ProfileGalleryView;

/* loaded from: classes4.dex */
public class AvatarPreviewPagerIndicator extends View implements ProfileGalleryView.Callback {
    private float alpha;
    private float[] alphas;
    private final ValueAnimator animator;
    private final float[] animatorValues;
    private final Paint backgroundPaint;
    private final Paint barPaint;
    private final GradientDrawable bottomOverlayGradient;
    private final Rect bottomOverlayRect;
    private float currentAnimationValue;
    private int currentLoadingAnimationDirection;
    private float currentLoadingAnimationProgress;
    private float currentProgress;
    private final RectF indicatorRect;
    private boolean isOverlaysVisible;
    int lastCurrentItem;
    private long lastTime;
    private int overlayCountVisible;
    Path path;
    private final float[] pressedOverlayAlpha;
    private final GradientDrawable[] pressedOverlayGradient;
    private final boolean[] pressedOverlayVisible;
    private int previousSelectedPotision;
    private float previousSelectedProgress;
    protected ProfileGalleryView profileGalleryView;
    private float progressToCounter;
    private final RectF rect;
    RectF rectF;
    private final Paint selectedBarPaint;
    private int selectedPosition;
    private final int statusBarHeight;
    TextPaint textPaint;
    String title;
    private final GradientDrawable topOverlayGradient;
    private final Rect topOverlayRect;

    public AvatarPreviewPagerIndicator(Context context) {
        super(context);
        this.indicatorRect = new RectF();
        this.statusBarHeight = 0;
        this.overlayCountVisible = 1;
        this.topOverlayRect = new Rect();
        this.bottomOverlayRect = new Rect();
        this.rect = new RectF();
        this.animatorValues = new float[]{0.0f, 1.0f};
        this.path = new Path();
        this.rectF = new RectF();
        this.pressedOverlayGradient = new GradientDrawable[2];
        this.pressedOverlayVisible = new boolean[2];
        this.pressedOverlayAlpha = new float[2];
        this.alpha = 0.0f;
        this.alphas = null;
        this.previousSelectedPotision = -1;
        this.currentLoadingAnimationDirection = 1;
        this.lastCurrentItem = -1;
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
                if (AvatarPreviewPagerIndicator.this.isOverlaysVisible) {
                    return;
                }
                AvatarPreviewPagerIndicator.this.setVisibility(8);
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

    private String getCurrentTitle() {
        if (this.lastCurrentItem != this.profileGalleryView.getCurrentItem()) {
            this.title = this.profileGalleryView.getAdapter().getPageTitle(this.profileGalleryView.getCurrentItem()).toString();
            this.lastCurrentItem = this.profileGalleryView.getCurrentItem();
        }
        return this.title;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        float[] fArr = this.animatorValues;
        float animatedFraction = valueAnimator.getAnimatedFraction();
        this.currentAnimationValue = animatedFraction;
        setAlphaValue(AndroidUtilities.lerp(fArr, animatedFraction), true);
    }

    public ProfileGalleryView getProfileGalleryView() {
        return this.profileGalleryView;
    }

    @Override // org.telegram.ui.Components.ProfileGalleryView.Callback
    public void onDown(boolean z) {
        this.pressedOverlayVisible[!z ? 1 : 0] = true;
        postInvalidateOnAnimation();
    }

    /* JADX WARN: Removed duplicated region for block: B:123:0x02fc  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x01cb  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0204  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0207  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x01ec  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        int i;
        boolean z;
        float f;
        int i2;
        int i3;
        float f2;
        for (int i4 = 0; i4 < 2; i4++) {
            float f3 = this.pressedOverlayAlpha[i4];
            if (f3 > 0.0f) {
                this.pressedOverlayGradient[i4].setAlpha((int) (f3 * 255.0f));
                this.pressedOverlayGradient[i4].draw(canvas);
            }
        }
        this.topOverlayGradient.draw(canvas);
        canvas.drawRect(this.topOverlayRect, this.backgroundPaint);
        int realCount = this.profileGalleryView.getRealCount();
        this.selectedPosition = this.profileGalleryView.getRealPosition();
        float[] fArr = this.alphas;
        if (fArr == null || fArr.length != realCount) {
            float[] fArr2 = new float[realCount];
            this.alphas = fArr2;
            Arrays.fill(fArr2, 0.0f);
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = elapsedRealtime - this.lastTime;
        if (j < 0 || j > 20) {
            j = 17;
        }
        this.lastTime = elapsedRealtime;
        float f4 = 1.0f;
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
                if (i6 != this.previousSelectedPotision || Math.abs(this.previousSelectedProgress - f4) <= 1.0E-4f) {
                    i2 = 85;
                    if (i6 != this.selectedPosition) {
                        i3 = i6;
                    } else if (this.profileGalleryView.isCurrentItemVideo()) {
                        f2 = this.profileGalleryView.getCurrentItemProgress();
                        this.currentProgress = f2;
                        if ((f2 > 0.0f || !this.profileGalleryView.isLoadingCurrentVideo()) && this.currentLoadingAnimationProgress <= 0.0f) {
                            i3 = i6;
                        } else {
                            float f5 = this.currentLoadingAnimationProgress;
                            int i7 = this.currentLoadingAnimationDirection;
                            i3 = i6;
                            float f6 = f5 + ((i7 * j) / 500.0f);
                            this.currentLoadingAnimationProgress = f6;
                            if (f6 > 1.0f) {
                                this.currentLoadingAnimationProgress = 1.0f;
                            } else if (f6 <= 0.0f) {
                                this.currentLoadingAnimationProgress = 0.0f;
                            }
                            this.currentLoadingAnimationDirection = i7 * (-1);
                        }
                        this.rect.set(dp2, dp, dp2 + measuredWidth, AndroidUtilities.dp(2.0f) + dp);
                        this.barPaint.setAlpha((int) (((this.currentLoadingAnimationProgress * 48.0f) + 85.0f) * this.alpha));
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), this.barPaint);
                    } else {
                        i3 = i6;
                        this.currentProgress = 1.0f;
                    }
                    f2 = 1.0f;
                    float f7 = dp2;
                    this.rect.set(f7, dp, (measuredWidth * f2) + f7, AndroidUtilities.dp(2.0f) + dp);
                    if (i3 != this.selectedPosition) {
                        this.alphas[i3] = 0.75f;
                    } else if (this.overlayCountVisible == 3) {
                        this.barPaint.setAlpha((int) (AndroidUtilities.lerp(i2, NotificationCenter.liveLocationsChanged, CubicBezierInterpolator.EASE_BOTH.getInterpolation(this.alphas[i3])) * this.alpha));
                    }
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), i3 != this.selectedPosition ? this.selectedBarPaint : this.barPaint);
                    i6 = i3 + 1;
                    f4 = 1.0f;
                } else {
                    f2 = this.previousSelectedProgress;
                    canvas.save();
                    float f8 = dp2;
                    float f9 = dp;
                    float f10 = dp2 + measuredWidth;
                    canvas.clipRect((measuredWidth * f2) + f8, f9, f10, dp + AndroidUtilities.dp(2.0f));
                    this.rect.set(f8, f9, f10, AndroidUtilities.dp(2.0f) + dp);
                    this.barPaint.setAlpha((int) (this.alpha * 85.0f));
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), this.barPaint);
                    canvas.restore();
                    i3 = i6;
                }
                i2 = 80;
                z = true;
                float f72 = dp2;
                this.rect.set(f72, dp, (measuredWidth * f2) + f72, AndroidUtilities.dp(2.0f) + dp);
                if (i3 != this.selectedPosition) {
                }
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), i3 != this.selectedPosition ? this.selectedBarPaint : this.barPaint);
                i6 = i3 + 1;
                f4 = 1.0f;
            }
            int i8 = this.overlayCountVisible;
            if (i8 == 2) {
                float f11 = this.alpha;
                if (f11 < 1.0f) {
                    float f12 = f11 + (j / 180.0f);
                    this.alpha = f12;
                    if (f12 > 1.0f) {
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
                    float[] fArr3 = this.alphas;
                    if (i9 >= fArr3.length) {
                        break;
                    }
                    if (i9 != this.selectedPosition) {
                        float f13 = fArr3[i9];
                        if (f13 > 0.0f) {
                            float f14 = f13 - (j / 500.0f);
                            fArr3[i9] = f14;
                            if (f14 <= 0.0f) {
                                fArr3[i9] = 0.0f;
                                if (i9 == this.previousSelectedPotision) {
                                    this.previousSelectedPotision = -1;
                                }
                            }
                            z = true;
                            i9++;
                        }
                    }
                    if (i9 == this.previousSelectedPotision) {
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
                float f15 = this.progressToCounter;
                if (f15 != 1.0f) {
                    this.progressToCounter = f15 + (j / 150.0f);
                    f = this.progressToCounter;
                    float f16 = 1.0f;
                    if (f < 1.0f) {
                        f16 = 0.0f;
                        if (f > 0.0f) {
                            invalidate();
                            float f17 = this.progressToCounter;
                            canvas.scale(f17, f17, this.indicatorRect.centerX(), this.indicatorRect.centerY());
                            canvas.drawRoundRect(this.indicatorRect, dpf2, dpf2, this.backgroundPaint);
                            canvas.drawText(getCurrentTitle(), this.indicatorRect.centerX(), this.indicatorRect.top + AndroidUtilities.dpf2(18.5f), this.textPaint);
                            canvas.restore();
                        }
                    }
                    this.progressToCounter = f16;
                    float f172 = this.progressToCounter;
                    canvas.scale(f172, f172, this.indicatorRect.centerX(), this.indicatorRect.centerY());
                    canvas.drawRoundRect(this.indicatorRect, dpf2, dpf2, this.backgroundPaint);
                    canvas.drawText(getCurrentTitle(), this.indicatorRect.centerX(), this.indicatorRect.top + AndroidUtilities.dpf2(18.5f), this.textPaint);
                    canvas.restore();
                }
            }
            if (!z2) {
                float f18 = this.progressToCounter;
                if (f18 != 0.0f) {
                    this.progressToCounter = f18 - (j / 150.0f);
                }
            }
            f = this.progressToCounter;
            float f162 = 1.0f;
            if (f < 1.0f) {
            }
            this.progressToCounter = f162;
            float f1722 = this.progressToCounter;
            canvas.scale(f1722, f1722, this.indicatorRect.centerX(), this.indicatorRect.centerY());
            canvas.drawRoundRect(this.indicatorRect, dpf2, dpf2, this.backgroundPaint);
            canvas.drawText(getCurrentTitle(), this.indicatorRect.centerX(), this.indicatorRect.top + AndroidUtilities.dpf2(18.5f), this.textPaint);
            canvas.restore();
        }
        for (int i10 = 0; i10 < 2; i10++) {
            if (this.pressedOverlayVisible[i10]) {
                float[] fArr4 = this.pressedOverlayAlpha;
                float f19 = fArr4[i10];
                if (f19 < 1.0f) {
                    float f20 = f19 + (j / 180.0f);
                    fArr4[i10] = f20;
                    if (f20 > 1.0f) {
                        fArr4[i10] = 1.0f;
                    }
                    z = true;
                }
            } else {
                float[] fArr5 = this.pressedOverlayAlpha;
                float f21 = fArr5[i10];
                if (f21 > 0.0f) {
                    float f22 = f21 - (j / 180.0f);
                    fArr5[i10] = f22;
                    if (f22 < 0.0f) {
                        fArr5[i10] = 0.0f;
                    }
                    z = true;
                }
            }
        }
        if (z) {
            postInvalidateOnAnimation();
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.path.reset();
        this.rectF.set(0.0f, 0.0f, getMeasuredHeight(), getMeasuredWidth());
        this.path.addRoundRect(this.rectF, new float[]{AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), 0.0f, 0.0f, 0.0f, 0.0f}, Path.Direction.CCW);
    }

    @Override // org.telegram.ui.Components.ProfileGalleryView.Callback
    public void onPhotosLoaded() {
    }

    @Override // org.telegram.ui.Components.ProfileGalleryView.Callback
    public void onRelease() {
        Arrays.fill(this.pressedOverlayVisible, false);
        postInvalidateOnAnimation();
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        int currentActionBarHeight = ActionBar.getCurrentActionBarHeight();
        this.topOverlayRect.set(0, 0, i, (int) (currentActionBarHeight * 0.5f));
        this.bottomOverlayRect.set(0, (int) (i2 - (AndroidUtilities.dp(72.0f) * 0.5f)), i, i2);
        this.topOverlayGradient.setBounds(0, this.topOverlayRect.bottom, i, currentActionBarHeight + AndroidUtilities.dp(16.0f));
        this.bottomOverlayGradient.setBounds(0, (i2 - AndroidUtilities.dp(72.0f)) - AndroidUtilities.dp(24.0f), i, this.bottomOverlayRect.top);
        int i5 = i / 5;
        this.pressedOverlayGradient[0].setBounds(0, 0, i5, i2);
        this.pressedOverlayGradient[1].setBounds(i - i5, 0, i, i2);
    }

    @Override // org.telegram.ui.Components.ProfileGalleryView.Callback
    public void onVideoSet() {
        invalidate();
    }

    public void saveCurrentPageProgress() {
        this.previousSelectedProgress = this.currentProgress;
        this.previousSelectedPotision = this.selectedPosition;
        this.currentLoadingAnimationProgress = 0.0f;
        this.currentLoadingAnimationDirection = 1;
    }

    public void setAlphaValue(float f, boolean z) {
        int i = (int) (255.0f * f);
        this.topOverlayGradient.setAlpha(i);
        this.bottomOverlayGradient.setAlpha(i);
        this.backgroundPaint.setAlpha((int) (66.0f * f));
        this.barPaint.setAlpha((int) (85.0f * f));
        this.selectedBarPaint.setAlpha(i);
        this.alpha = f;
        if (!z) {
            this.currentAnimationValue = f;
        }
        invalidate();
    }

    public void setProfileGalleryView(ProfileGalleryView profileGalleryView) {
        this.profileGalleryView = profileGalleryView;
    }
}
