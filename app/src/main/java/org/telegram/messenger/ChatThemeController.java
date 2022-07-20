package org.telegram.messenger;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.Pair;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.ResultCallback;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_account_getChatThemes;
import org.telegram.tgnet.TLRPC$TL_account_themes;
import org.telegram.tgnet.TLRPC$TL_account_themesNotModified;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_setChatTheme;
import org.telegram.tgnet.TLRPC$TL_theme;
import org.telegram.tgnet.TLRPC$Theme;
import org.telegram.ui.ActionBar.EmojiThemes;
/* loaded from: classes.dex */
public class ChatThemeController extends BaseController {
    private static List<EmojiThemes> allChatThemes = null;
    private static volatile long lastReloadTimeMs = 0;
    private static final long reloadTimeoutMs = 7200000;
    private static volatile long themesHash;
    private final LongSparseArray<String> dialogEmoticonsMap = new LongSparseArray<>();
    public static volatile DispatchQueue chatThemeQueue = new DispatchQueue("chatThemeQueue");
    private static final HashMap<Long, Bitmap> themeIdWallpaperThumbMap = new HashMap<>();
    private static final ChatThemeController[] instances = new ChatThemeController[4];

    public static void clearWallpaperImages() {
    }

    public static void init() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        themesHash = 0L;
        lastReloadTimeMs = 0L;
        try {
            themesHash = sharedPreferences.getLong("hash", 0L);
            lastReloadTimeMs = sharedPreferences.getLong("lastReload", 0L);
        } catch (Exception e) {
            FileLog.e(e);
        }
        allChatThemes = getAllChatThemesFromPrefs();
        preloadSticker("❌");
        if (!allChatThemes.isEmpty()) {
            for (EmojiThemes emojiThemes : allChatThemes) {
                preloadSticker(emojiThemes.getEmoticon());
            }
        }
    }

    private static void preloadSticker(String str) {
        new ImageReceiver().setImage(ImageLocation.getForDocument(MediaDataController.getInstance(UserConfig.selectedAccount).getEmojiAnimatedSticker(str)), "50_50", null, null, null, 0);
        Emoji.preloadEmoji(str);
    }

    public static void requestAllChatThemes(ResultCallback<List<EmojiThemes>> resultCallback, boolean z) {
        if (themesHash == 0 || lastReloadTimeMs == 0) {
            init();
        }
        boolean z2 = System.currentTimeMillis() - lastReloadTimeMs > 7200000;
        List<EmojiThemes> list = allChatThemes;
        if (list == null || list.isEmpty() || z2) {
            TLRPC$TL_account_getChatThemes tLRPC$TL_account_getChatThemes = new TLRPC$TL_account_getChatThemes();
            tLRPC$TL_account_getChatThemes.hash = themesHash;
            ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(tLRPC$TL_account_getChatThemes, new ChatThemeController$$ExternalSyntheticLambda6(resultCallback, z));
            return;
        }
        ArrayList<EmojiThemes> arrayList = new ArrayList(allChatThemes);
        if (z && !arrayList.get(0).showAsDefaultStub) {
            arrayList.add(0, EmojiThemes.createChatThemesDefault());
        }
        for (EmojiThemes emojiThemes : arrayList) {
            emojiThemes.initColors();
        }
        resultCallback.onComplete(arrayList);
    }

    public static /* synthetic */ void lambda$requestAllChatThemes$3(ResultCallback resultCallback, boolean z, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        chatThemeQueue.postRunnable(new ChatThemeController$$ExternalSyntheticLambda5(tLObject, resultCallback, tLRPC$TL_error, z));
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$requestAllChatThemes$2(TLObject tLObject, ResultCallback resultCallback, TLRPC$TL_error tLRPC$TL_error, boolean z) {
        boolean z2;
        List<EmojiThemes> list;
        if (tLObject instanceof TLRPC$TL_account_themes) {
            TLRPC$TL_account_themes tLRPC$TL_account_themes = (TLRPC$TL_account_themes) tLObject;
            themesHash = tLRPC$TL_account_themes.hash;
            lastReloadTimeMs = System.currentTimeMillis();
            SharedPreferences.Editor edit = getSharedPreferences().edit();
            edit.clear();
            edit.putLong("hash", themesHash);
            edit.putLong("lastReload", lastReloadTimeMs);
            edit.putInt("count", tLRPC$TL_account_themes.themes.size());
            list = new ArrayList(tLRPC$TL_account_themes.themes.size());
            for (int i = 0; i < tLRPC$TL_account_themes.themes.size(); i++) {
                TLRPC$TL_theme tLRPC$TL_theme = tLRPC$TL_account_themes.themes.get(i);
                Emoji.preloadEmoji(tLRPC$TL_theme.emoticon);
                SerializedData serializedData = new SerializedData(tLRPC$TL_theme.getObjectSize());
                tLRPC$TL_theme.serializeToStream(serializedData);
                edit.putString("theme_" + i, Utilities.bytesToHex(serializedData.toByteArray()));
                EmojiThemes emojiThemes = new EmojiThemes(tLRPC$TL_theme, false);
                emojiThemes.preloadWallpaper();
                list.add(emojiThemes);
            }
            edit.apply();
        } else if (tLObject instanceof TLRPC$TL_account_themesNotModified) {
            list = getAllChatThemesFromPrefs();
        } else {
            list = null;
            AndroidUtilities.runOnUIThread(new ChatThemeController$$ExternalSyntheticLambda4(resultCallback, tLRPC$TL_error));
            z2 = true;
            if (!z2) {
                return;
            }
            if (z && !((EmojiThemes) list.get(0)).showAsDefaultStub) {
                list.add(0, EmojiThemes.createChatThemesDefault());
            }
            for (EmojiThemes emojiThemes2 : list) {
                emojiThemes2.initColors();
            }
            AndroidUtilities.runOnUIThread(new ChatThemeController$$ExternalSyntheticLambda2(list, resultCallback));
            return;
        }
        z2 = false;
        if (!z2) {
        }
    }

    public static /* synthetic */ void lambda$requestAllChatThemes$1(List list, ResultCallback resultCallback) {
        allChatThemes = new ArrayList(list);
        resultCallback.onComplete(list);
    }

    private static SharedPreferences getSharedPreferences() {
        return ApplicationLoader.applicationContext.getSharedPreferences("chatthemeconfig", 0);
    }

    private static SharedPreferences getEmojiSharedPreferences() {
        return ApplicationLoader.applicationContext.getSharedPreferences("chatthemeconfig_emoji", 0);
    }

    private static List<EmojiThemes> getAllChatThemesFromPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        int i = sharedPreferences.getInt("count", 0);
        ArrayList arrayList = new ArrayList(i);
        for (int i2 = 0; i2 < i; i2++) {
            SerializedData serializedData = new SerializedData(Utilities.hexToBytes(sharedPreferences.getString("theme_" + i2, "")));
            try {
                TLRPC$TL_theme TLdeserialize = TLRPC$Theme.TLdeserialize(serializedData, serializedData.readInt32(true), true);
                if (TLdeserialize != null) {
                    arrayList.add(new EmojiThemes(TLdeserialize, false));
                }
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
        return arrayList;
    }

    public static void requestChatTheme(String str, ResultCallback<EmojiThemes> resultCallback) {
        if (TextUtils.isEmpty(str)) {
            resultCallback.onComplete(null);
        } else {
            requestAllChatThemes(new AnonymousClass1(str, resultCallback), false);
        }
    }

    /* renamed from: org.telegram.messenger.ChatThemeController$1 */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements ResultCallback<List<EmojiThemes>> {
        final /* synthetic */ ResultCallback val$callback;
        final /* synthetic */ String val$emoticon;

        public /* bridge */ /* synthetic */ void onError(Throwable th) {
            ResultCallback.CC.$default$onError(this, th);
        }

        AnonymousClass1(String str, ResultCallback resultCallback) {
            this.val$emoticon = str;
            this.val$callback = resultCallback;
        }

        public void onComplete(List<EmojiThemes> list) {
            for (EmojiThemes emojiThemes : list) {
                if (this.val$emoticon.equals(emojiThemes.getEmoticon())) {
                    emojiThemes.initColors();
                    this.val$callback.onComplete(emojiThemes);
                    return;
                }
            }
        }

        @Override // org.telegram.tgnet.ResultCallback
        public void onError(TLRPC$TL_error tLRPC$TL_error) {
            this.val$callback.onComplete(null);
        }
    }

    public static ChatThemeController getInstance(int i) {
        ChatThemeController[] chatThemeControllerArr = instances;
        ChatThemeController chatThemeController = chatThemeControllerArr[i];
        if (chatThemeController == null) {
            synchronized (ChatThemeController.class) {
                chatThemeController = chatThemeControllerArr[i];
                if (chatThemeController == null) {
                    chatThemeController = new ChatThemeController(i);
                    chatThemeControllerArr[i] = chatThemeController;
                }
            }
        }
        return chatThemeController;
    }

    public ChatThemeController(int i) {
        super(i);
    }

    public void setDialogTheme(long j, String str, boolean z) {
        if (TextUtils.equals(this.dialogEmoticonsMap.get(j), str)) {
            return;
        }
        if (str == null) {
            this.dialogEmoticonsMap.delete(j);
        } else {
            this.dialogEmoticonsMap.put(j, str);
        }
        SharedPreferences.Editor edit = getEmojiSharedPreferences().edit();
        edit.putString("chatTheme_" + this.currentAccount + "_" + j, str).apply();
        if (!z) {
            return;
        }
        TLRPC$TL_messages_setChatTheme tLRPC$TL_messages_setChatTheme = new TLRPC$TL_messages_setChatTheme();
        if (str == null) {
            str = "";
        }
        tLRPC$TL_messages_setChatTheme.emoticon = str;
        tLRPC$TL_messages_setChatTheme.peer = getMessagesController().getInputPeer(j);
        getConnectionsManager().sendRequest(tLRPC$TL_messages_setChatTheme, null);
    }

    public EmojiThemes getDialogTheme(long j) {
        String str = this.dialogEmoticonsMap.get(j);
        if (str == null) {
            SharedPreferences emojiSharedPreferences = getEmojiSharedPreferences();
            str = emojiSharedPreferences.getString("chatTheme_" + this.currentAccount + "_" + j, null);
            this.dialogEmoticonsMap.put(j, str);
        }
        if (str != null) {
            for (EmojiThemes emojiThemes : allChatThemes) {
                if (str.equals(emojiThemes.getEmoticon())) {
                    return emojiThemes;
                }
            }
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void preloadAllWallpaperImages(boolean z) {
        for (EmojiThemes emojiThemes : allChatThemes) {
            TLRPC$TL_theme tlTheme = emojiThemes.getTlTheme(z ? 1 : 0);
            if (tlTheme != null && !getPatternFile(tlTheme.id).exists()) {
                emojiThemes.loadWallpaper(z, null);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void preloadAllWallpaperThumbs(boolean z) {
        for (EmojiThemes emojiThemes : allChatThemes) {
            TLRPC$TL_theme tlTheme = emojiThemes.getTlTheme(z ? 1 : 0);
            if (tlTheme != null) {
                if (!themeIdWallpaperThumbMap.containsKey(Long.valueOf(tlTheme.id))) {
                    emojiThemes.loadWallpaperThumb(z, ChatThemeController$$ExternalSyntheticLambda7.INSTANCE);
                }
            }
        }
    }

    public static /* synthetic */ void lambda$preloadAllWallpaperThumbs$4(Pair pair) {
        if (pair != null) {
            themeIdWallpaperThumbMap.put((Long) pair.first, (Bitmap) pair.second);
        }
    }

    public static void clearWallpaperThumbImages() {
        themeIdWallpaperThumbMap.clear();
    }

    public static void getWallpaperBitmap(long j, ResultCallback<Bitmap> resultCallback) {
        if (themesHash == 0) {
            resultCallback.onComplete(null);
            return;
        }
        chatThemeQueue.postRunnable(new ChatThemeController$$ExternalSyntheticLambda1(getPatternFile(j), resultCallback));
    }

    public static /* synthetic */ void lambda$getWallpaperBitmap$6(File file, ResultCallback resultCallback) {
        Bitmap bitmap = null;
        try {
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (resultCallback != null) {
            AndroidUtilities.runOnUIThread(new ChatThemeController$$ExternalSyntheticLambda3(resultCallback, bitmap));
        }
    }

    private static File getPatternFile(long j) {
        return new File(ApplicationLoader.getFilesDirFixed(), String.format(Locale.US, "%d_%d.jpg", Long.valueOf(j), Long.valueOf(themesHash)));
    }

    public static void saveWallpaperBitmap(Bitmap bitmap, long j) {
        chatThemeQueue.postRunnable(new ChatThemeController$$ExternalSyntheticLambda0(getPatternFile(j), bitmap));
    }

    public static /* synthetic */ void lambda$saveWallpaperBitmap$7(File file, Bitmap bitmap) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 87, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static Bitmap getWallpaperThumbBitmap(long j) {
        return themeIdWallpaperThumbMap.get(Long.valueOf(j));
    }

    public void clearCache() {
        themesHash = 0L;
        lastReloadTimeMs = 0L;
        getSharedPreferences().edit().clear().apply();
    }
}
