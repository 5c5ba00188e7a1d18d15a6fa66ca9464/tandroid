package org.telegram.ui.Components.Paint;

import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.SystemFonts;
import android.os.Build;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.Utilities;
/* loaded from: classes4.dex */
public class PaintTypeface {
    public static final List<PaintTypeface> BUILT_IN_FONTS;
    public static final PaintTypeface COURIER_NEW_BOLD;
    public static final PaintTypeface MW_BOLD;
    public static final PaintTypeface ROBOTO_CONDENSED;
    public static final PaintTypeface ROBOTO_ITALIC;
    public static final PaintTypeface ROBOTO_MEDIUM;
    public static final PaintTypeface ROBOTO_MONO;
    public static final PaintTypeface ROBOTO_SERIF;
    public static boolean loadingTypefaces;
    private static final List<String> preferable;
    private static List<PaintTypeface> typefaces;
    private final String key;
    private final LazyTypeface lazyTypeface;
    private final String name;
    private final String nameKey;
    private final Typeface typeface;

    static {
        PaintTypeface paintTypeface = new PaintTypeface("roboto", "PhotoEditorTypefaceRoboto", new LazyTypeface(PaintTypeface$$ExternalSyntheticLambda9.INSTANCE));
        ROBOTO_MEDIUM = paintTypeface;
        PaintTypeface paintTypeface2 = new PaintTypeface("italic", "PhotoEditorTypefaceItalic", new LazyTypeface(PaintTypeface$$ExternalSyntheticLambda5.INSTANCE));
        ROBOTO_ITALIC = paintTypeface2;
        PaintTypeface paintTypeface3 = new PaintTypeface("serif", "PhotoEditorTypefaceSerif", new LazyTypeface(PaintTypeface$$ExternalSyntheticLambda6.INSTANCE));
        ROBOTO_SERIF = paintTypeface3;
        PaintTypeface paintTypeface4 = new PaintTypeface("condensed", "PhotoEditorTypefaceCondensed", new LazyTypeface(PaintTypeface$$ExternalSyntheticLambda4.INSTANCE));
        ROBOTO_CONDENSED = paintTypeface4;
        PaintTypeface paintTypeface5 = new PaintTypeface("mono", "PhotoEditorTypefaceMono", new LazyTypeface(PaintTypeface$$ExternalSyntheticLambda7.INSTANCE));
        ROBOTO_MONO = paintTypeface5;
        PaintTypeface paintTypeface6 = new PaintTypeface("mw_bold", "PhotoEditorTypefaceMerriweather", new LazyTypeface(PaintTypeface$$ExternalSyntheticLambda3.INSTANCE));
        MW_BOLD = paintTypeface6;
        PaintTypeface paintTypeface7 = new PaintTypeface("courier_new_bold", "PhotoEditorTypefaceCourierNew", new LazyTypeface(PaintTypeface$$ExternalSyntheticLambda8.INSTANCE));
        COURIER_NEW_BOLD = paintTypeface7;
        BUILT_IN_FONTS = Arrays.asList(paintTypeface, paintTypeface2, paintTypeface3, paintTypeface4, paintTypeface5, paintTypeface6, paintTypeface7);
        preferable = Arrays.asList("Google Sans", "Dancing Script", "Carrois Gothic SC", "Cutive Mono", "Droid Sans Mono", "Coming Soon");
    }

    /* loaded from: classes4.dex */
    private static class LazyTypeface {
        private final LazyTypefaceLoader loader;
        private Typeface typeface;

        /* loaded from: classes4.dex */
        public interface LazyTypefaceLoader {
            Typeface load();
        }

        public LazyTypeface(LazyTypefaceLoader lazyTypefaceLoader) {
            this.loader = lazyTypefaceLoader;
        }

        public Typeface get() {
            if (this.typeface == null) {
                this.typeface = this.loader.load();
            }
            return this.typeface;
        }
    }

    PaintTypeface(String str, String str2, LazyTypeface lazyTypeface) {
        this.key = str;
        this.nameKey = str2;
        this.name = null;
        this.typeface = null;
        this.lazyTypeface = lazyTypeface;
    }

    PaintTypeface(final Font font, String str) {
        this.key = str;
        this.name = str;
        this.nameKey = null;
        this.typeface = null;
        this.lazyTypeface = new LazyTypeface(new LazyTypeface.LazyTypefaceLoader() { // from class: org.telegram.ui.Components.Paint.PaintTypeface$$ExternalSyntheticLambda2
            @Override // org.telegram.ui.Components.Paint.PaintTypeface.LazyTypeface.LazyTypefaceLoader
            public final Typeface load() {
                Typeface lambda$new$7;
                lambda$new$7 = PaintTypeface.lambda$new$7(font);
                return lambda$new$7;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Typeface lambda$new$7(Font font) {
        return Typeface.createFromFile(font.getFile());
    }

    public String getKey() {
        return this.key;
    }

    public Typeface getTypeface() {
        LazyTypeface lazyTypeface = this.lazyTypeface;
        if (lazyTypeface != null) {
            return lazyTypeface.get();
        }
        return this.typeface;
    }

    public String getName() {
        String str = this.name;
        return str != null ? str : LocaleController.getString(this.nameKey);
    }

    private static void load() {
        if (typefaces != null || loadingTypefaces) {
            return;
        }
        loadingTypefaces = true;
        Utilities.themeQueue.postRunnable(PaintTypeface$$ExternalSyntheticLambda1.INSTANCE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$load$9() {
        FontData parseFont;
        final ArrayList arrayList = new ArrayList(BUILT_IN_FONTS);
        if (Build.VERSION.SDK_INT >= 29) {
            HashMap hashMap = new HashMap();
            for (Font font : SystemFonts.getAvailableFonts()) {
                if (!font.getFile().getName().contains("Noto") && (parseFont = parseFont(font)) != null) {
                    Family family = (Family) hashMap.get(parseFont.family);
                    if (family == null) {
                        family = new Family();
                        hashMap.put(parseFont.family, family);
                    }
                    family.fonts.add(parseFont);
                }
            }
            for (String str : preferable) {
                Family family2 = (Family) hashMap.get(str);
                if (family2 != null) {
                    FontData bold = family2.getBold();
                    if (bold == null) {
                        bold = family2.getRegular();
                    }
                    if (bold != null) {
                        arrayList.add(new PaintTypeface(bold.font, bold.getName()));
                    }
                }
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Paint.PaintTypeface$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PaintTypeface.lambda$load$8(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$load$8(ArrayList arrayList) {
        typefaces = arrayList;
        loadingTypefaces = false;
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.customTypefacesLoaded, new Object[0]);
    }

    public static List<PaintTypeface> get() {
        List<PaintTypeface> list = typefaces;
        if (list == null) {
            load();
            return BUILT_IN_FONTS;
        }
        return list;
    }

    public static PaintTypeface find(String str) {
        if (str != null && !TextUtils.isEmpty(str)) {
            List<PaintTypeface> list = get();
            for (int i = 0; i < list.size(); i++) {
                PaintTypeface paintTypeface = list.get(i);
                if (paintTypeface != null && TextUtils.equals(str, paintTypeface.key)) {
                    return paintTypeface;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class Family {
        ArrayList<FontData> fonts = new ArrayList<>();

        Family() {
        }

        public FontData getRegular() {
            FontData fontData;
            int i = 0;
            while (true) {
                if (i >= this.fonts.size()) {
                    fontData = null;
                    break;
                } else if ("Regular".equalsIgnoreCase(this.fonts.get(i).subfamily)) {
                    fontData = this.fonts.get(i);
                    break;
                } else {
                    i++;
                }
            }
            return (fontData != null || this.fonts.isEmpty()) ? fontData : this.fonts.get(0);
        }

        public FontData getBold() {
            for (int i = 0; i < this.fonts.size(); i++) {
                if ("Bold".equalsIgnoreCase(this.fonts.get(i).subfamily)) {
                    return this.fonts.get(i);
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class FontData {
        String family;
        Font font;
        String subfamily;

        FontData() {
        }

        public String getName() {
            if ("Regular".equals(this.subfamily) || TextUtils.isEmpty(this.subfamily)) {
                return this.family;
            }
            return this.family + " " + this.subfamily;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class NameRecord {
        final int encodingID;
        final int nameID;
        final int nameLength;
        final int stringOffset;

        public NameRecord(RandomAccessFile randomAccessFile) throws IOException {
            randomAccessFile.readUnsignedShort();
            this.encodingID = randomAccessFile.readUnsignedShort();
            randomAccessFile.readUnsignedShort();
            this.nameID = randomAccessFile.readUnsignedShort();
            this.nameLength = randomAccessFile.readUnsignedShort();
            this.stringOffset = randomAccessFile.readUnsignedShort();
        }

        public String read(RandomAccessFile randomAccessFile, int i) throws IOException {
            Charset charset;
            randomAccessFile.seek(i + this.stringOffset);
            byte[] bArr = new byte[this.nameLength];
            randomAccessFile.read(bArr);
            if (this.encodingID == 1) {
                charset = StandardCharsets.UTF_16BE;
            } else {
                charset = StandardCharsets.UTF_8;
            }
            return new String(bArr, charset);
        }
    }

    private static String parseString(RandomAccessFile randomAccessFile, int i, NameRecord nameRecord) throws IOException {
        if (nameRecord == null) {
            return null;
        }
        return nameRecord.read(randomAccessFile, i);
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x00a7, code lost:
        if (r2 == null) goto L42;
     */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00af A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static FontData parseFont(Font font) {
        File file;
        RandomAccessFile randomAccessFile;
        int readInt;
        RandomAccessFile randomAccessFile2 = null;
        if (font == null || (file = font.getFile()) == null) {
            return null;
        }
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            try {
                try {
                    readInt = randomAccessFile.readInt();
                } catch (Exception e) {
                    e = e;
                    FileLog.e(e);
                }
            } catch (Throwable th) {
                th = th;
                randomAccessFile2 = randomAccessFile;
                if (randomAccessFile2 != null) {
                    try {
                        randomAccessFile2.close();
                    } catch (Exception unused) {
                    }
                }
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            randomAccessFile = null;
        } catch (Throwable th2) {
            th = th2;
            if (randomAccessFile2 != null) {
            }
            throw th;
        }
        if (readInt == 65536 || readInt == 1330926671) {
            int readUnsignedShort = randomAccessFile.readUnsignedShort();
            randomAccessFile.skipBytes(6);
            for (int i = 0; i < readUnsignedShort; i++) {
                int readInt2 = randomAccessFile.readInt();
                randomAccessFile.skipBytes(4);
                int readInt3 = randomAccessFile.readInt();
                randomAccessFile.readInt();
                if (readInt2 == 1851878757) {
                    randomAccessFile.seek(readInt3 + 2);
                    int readUnsignedShort2 = randomAccessFile.readUnsignedShort();
                    int readUnsignedShort3 = randomAccessFile.readUnsignedShort();
                    HashMap hashMap = new HashMap();
                    for (int i2 = 0; i2 < readUnsignedShort2; i2++) {
                        NameRecord nameRecord = new NameRecord(randomAccessFile);
                        hashMap.put(Integer.valueOf(nameRecord.nameID), nameRecord);
                    }
                    FontData fontData = new FontData();
                    fontData.font = font;
                    int i3 = readInt3 + readUnsignedShort3;
                    fontData.family = parseString(randomAccessFile, i3, (NameRecord) hashMap.get(1));
                    fontData.subfamily = parseString(randomAccessFile, i3, (NameRecord) hashMap.get(2));
                    try {
                        randomAccessFile.close();
                    } catch (Exception unused2) {
                    }
                    return fontData;
                }
            }
            try {
                randomAccessFile.close();
            } catch (Exception unused3) {
                return null;
            }
        } else {
            try {
                randomAccessFile.close();
            } catch (Exception unused4) {
            }
            return null;
        }
    }
}
