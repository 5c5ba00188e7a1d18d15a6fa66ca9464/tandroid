package org.telegram.ui.Cells;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.EmojiThemes;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeColors;
import org.telegram.ui.Cells.ThemesHorizontalListCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MotionBackgroundDrawable;
import org.telegram.ui.Components.RadioButton;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.ThemeSetUrlActivity;
/* loaded from: classes4.dex */
public abstract class ThemesHorizontalListCell extends RecyclerListView implements NotificationCenter.NotificationCenterDelegate {
    public static byte[] bytes = new byte[1024];
    private ThemesListAdapter adapter;
    private int currentType;
    private ArrayList customThemes;
    private ArrayList defaultThemes;
    private boolean drawDivider;
    private BaseFragment fragment;
    private LinearLayoutManager horizontalLayoutManager;
    private HashMap loadingThemes;
    private HashMap loadingWallpapers;
    private int prevCount;
    private Theme.ThemeInfo prevThemeInfo;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class InnerThemeView extends FrameLayout {
        private ObjectAnimator accentAnimator;
        private boolean accentColorChanged;
        private int accentId;
        private float accentState;
        private int backColor;
        private Drawable backgroundDrawable;
        private Paint bitmapPaint;
        private BitmapShader bitmapShader;
        private RadioButton button;
        private int checkColor;
        private final ArgbEvaluator evaluator;
        private boolean hasWhiteBackground;
        private int inColor;
        private Drawable inDrawable;
        private boolean isFirst;
        private boolean isLast;
        private long lastDrawTime;
        private int loadingColor;
        private Drawable loadingDrawable;
        private int oldBackColor;
        private int oldCheckColor;
        private int oldInColor;
        private int oldOutColor;
        private Drawable optionsDrawable;
        private int outColor;
        private Drawable outDrawable;
        private Paint paint;
        private float placeholderAlpha;
        private boolean pressed;
        private RectF rect;
        private Matrix shaderMatrix;
        private TextPaint textPaint;
        private Theme.ThemeInfo themeInfo;

        public InnerThemeView(Context context) {
            super(context);
            this.rect = new RectF();
            this.paint = new Paint(1);
            this.textPaint = new TextPaint(1);
            this.evaluator = new ArgbEvaluator();
            this.bitmapPaint = new Paint(3);
            this.shaderMatrix = new Matrix();
            setWillNotDraw(false);
            this.inDrawable = context.getResources().getDrawable(R.drawable.minibubble_in).mutate();
            this.outDrawable = context.getResources().getDrawable(R.drawable.minibubble_out).mutate();
            this.textPaint.setTextSize(AndroidUtilities.dp(13.0f));
            RadioButton radioButton = new RadioButton(context);
            this.button = radioButton;
            radioButton.setSize(AndroidUtilities.dp(20.0f));
            addView(this.button, LayoutHelper.createFrame(22, 22.0f, 51, 27.0f, 75.0f, 0.0f, 0.0f));
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x00c9, code lost:
            if (r0.getPreviewBackgroundColor() != 0) goto L10;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:41:0x0158  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void applyTheme() {
            Drawable createDefaultWallpaper;
            GradientDrawable gradientDrawable;
            Drawable drawable = this.inDrawable;
            int previewInColor = this.themeInfo.getPreviewInColor();
            PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
            drawable.setColorFilter(new PorterDuffColorFilter(previewInColor, mode));
            this.outDrawable.setColorFilter(new PorterDuffColorFilter(this.themeInfo.getPreviewOutColor(), mode));
            double[] dArr = null;
            if (this.themeInfo.pathToFile == null) {
                updateColors(false);
                this.optionsDrawable = null;
            } else {
                this.optionsDrawable = getResources().getDrawable(R.drawable.preview_dots).mutate();
                int previewBackgroundColor = this.themeInfo.getPreviewBackgroundColor();
                this.backColor = previewBackgroundColor;
                this.oldBackColor = previewBackgroundColor;
            }
            this.bitmapShader = null;
            this.backgroundDrawable = null;
            Theme.ThemeInfo themeInfo = this.themeInfo;
            int i = themeInfo.previewBackgroundGradientColor1;
            if (i != 0 && themeInfo.previewBackgroundGradientColor2 != 0) {
                int previewBackgroundColor2 = this.themeInfo.getPreviewBackgroundColor();
                Theme.ThemeInfo themeInfo2 = this.themeInfo;
                MotionBackgroundDrawable motionBackgroundDrawable = new MotionBackgroundDrawable(previewBackgroundColor2, themeInfo2.previewBackgroundGradientColor1, themeInfo2.previewBackgroundGradientColor2, themeInfo2.previewBackgroundGradientColor3, true);
                motionBackgroundDrawable.setRoundRadius(AndroidUtilities.dp(6.0f));
                gradientDrawable = motionBackgroundDrawable;
            } else if (i == 0) {
                if (themeInfo.previewWallpaperOffset > 0 || themeInfo.pathToWallpaper != null) {
                    Theme.ThemeInfo themeInfo3 = this.themeInfo;
                    Bitmap scaledBitmap = AndroidUtilities.getScaledBitmap(AndroidUtilities.dp(76.0f), AndroidUtilities.dp(97.0f), themeInfo3.pathToWallpaper, themeInfo3.pathToFile, themeInfo3.previewWallpaperOffset);
                    if (scaledBitmap != null) {
                        this.backgroundDrawable = new BitmapDrawable(scaledBitmap);
                        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                        BitmapShader bitmapShader = new BitmapShader(scaledBitmap, tileMode, tileMode);
                        this.bitmapShader = bitmapShader;
                        this.bitmapPaint.setShader(bitmapShader);
                        int[] calcDrawableColor = AndroidUtilities.calcDrawableColor(this.backgroundDrawable);
                        dArr = AndroidUtilities.rgbToHsv(Color.red(calcDrawableColor[0]), Color.green(calcDrawableColor[0]), Color.blue(calcDrawableColor[0]));
                    }
                }
                if (dArr != null || dArr[1] > 0.10000000149011612d || dArr[2] < 0.9599999785423279d) {
                    this.hasWhiteBackground = false;
                } else {
                    this.hasWhiteBackground = true;
                }
                if (this.themeInfo.getPreviewBackgroundColor() == 0 && this.themeInfo.previewParsed && this.backgroundDrawable == null) {
                    createDefaultWallpaper = Theme.createDefaultWallpaper(100, NotificationCenter.storyQualityUpdate);
                    this.backgroundDrawable = createDefaultWallpaper;
                    if (createDefaultWallpaper instanceof MotionBackgroundDrawable) {
                        ((MotionBackgroundDrawable) createDefaultWallpaper).setRoundRadius(AndroidUtilities.dp(6.0f));
                    }
                }
                invalidate();
            } else {
                GradientDrawable gradientDrawable2 = new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]{this.themeInfo.getPreviewBackgroundColor(), this.themeInfo.previewBackgroundGradientColor1});
                gradientDrawable2.setCornerRadius(AndroidUtilities.dp(6.0f));
                gradientDrawable = gradientDrawable2;
            }
            this.backgroundDrawable = gradientDrawable;
            dArr = AndroidUtilities.rgbToHsv(Color.red(this.themeInfo.getPreviewBackgroundColor()), Color.green(this.themeInfo.getPreviewBackgroundColor()), Color.blue(this.themeInfo.getPreviewBackgroundColor()));
            if (dArr != null) {
            }
            this.hasWhiteBackground = false;
            if (this.themeInfo.getPreviewBackgroundColor() == 0) {
                createDefaultWallpaper = Theme.createDefaultWallpaper(100, NotificationCenter.storyQualityUpdate);
                this.backgroundDrawable = createDefaultWallpaper;
                if (createDefaultWallpaper instanceof MotionBackgroundDrawable) {
                }
            }
            invalidate();
        }

        private int blend(int i, int i2) {
            float f = this.accentState;
            return f == 1.0f ? i2 : ((Integer) this.evaluator.evaluate(f, Integer.valueOf(i), Integer.valueOf(i2))).intValue();
        }

        private String getThemeName() {
            String name = this.themeInfo.getName();
            return name.toLowerCase().endsWith(".attheme") ? name.substring(0, name.lastIndexOf(46)) : name;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$parseTheme$0(TLObject tLObject) {
            if (!(tLObject instanceof TLRPC.TL_wallPaper)) {
                this.themeInfo.badWallpaper = true;
                return;
            }
            TLRPC.WallPaper wallPaper = (TLRPC.WallPaper) tLObject;
            String attachFileName = FileLoader.getAttachFileName(wallPaper.document);
            if (ThemesHorizontalListCell.this.loadingThemes.containsKey(attachFileName)) {
                return;
            }
            ThemesHorizontalListCell.this.loadingThemes.put(attachFileName, this.themeInfo);
            FileLoader.getInstance(this.themeInfo.account).loadFile(wallPaper.document, wallPaper, 1, 1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$parseTheme$1(final TLObject tLObject, TLRPC.TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ThemesHorizontalListCell$InnerThemeView$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ThemesHorizontalListCell.InnerThemeView.this.lambda$parseTheme$0(tLObject);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:89:0x01df A[Catch: all -> 0x00aa, TryCatch #4 {all -> 0x022d, blocks: (B:8:0x0017, B:112:0x0229, B:10:0x001e, B:14:0x002c, B:16:0x0034, B:18:0x0047, B:20:0x0088, B:22:0x0094, B:25:0x0098, B:27:0x009b, B:29:0x00a5, B:32:0x00ae, B:33:0x00b1, B:35:0x00bd, B:37:0x00c9, B:39:0x00e5, B:41:0x00ef, B:42:0x00fe, B:44:0x0106, B:46:0x0110, B:47:0x0120, B:49:0x0128, B:51:0x0132, B:105:0x0213, B:52:0x0142, B:54:0x014e, B:55:0x015a, B:57:0x0166, B:58:0x0172, B:60:0x0178, B:62:0x017f, B:64:0x0187, B:65:0x018f, B:67:0x0198, B:69:0x01a5, B:71:0x01a9, B:73:0x01ad, B:75:0x01b1, B:77:0x01b5, B:79:0x01b9, B:81:0x01c5, B:84:0x01ce, B:87:0x01db, B:89:0x01df, B:90:0x01e5, B:92:0x01e9, B:93:0x01ef, B:95:0x01f3, B:96:0x01f9, B:98:0x01fd, B:99:0x0202, B:101:0x0206, B:102:0x020b, B:104:0x020f, B:86:0x01d3, B:106:0x0215, B:110:0x021e), top: B:144:0x0017 }] */
        /* JADX WARN: Removed duplicated region for block: B:90:0x01e5 A[Catch: all -> 0x00aa, TryCatch #4 {all -> 0x022d, blocks: (B:8:0x0017, B:112:0x0229, B:10:0x001e, B:14:0x002c, B:16:0x0034, B:18:0x0047, B:20:0x0088, B:22:0x0094, B:25:0x0098, B:27:0x009b, B:29:0x00a5, B:32:0x00ae, B:33:0x00b1, B:35:0x00bd, B:37:0x00c9, B:39:0x00e5, B:41:0x00ef, B:42:0x00fe, B:44:0x0106, B:46:0x0110, B:47:0x0120, B:49:0x0128, B:51:0x0132, B:105:0x0213, B:52:0x0142, B:54:0x014e, B:55:0x015a, B:57:0x0166, B:58:0x0172, B:60:0x0178, B:62:0x017f, B:64:0x0187, B:65:0x018f, B:67:0x0198, B:69:0x01a5, B:71:0x01a9, B:73:0x01ad, B:75:0x01b1, B:77:0x01b5, B:79:0x01b9, B:81:0x01c5, B:84:0x01ce, B:87:0x01db, B:89:0x01df, B:90:0x01e5, B:92:0x01e9, B:93:0x01ef, B:95:0x01f3, B:96:0x01f9, B:98:0x01fd, B:99:0x0202, B:101:0x0206, B:102:0x020b, B:104:0x020f, B:86:0x01d3, B:106:0x0215, B:110:0x021e), top: B:144:0x0017 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean parseTheme() {
            int stringKeyToInt;
            int intValue;
            String[] split;
            Theme.ThemeInfo themeInfo = this.themeInfo;
            if (themeInfo == null || themeInfo.pathToFile == null) {
                return false;
            }
            try {
                FileInputStream fileInputStream = new FileInputStream(new File(this.themeInfo.pathToFile));
                int i = 0;
                boolean z = false;
                while (true) {
                    int read = fileInputStream.read(ThemesHorizontalListCell.bytes);
                    if (read == -1) {
                        break;
                    }
                    int i2 = i;
                    int i3 = 0;
                    int i4 = 0;
                    while (true) {
                        if (i3 >= read) {
                            break;
                        }
                        byte[] bArr = ThemesHorizontalListCell.bytes;
                        if (bArr[i3] == 10) {
                            int i5 = i3 - i4;
                            int i6 = i5 + 1;
                            String str = new String(bArr, i4, i5, "UTF-8");
                            if (str.startsWith("WLS=")) {
                                String substring = str.substring(4);
                                Uri parse = Uri.parse(substring);
                                this.themeInfo.slug = parse.getQueryParameter("slug");
                                Theme.ThemeInfo themeInfo2 = this.themeInfo;
                                File filesDirFixed = ApplicationLoader.getFilesDirFixed();
                                themeInfo2.pathToWallpaper = new File(filesDirFixed, Utilities.MD5(substring) + ".wp").getAbsolutePath();
                                String queryParameter = parse.getQueryParameter("mode");
                                if (queryParameter != null && (split = queryParameter.toLowerCase().split(" ")) != null && split.length > 0) {
                                    int i7 = 0;
                                    while (true) {
                                        if (i7 >= split.length) {
                                            break;
                                        } else if ("blur".equals(split[i7])) {
                                            this.themeInfo.isBlured = true;
                                            break;
                                        } else {
                                            i7++;
                                        }
                                    }
                                }
                                if (!TextUtils.isEmpty(parse.getQueryParameter("pattern"))) {
                                    try {
                                        String queryParameter2 = parse.getQueryParameter("bg_color");
                                        if (!TextUtils.isEmpty(queryParameter2)) {
                                            this.themeInfo.patternBgColor = Integer.parseInt(queryParameter2.substring(0, 6), 16) | (-16777216);
                                            if (queryParameter2.length() >= 13 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(6))) {
                                                this.themeInfo.patternBgGradientColor1 = Integer.parseInt(queryParameter2.substring(7, 13), 16) | (-16777216);
                                            }
                                            if (queryParameter2.length() >= 20 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(13))) {
                                                this.themeInfo.patternBgGradientColor2 = Integer.parseInt(queryParameter2.substring(14, 20), 16) | (-16777216);
                                            }
                                            if (queryParameter2.length() == 27 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(20))) {
                                                this.themeInfo.patternBgGradientColor3 = Integer.parseInt(queryParameter2.substring(21), 16) | (-16777216);
                                            }
                                        }
                                    } catch (Exception unused) {
                                    }
                                    try {
                                        String queryParameter3 = parse.getQueryParameter("rotation");
                                        if (!TextUtils.isEmpty(queryParameter3)) {
                                            this.themeInfo.patternBgGradientRotation = Utilities.parseInt((CharSequence) queryParameter3).intValue();
                                        }
                                    } catch (Exception unused2) {
                                    }
                                    String queryParameter4 = parse.getQueryParameter("intensity");
                                    if (!TextUtils.isEmpty(queryParameter4)) {
                                        this.themeInfo.patternIntensity = Utilities.parseInt((CharSequence) queryParameter4).intValue();
                                    }
                                    Theme.ThemeInfo themeInfo3 = this.themeInfo;
                                    if (themeInfo3.patternIntensity == 0) {
                                        themeInfo3.patternIntensity = 50;
                                    }
                                }
                            } else if (str.startsWith("WPS")) {
                                this.themeInfo.previewWallpaperOffset = i6 + i2;
                                z = true;
                                break;
                            } else {
                                int indexOf = str.indexOf(61);
                                if (indexOf != -1 && ((stringKeyToInt = ThemeColors.stringKeyToInt(str.substring(0, indexOf))) == Theme.key_chat_inBubble || stringKeyToInt == Theme.key_chat_outBubble || stringKeyToInt == Theme.key_chat_wallpaper || stringKeyToInt == Theme.key_chat_wallpaper_gradient_to1 || stringKeyToInt == Theme.key_chat_wallpaper_gradient_to2 || stringKeyToInt == Theme.key_chat_wallpaper_gradient_to3)) {
                                    String substring2 = str.substring(indexOf + 1);
                                    if (substring2.length() > 0 && substring2.charAt(0) == '#') {
                                        try {
                                            intValue = Color.parseColor(substring2);
                                        } catch (Exception unused3) {
                                        }
                                        if (stringKeyToInt != Theme.key_chat_inBubble) {
                                            this.themeInfo.setPreviewInColor(intValue);
                                        } else if (stringKeyToInt == Theme.key_chat_outBubble) {
                                            this.themeInfo.setPreviewOutColor(intValue);
                                        } else if (stringKeyToInt == Theme.key_chat_wallpaper) {
                                            this.themeInfo.setPreviewBackgroundColor(intValue);
                                        } else if (stringKeyToInt == Theme.key_chat_wallpaper_gradient_to1) {
                                            this.themeInfo.previewBackgroundGradientColor1 = intValue;
                                        } else if (stringKeyToInt == Theme.key_chat_wallpaper_gradient_to2) {
                                            this.themeInfo.previewBackgroundGradientColor2 = intValue;
                                        } else if (stringKeyToInt == Theme.key_chat_wallpaper_gradient_to3) {
                                            this.themeInfo.previewBackgroundGradientColor3 = intValue;
                                        }
                                    }
                                    intValue = Utilities.parseInt((CharSequence) substring2).intValue();
                                    if (stringKeyToInt != Theme.key_chat_inBubble) {
                                    }
                                }
                            }
                            i4 += i6;
                            i2 += i6;
                        }
                        i3++;
                    }
                    if (z || i == i2) {
                        break;
                    }
                    fileInputStream.getChannel().position(i2);
                    i = i2;
                }
                fileInputStream.close();
            } catch (Throwable th) {
                FileLog.e(th);
            }
            Theme.ThemeInfo themeInfo4 = this.themeInfo;
            if (themeInfo4.pathToWallpaper == null || themeInfo4.badWallpaper || new File(this.themeInfo.pathToWallpaper).exists()) {
                this.themeInfo.previewParsed = true;
                return true;
            } else if (ThemesHorizontalListCell.this.loadingWallpapers.containsKey(this.themeInfo)) {
                return false;
            } else {
                HashMap hashMap = ThemesHorizontalListCell.this.loadingWallpapers;
                Theme.ThemeInfo themeInfo5 = this.themeInfo;
                hashMap.put(themeInfo5, themeInfo5.slug);
                TLRPC.TL_account_getWallPaper tL_account_getWallPaper = new TLRPC.TL_account_getWallPaper();
                TLRPC.TL_inputWallPaperSlug tL_inputWallPaperSlug = new TLRPC.TL_inputWallPaperSlug();
                Theme.ThemeInfo themeInfo6 = this.themeInfo;
                tL_inputWallPaperSlug.slug = themeInfo6.slug;
                tL_account_getWallPaper.wallpaper = tL_inputWallPaperSlug;
                ConnectionsManager.getInstance(themeInfo6.account).sendRequest(tL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.Cells.ThemesHorizontalListCell$InnerThemeView$$ExternalSyntheticLambda1
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        ThemesHorizontalListCell.InnerThemeView.this.lambda$parseTheme$1(tLObject, tL_error);
                    }
                });
                return false;
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            TLRPC.TL_theme tL_theme;
            super.onAttachedToWindow();
            this.button.setChecked(this.themeInfo == (ThemesHorizontalListCell.this.currentType == 1 ? Theme.getCurrentNightTheme() : Theme.getCurrentTheme()), false);
            Theme.ThemeInfo themeInfo = this.themeInfo;
            if (themeInfo == null || (tL_theme = themeInfo.info) == null || themeInfo.themeLoaded) {
                return;
            }
            if (ThemesHorizontalListCell.this.loadingThemes.containsKey(FileLoader.getAttachFileName(tL_theme.document)) || ThemesHorizontalListCell.this.loadingWallpapers.containsKey(this.themeInfo)) {
                return;
            }
            this.themeInfo.themeLoaded = true;
            this.placeholderAlpha = 0.0f;
            parseTheme();
            applyTheme();
        }

        /* JADX WARN: Removed duplicated region for block: B:40:0x0198  */
        /* JADX WARN: Removed duplicated region for block: B:46:0x01d2  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onDraw(Canvas canvas) {
            int dp;
            RectF rectF;
            float dp2;
            float dp3;
            Paint paint;
            Theme.ThemeInfo themeInfo;
            if (this.accentId != this.themeInfo.currentAccentId) {
                updateColors(true);
            }
            int dp4 = this.isFirst ? AndroidUtilities.dp(22.0f) : 0;
            float f = dp4;
            float dp5 = AndroidUtilities.dp(11.0f);
            this.rect.set(f, dp5, AndroidUtilities.dp(76.0f) + dp4, dp + AndroidUtilities.dp(97.0f));
            String charSequence = TextUtils.ellipsize(getThemeName(), this.textPaint, (getMeasuredWidth() - AndroidUtilities.dp(this.isFirst ? 10.0f : 15.0f)) - (this.isLast ? AndroidUtilities.dp(7.0f) : 0), TextUtils.TruncateAt.END).toString();
            int ceil = (int) Math.ceil(this.textPaint.measureText(charSequence));
            this.textPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            canvas.drawText(charSequence, ((AndroidUtilities.dp(76.0f) - ceil) / 2) + dp4, AndroidUtilities.dp(131.0f), this.textPaint);
            Theme.ThemeInfo themeInfo2 = this.themeInfo;
            TLRPC.TL_theme tL_theme = themeInfo2.info;
            if (tL_theme == null || (tL_theme.document != null && themeInfo2.themeLoaded)) {
                this.paint.setColor(blend(this.oldBackColor, this.backColor));
                if (this.accentColorChanged) {
                    Drawable drawable = this.inDrawable;
                    int blend = blend(this.oldInColor, this.inColor);
                    PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
                    drawable.setColorFilter(new PorterDuffColorFilter(blend, mode));
                    this.outDrawable.setColorFilter(new PorterDuffColorFilter(blend(this.oldOutColor, this.outColor), mode));
                    this.accentColorChanged = false;
                }
                Drawable drawable2 = this.backgroundDrawable;
                if (drawable2 == null) {
                    rectF = this.rect;
                    dp2 = AndroidUtilities.dp(6.0f);
                    dp3 = AndroidUtilities.dp(6.0f);
                    paint = this.paint;
                } else if (this.bitmapShader != null) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable2;
                    float width = bitmapDrawable.getBitmap().getWidth();
                    float height = bitmapDrawable.getBitmap().getHeight();
                    float width2 = width / this.rect.width();
                    float height2 = height / this.rect.height();
                    this.shaderMatrix.reset();
                    float min = 1.0f / Math.min(width2, height2);
                    float f2 = width / height2;
                    if (f2 > this.rect.width()) {
                        this.shaderMatrix.setTranslate(f - ((f2 - this.rect.width()) / 2.0f), dp5);
                    } else {
                        this.shaderMatrix.setTranslate(f, dp5 - (((height / width2) - this.rect.height()) / 2.0f));
                    }
                    this.shaderMatrix.preScale(min, min);
                    this.bitmapShader.setLocalMatrix(this.shaderMatrix);
                    rectF = this.rect;
                    dp2 = AndroidUtilities.dp(6.0f);
                    dp3 = AndroidUtilities.dp(6.0f);
                    paint = this.bitmapPaint;
                } else {
                    RectF rectF2 = this.rect;
                    drawable2.setBounds((int) rectF2.left, (int) rectF2.top, (int) rectF2.right, (int) rectF2.bottom);
                    this.backgroundDrawable.draw(canvas);
                    this.button.setColor(1728053247, -1);
                    themeInfo = this.themeInfo;
                    if (themeInfo.accentBaseColor == 0) {
                        if ("Day".equals(themeInfo.name) || "Arctic Blue".equals(this.themeInfo.name)) {
                            this.button.setColor(-5000269, blend(this.oldCheckColor, this.checkColor));
                            Theme.chat_instantViewRectPaint.setColor(733001146);
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), Theme.chat_instantViewRectPaint);
                        }
                        this.inDrawable.setBounds(AndroidUtilities.dp(6.0f) + dp4, AndroidUtilities.dp(22.0f), AndroidUtilities.dp(49.0f) + dp4, AndroidUtilities.dp(36.0f));
                        this.inDrawable.draw(canvas);
                        this.outDrawable.setBounds(AndroidUtilities.dp(27.0f) + dp4, AndroidUtilities.dp(41.0f), dp4 + AndroidUtilities.dp(70.0f), AndroidUtilities.dp(55.0f));
                        this.outDrawable.draw(canvas);
                        if (this.optionsDrawable != null && ThemesHorizontalListCell.this.currentType == 0) {
                            int dp6 = ((int) this.rect.right) - AndroidUtilities.dp(16.0f);
                            int dp7 = ((int) this.rect.top) + AndroidUtilities.dp(6.0f);
                            Drawable drawable3 = this.optionsDrawable;
                            drawable3.setBounds(dp6, dp7, drawable3.getIntrinsicWidth() + dp6, this.optionsDrawable.getIntrinsicHeight() + dp7);
                            this.optionsDrawable.draw(canvas);
                        }
                    } else {
                        if (this.hasWhiteBackground) {
                            this.button.setColor(-5000269, themeInfo.getPreviewOutColor());
                            Theme.chat_instantViewRectPaint.setColor(733001146);
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), Theme.chat_instantViewRectPaint);
                        }
                        this.inDrawable.setBounds(AndroidUtilities.dp(6.0f) + dp4, AndroidUtilities.dp(22.0f), AndroidUtilities.dp(49.0f) + dp4, AndroidUtilities.dp(36.0f));
                        this.inDrawable.draw(canvas);
                        this.outDrawable.setBounds(AndroidUtilities.dp(27.0f) + dp4, AndroidUtilities.dp(41.0f), dp4 + AndroidUtilities.dp(70.0f), AndroidUtilities.dp(55.0f));
                        this.outDrawable.draw(canvas);
                        if (this.optionsDrawable != null) {
                            int dp62 = ((int) this.rect.right) - AndroidUtilities.dp(16.0f);
                            int dp72 = ((int) this.rect.top) + AndroidUtilities.dp(6.0f);
                            Drawable drawable32 = this.optionsDrawable;
                            drawable32.setBounds(dp62, dp72, drawable32.getIntrinsicWidth() + dp62, this.optionsDrawable.getIntrinsicHeight() + dp72);
                            this.optionsDrawable.draw(canvas);
                        }
                    }
                }
                canvas.drawRoundRect(rectF, dp2, dp3, paint);
                this.button.setColor(1728053247, -1);
                themeInfo = this.themeInfo;
                if (themeInfo.accentBaseColor == 0) {
                }
            }
            Theme.ThemeInfo themeInfo3 = this.themeInfo;
            TLRPC.TL_theme tL_theme2 = themeInfo3.info;
            if (tL_theme2 != null && tL_theme2.document == null) {
                this.button.setAlpha(0.0f);
                Theme.chat_instantViewRectPaint.setColor(733001146);
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), Theme.chat_instantViewRectPaint);
                if (this.loadingDrawable != null) {
                    int color = Theme.getColor(Theme.key_windowBackgroundWhiteGrayText7);
                    if (this.loadingColor != color) {
                        Drawable drawable4 = this.loadingDrawable;
                        this.loadingColor = color;
                        Theme.setDrawableColor(drawable4, color);
                    }
                    int centerX = (int) (this.rect.centerX() - (this.loadingDrawable.getIntrinsicWidth() / 2));
                    int centerY = (int) (this.rect.centerY() - (this.loadingDrawable.getIntrinsicHeight() / 2));
                    Drawable drawable5 = this.loadingDrawable;
                    drawable5.setBounds(centerX, centerY, drawable5.getIntrinsicWidth() + centerX, this.loadingDrawable.getIntrinsicHeight() + centerY);
                    this.loadingDrawable.draw(canvas);
                }
            } else if ((tL_theme2 == null || themeInfo3.themeLoaded) && this.placeholderAlpha <= 0.0f) {
                if (this.button.getAlpha() != 1.0f) {
                    this.button.setAlpha(1.0f);
                }
            } else {
                this.button.setAlpha(1.0f - this.placeholderAlpha);
                this.paint.setColor(Theme.getColor(Theme.key_windowBackgroundGray));
                this.paint.setAlpha((int) (this.placeholderAlpha * 255.0f));
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), this.paint);
                if (this.loadingDrawable != null) {
                    int color2 = Theme.getColor(Theme.key_windowBackgroundWhiteGrayText7);
                    if (this.loadingColor != color2) {
                        Drawable drawable6 = this.loadingDrawable;
                        this.loadingColor = color2;
                        Theme.setDrawableColor(drawable6, color2);
                    }
                    int centerX2 = (int) (this.rect.centerX() - (this.loadingDrawable.getIntrinsicWidth() / 2));
                    int centerY2 = (int) (this.rect.centerY() - (this.loadingDrawable.getIntrinsicHeight() / 2));
                    this.loadingDrawable.setAlpha((int) (this.placeholderAlpha * 255.0f));
                    Drawable drawable7 = this.loadingDrawable;
                    drawable7.setBounds(centerX2, centerY2, drawable7.getIntrinsicWidth() + centerX2, this.loadingDrawable.getIntrinsicHeight() + centerY2);
                    this.loadingDrawable.draw(canvas);
                }
                if (this.themeInfo.themeLoaded) {
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    long min2 = Math.min(17L, elapsedRealtime - this.lastDrawTime);
                    this.lastDrawTime = elapsedRealtime;
                    float f3 = this.placeholderAlpha - (((float) min2) / 180.0f);
                    this.placeholderAlpha = f3;
                    if (f3 < 0.0f) {
                        this.placeholderAlpha = 0.0f;
                    }
                    invalidate();
                }
            }
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            AccessibilityNodeInfo.AccessibilityAction accessibilityAction;
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setText(getThemeName());
            accessibilityNodeInfo.setClassName(Button.class.getName());
            accessibilityNodeInfo.setChecked(this.button.isChecked());
            accessibilityNodeInfo.setCheckable(true);
            accessibilityNodeInfo.setEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                accessibilityAction = AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK;
                accessibilityNodeInfo.addAction(accessibilityAction);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(32, LocaleController.getString(R.string.AccDescrMoreOptions)));
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((this.isLast ? 22 : 15) + 76 + (this.isFirst ? 22 : 0)), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(148.0f), 1073741824));
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            Theme.ThemeInfo themeInfo;
            if (this.optionsDrawable == null || (themeInfo = this.themeInfo) == null || !((themeInfo.info == null || themeInfo.themeLoaded) && ThemesHorizontalListCell.this.currentType == 0)) {
                return super.onTouchEvent(motionEvent);
            }
            int action = motionEvent.getAction();
            if (action == 0 || action == 1) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                if (x > this.rect.centerX() && y < this.rect.centerY() - AndroidUtilities.dp(10.0f)) {
                    if (action == 0) {
                        this.pressed = true;
                    } else {
                        performHapticFeedback(3);
                        ThemesHorizontalListCell.this.showOptionsForTheme(this.themeInfo);
                    }
                }
                if (action == 1) {
                    this.pressed = false;
                }
            }
            return this.pressed;
        }

        public void setAccentState(float f) {
            this.accentState = f;
            this.accentColorChanged = true;
            invalidate();
        }

        public void setTheme(Theme.ThemeInfo themeInfo, boolean z, boolean z2) {
            Theme.ThemeInfo themeInfo2;
            TLRPC.TL_theme tL_theme;
            this.themeInfo = themeInfo;
            this.isFirst = z2;
            this.isLast = z;
            this.accentId = themeInfo.currentAccentId;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.button.getLayoutParams();
            layoutParams.leftMargin = AndroidUtilities.dp(this.isFirst ? 49.0f : 27.0f);
            this.button.setLayoutParams(layoutParams);
            this.placeholderAlpha = 0.0f;
            Theme.ThemeInfo themeInfo3 = this.themeInfo;
            if (themeInfo3.pathToFile != null && !themeInfo3.previewParsed) {
                themeInfo3.setPreviewInColor(Theme.getDefaultColor(Theme.key_chat_inBubble));
                this.themeInfo.setPreviewOutColor(Theme.getDefaultColor(Theme.key_chat_outBubble));
                boolean exists = new File(this.themeInfo.pathToFile).exists();
                if ((!exists || !parseTheme() || !exists) && (tL_theme = (themeInfo2 = this.themeInfo).info) != null) {
                    if (tL_theme.document != null) {
                        themeInfo2.themeLoaded = false;
                        this.placeholderAlpha = 1.0f;
                        Drawable mutate = getResources().getDrawable(R.drawable.msg_theme).mutate();
                        this.loadingDrawable = mutate;
                        int color = Theme.getColor(Theme.key_windowBackgroundWhiteGrayText7);
                        this.loadingColor = color;
                        Theme.setDrawableColor(mutate, color);
                        if (!exists) {
                            String attachFileName = FileLoader.getAttachFileName(this.themeInfo.info.document);
                            if (!ThemesHorizontalListCell.this.loadingThemes.containsKey(attachFileName)) {
                                ThemesHorizontalListCell.this.loadingThemes.put(attachFileName, this.themeInfo);
                                FileLoader fileLoader = FileLoader.getInstance(this.themeInfo.account);
                                TLRPC.TL_theme tL_theme2 = this.themeInfo.info;
                                fileLoader.loadFile(tL_theme2.document, tL_theme2, 1, 1);
                            }
                        }
                    } else {
                        Drawable mutate2 = getResources().getDrawable(R.drawable.preview_custom).mutate();
                        this.loadingDrawable = mutate2;
                        int color2 = Theme.getColor(Theme.key_windowBackgroundWhiteGrayText7);
                        this.loadingColor = color2;
                        Theme.setDrawableColor(mutate2, color2);
                    }
                }
            }
            applyTheme();
        }

        void updateColors(boolean z) {
            int i;
            int i2;
            this.oldInColor = this.inColor;
            this.oldOutColor = this.outColor;
            this.oldBackColor = this.backColor;
            this.oldCheckColor = this.checkColor;
            int i3 = 0;
            Theme.ThemeAccent accent = this.themeInfo.getAccent(false);
            if (accent != null) {
                i3 = accent.accentColor;
                i2 = accent.myMessagesAccentColor;
                if (i2 == 0) {
                    i2 = i3;
                }
                i = (int) accent.backgroundOverrideColor;
                if (i == 0) {
                    i = i3;
                }
            } else {
                i = 0;
                i2 = 0;
            }
            Theme.ThemeInfo themeInfo = this.themeInfo;
            this.inColor = Theme.changeColorAccent(themeInfo, i3, themeInfo.getPreviewInColor());
            Theme.ThemeInfo themeInfo2 = this.themeInfo;
            this.outColor = Theme.changeColorAccent(themeInfo2, i2, themeInfo2.getPreviewOutColor());
            Theme.ThemeInfo themeInfo3 = this.themeInfo;
            this.backColor = Theme.changeColorAccent(themeInfo3, i, themeInfo3.getPreviewBackgroundColor());
            this.checkColor = this.outColor;
            this.accentId = this.themeInfo.currentAccentId;
            ObjectAnimator objectAnimator = this.accentAnimator;
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
            if (!z) {
                setAccentState(1.0f);
                return;
            }
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "accentState", 0.0f, 1.0f);
            this.accentAnimator = ofFloat;
            ofFloat.setDuration(200L);
            this.accentAnimator.start();
        }

        public void updateCurrentThemeCheck() {
            this.button.setChecked(this.themeInfo == (ThemesHorizontalListCell.this.currentType == 1 ? Theme.getCurrentNightTheme() : Theme.getCurrentTheme()), true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ThemesListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        ThemesListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            ThemesHorizontalListCell themesHorizontalListCell = ThemesHorizontalListCell.this;
            return themesHorizontalListCell.prevCount = themesHorizontalListCell.defaultThemes.size() + ThemesHorizontalListCell.this.customThemes.size();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            ArrayList arrayList;
            int size;
            InnerThemeView innerThemeView = (InnerThemeView) viewHolder.itemView;
            if (i < ThemesHorizontalListCell.this.defaultThemes.size()) {
                arrayList = ThemesHorizontalListCell.this.defaultThemes;
                size = i;
            } else {
                arrayList = ThemesHorizontalListCell.this.customThemes;
                size = i - ThemesHorizontalListCell.this.defaultThemes.size();
            }
            innerThemeView.setTheme((Theme.ThemeInfo) arrayList.get(size), i == getItemCount() - 1, i == 0);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new RecyclerListView.Holder(new InnerThemeView(this.mContext));
        }
    }

    public ThemesHorizontalListCell(Context context, BaseFragment baseFragment, int i, ArrayList arrayList, ArrayList arrayList2) {
        super(context);
        this.loadingThemes = new HashMap();
        this.loadingWallpapers = new HashMap();
        this.customThemes = arrayList2;
        this.defaultThemes = arrayList;
        this.currentType = i;
        this.fragment = baseFragment;
        setBackgroundColor(Theme.getColor(i == 2 ? Theme.key_dialogBackground : Theme.key_windowBackgroundWhite));
        setItemAnimator(null);
        setLayoutAnimation(null);
        this.horizontalLayoutManager = new LinearLayoutManager(context) { // from class: org.telegram.ui.Cells.ThemesHorizontalListCell.1
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        setPadding(0, 0, 0, 0);
        setClipToPadding(false);
        this.horizontalLayoutManager.setOrientation(0);
        setLayoutManager(this.horizontalLayoutManager);
        ThemesListAdapter themesListAdapter = new ThemesListAdapter(context);
        this.adapter = themesListAdapter;
        setAdapter(themesListAdapter);
        setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Cells.ThemesHorizontalListCell$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i2) {
                ThemesHorizontalListCell.this.lambda$new$0(view, i2);
            }
        });
        setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.Cells.ThemesHorizontalListCell$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view, int i2) {
                boolean lambda$new$1;
                lambda$new$1 = ThemesHorizontalListCell.this.lambda$new$1(view, i2);
                return lambda$new$1;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: checkVisibleTheme */
    public void lambda$didReceivedNotification$2(Theme.ThemeInfo themeInfo) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof InnerThemeView) {
                InnerThemeView innerThemeView = (InnerThemeView) childAt;
                if (innerThemeView.themeInfo == themeInfo && innerThemeView.parseTheme()) {
                    innerThemeView.themeInfo.themeLoaded = true;
                    innerThemeView.applyTheme();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$3(final Theme.ThemeInfo themeInfo, File file) {
        themeInfo.badWallpaper = !themeInfo.createBackground(file, themeInfo.pathToWallpaper);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ThemesHorizontalListCell$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ThemesHorizontalListCell.this.lambda$didReceivedNotification$2(themeInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view, int i) {
        selectTheme(((InnerThemeView) view).themeInfo);
        int left = view.getLeft();
        int right = view.getRight();
        if (left < 0) {
            smoothScrollBy(left - AndroidUtilities.dp(8.0f), 0);
        } else if (right > getMeasuredWidth()) {
            smoothScrollBy(right - getMeasuredWidth(), 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$1(View view, int i) {
        showOptionsForTheme(((InnerThemeView) view).themeInfo);
        return true;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i != NotificationCenter.fileLoaded) {
            if (i == NotificationCenter.fileLoadFailed) {
                this.loadingThemes.remove((String) objArr[0]);
                return;
            }
            return;
        }
        String str = (String) objArr[0];
        final File file = (File) objArr[1];
        final Theme.ThemeInfo themeInfo = (Theme.ThemeInfo) this.loadingThemes.get(str);
        if (themeInfo != null) {
            this.loadingThemes.remove(str);
            if (this.loadingWallpapers.remove(themeInfo) != null) {
                Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Cells.ThemesHorizontalListCell$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        ThemesHorizontalListCell.this.lambda$didReceivedNotification$3(themeInfo, file);
                    }
                });
            } else {
                lambda$didReceivedNotification$2(themeInfo);
            }
        }
    }

    public void notifyDataSetChanged(int i) {
        if (this.prevCount == this.adapter.getItemCount()) {
            return;
        }
        this.adapter.notifyDataSetChanged();
        if (this.prevThemeInfo != (this.currentType == 1 ? Theme.getCurrentNightTheme() : Theme.getCurrentTheme())) {
            scrollToCurrentTheme(i, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (int i = 0; i < 4; i++) {
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.fileLoadFailed);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (int i = 0; i < 4; i++) {
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.fileLoadFailed);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.drawDivider) {
            canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
        }
    }

    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (getParent() != null && getParent().getParent() != null) {
            getParent().getParent().requestDisallowInterceptTouchEvent(canScrollHorizontally(-1));
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    public void scrollToCurrentTheme(int i, boolean z) {
        View view;
        if (i == 0 && (view = (View) getParent()) != null) {
            i = view.getMeasuredWidth();
        }
        if (i == 0) {
            return;
        }
        Theme.ThemeInfo currentNightTheme = this.currentType == 1 ? Theme.getCurrentNightTheme() : Theme.getCurrentTheme();
        this.prevThemeInfo = currentNightTheme;
        int indexOf = this.defaultThemes.indexOf(currentNightTheme);
        if (indexOf >= 0 || (indexOf = this.customThemes.indexOf(this.prevThemeInfo) + this.defaultThemes.size()) >= 0) {
            if (z) {
                smoothScrollToPosition(indexOf);
            } else {
                this.horizontalLayoutManager.scrollToPositionWithOffset(indexOf, (i - AndroidUtilities.dp(76.0f)) / 2);
            }
        }
    }

    public void selectTheme(Theme.ThemeInfo themeInfo) {
        TLRPC.TL_theme tL_theme = themeInfo.info;
        if (tL_theme != null) {
            if (!themeInfo.themeLoaded) {
                return;
            }
            if (tL_theme.document == null) {
                BaseFragment baseFragment = this.fragment;
                if (baseFragment != null) {
                    baseFragment.presentFragment(new ThemeSetUrlActivity(themeInfo, null, true));
                    return;
                }
                return;
            }
        }
        if (!TextUtils.isEmpty(themeInfo.assetName)) {
            Theme.PatternsLoader.createLoader(false);
        }
        SharedPreferences.Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0).edit();
        edit.putString((this.currentType == 1 || themeInfo.isDark()) ? "lastDarkTheme" : "lastDayTheme", themeInfo.getKey());
        edit.commit();
        if (this.currentType == 1) {
            if (themeInfo == Theme.getCurrentNightTheme()) {
                return;
            }
            Theme.setCurrentNightTheme(themeInfo);
        } else if (themeInfo == Theme.getCurrentTheme()) {
            return;
        } else {
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needSetDayNightTheme, themeInfo, Boolean.FALSE, null, -1);
        }
        updateRows();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof InnerThemeView) {
                ((InnerThemeView) childAt).updateCurrentThemeCheck();
            }
        }
        EmojiThemes.saveCustomTheme(themeInfo, themeInfo.currentAccentId);
        if (this.currentType != 1) {
            Theme.turnOffAutoNight(this.fragment);
        }
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        super.setBackgroundColor(i);
        invalidateViews();
    }

    public void setDrawDivider(boolean z) {
        this.drawDivider = z;
    }

    protected void showOptionsForTheme(Theme.ThemeInfo themeInfo) {
    }

    protected abstract void updateRows();
}
