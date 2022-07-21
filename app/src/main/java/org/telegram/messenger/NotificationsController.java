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
import com.huawei.hms.adapter.internal.AvailableCode;
import com.huawei.hms.push.constant.RemoteMessageConst;
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
import org.telegram.tgnet.TLRPC$TL_inputNotifyBroadcasts;
import org.telegram.tgnet.TLRPC$TL_inputNotifyChats;
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

    public static /* synthetic */ void lambda$updateServerNotificationsSettings$39(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    public static /* synthetic */ void lambda$updateServerNotificationsSettings$40(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    static {
        notificationManager = null;
        systemNotificationManager = null;
        if (Build.VERSION.SDK_INT >= 26 && ApplicationLoader.applicationContext != null) {
            notificationManager = NotificationManagerCompat.from(ApplicationLoader.applicationContext);
            systemNotificationManager = (NotificationManager) ApplicationLoader.applicationContext.getSystemService(RemoteMessageConst.NOTIFICATION);
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
        systemNotificationManager = (NotificationManager) ApplicationLoader.applicationContext.getSystemService(RemoteMessageConst.NOTIFICATION);
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
        this.notificationDelayRunnable = new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$new$0();
            }
        };
    }

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

    public void muteUntil(long j, int i) {
        long j2 = 0;
        if (j != 0) {
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            boolean isGlobalNotificationsEnabled = getInstance(this.currentAccount).isGlobalNotificationsEnabled(j);
            if (i != Integer.MAX_VALUE) {
                edit.putInt("notify2_" + j, 3);
                edit.putInt("notifyuntil_" + j, getConnectionsManager().getCurrentTime() + i);
                j2 = (((long) i) << 32) | 1;
            } else if (!isGlobalNotificationsEnabled) {
                edit.remove("notify2_" + j);
            } else {
                edit.putInt("notify2_" + j, 2);
                j2 = 1L;
            }
            getInstance(this.currentAccount).removeNotificationsForDialog(j);
            MessagesStorage.getInstance(this.currentAccount).setDialogFlags(j, j2);
            edit.commit();
            TLRPC$Dialog tLRPC$Dialog = MessagesController.getInstance(this.currentAccount).dialogs_dict.get(j);
            if (tLRPC$Dialog != null) {
                TLRPC$TL_peerNotifySettings tLRPC$TL_peerNotifySettings = new TLRPC$TL_peerNotifySettings();
                tLRPC$Dialog.notify_settings = tLRPC$TL_peerNotifySettings;
                if (i != Integer.MAX_VALUE || isGlobalNotificationsEnabled) {
                    tLRPC$TL_peerNotifySettings.mute_until = i;
                }
            }
            getInstance(this.currentAccount).updateServerNotificationsSettings(j);
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

    public /* synthetic */ void lambda$cleanup$1() {
        this.openedDialogId = 0L;
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

    public /* synthetic */ void lambda$setOpenedDialogId$2(long j) {
        this.openedDialogId = j;
    }

    public void setOpenedDialogId(final long j) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$setOpenedDialogId$2(j);
            }
        });
    }

    public void setOpenedInBubble(final long j, final boolean z) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$setOpenedInBubble$3(z, j);
            }
        });
    }

    public /* synthetic */ void lambda$setOpenedInBubble$3(boolean z, long j) {
        if (z) {
            this.openedInBubbleDialogs.add(Long.valueOf(j));
        } else {
            this.openedInBubbleDialogs.remove(Long.valueOf(j));
        }
    }

    public void setLastOnlineFromOtherDevice(final int i) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$setLastOnlineFromOtherDevice$4(i);
            }
        });
    }

    public /* synthetic */ void lambda$setLastOnlineFromOtherDevice$4(int i) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("set last online from other device = " + i);
        }
        this.lastOnlineFromOtherDevice = i;
    }

    public void removeNotificationsForDialog(long j) {
        processReadMessages(null, j, 0, Integer.MAX_VALUE, false);
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

    public void forceShowPopupForReply() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$forceShowPopupForReply$6();
            }
        });
    }

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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$forceShowPopupForReply$5(arrayList);
            }
        });
    }

    public /* synthetic */ void lambda$forceShowPopupForReply$5(ArrayList arrayList) {
        this.popupReplyMessages = arrayList;
        Intent intent = new Intent(ApplicationLoader.applicationContext, PopupNotificationActivity.class);
        intent.putExtra("force", true);
        intent.putExtra("currentAccount", this.currentAccount);
        intent.setFlags(268763140);
        ApplicationLoader.applicationContext.startActivity(intent);
        ApplicationLoader.applicationContext.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    public void removeDeletedMessagesFromNotifications(final LongSparseArray<ArrayList<Integer>> longSparseArray) {
        final ArrayList arrayList = new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$removeDeletedMessagesFromNotifications$9(longSparseArray, arrayList);
            }
        });
    }

    public /* synthetic */ void lambda$removeDeletedMessagesFromNotifications$9(LongSparseArray longSparseArray, final ArrayList arrayList) {
        Integer num;
        ArrayList arrayList2;
        Integer num2;
        LongSparseArray longSparseArray2 = longSparseArray;
        int i = this.total_unread_count;
        getAccountInstance().getNotificationsSettings();
        Integer num3 = 0;
        int i2 = 0;
        while (i2 < longSparseArray.size()) {
            long keyAt = longSparseArray2.keyAt(i2);
            SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(keyAt);
            if (sparseArray == null) {
                num = num3;
            } else {
                ArrayList arrayList3 = (ArrayList) longSparseArray2.get(keyAt);
                int size = arrayList3.size();
                int i3 = 0;
                while (i3 < size) {
                    int intValue = ((Integer) arrayList3.get(i3)).intValue();
                    MessageObject messageObject = sparseArray.get(intValue);
                    Integer num4 = num3;
                    if (messageObject != null) {
                        long dialogId = messageObject.getDialogId();
                        Integer num5 = this.pushDialogs.get(dialogId);
                        if (num5 == null) {
                            num5 = num4;
                        }
                        Integer valueOf = Integer.valueOf(num5.intValue() - 1);
                        if (valueOf.intValue() <= 0) {
                            this.smartNotificationsDialogs.remove(dialogId);
                            num2 = num4;
                        } else {
                            num2 = valueOf;
                        }
                        if (!num2.equals(num5)) {
                            arrayList2 = arrayList3;
                            int intValue2 = this.total_unread_count - num5.intValue();
                            this.total_unread_count = intValue2;
                            this.total_unread_count = intValue2 + num2.intValue();
                            this.pushDialogs.put(dialogId, num2);
                        } else {
                            arrayList2 = arrayList3;
                        }
                        if (num2.intValue() == 0) {
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
                    } else {
                        arrayList2 = arrayList3;
                    }
                    i3++;
                    num3 = num4;
                    arrayList3 = arrayList2;
                }
                num = num3;
                if (sparseArray.size() == 0) {
                    this.pushMessagesDict.remove(keyAt);
                }
            }
            i2++;
            longSparseArray2 = longSparseArray;
            num3 = num;
        }
        if (!arrayList.isEmpty()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda27
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda15
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

    public /* synthetic */ void lambda$removeDeletedMessagesFromNotifications$7(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    public /* synthetic */ void lambda$removeDeletedMessagesFromNotifications$8(int i) {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    public void removeDeletedHisoryFromNotifications(final LongSparseIntArray longSparseIntArray) {
        final ArrayList arrayList = new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$removeDeletedHisoryFromNotifications$12(longSparseIntArray, arrayList);
            }
        });
    }

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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda28
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda19
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

    public /* synthetic */ void lambda$removeDeletedHisoryFromNotifications$10(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    public /* synthetic */ void lambda$removeDeletedHisoryFromNotifications$11(int i) {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    public void processReadMessages(final LongSparseIntArray longSparseIntArray, final long j, final int i, final int i2, final boolean z) {
        final ArrayList arrayList = new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processReadMessages$14(longSparseIntArray, arrayList, j, i2, i, z);
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:52:0x00d7, code lost:
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda30
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$processReadMessages$13(arrayList);
                }
            });
        }
    }

    public /* synthetic */ void lambda$processReadMessages$13(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0056, code lost:
        if (r0 == 2) goto L21;
     */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0070  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int addToPopupMessages(ArrayList<MessageObject> arrayList, MessageObject messageObject, long j, boolean z, SharedPreferences sharedPreferences) {
        int i;
        if (!DialogObject.isEncryptedDialog(j)) {
            if (sharedPreferences.getBoolean("custom_" + j, false)) {
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processEditedMessages$15(longSparseArray);
            }
        });
    }

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
            notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda33
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

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0048, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserJoined) == false) goto L18;
     */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00f6  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x013d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processNewMessages$18(ArrayList arrayList, final ArrayList arrayList2, boolean z, boolean z2, CountDownLatch countDownLatch) {
        boolean z3;
        boolean z4;
        Integer num;
        boolean z5;
        LongSparseArray longSparseArray;
        int i;
        int i2;
        long j;
        boolean z6;
        long j2;
        MessageObject messageObject;
        long j3;
        boolean z7;
        int i3;
        SparseArray<MessageObject> sparseArray;
        long j4;
        SparseArray<MessageObject> sparseArray2;
        MessageObject messageObject2;
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
                messageObject3 = messageObject5;
                long j7 = messageObject4.messageOwner.random_id;
                if (j7 != 0) {
                    messageObject = this.fcmRandomMessagesDict.get(j7);
                    if (messageObject != null) {
                        j2 = j;
                        this.fcmRandomMessagesDict.remove(messageObject4.messageOwner.random_id);
                    } else {
                        j2 = j;
                    }
                    if (messageObject == null) {
                        if (messageObject.isFcmMessage()) {
                            if (sparseArray3 == null) {
                                sparseArray3 = new SparseArray<>();
                                this.pushMessagesDict.put(j6, sparseArray3);
                            }
                            sparseArray3.put(id, messageObject4);
                            int indexOf = this.pushMessages.indexOf(messageObject);
                            if (indexOf >= 0) {
                                this.pushMessages.set(indexOf, messageObject4);
                                messageObject2 = messageObject4;
                                i4 = addToPopupMessages(arrayList2, messageObject4, dialogId, z6, notificationsSettings);
                            } else {
                                messageObject2 = messageObject4;
                                i4 = i2;
                            }
                            if (z) {
                                boolean z12 = messageObject2.localEdit;
                                if (z12) {
                                    getMessagesStorage().putPushMessage(messageObject2);
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
                                if (indexOfKey >= 0) {
                                    z7 = ((Boolean) longSparseArray2.valueAt(indexOfKey)).booleanValue();
                                } else {
                                    int notifyOverride = getNotifyOverride(notificationsSettings, j3);
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
                                    getMessagesController().checkUnreadReactions(j3, sparseBooleanArray);
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
                messageObject3 = messageObject5;
            }
            j2 = j;
            messageObject = messageObject3;
            if (messageObject == null) {
            }
        }
        final int i6 = i4;
        if (z9) {
            this.notifyCheck = z2;
        }
        if (!arrayList2.isEmpty() && !AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda31
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
                Boolean valueOf = messageObject6.isFcmMessage() ? Boolean.valueOf(messageObject6.localChannel) : null;
                int i7 = this.total_unread_count;
                int notifyOverride2 = getNotifyOverride(notificationsSettings, dialogId2);
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
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda20
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

    public /* synthetic */ void lambda$processNewMessages$17(int i) {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    public int getTotalUnreadCount() {
        return this.total_unread_count;
    }

    public void processDialogsUpdateRead(final LongSparseIntArray longSparseIntArray) {
        final ArrayList arrayList = new ArrayList();
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processDialogsUpdateRead$21(longSparseIntArray, arrayList);
            }
        });
    }

    public /* synthetic */ void lambda$processDialogsUpdateRead$21(LongSparseIntArray longSparseIntArray, final ArrayList arrayList) {
        boolean z;
        boolean z2;
        Integer num;
        TLRPC$Chat chat;
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
            if (DialogObject.isChatDialog(keyAt) && ((chat = getMessagesController().getChat(Long.valueOf(-keyAt))) == null || chat.min || ChatObject.isNotInChat(chat))) {
                i3 = 0;
            }
            int notifyOverride = getNotifyOverride(notificationsSettings, keyAt);
            if (notifyOverride == -1) {
                z2 = isGlobalNotificationsEnabled(keyAt);
            } else {
                z2 = notifyOverride != 2;
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
            if ((z2 || i3 == 0) && num2 != null) {
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
            } else if (z2) {
                this.total_unread_count += i3;
                this.pushDialogs.put(keyAt, Integer.valueOf(i3));
            }
            i2++;
        }
        if (!arrayList.isEmpty()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda26
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda17
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

    public /* synthetic */ void lambda$processDialogsUpdateRead$19(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    public /* synthetic */ void lambda$processDialogsUpdateRead$20(int i) {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    public void processLoadedUnreadMessages(final LongSparseArray<Integer> longSparseArray, final ArrayList<TLRPC$Message> arrayList, final ArrayList<MessageObject> arrayList2, ArrayList<TLRPC$User> arrayList3, ArrayList<TLRPC$Chat> arrayList4, ArrayList<TLRPC$EncryptedChat> arrayList5) {
        getMessagesController().putUsers(arrayList3, true);
        getMessagesController().putChats(arrayList4, true);
        getMessagesController().putEncryptedChats(arrayList5, true);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processLoadedUnreadMessages$23(arrayList, longSparseArray, arrayList2);
            }
        });
    }

    public /* synthetic */ void lambda$processLoadedUnreadMessages$23(ArrayList arrayList, LongSparseArray longSparseArray, ArrayList arrayList2) {
        LongSparseArray longSparseArray2;
        SharedPreferences sharedPreferences;
        boolean z;
        boolean z2;
        int i;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        long j;
        SparseArray<MessageObject> sparseArray;
        boolean z3;
        SparseArray<MessageObject> sparseArray2;
        boolean z4;
        ArrayList arrayList3 = arrayList;
        this.pushDialogs.clear();
        this.pushMessages.clear();
        this.pushMessagesDict.clear();
        boolean z5 = false;
        this.total_unread_count = 0;
        this.personalCount = 0;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        LongSparseArray longSparseArray3 = new LongSparseArray();
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
                            MessageObject messageObject = new MessageObject(this.currentAccount, tLRPC$Message, z5, z5);
                            if (isPersonalMessage(messageObject)) {
                                this.personalCount += i2;
                            }
                            i = i3;
                            long dialogId = messageObject.getDialogId();
                            if (messageObject.messageOwner.mentioned) {
                                sparseArray = sparseArray3;
                                j = messageObject.getFromChatId();
                            } else {
                                sparseArray = sparseArray3;
                                j = dialogId;
                            }
                            int indexOfKey = longSparseArray3.indexOfKey(j);
                            if (indexOfKey >= 0) {
                                z3 = ((Boolean) longSparseArray3.valueAt(indexOfKey)).booleanValue();
                            } else {
                                int notifyOverride = getNotifyOverride(notificationsSettings, j);
                                if (notifyOverride == -1) {
                                    z4 = isGlobalNotificationsEnabled(j);
                                } else {
                                    z4 = notifyOverride != 2;
                                }
                                z3 = z4;
                                longSparseArray3.put(j, Boolean.valueOf(z3));
                            }
                            if (z3 && (j != this.openedDialogId || !ApplicationLoader.isScreenOn)) {
                                if (sparseArray == null) {
                                    sparseArray2 = new SparseArray<>();
                                    this.pushMessagesDict.put(j4, sparseArray2);
                                } else {
                                    sparseArray2 = sparseArray;
                                }
                                sparseArray2.put(tLRPC$Message.id, messageObject);
                                this.pushMessages.add(0, messageObject);
                                if (dialogId != j) {
                                    Integer num = this.pushDialogsOverrideMention.get(dialogId);
                                    this.pushDialogsOverrideMention.put(dialogId, Integer.valueOf(num == null ? 1 : num.intValue() + 1));
                                }
                            }
                            i3 = i + 1;
                            arrayList3 = arrayList;
                            z5 = false;
                            j2 = 0;
                            i2 = 1;
                        }
                    }
                }
                i = i3;
                i3 = i + 1;
                arrayList3 = arrayList;
                z5 = false;
                j2 = 0;
                i2 = 1;
            }
        }
        for (int i4 = 0; i4 < longSparseArray.size(); i4++) {
            long keyAt = longSparseArray.keyAt(i4);
            int indexOfKey2 = longSparseArray3.indexOfKey(keyAt);
            if (indexOfKey2 >= 0) {
                z2 = ((Boolean) longSparseArray3.valueAt(indexOfKey2)).booleanValue();
            } else {
                int notifyOverride2 = getNotifyOverride(notificationsSettings, keyAt);
                if (notifyOverride2 == -1) {
                    z2 = isGlobalNotificationsEnabled(keyAt);
                } else {
                    z2 = notifyOverride2 != 2;
                }
                longSparseArray3.put(keyAt, Boolean.valueOf(z2));
            }
            if (z2) {
                int intValue = ((Integer) longSparseArray.valueAt(i4)).intValue();
                this.pushDialogs.put(keyAt, Integer.valueOf(intValue));
                this.total_unread_count += intValue;
            }
        }
        if (arrayList2 != null) {
            int i5 = 0;
            while (i5 < arrayList2.size()) {
                MessageObject messageObject2 = (MessageObject) arrayList2.get(i5);
                int id = messageObject2.getId();
                if (this.pushMessagesDict.indexOfKey(id) >= 0) {
                    sharedPreferences = notificationsSettings;
                    longSparseArray2 = longSparseArray3;
                } else {
                    if (isPersonalMessage(messageObject2)) {
                        this.personalCount++;
                    }
                    long dialogId2 = messageObject2.getDialogId();
                    TLRPC$Message tLRPC$Message2 = messageObject2.messageOwner;
                    long j5 = tLRPC$Message2.random_id;
                    long fromChatId = tLRPC$Message2.mentioned ? messageObject2.getFromChatId() : dialogId2;
                    int indexOfKey3 = longSparseArray3.indexOfKey(fromChatId);
                    if (indexOfKey3 >= 0) {
                        z = ((Boolean) longSparseArray3.valueAt(indexOfKey3)).booleanValue();
                    } else {
                        int notifyOverride3 = getNotifyOverride(notificationsSettings, fromChatId);
                        if (notifyOverride3 == -1) {
                            z = isGlobalNotificationsEnabled(fromChatId);
                        } else {
                            z = notifyOverride3 != 2;
                        }
                        longSparseArray3.put(fromChatId, Boolean.valueOf(z));
                    }
                    sharedPreferences = notificationsSettings;
                    if (z) {
                        longSparseArray2 = longSparseArray3;
                        if (fromChatId != this.openedDialogId || !ApplicationLoader.isScreenOn) {
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
                            this.pushMessages.add(0, messageObject2);
                            if (dialogId2 != fromChatId) {
                                Integer num2 = this.pushDialogsOverrideMention.get(dialogId2);
                                this.pushDialogsOverrideMention.put(dialogId2, Integer.valueOf(num2 == null ? 1 : num2.intValue() + 1));
                            }
                            Integer num3 = this.pushDialogs.get(fromChatId);
                            int intValue2 = num3 != null ? num3.intValue() + 1 : 1;
                            if (num3 != null) {
                                this.total_unread_count -= num3.intValue();
                            }
                            this.total_unread_count += intValue2;
                            this.pushDialogs.put(fromChatId, Integer.valueOf(intValue2));
                            i5++;
                            notificationsSettings = sharedPreferences;
                            longSparseArray3 = longSparseArray2;
                        }
                    } else {
                        longSparseArray2 = longSparseArray3;
                    }
                }
                i5++;
                notificationsSettings = sharedPreferences;
                longSparseArray3 = longSparseArray2;
            }
        }
        final int size = this.pushDialogs.size();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda16
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

    public /* synthetic */ void lambda$processLoadedUnreadMessages$22(int i) {
        if (this.total_unread_count == 0) {
            this.popupMessages.clear();
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    private int getTotalAllUnreadCount() {
        int i;
        int i2;
        int i3 = 0;
        for (int i4 = 0; i4 < 4; i4++) {
            if (UserConfig.getInstance(i4).isClientActivated()) {
                NotificationsController notificationsController = getInstance(i4);
                if (notificationsController.showBadgeNumber) {
                    if (notificationsController.showBadgeMessages) {
                        if (notificationsController.showBadgeMuted) {
                            try {
                                ArrayList arrayList = new ArrayList(MessagesController.getInstance(i4).allDialogs);
                                int size = arrayList.size();
                                for (int i5 = 0; i5 < size; i5++) {
                                    TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) arrayList.get(i5);
                                    if ((tLRPC$Dialog == null || !DialogObject.isChatDialog(tLRPC$Dialog.id) || !ChatObject.isNotInChat(getMessagesController().getChat(Long.valueOf(-tLRPC$Dialog.id)))) && tLRPC$Dialog != null && (i2 = tLRPC$Dialog.unread_count) != 0) {
                                        i3 += i2;
                                    }
                                }
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        } else {
                            i = notificationsController.total_unread_count;
                        }
                    } else if (notificationsController.showBadgeMuted) {
                        try {
                            int size2 = MessagesController.getInstance(i4).allDialogs.size();
                            for (int i6 = 0; i6 < size2; i6++) {
                                TLRPC$Dialog tLRPC$Dialog2 = MessagesController.getInstance(i4).allDialogs.get(i6);
                                if ((!DialogObject.isChatDialog(tLRPC$Dialog2.id) || !ChatObject.isNotInChat(getMessagesController().getChat(Long.valueOf(-tLRPC$Dialog2.id)))) && tLRPC$Dialog2.unread_count != 0) {
                                    i3++;
                                }
                            }
                        } catch (Exception e2) {
                            FileLog.e(e2);
                        }
                    } else {
                        i = notificationsController.pushDialogs.size();
                    }
                    i3 += i;
                }
            }
        }
        return i3;
    }

    public /* synthetic */ void lambda$updateBadge$24() {
        setBadge(getTotalAllUnreadCount());
    }

    public void updateBadge() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda13
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

    /* JADX WARN: Code restructure failed: missing block: B:147:0x0224, code lost:
        if (r11.getBoolean("EnablePreviewAll", true) == false) goto L149;
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x0234, code lost:
        if (r11.getBoolean("EnablePreviewGroup", r10) != false) goto L157;
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x023e, code lost:
        if (r11.getBoolean("EnablePreviewChannel", r10) != false) goto L157;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x0240, code lost:
        r4 = r23.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x0250, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageService) == false) goto L676;
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x0252, code lost:
        r24[0] = null;
        r5 = r4.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x025a, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGeoProximityReached) == false) goto L163;
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x0262, code lost:
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x0265, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserJoined) != false) goto L674;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x0269, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionContactSignUp) == false) goto L167;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x026f, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserUpdatedPhoto) == false) goto L171;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x0280, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationContactNewPhoto", org.telegram.messenger.beta.R.string.NotificationContactNewPhoto, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x0284, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionLoginUnknownLocation) == false) goto L175;
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x0286, code lost:
        r1 = org.telegram.messenger.LocaleController.formatString("formatDateAtTime", org.telegram.messenger.beta.R.string.formatDateAtTime, org.telegram.messenger.LocaleController.getInstance().formatterYear.format(r23.messageOwner.date * 1000), org.telegram.messenger.LocaleController.getInstance().formatterDay.format(r23.messageOwner.date * 1000));
        r0 = r23.messageOwner.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x02e4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationUnrecognizedDevice", org.telegram.messenger.beta.R.string.NotificationUnrecognizedDevice, getUserConfig().getCurrentUser().first_name, r1, r0.title, r0.address);
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x02e7, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGameScore) != false) goto L672;
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x02eb, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPaymentSent) == false) goto L179;
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x02f1, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPhoneCall) == false) goto L187;
     */
    /* JADX WARN: Code restructure failed: missing block: B:182:0x02f5, code lost:
        if (r5.video == false) goto L185;
     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x0300, code lost:
        return org.telegram.messenger.LocaleController.getString("CallMessageVideoIncomingMissed", org.telegram.messenger.beta.R.string.CallMessageVideoIncomingMissed);
     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x030a, code lost:
        return org.telegram.messenger.LocaleController.getString("CallMessageIncomingMissed", org.telegram.messenger.beta.R.string.CallMessageIncomingMissed);
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x030d, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser) == false) goto L231;
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x030f, code lost:
        r2 = r5.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x0315, code lost:
        if (r2 != 0) goto L194;
     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x031e, code lost:
        if (r5.users.size() != 1) goto L194;
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x0320, code lost:
        r2 = r23.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:195:0x0335, code lost:
        if (r2 == 0) goto L219;
     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x033f, code lost:
        if (r23.messageOwner.peer_id.channel_id == 0) goto L202;
     */
    /* JADX WARN: Code restructure failed: missing block: B:199:0x0343, code lost:
        if (r6.megagroup != false) goto L202;
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x0359, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.beta.R.string.ChannelAddedByNotification, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:203:0x035c, code lost:
        if (r2 != r17) goto L206;
     */
    /* JADX WARN: Code restructure failed: missing block: B:205:0x0372, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.beta.R.string.NotificationInvitedToGroup, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x0373, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x037f, code lost:
        if (r0 != null) goto L209;
     */
    /* JADX WARN: Code restructure failed: missing block: B:208:0x0381, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x0387, code lost:
        if (r8 != r0.id) goto L217;
     */
    /* JADX WARN: Code restructure failed: missing block: B:212:0x038b, code lost:
        if (r6.megagroup == false) goto L215;
     */
    /* JADX WARN: Code restructure failed: missing block: B:214:0x03a1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.beta.R.string.NotificationGroupAddSelfMega, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:216:0x03b6, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.beta.R.string.NotificationGroupAddSelf, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x03d1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.beta.R.string.NotificationGroupAddMember, r1, r6.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:219:0x03d2, code lost:
        r2 = new java.lang.StringBuilder();
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:221:0x03e2, code lost:
        if (r3 >= r23.messageOwner.action.users.size()) goto L793;
     */
    /* JADX WARN: Code restructure failed: missing block: B:222:0x03e4, code lost:
        r4 = getMessagesController().getUser(r23.messageOwner.action.users.get(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:223:0x03f8, code lost:
        if (r4 == null) goto L795;
     */
    /* JADX WARN: Code restructure failed: missing block: B:224:0x03fa, code lost:
        r4 = org.telegram.messenger.UserObject.getUserName(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:225:0x0402, code lost:
        if (r2.length() == 0) goto L227;
     */
    /* JADX WARN: Code restructure failed: missing block: B:226:0x0404, code lost:
        r2.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:227:0x0409, code lost:
        r2.append(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:228:0x040c, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:230:0x0429, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.beta.R.string.NotificationGroupAddMember, r1, r6.title, r2.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:232:0x042d, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCall) == false) goto L235;
     */
    /* JADX WARN: Code restructure failed: missing block: B:234:0x0442, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.beta.R.string.NotificationGroupCreatedCall, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:236:0x0445, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled) == false) goto L239;
     */
    /* JADX WARN: Code restructure failed: missing block: B:238:0x044d, code lost:
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x0450, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall) == false) goto L269;
     */
    /* JADX WARN: Code restructure failed: missing block: B:241:0x0452, code lost:
        r2 = r5.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:242:0x0458, code lost:
        if (r2 != 0) goto L246;
     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x0461, code lost:
        if (r5.users.size() != 1) goto L246;
     */
    /* JADX WARN: Code restructure failed: missing block: B:245:0x0463, code lost:
        r2 = r23.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:247:0x0478, code lost:
        if (r2 == 0) goto L257;
     */
    /* JADX WARN: Code restructure failed: missing block: B:249:0x047c, code lost:
        if (r2 != r17) goto L252;
     */
    /* JADX WARN: Code restructure failed: missing block: B:251:0x0492, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.beta.R.string.NotificationGroupInvitedYouToCall, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:252:0x0493, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:253:0x049f, code lost:
        if (r0 != null) goto L255;
     */
    /* JADX WARN: Code restructure failed: missing block: B:254:0x04a1, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:256:0x04bd, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.beta.R.string.NotificationGroupInvitedToCall, r1, r6.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:257:0x04be, code lost:
        r2 = new java.lang.StringBuilder();
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:259:0x04ce, code lost:
        if (r3 >= r23.messageOwner.action.users.size()) goto L796;
     */
    /* JADX WARN: Code restructure failed: missing block: B:260:0x04d0, code lost:
        r4 = getMessagesController().getUser(r23.messageOwner.action.users.get(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:261:0x04e4, code lost:
        if (r4 == null) goto L798;
     */
    /* JADX WARN: Code restructure failed: missing block: B:262:0x04e6, code lost:
        r4 = org.telegram.messenger.UserObject.getUserName(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:263:0x04ee, code lost:
        if (r2.length() == 0) goto L265;
     */
    /* JADX WARN: Code restructure failed: missing block: B:264:0x04f0, code lost:
        r2.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x04f5, code lost:
        r2.append(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x04f8, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:268:0x0515, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.beta.R.string.NotificationGroupInvitedToCall, r1, r6.title, r2.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x0519, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink) == false) goto L273;
     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x052f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.beta.R.string.NotificationInvitedToGroupByLink, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x0535, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle) == false) goto L277;
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x0548, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.beta.R.string.NotificationEditedGroupName, r1, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x054b, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto) != false) goto L656;
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x054f, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto) == false) goto L281;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x0555, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser) == false) goto L296;
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x0557, code lost:
        r2 = r5.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x055b, code lost:
        if (r2 != r17) goto L287;
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x0571, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.beta.R.string.NotificationGroupKickYou, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x0577, code lost:
        if (r2 != r8) goto L291;
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x058a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.beta.R.string.NotificationGroupLeftMember, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:291:0x058b, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r23.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x059d, code lost:
        if (r0 != null) goto L294;
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x059f, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x05bc, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.beta.R.string.NotificationGroupKickMember, r1, r6.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x05bf, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatCreate) == false) goto L300;
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x05c7, code lost:
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x05ca, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate) == false) goto L304;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x05d2, code lost:
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x05d5, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo) == false) goto L308;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x05e8, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.beta.R.string.ActionMigrateFromGroupNotify, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x05ed, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) == false) goto L312;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x05fe, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.beta.R.string.ActionMigrateFromGroupNotify, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x0601, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken) == false) goto L316;
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x0609, code lost:
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x060c, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPinMessage) == false) goto L638;
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x0612, code lost:
        if (r6 == null) goto L428;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x0618, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r6) == false) goto L324;
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x061c, code lost:
        if (r6.megagroup == false) goto L428;
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x061e, code lost:
        r0 = r23.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x0620, code lost:
        if (r0 != null) goto L328;
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x0636, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoText, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x063e, code lost:
        if (r0.isMusic() == false) goto L332;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x0651, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.beta.R.string.NotificationActionPinnedMusic, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x065b, code lost:
        if (r0.isVideo() == false) goto L342;
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x0661, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L340;
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x066b, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L340;
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x0692, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, r1, "📹 " + r0.messageOwner.message, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x06a7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.beta.R.string.NotificationActionPinnedVideo, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x06ac, code lost:
        if (r0.isGif() == false) goto L352;
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x06b2, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L350;
     */
    /* JADX WARN: Code restructure failed: missing block: B:347:0x06bc, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L350;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x06e3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, r1, "🎬 " + r0.messageOwner.message, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x06f8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.beta.R.string.NotificationActionPinnedGif, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x0700, code lost:
        if (r0.isVoice() == false) goto L356;
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x0713, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.beta.R.string.NotificationActionPinnedVoice, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x0718, code lost:
        if (r0.isRoundVideo() == false) goto L360;
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x072b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.beta.R.string.NotificationActionPinnedRound, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x0730, code lost:
        if (r0.isSticker() != false) goto L422;
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x0736, code lost:
        if (r0.isAnimatedSticker() == false) goto L364;
     */
    /* JADX WARN: Code restructure failed: missing block: B:364:0x073a, code lost:
        r4 = r0.messageOwner;
        r7 = r4.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x0740, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L374;
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x0746, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L372;
     */
    /* JADX WARN: Code restructure failed: missing block: B:369:0x074e, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L372;
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x0775, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, r1, "📎 " + r0.messageOwner.message, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x078a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.beta.R.string.NotificationActionPinnedFile, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:375:0x078d, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L420;
     */
    /* JADX WARN: Code restructure failed: missing block: B:377:0x0791, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L378;
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x0797, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L382;
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x07ad, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoLive, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x07b2, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L386;
     */
    /* JADX WARN: Code restructure failed: missing block: B:384:0x07b4, code lost:
        r7 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:385:0x07d3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.beta.R.string.NotificationActionPinnedContact2, r1, r6.title, org.telegram.messenger.ContactsController.formatName(r7.first_name, r7.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:387:0x07d6, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L394;
     */
    /* JADX WARN: Code restructure failed: missing block: B:388:0x07d8, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r7).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x07de, code lost:
        if (r0.quiz == false) goto L392;
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x07f9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.beta.R.string.NotificationActionPinnedQuiz2, r1, r6.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:393:0x0813, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.beta.R.string.NotificationActionPinnedPoll2, r1, r6.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:395:0x0816, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L404;
     */
    /* JADX WARN: Code restructure failed: missing block: B:397:0x081c, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L402;
     */
    /* JADX WARN: Code restructure failed: missing block: B:399:0x0824, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L402;
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x084b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, r1, "🖼 " + r0.messageOwner.message, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:403:0x0860, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.beta.R.string.NotificationActionPinnedPhoto, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:405:0x0866, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L408;
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x0879, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.beta.R.string.NotificationActionPinnedGame, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:408:0x087a, code lost:
        r4 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:409:0x087c, code lost:
        if (r4 == null) goto L418;
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x0882, code lost:
        if (r4.length() <= 0) goto L418;
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x0884, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x088a, code lost:
        if (r0.length() <= 20) goto L415;
     */
    /* JADX WARN: Code restructure failed: missing block: B:414:0x088c, code lost:
        r4 = new java.lang.StringBuilder();
        r7 = 0;
        r4.append((java.lang.Object) r0.subSequence(0, 20));
        r4.append("...");
        r0 = r4.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:415:0x08a1, code lost:
        r7 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:416:0x08a2, code lost:
        r2 = new java.lang.Object[3];
        r2[r7] = r1;
        r2[1] = r0;
        r2[2] = r6.title;
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x08b3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x08c8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoText, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x08dd, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeo, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:422:0x08de, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x08e4, code lost:
        if (r0 == null) goto L426;
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x08fb, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerEmoji, r1, r6.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x090e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.beta.R.string.NotificationActionPinnedSticker, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x0910, code lost:
        if (r6 == null) goto L534;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00a5, code lost:
        if (r11.getBoolean("EnablePreviewGroup", true) != false) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:430:0x0912, code lost:
        r0 = r23.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x0914, code lost:
        if (r0 != null) goto L434;
     */
    /* JADX WARN: Code restructure failed: missing block: B:433:0x0926, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoTextChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x092c, code lost:
        if (r0.isMusic() == false) goto L438;
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x093d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedMusicChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x0947, code lost:
        if (r0.isVideo() == false) goto L448;
     */
    /* JADX WARN: Code restructure failed: missing block: B:441:0x094d, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L446;
     */
    /* JADX WARN: Code restructure failed: missing block: B:443:0x0957, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L446;
     */
    /* JADX WARN: Code restructure failed: missing block: B:445:0x097b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, r6.title, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:447:0x098d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedVideoChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:449:0x0992, code lost:
        if (r0.isGif() == false) goto L458;
     */
    /* JADX WARN: Code restructure failed: missing block: B:451:0x0998, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L456;
     */
    /* JADX WARN: Code restructure failed: missing block: B:453:0x09a2, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L456;
     */
    /* JADX WARN: Code restructure failed: missing block: B:455:0x09c6, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, r6.title, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:0x09d8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGifChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x09df, code lost:
        if (r0.isVoice() == false) goto L462;
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x09f0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedVoiceChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x09f5, code lost:
        if (r0.isRoundVideo() == false) goto L466;
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x0a06, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedRoundChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x0a0b, code lost:
        if (r0.isSticker() != false) goto L528;
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x0a11, code lost:
        if (r0.isAnimatedSticker() == false) goto L470;
     */
    /* JADX WARN: Code restructure failed: missing block: B:470:0x0a15, code lost:
        r1 = r0.messageOwner;
        r7 = r1.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x0a1b, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L480;
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x0a21, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L478;
     */
    /* JADX WARN: Code restructure failed: missing block: B:475:0x0a29, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L478;
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x0a4d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, r6.title, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x0a5f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedFileChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00b3, code lost:
        if (r11.getBoolean("EnablePreviewChannel", r3) == false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x0a62, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L526;
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0a66, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L484;
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0a6c, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L488;
     */
    /* JADX WARN: Code restructure failed: missing block: B:487:0x0a7f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoLiveChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x0a83, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L492;
     */
    /* JADX WARN: Code restructure failed: missing block: B:490:0x0a85, code lost:
        r7 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x0aa2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.beta.R.string.NotificationActionPinnedContactChannel2, r6.title, org.telegram.messenger.ContactsController.formatName(r7.first_name, r7.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x0aa5, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L500;
     */
    /* JADX WARN: Code restructure failed: missing block: B:494:0x0aa7, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r7).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:0x0aad, code lost:
        if (r0.quiz == false) goto L498;
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x0ac5, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.beta.R.string.NotificationActionPinnedQuizChannel2, r6.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:499:0x0adc, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.beta.R.string.NotificationActionPinnedPollChannel2, r6.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:501:0x0adf, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L510;
     */
    /* JADX WARN: Code restructure failed: missing block: B:503:0x0ae5, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L508;
     */
    /* JADX WARN: Code restructure failed: missing block: B:505:0x0aed, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L508;
     */
    /* JADX WARN: Code restructure failed: missing block: B:507:0x0b11, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, r6.title, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:509:0x0b23, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedPhotoChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:511:0x0b28, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L514;
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x0b39, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGameChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:514:0x0b3a, code lost:
        r1 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x0b3c, code lost:
        if (r1 == null) goto L524;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0b42, code lost:
        if (r1.length() <= 0) goto L524;
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x0b44, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:519:0x0b4a, code lost:
        if (r0.length() <= 20) goto L521;
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x0b4c, code lost:
        r1 = new java.lang.StringBuilder();
        r8 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x0b61, code lost:
        r8 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:522:0x0b62, code lost:
        r1 = new java.lang.Object[2];
        r1[r8] = r6.title;
        r1[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x0b70, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:525:0x0b82, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoTextChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:527:0x0b94, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:528:0x0b95, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x0b9a, code lost:
        if (r0 == null) goto L532;
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x0baf, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerEmojiChannel, r6.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0bc0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:534:0x0bc1, code lost:
        r0 = r23.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0bc4, code lost:
        if (r0 != null) goto L538;
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0bd3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoTextUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0bd8, code lost:
        if (r0.isMusic() == false) goto L542;
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x0be7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedMusicUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:543:0x0bf1, code lost:
        if (r0.isVideo() == false) goto L552;
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x0bf7, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L550;
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0c01, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L550;
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0c23, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextUser, r1, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:551:0x0c33, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedVideoUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:553:0x0c38, code lost:
        if (r0.isGif() == false) goto L562;
     */
    /* JADX WARN: Code restructure failed: missing block: B:555:0x0c3e, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L560;
     */
    /* JADX WARN: Code restructure failed: missing block: B:557:0x0c48, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L560;
     */
    /* JADX WARN: Code restructure failed: missing block: B:559:0x0c6a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextUser, r1, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0c7a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedGifUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:563:0x0c81, code lost:
        if (r0.isVoice() == false) goto L566;
     */
    /* JADX WARN: Code restructure failed: missing block: B:565:0x0c90, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedVoiceUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:567:0x0c95, code lost:
        if (r0.isRoundVideo() == false) goto L570;
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x0ca4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedRoundUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:571:0x0ca9, code lost:
        if (r0.isSticker() != false) goto L632;
     */
    /* JADX WARN: Code restructure failed: missing block: B:573:0x0caf, code lost:
        if (r0.isAnimatedSticker() == false) goto L574;
     */
    /* JADX WARN: Code restructure failed: missing block: B:574:0x0cb3, code lost:
        r4 = r0.messageOwner;
        r7 = r4.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:575:0x0cb9, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L584;
     */
    /* JADX WARN: Code restructure failed: missing block: B:577:0x0cbf, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L582;
     */
    /* JADX WARN: Code restructure failed: missing block: B:579:0x0cc7, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L582;
     */
    /* JADX WARN: Code restructure failed: missing block: B:581:0x0ce9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextUser, r1, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:583:0x0cf9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedFileUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:585:0x0cfc, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L630;
     */
    /* JADX WARN: Code restructure failed: missing block: B:587:0x0d00, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L588;
     */
    /* JADX WARN: Code restructure failed: missing block: B:589:0x0d06, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L592;
     */
    /* JADX WARN: Code restructure failed: missing block: B:591:0x0d17, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoLiveUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:593:0x0d1b, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L596;
     */
    /* JADX WARN: Code restructure failed: missing block: B:594:0x0d1d, code lost:
        r7 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:595:0x0d38, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedContactUser, r1, org.telegram.messenger.ContactsController.formatName(r7.first_name, r7.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:597:0x0d3b, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L604;
     */
    /* JADX WARN: Code restructure failed: missing block: B:598:0x0d3d, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r7).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:599:0x0d43, code lost:
        if (r0.quiz == false) goto L602;
     */
    /* JADX WARN: Code restructure failed: missing block: B:601:0x0d59, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedQuizUser, r1, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:603:0x0d6e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedPollUser, r1, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:605:0x0d71, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L614;
     */
    /* JADX WARN: Code restructure failed: missing block: B:607:0x0d77, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L612;
     */
    /* JADX WARN: Code restructure failed: missing block: B:609:0x0d7f, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L612;
     */
    /* JADX WARN: Code restructure failed: missing block: B:611:0x0da1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextUser, r1, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:613:0x0db1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedPhotoUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:615:0x0db6, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L618;
     */
    /* JADX WARN: Code restructure failed: missing block: B:617:0x0dc5, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedGameUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:618:0x0dc6, code lost:
        r4 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:619:0x0dc8, code lost:
        if (r4 == null) goto L628;
     */
    /* JADX WARN: Code restructure failed: missing block: B:621:0x0dce, code lost:
        if (r4.length() <= 0) goto L628;
     */
    /* JADX WARN: Code restructure failed: missing block: B:622:0x0dd0, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:623:0x0dd6, code lost:
        if (r0.length() <= 20) goto L625;
     */
    /* JADX WARN: Code restructure failed: missing block: B:624:0x0dd8, code lost:
        r4 = new java.lang.StringBuilder();
        r7 = 0;
        r4.append((java.lang.Object) r0.subSequence(0, 20));
        r4.append("...");
        r0 = r4.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x0ded, code lost:
        r7 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:626:0x0dee, code lost:
        r2 = new java.lang.Object[2];
        r2[r7] = r1;
        r2[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:627:0x0dfa, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextUser, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:629:0x0e0a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoTextUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:0x0e1a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:632:0x0e1b, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:633:0x0e21, code lost:
        if (r0 == null) goto L636;
     */
    /* JADX WARN: Code restructure failed: missing block: B:635:0x0e33, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerEmojiUser, r1, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:637:0x0e41, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerUser, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:639:0x0e44, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) == false) goto L651;
     */
    /* JADX WARN: Code restructure failed: missing block: B:640:0x0e46, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) r5).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:641:0x0e4e, code lost:
        if (android.text.TextUtils.isEmpty(r0) == false) goto L646;
     */
    /* JADX WARN: Code restructure failed: missing block: B:643:0x0e52, code lost:
        if (r2 != r17) goto L645;
     */
    /* JADX WARN: Code restructure failed: missing block: B:647:0x0e78, code lost:
        if (r2 != r17) goto L649;
     */
    /* JADX WARN: Code restructure failed: missing block: B:650:0x0e98, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.beta.R.string.ChatThemeChangedTo, r1, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:652:0x0e9b, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest) == false) goto L655;
     */
    /* JADX WARN: Code restructure failed: missing block: B:654:0x0ea3, code lost:
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:655:0x0ea4, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:657:0x0eae, code lost:
        if (r4.peer_id.channel_id == 0) goto L666;
     */
    /* JADX WARN: Code restructure failed: missing block: B:659:0x0eb2, code lost:
        if (r6.megagroup != false) goto L666;
     */
    /* JADX WARN: Code restructure failed: missing block: B:661:0x0eb8, code lost:
        if (r23.isVideoAvatar() == false) goto L664;
     */
    /* JADX WARN: Code restructure failed: missing block: B:663:0x0ecb, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.beta.R.string.ChannelVideoEditNotification, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:665:0x0edd, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.beta.R.string.ChannelPhotoEditNotification, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:667:0x0ee3, code lost:
        if (r23.isVideoAvatar() == false) goto L670;
     */
    /* JADX WARN: Code restructure failed: missing block: B:669:0x0ef8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.beta.R.string.NotificationEditedGroupVideo, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:671:0x0f0c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.beta.R.string.NotificationEditedGroupPhoto, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:673:0x0f13, code lost:
        return r23.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:675:0x0f23, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationContactJoined", org.telegram.messenger.beta.R.string.NotificationContactJoined, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:677:0x0f28, code lost:
        if (r23.isMediaEmpty() == false) goto L684;
     */
    /* JADX WARN: Code restructure failed: missing block: B:679:0x0f32, code lost:
        if (android.text.TextUtils.isEmpty(r23.messageOwner.message) != false) goto L682;
     */
    /* JADX WARN: Code restructure failed: missing block: B:681:0x0f38, code lost:
        return replaceSpoilers(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:683:0x0f41, code lost:
        return org.telegram.messenger.LocaleController.getString(r13, org.telegram.messenger.beta.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:684:0x0f42, code lost:
        r1 = r13;
        r2 = r23.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:685:0x0f49, code lost:
        if ((r2.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L698;
     */
    /* JADX WARN: Code restructure failed: missing block: B:687:0x0f4f, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L692;
     */
    /* JADX WARN: Code restructure failed: missing block: B:689:0x0f57, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L692;
     */
    /* JADX WARN: Code restructure failed: missing block: B:691:0x0f6c, code lost:
        return "🖼 " + replaceSpoilers(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:693:0x0f73, code lost:
        if (r23.messageOwner.media.ttl_seconds == 0) goto L696;
     */
    /* JADX WARN: Code restructure failed: missing block: B:695:0x0f7e, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDestructingPhoto", org.telegram.messenger.beta.R.string.AttachDestructingPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:697:0x0f88, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachPhoto", org.telegram.messenger.beta.R.string.AttachPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:699:0x0f8d, code lost:
        if (r23.isVideo() == false) goto L712;
     */
    /* JADX WARN: Code restructure failed: missing block: B:701:0x0f93, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L706;
     */
    /* JADX WARN: Code restructure failed: missing block: B:703:0x0f9d, code lost:
        if (android.text.TextUtils.isEmpty(r23.messageOwner.message) != false) goto L706;
     */
    /* JADX WARN: Code restructure failed: missing block: B:705:0x0fb2, code lost:
        return "📹 " + replaceSpoilers(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:707:0x0fb9, code lost:
        if (r23.messageOwner.media.ttl_seconds == 0) goto L710;
     */
    /* JADX WARN: Code restructure failed: missing block: B:709:0x0fc4, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDestructingVideo", org.telegram.messenger.beta.R.string.AttachDestructingVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:711:0x0fce, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachVideo", org.telegram.messenger.beta.R.string.AttachVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:713:0x0fd3, code lost:
        if (r23.isGame() == false) goto L716;
     */
    /* JADX WARN: Code restructure failed: missing block: B:715:0x0fde, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachGame", org.telegram.messenger.beta.R.string.AttachGame);
     */
    /* JADX WARN: Code restructure failed: missing block: B:717:0x0fe3, code lost:
        if (r23.isVoice() == false) goto L720;
     */
    /* JADX WARN: Code restructure failed: missing block: B:719:0x0fee, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachAudio", org.telegram.messenger.beta.R.string.AttachAudio);
     */
    /* JADX WARN: Code restructure failed: missing block: B:721:0x0ff3, code lost:
        if (r23.isRoundVideo() == false) goto L724;
     */
    /* JADX WARN: Code restructure failed: missing block: B:723:0x0ffe, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachRound", org.telegram.messenger.beta.R.string.AttachRound);
     */
    /* JADX WARN: Code restructure failed: missing block: B:725:0x1003, code lost:
        if (r23.isMusic() == false) goto L728;
     */
    /* JADX WARN: Code restructure failed: missing block: B:727:0x100e, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachMusic", org.telegram.messenger.beta.R.string.AttachMusic);
     */
    /* JADX WARN: Code restructure failed: missing block: B:728:0x100f, code lost:
        r2 = r23.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:729:0x1015, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L732;
     */
    /* JADX WARN: Code restructure failed: missing block: B:731:0x1020, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachContact", org.telegram.messenger.beta.R.string.AttachContact);
     */
    /* JADX WARN: Code restructure failed: missing block: B:733:0x1023, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L740;
     */
    /* JADX WARN: Code restructure failed: missing block: B:735:0x102b, code lost:
        if (((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r2).poll.quiz == false) goto L738;
     */
    /* JADX WARN: Code restructure failed: missing block: B:737:0x1036, code lost:
        return org.telegram.messenger.LocaleController.getString("QuizPoll", org.telegram.messenger.beta.R.string.QuizPoll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:739:0x1040, code lost:
        return org.telegram.messenger.LocaleController.getString("Poll", org.telegram.messenger.beta.R.string.Poll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:741:0x1043, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L784;
     */
    /* JADX WARN: Code restructure failed: missing block: B:743:0x1047, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L744;
     */
    /* JADX WARN: Code restructure failed: missing block: B:745:0x104d, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L748;
     */
    /* JADX WARN: Code restructure failed: missing block: B:747:0x1058, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachLiveLocation", org.telegram.messenger.beta.R.string.AttachLiveLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:749:0x105b, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L778;
     */
    /* JADX WARN: Code restructure failed: missing block: B:751:0x1061, code lost:
        if (r23.isSticker() != false) goto L772;
     */
    /* JADX WARN: Code restructure failed: missing block: B:753:0x1067, code lost:
        if (r23.isAnimatedSticker() == false) goto L754;
     */
    /* JADX WARN: Code restructure failed: missing block: B:755:0x106e, code lost:
        if (r23.isGif() == false) goto L764;
     */
    /* JADX WARN: Code restructure failed: missing block: B:757:0x1074, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L762;
     */
    /* JADX WARN: Code restructure failed: missing block: B:759:0x107e, code lost:
        if (android.text.TextUtils.isEmpty(r23.messageOwner.message) != false) goto L762;
     */
    /* JADX WARN: Code restructure failed: missing block: B:761:0x1093, code lost:
        return "🎬 " + replaceSpoilers(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:763:0x109d, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachGif", org.telegram.messenger.beta.R.string.AttachGif);
     */
    /* JADX WARN: Code restructure failed: missing block: B:765:0x10a2, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L770;
     */
    /* JADX WARN: Code restructure failed: missing block: B:767:0x10ac, code lost:
        if (android.text.TextUtils.isEmpty(r23.messageOwner.message) != false) goto L770;
     */
    /* JADX WARN: Code restructure failed: missing block: B:769:0x10c1, code lost:
        return "📎 " + replaceSpoilers(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:771:0x10cb, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDocument", org.telegram.messenger.beta.R.string.AttachDocument);
     */
    /* JADX WARN: Code restructure failed: missing block: B:772:0x10cc, code lost:
        r0 = r23.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:773:0x10d0, code lost:
        if (r0 == null) goto L776;
     */
    /* JADX WARN: Code restructure failed: missing block: B:775:0x10ef, code lost:
        return r0 + " " + org.telegram.messenger.LocaleController.getString("AttachSticker", org.telegram.messenger.beta.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:777:0x10f9, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachSticker", org.telegram.messenger.beta.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:779:0x1100, code lost:
        if (android.text.TextUtils.isEmpty(r23.messageText) != false) goto L782;
     */
    /* JADX WARN: Code restructure failed: missing block: B:781:0x1106, code lost:
        return replaceSpoilers(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:783:0x110e, code lost:
        return org.telegram.messenger.LocaleController.getString(r1, org.telegram.messenger.beta.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:785:0x1118, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachLocation", org.telegram.messenger.beta.R.string.AttachLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:799:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.beta.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:800:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.beta.R.string.ChatThemeDisabled, r1, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:801:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChangedChatThemeYou", org.telegram.messenger.beta.R.string.ChatThemeChangedYou, r0);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getShortStringForMessage(MessageObject messageObject, String[] strArr, boolean[] zArr) {
        String str;
        String str2;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        TLRPC$Peer tLRPC$Peer;
        TLRPC$Chat chat;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader2;
        TLRPC$Peer tLRPC$Peer2;
        if (AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter) {
            return LocaleController.getString("NotificationHiddenMessage", org.telegram.messenger.beta.R.string.NotificationHiddenMessage);
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
        boolean z = notificationsSettings.getBoolean("content_preview_" + j, true);
        if (messageObject.isFcmMessage()) {
            if (j2 == 0 && j3 != 0) {
                if (Build.VERSION.SDK_INT > 27) {
                    strArr[0] = messageObject.localName;
                }
                if (!z || !notificationsSettings.getBoolean("EnablePreviewAll", true)) {
                    if (zArr != null) {
                        zArr[0] = false;
                    }
                    return LocaleController.getString("Message", org.telegram.messenger.beta.R.string.Message);
                }
            } else if (j2 != 0) {
                if (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup()) {
                    strArr[0] = messageObject.localUserName;
                } else if (Build.VERSION.SDK_INT > 27) {
                    strArr[0] = messageObject.localName;
                }
                if (z) {
                    boolean z2 = !messageObject.localChannel ? true : true;
                    if (messageObject.localChannel) {
                    }
                }
                if (zArr != null) {
                    zArr[0] = false;
                }
                return (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup()) ? LocaleController.formatString("NotificationMessageGroupNoText", org.telegram.messenger.beta.R.string.NotificationMessageGroupNoText, messageObject.localUserName, messageObject.localName) : LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.beta.R.string.ChannelMessageNoText, messageObject.localName);
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
                str2 = UserObject.getUserName(user);
                if (j2 != 0) {
                    strArr[0] = str2;
                } else if (Build.VERSION.SDK_INT > 27) {
                    strArr[0] = str2;
                } else {
                    strArr[0] = null;
                }
            } else {
                str2 = null;
            }
            str = "Message";
        } else {
            str = "Message";
            TLRPC$Chat chat2 = getMessagesController().getChat(Long.valueOf(-j3));
            if (chat2 != null) {
                str2 = chat2.title;
                strArr[0] = str2;
            } else {
                str2 = null;
            }
        }
        if (str2 != null && j3 > 0 && UserObject.isReplyUser(j) && (tLRPC$MessageFwdHeader = messageObject.messageOwner.fwd_from) != null && (tLRPC$Peer = tLRPC$MessageFwdHeader.saved_from_peer) != null) {
            long peerId = MessageObject.getPeerId(tLRPC$Peer);
            if (DialogObject.isChatDialog(peerId) && (chat = getMessagesController().getChat(Long.valueOf(-peerId))) != null) {
                str2 = str2 + " @ " + chat.title;
                if (strArr[0] != null) {
                    strArr[0] = str2;
                }
            }
        }
        if (str2 == null) {
            return null;
        }
        if (j2 != 0) {
            tLRPC$Chat = getMessagesController().getChat(Long.valueOf(j2));
            if (tLRPC$Chat == null) {
                return null;
            }
            if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup && Build.VERSION.SDK_INT <= 27) {
                strArr[0] = null;
            }
        } else {
            tLRPC$Chat = null;
        }
        if (DialogObject.isEncryptedDialog(j)) {
            strArr[0] = null;
            return LocaleController.getString("NotificationHiddenMessage", org.telegram.messenger.beta.R.string.NotificationHiddenMessage);
        }
        boolean z3 = ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup;
        if (z) {
            boolean z4 = (j2 != 0 || j3 == 0) ? true : true;
            if (j2 != 0) {
                if (!z3) {
                }
                if (z3) {
                }
            }
        }
        String str3 = str;
        if (zArr != null) {
            zArr[0] = false;
        }
        return LocaleController.getString(str3, org.telegram.messenger.beta.R.string.Message);
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

    /* JADX WARN: Code restructure failed: missing block: B:248:0x0614, code lost:
        if (r11.getBoolean("EnablePreviewGroup", true) == false) goto L250;
     */
    /* JADX WARN: Code restructure failed: missing block: B:252:0x0620, code lost:
        if (r11.getBoolean("EnablePreviewChannel", r10) != false) goto L253;
     */
    /* JADX WARN: Code restructure failed: missing block: B:253:0x0622, code lost:
        r5 = r27.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:254:0x0626, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageService) == false) goto L563;
     */
    /* JADX WARN: Code restructure failed: missing block: B:255:0x0628, code lost:
        r6 = r5.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:256:0x062c, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser) == false) goto L293;
     */
    /* JADX WARN: Code restructure failed: missing block: B:257:0x062e, code lost:
        r2 = r6.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:258:0x0634, code lost:
        if (r2 != 0) goto L262;
     */
    /* JADX WARN: Code restructure failed: missing block: B:260:0x063d, code lost:
        if (r6.users.size() != 1) goto L262;
     */
    /* JADX WARN: Code restructure failed: missing block: B:261:0x063f, code lost:
        r2 = r27.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:263:0x0654, code lost:
        if (r2 == 0) goto L282;
     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x065e, code lost:
        if (r27.messageOwner.peer_id.channel_id == 0) goto L269;
     */
    /* JADX WARN: Code restructure failed: missing block: B:267:0x0662, code lost:
        if (r4.megagroup != false) goto L269;
     */
    /* JADX WARN: Code restructure failed: missing block: B:268:0x0664, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.beta.R.string.ChannelAddedByNotification, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x067c, code lost:
        if (r2 != r17) goto L272;
     */
    /* JADX WARN: Code restructure failed: missing block: B:271:0x067e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.beta.R.string.NotificationInvitedToGroup, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x0694, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:273:0x06a0, code lost:
        if (r0 != null) goto L275;
     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x06a2, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x06a8, code lost:
        if (r8 != r0.id) goto L281;
     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x06ac, code lost:
        if (r4.megagroup == false) goto L280;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x06ae, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.beta.R.string.NotificationGroupAddSelfMega, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x06c4, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.beta.R.string.NotificationGroupAddSelf, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x06da, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.beta.R.string.NotificationGroupAddMember, r1, r4.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x06f7, code lost:
        r2 = new java.lang.StringBuilder();
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x0707, code lost:
        if (r3 >= r27.messageOwner.action.users.size()) goto L749;
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x0709, code lost:
        r5 = getMessagesController().getUser(r27.messageOwner.action.users.get(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x071d, code lost:
        if (r5 == null) goto L751;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x071f, code lost:
        r5 = org.telegram.messenger.UserObject.getUserName(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x0727, code lost:
        if (r2.length() == 0) goto L290;
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x0729, code lost:
        r2.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x072e, code lost:
        r2.append(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:291:0x0731, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x0734, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.beta.R.string.NotificationGroupAddMember, r1, r4.title, r2.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x0754, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCall) == false) goto L296;
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x076d, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled) == false) goto L299;
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x0779, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall) == false) goto L326;
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x077b, code lost:
        r2 = r6.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x0781, code lost:
        if (r2 != 0) goto L306;
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x078a, code lost:
        if (r6.users.size() != 1) goto L306;
     */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x078c, code lost:
        r2 = r27.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x07a1, code lost:
        if (r2 == 0) goto L315;
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x07a5, code lost:
        if (r2 != r17) goto L311;
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x07a7, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.beta.R.string.NotificationGroupInvitedYouToCall, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x07bd, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x07c9, code lost:
        if (r0 != null) goto L314;
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x07cb, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x07cd, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.beta.R.string.NotificationGroupInvitedToCall, r1, r4.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x07ea, code lost:
        r2 = new java.lang.StringBuilder();
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x07fa, code lost:
        if (r3 >= r27.messageOwner.action.users.size()) goto L752;
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x07fc, code lost:
        r5 = getMessagesController().getUser(r27.messageOwner.action.users.get(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x0810, code lost:
        if (r5 == null) goto L754;
     */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x0812, code lost:
        r5 = org.telegram.messenger.UserObject.getUserName(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x081a, code lost:
        if (r2.length() == 0) goto L323;
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x081c, code lost:
        r2.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x0821, code lost:
        r2.append(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x0824, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x0827, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.beta.R.string.NotificationGroupInvitedToCall, r1, r4.title, r2.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x0847, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink) == false) goto L329;
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x0861, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle) == false) goto L332;
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x0879, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto) != false) goto L551;
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x087d, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto) == false) goto L336;
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x0883, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser) == false) goto L348;
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x0885, code lost:
        r2 = r6.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x0889, code lost:
        if (r2 != r17) goto L341;
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x08a6, code lost:
        if (r2 != r8) goto L344;
     */
    /* JADX WARN: Code restructure failed: missing block: B:344:0x08bb, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r27.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x08cd, code lost:
        if (r0 != null) goto L347;
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x08cf, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x08ee, code lost:
        r8 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x08f1, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatCreate) == false) goto L351;
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x08fd, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate) == false) goto L354;
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x0909, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo) == false) goto L357;
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x0922, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) == false) goto L360;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x0937, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken) == false) goto L363;
     */
    /* JADX WARN: Code restructure failed: missing block: B:364:0x0943, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPinMessage) == false) goto L533;
     */
    /* JADX WARN: Code restructure failed: missing block: B:366:0x0949, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r4) == false) goto L451;
     */
    /* JADX WARN: Code restructure failed: missing block: B:368:0x094d, code lost:
        if (r4.megagroup == false) goto L369;
     */
    /* JADX WARN: Code restructure failed: missing block: B:369:0x0951, code lost:
        r1 = r27.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:370:0x0953, code lost:
        if (r1 != null) goto L372;
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x096e, code lost:
        if (r1.isMusic() == false) goto L375;
     */
    /* JADX WARN: Code restructure failed: missing block: B:374:0x0970, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedMusicChannel, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:376:0x098a, code lost:
        if (r1.isVideo() == false) goto L383;
     */
    /* JADX WARN: Code restructure failed: missing block: B:378:0x0990, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L382;
     */
    /* JADX WARN: Code restructure failed: missing block: B:380:0x099a, code lost:
        if (android.text.TextUtils.isEmpty(r1.messageOwner.message) != false) goto L382;
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x099c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, r4.title, "📹 " + r1.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:382:0x09c0, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedVideoChannel, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:384:0x09d7, code lost:
        if (r1.isGif() == false) goto L391;
     */
    /* JADX WARN: Code restructure failed: missing block: B:386:0x09dd, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L390;
     */
    /* JADX WARN: Code restructure failed: missing block: B:388:0x09e7, code lost:
        if (android.text.TextUtils.isEmpty(r1.messageOwner.message) != false) goto L390;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x09e9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, r4.title, "🎬 " + r1.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:390:0x0a0d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGifChannel, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x0a26, code lost:
        if (r1.isVoice() == false) goto L394;
     */
    /* JADX WARN: Code restructure failed: missing block: B:393:0x0a28, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedVoiceChannel, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:395:0x0a3d, code lost:
        if (r1.isRoundVideo() == false) goto L397;
     */
    /* JADX WARN: Code restructure failed: missing block: B:396:0x0a3f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedRoundChannel, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:0x0a54, code lost:
        if (r1.isSticker() != false) goto L447;
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x0a5a, code lost:
        if (r1.isAnimatedSticker() == false) goto L401;
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x0a5e, code lost:
        r2 = r1.messageOwner;
        r6 = r2.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:402:0x0a64, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L409;
     */
    /* JADX WARN: Code restructure failed: missing block: B:404:0x0a6a, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L408;
     */
    /* JADX WARN: Code restructure failed: missing block: B:406:0x0a72, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L408;
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x0a74, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, r4.title, "📎 " + r1.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:408:0x0a98, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedFileChannel, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:410:0x0aad, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L446;
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x0ab1, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L413;
     */
    /* JADX WARN: Code restructure failed: missing block: B:414:0x0ab7, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L416;
     */
    /* JADX WARN: Code restructure failed: missing block: B:415:0x0ab9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoLiveChannel, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x0ace, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L419;
     */
    /* JADX WARN: Code restructure failed: missing block: B:418:0x0ad0, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r27.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.beta.R.string.NotificationActionPinnedContactChannel2, r4.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:420:0x0af6, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L425;
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x0af8, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r6).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:422:0x0afe, code lost:
        if (r0.quiz == false) goto L424;
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x0b00, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.beta.R.string.NotificationActionPinnedQuizChannel2, r4.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:424:0x0b18, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.beta.R.string.NotificationActionPinnedPollChannel2, r4.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:426:0x0b32, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L433;
     */
    /* JADX WARN: Code restructure failed: missing block: B:428:0x0b38, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L432;
     */
    /* JADX WARN: Code restructure failed: missing block: B:430:0x0b40, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L432;
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x0b42, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, r4.title, "🖼 " + r1.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:432:0x0b66, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedPhotoChannel, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:434:0x0b7d, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L436;
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x0b7f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGameChannel, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:436:0x0b90, code lost:
        r0 = r1.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x0b92, code lost:
        if (r0 == null) goto L445;
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x0b98, code lost:
        if (r0.length() <= 0) goto L445;
     */
    /* JADX WARN: Code restructure failed: missing block: B:440:0x0b9a, code lost:
        r0 = r1.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:441:0x0ba2, code lost:
        if (r0.length() <= 20) goto L443;
     */
    /* JADX WARN: Code restructure failed: missing block: B:442:0x0ba4, code lost:
        r1 = new java.lang.StringBuilder();
        r6 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:443:0x0bbd, code lost:
        r6 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:444:0x0bbe, code lost:
        r1 = new java.lang.Object[2];
        r1[r6] = r4.title;
        r1[1] = r0;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:445:0x0bce, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoTextChannel, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:446:0x0be1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoChannel, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:447:0x0bf4, code lost:
        r0 = r1.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:448:0x0bf9, code lost:
        if (r0 == null) goto L450;
     */
    /* JADX WARN: Code restructure failed: missing block: B:449:0x0bfb, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerEmojiChannel, r4.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:450:0x0c10, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerChannel, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:451:0x0c22, code lost:
        r2 = r27.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:452:0x0c25, code lost:
        if (r2 != null) goto L454;
     */
    /* JADX WARN: Code restructure failed: missing block: B:455:0x0c42, code lost:
        if (r2.isMusic() == false) goto L457;
     */
    /* JADX WARN: Code restructure failed: missing block: B:456:0x0c44, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.beta.R.string.NotificationActionPinnedMusic, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:458:0x0c60, code lost:
        if (r2.isVideo() == false) goto L465;
     */
    /* JADX WARN: Code restructure failed: missing block: B:460:0x0c66, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L464;
     */
    /* JADX WARN: Code restructure failed: missing block: B:462:0x0c70, code lost:
        if (android.text.TextUtils.isEmpty(r2.messageOwner.message) != false) goto L464;
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0c72, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, r1, "📹 " + r2.messageOwner.message, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:464:0x0c99, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.beta.R.string.NotificationActionPinnedVideo, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:466:0x0cb3, code lost:
        if (r2.isGif() == false) goto L473;
     */
    /* JADX WARN: Code restructure failed: missing block: B:468:0x0cb9, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L472;
     */
    /* JADX WARN: Code restructure failed: missing block: B:470:0x0cc3, code lost:
        if (android.text.TextUtils.isEmpty(r2.messageOwner.message) != false) goto L472;
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x0cc5, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, r1, "🎬 " + r2.messageOwner.message, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:472:0x0cec, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.beta.R.string.NotificationActionPinnedGif, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:474:0x0d09, code lost:
        if (r2.isVoice() == false) goto L476;
     */
    /* JADX WARN: Code restructure failed: missing block: B:475:0x0d0b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.beta.R.string.NotificationActionPinnedVoice, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x0d22, code lost:
        if (r2.isRoundVideo() == false) goto L479;
     */
    /* JADX WARN: Code restructure failed: missing block: B:478:0x0d24, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.beta.R.string.NotificationActionPinnedRound, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:480:0x0d3b, code lost:
        if (r2.isSticker() != false) goto L529;
     */
    /* JADX WARN: Code restructure failed: missing block: B:482:0x0d41, code lost:
        if (r2.isAnimatedSticker() == false) goto L483;
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0d45, code lost:
        r3 = r2.messageOwner;
        r7 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:484:0x0d4b, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L491;
     */
    /* JADX WARN: Code restructure failed: missing block: B:486:0x0d51, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L490;
     */
    /* JADX WARN: Code restructure failed: missing block: B:488:0x0d59, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L490;
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x0d5b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, r1, "📎 " + r2.messageOwner.message, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:490:0x0d82, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.beta.R.string.NotificationActionPinnedFile, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x0d9a, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L528;
     */
    /* JADX WARN: Code restructure failed: missing block: B:494:0x0d9e, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L495;
     */
    /* JADX WARN: Code restructure failed: missing block: B:496:0x0da4, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L498;
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x0da6, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoLive, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:499:0x0dbe, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L501;
     */
    /* JADX WARN: Code restructure failed: missing block: B:500:0x0dc0, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r27.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.beta.R.string.NotificationActionPinnedContact2, r1, r4.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:502:0x0de9, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L507;
     */
    /* JADX WARN: Code restructure failed: missing block: B:503:0x0deb, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r7).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0df1, code lost:
        if (r0.quiz == false) goto L506;
     */
    /* JADX WARN: Code restructure failed: missing block: B:505:0x0df3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.beta.R.string.NotificationActionPinnedQuiz2, r1, r4.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:506:0x0e0e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.beta.R.string.NotificationActionPinnedPoll2, r1, r4.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x0e2b, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L515;
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x0e31, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L514;
     */
    /* JADX WARN: Code restructure failed: missing block: B:512:0x0e39, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L514;
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x0e3b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, r1, "🖼 " + r2.messageOwner.message, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:514:0x0e62, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.beta.R.string.NotificationActionPinnedPhoto, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:516:0x0e7d, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L518;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0e7f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.beta.R.string.NotificationActionPinnedGame, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x0e92, code lost:
        r0 = r2.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:519:0x0e94, code lost:
        if (r0 == null) goto L527;
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x0e9a, code lost:
        if (r0.length() <= 0) goto L527;
     */
    /* JADX WARN: Code restructure failed: missing block: B:522:0x0e9c, code lost:
        r0 = r2.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x0ea4, code lost:
        if (r0.length() <= 20) goto L525;
     */
    /* JADX WARN: Code restructure failed: missing block: B:524:0x0ea6, code lost:
        r2 = new java.lang.StringBuilder();
        r7 = 0;
        r2.append((java.lang.Object) r0.subSequence(0, 20));
        r2.append("...");
        r0 = r2.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:525:0x0ebf, code lost:
        r7 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:526:0x0ec0, code lost:
        r2 = new java.lang.Object[3];
        r2[r7] = r1;
        r2[1] = r0;
        r2[2] = r4.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:527:0x0ed3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoText, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:528:0x0ee9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeo, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x0eff, code lost:
        r0 = r2.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:530:0x0f05, code lost:
        if (r0 == null) goto L532;
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x0f07, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerEmoji, r1, r4.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:532:0x0f1e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.beta.R.string.NotificationActionPinnedSticker, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:534:0x0f34, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGameScore) == false) goto L536;
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0f40, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) == false) goto L548;
     */
    /* JADX WARN: Code restructure failed: missing block: B:538:0x0f42, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) r6).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0f4a, code lost:
        if (android.text.TextUtils.isEmpty(r0) == false) goto L544;
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x0f4e, code lost:
        if (r2 != r17) goto L543;
     */
    /* JADX WARN: Code restructure failed: missing block: B:542:0x0f50, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.beta.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:543:0x0f5e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.beta.R.string.ChatThemeDisabled, r1, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x0f76, code lost:
        if (r2 != r17) goto L547;
     */
    /* JADX WARN: Code restructure failed: missing block: B:546:0x0f78, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeYou", org.telegram.messenger.beta.R.string.ChatThemeChangedYou, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0f87, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.beta.R.string.ChatThemeChangedTo, r1, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0f9b, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest) == false) goto L745;
     */
    /* JADX WARN: Code restructure failed: missing block: B:552:0x0fad, code lost:
        if (r5.peer_id.channel_id == 0) goto L559;
     */
    /* JADX WARN: Code restructure failed: missing block: B:554:0x0fb1, code lost:
        if (r4.megagroup != false) goto L559;
     */
    /* JADX WARN: Code restructure failed: missing block: B:556:0x0fb7, code lost:
        if (r27.isVideoAvatar() == false) goto L558;
     */
    /* JADX WARN: Code restructure failed: missing block: B:560:0x0fe4, code lost:
        if (r27.isVideoAvatar() == false) goto L562;
     */
    /* JADX WARN: Code restructure failed: missing block: B:564:0x1014, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r4) == false) goto L650;
     */
    /* JADX WARN: Code restructure failed: missing block: B:566:0x1018, code lost:
        if (r4.megagroup != false) goto L650;
     */
    /* JADX WARN: Code restructure failed: missing block: B:568:0x101e, code lost:
        if (r27.isMediaEmpty() == false) goto L574;
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x1020, code lost:
        if (r28 != false) goto L573;
     */
    /* JADX WARN: Code restructure failed: missing block: B:571:0x102a, code lost:
        if (android.text.TextUtils.isEmpty(r27.messageOwner.message) != false) goto L573;
     */
    /* JADX WARN: Code restructure failed: missing block: B:572:0x102c, code lost:
        r13 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, r1, r27.messageOwner.message);
        r29[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:574:0x1055, code lost:
        r2 = r27.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:575:0x105b, code lost:
        if ((r2.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L583;
     */
    /* JADX WARN: Code restructure failed: missing block: B:576:0x105d, code lost:
        if (r28 != false) goto L582;
     */
    /* JADX WARN: Code restructure failed: missing block: B:578:0x1063, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L582;
     */
    /* JADX WARN: Code restructure failed: missing block: B:580:0x106b, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L582;
     */
    /* JADX WARN: Code restructure failed: missing block: B:581:0x106d, code lost:
        r13 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, r1, "🖼 " + r27.messageOwner.message);
        r29[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:584:0x10a9, code lost:
        if (r27.isVideo() == false) goto L592;
     */
    /* JADX WARN: Code restructure failed: missing block: B:585:0x10ab, code lost:
        if (r28 != false) goto L591;
     */
    /* JADX WARN: Code restructure failed: missing block: B:587:0x10b1, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L591;
     */
    /* JADX WARN: Code restructure failed: missing block: B:589:0x10bb, code lost:
        if (android.text.TextUtils.isEmpty(r27.messageOwner.message) != false) goto L591;
     */
    /* JADX WARN: Code restructure failed: missing block: B:590:0x10bd, code lost:
        r13 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, r1, "📹 " + r27.messageOwner.message);
        r29[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:593:0x10fb, code lost:
        if (r27.isVoice() == false) goto L595;
     */
    /* JADX WARN: Code restructure failed: missing block: B:596:0x1110, code lost:
        if (r27.isRoundVideo() == false) goto L598;
     */
    /* JADX WARN: Code restructure failed: missing block: B:599:0x1125, code lost:
        if (r27.isMusic() == false) goto L601;
     */
    /* JADX WARN: Code restructure failed: missing block: B:601:0x1136, code lost:
        r2 = r27.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:602:0x113c, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L604;
     */
    /* JADX WARN: Code restructure failed: missing block: B:603:0x113e, code lost:
        r2 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:605:0x115d, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L610;
     */
    /* JADX WARN: Code restructure failed: missing block: B:606:0x115f, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r2).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:607:0x1165, code lost:
        if (r0.quiz == false) goto L609;
     */
    /* JADX WARN: Code restructure failed: missing block: B:608:0x1167, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageQuiz2", org.telegram.messenger.beta.R.string.ChannelMessageQuiz2, r1, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:609:0x117d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessagePoll2", org.telegram.messenger.beta.R.string.ChannelMessagePoll2, r1, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:611:0x1195, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L649;
     */
    /* JADX WARN: Code restructure failed: missing block: B:613:0x1199, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L614;
     */
    /* JADX WARN: Code restructure failed: missing block: B:615:0x119f, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L617;
     */
    /* JADX WARN: Code restructure failed: missing block: B:618:0x11b4, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L643;
     */
    /* JADX WARN: Code restructure failed: missing block: B:620:0x11ba, code lost:
        if (r27.isSticker() != false) goto L639;
     */
    /* JADX WARN: Code restructure failed: missing block: B:622:0x11c0, code lost:
        if (r27.isAnimatedSticker() == false) goto L623;
     */
    /* JADX WARN: Code restructure failed: missing block: B:624:0x11c8, code lost:
        if (r27.isGif() == false) goto L632;
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x11ca, code lost:
        if (r28 != false) goto L631;
     */
    /* JADX WARN: Code restructure failed: missing block: B:627:0x11d0, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L631;
     */
    /* JADX WARN: Code restructure failed: missing block: B:629:0x11da, code lost:
        if (android.text.TextUtils.isEmpty(r27.messageOwner.message) != false) goto L631;
     */
    /* JADX WARN: Code restructure failed: missing block: B:630:0x11dc, code lost:
        r13 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, r1, "🎬 " + r27.messageOwner.message);
        r29[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:632:0x1214, code lost:
        if (r28 != false) goto L638;
     */
    /* JADX WARN: Code restructure failed: missing block: B:634:0x121a, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L638;
     */
    /* JADX WARN: Code restructure failed: missing block: B:636:0x1224, code lost:
        if (android.text.TextUtils.isEmpty(r27.messageOwner.message) != false) goto L638;
     */
    /* JADX WARN: Code restructure failed: missing block: B:637:0x1226, code lost:
        r13 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, r1, "📎 " + r27.messageOwner.message);
        r29[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:639:0x125e, code lost:
        r0 = r27.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:640:0x1264, code lost:
        if (r0 == null) goto L642;
     */
    /* JADX WARN: Code restructure failed: missing block: B:641:0x1266, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageStickerEmoji", org.telegram.messenger.beta.R.string.ChannelMessageStickerEmoji, r1, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:642:0x1278, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageSticker", org.telegram.messenger.beta.R.string.ChannelMessageSticker, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:644:0x1288, code lost:
        if (r28 != false) goto L648;
     */
    /* JADX WARN: Code restructure failed: missing block: B:646:0x1290, code lost:
        if (android.text.TextUtils.isEmpty(r27.messageText) != false) goto L648;
     */
    /* JADX WARN: Code restructure failed: missing block: B:647:0x1292, code lost:
        r13 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, r1, r27.messageText);
        r29[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:651:0x12d1, code lost:
        if (r27.isMediaEmpty() == false) goto L657;
     */
    /* JADX WARN: Code restructure failed: missing block: B:652:0x12d3, code lost:
        if (r28 != false) goto L656;
     */
    /* JADX WARN: Code restructure failed: missing block: B:654:0x12dd, code lost:
        if (android.text.TextUtils.isEmpty(r27.messageOwner.message) != false) goto L656;
     */
    /* JADX WARN: Code restructure failed: missing block: B:657:0x130d, code lost:
        r2 = r27.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:658:0x1315, code lost:
        if ((r2.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L666;
     */
    /* JADX WARN: Code restructure failed: missing block: B:659:0x1317, code lost:
        if (r28 != false) goto L665;
     */
    /* JADX WARN: Code restructure failed: missing block: B:661:0x131d, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L665;
     */
    /* JADX WARN: Code restructure failed: missing block: B:663:0x1325, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L665;
     */
    /* JADX WARN: Code restructure failed: missing block: B:667:0x1368, code lost:
        if (r27.isVideo() == false) goto L675;
     */
    /* JADX WARN: Code restructure failed: missing block: B:668:0x136a, code lost:
        if (r28 != false) goto L674;
     */
    /* JADX WARN: Code restructure failed: missing block: B:670:0x1370, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L674;
     */
    /* JADX WARN: Code restructure failed: missing block: B:672:0x137a, code lost:
        if (android.text.TextUtils.isEmpty(r27.messageOwner.message) != false) goto L674;
     */
    /* JADX WARN: Code restructure failed: missing block: B:676:0x13c0, code lost:
        if (r27.isVoice() == false) goto L678;
     */
    /* JADX WARN: Code restructure failed: missing block: B:679:0x13d9, code lost:
        if (r27.isRoundVideo() == false) goto L681;
     */
    /* JADX WARN: Code restructure failed: missing block: B:682:0x13f2, code lost:
        if (r27.isMusic() == false) goto L684;
     */
    /* JADX WARN: Code restructure failed: missing block: B:684:0x1407, code lost:
        r2 = r27.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:685:0x140d, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L687;
     */
    /* JADX WARN: Code restructure failed: missing block: B:686:0x140f, code lost:
        r2 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:688:0x1434, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L693;
     */
    /* JADX WARN: Code restructure failed: missing block: B:689:0x1436, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r2).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:690:0x143c, code lost:
        if (r0.quiz == false) goto L692;
     */
    /* JADX WARN: Code restructure failed: missing block: B:691:0x143e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupQuiz2", org.telegram.messenger.beta.R.string.NotificationMessageGroupQuiz2, r1, r4.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:692:0x1459, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPoll2", org.telegram.messenger.beta.R.string.NotificationMessageGroupPoll2, r1, r4.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:694:0x1476, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L696;
     */
    /* JADX WARN: Code restructure failed: missing block: B:697:0x1497, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L734;
     */
    /* JADX WARN: Code restructure failed: missing block: B:699:0x149b, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L700;
     */
    /* JADX WARN: Code restructure failed: missing block: B:701:0x14a1, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L703;
     */
    /* JADX WARN: Code restructure failed: missing block: B:704:0x14bb, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L729;
     */
    /* JADX WARN: Code restructure failed: missing block: B:706:0x14c1, code lost:
        if (r27.isSticker() != false) goto L725;
     */
    /* JADX WARN: Code restructure failed: missing block: B:708:0x14c7, code lost:
        if (r27.isAnimatedSticker() == false) goto L709;
     */
    /* JADX WARN: Code restructure failed: missing block: B:710:0x14cf, code lost:
        if (r27.isGif() == false) goto L718;
     */
    /* JADX WARN: Code restructure failed: missing block: B:711:0x14d1, code lost:
        if (r28 != false) goto L717;
     */
    /* JADX WARN: Code restructure failed: missing block: B:713:0x14d7, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L717;
     */
    /* JADX WARN: Code restructure failed: missing block: B:715:0x14e1, code lost:
        if (android.text.TextUtils.isEmpty(r27.messageOwner.message) != false) goto L717;
     */
    /* JADX WARN: Code restructure failed: missing block: B:718:0x1520, code lost:
        if (r28 != false) goto L724;
     */
    /* JADX WARN: Code restructure failed: missing block: B:720:0x1526, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L724;
     */
    /* JADX WARN: Code restructure failed: missing block: B:722:0x1530, code lost:
        if (android.text.TextUtils.isEmpty(r27.messageOwner.message) != false) goto L724;
     */
    /* JADX WARN: Code restructure failed: missing block: B:725:0x156f, code lost:
        r0 = r27.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x1575, code lost:
        if (r0 == null) goto L728;
     */
    /* JADX WARN: Code restructure failed: missing block: B:727:0x1577, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupStickerEmoji", org.telegram.messenger.beta.R.string.NotificationMessageGroupStickerEmoji, r1, r4.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:728:0x158e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupSticker", org.telegram.messenger.beta.R.string.NotificationMessageGroupSticker, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:729:0x15a2, code lost:
        if (r28 != false) goto L733;
     */
    /* JADX WARN: Code restructure failed: missing block: B:731:0x15aa, code lost:
        if (android.text.TextUtils.isEmpty(r27.messageText) != false) goto L733;
     */
    /* JADX WARN: Code restructure failed: missing block: B:788:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.beta.R.string.NotificationGroupCreatedCall, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:789:?, code lost:
        return r27.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:790:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.beta.R.string.NotificationInvitedToGroupByLink, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:791:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.beta.R.string.NotificationEditedGroupName, r1, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:792:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.beta.R.string.NotificationGroupKickYou, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:793:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.beta.R.string.NotificationGroupLeftMember, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:794:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.beta.R.string.NotificationGroupKickMember, r1, r4.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:795:?, code lost:
        return r27.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:796:?, code lost:
        return r27.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:797:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.beta.R.string.ActionMigrateFromGroupNotify, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:798:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.beta.R.string.ActionMigrateFromGroupNotify, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:799:?, code lost:
        return r27.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:800:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoTextChannel, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:801:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoText, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:802:?, code lost:
        return r27.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:803:?, code lost:
        return r27.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:804:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.beta.R.string.ChannelVideoEditNotification, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:805:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.beta.R.string.ChannelPhotoEditNotification, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:806:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.beta.R.string.NotificationEditedGroupVideo, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:807:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.beta.R.string.NotificationEditedGroupPhoto, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:808:?, code lost:
        return r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:809:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.beta.R.string.ChannelMessageNoText, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:810:?, code lost:
        return r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:811:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessagePhoto", org.telegram.messenger.beta.R.string.ChannelMessagePhoto, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:812:?, code lost:
        return r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:813:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageVideo", org.telegram.messenger.beta.R.string.ChannelMessageVideo, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:814:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageAudio", org.telegram.messenger.beta.R.string.ChannelMessageAudio, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:815:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageRound", org.telegram.messenger.beta.R.string.ChannelMessageRound, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:816:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageMusic", org.telegram.messenger.beta.R.string.ChannelMessageMusic, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:817:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageContact2", org.telegram.messenger.beta.R.string.ChannelMessageContact2, r1, org.telegram.messenger.ContactsController.formatName(r2.first_name, r2.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:818:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageLiveLocation", org.telegram.messenger.beta.R.string.ChannelMessageLiveLocation, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:819:?, code lost:
        return r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:820:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageGIF", org.telegram.messenger.beta.R.string.ChannelMessageGIF, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:821:?, code lost:
        return r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:822:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageDocument", org.telegram.messenger.beta.R.string.ChannelMessageDocument, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:823:?, code lost:
        return r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:824:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.beta.R.string.ChannelMessageNoText, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:825:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageMap", org.telegram.messenger.beta.R.string.ChannelMessageMap, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:826:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.beta.R.string.NotificationMessageGroupText, r1, r4.title, r27.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:827:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupNoText", org.telegram.messenger.beta.R.string.NotificationMessageGroupNoText, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:828:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.beta.R.string.NotificationMessageGroupText, r1, r4.title, "🖼 " + r27.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:829:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPhoto", org.telegram.messenger.beta.R.string.NotificationMessageGroupPhoto, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:830:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.beta.R.string.NotificationMessageGroupText, r1, r4.title, "📹 " + r27.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:831:?, code lost:
        return org.telegram.messenger.LocaleController.formatString(" ", org.telegram.messenger.beta.R.string.NotificationMessageGroupVideo, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:832:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupAudio", org.telegram.messenger.beta.R.string.NotificationMessageGroupAudio, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:833:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupRound", org.telegram.messenger.beta.R.string.NotificationMessageGroupRound, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:834:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMusic", org.telegram.messenger.beta.R.string.NotificationMessageGroupMusic, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:835:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupContact2", org.telegram.messenger.beta.R.string.NotificationMessageGroupContact2, r1, r4.title, org.telegram.messenger.ContactsController.formatName(r2.first_name, r2.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:836:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGame", org.telegram.messenger.beta.R.string.NotificationMessageGroupGame, r1, r4.title, r2.game.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:837:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupLiveLocation", org.telegram.messenger.beta.R.string.NotificationMessageGroupLiveLocation, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:838:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.beta.R.string.NotificationMessageGroupText, r1, r4.title, "🎬 " + r27.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:839:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGif", org.telegram.messenger.beta.R.string.NotificationMessageGroupGif, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:840:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.beta.R.string.NotificationMessageGroupText, r1, r4.title, "📎 " + r27.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:841:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupDocument", org.telegram.messenger.beta.R.string.NotificationMessageGroupDocument, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:842:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.beta.R.string.NotificationMessageGroupText, r1, r4.title, r27.messageText);
     */
    /* JADX WARN: Code restructure failed: missing block: B:843:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupNoText", org.telegram.messenger.beta.R.string.NotificationMessageGroupNoText, r1, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:844:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMap", org.telegram.messenger.beta.R.string.NotificationMessageGroupMap, r1, r4.title);
     */
    /* JADX WARN: Removed duplicated region for block: B:246:0x060b  */
    /* JADX WARN: Removed duplicated region for block: B:737:0x15ee  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x014b A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x014c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getStringForMessage(MessageObject messageObject, boolean z, boolean[] zArr, boolean[] zArr2) {
        String str;
        TLRPC$Chat tLRPC$Chat;
        String str2;
        boolean z2;
        TLRPC$Chat tLRPC$Chat2;
        String formatString;
        boolean z3;
        char c;
        String str3;
        if (AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter) {
            return LocaleController.getString("YouHaveNewMessage", org.telegram.messenger.beta.R.string.YouHaveNewMessage);
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
        boolean z4 = notificationsSettings.getBoolean("content_preview_" + j, true);
        if (messageObject.isFcmMessage()) {
            if (j2 == 0 && j3 != 0) {
                if (!z4 || !notificationsSettings.getBoolean("EnablePreviewAll", true)) {
                    if (zArr2 != null) {
                        zArr2[0] = false;
                    }
                    return LocaleController.formatString("NotificationMessageNoText", org.telegram.messenger.beta.R.string.NotificationMessageNoText, messageObject.localName);
                }
            } else if (j2 != 0 && (!z4 || ((!messageObject.localChannel && !notificationsSettings.getBoolean("EnablePreviewGroup", true)) || (messageObject.localChannel && !notificationsSettings.getBoolean("EnablePreviewChannel", true))))) {
                if (zArr2 != null) {
                    zArr2[0] = false;
                }
                return (messageObject.messageOwner.peer_id.channel_id == 0 || messageObject.isSupergroup()) ? LocaleController.formatString("NotificationMessageGroupNoText", org.telegram.messenger.beta.R.string.NotificationMessageGroupNoText, messageObject.localUserName, messageObject.localName) : LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.beta.R.string.ChannelMessageNoText, messageObject.localName);
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
                str = LocaleController.getString("MessageScheduledReminderNotification", org.telegram.messenger.beta.R.string.MessageScheduledReminderNotification);
            } else {
                str = LocaleController.getString("NotificationMessageScheduledName", org.telegram.messenger.beta.R.string.NotificationMessageScheduledName);
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
                return LocaleController.getString("YouHaveNewMessage", org.telegram.messenger.beta.R.string.YouHaveNewMessage);
            }
            TLRPC$Chat tLRPC$Chat3 = tLRPC$Chat;
            if (j2 != 0 || j3 == 0) {
                if (j2 != 0) {
                    if (ChatObject.isChannel(tLRPC$Chat3)) {
                        tLRPC$Chat2 = tLRPC$Chat3;
                        if (!tLRPC$Chat2.megagroup) {
                            z2 = true;
                            if (z4) {
                                boolean z5 = !z2 ? true : true;
                                if (z2) {
                                }
                            }
                            if (zArr2 != null) {
                                zArr2[0] = false;
                            }
                            return (ChatObject.isChannel(tLRPC$Chat2) || tLRPC$Chat2.megagroup) ? LocaleController.formatString("NotificationMessageGroupNoText", org.telegram.messenger.beta.R.string.NotificationMessageGroupNoText, str, tLRPC$Chat2.title) : LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.beta.R.string.ChannelMessageNoText, str);
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
                str2 = null;
            } else if (z4 && notificationsSettings.getBoolean("EnablePreviewAll", true)) {
                TLRPC$Message tLRPC$Message2 = messageObject.messageOwner;
                if (tLRPC$Message2 instanceof TLRPC$TL_messageService) {
                    TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message2.action;
                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGeoProximityReached) {
                        return messageObject.messageText.toString();
                    }
                    if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserJoined) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp)) {
                        return LocaleController.formatString("NotificationContactJoined", org.telegram.messenger.beta.R.string.NotificationContactJoined, str);
                    }
                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserUpdatedPhoto) {
                        return LocaleController.formatString("NotificationContactNewPhoto", org.telegram.messenger.beta.R.string.NotificationContactNewPhoto, str);
                    }
                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionLoginUnknownLocation) {
                        String formatString2 = LocaleController.formatString("formatDateAtTime", org.telegram.messenger.beta.R.string.formatDateAtTime, LocaleController.getInstance().formatterYear.format(messageObject.messageOwner.date * 1000), LocaleController.getInstance().formatterDay.format(messageObject.messageOwner.date * 1000));
                        TLRPC$MessageAction tLRPC$MessageAction2 = messageObject.messageOwner.action;
                        return LocaleController.formatString("NotificationUnrecognizedDevice", org.telegram.messenger.beta.R.string.NotificationUnrecognizedDevice, getUserConfig().getCurrentUser().first_name, formatString2, tLRPC$MessageAction2.title, tLRPC$MessageAction2.address);
                    } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionGameScore) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPaymentSent)) {
                        return messageObject.messageText.toString();
                    } else {
                        if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPhoneCall) {
                            if (tLRPC$MessageAction.video) {
                                return LocaleController.getString("CallMessageVideoIncomingMissed", org.telegram.messenger.beta.R.string.CallMessageVideoIncomingMissed);
                            }
                            return LocaleController.getString("CallMessageIncomingMissed", org.telegram.messenger.beta.R.string.CallMessageIncomingMissed);
                        }
                        if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatTheme) {
                            String str4 = ((TLRPC$TL_messageActionSetChatTheme) tLRPC$MessageAction).emoticon;
                            if (!TextUtils.isEmpty(str4)) {
                                c = 0;
                                z3 = true;
                                if (j == clientUserId) {
                                    str3 = LocaleController.formatString("ChangedChatThemeYou", org.telegram.messenger.beta.R.string.ChatThemeChangedYou, str4);
                                } else {
                                    str3 = LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.beta.R.string.ChatThemeChangedTo, str, str4);
                                }
                            } else if (j == clientUserId) {
                                c = 0;
                                str3 = LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.beta.R.string.ChatThemeDisabledYou, new Object[0]);
                                z3 = true;
                            } else {
                                c = 0;
                                z3 = true;
                                str3 = LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.beta.R.string.ChatThemeDisabled, str, str4);
                            }
                            String str5 = str3;
                            zArr[c] = z3;
                            return str5;
                        }
                        str2 = null;
                    }
                } else if (messageObject.isMediaEmpty()) {
                    if (!z) {
                        if (!TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            String formatString3 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, str, messageObject.messageOwner.message);
                            zArr[0] = true;
                            return formatString3;
                        }
                        return LocaleController.formatString("NotificationMessageNoText", org.telegram.messenger.beta.R.string.NotificationMessageNoText, str);
                    }
                    return LocaleController.formatString("NotificationMessageNoText", org.telegram.messenger.beta.R.string.NotificationMessageNoText, str);
                } else {
                    TLRPC$Message tLRPC$Message3 = messageObject.messageOwner;
                    if (tLRPC$Message3.media instanceof TLRPC$TL_messageMediaPhoto) {
                        if (!z && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(tLRPC$Message3.message)) {
                            String formatString4 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, str, "🖼 " + messageObject.messageOwner.message);
                            zArr[0] = true;
                            return formatString4;
                        } else if (messageObject.messageOwner.media.ttl_seconds != 0) {
                            return LocaleController.formatString("NotificationMessageSDPhoto", org.telegram.messenger.beta.R.string.NotificationMessageSDPhoto, str);
                        } else {
                            return LocaleController.formatString("NotificationMessagePhoto", org.telegram.messenger.beta.R.string.NotificationMessagePhoto, str);
                        }
                    } else if (messageObject.isVideo()) {
                        if (!z && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            String formatString5 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, str, "📹 " + messageObject.messageOwner.message);
                            zArr[0] = true;
                            return formatString5;
                        } else if (messageObject.messageOwner.media.ttl_seconds != 0) {
                            return LocaleController.formatString("NotificationMessageSDVideo", org.telegram.messenger.beta.R.string.NotificationMessageSDVideo, str);
                        } else {
                            return LocaleController.formatString("NotificationMessageVideo", org.telegram.messenger.beta.R.string.NotificationMessageVideo, str);
                        }
                    } else if (messageObject.isGame()) {
                        return LocaleController.formatString("NotificationMessageGame", org.telegram.messenger.beta.R.string.NotificationMessageGame, str, messageObject.messageOwner.media.game.title);
                    } else {
                        if (messageObject.isVoice()) {
                            return LocaleController.formatString("NotificationMessageAudio", org.telegram.messenger.beta.R.string.NotificationMessageAudio, str);
                        }
                        if (messageObject.isRoundVideo()) {
                            return LocaleController.formatString("NotificationMessageRound", org.telegram.messenger.beta.R.string.NotificationMessageRound, str);
                        }
                        if (messageObject.isMusic()) {
                            return LocaleController.formatString("NotificationMessageMusic", org.telegram.messenger.beta.R.string.NotificationMessageMusic, str);
                        }
                        TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
                        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaContact) {
                            TLRPC$TL_messageMediaContact tLRPC$TL_messageMediaContact = (TLRPC$TL_messageMediaContact) tLRPC$MessageMedia;
                            return LocaleController.formatString("NotificationMessageContact2", org.telegram.messenger.beta.R.string.NotificationMessageContact2, str, ContactsController.formatName(tLRPC$TL_messageMediaContact.first_name, tLRPC$TL_messageMediaContact.last_name));
                        }
                        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                            TLRPC$Poll tLRPC$Poll = ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia).poll;
                            formatString = tLRPC$Poll.quiz ? LocaleController.formatString("NotificationMessageQuiz2", org.telegram.messenger.beta.R.string.NotificationMessageQuiz2, str, tLRPC$Poll.question) : LocaleController.formatString("NotificationMessagePoll2", org.telegram.messenger.beta.R.string.NotificationMessagePoll2, str, tLRPC$Poll.question);
                        } else if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeo) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaVenue)) {
                            return LocaleController.formatString("NotificationMessageMap", org.telegram.messenger.beta.R.string.NotificationMessageMap, str);
                        } else {
                            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeoLive) {
                                return LocaleController.formatString("NotificationMessageLiveLocation", org.telegram.messenger.beta.R.string.NotificationMessageLiveLocation, str);
                            }
                            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                                if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {
                                    String stickerEmoji = messageObject.getStickerEmoji();
                                    if (stickerEmoji != null) {
                                        formatString = LocaleController.formatString("NotificationMessageStickerEmoji", org.telegram.messenger.beta.R.string.NotificationMessageStickerEmoji, str, stickerEmoji);
                                    } else {
                                        formatString = LocaleController.formatString("NotificationMessageSticker", org.telegram.messenger.beta.R.string.NotificationMessageSticker, str);
                                    }
                                } else if (messageObject.isGif()) {
                                    if (!z && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                        String formatString6 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, str, "🎬 " + messageObject.messageOwner.message);
                                        zArr[0] = true;
                                        return formatString6;
                                    }
                                    return LocaleController.formatString("NotificationMessageGif", org.telegram.messenger.beta.R.string.NotificationMessageGif, str);
                                } else if (!z && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                    String formatString7 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, str, "📎 " + messageObject.messageOwner.message);
                                    zArr[0] = true;
                                    return formatString7;
                                } else {
                                    return LocaleController.formatString("NotificationMessageDocument", org.telegram.messenger.beta.R.string.NotificationMessageDocument, str);
                                }
                            } else if (!z && !TextUtils.isEmpty(messageObject.messageText)) {
                                String formatString8 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, str, messageObject.messageText);
                                zArr[0] = true;
                                return formatString8;
                            } else {
                                return LocaleController.formatString("NotificationMessageNoText", org.telegram.messenger.beta.R.string.NotificationMessageNoText, str);
                            }
                        }
                        return formatString;
                    }
                }
            } else {
                if (zArr2 != null) {
                    zArr2[0] = false;
                }
                return LocaleController.formatString("NotificationMessageNoText", org.telegram.messenger.beta.R.string.NotificationMessageNoText, str);
            }
            return str2;
        }
        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j3));
        if (chat != null) {
            str = chat.title;
            if (str == null) {
            }
        }
        str = null;
        if (str == null) {
        }
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

    private int getNotifyOverride(SharedPreferences sharedPreferences, long j) {
        int i = sharedPreferences.getInt("notify2_" + j, -1);
        if (i == 3) {
            if (sharedPreferences.getInt("notifyuntil_" + j, 0) < getConnectionsManager().getCurrentTime()) {
                return i;
            }
            return 2;
        }
        return i;
    }

    public /* synthetic */ void lambda$showNotifications$25() {
        showOrUpdateNotification(false);
    }

    public void showNotifications() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$showNotifications$25();
            }
        });
    }

    public void hideNotifications() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$hideNotifications$26();
            }
        });
    }

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
            AndroidUtilities.runOnUIThread(NotificationsController$$ExternalSyntheticLambda38.INSTANCE);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

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
            if (getNotifyOverride(getAccountInstance().getNotificationsSettings(), this.openedDialogId) == 2) {
                return;
            }
            notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$playInChatSound$29();
                }
            });
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

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
                this.soundIn = this.soundPool.load(ApplicationLoader.applicationContext, org.telegram.messenger.beta.R.raw.sound_in, 1);
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

    public void repeatNotificationMaybe() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$repeatNotificationMaybe$30();
            }
        });
    }

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

    public void deleteNotificationChannel(long j) {
        deleteNotificationChannel(j, -1);
    }

    /* renamed from: deleteNotificationChannelInternal */
    public void lambda$deleteNotificationChannel$31(long j, int i) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        try {
            SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
            SharedPreferences.Editor edit = notificationsSettings.edit();
            if (i == 0 || i == -1) {
                String str = "org.telegram.key" + j;
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

    public void deleteNotificationChannel(final long j, final int i) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$deleteNotificationChannel$31(j, i);
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda21
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$deleteAllNotificationChannels$33();
            }
        });
    }

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

    /* JADX WARN: Removed duplicated region for block: B:25:0x00ef A[Catch: Exception -> 0x0150, TryCatch #0 {Exception -> 0x0150, blocks: (B:8:0x0020, B:11:0x0060, B:12:0x0064, B:13:0x0068, B:16:0x0074, B:17:0x0078, B:19:0x00a1, B:21:0x00b1, B:23:0x00bb, B:25:0x00ef, B:26:0x00f3, B:27:0x00f7, B:29:0x0100, B:31:0x0107, B:35:0x0114, B:36:0x0119, B:37:0x0122, B:41:0x0139), top: B:47:0x0020 }] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00f3 A[Catch: Exception -> 0x0150, TryCatch #0 {Exception -> 0x0150, blocks: (B:8:0x0020, B:11:0x0060, B:12:0x0064, B:13:0x0068, B:16:0x0074, B:17:0x0078, B:19:0x00a1, B:21:0x00b1, B:23:0x00bb, B:25:0x00ef, B:26:0x00f3, B:27:0x00f7, B:29:0x0100, B:31:0x0107, B:35:0x0114, B:36:0x0119, B:37:0x0122, B:41:0x0139), top: B:47:0x0020 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0100 A[Catch: Exception -> 0x0150, TryCatch #0 {Exception -> 0x0150, blocks: (B:8:0x0020, B:11:0x0060, B:12:0x0064, B:13:0x0068, B:16:0x0074, B:17:0x0078, B:19:0x00a1, B:21:0x00b1, B:23:0x00bb, B:25:0x00ef, B:26:0x00f3, B:27:0x00f7, B:29:0x0100, B:31:0x0107, B:35:0x0114, B:36:0x0119, B:37:0x0122, B:41:0x0139), top: B:47:0x0020 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0138  */
    @SuppressLint({"RestrictedApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String createNotificationShortcut(NotificationCompat.Builder builder, long j, String str, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, Person person) {
        Bitmap bitmap;
        IconCompat iconCompat;
        if (!unsupportedNotificationShortcut()) {
            if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup) {
                return null;
            }
            try {
                String str2 = "ndid_" + j;
                Intent intent = new Intent(ApplicationLoader.applicationContext, OpenChatReceiver.class);
                intent.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
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
                        intent2.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                        if (!DialogObject.isUserDialog(j)) {
                            intent2.putExtra("userId", j);
                        } else {
                            intent2.putExtra("chatId", -j);
                        }
                        intent2.putExtra("currentAccount", this.currentAccount);
                        if (bitmap == null) {
                            iconCompat = IconCompat.createWithAdaptiveBitmap(bitmap);
                        } else if (tLRPC$User != null) {
                            iconCompat = IconCompat.createWithResource(ApplicationLoader.applicationContext, tLRPC$User.bot ? org.telegram.messenger.beta.R.drawable.book_bot : org.telegram.messenger.beta.R.drawable.book_user);
                        } else {
                            iconCompat = IconCompat.createWithResource(ApplicationLoader.applicationContext, org.telegram.messenger.beta.R.drawable.book_group);
                        }
                        NotificationCompat.BubbleMetadata.Builder builder2 = new NotificationCompat.BubbleMetadata.Builder(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 134217728), iconCompat);
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
                intent22.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                if (!DialogObject.isUserDialog(j)) {
                }
                intent22.putExtra("currentAccount", this.currentAccount);
                if (bitmap == null) {
                }
                NotificationCompat.BubbleMetadata.Builder builder22 = new NotificationCompat.BubbleMetadata.Builder(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22, 134217728), iconCompat);
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

    @TargetApi(AvailableCode.ERROR_NO_ACTIVITY)
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
                    arrayList.add(new NotificationChannelGroup(str2, LocaleController.getString("NotificationsChannels", org.telegram.messenger.beta.R.string.NotificationsChannels) + str6));
                }
                if (str3 != null) {
                    arrayList.add(new NotificationChannelGroup(str3, LocaleController.getString("NotificationsGroups", org.telegram.messenger.beta.R.string.NotificationsGroups) + str6));
                }
                if (str5 != null) {
                    arrayList.add(new NotificationChannelGroup(str5, LocaleController.getString("NotificationsPrivateChats", org.telegram.messenger.beta.R.string.NotificationsPrivateChats) + str6));
                }
                if (str4 != null) {
                    arrayList.add(new NotificationChannelGroup(str4, LocaleController.getString("NotificationsOther", org.telegram.messenger.beta.R.string.NotificationsOther) + str6));
                }
                systemNotificationManager.createNotificationChannelGroups(arrayList);
            }
            this.channelGroupsCreated = true;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:189:0x0402 A[LOOP:1: B:187:0x03ff->B:189:0x0402, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:192:0x0417  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x0465  */
    @TargetApi(AvailableCode.ERROR_NO_ACTIVITY)
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String validateChannelId(long j, String str, long[] jArr, int i, Uri uri, int i2, boolean z, boolean z2, boolean z3, int i3) {
        String str2;
        String str3;
        String str4;
        String str5;
        boolean z4;
        String str6;
        String str7;
        int i4;
        long[] jArr2;
        long j2;
        String str8;
        boolean z5;
        int i5;
        String str9;
        SharedPreferences sharedPreferences;
        String str10;
        String str11;
        String str12;
        Uri uri2;
        boolean z6;
        boolean z7;
        int i6;
        String MD5;
        boolean z8;
        String str13;
        boolean z9;
        SharedPreferences.Editor editor;
        long[] jArr3;
        int i7;
        SharedPreferences.Editor editor2;
        String str14;
        int i8;
        ensureGroupsCreated();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        String str15 = "groups";
        String str16 = "private";
        String str17 = "channels";
        if (z3) {
            str3 = "other" + this.currentAccount;
            str2 = null;
        } else if (i3 == 2) {
            str3 = str17 + this.currentAccount;
            str2 = "overwrite_channel";
        } else if (i3 == 0) {
            str3 = str15 + this.currentAccount;
            str2 = "overwrite_group";
        } else {
            str3 = str16 + this.currentAccount;
            str2 = "overwrite_private";
        }
        boolean z10 = !z && DialogObject.isEncryptedDialog(j);
        boolean z11 = !z2 && str2 != null && notificationsSettings.getBoolean(str2, false);
        String MD52 = Utilities.MD5(uri == null ? "NoSound" : uri.toString());
        if (MD52 != null && MD52.length() > 5) {
            MD52 = MD52.substring(0, 5);
        }
        if (z3) {
            str4 = LocaleController.getString("NotificationsSilent", org.telegram.messenger.beta.R.string.NotificationsSilent);
            str16 = "silent";
        } else if (z) {
            if (z2) {
                i8 = org.telegram.messenger.beta.R.string.NotificationsInAppDefault;
                str14 = "NotificationsInAppDefault";
            } else {
                i8 = org.telegram.messenger.beta.R.string.NotificationsDefault;
                str14 = "NotificationsDefault";
            }
            String string = LocaleController.getString(str14, i8);
            if (i3 == 2) {
                if (z2) {
                    str17 = "channels_ia";
                }
                str16 = str17;
            } else if (i3 == 0) {
                if (z2) {
                    str15 = "groups_ia";
                }
                str16 = str15;
            } else if (z2) {
                str16 = "private_ia";
            }
            str4 = string;
        } else {
            str4 = z2 ? LocaleController.formatString("NotificationsChatInApp", org.telegram.messenger.beta.R.string.NotificationsChatInApp, str) : str;
            StringBuilder sb = new StringBuilder();
            sb.append(z2 ? "org.telegram.keyia" : "org.telegram.key");
            sb.append(j);
            str16 = sb.toString();
        }
        String str18 = str16 + "_" + MD52;
        String string2 = notificationsSettings.getString(str18, null);
        String string3 = notificationsSettings.getString(str18 + "_s", null);
        StringBuilder sb2 = new StringBuilder();
        String str19 = str4;
        String str20 = str3;
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
                i4 = i2;
                str5 = str18;
                z4 = z11;
                i5 = i;
                jArr2 = jArr;
                str9 = null;
                z5 = false;
                str8 = null;
                string3 = null;
            } else if (z3 || z11) {
                i4 = i2;
                str5 = str18;
                str8 = string2;
                z4 = z11;
                j2 = j;
            } else {
                int importance = notificationChannel.getImportance();
                Uri sound = notificationChannel.getSound();
                long[] vibrationPattern = notificationChannel.getVibrationPattern();
                z4 = z11;
                boolean shouldVibrate = notificationChannel.shouldVibrate();
                if (shouldVibrate || vibrationPattern != null) {
                    str5 = str18;
                    z8 = shouldVibrate;
                    jArr2 = vibrationPattern;
                } else {
                    str5 = str18;
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
                                editor.putInt(getGlobalNotificationsKey(i3), Integer.MAX_VALUE);
                                updateServerNotificationsSettings(i3);
                            }
                            str8 = string2;
                            z9 = true;
                            j2 = j;
                        } else {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("notify2_");
                            str8 = string2;
                            j2 = j;
                            sb4.append(j2);
                            editor.putInt(sb4.toString(), 2);
                            z9 = true;
                            updateServerNotificationsSettings(j2, true);
                        }
                        i4 = i2;
                        str13 = MD53;
                        jArr3 = jArr;
                    } else {
                        i4 = i2;
                        str8 = string2;
                        j2 = j;
                        if (importance != i4) {
                            if (!z2) {
                                editor2 = notificationsSettings.edit();
                                str13 = MD53;
                                int i9 = (importance == 4 || importance == 5) ? 1 : importance == 1 ? 4 : importance == 2 ? 5 : 0;
                                if (z) {
                                    editor2.putInt(getGlobalNotificationsKey(i3), 0).commit();
                                    if (i3 == 2) {
                                        editor2.putInt("priority_channel", i9);
                                    } else if (i3 == 0) {
                                        editor2.putInt("priority_group", i9);
                                    } else {
                                        editor2.putInt("priority_messages", i9);
                                    }
                                } else {
                                    editor2.putInt("notify2_" + j2, 0);
                                    editor2.remove("notifyuntil_" + j2);
                                    editor2.putInt("priority_" + j2, i9);
                                }
                            } else {
                                str13 = MD53;
                                editor2 = null;
                            }
                            jArr3 = jArr;
                            editor = editor2;
                            z9 = true;
                        } else {
                            str13 = MD53;
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
                            } else if (i3 == 2) {
                                editor.putInt("vibrate_channel", z13 ? 0 : 2);
                            } else if (i3 == 0) {
                                editor.putInt("vibrate_group", z13 ? 0 : 2);
                            } else {
                                editor.putInt("vibrate_messages", z13 ? 0 : 2);
                            }
                        }
                        i7 = i;
                        z9 = true;
                    } else {
                        jArr2 = jArr;
                        i7 = i;
                    }
                    if (lightColor != i7) {
                        if (!z2) {
                            if (editor == null) {
                                editor = notificationsSettings.edit();
                            }
                            if (!z) {
                                editor.putInt("color_" + j2, lightColor);
                            } else if (i3 == 2) {
                                editor.putInt("ChannelLed", lightColor);
                            } else if (i3 == 0) {
                                editor.putInt("GroupLed", lightColor);
                            } else {
                                editor.putInt("MessagesLed", lightColor);
                            }
                        }
                        z9 = true;
                    } else {
                        lightColor = i7;
                    }
                    if (editor != null) {
                        editor.commit();
                    }
                    z5 = z9;
                } else {
                    i4 = i2;
                    str13 = MD53;
                    str8 = string2;
                    j2 = j;
                    jArr2 = jArr;
                    lightColor = i;
                    z5 = false;
                }
                i5 = lightColor;
                str9 = str13;
            }
            if (z5 || str9 == null) {
                str11 = str7;
                str12 = str5;
                if (!z4 || str9 == null || !z2 || !z) {
                    i6 = 0;
                    while (i6 < jArr2.length) {
                        sb2.append(jArr2[i6]);
                        i6++;
                        notificationsSettings = notificationsSettings;
                    }
                    sharedPreferences = notificationsSettings;
                    sb2.append(i5);
                    uri2 = uri;
                    if (uri2 != null) {
                        sb2.append(uri.toString());
                    }
                    sb2.append(i4);
                    if (!z && z10) {
                        sb2.append("secret");
                    }
                    MD5 = Utilities.MD5(sb2.toString());
                    if (z3 && str8 != null && (z4 || !string3.equals(MD5))) {
                        try {
                            systemNotificationManager.deleteNotificationChannel(str8);
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("delete channel by settings change " + str8);
                        }
                        str9 = MD5;
                        str10 = null;
                        if (str10 == null) {
                        }
                        return str10;
                    }
                    str9 = MD5;
                    str10 = str8;
                    if (str10 == null) {
                        str10 = z ? this.currentAccount + "channel_" + str12 + str6 + Utilities.random.nextLong() : this.currentAccount + "channel_" + j2 + str6 + Utilities.random.nextLong();
                        NotificationChannel notificationChannel2 = new NotificationChannel(str10, z10 ? LocaleController.getString("SecretChatName", org.telegram.messenger.beta.R.string.SecretChatName) : str19, i4);
                        notificationChannel2.setGroup(str20);
                        if (i5 != 0) {
                            z6 = true;
                            notificationChannel2.enableLights(true);
                            notificationChannel2.setLightColor(i5);
                            z7 = false;
                        } else {
                            z7 = false;
                            z6 = true;
                            notificationChannel2.enableLights(false);
                        }
                        if (!isEmptyVibration(jArr2)) {
                            notificationChannel2.enableVibration(z6);
                            if (jArr2.length > 0) {
                                notificationChannel2.setVibrationPattern(jArr2);
                            }
                        } else {
                            notificationChannel2.enableVibration(z7);
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
                            FileLog.d("create new channel " + str10);
                        }
                        this.lastNotificationChannelCreateTime = SystemClock.elapsedRealtime();
                        systemNotificationManager.createNotificationChannel(notificationChannel2);
                        sharedPreferences.edit().putString(str12, str10).putString(str12 + str11, str9).commit();
                    }
                    return str10;
                }
            } else {
                str12 = str5;
                SharedPreferences.Editor putString = notificationsSettings.edit().putString(str12, str8);
                StringBuilder sb5 = new StringBuilder();
                sb5.append(str12);
                str11 = str7;
                sb5.append(str11);
                putString.putString(sb5.toString(), str9).commit();
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("change edited channel " + str8);
                }
            }
            uri2 = uri;
            sharedPreferences = notificationsSettings;
            str10 = str8;
            if (str10 == null) {
            }
            return str10;
        }
        str6 = "_";
        str5 = str18;
        str8 = string2;
        z4 = z11;
        str7 = "_s";
        j2 = j;
        i4 = i2;
        i5 = i;
        jArr2 = jArr;
        str9 = null;
        z5 = false;
        if (z5) {
        }
        str11 = str7;
        str12 = str5;
        if (!z4) {
        }
        i6 = 0;
        while (i6 < jArr2.length) {
        }
        sharedPreferences = notificationsSettings;
        sb2.append(i5);
        uri2 = uri;
        if (uri2 != null) {
        }
        sb2.append(i4);
        if (!z) {
            sb2.append("secret");
        }
        MD5 = Utilities.MD5(sb2.toString());
        if (z3) {
        }
        str9 = MD5;
        str10 = str8;
        if (str10 == null) {
        }
        return str10;
    }

    /* JADX WARN: Code restructure failed: missing block: B:364:0x0883, code lost:
        if (android.os.Build.VERSION.SDK_INT < 26) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0133, code lost:
        if (r11 == 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0135, code lost:
        r1 = org.telegram.messenger.LocaleController.getString("NotificationHiddenChatName", org.telegram.messenger.beta.R.string.NotificationHiddenChatName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x013f, code lost:
        r1 = org.telegram.messenger.LocaleController.getString("NotificationHiddenName", org.telegram.messenger.beta.R.string.NotificationHiddenName);
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:100:0x01b7 A[Catch: Exception -> 0x0b0c, TRY_ENTER, TryCatch #1 {Exception -> 0x0b0c, blocks: (B:10:0x0022, B:12:0x0046, B:14:0x004a, B:16:0x0054, B:18:0x005a, B:21:0x006a, B:22:0x006c, B:26:0x007a, B:28:0x0086, B:29:0x008c, B:31:0x009e, B:33:0x00ac, B:35:0x00b2, B:36:0x00b5, B:38:0x00bb, B:44:0x00c9, B:46:0x00d9, B:54:0x00f1, B:56:0x00f7, B:58:0x00fc, B:59:0x00ff, B:60:0x0103, B:62:0x010b, B:67:0x0113, B:69:0x0119, B:78:0x0135, B:79:0x013f, B:80:0x0149, B:82:0x0153, B:85:0x0160, B:87:0x0168, B:88:0x0175, B:90:0x0195, B:92:0x01a0, B:97:0x01ac, B:100:0x01b7, B:101:0x01d2, B:102:0x020d, B:105:0x0223, B:110:0x0240, B:111:0x0254, B:113:0x0259, B:114:0x026d, B:116:0x0282, B:117:0x0299, B:119:0x02bd, B:121:0x02d5, B:126:0x02df, B:127:0x02e5, B:131:0x02f2, B:132:0x0306, B:134:0x030b, B:135:0x031f, B:136:0x0332, B:138:0x033a, B:139:0x0343, B:142:0x0357, B:153:0x0372, B:155:0x038c, B:158:0x03c3, B:160:0x03cd, B:161:0x03e7, B:163:0x03fc, B:164:0x0408, B:166:0x040c, B:172:0x042f, B:175:0x0449, B:179:0x0458, B:181:0x046f, B:183:0x04b7, B:184:0x04c3, B:185:0x04da, B:187:0x04f1, B:194:0x0525, B:196:0x0535, B:197:0x0541, B:198:0x0548, B:199:0x0565, B:201:0x0577, B:202:0x0583, B:203:0x058a, B:206:0x05b1, B:208:0x05bb, B:209:0x05c7, B:210:0x05ce, B:217:0x0609, B:219:0x060f, B:231:0x0629, B:233:0x062f, B:241:0x0641, B:244:0x064b, B:247:0x0654, B:263:0x0677, B:267:0x0682, B:269:0x06b8, B:273:0x06c7, B:276:0x06d3, B:277:0x06da, B:279:0x06e0, B:282:0x06e5, B:284:0x06ee, B:287:0x06f6, B:289:0x06fa, B:291:0x06fe, B:293:0x0706, B:297:0x0710, B:299:0x0716, B:301:0x071a, B:303:0x0722, B:307:0x072a, B:309:0x0735, B:311:0x073b, B:313:0x0745, B:316:0x07a6, B:318:0x07aa, B:320:0x07b0, B:321:0x07c6, B:323:0x07eb, B:325:0x07f8, B:343:0x0848, B:353:0x085c, B:357:0x0869, B:360:0x0872, B:363:0x087d, B:370:0x0890, B:372:0x0898, B:374:0x08a0, B:376:0x08c8, B:378:0x08cd, B:380:0x08d5, B:382:0x08d9, B:384:0x08e1, B:388:0x08ec, B:389:0x0902, B:390:0x0907, B:391:0x090a, B:393:0x0912, B:396:0x091c, B:398:0x0924, B:401:0x094f, B:402:0x0957, B:405:0x0961, B:409:0x0972, B:412:0x0983, B:419:0x09a4, B:422:0x09b8, B:424:0x09c1, B:425:0x09d3, B:427:0x09d9, B:429:0x09dd, B:431:0x09e8, B:433:0x09ee, B:435:0x09f8, B:437:0x0a07, B:439:0x0a17, B:441:0x0a36, B:442:0x0a3b, B:444:0x0a67, B:445:0x0a78, B:449:0x0a9b, B:451:0x0aa1, B:453:0x0aa9, B:455:0x0aaf, B:457:0x0ac1, B:458:0x0ada, B:459:0x0af2, B:400:0x092e, B:256:0x0668), top: B:467:0x0022, inners: #0, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:101:0x01d2 A[Catch: Exception -> 0x0b0c, TryCatch #1 {Exception -> 0x0b0c, blocks: (B:10:0x0022, B:12:0x0046, B:14:0x004a, B:16:0x0054, B:18:0x005a, B:21:0x006a, B:22:0x006c, B:26:0x007a, B:28:0x0086, B:29:0x008c, B:31:0x009e, B:33:0x00ac, B:35:0x00b2, B:36:0x00b5, B:38:0x00bb, B:44:0x00c9, B:46:0x00d9, B:54:0x00f1, B:56:0x00f7, B:58:0x00fc, B:59:0x00ff, B:60:0x0103, B:62:0x010b, B:67:0x0113, B:69:0x0119, B:78:0x0135, B:79:0x013f, B:80:0x0149, B:82:0x0153, B:85:0x0160, B:87:0x0168, B:88:0x0175, B:90:0x0195, B:92:0x01a0, B:97:0x01ac, B:100:0x01b7, B:101:0x01d2, B:102:0x020d, B:105:0x0223, B:110:0x0240, B:111:0x0254, B:113:0x0259, B:114:0x026d, B:116:0x0282, B:117:0x0299, B:119:0x02bd, B:121:0x02d5, B:126:0x02df, B:127:0x02e5, B:131:0x02f2, B:132:0x0306, B:134:0x030b, B:135:0x031f, B:136:0x0332, B:138:0x033a, B:139:0x0343, B:142:0x0357, B:153:0x0372, B:155:0x038c, B:158:0x03c3, B:160:0x03cd, B:161:0x03e7, B:163:0x03fc, B:164:0x0408, B:166:0x040c, B:172:0x042f, B:175:0x0449, B:179:0x0458, B:181:0x046f, B:183:0x04b7, B:184:0x04c3, B:185:0x04da, B:187:0x04f1, B:194:0x0525, B:196:0x0535, B:197:0x0541, B:198:0x0548, B:199:0x0565, B:201:0x0577, B:202:0x0583, B:203:0x058a, B:206:0x05b1, B:208:0x05bb, B:209:0x05c7, B:210:0x05ce, B:217:0x0609, B:219:0x060f, B:231:0x0629, B:233:0x062f, B:241:0x0641, B:244:0x064b, B:247:0x0654, B:263:0x0677, B:267:0x0682, B:269:0x06b8, B:273:0x06c7, B:276:0x06d3, B:277:0x06da, B:279:0x06e0, B:282:0x06e5, B:284:0x06ee, B:287:0x06f6, B:289:0x06fa, B:291:0x06fe, B:293:0x0706, B:297:0x0710, B:299:0x0716, B:301:0x071a, B:303:0x0722, B:307:0x072a, B:309:0x0735, B:311:0x073b, B:313:0x0745, B:316:0x07a6, B:318:0x07aa, B:320:0x07b0, B:321:0x07c6, B:323:0x07eb, B:325:0x07f8, B:343:0x0848, B:353:0x085c, B:357:0x0869, B:360:0x0872, B:363:0x087d, B:370:0x0890, B:372:0x0898, B:374:0x08a0, B:376:0x08c8, B:378:0x08cd, B:380:0x08d5, B:382:0x08d9, B:384:0x08e1, B:388:0x08ec, B:389:0x0902, B:390:0x0907, B:391:0x090a, B:393:0x0912, B:396:0x091c, B:398:0x0924, B:401:0x094f, B:402:0x0957, B:405:0x0961, B:409:0x0972, B:412:0x0983, B:419:0x09a4, B:422:0x09b8, B:424:0x09c1, B:425:0x09d3, B:427:0x09d9, B:429:0x09dd, B:431:0x09e8, B:433:0x09ee, B:435:0x09f8, B:437:0x0a07, B:439:0x0a17, B:441:0x0a36, B:442:0x0a3b, B:444:0x0a67, B:445:0x0a78, B:449:0x0a9b, B:451:0x0aa1, B:453:0x0aa9, B:455:0x0aaf, B:457:0x0ac1, B:458:0x0ada, B:459:0x0af2, B:400:0x092e, B:256:0x0668), top: B:467:0x0022, inners: #0, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0223 A[Catch: Exception -> 0x0b0c, TRY_ENTER, TryCatch #1 {Exception -> 0x0b0c, blocks: (B:10:0x0022, B:12:0x0046, B:14:0x004a, B:16:0x0054, B:18:0x005a, B:21:0x006a, B:22:0x006c, B:26:0x007a, B:28:0x0086, B:29:0x008c, B:31:0x009e, B:33:0x00ac, B:35:0x00b2, B:36:0x00b5, B:38:0x00bb, B:44:0x00c9, B:46:0x00d9, B:54:0x00f1, B:56:0x00f7, B:58:0x00fc, B:59:0x00ff, B:60:0x0103, B:62:0x010b, B:67:0x0113, B:69:0x0119, B:78:0x0135, B:79:0x013f, B:80:0x0149, B:82:0x0153, B:85:0x0160, B:87:0x0168, B:88:0x0175, B:90:0x0195, B:92:0x01a0, B:97:0x01ac, B:100:0x01b7, B:101:0x01d2, B:102:0x020d, B:105:0x0223, B:110:0x0240, B:111:0x0254, B:113:0x0259, B:114:0x026d, B:116:0x0282, B:117:0x0299, B:119:0x02bd, B:121:0x02d5, B:126:0x02df, B:127:0x02e5, B:131:0x02f2, B:132:0x0306, B:134:0x030b, B:135:0x031f, B:136:0x0332, B:138:0x033a, B:139:0x0343, B:142:0x0357, B:153:0x0372, B:155:0x038c, B:158:0x03c3, B:160:0x03cd, B:161:0x03e7, B:163:0x03fc, B:164:0x0408, B:166:0x040c, B:172:0x042f, B:175:0x0449, B:179:0x0458, B:181:0x046f, B:183:0x04b7, B:184:0x04c3, B:185:0x04da, B:187:0x04f1, B:194:0x0525, B:196:0x0535, B:197:0x0541, B:198:0x0548, B:199:0x0565, B:201:0x0577, B:202:0x0583, B:203:0x058a, B:206:0x05b1, B:208:0x05bb, B:209:0x05c7, B:210:0x05ce, B:217:0x0609, B:219:0x060f, B:231:0x0629, B:233:0x062f, B:241:0x0641, B:244:0x064b, B:247:0x0654, B:263:0x0677, B:267:0x0682, B:269:0x06b8, B:273:0x06c7, B:276:0x06d3, B:277:0x06da, B:279:0x06e0, B:282:0x06e5, B:284:0x06ee, B:287:0x06f6, B:289:0x06fa, B:291:0x06fe, B:293:0x0706, B:297:0x0710, B:299:0x0716, B:301:0x071a, B:303:0x0722, B:307:0x072a, B:309:0x0735, B:311:0x073b, B:313:0x0745, B:316:0x07a6, B:318:0x07aa, B:320:0x07b0, B:321:0x07c6, B:323:0x07eb, B:325:0x07f8, B:343:0x0848, B:353:0x085c, B:357:0x0869, B:360:0x0872, B:363:0x087d, B:370:0x0890, B:372:0x0898, B:374:0x08a0, B:376:0x08c8, B:378:0x08cd, B:380:0x08d5, B:382:0x08d9, B:384:0x08e1, B:388:0x08ec, B:389:0x0902, B:390:0x0907, B:391:0x090a, B:393:0x0912, B:396:0x091c, B:398:0x0924, B:401:0x094f, B:402:0x0957, B:405:0x0961, B:409:0x0972, B:412:0x0983, B:419:0x09a4, B:422:0x09b8, B:424:0x09c1, B:425:0x09d3, B:427:0x09d9, B:429:0x09dd, B:431:0x09e8, B:433:0x09ee, B:435:0x09f8, B:437:0x0a07, B:439:0x0a17, B:441:0x0a36, B:442:0x0a3b, B:444:0x0a67, B:445:0x0a78, B:449:0x0a9b, B:451:0x0aa1, B:453:0x0aa9, B:455:0x0aaf, B:457:0x0ac1, B:458:0x0ada, B:459:0x0af2, B:400:0x092e, B:256:0x0668), top: B:467:0x0022, inners: #0, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0299 A[Catch: Exception -> 0x0b0c, TryCatch #1 {Exception -> 0x0b0c, blocks: (B:10:0x0022, B:12:0x0046, B:14:0x004a, B:16:0x0054, B:18:0x005a, B:21:0x006a, B:22:0x006c, B:26:0x007a, B:28:0x0086, B:29:0x008c, B:31:0x009e, B:33:0x00ac, B:35:0x00b2, B:36:0x00b5, B:38:0x00bb, B:44:0x00c9, B:46:0x00d9, B:54:0x00f1, B:56:0x00f7, B:58:0x00fc, B:59:0x00ff, B:60:0x0103, B:62:0x010b, B:67:0x0113, B:69:0x0119, B:78:0x0135, B:79:0x013f, B:80:0x0149, B:82:0x0153, B:85:0x0160, B:87:0x0168, B:88:0x0175, B:90:0x0195, B:92:0x01a0, B:97:0x01ac, B:100:0x01b7, B:101:0x01d2, B:102:0x020d, B:105:0x0223, B:110:0x0240, B:111:0x0254, B:113:0x0259, B:114:0x026d, B:116:0x0282, B:117:0x0299, B:119:0x02bd, B:121:0x02d5, B:126:0x02df, B:127:0x02e5, B:131:0x02f2, B:132:0x0306, B:134:0x030b, B:135:0x031f, B:136:0x0332, B:138:0x033a, B:139:0x0343, B:142:0x0357, B:153:0x0372, B:155:0x038c, B:158:0x03c3, B:160:0x03cd, B:161:0x03e7, B:163:0x03fc, B:164:0x0408, B:166:0x040c, B:172:0x042f, B:175:0x0449, B:179:0x0458, B:181:0x046f, B:183:0x04b7, B:184:0x04c3, B:185:0x04da, B:187:0x04f1, B:194:0x0525, B:196:0x0535, B:197:0x0541, B:198:0x0548, B:199:0x0565, B:201:0x0577, B:202:0x0583, B:203:0x058a, B:206:0x05b1, B:208:0x05bb, B:209:0x05c7, B:210:0x05ce, B:217:0x0609, B:219:0x060f, B:231:0x0629, B:233:0x062f, B:241:0x0641, B:244:0x064b, B:247:0x0654, B:263:0x0677, B:267:0x0682, B:269:0x06b8, B:273:0x06c7, B:276:0x06d3, B:277:0x06da, B:279:0x06e0, B:282:0x06e5, B:284:0x06ee, B:287:0x06f6, B:289:0x06fa, B:291:0x06fe, B:293:0x0706, B:297:0x0710, B:299:0x0716, B:301:0x071a, B:303:0x0722, B:307:0x072a, B:309:0x0735, B:311:0x073b, B:313:0x0745, B:316:0x07a6, B:318:0x07aa, B:320:0x07b0, B:321:0x07c6, B:323:0x07eb, B:325:0x07f8, B:343:0x0848, B:353:0x085c, B:357:0x0869, B:360:0x0872, B:363:0x087d, B:370:0x0890, B:372:0x0898, B:374:0x08a0, B:376:0x08c8, B:378:0x08cd, B:380:0x08d5, B:382:0x08d9, B:384:0x08e1, B:388:0x08ec, B:389:0x0902, B:390:0x0907, B:391:0x090a, B:393:0x0912, B:396:0x091c, B:398:0x0924, B:401:0x094f, B:402:0x0957, B:405:0x0961, B:409:0x0972, B:412:0x0983, B:419:0x09a4, B:422:0x09b8, B:424:0x09c1, B:425:0x09d3, B:427:0x09d9, B:429:0x09dd, B:431:0x09e8, B:433:0x09ee, B:435:0x09f8, B:437:0x0a07, B:439:0x0a17, B:441:0x0a36, B:442:0x0a3b, B:444:0x0a67, B:445:0x0a78, B:449:0x0a9b, B:451:0x0aa1, B:453:0x0aa9, B:455:0x0aaf, B:457:0x0ac1, B:458:0x0ada, B:459:0x0af2, B:400:0x092e, B:256:0x0668), top: B:467:0x0022, inners: #0, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0355 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:150:0x036c  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x042f A[Catch: Exception -> 0x0b0c, TryCatch #1 {Exception -> 0x0b0c, blocks: (B:10:0x0022, B:12:0x0046, B:14:0x004a, B:16:0x0054, B:18:0x005a, B:21:0x006a, B:22:0x006c, B:26:0x007a, B:28:0x0086, B:29:0x008c, B:31:0x009e, B:33:0x00ac, B:35:0x00b2, B:36:0x00b5, B:38:0x00bb, B:44:0x00c9, B:46:0x00d9, B:54:0x00f1, B:56:0x00f7, B:58:0x00fc, B:59:0x00ff, B:60:0x0103, B:62:0x010b, B:67:0x0113, B:69:0x0119, B:78:0x0135, B:79:0x013f, B:80:0x0149, B:82:0x0153, B:85:0x0160, B:87:0x0168, B:88:0x0175, B:90:0x0195, B:92:0x01a0, B:97:0x01ac, B:100:0x01b7, B:101:0x01d2, B:102:0x020d, B:105:0x0223, B:110:0x0240, B:111:0x0254, B:113:0x0259, B:114:0x026d, B:116:0x0282, B:117:0x0299, B:119:0x02bd, B:121:0x02d5, B:126:0x02df, B:127:0x02e5, B:131:0x02f2, B:132:0x0306, B:134:0x030b, B:135:0x031f, B:136:0x0332, B:138:0x033a, B:139:0x0343, B:142:0x0357, B:153:0x0372, B:155:0x038c, B:158:0x03c3, B:160:0x03cd, B:161:0x03e7, B:163:0x03fc, B:164:0x0408, B:166:0x040c, B:172:0x042f, B:175:0x0449, B:179:0x0458, B:181:0x046f, B:183:0x04b7, B:184:0x04c3, B:185:0x04da, B:187:0x04f1, B:194:0x0525, B:196:0x0535, B:197:0x0541, B:198:0x0548, B:199:0x0565, B:201:0x0577, B:202:0x0583, B:203:0x058a, B:206:0x05b1, B:208:0x05bb, B:209:0x05c7, B:210:0x05ce, B:217:0x0609, B:219:0x060f, B:231:0x0629, B:233:0x062f, B:241:0x0641, B:244:0x064b, B:247:0x0654, B:263:0x0677, B:267:0x0682, B:269:0x06b8, B:273:0x06c7, B:276:0x06d3, B:277:0x06da, B:279:0x06e0, B:282:0x06e5, B:284:0x06ee, B:287:0x06f6, B:289:0x06fa, B:291:0x06fe, B:293:0x0706, B:297:0x0710, B:299:0x0716, B:301:0x071a, B:303:0x0722, B:307:0x072a, B:309:0x0735, B:311:0x073b, B:313:0x0745, B:316:0x07a6, B:318:0x07aa, B:320:0x07b0, B:321:0x07c6, B:323:0x07eb, B:325:0x07f8, B:343:0x0848, B:353:0x085c, B:357:0x0869, B:360:0x0872, B:363:0x087d, B:370:0x0890, B:372:0x0898, B:374:0x08a0, B:376:0x08c8, B:378:0x08cd, B:380:0x08d5, B:382:0x08d9, B:384:0x08e1, B:388:0x08ec, B:389:0x0902, B:390:0x0907, B:391:0x090a, B:393:0x0912, B:396:0x091c, B:398:0x0924, B:401:0x094f, B:402:0x0957, B:405:0x0961, B:409:0x0972, B:412:0x0983, B:419:0x09a4, B:422:0x09b8, B:424:0x09c1, B:425:0x09d3, B:427:0x09d9, B:429:0x09dd, B:431:0x09e8, B:433:0x09ee, B:435:0x09f8, B:437:0x0a07, B:439:0x0a17, B:441:0x0a36, B:442:0x0a3b, B:444:0x0a67, B:445:0x0a78, B:449:0x0a9b, B:451:0x0aa1, B:453:0x0aa9, B:455:0x0aaf, B:457:0x0ac1, B:458:0x0ada, B:459:0x0af2, B:400:0x092e, B:256:0x0668), top: B:467:0x0022, inners: #0, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:177:0x0453  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x0456  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x046f A[Catch: Exception -> 0x0b0c, TryCatch #1 {Exception -> 0x0b0c, blocks: (B:10:0x0022, B:12:0x0046, B:14:0x004a, B:16:0x0054, B:18:0x005a, B:21:0x006a, B:22:0x006c, B:26:0x007a, B:28:0x0086, B:29:0x008c, B:31:0x009e, B:33:0x00ac, B:35:0x00b2, B:36:0x00b5, B:38:0x00bb, B:44:0x00c9, B:46:0x00d9, B:54:0x00f1, B:56:0x00f7, B:58:0x00fc, B:59:0x00ff, B:60:0x0103, B:62:0x010b, B:67:0x0113, B:69:0x0119, B:78:0x0135, B:79:0x013f, B:80:0x0149, B:82:0x0153, B:85:0x0160, B:87:0x0168, B:88:0x0175, B:90:0x0195, B:92:0x01a0, B:97:0x01ac, B:100:0x01b7, B:101:0x01d2, B:102:0x020d, B:105:0x0223, B:110:0x0240, B:111:0x0254, B:113:0x0259, B:114:0x026d, B:116:0x0282, B:117:0x0299, B:119:0x02bd, B:121:0x02d5, B:126:0x02df, B:127:0x02e5, B:131:0x02f2, B:132:0x0306, B:134:0x030b, B:135:0x031f, B:136:0x0332, B:138:0x033a, B:139:0x0343, B:142:0x0357, B:153:0x0372, B:155:0x038c, B:158:0x03c3, B:160:0x03cd, B:161:0x03e7, B:163:0x03fc, B:164:0x0408, B:166:0x040c, B:172:0x042f, B:175:0x0449, B:179:0x0458, B:181:0x046f, B:183:0x04b7, B:184:0x04c3, B:185:0x04da, B:187:0x04f1, B:194:0x0525, B:196:0x0535, B:197:0x0541, B:198:0x0548, B:199:0x0565, B:201:0x0577, B:202:0x0583, B:203:0x058a, B:206:0x05b1, B:208:0x05bb, B:209:0x05c7, B:210:0x05ce, B:217:0x0609, B:219:0x060f, B:231:0x0629, B:233:0x062f, B:241:0x0641, B:244:0x064b, B:247:0x0654, B:263:0x0677, B:267:0x0682, B:269:0x06b8, B:273:0x06c7, B:276:0x06d3, B:277:0x06da, B:279:0x06e0, B:282:0x06e5, B:284:0x06ee, B:287:0x06f6, B:289:0x06fa, B:291:0x06fe, B:293:0x0706, B:297:0x0710, B:299:0x0716, B:301:0x071a, B:303:0x0722, B:307:0x072a, B:309:0x0735, B:311:0x073b, B:313:0x0745, B:316:0x07a6, B:318:0x07aa, B:320:0x07b0, B:321:0x07c6, B:323:0x07eb, B:325:0x07f8, B:343:0x0848, B:353:0x085c, B:357:0x0869, B:360:0x0872, B:363:0x087d, B:370:0x0890, B:372:0x0898, B:374:0x08a0, B:376:0x08c8, B:378:0x08cd, B:380:0x08d5, B:382:0x08d9, B:384:0x08e1, B:388:0x08ec, B:389:0x0902, B:390:0x0907, B:391:0x090a, B:393:0x0912, B:396:0x091c, B:398:0x0924, B:401:0x094f, B:402:0x0957, B:405:0x0961, B:409:0x0972, B:412:0x0983, B:419:0x09a4, B:422:0x09b8, B:424:0x09c1, B:425:0x09d3, B:427:0x09d9, B:429:0x09dd, B:431:0x09e8, B:433:0x09ee, B:435:0x09f8, B:437:0x0a07, B:439:0x0a17, B:441:0x0a36, B:442:0x0a3b, B:444:0x0a67, B:445:0x0a78, B:449:0x0a9b, B:451:0x0aa1, B:453:0x0aa9, B:455:0x0aaf, B:457:0x0ac1, B:458:0x0ada, B:459:0x0af2, B:400:0x092e, B:256:0x0668), top: B:467:0x0022, inners: #0, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0515  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x0523  */
    /* JADX WARN: Removed duplicated region for block: B:204:0x05a6  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x0603  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0607  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x060f A[Catch: Exception -> 0x0b0c, TryCatch #1 {Exception -> 0x0b0c, blocks: (B:10:0x0022, B:12:0x0046, B:14:0x004a, B:16:0x0054, B:18:0x005a, B:21:0x006a, B:22:0x006c, B:26:0x007a, B:28:0x0086, B:29:0x008c, B:31:0x009e, B:33:0x00ac, B:35:0x00b2, B:36:0x00b5, B:38:0x00bb, B:44:0x00c9, B:46:0x00d9, B:54:0x00f1, B:56:0x00f7, B:58:0x00fc, B:59:0x00ff, B:60:0x0103, B:62:0x010b, B:67:0x0113, B:69:0x0119, B:78:0x0135, B:79:0x013f, B:80:0x0149, B:82:0x0153, B:85:0x0160, B:87:0x0168, B:88:0x0175, B:90:0x0195, B:92:0x01a0, B:97:0x01ac, B:100:0x01b7, B:101:0x01d2, B:102:0x020d, B:105:0x0223, B:110:0x0240, B:111:0x0254, B:113:0x0259, B:114:0x026d, B:116:0x0282, B:117:0x0299, B:119:0x02bd, B:121:0x02d5, B:126:0x02df, B:127:0x02e5, B:131:0x02f2, B:132:0x0306, B:134:0x030b, B:135:0x031f, B:136:0x0332, B:138:0x033a, B:139:0x0343, B:142:0x0357, B:153:0x0372, B:155:0x038c, B:158:0x03c3, B:160:0x03cd, B:161:0x03e7, B:163:0x03fc, B:164:0x0408, B:166:0x040c, B:172:0x042f, B:175:0x0449, B:179:0x0458, B:181:0x046f, B:183:0x04b7, B:184:0x04c3, B:185:0x04da, B:187:0x04f1, B:194:0x0525, B:196:0x0535, B:197:0x0541, B:198:0x0548, B:199:0x0565, B:201:0x0577, B:202:0x0583, B:203:0x058a, B:206:0x05b1, B:208:0x05bb, B:209:0x05c7, B:210:0x05ce, B:217:0x0609, B:219:0x060f, B:231:0x0629, B:233:0x062f, B:241:0x0641, B:244:0x064b, B:247:0x0654, B:263:0x0677, B:267:0x0682, B:269:0x06b8, B:273:0x06c7, B:276:0x06d3, B:277:0x06da, B:279:0x06e0, B:282:0x06e5, B:284:0x06ee, B:287:0x06f6, B:289:0x06fa, B:291:0x06fe, B:293:0x0706, B:297:0x0710, B:299:0x0716, B:301:0x071a, B:303:0x0722, B:307:0x072a, B:309:0x0735, B:311:0x073b, B:313:0x0745, B:316:0x07a6, B:318:0x07aa, B:320:0x07b0, B:321:0x07c6, B:323:0x07eb, B:325:0x07f8, B:343:0x0848, B:353:0x085c, B:357:0x0869, B:360:0x0872, B:363:0x087d, B:370:0x0890, B:372:0x0898, B:374:0x08a0, B:376:0x08c8, B:378:0x08cd, B:380:0x08d5, B:382:0x08d9, B:384:0x08e1, B:388:0x08ec, B:389:0x0902, B:390:0x0907, B:391:0x090a, B:393:0x0912, B:396:0x091c, B:398:0x0924, B:401:0x094f, B:402:0x0957, B:405:0x0961, B:409:0x0972, B:412:0x0983, B:419:0x09a4, B:422:0x09b8, B:424:0x09c1, B:425:0x09d3, B:427:0x09d9, B:429:0x09dd, B:431:0x09e8, B:433:0x09ee, B:435:0x09f8, B:437:0x0a07, B:439:0x0a17, B:441:0x0a36, B:442:0x0a3b, B:444:0x0a67, B:445:0x0a78, B:449:0x0a9b, B:451:0x0aa1, B:453:0x0aa9, B:455:0x0aaf, B:457:0x0ac1, B:458:0x0ada, B:459:0x0af2, B:400:0x092e, B:256:0x0668), top: B:467:0x0022, inners: #0, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:225:0x061e  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x0624  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0629 A[Catch: Exception -> 0x0b0c, TryCatch #1 {Exception -> 0x0b0c, blocks: (B:10:0x0022, B:12:0x0046, B:14:0x004a, B:16:0x0054, B:18:0x005a, B:21:0x006a, B:22:0x006c, B:26:0x007a, B:28:0x0086, B:29:0x008c, B:31:0x009e, B:33:0x00ac, B:35:0x00b2, B:36:0x00b5, B:38:0x00bb, B:44:0x00c9, B:46:0x00d9, B:54:0x00f1, B:56:0x00f7, B:58:0x00fc, B:59:0x00ff, B:60:0x0103, B:62:0x010b, B:67:0x0113, B:69:0x0119, B:78:0x0135, B:79:0x013f, B:80:0x0149, B:82:0x0153, B:85:0x0160, B:87:0x0168, B:88:0x0175, B:90:0x0195, B:92:0x01a0, B:97:0x01ac, B:100:0x01b7, B:101:0x01d2, B:102:0x020d, B:105:0x0223, B:110:0x0240, B:111:0x0254, B:113:0x0259, B:114:0x026d, B:116:0x0282, B:117:0x0299, B:119:0x02bd, B:121:0x02d5, B:126:0x02df, B:127:0x02e5, B:131:0x02f2, B:132:0x0306, B:134:0x030b, B:135:0x031f, B:136:0x0332, B:138:0x033a, B:139:0x0343, B:142:0x0357, B:153:0x0372, B:155:0x038c, B:158:0x03c3, B:160:0x03cd, B:161:0x03e7, B:163:0x03fc, B:164:0x0408, B:166:0x040c, B:172:0x042f, B:175:0x0449, B:179:0x0458, B:181:0x046f, B:183:0x04b7, B:184:0x04c3, B:185:0x04da, B:187:0x04f1, B:194:0x0525, B:196:0x0535, B:197:0x0541, B:198:0x0548, B:199:0x0565, B:201:0x0577, B:202:0x0583, B:203:0x058a, B:206:0x05b1, B:208:0x05bb, B:209:0x05c7, B:210:0x05ce, B:217:0x0609, B:219:0x060f, B:231:0x0629, B:233:0x062f, B:241:0x0641, B:244:0x064b, B:247:0x0654, B:263:0x0677, B:267:0x0682, B:269:0x06b8, B:273:0x06c7, B:276:0x06d3, B:277:0x06da, B:279:0x06e0, B:282:0x06e5, B:284:0x06ee, B:287:0x06f6, B:289:0x06fa, B:291:0x06fe, B:293:0x0706, B:297:0x0710, B:299:0x0716, B:301:0x071a, B:303:0x0722, B:307:0x072a, B:309:0x0735, B:311:0x073b, B:313:0x0745, B:316:0x07a6, B:318:0x07aa, B:320:0x07b0, B:321:0x07c6, B:323:0x07eb, B:325:0x07f8, B:343:0x0848, B:353:0x085c, B:357:0x0869, B:360:0x0872, B:363:0x087d, B:370:0x0890, B:372:0x0898, B:374:0x08a0, B:376:0x08c8, B:378:0x08cd, B:380:0x08d5, B:382:0x08d9, B:384:0x08e1, B:388:0x08ec, B:389:0x0902, B:390:0x0907, B:391:0x090a, B:393:0x0912, B:396:0x091c, B:398:0x0924, B:401:0x094f, B:402:0x0957, B:405:0x0961, B:409:0x0972, B:412:0x0983, B:419:0x09a4, B:422:0x09b8, B:424:0x09c1, B:425:0x09d3, B:427:0x09d9, B:429:0x09dd, B:431:0x09e8, B:433:0x09ee, B:435:0x09f8, B:437:0x0a07, B:439:0x0a17, B:441:0x0a36, B:442:0x0a3b, B:444:0x0a67, B:445:0x0a78, B:449:0x0a9b, B:451:0x0aa1, B:453:0x0aa9, B:455:0x0aaf, B:457:0x0ac1, B:458:0x0ada, B:459:0x0af2, B:400:0x092e, B:256:0x0668), top: B:467:0x0022, inners: #0, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:235:0x0636  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x0641 A[Catch: Exception -> 0x0b0c, TryCatch #1 {Exception -> 0x0b0c, blocks: (B:10:0x0022, B:12:0x0046, B:14:0x004a, B:16:0x0054, B:18:0x005a, B:21:0x006a, B:22:0x006c, B:26:0x007a, B:28:0x0086, B:29:0x008c, B:31:0x009e, B:33:0x00ac, B:35:0x00b2, B:36:0x00b5, B:38:0x00bb, B:44:0x00c9, B:46:0x00d9, B:54:0x00f1, B:56:0x00f7, B:58:0x00fc, B:59:0x00ff, B:60:0x0103, B:62:0x010b, B:67:0x0113, B:69:0x0119, B:78:0x0135, B:79:0x013f, B:80:0x0149, B:82:0x0153, B:85:0x0160, B:87:0x0168, B:88:0x0175, B:90:0x0195, B:92:0x01a0, B:97:0x01ac, B:100:0x01b7, B:101:0x01d2, B:102:0x020d, B:105:0x0223, B:110:0x0240, B:111:0x0254, B:113:0x0259, B:114:0x026d, B:116:0x0282, B:117:0x0299, B:119:0x02bd, B:121:0x02d5, B:126:0x02df, B:127:0x02e5, B:131:0x02f2, B:132:0x0306, B:134:0x030b, B:135:0x031f, B:136:0x0332, B:138:0x033a, B:139:0x0343, B:142:0x0357, B:153:0x0372, B:155:0x038c, B:158:0x03c3, B:160:0x03cd, B:161:0x03e7, B:163:0x03fc, B:164:0x0408, B:166:0x040c, B:172:0x042f, B:175:0x0449, B:179:0x0458, B:181:0x046f, B:183:0x04b7, B:184:0x04c3, B:185:0x04da, B:187:0x04f1, B:194:0x0525, B:196:0x0535, B:197:0x0541, B:198:0x0548, B:199:0x0565, B:201:0x0577, B:202:0x0583, B:203:0x058a, B:206:0x05b1, B:208:0x05bb, B:209:0x05c7, B:210:0x05ce, B:217:0x0609, B:219:0x060f, B:231:0x0629, B:233:0x062f, B:241:0x0641, B:244:0x064b, B:247:0x0654, B:263:0x0677, B:267:0x0682, B:269:0x06b8, B:273:0x06c7, B:276:0x06d3, B:277:0x06da, B:279:0x06e0, B:282:0x06e5, B:284:0x06ee, B:287:0x06f6, B:289:0x06fa, B:291:0x06fe, B:293:0x0706, B:297:0x0710, B:299:0x0716, B:301:0x071a, B:303:0x0722, B:307:0x072a, B:309:0x0735, B:311:0x073b, B:313:0x0745, B:316:0x07a6, B:318:0x07aa, B:320:0x07b0, B:321:0x07c6, B:323:0x07eb, B:325:0x07f8, B:343:0x0848, B:353:0x085c, B:357:0x0869, B:360:0x0872, B:363:0x087d, B:370:0x0890, B:372:0x0898, B:374:0x08a0, B:376:0x08c8, B:378:0x08cd, B:380:0x08d5, B:382:0x08d9, B:384:0x08e1, B:388:0x08ec, B:389:0x0902, B:390:0x0907, B:391:0x090a, B:393:0x0912, B:396:0x091c, B:398:0x0924, B:401:0x094f, B:402:0x0957, B:405:0x0961, B:409:0x0972, B:412:0x0983, B:419:0x09a4, B:422:0x09b8, B:424:0x09c1, B:425:0x09d3, B:427:0x09d9, B:429:0x09dd, B:431:0x09e8, B:433:0x09ee, B:435:0x09f8, B:437:0x0a07, B:439:0x0a17, B:441:0x0a36, B:442:0x0a3b, B:444:0x0a67, B:445:0x0a78, B:449:0x0a9b, B:451:0x0aa1, B:453:0x0aa9, B:455:0x0aaf, B:457:0x0ac1, B:458:0x0ada, B:459:0x0af2, B:400:0x092e, B:256:0x0668), top: B:467:0x0022, inners: #0, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:254:0x0665  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x067c  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x0681  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x06b8 A[Catch: Exception -> 0x0b0c, TryCatch #1 {Exception -> 0x0b0c, blocks: (B:10:0x0022, B:12:0x0046, B:14:0x004a, B:16:0x0054, B:18:0x005a, B:21:0x006a, B:22:0x006c, B:26:0x007a, B:28:0x0086, B:29:0x008c, B:31:0x009e, B:33:0x00ac, B:35:0x00b2, B:36:0x00b5, B:38:0x00bb, B:44:0x00c9, B:46:0x00d9, B:54:0x00f1, B:56:0x00f7, B:58:0x00fc, B:59:0x00ff, B:60:0x0103, B:62:0x010b, B:67:0x0113, B:69:0x0119, B:78:0x0135, B:79:0x013f, B:80:0x0149, B:82:0x0153, B:85:0x0160, B:87:0x0168, B:88:0x0175, B:90:0x0195, B:92:0x01a0, B:97:0x01ac, B:100:0x01b7, B:101:0x01d2, B:102:0x020d, B:105:0x0223, B:110:0x0240, B:111:0x0254, B:113:0x0259, B:114:0x026d, B:116:0x0282, B:117:0x0299, B:119:0x02bd, B:121:0x02d5, B:126:0x02df, B:127:0x02e5, B:131:0x02f2, B:132:0x0306, B:134:0x030b, B:135:0x031f, B:136:0x0332, B:138:0x033a, B:139:0x0343, B:142:0x0357, B:153:0x0372, B:155:0x038c, B:158:0x03c3, B:160:0x03cd, B:161:0x03e7, B:163:0x03fc, B:164:0x0408, B:166:0x040c, B:172:0x042f, B:175:0x0449, B:179:0x0458, B:181:0x046f, B:183:0x04b7, B:184:0x04c3, B:185:0x04da, B:187:0x04f1, B:194:0x0525, B:196:0x0535, B:197:0x0541, B:198:0x0548, B:199:0x0565, B:201:0x0577, B:202:0x0583, B:203:0x058a, B:206:0x05b1, B:208:0x05bb, B:209:0x05c7, B:210:0x05ce, B:217:0x0609, B:219:0x060f, B:231:0x0629, B:233:0x062f, B:241:0x0641, B:244:0x064b, B:247:0x0654, B:263:0x0677, B:267:0x0682, B:269:0x06b8, B:273:0x06c7, B:276:0x06d3, B:277:0x06da, B:279:0x06e0, B:282:0x06e5, B:284:0x06ee, B:287:0x06f6, B:289:0x06fa, B:291:0x06fe, B:293:0x0706, B:297:0x0710, B:299:0x0716, B:301:0x071a, B:303:0x0722, B:307:0x072a, B:309:0x0735, B:311:0x073b, B:313:0x0745, B:316:0x07a6, B:318:0x07aa, B:320:0x07b0, B:321:0x07c6, B:323:0x07eb, B:325:0x07f8, B:343:0x0848, B:353:0x085c, B:357:0x0869, B:360:0x0872, B:363:0x087d, B:370:0x0890, B:372:0x0898, B:374:0x08a0, B:376:0x08c8, B:378:0x08cd, B:380:0x08d5, B:382:0x08d9, B:384:0x08e1, B:388:0x08ec, B:389:0x0902, B:390:0x0907, B:391:0x090a, B:393:0x0912, B:396:0x091c, B:398:0x0924, B:401:0x094f, B:402:0x0957, B:405:0x0961, B:409:0x0972, B:412:0x0983, B:419:0x09a4, B:422:0x09b8, B:424:0x09c1, B:425:0x09d3, B:427:0x09d9, B:429:0x09dd, B:431:0x09e8, B:433:0x09ee, B:435:0x09f8, B:437:0x0a07, B:439:0x0a17, B:441:0x0a36, B:442:0x0a3b, B:444:0x0a67, B:445:0x0a78, B:449:0x0a9b, B:451:0x0aa1, B:453:0x0aa9, B:455:0x0aaf, B:457:0x0ac1, B:458:0x0ada, B:459:0x0af2, B:400:0x092e, B:256:0x0668), top: B:467:0x0022, inners: #0, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:307:0x072a A[Catch: Exception -> 0x0b0c, TryCatch #1 {Exception -> 0x0b0c, blocks: (B:10:0x0022, B:12:0x0046, B:14:0x004a, B:16:0x0054, B:18:0x005a, B:21:0x006a, B:22:0x006c, B:26:0x007a, B:28:0x0086, B:29:0x008c, B:31:0x009e, B:33:0x00ac, B:35:0x00b2, B:36:0x00b5, B:38:0x00bb, B:44:0x00c9, B:46:0x00d9, B:54:0x00f1, B:56:0x00f7, B:58:0x00fc, B:59:0x00ff, B:60:0x0103, B:62:0x010b, B:67:0x0113, B:69:0x0119, B:78:0x0135, B:79:0x013f, B:80:0x0149, B:82:0x0153, B:85:0x0160, B:87:0x0168, B:88:0x0175, B:90:0x0195, B:92:0x01a0, B:97:0x01ac, B:100:0x01b7, B:101:0x01d2, B:102:0x020d, B:105:0x0223, B:110:0x0240, B:111:0x0254, B:113:0x0259, B:114:0x026d, B:116:0x0282, B:117:0x0299, B:119:0x02bd, B:121:0x02d5, B:126:0x02df, B:127:0x02e5, B:131:0x02f2, B:132:0x0306, B:134:0x030b, B:135:0x031f, B:136:0x0332, B:138:0x033a, B:139:0x0343, B:142:0x0357, B:153:0x0372, B:155:0x038c, B:158:0x03c3, B:160:0x03cd, B:161:0x03e7, B:163:0x03fc, B:164:0x0408, B:166:0x040c, B:172:0x042f, B:175:0x0449, B:179:0x0458, B:181:0x046f, B:183:0x04b7, B:184:0x04c3, B:185:0x04da, B:187:0x04f1, B:194:0x0525, B:196:0x0535, B:197:0x0541, B:198:0x0548, B:199:0x0565, B:201:0x0577, B:202:0x0583, B:203:0x058a, B:206:0x05b1, B:208:0x05bb, B:209:0x05c7, B:210:0x05ce, B:217:0x0609, B:219:0x060f, B:231:0x0629, B:233:0x062f, B:241:0x0641, B:244:0x064b, B:247:0x0654, B:263:0x0677, B:267:0x0682, B:269:0x06b8, B:273:0x06c7, B:276:0x06d3, B:277:0x06da, B:279:0x06e0, B:282:0x06e5, B:284:0x06ee, B:287:0x06f6, B:289:0x06fa, B:291:0x06fe, B:293:0x0706, B:297:0x0710, B:299:0x0716, B:301:0x071a, B:303:0x0722, B:307:0x072a, B:309:0x0735, B:311:0x073b, B:313:0x0745, B:316:0x07a6, B:318:0x07aa, B:320:0x07b0, B:321:0x07c6, B:323:0x07eb, B:325:0x07f8, B:343:0x0848, B:353:0x085c, B:357:0x0869, B:360:0x0872, B:363:0x087d, B:370:0x0890, B:372:0x0898, B:374:0x08a0, B:376:0x08c8, B:378:0x08cd, B:380:0x08d5, B:382:0x08d9, B:384:0x08e1, B:388:0x08ec, B:389:0x0902, B:390:0x0907, B:391:0x090a, B:393:0x0912, B:396:0x091c, B:398:0x0924, B:401:0x094f, B:402:0x0957, B:405:0x0961, B:409:0x0972, B:412:0x0983, B:419:0x09a4, B:422:0x09b8, B:424:0x09c1, B:425:0x09d3, B:427:0x09d9, B:429:0x09dd, B:431:0x09e8, B:433:0x09ee, B:435:0x09f8, B:437:0x0a07, B:439:0x0a17, B:441:0x0a36, B:442:0x0a3b, B:444:0x0a67, B:445:0x0a78, B:449:0x0a9b, B:451:0x0aa1, B:453:0x0aa9, B:455:0x0aaf, B:457:0x0ac1, B:458:0x0ada, B:459:0x0af2, B:400:0x092e, B:256:0x0668), top: B:467:0x0022, inners: #0, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:315:0x07a4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:323:0x07eb A[Catch: Exception -> 0x0b0c, TryCatch #1 {Exception -> 0x0b0c, blocks: (B:10:0x0022, B:12:0x0046, B:14:0x004a, B:16:0x0054, B:18:0x005a, B:21:0x006a, B:22:0x006c, B:26:0x007a, B:28:0x0086, B:29:0x008c, B:31:0x009e, B:33:0x00ac, B:35:0x00b2, B:36:0x00b5, B:38:0x00bb, B:44:0x00c9, B:46:0x00d9, B:54:0x00f1, B:56:0x00f7, B:58:0x00fc, B:59:0x00ff, B:60:0x0103, B:62:0x010b, B:67:0x0113, B:69:0x0119, B:78:0x0135, B:79:0x013f, B:80:0x0149, B:82:0x0153, B:85:0x0160, B:87:0x0168, B:88:0x0175, B:90:0x0195, B:92:0x01a0, B:97:0x01ac, B:100:0x01b7, B:101:0x01d2, B:102:0x020d, B:105:0x0223, B:110:0x0240, B:111:0x0254, B:113:0x0259, B:114:0x026d, B:116:0x0282, B:117:0x0299, B:119:0x02bd, B:121:0x02d5, B:126:0x02df, B:127:0x02e5, B:131:0x02f2, B:132:0x0306, B:134:0x030b, B:135:0x031f, B:136:0x0332, B:138:0x033a, B:139:0x0343, B:142:0x0357, B:153:0x0372, B:155:0x038c, B:158:0x03c3, B:160:0x03cd, B:161:0x03e7, B:163:0x03fc, B:164:0x0408, B:166:0x040c, B:172:0x042f, B:175:0x0449, B:179:0x0458, B:181:0x046f, B:183:0x04b7, B:184:0x04c3, B:185:0x04da, B:187:0x04f1, B:194:0x0525, B:196:0x0535, B:197:0x0541, B:198:0x0548, B:199:0x0565, B:201:0x0577, B:202:0x0583, B:203:0x058a, B:206:0x05b1, B:208:0x05bb, B:209:0x05c7, B:210:0x05ce, B:217:0x0609, B:219:0x060f, B:231:0x0629, B:233:0x062f, B:241:0x0641, B:244:0x064b, B:247:0x0654, B:263:0x0677, B:267:0x0682, B:269:0x06b8, B:273:0x06c7, B:276:0x06d3, B:277:0x06da, B:279:0x06e0, B:282:0x06e5, B:284:0x06ee, B:287:0x06f6, B:289:0x06fa, B:291:0x06fe, B:293:0x0706, B:297:0x0710, B:299:0x0716, B:301:0x071a, B:303:0x0722, B:307:0x072a, B:309:0x0735, B:311:0x073b, B:313:0x0745, B:316:0x07a6, B:318:0x07aa, B:320:0x07b0, B:321:0x07c6, B:323:0x07eb, B:325:0x07f8, B:343:0x0848, B:353:0x085c, B:357:0x0869, B:360:0x0872, B:363:0x087d, B:370:0x0890, B:372:0x0898, B:374:0x08a0, B:376:0x08c8, B:378:0x08cd, B:380:0x08d5, B:382:0x08d9, B:384:0x08e1, B:388:0x08ec, B:389:0x0902, B:390:0x0907, B:391:0x090a, B:393:0x0912, B:396:0x091c, B:398:0x0924, B:401:0x094f, B:402:0x0957, B:405:0x0961, B:409:0x0972, B:412:0x0983, B:419:0x09a4, B:422:0x09b8, B:424:0x09c1, B:425:0x09d3, B:427:0x09d9, B:429:0x09dd, B:431:0x09e8, B:433:0x09ee, B:435:0x09f8, B:437:0x0a07, B:439:0x0a17, B:441:0x0a36, B:442:0x0a3b, B:444:0x0a67, B:445:0x0a78, B:449:0x0a9b, B:451:0x0aa1, B:453:0x0aa9, B:455:0x0aaf, B:457:0x0ac1, B:458:0x0ada, B:459:0x0af2, B:400:0x092e, B:256:0x0668), top: B:467:0x0022, inners: #0, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:336:0x0839  */
    /* JADX WARN: Removed duplicated region for block: B:339:0x0841  */
    /* JADX WARN: Removed duplicated region for block: B:405:0x0961 A[Catch: Exception -> 0x0b0c, TryCatch #1 {Exception -> 0x0b0c, blocks: (B:10:0x0022, B:12:0x0046, B:14:0x004a, B:16:0x0054, B:18:0x005a, B:21:0x006a, B:22:0x006c, B:26:0x007a, B:28:0x0086, B:29:0x008c, B:31:0x009e, B:33:0x00ac, B:35:0x00b2, B:36:0x00b5, B:38:0x00bb, B:44:0x00c9, B:46:0x00d9, B:54:0x00f1, B:56:0x00f7, B:58:0x00fc, B:59:0x00ff, B:60:0x0103, B:62:0x010b, B:67:0x0113, B:69:0x0119, B:78:0x0135, B:79:0x013f, B:80:0x0149, B:82:0x0153, B:85:0x0160, B:87:0x0168, B:88:0x0175, B:90:0x0195, B:92:0x01a0, B:97:0x01ac, B:100:0x01b7, B:101:0x01d2, B:102:0x020d, B:105:0x0223, B:110:0x0240, B:111:0x0254, B:113:0x0259, B:114:0x026d, B:116:0x0282, B:117:0x0299, B:119:0x02bd, B:121:0x02d5, B:126:0x02df, B:127:0x02e5, B:131:0x02f2, B:132:0x0306, B:134:0x030b, B:135:0x031f, B:136:0x0332, B:138:0x033a, B:139:0x0343, B:142:0x0357, B:153:0x0372, B:155:0x038c, B:158:0x03c3, B:160:0x03cd, B:161:0x03e7, B:163:0x03fc, B:164:0x0408, B:166:0x040c, B:172:0x042f, B:175:0x0449, B:179:0x0458, B:181:0x046f, B:183:0x04b7, B:184:0x04c3, B:185:0x04da, B:187:0x04f1, B:194:0x0525, B:196:0x0535, B:197:0x0541, B:198:0x0548, B:199:0x0565, B:201:0x0577, B:202:0x0583, B:203:0x058a, B:206:0x05b1, B:208:0x05bb, B:209:0x05c7, B:210:0x05ce, B:217:0x0609, B:219:0x060f, B:231:0x0629, B:233:0x062f, B:241:0x0641, B:244:0x064b, B:247:0x0654, B:263:0x0677, B:267:0x0682, B:269:0x06b8, B:273:0x06c7, B:276:0x06d3, B:277:0x06da, B:279:0x06e0, B:282:0x06e5, B:284:0x06ee, B:287:0x06f6, B:289:0x06fa, B:291:0x06fe, B:293:0x0706, B:297:0x0710, B:299:0x0716, B:301:0x071a, B:303:0x0722, B:307:0x072a, B:309:0x0735, B:311:0x073b, B:313:0x0745, B:316:0x07a6, B:318:0x07aa, B:320:0x07b0, B:321:0x07c6, B:323:0x07eb, B:325:0x07f8, B:343:0x0848, B:353:0x085c, B:357:0x0869, B:360:0x0872, B:363:0x087d, B:370:0x0890, B:372:0x0898, B:374:0x08a0, B:376:0x08c8, B:378:0x08cd, B:380:0x08d5, B:382:0x08d9, B:384:0x08e1, B:388:0x08ec, B:389:0x0902, B:390:0x0907, B:391:0x090a, B:393:0x0912, B:396:0x091c, B:398:0x0924, B:401:0x094f, B:402:0x0957, B:405:0x0961, B:409:0x0972, B:412:0x0983, B:419:0x09a4, B:422:0x09b8, B:424:0x09c1, B:425:0x09d3, B:427:0x09d9, B:429:0x09dd, B:431:0x09e8, B:433:0x09ee, B:435:0x09f8, B:437:0x0a07, B:439:0x0a17, B:441:0x0a36, B:442:0x0a3b, B:444:0x0a67, B:445:0x0a78, B:449:0x0a9b, B:451:0x0aa1, B:453:0x0aa9, B:455:0x0aaf, B:457:0x0ac1, B:458:0x0ada, B:459:0x0af2, B:400:0x092e, B:256:0x0668), top: B:467:0x0022, inners: #0, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:406:0x096b  */
    /* JADX WARN: Removed duplicated region for block: B:409:0x0972 A[Catch: Exception -> 0x0b0c, TryCatch #1 {Exception -> 0x0b0c, blocks: (B:10:0x0022, B:12:0x0046, B:14:0x004a, B:16:0x0054, B:18:0x005a, B:21:0x006a, B:22:0x006c, B:26:0x007a, B:28:0x0086, B:29:0x008c, B:31:0x009e, B:33:0x00ac, B:35:0x00b2, B:36:0x00b5, B:38:0x00bb, B:44:0x00c9, B:46:0x00d9, B:54:0x00f1, B:56:0x00f7, B:58:0x00fc, B:59:0x00ff, B:60:0x0103, B:62:0x010b, B:67:0x0113, B:69:0x0119, B:78:0x0135, B:79:0x013f, B:80:0x0149, B:82:0x0153, B:85:0x0160, B:87:0x0168, B:88:0x0175, B:90:0x0195, B:92:0x01a0, B:97:0x01ac, B:100:0x01b7, B:101:0x01d2, B:102:0x020d, B:105:0x0223, B:110:0x0240, B:111:0x0254, B:113:0x0259, B:114:0x026d, B:116:0x0282, B:117:0x0299, B:119:0x02bd, B:121:0x02d5, B:126:0x02df, B:127:0x02e5, B:131:0x02f2, B:132:0x0306, B:134:0x030b, B:135:0x031f, B:136:0x0332, B:138:0x033a, B:139:0x0343, B:142:0x0357, B:153:0x0372, B:155:0x038c, B:158:0x03c3, B:160:0x03cd, B:161:0x03e7, B:163:0x03fc, B:164:0x0408, B:166:0x040c, B:172:0x042f, B:175:0x0449, B:179:0x0458, B:181:0x046f, B:183:0x04b7, B:184:0x04c3, B:185:0x04da, B:187:0x04f1, B:194:0x0525, B:196:0x0535, B:197:0x0541, B:198:0x0548, B:199:0x0565, B:201:0x0577, B:202:0x0583, B:203:0x058a, B:206:0x05b1, B:208:0x05bb, B:209:0x05c7, B:210:0x05ce, B:217:0x0609, B:219:0x060f, B:231:0x0629, B:233:0x062f, B:241:0x0641, B:244:0x064b, B:247:0x0654, B:263:0x0677, B:267:0x0682, B:269:0x06b8, B:273:0x06c7, B:276:0x06d3, B:277:0x06da, B:279:0x06e0, B:282:0x06e5, B:284:0x06ee, B:287:0x06f6, B:289:0x06fa, B:291:0x06fe, B:293:0x0706, B:297:0x0710, B:299:0x0716, B:301:0x071a, B:303:0x0722, B:307:0x072a, B:309:0x0735, B:311:0x073b, B:313:0x0745, B:316:0x07a6, B:318:0x07aa, B:320:0x07b0, B:321:0x07c6, B:323:0x07eb, B:325:0x07f8, B:343:0x0848, B:353:0x085c, B:357:0x0869, B:360:0x0872, B:363:0x087d, B:370:0x0890, B:372:0x0898, B:374:0x08a0, B:376:0x08c8, B:378:0x08cd, B:380:0x08d5, B:382:0x08d9, B:384:0x08e1, B:388:0x08ec, B:389:0x0902, B:390:0x0907, B:391:0x090a, B:393:0x0912, B:396:0x091c, B:398:0x0924, B:401:0x094f, B:402:0x0957, B:405:0x0961, B:409:0x0972, B:412:0x0983, B:419:0x09a4, B:422:0x09b8, B:424:0x09c1, B:425:0x09d3, B:427:0x09d9, B:429:0x09dd, B:431:0x09e8, B:433:0x09ee, B:435:0x09f8, B:437:0x0a07, B:439:0x0a17, B:441:0x0a36, B:442:0x0a3b, B:444:0x0a67, B:445:0x0a78, B:449:0x0a9b, B:451:0x0aa1, B:453:0x0aa9, B:455:0x0aaf, B:457:0x0ac1, B:458:0x0ada, B:459:0x0af2, B:400:0x092e, B:256:0x0668), top: B:467:0x0022, inners: #0, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:410:0x0980  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0119 A[Catch: Exception -> 0x0b0c, TryCatch #1 {Exception -> 0x0b0c, blocks: (B:10:0x0022, B:12:0x0046, B:14:0x004a, B:16:0x0054, B:18:0x005a, B:21:0x006a, B:22:0x006c, B:26:0x007a, B:28:0x0086, B:29:0x008c, B:31:0x009e, B:33:0x00ac, B:35:0x00b2, B:36:0x00b5, B:38:0x00bb, B:44:0x00c9, B:46:0x00d9, B:54:0x00f1, B:56:0x00f7, B:58:0x00fc, B:59:0x00ff, B:60:0x0103, B:62:0x010b, B:67:0x0113, B:69:0x0119, B:78:0x0135, B:79:0x013f, B:80:0x0149, B:82:0x0153, B:85:0x0160, B:87:0x0168, B:88:0x0175, B:90:0x0195, B:92:0x01a0, B:97:0x01ac, B:100:0x01b7, B:101:0x01d2, B:102:0x020d, B:105:0x0223, B:110:0x0240, B:111:0x0254, B:113:0x0259, B:114:0x026d, B:116:0x0282, B:117:0x0299, B:119:0x02bd, B:121:0x02d5, B:126:0x02df, B:127:0x02e5, B:131:0x02f2, B:132:0x0306, B:134:0x030b, B:135:0x031f, B:136:0x0332, B:138:0x033a, B:139:0x0343, B:142:0x0357, B:153:0x0372, B:155:0x038c, B:158:0x03c3, B:160:0x03cd, B:161:0x03e7, B:163:0x03fc, B:164:0x0408, B:166:0x040c, B:172:0x042f, B:175:0x0449, B:179:0x0458, B:181:0x046f, B:183:0x04b7, B:184:0x04c3, B:185:0x04da, B:187:0x04f1, B:194:0x0525, B:196:0x0535, B:197:0x0541, B:198:0x0548, B:199:0x0565, B:201:0x0577, B:202:0x0583, B:203:0x058a, B:206:0x05b1, B:208:0x05bb, B:209:0x05c7, B:210:0x05ce, B:217:0x0609, B:219:0x060f, B:231:0x0629, B:233:0x062f, B:241:0x0641, B:244:0x064b, B:247:0x0654, B:263:0x0677, B:267:0x0682, B:269:0x06b8, B:273:0x06c7, B:276:0x06d3, B:277:0x06da, B:279:0x06e0, B:282:0x06e5, B:284:0x06ee, B:287:0x06f6, B:289:0x06fa, B:291:0x06fe, B:293:0x0706, B:297:0x0710, B:299:0x0716, B:301:0x071a, B:303:0x0722, B:307:0x072a, B:309:0x0735, B:311:0x073b, B:313:0x0745, B:316:0x07a6, B:318:0x07aa, B:320:0x07b0, B:321:0x07c6, B:323:0x07eb, B:325:0x07f8, B:343:0x0848, B:353:0x085c, B:357:0x0869, B:360:0x0872, B:363:0x087d, B:370:0x0890, B:372:0x0898, B:374:0x08a0, B:376:0x08c8, B:378:0x08cd, B:380:0x08d5, B:382:0x08d9, B:384:0x08e1, B:388:0x08ec, B:389:0x0902, B:390:0x0907, B:391:0x090a, B:393:0x0912, B:396:0x091c, B:398:0x0924, B:401:0x094f, B:402:0x0957, B:405:0x0961, B:409:0x0972, B:412:0x0983, B:419:0x09a4, B:422:0x09b8, B:424:0x09c1, B:425:0x09d3, B:427:0x09d9, B:429:0x09dd, B:431:0x09e8, B:433:0x09ee, B:435:0x09f8, B:437:0x0a07, B:439:0x0a17, B:441:0x0a36, B:442:0x0a3b, B:444:0x0a67, B:445:0x0a78, B:449:0x0a9b, B:451:0x0aa1, B:453:0x0aa9, B:455:0x0aaf, B:457:0x0ac1, B:458:0x0ada, B:459:0x0af2, B:400:0x092e, B:256:0x0668), top: B:467:0x0022, inners: #0, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x012b  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0160 A[Catch: Exception -> 0x0b0c, TRY_ENTER, TryCatch #1 {Exception -> 0x0b0c, blocks: (B:10:0x0022, B:12:0x0046, B:14:0x004a, B:16:0x0054, B:18:0x005a, B:21:0x006a, B:22:0x006c, B:26:0x007a, B:28:0x0086, B:29:0x008c, B:31:0x009e, B:33:0x00ac, B:35:0x00b2, B:36:0x00b5, B:38:0x00bb, B:44:0x00c9, B:46:0x00d9, B:54:0x00f1, B:56:0x00f7, B:58:0x00fc, B:59:0x00ff, B:60:0x0103, B:62:0x010b, B:67:0x0113, B:69:0x0119, B:78:0x0135, B:79:0x013f, B:80:0x0149, B:82:0x0153, B:85:0x0160, B:87:0x0168, B:88:0x0175, B:90:0x0195, B:92:0x01a0, B:97:0x01ac, B:100:0x01b7, B:101:0x01d2, B:102:0x020d, B:105:0x0223, B:110:0x0240, B:111:0x0254, B:113:0x0259, B:114:0x026d, B:116:0x0282, B:117:0x0299, B:119:0x02bd, B:121:0x02d5, B:126:0x02df, B:127:0x02e5, B:131:0x02f2, B:132:0x0306, B:134:0x030b, B:135:0x031f, B:136:0x0332, B:138:0x033a, B:139:0x0343, B:142:0x0357, B:153:0x0372, B:155:0x038c, B:158:0x03c3, B:160:0x03cd, B:161:0x03e7, B:163:0x03fc, B:164:0x0408, B:166:0x040c, B:172:0x042f, B:175:0x0449, B:179:0x0458, B:181:0x046f, B:183:0x04b7, B:184:0x04c3, B:185:0x04da, B:187:0x04f1, B:194:0x0525, B:196:0x0535, B:197:0x0541, B:198:0x0548, B:199:0x0565, B:201:0x0577, B:202:0x0583, B:203:0x058a, B:206:0x05b1, B:208:0x05bb, B:209:0x05c7, B:210:0x05ce, B:217:0x0609, B:219:0x060f, B:231:0x0629, B:233:0x062f, B:241:0x0641, B:244:0x064b, B:247:0x0654, B:263:0x0677, B:267:0x0682, B:269:0x06b8, B:273:0x06c7, B:276:0x06d3, B:277:0x06da, B:279:0x06e0, B:282:0x06e5, B:284:0x06ee, B:287:0x06f6, B:289:0x06fa, B:291:0x06fe, B:293:0x0706, B:297:0x0710, B:299:0x0716, B:301:0x071a, B:303:0x0722, B:307:0x072a, B:309:0x0735, B:311:0x073b, B:313:0x0745, B:316:0x07a6, B:318:0x07aa, B:320:0x07b0, B:321:0x07c6, B:323:0x07eb, B:325:0x07f8, B:343:0x0848, B:353:0x085c, B:357:0x0869, B:360:0x0872, B:363:0x087d, B:370:0x0890, B:372:0x0898, B:374:0x08a0, B:376:0x08c8, B:378:0x08cd, B:380:0x08d5, B:382:0x08d9, B:384:0x08e1, B:388:0x08ec, B:389:0x0902, B:390:0x0907, B:391:0x090a, B:393:0x0912, B:396:0x091c, B:398:0x0924, B:401:0x094f, B:402:0x0957, B:405:0x0961, B:409:0x0972, B:412:0x0983, B:419:0x09a4, B:422:0x09b8, B:424:0x09c1, B:425:0x09d3, B:427:0x09d9, B:429:0x09dd, B:431:0x09e8, B:433:0x09ee, B:435:0x09f8, B:437:0x0a07, B:439:0x0a17, B:441:0x0a36, B:442:0x0a3b, B:444:0x0a67, B:445:0x0a78, B:449:0x0a9b, B:451:0x0aa1, B:453:0x0aa9, B:455:0x0aaf, B:457:0x0ac1, B:458:0x0ada, B:459:0x0af2, B:400:0x092e, B:256:0x0668), top: B:467:0x0022, inners: #0, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0194  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01a0 A[Catch: Exception -> 0x0b0c, TryCatch #1 {Exception -> 0x0b0c, blocks: (B:10:0x0022, B:12:0x0046, B:14:0x004a, B:16:0x0054, B:18:0x005a, B:21:0x006a, B:22:0x006c, B:26:0x007a, B:28:0x0086, B:29:0x008c, B:31:0x009e, B:33:0x00ac, B:35:0x00b2, B:36:0x00b5, B:38:0x00bb, B:44:0x00c9, B:46:0x00d9, B:54:0x00f1, B:56:0x00f7, B:58:0x00fc, B:59:0x00ff, B:60:0x0103, B:62:0x010b, B:67:0x0113, B:69:0x0119, B:78:0x0135, B:79:0x013f, B:80:0x0149, B:82:0x0153, B:85:0x0160, B:87:0x0168, B:88:0x0175, B:90:0x0195, B:92:0x01a0, B:97:0x01ac, B:100:0x01b7, B:101:0x01d2, B:102:0x020d, B:105:0x0223, B:110:0x0240, B:111:0x0254, B:113:0x0259, B:114:0x026d, B:116:0x0282, B:117:0x0299, B:119:0x02bd, B:121:0x02d5, B:126:0x02df, B:127:0x02e5, B:131:0x02f2, B:132:0x0306, B:134:0x030b, B:135:0x031f, B:136:0x0332, B:138:0x033a, B:139:0x0343, B:142:0x0357, B:153:0x0372, B:155:0x038c, B:158:0x03c3, B:160:0x03cd, B:161:0x03e7, B:163:0x03fc, B:164:0x0408, B:166:0x040c, B:172:0x042f, B:175:0x0449, B:179:0x0458, B:181:0x046f, B:183:0x04b7, B:184:0x04c3, B:185:0x04da, B:187:0x04f1, B:194:0x0525, B:196:0x0535, B:197:0x0541, B:198:0x0548, B:199:0x0565, B:201:0x0577, B:202:0x0583, B:203:0x058a, B:206:0x05b1, B:208:0x05bb, B:209:0x05c7, B:210:0x05ce, B:217:0x0609, B:219:0x060f, B:231:0x0629, B:233:0x062f, B:241:0x0641, B:244:0x064b, B:247:0x0654, B:263:0x0677, B:267:0x0682, B:269:0x06b8, B:273:0x06c7, B:276:0x06d3, B:277:0x06da, B:279:0x06e0, B:282:0x06e5, B:284:0x06ee, B:287:0x06f6, B:289:0x06fa, B:291:0x06fe, B:293:0x0706, B:297:0x0710, B:299:0x0716, B:301:0x071a, B:303:0x0722, B:307:0x072a, B:309:0x0735, B:311:0x073b, B:313:0x0745, B:316:0x07a6, B:318:0x07aa, B:320:0x07b0, B:321:0x07c6, B:323:0x07eb, B:325:0x07f8, B:343:0x0848, B:353:0x085c, B:357:0x0869, B:360:0x0872, B:363:0x087d, B:370:0x0890, B:372:0x0898, B:374:0x08a0, B:376:0x08c8, B:378:0x08cd, B:380:0x08d5, B:382:0x08d9, B:384:0x08e1, B:388:0x08ec, B:389:0x0902, B:390:0x0907, B:391:0x090a, B:393:0x0912, B:396:0x091c, B:398:0x0924, B:401:0x094f, B:402:0x0957, B:405:0x0961, B:409:0x0972, B:412:0x0983, B:419:0x09a4, B:422:0x09b8, B:424:0x09c1, B:425:0x09d3, B:427:0x09d9, B:429:0x09dd, B:431:0x09e8, B:433:0x09ee, B:435:0x09f8, B:437:0x0a07, B:439:0x0a17, B:441:0x0a36, B:442:0x0a3b, B:444:0x0a67, B:445:0x0a78, B:449:0x0a9b, B:451:0x0aa1, B:453:0x0aa9, B:455:0x0aaf, B:457:0x0ac1, B:458:0x0ada, B:459:0x0af2, B:400:0x092e, B:256:0x0668), top: B:467:0x0022, inners: #0, #3 }] */
    /* JADX WARN: Type inference failed for: r1v51 */
    /* JADX WARN: Type inference failed for: r6v86 */
    /* JADX WARN: Type inference failed for: r6v87 */
    /* JADX WARN: Type inference failed for: r6v88 */
    /* JADX WARN: Type inference failed for: r6v89 */
    /* JADX WARN: Type inference failed for: r6v94 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showOrUpdateNotification(boolean z) {
        MessageObject messageObject;
        SharedPreferences notificationsSettings;
        int i;
        long j;
        boolean z2;
        TLRPC$Chat tLRPC$Chat;
        boolean z3;
        String str;
        boolean z4;
        MessageObject messageObject2;
        boolean z5;
        String str2;
        String str3;
        long j2;
        long j3;
        long j4;
        boolean z6;
        boolean z7;
        String str4;
        String str5;
        NotificationCompat.Builder builder;
        boolean z8;
        String str6;
        boolean z9;
        CharSequence charSequence;
        String str7;
        SharedPreferences sharedPreferences;
        long j5;
        boolean z10;
        boolean z11;
        int i2;
        Integer num;
        int i3;
        String str8;
        boolean z12;
        NotificationCompat.Builder builder2;
        CharSequence charSequence2;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        String str9;
        boolean z13;
        boolean z14;
        int i9;
        boolean z15;
        int i10;
        TLRPC$FileLocation tLRPC$FileLocation;
        TLRPC$User tLRPC$User;
        NotificationCompat.Builder builder3;
        long[] jArr;
        int i11;
        boolean z16;
        Uri uri;
        int i12;
        long[] jArr2;
        Uri uri2;
        long j6;
        boolean z17;
        int i13;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup;
        MessageObject messageObject3;
        TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow;
        Uri uri3;
        int i14;
        long j7;
        long[] jArr3;
        String str10;
        String str11;
        TLRPC$FileLocation tLRPC$FileLocation2;
        int ringerMode;
        String str12;
        int i15;
        boolean z18;
        int i16;
        String str13;
        String str14;
        String str15;
        boolean z19;
        int i17;
        int i18;
        NotificationCompat.InboxStyle inboxStyle;
        String str16;
        if (!getUserConfig().isClientActivated() || this.pushMessages.isEmpty() || (!SharedConfig.showNotificationsForAllAccounts && this.currentAccount != UserConfig.selectedAccount)) {
            dismissNotification();
            return;
        }
        try {
            getConnectionsManager().resumeNetworkMaybe();
            messageObject = this.pushMessages.get(0);
            notificationsSettings = getAccountInstance().getNotificationsSettings();
            i = notificationsSettings.getInt("dismissDate", 0);
        } catch (Exception e) {
            FileLog.e(e);
            return;
        }
        if (messageObject.messageOwner.date <= i) {
            dismissNotification();
            return;
        }
        long dialogId = messageObject.getDialogId();
        long fromChatId = messageObject.messageOwner.mentioned ? messageObject.getFromChatId() : dialogId;
        messageObject.getId();
        TLRPC$Peer tLRPC$Peer = messageObject.messageOwner.peer_id;
        long j8 = tLRPC$Peer.chat_id;
        if (j8 == 0) {
            j8 = tLRPC$Peer.channel_id;
        }
        long j9 = tLRPC$Peer.user_id;
        if (messageObject.isFromUser() && (j9 == 0 || j9 == getUserConfig().getClientUserId())) {
            j9 = messageObject.messageOwner.from_id.user_id;
        }
        TLRPC$User user = getMessagesController().getUser(Long.valueOf(j9));
        if (j8 != 0) {
            tLRPC$Chat = getMessagesController().getChat(Long.valueOf(j8));
            if (tLRPC$Chat == null && messageObject.isFcmMessage()) {
                z2 = messageObject.localChannel;
            } else {
                z2 = ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup;
            }
            j = j9;
        } else {
            j = j9;
            tLRPC$Chat = null;
            z2 = false;
        }
        int notifyOverride = getNotifyOverride(notificationsSettings, fromChatId);
        if (notifyOverride == -1) {
            z3 = isGlobalNotificationsEnabled(dialogId, Boolean.valueOf(z2));
        } else {
            z3 = notifyOverride != 2;
        }
        if (((j8 != 0 && tLRPC$Chat == null) || user == null) && messageObject.isFcmMessage()) {
            str = messageObject.localName;
        } else if (tLRPC$Chat != null) {
            str = tLRPC$Chat.title;
        } else {
            str = UserObject.getUserName(user);
        }
        String str17 = str;
        if (!AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
            z4 = false;
            if (DialogObject.isEncryptedDialog(dialogId)) {
                messageObject2 = messageObject;
                if (this.pushDialogs.size() <= 1 && !z4) {
                    str2 = str17;
                    z5 = true;
                    boolean z20 = z2;
                    if (UserConfig.getActivatedAccountsCount() <= 1) {
                        str3 = "";
                    } else if (this.pushDialogs.size() == 1) {
                        str3 = UserObject.getFirstName(getUserConfig().getCurrentUser());
                    } else {
                        str3 = UserObject.getFirstName(getUserConfig().getCurrentUser()) + "・";
                    }
                    j2 = j8;
                    if (this.pushDialogs.size() == 1 && Build.VERSION.SDK_INT >= 23) {
                        j4 = dialogId;
                        j3 = fromChatId;
                        NotificationCompat.Builder builder4 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                        if (this.pushMessages.size() != 1) {
                            MessageObject messageObject4 = this.pushMessages.get(0);
                            boolean[] zArr = new boolean[1];
                            z6 = z3;
                            String stringForMessage = getStringForMessage(messageObject4, false, zArr, null);
                            z7 = isSilentMessage(messageObject4);
                            if (stringForMessage == null) {
                                return;
                            }
                            if (!z5) {
                                str16 = stringForMessage;
                            } else if (tLRPC$Chat != null) {
                                str16 = stringForMessage.replace(" @ " + str2, "");
                            } else if (zArr[0]) {
                                str16 = stringForMessage.replace(str2 + ": ", "");
                            } else {
                                str16 = stringForMessage.replace(str2 + " ", "");
                            }
                            builder4.setContentText(str16);
                            builder4.setStyle(new NotificationCompat.BigTextStyle().bigText(str16));
                            builder = builder4;
                            String str18 = str3;
                            str4 = stringForMessage;
                            str5 = str18;
                        } else {
                            z6 = z3;
                            builder4.setContentText(str3);
                            NotificationCompat.InboxStyle inboxStyle2 = new NotificationCompat.InboxStyle();
                            inboxStyle2.setBigContentTitle(str2);
                            int min = Math.min(10, this.pushMessages.size());
                            boolean[] zArr2 = new boolean[1];
                            boolean z21 = 2;
                            int i19 = 0;
                            String str19 = null;
                            while (i19 < min) {
                                int i20 = min;
                                MessageObject messageObject5 = this.pushMessages.get(i19);
                                NotificationCompat.InboxStyle inboxStyle3 = inboxStyle2;
                                String str20 = str3;
                                int i21 = i19;
                                String stringForMessage2 = getStringForMessage(messageObject5, false, zArr2, null);
                                if (stringForMessage2 != null && messageObject5.messageOwner.date > i) {
                                    z21 = z21;
                                    if (z21 == 2) {
                                        str19 = stringForMessage2;
                                        z21 = isSilentMessage(messageObject5);
                                    }
                                    if (this.pushDialogs.size() == 1 && z5) {
                                        if (tLRPC$Chat != null) {
                                            stringForMessage2 = stringForMessage2.replace(" @ " + str2, "");
                                        } else if (zArr2[0]) {
                                            stringForMessage2 = stringForMessage2.replace(str2 + ": ", "");
                                        } else {
                                            stringForMessage2 = stringForMessage2.replace(str2 + " ", "");
                                        }
                                    }
                                    inboxStyle = inboxStyle3;
                                    inboxStyle.addLine(stringForMessage2);
                                    i19 = i21 + 1;
                                    inboxStyle2 = inboxStyle;
                                    min = i20;
                                    str3 = str20;
                                    z21 = z21;
                                }
                                inboxStyle = inboxStyle3;
                                i19 = i21 + 1;
                                inboxStyle2 = inboxStyle;
                                min = i20;
                                str3 = str20;
                                z21 = z21;
                            }
                            String str21 = str3;
                            NotificationCompat.InboxStyle inboxStyle4 = inboxStyle2;
                            str5 = str21;
                            inboxStyle4.setSummaryText(str5);
                            builder = builder4;
                            builder.setStyle(inboxStyle4);
                            z7 = z21 == true ? 1 : 0;
                            str4 = str19;
                        }
                        z8 = z || !z6 || MediaController.getInstance().isRecordingAudio() || z7;
                        if (!z8 || j4 != j3 || tLRPC$Chat == null) {
                            str6 = str5;
                            str7 = str4;
                            sharedPreferences = notificationsSettings;
                            j5 = j4;
                            z9 = z8;
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("custom_");
                            j5 = j4;
                            sb.append(j5);
                            sharedPreferences = notificationsSettings;
                            if (sharedPreferences.getBoolean(sb.toString(), false)) {
                                i18 = sharedPreferences.getInt("smart_max_count_" + j5, 2);
                                StringBuilder sb2 = new StringBuilder();
                                z9 = z8;
                                sb2.append("smart_delay_");
                                sb2.append(j5);
                                i17 = sharedPreferences.getInt(sb2.toString(), 180);
                            } else {
                                z9 = z8;
                                i17 = 180;
                                i18 = 2;
                            }
                            if (i18 != 0) {
                                Point point = this.smartNotificationsDialogs.get(j5);
                                if (point == null) {
                                    charSequence = "";
                                    this.smartNotificationsDialogs.put(j5, new Point(1, (int) (SystemClock.elapsedRealtime() / 1000)));
                                    str6 = str5;
                                    str7 = str4;
                                } else {
                                    charSequence = "";
                                    int i22 = point.y + i17;
                                    str6 = str5;
                                    str7 = str4;
                                    if (i22 < SystemClock.elapsedRealtime() / 1000) {
                                        point.set(1, (int) (SystemClock.elapsedRealtime() / 1000));
                                    } else {
                                        int i23 = point.x;
                                        if (i23 < i18) {
                                            point.set(i23 + 1, (int) (SystemClock.elapsedRealtime() / 1000));
                                        } else {
                                            z9 = true;
                                        }
                                    }
                                }
                                if (!z9) {
                                    if (!sharedPreferences.getBoolean("sound_enabled_" + j5, true)) {
                                        z9 = true;
                                    }
                                }
                                String path = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                                z10 = !ApplicationLoader.mainInterfacePaused;
                                if (sharedPreferences.getBoolean("custom_" + j5, false)) {
                                    int i24 = sharedPreferences.getInt("vibrate_" + j5, 0);
                                    int i25 = sharedPreferences.getInt("priority_" + j5, 3);
                                    long j10 = sharedPreferences.getLong("sound_document_id_" + j5, 0L);
                                    if (j10 != 0) {
                                        str8 = getMediaDataController().ringtoneDataStore.getSoundPath(j10);
                                        z19 = true;
                                    } else {
                                        str8 = sharedPreferences.getString("sound_path_" + j5, null);
                                        z19 = false;
                                    }
                                    if (sharedPreferences.contains("color_" + j5)) {
                                        num = Integer.valueOf(sharedPreferences.getInt("color_" + j5, 0));
                                    } else {
                                        num = null;
                                    }
                                    i2 = i24;
                                    z11 = z19;
                                    i3 = i25;
                                } else {
                                    str8 = null;
                                    i3 = 3;
                                    num = null;
                                    i2 = 0;
                                    z11 = false;
                                }
                                boolean z22 = z7;
                                if (j2 == 0) {
                                    charSequence2 = str2;
                                    builder2 = builder;
                                    i5 = i2;
                                    if (j != 0) {
                                        long j11 = sharedPreferences.getLong("GlobalSoundDocId", 0L);
                                        if (j11 != 0) {
                                            str13 = getMediaDataController().ringtoneDataStore.getSoundPath(j11);
                                            z18 = true;
                                        } else {
                                            str13 = sharedPreferences.getString("GlobalSoundPath", path);
                                            z18 = false;
                                        }
                                        i15 = sharedPreferences.getInt("vibrate_messages", 0);
                                        i6 = sharedPreferences.getInt("priority_messages", 1);
                                        str12 = str13;
                                        i16 = sharedPreferences.getInt("MessagesLed", -16776961);
                                        i4 = 1;
                                    } else {
                                        str9 = 0;
                                        i8 = 0;
                                        i7 = -16776961;
                                        i6 = 0;
                                        i4 = 1;
                                        z12 = false;
                                        if (i8 != 4) {
                                            i8 = 0;
                                            z13 = true;
                                        } else {
                                            z13 = false;
                                        }
                                        if (!TextUtils.isEmpty(str8) || TextUtils.equals(str9, str8)) {
                                            z11 = z12;
                                            z14 = true;
                                        } else {
                                            str9 = str8;
                                            z14 = false;
                                        }
                                        if (i3 == 3) {
                                            i9 = i5;
                                            if (i6 != i3) {
                                                z14 = false;
                                                if (num != null && num.intValue() != i7) {
                                                    i7 = num.intValue();
                                                    z14 = false;
                                                }
                                                if (i9 != 0 || i9 == 4 || i9 == i8) {
                                                    z15 = z14;
                                                } else {
                                                    i8 = i9;
                                                    z15 = false;
                                                }
                                                if (z10) {
                                                    if (!sharedPreferences.getBoolean("EnableInAppSounds", true)) {
                                                        str9 = null;
                                                    }
                                                    if (!sharedPreferences.getBoolean("EnableInAppVibrate", true)) {
                                                        i8 = 2;
                                                    }
                                                    if (!sharedPreferences.getBoolean("EnableInAppPriority", false)) {
                                                        i3 = 0;
                                                    } else if (i3 == 2) {
                                                        i3 = 1;
                                                    }
                                                }
                                                if (z13 && i8 != 2) {
                                                    try {
                                                        ringerMode = audioManager.getRingerMode();
                                                        if (ringerMode != 0 && ringerMode != 1) {
                                                            i8 = 2;
                                                        }
                                                    } catch (Exception e2) {
                                                        FileLog.e(e2);
                                                    }
                                                }
                                                if (z9) {
                                                    str9 = null;
                                                    i8 = 0;
                                                    i3 = 0;
                                                    i10 = 0;
                                                } else {
                                                    i10 = i7;
                                                }
                                                Intent intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                StringBuilder sb3 = new StringBuilder();
                                                sb3.append("com.tmessages.openchat");
                                                int i26 = i10;
                                                boolean z23 = z15;
                                                sb3.append(Math.random());
                                                sb3.append(Integer.MAX_VALUE);
                                                intent.setAction(sb3.toString());
                                                intent.setFlags(ConnectionsManager.FileTypeFile);
                                                if (!DialogObject.isEncryptedDialog(j5)) {
                                                    if (this.pushDialogs.size() == 1) {
                                                        if (j2 != 0) {
                                                            intent.putExtra("chatId", j2);
                                                        } else if (j != 0) {
                                                            intent.putExtra("userId", j);
                                                        }
                                                    }
                                                    if (!AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter && this.pushDialogs.size() == 1 && Build.VERSION.SDK_INT < 28) {
                                                        if (tLRPC$Chat != null) {
                                                            TLRPC$ChatPhoto tLRPC$ChatPhoto = tLRPC$Chat.photo;
                                                            if (tLRPC$ChatPhoto != null && (tLRPC$FileLocation2 = tLRPC$ChatPhoto.photo_small) != null && tLRPC$FileLocation2.volume_id != 0 && tLRPC$FileLocation2.local_id != 0) {
                                                                tLRPC$FileLocation = tLRPC$FileLocation2;
                                                                tLRPC$User = user;
                                                            }
                                                        } else if (user != null) {
                                                            tLRPC$User = user;
                                                            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = tLRPC$User.photo;
                                                            if (tLRPC$UserProfilePhoto != null && (tLRPC$FileLocation = tLRPC$UserProfilePhoto.photo_small) != null && tLRPC$FileLocation.volume_id != 0 && tLRPC$FileLocation.local_id != 0) {
                                                            }
                                                        }
                                                        intent.putExtra("currentAccount", this.currentAccount);
                                                        long j12 = j5;
                                                        builder3 = builder2;
                                                        MessageObject messageObject6 = messageObject2;
                                                        int i27 = i8;
                                                        builder3.setContentTitle(charSequence2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject6.messageOwner.date * 1000).setColor(-15618822);
                                                        builder3.setCategory(RemoteMessageConst.MessageBody.MSG);
                                                        if (tLRPC$Chat == null && tLRPC$User != null && (str11 = tLRPC$User.phone) != null && str11.length() > 0) {
                                                            builder3.addPerson("tel:+" + tLRPC$User.phone);
                                                        }
                                                        Intent intent2 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                        intent2.putExtra("messageDate", messageObject6.messageOwner.date);
                                                        intent2.putExtra("currentAccount", this.currentAccount);
                                                        builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent2, 134217728));
                                                        if (tLRPC$FileLocation == null) {
                                                            jArr = null;
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
                                                        } else {
                                                            jArr = null;
                                                        }
                                                        if (z || z22) {
                                                            builder3.setPriority(-1);
                                                        } else {
                                                            if (i3 == 0) {
                                                                builder3.setPriority(0);
                                                                if (Build.VERSION.SDK_INT >= 26) {
                                                                    z16 = true;
                                                                    i11 = 3;
                                                                }
                                                                z16 = true;
                                                                i11 = 0;
                                                            } else {
                                                                if (i3 != 1 && i3 != 2) {
                                                                    if (i3 == 4) {
                                                                        builder3.setPriority(-2);
                                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                                            z16 = true;
                                                                            i11 = 1;
                                                                        }
                                                                    } else if (i3 == 5) {
                                                                        builder3.setPriority(-1);
                                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                                            z16 = true;
                                                                            i11 = 2;
                                                                        }
                                                                    }
                                                                    z16 = true;
                                                                    i11 = 0;
                                                                }
                                                                builder3.setPriority(1);
                                                                if (Build.VERSION.SDK_INT >= 26) {
                                                                    z16 = true;
                                                                    i11 = 4;
                                                                }
                                                                z16 = true;
                                                                i11 = 0;
                                                            }
                                                            FileLog.e(e);
                                                            return;
                                                        }
                                                        if (z22 != z16 && !z9) {
                                                            if (!z10 || sharedPreferences.getBoolean("EnableInAppPreview", z16)) {
                                                                if (str7.length() > 100) {
                                                                    str10 = str7.substring(0, 100).replace('\n', ' ').trim() + "...";
                                                                } else {
                                                                    str10 = str7;
                                                                }
                                                                builder3.setTicker(str10);
                                                            }
                                                            if (str9 != null && !str9.equals("NoSound")) {
                                                                int i28 = Build.VERSION.SDK_INT;
                                                                if (i28 >= 26) {
                                                                    if (!str9.equals("Default") && !str9.equals(path)) {
                                                                        if (z11) {
                                                                            uri = FileProvider.getUriForFile(ApplicationLoader.applicationContext, "org.telegram.messenger.beta.provider", new File(str9));
                                                                            ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uri, 1);
                                                                        } else {
                                                                            uri = Uri.parse(str9);
                                                                        }
                                                                        if (i26 != 0) {
                                                                            i12 = i26;
                                                                            builder3.setLights(i12, 1000, 1000);
                                                                        } else {
                                                                            i12 = i26;
                                                                        }
                                                                        if (i27 == 2) {
                                                                            jArr3 = new long[]{0, 0};
                                                                            builder3.setVibrate(jArr3);
                                                                        } else if (i27 == 1) {
                                                                            jArr3 = new long[]{0, 100, 0, 100};
                                                                            builder3.setVibrate(jArr3);
                                                                        } else {
                                                                            if (i27 != 0 && i27 != 4) {
                                                                                if (i27 == 3) {
                                                                                    jArr3 = new long[]{0, 1000};
                                                                                    builder3.setVibrate(jArr3);
                                                                                } else {
                                                                                    jArr2 = jArr;
                                                                                }
                                                                            }
                                                                            builder3.setDefaults(2);
                                                                            jArr3 = new long[0];
                                                                        }
                                                                        jArr2 = jArr3;
                                                                    }
                                                                    uri = Settings.System.DEFAULT_NOTIFICATION_URI;
                                                                    if (i26 != 0) {
                                                                    }
                                                                    if (i27 == 2) {
                                                                    }
                                                                    jArr2 = jArr3;
                                                                } else if (str9.equals(path)) {
                                                                    builder3.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, 5);
                                                                } else if (i28 >= 24 && str9.startsWith("file://") && !AndroidUtilities.isInternalUri(Uri.parse(str9))) {
                                                                    try {
                                                                        Uri uriForFile = FileProvider.getUriForFile(ApplicationLoader.applicationContext, "org.telegram.messenger.beta.provider", new File(str9.replace("file://", charSequence)));
                                                                        ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile, 1);
                                                                        builder3.setSound(uriForFile, 5);
                                                                    } catch (Exception unused2) {
                                                                        builder3.setSound(Uri.parse(str9), 5);
                                                                    }
                                                                } else {
                                                                    builder3.setSound(Uri.parse(str9), 5);
                                                                }
                                                            }
                                                            uri = jArr;
                                                            if (i26 != 0) {
                                                            }
                                                            if (i27 == 2) {
                                                            }
                                                            jArr2 = jArr3;
                                                        } else {
                                                            i12 = i26;
                                                            long[] jArr4 = {0, 0};
                                                            builder3.setVibrate(jArr4);
                                                            jArr2 = jArr4;
                                                            uri = jArr;
                                                        }
                                                        if (AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter || messageObject6.getDialogId() != 777000 || (tLRPC$ReplyMarkup = messageObject6.messageOwner.reply_markup) == null) {
                                                            uri2 = uri;
                                                            j6 = j12;
                                                            z17 = false;
                                                        } else {
                                                            ArrayList<TLRPC$TL_keyboardButtonRow> arrayList = tLRPC$ReplyMarkup.rows;
                                                            int size = arrayList.size();
                                                            int i29 = 0;
                                                            boolean z24 = false;
                                                            while (i29 < size) {
                                                                TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow2 = arrayList.get(i29);
                                                                int size2 = tLRPC$TL_keyboardButtonRow2.buttons.size();
                                                                int i30 = 0;
                                                                while (i30 < size2) {
                                                                    int i31 = size2;
                                                                    TLRPC$KeyboardButton tLRPC$KeyboardButton = tLRPC$TL_keyboardButtonRow2.buttons.get(i30);
                                                                    ArrayList<TLRPC$TL_keyboardButtonRow> arrayList2 = arrayList;
                                                                    if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonCallback) {
                                                                        i14 = size;
                                                                        Intent intent3 = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                                                        intent3.putExtra("currentAccount", this.currentAccount);
                                                                        tLRPC$TL_keyboardButtonRow = tLRPC$TL_keyboardButtonRow2;
                                                                        uri3 = uri;
                                                                        j7 = j12;
                                                                        intent3.putExtra("did", j7);
                                                                        byte[] bArr = tLRPC$KeyboardButton.data;
                                                                        if (bArr != null) {
                                                                            intent3.putExtra(RemoteMessageConst.DATA, bArr);
                                                                        }
                                                                        intent3.putExtra("mid", messageObject6.getId());
                                                                        String str22 = tLRPC$KeyboardButton.text;
                                                                        Context context = ApplicationLoader.applicationContext;
                                                                        int i32 = this.lastButtonId;
                                                                        messageObject3 = messageObject6;
                                                                        this.lastButtonId = i32 + 1;
                                                                        builder3.addAction(0, str22, PendingIntent.getBroadcast(context, i32, intent3, 134217728));
                                                                        z24 = true;
                                                                    } else {
                                                                        i14 = size;
                                                                        messageObject3 = messageObject6;
                                                                        tLRPC$TL_keyboardButtonRow = tLRPC$TL_keyboardButtonRow2;
                                                                        uri3 = uri;
                                                                        j7 = j12;
                                                                    }
                                                                    i30++;
                                                                    size2 = i31;
                                                                    j12 = j7;
                                                                    arrayList = arrayList2;
                                                                    size = i14;
                                                                    uri = uri3;
                                                                    tLRPC$TL_keyboardButtonRow2 = tLRPC$TL_keyboardButtonRow;
                                                                    messageObject6 = messageObject3;
                                                                }
                                                                i29++;
                                                                size = size;
                                                                uri = uri;
                                                            }
                                                            uri2 = uri;
                                                            j6 = j12;
                                                            z17 = z24;
                                                        }
                                                        if (!z17 && (i13 = Build.VERSION.SDK_INT) < 24 && SharedConfig.passcodeHash.length() == 0 && hasMessagesToReply()) {
                                                            Intent intent4 = new Intent(ApplicationLoader.applicationContext, PopupReplyReceiver.class);
                                                            intent4.putExtra("currentAccount", this.currentAccount);
                                                            if (i13 <= 19) {
                                                                builder3.addAction(org.telegram.messenger.beta.R.drawable.ic_ab_reply2, LocaleController.getString("Reply", org.telegram.messenger.beta.R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent4, 134217728));
                                                            } else {
                                                                builder3.addAction(org.telegram.messenger.beta.R.drawable.ic_ab_reply, LocaleController.getString("Reply", org.telegram.messenger.beta.R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent4, 134217728));
                                                            }
                                                        }
                                                        showExtraNotifications(builder3, str6, j6, str17, jArr2, i12, uri2, i11, z23, z10, z9, i4);
                                                        scheduleNotificationRepeat();
                                                        return;
                                                    }
                                                    tLRPC$User = user;
                                                } else {
                                                    tLRPC$User = user;
                                                    if (this.pushDialogs.size() == 1 && j5 != globalSecretChatId) {
                                                        intent.putExtra("encId", DialogObject.getEncryptedChatId(j5));
                                                    }
                                                }
                                                tLRPC$FileLocation = null;
                                                intent.putExtra("currentAccount", this.currentAccount);
                                                long j122 = j5;
                                                builder3 = builder2;
                                                MessageObject messageObject62 = messageObject2;
                                                int i272 = i8;
                                                builder3.setContentTitle(charSequence2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject62.messageOwner.date * 1000).setColor(-15618822);
                                                builder3.setCategory(RemoteMessageConst.MessageBody.MSG);
                                                if (tLRPC$Chat == null) {
                                                    builder3.addPerson("tel:+" + tLRPC$User.phone);
                                                }
                                                Intent intent22 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                intent22.putExtra("messageDate", messageObject62.messageOwner.date);
                                                intent22.putExtra("currentAccount", this.currentAccount);
                                                builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent22, 134217728));
                                                if (tLRPC$FileLocation == null) {
                                                }
                                                if (z) {
                                                }
                                                builder3.setPriority(-1);
                                            }
                                        } else {
                                            i9 = i5;
                                        }
                                        i3 = i6;
                                        if (num != null) {
                                            i7 = num.intValue();
                                            z14 = false;
                                        }
                                        if (i9 != 0) {
                                        }
                                        z15 = z14;
                                        if (z10) {
                                        }
                                        if (z13) {
                                            ringerMode = audioManager.getRingerMode();
                                            if (ringerMode != 0) {
                                                i8 = 2;
                                            }
                                        }
                                        if (z9) {
                                        }
                                        Intent intent5 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                        StringBuilder sb32 = new StringBuilder();
                                        sb32.append("com.tmessages.openchat");
                                        int i262 = i10;
                                        boolean z232 = z15;
                                        sb32.append(Math.random());
                                        sb32.append(Integer.MAX_VALUE);
                                        intent5.setAction(sb32.toString());
                                        intent5.setFlags(ConnectionsManager.FileTypeFile);
                                        if (!DialogObject.isEncryptedDialog(j5)) {
                                        }
                                        tLRPC$FileLocation = null;
                                        intent5.putExtra("currentAccount", this.currentAccount);
                                        long j1222 = j5;
                                        builder3 = builder2;
                                        MessageObject messageObject622 = messageObject2;
                                        int i2722 = i8;
                                        builder3.setContentTitle(charSequence2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent5, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject622.messageOwner.date * 1000).setColor(-15618822);
                                        builder3.setCategory(RemoteMessageConst.MessageBody.MSG);
                                        if (tLRPC$Chat == null) {
                                        }
                                        Intent intent222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                        intent222.putExtra("messageDate", messageObject622.messageOwner.date);
                                        intent222.putExtra("currentAccount", this.currentAccount);
                                        builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent222, 134217728));
                                        if (tLRPC$FileLocation == null) {
                                        }
                                        if (z) {
                                        }
                                        builder3.setPriority(-1);
                                    }
                                } else if (z20) {
                                    i5 = i2;
                                    charSequence2 = str2;
                                    builder2 = builder;
                                    long j13 = sharedPreferences.getLong("ChannelSoundDocId", 0L);
                                    if (j13 != 0) {
                                        str15 = getMediaDataController().ringtoneDataStore.getSoundPath(j13);
                                        z18 = true;
                                    } else {
                                        str15 = sharedPreferences.getString("ChannelSoundPath", path);
                                        z18 = false;
                                    }
                                    i15 = sharedPreferences.getInt("vibrate_channel", 0);
                                    i6 = sharedPreferences.getInt("priority_channel", 1);
                                    str12 = str15;
                                    i16 = sharedPreferences.getInt("ChannelLed", -16776961);
                                    i4 = 2;
                                } else {
                                    charSequence2 = str2;
                                    builder2 = builder;
                                    i5 = i2;
                                    long j14 = sharedPreferences.getLong("GroupSoundDocId", 0L);
                                    if (j14 != 0) {
                                        str14 = getMediaDataController().ringtoneDataStore.getSoundPath(j14);
                                        z18 = true;
                                    } else {
                                        str14 = sharedPreferences.getString("GroupSoundPath", path);
                                        z18 = false;
                                    }
                                    i15 = sharedPreferences.getInt("vibrate_group", 0);
                                    i6 = sharedPreferences.getInt("priority_group", 1);
                                    str12 = str14;
                                    i16 = sharedPreferences.getInt("GroupLed", -16776961);
                                    i4 = 0;
                                }
                                int i33 = i15;
                                i7 = i16;
                                str9 = str12;
                                z12 = z18;
                                i8 = i33;
                                if (i8 != 4) {
                                }
                                if (!TextUtils.isEmpty(str8)) {
                                }
                                z11 = z12;
                                z14 = true;
                                if (i3 == 3) {
                                }
                                i3 = i6;
                                if (num != null) {
                                }
                                if (i9 != 0) {
                                }
                                z15 = z14;
                                if (z10) {
                                }
                                if (z13) {
                                }
                                if (z9) {
                                }
                                Intent intent52 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                StringBuilder sb322 = new StringBuilder();
                                sb322.append("com.tmessages.openchat");
                                int i2622 = i10;
                                boolean z2322 = z15;
                                sb322.append(Math.random());
                                sb322.append(Integer.MAX_VALUE);
                                intent52.setAction(sb322.toString());
                                intent52.setFlags(ConnectionsManager.FileTypeFile);
                                if (!DialogObject.isEncryptedDialog(j5)) {
                                }
                                tLRPC$FileLocation = null;
                                intent52.putExtra("currentAccount", this.currentAccount);
                                long j12222 = j5;
                                builder3 = builder2;
                                MessageObject messageObject6222 = messageObject2;
                                int i27222 = i8;
                                builder3.setContentTitle(charSequence2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent52, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject6222.messageOwner.date * 1000).setColor(-15618822);
                                builder3.setCategory(RemoteMessageConst.MessageBody.MSG);
                                if (tLRPC$Chat == null) {
                                }
                                Intent intent2222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                intent2222.putExtra("messageDate", messageObject6222.messageOwner.date);
                                intent2222.putExtra("currentAccount", this.currentAccount);
                                builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent2222, 134217728));
                                if (tLRPC$FileLocation == null) {
                                }
                                if (z) {
                                }
                                builder3.setPriority(-1);
                            } else {
                                str6 = str5;
                                str7 = str4;
                            }
                        }
                        charSequence = "";
                        if (!z9) {
                        }
                        String path2 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                        if (!ApplicationLoader.mainInterfacePaused) {
                        }
                        if (sharedPreferences.getBoolean("custom_" + j5, false)) {
                        }
                        boolean z222 = z7;
                        if (j2 == 0) {
                        }
                        int i332 = i15;
                        i7 = i16;
                        str9 = str12;
                        z12 = z18;
                        i8 = i332;
                        if (i8 != 4) {
                        }
                        if (!TextUtils.isEmpty(str8)) {
                        }
                        z11 = z12;
                        z14 = true;
                        if (i3 == 3) {
                        }
                        i3 = i6;
                        if (num != null) {
                        }
                        if (i9 != 0) {
                        }
                        z15 = z14;
                        if (z10) {
                        }
                        if (z13) {
                        }
                        if (z9) {
                        }
                        Intent intent522 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        StringBuilder sb3222 = new StringBuilder();
                        sb3222.append("com.tmessages.openchat");
                        int i26222 = i10;
                        boolean z23222 = z15;
                        sb3222.append(Math.random());
                        sb3222.append(Integer.MAX_VALUE);
                        intent522.setAction(sb3222.toString());
                        intent522.setFlags(ConnectionsManager.FileTypeFile);
                        if (!DialogObject.isEncryptedDialog(j5)) {
                        }
                        tLRPC$FileLocation = null;
                        intent522.putExtra("currentAccount", this.currentAccount);
                        long j122222 = j5;
                        builder3 = builder2;
                        MessageObject messageObject62222 = messageObject2;
                        int i272222 = i8;
                        builder3.setContentTitle(charSequence2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent522, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject62222.messageOwner.date * 1000).setColor(-15618822);
                        builder3.setCategory(RemoteMessageConst.MessageBody.MSG);
                        if (tLRPC$Chat == null) {
                        }
                        Intent intent22222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                        intent22222.putExtra("messageDate", messageObject62222.messageOwner.date);
                        intent22222.putExtra("currentAccount", this.currentAccount);
                        builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent22222, 134217728));
                        if (tLRPC$FileLocation == null) {
                        }
                        if (z) {
                        }
                        builder3.setPriority(-1);
                    }
                    if (this.pushDialogs.size() == 1) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(str3);
                        j4 = dialogId;
                        sb4.append(LocaleController.formatPluralString("NewMessages", this.total_unread_count, new Object[0]));
                        str3 = sb4.toString();
                        j3 = fromChatId;
                        NotificationCompat.Builder builder42 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                        if (this.pushMessages.size() != 1) {
                        }
                        if (z) {
                        }
                        if (!z8) {
                        }
                        str6 = str5;
                        str7 = str4;
                        sharedPreferences = notificationsSettings;
                        j5 = j4;
                        z9 = z8;
                        charSequence = "";
                        if (!z9) {
                        }
                        String path22 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                        if (!ApplicationLoader.mainInterfacePaused) {
                        }
                        if (sharedPreferences.getBoolean("custom_" + j5, false)) {
                        }
                        boolean z2222 = z7;
                        if (j2 == 0) {
                        }
                        int i3322 = i15;
                        i7 = i16;
                        str9 = str12;
                        z12 = z18;
                        i8 = i3322;
                        if (i8 != 4) {
                        }
                        if (!TextUtils.isEmpty(str8)) {
                        }
                        z11 = z12;
                        z14 = true;
                        if (i3 == 3) {
                        }
                        i3 = i6;
                        if (num != null) {
                        }
                        if (i9 != 0) {
                        }
                        z15 = z14;
                        if (z10) {
                        }
                        if (z13) {
                        }
                        if (z9) {
                        }
                        Intent intent5222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        StringBuilder sb32222 = new StringBuilder();
                        sb32222.append("com.tmessages.openchat");
                        int i262222 = i10;
                        boolean z232222 = z15;
                        sb32222.append(Math.random());
                        sb32222.append(Integer.MAX_VALUE);
                        intent5222.setAction(sb32222.toString());
                        intent5222.setFlags(ConnectionsManager.FileTypeFile);
                        if (!DialogObject.isEncryptedDialog(j5)) {
                        }
                        tLRPC$FileLocation = null;
                        intent5222.putExtra("currentAccount", this.currentAccount);
                        long j1222222 = j5;
                        builder3 = builder2;
                        MessageObject messageObject622222 = messageObject2;
                        int i2722222 = i8;
                        builder3.setContentTitle(charSequence2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent5222, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject622222.messageOwner.date * 1000).setColor(-15618822);
                        builder3.setCategory(RemoteMessageConst.MessageBody.MSG);
                        if (tLRPC$Chat == null) {
                        }
                        Intent intent222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                        intent222222.putExtra("messageDate", messageObject622222.messageOwner.date);
                        intent222222.putExtra("currentAccount", this.currentAccount);
                        builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent222222, 134217728));
                        if (tLRPC$FileLocation == null) {
                        }
                        if (z) {
                        }
                        builder3.setPriority(-1);
                    } else {
                        j4 = dialogId;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(str3);
                        j3 = fromChatId;
                        sb5.append(LocaleController.formatString("NotificationMessagesPeopleDisplayOrder", org.telegram.messenger.beta.R.string.NotificationMessagesPeopleDisplayOrder, LocaleController.formatPluralString("NewMessages", this.total_unread_count, new Object[0]), LocaleController.formatPluralString("FromChats", this.pushDialogs.size(), new Object[0])));
                        str3 = sb5.toString();
                        NotificationCompat.Builder builder422 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                        if (this.pushMessages.size() != 1) {
                        }
                        if (z) {
                        }
                        if (!z8) {
                        }
                        str6 = str5;
                        str7 = str4;
                        sharedPreferences = notificationsSettings;
                        j5 = j4;
                        z9 = z8;
                        charSequence = "";
                        if (!z9) {
                        }
                        String path222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                        if (!ApplicationLoader.mainInterfacePaused) {
                        }
                        if (sharedPreferences.getBoolean("custom_" + j5, false)) {
                        }
                        boolean z22222 = z7;
                        if (j2 == 0) {
                        }
                        int i33222 = i15;
                        i7 = i16;
                        str9 = str12;
                        z12 = z18;
                        i8 = i33222;
                        if (i8 != 4) {
                        }
                        if (!TextUtils.isEmpty(str8)) {
                        }
                        z11 = z12;
                        z14 = true;
                        if (i3 == 3) {
                        }
                        i3 = i6;
                        if (num != null) {
                        }
                        if (i9 != 0) {
                        }
                        z15 = z14;
                        if (z10) {
                        }
                        if (z13) {
                        }
                        if (z9) {
                        }
                        Intent intent52222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        StringBuilder sb322222 = new StringBuilder();
                        sb322222.append("com.tmessages.openchat");
                        int i2622222 = i10;
                        boolean z2322222 = z15;
                        sb322222.append(Math.random());
                        sb322222.append(Integer.MAX_VALUE);
                        intent52222.setAction(sb322222.toString());
                        intent52222.setFlags(ConnectionsManager.FileTypeFile);
                        if (!DialogObject.isEncryptedDialog(j5)) {
                        }
                        tLRPC$FileLocation = null;
                        intent52222.putExtra("currentAccount", this.currentAccount);
                        long j12222222 = j5;
                        builder3 = builder2;
                        MessageObject messageObject6222222 = messageObject2;
                        int i27222222 = i8;
                        builder3.setContentTitle(charSequence2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent52222, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject6222222.messageOwner.date * 1000).setColor(-15618822);
                        builder3.setCategory(RemoteMessageConst.MessageBody.MSG);
                        if (tLRPC$Chat == null) {
                        }
                        Intent intent2222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                        intent2222222.putExtra("messageDate", messageObject6222222.messageOwner.date);
                        intent2222222.putExtra("currentAccount", this.currentAccount);
                        builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent2222222, 134217728));
                        if (tLRPC$FileLocation == null) {
                        }
                        if (z) {
                        }
                        builder3.setPriority(-1);
                    }
                }
            } else {
                messageObject2 = messageObject;
            }
            str2 = LocaleController.getString("AppName", org.telegram.messenger.beta.R.string.AppName);
            z5 = false;
            boolean z202 = z2;
            if (UserConfig.getActivatedAccountsCount() <= 1) {
            }
            j2 = j8;
            if (this.pushDialogs.size() == 1) {
                j4 = dialogId;
                j3 = fromChatId;
                NotificationCompat.Builder builder4222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                if (this.pushMessages.size() != 1) {
                }
                if (z) {
                }
                if (!z8) {
                }
                str6 = str5;
                str7 = str4;
                sharedPreferences = notificationsSettings;
                j5 = j4;
                z9 = z8;
                charSequence = "";
                if (!z9) {
                }
                String path2222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                if (!ApplicationLoader.mainInterfacePaused) {
                }
                if (sharedPreferences.getBoolean("custom_" + j5, false)) {
                }
                boolean z222222 = z7;
                if (j2 == 0) {
                }
                int i332222 = i15;
                i7 = i16;
                str9 = str12;
                z12 = z18;
                i8 = i332222;
                if (i8 != 4) {
                }
                if (!TextUtils.isEmpty(str8)) {
                }
                z11 = z12;
                z14 = true;
                if (i3 == 3) {
                }
                i3 = i6;
                if (num != null) {
                }
                if (i9 != 0) {
                }
                z15 = z14;
                if (z10) {
                }
                if (z13) {
                }
                if (z9) {
                }
                Intent intent522222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                StringBuilder sb3222222 = new StringBuilder();
                sb3222222.append("com.tmessages.openchat");
                int i26222222 = i10;
                boolean z23222222 = z15;
                sb3222222.append(Math.random());
                sb3222222.append(Integer.MAX_VALUE);
                intent522222.setAction(sb3222222.toString());
                intent522222.setFlags(ConnectionsManager.FileTypeFile);
                if (!DialogObject.isEncryptedDialog(j5)) {
                }
                tLRPC$FileLocation = null;
                intent522222.putExtra("currentAccount", this.currentAccount);
                long j122222222 = j5;
                builder3 = builder2;
                MessageObject messageObject62222222 = messageObject2;
                int i272222222 = i8;
                builder3.setContentTitle(charSequence2).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent522222, 1073741824)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject62222222.messageOwner.date * 1000).setColor(-15618822);
                builder3.setCategory(RemoteMessageConst.MessageBody.MSG);
                if (tLRPC$Chat == null) {
                }
                Intent intent22222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                intent22222222.putExtra("messageDate", messageObject62222222.messageOwner.date);
                intent22222222.putExtra("currentAccount", this.currentAccount);
                builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, intent22222222, 134217728));
                if (tLRPC$FileLocation == null) {
                }
                if (z) {
                }
                builder3.setPriority(-1);
            }
            if (this.pushDialogs.size() == 1) {
            }
        }
        z4 = true;
        if (DialogObject.isEncryptedDialog(dialogId)) {
        }
        str2 = LocaleController.getString("AppName", org.telegram.messenger.beta.R.string.AppName);
        z5 = false;
        boolean z2022 = z2;
        if (UserConfig.getActivatedAccountsCount() <= 1) {
        }
        j2 = j8;
        if (this.pushDialogs.size() == 1) {
        }
        if (this.pushDialogs.size() == 1) {
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

    public void resetNotificationSound(NotificationCompat.Builder builder, long j, String str, long[] jArr, int i, Uri uri, int i2, boolean z, boolean z2, boolean z3, int i3) {
        Uri uri2 = Settings.System.DEFAULT_RINGTONE_URI;
        if (uri2 == null || uri == null || TextUtils.equals(uri2.toString(), uri.toString())) {
            return;
        }
        SharedPreferences.Editor edit = getAccountInstance().getNotificationsSettings().edit();
        String uri3 = uri2.toString();
        String string = LocaleController.getString("DefaultRingtone", org.telegram.messenger.beta.R.string.DefaultRingtone);
        if (z) {
            if (i3 == 2) {
                edit.putString("ChannelSound", string);
            } else if (i3 == 0) {
                edit.putString("GroupSound", string);
            } else {
                edit.putString("GlobalSound", string);
            }
            if (i3 == 2) {
                edit.putString("ChannelSoundPath", uri3);
            } else if (i3 == 0) {
                edit.putString("GroupSoundPath", uri3);
            } else {
                edit.putString("GlobalSoundPath", uri3);
            }
            getNotificationsController().lambda$deleteNotificationChannelGlobal$32(i3, -1);
        } else {
            edit.putString("sound_" + j, string);
            edit.putString("sound_path_" + j, uri3);
            lambda$deleteNotificationChannel$31(j, -1);
        }
        edit.commit();
        builder.setChannelId(validateChannelId(j, str, jArr, i, Settings.System.DEFAULT_RINGTONE_URI, i2, z, z2, z3, i3));
        notificationManager.notify(this.notificationId, builder.build());
    }

    /* JADX WARN: Removed duplicated region for block: B:135:0x0347  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0365  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x036c  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x03c4  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x03cf  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x03f5  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x0400 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:181:0x0456  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x0468  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x04ac  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x04c1  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x04ea A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:199:0x04fa  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x0565 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:227:0x0579  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x058e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:245:0x05bd  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x05f7  */
    /* JADX WARN: Removed duplicated region for block: B:260:0x0633  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x06ad  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x070a  */
    /* JADX WARN: Removed duplicated region for block: B:304:0x071f  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x0740  */
    /* JADX WARN: Removed duplicated region for block: B:340:0x07c2  */
    /* JADX WARN: Removed duplicated region for block: B:373:0x08ae  */
    /* JADX WARN: Removed duplicated region for block: B:381:0x08cf  */
    /* JADX WARN: Removed duplicated region for block: B:388:0x0902  */
    /* JADX WARN: Removed duplicated region for block: B:391:0x0912  */
    /* JADX WARN: Removed duplicated region for block: B:397:0x0975  */
    /* JADX WARN: Removed duplicated region for block: B:398:0x097f  */
    /* JADX WARN: Removed duplicated region for block: B:404:0x09aa  */
    /* JADX WARN: Removed duplicated region for block: B:407:0x0a04  */
    /* JADX WARN: Removed duplicated region for block: B:411:0x0a3b  */
    /* JADX WARN: Removed duplicated region for block: B:416:0x0a60  */
    /* JADX WARN: Removed duplicated region for block: B:417:0x0a82  */
    /* JADX WARN: Removed duplicated region for block: B:420:0x0b34  */
    /* JADX WARN: Removed duplicated region for block: B:422:0x0b3f  */
    /* JADX WARN: Removed duplicated region for block: B:424:0x0b44  */
    /* JADX WARN: Removed duplicated region for block: B:427:0x0b4e  */
    /* JADX WARN: Removed duplicated region for block: B:433:0x0b62  */
    /* JADX WARN: Removed duplicated region for block: B:435:0x0b67  */
    /* JADX WARN: Removed duplicated region for block: B:438:0x0b73  */
    /* JADX WARN: Removed duplicated region for block: B:444:0x0b82  */
    /* JADX WARN: Removed duplicated region for block: B:457:0x0c09 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:466:0x0c3a  */
    /* JADX WARN: Removed duplicated region for block: B:520:0x0504 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:536:0x0922 A[ADDED_TO_REGION, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0206  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0210  */
    @SuppressLint({"InlinedApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showExtraNotifications(NotificationCompat.Builder builder, String str, long j, String str2, long[] jArr, int i, Uri uri, int i2, boolean z, boolean z2, boolean z3, int i3) {
        LongSparseArray longSparseArray;
        LongSparseArray longSparseArray2;
        ArrayList arrayList;
        LongSparseArray longSparseArray3;
        Notification notification;
        long j2;
        boolean z4;
        LongSparseArray longSparseArray4;
        ArrayList arrayList2;
        TLRPC$FileLocation tLRPC$FileLocation;
        ArrayList arrayList3;
        int i4;
        boolean z5;
        boolean z6;
        boolean z7;
        TLRPC$User tLRPC$User;
        TLRPC$Chat tLRPC$Chat;
        boolean z8;
        String str3;
        TLRPC$User tLRPC$User2;
        String str4;
        TLRPC$FileLocation tLRPC$FileLocation2;
        MessageObject messageObject;
        String str5;
        String str6;
        File file;
        Bitmap bitmap;
        Integer num;
        String str7;
        int i5;
        Integer num2;
        Bitmap bitmap2;
        TLRPC$Chat tLRPC$Chat2;
        NotificationCompat.Action action;
        Integer num3;
        Person person;
        String str8;
        String str9;
        NotificationCompat.MessagingStyle messagingStyle;
        NotificationCompat.MessagingStyle messagingStyle2;
        int i6;
        NotificationCompat.Action action2;
        int size;
        String str10;
        TLRPC$User tLRPC$User3;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList4;
        int size2;
        int i7;
        String str11;
        int i8;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList5;
        int i9;
        String str12;
        String str13;
        long j3;
        StringBuilder sb;
        boolean z9;
        String str14;
        NotificationCompat.MessagingStyle messagingStyle3;
        NotificationCompat.MessagingStyle messagingStyle4;
        long j4;
        Person person2;
        String str15;
        String str16;
        String str17;
        String str18;
        File file2;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        TLRPC$FileLocation tLRPC$FileLocation3;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup;
        boolean z10;
        List<NotificationCompat.MessagingStyle.Message> messages;
        Uri uri2;
        final Uri uriForFile;
        String str19;
        TLRPC$User user;
        Throwable th;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto2;
        TLRPC$FileLocation tLRPC$FileLocation4;
        String str20;
        Bitmap bitmap3;
        Bitmap bitmap4;
        String str21;
        String str22;
        TLRPC$User tLRPC$User4;
        TLRPC$Chat tLRPC$Chat3;
        TLRPC$FileLocation tLRPC$FileLocation5;
        TLRPC$FileLocation tLRPC$FileLocation6;
        TLRPC$FileLocation tLRPC$FileLocation7;
        NotificationsController notificationsController = this;
        int i10 = Build.VERSION.SDK_INT;
        if (i10 >= 26) {
            builder.setChannelId(validateChannelId(j, str2, jArr, i, uri, i2, z, z2, z3, i3));
        }
        Notification build = builder.build();
        if (i10 < 18) {
            notificationManager.notify(notificationsController.notificationId, build);
            if (!BuildVars.LOGS_ENABLED) {
                return;
            }
            FileLog.d("show summary notification by SDK check");
            return;
        }
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        ArrayList arrayList6 = new ArrayList();
        LongSparseArray longSparseArray5 = new LongSparseArray();
        int i11 = 0;
        for (int i12 = 0; i12 < notificationsController.pushMessages.size(); i12++) {
            MessageObject messageObject2 = notificationsController.pushMessages.get(i12);
            long dialogId = messageObject2.getDialogId();
            if (messageObject2.messageOwner.date > notificationsSettings.getInt("dismissDate" + dialogId, 0)) {
                ArrayList arrayList7 = (ArrayList) longSparseArray5.get(dialogId);
                if (arrayList7 == null) {
                    arrayList7 = new ArrayList();
                    longSparseArray5.put(dialogId, arrayList7);
                    arrayList6.add(Long.valueOf(dialogId));
                }
                arrayList7.add(messageObject2);
            }
        }
        LongSparseArray longSparseArray6 = new LongSparseArray();
        for (int i13 = 0; i13 < notificationsController.wearNotificationsIds.size(); i13++) {
            longSparseArray6.put(notificationsController.wearNotificationsIds.keyAt(i13), notificationsController.wearNotificationsIds.valueAt(i13));
        }
        notificationsController.wearNotificationsIds.clear();
        ArrayList arrayList8 = new ArrayList();
        int i14 = Build.VERSION.SDK_INT;
        boolean z11 = i14 <= 27 || arrayList6.size() > 1;
        if (z11 && i14 >= 26) {
            checkOtherNotificationsChannel();
        }
        long clientUserId = getUserConfig().getClientUserId();
        boolean z12 = AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter;
        int i15 = 7;
        LongSparseArray longSparseArray7 = new LongSparseArray();
        int size3 = arrayList6.size();
        int i16 = 0;
        while (i16 < size3 && arrayList8.size() < i15) {
            int i17 = size3;
            ArrayList arrayList9 = arrayList8;
            long longValue = ((Long) arrayList6.get(i16)).longValue();
            ArrayList arrayList10 = (ArrayList) longSparseArray5.get(longValue);
            int id = ((MessageObject) arrayList10.get(i11)).getId();
            Integer num4 = (Integer) longSparseArray6.get(longValue);
            int i18 = i16;
            if (num4 == null) {
                longSparseArray2 = longSparseArray5;
                arrayList = arrayList6;
                num4 = Integer.valueOf(((int) longValue) + ((int) (longValue >> 32)));
            } else {
                longSparseArray2 = longSparseArray5;
                arrayList = arrayList6;
                longSparseArray6.remove(longValue);
            }
            Integer num5 = num4;
            MessageObject messageObject3 = (MessageObject) arrayList10.get(0);
            LongSparseArray longSparseArray8 = longSparseArray6;
            int i19 = 0;
            for (int i20 = 0; i20 < arrayList10.size(); i20++) {
                if (i19 < ((MessageObject) arrayList10.get(i20)).messageOwner.date) {
                    i19 = ((MessageObject) arrayList10.get(i20)).messageOwner.date;
                }
            }
            if (!DialogObject.isEncryptedDialog(longValue)) {
                boolean z13 = longValue != 777000;
                if (DialogObject.isUserDialog(longValue)) {
                    z7 = z13;
                    tLRPC$User4 = getMessagesController().getUser(Long.valueOf(longValue));
                    if (tLRPC$User4 == null) {
                        if (messageObject3.isFcmMessage()) {
                            str22 = messageObject3.localName;
                            i4 = i19;
                            z5 = z11;
                        } else {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.w("not found user to show dialog notification " + longValue);
                            }
                            j2 = clientUserId;
                            z4 = z11;
                            notification = build;
                            longSparseArray3 = longSparseArray7;
                            arrayList2 = arrayList9;
                            longSparseArray4 = longSparseArray8;
                        }
                    } else {
                        String userName = UserObject.getUserName(tLRPC$User4);
                        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto3 = tLRPC$User4.photo;
                        if (tLRPC$UserProfilePhoto3 == null || (tLRPC$FileLocation7 = tLRPC$UserProfilePhoto3.photo_small) == null) {
                            i4 = i19;
                            z5 = z11;
                        } else {
                            i4 = i19;
                            z5 = z11;
                            if (tLRPC$FileLocation7.volume_id != 0 && tLRPC$FileLocation7.local_id != 0) {
                                tLRPC$FileLocation6 = tLRPC$FileLocation7;
                                str22 = userName;
                                if (!UserObject.isReplyUser(longValue)) {
                                    str22 = LocaleController.getString("RepliesTitle", org.telegram.messenger.beta.R.string.RepliesTitle);
                                } else if (longValue == clientUserId) {
                                    str22 = LocaleController.getString("MessageScheduledReminderNotification", org.telegram.messenger.beta.R.string.MessageScheduledReminderNotification);
                                }
                                arrayList3 = arrayList10;
                                tLRPC$FileLocation = tLRPC$FileLocation6;
                                z8 = false;
                                tLRPC$Chat = null;
                                z6 = false;
                                String str23 = str22;
                                tLRPC$User = tLRPC$User4;
                                str3 = str23;
                                Notification notification2 = build;
                                if (!z12) {
                                    if (DialogObject.isChatDialog(longValue)) {
                                        tLRPC$User2 = tLRPC$User;
                                        str21 = LocaleController.getString("NotificationHiddenChatName", org.telegram.messenger.beta.R.string.NotificationHiddenChatName);
                                    } else {
                                        tLRPC$User2 = tLRPC$User;
                                        str21 = LocaleController.getString("NotificationHiddenName", org.telegram.messenger.beta.R.string.NotificationHiddenName);
                                    }
                                    str4 = str21;
                                    tLRPC$FileLocation2 = null;
                                    z7 = false;
                                } else {
                                    tLRPC$User2 = tLRPC$User;
                                    str4 = str3;
                                    tLRPC$FileLocation2 = tLRPC$FileLocation;
                                }
                                if (tLRPC$FileLocation2 == null) {
                                    str6 = "NotificationHiddenName";
                                    file = getFileLoader().getPathToAttach(tLRPC$FileLocation2, true);
                                    str5 = "NotificationHiddenChatName";
                                    if (Build.VERSION.SDK_INT < 28) {
                                        messageObject = messageObject3;
                                        bitmap3 = null;
                                        BitmapDrawable imageFromMemory = ImageLoader.getInstance().getImageFromMemory(tLRPC$FileLocation2, null, "50_50");
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
                                        messageObject = messageObject3;
                                        bitmap3 = null;
                                    }
                                    bitmap = bitmap3;
                                } else {
                                    str5 = "NotificationHiddenChatName";
                                    messageObject = messageObject3;
                                    str6 = "NotificationHiddenName";
                                    bitmap = null;
                                    file = null;
                                }
                                if (tLRPC$Chat == null) {
                                    Person.Builder name = new Person.Builder().setName(str4);
                                    if (file != null && file.exists() && Build.VERSION.SDK_INT >= 28) {
                                        notificationsController.loadRoundAvatar(file, name);
                                    }
                                    num = num5;
                                    longSparseArray7.put(-tLRPC$Chat.id, name.build());
                                } else {
                                    num = num5;
                                }
                                File file3 = file;
                                str7 = "currentAccount";
                                if ((z8 || z6) && z7 && !SharedConfig.isWaitingForPasscodeEnter && clientUserId != longValue && !UserObject.isReplyUser(longValue)) {
                                    tLRPC$Chat2 = tLRPC$Chat;
                                    bitmap2 = bitmap;
                                    Intent intent = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                                    intent.putExtra("dialog_id", longValue);
                                    intent.putExtra("max_id", id);
                                    intent.putExtra(str7, notificationsController.currentAccount);
                                    num2 = num;
                                    PendingIntent broadcast = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent, 134217728);
                                    RemoteInput build2 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", org.telegram.messenger.beta.R.string.Reply)).build();
                                    if (!DialogObject.isChatDialog(longValue)) {
                                        i5 = id;
                                        str20 = LocaleController.formatString("ReplyToGroup", org.telegram.messenger.beta.R.string.ReplyToGroup, str4);
                                    } else {
                                        i5 = id;
                                        str20 = LocaleController.formatString("ReplyToUser", org.telegram.messenger.beta.R.string.ReplyToUser, str4);
                                    }
                                    action = new NotificationCompat.Action.Builder(org.telegram.messenger.beta.R.drawable.ic_reply_icon, str20, broadcast).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build2).setShowsUserInterface(false).build();
                                } else {
                                    bitmap2 = bitmap;
                                    tLRPC$Chat2 = tLRPC$Chat;
                                    i5 = id;
                                    num2 = num;
                                    action = null;
                                }
                                num3 = notificationsController.pushDialogs.get(longValue);
                                if (num3 == null) {
                                    num3 = 0;
                                }
                                int max = Math.max(num3.intValue(), arrayList3.size());
                                String format = (max > 1 || Build.VERSION.SDK_INT >= 28) ? str4 : String.format("%1$s (%2$d)", str4, Integer.valueOf(max));
                                person = (Person) longSparseArray7.get(clientUserId);
                                if (Build.VERSION.SDK_INT >= 28 && person == null) {
                                    user = getMessagesController().getUser(Long.valueOf(clientUserId));
                                    if (user == null) {
                                        user = getUserConfig().getCurrentUser();
                                    }
                                    if (user != null) {
                                        try {
                                            tLRPC$UserProfilePhoto2 = user.photo;
                                        } catch (Throwable th2) {
                                            th = th2;
                                            str9 = "max_id";
                                            str8 = "dialog_id";
                                        }
                                        if (tLRPC$UserProfilePhoto2 != null && (tLRPC$FileLocation4 = tLRPC$UserProfilePhoto2.photo_small) != null) {
                                            str9 = "max_id";
                                            str8 = "dialog_id";
                                            try {
                                                if (tLRPC$FileLocation4.volume_id != 0 && tLRPC$FileLocation4.local_id != 0) {
                                                    Person.Builder name2 = new Person.Builder().setName(LocaleController.getString("FromYou", org.telegram.messenger.beta.R.string.FromYou));
                                                    notificationsController.loadRoundAvatar(getFileLoader().getPathToAttach(user.photo.photo_small, true), name2);
                                                    Person build3 = name2.build();
                                                    try {
                                                        longSparseArray7.put(clientUserId, build3);
                                                        person = build3;
                                                    } catch (Throwable th3) {
                                                        th = th3;
                                                        person = build3;
                                                        FileLog.e(th);
                                                        boolean z14 = !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest);
                                                        if (person == null) {
                                                        }
                                                        messagingStyle = new NotificationCompat.MessagingStyle("");
                                                        messagingStyle2 = messagingStyle;
                                                        i6 = Build.VERSION.SDK_INT;
                                                        if (i6 >= 28) {
                                                        }
                                                        messagingStyle2.setConversationTitle(format);
                                                        messagingStyle2.setGroupConversation(i6 >= 28 || (!z8 && DialogObject.isChatDialog(longValue)) || UserObject.isReplyUser(longValue));
                                                        StringBuilder sb2 = new StringBuilder();
                                                        String[] strArr = new String[1];
                                                        action2 = action;
                                                        boolean[] zArr = new boolean[1];
                                                        size = arrayList3.size() - 1;
                                                        int i21 = 0;
                                                        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList11 = null;
                                                        while (size >= 0) {
                                                        }
                                                        long j5 = clientUserId;
                                                        String str24 = str4;
                                                        StringBuilder sb3 = sb2;
                                                        NotificationCompat.MessagingStyle messagingStyle5 = messagingStyle2;
                                                        String str25 = str7;
                                                        Intent intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                        intent2.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                                                        intent2.setFlags(ConnectionsManager.FileTypeFile);
                                                        intent2.addCategory("android.intent.category.LAUNCHER");
                                                        if (!DialogObject.isEncryptedDialog(longValue)) {
                                                        }
                                                        String str26 = str25;
                                                        intent2.putExtra(str26, notificationsController.currentAccount);
                                                        PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1073741824);
                                                        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
                                                        if (action2 != null) {
                                                        }
                                                        Intent intent3 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                                        intent3.addFlags(32);
                                                        intent3.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                                        intent3.putExtra(str8, longValue);
                                                        int i22 = i5;
                                                        intent3.putExtra(str9, i22);
                                                        intent3.putExtra(str26, notificationsController.currentAccount);
                                                        NotificationCompat.Action build4 = new NotificationCompat.Action.Builder(org.telegram.messenger.beta.R.drawable.msg_markread, LocaleController.getString("MarkAsRead", org.telegram.messenger.beta.R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent3, 134217728)).setSemanticAction(2).setShowsUserInterface(false).build();
                                                        if (DialogObject.isEncryptedDialog(longValue)) {
                                                        }
                                                        if (str10 == null) {
                                                        }
                                                        wearableExtender.setBridgeTag("tgaccount" + j5);
                                                        ArrayList arrayList12 = arrayList3;
                                                        long j6 = ((long) ((MessageObject) arrayList12.get(0)).messageOwner.date) * 1000;
                                                        LongSparseArray longSparseArray9 = longSparseArray7;
                                                        NotificationCompat.Builder category = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str24).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setContentText(sb3.toString()).setAutoCancel(true).setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j6).setShowWhen(true).setStyle(messagingStyle5).setContentIntent(activity).extend(wearableExtender).setSortKey(String.valueOf(Long.MAX_VALUE - j6)).setCategory(RemoteMessageConst.MessageBody.MSG);
                                                        Intent intent4 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                        intent4.putExtra("messageDate", i4);
                                                        intent4.putExtra("dialogId", longValue);
                                                        intent4.putExtra(str26, notificationsController.currentAccount);
                                                        category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent4, 134217728));
                                                        if (z5) {
                                                        }
                                                        if (action2 != null) {
                                                        }
                                                        if (!z12) {
                                                        }
                                                        if (arrayList.size() != 1) {
                                                        }
                                                        if (DialogObject.isEncryptedDialog(longValue)) {
                                                        }
                                                        if (bitmap2 != null) {
                                                        }
                                                        if (!AndroidUtilities.needShowPasscode(false)) {
                                                        }
                                                        if (tLRPC$Chat2 == null) {
                                                        }
                                                        tLRPC$User3 = tLRPC$User2;
                                                        boolean z15 = z5;
                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                        }
                                                        j2 = j5;
                                                        z4 = z15;
                                                        longSparseArray4 = longSparseArray8;
                                                        notification = notification2;
                                                        longSparseArray3 = longSparseArray9;
                                                        arrayList2 = arrayList9;
                                                        arrayList2.add(new C1NotificationHolder(num2.intValue(), longValue, str24, tLRPC$User3, tLRPC$Chat2, category, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                                        notificationsController = this;
                                                        notificationsController.wearNotificationsIds.put(longValue, num2);
                                                        i16 = i18 + 1;
                                                        arrayList8 = arrayList2;
                                                        longSparseArray6 = longSparseArray4;
                                                        size3 = i17;
                                                        z11 = z4;
                                                        arrayList6 = arrayList;
                                                        longSparseArray5 = longSparseArray2;
                                                        clientUserId = j2;
                                                        build = notification;
                                                        longSparseArray7 = longSparseArray3;
                                                        i15 = 7;
                                                        i11 = 0;
                                                    }
                                                }
                                            } catch (Throwable th4) {
                                                th = th4;
                                            }
                                            boolean z142 = !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest);
                                            if (person == null && z142) {
                                                messagingStyle = new NotificationCompat.MessagingStyle(person);
                                            } else {
                                                messagingStyle = new NotificationCompat.MessagingStyle("");
                                            }
                                            messagingStyle2 = messagingStyle;
                                            i6 = Build.VERSION.SDK_INT;
                                            if (i6 >= 28 || ((DialogObject.isChatDialog(longValue) && !z8) || UserObject.isReplyUser(longValue))) {
                                                messagingStyle2.setConversationTitle(format);
                                            }
                                            messagingStyle2.setGroupConversation(i6 >= 28 || (!z8 && DialogObject.isChatDialog(longValue)) || UserObject.isReplyUser(longValue));
                                            StringBuilder sb22 = new StringBuilder();
                                            String[] strArr2 = new String[1];
                                            action2 = action;
                                            boolean[] zArr2 = new boolean[1];
                                            size = arrayList3.size() - 1;
                                            int i212 = 0;
                                            ArrayList<TLRPC$TL_keyboardButtonRow> arrayList112 = null;
                                            while (size >= 0) {
                                                String str27 = str7;
                                                MessageObject messageObject4 = (MessageObject) arrayList3.get(size);
                                                String shortStringForMessage = notificationsController.getShortStringForMessage(messageObject4, strArr2, zArr2);
                                                int i23 = size;
                                                if (longValue == clientUserId) {
                                                    strArr2[0] = str4;
                                                } else if (DialogObject.isChatDialog(longValue)) {
                                                    str12 = str4;
                                                    if (messageObject4.messageOwner.from_scheduled) {
                                                        strArr2[0] = LocaleController.getString("NotificationMessageScheduledName", org.telegram.messenger.beta.R.string.NotificationMessageScheduledName);
                                                    }
                                                    if (shortStringForMessage != null) {
                                                        if (BuildVars.LOGS_ENABLED) {
                                                            FileLog.w("message text is null for " + messageObject4.getId() + " did = " + messageObject4.getDialogId());
                                                            j3 = clientUserId;
                                                            messagingStyle3 = messagingStyle2;
                                                            sb = sb22;
                                                        } else {
                                                            j3 = clientUserId;
                                                            sb = sb22;
                                                            messagingStyle3 = messagingStyle2;
                                                        }
                                                        str14 = str6;
                                                        str13 = str5;
                                                        z9 = z8;
                                                    } else {
                                                        NotificationCompat.MessagingStyle messagingStyle6 = messagingStyle2;
                                                        if (sb22.length() > 0) {
                                                            sb22.append("\n\n");
                                                        }
                                                        if (longValue != clientUserId && messageObject4.messageOwner.from_scheduled && DialogObject.isUserDialog(longValue)) {
                                                            messagingStyle4 = messagingStyle6;
                                                            shortStringForMessage = String.format("%1$s: %2$s", LocaleController.getString("NotificationMessageScheduledName", org.telegram.messenger.beta.R.string.NotificationMessageScheduledName), shortStringForMessage);
                                                            sb22.append(shortStringForMessage);
                                                        } else {
                                                            messagingStyle4 = messagingStyle6;
                                                            if (strArr2[0] != null) {
                                                                sb22.append(String.format("%1$s: %2$s", strArr2[0], shortStringForMessage));
                                                            } else {
                                                                sb22.append(shortStringForMessage);
                                                            }
                                                        }
                                                        String str28 = shortStringForMessage;
                                                        if (!DialogObject.isUserDialog(longValue)) {
                                                            if (z8) {
                                                                j4 = -longValue;
                                                            } else if (DialogObject.isChatDialog(longValue)) {
                                                                j4 = messageObject4.getSenderId();
                                                            }
                                                            person2 = (Person) longSparseArray7.get(j4);
                                                            if (strArr2[0] == null) {
                                                                j3 = clientUserId;
                                                                sb = sb22;
                                                                str16 = str6;
                                                                str15 = str5;
                                                                str19 = strArr2[0];
                                                            } else {
                                                                if (z12) {
                                                                    if (!DialogObject.isChatDialog(longValue)) {
                                                                        j3 = clientUserId;
                                                                        sb = sb22;
                                                                        str15 = str5;
                                                                        if (Build.VERSION.SDK_INT > 27) {
                                                                            str16 = str6;
                                                                            str19 = LocaleController.getString(str16, org.telegram.messenger.beta.R.string.NotificationHiddenName);
                                                                        }
                                                                    } else {
                                                                        if (z8) {
                                                                            sb = sb22;
                                                                            j3 = clientUserId;
                                                                            if (Build.VERSION.SDK_INT > 27) {
                                                                                str15 = str5;
                                                                                str17 = LocaleController.getString(str15, org.telegram.messenger.beta.R.string.NotificationHiddenChatName);
                                                                            } else {
                                                                                str15 = str5;
                                                                            }
                                                                        } else {
                                                                            j3 = clientUserId;
                                                                            sb = sb22;
                                                                            str15 = str5;
                                                                            str17 = LocaleController.getString("NotificationHiddenChatUserName", org.telegram.messenger.beta.R.string.NotificationHiddenChatUserName);
                                                                        }
                                                                        str16 = str6;
                                                                        str14 = str16;
                                                                        if (person2 != null || !TextUtils.equals(person2.getName(), str17)) {
                                                                            Person.Builder name3 = new Person.Builder().setName(str17);
                                                                            if (zArr2[0] || DialogObject.isEncryptedDialog(longValue) || Build.VERSION.SDK_INT < 28) {
                                                                                z9 = z8;
                                                                                str18 = str28;
                                                                                str13 = str15;
                                                                            } else {
                                                                                if (DialogObject.isUserDialog(longValue) || z8) {
                                                                                    z9 = z8;
                                                                                    str18 = str28;
                                                                                    str13 = str15;
                                                                                    file2 = file3;
                                                                                } else {
                                                                                    long senderId = messageObject4.getSenderId();
                                                                                    z9 = z8;
                                                                                    str13 = str15;
                                                                                    TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(senderId));
                                                                                    if (user2 == null && (user2 = getMessagesStorage().getUserSync(senderId)) != null) {
                                                                                        getMessagesController().putUser(user2, true);
                                                                                    }
                                                                                    if (user2 == null || (tLRPC$UserProfilePhoto = user2.photo) == null || (tLRPC$FileLocation3 = tLRPC$UserProfilePhoto.photo_small) == null) {
                                                                                        str18 = str28;
                                                                                    } else {
                                                                                        str18 = str28;
                                                                                        if (tLRPC$FileLocation3.volume_id != 0 && tLRPC$FileLocation3.local_id != 0) {
                                                                                            file2 = getFileLoader().getPathToAttach(user2.photo.photo_small, true);
                                                                                        }
                                                                                    }
                                                                                    file2 = null;
                                                                                }
                                                                                notificationsController.loadRoundAvatar(file2, name3);
                                                                            }
                                                                            person2 = name3.build();
                                                                            longSparseArray7.put(j4, person2);
                                                                        } else {
                                                                            z9 = z8;
                                                                            str18 = str28;
                                                                            str13 = str15;
                                                                        }
                                                                        Person person3 = person2;
                                                                        if (DialogObject.isEncryptedDialog(longValue)) {
                                                                            if (zArr2[0] && Build.VERSION.SDK_INT >= 28 && !((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).isLowRamDevice() && !z12 && !messageObject4.isSecretMedia() && (messageObject4.type == 1 || messageObject4.isSticker())) {
                                                                                File pathToMessage = getFileLoader().getPathToMessage(messageObject4.messageOwner);
                                                                                NotificationCompat.MessagingStyle.Message message = new NotificationCompat.MessagingStyle.Message(str18, messageObject4.messageOwner.date * 1000, person3);
                                                                                String str29 = messageObject4.isSticker() ? "image/webp" : "image/jpeg";
                                                                                if (pathToMessage.exists()) {
                                                                                    try {
                                                                                        uriForFile = FileProvider.getUriForFile(ApplicationLoader.applicationContext, "org.telegram.messenger.beta.provider", pathToMessage);
                                                                                    } catch (Exception e) {
                                                                                        FileLog.e(e);
                                                                                    }
                                                                                } else {
                                                                                    if (getFileLoader().isLoadingFile(pathToMessage.getName())) {
                                                                                        uriForFile = new Uri.Builder().scheme(RemoteMessageConst.Notification.CONTENT).authority(NotificationImageProvider.AUTHORITY).appendPath("msg_media_raw").appendPath(notificationsController.currentAccount + "").appendPath(pathToMessage.getName()).appendQueryParameter("final_path", pathToMessage.getAbsolutePath()).build();
                                                                                    }
                                                                                    uriForFile = null;
                                                                                }
                                                                                if (uriForFile != null) {
                                                                                    message.setData(str29, uriForFile);
                                                                                    messagingStyle3 = messagingStyle4;
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
                                                                                    z10 = true;
                                                                                    if (!z10) {
                                                                                        messagingStyle3.addMessage(str18, messageObject4.messageOwner.date * 1000, person3);
                                                                                    }
                                                                                    if (zArr2[0] && !z12 && messageObject4.isVoice()) {
                                                                                        messages = messagingStyle3.getMessages();
                                                                                        if (!messages.isEmpty()) {
                                                                                            File pathToMessage2 = getFileLoader().getPathToMessage(messageObject4.messageOwner);
                                                                                            if (Build.VERSION.SDK_INT >= 24) {
                                                                                                try {
                                                                                                    uri2 = FileProvider.getUriForFile(ApplicationLoader.applicationContext, "org.telegram.messenger.beta.provider", pathToMessage2);
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
                                                                            messagingStyle3 = messagingStyle4;
                                                                            z10 = false;
                                                                            if (!z10) {
                                                                            }
                                                                            if (zArr2[0]) {
                                                                                messages = messagingStyle3.getMessages();
                                                                                if (!messages.isEmpty()) {
                                                                                }
                                                                            }
                                                                        } else {
                                                                            messagingStyle3 = messagingStyle4;
                                                                            messagingStyle3.addMessage(str18, messageObject4.messageOwner.date * 1000, person3);
                                                                        }
                                                                        if (longValue == 777000 && (tLRPC$ReplyMarkup = messageObject4.messageOwner.reply_markup) != null) {
                                                                            arrayList112 = tLRPC$ReplyMarkup.rows;
                                                                            i212 = messageObject4.getId();
                                                                        }
                                                                    }
                                                                    str16 = str6;
                                                                } else {
                                                                    j3 = clientUserId;
                                                                    sb = sb22;
                                                                    str16 = str6;
                                                                    str15 = str5;
                                                                }
                                                                str17 = "";
                                                                str14 = str16;
                                                                if (person2 != null) {
                                                                }
                                                                Person.Builder name32 = new Person.Builder().setName(str17);
                                                                if (zArr2[0]) {
                                                                }
                                                                z9 = z8;
                                                                str18 = str28;
                                                                str13 = str15;
                                                                person2 = name32.build();
                                                                longSparseArray7.put(j4, person2);
                                                                Person person32 = person2;
                                                                if (DialogObject.isEncryptedDialog(longValue)) {
                                                                }
                                                                if (longValue == 777000) {
                                                                    arrayList112 = tLRPC$ReplyMarkup.rows;
                                                                    i212 = messageObject4.getId();
                                                                }
                                                            }
                                                            str17 = str19;
                                                            str14 = str16;
                                                            if (person2 != null) {
                                                            }
                                                            Person.Builder name322 = new Person.Builder().setName(str17);
                                                            if (zArr2[0]) {
                                                            }
                                                            z9 = z8;
                                                            str18 = str28;
                                                            str13 = str15;
                                                            person2 = name322.build();
                                                            longSparseArray7.put(j4, person2);
                                                            Person person322 = person2;
                                                            if (DialogObject.isEncryptedDialog(longValue)) {
                                                            }
                                                            if (longValue == 777000) {
                                                            }
                                                        }
                                                        j4 = longValue;
                                                        person2 = (Person) longSparseArray7.get(j4);
                                                        if (strArr2[0] == null) {
                                                        }
                                                        str17 = str19;
                                                        str14 = str16;
                                                        if (person2 != null) {
                                                        }
                                                        Person.Builder name3222 = new Person.Builder().setName(str17);
                                                        if (zArr2[0]) {
                                                        }
                                                        z9 = z8;
                                                        str18 = str28;
                                                        str13 = str15;
                                                        person2 = name3222.build();
                                                        longSparseArray7.put(j4, person2);
                                                        Person person3222 = person2;
                                                        if (DialogObject.isEncryptedDialog(longValue)) {
                                                        }
                                                        if (longValue == 777000) {
                                                        }
                                                    }
                                                    size = i23 - 1;
                                                    messagingStyle2 = messagingStyle3;
                                                    str6 = str14;
                                                    z8 = z9;
                                                    str7 = str27;
                                                    str4 = str12;
                                                    sb22 = sb;
                                                    clientUserId = j3;
                                                    str5 = str13;
                                                }
                                                str12 = str4;
                                                if (shortStringForMessage != null) {
                                                }
                                                size = i23 - 1;
                                                messagingStyle2 = messagingStyle3;
                                                str6 = str14;
                                                z8 = z9;
                                                str7 = str27;
                                                str4 = str12;
                                                sb22 = sb;
                                                clientUserId = j3;
                                                str5 = str13;
                                            }
                                            long j52 = clientUserId;
                                            String str242 = str4;
                                            StringBuilder sb32 = sb22;
                                            NotificationCompat.MessagingStyle messagingStyle52 = messagingStyle2;
                                            String str252 = str7;
                                            Intent intent22 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                            intent22.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                                            intent22.setFlags(ConnectionsManager.FileTypeFile);
                                            intent22.addCategory("android.intent.category.LAUNCHER");
                                            if (!DialogObject.isEncryptedDialog(longValue)) {
                                                intent22.putExtra("encId", DialogObject.getEncryptedChatId(longValue));
                                            } else if (DialogObject.isUserDialog(longValue)) {
                                                intent22.putExtra("userId", longValue);
                                            } else {
                                                intent22.putExtra("chatId", -longValue);
                                            }
                                            String str262 = str252;
                                            intent22.putExtra(str262, notificationsController.currentAccount);
                                            PendingIntent activity2 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22, 1073741824);
                                            NotificationCompat.WearableExtender wearableExtender2 = new NotificationCompat.WearableExtender();
                                            if (action2 != null) {
                                                wearableExtender2.addAction(action2);
                                            }
                                            Intent intent32 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                            intent32.addFlags(32);
                                            intent32.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                            intent32.putExtra(str8, longValue);
                                            int i222 = i5;
                                            intent32.putExtra(str9, i222);
                                            intent32.putExtra(str262, notificationsController.currentAccount);
                                            NotificationCompat.Action build42 = new NotificationCompat.Action.Builder(org.telegram.messenger.beta.R.drawable.msg_markread, LocaleController.getString("MarkAsRead", org.telegram.messenger.beta.R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent32, 134217728)).setSemanticAction(2).setShowsUserInterface(false).build();
                                            if (DialogObject.isEncryptedDialog(longValue)) {
                                                str10 = DialogObject.isUserDialog(longValue) ? "tguser" + longValue + "_" + i222 : "tgchat" + (-longValue) + "_" + i222;
                                            } else if (longValue != globalSecretChatId) {
                                                str10 = "tgenc" + DialogObject.getEncryptedChatId(longValue) + "_" + i222;
                                            } else {
                                                str10 = null;
                                            }
                                            if (str10 == null) {
                                                wearableExtender2.setDismissalId(str10);
                                                NotificationCompat.WearableExtender wearableExtender3 = new NotificationCompat.WearableExtender();
                                                wearableExtender3.setDismissalId("summary_" + str10);
                                                builder.extend(wearableExtender3);
                                            }
                                            wearableExtender2.setBridgeTag("tgaccount" + j52);
                                            ArrayList arrayList122 = arrayList3;
                                            long j62 = ((long) ((MessageObject) arrayList122.get(0)).messageOwner.date) * 1000;
                                            LongSparseArray longSparseArray92 = longSparseArray7;
                                            NotificationCompat.Builder category2 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str242).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setContentText(sb32.toString()).setAutoCancel(true).setNumber(arrayList122.size()).setColor(-15618822).setGroupSummary(false).setWhen(j62).setShowWhen(true).setStyle(messagingStyle52).setContentIntent(activity2).extend(wearableExtender2).setSortKey(String.valueOf(Long.MAX_VALUE - j62)).setCategory(RemoteMessageConst.MessageBody.MSG);
                                            Intent intent42 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                            intent42.putExtra("messageDate", i4);
                                            intent42.putExtra("dialogId", longValue);
                                            intent42.putExtra(str262, notificationsController.currentAccount);
                                            category2.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent42, 134217728));
                                            if (z5) {
                                                category2.setGroup(notificationsController.notificationGroup);
                                                category2.setGroupAlertBehavior(1);
                                            }
                                            if (action2 != null) {
                                                category2.addAction(action2);
                                            }
                                            if (!z12) {
                                                category2.addAction(build42);
                                            }
                                            if (arrayList.size() != 1 && !TextUtils.isEmpty(str)) {
                                                category2.setSubText(str);
                                            }
                                            if (DialogObject.isEncryptedDialog(longValue)) {
                                                category2.setLocalOnly(true);
                                            }
                                            if (bitmap2 != null) {
                                                category2.setLargeIcon(bitmap2);
                                            }
                                            if (!AndroidUtilities.needShowPasscode(false) && !SharedConfig.isWaitingForPasscodeEnter && (arrayList4 = arrayList112) != null) {
                                                size2 = arrayList4.size();
                                                i7 = 0;
                                                while (i7 < size2) {
                                                    TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow = arrayList4.get(i7);
                                                    int size4 = tLRPC$TL_keyboardButtonRow.buttons.size();
                                                    int i24 = 0;
                                                    while (i24 < size4) {
                                                        TLRPC$KeyboardButton tLRPC$KeyboardButton = tLRPC$TL_keyboardButtonRow.buttons.get(i24);
                                                        if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonCallback) {
                                                            i9 = size2;
                                                            arrayList5 = arrayList4;
                                                            Intent intent5 = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                                            intent5.putExtra(str262, notificationsController.currentAccount);
                                                            intent5.putExtra("did", longValue);
                                                            byte[] bArr = tLRPC$KeyboardButton.data;
                                                            if (bArr != null) {
                                                                intent5.putExtra(RemoteMessageConst.DATA, bArr);
                                                            }
                                                            int i25 = i212;
                                                            intent5.putExtra("mid", i25);
                                                            String str30 = tLRPC$KeyboardButton.text;
                                                            Context context = ApplicationLoader.applicationContext;
                                                            i8 = i25;
                                                            int i26 = notificationsController.lastButtonId;
                                                            str11 = str262;
                                                            notificationsController.lastButtonId = i26 + 1;
                                                            category2.addAction(0, str30, PendingIntent.getBroadcast(context, i26, intent5, 134217728));
                                                        } else {
                                                            i9 = size2;
                                                            arrayList5 = arrayList4;
                                                            str11 = str262;
                                                            i8 = i212;
                                                        }
                                                        i24++;
                                                        size2 = i9;
                                                        arrayList4 = arrayList5;
                                                        i212 = i8;
                                                        str262 = str11;
                                                    }
                                                    i7++;
                                                    str262 = str262;
                                                }
                                            }
                                            if (tLRPC$Chat2 == null || tLRPC$User2 == null) {
                                                tLRPC$User3 = tLRPC$User2;
                                            } else {
                                                tLRPC$User3 = tLRPC$User2;
                                                String str31 = tLRPC$User3.phone;
                                                if (str31 != null && str31.length() > 0) {
                                                    category2.addPerson("tel:+" + tLRPC$User3.phone);
                                                }
                                            }
                                            boolean z152 = z5;
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                notificationsController.setNotificationChannel(notification2, category2, z152);
                                            }
                                            j2 = j52;
                                            z4 = z152;
                                            longSparseArray4 = longSparseArray8;
                                            notification = notification2;
                                            longSparseArray3 = longSparseArray92;
                                            arrayList2 = arrayList9;
                                            arrayList2.add(new C1NotificationHolder(num2.intValue(), longValue, str242, tLRPC$User3, tLRPC$Chat2, category2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                            notificationsController = this;
                                            notificationsController.wearNotificationsIds.put(longValue, num2);
                                        }
                                    }
                                }
                                str9 = "max_id";
                                str8 = "dialog_id";
                                boolean z1422 = !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest);
                                if (person == null) {
                                }
                                messagingStyle = new NotificationCompat.MessagingStyle("");
                                messagingStyle2 = messagingStyle;
                                i6 = Build.VERSION.SDK_INT;
                                if (i6 >= 28) {
                                }
                                messagingStyle2.setConversationTitle(format);
                                messagingStyle2.setGroupConversation(i6 >= 28 || (!z8 && DialogObject.isChatDialog(longValue)) || UserObject.isReplyUser(longValue));
                                StringBuilder sb222 = new StringBuilder();
                                String[] strArr22 = new String[1];
                                action2 = action;
                                boolean[] zArr22 = new boolean[1];
                                size = arrayList3.size() - 1;
                                int i2122 = 0;
                                ArrayList<TLRPC$TL_keyboardButtonRow> arrayList1122 = null;
                                while (size >= 0) {
                                }
                                long j522 = clientUserId;
                                String str2422 = str4;
                                StringBuilder sb322 = sb222;
                                NotificationCompat.MessagingStyle messagingStyle522 = messagingStyle2;
                                String str2522 = str7;
                                Intent intent222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                intent222.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                                intent222.setFlags(ConnectionsManager.FileTypeFile);
                                intent222.addCategory("android.intent.category.LAUNCHER");
                                if (!DialogObject.isEncryptedDialog(longValue)) {
                                }
                                String str2622 = str2522;
                                intent222.putExtra(str2622, notificationsController.currentAccount);
                                PendingIntent activity22 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222, 1073741824);
                                NotificationCompat.WearableExtender wearableExtender22 = new NotificationCompat.WearableExtender();
                                if (action2 != null) {
                                }
                                Intent intent322 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                intent322.addFlags(32);
                                intent322.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                intent322.putExtra(str8, longValue);
                                int i2222 = i5;
                                intent322.putExtra(str9, i2222);
                                intent322.putExtra(str2622, notificationsController.currentAccount);
                                NotificationCompat.Action build422 = new NotificationCompat.Action.Builder(org.telegram.messenger.beta.R.drawable.msg_markread, LocaleController.getString("MarkAsRead", org.telegram.messenger.beta.R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent322, 134217728)).setSemanticAction(2).setShowsUserInterface(false).build();
                                if (DialogObject.isEncryptedDialog(longValue)) {
                                }
                                if (str10 == null) {
                                }
                                wearableExtender22.setBridgeTag("tgaccount" + j522);
                                ArrayList arrayList1222 = arrayList3;
                                long j622 = ((long) ((MessageObject) arrayList1222.get(0)).messageOwner.date) * 1000;
                                LongSparseArray longSparseArray922 = longSparseArray7;
                                NotificationCompat.Builder category22 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str2422).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setContentText(sb322.toString()).setAutoCancel(true).setNumber(arrayList1222.size()).setColor(-15618822).setGroupSummary(false).setWhen(j622).setShowWhen(true).setStyle(messagingStyle522).setContentIntent(activity22).extend(wearableExtender22).setSortKey(String.valueOf(Long.MAX_VALUE - j622)).setCategory(RemoteMessageConst.MessageBody.MSG);
                                Intent intent422 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                intent422.putExtra("messageDate", i4);
                                intent422.putExtra("dialogId", longValue);
                                intent422.putExtra(str2622, notificationsController.currentAccount);
                                category22.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent422, 134217728));
                                if (z5) {
                                }
                                if (action2 != null) {
                                }
                                if (!z12) {
                                }
                                if (arrayList.size() != 1) {
                                }
                                if (DialogObject.isEncryptedDialog(longValue)) {
                                }
                                if (bitmap2 != null) {
                                }
                                if (!AndroidUtilities.needShowPasscode(false)) {
                                    size2 = arrayList4.size();
                                    i7 = 0;
                                    while (i7 < size2) {
                                    }
                                }
                                if (tLRPC$Chat2 == null) {
                                }
                                tLRPC$User3 = tLRPC$User2;
                                boolean z1522 = z5;
                                if (Build.VERSION.SDK_INT >= 26) {
                                }
                                j2 = j522;
                                z4 = z1522;
                                longSparseArray4 = longSparseArray8;
                                notification = notification2;
                                longSparseArray3 = longSparseArray922;
                                arrayList2 = arrayList9;
                                arrayList2.add(new C1NotificationHolder(num2.intValue(), longValue, str2422, tLRPC$User3, tLRPC$Chat2, category22, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                notificationsController = this;
                                notificationsController.wearNotificationsIds.put(longValue, num2);
                            }
                        }
                        str22 = userName;
                    }
                    tLRPC$FileLocation6 = null;
                    if (!UserObject.isReplyUser(longValue)) {
                    }
                    arrayList3 = arrayList10;
                    tLRPC$FileLocation = tLRPC$FileLocation6;
                    z8 = false;
                    tLRPC$Chat = null;
                    z6 = false;
                    String str232 = str22;
                    tLRPC$User = tLRPC$User4;
                    str3 = str232;
                    Notification notification22 = build;
                    if (!z12) {
                    }
                    if (tLRPC$FileLocation2 == null) {
                    }
                    if (tLRPC$Chat == null) {
                    }
                    File file32 = file;
                    str7 = "currentAccount";
                    if (z8) {
                    }
                    tLRPC$Chat2 = tLRPC$Chat;
                    bitmap2 = bitmap;
                    Intent intent6 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                    intent6.putExtra("dialog_id", longValue);
                    intent6.putExtra("max_id", id);
                    intent6.putExtra(str7, notificationsController.currentAccount);
                    num2 = num;
                    PendingIntent broadcast2 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent6, 134217728);
                    RemoteInput build22 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", org.telegram.messenger.beta.R.string.Reply)).build();
                    if (!DialogObject.isChatDialog(longValue)) {
                    }
                    action = new NotificationCompat.Action.Builder(org.telegram.messenger.beta.R.drawable.ic_reply_icon, str20, broadcast2).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build22).setShowsUserInterface(false).build();
                    num3 = notificationsController.pushDialogs.get(longValue);
                    if (num3 == null) {
                    }
                    int max2 = Math.max(num3.intValue(), arrayList3.size());
                    if (max2 > 1) {
                    }
                    person = (Person) longSparseArray7.get(clientUserId);
                    if (Build.VERSION.SDK_INT >= 28) {
                        user = getMessagesController().getUser(Long.valueOf(clientUserId));
                        if (user == null) {
                        }
                        if (user != null) {
                        }
                    }
                    str9 = "max_id";
                    str8 = "dialog_id";
                    boolean z14222 = !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest);
                    if (person == null) {
                    }
                    messagingStyle = new NotificationCompat.MessagingStyle("");
                    messagingStyle2 = messagingStyle;
                    i6 = Build.VERSION.SDK_INT;
                    if (i6 >= 28) {
                    }
                    messagingStyle2.setConversationTitle(format);
                    messagingStyle2.setGroupConversation(i6 >= 28 || (!z8 && DialogObject.isChatDialog(longValue)) || UserObject.isReplyUser(longValue));
                    StringBuilder sb2222 = new StringBuilder();
                    String[] strArr222 = new String[1];
                    action2 = action;
                    boolean[] zArr222 = new boolean[1];
                    size = arrayList3.size() - 1;
                    int i21222 = 0;
                    ArrayList<TLRPC$TL_keyboardButtonRow> arrayList11222 = null;
                    while (size >= 0) {
                    }
                    long j5222 = clientUserId;
                    String str24222 = str4;
                    StringBuilder sb3222 = sb2222;
                    NotificationCompat.MessagingStyle messagingStyle5222 = messagingStyle2;
                    String str25222 = str7;
                    Intent intent2222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    intent2222.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                    intent2222.setFlags(ConnectionsManager.FileTypeFile);
                    intent2222.addCategory("android.intent.category.LAUNCHER");
                    if (!DialogObject.isEncryptedDialog(longValue)) {
                    }
                    String str26222 = str25222;
                    intent2222.putExtra(str26222, notificationsController.currentAccount);
                    PendingIntent activity222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2222, 1073741824);
                    NotificationCompat.WearableExtender wearableExtender222 = new NotificationCompat.WearableExtender();
                    if (action2 != null) {
                    }
                    Intent intent3222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                    intent3222.addFlags(32);
                    intent3222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                    intent3222.putExtra(str8, longValue);
                    int i22222 = i5;
                    intent3222.putExtra(str9, i22222);
                    intent3222.putExtra(str26222, notificationsController.currentAccount);
                    NotificationCompat.Action build4222 = new NotificationCompat.Action.Builder(org.telegram.messenger.beta.R.drawable.msg_markread, LocaleController.getString("MarkAsRead", org.telegram.messenger.beta.R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent3222, 134217728)).setSemanticAction(2).setShowsUserInterface(false).build();
                    if (DialogObject.isEncryptedDialog(longValue)) {
                    }
                    if (str10 == null) {
                    }
                    wearableExtender222.setBridgeTag("tgaccount" + j5222);
                    ArrayList arrayList12222 = arrayList3;
                    long j6222 = ((long) ((MessageObject) arrayList12222.get(0)).messageOwner.date) * 1000;
                    LongSparseArray longSparseArray9222 = longSparseArray7;
                    NotificationCompat.Builder category222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str24222).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setContentText(sb3222.toString()).setAutoCancel(true).setNumber(arrayList12222.size()).setColor(-15618822).setGroupSummary(false).setWhen(j6222).setShowWhen(true).setStyle(messagingStyle5222).setContentIntent(activity222).extend(wearableExtender222).setSortKey(String.valueOf(Long.MAX_VALUE - j6222)).setCategory(RemoteMessageConst.MessageBody.MSG);
                    Intent intent4222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                    intent4222.putExtra("messageDate", i4);
                    intent4222.putExtra("dialogId", longValue);
                    intent4222.putExtra(str26222, notificationsController.currentAccount);
                    category222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent4222, 134217728));
                    if (z5) {
                    }
                    if (action2 != null) {
                    }
                    if (!z12) {
                    }
                    if (arrayList.size() != 1) {
                    }
                    if (DialogObject.isEncryptedDialog(longValue)) {
                    }
                    if (bitmap2 != null) {
                    }
                    if (!AndroidUtilities.needShowPasscode(false)) {
                    }
                    if (tLRPC$Chat2 == null) {
                    }
                    tLRPC$User3 = tLRPC$User2;
                    boolean z15222 = z5;
                    if (Build.VERSION.SDK_INT >= 26) {
                    }
                    j2 = j5222;
                    z4 = z15222;
                    longSparseArray4 = longSparseArray8;
                    notification = notification22;
                    longSparseArray3 = longSparseArray9222;
                    arrayList2 = arrayList9;
                    arrayList2.add(new C1NotificationHolder(num2.intValue(), longValue, str24222, tLRPC$User3, tLRPC$Chat2, category222, str2, jArr, i, uri, i2, z, z2, z3, i3));
                    notificationsController = this;
                    notificationsController.wearNotificationsIds.put(longValue, num2);
                } else {
                    z7 = z13;
                    i4 = i19;
                    z5 = z11;
                    TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-longValue));
                    if (chat == null) {
                        if (messageObject3.isFcmMessage()) {
                            boolean isSupergroup = messageObject3.isSupergroup();
                            String str32 = messageObject3.localName;
                            arrayList3 = arrayList10;
                            z6 = isSupergroup;
                            z8 = messageObject3.localChannel;
                            tLRPC$FileLocation = null;
                            tLRPC$Chat = chat;
                            str3 = str32;
                            tLRPC$User = null;
                        } else {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.w("not found chat to show dialog notification " + longValue);
                            }
                            j2 = clientUserId;
                            notification = build;
                            longSparseArray3 = longSparseArray7;
                            arrayList2 = arrayList9;
                            longSparseArray4 = longSparseArray8;
                            z4 = z5;
                        }
                    } else {
                        boolean z16 = chat.megagroup;
                        boolean z17 = ChatObject.isChannel(chat) && !chat.megagroup;
                        String str33 = chat.title;
                        z6 = z16;
                        TLRPC$ChatPhoto tLRPC$ChatPhoto = chat.photo;
                        if (tLRPC$ChatPhoto == null || (tLRPC$FileLocation5 = tLRPC$ChatPhoto.photo_small) == null) {
                            tLRPC$Chat3 = chat;
                            arrayList3 = arrayList10;
                        } else {
                            tLRPC$Chat3 = chat;
                            arrayList3 = arrayList10;
                            if (tLRPC$FileLocation5.volume_id != 0 && tLRPC$FileLocation5.local_id != 0) {
                                z8 = z17;
                                str3 = str33;
                                tLRPC$Chat = tLRPC$Chat3;
                                tLRPC$User = null;
                                tLRPC$FileLocation = tLRPC$FileLocation5;
                            }
                        }
                        z8 = z17;
                        str3 = str33;
                        tLRPC$Chat = tLRPC$Chat3;
                        tLRPC$User = null;
                        tLRPC$FileLocation = null;
                    }
                    Notification notification222 = build;
                    if (!z12) {
                    }
                    if (tLRPC$FileLocation2 == null) {
                    }
                    if (tLRPC$Chat == null) {
                    }
                    File file322 = file;
                    str7 = "currentAccount";
                    if (z8) {
                    }
                    tLRPC$Chat2 = tLRPC$Chat;
                    bitmap2 = bitmap;
                    Intent intent62 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                    intent62.putExtra("dialog_id", longValue);
                    intent62.putExtra("max_id", id);
                    intent62.putExtra(str7, notificationsController.currentAccount);
                    num2 = num;
                    PendingIntent broadcast22 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent62, 134217728);
                    RemoteInput build222 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", org.telegram.messenger.beta.R.string.Reply)).build();
                    if (!DialogObject.isChatDialog(longValue)) {
                    }
                    action = new NotificationCompat.Action.Builder(org.telegram.messenger.beta.R.drawable.ic_reply_icon, str20, broadcast22).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build222).setShowsUserInterface(false).build();
                    num3 = notificationsController.pushDialogs.get(longValue);
                    if (num3 == null) {
                    }
                    int max22 = Math.max(num3.intValue(), arrayList3.size());
                    if (max22 > 1) {
                    }
                    person = (Person) longSparseArray7.get(clientUserId);
                    if (Build.VERSION.SDK_INT >= 28) {
                    }
                    str9 = "max_id";
                    str8 = "dialog_id";
                    boolean z142222 = !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest);
                    if (person == null) {
                    }
                    messagingStyle = new NotificationCompat.MessagingStyle("");
                    messagingStyle2 = messagingStyle;
                    i6 = Build.VERSION.SDK_INT;
                    if (i6 >= 28) {
                    }
                    messagingStyle2.setConversationTitle(format);
                    messagingStyle2.setGroupConversation(i6 >= 28 || (!z8 && DialogObject.isChatDialog(longValue)) || UserObject.isReplyUser(longValue));
                    StringBuilder sb22222 = new StringBuilder();
                    String[] strArr2222 = new String[1];
                    action2 = action;
                    boolean[] zArr2222 = new boolean[1];
                    size = arrayList3.size() - 1;
                    int i212222 = 0;
                    ArrayList<TLRPC$TL_keyboardButtonRow> arrayList112222 = null;
                    while (size >= 0) {
                    }
                    long j52222 = clientUserId;
                    String str242222 = str4;
                    StringBuilder sb32222 = sb22222;
                    NotificationCompat.MessagingStyle messagingStyle52222 = messagingStyle2;
                    String str252222 = str7;
                    Intent intent22222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    intent22222.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                    intent22222.setFlags(ConnectionsManager.FileTypeFile);
                    intent22222.addCategory("android.intent.category.LAUNCHER");
                    if (!DialogObject.isEncryptedDialog(longValue)) {
                    }
                    String str262222 = str252222;
                    intent22222.putExtra(str262222, notificationsController.currentAccount);
                    PendingIntent activity2222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22222, 1073741824);
                    NotificationCompat.WearableExtender wearableExtender2222 = new NotificationCompat.WearableExtender();
                    if (action2 != null) {
                    }
                    Intent intent32222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                    intent32222.addFlags(32);
                    intent32222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                    intent32222.putExtra(str8, longValue);
                    int i222222 = i5;
                    intent32222.putExtra(str9, i222222);
                    intent32222.putExtra(str262222, notificationsController.currentAccount);
                    NotificationCompat.Action build42222 = new NotificationCompat.Action.Builder(org.telegram.messenger.beta.R.drawable.msg_markread, LocaleController.getString("MarkAsRead", org.telegram.messenger.beta.R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent32222, 134217728)).setSemanticAction(2).setShowsUserInterface(false).build();
                    if (DialogObject.isEncryptedDialog(longValue)) {
                    }
                    if (str10 == null) {
                    }
                    wearableExtender2222.setBridgeTag("tgaccount" + j52222);
                    ArrayList arrayList122222 = arrayList3;
                    long j62222 = ((long) ((MessageObject) arrayList122222.get(0)).messageOwner.date) * 1000;
                    LongSparseArray longSparseArray92222 = longSparseArray7;
                    NotificationCompat.Builder category2222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str242222).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setContentText(sb32222.toString()).setAutoCancel(true).setNumber(arrayList122222.size()).setColor(-15618822).setGroupSummary(false).setWhen(j62222).setShowWhen(true).setStyle(messagingStyle52222).setContentIntent(activity2222).extend(wearableExtender2222).setSortKey(String.valueOf(Long.MAX_VALUE - j62222)).setCategory(RemoteMessageConst.MessageBody.MSG);
                    Intent intent42222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                    intent42222.putExtra("messageDate", i4);
                    intent42222.putExtra("dialogId", longValue);
                    intent42222.putExtra(str262222, notificationsController.currentAccount);
                    category2222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent42222, 134217728));
                    if (z5) {
                    }
                    if (action2 != null) {
                    }
                    if (!z12) {
                    }
                    if (arrayList.size() != 1) {
                    }
                    if (DialogObject.isEncryptedDialog(longValue)) {
                    }
                    if (bitmap2 != null) {
                    }
                    if (!AndroidUtilities.needShowPasscode(false)) {
                    }
                    if (tLRPC$Chat2 == null) {
                    }
                    tLRPC$User3 = tLRPC$User2;
                    boolean z152222 = z5;
                    if (Build.VERSION.SDK_INT >= 26) {
                    }
                    j2 = j52222;
                    z4 = z152222;
                    longSparseArray4 = longSparseArray8;
                    notification = notification222;
                    longSparseArray3 = longSparseArray92222;
                    arrayList2 = arrayList9;
                    arrayList2.add(new C1NotificationHolder(num2.intValue(), longValue, str242222, tLRPC$User3, tLRPC$Chat2, category2222, str2, jArr, i, uri, i2, z, z2, z3, i3));
                    notificationsController = this;
                    notificationsController.wearNotificationsIds.put(longValue, num2);
                }
            } else {
                arrayList3 = arrayList10;
                i4 = i19;
                z5 = z11;
                if (longValue != globalSecretChatId) {
                    int encryptedChatId = DialogObject.getEncryptedChatId(longValue);
                    TLRPC$EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(encryptedChatId));
                    if (encryptedChat == null) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.w("not found secret chat to show dialog notification " + encryptedChatId);
                        }
                    } else {
                        tLRPC$User4 = getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
                        if (tLRPC$User4 == null) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.w("not found secret chat user to show dialog notification " + encryptedChat.user_id);
                            }
                        }
                    }
                    j2 = clientUserId;
                    notification = build;
                    longSparseArray3 = longSparseArray7;
                    arrayList2 = arrayList9;
                    longSparseArray4 = longSparseArray8;
                    z4 = z5;
                } else {
                    tLRPC$User4 = null;
                }
                str22 = LocaleController.getString("SecretChatName", org.telegram.messenger.beta.R.string.SecretChatName);
                z8 = false;
                tLRPC$Chat = null;
                z7 = false;
                z6 = false;
                tLRPC$FileLocation = null;
                String str2322 = str22;
                tLRPC$User = tLRPC$User4;
                str3 = str2322;
                Notification notification2222 = build;
                if (!z12) {
                }
                if (tLRPC$FileLocation2 == null) {
                }
                if (tLRPC$Chat == null) {
                }
                File file3222 = file;
                str7 = "currentAccount";
                if (z8) {
                }
                tLRPC$Chat2 = tLRPC$Chat;
                bitmap2 = bitmap;
                Intent intent622 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                intent622.putExtra("dialog_id", longValue);
                intent622.putExtra("max_id", id);
                intent622.putExtra(str7, notificationsController.currentAccount);
                num2 = num;
                PendingIntent broadcast222 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent622, 134217728);
                RemoteInput build2222 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", org.telegram.messenger.beta.R.string.Reply)).build();
                if (!DialogObject.isChatDialog(longValue)) {
                }
                action = new NotificationCompat.Action.Builder(org.telegram.messenger.beta.R.drawable.ic_reply_icon, str20, broadcast222).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build2222).setShowsUserInterface(false).build();
                num3 = notificationsController.pushDialogs.get(longValue);
                if (num3 == null) {
                }
                int max222 = Math.max(num3.intValue(), arrayList3.size());
                if (max222 > 1) {
                }
                person = (Person) longSparseArray7.get(clientUserId);
                if (Build.VERSION.SDK_INT >= 28) {
                }
                str9 = "max_id";
                str8 = "dialog_id";
                boolean z1422222 = !(messageObject.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest);
                if (person == null) {
                }
                messagingStyle = new NotificationCompat.MessagingStyle("");
                messagingStyle2 = messagingStyle;
                i6 = Build.VERSION.SDK_INT;
                if (i6 >= 28) {
                }
                messagingStyle2.setConversationTitle(format);
                messagingStyle2.setGroupConversation(i6 >= 28 || (!z8 && DialogObject.isChatDialog(longValue)) || UserObject.isReplyUser(longValue));
                StringBuilder sb222222 = new StringBuilder();
                String[] strArr22222 = new String[1];
                action2 = action;
                boolean[] zArr22222 = new boolean[1];
                size = arrayList3.size() - 1;
                int i2122222 = 0;
                ArrayList<TLRPC$TL_keyboardButtonRow> arrayList1122222 = null;
                while (size >= 0) {
                }
                long j522222 = clientUserId;
                String str2422222 = str4;
                StringBuilder sb322222 = sb222222;
                NotificationCompat.MessagingStyle messagingStyle522222 = messagingStyle2;
                String str2522222 = str7;
                Intent intent222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                intent222222.setAction("com.tmessages.openchat" + Math.random() + Integer.MAX_VALUE);
                intent222222.setFlags(ConnectionsManager.FileTypeFile);
                intent222222.addCategory("android.intent.category.LAUNCHER");
                if (!DialogObject.isEncryptedDialog(longValue)) {
                }
                String str2622222 = str2522222;
                intent222222.putExtra(str2622222, notificationsController.currentAccount);
                PendingIntent activity22222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222222, 1073741824);
                NotificationCompat.WearableExtender wearableExtender22222 = new NotificationCompat.WearableExtender();
                if (action2 != null) {
                }
                Intent intent322222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                intent322222.addFlags(32);
                intent322222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                intent322222.putExtra(str8, longValue);
                int i2222222 = i5;
                intent322222.putExtra(str9, i2222222);
                intent322222.putExtra(str2622222, notificationsController.currentAccount);
                NotificationCompat.Action build422222 = new NotificationCompat.Action.Builder(org.telegram.messenger.beta.R.drawable.msg_markread, LocaleController.getString("MarkAsRead", org.telegram.messenger.beta.R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent322222, 134217728)).setSemanticAction(2).setShowsUserInterface(false).build();
                if (DialogObject.isEncryptedDialog(longValue)) {
                }
                if (str10 == null) {
                }
                wearableExtender22222.setBridgeTag("tgaccount" + j522222);
                ArrayList arrayList1222222 = arrayList3;
                long j622222 = ((long) ((MessageObject) arrayList1222222.get(0)).messageOwner.date) * 1000;
                LongSparseArray longSparseArray922222 = longSparseArray7;
                NotificationCompat.Builder category22222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str2422222).setSmallIcon(org.telegram.messenger.beta.R.drawable.notification).setContentText(sb322222.toString()).setAutoCancel(true).setNumber(arrayList1222222.size()).setColor(-15618822).setGroupSummary(false).setWhen(j622222).setShowWhen(true).setStyle(messagingStyle522222).setContentIntent(activity22222).extend(wearableExtender22222).setSortKey(String.valueOf(Long.MAX_VALUE - j622222)).setCategory(RemoteMessageConst.MessageBody.MSG);
                Intent intent422222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                intent422222.putExtra("messageDate", i4);
                intent422222.putExtra("dialogId", longValue);
                intent422222.putExtra(str2622222, notificationsController.currentAccount);
                category22222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent422222, 134217728));
                if (z5) {
                }
                if (action2 != null) {
                }
                if (!z12) {
                }
                if (arrayList.size() != 1) {
                }
                if (DialogObject.isEncryptedDialog(longValue)) {
                }
                if (bitmap2 != null) {
                }
                if (!AndroidUtilities.needShowPasscode(false)) {
                }
                if (tLRPC$Chat2 == null) {
                }
                tLRPC$User3 = tLRPC$User2;
                boolean z1522222 = z5;
                if (Build.VERSION.SDK_INT >= 26) {
                }
                j2 = j522222;
                z4 = z1522222;
                longSparseArray4 = longSparseArray8;
                notification = notification2222;
                longSparseArray3 = longSparseArray922222;
                arrayList2 = arrayList9;
                arrayList2.add(new C1NotificationHolder(num2.intValue(), longValue, str2422222, tLRPC$User3, tLRPC$Chat2, category22222, str2, jArr, i, uri, i2, z, z2, z3, i3));
                notificationsController = this;
                notificationsController.wearNotificationsIds.put(longValue, num2);
            }
            i16 = i18 + 1;
            arrayList8 = arrayList2;
            longSparseArray6 = longSparseArray4;
            size3 = i17;
            z11 = z4;
            arrayList6 = arrayList;
            longSparseArray5 = longSparseArray2;
            clientUserId = j2;
            build = notification;
            longSparseArray7 = longSparseArray3;
            i15 = 7;
            i11 = 0;
        }
        ArrayList arrayList13 = arrayList8;
        LongSparseArray longSparseArray10 = longSparseArray6;
        Notification notification3 = build;
        LongSparseArray longSparseArray11 = longSparseArray7;
        if (z11) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("show summary with id " + notificationsController.notificationId);
            }
            try {
                notificationManager.notify(notificationsController.notificationId, notification3);
            } catch (SecurityException e2) {
                FileLog.e(e2);
                resetNotificationSound(builder, j, str2, jArr, i, uri, i2, z, z2, z3, i3);
            }
        } else if (notificationsController.openedInBubbleDialogs.isEmpty()) {
            notificationManager.cancel(notificationsController.notificationId);
        }
        int i27 = 0;
        while (i27 < longSparseArray10.size()) {
            LongSparseArray longSparseArray12 = longSparseArray10;
            if (!notificationsController.openedInBubbleDialogs.contains(Long.valueOf(longSparseArray12.keyAt(i27)))) {
                Integer num6 = (Integer) longSparseArray12.valueAt(i27);
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("cancel notification id " + num6);
                }
                notificationManager.cancel(num6.intValue());
            }
            i27++;
            longSparseArray10 = longSparseArray12;
        }
        ArrayList arrayList14 = new ArrayList(arrayList13.size());
        int size5 = arrayList13.size();
        int i28 = 0;
        while (i28 < size5) {
            C1NotificationHolder c1NotificationHolder = (C1NotificationHolder) arrayList13.get(i28);
            arrayList14.clear();
            if (Build.VERSION.SDK_INT < 29 || DialogObject.isEncryptedDialog(c1NotificationHolder.dialogId)) {
                longSparseArray = longSparseArray11;
            } else {
                NotificationCompat.Builder builder2 = c1NotificationHolder.notification;
                long j7 = c1NotificationHolder.dialogId;
                longSparseArray = longSparseArray11;
                String createNotificationShortcut = createNotificationShortcut(builder2, j7, c1NotificationHolder.name, c1NotificationHolder.user, c1NotificationHolder.chat, (Person) longSparseArray.get(j7));
                if (createNotificationShortcut != null) {
                    arrayList14.add(createNotificationShortcut);
                }
            }
            c1NotificationHolder.call();
            if (!unsupportedNotificationShortcut() && !arrayList14.isEmpty()) {
                ShortcutManagerCompat.removeDynamicShortcuts(ApplicationLoader.applicationContext, arrayList14);
            }
            i28++;
            longSparseArray11 = longSparseArray;
        }
    }

    /* renamed from: org.telegram.messenger.NotificationsController$1NotificationHolder */
    /* loaded from: classes.dex */
    public class C1NotificationHolder {
        TLRPC$Chat chat;
        long dialogId;
        int id;
        String name;
        NotificationCompat.Builder notification;
        TLRPC$User user;
        final /* synthetic */ String val$chatName;
        final /* synthetic */ int val$chatType;
        final /* synthetic */ int val$importance;
        final /* synthetic */ boolean val$isDefault;
        final /* synthetic */ boolean val$isInApp;
        final /* synthetic */ boolean val$isSilent;
        final /* synthetic */ int val$ledColor;
        final /* synthetic */ Uri val$sound;
        final /* synthetic */ long[] val$vibrationPattern;

        C1NotificationHolder(int i, long j, String str, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, NotificationCompat.Builder builder, String str2, long[] jArr, int i2, Uri uri, int i3, boolean z, boolean z2, boolean z3, int i4) {
            NotificationsController.this = r4;
            this.val$chatName = str2;
            this.val$vibrationPattern = jArr;
            this.val$ledColor = i2;
            this.val$sound = uri;
            this.val$importance = i3;
            this.val$isDefault = z;
            this.val$isInApp = z2;
            this.val$isSilent = z3;
            this.val$chatType = i4;
            this.id = i;
            this.name = str;
            this.user = tLRPC$User;
            this.chat = tLRPC$Chat;
            this.notification = builder;
            this.dialogId = j;
        }

        void call() {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.w("show dialog notification with id " + this.id + " " + this.dialogId + " user=" + this.user + " chat=" + this.chat);
            }
            try {
                NotificationsController.notificationManager.notify(this.id, this.notification.build());
            } catch (SecurityException e) {
                FileLog.e(e);
                NotificationsController.this.resetNotificationSound(this.notification, this.dialogId, this.val$chatName, this.val$vibrationPattern, this.val$ledColor, this.val$sound, this.val$importance, this.val$isDefault, this.val$isInApp, this.val$isSilent, this.val$chatType);
            }
        }
    }

    public static /* synthetic */ void lambda$showExtraNotifications$34(Uri uri) {
        ApplicationLoader.applicationContext.revokeUriPermission(uri, 1);
    }

    public static /* synthetic */ void lambda$loadRoundAvatar$36(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
        imageDecoder.setPostProcessor(NotificationsController$$ExternalSyntheticLambda1.INSTANCE);
    }

    @TargetApi(AvailableCode.APP_IS_BACKGROUND_OR_LOCKED)
    private void loadRoundAvatar(File file, Person.Builder builder) {
        if (file != null) {
            try {
                builder.setIcon(IconCompat.createWithBitmap(ImageDecoder.decodeBitmap(ImageDecoder.createSource(file), NotificationsController$$ExternalSyntheticLambda0.INSTANCE)));
            } catch (Throwable unused) {
            }
        }
    }

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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$playOutChatSound$38();
            }
        });
    }

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
                this.soundOut = this.soundPool.load(ApplicationLoader.applicationContext, org.telegram.messenger.beta.R.raw.sound_out, 1);
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

    public static /* synthetic */ void lambda$playOutChatSound$37(SoundPool soundPool, int i, int i2) {
        if (i2 == 0) {
            try {
                soundPool.play(i, 1.0f, 1.0f, 1, 0, 1.0f);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public void clearDialogNotificationsSettings(long j) {
        SharedPreferences.Editor edit = getAccountInstance().getNotificationsSettings().edit();
        SharedPreferences.Editor remove = edit.remove("notify2_" + j);
        remove.remove("custom_" + j);
        getMessagesStorage().setDialogFlags(j, 0L);
        TLRPC$Dialog tLRPC$Dialog = getMessagesController().dialogs_dict.get(j);
        if (tLRPC$Dialog != null) {
            tLRPC$Dialog.notify_settings = new TLRPC$TL_peerNotifySettings();
        }
        edit.commit();
        getNotificationsController().updateServerNotificationsSettings(j, true);
    }

    public void setDialogNotificationsSettings(long j, int i) {
        SharedPreferences.Editor edit = getAccountInstance().getNotificationsSettings().edit();
        TLRPC$Dialog tLRPC$Dialog = MessagesController.getInstance(UserConfig.selectedAccount).dialogs_dict.get(j);
        if (i == 4) {
            if (isGlobalNotificationsEnabled(j)) {
                edit.remove("notify2_" + j);
            } else {
                edit.putInt("notify2_" + j, 0);
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
                currentTime = Integer.MAX_VALUE;
            }
            long j2 = 1;
            if (i == 3) {
                edit.putInt("notify2_" + j, 2);
            } else {
                edit.putInt("notify2_" + j, 3);
                edit.putInt("notifyuntil_" + j, currentTime);
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
        updateServerNotificationsSettings(j);
    }

    public void updateServerNotificationsSettings(long j) {
        updateServerNotificationsSettings(j, true);
    }

    public void updateServerNotificationsSettings(long j, boolean z) {
        int i = 0;
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
        tLRPC$TL_inputPeerNotifySettings.show_previews = notificationsSettings.getBoolean("content_preview_" + j, true);
        TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings2 = tLRPC$TL_account_updateNotifySettings.settings;
        tLRPC$TL_inputPeerNotifySettings2.flags = tLRPC$TL_inputPeerNotifySettings2.flags | 2;
        tLRPC$TL_inputPeerNotifySettings2.silent = notificationsSettings.getBoolean("silent_" + j, false);
        int i2 = notificationsSettings.getInt("notify2_" + j, -1);
        if (i2 != -1) {
            TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings3 = tLRPC$TL_account_updateNotifySettings.settings;
            tLRPC$TL_inputPeerNotifySettings3.flags |= 4;
            if (i2 == 3) {
                tLRPC$TL_inputPeerNotifySettings3.mute_until = notificationsSettings.getInt("notifyuntil_" + j, 0);
            } else {
                if (i2 == 2) {
                    i = Integer.MAX_VALUE;
                }
                tLRPC$TL_inputPeerNotifySettings3.mute_until = i;
            }
        }
        long j2 = notificationsSettings.getLong("sound_document_id_" + j, 0L);
        String string = notificationsSettings.getString("sound_path_" + j, null);
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
                tLRPC$TL_notificationSoundLocal.title = notificationsSettings.getString("sound_" + j, null);
                tLRPC$TL_notificationSoundLocal.data = string;
                tLRPC$TL_account_updateNotifySettings.settings.sound = tLRPC$TL_notificationSoundLocal;
            }
        } else {
            tLRPC$TL_inputPeerNotifySettings4.sound = new TLRPC$TL_notificationSoundDefault();
        }
        TLRPC$TL_inputNotifyPeer tLRPC$TL_inputNotifyPeer = new TLRPC$TL_inputNotifyPeer();
        tLRPC$TL_account_updateNotifySettings.peer = tLRPC$TL_inputNotifyPeer;
        tLRPC$TL_inputNotifyPeer.peer = getMessagesController().getInputPeer(j);
        getConnectionsManager().sendRequest(tLRPC$TL_account_updateNotifySettings, NotificationsController$$ExternalSyntheticLambda40.INSTANCE);
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
            str3 = "GroupSoundDocId";
            str2 = "GroupSoundPath";
        } else if (i == 1) {
            tLRPC$TL_account_updateNotifySettings.peer = new TLRPC$TL_inputNotifyUsers();
            tLRPC$TL_account_updateNotifySettings.settings.mute_until = notificationsSettings.getInt("EnableAll2", 0);
            tLRPC$TL_account_updateNotifySettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewAll", true);
            str = "GlobalSound";
            str3 = "GlobalSoundDocId";
            str2 = "GlobalSoundPath";
        } else {
            tLRPC$TL_account_updateNotifySettings.peer = new TLRPC$TL_inputNotifyBroadcasts();
            tLRPC$TL_account_updateNotifySettings.settings.mute_until = notificationsSettings.getInt("EnableChannel2", 0);
            tLRPC$TL_account_updateNotifySettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewChannel", true);
            str = "ChannelSound";
            str3 = "ChannelSoundDocId";
            str2 = "ChannelSoundPath";
        }
        tLRPC$TL_account_updateNotifySettings.settings.flags |= 8;
        long j = notificationsSettings.getLong(str3, 0L);
        String string = notificationsSettings.getString(str2, "NoSound");
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
        getConnectionsManager().sendRequest(tLRPC$TL_account_updateNotifySettings, NotificationsController$$ExternalSyntheticLambda39.INSTANCE);
    }

    public boolean isGlobalNotificationsEnabled(long j) {
        return isGlobalNotificationsEnabled(j, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0028, code lost:
        if (r4.megagroup == false) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x000e, code lost:
        if (r6.booleanValue() != false) goto L13;
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

    public void muteDialog(long j, boolean z) {
        if (z) {
            getInstance(this.currentAccount).muteUntil(j, Integer.MAX_VALUE);
            return;
        }
        boolean isGlobalNotificationsEnabled = getInstance(this.currentAccount).isGlobalNotificationsEnabled(j);
        SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
        if (isGlobalNotificationsEnabled) {
            edit.remove("notify2_" + j);
        } else {
            edit.putInt("notify2_" + j, 0);
        }
        getMessagesStorage().setDialogFlags(j, 0L);
        edit.apply();
        TLRPC$Dialog tLRPC$Dialog = getMessagesController().dialogs_dict.get(j);
        if (tLRPC$Dialog != null) {
            tLRPC$Dialog.notify_settings = new TLRPC$TL_peerNotifySettings();
        }
        updateServerNotificationsSettings(j);
    }
}
