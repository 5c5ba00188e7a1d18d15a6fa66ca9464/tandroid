package org.telegram.messenger;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.telegram.messenger.CompoundEmoji;
import org.telegram.ui.Components.AnimatedEmojiSpan;
/* loaded from: classes3.dex */
public class Emoji {
    private static String[] DEFAULT_RECENT = null;
    private static final int MAX_RECENT_EMOJI_COUNT = 48;
    public static int bigImgSize;
    public static int drawImgSize;
    private static Bitmap[][] emojiBmp;
    public static HashMap<String, String> emojiColor;
    private static int[] emojiCounts;
    public static boolean emojiDrawingUseAlpha;
    public static float emojiDrawingYOffset;
    public static HashMap<String, Integer> emojiUseHistory;
    public static Runnable invalidateUiRunnable;
    private static boolean[][] loadingEmoji;
    public static Paint placeholderPaint;
    public static ArrayList<String> recentEmoji;
    private static boolean recentEmojiLoaded;
    private static HashMap<CharSequence, DrawableInfo> rects = new HashMap<>();
    private static boolean inited = false;

    /* loaded from: classes3.dex */
    public static abstract class EmojiDrawable extends Drawable {
        boolean fullSize = false;
        int placeholderColor = 268435456;

        public boolean isLoaded() {
            return false;
        }

        public void preload() {
        }
    }

    static {
        String[][] strArr = EmojiData.data;
        emojiCounts = new int[]{strArr[0].length, strArr[1].length, strArr[2].length, strArr[3].length, strArr[4].length, strArr[5].length, strArr[6].length, strArr[7].length};
        emojiBmp = new Bitmap[8];
        loadingEmoji = new boolean[8];
        emojiUseHistory = new HashMap<>();
        recentEmoji = new ArrayList<>();
        emojiColor = new HashMap<>();
        invalidateUiRunnable = Emoji$$ExternalSyntheticLambda1.INSTANCE;
        emojiDrawingUseAlpha = true;
        DEFAULT_RECENT = new String[]{"😂", "😘", "❤", "😍", "😊", "😁", "👍", "☺", "😔", "😄", "😭", "💋", "😒", "😳", "😜", "🙈", "😉", "😃", "😢", "😝", "😱", "😡", "😏", "😞", "😅", "😚", "🙊", "😌", "😀", "😋", "😆", "👌", "😐", "😕"};
        drawImgSize = AndroidUtilities.dp(20.0f);
        bigImgSize = AndroidUtilities.dp(AndroidUtilities.isTablet() ? 40.0f : 34.0f);
        int i = 0;
        while (true) {
            Bitmap[][] bitmapArr = emojiBmp;
            if (i >= bitmapArr.length) {
                break;
            }
            int[] iArr = emojiCounts;
            bitmapArr[i] = new Bitmap[iArr[i]];
            loadingEmoji[i] = new boolean[iArr[i]];
            i++;
        }
        for (int i2 = 0; i2 < EmojiData.data.length; i2++) {
            int i3 = 0;
            while (true) {
                String[][] strArr2 = EmojiData.data;
                if (i3 < strArr2[i2].length) {
                    rects.put(strArr2[i2][i3], new DrawableInfo((byte) i2, (short) i3, i3));
                    i3++;
                }
            }
        }
        Paint paint = new Paint();
        placeholderPaint = paint;
        paint.setColor(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$static$0() {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.emojiLoaded, new Object[0]);
    }

    public static void preloadEmoji(CharSequence charSequence) {
        DrawableInfo drawableInfo = getDrawableInfo(charSequence);
        if (drawableInfo != null) {
            loadEmoji(drawableInfo.page, drawableInfo.page2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loadEmoji(final byte b, final short s) {
        if (emojiBmp[b][s] == null) {
            boolean[][] zArr = loadingEmoji;
            if (zArr[b][s]) {
                return;
            }
            zArr[b][s] = true;
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.Emoji$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Emoji.lambda$loadEmoji$1(b, s);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadEmoji$1(byte b, short s) {
        Bitmap loadBitmap = loadBitmap("emoji/" + String.format(Locale.US, "%d_%d.png", Byte.valueOf(b), Short.valueOf(s)));
        if (loadBitmap != null) {
            emojiBmp[b][s] = loadBitmap;
            AndroidUtilities.cancelRunOnUIThread(invalidateUiRunnable);
            AndroidUtilities.runOnUIThread(invalidateUiRunnable);
        }
        loadingEmoji[b][s] = false;
    }

    public static Bitmap loadBitmap(String str) {
        Bitmap bitmap;
        try {
            int i = AndroidUtilities.density <= 1.0f ? 2 : 1;
            try {
                InputStream open = ApplicationLoader.applicationContext.getAssets().open(str);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize = i;
                bitmap = BitmapFactory.decodeStream(open, null, options);
                try {
                    open.close();
                } catch (Throwable th) {
                    th = th;
                    FileLog.e(th);
                    return bitmap;
                }
            } catch (Throwable th2) {
                th = th2;
                bitmap = null;
            }
            return bitmap;
        } catch (Throwable th3) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Error loading emoji", th3);
            }
            return null;
        }
    }

    public static void invalidateAll(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                invalidateAll(viewGroup.getChildAt(i));
            }
        } else if (view instanceof TextView) {
            view.invalidate();
        }
    }

    public static String fixEmoji(String str) {
        int length = str.length();
        int i = 0;
        while (i < length) {
            char charAt = str.charAt(i);
            if (charAt < 55356 || charAt > 55358) {
                if (charAt == 8419) {
                    return str;
                }
                if (charAt >= 8252 && charAt <= 12953 && EmojiData.emojiToFE0FMap.containsKey(Character.valueOf(charAt))) {
                    StringBuilder sb = new StringBuilder();
                    i++;
                    sb.append(str.substring(0, i));
                    sb.append("️");
                    sb.append(str.substring(i));
                    str = sb.toString();
                    length++;
                }
            } else if (charAt != 55356 || i >= length - 1) {
                i++;
            } else {
                int i2 = i + 1;
                char charAt2 = str.charAt(i2);
                if (charAt2 == 56879 || charAt2 == 56324 || charAt2 == 56858 || charAt2 == 56703) {
                    StringBuilder sb2 = new StringBuilder();
                    i += 2;
                    sb2.append(str.substring(0, i));
                    sb2.append("️");
                    sb2.append(str.substring(i));
                    str = sb2.toString();
                    length++;
                } else {
                    i = i2;
                }
            }
            i++;
        }
        return str;
    }

    public static EmojiDrawable getEmojiDrawable(CharSequence charSequence) {
        CompoundEmoji.CompoundEmojiDrawable compoundEmojiDrawable;
        DrawableInfo drawableInfo = getDrawableInfo(charSequence);
        if (drawableInfo != null) {
            SimpleEmojiDrawable simpleEmojiDrawable = new SimpleEmojiDrawable(drawableInfo, endsWithRightArrow(charSequence));
            int i = drawImgSize;
            simpleEmojiDrawable.setBounds(0, 0, i, i);
            return simpleEmojiDrawable;
        } else if (charSequence == null || (compoundEmojiDrawable = CompoundEmoji.getCompoundEmojiDrawable(charSequence.toString())) == null) {
            return null;
        } else {
            int i2 = drawImgSize;
            compoundEmojiDrawable.setBounds(0, 0, i2, i2);
            return compoundEmojiDrawable;
        }
    }

    public static boolean endsWithRightArrow(CharSequence charSequence) {
        return charSequence != null && charSequence.length() > 2 && charSequence.charAt(charSequence.length() - 2) == 8205 && charSequence.charAt(charSequence.length() - 1) == 10145;
    }

    private static DrawableInfo getDrawableInfo(CharSequence charSequence) {
        CharSequence charSequence2;
        if (endsWithRightArrow(charSequence)) {
            charSequence = charSequence.subSequence(0, charSequence.length() - 2);
        }
        DrawableInfo drawableInfo = rects.get(charSequence);
        return (drawableInfo != null || (charSequence2 = EmojiData.emojiAliasMap.get(charSequence)) == null) ? drawableInfo : rects.get(charSequence2);
    }

    public static boolean isValidEmoji(CharSequence charSequence) {
        CharSequence charSequence2;
        if (TextUtils.isEmpty(charSequence)) {
            return false;
        }
        DrawableInfo drawableInfo = rects.get(charSequence);
        if (drawableInfo == null && (charSequence2 = EmojiData.emojiAliasMap.get(charSequence)) != null) {
            drawableInfo = rects.get(charSequence2);
        }
        return drawableInfo != null;
    }

    public static Drawable getEmojiBigDrawable(String str) {
        CharSequence charSequence;
        EmojiDrawable compoundEmojiDrawable = CompoundEmoji.getCompoundEmojiDrawable(str);
        if (compoundEmojiDrawable != null) {
            int i = drawImgSize;
            compoundEmojiDrawable.setBounds(0, 0, i, i);
        } else {
            compoundEmojiDrawable = null;
        }
        if (compoundEmojiDrawable == null) {
            compoundEmojiDrawable = getEmojiDrawable(str);
        }
        if (compoundEmojiDrawable == null && (charSequence = EmojiData.emojiAliasMap.get(str)) != null) {
            compoundEmojiDrawable = getEmojiDrawable(charSequence);
        }
        if (compoundEmojiDrawable == null) {
            return null;
        }
        int i2 = bigImgSize;
        compoundEmojiDrawable.setBounds(0, 0, i2, i2);
        compoundEmojiDrawable.fullSize = true;
        return compoundEmojiDrawable;
    }

    /* loaded from: classes3.dex */
    public static class SimpleEmojiDrawable extends EmojiDrawable {
        private static Paint paint = new Paint(2);
        private static Rect rect = new Rect();
        private DrawableInfo info;
        private boolean invert;

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        public SimpleEmojiDrawable(DrawableInfo drawableInfo, boolean z) {
            this.info = drawableInfo;
            this.invert = z;
        }

        public DrawableInfo getDrawableInfo() {
            return this.info;
        }

        public Rect getDrawRect() {
            Rect bounds = getBounds();
            int centerX = bounds.centerX();
            int centerY = bounds.centerY();
            Rect rect2 = rect;
            boolean z = this.fullSize;
            rect2.left = centerX - ((z ? Emoji.bigImgSize : Emoji.drawImgSize) / 2);
            rect.right = centerX + ((z ? Emoji.bigImgSize : Emoji.drawImgSize) / 2);
            rect.top = centerY - ((z ? Emoji.bigImgSize : Emoji.drawImgSize) / 2);
            rect.bottom = centerY + ((z ? Emoji.bigImgSize : Emoji.drawImgSize) / 2);
            return rect;
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            Rect bounds;
            if (!isLoaded()) {
                DrawableInfo drawableInfo = this.info;
                Emoji.loadEmoji(drawableInfo.page, drawableInfo.page2);
                Emoji.placeholderPaint.setColor(this.placeholderColor);
                Rect bounds2 = getBounds();
                canvas.drawCircle(bounds2.centerX(), bounds2.centerY(), bounds2.width() * 0.4f, Emoji.placeholderPaint);
                return;
            }
            if (this.fullSize) {
                bounds = getDrawRect();
            } else {
                bounds = getBounds();
            }
            if (canvas.quickReject(bounds.left, bounds.top, bounds.right, bounds.bottom, Canvas.EdgeType.AA)) {
                return;
            }
            if (this.invert) {
                canvas.save();
                canvas.scale(-1.0f, 1.0f, bounds.centerX(), bounds.centerY());
            }
            Bitmap[][] bitmapArr = Emoji.emojiBmp;
            DrawableInfo drawableInfo2 = this.info;
            canvas.drawBitmap(bitmapArr[drawableInfo2.page][drawableInfo2.page2], (Rect) null, bounds, paint);
            if (this.invert) {
                canvas.restore();
            }
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            paint.setAlpha(i);
        }

        @Override // org.telegram.messenger.Emoji.EmojiDrawable
        public boolean isLoaded() {
            Bitmap[][] bitmapArr = Emoji.emojiBmp;
            DrawableInfo drawableInfo = this.info;
            return bitmapArr[drawableInfo.page][drawableInfo.page2] != null;
        }

        @Override // org.telegram.messenger.Emoji.EmojiDrawable
        public void preload() {
            if (isLoaded()) {
                return;
            }
            DrawableInfo drawableInfo = this.info;
            Emoji.loadEmoji(drawableInfo.page, drawableInfo.page2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class DrawableInfo {
        public int emojiIndex;
        public byte page;
        public short page2;

        public DrawableInfo(byte b, short s, int i) {
            this.page = b;
            this.page2 = s;
            this.emojiIndex = i;
        }
    }

    /* loaded from: classes3.dex */
    public static class EmojiSpanRange {
        public CharSequence code;
        public int end;
        public int start;

        public EmojiSpanRange(int i, int i2, CharSequence charSequence) {
            this.start = i;
            this.end = i2;
            this.code = charSequence;
        }
    }

    public static boolean fullyConsistsOfEmojis(CharSequence charSequence) {
        int[] iArr = new int[1];
        parseEmojis(charSequence, iArr);
        return iArr[0] > 0;
    }

    public static ArrayList<EmojiSpanRange> parseEmojis(CharSequence charSequence) {
        return parseEmojis(charSequence, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:121:0x01a2 A[Catch: Exception -> 0x0286, TryCatch #0 {Exception -> 0x0286, blocks: (B:9:0x0029, B:30:0x006c, B:88:0x011f, B:90:0x0123, B:92:0x0130, B:96:0x013e, B:121:0x01a2, B:123:0x01a6, B:127:0x01b3, B:129:0x01b9, B:167:0x0216, B:145:0x01e5, B:147:0x01e9, B:158:0x0201, B:160:0x0205, B:171:0x0221, B:173:0x0228, B:175:0x022c, B:177:0x0237, B:181:0x0245, B:184:0x0255, B:186:0x025e, B:188:0x0261, B:189:0x0272, B:97:0x014b, B:99:0x0152, B:101:0x015c, B:105:0x016b, B:107:0x0171, B:108:0x0178, B:110:0x0180, B:111:0x0187, B:113:0x0191, B:115:0x0197, B:17:0x0041, B:19:0x004c, B:32:0x0078, B:44:0x009a, B:42:0x0090, B:49:0x00ac, B:57:0x00c0, B:77:0x0100, B:68:0x00e2, B:75:0x00f8), top: B:200:0x0029 }] */
    /* JADX WARN: Removed duplicated region for block: B:170:0x021f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:173:0x0228 A[Catch: Exception -> 0x0286, TryCatch #0 {Exception -> 0x0286, blocks: (B:9:0x0029, B:30:0x006c, B:88:0x011f, B:90:0x0123, B:92:0x0130, B:96:0x013e, B:121:0x01a2, B:123:0x01a6, B:127:0x01b3, B:129:0x01b9, B:167:0x0216, B:145:0x01e5, B:147:0x01e9, B:158:0x0201, B:160:0x0205, B:171:0x0221, B:173:0x0228, B:175:0x022c, B:177:0x0237, B:181:0x0245, B:184:0x0255, B:186:0x025e, B:188:0x0261, B:189:0x0272, B:97:0x014b, B:99:0x0152, B:101:0x015c, B:105:0x016b, B:107:0x0171, B:108:0x0178, B:110:0x0180, B:111:0x0187, B:113:0x0191, B:115:0x0197, B:17:0x0041, B:19:0x004c, B:32:0x0078, B:44:0x009a, B:42:0x0090, B:49:0x00ac, B:57:0x00c0, B:77:0x0100, B:68:0x00e2, B:75:0x00f8), top: B:200:0x0029 }] */
    /* JADX WARN: Removed duplicated region for block: B:183:0x0253  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x027d  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0100 A[Catch: Exception -> 0x0286, TryCatch #0 {Exception -> 0x0286, blocks: (B:9:0x0029, B:30:0x006c, B:88:0x011f, B:90:0x0123, B:92:0x0130, B:96:0x013e, B:121:0x01a2, B:123:0x01a6, B:127:0x01b3, B:129:0x01b9, B:167:0x0216, B:145:0x01e5, B:147:0x01e9, B:158:0x0201, B:160:0x0205, B:171:0x0221, B:173:0x0228, B:175:0x022c, B:177:0x0237, B:181:0x0245, B:184:0x0255, B:186:0x025e, B:188:0x0261, B:189:0x0272, B:97:0x014b, B:99:0x0152, B:101:0x015c, B:105:0x016b, B:107:0x0171, B:108:0x0178, B:110:0x0180, B:111:0x0187, B:113:0x0191, B:115:0x0197, B:17:0x0041, B:19:0x004c, B:32:0x0078, B:44:0x009a, B:42:0x0090, B:49:0x00ac, B:57:0x00c0, B:77:0x0100, B:68:0x00e2, B:75:0x00f8), top: B:200:0x0029 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x010b  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x011f A[Catch: Exception -> 0x0286, TryCatch #0 {Exception -> 0x0286, blocks: (B:9:0x0029, B:30:0x006c, B:88:0x011f, B:90:0x0123, B:92:0x0130, B:96:0x013e, B:121:0x01a2, B:123:0x01a6, B:127:0x01b3, B:129:0x01b9, B:167:0x0216, B:145:0x01e5, B:147:0x01e9, B:158:0x0201, B:160:0x0205, B:171:0x0221, B:173:0x0228, B:175:0x022c, B:177:0x0237, B:181:0x0245, B:184:0x0255, B:186:0x025e, B:188:0x0261, B:189:0x0272, B:97:0x014b, B:99:0x0152, B:101:0x015c, B:105:0x016b, B:107:0x0171, B:108:0x0178, B:110:0x0180, B:111:0x0187, B:113:0x0191, B:115:0x0197, B:17:0x0041, B:19:0x004c, B:32:0x0078, B:44:0x009a, B:42:0x0090, B:49:0x00ac, B:57:0x00c0, B:77:0x0100, B:68:0x00e2, B:75:0x00f8), top: B:200:0x0029 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ArrayList<EmojiSpanRange> parseEmojis(CharSequence charSequence, int[] iArr) {
        boolean z;
        char charAt;
        boolean z2;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        char charAt2;
        int i7;
        int i8;
        ArrayList<EmojiSpanRange> arrayList = new ArrayList<>();
        if (charSequence != null && charSequence.length() > 0) {
            StringBuilder sb = new StringBuilder(16);
            int length = charSequence.length();
            int[] iArr2 = iArr;
            int i9 = 0;
            int i10 = 0;
            long j = 0;
            int i11 = -1;
            int i12 = 0;
            boolean z3 = false;
            boolean z4 = false;
            while (i10 < length) {
                try {
                    char charAt3 = charSequence.charAt(i10);
                    if (charAt3 >= 55356) {
                        if (charAt3 > 55358) {
                        }
                        if (i11 != -1) {
                            i11 = i10;
                        } else if (z3) {
                            i11 = i10;
                            i12 = 0;
                            z3 = false;
                        }
                        sb.append(charAt3);
                        i12++;
                        j = (j << 16) | charAt3;
                        z = false;
                        if (z4 || (i8 = i10 + 2) >= length) {
                            z2 = z;
                        } else {
                            int i13 = i10 + 1;
                            char charAt4 = charSequence.charAt(i13);
                            z2 = z;
                            if (charAt4 == 55356) {
                                char charAt5 = charSequence.charAt(i8);
                                if (charAt5 >= 57339 && charAt5 <= 57343) {
                                    sb.append(charSequence.subSequence(i13, i10 + 3));
                                    i12 += 2;
                                    i = i8;
                                    i2 = i;
                                    i3 = 0;
                                    while (i3 < 3) {
                                        int i14 = i2 + 1;
                                        if (i14 < length) {
                                            char charAt6 = charSequence.charAt(i14);
                                            i7 = i;
                                            if (i3 != 1) {
                                                if (charAt3 != '*' && charAt3 != '#' && (charAt3 < '0' || charAt3 > '9')) {
                                                    if (i11 != -1 && charAt6 >= 65024) {
                                                        if (charAt6 <= 65039) {
                                                            i12++;
                                                            if (!z4) {
                                                                z4 = i14 + 1 >= length;
                                                            }
                                                            i2 = i14;
                                                        }
                                                    }
                                                }
                                                if (charAt6 >= 65024) {
                                                    if (charAt6 <= 65039) {
                                                        i12++;
                                                        if (!z4) {
                                                            z4 = i14 + 1 >= length;
                                                        }
                                                        i2 = i14;
                                                        i11 = i7;
                                                        z3 = true;
                                                    }
                                                }
                                            } else if (charAt6 == 8205 && sb.length() > 0) {
                                                sb.append(charAt6);
                                                i12++;
                                                i2 = i14;
                                                z4 = false;
                                                z2 = false;
                                            }
                                            i3++;
                                            i = i7;
                                        } else {
                                            i7 = i;
                                        }
                                        i3++;
                                        i = i7;
                                    }
                                    int i15 = i;
                                    if (z2 && iArr2 != null) {
                                        iArr2[0] = 0;
                                        iArr2 = null;
                                    }
                                    if (z4 && (i5 = i2 + 2) < length) {
                                        i6 = i2 + 1;
                                        if (charSequence.charAt(i6) == 55356 && (charAt2 = charSequence.charAt(i5)) >= 57339 && charAt2 <= 57343) {
                                            sb.append(charSequence.subSequence(i6, i2 + 3));
                                            i12 += 2;
                                            i2 = i5;
                                        }
                                    }
                                    if (z4) {
                                        if (iArr2 != null) {
                                            iArr2[0] = iArr2[0] + 1;
                                        }
                                        if (i11 >= 0 && (i4 = i12 + i11) <= length) {
                                            arrayList.add(new EmojiSpanRange(i11, i4, sb.subSequence(0, sb.length())));
                                        }
                                        sb.setLength(0);
                                        i11 = -1;
                                        i12 = 0;
                                        z3 = false;
                                        z4 = false;
                                    }
                                    i10 = i2 + 1;
                                    i9 = i15;
                                }
                            } else if (sb.length() >= 2 && sb.charAt(0) == 55356 && sb.charAt(1) == 57332 && charAt4 == 56128) {
                                do {
                                    if (i13 < charSequence.length()) {
                                        sb.append(charSequence.charAt(i13));
                                    }
                                    int i16 = i13 + 1;
                                    if (i16 < charSequence.length()) {
                                        sb.append(charSequence.charAt(i16));
                                    }
                                    i12 += 2;
                                    i13 += 2;
                                    if (i13 >= charSequence.length()) {
                                        break;
                                    }
                                } while (charSequence.charAt(i13) == 56128);
                                i10 = i13 - 1;
                            }
                        }
                        i = i10;
                        i2 = i;
                        i3 = 0;
                        while (i3 < 3) {
                        }
                        int i152 = i;
                        if (z2) {
                            iArr2[0] = 0;
                            iArr2 = null;
                        }
                        if (z4) {
                            i6 = i2 + 1;
                            if (charSequence.charAt(i6) == 55356) {
                                sb.append(charSequence.subSequence(i6, i2 + 3));
                                i12 += 2;
                                i2 = i5;
                            }
                        }
                        if (z4) {
                        }
                        i10 = i2 + 1;
                        i9 = i152;
                    }
                    if (j != 0 && (j & (-4294967296L)) == 0 && (j & 65535) == 55356 && charAt3 >= 56806 && charAt3 <= 56831) {
                        if (i11 != -1) {
                        }
                        sb.append(charAt3);
                        i12++;
                        j = (j << 16) | charAt3;
                        z = false;
                        if (z4) {
                        }
                        z2 = z;
                        i = i10;
                        i2 = i;
                        i3 = 0;
                        while (i3 < 3) {
                        }
                        int i1522 = i;
                        if (z2) {
                        }
                        if (z4) {
                        }
                        if (z4) {
                        }
                        i10 = i2 + 1;
                        i9 = i1522;
                    } else {
                        if ((sb.length() <= 0 || !(charAt3 == 9792 || charAt3 == 9794 || charAt3 == 9877)) && (j <= 0 || (charAt3 & 61440) != 53248)) {
                            if (charAt3 != 8419) {
                                if (charAt3 != 169) {
                                    if (charAt3 != 174) {
                                        if (charAt3 >= 8252 && charAt3 <= 12953) {
                                        }
                                        if (i11 == -1) {
                                            sb.setLength(0);
                                            z = false;
                                            i11 = -1;
                                            i12 = 0;
                                            z3 = false;
                                            z4 = false;
                                        } else if (charAt3 != 65039 && charAt3 != '\n' && charAt3 != ' ' && charAt3 != '\t') {
                                            z = true;
                                        }
                                        if (z4) {
                                        }
                                        z2 = z;
                                        i = i10;
                                        i2 = i;
                                        i3 = 0;
                                        while (i3 < 3) {
                                        }
                                        int i15222 = i;
                                        if (z2) {
                                        }
                                        if (z4) {
                                        }
                                        if (z4) {
                                        }
                                        i10 = i2 + 1;
                                        i9 = i15222;
                                    }
                                }
                                if (EmojiData.dataCharsMap.containsKey(Character.valueOf(charAt3))) {
                                    if (i11 == -1) {
                                        i11 = i10;
                                    } else if (z3) {
                                        i11 = i10;
                                        i12 = 0;
                                        z3 = false;
                                    }
                                    i12++;
                                    sb.append(charAt3);
                                    z = false;
                                }
                                if (i11 == -1) {
                                }
                                if (z4) {
                                }
                                z2 = z;
                                i = i10;
                                i2 = i;
                                i3 = 0;
                                while (i3 < 3) {
                                }
                                int i152222 = i;
                                if (z2) {
                                }
                                if (z4) {
                                }
                                if (z4) {
                                }
                                i10 = i2 + 1;
                                i9 = i152222;
                            } else if (i10 > 0 && (((charAt = charSequence.charAt(i9)) >= '0' && charAt <= '9') || charAt == '#' || charAt == '*')) {
                                i12 = (i10 - i9) + 1;
                                sb.append(charAt);
                                sb.append(charAt3);
                                i11 = i9;
                                z3 = false;
                                z4 = true;
                            }
                            z = false;
                            if (z4) {
                            }
                            z2 = z;
                            i = i10;
                            i2 = i;
                            i3 = 0;
                            while (i3 < 3) {
                            }
                            int i1522222 = i;
                            if (z2) {
                            }
                            if (z4) {
                            }
                            if (z4) {
                            }
                            i10 = i2 + 1;
                            i9 = i1522222;
                        } else {
                            sb.append(charAt3);
                            i12++;
                            z = false;
                            j = 0;
                        }
                        z4 = true;
                        if (z4) {
                        }
                        z2 = z;
                        i = i10;
                        i2 = i;
                        i3 = 0;
                        while (i3 < 3) {
                        }
                        int i15222222 = i;
                        if (z2) {
                        }
                        if (z4) {
                        }
                        if (z4) {
                        }
                        i10 = i2 + 1;
                        i9 = i15222222;
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            if (iArr2 != null && sb.length() != 0) {
                iArr2[0] = 0;
            }
        }
        return arrayList;
    }

    public static CharSequence replaceEmoji(CharSequence charSequence, Paint.FontMetricsInt fontMetricsInt, boolean z) {
        return replaceEmoji(charSequence, fontMetricsInt, z, (int[]) null);
    }

    public static CharSequence replaceEmoji(CharSequence charSequence, Paint.FontMetricsInt fontMetricsInt, int i, boolean z) {
        return replaceEmoji(charSequence, fontMetricsInt, z, (int[]) null);
    }

    public static CharSequence replaceEmoji(CharSequence charSequence, Paint.FontMetricsInt fontMetricsInt, boolean z, int[] iArr) {
        return replaceEmoji(charSequence, fontMetricsInt, z, iArr, 0);
    }

    public static CharSequence replaceEmoji(CharSequence charSequence, Paint.FontMetricsInt fontMetricsInt, boolean z, int[] iArr, int i) {
        Spannable newSpannable;
        int i2;
        EmojiSpanRange emojiSpanRange;
        boolean z2;
        if (SharedConfig.useSystemEmoji || charSequence == null || charSequence.length() == 0) {
            return charSequence;
        }
        if (!z && (charSequence instanceof Spannable)) {
            newSpannable = (Spannable) charSequence;
        } else {
            newSpannable = Spannable.Factory.getInstance().newSpannable(charSequence.toString());
        }
        ArrayList<EmojiSpanRange> parseEmojis = parseEmojis(newSpannable, iArr);
        if (parseEmojis.isEmpty()) {
            return charSequence;
        }
        AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) newSpannable.getSpans(0, newSpannable.length(), AnimatedEmojiSpan.class);
        int i3 = SharedConfig.getDevicePerformanceClass() >= 2 ? 100 : 50;
        while (i2 < parseEmojis.size()) {
            try {
                emojiSpanRange = parseEmojis.get(i2);
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (animatedEmojiSpanArr != null) {
                int i4 = 0;
                while (true) {
                    if (i4 >= animatedEmojiSpanArr.length) {
                        z2 = false;
                        break;
                    }
                    AnimatedEmojiSpan animatedEmojiSpan = animatedEmojiSpanArr[i4];
                    if (animatedEmojiSpan != null && newSpannable.getSpanStart(animatedEmojiSpan) == emojiSpanRange.start && newSpannable.getSpanEnd(animatedEmojiSpan) == emojiSpanRange.end) {
                        z2 = true;
                        break;
                    }
                    i4++;
                }
                i2 = z2 ? i2 + 1 : 0;
            }
            EmojiDrawable emojiDrawable = getEmojiDrawable(emojiSpanRange.code);
            if (emojiDrawable != null) {
                EmojiSpan emojiSpan = new EmojiSpan(emojiDrawable, i, fontMetricsInt);
                CharSequence charSequence2 = emojiSpanRange.code;
                emojiSpan.emoji = charSequence2 == null ? null : charSequence2.toString();
                newSpannable.setSpan(emojiSpan, emojiSpanRange.start, emojiSpanRange.end, 33);
            }
            int i5 = Build.VERSION.SDK_INT;
            if ((i5 < 23 || i5 >= 29) && i2 + 1 >= i3) {
                break;
            }
        }
        return newSpannable;
    }

    /* loaded from: classes3.dex */
    public static class EmojiSpan extends ImageSpan {
        public boolean drawn;
        public String emoji;
        public Paint.FontMetricsInt fontMetrics;
        public float lastDrawX;
        public float lastDrawY;
        public float scale;
        public int size;

        public EmojiSpan(Drawable drawable, int i, Paint.FontMetricsInt fontMetricsInt) {
            super(drawable, i);
            this.scale = 1.0f;
            this.size = AndroidUtilities.dp(20.0f);
            this.fontMetrics = fontMetricsInt;
            if (fontMetricsInt != null) {
                int abs = Math.abs(fontMetricsInt.descent) + Math.abs(this.fontMetrics.ascent);
                this.size = abs;
                if (abs == 0) {
                    this.size = AndroidUtilities.dp(20.0f);
                }
            }
        }

        public void replaceFontMetrics(Paint.FontMetricsInt fontMetricsInt, int i) {
            this.fontMetrics = fontMetricsInt;
            this.size = i;
        }

        public void replaceFontMetrics(Paint.FontMetricsInt fontMetricsInt) {
            this.fontMetrics = fontMetricsInt;
            if (fontMetricsInt != null) {
                int abs = Math.abs(fontMetricsInt.descent) + Math.abs(this.fontMetrics.ascent);
                this.size = abs;
                if (abs == 0) {
                    this.size = AndroidUtilities.dp(20.0f);
                }
            }
        }

        @Override // android.text.style.DynamicDrawableSpan, android.text.style.ReplacementSpan
        public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
            if (fontMetricsInt == null) {
                fontMetricsInt = new Paint.FontMetricsInt();
            }
            int i3 = (int) (this.scale * this.size);
            Paint.FontMetricsInt fontMetricsInt2 = this.fontMetrics;
            if (fontMetricsInt2 == null) {
                int size = super.getSize(paint, charSequence, i, i2, fontMetricsInt);
                int dp = AndroidUtilities.dp(8.0f);
                int dp2 = AndroidUtilities.dp(10.0f);
                int i4 = (-dp2) - dp;
                fontMetricsInt.top = i4;
                int i5 = dp2 - dp;
                fontMetricsInt.bottom = i5;
                fontMetricsInt.ascent = i4;
                fontMetricsInt.leading = 0;
                fontMetricsInt.descent = i5;
                return size;
            }
            fontMetricsInt.ascent = fontMetricsInt2.ascent;
            fontMetricsInt.descent = fontMetricsInt2.descent;
            fontMetricsInt.top = fontMetricsInt2.top;
            fontMetricsInt.bottom = fontMetricsInt2.bottom;
            if (getDrawable() != null) {
                getDrawable().setBounds(0, 0, i3, i3);
            }
            return i3;
        }

        @Override // android.text.style.DynamicDrawableSpan, android.text.style.ReplacementSpan
        public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
            boolean z;
            this.lastDrawX = f + ((this.scale * this.size) / 2.0f);
            this.lastDrawY = i3 + ((i5 - i3) / 2.0f);
            boolean z2 = true;
            this.drawn = true;
            if (paint.getAlpha() == 255 || !Emoji.emojiDrawingUseAlpha) {
                z = false;
            } else {
                getDrawable().setAlpha(paint.getAlpha());
                z = true;
            }
            float f2 = Emoji.emojiDrawingYOffset;
            int i6 = this.size;
            float f3 = f2 - ((i6 - (this.scale * i6)) / 2.0f);
            if (f3 != 0.0f) {
                canvas.save();
                canvas.translate(0.0f, f3);
            } else {
                z2 = false;
            }
            super.draw(canvas, charSequence, i, i2, f, i3, i4, i5, paint);
            if (z2) {
                canvas.restore();
            }
            if (z) {
                getDrawable().setAlpha(255);
            }
        }

        @Override // android.text.style.ReplacementSpan, android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            if (getDrawable() instanceof EmojiDrawable) {
                ((EmojiDrawable) getDrawable()).placeholderColor = 285212671 & textPaint.getColor();
            }
            super.updateDrawState(textPaint);
        }
    }

    public static void addRecentEmoji(String str) {
        Integer num = emojiUseHistory.get(str);
        if (num == null) {
            num = 0;
        }
        if (num.intValue() == 0 && emojiUseHistory.size() >= 48) {
            ArrayList<String> arrayList = recentEmoji;
            emojiUseHistory.remove(arrayList.get(arrayList.size() - 1));
            ArrayList<String> arrayList2 = recentEmoji;
            arrayList2.set(arrayList2.size() - 1, str);
        }
        emojiUseHistory.put(str, Integer.valueOf(num.intValue() + 1));
    }

    public static void removeRecentEmoji(String str) {
        emojiUseHistory.remove(str);
        recentEmoji.remove(str);
        if (emojiUseHistory.isEmpty() || recentEmoji.isEmpty()) {
            addRecentEmoji(DEFAULT_RECENT[0]);
        }
    }

    public static void sortEmoji() {
        recentEmoji.clear();
        for (Map.Entry<String, Integer> entry : emojiUseHistory.entrySet()) {
            recentEmoji.add(entry.getKey());
        }
        Collections.sort(recentEmoji, Emoji$$ExternalSyntheticLambda2.INSTANCE);
        while (recentEmoji.size() > 48) {
            ArrayList<String> arrayList = recentEmoji;
            arrayList.remove(arrayList.size() - 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$sortEmoji$2(String str, String str2) {
        Integer num = emojiUseHistory.get(str);
        Integer num2 = emojiUseHistory.get(str2);
        if (num == null) {
            num = 0;
        }
        if (num2 == null) {
            num2 = 0;
        }
        if (num.intValue() > num2.intValue()) {
            return -1;
        }
        return num.intValue() < num2.intValue() ? 1 : 0;
    }

    public static void saveRecentEmoji() {
        SharedPreferences globalEmojiSettings = MessagesController.getGlobalEmojiSettings();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : emojiUseHistory.entrySet()) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
        }
        globalEmojiSettings.edit().putString("emojis2", sb.toString()).commit();
    }

    public static void clearRecentEmoji() {
        MessagesController.getGlobalEmojiSettings().edit().putBoolean("filled_default", true).commit();
        emojiUseHistory.clear();
        recentEmoji.clear();
        saveRecentEmoji();
    }

    public static void loadRecentEmoji() {
        if (recentEmojiLoaded) {
            return;
        }
        recentEmojiLoaded = true;
        SharedPreferences globalEmojiSettings = MessagesController.getGlobalEmojiSettings();
        try {
            emojiUseHistory.clear();
            if (globalEmojiSettings.contains("emojis")) {
                String string = globalEmojiSettings.getString("emojis", "");
                if (string != null && string.length() > 0) {
                    for (String str : string.split(",")) {
                        String[] split = str.split("=");
                        long longValue = Utilities.parseLong(split[0]).longValue();
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < 4; i++) {
                            sb.insert(0, (char) longValue);
                            longValue >>= 16;
                            if (longValue == 0) {
                                break;
                            }
                        }
                        if (sb.length() > 0) {
                            emojiUseHistory.put(sb.toString(), Utilities.parseInt((CharSequence) split[1]));
                        }
                    }
                }
                globalEmojiSettings.edit().remove("emojis").commit();
                saveRecentEmoji();
            } else {
                String string2 = globalEmojiSettings.getString("emojis2", "");
                if (string2 != null && string2.length() > 0) {
                    for (String str2 : string2.split(",")) {
                        String[] split2 = str2.split("=");
                        emojiUseHistory.put(split2[0], Utilities.parseInt((CharSequence) split2[1]));
                    }
                }
            }
            if (emojiUseHistory.isEmpty() && !globalEmojiSettings.getBoolean("filled_default", false)) {
                int i2 = 0;
                while (true) {
                    String[] strArr = DEFAULT_RECENT;
                    if (i2 >= strArr.length) {
                        break;
                    }
                    emojiUseHistory.put(strArr[i2], Integer.valueOf(strArr.length - i2));
                    i2++;
                }
                globalEmojiSettings.edit().putBoolean("filled_default", true).commit();
                saveRecentEmoji();
            }
            sortEmoji();
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            String string3 = globalEmojiSettings.getString("color", "");
            if (string3 == null || string3.length() <= 0) {
                return;
            }
            for (String str3 : string3.split(",")) {
                String[] split3 = str3.split("=");
                emojiColor.put(split3[0], split3[1]);
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    public static void saveEmojiColors() {
        SharedPreferences globalEmojiSettings = MessagesController.getGlobalEmojiSettings();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : emojiColor.entrySet()) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
        }
        globalEmojiSettings.edit().putString("color", sb.toString()).commit();
    }
}
