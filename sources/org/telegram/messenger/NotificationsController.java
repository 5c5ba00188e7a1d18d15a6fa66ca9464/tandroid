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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
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
import j$.util.function.Consumer;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.support.LongSparseIntArray;
import org.telegram.tgnet.ConnectionsManager;
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
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$Poll;
import org.telegram.tgnet.TLRPC$ReplyMarkup;
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
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageMediaPoll;
import org.telegram.tgnet.TLRPC$TL_messageMediaVenue;
import org.telegram.tgnet.TLRPC$TL_messageService;
import org.telegram.tgnet.TLRPC$TL_notificationSoundDefault;
import org.telegram.tgnet.TLRPC$TL_notificationSoundLocal;
import org.telegram.tgnet.TLRPC$TL_notificationSoundNone;
import org.telegram.tgnet.TLRPC$TL_notificationSoundRingtone;
import org.telegram.tgnet.TLRPC$TL_peerNotifySettings;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.ui.BubbleActivity;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PopupNotificationActivity;
import org.webrtc.MediaStreamTrack;
/* loaded from: classes.dex */
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
    protected static AudioManager audioManager;
    private static final Object[] lockObjects;
    private static NotificationManagerCompat notificationManager;
    private static NotificationManager systemNotificationManager;
    private AlarmManager alarmManager;
    private boolean channelGroupsCreated;
    private ArrayList<MessageObject> delayedPushMessages;
    NotificationsSettingsFacade dialogsNotificationsFacade;
    private LongSparseArray<MessageObject> fcmRandomMessagesDict;
    private Boolean groupsCreated;
    private boolean inChatSoundEnabled;
    private int lastBadgeCount;
    private int lastButtonId;
    public long lastNotificationChannelCreateTime;
    private int lastOnlineFromOtherDevice;
    private long lastSoundOutPlay;
    private long lastSoundPlay;
    private LongSparseArray<Integer> lastWearNotifiedMessageId;
    private String launcherClassName;
    private SpoilerEffect mediaSpoilerEffect;
    private Runnable notificationDelayRunnable;
    private PowerManager.WakeLock notificationDelayWakelock;
    private String notificationGroup;
    private int notificationId;
    private boolean notifyCheck;
    private long openedDialogId;
    private HashSet<Long> openedInBubbleDialogs;
    private int openedTopicId;
    private int personalCount;
    public ArrayList<MessageObject> popupMessages;
    public ArrayList<MessageObject> popupReplyMessages;
    private LongSparseArray<Integer> pushDialogs;
    private LongSparseArray<Integer> pushDialogsOverrideMention;
    private ArrayList<MessageObject> pushMessages;
    private LongSparseArray<SparseArray<MessageObject>> pushMessagesDict;
    public boolean showBadgeMessages;
    public boolean showBadgeMuted;
    public boolean showBadgeNumber;
    private LongSparseArray<Point> smartNotificationsDialogs;
    private int soundIn;
    private boolean soundInLoaded;
    private int soundOut;
    private boolean soundOutLoaded;
    private SoundPool soundPool;
    private int soundRecord;
    private boolean soundRecordLoaded;
    char[] spoilerChars;
    private int total_unread_count;
    private LongSparseArray<Integer> wearNotificationsIds;
    private static DispatchQueue notificationsQueue = new DispatchQueue("notificationsQueue");
    public static long globalSecretChatId = DialogObject.makeEncryptedDialogId(1);

    public static String getGlobalNotificationsKey(int i) {
        return i == 0 ? "EnableGroup2" : i == 1 ? "EnableAll2" : "EnableChannel2";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateServerNotificationsSettings$39(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateServerNotificationsSettings$40(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
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
        this.openedDialogId = 0L;
        this.openedTopicId = 0;
        this.lastButtonId = 5000;
        this.total_unread_count = 0;
        this.personalCount = 0;
        this.notifyCheck = false;
        this.lastOnlineFromOtherDevice = 0;
        this.lastBadgeCount = -1;
        this.mediaSpoilerEffect = new SpoilerEffect();
        this.spoilerChars = new char[]{10252, 10338, 10385, 10280};
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
        this.notificationDelayRunnable = new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda9
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
            systemNotificationManager.deleteNotificationChannel(OTHER_NOTIFICATIONS_CHANNEL);
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
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public static String getSharedPrefKey(long j, int i) {
        return i != 0 ? String.format(Locale.US, "%d_%d", Long.valueOf(j), Integer.valueOf(i)) : String.valueOf(j);
    }

    public void muteUntil(long j, int i, int i2) {
        long j2 = 0;
        if (j != 0) {
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            boolean z = i != 0;
            boolean isGlobalNotificationsEnabled = getInstance(this.currentAccount).isGlobalNotificationsEnabled(j);
            String sharedPrefKey = getSharedPrefKey(j, i);
            if (i2 != Integer.MAX_VALUE) {
                edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey, 3);
                edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + sharedPrefKey, getConnectionsManager().getCurrentTime() + i2);
                j2 = (((long) i2) << 32) | 1;
            } else if (!isGlobalNotificationsEnabled && !z) {
                edit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey);
            } else {
                edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey, 2);
                j2 = 1L;
            }
            edit.apply();
            if (i == 0) {
                getInstance(this.currentAccount).removeNotificationsForDialog(j);
                MessagesStorage.getInstance(this.currentAccount).setDialogFlags(j, j2);
                TLRPC$Dialog tLRPC$Dialog = MessagesController.getInstance(this.currentAccount).dialogs_dict.get(j);
                if (tLRPC$Dialog != null) {
                    TLRPC$TL_peerNotifySettings tLRPC$TL_peerNotifySettings = new TLRPC$TL_peerNotifySettings();
                    tLRPC$Dialog.notify_settings = tLRPC$TL_peerNotifySettings;
                    if (i2 != Integer.MAX_VALUE || isGlobalNotificationsEnabled) {
                        tLRPC$TL_peerNotifySettings.mute_until = i2;
                    }
                }
            }
            getInstance(this.currentAccount).updateServerNotificationsSettings(j, i);
        }
    }

    public void cleanup() {
        this.popupMessages.clear();
        this.popupReplyMessages.clear();
        this.channelGroupsCreated = false;
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$cleanup$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanup$1() {
        this.openedDialogId = 0L;
        this.openedTopicId = 0;
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

    public void setOpenedDialogId(final long j, final int i) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$setOpenedDialogId$2(j, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setOpenedDialogId$2(long j, int i) {
        this.openedDialogId = j;
        this.openedTopicId = i;
    }

    public void setOpenedInBubble(final long j, final boolean z) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda39
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda19
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
            TLRPC$Message tLRPC$Message = messageObject.messageOwner;
            if ((!tLRPC$Message.mentioned || !(tLRPC$Message.action instanceof TLRPC$TL_messageActionPinMessage)) && !DialogObject.isEncryptedDialog(dialogId) && (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup())) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void forceShowPopupForReply() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda7
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda31
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda27
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
                    if (messageObject == null || (z && !messageObject.isReactionPush)) {
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda29
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda16
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
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeDeletedMessagesFromNotifications$8(int i) {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    public void removeDeletedHisoryFromNotifications(final LongSparseIntArray longSparseIntArray) {
        final ArrayList arrayList = new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda37
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda30
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda20
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
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeDeletedHisoryFromNotifications$11(int i) {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    public void processReadMessages(final LongSparseIntArray longSparseIntArray, final long j, final int i, final int i2, final boolean z) {
        final ArrayList arrayList = new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda38
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processReadMessages$14(longSparseIntArray, arrayList, j, i2, i, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00d7, code lost:
        r8 = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processReadMessages$14(LongSparseIntArray longSparseIntArray, final ArrayList arrayList, long j, int i, int i2, boolean z) {
        int i3;
        int i4 = 1;
        if (longSparseIntArray != null) {
            int i5 = 0;
            while (i5 < longSparseIntArray.size()) {
                long keyAt = longSparseIntArray.keyAt(i5);
                int i6 = longSparseIntArray.get(keyAt);
                int i7 = 0;
                while (i7 < this.pushMessages.size()) {
                    MessageObject messageObject = this.pushMessages.get(i7);
                    if (messageObject.messageOwner.from_scheduled || messageObject.getDialogId() != keyAt || messageObject.getId() > i6) {
                        i3 = i5;
                    } else {
                        if (isPersonalMessage(messageObject)) {
                            this.personalCount -= i4;
                        }
                        arrayList.add(messageObject);
                        i3 = i5;
                        long j2 = messageObject.messageOwner.peer_id.channel_id;
                        long j3 = j2 != 0 ? -j2 : 0L;
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
                if (messageObject2.getDialogId() == j) {
                    boolean z2 = i2 != 0 ? true : true;
                    if (z2) {
                        if (isPersonalMessage(messageObject2)) {
                            this.personalCount--;
                        }
                        long j4 = messageObject2.messageOwner.peer_id.channel_id;
                        long j5 = j4 != 0 ? -j4 : 0L;
                        SparseArray<MessageObject> sparseArray2 = this.pushMessagesDict.get(j5);
                        if (sparseArray2 != null) {
                            sparseArray2.remove(messageObject2.getId());
                            if (sparseArray2.size() == 0) {
                                this.pushMessagesDict.remove(j5);
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processReadMessages$13(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processReadMessages$13(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0056, code lost:
        if (r0 == 2) goto L30;
     */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0070  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int addToPopupMessages(ArrayList<MessageObject> arrayList, MessageObject messageObject, long j, boolean z, SharedPreferences sharedPreferences) {
        int i;
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processEditedMessages$15(longSparseArray);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processEditedMessages$15(LongSparseArray longSparseArray) {
        int size = longSparseArray.size();
        boolean z = false;
        for (int i = 0; i < size; i++) {
            longSparseArray.keyAt(i);
            ArrayList arrayList = (ArrayList) longSparseArray.valueAt(i);
            int size2 = arrayList.size();
            for (int i2 = 0; i2 < size2; i2++) {
                MessageObject messageObject = (MessageObject) arrayList.get(i2);
                long j = messageObject.messageOwner.peer_id.channel_id;
                SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(j != 0 ? -j : 0L);
                if (sparseArray == null) {
                    break;
                }
                MessageObject messageObject2 = sparseArray.get(messageObject.getId());
                if (messageObject2 != null && messageObject2.isReactionPush) {
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
            notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda35
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$processNewMessages$18(arrayList, arrayList2, z2, z, countDownLatch);
                }
            });
        } else if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0048, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserJoined) == false) goto L20;
     */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0145  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processNewMessages$18(ArrayList arrayList, final ArrayList arrayList2, boolean z, boolean z2, CountDownLatch countDownLatch) {
        boolean z3;
        Integer num;
        int i;
        long j;
        boolean z4;
        boolean z5;
        int i2;
        MessageObject messageObject;
        long j2;
        MessageObject messageObject2;
        long j3;
        boolean z6;
        int i3;
        LongSparseArray longSparseArray;
        SparseArray<MessageObject> sparseArray;
        long j4;
        SparseArray<MessageObject> sparseArray2;
        MessageObject messageObject3;
        ArrayList arrayList3 = arrayList;
        LongSparseArray longSparseArray2 = new LongSparseArray();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        boolean z7 = notificationsSettings.getBoolean("PinnedMessages", true);
        int i4 = 0;
        int i5 = 0;
        boolean z8 = false;
        boolean z9 = false;
        boolean z10 = false;
        while (i5 < arrayList.size()) {
            MessageObject messageObject4 = (MessageObject) arrayList3.get(i5);
            if (messageObject4.messageOwner != null) {
                if (!messageObject4.isImportedForward()) {
                    TLRPC$Message tLRPC$Message = messageObject4.messageOwner;
                    TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
                    if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetMessagesTTL)) {
                        if (tLRPC$Message.silent) {
                            if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp)) {
                            }
                        }
                    }
                }
                i2 = i4;
                z4 = z7;
                i = i5;
                longSparseArray = longSparseArray2;
                i4 = i2;
                i5 = i + 1;
                arrayList3 = arrayList;
                longSparseArray2 = longSparseArray;
                z7 = z4;
            }
            if (!MessageObject.isTopicActionMessage(messageObject4)) {
                int id = messageObject4.getId();
                if (messageObject4.isFcmMessage()) {
                    i = i5;
                    j = messageObject4.messageOwner.random_id;
                } else {
                    i = i5;
                    j = 0;
                }
                long dialogId = messageObject4.getDialogId();
                if (messageObject4.isFcmMessage()) {
                    z5 = messageObject4.localChannel;
                    z4 = z7;
                } else if (DialogObject.isChatDialog(dialogId)) {
                    z4 = z7;
                    TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-dialogId));
                    z5 = ChatObject.isChannel(chat) && !chat.megagroup;
                } else {
                    z4 = z7;
                    z5 = false;
                }
                long j5 = messageObject4.messageOwner.peer_id.channel_id;
                long j6 = j5 != 0 ? -j5 : 0L;
                SparseArray<MessageObject> sparseArray3 = this.pushMessagesDict.get(j6);
                MessageObject messageObject5 = sparseArray3 != null ? sparseArray3.get(id) : null;
                if (messageObject5 == null) {
                    i2 = i4;
                    messageObject = messageObject5;
                    long j7 = messageObject4.messageOwner.random_id;
                    if (j7 != 0) {
                        messageObject2 = this.fcmRandomMessagesDict.get(j7);
                        if (messageObject2 != null) {
                            j2 = j;
                            this.fcmRandomMessagesDict.remove(messageObject4.messageOwner.random_id);
                        } else {
                            j2 = j;
                        }
                        if (messageObject2 == null) {
                            if (messageObject2.isFcmMessage()) {
                                if (sparseArray3 == null) {
                                    sparseArray3 = new SparseArray<>();
                                    this.pushMessagesDict.put(j6, sparseArray3);
                                }
                                sparseArray3.put(id, messageObject4);
                                int indexOf = this.pushMessages.indexOf(messageObject2);
                                if (indexOf >= 0) {
                                    this.pushMessages.set(indexOf, messageObject4);
                                    messageObject3 = messageObject4;
                                    i4 = addToPopupMessages(arrayList2, messageObject4, dialogId, z5, notificationsSettings);
                                } else {
                                    messageObject3 = messageObject4;
                                    i4 = i2;
                                }
                                if (z) {
                                    boolean z11 = messageObject3.localEdit;
                                    if (z11) {
                                        getMessagesStorage().putPushMessage(messageObject3);
                                    }
                                    z9 = z11;
                                }
                                longSparseArray = longSparseArray2;
                                i5 = i + 1;
                                arrayList3 = arrayList;
                                longSparseArray2 = longSparseArray;
                                z7 = z4;
                            }
                            longSparseArray = longSparseArray2;
                            i4 = i2;
                            i5 = i + 1;
                            arrayList3 = arrayList;
                            longSparseArray2 = longSparseArray;
                            z7 = z4;
                        } else {
                            if (!z9) {
                                if (z) {
                                    getMessagesStorage().putPushMessage(messageObject4);
                                }
                                int topicId = MessageObject.getTopicId(messageObject4.messageOwner, getMessagesController().isForum(messageObject4));
                                if (dialogId != this.openedDialogId || !ApplicationLoader.isScreenOn) {
                                    TLRPC$Message tLRPC$Message2 = messageObject4.messageOwner;
                                    if (!tLRPC$Message2.mentioned) {
                                        j3 = dialogId;
                                    } else if (z4 || !(tLRPC$Message2.action instanceof TLRPC$TL_messageActionPinMessage)) {
                                        j3 = messageObject4.getFromChatId();
                                    }
                                    if (isPersonalMessage(messageObject4)) {
                                        this.personalCount++;
                                    }
                                    DialogObject.isChatDialog(j3);
                                    int indexOfKey = longSparseArray2.indexOfKey(j3);
                                    if (indexOfKey >= 0 && topicId == 0) {
                                        z6 = ((Boolean) longSparseArray2.valueAt(indexOfKey)).booleanValue();
                                    } else {
                                        int notifyOverride = getNotifyOverride(notificationsSettings, j3, topicId);
                                        if (notifyOverride == -1) {
                                            z6 = isGlobalNotificationsEnabled(j3, Boolean.valueOf(z5));
                                        } else {
                                            z6 = notifyOverride != 2;
                                        }
                                        longSparseArray2.put(j3, Boolean.valueOf(z6));
                                    }
                                    if (z6) {
                                        if (z) {
                                            sparseArray = sparseArray3;
                                            i3 = id;
                                            longSparseArray = longSparseArray2;
                                            j4 = j6;
                                            i4 = i2;
                                        } else {
                                            sparseArray = sparseArray3;
                                            longSparseArray = longSparseArray2;
                                            j4 = j6;
                                            boolean z12 = z5;
                                            i3 = id;
                                            i4 = addToPopupMessages(arrayList2, messageObject4, j3, z12, notificationsSettings);
                                        }
                                        if (!z10) {
                                            z10 = messageObject4.messageOwner.from_scheduled;
                                        }
                                        this.delayedPushMessages.add(messageObject4);
                                        appendMessage(messageObject4);
                                        if (i3 != 0) {
                                            if (sparseArray == null) {
                                                sparseArray2 = new SparseArray<>();
                                                this.pushMessagesDict.put(j4, sparseArray2);
                                            } else {
                                                sparseArray2 = sparseArray;
                                            }
                                            sparseArray2.put(i3, messageObject4);
                                        } else if (j2 != 0) {
                                            this.fcmRandomMessagesDict.put(j2, messageObject4);
                                        }
                                        if (dialogId != j3) {
                                            Integer num2 = this.pushDialogsOverrideMention.get(dialogId);
                                            this.pushDialogsOverrideMention.put(dialogId, Integer.valueOf(num2 == null ? 1 : num2.intValue() + 1));
                                        }
                                    } else {
                                        i3 = id;
                                        longSparseArray = longSparseArray2;
                                        i4 = i2;
                                    }
                                    if (messageObject4.isReactionPush) {
                                        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
                                        sparseBooleanArray.put(i3, true);
                                        getMessagesController().checkUnreadReactions(j3, topicId, sparseBooleanArray);
                                    }
                                    z8 = true;
                                    i5 = i + 1;
                                    arrayList3 = arrayList;
                                    longSparseArray2 = longSparseArray;
                                    z7 = z4;
                                } else if (!z) {
                                    playInChatSound();
                                }
                            }
                            longSparseArray = longSparseArray2;
                            i4 = i2;
                            i5 = i + 1;
                            arrayList3 = arrayList;
                            longSparseArray2 = longSparseArray;
                            z7 = z4;
                        }
                    }
                } else {
                    i2 = i4;
                    messageObject = messageObject5;
                }
                j2 = j;
                messageObject2 = messageObject;
                if (messageObject2 == null) {
                }
            }
            i2 = i4;
            z4 = z7;
            i = i5;
            longSparseArray = longSparseArray2;
            i4 = i2;
            i5 = i + 1;
            arrayList3 = arrayList;
            longSparseArray2 = longSparseArray;
            z7 = z4;
        }
        final int i6 = i4;
        if (z8) {
            this.notifyCheck = z2;
        }
        if (!arrayList2.isEmpty() && !AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda33
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$processNewMessages$16(arrayList2, i6);
                }
            });
        }
        if (z || z10) {
            if (z9) {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
            } else if (z8) {
                MessageObject messageObject6 = (MessageObject) arrayList.get(0);
                long dialogId2 = messageObject6.getDialogId();
                int topicId2 = MessageObject.getTopicId(messageObject6.messageOwner, getMessagesController().isForum(dialogId2));
                Boolean valueOf = messageObject6.isFcmMessage() ? Boolean.valueOf(messageObject6.localChannel) : null;
                int i7 = this.total_unread_count;
                int notifyOverride2 = getNotifyOverride(notificationsSettings, dialogId2, topicId2);
                if (notifyOverride2 == -1) {
                    z3 = isGlobalNotificationsEnabled(dialogId2, valueOf);
                } else {
                    z3 = notifyOverride2 != 2;
                }
                Integer num3 = this.pushDialogs.get(dialogId2);
                int intValue = num3 != null ? num3.intValue() + 1 : 1;
                if (this.notifyCheck && !z3 && (num = this.pushDialogsOverrideMention.get(dialogId2)) != null && num.intValue() != 0) {
                    intValue = num.intValue();
                    z3 = true;
                }
                if (z3) {
                    if (getMessagesController().isForum(dialogId2)) {
                        int i8 = this.total_unread_count - ((num3 == null || num3.intValue() <= 0) ? 0 : 1);
                        this.total_unread_count = i8;
                        this.total_unread_count = i8 + (intValue > 0 ? 1 : 0);
                    } else {
                        if (num3 != null) {
                            this.total_unread_count -= num3.intValue();
                        }
                        this.total_unread_count += intValue;
                    }
                    this.pushDialogs.put(dialogId2, Integer.valueOf(intValue));
                }
                if (i7 != this.total_unread_count) {
                    this.delayedPushMessages.clear();
                    showOrUpdateNotification(this.notifyCheck);
                    final int size = this.pushDialogs.size();
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda21
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationsController.this.lambda$processNewMessages$17(size);
                        }
                    });
                }
                this.notifyCheck = false;
                if (this.showBadgeNumber) {
                    setBadge(getTotalAllUnreadCount());
                }
            }
        }
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processNewMessages$16(ArrayList arrayList, int i) {
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
    public /* synthetic */ void lambda$processNewMessages$17(int i) {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    private void appendMessage(MessageObject messageObject) {
        for (int i = 0; i < this.pushMessages.size(); i++) {
            if (this.pushMessages.get(i).getId() == messageObject.getId() && this.pushMessages.get(i).getDialogId() == messageObject.getDialogId()) {
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processDialogsUpdateRead$21(longSparseIntArray, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0051  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0068 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0093 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00b0  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00bb  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0129  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processDialogsUpdateRead$21(LongSparseIntArray longSparseIntArray, final ArrayList arrayList) {
        boolean z;
        boolean z2;
        Integer num;
        int i = this.total_unread_count;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        int i2 = 0;
        while (true) {
            if (i2 >= longSparseIntArray.size()) {
                break;
            }
            long keyAt = longSparseIntArray.keyAt(i2);
            Integer num2 = this.pushDialogs.get(keyAt);
            int i3 = longSparseIntArray.get(keyAt);
            if (DialogObject.isChatDialog(keyAt)) {
                TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-keyAt));
                i3 = (chat == null || chat.min || ChatObject.isNotInChat(chat)) ? 0 : 0;
                if (chat != null) {
                    z = chat.forum;
                    if (!z) {
                        int notifyOverride = getNotifyOverride(notificationsSettings, keyAt, 0);
                        if (notifyOverride == -1) {
                            z2 = isGlobalNotificationsEnabled(keyAt);
                        } else if (notifyOverride == 2) {
                            z2 = false;
                        }
                        if (this.notifyCheck && !z2 && (num = this.pushDialogsOverrideMention.get(keyAt)) != null && num.intValue() != 0) {
                            i3 = num.intValue();
                            z2 = true;
                        }
                        if (i3 == 0) {
                            this.smartNotificationsDialogs.remove(keyAt);
                        }
                        if (i3 < 0) {
                            if (num2 == null) {
                                i2++;
                            } else {
                                i3 += num2.intValue();
                            }
                        }
                        if ((!z2 || i3 == 0) && num2 != null) {
                            if (getMessagesController().isForum(keyAt)) {
                                this.total_unread_count -= num2.intValue() > 0 ? 1 : 0;
                            } else {
                                this.total_unread_count -= num2.intValue();
                            }
                        }
                        if (i3 == 0) {
                            this.pushDialogs.remove(keyAt);
                            this.pushDialogsOverrideMention.remove(keyAt);
                            int i4 = 0;
                            while (i4 < this.pushMessages.size()) {
                                MessageObject messageObject = this.pushMessages.get(i4);
                                if (!messageObject.messageOwner.from_scheduled && messageObject.getDialogId() == keyAt) {
                                    if (isPersonalMessage(messageObject)) {
                                        this.personalCount--;
                                    }
                                    this.pushMessages.remove(i4);
                                    i4--;
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
                                i4++;
                            }
                        } else if (z2) {
                            if (getMessagesController().isForum(keyAt)) {
                                this.total_unread_count += i3 <= 0 ? 0 : 1;
                            } else {
                                this.total_unread_count += i3;
                            }
                            this.pushDialogs.put(keyAt, Integer.valueOf(i3));
                        }
                        i2++;
                    }
                    z2 = true;
                    if (this.notifyCheck) {
                        i3 = num.intValue();
                        z2 = true;
                    }
                    if (i3 == 0) {
                    }
                    if (i3 < 0) {
                    }
                    if (!z2) {
                    }
                    if (getMessagesController().isForum(keyAt)) {
                    }
                    if (i3 == 0) {
                    }
                    i2++;
                }
            }
            z = false;
            if (!z) {
            }
            z2 = true;
            if (this.notifyCheck) {
            }
            if (i3 == 0) {
            }
            if (i3 < 0) {
            }
            if (!z2) {
            }
            if (getMessagesController().isForum(keyAt)) {
            }
            if (i3 == 0) {
            }
            i2++;
        }
        if (!arrayList.isEmpty()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda28
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$processDialogsUpdateRead$19(arrayList);
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$processDialogsUpdateRead$20(size);
                }
            });
        }
        this.notifyCheck = false;
        if (this.showBadgeNumber) {
            setBadge(getTotalAllUnreadCount());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDialogsUpdateRead$19(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDialogsUpdateRead$20(int i) {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    public void processLoadedUnreadMessages(final LongSparseArray<Integer> longSparseArray, final ArrayList<TLRPC$Message> arrayList, final ArrayList<MessageObject> arrayList2, ArrayList<TLRPC$User> arrayList3, ArrayList<TLRPC$Chat> arrayList4, ArrayList<TLRPC$EncryptedChat> arrayList5) {
        getMessagesController().putUsers(arrayList3, true);
        getMessagesController().putChats(arrayList4, true);
        getMessagesController().putEncryptedChats(arrayList5, true);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processLoadedUnreadMessages$23(arrayList, longSparseArray, arrayList2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedUnreadMessages$23(ArrayList arrayList, LongSparseArray longSparseArray, ArrayList arrayList2) {
        boolean z;
        boolean z2;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        int i;
        SparseArray<MessageObject> sparseArray;
        long j;
        boolean z3;
        SparseArray<MessageObject> sparseArray2;
        ArrayList arrayList3 = arrayList;
        this.pushDialogs.clear();
        this.pushMessages.clear();
        this.pushMessagesDict.clear();
        boolean z4 = false;
        this.total_unread_count = 0;
        this.personalCount = 0;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        LongSparseArray longSparseArray2 = new LongSparseArray();
        long j2 = 0;
        int i2 = 1;
        if (arrayList3 != null) {
            int i3 = 0;
            while (i3 < arrayList.size()) {
                TLRPC$Message tLRPC$Message = (TLRPC$Message) arrayList3.get(i3);
                if (tLRPC$Message != null && ((tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from) == null || !tLRPC$MessageFwdHeader.imported)) {
                    TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
                    if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetMessagesTTL) && (!tLRPC$Message.silent || (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp) && !(tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserJoined)))) {
                        long j3 = tLRPC$Message.peer_id.channel_id;
                        long j4 = j3 != j2 ? -j3 : j2;
                        SparseArray<MessageObject> sparseArray3 = this.pushMessagesDict.get(j4);
                        if (sparseArray3 == null || sparseArray3.indexOfKey(tLRPC$Message.id) < 0) {
                            MessageObject messageObject = new MessageObject(this.currentAccount, tLRPC$Message, z4, z4);
                            if (isPersonalMessage(messageObject)) {
                                this.personalCount += i2;
                            }
                            i = i3;
                            long dialogId = messageObject.getDialogId();
                            int topicId = MessageObject.getTopicId(messageObject.messageOwner, getMessagesController().isForum(messageObject));
                            if (messageObject.messageOwner.mentioned) {
                                sparseArray = sparseArray3;
                                j = messageObject.getFromChatId();
                            } else {
                                sparseArray = sparseArray3;
                                j = dialogId;
                            }
                            int indexOfKey = longSparseArray2.indexOfKey(j);
                            if (indexOfKey >= 0 && topicId == 0) {
                                z3 = ((Boolean) longSparseArray2.valueAt(indexOfKey)).booleanValue();
                            } else {
                                int notifyOverride = getNotifyOverride(notificationsSettings, j, topicId);
                                if (notifyOverride == -1) {
                                    z3 = isGlobalNotificationsEnabled(j);
                                } else {
                                    z3 = notifyOverride != 2;
                                }
                                longSparseArray2.put(j, Boolean.valueOf(z3));
                            }
                            if (z3 && (j != this.openedDialogId || !ApplicationLoader.isScreenOn)) {
                                if (sparseArray == null) {
                                    sparseArray2 = new SparseArray<>();
                                    this.pushMessagesDict.put(j4, sparseArray2);
                                } else {
                                    sparseArray2 = sparseArray;
                                }
                                sparseArray2.put(tLRPC$Message.id, messageObject);
                                appendMessage(messageObject);
                                if (dialogId != j) {
                                    Integer num = this.pushDialogsOverrideMention.get(dialogId);
                                    this.pushDialogsOverrideMention.put(dialogId, Integer.valueOf(num == null ? 1 : num.intValue() + 1));
                                }
                            }
                            i3 = i + 1;
                            arrayList3 = arrayList;
                            z4 = false;
                            j2 = 0;
                            i2 = 1;
                        }
                    }
                }
                i = i3;
                i3 = i + 1;
                arrayList3 = arrayList;
                z4 = false;
                j2 = 0;
                i2 = 1;
            }
        }
        for (int i4 = 0; i4 < longSparseArray.size(); i4++) {
            long keyAt = longSparseArray.keyAt(i4);
            int indexOfKey2 = longSparseArray2.indexOfKey(keyAt);
            if (indexOfKey2 >= 0) {
                z2 = ((Boolean) longSparseArray2.valueAt(indexOfKey2)).booleanValue();
            } else {
                int notifyOverride2 = getNotifyOverride(notificationsSettings, keyAt, 0);
                if (notifyOverride2 == -1) {
                    z2 = isGlobalNotificationsEnabled(keyAt);
                } else {
                    z2 = notifyOverride2 != 2;
                }
                longSparseArray2.put(keyAt, Boolean.valueOf(z2));
            }
            if (z2) {
                int intValue = ((Integer) longSparseArray.valueAt(i4)).intValue();
                this.pushDialogs.put(keyAt, Integer.valueOf(intValue));
                if (getMessagesController().isForum(keyAt)) {
                    this.total_unread_count += intValue > 0 ? 1 : 0;
                } else {
                    this.total_unread_count += intValue;
                }
            }
        }
        if (arrayList2 != null) {
            for (int i5 = 0; i5 < arrayList2.size(); i5++) {
                MessageObject messageObject2 = (MessageObject) arrayList2.get(i5);
                int id = messageObject2.getId();
                if (this.pushMessagesDict.indexOfKey(id) < 0) {
                    if (isPersonalMessage(messageObject2)) {
                        this.personalCount++;
                    }
                    long dialogId2 = messageObject2.getDialogId();
                    int topicId2 = MessageObject.getTopicId(messageObject2.messageOwner, getMessagesController().isForum(messageObject2));
                    TLRPC$Message tLRPC$Message2 = messageObject2.messageOwner;
                    long j5 = tLRPC$Message2.random_id;
                    long fromChatId = tLRPC$Message2.mentioned ? messageObject2.getFromChatId() : dialogId2;
                    int indexOfKey3 = longSparseArray2.indexOfKey(fromChatId);
                    if (indexOfKey3 >= 0 && topicId2 == 0) {
                        z = ((Boolean) longSparseArray2.valueAt(indexOfKey3)).booleanValue();
                    } else {
                        int notifyOverride3 = getNotifyOverride(notificationsSettings, fromChatId, topicId2);
                        if (notifyOverride3 == -1) {
                            z = isGlobalNotificationsEnabled(fromChatId);
                        } else {
                            z = notifyOverride3 != 2;
                        }
                        longSparseArray2.put(fromChatId, Boolean.valueOf(z));
                    }
                    if (z && (fromChatId != this.openedDialogId || !ApplicationLoader.isScreenOn)) {
                        if (id != 0) {
                            long j6 = messageObject2.messageOwner.peer_id.channel_id;
                            long j7 = j6 != 0 ? -j6 : 0L;
                            SparseArray<MessageObject> sparseArray4 = this.pushMessagesDict.get(j7);
                            if (sparseArray4 == null) {
                                sparseArray4 = new SparseArray<>();
                                this.pushMessagesDict.put(j7, sparseArray4);
                            }
                            sparseArray4.put(id, messageObject2);
                        } else if (j5 != 0) {
                            this.fcmRandomMessagesDict.put(j5, messageObject2);
                        }
                        appendMessage(messageObject2);
                        if (dialogId2 != fromChatId) {
                            Integer num2 = this.pushDialogsOverrideMention.get(dialogId2);
                            this.pushDialogsOverrideMention.put(dialogId2, Integer.valueOf(num2 == null ? 1 : num2.intValue() + 1));
                        }
                        Integer num3 = this.pushDialogs.get(fromChatId);
                        int intValue2 = num3 != null ? num3.intValue() + 1 : 1;
                        if (getMessagesController().isForum(fromChatId)) {
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
                        this.pushDialogs.put(fromChatId, Integer.valueOf(intValue2));
                    }
                }
            }
        }
        final int size = this.pushDialogs.size();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processLoadedUnreadMessages$22(size);
            }
        });
        showOrUpdateNotification(SystemClock.elapsedRealtime() / 1000 < 60);
        if (this.showBadgeNumber) {
            setBadge(getTotalAllUnreadCount());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedUnreadMessages$22(int i) {
        if (this.total_unread_count == 0) {
            this.popupMessages.clear();
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
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
    public /* synthetic */ void lambda$updateBadge$24() {
        setBadge(getTotalAllUnreadCount());
    }

    public void updateBadge() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$updateBadge$24();
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
        if (r12.getBoolean("EnablePreviewAll", true) == false) goto L781;
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
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageService) == false) goto L671;
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
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserJoined) != false) goto L669;
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
        r1 = org.telegram.messenger.LocaleController.formatString("formatDateAtTime", org.telegram.messenger.R.string.formatDateAtTime, org.telegram.messenger.LocaleController.getInstance().formatterYear.format(r26.messageOwner.date * 1000), org.telegram.messenger.LocaleController.getInstance().formatterDay.format(r26.messageOwner.date * 1000));
        r2 = org.telegram.messenger.R.string.NotificationUnrecognizedDevice;
        r0 = r26.messageOwner.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x0324, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationUnrecognizedDevice", r2, getUserConfig().getCurrentUser().first_name, r1, r0.title, r0.address);
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x0327, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGameScore) != false) goto L667;
     */
    /* JADX WARN: Code restructure failed: missing block: B:195:0x032b, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPaymentSent) == false) goto L165;
     */
    /* JADX WARN: Code restructure failed: missing block: B:198:0x0331, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPhoneCall) == false) goto L173;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x0335, code lost:
        if (r2.video == false) goto L171;
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x033f, code lost:
        return org.telegram.messenger.LocaleController.getString("CallMessageVideoIncomingMissed", org.telegram.messenger.R.string.CallMessageVideoIncomingMissed);
     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x0348, code lost:
        return org.telegram.messenger.LocaleController.getString("CallMessageIncomingMissed", org.telegram.messenger.R.string.CallMessageIncomingMissed);
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x034b, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser) == false) goto L220;
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x034d, code lost:
        r3 = r2.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:208:0x0353, code lost:
        if (r3 != 0) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x035c, code lost:
        if (r2.users.size() != 1) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:211:0x035e, code lost:
        r3 = r26.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:213:0x0373, code lost:
        if (r3 == 0) goto L205;
     */
    /* JADX WARN: Code restructure failed: missing block: B:215:0x037d, code lost:
        if (r26.messageOwner.peer_id.channel_id == 0) goto L188;
     */
    /* JADX WARN: Code restructure failed: missing block: B:217:0x0381, code lost:
        if (r8.megagroup != false) goto L188;
     */
    /* JADX WARN: Code restructure failed: missing block: B:219:0x0396, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.R.string.ChannelAddedByNotification, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:221:0x0399, code lost:
        if (r3 != r19) goto L192;
     */
    /* JADX WARN: Code restructure failed: missing block: B:223:0x03ae, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.R.string.NotificationInvitedToGroup, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:224:0x03af, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:225:0x03bb, code lost:
        if (r0 != null) goto L195;
     */
    /* JADX WARN: Code restructure failed: missing block: B:226:0x03bd, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:229:0x03c3, code lost:
        if (r9 != r0.id) goto L203;
     */
    /* JADX WARN: Code restructure failed: missing block: B:231:0x03c7, code lost:
        if (r8.megagroup == false) goto L201;
     */
    /* JADX WARN: Code restructure failed: missing block: B:233:0x03dc, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.R.string.NotificationGroupAddSelfMega, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:235:0x03f0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.R.string.NotificationGroupAddSelf, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:237:0x040a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r7, r8.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:238:0x040b, code lost:
        r1 = new java.lang.StringBuilder();
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x041b, code lost:
        if (r2 >= r26.messageOwner.action.users.size()) goto L217;
     */
    /* JADX WARN: Code restructure failed: missing block: B:241:0x041d, code lost:
        r3 = getMessagesController().getUser(r26.messageOwner.action.users.get(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:242:0x0431, code lost:
        if (r3 == null) goto L216;
     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x0433, code lost:
        r3 = org.telegram.messenger.UserObject.getUserName(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x043b, code lost:
        if (r1.length() == 0) goto L213;
     */
    /* JADX WARN: Code restructure failed: missing block: B:245:0x043d, code lost:
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:246:0x0442, code lost:
        r1.append(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:247:0x0445, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:249:0x0461, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r7, r8.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:251:0x0465, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCall) == false) goto L224;
     */
    /* JADX WARN: Code restructure failed: missing block: B:253:0x0479, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.R.string.NotificationGroupCreatedCall, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:255:0x047c, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled) == false) goto L228;
     */
    /* JADX WARN: Code restructure failed: missing block: B:257:0x0484, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:259:0x0487, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall) == false) goto L261;
     */
    /* JADX WARN: Code restructure failed: missing block: B:260:0x0489, code lost:
        r3 = r2.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:261:0x048f, code lost:
        if (r3 != 0) goto L235;
     */
    /* JADX WARN: Code restructure failed: missing block: B:263:0x0498, code lost:
        if (r2.users.size() != 1) goto L235;
     */
    /* JADX WARN: Code restructure failed: missing block: B:264:0x049a, code lost:
        r3 = r26.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x04af, code lost:
        if (r3 == 0) goto L246;
     */
    /* JADX WARN: Code restructure failed: missing block: B:268:0x04b3, code lost:
        if (r3 != r19) goto L241;
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x04c8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.R.string.NotificationGroupInvitedYouToCall, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:271:0x04c9, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x04d5, code lost:
        if (r0 != null) goto L244;
     */
    /* JADX WARN: Code restructure failed: missing block: B:273:0x04d7, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x04f2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r7, r8.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:277:0x04f3, code lost:
        r1 = new java.lang.StringBuilder();
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x0503, code lost:
        if (r2 >= r26.messageOwner.action.users.size()) goto L258;
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x0505, code lost:
        r3 = getMessagesController().getUser(r26.messageOwner.action.users.get(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x0519, code lost:
        if (r3 == null) goto L257;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x051b, code lost:
        r3 = org.telegram.messenger.UserObject.getUserName(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x0523, code lost:
        if (r1.length() == 0) goto L254;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x0525, code lost:
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x052a, code lost:
        r1.append(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x052d, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x0549, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r7, r8.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x054d, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink) == false) goto L265;
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x0562, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.R.string.NotificationInvitedToGroupByLink, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x0568, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle) == false) goto L269;
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x057a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.R.string.NotificationEditedGroupName, r7, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x057d, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto) != false) goto L651;
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x0581, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto) == false) goto L273;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x0587, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser) == false) goto L288;
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x0589, code lost:
        r1 = r2.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x058d, code lost:
        if (r1 != r19) goto L279;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x05a2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.R.string.NotificationGroupKickYou, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x05a8, code lost:
        if (r1 != r9) goto L283;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x05ba, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.R.string.NotificationGroupLeftMember, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x05bb, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r26.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x05cd, code lost:
        if (r0 != null) goto L286;
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x05cf, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x05eb, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.R.string.NotificationGroupKickMember, r7, r8.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x05ee, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatCreate) == false) goto L292;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x05f6, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x05f9, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate) == false) goto L296;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x0601, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x0604, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo) == false) goto L300;
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x0616, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x061b, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) == false) goto L304;
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x062b, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x062e, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken) == false) goto L308;
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x0636, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x0639, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPinMessage) == false) goto L630;
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x063f, code lost:
        if (r8 == null) goto L420;
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x0645, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r8) == false) goto L316;
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x0649, code lost:
        if (r8.megagroup == false) goto L420;
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x064b, code lost:
        r0 = r26.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:347:0x064d, code lost:
        if (r0 != null) goto L320;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x0662, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x066a, code lost:
        if (r0.isMusic() == false) goto L324;
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x067c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.R.string.NotificationActionPinnedMusic, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x0683, code lost:
        if (r0.isVideo() == false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x0689, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L332;
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x0693, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L332;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x06bc, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "📹 " + r0.messageOwner.message, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x06d0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.R.string.NotificationActionPinnedVideo, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x06d5, code lost:
        if (r0.isGif() == false) goto L344;
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x06db, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L342;
     */
    /* JADX WARN: Code restructure failed: missing block: B:369:0x06e5, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L342;
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x070e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "🎬 " + r0.messageOwner.message, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x0722, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.R.string.NotificationActionPinnedGif, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:375:0x072a, code lost:
        if (r0.isVoice() == false) goto L348;
     */
    /* JADX WARN: Code restructure failed: missing block: B:377:0x073c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.R.string.NotificationActionPinnedVoice, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x0741, code lost:
        if (r0.isRoundVideo() == false) goto L352;
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x0753, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.R.string.NotificationActionPinnedRound, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x0758, code lost:
        if (r0.isSticker() != false) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:385:0x075e, code lost:
        if (r0.isAnimatedSticker() == false) goto L356;
     */
    /* JADX WARN: Code restructure failed: missing block: B:387:0x0762, code lost:
        r3 = r0.messageOwner;
        r5 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:388:0x0768, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:390:0x076e, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L364;
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x0776, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L364;
     */
    /* JADX WARN: Code restructure failed: missing block: B:394:0x079f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "📎 " + r0.messageOwner.message, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:396:0x07b3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.R.string.NotificationActionPinnedFile, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:0x07b6, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L412;
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x07ba, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L370;
     */
    /* JADX WARN: Code restructure failed: missing block: B:403:0x07c0, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L374;
     */
    /* JADX WARN: Code restructure failed: missing block: B:405:0x07d5, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.R.string.NotificationActionPinnedGeoLive, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x07da, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L378;
     */
    /* JADX WARN: Code restructure failed: missing block: B:408:0x07dc, code lost:
        r5 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:409:0x07fa, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.R.string.NotificationActionPinnedContact2, r7, r8.title, org.telegram.messenger.ContactsController.formatName(r5.first_name, r5.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x07fd, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L386;
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x07ff, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r5).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x0805, code lost:
        if (r0.quiz == false) goto L384;
     */
    /* JADX WARN: Code restructure failed: missing block: B:415:0x081f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.R.string.NotificationActionPinnedQuiz2, r7, r8.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x0838, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.R.string.NotificationActionPinnedPoll2, r7, r8.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x083b, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L396;
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x0841, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L394;
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x0849, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L394;
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x0872, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "🖼 " + r0.messageOwner.message, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x0886, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.R.string.NotificationActionPinnedPhoto, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x088c, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L400;
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x089e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.R.string.NotificationActionPinnedGame, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:432:0x089f, code lost:
        r3 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:433:0x08a1, code lost:
        if (r3 == null) goto L410;
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x08a7, code lost:
        if (r3.length() <= 0) goto L410;
     */
    /* JADX WARN: Code restructure failed: missing block: B:436:0x08a9, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x08af, code lost:
        if (r0.length() <= 20) goto L409;
     */
    /* JADX WARN: Code restructure failed: missing block: B:438:0x08b1, code lost:
        r3 = new java.lang.StringBuilder();
        r5 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x08c6, code lost:
        r5 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:440:0x08c7, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedText;
        r2 = new java.lang.Object[3];
        r2[r5] = r7;
        r2[1] = r0;
        r2[2] = r8.title;
     */
    /* JADX WARN: Code restructure failed: missing block: B:441:0x08da, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:443:0x08ee, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:445:0x0902, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.R.string.NotificationActionPinnedGeo, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:446:0x0903, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:447:0x0909, code lost:
        if (r0 == null) goto L418;
     */
    /* JADX WARN: Code restructure failed: missing block: B:449:0x091f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmoji, r7, r8.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:451:0x0931, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.R.string.NotificationActionPinnedSticker, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:453:0x0933, code lost:
        if (r8 == null) goto L526;
     */
    /* JADX WARN: Code restructure failed: missing block: B:454:0x0935, code lost:
        r0 = r26.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:455:0x0937, code lost:
        if (r0 != null) goto L426;
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:0x0948, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x094e, code lost:
        if (r0.isMusic() == false) goto L430;
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x095e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.R.string.NotificationActionPinnedMusicChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0965, code lost:
        if (r0.isVideo() == false) goto L440;
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x096b, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L438;
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x0975, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L438;
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x099b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r8.title, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00b3, code lost:
        if (r12.getBoolean("EnablePreviewGroup", true) != false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x09ac, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.R.string.NotificationActionPinnedVideoChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x09b1, code lost:
        if (r0.isGif() == false) goto L450;
     */
    /* JADX WARN: Code restructure failed: missing block: B:475:0x09b7, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L448;
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x09c1, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L448;
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x09e7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r8.title, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x09f8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.R.string.NotificationActionPinnedGifChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x09ff, code lost:
        if (r0.isVoice() == false) goto L454;
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0a0f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.R.string.NotificationActionPinnedVoiceChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:487:0x0a14, code lost:
        if (r0.isRoundVideo() == false) goto L458;
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x0a24, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.R.string.NotificationActionPinnedRoundChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x0a29, code lost:
        if (r0.isSticker() != false) goto L520;
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x0a2f, code lost:
        if (r0.isAnimatedSticker() == false) goto L462;
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:0x0a33, code lost:
        r3 = r0.messageOwner;
        r5 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:496:0x0a39, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L472;
     */
    /* JADX WARN: Code restructure failed: missing block: B:498:0x0a3f, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L470;
     */
    /* JADX WARN: Code restructure failed: missing block: B:500:0x0a47, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L470;
     */
    /* JADX WARN: Code restructure failed: missing block: B:502:0x0a6d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r8.title, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0a7e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.R.string.NotificationActionPinnedFileChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:506:0x0a81, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L518;
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x0a85, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L476;
     */
    /* JADX WARN: Code restructure failed: missing block: B:511:0x0a8b, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L480;
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x0a9d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x0aa1, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L484;
     */
    /* JADX WARN: Code restructure failed: missing block: B:516:0x0aa3, code lost:
        r5 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0abf, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.R.string.NotificationActionPinnedContactChannel2, r8.title, org.telegram.messenger.ContactsController.formatName(r5.first_name, r5.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:519:0x0ac2, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L492;
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x0ac4, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r5).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x0aca, code lost:
        if (r0.quiz == false) goto L490;
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x0ae1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.R.string.NotificationActionPinnedQuizChannel2, r8.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:525:0x0af7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.R.string.NotificationActionPinnedPollChannel2, r8.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:527:0x0afa, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L502;
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x0b00, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L500;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00bf, code lost:
        if (r12.getBoolean("EnablePreviewChannel", r2) == false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x0b08, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L500;
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0b2e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r8.title, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0b3f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.R.string.NotificationActionPinnedPhotoChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0b44, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L506;
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0b54, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.R.string.NotificationActionPinnedGameChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:540:0x0b55, code lost:
        r3 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x0b57, code lost:
        if (r3 == null) goto L516;
     */
    /* JADX WARN: Code restructure failed: missing block: B:543:0x0b5d, code lost:
        if (r3.length() <= 0) goto L516;
     */
    /* JADX WARN: Code restructure failed: missing block: B:544:0x0b5f, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x0b65, code lost:
        if (r0.length() <= 20) goto L515;
     */
    /* JADX WARN: Code restructure failed: missing block: B:546:0x0b67, code lost:
        r3 = new java.lang.StringBuilder();
        r9 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0b7c, code lost:
        r9 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:548:0x0b7d, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextChannel;
        r2 = new java.lang.Object[2];
        r2[r9] = r8.title;
        r2[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0b8d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:551:0x0b9e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:553:0x0baf, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:554:0x0bb0, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:555:0x0bb5, code lost:
        if (r0 == null) goto L524;
     */
    /* JADX WARN: Code restructure failed: missing block: B:557:0x0bc9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiChannel, r8.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:559:0x0bd9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:560:0x0bda, code lost:
        r0 = r26.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0bdd, code lost:
        if (r0 != null) goto L530;
     */
    /* JADX WARN: Code restructure failed: missing block: B:563:0x0beb, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.R.string.NotificationActionPinnedNoTextUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:565:0x0bf0, code lost:
        if (r0.isMusic() == false) goto L534;
     */
    /* JADX WARN: Code restructure failed: missing block: B:567:0x0bfe, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicUser", org.telegram.messenger.R.string.NotificationActionPinnedMusicUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x0c05, code lost:
        if (r0.isVideo() == false) goto L544;
     */
    /* JADX WARN: Code restructure failed: missing block: B:571:0x0c0b, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L542;
     */
    /* JADX WARN: Code restructure failed: missing block: B:573:0x0c15, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L542;
     */
    /* JADX WARN: Code restructure failed: missing block: B:575:0x0c39, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r7, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:577:0x0c48, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoUser", org.telegram.messenger.R.string.NotificationActionPinnedVideoUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:579:0x0c4d, code lost:
        if (r0.isGif() == false) goto L554;
     */
    /* JADX WARN: Code restructure failed: missing block: B:581:0x0c53, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L552;
     */
    /* JADX WARN: Code restructure failed: missing block: B:583:0x0c5d, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L552;
     */
    /* JADX WARN: Code restructure failed: missing block: B:585:0x0c81, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r7, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:587:0x0c90, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifUser", org.telegram.messenger.R.string.NotificationActionPinnedGifUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:589:0x0c97, code lost:
        if (r0.isVoice() == false) goto L558;
     */
    /* JADX WARN: Code restructure failed: missing block: B:591:0x0ca5, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceUser", org.telegram.messenger.R.string.NotificationActionPinnedVoiceUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:593:0x0caa, code lost:
        if (r0.isRoundVideo() == false) goto L562;
     */
    /* JADX WARN: Code restructure failed: missing block: B:595:0x0cb8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundUser", org.telegram.messenger.R.string.NotificationActionPinnedRoundUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:597:0x0cbd, code lost:
        if (r0.isSticker() != false) goto L624;
     */
    /* JADX WARN: Code restructure failed: missing block: B:599:0x0cc3, code lost:
        if (r0.isAnimatedSticker() == false) goto L566;
     */
    /* JADX WARN: Code restructure failed: missing block: B:601:0x0cc7, code lost:
        r3 = r0.messageOwner;
        r5 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:602:0x0ccd, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L576;
     */
    /* JADX WARN: Code restructure failed: missing block: B:604:0x0cd3, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L574;
     */
    /* JADX WARN: Code restructure failed: missing block: B:606:0x0cdb, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L574;
     */
    /* JADX WARN: Code restructure failed: missing block: B:608:0x0cff, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r7, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:610:0x0d0e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileUser", org.telegram.messenger.R.string.NotificationActionPinnedFileUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:612:0x0d11, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L622;
     */
    /* JADX WARN: Code restructure failed: missing block: B:614:0x0d15, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L580;
     */
    /* JADX WARN: Code restructure failed: missing block: B:617:0x0d1b, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L584;
     */
    /* JADX WARN: Code restructure failed: missing block: B:619:0x0d2b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveUser", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:621:0x0d2f, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L588;
     */
    /* JADX WARN: Code restructure failed: missing block: B:622:0x0d31, code lost:
        r5 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:623:0x0d4b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactUser", org.telegram.messenger.R.string.NotificationActionPinnedContactUser, r7, org.telegram.messenger.ContactsController.formatName(r5.first_name, r5.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x0d4e, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L596;
     */
    /* JADX WARN: Code restructure failed: missing block: B:626:0x0d50, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r5).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:627:0x0d56, code lost:
        if (r0.quiz == false) goto L594;
     */
    /* JADX WARN: Code restructure failed: missing block: B:629:0x0d6b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizUser", org.telegram.messenger.R.string.NotificationActionPinnedQuizUser, r7, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:0x0d7f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollUser", org.telegram.messenger.R.string.NotificationActionPinnedPollUser, r7, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:633:0x0d82, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L606;
     */
    /* JADX WARN: Code restructure failed: missing block: B:635:0x0d88, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L604;
     */
    /* JADX WARN: Code restructure failed: missing block: B:637:0x0d90, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L604;
     */
    /* JADX WARN: Code restructure failed: missing block: B:639:0x0db4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r7, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:641:0x0dc3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoUser", org.telegram.messenger.R.string.NotificationActionPinnedPhotoUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:643:0x0dc8, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L610;
     */
    /* JADX WARN: Code restructure failed: missing block: B:645:0x0dd6, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameUser", org.telegram.messenger.R.string.NotificationActionPinnedGameUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:646:0x0dd7, code lost:
        r3 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:647:0x0dd9, code lost:
        if (r3 == null) goto L620;
     */
    /* JADX WARN: Code restructure failed: missing block: B:649:0x0ddf, code lost:
        if (r3.length() <= 0) goto L620;
     */
    /* JADX WARN: Code restructure failed: missing block: B:650:0x0de1, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:651:0x0de7, code lost:
        if (r0.length() <= 20) goto L619;
     */
    /* JADX WARN: Code restructure failed: missing block: B:652:0x0de9, code lost:
        r3 = new java.lang.StringBuilder();
        r5 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:653:0x0dfe, code lost:
        r5 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:654:0x0dff, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextUser;
        r2 = new java.lang.Object[2];
        r2[r5] = r7;
        r2[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:655:0x0e0d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:657:0x0e1c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.R.string.NotificationActionPinnedNoTextUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:659:0x0e2b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoUser", org.telegram.messenger.R.string.NotificationActionPinnedGeoUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:660:0x0e2c, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:661:0x0e32, code lost:
        if (r0 == null) goto L628;
     */
    /* JADX WARN: Code restructure failed: missing block: B:663:0x0e43, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiUser", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiUser, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:665:0x0e50, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerUser", org.telegram.messenger.R.string.NotificationActionPinnedStickerUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:667:0x0e53, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) == false) goto L646;
     */
    /* JADX WARN: Code restructure failed: missing block: B:668:0x0e55, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) r2).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:669:0x0e5d, code lost:
        if (android.text.TextUtils.isEmpty(r0) == false) goto L640;
     */
    /* JADX WARN: Code restructure failed: missing block: B:671:0x0e61, code lost:
        if (r3 != r19) goto L638;
     */
    /* JADX WARN: Code restructure failed: missing block: B:675:0x0e85, code lost:
        if (r3 != r19) goto L644;
     */
    /* JADX WARN: Code restructure failed: missing block: B:678:0x0ea3, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.R.string.ChatThemeChangedTo, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:680:0x0ea6, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest) == false) goto L650;
     */
    /* JADX WARN: Code restructure failed: missing block: B:682:0x0eae, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:683:0x0eaf, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:686:0x0eb9, code lost:
        if (r1.peer_id.channel_id == 0) goto L661;
     */
    /* JADX WARN: Code restructure failed: missing block: B:688:0x0ebd, code lost:
        if (r8.megagroup != false) goto L661;
     */
    /* JADX WARN: Code restructure failed: missing block: B:690:0x0ec3, code lost:
        if (r26.isVideoAvatar() == false) goto L659;
     */
    /* JADX WARN: Code restructure failed: missing block: B:692:0x0ed5, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.R.string.ChannelVideoEditNotification, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:694:0x0ee6, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.R.string.ChannelPhotoEditNotification, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:696:0x0eec, code lost:
        if (r26.isVideoAvatar() == false) goto L665;
     */
    /* JADX WARN: Code restructure failed: missing block: B:698:0x0f00, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.R.string.NotificationEditedGroupVideo, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:700:0x0f13, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.R.string.NotificationEditedGroupPhoto, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:702:0x0f1a, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:704:0x0f29, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationContactJoined", org.telegram.messenger.R.string.NotificationContactJoined, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:706:0x0f2e, code lost:
        if (r26.isMediaEmpty() == false) goto L679;
     */
    /* JADX WARN: Code restructure failed: missing block: B:708:0x0f38, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageOwner.message) != false) goto L677;
     */
    /* JADX WARN: Code restructure failed: missing block: B:710:0x0f3e, code lost:
        return replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:712:0x0f47, code lost:
        return org.telegram.messenger.LocaleController.getString(r23, org.telegram.messenger.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:713:0x0f48, code lost:
        r1 = r23;
        r2 = r26.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:714:0x0f50, code lost:
        if ((r2.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L693;
     */
    /* JADX WARN: Code restructure failed: missing block: B:716:0x0f56, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L687;
     */
    /* JADX WARN: Code restructure failed: missing block: B:718:0x0f5e, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L687;
     */
    /* JADX WARN: Code restructure failed: missing block: B:720:0x0f73, code lost:
        return "🖼 " + replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:722:0x0f7a, code lost:
        if (r26.messageOwner.media.ttl_seconds == 0) goto L691;
     */
    /* JADX WARN: Code restructure failed: missing block: B:724:0x0f84, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDestructingPhoto", org.telegram.messenger.R.string.AttachDestructingPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x0f8d, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachPhoto", org.telegram.messenger.R.string.AttachPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:728:0x0f92, code lost:
        if (r26.isVideo() == false) goto L707;
     */
    /* JADX WARN: Code restructure failed: missing block: B:730:0x0f98, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L701;
     */
    /* JADX WARN: Code restructure failed: missing block: B:732:0x0fa2, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageOwner.message) != false) goto L701;
     */
    /* JADX WARN: Code restructure failed: missing block: B:734:0x0fb7, code lost:
        return "📹 " + replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:736:0x0fbe, code lost:
        if (r26.messageOwner.media.ttl_seconds == 0) goto L705;
     */
    /* JADX WARN: Code restructure failed: missing block: B:738:0x0fc8, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDestructingVideo", org.telegram.messenger.R.string.AttachDestructingVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:740:0x0fd1, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachVideo", org.telegram.messenger.R.string.AttachVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:742:0x0fd6, code lost:
        if (r26.isGame() == false) goto L711;
     */
    /* JADX WARN: Code restructure failed: missing block: B:744:0x0fe0, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachGame", org.telegram.messenger.R.string.AttachGame);
     */
    /* JADX WARN: Code restructure failed: missing block: B:746:0x0fe5, code lost:
        if (r26.isVoice() == false) goto L715;
     */
    /* JADX WARN: Code restructure failed: missing block: B:748:0x0fef, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachAudio", org.telegram.messenger.R.string.AttachAudio);
     */
    /* JADX WARN: Code restructure failed: missing block: B:750:0x0ff4, code lost:
        if (r26.isRoundVideo() == false) goto L719;
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x0ffe, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachRound", org.telegram.messenger.R.string.AttachRound);
     */
    /* JADX WARN: Code restructure failed: missing block: B:754:0x1003, code lost:
        if (r26.isMusic() == false) goto L723;
     */
    /* JADX WARN: Code restructure failed: missing block: B:756:0x100d, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachMusic", org.telegram.messenger.R.string.AttachMusic);
     */
    /* JADX WARN: Code restructure failed: missing block: B:757:0x100e, code lost:
        r2 = r26.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:758:0x1014, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L727;
     */
    /* JADX WARN: Code restructure failed: missing block: B:760:0x101e, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachContact", org.telegram.messenger.R.string.AttachContact);
     */
    /* JADX WARN: Code restructure failed: missing block: B:762:0x1021, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L735;
     */
    /* JADX WARN: Code restructure failed: missing block: B:764:0x1029, code lost:
        if (((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r2).poll.quiz == false) goto L733;
     */
    /* JADX WARN: Code restructure failed: missing block: B:766:0x1033, code lost:
        return org.telegram.messenger.LocaleController.getString("QuizPoll", org.telegram.messenger.R.string.QuizPoll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:768:0x103c, code lost:
        return org.telegram.messenger.LocaleController.getString("Poll", org.telegram.messenger.R.string.Poll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:770:0x103f, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L779;
     */
    /* JADX WARN: Code restructure failed: missing block: B:772:0x1043, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L739;
     */
    /* JADX WARN: Code restructure failed: missing block: B:775:0x1049, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L743;
     */
    /* JADX WARN: Code restructure failed: missing block: B:777:0x1053, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachLiveLocation", org.telegram.messenger.R.string.AttachLiveLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:779:0x1056, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L773;
     */
    /* JADX WARN: Code restructure failed: missing block: B:781:0x105c, code lost:
        if (r26.isSticker() != false) goto L767;
     */
    /* JADX WARN: Code restructure failed: missing block: B:783:0x1062, code lost:
        if (r26.isAnimatedSticker() == false) goto L749;
     */
    /* JADX WARN: Code restructure failed: missing block: B:786:0x1069, code lost:
        if (r26.isGif() == false) goto L759;
     */
    /* JADX WARN: Code restructure failed: missing block: B:788:0x106f, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L757;
     */
    /* JADX WARN: Code restructure failed: missing block: B:790:0x1079, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageOwner.message) != false) goto L757;
     */
    /* JADX WARN: Code restructure failed: missing block: B:792:0x108e, code lost:
        return "🎬 " + replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:794:0x1097, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachGif", org.telegram.messenger.R.string.AttachGif);
     */
    /* JADX WARN: Code restructure failed: missing block: B:796:0x109c, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L765;
     */
    /* JADX WARN: Code restructure failed: missing block: B:798:0x10a6, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageOwner.message) != false) goto L765;
     */
    /* JADX WARN: Code restructure failed: missing block: B:800:0x10bb, code lost:
        return "📎 " + replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:802:0x10c4, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDocument", org.telegram.messenger.R.string.AttachDocument);
     */
    /* JADX WARN: Code restructure failed: missing block: B:803:0x10c5, code lost:
        r0 = r26.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:804:0x10c9, code lost:
        if (r0 == null) goto L771;
     */
    /* JADX WARN: Code restructure failed: missing block: B:806:0x10e7, code lost:
        return r0 + " " + org.telegram.messenger.LocaleController.getString("AttachSticker", org.telegram.messenger.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:808:0x10f0, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachSticker", org.telegram.messenger.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:810:0x10f7, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageText) != false) goto L777;
     */
    /* JADX WARN: Code restructure failed: missing block: B:812:0x10fd, code lost:
        return replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:814:0x1104, code lost:
        return org.telegram.messenger.LocaleController.getString(r1, org.telegram.messenger.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:816:0x110d, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachLocation", org.telegram.messenger.R.string.AttachLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:830:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:831:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.R.string.ChatThemeDisabled, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:832:?, code lost:
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

    /* JADX WARN: Code restructure failed: missing block: B:268:0x0626, code lost:
        if (r12.getBoolean(r22, true) == false) goto L828;
     */
    /* JADX WARN: Code restructure failed: missing block: B:273:0x0632, code lost:
        if (r12.getBoolean("EnablePreviewChannel", r15) != false) goto L283;
     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x0634, code lost:
        r6 = r29.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:275:0x0638, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageService) == false) goto L619;
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x063a, code lost:
        r7 = r6.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:277:0x063e, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser) == false) goto L326;
     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x0640, code lost:
        r3 = r7.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x0646, code lost:
        if (r3 != 0) goto L292;
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x064f, code lost:
        if (r7.users.size() != 1) goto L292;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x0651, code lost:
        r3 = r29.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x0666, code lost:
        if (r3 == 0) goto L312;
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x0670, code lost:
        if (r29.messageOwner.peer_id.channel_id == 0) goto L299;
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x0674, code lost:
        if (r5.megagroup != false) goto L299;
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x0676, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.R.string.ChannelAddedByNotification, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:291:0x068d, code lost:
        if (r3 != r19) goto L302;
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x068f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.R.string.NotificationInvitedToGroup, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x06a4, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x06b0, code lost:
        if (r0 != null) goto L305;
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x06b2, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x06b8, code lost:
        if (r9 != r0.id) goto L311;
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x06bc, code lost:
        if (r5.megagroup == false) goto L310;
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x06be, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.R.string.NotificationGroupAddSelfMega, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x06d3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.R.string.NotificationGroupAddSelf, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x06e8, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r2, r5.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x0704, code lost:
        r1 = new java.lang.StringBuilder();
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x0714, code lost:
        if (r3 >= r29.messageOwner.action.users.size()) goto L324;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x0716, code lost:
        r4 = getMessagesController().getUser(r29.messageOwner.action.users.get(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x072a, code lost:
        if (r4 == null) goto L323;
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x072c, code lost:
        r4 = org.telegram.messenger.UserObject.getUserName(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x0734, code lost:
        if (r1.length() == 0) goto L320;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x0736, code lost:
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x073b, code lost:
        r1.append(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x073e, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x0741, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r2, r5.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x0760, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCall) == false) goto L330;
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x0778, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled) == false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x0784, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall) == false) goto L364;
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x0786, code lost:
        r3 = r7.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x078c, code lost:
        if (r3 != 0) goto L341;
     */
    /* JADX WARN: Code restructure failed: missing block: B:326:0x0795, code lost:
        if (r7.users.size() != 1) goto L341;
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x0797, code lost:
        r3 = r29.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x07ac, code lost:
        if (r3 == 0) goto L350;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x07b0, code lost:
        if (r3 != r19) goto L346;
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x07b2, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.R.string.NotificationGroupInvitedYouToCall, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x07c7, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x07d3, code lost:
        if (r0 != null) goto L349;
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x07d5, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x07d7, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r2, r5.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x07f3, code lost:
        r1 = new java.lang.StringBuilder();
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x0803, code lost:
        if (r3 >= r29.messageOwner.action.users.size()) goto L362;
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x0805, code lost:
        r4 = getMessagesController().getUser(r29.messageOwner.action.users.get(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x0819, code lost:
        if (r4 == null) goto L361;
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x081b, code lost:
        r4 = org.telegram.messenger.UserObject.getUserName(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:344:0x0823, code lost:
        if (r1.length() == 0) goto L358;
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x0825, code lost:
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x082a, code lost:
        r1.append(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:347:0x082d, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x0830, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r2, r5.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x084f, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink) == false) goto L368;
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x0868, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle) == false) goto L372;
     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x087f, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto) != false) goto L603;
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x0883, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto) == false) goto L376;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x008f, code lost:
        if (r12.getBoolean("EnablePreviewGroup", true) != false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x0889, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser) == false) goto L391;
     */
    /* JADX WARN: Code restructure failed: missing block: B:362:0x088b, code lost:
        r3 = r7.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x088f, code lost:
        if (r3 != r19) goto L382;
     */
    /* JADX WARN: Code restructure failed: missing block: B:366:0x08ab, code lost:
        if (r3 != r9) goto L386;
     */
    /* JADX WARN: Code restructure failed: missing block: B:368:0x08bf, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r29.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:369:0x08d1, code lost:
        if (r0 != null) goto L389;
     */
    /* JADX WARN: Code restructure failed: missing block: B:370:0x08d3, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x08f1, code lost:
        r9 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:374:0x08f4, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatCreate) == false) goto L395;
     */
    /* JADX WARN: Code restructure failed: missing block: B:377:0x0900, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate) == false) goto L399;
     */
    /* JADX WARN: Code restructure failed: missing block: B:380:0x090c, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo) == false) goto L403;
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x0924, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) == false) goto L407;
     */
    /* JADX WARN: Code restructure failed: missing block: B:386:0x0938, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken) == false) goto L411;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x0944, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPinMessage) == false) goto L583;
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x094c, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r5) == false) goto L500;
     */
    /* JADX WARN: Code restructure failed: missing block: B:393:0x0950, code lost:
        if (r5.megagroup == false) goto L417;
     */
    /* JADX WARN: Code restructure failed: missing block: B:395:0x0954, code lost:
        r2 = r29.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:396:0x0956, code lost:
        if (r2 != null) goto L421;
     */
    /* JADX WARN: Code restructure failed: missing block: B:399:0x0970, code lost:
        if (r2.isMusic() == false) goto L424;
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x0972, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.R.string.NotificationActionPinnedMusicChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:402:0x0988, code lost:
        if (r2.isVideo() == false) goto L432;
     */
    /* JADX WARN: Code restructure failed: missing block: B:404:0x098e, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L431;
     */
    /* JADX WARN: Code restructure failed: missing block: B:406:0x0998, code lost:
        if (android.text.TextUtils.isEmpty(r2.messageOwner.message) != false) goto L431;
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x099a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "📹 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:408:0x09c0, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.R.string.NotificationActionPinnedVideoChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:410:0x09d6, code lost:
        if (r2.isGif() == false) goto L440;
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x09dc, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L439;
     */
    /* JADX WARN: Code restructure failed: missing block: B:414:0x09e6, code lost:
        if (android.text.TextUtils.isEmpty(r2.messageOwner.message) != false) goto L439;
     */
    /* JADX WARN: Code restructure failed: missing block: B:415:0x09e8, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "🎬 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:416:0x0a0e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.R.string.NotificationActionPinnedGifChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:418:0x0a26, code lost:
        if (r2.isVoice() == false) goto L443;
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x0a28, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.R.string.NotificationActionPinnedVoiceChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x009d, code lost:
        if (r12.getBoolean("EnablePreviewChannel", r3) == false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x0a3c, code lost:
        if (r2.isRoundVideo() == false) goto L446;
     */
    /* JADX WARN: Code restructure failed: missing block: B:422:0x0a3e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.R.string.NotificationActionPinnedRoundChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:424:0x0a52, code lost:
        if (r2.isSticker() != false) goto L496;
     */
    /* JADX WARN: Code restructure failed: missing block: B:426:0x0a58, code lost:
        if (r2.isAnimatedSticker() == false) goto L450;
     */
    /* JADX WARN: Code restructure failed: missing block: B:428:0x0a5c, code lost:
        r1 = r2.messageOwner;
        r3 = r1.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x0a62, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L458;
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x0a68, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L457;
     */
    /* JADX WARN: Code restructure failed: missing block: B:433:0x0a70, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L457;
     */
    /* JADX WARN: Code restructure failed: missing block: B:434:0x0a72, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "📎 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x0a98, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.R.string.NotificationActionPinnedFileChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x0aac, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L495;
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x0ab0, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L462;
     */
    /* JADX WARN: Code restructure failed: missing block: B:442:0x0ab6, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L465;
     */
    /* JADX WARN: Code restructure failed: missing block: B:443:0x0ab8, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:445:0x0acc, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L468;
     */
    /* JADX WARN: Code restructure failed: missing block: B:446:0x0ace, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r29.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.R.string.NotificationActionPinnedContactChannel2, r5.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:448:0x0af3, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L474;
     */
    /* JADX WARN: Code restructure failed: missing block: B:449:0x0af5, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r3).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:450:0x0afb, code lost:
        if (r0.quiz == false) goto L473;
     */
    /* JADX WARN: Code restructure failed: missing block: B:451:0x0afd, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.R.string.NotificationActionPinnedQuizChannel2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:452:0x0b14, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.R.string.NotificationActionPinnedPollChannel2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:454:0x0b2d, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L482;
     */
    /* JADX WARN: Code restructure failed: missing block: B:456:0x0b33, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L481;
     */
    /* JADX WARN: Code restructure failed: missing block: B:458:0x0b3b, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L481;
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x0b3d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "🖼 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:460:0x0b63, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.R.string.NotificationActionPinnedPhotoChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:462:0x0b79, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L485;
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0b7b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.R.string.NotificationActionPinnedGameChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:464:0x0b8b, code lost:
        r0 = r2.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x0b8d, code lost:
        if (r0 == null) goto L494;
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x0b93, code lost:
        if (r0.length() <= 0) goto L494;
     */
    /* JADX WARN: Code restructure failed: missing block: B:468:0x0b95, code lost:
        r0 = r2.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x0b9b, code lost:
        if (r0.length() <= 20) goto L493;
     */
    /* JADX WARN: Code restructure failed: missing block: B:470:0x0b9d, code lost:
        r1 = new java.lang.StringBuilder();
        r3 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x0bb4, code lost:
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:472:0x0bb5, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextChannel;
        r2 = new java.lang.Object[2];
        r2[r3] = r5.title;
        r2[1] = r0;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x0bc7, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:474:0x0bd9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:475:0x0beb, code lost:
        r0 = r2.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:476:0x0bf0, code lost:
        if (r0 == null) goto L499;
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x0bf2, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiChannel, r5.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:478:0x0c06, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x0c17, code lost:
        r6 = r29.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:480:0x0c1a, code lost:
        if (r6 != null) goto L504;
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0c36, code lost:
        if (r6.isMusic() == false) goto L507;
     */
    /* JADX WARN: Code restructure failed: missing block: B:484:0x0c38, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.R.string.NotificationActionPinnedMusic, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:486:0x0c50, code lost:
        if (r6.isVideo() == false) goto L515;
     */
    /* JADX WARN: Code restructure failed: missing block: B:488:0x0c56, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L514;
     */
    /* JADX WARN: Code restructure failed: missing block: B:490:0x0c60, code lost:
        if (android.text.TextUtils.isEmpty(r6.messageOwner.message) != false) goto L514;
     */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x0c62, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "📹 " + r6.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x0c8b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.R.string.NotificationActionPinnedVideo, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:494:0x0ca4, code lost:
        if (r6.isGif() == false) goto L523;
     */
    /* JADX WARN: Code restructure failed: missing block: B:496:0x0caa, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L522;
     */
    /* JADX WARN: Code restructure failed: missing block: B:498:0x0cb4, code lost:
        if (android.text.TextUtils.isEmpty(r6.messageOwner.message) != false) goto L522;
     */
    /* JADX WARN: Code restructure failed: missing block: B:499:0x0cb6, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "🎬 " + r6.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:500:0x0cdf, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.R.string.NotificationActionPinnedGif, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:502:0x0cfb, code lost:
        if (r6.isVoice() == false) goto L526;
     */
    /* JADX WARN: Code restructure failed: missing block: B:503:0x0cfd, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.R.string.NotificationActionPinnedVoice, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:505:0x0d13, code lost:
        if (r6.isRoundVideo() == false) goto L529;
     */
    /* JADX WARN: Code restructure failed: missing block: B:506:0x0d15, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.R.string.NotificationActionPinnedRound, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x0d2b, code lost:
        if (r6.isSticker() != false) goto L579;
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x0d31, code lost:
        if (r6.isAnimatedSticker() == false) goto L533;
     */
    /* JADX WARN: Code restructure failed: missing block: B:512:0x0d35, code lost:
        r1 = r6.messageOwner;
        r3 = r1.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x0d3b, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L541;
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x0d41, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L540;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0d49, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L540;
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x0d4b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "📎 " + r6.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:519:0x0d74, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.R.string.NotificationActionPinnedFile, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x0d8b, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L578;
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x0d8f, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L545;
     */
    /* JADX WARN: Code restructure failed: missing block: B:526:0x0d95, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L548;
     */
    /* JADX WARN: Code restructure failed: missing block: B:527:0x0d97, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.R.string.NotificationActionPinnedGeoLive, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x0dae, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L551;
     */
    /* JADX WARN: Code restructure failed: missing block: B:530:0x0db0, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r29.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.R.string.NotificationActionPinnedContact2, r2, r5.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:532:0x0dd8, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L557;
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0dda, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r3).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:534:0x0de0, code lost:
        if (r0.quiz == false) goto L556;
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0de2, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.R.string.NotificationActionPinnedQuiz2, r2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:536:0x0dfc, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.R.string.NotificationActionPinnedPoll2, r2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:538:0x0e18, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L565;
     */
    /* JADX WARN: Code restructure failed: missing block: B:540:0x0e1e, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L564;
     */
    /* JADX WARN: Code restructure failed: missing block: B:542:0x0e26, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L564;
     */
    /* JADX WARN: Code restructure failed: missing block: B:543:0x0e28, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "🖼 " + r6.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:544:0x0e51, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.R.string.NotificationActionPinnedPhoto, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:546:0x0e6b, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L568;
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0e6d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.R.string.NotificationActionPinnedGame, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:548:0x0e7f, code lost:
        r0 = r6.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0e81, code lost:
        if (r0 == null) goto L577;
     */
    /* JADX WARN: Code restructure failed: missing block: B:551:0x0e87, code lost:
        if (r0.length() <= 0) goto L577;
     */
    /* JADX WARN: Code restructure failed: missing block: B:552:0x0e89, code lost:
        r0 = r6.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:553:0x0e8f, code lost:
        if (r0.length() <= 20) goto L576;
     */
    /* JADX WARN: Code restructure failed: missing block: B:554:0x0e91, code lost:
        r1 = new java.lang.StringBuilder();
        r3 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:555:0x0ea8, code lost:
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:556:0x0ea9, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedText;
        r4 = new java.lang.Object[3];
        r4[r3] = r2;
        r4[1] = r0;
        r4[2] = r5.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", r1, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:557:0x0ebe, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:558:0x0ed3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.R.string.NotificationActionPinnedGeo, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:559:0x0ee8, code lost:
        r0 = r6.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:560:0x0eee, code lost:
        if (r0 == null) goto L582;
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0ef0, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmoji, r2, r5.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:562:0x0f06, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.R.string.NotificationActionPinnedSticker, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:564:0x0f1b, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGameScore) == false) goto L587;
     */
    /* JADX WARN: Code restructure failed: missing block: B:567:0x0f27, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) == false) goto L599;
     */
    /* JADX WARN: Code restructure failed: missing block: B:568:0x0f29, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) r7).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x0f31, code lost:
        if (android.text.TextUtils.isEmpty(r0) == false) goto L595;
     */
    /* JADX WARN: Code restructure failed: missing block: B:571:0x0f35, code lost:
        if (r3 != r19) goto L594;
     */
    /* JADX WARN: Code restructure failed: missing block: B:572:0x0f37, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:573:0x0f44, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.R.string.ChatThemeDisabled, r2, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:575:0x0f5b, code lost:
        if (r3 != r19) goto L598;
     */
    /* JADX WARN: Code restructure failed: missing block: B:576:0x0f5d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeYou", org.telegram.messenger.R.string.ChatThemeChangedYou, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:577:0x0f6b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.R.string.ChatThemeChangedTo, r2, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:579:0x0f7e, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest) == false) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:582:0x0f90, code lost:
        if (r6.peer_id.channel_id == 0) goto L613;
     */
    /* JADX WARN: Code restructure failed: missing block: B:584:0x0f94, code lost:
        if (r5.megagroup != false) goto L613;
     */
    /* JADX WARN: Code restructure failed: missing block: B:586:0x0f9a, code lost:
        if (r29.isVideoAvatar() == false) goto L611;
     */
    /* JADX WARN: Code restructure failed: missing block: B:590:0x0fc5, code lost:
        if (r29.isVideoAvatar() == false) goto L617;
     */
    /* JADX WARN: Code restructure failed: missing block: B:594:0x0ff3, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r5) == false) goto L724;
     */
    /* JADX WARN: Code restructure failed: missing block: B:596:0x0ff7, code lost:
        if (r5.megagroup != false) goto L724;
     */
    /* JADX WARN: Code restructure failed: missing block: B:598:0x0ffd, code lost:
        if (r29.isMediaEmpty() == false) goto L632;
     */
    /* JADX WARN: Code restructure failed: missing block: B:599:0x0fff, code lost:
        if (r30 != false) goto L630;
     */
    /* JADX WARN: Code restructure failed: missing block: B:601:0x1009, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L630;
     */
    /* JADX WARN: Code restructure failed: missing block: B:602:0x100b, code lost:
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r2, r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:604:0x1032, code lost:
        r4 = r29.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:605:0x103a, code lost:
        if ((r4.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L643;
     */
    /* JADX WARN: Code restructure failed: missing block: B:606:0x103c, code lost:
        if (r30 != false) goto L641;
     */
    /* JADX WARN: Code restructure failed: missing block: B:608:0x1042, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L641;
     */
    /* JADX WARN: Code restructure failed: missing block: B:610:0x104a, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L641;
     */
    /* JADX WARN: Code restructure failed: missing block: B:611:0x104c, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r2, "🖼 " + r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:614:0x1086, code lost:
        if (r29.isVideo() == false) goto L654;
     */
    /* JADX WARN: Code restructure failed: missing block: B:615:0x1088, code lost:
        if (r30 != false) goto L652;
     */
    /* JADX WARN: Code restructure failed: missing block: B:617:0x108e, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L652;
     */
    /* JADX WARN: Code restructure failed: missing block: B:619:0x1098, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L652;
     */
    /* JADX WARN: Code restructure failed: missing block: B:620:0x109a, code lost:
        r3 = org.telegram.messenger.R.string.NotificationMessageText;
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r3, r2, "📹 " + r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:623:0x10d6, code lost:
        if (r29.isVoice() == false) goto L658;
     */
    /* JADX WARN: Code restructure failed: missing block: B:626:0x10ea, code lost:
        if (r29.isRoundVideo() == false) goto L662;
     */
    /* JADX WARN: Code restructure failed: missing block: B:629:0x10fe, code lost:
        if (r29.isMusic() == false) goto L666;
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:0x110e, code lost:
        r1 = r29.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:632:0x1114, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L670;
     */
    /* JADX WARN: Code restructure failed: missing block: B:633:0x1116, code lost:
        r1 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:635:0x1134, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L676;
     */
    /* JADX WARN: Code restructure failed: missing block: B:636:0x1136, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r1).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:637:0x113c, code lost:
        if (r0.quiz == false) goto L675;
     */
    /* JADX WARN: Code restructure failed: missing block: B:638:0x113e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageQuiz2", org.telegram.messenger.R.string.ChannelMessageQuiz2, r2, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:639:0x1153, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessagePoll2", org.telegram.messenger.R.string.ChannelMessagePoll2, r2, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:641:0x116a, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L722;
     */
    /* JADX WARN: Code restructure failed: missing block: B:643:0x116e, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L680;
     */
    /* JADX WARN: Code restructure failed: missing block: B:646:0x1174, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L684;
     */
    /* JADX WARN: Code restructure failed: missing block: B:649:0x1188, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L714;
     */
    /* JADX WARN: Code restructure failed: missing block: B:651:0x118e, code lost:
        if (r29.isSticker() != false) goto L710;
     */
    /* JADX WARN: Code restructure failed: missing block: B:653:0x1194, code lost:
        if (r29.isAnimatedSticker() == false) goto L690;
     */
    /* JADX WARN: Code restructure failed: missing block: B:656:0x119c, code lost:
        if (r29.isGif() == false) goto L701;
     */
    /* JADX WARN: Code restructure failed: missing block: B:657:0x119e, code lost:
        if (r30 != false) goto L699;
     */
    /* JADX WARN: Code restructure failed: missing block: B:659:0x11a4, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L699;
     */
    /* JADX WARN: Code restructure failed: missing block: B:661:0x11ae, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L699;
     */
    /* JADX WARN: Code restructure failed: missing block: B:662:0x11b0, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r2, "🎬 " + r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:664:0x11e6, code lost:
        if (r30 != false) goto L708;
     */
    /* JADX WARN: Code restructure failed: missing block: B:666:0x11ec, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L708;
     */
    /* JADX WARN: Code restructure failed: missing block: B:668:0x11f6, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L708;
     */
    /* JADX WARN: Code restructure failed: missing block: B:669:0x11f8, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r2, "📎 " + r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:671:0x122e, code lost:
        r0 = r29.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:672:0x1234, code lost:
        if (r0 == null) goto L713;
     */
    /* JADX WARN: Code restructure failed: missing block: B:673:0x1236, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageStickerEmoji", org.telegram.messenger.R.string.ChannelMessageStickerEmoji, r2, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:674:0x1247, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageSticker", org.telegram.messenger.R.string.ChannelMessageSticker, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:676:0x1256, code lost:
        if (r30 != false) goto L720;
     */
    /* JADX WARN: Code restructure failed: missing block: B:678:0x125e, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageText) != false) goto L720;
     */
    /* JADX WARN: Code restructure failed: missing block: B:679:0x1260, code lost:
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r2, r29.messageText);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:683:0x1297, code lost:
        if (r29.isMediaEmpty() == false) goto L733;
     */
    /* JADX WARN: Code restructure failed: missing block: B:684:0x1299, code lost:
        if (r30 != false) goto L731;
     */
    /* JADX WARN: Code restructure failed: missing block: B:686:0x12a3, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L731;
     */
    /* JADX WARN: Code restructure failed: missing block: B:689:0x12d4, code lost:
        r3 = r29.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:690:0x12dc, code lost:
        if ((r3.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L744;
     */
    /* JADX WARN: Code restructure failed: missing block: B:691:0x12de, code lost:
        if (r30 != false) goto L742;
     */
    /* JADX WARN: Code restructure failed: missing block: B:693:0x12e4, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L742;
     */
    /* JADX WARN: Code restructure failed: missing block: B:695:0x12ec, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L742;
     */
    /* JADX WARN: Code restructure failed: missing block: B:696:0x12ee, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:699:0x1330, code lost:
        if (r29.isVideo() == false) goto L755;
     */
    /* JADX WARN: Code restructure failed: missing block: B:700:0x1332, code lost:
        if (r30 != false) goto L753;
     */
    /* JADX WARN: Code restructure failed: missing block: B:702:0x1338, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L753;
     */
    /* JADX WARN: Code restructure failed: missing block: B:704:0x1342, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L753;
     */
    /* JADX WARN: Code restructure failed: missing block: B:705:0x1344, code lost:
        r3 = org.telegram.messenger.R.string.NotificationMessageGroupText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:708:0x1389, code lost:
        if (r29.isVoice() == false) goto L759;
     */
    /* JADX WARN: Code restructure failed: missing block: B:711:0x13a1, code lost:
        if (r29.isRoundVideo() == false) goto L763;
     */
    /* JADX WARN: Code restructure failed: missing block: B:714:0x13b9, code lost:
        if (r29.isMusic() == false) goto L767;
     */
    /* JADX WARN: Code restructure failed: missing block: B:716:0x13cd, code lost:
        r1 = r29.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:717:0x13d3, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L771;
     */
    /* JADX WARN: Code restructure failed: missing block: B:718:0x13d5, code lost:
        r1 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:720:0x13f9, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L777;
     */
    /* JADX WARN: Code restructure failed: missing block: B:721:0x13fb, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r1).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:722:0x1401, code lost:
        if (r0.quiz == false) goto L776;
     */
    /* JADX WARN: Code restructure failed: missing block: B:723:0x1403, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupQuiz2", org.telegram.messenger.R.string.NotificationMessageGroupQuiz2, r2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:724:0x141d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPoll2", org.telegram.messenger.R.string.NotificationMessageGroupPoll2, r2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x1439, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L781;
     */
    /* JADX WARN: Code restructure failed: missing block: B:729:0x1459, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L826;
     */
    /* JADX WARN: Code restructure failed: missing block: B:731:0x145d, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L785;
     */
    /* JADX WARN: Code restructure failed: missing block: B:734:0x1463, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L789;
     */
    /* JADX WARN: Code restructure failed: missing block: B:737:0x147c, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L819;
     */
    /* JADX WARN: Code restructure failed: missing block: B:739:0x1482, code lost:
        if (r29.isSticker() != false) goto L815;
     */
    /* JADX WARN: Code restructure failed: missing block: B:741:0x1488, code lost:
        if (r29.isAnimatedSticker() == false) goto L795;
     */
    /* JADX WARN: Code restructure failed: missing block: B:744:0x1490, code lost:
        if (r29.isGif() == false) goto L806;
     */
    /* JADX WARN: Code restructure failed: missing block: B:745:0x1492, code lost:
        if (r30 != false) goto L804;
     */
    /* JADX WARN: Code restructure failed: missing block: B:747:0x1498, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L804;
     */
    /* JADX WARN: Code restructure failed: missing block: B:749:0x14a2, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L804;
     */
    /* JADX WARN: Code restructure failed: missing block: B:750:0x14a4, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x14e2, code lost:
        if (r30 != false) goto L813;
     */
    /* JADX WARN: Code restructure failed: missing block: B:754:0x14e8, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L813;
     */
    /* JADX WARN: Code restructure failed: missing block: B:756:0x14f2, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L813;
     */
    /* JADX WARN: Code restructure failed: missing block: B:757:0x14f4, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:759:0x1532, code lost:
        r0 = r29.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:760:0x1538, code lost:
        if (r0 == null) goto L818;
     */
    /* JADX WARN: Code restructure failed: missing block: B:761:0x153a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupStickerEmoji", org.telegram.messenger.R.string.NotificationMessageGroupStickerEmoji, r2, r5.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:762:0x1550, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupSticker", org.telegram.messenger.R.string.NotificationMessageGroupSticker, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:763:0x1563, code lost:
        if (r30 != false) goto L824;
     */
    /* JADX WARN: Code restructure failed: missing block: B:765:0x156b, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageText) != false) goto L824;
     */
    /* JADX WARN: Code restructure failed: missing block: B:824:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.R.string.NotificationGroupCreatedCall, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:825:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:826:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.R.string.NotificationInvitedToGroupByLink, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:827:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.R.string.NotificationEditedGroupName, r2, r7.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:828:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.R.string.NotificationGroupKickYou, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:829:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.R.string.NotificationGroupLeftMember, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:830:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.R.string.NotificationGroupKickMember, r2, r5.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:831:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:832:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:833:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:834:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r7.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:835:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:836:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:837:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:838:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:839:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:840:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.R.string.ChannelVideoEditNotification, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:841:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.R.string.ChannelPhotoEditNotification, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:842:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.R.string.NotificationEditedGroupVideo, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:843:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.R.string.NotificationEditedGroupPhoto, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:844:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:845:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.R.string.ChannelMessageNoText, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:846:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:847:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessagePhoto", org.telegram.messenger.R.string.ChannelMessagePhoto, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:848:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:849:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageVideo", org.telegram.messenger.R.string.ChannelMessageVideo, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:850:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageAudio", org.telegram.messenger.R.string.ChannelMessageAudio, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:851:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageRound", org.telegram.messenger.R.string.ChannelMessageRound, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:852:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageMusic", org.telegram.messenger.R.string.ChannelMessageMusic, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:853:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageContact2", org.telegram.messenger.R.string.ChannelMessageContact2, r2, org.telegram.messenger.ContactsController.formatName(r1.first_name, r1.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:854:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageLiveLocation", org.telegram.messenger.R.string.ChannelMessageLiveLocation, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:855:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:856:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageGIF", org.telegram.messenger.R.string.ChannelMessageGIF, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:857:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:858:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageDocument", org.telegram.messenger.R.string.ChannelMessageDocument, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:859:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:860:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.R.string.ChannelMessageNoText, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:861:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageMap", org.telegram.messenger.R.string.ChannelMessageMap, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:862:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r2, r5.title, r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:863:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupNoText", org.telegram.messenger.R.string.NotificationMessageGroupNoText, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:864:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r2, r5.title, "🖼 " + r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:865:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPhoto", org.telegram.messenger.R.string.NotificationMessageGroupPhoto, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:866:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r3, r2, r5.title, "📹 " + r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:867:?, code lost:
        return org.telegram.messenger.LocaleController.formatString(" ", org.telegram.messenger.R.string.NotificationMessageGroupVideo, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:868:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupAudio", org.telegram.messenger.R.string.NotificationMessageGroupAudio, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:869:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupRound", org.telegram.messenger.R.string.NotificationMessageGroupRound, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:870:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMusic", org.telegram.messenger.R.string.NotificationMessageGroupMusic, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:871:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupContact2", org.telegram.messenger.R.string.NotificationMessageGroupContact2, r2, r5.title, org.telegram.messenger.ContactsController.formatName(r1.first_name, r1.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:872:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGame", org.telegram.messenger.R.string.NotificationMessageGroupGame, r2, r5.title, r1.game.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:873:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupLiveLocation", org.telegram.messenger.R.string.NotificationMessageGroupLiveLocation, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:874:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r2, r5.title, "🎬 " + r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:875:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGif", org.telegram.messenger.R.string.NotificationMessageGroupGif, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:876:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r2, r5.title, "📎 " + r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:877:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupDocument", org.telegram.messenger.R.string.NotificationMessageGroupDocument, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:878:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r2, r5.title, r29.messageText);
     */
    /* JADX WARN: Code restructure failed: missing block: B:879:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupNoText", org.telegram.messenger.R.string.NotificationMessageGroupNoText, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:880:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMap", org.telegram.messenger.R.string.NotificationMessageGroupMap, r2, r5.title);
     */
    /* JADX WARN: Removed duplicated region for block: B:266:0x061d  */
    /* JADX WARN: Removed duplicated region for block: B:771:0x15b1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getStringForMessage(MessageObject messageObject, boolean z, boolean[] zArr, boolean[] zArr2) {
        String str;
        String str2;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$Chat tLRPC$Chat2;
        boolean z2;
        String formatString;
        String str3;
        char c;
        boolean z3;
        String formatString2;
        boolean z4;
        if (AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter) {
            return LocaleController.getString("YouHaveNewMessage", R.string.YouHaveNewMessage);
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
        boolean z5 = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CONTENT_PREVIEW + j, true);
        if (messageObject.isFcmMessage()) {
            if (j2 != 0 || j3 == 0) {
                if (j2 != 0) {
                    if (z5) {
                        z4 = !messageObject.localChannel ? true : true;
                        if (messageObject.localChannel) {
                        }
                        zArr[0] = z4;
                        return (String) messageObject.messageText;
                    }
                    if (zArr2 != null) {
                        zArr2[0] = false;
                    }
                    return (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup()) ? LocaleController.formatString("NotificationMessageGroupNoText", R.string.NotificationMessageGroupNoText, messageObject.localUserName, messageObject.localName) : LocaleController.formatString("ChannelMessageNoText", R.string.ChannelMessageNoText, messageObject.localName);
                }
            } else if (!z5 || !notificationsSettings.getBoolean("EnablePreviewAll", true)) {
                if (zArr2 != null) {
                    zArr2[0] = false;
                }
                return LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, messageObject.localName);
            }
            z4 = true;
            zArr[0] = z4;
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
                str2 = user != null ? UserObject.getUserName(user) : null;
            } else if (j == clientUserId) {
                str2 = LocaleController.getString("MessageScheduledReminderNotification", R.string.MessageScheduledReminderNotification);
            } else {
                str2 = LocaleController.getString("NotificationMessageScheduledName", R.string.NotificationMessageScheduledName);
            }
            str = "EnablePreviewGroup";
        } else {
            str = "EnablePreviewGroup";
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j3));
            str2 = chat != null ? chat.title : null;
        }
        if (str2 == null) {
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
            return LocaleController.getString("YouHaveNewMessage", R.string.YouHaveNewMessage);
        }
        TLRPC$Chat tLRPC$Chat3 = tLRPC$Chat;
        if (j2 != 0 || j3 == 0) {
            if (j2 != 0) {
                if (ChatObject.isChannel(tLRPC$Chat3)) {
                    tLRPC$Chat2 = tLRPC$Chat3;
                    if (!tLRPC$Chat2.megagroup) {
                        z2 = true;
                        if (z5) {
                            boolean z6 = !z2 ? true : true;
                            if (z2) {
                            }
                        }
                        if (zArr2 != null) {
                            zArr2[0] = false;
                        }
                        return (ChatObject.isChannel(tLRPC$Chat2) || tLRPC$Chat2.megagroup) ? LocaleController.formatString("NotificationMessageGroupNoText", R.string.NotificationMessageGroupNoText, str2, tLRPC$Chat2.title) : LocaleController.formatString("ChannelMessageNoText", R.string.ChannelMessageNoText, str2);
                    }
                } else {
                    tLRPC$Chat2 = tLRPC$Chat3;
                }
                z2 = false;
                if (z5) {
                }
                if (zArr2 != null) {
                }
                if (ChatObject.isChannel(tLRPC$Chat2)) {
                }
            }
            str3 = null;
        } else if (!z5 || !notificationsSettings.getBoolean("EnablePreviewAll", true)) {
            if (zArr2 != null) {
                zArr2[0] = false;
            }
            return LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str2);
        } else {
            TLRPC$Message tLRPC$Message2 = messageObject.messageOwner;
            if (tLRPC$Message2 instanceof TLRPC$TL_messageService) {
                TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message2.action;
                if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetSameChatWallPaper) {
                    return LocaleController.getString("WallpaperSameNotification", R.string.WallpaperSameNotification);
                }
                if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatWallPaper) {
                    return LocaleController.getString("WallpaperNotification", R.string.WallpaperNotification);
                }
                if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGeoProximityReached) {
                    return messageObject.messageText.toString();
                }
                if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserJoined) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp)) {
                    return LocaleController.formatString("NotificationContactJoined", R.string.NotificationContactJoined, str2);
                }
                if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserUpdatedPhoto) {
                    return LocaleController.formatString("NotificationContactNewPhoto", R.string.NotificationContactNewPhoto, str2);
                }
                if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionLoginUnknownLocation) {
                    String formatString3 = LocaleController.formatString("formatDateAtTime", R.string.formatDateAtTime, LocaleController.getInstance().formatterYear.format(messageObject.messageOwner.date * 1000), LocaleController.getInstance().formatterDay.format(messageObject.messageOwner.date * 1000));
                    int i = R.string.NotificationUnrecognizedDevice;
                    TLRPC$MessageAction tLRPC$MessageAction2 = messageObject.messageOwner.action;
                    return LocaleController.formatString("NotificationUnrecognizedDevice", i, getUserConfig().getCurrentUser().first_name, formatString3, tLRPC$MessageAction2.title, tLRPC$MessageAction2.address);
                } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionGameScore) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPaymentSent)) {
                    return messageObject.messageText.toString();
                } else {
                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPhoneCall) {
                        if (tLRPC$MessageAction.video) {
                            return LocaleController.getString("CallMessageVideoIncomingMissed", R.string.CallMessageVideoIncomingMissed);
                        }
                        return LocaleController.getString("CallMessageIncomingMissed", R.string.CallMessageIncomingMissed);
                    }
                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatTheme) {
                        String str4 = ((TLRPC$TL_messageActionSetChatTheme) tLRPC$MessageAction).emoticon;
                        if (!TextUtils.isEmpty(str4)) {
                            c = 0;
                            z3 = true;
                            if (j == clientUserId) {
                                formatString2 = LocaleController.formatString("ChangedChatThemeYou", R.string.ChatThemeChangedYou, str4);
                            } else {
                                formatString2 = LocaleController.formatString("ChangedChatThemeTo", R.string.ChatThemeChangedTo, str2, str4);
                            }
                        } else if (j == clientUserId) {
                            c = 0;
                            formatString2 = LocaleController.formatString("ChatThemeDisabledYou", R.string.ChatThemeDisabledYou, new Object[0]);
                            z3 = true;
                        } else {
                            c = 0;
                            z3 = true;
                            formatString2 = LocaleController.formatString("ChatThemeDisabled", R.string.ChatThemeDisabled, str2, str4);
                        }
                        String str5 = formatString2;
                        zArr[c] = z3;
                        return str5;
                    }
                    str3 = null;
                }
            } else if (messageObject.isMediaEmpty()) {
                if (!z && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                    String formatString4 = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, str2, messageObject.messageOwner.message);
                    zArr[0] = true;
                    return formatString4;
                }
                return LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str2);
            } else {
                TLRPC$Message tLRPC$Message3 = messageObject.messageOwner;
                if (tLRPC$Message3.media instanceof TLRPC$TL_messageMediaPhoto) {
                    if (z || Build.VERSION.SDK_INT < 19 || TextUtils.isEmpty(tLRPC$Message3.message)) {
                        return messageObject.messageOwner.media.ttl_seconds != 0 ? LocaleController.formatString("NotificationMessageSDPhoto", R.string.NotificationMessageSDPhoto, str2) : LocaleController.formatString("NotificationMessagePhoto", R.string.NotificationMessagePhoto, str2);
                    }
                    int i2 = R.string.NotificationMessageText;
                    String formatString5 = LocaleController.formatString("NotificationMessageText", i2, str2, "🖼 " + messageObject.messageOwner.message);
                    zArr[0] = true;
                    return formatString5;
                } else if (messageObject.isVideo()) {
                    if (z || Build.VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        return messageObject.messageOwner.media.ttl_seconds != 0 ? LocaleController.formatString("NotificationMessageSDVideo", R.string.NotificationMessageSDVideo, str2) : LocaleController.formatString("NotificationMessageVideo", R.string.NotificationMessageVideo, str2);
                    }
                    int i3 = R.string.NotificationMessageText;
                    String formatString6 = LocaleController.formatString("NotificationMessageText", i3, str2, "📹 " + messageObject.messageOwner.message);
                    zArr[0] = true;
                    return formatString6;
                } else if (messageObject.isGame()) {
                    return LocaleController.formatString("NotificationMessageGame", R.string.NotificationMessageGame, str2, messageObject.messageOwner.media.game.title);
                } else {
                    if (messageObject.isVoice()) {
                        return LocaleController.formatString("NotificationMessageAudio", R.string.NotificationMessageAudio, str2);
                    }
                    if (messageObject.isRoundVideo()) {
                        return LocaleController.formatString("NotificationMessageRound", R.string.NotificationMessageRound, str2);
                    }
                    if (messageObject.isMusic()) {
                        return LocaleController.formatString("NotificationMessageMusic", R.string.NotificationMessageMusic, str2);
                    }
                    TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
                    if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaContact) {
                        TLRPC$TL_messageMediaContact tLRPC$TL_messageMediaContact = (TLRPC$TL_messageMediaContact) tLRPC$MessageMedia;
                        return LocaleController.formatString("NotificationMessageContact2", R.string.NotificationMessageContact2, str2, ContactsController.formatName(tLRPC$TL_messageMediaContact.first_name, tLRPC$TL_messageMediaContact.last_name));
                    }
                    if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                        TLRPC$Poll tLRPC$Poll = ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia).poll;
                        if (tLRPC$Poll.quiz) {
                            formatString = LocaleController.formatString("NotificationMessageQuiz2", R.string.NotificationMessageQuiz2, str2, tLRPC$Poll.question);
                        } else {
                            formatString = LocaleController.formatString("NotificationMessagePoll2", R.string.NotificationMessagePoll2, str2, tLRPC$Poll.question);
                        }
                    } else if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeo) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaVenue)) {
                        return LocaleController.formatString("NotificationMessageMap", R.string.NotificationMessageMap, str2);
                    } else {
                        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeoLive) {
                            return LocaleController.formatString("NotificationMessageLiveLocation", R.string.NotificationMessageLiveLocation, str2);
                        }
                        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                            if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {
                                String stickerEmoji = messageObject.getStickerEmoji();
                                if (stickerEmoji != null) {
                                    formatString = LocaleController.formatString("NotificationMessageStickerEmoji", R.string.NotificationMessageStickerEmoji, str2, stickerEmoji);
                                } else {
                                    formatString = LocaleController.formatString("NotificationMessageSticker", R.string.NotificationMessageSticker, str2);
                                }
                            } else if (messageObject.isGif()) {
                                if (z || Build.VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                    return LocaleController.formatString("NotificationMessageGif", R.string.NotificationMessageGif, str2);
                                }
                                int i4 = R.string.NotificationMessageText;
                                String formatString7 = LocaleController.formatString("NotificationMessageText", i4, str2, "🎬 " + messageObject.messageOwner.message);
                                zArr[0] = true;
                                return formatString7;
                            } else if (z || Build.VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                return LocaleController.formatString("NotificationMessageDocument", R.string.NotificationMessageDocument, str2);
                            } else {
                                int i5 = R.string.NotificationMessageText;
                                String formatString8 = LocaleController.formatString("NotificationMessageText", i5, str2, "📎 " + messageObject.messageOwner.message);
                                zArr[0] = true;
                                return formatString8;
                            }
                        } else if (z || TextUtils.isEmpty(messageObject.messageText)) {
                            return LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str2);
                        } else {
                            String formatString9 = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, str2, messageObject.messageText);
                            zArr[0] = true;
                            return formatString9;
                        }
                    }
                    return formatString;
                }
            }
        }
        return str3;
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
        return tLRPC$Peer != null && tLRPC$Peer.chat_id == 0 && tLRPC$Peer.channel_id == 0 && ((tLRPC$MessageAction = tLRPC$Message.action) == null || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionEmpty));
    }

    private int getNotifyOverride(SharedPreferences sharedPreferences, long j, int i) {
        int property = this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_NOTIFY, j, i, -1);
        if (property != 3 || this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL, j, i, 0) < getConnectionsManager().getCurrentTime()) {
            return property;
        }
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showNotifications$25() {
        showOrUpdateNotification(false);
    }

    public void showNotifications() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$showNotifications$25();
            }
        });
    }

    public void hideNotifications() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$hideNotifications$26();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideNotifications$26() {
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
            AndroidUtilities.runOnUIThread(NotificationsController$$ExternalSyntheticLambda40.INSTANCE);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dismissNotification$27() {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
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
            notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$playInChatSound$29();
                }
            });
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playInChatSound$29() {
        if (Math.abs(SystemClock.elapsedRealtime() - this.lastSoundPlay) <= 500) {
            return;
        }
        try {
            if (this.soundPool == null) {
                SoundPool soundPool = new SoundPool(3, 1, 0);
                this.soundPool = soundPool;
                soundPool.setOnLoadCompleteListener(NotificationsController$$ExternalSyntheticLambda3.INSTANCE);
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
    public static /* synthetic */ void lambda$playInChatSound$28(SoundPool soundPool, int i, int i2) {
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
            notificationsQueue.cancelRunnable(this.notificationDelayRunnable);
            notificationsQueue.postRunnable(this.notificationDelayRunnable, z ? 3000 : 1000);
        } catch (Exception e) {
            FileLog.e(e);
            showOrUpdateNotification(this.notifyCheck);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void repeatNotificationMaybe() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$repeatNotificationMaybe$30();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$repeatNotificationMaybe$30() {
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

    public void deleteNotificationChannel(long j, int i) {
        deleteNotificationChannel(j, i, -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: deleteNotificationChannelInternal */
    public void lambda$deleteNotificationChannel$31(long j, int i, int i2) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        try {
            SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
            SharedPreferences.Editor edit = notificationsSettings.edit();
            if (i2 == 0 || i2 == -1) {
                String str = "org.telegram.key" + j;
                if (i != 0) {
                    str = str + ".topic" + i;
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
            if (i2 == 1 || i2 == -1) {
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

    public void deleteNotificationChannel(final long j, final int i, final int i2) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$deleteNotificationChannel$31(j, i, i2);
            }
        });
    }

    public void deleteNotificationChannelGlobal(int i) {
        deleteNotificationChannelGlobal(i, -1);
    }

    /* renamed from: deleteNotificationChannelGlobalInternal */
    public void lambda$deleteNotificationChannelGlobal$32(int i, int i2) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        try {
            SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
            SharedPreferences.Editor edit = notificationsSettings.edit();
            if (i2 == 0 || i2 == -1) {
                String str = i == 2 ? "channels" : i == 0 ? "groups" : "private";
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
                String str2 = i == 2 ? "channels_ia" : i == 0 ? "groups_ia" : "private_ia";
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
            edit.remove(i == 2 ? "overwrite_channel" : i == 0 ? "overwrite_group" : "overwrite_private");
            edit.commit();
        } catch (Exception e3) {
            FileLog.e(e3);
        }
    }

    public void deleteNotificationChannelGlobal(final int i, final int i2) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$deleteNotificationChannelGlobal$32(i, i2);
            }
        });
    }

    public void deleteAllNotificationChannels() {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$deleteAllNotificationChannels$33();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteAllNotificationChannels$33() {
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

    /* JADX WARN: Removed duplicated region for block: B:26:0x00f0 A[Catch: Exception -> 0x014e, TryCatch #0 {Exception -> 0x014e, blocks: (B:9:0x0020, B:12:0x0061, B:14:0x0069, B:18:0x0079, B:20:0x00a2, B:22:0x00b2, B:24:0x00bc, B:26:0x00f0, B:28:0x00f8, B:30:0x0101, B:38:0x0120, B:42:0x0137, B:32:0x0108, B:34:0x010e, B:36:0x0113, B:35:0x0111, B:37:0x0118, B:27:0x00f4, B:17:0x0075, B:13:0x0065), top: B:49:0x0020 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00f4 A[Catch: Exception -> 0x014e, TryCatch #0 {Exception -> 0x014e, blocks: (B:9:0x0020, B:12:0x0061, B:14:0x0069, B:18:0x0079, B:20:0x00a2, B:22:0x00b2, B:24:0x00bc, B:26:0x00f0, B:28:0x00f8, B:30:0x0101, B:38:0x0120, B:42:0x0137, B:32:0x0108, B:34:0x010e, B:36:0x0113, B:35:0x0111, B:37:0x0118, B:27:0x00f4, B:17:0x0075, B:13:0x0065), top: B:49:0x0020 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0101 A[Catch: Exception -> 0x014e, TryCatch #0 {Exception -> 0x014e, blocks: (B:9:0x0020, B:12:0x0061, B:14:0x0069, B:18:0x0079, B:20:0x00a2, B:22:0x00b2, B:24:0x00bc, B:26:0x00f0, B:28:0x00f8, B:30:0x0101, B:38:0x0120, B:42:0x0137, B:32:0x0108, B:34:0x010e, B:36:0x0113, B:35:0x0111, B:37:0x0118, B:27:0x00f4, B:17:0x0075, B:13:0x0065), top: B:49:0x0020 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0134  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0136  */
    @SuppressLint({"RestrictedApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String createNotificationShortcut(NotificationCompat.Builder builder, long j, String str, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, Person person) {
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
                        NotificationCompat.BubbleMetadata.Builder builder2 = new NotificationCompat.BubbleMetadata.Builder(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 167772160), createWithResource);
                        builder2.setSuppressNotification(this.openedDialogId != j);
                        builder2.setAutoExpandBubble(false);
                        builder2.setDesiredHeight(AndroidUtilities.dp(640.0f));
                        builder.setBubbleMetadata(builder2.build());
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
                NotificationCompat.BubbleMetadata.Builder builder22 = new NotificationCompat.BubbleMetadata.Builder(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22, 167772160), createWithResource);
                builder22.setSuppressNotification(this.openedDialogId != j);
                builder22.setAutoExpandBubble(false);
                builder22.setDesiredHeight(AndroidUtilities.dp(640.0f));
                builder.setBubbleMetadata(builder22.build());
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
            this.groupsCreated = Boolean.valueOf(notificationsSettings.getBoolean("groupsCreated4", false));
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
            notificationsSettings.edit().putBoolean("groupsCreated4", true).commit();
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
        String str5 = "private" + this.currentAccount;
        for (int i2 = 0; i2 < size2; i2++) {
            String id2 = notificationChannelGroups.get(i2).getId();
            if (str2 != null && str2.equals(id2)) {
                str2 = null;
            } else if (str3 != null && str3.equals(id2)) {
                str3 = null;
            } else if (str5 != null && str5.equals(id2)) {
                str5 = null;
            } else if (str4 != null && str4.equals(id2)) {
                str4 = null;
            }
            if (str2 == null && str3 == null && str5 == null && str4 == null) {
                break;
            }
        }
        if (str2 != null || str3 != null || str5 != null || str4 != null) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(getUserConfig().getClientUserId()));
            if (user == null) {
                getUserConfig().getCurrentUser();
            }
            String str6 = user != null ? " (" + ContactsController.formatName(user.first_name, user.last_name) + ")" : "";
            ArrayList arrayList = new ArrayList();
            if (str2 != null) {
                arrayList.add(new NotificationChannelGroup(str2, LocaleController.getString("NotificationsChannels", R.string.NotificationsChannels) + str6));
            }
            if (str3 != null) {
                arrayList.add(new NotificationChannelGroup(str3, LocaleController.getString("NotificationsGroups", R.string.NotificationsGroups) + str6));
            }
            if (str5 != null) {
                arrayList.add(new NotificationChannelGroup(str5, LocaleController.getString("NotificationsPrivateChats", R.string.NotificationsPrivateChats) + str6));
            }
            if (str4 != null) {
                arrayList.add(new NotificationChannelGroup(str4, LocaleController.getString("NotificationsOther", R.string.NotificationsOther) + str6));
            }
            systemNotificationManager.createNotificationChannelGroups(arrayList);
        }
        this.channelGroupsCreated = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:191:0x0419 A[LOOP:1: B:189:0x0416->B:191:0x0419, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:194:0x042b  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x0479  */
    @TargetApi(26)
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String validateChannelId(long j, int i, String str, long[] jArr, int i2, Uri uri, int i3, boolean z, boolean z2, boolean z3, int i4) {
        String str2;
        String str3;
        String formatString;
        int i5;
        String str4;
        String str5;
        String str6;
        boolean z4;
        String str7;
        long j2;
        int i6;
        int i7;
        long[] jArr2;
        String str8;
        boolean z5;
        String str9;
        String str10;
        int i8;
        SharedPreferences sharedPreferences;
        Uri uri2;
        String MD5;
        String str11;
        boolean z6;
        boolean z7;
        boolean z8;
        String str12;
        boolean z9;
        long[] jArr3;
        SharedPreferences.Editor editor;
        SharedPreferences.Editor editor2;
        int i9;
        ensureGroupsCreated();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        String str13 = "private";
        if (z3) {
            str2 = "other" + this.currentAccount;
            str3 = null;
        } else if (i4 == 2) {
            str2 = "channels" + this.currentAccount;
            str3 = "overwrite_channel";
        } else if (i4 == 0) {
            str2 = "groups" + this.currentAccount;
            str3 = "overwrite_group";
        } else {
            str2 = "private" + this.currentAccount;
            str3 = "overwrite_private";
        }
        boolean z10 = !z && DialogObject.isEncryptedDialog(j);
        boolean z11 = (z2 || str3 == null || !notificationsSettings.getBoolean(str3, false)) ? false : true;
        String MD52 = Utilities.MD5(uri == null ? "NoSound2" : uri.toString());
        if (MD52 != null && MD52.length() > 5) {
            MD52 = MD52.substring(0, 5);
        }
        if (z3) {
            formatString = LocaleController.getString("NotificationsSilent", R.string.NotificationsSilent);
            str13 = "silent";
        } else if (z) {
            if (z2) {
                i5 = R.string.NotificationsInAppDefault;
                str4 = "NotificationsInAppDefault";
            } else {
                i5 = R.string.NotificationsDefault;
                str4 = "NotificationsDefault";
            }
            String string = LocaleController.getString(str4, i5);
            if (i4 == 2) {
                str13 = z2 ? "channels_ia" : "channels";
            } else if (i4 == 0) {
                str13 = z2 ? "groups_ia" : "groups";
            } else if (z2) {
                str13 = "private_ia";
            }
            formatString = string;
        } else {
            formatString = z2 ? LocaleController.formatString("NotificationsChatInApp", R.string.NotificationsChatInApp, str) : str;
            StringBuilder sb = new StringBuilder();
            sb.append(z2 ? "org.telegram.keyia" : "org.telegram.key");
            sb.append(j);
            sb.append("_");
            sb.append(i);
            str13 = sb.toString();
        }
        String str14 = str13 + "_" + MD52;
        String string2 = notificationsSettings.getString(str14, null);
        String string3 = notificationsSettings.getString(str14 + "_s", null);
        StringBuilder sb2 = new StringBuilder();
        String str15 = formatString;
        String str16 = str2;
        if (string2 != null) {
            NotificationChannel notificationChannel = systemNotificationManager.getNotificationChannel(string2);
            str6 = "_";
            if (BuildVars.LOGS_ENABLED) {
                StringBuilder sb3 = new StringBuilder();
                str7 = "_s";
                sb3.append("current channel for ");
                sb3.append(string2);
                sb3.append(" = ");
                sb3.append(notificationChannel);
                FileLog.d(sb3.toString());
            } else {
                str7 = "_s";
            }
            if (notificationChannel == null) {
                j2 = j;
                i6 = i3;
                str5 = str14;
                z4 = z11;
                i7 = i2;
                jArr2 = jArr;
                str8 = null;
                z5 = false;
                string2 = null;
                string3 = null;
            } else if (z3 || z11) {
                j2 = j;
                i6 = i3;
                str5 = str14;
                z4 = z11;
            } else {
                int importance = notificationChannel.getImportance();
                Uri sound = notificationChannel.getSound();
                long[] vibrationPattern = notificationChannel.getVibrationPattern();
                z4 = z11;
                boolean shouldVibrate = notificationChannel.shouldVibrate();
                if (shouldVibrate || vibrationPattern != null) {
                    str5 = str14;
                    z8 = shouldVibrate;
                    jArr2 = vibrationPattern;
                } else {
                    str5 = str14;
                    z8 = shouldVibrate;
                    jArr2 = new long[]{0, 0};
                }
                int lightColor = notificationChannel.getLightColor();
                if (jArr2 != null) {
                    for (long j3 : jArr2) {
                        sb2.append(j3);
                    }
                }
                sb2.append(lightColor);
                if (sound != null) {
                    sb2.append(sound.toString());
                }
                sb2.append(importance);
                if (!z && z10) {
                    sb2.append("secret");
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("current channel settings for " + string2 + " = " + ((Object) sb2) + " old = " + string3);
                }
                String MD53 = Utilities.MD5(sb2.toString());
                sb2.setLength(0);
                if (MD53.equals(string3)) {
                    j2 = j;
                    i6 = i3;
                    str12 = MD53;
                    jArr2 = jArr;
                    lightColor = i2;
                    z9 = false;
                } else {
                    if (importance == 0) {
                        editor = notificationsSettings.edit();
                        if (z) {
                            if (!z2) {
                                editor.putInt(getGlobalNotificationsKey(i4), ConnectionsManager.DEFAULT_DATACENTER_ID);
                                updateServerNotificationsSettings(i4);
                            }
                            j2 = j;
                            z9 = true;
                        } else {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(NotificationsSettingsFacade.PROPERTY_NOTIFY);
                            j2 = j;
                            sb4.append(getSharedPrefKey(j2, 0));
                            editor.putInt(sb4.toString(), 2);
                            z9 = true;
                            updateServerNotificationsSettings(j2, 0, true);
                        }
                        i6 = i3;
                        str12 = MD53;
                        jArr3 = jArr;
                    } else {
                        j2 = j;
                        i6 = i3;
                        if (importance != i6) {
                            if (z2) {
                                str12 = MD53;
                                editor2 = null;
                            } else {
                                editor2 = notificationsSettings.edit();
                                str12 = MD53;
                                int i10 = (importance == 4 || importance == 5) ? 1 : importance == 1 ? 4 : importance == 2 ? 5 : 0;
                                if (z) {
                                    editor2.putInt(getGlobalNotificationsKey(i4), 0).commit();
                                    if (i4 == 2) {
                                        editor2.putInt("priority_channel", i10);
                                    } else if (i4 == 0) {
                                        editor2.putInt("priority_group", i10);
                                    } else {
                                        editor2.putInt("priority_messages", i10);
                                    }
                                } else {
                                    editor2.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + j2, 0);
                                    editor2.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + j2);
                                    editor2.putInt("priority_" + j2, i10);
                                }
                            }
                            jArr3 = jArr;
                            editor = editor2;
                            z9 = true;
                        } else {
                            str12 = MD53;
                            jArr3 = jArr;
                            editor = null;
                            z9 = false;
                        }
                    }
                    boolean z12 = !isEmptyVibration(jArr3);
                    boolean z13 = z8;
                    if (z12 != z13) {
                        if (!z2) {
                            if (editor == null) {
                                editor = notificationsSettings.edit();
                            }
                            if (!z) {
                                editor.putInt("vibrate_" + j2, z13 ? 0 : 2);
                            } else if (i4 == 2) {
                                editor.putInt("vibrate_channel", z13 ? 0 : 2);
                            } else if (i4 == 0) {
                                editor.putInt("vibrate_group", z13 ? 0 : 2);
                            } else {
                                editor.putInt("vibrate_messages", z13 ? 0 : 2);
                            }
                        }
                        i9 = i2;
                        z9 = true;
                    } else {
                        jArr2 = jArr;
                        i9 = i2;
                    }
                    if (lightColor != i9) {
                        if (!z2) {
                            if (editor == null) {
                                editor = notificationsSettings.edit();
                            }
                            if (!z) {
                                editor.putInt("color_" + j2, lightColor);
                            } else if (i4 == 2) {
                                editor.putInt("ChannelLed", lightColor);
                            } else if (i4 == 0) {
                                editor.putInt("GroupLed", lightColor);
                            } else {
                                editor.putInt("MessagesLed", lightColor);
                            }
                        }
                        z9 = true;
                    } else {
                        lightColor = i9;
                    }
                    if (editor != null) {
                        editor.commit();
                    }
                }
                i7 = lightColor;
                str8 = str12;
                z5 = z9;
            }
            if (z5 || str8 == null) {
                str9 = str7;
                str10 = str5;
                if (!z4 || str8 == null || !z2 || !z) {
                    i8 = 0;
                    while (i8 < jArr2.length) {
                        sb2.append(jArr2[i8]);
                        i8++;
                        notificationsSettings = notificationsSettings;
                    }
                    sharedPreferences = notificationsSettings;
                    sb2.append(i7);
                    uri2 = uri;
                    if (uri2 != null) {
                        sb2.append(uri.toString());
                    }
                    sb2.append(i6);
                    if (!z && z10) {
                        sb2.append("secret");
                    }
                    MD5 = Utilities.MD5(sb2.toString());
                    if (z3 && string2 != null && (z4 || !string3.equals(MD5))) {
                        try {
                            systemNotificationManager.deleteNotificationChannel(string2);
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("delete channel by settings change " + string2);
                        }
                        str8 = MD5;
                        str11 = null;
                        if (str11 == null) {
                        }
                        return str11;
                    }
                    str8 = MD5;
                    str11 = string2;
                    if (str11 == null) {
                        str11 = z ? this.currentAccount + "channel_" + str10 + str6 + Utilities.random.nextLong() : this.currentAccount + "channel_" + j2 + str6 + Utilities.random.nextLong();
                        NotificationChannel notificationChannel2 = new NotificationChannel(str11, z10 ? LocaleController.getString("SecretChatName", R.string.SecretChatName) : str15, i6);
                        notificationChannel2.setGroup(str16);
                        if (i7 != 0) {
                            z7 = true;
                            notificationChannel2.enableLights(true);
                            notificationChannel2.setLightColor(i7);
                            z6 = false;
                        } else {
                            z6 = false;
                            z7 = true;
                            notificationChannel2.enableLights(false);
                        }
                        if (!isEmptyVibration(jArr2)) {
                            notificationChannel2.enableVibration(z7);
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
                            FileLog.d("create new channel " + str11);
                        }
                        this.lastNotificationChannelCreateTime = SystemClock.elapsedRealtime();
                        systemNotificationManager.createNotificationChannel(notificationChannel2);
                        sharedPreferences.edit().putString(str10, str11).putString(str10 + str9, str8).commit();
                    }
                    return str11;
                }
            } else {
                str10 = str5;
                SharedPreferences.Editor putString = notificationsSettings.edit().putString(str10, string2);
                StringBuilder sb5 = new StringBuilder();
                sb5.append(str10);
                str9 = str7;
                sb5.append(str9);
                putString.putString(sb5.toString(), str8).commit();
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("change edited channel " + string2);
                }
            }
            uri2 = uri;
            sharedPreferences = notificationsSettings;
            str11 = string2;
            if (str11 == null) {
            }
            return str11;
        }
        str5 = str14;
        str6 = "_";
        z4 = z11;
        str7 = "_s";
        j2 = j;
        i6 = i3;
        i7 = i2;
        jArr2 = jArr;
        str8 = null;
        z5 = false;
        if (z5) {
        }
        str9 = str7;
        str10 = str5;
        if (!z4) {
        }
        i8 = 0;
        while (i8 < jArr2.length) {
        }
        sharedPreferences = notificationsSettings;
        sb2.append(i7);
        uri2 = uri;
        if (uri2 != null) {
        }
        sb2.append(i6);
        if (!z) {
            sb2.append("secret");
        }
        MD5 = Utilities.MD5(sb2.toString());
        if (z3) {
        }
        str8 = MD5;
        str11 = string2;
        if (str11 == null) {
        }
        return str11;
    }

    /* JADX WARN: Code restructure failed: missing block: B:295:0x0719, code lost:
        if (r2.local_id != 0) goto L204;
     */
    /* JADX WARN: Code restructure failed: missing block: B:376:0x08a6, code lost:
        if (android.os.Build.VERSION.SDK_INT >= 26) goto L346;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0153, code lost:
        if (r10 == 0) goto L460;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0155, code lost:
        r3 = org.telegram.messenger.LocaleController.getString("NotificationHiddenChatName", org.telegram.messenger.R.string.NotificationHiddenChatName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x015e, code lost:
        r3 = org.telegram.messenger.LocaleController.getString("NotificationHiddenName", org.telegram.messenger.R.string.NotificationHiddenName);
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:102:0x01d9 A[Catch: Exception -> 0x0b62, TRY_ENTER, TryCatch #4 {Exception -> 0x0b62, blocks: (B:11:0x0022, B:13:0x0046, B:15:0x004a, B:17:0x0062, B:19:0x0068, B:23:0x007c, B:27:0x008a, B:29:0x0096, B:30:0x009c, B:32:0x00ac, B:34:0x00ba, B:36:0x00c0, B:45:0x00db, B:47:0x00e7, B:56:0x0107, B:58:0x010d, B:63:0x011d, B:65:0x0125, B:70:0x012d, B:72:0x0133, B:85:0x0172, B:88:0x017d, B:90:0x0185, B:93:0x01b2, B:95:0x01bd, B:104:0x0234, B:107:0x024a, B:112:0x0267, B:118:0x02a9, B:144:0x037b, B:156:0x0396, B:158:0x03b2, B:161:0x03e9, B:163:0x03f3, B:164:0x0406, B:166:0x041a, B:175:0x0449, B:179:0x046d, B:183:0x047c, B:185:0x04a4, B:187:0x04dc, B:189:0x0505, B:191:0x0513, B:198:0x0537, B:200:0x0541, B:202:0x0556, B:221:0x061c, B:223:0x0622, B:232:0x0639, B:234:0x063f, B:242:0x0651, B:245:0x065b, B:248:0x0664, B:265:0x068c, B:268:0x0695, B:270:0x06c8, B:274:0x06d7, B:277:0x06e1, B:278:0x06e9, B:280:0x06ef, B:283:0x06f4, B:285:0x06fd, B:288:0x0705, B:290:0x070b, B:292:0x070f, B:294:0x0717, B:317:0x0762, B:320:0x07c2, B:322:0x07c6, B:324:0x07cc, B:332:0x080e, B:334:0x081b, B:355:0x086b, B:382:0x08b3, B:391:0x08f2, B:393:0x08fa, B:395:0x08fe, B:397:0x0906, B:401:0x090f, B:419:0x09a9, B:423:0x09b8, B:440:0x0a1d, B:442:0x0a23, B:444:0x0a27, B:446:0x0a32, B:448:0x0a38, B:450:0x0a43, B:452:0x0a52, B:454:0x0a5e, B:456:0x0a7e, B:458:0x0a88, B:460:0x0ab9, B:461:0x0acd, B:465:0x0af1, B:467:0x0af7, B:469:0x0aff, B:471:0x0b05, B:473:0x0b17, B:474:0x0b2e, B:475:0x0b44, B:426:0x09c9, B:434:0x09ec, B:436:0x09ff, B:402:0x0939, B:403:0x093e, B:404:0x0941, B:406:0x0947, B:409:0x0951, B:411:0x0959, B:415:0x0997, B:416:0x099f, B:385:0x08bd, B:387:0x08c5, B:389:0x08ed, B:439:0x0a0a, B:365:0x0880, B:369:0x088d, B:372:0x0896, B:375:0x08a0, B:299:0x0724, B:301:0x0728, B:303:0x072c, B:305:0x0734, B:311:0x0743, B:313:0x074e, B:315:0x0758, B:201:0x054e, B:203:0x0579, B:205:0x0585, B:207:0x059a, B:206:0x0592, B:210:0x05c1, B:212:0x05cb, B:214:0x05e0, B:213:0x05d8, B:188:0x04ee, B:167:0x0426, B:169:0x042a, B:113:0x027b, B:115:0x0280, B:116:0x0294, B:119:0x02c1, B:121:0x02e5, B:123:0x02fd, B:128:0x0307, B:129:0x030d, B:133:0x031a, B:134:0x032e, B:136:0x0333, B:137:0x0347, B:138:0x035a, B:140:0x0362, B:141:0x036b, B:99:0x01cc, B:102:0x01d9, B:103:0x01f8, B:91:0x0192, B:81:0x0155, B:82:0x015e, B:83:0x0167, B:61:0x0116, B:62:0x0119, B:37:0x00c3, B:39:0x00c9, B:22:0x007a, B:258:0x067d, B:326:0x07e5, B:413:0x0963), top: B:488:0x0022, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:103:0x01f8 A[Catch: Exception -> 0x0b62, TryCatch #4 {Exception -> 0x0b62, blocks: (B:11:0x0022, B:13:0x0046, B:15:0x004a, B:17:0x0062, B:19:0x0068, B:23:0x007c, B:27:0x008a, B:29:0x0096, B:30:0x009c, B:32:0x00ac, B:34:0x00ba, B:36:0x00c0, B:45:0x00db, B:47:0x00e7, B:56:0x0107, B:58:0x010d, B:63:0x011d, B:65:0x0125, B:70:0x012d, B:72:0x0133, B:85:0x0172, B:88:0x017d, B:90:0x0185, B:93:0x01b2, B:95:0x01bd, B:104:0x0234, B:107:0x024a, B:112:0x0267, B:118:0x02a9, B:144:0x037b, B:156:0x0396, B:158:0x03b2, B:161:0x03e9, B:163:0x03f3, B:164:0x0406, B:166:0x041a, B:175:0x0449, B:179:0x046d, B:183:0x047c, B:185:0x04a4, B:187:0x04dc, B:189:0x0505, B:191:0x0513, B:198:0x0537, B:200:0x0541, B:202:0x0556, B:221:0x061c, B:223:0x0622, B:232:0x0639, B:234:0x063f, B:242:0x0651, B:245:0x065b, B:248:0x0664, B:265:0x068c, B:268:0x0695, B:270:0x06c8, B:274:0x06d7, B:277:0x06e1, B:278:0x06e9, B:280:0x06ef, B:283:0x06f4, B:285:0x06fd, B:288:0x0705, B:290:0x070b, B:292:0x070f, B:294:0x0717, B:317:0x0762, B:320:0x07c2, B:322:0x07c6, B:324:0x07cc, B:332:0x080e, B:334:0x081b, B:355:0x086b, B:382:0x08b3, B:391:0x08f2, B:393:0x08fa, B:395:0x08fe, B:397:0x0906, B:401:0x090f, B:419:0x09a9, B:423:0x09b8, B:440:0x0a1d, B:442:0x0a23, B:444:0x0a27, B:446:0x0a32, B:448:0x0a38, B:450:0x0a43, B:452:0x0a52, B:454:0x0a5e, B:456:0x0a7e, B:458:0x0a88, B:460:0x0ab9, B:461:0x0acd, B:465:0x0af1, B:467:0x0af7, B:469:0x0aff, B:471:0x0b05, B:473:0x0b17, B:474:0x0b2e, B:475:0x0b44, B:426:0x09c9, B:434:0x09ec, B:436:0x09ff, B:402:0x0939, B:403:0x093e, B:404:0x0941, B:406:0x0947, B:409:0x0951, B:411:0x0959, B:415:0x0997, B:416:0x099f, B:385:0x08bd, B:387:0x08c5, B:389:0x08ed, B:439:0x0a0a, B:365:0x0880, B:369:0x088d, B:372:0x0896, B:375:0x08a0, B:299:0x0724, B:301:0x0728, B:303:0x072c, B:305:0x0734, B:311:0x0743, B:313:0x074e, B:315:0x0758, B:201:0x054e, B:203:0x0579, B:205:0x0585, B:207:0x059a, B:206:0x0592, B:210:0x05c1, B:212:0x05cb, B:214:0x05e0, B:213:0x05d8, B:188:0x04ee, B:167:0x0426, B:169:0x042a, B:113:0x027b, B:115:0x0280, B:116:0x0294, B:119:0x02c1, B:121:0x02e5, B:123:0x02fd, B:128:0x0307, B:129:0x030d, B:133:0x031a, B:134:0x032e, B:136:0x0333, B:137:0x0347, B:138:0x035a, B:140:0x0362, B:141:0x036b, B:99:0x01cc, B:102:0x01d9, B:103:0x01f8, B:91:0x0192, B:81:0x0155, B:82:0x015e, B:83:0x0167, B:61:0x0116, B:62:0x0119, B:37:0x00c3, B:39:0x00c9, B:22:0x007a, B:258:0x067d, B:326:0x07e5, B:413:0x0963), top: B:488:0x0022, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:107:0x024a A[Catch: Exception -> 0x0b62, TRY_ENTER, TryCatch #4 {Exception -> 0x0b62, blocks: (B:11:0x0022, B:13:0x0046, B:15:0x004a, B:17:0x0062, B:19:0x0068, B:23:0x007c, B:27:0x008a, B:29:0x0096, B:30:0x009c, B:32:0x00ac, B:34:0x00ba, B:36:0x00c0, B:45:0x00db, B:47:0x00e7, B:56:0x0107, B:58:0x010d, B:63:0x011d, B:65:0x0125, B:70:0x012d, B:72:0x0133, B:85:0x0172, B:88:0x017d, B:90:0x0185, B:93:0x01b2, B:95:0x01bd, B:104:0x0234, B:107:0x024a, B:112:0x0267, B:118:0x02a9, B:144:0x037b, B:156:0x0396, B:158:0x03b2, B:161:0x03e9, B:163:0x03f3, B:164:0x0406, B:166:0x041a, B:175:0x0449, B:179:0x046d, B:183:0x047c, B:185:0x04a4, B:187:0x04dc, B:189:0x0505, B:191:0x0513, B:198:0x0537, B:200:0x0541, B:202:0x0556, B:221:0x061c, B:223:0x0622, B:232:0x0639, B:234:0x063f, B:242:0x0651, B:245:0x065b, B:248:0x0664, B:265:0x068c, B:268:0x0695, B:270:0x06c8, B:274:0x06d7, B:277:0x06e1, B:278:0x06e9, B:280:0x06ef, B:283:0x06f4, B:285:0x06fd, B:288:0x0705, B:290:0x070b, B:292:0x070f, B:294:0x0717, B:317:0x0762, B:320:0x07c2, B:322:0x07c6, B:324:0x07cc, B:332:0x080e, B:334:0x081b, B:355:0x086b, B:382:0x08b3, B:391:0x08f2, B:393:0x08fa, B:395:0x08fe, B:397:0x0906, B:401:0x090f, B:419:0x09a9, B:423:0x09b8, B:440:0x0a1d, B:442:0x0a23, B:444:0x0a27, B:446:0x0a32, B:448:0x0a38, B:450:0x0a43, B:452:0x0a52, B:454:0x0a5e, B:456:0x0a7e, B:458:0x0a88, B:460:0x0ab9, B:461:0x0acd, B:465:0x0af1, B:467:0x0af7, B:469:0x0aff, B:471:0x0b05, B:473:0x0b17, B:474:0x0b2e, B:475:0x0b44, B:426:0x09c9, B:434:0x09ec, B:436:0x09ff, B:402:0x0939, B:403:0x093e, B:404:0x0941, B:406:0x0947, B:409:0x0951, B:411:0x0959, B:415:0x0997, B:416:0x099f, B:385:0x08bd, B:387:0x08c5, B:389:0x08ed, B:439:0x0a0a, B:365:0x0880, B:369:0x088d, B:372:0x0896, B:375:0x08a0, B:299:0x0724, B:301:0x0728, B:303:0x072c, B:305:0x0734, B:311:0x0743, B:313:0x074e, B:315:0x0758, B:201:0x054e, B:203:0x0579, B:205:0x0585, B:207:0x059a, B:206:0x0592, B:210:0x05c1, B:212:0x05cb, B:214:0x05e0, B:213:0x05d8, B:188:0x04ee, B:167:0x0426, B:169:0x042a, B:113:0x027b, B:115:0x0280, B:116:0x0294, B:119:0x02c1, B:121:0x02e5, B:123:0x02fd, B:128:0x0307, B:129:0x030d, B:133:0x031a, B:134:0x032e, B:136:0x0333, B:137:0x0347, B:138:0x035a, B:140:0x0362, B:141:0x036b, B:99:0x01cc, B:102:0x01d9, B:103:0x01f8, B:91:0x0192, B:81:0x0155, B:82:0x015e, B:83:0x0167, B:61:0x0116, B:62:0x0119, B:37:0x00c3, B:39:0x00c9, B:22:0x007a, B:258:0x067d, B:326:0x07e5, B:413:0x0963), top: B:488:0x0022, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x02c1 A[Catch: Exception -> 0x0b62, TryCatch #4 {Exception -> 0x0b62, blocks: (B:11:0x0022, B:13:0x0046, B:15:0x004a, B:17:0x0062, B:19:0x0068, B:23:0x007c, B:27:0x008a, B:29:0x0096, B:30:0x009c, B:32:0x00ac, B:34:0x00ba, B:36:0x00c0, B:45:0x00db, B:47:0x00e7, B:56:0x0107, B:58:0x010d, B:63:0x011d, B:65:0x0125, B:70:0x012d, B:72:0x0133, B:85:0x0172, B:88:0x017d, B:90:0x0185, B:93:0x01b2, B:95:0x01bd, B:104:0x0234, B:107:0x024a, B:112:0x0267, B:118:0x02a9, B:144:0x037b, B:156:0x0396, B:158:0x03b2, B:161:0x03e9, B:163:0x03f3, B:164:0x0406, B:166:0x041a, B:175:0x0449, B:179:0x046d, B:183:0x047c, B:185:0x04a4, B:187:0x04dc, B:189:0x0505, B:191:0x0513, B:198:0x0537, B:200:0x0541, B:202:0x0556, B:221:0x061c, B:223:0x0622, B:232:0x0639, B:234:0x063f, B:242:0x0651, B:245:0x065b, B:248:0x0664, B:265:0x068c, B:268:0x0695, B:270:0x06c8, B:274:0x06d7, B:277:0x06e1, B:278:0x06e9, B:280:0x06ef, B:283:0x06f4, B:285:0x06fd, B:288:0x0705, B:290:0x070b, B:292:0x070f, B:294:0x0717, B:317:0x0762, B:320:0x07c2, B:322:0x07c6, B:324:0x07cc, B:332:0x080e, B:334:0x081b, B:355:0x086b, B:382:0x08b3, B:391:0x08f2, B:393:0x08fa, B:395:0x08fe, B:397:0x0906, B:401:0x090f, B:419:0x09a9, B:423:0x09b8, B:440:0x0a1d, B:442:0x0a23, B:444:0x0a27, B:446:0x0a32, B:448:0x0a38, B:450:0x0a43, B:452:0x0a52, B:454:0x0a5e, B:456:0x0a7e, B:458:0x0a88, B:460:0x0ab9, B:461:0x0acd, B:465:0x0af1, B:467:0x0af7, B:469:0x0aff, B:471:0x0b05, B:473:0x0b17, B:474:0x0b2e, B:475:0x0b44, B:426:0x09c9, B:434:0x09ec, B:436:0x09ff, B:402:0x0939, B:403:0x093e, B:404:0x0941, B:406:0x0947, B:409:0x0951, B:411:0x0959, B:415:0x0997, B:416:0x099f, B:385:0x08bd, B:387:0x08c5, B:389:0x08ed, B:439:0x0a0a, B:365:0x0880, B:369:0x088d, B:372:0x0896, B:375:0x08a0, B:299:0x0724, B:301:0x0728, B:303:0x072c, B:305:0x0734, B:311:0x0743, B:313:0x074e, B:315:0x0758, B:201:0x054e, B:203:0x0579, B:205:0x0585, B:207:0x059a, B:206:0x0592, B:210:0x05c1, B:212:0x05cb, B:214:0x05e0, B:213:0x05d8, B:188:0x04ee, B:167:0x0426, B:169:0x042a, B:113:0x027b, B:115:0x0280, B:116:0x0294, B:119:0x02c1, B:121:0x02e5, B:123:0x02fd, B:128:0x0307, B:129:0x030d, B:133:0x031a, B:134:0x032e, B:136:0x0333, B:137:0x0347, B:138:0x035a, B:140:0x0362, B:141:0x036b, B:99:0x01cc, B:102:0x01d9, B:103:0x01f8, B:91:0x0192, B:81:0x0155, B:82:0x015e, B:83:0x0167, B:61:0x0116, B:62:0x0119, B:37:0x00c3, B:39:0x00c9, B:22:0x007a, B:258:0x067d, B:326:0x07e5, B:413:0x0963), top: B:488:0x0022, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0379 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:153:0x0390  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x0449 A[Catch: Exception -> 0x0b62, TryCatch #4 {Exception -> 0x0b62, blocks: (B:11:0x0022, B:13:0x0046, B:15:0x004a, B:17:0x0062, B:19:0x0068, B:23:0x007c, B:27:0x008a, B:29:0x0096, B:30:0x009c, B:32:0x00ac, B:34:0x00ba, B:36:0x00c0, B:45:0x00db, B:47:0x00e7, B:56:0x0107, B:58:0x010d, B:63:0x011d, B:65:0x0125, B:70:0x012d, B:72:0x0133, B:85:0x0172, B:88:0x017d, B:90:0x0185, B:93:0x01b2, B:95:0x01bd, B:104:0x0234, B:107:0x024a, B:112:0x0267, B:118:0x02a9, B:144:0x037b, B:156:0x0396, B:158:0x03b2, B:161:0x03e9, B:163:0x03f3, B:164:0x0406, B:166:0x041a, B:175:0x0449, B:179:0x046d, B:183:0x047c, B:185:0x04a4, B:187:0x04dc, B:189:0x0505, B:191:0x0513, B:198:0x0537, B:200:0x0541, B:202:0x0556, B:221:0x061c, B:223:0x0622, B:232:0x0639, B:234:0x063f, B:242:0x0651, B:245:0x065b, B:248:0x0664, B:265:0x068c, B:268:0x0695, B:270:0x06c8, B:274:0x06d7, B:277:0x06e1, B:278:0x06e9, B:280:0x06ef, B:283:0x06f4, B:285:0x06fd, B:288:0x0705, B:290:0x070b, B:292:0x070f, B:294:0x0717, B:317:0x0762, B:320:0x07c2, B:322:0x07c6, B:324:0x07cc, B:332:0x080e, B:334:0x081b, B:355:0x086b, B:382:0x08b3, B:391:0x08f2, B:393:0x08fa, B:395:0x08fe, B:397:0x0906, B:401:0x090f, B:419:0x09a9, B:423:0x09b8, B:440:0x0a1d, B:442:0x0a23, B:444:0x0a27, B:446:0x0a32, B:448:0x0a38, B:450:0x0a43, B:452:0x0a52, B:454:0x0a5e, B:456:0x0a7e, B:458:0x0a88, B:460:0x0ab9, B:461:0x0acd, B:465:0x0af1, B:467:0x0af7, B:469:0x0aff, B:471:0x0b05, B:473:0x0b17, B:474:0x0b2e, B:475:0x0b44, B:426:0x09c9, B:434:0x09ec, B:436:0x09ff, B:402:0x0939, B:403:0x093e, B:404:0x0941, B:406:0x0947, B:409:0x0951, B:411:0x0959, B:415:0x0997, B:416:0x099f, B:385:0x08bd, B:387:0x08c5, B:389:0x08ed, B:439:0x0a0a, B:365:0x0880, B:369:0x088d, B:372:0x0896, B:375:0x08a0, B:299:0x0724, B:301:0x0728, B:303:0x072c, B:305:0x0734, B:311:0x0743, B:313:0x074e, B:315:0x0758, B:201:0x054e, B:203:0x0579, B:205:0x0585, B:207:0x059a, B:206:0x0592, B:210:0x05c1, B:212:0x05cb, B:214:0x05e0, B:213:0x05d8, B:188:0x04ee, B:167:0x0426, B:169:0x042a, B:113:0x027b, B:115:0x0280, B:116:0x0294, B:119:0x02c1, B:121:0x02e5, B:123:0x02fd, B:128:0x0307, B:129:0x030d, B:133:0x031a, B:134:0x032e, B:136:0x0333, B:137:0x0347, B:138:0x035a, B:140:0x0362, B:141:0x036b, B:99:0x01cc, B:102:0x01d9, B:103:0x01f8, B:91:0x0192, B:81:0x0155, B:82:0x015e, B:83:0x0167, B:61:0x0116, B:62:0x0119, B:37:0x00c3, B:39:0x00c9, B:22:0x007a, B:258:0x067d, B:326:0x07e5, B:413:0x0963), top: B:488:0x0022, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:178:0x046b  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x0477  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x047a  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x04a4 A[Catch: Exception -> 0x0b62, TryCatch #4 {Exception -> 0x0b62, blocks: (B:11:0x0022, B:13:0x0046, B:15:0x004a, B:17:0x0062, B:19:0x0068, B:23:0x007c, B:27:0x008a, B:29:0x0096, B:30:0x009c, B:32:0x00ac, B:34:0x00ba, B:36:0x00c0, B:45:0x00db, B:47:0x00e7, B:56:0x0107, B:58:0x010d, B:63:0x011d, B:65:0x0125, B:70:0x012d, B:72:0x0133, B:85:0x0172, B:88:0x017d, B:90:0x0185, B:93:0x01b2, B:95:0x01bd, B:104:0x0234, B:107:0x024a, B:112:0x0267, B:118:0x02a9, B:144:0x037b, B:156:0x0396, B:158:0x03b2, B:161:0x03e9, B:163:0x03f3, B:164:0x0406, B:166:0x041a, B:175:0x0449, B:179:0x046d, B:183:0x047c, B:185:0x04a4, B:187:0x04dc, B:189:0x0505, B:191:0x0513, B:198:0x0537, B:200:0x0541, B:202:0x0556, B:221:0x061c, B:223:0x0622, B:232:0x0639, B:234:0x063f, B:242:0x0651, B:245:0x065b, B:248:0x0664, B:265:0x068c, B:268:0x0695, B:270:0x06c8, B:274:0x06d7, B:277:0x06e1, B:278:0x06e9, B:280:0x06ef, B:283:0x06f4, B:285:0x06fd, B:288:0x0705, B:290:0x070b, B:292:0x070f, B:294:0x0717, B:317:0x0762, B:320:0x07c2, B:322:0x07c6, B:324:0x07cc, B:332:0x080e, B:334:0x081b, B:355:0x086b, B:382:0x08b3, B:391:0x08f2, B:393:0x08fa, B:395:0x08fe, B:397:0x0906, B:401:0x090f, B:419:0x09a9, B:423:0x09b8, B:440:0x0a1d, B:442:0x0a23, B:444:0x0a27, B:446:0x0a32, B:448:0x0a38, B:450:0x0a43, B:452:0x0a52, B:454:0x0a5e, B:456:0x0a7e, B:458:0x0a88, B:460:0x0ab9, B:461:0x0acd, B:465:0x0af1, B:467:0x0af7, B:469:0x0aff, B:471:0x0b05, B:473:0x0b17, B:474:0x0b2e, B:475:0x0b44, B:426:0x09c9, B:434:0x09ec, B:436:0x09ff, B:402:0x0939, B:403:0x093e, B:404:0x0941, B:406:0x0947, B:409:0x0951, B:411:0x0959, B:415:0x0997, B:416:0x099f, B:385:0x08bd, B:387:0x08c5, B:389:0x08ed, B:439:0x0a0a, B:365:0x0880, B:369:0x088d, B:372:0x0896, B:375:0x08a0, B:299:0x0724, B:301:0x0728, B:303:0x072c, B:305:0x0734, B:311:0x0743, B:313:0x074e, B:315:0x0758, B:201:0x054e, B:203:0x0579, B:205:0x0585, B:207:0x059a, B:206:0x0592, B:210:0x05c1, B:212:0x05cb, B:214:0x05e0, B:213:0x05d8, B:188:0x04ee, B:167:0x0426, B:169:0x042a, B:113:0x027b, B:115:0x0280, B:116:0x0294, B:119:0x02c1, B:121:0x02e5, B:123:0x02fd, B:128:0x0307, B:129:0x030d, B:133:0x031a, B:134:0x032e, B:136:0x0333, B:137:0x0347, B:138:0x035a, B:140:0x0362, B:141:0x036b, B:99:0x01cc, B:102:0x01d9, B:103:0x01f8, B:91:0x0192, B:81:0x0155, B:82:0x015e, B:83:0x0167, B:61:0x0116, B:62:0x0119, B:37:0x00c3, B:39:0x00c9, B:22:0x007a, B:258:0x067d, B:326:0x07e5, B:413:0x0963), top: B:488:0x0022, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:194:0x0521  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0535  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x05bc  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x0616  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x061a  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x0622 A[Catch: Exception -> 0x0b62, TryCatch #4 {Exception -> 0x0b62, blocks: (B:11:0x0022, B:13:0x0046, B:15:0x004a, B:17:0x0062, B:19:0x0068, B:23:0x007c, B:27:0x008a, B:29:0x0096, B:30:0x009c, B:32:0x00ac, B:34:0x00ba, B:36:0x00c0, B:45:0x00db, B:47:0x00e7, B:56:0x0107, B:58:0x010d, B:63:0x011d, B:65:0x0125, B:70:0x012d, B:72:0x0133, B:85:0x0172, B:88:0x017d, B:90:0x0185, B:93:0x01b2, B:95:0x01bd, B:104:0x0234, B:107:0x024a, B:112:0x0267, B:118:0x02a9, B:144:0x037b, B:156:0x0396, B:158:0x03b2, B:161:0x03e9, B:163:0x03f3, B:164:0x0406, B:166:0x041a, B:175:0x0449, B:179:0x046d, B:183:0x047c, B:185:0x04a4, B:187:0x04dc, B:189:0x0505, B:191:0x0513, B:198:0x0537, B:200:0x0541, B:202:0x0556, B:221:0x061c, B:223:0x0622, B:232:0x0639, B:234:0x063f, B:242:0x0651, B:245:0x065b, B:248:0x0664, B:265:0x068c, B:268:0x0695, B:270:0x06c8, B:274:0x06d7, B:277:0x06e1, B:278:0x06e9, B:280:0x06ef, B:283:0x06f4, B:285:0x06fd, B:288:0x0705, B:290:0x070b, B:292:0x070f, B:294:0x0717, B:317:0x0762, B:320:0x07c2, B:322:0x07c6, B:324:0x07cc, B:332:0x080e, B:334:0x081b, B:355:0x086b, B:382:0x08b3, B:391:0x08f2, B:393:0x08fa, B:395:0x08fe, B:397:0x0906, B:401:0x090f, B:419:0x09a9, B:423:0x09b8, B:440:0x0a1d, B:442:0x0a23, B:444:0x0a27, B:446:0x0a32, B:448:0x0a38, B:450:0x0a43, B:452:0x0a52, B:454:0x0a5e, B:456:0x0a7e, B:458:0x0a88, B:460:0x0ab9, B:461:0x0acd, B:465:0x0af1, B:467:0x0af7, B:469:0x0aff, B:471:0x0b05, B:473:0x0b17, B:474:0x0b2e, B:475:0x0b44, B:426:0x09c9, B:434:0x09ec, B:436:0x09ff, B:402:0x0939, B:403:0x093e, B:404:0x0941, B:406:0x0947, B:409:0x0951, B:411:0x0959, B:415:0x0997, B:416:0x099f, B:385:0x08bd, B:387:0x08c5, B:389:0x08ed, B:439:0x0a0a, B:365:0x0880, B:369:0x088d, B:372:0x0896, B:375:0x08a0, B:299:0x0724, B:301:0x0728, B:303:0x072c, B:305:0x0734, B:311:0x0743, B:313:0x074e, B:315:0x0758, B:201:0x054e, B:203:0x0579, B:205:0x0585, B:207:0x059a, B:206:0x0592, B:210:0x05c1, B:212:0x05cb, B:214:0x05e0, B:213:0x05d8, B:188:0x04ee, B:167:0x0426, B:169:0x042a, B:113:0x027b, B:115:0x0280, B:116:0x0294, B:119:0x02c1, B:121:0x02e5, B:123:0x02fd, B:128:0x0307, B:129:0x030d, B:133:0x031a, B:134:0x032e, B:136:0x0333, B:137:0x0347, B:138:0x035a, B:140:0x0362, B:141:0x036b, B:99:0x01cc, B:102:0x01d9, B:103:0x01f8, B:91:0x0192, B:81:0x0155, B:82:0x015e, B:83:0x0167, B:61:0x0116, B:62:0x0119, B:37:0x00c3, B:39:0x00c9, B:22:0x007a, B:258:0x067d, B:326:0x07e5, B:413:0x0963), top: B:488:0x0022, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:228:0x0632 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:232:0x0639 A[Catch: Exception -> 0x0b62, TryCatch #4 {Exception -> 0x0b62, blocks: (B:11:0x0022, B:13:0x0046, B:15:0x004a, B:17:0x0062, B:19:0x0068, B:23:0x007c, B:27:0x008a, B:29:0x0096, B:30:0x009c, B:32:0x00ac, B:34:0x00ba, B:36:0x00c0, B:45:0x00db, B:47:0x00e7, B:56:0x0107, B:58:0x010d, B:63:0x011d, B:65:0x0125, B:70:0x012d, B:72:0x0133, B:85:0x0172, B:88:0x017d, B:90:0x0185, B:93:0x01b2, B:95:0x01bd, B:104:0x0234, B:107:0x024a, B:112:0x0267, B:118:0x02a9, B:144:0x037b, B:156:0x0396, B:158:0x03b2, B:161:0x03e9, B:163:0x03f3, B:164:0x0406, B:166:0x041a, B:175:0x0449, B:179:0x046d, B:183:0x047c, B:185:0x04a4, B:187:0x04dc, B:189:0x0505, B:191:0x0513, B:198:0x0537, B:200:0x0541, B:202:0x0556, B:221:0x061c, B:223:0x0622, B:232:0x0639, B:234:0x063f, B:242:0x0651, B:245:0x065b, B:248:0x0664, B:265:0x068c, B:268:0x0695, B:270:0x06c8, B:274:0x06d7, B:277:0x06e1, B:278:0x06e9, B:280:0x06ef, B:283:0x06f4, B:285:0x06fd, B:288:0x0705, B:290:0x070b, B:292:0x070f, B:294:0x0717, B:317:0x0762, B:320:0x07c2, B:322:0x07c6, B:324:0x07cc, B:332:0x080e, B:334:0x081b, B:355:0x086b, B:382:0x08b3, B:391:0x08f2, B:393:0x08fa, B:395:0x08fe, B:397:0x0906, B:401:0x090f, B:419:0x09a9, B:423:0x09b8, B:440:0x0a1d, B:442:0x0a23, B:444:0x0a27, B:446:0x0a32, B:448:0x0a38, B:450:0x0a43, B:452:0x0a52, B:454:0x0a5e, B:456:0x0a7e, B:458:0x0a88, B:460:0x0ab9, B:461:0x0acd, B:465:0x0af1, B:467:0x0af7, B:469:0x0aff, B:471:0x0b05, B:473:0x0b17, B:474:0x0b2e, B:475:0x0b44, B:426:0x09c9, B:434:0x09ec, B:436:0x09ff, B:402:0x0939, B:403:0x093e, B:404:0x0941, B:406:0x0947, B:409:0x0951, B:411:0x0959, B:415:0x0997, B:416:0x099f, B:385:0x08bd, B:387:0x08c5, B:389:0x08ed, B:439:0x0a0a, B:365:0x0880, B:369:0x088d, B:372:0x0896, B:375:0x08a0, B:299:0x0724, B:301:0x0728, B:303:0x072c, B:305:0x0734, B:311:0x0743, B:313:0x074e, B:315:0x0758, B:201:0x054e, B:203:0x0579, B:205:0x0585, B:207:0x059a, B:206:0x0592, B:210:0x05c1, B:212:0x05cb, B:214:0x05e0, B:213:0x05d8, B:188:0x04ee, B:167:0x0426, B:169:0x042a, B:113:0x027b, B:115:0x0280, B:116:0x0294, B:119:0x02c1, B:121:0x02e5, B:123:0x02fd, B:128:0x0307, B:129:0x030d, B:133:0x031a, B:134:0x032e, B:136:0x0333, B:137:0x0347, B:138:0x035a, B:140:0x0362, B:141:0x036b, B:99:0x01cc, B:102:0x01d9, B:103:0x01f8, B:91:0x0192, B:81:0x0155, B:82:0x015e, B:83:0x0167, B:61:0x0116, B:62:0x0119, B:37:0x00c3, B:39:0x00c9, B:22:0x007a, B:258:0x067d, B:326:0x07e5, B:413:0x0963), top: B:488:0x0022, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:236:0x0646  */
    /* JADX WARN: Removed duplicated region for block: B:242:0x0651 A[Catch: Exception -> 0x0b62, TryCatch #4 {Exception -> 0x0b62, blocks: (B:11:0x0022, B:13:0x0046, B:15:0x004a, B:17:0x0062, B:19:0x0068, B:23:0x007c, B:27:0x008a, B:29:0x0096, B:30:0x009c, B:32:0x00ac, B:34:0x00ba, B:36:0x00c0, B:45:0x00db, B:47:0x00e7, B:56:0x0107, B:58:0x010d, B:63:0x011d, B:65:0x0125, B:70:0x012d, B:72:0x0133, B:85:0x0172, B:88:0x017d, B:90:0x0185, B:93:0x01b2, B:95:0x01bd, B:104:0x0234, B:107:0x024a, B:112:0x0267, B:118:0x02a9, B:144:0x037b, B:156:0x0396, B:158:0x03b2, B:161:0x03e9, B:163:0x03f3, B:164:0x0406, B:166:0x041a, B:175:0x0449, B:179:0x046d, B:183:0x047c, B:185:0x04a4, B:187:0x04dc, B:189:0x0505, B:191:0x0513, B:198:0x0537, B:200:0x0541, B:202:0x0556, B:221:0x061c, B:223:0x0622, B:232:0x0639, B:234:0x063f, B:242:0x0651, B:245:0x065b, B:248:0x0664, B:265:0x068c, B:268:0x0695, B:270:0x06c8, B:274:0x06d7, B:277:0x06e1, B:278:0x06e9, B:280:0x06ef, B:283:0x06f4, B:285:0x06fd, B:288:0x0705, B:290:0x070b, B:292:0x070f, B:294:0x0717, B:317:0x0762, B:320:0x07c2, B:322:0x07c6, B:324:0x07cc, B:332:0x080e, B:334:0x081b, B:355:0x086b, B:382:0x08b3, B:391:0x08f2, B:393:0x08fa, B:395:0x08fe, B:397:0x0906, B:401:0x090f, B:419:0x09a9, B:423:0x09b8, B:440:0x0a1d, B:442:0x0a23, B:444:0x0a27, B:446:0x0a32, B:448:0x0a38, B:450:0x0a43, B:452:0x0a52, B:454:0x0a5e, B:456:0x0a7e, B:458:0x0a88, B:460:0x0ab9, B:461:0x0acd, B:465:0x0af1, B:467:0x0af7, B:469:0x0aff, B:471:0x0b05, B:473:0x0b17, B:474:0x0b2e, B:475:0x0b44, B:426:0x09c9, B:434:0x09ec, B:436:0x09ff, B:402:0x0939, B:403:0x093e, B:404:0x0941, B:406:0x0947, B:409:0x0951, B:411:0x0959, B:415:0x0997, B:416:0x099f, B:385:0x08bd, B:387:0x08c5, B:389:0x08ed, B:439:0x0a0a, B:365:0x0880, B:369:0x088d, B:372:0x0896, B:375:0x08a0, B:299:0x0724, B:301:0x0728, B:303:0x072c, B:305:0x0734, B:311:0x0743, B:313:0x074e, B:315:0x0758, B:201:0x054e, B:203:0x0579, B:205:0x0585, B:207:0x059a, B:206:0x0592, B:210:0x05c1, B:212:0x05cb, B:214:0x05e0, B:213:0x05d8, B:188:0x04ee, B:167:0x0426, B:169:0x042a, B:113:0x027b, B:115:0x0280, B:116:0x0294, B:119:0x02c1, B:121:0x02e5, B:123:0x02fd, B:128:0x0307, B:129:0x030d, B:133:0x031a, B:134:0x032e, B:136:0x0333, B:137:0x0347, B:138:0x035a, B:140:0x0362, B:141:0x036b, B:99:0x01cc, B:102:0x01d9, B:103:0x01f8, B:91:0x0192, B:81:0x0155, B:82:0x015e, B:83:0x0167, B:61:0x0116, B:62:0x0119, B:37:0x00c3, B:39:0x00c9, B:22:0x007a, B:258:0x067d, B:326:0x07e5, B:413:0x0963), top: B:488:0x0022, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:256:0x067a  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x0691  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x06c8 A[Catch: Exception -> 0x0b62, TryCatch #4 {Exception -> 0x0b62, blocks: (B:11:0x0022, B:13:0x0046, B:15:0x004a, B:17:0x0062, B:19:0x0068, B:23:0x007c, B:27:0x008a, B:29:0x0096, B:30:0x009c, B:32:0x00ac, B:34:0x00ba, B:36:0x00c0, B:45:0x00db, B:47:0x00e7, B:56:0x0107, B:58:0x010d, B:63:0x011d, B:65:0x0125, B:70:0x012d, B:72:0x0133, B:85:0x0172, B:88:0x017d, B:90:0x0185, B:93:0x01b2, B:95:0x01bd, B:104:0x0234, B:107:0x024a, B:112:0x0267, B:118:0x02a9, B:144:0x037b, B:156:0x0396, B:158:0x03b2, B:161:0x03e9, B:163:0x03f3, B:164:0x0406, B:166:0x041a, B:175:0x0449, B:179:0x046d, B:183:0x047c, B:185:0x04a4, B:187:0x04dc, B:189:0x0505, B:191:0x0513, B:198:0x0537, B:200:0x0541, B:202:0x0556, B:221:0x061c, B:223:0x0622, B:232:0x0639, B:234:0x063f, B:242:0x0651, B:245:0x065b, B:248:0x0664, B:265:0x068c, B:268:0x0695, B:270:0x06c8, B:274:0x06d7, B:277:0x06e1, B:278:0x06e9, B:280:0x06ef, B:283:0x06f4, B:285:0x06fd, B:288:0x0705, B:290:0x070b, B:292:0x070f, B:294:0x0717, B:317:0x0762, B:320:0x07c2, B:322:0x07c6, B:324:0x07cc, B:332:0x080e, B:334:0x081b, B:355:0x086b, B:382:0x08b3, B:391:0x08f2, B:393:0x08fa, B:395:0x08fe, B:397:0x0906, B:401:0x090f, B:419:0x09a9, B:423:0x09b8, B:440:0x0a1d, B:442:0x0a23, B:444:0x0a27, B:446:0x0a32, B:448:0x0a38, B:450:0x0a43, B:452:0x0a52, B:454:0x0a5e, B:456:0x0a7e, B:458:0x0a88, B:460:0x0ab9, B:461:0x0acd, B:465:0x0af1, B:467:0x0af7, B:469:0x0aff, B:471:0x0b05, B:473:0x0b17, B:474:0x0b2e, B:475:0x0b44, B:426:0x09c9, B:434:0x09ec, B:436:0x09ff, B:402:0x0939, B:403:0x093e, B:404:0x0941, B:406:0x0947, B:409:0x0951, B:411:0x0959, B:415:0x0997, B:416:0x099f, B:385:0x08bd, B:387:0x08c5, B:389:0x08ed, B:439:0x0a0a, B:365:0x0880, B:369:0x088d, B:372:0x0896, B:375:0x08a0, B:299:0x0724, B:301:0x0728, B:303:0x072c, B:305:0x0734, B:311:0x0743, B:313:0x074e, B:315:0x0758, B:201:0x054e, B:203:0x0579, B:205:0x0585, B:207:0x059a, B:206:0x0592, B:210:0x05c1, B:212:0x05cb, B:214:0x05e0, B:213:0x05d8, B:188:0x04ee, B:167:0x0426, B:169:0x042a, B:113:0x027b, B:115:0x0280, B:116:0x0294, B:119:0x02c1, B:121:0x02e5, B:123:0x02fd, B:128:0x0307, B:129:0x030d, B:133:0x031a, B:134:0x032e, B:136:0x0333, B:137:0x0347, B:138:0x035a, B:140:0x0362, B:141:0x036b, B:99:0x01cc, B:102:0x01d9, B:103:0x01f8, B:91:0x0192, B:81:0x0155, B:82:0x015e, B:83:0x0167, B:61:0x0116, B:62:0x0119, B:37:0x00c3, B:39:0x00c9, B:22:0x007a, B:258:0x067d, B:326:0x07e5, B:413:0x0963), top: B:488:0x0022, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:311:0x0743 A[Catch: Exception -> 0x0b62, TryCatch #4 {Exception -> 0x0b62, blocks: (B:11:0x0022, B:13:0x0046, B:15:0x004a, B:17:0x0062, B:19:0x0068, B:23:0x007c, B:27:0x008a, B:29:0x0096, B:30:0x009c, B:32:0x00ac, B:34:0x00ba, B:36:0x00c0, B:45:0x00db, B:47:0x00e7, B:56:0x0107, B:58:0x010d, B:63:0x011d, B:65:0x0125, B:70:0x012d, B:72:0x0133, B:85:0x0172, B:88:0x017d, B:90:0x0185, B:93:0x01b2, B:95:0x01bd, B:104:0x0234, B:107:0x024a, B:112:0x0267, B:118:0x02a9, B:144:0x037b, B:156:0x0396, B:158:0x03b2, B:161:0x03e9, B:163:0x03f3, B:164:0x0406, B:166:0x041a, B:175:0x0449, B:179:0x046d, B:183:0x047c, B:185:0x04a4, B:187:0x04dc, B:189:0x0505, B:191:0x0513, B:198:0x0537, B:200:0x0541, B:202:0x0556, B:221:0x061c, B:223:0x0622, B:232:0x0639, B:234:0x063f, B:242:0x0651, B:245:0x065b, B:248:0x0664, B:265:0x068c, B:268:0x0695, B:270:0x06c8, B:274:0x06d7, B:277:0x06e1, B:278:0x06e9, B:280:0x06ef, B:283:0x06f4, B:285:0x06fd, B:288:0x0705, B:290:0x070b, B:292:0x070f, B:294:0x0717, B:317:0x0762, B:320:0x07c2, B:322:0x07c6, B:324:0x07cc, B:332:0x080e, B:334:0x081b, B:355:0x086b, B:382:0x08b3, B:391:0x08f2, B:393:0x08fa, B:395:0x08fe, B:397:0x0906, B:401:0x090f, B:419:0x09a9, B:423:0x09b8, B:440:0x0a1d, B:442:0x0a23, B:444:0x0a27, B:446:0x0a32, B:448:0x0a38, B:450:0x0a43, B:452:0x0a52, B:454:0x0a5e, B:456:0x0a7e, B:458:0x0a88, B:460:0x0ab9, B:461:0x0acd, B:465:0x0af1, B:467:0x0af7, B:469:0x0aff, B:471:0x0b05, B:473:0x0b17, B:474:0x0b2e, B:475:0x0b44, B:426:0x09c9, B:434:0x09ec, B:436:0x09ff, B:402:0x0939, B:403:0x093e, B:404:0x0941, B:406:0x0947, B:409:0x0951, B:411:0x0959, B:415:0x0997, B:416:0x099f, B:385:0x08bd, B:387:0x08c5, B:389:0x08ed, B:439:0x0a0a, B:365:0x0880, B:369:0x088d, B:372:0x0896, B:375:0x08a0, B:299:0x0724, B:301:0x0728, B:303:0x072c, B:305:0x0734, B:311:0x0743, B:313:0x074e, B:315:0x0758, B:201:0x054e, B:203:0x0579, B:205:0x0585, B:207:0x059a, B:206:0x0592, B:210:0x05c1, B:212:0x05cb, B:214:0x05e0, B:213:0x05d8, B:188:0x04ee, B:167:0x0426, B:169:0x042a, B:113:0x027b, B:115:0x0280, B:116:0x0294, B:119:0x02c1, B:121:0x02e5, B:123:0x02fd, B:128:0x0307, B:129:0x030d, B:133:0x031a, B:134:0x032e, B:136:0x0333, B:137:0x0347, B:138:0x035a, B:140:0x0362, B:141:0x036b, B:99:0x01cc, B:102:0x01d9, B:103:0x01f8, B:91:0x0192, B:81:0x0155, B:82:0x015e, B:83:0x0167, B:61:0x0116, B:62:0x0119, B:37:0x00c3, B:39:0x00c9, B:22:0x007a, B:258:0x067d, B:326:0x07e5, B:413:0x0963), top: B:488:0x0022, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:319:0x07c0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:332:0x080e A[Catch: Exception -> 0x0b62, TryCatch #4 {Exception -> 0x0b62, blocks: (B:11:0x0022, B:13:0x0046, B:15:0x004a, B:17:0x0062, B:19:0x0068, B:23:0x007c, B:27:0x008a, B:29:0x0096, B:30:0x009c, B:32:0x00ac, B:34:0x00ba, B:36:0x00c0, B:45:0x00db, B:47:0x00e7, B:56:0x0107, B:58:0x010d, B:63:0x011d, B:65:0x0125, B:70:0x012d, B:72:0x0133, B:85:0x0172, B:88:0x017d, B:90:0x0185, B:93:0x01b2, B:95:0x01bd, B:104:0x0234, B:107:0x024a, B:112:0x0267, B:118:0x02a9, B:144:0x037b, B:156:0x0396, B:158:0x03b2, B:161:0x03e9, B:163:0x03f3, B:164:0x0406, B:166:0x041a, B:175:0x0449, B:179:0x046d, B:183:0x047c, B:185:0x04a4, B:187:0x04dc, B:189:0x0505, B:191:0x0513, B:198:0x0537, B:200:0x0541, B:202:0x0556, B:221:0x061c, B:223:0x0622, B:232:0x0639, B:234:0x063f, B:242:0x0651, B:245:0x065b, B:248:0x0664, B:265:0x068c, B:268:0x0695, B:270:0x06c8, B:274:0x06d7, B:277:0x06e1, B:278:0x06e9, B:280:0x06ef, B:283:0x06f4, B:285:0x06fd, B:288:0x0705, B:290:0x070b, B:292:0x070f, B:294:0x0717, B:317:0x0762, B:320:0x07c2, B:322:0x07c6, B:324:0x07cc, B:332:0x080e, B:334:0x081b, B:355:0x086b, B:382:0x08b3, B:391:0x08f2, B:393:0x08fa, B:395:0x08fe, B:397:0x0906, B:401:0x090f, B:419:0x09a9, B:423:0x09b8, B:440:0x0a1d, B:442:0x0a23, B:444:0x0a27, B:446:0x0a32, B:448:0x0a38, B:450:0x0a43, B:452:0x0a52, B:454:0x0a5e, B:456:0x0a7e, B:458:0x0a88, B:460:0x0ab9, B:461:0x0acd, B:465:0x0af1, B:467:0x0af7, B:469:0x0aff, B:471:0x0b05, B:473:0x0b17, B:474:0x0b2e, B:475:0x0b44, B:426:0x09c9, B:434:0x09ec, B:436:0x09ff, B:402:0x0939, B:403:0x093e, B:404:0x0941, B:406:0x0947, B:409:0x0951, B:411:0x0959, B:415:0x0997, B:416:0x099f, B:385:0x08bd, B:387:0x08c5, B:389:0x08ed, B:439:0x0a0a, B:365:0x0880, B:369:0x088d, B:372:0x0896, B:375:0x08a0, B:299:0x0724, B:301:0x0728, B:303:0x072c, B:305:0x0734, B:311:0x0743, B:313:0x074e, B:315:0x0758, B:201:0x054e, B:203:0x0579, B:205:0x0585, B:207:0x059a, B:206:0x0592, B:210:0x05c1, B:212:0x05cb, B:214:0x05e0, B:213:0x05d8, B:188:0x04ee, B:167:0x0426, B:169:0x042a, B:113:0x027b, B:115:0x0280, B:116:0x0294, B:119:0x02c1, B:121:0x02e5, B:123:0x02fd, B:128:0x0307, B:129:0x030d, B:133:0x031a, B:134:0x032e, B:136:0x0333, B:137:0x0347, B:138:0x035a, B:140:0x0362, B:141:0x036b, B:99:0x01cc, B:102:0x01d9, B:103:0x01f8, B:91:0x0192, B:81:0x0155, B:82:0x015e, B:83:0x0167, B:61:0x0116, B:62:0x0119, B:37:0x00c3, B:39:0x00c9, B:22:0x007a, B:258:0x067d, B:326:0x07e5, B:413:0x0963), top: B:488:0x0022, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:347:0x085c  */
    /* JADX WARN: Removed duplicated region for block: B:350:0x0864  */
    /* JADX WARN: Removed duplicated region for block: B:419:0x09a9 A[Catch: Exception -> 0x0b62, TryCatch #4 {Exception -> 0x0b62, blocks: (B:11:0x0022, B:13:0x0046, B:15:0x004a, B:17:0x0062, B:19:0x0068, B:23:0x007c, B:27:0x008a, B:29:0x0096, B:30:0x009c, B:32:0x00ac, B:34:0x00ba, B:36:0x00c0, B:45:0x00db, B:47:0x00e7, B:56:0x0107, B:58:0x010d, B:63:0x011d, B:65:0x0125, B:70:0x012d, B:72:0x0133, B:85:0x0172, B:88:0x017d, B:90:0x0185, B:93:0x01b2, B:95:0x01bd, B:104:0x0234, B:107:0x024a, B:112:0x0267, B:118:0x02a9, B:144:0x037b, B:156:0x0396, B:158:0x03b2, B:161:0x03e9, B:163:0x03f3, B:164:0x0406, B:166:0x041a, B:175:0x0449, B:179:0x046d, B:183:0x047c, B:185:0x04a4, B:187:0x04dc, B:189:0x0505, B:191:0x0513, B:198:0x0537, B:200:0x0541, B:202:0x0556, B:221:0x061c, B:223:0x0622, B:232:0x0639, B:234:0x063f, B:242:0x0651, B:245:0x065b, B:248:0x0664, B:265:0x068c, B:268:0x0695, B:270:0x06c8, B:274:0x06d7, B:277:0x06e1, B:278:0x06e9, B:280:0x06ef, B:283:0x06f4, B:285:0x06fd, B:288:0x0705, B:290:0x070b, B:292:0x070f, B:294:0x0717, B:317:0x0762, B:320:0x07c2, B:322:0x07c6, B:324:0x07cc, B:332:0x080e, B:334:0x081b, B:355:0x086b, B:382:0x08b3, B:391:0x08f2, B:393:0x08fa, B:395:0x08fe, B:397:0x0906, B:401:0x090f, B:419:0x09a9, B:423:0x09b8, B:440:0x0a1d, B:442:0x0a23, B:444:0x0a27, B:446:0x0a32, B:448:0x0a38, B:450:0x0a43, B:452:0x0a52, B:454:0x0a5e, B:456:0x0a7e, B:458:0x0a88, B:460:0x0ab9, B:461:0x0acd, B:465:0x0af1, B:467:0x0af7, B:469:0x0aff, B:471:0x0b05, B:473:0x0b17, B:474:0x0b2e, B:475:0x0b44, B:426:0x09c9, B:434:0x09ec, B:436:0x09ff, B:402:0x0939, B:403:0x093e, B:404:0x0941, B:406:0x0947, B:409:0x0951, B:411:0x0959, B:415:0x0997, B:416:0x099f, B:385:0x08bd, B:387:0x08c5, B:389:0x08ed, B:439:0x0a0a, B:365:0x0880, B:369:0x088d, B:372:0x0896, B:375:0x08a0, B:299:0x0724, B:301:0x0728, B:303:0x072c, B:305:0x0734, B:311:0x0743, B:313:0x074e, B:315:0x0758, B:201:0x054e, B:203:0x0579, B:205:0x0585, B:207:0x059a, B:206:0x0592, B:210:0x05c1, B:212:0x05cb, B:214:0x05e0, B:213:0x05d8, B:188:0x04ee, B:167:0x0426, B:169:0x042a, B:113:0x027b, B:115:0x0280, B:116:0x0294, B:119:0x02c1, B:121:0x02e5, B:123:0x02fd, B:128:0x0307, B:129:0x030d, B:133:0x031a, B:134:0x032e, B:136:0x0333, B:137:0x0347, B:138:0x035a, B:140:0x0362, B:141:0x036b, B:99:0x01cc, B:102:0x01d9, B:103:0x01f8, B:91:0x0192, B:81:0x0155, B:82:0x015e, B:83:0x0167, B:61:0x0116, B:62:0x0119, B:37:0x00c3, B:39:0x00c9, B:22:0x007a, B:258:0x067d, B:326:0x07e5, B:413:0x0963), top: B:488:0x0022, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:420:0x09b3  */
    /* JADX WARN: Removed duplicated region for block: B:423:0x09b8 A[Catch: Exception -> 0x0b62, TryCatch #4 {Exception -> 0x0b62, blocks: (B:11:0x0022, B:13:0x0046, B:15:0x004a, B:17:0x0062, B:19:0x0068, B:23:0x007c, B:27:0x008a, B:29:0x0096, B:30:0x009c, B:32:0x00ac, B:34:0x00ba, B:36:0x00c0, B:45:0x00db, B:47:0x00e7, B:56:0x0107, B:58:0x010d, B:63:0x011d, B:65:0x0125, B:70:0x012d, B:72:0x0133, B:85:0x0172, B:88:0x017d, B:90:0x0185, B:93:0x01b2, B:95:0x01bd, B:104:0x0234, B:107:0x024a, B:112:0x0267, B:118:0x02a9, B:144:0x037b, B:156:0x0396, B:158:0x03b2, B:161:0x03e9, B:163:0x03f3, B:164:0x0406, B:166:0x041a, B:175:0x0449, B:179:0x046d, B:183:0x047c, B:185:0x04a4, B:187:0x04dc, B:189:0x0505, B:191:0x0513, B:198:0x0537, B:200:0x0541, B:202:0x0556, B:221:0x061c, B:223:0x0622, B:232:0x0639, B:234:0x063f, B:242:0x0651, B:245:0x065b, B:248:0x0664, B:265:0x068c, B:268:0x0695, B:270:0x06c8, B:274:0x06d7, B:277:0x06e1, B:278:0x06e9, B:280:0x06ef, B:283:0x06f4, B:285:0x06fd, B:288:0x0705, B:290:0x070b, B:292:0x070f, B:294:0x0717, B:317:0x0762, B:320:0x07c2, B:322:0x07c6, B:324:0x07cc, B:332:0x080e, B:334:0x081b, B:355:0x086b, B:382:0x08b3, B:391:0x08f2, B:393:0x08fa, B:395:0x08fe, B:397:0x0906, B:401:0x090f, B:419:0x09a9, B:423:0x09b8, B:440:0x0a1d, B:442:0x0a23, B:444:0x0a27, B:446:0x0a32, B:448:0x0a38, B:450:0x0a43, B:452:0x0a52, B:454:0x0a5e, B:456:0x0a7e, B:458:0x0a88, B:460:0x0ab9, B:461:0x0acd, B:465:0x0af1, B:467:0x0af7, B:469:0x0aff, B:471:0x0b05, B:473:0x0b17, B:474:0x0b2e, B:475:0x0b44, B:426:0x09c9, B:434:0x09ec, B:436:0x09ff, B:402:0x0939, B:403:0x093e, B:404:0x0941, B:406:0x0947, B:409:0x0951, B:411:0x0959, B:415:0x0997, B:416:0x099f, B:385:0x08bd, B:387:0x08c5, B:389:0x08ed, B:439:0x0a0a, B:365:0x0880, B:369:0x088d, B:372:0x0896, B:375:0x08a0, B:299:0x0724, B:301:0x0728, B:303:0x072c, B:305:0x0734, B:311:0x0743, B:313:0x074e, B:315:0x0758, B:201:0x054e, B:203:0x0579, B:205:0x0585, B:207:0x059a, B:206:0x0592, B:210:0x05c1, B:212:0x05cb, B:214:0x05e0, B:213:0x05d8, B:188:0x04ee, B:167:0x0426, B:169:0x042a, B:113:0x027b, B:115:0x0280, B:116:0x0294, B:119:0x02c1, B:121:0x02e5, B:123:0x02fd, B:128:0x0307, B:129:0x030d, B:133:0x031a, B:134:0x032e, B:136:0x0333, B:137:0x0347, B:138:0x035a, B:140:0x0362, B:141:0x036b, B:99:0x01cc, B:102:0x01d9, B:103:0x01f8, B:91:0x0192, B:81:0x0155, B:82:0x015e, B:83:0x0167, B:61:0x0116, B:62:0x0119, B:37:0x00c3, B:39:0x00c9, B:22:0x007a, B:258:0x067d, B:326:0x07e5, B:413:0x0963), top: B:488:0x0022, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:424:0x09c6  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0133 A[Catch: Exception -> 0x0b62, TryCatch #4 {Exception -> 0x0b62, blocks: (B:11:0x0022, B:13:0x0046, B:15:0x004a, B:17:0x0062, B:19:0x0068, B:23:0x007c, B:27:0x008a, B:29:0x0096, B:30:0x009c, B:32:0x00ac, B:34:0x00ba, B:36:0x00c0, B:45:0x00db, B:47:0x00e7, B:56:0x0107, B:58:0x010d, B:63:0x011d, B:65:0x0125, B:70:0x012d, B:72:0x0133, B:85:0x0172, B:88:0x017d, B:90:0x0185, B:93:0x01b2, B:95:0x01bd, B:104:0x0234, B:107:0x024a, B:112:0x0267, B:118:0x02a9, B:144:0x037b, B:156:0x0396, B:158:0x03b2, B:161:0x03e9, B:163:0x03f3, B:164:0x0406, B:166:0x041a, B:175:0x0449, B:179:0x046d, B:183:0x047c, B:185:0x04a4, B:187:0x04dc, B:189:0x0505, B:191:0x0513, B:198:0x0537, B:200:0x0541, B:202:0x0556, B:221:0x061c, B:223:0x0622, B:232:0x0639, B:234:0x063f, B:242:0x0651, B:245:0x065b, B:248:0x0664, B:265:0x068c, B:268:0x0695, B:270:0x06c8, B:274:0x06d7, B:277:0x06e1, B:278:0x06e9, B:280:0x06ef, B:283:0x06f4, B:285:0x06fd, B:288:0x0705, B:290:0x070b, B:292:0x070f, B:294:0x0717, B:317:0x0762, B:320:0x07c2, B:322:0x07c6, B:324:0x07cc, B:332:0x080e, B:334:0x081b, B:355:0x086b, B:382:0x08b3, B:391:0x08f2, B:393:0x08fa, B:395:0x08fe, B:397:0x0906, B:401:0x090f, B:419:0x09a9, B:423:0x09b8, B:440:0x0a1d, B:442:0x0a23, B:444:0x0a27, B:446:0x0a32, B:448:0x0a38, B:450:0x0a43, B:452:0x0a52, B:454:0x0a5e, B:456:0x0a7e, B:458:0x0a88, B:460:0x0ab9, B:461:0x0acd, B:465:0x0af1, B:467:0x0af7, B:469:0x0aff, B:471:0x0b05, B:473:0x0b17, B:474:0x0b2e, B:475:0x0b44, B:426:0x09c9, B:434:0x09ec, B:436:0x09ff, B:402:0x0939, B:403:0x093e, B:404:0x0941, B:406:0x0947, B:409:0x0951, B:411:0x0959, B:415:0x0997, B:416:0x099f, B:385:0x08bd, B:387:0x08c5, B:389:0x08ed, B:439:0x0a0a, B:365:0x0880, B:369:0x088d, B:372:0x0896, B:375:0x08a0, B:299:0x0724, B:301:0x0728, B:303:0x072c, B:305:0x0734, B:311:0x0743, B:313:0x074e, B:315:0x0758, B:201:0x054e, B:203:0x0579, B:205:0x0585, B:207:0x059a, B:206:0x0592, B:210:0x05c1, B:212:0x05cb, B:214:0x05e0, B:213:0x05d8, B:188:0x04ee, B:167:0x0426, B:169:0x042a, B:113:0x027b, B:115:0x0280, B:116:0x0294, B:119:0x02c1, B:121:0x02e5, B:123:0x02fd, B:128:0x0307, B:129:0x030d, B:133:0x031a, B:134:0x032e, B:136:0x0333, B:137:0x0347, B:138:0x035a, B:140:0x0362, B:141:0x036b, B:99:0x01cc, B:102:0x01d9, B:103:0x01f8, B:91:0x0192, B:81:0x0155, B:82:0x015e, B:83:0x0167, B:61:0x0116, B:62:0x0119, B:37:0x00c3, B:39:0x00c9, B:22:0x007a, B:258:0x067d, B:326:0x07e5, B:413:0x0963), top: B:488:0x0022, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0149  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x017d A[Catch: Exception -> 0x0b62, TRY_ENTER, TryCatch #4 {Exception -> 0x0b62, blocks: (B:11:0x0022, B:13:0x0046, B:15:0x004a, B:17:0x0062, B:19:0x0068, B:23:0x007c, B:27:0x008a, B:29:0x0096, B:30:0x009c, B:32:0x00ac, B:34:0x00ba, B:36:0x00c0, B:45:0x00db, B:47:0x00e7, B:56:0x0107, B:58:0x010d, B:63:0x011d, B:65:0x0125, B:70:0x012d, B:72:0x0133, B:85:0x0172, B:88:0x017d, B:90:0x0185, B:93:0x01b2, B:95:0x01bd, B:104:0x0234, B:107:0x024a, B:112:0x0267, B:118:0x02a9, B:144:0x037b, B:156:0x0396, B:158:0x03b2, B:161:0x03e9, B:163:0x03f3, B:164:0x0406, B:166:0x041a, B:175:0x0449, B:179:0x046d, B:183:0x047c, B:185:0x04a4, B:187:0x04dc, B:189:0x0505, B:191:0x0513, B:198:0x0537, B:200:0x0541, B:202:0x0556, B:221:0x061c, B:223:0x0622, B:232:0x0639, B:234:0x063f, B:242:0x0651, B:245:0x065b, B:248:0x0664, B:265:0x068c, B:268:0x0695, B:270:0x06c8, B:274:0x06d7, B:277:0x06e1, B:278:0x06e9, B:280:0x06ef, B:283:0x06f4, B:285:0x06fd, B:288:0x0705, B:290:0x070b, B:292:0x070f, B:294:0x0717, B:317:0x0762, B:320:0x07c2, B:322:0x07c6, B:324:0x07cc, B:332:0x080e, B:334:0x081b, B:355:0x086b, B:382:0x08b3, B:391:0x08f2, B:393:0x08fa, B:395:0x08fe, B:397:0x0906, B:401:0x090f, B:419:0x09a9, B:423:0x09b8, B:440:0x0a1d, B:442:0x0a23, B:444:0x0a27, B:446:0x0a32, B:448:0x0a38, B:450:0x0a43, B:452:0x0a52, B:454:0x0a5e, B:456:0x0a7e, B:458:0x0a88, B:460:0x0ab9, B:461:0x0acd, B:465:0x0af1, B:467:0x0af7, B:469:0x0aff, B:471:0x0b05, B:473:0x0b17, B:474:0x0b2e, B:475:0x0b44, B:426:0x09c9, B:434:0x09ec, B:436:0x09ff, B:402:0x0939, B:403:0x093e, B:404:0x0941, B:406:0x0947, B:409:0x0951, B:411:0x0959, B:415:0x0997, B:416:0x099f, B:385:0x08bd, B:387:0x08c5, B:389:0x08ed, B:439:0x0a0a, B:365:0x0880, B:369:0x088d, B:372:0x0896, B:375:0x08a0, B:299:0x0724, B:301:0x0728, B:303:0x072c, B:305:0x0734, B:311:0x0743, B:313:0x074e, B:315:0x0758, B:201:0x054e, B:203:0x0579, B:205:0x0585, B:207:0x059a, B:206:0x0592, B:210:0x05c1, B:212:0x05cb, B:214:0x05e0, B:213:0x05d8, B:188:0x04ee, B:167:0x0426, B:169:0x042a, B:113:0x027b, B:115:0x0280, B:116:0x0294, B:119:0x02c1, B:121:0x02e5, B:123:0x02fd, B:128:0x0307, B:129:0x030d, B:133:0x031a, B:134:0x032e, B:136:0x0333, B:137:0x0347, B:138:0x035a, B:140:0x0362, B:141:0x036b, B:99:0x01cc, B:102:0x01d9, B:103:0x01f8, B:91:0x0192, B:81:0x0155, B:82:0x015e, B:83:0x0167, B:61:0x0116, B:62:0x0119, B:37:0x00c3, B:39:0x00c9, B:22:0x007a, B:258:0x067d, B:326:0x07e5, B:413:0x0963), top: B:488:0x0022, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01b1  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x01bd A[Catch: Exception -> 0x0b62, TryCatch #4 {Exception -> 0x0b62, blocks: (B:11:0x0022, B:13:0x0046, B:15:0x004a, B:17:0x0062, B:19:0x0068, B:23:0x007c, B:27:0x008a, B:29:0x0096, B:30:0x009c, B:32:0x00ac, B:34:0x00ba, B:36:0x00c0, B:45:0x00db, B:47:0x00e7, B:56:0x0107, B:58:0x010d, B:63:0x011d, B:65:0x0125, B:70:0x012d, B:72:0x0133, B:85:0x0172, B:88:0x017d, B:90:0x0185, B:93:0x01b2, B:95:0x01bd, B:104:0x0234, B:107:0x024a, B:112:0x0267, B:118:0x02a9, B:144:0x037b, B:156:0x0396, B:158:0x03b2, B:161:0x03e9, B:163:0x03f3, B:164:0x0406, B:166:0x041a, B:175:0x0449, B:179:0x046d, B:183:0x047c, B:185:0x04a4, B:187:0x04dc, B:189:0x0505, B:191:0x0513, B:198:0x0537, B:200:0x0541, B:202:0x0556, B:221:0x061c, B:223:0x0622, B:232:0x0639, B:234:0x063f, B:242:0x0651, B:245:0x065b, B:248:0x0664, B:265:0x068c, B:268:0x0695, B:270:0x06c8, B:274:0x06d7, B:277:0x06e1, B:278:0x06e9, B:280:0x06ef, B:283:0x06f4, B:285:0x06fd, B:288:0x0705, B:290:0x070b, B:292:0x070f, B:294:0x0717, B:317:0x0762, B:320:0x07c2, B:322:0x07c6, B:324:0x07cc, B:332:0x080e, B:334:0x081b, B:355:0x086b, B:382:0x08b3, B:391:0x08f2, B:393:0x08fa, B:395:0x08fe, B:397:0x0906, B:401:0x090f, B:419:0x09a9, B:423:0x09b8, B:440:0x0a1d, B:442:0x0a23, B:444:0x0a27, B:446:0x0a32, B:448:0x0a38, B:450:0x0a43, B:452:0x0a52, B:454:0x0a5e, B:456:0x0a7e, B:458:0x0a88, B:460:0x0ab9, B:461:0x0acd, B:465:0x0af1, B:467:0x0af7, B:469:0x0aff, B:471:0x0b05, B:473:0x0b17, B:474:0x0b2e, B:475:0x0b44, B:426:0x09c9, B:434:0x09ec, B:436:0x09ff, B:402:0x0939, B:403:0x093e, B:404:0x0941, B:406:0x0947, B:409:0x0951, B:411:0x0959, B:415:0x0997, B:416:0x099f, B:385:0x08bd, B:387:0x08c5, B:389:0x08ed, B:439:0x0a0a, B:365:0x0880, B:369:0x088d, B:372:0x0896, B:375:0x08a0, B:299:0x0724, B:301:0x0728, B:303:0x072c, B:305:0x0734, B:311:0x0743, B:313:0x074e, B:315:0x0758, B:201:0x054e, B:203:0x0579, B:205:0x0585, B:207:0x059a, B:206:0x0592, B:210:0x05c1, B:212:0x05cb, B:214:0x05e0, B:213:0x05d8, B:188:0x04ee, B:167:0x0426, B:169:0x042a, B:113:0x027b, B:115:0x0280, B:116:0x0294, B:119:0x02c1, B:121:0x02e5, B:123:0x02fd, B:128:0x0307, B:129:0x030d, B:133:0x031a, B:134:0x032e, B:136:0x0333, B:137:0x0347, B:138:0x035a, B:140:0x0362, B:141:0x036b, B:99:0x01cc, B:102:0x01d9, B:103:0x01f8, B:91:0x0192, B:81:0x0155, B:82:0x015e, B:83:0x0167, B:61:0x0116, B:62:0x0119, B:37:0x00c3, B:39:0x00c9, B:22:0x007a, B:258:0x067d, B:326:0x07e5, B:413:0x0963), top: B:488:0x0022, inners: #0, #2 }] */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v15 */
    /* JADX WARN: Type inference failed for: r2v16 */
    /* JADX WARN: Type inference failed for: r2v17 */
    /* JADX WARN: Type inference failed for: r2v76 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showOrUpdateNotification(boolean z) {
        SharedPreferences sharedPreferences;
        TLRPC$Chat tLRPC$Chat;
        boolean z2;
        MessageObject messageObject;
        long j;
        boolean z3;
        MessageObject messageObject2;
        String userName;
        boolean z4;
        TLRPC$User tLRPC$User;
        MessageObject messageObject3;
        String string;
        boolean z5;
        long j2;
        String str;
        SharedPreferences sharedPreferences2;
        long j3;
        long j4;
        String sb;
        boolean z6;
        String str2;
        NotificationCompat.Builder builder;
        String str3;
        NotificationCompat.InboxStyle inboxStyle;
        boolean z7;
        boolean z8;
        String str4;
        SharedPreferences sharedPreferences3;
        long j5;
        String str5;
        boolean z9;
        int i;
        boolean z10;
        long j6;
        CharSequence charSequence;
        MessageObject messageObject4;
        TLRPC$Chat tLRPC$Chat2;
        SharedPreferences sharedPreferences4;
        long j7;
        String str6;
        int i2;
        Integer num;
        int i3;
        boolean z11;
        int i4;
        int i5;
        String str7;
        int i6;
        int i7;
        int i8;
        boolean z12;
        String string2;
        boolean z13;
        boolean z14;
        int i9;
        boolean z15;
        boolean z16;
        int i10;
        TLRPC$Chat tLRPC$Chat3;
        int i11;
        long j8;
        TLRPC$FileLocation tLRPC$FileLocation;
        boolean z17;
        int i12;
        int i13;
        long[] jArr;
        Uri uri;
        int i14;
        long j9;
        boolean z18;
        int i15;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList;
        int i16;
        MessageObject messageObject5;
        int i17;
        TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow;
        long j10;
        String str8;
        long[] jArr2;
        long[] jArr3;
        String str9;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        int ringerMode;
        String string3;
        boolean z19;
        String string4;
        boolean z20;
        String propertyString;
        int i18;
        int i19;
        String str10;
        boolean z21;
        if (!getUserConfig().isClientActivated() || this.pushMessages.isEmpty() || (!SharedConfig.showNotificationsForAllAccounts && this.currentAccount != UserConfig.selectedAccount)) {
            dismissNotification();
            return;
        }
        try {
            getConnectionsManager().resumeNetworkMaybe();
            MessageObject messageObject6 = this.pushMessages.get(0);
            SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
            int i20 = notificationsSettings.getInt("dismissDate", 0);
            if (messageObject6.messageOwner.date <= i20) {
                dismissNotification();
                return;
            }
            long dialogId = messageObject6.getDialogId();
            int topicId = MessageObject.getTopicId(messageObject6.messageOwner, getMessagesController().isForum(messageObject6));
            long fromChatId = messageObject6.messageOwner.mentioned ? messageObject6.getFromChatId() : dialogId;
            messageObject6.getId();
            TLRPC$Peer tLRPC$Peer = messageObject6.messageOwner.peer_id;
            long j11 = tLRPC$Peer.chat_id;
            if (j11 == 0) {
                j11 = tLRPC$Peer.channel_id;
            }
            long j12 = tLRPC$Peer.user_id;
            if (messageObject6.isFromUser() && (j12 == 0 || j12 == getUserConfig().getClientUserId())) {
                j12 = messageObject6.messageOwner.from_id.user_id;
            }
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(j12));
            if (j11 != 0) {
                TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(j11));
                if (chat == null && messageObject6.isFcmMessage()) {
                    z21 = messageObject6.localChannel;
                } else {
                    z21 = ChatObject.isChannel(chat) && !chat.megagroup;
                }
                sharedPreferences = notificationsSettings;
                z2 = z21;
                tLRPC$Chat = chat;
            } else {
                sharedPreferences = notificationsSettings;
                tLRPC$Chat = null;
                z2 = false;
            }
            int notifyOverride = getNotifyOverride(sharedPreferences, fromChatId, topicId);
            long j13 = j12;
            if (notifyOverride == -1) {
                messageObject = messageObject6;
                j = dialogId;
                z3 = isGlobalNotificationsEnabled(j, Boolean.valueOf(z2));
            } else {
                messageObject = messageObject6;
                j = dialogId;
                z3 = notifyOverride != 2;
            }
            if (((j11 == 0 || tLRPC$Chat != null) && user != null) || !messageObject.isFcmMessage()) {
                messageObject2 = messageObject;
                if (tLRPC$Chat != null) {
                    userName = tLRPC$Chat.title;
                } else {
                    userName = UserObject.getUserName(user);
                }
            } else {
                messageObject2 = messageObject;
                userName = messageObject2.localName;
            }
            String str11 = userName;
            if (!AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
                z4 = false;
                if (DialogObject.isEncryptedDialog(j)) {
                    tLRPC$User = user;
                    messageObject3 = messageObject2;
                    if (this.pushDialogs.size() <= 1 && !z4) {
                        string = str11;
                        z5 = true;
                        j2 = j11;
                        if (UserConfig.getActivatedAccountsCount() <= 1) {
                            str = "";
                        } else if (this.pushDialogs.size() == 1) {
                            str = UserObject.getFirstName(getUserConfig().getCurrentUser());
                        } else {
                            str = UserObject.getFirstName(getUserConfig().getCurrentUser()) + "・";
                        }
                        if (this.pushDialogs.size() == 1 && Build.VERSION.SDK_INT >= 23) {
                            j3 = j;
                            j4 = fromChatId;
                            sb = str;
                            sharedPreferences2 = sharedPreferences;
                            NotificationCompat.Builder builder2 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                            if (this.pushMessages.size() == 1) {
                                MessageObject messageObject7 = this.pushMessages.get(0);
                                boolean[] zArr = new boolean[1];
                                z6 = z3;
                                String stringForMessage = getStringForMessage(messageObject7, false, zArr, null);
                                boolean isSilentMessage = isSilentMessage(messageObject7);
                                if (stringForMessage == null) {
                                    return;
                                }
                                if (!z5) {
                                    str10 = stringForMessage;
                                } else if (tLRPC$Chat != null) {
                                    str10 = stringForMessage.replace(" @ " + string, "");
                                } else if (zArr[0]) {
                                    str10 = stringForMessage.replace(string + ": ", "");
                                } else {
                                    str10 = stringForMessage.replace(string + " ", "");
                                }
                                builder2.setContentText(str10);
                                builder2.setStyle(new NotificationCompat.BigTextStyle().bigText(str10));
                                str2 = sb;
                                str3 = stringForMessage;
                                z7 = isSilentMessage;
                                builder = builder2;
                            } else {
                                z6 = z3;
                                builder2.setContentText(sb);
                                NotificationCompat.InboxStyle inboxStyle2 = new NotificationCompat.InboxStyle();
                                inboxStyle2.setBigContentTitle(string);
                                int min = Math.min(10, this.pushMessages.size());
                                boolean[] zArr2 = new boolean[1];
                                boolean z22 = 2;
                                int i21 = 0;
                                String str12 = null;
                                while (i21 < min) {
                                    int i22 = min;
                                    MessageObject messageObject8 = this.pushMessages.get(i21);
                                    NotificationCompat.InboxStyle inboxStyle3 = inboxStyle2;
                                    String str13 = sb;
                                    int i23 = i21;
                                    String stringForMessage2 = getStringForMessage(messageObject8, false, zArr2, null);
                                    if (stringForMessage2 != null && messageObject8.messageOwner.date > i20) {
                                        z22 = z22;
                                        if (z22 == 2) {
                                            str12 = stringForMessage2;
                                            z22 = isSilentMessage(messageObject8);
                                        }
                                        if (this.pushDialogs.size() == 1 && z5) {
                                            if (tLRPC$Chat != null) {
                                                stringForMessage2 = stringForMessage2.replace(" @ " + string, "");
                                            } else if (zArr2[0]) {
                                                stringForMessage2 = stringForMessage2.replace(string + ": ", "");
                                            } else {
                                                stringForMessage2 = stringForMessage2.replace(string + " ", "");
                                            }
                                        }
                                        inboxStyle = inboxStyle3;
                                        inboxStyle.addLine(stringForMessage2);
                                        i21 = i23 + 1;
                                        inboxStyle2 = inboxStyle;
                                        min = i22;
                                        sb = str13;
                                        z22 = z22;
                                    }
                                    inboxStyle = inboxStyle3;
                                    i21 = i23 + 1;
                                    inboxStyle2 = inboxStyle;
                                    min = i22;
                                    sb = str13;
                                    z22 = z22;
                                }
                                str2 = sb;
                                NotificationCompat.InboxStyle inboxStyle4 = inboxStyle2;
                                inboxStyle4.setSummaryText(str2);
                                builder = builder2;
                                builder.setStyle(inboxStyle4);
                                str3 = str12;
                                z7 = z22;
                            }
                            if (z && z6 && !MediaController.getInstance().isRecordingAudio() && !z7) {
                                z8 = false;
                                if (z8 && j3 == j4 && tLRPC$Chat != null) {
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append(NotificationsSettingsFacade.PROPERTY_CUSTOM);
                                    j5 = j3;
                                    sb2.append(j5);
                                    sharedPreferences3 = sharedPreferences2;
                                    if (sharedPreferences3.getBoolean(sb2.toString(), false)) {
                                        i18 = sharedPreferences3.getInt("smart_max_count_" + j5, 2);
                                        i19 = sharedPreferences3.getInt("smart_delay_" + j5, 180);
                                    } else {
                                        i18 = 2;
                                        i19 = 180;
                                    }
                                    if (i18 != 0) {
                                        Point point = this.smartNotificationsDialogs.get(j5);
                                        if (point == null) {
                                            this.smartNotificationsDialogs.put(j5, new Point(1, (int) (SystemClock.elapsedRealtime() / 1000)));
                                        } else {
                                            z9 = z8;
                                            str4 = str2;
                                            str5 = str3;
                                            if (point.y + i19 < SystemClock.elapsedRealtime() / 1000) {
                                                point.set(1, (int) (SystemClock.elapsedRealtime() / 1000));
                                            } else {
                                                int i24 = point.x;
                                                if (i24 < i18) {
                                                    point.set(i24 + 1, (int) (SystemClock.elapsedRealtime() / 1000));
                                                } else {
                                                    z9 = true;
                                                }
                                            }
                                            if (z9) {
                                                StringBuilder sb3 = new StringBuilder();
                                                sb3.append("sound_enabled_");
                                                i = topicId;
                                                sb3.append(getSharedPrefKey(j5, i));
                                                if (!sharedPreferences3.getBoolean(sb3.toString(), true)) {
                                                    z9 = true;
                                                }
                                            } else {
                                                i = topicId;
                                            }
                                            String path = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                                            z10 = ApplicationLoader.mainInterfacePaused;
                                            getSharedPrefKey(j5, i);
                                            NotificationCompat.Builder builder3 = builder;
                                            boolean z23 = z7;
                                            TLRPC$User tLRPC$User2 = tLRPC$User;
                                            SharedPreferences sharedPreferences5 = sharedPreferences3;
                                            j6 = j5;
                                            MessageObject messageObject9 = messageObject3;
                                            if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, i, false)) {
                                                charSequence = string;
                                                messageObject4 = messageObject9;
                                                tLRPC$Chat2 = tLRPC$Chat;
                                                sharedPreferences4 = sharedPreferences5;
                                                j7 = 0;
                                                str6 = null;
                                                i2 = 0;
                                                num = null;
                                                i3 = 3;
                                                z11 = false;
                                            } else {
                                                int property = this.dialogsNotificationsFacade.getProperty("vibrate_", j6, i, 0);
                                                int property2 = this.dialogsNotificationsFacade.getProperty("priority_", j6, i, 3);
                                                charSequence = string;
                                                messageObject4 = messageObject9;
                                                sharedPreferences4 = sharedPreferences5;
                                                long property3 = this.dialogsNotificationsFacade.getProperty("sound_document_id_", j6, i, 0L);
                                                if (property3 != 0) {
                                                    propertyString = getMediaDataController().ringtoneDataStore.getSoundPath(property3);
                                                    tLRPC$Chat2 = tLRPC$Chat;
                                                    z11 = true;
                                                    j7 = 0;
                                                } else {
                                                    tLRPC$Chat2 = tLRPC$Chat;
                                                    j7 = 0;
                                                    propertyString = this.dialogsNotificationsFacade.getPropertyString("sound_path_", j6, i, null);
                                                    z11 = false;
                                                }
                                                int property4 = this.dialogsNotificationsFacade.getProperty("color_", j6, i, 0);
                                                num = property4 != 0 ? Integer.valueOf(property4) : null;
                                                str6 = propertyString;
                                                i2 = property;
                                                i3 = property2;
                                            }
                                            if (j2 != j7) {
                                                long j14 = j7;
                                                if (j13 != j14) {
                                                    long j15 = sharedPreferences4.getLong("GlobalSoundDocId", j14);
                                                    if (j15 != j14) {
                                                        string2 = getMediaDataController().ringtoneDataStore.getSoundPath(j15);
                                                        z13 = true;
                                                    } else {
                                                        string2 = sharedPreferences4.getString("GlobalSoundPath", path);
                                                        z13 = false;
                                                    }
                                                    int i25 = sharedPreferences4.getInt("vibrate_messages", 0);
                                                    z12 = z13;
                                                    i6 = sharedPreferences4.getInt("priority_messages", 1);
                                                    i5 = sharedPreferences4.getInt("MessagesLed", -16776961);
                                                    str7 = string2;
                                                    i7 = i25;
                                                    i8 = 1;
                                                } else {
                                                    i4 = i;
                                                    i5 = -16776961;
                                                    str7 = null;
                                                    i6 = 0;
                                                    i7 = 0;
                                                    i8 = 1;
                                                    z12 = false;
                                                    if (i7 == 4) {
                                                        i7 = 0;
                                                        z14 = true;
                                                    } else {
                                                        z14 = false;
                                                    }
                                                    if (!TextUtils.isEmpty(str6) || TextUtils.equals(str7, str6)) {
                                                        str6 = str7;
                                                        z11 = z12;
                                                        i9 = 3;
                                                        z15 = true;
                                                    } else {
                                                        i9 = 3;
                                                        z15 = false;
                                                    }
                                                    if (i3 != i9 || i6 == i3) {
                                                        i3 = i6;
                                                    } else {
                                                        z15 = false;
                                                    }
                                                    if (num != null && num.intValue() != i5) {
                                                        i5 = num.intValue();
                                                        z15 = false;
                                                    }
                                                    if (i2 != 0 || i2 == 4 || i2 == i7) {
                                                        z16 = z15;
                                                    } else {
                                                        i7 = i2;
                                                        z16 = false;
                                                    }
                                                    if (z10) {
                                                        if (!sharedPreferences4.getBoolean("EnableInAppSounds", true)) {
                                                            str6 = null;
                                                        }
                                                        if (!sharedPreferences4.getBoolean("EnableInAppVibrate", true)) {
                                                            i7 = 2;
                                                        }
                                                        if (!sharedPreferences4.getBoolean("EnableInAppPriority", false)) {
                                                            i3 = 0;
                                                        } else if (i3 == 2) {
                                                            i3 = 1;
                                                        }
                                                    }
                                                    int i26 = i7;
                                                    String str14 = str6;
                                                    i10 = i26;
                                                    if (z14 && i10 != 2) {
                                                        try {
                                                            ringerMode = audioManager.getRingerMode();
                                                            if (ringerMode != 0 && ringerMode != 1) {
                                                                i10 = 2;
                                                            }
                                                        } catch (Exception e) {
                                                            FileLog.e(e);
                                                        }
                                                    }
                                                    if (z9) {
                                                        i10 = 0;
                                                        i5 = 0;
                                                        i3 = 0;
                                                        str14 = null;
                                                    }
                                                    Intent intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                    StringBuilder sb4 = new StringBuilder();
                                                    sb4.append("com.tmessages.openchat");
                                                    int i27 = i10;
                                                    sb4.append(Math.random());
                                                    sb4.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                    intent.setAction(sb4.toString());
                                                    intent.setFlags(ConnectionsManager.FileTypeFile);
                                                    if (!DialogObject.isEncryptedDialog(j6)) {
                                                        if (this.pushDialogs.size() == 1) {
                                                            if (j2 != 0) {
                                                                intent.putExtra("chatId", j2);
                                                            } else if (j13 != 0) {
                                                                intent.putExtra("userId", j13);
                                                            }
                                                        }
                                                        if (!AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
                                                            if (this.pushDialogs.size() != 1 || Build.VERSION.SDK_INT >= 28) {
                                                                tLRPC$Chat3 = tLRPC$Chat2;
                                                            } else if (tLRPC$Chat2 != null) {
                                                                tLRPC$Chat3 = tLRPC$Chat2;
                                                                TLRPC$ChatPhoto tLRPC$ChatPhoto = tLRPC$Chat3.photo;
                                                                if (tLRPC$ChatPhoto != null) {
                                                                    tLRPC$FileLocation = tLRPC$ChatPhoto.photo_small;
                                                                    if (tLRPC$FileLocation != null) {
                                                                        if (tLRPC$FileLocation.volume_id != 0) {
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                tLRPC$Chat3 = tLRPC$Chat2;
                                                                if (tLRPC$User2 != null && (tLRPC$UserProfilePhoto = tLRPC$User2.photo) != null && (tLRPC$FileLocation = tLRPC$UserProfilePhoto.photo_small) != null && tLRPC$FileLocation.volume_id != 0 && tLRPC$FileLocation.local_id != 0) {
                                                                    i11 = i5;
                                                                    j8 = j6;
                                                                    intent.putExtra("currentAccount", this.currentAccount);
                                                                    boolean z24 = z16;
                                                                    long j16 = j8;
                                                                    MessageObject messageObject10 = messageObject4;
                                                                    builder3.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject10.messageOwner.date * 1000).setColor(-15618822);
                                                                    builder3.setCategory("msg");
                                                                    if (tLRPC$Chat3 == null && tLRPC$User2 != null && (str9 = tLRPC$User2.phone) != null && str9.length() > 0) {
                                                                        builder3.addPerson("tel:+" + tLRPC$User2.phone);
                                                                    }
                                                                    Intent intent2 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                                    intent2.putExtra("messageDate", messageObject10.messageOwner.date);
                                                                    intent2.putExtra("currentAccount", this.currentAccount);
                                                                    builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent2, 167772160));
                                                                    if (tLRPC$FileLocation != null) {
                                                                        BitmapDrawable imageFromMemory = ImageLoader.getInstance().getImageFromMemory(tLRPC$FileLocation, null, "50_50");
                                                                        if (imageFromMemory != null) {
                                                                            builder3.setLargeIcon(imageFromMemory.getBitmap());
                                                                        } else {
                                                                            try {
                                                                                File pathToAttach = getFileLoader().getPathToAttach(tLRPC$FileLocation, true);
                                                                                if (pathToAttach.exists()) {
                                                                                    float dp = 160.0f / AndroidUtilities.dp(50.0f);
                                                                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                                                                    options.inSampleSize = dp < 1.0f ? 1 : (int) dp;
                                                                                    Bitmap decodeFile = BitmapFactory.decodeFile(pathToAttach.getAbsolutePath(), options);
                                                                                    if (decodeFile != null) {
                                                                                        builder3.setLargeIcon(decodeFile);
                                                                                    }
                                                                                }
                                                                            } catch (Throwable unused) {
                                                                            }
                                                                        }
                                                                    }
                                                                    if (z || z23) {
                                                                        builder3.setPriority(-1);
                                                                    } else if (i3 == 0) {
                                                                        builder3.setPriority(0);
                                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                                            z17 = true;
                                                                            i12 = 3;
                                                                        }
                                                                        z17 = true;
                                                                        i12 = 0;
                                                                    } else {
                                                                        int i28 = 1;
                                                                        if (i3 != 1) {
                                                                            if (i3 == 2) {
                                                                                i28 = 1;
                                                                            } else {
                                                                                if (i3 == 4) {
                                                                                    builder3.setPriority(-2);
                                                                                    if (Build.VERSION.SDK_INT >= 26) {
                                                                                        z17 = true;
                                                                                        i12 = 1;
                                                                                    }
                                                                                } else if (i3 == 5) {
                                                                                    builder3.setPriority(-1);
                                                                                    if (Build.VERSION.SDK_INT >= 26) {
                                                                                        z17 = true;
                                                                                        i12 = 2;
                                                                                    }
                                                                                }
                                                                                z17 = true;
                                                                                i12 = 0;
                                                                            }
                                                                        }
                                                                        builder3.setPriority(i28);
                                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                                            z17 = true;
                                                                            i12 = 4;
                                                                        }
                                                                        z17 = true;
                                                                        i12 = 0;
                                                                    }
                                                                    if (z23 != z17 && !z9) {
                                                                        if (!z10 || (sharedPreferences4.getBoolean("EnableInAppPreview", z17) && str5 != null)) {
                                                                            if (str5.length() > 100) {
                                                                                str8 = str5.substring(0, 100).replace('\n', ' ').trim() + "...";
                                                                            } else {
                                                                                str8 = str5;
                                                                            }
                                                                            builder3.setTicker(str8);
                                                                        }
                                                                        if (str14 != null && !str14.equalsIgnoreCase("NoSound")) {
                                                                            int i29 = Build.VERSION.SDK_INT;
                                                                            if (i29 >= 26) {
                                                                                if (!str14.equalsIgnoreCase("Default") && !str14.equals(path)) {
                                                                                    if (z11) {
                                                                                        uri = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", new File(str14));
                                                                                        ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uri, 1);
                                                                                    } else {
                                                                                        uri = Uri.parse(str14);
                                                                                    }
                                                                                    if (i11 != 0) {
                                                                                        i13 = i11;
                                                                                        builder3.setLights(i13, 1000, 1000);
                                                                                    } else {
                                                                                        i13 = i11;
                                                                                    }
                                                                                    if (i27 == 2) {
                                                                                        jArr2 = new long[]{0, 0};
                                                                                        builder3.setVibrate(jArr2);
                                                                                    } else {
                                                                                        if (i27 == 1) {
                                                                                            jArr3 = new long[]{0, 100, 0, 100};
                                                                                            builder3.setVibrate(jArr3);
                                                                                        } else {
                                                                                            if (i27 != 0 && i27 != 4) {
                                                                                                if (i27 == 3) {
                                                                                                    jArr3 = new long[]{0, 1000};
                                                                                                    builder3.setVibrate(jArr3);
                                                                                                } else {
                                                                                                    jArr = null;
                                                                                                }
                                                                                            }
                                                                                            builder3.setDefaults(2);
                                                                                            jArr2 = new long[0];
                                                                                        }
                                                                                        jArr = jArr3;
                                                                                    }
                                                                                    jArr = jArr2;
                                                                                }
                                                                                uri = Settings.System.DEFAULT_NOTIFICATION_URI;
                                                                                if (i11 != 0) {
                                                                                }
                                                                                if (i27 == 2) {
                                                                                }
                                                                                jArr = jArr2;
                                                                            } else if (str14.equals(path)) {
                                                                                builder3.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, 5);
                                                                            } else if (i29 >= 24 && str14.startsWith("file://") && !AndroidUtilities.isInternalUri(Uri.parse(str14))) {
                                                                                try {
                                                                                    Uri uriForFile = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", new File(str14.replace("file://", "")));
                                                                                    ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile, 1);
                                                                                    builder3.setSound(uriForFile, 5);
                                                                                } catch (Exception unused2) {
                                                                                    builder3.setSound(Uri.parse(str14), 5);
                                                                                }
                                                                            } else {
                                                                                builder3.setSound(Uri.parse(str14), 5);
                                                                            }
                                                                        }
                                                                        uri = null;
                                                                        if (i11 != 0) {
                                                                        }
                                                                        if (i27 == 2) {
                                                                        }
                                                                        jArr = jArr2;
                                                                    } else {
                                                                        i13 = i11;
                                                                        long[] jArr4 = {0, 0};
                                                                        builder3.setVibrate(jArr4);
                                                                        jArr = jArr4;
                                                                        uri = null;
                                                                    }
                                                                    if (AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter || messageObject10.getDialogId() != 777000 || (tLRPC$ReplyMarkup = messageObject10.messageOwner.reply_markup) == null) {
                                                                        i14 = i12;
                                                                        j9 = j16;
                                                                        z18 = false;
                                                                    } else {
                                                                        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList2 = tLRPC$ReplyMarkup.rows;
                                                                        int size = arrayList2.size();
                                                                        int i30 = 0;
                                                                        boolean z25 = false;
                                                                        while (i30 < size) {
                                                                            TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow2 = arrayList2.get(i30);
                                                                            int size2 = tLRPC$TL_keyboardButtonRow2.buttons.size();
                                                                            int i31 = 0;
                                                                            while (i31 < size2) {
                                                                                TLRPC$KeyboardButton tLRPC$KeyboardButton = tLRPC$TL_keyboardButtonRow2.buttons.get(i31);
                                                                                if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonCallback) {
                                                                                    arrayList = arrayList2;
                                                                                    i16 = size;
                                                                                    Intent intent3 = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                                                                    intent3.putExtra("currentAccount", this.currentAccount);
                                                                                    TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow3 = tLRPC$TL_keyboardButtonRow2;
                                                                                    i17 = i12;
                                                                                    j10 = j16;
                                                                                    intent3.putExtra("did", j10);
                                                                                    byte[] bArr = tLRPC$KeyboardButton.data;
                                                                                    if (bArr != null) {
                                                                                        tLRPC$TL_keyboardButtonRow = tLRPC$TL_keyboardButtonRow3;
                                                                                        intent3.putExtra("data", bArr);
                                                                                    } else {
                                                                                        tLRPC$TL_keyboardButtonRow = tLRPC$TL_keyboardButtonRow3;
                                                                                    }
                                                                                    intent3.putExtra("mid", messageObject10.getId());
                                                                                    String str15 = tLRPC$KeyboardButton.text;
                                                                                    Context context = ApplicationLoader.applicationContext;
                                                                                    int i32 = this.lastButtonId;
                                                                                    messageObject5 = messageObject10;
                                                                                    this.lastButtonId = i32 + 1;
                                                                                    builder3.addAction(0, str15, PendingIntent.getBroadcast(context, i32, intent3, 167772160));
                                                                                    z25 = true;
                                                                                } else {
                                                                                    arrayList = arrayList2;
                                                                                    i16 = size;
                                                                                    messageObject5 = messageObject10;
                                                                                    i17 = i12;
                                                                                    long j17 = j16;
                                                                                    tLRPC$TL_keyboardButtonRow = tLRPC$TL_keyboardButtonRow2;
                                                                                    j10 = j17;
                                                                                }
                                                                                i31++;
                                                                                size = i16;
                                                                                arrayList2 = arrayList;
                                                                                messageObject10 = messageObject5;
                                                                                long j18 = j10;
                                                                                tLRPC$TL_keyboardButtonRow2 = tLRPC$TL_keyboardButtonRow;
                                                                                j16 = j18;
                                                                                i12 = i17;
                                                                            }
                                                                            i30++;
                                                                            size = size;
                                                                            i12 = i12;
                                                                        }
                                                                        i14 = i12;
                                                                        j9 = j16;
                                                                        z18 = z25;
                                                                    }
                                                                    if (!z18 && (i15 = Build.VERSION.SDK_INT) < 24 && SharedConfig.passcodeHash.length() == 0 && hasMessagesToReply()) {
                                                                        Intent intent4 = new Intent(ApplicationLoader.applicationContext, PopupReplyReceiver.class);
                                                                        intent4.putExtra("currentAccount", this.currentAccount);
                                                                        if (i15 <= 19) {
                                                                            builder3.addAction(R.drawable.ic_ab_reply2, LocaleController.getString("Reply", R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent4, 167772160));
                                                                        } else {
                                                                            builder3.addAction(R.drawable.ic_ab_reply, LocaleController.getString("Reply", R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent4, 167772160));
                                                                        }
                                                                    }
                                                                    showExtraNotifications(builder3, str4, j9, i4, str11, jArr, i13, uri, i14, z24, z10, z9, i8);
                                                                    scheduleNotificationRepeat();
                                                                    return;
                                                                }
                                                            }
                                                        }
                                                        tLRPC$Chat3 = tLRPC$Chat2;
                                                    } else {
                                                        tLRPC$Chat3 = tLRPC$Chat2;
                                                        if (this.pushDialogs.size() == 1) {
                                                            i11 = i5;
                                                            j8 = j6;
                                                            if (j8 != globalSecretChatId) {
                                                                intent.putExtra("encId", DialogObject.getEncryptedChatId(j8));
                                                            }
                                                            tLRPC$FileLocation = null;
                                                            intent.putExtra("currentAccount", this.currentAccount);
                                                            boolean z242 = z16;
                                                            long j162 = j8;
                                                            MessageObject messageObject102 = messageObject4;
                                                            builder3.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject102.messageOwner.date * 1000).setColor(-15618822);
                                                            builder3.setCategory("msg");
                                                            if (tLRPC$Chat3 == null) {
                                                                builder3.addPerson("tel:+" + tLRPC$User2.phone);
                                                            }
                                                            Intent intent22 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                            intent22.putExtra("messageDate", messageObject102.messageOwner.date);
                                                            intent22.putExtra("currentAccount", this.currentAccount);
                                                            builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent22, 167772160));
                                                            if (tLRPC$FileLocation != null) {
                                                            }
                                                            if (z) {
                                                            }
                                                            builder3.setPriority(-1);
                                                        }
                                                    }
                                                    i11 = i5;
                                                    j8 = j6;
                                                    tLRPC$FileLocation = null;
                                                    intent.putExtra("currentAccount", this.currentAccount);
                                                    boolean z2422 = z16;
                                                    long j1622 = j8;
                                                    MessageObject messageObject1022 = messageObject4;
                                                    builder3.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject1022.messageOwner.date * 1000).setColor(-15618822);
                                                    builder3.setCategory("msg");
                                                    if (tLRPC$Chat3 == null) {
                                                    }
                                                    Intent intent222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                    intent222.putExtra("messageDate", messageObject1022.messageOwner.date);
                                                    intent222.putExtra("currentAccount", this.currentAccount);
                                                    builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent222, 167772160));
                                                    if (tLRPC$FileLocation != null) {
                                                    }
                                                    if (z) {
                                                    }
                                                    builder3.setPriority(-1);
                                                }
                                            } else if (z2) {
                                                long j19 = sharedPreferences4.getLong("ChannelSoundDocId", j7);
                                                if (j19 != j7) {
                                                    string4 = getMediaDataController().ringtoneDataStore.getSoundPath(j19);
                                                    z20 = true;
                                                } else {
                                                    string4 = sharedPreferences4.getString("ChannelSoundPath", path);
                                                    z20 = false;
                                                }
                                                int i33 = sharedPreferences4.getInt("vibrate_channel", 0);
                                                z12 = z20;
                                                i6 = sharedPreferences4.getInt("priority_channel", 1);
                                                i5 = sharedPreferences4.getInt("ChannelLed", -16776961);
                                                str7 = string4;
                                                i7 = i33;
                                                i8 = 2;
                                            } else {
                                                long j20 = sharedPreferences4.getLong("GroupSoundDocId", 0L);
                                                if (j20 != 0) {
                                                    string3 = getMediaDataController().ringtoneDataStore.getSoundPath(j20);
                                                    z19 = true;
                                                } else {
                                                    string3 = sharedPreferences4.getString("GroupSoundPath", path);
                                                    z19 = false;
                                                }
                                                int i34 = sharedPreferences4.getInt("vibrate_group", 0);
                                                z12 = z19;
                                                i6 = sharedPreferences4.getInt("priority_group", 1);
                                                i5 = sharedPreferences4.getInt("GroupLed", -16776961);
                                                str7 = string3;
                                                i7 = i34;
                                                i8 = 0;
                                            }
                                            i4 = i;
                                            if (i7 == 4) {
                                            }
                                            if (TextUtils.isEmpty(str6)) {
                                            }
                                            str6 = str7;
                                            z11 = z12;
                                            i9 = 3;
                                            z15 = true;
                                            if (i3 != i9) {
                                            }
                                            i3 = i6;
                                            if (num != null) {
                                                i5 = num.intValue();
                                                z15 = false;
                                            }
                                            if (i2 != 0) {
                                            }
                                            z16 = z15;
                                            if (z10) {
                                            }
                                            int i262 = i7;
                                            String str142 = str6;
                                            i10 = i262;
                                            if (z14) {
                                                ringerMode = audioManager.getRingerMode();
                                                if (ringerMode != 0) {
                                                    i10 = 2;
                                                }
                                            }
                                            if (z9) {
                                            }
                                            Intent intent5 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                            StringBuilder sb42 = new StringBuilder();
                                            sb42.append("com.tmessages.openchat");
                                            int i272 = i10;
                                            sb42.append(Math.random());
                                            sb42.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                            intent5.setAction(sb42.toString());
                                            intent5.setFlags(ConnectionsManager.FileTypeFile);
                                            if (!DialogObject.isEncryptedDialog(j6)) {
                                            }
                                            i11 = i5;
                                            j8 = j6;
                                            tLRPC$FileLocation = null;
                                            intent5.putExtra("currentAccount", this.currentAccount);
                                            boolean z24222 = z16;
                                            long j16222 = j8;
                                            MessageObject messageObject10222 = messageObject4;
                                            builder3.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent5, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject10222.messageOwner.date * 1000).setColor(-15618822);
                                            builder3.setCategory("msg");
                                            if (tLRPC$Chat3 == null) {
                                            }
                                            Intent intent2222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                            intent2222.putExtra("messageDate", messageObject10222.messageOwner.date);
                                            intent2222.putExtra("currentAccount", this.currentAccount);
                                            builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent2222, 167772160));
                                            if (tLRPC$FileLocation != null) {
                                            }
                                            if (z) {
                                            }
                                            builder3.setPriority(-1);
                                        }
                                    }
                                    str4 = str2;
                                } else {
                                    str4 = str2;
                                    sharedPreferences3 = sharedPreferences2;
                                    j5 = j3;
                                }
                                str5 = str3;
                                z9 = z8;
                                if (z9) {
                                }
                                String path2 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                                if (ApplicationLoader.mainInterfacePaused) {
                                }
                                getSharedPrefKey(j5, i);
                                NotificationCompat.Builder builder32 = builder;
                                boolean z232 = z7;
                                TLRPC$User tLRPC$User22 = tLRPC$User;
                                SharedPreferences sharedPreferences52 = sharedPreferences3;
                                j6 = j5;
                                MessageObject messageObject92 = messageObject3;
                                if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, i, false)) {
                                }
                                if (j2 != j7) {
                                }
                                i4 = i;
                                if (i7 == 4) {
                                }
                                if (TextUtils.isEmpty(str6)) {
                                }
                                str6 = str7;
                                z11 = z12;
                                i9 = 3;
                                z15 = true;
                                if (i3 != i9) {
                                }
                                i3 = i6;
                                if (num != null) {
                                }
                                if (i2 != 0) {
                                }
                                z16 = z15;
                                if (z10) {
                                }
                                int i2622 = i7;
                                String str1422 = str6;
                                i10 = i2622;
                                if (z14) {
                                }
                                if (z9) {
                                }
                                Intent intent52 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                StringBuilder sb422 = new StringBuilder();
                                sb422.append("com.tmessages.openchat");
                                int i2722 = i10;
                                sb422.append(Math.random());
                                sb422.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                intent52.setAction(sb422.toString());
                                intent52.setFlags(ConnectionsManager.FileTypeFile);
                                if (!DialogObject.isEncryptedDialog(j6)) {
                                }
                                i11 = i5;
                                j8 = j6;
                                tLRPC$FileLocation = null;
                                intent52.putExtra("currentAccount", this.currentAccount);
                                boolean z242222 = z16;
                                long j162222 = j8;
                                MessageObject messageObject102222 = messageObject4;
                                builder32.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent52, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject102222.messageOwner.date * 1000).setColor(-15618822);
                                builder32.setCategory("msg");
                                if (tLRPC$Chat3 == null) {
                                }
                                Intent intent22222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                intent22222.putExtra("messageDate", messageObject102222.messageOwner.date);
                                intent22222.putExtra("currentAccount", this.currentAccount);
                                builder32.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent22222, 167772160));
                                if (tLRPC$FileLocation != null) {
                                }
                                if (z) {
                                }
                                builder32.setPriority(-1);
                            }
                            z8 = true;
                            if (z8) {
                            }
                            str4 = str2;
                            sharedPreferences3 = sharedPreferences2;
                            j5 = j3;
                            str5 = str3;
                            z9 = z8;
                            if (z9) {
                            }
                            String path22 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                            if (ApplicationLoader.mainInterfacePaused) {
                            }
                            getSharedPrefKey(j5, i);
                            NotificationCompat.Builder builder322 = builder;
                            boolean z2322 = z7;
                            TLRPC$User tLRPC$User222 = tLRPC$User;
                            SharedPreferences sharedPreferences522 = sharedPreferences3;
                            j6 = j5;
                            MessageObject messageObject922 = messageObject3;
                            if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, i, false)) {
                            }
                            if (j2 != j7) {
                            }
                            i4 = i;
                            if (i7 == 4) {
                            }
                            if (TextUtils.isEmpty(str6)) {
                            }
                            str6 = str7;
                            z11 = z12;
                            i9 = 3;
                            z15 = true;
                            if (i3 != i9) {
                            }
                            i3 = i6;
                            if (num != null) {
                            }
                            if (i2 != 0) {
                            }
                            z16 = z15;
                            if (z10) {
                            }
                            int i26222 = i7;
                            String str14222 = str6;
                            i10 = i26222;
                            if (z14) {
                            }
                            if (z9) {
                            }
                            Intent intent522 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                            StringBuilder sb4222 = new StringBuilder();
                            sb4222.append("com.tmessages.openchat");
                            int i27222 = i10;
                            sb4222.append(Math.random());
                            sb4222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                            intent522.setAction(sb4222.toString());
                            intent522.setFlags(ConnectionsManager.FileTypeFile);
                            if (!DialogObject.isEncryptedDialog(j6)) {
                            }
                            i11 = i5;
                            j8 = j6;
                            tLRPC$FileLocation = null;
                            intent522.putExtra("currentAccount", this.currentAccount);
                            boolean z2422222 = z16;
                            long j1622222 = j8;
                            MessageObject messageObject1022222 = messageObject4;
                            builder322.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent522, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject1022222.messageOwner.date * 1000).setColor(-15618822);
                            builder322.setCategory("msg");
                            if (tLRPC$Chat3 == null) {
                            }
                            Intent intent222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                            intent222222.putExtra("messageDate", messageObject1022222.messageOwner.date);
                            intent222222.putExtra("currentAccount", this.currentAccount);
                            builder322.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent222222, 167772160));
                            if (tLRPC$FileLocation != null) {
                            }
                            if (z) {
                            }
                            builder322.setPriority(-1);
                        }
                        sharedPreferences2 = sharedPreferences;
                        if (this.pushDialogs.size() == 1) {
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append(str);
                            j3 = j;
                            sb5.append(LocaleController.formatPluralString("NewMessages", this.total_unread_count, new Object[0]));
                            sb = sb5.toString();
                            j4 = fromChatId;
                        } else {
                            j3 = j;
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append(str);
                            j4 = fromChatId;
                            sb6.append(LocaleController.formatString("NotificationMessagesPeopleDisplayOrder", R.string.NotificationMessagesPeopleDisplayOrder, LocaleController.formatPluralString("NewMessages", this.total_unread_count, new Object[0]), LocaleController.formatPluralString("FromChats", this.pushDialogs.size(), new Object[0])));
                            sb = sb6.toString();
                        }
                        NotificationCompat.Builder builder22 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                        if (this.pushMessages.size() == 1) {
                        }
                        if (z) {
                            z8 = false;
                            if (z8) {
                            }
                            str4 = str2;
                            sharedPreferences3 = sharedPreferences2;
                            j5 = j3;
                            str5 = str3;
                            z9 = z8;
                            if (z9) {
                            }
                            String path222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                            if (ApplicationLoader.mainInterfacePaused) {
                            }
                            getSharedPrefKey(j5, i);
                            NotificationCompat.Builder builder3222 = builder;
                            boolean z23222 = z7;
                            TLRPC$User tLRPC$User2222 = tLRPC$User;
                            SharedPreferences sharedPreferences5222 = sharedPreferences3;
                            j6 = j5;
                            MessageObject messageObject9222 = messageObject3;
                            if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, i, false)) {
                            }
                            if (j2 != j7) {
                            }
                            i4 = i;
                            if (i7 == 4) {
                            }
                            if (TextUtils.isEmpty(str6)) {
                            }
                            str6 = str7;
                            z11 = z12;
                            i9 = 3;
                            z15 = true;
                            if (i3 != i9) {
                            }
                            i3 = i6;
                            if (num != null) {
                            }
                            if (i2 != 0) {
                            }
                            z16 = z15;
                            if (z10) {
                            }
                            int i262222 = i7;
                            String str142222 = str6;
                            i10 = i262222;
                            if (z14) {
                            }
                            if (z9) {
                            }
                            Intent intent5222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                            StringBuilder sb42222 = new StringBuilder();
                            sb42222.append("com.tmessages.openchat");
                            int i272222 = i10;
                            sb42222.append(Math.random());
                            sb42222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                            intent5222.setAction(sb42222.toString());
                            intent5222.setFlags(ConnectionsManager.FileTypeFile);
                            if (!DialogObject.isEncryptedDialog(j6)) {
                            }
                            i11 = i5;
                            j8 = j6;
                            tLRPC$FileLocation = null;
                            intent5222.putExtra("currentAccount", this.currentAccount);
                            boolean z24222222 = z16;
                            long j16222222 = j8;
                            MessageObject messageObject10222222 = messageObject4;
                            builder3222.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent5222, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject10222222.messageOwner.date * 1000).setColor(-15618822);
                            builder3222.setCategory("msg");
                            if (tLRPC$Chat3 == null) {
                            }
                            Intent intent2222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                            intent2222222.putExtra("messageDate", messageObject10222222.messageOwner.date);
                            intent2222222.putExtra("currentAccount", this.currentAccount);
                            builder3222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent2222222, 167772160));
                            if (tLRPC$FileLocation != null) {
                            }
                            if (z) {
                            }
                            builder3222.setPriority(-1);
                        }
                        z8 = true;
                        if (z8) {
                        }
                        str4 = str2;
                        sharedPreferences3 = sharedPreferences2;
                        j5 = j3;
                        str5 = str3;
                        z9 = z8;
                        if (z9) {
                        }
                        String path2222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                        if (ApplicationLoader.mainInterfacePaused) {
                        }
                        getSharedPrefKey(j5, i);
                        NotificationCompat.Builder builder32222 = builder;
                        boolean z232222 = z7;
                        TLRPC$User tLRPC$User22222 = tLRPC$User;
                        SharedPreferences sharedPreferences52222 = sharedPreferences3;
                        j6 = j5;
                        MessageObject messageObject92222 = messageObject3;
                        if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, i, false)) {
                        }
                        if (j2 != j7) {
                        }
                        i4 = i;
                        if (i7 == 4) {
                        }
                        if (TextUtils.isEmpty(str6)) {
                        }
                        str6 = str7;
                        z11 = z12;
                        i9 = 3;
                        z15 = true;
                        if (i3 != i9) {
                        }
                        i3 = i6;
                        if (num != null) {
                        }
                        if (i2 != 0) {
                        }
                        z16 = z15;
                        if (z10) {
                        }
                        int i2622222 = i7;
                        String str1422222 = str6;
                        i10 = i2622222;
                        if (z14) {
                        }
                        if (z9) {
                        }
                        Intent intent52222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        StringBuilder sb422222 = new StringBuilder();
                        sb422222.append("com.tmessages.openchat");
                        int i2722222 = i10;
                        sb422222.append(Math.random());
                        sb422222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                        intent52222.setAction(sb422222.toString());
                        intent52222.setFlags(ConnectionsManager.FileTypeFile);
                        if (!DialogObject.isEncryptedDialog(j6)) {
                        }
                        i11 = i5;
                        j8 = j6;
                        tLRPC$FileLocation = null;
                        intent52222.putExtra("currentAccount", this.currentAccount);
                        boolean z242222222 = z16;
                        long j162222222 = j8;
                        MessageObject messageObject102222222 = messageObject4;
                        builder32222.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent52222, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject102222222.messageOwner.date * 1000).setColor(-15618822);
                        builder32222.setCategory("msg");
                        if (tLRPC$Chat3 == null) {
                        }
                        Intent intent22222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                        intent22222222.putExtra("messageDate", messageObject102222222.messageOwner.date);
                        intent22222222.putExtra("currentAccount", this.currentAccount);
                        builder32222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent22222222, 167772160));
                        if (tLRPC$FileLocation != null) {
                        }
                        if (z) {
                        }
                        builder32222.setPriority(-1);
                    }
                } else {
                    tLRPC$User = user;
                    messageObject3 = messageObject2;
                }
                string = LocaleController.getString("AppName", R.string.AppName);
                z5 = false;
                j2 = j11;
                if (UserConfig.getActivatedAccountsCount() <= 1) {
                }
                if (this.pushDialogs.size() == 1) {
                    j3 = j;
                    j4 = fromChatId;
                    sb = str;
                    sharedPreferences2 = sharedPreferences;
                    NotificationCompat.Builder builder222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                    if (this.pushMessages.size() == 1) {
                    }
                    if (z) {
                    }
                    z8 = true;
                    if (z8) {
                    }
                    str4 = str2;
                    sharedPreferences3 = sharedPreferences2;
                    j5 = j3;
                    str5 = str3;
                    z9 = z8;
                    if (z9) {
                    }
                    String path22222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                    if (ApplicationLoader.mainInterfacePaused) {
                    }
                    getSharedPrefKey(j5, i);
                    NotificationCompat.Builder builder322222 = builder;
                    boolean z2322222 = z7;
                    TLRPC$User tLRPC$User222222 = tLRPC$User;
                    SharedPreferences sharedPreferences522222 = sharedPreferences3;
                    j6 = j5;
                    MessageObject messageObject922222 = messageObject3;
                    if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, i, false)) {
                    }
                    if (j2 != j7) {
                    }
                    i4 = i;
                    if (i7 == 4) {
                    }
                    if (TextUtils.isEmpty(str6)) {
                    }
                    str6 = str7;
                    z11 = z12;
                    i9 = 3;
                    z15 = true;
                    if (i3 != i9) {
                    }
                    i3 = i6;
                    if (num != null) {
                    }
                    if (i2 != 0) {
                    }
                    z16 = z15;
                    if (z10) {
                    }
                    int i26222222 = i7;
                    String str14222222 = str6;
                    i10 = i26222222;
                    if (z14) {
                    }
                    if (z9) {
                    }
                    Intent intent522222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    StringBuilder sb4222222 = new StringBuilder();
                    sb4222222.append("com.tmessages.openchat");
                    int i27222222 = i10;
                    sb4222222.append(Math.random());
                    sb4222222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                    intent522222.setAction(sb4222222.toString());
                    intent522222.setFlags(ConnectionsManager.FileTypeFile);
                    if (!DialogObject.isEncryptedDialog(j6)) {
                    }
                    i11 = i5;
                    j8 = j6;
                    tLRPC$FileLocation = null;
                    intent522222.putExtra("currentAccount", this.currentAccount);
                    boolean z2422222222 = z16;
                    long j1622222222 = j8;
                    MessageObject messageObject1022222222 = messageObject4;
                    builder322222.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent522222, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject1022222222.messageOwner.date * 1000).setColor(-15618822);
                    builder322222.setCategory("msg");
                    if (tLRPC$Chat3 == null) {
                    }
                    Intent intent222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                    intent222222222.putExtra("messageDate", messageObject1022222222.messageOwner.date);
                    intent222222222.putExtra("currentAccount", this.currentAccount);
                    builder322222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent222222222, 167772160));
                    if (tLRPC$FileLocation != null) {
                    }
                    if (z) {
                    }
                    builder322222.setPriority(-1);
                }
                sharedPreferences2 = sharedPreferences;
                if (this.pushDialogs.size() == 1) {
                }
                NotificationCompat.Builder builder2222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                if (this.pushMessages.size() == 1) {
                }
                if (z) {
                }
                z8 = true;
                if (z8) {
                }
                str4 = str2;
                sharedPreferences3 = sharedPreferences2;
                j5 = j3;
                str5 = str3;
                z9 = z8;
                if (z9) {
                }
                String path222222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                if (ApplicationLoader.mainInterfacePaused) {
                }
                getSharedPrefKey(j5, i);
                NotificationCompat.Builder builder3222222 = builder;
                boolean z23222222 = z7;
                TLRPC$User tLRPC$User2222222 = tLRPC$User;
                SharedPreferences sharedPreferences5222222 = sharedPreferences3;
                j6 = j5;
                MessageObject messageObject9222222 = messageObject3;
                if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, i, false)) {
                }
                if (j2 != j7) {
                }
                i4 = i;
                if (i7 == 4) {
                }
                if (TextUtils.isEmpty(str6)) {
                }
                str6 = str7;
                z11 = z12;
                i9 = 3;
                z15 = true;
                if (i3 != i9) {
                }
                i3 = i6;
                if (num != null) {
                }
                if (i2 != 0) {
                }
                z16 = z15;
                if (z10) {
                }
                int i262222222 = i7;
                String str142222222 = str6;
                i10 = i262222222;
                if (z14) {
                }
                if (z9) {
                }
                Intent intent5222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                StringBuilder sb42222222 = new StringBuilder();
                sb42222222.append("com.tmessages.openchat");
                int i272222222 = i10;
                sb42222222.append(Math.random());
                sb42222222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                intent5222222.setAction(sb42222222.toString());
                intent5222222.setFlags(ConnectionsManager.FileTypeFile);
                if (!DialogObject.isEncryptedDialog(j6)) {
                }
                i11 = i5;
                j8 = j6;
                tLRPC$FileLocation = null;
                intent5222222.putExtra("currentAccount", this.currentAccount);
                boolean z24222222222 = z16;
                long j16222222222 = j8;
                MessageObject messageObject10222222222 = messageObject4;
                builder3222222.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent5222222, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject10222222222.messageOwner.date * 1000).setColor(-15618822);
                builder3222222.setCategory("msg");
                if (tLRPC$Chat3 == null) {
                }
                Intent intent2222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                intent2222222222.putExtra("messageDate", messageObject10222222222.messageOwner.date);
                intent2222222222.putExtra("currentAccount", this.currentAccount);
                builder3222222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent2222222222, 167772160));
                if (tLRPC$FileLocation != null) {
                }
                if (z) {
                }
                builder3222222.setPriority(-1);
            }
            z4 = true;
            if (DialogObject.isEncryptedDialog(j)) {
            }
            string = LocaleController.getString("AppName", R.string.AppName);
            z5 = false;
            j2 = j11;
            if (UserConfig.getActivatedAccountsCount() <= 1) {
            }
            if (this.pushDialogs.size() == 1) {
            }
            sharedPreferences2 = sharedPreferences;
            if (this.pushDialogs.size() == 1) {
            }
            NotificationCompat.Builder builder22222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
            if (this.pushMessages.size() == 1) {
            }
            if (z) {
            }
            z8 = true;
            if (z8) {
            }
            str4 = str2;
            sharedPreferences3 = sharedPreferences2;
            j5 = j3;
            str5 = str3;
            z9 = z8;
            if (z9) {
            }
            String path2222222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
            if (ApplicationLoader.mainInterfacePaused) {
            }
            getSharedPrefKey(j5, i);
            NotificationCompat.Builder builder32222222 = builder;
            boolean z232222222 = z7;
            TLRPC$User tLRPC$User22222222 = tLRPC$User;
            SharedPreferences sharedPreferences52222222 = sharedPreferences3;
            j6 = j5;
            MessageObject messageObject92222222 = messageObject3;
            if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, i, false)) {
            }
            if (j2 != j7) {
            }
            i4 = i;
            if (i7 == 4) {
            }
            if (TextUtils.isEmpty(str6)) {
            }
            str6 = str7;
            z11 = z12;
            i9 = 3;
            z15 = true;
            if (i3 != i9) {
            }
            i3 = i6;
            if (num != null) {
            }
            if (i2 != 0) {
            }
            z16 = z15;
            if (z10) {
            }
            int i2622222222 = i7;
            String str1422222222 = str6;
            i10 = i2622222222;
            if (z14) {
            }
            if (z9) {
            }
            Intent intent52222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
            StringBuilder sb422222222 = new StringBuilder();
            sb422222222.append("com.tmessages.openchat");
            int i2722222222 = i10;
            sb422222222.append(Math.random());
            sb422222222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
            intent52222222.setAction(sb422222222.toString());
            intent52222222.setFlags(ConnectionsManager.FileTypeFile);
            if (!DialogObject.isEncryptedDialog(j6)) {
            }
            i11 = i5;
            j8 = j6;
            tLRPC$FileLocation = null;
            intent52222222.putExtra("currentAccount", this.currentAccount);
            boolean z242222222222 = z16;
            long j162222222222 = j8;
            MessageObject messageObject102222222222 = messageObject4;
            builder32222222.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent52222222, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject102222222222.messageOwner.date * 1000).setColor(-15618822);
            builder32222222.setCategory("msg");
            if (tLRPC$Chat3 == null) {
            }
            Intent intent22222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
            intent22222222222.putExtra("messageDate", messageObject102222222222.messageOwner.date);
            intent22222222222.putExtra("currentAccount", this.currentAccount);
            builder32222222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent22222222222, 167772160));
            if (tLRPC$FileLocation != null) {
            }
            if (z) {
            }
            builder32222222.setPriority(-1);
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
    public void resetNotificationSound(NotificationCompat.Builder builder, long j, int i, String str, long[] jArr, int i2, Uri uri, int i3, boolean z, boolean z2, boolean z3, int i4) {
        Uri uri2 = Settings.System.DEFAULT_RINGTONE_URI;
        if (uri2 == null || uri == null || TextUtils.equals(uri2.toString(), uri.toString())) {
            return;
        }
        SharedPreferences.Editor edit = getAccountInstance().getNotificationsSettings().edit();
        String uri3 = uri2.toString();
        String string = LocaleController.getString("DefaultRingtone", R.string.DefaultRingtone);
        if (z) {
            if (i4 == 2) {
                edit.putString("ChannelSound", string);
            } else if (i4 == 0) {
                edit.putString("GroupSound", string);
            } else {
                edit.putString("GlobalSound", string);
            }
            if (i4 == 2) {
                edit.putString("ChannelSoundPath", uri3);
            } else if (i4 == 0) {
                edit.putString("GroupSoundPath", uri3);
            } else {
                edit.putString("GlobalSoundPath", uri3);
            }
            getNotificationsController().lambda$deleteNotificationChannelGlobal$32(i4, -1);
        } else {
            edit.putString("sound_" + getSharedPrefKey(j, i), string);
            edit.putString("sound_path_" + getSharedPrefKey(j, i), uri3);
            lambda$deleteNotificationChannel$31(j, i, -1);
        }
        edit.commit();
        builder.setChannelId(validateChannelId(j, i, str, jArr, i2, Settings.System.DEFAULT_RINGTONE_URI, i3, z, z2, z3, i4));
        notificationManager.notify(this.notificationId, builder.build());
    }

    /* JADX WARN: Can't wrap try/catch for region: R(68:68|(2:70|(1:72)(5:478|(1:480)|481|482|483))(3:484|(1:494)(2:488|(53:492|74|(1:76)(2:475|(1:477))|77|78|(3:80|(1:82)(1:473)|83)(1:474)|(3:85|(2:87|(1:89)(3:460|461|(3:463|(1:465)(1:467)|466)))(1:471)|468)(1:472)|(3:91|(1:97)|98)|99|(3:455|(1:457)(1:459)|458)(1:102)|103|(1:105)|106|(1:447)(1:110)|111|(1:446)(6:114|(1:116)|(3:421|422|(6:426|427|428|(5:432|433|434|435|120)|119|120))|118|119|120)|121|(1:420)(1:124)|125|(1:419)|132|(1:418)(1:139)|140|(6:143|(1:145)(4:149|(1:151)(2:325|(3:327|(1:329)|(3:154|(1:156)(1:158)|157)(22:159|(1:161)|162|(2:164|(17:168|169|(11:(1:172)(2:316|(1:318))|173|174|(2:(3:177|(2:(2:180|(1:182))(1:310)|183)(2:311|(1:313))|148)|314)(1:315)|184|(3:281|(1:309)(3:287|(1:308)(3:290|(1:294)|(1:307)(1:304))|305)|306)(1:188)|189|(6:191|(1:279)(7:204|(1:278)(3:208|(9:260|261|262|263|264|265|266|267|268)(1:210)|211)|212|(1:214)(1:259)|215|(3:253|254|255)(3:217|(1:219)|252)|(6:221|(1:223)|224|(1:226)|227|(2:232|(3:234|(2:239|240)(1:236)|(1:238)))))|251|(0)|227|(3:230|232|(0)))(1:280)|243|(2:247|248)|148)|319|174|(0)(0)|184|(1:186)|281|(1:283)|309|306|189|(0)(0)|243|(1:250)(3:245|247|248)|148))(1:324)|320|(1:322)(1:323)|169|(0)|319|174|(0)(0)|184|(0)|281|(0)|309|306|189|(0)(0)|243|(0)(0)|148)))|152|(0)(0))|146|147|148|141)|330|331|(1:333)(2:414|(1:416)(1:417))|334|(1:336)|337|(1:339)|340|(2:342|(1:344)(1:409))(2:410|(1:412)(1:413))|(1:346)(1:408)|347|348|349|(1:351)|(1:353)|(1:355)|356|(1:404)(1:360)|361|(1:363)|(1:365)|366|(3:372|(4:374|(3:376|(4:378|(1:380)|381|382)(2:384|385)|383)|386|387)|388)|389|(1:403)(2:392|(1:396))|397|(1:399)|400|401))|493)|73|74|(0)(0)|77|78|(0)(0)|(0)(0)|(0)|99|(0)|449|451|453|455|(0)(0)|458|103|(0)|106|(1:108)|447|111|(0)|446|121|(0)|420|125|(1:127)|419|132|(1:135)|418|140|(1:141)|330|331|(0)(0)|334|(0)|337|(0)|340|(0)(0)|(0)(0)|347|348|349|(0)|(0)|(0)|356|(1:358)|404|361|(0)|(0)|366|(5:368|370|372|(0)|388)|389|(0)|403|397|(0)|400|401) */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x02cd, code lost:
        if (r9.local_id != 0) goto L516;
     */
    /* JADX WARN: Code restructure failed: missing block: B:470:0x0cb6, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x0cb7, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0307  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x03b5  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x03ce  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x03d2  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x0427  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x0432  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x045e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:192:0x04c1  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x04d2  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x051a  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x052f  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x0558 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:238:0x05df A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:243:0x05f3  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x0608 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:262:0x0639  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x0694  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x06cb  */
    /* JADX WARN: Removed duplicated region for block: B:299:0x0726  */
    /* JADX WARN: Removed duplicated region for block: B:308:0x074c  */
    /* JADX WARN: Removed duplicated region for block: B:321:0x0783  */
    /* JADX WARN: Removed duplicated region for block: B:324:0x078e  */
    /* JADX WARN: Removed duplicated region for block: B:330:0x07ae  */
    /* JADX WARN: Removed duplicated region for block: B:360:0x082c  */
    /* JADX WARN: Removed duplicated region for block: B:398:0x0929  */
    /* JADX WARN: Removed duplicated region for block: B:399:0x092c  */
    /* JADX WARN: Removed duplicated region for block: B:406:0x0958  */
    /* JADX WARN: Removed duplicated region for block: B:411:0x09b2  */
    /* JADX WARN: Removed duplicated region for block: B:418:0x09eb  */
    /* JADX WARN: Removed duplicated region for block: B:421:0x09fa A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:426:0x0a0c  */
    /* JADX WARN: Removed duplicated region for block: B:434:0x0a52  */
    /* JADX WARN: Removed duplicated region for block: B:437:0x0a66  */
    /* JADX WARN: Removed duplicated region for block: B:443:0x0ad2  */
    /* JADX WARN: Removed duplicated region for block: B:444:0x0adc  */
    /* JADX WARN: Removed duplicated region for block: B:450:0x0b0e  */
    /* JADX WARN: Removed duplicated region for block: B:453:0x0b2d  */
    /* JADX WARN: Removed duplicated region for block: B:456:0x0b85  */
    /* JADX WARN: Removed duplicated region for block: B:460:0x0bbe  */
    /* JADX WARN: Removed duplicated region for block: B:465:0x0be4  */
    /* JADX WARN: Removed duplicated region for block: B:466:0x0c07  */
    /* JADX WARN: Removed duplicated region for block: B:473:0x0cbc  */
    /* JADX WARN: Removed duplicated region for block: B:475:0x0cc7  */
    /* JADX WARN: Removed duplicated region for block: B:477:0x0ccc  */
    /* JADX WARN: Removed duplicated region for block: B:480:0x0cd6  */
    /* JADX WARN: Removed duplicated region for block: B:486:0x0cea  */
    /* JADX WARN: Removed duplicated region for block: B:488:0x0cef  */
    /* JADX WARN: Removed duplicated region for block: B:491:0x0cfb  */
    /* JADX WARN: Removed duplicated region for block: B:497:0x0d0a  */
    /* JADX WARN: Removed duplicated region for block: B:510:0x0d91 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:519:0x0dc3  */
    /* JADX WARN: Removed duplicated region for block: B:581:0x0935 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:599:0x0a76 A[ADDED_TO_REGION, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x020f  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0218  */
    @SuppressLint({"InlinedApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showExtraNotifications(NotificationCompat.Builder builder, String str, long j, int i, String str2, long[] jArr, int i2, Uri uri, int i3, boolean z, boolean z2, boolean z3, int i4) {
        ArrayList arrayList;
        NotificationsController notificationsController;
        LongSparseArray longSparseArray;
        int i5;
        int i6;
        int i7;
        MessageObject messageObject;
        Integer num;
        boolean z4;
        TLRPC$User tLRPC$User;
        TLRPC$User tLRPC$User2;
        String string;
        boolean z5;
        TLRPC$FileLocation tLRPC$FileLocation;
        TLRPC$Chat tLRPC$Chat;
        boolean z6;
        boolean z7;
        NotificationsController notificationsController2;
        long j2;
        LongSparseArray longSparseArray2;
        Notification notification;
        ArrayList arrayList2;
        int i8;
        LongSparseArray longSparseArray3;
        boolean z8;
        ArrayList arrayList3;
        int i9;
        LongSparseArray longSparseArray4;
        LongSparseArray longSparseArray5;
        TLRPC$User tLRPC$User3;
        String str3;
        boolean z9;
        Bitmap bitmap;
        File file;
        File file2;
        TLRPC$Chat tLRPC$Chat2;
        NotificationsController notificationsController3;
        Bitmap bitmap2;
        String str4;
        int i10;
        String str5;
        String formatString;
        NotificationCompat.Action build;
        Integer num2;
        String format;
        Person person;
        String str6;
        MessageObject messageObject2;
        NotificationCompat.MessagingStyle messagingStyle;
        int i11;
        NotificationCompat.Action action;
        int size;
        String str7;
        TLRPC$User tLRPC$User4;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList4;
        int size2;
        int i12;
        int i13;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList5;
        String str8;
        int i14;
        String str9;
        boolean[] zArr;
        long j3;
        String str10;
        StringBuilder sb;
        long j4;
        Person person2;
        String str11;
        String str12;
        String str13;
        String[] strArr;
        Person person3;
        File file3;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        TLRPC$FileLocation tLRPC$FileLocation2;
        LongSparseArray longSparseArray6;
        String str14;
        NotificationCompat.MessagingStyle messagingStyle2;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup;
        boolean z10;
        List<NotificationCompat.MessagingStyle.Message> messages;
        Uri uri2;
        File file4;
        final File file5;
        final Uri uriForFile;
        File file6;
        Bitmap createScaledBitmap;
        Canvas canvas;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto2;
        TLRPC$FileLocation tLRPC$FileLocation3;
        Person person4;
        Bitmap bitmap3;
        String string2;
        boolean z11;
        TLRPC$FileLocation tLRPC$FileLocation4;
        TLRPC$TL_forumTopic findTopic;
        TLRPC$User tLRPC$User5;
        String str15;
        TLRPC$FileLocation tLRPC$FileLocation5;
        NotificationsController notificationsController4 = this;
        int i15 = Build.VERSION.SDK_INT;
        if (i15 >= 26) {
            builder.setChannelId(validateChannelId(j, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
        }
        Notification build2 = builder.build();
        if (i15 < 18) {
            notificationManager.notify(notificationsController4.notificationId, build2);
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("show summary notification by SDK check");
                return;
            }
            return;
        }
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        ArrayList arrayList6 = new ArrayList();
        LongSparseArray longSparseArray7 = new LongSparseArray();
        for (int i16 = 0; i16 < notificationsController4.pushMessages.size(); i16++) {
            MessageObject messageObject3 = notificationsController4.pushMessages.get(i16);
            long dialogId = messageObject3.getDialogId();
            int topicId = MessageObject.getTopicId(messageObject3.messageOwner, getMessagesController().isForum(messageObject3));
            if (messageObject3.messageOwner.date > notificationsSettings.getInt("dismissDate" + dialogId, 0)) {
                ArrayList arrayList7 = (ArrayList) longSparseArray7.get(dialogId);
                if (arrayList7 == null) {
                    arrayList7 = new ArrayList();
                    longSparseArray7.put(dialogId, arrayList7);
                    arrayList6.add(new DialogKey(dialogId, topicId));
                }
                arrayList7.add(messageObject3);
            }
        }
        LongSparseArray longSparseArray8 = new LongSparseArray();
        for (int i17 = 0; i17 < notificationsController4.wearNotificationsIds.size(); i17++) {
            longSparseArray8.put(notificationsController4.wearNotificationsIds.keyAt(i17), notificationsController4.wearNotificationsIds.valueAt(i17));
        }
        notificationsController4.wearNotificationsIds.clear();
        ArrayList arrayList8 = new ArrayList();
        int i18 = Build.VERSION.SDK_INT;
        boolean z12 = i18 <= 27 || arrayList6.size() > 1;
        if (z12 && i18 >= 26) {
            checkOtherNotificationsChannel();
        }
        long clientUserId = getUserConfig().getClientUserId();
        boolean z13 = AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter;
        SharedConfig.passcodeHash.length();
        int i19 = 7;
        LongSparseArray longSparseArray9 = new LongSparseArray();
        int size3 = arrayList6.size();
        int i20 = 0;
        while (i20 < size3 && arrayList8.size() < i19) {
            DialogKey dialogKey = (DialogKey) arrayList6.get(i20);
            ArrayList arrayList9 = arrayList6;
            Notification notification2 = build2;
            long j5 = dialogKey.dialogId;
            int i21 = dialogKey.topicId;
            ArrayList arrayList10 = (ArrayList) longSparseArray7.get(j5);
            int i22 = i20;
            int id = ((MessageObject) arrayList10.get(0)).getId();
            Integer num3 = (Integer) longSparseArray8.get(j5);
            ArrayList arrayList11 = arrayList8;
            if (num3 == null) {
                num3 = Integer.valueOf(((int) j5) + ((int) (j5 >> 32)));
            } else {
                longSparseArray8.remove(j5);
            }
            Integer num4 = num3;
            MessageObject messageObject4 = (MessageObject) arrayList10.get(0);
            LongSparseArray longSparseArray10 = longSparseArray8;
            int i23 = 0;
            for (int i24 = 0; i24 < arrayList10.size(); i24++) {
                if (i23 < ((MessageObject) arrayList10.get(i24)).messageOwner.date) {
                    i23 = ((MessageObject) arrayList10.get(i24)).messageOwner.date;
                }
            }
            if (!DialogObject.isEncryptedDialog(j5)) {
                z5 = j5 != 777000;
                if (DialogObject.isUserDialog(j5)) {
                    i7 = size3;
                    TLRPC$User user = getMessagesController().getUser(Long.valueOf(j5));
                    if (user == null) {
                        if (messageObject4.isFcmMessage()) {
                            str15 = messageObject4.localName;
                            i6 = i23;
                            tLRPC$User5 = user;
                        } else {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.w("not found user to show dialog notification " + j5);
                            }
                            notificationsController2 = this;
                            j2 = clientUserId;
                            longSparseArray2 = longSparseArray9;
                            notification = notification2;
                            arrayList2 = arrayList9;
                            i8 = i22;
                            arrayList3 = arrayList11;
                            longSparseArray3 = longSparseArray10;
                            i9 = i7;
                            z8 = z12;
                            longSparseArray4 = longSparseArray7;
                        }
                    } else {
                        String userName = UserObject.getUserName(user);
                        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto3 = user.photo;
                        if (tLRPC$UserProfilePhoto3 == null || (tLRPC$FileLocation5 = tLRPC$UserProfilePhoto3.photo_small) == null) {
                            i6 = i23;
                            tLRPC$User5 = user;
                        } else {
                            i6 = i23;
                            tLRPC$User5 = user;
                            if (tLRPC$FileLocation5.volume_id != 0 && tLRPC$FileLocation5.local_id != 0) {
                                tLRPC$FileLocation = tLRPC$FileLocation5;
                                str15 = userName;
                                if (!UserObject.isReplyUser(j5)) {
                                    str15 = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                                } else if (j5 == clientUserId) {
                                    str15 = LocaleController.getString("MessageScheduledReminderNotification", R.string.MessageScheduledReminderNotification);
                                }
                                i5 = id;
                                string = str15;
                                messageObject = messageObject4;
                                num = num4;
                                tLRPC$User2 = tLRPC$User5;
                                tLRPC$Chat = null;
                                z6 = false;
                                z7 = false;
                                z4 = z12;
                                String str16 = "NotificationHiddenChatName";
                                if (z13) {
                                    longSparseArray5 = longSparseArray7;
                                } else {
                                    if (DialogObject.isChatDialog(j5)) {
                                        string2 = LocaleController.getString("NotificationHiddenChatName", R.string.NotificationHiddenChatName);
                                    } else {
                                        string2 = LocaleController.getString("NotificationHiddenName", R.string.NotificationHiddenName);
                                    }
                                    string = string2;
                                    longSparseArray5 = longSparseArray7;
                                    z5 = false;
                                    tLRPC$FileLocation = null;
                                }
                                if (tLRPC$FileLocation == null) {
                                    tLRPC$User3 = tLRPC$User2;
                                    file = getFileLoader().getPathToAttach(tLRPC$FileLocation, true);
                                    z9 = z4;
                                    if (Build.VERSION.SDK_INT < 28) {
                                        str3 = "NotificationHiddenName";
                                        bitmap3 = null;
                                        BitmapDrawable imageFromMemory = ImageLoader.getInstance().getImageFromMemory(tLRPC$FileLocation, null, "50_50");
                                        if (imageFromMemory != null) {
                                            bitmap = imageFromMemory.getBitmap();
                                        } else {
                                            try {
                                                if (file.exists()) {
                                                    float dp = 160.0f / AndroidUtilities.dp(50.0f);
                                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                                    options.inSampleSize = dp < 1.0f ? 1 : (int) dp;
                                                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                                                }
                                            } catch (Throwable unused) {
                                            }
                                        }
                                    } else {
                                        str3 = "NotificationHiddenName";
                                        bitmap3 = null;
                                    }
                                    bitmap = bitmap3;
                                } else {
                                    tLRPC$User3 = tLRPC$User2;
                                    str3 = "NotificationHiddenName";
                                    z9 = z4;
                                    bitmap = null;
                                    file = null;
                                }
                                if (tLRPC$Chat != null) {
                                    Person.Builder name = new Person.Builder().setName(string);
                                    if (file != null && file.exists() && Build.VERSION.SDK_INT >= 28) {
                                        loadRoundAvatar(file, name);
                                    }
                                    longSparseArray9.put(-tLRPC$Chat.id, name.build());
                                }
                                if (!(z7 || z6) || !z5 || SharedConfig.isWaitingForPasscodeEnter || clientUserId == j5 || UserObject.isReplyUser(j5)) {
                                    str4 = "max_id";
                                    tLRPC$Chat2 = tLRPC$Chat;
                                    file2 = file;
                                    str5 = "dialog_id";
                                    i10 = i5;
                                    notificationsController3 = this;
                                    bitmap2 = bitmap;
                                    build = null;
                                } else {
                                    file2 = file;
                                    tLRPC$Chat2 = tLRPC$Chat;
                                    Intent intent = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                                    intent.putExtra("dialog_id", j5);
                                    int i25 = i5;
                                    intent.putExtra("max_id", i25);
                                    intent.putExtra("topic_id", i21);
                                    notificationsController3 = this;
                                    bitmap2 = bitmap;
                                    intent.putExtra("currentAccount", notificationsController3.currentAccount);
                                    str4 = "max_id";
                                    i10 = i25;
                                    PendingIntent broadcast = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent, 167772160);
                                    RemoteInput build3 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                                    if (!DialogObject.isChatDialog(j5)) {
                                        str5 = "dialog_id";
                                        formatString = LocaleController.formatString("ReplyToGroup", R.string.ReplyToGroup, string);
                                    } else {
                                        str5 = "dialog_id";
                                        formatString = LocaleController.formatString("ReplyToUser", R.string.ReplyToUser, string);
                                    }
                                    build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build3).setShowsUserInterface(false).build();
                                }
                                num2 = notificationsController3.pushDialogs.get(j5);
                                if (num2 == null) {
                                    num2 = 0;
                                }
                                int max = Math.max(num2.intValue(), arrayList10.size());
                                format = (max > 1 || Build.VERSION.SDK_INT >= 28) ? string : String.format("%1$s (%2$d)", string, Integer.valueOf(max));
                                person = (Person) longSparseArray9.get(clientUserId);
                                if (Build.VERSION.SDK_INT >= 28 || person != null) {
                                    str6 = "currentAccount";
                                    messageObject2 = messageObject;
                                    person = person;
                                } else {
                                    TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(clientUserId));
                                    if (user2 == null) {
                                        user2 = getUserConfig().getCurrentUser();
                                    }
                                    if (user2 != null) {
                                        try {
                                            tLRPC$UserProfilePhoto2 = user2.photo;
                                        } catch (Throwable th) {
                                            th = th;
                                            str6 = "currentAccount";
                                        }
                                        if (tLRPC$UserProfilePhoto2 != null && (tLRPC$FileLocation3 = tLRPC$UserProfilePhoto2.photo_small) != null) {
                                            person4 = person;
                                            str6 = "currentAccount";
                                            try {
                                            } catch (Throwable th2) {
                                                th = th2;
                                                person = person4;
                                            }
                                            if (tLRPC$FileLocation3.volume_id != 0 && tLRPC$FileLocation3.local_id != 0) {
                                                Person.Builder name2 = new Person.Builder().setName(LocaleController.getString("FromYou", R.string.FromYou));
                                                loadRoundAvatar(getFileLoader().getPathToAttach(user2.photo.photo_small, true), name2);
                                                Person build4 = name2.build();
                                                try {
                                                    longSparseArray9.put(clientUserId, build4);
                                                    person = build4;
                                                } catch (Throwable th3) {
                                                    th = th3;
                                                    person = build4;
                                                    FileLog.e(th);
                                                    messageObject2 = messageObject;
                                                    boolean z14 = !(messageObject2.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest);
                                                    String str17 = "";
                                                    if (person == null) {
                                                    }
                                                    messagingStyle = new NotificationCompat.MessagingStyle("");
                                                    NotificationCompat.MessagingStyle messagingStyle3 = messagingStyle;
                                                    i11 = Build.VERSION.SDK_INT;
                                                    if (i11 >= 28) {
                                                    }
                                                    messagingStyle3.setConversationTitle(format);
                                                    messagingStyle3.setGroupConversation(i11 >= 28 || (!z7 && DialogObject.isChatDialog(j5)) || UserObject.isReplyUser(j5));
                                                    StringBuilder sb2 = new StringBuilder();
                                                    action = build;
                                                    String[] strArr2 = new String[1];
                                                    NotificationCompat.MessagingStyle messagingStyle4 = messagingStyle3;
                                                    boolean[] zArr2 = new boolean[1];
                                                    size = arrayList10.size() - 1;
                                                    int i26 = 0;
                                                    ArrayList<TLRPC$TL_keyboardButtonRow> arrayList12 = null;
                                                    while (size >= 0) {
                                                    }
                                                    ArrayList arrayList13 = arrayList10;
                                                    long j6 = clientUserId;
                                                    String str18 = string;
                                                    LongSparseArray longSparseArray11 = longSparseArray9;
                                                    StringBuilder sb3 = sb2;
                                                    NotificationCompat.MessagingStyle messagingStyle5 = messagingStyle4;
                                                    Intent intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                    intent2.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                    intent2.setFlags(ConnectionsManager.FileTypeFile);
                                                    intent2.addCategory("android.intent.category.LAUNCHER");
                                                    if (DialogObject.isEncryptedDialog(j5)) {
                                                    }
                                                    FileLog.d("show extra notifications chatId " + j5 + " topicId " + i21);
                                                    if (i21 != 0) {
                                                    }
                                                    String str19 = str6;
                                                    intent2.putExtra(str19, notificationsController3.currentAccount);
                                                    PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1140850688);
                                                    NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
                                                    if (action != null) {
                                                    }
                                                    Intent intent3 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                                    intent3.addFlags(32);
                                                    intent3.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                                    intent3.putExtra(str5, j5);
                                                    int i27 = i10;
                                                    intent3.putExtra(str4, i27);
                                                    intent3.putExtra(str19, notificationsController3.currentAccount);
                                                    NotificationCompat.Action build5 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent3, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                                    if (!DialogObject.isEncryptedDialog(j5)) {
                                                    }
                                                    if (str7 != null) {
                                                    }
                                                    wearableExtender.setBridgeTag("tgaccount" + j6);
                                                    long j7 = ((long) ((MessageObject) arrayList13.get(0)).messageOwner.date) * 1000;
                                                    NotificationCompat.Builder category = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str18).setSmallIcon(R.drawable.notification).setContentText(sb3.toString()).setAutoCancel(true).setNumber(arrayList13.size()).setColor(-15618822).setGroupSummary(false).setWhen(j7).setShowWhen(true).setStyle(messagingStyle5).setContentIntent(activity).extend(wearableExtender).setSortKey(String.valueOf(Long.MAX_VALUE - j7)).setCategory("msg");
                                                    Intent intent4 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                    intent4.putExtra("messageDate", i6);
                                                    intent4.putExtra("dialogId", j5);
                                                    intent4.putExtra(str19, notificationsController3.currentAccount);
                                                    category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent4, 167772160));
                                                    if (z9) {
                                                    }
                                                    if (action != null) {
                                                    }
                                                    if (!z13) {
                                                    }
                                                    if (arrayList9.size() != 1) {
                                                    }
                                                    if (DialogObject.isEncryptedDialog(j5)) {
                                                    }
                                                    if (bitmap2 != null) {
                                                    }
                                                    if (!AndroidUtilities.needShowPasscode(false)) {
                                                    }
                                                    if (tLRPC$Chat2 == null) {
                                                    }
                                                    tLRPC$User4 = tLRPC$User3;
                                                    boolean z15 = z9;
                                                    if (Build.VERSION.SDK_INT >= 26) {
                                                    }
                                                    j2 = j6;
                                                    i8 = i22;
                                                    z8 = z15;
                                                    longSparseArray2 = longSparseArray11;
                                                    i9 = i7;
                                                    longSparseArray4 = longSparseArray5;
                                                    arrayList2 = arrayList9;
                                                    notification = notification2;
                                                    longSparseArray3 = longSparseArray10;
                                                    arrayList3 = arrayList11;
                                                    arrayList3.add(new 1NotificationHolder(num.intValue(), j5, i21, str18, tLRPC$User4, tLRPC$Chat2, category, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                                                    notificationsController2 = this;
                                                    notificationsController2.wearNotificationsIds.put(j5, num);
                                                    i20 = i8 + 1;
                                                    arrayList8 = arrayList3;
                                                    clientUserId = j2;
                                                    z12 = z8;
                                                    size3 = i9;
                                                    longSparseArray7 = longSparseArray4;
                                                    arrayList6 = arrayList2;
                                                    longSparseArray8 = longSparseArray3;
                                                    longSparseArray9 = longSparseArray2;
                                                    build2 = notification;
                                                    i19 = 7;
                                                    notificationsController4 = notificationsController2;
                                                }
                                                messageObject2 = messageObject;
                                            }
                                            person = person4;
                                            messageObject2 = messageObject;
                                        }
                                    }
                                    person4 = person;
                                    str6 = "currentAccount";
                                    person = person4;
                                    messageObject2 = messageObject;
                                }
                                boolean z142 = !(messageObject2.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest);
                                String str172 = "";
                                if (person == null && z142) {
                                    messagingStyle = new NotificationCompat.MessagingStyle(person);
                                } else {
                                    messagingStyle = new NotificationCompat.MessagingStyle("");
                                }
                                NotificationCompat.MessagingStyle messagingStyle32 = messagingStyle;
                                i11 = Build.VERSION.SDK_INT;
                                if (i11 >= 28 || ((DialogObject.isChatDialog(j5) && !z7) || UserObject.isReplyUser(j5))) {
                                    messagingStyle32.setConversationTitle(format);
                                }
                                messagingStyle32.setGroupConversation(i11 >= 28 || (!z7 && DialogObject.isChatDialog(j5)) || UserObject.isReplyUser(j5));
                                StringBuilder sb22 = new StringBuilder();
                                action = build;
                                String[] strArr22 = new String[1];
                                NotificationCompat.MessagingStyle messagingStyle42 = messagingStyle32;
                                boolean[] zArr22 = new boolean[1];
                                size = arrayList10.size() - 1;
                                int i262 = 0;
                                ArrayList<TLRPC$TL_keyboardButtonRow> arrayList122 = null;
                                while (size >= 0) {
                                    ArrayList arrayList14 = arrayList10;
                                    MessageObject messageObject5 = (MessageObject) arrayList10.get(size);
                                    int i28 = size;
                                    if (i21 != MessageObject.getTopicId(messageObject5.messageOwner, getMessagesController().isForum(messageObject5))) {
                                        j3 = clientUserId;
                                        str9 = string;
                                        str13 = str16;
                                        longSparseArray6 = longSparseArray9;
                                        strArr = strArr22;
                                        str14 = str172;
                                        sb = sb22;
                                        zArr = zArr22;
                                    } else {
                                        String shortStringForMessage = notificationsController3.getShortStringForMessage(messageObject5, strArr22, zArr22);
                                        if (j5 == clientUserId) {
                                            strArr22[0] = string;
                                        } else if (DialogObject.isChatDialog(j5)) {
                                            str9 = string;
                                            if (messageObject5.messageOwner.from_scheduled) {
                                                strArr22[0] = LocaleController.getString("NotificationMessageScheduledName", R.string.NotificationMessageScheduledName);
                                            }
                                            if (shortStringForMessage != null) {
                                                if (BuildVars.LOGS_ENABLED) {
                                                    StringBuilder sb4 = new StringBuilder();
                                                    sb4.append("message text is null for ");
                                                    sb4.append(messageObject5.getId());
                                                    sb4.append(" did = ");
                                                    zArr = zArr22;
                                                    sb4.append(messageObject5.getDialogId());
                                                    FileLog.w(sb4.toString());
                                                } else {
                                                    zArr = zArr22;
                                                }
                                                j3 = clientUserId;
                                                str13 = str16;
                                                longSparseArray6 = longSparseArray9;
                                                strArr = strArr22;
                                                str14 = str172;
                                                sb = sb22;
                                            } else {
                                                zArr = zArr22;
                                                if (sb22.length() > 0) {
                                                    sb22.append("\n\n");
                                                }
                                                if (j5 != clientUserId) {
                                                    j3 = clientUserId;
                                                    if (messageObject5.messageOwner.from_scheduled && DialogObject.isUserDialog(j5)) {
                                                        shortStringForMessage = String.format("%1$s: %2$s", LocaleController.getString("NotificationMessageScheduledName", R.string.NotificationMessageScheduledName), shortStringForMessage);
                                                        sb22.append(shortStringForMessage);
                                                        String str20 = shortStringForMessage;
                                                        if (!DialogObject.isUserDialog(j5)) {
                                                            if (z7) {
                                                                j4 = -j5;
                                                            } else if (DialogObject.isChatDialog(j5)) {
                                                                j4 = messageObject5.getSenderId();
                                                            }
                                                            str10 = str172;
                                                            sb = sb22;
                                                            person2 = (Person) longSparseArray9.get((i21 << 16) + j4);
                                                            if (strArr22[0] == null) {
                                                                if (z13) {
                                                                    if (DialogObject.isChatDialog(j5)) {
                                                                        if (z7) {
                                                                            if (Build.VERSION.SDK_INT > 27) {
                                                                                str12 = LocaleController.getString(str16, R.string.NotificationHiddenChatName);
                                                                            }
                                                                        } else {
                                                                            str12 = LocaleController.getString("NotificationHiddenChatUserName", R.string.NotificationHiddenChatUserName);
                                                                        }
                                                                        str11 = str3;
                                                                    } else if (Build.VERSION.SDK_INT > 27) {
                                                                        str11 = str3;
                                                                        str12 = LocaleController.getString(str11, R.string.NotificationHiddenName);
                                                                    }
                                                                    zArr22 = zArr;
                                                                    messagingStyle42 = messagingStyle2;
                                                                    str3 = str11;
                                                                    str172 = str14;
                                                                    arrayList10 = arrayList14;
                                                                    string = str9;
                                                                    clientUserId = j3;
                                                                    sb22 = sb;
                                                                    strArr22 = strArr;
                                                                    str16 = str13;
                                                                    longSparseArray9 = longSparseArray6;
                                                                    size = i28 - 1;
                                                                }
                                                                str11 = str3;
                                                                str12 = str10;
                                                            } else {
                                                                str11 = str3;
                                                                str12 = strArr22[0];
                                                            }
                                                            String str21 = str10;
                                                            if (person2 == null && TextUtils.equals(person2.getName(), str12)) {
                                                                person3 = person2;
                                                                str13 = str16;
                                                                strArr = strArr22;
                                                            } else {
                                                                Person.Builder name3 = new Person.Builder().setName(str12);
                                                                if (zArr[0] || DialogObject.isEncryptedDialog(j5) || Build.VERSION.SDK_INT < 28) {
                                                                    str13 = str16;
                                                                    strArr = strArr22;
                                                                } else {
                                                                    if (DialogObject.isUserDialog(j5) || z7) {
                                                                        str13 = str16;
                                                                        strArr = strArr22;
                                                                        file3 = file2;
                                                                    } else {
                                                                        String[] strArr3 = strArr22;
                                                                        long senderId = messageObject5.getSenderId();
                                                                        strArr = strArr3;
                                                                        str13 = str16;
                                                                        TLRPC$User user3 = getMessagesController().getUser(Long.valueOf(senderId));
                                                                        if (user3 == null && (user3 = getMessagesStorage().getUserSync(senderId)) != null) {
                                                                            getMessagesController().putUser(user3, true);
                                                                        }
                                                                        file3 = (user3 == null || (tLRPC$UserProfilePhoto = user3.photo) == null || (tLRPC$FileLocation2 = tLRPC$UserProfilePhoto.photo_small) == null || tLRPC$FileLocation2.volume_id == 0 || tLRPC$FileLocation2.local_id == 0) ? null : getFileLoader().getPathToAttach(user3.photo.photo_small, true);
                                                                    }
                                                                    loadRoundAvatar(file3, name3);
                                                                }
                                                                Person build6 = name3.build();
                                                                longSparseArray9.put(j4, build6);
                                                                person3 = build6;
                                                            }
                                                            if (!DialogObject.isEncryptedDialog(j5)) {
                                                                if (!zArr[0] || Build.VERSION.SDK_INT < 28 || ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).isLowRamDevice() || z13 || messageObject5.isSecretMedia() || !(messageObject5.type == 1 || messageObject5.isSticker())) {
                                                                    longSparseArray6 = longSparseArray9;
                                                                    str14 = str21;
                                                                } else {
                                                                    File pathToMessage = getFileLoader().getPathToMessage(messageObject5.messageOwner);
                                                                    if (pathToMessage.exists() && messageObject5.hasMediaSpoilers()) {
                                                                        file5 = new File(pathToMessage.getParentFile(), pathToMessage.getName() + ".blur.jpg");
                                                                        if (file5.exists()) {
                                                                            longSparseArray6 = longSparseArray9;
                                                                            file6 = pathToMessage;
                                                                        } else {
                                                                            try {
                                                                                Bitmap decodeFile = BitmapFactory.decodeFile(pathToMessage.getAbsolutePath());
                                                                                Bitmap stackBlurBitmapMax = Utilities.stackBlurBitmapMax(decodeFile);
                                                                                decodeFile.recycle();
                                                                                createScaledBitmap = Bitmap.createScaledBitmap(stackBlurBitmapMax, decodeFile.getWidth(), decodeFile.getHeight(), true);
                                                                                Utilities.stackBlurBitmap(createScaledBitmap, 5);
                                                                                stackBlurBitmapMax.recycle();
                                                                                canvas = new Canvas(createScaledBitmap);
                                                                                longSparseArray6 = longSparseArray9;
                                                                            } catch (Exception e) {
                                                                                e = e;
                                                                                longSparseArray6 = longSparseArray9;
                                                                            }
                                                                            try {
                                                                                notificationsController3.mediaSpoilerEffect.setColor(ColorUtils.setAlphaComponent(-1, (int) (Color.alpha(-1) * 0.325f)));
                                                                                file6 = pathToMessage;
                                                                            } catch (Exception e2) {
                                                                                e = e2;
                                                                                file6 = pathToMessage;
                                                                                FileLog.e(e);
                                                                                file4 = file6;
                                                                                NotificationCompat.MessagingStyle.Message message = new NotificationCompat.MessagingStyle.Message(str20, messageObject5.messageOwner.date * 1000, person3);
                                                                                String str22 = messageObject5.isSticker() ? "image/webp" : "image/jpeg";
                                                                                if (file4.exists()) {
                                                                                }
                                                                                if (uriForFile != null) {
                                                                                }
                                                                                messagingStyle2 = messagingStyle42;
                                                                                z10 = false;
                                                                                if (!z10) {
                                                                                }
                                                                                if (zArr[0]) {
                                                                                }
                                                                                if (j5 != 777000) {
                                                                                }
                                                                                zArr22 = zArr;
                                                                                messagingStyle42 = messagingStyle2;
                                                                                str3 = str11;
                                                                                str172 = str14;
                                                                                arrayList10 = arrayList14;
                                                                                string = str9;
                                                                                clientUserId = j3;
                                                                                sb22 = sb;
                                                                                strArr22 = strArr;
                                                                                str16 = str13;
                                                                                longSparseArray9 = longSparseArray6;
                                                                                size = i28 - 1;
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
                                                                                NotificationCompat.MessagingStyle.Message message2 = new NotificationCompat.MessagingStyle.Message(str20, messageObject5.messageOwner.date * 1000, person3);
                                                                                String str222 = messageObject5.isSticker() ? "image/webp" : "image/jpeg";
                                                                                if (file4.exists()) {
                                                                                }
                                                                                if (uriForFile != null) {
                                                                                }
                                                                                messagingStyle2 = messagingStyle42;
                                                                                z10 = false;
                                                                                if (!z10) {
                                                                                }
                                                                                if (zArr[0]) {
                                                                                }
                                                                                if (j5 != 777000) {
                                                                                }
                                                                                zArr22 = zArr;
                                                                                messagingStyle42 = messagingStyle2;
                                                                                str3 = str11;
                                                                                str172 = str14;
                                                                                arrayList10 = arrayList14;
                                                                                string = str9;
                                                                                clientUserId = j3;
                                                                                sb22 = sb;
                                                                                strArr22 = strArr;
                                                                                str16 = str13;
                                                                                longSparseArray9 = longSparseArray6;
                                                                                size = i28 - 1;
                                                                            }
                                                                        }
                                                                        file4 = file6;
                                                                    } else {
                                                                        longSparseArray6 = longSparseArray9;
                                                                        file4 = pathToMessage;
                                                                        file5 = null;
                                                                    }
                                                                    NotificationCompat.MessagingStyle.Message message22 = new NotificationCompat.MessagingStyle.Message(str20, messageObject5.messageOwner.date * 1000, person3);
                                                                    String str2222 = messageObject5.isSticker() ? "image/webp" : "image/jpeg";
                                                                    if (file4.exists()) {
                                                                        try {
                                                                            uriForFile = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", file4);
                                                                            str14 = str21;
                                                                        } catch (Exception e4) {
                                                                            FileLog.e(e4);
                                                                        }
                                                                    } else {
                                                                        if (getFileLoader().isLoadingFile(file4.getName())) {
                                                                            Uri.Builder appendPath = new Uri.Builder().scheme("content").authority(NotificationImageProvider.getAuthority()).appendPath("msg_media_raw");
                                                                            StringBuilder sb5 = new StringBuilder();
                                                                            sb5.append(notificationsController3.currentAccount);
                                                                            str14 = str21;
                                                                            sb5.append(str14);
                                                                            uriForFile = appendPath.appendPath(sb5.toString()).appendPath(file4.getName()).appendQueryParameter("final_path", file4.getAbsolutePath()).build();
                                                                        }
                                                                        str14 = str21;
                                                                        uriForFile = null;
                                                                    }
                                                                    if (uriForFile != null) {
                                                                        message22.setData(str2222, uriForFile);
                                                                        messagingStyle2 = messagingStyle42;
                                                                        messagingStyle2.addMessage(message22);
                                                                        ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile, 1);
                                                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda4
                                                                            @Override // java.lang.Runnable
                                                                            public final void run() {
                                                                                NotificationsController.lambda$showExtraNotifications$34(uriForFile, file5);
                                                                            }
                                                                        }, 20000L);
                                                                        if (!TextUtils.isEmpty(messageObject5.caption)) {
                                                                            messagingStyle2.addMessage(messageObject5.caption, messageObject5.messageOwner.date * 1000, person3);
                                                                        }
                                                                        z10 = true;
                                                                        if (!z10) {
                                                                            messagingStyle2.addMessage(str20, messageObject5.messageOwner.date * 1000, person3);
                                                                        }
                                                                        if (zArr[0] && !z13 && messageObject5.isVoice()) {
                                                                            messages = messagingStyle2.getMessages();
                                                                            if (!messages.isEmpty()) {
                                                                                File pathToMessage2 = getFileLoader().getPathToMessage(messageObject5.messageOwner);
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
                                                                    }
                                                                }
                                                                messagingStyle2 = messagingStyle42;
                                                                z10 = false;
                                                                if (!z10) {
                                                                }
                                                                if (zArr[0]) {
                                                                    messages = messagingStyle2.getMessages();
                                                                    if (!messages.isEmpty()) {
                                                                    }
                                                                }
                                                            } else {
                                                                longSparseArray6 = longSparseArray9;
                                                                str14 = str21;
                                                                messagingStyle2 = messagingStyle42;
                                                                messagingStyle2.addMessage(str20, messageObject5.messageOwner.date * 1000, person3);
                                                            }
                                                            if (j5 != 777000 && (tLRPC$ReplyMarkup = messageObject5.messageOwner.reply_markup) != null) {
                                                                arrayList122 = tLRPC$ReplyMarkup.rows;
                                                                i262 = messageObject5.getId();
                                                            }
                                                            zArr22 = zArr;
                                                            messagingStyle42 = messagingStyle2;
                                                            str3 = str11;
                                                            str172 = str14;
                                                            arrayList10 = arrayList14;
                                                            string = str9;
                                                            clientUserId = j3;
                                                            sb22 = sb;
                                                            strArr22 = strArr;
                                                            str16 = str13;
                                                            longSparseArray9 = longSparseArray6;
                                                            size = i28 - 1;
                                                        }
                                                        str10 = str172;
                                                        sb = sb22;
                                                        j4 = j5;
                                                        person2 = (Person) longSparseArray9.get((i21 << 16) + j4);
                                                        if (strArr22[0] == null) {
                                                        }
                                                        String str212 = str10;
                                                        if (person2 == null) {
                                                        }
                                                        Person.Builder name32 = new Person.Builder().setName(str12);
                                                        if (zArr[0]) {
                                                        }
                                                        str13 = str16;
                                                        strArr = strArr22;
                                                        Person build62 = name32.build();
                                                        longSparseArray9.put(j4, build62);
                                                        person3 = build62;
                                                        if (!DialogObject.isEncryptedDialog(j5)) {
                                                        }
                                                        if (j5 != 777000) {
                                                            arrayList122 = tLRPC$ReplyMarkup.rows;
                                                            i262 = messageObject5.getId();
                                                        }
                                                        zArr22 = zArr;
                                                        messagingStyle42 = messagingStyle2;
                                                        str3 = str11;
                                                        str172 = str14;
                                                        arrayList10 = arrayList14;
                                                        string = str9;
                                                        clientUserId = j3;
                                                        sb22 = sb;
                                                        strArr22 = strArr;
                                                        str16 = str13;
                                                        longSparseArray9 = longSparseArray6;
                                                        size = i28 - 1;
                                                    }
                                                } else {
                                                    j3 = clientUserId;
                                                }
                                                if (strArr22[0] != null) {
                                                    sb22.append(String.format("%1$s: %2$s", strArr22[0], shortStringForMessage));
                                                } else {
                                                    sb22.append(shortStringForMessage);
                                                }
                                                String str202 = shortStringForMessage;
                                                if (!DialogObject.isUserDialog(j5)) {
                                                }
                                                str10 = str172;
                                                sb = sb22;
                                                j4 = j5;
                                                person2 = (Person) longSparseArray9.get((i21 << 16) + j4);
                                                if (strArr22[0] == null) {
                                                }
                                                String str2122 = str10;
                                                if (person2 == null) {
                                                }
                                                Person.Builder name322 = new Person.Builder().setName(str12);
                                                if (zArr[0]) {
                                                }
                                                str13 = str16;
                                                strArr = strArr22;
                                                Person build622 = name322.build();
                                                longSparseArray9.put(j4, build622);
                                                person3 = build622;
                                                if (!DialogObject.isEncryptedDialog(j5)) {
                                                }
                                                if (j5 != 777000) {
                                                }
                                                zArr22 = zArr;
                                                messagingStyle42 = messagingStyle2;
                                                str3 = str11;
                                                str172 = str14;
                                                arrayList10 = arrayList14;
                                                string = str9;
                                                clientUserId = j3;
                                                sb22 = sb;
                                                strArr22 = strArr;
                                                str16 = str13;
                                                longSparseArray9 = longSparseArray6;
                                                size = i28 - 1;
                                            }
                                        }
                                        str9 = string;
                                        if (shortStringForMessage != null) {
                                        }
                                    }
                                    str11 = str3;
                                    messagingStyle2 = messagingStyle42;
                                    zArr22 = zArr;
                                    messagingStyle42 = messagingStyle2;
                                    str3 = str11;
                                    str172 = str14;
                                    arrayList10 = arrayList14;
                                    string = str9;
                                    clientUserId = j3;
                                    sb22 = sb;
                                    strArr22 = strArr;
                                    str16 = str13;
                                    longSparseArray9 = longSparseArray6;
                                    size = i28 - 1;
                                }
                                ArrayList arrayList132 = arrayList10;
                                long j62 = clientUserId;
                                String str182 = string;
                                LongSparseArray longSparseArray112 = longSparseArray9;
                                StringBuilder sb32 = sb22;
                                NotificationCompat.MessagingStyle messagingStyle52 = messagingStyle42;
                                Intent intent22 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                intent22.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                intent22.setFlags(ConnectionsManager.FileTypeFile);
                                intent22.addCategory("android.intent.category.LAUNCHER");
                                if (DialogObject.isEncryptedDialog(j5)) {
                                    intent22.putExtra("encId", DialogObject.getEncryptedChatId(j5));
                                } else if (DialogObject.isUserDialog(j5)) {
                                    intent22.putExtra("userId", j5);
                                } else {
                                    intent22.putExtra("chatId", -j5);
                                }
                                FileLog.d("show extra notifications chatId " + j5 + " topicId " + i21);
                                if (i21 != 0) {
                                    intent22.putExtra("topicId", i21);
                                }
                                String str192 = str6;
                                intent22.putExtra(str192, notificationsController3.currentAccount);
                                PendingIntent activity2 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22, 1140850688);
                                NotificationCompat.WearableExtender wearableExtender2 = new NotificationCompat.WearableExtender();
                                if (action != null) {
                                    wearableExtender2.addAction(action);
                                }
                                Intent intent32 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                intent32.addFlags(32);
                                intent32.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                intent32.putExtra(str5, j5);
                                int i272 = i10;
                                intent32.putExtra(str4, i272);
                                intent32.putExtra(str192, notificationsController3.currentAccount);
                                NotificationCompat.Action build52 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent32, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                if (!DialogObject.isEncryptedDialog(j5)) {
                                    str7 = DialogObject.isUserDialog(j5) ? "tguser" + j5 + "_" + i272 : "tgchat" + (-j5) + "_" + i272;
                                } else if (j5 != globalSecretChatId) {
                                    str7 = "tgenc" + DialogObject.getEncryptedChatId(j5) + "_" + i272;
                                } else {
                                    str7 = null;
                                }
                                if (str7 != null) {
                                    wearableExtender2.setDismissalId(str7);
                                    NotificationCompat.WearableExtender wearableExtender3 = new NotificationCompat.WearableExtender();
                                    wearableExtender3.setDismissalId("summary_" + str7);
                                    builder.extend(wearableExtender3);
                                }
                                wearableExtender2.setBridgeTag("tgaccount" + j62);
                                long j72 = ((long) ((MessageObject) arrayList132.get(0)).messageOwner.date) * 1000;
                                NotificationCompat.Builder category2 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str182).setSmallIcon(R.drawable.notification).setContentText(sb32.toString()).setAutoCancel(true).setNumber(arrayList132.size()).setColor(-15618822).setGroupSummary(false).setWhen(j72).setShowWhen(true).setStyle(messagingStyle52).setContentIntent(activity2).extend(wearableExtender2).setSortKey(String.valueOf(Long.MAX_VALUE - j72)).setCategory("msg");
                                Intent intent42 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                intent42.putExtra("messageDate", i6);
                                intent42.putExtra("dialogId", j5);
                                intent42.putExtra(str192, notificationsController3.currentAccount);
                                category2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent42, 167772160));
                                if (z9) {
                                    category2.setGroup(notificationsController3.notificationGroup);
                                    category2.setGroupAlertBehavior(1);
                                }
                                if (action != null) {
                                    category2.addAction(action);
                                }
                                if (!z13) {
                                    category2.addAction(build52);
                                }
                                if (arrayList9.size() != 1 && !TextUtils.isEmpty(str)) {
                                    category2.setSubText(str);
                                }
                                if (DialogObject.isEncryptedDialog(j5)) {
                                    category2.setLocalOnly(true);
                                }
                                if (bitmap2 != null) {
                                    category2.setLargeIcon(bitmap2);
                                }
                                if (!AndroidUtilities.needShowPasscode(false) && !SharedConfig.isWaitingForPasscodeEnter && (arrayList4 = arrayList122) != null) {
                                    size2 = arrayList4.size();
                                    i12 = 0;
                                    while (i12 < size2) {
                                        TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow = arrayList4.get(i12);
                                        int size4 = tLRPC$TL_keyboardButtonRow.buttons.size();
                                        int i29 = 0;
                                        while (i29 < size4) {
                                            TLRPC$KeyboardButton tLRPC$KeyboardButton = tLRPC$TL_keyboardButtonRow.buttons.get(i29);
                                            if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonCallback) {
                                                i13 = size2;
                                                arrayList5 = arrayList4;
                                                Intent intent5 = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                                intent5.putExtra(str192, notificationsController3.currentAccount);
                                                intent5.putExtra("did", j5);
                                                byte[] bArr = tLRPC$KeyboardButton.data;
                                                if (bArr != null) {
                                                    intent5.putExtra("data", bArr);
                                                }
                                                int i30 = i262;
                                                intent5.putExtra("mid", i30);
                                                String str23 = tLRPC$KeyboardButton.text;
                                                Context context = ApplicationLoader.applicationContext;
                                                i14 = i30;
                                                int i31 = notificationsController3.lastButtonId;
                                                str8 = str192;
                                                notificationsController3.lastButtonId = i31 + 1;
                                                category2.addAction(0, str23, PendingIntent.getBroadcast(context, i31, intent5, 167772160));
                                            } else {
                                                i13 = size2;
                                                arrayList5 = arrayList4;
                                                str8 = str192;
                                                i14 = i262;
                                            }
                                            i29++;
                                            size2 = i13;
                                            arrayList4 = arrayList5;
                                            i262 = i14;
                                            str192 = str8;
                                        }
                                        i12++;
                                        str192 = str192;
                                    }
                                }
                                if (tLRPC$Chat2 == null || tLRPC$User3 == null) {
                                    tLRPC$User4 = tLRPC$User3;
                                } else {
                                    tLRPC$User4 = tLRPC$User3;
                                    String str24 = tLRPC$User4.phone;
                                    if (str24 != null && str24.length() > 0) {
                                        category2.addPerson("tel:+" + tLRPC$User4.phone);
                                    }
                                }
                                boolean z152 = z9;
                                if (Build.VERSION.SDK_INT >= 26) {
                                    notificationsController3.setNotificationChannel(notification2, category2, z152);
                                }
                                j2 = j62;
                                i8 = i22;
                                z8 = z152;
                                longSparseArray2 = longSparseArray112;
                                i9 = i7;
                                longSparseArray4 = longSparseArray5;
                                arrayList2 = arrayList9;
                                notification = notification2;
                                longSparseArray3 = longSparseArray10;
                                arrayList3 = arrayList11;
                                arrayList3.add(new 1NotificationHolder(num.intValue(), j5, i21, str182, tLRPC$User4, tLRPC$Chat2, category2, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                                notificationsController2 = this;
                                notificationsController2.wearNotificationsIds.put(j5, num);
                            }
                        }
                        str15 = userName;
                    }
                    tLRPC$FileLocation = null;
                    if (!UserObject.isReplyUser(j5)) {
                    }
                    i5 = id;
                    string = str15;
                    messageObject = messageObject4;
                    num = num4;
                    tLRPC$User2 = tLRPC$User5;
                    tLRPC$Chat = null;
                    z6 = false;
                    z7 = false;
                    z4 = z12;
                    String str162 = "NotificationHiddenChatName";
                    if (z13) {
                    }
                    if (tLRPC$FileLocation == null) {
                    }
                    if (tLRPC$Chat != null) {
                    }
                    if (z7) {
                    }
                    file2 = file;
                    tLRPC$Chat2 = tLRPC$Chat;
                    Intent intent6 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                    intent6.putExtra("dialog_id", j5);
                    int i252 = i5;
                    intent6.putExtra("max_id", i252);
                    intent6.putExtra("topic_id", i21);
                    notificationsController3 = this;
                    bitmap2 = bitmap;
                    intent6.putExtra("currentAccount", notificationsController3.currentAccount);
                    str4 = "max_id";
                    i10 = i252;
                    PendingIntent broadcast2 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent6, 167772160);
                    RemoteInput build32 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                    if (!DialogObject.isChatDialog(j5)) {
                    }
                    build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast2).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build32).setShowsUserInterface(false).build();
                    num2 = notificationsController3.pushDialogs.get(j5);
                    if (num2 == null) {
                    }
                    int max2 = Math.max(num2.intValue(), arrayList10.size());
                    if (max2 > 1) {
                    }
                    person = (Person) longSparseArray9.get(clientUserId);
                    if (Build.VERSION.SDK_INT >= 28) {
                    }
                    str6 = "currentAccount";
                    messageObject2 = messageObject;
                    person = person;
                    boolean z1422 = !(messageObject2.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest);
                    String str1722 = "";
                    if (person == null) {
                    }
                    messagingStyle = new NotificationCompat.MessagingStyle("");
                    NotificationCompat.MessagingStyle messagingStyle322 = messagingStyle;
                    i11 = Build.VERSION.SDK_INT;
                    if (i11 >= 28) {
                    }
                    messagingStyle322.setConversationTitle(format);
                    messagingStyle322.setGroupConversation(i11 >= 28 || (!z7 && DialogObject.isChatDialog(j5)) || UserObject.isReplyUser(j5));
                    StringBuilder sb222 = new StringBuilder();
                    action = build;
                    String[] strArr222 = new String[1];
                    NotificationCompat.MessagingStyle messagingStyle422 = messagingStyle322;
                    boolean[] zArr222 = new boolean[1];
                    size = arrayList10.size() - 1;
                    int i2622 = 0;
                    ArrayList<TLRPC$TL_keyboardButtonRow> arrayList1222 = null;
                    while (size >= 0) {
                    }
                    ArrayList arrayList1322 = arrayList10;
                    long j622 = clientUserId;
                    String str1822 = string;
                    LongSparseArray longSparseArray1122 = longSparseArray9;
                    StringBuilder sb322 = sb222;
                    NotificationCompat.MessagingStyle messagingStyle522 = messagingStyle422;
                    Intent intent222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    intent222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                    intent222.setFlags(ConnectionsManager.FileTypeFile);
                    intent222.addCategory("android.intent.category.LAUNCHER");
                    if (DialogObject.isEncryptedDialog(j5)) {
                    }
                    FileLog.d("show extra notifications chatId " + j5 + " topicId " + i21);
                    if (i21 != 0) {
                    }
                    String str1922 = str6;
                    intent222.putExtra(str1922, notificationsController3.currentAccount);
                    PendingIntent activity22 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222, 1140850688);
                    NotificationCompat.WearableExtender wearableExtender22 = new NotificationCompat.WearableExtender();
                    if (action != null) {
                    }
                    Intent intent322 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                    intent322.addFlags(32);
                    intent322.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                    intent322.putExtra(str5, j5);
                    int i2722 = i10;
                    intent322.putExtra(str4, i2722);
                    intent322.putExtra(str1922, notificationsController3.currentAccount);
                    NotificationCompat.Action build522 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent322, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                    if (!DialogObject.isEncryptedDialog(j5)) {
                    }
                    if (str7 != null) {
                    }
                    wearableExtender22.setBridgeTag("tgaccount" + j622);
                    long j722 = ((long) ((MessageObject) arrayList1322.get(0)).messageOwner.date) * 1000;
                    NotificationCompat.Builder category22 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str1822).setSmallIcon(R.drawable.notification).setContentText(sb322.toString()).setAutoCancel(true).setNumber(arrayList1322.size()).setColor(-15618822).setGroupSummary(false).setWhen(j722).setShowWhen(true).setStyle(messagingStyle522).setContentIntent(activity22).extend(wearableExtender22).setSortKey(String.valueOf(Long.MAX_VALUE - j722)).setCategory("msg");
                    Intent intent422 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                    intent422.putExtra("messageDate", i6);
                    intent422.putExtra("dialogId", j5);
                    intent422.putExtra(str1922, notificationsController3.currentAccount);
                    category22.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent422, 167772160));
                    if (z9) {
                    }
                    if (action != null) {
                    }
                    if (!z13) {
                    }
                    if (arrayList9.size() != 1) {
                    }
                    if (DialogObject.isEncryptedDialog(j5)) {
                    }
                    if (bitmap2 != null) {
                    }
                    if (!AndroidUtilities.needShowPasscode(false)) {
                        size2 = arrayList4.size();
                        i12 = 0;
                        while (i12 < size2) {
                        }
                    }
                    if (tLRPC$Chat2 == null) {
                    }
                    tLRPC$User4 = tLRPC$User3;
                    boolean z1522 = z9;
                    if (Build.VERSION.SDK_INT >= 26) {
                    }
                    j2 = j622;
                    i8 = i22;
                    z8 = z1522;
                    longSparseArray2 = longSparseArray1122;
                    i9 = i7;
                    longSparseArray4 = longSparseArray5;
                    arrayList2 = arrayList9;
                    notification = notification2;
                    longSparseArray3 = longSparseArray10;
                    arrayList3 = arrayList11;
                    arrayList3.add(new 1NotificationHolder(num.intValue(), j5, i21, str1822, tLRPC$User4, tLRPC$Chat2, category22, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                    notificationsController2 = this;
                    notificationsController2.wearNotificationsIds.put(j5, num);
                } else {
                    i6 = i23;
                    i7 = size3;
                    TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j5));
                    if (chat == null) {
                        if (messageObject4.isFcmMessage()) {
                            boolean isSupergroup = messageObject4.isSupergroup();
                            String str25 = messageObject4.localName;
                            z6 = isSupergroup;
                            i5 = id;
                            z7 = messageObject4.localChannel;
                            string = str25;
                            messageObject = messageObject4;
                            num = num4;
                            z5 = false;
                            tLRPC$User2 = null;
                            z4 = z12;
                            tLRPC$Chat = chat;
                            tLRPC$FileLocation = null;
                        } else {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.w("not found chat to show dialog notification " + j5);
                            }
                            notificationsController2 = this;
                            j2 = clientUserId;
                            longSparseArray2 = longSparseArray9;
                            notification = notification2;
                            arrayList2 = arrayList9;
                            i8 = i22;
                            arrayList3 = arrayList11;
                            longSparseArray3 = longSparseArray10;
                            i9 = i7;
                            z8 = z12;
                            longSparseArray4 = longSparseArray7;
                        }
                    } else {
                        boolean z16 = chat.megagroup;
                        if (!ChatObject.isChannel(chat) || chat.megagroup) {
                            z6 = z16;
                            z11 = false;
                        } else {
                            z6 = z16;
                            z11 = true;
                        }
                        String str26 = chat.title;
                        z7 = z11;
                        TLRPC$ChatPhoto tLRPC$ChatPhoto = chat.photo;
                        if (tLRPC$ChatPhoto == null || (tLRPC$FileLocation4 = tLRPC$ChatPhoto.photo_small) == null) {
                            messageObject = messageObject4;
                            num = num4;
                        } else {
                            messageObject = messageObject4;
                            num = num4;
                            if (tLRPC$FileLocation4.volume_id != 0) {
                            }
                        }
                        tLRPC$FileLocation4 = null;
                        if (i21 != 0) {
                            i5 = id;
                            z4 = z12;
                            if (getMessagesController().getTopicsController().findTopic(chat.id, i21) != null) {
                                string = findTopic.title + " in " + str26;
                                if (z5) {
                                    z5 = ChatObject.canSendPlain(chat);
                                }
                                tLRPC$User2 = null;
                                TLRPC$FileLocation tLRPC$FileLocation6 = tLRPC$FileLocation4;
                                tLRPC$Chat = chat;
                                tLRPC$FileLocation = tLRPC$FileLocation6;
                            }
                        } else {
                            i5 = id;
                            z4 = z12;
                        }
                        string = str26;
                        if (z5) {
                        }
                        tLRPC$User2 = null;
                        TLRPC$FileLocation tLRPC$FileLocation62 = tLRPC$FileLocation4;
                        tLRPC$Chat = chat;
                        tLRPC$FileLocation = tLRPC$FileLocation62;
                    }
                    String str1622 = "NotificationHiddenChatName";
                    if (z13) {
                    }
                    if (tLRPC$FileLocation == null) {
                    }
                    if (tLRPC$Chat != null) {
                    }
                    if (z7) {
                    }
                    file2 = file;
                    tLRPC$Chat2 = tLRPC$Chat;
                    Intent intent62 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                    intent62.putExtra("dialog_id", j5);
                    int i2522 = i5;
                    intent62.putExtra("max_id", i2522);
                    intent62.putExtra("topic_id", i21);
                    notificationsController3 = this;
                    bitmap2 = bitmap;
                    intent62.putExtra("currentAccount", notificationsController3.currentAccount);
                    str4 = "max_id";
                    i10 = i2522;
                    PendingIntent broadcast22 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent62, 167772160);
                    RemoteInput build322 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                    if (!DialogObject.isChatDialog(j5)) {
                    }
                    build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast22).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build322).setShowsUserInterface(false).build();
                    num2 = notificationsController3.pushDialogs.get(j5);
                    if (num2 == null) {
                    }
                    int max22 = Math.max(num2.intValue(), arrayList10.size());
                    if (max22 > 1) {
                    }
                    person = (Person) longSparseArray9.get(clientUserId);
                    if (Build.VERSION.SDK_INT >= 28) {
                    }
                    str6 = "currentAccount";
                    messageObject2 = messageObject;
                    person = person;
                    boolean z14222 = !(messageObject2.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest);
                    String str17222 = "";
                    if (person == null) {
                    }
                    messagingStyle = new NotificationCompat.MessagingStyle("");
                    NotificationCompat.MessagingStyle messagingStyle3222 = messagingStyle;
                    i11 = Build.VERSION.SDK_INT;
                    if (i11 >= 28) {
                    }
                    messagingStyle3222.setConversationTitle(format);
                    messagingStyle3222.setGroupConversation(i11 >= 28 || (!z7 && DialogObject.isChatDialog(j5)) || UserObject.isReplyUser(j5));
                    StringBuilder sb2222 = new StringBuilder();
                    action = build;
                    String[] strArr2222 = new String[1];
                    NotificationCompat.MessagingStyle messagingStyle4222 = messagingStyle3222;
                    boolean[] zArr2222 = new boolean[1];
                    size = arrayList10.size() - 1;
                    int i26222 = 0;
                    ArrayList<TLRPC$TL_keyboardButtonRow> arrayList12222 = null;
                    while (size >= 0) {
                    }
                    ArrayList arrayList13222 = arrayList10;
                    long j6222 = clientUserId;
                    String str18222 = string;
                    LongSparseArray longSparseArray11222 = longSparseArray9;
                    StringBuilder sb3222 = sb2222;
                    NotificationCompat.MessagingStyle messagingStyle5222 = messagingStyle4222;
                    Intent intent2222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    intent2222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                    intent2222.setFlags(ConnectionsManager.FileTypeFile);
                    intent2222.addCategory("android.intent.category.LAUNCHER");
                    if (DialogObject.isEncryptedDialog(j5)) {
                    }
                    FileLog.d("show extra notifications chatId " + j5 + " topicId " + i21);
                    if (i21 != 0) {
                    }
                    String str19222 = str6;
                    intent2222.putExtra(str19222, notificationsController3.currentAccount);
                    PendingIntent activity222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2222, 1140850688);
                    NotificationCompat.WearableExtender wearableExtender222 = new NotificationCompat.WearableExtender();
                    if (action != null) {
                    }
                    Intent intent3222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                    intent3222.addFlags(32);
                    intent3222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                    intent3222.putExtra(str5, j5);
                    int i27222 = i10;
                    intent3222.putExtra(str4, i27222);
                    intent3222.putExtra(str19222, notificationsController3.currentAccount);
                    NotificationCompat.Action build5222 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent3222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                    if (!DialogObject.isEncryptedDialog(j5)) {
                    }
                    if (str7 != null) {
                    }
                    wearableExtender222.setBridgeTag("tgaccount" + j6222);
                    long j7222 = ((long) ((MessageObject) arrayList13222.get(0)).messageOwner.date) * 1000;
                    NotificationCompat.Builder category222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str18222).setSmallIcon(R.drawable.notification).setContentText(sb3222.toString()).setAutoCancel(true).setNumber(arrayList13222.size()).setColor(-15618822).setGroupSummary(false).setWhen(j7222).setShowWhen(true).setStyle(messagingStyle5222).setContentIntent(activity222).extend(wearableExtender222).setSortKey(String.valueOf(Long.MAX_VALUE - j7222)).setCategory("msg");
                    Intent intent4222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                    intent4222.putExtra("messageDate", i6);
                    intent4222.putExtra("dialogId", j5);
                    intent4222.putExtra(str19222, notificationsController3.currentAccount);
                    category222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent4222, 167772160));
                    if (z9) {
                    }
                    if (action != null) {
                    }
                    if (!z13) {
                    }
                    if (arrayList9.size() != 1) {
                    }
                    if (DialogObject.isEncryptedDialog(j5)) {
                    }
                    if (bitmap2 != null) {
                    }
                    if (!AndroidUtilities.needShowPasscode(false)) {
                    }
                    if (tLRPC$Chat2 == null) {
                    }
                    tLRPC$User4 = tLRPC$User3;
                    boolean z15222 = z9;
                    if (Build.VERSION.SDK_INT >= 26) {
                    }
                    j2 = j6222;
                    i8 = i22;
                    z8 = z15222;
                    longSparseArray2 = longSparseArray11222;
                    i9 = i7;
                    longSparseArray4 = longSparseArray5;
                    arrayList2 = arrayList9;
                    notification = notification2;
                    longSparseArray3 = longSparseArray10;
                    arrayList3 = arrayList11;
                    arrayList3.add(new 1NotificationHolder(num.intValue(), j5, i21, str18222, tLRPC$User4, tLRPC$Chat2, category222, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                    notificationsController2 = this;
                    notificationsController2.wearNotificationsIds.put(j5, num);
                }
            } else {
                i5 = id;
                i6 = i23;
                i7 = size3;
                messageObject = messageObject4;
                num = num4;
                z4 = z12;
                if (j5 != globalSecretChatId) {
                    int encryptedChatId = DialogObject.getEncryptedChatId(j5);
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
                    j2 = clientUserId;
                    longSparseArray2 = longSparseArray9;
                    notification = notification2;
                    arrayList2 = arrayList9;
                    i8 = i22;
                    longSparseArray3 = longSparseArray10;
                    z8 = z4;
                    arrayList3 = arrayList11;
                    i9 = i7;
                    longSparseArray4 = longSparseArray7;
                } else {
                    tLRPC$User = null;
                }
                tLRPC$User2 = tLRPC$User;
                string = LocaleController.getString("SecretChatName", R.string.SecretChatName);
                z5 = false;
                tLRPC$FileLocation = null;
                tLRPC$Chat = null;
                z6 = false;
                z7 = false;
                String str16222 = "NotificationHiddenChatName";
                if (z13) {
                }
                if (tLRPC$FileLocation == null) {
                }
                if (tLRPC$Chat != null) {
                }
                if (z7) {
                }
                file2 = file;
                tLRPC$Chat2 = tLRPC$Chat;
                Intent intent622 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                intent622.putExtra("dialog_id", j5);
                int i25222 = i5;
                intent622.putExtra("max_id", i25222);
                intent622.putExtra("topic_id", i21);
                notificationsController3 = this;
                bitmap2 = bitmap;
                intent622.putExtra("currentAccount", notificationsController3.currentAccount);
                str4 = "max_id";
                i10 = i25222;
                PendingIntent broadcast222 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent622, 167772160);
                RemoteInput build3222 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                if (!DialogObject.isChatDialog(j5)) {
                }
                build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast222).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build3222).setShowsUserInterface(false).build();
                num2 = notificationsController3.pushDialogs.get(j5);
                if (num2 == null) {
                }
                int max222 = Math.max(num2.intValue(), arrayList10.size());
                if (max222 > 1) {
                }
                person = (Person) longSparseArray9.get(clientUserId);
                if (Build.VERSION.SDK_INT >= 28) {
                }
                str6 = "currentAccount";
                messageObject2 = messageObject;
                person = person;
                boolean z142222 = !(messageObject2.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest);
                String str172222 = "";
                if (person == null) {
                }
                messagingStyle = new NotificationCompat.MessagingStyle("");
                NotificationCompat.MessagingStyle messagingStyle32222 = messagingStyle;
                i11 = Build.VERSION.SDK_INT;
                if (i11 >= 28) {
                }
                messagingStyle32222.setConversationTitle(format);
                messagingStyle32222.setGroupConversation(i11 >= 28 || (!z7 && DialogObject.isChatDialog(j5)) || UserObject.isReplyUser(j5));
                StringBuilder sb22222 = new StringBuilder();
                action = build;
                String[] strArr22222 = new String[1];
                NotificationCompat.MessagingStyle messagingStyle42222 = messagingStyle32222;
                boolean[] zArr22222 = new boolean[1];
                size = arrayList10.size() - 1;
                int i262222 = 0;
                ArrayList<TLRPC$TL_keyboardButtonRow> arrayList122222 = null;
                while (size >= 0) {
                }
                ArrayList arrayList132222 = arrayList10;
                long j62222 = clientUserId;
                String str182222 = string;
                LongSparseArray longSparseArray112222 = longSparseArray9;
                StringBuilder sb32222 = sb22222;
                NotificationCompat.MessagingStyle messagingStyle52222 = messagingStyle42222;
                Intent intent22222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                intent22222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                intent22222.setFlags(ConnectionsManager.FileTypeFile);
                intent22222.addCategory("android.intent.category.LAUNCHER");
                if (DialogObject.isEncryptedDialog(j5)) {
                }
                FileLog.d("show extra notifications chatId " + j5 + " topicId " + i21);
                if (i21 != 0) {
                }
                String str192222 = str6;
                intent22222.putExtra(str192222, notificationsController3.currentAccount);
                PendingIntent activity2222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22222, 1140850688);
                NotificationCompat.WearableExtender wearableExtender2222 = new NotificationCompat.WearableExtender();
                if (action != null) {
                }
                Intent intent32222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                intent32222.addFlags(32);
                intent32222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                intent32222.putExtra(str5, j5);
                int i272222 = i10;
                intent32222.putExtra(str4, i272222);
                intent32222.putExtra(str192222, notificationsController3.currentAccount);
                NotificationCompat.Action build52222 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent32222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                if (!DialogObject.isEncryptedDialog(j5)) {
                }
                if (str7 != null) {
                }
                wearableExtender2222.setBridgeTag("tgaccount" + j62222);
                long j72222 = ((long) ((MessageObject) arrayList132222.get(0)).messageOwner.date) * 1000;
                NotificationCompat.Builder category2222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str182222).setSmallIcon(R.drawable.notification).setContentText(sb32222.toString()).setAutoCancel(true).setNumber(arrayList132222.size()).setColor(-15618822).setGroupSummary(false).setWhen(j72222).setShowWhen(true).setStyle(messagingStyle52222).setContentIntent(activity2222).extend(wearableExtender2222).setSortKey(String.valueOf(Long.MAX_VALUE - j72222)).setCategory("msg");
                Intent intent42222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                intent42222.putExtra("messageDate", i6);
                intent42222.putExtra("dialogId", j5);
                intent42222.putExtra(str192222, notificationsController3.currentAccount);
                category2222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent42222, 167772160));
                if (z9) {
                }
                if (action != null) {
                }
                if (!z13) {
                }
                if (arrayList9.size() != 1) {
                }
                if (DialogObject.isEncryptedDialog(j5)) {
                }
                if (bitmap2 != null) {
                }
                if (!AndroidUtilities.needShowPasscode(false)) {
                }
                if (tLRPC$Chat2 == null) {
                }
                tLRPC$User4 = tLRPC$User3;
                boolean z152222 = z9;
                if (Build.VERSION.SDK_INT >= 26) {
                }
                j2 = j62222;
                i8 = i22;
                z8 = z152222;
                longSparseArray2 = longSparseArray112222;
                i9 = i7;
                longSparseArray4 = longSparseArray5;
                arrayList2 = arrayList9;
                notification = notification2;
                longSparseArray3 = longSparseArray10;
                arrayList3 = arrayList11;
                arrayList3.add(new 1NotificationHolder(num.intValue(), j5, i21, str182222, tLRPC$User4, tLRPC$Chat2, category2222, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                notificationsController2 = this;
                notificationsController2.wearNotificationsIds.put(j5, num);
            }
            i20 = i8 + 1;
            arrayList8 = arrayList3;
            clientUserId = j2;
            z12 = z8;
            size3 = i9;
            longSparseArray7 = longSparseArray4;
            arrayList6 = arrayList2;
            longSparseArray8 = longSparseArray3;
            longSparseArray9 = longSparseArray2;
            build2 = notification;
            i19 = 7;
            notificationsController4 = notificationsController2;
        }
        LongSparseArray longSparseArray12 = longSparseArray9;
        LongSparseArray longSparseArray13 = longSparseArray8;
        Notification notification3 = build2;
        NotificationsController notificationsController5 = notificationsController4;
        ArrayList arrayList15 = arrayList8;
        if (z12) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("show summary with id " + notificationsController5.notificationId);
            }
            try {
                notificationManager.notify(notificationsController5.notificationId, notification3);
                arrayList = arrayList15;
                notificationsController = notificationsController5;
            } catch (SecurityException e5) {
                FileLog.e(e5);
                arrayList = arrayList15;
                notificationsController = notificationsController5;
                resetNotificationSound(builder, j, i, str2, jArr, i2, uri, i3, z, z2, z3, i4);
            }
        } else {
            arrayList = arrayList15;
            notificationsController = notificationsController5;
            if (notificationsController.openedInBubbleDialogs.isEmpty()) {
                notificationManager.cancel(notificationsController.notificationId);
            }
        }
        int i32 = 0;
        while (i32 < longSparseArray13.size()) {
            LongSparseArray longSparseArray14 = longSparseArray13;
            if (!notificationsController.openedInBubbleDialogs.contains(Long.valueOf(longSparseArray14.keyAt(i32)))) {
                Integer num5 = (Integer) longSparseArray14.valueAt(i32);
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("cancel notification id " + num5);
                }
                notificationManager.cancel(num5.intValue());
            }
            i32++;
            longSparseArray13 = longSparseArray14;
        }
        ArrayList arrayList16 = new ArrayList(arrayList.size());
        int size5 = arrayList.size();
        int i33 = 0;
        while (i33 < size5) {
            ArrayList arrayList17 = arrayList;
            1NotificationHolder r3 = (1NotificationHolder) arrayList17.get(i33);
            arrayList16.clear();
            if (Build.VERSION.SDK_INT < 29 || DialogObject.isEncryptedDialog(r3.dialogId)) {
                longSparseArray = longSparseArray12;
            } else {
                NotificationCompat.Builder builder2 = r3.notification;
                long j8 = r3.dialogId;
                longSparseArray = longSparseArray12;
                String createNotificationShortcut = createNotificationShortcut(builder2, j8, r3.name, r3.user, r3.chat, (Person) longSparseArray.get(j8));
                if (createNotificationShortcut != null) {
                    arrayList16.add(createNotificationShortcut);
                }
            }
            r3.call();
            if (!unsupportedNotificationShortcut() && !arrayList16.isEmpty()) {
                ShortcutManagerCompat.removeDynamicShortcuts(ApplicationLoader.applicationContext, arrayList16);
            }
            i33++;
            arrayList = arrayList17;
            longSparseArray12 = longSparseArray;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class 1NotificationHolder {
        TLRPC$Chat chat;
        long dialogId;
        int id;
        String name;
        NotificationCompat.Builder notification;
        int topicId;
        TLRPC$User user;
        final /* synthetic */ String val$chatName;
        final /* synthetic */ int val$chatType;
        final /* synthetic */ int val$importance;
        final /* synthetic */ boolean val$isDefault;
        final /* synthetic */ boolean val$isInApp;
        final /* synthetic */ boolean val$isSilent;
        final /* synthetic */ int val$lastTopicId;
        final /* synthetic */ int val$ledColor;
        final /* synthetic */ Uri val$sound;
        final /* synthetic */ long[] val$vibrationPattern;

        1NotificationHolder(int i, long j, int i2, String str, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, NotificationCompat.Builder builder, int i3, String str2, long[] jArr, int i4, Uri uri, int i5, boolean z, boolean z2, boolean z3, int i6) {
            this.val$lastTopicId = i3;
            this.val$chatName = str2;
            this.val$vibrationPattern = jArr;
            this.val$ledColor = i4;
            this.val$sound = uri;
            this.val$importance = i5;
            this.val$isDefault = z;
            this.val$isInApp = z2;
            this.val$isSilent = z3;
            this.val$chatType = i6;
            this.id = i;
            this.name = str;
            this.user = tLRPC$User;
            this.chat = tLRPC$Chat;
            this.notification = builder;
            this.dialogId = j;
            this.topicId = i2;
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
    public static /* synthetic */ void lambda$showExtraNotifications$34(Uri uri, File file) {
        ApplicationLoader.applicationContext.revokeUriPermission(uri, 1);
        if (file != null) {
            file.delete();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadRoundAvatar$36(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
        imageDecoder.setPostProcessor(NotificationsController$$ExternalSyntheticLambda1.INSTANCE);
    }

    @TargetApi(28)
    public static void loadRoundAvatar(File file, Person.Builder builder) {
        if (file != null) {
            try {
                builder.setIcon(IconCompat.createWithBitmap(ImageDecoder.decodeBitmap(ImageDecoder.createSource(file), NotificationsController$$ExternalSyntheticLambda0.INSTANCE)));
            } catch (Throwable unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$loadRoundAvatar$35(Canvas canvas) {
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$playOutChatSound$38();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playOutChatSound$38() {
        try {
            if (Math.abs(SystemClock.elapsedRealtime() - this.lastSoundOutPlay) <= 100) {
                return;
            }
            this.lastSoundOutPlay = SystemClock.elapsedRealtime();
            if (this.soundPool == null) {
                SoundPool soundPool = new SoundPool(3, 1, 0);
                this.soundPool = soundPool;
                soundPool.setOnLoadCompleteListener(NotificationsController$$ExternalSyntheticLambda2.INSTANCE);
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
    public static /* synthetic */ void lambda$playOutChatSound$37(SoundPool soundPool, int i, int i2) {
        if (i2 == 0) {
            try {
                soundPool.play(i, 1.0f, 1.0f, 1, 0, 1.0f);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public void clearDialogNotificationsSettings(long j, int i) {
        SharedPreferences.Editor edit = getAccountInstance().getNotificationsSettings().edit();
        String sharedPrefKey = getSharedPrefKey(j, i);
        SharedPreferences.Editor remove = edit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + sharedPrefKey);
        remove.remove(NotificationsSettingsFacade.PROPERTY_CUSTOM + sharedPrefKey);
        getMessagesStorage().setDialogFlags(j, 0L);
        TLRPC$Dialog tLRPC$Dialog = getMessagesController().dialogs_dict.get(j);
        if (tLRPC$Dialog != null) {
            tLRPC$Dialog.notify_settings = new TLRPC$TL_peerNotifySettings();
        }
        edit.commit();
        getNotificationsController().updateServerNotificationsSettings(j, i, true);
    }

    public void setDialogNotificationsSettings(long j, int i, int i2) {
        SharedPreferences.Editor edit = getAccountInstance().getNotificationsSettings().edit();
        TLRPC$Dialog tLRPC$Dialog = MessagesController.getInstance(UserConfig.selectedAccount).dialogs_dict.get(j);
        if (i2 == 4) {
            if (isGlobalNotificationsEnabled(j)) {
                edit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, i));
            } else {
                edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, i), 0);
            }
            getMessagesStorage().setDialogFlags(j, 0L);
            if (tLRPC$Dialog != null) {
                tLRPC$Dialog.notify_settings = new TLRPC$TL_peerNotifySettings();
            }
        } else {
            int currentTime = ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime();
            if (i2 == 0) {
                currentTime += 3600;
            } else if (i2 == 1) {
                currentTime += 28800;
            } else if (i2 == 2) {
                currentTime += 172800;
            } else if (i2 == 3) {
                currentTime = ConnectionsManager.DEFAULT_DATACENTER_ID;
            }
            long j2 = 1;
            if (i2 == 3) {
                edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, i), 2);
            } else {
                edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, i), 3);
                edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + getSharedPrefKey(j, i), currentTime);
                j2 = 1 | (((long) currentTime) << 32);
            }
            getInstance(UserConfig.selectedAccount).removeNotificationsForDialog(j);
            MessagesStorage.getInstance(UserConfig.selectedAccount).setDialogFlags(j, j2);
            if (tLRPC$Dialog != null) {
                TLRPC$TL_peerNotifySettings tLRPC$TL_peerNotifySettings = new TLRPC$TL_peerNotifySettings();
                tLRPC$Dialog.notify_settings = tLRPC$TL_peerNotifySettings;
                tLRPC$TL_peerNotifySettings.mute_until = currentTime;
            }
        }
        edit.commit();
        updateServerNotificationsSettings(j, i);
    }

    public void updateServerNotificationsSettings(long j, int i) {
        updateServerNotificationsSettings(j, i, true);
    }

    public void updateServerNotificationsSettings(long j, int i, boolean z) {
        if (z) {
            getNotificationCenter().postNotificationName(NotificationCenter.notificationsSettingsUpdated, new Object[0]);
        }
        if (DialogObject.isEncryptedDialog(j)) {
            return;
        }
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        TLRPC$TL_account_updateNotifySettings tLRPC$TL_account_updateNotifySettings = new TLRPC$TL_account_updateNotifySettings();
        TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings = new TLRPC$TL_inputPeerNotifySettings();
        tLRPC$TL_account_updateNotifySettings.settings = tLRPC$TL_inputPeerNotifySettings;
        tLRPC$TL_inputPeerNotifySettings.flags |= 1;
        tLRPC$TL_inputPeerNotifySettings.show_previews = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_CONTENT_PREVIEW + getSharedPrefKey(j, i), true);
        TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings2 = tLRPC$TL_account_updateNotifySettings.settings;
        tLRPC$TL_inputPeerNotifySettings2.flags = tLRPC$TL_inputPeerNotifySettings2.flags | 2;
        tLRPC$TL_inputPeerNotifySettings2.silent = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_SILENT + getSharedPrefKey(j, i), false);
        int i2 = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, i), -1);
        if (i2 != -1) {
            TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings3 = tLRPC$TL_account_updateNotifySettings.settings;
            tLRPC$TL_inputPeerNotifySettings3.flags |= 4;
            if (i2 == 3) {
                tLRPC$TL_inputPeerNotifySettings3.mute_until = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + getSharedPrefKey(j, i), 0);
            } else {
                tLRPC$TL_inputPeerNotifySettings3.mute_until = i2 == 2 ? ConnectionsManager.DEFAULT_DATACENTER_ID : 0;
            }
        }
        long j2 = notificationsSettings.getLong("sound_document_id_" + getSharedPrefKey(j, i), 0L);
        String string = notificationsSettings.getString("sound_path_" + getSharedPrefKey(j, i), null);
        TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings4 = tLRPC$TL_account_updateNotifySettings.settings;
        tLRPC$TL_inputPeerNotifySettings4.flags = tLRPC$TL_inputPeerNotifySettings4.flags | 8;
        if (j2 != 0) {
            TLRPC$TL_notificationSoundRingtone tLRPC$TL_notificationSoundRingtone = new TLRPC$TL_notificationSoundRingtone();
            tLRPC$TL_notificationSoundRingtone.id = j2;
            tLRPC$TL_account_updateNotifySettings.settings.sound = tLRPC$TL_notificationSoundRingtone;
        } else if (string != null) {
            if (string.equalsIgnoreCase("NoSound")) {
                tLRPC$TL_account_updateNotifySettings.settings.sound = new TLRPC$TL_notificationSoundNone();
            } else {
                TLRPC$TL_notificationSoundLocal tLRPC$TL_notificationSoundLocal = new TLRPC$TL_notificationSoundLocal();
                tLRPC$TL_notificationSoundLocal.title = notificationsSettings.getString("sound_" + getSharedPrefKey(j, i), null);
                tLRPC$TL_notificationSoundLocal.data = string;
                tLRPC$TL_account_updateNotifySettings.settings.sound = tLRPC$TL_notificationSoundLocal;
            }
        } else {
            tLRPC$TL_inputPeerNotifySettings4.sound = new TLRPC$TL_notificationSoundDefault();
        }
        if (i != 0) {
            TLRPC$TL_inputNotifyForumTopic tLRPC$TL_inputNotifyForumTopic = new TLRPC$TL_inputNotifyForumTopic();
            tLRPC$TL_inputNotifyForumTopic.peer = getMessagesController().getInputPeer(j);
            tLRPC$TL_inputNotifyForumTopic.top_msg_id = i;
            tLRPC$TL_account_updateNotifySettings.peer = tLRPC$TL_inputNotifyForumTopic;
        } else {
            TLRPC$TL_inputNotifyPeer tLRPC$TL_inputNotifyPeer = new TLRPC$TL_inputNotifyPeer();
            tLRPC$TL_account_updateNotifySettings.peer = tLRPC$TL_inputNotifyPeer;
            tLRPC$TL_inputNotifyPeer.peer = getMessagesController().getInputPeer(j);
        }
        getConnectionsManager().sendRequest(tLRPC$TL_account_updateNotifySettings, NotificationsController$$ExternalSyntheticLambda42.INSTANCE);
    }

    public void updateServerNotificationsSettings(int i) {
        String str;
        String str2;
        String str3;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        TLRPC$TL_account_updateNotifySettings tLRPC$TL_account_updateNotifySettings = new TLRPC$TL_account_updateNotifySettings();
        TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings = new TLRPC$TL_inputPeerNotifySettings();
        tLRPC$TL_account_updateNotifySettings.settings = tLRPC$TL_inputPeerNotifySettings;
        tLRPC$TL_inputPeerNotifySettings.flags = 5;
        if (i == 0) {
            tLRPC$TL_account_updateNotifySettings.peer = new TLRPC$TL_inputNotifyChats();
            tLRPC$TL_account_updateNotifySettings.settings.mute_until = notificationsSettings.getInt("EnableGroup2", 0);
            tLRPC$TL_account_updateNotifySettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewGroup", true);
            str = "GroupSound";
            str2 = "GroupSoundDocId";
            str3 = "GroupSoundPath";
        } else if (i == 1) {
            tLRPC$TL_account_updateNotifySettings.peer = new TLRPC$TL_inputNotifyUsers();
            tLRPC$TL_account_updateNotifySettings.settings.mute_until = notificationsSettings.getInt("EnableAll2", 0);
            tLRPC$TL_account_updateNotifySettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewAll", true);
            str = "GlobalSound";
            str2 = "GlobalSoundDocId";
            str3 = "GlobalSoundPath";
        } else {
            tLRPC$TL_account_updateNotifySettings.peer = new TLRPC$TL_inputNotifyBroadcasts();
            tLRPC$TL_account_updateNotifySettings.settings.mute_until = notificationsSettings.getInt("EnableChannel2", 0);
            tLRPC$TL_account_updateNotifySettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewChannel", true);
            str = "ChannelSound";
            str2 = "ChannelSoundDocId";
            str3 = "ChannelSoundPath";
        }
        tLRPC$TL_account_updateNotifySettings.settings.flags |= 8;
        long j = notificationsSettings.getLong(str2, 0L);
        String string = notificationsSettings.getString(str3, "NoSound");
        if (j != 0) {
            TLRPC$TL_notificationSoundRingtone tLRPC$TL_notificationSoundRingtone = new TLRPC$TL_notificationSoundRingtone();
            tLRPC$TL_notificationSoundRingtone.id = j;
            tLRPC$TL_account_updateNotifySettings.settings.sound = tLRPC$TL_notificationSoundRingtone;
        } else if (string != null) {
            if (string.equalsIgnoreCase("NoSound")) {
                tLRPC$TL_account_updateNotifySettings.settings.sound = new TLRPC$TL_notificationSoundNone();
            } else {
                TLRPC$TL_notificationSoundLocal tLRPC$TL_notificationSoundLocal = new TLRPC$TL_notificationSoundLocal();
                tLRPC$TL_notificationSoundLocal.title = notificationsSettings.getString(str, null);
                tLRPC$TL_notificationSoundLocal.data = string;
                tLRPC$TL_account_updateNotifySettings.settings.sound = tLRPC$TL_notificationSoundLocal;
            }
        } else {
            tLRPC$TL_account_updateNotifySettings.settings.sound = new TLRPC$TL_notificationSoundDefault();
        }
        getConnectionsManager().sendRequest(tLRPC$TL_account_updateNotifySettings, NotificationsController$$ExternalSyntheticLambda41.INSTANCE);
    }

    public boolean isGlobalNotificationsEnabled(long j) {
        return isGlobalNotificationsEnabled(j, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0028, code lost:
        if (r4.megagroup == false) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x000e, code lost:
        if (r6.booleanValue() != false) goto L7;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isGlobalNotificationsEnabled(long j, Boolean bool) {
        int i = 2;
        if (!DialogObject.isChatDialog(j)) {
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
        return getAccountInstance().getNotificationsSettings().getInt(getGlobalNotificationsKey(i), 0) < getConnectionsManager().getCurrentTime();
    }

    public void setGlobalNotificationsEnabled(int i, int i2) {
        getAccountInstance().getNotificationsSettings().edit().putInt(getGlobalNotificationsKey(i), i2).commit();
        updateServerNotificationsSettings(i);
        getMessagesStorage().updateMutedDialogsFiltersCounters();
        deleteNotificationChannelGlobal(i);
    }

    public void muteDialog(long j, int i, boolean z) {
        if (z) {
            getInstance(this.currentAccount).muteUntil(j, i, ConnectionsManager.DEFAULT_DATACENTER_ID);
            return;
        }
        boolean isGlobalNotificationsEnabled = getInstance(this.currentAccount).isGlobalNotificationsEnabled(j);
        boolean z2 = i != 0;
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
        if (isGlobalNotificationsEnabled && !z2) {
            edit.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, i));
        } else {
            edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, i), 0);
        }
        if (i == 0) {
            getMessagesStorage().setDialogFlags(j, 0L);
            TLRPC$Dialog tLRPC$Dialog = getMessagesController().dialogs_dict.get(j);
            if (tLRPC$Dialog != null) {
                tLRPC$Dialog.notify_settings = new TLRPC$TL_peerNotifySettings();
            }
        }
        edit.apply();
        updateServerNotificationsSettings(j, i);
    }

    public NotificationsSettingsFacade getNotificationsSettingsFacade() {
        return this.dialogsNotificationsFacade;
    }

    public void loadTopicsNotificationsExceptions(final long j, final Consumer<HashSet<Integer>> consumer) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$loadTopicsNotificationsExceptions$42(j, consumer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadTopicsNotificationsExceptions$42(long j, final Consumer consumer) {
        final HashSet hashSet = new HashSet();
        for (Map.Entry<String, ?> entry : MessagesController.getNotificationsSettings(this.currentAccount).getAll().entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(NotificationsSettingsFacade.PROPERTY_NOTIFY + j)) {
                int intValue = Utilities.parseInt((CharSequence) key.replace(NotificationsSettingsFacade.PROPERTY_NOTIFY + j, "")).intValue();
                if (intValue != 0 && getMessagesController().isDialogMuted(j, intValue) != getMessagesController().isDialogMuted(j, 0)) {
                    hashSet.add(Integer.valueOf(intValue));
                }
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.lambda$loadTopicsNotificationsExceptions$41(Consumer.this, hashSet);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadTopicsNotificationsExceptions$41(Consumer consumer, HashSet hashSet) {
        if (consumer != null) {
            consumer.accept(hashSet);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DialogKey {
        final long dialogId;
        final int topicId;

        private DialogKey(long j, int i) {
            this.dialogId = j;
            this.topicId = i;
        }
    }
}
