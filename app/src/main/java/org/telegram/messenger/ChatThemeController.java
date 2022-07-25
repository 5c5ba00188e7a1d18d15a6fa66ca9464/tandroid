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
import org.telegram.messenger.NotificationBadge;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
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

    public static void requestAllChatThemes(final ResultCallback<List<EmojiThemes>> resultCallback, final boolean z) {
        if (themesHash == 0 || lastReloadTimeMs == 0) {
            init();
        }
        boolean z2 = System.currentTimeMillis() - lastReloadTimeMs > reloadTimeoutMs;
        List<EmojiThemes> list = allChatThemes;
        if (list == null || list.isEmpty() || z2) {
            TLRPC$TL_account_getChatThemes tLRPC$TL_account_getChatThemes = new TLRPC$TL_account_getChatThemes();
            tLRPC$TL_account_getChatThemes.hash = themesHash;
            ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(tLRPC$TL_account_getChatThemes, new RequestDelegate() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda6
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    ChatThemeController.lambda$requestAllChatThemes$3(ResultCallback.this, z, tLObject, tLRPC$TL_error);
                }
            });
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$requestAllChatThemes$3(final ResultCallback resultCallback, final boolean z, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        chatThemeQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ChatThemeController.lambda$requestAllChatThemes$2(TLObject.this, resultCallback, tLRPC$TL_error, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:11:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:23:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$requestAllChatThemes$2(TLObject tLObject, final ResultCallback resultCallback, final TLRPC$TL_error tLRPC$TL_error, boolean z) {
        final List<EmojiThemes> list;
        boolean z2;
        if (tLObject instanceof TLRPC$TL_account_themes) {
            TLRPC$TL_account_themes tLRPC$TL_account_themes = (TLRPC$TL_account_themes) tLObject;
            themesHash = tLRPC$TL_account_themes.hash;
            lastReloadTimeMs = System.currentTimeMillis();
            SharedPreferences.Editor edit = getSharedPreferences().edit();
            edit.clear();
            edit.putLong("hash", themesHash);
            edit.putLong("lastReload", lastReloadTimeMs);
            edit.putInt(NotificationBadge.NewHtcHomeBadger.COUNT, tLRPC$TL_account_themes.themes.size());
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    ResultCallback.this.onError(tLRPC$TL_error);
                }
            });
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ChatThemeController.lambda$requestAllChatThemes$1(list, resultCallback);
                }
            });
            return;
        }
        z2 = false;
        if (!z2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
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
        int i = sharedPreferences.getInt(NotificationBadge.NewHtcHomeBadger.COUNT, 0);
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

    public static void requestChatTheme(final String str, final ResultCallback<EmojiThemes> resultCallback) {
        if (TextUtils.isEmpty(str)) {
            resultCallback.onComplete(null);
        } else {
            requestAllChatThemes(new ResultCallback<List<EmojiThemes>>() { // from class: org.telegram.messenger.ChatThemeController.1
                public /* bridge */ /* synthetic */ void onError(Throwable th) {
                    ResultCallback.CC.$default$onError(this, th);
                }

                @Override // org.telegram.tgnet.ResultCallback
                public void onComplete(List<EmojiThemes> list) {
                    for (EmojiThemes emojiThemes : list) {
                        if (str.equals(emojiThemes.getEmoticon())) {
                            emojiThemes.initColors();
                            resultCallback.onComplete(emojiThemes);
                            return;
                        }
                    }
                }

                @Override // org.telegram.tgnet.ResultCallback
                public void onError(TLRPC$TL_error tLRPC$TL_error) {
                    resultCallback.onComplete(null);
                }
            }, false);
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$preloadAllWallpaperThumbs$4(Pair pair) {
        if (pair != null) {
            themeIdWallpaperThumbMap.put((Long) pair.first, (Bitmap) pair.second);
        }
    }

    public static void clearWallpaperThumbImages() {
        themeIdWallpaperThumbMap.clear();
    }

    public static void getWallpaperBitmap(long j, final ResultCallback<Bitmap> resultCallback) {
        if (themesHash == 0) {
            resultCallback.onComplete(null);
            return;
        }
        final File patternFile = getPatternFile(j);
        chatThemeQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ChatThemeController.lambda$getWallpaperBitmap$6(patternFile, resultCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getWallpaperBitmap$6(File file, final ResultCallback resultCallback) {
        final Bitmap bitmap = null;
        try {
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (resultCallback != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ResultCallback.this.onComplete(bitmap);
                }
            });
        }
    }

    private static File getPatternFile(long j) {
        return new File(ApplicationLoader.getFilesDirFixed(), String.format(Locale.US, "%d_%d.jpg", Long.valueOf(j), Long.valueOf(themesHash)));
    }

    public static void saveWallpaperBitmap(final Bitmap bitmap, long j) {
        final File patternFile = getPatternFile(j);
        chatThemeQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ChatThemeController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ChatThemeController.lambda$saveWallpaperBitmap$7(patternFile, bitmap);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
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
