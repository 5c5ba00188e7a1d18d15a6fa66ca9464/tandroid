package org.telegram.messenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Base64;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$EmojiStatus;
import org.telegram.tgnet.TLRPC$InputStorePaymentPurpose;
import org.telegram.tgnet.TLRPC$TL_account_tmpPassword;
import org.telegram.tgnet.TLRPC$TL_defaultHistoryTTL;
import org.telegram.tgnet.TLRPC$TL_emojiStatus;
import org.telegram.tgnet.TLRPC$TL_emojiStatusUntil;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_help_termsOfService;
import org.telegram.tgnet.TLRPC$User;
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
    public TLRPC$InputStorePaymentPurpose billingPaymentPurpose;
    public int botRatingLoadTime;
    public long clientUserId;
    private boolean configLoaded;
    public boolean contactsReimported;
    public int contactsSavedCount;
    private TLRPC$User currentUser;
    public String defaultTopicIcons;
    public boolean draftsLoaded;
    public boolean filtersLoaded;
    public String genericAnimationsStickerPack;
    public boolean hasSecureData;
    public boolean hasValidDialogLoadIds;
    public int lastContactsSyncTime;
    public int lastHintsSyncTime;
    long lastLoadingTime;
    public int lastMyLocationShareTime;
    public long lastUpdatedDefaultTopicIcons;
    public long lastUpdatedGenericAnimations;
    public long lastUpdatedPremiumGiftsStickerPack;
    public int loginTime;
    public boolean notificationsSettingsLoaded;
    public boolean notificationsSignUpSettingsLoaded;
    public String premiumGiftsStickerPack;
    public int ratingLoadTime;
    public boolean registeredForPush;
    public volatile byte[] savedPasswordHash;
    public volatile long savedPasswordTime;
    public volatile byte[] savedSaltedPassword;
    public int sharingMyLocationUntil;
    public TLRPC$TL_account_tmpPassword tmpPassword;
    public TLRPC$TL_help_termsOfService unacceptedTermsOfService;
    private final Object sync = new Object();
    public int lastSendMessageId = -210000;
    public int lastBroadcastId = -1;
    public boolean unreadDialogsLoaded = true;
    public int migrateOffsetId = -1;
    public int migrateOffsetDate = -1;
    public long migrateOffsetUserId = -1;
    public long migrateOffsetChatId = -1;
    public long migrateOffsetChannelId = -1;
    public long migrateOffsetAccess = -1;
    public boolean syncContacts = true;
    public boolean suggestContacts = true;
    public List<String> awaitBillingProductIds = new ArrayList();
    int globalTtl = 0;
    boolean ttlIsLoading = false;

    public static UserConfig getInstance(int i) {
        UserConfig userConfig = Instance[i];
        if (userConfig == null) {
            synchronized (UserConfig.class) {
                userConfig = Instance[i];
                if (userConfig == null) {
                    UserConfig[] userConfigArr = Instance;
                    UserConfig userConfig2 = new UserConfig(i);
                    userConfigArr[i] = userConfig2;
                    userConfig = userConfig2;
                }
            }
        }
        return userConfig;
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

    public UserConfig(int i) {
        super(i);
    }

    public static boolean hasPremiumOnAccounts() {
        for (int i = 0; i < 4; i++) {
            if (AccountInstance.getInstance(i).getUserConfig().isClientActivated() && AccountInstance.getInstance(i).getUserConfig().getUserConfig().isPremium()) {
                return true;
            }
        }
        return false;
    }

    public static int getMaxAccountCount() {
        return hasPremiumOnAccounts() ? 5 : 3;
    }

    public int getNewMessageId() {
        int i;
        synchronized (this.sync) {
            i = this.lastSendMessageId;
            this.lastSendMessageId = i - 1;
        }
        return i;
    }

    public void saveConfig(final boolean z) {
        NotificationCenter.getInstance(this.currentAccount).doOnIdle(new Runnable() { // from class: org.telegram.messenger.UserConfig$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                UserConfig.this.lambda$saveConfig$0(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveConfig$0(boolean z) {
        synchronized (this.sync) {
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
                edit.putBoolean("contactsReimported", this.contactsReimported);
                edit.putInt("loginTime", this.loginTime);
                edit.putBoolean("syncContacts", this.syncContacts);
                edit.putBoolean("suggestContacts", this.suggestContacts);
                edit.putBoolean("hasSecureData", this.hasSecureData);
                edit.putBoolean("notificationsSettingsLoaded3", this.notificationsSettingsLoaded);
                edit.putBoolean("notificationsSignUpSettingsLoaded", this.notificationsSignUpSettingsLoaded);
                edit.putLong("autoDownloadConfigLoadTime", this.autoDownloadConfigLoadTime);
                edit.putBoolean("hasValidDialogLoadIds", this.hasValidDialogLoadIds);
                edit.putInt("sharingMyLocationUntil", this.sharingMyLocationUntil);
                edit.putInt("lastMyLocationShareTime", this.lastMyLocationShareTime);
                edit.putBoolean("filtersLoaded", this.filtersLoaded);
                edit.putStringSet("awaitBillingProductIds", new HashSet(this.awaitBillingProductIds));
                TLRPC$InputStorePaymentPurpose tLRPC$InputStorePaymentPurpose = this.billingPaymentPurpose;
                if (tLRPC$InputStorePaymentPurpose != null) {
                    SerializedData serializedData = new SerializedData(tLRPC$InputStorePaymentPurpose.getObjectSize());
                    this.billingPaymentPurpose.serializeToStream(serializedData);
                    edit.putString("billingPaymentPurpose", Base64.encodeToString(serializedData.toByteArray(), 0));
                    serializedData.cleanup();
                } else {
                    edit.remove("billingPaymentPurpose");
                }
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
                TLRPC$TL_help_termsOfService tLRPC$TL_help_termsOfService = this.unacceptedTermsOfService;
                if (tLRPC$TL_help_termsOfService != null) {
                    try {
                        SerializedData serializedData2 = new SerializedData(tLRPC$TL_help_termsOfService.getObjectSize());
                        this.unacceptedTermsOfService.serializeToStream(serializedData2);
                        edit.putString("terms", Base64.encodeToString(serializedData2.toByteArray(), 0));
                        serializedData2.cleanup();
                    } catch (Exception unused) {
                    }
                } else {
                    edit.remove("terms");
                }
                SharedConfig.saveConfig();
                if (this.tmpPassword != null) {
                    SerializedData serializedData3 = new SerializedData();
                    this.tmpPassword.serializeToStream(serializedData3);
                    edit.putString("tmpPassword", Base64.encodeToString(serializedData3.toByteArray(), 0));
                    serializedData3.cleanup();
                } else {
                    edit.remove("tmpPassword");
                }
                if (this.currentUser == null) {
                    edit.remove("user");
                } else if (z) {
                    SerializedData serializedData4 = new SerializedData();
                    this.currentUser.serializeToStream(serializedData4);
                    edit.putString("user", Base64.encodeToString(serializedData4.toByteArray(), 0));
                    serializedData4.cleanup();
                }
                edit.commit();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public static boolean isValidAccount(int i) {
        return i >= 0 && i < 4 && getInstance(i).isClientActivated();
    }

    public boolean isClientActivated() {
        boolean z;
        synchronized (this.sync) {
            z = this.currentUser != null;
        }
        return z;
    }

    public long getClientUserId() {
        long j;
        synchronized (this.sync) {
            TLRPC$User tLRPC$User = this.currentUser;
            j = tLRPC$User != null ? tLRPC$User.id : 0L;
        }
        return j;
    }

    public String getClientPhone() {
        String str;
        synchronized (this.sync) {
            TLRPC$User tLRPC$User = this.currentUser;
            if (tLRPC$User == null || (str = tLRPC$User.phone) == null) {
                str = "";
            }
        }
        return str;
    }

    public TLRPC$User getCurrentUser() {
        TLRPC$User tLRPC$User;
        synchronized (this.sync) {
            tLRPC$User = this.currentUser;
        }
        return tLRPC$User;
    }

    public void setCurrentUser(TLRPC$User tLRPC$User) {
        synchronized (this.sync) {
            TLRPC$User tLRPC$User2 = this.currentUser;
            this.currentUser = tLRPC$User;
            this.clientUserId = tLRPC$User.id;
            checkPremiumSelf(tLRPC$User2, tLRPC$User);
        }
    }

    private void checkPremiumSelf(TLRPC$User tLRPC$User, final TLRPC$User tLRPC$User2) {
        if (tLRPC$User == null || !(tLRPC$User2 == null || tLRPC$User.premium == tLRPC$User2.premium)) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.UserConfig$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    UserConfig.this.lambda$checkPremiumSelf$1(tLRPC$User2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkPremiumSelf$1(TLRPC$User tLRPC$User) {
        getMessagesController().updatePremium(tLRPC$User.premium);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.currentUserPremiumStatusChanged, new Object[0]);
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.premiumStatusChangedGlobal, new Object[0]);
        getMediaDataController().loadPremiumPromo(false);
        getMediaDataController().loadReactions(false, true);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(22:9|(1:11)|12|(18:17|18|(1:24)|25|26|27|(1:31)|33|(1:35)|36|(1:40)|41|(1:45)|46|(1:48)|49|50|51)|54|18|(3:20|22|24)|25|26|27|(2:29|31)|33|(0)|36|(2:38|40)|41|(2:43|45)|46|(0)|49|50|51) */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0176, code lost:
        r2 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0177, code lost:
        org.telegram.messenger.FileLog.e(r2);
     */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0184 A[Catch: all -> 0x01ff, TryCatch #1 {, blocks: (B:4:0x0003, B:6:0x0007, B:9:0x0009, B:11:0x0012, B:12:0x001a, B:14:0x00d4, B:18:0x00e0, B:20:0x0114, B:22:0x011c, B:24:0x0122, B:25:0x0134, B:27:0x0154, B:29:0x015d, B:31:0x0163, B:33:0x017a, B:35:0x0184, B:36:0x01ac, B:38:0x01b5, B:40:0x01bb, B:41:0x01cd, B:43:0x01d6, B:45:0x01dc, B:46:0x01ee, B:48:0x01f2, B:49:0x01fb, B:50:0x01fd, B:53:0x0177), top: B:3:0x0003, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x01f2 A[Catch: all -> 0x01ff, TryCatch #1 {, blocks: (B:4:0x0003, B:6:0x0007, B:9:0x0009, B:11:0x0012, B:12:0x001a, B:14:0x00d4, B:18:0x00e0, B:20:0x0114, B:22:0x011c, B:24:0x0122, B:25:0x0134, B:27:0x0154, B:29:0x015d, B:31:0x0163, B:33:0x017a, B:35:0x0184, B:36:0x01ac, B:38:0x01b5, B:40:0x01bb, B:41:0x01cd, B:43:0x01d6, B:45:0x01dc, B:46:0x01ee, B:48:0x01f2, B:49:0x01fb, B:50:0x01fd, B:53:0x0177), top: B:3:0x0003, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadConfig() {
        boolean z;
        int i;
        String string;
        String string2;
        TLRPC$User tLRPC$User;
        byte[] decode;
        byte[] decode2;
        String string3;
        byte[] decode3;
        String string4;
        synchronized (this.sync) {
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
            this.loginTime = preferences.getInt("loginTime", this.currentAccount);
            this.syncContacts = preferences.getBoolean("syncContacts", true);
            this.suggestContacts = preferences.getBoolean("suggestContacts", true);
            this.hasSecureData = preferences.getBoolean("hasSecureData", false);
            this.notificationsSettingsLoaded = preferences.getBoolean("notificationsSettingsLoaded3", false);
            this.notificationsSignUpSettingsLoaded = preferences.getBoolean("notificationsSignUpSettingsLoaded", false);
            this.autoDownloadConfigLoadTime = preferences.getLong("autoDownloadConfigLoadTime", 0L);
            if (!preferences.contains("2dialogsLoadOffsetId") && !preferences.getBoolean("hasValidDialogLoadIds", false)) {
                z = false;
                this.hasValidDialogLoadIds = z;
                this.sharingMyLocationUntil = preferences.getInt("sharingMyLocationUntil", 0);
                this.lastMyLocationShareTime = preferences.getInt("lastMyLocationShareTime", 0);
                this.filtersLoaded = preferences.getBoolean("filtersLoaded", false);
                this.awaitBillingProductIds = new ArrayList(preferences.getStringSet("awaitBillingProductIds", Collections.emptySet()));
                if (preferences.contains("billingPaymentPurpose") && (string4 = preferences.getString("billingPaymentPurpose", null)) != null && Base64.decode(string4, 0) != null) {
                    SerializedData serializedData = new SerializedData();
                    this.billingPaymentPurpose = TLRPC$InputStorePaymentPurpose.TLdeserialize(serializedData, serializedData.readInt32(false), false);
                    serializedData.cleanup();
                }
                this.premiumGiftsStickerPack = preferences.getString("premiumGiftsStickerPack", null);
                this.lastUpdatedPremiumGiftsStickerPack = preferences.getLong("lastUpdatedPremiumGiftsStickerPack", 0L);
                this.genericAnimationsStickerPack = preferences.getString("genericAnimationsStickerPack", null);
                this.lastUpdatedGenericAnimations = preferences.getLong("lastUpdatedGenericAnimations", 0L);
                string3 = preferences.getString("terms", null);
                if (string3 != null && (decode3 = Base64.decode(string3, 0)) != null) {
                    SerializedData serializedData2 = new SerializedData(decode3);
                    this.unacceptedTermsOfService = TLRPC$TL_help_termsOfService.TLdeserialize(serializedData2, serializedData2.readInt32(false), false);
                    serializedData2.cleanup();
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
                    SerializedData serializedData3 = new SerializedData(decode2);
                    this.tmpPassword = TLRPC$TL_account_tmpPassword.TLdeserialize(serializedData3, serializedData3.readInt32(false), false);
                    serializedData3.cleanup();
                }
                string2 = preferences.getString("user", null);
                if (string2 != null && (decode = Base64.decode(string2, 0)) != null) {
                    SerializedData serializedData4 = new SerializedData(decode);
                    this.currentUser = TLRPC$User.TLdeserialize(serializedData4, serializedData4.readInt32(false), false);
                    serializedData4.cleanup();
                }
                tLRPC$User = this.currentUser;
                if (tLRPC$User != null) {
                    checkPremiumSelf(null, tLRPC$User);
                    this.clientUserId = this.currentUser.id;
                }
                this.configLoaded = true;
            }
            z = true;
            this.hasValidDialogLoadIds = z;
            this.sharingMyLocationUntil = preferences.getInt("sharingMyLocationUntil", 0);
            this.lastMyLocationShareTime = preferences.getInt("lastMyLocationShareTime", 0);
            this.filtersLoaded = preferences.getBoolean("filtersLoaded", false);
            this.awaitBillingProductIds = new ArrayList(preferences.getStringSet("awaitBillingProductIds", Collections.emptySet()));
            if (preferences.contains("billingPaymentPurpose")) {
                SerializedData serializedData5 = new SerializedData();
                this.billingPaymentPurpose = TLRPC$InputStorePaymentPurpose.TLdeserialize(serializedData5, serializedData5.readInt32(false), false);
                serializedData5.cleanup();
            }
            this.premiumGiftsStickerPack = preferences.getString("premiumGiftsStickerPack", null);
            this.lastUpdatedPremiumGiftsStickerPack = preferences.getLong("lastUpdatedPremiumGiftsStickerPack", 0L);
            this.genericAnimationsStickerPack = preferences.getString("genericAnimationsStickerPack", null);
            this.lastUpdatedGenericAnimations = preferences.getLong("lastUpdatedGenericAnimations", 0L);
            string3 = preferences.getString("terms", null);
            if (string3 != null) {
                SerializedData serializedData22 = new SerializedData(decode3);
                this.unacceptedTermsOfService = TLRPC$TL_help_termsOfService.TLdeserialize(serializedData22, serializedData22.readInt32(false), false);
                serializedData22.cleanup();
            }
            i = preferences.getInt("6migrateOffsetId", 0);
            this.migrateOffsetId = i;
            if (i != -1) {
            }
            string = preferences.getString("tmpPassword", null);
            if (string != null) {
                SerializedData serializedData32 = new SerializedData(decode2);
                this.tmpPassword = TLRPC$TL_account_tmpPassword.TLdeserialize(serializedData32, serializedData32.readInt32(false), false);
                serializedData32.cleanup();
            }
            string2 = preferences.getString("user", null);
            if (string2 != null) {
                SerializedData serializedData42 = new SerializedData(decode);
                this.currentUser = TLRPC$User.TLdeserialize(serializedData42, serializedData42.readInt32(false), false);
                serializedData42.cleanup();
            }
            tLRPC$User = this.currentUser;
            if (tLRPC$User != null) {
            }
            this.configLoaded = true;
        }
    }

    public boolean isConfigLoaded() {
        return this.configLoaded;
    }

    public void savePassword(byte[] bArr, byte[] bArr2) {
        this.savedPasswordTime = SystemClock.elapsedRealtime();
        this.savedPasswordHash = bArr;
        this.savedSaltedPassword = bArr2;
    }

    public void checkSavedPassword() {
        if (!(this.savedSaltedPassword == null && this.savedPasswordHash == null) && Math.abs(SystemClock.elapsedRealtime() - this.savedPasswordTime) >= 1800000) {
            resetSavedPassword();
        }
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

    public SharedPreferences getPreferences() {
        if (this.currentAccount == 0) {
            return ApplicationLoader.applicationContext.getSharedPreferences("userconfing", 0);
        }
        Context context = ApplicationLoader.applicationContext;
        return context.getSharedPreferences("userconfig" + this.currentAccount, 0);
    }

    public void clearConfig() {
        getPreferences().edit().clear().apply();
        boolean z = false;
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
        int i = 0;
        while (true) {
            if (i >= 4) {
                break;
            } else if (AccountInstance.getInstance(i).getUserConfig().isClientActivated()) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        if (!z) {
            SharedConfig.clearConfig();
        }
        saveConfig(true);
    }

    public boolean isPinnedDialogsLoaded(int i) {
        SharedPreferences preferences = getPreferences();
        return preferences.getBoolean("2pinnedDialogsLoaded" + i, false);
    }

    public void setPinnedDialogsLoaded(int i, boolean z) {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putBoolean("2pinnedDialogsLoaded" + i, z).commit();
    }

    public int getTotalDialogsCount(int i) {
        SharedPreferences preferences = getPreferences();
        StringBuilder sb = new StringBuilder();
        sb.append("2totalDialogsLoadCount");
        sb.append(i == 0 ? "" : Integer.valueOf(i));
        return preferences.getInt(sb.toString(), 0);
    }

    public void setTotalDialogsCount(int i, int i2) {
        SharedPreferences.Editor edit = getPreferences().edit();
        StringBuilder sb = new StringBuilder();
        sb.append("2totalDialogsLoadCount");
        sb.append(i == 0 ? "" : Integer.valueOf(i));
        edit.putInt(sb.toString(), i2).commit();
    }

    public long[] getDialogLoadOffsets(int i) {
        SharedPreferences preferences = getPreferences();
        StringBuilder sb = new StringBuilder();
        sb.append("2dialogsLoadOffsetId");
        Object obj = "";
        sb.append(i == 0 ? obj : Integer.valueOf(i));
        int i2 = -1;
        int i3 = preferences.getInt(sb.toString(), this.hasValidDialogLoadIds ? 0 : -1);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("2dialogsLoadOffsetDate");
        sb2.append(i == 0 ? obj : Integer.valueOf(i));
        String sb3 = sb2.toString();
        if (this.hasValidDialogLoadIds) {
            i2 = 0;
        }
        int i4 = preferences.getInt(sb3, i2);
        StringBuilder sb4 = new StringBuilder();
        sb4.append("2dialogsLoadOffsetUserId");
        sb4.append(i == 0 ? obj : Integer.valueOf(i));
        long j = -1;
        long prefIntOrLong = AndroidUtilities.getPrefIntOrLong(preferences, sb4.toString(), this.hasValidDialogLoadIds ? 0L : -1L);
        StringBuilder sb5 = new StringBuilder();
        sb5.append("2dialogsLoadOffsetChatId");
        sb5.append(i == 0 ? obj : Integer.valueOf(i));
        long prefIntOrLong2 = AndroidUtilities.getPrefIntOrLong(preferences, sb5.toString(), this.hasValidDialogLoadIds ? 0L : -1L);
        StringBuilder sb6 = new StringBuilder();
        sb6.append("2dialogsLoadOffsetChannelId");
        sb6.append(i == 0 ? obj : Integer.valueOf(i));
        long prefIntOrLong3 = AndroidUtilities.getPrefIntOrLong(preferences, sb6.toString(), this.hasValidDialogLoadIds ? 0L : -1L);
        StringBuilder sb7 = new StringBuilder();
        sb7.append("2dialogsLoadOffsetAccess");
        if (i != 0) {
            obj = Integer.valueOf(i);
        }
        sb7.append(obj);
        String sb8 = sb7.toString();
        if (this.hasValidDialogLoadIds) {
            j = 0;
        }
        return new long[]{i3, i4, prefIntOrLong, prefIntOrLong2, prefIntOrLong3, preferences.getLong(sb8, j)};
    }

    public void setDialogsLoadOffset(int i, int i2, int i3, long j, long j2, long j3, long j4) {
        SharedPreferences.Editor edit = getPreferences().edit();
        StringBuilder sb = new StringBuilder();
        sb.append("2dialogsLoadOffsetId");
        Object obj = "";
        sb.append(i == 0 ? obj : Integer.valueOf(i));
        edit.putInt(sb.toString(), i2);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("2dialogsLoadOffsetDate");
        sb2.append(i == 0 ? obj : Integer.valueOf(i));
        edit.putInt(sb2.toString(), i3);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("2dialogsLoadOffsetUserId");
        sb3.append(i == 0 ? obj : Integer.valueOf(i));
        edit.putLong(sb3.toString(), j);
        StringBuilder sb4 = new StringBuilder();
        sb4.append("2dialogsLoadOffsetChatId");
        sb4.append(i == 0 ? obj : Integer.valueOf(i));
        edit.putLong(sb4.toString(), j2);
        StringBuilder sb5 = new StringBuilder();
        sb5.append("2dialogsLoadOffsetChannelId");
        sb5.append(i == 0 ? obj : Integer.valueOf(i));
        edit.putLong(sb5.toString(), j3);
        StringBuilder sb6 = new StringBuilder();
        sb6.append("2dialogsLoadOffsetAccess");
        if (i != 0) {
            obj = Integer.valueOf(i);
        }
        sb6.append(obj);
        edit.putLong(sb6.toString(), j4);
        edit.putBoolean("hasValidDialogLoadIds", true);
        edit.commit();
    }

    public boolean isPremium() {
        TLRPC$User tLRPC$User = this.currentUser;
        if (tLRPC$User == null) {
            return false;
        }
        return tLRPC$User.premium;
    }

    public Long getEmojiStatus() {
        TLRPC$User tLRPC$User = this.currentUser;
        if (tLRPC$User == null) {
            return null;
        }
        TLRPC$EmojiStatus tLRPC$EmojiStatus = tLRPC$User.emoji_status;
        if ((tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatusUntil) && ((TLRPC$TL_emojiStatusUntil) tLRPC$EmojiStatus).until > ((int) (System.currentTimeMillis() / 1000))) {
            return Long.valueOf(((TLRPC$TL_emojiStatusUntil) this.currentUser.emoji_status).document_id);
        }
        TLRPC$EmojiStatus tLRPC$EmojiStatus2 = this.currentUser.emoji_status;
        if (!(tLRPC$EmojiStatus2 instanceof TLRPC$TL_emojiStatus)) {
            return null;
        }
        return Long.valueOf(((TLRPC$TL_emojiStatus) tLRPC$EmojiStatus2).document_id);
    }

    public int getGlobalTTl() {
        return this.globalTtl;
    }

    public void loadGlobalTTl() {
        if (this.ttlIsLoading || System.currentTimeMillis() - this.lastLoadingTime < 60000) {
            return;
        }
        this.ttlIsLoading = true;
        getConnectionsManager().sendRequest(new TLObject() { // from class: org.telegram.tgnet.TLRPC$TL_messages_getDefaultHistoryTTL
            public static int constructor = 1703637384;

            @Override // org.telegram.tgnet.TLObject
            public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
                return TLRPC$TL_defaultHistoryTTL.TLdeserialize(abstractSerializedData, i, z);
            }

            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                abstractSerializedData.writeInt32(constructor);
            }
        }, new RequestDelegate() { // from class: org.telegram.messenger.UserConfig$$ExternalSyntheticLambda3
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                UserConfig.this.lambda$loadGlobalTTl$3(tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadGlobalTTl$3(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.UserConfig$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                UserConfig.this.lambda$loadGlobalTTl$2(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadGlobalTTl$2(TLObject tLObject) {
        if (tLObject != null) {
            this.globalTtl = ((TLRPC$TL_defaultHistoryTTL) tLObject).period / 60;
            getNotificationCenter().postNotificationName(NotificationCenter.didUpdateGlobalAutoDeleteTimer, new Object[0]);
            this.ttlIsLoading = false;
            this.lastLoadingTime = System.currentTimeMillis();
        }
    }

    public void setGlobalTtl(int i) {
        this.globalTtl = i;
    }
}
