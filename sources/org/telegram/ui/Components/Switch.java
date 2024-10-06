package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.StateSet;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.BaseCell;
/* loaded from: classes3.dex */
public class Switch extends View {
    private boolean attachedToWindow;
    private boolean bitmapsCreated;
    private ObjectAnimator checkAnimator;
    private int colorSet;
    private int drawIconType;
    private boolean drawRipple;
    private ObjectAnimator iconAnimator;
    private Drawable iconDrawable;
    private float iconProgress;
    private boolean isChecked;
    private int lastIconColor;
    private Bitmap[] overlayBitmap;
    private Canvas[] overlayCanvas;
    private float overlayCx;
    private float overlayCy;
    private Paint overlayEraserPaint;
    private Bitmap overlayMaskBitmap;
    private Canvas overlayMaskCanvas;
    private Paint overlayMaskPaint;
    private float overlayRad;
    private int overrideColorProgress;
    private Paint paint;
    private Paint paint2;
    private int[] pressedState;
    private float progress;
    private RectF rectF;
    private Theme.ResourcesProvider resourcesProvider;
    private RippleDrawable rippleDrawable;
    private Paint ripplePaint;
    private int thumbCheckedColorKey;
    private int thumbColorKey;
    private int trackCheckedColorKey;
    private int trackColorKey;

    /* loaded from: classes3.dex */
    public interface OnCheckedChangeListener {
    }

    public Switch(Context context) {
        this(context, null);
    }

    public Switch(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.iconProgress = 1.0f;
        this.trackColorKey = Theme.key_fill_RedNormal;
        this.trackCheckedColorKey = Theme.key_switch2TrackChecked;
        int i = Theme.key_windowBackgroundWhite;
        this.thumbColorKey = i;
        this.thumbCheckedColorKey = i;
        this.pressedState = new int[]{16842910, 16842919};
        this.resourcesProvider = resourcesProvider;
        this.rectF = new RectF();
        this.paint = new Paint(1);
        Paint paint = new Paint(1);
        this.paint2 = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.paint2.setStrokeCap(Paint.Cap.ROUND);
        this.paint2.setStrokeWidth(AndroidUtilities.dp(2.0f));
        setHapticFeedbackEnabled(true);
    }

    private void animateIcon(boolean z) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "iconProgress", z ? 1.0f : 0.0f);
        this.iconAnimator = ofFloat;
        ofFloat.setDuration(200L);
        this.iconAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Switch.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                Switch.this.iconAnimator = null;
            }
        });
        this.iconAnimator.start();
    }

    private void animateToCheckedState(boolean z) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "progress", z ? 1.0f : 0.0f);
        this.checkAnimator = ofFloat;
        ofFloat.setDuration(200L);
        this.checkAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Switch.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                Switch.this.checkAnimator = null;
            }
        });
        this.checkAnimator.start();
    }

    private void cancelCheckAnimator() {
        ObjectAnimator objectAnimator = this.checkAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
            this.checkAnimator = null;
        }
    }

    private void cancelIconAnimator() {
        ObjectAnimator objectAnimator = this.iconAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
            this.iconAnimator = null;
        }
    }

    public float getIconProgress() {
        return this.iconProgress;
    }

    public float getProgress() {
        return this.progress;
    }

    public boolean hasIcon() {
        return this.iconDrawable != null;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attachedToWindow = true;
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attachedToWindow = false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x00a8, code lost:
        if (r12 == 0) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00ad, code lost:
        if (r12 == 0) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00af, code lost:
        r16 = 1.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00b2, code lost:
        r16 = 0.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x01f9, code lost:
        if (r1 == 0) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x01ff, code lost:
        if (r1 == 0) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0201, code lost:
        r6 = 1.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0204, code lost:
        r6 = 0.0f;
     */
    /* JADX WARN: Removed duplicated region for block: B:94:0x03df  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x03e8  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        Canvas canvas2;
        float f;
        int alpha;
        int i;
        int dpf2;
        int dpf22;
        float f2;
        int alpha2;
        RippleDrawable rippleDrawable;
        Drawable drawable;
        Canvas canvas3 = canvas;
        if (getVisibility() != 0) {
            return;
        }
        int dp = AndroidUtilities.dp(31.0f);
        AndroidUtilities.dp(20.0f);
        int i2 = 2;
        int measuredWidth = (getMeasuredWidth() - dp) / 2;
        float measuredHeight = (getMeasuredHeight() - AndroidUtilities.dpf2(14.0f)) / 2.0f;
        int dp2 = AndroidUtilities.dp(7.0f) + measuredWidth + ((int) (AndroidUtilities.dp(17.0f) * this.progress));
        int measuredHeight2 = getMeasuredHeight() / 2;
        int i3 = 0;
        int i4 = 0;
        while (i4 < i2) {
            if (i4 != 1 || this.overrideColorProgress != 0) {
                Canvas canvas4 = i4 == 0 ? canvas3 : this.overlayCanvas[i3];
                if (i4 == 1) {
                    this.overlayBitmap[i3].eraseColor(i3);
                    this.paint.setColor(-16777216);
                    this.overlayMaskCanvas.drawRect(0.0f, 0.0f, this.overlayMaskBitmap.getWidth(), this.overlayMaskBitmap.getHeight(), this.paint);
                    this.overlayMaskCanvas.drawCircle(this.overlayCx - getX(), this.overlayCy - getY(), this.overlayRad, this.overlayEraserPaint);
                }
                int i5 = this.overrideColorProgress;
                if (i5 != 1) {
                    if (i5 != i2) {
                        f2 = this.progress;
                    }
                }
                int processColor = processColor(Theme.getColor(this.trackColorKey, this.resourcesProvider));
                int processColor2 = processColor(Theme.getColor(this.trackCheckedColorKey, this.resourcesProvider));
                if (i4 == 0 && (drawable = this.iconDrawable) != null) {
                    if (this.lastIconColor != (this.isChecked ? processColor2 : processColor)) {
                        int i6 = this.isChecked ? processColor2 : processColor;
                        this.lastIconColor = i6;
                        drawable.setColorFilter(new PorterDuffColorFilter(i6, PorterDuff.Mode.MULTIPLY));
                    }
                }
                int red = Color.red(processColor);
                int red2 = Color.red(processColor2);
                int green = Color.green(processColor);
                int green2 = Color.green(processColor2);
                int blue = Color.blue(processColor);
                int blue2 = Color.blue(processColor2);
                int alpha3 = ((((int) (red + ((red2 - red) * f2))) & NotificationCenter.closeSearchByActiveAction) << 16) | ((((int) (Color.alpha(processColor) + ((Color.alpha(processColor2) - alpha2) * f2))) & NotificationCenter.closeSearchByActiveAction) << 24) | ((((int) (green + ((green2 - green) * f2))) & NotificationCenter.closeSearchByActiveAction) << 8) | (((int) (blue + ((blue2 - blue) * f2))) & NotificationCenter.closeSearchByActiveAction);
                this.paint.setColor(alpha3);
                this.paint2.setColor(alpha3);
                this.rectF.set(measuredWidth, measuredHeight, measuredWidth + dp, AndroidUtilities.dpf2(14.0f) + measuredHeight);
                canvas4.drawRoundRect(this.rectF, AndroidUtilities.dpf2(7.0f), AndroidUtilities.dpf2(7.0f), this.paint);
                canvas4.drawCircle(dp2, measuredHeight2, AndroidUtilities.dpf2(10.0f), this.paint);
                if (i4 == 0 && (rippleDrawable = this.rippleDrawable) != null) {
                    rippleDrawable.setBounds(dp2 - AndroidUtilities.dp(18.0f), measuredHeight2 - AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f) + dp2, AndroidUtilities.dp(18.0f) + measuredHeight2);
                    this.rippleDrawable.draw(canvas4);
                } else if (i4 == 1) {
                    canvas4.drawBitmap(this.overlayMaskBitmap, 0.0f, 0.0f, this.overlayMaskPaint);
                }
            }
            i4++;
            canvas3 = canvas;
            i2 = 2;
            i3 = 0;
        }
        if (this.overrideColorProgress != 0) {
            canvas2 = canvas;
            canvas2.drawBitmap(this.overlayBitmap[0], 0.0f, 0.0f, (Paint) null);
        } else {
            canvas2 = canvas;
        }
        int i7 = 0;
        while (i7 < 2) {
            if (i7 != 1 || this.overrideColorProgress != 0) {
                Canvas canvas5 = i7 == 0 ? canvas2 : this.overlayCanvas[1];
                if (i7 == 1) {
                    this.overlayBitmap[1].eraseColor(0);
                }
                int i8 = this.overrideColorProgress;
                if (i8 != 1) {
                    if (i8 != 2) {
                        f = this.progress;
                    }
                }
                int color = Theme.getColor(this.thumbColorKey, this.resourcesProvider);
                int processColor3 = processColor(Theme.getColor(this.thumbCheckedColorKey, this.resourcesProvider));
                int red3 = Color.red(color);
                int red4 = Color.red(processColor3);
                int green3 = Color.green(color);
                int green4 = Color.green(processColor3);
                int blue3 = Color.blue(color);
                int blue4 = Color.blue(processColor3);
                this.paint.setColor(((((int) (Color.alpha(color) + ((Color.alpha(processColor3) - alpha) * f))) & NotificationCenter.closeSearchByActiveAction) << 24) | ((((int) (red3 + ((red4 - red3) * f))) & NotificationCenter.closeSearchByActiveAction) << 16) | ((((int) (green3 + ((green4 - green3) * f))) & NotificationCenter.closeSearchByActiveAction) << 8) | (((int) (blue3 + ((blue4 - blue3) * f))) & NotificationCenter.closeSearchByActiveAction));
                float f3 = dp2;
                float f4 = measuredHeight2;
                canvas5.drawCircle(f3, f4, AndroidUtilities.dp(8.0f), this.paint);
                if (i7 == 0) {
                    Drawable drawable2 = this.iconDrawable;
                    if (drawable2 != null) {
                        drawable2.setBounds(dp2 - (drawable2.getIntrinsicWidth() / 2), measuredHeight2 - (this.iconDrawable.getIntrinsicHeight() / 2), (this.iconDrawable.getIntrinsicWidth() / 2) + dp2, (this.iconDrawable.getIntrinsicHeight() / 2) + measuredHeight2);
                        this.iconDrawable.draw(canvas5);
                    } else {
                        int i9 = this.drawIconType;
                        if (i9 == 1) {
                            dp2 = (int) (f3 - (AndroidUtilities.dp(10.8f) - (AndroidUtilities.dp(1.3f) * this.progress)));
                            measuredHeight2 = (int) (f4 - (AndroidUtilities.dp(8.5f) - (AndroidUtilities.dp(0.5f) * this.progress)));
                            int dpf23 = ((int) AndroidUtilities.dpf2(4.6f)) + dp2;
                            int dpf24 = (int) (AndroidUtilities.dpf2(9.5f) + measuredHeight2);
                            int dp3 = AndroidUtilities.dp(2.0f) + dpf23;
                            int dp4 = AndroidUtilities.dp(2.0f) + dpf24;
                            int dpf25 = ((int) AndroidUtilities.dpf2(7.5f)) + dp2;
                            int dpf26 = ((int) AndroidUtilities.dpf2(5.4f)) + measuredHeight2;
                            int dp5 = dpf25 + AndroidUtilities.dp(7.0f);
                            int dp6 = dpf26 + AndroidUtilities.dp(7.0f);
                            float f5 = dpf25;
                            float f6 = this.progress;
                            Canvas canvas6 = canvas5;
                            canvas6.drawLine((int) (f5 + ((dpf23 - dpf25) * f6)), (int) (dpf26 + ((dpf24 - dpf26) * f6)), (int) (dp5 + ((dp3 - dp5) * f6)), (int) (dp6 + ((dp4 - dp6) * f6)), this.paint2);
                            canvas6.drawLine(((int) AndroidUtilities.dpf2(7.5f)) + dp2, ((int) AndroidUtilities.dpf2(12.5f)) + measuredHeight2, AndroidUtilities.dp(7.0f) + dpf2, dpf22 - AndroidUtilities.dp(7.0f), this.paint2);
                            i = 1;
                            if (i7 != i) {
                                canvas5.drawBitmap(this.overlayMaskBitmap, 0.0f, 0.0f, this.overlayMaskPaint);
                            }
                        } else {
                            if (i9 == 2 || this.iconAnimator != null) {
                                this.paint2.setAlpha((int) ((1.0f - this.iconProgress) * 255.0f));
                                Canvas canvas7 = canvas5;
                                canvas7.drawLine(f3, f4, f3, measuredHeight2 - AndroidUtilities.dp(5.0f), this.paint2);
                                canvas5.save();
                                canvas5.rotate(this.iconProgress * (-90.0f), f3, f4);
                                canvas7.drawLine(f3, f4, AndroidUtilities.dp(4.0f) + dp2, f4, this.paint2);
                                canvas5.restore();
                                i = 1;
                                if (i7 != i) {
                                }
                            }
                            i = 1;
                            if (i7 != i) {
                            }
                        }
                    }
                }
                i = 1;
                if (i7 != i) {
                }
            }
            i7++;
        }
        if (this.overrideColorProgress != 0) {
            canvas2.drawBitmap(this.overlayBitmap[1], 0.0f, 0.0f, (Paint) null);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("android.widget.Switch");
        accessibilityNodeInfo.setCheckable(true);
        accessibilityNodeInfo.setChecked(this.isChecked);
    }

    protected int processColor(int i) {
        return i;
    }

    public void setChecked(boolean z, int i, boolean z2) {
        if (z != this.isChecked) {
            this.isChecked = z;
            if (this.attachedToWindow && z2) {
                animateToCheckedState(z);
            } else {
                cancelCheckAnimator();
                setProgress(z ? 1.0f : 0.0f);
            }
        }
        setDrawIconType(i, z2);
    }

    public void setChecked(boolean z, boolean z2) {
        setChecked(z, this.drawIconType, z2);
    }

    public void setColors(int i, int i2, int i3, int i4) {
        this.trackColorKey = i;
        this.trackCheckedColorKey = i2;
        this.thumbColorKey = i3;
        this.thumbCheckedColorKey = i4;
    }

    public void setDrawIconType(int i) {
        this.drawIconType = i;
    }

    public void setDrawIconType(int i, boolean z) {
        if (this.drawIconType != i) {
            this.drawIconType = i;
            if (this.attachedToWindow && z) {
                animateIcon(i == 0);
                return;
            }
            cancelIconAnimator();
            setIconProgress(i == 0 ? 1.0f : 0.0f);
        }
    }

    public void setDrawRipple(boolean z) {
        int i = Build.VERSION.SDK_INT;
        if (i < 21 || z == this.drawRipple) {
            return;
        }
        this.drawRipple = z;
        if (this.rippleDrawable == null) {
            Paint paint = new Paint(1);
            this.ripplePaint = paint;
            paint.setColor(-1);
            BaseCell.RippleDrawableSafe rippleDrawableSafe = new BaseCell.RippleDrawableSafe(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{0}), null, i >= 23 ? null : new Drawable() { // from class: org.telegram.ui.Components.Switch.1
                @Override // android.graphics.drawable.Drawable
                public void draw(Canvas canvas) {
                    android.graphics.Rect bounds = getBounds();
                    canvas.drawCircle(bounds.centerX(), bounds.centerY(), AndroidUtilities.dp(18.0f), Switch.this.ripplePaint);
                }

                @Override // android.graphics.drawable.Drawable
                public int getOpacity() {
                    return 0;
                }

                @Override // android.graphics.drawable.Drawable
                public void setAlpha(int i2) {
                }

                @Override // android.graphics.drawable.Drawable
                public void setColorFilter(ColorFilter colorFilter) {
                }
            });
            this.rippleDrawable = rippleDrawableSafe;
            if (i >= 23) {
                rippleDrawableSafe.setRadius(AndroidUtilities.dp(18.0f));
            }
            this.rippleDrawable.setCallback(this);
        }
        boolean z2 = this.isChecked;
        if ((z2 && this.colorSet != 2) || (!z2 && this.colorSet != 1)) {
            this.rippleDrawable.setColor(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{processColor(Theme.getColor(z2 ? Theme.key_switchTrackBlueSelectorChecked : Theme.key_switchTrackBlueSelector, this.resourcesProvider))}));
            this.colorSet = this.isChecked ? 2 : 1;
        }
        if (i >= 28 && z) {
            this.rippleDrawable.setHotspot(this.isChecked ? 0.0f : AndroidUtilities.dp(100.0f), AndroidUtilities.dp(18.0f));
        }
        this.rippleDrawable.setState(z ? this.pressedState : StateSet.NOTHING);
        invalidate();
    }

    public void setIcon(int i) {
        if (i != 0) {
            Drawable mutate = getResources().getDrawable(i).mutate();
            this.iconDrawable = mutate;
            if (mutate != null) {
                int color = Theme.getColor(this.isChecked ? this.trackCheckedColorKey : this.trackColorKey, this.resourcesProvider);
                this.lastIconColor = color;
                mutate.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
            }
        } else {
            this.iconDrawable = null;
        }
        invalidate();
    }

    public void setIconProgress(float f) {
        if (this.iconProgress == f) {
            return;
        }
        this.iconProgress = f;
        invalidate();
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
    }

    public void setOverrideColor(int i) {
        if (this.overrideColorProgress == i) {
            return;
        }
        if (this.overlayBitmap == null) {
            try {
                this.overlayBitmap = new Bitmap[2];
                this.overlayCanvas = new Canvas[2];
                for (int i2 = 0; i2 < 2; i2++) {
                    this.overlayBitmap[i2] = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                    this.overlayCanvas[i2] = new Canvas(this.overlayBitmap[i2]);
                }
                this.overlayMaskBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                this.overlayMaskCanvas = new Canvas(this.overlayMaskBitmap);
                Paint paint = new Paint(1);
                this.overlayEraserPaint = paint;
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                Paint paint2 = new Paint(1);
                this.overlayMaskPaint = paint2;
                paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                this.bitmapsCreated = true;
            } catch (Throwable unused) {
                return;
            }
        }
        if (this.bitmapsCreated) {
            this.overrideColorProgress = i;
            this.overlayCx = 0.0f;
            this.overlayCy = 0.0f;
            this.overlayRad = 0.0f;
            invalidate();
        }
    }

    public void setOverrideColorProgress(float f, float f2, float f3) {
        this.overlayCx = f;
        this.overlayCy = f2;
        this.overlayRad = f3;
        invalidate();
    }

    public void setProgress(float f) {
        if (this.progress == f) {
            return;
        }
        this.progress = f;
        invalidate();
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        RippleDrawable rippleDrawable;
        return super.verifyDrawable(drawable) || ((rippleDrawable = this.rippleDrawable) != null && drawable == rippleDrawable);
    }
}
