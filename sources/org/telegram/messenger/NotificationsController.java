package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.PostProcessor;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import androidx.collection.LongSparseArray;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.app.RemoteInput;
import androidx.core.content.FileProvider;
import androidx.core.content.LocusIdCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.drawable.IconCompat;
import j$.util.Comparator$-CC;
import j$.util.function.Consumer;
import j$.util.function.ToLongFunction;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.support.LongSparseIntArray;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatPhoto;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$KeyboardButton;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$MessageFwdHeader;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$NotificationSound;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$Poll;
import org.telegram.tgnet.TLRPC$ReplyMarkup;
import org.telegram.tgnet.TLRPC$TL_account_setReactionsNotifySettings;
import org.telegram.tgnet.TLRPC$TL_account_updateNotifySettings;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$TL_inputNotifyBroadcasts;
import org.telegram.tgnet.TLRPC$TL_inputNotifyChats;
import org.telegram.tgnet.TLRPC$TL_inputNotifyForumTopic;
import org.telegram.tgnet.TLRPC$TL_inputNotifyPeer;
import org.telegram.tgnet.TLRPC$TL_inputNotifyUsers;
import org.telegram.tgnet.TLRPC$TL_inputPeerNotifySettings;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonCallback;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonRow;
import org.telegram.tgnet.TLRPC$TL_message;
import org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest;
import org.telegram.tgnet.TLRPC$TL_messageActionContactSignUp;
import org.telegram.tgnet.TLRPC$TL_messageActionEmpty;
import org.telegram.tgnet.TLRPC$TL_messageActionGameScore;
import org.telegram.tgnet.TLRPC$TL_messageActionGeoProximityReached;
import org.telegram.tgnet.TLRPC$TL_messageActionLoginUnknownLocation;
import org.telegram.tgnet.TLRPC$TL_messageActionPaymentSent;
import org.telegram.tgnet.TLRPC$TL_messageActionPhoneCall;
import org.telegram.tgnet.TLRPC$TL_messageActionPinMessage;
import org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme;
import org.telegram.tgnet.TLRPC$TL_messageActionSetChatWallPaper;
import org.telegram.tgnet.TLRPC$TL_messageActionSetMessagesTTL;
import org.telegram.tgnet.TLRPC$TL_messageActionSetSameChatWallPaper;
import org.telegram.tgnet.TLRPC$TL_messageActionUserJoined;
import org.telegram.tgnet.TLRPC$TL_messageActionUserUpdatedPhoto;
import org.telegram.tgnet.TLRPC$TL_messageEntitySpoiler;
import org.telegram.tgnet.TLRPC$TL_messageMediaContact;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaGeo;
import org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive;
import org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway;
import org.telegram.tgnet.TLRPC$TL_messageMediaGiveawayResults;
import org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageMediaPoll;
import org.telegram.tgnet.TLRPC$TL_messageMediaVenue;
import org.telegram.tgnet.TLRPC$TL_messageService;
import org.telegram.tgnet.TLRPC$TL_notificationSoundDefault;
import org.telegram.tgnet.TLRPC$TL_notificationSoundLocal;
import org.telegram.tgnet.TLRPC$TL_notificationSoundNone;
import org.telegram.tgnet.TLRPC$TL_notificationSoundRingtone;
import org.telegram.tgnet.TLRPC$TL_peerNotifySettings;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_reactionNotificationsFromAll;
import org.telegram.tgnet.TLRPC$TL_reactionNotificationsFromContacts;
import org.telegram.tgnet.TLRPC$TL_reactionsNotifySettings;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.BubbleActivity;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PopupNotificationActivity;
import org.telegram.ui.Stories.recorder.StoryEntry;
import org.webrtc.MediaStreamTrack;
/* loaded from: classes3.dex */
public class NotificationsController extends BaseController {
    public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";
    private static volatile NotificationsController[] Instance = null;
    public static String OTHER_NOTIFICATIONS_CHANNEL = null;
    public static final int SETTING_MUTE_2_DAYS = 2;
    public static final int SETTING_MUTE_8_HOURS = 1;
    public static final int SETTING_MUTE_CUSTOM = 5;
    public static final int SETTING_MUTE_FOREVER = 3;
    public static final int SETTING_MUTE_HOUR = 0;
    public static final int SETTING_MUTE_UNMUTE = 4;
    public static final int SETTING_SOUND_OFF = 1;
    public static final int SETTING_SOUND_ON = 0;
    public static final int TYPE_CHANNEL = 2;
    public static final int TYPE_GROUP = 0;
    public static final int TYPE_PRIVATE = 1;
    public static final int TYPE_REACTIONS_MESSAGES = 4;
    public static final int TYPE_REACTIONS_STORIES = 5;
    public static final int TYPE_STORIES = 3;
    protected static AudioManager audioManager;
    private static final Object[] lockObjects;
    private static NotificationManagerCompat notificationManager;
    private static final LongSparseArray<String> sharedPrefCachedKeys;
    private static NotificationManager systemNotificationManager;
    private AlarmManager alarmManager;
    private boolean channelGroupsCreated;
    private Runnable checkStoryPushesRunnable;
    private final ArrayList<MessageObject> delayedPushMessages;
    NotificationsSettingsFacade dialogsNotificationsFacade;
    private final LongSparseArray<MessageObject> fcmRandomMessagesDict;
    private Boolean groupsCreated;
    private boolean inChatSoundEnabled;
    private int lastBadgeCount;
    private int lastButtonId;
    public long lastNotificationChannelCreateTime;
    private int lastOnlineFromOtherDevice;
    private long lastSoundOutPlay;
    private long lastSoundPlay;
    private final LongSparseArray<Integer> lastWearNotifiedMessageId;
    private String launcherClassName;
    private SpoilerEffect mediaSpoilerEffect;
    private Runnable notificationDelayRunnable;
    private PowerManager.WakeLock notificationDelayWakelock;
    private String notificationGroup;
    private int notificationId;
    private boolean notifyCheck;
    private long openedDialogId;
    private final HashSet<Long> openedInBubbleDialogs;
    private long openedTopicId;
    private int personalCount;
    public final ArrayList<MessageObject> popupMessages;
    public ArrayList<MessageObject> popupReplyMessages;
    private final LongSparseArray<Integer> pushDialogs;
    private final LongSparseArray<Integer> pushDialogsOverrideMention;
    private final ArrayList<MessageObject> pushMessages;
    private final LongSparseArray<SparseArray<MessageObject>> pushMessagesDict;
    public boolean showBadgeMessages;
    public boolean showBadgeMuted;
    public boolean showBadgeNumber;
    private final LongSparseArray<Point> smartNotificationsDialogs;
    private int soundIn;
    private boolean soundInLoaded;
    private int soundOut;
    private boolean soundOutLoaded;
    private SoundPool soundPool;
    private int soundRecord;
    private boolean soundRecordLoaded;
    char[] spoilerChars;
    private final ArrayList<StoryNotification> storyPushMessages;
    private final LongSparseArray<StoryNotification> storyPushMessagesDict;
    private int total_unread_count;
    private final LongSparseArray<Integer> wearNotificationsIds;
    private static final DispatchQueue notificationsQueue = new DispatchQueue("notificationsQueue");
    public static long globalSecretChatId = DialogObject.makeEncryptedDialogId(1);

    public static String getGlobalNotificationsKey(int i) {
        return i == 0 ? "EnableGroup2" : i == 1 ? "EnableAll2" : "EnableChannel2";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateServerNotificationsSettings$47(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateServerNotificationsSettings$48(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateServerNotificationsSettings$49(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    public void processReadStories() {
    }

    static {
        notificationManager = null;
        systemNotificationManager = null;
        if (Build.VERSION.SDK_INT >= 26 && ApplicationLoader.applicationContext != null) {
            notificationManager = NotificationManagerCompat.from(ApplicationLoader.applicationContext);
            systemNotificationManager = (NotificationManager) ApplicationLoader.applicationContext.getSystemService("notification");
            checkOtherNotificationsChannel();
        }
        audioManager = (AudioManager) ApplicationLoader.applicationContext.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        Instance = new NotificationsController[4];
        lockObjects = new Object[4];
        for (int i = 0; i < 4; i++) {
            lockObjects[i] = new Object();
        }
        sharedPrefCachedKeys = new LongSparseArray<>();
    }

    public static NotificationsController getInstance(int i) {
        NotificationsController notificationsController = Instance[i];
        if (notificationsController == null) {
            synchronized (lockObjects[i]) {
                notificationsController = Instance[i];
                if (notificationsController == null) {
                    NotificationsController[] notificationsControllerArr = Instance;
                    NotificationsController notificationsController2 = new NotificationsController(i);
                    notificationsControllerArr[i] = notificationsController2;
                    notificationsController = notificationsController2;
                }
            }
        }
        return notificationsController;
    }

    public NotificationsController(int i) {
        super(i);
        this.pushMessages = new ArrayList<>();
        this.delayedPushMessages = new ArrayList<>();
        this.pushMessagesDict = new LongSparseArray<>();
        this.fcmRandomMessagesDict = new LongSparseArray<>();
        this.smartNotificationsDialogs = new LongSparseArray<>();
        this.pushDialogs = new LongSparseArray<>();
        this.wearNotificationsIds = new LongSparseArray<>();
        this.lastWearNotifiedMessageId = new LongSparseArray<>();
        this.pushDialogsOverrideMention = new LongSparseArray<>();
        this.popupMessages = new ArrayList<>();
        this.popupReplyMessages = new ArrayList<>();
        this.openedInBubbleDialogs = new HashSet<>();
        this.storyPushMessages = new ArrayList<>();
        this.storyPushMessagesDict = new LongSparseArray<>();
        this.openedDialogId = 0L;
        this.openedTopicId = 0L;
        this.lastButtonId = 5000;
        this.total_unread_count = 0;
        this.personalCount = 0;
        this.notifyCheck = false;
        this.lastOnlineFromOtherDevice = 0;
        this.lastBadgeCount = -1;
        this.mediaSpoilerEffect = new SpoilerEffect();
        this.spoilerChars = new char[]{10252, 10338, 10385, 10280};
        this.checkStoryPushesRunnable = new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.checkStoryPushes();
            }
        };
        this.notificationId = this.currentAccount + 1;
        StringBuilder sb = new StringBuilder();
        sb.append("messages");
        int i2 = this.currentAccount;
        sb.append(i2 == 0 ? "" : Integer.valueOf(i2));
        this.notificationGroup = sb.toString();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        this.inChatSoundEnabled = notificationsSettings.getBoolean("EnableInChatSound", true);
        this.showBadgeNumber = notificationsSettings.getBoolean("badgeNumber", true);
        this.showBadgeMuted = notificationsSettings.getBoolean("badgeNumberMuted", false);
        this.showBadgeMessages = notificationsSettings.getBoolean("badgeNumberMessages", true);
        notificationManager = NotificationManagerCompat.from(ApplicationLoader.applicationContext);
        systemNotificationManager = (NotificationManager) ApplicationLoader.applicationContext.getSystemService("notification");
        try {
            audioManager = (AudioManager) ApplicationLoader.applicationContext.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            this.alarmManager = (AlarmManager) ApplicationLoader.applicationContext.getSystemService("alarm");
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        try {
            PowerManager.WakeLock newWakeLock = ((PowerManager) ApplicationLoader.applicationContext.getSystemService("power")).newWakeLock(1, "telegram:notification_delay_lock");
            this.notificationDelayWakelock = newWakeLock;
            newWakeLock.setReferenceCounted(false);
        } catch (Exception e3) {
            FileLog.e(e3);
        }
        this.notificationDelayRunnable = new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$new$0();
            }
        };
        this.dialogsNotificationsFacade = new NotificationsSettingsFacade(this.currentAccount);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("delay reached");
        }
        if (!this.delayedPushMessages.isEmpty()) {
            showOrUpdateNotification(true);
            this.delayedPushMessages.clear();
        }
        try {
            if (this.notificationDelayWakelock.isHeld()) {
                this.notificationDelayWakelock.release();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static void checkOtherNotificationsChannel() {
        SharedPreferences sharedPreferences;
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        if (OTHER_NOTIFICATIONS_CHANNEL == null) {
            sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
            OTHER_NOTIFICATIONS_CHANNEL = sharedPreferences.getString("OtherKey", "Other3");
        } else {
            sharedPreferences = null;
        }
        NotificationChannel notificationChannel = systemNotificationManager.getNotificationChannel(OTHER_NOTIFICATIONS_CHANNEL);
        if (notificationChannel != null && notificationChannel.getImportance() == 0) {
            try {
                systemNotificationManager.deleteNotificationChannel(OTHER_NOTIFICATIONS_CHANNEL);
            } catch (Exception e) {
                FileLog.e(e);
            }
            OTHER_NOTIFICATIONS_CHANNEL = null;
            notificationChannel = null;
        }
        if (OTHER_NOTIFICATIONS_CHANNEL == null) {
            if (sharedPreferences == null) {
                sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
            }
            OTHER_NOTIFICATIONS_CHANNEL = "Other" + Utilities.random.nextLong();
            sharedPreferences.edit().putString("OtherKey", OTHER_NOTIFICATIONS_CHANNEL).commit();
        }
        if (notificationChannel == null) {
            NotificationChannel notificationChannel2 = new NotificationChannel(OTHER_NOTIFICATIONS_CHANNEL, "Internal notifications", 3);
            notificationChannel2.enableLights(false);
            notificationChannel2.enableVibration(false);
            notificationChannel2.setSound(null, null);
            try {
                systemNotificationManager.createNotificationChannel(notificationChannel2);
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
    }

    public static String getSharedPrefKey(long j, long j2) {
        return getSharedPrefKey(j, j2, false);
    }

    public static String getSharedPrefKey(long j, long j2, boolean z) {
        String valueOf;
        if (z) {
            return j2 != 0 ? String.format(Locale.US, "%d_%d", Long.valueOf(j), Long.valueOf(j2)) : String.valueOf(j);
        }
        long j3 = (j2 << 12) + j;
        LongSparseArray<String> longSparseArray = sharedPrefCachedKeys;
        int indexOfKey = longSparseArray.indexOfKey(j3);
        if (indexOfKey >= 0) {
            return longSparseArray.valueAt(indexOfKey);
        }
        if (j2 != 0) {
            valueOf = String.format(Locale.US, "%d_%d", Long.valueOf(j), Long.valueOf(j2));
        } else {
            valueOf = String.valueOf(j);
        }
        longSparseArray.put(j3, valueOf);
        return valueOf;
    }

    public void muteUntil(long j, long j2, int i) {
        if (j != 0) {
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            boolean z = j2 != 0;
            boolean isGlobalNotificationsEnabled = getInstance(this.currentAccount).isGlobalNotificationsEnabled(j, false, false);
            String sharedPrefKey = getSharedPrefKey(j, j2);
            long j3 = 1;
            if (i != Integer.MAX_VALUE) {
                edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey, 3);
                edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + sharedPrefKey, getConnectionsManager().getCurrentTime() + i);
                j3 = 1 | (((long) i) << 32);
            } else if (!isGlobalNotificationsEnabled && !z) {
                edit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey);
                j3 = 0L;
            } else {
                edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey, 2);
            }
            edit.apply();
            if (j2 == 0) {
                getInstance(this.currentAccount).removeNotificationsForDialog(j);
                MessagesStorage.getInstance(this.currentAccount).setDialogFlags(j, j3);
                TLRPC$Dialog tLRPC$Dialog = MessagesController.getInstance(this.currentAccount).dialogs_dict.get(j);
                if (tLRPC$Dialog != null) {
                    TLRPC$TL_peerNotifySettings tLRPC$TL_peerNotifySettings = new TLRPC$TL_peerNotifySettings();
                    tLRPC$Dialog.notify_settings = tLRPC$TL_peerNotifySettings;
                    if (i != Integer.MAX_VALUE || isGlobalNotificationsEnabled) {
                        tLRPC$TL_peerNotifySettings.mute_until = i;
                    }
                }
            }
            getInstance(this.currentAccount).updateServerNotificationsSettings(j, j2);
        }
    }

    public void cleanup() {
        this.popupMessages.clear();
        this.popupReplyMessages.clear();
        this.channelGroupsCreated = false;
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$cleanup$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanup$1() {
        this.openedDialogId = 0L;
        this.openedTopicId = 0L;
        this.total_unread_count = 0;
        this.personalCount = 0;
        this.pushMessages.clear();
        this.pushMessagesDict.clear();
        this.fcmRandomMessagesDict.clear();
        this.pushDialogs.clear();
        this.wearNotificationsIds.clear();
        this.lastWearNotifiedMessageId.clear();
        this.openedInBubbleDialogs.clear();
        this.delayedPushMessages.clear();
        this.notifyCheck = false;
        this.lastBadgeCount = 0;
        try {
            if (this.notificationDelayWakelock.isHeld()) {
                this.notificationDelayWakelock.release();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        dismissNotification();
        setBadge(getTotalAllUnreadCount());
        SharedPreferences.Editor edit = getAccountInstance().getNotificationsSettings().edit();
        edit.clear();
        edit.commit();
        if (Build.VERSION.SDK_INT >= 26) {
            try {
                systemNotificationManager.deleteNotificationChannelGroup("channels" + this.currentAccount);
                systemNotificationManager.deleteNotificationChannelGroup("groups" + this.currentAccount);
                systemNotificationManager.deleteNotificationChannelGroup("private" + this.currentAccount);
                systemNotificationManager.deleteNotificationChannelGroup("stories" + this.currentAccount);
                systemNotificationManager.deleteNotificationChannelGroup("other" + this.currentAccount);
                String str = this.currentAccount + "channel";
                List<NotificationChannel> notificationChannels = systemNotificationManager.getNotificationChannels();
                int size = notificationChannels.size();
                for (int i = 0; i < size; i++) {
                    String id = notificationChannels.get(i).getId();
                    if (id.startsWith(str)) {
                        try {
                            systemNotificationManager.deleteNotificationChannel(id);
                        } catch (Exception e2) {
                            FileLog.e(e2);
                        }
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("delete channel cleanup " + id);
                        }
                    }
                }
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
    }

    public void setInChatSoundEnabled(boolean z) {
        this.inChatSoundEnabled = z;
    }

    public void setOpenedDialogId(final long j, final long j2) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$setOpenedDialogId$2(j, j2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setOpenedDialogId$2(long j, long j2) {
        this.openedDialogId = j;
        this.openedTopicId = j2;
    }

    public void setOpenedInBubble(final long j, final boolean z) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$setOpenedInBubble$3(z, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setOpenedInBubble$3(boolean z, long j) {
        if (z) {
            this.openedInBubbleDialogs.add(Long.valueOf(j));
        } else {
            this.openedInBubbleDialogs.remove(Long.valueOf(j));
        }
    }

    public void setLastOnlineFromOtherDevice(final int i) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$setLastOnlineFromOtherDevice$4(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setLastOnlineFromOtherDevice$4(int i) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("set last online from other device = " + i);
        }
        this.lastOnlineFromOtherDevice = i;
    }

    public void removeNotificationsForDialog(long j) {
        processReadMessages(null, j, 0, ConnectionsManager.DEFAULT_DATACENTER_ID, false);
        LongSparseIntArray longSparseIntArray = new LongSparseIntArray();
        longSparseIntArray.put(j, 0);
        processDialogsUpdateRead(longSparseIntArray);
    }

    public boolean hasMessagesToReply() {
        for (int i = 0; i < this.pushMessages.size(); i++) {
            MessageObject messageObject = this.pushMessages.get(i);
            long dialogId = messageObject.getDialogId();
            if (!messageObject.isReactionPush) {
                TLRPC$Message tLRPC$Message = messageObject.messageOwner;
                if ((!tLRPC$Message.mentioned || !(tLRPC$Message.action instanceof TLRPC$TL_messageActionPinMessage)) && !DialogObject.isEncryptedDialog(dialogId) && (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup())) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void forceShowPopupForReply() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$forceShowPopupForReply$6();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$forceShowPopupForReply$6() {
        final ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.pushMessages.size(); i++) {
            MessageObject messageObject = this.pushMessages.get(i);
            long dialogId = messageObject.getDialogId();
            TLRPC$Message tLRPC$Message = messageObject.messageOwner;
            if ((!tLRPC$Message.mentioned || !(tLRPC$Message.action instanceof TLRPC$TL_messageActionPinMessage)) && !DialogObject.isEncryptedDialog(dialogId) && (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup())) {
                arrayList.add(0, messageObject);
            }
        }
        if (arrayList.isEmpty() || AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda38
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$forceShowPopupForReply$5(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$forceShowPopupForReply$5(ArrayList arrayList) {
        this.popupReplyMessages = arrayList;
        Intent intent = new Intent(ApplicationLoader.applicationContext, PopupNotificationActivity.class);
        intent.putExtra("force", true);
        intent.putExtra("currentAccount", this.currentAccount);
        intent.setFlags(268763140);
        ApplicationLoader.applicationContext.startActivity(intent);
        ApplicationLoader.applicationContext.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    public void removeDeletedMessagesFromNotifications(final LongSparseArray<ArrayList<Integer>> longSparseArray, final boolean z) {
        final ArrayList arrayList = new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$removeDeletedMessagesFromNotifications$9(longSparseArray, z, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeDeletedMessagesFromNotifications$9(LongSparseArray longSparseArray, boolean z, final ArrayList arrayList) {
        long j;
        Integer num;
        LongSparseArray longSparseArray2 = longSparseArray;
        int i = this.total_unread_count;
        getAccountInstance().getNotificationsSettings();
        int i2 = 0;
        while (i2 < longSparseArray.size()) {
            long keyAt = longSparseArray2.keyAt(i2);
            SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(keyAt);
            if (sparseArray != null) {
                ArrayList arrayList2 = (ArrayList) longSparseArray2.get(keyAt);
                int size = arrayList2.size();
                int i3 = 0;
                while (i3 < size) {
                    int intValue = ((Integer) arrayList2.get(i3)).intValue();
                    MessageObject messageObject = sparseArray.get(intValue);
                    if (messageObject == null || messageObject.isStoryReactionPush || (z && !messageObject.isReactionPush)) {
                        j = keyAt;
                    } else {
                        j = keyAt;
                        long dialogId = messageObject.getDialogId();
                        Integer num2 = this.pushDialogs.get(dialogId);
                        if (num2 == null) {
                            num2 = 0;
                        }
                        Integer valueOf = Integer.valueOf(num2.intValue() - 1);
                        if (valueOf.intValue() <= 0) {
                            this.smartNotificationsDialogs.remove(dialogId);
                            num = 0;
                        } else {
                            num = valueOf;
                        }
                        if (!num.equals(num2)) {
                            if (getMessagesController().isForum(dialogId)) {
                                int i4 = this.total_unread_count - (num2.intValue() > 0 ? 1 : 0);
                                this.total_unread_count = i4;
                                this.total_unread_count = i4 + (num.intValue() > 0 ? 1 : 0);
                            } else {
                                int intValue2 = this.total_unread_count - num2.intValue();
                                this.total_unread_count = intValue2;
                                this.total_unread_count = intValue2 + num.intValue();
                            }
                            this.pushDialogs.put(dialogId, num);
                        }
                        if (num.intValue() == 0) {
                            this.pushDialogs.remove(dialogId);
                            this.pushDialogsOverrideMention.remove(dialogId);
                        }
                        sparseArray.remove(intValue);
                        this.delayedPushMessages.remove(messageObject);
                        this.pushMessages.remove(messageObject);
                        if (isPersonalMessage(messageObject)) {
                            this.personalCount--;
                        }
                        arrayList.add(messageObject);
                    }
                    i3++;
                    keyAt = j;
                }
                long j2 = keyAt;
                if (sparseArray.size() == 0) {
                    this.pushMessagesDict.remove(j2);
                }
            }
            i2++;
            longSparseArray2 = longSparseArray;
        }
        if (!arrayList.isEmpty()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda36
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$removeDeletedMessagesFromNotifications$7(arrayList);
                }
            });
        }
        if (i != this.total_unread_count) {
            if (!this.notifyCheck) {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
            } else {
                scheduleNotificationDelay(this.lastOnlineFromOtherDevice > getConnectionsManager().getCurrentTime());
            }
            final int size2 = this.pushDialogs.size();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$removeDeletedMessagesFromNotifications$8(size2);
                }
            });
        }
        this.notifyCheck = false;
        if (this.showBadgeNumber) {
            setBadge(getTotalAllUnreadCount());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeDeletedMessagesFromNotifications$7(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeDeletedMessagesFromNotifications$8(int i) {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    public void removeDeletedHisoryFromNotifications(final LongSparseIntArray longSparseIntArray) {
        final ArrayList arrayList = new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$removeDeletedHisoryFromNotifications$12(longSparseIntArray, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeDeletedHisoryFromNotifications$12(LongSparseIntArray longSparseIntArray, final ArrayList arrayList) {
        Integer num;
        int i = this.total_unread_count;
        getAccountInstance().getNotificationsSettings();
        Integer num2 = 0;
        int i2 = 0;
        while (true) {
            if (i2 >= longSparseIntArray.size()) {
                break;
            }
            long keyAt = longSparseIntArray.keyAt(i2);
            long j = -keyAt;
            long j2 = longSparseIntArray.get(keyAt);
            Integer num3 = this.pushDialogs.get(j);
            if (num3 == null) {
                num3 = num2;
            }
            Integer num4 = num3;
            int i3 = 0;
            while (i3 < this.pushMessages.size()) {
                MessageObject messageObject = this.pushMessages.get(i3);
                if (messageObject.getDialogId() == j) {
                    num = num2;
                    if (messageObject.getId() <= j2) {
                        SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(j);
                        if (sparseArray != null) {
                            sparseArray.remove(messageObject.getId());
                            if (sparseArray.size() == 0) {
                                this.pushMessagesDict.remove(j);
                            }
                        }
                        this.delayedPushMessages.remove(messageObject);
                        this.pushMessages.remove(messageObject);
                        i3--;
                        if (isPersonalMessage(messageObject)) {
                            this.personalCount--;
                        }
                        arrayList.add(messageObject);
                        num4 = Integer.valueOf(num4.intValue() - 1);
                    }
                } else {
                    num = num2;
                }
                i3++;
                num2 = num;
            }
            Integer num5 = num2;
            if (num4.intValue() <= 0) {
                this.smartNotificationsDialogs.remove(j);
                num4 = num5;
            }
            if (!num4.equals(num3)) {
                if (getMessagesController().isForum(j)) {
                    int i4 = this.total_unread_count - (num3.intValue() > 0 ? 1 : 0);
                    this.total_unread_count = i4;
                    this.total_unread_count = i4 + (num4.intValue() <= 0 ? 0 : 1);
                } else {
                    int intValue = this.total_unread_count - num3.intValue();
                    this.total_unread_count = intValue;
                    this.total_unread_count = intValue + num4.intValue();
                }
                this.pushDialogs.put(j, num4);
            }
            if (num4.intValue() == 0) {
                this.pushDialogs.remove(j);
                this.pushDialogsOverrideMention.remove(j);
            }
            i2++;
            num2 = num5;
        }
        if (arrayList.isEmpty()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda37
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$removeDeletedHisoryFromNotifications$10(arrayList);
                }
            });
        }
        if (i != this.total_unread_count) {
            if (!this.notifyCheck) {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
            } else {
                scheduleNotificationDelay(this.lastOnlineFromOtherDevice > getConnectionsManager().getCurrentTime());
            }
            final int size = this.pushDialogs.size();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda25
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$removeDeletedHisoryFromNotifications$11(size);
                }
            });
        }
        this.notifyCheck = false;
        if (this.showBadgeNumber) {
            setBadge(getTotalAllUnreadCount());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeDeletedHisoryFromNotifications$10(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeDeletedHisoryFromNotifications$11(int i) {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    public void processSeenStoryReactions(long j, final int i) {
        if (j != getUserConfig().getClientUserId()) {
            return;
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processSeenStoryReactions$13(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processSeenStoryReactions$13(int i) {
        int i2 = 0;
        boolean z = false;
        while (i2 < this.pushMessages.size()) {
            MessageObject messageObject = this.pushMessages.get(i2);
            if (messageObject.isStoryReactionPush && Math.abs(messageObject.getId()) == i) {
                this.pushMessages.remove(i2);
                SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(messageObject.getDialogId());
                if (sparseArray != null) {
                    sparseArray.remove(messageObject.getId());
                }
                if (sparseArray != null && sparseArray.size() <= 0) {
                    this.pushMessagesDict.remove(messageObject.getDialogId());
                }
                ArrayList<Integer> arrayList = new ArrayList<>();
                arrayList.add(Integer.valueOf(messageObject.getId()));
                getMessagesStorage().deletePushMessages(messageObject.getDialogId(), arrayList);
                i2--;
                z = true;
            }
            i2++;
        }
        if (z) {
            showOrUpdateNotification(false);
        }
    }

    public void processDeleteStory(final long j, final int i) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processDeleteStory$14(j, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0039  */
    /* JADX WARN: Removed duplicated region for block: B:12:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processDeleteStory$14(long j, int i) {
        boolean z;
        StoryNotification storyNotification = this.storyPushMessagesDict.get(j);
        if (storyNotification != null) {
            storyNotification.dateByIds.remove(Integer.valueOf(i));
            if (storyNotification.dateByIds.isEmpty()) {
                this.storyPushMessagesDict.remove(j);
                this.storyPushMessages.remove(storyNotification);
                z = true;
                getMessagesStorage().deleteStoryPushMessage(j);
                if (z) {
                    return;
                }
                showOrUpdateNotification(false);
                return;
            }
            getMessagesStorage().putStoryPushMessage(storyNotification);
        }
        z = false;
        if (z) {
        }
    }

    public void processReadStories(final long j, int i) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processReadStories$15(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processReadStories$15(long j) {
        boolean z;
        StoryNotification storyNotification = this.storyPushMessagesDict.get(j);
        if (storyNotification != null) {
            this.storyPushMessagesDict.remove(j);
            this.storyPushMessages.remove(storyNotification);
            z = true;
            getMessagesStorage().deleteStoryPushMessage(j);
        } else {
            z = false;
        }
        if (z) {
            showOrUpdateNotification(false);
            updateStoryPushesRunnable();
        }
    }

    public void processIgnoreStories() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processIgnoreStories$16();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processIgnoreStories$16() {
        boolean z = !this.storyPushMessages.isEmpty();
        this.storyPushMessages.clear();
        this.storyPushMessagesDict.clear();
        getMessagesStorage().deleteAllStoryPushMessages();
        if (z) {
            showOrUpdateNotification(false);
        }
    }

    public void processIgnoreStoryReactions() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processIgnoreStoryReactions$17();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processIgnoreStoryReactions$17() {
        int i = 0;
        boolean z = false;
        while (i < this.pushMessages.size()) {
            MessageObject messageObject = this.pushMessages.get(i);
            if (messageObject != null && messageObject.isStoryReactionPush) {
                this.pushMessages.remove(i);
                i--;
                SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(messageObject.getDialogId());
                if (sparseArray != null) {
                    sparseArray.remove(messageObject.getId());
                }
                if (sparseArray != null && sparseArray.size() <= 0) {
                    this.pushMessagesDict.remove(messageObject.getDialogId());
                }
                z = true;
            }
            i++;
        }
        getMessagesStorage().deleteAllStoryReactionPushMessages();
        if (z) {
            showOrUpdateNotification(false);
        }
    }

    public void processIgnoreStories(final long j) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda28
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processIgnoreStories$18(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processIgnoreStories$18(long j) {
        boolean z = !this.storyPushMessages.isEmpty();
        this.storyPushMessages.clear();
        this.storyPushMessagesDict.clear();
        getMessagesStorage().deleteStoryPushMessage(j);
        if (z) {
            showOrUpdateNotification(false);
        }
    }

    public void processReadMessages(final LongSparseIntArray longSparseIntArray, final long j, final int i, final int i2, final boolean z) {
        final ArrayList arrayList = new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda45
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processReadMessages$20(longSparseIntArray, arrayList, j, i2, i, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00ec, code lost:
        r8 = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processReadMessages$20(LongSparseIntArray longSparseIntArray, final ArrayList arrayList, long j, int i, int i2, boolean z) {
        long j2;
        int i3;
        long j3;
        int i4 = 1;
        if (longSparseIntArray != null) {
            int i5 = 0;
            while (i5 < longSparseIntArray.size()) {
                long keyAt = longSparseIntArray.keyAt(i5);
                int i6 = longSparseIntArray.get(keyAt);
                int i7 = 0;
                while (i7 < this.pushMessages.size()) {
                    MessageObject messageObject = this.pushMessages.get(i7);
                    if (messageObject.messageOwner.from_scheduled || messageObject.getDialogId() != keyAt || messageObject.getId() > i6 || messageObject.isStoryReactionPush) {
                        i3 = i5;
                    } else {
                        if (isPersonalMessage(messageObject)) {
                            this.personalCount -= i4;
                        }
                        arrayList.add(messageObject);
                        if (messageObject.isStoryReactionPush) {
                            i3 = i5;
                            j3 = messageObject.getDialogId();
                        } else {
                            i3 = i5;
                            long j4 = messageObject.messageOwner.peer_id.channel_id;
                            j3 = j4 != 0 ? -j4 : 0L;
                        }
                        SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(j3);
                        if (sparseArray != null) {
                            sparseArray.remove(messageObject.getId());
                            if (sparseArray.size() == 0) {
                                this.pushMessagesDict.remove(j3);
                            }
                        }
                        this.delayedPushMessages.remove(messageObject);
                        this.pushMessages.remove(i7);
                        i7--;
                    }
                    i7++;
                    i5 = i3;
                    i4 = 1;
                }
                i5++;
                i4 = 1;
            }
        }
        if (j != 0 && (i != 0 || i2 != 0)) {
            int i8 = 0;
            while (i8 < this.pushMessages.size()) {
                MessageObject messageObject2 = this.pushMessages.get(i8);
                if (messageObject2.getDialogId() == j && !messageObject2.isStoryReactionPush) {
                    boolean z2 = i2 != 0 ? true : true;
                    if (z2) {
                        if (isPersonalMessage(messageObject2)) {
                            this.personalCount--;
                        }
                        if (messageObject2.isStoryReactionPush) {
                            j2 = messageObject2.getDialogId();
                        } else {
                            long j5 = messageObject2.messageOwner.peer_id.channel_id;
                            j2 = j5 != 0 ? -j5 : 0L;
                        }
                        SparseArray<MessageObject> sparseArray2 = this.pushMessagesDict.get(j2);
                        if (sparseArray2 != null) {
                            sparseArray2.remove(messageObject2.getId());
                            if (sparseArray2.size() == 0) {
                                this.pushMessagesDict.remove(j2);
                            }
                        }
                        this.pushMessages.remove(i8);
                        this.delayedPushMessages.remove(messageObject2);
                        arrayList.add(messageObject2);
                        i8--;
                    }
                }
                i8++;
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processReadMessages$19(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processReadMessages$19(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x005b, code lost:
        if (r0 == 2) goto L33;
     */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0075  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int addToPopupMessages(ArrayList<MessageObject> arrayList, MessageObject messageObject, long j, boolean z, SharedPreferences sharedPreferences) {
        int i;
        if (messageObject.isStoryReactionPush) {
            return 0;
        }
        if (!DialogObject.isEncryptedDialog(j)) {
            if (sharedPreferences.getBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + j, false)) {
                i = sharedPreferences.getInt("popup_" + j, 0);
            } else {
                i = 0;
            }
            if (i == 0) {
                if (z) {
                    i = sharedPreferences.getInt("popupChannel", 0);
                } else {
                    i = sharedPreferences.getInt(DialogObject.isChatDialog(j) ? "popupGroup" : "popupAll", 0);
                }
            } else if (i == 1) {
                i = 3;
            }
            if (i != 0 && messageObject.messageOwner.peer_id.channel_id != 0 && !messageObject.isSupergroup()) {
                i = 0;
            }
            if (i != 0) {
                arrayList.add(0, messageObject);
            }
            return i;
        }
        i = 0;
        if (i != 0) {
            i = 0;
        }
        if (i != 0) {
        }
        return i;
    }

    public void processEditedMessages(final LongSparseArray<ArrayList<MessageObject>> longSparseArray) {
        if (longSparseArray.size() == 0) {
            return;
        }
        new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda33
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processEditedMessages$21(longSparseArray);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processEditedMessages$21(LongSparseArray longSparseArray) {
        int size = longSparseArray.size();
        boolean z = false;
        for (int i = 0; i < size; i++) {
            longSparseArray.keyAt(i);
            ArrayList arrayList = (ArrayList) longSparseArray.valueAt(i);
            int size2 = arrayList.size();
            for (int i2 = 0; i2 < size2; i2++) {
                MessageObject messageObject = (MessageObject) arrayList.get(i2);
                long j = 0;
                if (messageObject.isStoryReactionPush) {
                    j = messageObject.getDialogId();
                } else {
                    long j2 = messageObject.messageOwner.peer_id.channel_id;
                    if (j2 != 0) {
                        j = -j2;
                    }
                }
                SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(j);
                if (sparseArray == null) {
                    break;
                }
                MessageObject messageObject2 = sparseArray.get(messageObject.getId());
                if (messageObject2 != null && (messageObject2.isReactionPush || messageObject2.isStoryReactionPush)) {
                    messageObject2 = null;
                }
                if (messageObject2 != null) {
                    sparseArray.put(messageObject.getId(), messageObject);
                    int indexOf = this.pushMessages.indexOf(messageObject2);
                    if (indexOf >= 0) {
                        this.pushMessages.set(indexOf, messageObject);
                    }
                    int indexOf2 = this.delayedPushMessages.indexOf(messageObject2);
                    if (indexOf2 >= 0) {
                        this.delayedPushMessages.set(indexOf2, messageObject);
                    }
                    z = true;
                }
            }
        }
        if (z) {
            showOrUpdateNotification(false);
        }
    }

    public void processNewMessages(final ArrayList<MessageObject> arrayList, final boolean z, final boolean z2, final CountDownLatch countDownLatch) {
        if (!arrayList.isEmpty()) {
            final ArrayList arrayList2 = new ArrayList(0);
            notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda42
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$processNewMessages$25(arrayList, arrayList2, z2, z, countDownLatch);
                }
            });
        } else if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x004a, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserJoined) == false) goto L20;
     */
    /* JADX WARN: Removed duplicated region for block: B:55:0x013b  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0140  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0158  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x015f  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0165  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x018d  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x01cf  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processNewMessages$25(ArrayList arrayList, final ArrayList arrayList2, boolean z, boolean z2, CountDownLatch countDownLatch) {
        boolean z3;
        int i;
        int i2;
        Integer num;
        boolean z4;
        long j;
        boolean z5;
        boolean z6;
        long j2;
        MessageObject messageObject;
        int i3;
        boolean z7;
        MessageObject messageObject2;
        long j3;
        SparseArray<MessageObject> sparseArray;
        long j4;
        int i4;
        long j5;
        long j6;
        boolean z8;
        boolean z9;
        long j7;
        long j8;
        SparseArray<MessageObject> sparseArray2;
        MessageObject messageObject3;
        TLRPC$Message tLRPC$Message;
        ArrayList arrayList3 = arrayList;
        LongSparseArray longSparseArray = new LongSparseArray();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        boolean z10 = notificationsSettings.getBoolean("PinnedMessages", true);
        int i5 = 0;
        int i6 = 0;
        boolean z11 = false;
        boolean z12 = false;
        boolean z13 = false;
        boolean z14 = false;
        while (i6 < arrayList.size()) {
            MessageObject messageObject4 = (MessageObject) arrayList3.get(i6);
            if (messageObject4.messageOwner != null) {
                if (!messageObject4.isImportedForward()) {
                    TLRPC$Message tLRPC$Message2 = messageObject4.messageOwner;
                    TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message2.action;
                    if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetMessagesTTL)) {
                        if (tLRPC$Message2.silent) {
                            if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp)) {
                            }
                        }
                    }
                }
                z4 = z10;
                i3 = i6;
                z7 = z11;
                z11 = z7;
                i6 = i3 + 1;
                arrayList3 = arrayList;
                z10 = z4;
            }
            if (!MessageObject.isTopicActionMessage(messageObject4)) {
                if (messageObject4.isStoryPush) {
                    long currentTimeMillis = messageObject4.messageOwner == null ? System.currentTimeMillis() : tLRPC$Message.date * 1000;
                    long dialogId = messageObject4.getDialogId();
                    int id = messageObject4.getId();
                    StoryNotification storyNotification = this.storyPushMessagesDict.get(dialogId);
                    if (storyNotification != null) {
                        storyNotification.dateByIds.put(Integer.valueOf(id), new Pair<>(Long.valueOf(currentTimeMillis), Long.valueOf(currentTimeMillis + 86400000)));
                        boolean z15 = storyNotification.hidden;
                        boolean z16 = messageObject4.isStoryPushHidden;
                        if (z15 != z16) {
                            storyNotification.hidden = z16;
                            z14 = true;
                        }
                        storyNotification.date = storyNotification.getLeastDate();
                        getMessagesStorage().putStoryPushMessage(storyNotification);
                        z12 = true;
                    } else {
                        StoryNotification storyNotification2 = new StoryNotification(dialogId, messageObject4.localName, id, currentTimeMillis);
                        storyNotification2.hidden = messageObject4.isStoryPushHidden;
                        this.storyPushMessages.add(storyNotification2);
                        this.storyPushMessagesDict.put(dialogId, storyNotification2);
                        getMessagesStorage().putStoryPushMessage(storyNotification2);
                        z11 = true;
                        z14 = true;
                    }
                    Collections.sort(this.storyPushMessages, Comparator$-CC.comparingLong(new ToLongFunction() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda48
                        @Override // j$.util.function.ToLongFunction
                        public final long applyAsLong(Object obj) {
                            long j9;
                            j9 = ((NotificationsController.StoryNotification) obj).date;
                            return j9;
                        }
                    }));
                    z4 = z10;
                    i3 = i6;
                } else {
                    int id2 = messageObject4.getId();
                    if (messageObject4.isFcmMessage()) {
                        j = messageObject4.messageOwner.random_id;
                        z4 = z10;
                    } else {
                        z4 = z10;
                        j = 0;
                    }
                    long dialogId2 = messageObject4.getDialogId();
                    if (messageObject4.isFcmMessage()) {
                        z6 = messageObject4.localChannel;
                    } else if (DialogObject.isChatDialog(dialogId2)) {
                        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-dialogId2));
                        z6 = ChatObject.isChannel(chat) && !chat.megagroup;
                    } else {
                        z5 = false;
                        if (!messageObject4.isStoryReactionPush) {
                            j2 = messageObject4.getDialogId();
                        } else {
                            long j9 = messageObject4.messageOwner.peer_id.channel_id;
                            j2 = j9 != 0 ? -j9 : 0L;
                        }
                        SparseArray<MessageObject> sparseArray3 = this.pushMessagesDict.get(j2);
                        messageObject = sparseArray3 == null ? sparseArray3.get(id2) : null;
                        long j10 = j;
                        if (messageObject == null) {
                            long j11 = messageObject4.messageOwner.random_id;
                            if (j11 != 0 && (messageObject = this.fcmRandomMessagesDict.get(j11)) != null) {
                                i3 = i6;
                                z7 = z11;
                                this.fcmRandomMessagesDict.remove(messageObject4.messageOwner.random_id);
                                messageObject2 = messageObject;
                                if (messageObject2 != null) {
                                    if (messageObject2.isFcmMessage()) {
                                        if (sparseArray3 == null) {
                                            sparseArray3 = new SparseArray<>();
                                            this.pushMessagesDict.put(j2, sparseArray3);
                                        }
                                        sparseArray3.put(id2, messageObject4);
                                        int indexOf = this.pushMessages.indexOf(messageObject2);
                                        if (indexOf >= 0) {
                                            this.pushMessages.set(indexOf, messageObject4);
                                            messageObject3 = messageObject4;
                                            i5 = addToPopupMessages(arrayList2, messageObject4, dialogId2, z5, notificationsSettings);
                                        } else {
                                            messageObject3 = messageObject4;
                                        }
                                        if (z) {
                                            boolean z17 = messageObject3.localEdit;
                                            if (z17) {
                                                getMessagesStorage().putPushMessage(messageObject3);
                                            }
                                            z12 = z17;
                                        }
                                    }
                                } else if (!z12) {
                                    if (z) {
                                        getMessagesStorage().putPushMessage(messageObject4);
                                    }
                                    long topicId = MessageObject.getTopicId(this.currentAccount, messageObject4.messageOwner, getMessagesController().isForum(messageObject4));
                                    if (dialogId2 != this.openedDialogId || !ApplicationLoader.isScreenOn || messageObject4.isStoryReactionPush) {
                                        TLRPC$Message tLRPC$Message3 = messageObject4.messageOwner;
                                        if (!tLRPC$Message3.mentioned) {
                                            j3 = dialogId2;
                                        } else if (z4 || !(tLRPC$Message3.action instanceof TLRPC$TL_messageActionPinMessage)) {
                                            j3 = messageObject4.getFromChatId();
                                        }
                                        if (isPersonalMessage(messageObject4)) {
                                            this.personalCount++;
                                        }
                                        DialogObject.isChatDialog(j3);
                                        int indexOfKey = longSparseArray.indexOfKey(j3);
                                        if (indexOfKey >= 0 && topicId == 0) {
                                            z9 = ((Boolean) longSparseArray.valueAt(indexOfKey)).booleanValue();
                                            sparseArray = sparseArray3;
                                            i4 = i5;
                                            j4 = dialogId2;
                                            j5 = j10;
                                            j6 = j2;
                                            j7 = j3;
                                        } else {
                                            sparseArray = sparseArray3;
                                            j4 = dialogId2;
                                            long j12 = j3;
                                            i4 = i5;
                                            j5 = j10;
                                            j6 = j2;
                                            int notifyOverride = getNotifyOverride(notificationsSettings, j3, topicId);
                                            if (notifyOverride == -1) {
                                                z8 = isGlobalNotificationsEnabled(j12, Boolean.valueOf(z5), messageObject4.isReactionPush, messageObject4.isStoryReactionPush);
                                            } else {
                                                z8 = notifyOverride != 2;
                                            }
                                            z9 = z8;
                                            j7 = j12;
                                            longSparseArray.put(j7, Boolean.valueOf(z9));
                                        }
                                        if (z9) {
                                            if (z) {
                                                j8 = j7;
                                                i5 = i4;
                                            } else {
                                                j8 = j7;
                                                i5 = addToPopupMessages(arrayList2, messageObject4, j7, z5, notificationsSettings);
                                            }
                                            if (!z13) {
                                                z13 = messageObject4.messageOwner.from_scheduled;
                                            }
                                            this.delayedPushMessages.add(messageObject4);
                                            appendMessage(messageObject4);
                                            if (id2 != 0) {
                                                if (sparseArray == null) {
                                                    sparseArray2 = new SparseArray<>();
                                                    this.pushMessagesDict.put(j6, sparseArray2);
                                                } else {
                                                    sparseArray2 = sparseArray;
                                                }
                                                sparseArray2.put(id2, messageObject4);
                                            } else if (j5 != 0) {
                                                this.fcmRandomMessagesDict.put(j5, messageObject4);
                                            }
                                            if (j4 != j8) {
                                                long j13 = j4;
                                                Integer num2 = this.pushDialogsOverrideMention.get(j13);
                                                this.pushDialogsOverrideMention.put(j13, Integer.valueOf(num2 == null ? 1 : num2.intValue() + 1));
                                            }
                                        } else {
                                            j8 = j7;
                                            i5 = i4;
                                        }
                                        if (messageObject4.isReactionPush) {
                                            SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
                                            sparseBooleanArray.put(id2, true);
                                            getMessagesController().checkUnreadReactions(j8, topicId, sparseBooleanArray);
                                        }
                                        z11 = true;
                                    } else if (!z) {
                                        playInChatSound();
                                    }
                                }
                                z11 = z7;
                            }
                        }
                        i3 = i6;
                        z7 = z11;
                        messageObject2 = messageObject;
                        if (messageObject2 != null) {
                        }
                        z11 = z7;
                    }
                    z5 = z6;
                    if (!messageObject4.isStoryReactionPush) {
                    }
                    SparseArray<MessageObject> sparseArray32 = this.pushMessagesDict.get(j2);
                    if (sparseArray32 == null) {
                    }
                    long j102 = j;
                    if (messageObject == null) {
                    }
                    i3 = i6;
                    z7 = z11;
                    messageObject2 = messageObject;
                    if (messageObject2 != null) {
                    }
                    z11 = z7;
                }
                i6 = i3 + 1;
                arrayList3 = arrayList;
                z10 = z4;
            }
            z4 = z10;
            i3 = i6;
            z7 = z11;
            z11 = z7;
            i6 = i3 + 1;
            arrayList3 = arrayList;
            z10 = z4;
        }
        final int i7 = i5;
        boolean z18 = z11;
        if (z18) {
            this.notifyCheck = z2;
        }
        if (!arrayList2.isEmpty() && !AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda40
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$processNewMessages$23(arrayList2, i7);
                }
            });
        }
        if (z || z13) {
            if (z12) {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
            } else if (z18) {
                MessageObject messageObject5 = (MessageObject) arrayList.get(0);
                long dialogId3 = messageObject5.getDialogId();
                long topicId2 = MessageObject.getTopicId(this.currentAccount, messageObject5.messageOwner, getMessagesController().isForum(dialogId3));
                Boolean valueOf = messageObject5.isFcmMessage() ? Boolean.valueOf(messageObject5.localChannel) : null;
                int i8 = this.total_unread_count;
                int notifyOverride2 = getNotifyOverride(notificationsSettings, dialogId3, topicId2);
                if (notifyOverride2 == -1) {
                    z3 = isGlobalNotificationsEnabled(dialogId3, valueOf, messageObject5.isReactionPush, messageObject5.isStoryReactionPush);
                } else {
                    z3 = notifyOverride2 != 2;
                }
                Integer num3 = this.pushDialogs.get(dialogId3);
                if (num3 != null) {
                    i = 1;
                    i2 = num3.intValue() + 1;
                } else {
                    i = 1;
                    i2 = 1;
                }
                if (this.notifyCheck && !z3 && (num = this.pushDialogsOverrideMention.get(dialogId3)) != null && num.intValue() != 0) {
                    i2 = num.intValue();
                    z3 = true;
                }
                if (z3 && !messageObject5.isStoryPush) {
                    if (getMessagesController().isForum(dialogId3)) {
                        int i9 = this.total_unread_count - ((num3 == null || num3.intValue() <= 0) ? 0 : 1);
                        this.total_unread_count = i9;
                        if (i2 <= 0) {
                            i = 0;
                        }
                        this.total_unread_count = i9 + i;
                    } else {
                        if (num3 != null) {
                            this.total_unread_count -= num3.intValue();
                        }
                        this.total_unread_count += i2;
                    }
                    this.pushDialogs.put(dialogId3, Integer.valueOf(i2));
                }
                if (i8 != this.total_unread_count || z14) {
                    this.delayedPushMessages.clear();
                    showOrUpdateNotification(this.notifyCheck);
                    final int size = this.pushDialogs.size();
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda22
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationsController.this.lambda$processNewMessages$24(size);
                        }
                    });
                }
                this.notifyCheck = false;
                if (this.showBadgeNumber) {
                    setBadge(getTotalAllUnreadCount());
                }
            }
        }
        if (z14) {
            updateStoryPushesRunnable();
        }
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processNewMessages$23(ArrayList arrayList, int i) {
        this.popupMessages.addAll(0, arrayList);
        if (ApplicationLoader.mainInterfacePaused || !ApplicationLoader.isScreenOn) {
            if (i == 3 || ((i == 1 && ApplicationLoader.isScreenOn) || (i == 2 && !ApplicationLoader.isScreenOn))) {
                Intent intent = new Intent(ApplicationLoader.applicationContext, PopupNotificationActivity.class);
                intent.setFlags(268763140);
                try {
                    ApplicationLoader.applicationContext.startActivity(intent);
                } catch (Throwable unused) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processNewMessages$24(int i) {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    private void appendMessage(MessageObject messageObject) {
        for (int i = 0; i < this.pushMessages.size(); i++) {
            if (this.pushMessages.get(i).getId() == messageObject.getId() && this.pushMessages.get(i).getDialogId() == messageObject.getDialogId() && this.pushMessages.get(i).isStoryPush == messageObject.isStoryPush) {
                return;
            }
        }
        this.pushMessages.add(0, messageObject);
    }

    public int getTotalUnreadCount() {
        return this.total_unread_count;
    }

    public void processDialogsUpdateRead(final LongSparseIntArray longSparseIntArray) {
        final ArrayList arrayList = new ArrayList();
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processDialogsUpdateRead$28(longSparseIntArray, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0074 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00a0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00c8  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x013a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processDialogsUpdateRead$28(LongSparseIntArray longSparseIntArray, final ArrayList arrayList) {
        int i;
        boolean z;
        boolean z2;
        Integer num;
        int i2 = this.total_unread_count;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        int i3 = 0;
        while (true) {
            if (i3 >= longSparseIntArray.size()) {
                break;
            }
            long keyAt = longSparseIntArray.keyAt(i3);
            Integer num2 = this.pushDialogs.get(keyAt);
            int i4 = longSparseIntArray.get(keyAt);
            if (DialogObject.isChatDialog(keyAt)) {
                TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-keyAt));
                i4 = (chat == null || chat.min || ChatObject.isNotInChat(chat)) ? 0 : 0;
                if (chat != null) {
                    z = chat.forum;
                    i = i4;
                    if (!z) {
                        int notifyOverride = getNotifyOverride(notificationsSettings, keyAt, 0L);
                        if (notifyOverride == -1) {
                            z2 = isGlobalNotificationsEnabled(keyAt, false, false);
                        } else if (notifyOverride == 2) {
                            z2 = false;
                        }
                        if (this.notifyCheck && !z2 && (num = this.pushDialogsOverrideMention.get(keyAt)) != null && num.intValue() != 0) {
                            i = num.intValue();
                            z2 = true;
                        }
                        if (i == 0) {
                            this.smartNotificationsDialogs.remove(keyAt);
                        }
                        if (i < 0) {
                            if (num2 == null) {
                                i3++;
                            } else {
                                i = num2.intValue() + i;
                            }
                        }
                        if ((!z2 || i == 0) && num2 != null) {
                            if (getMessagesController().isForum(keyAt)) {
                                this.total_unread_count -= num2.intValue() > 0 ? 1 : 0;
                            } else {
                                this.total_unread_count -= num2.intValue();
                            }
                        }
                        if (i == 0) {
                            this.pushDialogs.remove(keyAt);
                            this.pushDialogsOverrideMention.remove(keyAt);
                            int i5 = 0;
                            while (i5 < this.pushMessages.size()) {
                                MessageObject messageObject = this.pushMessages.get(i5);
                                if (!messageObject.messageOwner.from_scheduled && messageObject.getDialogId() == keyAt && !messageObject.isStoryReactionPush) {
                                    if (isPersonalMessage(messageObject)) {
                                        this.personalCount--;
                                    }
                                    this.pushMessages.remove(i5);
                                    i5--;
                                    this.delayedPushMessages.remove(messageObject);
                                    long j = messageObject.messageOwner.peer_id.channel_id;
                                    long j2 = j != 0 ? -j : 0L;
                                    SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(j2);
                                    if (sparseArray != null) {
                                        sparseArray.remove(messageObject.getId());
                                        if (sparseArray.size() == 0) {
                                            this.pushMessagesDict.remove(j2);
                                        }
                                    }
                                    arrayList.add(messageObject);
                                }
                                i5++;
                            }
                        } else if (z2) {
                            if (getMessagesController().isForum(keyAt)) {
                                this.total_unread_count += i <= 0 ? 0 : 1;
                            } else {
                                this.total_unread_count += i;
                            }
                            this.pushDialogs.put(keyAt, Integer.valueOf(i));
                        }
                        i3++;
                    }
                    z2 = true;
                    if (this.notifyCheck) {
                        i = num.intValue();
                        z2 = true;
                    }
                    if (i == 0) {
                    }
                    if (i < 0) {
                    }
                    if (!z2) {
                    }
                    if (getMessagesController().isForum(keyAt)) {
                    }
                    if (i == 0) {
                    }
                    i3++;
                }
            }
            i = i4;
            z = false;
            if (!z) {
            }
            z2 = true;
            if (this.notifyCheck) {
            }
            if (i == 0) {
            }
            if (i < 0) {
            }
            if (!z2) {
            }
            if (getMessagesController().isForum(keyAt)) {
            }
            if (i == 0) {
            }
            i3++;
        }
        if (!arrayList.isEmpty()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda39
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$processDialogsUpdateRead$26(arrayList);
                }
            });
        }
        if (i2 != this.total_unread_count) {
            if (!this.notifyCheck) {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
            } else {
                scheduleNotificationDelay(this.lastOnlineFromOtherDevice > getConnectionsManager().getCurrentTime());
            }
            final int size = this.pushDialogs.size();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$processDialogsUpdateRead$27(size);
                }
            });
        }
        this.notifyCheck = false;
        if (this.showBadgeNumber) {
            setBadge(getTotalAllUnreadCount());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDialogsUpdateRead$26(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDialogsUpdateRead$27(int i) {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    public void processLoadedUnreadMessages(final LongSparseArray<Integer> longSparseArray, final ArrayList<TLRPC$Message> arrayList, final ArrayList<MessageObject> arrayList2, ArrayList<TLRPC$User> arrayList3, ArrayList<TLRPC$Chat> arrayList4, ArrayList<TLRPC$EncryptedChat> arrayList5, final Collection<StoryNotification> collection) {
        getMessagesController().putUsers(arrayList3, true);
        getMessagesController().putChats(arrayList4, true);
        getMessagesController().putEncryptedChats(arrayList5, true);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processLoadedUnreadMessages$31(arrayList, longSparseArray, arrayList2, collection);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedUnreadMessages$31(ArrayList arrayList, LongSparseArray longSparseArray, ArrayList arrayList2, Collection collection) {
        long j;
        long j2;
        boolean z;
        long j3;
        boolean z2;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        SharedPreferences sharedPreferences;
        MessageObject messageObject;
        SparseArray<MessageObject> sparseArray;
        long j4;
        long j5;
        int i;
        TLRPC$Message tLRPC$Message;
        boolean z3;
        boolean z4;
        SparseArray<MessageObject> sparseArray2;
        ArrayList arrayList3 = arrayList;
        this.pushDialogs.clear();
        this.pushMessages.clear();
        this.pushMessagesDict.clear();
        this.storyPushMessages.clear();
        this.storyPushMessagesDict.clear();
        boolean z5 = false;
        this.total_unread_count = 0;
        this.personalCount = 0;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        LongSparseArray longSparseArray2 = new LongSparseArray();
        long j6 = 0;
        if (arrayList3 != null) {
            int i2 = 0;
            while (i2 < arrayList.size()) {
                TLRPC$Message tLRPC$Message2 = (TLRPC$Message) arrayList3.get(i2);
                if (tLRPC$Message2 != null && ((tLRPC$MessageFwdHeader = tLRPC$Message2.fwd_from) == null || !tLRPC$MessageFwdHeader.imported)) {
                    TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message2.action;
                    if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetMessagesTTL) && (!tLRPC$Message2.silent || (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp) && !(tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserJoined)))) {
                        long j7 = tLRPC$Message2.peer_id.channel_id;
                        long j8 = j7 != j6 ? -j7 : j6;
                        SparseArray<MessageObject> sparseArray3 = this.pushMessagesDict.get(j8);
                        if (sparseArray3 == null || sparseArray3.indexOfKey(tLRPC$Message2.id) < 0) {
                            MessageObject messageObject2 = new MessageObject(this.currentAccount, tLRPC$Message2, z5, z5);
                            if (isPersonalMessage(messageObject2)) {
                                this.personalCount++;
                            }
                            sharedPreferences = notificationsSettings;
                            long dialogId = messageObject2.getDialogId();
                            long topicId = MessageObject.getTopicId(this.currentAccount, messageObject2.messageOwner, getMessagesController().isForum(messageObject2));
                            long fromChatId = messageObject2.messageOwner.mentioned ? messageObject2.getFromChatId() : dialogId;
                            int indexOfKey = longSparseArray2.indexOfKey(fromChatId);
                            if (indexOfKey >= 0 && topicId == 0) {
                                z4 = ((Boolean) longSparseArray2.valueAt(indexOfKey)).booleanValue();
                                messageObject = messageObject2;
                                sparseArray = sparseArray3;
                                i = i2;
                                j4 = dialogId;
                                j5 = j8;
                                tLRPC$Message = tLRPC$Message2;
                            } else {
                                messageObject = messageObject2;
                                sparseArray = sparseArray3;
                                j4 = dialogId;
                                j5 = j8;
                                i = i2;
                                tLRPC$Message = tLRPC$Message2;
                                int notifyOverride = getNotifyOverride(sharedPreferences, fromChatId, topicId);
                                if (notifyOverride == -1) {
                                    z3 = isGlobalNotificationsEnabled(fromChatId, messageObject.isReactionPush, messageObject.isStoryReactionPush);
                                } else {
                                    z3 = notifyOverride != 2;
                                }
                                z4 = z3;
                                longSparseArray2.put(fromChatId, Boolean.valueOf(z4));
                            }
                            if (z4 && (fromChatId != this.openedDialogId || !ApplicationLoader.isScreenOn)) {
                                if (sparseArray == null) {
                                    sparseArray2 = new SparseArray<>();
                                    this.pushMessagesDict.put(j5, sparseArray2);
                                } else {
                                    sparseArray2 = sparseArray;
                                }
                                sparseArray2.put(tLRPC$Message.id, messageObject);
                                appendMessage(messageObject);
                                if (j4 != fromChatId) {
                                    long j9 = j4;
                                    Integer num = this.pushDialogsOverrideMention.get(j9);
                                    this.pushDialogsOverrideMention.put(j9, Integer.valueOf(num == null ? 1 : num.intValue() + 1));
                                }
                            }
                            i2 = i + 1;
                            arrayList3 = arrayList;
                            notificationsSettings = sharedPreferences;
                            z5 = false;
                            j6 = 0;
                        }
                    }
                }
                i = i2;
                sharedPreferences = notificationsSettings;
                i2 = i + 1;
                arrayList3 = arrayList;
                notificationsSettings = sharedPreferences;
                z5 = false;
                j6 = 0;
            }
        }
        SharedPreferences sharedPreferences2 = notificationsSettings;
        for (int i3 = 0; i3 < longSparseArray.size(); i3++) {
            long keyAt = longSparseArray.keyAt(i3);
            int indexOfKey2 = longSparseArray2.indexOfKey(keyAt);
            if (indexOfKey2 >= 0) {
                z2 = ((Boolean) longSparseArray2.valueAt(indexOfKey2)).booleanValue();
            } else {
                int notifyOverride2 = getNotifyOverride(sharedPreferences2, keyAt, 0L);
                if (notifyOverride2 == -1) {
                    z2 = isGlobalNotificationsEnabled(keyAt, false, false);
                } else {
                    z2 = notifyOverride2 != 2;
                }
                longSparseArray2.put(keyAt, Boolean.valueOf(z2));
            }
            if (z2) {
                int intValue = ((Integer) longSparseArray.valueAt(i3)).intValue();
                this.pushDialogs.put(keyAt, Integer.valueOf(intValue));
                if (getMessagesController().isForum(keyAt)) {
                    this.total_unread_count += intValue > 0 ? 1 : 0;
                } else {
                    this.total_unread_count += intValue;
                }
            }
        }
        if (arrayList2 != null) {
            for (int i4 = 0; i4 < arrayList2.size(); i4++) {
                MessageObject messageObject3 = (MessageObject) arrayList2.get(i4);
                int id = messageObject3.getId();
                if (this.pushMessagesDict.indexOfKey(id) < 0) {
                    if (isPersonalMessage(messageObject3)) {
                        this.personalCount++;
                    }
                    long dialogId2 = messageObject3.getDialogId();
                    long topicId2 = MessageObject.getTopicId(this.currentAccount, messageObject3.messageOwner, getMessagesController().isForum(messageObject3));
                    TLRPC$Message tLRPC$Message3 = messageObject3.messageOwner;
                    long j10 = tLRPC$Message3.random_id;
                    long fromChatId2 = tLRPC$Message3.mentioned ? messageObject3.getFromChatId() : dialogId2;
                    int indexOfKey3 = longSparseArray2.indexOfKey(fromChatId2);
                    if (indexOfKey3 >= 0 && topicId2 == 0) {
                        j = j10;
                        long j11 = fromChatId2;
                        z = ((Boolean) longSparseArray2.valueAt(indexOfKey3)).booleanValue();
                        j2 = j11;
                    } else {
                        long j12 = fromChatId2;
                        j = j10;
                        int notifyOverride3 = getNotifyOverride(sharedPreferences2, j12, topicId2);
                        if (notifyOverride3 == -1) {
                            j2 = j12;
                            z = isGlobalNotificationsEnabled(j2, messageObject3.isReactionPush, messageObject3.isStoryReactionPush);
                        } else {
                            j2 = j12;
                            z = notifyOverride3 != 2;
                        }
                        longSparseArray2.put(j2, Boolean.valueOf(z));
                    }
                    if (z && (j2 != this.openedDialogId || !ApplicationLoader.isScreenOn)) {
                        if (id != 0) {
                            if (messageObject3.isStoryReactionPush) {
                                j3 = messageObject3.getDialogId();
                            } else {
                                long j13 = messageObject3.messageOwner.peer_id.channel_id;
                                j3 = j13 != 0 ? -j13 : 0L;
                            }
                            SparseArray<MessageObject> sparseArray4 = this.pushMessagesDict.get(j3);
                            if (sparseArray4 == null) {
                                sparseArray4 = new SparseArray<>();
                                this.pushMessagesDict.put(j3, sparseArray4);
                            }
                            sparseArray4.put(id, messageObject3);
                        } else {
                            long j14 = j;
                            if (j14 != 0) {
                                this.fcmRandomMessagesDict.put(j14, messageObject3);
                            }
                        }
                        appendMessage(messageObject3);
                        if (dialogId2 != j2) {
                            Integer num2 = this.pushDialogsOverrideMention.get(dialogId2);
                            this.pushDialogsOverrideMention.put(dialogId2, Integer.valueOf(num2 == null ? 1 : num2.intValue() + 1));
                        }
                        Integer num3 = this.pushDialogs.get(j2);
                        int intValue2 = num3 != null ? num3.intValue() + 1 : 1;
                        if (getMessagesController().isForum(j2)) {
                            if (num3 != null) {
                                this.total_unread_count -= num3.intValue() > 0 ? 1 : 0;
                            }
                            this.total_unread_count += intValue2 > 0 ? 1 : 0;
                        } else {
                            if (num3 != null) {
                                this.total_unread_count -= num3.intValue();
                            }
                            this.total_unread_count += intValue2;
                        }
                        this.pushDialogs.put(j2, Integer.valueOf(intValue2));
                    }
                }
            }
        }
        if (collection != null) {
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                StoryNotification storyNotification = (StoryNotification) it.next();
                long j15 = storyNotification.dialogId;
                StoryNotification storyNotification2 = this.storyPushMessagesDict.get(j15);
                if (storyNotification2 != null) {
                    storyNotification2.dateByIds.putAll(storyNotification.dateByIds);
                } else {
                    this.storyPushMessages.add(storyNotification);
                    this.storyPushMessagesDict.put(j15, storyNotification);
                }
            }
            Collections.sort(this.storyPushMessages, Comparator$-CC.comparingLong(new ToLongFunction() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda49
                @Override // j$.util.function.ToLongFunction
                public final long applyAsLong(Object obj) {
                    long j16;
                    j16 = ((NotificationsController.StoryNotification) obj).date;
                    return j16;
                }
            }));
        }
        final int size = this.pushDialogs.size();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processLoadedUnreadMessages$30(size);
            }
        });
        showOrUpdateNotification(SystemClock.elapsedRealtime() / 1000 < 60);
        if (this.showBadgeNumber) {
            setBadge(getTotalAllUnreadCount());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedUnreadMessages$30(int i) {
        if (this.total_unread_count == 0) {
            this.popupMessages.clear();
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pushMessagesUpdated, new Object[0]);
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    private int getTotalAllUnreadCount() {
        int size;
        int i = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            if (UserConfig.getInstance(i2).isClientActivated()) {
                NotificationsController notificationsController = getInstance(i2);
                if (notificationsController.showBadgeNumber) {
                    if (notificationsController.showBadgeMessages) {
                        if (notificationsController.showBadgeMuted) {
                            try {
                                ArrayList arrayList = new ArrayList(MessagesController.getInstance(i2).allDialogs);
                                int size2 = arrayList.size();
                                for (int i3 = 0; i3 < size2; i3++) {
                                    TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) arrayList.get(i3);
                                    if ((tLRPC$Dialog == null || !DialogObject.isChatDialog(tLRPC$Dialog.id) || !ChatObject.isNotInChat(getMessagesController().getChat(Long.valueOf(-tLRPC$Dialog.id)))) && tLRPC$Dialog != null) {
                                        i += MessagesController.getInstance(i2).getDialogUnreadCount(tLRPC$Dialog);
                                    }
                                }
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        } else {
                            size = notificationsController.total_unread_count;
                        }
                    } else if (notificationsController.showBadgeMuted) {
                        try {
                            int size3 = MessagesController.getInstance(i2).allDialogs.size();
                            for (int i4 = 0; i4 < size3; i4++) {
                                TLRPC$Dialog tLRPC$Dialog2 = MessagesController.getInstance(i2).allDialogs.get(i4);
                                if ((!DialogObject.isChatDialog(tLRPC$Dialog2.id) || !ChatObject.isNotInChat(getMessagesController().getChat(Long.valueOf(-tLRPC$Dialog2.id)))) && MessagesController.getInstance(i2).getDialogUnreadCount(tLRPC$Dialog2) != 0) {
                                    i++;
                                }
                            }
                        } catch (Exception e2) {
                            FileLog.e((Throwable) e2, false);
                        }
                    } else {
                        size = notificationsController.pushDialogs.size();
                    }
                    i += size;
                }
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateBadge$32() {
        setBadge(getTotalAllUnreadCount());
    }

    public void updateBadge() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$updateBadge$32();
            }
        });
    }

    private void setBadge(int i) {
        if (this.lastBadgeCount == i) {
            return;
        }
        this.lastBadgeCount = i;
        NotificationBadge.applyCount(i);
    }

    /* JADX WARN: Code restructure failed: missing block: B:154:0x024f, code lost:
        if (r12.getBoolean("EnablePreviewAll", true) == false) goto L847;
     */
    /* JADX WARN: Code restructure failed: missing block: B:161:0x025d, code lost:
        if (r12.getBoolean("EnablePreviewGroup", r15) != false) goto L135;
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x0267, code lost:
        if (r12.getBoolean(r24, r15) != false) goto L135;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x0269, code lost:
        r1 = r26.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x0279, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageService) == false) goto L675;
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x027b, code lost:
        r27[0] = null;
        r2 = r1.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x0283, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetSameChatWallPaper) == false) goto L141;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x028d, code lost:
        return org.telegram.messenger.LocaleController.getString("WallpaperSameNotification", org.telegram.messenger.R.string.WallpaperSameNotification);
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x0290, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatWallPaper) == false) goto L145;
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x029a, code lost:
        return org.telegram.messenger.LocaleController.getString("WallpaperNotification", org.telegram.messenger.R.string.WallpaperNotification);
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x029d, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGeoProximityReached) == false) goto L149;
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x02a5, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x02a8, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserJoined) != false) goto L673;
     */
    /* JADX WARN: Code restructure failed: missing block: B:182:0x02ac, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionContactSignUp) == false) goto L153;
     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x02b2, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserUpdatedPhoto) == false) goto L157;
     */
    /* JADX WARN: Code restructure failed: missing block: B:187:0x02c2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationContactNewPhoto", org.telegram.messenger.R.string.NotificationContactNewPhoto, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x02c6, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionLoginUnknownLocation) == false) goto L161;
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x02c8, code lost:
        r1 = org.telegram.messenger.LocaleController.formatString("formatDateAtTime", org.telegram.messenger.R.string.formatDateAtTime, org.telegram.messenger.LocaleController.getInstance().getFormatterYear().format(r26.messageOwner.date * 1000), org.telegram.messenger.LocaleController.getInstance().getFormatterDay().format(r26.messageOwner.date * 1000));
        r2 = org.telegram.messenger.R.string.NotificationUnrecognizedDevice;
        r0 = r26.messageOwner.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x0328, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationUnrecognizedDevice", r2, getUserConfig().getCurrentUser().first_name, r1, r0.title, r0.address);
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x032b, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGameScore) != false) goto L671;
     */
    /* JADX WARN: Code restructure failed: missing block: B:195:0x032f, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPaymentSent) == false) goto L165;
     */
    /* JADX WARN: Code restructure failed: missing block: B:198:0x0335, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPhoneCall) == false) goto L173;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x0339, code lost:
        if (r2.video == false) goto L171;
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x0343, code lost:
        return org.telegram.messenger.LocaleController.getString("CallMessageVideoIncomingMissed", org.telegram.messenger.R.string.CallMessageVideoIncomingMissed);
     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x034c, code lost:
        return org.telegram.messenger.LocaleController.getString("CallMessageIncomingMissed", org.telegram.messenger.R.string.CallMessageIncomingMissed);
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x034f, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser) == false) goto L220;
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x0351, code lost:
        r3 = r2.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:208:0x0357, code lost:
        if (r3 != 0) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x0360, code lost:
        if (r2.users.size() != 1) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:211:0x0362, code lost:
        r3 = r26.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:213:0x0377, code lost:
        if (r3 == 0) goto L205;
     */
    /* JADX WARN: Code restructure failed: missing block: B:215:0x0381, code lost:
        if (r26.messageOwner.peer_id.channel_id == 0) goto L188;
     */
    /* JADX WARN: Code restructure failed: missing block: B:217:0x0385, code lost:
        if (r8.megagroup != false) goto L188;
     */
    /* JADX WARN: Code restructure failed: missing block: B:219:0x039a, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.R.string.ChannelAddedByNotification, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:221:0x039d, code lost:
        if (r3 != r19) goto L192;
     */
    /* JADX WARN: Code restructure failed: missing block: B:223:0x03b2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.R.string.NotificationInvitedToGroup, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:224:0x03b3, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:225:0x03bf, code lost:
        if (r0 != null) goto L195;
     */
    /* JADX WARN: Code restructure failed: missing block: B:226:0x03c1, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:229:0x03c7, code lost:
        if (r9 != r0.id) goto L203;
     */
    /* JADX WARN: Code restructure failed: missing block: B:231:0x03cb, code lost:
        if (r8.megagroup == false) goto L201;
     */
    /* JADX WARN: Code restructure failed: missing block: B:233:0x03e0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.R.string.NotificationGroupAddSelfMega, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:235:0x03f4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.R.string.NotificationGroupAddSelf, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:237:0x040e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r7, r8.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:238:0x040f, code lost:
        r1 = new java.lang.StringBuilder();
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x041f, code lost:
        if (r2 >= r26.messageOwner.action.users.size()) goto L217;
     */
    /* JADX WARN: Code restructure failed: missing block: B:241:0x0421, code lost:
        r3 = getMessagesController().getUser(r26.messageOwner.action.users.get(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:242:0x0435, code lost:
        if (r3 == null) goto L216;
     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x0437, code lost:
        r3 = org.telegram.messenger.UserObject.getUserName(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x043f, code lost:
        if (r1.length() == 0) goto L213;
     */
    /* JADX WARN: Code restructure failed: missing block: B:245:0x0441, code lost:
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:246:0x0446, code lost:
        r1.append(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:247:0x0449, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:249:0x0465, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r7, r8.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:251:0x0469, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCall) == false) goto L224;
     */
    /* JADX WARN: Code restructure failed: missing block: B:253:0x047d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.R.string.NotificationGroupCreatedCall, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:255:0x0480, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled) == false) goto L228;
     */
    /* JADX WARN: Code restructure failed: missing block: B:257:0x0488, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:259:0x048b, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall) == false) goto L261;
     */
    /* JADX WARN: Code restructure failed: missing block: B:260:0x048d, code lost:
        r3 = r2.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:261:0x0493, code lost:
        if (r3 != 0) goto L235;
     */
    /* JADX WARN: Code restructure failed: missing block: B:263:0x049c, code lost:
        if (r2.users.size() != 1) goto L235;
     */
    /* JADX WARN: Code restructure failed: missing block: B:264:0x049e, code lost:
        r3 = r26.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x04b3, code lost:
        if (r3 == 0) goto L246;
     */
    /* JADX WARN: Code restructure failed: missing block: B:268:0x04b7, code lost:
        if (r3 != r19) goto L241;
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x04cc, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.R.string.NotificationGroupInvitedYouToCall, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:271:0x04cd, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x04d9, code lost:
        if (r0 != null) goto L244;
     */
    /* JADX WARN: Code restructure failed: missing block: B:273:0x04db, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x04f6, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r7, r8.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:277:0x04f7, code lost:
        r1 = new java.lang.StringBuilder();
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x0507, code lost:
        if (r2 >= r26.messageOwner.action.users.size()) goto L258;
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x0509, code lost:
        r3 = getMessagesController().getUser(r26.messageOwner.action.users.get(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x051d, code lost:
        if (r3 == null) goto L257;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x051f, code lost:
        r3 = org.telegram.messenger.UserObject.getUserName(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x0527, code lost:
        if (r1.length() == 0) goto L254;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x0529, code lost:
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x052e, code lost:
        r1.append(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x0531, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x054d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r7, r8.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x0550, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGiftCode) == false) goto L265;
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x055a, code lost:
        return org.telegram.messenger.LocaleController.getString("BoostingReceivedGiftNoName", org.telegram.messenger.R.string.BoostingReceivedGiftNoName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x055d, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink) == false) goto L269;
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x0573, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.R.string.NotificationInvitedToGroupByLink, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x057a, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle) == false) goto L273;
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x058c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.R.string.NotificationEditedGroupName, r7, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x058f, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto) != false) goto L655;
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x0593, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto) == false) goto L277;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x0599, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser) == false) goto L292;
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x059b, code lost:
        r1 = r2.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x059f, code lost:
        if (r1 != r19) goto L283;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x05b4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.R.string.NotificationGroupKickYou, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x05ba, code lost:
        if (r1 != r9) goto L287;
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x05cc, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.R.string.NotificationGroupLeftMember, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x05cd, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r26.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x05df, code lost:
        if (r0 != null) goto L290;
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x05e1, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x05fd, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.R.string.NotificationGroupKickMember, r7, r8.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x0600, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatCreate) == false) goto L296;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x0608, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x060b, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate) == false) goto L300;
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x0613, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x0616, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo) == false) goto L304;
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x0628, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x062d, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) == false) goto L308;
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x063d, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x0640, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken) == false) goto L312;
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x0648, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x064b, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPinMessage) == false) goto L634;
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x0651, code lost:
        if (r8 == null) goto L424;
     */
    /* JADX WARN: Code restructure failed: missing block: B:347:0x0657, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r8) == false) goto L320;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x065b, code lost:
        if (r8.megagroup == false) goto L424;
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x065d, code lost:
        r0 = r26.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x065f, code lost:
        if (r0 != null) goto L324;
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x0674, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x067c, code lost:
        if (r0.isMusic() == false) goto L328;
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x068e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.R.string.NotificationActionPinnedMusic, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x0695, code lost:
        if (r0.isVideo() == false) goto L338;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x069b, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L336;
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x06a5, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L336;
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x06ce, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "📹 " + r0.messageOwner.message, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x06e2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.R.string.NotificationActionPinnedVideo, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:369:0x06e7, code lost:
        if (r0.isGif() == false) goto L348;
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x06ed, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L346;
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x06f7, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L346;
     */
    /* JADX WARN: Code restructure failed: missing block: B:375:0x0720, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "🎬 " + r0.messageOwner.message, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:377:0x0734, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.R.string.NotificationActionPinnedGif, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x073c, code lost:
        if (r0.isVoice() == false) goto L352;
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x074e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.R.string.NotificationActionPinnedVoice, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x0753, code lost:
        if (r0.isRoundVideo() == false) goto L356;
     */
    /* JADX WARN: Code restructure failed: missing block: B:385:0x0765, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.R.string.NotificationActionPinnedRound, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:387:0x076a, code lost:
        if (r0.isSticker() != false) goto L418;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x0770, code lost:
        if (r0.isAnimatedSticker() == false) goto L360;
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x0774, code lost:
        r3 = r0.messageOwner;
        r5 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x077a, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L370;
     */
    /* JADX WARN: Code restructure failed: missing block: B:394:0x0780, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L368;
     */
    /* JADX WARN: Code restructure failed: missing block: B:396:0x0788, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L368;
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:0x07b1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "📎 " + r0.messageOwner.message, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x07c5, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.R.string.NotificationActionPinnedFile, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:402:0x07c8, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L416;
     */
    /* JADX WARN: Code restructure failed: missing block: B:404:0x07cc, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L374;
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x07d2, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L378;
     */
    /* JADX WARN: Code restructure failed: missing block: B:409:0x07e7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.R.string.NotificationActionPinnedGeoLive, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x07ec, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L382;
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x07ee, code lost:
        r5 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x080c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.R.string.NotificationActionPinnedContact2, r7, r8.title, org.telegram.messenger.ContactsController.formatName(r5.first_name, r5.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:415:0x080f, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L390;
     */
    /* JADX WARN: Code restructure failed: missing block: B:416:0x0811, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r5).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x0817, code lost:
        if (r0.quiz == false) goto L388;
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x0833, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.R.string.NotificationActionPinnedQuiz2, r7, r8.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x084e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.R.string.NotificationActionPinnedPoll2, r7, r8.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x0851, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L400;
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x0857, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L398;
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x085f, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L398;
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x0888, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "🖼 " + r0.messageOwner.message, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x089c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.R.string.NotificationActionPinnedPhoto, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:433:0x08a2, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L404;
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x08b4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.R.string.NotificationActionPinnedGame, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:436:0x08b5, code lost:
        r3 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x08b7, code lost:
        if (r3 == null) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x08bd, code lost:
        if (r3.length() <= 0) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:440:0x08bf, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:441:0x08c5, code lost:
        if (r0.length() <= 20) goto L413;
     */
    /* JADX WARN: Code restructure failed: missing block: B:442:0x08c7, code lost:
        r3 = new java.lang.StringBuilder();
        r5 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:443:0x08dc, code lost:
        r5 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:444:0x08dd, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedText;
        r2 = new java.lang.Object[3];
        r2[r5] = r7;
        r2[1] = r0;
        r2[2] = r8.title;
     */
    /* JADX WARN: Code restructure failed: missing block: B:445:0x08f0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:447:0x0904, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:449:0x0918, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.R.string.NotificationActionPinnedGeo, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:450:0x0919, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:451:0x091f, code lost:
        if (r0 == null) goto L422;
     */
    /* JADX WARN: Code restructure failed: missing block: B:453:0x0935, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmoji, r7, r8.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:455:0x0947, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.R.string.NotificationActionPinnedSticker, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:0x0949, code lost:
        if (r8 == null) goto L530;
     */
    /* JADX WARN: Code restructure failed: missing block: B:458:0x094b, code lost:
        r0 = r26.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x094d, code lost:
        if (r0 != null) goto L430;
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x095e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0964, code lost:
        if (r0.isMusic() == false) goto L434;
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x0974, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.R.string.NotificationActionPinnedMusicChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x097b, code lost:
        if (r0.isVideo() == false) goto L444;
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x0981, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L442;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00b3, code lost:
        if (r12.getBoolean("EnablePreviewGroup", true) != false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x098b, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L442;
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x09b1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r8.title, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:475:0x09c2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.R.string.NotificationActionPinnedVideoChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x09c7, code lost:
        if (r0.isGif() == false) goto L454;
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x09cd, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L452;
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x09d7, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L452;
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x09fd, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r8.title, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0a0e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.R.string.NotificationActionPinnedGifChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:487:0x0a15, code lost:
        if (r0.isVoice() == false) goto L458;
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x0a25, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.R.string.NotificationActionPinnedVoiceChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x0a2a, code lost:
        if (r0.isRoundVideo() == false) goto L462;
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x0a3a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.R.string.NotificationActionPinnedRoundChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:0x0a3f, code lost:
        if (r0.isSticker() != false) goto L524;
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x0a45, code lost:
        if (r0.isAnimatedSticker() == false) goto L466;
     */
    /* JADX WARN: Code restructure failed: missing block: B:499:0x0a49, code lost:
        r3 = r0.messageOwner;
        r5 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:500:0x0a4f, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L476;
     */
    /* JADX WARN: Code restructure failed: missing block: B:502:0x0a55, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L474;
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0a5d, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L474;
     */
    /* JADX WARN: Code restructure failed: missing block: B:506:0x0a83, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r8.title, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x0a94, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.R.string.NotificationActionPinnedFileChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x0a97, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L522;
     */
    /* JADX WARN: Code restructure failed: missing block: B:512:0x0a9b, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L480;
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x0aa1, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L484;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0ab3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:519:0x0ab7, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L488;
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x0ab9, code lost:
        r5 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x0ad5, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.R.string.NotificationActionPinnedContactChannel2, r8.title, org.telegram.messenger.ContactsController.formatName(r5.first_name, r5.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x0ad8, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L496;
     */
    /* JADX WARN: Code restructure failed: missing block: B:524:0x0ada, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r5).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:525:0x0ae0, code lost:
        if (r0.quiz == false) goto L494;
     */
    /* JADX WARN: Code restructure failed: missing block: B:527:0x0af9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.R.string.NotificationActionPinnedQuizChannel2, r8.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x0b11, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.R.string.NotificationActionPinnedPollChannel2, r8.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00bf, code lost:
        if (r12.getBoolean("EnablePreviewChannel", r2) == false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x0b14, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L506;
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0b1a, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L504;
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0b22, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L504;
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0b48, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r8.title, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0b59, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.R.string.NotificationActionPinnedPhotoChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x0b5e, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L510;
     */
    /* JADX WARN: Code restructure failed: missing block: B:543:0x0b6e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.R.string.NotificationActionPinnedGameChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:544:0x0b6f, code lost:
        r3 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x0b71, code lost:
        if (r3 == null) goto L520;
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0b77, code lost:
        if (r3.length() <= 0) goto L520;
     */
    /* JADX WARN: Code restructure failed: missing block: B:548:0x0b79, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0b7f, code lost:
        if (r0.length() <= 20) goto L519;
     */
    /* JADX WARN: Code restructure failed: missing block: B:550:0x0b81, code lost:
        r3 = new java.lang.StringBuilder();
        r9 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:551:0x0b96, code lost:
        r9 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:552:0x0b97, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextChannel;
        r2 = new java.lang.Object[2];
        r2[r9] = r8.title;
        r2[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:553:0x0ba7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:555:0x0bb8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:557:0x0bc9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:558:0x0bca, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:559:0x0bcf, code lost:
        if (r0 == null) goto L528;
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0be3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiChannel, r8.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:563:0x0bf3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:564:0x0bf4, code lost:
        r0 = r26.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:565:0x0bf7, code lost:
        if (r0 != null) goto L534;
     */
    /* JADX WARN: Code restructure failed: missing block: B:567:0x0c05, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.R.string.NotificationActionPinnedNoTextUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x0c0a, code lost:
        if (r0.isMusic() == false) goto L538;
     */
    /* JADX WARN: Code restructure failed: missing block: B:571:0x0c18, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicUser", org.telegram.messenger.R.string.NotificationActionPinnedMusicUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:573:0x0c1f, code lost:
        if (r0.isVideo() == false) goto L548;
     */
    /* JADX WARN: Code restructure failed: missing block: B:575:0x0c25, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L546;
     */
    /* JADX WARN: Code restructure failed: missing block: B:577:0x0c2f, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L546;
     */
    /* JADX WARN: Code restructure failed: missing block: B:579:0x0c53, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r7, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:581:0x0c62, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoUser", org.telegram.messenger.R.string.NotificationActionPinnedVideoUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:583:0x0c67, code lost:
        if (r0.isGif() == false) goto L558;
     */
    /* JADX WARN: Code restructure failed: missing block: B:585:0x0c6d, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L556;
     */
    /* JADX WARN: Code restructure failed: missing block: B:587:0x0c77, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L556;
     */
    /* JADX WARN: Code restructure failed: missing block: B:589:0x0c9b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r7, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:591:0x0caa, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifUser", org.telegram.messenger.R.string.NotificationActionPinnedGifUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:593:0x0cb1, code lost:
        if (r0.isVoice() == false) goto L562;
     */
    /* JADX WARN: Code restructure failed: missing block: B:595:0x0cbf, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceUser", org.telegram.messenger.R.string.NotificationActionPinnedVoiceUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:597:0x0cc4, code lost:
        if (r0.isRoundVideo() == false) goto L566;
     */
    /* JADX WARN: Code restructure failed: missing block: B:599:0x0cd2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundUser", org.telegram.messenger.R.string.NotificationActionPinnedRoundUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:601:0x0cd7, code lost:
        if (r0.isSticker() != false) goto L628;
     */
    /* JADX WARN: Code restructure failed: missing block: B:603:0x0cdd, code lost:
        if (r0.isAnimatedSticker() == false) goto L570;
     */
    /* JADX WARN: Code restructure failed: missing block: B:605:0x0ce1, code lost:
        r3 = r0.messageOwner;
        r5 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:606:0x0ce7, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L580;
     */
    /* JADX WARN: Code restructure failed: missing block: B:608:0x0ced, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L578;
     */
    /* JADX WARN: Code restructure failed: missing block: B:610:0x0cf5, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L578;
     */
    /* JADX WARN: Code restructure failed: missing block: B:612:0x0d19, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r7, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:614:0x0d28, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileUser", org.telegram.messenger.R.string.NotificationActionPinnedFileUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:616:0x0d2b, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L626;
     */
    /* JADX WARN: Code restructure failed: missing block: B:618:0x0d2f, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L584;
     */
    /* JADX WARN: Code restructure failed: missing block: B:621:0x0d35, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L588;
     */
    /* JADX WARN: Code restructure failed: missing block: B:623:0x0d45, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveUser", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x0d49, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L592;
     */
    /* JADX WARN: Code restructure failed: missing block: B:626:0x0d4b, code lost:
        r5 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:627:0x0d65, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactUser", org.telegram.messenger.R.string.NotificationActionPinnedContactUser, r7, org.telegram.messenger.ContactsController.formatName(r5.first_name, r5.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:629:0x0d68, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L600;
     */
    /* JADX WARN: Code restructure failed: missing block: B:630:0x0d6a, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r5).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:0x0d70, code lost:
        if (r0.quiz == false) goto L598;
     */
    /* JADX WARN: Code restructure failed: missing block: B:633:0x0d87, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizUser", org.telegram.messenger.R.string.NotificationActionPinnedQuizUser, r7, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:635:0x0d9d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollUser", org.telegram.messenger.R.string.NotificationActionPinnedPollUser, r7, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:637:0x0da0, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L610;
     */
    /* JADX WARN: Code restructure failed: missing block: B:639:0x0da6, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L608;
     */
    /* JADX WARN: Code restructure failed: missing block: B:641:0x0dae, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L608;
     */
    /* JADX WARN: Code restructure failed: missing block: B:643:0x0dd2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r7, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:645:0x0de1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoUser", org.telegram.messenger.R.string.NotificationActionPinnedPhotoUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:647:0x0de6, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L614;
     */
    /* JADX WARN: Code restructure failed: missing block: B:649:0x0df4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameUser", org.telegram.messenger.R.string.NotificationActionPinnedGameUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:650:0x0df5, code lost:
        r3 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:651:0x0df7, code lost:
        if (r3 == null) goto L624;
     */
    /* JADX WARN: Code restructure failed: missing block: B:653:0x0dfd, code lost:
        if (r3.length() <= 0) goto L624;
     */
    /* JADX WARN: Code restructure failed: missing block: B:654:0x0dff, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:655:0x0e05, code lost:
        if (r0.length() <= 20) goto L623;
     */
    /* JADX WARN: Code restructure failed: missing block: B:656:0x0e07, code lost:
        r3 = new java.lang.StringBuilder();
        r5 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:657:0x0e1c, code lost:
        r5 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:658:0x0e1d, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextUser;
        r2 = new java.lang.Object[2];
        r2[r5] = r7;
        r2[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:659:0x0e2b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:661:0x0e3a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.R.string.NotificationActionPinnedNoTextUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:663:0x0e49, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoUser", org.telegram.messenger.R.string.NotificationActionPinnedGeoUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:664:0x0e4a, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:665:0x0e50, code lost:
        if (r0 == null) goto L632;
     */
    /* JADX WARN: Code restructure failed: missing block: B:667:0x0e61, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiUser", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiUser, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:669:0x0e6e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerUser", org.telegram.messenger.R.string.NotificationActionPinnedStickerUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:671:0x0e71, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) == false) goto L650;
     */
    /* JADX WARN: Code restructure failed: missing block: B:672:0x0e73, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) r2).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:673:0x0e7b, code lost:
        if (android.text.TextUtils.isEmpty(r0) == false) goto L644;
     */
    /* JADX WARN: Code restructure failed: missing block: B:675:0x0e7f, code lost:
        if (r3 != r19) goto L642;
     */
    /* JADX WARN: Code restructure failed: missing block: B:679:0x0ea3, code lost:
        if (r3 != r19) goto L648;
     */
    /* JADX WARN: Code restructure failed: missing block: B:682:0x0ec1, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.R.string.ChatThemeChangedTo, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:684:0x0ec4, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest) == false) goto L654;
     */
    /* JADX WARN: Code restructure failed: missing block: B:686:0x0ecc, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:687:0x0ecd, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:690:0x0ed7, code lost:
        if (r1.peer_id.channel_id == 0) goto L665;
     */
    /* JADX WARN: Code restructure failed: missing block: B:692:0x0edb, code lost:
        if (r8.megagroup != false) goto L665;
     */
    /* JADX WARN: Code restructure failed: missing block: B:694:0x0ee1, code lost:
        if (r26.isVideoAvatar() == false) goto L663;
     */
    /* JADX WARN: Code restructure failed: missing block: B:696:0x0ef3, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.R.string.ChannelVideoEditNotification, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:698:0x0f04, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.R.string.ChannelPhotoEditNotification, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:700:0x0f0a, code lost:
        if (r26.isVideoAvatar() == false) goto L669;
     */
    /* JADX WARN: Code restructure failed: missing block: B:702:0x0f1e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.R.string.NotificationEditedGroupVideo, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:704:0x0f31, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.R.string.NotificationEditedGroupPhoto, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:706:0x0f38, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:708:0x0f47, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationContactJoined", org.telegram.messenger.R.string.NotificationContactJoined, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:710:0x0f4c, code lost:
        if (r26.isMediaEmpty() == false) goto L683;
     */
    /* JADX WARN: Code restructure failed: missing block: B:712:0x0f56, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageOwner.message) != false) goto L681;
     */
    /* JADX WARN: Code restructure failed: missing block: B:714:0x0f5c, code lost:
        return replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:716:0x0f65, code lost:
        return org.telegram.messenger.LocaleController.getString(r23, org.telegram.messenger.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:717:0x0f66, code lost:
        r1 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:718:0x0f6c, code lost:
        if (r26.type != 29) goto L717;
     */
    /* JADX WARN: Code restructure failed: missing block: B:720:0x0f74, code lost:
        if ((org.telegram.messenger.MessageObject.getMedia(r26) instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) == false) goto L717;
     */
    /* JADX WARN: Code restructure failed: missing block: B:721:0x0f76, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) org.telegram.messenger.MessageObject.getMedia(r26);
        r1 = r0.extended_media.size();
        r2 = 0;
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:722:0x0f84, code lost:
        if (r2 >= r1) goto L716;
     */
    /* JADX WARN: Code restructure failed: missing block: B:723:0x0f86, code lost:
        r4 = r0.extended_media.get(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:724:0x0f90, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageExtendedMedia) == false) goto L712;
     */
    /* JADX WARN: Code restructure failed: missing block: B:725:0x0f92, code lost:
        r3 = ((org.telegram.tgnet.TLRPC$TL_messageExtendedMedia) r4).media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x0f98, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L711;
     */
    /* JADX WARN: Code restructure failed: missing block: B:728:0x0fa0, code lost:
        if (org.telegram.messenger.MessageObject.isVideoDocument(r3.document) == false) goto L711;
     */
    /* JADX WARN: Code restructure failed: missing block: B:729:0x0fa2, code lost:
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:730:0x0fa4, code lost:
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:732:0x0fa8, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview) == false) goto L696;
     */
    /* JADX WARN: Code restructure failed: missing block: B:734:0x0fb0, code lost:
        if ((((org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview) r4).flags & 4) == 0) goto L711;
     */
    /* JADX WARN: Code restructure failed: missing block: B:736:0x0fb3, code lost:
        if (r3 == false) goto L697;
     */
    /* JADX WARN: Code restructure failed: missing block: B:738:0x0fb6, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:739:0x0fb9, code lost:
        r0 = org.telegram.messenger.R.string.AttachPaidMedia;
        r4 = new java.lang.Object[1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:740:0x0fbe, code lost:
        if (r1 != 1) goto L707;
     */
    /* JADX WARN: Code restructure failed: missing block: B:741:0x0fc0, code lost:
        if (r3 == false) goto L706;
     */
    /* JADX WARN: Code restructure failed: missing block: B:742:0x0fc2, code lost:
        r1 = org.telegram.messenger.R.string.AttachVideo;
     */
    /* JADX WARN: Code restructure failed: missing block: B:743:0x0fc5, code lost:
        r1 = org.telegram.messenger.R.string.AttachPhoto;
     */
    /* JADX WARN: Code restructure failed: missing block: B:744:0x0fc7, code lost:
        r1 = org.telegram.messenger.LocaleController.getString(r1);
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:745:0x0fcd, code lost:
        if (r3 == false) goto L710;
     */
    /* JADX WARN: Code restructure failed: missing block: B:746:0x0fcf, code lost:
        r2 = "Media";
     */
    /* JADX WARN: Code restructure failed: missing block: B:747:0x0fd2, code lost:
        r2 = "Photos";
     */
    /* JADX WARN: Code restructure failed: missing block: B:748:0x0fd4, code lost:
        r3 = 0;
        r1 = org.telegram.messenger.LocaleController.formatPluralString(r2, r1, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:749:0x0fdb, code lost:
        r4[r3] = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:750:0x0fe1, code lost:
        return org.telegram.messenger.LocaleController.formatString(r0, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x0fe6, code lost:
        if (r26.isVoiceOnce() == false) goto L721;
     */
    /* JADX WARN: Code restructure failed: missing block: B:754:0x0fee, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachOnceAudio);
     */
    /* JADX WARN: Code restructure failed: missing block: B:756:0x0ff3, code lost:
        if (r26.isRoundOnce() == false) goto L725;
     */
    /* JADX WARN: Code restructure failed: missing block: B:758:0x0ffb, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachOnceRound);
     */
    /* JADX WARN: Code restructure failed: missing block: B:759:0x0ffc, code lost:
        r2 = r26.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:760:0x1002, code lost:
        if ((r2.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L739;
     */
    /* JADX WARN: Code restructure failed: missing block: B:762:0x1008, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L733;
     */
    /* JADX WARN: Code restructure failed: missing block: B:764:0x1010, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L733;
     */
    /* JADX WARN: Code restructure failed: missing block: B:766:0x1025, code lost:
        return "🖼 " + replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:768:0x102c, code lost:
        if (r26.messageOwner.media.ttl_seconds == 0) goto L737;
     */
    /* JADX WARN: Code restructure failed: missing block: B:770:0x1036, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDestructingPhoto", org.telegram.messenger.R.string.AttachDestructingPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:772:0x103f, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachPhoto", org.telegram.messenger.R.string.AttachPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:774:0x1044, code lost:
        if (r26.isVideo() == false) goto L753;
     */
    /* JADX WARN: Code restructure failed: missing block: B:776:0x104a, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L747;
     */
    /* JADX WARN: Code restructure failed: missing block: B:778:0x1054, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageOwner.message) != false) goto L747;
     */
    /* JADX WARN: Code restructure failed: missing block: B:780:0x1069, code lost:
        return "📹 " + replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:782:0x1070, code lost:
        if (r26.messageOwner.media.ttl_seconds == 0) goto L751;
     */
    /* JADX WARN: Code restructure failed: missing block: B:784:0x107a, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDestructingVideo", org.telegram.messenger.R.string.AttachDestructingVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:786:0x1083, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachVideo", org.telegram.messenger.R.string.AttachVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:788:0x1088, code lost:
        if (r26.isGame() == false) goto L757;
     */
    /* JADX WARN: Code restructure failed: missing block: B:790:0x1092, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachGame", org.telegram.messenger.R.string.AttachGame);
     */
    /* JADX WARN: Code restructure failed: missing block: B:792:0x1097, code lost:
        if (r26.isVoice() == false) goto L761;
     */
    /* JADX WARN: Code restructure failed: missing block: B:794:0x10a1, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachAudio", org.telegram.messenger.R.string.AttachAudio);
     */
    /* JADX WARN: Code restructure failed: missing block: B:796:0x10a6, code lost:
        if (r26.isRoundVideo() == false) goto L765;
     */
    /* JADX WARN: Code restructure failed: missing block: B:798:0x10b0, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachRound", org.telegram.messenger.R.string.AttachRound);
     */
    /* JADX WARN: Code restructure failed: missing block: B:800:0x10b5, code lost:
        if (r26.isMusic() == false) goto L769;
     */
    /* JADX WARN: Code restructure failed: missing block: B:802:0x10bf, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachMusic", org.telegram.messenger.R.string.AttachMusic);
     */
    /* JADX WARN: Code restructure failed: missing block: B:803:0x10c0, code lost:
        r2 = r26.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:804:0x10c6, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L773;
     */
    /* JADX WARN: Code restructure failed: missing block: B:806:0x10d0, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachContact", org.telegram.messenger.R.string.AttachContact);
     */
    /* JADX WARN: Code restructure failed: missing block: B:808:0x10d3, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L781;
     */
    /* JADX WARN: Code restructure failed: missing block: B:810:0x10db, code lost:
        if (((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r2).poll.quiz == false) goto L779;
     */
    /* JADX WARN: Code restructure failed: missing block: B:812:0x10e5, code lost:
        return org.telegram.messenger.LocaleController.getString("QuizPoll", org.telegram.messenger.R.string.QuizPoll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:814:0x10ee, code lost:
        return org.telegram.messenger.LocaleController.getString("Poll", org.telegram.messenger.R.string.Poll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:816:0x10f1, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) == false) goto L785;
     */
    /* JADX WARN: Code restructure failed: missing block: B:818:0x10fb, code lost:
        return org.telegram.messenger.LocaleController.getString("BoostingGiveaway", org.telegram.messenger.R.string.BoostingGiveaway);
     */
    /* JADX WARN: Code restructure failed: missing block: B:820:0x10fe, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveawayResults) == false) goto L789;
     */
    /* JADX WARN: Code restructure failed: missing block: B:822:0x1108, code lost:
        return org.telegram.messenger.LocaleController.getString("BoostingGiveawayResults", org.telegram.messenger.R.string.BoostingGiveawayResults);
     */
    /* JADX WARN: Code restructure failed: missing block: B:824:0x110b, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L845;
     */
    /* JADX WARN: Code restructure failed: missing block: B:826:0x110f, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L793;
     */
    /* JADX WARN: Code restructure failed: missing block: B:829:0x1115, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L797;
     */
    /* JADX WARN: Code restructure failed: missing block: B:831:0x111f, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachLiveLocation", org.telegram.messenger.R.string.AttachLiveLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:833:0x1122, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L827;
     */
    /* JADX WARN: Code restructure failed: missing block: B:835:0x1128, code lost:
        if (r26.isSticker() != false) goto L821;
     */
    /* JADX WARN: Code restructure failed: missing block: B:837:0x112e, code lost:
        if (r26.isAnimatedSticker() == false) goto L803;
     */
    /* JADX WARN: Code restructure failed: missing block: B:840:0x1135, code lost:
        if (r26.isGif() == false) goto L813;
     */
    /* JADX WARN: Code restructure failed: missing block: B:842:0x113b, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L811;
     */
    /* JADX WARN: Code restructure failed: missing block: B:844:0x1145, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageOwner.message) != false) goto L811;
     */
    /* JADX WARN: Code restructure failed: missing block: B:846:0x115a, code lost:
        return "🎬 " + replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:848:0x1163, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachGif", org.telegram.messenger.R.string.AttachGif);
     */
    /* JADX WARN: Code restructure failed: missing block: B:850:0x1168, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L819;
     */
    /* JADX WARN: Code restructure failed: missing block: B:852:0x1172, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageOwner.message) != false) goto L819;
     */
    /* JADX WARN: Code restructure failed: missing block: B:854:0x1187, code lost:
        return "📎 " + replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:856:0x1190, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDocument", org.telegram.messenger.R.string.AttachDocument);
     */
    /* JADX WARN: Code restructure failed: missing block: B:857:0x1191, code lost:
        r0 = r26.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:858:0x1195, code lost:
        if (r0 == null) goto L825;
     */
    /* JADX WARN: Code restructure failed: missing block: B:860:0x11b3, code lost:
        return r0 + " " + org.telegram.messenger.LocaleController.getString("AttachSticker", org.telegram.messenger.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:862:0x11bc, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachSticker", org.telegram.messenger.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:864:0x11bf, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaStory) == false) goto L839;
     */
    /* JADX WARN: Code restructure failed: missing block: B:866:0x11c5, code lost:
        if (((org.telegram.tgnet.TLRPC$TL_messageMediaStory) r2).via_mention == false) goto L837;
     */
    /* JADX WARN: Code restructure failed: missing block: B:867:0x11c7, code lost:
        r0 = org.telegram.messenger.R.string.StoryNotificationMention;
        r1 = new java.lang.Object[1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:868:0x11cf, code lost:
        if (r27[0] != null) goto L836;
     */
    /* JADX WARN: Code restructure failed: missing block: B:869:0x11d1, code lost:
        r3 = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:870:0x11d4, code lost:
        r3 = r27[0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:871:0x11d6, code lost:
        r1[0] = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:872:0x11de, code lost:
        return org.telegram.messenger.LocaleController.formatString("StoryNotificationMention", r0, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:874:0x11e7, code lost:
        return org.telegram.messenger.LocaleController.getString("Story", org.telegram.messenger.R.string.Story);
     */
    /* JADX WARN: Code restructure failed: missing block: B:876:0x11ee, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageText) != false) goto L843;
     */
    /* JADX WARN: Code restructure failed: missing block: B:878:0x11f4, code lost:
        return replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:880:0x11fb, code lost:
        return org.telegram.messenger.LocaleController.getString(r1, org.telegram.messenger.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:882:0x1204, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachLocation", org.telegram.messenger.R.string.AttachLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:898:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:899:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.R.string.ChatThemeDisabled, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:900:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChangedChatThemeYou", org.telegram.messenger.R.string.ChatThemeChangedYou, r0);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getShortStringForMessage(MessageObject messageObject, String[] strArr, boolean[] zArr) {
        String str;
        String str2;
        String str3;
        char c;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        TLRPC$Peer tLRPC$Peer;
        TLRPC$Chat chat;
        String str4;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader2;
        TLRPC$Peer tLRPC$Peer2;
        boolean z;
        if (AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter) {
            return LocaleController.getString("NotificationHiddenMessage", R.string.NotificationHiddenMessage);
        }
        TLRPC$Message tLRPC$Message = messageObject.messageOwner;
        long j = tLRPC$Message.dialog_id;
        TLRPC$Peer tLRPC$Peer3 = tLRPC$Message.peer_id;
        long j2 = tLRPC$Peer3.chat_id;
        if (j2 == 0) {
            j2 = tLRPC$Peer3.channel_id;
        }
        long j3 = tLRPC$Peer3.user_id;
        if (zArr != null) {
            zArr[0] = true;
        }
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        boolean z2 = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CONTENT_PREVIEW + j, true);
        if (messageObject.isFcmMessage()) {
            if (j2 == 0 && j3 != 0) {
                if (Build.VERSION.SDK_INT > 27) {
                    z = false;
                    strArr[0] = messageObject.localName;
                } else {
                    z = false;
                }
                if (!z2 || !notificationsSettings.getBoolean("EnablePreviewAll", true)) {
                    if (zArr != null) {
                        zArr[z ? 1 : 0] = z;
                    }
                    return LocaleController.getString("Message", R.string.Message);
                }
            } else if (j2 != 0) {
                if (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup()) {
                    strArr[0] = messageObject.localUserName;
                } else if (Build.VERSION.SDK_INT > 27) {
                    strArr[0] = messageObject.localName;
                }
                if (z2) {
                    boolean z3 = !messageObject.localChannel ? true : true;
                    if (messageObject.localChannel) {
                    }
                }
                if (zArr != null) {
                    zArr[0] = false;
                }
                return (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup()) ? LocaleController.formatString("NotificationMessageGroupNoText", R.string.NotificationMessageGroupNoText, messageObject.localUserName, messageObject.localName) : LocaleController.formatString("ChannelMessageNoText", R.string.ChannelMessageNoText, messageObject.localName);
            }
            return replaceSpoilers(messageObject);
        }
        long clientUserId = getUserConfig().getClientUserId();
        if (j3 == 0) {
            j3 = messageObject.getFromChatId();
            if (j3 == 0) {
                j3 = -j2;
            }
        } else if (j3 == clientUserId) {
            j3 = messageObject.getFromChatId();
        }
        if (j == 0) {
            if (j2 != 0) {
                j = -j2;
            } else if (j3 != 0) {
                j = j3;
            }
        }
        if (UserObject.isReplyUser(j) && (tLRPC$MessageFwdHeader2 = messageObject.messageOwner.fwd_from) != null && (tLRPC$Peer2 = tLRPC$MessageFwdHeader2.from_id) != null) {
            j3 = MessageObject.getPeerId(tLRPC$Peer2);
        }
        if (j3 > 0) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(j3));
            if (user != null) {
                str4 = UserObject.getUserName(user);
                if (j2 != 0) {
                    strArr[0] = str4;
                    str = "Message";
                    str2 = "EnablePreviewChannel";
                } else {
                    str = "Message";
                    str2 = "EnablePreviewChannel";
                    if (Build.VERSION.SDK_INT > 27) {
                        strArr[0] = str4;
                    } else {
                        strArr[0] = null;
                    }
                }
            } else {
                str = "Message";
                str2 = "EnablePreviewChannel";
                str4 = null;
            }
            str3 = str4;
        } else {
            str = "Message";
            str2 = "EnablePreviewChannel";
            TLRPC$Chat chat2 = getMessagesController().getChat(Long.valueOf(-j3));
            if (chat2 != null) {
                str3 = chat2.title;
                strArr[0] = str3;
            } else {
                str3 = null;
            }
        }
        if (str3 != null && j3 > 0 && UserObject.isReplyUser(j) && (tLRPC$MessageFwdHeader = messageObject.messageOwner.fwd_from) != null && (tLRPC$Peer = tLRPC$MessageFwdHeader.saved_from_peer) != null) {
            long peerId = MessageObject.getPeerId(tLRPC$Peer);
            if (DialogObject.isChatDialog(peerId) && (chat = getMessagesController().getChat(Long.valueOf(-peerId))) != null) {
                str3 = str3 + " @ " + chat.title;
                if (strArr[0] != null) {
                    strArr[0] = str3;
                }
            }
        }
        if (str3 == null) {
            return null;
        }
        if (j2 != 0) {
            tLRPC$Chat = getMessagesController().getChat(Long.valueOf(j2));
            if (tLRPC$Chat == null) {
                return null;
            }
            if (!ChatObject.isChannel(tLRPC$Chat) || tLRPC$Chat.megagroup || Build.VERSION.SDK_INT > 27) {
                c = 0;
            } else {
                c = 0;
                strArr[0] = null;
            }
        } else {
            c = 0;
            tLRPC$Chat = null;
        }
        if (DialogObject.isEncryptedDialog(j)) {
            strArr[c] = null;
            return LocaleController.getString("NotificationHiddenMessage", R.string.NotificationHiddenMessage);
        }
        boolean z4 = ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup;
        if (z2) {
            boolean z5 = (j2 != 0 || j3 == 0) ? true : true;
            if (j2 != 0) {
                if (!z4) {
                }
                if (z4) {
                }
            }
        }
        String str5 = str;
        if (zArr != null) {
            zArr[0] = false;
        }
        return LocaleController.getString(str5, R.string.Message);
    }

    private String replaceSpoilers(MessageObject messageObject) {
        TLRPC$Message tLRPC$Message;
        String str;
        if (messageObject == null || (tLRPC$Message = messageObject.messageOwner) == null || (str = tLRPC$Message.message) == null || tLRPC$Message.entities == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < messageObject.messageOwner.entities.size(); i++) {
            if (messageObject.messageOwner.entities.get(i) instanceof TLRPC$TL_messageEntitySpoiler) {
                TLRPC$TL_messageEntitySpoiler tLRPC$TL_messageEntitySpoiler = (TLRPC$TL_messageEntitySpoiler) messageObject.messageOwner.entities.get(i);
                for (int i2 = 0; i2 < tLRPC$TL_messageEntitySpoiler.length; i2++) {
                    char[] cArr = this.spoilerChars;
                    sb.setCharAt(tLRPC$TL_messageEntitySpoiler.offset + i2, cArr[i2 % cArr.length]);
                }
            }
        }
        return sb.toString();
    }

    /* JADX WARN: Code restructure failed: missing block: B:276:0x0662, code lost:
        if (r12.getBoolean("EnablePreviewGroup", true) == false) goto L836;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0070, code lost:
        if (r12.getBoolean("EnablePreviewAll", true) == false) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x066e, code lost:
        if (r12.getBoolean("EnablePreviewChannel", r14) != false) goto L258;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x0670, code lost:
        r6 = r30.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x0674, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageService) == false) goto L603;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x0676, code lost:
        r8 = r6.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x067a, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser) == false) goto L301;
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x067c, code lost:
        r3 = r8.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x0682, code lost:
        if (r3 != 0) goto L267;
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x068b, code lost:
        if (r8.users.size() != 1) goto L267;
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x068d, code lost:
        r3 = r30.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x06a2, code lost:
        if (r3 == 0) goto L287;
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x06ac, code lost:
        if (r30.messageOwner.peer_id.channel_id == 0) goto L274;
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x06b0, code lost:
        if (r5.megagroup != false) goto L274;
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x06b2, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.R.string.ChannelAddedByNotification, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x06c9, code lost:
        if (r3 != r17) goto L277;
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x06cb, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.R.string.NotificationInvitedToGroup, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x06e0, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x06ec, code lost:
        if (r0 != null) goto L280;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x06ee, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x06f4, code lost:
        if (r9 != r0.id) goto L286;
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x06f8, code lost:
        if (r5.megagroup == false) goto L285;
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x06fa, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.R.string.NotificationGroupAddSelfMega, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x070f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.R.string.NotificationGroupAddSelf, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x0724, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r2, r5.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x0740, code lost:
        r1 = new java.lang.StringBuilder();
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x0750, code lost:
        if (r3 >= r30.messageOwner.action.users.size()) goto L299;
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x0752, code lost:
        r4 = getMessagesController().getUser(r30.messageOwner.action.users.get(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x0766, code lost:
        if (r4 == null) goto L298;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x0768, code lost:
        r4 = org.telegram.messenger.UserObject.getUserName(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x0770, code lost:
        if (r1.length() == 0) goto L295;
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x0772, code lost:
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x0777, code lost:
        r1.append(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x077a, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x077d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r2, r5.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x079c, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCall) == false) goto L304;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x079e, code lost:
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.R.string.NotificationGroupCreatedCall, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x07b4, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled) == false) goto L307;
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x07b6, code lost:
        r14 = r30.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x07c0, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall) == false) goto L337;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x07c2, code lost:
        r3 = r8.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x07c8, code lost:
        if (r3 != 0) goto L314;
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x07d1, code lost:
        if (r8.users.size() != 1) goto L314;
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x07d3, code lost:
        r3 = r30.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x07e8, code lost:
        if (r3 == 0) goto L323;
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x07ec, code lost:
        if (r3 != r17) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x07ee, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.R.string.NotificationGroupInvitedYouToCall, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x0803, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x080f, code lost:
        if (r0 != null) goto L322;
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x0811, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x0813, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r2, r5.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x082f, code lost:
        r1 = new java.lang.StringBuilder();
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x083f, code lost:
        if (r3 >= r30.messageOwner.action.users.size()) goto L335;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x0841, code lost:
        r4 = getMessagesController().getUser(r30.messageOwner.action.users.get(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x0855, code lost:
        if (r4 == null) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x0857, code lost:
        r4 = org.telegram.messenger.UserObject.getUserName(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x085f, code lost:
        if (r1.length() == 0) goto L331;
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x0861, code lost:
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x0866, code lost:
        r1.append(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x0869, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x086c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r2, r5.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x088a, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGiftCode) == false) goto L348;
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x088c, code lost:
        r8 = (org.telegram.tgnet.TLRPC$TL_messageActionGiftCode) r8;
        r0 = org.telegram.messenger.MessagesController.getInstance(r29.currentAccount).getChat(java.lang.Long.valueOf(-org.telegram.messenger.DialogObject.getPeerDialogId(r8.boost_peer)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:360:0x08a5, code lost:
        if (r0 != null) goto L347;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x08a7, code lost:
        r14 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:362:0x08a9, code lost:
        r14 = r0.title;
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x08ab, code lost:
        if (r14 != null) goto L346;
     */
    /* JADX WARN: Code restructure failed: missing block: B:364:0x08ad, code lost:
        r0 = org.telegram.messenger.LocaleController.getString("BoostingReceivedGiftNoName", org.telegram.messenger.R.string.BoostingReceivedGiftNoName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x08b6, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGiftCode", org.telegram.messenger.R.string.NotificationMessageGiftCode, r14, org.telegram.messenger.LocaleController.formatPluralString("Months", r8.months, new java.lang.Object[0]));
     */
    /* JADX WARN: Code restructure failed: missing block: B:368:0x08d9, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink) == false) goto L352;
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x08f3, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle) == false) goto L356;
     */
    /* JADX WARN: Code restructure failed: missing block: B:374:0x090a, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto) != false) goto L587;
     */
    /* JADX WARN: Code restructure failed: missing block: B:376:0x090e, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto) == false) goto L360;
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x0914, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser) == false) goto L375;
     */
    /* JADX WARN: Code restructure failed: missing block: B:380:0x0916, code lost:
        r3 = r8.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x091a, code lost:
        if (r3 != r17) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:384:0x0936, code lost:
        if (r3 != r9) goto L370;
     */
    /* JADX WARN: Code restructure failed: missing block: B:386:0x094a, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r30.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:387:0x095c, code lost:
        if (r0 != null) goto L373;
     */
    /* JADX WARN: Code restructure failed: missing block: B:388:0x095e, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x097c, code lost:
        r9 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x097f, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatCreate) == false) goto L379;
     */
    /* JADX WARN: Code restructure failed: missing block: B:395:0x098b, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate) == false) goto L383;
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:0x0997, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo) == false) goto L387;
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x09af, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) == false) goto L391;
     */
    /* JADX WARN: Code restructure failed: missing block: B:404:0x09c3, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken) == false) goto L395;
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x09cf, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPinMessage) == false) goto L567;
     */
    /* JADX WARN: Code restructure failed: missing block: B:409:0x09d7, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r5) == false) goto L484;
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x09db, code lost:
        if (r5.megagroup == false) goto L401;
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x09df, code lost:
        r2 = r30.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:414:0x09e1, code lost:
        if (r2 != null) goto L405;
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x09fb, code lost:
        if (r2.isMusic() == false) goto L408;
     */
    /* JADX WARN: Code restructure failed: missing block: B:418:0x09fd, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.R.string.NotificationActionPinnedMusicChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0097, code lost:
        if (r12.getBoolean("EnablePreviewGroup", true) != false) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:420:0x0a13, code lost:
        if (r2.isVideo() == false) goto L416;
     */
    /* JADX WARN: Code restructure failed: missing block: B:422:0x0a19, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L415;
     */
    /* JADX WARN: Code restructure failed: missing block: B:424:0x0a23, code lost:
        if (android.text.TextUtils.isEmpty(r2.messageOwner.message) != false) goto L415;
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x0a25, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "📹 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:426:0x0a4b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.R.string.NotificationActionPinnedVideoChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:428:0x0a61, code lost:
        if (r2.isGif() == false) goto L424;
     */
    /* JADX WARN: Code restructure failed: missing block: B:430:0x0a67, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L423;
     */
    /* JADX WARN: Code restructure failed: missing block: B:432:0x0a71, code lost:
        if (android.text.TextUtils.isEmpty(r2.messageOwner.message) != false) goto L423;
     */
    /* JADX WARN: Code restructure failed: missing block: B:433:0x0a73, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "🎬 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:434:0x0a99, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.R.string.NotificationActionPinnedGifChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:436:0x0ab1, code lost:
        if (r2.isVoice() == false) goto L427;
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x0ab3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.R.string.NotificationActionPinnedVoiceChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x0ac7, code lost:
        if (r2.isRoundVideo() == false) goto L430;
     */
    /* JADX WARN: Code restructure failed: missing block: B:440:0x0ac9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.R.string.NotificationActionPinnedRoundChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:442:0x0add, code lost:
        if (r2.isSticker() != false) goto L480;
     */
    /* JADX WARN: Code restructure failed: missing block: B:444:0x0ae3, code lost:
        if (r2.isAnimatedSticker() == false) goto L434;
     */
    /* JADX WARN: Code restructure failed: missing block: B:446:0x0ae7, code lost:
        r1 = r2.messageOwner;
        r3 = r1.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:447:0x0aed, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L442;
     */
    /* JADX WARN: Code restructure failed: missing block: B:449:0x0af3, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L441;
     */
    /* JADX WARN: Code restructure failed: missing block: B:451:0x0afb, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L441;
     */
    /* JADX WARN: Code restructure failed: missing block: B:452:0x0afd, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "📎 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:453:0x0b25, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.R.string.NotificationActionPinnedFileChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:455:0x0b39, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L479;
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:0x0b3d, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L446;
     */
    /* JADX WARN: Code restructure failed: missing block: B:460:0x0b43, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L449;
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x0b45, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0b59, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L452;
     */
    /* JADX WARN: Code restructure failed: missing block: B:464:0x0b5b, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r30.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.R.string.NotificationActionPinnedContactChannel2, r5.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:466:0x0b80, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L458;
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x0b82, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r3).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:468:0x0b88, code lost:
        if (r0.quiz == false) goto L457;
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x0b8a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.R.string.NotificationActionPinnedQuizChannel2, r5.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:470:0x0ba3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.R.string.NotificationActionPinnedPollChannel2, r5.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:472:0x0bbe, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L466;
     */
    /* JADX WARN: Code restructure failed: missing block: B:474:0x0bc4, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L465;
     */
    /* JADX WARN: Code restructure failed: missing block: B:476:0x0bcc, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L465;
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x0bce, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "🖼 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:478:0x0bf4, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.R.string.NotificationActionPinnedPhotoChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00a5, code lost:
        if (r12.getBoolean("EnablePreviewChannel", r3) == false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:480:0x0c0a, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L469;
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x0c0c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.R.string.NotificationActionPinnedGameChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:482:0x0c1c, code lost:
        r0 = r2.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0c1e, code lost:
        if (r0 == null) goto L478;
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0c24, code lost:
        if (r0.length() <= 0) goto L478;
     */
    /* JADX WARN: Code restructure failed: missing block: B:486:0x0c26, code lost:
        r0 = r2.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:487:0x0c2c, code lost:
        if (r0.length() <= 20) goto L477;
     */
    /* JADX WARN: Code restructure failed: missing block: B:488:0x0c2e, code lost:
        r1 = new java.lang.StringBuilder();
        r8 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x0c45, code lost:
        r8 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:490:0x0c46, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextChannel;
        r2 = new java.lang.Object[2];
        r2[r8] = r5.title;
        r2[1] = r0;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x0c58, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x0c6a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x0c7c, code lost:
        r0 = r2.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:494:0x0c81, code lost:
        if (r0 == null) goto L483;
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:0x0c83, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiChannel, r5.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:496:0x0c97, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x0ca8, code lost:
        r6 = r30.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:498:0x0cad, code lost:
        if (r6 != null) goto L488;
     */
    /* JADX WARN: Code restructure failed: missing block: B:501:0x0cc9, code lost:
        if (r6.isMusic() == false) goto L491;
     */
    /* JADX WARN: Code restructure failed: missing block: B:502:0x0ccb, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.R.string.NotificationActionPinnedMusic, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0ce3, code lost:
        if (r6.isVideo() == false) goto L499;
     */
    /* JADX WARN: Code restructure failed: missing block: B:506:0x0ce9, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L498;
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x0cf3, code lost:
        if (android.text.TextUtils.isEmpty(r6.messageOwner.message) != false) goto L498;
     */
    /* JADX WARN: Code restructure failed: missing block: B:509:0x0cf5, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "📹 " + r6.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x0d1e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.R.string.NotificationActionPinnedVideo, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:512:0x0d37, code lost:
        if (r6.isGif() == false) goto L507;
     */
    /* JADX WARN: Code restructure failed: missing block: B:514:0x0d3d, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L506;
     */
    /* JADX WARN: Code restructure failed: missing block: B:516:0x0d47, code lost:
        if (android.text.TextUtils.isEmpty(r6.messageOwner.message) != false) goto L506;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0d49, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "🎬 " + r6.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x0d72, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.R.string.NotificationActionPinnedGif, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x0d8e, code lost:
        if (r6.isVoice() == false) goto L510;
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x0d90, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.R.string.NotificationActionPinnedVoice, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x0da6, code lost:
        if (r6.isRoundVideo() == false) goto L513;
     */
    /* JADX WARN: Code restructure failed: missing block: B:524:0x0da8, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.R.string.NotificationActionPinnedRound, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:526:0x0dbe, code lost:
        if (r6.isSticker() != false) goto L563;
     */
    /* JADX WARN: Code restructure failed: missing block: B:528:0x0dc4, code lost:
        if (r6.isAnimatedSticker() == false) goto L517;
     */
    /* JADX WARN: Code restructure failed: missing block: B:530:0x0dc8, code lost:
        r1 = r6.messageOwner;
        r8 = r1.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x0dce, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L525;
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0dd4, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L524;
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0ddc, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L524;
     */
    /* JADX WARN: Code restructure failed: missing block: B:536:0x0dde, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "📎 " + r6.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0e07, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.R.string.NotificationActionPinnedFile, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0e1e, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L562;
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x0e22, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L529;
     */
    /* JADX WARN: Code restructure failed: missing block: B:544:0x0e28, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L532;
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x0e2a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.R.string.NotificationActionPinnedGeoLive, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0e41, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L535;
     */
    /* JADX WARN: Code restructure failed: missing block: B:548:0x0e43, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r30.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.R.string.NotificationActionPinnedContact2, r2, r5.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:550:0x0e6b, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L541;
     */
    /* JADX WARN: Code restructure failed: missing block: B:551:0x0e6d, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r8).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:552:0x0e73, code lost:
        if (r0.quiz == false) goto L540;
     */
    /* JADX WARN: Code restructure failed: missing block: B:553:0x0e75, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.R.string.NotificationActionPinnedQuiz2, r2, r5.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:554:0x0e91, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.R.string.NotificationActionPinnedPoll2, r2, r5.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:556:0x0eaf, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L549;
     */
    /* JADX WARN: Code restructure failed: missing block: B:558:0x0eb5, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L548;
     */
    /* JADX WARN: Code restructure failed: missing block: B:560:0x0ebd, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L548;
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0ebf, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "🖼 " + r6.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:562:0x0ee8, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.R.string.NotificationActionPinnedPhoto, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:564:0x0f02, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L552;
     */
    /* JADX WARN: Code restructure failed: missing block: B:565:0x0f04, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.R.string.NotificationActionPinnedGame, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:566:0x0f16, code lost:
        r0 = r6.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:567:0x0f18, code lost:
        if (r0 == null) goto L561;
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x0f1e, code lost:
        if (r0.length() <= 0) goto L561;
     */
    /* JADX WARN: Code restructure failed: missing block: B:570:0x0f20, code lost:
        r0 = r6.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:571:0x0f26, code lost:
        if (r0.length() <= 20) goto L560;
     */
    /* JADX WARN: Code restructure failed: missing block: B:572:0x0f28, code lost:
        r1 = new java.lang.StringBuilder();
        r3 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:573:0x0f3f, code lost:
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:574:0x0f40, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedText;
        r4 = new java.lang.Object[3];
        r4[r3] = r2;
        r4[1] = r0;
        r4[2] = r5.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", r1, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:575:0x0f55, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:576:0x0f6a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.R.string.NotificationActionPinnedGeo, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:577:0x0f7f, code lost:
        r0 = r6.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:578:0x0f85, code lost:
        if (r0 == null) goto L566;
     */
    /* JADX WARN: Code restructure failed: missing block: B:579:0x0f87, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmoji, r2, r5.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:580:0x0f9d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.R.string.NotificationActionPinnedSticker, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:582:0x0fb2, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGameScore) == false) goto L571;
     */
    /* JADX WARN: Code restructure failed: missing block: B:585:0x0fbe, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) == false) goto L583;
     */
    /* JADX WARN: Code restructure failed: missing block: B:586:0x0fc0, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) r8).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:587:0x0fc8, code lost:
        if (android.text.TextUtils.isEmpty(r0) == false) goto L579;
     */
    /* JADX WARN: Code restructure failed: missing block: B:589:0x0fcc, code lost:
        if (r3 != r17) goto L578;
     */
    /* JADX WARN: Code restructure failed: missing block: B:590:0x0fce, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:591:0x0fdb, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.R.string.ChatThemeDisabled, r2, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:593:0x0ff2, code lost:
        if (r3 != r17) goto L582;
     */
    /* JADX WARN: Code restructure failed: missing block: B:594:0x0ff4, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeYou", org.telegram.messenger.R.string.ChatThemeChangedYou, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:595:0x1002, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.R.string.ChatThemeChangedTo, r2, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:597:0x1015, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest) == false) goto L139;
     */
    /* JADX WARN: Code restructure failed: missing block: B:600:0x1027, code lost:
        if (r6.peer_id.channel_id == 0) goto L597;
     */
    /* JADX WARN: Code restructure failed: missing block: B:602:0x102b, code lost:
        if (r5.megagroup != false) goto L597;
     */
    /* JADX WARN: Code restructure failed: missing block: B:604:0x1031, code lost:
        if (r30.isVideoAvatar() == false) goto L595;
     */
    /* JADX WARN: Code restructure failed: missing block: B:608:0x105c, code lost:
        if (r30.isVideoAvatar() == false) goto L601;
     */
    /* JADX WARN: Code restructure failed: missing block: B:612:0x108e, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r5) == false) goto L718;
     */
    /* JADX WARN: Code restructure failed: missing block: B:614:0x1092, code lost:
        if (r5.megagroup != false) goto L718;
     */
    /* JADX WARN: Code restructure failed: missing block: B:616:0x1098, code lost:
        if (r30.isMediaEmpty() == false) goto L616;
     */
    /* JADX WARN: Code restructure failed: missing block: B:617:0x109a, code lost:
        if (r31 != false) goto L614;
     */
    /* JADX WARN: Code restructure failed: missing block: B:619:0x10a4, code lost:
        if (android.text.TextUtils.isEmpty(r30.messageOwner.message) != false) goto L614;
     */
    /* JADX WARN: Code restructure failed: missing block: B:620:0x10a6, code lost:
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r2, r30.messageOwner.message);
        r32[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:623:0x10d3, code lost:
        if (r30.type != 29) goto L622;
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x10db, code lost:
        if ((org.telegram.messenger.MessageObject.getMedia(r30) instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) == false) goto L622;
     */
    /* JADX WARN: Code restructure failed: missing block: B:627:0x10f6, code lost:
        r6 = r30.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:628:0x10fc, code lost:
        if ((r6.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L633;
     */
    /* JADX WARN: Code restructure failed: missing block: B:629:0x10fe, code lost:
        if (r31 != false) goto L631;
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:0x1104, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L631;
     */
    /* JADX WARN: Code restructure failed: missing block: B:633:0x110c, code lost:
        if (android.text.TextUtils.isEmpty(r6.message) != false) goto L631;
     */
    /* JADX WARN: Code restructure failed: missing block: B:634:0x110e, code lost:
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r2, "🖼 " + r30.messageOwner.message);
        r32[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:637:0x1148, code lost:
        if (r30.isVideo() == false) goto L644;
     */
    /* JADX WARN: Code restructure failed: missing block: B:638:0x114a, code lost:
        if (r31 != false) goto L642;
     */
    /* JADX WARN: Code restructure failed: missing block: B:640:0x1150, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L642;
     */
    /* JADX WARN: Code restructure failed: missing block: B:642:0x115a, code lost:
        if (android.text.TextUtils.isEmpty(r30.messageOwner.message) != false) goto L642;
     */
    /* JADX WARN: Code restructure failed: missing block: B:643:0x115c, code lost:
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r2, "📹 " + r30.messageOwner.message);
        r32[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:646:0x1198, code lost:
        if (r30.isVoice() == false) goto L648;
     */
    /* JADX WARN: Code restructure failed: missing block: B:649:0x11ac, code lost:
        if (r30.isRoundVideo() == false) goto L652;
     */
    /* JADX WARN: Code restructure failed: missing block: B:652:0x11c0, code lost:
        if (r30.isMusic() == false) goto L656;
     */
    /* JADX WARN: Code restructure failed: missing block: B:654:0x11d0, code lost:
        r7 = r30.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:655:0x11d6, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L660;
     */
    /* JADX WARN: Code restructure failed: missing block: B:656:0x11d8, code lost:
        r7 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:658:0x11f6, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L666;
     */
    /* JADX WARN: Code restructure failed: missing block: B:659:0x11f8, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r7).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:660:0x11fe, code lost:
        if (r0.quiz == false) goto L665;
     */
    /* JADX WARN: Code restructure failed: missing block: B:661:0x1200, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageQuiz2", org.telegram.messenger.R.string.ChannelMessageQuiz2, r2, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:662:0x1217, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessagePoll2", org.telegram.messenger.R.string.ChannelMessagePoll2, r2, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:664:0x1230, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) == false) goto L670;
     */
    /* JADX WARN: Code restructure failed: missing block: B:665:0x1232, code lost:
        r7 = (org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:667:0x1258, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L716;
     */
    /* JADX WARN: Code restructure failed: missing block: B:669:0x125c, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L674;
     */
    /* JADX WARN: Code restructure failed: missing block: B:672:0x1262, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L678;
     */
    /* JADX WARN: Code restructure failed: missing block: B:675:0x1276, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L708;
     */
    /* JADX WARN: Code restructure failed: missing block: B:677:0x127c, code lost:
        if (r30.isSticker() != false) goto L704;
     */
    /* JADX WARN: Code restructure failed: missing block: B:679:0x1282, code lost:
        if (r30.isAnimatedSticker() == false) goto L684;
     */
    /* JADX WARN: Code restructure failed: missing block: B:682:0x128a, code lost:
        if (r30.isGif() == false) goto L695;
     */
    /* JADX WARN: Code restructure failed: missing block: B:683:0x128c, code lost:
        if (r31 != false) goto L693;
     */
    /* JADX WARN: Code restructure failed: missing block: B:685:0x1292, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L693;
     */
    /* JADX WARN: Code restructure failed: missing block: B:687:0x129c, code lost:
        if (android.text.TextUtils.isEmpty(r30.messageOwner.message) != false) goto L693;
     */
    /* JADX WARN: Code restructure failed: missing block: B:688:0x129e, code lost:
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r2, "🎬 " + r30.messageOwner.message);
        r32[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:690:0x12d4, code lost:
        if (r31 != false) goto L702;
     */
    /* JADX WARN: Code restructure failed: missing block: B:692:0x12da, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L702;
     */
    /* JADX WARN: Code restructure failed: missing block: B:694:0x12e4, code lost:
        if (android.text.TextUtils.isEmpty(r30.messageOwner.message) != false) goto L702;
     */
    /* JADX WARN: Code restructure failed: missing block: B:695:0x12e6, code lost:
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r2, "📎 " + r30.messageOwner.message);
        r32[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:697:0x131c, code lost:
        r0 = r30.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:698:0x1322, code lost:
        if (r0 == null) goto L707;
     */
    /* JADX WARN: Code restructure failed: missing block: B:699:0x1324, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageStickerEmoji", org.telegram.messenger.R.string.ChannelMessageStickerEmoji, r2, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:700:0x1335, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageSticker", org.telegram.messenger.R.string.ChannelMessageSticker, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:702:0x1344, code lost:
        if (r31 != false) goto L714;
     */
    /* JADX WARN: Code restructure failed: missing block: B:704:0x134c, code lost:
        if (android.text.TextUtils.isEmpty(r30.messageText) != false) goto L714;
     */
    /* JADX WARN: Code restructure failed: missing block: B:705:0x134e, code lost:
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r2, r30.messageText);
        r32[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:709:0x1385, code lost:
        if (r30.isMediaEmpty() == false) goto L727;
     */
    /* JADX WARN: Code restructure failed: missing block: B:710:0x1387, code lost:
        if (r31 != false) goto L725;
     */
    /* JADX WARN: Code restructure failed: missing block: B:712:0x1391, code lost:
        if (android.text.TextUtils.isEmpty(r30.messageOwner.message) != false) goto L725;
     */
    /* JADX WARN: Code restructure failed: missing block: B:716:0x13c8, code lost:
        if (r30.type != 29) goto L733;
     */
    /* JADX WARN: Code restructure failed: missing block: B:718:0x13d0, code lost:
        if ((org.telegram.messenger.MessageObject.getMedia(r30) instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) == false) goto L733;
     */
    /* JADX WARN: Code restructure failed: missing block: B:720:0x13ee, code lost:
        r4 = r30.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:721:0x13f4, code lost:
        if ((r4.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L744;
     */
    /* JADX WARN: Code restructure failed: missing block: B:722:0x13f6, code lost:
        if (r31 != false) goto L742;
     */
    /* JADX WARN: Code restructure failed: missing block: B:724:0x13fc, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L742;
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x1404, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L742;
     */
    /* JADX WARN: Code restructure failed: missing block: B:730:0x1448, code lost:
        if (r30.isVideo() == false) goto L755;
     */
    /* JADX WARN: Code restructure failed: missing block: B:731:0x144a, code lost:
        if (r31 != false) goto L753;
     */
    /* JADX WARN: Code restructure failed: missing block: B:733:0x1450, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L753;
     */
    /* JADX WARN: Code restructure failed: missing block: B:735:0x145a, code lost:
        if (android.text.TextUtils.isEmpty(r30.messageOwner.message) != false) goto L753;
     */
    /* JADX WARN: Code restructure failed: missing block: B:739:0x14a1, code lost:
        if (r30.isVoice() == false) goto L759;
     */
    /* JADX WARN: Code restructure failed: missing block: B:742:0x14b9, code lost:
        if (r30.isRoundVideo() == false) goto L763;
     */
    /* JADX WARN: Code restructure failed: missing block: B:745:0x14d1, code lost:
        if (r30.isMusic() == false) goto L767;
     */
    /* JADX WARN: Code restructure failed: missing block: B:747:0x14e5, code lost:
        r4 = r30.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:748:0x14eb, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L771;
     */
    /* JADX WARN: Code restructure failed: missing block: B:749:0x14ed, code lost:
        r4 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:751:0x1511, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L777;
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x1513, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r4).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:753:0x1519, code lost:
        if (r0.quiz == false) goto L776;
     */
    /* JADX WARN: Code restructure failed: missing block: B:754:0x151b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupQuiz2", org.telegram.messenger.R.string.NotificationMessageGroupQuiz2, r2, r5.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:755:0x1537, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPoll2", org.telegram.messenger.R.string.NotificationMessageGroupPoll2, r2, r5.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:757:0x1555, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L781;
     */
    /* JADX WARN: Code restructure failed: missing block: B:760:0x1575, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) == false) goto L785;
     */
    /* JADX WARN: Code restructure failed: missing block: B:761:0x1577, code lost:
        r4 = (org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:763:0x159d, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveawayResults) == false) goto L789;
     */
    /* JADX WARN: Code restructure failed: missing block: B:766:0x15ae, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L834;
     */
    /* JADX WARN: Code restructure failed: missing block: B:768:0x15b2, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L793;
     */
    /* JADX WARN: Code restructure failed: missing block: B:771:0x15b8, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L797;
     */
    /* JADX WARN: Code restructure failed: missing block: B:774:0x15d1, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L827;
     */
    /* JADX WARN: Code restructure failed: missing block: B:776:0x15d7, code lost:
        if (r30.isSticker() != false) goto L823;
     */
    /* JADX WARN: Code restructure failed: missing block: B:778:0x15dd, code lost:
        if (r30.isAnimatedSticker() == false) goto L803;
     */
    /* JADX WARN: Code restructure failed: missing block: B:781:0x15e5, code lost:
        if (r30.isGif() == false) goto L814;
     */
    /* JADX WARN: Code restructure failed: missing block: B:782:0x15e7, code lost:
        if (r31 != false) goto L812;
     */
    /* JADX WARN: Code restructure failed: missing block: B:784:0x15ed, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L812;
     */
    /* JADX WARN: Code restructure failed: missing block: B:786:0x15f7, code lost:
        if (android.text.TextUtils.isEmpty(r30.messageOwner.message) != false) goto L812;
     */
    /* JADX WARN: Code restructure failed: missing block: B:789:0x1637, code lost:
        if (r31 != false) goto L821;
     */
    /* JADX WARN: Code restructure failed: missing block: B:791:0x163d, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L821;
     */
    /* JADX WARN: Code restructure failed: missing block: B:793:0x1647, code lost:
        if (android.text.TextUtils.isEmpty(r30.messageOwner.message) != false) goto L821;
     */
    /* JADX WARN: Code restructure failed: missing block: B:796:0x1687, code lost:
        r0 = r30.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:797:0x168d, code lost:
        if (r0 == null) goto L826;
     */
    /* JADX WARN: Code restructure failed: missing block: B:798:0x168f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupStickerEmoji", org.telegram.messenger.R.string.NotificationMessageGroupStickerEmoji, r2, r5.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:799:0x16a5, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupSticker", org.telegram.messenger.R.string.NotificationMessageGroupSticker, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:800:0x16b8, code lost:
        if (r31 != false) goto L832;
     */
    /* JADX WARN: Code restructure failed: missing block: B:802:0x16c0, code lost:
        if (android.text.TextUtils.isEmpty(r30.messageText) != false) goto L832;
     */
    /* JADX WARN: Code restructure failed: missing block: B:834:?, code lost:
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:835:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.R.string.NotificationInvitedToGroupByLink, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:836:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.R.string.NotificationEditedGroupName, r2, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:837:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.R.string.NotificationGroupKickYou, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:838:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.R.string.NotificationGroupLeftMember, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:839:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.R.string.NotificationGroupKickMember, r2, r5.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:840:?, code lost:
        return r30.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:841:?, code lost:
        return r30.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:842:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:843:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:844:?, code lost:
        return r30.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:845:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:846:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:847:?, code lost:
        return r30.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:848:?, code lost:
        return r30.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:849:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.R.string.ChannelVideoEditNotification, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:850:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.R.string.ChannelPhotoEditNotification, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:851:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.R.string.NotificationEditedGroupVideo, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:852:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.R.string.NotificationEditedGroupPhoto, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:853:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:854:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.R.string.ChannelMessageNoText, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:855:?, code lost:
        return org.telegram.messenger.LocaleController.formatPluralString("NotificationChannelMessagePaidMedia", (int) ((org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) org.telegram.messenger.MessageObject.getMedia(r30)).stars_amount, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:856:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:857:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessagePhoto", org.telegram.messenger.R.string.ChannelMessagePhoto, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:858:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:859:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageVideo", org.telegram.messenger.R.string.ChannelMessageVideo, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:860:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageAudio", org.telegram.messenger.R.string.ChannelMessageAudio, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:861:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageRound", org.telegram.messenger.R.string.ChannelMessageRound, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:862:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageMusic", org.telegram.messenger.R.string.ChannelMessageMusic, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:863:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageContact2", org.telegram.messenger.R.string.ChannelMessageContact2, r2, org.telegram.messenger.ContactsController.formatName(r7.first_name, r7.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:864:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageChannelGiveaway", org.telegram.messenger.R.string.NotificationMessageChannelGiveaway, r5.title, java.lang.Integer.valueOf(r7.quantity), java.lang.Integer.valueOf(r7.months));
     */
    /* JADX WARN: Code restructure failed: missing block: B:865:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageLiveLocation", org.telegram.messenger.R.string.ChannelMessageLiveLocation, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:866:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:867:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageGIF", org.telegram.messenger.R.string.ChannelMessageGIF, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:868:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:869:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageDocument", org.telegram.messenger.R.string.ChannelMessageDocument, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:870:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:871:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.R.string.ChannelMessageNoText, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:872:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageMap", org.telegram.messenger.R.string.ChannelMessageMap, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:873:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r2, r5.title, r30.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:874:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupNoText", org.telegram.messenger.R.string.NotificationMessageGroupNoText, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:875:?, code lost:
        return org.telegram.messenger.LocaleController.formatPluralString("NotificationChatMessagePaidMedia", (int) ((org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) org.telegram.messenger.MessageObject.getMedia(r30)).stars_amount, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:876:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r2, r5.title, "🖼 " + r30.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:877:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPhoto", org.telegram.messenger.R.string.NotificationMessageGroupPhoto, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:878:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r2, r5.title, "📹 " + r30.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:879:?, code lost:
        return org.telegram.messenger.LocaleController.formatString(" ", org.telegram.messenger.R.string.NotificationMessageGroupVideo, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:880:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupAudio", org.telegram.messenger.R.string.NotificationMessageGroupAudio, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:881:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupRound", org.telegram.messenger.R.string.NotificationMessageGroupRound, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:882:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMusic", org.telegram.messenger.R.string.NotificationMessageGroupMusic, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:883:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupContact2", org.telegram.messenger.R.string.NotificationMessageGroupContact2, r2, r5.title, org.telegram.messenger.ContactsController.formatName(r4.first_name, r4.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:884:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGame", org.telegram.messenger.R.string.NotificationMessageGroupGame, r2, r5.title, r4.game.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:885:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageChannelGiveaway", org.telegram.messenger.R.string.NotificationMessageChannelGiveaway, r5.title, java.lang.Integer.valueOf(r4.quantity), java.lang.Integer.valueOf(r4.months));
     */
    /* JADX WARN: Code restructure failed: missing block: B:886:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("BoostingGiveawayResults", org.telegram.messenger.R.string.BoostingGiveawayResults, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:887:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupLiveLocation", org.telegram.messenger.R.string.NotificationMessageGroupLiveLocation, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:888:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r2, r5.title, "🎬 " + r30.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:889:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGif", org.telegram.messenger.R.string.NotificationMessageGroupGif, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:890:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r2, r5.title, "📎 " + r30.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:891:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupDocument", org.telegram.messenger.R.string.NotificationMessageGroupDocument, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:892:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r2, r5.title, r30.messageText);
     */
    /* JADX WARN: Code restructure failed: missing block: B:893:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupNoText", org.telegram.messenger.R.string.NotificationMessageGroupNoText, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:894:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMap", org.telegram.messenger.R.string.NotificationMessageGroupMap, r2, r5.title);
     */
    /* JADX WARN: Removed duplicated region for block: B:274:0x0659  */
    /* JADX WARN: Removed duplicated region for block: B:808:0x170a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getStringForMessage(MessageObject messageObject, boolean z, boolean[] zArr, boolean[] zArr2) {
        String str;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$Chat tLRPC$Chat2;
        boolean z2;
        String formatString;
        String charSequence;
        char c;
        boolean z3;
        String formatString2;
        if (AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter) {
            return LocaleController.getString("YouHaveNewMessage", R.string.YouHaveNewMessage);
        }
        if (messageObject.isStoryPush || messageObject.isStoryMentionPush) {
            return "!" + messageObject.messageOwner.message;
        }
        TLRPC$Message tLRPC$Message = messageObject.messageOwner;
        long j = tLRPC$Message.dialog_id;
        TLRPC$Peer tLRPC$Peer = tLRPC$Message.peer_id;
        long j2 = tLRPC$Peer.chat_id;
        if (j2 == 0) {
            j2 = tLRPC$Peer.channel_id;
        }
        long j3 = tLRPC$Peer.user_id;
        if (zArr2 != null) {
            zArr2[0] = true;
        }
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        boolean z4 = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CONTENT_PREVIEW + j, true);
        if (messageObject.isFcmMessage()) {
            if (j2 == 0 && j3 != 0) {
                int i = z4 ? 1 : 1;
                if (zArr2 != null) {
                    zArr2[0] = false;
                }
                int i2 = R.string.NotificationMessageNoText;
                Object[] objArr = new Object[i];
                objArr[0] = messageObject.localName;
                return LocaleController.formatString("NotificationMessageNoText", i2, objArr);
            } else if (j2 != 0) {
                if (z4) {
                    boolean z5 = !messageObject.localChannel ? true : true;
                    if (messageObject.localChannel) {
                    }
                }
                if (zArr2 != null) {
                    zArr2[0] = false;
                }
                return (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup()) ? LocaleController.formatString("NotificationMessageGroupNoText", R.string.NotificationMessageGroupNoText, messageObject.localUserName, messageObject.localName) : LocaleController.formatString("ChannelMessageNoText", R.string.ChannelMessageNoText, messageObject.localName);
            }
            zArr[0] = true;
            return (String) messageObject.messageText;
        }
        long clientUserId = getUserConfig().getClientUserId();
        if (j3 == 0) {
            j3 = messageObject.getFromChatId();
            if (j3 == 0) {
                j3 = -j2;
            }
        } else if (j3 == clientUserId) {
            j3 = messageObject.getFromChatId();
        }
        if (j == 0) {
            if (j2 != 0) {
                j = -j2;
            } else if (j3 != 0) {
                j = j3;
            }
        }
        if (j3 > 0) {
            if (!messageObject.messageOwner.from_scheduled) {
                TLRPC$User user = getMessagesController().getUser(Long.valueOf(j3));
                if (user != null) {
                    str = UserObject.getUserName(user);
                }
                str = null;
            } else if (j == clientUserId) {
                str = LocaleController.getString("MessageScheduledReminderNotification", R.string.MessageScheduledReminderNotification);
            } else {
                str = LocaleController.getString("NotificationMessageScheduledName", R.string.NotificationMessageScheduledName);
            }
        } else {
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j3));
            if (chat != null) {
                str = chat.title;
            }
            str = null;
        }
        if (str == null) {
            return null;
        }
        if (j2 != 0) {
            tLRPC$Chat = getMessagesController().getChat(Long.valueOf(j2));
            if (tLRPC$Chat == null) {
                return null;
            }
        } else {
            tLRPC$Chat = null;
        }
        if (DialogObject.isEncryptedDialog(j)) {
            charSequence = LocaleController.getString("YouHaveNewMessage", R.string.YouHaveNewMessage);
        } else {
            TLRPC$Chat tLRPC$Chat3 = tLRPC$Chat;
            if (j2 != 0 || j3 == 0) {
                if (j2 != 0) {
                    if (ChatObject.isChannel(tLRPC$Chat3)) {
                        tLRPC$Chat2 = tLRPC$Chat3;
                        if (!tLRPC$Chat2.megagroup) {
                            z2 = true;
                            if (z4) {
                                boolean z6 = !z2 ? true : true;
                                if (z2) {
                                }
                            }
                            if (zArr2 != null) {
                                zArr2[0] = false;
                            }
                            return (ChatObject.isChannel(tLRPC$Chat2) || tLRPC$Chat2.megagroup) ? (messageObject.type == 29 || !(MessageObject.getMedia(messageObject) instanceof TLRPC$TL_messageMediaPaidMedia)) ? LocaleController.formatString("NotificationMessageGroupNoText", R.string.NotificationMessageGroupNoText, str, tLRPC$Chat2.title) : LocaleController.formatPluralString("NotificationMessagePaidMedia", (int) ((TLRPC$TL_messageMediaPaidMedia) MessageObject.getMedia(messageObject)).stars_amount, str) : LocaleController.formatString("ChannelMessageNoText", R.string.ChannelMessageNoText, str);
                        }
                    } else {
                        tLRPC$Chat2 = tLRPC$Chat3;
                    }
                    z2 = false;
                    if (z4) {
                    }
                    if (zArr2 != null) {
                    }
                    if (ChatObject.isChannel(tLRPC$Chat2)) {
                    }
                }
                String str2 = null;
                return str2;
            } else if (z4 && notificationsSettings.getBoolean("EnablePreviewAll", true)) {
                TLRPC$Message tLRPC$Message2 = messageObject.messageOwner;
                if (tLRPC$Message2 instanceof TLRPC$TL_messageService) {
                    TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message2.action;
                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetSameChatWallPaper) {
                        charSequence = LocaleController.getString("WallpaperSameNotification", R.string.WallpaperSameNotification);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatWallPaper) {
                        charSequence = LocaleController.getString("WallpaperNotification", R.string.WallpaperNotification);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGeoProximityReached) {
                        charSequence = messageObject.messageText.toString();
                    } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserJoined) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp)) {
                        charSequence = LocaleController.formatString("NotificationContactJoined", R.string.NotificationContactJoined, str);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserUpdatedPhoto) {
                        charSequence = LocaleController.formatString("NotificationContactNewPhoto", R.string.NotificationContactNewPhoto, str);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionLoginUnknownLocation) {
                        String formatString3 = LocaleController.formatString("formatDateAtTime", R.string.formatDateAtTime, LocaleController.getInstance().getFormatterYear().format(messageObject.messageOwner.date * 1000), LocaleController.getInstance().getFormatterDay().format(messageObject.messageOwner.date * 1000));
                        int i3 = R.string.NotificationUnrecognizedDevice;
                        TLRPC$MessageAction tLRPC$MessageAction2 = messageObject.messageOwner.action;
                        charSequence = LocaleController.formatString("NotificationUnrecognizedDevice", i3, getUserConfig().getCurrentUser().first_name, formatString3, tLRPC$MessageAction2.title, tLRPC$MessageAction2.address);
                    } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionGameScore) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPaymentSent)) {
                        charSequence = messageObject.messageText.toString();
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPhoneCall) {
                        if (tLRPC$MessageAction.video) {
                            charSequence = LocaleController.getString("CallMessageVideoIncomingMissed", R.string.CallMessageVideoIncomingMissed);
                        } else {
                            charSequence = LocaleController.getString("CallMessageIncomingMissed", R.string.CallMessageIncomingMissed);
                        }
                    } else {
                        if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatTheme) {
                            String str3 = ((TLRPC$TL_messageActionSetChatTheme) tLRPC$MessageAction).emoticon;
                            if (!TextUtils.isEmpty(str3)) {
                                c = 0;
                                z3 = true;
                                if (j == clientUserId) {
                                    formatString2 = LocaleController.formatString("ChangedChatThemeYou", R.string.ChatThemeChangedYou, str3);
                                } else {
                                    formatString2 = LocaleController.formatString("ChangedChatThemeTo", R.string.ChatThemeChangedTo, str, str3);
                                }
                            } else if (j == clientUserId) {
                                c = 0;
                                formatString2 = LocaleController.formatString("ChatThemeDisabledYou", R.string.ChatThemeDisabledYou, new Object[0]);
                                z3 = true;
                            } else {
                                c = 0;
                                z3 = true;
                                formatString2 = LocaleController.formatString("ChatThemeDisabled", R.string.ChatThemeDisabled, str, str3);
                            }
                            charSequence = formatString2;
                            zArr[c] = z3;
                        }
                        String str22 = null;
                        return str22;
                    }
                } else if (!messageObject.isMediaEmpty()) {
                    TLRPC$Message tLRPC$Message3 = messageObject.messageOwner;
                    if (tLRPC$Message3.media instanceof TLRPC$TL_messageMediaPhoto) {
                        if (!z && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(tLRPC$Message3.message)) {
                            charSequence = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, str, "🖼 " + messageObject.messageOwner.message);
                            zArr[0] = true;
                        } else if (messageObject.messageOwner.media.ttl_seconds != 0) {
                            charSequence = LocaleController.formatString("NotificationMessageSDPhoto", R.string.NotificationMessageSDPhoto, str);
                        } else {
                            charSequence = LocaleController.formatString("NotificationMessagePhoto", R.string.NotificationMessagePhoto, str);
                        }
                    } else if (messageObject.isVideo()) {
                        if (!z && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            charSequence = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, str, "📹 " + messageObject.messageOwner.message);
                            zArr[0] = true;
                        } else if (messageObject.messageOwner.media.ttl_seconds != 0) {
                            charSequence = LocaleController.formatString("NotificationMessageSDVideo", R.string.NotificationMessageSDVideo, str);
                        } else {
                            charSequence = LocaleController.formatString("NotificationMessageVideo", R.string.NotificationMessageVideo, str);
                        }
                    } else if (messageObject.isGame()) {
                        charSequence = LocaleController.formatString("NotificationMessageGame", R.string.NotificationMessageGame, str, messageObject.messageOwner.media.game.title);
                    } else if (messageObject.isVoice()) {
                        charSequence = LocaleController.formatString("NotificationMessageAudio", R.string.NotificationMessageAudio, str);
                    } else if (messageObject.isRoundVideo()) {
                        charSequence = LocaleController.formatString("NotificationMessageRound", R.string.NotificationMessageRound, str);
                    } else if (messageObject.isMusic()) {
                        charSequence = LocaleController.formatString("NotificationMessageMusic", R.string.NotificationMessageMusic, str);
                    } else {
                        TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
                        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaContact) {
                            TLRPC$TL_messageMediaContact tLRPC$TL_messageMediaContact = (TLRPC$TL_messageMediaContact) tLRPC$MessageMedia;
                            charSequence = LocaleController.formatString("NotificationMessageContact2", R.string.NotificationMessageContact2, str, ContactsController.formatName(tLRPC$TL_messageMediaContact.first_name, tLRPC$TL_messageMediaContact.last_name));
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGiveaway) {
                            TLRPC$TL_messageMediaGiveaway tLRPC$TL_messageMediaGiveaway = (TLRPC$TL_messageMediaGiveaway) tLRPC$MessageMedia;
                            charSequence = LocaleController.formatString("NotificationMessageChannelGiveaway", R.string.NotificationMessageChannelGiveaway, str, Integer.valueOf(tLRPC$TL_messageMediaGiveaway.quantity), Integer.valueOf(tLRPC$TL_messageMediaGiveaway.months));
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGiveawayResults) {
                            charSequence = LocaleController.formatString("BoostingGiveawayResults", R.string.BoostingGiveawayResults, new Object[0]);
                        } else {
                            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                                TLRPC$Poll tLRPC$Poll = ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia).poll;
                                if (tLRPC$Poll.quiz) {
                                    formatString = LocaleController.formatString("NotificationMessageQuiz2", R.string.NotificationMessageQuiz2, str, tLRPC$Poll.question.text);
                                } else {
                                    formatString = LocaleController.formatString("NotificationMessagePoll2", R.string.NotificationMessagePoll2, str, tLRPC$Poll.question.text);
                                }
                            } else if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeo) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaVenue)) {
                                charSequence = LocaleController.formatString("NotificationMessageMap", R.string.NotificationMessageMap, str);
                            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeoLive) {
                                charSequence = LocaleController.formatString("NotificationMessageLiveLocation", R.string.NotificationMessageLiveLocation, str);
                            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                                if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {
                                    String stickerEmoji = messageObject.getStickerEmoji();
                                    if (stickerEmoji != null) {
                                        formatString = LocaleController.formatString("NotificationMessageStickerEmoji", R.string.NotificationMessageStickerEmoji, str, stickerEmoji);
                                    } else {
                                        formatString = LocaleController.formatString("NotificationMessageSticker", R.string.NotificationMessageSticker, str);
                                    }
                                } else if (messageObject.isGif()) {
                                    if (!z && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                        charSequence = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, str, "🎬 " + messageObject.messageOwner.message);
                                        zArr[0] = true;
                                    } else {
                                        charSequence = LocaleController.formatString("NotificationMessageGif", R.string.NotificationMessageGif, str);
                                    }
                                } else if (!z && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                    charSequence = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, str, "📎 " + messageObject.messageOwner.message);
                                    zArr[0] = true;
                                } else {
                                    charSequence = LocaleController.formatString("NotificationMessageDocument", R.string.NotificationMessageDocument, str);
                                }
                            } else if (!z && !TextUtils.isEmpty(messageObject.messageText)) {
                                charSequence = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, str, messageObject.messageText);
                                zArr[0] = true;
                            } else {
                                charSequence = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str);
                            }
                            charSequence = formatString;
                        }
                    }
                } else if (!z) {
                    if (!TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        charSequence = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, str, messageObject.messageOwner.message);
                        zArr[0] = true;
                    } else {
                        charSequence = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str);
                    }
                } else {
                    charSequence = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str);
                }
            } else {
                if (zArr2 != null) {
                    zArr2[0] = false;
                }
                charSequence = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str);
            }
        }
        return charSequence;
    }

    private void scheduleNotificationRepeat() {
        try {
            Intent intent = new Intent(ApplicationLoader.applicationContext, NotificationRepeat.class);
            intent.putExtra("currentAccount", this.currentAccount);
            PendingIntent service = PendingIntent.getService(ApplicationLoader.applicationContext, 0, intent, ConnectionsManager.FileTypeVideo);
            int i = getAccountInstance().getNotificationsSettings().getInt("repeat_messages", 60);
            if (i > 0 && this.personalCount > 0) {
                this.alarmManager.set(2, SystemClock.elapsedRealtime() + (i * 60 * 1000), service);
            } else {
                this.alarmManager.cancel(service);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private boolean isPersonalMessage(MessageObject messageObject) {
        TLRPC$MessageAction tLRPC$MessageAction;
        TLRPC$Message tLRPC$Message = messageObject.messageOwner;
        TLRPC$Peer tLRPC$Peer = tLRPC$Message.peer_id;
        return (tLRPC$Peer != null && tLRPC$Peer.chat_id == 0 && tLRPC$Peer.channel_id == 0 && ((tLRPC$MessageAction = tLRPC$Message.action) == null || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionEmpty))) || messageObject.isStoryReactionPush;
    }

    private int getNotifyOverride(SharedPreferences sharedPreferences, long j, long j2) {
        int property = this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_NOTIFY, j, j2, -1);
        if (property != 3 || this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL, j, j2, 0) < getConnectionsManager().getCurrentTime()) {
            return property;
        }
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showNotifications$33() {
        showOrUpdateNotification(false);
    }

    public void showNotifications() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$showNotifications$33();
            }
        });
    }

    public void hideNotifications() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$hideNotifications$34();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideNotifications$34() {
        notificationManager.cancel(this.notificationId);
        this.lastWearNotifiedMessageId.clear();
        for (int i = 0; i < this.wearNotificationsIds.size(); i++) {
            notificationManager.cancel(this.wearNotificationsIds.valueAt(i).intValue());
        }
        this.wearNotificationsIds.clear();
    }

    private void dismissNotification() {
        try {
            notificationManager.cancel(this.notificationId);
            this.pushMessages.clear();
            this.pushMessagesDict.clear();
            this.lastWearNotifiedMessageId.clear();
            for (int i = 0; i < this.wearNotificationsIds.size(); i++) {
                if (!this.openedInBubbleDialogs.contains(Long.valueOf(this.wearNotificationsIds.keyAt(i)))) {
                    notificationManager.cancel(this.wearNotificationsIds.valueAt(i).intValue());
                }
            }
            this.wearNotificationsIds.clear();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda47
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.lambda$dismissNotification$35();
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dismissNotification$35() {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    private void playInChatSound() {
        if (!this.inChatSoundEnabled || MediaController.getInstance().isRecordingAudio()) {
            return;
        }
        try {
            if (audioManager.getRingerMode() == 0) {
                return;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            if (getNotifyOverride(getAccountInstance().getNotificationsSettings(), this.openedDialogId, this.openedTopicId) == 2) {
                return;
            }
            notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$playInChatSound$37();
                }
            });
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playInChatSound$37() {
        if (Math.abs(SystemClock.elapsedRealtime() - this.lastSoundPlay) <= 500) {
            return;
        }
        try {
            if (this.soundPool == null) {
                SoundPool soundPool = new SoundPool(3, 1, 0);
                this.soundPool = soundPool;
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda3
                    @Override // android.media.SoundPool.OnLoadCompleteListener
                    public final void onLoadComplete(SoundPool soundPool2, int i, int i2) {
                        NotificationsController.lambda$playInChatSound$36(soundPool2, i, i2);
                    }
                });
            }
            if (this.soundIn == 0 && !this.soundInLoaded) {
                this.soundInLoaded = true;
                this.soundIn = this.soundPool.load(ApplicationLoader.applicationContext, R.raw.sound_in, 1);
            }
            int i = this.soundIn;
            if (i != 0) {
                try {
                    this.soundPool.play(i, 1.0f, 1.0f, 1, 0, 1.0f);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$playInChatSound$36(SoundPool soundPool, int i, int i2) {
        if (i2 == 0) {
            try {
                soundPool.play(i, 1.0f, 1.0f, 1, 0, 1.0f);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    private void scheduleNotificationDelay(boolean z) {
        try {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("delay notification start, onlineReason = " + z);
            }
            this.notificationDelayWakelock.acquire(10000L);
            DispatchQueue dispatchQueue = notificationsQueue;
            dispatchQueue.cancelRunnable(this.notificationDelayRunnable);
            dispatchQueue.postRunnable(this.notificationDelayRunnable, z ? 3000 : 1000);
        } catch (Exception e) {
            FileLog.e(e);
            showOrUpdateNotification(this.notifyCheck);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void repeatNotificationMaybe() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$repeatNotificationMaybe$38();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$repeatNotificationMaybe$38() {
        int i = Calendar.getInstance().get(11);
        if (i >= 11 && i <= 22) {
            notificationManager.cancel(this.notificationId);
            showOrUpdateNotification(true);
            return;
        }
        scheduleNotificationRepeat();
    }

    private boolean isEmptyVibration(long[] jArr) {
        if (jArr == null || jArr.length == 0) {
            return false;
        }
        for (long j : jArr) {
            if (j != 0) {
                return false;
            }
        }
        return true;
    }

    public void deleteNotificationChannel(long j, long j2) {
        deleteNotificationChannel(j, j2, -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: deleteNotificationChannelInternal */
    public void lambda$deleteNotificationChannel$39(long j, long j2, int i) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        try {
            SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
            SharedPreferences.Editor edit = notificationsSettings.edit();
            if (i == 0 || i == -1) {
                String str = "org.telegram.key" + j;
                if (j2 != 0) {
                    str = str + ".topic" + j2;
                }
                String string = notificationsSettings.getString(str, null);
                if (string != null) {
                    edit.remove(str).remove(str + "_s");
                    try {
                        systemNotificationManager.deleteNotificationChannel(string);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("delete channel internal " + string);
                    }
                }
            }
            if (i == 1 || i == -1) {
                String str2 = "org.telegram.keyia" + j;
                String string2 = notificationsSettings.getString(str2, null);
                if (string2 != null) {
                    edit.remove(str2).remove(str2 + "_s");
                    try {
                        systemNotificationManager.deleteNotificationChannel(string2);
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("delete channel internal " + string2);
                    }
                }
            }
            edit.commit();
        } catch (Exception e3) {
            FileLog.e(e3);
        }
    }

    public void deleteNotificationChannel(final long j, final long j2, final int i) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$deleteNotificationChannel$39(j, j2, i);
            }
        });
    }

    public void deleteNotificationChannelGlobal(int i) {
        deleteNotificationChannelGlobal(i, -1);
    }

    /* renamed from: deleteNotificationChannelGlobalInternal */
    public void lambda$deleteNotificationChannelGlobal$40(int i, int i2) {
        String str;
        String str2;
        String str3;
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        try {
            SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
            SharedPreferences.Editor edit = notificationsSettings.edit();
            if (i2 == 0 || i2 == -1) {
                if (i == 2) {
                    str = "channels";
                } else if (i == 0) {
                    str = "groups";
                } else if (i == 3) {
                    str = "stories";
                } else {
                    if (i != 4 && i != 5) {
                        str = "private";
                    }
                    str = "reactions";
                }
                String string = notificationsSettings.getString(str, null);
                if (string != null) {
                    SharedPreferences.Editor remove = edit.remove(str);
                    remove.remove(str + "_s");
                    try {
                        systemNotificationManager.deleteNotificationChannel(string);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("delete channel global internal " + string);
                    }
                }
            }
            if (i2 == 1 || i2 == -1) {
                if (i == 2) {
                    str2 = "channels_ia";
                } else if (i == 0) {
                    str2 = "groups_ia";
                } else if (i == 3) {
                    str2 = "stories_ia";
                } else {
                    if (i != 4 && i != 5) {
                        str2 = "private_ia";
                    }
                    str2 = "reactions_ia";
                }
                String string2 = notificationsSettings.getString(str2, null);
                if (string2 != null) {
                    SharedPreferences.Editor remove2 = edit.remove(str2);
                    remove2.remove(str2 + "_s");
                    try {
                        systemNotificationManager.deleteNotificationChannel(string2);
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("delete channel global internal " + string2);
                    }
                }
            }
            if (i == 2) {
                str3 = "overwrite_channel";
            } else if (i == 0) {
                str3 = "overwrite_group";
            } else if (i == 3) {
                str3 = "overwrite_stories";
            } else {
                if (i != 4 && i != 5) {
                    str3 = "overwrite_private";
                }
                str3 = "overwrite_reactions";
            }
            edit.remove(str3);
            edit.commit();
        } catch (Exception e3) {
            FileLog.e(e3);
        }
    }

    public void deleteNotificationChannelGlobal(final int i, final int i2) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$deleteNotificationChannelGlobal$40(i, i2);
            }
        });
    }

    public void deleteAllNotificationChannels() {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$deleteAllNotificationChannels$41();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteAllNotificationChannels$41() {
        try {
            SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
            Map<String, ?> all = notificationsSettings.getAll();
            SharedPreferences.Editor edit = notificationsSettings.edit();
            for (Map.Entry<String, ?> entry : all.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith("org.telegram.key")) {
                    if (!key.endsWith("_s")) {
                        String str = (String) entry.getValue();
                        systemNotificationManager.deleteNotificationChannel(str);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("delete all channel " + str);
                        }
                    }
                    edit.remove(key);
                }
            }
            edit.commit();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private boolean unsupportedNotificationShortcut() {
        return Build.VERSION.SDK_INT < 29 || !SharedConfig.chatBubbles;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x00f0 A[Catch: Exception -> 0x0155, TryCatch #0 {Exception -> 0x0155, blocks: (B:9:0x0020, B:12:0x0061, B:14:0x0069, B:18:0x0079, B:20:0x00a2, B:22:0x00b2, B:24:0x00bc, B:26:0x00f0, B:28:0x00f8, B:30:0x0101, B:39:0x0122, B:43:0x0139, B:44:0x0150, B:32:0x0108, B:34:0x010e, B:36:0x0113, B:35:0x0111, B:37:0x0118, B:27:0x00f4, B:17:0x0075, B:13:0x0065), top: B:51:0x0020 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00f4 A[Catch: Exception -> 0x0155, TryCatch #0 {Exception -> 0x0155, blocks: (B:9:0x0020, B:12:0x0061, B:14:0x0069, B:18:0x0079, B:20:0x00a2, B:22:0x00b2, B:24:0x00bc, B:26:0x00f0, B:28:0x00f8, B:30:0x0101, B:39:0x0122, B:43:0x0139, B:44:0x0150, B:32:0x0108, B:34:0x010e, B:36:0x0113, B:35:0x0111, B:37:0x0118, B:27:0x00f4, B:17:0x0075, B:13:0x0065), top: B:51:0x0020 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0101 A[Catch: Exception -> 0x0155, TryCatch #0 {Exception -> 0x0155, blocks: (B:9:0x0020, B:12:0x0061, B:14:0x0069, B:18:0x0079, B:20:0x00a2, B:22:0x00b2, B:24:0x00bc, B:26:0x00f0, B:28:0x00f8, B:30:0x0101, B:39:0x0122, B:43:0x0139, B:44:0x0150, B:32:0x0108, B:34:0x010e, B:36:0x0113, B:35:0x0111, B:37:0x0118, B:27:0x00f4, B:17:0x0075, B:13:0x0065), top: B:51:0x0020 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0122 A[Catch: Exception -> 0x0155, TryCatch #0 {Exception -> 0x0155, blocks: (B:9:0x0020, B:12:0x0061, B:14:0x0069, B:18:0x0079, B:20:0x00a2, B:22:0x00b2, B:24:0x00bc, B:26:0x00f0, B:28:0x00f8, B:30:0x0101, B:39:0x0122, B:43:0x0139, B:44:0x0150, B:32:0x0108, B:34:0x010e, B:36:0x0113, B:35:0x0111, B:37:0x0118, B:27:0x00f4, B:17:0x0075, B:13:0x0065), top: B:51:0x0020 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0150 A[Catch: Exception -> 0x0155, TRY_LEAVE, TryCatch #0 {Exception -> 0x0155, blocks: (B:9:0x0020, B:12:0x0061, B:14:0x0069, B:18:0x0079, B:20:0x00a2, B:22:0x00b2, B:24:0x00bc, B:26:0x00f0, B:28:0x00f8, B:30:0x0101, B:39:0x0122, B:43:0x0139, B:44:0x0150, B:32:0x0108, B:34:0x010e, B:36:0x0113, B:35:0x0111, B:37:0x0118, B:27:0x00f4, B:17:0x0075, B:13:0x0065), top: B:51:0x0020 }] */
    @SuppressLint({"RestrictedApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String createNotificationShortcut(NotificationCompat.Builder builder, long j, String str, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, Person person, boolean z) {
        Bitmap bitmap;
        IconCompat createWithResource;
        if (unsupportedNotificationShortcut()) {
            return null;
        }
        if (!ChatObject.isChannel(tLRPC$Chat) || tLRPC$Chat.megagroup) {
            try {
                String str2 = "ndid_" + j;
                Intent intent = new Intent(ApplicationLoader.applicationContext, OpenChatReceiver.class);
                intent.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                if (j > 0) {
                    intent.putExtra("userId", j);
                } else {
                    intent.putExtra("chatId", -j);
                }
                ShortcutInfoCompat.Builder locusId = new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, str2).setShortLabel(tLRPC$Chat != null ? str : UserObject.getFirstName(tLRPC$User)).setLongLabel(str).setIntent(new Intent("android.intent.action.VIEW")).setIntent(intent).setLongLived(true).setLocusId(new LocusIdCompat(str2));
                if (person != null) {
                    locusId.setPerson(person);
                    locusId.setIcon(person.getIcon());
                    if (person.getIcon() != null) {
                        bitmap = person.getIcon().getBitmap();
                        ShortcutInfoCompat build = locusId.build();
                        ShortcutManagerCompat.pushDynamicShortcut(ApplicationLoader.applicationContext, build);
                        builder.setShortcutInfo(build);
                        Intent intent2 = new Intent(ApplicationLoader.applicationContext, BubbleActivity.class);
                        intent2.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                        if (!DialogObject.isUserDialog(j)) {
                            intent2.putExtra("userId", j);
                        } else {
                            intent2.putExtra("chatId", -j);
                        }
                        intent2.putExtra("currentAccount", this.currentAccount);
                        if (bitmap == null) {
                            createWithResource = IconCompat.createWithAdaptiveBitmap(bitmap);
                        } else if (tLRPC$User != null) {
                            createWithResource = IconCompat.createWithResource(ApplicationLoader.applicationContext, tLRPC$User.bot ? R.drawable.book_bot : R.drawable.book_user);
                        } else {
                            createWithResource = IconCompat.createWithResource(ApplicationLoader.applicationContext, R.drawable.book_group);
                        }
                        if (!z) {
                            NotificationCompat.BubbleMetadata.Builder builder2 = new NotificationCompat.BubbleMetadata.Builder(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 167772160), createWithResource);
                            builder2.setSuppressNotification(this.openedDialogId == j);
                            builder2.setAutoExpandBubble(false);
                            builder2.setDesiredHeight(AndroidUtilities.dp(640.0f));
                            builder.setBubbleMetadata(builder2.build());
                        } else {
                            builder.setBubbleMetadata(null);
                        }
                        return str2;
                    }
                }
                bitmap = null;
                ShortcutInfoCompat build2 = locusId.build();
                ShortcutManagerCompat.pushDynamicShortcut(ApplicationLoader.applicationContext, build2);
                builder.setShortcutInfo(build2);
                Intent intent22 = new Intent(ApplicationLoader.applicationContext, BubbleActivity.class);
                intent22.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                if (!DialogObject.isUserDialog(j)) {
                }
                intent22.putExtra("currentAccount", this.currentAccount);
                if (bitmap == null) {
                }
                if (!z) {
                }
                return str2;
            } catch (Exception e) {
                FileLog.e(e);
                return null;
            }
        }
        return null;
    }

    @TargetApi(26)
    protected void ensureGroupsCreated() {
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        if (this.groupsCreated == null) {
            this.groupsCreated = Boolean.valueOf(notificationsSettings.getBoolean("groupsCreated5", false));
        }
        if (!this.groupsCreated.booleanValue()) {
            try {
                String str = this.currentAccount + "channel";
                List<NotificationChannel> notificationChannels = systemNotificationManager.getNotificationChannels();
                int size = notificationChannels.size();
                SharedPreferences.Editor editor = null;
                for (int i = 0; i < size; i++) {
                    NotificationChannel notificationChannel = notificationChannels.get(i);
                    String id = notificationChannel.getId();
                    if (id.startsWith(str)) {
                        int importance = notificationChannel.getImportance();
                        if (importance != 4 && importance != 5 && !id.contains("_ia_")) {
                            if (id.contains("_channels_")) {
                                if (editor == null) {
                                    editor = getAccountInstance().getNotificationsSettings().edit();
                                }
                                editor.remove("priority_channel").remove("vibrate_channel").remove("ChannelSoundPath").remove("ChannelSound");
                            } else if (id.contains("_reactions_")) {
                                if (editor == null) {
                                    editor = getAccountInstance().getNotificationsSettings().edit();
                                }
                                editor.remove("priority_react").remove("vibrate_react").remove("ReactionSoundPath").remove("ReactionSound");
                            } else if (id.contains("_groups_")) {
                                if (editor == null) {
                                    editor = getAccountInstance().getNotificationsSettings().edit();
                                }
                                editor.remove("priority_group").remove("vibrate_group").remove("GroupSoundPath").remove("GroupSound");
                            } else if (id.contains("_private_")) {
                                if (editor == null) {
                                    editor = getAccountInstance().getNotificationsSettings().edit();
                                }
                                editor.remove("priority_messages");
                                editor.remove("priority_group").remove("vibrate_messages").remove("GlobalSoundPath").remove("GlobalSound");
                            } else {
                                long longValue = Utilities.parseLong(id.substring(9, id.indexOf(95, 9))).longValue();
                                if (longValue != 0) {
                                    if (editor == null) {
                                        editor = getAccountInstance().getNotificationsSettings().edit();
                                    }
                                    editor.remove("priority_" + longValue).remove("vibrate_" + longValue).remove("sound_path_" + longValue).remove("sound_" + longValue);
                                }
                            }
                        }
                        systemNotificationManager.deleteNotificationChannel(id);
                    }
                }
                if (editor != null) {
                    editor.commit();
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            notificationsSettings.edit().putBoolean("groupsCreated5", true).commit();
            this.groupsCreated = Boolean.TRUE;
        }
        if (this.channelGroupsCreated) {
            return;
        }
        List<NotificationChannelGroup> notificationChannelGroups = systemNotificationManager.getNotificationChannelGroups();
        String str2 = "channels" + this.currentAccount;
        String str3 = "groups" + this.currentAccount;
        int size2 = notificationChannelGroups.size();
        String str4 = "other" + this.currentAccount;
        String str5 = "reactions" + this.currentAccount;
        String str6 = "stories" + this.currentAccount;
        String str7 = "private" + this.currentAccount;
        for (int i2 = 0; i2 < size2; i2++) {
            String id2 = notificationChannelGroups.get(i2).getId();
            if (str2 != null && str2.equals(id2)) {
                str2 = null;
            } else if (str3 != null && str3.equals(id2)) {
                str3 = null;
            } else if (str6 != null && str6.equals(id2)) {
                str6 = null;
            } else if (str5 != null && str5.equals(id2)) {
                str5 = null;
            } else if (str7 != null && str7.equals(id2)) {
                str7 = null;
            } else if (str4 != null && str4.equals(id2)) {
                str4 = null;
            }
            if (str2 == null && str6 == null && str5 == null && str3 == null && str7 == null && str4 == null) {
                break;
            }
        }
        if (str2 != null || str3 != null || str5 != null || str6 != null || str7 != null || str4 != null) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(getUserConfig().getClientUserId()));
            if (user == null) {
                getUserConfig().getCurrentUser();
            }
            String str8 = user != null ? " (" + ContactsController.formatName(user.first_name, user.last_name) + ")" : "";
            ArrayList arrayList = new ArrayList();
            if (str2 != null) {
                arrayList.add(new NotificationChannelGroup(str2, LocaleController.getString("NotificationsChannels", R.string.NotificationsChannels) + str8));
            }
            if (str3 != null) {
                arrayList.add(new NotificationChannelGroup(str3, LocaleController.getString("NotificationsGroups", R.string.NotificationsGroups) + str8));
            }
            if (str6 != null) {
                arrayList.add(new NotificationChannelGroup(str6, LocaleController.getString(R.string.NotificationsStories) + str8));
            }
            if (str5 != null) {
                arrayList.add(new NotificationChannelGroup(str5, LocaleController.getString(R.string.NotificationsReactions) + str8));
            }
            if (str7 != null) {
                arrayList.add(new NotificationChannelGroup(str7, LocaleController.getString("NotificationsPrivateChats", R.string.NotificationsPrivateChats) + str8));
            }
            if (str4 != null) {
                arrayList.add(new NotificationChannelGroup(str4, LocaleController.getString("NotificationsOther", R.string.NotificationsOther) + str8));
            }
            systemNotificationManager.createNotificationChannelGroups(arrayList);
        }
        this.channelGroupsCreated = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:247:0x0526  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x0546 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:257:0x0585 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:265:0x0593 A[LOOP:1: B:263:0x0590->B:265:0x0593, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:268:0x05a7  */
    /* JADX WARN: Removed duplicated region for block: B:271:0x05b3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:275:0x05c4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:285:0x05dc  */
    /* JADX WARN: Removed duplicated region for block: B:288:0x05f3  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x01c4  */
    @TargetApi(26)
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String validateChannelId(long j, long j2, String str, long[] jArr, int i, Uri uri, int i2, boolean z, boolean z2, boolean z3, int i3) {
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String formatString;
        String str7;
        String string;
        String str8;
        String str9;
        StringBuilder sb;
        String str10;
        String str11;
        String str12;
        NotificationsController notificationsController;
        int i4;
        String str13;
        String str14;
        boolean z4;
        int i5;
        long j3;
        long[] jArr2;
        String str15;
        String str16;
        boolean z5;
        String str17;
        String str18;
        StringBuilder sb2;
        Uri uri2;
        boolean z6;
        boolean z7;
        long[] jArr3;
        String str19;
        String str20;
        int i6;
        SharedPreferences.Editor editor;
        int i7;
        String str21;
        ensureGroupsCreated();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        String str22 = "reactions";
        if (z3) {
            str4 = "groups";
            str5 = "other" + this.currentAccount;
            str6 = null;
        } else {
            if (i3 == 2) {
                str2 = "channels" + this.currentAccount;
                str3 = "overwrite_channel";
            } else if (i3 == 0) {
                str2 = "groups" + this.currentAccount;
                str3 = "overwrite_group";
            } else if (i3 == 3) {
                str2 = "stories" + this.currentAccount;
                str3 = "overwrite_stories";
            } else if (i3 == 4 || i3 == 5) {
                str2 = "reactions" + this.currentAccount;
                str3 = "overwrite_reactions";
            } else {
                str2 = "private" + this.currentAccount;
                str3 = "overwrite_private";
            }
            str4 = "groups";
            String str23 = str3;
            str5 = str2;
            str6 = str23;
        }
        boolean z8 = !z && DialogObject.isEncryptedDialog(j);
        boolean z9 = (z2 || str6 == null || !notificationsSettings.getBoolean(str6, false)) ? false : true;
        String MD5 = Utilities.MD5(uri == null ? "NoSound2" : uri.toString());
        if (MD5 != null && MD5.length() > 5) {
            MD5 = MD5.substring(0, 5);
        }
        if (z3) {
            formatString = LocaleController.getString("NotificationsSilent", R.string.NotificationsSilent);
            str22 = "silent";
        } else if (z) {
            String string2 = z2 ? LocaleController.getString("NotificationsInAppDefault", R.string.NotificationsInAppDefault) : LocaleController.getString("NotificationsDefault", R.string.NotificationsDefault);
            if (i3 == 2) {
                str7 = string2;
                str22 = z2 ? "channels_ia" : "channels";
            } else {
                if (i3 == 0) {
                    if (z2) {
                        str4 = "groups_ia";
                    }
                    str22 = str4;
                } else if (i3 == 3) {
                    str22 = z2 ? "stories_ia" : "stories";
                } else if (i3 != 4 && i3 != 5) {
                    str22 = z2 ? "private_ia" : "private";
                } else if (z2) {
                    str22 = "reactions_ia";
                }
                str7 = string2;
            }
            String str24 = str22 + "_" + MD5;
            string = notificationsSettings.getString(str24, null);
            String string3 = notificationsSettings.getString(str24 + "_s", null);
            StringBuilder sb3 = new StringBuilder();
            if (string == null) {
                NotificationChannel notificationChannel = systemNotificationManager.getNotificationChannel(string);
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder sb4 = new StringBuilder();
                    str14 = str5;
                    sb4.append("current channel for ");
                    sb4.append(string);
                    sb4.append(" = ");
                    sb4.append(notificationChannel);
                    FileLog.d(sb4.toString());
                } else {
                    str14 = str5;
                }
                if (notificationChannel == null) {
                    str11 = "_s";
                    str12 = str24;
                    str8 = "secret";
                    sb = sb3;
                    notificationsController = this;
                    i4 = i2;
                    str13 = "_";
                    z4 = true;
                    i5 = i;
                    j3 = j;
                    jArr2 = jArr;
                    str16 = null;
                    str15 = null;
                    z5 = false;
                    str17 = null;
                    if (z5) {
                    }
                    str18 = str11;
                    if (!z9) {
                    }
                    while (r5 < jArr2.length) {
                    }
                    sb2 = sb;
                    sb2.append(i5);
                    uri2 = uri;
                    if (uri2 != null) {
                    }
                    sb2.append(i4);
                    if (!z) {
                        sb2.append(str8);
                    }
                    str17 = Utilities.MD5(sb2.toString());
                    if (!z3) {
                        systemNotificationManager.deleteNotificationChannel(str15);
                        if (BuildVars.LOGS_ENABLED) {
                        }
                        str15 = null;
                    }
                    if (str15 == null) {
                    }
                    return str15;
                } else if (!z3 && !z9) {
                    int importance = notificationChannel.getImportance();
                    Uri sound = notificationChannel.getSound();
                    long[] vibrationPattern = notificationChannel.getVibrationPattern();
                    str13 = "_";
                    boolean shouldVibrate = notificationChannel.shouldVibrate();
                    if (shouldVibrate || vibrationPattern != null) {
                        z7 = shouldVibrate;
                        jArr3 = vibrationPattern;
                    } else {
                        z7 = shouldVibrate;
                        jArr3 = new long[]{0, 0};
                    }
                    int lightColor = notificationChannel.getLightColor();
                    if (jArr3 != null) {
                        for (long j4 : jArr3) {
                            sb3.append(j4);
                        }
                    }
                    sb3.append(lightColor);
                    if (sound != null) {
                        sb3.append(sound.toString());
                    }
                    sb3.append(importance);
                    if (!z && z8) {
                        sb3.append("secret");
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("current channel settings for " + string + " = " + ((Object) sb3) + " old = " + string3);
                    }
                    str17 = Utilities.MD5(sb3.toString());
                    sb3.setLength(0);
                    if (str17.equals(string3)) {
                        j3 = j;
                        str11 = "_s";
                        jArr2 = jArr;
                        i5 = i;
                        str8 = "secret";
                        str19 = string3;
                        sb = sb3;
                        str20 = string;
                        notificationsController = this;
                        z4 = true;
                        str12 = str24;
                        i4 = i2;
                        z5 = false;
                    } else {
                        if (importance == 0) {
                            SharedPreferences.Editor edit = notificationsSettings.edit();
                            if (z) {
                                if (!z2) {
                                    if (i3 == 3) {
                                        edit.putBoolean("EnableAllStories", false);
                                    } else if (i3 == 4) {
                                        edit.putBoolean("EnableReactionsMessages", true);
                                        edit.putBoolean("EnableReactionsStories", true);
                                    } else {
                                        edit.putInt(getGlobalNotificationsKey(i3), ConnectionsManager.DEFAULT_DATACENTER_ID);
                                    }
                                    updateServerNotificationsSettings(i3);
                                }
                                str11 = "_s";
                                str21 = str24;
                                str8 = "secret";
                                str19 = string3;
                                sb = sb3;
                                str20 = string;
                                i6 = lightColor;
                            } else {
                                i6 = lightColor;
                                if (i3 == 3) {
                                    edit.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + getSharedPrefKey(j, 0L), false);
                                } else {
                                    edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, 0L), 2);
                                }
                                str19 = string3;
                                sb = sb3;
                                str11 = "_s";
                                str20 = string;
                                str21 = str24;
                                str8 = "secret";
                                updateServerNotificationsSettings(j, 0L, true);
                            }
                            notificationsController = this;
                            j3 = j;
                            i4 = i2;
                            str12 = str21;
                            editor = edit;
                            z4 = true;
                            z5 = true;
                        } else {
                            str11 = "_s";
                            str8 = "secret";
                            str19 = string3;
                            sb = sb3;
                            str20 = string;
                            i6 = lightColor;
                            z4 = true;
                            str12 = str24;
                            i4 = i2;
                            if (importance != i4) {
                                if (z2) {
                                    j3 = j;
                                    editor = null;
                                } else {
                                    editor = notificationsSettings.edit();
                                    int i8 = (importance == 4 || importance == 5) ? 1 : importance == 1 ? 4 : importance == 2 ? 5 : 0;
                                    if (z) {
                                        if (i3 == 3) {
                                            editor.putBoolean("EnableAllStories", true);
                                        } else if (i3 == 4) {
                                            editor.putBoolean("EnableReactionsMessages", true);
                                            editor.putBoolean("EnableReactionsStories", true);
                                        } else {
                                            editor.putInt(getGlobalNotificationsKey(i3), 0);
                                        }
                                        if (i3 == 2) {
                                            editor.putInt("priority_channel", i8);
                                        } else if (i3 == 0) {
                                            editor.putInt("priority_group", i8);
                                        } else if (i3 == 3) {
                                            editor.putInt("priority_stories", i8);
                                        } else if (i3 == 4 || i3 == 5) {
                                            editor.putInt("priority_react", i8);
                                        } else {
                                            editor.putInt("priority_messages", i8);
                                        }
                                        j3 = j;
                                    } else if (i3 == 3) {
                                        editor.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + j, true);
                                        j3 = j;
                                    } else {
                                        j3 = j;
                                        editor.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + j3, 0);
                                        editor.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + j3);
                                        editor.putInt("priority_" + j3, i8);
                                    }
                                }
                                z5 = true;
                            } else {
                                j3 = j;
                                editor = null;
                                z5 = false;
                            }
                            notificationsController = this;
                        }
                        jArr2 = jArr;
                        boolean z10 = z7;
                        if ((notificationsController.isEmptyVibration(jArr2) ^ z4) != z10) {
                            if (!z2) {
                                if (editor == null) {
                                    editor = notificationsSettings.edit();
                                }
                                if (!z) {
                                    editor.putInt("vibrate_" + j3, z10 ? 0 : 2);
                                } else if (i3 == 2) {
                                    editor.putInt("vibrate_channel", z10 ? 0 : 2);
                                } else if (i3 == 0) {
                                    editor.putInt("vibrate_group", z10 ? 0 : 2);
                                } else if (i3 == 3) {
                                    editor.putInt("vibrate_stories", z10 ? 0 : 2);
                                } else if (i3 == 4 || i3 == 5) {
                                    editor.putInt("vibrate_react", z10 ? 0 : 2);
                                } else {
                                    editor.putInt("vibrate_messages", z10 ? 0 : 2);
                                }
                            }
                            jArr2 = jArr3;
                            i7 = i6;
                            z5 = true;
                            i5 = i;
                        } else {
                            i5 = i;
                            i7 = i6;
                        }
                        if (i7 != i5) {
                            if (!z2) {
                                if (editor == null) {
                                    editor = notificationsSettings.edit();
                                }
                                if (!z) {
                                    editor.putInt("color_" + j3, i7);
                                } else if (i3 == 2) {
                                    editor.putInt("ChannelLed", i7);
                                } else if (i3 == 0) {
                                    editor.putInt("GroupLed", i7);
                                } else if (i3 == 3) {
                                    editor.putInt("StoriesLed", i7);
                                } else if (i3 == 5 || i3 == 4) {
                                    editor.putInt("ReactionsLed", i7);
                                } else {
                                    editor.putInt("MessagesLed", i7);
                                }
                            }
                            i5 = i7;
                            z5 = true;
                        }
                        if (editor != null) {
                            editor.commit();
                        }
                    }
                    str15 = str20;
                    str16 = str19;
                    if (z5 || str17 == null) {
                        str18 = str11;
                        if (!z9 || str17 == null || !z2 || !z) {
                            for (long j5 : jArr2) {
                                sb.append(j5);
                            }
                            sb2 = sb;
                            sb2.append(i5);
                            uri2 = uri;
                            if (uri2 != null) {
                                sb2.append(uri.toString());
                            }
                            sb2.append(i4);
                            if (!z && z8) {
                                sb2.append(str8);
                            }
                            str17 = Utilities.MD5(sb2.toString());
                            if (!z3 && str15 != null && (z9 || !str16.equals(str17))) {
                                try {
                                    systemNotificationManager.deleteNotificationChannel(str15);
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d("delete channel by settings change " + str15);
                                }
                                str15 = null;
                            }
                            if (str15 == null) {
                                str15 = z ? notificationsController.currentAccount + "channel_" + str12 + str13 + Utilities.random.nextLong() : notificationsController.currentAccount + "channel_" + j3 + str13 + Utilities.random.nextLong();
                                if (z8) {
                                    str7 = LocaleController.getString("SecretChatName", R.string.SecretChatName);
                                }
                                NotificationChannel notificationChannel2 = new NotificationChannel(str15, str7, i4);
                                notificationChannel2.setGroup(str14);
                                if (i5 != 0) {
                                    notificationChannel2.enableLights(z4);
                                    notificationChannel2.setLightColor(i5);
                                    z6 = false;
                                } else {
                                    z6 = false;
                                    notificationChannel2.enableLights(false);
                                }
                                if (!notificationsController.isEmptyVibration(jArr2)) {
                                    notificationChannel2.enableVibration(z4);
                                    if (jArr2.length > 0) {
                                        notificationChannel2.setVibrationPattern(jArr2);
                                    }
                                } else {
                                    notificationChannel2.enableVibration(z6);
                                }
                                AudioAttributes.Builder builder = new AudioAttributes.Builder();
                                builder.setContentType(4);
                                builder.setUsage(5);
                                if (uri2 != null) {
                                    notificationChannel2.setSound(uri2, builder.build());
                                } else {
                                    notificationChannel2.setSound(null, builder.build());
                                }
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d("create new channel " + str15);
                                }
                                notificationsController.lastNotificationChannelCreateTime = SystemClock.elapsedRealtime();
                                systemNotificationManager.createNotificationChannel(notificationChannel2);
                                notificationsSettings.edit().putString(str12, str15).putString(str12 + str18, str17).commit();
                            }
                            return str15;
                        }
                    } else {
                        SharedPreferences.Editor putString = notificationsSettings.edit().putString(str12, str15);
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(str12);
                        str18 = str11;
                        sb5.append(str18);
                        putString.putString(sb5.toString(), str17).commit();
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("change edited channel " + str15);
                        }
                    }
                    uri2 = uri;
                    if (str15 == null) {
                    }
                    return str15;
                } else {
                    str11 = "_s";
                    str8 = "secret";
                    str9 = string3;
                    sb = sb3;
                    str10 = string;
                    notificationsController = this;
                    i4 = i2;
                    str13 = "_";
                    z4 = true;
                    str12 = str24;
                }
            } else {
                str8 = "secret";
                str9 = string3;
                sb = sb3;
                str10 = string;
                str11 = "_s";
                str12 = str24;
                notificationsController = this;
                i4 = i2;
                str13 = "_";
                str14 = str5;
                z4 = true;
            }
            i5 = i;
            j3 = j;
            jArr2 = jArr;
            str15 = str10;
            str16 = str9;
            z5 = false;
            str17 = null;
            if (z5) {
            }
            str18 = str11;
            if (!z9) {
            }
            while (r5 < jArr2.length) {
            }
            sb2 = sb;
            sb2.append(i5);
            uri2 = uri;
            if (uri2 != null) {
            }
            sb2.append(i4);
            if (!z) {
            }
            str17 = Utilities.MD5(sb2.toString());
            if (!z3) {
            }
            if (str15 == null) {
            }
            return str15;
        } else {
            formatString = z2 ? LocaleController.formatString("NotificationsChatInApp", R.string.NotificationsChatInApp, str) : str;
            StringBuilder sb6 = new StringBuilder();
            sb6.append(z2 ? "org.telegram.keyia" : "org.telegram.key");
            sb6.append(j);
            sb6.append("_");
            sb6.append(j2);
            str22 = sb6.toString();
        }
        str7 = formatString;
        String str242 = str22 + "_" + MD5;
        string = notificationsSettings.getString(str242, null);
        String string32 = notificationsSettings.getString(str242 + "_s", null);
        StringBuilder sb32 = new StringBuilder();
        if (string == null) {
        }
        i5 = i;
        j3 = j;
        jArr2 = jArr;
        str15 = str10;
        str16 = str9;
        z5 = false;
        str17 = null;
        if (z5) {
        }
        str18 = str11;
        if (!z9) {
        }
        while (r5 < jArr2.length) {
        }
        sb2 = sb;
        sb2.append(i5);
        uri2 = uri;
        if (uri2 != null) {
        }
        sb2.append(i4);
        if (!z) {
        }
        str17 = Utilities.MD5(sb2.toString());
        if (!z3) {
        }
        if (str15 == null) {
        }
        return str15;
    }

    /* JADX WARN: Code restructure failed: missing block: B:145:0x034f, code lost:
        if (r12 == 0) goto L572;
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x0351, code lost:
        r3 = org.telegram.messenger.LocaleController.getString("NotificationHiddenChatName", org.telegram.messenger.R.string.NotificationHiddenChatName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x035a, code lost:
        r3 = org.telegram.messenger.LocaleController.getString("NotificationHiddenName", org.telegram.messenger.R.string.NotificationHiddenName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:501:0x0b8a, code lost:
        if (android.os.Build.VERSION.SDK_INT >= 26) goto L409;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:132:0x032c  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x032e  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x036e A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:158:0x0381 A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:161:0x038c A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:179:0x044d  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x0470 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:211:0x0541 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:212:0x0542  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x05b6  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x0677  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x0685 A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:261:0x06a7  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x06b5  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x06b8  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x06cf A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:278:0x074e  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x075e A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:286:0x076b  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x07f7  */
    /* JADX WARN: Removed duplicated region for block: B:318:0x0870 A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:319:0x087d A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:322:0x08a3  */
    /* JADX WARN: Removed duplicated region for block: B:323:0x08a5  */
    /* JADX WARN: Removed duplicated region for block: B:327:0x08ad  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x08b0  */
    /* JADX WARN: Removed duplicated region for block: B:331:0x08b8 A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:336:0x08ca A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:340:0x08d2 A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:344:0x08e0  */
    /* JADX WARN: Removed duplicated region for block: B:350:0x08ec A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:363:0x0910  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x0927  */
    /* JADX WARN: Removed duplicated region for block: B:375:0x092c  */
    /* JADX WARN: Removed duplicated region for block: B:378:0x095f A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:380:0x0971 A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:434:0x0a85 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:443:0x0ac5 A[Catch: all -> 0x0ae3, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:446:0x0ad0 A[Catch: all -> 0x0ae3, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:447:0x0ad8  */
    /* JADX WARN: Removed duplicated region for block: B:454:0x0aea A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:455:0x0af0  */
    /* JADX WARN: Removed duplicated region for block: B:474:0x0b47  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0119 A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0125 A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:544:0x0c96 A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:545:0x0ca0  */
    /* JADX WARN: Removed duplicated region for block: B:548:0x0ca7 A[Catch: Exception -> 0x0e55, TryCatch #2 {Exception -> 0x0e55, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02dc, B:117:0x0300, B:119:0x0306, B:123:0x0312, B:125:0x031a, B:130:0x0322, B:134:0x032f, B:138:0x0339, B:150:0x036a, B:152:0x036e, B:161:0x038c, B:163:0x0393, B:165:0x039b, B:168:0x03c8, B:170:0x03d3, B:180:0x0458, B:185:0x0474, B:187:0x0494, B:189:0x04ae, B:191:0x04b2, B:196:0x04bc, B:197:0x04c2, B:201:0x04cf, B:202:0x04e3, B:204:0x04e8, B:205:0x04fc, B:206:0x050f, B:207:0x0512, B:208:0x051e, B:226:0x05a3, B:237:0x05be, B:239:0x05d8, B:242:0x0611, B:244:0x061d, B:258:0x0685, B:263:0x06ab, B:267:0x06ba, B:269:0x06cf, B:271:0x070b, B:273:0x072d, B:275:0x073f, B:279:0x0758, B:281:0x075e, B:287:0x076d, B:289:0x077b, B:291:0x0790, B:329:0x08b2, B:331:0x08b8, B:340:0x08d2, B:342:0x08d8, B:350:0x08ec, B:353:0x08f6, B:356:0x08ff, B:372:0x0922, B:376:0x092d, B:378:0x095f, B:432:0x0a2d, B:435:0x0a87, B:437:0x0a8b, B:439:0x0a91, B:454:0x0aea, B:479:0x0b4e, B:507:0x0b97, B:516:0x0bd8, B:518:0x0be0, B:520:0x0be4, B:522:0x0bec, B:526:0x0bf5, B:544:0x0c96, B:548:0x0ca7, B:565:0x0d0a, B:567:0x0d10, B:569:0x0d14, B:571:0x0d1f, B:573:0x0d25, B:575:0x0d2f, B:577:0x0d40, B:579:0x0d4e, B:581:0x0d6f, B:582:0x0d74, B:584:0x0da3, B:585:0x0db6, B:589:0x0de5, B:591:0x0deb, B:593:0x0df3, B:595:0x0df9, B:597:0x0e0b, B:598:0x0e22, B:599:0x0e38, B:552:0x0cb9, B:559:0x0cda, B:562:0x0cf0, B:527:0x0c21, B:528:0x0c26, B:529:0x0c29, B:531:0x0c2f, B:534:0x0c3a, B:536:0x0c42, B:540:0x0c81, B:541:0x0c8a, B:510:0x0ba1, B:512:0x0ba9, B:514:0x0bd3, B:564:0x0cf9, B:489:0x0b62, B:494:0x0b70, B:497:0x0b79, B:500:0x0b84, B:456:0x0af2, B:458:0x0aff, B:380:0x0971, B:382:0x0975, B:383:0x097e, B:385:0x0986, B:386:0x0995, B:387:0x099c, B:389:0x09a2, B:393:0x09b1, B:396:0x09bd, B:397:0x09c5, B:399:0x09cb, B:402:0x09d0, B:404:0x09d9, B:407:0x09e1, B:409:0x09e5, B:411:0x09e9, B:413:0x09f1, B:417:0x09fb, B:419:0x0a01, B:421:0x0a05, B:423:0x0a0d, B:426:0x0a12, B:428:0x0a1d, B:430:0x0a23, B:290:0x0788, B:292:0x07ae, B:294:0x07be, B:296:0x07d3, B:295:0x07cb, B:303:0x0809, B:305:0x0813, B:310:0x082d, B:309:0x0827, B:316:0x085f, B:318:0x0870, B:320:0x0885, B:319:0x087d, B:272:0x0719, B:245:0x0634, B:247:0x0648, B:248:0x0654, B:250:0x0658, B:209:0x052c, B:214:0x0546, B:220:0x0588, B:223:0x058f, B:215:0x055a, B:217:0x055f, B:218:0x0573, B:174:0x03e4, B:177:0x03f1, B:178:0x0410, B:166:0x03a8, B:156:0x0376, B:158:0x0381, B:146:0x0351, B:147:0x035a, B:148:0x0361, B:121:0x030b, B:122:0x030e, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:365:0x0913, B:441:0x0aaa, B:443:0x0ac5, B:444:0x0acc, B:446:0x0ad0, B:448:0x0ad9, B:538:0x0c4c), top: B:608:0x002a, inners: #0, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:550:0x0cb6  */
    /* JADX WARN: Type inference failed for: r28v0 */
    /* JADX WARN: Type inference failed for: r28v1 */
    /* JADX WARN: Type inference failed for: r28v2, types: [android.net.Uri] */
    /* JADX WARN: Type inference failed for: r28v3, types: [android.net.Uri] */
    /* JADX WARN: Type inference failed for: r4v13 */
    /* JADX WARN: Type inference failed for: r5v115, types: [org.telegram.messenger.MessageObject] */
    /* JADX WARN: Type inference failed for: r8v111 */
    /* JADX WARN: Type inference failed for: r8v80 */
    /* JADX WARN: Type inference failed for: r8v81 */
    /* JADX WARN: Type inference failed for: r8v82 */
    /* JADX WARN: Type inference failed for: r8v83 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showOrUpdateNotification(boolean z) {
        MessageObject messageObject;
        Bitmap bitmap;
        long j;
        long j2;
        TLRPC$Chat tLRPC$Chat;
        boolean z2;
        boolean z3;
        String userName;
        boolean z4;
        boolean z5;
        String string;
        boolean z6;
        String str;
        String string2;
        boolean z7;
        String str2;
        TLRPC$User tLRPC$User;
        SharedPreferences sharedPreferences;
        long j3;
        String str3;
        MessageObject messageObject2;
        String stringForMessage;
        String str4;
        boolean z8;
        String str5;
        boolean z9;
        boolean z10;
        long j4;
        SharedPreferences sharedPreferences2;
        String str6;
        String str7;
        boolean z11;
        long j5;
        boolean z12;
        boolean z13;
        CharSequence charSequence;
        long j6;
        String str8;
        int i;
        Integer num;
        int i2;
        boolean z14;
        CharSequence charSequence2;
        Integer num2;
        long j7;
        long j8;
        String string3;
        boolean z15;
        int i3;
        int i4;
        boolean z16;
        int i5;
        int i6;
        int i7;
        int i8;
        String str9;
        int i9;
        boolean z17;
        int i10;
        boolean z18;
        boolean z19;
        int i11;
        TLRPC$User tLRPC$User2;
        TLRPC$FileLocation tLRPC$FileLocation;
        TLRPC$FileLocation tLRPC$FileLocation2;
        int i12;
        long[] jArr;
        boolean z20;
        int i13;
        int i14;
        long[] jArr2;
        long[] jArr3;
        int i15;
        long j9;
        boolean z21;
        int i16;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList;
        int i17;
        long[] jArr4;
        int i18;
        long j10;
        MessageObject messageObject3;
        Uri uri;
        long[] jArr5;
        long[] jArr6;
        String str10;
        int ringerMode;
        boolean z22;
        boolean z23;
        boolean z24;
        String propertyString;
        boolean z25;
        int i19;
        int i20;
        boolean z26;
        int i21;
        String formatPluralString;
        if (!getUserConfig().isClientActivated() || ((this.pushMessages.isEmpty() && this.storyPushMessages.isEmpty()) || (!SharedConfig.showNotificationsForAllAccounts && this.currentAccount != UserConfig.selectedAccount))) {
            dismissNotification();
            return;
        }
        try {
            getConnectionsManager().resumeNetworkMaybe();
            long j11 = 0;
            StoryNotification storyNotification = null;
            for (int i22 = 0; i22 < this.pushMessages.size(); i22++) {
                MessageObject messageObject4 = this.pushMessages.get(i22);
                int i23 = messageObject4.messageOwner.date;
                if (j11 < i23) {
                    j11 = i23;
                    storyNotification = messageObject4;
                }
            }
            for (int i24 = 0; i24 < this.storyPushMessages.size(); i24++) {
                StoryNotification storyNotification2 = this.storyPushMessages.get(i24);
                long j12 = storyNotification2.date;
                if (j11 < j12 / 1000) {
                    storyNotification = storyNotification2;
                    j11 = j12 / 1000;
                }
            }
            if (storyNotification == null) {
                return;
            }
            if (storyNotification instanceof StoryNotification) {
                StoryNotification storyNotification3 = storyNotification;
                TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
                tLRPC$TL_message.date = (int) (System.currentTimeMillis() / 1000);
                boolean z27 = false;
                int i25 = 0;
                for (int i26 = 0; i26 < this.storyPushMessages.size(); i26++) {
                    z27 |= this.storyPushMessages.get(i26).hidden;
                    tLRPC$TL_message.date = Math.min(tLRPC$TL_message.date, (int) (this.storyPushMessages.get(i26).date / 1000));
                    i25 += this.storyPushMessages.get(i26).dateByIds.size();
                }
                TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                long j13 = storyNotification3.dialogId;
                tLRPC$TL_peerUser.user_id = j13;
                tLRPC$TL_message.dialog_id = j13;
                tLRPC$TL_message.peer_id = tLRPC$TL_peerUser;
                ArrayList<String> arrayList2 = new ArrayList<>();
                ArrayList<Object> arrayList3 = new ArrayList<>();
                parseStoryPushes(arrayList2, arrayList3);
                Bitmap loadMultipleAvatars = SharedConfig.getDevicePerformanceClass() >= 1 ? loadMultipleAvatars(arrayList3) : null;
                if (!z27 && this.storyPushMessages.size() < 2 && !arrayList2.isEmpty()) {
                    formatPluralString = arrayList2.get(0);
                    i21 = 0;
                    if (!z27) {
                        tLRPC$TL_message.message = LocaleController.formatPluralString("StoryNotificationHidden", i25, new Object[i21]);
                    } else if (arrayList2.isEmpty()) {
                        tLRPC$TL_message.message = "";
                    } else if (arrayList2.size() == 1) {
                        if (i25 == 1) {
                            tLRPC$TL_message.message = LocaleController.getString("StoryNotificationSingle");
                        } else {
                            tLRPC$TL_message.message = LocaleController.formatPluralString("StoryNotification1", i25, arrayList2.get(0));
                        }
                    } else if (arrayList2.size() == 2) {
                        tLRPC$TL_message.message = LocaleController.formatString(R.string.StoryNotification2, arrayList2.get(0), arrayList2.get(1));
                    } else if (arrayList2.size() == 3 && this.storyPushMessages.size() == 3) {
                        tLRPC$TL_message.message = LocaleController.formatString(R.string.StoryNotification3, cutLastName(arrayList2.get(0)), cutLastName(arrayList2.get(1)), cutLastName(arrayList2.get(2)));
                    } else {
                        tLRPC$TL_message.message = LocaleController.formatPluralString("StoryNotification4", this.storyPushMessages.size() - 2, cutLastName(arrayList2.get(0)), cutLastName(arrayList2.get(1)));
                    }
                    MessageObject messageObject5 = new MessageObject(this.currentAccount, tLRPC$TL_message, tLRPC$TL_message.message, formatPluralString, formatPluralString, false, false, false, false);
                    messageObject5.isStoryPush = true;
                    messageObject = messageObject5;
                    bitmap = loadMultipleAvatars;
                }
                i21 = 0;
                formatPluralString = LocaleController.formatPluralString("Stories", i25, new Object[0]);
                if (!z27) {
                }
                MessageObject messageObject52 = new MessageObject(this.currentAccount, tLRPC$TL_message, tLRPC$TL_message.message, formatPluralString, formatPluralString, false, false, false, false);
                messageObject52.isStoryPush = true;
                messageObject = messageObject52;
                bitmap = loadMultipleAvatars;
            } else {
                messageObject = this.pushMessages.get(0);
                bitmap = null;
            }
            SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
            int i27 = notificationsSettings.getInt("dismissDate", 0);
            if (!messageObject.isStoryPush && messageObject.messageOwner.date <= i27) {
                dismissNotification();
                return;
            }
            long dialogId = messageObject.getDialogId();
            long topicId = MessageObject.getTopicId(this.currentAccount, messageObject.messageOwner, getMessagesController().isForum(messageObject));
            boolean z28 = messageObject.isStoryPush;
            long fromChatId = messageObject.messageOwner.mentioned ? messageObject.getFromChatId() : dialogId;
            messageObject.getId();
            TLRPC$Peer tLRPC$Peer = messageObject.messageOwner.peer_id;
            long j14 = tLRPC$Peer.chat_id;
            if (j14 == 0) {
                j14 = tLRPC$Peer.channel_id;
            }
            long j15 = tLRPC$Peer.user_id;
            if (messageObject.isFromUser() && (j15 == 0 || j15 == getUserConfig().getClientUserId())) {
                j15 = messageObject.messageOwner.from_id.user_id;
            }
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(j15));
            if (j14 != 0) {
                tLRPC$Chat = getMessagesController().getChat(Long.valueOf(j14));
                if (tLRPC$Chat == null && messageObject.isFcmMessage()) {
                    z26 = messageObject.localChannel;
                } else {
                    z26 = ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup;
                }
                j = j15;
                z2 = z26;
                j2 = topicId;
            } else {
                j = j15;
                j2 = topicId;
                tLRPC$Chat = null;
                z2 = false;
            }
            int i28 = i27;
            TLRPC$Chat tLRPC$Chat2 = tLRPC$Chat;
            Bitmap bitmap2 = bitmap;
            int notifyOverride = getNotifyOverride(notificationsSettings, fromChatId, j2);
            if (notifyOverride == -1) {
                Boolean valueOf = Boolean.valueOf(z2);
                boolean z29 = messageObject.isReactionPush;
                z3 = isGlobalNotificationsEnabled(dialogId, valueOf, z29, z29);
            } else {
                z3 = notifyOverride != 2;
            }
            if (((j14 != 0 && tLRPC$Chat2 == null) || user == null) && messageObject.isFcmMessage()) {
                userName = messageObject.localName;
            } else if (tLRPC$Chat2 != null) {
                userName = tLRPC$Chat2.title;
            } else {
                userName = UserObject.getUserName(user);
            }
            String str11 = userName;
            if (!AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
                z4 = false;
                z5 = "samsung".equalsIgnoreCase(Build.MANUFACTURER);
                if (!DialogObject.isEncryptedDialog(dialogId) && ((!z5 || this.pushDialogs.size() <= 1) && !z4)) {
                    string = str11;
                    z6 = true;
                    if (!messageObject.isReactionPush && !messageObject.isStoryReactionPush) {
                        str = string;
                        string2 = str;
                        if (z5) {
                            if (UserConfig.getActivatedAccountsCount() <= 1) {
                                str3 = "";
                            } else if (this.pushDialogs.size() == 1) {
                                str3 = UserObject.getFirstName(getUserConfig().getCurrentUser());
                            } else {
                                str3 = UserObject.getFirstName(getUserConfig().getCurrentUser()) + "・";
                            }
                            str2 = "currentAccount";
                            if (this.pushDialogs.size() == 1 && Build.VERSION.SDK_INT >= 23) {
                                z7 = z3;
                                tLRPC$User = user;
                                sharedPreferences = notificationsSettings;
                                j3 = j14;
                            }
                            tLRPC$User = user;
                            if (this.pushDialogs.size() == 1) {
                                StringBuilder sb = new StringBuilder();
                                sb.append(str3);
                                j3 = j14;
                                sb.append(LocaleController.formatPluralString("NewMessages", this.total_unread_count, new Object[0]));
                                str3 = sb.toString();
                                z7 = z3;
                                sharedPreferences = notificationsSettings;
                            } else {
                                j3 = j14;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(str3);
                                z7 = z3;
                                sharedPreferences = notificationsSettings;
                                sb2.append(LocaleController.formatString("NotificationMessagesPeopleDisplayOrder", R.string.NotificationMessagesPeopleDisplayOrder, LocaleController.formatPluralString("NewMessages", this.total_unread_count, new Object[0]), LocaleController.formatPluralString("FromChats", this.pushDialogs.size(), new Object[0])));
                                str3 = sb2.toString();
                            }
                        } else {
                            z7 = z3;
                            str2 = "currentAccount";
                            tLRPC$User = user;
                            sharedPreferences = notificationsSettings;
                            j3 = j14;
                            str3 = "";
                        }
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                        if (this.pushMessages.size() > 1 || !z5) {
                            MessageObject messageObject6 = messageObject;
                            boolean[] zArr = new boolean[1];
                            String str12 = str3;
                            messageObject2 = messageObject6;
                            stringForMessage = getStringForMessage(messageObject2, false, zArr, null);
                            boolean isSilentMessage = isSilentMessage(messageObject2);
                            if (stringForMessage == null) {
                                return;
                            }
                            if (!z6) {
                                str4 = stringForMessage;
                            } else if (tLRPC$Chat2 != null) {
                                str4 = stringForMessage.replace(" @ " + string2, "");
                            } else if (zArr[0]) {
                                str4 = stringForMessage.replace(string2 + ": ", "");
                            } else {
                                str4 = stringForMessage.replace(string2 + " ", "");
                            }
                            builder.setContentText(str4);
                            if (!z5) {
                                str12 = str4;
                            }
                            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(str4));
                            z8 = isSilentMessage;
                            str5 = stringForMessage;
                            str3 = str12;
                        } else {
                            builder.setContentText(str3);
                            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                            inboxStyle.setBigContentTitle(string2);
                            int min = Math.min(10, this.pushMessages.size());
                            boolean[] zArr2 = new boolean[1];
                            MessageObject messageObject7 = messageObject;
                            boolean z30 = 2;
                            int i29 = 0;
                            String str13 = null;
                            while (i29 < min) {
                                int i30 = min;
                                MessageObject messageObject8 = this.pushMessages.get(i29);
                                String str14 = str3;
                                NotificationCompat.Builder builder2 = builder;
                                int i31 = i29;
                                String stringForMessage2 = getStringForMessage(messageObject8, false, zArr2, null);
                                int i32 = i28;
                                if (stringForMessage2 != null && (messageObject8.isStoryPush || messageObject8.messageOwner.date > i32)) {
                                    z30 = z30;
                                    if (z30 == 2) {
                                        str13 = stringForMessage2;
                                        z30 = isSilentMessage(messageObject8);
                                    }
                                    if (this.pushDialogs.size() == 1 && z6) {
                                        if (tLRPC$Chat2 != null) {
                                            stringForMessage2 = stringForMessage2.replace(" @ " + string2, "");
                                        } else if (zArr2[0]) {
                                            stringForMessage2 = stringForMessage2.replace(string2 + ": ", "");
                                        } else {
                                            stringForMessage2 = stringForMessage2.replace(string2 + " ", "");
                                        }
                                    }
                                    inboxStyle.addLine(stringForMessage2);
                                }
                                i29 = i31 + 1;
                                i28 = i32;
                                min = i30;
                                str3 = str14;
                                builder = builder2;
                                z30 = z30;
                            }
                            inboxStyle.setSummaryText(str3);
                            builder.setStyle(inboxStyle);
                            str5 = str13;
                            messageObject2 = messageObject7;
                            z8 = z30;
                        }
                        if (z && z7 && !MediaController.getInstance().isRecordingAudio() && !z8) {
                            z9 = false;
                            if (z9) {
                                j4 = dialogId;
                                if (j4 != fromChatId || tLRPC$Chat2 == null) {
                                    str6 = str3;
                                    z10 = z9;
                                    str7 = str5;
                                    sharedPreferences2 = sharedPreferences;
                                } else {
                                    sharedPreferences2 = sharedPreferences;
                                    if (sharedPreferences2.getBoolean(NotificationsSettingsFacade.PROPERTY_CUSTOM + j4, false)) {
                                        int i33 = sharedPreferences2.getInt("smart_max_count_" + j4, 2);
                                        i19 = sharedPreferences2.getInt("smart_delay_" + j4, 180);
                                        i20 = i33;
                                    } else {
                                        i19 = 180;
                                        i20 = 2;
                                    }
                                    if (i20 != 0) {
                                        z10 = z9;
                                        Point point = this.smartNotificationsDialogs.get(j4);
                                        if (point == null) {
                                            str6 = str3;
                                            this.smartNotificationsDialogs.put(j4, new Point(1, (int) (SystemClock.elapsedRealtime() / 1000)));
                                        } else {
                                            str6 = str3;
                                            str7 = str5;
                                            if (point.y + i19 < SystemClock.elapsedRealtime() / 1000) {
                                                point.set(1, (int) (SystemClock.elapsedRealtime() / 1000));
                                            } else {
                                                int i34 = point.x;
                                                if (i34 < i20) {
                                                    point.set(i34 + 1, (int) (SystemClock.elapsedRealtime() / 1000));
                                                } else {
                                                    z11 = true;
                                                    if (z11) {
                                                        StringBuilder sb3 = new StringBuilder();
                                                        sb3.append("sound_enabled_");
                                                        j5 = j2;
                                                        sb3.append(getSharedPrefKey(j4, j5));
                                                        if (!sharedPreferences2.getBoolean(sb3.toString(), true)) {
                                                            z12 = true;
                                                            String path = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                                                            z13 = !ApplicationLoader.mainInterfacePaused;
                                                            getSharedPrefKey(j4, j5);
                                                            if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j4, j5, false)) {
                                                                int property = this.dialogsNotificationsFacade.getProperty("vibrate_", j4, j5, 0);
                                                                int property2 = this.dialogsNotificationsFacade.getProperty("priority_", j4, j5, 3);
                                                                long property3 = this.dialogsNotificationsFacade.getProperty("sound_document_id_", j4, j5, 0L);
                                                                if (property3 != 0) {
                                                                    charSequence = "";
                                                                    propertyString = getMediaDataController().ringtoneDataStore.getSoundPath(property3);
                                                                    z25 = true;
                                                                } else {
                                                                    charSequence = "";
                                                                    propertyString = this.dialogsNotificationsFacade.getPropertyString("sound_path_", j4, j5, null);
                                                                    z25 = false;
                                                                }
                                                                int property4 = this.dialogsNotificationsFacade.getProperty("color_", j4, j5, 0);
                                                                num = property4 != 0 ? Integer.valueOf(property4) : null;
                                                                z14 = z25;
                                                                j6 = j5;
                                                                i2 = property;
                                                                i = property2;
                                                                str8 = propertyString;
                                                            } else {
                                                                charSequence = "";
                                                                j6 = j5;
                                                                str8 = 0;
                                                                i = 3;
                                                                num = null;
                                                                i2 = 0;
                                                                z14 = false;
                                                            }
                                                            boolean z31 = z8;
                                                            if (!messageObject2.isReactionPush && !messageObject2.isStoryReactionPush) {
                                                                num2 = num;
                                                                if (j3 == 0) {
                                                                    if (z2) {
                                                                        charSequence2 = string2;
                                                                        j7 = j4;
                                                                        long j16 = sharedPreferences2.getLong("ChannelSoundDocId", 0L);
                                                                        if (j16 != 0) {
                                                                            string3 = getMediaDataController().ringtoneDataStore.getSoundPath(j16);
                                                                            z24 = true;
                                                                        } else {
                                                                            string3 = sharedPreferences2.getString("ChannelSoundPath", path);
                                                                            z24 = false;
                                                                        }
                                                                        i3 = sharedPreferences2.getInt("vibrate_channel", 0);
                                                                        i4 = sharedPreferences2.getInt("priority_channel", 1);
                                                                        z16 = z24;
                                                                        i5 = sharedPreferences2.getInt("ChannelLed", -16776961);
                                                                        i7 = 4;
                                                                        i6 = 2;
                                                                    } else {
                                                                        charSequence2 = string2;
                                                                        j7 = j4;
                                                                        long j17 = sharedPreferences2.getLong("GroupSoundDocId", 0L);
                                                                        if (j17 != 0) {
                                                                            string3 = getMediaDataController().ringtoneDataStore.getSoundPath(j17);
                                                                            z23 = true;
                                                                        } else {
                                                                            string3 = sharedPreferences2.getString("GroupSoundPath", path);
                                                                            z23 = false;
                                                                        }
                                                                        i3 = sharedPreferences2.getInt("vibrate_group", 0);
                                                                        i4 = sharedPreferences2.getInt("priority_group", 1);
                                                                        z16 = z23;
                                                                        i5 = sharedPreferences2.getInt("GroupLed", -16776961);
                                                                        i7 = 4;
                                                                        i6 = 0;
                                                                    }
                                                                    String str15 = string3;
                                                                    i8 = i5;
                                                                    str9 = str15;
                                                                    if (i3 != i7) {
                                                                        z17 = true;
                                                                        i9 = 0;
                                                                    } else {
                                                                        i9 = i3;
                                                                        z17 = false;
                                                                    }
                                                                    if (!TextUtils.isEmpty(str8) || TextUtils.equals(str9, str8)) {
                                                                        str8 = str9;
                                                                        z14 = z16;
                                                                        i10 = 3;
                                                                        z18 = true;
                                                                    } else {
                                                                        i10 = 3;
                                                                        z18 = false;
                                                                    }
                                                                    if (i != i10 || i4 == i) {
                                                                        i = i4;
                                                                    } else {
                                                                        z18 = false;
                                                                    }
                                                                    if (num2 != null && num2.intValue() != i8) {
                                                                        i8 = num2.intValue();
                                                                        z18 = false;
                                                                    }
                                                                    if (i2 != 0 || i2 == 4 || i2 == i9) {
                                                                        z19 = z18;
                                                                    } else {
                                                                        i9 = i2;
                                                                        z19 = false;
                                                                    }
                                                                    if (z13) {
                                                                        if (!sharedPreferences2.getBoolean("EnableInAppSounds", true)) {
                                                                            str8 = null;
                                                                        }
                                                                        if (!sharedPreferences2.getBoolean("EnableInAppVibrate", true)) {
                                                                            i9 = 2;
                                                                        }
                                                                        if (!sharedPreferences2.getBoolean("EnableInAppPriority", false)) {
                                                                            i = 0;
                                                                        } else if (i == 2) {
                                                                            i = 1;
                                                                        }
                                                                    }
                                                                    if (z17 && i9 != 2) {
                                                                        try {
                                                                            ringerMode = audioManager.getRingerMode();
                                                                            if (ringerMode != 0 && ringerMode != 1) {
                                                                                i9 = 2;
                                                                            }
                                                                        } catch (Exception e) {
                                                                            FileLog.e(e);
                                                                        }
                                                                    }
                                                                    if (z12) {
                                                                        i11 = i8;
                                                                    } else {
                                                                        str8 = null;
                                                                        i = 0;
                                                                        i11 = 0;
                                                                        i9 = 0;
                                                                    }
                                                                    Intent intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                                    StringBuilder sb4 = new StringBuilder();
                                                                    sb4.append("com.tmessages.openchat");
                                                                    boolean z32 = z19;
                                                                    sb4.append(Math.random());
                                                                    sb4.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                                    intent.setAction(sb4.toString());
                                                                    intent.setFlags(ConnectionsManager.FileTypeFile);
                                                                    if (!messageObject2.isStoryReactionPush) {
                                                                        intent.putExtra("storyId", Math.abs(messageObject2.getId()));
                                                                    } else if (messageObject2.isStoryPush) {
                                                                        long[] jArr7 = new long[this.storyPushMessages.size()];
                                                                        for (int i35 = 0; i35 < this.storyPushMessages.size(); i35++) {
                                                                            jArr7[i35] = this.storyPushMessages.get(i35).dialogId;
                                                                        }
                                                                        intent.putExtra("storyDialogIds", jArr7);
                                                                    } else {
                                                                        if (!DialogObject.isEncryptedDialog(j7)) {
                                                                            if (this.pushDialogs.size() == 1) {
                                                                                if (j3 != 0) {
                                                                                    intent.putExtra("chatId", j3);
                                                                                } else if (j != 0) {
                                                                                    intent.putExtra("userId", j);
                                                                                }
                                                                            }
                                                                            if (!AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter && this.pushDialogs.size() == 1 && Build.VERSION.SDK_INT < 28) {
                                                                                if (tLRPC$Chat2 != null) {
                                                                                    TLRPC$ChatPhoto tLRPC$ChatPhoto = tLRPC$Chat2.photo;
                                                                                    if (tLRPC$ChatPhoto != null && (tLRPC$FileLocation2 = tLRPC$ChatPhoto.photo_small) != null && tLRPC$FileLocation2.volume_id != 0 && tLRPC$FileLocation2.local_id != 0) {
                                                                                        tLRPC$FileLocation = tLRPC$FileLocation2;
                                                                                        tLRPC$User2 = tLRPC$User;
                                                                                    }
                                                                                } else if (tLRPC$User != null) {
                                                                                    tLRPC$User2 = tLRPC$User;
                                                                                    TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = tLRPC$User2.photo;
                                                                                    if (tLRPC$UserProfilePhoto != null && (tLRPC$FileLocation = tLRPC$UserProfilePhoto.photo_small) != null && tLRPC$FileLocation.volume_id != 0 && tLRPC$FileLocation.local_id != 0) {
                                                                                    }
                                                                                }
                                                                                String str16 = str2;
                                                                                intent.putExtra(str16, this.currentAccount);
                                                                                int i36 = i9;
                                                                                int i37 = i11;
                                                                                builder.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject2.messageOwner.date * 1000).setColor(-15618822);
                                                                                builder.setCategory("msg");
                                                                                if (tLRPC$Chat2 == null && tLRPC$User2 != null && (str10 = tLRPC$User2.phone) != null && str10.length() > 0) {
                                                                                    builder.addPerson("tel:+" + tLRPC$User2.phone);
                                                                                }
                                                                                Intent intent2 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                                                intent2.putExtra("messageDate", messageObject2.messageOwner.date);
                                                                                intent2.putExtra(str16, this.currentAccount);
                                                                                if (messageObject2.isStoryPush) {
                                                                                    intent2.putExtra("story", true);
                                                                                }
                                                                                if (messageObject2.isStoryReactionPush) {
                                                                                    i12 = 1;
                                                                                } else {
                                                                                    i12 = 1;
                                                                                    intent2.putExtra("storyReaction", true);
                                                                                }
                                                                                builder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent2, 167772160));
                                                                                if (bitmap2 == null) {
                                                                                    builder.setLargeIcon(bitmap2);
                                                                                } else if (tLRPC$FileLocation != null) {
                                                                                    jArr = null;
                                                                                    BitmapDrawable imageFromMemory = ImageLoader.getInstance().getImageFromMemory(tLRPC$FileLocation, null, "50_50");
                                                                                    if (imageFromMemory != null) {
                                                                                        builder.setLargeIcon(imageFromMemory.getBitmap());
                                                                                    } else {
                                                                                        try {
                                                                                            File pathToAttach = getFileLoader().getPathToAttach(tLRPC$FileLocation, true);
                                                                                            if (pathToAttach.exists()) {
                                                                                                float dp = 160.0f / AndroidUtilities.dp(50.0f);
                                                                                                BitmapFactory.Options options = new BitmapFactory.Options();
                                                                                                options.inSampleSize = dp < 1.0f ? 1 : (int) dp;
                                                                                                Bitmap decodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options);
                                                                                                if (decodeFile != null) {
                                                                                                    builder.setLargeIcon(decodeFile);
                                                                                                }
                                                                                            }
                                                                                        } catch (Throwable unused) {
                                                                                        }
                                                                                    }
                                                                                    if (z || z31) {
                                                                                        builder.setPriority(-1);
                                                                                    } else if (i == 0) {
                                                                                        builder.setPriority(0);
                                                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                                                            z20 = true;
                                                                                            i13 = 3;
                                                                                        }
                                                                                        z20 = true;
                                                                                        i13 = 0;
                                                                                    } else {
                                                                                        if (i != 1 && i != 2) {
                                                                                            if (i == 4) {
                                                                                                builder.setPriority(-2);
                                                                                                if (Build.VERSION.SDK_INT >= 26) {
                                                                                                    z20 = true;
                                                                                                    i13 = 1;
                                                                                                }
                                                                                            } else if (i == 5) {
                                                                                                builder.setPriority(-1);
                                                                                                if (Build.VERSION.SDK_INT >= 26) {
                                                                                                    z20 = true;
                                                                                                    i13 = 2;
                                                                                                }
                                                                                            }
                                                                                            z20 = true;
                                                                                            i13 = 0;
                                                                                        }
                                                                                        builder.setPriority(1);
                                                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                                                            z20 = true;
                                                                                            i13 = 4;
                                                                                        }
                                                                                        z20 = true;
                                                                                        i13 = 0;
                                                                                    }
                                                                                    if (z31 != z20 && !z12) {
                                                                                        if (!z13 || (sharedPreferences2.getBoolean("EnableInAppPreview", z20) && str7 != null)) {
                                                                                            builder.setTicker(str7.length() > 100 ? str7.substring(0, 100).replace('\n', ' ').trim() + "..." : str7);
                                                                                        }
                                                                                        if (str8 != null && !str8.equalsIgnoreCase("NoSound")) {
                                                                                            int i38 = Build.VERSION.SDK_INT;
                                                                                            if (i38 >= 26) {
                                                                                                if (!str8.equalsIgnoreCase("Default") && !str8.equals(path)) {
                                                                                                    if (z14) {
                                                                                                        Uri uriForFile = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", new File(str8));
                                                                                                        ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile, 1);
                                                                                                        uri = uriForFile;
                                                                                                    } else {
                                                                                                        uri = Uri.parse(str8);
                                                                                                    }
                                                                                                    if (i37 != 0) {
                                                                                                        i14 = i37;
                                                                                                        builder.setLights(i14, 1000, 1000);
                                                                                                    } else {
                                                                                                        i14 = i37;
                                                                                                    }
                                                                                                    if (i36 == 2) {
                                                                                                        jArr6 = new long[]{0, 0};
                                                                                                        builder.setVibrate(jArr6);
                                                                                                    } else if (i36 == 1) {
                                                                                                        jArr6 = new long[]{0, 100, 0, 100};
                                                                                                        builder.setVibrate(jArr6);
                                                                                                    } else {
                                                                                                        if (i36 != 0 && i36 != 4) {
                                                                                                            if (i36 == 3) {
                                                                                                                jArr5 = new long[]{0, 1000};
                                                                                                                builder.setVibrate(jArr5);
                                                                                                                jArr2 = jArr5;
                                                                                                                jArr = uri;
                                                                                                            } else {
                                                                                                                jArr2 = jArr;
                                                                                                                jArr = uri;
                                                                                                            }
                                                                                                        }
                                                                                                        builder.setDefaults(2);
                                                                                                        jArr5 = new long[0];
                                                                                                        jArr2 = jArr5;
                                                                                                        jArr = uri;
                                                                                                    }
                                                                                                    jArr2 = jArr6;
                                                                                                    jArr = uri;
                                                                                                }
                                                                                                uri = Settings.System.DEFAULT_NOTIFICATION_URI;
                                                                                                if (i37 != 0) {
                                                                                                }
                                                                                                if (i36 == 2) {
                                                                                                }
                                                                                                jArr2 = jArr6;
                                                                                                jArr = uri;
                                                                                            } else if (str8.equals(path)) {
                                                                                                builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, 5);
                                                                                            } else if (i38 >= 24 && str8.startsWith("file://") && !AndroidUtilities.isInternalUri(Uri.parse(str8))) {
                                                                                                try {
                                                                                                    Uri uriForFile2 = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", new File(str8.replace("file://", charSequence)));
                                                                                                    ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile2, 1);
                                                                                                    builder.setSound(uriForFile2, 5);
                                                                                                } catch (Exception unused2) {
                                                                                                    builder.setSound(Uri.parse(str8), 5);
                                                                                                }
                                                                                            } else {
                                                                                                builder.setSound(Uri.parse(str8), 5);
                                                                                            }
                                                                                        }
                                                                                        uri = jArr;
                                                                                        if (i37 != 0) {
                                                                                        }
                                                                                        if (i36 == 2) {
                                                                                        }
                                                                                        jArr2 = jArr6;
                                                                                        jArr = uri;
                                                                                    } else {
                                                                                        i14 = i37;
                                                                                        long[] jArr8 = {0, 0};
                                                                                        builder.setVibrate(jArr8);
                                                                                        jArr2 = jArr8;
                                                                                    }
                                                                                    if (AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter || messageObject2.getDialogId() != 777000 || (tLRPC$ReplyMarkup = messageObject2.messageOwner.reply_markup) == null) {
                                                                                        jArr3 = jArr;
                                                                                        i15 = i13;
                                                                                        j9 = j7;
                                                                                        z21 = false;
                                                                                    } else {
                                                                                        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList4 = tLRPC$ReplyMarkup.rows;
                                                                                        int size = arrayList4.size();
                                                                                        int i39 = 0;
                                                                                        boolean z33 = false;
                                                                                        while (i39 < size) {
                                                                                            TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow = arrayList4.get(i39);
                                                                                            int size2 = tLRPC$TL_keyboardButtonRow.buttons.size();
                                                                                            boolean z34 = z33;
                                                                                            int i40 = 0;
                                                                                            while (i40 < size2) {
                                                                                                TLRPC$KeyboardButton tLRPC$KeyboardButton = tLRPC$TL_keyboardButtonRow.buttons.get(i40);
                                                                                                int i41 = size2;
                                                                                                if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonCallback) {
                                                                                                    arrayList = arrayList4;
                                                                                                    i17 = size;
                                                                                                    Intent intent3 = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                                                                                    intent3.putExtra(str16, this.currentAccount);
                                                                                                    jArr4 = jArr;
                                                                                                    i18 = i13;
                                                                                                    j10 = j7;
                                                                                                    intent3.putExtra("did", j10);
                                                                                                    byte[] bArr = tLRPC$KeyboardButton.data;
                                                                                                    if (bArr != null) {
                                                                                                        intent3.putExtra("data", bArr);
                                                                                                    }
                                                                                                    intent3.putExtra("mid", messageObject2.getId());
                                                                                                    String str17 = tLRPC$KeyboardButton.text;
                                                                                                    Context context = ApplicationLoader.applicationContext;
                                                                                                    int i42 = this.lastButtonId;
                                                                                                    messageObject3 = messageObject2;
                                                                                                    this.lastButtonId = i42 + 1;
                                                                                                    builder.addAction(0, str17, PendingIntent.getBroadcast(context, i42, intent3, 167772160));
                                                                                                    z34 = true;
                                                                                                } else {
                                                                                                    arrayList = arrayList4;
                                                                                                    i17 = size;
                                                                                                    jArr4 = jArr;
                                                                                                    i18 = i13;
                                                                                                    j10 = j7;
                                                                                                    messageObject3 = messageObject2;
                                                                                                }
                                                                                                i40++;
                                                                                                size2 = i41;
                                                                                                arrayList4 = arrayList;
                                                                                                size = i17;
                                                                                                messageObject2 = messageObject3;
                                                                                                j7 = j10;
                                                                                                i13 = i18;
                                                                                                jArr = jArr4;
                                                                                            }
                                                                                            i39++;
                                                                                            z33 = z34;
                                                                                            messageObject2 = messageObject2;
                                                                                            j7 = j7;
                                                                                            i13 = i13;
                                                                                            jArr = jArr;
                                                                                        }
                                                                                        jArr3 = jArr;
                                                                                        i15 = i13;
                                                                                        j9 = j7;
                                                                                        z21 = z33;
                                                                                    }
                                                                                    if (!z21 && (i16 = Build.VERSION.SDK_INT) < 24 && SharedConfig.passcodeHash.length() == 0 && hasMessagesToReply()) {
                                                                                        Intent intent4 = new Intent(ApplicationLoader.applicationContext, PopupReplyReceiver.class);
                                                                                        intent4.putExtra(str16, this.currentAccount);
                                                                                        if (i16 <= 19) {
                                                                                            builder.addAction(R.drawable.ic_ab_reply2, LocaleController.getString("Reply", R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent4, 167772160));
                                                                                        } else {
                                                                                            builder.addAction(R.drawable.ic_ab_reply, LocaleController.getString("Reply", R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent4, 167772160));
                                                                                        }
                                                                                    }
                                                                                    showExtraNotifications(builder, str6, j9, j6, str11, jArr2, i14, jArr3, i15, z32, z13, z12, i6);
                                                                                    scheduleNotificationRepeat();
                                                                                    return;
                                                                                }
                                                                                jArr = null;
                                                                                if (z) {
                                                                                }
                                                                                builder.setPriority(-1);
                                                                            }
                                                                        } else {
                                                                            tLRPC$User2 = tLRPC$User;
                                                                            if (this.pushDialogs.size() == 1 && j7 != globalSecretChatId) {
                                                                                intent.putExtra("encId", DialogObject.getEncryptedChatId(j7));
                                                                            }
                                                                        }
                                                                        tLRPC$FileLocation = null;
                                                                        String str162 = str2;
                                                                        intent.putExtra(str162, this.currentAccount);
                                                                        int i362 = i9;
                                                                        int i372 = i11;
                                                                        builder.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject2.messageOwner.date * 1000).setColor(-15618822);
                                                                        builder.setCategory("msg");
                                                                        if (tLRPC$Chat2 == null) {
                                                                            builder.addPerson("tel:+" + tLRPC$User2.phone);
                                                                        }
                                                                        Intent intent22 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                                        intent22.putExtra("messageDate", messageObject2.messageOwner.date);
                                                                        intent22.putExtra(str162, this.currentAccount);
                                                                        if (messageObject2.isStoryPush) {
                                                                        }
                                                                        if (messageObject2.isStoryReactionPush) {
                                                                        }
                                                                        builder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent22, 167772160));
                                                                        if (bitmap2 == null) {
                                                                        }
                                                                        jArr = null;
                                                                        if (z) {
                                                                        }
                                                                        builder.setPriority(-1);
                                                                    }
                                                                    tLRPC$User2 = tLRPC$User;
                                                                    tLRPC$FileLocation = null;
                                                                    String str1622 = str2;
                                                                    intent.putExtra(str1622, this.currentAccount);
                                                                    int i3622 = i9;
                                                                    int i3722 = i11;
                                                                    builder.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject2.messageOwner.date * 1000).setColor(-15618822);
                                                                    builder.setCategory("msg");
                                                                    if (tLRPC$Chat2 == null) {
                                                                    }
                                                                    Intent intent222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                                    intent222.putExtra("messageDate", messageObject2.messageOwner.date);
                                                                    intent222.putExtra(str1622, this.currentAccount);
                                                                    if (messageObject2.isStoryPush) {
                                                                    }
                                                                    if (messageObject2.isStoryReactionPush) {
                                                                    }
                                                                    builder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent222, 167772160));
                                                                    if (bitmap2 == null) {
                                                                    }
                                                                    jArr = null;
                                                                    if (z) {
                                                                    }
                                                                    builder.setPriority(-1);
                                                                } else {
                                                                    charSequence2 = string2;
                                                                    j7 = j4;
                                                                    long j18 = j;
                                                                    if (j18 != 0) {
                                                                        j = j18;
                                                                        long j19 = sharedPreferences2.getLong(z28 ? "StoriesSoundDocId" : "GlobalSoundDocId", 0L);
                                                                        if (j19 != 0) {
                                                                            string3 = getMediaDataController().ringtoneDataStore.getSoundPath(j19);
                                                                            z22 = true;
                                                                        } else {
                                                                            string3 = sharedPreferences2.getString(z28 ? "StoriesSoundPath" : "GlobalSoundPath", path);
                                                                            z22 = false;
                                                                        }
                                                                        i3 = sharedPreferences2.getInt("vibrate_messages", 0);
                                                                        i4 = sharedPreferences2.getInt("priority_messages", 1);
                                                                        boolean z35 = z22;
                                                                        i5 = sharedPreferences2.getInt("MessagesLed", -16776961);
                                                                        i6 = z28 ? 3 : 1;
                                                                        z16 = z35;
                                                                        i7 = 4;
                                                                        String str152 = string3;
                                                                        i8 = i5;
                                                                        str9 = str152;
                                                                        if (i3 != i7) {
                                                                        }
                                                                        if (TextUtils.isEmpty(str8)) {
                                                                        }
                                                                        str8 = str9;
                                                                        z14 = z16;
                                                                        i10 = 3;
                                                                        z18 = true;
                                                                        if (i != i10) {
                                                                        }
                                                                        i = i4;
                                                                        if (num2 != null) {
                                                                            i8 = num2.intValue();
                                                                            z18 = false;
                                                                        }
                                                                        if (i2 != 0) {
                                                                        }
                                                                        z19 = z18;
                                                                        if (z13) {
                                                                        }
                                                                        if (z17) {
                                                                            ringerMode = audioManager.getRingerMode();
                                                                            if (ringerMode != 0) {
                                                                                i9 = 2;
                                                                            }
                                                                        }
                                                                        if (z12) {
                                                                        }
                                                                        Intent intent5 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                                        StringBuilder sb42 = new StringBuilder();
                                                                        sb42.append("com.tmessages.openchat");
                                                                        boolean z322 = z19;
                                                                        sb42.append(Math.random());
                                                                        sb42.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                                        intent5.setAction(sb42.toString());
                                                                        intent5.setFlags(ConnectionsManager.FileTypeFile);
                                                                        if (!messageObject2.isStoryReactionPush) {
                                                                        }
                                                                        tLRPC$User2 = tLRPC$User;
                                                                        tLRPC$FileLocation = null;
                                                                        String str16222 = str2;
                                                                        intent5.putExtra(str16222, this.currentAccount);
                                                                        int i36222 = i9;
                                                                        int i37222 = i11;
                                                                        builder.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent5, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject2.messageOwner.date * 1000).setColor(-15618822);
                                                                        builder.setCategory("msg");
                                                                        if (tLRPC$Chat2 == null) {
                                                                        }
                                                                        Intent intent2222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                                        intent2222.putExtra("messageDate", messageObject2.messageOwner.date);
                                                                        intent2222.putExtra(str16222, this.currentAccount);
                                                                        if (messageObject2.isStoryPush) {
                                                                        }
                                                                        if (messageObject2.isStoryReactionPush) {
                                                                        }
                                                                        builder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent2222, 167772160));
                                                                        if (bitmap2 == null) {
                                                                        }
                                                                        jArr = null;
                                                                        if (z) {
                                                                        }
                                                                        builder.setPriority(-1);
                                                                    } else {
                                                                        j = j18;
                                                                        str9 = null;
                                                                        i8 = -16776961;
                                                                        i3 = 0;
                                                                        i4 = 0;
                                                                        i7 = 4;
                                                                        i6 = 1;
                                                                        z16 = false;
                                                                        if (i3 != i7) {
                                                                        }
                                                                        if (TextUtils.isEmpty(str8)) {
                                                                        }
                                                                        str8 = str9;
                                                                        z14 = z16;
                                                                        i10 = 3;
                                                                        z18 = true;
                                                                        if (i != i10) {
                                                                        }
                                                                        i = i4;
                                                                        if (num2 != null) {
                                                                        }
                                                                        if (i2 != 0) {
                                                                        }
                                                                        z19 = z18;
                                                                        if (z13) {
                                                                        }
                                                                        if (z17) {
                                                                        }
                                                                        if (z12) {
                                                                        }
                                                                        Intent intent52 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                                        StringBuilder sb422 = new StringBuilder();
                                                                        sb422.append("com.tmessages.openchat");
                                                                        boolean z3222 = z19;
                                                                        sb422.append(Math.random());
                                                                        sb422.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                                        intent52.setAction(sb422.toString());
                                                                        intent52.setFlags(ConnectionsManager.FileTypeFile);
                                                                        if (!messageObject2.isStoryReactionPush) {
                                                                        }
                                                                        tLRPC$User2 = tLRPC$User;
                                                                        tLRPC$FileLocation = null;
                                                                        String str162222 = str2;
                                                                        intent52.putExtra(str162222, this.currentAccount);
                                                                        int i362222 = i9;
                                                                        int i372222 = i11;
                                                                        builder.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent52, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject2.messageOwner.date * 1000).setColor(-15618822);
                                                                        builder.setCategory("msg");
                                                                        if (tLRPC$Chat2 == null) {
                                                                        }
                                                                        Intent intent22222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                                        intent22222.putExtra("messageDate", messageObject2.messageOwner.date);
                                                                        intent22222.putExtra(str162222, this.currentAccount);
                                                                        if (messageObject2.isStoryPush) {
                                                                        }
                                                                        if (messageObject2.isStoryReactionPush) {
                                                                        }
                                                                        builder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent22222, 167772160));
                                                                        if (bitmap2 == null) {
                                                                        }
                                                                        jArr = null;
                                                                        if (z) {
                                                                        }
                                                                        builder.setPriority(-1);
                                                                    }
                                                                }
                                                            }
                                                            charSequence2 = string2;
                                                            num2 = num;
                                                            j7 = j4;
                                                            j8 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                                                            if (j8 != 0) {
                                                                string3 = getMediaDataController().ringtoneDataStore.getSoundPath(j8);
                                                                z15 = true;
                                                            } else {
                                                                string3 = sharedPreferences2.getString("ReactionSoundPath", path);
                                                                z15 = false;
                                                            }
                                                            i3 = sharedPreferences2.getInt("vibrate_react", 0);
                                                            i4 = sharedPreferences2.getInt("priority_react", 1);
                                                            z16 = z15;
                                                            i5 = sharedPreferences2.getInt("ReactionsLed", -16776961);
                                                            i6 = messageObject2.isStoryReactionPush ? 5 : 4;
                                                            i7 = 4;
                                                            String str1522 = string3;
                                                            i8 = i5;
                                                            str9 = str1522;
                                                            if (i3 != i7) {
                                                            }
                                                            if (TextUtils.isEmpty(str8)) {
                                                            }
                                                            str8 = str9;
                                                            z14 = z16;
                                                            i10 = 3;
                                                            z18 = true;
                                                            if (i != i10) {
                                                            }
                                                            i = i4;
                                                            if (num2 != null) {
                                                            }
                                                            if (i2 != 0) {
                                                            }
                                                            z19 = z18;
                                                            if (z13) {
                                                            }
                                                            if (z17) {
                                                            }
                                                            if (z12) {
                                                            }
                                                            Intent intent522 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                            StringBuilder sb4222 = new StringBuilder();
                                                            sb4222.append("com.tmessages.openchat");
                                                            boolean z32222 = z19;
                                                            sb4222.append(Math.random());
                                                            sb4222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                            intent522.setAction(sb4222.toString());
                                                            intent522.setFlags(ConnectionsManager.FileTypeFile);
                                                            if (!messageObject2.isStoryReactionPush) {
                                                            }
                                                            tLRPC$User2 = tLRPC$User;
                                                            tLRPC$FileLocation = null;
                                                            String str1622222 = str2;
                                                            intent522.putExtra(str1622222, this.currentAccount);
                                                            int i3622222 = i9;
                                                            int i3722222 = i11;
                                                            builder.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent522, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject2.messageOwner.date * 1000).setColor(-15618822);
                                                            builder.setCategory("msg");
                                                            if (tLRPC$Chat2 == null) {
                                                            }
                                                            Intent intent222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                            intent222222.putExtra("messageDate", messageObject2.messageOwner.date);
                                                            intent222222.putExtra(str1622222, this.currentAccount);
                                                            if (messageObject2.isStoryPush) {
                                                            }
                                                            if (messageObject2.isStoryReactionPush) {
                                                            }
                                                            builder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent222222, 167772160));
                                                            if (bitmap2 == null) {
                                                            }
                                                            jArr = null;
                                                            if (z) {
                                                            }
                                                            builder.setPriority(-1);
                                                        }
                                                    } else {
                                                        j5 = j2;
                                                    }
                                                    z12 = z11;
                                                    String path2 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                                                    if (!ApplicationLoader.mainInterfacePaused) {
                                                    }
                                                    getSharedPrefKey(j4, j5);
                                                    if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j4, j5, false)) {
                                                    }
                                                    boolean z312 = z8;
                                                    if (!messageObject2.isReactionPush) {
                                                        num2 = num;
                                                        if (j3 == 0) {
                                                        }
                                                    }
                                                    charSequence2 = string2;
                                                    num2 = num;
                                                    j7 = j4;
                                                    j8 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                                                    if (j8 != 0) {
                                                    }
                                                    i3 = sharedPreferences2.getInt("vibrate_react", 0);
                                                    i4 = sharedPreferences2.getInt("priority_react", 1);
                                                    z16 = z15;
                                                    i5 = sharedPreferences2.getInt("ReactionsLed", -16776961);
                                                    i6 = messageObject2.isStoryReactionPush ? 5 : 4;
                                                    i7 = 4;
                                                    String str15222 = string3;
                                                    i8 = i5;
                                                    str9 = str15222;
                                                    if (i3 != i7) {
                                                    }
                                                    if (TextUtils.isEmpty(str8)) {
                                                    }
                                                    str8 = str9;
                                                    z14 = z16;
                                                    i10 = 3;
                                                    z18 = true;
                                                    if (i != i10) {
                                                    }
                                                    i = i4;
                                                    if (num2 != null) {
                                                    }
                                                    if (i2 != 0) {
                                                    }
                                                    z19 = z18;
                                                    if (z13) {
                                                    }
                                                    if (z17) {
                                                    }
                                                    if (z12) {
                                                    }
                                                    Intent intent5222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                    StringBuilder sb42222 = new StringBuilder();
                                                    sb42222.append("com.tmessages.openchat");
                                                    boolean z322222 = z19;
                                                    sb42222.append(Math.random());
                                                    sb42222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                    intent5222.setAction(sb42222.toString());
                                                    intent5222.setFlags(ConnectionsManager.FileTypeFile);
                                                    if (!messageObject2.isStoryReactionPush) {
                                                    }
                                                    tLRPC$User2 = tLRPC$User;
                                                    tLRPC$FileLocation = null;
                                                    String str16222222 = str2;
                                                    intent5222.putExtra(str16222222, this.currentAccount);
                                                    int i36222222 = i9;
                                                    int i37222222 = i11;
                                                    builder.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent5222, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject2.messageOwner.date * 1000).setColor(-15618822);
                                                    builder.setCategory("msg");
                                                    if (tLRPC$Chat2 == null) {
                                                    }
                                                    Intent intent2222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                    intent2222222.putExtra("messageDate", messageObject2.messageOwner.date);
                                                    intent2222222.putExtra(str16222222, this.currentAccount);
                                                    if (messageObject2.isStoryPush) {
                                                    }
                                                    if (messageObject2.isStoryReactionPush) {
                                                    }
                                                    builder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent2222222, 167772160));
                                                    if (bitmap2 == null) {
                                                    }
                                                    jArr = null;
                                                    if (z) {
                                                    }
                                                    builder.setPriority(-1);
                                                }
                                            }
                                        }
                                    } else {
                                        str6 = str3;
                                        z10 = z9;
                                    }
                                }
                                z11 = z10;
                                if (z11) {
                                }
                                z12 = z11;
                                String path22 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                                if (!ApplicationLoader.mainInterfacePaused) {
                                }
                                getSharedPrefKey(j4, j5);
                                if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j4, j5, false)) {
                                }
                                boolean z3122 = z8;
                                if (!messageObject2.isReactionPush) {
                                }
                                charSequence2 = string2;
                                num2 = num;
                                j7 = j4;
                                j8 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                                if (j8 != 0) {
                                }
                                i3 = sharedPreferences2.getInt("vibrate_react", 0);
                                i4 = sharedPreferences2.getInt("priority_react", 1);
                                z16 = z15;
                                i5 = sharedPreferences2.getInt("ReactionsLed", -16776961);
                                i6 = messageObject2.isStoryReactionPush ? 5 : 4;
                                i7 = 4;
                                String str152222 = string3;
                                i8 = i5;
                                str9 = str152222;
                                if (i3 != i7) {
                                }
                                if (TextUtils.isEmpty(str8)) {
                                }
                                str8 = str9;
                                z14 = z16;
                                i10 = 3;
                                z18 = true;
                                if (i != i10) {
                                }
                                i = i4;
                                if (num2 != null) {
                                }
                                if (i2 != 0) {
                                }
                                z19 = z18;
                                if (z13) {
                                }
                                if (z17) {
                                }
                                if (z12) {
                                }
                                Intent intent52222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                StringBuilder sb422222 = new StringBuilder();
                                sb422222.append("com.tmessages.openchat");
                                boolean z3222222 = z19;
                                sb422222.append(Math.random());
                                sb422222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                intent52222.setAction(sb422222.toString());
                                intent52222.setFlags(ConnectionsManager.FileTypeFile);
                                if (!messageObject2.isStoryReactionPush) {
                                }
                                tLRPC$User2 = tLRPC$User;
                                tLRPC$FileLocation = null;
                                String str162222222 = str2;
                                intent52222.putExtra(str162222222, this.currentAccount);
                                int i362222222 = i9;
                                int i372222222 = i11;
                                builder.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent52222, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject2.messageOwner.date * 1000).setColor(-15618822);
                                builder.setCategory("msg");
                                if (tLRPC$Chat2 == null) {
                                }
                                Intent intent22222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                intent22222222.putExtra("messageDate", messageObject2.messageOwner.date);
                                intent22222222.putExtra(str162222222, this.currentAccount);
                                if (messageObject2.isStoryPush) {
                                }
                                if (messageObject2.isStoryReactionPush) {
                                }
                                builder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent22222222, 167772160));
                                if (bitmap2 == null) {
                                }
                                jArr = null;
                                if (z) {
                                }
                                builder.setPriority(-1);
                            } else {
                                z10 = z9;
                                j4 = dialogId;
                                sharedPreferences2 = sharedPreferences;
                                str6 = str3;
                            }
                            str7 = str5;
                            z11 = z10;
                            if (z11) {
                            }
                            z12 = z11;
                            String path222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                            if (!ApplicationLoader.mainInterfacePaused) {
                            }
                            getSharedPrefKey(j4, j5);
                            if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j4, j5, false)) {
                            }
                            boolean z31222 = z8;
                            if (!messageObject2.isReactionPush) {
                            }
                            charSequence2 = string2;
                            num2 = num;
                            j7 = j4;
                            j8 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                            if (j8 != 0) {
                            }
                            i3 = sharedPreferences2.getInt("vibrate_react", 0);
                            i4 = sharedPreferences2.getInt("priority_react", 1);
                            z16 = z15;
                            i5 = sharedPreferences2.getInt("ReactionsLed", -16776961);
                            i6 = messageObject2.isStoryReactionPush ? 5 : 4;
                            i7 = 4;
                            String str1522222 = string3;
                            i8 = i5;
                            str9 = str1522222;
                            if (i3 != i7) {
                            }
                            if (TextUtils.isEmpty(str8)) {
                            }
                            str8 = str9;
                            z14 = z16;
                            i10 = 3;
                            z18 = true;
                            if (i != i10) {
                            }
                            i = i4;
                            if (num2 != null) {
                            }
                            if (i2 != 0) {
                            }
                            z19 = z18;
                            if (z13) {
                            }
                            if (z17) {
                            }
                            if (z12) {
                            }
                            Intent intent522222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                            StringBuilder sb4222222 = new StringBuilder();
                            sb4222222.append("com.tmessages.openchat");
                            boolean z32222222 = z19;
                            sb4222222.append(Math.random());
                            sb4222222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                            intent522222.setAction(sb4222222.toString());
                            intent522222.setFlags(ConnectionsManager.FileTypeFile);
                            if (!messageObject2.isStoryReactionPush) {
                            }
                            tLRPC$User2 = tLRPC$User;
                            tLRPC$FileLocation = null;
                            String str1622222222 = str2;
                            intent522222.putExtra(str1622222222, this.currentAccount);
                            int i3622222222 = i9;
                            int i3722222222 = i11;
                            builder.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent522222, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject2.messageOwner.date * 1000).setColor(-15618822);
                            builder.setCategory("msg");
                            if (tLRPC$Chat2 == null) {
                            }
                            Intent intent222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                            intent222222222.putExtra("messageDate", messageObject2.messageOwner.date);
                            intent222222222.putExtra(str1622222222, this.currentAccount);
                            if (messageObject2.isStoryPush) {
                            }
                            if (messageObject2.isStoryReactionPush) {
                            }
                            builder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent222222222, 167772160));
                            if (bitmap2 == null) {
                            }
                            jArr = null;
                            if (z) {
                            }
                            builder.setPriority(-1);
                        }
                        z9 = true;
                        if (z9) {
                        }
                        str7 = str5;
                        z11 = z10;
                        if (z11) {
                        }
                        z12 = z11;
                        String path2222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                        if (!ApplicationLoader.mainInterfacePaused) {
                        }
                        getSharedPrefKey(j4, j5);
                        if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j4, j5, false)) {
                        }
                        boolean z312222 = z8;
                        if (!messageObject2.isReactionPush) {
                        }
                        charSequence2 = string2;
                        num2 = num;
                        j7 = j4;
                        j8 = sharedPreferences2.getLong("ReactionSoundDocId", 0L);
                        if (j8 != 0) {
                        }
                        i3 = sharedPreferences2.getInt("vibrate_react", 0);
                        i4 = sharedPreferences2.getInt("priority_react", 1);
                        z16 = z15;
                        i5 = sharedPreferences2.getInt("ReactionsLed", -16776961);
                        i6 = messageObject2.isStoryReactionPush ? 5 : 4;
                        i7 = 4;
                        String str15222222 = string3;
                        i8 = i5;
                        str9 = str15222222;
                        if (i3 != i7) {
                        }
                        if (TextUtils.isEmpty(str8)) {
                        }
                        str8 = str9;
                        z14 = z16;
                        i10 = 3;
                        z18 = true;
                        if (i != i10) {
                        }
                        i = i4;
                        if (num2 != null) {
                        }
                        if (i2 != 0) {
                        }
                        z19 = z18;
                        if (z13) {
                        }
                        if (z17) {
                        }
                        if (z12) {
                        }
                        Intent intent5222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        StringBuilder sb42222222 = new StringBuilder();
                        sb42222222.append("com.tmessages.openchat");
                        boolean z322222222 = z19;
                        sb42222222.append(Math.random());
                        sb42222222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                        intent5222222.setAction(sb42222222.toString());
                        intent5222222.setFlags(ConnectionsManager.FileTypeFile);
                        if (!messageObject2.isStoryReactionPush) {
                        }
                        tLRPC$User2 = tLRPC$User;
                        tLRPC$FileLocation = null;
                        String str16222222222 = str2;
                        intent5222222.putExtra(str16222222222, this.currentAccount);
                        int i36222222222 = i9;
                        int i37222222222 = i11;
                        builder.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent5222222, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject2.messageOwner.date * 1000).setColor(-15618822);
                        builder.setCategory("msg");
                        if (tLRPC$Chat2 == null) {
                        }
                        Intent intent2222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                        intent2222222222.putExtra("messageDate", messageObject2.messageOwner.date);
                        intent2222222222.putExtra(str16222222222, this.currentAccount);
                        if (messageObject2.isStoryPush) {
                        }
                        if (messageObject2.isStoryReactionPush) {
                        }
                        builder.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent2222222222, 167772160));
                        if (bitmap2 == null) {
                        }
                        jArr = null;
                        if (z) {
                        }
                        builder.setPriority(-1);
                    }
                    str = string;
                    if (!notificationsSettings.getBoolean("EnableReactionsPreview", true)) {
                        string2 = LocaleController.getString("NotificationHiddenName", R.string.NotificationHiddenName);
                        if (z5) {
                        }
                        NotificationCompat.Builder builder3 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                        if (this.pushMessages.size() > 1) {
                        }
                        MessageObject messageObject62 = messageObject;
                        boolean[] zArr3 = new boolean[1];
                        String str122 = str3;
                        messageObject2 = messageObject62;
                        stringForMessage = getStringForMessage(messageObject2, false, zArr3, null);
                        boolean isSilentMessage2 = isSilentMessage(messageObject2);
                        if (stringForMessage == null) {
                        }
                    }
                    string2 = str;
                    if (z5) {
                    }
                    NotificationCompat.Builder builder32 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                    if (this.pushMessages.size() > 1) {
                    }
                    MessageObject messageObject622 = messageObject;
                    boolean[] zArr32 = new boolean[1];
                    String str1222 = str3;
                    messageObject2 = messageObject622;
                    stringForMessage = getStringForMessage(messageObject2, false, zArr32, null);
                    boolean isSilentMessage22 = isSilentMessage(messageObject2);
                    if (stringForMessage == null) {
                    }
                }
                string = LocaleController.getString("AppName", R.string.AppName);
                z6 = false;
                if (!messageObject.isReactionPush) {
                    str = string;
                    string2 = str;
                    if (z5) {
                    }
                    NotificationCompat.Builder builder322 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                    if (this.pushMessages.size() > 1) {
                    }
                    MessageObject messageObject6222 = messageObject;
                    boolean[] zArr322 = new boolean[1];
                    String str12222 = str3;
                    messageObject2 = messageObject6222;
                    stringForMessage = getStringForMessage(messageObject2, false, zArr322, null);
                    boolean isSilentMessage222 = isSilentMessage(messageObject2);
                    if (stringForMessage == null) {
                    }
                }
                str = string;
                if (!notificationsSettings.getBoolean("EnableReactionsPreview", true)) {
                }
                string2 = str;
                if (z5) {
                }
                NotificationCompat.Builder builder3222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                if (this.pushMessages.size() > 1) {
                }
                MessageObject messageObject62222 = messageObject;
                boolean[] zArr3222 = new boolean[1];
                String str122222 = str3;
                messageObject2 = messageObject62222;
                stringForMessage = getStringForMessage(messageObject2, false, zArr3222, null);
                boolean isSilentMessage2222 = isSilentMessage(messageObject2);
                if (stringForMessage == null) {
                }
            }
            z4 = true;
            if ("samsung".equalsIgnoreCase(Build.MANUFACTURER)) {
            }
            if (!DialogObject.isEncryptedDialog(dialogId)) {
                string = str11;
                z6 = true;
                if (!messageObject.isReactionPush) {
                }
                str = string;
                if (!notificationsSettings.getBoolean("EnableReactionsPreview", true)) {
                }
                string2 = str;
                if (z5) {
                }
                NotificationCompat.Builder builder32222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                if (this.pushMessages.size() > 1) {
                }
                MessageObject messageObject622222 = messageObject;
                boolean[] zArr32222 = new boolean[1];
                String str1222222 = str3;
                messageObject2 = messageObject622222;
                stringForMessage = getStringForMessage(messageObject2, false, zArr32222, null);
                boolean isSilentMessage22222 = isSilentMessage(messageObject2);
                if (stringForMessage == null) {
                }
            }
            string = LocaleController.getString("AppName", R.string.AppName);
            z6 = false;
            if (!messageObject.isReactionPush) {
            }
            str = string;
            if (!notificationsSettings.getBoolean("EnableReactionsPreview", true)) {
            }
            string2 = str;
            if (z5) {
            }
            NotificationCompat.Builder builder322222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
            if (this.pushMessages.size() > 1) {
            }
            MessageObject messageObject6222222 = messageObject;
            boolean[] zArr322222 = new boolean[1];
            String str12222222 = str3;
            messageObject2 = messageObject6222222;
            stringForMessage = getStringForMessage(messageObject2, false, zArr322222, null);
            boolean isSilentMessage222222 = isSilentMessage(messageObject2);
            if (stringForMessage == null) {
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    private boolean isSilentMessage(MessageObject messageObject) {
        return messageObject.messageOwner.silent || messageObject.isReactionPush;
    }

    @SuppressLint({"NewApi"})
    private void setNotificationChannel(Notification notification, NotificationCompat.Builder builder, boolean z) {
        if (z) {
            builder.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
        } else {
            builder.setChannelId(notification.getChannelId());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetNotificationSound(NotificationCompat.Builder builder, long j, long j2, String str, long[] jArr, int i, Uri uri, int i2, boolean z, boolean z2, boolean z3, int i3) {
        Uri uri2 = Settings.System.DEFAULT_RINGTONE_URI;
        if (uri2 == null || uri == null || TextUtils.equals(uri2.toString(), uri.toString())) {
            return;
        }
        SharedPreferences.Editor edit = getAccountInstance().getNotificationsSettings().edit();
        String uri3 = uri2.toString();
        String string = LocaleController.getString("DefaultRingtone", R.string.DefaultRingtone);
        if (z) {
            if (i3 == 2) {
                edit.putString("ChannelSound", string);
            } else if (i3 == 0) {
                edit.putString("GroupSound", string);
            } else if (i3 == 1) {
                edit.putString("GlobalSound", string);
            } else if (i3 == 3) {
                edit.putString("StoriesSound", string);
            } else if (i3 == 4 || i3 == 5) {
                edit.putString("ReactionSound", string);
            }
            if (i3 == 2) {
                edit.putString("ChannelSoundPath", uri3);
            } else if (i3 == 0) {
                edit.putString("GroupSoundPath", uri3);
            } else if (i3 == 1) {
                edit.putString("GlobalSoundPath", uri3);
            } else if (i3 == 3) {
                edit.putString("StoriesSoundPath", uri3);
            } else if (i3 == 4 || i3 == 5) {
                edit.putString("ReactionSound", uri3);
            }
            getNotificationsController().lambda$deleteNotificationChannelGlobal$40(i3, -1);
        } else {
            edit.putString("sound_" + getSharedPrefKey(j, j2), string);
            edit.putString("sound_path_" + getSharedPrefKey(j, j2), uri3);
            lambda$deleteNotificationChannel$39(j, j2, -1);
        }
        edit.commit();
        builder.setChannelId(validateChannelId(j, j2, str, jArr, i, Settings.System.DEFAULT_RINGTONE_URI, i2, z, z2, z3, i3));
        notificationManager.notify(this.notificationId, builder.build());
    }

    /* JADX WARN: Can't wrap try/catch for region: R(92:55|(2:57|(3:59|60|61)(4:62|(2:65|63)|66|67))(1:693)|68|(1:70)(2:(1:690)(1:692)|691)|71|(4:74|(2:76|77)(1:79)|78|72)|80|81|(5:83|(2:(1:86)(1:598)|87)(1:599)|(1:597)(2:93|(2:97|98))|595|596)(2:600|(8:(1:673)(1:609)|610|(8:612|(2:614|(1:616)(2:626|(1:628)))(2:630|(7:634|(5:638|619|(1:621)(2:623|(1:625))|622|98)|618|619|(0)(0)|622|98))|617|618|619|(0)(0)|622|98)(83:639|(2:641|(1:643)(2:644|(1:646)))(10:647|(1:672)(1:651)|652|(1:671)(2:656|(1:658))|670|660|(2:662|(3:664|(1:666)|667))(1:669)|668|(0)|667)|100|(1:594)(2:104|(72:106|107|(3:109|(1:111)(1:591)|112)(1:592)|(3:114|(3:116|(1:118)(3:577|578|(3:580|(1:582)(1:584)|583)(1:585))|119)(1:589)|588)(1:590)|(3:121|(1:127)|128)(1:576)|129|(3:571|(1:573)(1:575)|574)(1:132)|133|(1:135)|136|(1:138)(1:563)|139|(1:562)(1:143)|144|(3:147|(1:149)|(3:151|152|(52:156|157|158|(4:162|163|164|165)|170|(1:555)(1:174)|175|(1:554)(1:178)|179|(1:553)|186|(1:552)(1:193)|194|(12:196|(1:198)(2:341|(5:343|344|345|346|61)(2:347|(1:(1:350)(1:351))(10:352|(1:354)(2:355|(1:360)(1:359))|200|(2:203|201)|204|205|(1:340)(1:208)|209|(1:211)(1:339)|212)))|199|200|(1:201)|204|205|(0)|340|209|(0)(0)|212)(4:361|(6:363|(1:365)(4:370|(1:372)(2:545|(2:549|(3:375|(1:377)|378)(22:379|(1:381)|382|(2:541|(1:543)(1:544))(1:388)|389|(1:391)(12:(1:537)(2:538|(1:540))|393|(2:(2:396|(2:(2:399|(1:401))(1:530)|402)(2:531|(1:533)))(1:534)|529)(1:535)|403|(3:499|(1:528)(3:505|(1:527)(4:508|(1:512)|(1:526)(2:518|(1:522))|525)|523)|524)(1:407)|408|(6:410|(1:497)(7:423|(1:496)(3:427|(9:478|479|480|481|482|483|484|485|486)(1:429)|430)|431|(1:433)(1:477)|434|(3:471|472|473)(3:436|(1:438)|470)|(6:440|(1:442)|443|(1:445)|446|(2:451|(3:453|(2:458|459)(1:455)|(1:457))))(1:468))|469|(0)|446|(3:449|451|(0)))(1:498)|462|(3:466|467|369)|367|368|369)|392|393|(0)(0)|403|(1:405)|499|(1:501)|528|524|408|(0)(0)|462|(4:464|466|467|369)|367|368|369)))|373|(0)(0))|366|367|368|369)|550|551)|213|(2:324|(4:326|(2:329|327)|330|331)(2:332|(1:334)(2:335|(1:337)(1:338))))(1:217)|218|(1:220)|221|(1:223)|224|(2:226|(1:228)(1:319))(2:320|(1:322)(1:323))|(1:230)(1:318)|231|(4:233|(2:236|234)|237|238)(1:317)|239|(1:241)|242|243|244|245|246|247|(1:249)|250|(1:252)|(1:254)|(1:261)|262|(1:310)(1:268)|269|(1:271)|(1:273)|274|(3:279|(4:281|(3:283|(4:285|(1:287)|288|289)(2:291|292)|290)|293|294)|295)|296|(1:309)(2:299|(1:303))|304|(1:306)|307|308|61)))|561|170|(1:172)|555|175|(0)|554|179|(1:181)|553|186|(1:189)|552|194|(0)(0)|213|(1:215)|324|(0)(0)|218|(0)|221|(0)|224|(0)(0)|(0)(0)|231|(0)(0)|239|(0)|242|243|244|245|246|247|(0)|250|(0)|(0)|(2:256|261)|262|(1:264)|310|269|(0)|(0)|274|(4:276|279|(0)|295)|296|(0)|309|304|(0)|307|308|61))|593|107|(0)(0)|(0)(0)|(0)(0)|129|(0)|565|567|569|571|(0)(0)|574|133|(0)|136|(0)(0)|139|(1:141)|562|144|(3:147|(0)|(0))|561|170|(0)|555|175|(0)|554|179|(0)|553|186|(0)|552|194|(0)(0)|213|(0)|324|(0)(0)|218|(0)|221|(0)|224|(0)(0)|(0)(0)|231|(0)(0)|239|(0)|242|243|244|245|246|247|(0)|250|(0)|(0)|(0)|262|(0)|310|269|(0)|(0)|274|(0)|296|(0)|309|304|(0)|307|308|61)|629|344|345|346|61)(4:674|(6:676|(2:678|(1:680))(2:682|(2:684|(1:686)))|681|345|346|61)(1:688)|687|596))|99|100|(1:102)|594|593|107|(0)(0)|(0)(0)|(0)(0)|129|(0)|565|567|569|571|(0)(0)|574|133|(0)|136|(0)(0)|139|(0)|562|144|(0)|561|170|(0)|555|175|(0)|554|179|(0)|553|186|(0)|552|194|(0)(0)|213|(0)|324|(0)(0)|218|(0)|221|(0)|224|(0)(0)|(0)(0)|231|(0)(0)|239|(0)|242|243|244|245|246|247|(0)|250|(0)|(0)|(0)|262|(0)|310|269|(0)|(0)|274|(0)|296|(0)|309|304|(0)|307|308|61) */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x040e, code lost:
        if (r3.local_id != 0) goto L660;
     */
    /* JADX WARN: Code restructure failed: missing block: B:609:0x1071, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:611:0x1073, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:612:0x1074, code lost:
        r3 = r44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:613:0x1076, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:104:0x02ec  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0367  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0370  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x0451  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x04f7  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x0519  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x0533  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x053c  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x0594  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x059f  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x05c5  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x05d0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:257:0x0633  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x0644  */
    /* JADX WARN: Removed duplicated region for block: B:263:0x068c  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x0697  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x069e  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x06ad  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x06e2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:280:0x06f2  */
    /* JADX WARN: Removed duplicated region for block: B:304:0x074e  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x075e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:316:0x0771  */
    /* JADX WARN: Removed duplicated region for block: B:324:0x0786 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:334:0x07b4  */
    /* JADX WARN: Removed duplicated region for block: B:358:0x08cb A[LOOP:5: B:356:0x08c3->B:358:0x08cb, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:366:0x0910  */
    /* JADX WARN: Removed duplicated region for block: B:367:0x0915  */
    /* JADX WARN: Removed duplicated region for block: B:369:0x0925  */
    /* JADX WARN: Removed duplicated region for block: B:385:0x0997  */
    /* JADX WARN: Removed duplicated region for block: B:389:0x09c5  */
    /* JADX WARN: Removed duplicated region for block: B:414:0x0a58  */
    /* JADX WARN: Removed duplicated region for block: B:428:0x0a9a  */
    /* JADX WARN: Removed duplicated region for block: B:468:0x0b4a  */
    /* JADX WARN: Removed duplicated region for block: B:506:0x0c47  */
    /* JADX WARN: Removed duplicated region for block: B:507:0x0c4a  */
    /* JADX WARN: Removed duplicated region for block: B:514:0x0c76  */
    /* JADX WARN: Removed duplicated region for block: B:519:0x0cd0  */
    /* JADX WARN: Removed duplicated region for block: B:523:0x0cff  */
    /* JADX WARN: Removed duplicated region for block: B:527:0x0d09  */
    /* JADX WARN: Removed duplicated region for block: B:535:0x0d2a  */
    /* JADX WARN: Removed duplicated region for block: B:543:0x0d70  */
    /* JADX WARN: Removed duplicated region for block: B:554:0x0df9  */
    /* JADX WARN: Removed duplicated region for block: B:559:0x0e14  */
    /* JADX WARN: Removed duplicated region for block: B:564:0x0e3b  */
    /* JADX WARN: Removed duplicated region for block: B:573:0x0e80  */
    /* JADX WARN: Removed duplicated region for block: B:576:0x0ea1  */
    /* JADX WARN: Removed duplicated region for block: B:579:0x0f03  */
    /* JADX WARN: Removed duplicated region for block: B:583:0x0f40  */
    /* JADX WARN: Removed duplicated region for block: B:588:0x0f68  */
    /* JADX WARN: Removed duplicated region for block: B:589:0x0f8b  */
    /* JADX WARN: Removed duplicated region for block: B:592:0x0fa8  */
    /* JADX WARN: Removed duplicated region for block: B:597:0x0fcb  */
    /* JADX WARN: Removed duplicated region for block: B:600:0x0fff  */
    /* JADX WARN: Removed duplicated region for block: B:606:0x105a A[Catch: Exception -> 0x1071, TryCatch #10 {Exception -> 0x1071, blocks: (B:604:0x1053, B:606:0x105a, B:607:0x1061), top: B:731:0x1053 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0177  */
    /* JADX WARN: Removed duplicated region for block: B:615:0x107b  */
    /* JADX WARN: Removed duplicated region for block: B:617:0x1086  */
    /* JADX WARN: Removed duplicated region for block: B:619:0x108d  */
    /* JADX WARN: Removed duplicated region for block: B:627:0x10a3  */
    /* JADX WARN: Removed duplicated region for block: B:635:0x10bc  */
    /* JADX WARN: Removed duplicated region for block: B:637:0x10c1  */
    /* JADX WARN: Removed duplicated region for block: B:640:0x10cd  */
    /* JADX WARN: Removed duplicated region for block: B:645:0x10da  */
    /* JADX WARN: Removed duplicated region for block: B:658:0x115d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:667:0x118f  */
    /* JADX WARN: Removed duplicated region for block: B:672:0x1222  */
    /* JADX WARN: Removed duplicated region for block: B:679:0x126d  */
    /* JADX WARN: Removed duplicated region for block: B:685:0x1286  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x01f8  */
    /* JADX WARN: Removed duplicated region for block: B:695:0x12d5  */
    /* JADX WARN: Removed duplicated region for block: B:711:0x06fc A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0239  */
    /* JADX WARN: Removed duplicated region for block: B:729:0x0c53 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0244  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0260  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0282  */
    @SuppressLint({"InlinedApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showExtraNotifications(NotificationCompat.Builder builder, String str, long j, long j2, String str2, long[] jArr, int i, Uri uri, int i2, boolean z, boolean z2, boolean z3, int i3) {
        NotificationCompat.Builder builder2;
        boolean z4;
        long clientUserId;
        boolean z5;
        int i4;
        LongSparseArray longSparseArray;
        int size;
        int i5;
        LongSparseArray longSparseArray2;
        NotificationsController notificationsController;
        ArrayList arrayList;
        int i6;
        int size2;
        int i7;
        LongSparseArray longSparseArray3;
        DialogKey dialogKey;
        boolean z6;
        ArrayList arrayList2;
        int i8;
        int id;
        ArrayList<StoryNotification> arrayList3;
        LongSparseArray longSparseArray4;
        ArrayList arrayList4;
        long j3;
        long j4;
        Notification notification;
        MessageObject messageObject;
        long j5;
        Integer num;
        int i9;
        LongSparseArray longSparseArray5;
        int i10;
        String str3;
        ArrayList<StoryNotification> arrayList5;
        long j6;
        DialogKey dialogKey2;
        Integer num2;
        long j7;
        TLRPC$User tLRPC$User;
        String string;
        TLRPC$User tLRPC$User2;
        NotificationsController notificationsController2;
        LongSparseArray longSparseArray6;
        SharedPreferences sharedPreferences;
        int i11;
        ArrayList arrayList6;
        int i12;
        Notification notification2;
        LongSparseArray longSparseArray7;
        long j8;
        boolean z7;
        boolean z8;
        TLRPC$FileLocation tLRPC$FileLocation;
        TLRPC$FileLocation tLRPC$FileLocation2;
        TLRPC$Chat tLRPC$Chat;
        boolean z9;
        TLRPC$FileLocation tLRPC$FileLocation3;
        TLRPC$TL_forumTopic findTopic;
        String userName;
        TLRPC$FileLocation tLRPC$FileLocation4;
        TLRPC$User tLRPC$User3;
        boolean z10;
        String str4;
        String str5;
        SharedPreferences sharedPreferences2;
        String str6;
        TLRPC$FileLocation tLRPC$FileLocation5;
        String str7;
        TLRPC$User tLRPC$User4;
        DialogKey dialogKey3;
        Bitmap bitmap;
        File file;
        String str8;
        File file2;
        String str9;
        NotificationsController notificationsController3;
        TLRPC$Chat tLRPC$Chat2;
        String str10;
        int i13;
        String str11;
        String formatString;
        NotificationCompat.Action build;
        Integer num3;
        DialogKey dialogKey4;
        int max;
        String str12;
        String str13;
        long j9;
        NotificationCompat.Action action;
        long j10;
        Person person;
        NotificationCompat.MessagingStyle messagingStyle;
        int i14;
        MessageObject messageObject2;
        DialogKey dialogKey5;
        StringBuilder sb;
        ArrayList<StoryNotification> arrayList7;
        LongSparseArray longSparseArray8;
        long j11;
        long j12;
        int i15;
        Bitmap bitmap2;
        String str14;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList8;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList9;
        StringBuilder sb2;
        long j13;
        long j14;
        ArrayList<StoryNotification> arrayList10;
        long senderId;
        int i16;
        ArrayList<StoryNotification> arrayList11;
        LongSparseArray longSparseArray9;
        Person person2;
        String str15;
        String str16;
        String str17;
        String[] strArr;
        boolean z11;
        String str18;
        NotificationCompat.MessagingStyle messagingStyle2;
        Person person3;
        File file3;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        TLRPC$FileLocation tLRPC$FileLocation6;
        LongSparseArray longSparseArray10;
        String str19;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList12;
        boolean z12;
        List<NotificationCompat.MessagingStyle.Message> messages;
        Uri uri2;
        File file4;
        final File file5;
        final Uri uriForFile;
        File file6;
        Bitmap createScaledBitmap;
        Canvas canvas;
        DialogKey dialogKey6;
        long j15;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList13;
        Bitmap bitmap3;
        NotificationCompat.Action build2;
        String str20;
        String str21;
        long j16;
        ArrayList<StoryNotification> arrayList14;
        long j17;
        NotificationCompat.Builder category;
        TLRPC$User tLRPC$User5;
        int size3;
        int i17;
        int i18;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList15;
        String str22;
        LongSparseArray longSparseArray11;
        int i19;
        TLRPC$User user;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto2;
        TLRPC$FileLocation tLRPC$FileLocation7;
        Bitmap bitmap4;
        Bitmap bitmap5;
        String string2;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto3;
        int i20 = Build.VERSION.SDK_INT;
        if (i20 >= 26) {
            builder2 = builder;
            builder2.setChannelId(validateChannelId(j, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
        } else {
            builder2 = builder;
        }
        Notification build3 = builder.build();
        if (i20 <= 19) {
            notificationManager.notify(this.notificationId, build3);
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("show summary notification by SDK check");
                return;
            }
            return;
        }
        NotificationsController notificationsController4 = this;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        ArrayList arrayList16 = new ArrayList();
        if (!notificationsController4.storyPushMessages.isEmpty()) {
            arrayList16.add(new DialogKey(0L, 0L, true));
        }
        LongSparseArray longSparseArray12 = new LongSparseArray();
        int i21 = 0;
        int i22 = 0;
        while (i22 < notificationsController4.pushMessages.size()) {
            MessageObject messageObject3 = notificationsController4.pushMessages.get(i22);
            long dialogId = messageObject3.getDialogId();
            long topicId = MessageObject.getTopicId(notificationsController4.currentAccount, messageObject3.messageOwner, getMessagesController().isForum(messageObject3));
            int i23 = notificationsSettings.getInt("dismissDate" + dialogId, i21);
            if (messageObject3.isStoryPush || messageObject3.messageOwner.date > i23) {
                ArrayList arrayList17 = (ArrayList) longSparseArray12.get(dialogId);
                if (arrayList17 == null) {
                    ArrayList arrayList18 = new ArrayList();
                    longSparseArray12.put(dialogId, arrayList18);
                    arrayList16.add(new DialogKey(dialogId, topicId, false));
                    arrayList17 = arrayList18;
                }
                arrayList17.add(messageObject3);
            }
            i22++;
            i21 = 0;
        }
        LongSparseArray longSparseArray13 = new LongSparseArray();
        for (int i24 = 0; i24 < notificationsController4.wearNotificationsIds.size(); i24++) {
            longSparseArray13.put(notificationsController4.wearNotificationsIds.keyAt(i24), notificationsController4.wearNotificationsIds.valueAt(i24));
        }
        notificationsController4.wearNotificationsIds.clear();
        ArrayList arrayList19 = new ArrayList();
        int i25 = Build.VERSION.SDK_INT;
        if (i25 > 27) {
            if (arrayList16.size() <= (notificationsController4.storyPushMessages.isEmpty() ? 1 : 2)) {
                z4 = false;
                if (z4 && i25 >= 26) {
                    checkOtherNotificationsChannel();
                }
                clientUserId = getUserConfig().getClientUserId();
                z5 = !AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter;
                SharedConfig.passcodeHash.length();
                i4 = 7;
                longSparseArray = new LongSparseArray();
                size = arrayList16.size();
                i5 = 0;
                while (i5 < size && arrayList19.size() < i4) {
                    int i26 = size;
                    dialogKey = (DialogKey) arrayList16.get(i5);
                    if (!dialogKey.story) {
                        ArrayList<StoryNotification> arrayList20 = new ArrayList<>();
                        if (notificationsController4.storyPushMessages.isEmpty()) {
                            longSparseArray6 = longSparseArray;
                            z10 = z4;
                            j8 = clientUserId;
                            i12 = i5;
                            longSparseArray7 = longSparseArray13;
                            longSparseArray4 = longSparseArray12;
                            arrayList4 = arrayList16;
                            sharedPreferences = notificationsSettings;
                            notification2 = build3;
                            notificationsController2 = notificationsController4;
                            i11 = i26;
                            arrayList6 = arrayList19;
                            i5 = i12 + 1;
                            arrayList19 = arrayList6;
                            size = i11;
                            arrayList16 = arrayList4;
                            longSparseArray12 = longSparseArray4;
                            z4 = z10;
                            notificationsSettings = sharedPreferences;
                            longSparseArray13 = longSparseArray7;
                            clientUserId = j8;
                            longSparseArray = longSparseArray6;
                            build3 = notification2;
                            i4 = 7;
                            notificationsController4 = notificationsController2;
                        } else {
                            arrayList2 = arrayList19;
                            z6 = z4;
                            long j18 = notificationsController4.storyPushMessages.get(0).dialogId;
                            int i27 = 0;
                            for (Iterator<Integer> it = notificationsController4.storyPushMessages.get(0).dateByIds.keySet().iterator(); it.hasNext(); it = it) {
                                i27 = Math.max(i27, it.next().intValue());
                            }
                            longSparseArray4 = longSparseArray12;
                            arrayList4 = arrayList16;
                            notification = build3;
                            j3 = 0;
                            messageObject = null;
                            id = i27;
                            arrayList3 = arrayList20;
                            i8 = i5;
                            j4 = j18;
                        }
                    } else {
                        z6 = z4;
                        arrayList2 = arrayList19;
                        long j19 = dialogKey.dialogId;
                        i8 = i5;
                        long j20 = dialogKey.topicId;
                        ArrayList<StoryNotification> arrayList21 = (ArrayList) longSparseArray12.get(j19);
                        id = ((MessageObject) arrayList21.get(0)).getId();
                        arrayList3 = arrayList21;
                        longSparseArray4 = longSparseArray12;
                        arrayList4 = arrayList16;
                        j3 = j20;
                        j4 = j19;
                        notification = build3;
                        messageObject = (MessageObject) arrayList21.get(0);
                    }
                    Integer num4 = (Integer) longSparseArray13.get(j4);
                    int i28 = id;
                    if (dialogKey.story) {
                        if (num4 == null) {
                            j5 = j3;
                            num4 = Integer.valueOf(((int) j4) + ((int) (j4 >> 32)));
                        } else {
                            j5 = j3;
                            longSparseArray13.remove(j4);
                        }
                        num = num4;
                    } else {
                        num = 2147483646;
                        j5 = j3;
                    }
                    int i29 = 0;
                    for (i9 = 0; i9 < arrayList3.size(); i9++) {
                        if (i29 < ((MessageObject) arrayList3.get(i9)).messageOwner.date) {
                            i29 = ((MessageObject) arrayList3.get(i9)).messageOwner.date;
                        }
                    }
                    if (!dialogKey.story) {
                        TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(j4));
                        longSparseArray5 = longSparseArray13;
                        if (notificationsController4.storyPushMessages.size() == 1) {
                            if (user2 != null) {
                                string = UserObject.getFirstName(user2);
                            } else {
                                string = notificationsController4.storyPushMessages.get(0).localName;
                            }
                            i10 = i29;
                        } else {
                            i10 = i29;
                            string = LocaleController.formatPluralString("Stories", notificationsController4.storyPushMessages.size(), new Object[0]);
                        }
                        if (user2 == null || (tLRPC$UserProfilePhoto3 = user2.photo) == null || (tLRPC$FileLocation3 = tLRPC$UserProfilePhoto3.photo_small) == null) {
                            str3 = "Stories";
                        } else {
                            str3 = "Stories";
                            if (tLRPC$FileLocation3.volume_id != 0 && tLRPC$FileLocation3.local_id != 0) {
                                tLRPC$User2 = user2;
                                arrayList5 = arrayList3;
                                j6 = clientUserId;
                                dialogKey2 = dialogKey;
                                num2 = num;
                                j7 = j5;
                                z7 = false;
                                z9 = false;
                            }
                        }
                        tLRPC$User2 = user2;
                        arrayList5 = arrayList3;
                        j6 = clientUserId;
                        dialogKey2 = dialogKey;
                        num2 = num;
                        j7 = j5;
                        z7 = false;
                        z9 = false;
                        tLRPC$FileLocation3 = null;
                    } else {
                        longSparseArray5 = longSparseArray13;
                        i10 = i29;
                        str3 = "Stories";
                        if (!DialogObject.isEncryptedDialog(j4)) {
                            z7 = (messageObject == null || messageObject.isReactionPush || messageObject.isStoryReactionPush || j4 == 777000) ? false : true;
                            if (DialogObject.isUserDialog(j4)) {
                                TLRPC$User user3 = getMessagesController().getUser(Long.valueOf(j4));
                                if (user3 == null) {
                                    if (messageObject.isFcmMessage()) {
                                        userName = messageObject.localName;
                                    } else if (BuildVars.LOGS_ENABLED) {
                                        FileLog.w("not found user to show dialog notification " + j4);
                                    }
                                } else {
                                    userName = UserObject.getUserName(user3);
                                    TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto4 = user3.photo;
                                    if (tLRPC$UserProfilePhoto4 != null && (tLRPC$FileLocation4 = tLRPC$UserProfilePhoto4.photo_small) != null) {
                                        arrayList5 = arrayList3;
                                        tLRPC$User3 = user3;
                                        if (tLRPC$FileLocation4.volume_id != 0 && tLRPC$FileLocation4.local_id != 0) {
                                            string = userName;
                                            tLRPC$FileLocation3 = tLRPC$FileLocation4;
                                            if (UserObject.isReplyUser(j4)) {
                                                string = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                                            } else if (j4 == clientUserId) {
                                                string = LocaleController.getString("MessageScheduledReminderNotification", R.string.MessageScheduledReminderNotification);
                                            }
                                            j6 = clientUserId;
                                            dialogKey2 = dialogKey;
                                            num2 = num;
                                            j7 = j5;
                                            tLRPC$User2 = tLRPC$User3;
                                            z9 = false;
                                        }
                                        string = userName;
                                        tLRPC$FileLocation3 = null;
                                        if (UserObject.isReplyUser(j4)) {
                                        }
                                        j6 = clientUserId;
                                        dialogKey2 = dialogKey;
                                        num2 = num;
                                        j7 = j5;
                                        tLRPC$User2 = tLRPC$User3;
                                        z9 = false;
                                    }
                                }
                                arrayList5 = arrayList3;
                                tLRPC$User3 = user3;
                                string = userName;
                                tLRPC$FileLocation3 = null;
                                if (UserObject.isReplyUser(j4)) {
                                }
                                j6 = clientUserId;
                                dialogKey2 = dialogKey;
                                num2 = num;
                                j7 = j5;
                                tLRPC$User2 = tLRPC$User3;
                                z9 = false;
                            } else {
                                arrayList5 = arrayList3;
                                TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j4));
                                if (chat == null) {
                                    if (messageObject.isFcmMessage()) {
                                        boolean isSupergroup = messageObject.isSupergroup();
                                        string = messageObject.localName;
                                        z8 = isSupergroup;
                                        j6 = clientUserId;
                                        dialogKey2 = dialogKey;
                                        num2 = num;
                                        j7 = j5;
                                        z7 = false;
                                        tLRPC$User2 = null;
                                        tLRPC$Chat = chat;
                                        z9 = messageObject.localChannel;
                                        tLRPC$FileLocation3 = null;
                                    } else if (BuildVars.LOGS_ENABLED) {
                                        FileLog.w("not found chat to show dialog notification " + j4);
                                    }
                                } else {
                                    boolean z13 = chat.megagroup;
                                    boolean z14 = ChatObject.isChannel(chat) && !chat.megagroup;
                                    String str23 = chat.title;
                                    z8 = z13;
                                    TLRPC$ChatPhoto tLRPC$ChatPhoto = chat.photo;
                                    if (tLRPC$ChatPhoto == null || (tLRPC$FileLocation = tLRPC$ChatPhoto.photo_small) == null) {
                                        num2 = num;
                                    } else {
                                        num2 = num;
                                        if (tLRPC$FileLocation.volume_id != 0) {
                                        }
                                    }
                                    tLRPC$FileLocation = null;
                                    if (j5 != 0) {
                                        j6 = clientUserId;
                                        tLRPC$FileLocation2 = tLRPC$FileLocation;
                                        dialogKey2 = dialogKey;
                                        j7 = j5;
                                        if (getMessagesController().getTopicsController().findTopic(chat.id, j7) != null) {
                                            string = findTopic.title + " in " + str23;
                                            if (z7) {
                                                z7 = ChatObject.canSendPlain(chat);
                                            }
                                            tLRPC$Chat = chat;
                                            z9 = z14;
                                            tLRPC$FileLocation3 = tLRPC$FileLocation2;
                                            tLRPC$User2 = null;
                                        }
                                    } else {
                                        tLRPC$FileLocation2 = tLRPC$FileLocation;
                                        j6 = clientUserId;
                                        dialogKey2 = dialogKey;
                                        j7 = j5;
                                    }
                                    string = str23;
                                    if (z7) {
                                    }
                                    tLRPC$Chat = chat;
                                    z9 = z14;
                                    tLRPC$FileLocation3 = tLRPC$FileLocation2;
                                    tLRPC$User2 = null;
                                }
                                boolean z15 = z7;
                                if (messageObject == null && messageObject.isStoryReactionPush) {
                                    str4 = string;
                                    if (!notificationsSettings.getBoolean("EnableReactionsPreview", true)) {
                                        str5 = LocaleController.getString("NotificationHiddenChatName", R.string.NotificationHiddenChatName);
                                        tLRPC$FileLocation3 = null;
                                        z15 = false;
                                        if (z5) {
                                            sharedPreferences2 = notificationsSettings;
                                            TLRPC$FileLocation tLRPC$FileLocation8 = tLRPC$FileLocation3;
                                            str6 = str5;
                                            tLRPC$FileLocation5 = tLRPC$FileLocation8;
                                        } else {
                                            if (DialogObject.isChatDialog(j4)) {
                                                string2 = LocaleController.getString("NotificationHiddenChatName", R.string.NotificationHiddenChatName);
                                            } else {
                                                string2 = LocaleController.getString("NotificationHiddenName", R.string.NotificationHiddenName);
                                            }
                                            str6 = string2;
                                            sharedPreferences2 = notificationsSettings;
                                            tLRPC$FileLocation5 = null;
                                            z15 = false;
                                        }
                                        if (tLRPC$FileLocation5 == null) {
                                            dialogKey3 = dialogKey2;
                                            file = getFileLoader().getPathToAttach(tLRPC$FileLocation5, true);
                                            tLRPC$User4 = tLRPC$User2;
                                            if (Build.VERSION.SDK_INT < 28) {
                                                str7 = "NotificationHiddenName";
                                                bitmap4 = null;
                                                BitmapDrawable imageFromMemory = ImageLoader.getInstance().getImageFromMemory(tLRPC$FileLocation5, null, "50_50");
                                                if (imageFromMemory != null) {
                                                    bitmap5 = imageFromMemory.getBitmap();
                                                } else {
                                                    try {
                                                        if (file.exists()) {
                                                            float dp = 160.0f / AndroidUtilities.dp(50.0f);
                                                            BitmapFactory.Options options = new BitmapFactory.Options();
                                                            options.inSampleSize = dp < 1.0f ? 1 : (int) dp;
                                                            bitmap5 = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                                                        } else {
                                                            bitmap5 = null;
                                                        }
                                                    } catch (Throwable unused) {
                                                    }
                                                }
                                                bitmap = bitmap5;
                                            } else {
                                                str7 = "NotificationHiddenName";
                                                bitmap4 = null;
                                            }
                                            bitmap = bitmap4;
                                        } else {
                                            str7 = "NotificationHiddenName";
                                            tLRPC$User4 = tLRPC$User2;
                                            dialogKey3 = dialogKey2;
                                            bitmap = null;
                                            file = null;
                                        }
                                        if (tLRPC$Chat == null) {
                                            Person.Builder name = new Person.Builder().setName(str6);
                                            if (file != null && file.exists() && Build.VERSION.SDK_INT >= 28) {
                                                loadRoundAvatar(file, name);
                                            }
                                            str8 = "NotificationHiddenChatName";
                                            longSparseArray.put(-tLRPC$Chat.id, name.build());
                                        } else {
                                            str8 = "NotificationHiddenChatName";
                                        }
                                        Bitmap bitmap6 = bitmap;
                                        if (!(z9 || z8) || !z15 || SharedConfig.isWaitingForPasscodeEnter || j6 == j4 || UserObject.isReplyUser(j4)) {
                                            str10 = "max_id";
                                            str11 = "dialog_id";
                                            file2 = file;
                                            str9 = str8;
                                            i13 = i28;
                                            notificationsController3 = this;
                                            tLRPC$Chat2 = tLRPC$Chat;
                                            build = null;
                                        } else {
                                            file2 = file;
                                            str9 = str8;
                                            Intent intent = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                                            intent.putExtra("dialog_id", j4);
                                            intent.putExtra("max_id", i28);
                                            intent.putExtra("topic_id", j7);
                                            notificationsController3 = this;
                                            tLRPC$Chat2 = tLRPC$Chat;
                                            intent.putExtra("currentAccount", notificationsController3.currentAccount);
                                            str10 = "max_id";
                                            i13 = i28;
                                            PendingIntent broadcast = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent, 167772160);
                                            RemoteInput build4 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                                            if (!DialogObject.isChatDialog(j4)) {
                                                str11 = "dialog_id";
                                                formatString = LocaleController.formatString("ReplyToGroup", R.string.ReplyToGroup, str6);
                                            } else {
                                                str11 = "dialog_id";
                                                formatString = LocaleController.formatString("ReplyToUser", R.string.ReplyToUser, str6);
                                            }
                                            build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build4).setShowsUserInterface(false).build();
                                        }
                                        num3 = notificationsController3.pushDialogs.get(j4);
                                        if (num3 == null) {
                                            num3 = 0;
                                        }
                                        dialogKey4 = dialogKey3;
                                        if (!dialogKey4.story) {
                                            max = notificationsController3.storyPushMessages.size();
                                        } else {
                                            max = Math.max(num3.intValue(), arrayList5.size());
                                        }
                                        if (max > 1 || Build.VERSION.SDK_INT >= 28) {
                                            str12 = str6;
                                            str13 = str12;
                                        } else {
                                            str12 = String.format("%1$s (%2$d)", str6, Integer.valueOf(max));
                                            str13 = str6;
                                        }
                                        j9 = j6;
                                        Person person4 = (Person) longSparseArray.get(j9);
                                        action = build;
                                        if (Build.VERSION.SDK_INT >= 28 && person4 == null) {
                                            user = getMessagesController().getUser(Long.valueOf(j9));
                                            if (user == null) {
                                                user = getUserConfig().getCurrentUser();
                                            }
                                            if (user != null) {
                                                try {
                                                    tLRPC$UserProfilePhoto2 = user.photo;
                                                } catch (Throwable th) {
                                                    th = th;
                                                    j10 = j7;
                                                }
                                                if (tLRPC$UserProfilePhoto2 != null && (tLRPC$FileLocation7 = tLRPC$UserProfilePhoto2.photo_small) != null) {
                                                    j10 = j7;
                                                    try {
                                                        if (tLRPC$FileLocation7.volume_id != 0 && tLRPC$FileLocation7.local_id != 0) {
                                                            Person.Builder name2 = new Person.Builder().setName(LocaleController.getString("FromYou", R.string.FromYou));
                                                            loadRoundAvatar(getFileLoader().getPathToAttach(user.photo.photo_small, true), name2);
                                                            Person build5 = name2.build();
                                                            try {
                                                                longSparseArray.put(j9, build5);
                                                                person4 = build5;
                                                            } catch (Throwable th2) {
                                                                th = th2;
                                                                person4 = build5;
                                                                FileLog.e(th);
                                                                person = person4;
                                                                if (messageObject == null) {
                                                                }
                                                                if (person == null) {
                                                                }
                                                                messagingStyle = new NotificationCompat.MessagingStyle("");
                                                                i14 = Build.VERSION.SDK_INT;
                                                                if (i14 >= 28) {
                                                                }
                                                                messagingStyle.setConversationTitle(str12);
                                                                messagingStyle.setGroupConversation(i14 >= 28 || (!z9 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                                                                StringBuilder sb3 = new StringBuilder();
                                                                messageObject2 = messageObject;
                                                                String[] strArr2 = new String[1];
                                                                String str24 = "";
                                                                boolean[] zArr = new boolean[1];
                                                                if (!dialogKey4.story) {
                                                                }
                                                                Intent intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                                intent2.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                                intent2.setFlags(ConnectionsManager.FileTypeFile);
                                                                intent2.addCategory("android.intent.category.LAUNCHER");
                                                                if (messageObject2 == null) {
                                                                }
                                                                dialogKey6 = dialogKey5;
                                                                if (!dialogKey6.story) {
                                                                }
                                                                StringBuilder sb4 = new StringBuilder();
                                                                sb4.append("show extra notifications chatId ");
                                                                sb4.append(j11);
                                                                sb4.append(" topicId ");
                                                                j15 = j10;
                                                                sb4.append(j15);
                                                                FileLog.d(sb4.toString());
                                                                if (j15 != 0) {
                                                                }
                                                                intent2.putExtra("currentAccount", notificationsController3.currentAccount);
                                                                PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1140850688);
                                                                NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
                                                                if (action != null) {
                                                                }
                                                                int i30 = i15;
                                                                arrayList13 = arrayList8;
                                                                Intent intent3 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                                                intent3.addFlags(32);
                                                                intent3.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                                                intent3.putExtra(str11, j11);
                                                                int i31 = i13;
                                                                intent3.putExtra(str10, i31);
                                                                intent3.putExtra("currentAccount", notificationsController3.currentAccount);
                                                                bitmap3 = bitmap2;
                                                                build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent3, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                                                if (DialogObject.isEncryptedDialog(j11)) {
                                                                }
                                                                if (str21 == null) {
                                                                }
                                                                StringBuilder sb5 = new StringBuilder();
                                                                sb5.append("tgaccount");
                                                                long j21 = j12;
                                                                sb5.append(j21);
                                                                wearableExtender.setBridgeTag(sb5.toString());
                                                                if (!dialogKey6.story) {
                                                                }
                                                                NotificationCompat.Builder autoCancel = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str14).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                                                if (dialogKey6.story) {
                                                                }
                                                                category = autoCancel.setNumber(arrayList14.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle).setContentIntent(activity).extend(wearableExtender).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
                                                                Intent intent4 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                                intent4.putExtra("messageDate", i10);
                                                                intent4.putExtra("dialogId", j11);
                                                                String str25 = str20;
                                                                intent4.putExtra(str25, notificationsController3.currentAccount);
                                                                if (dialogKey6.story) {
                                                                }
                                                                category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent4, 167772160));
                                                                if (z6) {
                                                                }
                                                                if (action != null) {
                                                                }
                                                                if (!z5) {
                                                                }
                                                                if (arrayList4.size() != 1) {
                                                                }
                                                                if (DialogObject.isEncryptedDialog(j11)) {
                                                                }
                                                                if (bitmap3 != null) {
                                                                }
                                                                if (!AndroidUtilities.needShowPasscode(false)) {
                                                                }
                                                                if (tLRPC$Chat2 == null) {
                                                                }
                                                                tLRPC$User5 = tLRPC$User4;
                                                                boolean z16 = z6;
                                                                Notification notification3 = notification;
                                                                if (Build.VERSION.SDK_INT >= 26) {
                                                                }
                                                                long j22 = j11;
                                                                i11 = i26;
                                                                z10 = z16;
                                                                j8 = j16;
                                                                longSparseArray6 = longSparseArray8;
                                                                ArrayList arrayList22 = arrayList2;
                                                                i12 = i8;
                                                                TLRPC$Chat tLRPC$Chat3 = tLRPC$Chat2;
                                                                sharedPreferences = sharedPreferences2;
                                                                longSparseArray7 = longSparseArray5;
                                                                notification2 = notification3;
                                                                arrayList6 = arrayList22;
                                                                arrayList6.add(new 1NotificationHolder(num2.intValue(), j22, dialogKey6.story, j15, str14, tLRPC$User5, tLRPC$Chat3, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                                                notificationsController2 = this;
                                                                notificationsController2.wearNotificationsIds.put(j22, num2);
                                                                i5 = i12 + 1;
                                                                arrayList19 = arrayList6;
                                                                size = i11;
                                                                arrayList16 = arrayList4;
                                                                longSparseArray12 = longSparseArray4;
                                                                z4 = z10;
                                                                notificationsSettings = sharedPreferences;
                                                                longSparseArray13 = longSparseArray7;
                                                                clientUserId = j8;
                                                                longSparseArray = longSparseArray6;
                                                                build3 = notification2;
                                                                i4 = 7;
                                                                notificationsController4 = notificationsController2;
                                                            }
                                                        }
                                                    } catch (Throwable th3) {
                                                        th = th3;
                                                    }
                                                    person = person4;
                                                    boolean z17 = (messageObject == null && (messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest)) ? false : true;
                                                    if (person == null && z17) {
                                                        messagingStyle = new NotificationCompat.MessagingStyle(person);
                                                    } else {
                                                        messagingStyle = new NotificationCompat.MessagingStyle("");
                                                    }
                                                    i14 = Build.VERSION.SDK_INT;
                                                    if (i14 >= 28 || ((DialogObject.isChatDialog(j4) && !z9) || UserObject.isReplyUser(j4))) {
                                                        messagingStyle.setConversationTitle(str12);
                                                    }
                                                    messagingStyle.setGroupConversation(i14 >= 28 || (!z9 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                                                    StringBuilder sb32 = new StringBuilder();
                                                    messageObject2 = messageObject;
                                                    String[] strArr22 = new String[1];
                                                    String str242 = "";
                                                    boolean[] zArr2 = new boolean[1];
                                                    if (!dialogKey4.story) {
                                                        ArrayList<String> arrayList23 = new ArrayList<>();
                                                        ArrayList<Object> arrayList24 = new ArrayList<>();
                                                        Pair<Integer, Boolean> parseStoryPushes = notificationsController3.parseStoryPushes(arrayList23, arrayList24);
                                                        int intValue = ((Integer) parseStoryPushes.first).intValue();
                                                        boolean booleanValue = ((Boolean) parseStoryPushes.second).booleanValue();
                                                        if (booleanValue) {
                                                            sb32.append(LocaleController.formatPluralString("StoryNotificationHidden", intValue, new Object[0]));
                                                        } else if (arrayList23.isEmpty()) {
                                                            longSparseArray6 = longSparseArray;
                                                            j8 = j9;
                                                            notificationsController2 = notificationsController3;
                                                            i11 = i26;
                                                            arrayList6 = arrayList2;
                                                            i12 = i8;
                                                            notification2 = notification;
                                                            sharedPreferences = sharedPreferences2;
                                                            longSparseArray7 = longSparseArray5;
                                                            z10 = z6;
                                                            i5 = i12 + 1;
                                                            arrayList19 = arrayList6;
                                                            size = i11;
                                                            arrayList16 = arrayList4;
                                                            longSparseArray12 = longSparseArray4;
                                                            z4 = z10;
                                                            notificationsSettings = sharedPreferences;
                                                            longSparseArray13 = longSparseArray7;
                                                            clientUserId = j8;
                                                            longSparseArray = longSparseArray6;
                                                            build3 = notification2;
                                                            i4 = 7;
                                                            notificationsController4 = notificationsController2;
                                                        } else if (arrayList23.size() != 1) {
                                                            if (arrayList23.size() == 2) {
                                                                dialogKey5 = dialogKey4;
                                                                sb32.append(LocaleController.formatString(R.string.StoryNotification2, arrayList23.get(0), arrayList23.get(1)));
                                                                longSparseArray11 = longSparseArray;
                                                            } else {
                                                                dialogKey5 = dialogKey4;
                                                                if (arrayList23.size() == 3 && notificationsController3.storyPushMessages.size() == 3) {
                                                                    longSparseArray11 = longSparseArray;
                                                                    sb32.append(LocaleController.formatString(R.string.StoryNotification3, notificationsController3.cutLastName(arrayList23.get(0)), notificationsController3.cutLastName(arrayList23.get(1)), notificationsController3.cutLastName(arrayList23.get(2))));
                                                                } else {
                                                                    longSparseArray11 = longSparseArray;
                                                                    sb32.append(LocaleController.formatPluralString("StoryNotification4", notificationsController3.storyPushMessages.size() - 2, notificationsController3.cutLastName(arrayList23.get(0)), notificationsController3.cutLastName(arrayList23.get(1))));
                                                                }
                                                            }
                                                            long j23 = Long.MAX_VALUE;
                                                            i19 = 0;
                                                            while (i19 < notificationsController3.storyPushMessages.size()) {
                                                                j23 = Math.min(notificationsController3.storyPushMessages.get(i19).date, j23);
                                                                i19++;
                                                                j4 = j4;
                                                            }
                                                            long j24 = j4;
                                                            messagingStyle.setGroupConversation(false);
                                                            String formatPluralString = (arrayList23.size() == 1 || booleanValue) ? LocaleController.formatPluralString(str3, intValue, new Object[0]) : arrayList23.get(0);
                                                            messagingStyle.addMessage(sb32, j23, new Person.Builder().setName(formatPluralString).build());
                                                            Bitmap loadMultipleAvatars = booleanValue ? loadMultipleAvatars(arrayList24) : null;
                                                            sb = sb32;
                                                            arrayList7 = arrayList5;
                                                            longSparseArray8 = longSparseArray11;
                                                            j11 = j24;
                                                            i15 = 0;
                                                            j12 = j9;
                                                            str14 = formatPluralString;
                                                            bitmap2 = loadMultipleAvatars;
                                                            arrayList8 = null;
                                                        } else if (intValue == 1) {
                                                            sb32.append(LocaleController.getString("StoryNotificationSingle"));
                                                        } else {
                                                            sb32.append(LocaleController.formatPluralString("StoryNotification1", intValue, arrayList23.get(0)));
                                                        }
                                                        longSparseArray11 = longSparseArray;
                                                        dialogKey5 = dialogKey4;
                                                        long j232 = Long.MAX_VALUE;
                                                        i19 = 0;
                                                        while (i19 < notificationsController3.storyPushMessages.size()) {
                                                        }
                                                        long j242 = j4;
                                                        messagingStyle.setGroupConversation(false);
                                                        if (arrayList23.size() == 1) {
                                                        }
                                                        messagingStyle.addMessage(sb32, j232, new Person.Builder().setName(formatPluralString).build());
                                                        if (booleanValue) {
                                                        }
                                                        sb = sb32;
                                                        arrayList7 = arrayList5;
                                                        longSparseArray8 = longSparseArray11;
                                                        j11 = j242;
                                                        i15 = 0;
                                                        j12 = j9;
                                                        str14 = formatPluralString;
                                                        bitmap2 = loadMultipleAvatars;
                                                        arrayList8 = null;
                                                    } else {
                                                        LongSparseArray longSparseArray14 = longSparseArray;
                                                        dialogKey5 = dialogKey4;
                                                        long j25 = j4;
                                                        int size4 = arrayList5.size() - 1;
                                                        int i32 = 0;
                                                        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList25 = null;
                                                        while (size4 >= 0) {
                                                            ArrayList<StoryNotification> arrayList26 = arrayList5;
                                                            MessageObject messageObject4 = (MessageObject) arrayList26.get(size4);
                                                            int i33 = i32;
                                                            if (j10 != MessageObject.getTopicId(notificationsController3.currentAccount, messageObject4.messageOwner, getMessagesController().isForum(messageObject4))) {
                                                                i16 = size4;
                                                                z11 = z9;
                                                                sb2 = sb32;
                                                                arrayList9 = arrayList25;
                                                            } else {
                                                                String shortStringForMessage = notificationsController3.getShortStringForMessage(messageObject4, strArr22, zArr2);
                                                                if (j25 == j9) {
                                                                    strArr22[0] = str13;
                                                                } else if (DialogObject.isChatDialog(j25) && messageObject4.messageOwner.from_scheduled) {
                                                                    arrayList9 = arrayList25;
                                                                    strArr22[0] = LocaleController.getString("NotificationMessageScheduledName", R.string.NotificationMessageScheduledName);
                                                                    if (shortStringForMessage != null) {
                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                            FileLog.w("message text is null for " + messageObject4.getId() + " did = " + messageObject4.getDialogId());
                                                                        }
                                                                        i16 = size4;
                                                                        z11 = z9;
                                                                        sb2 = sb32;
                                                                    } else {
                                                                        if (sb32.length() > 0) {
                                                                            sb32.append("\n\n");
                                                                        }
                                                                        if (j25 != j9 && messageObject4.messageOwner.from_scheduled && DialogObject.isUserDialog(j25)) {
                                                                            shortStringForMessage = String.format("%1$s: %2$s", LocaleController.getString("NotificationMessageScheduledName", R.string.NotificationMessageScheduledName), shortStringForMessage);
                                                                            sb32.append(shortStringForMessage);
                                                                        } else if (strArr22[0] != null) {
                                                                            sb32.append(String.format("%1$s: %2$s", strArr22[0], shortStringForMessage));
                                                                        } else {
                                                                            sb32.append(shortStringForMessage);
                                                                        }
                                                                        String str26 = shortStringForMessage;
                                                                        if (DialogObject.isUserDialog(j25)) {
                                                                            sb2 = sb32;
                                                                            j13 = j25;
                                                                            j14 = j9;
                                                                            arrayList10 = arrayList26;
                                                                        } else {
                                                                            if (z9) {
                                                                                sb2 = sb32;
                                                                                j13 = j25;
                                                                                j14 = j9;
                                                                                arrayList10 = arrayList26;
                                                                                senderId = -j13;
                                                                            } else {
                                                                                sb2 = sb32;
                                                                                j13 = j25;
                                                                                j14 = j9;
                                                                                arrayList10 = arrayList26;
                                                                                if (DialogObject.isChatDialog(j13)) {
                                                                                    senderId = messageObject4.getSenderId();
                                                                                }
                                                                            }
                                                                            i16 = size4;
                                                                            arrayList11 = arrayList10;
                                                                            longSparseArray9 = longSparseArray14;
                                                                            person2 = (Person) longSparseArray9.get(senderId + (j10 << 16));
                                                                            if (strArr22[0] != null) {
                                                                                if (!z5) {
                                                                                    str15 = str13;
                                                                                } else if (DialogObject.isChatDialog(j13)) {
                                                                                    if (z9) {
                                                                                        str15 = str13;
                                                                                        if (Build.VERSION.SDK_INT > 27) {
                                                                                            str17 = LocaleController.getString(str9, R.string.NotificationHiddenChatName);
                                                                                        }
                                                                                    } else {
                                                                                        str15 = str13;
                                                                                        str17 = LocaleController.getString("NotificationHiddenChatUserName", R.string.NotificationHiddenChatUserName);
                                                                                    }
                                                                                    str16 = str7;
                                                                                } else {
                                                                                    str15 = str13;
                                                                                    if (Build.VERSION.SDK_INT > 27) {
                                                                                        str16 = str7;
                                                                                        str17 = LocaleController.getString(str16, R.string.NotificationHiddenName);
                                                                                    }
                                                                                }
                                                                                str16 = str7;
                                                                                str17 = str242;
                                                                            } else {
                                                                                str15 = str13;
                                                                                str16 = str7;
                                                                                str17 = strArr22[0];
                                                                            }
                                                                            strArr = strArr22;
                                                                            if (person2 == null && TextUtils.equals(person2.getName(), str17)) {
                                                                                person3 = person2;
                                                                                z11 = z9;
                                                                                str18 = str16;
                                                                                messagingStyle2 = messagingStyle;
                                                                            } else {
                                                                                Person.Builder name3 = new Person.Builder().setName(str17);
                                                                                if (zArr2[0] || DialogObject.isEncryptedDialog(j13) || Build.VERSION.SDK_INT < 28) {
                                                                                    z11 = z9;
                                                                                    str18 = str16;
                                                                                    messagingStyle2 = messagingStyle;
                                                                                } else {
                                                                                    if (DialogObject.isUserDialog(j13) || z9) {
                                                                                        z11 = z9;
                                                                                        str18 = str16;
                                                                                        messagingStyle2 = messagingStyle;
                                                                                        file3 = file2;
                                                                                    } else {
                                                                                        long senderId2 = messageObject4.getSenderId();
                                                                                        z11 = z9;
                                                                                        str18 = str16;
                                                                                        TLRPC$User user4 = getMessagesController().getUser(Long.valueOf(senderId2));
                                                                                        if (user4 == null && (user4 = getMessagesStorage().getUserSync(senderId2)) != null) {
                                                                                            getMessagesController().putUser(user4, true);
                                                                                        }
                                                                                        if (user4 == null || (tLRPC$UserProfilePhoto = user4.photo) == null || (tLRPC$FileLocation6 = tLRPC$UserProfilePhoto.photo_small) == null) {
                                                                                            messagingStyle2 = messagingStyle;
                                                                                        } else {
                                                                                            messagingStyle2 = messagingStyle;
                                                                                            if (tLRPC$FileLocation6.volume_id != 0 && tLRPC$FileLocation6.local_id != 0) {
                                                                                                file3 = getFileLoader().getPathToAttach(user4.photo.photo_small, true);
                                                                                            }
                                                                                        }
                                                                                        file3 = null;
                                                                                    }
                                                                                    loadRoundAvatar(file3, name3);
                                                                                }
                                                                                Person build6 = name3.build();
                                                                                longSparseArray9.put(senderId, build6);
                                                                                person3 = build6;
                                                                            }
                                                                            if (DialogObject.isEncryptedDialog(j13)) {
                                                                                if (!zArr2[0] || Build.VERSION.SDK_INT < 28 || ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).isLowRamDevice() || z5 || messageObject4.isSecretMedia() || !(messageObject4.type == 1 || messageObject4.isSticker())) {
                                                                                    longSparseArray10 = longSparseArray9;
                                                                                    messagingStyle = messagingStyle2;
                                                                                    str19 = str242;
                                                                                } else {
                                                                                    File pathToMessage = getFileLoader().getPathToMessage(messageObject4.messageOwner);
                                                                                    if (pathToMessage.exists() && messageObject4.hasMediaSpoilers()) {
                                                                                        file5 = new File(pathToMessage.getParentFile(), pathToMessage.getName() + ".blur.jpg");
                                                                                        if (file5.exists()) {
                                                                                            file6 = pathToMessage;
                                                                                            longSparseArray10 = longSparseArray9;
                                                                                        } else {
                                                                                            try {
                                                                                                Bitmap decodeFile = BitmapFactory.decodeFile(pathToMessage.getAbsolutePath());
                                                                                                Bitmap stackBlurBitmapMax = Utilities.stackBlurBitmapMax(decodeFile);
                                                                                                decodeFile.recycle();
                                                                                                createScaledBitmap = Bitmap.createScaledBitmap(stackBlurBitmapMax, decodeFile.getWidth(), decodeFile.getHeight(), true);
                                                                                                Utilities.stackBlurBitmap(createScaledBitmap, 5);
                                                                                                stackBlurBitmapMax.recycle();
                                                                                                canvas = new Canvas(createScaledBitmap);
                                                                                                file6 = pathToMessage;
                                                                                            } catch (Exception e) {
                                                                                                e = e;
                                                                                                file6 = pathToMessage;
                                                                                            }
                                                                                            try {
                                                                                                notificationsController3.mediaSpoilerEffect.setColor(ColorUtils.setAlphaComponent(-1, (int) (Color.alpha(-1) * 0.325f)));
                                                                                                longSparseArray10 = longSparseArray9;
                                                                                            } catch (Exception e2) {
                                                                                                e = e2;
                                                                                                longSparseArray10 = longSparseArray9;
                                                                                                FileLog.e(e);
                                                                                                file4 = file6;
                                                                                                NotificationCompat.MessagingStyle.Message message = new NotificationCompat.MessagingStyle.Message(str26, messageObject4.messageOwner.date * 1000, person3);
                                                                                                String str27 = messageObject4.isSticker() ? "image/webp" : "image/jpeg";
                                                                                                if (file4.exists()) {
                                                                                                }
                                                                                                if (uriForFile != null) {
                                                                                                }
                                                                                            }
                                                                                            try {
                                                                                                notificationsController3.mediaSpoilerEffect.setBounds(0, 0, createScaledBitmap.getWidth(), createScaledBitmap.getHeight());
                                                                                                notificationsController3.mediaSpoilerEffect.draw(canvas);
                                                                                                FileOutputStream fileOutputStream = new FileOutputStream(file5);
                                                                                                createScaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                                                                                                fileOutputStream.close();
                                                                                                createScaledBitmap.recycle();
                                                                                                file4 = file5;
                                                                                            } catch (Exception e3) {
                                                                                                e = e3;
                                                                                                FileLog.e(e);
                                                                                                file4 = file6;
                                                                                                NotificationCompat.MessagingStyle.Message message2 = new NotificationCompat.MessagingStyle.Message(str26, messageObject4.messageOwner.date * 1000, person3);
                                                                                                String str272 = messageObject4.isSticker() ? "image/webp" : "image/jpeg";
                                                                                                if (file4.exists()) {
                                                                                                }
                                                                                                if (uriForFile != null) {
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        file4 = file6;
                                                                                    } else {
                                                                                        longSparseArray10 = longSparseArray9;
                                                                                        file4 = pathToMessage;
                                                                                        file5 = null;
                                                                                    }
                                                                                    NotificationCompat.MessagingStyle.Message message22 = new NotificationCompat.MessagingStyle.Message(str26, messageObject4.messageOwner.date * 1000, person3);
                                                                                    String str2722 = messageObject4.isSticker() ? "image/webp" : "image/jpeg";
                                                                                    if (file4.exists()) {
                                                                                        try {
                                                                                            uriForFile = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", file4);
                                                                                            str19 = str242;
                                                                                        } catch (Exception e4) {
                                                                                            FileLog.e(e4);
                                                                                        }
                                                                                    } else {
                                                                                        if (getFileLoader().isLoadingFile(file4.getName())) {
                                                                                            Uri.Builder appendPath = new Uri.Builder().scheme("content").authority(NotificationImageProvider.getAuthority()).appendPath("msg_media_raw");
                                                                                            StringBuilder sb6 = new StringBuilder();
                                                                                            sb6.append(notificationsController3.currentAccount);
                                                                                            str19 = str242;
                                                                                            sb6.append(str19);
                                                                                            uriForFile = appendPath.appendPath(sb6.toString()).appendPath(file4.getName()).appendQueryParameter("final_path", file4.getAbsolutePath()).build();
                                                                                        }
                                                                                        str19 = str242;
                                                                                        uriForFile = null;
                                                                                    }
                                                                                    if (uriForFile != null) {
                                                                                        message22.setData(str2722, uriForFile);
                                                                                        messagingStyle = messagingStyle2;
                                                                                        messagingStyle.addMessage(message22);
                                                                                        ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile, 1);
                                                                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda4
                                                                                            @Override // java.lang.Runnable
                                                                                            public final void run() {
                                                                                                NotificationsController.lambda$showExtraNotifications$42(uriForFile, file5);
                                                                                            }
                                                                                        }, 20000L);
                                                                                        if (!TextUtils.isEmpty(messageObject4.caption)) {
                                                                                            messagingStyle.addMessage(messageObject4.caption, messageObject4.messageOwner.date * 1000, person3);
                                                                                        }
                                                                                        z12 = true;
                                                                                        if (!z12) {
                                                                                            messagingStyle.addMessage(str26, messageObject4.messageOwner.date * 1000, person3);
                                                                                        }
                                                                                        if (zArr2[0] && !z5 && messageObject4.isVoice()) {
                                                                                            messages = messagingStyle.getMessages();
                                                                                            if (!messages.isEmpty()) {
                                                                                                File pathToMessage2 = getFileLoader().getPathToMessage(messageObject4.messageOwner);
                                                                                                if (Build.VERSION.SDK_INT >= 24) {
                                                                                                    try {
                                                                                                        uri2 = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", pathToMessage2);
                                                                                                    } catch (Exception unused2) {
                                                                                                        uri2 = null;
                                                                                                    }
                                                                                                } else {
                                                                                                    uri2 = Uri.fromFile(pathToMessage2);
                                                                                                }
                                                                                                if (uri2 != null) {
                                                                                                    messages.get(messages.size() - 1).setData("audio/ogg", uri2);
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        messagingStyle = messagingStyle2;
                                                                                    }
                                                                                }
                                                                                z12 = false;
                                                                                if (!z12) {
                                                                                }
                                                                                if (zArr2[0]) {
                                                                                    messages = messagingStyle.getMessages();
                                                                                    if (!messages.isEmpty()) {
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                longSparseArray10 = longSparseArray9;
                                                                                messagingStyle = messagingStyle2;
                                                                                str19 = str242;
                                                                                messagingStyle.addMessage(str26, messageObject4.messageOwner.date * 1000, person3);
                                                                            }
                                                                            if (j13 == 777000 && (tLRPC$ReplyMarkup = messageObject4.messageOwner.reply_markup) != null) {
                                                                                arrayList12 = tLRPC$ReplyMarkup.rows;
                                                                                i32 = messageObject4.getId();
                                                                                size4 = i16 - 1;
                                                                                str242 = str19;
                                                                                strArr22 = strArr;
                                                                                str13 = str15;
                                                                                j9 = j14;
                                                                                arrayList5 = arrayList11;
                                                                                z9 = z11;
                                                                                str7 = str18;
                                                                                longSparseArray14 = longSparseArray10;
                                                                                j25 = j13;
                                                                                sb32 = sb2;
                                                                                arrayList25 = arrayList12;
                                                                            }
                                                                            i32 = i33;
                                                                            arrayList12 = arrayList9;
                                                                            size4 = i16 - 1;
                                                                            str242 = str19;
                                                                            strArr22 = strArr;
                                                                            str13 = str15;
                                                                            j9 = j14;
                                                                            arrayList5 = arrayList11;
                                                                            z9 = z11;
                                                                            str7 = str18;
                                                                            longSparseArray14 = longSparseArray10;
                                                                            j25 = j13;
                                                                            sb32 = sb2;
                                                                            arrayList25 = arrayList12;
                                                                        }
                                                                        senderId = j13;
                                                                        i16 = size4;
                                                                        arrayList11 = arrayList10;
                                                                        longSparseArray9 = longSparseArray14;
                                                                        person2 = (Person) longSparseArray9.get(senderId + (j10 << 16));
                                                                        if (strArr22[0] != null) {
                                                                        }
                                                                        strArr = strArr22;
                                                                        if (person2 == null) {
                                                                        }
                                                                        Person.Builder name32 = new Person.Builder().setName(str17);
                                                                        if (zArr2[0]) {
                                                                        }
                                                                        z11 = z9;
                                                                        str18 = str16;
                                                                        messagingStyle2 = messagingStyle;
                                                                        Person build62 = name32.build();
                                                                        longSparseArray9.put(senderId, build62);
                                                                        person3 = build62;
                                                                        if (DialogObject.isEncryptedDialog(j13)) {
                                                                        }
                                                                        if (j13 == 777000) {
                                                                            arrayList12 = tLRPC$ReplyMarkup.rows;
                                                                            i32 = messageObject4.getId();
                                                                            size4 = i16 - 1;
                                                                            str242 = str19;
                                                                            strArr22 = strArr;
                                                                            str13 = str15;
                                                                            j9 = j14;
                                                                            arrayList5 = arrayList11;
                                                                            z9 = z11;
                                                                            str7 = str18;
                                                                            longSparseArray14 = longSparseArray10;
                                                                            j25 = j13;
                                                                            sb32 = sb2;
                                                                            arrayList25 = arrayList12;
                                                                        }
                                                                        i32 = i33;
                                                                        arrayList12 = arrayList9;
                                                                        size4 = i16 - 1;
                                                                        str242 = str19;
                                                                        strArr22 = strArr;
                                                                        str13 = str15;
                                                                        j9 = j14;
                                                                        arrayList5 = arrayList11;
                                                                        z9 = z11;
                                                                        str7 = str18;
                                                                        longSparseArray14 = longSparseArray10;
                                                                        j25 = j13;
                                                                        sb32 = sb2;
                                                                        arrayList25 = arrayList12;
                                                                    }
                                                                }
                                                                arrayList9 = arrayList25;
                                                                if (shortStringForMessage != null) {
                                                                }
                                                            }
                                                            arrayList11 = arrayList26;
                                                            str18 = str7;
                                                            longSparseArray10 = longSparseArray14;
                                                            j13 = j25;
                                                            j14 = j9;
                                                            strArr = strArr22;
                                                            str15 = str13;
                                                            str19 = str242;
                                                            i32 = i33;
                                                            arrayList12 = arrayList9;
                                                            size4 = i16 - 1;
                                                            str242 = str19;
                                                            strArr22 = strArr;
                                                            str13 = str15;
                                                            j9 = j14;
                                                            arrayList5 = arrayList11;
                                                            z9 = z11;
                                                            str7 = str18;
                                                            longSparseArray14 = longSparseArray10;
                                                            j25 = j13;
                                                            sb32 = sb2;
                                                            arrayList25 = arrayList12;
                                                        }
                                                        sb = sb32;
                                                        arrayList7 = arrayList5;
                                                        longSparseArray8 = longSparseArray14;
                                                        j11 = j25;
                                                        j12 = j9;
                                                        i15 = i32;
                                                        bitmap2 = bitmap6;
                                                        str14 = str13;
                                                        arrayList8 = arrayList25;
                                                    }
                                                    Intent intent22 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                    intent22.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                    intent22.setFlags(ConnectionsManager.FileTypeFile);
                                                    intent22.addCategory("android.intent.category.LAUNCHER");
                                                    if (messageObject2 == null && messageObject2.isStoryReactionPush) {
                                                        intent22.putExtra("storyId", Math.abs(messageObject2.getId()));
                                                        dialogKey6 = dialogKey5;
                                                    } else {
                                                        dialogKey6 = dialogKey5;
                                                        if (!dialogKey6.story) {
                                                            long[] jArr2 = new long[notificationsController3.storyPushMessages.size()];
                                                            for (int i34 = 0; i34 < notificationsController3.storyPushMessages.size(); i34++) {
                                                                jArr2[i34] = notificationsController3.storyPushMessages.get(i34).dialogId;
                                                            }
                                                            intent22.putExtra("storyDialogIds", jArr2);
                                                        } else if (DialogObject.isEncryptedDialog(j11)) {
                                                            intent22.putExtra("encId", DialogObject.getEncryptedChatId(j11));
                                                        } else if (DialogObject.isUserDialog(j11)) {
                                                            intent22.putExtra("userId", j11);
                                                        } else {
                                                            intent22.putExtra("chatId", -j11);
                                                        }
                                                    }
                                                    StringBuilder sb42 = new StringBuilder();
                                                    sb42.append("show extra notifications chatId ");
                                                    sb42.append(j11);
                                                    sb42.append(" topicId ");
                                                    j15 = j10;
                                                    sb42.append(j15);
                                                    FileLog.d(sb42.toString());
                                                    if (j15 != 0) {
                                                        intent22.putExtra("topicId", j15);
                                                    }
                                                    intent22.putExtra("currentAccount", notificationsController3.currentAccount);
                                                    PendingIntent activity2 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22, 1140850688);
                                                    NotificationCompat.WearableExtender wearableExtender2 = new NotificationCompat.WearableExtender();
                                                    if (action != null) {
                                                        wearableExtender2.addAction(action);
                                                    }
                                                    int i302 = i15;
                                                    arrayList13 = arrayList8;
                                                    Intent intent32 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                                    intent32.addFlags(32);
                                                    intent32.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                                    intent32.putExtra(str11, j11);
                                                    int i312 = i13;
                                                    intent32.putExtra(str10, i312);
                                                    intent32.putExtra("currentAccount", notificationsController3.currentAccount);
                                                    bitmap3 = bitmap2;
                                                    build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent32, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                                    if (DialogObject.isEncryptedDialog(j11)) {
                                                        if (DialogObject.isUserDialog(j11)) {
                                                            str21 = "tguser" + j11 + "_" + i312;
                                                            str20 = "currentAccount";
                                                        } else {
                                                            StringBuilder sb7 = new StringBuilder();
                                                            sb7.append("tgchat");
                                                            str20 = "currentAccount";
                                                            sb7.append(-j11);
                                                            sb7.append("_");
                                                            sb7.append(i312);
                                                            str21 = sb7.toString();
                                                        }
                                                    } else {
                                                        str20 = "currentAccount";
                                                        str21 = j11 != globalSecretChatId ? "tgenc" + DialogObject.getEncryptedChatId(j11) + "_" + i312 : null;
                                                    }
                                                    if (str21 == null) {
                                                        wearableExtender2.setDismissalId(str21);
                                                        NotificationCompat.WearableExtender wearableExtender3 = new NotificationCompat.WearableExtender();
                                                        wearableExtender3.setDismissalId("summary_" + str21);
                                                        builder.extend(wearableExtender3);
                                                    }
                                                    StringBuilder sb52 = new StringBuilder();
                                                    sb52.append("tgaccount");
                                                    long j212 = j12;
                                                    sb52.append(j212);
                                                    wearableExtender2.setBridgeTag(sb52.toString());
                                                    if (!dialogKey6.story) {
                                                        j16 = j212;
                                                        j17 = Long.MAX_VALUE;
                                                        for (int i35 = 0; i35 < notificationsController3.storyPushMessages.size(); i35++) {
                                                            j17 = Math.min(notificationsController3.storyPushMessages.get(i35).date, j17);
                                                        }
                                                        arrayList14 = arrayList7;
                                                    } else {
                                                        j16 = j212;
                                                        arrayList14 = arrayList7;
                                                        j17 = ((MessageObject) arrayList14.get(0)).messageOwner.date * 1000;
                                                    }
                                                    NotificationCompat.Builder autoCancel2 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str14).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                                    if (dialogKey6.story) {
                                                        arrayList14 = notificationsController3.storyPushMessages;
                                                    }
                                                    category = autoCancel2.setNumber(arrayList14.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle).setContentIntent(activity2).extend(wearableExtender2).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
                                                    Intent intent42 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                    intent42.putExtra("messageDate", i10);
                                                    intent42.putExtra("dialogId", j11);
                                                    String str252 = str20;
                                                    intent42.putExtra(str252, notificationsController3.currentAccount);
                                                    if (dialogKey6.story) {
                                                        intent42.putExtra("story", true);
                                                    }
                                                    category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent42, 167772160));
                                                    if (z6) {
                                                        category.setGroup(notificationsController3.notificationGroup);
                                                        category.setGroupAlertBehavior(1);
                                                    }
                                                    if (action != null) {
                                                        category.addAction(action);
                                                    }
                                                    if (!z5 && !dialogKey6.story && (messageObject2 == null || !messageObject2.isStoryReactionPush)) {
                                                        category.addAction(build2);
                                                    }
                                                    if (arrayList4.size() != 1 && !TextUtils.isEmpty(str) && !dialogKey6.story) {
                                                        category.setSubText(str);
                                                    }
                                                    if (DialogObject.isEncryptedDialog(j11)) {
                                                        category.setLocalOnly(true);
                                                    }
                                                    if (bitmap3 != null) {
                                                        category.setLargeIcon(bitmap3);
                                                    }
                                                    if (!AndroidUtilities.needShowPasscode(false) && !SharedConfig.isWaitingForPasscodeEnter && arrayList13 != null) {
                                                        size3 = arrayList13.size();
                                                        i17 = 0;
                                                        while (i17 < size3) {
                                                            ArrayList<TLRPC$TL_keyboardButtonRow> arrayList27 = arrayList13;
                                                            TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow = arrayList27.get(i17);
                                                            int size5 = tLRPC$TL_keyboardButtonRow.buttons.size();
                                                            int i36 = 0;
                                                            while (i36 < size5) {
                                                                TLRPC$KeyboardButton tLRPC$KeyboardButton = tLRPC$TL_keyboardButtonRow.buttons.get(i36);
                                                                if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonCallback) {
                                                                    i18 = size3;
                                                                    arrayList15 = arrayList27;
                                                                    Intent intent5 = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                                                    intent5.putExtra(str252, notificationsController3.currentAccount);
                                                                    intent5.putExtra("did", j11);
                                                                    byte[] bArr = tLRPC$KeyboardButton.data;
                                                                    if (bArr != null) {
                                                                        intent5.putExtra("data", bArr);
                                                                    }
                                                                    intent5.putExtra("mid", i302);
                                                                    String str28 = tLRPC$KeyboardButton.text;
                                                                    Context context = ApplicationLoader.applicationContext;
                                                                    int i37 = notificationsController3.lastButtonId;
                                                                    str22 = str252;
                                                                    notificationsController3.lastButtonId = i37 + 1;
                                                                    category.addAction(0, str28, PendingIntent.getBroadcast(context, i37, intent5, 167772160));
                                                                } else {
                                                                    i18 = size3;
                                                                    arrayList15 = arrayList27;
                                                                    str22 = str252;
                                                                }
                                                                i36++;
                                                                size3 = i18;
                                                                arrayList27 = arrayList15;
                                                                str252 = str22;
                                                            }
                                                            i17++;
                                                            arrayList13 = arrayList27;
                                                            str252 = str252;
                                                        }
                                                    }
                                                    if (tLRPC$Chat2 == null || tLRPC$User4 == null) {
                                                        tLRPC$User5 = tLRPC$User4;
                                                    } else {
                                                        tLRPC$User5 = tLRPC$User4;
                                                        String str29 = tLRPC$User5.phone;
                                                        if (str29 != null && str29.length() > 0) {
                                                            category.addPerson("tel:+" + tLRPC$User5.phone);
                                                        }
                                                    }
                                                    boolean z162 = z6;
                                                    Notification notification32 = notification;
                                                    if (Build.VERSION.SDK_INT >= 26) {
                                                        notificationsController3.setNotificationChannel(notification32, category, z162);
                                                    }
                                                    long j222 = j11;
                                                    i11 = i26;
                                                    z10 = z162;
                                                    j8 = j16;
                                                    longSparseArray6 = longSparseArray8;
                                                    ArrayList arrayList222 = arrayList2;
                                                    i12 = i8;
                                                    TLRPC$Chat tLRPC$Chat32 = tLRPC$Chat2;
                                                    sharedPreferences = sharedPreferences2;
                                                    longSparseArray7 = longSparseArray5;
                                                    notification2 = notification32;
                                                    arrayList6 = arrayList222;
                                                    arrayList6.add(new 1NotificationHolder(num2.intValue(), j222, dialogKey6.story, j15, str14, tLRPC$User5, tLRPC$Chat32, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                                    notificationsController2 = this;
                                                    notificationsController2.wearNotificationsIds.put(j222, num2);
                                                    i5 = i12 + 1;
                                                    arrayList19 = arrayList6;
                                                    size = i11;
                                                    arrayList16 = arrayList4;
                                                    longSparseArray12 = longSparseArray4;
                                                    z4 = z10;
                                                    notificationsSettings = sharedPreferences;
                                                    longSparseArray13 = longSparseArray7;
                                                    clientUserId = j8;
                                                    longSparseArray = longSparseArray6;
                                                    build3 = notification2;
                                                    i4 = 7;
                                                    notificationsController4 = notificationsController2;
                                                }
                                            }
                                        }
                                        j10 = j7;
                                        person = person4;
                                        if (messageObject == null) {
                                        }
                                        if (person == null) {
                                        }
                                        messagingStyle = new NotificationCompat.MessagingStyle("");
                                        i14 = Build.VERSION.SDK_INT;
                                        if (i14 >= 28) {
                                        }
                                        messagingStyle.setConversationTitle(str12);
                                        messagingStyle.setGroupConversation(i14 >= 28 || (!z9 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                                        StringBuilder sb322 = new StringBuilder();
                                        messageObject2 = messageObject;
                                        String[] strArr222 = new String[1];
                                        String str2422 = "";
                                        boolean[] zArr22 = new boolean[1];
                                        if (!dialogKey4.story) {
                                        }
                                        Intent intent222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                        intent222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                        intent222.setFlags(ConnectionsManager.FileTypeFile);
                                        intent222.addCategory("android.intent.category.LAUNCHER");
                                        if (messageObject2 == null) {
                                        }
                                        dialogKey6 = dialogKey5;
                                        if (!dialogKey6.story) {
                                        }
                                        StringBuilder sb422 = new StringBuilder();
                                        sb422.append("show extra notifications chatId ");
                                        sb422.append(j11);
                                        sb422.append(" topicId ");
                                        j15 = j10;
                                        sb422.append(j15);
                                        FileLog.d(sb422.toString());
                                        if (j15 != 0) {
                                        }
                                        intent222.putExtra("currentAccount", notificationsController3.currentAccount);
                                        PendingIntent activity22 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222, 1140850688);
                                        NotificationCompat.WearableExtender wearableExtender22 = new NotificationCompat.WearableExtender();
                                        if (action != null) {
                                        }
                                        int i3022 = i15;
                                        arrayList13 = arrayList8;
                                        Intent intent322 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                        intent322.addFlags(32);
                                        intent322.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                        intent322.putExtra(str11, j11);
                                        int i3122 = i13;
                                        intent322.putExtra(str10, i3122);
                                        intent322.putExtra("currentAccount", notificationsController3.currentAccount);
                                        bitmap3 = bitmap2;
                                        build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent322, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                        if (DialogObject.isEncryptedDialog(j11)) {
                                        }
                                        if (str21 == null) {
                                        }
                                        StringBuilder sb522 = new StringBuilder();
                                        sb522.append("tgaccount");
                                        long j2122 = j12;
                                        sb522.append(j2122);
                                        wearableExtender22.setBridgeTag(sb522.toString());
                                        if (!dialogKey6.story) {
                                        }
                                        NotificationCompat.Builder autoCancel22 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str14).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                        if (dialogKey6.story) {
                                        }
                                        category = autoCancel22.setNumber(arrayList14.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle).setContentIntent(activity22).extend(wearableExtender22).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
                                        Intent intent422 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                        intent422.putExtra("messageDate", i10);
                                        intent422.putExtra("dialogId", j11);
                                        String str2522 = str20;
                                        intent422.putExtra(str2522, notificationsController3.currentAccount);
                                        if (dialogKey6.story) {
                                        }
                                        category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent422, 167772160));
                                        if (z6) {
                                        }
                                        if (action != null) {
                                        }
                                        if (!z5) {
                                            category.addAction(build2);
                                        }
                                        if (arrayList4.size() != 1) {
                                        }
                                        if (DialogObject.isEncryptedDialog(j11)) {
                                        }
                                        if (bitmap3 != null) {
                                        }
                                        if (!AndroidUtilities.needShowPasscode(false)) {
                                            size3 = arrayList13.size();
                                            i17 = 0;
                                            while (i17 < size3) {
                                            }
                                        }
                                        if (tLRPC$Chat2 == null) {
                                        }
                                        tLRPC$User5 = tLRPC$User4;
                                        boolean z1622 = z6;
                                        Notification notification322 = notification;
                                        if (Build.VERSION.SDK_INT >= 26) {
                                        }
                                        long j2222 = j11;
                                        i11 = i26;
                                        z10 = z1622;
                                        j8 = j16;
                                        longSparseArray6 = longSparseArray8;
                                        ArrayList arrayList2222 = arrayList2;
                                        i12 = i8;
                                        TLRPC$Chat tLRPC$Chat322 = tLRPC$Chat2;
                                        sharedPreferences = sharedPreferences2;
                                        longSparseArray7 = longSparseArray5;
                                        notification2 = notification322;
                                        arrayList6 = arrayList2222;
                                        arrayList6.add(new 1NotificationHolder(num2.intValue(), j2222, dialogKey6.story, j15, str14, tLRPC$User5, tLRPC$Chat322, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                        notificationsController2 = this;
                                        notificationsController2.wearNotificationsIds.put(j2222, num2);
                                        i5 = i12 + 1;
                                        arrayList19 = arrayList6;
                                        size = i11;
                                        arrayList16 = arrayList4;
                                        longSparseArray12 = longSparseArray4;
                                        z4 = z10;
                                        notificationsSettings = sharedPreferences;
                                        longSparseArray13 = longSparseArray7;
                                        clientUserId = j8;
                                        longSparseArray = longSparseArray6;
                                        build3 = notification2;
                                        i4 = 7;
                                        notificationsController4 = notificationsController2;
                                    }
                                } else {
                                    str4 = string;
                                }
                                str5 = str4;
                                if (z5) {
                                }
                                if (tLRPC$FileLocation5 == null) {
                                }
                                if (tLRPC$Chat == null) {
                                }
                                Bitmap bitmap62 = bitmap;
                                if (z9) {
                                }
                                file2 = file;
                                str9 = str8;
                                Intent intent6 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                                intent6.putExtra("dialog_id", j4);
                                intent6.putExtra("max_id", i28);
                                intent6.putExtra("topic_id", j7);
                                notificationsController3 = this;
                                tLRPC$Chat2 = tLRPC$Chat;
                                intent6.putExtra("currentAccount", notificationsController3.currentAccount);
                                str10 = "max_id";
                                i13 = i28;
                                PendingIntent broadcast2 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent6, 167772160);
                                RemoteInput build42 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                                if (!DialogObject.isChatDialog(j4)) {
                                }
                                build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast2).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build42).setShowsUserInterface(false).build();
                                num3 = notificationsController3.pushDialogs.get(j4);
                                if (num3 == null) {
                                }
                                dialogKey4 = dialogKey3;
                                if (!dialogKey4.story) {
                                }
                                if (max > 1) {
                                }
                                str12 = str6;
                                str13 = str12;
                                j9 = j6;
                                Person person42 = (Person) longSparseArray.get(j9);
                                action = build;
                                if (Build.VERSION.SDK_INT >= 28) {
                                    user = getMessagesController().getUser(Long.valueOf(j9));
                                    if (user == null) {
                                    }
                                    if (user != null) {
                                    }
                                }
                                j10 = j7;
                                person = person42;
                                if (messageObject == null) {
                                }
                                if (person == null) {
                                }
                                messagingStyle = new NotificationCompat.MessagingStyle("");
                                i14 = Build.VERSION.SDK_INT;
                                if (i14 >= 28) {
                                }
                                messagingStyle.setConversationTitle(str12);
                                messagingStyle.setGroupConversation(i14 >= 28 || (!z9 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                                StringBuilder sb3222 = new StringBuilder();
                                messageObject2 = messageObject;
                                String[] strArr2222 = new String[1];
                                String str24222 = "";
                                boolean[] zArr222 = new boolean[1];
                                if (!dialogKey4.story) {
                                }
                                Intent intent2222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                intent2222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                intent2222.setFlags(ConnectionsManager.FileTypeFile);
                                intent2222.addCategory("android.intent.category.LAUNCHER");
                                if (messageObject2 == null) {
                                }
                                dialogKey6 = dialogKey5;
                                if (!dialogKey6.story) {
                                }
                                StringBuilder sb4222 = new StringBuilder();
                                sb4222.append("show extra notifications chatId ");
                                sb4222.append(j11);
                                sb4222.append(" topicId ");
                                j15 = j10;
                                sb4222.append(j15);
                                FileLog.d(sb4222.toString());
                                if (j15 != 0) {
                                }
                                intent2222.putExtra("currentAccount", notificationsController3.currentAccount);
                                PendingIntent activity222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2222, 1140850688);
                                NotificationCompat.WearableExtender wearableExtender222 = new NotificationCompat.WearableExtender();
                                if (action != null) {
                                }
                                int i30222 = i15;
                                arrayList13 = arrayList8;
                                Intent intent3222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                intent3222.addFlags(32);
                                intent3222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                intent3222.putExtra(str11, j11);
                                int i31222 = i13;
                                intent3222.putExtra(str10, i31222);
                                intent3222.putExtra("currentAccount", notificationsController3.currentAccount);
                                bitmap3 = bitmap2;
                                build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent3222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                if (DialogObject.isEncryptedDialog(j11)) {
                                }
                                if (str21 == null) {
                                }
                                StringBuilder sb5222 = new StringBuilder();
                                sb5222.append("tgaccount");
                                long j21222 = j12;
                                sb5222.append(j21222);
                                wearableExtender222.setBridgeTag(sb5222.toString());
                                if (!dialogKey6.story) {
                                }
                                NotificationCompat.Builder autoCancel222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str14).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                if (dialogKey6.story) {
                                }
                                category = autoCancel222.setNumber(arrayList14.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle).setContentIntent(activity222).extend(wearableExtender222).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
                                Intent intent4222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                intent4222.putExtra("messageDate", i10);
                                intent4222.putExtra("dialogId", j11);
                                String str25222 = str20;
                                intent4222.putExtra(str25222, notificationsController3.currentAccount);
                                if (dialogKey6.story) {
                                }
                                category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent4222, 167772160));
                                if (z6) {
                                }
                                if (action != null) {
                                }
                                if (!z5) {
                                }
                                if (arrayList4.size() != 1) {
                                }
                                if (DialogObject.isEncryptedDialog(j11)) {
                                }
                                if (bitmap3 != null) {
                                }
                                if (!AndroidUtilities.needShowPasscode(false)) {
                                }
                                if (tLRPC$Chat2 == null) {
                                }
                                tLRPC$User5 = tLRPC$User4;
                                boolean z16222 = z6;
                                Notification notification3222 = notification;
                                if (Build.VERSION.SDK_INT >= 26) {
                                }
                                long j22222 = j11;
                                i11 = i26;
                                z10 = z16222;
                                j8 = j16;
                                longSparseArray6 = longSparseArray8;
                                ArrayList arrayList22222 = arrayList2;
                                i12 = i8;
                                TLRPC$Chat tLRPC$Chat3222 = tLRPC$Chat2;
                                sharedPreferences = sharedPreferences2;
                                longSparseArray7 = longSparseArray5;
                                notification2 = notification3222;
                                arrayList6 = arrayList22222;
                                arrayList6.add(new 1NotificationHolder(num2.intValue(), j22222, dialogKey6.story, j15, str14, tLRPC$User5, tLRPC$Chat3222, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                notificationsController2 = this;
                                notificationsController2.wearNotificationsIds.put(j22222, num2);
                                i5 = i12 + 1;
                                arrayList19 = arrayList6;
                                size = i11;
                                arrayList16 = arrayList4;
                                longSparseArray12 = longSparseArray4;
                                z4 = z10;
                                notificationsSettings = sharedPreferences;
                                longSparseArray13 = longSparseArray7;
                                clientUserId = j8;
                                longSparseArray = longSparseArray6;
                                build3 = notification2;
                                i4 = 7;
                                notificationsController4 = notificationsController2;
                            }
                            longSparseArray6 = longSparseArray;
                            j8 = clientUserId;
                            sharedPreferences = notificationsSettings;
                            notificationsController2 = notificationsController4;
                            i11 = i26;
                            arrayList6 = arrayList2;
                            i12 = i8;
                            notification2 = notification;
                            longSparseArray7 = longSparseArray5;
                            z10 = z6;
                            i5 = i12 + 1;
                            arrayList19 = arrayList6;
                            size = i11;
                            arrayList16 = arrayList4;
                            longSparseArray12 = longSparseArray4;
                            z4 = z10;
                            notificationsSettings = sharedPreferences;
                            longSparseArray13 = longSparseArray7;
                            clientUserId = j8;
                            longSparseArray = longSparseArray6;
                            build3 = notification2;
                            i4 = 7;
                            notificationsController4 = notificationsController2;
                        } else {
                            arrayList5 = arrayList3;
                            j6 = clientUserId;
                            dialogKey2 = dialogKey;
                            num2 = num;
                            j7 = j5;
                            if (j4 != globalSecretChatId) {
                                int encryptedChatId = DialogObject.getEncryptedChatId(j4);
                                TLRPC$EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(encryptedChatId));
                                if (encryptedChat == null) {
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.w("not found secret chat to show dialog notification " + encryptedChatId);
                                    }
                                } else {
                                    tLRPC$User = getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
                                    if (tLRPC$User == null) {
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.w("not found secret chat user to show dialog notification " + encryptedChat.user_id);
                                        }
                                    }
                                }
                                notificationsController2 = this;
                                longSparseArray6 = longSparseArray;
                                sharedPreferences = notificationsSettings;
                                i11 = i26;
                                arrayList6 = arrayList2;
                                i12 = i8;
                                notification2 = notification;
                                longSparseArray7 = longSparseArray5;
                                j8 = j6;
                                z10 = z6;
                                i5 = i12 + 1;
                                arrayList19 = arrayList6;
                                size = i11;
                                arrayList16 = arrayList4;
                                longSparseArray12 = longSparseArray4;
                                z4 = z10;
                                notificationsSettings = sharedPreferences;
                                longSparseArray13 = longSparseArray7;
                                clientUserId = j8;
                                longSparseArray = longSparseArray6;
                                build3 = notification2;
                                i4 = 7;
                                notificationsController4 = notificationsController2;
                            } else {
                                tLRPC$User = null;
                            }
                            string = LocaleController.getString("SecretChatName", R.string.SecretChatName);
                            tLRPC$User2 = tLRPC$User;
                            z7 = false;
                            z9 = false;
                            tLRPC$FileLocation3 = null;
                        }
                    }
                    tLRPC$Chat = null;
                    z8 = false;
                    boolean z152 = z7;
                    if (messageObject == null) {
                    }
                    str4 = string;
                    str5 = str4;
                    if (z5) {
                    }
                    if (tLRPC$FileLocation5 == null) {
                    }
                    if (tLRPC$Chat == null) {
                    }
                    Bitmap bitmap622 = bitmap;
                    if (z9) {
                    }
                    file2 = file;
                    str9 = str8;
                    Intent intent62 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                    intent62.putExtra("dialog_id", j4);
                    intent62.putExtra("max_id", i28);
                    intent62.putExtra("topic_id", j7);
                    notificationsController3 = this;
                    tLRPC$Chat2 = tLRPC$Chat;
                    intent62.putExtra("currentAccount", notificationsController3.currentAccount);
                    str10 = "max_id";
                    i13 = i28;
                    PendingIntent broadcast22 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent62, 167772160);
                    RemoteInput build422 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                    if (!DialogObject.isChatDialog(j4)) {
                    }
                    build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast22).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build422).setShowsUserInterface(false).build();
                    num3 = notificationsController3.pushDialogs.get(j4);
                    if (num3 == null) {
                    }
                    dialogKey4 = dialogKey3;
                    if (!dialogKey4.story) {
                    }
                    if (max > 1) {
                    }
                    str12 = str6;
                    str13 = str12;
                    j9 = j6;
                    Person person422 = (Person) longSparseArray.get(j9);
                    action = build;
                    if (Build.VERSION.SDK_INT >= 28) {
                    }
                    j10 = j7;
                    person = person422;
                    if (messageObject == null) {
                    }
                    if (person == null) {
                    }
                    messagingStyle = new NotificationCompat.MessagingStyle("");
                    i14 = Build.VERSION.SDK_INT;
                    if (i14 >= 28) {
                    }
                    messagingStyle.setConversationTitle(str12);
                    messagingStyle.setGroupConversation(i14 >= 28 || (!z9 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                    StringBuilder sb32222 = new StringBuilder();
                    messageObject2 = messageObject;
                    String[] strArr22222 = new String[1];
                    String str242222 = "";
                    boolean[] zArr2222 = new boolean[1];
                    if (!dialogKey4.story) {
                    }
                    Intent intent22222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    intent22222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                    intent22222.setFlags(ConnectionsManager.FileTypeFile);
                    intent22222.addCategory("android.intent.category.LAUNCHER");
                    if (messageObject2 == null) {
                    }
                    dialogKey6 = dialogKey5;
                    if (!dialogKey6.story) {
                    }
                    StringBuilder sb42222 = new StringBuilder();
                    sb42222.append("show extra notifications chatId ");
                    sb42222.append(j11);
                    sb42222.append(" topicId ");
                    j15 = j10;
                    sb42222.append(j15);
                    FileLog.d(sb42222.toString());
                    if (j15 != 0) {
                    }
                    intent22222.putExtra("currentAccount", notificationsController3.currentAccount);
                    PendingIntent activity2222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22222, 1140850688);
                    NotificationCompat.WearableExtender wearableExtender2222 = new NotificationCompat.WearableExtender();
                    if (action != null) {
                    }
                    int i302222 = i15;
                    arrayList13 = arrayList8;
                    Intent intent32222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                    intent32222.addFlags(32);
                    intent32222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                    intent32222.putExtra(str11, j11);
                    int i312222 = i13;
                    intent32222.putExtra(str10, i312222);
                    intent32222.putExtra("currentAccount", notificationsController3.currentAccount);
                    bitmap3 = bitmap2;
                    build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent32222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                    if (DialogObject.isEncryptedDialog(j11)) {
                    }
                    if (str21 == null) {
                    }
                    StringBuilder sb52222 = new StringBuilder();
                    sb52222.append("tgaccount");
                    long j212222 = j12;
                    sb52222.append(j212222);
                    wearableExtender2222.setBridgeTag(sb52222.toString());
                    if (!dialogKey6.story) {
                    }
                    NotificationCompat.Builder autoCancel2222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str14).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                    if (dialogKey6.story) {
                    }
                    category = autoCancel2222.setNumber(arrayList14.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle).setContentIntent(activity2222).extend(wearableExtender2222).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
                    Intent intent42222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                    intent42222.putExtra("messageDate", i10);
                    intent42222.putExtra("dialogId", j11);
                    String str252222 = str20;
                    intent42222.putExtra(str252222, notificationsController3.currentAccount);
                    if (dialogKey6.story) {
                    }
                    category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent42222, 167772160));
                    if (z6) {
                    }
                    if (action != null) {
                    }
                    if (!z5) {
                    }
                    if (arrayList4.size() != 1) {
                    }
                    if (DialogObject.isEncryptedDialog(j11)) {
                    }
                    if (bitmap3 != null) {
                    }
                    if (!AndroidUtilities.needShowPasscode(false)) {
                    }
                    if (tLRPC$Chat2 == null) {
                    }
                    tLRPC$User5 = tLRPC$User4;
                    boolean z162222 = z6;
                    Notification notification32222 = notification;
                    if (Build.VERSION.SDK_INT >= 26) {
                    }
                    long j222222 = j11;
                    i11 = i26;
                    z10 = z162222;
                    j8 = j16;
                    longSparseArray6 = longSparseArray8;
                    ArrayList arrayList222222 = arrayList2;
                    i12 = i8;
                    TLRPC$Chat tLRPC$Chat32222 = tLRPC$Chat2;
                    sharedPreferences = sharedPreferences2;
                    longSparseArray7 = longSparseArray5;
                    notification2 = notification32222;
                    arrayList6 = arrayList222222;
                    arrayList6.add(new 1NotificationHolder(num2.intValue(), j222222, dialogKey6.story, j15, str14, tLRPC$User5, tLRPC$Chat32222, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                    notificationsController2 = this;
                    notificationsController2.wearNotificationsIds.put(j222222, num2);
                    i5 = i12 + 1;
                    arrayList19 = arrayList6;
                    size = i11;
                    arrayList16 = arrayList4;
                    longSparseArray12 = longSparseArray4;
                    z4 = z10;
                    notificationsSettings = sharedPreferences;
                    longSparseArray13 = longSparseArray7;
                    clientUserId = j8;
                    longSparseArray = longSparseArray6;
                    build3 = notification2;
                    i4 = 7;
                    notificationsController4 = notificationsController2;
                }
                LongSparseArray longSparseArray15 = longSparseArray;
                longSparseArray2 = longSparseArray13;
                Notification notification4 = build3;
                NotificationsController notificationsController5 = notificationsController4;
                ArrayList arrayList28 = arrayList19;
                if (!z4) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("show summary with id " + notificationsController5.notificationId);
                    }
                    try {
                        notificationManager.notify(notificationsController5.notificationId, notification4);
                        notificationsController = notificationsController5;
                        arrayList = arrayList28;
                    } catch (SecurityException e5) {
                        FileLog.e(e5);
                        notificationsController = this;
                        arrayList = arrayList28;
                        notificationsController.resetNotificationSound(builder, j, j2, str2, jArr, i, uri, i2, z, z2, z3, i3);
                    }
                } else {
                    notificationsController = notificationsController5;
                    arrayList = arrayList28;
                    if (notificationsController.openedInBubbleDialogs.isEmpty()) {
                        notificationManager.cancel(notificationsController.notificationId);
                    }
                }
                i6 = 0;
                while (i6 < longSparseArray2.size()) {
                    LongSparseArray longSparseArray16 = longSparseArray2;
                    if (!notificationsController.openedInBubbleDialogs.contains(Long.valueOf(longSparseArray16.keyAt(i6)))) {
                        Integer num5 = (Integer) longSparseArray16.valueAt(i6);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("cancel notification id " + num5);
                        }
                        notificationManager.cancel(num5.intValue());
                    }
                    i6++;
                    longSparseArray2 = longSparseArray16;
                }
                ArrayList arrayList29 = new ArrayList(arrayList.size());
                size2 = arrayList.size();
                i7 = 0;
                while (i7 < size2) {
                    ArrayList arrayList30 = arrayList;
                    1NotificationHolder r4 = (1NotificationHolder) arrayList30.get(i7);
                    arrayList29.clear();
                    if (Build.VERSION.SDK_INT < 29 || DialogObject.isEncryptedDialog(r4.dialogId)) {
                        longSparseArray3 = longSparseArray15;
                    } else {
                        NotificationCompat.Builder builder3 = r4.notification;
                        long j26 = r4.dialogId;
                        longSparseArray3 = longSparseArray15;
                        String createNotificationShortcut = createNotificationShortcut(builder3, j26, r4.name, r4.user, r4.chat, (Person) longSparseArray3.get(j26), !r4.story);
                        if (createNotificationShortcut != null) {
                            arrayList29.add(createNotificationShortcut);
                        }
                    }
                    r4.call();
                    if (!unsupportedNotificationShortcut() && !arrayList29.isEmpty()) {
                        ShortcutManagerCompat.removeDynamicShortcuts(ApplicationLoader.applicationContext, arrayList29);
                    }
                    i7++;
                    arrayList = arrayList30;
                    longSparseArray15 = longSparseArray3;
                }
            }
        }
        z4 = true;
        if (z4) {
            checkOtherNotificationsChannel();
        }
        clientUserId = getUserConfig().getClientUserId();
        if (AndroidUtilities.needShowPasscode()) {
        }
        SharedConfig.passcodeHash.length();
        i4 = 7;
        longSparseArray = new LongSparseArray();
        size = arrayList16.size();
        i5 = 0;
        while (i5 < size) {
            int i262 = size;
            dialogKey = (DialogKey) arrayList16.get(i5);
            if (!dialogKey.story) {
            }
            Integer num42 = (Integer) longSparseArray13.get(j4);
            int i282 = id;
            if (dialogKey.story) {
            }
            int i292 = 0;
            while (i9 < arrayList3.size()) {
            }
            if (!dialogKey.story) {
            }
            tLRPC$Chat = null;
            z8 = false;
            boolean z1522 = z7;
            if (messageObject == null) {
            }
            str4 = string;
            str5 = str4;
            if (z5) {
            }
            if (tLRPC$FileLocation5 == null) {
            }
            if (tLRPC$Chat == null) {
            }
            Bitmap bitmap6222 = bitmap;
            if (z9) {
            }
            file2 = file;
            str9 = str8;
            Intent intent622 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
            intent622.putExtra("dialog_id", j4);
            intent622.putExtra("max_id", i282);
            intent622.putExtra("topic_id", j7);
            notificationsController3 = this;
            tLRPC$Chat2 = tLRPC$Chat;
            intent622.putExtra("currentAccount", notificationsController3.currentAccount);
            str10 = "max_id";
            i13 = i282;
            PendingIntent broadcast222 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent622, 167772160);
            RemoteInput build4222 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
            if (!DialogObject.isChatDialog(j4)) {
            }
            build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast222).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build4222).setShowsUserInterface(false).build();
            num3 = notificationsController3.pushDialogs.get(j4);
            if (num3 == null) {
            }
            dialogKey4 = dialogKey3;
            if (!dialogKey4.story) {
            }
            if (max > 1) {
            }
            str12 = str6;
            str13 = str12;
            j9 = j6;
            Person person4222 = (Person) longSparseArray.get(j9);
            action = build;
            if (Build.VERSION.SDK_INT >= 28) {
            }
            j10 = j7;
            person = person4222;
            if (messageObject == null) {
            }
            if (person == null) {
            }
            messagingStyle = new NotificationCompat.MessagingStyle("");
            i14 = Build.VERSION.SDK_INT;
            if (i14 >= 28) {
            }
            messagingStyle.setConversationTitle(str12);
            messagingStyle.setGroupConversation(i14 >= 28 || (!z9 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
            StringBuilder sb322222 = new StringBuilder();
            messageObject2 = messageObject;
            String[] strArr222222 = new String[1];
            String str2422222 = "";
            boolean[] zArr22222 = new boolean[1];
            if (!dialogKey4.story) {
            }
            Intent intent222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
            intent222222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
            intent222222.setFlags(ConnectionsManager.FileTypeFile);
            intent222222.addCategory("android.intent.category.LAUNCHER");
            if (messageObject2 == null) {
            }
            dialogKey6 = dialogKey5;
            if (!dialogKey6.story) {
            }
            StringBuilder sb422222 = new StringBuilder();
            sb422222.append("show extra notifications chatId ");
            sb422222.append(j11);
            sb422222.append(" topicId ");
            j15 = j10;
            sb422222.append(j15);
            FileLog.d(sb422222.toString());
            if (j15 != 0) {
            }
            intent222222.putExtra("currentAccount", notificationsController3.currentAccount);
            PendingIntent activity22222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222222, 1140850688);
            NotificationCompat.WearableExtender wearableExtender22222 = new NotificationCompat.WearableExtender();
            if (action != null) {
            }
            int i3022222 = i15;
            arrayList13 = arrayList8;
            Intent intent322222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
            intent322222.addFlags(32);
            intent322222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
            intent322222.putExtra(str11, j11);
            int i3122222 = i13;
            intent322222.putExtra(str10, i3122222);
            intent322222.putExtra("currentAccount", notificationsController3.currentAccount);
            bitmap3 = bitmap2;
            build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent322222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
            if (DialogObject.isEncryptedDialog(j11)) {
            }
            if (str21 == null) {
            }
            StringBuilder sb522222 = new StringBuilder();
            sb522222.append("tgaccount");
            long j2122222 = j12;
            sb522222.append(j2122222);
            wearableExtender22222.setBridgeTag(sb522222.toString());
            if (!dialogKey6.story) {
            }
            NotificationCompat.Builder autoCancel22222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str14).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
            if (dialogKey6.story) {
            }
            category = autoCancel22222.setNumber(arrayList14.size()).setColor(-15618822).setGroupSummary(false).setWhen(j17).setShowWhen(true).setStyle(messagingStyle).setContentIntent(activity22222).extend(wearableExtender22222).setSortKey(String.valueOf(Long.MAX_VALUE - j17)).setCategory("msg");
            Intent intent422222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
            intent422222.putExtra("messageDate", i10);
            intent422222.putExtra("dialogId", j11);
            String str2522222 = str20;
            intent422222.putExtra(str2522222, notificationsController3.currentAccount);
            if (dialogKey6.story) {
            }
            category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent422222, 167772160));
            if (z6) {
            }
            if (action != null) {
            }
            if (!z5) {
            }
            if (arrayList4.size() != 1) {
            }
            if (DialogObject.isEncryptedDialog(j11)) {
            }
            if (bitmap3 != null) {
            }
            if (!AndroidUtilities.needShowPasscode(false)) {
            }
            if (tLRPC$Chat2 == null) {
            }
            tLRPC$User5 = tLRPC$User4;
            boolean z1622222 = z6;
            Notification notification322222 = notification;
            if (Build.VERSION.SDK_INT >= 26) {
            }
            long j2222222 = j11;
            i11 = i262;
            z10 = z1622222;
            j8 = j16;
            longSparseArray6 = longSparseArray8;
            ArrayList arrayList2222222 = arrayList2;
            i12 = i8;
            TLRPC$Chat tLRPC$Chat322222 = tLRPC$Chat2;
            sharedPreferences = sharedPreferences2;
            longSparseArray7 = longSparseArray5;
            notification2 = notification322222;
            arrayList6 = arrayList2222222;
            arrayList6.add(new 1NotificationHolder(num2.intValue(), j2222222, dialogKey6.story, j15, str14, tLRPC$User5, tLRPC$Chat322222, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
            notificationsController2 = this;
            notificationsController2.wearNotificationsIds.put(j2222222, num2);
            i5 = i12 + 1;
            arrayList19 = arrayList6;
            size = i11;
            arrayList16 = arrayList4;
            longSparseArray12 = longSparseArray4;
            z4 = z10;
            notificationsSettings = sharedPreferences;
            longSparseArray13 = longSparseArray7;
            clientUserId = j8;
            longSparseArray = longSparseArray6;
            build3 = notification2;
            i4 = 7;
            notificationsController4 = notificationsController2;
        }
        LongSparseArray longSparseArray152 = longSparseArray;
        longSparseArray2 = longSparseArray13;
        Notification notification42 = build3;
        NotificationsController notificationsController52 = notificationsController4;
        ArrayList arrayList282 = arrayList19;
        if (!z4) {
        }
        i6 = 0;
        while (i6 < longSparseArray2.size()) {
        }
        ArrayList arrayList292 = new ArrayList(arrayList.size());
        size2 = arrayList.size();
        i7 = 0;
        while (i7 < size2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 1NotificationHolder {
        TLRPC$Chat chat;
        long dialogId;
        int id;
        String name;
        NotificationCompat.Builder notification;
        boolean story;
        long topicId;
        TLRPC$User user;
        final /* synthetic */ String val$chatName;
        final /* synthetic */ int val$chatType;
        final /* synthetic */ int val$importance;
        final /* synthetic */ boolean val$isDefault;
        final /* synthetic */ boolean val$isInApp;
        final /* synthetic */ boolean val$isSilent;
        final /* synthetic */ long val$lastTopicId;
        final /* synthetic */ int val$ledColor;
        final /* synthetic */ Uri val$sound;
        final /* synthetic */ long[] val$vibrationPattern;

        1NotificationHolder(int i, long j, boolean z, long j2, String str, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, NotificationCompat.Builder builder, long j3, String str2, long[] jArr, int i2, Uri uri, int i3, boolean z2, boolean z3, boolean z4, int i4) {
            this.val$lastTopicId = j3;
            this.val$chatName = str2;
            this.val$vibrationPattern = jArr;
            this.val$ledColor = i2;
            this.val$sound = uri;
            this.val$importance = i3;
            this.val$isDefault = z2;
            this.val$isInApp = z3;
            this.val$isSilent = z4;
            this.val$chatType = i4;
            this.id = i;
            this.name = str;
            this.user = tLRPC$User;
            this.chat = tLRPC$Chat;
            this.notification = builder;
            this.dialogId = j;
            this.story = z;
            this.topicId = j2;
        }

        void call() {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.w("show dialog notification with id " + this.id + " " + this.dialogId + " user=" + this.user + " chat=" + this.chat);
            }
            try {
                NotificationsController.notificationManager.notify(this.id, this.notification.build());
            } catch (SecurityException e) {
                FileLog.e(e);
                NotificationsController.this.resetNotificationSound(this.notification, this.dialogId, this.val$lastTopicId, this.val$chatName, this.val$vibrationPattern, this.val$ledColor, this.val$sound, this.val$importance, this.val$isDefault, this.val$isInApp, this.val$isSilent, this.val$chatType);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showExtraNotifications$42(Uri uri, File file) {
        try {
            ApplicationLoader.applicationContext.revokeUriPermission(uri, 1);
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (file != null) {
            try {
                file.delete();
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
    }

    private String cutLastName(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(32);
        if (indexOf >= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(str.substring(0, indexOf));
            sb.append(str.endsWith("…") ? "…" : "");
            return sb.toString();
        }
        return str;
    }

    private Pair<Integer, Boolean> parseStoryPushes(ArrayList<String> arrayList, ArrayList<Object> arrayList2) {
        int i;
        String str;
        TLRPC$FileLocation tLRPC$FileLocation;
        int min = Math.min(3, this.storyPushMessages.size());
        boolean z = false;
        int i2 = 0;
        while (i < min) {
            StoryNotification storyNotification = this.storyPushMessages.get(i);
            i2 += storyNotification.dateByIds.size();
            z |= storyNotification.hidden;
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(storyNotification.dialogId));
            if (user == null && (user = getMessagesStorage().getUserSync(storyNotification.dialogId)) != null) {
                getMessagesController().putUser(user, true);
            }
            Object obj = null;
            if (user != null) {
                str = UserObject.getUserName(user);
                TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = user.photo;
                if (tLRPC$UserProfilePhoto != null && (tLRPC$FileLocation = tLRPC$UserProfilePhoto.photo_small) != null && tLRPC$FileLocation.volume_id != 0 && tLRPC$FileLocation.local_id != 0) {
                    File pathToAttach = getFileLoader().getPathToAttach(user.photo.photo_small, true);
                    if (!pathToAttach.exists()) {
                        pathToAttach = user.photo.photo_big != null ? getFileLoader().getPathToAttach(user.photo.photo_big, true) : null;
                        if (pathToAttach != null && !pathToAttach.exists()) {
                            pathToAttach = null;
                        }
                    }
                    if (pathToAttach != null) {
                        obj = pathToAttach;
                    }
                }
            } else {
                str = storyNotification.localName;
                i = str == null ? i + 1 : 0;
            }
            if (str.length() > 50) {
                str = str.substring(0, 25) + "…";
            }
            arrayList.add(str);
            if (obj == null && user != null) {
                arrayList2.add(user);
            } else if (obj != null) {
                arrayList2.add(obj);
            }
        }
        if (z) {
            arrayList2.clear();
        }
        return new Pair<>(Integer.valueOf(i2), Boolean.valueOf(z));
    }

    public static Person.Builder loadRoundAvatar(File file, Person.Builder builder) {
        if (file != null && Build.VERSION.SDK_INT >= 28) {
            try {
                builder.setIcon(IconCompat.createWithBitmap(ImageDecoder.decodeBitmap(ImageDecoder.createSource(file), new ImageDecoder.OnHeaderDecodedListener() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda0
                    @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
                    public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
                        NotificationsController.lambda$loadRoundAvatar$44(imageDecoder, imageInfo, source);
                    }
                })));
            } catch (Throwable unused) {
            }
        }
        return builder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadRoundAvatar$44(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
        imageDecoder.setPostProcessor(new PostProcessor() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda1
            @Override // android.graphics.PostProcessor
            public final int onPostProcess(Canvas canvas) {
                int lambda$loadRoundAvatar$43;
                lambda$loadRoundAvatar$43 = NotificationsController.lambda$loadRoundAvatar$43(canvas);
                return lambda$loadRoundAvatar$43;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$loadRoundAvatar$43(Canvas canvas) {
        Path path = new Path();
        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        int width = canvas.getWidth();
        float f = width / 2;
        path.addRoundRect(0.0f, 0.0f, width, canvas.getHeight(), f, f, Path.Direction.CW);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        canvas.drawPath(path, paint);
        return -3;
    }

    public static Bitmap loadMultipleAvatars(ArrayList<Object> arrayList) {
        int i;
        Bitmap bitmap;
        Paint paint;
        float f;
        int i2;
        float size;
        float size2;
        float f2;
        float f3;
        float f4;
        float f5;
        Object obj;
        TextPaint textPaint;
        ArrayList<Object> arrayList2 = arrayList;
        if (Build.VERSION.SDK_INT < 28 || arrayList2 == null || arrayList.size() == 0) {
            return null;
        }
        int dp = AndroidUtilities.dp(64.0f);
        Bitmap createBitmap = Bitmap.createBitmap(dp, dp, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Matrix matrix = new Matrix();
        Paint paint2 = new Paint(3);
        Paint paint3 = new Paint(1);
        Rect rect = new Rect();
        paint3.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        float f6 = 1.0f;
        float f7 = arrayList.size() == 1 ? 1.0f : arrayList.size() == 2 ? 0.65f : 0.5f;
        int i3 = 0;
        TextPaint textPaint2 = null;
        while (i3 < arrayList.size()) {
            float f8 = dp;
            float f9 = (f6 - f7) * f8;
            try {
                size = (f9 / arrayList.size()) * ((arrayList.size() - 1) - i3);
                size2 = i3 * (f9 / arrayList.size());
                f2 = f8 * f7;
                f3 = f2 / 2.0f;
                i = dp;
                f4 = size + f3;
                f = f7;
                f5 = size2 + f3;
                bitmap = createBitmap;
                try {
                    canvas.drawCircle(f4, f5, AndroidUtilities.dp(2.0f) + f3, paint3);
                    obj = arrayList2.get(i3);
                    paint = paint3;
                    try {
                    } catch (Throwable unused) {
                        i2 = i3;
                        i3 = i2 + 1;
                        arrayList2 = arrayList;
                        dp = i;
                        f7 = f;
                        createBitmap = bitmap;
                        paint3 = paint;
                        f6 = 1.0f;
                    }
                } catch (Throwable unused2) {
                    paint = paint3;
                }
            } catch (Throwable unused3) {
                i = dp;
                bitmap = createBitmap;
                paint = paint3;
                f = f7;
            }
            if (obj instanceof File) {
                try {
                    String absolutePath = ((File) arrayList2.get(i3)).getAbsolutePath();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    try {
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(absolutePath, options);
                        int i4 = (int) f2;
                        options.inSampleSize = StoryEntry.calculateInSampleSize(options, i4, i4);
                        options.inJustDecodeBounds = false;
                        options.inDither = true;
                        Bitmap decodeFile = BitmapFactory.decodeFile(absolutePath, options);
                        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                        BitmapShader bitmapShader = new BitmapShader(decodeFile, tileMode, tileMode);
                        matrix.reset();
                        matrix.postScale(f2 / decodeFile.getWidth(), f2 / decodeFile.getHeight());
                        matrix.postTranslate(size, size2);
                        bitmapShader.setLocalMatrix(matrix);
                        paint2.setShader(bitmapShader);
                        canvas.drawCircle(f4, f5, f3, paint2);
                        decodeFile.recycle();
                    } catch (Throwable unused4) {
                        i2 = i3;
                        i3 = i2 + 1;
                        arrayList2 = arrayList;
                        dp = i;
                        f7 = f;
                        createBitmap = bitmap;
                        paint3 = paint;
                        f6 = 1.0f;
                    }
                } catch (Throwable unused5) {
                    i2 = i3;
                    i3 = i2 + 1;
                    arrayList2 = arrayList;
                    dp = i;
                    f7 = f;
                    createBitmap = bitmap;
                    paint3 = paint;
                    f6 = 1.0f;
                }
            } else if (obj instanceof TLRPC$User) {
                TLRPC$User tLRPC$User = (TLRPC$User) obj;
                int[] iArr = new int[2];
                i2 = i3;
                textPaint = textPaint2;
                try {
                    iArr[0] = Theme.getColor(Theme.keys_avatar_background[AvatarDrawable.getColorIndex(tLRPC$User.id)]);
                    iArr[1] = Theme.getColor(Theme.keys_avatar_background2[AvatarDrawable.getColorIndex(tLRPC$User.id)]);
                    float f10 = size2 + f2;
                    try {
                        float[] fArr = new float[2];
                        fArr[0] = 0.0f;
                        try {
                            fArr[1] = 1.0f;
                            paint2.setShader(new LinearGradient(size, size2, size, f10, iArr, fArr, Shader.TileMode.CLAMP));
                            canvas.drawCircle(f4, f5, f3, paint2);
                            if (textPaint == null) {
                                try {
                                    TextPaint textPaint3 = new TextPaint(1);
                                    try {
                                        textPaint3.setTypeface(AndroidUtilities.bold());
                                        textPaint3.setTextSize(f8 * 0.25f);
                                        textPaint3.setColor(-1);
                                        textPaint2 = textPaint3;
                                    } catch (Throwable unused6) {
                                        textPaint2 = textPaint3;
                                        i3 = i2 + 1;
                                        arrayList2 = arrayList;
                                        dp = i;
                                        f7 = f;
                                        createBitmap = bitmap;
                                        paint3 = paint;
                                        f6 = 1.0f;
                                    }
                                } catch (Throwable unused7) {
                                    textPaint2 = textPaint;
                                    i3 = i2 + 1;
                                    arrayList2 = arrayList;
                                    dp = i;
                                    f7 = f;
                                    createBitmap = bitmap;
                                    paint3 = paint;
                                    f6 = 1.0f;
                                }
                            } else {
                                textPaint2 = textPaint;
                            }
                            try {
                                StringBuilder sb = new StringBuilder();
                                AvatarDrawable.getAvatarSymbols(tLRPC$User.first_name, tLRPC$User.last_name, null, sb);
                                String sb2 = sb.toString();
                                try {
                                    textPaint2.getTextBounds(sb2, 0, sb2.length(), rect);
                                    canvas.drawText(sb2, (f4 - (rect.width() / 2.0f)) - rect.left, (f5 - (rect.height() / 2.0f)) - rect.top, textPaint2);
                                } catch (Throwable unused8) {
                                }
                            } catch (Throwable unused9) {
                                i3 = i2 + 1;
                                arrayList2 = arrayList;
                                dp = i;
                                f7 = f;
                                createBitmap = bitmap;
                                paint3 = paint;
                                f6 = 1.0f;
                            }
                        } catch (Throwable unused10) {
                        }
                    } catch (Throwable unused11) {
                    }
                } catch (Throwable unused12) {
                }
                i3 = i2 + 1;
                arrayList2 = arrayList;
                dp = i;
                f7 = f;
                createBitmap = bitmap;
                paint3 = paint;
                f6 = 1.0f;
            }
            i2 = i3;
            textPaint = textPaint2;
            textPaint2 = textPaint;
            i3 = i2 + 1;
            arrayList2 = arrayList;
            dp = i;
            f7 = f;
            createBitmap = bitmap;
            paint3 = paint;
            f6 = 1.0f;
        }
        return createBitmap;
    }

    public void playOutChatSound() {
        if (!this.inChatSoundEnabled || MediaController.getInstance().isRecordingAudio()) {
            return;
        }
        try {
            if (audioManager.getRingerMode() == 0) {
                return;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$playOutChatSound$46();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playOutChatSound$46() {
        try {
            if (Math.abs(SystemClock.elapsedRealtime() - this.lastSoundOutPlay) <= 100) {
                return;
            }
            this.lastSoundOutPlay = SystemClock.elapsedRealtime();
            if (this.soundPool == null) {
                SoundPool soundPool = new SoundPool(3, 1, 0);
                this.soundPool = soundPool;
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda2
                    @Override // android.media.SoundPool.OnLoadCompleteListener
                    public final void onLoadComplete(SoundPool soundPool2, int i, int i2) {
                        NotificationsController.lambda$playOutChatSound$45(soundPool2, i, i2);
                    }
                });
            }
            if (this.soundOut == 0 && !this.soundOutLoaded) {
                this.soundOutLoaded = true;
                this.soundOut = this.soundPool.load(ApplicationLoader.applicationContext, R.raw.sound_out, 1);
            }
            int i = this.soundOut;
            if (i != 0) {
                try {
                    this.soundPool.play(i, 1.0f, 1.0f, 1, 0, 1.0f);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$playOutChatSound$45(SoundPool soundPool, int i, int i2) {
        if (i2 == 0) {
            try {
                soundPool.play(i, 1.0f, 1.0f, 1, 0, 1.0f);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public void clearDialogNotificationsSettings(long j, long j2) {
        SharedPreferences.Editor edit = getAccountInstance().getNotificationsSettings().edit();
        String sharedPrefKey = getSharedPrefKey(j, j2);
        SharedPreferences.Editor remove = edit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey);
        remove.remove(NotificationsSettingsFacade.PROPERTY_CUSTOM + sharedPrefKey);
        getMessagesStorage().setDialogFlags(j, 0L);
        TLRPC$Dialog tLRPC$Dialog = getMessagesController().dialogs_dict.get(j);
        if (tLRPC$Dialog != null) {
            tLRPC$Dialog.notify_settings = new TLRPC$TL_peerNotifySettings();
        }
        edit.commit();
        getNotificationsController().updateServerNotificationsSettings(j, j2, true);
    }

    public void setDialogNotificationsSettings(long j, long j2, int i) {
        SharedPreferences.Editor edit = getAccountInstance().getNotificationsSettings().edit();
        TLRPC$Dialog tLRPC$Dialog = MessagesController.getInstance(UserConfig.selectedAccount).dialogs_dict.get(j);
        if (i == 4) {
            if (isGlobalNotificationsEnabled(j, false, false)) {
                edit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2));
            } else {
                edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2), 0);
            }
            getMessagesStorage().setDialogFlags(j, 0L);
            if (tLRPC$Dialog != null) {
                tLRPC$Dialog.notify_settings = new TLRPC$TL_peerNotifySettings();
            }
        } else {
            int currentTime = ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime();
            if (i == 0) {
                currentTime += 3600;
            } else if (i == 1) {
                currentTime += 28800;
            } else if (i == 2) {
                currentTime += 172800;
            } else if (i == 3) {
                currentTime = ConnectionsManager.DEFAULT_DATACENTER_ID;
            }
            long j3 = 1;
            if (i == 3) {
                edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2), 2);
            } else {
                edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2), 3);
                edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + getSharedPrefKey(j, j2), currentTime);
                j3 = 1 | (((long) currentTime) << 32);
            }
            getInstance(UserConfig.selectedAccount).removeNotificationsForDialog(j);
            MessagesStorage.getInstance(UserConfig.selectedAccount).setDialogFlags(j, j3);
            if (tLRPC$Dialog != null) {
                TLRPC$TL_peerNotifySettings tLRPC$TL_peerNotifySettings = new TLRPC$TL_peerNotifySettings();
                tLRPC$Dialog.notify_settings = tLRPC$TL_peerNotifySettings;
                tLRPC$TL_peerNotifySettings.mute_until = currentTime;
            }
        }
        edit.commit();
        updateServerNotificationsSettings(j, j2);
    }

    public void updateServerNotificationsSettings(long j, long j2) {
        updateServerNotificationsSettings(j, j2, true);
    }

    public void updateServerNotificationsSettings(long j, long j2, boolean z) {
        if (z) {
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsSettingsUpdated, new Object[0]);
        }
        if (DialogObject.isEncryptedDialog(j)) {
            return;
        }
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        TLRPC$TL_account_updateNotifySettings tLRPC$TL_account_updateNotifySettings = new TLRPC$TL_account_updateNotifySettings();
        tLRPC$TL_account_updateNotifySettings.settings = new TLRPC$TL_inputPeerNotifySettings();
        String sharedPrefKey = getSharedPrefKey(j, j2);
        TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings = tLRPC$TL_account_updateNotifySettings.settings;
        tLRPC$TL_inputPeerNotifySettings.flags |= 1;
        tLRPC$TL_inputPeerNotifySettings.show_previews = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CONTENT_PREVIEW + sharedPrefKey, true);
        TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings2 = tLRPC$TL_account_updateNotifySettings.settings;
        tLRPC$TL_inputPeerNotifySettings2.flags = tLRPC$TL_inputPeerNotifySettings2.flags | 2;
        tLRPC$TL_inputPeerNotifySettings2.silent = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_SILENT + sharedPrefKey, false);
        if (notificationsSettings.contains(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + sharedPrefKey)) {
            TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings3 = tLRPC$TL_account_updateNotifySettings.settings;
            tLRPC$TL_inputPeerNotifySettings3.flags |= 64;
            tLRPC$TL_inputPeerNotifySettings3.stories_muted = !notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + sharedPrefKey, true);
        }
        int i = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2), -1);
        if (i != -1) {
            TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings4 = tLRPC$TL_account_updateNotifySettings.settings;
            tLRPC$TL_inputPeerNotifySettings4.flags |= 4;
            if (i == 3) {
                tLRPC$TL_inputPeerNotifySettings4.mute_until = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + getSharedPrefKey(j, j2), 0);
            } else {
                tLRPC$TL_inputPeerNotifySettings4.mute_until = i == 2 ? ConnectionsManager.DEFAULT_DATACENTER_ID : 0;
            }
        }
        long j3 = notificationsSettings.getLong("sound_document_id_" + getSharedPrefKey(j, j2), 0L);
        String string = notificationsSettings.getString("sound_path_" + getSharedPrefKey(j, j2), null);
        TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings5 = tLRPC$TL_account_updateNotifySettings.settings;
        tLRPC$TL_inputPeerNotifySettings5.flags = tLRPC$TL_inputPeerNotifySettings5.flags | 8;
        if (j3 != 0) {
            TLRPC$TL_notificationSoundRingtone tLRPC$TL_notificationSoundRingtone = new TLRPC$TL_notificationSoundRingtone();
            tLRPC$TL_notificationSoundRingtone.id = j3;
            tLRPC$TL_account_updateNotifySettings.settings.sound = tLRPC$TL_notificationSoundRingtone;
        } else if (string != null) {
            if (string.equalsIgnoreCase("NoSound")) {
                tLRPC$TL_account_updateNotifySettings.settings.sound = new TLRPC$TL_notificationSoundNone();
            } else {
                TLRPC$TL_notificationSoundLocal tLRPC$TL_notificationSoundLocal = new TLRPC$TL_notificationSoundLocal();
                tLRPC$TL_notificationSoundLocal.title = notificationsSettings.getString("sound_" + getSharedPrefKey(j, j2), null);
                tLRPC$TL_notificationSoundLocal.data = string;
                tLRPC$TL_account_updateNotifySettings.settings.sound = tLRPC$TL_notificationSoundLocal;
            }
        } else {
            tLRPC$TL_inputPeerNotifySettings5.sound = new TLRPC$TL_notificationSoundDefault();
        }
        if (j2 != 0 && j != getUserConfig().getClientUserId()) {
            TLRPC$TL_inputNotifyForumTopic tLRPC$TL_inputNotifyForumTopic = new TLRPC$TL_inputNotifyForumTopic();
            tLRPC$TL_inputNotifyForumTopic.peer = getMessagesController().getInputPeer(j);
            tLRPC$TL_inputNotifyForumTopic.top_msg_id = (int) j2;
            tLRPC$TL_account_updateNotifySettings.peer = tLRPC$TL_inputNotifyForumTopic;
        } else {
            TLRPC$TL_inputNotifyPeer tLRPC$TL_inputNotifyPeer = new TLRPC$TL_inputNotifyPeer();
            tLRPC$TL_account_updateNotifySettings.peer = tLRPC$TL_inputNotifyPeer;
            tLRPC$TL_inputNotifyPeer.peer = getMessagesController().getInputPeer(j);
        }
        getConnectionsManager().sendRequest(tLRPC$TL_account_updateNotifySettings, new RequestDelegate() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda51
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                NotificationsController.lambda$updateServerNotificationsSettings$47(tLObject, tLRPC$TL_error);
            }
        });
    }

    public void updateServerNotificationsSettings(int i) {
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        if (i == 4 || i == 5) {
            TLRPC$TL_account_setReactionsNotifySettings tLRPC$TL_account_setReactionsNotifySettings = new TLRPC$TL_account_setReactionsNotifySettings();
            tLRPC$TL_account_setReactionsNotifySettings.settings = new TLRPC$TL_reactionsNotifySettings();
            if (notificationsSettings.getBoolean("EnableReactionsMessages", true)) {
                tLRPC$TL_account_setReactionsNotifySettings.settings.flags |= 1;
                if (notificationsSettings.getBoolean("EnableReactionsMessagesContacts", false)) {
                    tLRPC$TL_account_setReactionsNotifySettings.settings.messages_notify_from = new TLRPC$TL_reactionNotificationsFromContacts();
                } else {
                    tLRPC$TL_account_setReactionsNotifySettings.settings.messages_notify_from = new TLRPC$TL_reactionNotificationsFromAll();
                }
            }
            if (notificationsSettings.getBoolean("EnableReactionsStories", true)) {
                tLRPC$TL_account_setReactionsNotifySettings.settings.flags |= 2;
                if (notificationsSettings.getBoolean("EnableReactionsStoriesContacts", false)) {
                    tLRPC$TL_account_setReactionsNotifySettings.settings.stories_notify_from = new TLRPC$TL_reactionNotificationsFromContacts();
                } else {
                    tLRPC$TL_account_setReactionsNotifySettings.settings.stories_notify_from = new TLRPC$TL_reactionNotificationsFromAll();
                }
            }
            tLRPC$TL_account_setReactionsNotifySettings.settings.show_previews = notificationsSettings.getBoolean("EnableReactionsPreview", true);
            tLRPC$TL_account_setReactionsNotifySettings.settings.sound = getInputSound(notificationsSettings, "ReactionSound", "ReactionSoundDocId", "ReactionSoundPath");
            getConnectionsManager().sendRequest(tLRPC$TL_account_setReactionsNotifySettings, new RequestDelegate() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda50
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    NotificationsController.lambda$updateServerNotificationsSettings$48(tLObject, tLRPC$TL_error);
                }
            });
            return;
        }
        TLRPC$TL_account_updateNotifySettings tLRPC$TL_account_updateNotifySettings = new TLRPC$TL_account_updateNotifySettings();
        TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings = new TLRPC$TL_inputPeerNotifySettings();
        tLRPC$TL_account_updateNotifySettings.settings = tLRPC$TL_inputPeerNotifySettings;
        tLRPC$TL_inputPeerNotifySettings.flags = 5;
        if (i == 0) {
            tLRPC$TL_account_updateNotifySettings.peer = new TLRPC$TL_inputNotifyChats();
            tLRPC$TL_account_updateNotifySettings.settings.mute_until = notificationsSettings.getInt("EnableGroup2", 0);
            tLRPC$TL_account_updateNotifySettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewGroup", true);
            TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings2 = tLRPC$TL_account_updateNotifySettings.settings;
            tLRPC$TL_inputPeerNotifySettings2.flags |= 8;
            tLRPC$TL_inputPeerNotifySettings2.sound = getInputSound(notificationsSettings, "GroupSound", "GroupSoundDocId", "GroupSoundPath");
        } else if (i == 1 || i == 3) {
            tLRPC$TL_account_updateNotifySettings.peer = new TLRPC$TL_inputNotifyUsers();
            tLRPC$TL_account_updateNotifySettings.settings.mute_until = notificationsSettings.getInt("EnableAll2", 0);
            tLRPC$TL_account_updateNotifySettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewAll", true);
            TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings3 = tLRPC$TL_account_updateNotifySettings.settings;
            tLRPC$TL_inputPeerNotifySettings3.flags |= 128;
            tLRPC$TL_inputPeerNotifySettings3.stories_hide_sender = notificationsSettings.getBoolean("EnableHideStoriesSenders", false);
            if (notificationsSettings.contains("EnableAllStories")) {
                TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings4 = tLRPC$TL_account_updateNotifySettings.settings;
                tLRPC$TL_inputPeerNotifySettings4.flags |= 64;
                tLRPC$TL_inputPeerNotifySettings4.stories_muted = !notificationsSettings.getBoolean("EnableAllStories", true);
            }
            TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings5 = tLRPC$TL_account_updateNotifySettings.settings;
            tLRPC$TL_inputPeerNotifySettings5.flags |= 8;
            tLRPC$TL_inputPeerNotifySettings5.sound = getInputSound(notificationsSettings, "GlobalSound", "GlobalSoundDocId", "GlobalSoundPath");
            TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings6 = tLRPC$TL_account_updateNotifySettings.settings;
            tLRPC$TL_inputPeerNotifySettings6.flags |= LiteMode.FLAG_CHAT_BLUR;
            tLRPC$TL_inputPeerNotifySettings6.stories_sound = getInputSound(notificationsSettings, "StoriesSound", "StoriesSoundDocId", "StoriesSoundPath");
        } else {
            tLRPC$TL_account_updateNotifySettings.peer = new TLRPC$TL_inputNotifyBroadcasts();
            tLRPC$TL_account_updateNotifySettings.settings.mute_until = notificationsSettings.getInt("EnableChannel2", 0);
            tLRPC$TL_account_updateNotifySettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewChannel", true);
            TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings7 = tLRPC$TL_account_updateNotifySettings.settings;
            tLRPC$TL_inputPeerNotifySettings7.flags |= 8;
            tLRPC$TL_inputPeerNotifySettings7.sound = getInputSound(notificationsSettings, "ChannelSound", "ChannelSoundDocId", "ChannelSoundPath");
        }
        getConnectionsManager().sendRequest(tLRPC$TL_account_updateNotifySettings, new RequestDelegate() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda52
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                NotificationsController.lambda$updateServerNotificationsSettings$49(tLObject, tLRPC$TL_error);
            }
        });
    }

    private TLRPC$NotificationSound getInputSound(SharedPreferences sharedPreferences, String str, String str2, String str3) {
        long j = sharedPreferences.getLong(str2, 0L);
        String string = sharedPreferences.getString(str3, "NoSound");
        if (j != 0) {
            TLRPC$TL_notificationSoundRingtone tLRPC$TL_notificationSoundRingtone = new TLRPC$TL_notificationSoundRingtone();
            tLRPC$TL_notificationSoundRingtone.id = j;
            return tLRPC$TL_notificationSoundRingtone;
        } else if (string != null) {
            if (string.equalsIgnoreCase("NoSound")) {
                return new TLRPC$TL_notificationSoundNone();
            }
            TLRPC$TL_notificationSoundLocal tLRPC$TL_notificationSoundLocal = new TLRPC$TL_notificationSoundLocal();
            tLRPC$TL_notificationSoundLocal.title = sharedPreferences.getString(str, null);
            tLRPC$TL_notificationSoundLocal.data = string;
            return tLRPC$TL_notificationSoundLocal;
        } else {
            return new TLRPC$TL_notificationSoundDefault();
        }
    }

    public boolean isGlobalNotificationsEnabled(long j, boolean z, boolean z2) {
        return isGlobalNotificationsEnabled(j, null, z, z2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0016, code lost:
        if (r5.booleanValue() != false) goto L4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0030, code lost:
        if (r3.megagroup == false) goto L4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isGlobalNotificationsEnabled(long j, Boolean bool, boolean z, boolean z2) {
        int i = 2;
        if (z) {
            i = 4;
        } else if (z2) {
            i = 5;
        } else if (!DialogObject.isChatDialog(j)) {
            i = 1;
        } else if (bool == null) {
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j));
            if (ChatObject.isChannel(chat)) {
            }
            i = 0;
        }
        return isGlobalNotificationsEnabled(i);
    }

    public boolean isGlobalNotificationsEnabled(int i) {
        if (i == 4) {
            return getAccountInstance().getNotificationsSettings().getBoolean("EnableReactionsMessages", true);
        }
        if (i == 5) {
            return getAccountInstance().getNotificationsSettings().getBoolean("EnableReactionsStories", true);
        }
        if (i == 3) {
            return getAccountInstance().getNotificationsSettings().getBoolean("EnableAllStories", true);
        }
        return getAccountInstance().getNotificationsSettings().getInt(getGlobalNotificationsKey(i), 0) < getConnectionsManager().getCurrentTime();
    }

    public void setGlobalNotificationsEnabled(int i, int i2) {
        getAccountInstance().getNotificationsSettings().edit().putInt(getGlobalNotificationsKey(i), i2).commit();
        updateServerNotificationsSettings(i);
        getMessagesStorage().updateMutedDialogsFiltersCounters();
        deleteNotificationChannelGlobal(i);
    }

    public void muteDialog(long j, long j2, boolean z) {
        if (z) {
            getInstance(this.currentAccount).muteUntil(j, j2, ConnectionsManager.DEFAULT_DATACENTER_ID);
            return;
        }
        boolean isGlobalNotificationsEnabled = getInstance(this.currentAccount).isGlobalNotificationsEnabled(j, false, false);
        boolean z2 = j2 != 0;
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
        if (isGlobalNotificationsEnabled && !z2) {
            edit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2));
        } else {
            edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, j2), 0);
        }
        if (j2 == 0) {
            getMessagesStorage().setDialogFlags(j, 0L);
            TLRPC$Dialog tLRPC$Dialog = getMessagesController().dialogs_dict.get(j);
            if (tLRPC$Dialog != null) {
                tLRPC$Dialog.notify_settings = new TLRPC$TL_peerNotifySettings();
            }
        }
        edit.apply();
        updateServerNotificationsSettings(j, j2);
    }

    public NotificationsSettingsFacade getNotificationsSettingsFacade() {
        return this.dialogsNotificationsFacade;
    }

    public void loadTopicsNotificationsExceptions(final long j, final Consumer<HashSet<Integer>> consumer) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$loadTopicsNotificationsExceptions$51(j, consumer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadTopicsNotificationsExceptions$51(long j, final Consumer consumer) {
        final HashSet hashSet = new HashSet();
        for (Map.Entry<String, ?> entry : MessagesController.getNotificationsSettings(this.currentAccount).getAll().entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(NotificationsSettingsFacade.PROPERTY_NOTIFY + j)) {
                int intValue = Utilities.parseInt((CharSequence) key.replace(NotificationsSettingsFacade.PROPERTY_NOTIFY + j, "")).intValue();
                if (intValue != 0 && getMessagesController().isDialogMuted(j, intValue) != getMessagesController().isDialogMuted(j, 0L)) {
                    hashSet.add(Integer.valueOf(intValue));
                }
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.lambda$loadTopicsNotificationsExceptions$50(Consumer.this, hashSet);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadTopicsNotificationsExceptions$50(Consumer consumer, HashSet hashSet) {
        if (consumer != null) {
            consumer.accept(hashSet);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class DialogKey {
        final long dialogId;
        final boolean story;
        final long topicId;

        private DialogKey(long j, long j2, boolean z) {
            this.dialogId = j;
            this.topicId = j2;
            this.story = z;
        }
    }

    /* loaded from: classes3.dex */
    public static class StoryNotification {
        public long date;
        final HashMap<Integer, Pair<Long, Long>> dateByIds;
        final long dialogId;
        boolean hidden;
        String localName;

        public StoryNotification(long j, String str, int i, long j2) {
            this(j, str, i, j2, j2 + 86400000);
        }

        public StoryNotification(long j, String str, int i, long j2, long j3) {
            HashMap<Integer, Pair<Long, Long>> hashMap = new HashMap<>();
            this.dateByIds = hashMap;
            this.dialogId = j;
            this.localName = str;
            hashMap.put(Integer.valueOf(i), new Pair<>(Long.valueOf(j2), Long.valueOf(j3)));
            this.date = j2;
        }

        public long getLeastDate() {
            long j = -1;
            for (Pair<Long, Long> pair : this.dateByIds.values()) {
                if (j == -1 || j > ((Long) pair.first).longValue()) {
                    j = ((Long) pair.first).longValue();
                }
            }
            return j;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkStoryPushes() {
        long currentTimeMillis = System.currentTimeMillis();
        int i = 0;
        boolean z = false;
        while (i < this.storyPushMessages.size()) {
            StoryNotification storyNotification = this.storyPushMessages.get(i);
            Iterator<Map.Entry<Integer, Pair<Long, Long>>> it = storyNotification.dateByIds.entrySet().iterator();
            while (it.hasNext()) {
                if (currentTimeMillis >= ((Long) it.next().getValue().second).longValue()) {
                    it.remove();
                    z = true;
                }
            }
            if (z) {
                if (storyNotification.dateByIds.isEmpty()) {
                    getMessagesStorage().deleteStoryPushMessage(storyNotification.dialogId);
                    this.storyPushMessages.remove(i);
                    i--;
                } else {
                    getMessagesStorage().putStoryPushMessage(storyNotification);
                }
            }
            i++;
        }
        if (z) {
            showOrUpdateNotification(false);
        }
        updateStoryPushesRunnable();
    }

    private void updateStoryPushesRunnable() {
        long j = Long.MAX_VALUE;
        for (int i = 0; i < this.storyPushMessages.size(); i++) {
            for (Pair<Long, Long> pair : this.storyPushMessages.get(i).dateByIds.values()) {
                j = Math.min(j, ((Long) pair.second).longValue());
            }
        }
        DispatchQueue dispatchQueue = notificationsQueue;
        dispatchQueue.cancelRunnable(this.checkStoryPushesRunnable);
        long currentTimeMillis = j - System.currentTimeMillis();
        if (j != Long.MAX_VALUE) {
            dispatchQueue.postRunnable(this.checkStoryPushesRunnable, Math.max(0L, currentTimeMillis));
        }
    }
}
