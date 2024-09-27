package org.telegram.messenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Base64;
import android.util.LongSparseArray;
import java.util.Arrays;
import org.telegram.messenger.SaveToGallerySettingsHelper;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
/* loaded from: classes.dex */
public class UserConfig extends BaseController {
    private static volatile UserConfig[] Instance = new UserConfig[4];
    public static final int MAX_ACCOUNT_COUNT = 4;
    public static final int MAX_ACCOUNT_DEFAULT_COUNT = 3;
    public static final int i_dialogsLoadOffsetAccess = 5;
    public static final int i_dialogsLoadOffsetChannelId = 4;
    public static final int i_dialogsLoadOffsetChatId = 3;
    public static final int i_dialogsLoadOffsetDate = 1;
    public static final int i_dialogsLoadOffsetId = 0;
    public static final int i_dialogsLoadOffsetUserId = 2;
    public static int selectedAccount;
    public long autoDownloadConfigLoadTime;
    public int botRatingLoadTime;
    LongSparseArray<SaveToGallerySettingsHelper.DialogException> chanelSaveGalleryExceptions;
    public long clientUserId;
    private volatile boolean configLoaded;
    public boolean contactsReimported;
    public int contactsSavedCount;
    private TLRPC.User currentUser;
    public String defaultTopicIcons;
    public boolean draftsLoaded;
    public boolean filtersLoaded;
    public String genericAnimationsStickerPack;
    int globalTtl;
    LongSparseArray<SaveToGallerySettingsHelper.DialogException> groupsSaveGalleryExceptions;
    public boolean hasSecureData;
    public boolean hasValidDialogLoadIds;
    public int lastBroadcastId;
    public int lastContactsSyncTime;
    public int lastHintsSyncTime;
    long lastLoadingTime;
    public int lastMyLocationShareTime;
    public int lastSendMessageId;
    public long lastUpdatedDefaultTopicIcons;
    public long lastUpdatedGenericAnimations;
    public long lastUpdatedPremiumGiftsStickerPack;
    public int loginTime;
    public long migrateOffsetAccess;
    public long migrateOffsetChannelId;
    public long migrateOffsetChatId;
    public int migrateOffsetDate;
    public int migrateOffsetId;
    public long migrateOffsetUserId;
    public boolean notificationsSettingsLoaded;
    public boolean notificationsSignUpSettingsLoaded;
    public String premiumGiftsStickerPack;
    public int ratingLoadTime;
    public boolean registeredForPush;
    public volatile byte[] savedPasswordHash;
    public volatile long savedPasswordTime;
    public volatile byte[] savedSaltedPassword;
    public int sharingMyLocationUntil;
    public boolean suggestContacts;
    private final Object sync;
    public boolean syncContacts;
    public TLRPC.TL_account_tmpPassword tmpPassword;
    boolean ttlIsLoading;
    public TLRPC.TL_help_termsOfService unacceptedTermsOfService;
    public boolean unreadDialogsLoaded;
    LongSparseArray<SaveToGallerySettingsHelper.DialogException> userSaveGalleryExceptions;
    public int webappRatingLoadTime;

    public UserConfig(int i) {
        super(i);
        this.sync = new Object();
        this.lastSendMessageId = -210000;
        this.lastBroadcastId = -1;
        this.unreadDialogsLoaded = true;
        this.migrateOffsetId = -1;
        this.migrateOffsetDate = -1;
        this.migrateOffsetUserId = -1L;
        this.migrateOffsetChatId = -1L;
        this.migrateOffsetChannelId = -1L;
        this.migrateOffsetAccess = -1L;
        this.syncContacts = true;
        this.suggestContacts = true;
        this.globalTtl = 0;
        this.ttlIsLoading = false;
    }

    private void checkPremiumSelf(TLRPC.User user, final TLRPC.User user2) {
        if (user == null || !(user2 == null || user.premium == user2.premium)) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.UserConfig$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    UserConfig.this.lambda$checkPremiumSelf$1(user2);
                }
            });
        }
    }

    public static int getActivatedAccountsCount() {
        int i = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            if (AccountInstance.getInstance(i2).getUserConfig().isClientActivated()) {
                i++;
            }
        }
        return i;
    }

    public static UserConfig getInstance(int i) {
        UserConfig userConfig = Instance[i];
        if (userConfig == null) {
            synchronized (UserConfig.class) {
                try {
                    userConfig = Instance[i];
                    if (userConfig == null) {
                        UserConfig[] userConfigArr = Instance;
                        UserConfig userConfig2 = new UserConfig(i);
                        userConfigArr[i] = userConfig2;
                        userConfig = userConfig2;
                    }
                } finally {
                }
            }
        }
        return userConfig;
    }

    public static int getMaxAccountCount() {
        return hasPremiumOnAccounts() ? 5 : 3;
    }

    public static boolean hasPremiumOnAccounts() {
        for (int i = 0; i < 4; i++) {
            if (AccountInstance.getInstance(i).getUserConfig().isClientActivated() && AccountInstance.getInstance(i).getUserConfig().getUserConfig().isPremium()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidAccount(int i) {
        return i >= 0 && i < 4 && getInstance(i).isClientActivated();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkPremiumSelf$1(TLRPC.User user) {
        getMessagesController().updatePremium(user.premium);
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.currentUserPremiumStatusChanged, new Object[0]);
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.premiumStatusChangedGlobal, new Object[0]);
        getMediaDataController().loadPremiumPromo(false);
        getMediaDataController().loadReactions(false, null);
        getMessagesController().getStoriesController().invalidateStoryLimit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadGlobalTTl$2(TLObject tLObject) {
        if (tLObject != null) {
            this.globalTtl = ((TLRPC.TL_defaultHistoryTTL) tLObject).period / 60;
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didUpdateGlobalAutoDeleteTimer, new Object[0]);
            this.ttlIsLoading = false;
            this.lastLoadingTime = System.currentTimeMillis();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadGlobalTTl$3(final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.UserConfig$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                UserConfig.this.lambda$loadGlobalTTl$2(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveConfig$0(boolean z) {
        if (this.configLoaded) {
            synchronized (this.sync) {
                try {
                    try {
                        SharedPreferences.Editor edit = getPreferences().edit();
                        if (this.currentAccount == 0) {
                            edit.putInt("selectedAccount", selectedAccount);
                        }
                        edit.putBoolean("registeredForPush", this.registeredForPush);
                        edit.putInt("lastSendMessageId", this.lastSendMessageId);
                        edit.putInt("contactsSavedCount", this.contactsSavedCount);
                        edit.putInt("lastBroadcastId", this.lastBroadcastId);
                        edit.putInt("lastContactsSyncTime", this.lastContactsSyncTime);
                        edit.putInt("lastHintsSyncTime", this.lastHintsSyncTime);
                        edit.putBoolean("draftsLoaded", this.draftsLoaded);
                        edit.putBoolean("unreadDialogsLoaded", this.unreadDialogsLoaded);
                        edit.putInt("ratingLoadTime", this.ratingLoadTime);
                        edit.putInt("botRatingLoadTime", this.botRatingLoadTime);
                        edit.putInt("webappRatingLoadTime", this.webappRatingLoadTime);
                        edit.putBoolean("contactsReimported", this.contactsReimported);
                        edit.putInt("loginTime", this.loginTime);
                        edit.putBoolean("syncContacts", this.syncContacts);
                        edit.putBoolean("suggestContacts", this.suggestContacts);
                        edit.putBoolean("hasSecureData", this.hasSecureData);
                        edit.putBoolean("notificationsSettingsLoaded4", this.notificationsSettingsLoaded);
                        edit.putBoolean("notificationsSignUpSettingsLoaded", this.notificationsSignUpSettingsLoaded);
                        edit.putLong("autoDownloadConfigLoadTime", this.autoDownloadConfigLoadTime);
                        edit.putBoolean("hasValidDialogLoadIds", this.hasValidDialogLoadIds);
                        edit.putInt("sharingMyLocationUntil", this.sharingMyLocationUntil);
                        edit.putInt("lastMyLocationShareTime", this.lastMyLocationShareTime);
                        edit.putBoolean("filtersLoaded", this.filtersLoaded);
                        edit.putString("premiumGiftsStickerPack", this.premiumGiftsStickerPack);
                        edit.putLong("lastUpdatedPremiumGiftsStickerPack", this.lastUpdatedPremiumGiftsStickerPack);
                        edit.putString("genericAnimationsStickerPack", this.genericAnimationsStickerPack);
                        edit.putLong("lastUpdatedGenericAnimations", this.lastUpdatedGenericAnimations);
                        edit.putInt("6migrateOffsetId", this.migrateOffsetId);
                        if (this.migrateOffsetId != -1) {
                            edit.putInt("6migrateOffsetDate", this.migrateOffsetDate);
                            edit.putLong("6migrateOffsetUserId", this.migrateOffsetUserId);
                            edit.putLong("6migrateOffsetChatId", this.migrateOffsetChatId);
                            edit.putLong("6migrateOffsetChannelId", this.migrateOffsetChannelId);
                            edit.putLong("6migrateOffsetAccess", this.migrateOffsetAccess);
                        }
                        TLRPC.TL_help_termsOfService tL_help_termsOfService = this.unacceptedTermsOfService;
                        if (tL_help_termsOfService != null) {
                            try {
                                SerializedData serializedData = new SerializedData(tL_help_termsOfService.getObjectSize());
                                this.unacceptedTermsOfService.serializeToStream(serializedData);
                                edit.putString("terms", Base64.encodeToString(serializedData.toByteArray(), 0));
                                serializedData.cleanup();
                            } catch (Exception unused) {
                            }
                        } else {
                            edit.remove("terms");
                        }
                        SharedConfig.saveConfig();
                        if (this.tmpPassword != null) {
                            SerializedData serializedData2 = new SerializedData();
                            this.tmpPassword.serializeToStream(serializedData2);
                            edit.putString("tmpPassword", Base64.encodeToString(serializedData2.toByteArray(), 0));
                            serializedData2.cleanup();
                        } else {
                            edit.remove("tmpPassword");
                        }
                        if (this.currentUser == null) {
                            edit.remove("user");
                        } else if (z) {
                            SerializedData serializedData3 = new SerializedData();
                            this.currentUser.serializeToStream(serializedData3);
                            edit.putString("user", Base64.encodeToString(serializedData3.toByteArray(), 0));
                            serializedData3.cleanup();
                        }
                        edit.apply();
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public void checkSavedPassword() {
        if (!(this.savedSaltedPassword == null && this.savedPasswordHash == null) && Math.abs(SystemClock.elapsedRealtime() - this.savedPasswordTime) >= 1800000) {
            resetSavedPassword();
        }
    }

    public void clearConfig() {
        getPreferences().edit().clear().apply();
        int i = 0;
        this.sharingMyLocationUntil = 0;
        this.lastMyLocationShareTime = 0;
        this.currentUser = null;
        this.clientUserId = 0L;
        this.registeredForPush = false;
        this.contactsSavedCount = 0;
        this.lastSendMessageId = -210000;
        this.lastBroadcastId = -1;
        this.notificationsSettingsLoaded = false;
        this.notificationsSignUpSettingsLoaded = false;
        this.migrateOffsetId = -1;
        this.migrateOffsetDate = -1;
        this.migrateOffsetUserId = -1L;
        this.migrateOffsetChatId = -1L;
        this.migrateOffsetChannelId = -1L;
        this.migrateOffsetAccess = -1L;
        this.ratingLoadTime = 0;
        this.botRatingLoadTime = 0;
        this.webappRatingLoadTime = 0;
        this.draftsLoaded = false;
        this.contactsReimported = true;
        this.syncContacts = true;
        this.suggestContacts = true;
        this.unreadDialogsLoaded = true;
        this.hasValidDialogLoadIds = true;
        this.unacceptedTermsOfService = null;
        this.filtersLoaded = false;
        this.hasSecureData = false;
        this.loginTime = (int) (System.currentTimeMillis() / 1000);
        this.lastContactsSyncTime = ((int) (System.currentTimeMillis() / 1000)) - 82800;
        this.lastHintsSyncTime = ((int) (System.currentTimeMillis() / 1000)) - 90000;
        resetSavedPassword();
        while (true) {
            if (i >= 4) {
                SharedConfig.clearConfig();
                break;
            } else if (AccountInstance.getInstance(i).getUserConfig().isClientActivated()) {
                break;
            } else {
                i++;
            }
        }
        saveConfig(true);
    }

    public void clearFilters() {
        getPreferences().edit().remove("filtersLoaded").apply();
        this.filtersLoaded = false;
    }

    public void clearPinnedDialogsLoaded() {
        SharedPreferences.Editor edit = getPreferences().edit();
        for (String str : getPreferences().getAll().keySet()) {
            if (str.startsWith("2pinnedDialogsLoaded")) {
                edit.remove(str);
            }
        }
        edit.apply();
    }

    public String getClientPhone() {
        String str;
        synchronized (this.sync) {
            try {
                TLRPC.User user = this.currentUser;
                if (user == null || (str = user.phone) == null) {
                    str = "";
                }
            } finally {
            }
        }
        return str;
    }

    public long getClientUserId() {
        long j;
        synchronized (this.sync) {
            try {
                TLRPC.User user = this.currentUser;
                j = user != null ? user.id : 0L;
            } catch (Throwable th) {
                throw th;
            }
        }
        return j;
    }

    public TLRPC.User getCurrentUser() {
        TLRPC.User user;
        synchronized (this.sync) {
            user = this.currentUser;
        }
        return user;
    }

    public long[] getDialogLoadOffsets(int i) {
        SharedPreferences preferences = getPreferences();
        StringBuilder sb = new StringBuilder();
        sb.append("2dialogsLoadOffsetId");
        sb.append(i == 0 ? "" : Integer.valueOf(i));
        int i2 = preferences.getInt(sb.toString(), this.hasValidDialogLoadIds ? 0 : -1);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("2dialogsLoadOffsetDate");
        sb2.append(i == 0 ? "" : Integer.valueOf(i));
        int i3 = preferences.getInt(sb2.toString(), this.hasValidDialogLoadIds ? 0 : -1);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("2dialogsLoadOffsetUserId");
        sb3.append(i == 0 ? "" : Integer.valueOf(i));
        long prefIntOrLong = AndroidUtilities.getPrefIntOrLong(preferences, sb3.toString(), this.hasValidDialogLoadIds ? 0L : -1L);
        StringBuilder sb4 = new StringBuilder();
        sb4.append("2dialogsLoadOffsetChatId");
        sb4.append(i == 0 ? "" : Integer.valueOf(i));
        long prefIntOrLong2 = AndroidUtilities.getPrefIntOrLong(preferences, sb4.toString(), this.hasValidDialogLoadIds ? 0L : -1L);
        StringBuilder sb5 = new StringBuilder();
        sb5.append("2dialogsLoadOffsetChannelId");
        sb5.append(i == 0 ? "" : Integer.valueOf(i));
        long prefIntOrLong3 = AndroidUtilities.getPrefIntOrLong(preferences, sb5.toString(), this.hasValidDialogLoadIds ? 0L : -1L);
        StringBuilder sb6 = new StringBuilder();
        sb6.append("2dialogsLoadOffsetAccess");
        sb6.append(i != 0 ? Integer.valueOf(i) : "");
        return new long[]{i2, i3, prefIntOrLong, prefIntOrLong2, prefIntOrLong3, preferences.getLong(sb6.toString(), this.hasValidDialogLoadIds ? 0L : -1L)};
    }

    public Long getEmojiStatus() {
        return UserObject.getEmojiStatusDocumentId(this.currentUser);
    }

    public int getGlobalTTl() {
        return this.globalTtl;
    }

    public int getNewMessageId() {
        int i;
        synchronized (this.sync) {
            i = this.lastSendMessageId;
            this.lastSendMessageId = i - 1;
        }
        return i;
    }

    public SharedPreferences getPreferences() {
        Context context;
        String str;
        if (this.currentAccount == 0) {
            context = ApplicationLoader.applicationContext;
            str = "userconfing";
        } else {
            context = ApplicationLoader.applicationContext;
            str = "userconfig" + this.currentAccount;
        }
        return context.getSharedPreferences(str, 0);
    }

    public LongSparseArray<SaveToGallerySettingsHelper.DialogException> getSaveGalleryExceptions(int i) {
        if (i == 1) {
            if (this.userSaveGalleryExceptions == null) {
                Context context = ApplicationLoader.applicationContext;
                this.userSaveGalleryExceptions = SaveToGallerySettingsHelper.loadExceptions(context.getSharedPreferences(SaveToGallerySettingsHelper.USERS_PREF_NAME + "_" + this.currentAccount, 0));
            }
            return this.userSaveGalleryExceptions;
        } else if (i == 2) {
            if (this.groupsSaveGalleryExceptions == null) {
                Context context2 = ApplicationLoader.applicationContext;
                this.groupsSaveGalleryExceptions = SaveToGallerySettingsHelper.loadExceptions(context2.getSharedPreferences(SaveToGallerySettingsHelper.GROUPS_PREF_NAME + "_" + this.currentAccount, 0));
            }
            return this.groupsSaveGalleryExceptions;
        } else if (i == 4) {
            if (this.chanelSaveGalleryExceptions == null) {
                Context context3 = ApplicationLoader.applicationContext;
                this.chanelSaveGalleryExceptions = SaveToGallerySettingsHelper.loadExceptions(context3.getSharedPreferences(SaveToGallerySettingsHelper.CHANNELS_PREF_NAME + "_" + this.currentAccount, 0));
            }
            return this.chanelSaveGalleryExceptions;
        } else {
            return null;
        }
    }

    public int getTotalDialogsCount(int i) {
        SharedPreferences preferences = getPreferences();
        StringBuilder sb = new StringBuilder();
        sb.append("2totalDialogsLoadCount");
        sb.append(i == 0 ? "" : Integer.valueOf(i));
        return preferences.getInt(sb.toString(), 0);
    }

    public boolean isClientActivated() {
        boolean z;
        synchronized (this.sync) {
            z = this.currentUser != null;
        }
        return z;
    }

    public boolean isConfigLoaded() {
        return this.configLoaded;
    }

    public boolean isPinnedDialogsLoaded(int i) {
        SharedPreferences preferences = getPreferences();
        return preferences.getBoolean("2pinnedDialogsLoaded" + i, false);
    }

    public boolean isPremium() {
        TLRPC.User user = this.currentUser;
        if (user == null) {
            return false;
        }
        return user.premium;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(20:9|(1:11)|12|(16:17|18|19|20|(1:24)|26|(1:28)|29|(1:33)|34|(1:38)|39|(1:41)|42|43|44)|47|18|19|20|(2:22|24)|26|(0)|29|(2:31|33)|34|(2:36|38)|39|(0)|42|43|44) */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0144, code lost:
        r2 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0145, code lost:
        org.telegram.messenger.FileLog.e(r2);
     */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0152 A[Catch: all -> 0x0009, TryCatch #0 {all -> 0x0009, blocks: (B:4:0x0003, B:6:0x0007, B:10:0x000c, B:12:0x0015, B:13:0x001d, B:15:0x00dc, B:20:0x00e8, B:21:0x0123, B:23:0x012b, B:25:0x0131, B:29:0x0148, B:31:0x0152, B:32:0x017a, B:34:0x0182, B:36:0x0188, B:37:0x019a, B:39:0x01a2, B:41:0x01a8, B:42:0x01ba, B:44:0x01be, B:45:0x01c7, B:46:0x01c9, B:28:0x0145), top: B:50:0x0003, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x01be A[Catch: all -> 0x0009, TryCatch #0 {all -> 0x0009, blocks: (B:4:0x0003, B:6:0x0007, B:10:0x000c, B:12:0x0015, B:13:0x001d, B:15:0x00dc, B:20:0x00e8, B:21:0x0123, B:23:0x012b, B:25:0x0131, B:29:0x0148, B:31:0x0152, B:32:0x017a, B:34:0x0182, B:36:0x0188, B:37:0x019a, B:39:0x01a2, B:41:0x01a8, B:42:0x01ba, B:44:0x01be, B:45:0x01c7, B:46:0x01c9, B:28:0x0145), top: B:50:0x0003, inners: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadConfig() {
        boolean z;
        int i;
        String string;
        String string2;
        TLRPC.User user;
        byte[] decode;
        byte[] decode2;
        String string3;
        byte[] decode3;
        synchronized (this.sync) {
            try {
                if (this.configLoaded) {
                    return;
                }
                SharedPreferences preferences = getPreferences();
                if (this.currentAccount == 0) {
                    selectedAccount = preferences.getInt("selectedAccount", 0);
                }
                this.registeredForPush = preferences.getBoolean("registeredForPush", false);
                this.lastSendMessageId = preferences.getInt("lastSendMessageId", -210000);
                this.contactsSavedCount = preferences.getInt("contactsSavedCount", 0);
                this.lastBroadcastId = preferences.getInt("lastBroadcastId", -1);
                this.lastContactsSyncTime = preferences.getInt("lastContactsSyncTime", ((int) (System.currentTimeMillis() / 1000)) - 82800);
                this.lastHintsSyncTime = preferences.getInt("lastHintsSyncTime", ((int) (System.currentTimeMillis() / 1000)) - 90000);
                this.draftsLoaded = preferences.getBoolean("draftsLoaded", false);
                this.unreadDialogsLoaded = preferences.getBoolean("unreadDialogsLoaded", false);
                this.contactsReimported = preferences.getBoolean("contactsReimported", false);
                this.ratingLoadTime = preferences.getInt("ratingLoadTime", 0);
                this.botRatingLoadTime = preferences.getInt("botRatingLoadTime", 0);
                this.webappRatingLoadTime = preferences.getInt("webappRatingLoadTime", 0);
                this.loginTime = preferences.getInt("loginTime", this.currentAccount);
                this.syncContacts = preferences.getBoolean("syncContacts", true);
                this.suggestContacts = preferences.getBoolean("suggestContacts", true);
                this.hasSecureData = preferences.getBoolean("hasSecureData", false);
                this.notificationsSettingsLoaded = preferences.getBoolean("notificationsSettingsLoaded4", false);
                this.notificationsSignUpSettingsLoaded = preferences.getBoolean("notificationsSignUpSettingsLoaded", false);
                this.autoDownloadConfigLoadTime = preferences.getLong("autoDownloadConfigLoadTime", 0L);
                if (!preferences.contains("2dialogsLoadOffsetId") && !preferences.getBoolean("hasValidDialogLoadIds", false)) {
                    z = false;
                    this.hasValidDialogLoadIds = z;
                    this.sharingMyLocationUntil = preferences.getInt("sharingMyLocationUntil", 0);
                    this.lastMyLocationShareTime = preferences.getInt("lastMyLocationShareTime", 0);
                    this.filtersLoaded = preferences.getBoolean("filtersLoaded", false);
                    this.premiumGiftsStickerPack = preferences.getString("premiumGiftsStickerPack", null);
                    this.lastUpdatedPremiumGiftsStickerPack = preferences.getLong("lastUpdatedPremiumGiftsStickerPack", 0L);
                    this.genericAnimationsStickerPack = preferences.getString("genericAnimationsStickerPack", null);
                    this.lastUpdatedGenericAnimations = preferences.getLong("lastUpdatedGenericAnimations", 0L);
                    string3 = preferences.getString("terms", null);
                    if (string3 != null && (decode3 = Base64.decode(string3, 0)) != null) {
                        SerializedData serializedData = new SerializedData(decode3);
                        this.unacceptedTermsOfService = TLRPC.TL_help_termsOfService.TLdeserialize(serializedData, serializedData.readInt32(false), false);
                        serializedData.cleanup();
                    }
                    i = preferences.getInt("6migrateOffsetId", 0);
                    this.migrateOffsetId = i;
                    if (i != -1) {
                        this.migrateOffsetDate = preferences.getInt("6migrateOffsetDate", 0);
                        this.migrateOffsetUserId = AndroidUtilities.getPrefIntOrLong(preferences, "6migrateOffsetUserId", 0L);
                        this.migrateOffsetChatId = AndroidUtilities.getPrefIntOrLong(preferences, "6migrateOffsetChatId", 0L);
                        this.migrateOffsetChannelId = AndroidUtilities.getPrefIntOrLong(preferences, "6migrateOffsetChannelId", 0L);
                        this.migrateOffsetAccess = preferences.getLong("6migrateOffsetAccess", 0L);
                    }
                    string = preferences.getString("tmpPassword", null);
                    if (string != null && (decode2 = Base64.decode(string, 0)) != null) {
                        SerializedData serializedData2 = new SerializedData(decode2);
                        this.tmpPassword = TLRPC.TL_account_tmpPassword.TLdeserialize(serializedData2, serializedData2.readInt32(false), false);
                        serializedData2.cleanup();
                    }
                    string2 = preferences.getString("user", null);
                    if (string2 != null && (decode = Base64.decode(string2, 0)) != null) {
                        SerializedData serializedData3 = new SerializedData(decode);
                        this.currentUser = TLRPC.User.TLdeserialize(serializedData3, serializedData3.readInt32(false), false);
                        serializedData3.cleanup();
                    }
                    user = this.currentUser;
                    if (user != null) {
                        checkPremiumSelf(null, user);
                        this.clientUserId = this.currentUser.id;
                    }
                    this.configLoaded = true;
                }
                z = true;
                this.hasValidDialogLoadIds = z;
                this.sharingMyLocationUntil = preferences.getInt("sharingMyLocationUntil", 0);
                this.lastMyLocationShareTime = preferences.getInt("lastMyLocationShareTime", 0);
                this.filtersLoaded = preferences.getBoolean("filtersLoaded", false);
                this.premiumGiftsStickerPack = preferences.getString("premiumGiftsStickerPack", null);
                this.lastUpdatedPremiumGiftsStickerPack = preferences.getLong("lastUpdatedPremiumGiftsStickerPack", 0L);
                this.genericAnimationsStickerPack = preferences.getString("genericAnimationsStickerPack", null);
                this.lastUpdatedGenericAnimations = preferences.getLong("lastUpdatedGenericAnimations", 0L);
                string3 = preferences.getString("terms", null);
                if (string3 != null) {
                    SerializedData serializedData4 = new SerializedData(decode3);
                    this.unacceptedTermsOfService = TLRPC.TL_help_termsOfService.TLdeserialize(serializedData4, serializedData4.readInt32(false), false);
                    serializedData4.cleanup();
                }
                i = preferences.getInt("6migrateOffsetId", 0);
                this.migrateOffsetId = i;
                if (i != -1) {
                }
                string = preferences.getString("tmpPassword", null);
                if (string != null) {
                    SerializedData serializedData22 = new SerializedData(decode2);
                    this.tmpPassword = TLRPC.TL_account_tmpPassword.TLdeserialize(serializedData22, serializedData22.readInt32(false), false);
                    serializedData22.cleanup();
                }
                string2 = preferences.getString("user", null);
                if (string2 != null) {
                    SerializedData serializedData32 = new SerializedData(decode);
                    this.currentUser = TLRPC.User.TLdeserialize(serializedData32, serializedData32.readInt32(false), false);
                    serializedData32.cleanup();
                }
                user = this.currentUser;
                if (user != null) {
                }
                this.configLoaded = true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void loadGlobalTTl() {
        if (this.ttlIsLoading || System.currentTimeMillis() - this.lastLoadingTime < 60000) {
            return;
        }
        this.ttlIsLoading = true;
        getConnectionsManager().sendRequest(new TLRPC.TL_messages_getDefaultHistoryTTL(), new RequestDelegate() { // from class: org.telegram.messenger.UserConfig$$ExternalSyntheticLambda0
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                UserConfig.this.lambda$loadGlobalTTl$3(tLObject, tL_error);
            }
        });
    }

    public void resetSavedPassword() {
        this.savedPasswordTime = 0L;
        if (this.savedPasswordHash != null) {
            Arrays.fill(this.savedPasswordHash, (byte) 0);
            this.savedPasswordHash = null;
        }
        if (this.savedSaltedPassword != null) {
            Arrays.fill(this.savedSaltedPassword, (byte) 0);
            this.savedSaltedPassword = null;
        }
    }

    public void saveConfig(final boolean z) {
        NotificationCenter.getInstance(this.currentAccount).doOnIdle(new Runnable() { // from class: org.telegram.messenger.UserConfig$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                UserConfig.this.lambda$saveConfig$0(z);
            }
        });
    }

    public void savePassword(byte[] bArr, byte[] bArr2) {
        this.savedPasswordTime = SystemClock.elapsedRealtime();
        this.savedPasswordHash = bArr;
        this.savedSaltedPassword = bArr2;
    }

    public void setCurrentUser(TLRPC.User user) {
        synchronized (this.sync) {
            TLRPC.User user2 = this.currentUser;
            this.currentUser = user;
            this.clientUserId = user.id;
            checkPremiumSelf(user2, user);
        }
    }

    public void setDialogsLoadOffset(int i, int i2, int i3, long j, long j2, long j3, long j4) {
        SharedPreferences.Editor edit = getPreferences().edit();
        StringBuilder sb = new StringBuilder();
        sb.append("2dialogsLoadOffsetId");
        sb.append(i == 0 ? "" : Integer.valueOf(i));
        edit.putInt(sb.toString(), i2);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("2dialogsLoadOffsetDate");
        sb2.append(i == 0 ? "" : Integer.valueOf(i));
        edit.putInt(sb2.toString(), i3);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("2dialogsLoadOffsetUserId");
        sb3.append(i == 0 ? "" : Integer.valueOf(i));
        edit.putLong(sb3.toString(), j);
        StringBuilder sb4 = new StringBuilder();
        sb4.append("2dialogsLoadOffsetChatId");
        sb4.append(i == 0 ? "" : Integer.valueOf(i));
        edit.putLong(sb4.toString(), j2);
        StringBuilder sb5 = new StringBuilder();
        sb5.append("2dialogsLoadOffsetChannelId");
        sb5.append(i == 0 ? "" : Integer.valueOf(i));
        edit.putLong(sb5.toString(), j3);
        StringBuilder sb6 = new StringBuilder();
        sb6.append("2dialogsLoadOffsetAccess");
        sb6.append(i != 0 ? Integer.valueOf(i) : "");
        edit.putLong(sb6.toString(), j4);
        edit.putBoolean("hasValidDialogLoadIds", true);
        edit.commit();
    }

    public void setGlobalTtl(int i) {
        this.globalTtl = i;
    }

    public void setPinnedDialogsLoaded(int i, boolean z) {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putBoolean("2pinnedDialogsLoaded" + i, z).commit();
    }

    public void setTotalDialogsCount(int i, int i2) {
        SharedPreferences.Editor edit = getPreferences().edit();
        StringBuilder sb = new StringBuilder();
        sb.append("2totalDialogsLoadCount");
        sb.append(i == 0 ? "" : Integer.valueOf(i));
        edit.putInt(sb.toString(), i2).commit();
    }

    public void updateSaveGalleryExceptions(int i, LongSparseArray<SaveToGallerySettingsHelper.DialogException> longSparseArray) {
        SharedPreferences sharedPreferences;
        LongSparseArray<SaveToGallerySettingsHelper.DialogException> longSparseArray2;
        if (i == 1) {
            this.userSaveGalleryExceptions = longSparseArray;
            Context context = ApplicationLoader.applicationContext;
            sharedPreferences = context.getSharedPreferences(SaveToGallerySettingsHelper.USERS_PREF_NAME + "_" + this.currentAccount, 0);
            longSparseArray2 = this.userSaveGalleryExceptions;
        } else if (i == 2) {
            this.groupsSaveGalleryExceptions = longSparseArray;
            Context context2 = ApplicationLoader.applicationContext;
            sharedPreferences = context2.getSharedPreferences(SaveToGallerySettingsHelper.GROUPS_PREF_NAME + "_" + this.currentAccount, 0);
            longSparseArray2 = this.groupsSaveGalleryExceptions;
        } else if (i != 4) {
            return;
        } else {
            this.chanelSaveGalleryExceptions = longSparseArray;
            Context context3 = ApplicationLoader.applicationContext;
            sharedPreferences = context3.getSharedPreferences(SaveToGallerySettingsHelper.CHANNELS_PREF_NAME + "_" + this.currentAccount, 0);
            longSparseArray2 = this.chanelSaveGalleryExceptions;
        }
        SaveToGallerySettingsHelper.saveExceptions(sharedPreferences, longSparseArray2);
    }
}
