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
import androidx.core.graphics.drawable.IconCompat;
import j$.util.function.Consumer;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
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
import org.telegram.tgnet.TLRPC$TL_messageActionSetMessagesTTL;
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
    NotificationsSettingsFacade dialogsNotificationsFacade;
    private Boolean groupsCreated;
    private boolean inChatSoundEnabled;
    public long lastNotificationChannelCreateTime;
    private long lastSoundOutPlay;
    private long lastSoundPlay;
    private String launcherClassName;
    private Runnable notificationDelayRunnable;
    private PowerManager.WakeLock notificationDelayWakelock;
    private String notificationGroup;
    public boolean showBadgeMessages;
    public boolean showBadgeMuted;
    public boolean showBadgeNumber;
    private int soundIn;
    private boolean soundInLoaded;
    private int soundOut;
    private boolean soundOutLoaded;
    private SoundPool soundPool;
    private int soundRecord;
    private boolean soundRecordLoaded;
    private static DispatchQueue notificationsQueue = new DispatchQueue("notificationsQueue");
    public static long globalSecretChatId = DialogObject.makeEncryptedDialogId(1);
    private ArrayList<MessageObject> pushMessages = new ArrayList<>();
    private ArrayList<MessageObject> delayedPushMessages = new ArrayList<>();
    private LongSparseArray<SparseArray<MessageObject>> pushMessagesDict = new LongSparseArray<>();
    private LongSparseArray<MessageObject> fcmRandomMessagesDict = new LongSparseArray<>();
    private LongSparseArray<Point> smartNotificationsDialogs = new LongSparseArray<>();
    private LongSparseArray<Integer> pushDialogs = new LongSparseArray<>();
    private LongSparseArray<Integer> wearNotificationsIds = new LongSparseArray<>();
    private LongSparseArray<Integer> lastWearNotifiedMessageId = new LongSparseArray<>();
    private LongSparseArray<Integer> pushDialogsOverrideMention = new LongSparseArray<>();
    public ArrayList<MessageObject> popupMessages = new ArrayList<>();
    public ArrayList<MessageObject> popupReplyMessages = new ArrayList<>();
    private HashSet<Long> openedInBubbleDialogs = new HashSet<>();
    private long openedDialogId = 0;
    private int openedTopicId = 0;
    private int lastButtonId = 5000;
    private int total_unread_count = 0;
    private int personalCount = 0;
    private boolean notifyCheck = false;
    private int lastOnlineFromOtherDevice = 0;
    private int lastBadgeCount = -1;
    char[] spoilerChars = {10252, 10338, 10385, 10280};
    private int notificationId = this.currentAccount + 1;

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
            if (!this.notificationDelayWakelock.isHeld()) {
                return;
            }
            this.notificationDelayWakelock.release();
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
        if (notificationChannel != null) {
            return;
        }
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

    public static String getSharedPrefKey(long j, int i) {
        String l = Long.toString(j);
        if (i != 0) {
            return l + "_" + i;
        }
        return l;
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
                            int intValue2 = this.total_unread_count - num2.intValue();
                            this.total_unread_count = intValue2;
                            this.total_unread_count = intValue2 + num.intValue();
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
        boolean z;
        Integer num;
        int i = this.total_unread_count;
        getAccountInstance().getNotificationsSettings();
        Integer num2 = 0;
        int i2 = 0;
        while (true) {
            z = true;
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
                int intValue = this.total_unread_count - num3.intValue();
                this.total_unread_count = intValue;
                this.total_unread_count = intValue + num4.intValue();
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
                if (this.lastOnlineFromOtherDevice <= getConnectionsManager().getCurrentTime()) {
                    z = false;
                }
                scheduleNotificationDelay(z);
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
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00d7, code lost:
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
        if (!arrayList.isEmpty()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda32
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$processReadMessages$13(arrayList);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processReadMessages$13(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x0056, code lost:
        if (r0 == 2) goto L30;
     */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0070  */
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
            if (this.pushDialogs.indexOfKey(longSparseArray.keyAt(i)) >= 0) {
                ArrayList arrayList = (ArrayList) longSparseArray.valueAt(i);
                int size2 = arrayList.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    MessageObject messageObject = (MessageObject) arrayList.get(i2);
                    long j = messageObject.messageOwner.peer_id.channel_id;
                    long j2 = 0;
                    if (j != 0) {
                        j2 = -j;
                    }
                    SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(j2);
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
        } else if (countDownLatch == null) {
        } else {
            countDownLatch.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0048, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserJoined) == false) goto L20;
     */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0145  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processNewMessages$18(ArrayList arrayList, final ArrayList arrayList2, boolean z, boolean z2, CountDownLatch countDownLatch) {
        boolean z3;
        boolean z4;
        Integer num;
        int i;
        long j;
        boolean z5;
        boolean z6;
        int i2;
        MessageObject messageObject;
        long j2;
        MessageObject messageObject2;
        long j3;
        boolean z7;
        int i3;
        LongSparseArray longSparseArray;
        SparseArray<MessageObject> sparseArray;
        long j4;
        SparseArray<MessageObject> sparseArray2;
        MessageObject messageObject3;
        ArrayList arrayList3 = arrayList;
        LongSparseArray longSparseArray2 = new LongSparseArray();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        boolean z8 = notificationsSettings.getBoolean("PinnedMessages", true);
        int i4 = 0;
        int i5 = 0;
        boolean z9 = false;
        boolean z10 = false;
        boolean z11 = false;
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
                z5 = z8;
                i = i5;
                longSparseArray = longSparseArray2;
                i4 = i2;
                i5 = i + 1;
                arrayList3 = arrayList;
                longSparseArray2 = longSparseArray;
                z8 = z5;
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
                    z6 = messageObject4.localChannel;
                    z5 = z8;
                } else if (DialogObject.isChatDialog(dialogId)) {
                    z5 = z8;
                    TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-dialogId));
                    z6 = ChatObject.isChannel(chat) && !chat.megagroup;
                } else {
                    z5 = z8;
                    z6 = false;
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
                                    i4 = addToPopupMessages(arrayList2, messageObject4, dialogId, z6, notificationsSettings);
                                } else {
                                    messageObject3 = messageObject4;
                                    i4 = i2;
                                }
                                if (z) {
                                    boolean z12 = messageObject3.localEdit;
                                    if (z12) {
                                        getMessagesStorage().putPushMessage(messageObject3);
                                    }
                                    z10 = z12;
                                }
                                longSparseArray = longSparseArray2;
                                i5 = i + 1;
                                arrayList3 = arrayList;
                                longSparseArray2 = longSparseArray;
                                z8 = z5;
                            }
                            longSparseArray = longSparseArray2;
                            i4 = i2;
                            i5 = i + 1;
                            arrayList3 = arrayList;
                            longSparseArray2 = longSparseArray;
                            z8 = z5;
                        } else {
                            if (!z10) {
                                if (z) {
                                    getMessagesStorage().putPushMessage(messageObject4);
                                }
                                int topicId = MessageObject.getTopicId(messageObject4.messageOwner);
                                if (dialogId != this.openedDialogId || !ApplicationLoader.isScreenOn) {
                                    TLRPC$Message tLRPC$Message2 = messageObject4.messageOwner;
                                    if (!tLRPC$Message2.mentioned) {
                                        j3 = dialogId;
                                    } else if (z5 || !(tLRPC$Message2.action instanceof TLRPC$TL_messageActionPinMessage)) {
                                        j3 = messageObject4.getFromChatId();
                                    }
                                    if (isPersonalMessage(messageObject4)) {
                                        this.personalCount++;
                                    }
                                    DialogObject.isChatDialog(j3);
                                    int indexOfKey = longSparseArray2.indexOfKey(j3);
                                    if (indexOfKey >= 0 && topicId == 0) {
                                        z7 = ((Boolean) longSparseArray2.valueAt(indexOfKey)).booleanValue();
                                    } else {
                                        int notifyOverride = getNotifyOverride(notificationsSettings, j3, topicId);
                                        if (notifyOverride == -1) {
                                            z7 = isGlobalNotificationsEnabled(j3, Boolean.valueOf(z6));
                                        } else {
                                            z7 = notifyOverride != 2;
                                        }
                                        longSparseArray2.put(j3, Boolean.valueOf(z7));
                                    }
                                    if (z7) {
                                        if (!z) {
                                            sparseArray = sparseArray3;
                                            longSparseArray = longSparseArray2;
                                            j4 = j6;
                                            boolean z13 = z6;
                                            i3 = id;
                                            i4 = addToPopupMessages(arrayList2, messageObject4, j3, z13, notificationsSettings);
                                        } else {
                                            sparseArray = sparseArray3;
                                            i3 = id;
                                            longSparseArray = longSparseArray2;
                                            j4 = j6;
                                            i4 = i2;
                                        }
                                        if (!z11) {
                                            z11 = messageObject4.messageOwner.from_scheduled;
                                        }
                                        this.delayedPushMessages.add(messageObject4);
                                        this.pushMessages.add(0, messageObject4);
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
                                        getMessagesController().checkUnreadReactions(j3, MessageObject.getTopicId(messageObject4.messageOwner), sparseBooleanArray);
                                    }
                                    z9 = true;
                                    i5 = i + 1;
                                    arrayList3 = arrayList;
                                    longSparseArray2 = longSparseArray;
                                    z8 = z5;
                                } else if (!z) {
                                    playInChatSound();
                                }
                            }
                            longSparseArray = longSparseArray2;
                            i4 = i2;
                            i5 = i + 1;
                            arrayList3 = arrayList;
                            longSparseArray2 = longSparseArray;
                            z8 = z5;
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
            z5 = z8;
            i = i5;
            longSparseArray = longSparseArray2;
            i4 = i2;
            i5 = i + 1;
            arrayList3 = arrayList;
            longSparseArray2 = longSparseArray;
            z8 = z5;
        }
        final int i6 = i4;
        if (z9) {
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
        if (z || z11) {
            if (z10) {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
            } else if (z9) {
                MessageObject messageObject6 = (MessageObject) arrayList.get(0);
                long dialogId2 = messageObject6.getDialogId();
                int topicId2 = MessageObject.getTopicId(messageObject6.messageOwner);
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
                if (!this.notifyCheck || z3 || (num = this.pushDialogsOverrideMention.get(dialogId2)) == null || num.intValue() == 0) {
                    z4 = z3;
                } else {
                    intValue = num.intValue();
                    z4 = true;
                }
                if (z4) {
                    if (num3 != null) {
                        this.total_unread_count -= num3.intValue();
                    }
                    this.total_unread_count += intValue;
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
            if (i != 3 && ((i != 1 || !ApplicationLoader.isScreenOn) && (i != 2 || ApplicationLoader.isScreenOn))) {
                return;
            }
            Intent intent = new Intent(ApplicationLoader.applicationContext, PopupNotificationActivity.class);
            intent.setFlags(268763140);
            try {
                ApplicationLoader.applicationContext.startActivity(intent);
            } catch (Throwable unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processNewMessages$17(int i) {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
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
    /* JADX WARN: Removed duplicated region for block: B:15:0x0051  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0068 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0093 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0110  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processDialogsUpdateRead$21(LongSparseIntArray longSparseIntArray, final ArrayList arrayList) {
        boolean z;
        boolean z2;
        boolean z3;
        Integer num;
        int i = this.total_unread_count;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        int i2 = 0;
        while (true) {
            z = true;
            if (i2 >= longSparseIntArray.size()) {
                break;
            }
            long keyAt = longSparseIntArray.keyAt(i2);
            Integer num2 = this.pushDialogs.get(keyAt);
            int i3 = longSparseIntArray.get(keyAt);
            if (DialogObject.isChatDialog(keyAt)) {
                TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-keyAt));
                if (chat == null || chat.min || ChatObject.isNotInChat(chat)) {
                    i3 = 0;
                }
                if (chat != null) {
                    z2 = chat.forum;
                    if (!z2) {
                        int notifyOverride = getNotifyOverride(notificationsSettings, keyAt, 0);
                        if (notifyOverride == -1) {
                            z3 = isGlobalNotificationsEnabled(keyAt);
                        } else if (notifyOverride == 2) {
                            z3 = false;
                        }
                        if (this.notifyCheck && !z3 && (num = this.pushDialogsOverrideMention.get(keyAt)) != null && num.intValue() != 0) {
                            i3 = num.intValue();
                            z3 = true;
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
                        if ((!z3 || i3 == 0) && num2 != null) {
                            this.total_unread_count -= num2.intValue();
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
                                    long j2 = 0;
                                    if (j != 0) {
                                        j2 = -j;
                                    }
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
                        } else if (z3) {
                            this.total_unread_count += i3;
                            this.pushDialogs.put(keyAt, Integer.valueOf(i3));
                        }
                        i2++;
                    }
                    z3 = true;
                    if (this.notifyCheck) {
                        i3 = num.intValue();
                        z3 = true;
                    }
                    if (i3 == 0) {
                    }
                    if (i3 < 0) {
                    }
                    if (!z3) {
                    }
                    this.total_unread_count -= num2.intValue();
                    if (i3 == 0) {
                    }
                    i2++;
                }
            }
            z2 = false;
            if (!z2) {
            }
            z3 = true;
            if (this.notifyCheck) {
            }
            if (i3 == 0) {
            }
            if (i3 < 0) {
            }
            if (!z3) {
            }
            this.total_unread_count -= num2.intValue();
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
                if (this.lastOnlineFromOtherDevice <= getConnectionsManager().getCurrentTime()) {
                    z = false;
                }
                scheduleNotificationDelay(z);
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
        int i;
        long j;
        boolean z;
        SharedPreferences sharedPreferences;
        boolean z2;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        int i2;
        SparseArray<MessageObject> sparseArray;
        long j2;
        boolean z3;
        boolean z4;
        SparseArray<MessageObject> sparseArray2;
        this.pushDialogs.clear();
        this.pushMessages.clear();
        this.pushMessagesDict.clear();
        boolean z5 = false;
        this.total_unread_count = 0;
        this.personalCount = 0;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        LongSparseArray longSparseArray2 = new LongSparseArray();
        long j3 = 0;
        int i3 = 1;
        if (arrayList != null) {
            int i4 = 0;
            while (i4 < arrayList.size()) {
                TLRPC$Message tLRPC$Message = (TLRPC$Message) arrayList.get(i4);
                if (tLRPC$Message != null && ((tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from) == null || !tLRPC$MessageFwdHeader.imported)) {
                    TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
                    if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetMessagesTTL) && (!tLRPC$Message.silent || (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp) && !(tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserJoined)))) {
                        long j4 = tLRPC$Message.peer_id.channel_id;
                        long j5 = j4 != j3 ? -j4 : j3;
                        SparseArray<MessageObject> sparseArray3 = this.pushMessagesDict.get(j5);
                        if (sparseArray3 == null || sparseArray3.indexOfKey(tLRPC$Message.id) < 0) {
                            MessageObject messageObject = new MessageObject(this.currentAccount, tLRPC$Message, z5, z5);
                            if (isPersonalMessage(messageObject)) {
                                this.personalCount += i3;
                            }
                            i2 = i4;
                            long dialogId = messageObject.getDialogId();
                            int topicId = MessageObject.getTopicId(messageObject.messageOwner);
                            if (messageObject.messageOwner.mentioned) {
                                sparseArray = sparseArray3;
                                j2 = messageObject.getFromChatId();
                            } else {
                                sparseArray = sparseArray3;
                                j2 = dialogId;
                            }
                            int indexOfKey = longSparseArray2.indexOfKey(j2);
                            if (indexOfKey >= 0 && topicId == 0) {
                                z4 = ((Boolean) longSparseArray2.valueAt(indexOfKey)).booleanValue();
                            } else {
                                int notifyOverride = getNotifyOverride(notificationsSettings, j2, topicId);
                                if (notifyOverride == -1) {
                                    z3 = isGlobalNotificationsEnabled(j2);
                                } else {
                                    z3 = notifyOverride != 2;
                                }
                                z4 = z3;
                                longSparseArray2.put(j2, Boolean.valueOf(z4));
                            }
                            if (z4 && (j2 != this.openedDialogId || !ApplicationLoader.isScreenOn)) {
                                if (sparseArray == null) {
                                    sparseArray2 = new SparseArray<>();
                                    this.pushMessagesDict.put(j5, sparseArray2);
                                } else {
                                    sparseArray2 = sparseArray;
                                }
                                sparseArray2.put(tLRPC$Message.id, messageObject);
                                this.pushMessages.add(0, messageObject);
                                if (dialogId != j2) {
                                    Integer num = this.pushDialogsOverrideMention.get(dialogId);
                                    this.pushDialogsOverrideMention.put(dialogId, Integer.valueOf(num == null ? 1 : num.intValue() + 1));
                                }
                            }
                            i4 = i2 + 1;
                            z5 = false;
                            j3 = 0;
                            i3 = 1;
                        }
                    }
                }
                i2 = i4;
                i4 = i2 + 1;
                z5 = false;
                j3 = 0;
                i3 = 1;
            }
        }
        for (int i5 = 0; i5 < longSparseArray.size(); i5++) {
            long keyAt = longSparseArray.keyAt(i5);
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
                int intValue = ((Integer) longSparseArray.valueAt(i5)).intValue();
                this.pushDialogs.put(keyAt, Integer.valueOf(intValue));
                this.total_unread_count += intValue;
            }
        }
        if (arrayList2 != null) {
            int i6 = 0;
            while (i6 < arrayList2.size()) {
                MessageObject messageObject2 = (MessageObject) arrayList2.get(i6);
                int id = messageObject2.getId();
                if (this.pushMessagesDict.indexOfKey(id) >= 0) {
                    sharedPreferences = notificationsSettings;
                } else {
                    if (isPersonalMessage(messageObject2)) {
                        this.personalCount++;
                    }
                    long dialogId2 = messageObject2.getDialogId();
                    int topicId2 = MessageObject.getTopicId(messageObject2.messageOwner);
                    TLRPC$Message tLRPC$Message2 = messageObject2.messageOwner;
                    long j6 = tLRPC$Message2.random_id;
                    if (tLRPC$Message2.mentioned) {
                        i = id;
                        j = messageObject2.getFromChatId();
                    } else {
                        i = id;
                        j = dialogId2;
                    }
                    int indexOfKey3 = longSparseArray2.indexOfKey(j);
                    if (indexOfKey3 >= 0 && topicId2 == 0) {
                        z = ((Boolean) longSparseArray2.valueAt(indexOfKey3)).booleanValue();
                    } else {
                        int notifyOverride3 = getNotifyOverride(notificationsSettings, j, topicId2);
                        if (notifyOverride3 == -1) {
                            z = isGlobalNotificationsEnabled(j);
                        } else {
                            z = notifyOverride3 != 2;
                        }
                        longSparseArray2.put(j, Boolean.valueOf(z));
                    }
                    if (z) {
                        sharedPreferences = notificationsSettings;
                        if (j != this.openedDialogId || !ApplicationLoader.isScreenOn) {
                            if (i != 0) {
                                long j7 = messageObject2.messageOwner.peer_id.channel_id;
                                long j8 = j7 != 0 ? -j7 : 0L;
                                SparseArray<MessageObject> sparseArray4 = this.pushMessagesDict.get(j8);
                                if (sparseArray4 == null) {
                                    sparseArray4 = new SparseArray<>();
                                    this.pushMessagesDict.put(j8, sparseArray4);
                                }
                                sparseArray4.put(i, messageObject2);
                            } else if (j6 != 0) {
                                this.fcmRandomMessagesDict.put(j6, messageObject2);
                            }
                            this.pushMessages.add(0, messageObject2);
                            if (dialogId2 != j) {
                                Integer num2 = this.pushDialogsOverrideMention.get(dialogId2);
                                this.pushDialogsOverrideMention.put(dialogId2, Integer.valueOf(num2 == null ? 1 : num2.intValue() + 1));
                            }
                            Integer num3 = this.pushDialogs.get(j);
                            int intValue2 = num3 != null ? num3.intValue() + 1 : 1;
                            if (num3 != null) {
                                this.total_unread_count -= num3.intValue();
                            }
                            this.total_unread_count += intValue2;
                            this.pushDialogs.put(j, Integer.valueOf(intValue2));
                            i6++;
                            notificationsSettings = sharedPreferences;
                        }
                    } else {
                        sharedPreferences = notificationsSettings;
                    }
                }
                i6++;
                notificationsSettings = sharedPreferences;
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
        int i;
        int i2 = 0;
        for (int i3 = 0; i3 < 4; i3++) {
            if (UserConfig.getInstance(i3).isClientActivated()) {
                NotificationsController notificationsController = getInstance(i3);
                if (notificationsController.showBadgeNumber) {
                    if (notificationsController.showBadgeMessages) {
                        if (notificationsController.showBadgeMuted) {
                            try {
                                ArrayList arrayList = new ArrayList(MessagesController.getInstance(i3).allDialogs);
                                int size2 = arrayList.size();
                                for (int i4 = 0; i4 < size2; i4++) {
                                    TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) arrayList.get(i4);
                                    if ((tLRPC$Dialog == null || !DialogObject.isChatDialog(tLRPC$Dialog.id) || !ChatObject.isNotInChat(getMessagesController().getChat(Long.valueOf(-tLRPC$Dialog.id)))) && tLRPC$Dialog != null && (i = tLRPC$Dialog.unread_count) != 0) {
                                        i2 += i;
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
                            int size3 = MessagesController.getInstance(i3).allDialogs.size();
                            for (int i5 = 0; i5 < size3; i5++) {
                                TLRPC$Dialog tLRPC$Dialog2 = MessagesController.getInstance(i3).allDialogs.get(i5);
                                if ((!DialogObject.isChatDialog(tLRPC$Dialog2.id) || !ChatObject.isNotInChat(getMessagesController().getChat(Long.valueOf(-tLRPC$Dialog2.id)))) && tLRPC$Dialog2.unread_count != 0) {
                                    i2++;
                                }
                            }
                        } catch (Exception e2) {
                            FileLog.e(e2);
                        }
                    } else {
                        size = notificationsController.pushDialogs.size();
                    }
                    i2 += size;
                }
            }
        }
        return i2;
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

    /* JADX WARN: Code restructure failed: missing block: B:134:0x024f, code lost:
        if (r12.getBoolean("EnablePreviewAll", true) == false) goto L773;
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x0269, code lost:
        r1 = r26.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x0279, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageService) == false) goto L663;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x027b, code lost:
        r27[0] = null;
        r2 = r1.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x0283, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGeoProximityReached) == false) goto L141;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x028b, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x028e, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserJoined) != false) goto L661;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x0292, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionContactSignUp) == false) goto L145;
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x0298, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserUpdatedPhoto) == false) goto L149;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x02a8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationContactNewPhoto", org.telegram.messenger.R.string.NotificationContactNewPhoto, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x02ac, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionLoginUnknownLocation) == false) goto L153;
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x02ae, code lost:
        r1 = org.telegram.messenger.LocaleController.formatString("formatDateAtTime", org.telegram.messenger.R.string.formatDateAtTime, org.telegram.messenger.LocaleController.getInstance().formatterYear.format(r26.messageOwner.date * 1000), org.telegram.messenger.LocaleController.getInstance().formatterDay.format(r26.messageOwner.date * 1000));
        r2 = org.telegram.messenger.R.string.NotificationUnrecognizedDevice;
        r0 = r26.messageOwner.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x030a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationUnrecognizedDevice", r2, getUserConfig().getCurrentUser().first_name, r1, r0.title, r0.address);
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x030d, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGameScore) != false) goto L659;
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x0311, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPaymentSent) == false) goto L157;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x0317, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPhoneCall) == false) goto L165;
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x031b, code lost:
        if (r2.video == false) goto L163;
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x0325, code lost:
        return org.telegram.messenger.LocaleController.getString("CallMessageVideoIncomingMissed", org.telegram.messenger.R.string.CallMessageVideoIncomingMissed);
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x032e, code lost:
        return org.telegram.messenger.LocaleController.getString("CallMessageIncomingMissed", org.telegram.messenger.R.string.CallMessageIncomingMissed);
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x0331, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser) == false) goto L212;
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x0333, code lost:
        r3 = r2.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x0339, code lost:
        if (r3 != 0) goto L172;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x0342, code lost:
        if (r2.users.size() != 1) goto L172;
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x0344, code lost:
        r3 = r26.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x0359, code lost:
        if (r3 == 0) goto L197;
     */
    /* JADX WARN: Code restructure failed: missing block: B:175:0x0363, code lost:
        if (r26.messageOwner.peer_id.channel_id == 0) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x0367, code lost:
        if (r8.megagroup != false) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x037c, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.R.string.ChannelAddedByNotification, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:181:0x037f, code lost:
        if (r3 != r19) goto L184;
     */
    /* JADX WARN: Code restructure failed: missing block: B:183:0x0394, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.R.string.NotificationInvitedToGroup, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x0395, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x03a1, code lost:
        if (r0 != null) goto L187;
     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x03a3, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x03a9, code lost:
        if (r9 != r0.id) goto L195;
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x03ad, code lost:
        if (r8.megagroup == false) goto L193;
     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x03c2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.R.string.NotificationGroupAddSelfMega, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x03d6, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.R.string.NotificationGroupAddSelf, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:196:0x03f0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r7, r8.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x03f1, code lost:
        r1 = new java.lang.StringBuilder();
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:199:0x0401, code lost:
        if (r2 >= r26.messageOwner.action.users.size()) goto L209;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x0403, code lost:
        r3 = getMessagesController().getUser(r26.messageOwner.action.users.get(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x0417, code lost:
        if (r3 == null) goto L208;
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x0419, code lost:
        r3 = org.telegram.messenger.UserObject.getUserName(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:203:0x0421, code lost:
        if (r1.length() == 0) goto L205;
     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x0423, code lost:
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:205:0x0428, code lost:
        r1.append(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x042b, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:211:0x0447, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r7, r8.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:213:0x044b, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCall) == false) goto L216;
     */
    /* JADX WARN: Code restructure failed: missing block: B:215:0x045f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.R.string.NotificationGroupCreatedCall, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:217:0x0462, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled) == false) goto L220;
     */
    /* JADX WARN: Code restructure failed: missing block: B:219:0x046a, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:221:0x046d, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall) == false) goto L253;
     */
    /* JADX WARN: Code restructure failed: missing block: B:222:0x046f, code lost:
        r3 = r2.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:223:0x0475, code lost:
        if (r3 != 0) goto L227;
     */
    /* JADX WARN: Code restructure failed: missing block: B:225:0x047e, code lost:
        if (r2.users.size() != 1) goto L227;
     */
    /* JADX WARN: Code restructure failed: missing block: B:226:0x0480, code lost:
        r3 = r26.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:228:0x0495, code lost:
        if (r3 == 0) goto L238;
     */
    /* JADX WARN: Code restructure failed: missing block: B:230:0x0499, code lost:
        if (r3 != r19) goto L233;
     */
    /* JADX WARN: Code restructure failed: missing block: B:232:0x04ae, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.R.string.NotificationGroupInvitedYouToCall, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:233:0x04af, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:234:0x04bb, code lost:
        if (r0 != null) goto L236;
     */
    /* JADX WARN: Code restructure failed: missing block: B:235:0x04bd, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:237:0x04d8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r7, r8.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:238:0x04d9, code lost:
        r1 = new java.lang.StringBuilder();
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x04e9, code lost:
        if (r2 >= r26.messageOwner.action.users.size()) goto L250;
     */
    /* JADX WARN: Code restructure failed: missing block: B:241:0x04eb, code lost:
        r3 = getMessagesController().getUser(r26.messageOwner.action.users.get(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:242:0x04ff, code lost:
        if (r3 == null) goto L249;
     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x0501, code lost:
        r3 = org.telegram.messenger.UserObject.getUserName(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x0509, code lost:
        if (r1.length() == 0) goto L246;
     */
    /* JADX WARN: Code restructure failed: missing block: B:245:0x050b, code lost:
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:246:0x0510, code lost:
        r1.append(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:248:0x0513, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:252:0x052f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r7, r8.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:254:0x0533, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink) == false) goto L257;
     */
    /* JADX WARN: Code restructure failed: missing block: B:256:0x0548, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.R.string.NotificationInvitedToGroupByLink, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:258:0x054e, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle) == false) goto L261;
     */
    /* JADX WARN: Code restructure failed: missing block: B:260:0x0560, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.R.string.NotificationEditedGroupName, r7, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:262:0x0563, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto) != false) goto L643;
     */
    /* JADX WARN: Code restructure failed: missing block: B:264:0x0567, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto) == false) goto L265;
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x056d, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser) == false) goto L280;
     */
    /* JADX WARN: Code restructure failed: missing block: B:267:0x056f, code lost:
        r1 = r2.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:268:0x0573, code lost:
        if (r1 != r19) goto L271;
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x0588, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.R.string.NotificationGroupKickYou, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x058e, code lost:
        if (r1 != r9) goto L275;
     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x05a0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.R.string.NotificationGroupLeftMember, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:275:0x05a1, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r26.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x05b3, code lost:
        if (r0 != null) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:277:0x05b5, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x05d1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.R.string.NotificationGroupKickMember, r7, r8.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x05d4, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatCreate) == false) goto L284;
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x05dc, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x05df, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate) == false) goto L288;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x05e7, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x05ea, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo) == false) goto L292;
     */
    /* JADX WARN: Code restructure failed: missing block: B:291:0x05fc, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x0601, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) == false) goto L296;
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x0611, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x0614, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken) == false) goto L300;
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x061c, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x061f, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPinMessage) == false) goto L622;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x0625, code lost:
        if (r8 == null) goto L412;
     */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x062b, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r8) == false) goto L308;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x062f, code lost:
        if (r8.megagroup == false) goto L412;
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x0631, code lost:
        r0 = r26.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x0633, code lost:
        if (r0 != null) goto L312;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x0648, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x0650, code lost:
        if (r0.isMusic() == false) goto L316;
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x0662, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.R.string.NotificationActionPinnedMusic, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x0669, code lost:
        if (r0.isVideo() == false) goto L326;
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x066f, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L324;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x0679, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L324;
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x06a2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "📹 " + r0.messageOwner.message, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x06b6, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.R.string.NotificationActionPinnedVideo, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x06bb, code lost:
        if (r0.isGif() == false) goto L336;
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x06c1, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x06cb, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x06f4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "🎬 " + r0.messageOwner.message, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x0708, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.R.string.NotificationActionPinnedGif, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x0710, code lost:
        if (r0.isVoice() == false) goto L340;
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x0722, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.R.string.NotificationActionPinnedVoice, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x0727, code lost:
        if (r0.isRoundVideo() == false) goto L344;
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x0739, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.R.string.NotificationActionPinnedRound, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x073e, code lost:
        if (r0.isSticker() != false) goto L406;
     */
    /* JADX WARN: Code restructure failed: missing block: B:347:0x0744, code lost:
        if (r0.isAnimatedSticker() == false) goto L348;
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x0748, code lost:
        r3 = r0.messageOwner;
        r5 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x074e, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L358;
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x0754, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L356;
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x075c, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L356;
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x0785, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "📎 " + r0.messageOwner.message, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x0799, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.R.string.NotificationActionPinnedFile, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x079c, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L404;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x07a0, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L362;
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x07a6, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x07bb, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.R.string.NotificationActionPinnedGeoLive, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x07c0, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L370;
     */
    /* JADX WARN: Code restructure failed: missing block: B:368:0x07c2, code lost:
        r5 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:369:0x07e0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.R.string.NotificationActionPinnedContact2, r7, r8.title, org.telegram.messenger.ContactsController.formatName(r5.first_name, r5.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x07e3, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L378;
     */
    /* JADX WARN: Code restructure failed: missing block: B:372:0x07e5, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r5).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x07eb, code lost:
        if (r0.quiz == false) goto L376;
     */
    /* JADX WARN: Code restructure failed: missing block: B:375:0x0805, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.R.string.NotificationActionPinnedQuiz2, r7, r8.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:377:0x081e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.R.string.NotificationActionPinnedPoll2, r7, r8.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x0821, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L388;
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x0827, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L386;
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x082f, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L386;
     */
    /* JADX WARN: Code restructure failed: missing block: B:385:0x0858, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "🖼 " + r0.messageOwner.message, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:387:0x086c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.R.string.NotificationActionPinnedPhoto, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x0872, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L392;
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x0884, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.R.string.NotificationActionPinnedGame, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x0885, code lost:
        r3 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:393:0x0887, code lost:
        if (r3 == null) goto L402;
     */
    /* JADX WARN: Code restructure failed: missing block: B:395:0x088d, code lost:
        if (r3.length() <= 0) goto L402;
     */
    /* JADX WARN: Code restructure failed: missing block: B:396:0x088f, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:397:0x0895, code lost:
        if (r0.length() <= 20) goto L401;
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:0x0897, code lost:
        r3 = new java.lang.StringBuilder();
        r5 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:399:0x08ad, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedText;
        r2 = new java.lang.Object[3];
        r2[r5] = r7;
        r2[1] = r0;
        r2[2] = r8.title;
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x08c0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x08ac, code lost:
        r5 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:403:0x08d4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:405:0x08e8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.R.string.NotificationActionPinnedGeo, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:406:0x08e9, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x08ef, code lost:
        if (r0 == null) goto L410;
     */
    /* JADX WARN: Code restructure failed: missing block: B:409:0x0905, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmoji, r7, r8.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x0917, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.R.string.NotificationActionPinnedSticker, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x0919, code lost:
        if (r8 == null) goto L518;
     */
    /* JADX WARN: Code restructure failed: missing block: B:414:0x091b, code lost:
        r0 = r26.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:415:0x091d, code lost:
        if (r0 != null) goto L418;
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x092e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x0934, code lost:
        if (r0.isMusic() == false) goto L422;
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x0944, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.R.string.NotificationActionPinnedMusicChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x094b, code lost:
        if (r0.isVideo() == false) goto L432;
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x0951, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L430;
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x095b, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L430;
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x0981, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r8.title, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x0992, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.R.string.NotificationActionPinnedVideoChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:433:0x0997, code lost:
        if (r0.isGif() == false) goto L442;
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x099d, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L440;
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x09a7, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L440;
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x09cd, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r8.title, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:441:0x09de, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.R.string.NotificationActionPinnedGifChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:443:0x09e5, code lost:
        if (r0.isVoice() == false) goto L446;
     */
    /* JADX WARN: Code restructure failed: missing block: B:445:0x09f5, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.R.string.NotificationActionPinnedVoiceChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:447:0x09fa, code lost:
        if (r0.isRoundVideo() == false) goto L450;
     */
    /* JADX WARN: Code restructure failed: missing block: B:449:0x0a0a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.R.string.NotificationActionPinnedRoundChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00b3, code lost:
        if (r12.getBoolean("EnablePreviewGroup", true) != false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:451:0x0a0f, code lost:
        if (r0.isSticker() != false) goto L512;
     */
    /* JADX WARN: Code restructure failed: missing block: B:453:0x0a15, code lost:
        if (r0.isAnimatedSticker() == false) goto L454;
     */
    /* JADX WARN: Code restructure failed: missing block: B:454:0x0a19, code lost:
        r3 = r0.messageOwner;
        r5 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:455:0x0a1f, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L464;
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:0x0a25, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L462;
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x0a2d, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L462;
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x0a53, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r8.title, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0a64, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.R.string.NotificationActionPinnedFileChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x0a67, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L510;
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x0a6b, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L468;
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x0a71, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L472;
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x0a83, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x0a87, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L476;
     */
    /* JADX WARN: Code restructure failed: missing block: B:474:0x0a89, code lost:
        r5 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:475:0x0aa5, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.R.string.NotificationActionPinnedContactChannel2, r8.title, org.telegram.messenger.ContactsController.formatName(r5.first_name, r5.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x0aa8, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L484;
     */
    /* JADX WARN: Code restructure failed: missing block: B:478:0x0aaa, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r5).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x0ab0, code lost:
        if (r0.quiz == false) goto L482;
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x0ac7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.R.string.NotificationActionPinnedQuizChannel2, r8.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0add, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.R.string.NotificationActionPinnedPollChannel2, r8.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0ae0, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L494;
     */
    /* JADX WARN: Code restructure failed: missing block: B:487:0x0ae6, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L492;
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x0aee, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L492;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00bf, code lost:
        if (r12.getBoolean("EnablePreviewChannel", r2) == false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x0b14, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r8.title, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x0b25, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.R.string.NotificationActionPinnedPhotoChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:0x0b2a, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L498;
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x0b3a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.R.string.NotificationActionPinnedGameChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:498:0x0b3b, code lost:
        r3 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:499:0x0b3d, code lost:
        if (r3 == null) goto L508;
     */
    /* JADX WARN: Code restructure failed: missing block: B:501:0x0b43, code lost:
        if (r3.length() <= 0) goto L508;
     */
    /* JADX WARN: Code restructure failed: missing block: B:502:0x0b45, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:503:0x0b4b, code lost:
        if (r0.length() <= 20) goto L507;
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0b4d, code lost:
        r3 = new java.lang.StringBuilder();
        r9 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:505:0x0b63, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextChannel;
        r2 = new java.lang.Object[2];
        r2[r9] = r8.title;
        r2[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:506:0x0b73, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:507:0x0b62, code lost:
        r9 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:509:0x0b84, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:511:0x0b95, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:512:0x0b96, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x0b9b, code lost:
        if (r0 == null) goto L516;
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x0baf, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiChannel, r8.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0bbf, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x0bc0, code lost:
        r0 = r26.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:519:0x0bc3, code lost:
        if (r0 != null) goto L522;
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x0bd1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.R.string.NotificationActionPinnedNoTextUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x0bd6, code lost:
        if (r0.isMusic() == false) goto L526;
     */
    /* JADX WARN: Code restructure failed: missing block: B:525:0x0be4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicUser", org.telegram.messenger.R.string.NotificationActionPinnedMusicUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:527:0x0beb, code lost:
        if (r0.isVideo() == false) goto L536;
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x0bf1, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L534;
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x0bfb, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L534;
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0c1f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r7, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0c2e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoUser", org.telegram.messenger.R.string.NotificationActionPinnedVideoUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0c33, code lost:
        if (r0.isGif() == false) goto L546;
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0c39, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L544;
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x0c43, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L544;
     */
    /* JADX WARN: Code restructure failed: missing block: B:543:0x0c67, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r7, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x0c76, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifUser", org.telegram.messenger.R.string.NotificationActionPinnedGifUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0c7d, code lost:
        if (r0.isVoice() == false) goto L550;
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0c8b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceUser", org.telegram.messenger.R.string.NotificationActionPinnedVoiceUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:551:0x0c90, code lost:
        if (r0.isRoundVideo() == false) goto L554;
     */
    /* JADX WARN: Code restructure failed: missing block: B:553:0x0c9e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundUser", org.telegram.messenger.R.string.NotificationActionPinnedRoundUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:555:0x0ca3, code lost:
        if (r0.isSticker() != false) goto L616;
     */
    /* JADX WARN: Code restructure failed: missing block: B:557:0x0ca9, code lost:
        if (r0.isAnimatedSticker() == false) goto L558;
     */
    /* JADX WARN: Code restructure failed: missing block: B:558:0x0cad, code lost:
        r3 = r0.messageOwner;
        r5 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:559:0x0cb3, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L568;
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0cb9, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L566;
     */
    /* JADX WARN: Code restructure failed: missing block: B:563:0x0cc1, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L566;
     */
    /* JADX WARN: Code restructure failed: missing block: B:565:0x0ce5, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r7, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:567:0x0cf4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileUser", org.telegram.messenger.R.string.NotificationActionPinnedFileUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x0cf7, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L614;
     */
    /* JADX WARN: Code restructure failed: missing block: B:571:0x0cfb, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L572;
     */
    /* JADX WARN: Code restructure failed: missing block: B:573:0x0d01, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L576;
     */
    /* JADX WARN: Code restructure failed: missing block: B:575:0x0d11, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveUser", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:577:0x0d15, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L580;
     */
    /* JADX WARN: Code restructure failed: missing block: B:578:0x0d17, code lost:
        r5 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:579:0x0d31, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactUser", org.telegram.messenger.R.string.NotificationActionPinnedContactUser, r7, org.telegram.messenger.ContactsController.formatName(r5.first_name, r5.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:581:0x0d34, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L588;
     */
    /* JADX WARN: Code restructure failed: missing block: B:582:0x0d36, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r5).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:583:0x0d3c, code lost:
        if (r0.quiz == false) goto L586;
     */
    /* JADX WARN: Code restructure failed: missing block: B:585:0x0d51, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizUser", org.telegram.messenger.R.string.NotificationActionPinnedQuizUser, r7, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:587:0x0d65, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollUser", org.telegram.messenger.R.string.NotificationActionPinnedPollUser, r7, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:589:0x0d68, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L598;
     */
    /* JADX WARN: Code restructure failed: missing block: B:591:0x0d6e, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L596;
     */
    /* JADX WARN: Code restructure failed: missing block: B:593:0x0d76, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L596;
     */
    /* JADX WARN: Code restructure failed: missing block: B:595:0x0d9a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r7, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:597:0x0da9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoUser", org.telegram.messenger.R.string.NotificationActionPinnedPhotoUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:599:0x0dae, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L602;
     */
    /* JADX WARN: Code restructure failed: missing block: B:601:0x0dbc, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameUser", org.telegram.messenger.R.string.NotificationActionPinnedGameUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:602:0x0dbd, code lost:
        r3 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:603:0x0dbf, code lost:
        if (r3 == null) goto L612;
     */
    /* JADX WARN: Code restructure failed: missing block: B:605:0x0dc5, code lost:
        if (r3.length() <= 0) goto L612;
     */
    /* JADX WARN: Code restructure failed: missing block: B:606:0x0dc7, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:607:0x0dcd, code lost:
        if (r0.length() <= 20) goto L611;
     */
    /* JADX WARN: Code restructure failed: missing block: B:608:0x0dcf, code lost:
        r3 = new java.lang.StringBuilder();
        r5 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:609:0x0de5, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextUser;
        r2 = new java.lang.Object[2];
        r2[r5] = r7;
        r2[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:610:0x0df3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:611:0x0de4, code lost:
        r5 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:613:0x0e02, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.R.string.NotificationActionPinnedNoTextUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:615:0x0e11, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoUser", org.telegram.messenger.R.string.NotificationActionPinnedGeoUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:616:0x0e12, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:617:0x0e18, code lost:
        if (r0 == null) goto L620;
     */
    /* JADX WARN: Code restructure failed: missing block: B:619:0x0e29, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiUser", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiUser, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:621:0x0e36, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerUser", org.telegram.messenger.R.string.NotificationActionPinnedStickerUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:623:0x0e39, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) == false) goto L638;
     */
    /* JADX WARN: Code restructure failed: missing block: B:624:0x0e3b, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) r2).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x0e43, code lost:
        if (android.text.TextUtils.isEmpty(r0) == false) goto L632;
     */
    /* JADX WARN: Code restructure failed: missing block: B:627:0x0e47, code lost:
        if (r3 != r19) goto L630;
     */
    /* JADX WARN: Code restructure failed: missing block: B:629:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.R.string.ChatThemeDisabled, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:633:0x0e6b, code lost:
        if (r3 != r19) goto L636;
     */
    /* JADX WARN: Code restructure failed: missing block: B:635:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChangedChatThemeYou", org.telegram.messenger.R.string.ChatThemeChangedYou, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:637:0x0e89, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.R.string.ChatThemeChangedTo, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:639:0x0e8c, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest) == false) goto L642;
     */
    /* JADX WARN: Code restructure failed: missing block: B:641:0x0e94, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:642:0x0e95, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:644:0x0e9f, code lost:
        if (r1.peer_id.channel_id == 0) goto L653;
     */
    /* JADX WARN: Code restructure failed: missing block: B:646:0x0ea3, code lost:
        if (r8.megagroup != false) goto L653;
     */
    /* JADX WARN: Code restructure failed: missing block: B:648:0x0ea9, code lost:
        if (r26.isVideoAvatar() == false) goto L651;
     */
    /* JADX WARN: Code restructure failed: missing block: B:650:0x0ebb, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.R.string.ChannelVideoEditNotification, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:652:0x0ecc, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.R.string.ChannelPhotoEditNotification, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:654:0x0ed2, code lost:
        if (r26.isVideoAvatar() == false) goto L657;
     */
    /* JADX WARN: Code restructure failed: missing block: B:656:0x0ee6, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.R.string.NotificationEditedGroupVideo, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:658:0x0ef9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.R.string.NotificationEditedGroupPhoto, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:660:0x0f00, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:662:0x0f0f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationContactJoined", org.telegram.messenger.R.string.NotificationContactJoined, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:664:0x0f14, code lost:
        if (r26.isMediaEmpty() == false) goto L671;
     */
    /* JADX WARN: Code restructure failed: missing block: B:666:0x0f1e, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageOwner.message) != false) goto L669;
     */
    /* JADX WARN: Code restructure failed: missing block: B:668:0x0f24, code lost:
        return replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:670:0x0f2d, code lost:
        return org.telegram.messenger.LocaleController.getString(r23, org.telegram.messenger.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:671:0x0f2e, code lost:
        r1 = r23;
        r2 = r26.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:672:0x0f36, code lost:
        if ((r2.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L685;
     */
    /* JADX WARN: Code restructure failed: missing block: B:674:0x0f3c, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L679;
     */
    /* JADX WARN: Code restructure failed: missing block: B:676:0x0f44, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L679;
     */
    /* JADX WARN: Code restructure failed: missing block: B:678:0x0f59, code lost:
        return "🖼 " + replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:680:0x0f60, code lost:
        if (r26.messageOwner.media.ttl_seconds == 0) goto L683;
     */
    /* JADX WARN: Code restructure failed: missing block: B:682:0x0f6a, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDestructingPhoto", org.telegram.messenger.R.string.AttachDestructingPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:684:0x0f73, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachPhoto", org.telegram.messenger.R.string.AttachPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:686:0x0f78, code lost:
        if (r26.isVideo() == false) goto L699;
     */
    /* JADX WARN: Code restructure failed: missing block: B:688:0x0f7e, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L693;
     */
    /* JADX WARN: Code restructure failed: missing block: B:690:0x0f88, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageOwner.message) != false) goto L693;
     */
    /* JADX WARN: Code restructure failed: missing block: B:692:0x0f9d, code lost:
        return "📹 " + replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:694:0x0fa4, code lost:
        if (r26.messageOwner.media.ttl_seconds == 0) goto L697;
     */
    /* JADX WARN: Code restructure failed: missing block: B:696:0x0fae, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDestructingVideo", org.telegram.messenger.R.string.AttachDestructingVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:698:0x0fb7, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachVideo", org.telegram.messenger.R.string.AttachVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:700:0x0fbc, code lost:
        if (r26.isGame() == false) goto L703;
     */
    /* JADX WARN: Code restructure failed: missing block: B:702:0x0fc6, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachGame", org.telegram.messenger.R.string.AttachGame);
     */
    /* JADX WARN: Code restructure failed: missing block: B:704:0x0fcb, code lost:
        if (r26.isVoice() == false) goto L707;
     */
    /* JADX WARN: Code restructure failed: missing block: B:706:0x0fd5, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachAudio", org.telegram.messenger.R.string.AttachAudio);
     */
    /* JADX WARN: Code restructure failed: missing block: B:708:0x0fda, code lost:
        if (r26.isRoundVideo() == false) goto L711;
     */
    /* JADX WARN: Code restructure failed: missing block: B:710:0x0fe4, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachRound", org.telegram.messenger.R.string.AttachRound);
     */
    /* JADX WARN: Code restructure failed: missing block: B:712:0x0fe9, code lost:
        if (r26.isMusic() == false) goto L715;
     */
    /* JADX WARN: Code restructure failed: missing block: B:714:0x0ff3, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachMusic", org.telegram.messenger.R.string.AttachMusic);
     */
    /* JADX WARN: Code restructure failed: missing block: B:715:0x0ff4, code lost:
        r2 = r26.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:716:0x0ffa, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L719;
     */
    /* JADX WARN: Code restructure failed: missing block: B:718:0x1004, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachContact", org.telegram.messenger.R.string.AttachContact);
     */
    /* JADX WARN: Code restructure failed: missing block: B:720:0x1007, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L727;
     */
    /* JADX WARN: Code restructure failed: missing block: B:722:0x100f, code lost:
        if (((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r2).poll.quiz == false) goto L725;
     */
    /* JADX WARN: Code restructure failed: missing block: B:724:0x1019, code lost:
        return org.telegram.messenger.LocaleController.getString("QuizPoll", org.telegram.messenger.R.string.QuizPoll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x1022, code lost:
        return org.telegram.messenger.LocaleController.getString("Poll", org.telegram.messenger.R.string.Poll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:728:0x1025, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L771;
     */
    /* JADX WARN: Code restructure failed: missing block: B:730:0x1029, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L731;
     */
    /* JADX WARN: Code restructure failed: missing block: B:732:0x102f, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L735;
     */
    /* JADX WARN: Code restructure failed: missing block: B:734:0x1039, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachLiveLocation", org.telegram.messenger.R.string.AttachLiveLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:736:0x103c, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L765;
     */
    /* JADX WARN: Code restructure failed: missing block: B:738:0x1042, code lost:
        if (r26.isSticker() != false) goto L759;
     */
    /* JADX WARN: Code restructure failed: missing block: B:740:0x1048, code lost:
        if (r26.isAnimatedSticker() == false) goto L741;
     */
    /* JADX WARN: Code restructure failed: missing block: B:742:0x104f, code lost:
        if (r26.isGif() == false) goto L751;
     */
    /* JADX WARN: Code restructure failed: missing block: B:744:0x1055, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L749;
     */
    /* JADX WARN: Code restructure failed: missing block: B:746:0x105f, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageOwner.message) != false) goto L749;
     */
    /* JADX WARN: Code restructure failed: missing block: B:748:0x1074, code lost:
        return "🎬 " + replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:750:0x107d, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachGif", org.telegram.messenger.R.string.AttachGif);
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x1082, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L757;
     */
    /* JADX WARN: Code restructure failed: missing block: B:754:0x108c, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageOwner.message) != false) goto L757;
     */
    /* JADX WARN: Code restructure failed: missing block: B:756:0x10a1, code lost:
        return "📎 " + replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:758:0x10aa, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDocument", org.telegram.messenger.R.string.AttachDocument);
     */
    /* JADX WARN: Code restructure failed: missing block: B:759:0x10ab, code lost:
        r0 = r26.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:760:0x10af, code lost:
        if (r0 == null) goto L763;
     */
    /* JADX WARN: Code restructure failed: missing block: B:762:0x10cd, code lost:
        return r0 + " " + org.telegram.messenger.LocaleController.getString("AttachSticker", org.telegram.messenger.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:764:0x10d6, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachSticker", org.telegram.messenger.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:766:0x10dd, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageText) != false) goto L769;
     */
    /* JADX WARN: Code restructure failed: missing block: B:768:0x10e3, code lost:
        return replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:770:0x10ea, code lost:
        return org.telegram.messenger.LocaleController.getString(r1, org.telegram.messenger.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:772:0x10f3, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachLocation", org.telegram.messenger.R.string.AttachLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:777:0x025d, code lost:
        if (r12.getBoolean("EnablePreviewGroup", r15) != false) goto L135;
     */
    /* JADX WARN: Code restructure failed: missing block: B:780:0x0267, code lost:
        if (r12.getBoolean(r24, r15) != false) goto L135;
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
                        char c2 = z ? 1 : 0;
                        char c3 = z ? 1 : 0;
                        char c4 = z ? 1 : 0;
                        zArr[c2] = z;
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
        TLRPC$Message tLRPC$Message = messageObject.messageOwner;
        String str = tLRPC$Message.message;
        if (str == null || tLRPC$Message == null || tLRPC$Message.entities == null) {
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

    /* JADX WARN: Code restructure failed: missing block: B:274:0x060a, code lost:
        if (r12.getBoolean(r22, true) == false) goto L820;
     */
    /* JADX WARN: Code restructure failed: missing block: B:275:0x0618, code lost:
        r6 = r29.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x061c, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageService) == false) goto L611;
     */
    /* JADX WARN: Code restructure failed: missing block: B:277:0x061e, code lost:
        r7 = r6.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x0622, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser) == false) goto L318;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x0624, code lost:
        r3 = r7.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x062a, code lost:
        if (r3 != 0) goto L284;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x0633, code lost:
        if (r7.users.size() != 1) goto L284;
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x0635, code lost:
        r3 = r29.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x064a, code lost:
        if (r3 == 0) goto L304;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x0654, code lost:
        if (r29.messageOwner.peer_id.channel_id == 0) goto L291;
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x0658, code lost:
        if (r5.megagroup != false) goto L291;
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x065a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.R.string.ChannelAddedByNotification, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x0671, code lost:
        if (r3 != r19) goto L294;
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x0673, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.R.string.NotificationInvitedToGroup, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x0688, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x0694, code lost:
        if (r0 != null) goto L297;
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x0696, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x069c, code lost:
        if (r9 != r0.id) goto L303;
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x06a0, code lost:
        if (r5.megagroup == false) goto L302;
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x06a2, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.R.string.NotificationGroupAddSelfMega, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x06b7, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.R.string.NotificationGroupAddSelf, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x06cc, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r2, r5.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x06e8, code lost:
        r1 = new java.lang.StringBuilder();
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x06f8, code lost:
        if (r3 >= r29.messageOwner.action.users.size()) goto L316;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x06fa, code lost:
        r4 = getMessagesController().getUser(r29.messageOwner.action.users.get(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x070e, code lost:
        if (r4 == null) goto L315;
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x0710, code lost:
        r4 = org.telegram.messenger.UserObject.getUserName(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x0718, code lost:
        if (r1.length() == 0) goto L312;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x071a, code lost:
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x071f, code lost:
        r1.append(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x0722, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x0725, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r2, r5.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x0744, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCall) == false) goto L322;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.R.string.NotificationGroupCreatedCall, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x075c, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled) == false) goto L326;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x0768, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall) == false) goto L356;
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x076a, code lost:
        r3 = r7.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x0770, code lost:
        if (r3 != 0) goto L333;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x0779, code lost:
        if (r7.users.size() != 1) goto L333;
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x077b, code lost:
        r3 = r29.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x0790, code lost:
        if (r3 == 0) goto L342;
     */
    /* JADX WARN: Code restructure failed: missing block: B:336:0x0794, code lost:
        if (r3 != r19) goto L338;
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x0796, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.R.string.NotificationGroupInvitedYouToCall, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x07ab, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x07b7, code lost:
        if (r0 != null) goto L341;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x07b9, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x07bb, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r2, r5.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x07d7, code lost:
        r1 = new java.lang.StringBuilder();
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:344:0x07e7, code lost:
        if (r3 >= r29.messageOwner.action.users.size()) goto L354;
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x07e9, code lost:
        r4 = getMessagesController().getUser(r29.messageOwner.action.users.get(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x07fd, code lost:
        if (r4 == null) goto L353;
     */
    /* JADX WARN: Code restructure failed: missing block: B:347:0x07ff, code lost:
        r4 = org.telegram.messenger.UserObject.getUserName(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x0807, code lost:
        if (r1.length() == 0) goto L350;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x0809, code lost:
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x080e, code lost:
        r1.append(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x0811, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x0814, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r2, r5.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x0833, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink) == false) goto L360;
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.R.string.NotificationInvitedToGroupByLink, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x084c, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle) == false) goto L364;
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.R.string.NotificationEditedGroupName, r2, r7.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x0863, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto) != false) goto L595;
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x0867, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto) == false) goto L368;
     */
    /* JADX WARN: Code restructure failed: missing block: B:369:0x086d, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser) == false) goto L383;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x008f, code lost:
        if (r12.getBoolean("EnablePreviewGroup", true) != false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:370:0x086f, code lost:
        r3 = r7.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x0873, code lost:
        if (r3 != r19) goto L374;
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.R.string.NotificationGroupKickYou, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:375:0x088f, code lost:
        if (r3 != r9) goto L378;
     */
    /* JADX WARN: Code restructure failed: missing block: B:377:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.R.string.NotificationGroupLeftMember, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:378:0x08a3, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r29.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x08b5, code lost:
        if (r0 != null) goto L381;
     */
    /* JADX WARN: Code restructure failed: missing block: B:380:0x08b7, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:382:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.R.string.NotificationGroupKickMember, r2, r5.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x08d5, code lost:
        r9 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:384:0x08d8, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatCreate) == false) goto L387;
     */
    /* JADX WARN: Code restructure failed: missing block: B:386:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:388:0x08e4, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate) == false) goto L391;
     */
    /* JADX WARN: Code restructure failed: missing block: B:390:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x08f0, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo) == false) goto L395;
     */
    /* JADX WARN: Code restructure failed: missing block: B:394:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:396:0x0908, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) == false) goto L399;
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r7.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x091c, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken) == false) goto L403;
     */
    /* JADX WARN: Code restructure failed: missing block: B:402:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:404:0x0928, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPinMessage) == false) goto L575;
     */
    /* JADX WARN: Code restructure failed: missing block: B:406:0x0930, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r5) == false) goto L492;
     */
    /* JADX WARN: Code restructure failed: missing block: B:408:0x0934, code lost:
        if (r5.megagroup == false) goto L409;
     */
    /* JADX WARN: Code restructure failed: missing block: B:409:0x0938, code lost:
        r2 = r29.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x009d, code lost:
        if (r12.getBoolean("EnablePreviewChannel", r3) == false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:410:0x093a, code lost:
        if (r2 != null) goto L413;
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:414:0x0954, code lost:
        if (r2.isMusic() == false) goto L416;
     */
    /* JADX WARN: Code restructure failed: missing block: B:415:0x0956, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.R.string.NotificationActionPinnedMusicChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x096c, code lost:
        if (r2.isVideo() == false) goto L424;
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x0972, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L423;
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x097c, code lost:
        if (android.text.TextUtils.isEmpty(r2.messageOwner.message) != false) goto L423;
     */
    /* JADX WARN: Code restructure failed: missing block: B:422:0x097e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "📹 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x09a4, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.R.string.NotificationActionPinnedVideoChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x09ba, code lost:
        if (r2.isGif() == false) goto L432;
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x09c0, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L431;
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x09ca, code lost:
        if (android.text.TextUtils.isEmpty(r2.messageOwner.message) != false) goto L431;
     */
    /* JADX WARN: Code restructure failed: missing block: B:430:0x09cc, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "🎬 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x09f2, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.R.string.NotificationActionPinnedGifChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:433:0x0a0a, code lost:
        if (r2.isVoice() == false) goto L435;
     */
    /* JADX WARN: Code restructure failed: missing block: B:434:0x0a0c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.R.string.NotificationActionPinnedVoiceChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:436:0x0a20, code lost:
        if (r2.isRoundVideo() == false) goto L438;
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x0a22, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.R.string.NotificationActionPinnedRoundChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x0a36, code lost:
        if (r2.isSticker() != false) goto L488;
     */
    /* JADX WARN: Code restructure failed: missing block: B:441:0x0a3c, code lost:
        if (r2.isAnimatedSticker() == false) goto L442;
     */
    /* JADX WARN: Code restructure failed: missing block: B:442:0x0a40, code lost:
        r1 = r2.messageOwner;
        r3 = r1.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:443:0x0a46, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L450;
     */
    /* JADX WARN: Code restructure failed: missing block: B:445:0x0a4c, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L449;
     */
    /* JADX WARN: Code restructure failed: missing block: B:447:0x0a54, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L449;
     */
    /* JADX WARN: Code restructure failed: missing block: B:448:0x0a56, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "📎 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:449:0x0a7c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.R.string.NotificationActionPinnedFileChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:451:0x0a90, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L487;
     */
    /* JADX WARN: Code restructure failed: missing block: B:453:0x0a94, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L454;
     */
    /* JADX WARN: Code restructure failed: missing block: B:455:0x0a9a, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L457;
     */
    /* JADX WARN: Code restructure failed: missing block: B:456:0x0a9c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:458:0x0ab0, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L460;
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x0ab2, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r29.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.R.string.NotificationActionPinnedContactChannel2, r5.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x0ad7, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L466;
     */
    /* JADX WARN: Code restructure failed: missing block: B:462:0x0ad9, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r3).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0adf, code lost:
        if (r0.quiz == false) goto L465;
     */
    /* JADX WARN: Code restructure failed: missing block: B:464:0x0ae1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.R.string.NotificationActionPinnedQuizChannel2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x0af8, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.R.string.NotificationActionPinnedPollChannel2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x0b11, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L474;
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x0b17, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L473;
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x0b1f, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L473;
     */
    /* JADX WARN: Code restructure failed: missing block: B:472:0x0b21, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "🖼 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x0b47, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.R.string.NotificationActionPinnedPhotoChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:475:0x0b5d, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L477;
     */
    /* JADX WARN: Code restructure failed: missing block: B:476:0x0b5f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.R.string.NotificationActionPinnedGameChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x0b6f, code lost:
        r0 = r2.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:478:0x0b71, code lost:
        if (r0 == null) goto L486;
     */
    /* JADX WARN: Code restructure failed: missing block: B:480:0x0b77, code lost:
        if (r0.length() <= 0) goto L486;
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x0b79, code lost:
        r0 = r2.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:482:0x0b7f, code lost:
        if (r0.length() <= 20) goto L485;
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0b81, code lost:
        r1 = new java.lang.StringBuilder();
        r3 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:484:0x0b99, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextChannel;
        r2 = new java.lang.Object[2];
        r2[r3] = r5.title;
        r2[1] = r0;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0b98, code lost:
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:486:0x0bab, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:487:0x0bbd, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:488:0x0bcf, code lost:
        r0 = r2.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x0bd4, code lost:
        if (r0 == null) goto L491;
     */
    /* JADX WARN: Code restructure failed: missing block: B:490:0x0bd6, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiChannel, r5.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x0bea, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x0bfb, code lost:
        r6 = r29.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x0bfe, code lost:
        if (r6 != null) goto L496;
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x0c1a, code lost:
        if (r6.isMusic() == false) goto L499;
     */
    /* JADX WARN: Code restructure failed: missing block: B:498:0x0c1c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.R.string.NotificationActionPinnedMusic, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:500:0x0c34, code lost:
        if (r6.isVideo() == false) goto L507;
     */
    /* JADX WARN: Code restructure failed: missing block: B:502:0x0c3a, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L506;
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0c44, code lost:
        if (android.text.TextUtils.isEmpty(r6.messageOwner.message) != false) goto L506;
     */
    /* JADX WARN: Code restructure failed: missing block: B:505:0x0c46, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "📹 " + r6.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:506:0x0c6f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.R.string.NotificationActionPinnedVideo, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x0c88, code lost:
        if (r6.isGif() == false) goto L515;
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x0c8e, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L514;
     */
    /* JADX WARN: Code restructure failed: missing block: B:512:0x0c98, code lost:
        if (android.text.TextUtils.isEmpty(r6.messageOwner.message) != false) goto L514;
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x0c9a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "🎬 " + r6.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:514:0x0cc3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.R.string.NotificationActionPinnedGif, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:516:0x0cdf, code lost:
        if (r6.isVoice() == false) goto L518;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0ce1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.R.string.NotificationActionPinnedVoice, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:519:0x0cf7, code lost:
        if (r6.isRoundVideo() == false) goto L521;
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x0cf9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.R.string.NotificationActionPinnedRound, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:522:0x0d0f, code lost:
        if (r6.isSticker() != false) goto L571;
     */
    /* JADX WARN: Code restructure failed: missing block: B:524:0x0d15, code lost:
        if (r6.isAnimatedSticker() == false) goto L525;
     */
    /* JADX WARN: Code restructure failed: missing block: B:525:0x0d19, code lost:
        r1 = r6.messageOwner;
        r3 = r1.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:526:0x0d1f, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L533;
     */
    /* JADX WARN: Code restructure failed: missing block: B:528:0x0d25, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L532;
     */
    /* JADX WARN: Code restructure failed: missing block: B:530:0x0d2d, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L532;
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x0d2f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "📎 " + r6.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:532:0x0d58, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.R.string.NotificationActionPinnedFile, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:534:0x0d6f, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L570;
     */
    /* JADX WARN: Code restructure failed: missing block: B:536:0x0d73, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L537;
     */
    /* JADX WARN: Code restructure failed: missing block: B:538:0x0d79, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L540;
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0d7b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.R.string.NotificationActionPinnedGeoLive, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x0d92, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L543;
     */
    /* JADX WARN: Code restructure failed: missing block: B:542:0x0d94, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r29.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.R.string.NotificationActionPinnedContact2, r2, r5.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:544:0x0dbc, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L549;
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x0dbe, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r3).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:546:0x0dc4, code lost:
        if (r0.quiz == false) goto L548;
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0dc6, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.R.string.NotificationActionPinnedQuiz2, r2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:548:0x0de0, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.R.string.NotificationActionPinnedPoll2, r2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:550:0x0dfc, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L557;
     */
    /* JADX WARN: Code restructure failed: missing block: B:552:0x0e02, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L556;
     */
    /* JADX WARN: Code restructure failed: missing block: B:554:0x0e0a, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L556;
     */
    /* JADX WARN: Code restructure failed: missing block: B:555:0x0e0c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "🖼 " + r6.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:556:0x0e35, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.R.string.NotificationActionPinnedPhoto, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:558:0x0e4f, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L560;
     */
    /* JADX WARN: Code restructure failed: missing block: B:559:0x0e51, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.R.string.NotificationActionPinnedGame, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:560:0x0e63, code lost:
        r0 = r6.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0e65, code lost:
        if (r0 == null) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:563:0x0e6b, code lost:
        if (r0.length() <= 0) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:564:0x0e6d, code lost:
        r0 = r6.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:565:0x0e73, code lost:
        if (r0.length() <= 20) goto L568;
     */
    /* JADX WARN: Code restructure failed: missing block: B:566:0x0e75, code lost:
        r1 = new java.lang.StringBuilder();
        r3 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:567:0x0e8d, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedText;
        r4 = new java.lang.Object[3];
        r4[r3] = r2;
        r4[1] = r0;
        r4[2] = r5.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", r1, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:568:0x0e8c, code lost:
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x0ea2, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:570:0x0eb7, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.R.string.NotificationActionPinnedGeo, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:571:0x0ecc, code lost:
        r0 = r6.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:572:0x0ed2, code lost:
        if (r0 == null) goto L574;
     */
    /* JADX WARN: Code restructure failed: missing block: B:573:0x0ed4, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmoji, r2, r5.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:574:0x0eea, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.R.string.NotificationActionPinnedSticker, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:576:0x0eff, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGameScore) == false) goto L579;
     */
    /* JADX WARN: Code restructure failed: missing block: B:578:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:580:0x0f0b, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) == false) goto L591;
     */
    /* JADX WARN: Code restructure failed: missing block: B:581:0x0f0d, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) r7).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:582:0x0f15, code lost:
        if (android.text.TextUtils.isEmpty(r0) == false) goto L587;
     */
    /* JADX WARN: Code restructure failed: missing block: B:584:0x0f19, code lost:
        if (r3 != r19) goto L586;
     */
    /* JADX WARN: Code restructure failed: missing block: B:585:0x0f1b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:586:0x0f28, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.R.string.ChatThemeDisabled, r2, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:588:0x0f3f, code lost:
        if (r3 != r19) goto L590;
     */
    /* JADX WARN: Code restructure failed: missing block: B:589:0x0f41, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeYou", org.telegram.messenger.R.string.ChatThemeChangedYou, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:590:0x0f4f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.R.string.ChatThemeChangedTo, r2, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:592:0x0f62, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest) == false) goto L136;
     */
    /* JADX WARN: Code restructure failed: missing block: B:594:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:596:0x0f74, code lost:
        if (r6.peer_id.channel_id == 0) goto L605;
     */
    /* JADX WARN: Code restructure failed: missing block: B:598:0x0f78, code lost:
        if (r5.megagroup != false) goto L605;
     */
    /* JADX WARN: Code restructure failed: missing block: B:600:0x0f7e, code lost:
        if (r29.isVideoAvatar() == false) goto L603;
     */
    /* JADX WARN: Code restructure failed: missing block: B:602:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.R.string.ChannelVideoEditNotification, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:604:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.R.string.ChannelPhotoEditNotification, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:606:0x0fa9, code lost:
        if (r29.isVideoAvatar() == false) goto L609;
     */
    /* JADX WARN: Code restructure failed: missing block: B:608:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.R.string.NotificationEditedGroupVideo, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:610:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.R.string.NotificationEditedGroupPhoto, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:612:0x0fd7, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r5) == false) goto L716;
     */
    /* JADX WARN: Code restructure failed: missing block: B:614:0x0fdb, code lost:
        if (r5.megagroup != false) goto L716;
     */
    /* JADX WARN: Code restructure failed: missing block: B:616:0x0fe1, code lost:
        if (r29.isMediaEmpty() == false) goto L624;
     */
    /* JADX WARN: Code restructure failed: missing block: B:617:0x0fe3, code lost:
        if (r30 != false) goto L622;
     */
    /* JADX WARN: Code restructure failed: missing block: B:619:0x0fed, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L622;
     */
    /* JADX WARN: Code restructure failed: missing block: B:620:0x0fef, code lost:
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r2, r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:621:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:623:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.R.string.ChannelMessageNoText, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:624:0x1016, code lost:
        r4 = r29.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x101e, code lost:
        if ((r4.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L635;
     */
    /* JADX WARN: Code restructure failed: missing block: B:626:0x1020, code lost:
        if (r30 != false) goto L633;
     */
    /* JADX WARN: Code restructure failed: missing block: B:628:0x1026, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L633;
     */
    /* JADX WARN: Code restructure failed: missing block: B:630:0x102e, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L633;
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:0x1030, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r2, "🖼 " + r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:632:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:634:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessagePhoto", org.telegram.messenger.R.string.ChannelMessagePhoto, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:636:0x106a, code lost:
        if (r29.isVideo() == false) goto L646;
     */
    /* JADX WARN: Code restructure failed: missing block: B:637:0x106c, code lost:
        if (r30 != false) goto L644;
     */
    /* JADX WARN: Code restructure failed: missing block: B:639:0x1072, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L644;
     */
    /* JADX WARN: Code restructure failed: missing block: B:641:0x107c, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L644;
     */
    /* JADX WARN: Code restructure failed: missing block: B:642:0x107e, code lost:
        r3 = org.telegram.messenger.R.string.NotificationMessageText;
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r3, r2, "📹 " + r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:643:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:645:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageVideo", org.telegram.messenger.R.string.ChannelMessageVideo, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:647:0x10ba, code lost:
        if (r29.isVoice() == false) goto L650;
     */
    /* JADX WARN: Code restructure failed: missing block: B:649:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageAudio", org.telegram.messenger.R.string.ChannelMessageAudio, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:651:0x10ce, code lost:
        if (r29.isRoundVideo() == false) goto L654;
     */
    /* JADX WARN: Code restructure failed: missing block: B:653:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageRound", org.telegram.messenger.R.string.ChannelMessageRound, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:655:0x10e2, code lost:
        if (r29.isMusic() == false) goto L658;
     */
    /* JADX WARN: Code restructure failed: missing block: B:657:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageMusic", org.telegram.messenger.R.string.ChannelMessageMusic, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:658:0x10f2, code lost:
        r1 = r29.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:659:0x10f8, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L662;
     */
    /* JADX WARN: Code restructure failed: missing block: B:660:0x10fa, code lost:
        r1 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:661:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageContact2", org.telegram.messenger.R.string.ChannelMessageContact2, r2, org.telegram.messenger.ContactsController.formatName(r1.first_name, r1.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:663:0x1118, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L668;
     */
    /* JADX WARN: Code restructure failed: missing block: B:664:0x111a, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r1).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:665:0x1120, code lost:
        if (r0.quiz == false) goto L667;
     */
    /* JADX WARN: Code restructure failed: missing block: B:666:0x1122, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageQuiz2", org.telegram.messenger.R.string.ChannelMessageQuiz2, r2, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:667:0x1137, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessagePoll2", org.telegram.messenger.R.string.ChannelMessagePoll2, r2, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:669:0x114e, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L714;
     */
    /* JADX WARN: Code restructure failed: missing block: B:671:0x1152, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L672;
     */
    /* JADX WARN: Code restructure failed: missing block: B:673:0x1158, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L676;
     */
    /* JADX WARN: Code restructure failed: missing block: B:675:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageLiveLocation", org.telegram.messenger.R.string.ChannelMessageLiveLocation, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:677:0x116c, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L706;
     */
    /* JADX WARN: Code restructure failed: missing block: B:679:0x1172, code lost:
        if (r29.isSticker() != false) goto L702;
     */
    /* JADX WARN: Code restructure failed: missing block: B:681:0x1178, code lost:
        if (r29.isAnimatedSticker() == false) goto L682;
     */
    /* JADX WARN: Code restructure failed: missing block: B:683:0x1180, code lost:
        if (r29.isGif() == false) goto L693;
     */
    /* JADX WARN: Code restructure failed: missing block: B:684:0x1182, code lost:
        if (r30 != false) goto L691;
     */
    /* JADX WARN: Code restructure failed: missing block: B:686:0x1188, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L691;
     */
    /* JADX WARN: Code restructure failed: missing block: B:688:0x1192, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L691;
     */
    /* JADX WARN: Code restructure failed: missing block: B:689:0x1194, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r2, "🎬 " + r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:690:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:692:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageGIF", org.telegram.messenger.R.string.ChannelMessageGIF, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:693:0x11ca, code lost:
        if (r30 != false) goto L700;
     */
    /* JADX WARN: Code restructure failed: missing block: B:695:0x11d0, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L700;
     */
    /* JADX WARN: Code restructure failed: missing block: B:697:0x11da, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L700;
     */
    /* JADX WARN: Code restructure failed: missing block: B:698:0x11dc, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r2, "📎 " + r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:699:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:701:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageDocument", org.telegram.messenger.R.string.ChannelMessageDocument, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:702:0x1212, code lost:
        r0 = r29.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:703:0x1218, code lost:
        if (r0 == null) goto L705;
     */
    /* JADX WARN: Code restructure failed: missing block: B:704:0x121a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageStickerEmoji", org.telegram.messenger.R.string.ChannelMessageStickerEmoji, r2, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:705:0x122b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageSticker", org.telegram.messenger.R.string.ChannelMessageSticker, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:707:0x123a, code lost:
        if (r30 != false) goto L712;
     */
    /* JADX WARN: Code restructure failed: missing block: B:709:0x1242, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageText) != false) goto L712;
     */
    /* JADX WARN: Code restructure failed: missing block: B:710:0x1244, code lost:
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r2, r29.messageText);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:711:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:713:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.R.string.ChannelMessageNoText, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:715:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageMap", org.telegram.messenger.R.string.ChannelMessageMap, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:717:0x127b, code lost:
        if (r29.isMediaEmpty() == false) goto L725;
     */
    /* JADX WARN: Code restructure failed: missing block: B:718:0x127d, code lost:
        if (r30 != false) goto L723;
     */
    /* JADX WARN: Code restructure failed: missing block: B:720:0x1287, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L723;
     */
    /* JADX WARN: Code restructure failed: missing block: B:722:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r2, r5.title, r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:724:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupNoText", org.telegram.messenger.R.string.NotificationMessageGroupNoText, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:725:0x12b8, code lost:
        r3 = r29.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x12c0, code lost:
        if ((r3.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L736;
     */
    /* JADX WARN: Code restructure failed: missing block: B:727:0x12c2, code lost:
        if (r30 != false) goto L734;
     */
    /* JADX WARN: Code restructure failed: missing block: B:729:0x12c8, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L734;
     */
    /* JADX WARN: Code restructure failed: missing block: B:731:0x12d0, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L734;
     */
    /* JADX WARN: Code restructure failed: missing block: B:732:0x12d2, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:733:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r2, r5.title, "🖼 " + r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:735:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPhoto", org.telegram.messenger.R.string.NotificationMessageGroupPhoto, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:737:0x1314, code lost:
        if (r29.isVideo() == false) goto L747;
     */
    /* JADX WARN: Code restructure failed: missing block: B:738:0x1316, code lost:
        if (r30 != false) goto L745;
     */
    /* JADX WARN: Code restructure failed: missing block: B:740:0x131c, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L745;
     */
    /* JADX WARN: Code restructure failed: missing block: B:742:0x1326, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L745;
     */
    /* JADX WARN: Code restructure failed: missing block: B:743:0x1328, code lost:
        r3 = org.telegram.messenger.R.string.NotificationMessageGroupText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:744:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r3, r2, r5.title, "📹 " + r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:746:?, code lost:
        return org.telegram.messenger.LocaleController.formatString(" ", org.telegram.messenger.R.string.NotificationMessageGroupVideo, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:748:0x136d, code lost:
        if (r29.isVoice() == false) goto L751;
     */
    /* JADX WARN: Code restructure failed: missing block: B:750:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupAudio", org.telegram.messenger.R.string.NotificationMessageGroupAudio, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x1385, code lost:
        if (r29.isRoundVideo() == false) goto L755;
     */
    /* JADX WARN: Code restructure failed: missing block: B:754:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupRound", org.telegram.messenger.R.string.NotificationMessageGroupRound, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:756:0x139d, code lost:
        if (r29.isMusic() == false) goto L759;
     */
    /* JADX WARN: Code restructure failed: missing block: B:758:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMusic", org.telegram.messenger.R.string.NotificationMessageGroupMusic, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:759:0x13b1, code lost:
        r1 = r29.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:760:0x13b7, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L763;
     */
    /* JADX WARN: Code restructure failed: missing block: B:761:0x13b9, code lost:
        r1 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:762:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupContact2", org.telegram.messenger.R.string.NotificationMessageGroupContact2, r2, r5.title, org.telegram.messenger.ContactsController.formatName(r1.first_name, r1.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:764:0x13dd, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L769;
     */
    /* JADX WARN: Code restructure failed: missing block: B:765:0x13df, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r1).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:766:0x13e5, code lost:
        if (r0.quiz == false) goto L768;
     */
    /* JADX WARN: Code restructure failed: missing block: B:767:0x13e7, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupQuiz2", org.telegram.messenger.R.string.NotificationMessageGroupQuiz2, r2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:768:0x1401, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPoll2", org.telegram.messenger.R.string.NotificationMessageGroupPoll2, r2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:770:0x141d, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L773;
     */
    /* JADX WARN: Code restructure failed: missing block: B:772:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGame", org.telegram.messenger.R.string.NotificationMessageGroupGame, r2, r5.title, r1.game.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:774:0x143d, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L818;
     */
    /* JADX WARN: Code restructure failed: missing block: B:776:0x1441, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L777;
     */
    /* JADX WARN: Code restructure failed: missing block: B:778:0x1447, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L781;
     */
    /* JADX WARN: Code restructure failed: missing block: B:780:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupLiveLocation", org.telegram.messenger.R.string.NotificationMessageGroupLiveLocation, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:782:0x1460, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L811;
     */
    /* JADX WARN: Code restructure failed: missing block: B:784:0x1466, code lost:
        if (r29.isSticker() != false) goto L807;
     */
    /* JADX WARN: Code restructure failed: missing block: B:786:0x146c, code lost:
        if (r29.isAnimatedSticker() == false) goto L787;
     */
    /* JADX WARN: Code restructure failed: missing block: B:788:0x1474, code lost:
        if (r29.isGif() == false) goto L798;
     */
    /* JADX WARN: Code restructure failed: missing block: B:789:0x1476, code lost:
        if (r30 != false) goto L796;
     */
    /* JADX WARN: Code restructure failed: missing block: B:791:0x147c, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L796;
     */
    /* JADX WARN: Code restructure failed: missing block: B:793:0x1486, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L796;
     */
    /* JADX WARN: Code restructure failed: missing block: B:794:0x1488, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:795:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r2, r5.title, "🎬 " + r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:797:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGif", org.telegram.messenger.R.string.NotificationMessageGroupGif, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:798:0x14c6, code lost:
        if (r30 != false) goto L805;
     */
    /* JADX WARN: Code restructure failed: missing block: B:800:0x14cc, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L805;
     */
    /* JADX WARN: Code restructure failed: missing block: B:802:0x14d6, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L805;
     */
    /* JADX WARN: Code restructure failed: missing block: B:803:0x14d8, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:804:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r2, r5.title, "📎 " + r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:806:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupDocument", org.telegram.messenger.R.string.NotificationMessageGroupDocument, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:807:0x1516, code lost:
        r0 = r29.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:808:0x151c, code lost:
        if (r0 == null) goto L810;
     */
    /* JADX WARN: Code restructure failed: missing block: B:809:0x151e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupStickerEmoji", org.telegram.messenger.R.string.NotificationMessageGroupStickerEmoji, r2, r5.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:810:0x1534, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupSticker", org.telegram.messenger.R.string.NotificationMessageGroupSticker, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:811:0x1547, code lost:
        if (r30 != false) goto L816;
     */
    /* JADX WARN: Code restructure failed: missing block: B:813:0x154f, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageText) != false) goto L816;
     */
    /* JADX WARN: Code restructure failed: missing block: B:815:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r2, r5.title, r29.messageText);
     */
    /* JADX WARN: Code restructure failed: missing block: B:817:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupNoText", org.telegram.messenger.R.string.NotificationMessageGroupNoText, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:819:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMap", org.telegram.messenger.R.string.NotificationMessageGroupMap, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:822:0x0616, code lost:
        if (r12.getBoolean("EnablePreviewChannel", r15) != false) goto L275;
     */
    /* JADX WARN: Removed duplicated region for block: B:272:0x0601  */
    /* JADX WARN: Removed duplicated region for block: B:826:0x1595  */
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
            PendingIntent service = PendingIntent.getService(ApplicationLoader.applicationContext, 0, intent, 0);
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
            if (i == 0) {
                return;
            }
            try {
                this.soundPool.play(i, 1.0f, 1.0f, 1, 0, 1.0f);
            } catch (Exception e) {
                FileLog.e(e);
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

    /* JADX WARN: Removed duplicated region for block: B:24:0x00ef A[Catch: Exception -> 0x014d, TryCatch #0 {Exception -> 0x014d, blocks: (B:10:0x0020, B:13:0x0060, B:14:0x0068, B:17:0x0078, B:19:0x00a1, B:21:0x00b1, B:22:0x00bb, B:24:0x00ef, B:25:0x00f7, B:27:0x0100, B:28:0x011f, B:31:0x0136, B:36:0x0107, B:38:0x010d, B:39:0x0112, B:40:0x0110, B:41:0x0117, B:42:0x00f3, B:44:0x0074, B:45:0x0064), top: B:9:0x0020 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0100 A[Catch: Exception -> 0x014d, TryCatch #0 {Exception -> 0x014d, blocks: (B:10:0x0020, B:13:0x0060, B:14:0x0068, B:17:0x0078, B:19:0x00a1, B:21:0x00b1, B:22:0x00bb, B:24:0x00ef, B:25:0x00f7, B:27:0x0100, B:28:0x011f, B:31:0x0136, B:36:0x0107, B:38:0x010d, B:39:0x0112, B:40:0x0110, B:41:0x0117, B:42:0x00f3, B:44:0x0074, B:45:0x0064), top: B:9:0x0020 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0135  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00f3 A[Catch: Exception -> 0x014d, TryCatch #0 {Exception -> 0x014d, blocks: (B:10:0x0020, B:13:0x0060, B:14:0x0068, B:17:0x0078, B:19:0x00a1, B:21:0x00b1, B:22:0x00bb, B:24:0x00ef, B:25:0x00f7, B:27:0x0100, B:28:0x011f, B:31:0x0136, B:36:0x0107, B:38:0x010d, B:39:0x0112, B:40:0x0110, B:41:0x0117, B:42:0x00f3, B:44:0x0074, B:45:0x0064), top: B:9:0x0020 }] */
    @SuppressLint({"RestrictedApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String createNotificationShortcut(NotificationCompat.Builder builder, long j, String str, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, Person person) {
        Bitmap bitmap;
        IconCompat createWithResource;
        if (!unsupportedNotificationShortcut()) {
            if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup) {
                return null;
            }
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
                        NotificationCompat.BubbleMetadata.Builder builder2 = new NotificationCompat.BubbleMetadata.Builder(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 134217728), createWithResource);
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
                NotificationCompat.BubbleMetadata.Builder builder22 = new NotificationCompat.BubbleMetadata.Builder(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22, 134217728), createWithResource);
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
        if (!this.channelGroupsCreated) {
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
    }

    /* JADX WARN: Removed duplicated region for block: B:114:0x0465  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x0405 A[LOOP:1: B:148:0x0402->B:150:0x0405, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0417  */
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
        String str13 = "groups";
        String str14 = "private";
        String str15 = "channels";
        if (z3) {
            str2 = "other" + this.currentAccount;
            str3 = null;
        } else if (i4 == 2) {
            str2 = str15 + this.currentAccount;
            str3 = "overwrite_channel";
        } else if (i4 == 0) {
            str2 = str13 + this.currentAccount;
            str3 = "overwrite_group";
        } else {
            str2 = str14 + this.currentAccount;
            str3 = "overwrite_private";
        }
        boolean z10 = !z && DialogObject.isEncryptedDialog(j);
        boolean z11 = !z2 && str3 != null && notificationsSettings.getBoolean(str3, false);
        String MD52 = Utilities.MD5(uri == null ? "NoSound" : uri.toString());
        if (MD52 != null && MD52.length() > 5) {
            MD52 = MD52.substring(0, 5);
        }
        if (z3) {
            formatString = LocaleController.getString("NotificationsSilent", R.string.NotificationsSilent);
            str14 = "silent";
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
                if (z2) {
                    str15 = "channels_ia";
                }
                str14 = str15;
            } else if (i4 == 0) {
                if (z2) {
                    str13 = "groups_ia";
                }
                str14 = str13;
            } else if (z2) {
                str14 = "private_ia";
            }
            formatString = string;
        } else {
            formatString = z2 ? LocaleController.formatString("NotificationsChatInApp", R.string.NotificationsChatInApp, str) : str;
            StringBuilder sb = new StringBuilder();
            sb.append(z2 ? "org.telegram.keyia" : "org.telegram.key");
            sb.append(j);
            sb.append("_");
            sb.append(i);
            str14 = sb.toString();
        }
        String str16 = str14 + "_" + MD52;
        String string2 = notificationsSettings.getString(str16, null);
        String string3 = notificationsSettings.getString(str16 + "_s", null);
        StringBuilder sb2 = new StringBuilder();
        String str17 = formatString;
        String str18 = str2;
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
                str5 = str16;
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
                str5 = str16;
                z4 = z11;
            } else {
                int importance = notificationChannel.getImportance();
                Uri sound = notificationChannel.getSound();
                long[] vibrationPattern = notificationChannel.getVibrationPattern();
                z4 = z11;
                boolean shouldVibrate = notificationChannel.shouldVibrate();
                if (shouldVibrate || vibrationPattern != null) {
                    str5 = str16;
                    z8 = shouldVibrate;
                    jArr2 = vibrationPattern;
                } else {
                    str5 = str16;
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
                if (!MD53.equals(string3)) {
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
                            if (!z2) {
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
                            } else {
                                str12 = MD53;
                                editor2 = null;
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
                } else {
                    j2 = j;
                    i6 = i3;
                    str12 = MD53;
                    jArr2 = jArr;
                    lightColor = i2;
                    z9 = false;
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
                        NotificationChannel notificationChannel2 = new NotificationChannel(str11, z10 ? LocaleController.getString("SecretChatName", R.string.SecretChatName) : str17, i6);
                        notificationChannel2.setGroup(str18);
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
                            notificationChannel2.setSound(null, null);
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
        str5 = str16;
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

    /* JADX WARN: Code restructure failed: missing block: B:199:0x0707, code lost:
        if (r2.local_id != 0) goto L200;
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x0889, code lost:
        if (android.os.Build.VERSION.SDK_INT < 26) goto L325;
     */
    /* JADX WARN: Code restructure failed: missing block: B:455:0x014b, code lost:
        if (r10 == 0) goto L458;
     */
    /* JADX WARN: Code restructure failed: missing block: B:456:0x014d, code lost:
        r3 = org.telegram.messenger.LocaleController.getString("NotificationHiddenChatName", org.telegram.messenger.R.string.NotificationHiddenChatName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:458:0x0156, code lost:
        r3 = org.telegram.messenger.LocaleController.getString("NotificationHiddenName", org.telegram.messenger.R.string.NotificationHiddenName);
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:104:0x043f A[Catch: Exception -> 0x0b3d, TryCatch #0 {Exception -> 0x0b3d, blocks: (B:10:0x0022, B:12:0x0046, B:15:0x004a, B:17:0x005a, B:18:0x0060, B:21:0x0074, B:25:0x0082, B:27:0x008e, B:28:0x0094, B:30:0x00a4, B:32:0x00b2, B:34:0x00b8, B:36:0x00d3, B:38:0x00df, B:42:0x00ff, B:44:0x0105, B:45:0x0115, B:47:0x011d, B:51:0x0125, B:53:0x012b, B:58:0x016a, B:61:0x0175, B:63:0x017d, B:64:0x01aa, B:66:0x01b5, B:70:0x022c, B:73:0x0242, B:78:0x025f, B:79:0x02a1, B:82:0x0373, B:93:0x038e, B:95:0x03aa, B:97:0x03df, B:99:0x03e9, B:100:0x03fc, B:102:0x0410, B:104:0x043f, B:107:0x0462, B:110:0x0471, B:112:0x0499, B:114:0x04cf, B:115:0x04f7, B:117:0x0505, B:122:0x0529, B:124:0x0533, B:125:0x0548, B:130:0x060b, B:132:0x0611, B:139:0x0628, B:141:0x062e, B:148:0x0640, B:151:0x064a, B:154:0x0653, B:169:0x067b, B:172:0x0684, B:174:0x06b7, B:178:0x06c6, B:181:0x06d0, B:182:0x06d7, B:184:0x06dd, B:187:0x06e2, B:189:0x06eb, B:192:0x06f3, B:194:0x06f9, B:196:0x06fd, B:198:0x0705, B:201:0x074e, B:204:0x07ac, B:206:0x07b0, B:208:0x07b6, B:209:0x07cc, B:211:0x07f1, B:213:0x07fe, B:220:0x084e, B:226:0x0896, B:229:0x08d3, B:231:0x08db, B:233:0x08df, B:235:0x08e7, B:239:0x08f0, B:241:0x098a, B:244:0x0999, B:247:0x0a01, B:249:0x0a07, B:251:0x0a0b, B:253:0x0a16, B:255:0x0a1c, B:257:0x0a27, B:259:0x0a36, B:261:0x0a42, B:263:0x0a62, B:264:0x0a6c, B:266:0x0a9d, B:271:0x0ab1, B:275:0x0acc, B:277:0x0ad2, B:279:0x0ada, B:281:0x0ae0, B:283:0x0af2, B:284:0x0b09, B:285:0x0b1f, B:290:0x09ad, B:297:0x09ce, B:300:0x09e2, B:302:0x091a, B:303:0x091f, B:304:0x0922, B:306:0x0928, B:309:0x0932, B:311:0x093a, B:316:0x0978, B:317:0x0980, B:319:0x089e, B:321:0x08a6, B:322:0x08ce, B:324:0x09ee, B:333:0x0863, B:337:0x0870, B:341:0x0879, B:344:0x0883, B:363:0x0712, B:365:0x0716, B:367:0x071a, B:369:0x0722, B:374:0x0730, B:376:0x073b, B:378:0x0744, B:387:0x0540, B:388:0x056a, B:390:0x0576, B:391:0x058b, B:392:0x0583, B:395:0x05b1, B:397:0x05bb, B:398:0x05d0, B:399:0x05c8, B:402:0x04e1, B:406:0x041c, B:408:0x0420, B:415:0x0273, B:417:0x0278, B:418:0x028c, B:420:0x02b9, B:422:0x02dd, B:424:0x02f5, B:429:0x02ff, B:430:0x0305, B:434:0x0312, B:435:0x0326, B:437:0x032b, B:438:0x033f, B:439:0x0352, B:441:0x035a, B:445:0x0363, B:446:0x01c4, B:449:0x01d1, B:450:0x01f0, B:451:0x018a, B:456:0x014d, B:458:0x0156, B:459:0x015f, B:464:0x010e, B:465:0x0111, B:471:0x00bb, B:473:0x00c1, B:478:0x0072, B:161:0x066c, B:314:0x0944), top: B:9:0x0022, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:109:0x046c  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0499 A[Catch: Exception -> 0x0b3d, TryCatch #0 {Exception -> 0x0b3d, blocks: (B:10:0x0022, B:12:0x0046, B:15:0x004a, B:17:0x005a, B:18:0x0060, B:21:0x0074, B:25:0x0082, B:27:0x008e, B:28:0x0094, B:30:0x00a4, B:32:0x00b2, B:34:0x00b8, B:36:0x00d3, B:38:0x00df, B:42:0x00ff, B:44:0x0105, B:45:0x0115, B:47:0x011d, B:51:0x0125, B:53:0x012b, B:58:0x016a, B:61:0x0175, B:63:0x017d, B:64:0x01aa, B:66:0x01b5, B:70:0x022c, B:73:0x0242, B:78:0x025f, B:79:0x02a1, B:82:0x0373, B:93:0x038e, B:95:0x03aa, B:97:0x03df, B:99:0x03e9, B:100:0x03fc, B:102:0x0410, B:104:0x043f, B:107:0x0462, B:110:0x0471, B:112:0x0499, B:114:0x04cf, B:115:0x04f7, B:117:0x0505, B:122:0x0529, B:124:0x0533, B:125:0x0548, B:130:0x060b, B:132:0x0611, B:139:0x0628, B:141:0x062e, B:148:0x0640, B:151:0x064a, B:154:0x0653, B:169:0x067b, B:172:0x0684, B:174:0x06b7, B:178:0x06c6, B:181:0x06d0, B:182:0x06d7, B:184:0x06dd, B:187:0x06e2, B:189:0x06eb, B:192:0x06f3, B:194:0x06f9, B:196:0x06fd, B:198:0x0705, B:201:0x074e, B:204:0x07ac, B:206:0x07b0, B:208:0x07b6, B:209:0x07cc, B:211:0x07f1, B:213:0x07fe, B:220:0x084e, B:226:0x0896, B:229:0x08d3, B:231:0x08db, B:233:0x08df, B:235:0x08e7, B:239:0x08f0, B:241:0x098a, B:244:0x0999, B:247:0x0a01, B:249:0x0a07, B:251:0x0a0b, B:253:0x0a16, B:255:0x0a1c, B:257:0x0a27, B:259:0x0a36, B:261:0x0a42, B:263:0x0a62, B:264:0x0a6c, B:266:0x0a9d, B:271:0x0ab1, B:275:0x0acc, B:277:0x0ad2, B:279:0x0ada, B:281:0x0ae0, B:283:0x0af2, B:284:0x0b09, B:285:0x0b1f, B:290:0x09ad, B:297:0x09ce, B:300:0x09e2, B:302:0x091a, B:303:0x091f, B:304:0x0922, B:306:0x0928, B:309:0x0932, B:311:0x093a, B:316:0x0978, B:317:0x0980, B:319:0x089e, B:321:0x08a6, B:322:0x08ce, B:324:0x09ee, B:333:0x0863, B:337:0x0870, B:341:0x0879, B:344:0x0883, B:363:0x0712, B:365:0x0716, B:367:0x071a, B:369:0x0722, B:374:0x0730, B:376:0x073b, B:378:0x0744, B:387:0x0540, B:388:0x056a, B:390:0x0576, B:391:0x058b, B:392:0x0583, B:395:0x05b1, B:397:0x05bb, B:398:0x05d0, B:399:0x05c8, B:402:0x04e1, B:406:0x041c, B:408:0x0420, B:415:0x0273, B:417:0x0278, B:418:0x028c, B:420:0x02b9, B:422:0x02dd, B:424:0x02f5, B:429:0x02ff, B:430:0x0305, B:434:0x0312, B:435:0x0326, B:437:0x032b, B:438:0x033f, B:439:0x0352, B:441:0x035a, B:445:0x0363, B:446:0x01c4, B:449:0x01d1, B:450:0x01f0, B:451:0x018a, B:456:0x014d, B:458:0x0156, B:459:0x015f, B:464:0x010e, B:465:0x0111, B:471:0x00bb, B:473:0x00c1, B:478:0x0072, B:161:0x066c, B:314:0x0944), top: B:9:0x0022, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0527  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0605  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0611 A[Catch: Exception -> 0x0b3d, TryCatch #0 {Exception -> 0x0b3d, blocks: (B:10:0x0022, B:12:0x0046, B:15:0x004a, B:17:0x005a, B:18:0x0060, B:21:0x0074, B:25:0x0082, B:27:0x008e, B:28:0x0094, B:30:0x00a4, B:32:0x00b2, B:34:0x00b8, B:36:0x00d3, B:38:0x00df, B:42:0x00ff, B:44:0x0105, B:45:0x0115, B:47:0x011d, B:51:0x0125, B:53:0x012b, B:58:0x016a, B:61:0x0175, B:63:0x017d, B:64:0x01aa, B:66:0x01b5, B:70:0x022c, B:73:0x0242, B:78:0x025f, B:79:0x02a1, B:82:0x0373, B:93:0x038e, B:95:0x03aa, B:97:0x03df, B:99:0x03e9, B:100:0x03fc, B:102:0x0410, B:104:0x043f, B:107:0x0462, B:110:0x0471, B:112:0x0499, B:114:0x04cf, B:115:0x04f7, B:117:0x0505, B:122:0x0529, B:124:0x0533, B:125:0x0548, B:130:0x060b, B:132:0x0611, B:139:0x0628, B:141:0x062e, B:148:0x0640, B:151:0x064a, B:154:0x0653, B:169:0x067b, B:172:0x0684, B:174:0x06b7, B:178:0x06c6, B:181:0x06d0, B:182:0x06d7, B:184:0x06dd, B:187:0x06e2, B:189:0x06eb, B:192:0x06f3, B:194:0x06f9, B:196:0x06fd, B:198:0x0705, B:201:0x074e, B:204:0x07ac, B:206:0x07b0, B:208:0x07b6, B:209:0x07cc, B:211:0x07f1, B:213:0x07fe, B:220:0x084e, B:226:0x0896, B:229:0x08d3, B:231:0x08db, B:233:0x08df, B:235:0x08e7, B:239:0x08f0, B:241:0x098a, B:244:0x0999, B:247:0x0a01, B:249:0x0a07, B:251:0x0a0b, B:253:0x0a16, B:255:0x0a1c, B:257:0x0a27, B:259:0x0a36, B:261:0x0a42, B:263:0x0a62, B:264:0x0a6c, B:266:0x0a9d, B:271:0x0ab1, B:275:0x0acc, B:277:0x0ad2, B:279:0x0ada, B:281:0x0ae0, B:283:0x0af2, B:284:0x0b09, B:285:0x0b1f, B:290:0x09ad, B:297:0x09ce, B:300:0x09e2, B:302:0x091a, B:303:0x091f, B:304:0x0922, B:306:0x0928, B:309:0x0932, B:311:0x093a, B:316:0x0978, B:317:0x0980, B:319:0x089e, B:321:0x08a6, B:322:0x08ce, B:324:0x09ee, B:333:0x0863, B:337:0x0870, B:341:0x0879, B:344:0x0883, B:363:0x0712, B:365:0x0716, B:367:0x071a, B:369:0x0722, B:374:0x0730, B:376:0x073b, B:378:0x0744, B:387:0x0540, B:388:0x056a, B:390:0x0576, B:391:0x058b, B:392:0x0583, B:395:0x05b1, B:397:0x05bb, B:398:0x05d0, B:399:0x05c8, B:402:0x04e1, B:406:0x041c, B:408:0x0420, B:415:0x0273, B:417:0x0278, B:418:0x028c, B:420:0x02b9, B:422:0x02dd, B:424:0x02f5, B:429:0x02ff, B:430:0x0305, B:434:0x0312, B:435:0x0326, B:437:0x032b, B:438:0x033f, B:439:0x0352, B:441:0x035a, B:445:0x0363, B:446:0x01c4, B:449:0x01d1, B:450:0x01f0, B:451:0x018a, B:456:0x014d, B:458:0x0156, B:459:0x015f, B:464:0x010e, B:465:0x0111, B:471:0x00bb, B:473:0x00c1, B:478:0x0072, B:161:0x066c, B:314:0x0944), top: B:9:0x0022, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0621 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0628 A[Catch: Exception -> 0x0b3d, TryCatch #0 {Exception -> 0x0b3d, blocks: (B:10:0x0022, B:12:0x0046, B:15:0x004a, B:17:0x005a, B:18:0x0060, B:21:0x0074, B:25:0x0082, B:27:0x008e, B:28:0x0094, B:30:0x00a4, B:32:0x00b2, B:34:0x00b8, B:36:0x00d3, B:38:0x00df, B:42:0x00ff, B:44:0x0105, B:45:0x0115, B:47:0x011d, B:51:0x0125, B:53:0x012b, B:58:0x016a, B:61:0x0175, B:63:0x017d, B:64:0x01aa, B:66:0x01b5, B:70:0x022c, B:73:0x0242, B:78:0x025f, B:79:0x02a1, B:82:0x0373, B:93:0x038e, B:95:0x03aa, B:97:0x03df, B:99:0x03e9, B:100:0x03fc, B:102:0x0410, B:104:0x043f, B:107:0x0462, B:110:0x0471, B:112:0x0499, B:114:0x04cf, B:115:0x04f7, B:117:0x0505, B:122:0x0529, B:124:0x0533, B:125:0x0548, B:130:0x060b, B:132:0x0611, B:139:0x0628, B:141:0x062e, B:148:0x0640, B:151:0x064a, B:154:0x0653, B:169:0x067b, B:172:0x0684, B:174:0x06b7, B:178:0x06c6, B:181:0x06d0, B:182:0x06d7, B:184:0x06dd, B:187:0x06e2, B:189:0x06eb, B:192:0x06f3, B:194:0x06f9, B:196:0x06fd, B:198:0x0705, B:201:0x074e, B:204:0x07ac, B:206:0x07b0, B:208:0x07b6, B:209:0x07cc, B:211:0x07f1, B:213:0x07fe, B:220:0x084e, B:226:0x0896, B:229:0x08d3, B:231:0x08db, B:233:0x08df, B:235:0x08e7, B:239:0x08f0, B:241:0x098a, B:244:0x0999, B:247:0x0a01, B:249:0x0a07, B:251:0x0a0b, B:253:0x0a16, B:255:0x0a1c, B:257:0x0a27, B:259:0x0a36, B:261:0x0a42, B:263:0x0a62, B:264:0x0a6c, B:266:0x0a9d, B:271:0x0ab1, B:275:0x0acc, B:277:0x0ad2, B:279:0x0ada, B:281:0x0ae0, B:283:0x0af2, B:284:0x0b09, B:285:0x0b1f, B:290:0x09ad, B:297:0x09ce, B:300:0x09e2, B:302:0x091a, B:303:0x091f, B:304:0x0922, B:306:0x0928, B:309:0x0932, B:311:0x093a, B:316:0x0978, B:317:0x0980, B:319:0x089e, B:321:0x08a6, B:322:0x08ce, B:324:0x09ee, B:333:0x0863, B:337:0x0870, B:341:0x0879, B:344:0x0883, B:363:0x0712, B:365:0x0716, B:367:0x071a, B:369:0x0722, B:374:0x0730, B:376:0x073b, B:378:0x0744, B:387:0x0540, B:388:0x056a, B:390:0x0576, B:391:0x058b, B:392:0x0583, B:395:0x05b1, B:397:0x05bb, B:398:0x05d0, B:399:0x05c8, B:402:0x04e1, B:406:0x041c, B:408:0x0420, B:415:0x0273, B:417:0x0278, B:418:0x028c, B:420:0x02b9, B:422:0x02dd, B:424:0x02f5, B:429:0x02ff, B:430:0x0305, B:434:0x0312, B:435:0x0326, B:437:0x032b, B:438:0x033f, B:439:0x0352, B:441:0x035a, B:445:0x0363, B:446:0x01c4, B:449:0x01d1, B:450:0x01f0, B:451:0x018a, B:456:0x014d, B:458:0x0156, B:459:0x015f, B:464:0x010e, B:465:0x0111, B:471:0x00bb, B:473:0x00c1, B:478:0x0072, B:161:0x066c, B:314:0x0944), top: B:9:0x0022, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0635  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0640 A[Catch: Exception -> 0x0b3d, TryCatch #0 {Exception -> 0x0b3d, blocks: (B:10:0x0022, B:12:0x0046, B:15:0x004a, B:17:0x005a, B:18:0x0060, B:21:0x0074, B:25:0x0082, B:27:0x008e, B:28:0x0094, B:30:0x00a4, B:32:0x00b2, B:34:0x00b8, B:36:0x00d3, B:38:0x00df, B:42:0x00ff, B:44:0x0105, B:45:0x0115, B:47:0x011d, B:51:0x0125, B:53:0x012b, B:58:0x016a, B:61:0x0175, B:63:0x017d, B:64:0x01aa, B:66:0x01b5, B:70:0x022c, B:73:0x0242, B:78:0x025f, B:79:0x02a1, B:82:0x0373, B:93:0x038e, B:95:0x03aa, B:97:0x03df, B:99:0x03e9, B:100:0x03fc, B:102:0x0410, B:104:0x043f, B:107:0x0462, B:110:0x0471, B:112:0x0499, B:114:0x04cf, B:115:0x04f7, B:117:0x0505, B:122:0x0529, B:124:0x0533, B:125:0x0548, B:130:0x060b, B:132:0x0611, B:139:0x0628, B:141:0x062e, B:148:0x0640, B:151:0x064a, B:154:0x0653, B:169:0x067b, B:172:0x0684, B:174:0x06b7, B:178:0x06c6, B:181:0x06d0, B:182:0x06d7, B:184:0x06dd, B:187:0x06e2, B:189:0x06eb, B:192:0x06f3, B:194:0x06f9, B:196:0x06fd, B:198:0x0705, B:201:0x074e, B:204:0x07ac, B:206:0x07b0, B:208:0x07b6, B:209:0x07cc, B:211:0x07f1, B:213:0x07fe, B:220:0x084e, B:226:0x0896, B:229:0x08d3, B:231:0x08db, B:233:0x08df, B:235:0x08e7, B:239:0x08f0, B:241:0x098a, B:244:0x0999, B:247:0x0a01, B:249:0x0a07, B:251:0x0a0b, B:253:0x0a16, B:255:0x0a1c, B:257:0x0a27, B:259:0x0a36, B:261:0x0a42, B:263:0x0a62, B:264:0x0a6c, B:266:0x0a9d, B:271:0x0ab1, B:275:0x0acc, B:277:0x0ad2, B:279:0x0ada, B:281:0x0ae0, B:283:0x0af2, B:284:0x0b09, B:285:0x0b1f, B:290:0x09ad, B:297:0x09ce, B:300:0x09e2, B:302:0x091a, B:303:0x091f, B:304:0x0922, B:306:0x0928, B:309:0x0932, B:311:0x093a, B:316:0x0978, B:317:0x0980, B:319:0x089e, B:321:0x08a6, B:322:0x08ce, B:324:0x09ee, B:333:0x0863, B:337:0x0870, B:341:0x0879, B:344:0x0883, B:363:0x0712, B:365:0x0716, B:367:0x071a, B:369:0x0722, B:374:0x0730, B:376:0x073b, B:378:0x0744, B:387:0x0540, B:388:0x056a, B:390:0x0576, B:391:0x058b, B:392:0x0583, B:395:0x05b1, B:397:0x05bb, B:398:0x05d0, B:399:0x05c8, B:402:0x04e1, B:406:0x041c, B:408:0x0420, B:415:0x0273, B:417:0x0278, B:418:0x028c, B:420:0x02b9, B:422:0x02dd, B:424:0x02f5, B:429:0x02ff, B:430:0x0305, B:434:0x0312, B:435:0x0326, B:437:0x032b, B:438:0x033f, B:439:0x0352, B:441:0x035a, B:445:0x0363, B:446:0x01c4, B:449:0x01d1, B:450:0x01f0, B:451:0x018a, B:456:0x014d, B:458:0x0156, B:459:0x015f, B:464:0x010e, B:465:0x0111, B:471:0x00bb, B:473:0x00c1, B:478:0x0072, B:161:0x066c, B:314:0x0944), top: B:9:0x0022, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:158:0x0669  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x0680  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x06b7 A[Catch: Exception -> 0x0b3d, TryCatch #0 {Exception -> 0x0b3d, blocks: (B:10:0x0022, B:12:0x0046, B:15:0x004a, B:17:0x005a, B:18:0x0060, B:21:0x0074, B:25:0x0082, B:27:0x008e, B:28:0x0094, B:30:0x00a4, B:32:0x00b2, B:34:0x00b8, B:36:0x00d3, B:38:0x00df, B:42:0x00ff, B:44:0x0105, B:45:0x0115, B:47:0x011d, B:51:0x0125, B:53:0x012b, B:58:0x016a, B:61:0x0175, B:63:0x017d, B:64:0x01aa, B:66:0x01b5, B:70:0x022c, B:73:0x0242, B:78:0x025f, B:79:0x02a1, B:82:0x0373, B:93:0x038e, B:95:0x03aa, B:97:0x03df, B:99:0x03e9, B:100:0x03fc, B:102:0x0410, B:104:0x043f, B:107:0x0462, B:110:0x0471, B:112:0x0499, B:114:0x04cf, B:115:0x04f7, B:117:0x0505, B:122:0x0529, B:124:0x0533, B:125:0x0548, B:130:0x060b, B:132:0x0611, B:139:0x0628, B:141:0x062e, B:148:0x0640, B:151:0x064a, B:154:0x0653, B:169:0x067b, B:172:0x0684, B:174:0x06b7, B:178:0x06c6, B:181:0x06d0, B:182:0x06d7, B:184:0x06dd, B:187:0x06e2, B:189:0x06eb, B:192:0x06f3, B:194:0x06f9, B:196:0x06fd, B:198:0x0705, B:201:0x074e, B:204:0x07ac, B:206:0x07b0, B:208:0x07b6, B:209:0x07cc, B:211:0x07f1, B:213:0x07fe, B:220:0x084e, B:226:0x0896, B:229:0x08d3, B:231:0x08db, B:233:0x08df, B:235:0x08e7, B:239:0x08f0, B:241:0x098a, B:244:0x0999, B:247:0x0a01, B:249:0x0a07, B:251:0x0a0b, B:253:0x0a16, B:255:0x0a1c, B:257:0x0a27, B:259:0x0a36, B:261:0x0a42, B:263:0x0a62, B:264:0x0a6c, B:266:0x0a9d, B:271:0x0ab1, B:275:0x0acc, B:277:0x0ad2, B:279:0x0ada, B:281:0x0ae0, B:283:0x0af2, B:284:0x0b09, B:285:0x0b1f, B:290:0x09ad, B:297:0x09ce, B:300:0x09e2, B:302:0x091a, B:303:0x091f, B:304:0x0922, B:306:0x0928, B:309:0x0932, B:311:0x093a, B:316:0x0978, B:317:0x0980, B:319:0x089e, B:321:0x08a6, B:322:0x08ce, B:324:0x09ee, B:333:0x0863, B:337:0x0870, B:341:0x0879, B:344:0x0883, B:363:0x0712, B:365:0x0716, B:367:0x071a, B:369:0x0722, B:374:0x0730, B:376:0x073b, B:378:0x0744, B:387:0x0540, B:388:0x056a, B:390:0x0576, B:391:0x058b, B:392:0x0583, B:395:0x05b1, B:397:0x05bb, B:398:0x05d0, B:399:0x05c8, B:402:0x04e1, B:406:0x041c, B:408:0x0420, B:415:0x0273, B:417:0x0278, B:418:0x028c, B:420:0x02b9, B:422:0x02dd, B:424:0x02f5, B:429:0x02ff, B:430:0x0305, B:434:0x0312, B:435:0x0326, B:437:0x032b, B:438:0x033f, B:439:0x0352, B:441:0x035a, B:445:0x0363, B:446:0x01c4, B:449:0x01d1, B:450:0x01f0, B:451:0x018a, B:456:0x014d, B:458:0x0156, B:459:0x015f, B:464:0x010e, B:465:0x0111, B:471:0x00bb, B:473:0x00c1, B:478:0x0072, B:161:0x066c, B:314:0x0944), top: B:9:0x0022, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:203:0x07aa A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:211:0x07f1 A[Catch: Exception -> 0x0b3d, TryCatch #0 {Exception -> 0x0b3d, blocks: (B:10:0x0022, B:12:0x0046, B:15:0x004a, B:17:0x005a, B:18:0x0060, B:21:0x0074, B:25:0x0082, B:27:0x008e, B:28:0x0094, B:30:0x00a4, B:32:0x00b2, B:34:0x00b8, B:36:0x00d3, B:38:0x00df, B:42:0x00ff, B:44:0x0105, B:45:0x0115, B:47:0x011d, B:51:0x0125, B:53:0x012b, B:58:0x016a, B:61:0x0175, B:63:0x017d, B:64:0x01aa, B:66:0x01b5, B:70:0x022c, B:73:0x0242, B:78:0x025f, B:79:0x02a1, B:82:0x0373, B:93:0x038e, B:95:0x03aa, B:97:0x03df, B:99:0x03e9, B:100:0x03fc, B:102:0x0410, B:104:0x043f, B:107:0x0462, B:110:0x0471, B:112:0x0499, B:114:0x04cf, B:115:0x04f7, B:117:0x0505, B:122:0x0529, B:124:0x0533, B:125:0x0548, B:130:0x060b, B:132:0x0611, B:139:0x0628, B:141:0x062e, B:148:0x0640, B:151:0x064a, B:154:0x0653, B:169:0x067b, B:172:0x0684, B:174:0x06b7, B:178:0x06c6, B:181:0x06d0, B:182:0x06d7, B:184:0x06dd, B:187:0x06e2, B:189:0x06eb, B:192:0x06f3, B:194:0x06f9, B:196:0x06fd, B:198:0x0705, B:201:0x074e, B:204:0x07ac, B:206:0x07b0, B:208:0x07b6, B:209:0x07cc, B:211:0x07f1, B:213:0x07fe, B:220:0x084e, B:226:0x0896, B:229:0x08d3, B:231:0x08db, B:233:0x08df, B:235:0x08e7, B:239:0x08f0, B:241:0x098a, B:244:0x0999, B:247:0x0a01, B:249:0x0a07, B:251:0x0a0b, B:253:0x0a16, B:255:0x0a1c, B:257:0x0a27, B:259:0x0a36, B:261:0x0a42, B:263:0x0a62, B:264:0x0a6c, B:266:0x0a9d, B:271:0x0ab1, B:275:0x0acc, B:277:0x0ad2, B:279:0x0ada, B:281:0x0ae0, B:283:0x0af2, B:284:0x0b09, B:285:0x0b1f, B:290:0x09ad, B:297:0x09ce, B:300:0x09e2, B:302:0x091a, B:303:0x091f, B:304:0x0922, B:306:0x0928, B:309:0x0932, B:311:0x093a, B:316:0x0978, B:317:0x0980, B:319:0x089e, B:321:0x08a6, B:322:0x08ce, B:324:0x09ee, B:333:0x0863, B:337:0x0870, B:341:0x0879, B:344:0x0883, B:363:0x0712, B:365:0x0716, B:367:0x071a, B:369:0x0722, B:374:0x0730, B:376:0x073b, B:378:0x0744, B:387:0x0540, B:388:0x056a, B:390:0x0576, B:391:0x058b, B:392:0x0583, B:395:0x05b1, B:397:0x05bb, B:398:0x05d0, B:399:0x05c8, B:402:0x04e1, B:406:0x041c, B:408:0x0420, B:415:0x0273, B:417:0x0278, B:418:0x028c, B:420:0x02b9, B:422:0x02dd, B:424:0x02f5, B:429:0x02ff, B:430:0x0305, B:434:0x0312, B:435:0x0326, B:437:0x032b, B:438:0x033f, B:439:0x0352, B:441:0x035a, B:445:0x0363, B:446:0x01c4, B:449:0x01d1, B:450:0x01f0, B:451:0x018a, B:456:0x014d, B:458:0x0156, B:459:0x015f, B:464:0x010e, B:465:0x0111, B:471:0x00bb, B:473:0x00c1, B:478:0x0072, B:161:0x066c, B:314:0x0944), top: B:9:0x0022, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0847  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x098a A[Catch: Exception -> 0x0b3d, TryCatch #0 {Exception -> 0x0b3d, blocks: (B:10:0x0022, B:12:0x0046, B:15:0x004a, B:17:0x005a, B:18:0x0060, B:21:0x0074, B:25:0x0082, B:27:0x008e, B:28:0x0094, B:30:0x00a4, B:32:0x00b2, B:34:0x00b8, B:36:0x00d3, B:38:0x00df, B:42:0x00ff, B:44:0x0105, B:45:0x0115, B:47:0x011d, B:51:0x0125, B:53:0x012b, B:58:0x016a, B:61:0x0175, B:63:0x017d, B:64:0x01aa, B:66:0x01b5, B:70:0x022c, B:73:0x0242, B:78:0x025f, B:79:0x02a1, B:82:0x0373, B:93:0x038e, B:95:0x03aa, B:97:0x03df, B:99:0x03e9, B:100:0x03fc, B:102:0x0410, B:104:0x043f, B:107:0x0462, B:110:0x0471, B:112:0x0499, B:114:0x04cf, B:115:0x04f7, B:117:0x0505, B:122:0x0529, B:124:0x0533, B:125:0x0548, B:130:0x060b, B:132:0x0611, B:139:0x0628, B:141:0x062e, B:148:0x0640, B:151:0x064a, B:154:0x0653, B:169:0x067b, B:172:0x0684, B:174:0x06b7, B:178:0x06c6, B:181:0x06d0, B:182:0x06d7, B:184:0x06dd, B:187:0x06e2, B:189:0x06eb, B:192:0x06f3, B:194:0x06f9, B:196:0x06fd, B:198:0x0705, B:201:0x074e, B:204:0x07ac, B:206:0x07b0, B:208:0x07b6, B:209:0x07cc, B:211:0x07f1, B:213:0x07fe, B:220:0x084e, B:226:0x0896, B:229:0x08d3, B:231:0x08db, B:233:0x08df, B:235:0x08e7, B:239:0x08f0, B:241:0x098a, B:244:0x0999, B:247:0x0a01, B:249:0x0a07, B:251:0x0a0b, B:253:0x0a16, B:255:0x0a1c, B:257:0x0a27, B:259:0x0a36, B:261:0x0a42, B:263:0x0a62, B:264:0x0a6c, B:266:0x0a9d, B:271:0x0ab1, B:275:0x0acc, B:277:0x0ad2, B:279:0x0ada, B:281:0x0ae0, B:283:0x0af2, B:284:0x0b09, B:285:0x0b1f, B:290:0x09ad, B:297:0x09ce, B:300:0x09e2, B:302:0x091a, B:303:0x091f, B:304:0x0922, B:306:0x0928, B:309:0x0932, B:311:0x093a, B:316:0x0978, B:317:0x0980, B:319:0x089e, B:321:0x08a6, B:322:0x08ce, B:324:0x09ee, B:333:0x0863, B:337:0x0870, B:341:0x0879, B:344:0x0883, B:363:0x0712, B:365:0x0716, B:367:0x071a, B:369:0x0722, B:374:0x0730, B:376:0x073b, B:378:0x0744, B:387:0x0540, B:388:0x056a, B:390:0x0576, B:391:0x058b, B:392:0x0583, B:395:0x05b1, B:397:0x05bb, B:398:0x05d0, B:399:0x05c8, B:402:0x04e1, B:406:0x041c, B:408:0x0420, B:415:0x0273, B:417:0x0278, B:418:0x028c, B:420:0x02b9, B:422:0x02dd, B:424:0x02f5, B:429:0x02ff, B:430:0x0305, B:434:0x0312, B:435:0x0326, B:437:0x032b, B:438:0x033f, B:439:0x0352, B:441:0x035a, B:445:0x0363, B:446:0x01c4, B:449:0x01d1, B:450:0x01f0, B:451:0x018a, B:456:0x014d, B:458:0x0156, B:459:0x015f, B:464:0x010e, B:465:0x0111, B:471:0x00bb, B:473:0x00c1, B:478:0x0072, B:161:0x066c, B:314:0x0944), top: B:9:0x0022, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:244:0x0999 A[Catch: Exception -> 0x0b3d, TryCatch #0 {Exception -> 0x0b3d, blocks: (B:10:0x0022, B:12:0x0046, B:15:0x004a, B:17:0x005a, B:18:0x0060, B:21:0x0074, B:25:0x0082, B:27:0x008e, B:28:0x0094, B:30:0x00a4, B:32:0x00b2, B:34:0x00b8, B:36:0x00d3, B:38:0x00df, B:42:0x00ff, B:44:0x0105, B:45:0x0115, B:47:0x011d, B:51:0x0125, B:53:0x012b, B:58:0x016a, B:61:0x0175, B:63:0x017d, B:64:0x01aa, B:66:0x01b5, B:70:0x022c, B:73:0x0242, B:78:0x025f, B:79:0x02a1, B:82:0x0373, B:93:0x038e, B:95:0x03aa, B:97:0x03df, B:99:0x03e9, B:100:0x03fc, B:102:0x0410, B:104:0x043f, B:107:0x0462, B:110:0x0471, B:112:0x0499, B:114:0x04cf, B:115:0x04f7, B:117:0x0505, B:122:0x0529, B:124:0x0533, B:125:0x0548, B:130:0x060b, B:132:0x0611, B:139:0x0628, B:141:0x062e, B:148:0x0640, B:151:0x064a, B:154:0x0653, B:169:0x067b, B:172:0x0684, B:174:0x06b7, B:178:0x06c6, B:181:0x06d0, B:182:0x06d7, B:184:0x06dd, B:187:0x06e2, B:189:0x06eb, B:192:0x06f3, B:194:0x06f9, B:196:0x06fd, B:198:0x0705, B:201:0x074e, B:204:0x07ac, B:206:0x07b0, B:208:0x07b6, B:209:0x07cc, B:211:0x07f1, B:213:0x07fe, B:220:0x084e, B:226:0x0896, B:229:0x08d3, B:231:0x08db, B:233:0x08df, B:235:0x08e7, B:239:0x08f0, B:241:0x098a, B:244:0x0999, B:247:0x0a01, B:249:0x0a07, B:251:0x0a0b, B:253:0x0a16, B:255:0x0a1c, B:257:0x0a27, B:259:0x0a36, B:261:0x0a42, B:263:0x0a62, B:264:0x0a6c, B:266:0x0a9d, B:271:0x0ab1, B:275:0x0acc, B:277:0x0ad2, B:279:0x0ada, B:281:0x0ae0, B:283:0x0af2, B:284:0x0b09, B:285:0x0b1f, B:290:0x09ad, B:297:0x09ce, B:300:0x09e2, B:302:0x091a, B:303:0x091f, B:304:0x0922, B:306:0x0928, B:309:0x0932, B:311:0x093a, B:316:0x0978, B:317:0x0980, B:319:0x089e, B:321:0x08a6, B:322:0x08ce, B:324:0x09ee, B:333:0x0863, B:337:0x0870, B:341:0x0879, B:344:0x0883, B:363:0x0712, B:365:0x0716, B:367:0x071a, B:369:0x0722, B:374:0x0730, B:376:0x073b, B:378:0x0744, B:387:0x0540, B:388:0x056a, B:390:0x0576, B:391:0x058b, B:392:0x0583, B:395:0x05b1, B:397:0x05bb, B:398:0x05d0, B:399:0x05c8, B:402:0x04e1, B:406:0x041c, B:408:0x0420, B:415:0x0273, B:417:0x0278, B:418:0x028c, B:420:0x02b9, B:422:0x02dd, B:424:0x02f5, B:429:0x02ff, B:430:0x0305, B:434:0x0312, B:435:0x0326, B:437:0x032b, B:438:0x033f, B:439:0x0352, B:441:0x035a, B:445:0x0363, B:446:0x01c4, B:449:0x01d1, B:450:0x01f0, B:451:0x018a, B:456:0x014d, B:458:0x0156, B:459:0x015f, B:464:0x010e, B:465:0x0111, B:471:0x00bb, B:473:0x00c1, B:478:0x0072, B:161:0x066c, B:314:0x0944), top: B:9:0x0022, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:288:0x09aa  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x0994  */
    /* JADX WARN: Removed duplicated region for block: B:358:0x083f  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x0730 A[Catch: Exception -> 0x0b3d, TryCatch #0 {Exception -> 0x0b3d, blocks: (B:10:0x0022, B:12:0x0046, B:15:0x004a, B:17:0x005a, B:18:0x0060, B:21:0x0074, B:25:0x0082, B:27:0x008e, B:28:0x0094, B:30:0x00a4, B:32:0x00b2, B:34:0x00b8, B:36:0x00d3, B:38:0x00df, B:42:0x00ff, B:44:0x0105, B:45:0x0115, B:47:0x011d, B:51:0x0125, B:53:0x012b, B:58:0x016a, B:61:0x0175, B:63:0x017d, B:64:0x01aa, B:66:0x01b5, B:70:0x022c, B:73:0x0242, B:78:0x025f, B:79:0x02a1, B:82:0x0373, B:93:0x038e, B:95:0x03aa, B:97:0x03df, B:99:0x03e9, B:100:0x03fc, B:102:0x0410, B:104:0x043f, B:107:0x0462, B:110:0x0471, B:112:0x0499, B:114:0x04cf, B:115:0x04f7, B:117:0x0505, B:122:0x0529, B:124:0x0533, B:125:0x0548, B:130:0x060b, B:132:0x0611, B:139:0x0628, B:141:0x062e, B:148:0x0640, B:151:0x064a, B:154:0x0653, B:169:0x067b, B:172:0x0684, B:174:0x06b7, B:178:0x06c6, B:181:0x06d0, B:182:0x06d7, B:184:0x06dd, B:187:0x06e2, B:189:0x06eb, B:192:0x06f3, B:194:0x06f9, B:196:0x06fd, B:198:0x0705, B:201:0x074e, B:204:0x07ac, B:206:0x07b0, B:208:0x07b6, B:209:0x07cc, B:211:0x07f1, B:213:0x07fe, B:220:0x084e, B:226:0x0896, B:229:0x08d3, B:231:0x08db, B:233:0x08df, B:235:0x08e7, B:239:0x08f0, B:241:0x098a, B:244:0x0999, B:247:0x0a01, B:249:0x0a07, B:251:0x0a0b, B:253:0x0a16, B:255:0x0a1c, B:257:0x0a27, B:259:0x0a36, B:261:0x0a42, B:263:0x0a62, B:264:0x0a6c, B:266:0x0a9d, B:271:0x0ab1, B:275:0x0acc, B:277:0x0ad2, B:279:0x0ada, B:281:0x0ae0, B:283:0x0af2, B:284:0x0b09, B:285:0x0b1f, B:290:0x09ad, B:297:0x09ce, B:300:0x09e2, B:302:0x091a, B:303:0x091f, B:304:0x0922, B:306:0x0928, B:309:0x0932, B:311:0x093a, B:316:0x0978, B:317:0x0980, B:319:0x089e, B:321:0x08a6, B:322:0x08ce, B:324:0x09ee, B:333:0x0863, B:337:0x0870, B:341:0x0879, B:344:0x0883, B:363:0x0712, B:365:0x0716, B:367:0x071a, B:369:0x0722, B:374:0x0730, B:376:0x073b, B:378:0x0744, B:387:0x0540, B:388:0x056a, B:390:0x0576, B:391:0x058b, B:392:0x0583, B:395:0x05b1, B:397:0x05bb, B:398:0x05d0, B:399:0x05c8, B:402:0x04e1, B:406:0x041c, B:408:0x0420, B:415:0x0273, B:417:0x0278, B:418:0x028c, B:420:0x02b9, B:422:0x02dd, B:424:0x02f5, B:429:0x02ff, B:430:0x0305, B:434:0x0312, B:435:0x0326, B:437:0x032b, B:438:0x033f, B:439:0x0352, B:441:0x035a, B:445:0x0363, B:446:0x01c4, B:449:0x01d1, B:450:0x01f0, B:451:0x018a, B:456:0x014d, B:458:0x0156, B:459:0x015f, B:464:0x010e, B:465:0x0111, B:471:0x00bb, B:473:0x00c1, B:478:0x0072, B:161:0x066c, B:314:0x0944), top: B:9:0x0022, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:386:0x0609  */
    /* JADX WARN: Removed duplicated region for block: B:393:0x05ac  */
    /* JADX WARN: Removed duplicated region for block: B:403:0x0513  */
    /* JADX WARN: Removed duplicated region for block: B:404:0x046f  */
    /* JADX WARN: Removed duplicated region for block: B:405:0x0460  */
    /* JADX WARN: Removed duplicated region for block: B:420:0x02b9 A[Catch: Exception -> 0x0b3d, TryCatch #0 {Exception -> 0x0b3d, blocks: (B:10:0x0022, B:12:0x0046, B:15:0x004a, B:17:0x005a, B:18:0x0060, B:21:0x0074, B:25:0x0082, B:27:0x008e, B:28:0x0094, B:30:0x00a4, B:32:0x00b2, B:34:0x00b8, B:36:0x00d3, B:38:0x00df, B:42:0x00ff, B:44:0x0105, B:45:0x0115, B:47:0x011d, B:51:0x0125, B:53:0x012b, B:58:0x016a, B:61:0x0175, B:63:0x017d, B:64:0x01aa, B:66:0x01b5, B:70:0x022c, B:73:0x0242, B:78:0x025f, B:79:0x02a1, B:82:0x0373, B:93:0x038e, B:95:0x03aa, B:97:0x03df, B:99:0x03e9, B:100:0x03fc, B:102:0x0410, B:104:0x043f, B:107:0x0462, B:110:0x0471, B:112:0x0499, B:114:0x04cf, B:115:0x04f7, B:117:0x0505, B:122:0x0529, B:124:0x0533, B:125:0x0548, B:130:0x060b, B:132:0x0611, B:139:0x0628, B:141:0x062e, B:148:0x0640, B:151:0x064a, B:154:0x0653, B:169:0x067b, B:172:0x0684, B:174:0x06b7, B:178:0x06c6, B:181:0x06d0, B:182:0x06d7, B:184:0x06dd, B:187:0x06e2, B:189:0x06eb, B:192:0x06f3, B:194:0x06f9, B:196:0x06fd, B:198:0x0705, B:201:0x074e, B:204:0x07ac, B:206:0x07b0, B:208:0x07b6, B:209:0x07cc, B:211:0x07f1, B:213:0x07fe, B:220:0x084e, B:226:0x0896, B:229:0x08d3, B:231:0x08db, B:233:0x08df, B:235:0x08e7, B:239:0x08f0, B:241:0x098a, B:244:0x0999, B:247:0x0a01, B:249:0x0a07, B:251:0x0a0b, B:253:0x0a16, B:255:0x0a1c, B:257:0x0a27, B:259:0x0a36, B:261:0x0a42, B:263:0x0a62, B:264:0x0a6c, B:266:0x0a9d, B:271:0x0ab1, B:275:0x0acc, B:277:0x0ad2, B:279:0x0ada, B:281:0x0ae0, B:283:0x0af2, B:284:0x0b09, B:285:0x0b1f, B:290:0x09ad, B:297:0x09ce, B:300:0x09e2, B:302:0x091a, B:303:0x091f, B:304:0x0922, B:306:0x0928, B:309:0x0932, B:311:0x093a, B:316:0x0978, B:317:0x0980, B:319:0x089e, B:321:0x08a6, B:322:0x08ce, B:324:0x09ee, B:333:0x0863, B:337:0x0870, B:341:0x0879, B:344:0x0883, B:363:0x0712, B:365:0x0716, B:367:0x071a, B:369:0x0722, B:374:0x0730, B:376:0x073b, B:378:0x0744, B:387:0x0540, B:388:0x056a, B:390:0x0576, B:391:0x058b, B:392:0x0583, B:395:0x05b1, B:397:0x05bb, B:398:0x05d0, B:399:0x05c8, B:402:0x04e1, B:406:0x041c, B:408:0x0420, B:415:0x0273, B:417:0x0278, B:418:0x028c, B:420:0x02b9, B:422:0x02dd, B:424:0x02f5, B:429:0x02ff, B:430:0x0305, B:434:0x0312, B:435:0x0326, B:437:0x032b, B:438:0x033f, B:439:0x0352, B:441:0x035a, B:445:0x0363, B:446:0x01c4, B:449:0x01d1, B:450:0x01f0, B:451:0x018a, B:456:0x014d, B:458:0x0156, B:459:0x015f, B:464:0x010e, B:465:0x0111, B:471:0x00bb, B:473:0x00c1, B:478:0x0072, B:161:0x066c, B:314:0x0944), top: B:9:0x0022, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:449:0x01d1 A[Catch: Exception -> 0x0b3d, TRY_ENTER, TryCatch #0 {Exception -> 0x0b3d, blocks: (B:10:0x0022, B:12:0x0046, B:15:0x004a, B:17:0x005a, B:18:0x0060, B:21:0x0074, B:25:0x0082, B:27:0x008e, B:28:0x0094, B:30:0x00a4, B:32:0x00b2, B:34:0x00b8, B:36:0x00d3, B:38:0x00df, B:42:0x00ff, B:44:0x0105, B:45:0x0115, B:47:0x011d, B:51:0x0125, B:53:0x012b, B:58:0x016a, B:61:0x0175, B:63:0x017d, B:64:0x01aa, B:66:0x01b5, B:70:0x022c, B:73:0x0242, B:78:0x025f, B:79:0x02a1, B:82:0x0373, B:93:0x038e, B:95:0x03aa, B:97:0x03df, B:99:0x03e9, B:100:0x03fc, B:102:0x0410, B:104:0x043f, B:107:0x0462, B:110:0x0471, B:112:0x0499, B:114:0x04cf, B:115:0x04f7, B:117:0x0505, B:122:0x0529, B:124:0x0533, B:125:0x0548, B:130:0x060b, B:132:0x0611, B:139:0x0628, B:141:0x062e, B:148:0x0640, B:151:0x064a, B:154:0x0653, B:169:0x067b, B:172:0x0684, B:174:0x06b7, B:178:0x06c6, B:181:0x06d0, B:182:0x06d7, B:184:0x06dd, B:187:0x06e2, B:189:0x06eb, B:192:0x06f3, B:194:0x06f9, B:196:0x06fd, B:198:0x0705, B:201:0x074e, B:204:0x07ac, B:206:0x07b0, B:208:0x07b6, B:209:0x07cc, B:211:0x07f1, B:213:0x07fe, B:220:0x084e, B:226:0x0896, B:229:0x08d3, B:231:0x08db, B:233:0x08df, B:235:0x08e7, B:239:0x08f0, B:241:0x098a, B:244:0x0999, B:247:0x0a01, B:249:0x0a07, B:251:0x0a0b, B:253:0x0a16, B:255:0x0a1c, B:257:0x0a27, B:259:0x0a36, B:261:0x0a42, B:263:0x0a62, B:264:0x0a6c, B:266:0x0a9d, B:271:0x0ab1, B:275:0x0acc, B:277:0x0ad2, B:279:0x0ada, B:281:0x0ae0, B:283:0x0af2, B:284:0x0b09, B:285:0x0b1f, B:290:0x09ad, B:297:0x09ce, B:300:0x09e2, B:302:0x091a, B:303:0x091f, B:304:0x0922, B:306:0x0928, B:309:0x0932, B:311:0x093a, B:316:0x0978, B:317:0x0980, B:319:0x089e, B:321:0x08a6, B:322:0x08ce, B:324:0x09ee, B:333:0x0863, B:337:0x0870, B:341:0x0879, B:344:0x0883, B:363:0x0712, B:365:0x0716, B:367:0x071a, B:369:0x0722, B:374:0x0730, B:376:0x073b, B:378:0x0744, B:387:0x0540, B:388:0x056a, B:390:0x0576, B:391:0x058b, B:392:0x0583, B:395:0x05b1, B:397:0x05bb, B:398:0x05d0, B:399:0x05c8, B:402:0x04e1, B:406:0x041c, B:408:0x0420, B:415:0x0273, B:417:0x0278, B:418:0x028c, B:420:0x02b9, B:422:0x02dd, B:424:0x02f5, B:429:0x02ff, B:430:0x0305, B:434:0x0312, B:435:0x0326, B:437:0x032b, B:438:0x033f, B:439:0x0352, B:441:0x035a, B:445:0x0363, B:446:0x01c4, B:449:0x01d1, B:450:0x01f0, B:451:0x018a, B:456:0x014d, B:458:0x0156, B:459:0x015f, B:464:0x010e, B:465:0x0111, B:471:0x00bb, B:473:0x00c1, B:478:0x0072, B:161:0x066c, B:314:0x0944), top: B:9:0x0022, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:450:0x01f0 A[Catch: Exception -> 0x0b3d, TryCatch #0 {Exception -> 0x0b3d, blocks: (B:10:0x0022, B:12:0x0046, B:15:0x004a, B:17:0x005a, B:18:0x0060, B:21:0x0074, B:25:0x0082, B:27:0x008e, B:28:0x0094, B:30:0x00a4, B:32:0x00b2, B:34:0x00b8, B:36:0x00d3, B:38:0x00df, B:42:0x00ff, B:44:0x0105, B:45:0x0115, B:47:0x011d, B:51:0x0125, B:53:0x012b, B:58:0x016a, B:61:0x0175, B:63:0x017d, B:64:0x01aa, B:66:0x01b5, B:70:0x022c, B:73:0x0242, B:78:0x025f, B:79:0x02a1, B:82:0x0373, B:93:0x038e, B:95:0x03aa, B:97:0x03df, B:99:0x03e9, B:100:0x03fc, B:102:0x0410, B:104:0x043f, B:107:0x0462, B:110:0x0471, B:112:0x0499, B:114:0x04cf, B:115:0x04f7, B:117:0x0505, B:122:0x0529, B:124:0x0533, B:125:0x0548, B:130:0x060b, B:132:0x0611, B:139:0x0628, B:141:0x062e, B:148:0x0640, B:151:0x064a, B:154:0x0653, B:169:0x067b, B:172:0x0684, B:174:0x06b7, B:178:0x06c6, B:181:0x06d0, B:182:0x06d7, B:184:0x06dd, B:187:0x06e2, B:189:0x06eb, B:192:0x06f3, B:194:0x06f9, B:196:0x06fd, B:198:0x0705, B:201:0x074e, B:204:0x07ac, B:206:0x07b0, B:208:0x07b6, B:209:0x07cc, B:211:0x07f1, B:213:0x07fe, B:220:0x084e, B:226:0x0896, B:229:0x08d3, B:231:0x08db, B:233:0x08df, B:235:0x08e7, B:239:0x08f0, B:241:0x098a, B:244:0x0999, B:247:0x0a01, B:249:0x0a07, B:251:0x0a0b, B:253:0x0a16, B:255:0x0a1c, B:257:0x0a27, B:259:0x0a36, B:261:0x0a42, B:263:0x0a62, B:264:0x0a6c, B:266:0x0a9d, B:271:0x0ab1, B:275:0x0acc, B:277:0x0ad2, B:279:0x0ada, B:281:0x0ae0, B:283:0x0af2, B:284:0x0b09, B:285:0x0b1f, B:290:0x09ad, B:297:0x09ce, B:300:0x09e2, B:302:0x091a, B:303:0x091f, B:304:0x0922, B:306:0x0928, B:309:0x0932, B:311:0x093a, B:316:0x0978, B:317:0x0980, B:319:0x089e, B:321:0x08a6, B:322:0x08ce, B:324:0x09ee, B:333:0x0863, B:337:0x0870, B:341:0x0879, B:344:0x0883, B:363:0x0712, B:365:0x0716, B:367:0x071a, B:369:0x0722, B:374:0x0730, B:376:0x073b, B:378:0x0744, B:387:0x0540, B:388:0x056a, B:390:0x0576, B:391:0x058b, B:392:0x0583, B:395:0x05b1, B:397:0x05bb, B:398:0x05d0, B:399:0x05c8, B:402:0x04e1, B:406:0x041c, B:408:0x0420, B:415:0x0273, B:417:0x0278, B:418:0x028c, B:420:0x02b9, B:422:0x02dd, B:424:0x02f5, B:429:0x02ff, B:430:0x0305, B:434:0x0312, B:435:0x0326, B:437:0x032b, B:438:0x033f, B:439:0x0352, B:441:0x035a, B:445:0x0363, B:446:0x01c4, B:449:0x01d1, B:450:0x01f0, B:451:0x018a, B:456:0x014d, B:458:0x0156, B:459:0x015f, B:464:0x010e, B:465:0x0111, B:471:0x00bb, B:473:0x00c1, B:478:0x0072, B:161:0x066c, B:314:0x0944), top: B:9:0x0022, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:452:0x01a9  */
    /* JADX WARN: Removed duplicated region for block: B:460:0x0141  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x012b A[Catch: Exception -> 0x0b3d, TryCatch #0 {Exception -> 0x0b3d, blocks: (B:10:0x0022, B:12:0x0046, B:15:0x004a, B:17:0x005a, B:18:0x0060, B:21:0x0074, B:25:0x0082, B:27:0x008e, B:28:0x0094, B:30:0x00a4, B:32:0x00b2, B:34:0x00b8, B:36:0x00d3, B:38:0x00df, B:42:0x00ff, B:44:0x0105, B:45:0x0115, B:47:0x011d, B:51:0x0125, B:53:0x012b, B:58:0x016a, B:61:0x0175, B:63:0x017d, B:64:0x01aa, B:66:0x01b5, B:70:0x022c, B:73:0x0242, B:78:0x025f, B:79:0x02a1, B:82:0x0373, B:93:0x038e, B:95:0x03aa, B:97:0x03df, B:99:0x03e9, B:100:0x03fc, B:102:0x0410, B:104:0x043f, B:107:0x0462, B:110:0x0471, B:112:0x0499, B:114:0x04cf, B:115:0x04f7, B:117:0x0505, B:122:0x0529, B:124:0x0533, B:125:0x0548, B:130:0x060b, B:132:0x0611, B:139:0x0628, B:141:0x062e, B:148:0x0640, B:151:0x064a, B:154:0x0653, B:169:0x067b, B:172:0x0684, B:174:0x06b7, B:178:0x06c6, B:181:0x06d0, B:182:0x06d7, B:184:0x06dd, B:187:0x06e2, B:189:0x06eb, B:192:0x06f3, B:194:0x06f9, B:196:0x06fd, B:198:0x0705, B:201:0x074e, B:204:0x07ac, B:206:0x07b0, B:208:0x07b6, B:209:0x07cc, B:211:0x07f1, B:213:0x07fe, B:220:0x084e, B:226:0x0896, B:229:0x08d3, B:231:0x08db, B:233:0x08df, B:235:0x08e7, B:239:0x08f0, B:241:0x098a, B:244:0x0999, B:247:0x0a01, B:249:0x0a07, B:251:0x0a0b, B:253:0x0a16, B:255:0x0a1c, B:257:0x0a27, B:259:0x0a36, B:261:0x0a42, B:263:0x0a62, B:264:0x0a6c, B:266:0x0a9d, B:271:0x0ab1, B:275:0x0acc, B:277:0x0ad2, B:279:0x0ada, B:281:0x0ae0, B:283:0x0af2, B:284:0x0b09, B:285:0x0b1f, B:290:0x09ad, B:297:0x09ce, B:300:0x09e2, B:302:0x091a, B:303:0x091f, B:304:0x0922, B:306:0x0928, B:309:0x0932, B:311:0x093a, B:316:0x0978, B:317:0x0980, B:319:0x089e, B:321:0x08a6, B:322:0x08ce, B:324:0x09ee, B:333:0x0863, B:337:0x0870, B:341:0x0879, B:344:0x0883, B:363:0x0712, B:365:0x0716, B:367:0x071a, B:369:0x0722, B:374:0x0730, B:376:0x073b, B:378:0x0744, B:387:0x0540, B:388:0x056a, B:390:0x0576, B:391:0x058b, B:392:0x0583, B:395:0x05b1, B:397:0x05bb, B:398:0x05d0, B:399:0x05c8, B:402:0x04e1, B:406:0x041c, B:408:0x0420, B:415:0x0273, B:417:0x0278, B:418:0x028c, B:420:0x02b9, B:422:0x02dd, B:424:0x02f5, B:429:0x02ff, B:430:0x0305, B:434:0x0312, B:435:0x0326, B:437:0x032b, B:438:0x033f, B:439:0x0352, B:441:0x035a, B:445:0x0363, B:446:0x01c4, B:449:0x01d1, B:450:0x01f0, B:451:0x018a, B:456:0x014d, B:458:0x0156, B:459:0x015f, B:464:0x010e, B:465:0x0111, B:471:0x00bb, B:473:0x00c1, B:478:0x0072, B:161:0x066c, B:314:0x0944), top: B:9:0x0022, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0175 A[Catch: Exception -> 0x0b3d, TRY_ENTER, TryCatch #0 {Exception -> 0x0b3d, blocks: (B:10:0x0022, B:12:0x0046, B:15:0x004a, B:17:0x005a, B:18:0x0060, B:21:0x0074, B:25:0x0082, B:27:0x008e, B:28:0x0094, B:30:0x00a4, B:32:0x00b2, B:34:0x00b8, B:36:0x00d3, B:38:0x00df, B:42:0x00ff, B:44:0x0105, B:45:0x0115, B:47:0x011d, B:51:0x0125, B:53:0x012b, B:58:0x016a, B:61:0x0175, B:63:0x017d, B:64:0x01aa, B:66:0x01b5, B:70:0x022c, B:73:0x0242, B:78:0x025f, B:79:0x02a1, B:82:0x0373, B:93:0x038e, B:95:0x03aa, B:97:0x03df, B:99:0x03e9, B:100:0x03fc, B:102:0x0410, B:104:0x043f, B:107:0x0462, B:110:0x0471, B:112:0x0499, B:114:0x04cf, B:115:0x04f7, B:117:0x0505, B:122:0x0529, B:124:0x0533, B:125:0x0548, B:130:0x060b, B:132:0x0611, B:139:0x0628, B:141:0x062e, B:148:0x0640, B:151:0x064a, B:154:0x0653, B:169:0x067b, B:172:0x0684, B:174:0x06b7, B:178:0x06c6, B:181:0x06d0, B:182:0x06d7, B:184:0x06dd, B:187:0x06e2, B:189:0x06eb, B:192:0x06f3, B:194:0x06f9, B:196:0x06fd, B:198:0x0705, B:201:0x074e, B:204:0x07ac, B:206:0x07b0, B:208:0x07b6, B:209:0x07cc, B:211:0x07f1, B:213:0x07fe, B:220:0x084e, B:226:0x0896, B:229:0x08d3, B:231:0x08db, B:233:0x08df, B:235:0x08e7, B:239:0x08f0, B:241:0x098a, B:244:0x0999, B:247:0x0a01, B:249:0x0a07, B:251:0x0a0b, B:253:0x0a16, B:255:0x0a1c, B:257:0x0a27, B:259:0x0a36, B:261:0x0a42, B:263:0x0a62, B:264:0x0a6c, B:266:0x0a9d, B:271:0x0ab1, B:275:0x0acc, B:277:0x0ad2, B:279:0x0ada, B:281:0x0ae0, B:283:0x0af2, B:284:0x0b09, B:285:0x0b1f, B:290:0x09ad, B:297:0x09ce, B:300:0x09e2, B:302:0x091a, B:303:0x091f, B:304:0x0922, B:306:0x0928, B:309:0x0932, B:311:0x093a, B:316:0x0978, B:317:0x0980, B:319:0x089e, B:321:0x08a6, B:322:0x08ce, B:324:0x09ee, B:333:0x0863, B:337:0x0870, B:341:0x0879, B:344:0x0883, B:363:0x0712, B:365:0x0716, B:367:0x071a, B:369:0x0722, B:374:0x0730, B:376:0x073b, B:378:0x0744, B:387:0x0540, B:388:0x056a, B:390:0x0576, B:391:0x058b, B:392:0x0583, B:395:0x05b1, B:397:0x05bb, B:398:0x05d0, B:399:0x05c8, B:402:0x04e1, B:406:0x041c, B:408:0x0420, B:415:0x0273, B:417:0x0278, B:418:0x028c, B:420:0x02b9, B:422:0x02dd, B:424:0x02f5, B:429:0x02ff, B:430:0x0305, B:434:0x0312, B:435:0x0326, B:437:0x032b, B:438:0x033f, B:439:0x0352, B:441:0x035a, B:445:0x0363, B:446:0x01c4, B:449:0x01d1, B:450:0x01f0, B:451:0x018a, B:456:0x014d, B:458:0x0156, B:459:0x015f, B:464:0x010e, B:465:0x0111, B:471:0x00bb, B:473:0x00c1, B:478:0x0072, B:161:0x066c, B:314:0x0944), top: B:9:0x0022, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x01b5 A[Catch: Exception -> 0x0b3d, TryCatch #0 {Exception -> 0x0b3d, blocks: (B:10:0x0022, B:12:0x0046, B:15:0x004a, B:17:0x005a, B:18:0x0060, B:21:0x0074, B:25:0x0082, B:27:0x008e, B:28:0x0094, B:30:0x00a4, B:32:0x00b2, B:34:0x00b8, B:36:0x00d3, B:38:0x00df, B:42:0x00ff, B:44:0x0105, B:45:0x0115, B:47:0x011d, B:51:0x0125, B:53:0x012b, B:58:0x016a, B:61:0x0175, B:63:0x017d, B:64:0x01aa, B:66:0x01b5, B:70:0x022c, B:73:0x0242, B:78:0x025f, B:79:0x02a1, B:82:0x0373, B:93:0x038e, B:95:0x03aa, B:97:0x03df, B:99:0x03e9, B:100:0x03fc, B:102:0x0410, B:104:0x043f, B:107:0x0462, B:110:0x0471, B:112:0x0499, B:114:0x04cf, B:115:0x04f7, B:117:0x0505, B:122:0x0529, B:124:0x0533, B:125:0x0548, B:130:0x060b, B:132:0x0611, B:139:0x0628, B:141:0x062e, B:148:0x0640, B:151:0x064a, B:154:0x0653, B:169:0x067b, B:172:0x0684, B:174:0x06b7, B:178:0x06c6, B:181:0x06d0, B:182:0x06d7, B:184:0x06dd, B:187:0x06e2, B:189:0x06eb, B:192:0x06f3, B:194:0x06f9, B:196:0x06fd, B:198:0x0705, B:201:0x074e, B:204:0x07ac, B:206:0x07b0, B:208:0x07b6, B:209:0x07cc, B:211:0x07f1, B:213:0x07fe, B:220:0x084e, B:226:0x0896, B:229:0x08d3, B:231:0x08db, B:233:0x08df, B:235:0x08e7, B:239:0x08f0, B:241:0x098a, B:244:0x0999, B:247:0x0a01, B:249:0x0a07, B:251:0x0a0b, B:253:0x0a16, B:255:0x0a1c, B:257:0x0a27, B:259:0x0a36, B:261:0x0a42, B:263:0x0a62, B:264:0x0a6c, B:266:0x0a9d, B:271:0x0ab1, B:275:0x0acc, B:277:0x0ad2, B:279:0x0ada, B:281:0x0ae0, B:283:0x0af2, B:284:0x0b09, B:285:0x0b1f, B:290:0x09ad, B:297:0x09ce, B:300:0x09e2, B:302:0x091a, B:303:0x091f, B:304:0x0922, B:306:0x0928, B:309:0x0932, B:311:0x093a, B:316:0x0978, B:317:0x0980, B:319:0x089e, B:321:0x08a6, B:322:0x08ce, B:324:0x09ee, B:333:0x0863, B:337:0x0870, B:341:0x0879, B:344:0x0883, B:363:0x0712, B:365:0x0716, B:367:0x071a, B:369:0x0722, B:374:0x0730, B:376:0x073b, B:378:0x0744, B:387:0x0540, B:388:0x056a, B:390:0x0576, B:391:0x058b, B:392:0x0583, B:395:0x05b1, B:397:0x05bb, B:398:0x05d0, B:399:0x05c8, B:402:0x04e1, B:406:0x041c, B:408:0x0420, B:415:0x0273, B:417:0x0278, B:418:0x028c, B:420:0x02b9, B:422:0x02dd, B:424:0x02f5, B:429:0x02ff, B:430:0x0305, B:434:0x0312, B:435:0x0326, B:437:0x032b, B:438:0x033f, B:439:0x0352, B:441:0x035a, B:445:0x0363, B:446:0x01c4, B:449:0x01d1, B:450:0x01f0, B:451:0x018a, B:456:0x014d, B:458:0x0156, B:459:0x015f, B:464:0x010e, B:465:0x0111, B:471:0x00bb, B:473:0x00c1, B:478:0x0072, B:161:0x066c, B:314:0x0944), top: B:9:0x0022, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0242 A[Catch: Exception -> 0x0b3d, TRY_ENTER, TryCatch #0 {Exception -> 0x0b3d, blocks: (B:10:0x0022, B:12:0x0046, B:15:0x004a, B:17:0x005a, B:18:0x0060, B:21:0x0074, B:25:0x0082, B:27:0x008e, B:28:0x0094, B:30:0x00a4, B:32:0x00b2, B:34:0x00b8, B:36:0x00d3, B:38:0x00df, B:42:0x00ff, B:44:0x0105, B:45:0x0115, B:47:0x011d, B:51:0x0125, B:53:0x012b, B:58:0x016a, B:61:0x0175, B:63:0x017d, B:64:0x01aa, B:66:0x01b5, B:70:0x022c, B:73:0x0242, B:78:0x025f, B:79:0x02a1, B:82:0x0373, B:93:0x038e, B:95:0x03aa, B:97:0x03df, B:99:0x03e9, B:100:0x03fc, B:102:0x0410, B:104:0x043f, B:107:0x0462, B:110:0x0471, B:112:0x0499, B:114:0x04cf, B:115:0x04f7, B:117:0x0505, B:122:0x0529, B:124:0x0533, B:125:0x0548, B:130:0x060b, B:132:0x0611, B:139:0x0628, B:141:0x062e, B:148:0x0640, B:151:0x064a, B:154:0x0653, B:169:0x067b, B:172:0x0684, B:174:0x06b7, B:178:0x06c6, B:181:0x06d0, B:182:0x06d7, B:184:0x06dd, B:187:0x06e2, B:189:0x06eb, B:192:0x06f3, B:194:0x06f9, B:196:0x06fd, B:198:0x0705, B:201:0x074e, B:204:0x07ac, B:206:0x07b0, B:208:0x07b6, B:209:0x07cc, B:211:0x07f1, B:213:0x07fe, B:220:0x084e, B:226:0x0896, B:229:0x08d3, B:231:0x08db, B:233:0x08df, B:235:0x08e7, B:239:0x08f0, B:241:0x098a, B:244:0x0999, B:247:0x0a01, B:249:0x0a07, B:251:0x0a0b, B:253:0x0a16, B:255:0x0a1c, B:257:0x0a27, B:259:0x0a36, B:261:0x0a42, B:263:0x0a62, B:264:0x0a6c, B:266:0x0a9d, B:271:0x0ab1, B:275:0x0acc, B:277:0x0ad2, B:279:0x0ada, B:281:0x0ae0, B:283:0x0af2, B:284:0x0b09, B:285:0x0b1f, B:290:0x09ad, B:297:0x09ce, B:300:0x09e2, B:302:0x091a, B:303:0x091f, B:304:0x0922, B:306:0x0928, B:309:0x0932, B:311:0x093a, B:316:0x0978, B:317:0x0980, B:319:0x089e, B:321:0x08a6, B:322:0x08ce, B:324:0x09ee, B:333:0x0863, B:337:0x0870, B:341:0x0879, B:344:0x0883, B:363:0x0712, B:365:0x0716, B:367:0x071a, B:369:0x0722, B:374:0x0730, B:376:0x073b, B:378:0x0744, B:387:0x0540, B:388:0x056a, B:390:0x0576, B:391:0x058b, B:392:0x0583, B:395:0x05b1, B:397:0x05bb, B:398:0x05d0, B:399:0x05c8, B:402:0x04e1, B:406:0x041c, B:408:0x0420, B:415:0x0273, B:417:0x0278, B:418:0x028c, B:420:0x02b9, B:422:0x02dd, B:424:0x02f5, B:429:0x02ff, B:430:0x0305, B:434:0x0312, B:435:0x0326, B:437:0x032b, B:438:0x033f, B:439:0x0352, B:441:0x035a, B:445:0x0363, B:446:0x01c4, B:449:0x01d1, B:450:0x01f0, B:451:0x018a, B:456:0x014d, B:458:0x0156, B:459:0x015f, B:464:0x010e, B:465:0x0111, B:471:0x00bb, B:473:0x00c1, B:478:0x0072, B:161:0x066c, B:314:0x0944), top: B:9:0x0022, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0371 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0388  */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v15 */
    /* JADX WARN: Type inference failed for: r2v16 */
    /* JADX WARN: Type inference failed for: r2v17 */
    /* JADX WARN: Type inference failed for: r2v62 */
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
        NotificationsSettingsFacade notificationsSettingsFacade;
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
        int i11;
        TLRPC$Chat tLRPC$Chat3;
        int i12;
        long j8;
        TLRPC$FileLocation tLRPC$FileLocation;
        boolean z17;
        int i13;
        int i14;
        long[] jArr;
        Uri uri;
        long j9;
        boolean z18;
        int i15;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList;
        int i16;
        int i17;
        MessageObject messageObject5;
        TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow;
        long j10;
        Uri uri2;
        long[] jArr2;
        long[] jArr3;
        String str8;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        TLRPC$FileLocation tLRPC$FileLocation2;
        int ringerMode;
        String string3;
        boolean z19;
        String string4;
        boolean z20;
        String propertyString;
        int i18;
        int i19;
        String str9;
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
            int topicId = MessageObject.getTopicId(messageObject6.messageOwner);
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
            String str10 = userName;
            if (!AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
                z4 = false;
                if (DialogObject.isEncryptedDialog(j)) {
                    tLRPC$User = user;
                    messageObject3 = messageObject2;
                    if (this.pushDialogs.size() <= 1 && !z4) {
                        string = str10;
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
                                    str9 = stringForMessage;
                                } else if (tLRPC$Chat != null) {
                                    str9 = stringForMessage.replace(" @ " + string, "");
                                } else if (zArr[0]) {
                                    str9 = stringForMessage.replace(string + ": ", "");
                                } else {
                                    str9 = stringForMessage.replace(string + " ", "");
                                }
                                builder2.setContentText(str9);
                                builder2.setStyle(new NotificationCompat.BigTextStyle().bigText(str9));
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
                                String str11 = null;
                                while (i21 < min) {
                                    int i22 = min;
                                    MessageObject messageObject8 = this.pushMessages.get(i21);
                                    NotificationCompat.InboxStyle inboxStyle3 = inboxStyle2;
                                    String str12 = sb;
                                    int i23 = i21;
                                    String stringForMessage2 = getStringForMessage(messageObject8, false, zArr2, null);
                                    if (stringForMessage2 != null && messageObject8.messageOwner.date > i20) {
                                        z22 = z22;
                                        if (z22 == 2) {
                                            str11 = stringForMessage2;
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
                                        sb = str12;
                                        z22 = z22;
                                    }
                                    inboxStyle = inboxStyle3;
                                    i21 = i23 + 1;
                                    inboxStyle2 = inboxStyle;
                                    min = i22;
                                    sb = str12;
                                    z22 = z22;
                                }
                                str2 = sb;
                                NotificationCompat.InboxStyle inboxStyle4 = inboxStyle2;
                                inboxStyle4.setSummaryText(str2);
                                builder = builder2;
                                builder.setStyle(inboxStyle4);
                                str3 = str11;
                                z7 = z22;
                            }
                            if (z && z6 && !MediaController.getInstance().isRecordingAudio() && !z7) {
                                z8 = false;
                                if (!z8 || j3 != j4 || tLRPC$Chat == null) {
                                    str4 = str2;
                                    sharedPreferences3 = sharedPreferences2;
                                    j5 = j3;
                                } else {
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
                                            notificationsSettingsFacade = this.dialogsNotificationsFacade;
                                            NotificationCompat.Builder builder3 = builder;
                                            boolean z23 = z7 ? 1 : 0;
                                            TLRPC$User tLRPC$User2 = tLRPC$User;
                                            SharedPreferences sharedPreferences5 = sharedPreferences3;
                                            j6 = j5;
                                            MessageObject messageObject9 = messageObject3;
                                            if (!notificationsSettingsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, i, false)) {
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
                                            } else {
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
                                                            i10 = i7;
                                                            i11 = 0;
                                                        } else if (i3 == 2) {
                                                            i10 = i7;
                                                            i11 = 1;
                                                        }
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
                                                            str6 = null;
                                                            i10 = 0;
                                                            i5 = 0;
                                                            i11 = 0;
                                                        }
                                                        Intent intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                        StringBuilder sb4 = new StringBuilder();
                                                        sb4.append("com.tmessages.openchat");
                                                        String str13 = str6;
                                                        sb4.append(Math.random());
                                                        sb4.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                        intent.setAction(sb4.toString());
                                                        intent.setFlags(ConnectionsManager.FileTypeFile);
                                                        if (DialogObject.isEncryptedDialog(j6)) {
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
                                                                        tLRPC$FileLocation2 = tLRPC$ChatPhoto.photo_small;
                                                                        if (tLRPC$FileLocation2 != null) {
                                                                            if (tLRPC$FileLocation2.volume_id != 0) {
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    tLRPC$Chat3 = tLRPC$Chat2;
                                                                    if (tLRPC$User2 != null && (tLRPC$UserProfilePhoto = tLRPC$User2.photo) != null && (tLRPC$FileLocation2 = tLRPC$UserProfilePhoto.photo_small) != null && tLRPC$FileLocation2.volume_id != 0 && tLRPC$FileLocation2.local_id != 0) {
                                                                        tLRPC$FileLocation = tLRPC$FileLocation2;
                                                                        i12 = i10;
                                                                        j8 = j6;
                                                                        boolean z24 = z16;
                                                                        intent.putExtra("currentAccount", this.currentAccount);
                                                                        long j16 = j8;
                                                                        MessageObject messageObject10 = messageObject4;
                                                                        int i26 = i5;
                                                                        builder3.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject10.messageOwner.date * 1000).setColor(-15618822);
                                                                        builder3.setCategory("msg");
                                                                        if (tLRPC$Chat3 == null && tLRPC$User2 != null && (str8 = tLRPC$User2.phone) != null && str8.length() > 0) {
                                                                            builder3.addPerson("tel:+" + tLRPC$User2.phone);
                                                                        }
                                                                        Intent intent2 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                                        intent2.putExtra("messageDate", messageObject10.messageOwner.date);
                                                                        intent2.putExtra("currentAccount", this.currentAccount);
                                                                        builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent2, 134217728));
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
                                                                        } else if (i11 == 0) {
                                                                            builder3.setPriority(0);
                                                                            if (Build.VERSION.SDK_INT >= 26) {
                                                                                z17 = true;
                                                                                i13 = 3;
                                                                            }
                                                                            z17 = true;
                                                                            i13 = 0;
                                                                        } else {
                                                                            int i27 = 1;
                                                                            if (i11 != 1) {
                                                                                if (i11 == 2) {
                                                                                    i27 = 1;
                                                                                } else {
                                                                                    if (i11 == 4) {
                                                                                        builder3.setPriority(-2);
                                                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                                                            z17 = true;
                                                                                            i13 = 1;
                                                                                        }
                                                                                    } else if (i11 == 5) {
                                                                                        builder3.setPriority(-1);
                                                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                                                            z17 = true;
                                                                                            i13 = 2;
                                                                                        }
                                                                                    }
                                                                                    z17 = true;
                                                                                    i13 = 0;
                                                                                }
                                                                            }
                                                                            builder3.setPriority(i27);
                                                                            if (Build.VERSION.SDK_INT >= 26) {
                                                                                z17 = true;
                                                                                i13 = 4;
                                                                            }
                                                                            z17 = true;
                                                                            i13 = 0;
                                                                        }
                                                                        if (z23 != z17 && !z9) {
                                                                            if (!z10 || sharedPreferences4.getBoolean("EnableInAppPreview", z17)) {
                                                                                builder3.setTicker(str5.length() > 100 ? str5.substring(0, 100).replace('\n', ' ').trim() + "..." : str5);
                                                                            }
                                                                            if (str13 != null && !str13.equals("NoSound")) {
                                                                                int i28 = Build.VERSION.SDK_INT;
                                                                                if (i28 >= 26) {
                                                                                    if (!str13.equals("Default") && !str13.equals(path)) {
                                                                                        if (z11) {
                                                                                            uri2 = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", new File(str13));
                                                                                            ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uri2, 1);
                                                                                        } else {
                                                                                            uri2 = Uri.parse(str13);
                                                                                        }
                                                                                        if (i26 != 0) {
                                                                                            i14 = i26;
                                                                                            builder3.setLights(i14, 1000, 1000);
                                                                                        } else {
                                                                                            i14 = i26;
                                                                                        }
                                                                                        if (i12 == 2) {
                                                                                            jArr3 = new long[]{0, 0};
                                                                                            builder3.setVibrate(jArr3);
                                                                                        } else if (i12 == 1) {
                                                                                            jArr3 = new long[]{0, 100, 0, 100};
                                                                                            builder3.setVibrate(jArr3);
                                                                                        } else {
                                                                                            if (i12 != 0 && i12 != 4) {
                                                                                                if (i12 == 3) {
                                                                                                    jArr2 = new long[]{0, 1000};
                                                                                                    builder3.setVibrate(jArr2);
                                                                                                    uri = uri2;
                                                                                                    jArr = jArr2;
                                                                                                } else {
                                                                                                    uri = uri2;
                                                                                                    jArr = null;
                                                                                                }
                                                                                            }
                                                                                            builder3.setDefaults(2);
                                                                                            jArr2 = new long[0];
                                                                                            uri = uri2;
                                                                                            jArr = jArr2;
                                                                                        }
                                                                                        uri = uri2;
                                                                                        jArr = jArr3;
                                                                                    }
                                                                                    uri2 = Settings.System.DEFAULT_NOTIFICATION_URI;
                                                                                    if (i26 != 0) {
                                                                                    }
                                                                                    if (i12 == 2) {
                                                                                    }
                                                                                    uri = uri2;
                                                                                    jArr = jArr3;
                                                                                } else if (str13.equals(path)) {
                                                                                    builder3.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, 5);
                                                                                } else if (i28 >= 24 && str13.startsWith("file://") && !AndroidUtilities.isInternalUri(Uri.parse(str13))) {
                                                                                    try {
                                                                                        Uri uriForFile = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", new File(str13.replace("file://", "")));
                                                                                        ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile, 1);
                                                                                        builder3.setSound(uriForFile, 5);
                                                                                    } catch (Exception unused2) {
                                                                                        builder3.setSound(Uri.parse(str13), 5);
                                                                                    }
                                                                                } else {
                                                                                    builder3.setSound(Uri.parse(str13), 5);
                                                                                }
                                                                            }
                                                                            uri2 = null;
                                                                            if (i26 != 0) {
                                                                            }
                                                                            if (i12 == 2) {
                                                                            }
                                                                            uri = uri2;
                                                                            jArr = jArr3;
                                                                        } else {
                                                                            i14 = i26;
                                                                            long[] jArr4 = {0, 0};
                                                                            builder3.setVibrate(jArr4);
                                                                            jArr = jArr4;
                                                                            uri = null;
                                                                        }
                                                                        if (AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter || messageObject10.getDialogId() != 777000 || (tLRPC$ReplyMarkup = messageObject10.messageOwner.reply_markup) == null) {
                                                                            j9 = j16;
                                                                            z18 = false;
                                                                        } else {
                                                                            ArrayList<TLRPC$TL_keyboardButtonRow> arrayList2 = tLRPC$ReplyMarkup.rows;
                                                                            int size = arrayList2.size();
                                                                            boolean z25 = false;
                                                                            for (int i29 = 0; i29 < size; i29++) {
                                                                                TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow2 = arrayList2.get(i29);
                                                                                int size2 = tLRPC$TL_keyboardButtonRow2.buttons.size();
                                                                                int i30 = 0;
                                                                                while (i30 < size2) {
                                                                                    TLRPC$KeyboardButton tLRPC$KeyboardButton = tLRPC$TL_keyboardButtonRow2.buttons.get(i30);
                                                                                    if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonCallback) {
                                                                                        arrayList = arrayList2;
                                                                                        i16 = size;
                                                                                        Intent intent3 = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                                                                        intent3.putExtra("currentAccount", this.currentAccount);
                                                                                        TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow3 = tLRPC$TL_keyboardButtonRow2;
                                                                                        i17 = size2;
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
                                                                                        String str14 = tLRPC$KeyboardButton.text;
                                                                                        Context context = ApplicationLoader.applicationContext;
                                                                                        int i31 = this.lastButtonId;
                                                                                        messageObject5 = messageObject10;
                                                                                        this.lastButtonId = i31 + 1;
                                                                                        builder3.addAction(0, str14, PendingIntent.getBroadcast(context, i31, intent3, 134217728));
                                                                                        z25 = true;
                                                                                    } else {
                                                                                        arrayList = arrayList2;
                                                                                        i16 = size;
                                                                                        i17 = size2;
                                                                                        messageObject5 = messageObject10;
                                                                                        long j17 = j16;
                                                                                        tLRPC$TL_keyboardButtonRow = tLRPC$TL_keyboardButtonRow2;
                                                                                        j10 = j17;
                                                                                    }
                                                                                    i30++;
                                                                                    size = i16;
                                                                                    arrayList2 = arrayList;
                                                                                    messageObject10 = messageObject5;
                                                                                    long j18 = j10;
                                                                                    tLRPC$TL_keyboardButtonRow2 = tLRPC$TL_keyboardButtonRow;
                                                                                    j16 = j18;
                                                                                    size2 = i17;
                                                                                }
                                                                            }
                                                                            j9 = j16;
                                                                            z18 = z25;
                                                                        }
                                                                        if (!z18 && (i15 = Build.VERSION.SDK_INT) < 24 && SharedConfig.passcodeHash.length() == 0 && hasMessagesToReply()) {
                                                                            Intent intent4 = new Intent(ApplicationLoader.applicationContext, PopupReplyReceiver.class);
                                                                            intent4.putExtra("currentAccount", this.currentAccount);
                                                                            if (i15 <= 19) {
                                                                                builder3.addAction(R.drawable.ic_ab_reply2, LocaleController.getString("Reply", R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent4, 134217728));
                                                                            } else {
                                                                                builder3.addAction(R.drawable.ic_ab_reply, LocaleController.getString("Reply", R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent4, 134217728));
                                                                            }
                                                                        }
                                                                        showExtraNotifications(builder3, str4, j9, i4, str10, jArr, i14, uri, i13, z24, z10, z9, i8);
                                                                        scheduleNotificationRepeat();
                                                                        return;
                                                                    }
                                                                }
                                                            }
                                                            tLRPC$Chat3 = tLRPC$Chat2;
                                                        } else {
                                                            tLRPC$Chat3 = tLRPC$Chat2;
                                                            if (this.pushDialogs.size() == 1) {
                                                                i12 = i10;
                                                                j8 = j6;
                                                                if (j8 != globalSecretChatId) {
                                                                    intent.putExtra("encId", DialogObject.getEncryptedChatId(j8));
                                                                }
                                                                tLRPC$FileLocation = null;
                                                                boolean z242 = z16;
                                                                intent.putExtra("currentAccount", this.currentAccount);
                                                                long j162 = j8;
                                                                MessageObject messageObject102 = messageObject4;
                                                                int i262 = i5;
                                                                builder3.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject102.messageOwner.date * 1000).setColor(-15618822);
                                                                builder3.setCategory("msg");
                                                                if (tLRPC$Chat3 == null) {
                                                                    builder3.addPerson("tel:+" + tLRPC$User2.phone);
                                                                }
                                                                Intent intent22 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                                intent22.putExtra("messageDate", messageObject102.messageOwner.date);
                                                                intent22.putExtra("currentAccount", this.currentAccount);
                                                                builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent22, 134217728));
                                                                if (tLRPC$FileLocation != null) {
                                                                }
                                                                if (z) {
                                                                }
                                                                builder3.setPriority(-1);
                                                            }
                                                        }
                                                        i12 = i10;
                                                        j8 = j6;
                                                        tLRPC$FileLocation = null;
                                                        boolean z2422 = z16;
                                                        intent.putExtra("currentAccount", this.currentAccount);
                                                        long j1622 = j8;
                                                        MessageObject messageObject1022 = messageObject4;
                                                        int i2622 = i5;
                                                        builder3.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject1022.messageOwner.date * 1000).setColor(-15618822);
                                                        builder3.setCategory("msg");
                                                        if (tLRPC$Chat3 == null) {
                                                        }
                                                        Intent intent222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                        intent222.putExtra("messageDate", messageObject1022.messageOwner.date);
                                                        intent222.putExtra("currentAccount", this.currentAccount);
                                                        builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent222, 134217728));
                                                        if (tLRPC$FileLocation != null) {
                                                        }
                                                        if (z) {
                                                        }
                                                        builder3.setPriority(-1);
                                                    }
                                                    i10 = i7;
                                                    i11 = i3;
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
                                                    String str132 = str6;
                                                    sb42.append(Math.random());
                                                    sb42.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                    intent5.setAction(sb42.toString());
                                                    intent5.setFlags(ConnectionsManager.FileTypeFile);
                                                    if (DialogObject.isEncryptedDialog(j6)) {
                                                    }
                                                    i12 = i10;
                                                    j8 = j6;
                                                    tLRPC$FileLocation = null;
                                                    boolean z24222 = z16;
                                                    intent5.putExtra("currentAccount", this.currentAccount);
                                                    long j16222 = j8;
                                                    MessageObject messageObject10222 = messageObject4;
                                                    int i26222 = i5;
                                                    builder3.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent5, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject10222.messageOwner.date * 1000).setColor(-15618822);
                                                    builder3.setCategory("msg");
                                                    if (tLRPC$Chat3 == null) {
                                                    }
                                                    Intent intent2222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                    intent2222.putExtra("messageDate", messageObject10222.messageOwner.date);
                                                    intent2222.putExtra("currentAccount", this.currentAccount);
                                                    builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent2222, 134217728));
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
                                                int i32 = sharedPreferences4.getInt("vibrate_channel", 0);
                                                z12 = z20;
                                                i6 = sharedPreferences4.getInt("priority_channel", 1);
                                                i5 = sharedPreferences4.getInt("ChannelLed", -16776961);
                                                str7 = string4;
                                                i7 = i32;
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
                                                int i33 = sharedPreferences4.getInt("vibrate_group", 0);
                                                z12 = z19;
                                                i6 = sharedPreferences4.getInt("priority_group", 1);
                                                i5 = sharedPreferences4.getInt("GroupLed", -16776961);
                                                str7 = string3;
                                                i7 = i33;
                                                i8 = 0;
                                            }
                                            i4 = i;
                                            if (i7 == 4) {
                                            }
                                            if (!TextUtils.isEmpty(str6)) {
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
                                            i10 = i7;
                                            i11 = i3;
                                            if (z14) {
                                            }
                                            if (z9) {
                                            }
                                            Intent intent52 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                            StringBuilder sb422 = new StringBuilder();
                                            sb422.append("com.tmessages.openchat");
                                            String str1322 = str6;
                                            sb422.append(Math.random());
                                            sb422.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                            intent52.setAction(sb422.toString());
                                            intent52.setFlags(ConnectionsManager.FileTypeFile);
                                            if (DialogObject.isEncryptedDialog(j6)) {
                                            }
                                            i12 = i10;
                                            j8 = j6;
                                            tLRPC$FileLocation = null;
                                            boolean z242222 = z16;
                                            intent52.putExtra("currentAccount", this.currentAccount);
                                            long j162222 = j8;
                                            MessageObject messageObject102222 = messageObject4;
                                            int i262222 = i5;
                                            builder3.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent52, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject102222.messageOwner.date * 1000).setColor(-15618822);
                                            builder3.setCategory("msg");
                                            if (tLRPC$Chat3 == null) {
                                            }
                                            Intent intent22222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                            intent22222.putExtra("messageDate", messageObject102222.messageOwner.date);
                                            intent22222.putExtra("currentAccount", this.currentAccount);
                                            builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent22222, 134217728));
                                            if (tLRPC$FileLocation != null) {
                                            }
                                            if (z) {
                                            }
                                            builder3.setPriority(-1);
                                        }
                                    }
                                    str4 = str2;
                                }
                                str5 = str3;
                                z9 = z8;
                                if (z9) {
                                }
                                String path2 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                                if (ApplicationLoader.mainInterfacePaused) {
                                }
                                getSharedPrefKey(j5, i);
                                notificationsSettingsFacade = this.dialogsNotificationsFacade;
                                NotificationCompat.Builder builder32 = builder;
                                boolean z232 = z7 ? 1 : 0;
                                TLRPC$User tLRPC$User22 = tLRPC$User;
                                SharedPreferences sharedPreferences52 = sharedPreferences3;
                                j6 = j5;
                                MessageObject messageObject92 = messageObject3;
                                if (!notificationsSettingsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, i, false)) {
                                }
                                if (j2 != j7) {
                                }
                                i4 = i;
                                if (i7 == 4) {
                                }
                                if (!TextUtils.isEmpty(str6)) {
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
                                i10 = i7;
                                i11 = i3;
                                if (z14) {
                                }
                                if (z9) {
                                }
                                Intent intent522 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                StringBuilder sb4222 = new StringBuilder();
                                sb4222.append("com.tmessages.openchat");
                                String str13222 = str6;
                                sb4222.append(Math.random());
                                sb4222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                intent522.setAction(sb4222.toString());
                                intent522.setFlags(ConnectionsManager.FileTypeFile);
                                if (DialogObject.isEncryptedDialog(j6)) {
                                }
                                i12 = i10;
                                j8 = j6;
                                tLRPC$FileLocation = null;
                                boolean z2422222 = z16;
                                intent522.putExtra("currentAccount", this.currentAccount);
                                long j1622222 = j8;
                                MessageObject messageObject1022222 = messageObject4;
                                int i2622222 = i5;
                                builder32.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent522, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject1022222.messageOwner.date * 1000).setColor(-15618822);
                                builder32.setCategory("msg");
                                if (tLRPC$Chat3 == null) {
                                }
                                Intent intent222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                intent222222.putExtra("messageDate", messageObject1022222.messageOwner.date);
                                intent222222.putExtra("currentAccount", this.currentAccount);
                                builder32.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent222222, 134217728));
                                if (tLRPC$FileLocation != null) {
                                }
                                if (z) {
                                }
                                builder32.setPriority(-1);
                            }
                            z8 = true;
                            if (!z8) {
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
                            notificationsSettingsFacade = this.dialogsNotificationsFacade;
                            NotificationCompat.Builder builder322 = builder;
                            boolean z2322 = z7 ? 1 : 0;
                            TLRPC$User tLRPC$User222 = tLRPC$User;
                            SharedPreferences sharedPreferences522 = sharedPreferences3;
                            j6 = j5;
                            MessageObject messageObject922 = messageObject3;
                            if (!notificationsSettingsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, i, false)) {
                            }
                            if (j2 != j7) {
                            }
                            i4 = i;
                            if (i7 == 4) {
                            }
                            if (!TextUtils.isEmpty(str6)) {
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
                            i10 = i7;
                            i11 = i3;
                            if (z14) {
                            }
                            if (z9) {
                            }
                            Intent intent5222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                            StringBuilder sb42222 = new StringBuilder();
                            sb42222.append("com.tmessages.openchat");
                            String str132222 = str6;
                            sb42222.append(Math.random());
                            sb42222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                            intent5222.setAction(sb42222.toString());
                            intent5222.setFlags(ConnectionsManager.FileTypeFile);
                            if (DialogObject.isEncryptedDialog(j6)) {
                            }
                            i12 = i10;
                            j8 = j6;
                            tLRPC$FileLocation = null;
                            boolean z24222222 = z16;
                            intent5222.putExtra("currentAccount", this.currentAccount);
                            long j16222222 = j8;
                            MessageObject messageObject10222222 = messageObject4;
                            int i26222222 = i5;
                            builder322.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent5222, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject10222222.messageOwner.date * 1000).setColor(-15618822);
                            builder322.setCategory("msg");
                            if (tLRPC$Chat3 == null) {
                            }
                            Intent intent2222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                            intent2222222.putExtra("messageDate", messageObject10222222.messageOwner.date);
                            intent2222222.putExtra("currentAccount", this.currentAccount);
                            builder322.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent2222222, 134217728));
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
                            if (!z8) {
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
                            notificationsSettingsFacade = this.dialogsNotificationsFacade;
                            NotificationCompat.Builder builder3222 = builder;
                            boolean z23222 = z7 ? 1 : 0;
                            TLRPC$User tLRPC$User2222 = tLRPC$User;
                            SharedPreferences sharedPreferences5222 = sharedPreferences3;
                            j6 = j5;
                            MessageObject messageObject9222 = messageObject3;
                            if (!notificationsSettingsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, i, false)) {
                            }
                            if (j2 != j7) {
                            }
                            i4 = i;
                            if (i7 == 4) {
                            }
                            if (!TextUtils.isEmpty(str6)) {
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
                            i10 = i7;
                            i11 = i3;
                            if (z14) {
                            }
                            if (z9) {
                            }
                            Intent intent52222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                            StringBuilder sb422222 = new StringBuilder();
                            sb422222.append("com.tmessages.openchat");
                            String str1322222 = str6;
                            sb422222.append(Math.random());
                            sb422222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                            intent52222.setAction(sb422222.toString());
                            intent52222.setFlags(ConnectionsManager.FileTypeFile);
                            if (DialogObject.isEncryptedDialog(j6)) {
                            }
                            i12 = i10;
                            j8 = j6;
                            tLRPC$FileLocation = null;
                            boolean z242222222 = z16;
                            intent52222.putExtra("currentAccount", this.currentAccount);
                            long j162222222 = j8;
                            MessageObject messageObject102222222 = messageObject4;
                            int i262222222 = i5;
                            builder3222.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent52222, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject102222222.messageOwner.date * 1000).setColor(-15618822);
                            builder3222.setCategory("msg");
                            if (tLRPC$Chat3 == null) {
                            }
                            Intent intent22222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                            intent22222222.putExtra("messageDate", messageObject102222222.messageOwner.date);
                            intent22222222.putExtra("currentAccount", this.currentAccount);
                            builder3222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent22222222, 134217728));
                            if (tLRPC$FileLocation != null) {
                            }
                            if (z) {
                            }
                            builder3222.setPriority(-1);
                        }
                        z8 = true;
                        if (!z8) {
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
                        notificationsSettingsFacade = this.dialogsNotificationsFacade;
                        NotificationCompat.Builder builder32222 = builder;
                        boolean z232222 = z7 ? 1 : 0;
                        TLRPC$User tLRPC$User22222 = tLRPC$User;
                        SharedPreferences sharedPreferences52222 = sharedPreferences3;
                        j6 = j5;
                        MessageObject messageObject92222 = messageObject3;
                        if (!notificationsSettingsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, i, false)) {
                        }
                        if (j2 != j7) {
                        }
                        i4 = i;
                        if (i7 == 4) {
                        }
                        if (!TextUtils.isEmpty(str6)) {
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
                        i10 = i7;
                        i11 = i3;
                        if (z14) {
                        }
                        if (z9) {
                        }
                        Intent intent522222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        StringBuilder sb4222222 = new StringBuilder();
                        sb4222222.append("com.tmessages.openchat");
                        String str13222222 = str6;
                        sb4222222.append(Math.random());
                        sb4222222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                        intent522222.setAction(sb4222222.toString());
                        intent522222.setFlags(ConnectionsManager.FileTypeFile);
                        if (DialogObject.isEncryptedDialog(j6)) {
                        }
                        i12 = i10;
                        j8 = j6;
                        tLRPC$FileLocation = null;
                        boolean z2422222222 = z16;
                        intent522222.putExtra("currentAccount", this.currentAccount);
                        long j1622222222 = j8;
                        MessageObject messageObject1022222222 = messageObject4;
                        int i2622222222 = i5;
                        builder32222.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent522222, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject1022222222.messageOwner.date * 1000).setColor(-15618822);
                        builder32222.setCategory("msg");
                        if (tLRPC$Chat3 == null) {
                        }
                        Intent intent222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                        intent222222222.putExtra("messageDate", messageObject1022222222.messageOwner.date);
                        intent222222222.putExtra("currentAccount", this.currentAccount);
                        builder32222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent222222222, 134217728));
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
                    if (!z8) {
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
                    notificationsSettingsFacade = this.dialogsNotificationsFacade;
                    NotificationCompat.Builder builder322222 = builder;
                    boolean z2322222 = z7 ? 1 : 0;
                    TLRPC$User tLRPC$User222222 = tLRPC$User;
                    SharedPreferences sharedPreferences522222 = sharedPreferences3;
                    j6 = j5;
                    MessageObject messageObject922222 = messageObject3;
                    if (!notificationsSettingsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, i, false)) {
                    }
                    if (j2 != j7) {
                    }
                    i4 = i;
                    if (i7 == 4) {
                    }
                    if (!TextUtils.isEmpty(str6)) {
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
                    i10 = i7;
                    i11 = i3;
                    if (z14) {
                    }
                    if (z9) {
                    }
                    Intent intent5222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    StringBuilder sb42222222 = new StringBuilder();
                    sb42222222.append("com.tmessages.openchat");
                    String str132222222 = str6;
                    sb42222222.append(Math.random());
                    sb42222222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                    intent5222222.setAction(sb42222222.toString());
                    intent5222222.setFlags(ConnectionsManager.FileTypeFile);
                    if (DialogObject.isEncryptedDialog(j6)) {
                    }
                    i12 = i10;
                    j8 = j6;
                    tLRPC$FileLocation = null;
                    boolean z24222222222 = z16;
                    intent5222222.putExtra("currentAccount", this.currentAccount);
                    long j16222222222 = j8;
                    MessageObject messageObject10222222222 = messageObject4;
                    int i26222222222 = i5;
                    builder322222.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent5222222, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject10222222222.messageOwner.date * 1000).setColor(-15618822);
                    builder322222.setCategory("msg");
                    if (tLRPC$Chat3 == null) {
                    }
                    Intent intent2222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                    intent2222222222.putExtra("messageDate", messageObject10222222222.messageOwner.date);
                    intent2222222222.putExtra("currentAccount", this.currentAccount);
                    builder322222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent2222222222, 134217728));
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
                if (!z8) {
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
                notificationsSettingsFacade = this.dialogsNotificationsFacade;
                NotificationCompat.Builder builder3222222 = builder;
                boolean z23222222 = z7 ? 1 : 0;
                TLRPC$User tLRPC$User2222222 = tLRPC$User;
                SharedPreferences sharedPreferences5222222 = sharedPreferences3;
                j6 = j5;
                MessageObject messageObject9222222 = messageObject3;
                if (!notificationsSettingsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, i, false)) {
                }
                if (j2 != j7) {
                }
                i4 = i;
                if (i7 == 4) {
                }
                if (!TextUtils.isEmpty(str6)) {
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
                i10 = i7;
                i11 = i3;
                if (z14) {
                }
                if (z9) {
                }
                Intent intent52222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                StringBuilder sb422222222 = new StringBuilder();
                sb422222222.append("com.tmessages.openchat");
                String str1322222222 = str6;
                sb422222222.append(Math.random());
                sb422222222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                intent52222222.setAction(sb422222222.toString());
                intent52222222.setFlags(ConnectionsManager.FileTypeFile);
                if (DialogObject.isEncryptedDialog(j6)) {
                }
                i12 = i10;
                j8 = j6;
                tLRPC$FileLocation = null;
                boolean z242222222222 = z16;
                intent52222222.putExtra("currentAccount", this.currentAccount);
                long j162222222222 = j8;
                MessageObject messageObject102222222222 = messageObject4;
                int i262222222222 = i5;
                builder3222222.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent52222222, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject102222222222.messageOwner.date * 1000).setColor(-15618822);
                builder3222222.setCategory("msg");
                if (tLRPC$Chat3 == null) {
                }
                Intent intent22222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                intent22222222222.putExtra("messageDate", messageObject102222222222.messageOwner.date);
                intent22222222222.putExtra("currentAccount", this.currentAccount);
                builder3222222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent22222222222, 134217728));
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
            if (!z8) {
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
            notificationsSettingsFacade = this.dialogsNotificationsFacade;
            NotificationCompat.Builder builder32222222 = builder;
            boolean z232222222 = z7 ? 1 : 0;
            TLRPC$User tLRPC$User22222222 = tLRPC$User;
            SharedPreferences sharedPreferences52222222 = sharedPreferences3;
            j6 = j5;
            MessageObject messageObject92222222 = messageObject3;
            if (!notificationsSettingsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, i, false)) {
            }
            if (j2 != j7) {
            }
            i4 = i;
            if (i7 == 4) {
            }
            if (!TextUtils.isEmpty(str6)) {
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
            i10 = i7;
            i11 = i3;
            if (z14) {
            }
            if (z9) {
            }
            Intent intent522222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
            StringBuilder sb4222222222 = new StringBuilder();
            sb4222222222.append("com.tmessages.openchat");
            String str13222222222 = str6;
            sb4222222222.append(Math.random());
            sb4222222222.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
            intent522222222.setAction(sb4222222222.toString());
            intent522222222.setFlags(ConnectionsManager.FileTypeFile);
            if (DialogObject.isEncryptedDialog(j6)) {
            }
            i12 = i10;
            j8 = j6;
            tLRPC$FileLocation = null;
            boolean z2422222222222 = z16;
            intent522222222.putExtra("currentAccount", this.currentAccount);
            long j1622222222222 = j8;
            MessageObject messageObject1022222222222 = messageObject4;
            int i2622222222222 = i5;
            builder32222222.setContentTitle(charSequence).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent522222222, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject1022222222222.messageOwner.date * 1000).setColor(-15618822);
            builder32222222.setCategory("msg");
            if (tLRPC$Chat3 == null) {
            }
            Intent intent222222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
            intent222222222222.putExtra("messageDate", messageObject1022222222222.messageOwner.date);
            intent222222222222.putExtra("currentAccount", this.currentAccount);
            builder32222222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent222222222222, 134217728));
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

    /* JADX WARN: Code restructure failed: missing block: B:483:0x02c4, code lost:
        if (r1.local_id != 0) goto L484;
     */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0508  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0556  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0560 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:140:0x05c0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:144:0x05d4  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x05e9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:160:0x0618  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x0667  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x06a2  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x0723  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x0806  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x0912  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x0933  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x0977  */
    /* JADX WARN: Removed duplicated region for block: B:307:0x075d  */
    /* JADX WARN: Removed duplicated region for block: B:324:0x09e9  */
    /* JADX WARN: Removed duplicated region for block: B:326:0x0a07  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x0a25  */
    /* JADX WARN: Removed duplicated region for block: B:332:0x0a7d  */
    /* JADX WARN: Removed duplicated region for block: B:336:0x0adc  */
    /* JADX WARN: Removed duplicated region for block: B:339:0x0bb1  */
    /* JADX WARN: Removed duplicated region for block: B:341:0x0bbc  */
    /* JADX WARN: Removed duplicated region for block: B:343:0x0bc1  */
    /* JADX WARN: Removed duplicated region for block: B:346:0x0bcb  */
    /* JADX WARN: Removed duplicated region for block: B:351:0x0bdf  */
    /* JADX WARN: Removed duplicated region for block: B:353:0x0be4  */
    /* JADX WARN: Removed duplicated region for block: B:356:0x0bf0  */
    /* JADX WARN: Removed duplicated region for block: B:362:0x0bff  */
    /* JADX WARN: Removed duplicated region for block: B:379:0x0c86 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:387:0x0cb7  */
    /* JADX WARN: Removed duplicated region for block: B:393:0x0afe  */
    /* JADX WARN: Removed duplicated region for block: B:395:0x0ab6  */
    /* JADX WARN: Removed duplicated region for block: B:399:0x09f3  */
    /* JADX WARN: Removed duplicated region for block: B:422:0x04b1  */
    /* JADX WARN: Removed duplicated region for block: B:424:0x04c2  */
    /* JADX WARN: Removed duplicated region for block: B:425:0x0448  */
    /* JADX WARN: Removed duplicated region for block: B:439:0x0419  */
    /* JADX WARN: Removed duplicated region for block: B:441:0x03bf  */
    /* JADX WARN: Removed duplicated region for block: B:442:0x0234  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x022b  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x03a5  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x03c3  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0422  */
    @SuppressLint({"InlinedApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showExtraNotifications(NotificationCompat.Builder builder, String str, long j, int i, String str2, long[] jArr, int i2, Uri uri, int i3, boolean z, boolean z2, boolean z3, int i4) {
        ArrayList arrayList;
        NotificationsController notificationsController;
        LongSparseArray longSparseArray;
        int i5;
        boolean z4;
        ArrayList arrayList2;
        Integer num;
        MessageObject messageObject;
        int i6;
        int i7;
        TLRPC$User tLRPC$User;
        String string;
        TLRPC$User tLRPC$User2;
        TLRPC$FileLocation tLRPC$FileLocation;
        TLRPC$Chat tLRPC$Chat;
        boolean z5;
        boolean z6;
        boolean z7;
        long j2;
        LongSparseArray longSparseArray2;
        LongSparseArray longSparseArray3;
        NotificationsController notificationsController2;
        Notification notification;
        ArrayList arrayList3;
        int i8;
        ArrayList arrayList4;
        boolean z8;
        LongSparseArray longSparseArray4;
        int i9;
        LongSparseArray longSparseArray5;
        String str3;
        TLRPC$User tLRPC$User3;
        Bitmap bitmap;
        File file;
        String str4;
        String str5;
        String str6;
        TLRPC$Chat tLRPC$Chat2;
        int i10;
        Bitmap bitmap2;
        Integer num2;
        String str7;
        String formatString;
        NotificationCompat.Action build;
        Integer num3;
        Person person;
        NotificationCompat.Action action;
        String str8;
        String str9;
        NotificationCompat.MessagingStyle messagingStyle;
        NotificationCompat.MessagingStyle messagingStyle2;
        int i11;
        int size;
        NotificationCompat.Action action2;
        int i12;
        String str10;
        TLRPC$User tLRPC$User4;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList5;
        int size2;
        int i13;
        int i14;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList6;
        String str11;
        int i15;
        int i16;
        String str12;
        String str13;
        StringBuilder sb;
        long j3;
        Person person2;
        String str14;
        String str15;
        long j4;
        String[] strArr;
        File file2;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        TLRPC$FileLocation tLRPC$FileLocation2;
        NotificationCompat.MessagingStyle messagingStyle3;
        String str16;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup;
        boolean z9;
        List<NotificationCompat.MessagingStyle.Message> messages;
        Uri uri2;
        final Uri uriForFile;
        TLRPC$User user;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto2;
        TLRPC$FileLocation tLRPC$FileLocation3;
        Bitmap bitmap3;
        Bitmap bitmap4;
        String string2;
        TLRPC$FileLocation tLRPC$FileLocation4;
        TLRPC$TL_forumTopic findTopic;
        TLRPC$User tLRPC$User5;
        String str17;
        TLRPC$FileLocation tLRPC$FileLocation5;
        NotificationsController notificationsController3 = this;
        int i17 = Build.VERSION.SDK_INT;
        if (i17 >= 26) {
            builder.setChannelId(validateChannelId(j, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
        }
        Notification build2 = builder.build();
        if (i17 < 18) {
            notificationManager.notify(notificationsController3.notificationId, build2);
            if (!BuildVars.LOGS_ENABLED) {
                return;
            }
            FileLog.d("show summary notification by SDK check");
            return;
        }
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        ArrayList arrayList7 = new ArrayList();
        LongSparseArray longSparseArray6 = new LongSparseArray();
        for (int i18 = 0; i18 < notificationsController3.pushMessages.size(); i18++) {
            MessageObject messageObject2 = notificationsController3.pushMessages.get(i18);
            long dialogId = messageObject2.getDialogId();
            int topicId = MessageObject.getTopicId(messageObject2.messageOwner);
            if (messageObject2.messageOwner.date > notificationsSettings.getInt("dismissDate" + dialogId, 0)) {
                ArrayList arrayList8 = (ArrayList) longSparseArray6.get(dialogId);
                if (arrayList8 == null) {
                    arrayList8 = new ArrayList();
                    longSparseArray6.put(dialogId, arrayList8);
                    arrayList7.add(new DialogKey(dialogId, topicId));
                }
                arrayList8.add(messageObject2);
            }
        }
        LongSparseArray longSparseArray7 = new LongSparseArray();
        for (int i19 = 0; i19 < notificationsController3.wearNotificationsIds.size(); i19++) {
            longSparseArray7.put(notificationsController3.wearNotificationsIds.keyAt(i19), notificationsController3.wearNotificationsIds.valueAt(i19));
        }
        notificationsController3.wearNotificationsIds.clear();
        ArrayList arrayList9 = new ArrayList();
        int i20 = Build.VERSION.SDK_INT;
        boolean z10 = i20 <= 27 || arrayList7.size() > 1;
        if (z10 && i20 >= 26) {
            checkOtherNotificationsChannel();
        }
        long clientUserId = getUserConfig().getClientUserId();
        boolean z11 = AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter;
        int i21 = 7;
        LongSparseArray longSparseArray8 = new LongSparseArray();
        int size3 = arrayList7.size();
        int i22 = 0;
        while (i22 < size3 && arrayList9.size() < i21) {
            DialogKey dialogKey = (DialogKey) arrayList7.get(i22);
            ArrayList arrayList10 = arrayList7;
            Notification notification2 = build2;
            long j5 = dialogKey.dialogId;
            int i23 = dialogKey.topicId;
            ArrayList arrayList11 = (ArrayList) longSparseArray6.get(j5);
            int i24 = i22;
            int id = ((MessageObject) arrayList11.get(0)).getId();
            Integer num4 = (Integer) longSparseArray7.get(j5);
            ArrayList arrayList12 = arrayList9;
            if (num4 == null) {
                i5 = id;
                z4 = z10;
                num4 = Integer.valueOf(((int) j5) + ((int) (j5 >> 32)));
            } else {
                i5 = id;
                z4 = z10;
                longSparseArray7.remove(j5);
            }
            Integer num5 = num4;
            MessageObject messageObject3 = (MessageObject) arrayList11.get(0);
            LongSparseArray longSparseArray9 = longSparseArray7;
            int i25 = 0;
            for (int i26 = 0; i26 < arrayList11.size(); i26++) {
                if (i25 < ((MessageObject) arrayList11.get(i26)).messageOwner.date) {
                    i25 = ((MessageObject) arrayList11.get(i26)).messageOwner.date;
                }
            }
            if (!DialogObject.isEncryptedDialog(j5)) {
                boolean z12 = j5 != 777000;
                if (DialogObject.isUserDialog(j5)) {
                    z5 = z12;
                    TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(j5));
                    if (user2 == null) {
                        if (messageObject3.isFcmMessage()) {
                            str17 = messageObject3.localName;
                            tLRPC$User5 = user2;
                            arrayList2 = arrayList11;
                        } else {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.w("not found user to show dialog notification " + j5);
                            }
                            j2 = clientUserId;
                            longSparseArray2 = longSparseArray8;
                            longSparseArray3 = longSparseArray6;
                            notificationsController2 = notificationsController3;
                            notification = notification2;
                            arrayList3 = arrayList10;
                            i8 = i24;
                            arrayList4 = arrayList12;
                            z8 = z4;
                            longSparseArray4 = longSparseArray9;
                            i9 = size3;
                        }
                    } else {
                        String userName = UserObject.getUserName(user2);
                        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto3 = user2.photo;
                        if (tLRPC$UserProfilePhoto3 == null || (tLRPC$FileLocation5 = tLRPC$UserProfilePhoto3.photo_small) == null) {
                            tLRPC$User5 = user2;
                            arrayList2 = arrayList11;
                        } else {
                            tLRPC$User5 = user2;
                            arrayList2 = arrayList11;
                            if (tLRPC$FileLocation5.volume_id != 0 && tLRPC$FileLocation5.local_id != 0) {
                                tLRPC$FileLocation = tLRPC$FileLocation5;
                                str17 = userName;
                                if (!UserObject.isReplyUser(j5)) {
                                    str17 = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                                } else if (j5 == clientUserId) {
                                    str17 = LocaleController.getString("MessageScheduledReminderNotification", R.string.MessageScheduledReminderNotification);
                                }
                                messageObject = messageObject3;
                                i6 = i25;
                                string = str17;
                                i7 = size3;
                                tLRPC$User2 = tLRPC$User5;
                                tLRPC$Chat = null;
                                z6 = false;
                                z7 = false;
                                num = num5;
                            }
                        }
                        str17 = userName;
                    }
                    tLRPC$FileLocation = null;
                    if (!UserObject.isReplyUser(j5)) {
                    }
                    messageObject = messageObject3;
                    i6 = i25;
                    string = str17;
                    i7 = size3;
                    tLRPC$User2 = tLRPC$User5;
                    tLRPC$Chat = null;
                    z6 = false;
                    z7 = false;
                    num = num5;
                } else {
                    z5 = z12;
                    arrayList2 = arrayList11;
                    i7 = size3;
                    TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j5));
                    if (chat == null) {
                        if (messageObject3.isFcmMessage()) {
                            z6 = messageObject3.isSupergroup();
                            messageObject = messageObject3;
                            i6 = i25;
                            string = messageObject3.localName;
                            z7 = messageObject3.localChannel;
                            tLRPC$User2 = null;
                            tLRPC$Chat = chat;
                            num = num5;
                            tLRPC$FileLocation = null;
                        } else {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.w("not found chat to show dialog notification " + j5);
                            }
                            j2 = clientUserId;
                            longSparseArray2 = longSparseArray8;
                            longSparseArray3 = longSparseArray6;
                            notificationsController2 = notificationsController3;
                            notification = notification2;
                            arrayList3 = arrayList10;
                            i8 = i24;
                            arrayList4 = arrayList12;
                            z8 = z4;
                            longSparseArray4 = longSparseArray9;
                            i9 = i7;
                        }
                    } else {
                        boolean z13 = chat.megagroup;
                        boolean z14 = ChatObject.isChannel(chat) && !chat.megagroup;
                        String str18 = chat.title;
                        z6 = z13;
                        TLRPC$ChatPhoto tLRPC$ChatPhoto = chat.photo;
                        if (tLRPC$ChatPhoto == null || (tLRPC$FileLocation4 = tLRPC$ChatPhoto.photo_small) == null) {
                            i6 = i25;
                            z7 = z14;
                        } else {
                            i6 = i25;
                            z7 = z14;
                            if (tLRPC$FileLocation4.volume_id != 0) {
                            }
                        }
                        tLRPC$FileLocation4 = null;
                        if (i23 != 0) {
                            num = num5;
                            messageObject = messageObject3;
                            String str19 = getMessagesController().getTopicsController().findTopic(chat.id, i23) != null ? findTopic.title + " in " + str18 : str18;
                            tLRPC$Chat = chat;
                            tLRPC$FileLocation = tLRPC$FileLocation4;
                            string = str19;
                        } else {
                            num = num5;
                            messageObject = messageObject3;
                            tLRPC$Chat = chat;
                            tLRPC$FileLocation = tLRPC$FileLocation4;
                            string = str18;
                        }
                        tLRPC$User2 = null;
                    }
                }
                if (!z11) {
                    if (DialogObject.isChatDialog(j5)) {
                        string2 = LocaleController.getString("NotificationHiddenChatName", R.string.NotificationHiddenChatName);
                    } else {
                        string2 = LocaleController.getString("NotificationHiddenName", R.string.NotificationHiddenName);
                    }
                    string = string2;
                    longSparseArray5 = longSparseArray6;
                    tLRPC$FileLocation = null;
                    z5 = false;
                } else {
                    longSparseArray5 = longSparseArray6;
                }
                if (tLRPC$FileLocation == null) {
                    tLRPC$User3 = tLRPC$User2;
                    file = getFileLoader().getPathToAttach(tLRPC$FileLocation, true);
                    if (Build.VERSION.SDK_INT < 28) {
                        str3 = "NotificationHiddenName";
                        bitmap3 = null;
                        BitmapDrawable imageFromMemory = ImageLoader.getInstance().getImageFromMemory(tLRPC$FileLocation, null, "50_50");
                        if (imageFromMemory != null) {
                            bitmap4 = imageFromMemory.getBitmap();
                        } else {
                            try {
                                if (file.exists()) {
                                    float dp = 160.0f / AndroidUtilities.dp(50.0f);
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inSampleSize = dp < 1.0f ? 1 : (int) dp;
                                    bitmap4 = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                                } else {
                                    bitmap4 = null;
                                }
                            } catch (Throwable unused) {
                            }
                        }
                        bitmap = bitmap4;
                    } else {
                        str3 = "NotificationHiddenName";
                        bitmap3 = null;
                    }
                    bitmap = bitmap3;
                } else {
                    str3 = "NotificationHiddenName";
                    tLRPC$User3 = tLRPC$User2;
                    bitmap = null;
                    file = null;
                }
                if (tLRPC$Chat == null) {
                    Person.Builder name = new Person.Builder().setName(string);
                    if (file != null && file.exists() && Build.VERSION.SDK_INT >= 28) {
                        notificationsController3.loadRoundAvatar(file, name);
                    }
                    str4 = "NotificationHiddenChatName";
                    longSparseArray8.put(-tLRPC$Chat.id, name.build());
                } else {
                    str4 = "NotificationHiddenChatName";
                }
                File file3 = file;
                str5 = "currentAccount";
                if ((z7 || z6) && z5 && !SharedConfig.isWaitingForPasscodeEnter && clientUserId != j5 && !UserObject.isReplyUser(j5)) {
                    str6 = str4;
                    tLRPC$Chat2 = tLRPC$Chat;
                    Intent intent = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                    intent.putExtra("dialog_id", j5);
                    i10 = i5;
                    intent.putExtra("max_id", i10);
                    intent.putExtra("topic_id", i23);
                    intent.putExtra(str5, notificationsController3.currentAccount);
                    bitmap2 = bitmap;
                    num2 = num;
                    PendingIntent broadcast = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent, 134217728);
                    RemoteInput build3 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                    if (!DialogObject.isChatDialog(j5)) {
                        str7 = "max_id";
                        formatString = LocaleController.formatString("ReplyToGroup", R.string.ReplyToGroup, string);
                    } else {
                        str7 = "max_id";
                        formatString = LocaleController.formatString("ReplyToUser", R.string.ReplyToUser, string);
                    }
                    build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build3).setShowsUserInterface(false).build();
                } else {
                    str7 = "max_id";
                    tLRPC$Chat2 = tLRPC$Chat;
                    num2 = num;
                    str6 = str4;
                    i10 = i5;
                    bitmap2 = bitmap;
                    build = null;
                }
                num3 = notificationsController3.pushDialogs.get(j5);
                if (num3 == null) {
                    num3 = 0;
                }
                int max = Math.max(num3.intValue(), arrayList2.size());
                String format = (max > 1 || Build.VERSION.SDK_INT >= 28) ? string : String.format("%1$s (%2$d)", string, Integer.valueOf(max));
                person = (Person) longSparseArray8.get(clientUserId);
                if (Build.VERSION.SDK_INT >= 28 && person == null) {
                    user = getMessagesController().getUser(Long.valueOf(clientUserId));
                    if (user == null) {
                        user = getUserConfig().getCurrentUser();
                    }
                    if (user != null) {
                        try {
                            tLRPC$UserProfilePhoto2 = user.photo;
                        } catch (Throwable th) {
                            th = th;
                            action = build;
                            str8 = "dialog_id";
                        }
                        if (tLRPC$UserProfilePhoto2 != null && (tLRPC$FileLocation3 = tLRPC$UserProfilePhoto2.photo_small) != null) {
                            action = build;
                            str8 = "dialog_id";
                            try {
                                if (tLRPC$FileLocation3.volume_id != 0 && tLRPC$FileLocation3.local_id != 0) {
                                    Person.Builder name2 = new Person.Builder().setName(LocaleController.getString("FromYou", R.string.FromYou));
                                    notificationsController3.loadRoundAvatar(getFileLoader().getPathToAttach(user.photo.photo_small, true), name2);
                                    Person build4 = name2.build();
                                    try {
                                        longSparseArray8.put(clientUserId, build4);
                                        person = build4;
                                    } catch (Throwable th2) {
                                        th = th2;
                                        person = build4;
                                        FileLog.e(th);
                                        boolean z15 = !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest);
                                        str9 = "";
                                        if (person == null) {
                                        }
                                        messagingStyle = new NotificationCompat.MessagingStyle(str9);
                                        messagingStyle2 = messagingStyle;
                                        i11 = Build.VERSION.SDK_INT;
                                        if (i11 >= 28) {
                                        }
                                        messagingStyle2.setConversationTitle(format);
                                        messagingStyle2.setGroupConversation(i11 >= 28 || (!z7 && DialogObject.isChatDialog(j5)) || UserObject.isReplyUser(j5));
                                        StringBuilder sb2 = new StringBuilder();
                                        String[] strArr2 = new String[1];
                                        int i27 = i10;
                                        boolean[] zArr = new boolean[1];
                                        size = arrayList2.size() - 1;
                                        int i28 = 0;
                                        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList13 = null;
                                        while (size >= 0) {
                                        }
                                        String str20 = string;
                                        long j6 = clientUserId;
                                        NotificationCompat.MessagingStyle messagingStyle4 = messagingStyle2;
                                        StringBuilder sb3 = sb2;
                                        String str21 = str5;
                                        Intent intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                        intent2.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                        intent2.setFlags(ConnectionsManager.FileTypeFile);
                                        intent2.addCategory("android.intent.category.LAUNCHER");
                                        if (!DialogObject.isEncryptedDialog(j5)) {
                                        }
                                        if (i23 != 0) {
                                        }
                                        String str22 = str21;
                                        intent2.putExtra(str22, notificationsController3.currentAccount);
                                        PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1073741824);
                                        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
                                        action2 = action;
                                        if (action != null) {
                                        }
                                        Intent intent3 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                        intent3.addFlags(32);
                                        intent3.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                        intent3.putExtra(str8, j5);
                                        intent3.putExtra(str7, i27);
                                        intent3.putExtra(str22, notificationsController3.currentAccount);
                                        NotificationCompat.Action build5 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent3, 134217728)).setSemanticAction(2).setShowsUserInterface(false).build();
                                        if (DialogObject.isEncryptedDialog(j5)) {
                                        }
                                        if (str10 == null) {
                                        }
                                        wearableExtender.setBridgeTag("tgaccount" + j6);
                                        ArrayList arrayList14 = arrayList2;
                                        long j7 = ((long) ((MessageObject) arrayList14.get(0)).messageOwner.date) * 1000;
                                        LongSparseArray longSparseArray10 = longSparseArray8;
                                        NotificationCompat.Builder category = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str20).setSmallIcon(R.drawable.notification).setContentText(sb3.toString()).setAutoCancel(true).setNumber(arrayList14.size()).setColor(-15618822).setGroupSummary(false).setWhen(j7).setShowWhen(true).setStyle(messagingStyle4).setContentIntent(activity).extend(wearableExtender).setSortKey(String.valueOf(Long.MAX_VALUE - j7)).setCategory("msg");
                                        Intent intent4 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                        intent4.putExtra("messageDate", i6);
                                        intent4.putExtra("dialogId", j5);
                                        intent4.putExtra(str22, notificationsController3.currentAccount);
                                        category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent4, 134217728));
                                        if (z4) {
                                        }
                                        if (action2 != null) {
                                        }
                                        if (!z11) {
                                        }
                                        if (arrayList10.size() != 1) {
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
                                        boolean z16 = z4;
                                        if (Build.VERSION.SDK_INT >= 26) {
                                        }
                                        j2 = j6;
                                        i8 = i24;
                                        z8 = z16;
                                        longSparseArray2 = longSparseArray10;
                                        longSparseArray4 = longSparseArray9;
                                        i9 = i7;
                                        longSparseArray3 = longSparseArray5;
                                        arrayList3 = arrayList10;
                                        notification = notification2;
                                        arrayList4 = arrayList12;
                                        arrayList4.add(new 1NotificationHolder(num2.intValue(), j5, i12, str20, tLRPC$User4, tLRPC$Chat2, category, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                                        notificationsController2 = this;
                                        notificationsController2.wearNotificationsIds.put(j5, num2);
                                        i22 = i8 + 1;
                                        arrayList9 = arrayList4;
                                        clientUserId = j2;
                                        z10 = z8;
                                        longSparseArray7 = longSparseArray4;
                                        size3 = i9;
                                        longSparseArray6 = longSparseArray3;
                                        arrayList7 = arrayList3;
                                        longSparseArray8 = longSparseArray2;
                                        build2 = notification;
                                        i21 = 7;
                                        notificationsController3 = notificationsController2;
                                    }
                                }
                            } catch (Throwable th3) {
                                th = th3;
                            }
                            boolean z152 = !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest);
                            str9 = "";
                            if (person == null && z152) {
                                messagingStyle = new NotificationCompat.MessagingStyle(person);
                            } else {
                                messagingStyle = new NotificationCompat.MessagingStyle(str9);
                            }
                            messagingStyle2 = messagingStyle;
                            i11 = Build.VERSION.SDK_INT;
                            if (i11 >= 28 || ((DialogObject.isChatDialog(j5) && !z7) || UserObject.isReplyUser(j5))) {
                                messagingStyle2.setConversationTitle(format);
                            }
                            messagingStyle2.setGroupConversation(i11 >= 28 || (!z7 && DialogObject.isChatDialog(j5)) || UserObject.isReplyUser(j5));
                            StringBuilder sb22 = new StringBuilder();
                            String[] strArr22 = new String[1];
                            int i272 = i10;
                            boolean[] zArr2 = new boolean[1];
                            size = arrayList2.size() - 1;
                            int i282 = 0;
                            ArrayList<TLRPC$TL_keyboardButtonRow> arrayList132 = null;
                            while (size >= 0) {
                                String str23 = str5;
                                MessageObject messageObject4 = (MessageObject) arrayList2.get(size);
                                if (i23 != MessageObject.getTopicId(messageObject4.messageOwner)) {
                                    str12 = string;
                                    j4 = clientUserId;
                                    sb = sb22;
                                    i16 = size;
                                } else {
                                    String shortStringForMessage = notificationsController3.getShortStringForMessage(messageObject4, strArr22, zArr2);
                                    i16 = size;
                                    if (j5 == clientUserId) {
                                        strArr22[0] = string;
                                    } else if (DialogObject.isChatDialog(j5)) {
                                        str12 = string;
                                        if (messageObject4.messageOwner.from_scheduled) {
                                            strArr22[0] = LocaleController.getString("NotificationMessageScheduledName", R.string.NotificationMessageScheduledName);
                                        }
                                        if (shortStringForMessage != null) {
                                            if (BuildVars.LOGS_ENABLED) {
                                                StringBuilder sb4 = new StringBuilder();
                                                sb4.append("message text is null for ");
                                                sb4.append(messageObject4.getId());
                                                sb4.append(" did = ");
                                                String str24 = str9;
                                                sb4.append(messageObject4.getDialogId());
                                                FileLog.w(sb4.toString());
                                                j4 = clientUserId;
                                                sb = sb22;
                                                strArr = strArr22;
                                                messagingStyle3 = messagingStyle2;
                                                str16 = str24;
                                            } else {
                                                j4 = clientUserId;
                                                sb = sb22;
                                            }
                                        } else {
                                            String str25 = str9;
                                            NotificationCompat.MessagingStyle messagingStyle5 = messagingStyle2;
                                            if (sb22.length() > 0) {
                                                sb22.append("\n\n");
                                            }
                                            if (j5 != clientUserId && messageObject4.messageOwner.from_scheduled && DialogObject.isUserDialog(j5)) {
                                                str13 = str25;
                                                shortStringForMessage = String.format("%1$s: %2$s", LocaleController.getString("NotificationMessageScheduledName", R.string.NotificationMessageScheduledName), shortStringForMessage);
                                                sb22.append(shortStringForMessage);
                                            } else {
                                                str13 = str25;
                                                if (strArr22[0] != null) {
                                                    sb22.append(String.format("%1$s: %2$s", strArr22[0], shortStringForMessage));
                                                } else {
                                                    sb22.append(shortStringForMessage);
                                                }
                                            }
                                            String str26 = shortStringForMessage;
                                            if (!DialogObject.isUserDialog(j5)) {
                                                if (z7) {
                                                    j3 = -j5;
                                                } else if (DialogObject.isChatDialog(j5)) {
                                                    j3 = messageObject4.getSenderId();
                                                }
                                                sb = sb22;
                                                person2 = (Person) longSparseArray8.get((i23 << 16) + j3);
                                                if (strArr22[0] != null) {
                                                    if (z11) {
                                                        if (DialogObject.isChatDialog(j5)) {
                                                            if (z7) {
                                                                if (Build.VERSION.SDK_INT > 27) {
                                                                    str15 = LocaleController.getString(str6, R.string.NotificationHiddenChatName);
                                                                }
                                                            } else {
                                                                str15 = LocaleController.getString("NotificationHiddenChatUserName", R.string.NotificationHiddenChatUserName);
                                                            }
                                                            str14 = str3;
                                                        } else if (Build.VERSION.SDK_INT > 27) {
                                                            str14 = str3;
                                                            str15 = LocaleController.getString(str14, R.string.NotificationHiddenName);
                                                        }
                                                    }
                                                    str14 = str3;
                                                    str15 = str13;
                                                } else {
                                                    str14 = str3;
                                                    str15 = strArr22[0];
                                                }
                                                str3 = str14;
                                                if (person2 != null || !TextUtils.equals(person2.getName(), str15)) {
                                                    Person.Builder name3 = new Person.Builder().setName(str15);
                                                    if (zArr2[0] || DialogObject.isEncryptedDialog(j5) || Build.VERSION.SDK_INT < 28) {
                                                        j4 = clientUserId;
                                                        strArr = strArr22;
                                                    } else {
                                                        if (DialogObject.isUserDialog(j5) || z7) {
                                                            j4 = clientUserId;
                                                            strArr = strArr22;
                                                            file2 = file3;
                                                        } else {
                                                            long senderId = messageObject4.getSenderId();
                                                            strArr = strArr22;
                                                            j4 = clientUserId;
                                                            TLRPC$User user3 = getMessagesController().getUser(Long.valueOf(senderId));
                                                            if (user3 == null && (user3 = getMessagesStorage().getUserSync(senderId)) != null) {
                                                                getMessagesController().putUser(user3, true);
                                                            }
                                                            file2 = (user3 == null || (tLRPC$UserProfilePhoto = user3.photo) == null || (tLRPC$FileLocation2 = tLRPC$UserProfilePhoto.photo_small) == null || tLRPC$FileLocation2.volume_id == 0 || tLRPC$FileLocation2.local_id == 0) ? null : getFileLoader().getPathToAttach(user3.photo.photo_small, true);
                                                        }
                                                        notificationsController3.loadRoundAvatar(file2, name3);
                                                    }
                                                    person2 = name3.build();
                                                    longSparseArray8.put(j3, person2);
                                                } else {
                                                    j4 = clientUserId;
                                                    strArr = strArr22;
                                                }
                                                Person person3 = person2;
                                                if (DialogObject.isEncryptedDialog(j5)) {
                                                    if (!zArr2[0] || Build.VERSION.SDK_INT < 28 || ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).isLowRamDevice() || z11 || messageObject4.isSecretMedia() || (messageObject4.type != 1 && !messageObject4.isSticker())) {
                                                        messagingStyle3 = messagingStyle5;
                                                        str16 = str13;
                                                    } else {
                                                        File pathToMessage = getFileLoader().getPathToMessage(messageObject4.messageOwner);
                                                        NotificationCompat.MessagingStyle.Message message = new NotificationCompat.MessagingStyle.Message(str26, messageObject4.messageOwner.date * 1000, person3);
                                                        String str27 = messageObject4.isSticker() ? "image/webp" : "image/jpeg";
                                                        if (pathToMessage.exists()) {
                                                            try {
                                                                uriForFile = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", pathToMessage);
                                                                str16 = str13;
                                                            } catch (Exception e) {
                                                                FileLog.e(e);
                                                            }
                                                        } else {
                                                            if (getFileLoader().isLoadingFile(pathToMessage.getName())) {
                                                                Uri.Builder appendPath = new Uri.Builder().scheme("content").authority(NotificationImageProvider.getAuthority()).appendPath("msg_media_raw");
                                                                StringBuilder sb5 = new StringBuilder();
                                                                sb5.append(notificationsController3.currentAccount);
                                                                str16 = str13;
                                                                sb5.append(str16);
                                                                uriForFile = appendPath.appendPath(sb5.toString()).appendPath(pathToMessage.getName()).appendQueryParameter("final_path", pathToMessage.getAbsolutePath()).build();
                                                            }
                                                            str16 = str13;
                                                            uriForFile = null;
                                                        }
                                                        if (uriForFile != null) {
                                                            message.setData(str27, uriForFile);
                                                            messagingStyle3 = messagingStyle5;
                                                            messagingStyle3.addMessage(message);
                                                            ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile, 1);
                                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda4
                                                                @Override // java.lang.Runnable
                                                                public final void run() {
                                                                    NotificationsController.lambda$showExtraNotifications$34(uriForFile);
                                                                }
                                                            }, 20000L);
                                                            if (!TextUtils.isEmpty(messageObject4.caption)) {
                                                                messagingStyle3.addMessage(messageObject4.caption, messageObject4.messageOwner.date * 1000, person3);
                                                            }
                                                            z9 = true;
                                                            if (!z9) {
                                                                messagingStyle3.addMessage(str26, messageObject4.messageOwner.date * 1000, person3);
                                                            }
                                                            if (zArr2[0] && !z11 && messageObject4.isVoice()) {
                                                                messages = messagingStyle3.getMessages();
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
                                                            messagingStyle3 = messagingStyle5;
                                                        }
                                                    }
                                                    z9 = false;
                                                    if (!z9) {
                                                    }
                                                    if (zArr2[0]) {
                                                        messages = messagingStyle3.getMessages();
                                                        if (!messages.isEmpty()) {
                                                        }
                                                    }
                                                } else {
                                                    messagingStyle3 = messagingStyle5;
                                                    str16 = str13;
                                                    messagingStyle3.addMessage(str26, messageObject4.messageOwner.date * 1000, person3);
                                                }
                                                if (j5 == 777000 && (tLRPC$ReplyMarkup = messageObject4.messageOwner.reply_markup) != null) {
                                                    arrayList132 = tLRPC$ReplyMarkup.rows;
                                                    i282 = messageObject4.getId();
                                                }
                                            }
                                            sb = sb22;
                                            j3 = j5;
                                            person2 = (Person) longSparseArray8.get((i23 << 16) + j3);
                                            if (strArr22[0] != null) {
                                            }
                                            str3 = str14;
                                            if (person2 != null) {
                                            }
                                            Person.Builder name32 = new Person.Builder().setName(str15);
                                            if (zArr2[0]) {
                                            }
                                            j4 = clientUserId;
                                            strArr = strArr22;
                                            person2 = name32.build();
                                            longSparseArray8.put(j3, person2);
                                            Person person32 = person2;
                                            if (DialogObject.isEncryptedDialog(j5)) {
                                            }
                                            if (j5 == 777000) {
                                                arrayList132 = tLRPC$ReplyMarkup.rows;
                                                i282 = messageObject4.getId();
                                            }
                                        }
                                        size = i16 - 1;
                                        messagingStyle2 = messagingStyle3;
                                        str9 = str16;
                                        str5 = str23;
                                        string = str12;
                                        sb22 = sb;
                                        strArr22 = strArr;
                                        clientUserId = j4;
                                    }
                                    str12 = string;
                                    if (shortStringForMessage != null) {
                                    }
                                    size = i16 - 1;
                                    messagingStyle2 = messagingStyle3;
                                    str9 = str16;
                                    str5 = str23;
                                    string = str12;
                                    sb22 = sb;
                                    strArr22 = strArr;
                                    clientUserId = j4;
                                }
                                strArr = strArr22;
                                str16 = str9;
                                messagingStyle3 = messagingStyle2;
                                size = i16 - 1;
                                messagingStyle2 = messagingStyle3;
                                str9 = str16;
                                str5 = str23;
                                string = str12;
                                sb22 = sb;
                                strArr22 = strArr;
                                clientUserId = j4;
                            }
                            String str202 = string;
                            long j62 = clientUserId;
                            NotificationCompat.MessagingStyle messagingStyle42 = messagingStyle2;
                            StringBuilder sb32 = sb22;
                            String str212 = str5;
                            Intent intent22 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                            intent22.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                            intent22.setFlags(ConnectionsManager.FileTypeFile);
                            intent22.addCategory("android.intent.category.LAUNCHER");
                            if (!DialogObject.isEncryptedDialog(j5)) {
                                intent22.putExtra("encId", DialogObject.getEncryptedChatId(j5));
                            } else if (DialogObject.isUserDialog(j5)) {
                                intent22.putExtra("userId", j5);
                            } else {
                                intent22.putExtra("chatId", -j5);
                            }
                            if (i23 != 0) {
                                intent22.putExtra("topicId", i23);
                            }
                            String str222 = str212;
                            intent22.putExtra(str222, notificationsController3.currentAccount);
                            PendingIntent activity2 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22, 1073741824);
                            NotificationCompat.WearableExtender wearableExtender2 = new NotificationCompat.WearableExtender();
                            action2 = action;
                            if (action != null) {
                                wearableExtender2.addAction(action2);
                            }
                            Intent intent32 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                            intent32.addFlags(32);
                            intent32.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                            intent32.putExtra(str8, j5);
                            intent32.putExtra(str7, i272);
                            intent32.putExtra(str222, notificationsController3.currentAccount);
                            NotificationCompat.Action build52 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent32, 134217728)).setSemanticAction(2).setShowsUserInterface(false).build();
                            if (DialogObject.isEncryptedDialog(j5)) {
                                if (DialogObject.isUserDialog(j5)) {
                                    str10 = "tguser" + j5 + "_" + i272;
                                    i12 = i23;
                                } else {
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append("tgchat");
                                    i12 = i23;
                                    sb6.append(-j5);
                                    sb6.append("_");
                                    sb6.append(i272);
                                    str10 = sb6.toString();
                                }
                            } else {
                                i12 = i23;
                                str10 = j5 != globalSecretChatId ? "tgenc" + DialogObject.getEncryptedChatId(j5) + "_" + i272 : null;
                            }
                            if (str10 == null) {
                                wearableExtender2.setDismissalId(str10);
                                NotificationCompat.WearableExtender wearableExtender3 = new NotificationCompat.WearableExtender();
                                wearableExtender3.setDismissalId("summary_" + str10);
                                builder.extend(wearableExtender3);
                            }
                            wearableExtender2.setBridgeTag("tgaccount" + j62);
                            ArrayList arrayList142 = arrayList2;
                            long j72 = ((long) ((MessageObject) arrayList142.get(0)).messageOwner.date) * 1000;
                            LongSparseArray longSparseArray102 = longSparseArray8;
                            NotificationCompat.Builder category2 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str202).setSmallIcon(R.drawable.notification).setContentText(sb32.toString()).setAutoCancel(true).setNumber(arrayList142.size()).setColor(-15618822).setGroupSummary(false).setWhen(j72).setShowWhen(true).setStyle(messagingStyle42).setContentIntent(activity2).extend(wearableExtender2).setSortKey(String.valueOf(Long.MAX_VALUE - j72)).setCategory("msg");
                            Intent intent42 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                            intent42.putExtra("messageDate", i6);
                            intent42.putExtra("dialogId", j5);
                            intent42.putExtra(str222, notificationsController3.currentAccount);
                            category2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent42, 134217728));
                            if (z4) {
                                category2.setGroup(notificationsController3.notificationGroup);
                                category2.setGroupAlertBehavior(1);
                            }
                            if (action2 != null) {
                                category2.addAction(action2);
                            }
                            if (!z11) {
                                category2.addAction(build52);
                            }
                            if (arrayList10.size() != 1 && !TextUtils.isEmpty(str)) {
                                category2.setSubText(str);
                            }
                            if (DialogObject.isEncryptedDialog(j5)) {
                                category2.setLocalOnly(true);
                            }
                            if (bitmap2 != null) {
                                category2.setLargeIcon(bitmap2);
                            }
                            if (!AndroidUtilities.needShowPasscode(false) && !SharedConfig.isWaitingForPasscodeEnter && (arrayList5 = arrayList132) != null) {
                                size2 = arrayList5.size();
                                i13 = 0;
                                while (i13 < size2) {
                                    TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow = arrayList5.get(i13);
                                    int size4 = tLRPC$TL_keyboardButtonRow.buttons.size();
                                    int i29 = 0;
                                    while (i29 < size4) {
                                        TLRPC$KeyboardButton tLRPC$KeyboardButton = tLRPC$TL_keyboardButtonRow.buttons.get(i29);
                                        if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonCallback) {
                                            i14 = size2;
                                            arrayList6 = arrayList5;
                                            Intent intent5 = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                            intent5.putExtra(str222, notificationsController3.currentAccount);
                                            intent5.putExtra("did", j5);
                                            byte[] bArr = tLRPC$KeyboardButton.data;
                                            if (bArr != null) {
                                                intent5.putExtra("data", bArr);
                                            }
                                            int i30 = i282;
                                            intent5.putExtra("mid", i30);
                                            String str28 = tLRPC$KeyboardButton.text;
                                            Context context = ApplicationLoader.applicationContext;
                                            i15 = i30;
                                            int i31 = notificationsController3.lastButtonId;
                                            str11 = str222;
                                            notificationsController3.lastButtonId = i31 + 1;
                                            category2.addAction(0, str28, PendingIntent.getBroadcast(context, i31, intent5, 134217728));
                                        } else {
                                            i14 = size2;
                                            arrayList6 = arrayList5;
                                            str11 = str222;
                                            i15 = i282;
                                        }
                                        i29++;
                                        size2 = i14;
                                        arrayList5 = arrayList6;
                                        i282 = i15;
                                        str222 = str11;
                                    }
                                    i13++;
                                    str222 = str222;
                                }
                            }
                            if (tLRPC$Chat2 == null || tLRPC$User3 == null) {
                                tLRPC$User4 = tLRPC$User3;
                            } else {
                                tLRPC$User4 = tLRPC$User3;
                                String str29 = tLRPC$User4.phone;
                                if (str29 != null && str29.length() > 0) {
                                    category2.addPerson("tel:+" + tLRPC$User4.phone);
                                }
                            }
                            boolean z162 = z4;
                            if (Build.VERSION.SDK_INT >= 26) {
                                notificationsController3.setNotificationChannel(notification2, category2, z162);
                            }
                            j2 = j62;
                            i8 = i24;
                            z8 = z162;
                            longSparseArray2 = longSparseArray102;
                            longSparseArray4 = longSparseArray9;
                            i9 = i7;
                            longSparseArray3 = longSparseArray5;
                            arrayList3 = arrayList10;
                            notification = notification2;
                            arrayList4 = arrayList12;
                            arrayList4.add(new 1NotificationHolder(num2.intValue(), j5, i12, str202, tLRPC$User4, tLRPC$Chat2, category2, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                            notificationsController2 = this;
                            notificationsController2.wearNotificationsIds.put(j5, num2);
                        }
                    }
                }
                action = build;
                str8 = "dialog_id";
                boolean z1522 = !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest);
                str9 = "";
                if (person == null) {
                }
                messagingStyle = new NotificationCompat.MessagingStyle(str9);
                messagingStyle2 = messagingStyle;
                i11 = Build.VERSION.SDK_INT;
                if (i11 >= 28) {
                }
                messagingStyle2.setConversationTitle(format);
                messagingStyle2.setGroupConversation(i11 >= 28 || (!z7 && DialogObject.isChatDialog(j5)) || UserObject.isReplyUser(j5));
                StringBuilder sb222 = new StringBuilder();
                String[] strArr222 = new String[1];
                int i2722 = i10;
                boolean[] zArr22 = new boolean[1];
                size = arrayList2.size() - 1;
                int i2822 = 0;
                ArrayList<TLRPC$TL_keyboardButtonRow> arrayList1322 = null;
                while (size >= 0) {
                }
                String str2022 = string;
                long j622 = clientUserId;
                NotificationCompat.MessagingStyle messagingStyle422 = messagingStyle2;
                StringBuilder sb322 = sb222;
                String str2122 = str5;
                Intent intent222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                intent222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                intent222.setFlags(ConnectionsManager.FileTypeFile);
                intent222.addCategory("android.intent.category.LAUNCHER");
                if (!DialogObject.isEncryptedDialog(j5)) {
                }
                if (i23 != 0) {
                }
                String str2222 = str2122;
                intent222.putExtra(str2222, notificationsController3.currentAccount);
                PendingIntent activity22 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222, 1073741824);
                NotificationCompat.WearableExtender wearableExtender22 = new NotificationCompat.WearableExtender();
                action2 = action;
                if (action != null) {
                }
                Intent intent322 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                intent322.addFlags(32);
                intent322.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                intent322.putExtra(str8, j5);
                intent322.putExtra(str7, i2722);
                intent322.putExtra(str2222, notificationsController3.currentAccount);
                NotificationCompat.Action build522 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent322, 134217728)).setSemanticAction(2).setShowsUserInterface(false).build();
                if (DialogObject.isEncryptedDialog(j5)) {
                }
                if (str10 == null) {
                }
                wearableExtender22.setBridgeTag("tgaccount" + j622);
                ArrayList arrayList1422 = arrayList2;
                long j722 = ((long) ((MessageObject) arrayList1422.get(0)).messageOwner.date) * 1000;
                LongSparseArray longSparseArray1022 = longSparseArray8;
                NotificationCompat.Builder category22 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str2022).setSmallIcon(R.drawable.notification).setContentText(sb322.toString()).setAutoCancel(true).setNumber(arrayList1422.size()).setColor(-15618822).setGroupSummary(false).setWhen(j722).setShowWhen(true).setStyle(messagingStyle422).setContentIntent(activity22).extend(wearableExtender22).setSortKey(String.valueOf(Long.MAX_VALUE - j722)).setCategory("msg");
                Intent intent422 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                intent422.putExtra("messageDate", i6);
                intent422.putExtra("dialogId", j5);
                intent422.putExtra(str2222, notificationsController3.currentAccount);
                category22.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent422, 134217728));
                if (z4) {
                }
                if (action2 != null) {
                }
                if (!z11) {
                }
                if (arrayList10.size() != 1) {
                }
                if (DialogObject.isEncryptedDialog(j5)) {
                }
                if (bitmap2 != null) {
                }
                if (!AndroidUtilities.needShowPasscode(false)) {
                    size2 = arrayList5.size();
                    i13 = 0;
                    while (i13 < size2) {
                    }
                }
                if (tLRPC$Chat2 == null) {
                }
                tLRPC$User4 = tLRPC$User3;
                boolean z1622 = z4;
                if (Build.VERSION.SDK_INT >= 26) {
                }
                j2 = j622;
                i8 = i24;
                z8 = z1622;
                longSparseArray2 = longSparseArray1022;
                longSparseArray4 = longSparseArray9;
                i9 = i7;
                longSparseArray3 = longSparseArray5;
                arrayList3 = arrayList10;
                notification = notification2;
                arrayList4 = arrayList12;
                arrayList4.add(new 1NotificationHolder(num2.intValue(), j5, i12, str2022, tLRPC$User4, tLRPC$Chat2, category22, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                notificationsController2 = this;
                notificationsController2.wearNotificationsIds.put(j5, num2);
            } else {
                arrayList2 = arrayList11;
                num = num5;
                messageObject = messageObject3;
                i6 = i25;
                i7 = size3;
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
                    j2 = clientUserId;
                    longSparseArray2 = longSparseArray8;
                    longSparseArray3 = longSparseArray6;
                    notificationsController2 = notificationsController3;
                    notification = notification2;
                    arrayList3 = arrayList10;
                    i8 = i24;
                    arrayList4 = arrayList12;
                    z8 = z4;
                    longSparseArray4 = longSparseArray9;
                    i9 = i7;
                } else {
                    tLRPC$User = null;
                }
                string = LocaleController.getString("SecretChatName", R.string.SecretChatName);
                tLRPC$User2 = tLRPC$User;
                tLRPC$FileLocation = null;
                tLRPC$Chat = null;
                z5 = false;
                z6 = false;
                z7 = false;
                if (!z11) {
                }
                if (tLRPC$FileLocation == null) {
                }
                if (tLRPC$Chat == null) {
                }
                File file32 = file;
                str5 = "currentAccount";
                if (z7) {
                }
                str6 = str4;
                tLRPC$Chat2 = tLRPC$Chat;
                Intent intent6 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                intent6.putExtra("dialog_id", j5);
                i10 = i5;
                intent6.putExtra("max_id", i10);
                intent6.putExtra("topic_id", i23);
                intent6.putExtra(str5, notificationsController3.currentAccount);
                bitmap2 = bitmap;
                num2 = num;
                PendingIntent broadcast2 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent6, 134217728);
                RemoteInput build32 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                if (!DialogObject.isChatDialog(j5)) {
                }
                build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast2).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build32).setShowsUserInterface(false).build();
                num3 = notificationsController3.pushDialogs.get(j5);
                if (num3 == null) {
                }
                int max2 = Math.max(num3.intValue(), arrayList2.size());
                if (max2 > 1) {
                }
                person = (Person) longSparseArray8.get(clientUserId);
                if (Build.VERSION.SDK_INT >= 28) {
                    user = getMessagesController().getUser(Long.valueOf(clientUserId));
                    if (user == null) {
                    }
                    if (user != null) {
                    }
                }
                action = build;
                str8 = "dialog_id";
                boolean z15222 = !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest);
                str9 = "";
                if (person == null) {
                }
                messagingStyle = new NotificationCompat.MessagingStyle(str9);
                messagingStyle2 = messagingStyle;
                i11 = Build.VERSION.SDK_INT;
                if (i11 >= 28) {
                }
                messagingStyle2.setConversationTitle(format);
                messagingStyle2.setGroupConversation(i11 >= 28 || (!z7 && DialogObject.isChatDialog(j5)) || UserObject.isReplyUser(j5));
                StringBuilder sb2222 = new StringBuilder();
                String[] strArr2222 = new String[1];
                int i27222 = i10;
                boolean[] zArr222 = new boolean[1];
                size = arrayList2.size() - 1;
                int i28222 = 0;
                ArrayList<TLRPC$TL_keyboardButtonRow> arrayList13222 = null;
                while (size >= 0) {
                }
                String str20222 = string;
                long j6222 = clientUserId;
                NotificationCompat.MessagingStyle messagingStyle4222 = messagingStyle2;
                StringBuilder sb3222 = sb2222;
                String str21222 = str5;
                Intent intent2222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                intent2222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                intent2222.setFlags(ConnectionsManager.FileTypeFile);
                intent2222.addCategory("android.intent.category.LAUNCHER");
                if (!DialogObject.isEncryptedDialog(j5)) {
                }
                if (i23 != 0) {
                }
                String str22222 = str21222;
                intent2222.putExtra(str22222, notificationsController3.currentAccount);
                PendingIntent activity222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2222, 1073741824);
                NotificationCompat.WearableExtender wearableExtender222 = new NotificationCompat.WearableExtender();
                action2 = action;
                if (action != null) {
                }
                Intent intent3222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                intent3222.addFlags(32);
                intent3222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                intent3222.putExtra(str8, j5);
                intent3222.putExtra(str7, i27222);
                intent3222.putExtra(str22222, notificationsController3.currentAccount);
                NotificationCompat.Action build5222 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent3222, 134217728)).setSemanticAction(2).setShowsUserInterface(false).build();
                if (DialogObject.isEncryptedDialog(j5)) {
                }
                if (str10 == null) {
                }
                wearableExtender222.setBridgeTag("tgaccount" + j6222);
                ArrayList arrayList14222 = arrayList2;
                long j7222 = ((long) ((MessageObject) arrayList14222.get(0)).messageOwner.date) * 1000;
                LongSparseArray longSparseArray10222 = longSparseArray8;
                NotificationCompat.Builder category222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str20222).setSmallIcon(R.drawable.notification).setContentText(sb3222.toString()).setAutoCancel(true).setNumber(arrayList14222.size()).setColor(-15618822).setGroupSummary(false).setWhen(j7222).setShowWhen(true).setStyle(messagingStyle4222).setContentIntent(activity222).extend(wearableExtender222).setSortKey(String.valueOf(Long.MAX_VALUE - j7222)).setCategory("msg");
                Intent intent4222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                intent4222.putExtra("messageDate", i6);
                intent4222.putExtra("dialogId", j5);
                intent4222.putExtra(str22222, notificationsController3.currentAccount);
                category222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent4222, 134217728));
                if (z4) {
                }
                if (action2 != null) {
                }
                if (!z11) {
                }
                if (arrayList10.size() != 1) {
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
                boolean z16222 = z4;
                if (Build.VERSION.SDK_INT >= 26) {
                }
                j2 = j6222;
                i8 = i24;
                z8 = z16222;
                longSparseArray2 = longSparseArray10222;
                longSparseArray4 = longSparseArray9;
                i9 = i7;
                longSparseArray3 = longSparseArray5;
                arrayList3 = arrayList10;
                notification = notification2;
                arrayList4 = arrayList12;
                arrayList4.add(new 1NotificationHolder(num2.intValue(), j5, i12, str20222, tLRPC$User4, tLRPC$Chat2, category222, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                notificationsController2 = this;
                notificationsController2.wearNotificationsIds.put(j5, num2);
            }
            i22 = i8 + 1;
            arrayList9 = arrayList4;
            clientUserId = j2;
            z10 = z8;
            longSparseArray7 = longSparseArray4;
            size3 = i9;
            longSparseArray6 = longSparseArray3;
            arrayList7 = arrayList3;
            longSparseArray8 = longSparseArray2;
            build2 = notification;
            i21 = 7;
            notificationsController3 = notificationsController2;
        }
        LongSparseArray longSparseArray11 = longSparseArray8;
        LongSparseArray longSparseArray12 = longSparseArray7;
        Notification notification3 = build2;
        NotificationsController notificationsController4 = notificationsController3;
        ArrayList arrayList15 = arrayList9;
        if (z10) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("show summary with id " + notificationsController4.notificationId);
            }
            try {
                notificationManager.notify(notificationsController4.notificationId, notification3);
                arrayList = arrayList15;
                notificationsController = notificationsController4;
            } catch (SecurityException e2) {
                FileLog.e(e2);
                arrayList = arrayList15;
                notificationsController = notificationsController4;
                resetNotificationSound(builder, j, i, str2, jArr, i2, uri, i3, z, z2, z3, i4);
            }
        } else {
            arrayList = arrayList15;
            notificationsController = notificationsController4;
            if (notificationsController.openedInBubbleDialogs.isEmpty()) {
                notificationManager.cancel(notificationsController.notificationId);
            }
        }
        int i32 = 0;
        while (i32 < longSparseArray12.size()) {
            LongSparseArray longSparseArray13 = longSparseArray12;
            if (!notificationsController.openedInBubbleDialogs.contains(Long.valueOf(longSparseArray13.keyAt(i32)))) {
                Integer num6 = (Integer) longSparseArray13.valueAt(i32);
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("cancel notification id " + num6);
                }
                notificationManager.cancel(num6.intValue());
            }
            i32++;
            longSparseArray12 = longSparseArray13;
        }
        ArrayList arrayList16 = new ArrayList(arrayList.size());
        int size5 = arrayList.size();
        int i33 = 0;
        while (i33 < size5) {
            ArrayList arrayList17 = arrayList;
            1NotificationHolder r3 = (1NotificationHolder) arrayList17.get(i33);
            arrayList16.clear();
            if (Build.VERSION.SDK_INT < 29 || DialogObject.isEncryptedDialog(r3.dialogId)) {
                longSparseArray = longSparseArray11;
            } else {
                NotificationCompat.Builder builder2 = r3.notification;
                long j8 = r3.dialogId;
                longSparseArray = longSparseArray11;
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
            longSparseArray11 = longSparseArray;
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
    public static /* synthetic */ void lambda$showExtraNotifications$34(Uri uri) {
        ApplicationLoader.applicationContext.revokeUriPermission(uri, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadRoundAvatar$36(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
        imageDecoder.setPostProcessor(NotificationsController$$ExternalSyntheticLambda1.INSTANCE);
    }

    @TargetApi(28)
    private void loadRoundAvatar(File file, Person.Builder builder) {
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
            if (i == 0) {
                return;
            }
            try {
                this.soundPool.play(i, 1.0f, 1.0f, 1, 0, 1.0f);
            } catch (Exception e) {
                FileLog.e(e);
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
        int i2 = 0;
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
        int i3 = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, i), -1);
        if (i3 != -1) {
            TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings3 = tLRPC$TL_account_updateNotifySettings.settings;
            tLRPC$TL_inputPeerNotifySettings3.flags |= 4;
            if (i3 == 3) {
                tLRPC$TL_inputPeerNotifySettings3.mute_until = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + getSharedPrefKey(j, i), 0);
            } else {
                if (i3 == 2) {
                    i2 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                }
                tLRPC$TL_inputPeerNotifySettings3.mute_until = i2;
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
            if (string.equals("NoSound")) {
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
            if (string.equals("NoSound")) {
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

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0028, code lost:
        if (r4.megagroup == false) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x000e, code lost:
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
