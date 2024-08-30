package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import androidx.core.content.ContextCompat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class TimerDrawable extends Drawable {
    Context context;
    ColorFilter currentColorFilter;
    private Drawable currentTtlIcon;
    private int iconColor;
    private boolean isDialog;
    private boolean isStaticIcon;
    private boolean overrideColor;
    Theme.ResourcesProvider resourcesProvider;
    private StaticLayout timeLayout;
    private TextPaint timePaint = new TextPaint(1);
    private Paint paint = new Paint(1);
    private Paint linePaint = new Paint(1);
    private float timeWidth = 0.0f;
    private int timeHeight = 0;
    private int time = -1;

    public TimerDrawable(Context context, Theme.ResourcesProvider resourcesProvider) {
        this.context = context;
        this.resourcesProvider = resourcesProvider;
        this.timePaint.setTypeface(AndroidUtilities.getTypeface("fonts/rcondensedbold.ttf"));
        this.linePaint.setStrokeWidth(AndroidUtilities.dp(1.0f));
        this.linePaint.setStyle(Paint.Style.STROKE);
    }

    public static TimerDrawable getTtlIcon(int i) {
        TimerDrawable timerDrawable = new TimerDrawable(ApplicationLoader.applicationContext, null);
        timerDrawable.setTime(i);
        timerDrawable.isStaticIcon = true;
        return timerDrawable;
    }

    public static TimerDrawable getTtlIconForDialogs(int i) {
        TimerDrawable timerDrawable = new TimerDrawable(ApplicationLoader.applicationContext, null);
        timerDrawable.isDialog = true;
        timerDrawable.setTime(i);
        return timerDrawable;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        TextPaint textPaint;
        int i;
        float f;
        int intrinsicWidth = getIntrinsicWidth();
        int intrinsicHeight = getIntrinsicHeight();
        if (this.isDialog) {
            this.timePaint.setColor(-1);
        } else {
            if (this.isStaticIcon) {
                textPaint = this.timePaint;
                i = Theme.key_actionBarDefaultSubmenuItemIcon;
            } else {
                if (!this.overrideColor) {
                    this.paint.setColor(Theme.getColor(Theme.key_actionBarDefault, this.resourcesProvider));
                }
                textPaint = this.timePaint;
                i = Theme.key_actionBarDefaultTitle;
            }
            textPaint.setColor(Theme.getColor(i, this.resourcesProvider));
        }
        if (this.currentTtlIcon != null) {
            if (!this.isStaticIcon && !this.isDialog) {
                canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), getBounds().width() / 2.0f, this.paint);
                int color = Theme.getColor(Theme.key_actionBarDefaultTitle, this.resourcesProvider);
                if (this.iconColor != color) {
                    this.iconColor = color;
                    this.currentTtlIcon.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
                }
            }
            if (this.isDialog) {
                this.currentTtlIcon.setBounds(getBounds().left, getBounds().top, getBounds().left + this.currentTtlIcon.getIntrinsicWidth(), getBounds().top + this.currentTtlIcon.getIntrinsicHeight());
            } else {
                android.graphics.Rect rect = AndroidUtilities.rectTmp2;
                rect.set(getBounds().centerX() - AndroidUtilities.dp(10.5f), getBounds().centerY() - AndroidUtilities.dp(10.5f), (getBounds().centerX() - AndroidUtilities.dp(10.5f)) + this.currentTtlIcon.getIntrinsicWidth(), (getBounds().centerY() - AndroidUtilities.dp(10.5f)) + this.currentTtlIcon.getIntrinsicHeight());
                this.currentTtlIcon.setBounds(rect);
            }
            this.currentTtlIcon.draw(canvas);
        }
        if (this.time == 0 || this.timeLayout == null) {
            return;
        }
        int i2 = AndroidUtilities.density != 3.0f ? 0 : -1;
        if (this.isDialog) {
            double width = getBounds().width() / 2;
            double ceil = Math.ceil(this.timeWidth / 2.0f);
            Double.isNaN(width);
            double d = i2;
            Double.isNaN(d);
            f = (float) ((width - ceil) + d);
            intrinsicHeight = getBounds().height();
        } else {
            double d2 = intrinsicWidth / 2;
            double ceil2 = Math.ceil(this.timeWidth / 2.0f);
            Double.isNaN(d2);
            f = ((int) (d2 - ceil2)) + i2;
        }
        canvas.translate(f, (intrinsicHeight - this.timeHeight) / 2.0f);
        this.timeLayout.draw(canvas);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(23.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(23.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    public int getTime() {
        return this.time;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    public void setBackgroundColor(int i) {
        this.overrideColor = true;
        this.paint.setColor(i);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.currentColorFilter = colorFilter;
        if (this.isStaticIcon) {
            this.currentTtlIcon.setColorFilter(colorFilter);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:56:0x0191  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x01b0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setTime(int i) {
        String str;
        StringBuilder sb;
        int i2;
        String str2;
        float measureText;
        if (this.time != i) {
            this.time = i;
            if (this.isDialog) {
                this.currentTtlIcon = ContextCompat.getDrawable(this.context, R.drawable.msg_autodelete_badge2).mutate();
            } else {
                Drawable mutate = ContextCompat.getDrawable(this.context, i == 0 ? R.drawable.msg_mini_autodelete : R.drawable.msg_mini_autodelete_empty).mutate();
                this.currentTtlIcon = mutate;
                mutate.setColorFilter(this.currentColorFilter);
            }
            invalidateSelf();
            int i3 = this.time;
            if (i3 >= 1 && i3 < 60) {
                str = "" + i;
                if (str.length() < 2) {
                    sb = new StringBuilder();
                    sb.append(str);
                    i2 = R.string.SecretChatTimerSeconds;
                    sb.append(LocaleController.getString(i2));
                    str = sb.toString();
                }
                str2 = str;
                this.timePaint.setTextSize(AndroidUtilities.dp(11.0f));
                measureText = this.timePaint.measureText(str2);
                this.timeWidth = measureText;
                if (measureText > AndroidUtilities.dp(13.0f)) {
                }
                if (this.timeWidth > AndroidUtilities.dp(13.0f)) {
                }
                StaticLayout staticLayout = new StaticLayout(str2, this.timePaint, (int) Math.ceil(this.timeWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.timeLayout = staticLayout;
                this.timeHeight = staticLayout.getHeight();
                invalidateSelf();
            } else if (i3 >= 60 && i3 < 3600) {
                str = "" + (i / 60);
                if (str.length() < 2) {
                    sb = new StringBuilder();
                    sb.append(str);
                    i2 = R.string.SecretChatTimerMinutes;
                    sb.append(LocaleController.getString(i2));
                    str = sb.toString();
                }
                str2 = str;
                this.timePaint.setTextSize(AndroidUtilities.dp(11.0f));
                measureText = this.timePaint.measureText(str2);
                this.timeWidth = measureText;
                if (measureText > AndroidUtilities.dp(13.0f)) {
                }
                if (this.timeWidth > AndroidUtilities.dp(13.0f)) {
                }
                StaticLayout staticLayout2 = new StaticLayout(str2, this.timePaint, (int) Math.ceil(this.timeWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.timeLayout = staticLayout2;
                this.timeHeight = staticLayout2.getHeight();
                invalidateSelf();
            } else if (i3 >= 3600 && i3 < 86400) {
                str = "" + ((i / 60) / 60);
                if (str.length() < 2) {
                    sb = new StringBuilder();
                    sb.append(str);
                    i2 = R.string.SecretChatTimerHours;
                    sb.append(LocaleController.getString(i2));
                    str = sb.toString();
                }
                str2 = str;
                this.timePaint.setTextSize(AndroidUtilities.dp(11.0f));
                measureText = this.timePaint.measureText(str2);
                this.timeWidth = measureText;
                if (measureText > AndroidUtilities.dp(13.0f)) {
                }
                if (this.timeWidth > AndroidUtilities.dp(13.0f)) {
                }
                StaticLayout staticLayout22 = new StaticLayout(str2, this.timePaint, (int) Math.ceil(this.timeWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.timeLayout = staticLayout22;
                this.timeHeight = staticLayout22.getHeight();
                invalidateSelf();
            } else if (i3 >= 86400 && i3 < 604800) {
                str = "" + (((i / 60) / 60) / 24);
                if (str.length() < 2) {
                    sb = new StringBuilder();
                    sb.append(str);
                    i2 = R.string.SecretChatTimerDays;
                    sb.append(LocaleController.getString(i2));
                    str = sb.toString();
                }
                str2 = str;
                this.timePaint.setTextSize(AndroidUtilities.dp(11.0f));
                measureText = this.timePaint.measureText(str2);
                this.timeWidth = measureText;
                if (measureText > AndroidUtilities.dp(13.0f)) {
                }
                if (this.timeWidth > AndroidUtilities.dp(13.0f)) {
                }
                StaticLayout staticLayout222 = new StaticLayout(str2, this.timePaint, (int) Math.ceil(this.timeWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.timeLayout = staticLayout222;
                this.timeHeight = staticLayout222.getHeight();
                invalidateSelf();
            } else if (i3 < 2678400) {
                str = "" + ((((i / 60) / 60) / 24) / 7);
                if (str.length() >= 2) {
                    if (str.length() > 2) {
                        str = "c";
                    }
                    str2 = str;
                    this.timePaint.setTextSize(AndroidUtilities.dp(11.0f));
                    measureText = this.timePaint.measureText(str2);
                    this.timeWidth = measureText;
                    if (measureText > AndroidUtilities.dp(13.0f)) {
                    }
                    if (this.timeWidth > AndroidUtilities.dp(13.0f)) {
                    }
                    StaticLayout staticLayout2222 = new StaticLayout(str2, this.timePaint, (int) Math.ceil(this.timeWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    this.timeLayout = staticLayout2222;
                    this.timeHeight = staticLayout2222.getHeight();
                    invalidateSelf();
                }
                sb = new StringBuilder();
                sb.append(str);
                i2 = R.string.SecretChatTimerWeeks;
                sb.append(LocaleController.getString(i2));
                str = sb.toString();
                str2 = str;
                this.timePaint.setTextSize(AndroidUtilities.dp(11.0f));
                measureText = this.timePaint.measureText(str2);
                this.timeWidth = measureText;
                if (measureText > AndroidUtilities.dp(13.0f)) {
                    this.timePaint.setTextSize(AndroidUtilities.dp(9.0f));
                    this.timeWidth = this.timePaint.measureText(str2);
                }
                if (this.timeWidth > AndroidUtilities.dp(13.0f)) {
                    this.timePaint.setTextSize(AndroidUtilities.dp(6.0f));
                    this.timeWidth = this.timePaint.measureText(str2);
                }
                try {
                    StaticLayout staticLayout22222 = new StaticLayout(str2, this.timePaint, (int) Math.ceil(this.timeWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    this.timeLayout = staticLayout22222;
                    this.timeHeight = staticLayout22222.getHeight();
                } catch (Exception e) {
                    this.timeLayout = null;
                    FileLog.e(e);
                }
                invalidateSelf();
            } else if (i3 < 31449600) {
                str = "" + ((((i / 60) / 60) / 24) / 30);
                if (str.length() < 2) {
                    sb = new StringBuilder();
                    sb.append(str);
                    i2 = R.string.SecretChatTimerMonths;
                    sb.append(LocaleController.getString(i2));
                    str = sb.toString();
                }
                str2 = str;
                this.timePaint.setTextSize(AndroidUtilities.dp(11.0f));
                measureText = this.timePaint.measureText(str2);
                this.timeWidth = measureText;
                if (measureText > AndroidUtilities.dp(13.0f)) {
                }
                if (this.timeWidth > AndroidUtilities.dp(13.0f)) {
                }
                StaticLayout staticLayout222222 = new StaticLayout(str2, this.timePaint, (int) Math.ceil(this.timeWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.timeLayout = staticLayout222222;
                this.timeHeight = staticLayout222222.getHeight();
                invalidateSelf();
            } else {
                str = "" + ((((i / 60) / 60) / 24) / 364);
                if (str.length() < 2) {
                    sb = new StringBuilder();
                    sb.append(str);
                    i2 = R.string.SecretChatTimerYears;
                    sb.append(LocaleController.getString(i2));
                    str = sb.toString();
                }
                str2 = str;
                this.timePaint.setTextSize(AndroidUtilities.dp(11.0f));
                measureText = this.timePaint.measureText(str2);
                this.timeWidth = measureText;
                if (measureText > AndroidUtilities.dp(13.0f)) {
                }
                if (this.timeWidth > AndroidUtilities.dp(13.0f)) {
                }
                StaticLayout staticLayout2222222 = new StaticLayout(str2, this.timePaint, (int) Math.ceil(this.timeWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.timeLayout = staticLayout2222222;
                this.timeHeight = staticLayout2222222.getHeight();
                invalidateSelf();
            }
        }
    }
}
