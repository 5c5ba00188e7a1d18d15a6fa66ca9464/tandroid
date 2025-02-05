package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.NumberPicker;

/* loaded from: classes3.dex */
public class SwipeGestureSettingsView extends FrameLayout {
    int[] backgroundKeys;
    float colorProgress;
    int currentColorKey;
    int currentIconIndex;
    int currentIconValue;
    Paint filledPaint;
    int fromColor;
    boolean hasTabs;
    RLottieImageView[] iconViews;
    RLottieDrawable[] icons;
    Paint linePaint;
    Paint outlinePaint;
    private NumberPicker picker;
    Paint pickerDividersPaint;
    float progressToSwipeFolders;
    RectF rect;
    String[] strings;
    Runnable swapIconRunnable;

    public SwipeGestureSettingsView(Context context, int i) {
        super(context);
        this.outlinePaint = new Paint(1);
        this.filledPaint = new Paint(1);
        this.linePaint = new Paint(1);
        this.pickerDividersPaint = new Paint(1);
        this.rect = new RectF();
        String[] strArr = new String[6];
        this.strings = strArr;
        this.backgroundKeys = new int[6];
        this.icons = new RLottieDrawable[6];
        this.iconViews = new RLottieImageView[2];
        this.colorProgress = 1.0f;
        strArr[0] = LocaleController.getString(R.string.SwipeSettingsPin);
        this.strings[1] = LocaleController.getString(R.string.SwipeSettingsRead);
        this.strings[2] = LocaleController.getString(R.string.SwipeSettingsArchive);
        this.strings[3] = LocaleController.getString(R.string.SwipeSettingsMute);
        this.strings[4] = LocaleController.getString(R.string.SwipeSettingsDelete);
        this.strings[5] = LocaleController.getString(R.string.SwipeSettingsFolders);
        int[] iArr = this.backgroundKeys;
        int i2 = Theme.key_chats_archiveBackground;
        iArr[0] = i2;
        iArr[1] = i2;
        iArr[2] = i2;
        iArr[3] = i2;
        iArr[4] = Theme.key_dialogSwipeRemove;
        iArr[5] = Theme.key_chats_archivePinBackground;
        Paint paint = this.outlinePaint;
        Paint.Style style = Paint.Style.STROKE;
        paint.setStyle(style);
        this.outlinePaint.setStrokeWidth(AndroidUtilities.dp(1.0f));
        this.linePaint.setStyle(style);
        Paint paint2 = this.linePaint;
        Paint.Cap cap = Paint.Cap.ROUND;
        paint2.setStrokeCap(cap);
        this.linePaint.setStrokeWidth(AndroidUtilities.dp(5.0f));
        this.pickerDividersPaint.setStyle(style);
        this.pickerDividersPaint.setStrokeCap(cap);
        this.pickerDividersPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        NumberPicker numberPicker = new NumberPicker(context, 13) { // from class: org.telegram.ui.Components.SwipeGestureSettingsView.1
            @Override // org.telegram.ui.Components.NumberPicker, android.widget.LinearLayout, android.view.View
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                float dp = AndroidUtilities.dp(31.0f);
                SwipeGestureSettingsView.this.pickerDividersPaint.setColor(Theme.getColor(Theme.key_radioBackgroundChecked));
                canvas.drawLine(AndroidUtilities.dp(2.0f), dp, getMeasuredWidth() - AndroidUtilities.dp(2.0f), dp, SwipeGestureSettingsView.this.pickerDividersPaint);
                float measuredHeight = getMeasuredHeight() - AndroidUtilities.dp(31.0f);
                canvas.drawLine(AndroidUtilities.dp(2.0f), measuredHeight, getMeasuredWidth() - AndroidUtilities.dp(2.0f), measuredHeight, SwipeGestureSettingsView.this.pickerDividersPaint);
            }
        };
        this.picker = numberPicker;
        numberPicker.setMinValue(0);
        this.picker.setDrawDividers(false);
        boolean z = !MessagesController.getInstance(i).dialogFilters.isEmpty();
        this.hasTabs = z;
        this.picker.setMaxValue(z ? this.strings.length - 1 : this.strings.length - 2);
        this.picker.setAllItemsCount(this.hasTabs ? this.strings.length : this.strings.length - 1);
        this.picker.setWrapSelectorWheel(true);
        this.picker.setFormatter(new NumberPicker.Formatter() { // from class: org.telegram.ui.Components.SwipeGestureSettingsView$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.NumberPicker.Formatter
            public final String format(int i3) {
                String lambda$new$0;
                lambda$new$0 = SwipeGestureSettingsView.this.lambda$new$0(i3);
                return lambda$new$0;
            }
        });
        this.picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() { // from class: org.telegram.ui.Components.SwipeGestureSettingsView$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.NumberPicker.OnValueChangeListener
            public final void onValueChange(NumberPicker numberPicker2, int i3, int i4) {
                SwipeGestureSettingsView.this.lambda$new$1(numberPicker2, i3, i4);
            }
        });
        this.picker.setImportantForAccessibility(2);
        this.picker.setValue(SharedConfig.getChatSwipeAction(i));
        addView(this.picker, LayoutHelper.createFrame(NotificationCenter.httpFileDidFailedLoad, -1.0f, 5, 21.0f, 0.0f, 21.0f, 0.0f));
        setWillNotDraw(false);
        this.currentIconIndex = 0;
        for (int i3 = 0; i3 < 2; i3++) {
            this.iconViews[i3] = new RLottieImageView(context);
            addView(this.iconViews[i3], LayoutHelper.createFrame(28, 28.0f, 21, 0.0f, 0.0f, 184.0f, 0.0f));
        }
        RLottieDrawable icon = getIcon(this.picker.getValue());
        if (icon != null) {
            this.iconViews[0].setImageDrawable(icon);
            icon.setCurrentFrame(icon.getFramesCount() - 1);
        }
        AndroidUtilities.updateViewVisibilityAnimated(this.iconViews[0], true, 0.5f, false);
        AndroidUtilities.updateViewVisibilityAnimated(this.iconViews[1], false, 0.5f, false);
        this.progressToSwipeFolders = this.picker.getValue() != 5 ? 0.0f : 1.0f;
        this.currentIconValue = this.picker.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$new$0(int i) {
        return this.strings[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(NumberPicker numberPicker, int i, int i2) {
        swapIcons();
        SharedConfig.updateChatListSwipeSetting(i2);
        invalidate();
        try {
            numberPicker.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$swapIcons$2() {
        this.swapIconRunnable = null;
        swapIcons();
    }

    private void swapIcons() {
        int value;
        if (this.swapIconRunnable == null && this.currentIconValue != (value = this.picker.getValue())) {
            this.currentIconValue = value;
            int i = (this.currentIconIndex + 1) % 2;
            RLottieDrawable icon = getIcon(value);
            if (icon != null) {
                if (this.iconViews[i].getVisibility() != 0) {
                    icon.setCurrentFrame(0, false);
                }
                this.iconViews[i].setAnimation(icon);
                this.iconViews[i].playAnimation();
            } else {
                this.iconViews[i].clearAnimationDrawable();
            }
            AndroidUtilities.updateViewVisibilityAnimated(this.iconViews[this.currentIconIndex], false, 0.5f, true);
            AndroidUtilities.updateViewVisibilityAnimated(this.iconViews[i], true, 0.5f, true);
            this.currentIconIndex = i;
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.SwipeGestureSettingsView$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    SwipeGestureSettingsView.this.lambda$swapIcons$2();
                }
            };
            this.swapIconRunnable = runnable;
            AndroidUtilities.runOnUIThread(runnable, 150L);
        }
    }

    public RLottieDrawable getIcon(int i) {
        RLottieDrawable[] rLottieDrawableArr = this.icons;
        if (rLottieDrawableArr[i] == null) {
            int i2 = i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? R.raw.swipe_pin : R.raw.swipe_disabled : R.raw.swipe_delete : R.raw.swipe_mute : R.raw.chats_archive : R.raw.swipe_read;
            rLottieDrawableArr[i] = new RLottieDrawable(i2, "" + i2, AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), true, null);
            updateIconColor(i);
        }
        return this.icons[i];
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x010c  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00d0  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        RLottieImageView rLottieImageView;
        float f;
        super.onDraw(canvas);
        boolean z = this.picker.getValue() == 5;
        if (z) {
            float f2 = this.progressToSwipeFolders;
            if (f2 != 1.0f) {
                float f3 = f2 + 0.053333335f;
                this.progressToSwipeFolders = f3;
                if (f3 <= 1.0f) {
                    this.iconViews[0].invalidate();
                    rLottieImageView = this.iconViews[1];
                    rLottieImageView.invalidate();
                    invalidate();
                    Paint paint = this.outlinePaint;
                    int i = Theme.key_switchTrack;
                    paint.setColor(Theme.getColor(i));
                    this.linePaint.setColor(Theme.getColor(i));
                    int measuredWidth = getMeasuredWidth() - ((AndroidUtilities.dp(132.0f) + AndroidUtilities.dp(21.0f)) + AndroidUtilities.dp(16.0f));
                    int dp = AndroidUtilities.dp(21.0f);
                    float f4 = dp;
                    float measuredHeight = (getMeasuredHeight() - AndroidUtilities.dp(48.0f)) / 2;
                    this.rect.set(f4, measuredHeight, measuredWidth, getMeasuredHeight() - r4);
                    if (this.currentColorKey < 0) {
                    }
                    f = this.colorProgress;
                    if (f != 1.0f) {
                    }
                    int i2 = this.fromColor;
                    int i3 = Theme.key_windowBackgroundWhite;
                    this.filledPaint.setColor(ColorUtils.blendARGB(i2, ColorUtils.blendARGB(Theme.getColor(i3), Theme.getColor(this.currentColorKey), 0.9f), this.colorProgress));
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.filledPaint);
                    this.filledPaint.setColor(Theme.getColor(i3));
                    this.filledPaint.setAlpha(NotificationCenter.liveLocationsChanged);
                    this.rect.set(f4, measuredHeight, measuredWidth - AndroidUtilities.dp(58.0f), getMeasuredHeight() - r4);
                    this.rect.inset(-AndroidUtilities.dp(1.0f), -AndroidUtilities.dp(1.0f));
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.filledPaint);
                    this.outlinePaint.setAlpha(31);
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.outlinePaint);
                    canvas.save();
                    canvas.clipRect(this.rect);
                    this.filledPaint.setColor(Theme.getColor(i));
                    this.filledPaint.setAlpha(60);
                    RectF rectF = this.rect;
                    canvas.drawCircle(rectF.left + 0.0f, rectF.centerY(), AndroidUtilities.dp(15.0f), this.filledPaint);
                    float centerY = this.rect.centerY() - AndroidUtilities.dp(6.0f);
                    this.linePaint.setAlpha(57);
                    canvas.drawLine(this.rect.left + AndroidUtilities.dp(23.0f) + 0.0f, centerY, this.rect.right - AndroidUtilities.dp(68.0f), centerY, this.linePaint);
                    float centerY2 = this.rect.centerY() + AndroidUtilities.dp(6.0f);
                    canvas.drawLine(this.rect.left + AndroidUtilities.dp(23.0f) + 0.0f, centerY2, this.rect.right - AndroidUtilities.dp(23.0f), centerY2, this.linePaint);
                    canvas.restore();
                }
                this.progressToSwipeFolders = 1.0f;
                Paint paint2 = this.outlinePaint;
                int i4 = Theme.key_switchTrack;
                paint2.setColor(Theme.getColor(i4));
                this.linePaint.setColor(Theme.getColor(i4));
                int measuredWidth2 = getMeasuredWidth() - ((AndroidUtilities.dp(132.0f) + AndroidUtilities.dp(21.0f)) + AndroidUtilities.dp(16.0f));
                int dp2 = AndroidUtilities.dp(21.0f);
                float f42 = dp2;
                float measuredHeight2 = (getMeasuredHeight() - AndroidUtilities.dp(48.0f)) / 2;
                this.rect.set(f42, measuredHeight2, measuredWidth2, getMeasuredHeight() - r4);
                if (this.currentColorKey < 0) {
                    this.currentColorKey = this.backgroundKeys[this.picker.getValue()];
                    this.colorProgress = 1.0f;
                    this.fromColor = ColorUtils.blendARGB(Theme.getColor(Theme.key_windowBackgroundWhite), Theme.getColor(this.currentColorKey), 0.9f);
                } else if (this.backgroundKeys[this.picker.getValue()] != this.currentColorKey) {
                    this.fromColor = ColorUtils.blendARGB(this.fromColor, ColorUtils.blendARGB(Theme.getColor(Theme.key_windowBackgroundWhite), Theme.getColor(this.currentColorKey), 0.9f), this.colorProgress);
                    this.colorProgress = 0.0f;
                    this.currentColorKey = this.backgroundKeys[this.picker.getValue()];
                }
                f = this.colorProgress;
                if (f != 1.0f) {
                    float f5 = f + 0.16f;
                    this.colorProgress = f5;
                    if (f5 > 1.0f) {
                        this.colorProgress = 1.0f;
                    } else {
                        invalidate();
                    }
                }
                int i22 = this.fromColor;
                int i32 = Theme.key_windowBackgroundWhite;
                this.filledPaint.setColor(ColorUtils.blendARGB(i22, ColorUtils.blendARGB(Theme.getColor(i32), Theme.getColor(this.currentColorKey), 0.9f), this.colorProgress));
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.filledPaint);
                this.filledPaint.setColor(Theme.getColor(i32));
                this.filledPaint.setAlpha(NotificationCenter.liveLocationsChanged);
                this.rect.set(f42, measuredHeight2, measuredWidth2 - AndroidUtilities.dp(58.0f), getMeasuredHeight() - r4);
                this.rect.inset(-AndroidUtilities.dp(1.0f), -AndroidUtilities.dp(1.0f));
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.filledPaint);
                this.outlinePaint.setAlpha(31);
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.outlinePaint);
                canvas.save();
                canvas.clipRect(this.rect);
                this.filledPaint.setColor(Theme.getColor(i4));
                this.filledPaint.setAlpha(60);
                RectF rectF2 = this.rect;
                canvas.drawCircle(rectF2.left + 0.0f, rectF2.centerY(), AndroidUtilities.dp(15.0f), this.filledPaint);
                float centerY3 = this.rect.centerY() - AndroidUtilities.dp(6.0f);
                this.linePaint.setAlpha(57);
                canvas.drawLine(this.rect.left + AndroidUtilities.dp(23.0f) + 0.0f, centerY3, this.rect.right - AndroidUtilities.dp(68.0f), centerY3, this.linePaint);
                float centerY22 = this.rect.centerY() + AndroidUtilities.dp(6.0f);
                canvas.drawLine(this.rect.left + AndroidUtilities.dp(23.0f) + 0.0f, centerY22, this.rect.right - AndroidUtilities.dp(23.0f), centerY22, this.linePaint);
                canvas.restore();
            }
        }
        if (!z) {
            float f6 = this.progressToSwipeFolders;
            if (f6 != 0.0f) {
                float f7 = f6 - 0.053333335f;
                this.progressToSwipeFolders = f7;
                if (f7 < 0.0f) {
                    this.progressToSwipeFolders = 0.0f;
                } else {
                    this.iconViews[0].invalidate();
                    rLottieImageView = this.iconViews[1];
                    rLottieImageView.invalidate();
                    invalidate();
                }
            }
        }
        Paint paint22 = this.outlinePaint;
        int i42 = Theme.key_switchTrack;
        paint22.setColor(Theme.getColor(i42));
        this.linePaint.setColor(Theme.getColor(i42));
        int measuredWidth22 = getMeasuredWidth() - ((AndroidUtilities.dp(132.0f) + AndroidUtilities.dp(21.0f)) + AndroidUtilities.dp(16.0f));
        int dp22 = AndroidUtilities.dp(21.0f);
        float f422 = dp22;
        float measuredHeight22 = (getMeasuredHeight() - AndroidUtilities.dp(48.0f)) / 2;
        this.rect.set(f422, measuredHeight22, measuredWidth22, getMeasuredHeight() - r4);
        if (this.currentColorKey < 0) {
        }
        f = this.colorProgress;
        if (f != 1.0f) {
        }
        int i222 = this.fromColor;
        int i322 = Theme.key_windowBackgroundWhite;
        this.filledPaint.setColor(ColorUtils.blendARGB(i222, ColorUtils.blendARGB(Theme.getColor(i322), Theme.getColor(this.currentColorKey), 0.9f), this.colorProgress));
        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.filledPaint);
        this.filledPaint.setColor(Theme.getColor(i322));
        this.filledPaint.setAlpha(NotificationCenter.liveLocationsChanged);
        this.rect.set(f422, measuredHeight22, measuredWidth22 - AndroidUtilities.dp(58.0f), getMeasuredHeight() - r4);
        this.rect.inset(-AndroidUtilities.dp(1.0f), -AndroidUtilities.dp(1.0f));
        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.filledPaint);
        this.outlinePaint.setAlpha(31);
        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.outlinePaint);
        canvas.save();
        canvas.clipRect(this.rect);
        this.filledPaint.setColor(Theme.getColor(i42));
        this.filledPaint.setAlpha(60);
        RectF rectF22 = this.rect;
        canvas.drawCircle(rectF22.left + 0.0f, rectF22.centerY(), AndroidUtilities.dp(15.0f), this.filledPaint);
        float centerY32 = this.rect.centerY() - AndroidUtilities.dp(6.0f);
        this.linePaint.setAlpha(57);
        canvas.drawLine(this.rect.left + AndroidUtilities.dp(23.0f) + 0.0f, centerY32, this.rect.right - AndroidUtilities.dp(68.0f), centerY32, this.linePaint);
        float centerY222 = this.rect.centerY() + AndroidUtilities.dp(6.0f);
        canvas.drawLine(this.rect.left + AndroidUtilities.dp(23.0f) + 0.0f, centerY222, this.rect.right - AndroidUtilities.dp(23.0f), centerY222, this.linePaint);
        canvas.restore();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (accessibilityEvent.getEventType() == 1) {
            int value = this.picker.getValue() + 1;
            if (value > this.picker.getMaxValue() || value < 0) {
                value = 0;
            }
            setContentDescription(this.strings[value]);
            this.picker.changeValueByOne(true);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setEnabled(true);
        accessibilityNodeInfo.setContentDescription(this.strings[this.picker.getValue()]);
        if (Build.VERSION.SDK_INT >= 21) {
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, null));
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(102.0f), 1073741824));
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        super.setBackgroundColor(i);
        updateColors();
        this.picker.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        this.picker.invalidate();
    }

    public void updateColors() {
        for (int i = 0; i < this.icons.length; i++) {
            updateIconColor(i);
        }
    }

    public void updateIconColor(int i) {
        if (this.icons[i] != null) {
            int blendARGB = ColorUtils.blendARGB(Theme.getColor(Theme.key_windowBackgroundWhite), Theme.getColor(Theme.key_chats_archiveBackground), 0.9f);
            int color = Theme.getColor(Theme.key_chats_archiveIcon);
            if (i != 2) {
                this.icons[i].setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
                return;
            }
            this.icons[i].setLayerColor("Arrow.**", blendARGB);
            this.icons[i].setLayerColor("Box2.**", color);
            this.icons[i].setLayerColor("Box1.**", color);
        }
    }
}
