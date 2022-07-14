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
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.StateSet;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.app.NotificationCompat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.OneUIUtilities;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes5.dex */
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
    private OnCheckedChangeListener onCheckedChangeListener;
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
    private boolean semHaptics;
    private String thumbCheckedColorKey;
    private String thumbColorKey;
    private String trackCheckedColorKey;
    private String trackColorKey;

    /* loaded from: classes5.dex */
    public interface OnCheckedChangeListener {
        void onCheckedChanged(Switch r1, boolean z);
    }

    public Switch(Context context) {
        this(context, null);
    }

    public Switch(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.iconProgress = 1.0f;
        this.trackColorKey = Theme.key_switch2Track;
        this.trackCheckedColorKey = Theme.key_switch2TrackChecked;
        this.thumbColorKey = Theme.key_windowBackgroundWhite;
        this.thumbCheckedColorKey = Theme.key_windowBackgroundWhite;
        this.pressedState = new int[]{16842910, 16842919};
        this.semHaptics = false;
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

    public void setProgress(float value) {
        if (this.progress == value) {
            return;
        }
        this.progress = value;
        invalidate();
    }

    public float getProgress() {
        return this.progress;
    }

    public void setIconProgress(float value) {
        if (this.iconProgress == value) {
            return;
        }
        this.iconProgress = value;
        invalidate();
    }

    public float getIconProgress() {
        return this.iconProgress;
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

    public void setDrawIconType(int type) {
        this.drawIconType = type;
    }

    public void setDrawRipple(boolean value) {
        String str;
        Theme.ResourcesProvider resourcesProvider;
        Drawable maskDrawable;
        if (Build.VERSION.SDK_INT < 21 || value == this.drawRipple) {
            return;
        }
        this.drawRipple = value;
        int i = 1;
        if (this.rippleDrawable == null) {
            Paint paint = new Paint(1);
            this.ripplePaint = paint;
            paint.setColor(-1);
            if (Build.VERSION.SDK_INT >= 23) {
                maskDrawable = null;
            } else {
                maskDrawable = new Drawable() { // from class: org.telegram.ui.Components.Switch.1
                    @Override // android.graphics.drawable.Drawable
                    public void draw(Canvas canvas) {
                        android.graphics.Rect bounds = getBounds();
                        canvas.drawCircle(bounds.centerX(), bounds.centerY(), AndroidUtilities.dp(18.0f), Switch.this.ripplePaint);
                    }

                    @Override // android.graphics.drawable.Drawable
                    public void setAlpha(int alpha) {
                    }

                    @Override // android.graphics.drawable.Drawable
                    public void setColorFilter(ColorFilter colorFilter) {
                    }

                    @Override // android.graphics.drawable.Drawable
                    public int getOpacity() {
                        return 0;
                    }
                };
            }
            ColorStateList colorStateList = new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{0});
            this.rippleDrawable = new RippleDrawable(colorStateList, null, maskDrawable);
            if (Build.VERSION.SDK_INT >= 23) {
                this.rippleDrawable.setRadius(AndroidUtilities.dp(18.0f));
            }
            this.rippleDrawable.setCallback(this);
        }
        boolean z = this.isChecked;
        if ((z && this.colorSet != 2) || (!z && this.colorSet != 1)) {
            if (z) {
                resourcesProvider = this.resourcesProvider;
                str = Theme.key_switchTrackBlueSelectorChecked;
            } else {
                resourcesProvider = this.resourcesProvider;
                str = Theme.key_switchTrackBlueSelector;
            }
            int color = Theme.getColor(str, resourcesProvider);
            ColorStateList colorStateList2 = new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{color});
            this.rippleDrawable.setColor(colorStateList2);
            if (this.isChecked) {
                i = 2;
            }
            this.colorSet = i;
        }
        int color2 = Build.VERSION.SDK_INT;
        if (color2 >= 28 && value) {
            this.rippleDrawable.setHotspot(this.isChecked ? 0.0f : AndroidUtilities.dp(100.0f), AndroidUtilities.dp(18.0f));
        }
        this.rippleDrawable.setState(value ? this.pressedState : StateSet.NOTHING);
        invalidate();
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable who) {
        RippleDrawable rippleDrawable;
        return super.verifyDrawable(who) || ((rippleDrawable = this.rippleDrawable) != null && who == rippleDrawable);
    }

    public void setColors(String track, String trackChecked, String thumb, String thumbChecked) {
        this.trackColorKey = track;
        this.trackCheckedColorKey = trackChecked;
        this.thumbColorKey = thumb;
        this.thumbCheckedColorKey = thumbChecked;
    }

    private void animateToCheckedState(boolean newCheckedState) {
        float[] fArr = new float[1];
        fArr[0] = newCheckedState ? 1.0f : 0.0f;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, NotificationCompat.CATEGORY_PROGRESS, fArr);
        this.checkAnimator = ofFloat;
        ofFloat.setDuration(this.semHaptics ? 150L : 250L);
        this.checkAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Switch.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                Switch.this.checkAnimator = null;
            }
        });
        this.checkAnimator.start();
    }

    private void animateIcon(boolean newCheckedState) {
        float[] fArr = new float[1];
        fArr[0] = newCheckedState ? 1.0f : 0.0f;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "iconProgress", fArr);
        this.iconAnimator = ofFloat;
        ofFloat.setDuration(this.semHaptics ? 150L : 250L);
        this.iconAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.Switch.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                Switch.this.iconAnimator = null;
            }
        });
        this.iconAnimator.start();
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

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    public void setChecked(boolean checked, boolean animated) {
        setChecked(checked, this.drawIconType, animated);
    }

    public void setChecked(boolean checked, int iconType, boolean animated) {
        float f = 1.0f;
        if (checked != this.isChecked) {
            this.isChecked = checked;
            if (this.attachedToWindow && animated) {
                vibrateChecked(checked);
                animateToCheckedState(checked);
            } else {
                cancelCheckAnimator();
                setProgress(checked ? 1.0f : 0.0f);
            }
            OnCheckedChangeListener onCheckedChangeListener = this.onCheckedChangeListener;
            if (onCheckedChangeListener != null) {
                onCheckedChangeListener.onCheckedChanged(this, checked);
            }
        }
        if (this.drawIconType != iconType) {
            this.drawIconType = iconType;
            if (this.attachedToWindow && animated) {
                animateIcon(iconType == 0);
                return;
            }
            cancelIconAnimator();
            if (iconType != 0) {
                f = 0.0f;
            }
            setIconProgress(f);
        }
    }

    public void setIcon(int icon) {
        if (icon != 0) {
            Drawable mutate = getResources().getDrawable(icon).mutate();
            this.iconDrawable = mutate;
            if (mutate != null) {
                int color = Theme.getColor(this.isChecked ? this.trackCheckedColorKey : this.trackColorKey, this.resourcesProvider);
                this.lastIconColor = color;
                mutate.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
                return;
            }
            return;
        }
        this.iconDrawable = null;
    }

    public boolean hasIcon() {
        return this.iconDrawable != null;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setOverrideColor(int override) {
        if (this.overrideColorProgress == override) {
            return;
        }
        if (this.overlayBitmap == null) {
            try {
                this.overlayBitmap = new Bitmap[2];
                this.overlayCanvas = new Canvas[2];
                for (int a = 0; a < 2; a++) {
                    this.overlayBitmap[a] = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                    this.overlayCanvas[a] = new Canvas(this.overlayBitmap[a]);
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
            } catch (Throwable th) {
                return;
            }
        }
        if (!this.bitmapsCreated) {
            return;
        }
        this.overrideColorProgress = override;
        this.overlayCx = 0.0f;
        this.overlayCy = 0.0f;
        this.overlayRad = 0.0f;
        invalidate();
    }

    public void setOverrideColorProgress(float cx, float cy, float rad) {
        this.overlayCx = cx;
        this.overlayCy = cy;
        this.overlayRad = rad;
        invalidate();
    }

    /* JADX WARN: Removed duplicated region for block: B:109:0x01db A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x01d3  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        int i;
        Canvas canvas2;
        float y;
        int i2;
        Canvas canvasToDraw;
        float colorProgress;
        int width;
        int x;
        int thumb;
        int a;
        float colorProgress2;
        int a2;
        RippleDrawable rippleDrawable;
        Drawable drawable;
        Canvas canvas3 = canvas;
        if (getVisibility() != 0) {
            return;
        }
        int width2 = AndroidUtilities.dp(31.0f);
        int thumb2 = AndroidUtilities.dp(20.0f);
        int i3 = 2;
        int x2 = (getMeasuredWidth() - width2) / 2;
        float y2 = (getMeasuredHeight() - AndroidUtilities.dpf2(14.0f)) / 2.0f;
        int tx = AndroidUtilities.dp(7.0f) + x2 + ((int) (AndroidUtilities.dp(17.0f) * this.progress));
        int ty = getMeasuredHeight() / 2;
        int a1 = 0;
        while (true) {
            i = 0;
            if (a1 >= i3) {
                break;
            }
            if (a1 == 1 && this.overrideColorProgress == 0) {
                width = width2;
                thumb = thumb2;
                x = x2;
                a = a1;
            } else {
                Canvas canvasToDraw2 = a1 == 0 ? canvas3 : this.overlayCanvas[0];
                if (a1 == 1) {
                    this.overlayBitmap[0].eraseColor(0);
                    this.paint.setColor(-16777216);
                    this.overlayMaskCanvas.drawRect(0.0f, 0.0f, this.overlayMaskBitmap.getWidth(), this.overlayMaskBitmap.getHeight(), this.paint);
                    this.overlayMaskCanvas.drawCircle(this.overlayCx - getX(), this.overlayCy - getY(), this.overlayRad, this.overlayEraserPaint);
                }
                int i4 = this.overrideColorProgress;
                if (i4 == 1) {
                    colorProgress2 = a1 == 0 ? 0.0f : 1.0f;
                } else if (i4 == 2) {
                    colorProgress2 = a1 == 0 ? 1.0f : 0.0f;
                } else {
                    colorProgress2 = this.progress;
                }
                int color1 = Theme.getColor(this.trackColorKey, this.resourcesProvider);
                int color2 = Theme.getColor(this.trackCheckedColorKey, this.resourcesProvider);
                if (a1 == 0 && (drawable = this.iconDrawable) != null) {
                    if (this.lastIconColor != (this.isChecked ? color2 : color1)) {
                        int i5 = this.isChecked ? color2 : color1;
                        this.lastIconColor = i5;
                        thumb = thumb2;
                        drawable.setColorFilter(new PorterDuffColorFilter(i5, PorterDuff.Mode.MULTIPLY));
                        int r1 = Color.red(color1);
                        int r2 = Color.red(color2);
                        int g1 = Color.green(color1);
                        int g2 = Color.green(color2);
                        int b1 = Color.blue(color1);
                        int b2 = Color.blue(color2);
                        a2 = a1;
                        int a12 = Color.alpha(color1);
                        int a22 = Color.alpha(color2);
                        int red = (int) (r1 + ((r2 - r1) * colorProgress2));
                        int green = (int) (g1 + ((g2 - g1) * colorProgress2));
                        int r22 = b2 - b1;
                        int blue = (int) (b1 + (r22 * colorProgress2));
                        int color = ((red & 255) << 16) | ((((int) (a12 + ((a22 - a12) * colorProgress2))) & 255) << 24) | ((green & 255) << 8) | (blue & 255);
                        this.paint.setColor(color);
                        this.paint2.setColor(color);
                        int green2 = x2 + width2;
                        width = width2;
                        this.rectF.set(x2, y2, green2, y2 + AndroidUtilities.dpf2(14.0f));
                        canvasToDraw2.drawRoundRect(this.rectF, AndroidUtilities.dpf2(7.0f), AndroidUtilities.dpf2(7.0f), this.paint);
                        canvasToDraw2.drawCircle(tx, ty, AndroidUtilities.dpf2(10.0f), this.paint);
                        if (a2 == 0 || (rippleDrawable = this.rippleDrawable) == null) {
                            x = x2;
                            a = a2;
                            if (a != 1) {
                                canvasToDraw2.drawBitmap(this.overlayMaskBitmap, 0.0f, 0.0f, this.overlayMaskPaint);
                            }
                        } else {
                            x = x2;
                            rippleDrawable.setBounds(tx - AndroidUtilities.dp(18.0f), ty - AndroidUtilities.dp(18.0f), tx + AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f) + ty);
                            this.rippleDrawable.draw(canvasToDraw2);
                            a = a2;
                        }
                    }
                }
                thumb = thumb2;
                int r12 = Color.red(color1);
                int r23 = Color.red(color2);
                int g12 = Color.green(color1);
                int g22 = Color.green(color2);
                int b12 = Color.blue(color1);
                int b22 = Color.blue(color2);
                a2 = a1;
                int a122 = Color.alpha(color1);
                int a222 = Color.alpha(color2);
                int red2 = (int) (r12 + ((r23 - r12) * colorProgress2));
                int green3 = (int) (g12 + ((g22 - g12) * colorProgress2));
                int r222 = b22 - b12;
                int blue2 = (int) (b12 + (r222 * colorProgress2));
                int color3 = ((red2 & 255) << 16) | ((((int) (a122 + ((a222 - a122) * colorProgress2))) & 255) << 24) | ((green3 & 255) << 8) | (blue2 & 255);
                this.paint.setColor(color3);
                this.paint2.setColor(color3);
                int green22 = x2 + width2;
                width = width2;
                this.rectF.set(x2, y2, green22, y2 + AndroidUtilities.dpf2(14.0f));
                canvasToDraw2.drawRoundRect(this.rectF, AndroidUtilities.dpf2(7.0f), AndroidUtilities.dpf2(7.0f), this.paint);
                canvasToDraw2.drawCircle(tx, ty, AndroidUtilities.dpf2(10.0f), this.paint);
                if (a2 == 0) {
                }
                x = x2;
                a = a2;
                if (a != 1) {
                }
            }
            a1 = a + 1;
            canvas3 = canvas;
            thumb2 = thumb;
            x2 = x;
            width2 = width;
            i3 = 2;
        }
        if (this.overrideColorProgress == 0) {
            canvas2 = canvas;
        } else {
            canvas2 = canvas;
            canvas2.drawBitmap(this.overlayBitmap[0], 0.0f, 0.0f, (Paint) null);
        }
        int a3 = 0;
        while (a3 < 2) {
            if (a3 == 1 && this.overrideColorProgress == 0) {
                y = y2;
            } else {
                if (a3 == 0) {
                    canvasToDraw = canvas2;
                    i2 = 1;
                } else {
                    i2 = 1;
                    canvasToDraw = this.overlayCanvas[1];
                }
                if (a3 == i2) {
                    this.overlayBitmap[i2].eraseColor(i);
                }
                int i6 = this.overrideColorProgress;
                if (i6 == i2) {
                    colorProgress = a3 == 0 ? 0.0f : 1.0f;
                } else if (i6 == 2) {
                    colorProgress = a3 == 0 ? 1.0f : 0.0f;
                } else {
                    colorProgress = this.progress;
                }
                int color12 = Theme.getColor(this.thumbColorKey, this.resourcesProvider);
                int color22 = Theme.getColor(this.thumbCheckedColorKey, this.resourcesProvider);
                int r13 = Color.red(color12);
                int r24 = Color.red(color22);
                int g13 = Color.green(color12);
                int g23 = Color.green(color22);
                int b13 = Color.blue(color12);
                int b23 = Color.blue(color22);
                int a13 = Color.alpha(color12);
                int a23 = Color.alpha(color22);
                y = y2;
                int red3 = (int) (r13 + ((r24 - r13) * colorProgress));
                int green4 = (int) (g13 + ((g23 - g13) * colorProgress));
                int blue3 = (int) (b13 + ((b23 - b13) * colorProgress));
                int r25 = a23 - a13;
                this.paint.setColor(((((int) (a13 + (r25 * colorProgress))) & 255) << 24) | ((red3 & 255) << 16) | ((green4 & 255) << 8) | (blue3 & 255));
                canvasToDraw.drawCircle(tx, ty, AndroidUtilities.dp(8.0f), this.paint);
                if (a3 == 0) {
                    Drawable drawable2 = this.iconDrawable;
                    if (drawable2 != null) {
                        drawable2.setBounds(tx - (drawable2.getIntrinsicWidth() / 2), ty - (this.iconDrawable.getIntrinsicHeight() / 2), (this.iconDrawable.getIntrinsicWidth() / 2) + tx, (this.iconDrawable.getIntrinsicHeight() / 2) + ty);
                        this.iconDrawable.draw(canvasToDraw);
                    } else {
                        int i7 = this.drawIconType;
                        if (i7 == 1) {
                            tx = (int) (tx - (AndroidUtilities.dp(10.8f) - (AndroidUtilities.dp(1.3f) * this.progress)));
                            ty = (int) (ty - (AndroidUtilities.dp(8.5f) - (AndroidUtilities.dp(0.5f) * this.progress)));
                            int startX2 = ((int) AndroidUtilities.dpf2(4.6f)) + tx;
                            int startY2 = (int) (AndroidUtilities.dpf2(9.5f) + ty);
                            int endX2 = AndroidUtilities.dp(2.0f) + startX2;
                            int endY2 = AndroidUtilities.dp(2.0f) + startY2;
                            int startX = ((int) AndroidUtilities.dpf2(7.5f)) + tx;
                            int startY = ((int) AndroidUtilities.dpf2(5.4f)) + ty;
                            int alpha = startX + AndroidUtilities.dp(7.0f);
                            int g14 = startY + AndroidUtilities.dp(7.0f);
                            int g24 = startX2 - startX;
                            float f = this.progress;
                            int startX3 = (int) (startX + (g24 * f));
                            int startY3 = (int) (startY + ((startY2 - startY) * f));
                            int endX = (int) (alpha + ((endX2 - alpha) * f));
                            int endY = (int) (g14 + ((endY2 - g14) * f));
                            Canvas canvas4 = canvasToDraw;
                            canvas4.drawLine(startX3, startY3, endX, endY, this.paint2);
                            int startX4 = ((int) AndroidUtilities.dpf2(7.5f)) + tx;
                            int startY4 = ((int) AndroidUtilities.dpf2(12.5f)) + ty;
                            int endX3 = AndroidUtilities.dp(7.0f) + startX4;
                            int endX4 = AndroidUtilities.dp(7.0f);
                            int endY3 = startY4 - endX4;
                            canvas4.drawLine(startX4, startY4, endX3, endY3, this.paint2);
                        } else if (i7 == 2 || this.iconAnimator != null) {
                            this.paint2.setAlpha((int) ((1.0f - this.iconProgress) * 255.0f));
                            Canvas canvas5 = canvasToDraw;
                            canvas5.drawLine(tx, ty, tx, ty - AndroidUtilities.dp(5.0f), this.paint2);
                            canvasToDraw.save();
                            canvasToDraw.rotate(this.iconProgress * (-90.0f), tx, ty);
                            canvas5.drawLine(tx, ty, AndroidUtilities.dp(4.0f) + tx, ty, this.paint2);
                            canvasToDraw.restore();
                        }
                    }
                }
                if (a3 == 1) {
                    canvasToDraw.drawBitmap(this.overlayMaskBitmap, 0.0f, 0.0f, this.overlayMaskPaint);
                }
            }
            a3++;
            y2 = y;
            i = 0;
        }
        int a4 = this.overrideColorProgress;
        if (a4 != 0) {
            canvas2.drawBitmap(this.overlayBitmap[1], 0.0f, 0.0f, (Paint) null);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName("android.widget.Switch");
        info.setCheckable(true);
        info.setChecked(this.isChecked);
    }

    private void vibrateChecked(boolean toCheck) {
        try {
            if (isHapticFeedbackEnabled() && Build.VERSION.SDK_INT >= 28) {
                Vibrator vibrator = (Vibrator) getContext().getSystemService("vibrator");
                int slightAmplitude = OneUIUtilities.isOneUI() ? 5 : 15;
                VibrationEffect vibrationEffect = VibrationEffect.createWaveform(toCheck ? new long[]{80, 25, 15} : new long[]{25, 80, 10}, toCheck ? new int[]{slightAmplitude, 0, 255} : new int[]{0, slightAmplitude, 140}, -1);
                vibrator.cancel();
                vibrator.vibrate(vibrationEffect);
                this.semHaptics = true;
            }
        } catch (Exception e) {
        }
    }
}
