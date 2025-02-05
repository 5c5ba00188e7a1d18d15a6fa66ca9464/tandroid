package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

/* loaded from: classes3.dex */
public class SlideChooseView extends View {
    private final SeekBarAccessibilityDelegate accessibilityDelegate;
    private Callback callback;
    private int circleSize;
    private int dashedFrom;
    private int gapSize;
    private int lastDash;
    private Drawable[] leftDrawables;
    private Paint linePaint;
    private int lineSize;
    private int minIndex;
    private boolean moving;
    private AnimatedFloat movingAnimatedHolder;
    private int[] optionsSizes;
    private String[] optionsStr;
    private Paint paint;
    private final Theme.ResourcesProvider resourcesProvider;
    private int selectedIndex;
    private AnimatedFloat selectedIndexAnimatedHolder;
    private float selectedIndexTouch;
    private int sideSide;
    private boolean startMoving;
    private int startMovingPreset;
    private TextPaint textPaint;
    private boolean touchWasClose;
    private float xTouchDown;
    private float yTouchDown;

    public interface Callback {

        public abstract /* synthetic */ class -CC {
            public static void $default$onTouchEnd(Callback callback) {
            }
        }

        void onOptionSelected(int i);

        void onTouchEnd();
    }

    public SlideChooseView(Context context) {
        this(context, null);
    }

    public SlideChooseView(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.dashedFrom = -1;
        this.minIndex = Integer.MIN_VALUE;
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
        this.selectedIndexAnimatedHolder = new AnimatedFloat(this, 120L, cubicBezierInterpolator);
        this.movingAnimatedHolder = new AnimatedFloat(this, 150L, cubicBezierInterpolator);
        this.touchWasClose = false;
        this.resourcesProvider = resourcesProvider;
        this.paint = new Paint(1);
        this.textPaint = new TextPaint(1);
        Paint paint = new Paint(1);
        this.linePaint = paint;
        paint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        this.linePaint.setStrokeCap(Paint.Cap.ROUND);
        this.textPaint.setTextSize(AndroidUtilities.dp(13.0f));
        this.accessibilityDelegate = new IntSeekBarAccessibilityDelegate() { // from class: org.telegram.ui.Components.SlideChooseView.1
            @Override // org.telegram.ui.Components.SeekBarAccessibilityDelegate
            protected CharSequence getContentDescription(View view) {
                if (SlideChooseView.this.selectedIndex < SlideChooseView.this.optionsStr.length) {
                    return SlideChooseView.this.optionsStr[SlideChooseView.this.selectedIndex];
                }
                return null;
            }

            @Override // org.telegram.ui.Components.IntSeekBarAccessibilityDelegate
            protected int getMaxValue() {
                return SlideChooseView.this.optionsStr.length - 1;
            }

            @Override // org.telegram.ui.Components.IntSeekBarAccessibilityDelegate
            protected int getProgress() {
                return SlideChooseView.this.selectedIndex;
            }

            @Override // org.telegram.ui.Components.IntSeekBarAccessibilityDelegate
            protected void setProgress(int i) {
                SlideChooseView.this.setOption(i);
            }
        };
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setOption(int i) {
        if (this.selectedIndex != i) {
            AndroidUtilities.vibrateCursor(this);
        }
        this.selectedIndex = i;
        Callback callback = this.callback;
        if (callback != null) {
            callback.onOptionSelected(i);
        }
        invalidate();
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x025f  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0262 A[SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        float f;
        int i;
        int i2;
        int i3;
        float measuredWidth;
        float measuredWidth2;
        float dp;
        float f2;
        int i4;
        int i5 = 2;
        float f3 = this.selectedIndexAnimatedHolder.set(this.selectedIndex);
        float f4 = 0.0f;
        float f5 = 1.0f;
        float f6 = this.movingAnimatedHolder.set(this.moving ? 1.0f : 0.0f);
        int measuredHeight = (getMeasuredHeight() / 2) + AndroidUtilities.dp(11.0f);
        int i6 = 0;
        while (i6 < this.optionsStr.length) {
            int i7 = this.sideSide;
            int i8 = this.lineSize + (this.gapSize * 2);
            int i9 = this.circleSize;
            int i10 = i7 + ((i8 + i9) * i6) + (i9 / i5);
            float f7 = i6;
            float f8 = f7 - f3;
            float max = Math.max(f4, f5 - Math.abs(f8));
            float clamp = MathUtils.clamp((f3 - f7) + f5, f4, f5);
            int themedColor = getThemedColor(Theme.key_switchTrack);
            int themedColor2 = getThemedColor(Theme.key_switchTrackChecked);
            int i11 = this.minIndex;
            int blendARGB = ColorUtils.blendARGB(themedColor, Theme.multAlpha(themedColor2, (i11 == Integer.MIN_VALUE || i6 > i11) ? 1.0f : 0.5f), clamp);
            this.paint.setColor(blendARGB);
            this.linePaint.setColor(blendARGB);
            float f9 = measuredHeight;
            canvas.drawCircle(i10, f9, AndroidUtilities.lerp(this.circleSize / i5, AndroidUtilities.dp(6.0f), max), this.paint);
            if (i6 != 0) {
                int i12 = (i10 - (this.circleSize / i5)) - this.gapSize;
                int i13 = this.lineSize;
                int i14 = i12 - i13;
                int i15 = this.dashedFrom;
                if (i15 == -1 || i6 - 1 < i15) {
                    f = max;
                    i = i10;
                    i2 = i6;
                    float f10 = f8 - 1.0f;
                    float clamp2 = MathUtils.clamp(1.0f - Math.abs(f10), 0.0f, 1.0f);
                    int dp2 = (int) (i13 - (AndroidUtilities.dp(3.0f) * MathUtils.clamp(1.0f - Math.min(Math.abs(f8), Math.abs(f10)), 0.0f, 1.0f)));
                    canvas.drawRect((int) (i14 + (AndroidUtilities.dp(3.0f) * clamp2)), measuredHeight - AndroidUtilities.dp(1.0f), r1 + dp2, AndroidUtilities.dp(1.0f) + measuredHeight, this.paint);
                } else {
                    int dp3 = i14 + AndroidUtilities.dp(3.0f);
                    int dp4 = (i13 - AndroidUtilities.dp(3.0f)) / AndroidUtilities.dp(13.0f);
                    if (this.lastDash != dp4) {
                        f2 = max;
                        i4 = i10;
                        this.linePaint.setPathEffect(new DashPathEffect(new float[]{AndroidUtilities.dp(6.0f), (r12 - (AndroidUtilities.dp(8.0f) * dp4)) / (dp4 - 1)}, 0.0f));
                        this.lastDash = dp4;
                    } else {
                        f2 = max;
                        i4 = i10;
                    }
                    f = f2;
                    i = i4;
                    i2 = i6;
                    canvas.drawLine(AndroidUtilities.dp(1.0f) + dp3, f9, (dp3 + r12) - AndroidUtilities.dp(1.0f), f9, this.linePaint);
                }
            } else {
                f = max;
                i = i10;
                i2 = i6;
            }
            int i16 = this.optionsSizes[i2];
            String str = this.optionsStr[i2];
            this.textPaint.setColor(ColorUtils.blendARGB(getThemedColor(Theme.key_windowBackgroundWhiteGrayText), getThemedColor(Theme.key_windowBackgroundWhiteBlueText), f));
            if (this.leftDrawables != null) {
                canvas.save();
                if (i2 == 0) {
                    measuredWidth2 = AndroidUtilities.dp(12.0f);
                    dp = AndroidUtilities.dp(15.5f);
                } else {
                    measuredWidth2 = (i2 == this.optionsStr.length - 1 ? (getMeasuredWidth() - i16) - AndroidUtilities.dp(22.0f) : i - (i16 / 2)) - AndroidUtilities.dp(10.0f);
                    dp = AndroidUtilities.dp(28.0f) - AndroidUtilities.dp(12.5f);
                }
                canvas.translate(measuredWidth2, dp);
                this.leftDrawables[i2].setColorFilter(this.textPaint.getColor(), PorterDuff.Mode.MULTIPLY);
                this.leftDrawables[i2].draw(canvas);
                canvas.restore();
                canvas.save();
                canvas.translate((this.leftDrawables[i2].getIntrinsicWidth() / 2.0f) - AndroidUtilities.dp(i2 == 0 ? 3.0f : 2.0f), 0.0f);
            }
            if (i2 == 0) {
                i3 = AndroidUtilities.dp(22.0f);
            } else if (i2 == this.optionsStr.length - 1) {
                measuredWidth = (getMeasuredWidth() - i16) - AndroidUtilities.dp(22.0f);
                canvas.drawText(str, measuredWidth, AndroidUtilities.dp(28.0f), this.textPaint);
                if (this.leftDrawables == null) {
                    canvas.restore();
                }
                i6 = i2 + 1;
                i5 = 2;
                f4 = 0.0f;
                f5 = 1.0f;
            } else {
                i3 = i - (i16 / 2);
            }
            measuredWidth = i3;
            canvas.drawText(str, measuredWidth, AndroidUtilities.dp(28.0f), this.textPaint);
            if (this.leftDrawables == null) {
            }
            i6 = i2 + 1;
            i5 = 2;
            f4 = 0.0f;
            f5 = 1.0f;
        }
        float f11 = this.sideSide;
        int i17 = this.lineSize + (this.gapSize * 2);
        int i18 = this.circleSize;
        float f12 = f11 + ((i17 + i18) * f3) + (i18 / 2);
        Paint paint = this.paint;
        int i19 = Theme.key_switchTrackChecked;
        paint.setColor(ColorUtils.setAlphaComponent(getThemedColor(i19), 80));
        float f13 = measuredHeight;
        canvas.drawCircle(f12, f13, AndroidUtilities.dp(f6 * 12.0f), this.paint);
        this.paint.setColor(getThemedColor(i19));
        canvas.drawCircle(f12, f13, AndroidUtilities.dp(6.0f), this.paint);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        this.accessibilityDelegate.onInitializeAccessibilityNodeInfoInternal(this, accessibilityNodeInfo);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(74.0f), 1073741824));
        this.circleSize = AndroidUtilities.dp(6.0f);
        this.gapSize = AndroidUtilities.dp(2.0f);
        this.sideSide = AndroidUtilities.dp(22.0f);
        int measuredWidth = getMeasuredWidth();
        int i3 = this.circleSize;
        String[] strArr = this.optionsStr;
        this.lineSize = (((measuredWidth - (i3 * strArr.length)) - ((this.gapSize * 2) * (strArr.length - 1))) - (this.sideSide * 2)) / Math.max(1, strArr.length - 1);
    }

    /* JADX WARN: Code restructure failed: missing block: B:50:0x00f1, code lost:
    
        if (r10 != r9.startMovingPreset) goto L44;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int i;
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        float clamp = MathUtils.clamp(((x - this.sideSide) + (this.circleSize / 2.0f)) / ((this.lineSize + (this.gapSize * 2)) + r3), 0.0f, this.optionsStr.length - 1);
        boolean z = Math.abs(clamp - ((float) Math.round(clamp))) < 0.35f;
        if (z) {
            clamp = Math.round(clamp);
        }
        int i2 = this.minIndex;
        if (i2 != Integer.MIN_VALUE) {
            clamp = Math.max(clamp, i2);
        }
        if (motionEvent.getAction() == 0) {
            this.xTouchDown = x;
            this.yTouchDown = y;
            this.selectedIndexTouch = clamp;
            this.startMovingPreset = this.selectedIndex;
            this.startMoving = true;
        } else {
            if (motionEvent.getAction() != 2) {
                if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    if (this.moving) {
                        i = this.selectedIndex;
                    } else {
                        this.selectedIndexTouch = clamp;
                        if (motionEvent.getAction() == 1 && Math.round(this.selectedIndexTouch) != this.selectedIndex) {
                            i = Math.round(this.selectedIndexTouch);
                            setOption(i);
                        }
                        Callback callback = this.callback;
                        if (callback != null) {
                            callback.onTouchEnd();
                        }
                        this.startMoving = false;
                        this.moving = false;
                        invalidate();
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return true;
            }
            if (!this.moving && Math.abs(this.xTouchDown - x) > Math.abs(this.yTouchDown - y)) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (this.startMoving && Math.abs(this.xTouchDown - x) >= AndroidUtilities.touchSlop) {
                this.moving = true;
                this.startMoving = false;
            }
            if (this.moving) {
                this.selectedIndexTouch = clamp;
                invalidate();
                if (Math.round(this.selectedIndexTouch) != this.selectedIndex && z) {
                    setOption(Math.round(this.selectedIndexTouch));
                }
            }
        }
        invalidate();
        return true;
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int i, Bundle bundle) {
        return super.performAccessibilityAction(i, bundle) || this.accessibilityDelegate.performAccessibilityActionInternal(this, i, bundle);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setDashedFrom(int i) {
        this.dashedFrom = i;
    }

    public void setMinAllowedIndex(int i) {
        String[] strArr;
        if (i != -1 && (strArr = this.optionsStr) != null) {
            i = Math.min(i, strArr.length - 1);
        }
        if (this.minIndex != i) {
            this.minIndex = i;
            if (this.selectedIndex < i) {
                this.selectedIndex = i;
            }
            invalidate();
        }
    }

    public void setOptions(int i, Drawable[] drawableArr, String... strArr) {
        this.optionsStr = strArr;
        this.leftDrawables = drawableArr;
        this.selectedIndex = i;
        this.optionsSizes = new int[strArr.length];
        int i2 = 0;
        while (true) {
            if (i2 >= this.optionsStr.length) {
                break;
            }
            this.optionsSizes[i2] = (int) Math.ceil(this.textPaint.measureText(r7[i2]));
            i2++;
        }
        Drawable[] drawableArr2 = this.leftDrawables;
        if (drawableArr2 != null) {
            for (Drawable drawable : drawableArr2) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
        }
        requestLayout();
    }

    public void setOptions(int i, String... strArr) {
        setOptions(i, null, strArr);
    }
}
