package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.exoplayer2.C;
import java.io.File;
import java.io.FileInputStream;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.beta.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
/* loaded from: classes4.dex */
public class ThemeCell extends FrameLayout {
    private static byte[] bytes = new byte[1024];
    private ImageView checkImage;
    private Theme.ThemeInfo currentThemeInfo;
    private boolean isNightTheme;
    private boolean needDivider;
    private ImageView optionsButton;
    private Paint paint = new Paint(1);
    private Paint paintStroke;
    private TextView textView;

    public ThemeCell(Context context, boolean nightTheme) {
        super(context);
        setWillNotDraw(false);
        this.isNightTheme = nightTheme;
        Paint paint = new Paint(1);
        this.paintStroke = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.paintStroke.setStrokeWidth(AndroidUtilities.dp(2.0f));
        TextView textView = new TextView(context);
        this.textView = textView;
        textView.setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setPadding(0, 0, 0, AndroidUtilities.dp(1.0f));
        this.textView.setEllipsize(TextUtils.TruncateAt.END);
        int i = 5;
        this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        addView(this.textView, LayoutHelper.createFrame(-1, -1.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 105.0f : 60.0f, 0.0f, LocaleController.isRTL ? 60.0f : 105.0f, 0.0f));
        ImageView imageView = new ImageView(context);
        this.checkImage = imageView;
        imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_featuredStickers_addedIcon), PorterDuff.Mode.MULTIPLY));
        this.checkImage.setImageResource(R.drawable.sticker_added);
        if (!this.isNightTheme) {
            addView(this.checkImage, LayoutHelper.createFrame(19, 14.0f, (LocaleController.isRTL ? 3 : 5) | 16, 59.0f, 0.0f, 59.0f, 0.0f));
            ImageView imageView2 = new ImageView(context);
            this.optionsButton = imageView2;
            imageView2.setFocusable(false);
            this.optionsButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor(Theme.key_stickers_menuSelector)));
            this.optionsButton.setImageResource(R.drawable.ic_ab_other);
            this.optionsButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_stickers_menu), PorterDuff.Mode.MULTIPLY));
            this.optionsButton.setScaleType(ImageView.ScaleType.CENTER);
            this.optionsButton.setContentDescription(LocaleController.getString("AccDescrMoreOptions", R.string.AccDescrMoreOptions));
            addView(this.optionsButton, LayoutHelper.createFrame(48, 48, (LocaleController.isRTL ? 3 : i) | 48));
            return;
        }
        addView(this.checkImage, LayoutHelper.createFrame(19, 14.0f, (LocaleController.isRTL ? 3 : i) | 16, 21.0f, 0.0f, 21.0f, 0.0f));
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.checkImage.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_featuredStickers_addedIcon), PorterDuff.Mode.MULTIPLY));
        this.textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec), C.BUFFER_FLAG_ENCRYPTED), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f) + (this.needDivider ? 1 : 0), C.BUFFER_FLAG_ENCRYPTED));
    }

    public void setOnOptionsClick(View.OnClickListener listener) {
        this.optionsButton.setOnClickListener(listener);
    }

    public TextView getTextView() {
        return this.textView;
    }

    public void setTextColor(int color) {
        this.textView.setTextColor(color);
    }

    public Theme.ThemeInfo getCurrentThemeInfo() {
        return this.currentThemeInfo;
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x00b6, code lost:
        r0 = r0.substring(r0 + 1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00c2, code lost:
        if (r0.length() <= 0) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00c4, code lost:
        r2 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00d1, code lost:
        if (r2.charAt(0) != '#') goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00d3, code lost:
        r0 = android.graphics.Color.parseColor(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00db, code lost:
        r0 = org.telegram.messenger.Utilities.parseInt((java.lang.CharSequence) r2).intValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00e5, code lost:
        r2 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00eb, code lost:
        r0 = org.telegram.messenger.Utilities.parseInt((java.lang.CharSequence) r2).intValue();
     */
    /* JADX WARN: Removed duplicated region for block: B:105:0x013f A[EDGE_INSN: B:105:0x013f->B:60:0x013f ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:112:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x012c A[LOOP:0: B:16:0x0061->B:57:0x012c, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0152 A[Catch: Exception -> 0x0143, TRY_ENTER, TRY_LEAVE, TryCatch #5 {Exception -> 0x0143, blocks: (B:60:0x013f, B:67:0x0152), top: B:99:0x0053 }] */
    /* JADX WARN: Removed duplicated region for block: B:77:0x016b  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0178  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x017f  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0189  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setTheme(Theme.ThemeInfo themeInfo, boolean divider) {
        String text;
        FileInputStream stream;
        Throwable e;
        int previousPosition;
        int currentPosition;
        int value;
        String text2;
        int linesRead;
        int value2;
        this.currentThemeInfo = themeInfo;
        String text3 = themeInfo.getName();
        int i = 0;
        if (!text3.endsWith(".attheme")) {
            text = text3;
        } else {
            text = text3.substring(0, text3.lastIndexOf(46));
        }
        this.textView.setText(text);
        this.needDivider = divider;
        updateCurrentThemeCheck();
        boolean finished = false;
        Theme.ThemeAccent accent = themeInfo.getAccent(false);
        if (themeInfo.assetName != null) {
            Paint paint = this.paint;
            if (accent != null) {
                i = accent.accentColor;
            }
            paint.setColor(Theme.changeColorAccent(themeInfo, i, themeInfo.getPreviewBackgroundColor()));
            finished = true;
        } else if (themeInfo.pathToFile != null) {
            stream = null;
            int linesRead2 = 0;
            try {
                try {
                    File file = new File(themeInfo.pathToFile);
                    stream = new FileInputStream(file);
                    int currentPosition2 = 0;
                    while (true) {
                        int read = stream.read(bytes);
                        if (read == -1) {
                            break;
                        }
                        previousPosition = linesRead2;
                        int start = 0;
                        int a = 0;
                        int i2 = currentPosition2;
                        currentPosition = linesRead2;
                        value = i2;
                        while (true) {
                            if (a >= read) {
                                text2 = text;
                                break;
                            }
                            byte[] bArr = bytes;
                            text2 = text;
                            if (bArr[a] == 10) {
                                linesRead = value + 1;
                                int linesRead3 = a - start;
                                int len = linesRead3 + 1;
                                try {
                                    int linesRead4 = len - 1;
                                    String line = new String(bArr, start, linesRead4, "UTF-8");
                                    if (line.startsWith("WPS")) {
                                        value = linesRead;
                                        break;
                                    }
                                    int idx = line.indexOf(61);
                                    if (idx != -1) {
                                        String key = line.substring(0, idx);
                                        if (key.equals(Theme.key_actionBarDefault)) {
                                            break;
                                        }
                                    }
                                    start += len;
                                    currentPosition += len;
                                    value = linesRead;
                                } catch (Throwable th) {
                                    e = th;
                                    try {
                                        FileLog.e(e);
                                        if (stream != null) {
                                            stream.close();
                                        }
                                        if (!finished) {
                                        }
                                        this.paintStroke.setColor((accent == null ? Integer.valueOf(accent.accentColor) : null).intValue());
                                        if (accent == null) {
                                            return;
                                        }
                                        return;
                                    } catch (Throwable th2) {
                                        if (stream != null) {
                                            try {
                                                stream.close();
                                            } catch (Exception e2) {
                                                FileLog.e(e2);
                                            }
                                        }
                                        throw th2;
                                    }
                                }
                            }
                            a++;
                            text = text2;
                        }
                        if (previousPosition != currentPosition || value >= 500) {
                            break;
                            break;
                        }
                        stream.getChannel().position(currentPosition);
                        if (!finished) {
                            break;
                        }
                        text = text2;
                        int i3 = currentPosition;
                        currentPosition2 = value;
                        linesRead2 = i3;
                    }
                    stream.close();
                } catch (Throwable th3) {
                    e = th3;
                }
            } catch (Exception e3) {
                FileLog.e(e3);
            }
        }
        if (!finished) {
            this.paint.setColor(Theme.getDefaultColor(Theme.key_actionBarDefault));
        }
        this.paintStroke.setColor((accent == null ? Integer.valueOf(accent.accentColor) : null).intValue());
        if (accent == null && accent.accentColor != 0) {
            this.paintStroke.setAlpha(180);
            return;
        }
        return;
        try {
            this.paint.setColor(value2);
            finished = true;
            value = linesRead;
            if (previousPosition != currentPosition) {
                break;
            }
            stream.getChannel().position(currentPosition);
            if (!finished) {
            }
            stream.close();
        } catch (Throwable th4) {
            e = th4;
            finished = true;
            FileLog.e(e);
            if (stream != null) {
            }
            if (!finished) {
            }
            this.paintStroke.setColor((accent == null ? Integer.valueOf(accent.accentColor) : null).intValue());
            if (accent == null) {
            }
        }
        if (!finished) {
        }
        this.paintStroke.setColor((accent == null ? Integer.valueOf(accent.accentColor) : null).intValue());
        if (accent == null) {
        }
    }

    public void updateCurrentThemeCheck() {
        Theme.ThemeInfo currentTheme;
        if (this.isNightTheme) {
            currentTheme = Theme.getCurrentNightTheme();
        } else {
            currentTheme = Theme.getCurrentTheme();
        }
        int newVisibility = this.currentThemeInfo == currentTheme ? 0 : 4;
        if (this.checkImage.getVisibility() != newVisibility) {
            this.checkImage.setVisibility(newVisibility);
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.needDivider) {
            canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
        }
        int x = AndroidUtilities.dp(31.0f);
        if (LocaleController.isRTL) {
            x = getWidth() - x;
        }
        canvas.drawCircle(x, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(11.0f), this.paint);
        canvas.drawCircle(x, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(10.0f), this.paintStroke);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        setSelected(this.checkImage.getVisibility() == 0);
    }
}
