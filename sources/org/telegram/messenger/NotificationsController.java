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
import org.telegram.tgnet.TLRPC$NotificationSound;
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
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.BubbleActivity;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PopupNotificationActivity;
import org.telegram.ui.Stories.recorder.StoryEntry;
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
    private int openedTopicId;
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
    public static /* synthetic */ void lambda$updateServerNotificationsSettings$45(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateServerNotificationsSettings$46(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
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
        this.openedTopicId = 0;
        this.lastButtonId = 5000;
        this.total_unread_count = 0;
        this.personalCount = 0;
        this.notifyCheck = false;
        this.lastOnlineFromOtherDevice = 0;
        this.lastBadgeCount = -1;
        this.mediaSpoilerEffect = new SpoilerEffect();
        this.spoilerChars = new char[]{10252, 10338, 10385, 10280};
        this.checkStoryPushesRunnable = new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda16
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
        this.notificationDelayRunnable = new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda12
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
        String valueOf;
        long j2 = (i << 12) + j;
        LongSparseArray<String> longSparseArray = sharedPrefCachedKeys;
        int indexOfKey = longSparseArray.indexOfKey(j2);
        if (indexOfKey >= 0) {
            return longSparseArray.valueAt(indexOfKey);
        }
        if (i != 0) {
            valueOf = String.format(Locale.US, "%d_%d", Long.valueOf(j), Integer.valueOf(i));
        } else {
            valueOf = String.valueOf(j);
        }
        longSparseArray.put(j2, valueOf);
        return valueOf;
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda13
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

    public void setOpenedDialogId(final long j, final int i) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda28
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda44
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda21
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda11
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda36
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda32
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda34
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda19
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda42
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda35
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda22
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

    public void processDeleteStory(final long j, final int i) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processDeleteStory$13(j, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0039  */
    /* JADX WARN: Removed duplicated region for block: B:12:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processDeleteStory$13(long j, int i) {
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processReadStories$14(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processReadStories$14(long j) {
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processIgnoreStories$15();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processIgnoreStories$15() {
        boolean z = !this.storyPushMessages.isEmpty();
        this.storyPushMessages.clear();
        this.storyPushMessagesDict.clear();
        getMessagesStorage().deleteAllStoryPushMessages();
        if (z) {
            showOrUpdateNotification(false);
        }
    }

    public void processIgnoreStories(final long j) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processIgnoreStories$16(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processIgnoreStories$16(long j) {
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processReadMessages$18(longSparseIntArray, arrayList, j, i2, i, z);
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
    public /* synthetic */ void lambda$processReadMessages$18(LongSparseIntArray longSparseIntArray, final ArrayList arrayList, long j, int i, int i2, boolean z) {
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda33
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processReadMessages$17(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processReadMessages$17(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pushMessagesUpdated, new Object[0]);
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processEditedMessages$19(longSparseArray);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processEditedMessages$19(LongSparseArray longSparseArray) {
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
            notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda40
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$processNewMessages$23(arrayList, arrayList2, z2, z, countDownLatch);
                }
            });
        } else if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x004a, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserJoined) == false) goto L20;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processNewMessages$23(ArrayList arrayList, final ArrayList arrayList2, boolean z, boolean z2, CountDownLatch countDownLatch) {
        boolean z3;
        Integer num;
        boolean z4;
        int i;
        boolean z5;
        long j;
        boolean z6;
        long j2;
        int i2;
        long j3;
        long j4;
        long j5;
        long j6;
        MessageObject messageObject;
        TLRPC$Message tLRPC$Message;
        ArrayList arrayList3 = arrayList;
        LongSparseArray longSparseArray = new LongSparseArray();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        boolean z7 = notificationsSettings.getBoolean("PinnedMessages", true);
        final int i3 = 0;
        int i4 = 0;
        boolean z8 = false;
        boolean z9 = false;
        boolean z10 = false;
        boolean z11 = false;
        while (i4 < arrayList.size()) {
            MessageObject messageObject2 = (MessageObject) arrayList3.get(i4);
            if (messageObject2.messageOwner != null) {
                if (!messageObject2.isImportedForward()) {
                    TLRPC$Message tLRPC$Message2 = messageObject2.messageOwner;
                    TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message2.action;
                    if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetMessagesTTL)) {
                        if (tLRPC$Message2.silent) {
                            if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp)) {
                            }
                        }
                    }
                }
                i = i4;
                z5 = z8;
                z8 = z5;
                i4 = i + 1;
                arrayList3 = arrayList;
            }
            if (!MessageObject.isTopicActionMessage(messageObject2)) {
                if (messageObject2.isStoryPush) {
                    long currentTimeMillis = messageObject2.messageOwner == null ? System.currentTimeMillis() : tLRPC$Message.date * 1000;
                    long dialogId = messageObject2.getDialogId();
                    int id = messageObject2.getId();
                    StoryNotification storyNotification = this.storyPushMessagesDict.get(dialogId);
                    if (storyNotification != null) {
                        storyNotification.dateByIds.put(Integer.valueOf(id), new Pair<>(Long.valueOf(currentTimeMillis), Long.valueOf(currentTimeMillis + 86400000)));
                        boolean z12 = storyNotification.hidden;
                        boolean z13 = messageObject2.isStoryPushHidden;
                        if (z12 != z13) {
                            storyNotification.hidden = z13;
                            z11 = true;
                        }
                        storyNotification.date = storyNotification.getLeastDate();
                        getMessagesStorage().putStoryPushMessage(storyNotification);
                        z9 = true;
                    } else {
                        StoryNotification storyNotification2 = new StoryNotification(dialogId, messageObject2.localName, id, currentTimeMillis);
                        storyNotification2.hidden = messageObject2.isStoryPushHidden;
                        this.storyPushMessages.add(storyNotification2);
                        this.storyPushMessagesDict.put(dialogId, storyNotification2);
                        getMessagesStorage().putStoryPushMessage(storyNotification2);
                        z8 = true;
                        z11 = true;
                    }
                    Collections.sort(this.storyPushMessages, Comparator$-CC.comparingLong(NotificationsController$$ExternalSyntheticLambda47.INSTANCE));
                    i = i4;
                } else {
                    int id2 = messageObject2.getId();
                    long j7 = messageObject2.isFcmMessage() ? messageObject2.messageOwner.random_id : 0L;
                    long dialogId2 = messageObject2.getDialogId();
                    if (messageObject2.isFcmMessage()) {
                        z4 = messageObject2.localChannel;
                    } else if (DialogObject.isChatDialog(dialogId2)) {
                        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-dialogId2));
                        z4 = ChatObject.isChannel(chat) && !chat.megagroup;
                    } else {
                        z4 = false;
                    }
                    long j8 = messageObject2.messageOwner.peer_id.channel_id;
                    long j9 = j8 != 0 ? -j8 : 0L;
                    SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(j9);
                    MessageObject messageObject3 = sparseArray != null ? sparseArray.get(id2) : null;
                    if (messageObject3 == null) {
                        i = i4;
                        z5 = z8;
                        long j10 = messageObject2.messageOwner.random_id;
                        if (j10 != 0 && (messageObject3 = this.fcmRandomMessagesDict.get(j10)) != null) {
                            this.fcmRandomMessagesDict.remove(messageObject2.messageOwner.random_id);
                        }
                    } else {
                        i = i4;
                        z5 = z8;
                    }
                    MessageObject messageObject4 = messageObject3;
                    if (messageObject4 != null) {
                        if (messageObject4.isFcmMessage()) {
                            if (sparseArray == null) {
                                sparseArray = new SparseArray<>();
                                this.pushMessagesDict.put(j9, sparseArray);
                            }
                            sparseArray.put(id2, messageObject2);
                            int indexOf = this.pushMessages.indexOf(messageObject4);
                            if (indexOf >= 0) {
                                this.pushMessages.set(indexOf, messageObject2);
                                messageObject = messageObject2;
                                i3 = addToPopupMessages(arrayList2, messageObject2, dialogId2, z4, notificationsSettings);
                            } else {
                                messageObject = messageObject2;
                            }
                            if (z) {
                                boolean z14 = messageObject.localEdit;
                                if (z14) {
                                    getMessagesStorage().putPushMessage(messageObject);
                                }
                                z9 = z14;
                            }
                        }
                    } else if (!z9) {
                        if (z) {
                            getMessagesStorage().putPushMessage(messageObject2);
                        }
                        int topicId = MessageObject.getTopicId(messageObject2.messageOwner, getMessagesController().isForum(messageObject2));
                        long j11 = j9;
                        if (dialogId2 != this.openedDialogId || !ApplicationLoader.isScreenOn) {
                            TLRPC$Message tLRPC$Message3 = messageObject2.messageOwner;
                            if (!tLRPC$Message3.mentioned) {
                                j = dialogId2;
                            } else if (z7 || !(tLRPC$Message3.action instanceof TLRPC$TL_messageActionPinMessage)) {
                                j = messageObject2.getFromChatId();
                            }
                            if (isPersonalMessage(messageObject2)) {
                                this.personalCount++;
                            }
                            DialogObject.isChatDialog(j);
                            int indexOfKey = longSparseArray.indexOfKey(j);
                            if (indexOfKey >= 0 && topicId == 0) {
                                z6 = ((Boolean) longSparseArray.valueAt(indexOfKey)).booleanValue();
                            } else {
                                int notifyOverride = getNotifyOverride(notificationsSettings, j, topicId);
                                if (notifyOverride == -1) {
                                    z6 = isGlobalNotificationsEnabled(j, Boolean.valueOf(z4));
                                } else {
                                    z6 = notifyOverride != 2;
                                }
                                longSparseArray.put(j, Boolean.valueOf(z6));
                            }
                            if (z6) {
                                if (z) {
                                    j3 = j;
                                    j4 = dialogId2;
                                    i2 = topicId;
                                    j5 = j7;
                                    j6 = j11;
                                } else {
                                    j3 = j;
                                    i2 = topicId;
                                    j6 = j11;
                                    j4 = dialogId2;
                                    j5 = j7;
                                    i3 = addToPopupMessages(arrayList2, messageObject2, j3, z4, notificationsSettings);
                                }
                                if (!z10) {
                                    z10 = messageObject2.messageOwner.from_scheduled;
                                }
                                this.delayedPushMessages.add(messageObject2);
                                appendMessage(messageObject2);
                                if (id2 != 0) {
                                    if (sparseArray == null) {
                                        sparseArray = new SparseArray<>();
                                        this.pushMessagesDict.put(j6, sparseArray);
                                    }
                                    sparseArray.put(id2, messageObject2);
                                } else {
                                    long j12 = j5;
                                    if (j12 != 0) {
                                        this.fcmRandomMessagesDict.put(j12, messageObject2);
                                    }
                                }
                                j2 = j3;
                                long j13 = j4;
                                if (j13 != j2) {
                                    Integer num2 = this.pushDialogsOverrideMention.get(j13);
                                    this.pushDialogsOverrideMention.put(j13, Integer.valueOf(num2 == null ? 1 : num2.intValue() + 1));
                                }
                            } else {
                                j2 = j;
                                i2 = topicId;
                            }
                            if (messageObject2.isReactionPush) {
                                SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
                                sparseBooleanArray.put(id2, true);
                                getMessagesController().checkUnreadReactions(j2, i2, sparseBooleanArray);
                            }
                            z8 = true;
                        } else if (!z) {
                            playInChatSound();
                        }
                    }
                    z8 = z5;
                }
                i4 = i + 1;
                arrayList3 = arrayList;
            }
            i = i4;
            z5 = z8;
            z8 = z5;
            i4 = i + 1;
            arrayList3 = arrayList;
        }
        boolean z15 = z8;
        if (z15) {
            this.notifyCheck = z2;
        }
        if (!arrayList2.isEmpty() && !AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$processNewMessages$21(arrayList2, i3);
                }
            });
        }
        if (z || z10) {
            if (z9) {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
            } else if (z15) {
                MessageObject messageObject5 = (MessageObject) arrayList.get(0);
                long dialogId3 = messageObject5.getDialogId();
                int topicId2 = MessageObject.getTopicId(messageObject5.messageOwner, getMessagesController().isForum(dialogId3));
                Boolean valueOf = messageObject5.isFcmMessage() ? Boolean.valueOf(messageObject5.localChannel) : null;
                int i5 = this.total_unread_count;
                int notifyOverride2 = getNotifyOverride(notificationsSettings, dialogId3, topicId2);
                if (notifyOverride2 == -1) {
                    z3 = isGlobalNotificationsEnabled(dialogId3, valueOf);
                } else {
                    z3 = notifyOverride2 != 2;
                }
                Integer num3 = this.pushDialogs.get(dialogId3);
                int intValue = num3 != null ? num3.intValue() + 1 : 1;
                if (this.notifyCheck && !z3 && (num = this.pushDialogsOverrideMention.get(dialogId3)) != null && num.intValue() != 0) {
                    intValue = num.intValue();
                    z3 = true;
                }
                if (z3 && !messageObject5.isStoryPush) {
                    if (getMessagesController().isForum(dialogId3)) {
                        int i6 = this.total_unread_count - ((num3 == null || num3.intValue() <= 0) ? 0 : 1);
                        this.total_unread_count = i6;
                        this.total_unread_count = i6 + (intValue > 0 ? 1 : 0);
                    } else {
                        if (num3 != null) {
                            this.total_unread_count -= num3.intValue();
                        }
                        this.total_unread_count += intValue;
                    }
                    this.pushDialogs.put(dialogId3, Integer.valueOf(intValue));
                }
                if (i5 != this.total_unread_count || z11) {
                    this.delayedPushMessages.clear();
                    showOrUpdateNotification(this.notifyCheck);
                    final int size = this.pushDialogs.size();
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda23
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationsController.this.lambda$processNewMessages$22(size);
                        }
                    });
                }
                this.notifyCheck = false;
                if (this.showBadgeNumber) {
                    setBadge(getTotalAllUnreadCount());
                }
            }
        }
        if (z11) {
            updateStoryPushesRunnable();
        }
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processNewMessages$21(ArrayList arrayList, int i) {
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
    public /* synthetic */ void lambda$processNewMessages$22(int i) {
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processDialogsUpdateRead$26(longSparseIntArray, arrayList);
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
    public /* synthetic */ void lambda$processDialogsUpdateRead$26(LongSparseIntArray longSparseIntArray, final ArrayList arrayList) {
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda37
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$processDialogsUpdateRead$24(arrayList);
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
                    NotificationsController.this.lambda$processDialogsUpdateRead$25(size);
                }
            });
        }
        this.notifyCheck = false;
        if (this.showBadgeNumber) {
            setBadge(getTotalAllUnreadCount());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDialogsUpdateRead$24(ArrayList arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            this.popupMessages.remove(arrayList.get(i));
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDialogsUpdateRead$25(int i) {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsCountUpdated, Integer.valueOf(this.currentAccount));
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsUnreadCounterChanged, Integer.valueOf(i));
    }

    public void processLoadedUnreadMessages(final LongSparseArray<Integer> longSparseArray, final ArrayList<TLRPC$Message> arrayList, final ArrayList<MessageObject> arrayList2, ArrayList<TLRPC$User> arrayList3, ArrayList<TLRPC$Chat> arrayList4, ArrayList<TLRPC$EncryptedChat> arrayList5, final Collection<StoryNotification> collection) {
        getMessagesController().putUsers(arrayList3, true);
        getMessagesController().putChats(arrayList4, true);
        getMessagesController().putEncryptedChats(arrayList5, true);
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processLoadedUnreadMessages$29(arrayList, longSparseArray, arrayList2, collection);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedUnreadMessages$29(ArrayList arrayList, LongSparseArray longSparseArray, ArrayList arrayList2, Collection collection) {
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
        this.storyPushMessages.clear();
        this.storyPushMessagesDict.clear();
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
        if (collection != null) {
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                StoryNotification storyNotification = (StoryNotification) it.next();
                long j8 = storyNotification.dialogId;
                StoryNotification storyNotification2 = this.storyPushMessagesDict.get(j8);
                if (storyNotification2 != null) {
                    storyNotification2.dateByIds.putAll(storyNotification.dateByIds);
                } else {
                    this.storyPushMessages.add(storyNotification);
                    this.storyPushMessagesDict.put(j8, storyNotification);
                }
            }
            Collections.sort(this.storyPushMessages, Comparator$-CC.comparingLong(NotificationsController$$ExternalSyntheticLambda46.INSTANCE));
        }
        final int size = this.pushDialogs.size();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processLoadedUnreadMessages$28(size);
            }
        });
        showOrUpdateNotification(SystemClock.elapsedRealtime() / 1000 < 60);
        if (this.showBadgeNumber) {
            setBadge(getTotalAllUnreadCount());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedUnreadMessages$28(int i) {
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
    public /* synthetic */ void lambda$updateBadge$30() {
        setBadge(getTotalAllUnreadCount());
    }

    public void updateBadge() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$updateBadge$30();
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
        if (r12.getBoolean("EnablePreviewAll", true) == false) goto L793;
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
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L791;
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
    /* JADX WARN: Code restructure failed: missing block: B:810:0x10f3, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaStory) == false) goto L785;
     */
    /* JADX WARN: Code restructure failed: missing block: B:812:0x10f9, code lost:
        if (((org.telegram.tgnet.TLRPC$TL_messageMediaStory) r2).via_mention == false) goto L783;
     */
    /* JADX WARN: Code restructure failed: missing block: B:813:0x10fb, code lost:
        r0 = org.telegram.messenger.R.string.StoryNotificationMention;
        r1 = new java.lang.Object[1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:814:0x1103, code lost:
        if (r27[0] != null) goto L782;
     */
    /* JADX WARN: Code restructure failed: missing block: B:815:0x1105, code lost:
        r3 = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:816:0x1108, code lost:
        r3 = r27[0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:817:0x110a, code lost:
        r1[0] = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:818:0x1112, code lost:
        return org.telegram.messenger.LocaleController.formatString("StoryNotificationMention", r0, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:820:0x111b, code lost:
        return org.telegram.messenger.LocaleController.getString("Story", org.telegram.messenger.R.string.Story);
     */
    /* JADX WARN: Code restructure failed: missing block: B:822:0x1122, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageText) != false) goto L789;
     */
    /* JADX WARN: Code restructure failed: missing block: B:824:0x1128, code lost:
        return replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:826:0x112f, code lost:
        return org.telegram.messenger.LocaleController.getString(r1, org.telegram.messenger.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:828:0x1138, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachLocation", org.telegram.messenger.R.string.AttachLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:842:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:843:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.R.string.ChatThemeDisabled, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:844:?, code lost:
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

    /* JADX WARN: Code restructure failed: missing block: B:273:0x0630, code lost:
        if (r12.getBoolean(r22, true) == false) goto L832;
     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x063c, code lost:
        if (r12.getBoolean("EnablePreviewChannel", r15) != false) goto L287;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x063e, code lost:
        r6 = r29.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x0642, code lost:
        if ((r6 instanceof org.telegram.tgnet.TLRPC$TL_messageService) == false) goto L623;
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x0644, code lost:
        r7 = r6.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x0648, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser) == false) goto L330;
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x064a, code lost:
        r3 = r7.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x0650, code lost:
        if (r3 != 0) goto L296;
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x0659, code lost:
        if (r7.users.size() != 1) goto L296;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x065b, code lost:
        r3 = r29.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x0670, code lost:
        if (r3 == 0) goto L316;
     */
    /* JADX WARN: Code restructure failed: missing block: B:291:0x067a, code lost:
        if (r29.messageOwner.peer_id.channel_id == 0) goto L303;
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x067e, code lost:
        if (r5.megagroup != false) goto L303;
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x0680, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.R.string.ChannelAddedByNotification, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x0697, code lost:
        if (r3 != r19) goto L306;
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x0699, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.R.string.NotificationInvitedToGroup, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x06ae, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x06ba, code lost:
        if (r0 != null) goto L309;
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x06bc, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x06c2, code lost:
        if (r9 != r0.id) goto L315;
     */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x06c6, code lost:
        if (r5.megagroup == false) goto L314;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x06c8, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.R.string.NotificationGroupAddSelfMega, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x06dd, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.R.string.NotificationGroupAddSelf, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x06f2, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r2, r5.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x070e, code lost:
        r1 = new java.lang.StringBuilder();
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x071e, code lost:
        if (r3 >= r29.messageOwner.action.users.size()) goto L328;
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x0720, code lost:
        r4 = getMessagesController().getUser(r29.messageOwner.action.users.get(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x0734, code lost:
        if (r4 == null) goto L327;
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x0736, code lost:
        r4 = org.telegram.messenger.UserObject.getUserName(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x073e, code lost:
        if (r1.length() == 0) goto L324;
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x0740, code lost:
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x0745, code lost:
        r1.append(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x0748, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x074b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r2, r5.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x076a, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCall) == false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x0782, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled) == false) goto L338;
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x078e, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall) == false) goto L368;
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x0790, code lost:
        r3 = r7.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x0796, code lost:
        if (r3 != 0) goto L345;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x079f, code lost:
        if (r7.users.size() != 1) goto L345;
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x07a1, code lost:
        r3 = r29.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x07b6, code lost:
        if (r3 == 0) goto L354;
     */
    /* JADX WARN: Code restructure failed: missing block: B:336:0x07ba, code lost:
        if (r3 != r19) goto L350;
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x07bc, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.R.string.NotificationGroupInvitedYouToCall, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x07d1, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x07dd, code lost:
        if (r0 != null) goto L353;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x07df, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x07e1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r2, r5.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x07fd, code lost:
        r1 = new java.lang.StringBuilder();
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x080d, code lost:
        if (r3 >= r29.messageOwner.action.users.size()) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x080f, code lost:
        r4 = getMessagesController().getUser(r29.messageOwner.action.users.get(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:347:0x0823, code lost:
        if (r4 == null) goto L365;
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x0825, code lost:
        r4 = org.telegram.messenger.UserObject.getUserName(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x082d, code lost:
        if (r1.length() == 0) goto L362;
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x082f, code lost:
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x0834, code lost:
        r1.append(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x0837, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x083a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r2, r5.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x0859, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink) == false) goto L372;
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x0872, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle) == false) goto L376;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x0889, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto) != false) goto L607;
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x088d, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto) == false) goto L380;
     */
    /* JADX WARN: Code restructure failed: missing block: B:366:0x0893, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser) == false) goto L395;
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x0895, code lost:
        r3 = r7.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:368:0x0899, code lost:
        if (r3 != r19) goto L386;
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x08b5, code lost:
        if (r3 != r9) goto L390;
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x08c9, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r29.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:374:0x08db, code lost:
        if (r0 != null) goto L393;
     */
    /* JADX WARN: Code restructure failed: missing block: B:375:0x08dd, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:378:0x08fb, code lost:
        r9 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x08fe, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatCreate) == false) goto L399;
     */
    /* JADX WARN: Code restructure failed: missing block: B:382:0x090a, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate) == false) goto L403;
     */
    /* JADX WARN: Code restructure failed: missing block: B:385:0x0916, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo) == false) goto L407;
     */
    /* JADX WARN: Code restructure failed: missing block: B:388:0x092e, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) == false) goto L411;
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x0942, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken) == false) goto L415;
     */
    /* JADX WARN: Code restructure failed: missing block: B:394:0x094e, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPinMessage) == false) goto L587;
     */
    /* JADX WARN: Code restructure failed: missing block: B:396:0x0956, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r5) == false) goto L504;
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:0x095a, code lost:
        if (r5.megagroup == false) goto L421;
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x095e, code lost:
        r2 = r29.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x0960, code lost:
        if (r2 != null) goto L425;
     */
    /* JADX WARN: Code restructure failed: missing block: B:404:0x097a, code lost:
        if (r2.isMusic() == false) goto L428;
     */
    /* JADX WARN: Code restructure failed: missing block: B:405:0x097c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.R.string.NotificationActionPinnedMusicChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x0992, code lost:
        if (r2.isVideo() == false) goto L436;
     */
    /* JADX WARN: Code restructure failed: missing block: B:409:0x0998, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L435;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0099, code lost:
        if (r12.getBoolean("EnablePreviewGroup", true) != false) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x09a2, code lost:
        if (android.text.TextUtils.isEmpty(r2.messageOwner.message) != false) goto L435;
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x09a4, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "📹 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x09ca, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.R.string.NotificationActionPinnedVideoChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:415:0x09e0, code lost:
        if (r2.isGif() == false) goto L444;
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x09e6, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L443;
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x09f0, code lost:
        if (android.text.TextUtils.isEmpty(r2.messageOwner.message) != false) goto L443;
     */
    /* JADX WARN: Code restructure failed: missing block: B:420:0x09f2, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "🎬 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x0a18, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.R.string.NotificationActionPinnedGifChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x0a30, code lost:
        if (r2.isVoice() == false) goto L447;
     */
    /* JADX WARN: Code restructure failed: missing block: B:424:0x0a32, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.R.string.NotificationActionPinnedVoiceChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:426:0x0a46, code lost:
        if (r2.isRoundVideo() == false) goto L450;
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x0a48, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.R.string.NotificationActionPinnedRoundChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x0a5c, code lost:
        if (r2.isSticker() != false) goto L500;
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x0a62, code lost:
        if (r2.isAnimatedSticker() == false) goto L454;
     */
    /* JADX WARN: Code restructure failed: missing block: B:433:0x0a66, code lost:
        r1 = r2.messageOwner;
        r3 = r1.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:434:0x0a6c, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L462;
     */
    /* JADX WARN: Code restructure failed: missing block: B:436:0x0a72, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L461;
     */
    /* JADX WARN: Code restructure failed: missing block: B:438:0x0a7a, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L461;
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x0a7c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "📎 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:440:0x0aa2, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.R.string.NotificationActionPinnedFileChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:442:0x0ab6, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L499;
     */
    /* JADX WARN: Code restructure failed: missing block: B:444:0x0aba, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L466;
     */
    /* JADX WARN: Code restructure failed: missing block: B:447:0x0ac0, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L469;
     */
    /* JADX WARN: Code restructure failed: missing block: B:448:0x0ac2, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:450:0x0ad6, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L472;
     */
    /* JADX WARN: Code restructure failed: missing block: B:451:0x0ad8, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r29.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.R.string.NotificationActionPinnedContactChannel2, r5.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:453:0x0afd, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L478;
     */
    /* JADX WARN: Code restructure failed: missing block: B:454:0x0aff, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r3).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:455:0x0b05, code lost:
        if (r0.quiz == false) goto L477;
     */
    /* JADX WARN: Code restructure failed: missing block: B:456:0x0b07, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.R.string.NotificationActionPinnedQuizChannel2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:0x0b1e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.R.string.NotificationActionPinnedPollChannel2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x0b37, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L486;
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x0b3d, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L485;
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0b45, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L485;
     */
    /* JADX WARN: Code restructure failed: missing block: B:464:0x0b47, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "🖼 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x0b6d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.R.string.NotificationActionPinnedPhotoChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x0b83, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L489;
     */
    /* JADX WARN: Code restructure failed: missing block: B:468:0x0b85, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.R.string.NotificationActionPinnedGameChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x0b95, code lost:
        r0 = r2.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00a7, code lost:
        if (r12.getBoolean("EnablePreviewChannel", r3) == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:470:0x0b97, code lost:
        if (r0 == null) goto L498;
     */
    /* JADX WARN: Code restructure failed: missing block: B:472:0x0b9d, code lost:
        if (r0.length() <= 0) goto L498;
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x0b9f, code lost:
        r0 = r2.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:474:0x0ba5, code lost:
        if (r0.length() <= 20) goto L497;
     */
    /* JADX WARN: Code restructure failed: missing block: B:475:0x0ba7, code lost:
        r1 = new java.lang.StringBuilder();
        r3 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:476:0x0bbe, code lost:
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x0bbf, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextChannel;
        r2 = new java.lang.Object[2];
        r2[r3] = r5.title;
        r2[1] = r0;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:478:0x0bd1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x0be3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:480:0x0bf5, code lost:
        r0 = r2.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x0bfa, code lost:
        if (r0 == null) goto L503;
     */
    /* JADX WARN: Code restructure failed: missing block: B:482:0x0bfc, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiChannel, r5.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0c10, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:484:0x0c21, code lost:
        r6 = r29.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0c24, code lost:
        if (r6 != null) goto L508;
     */
    /* JADX WARN: Code restructure failed: missing block: B:488:0x0c40, code lost:
        if (r6.isMusic() == false) goto L511;
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x0c42, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.R.string.NotificationActionPinnedMusic, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x0c5a, code lost:
        if (r6.isVideo() == false) goto L519;
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x0c60, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L518;
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:0x0c6a, code lost:
        if (android.text.TextUtils.isEmpty(r6.messageOwner.message) != false) goto L518;
     */
    /* JADX WARN: Code restructure failed: missing block: B:496:0x0c6c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "📹 " + r6.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x0c95, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.R.string.NotificationActionPinnedVideo, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:499:0x0cae, code lost:
        if (r6.isGif() == false) goto L527;
     */
    /* JADX WARN: Code restructure failed: missing block: B:501:0x0cb4, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L526;
     */
    /* JADX WARN: Code restructure failed: missing block: B:503:0x0cbe, code lost:
        if (android.text.TextUtils.isEmpty(r6.messageOwner.message) != false) goto L526;
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0cc0, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "🎬 " + r6.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:505:0x0ce9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.R.string.NotificationActionPinnedGif, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:507:0x0d05, code lost:
        if (r6.isVoice() == false) goto L530;
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x0d07, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.R.string.NotificationActionPinnedVoice, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x0d1d, code lost:
        if (r6.isRoundVideo() == false) goto L533;
     */
    /* JADX WARN: Code restructure failed: missing block: B:511:0x0d1f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.R.string.NotificationActionPinnedRound, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x0d35, code lost:
        if (r6.isSticker() != false) goto L583;
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x0d3b, code lost:
        if (r6.isAnimatedSticker() == false) goto L537;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0d3f, code lost:
        r1 = r6.messageOwner;
        r3 = r1.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x0d45, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L545;
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x0d4b, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L544;
     */
    /* JADX WARN: Code restructure failed: missing block: B:522:0x0d53, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L544;
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x0d55, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "📎 " + r6.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:524:0x0d7e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.R.string.NotificationActionPinnedFile, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:526:0x0d95, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L582;
     */
    /* JADX WARN: Code restructure failed: missing block: B:528:0x0d99, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L549;
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x0d9f, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L552;
     */
    /* JADX WARN: Code restructure failed: missing block: B:532:0x0da1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.R.string.NotificationActionPinnedGeoLive, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:534:0x0db8, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L555;
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0dba, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r29.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.R.string.NotificationActionPinnedContact2, r2, r5.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0de2, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L561;
     */
    /* JADX WARN: Code restructure failed: missing block: B:538:0x0de4, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r3).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0dea, code lost:
        if (r0.quiz == false) goto L560;
     */
    /* JADX WARN: Code restructure failed: missing block: B:540:0x0dec, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.R.string.NotificationActionPinnedQuiz2, r2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x0e06, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.R.string.NotificationActionPinnedPoll2, r2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:543:0x0e22, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L569;
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x0e28, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L568;
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0e30, code lost:
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L568;
     */
    /* JADX WARN: Code restructure failed: missing block: B:548:0x0e32, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r2, "🖼 " + r6.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0e5b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.R.string.NotificationActionPinnedPhoto, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:551:0x0e75, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L572;
     */
    /* JADX WARN: Code restructure failed: missing block: B:552:0x0e77, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.R.string.NotificationActionPinnedGame, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:553:0x0e89, code lost:
        r0 = r6.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:554:0x0e8b, code lost:
        if (r0 == null) goto L581;
     */
    /* JADX WARN: Code restructure failed: missing block: B:556:0x0e91, code lost:
        if (r0.length() <= 0) goto L581;
     */
    /* JADX WARN: Code restructure failed: missing block: B:557:0x0e93, code lost:
        r0 = r6.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:558:0x0e99, code lost:
        if (r0.length() <= 20) goto L580;
     */
    /* JADX WARN: Code restructure failed: missing block: B:559:0x0e9b, code lost:
        r1 = new java.lang.StringBuilder();
        r3 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:560:0x0eb2, code lost:
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0eb3, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedText;
        r4 = new java.lang.Object[3];
        r4[r3] = r2;
        r4[1] = r0;
        r4[2] = r5.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", r1, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:562:0x0ec8, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:563:0x0edd, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.R.string.NotificationActionPinnedGeo, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:564:0x0ef2, code lost:
        r0 = r6.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:565:0x0ef8, code lost:
        if (r0 == null) goto L586;
     */
    /* JADX WARN: Code restructure failed: missing block: B:566:0x0efa, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmoji, r2, r5.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:567:0x0f10, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.R.string.NotificationActionPinnedSticker, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x0f25, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGameScore) == false) goto L591;
     */
    /* JADX WARN: Code restructure failed: missing block: B:572:0x0f31, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) == false) goto L603;
     */
    /* JADX WARN: Code restructure failed: missing block: B:573:0x0f33, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) r7).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:574:0x0f3b, code lost:
        if (android.text.TextUtils.isEmpty(r0) == false) goto L599;
     */
    /* JADX WARN: Code restructure failed: missing block: B:576:0x0f3f, code lost:
        if (r3 != r19) goto L598;
     */
    /* JADX WARN: Code restructure failed: missing block: B:577:0x0f41, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:578:0x0f4e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.R.string.ChatThemeDisabled, r2, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:580:0x0f65, code lost:
        if (r3 != r19) goto L602;
     */
    /* JADX WARN: Code restructure failed: missing block: B:581:0x0f67, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeYou", org.telegram.messenger.R.string.ChatThemeChangedYou, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:582:0x0f75, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.R.string.ChatThemeChangedTo, r2, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:584:0x0f88, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest) == false) goto L148;
     */
    /* JADX WARN: Code restructure failed: missing block: B:587:0x0f9a, code lost:
        if (r6.peer_id.channel_id == 0) goto L617;
     */
    /* JADX WARN: Code restructure failed: missing block: B:589:0x0f9e, code lost:
        if (r5.megagroup != false) goto L617;
     */
    /* JADX WARN: Code restructure failed: missing block: B:591:0x0fa4, code lost:
        if (r29.isVideoAvatar() == false) goto L615;
     */
    /* JADX WARN: Code restructure failed: missing block: B:595:0x0fcf, code lost:
        if (r29.isVideoAvatar() == false) goto L621;
     */
    /* JADX WARN: Code restructure failed: missing block: B:599:0x0ffd, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r5) == false) goto L728;
     */
    /* JADX WARN: Code restructure failed: missing block: B:601:0x1001, code lost:
        if (r5.megagroup != false) goto L728;
     */
    /* JADX WARN: Code restructure failed: missing block: B:603:0x1007, code lost:
        if (r29.isMediaEmpty() == false) goto L636;
     */
    /* JADX WARN: Code restructure failed: missing block: B:604:0x1009, code lost:
        if (r30 != false) goto L634;
     */
    /* JADX WARN: Code restructure failed: missing block: B:606:0x1013, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L634;
     */
    /* JADX WARN: Code restructure failed: missing block: B:607:0x1015, code lost:
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r2, r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:609:0x103c, code lost:
        r4 = r29.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:610:0x1044, code lost:
        if ((r4.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L647;
     */
    /* JADX WARN: Code restructure failed: missing block: B:611:0x1046, code lost:
        if (r30 != false) goto L645;
     */
    /* JADX WARN: Code restructure failed: missing block: B:613:0x104c, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L645;
     */
    /* JADX WARN: Code restructure failed: missing block: B:615:0x1054, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L645;
     */
    /* JADX WARN: Code restructure failed: missing block: B:616:0x1056, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r2, "🖼 " + r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:619:0x1090, code lost:
        if (r29.isVideo() == false) goto L658;
     */
    /* JADX WARN: Code restructure failed: missing block: B:620:0x1092, code lost:
        if (r30 != false) goto L656;
     */
    /* JADX WARN: Code restructure failed: missing block: B:622:0x1098, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L656;
     */
    /* JADX WARN: Code restructure failed: missing block: B:624:0x10a2, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L656;
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x10a4, code lost:
        r3 = org.telegram.messenger.R.string.NotificationMessageText;
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r3, r2, "📹 " + r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:628:0x10e0, code lost:
        if (r29.isVoice() == false) goto L662;
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:0x10f4, code lost:
        if (r29.isRoundVideo() == false) goto L666;
     */
    /* JADX WARN: Code restructure failed: missing block: B:634:0x1108, code lost:
        if (r29.isMusic() == false) goto L670;
     */
    /* JADX WARN: Code restructure failed: missing block: B:636:0x1118, code lost:
        r1 = r29.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:637:0x111e, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L674;
     */
    /* JADX WARN: Code restructure failed: missing block: B:638:0x1120, code lost:
        r1 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:640:0x113e, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L680;
     */
    /* JADX WARN: Code restructure failed: missing block: B:641:0x1140, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r1).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:642:0x1146, code lost:
        if (r0.quiz == false) goto L679;
     */
    /* JADX WARN: Code restructure failed: missing block: B:643:0x1148, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageQuiz2", org.telegram.messenger.R.string.ChannelMessageQuiz2, r2, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:644:0x115d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessagePoll2", org.telegram.messenger.R.string.ChannelMessagePoll2, r2, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:646:0x1174, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L726;
     */
    /* JADX WARN: Code restructure failed: missing block: B:648:0x1178, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L684;
     */
    /* JADX WARN: Code restructure failed: missing block: B:651:0x117e, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L688;
     */
    /* JADX WARN: Code restructure failed: missing block: B:654:0x1192, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L718;
     */
    /* JADX WARN: Code restructure failed: missing block: B:656:0x1198, code lost:
        if (r29.isSticker() != false) goto L714;
     */
    /* JADX WARN: Code restructure failed: missing block: B:658:0x119e, code lost:
        if (r29.isAnimatedSticker() == false) goto L694;
     */
    /* JADX WARN: Code restructure failed: missing block: B:661:0x11a6, code lost:
        if (r29.isGif() == false) goto L705;
     */
    /* JADX WARN: Code restructure failed: missing block: B:662:0x11a8, code lost:
        if (r30 != false) goto L703;
     */
    /* JADX WARN: Code restructure failed: missing block: B:664:0x11ae, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L703;
     */
    /* JADX WARN: Code restructure failed: missing block: B:666:0x11b8, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L703;
     */
    /* JADX WARN: Code restructure failed: missing block: B:667:0x11ba, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r2, "🎬 " + r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:669:0x11f0, code lost:
        if (r30 != false) goto L712;
     */
    /* JADX WARN: Code restructure failed: missing block: B:671:0x11f6, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L712;
     */
    /* JADX WARN: Code restructure failed: missing block: B:673:0x1200, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L712;
     */
    /* JADX WARN: Code restructure failed: missing block: B:674:0x1202, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r2, "📎 " + r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:676:0x1238, code lost:
        r0 = r29.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:677:0x123e, code lost:
        if (r0 == null) goto L717;
     */
    /* JADX WARN: Code restructure failed: missing block: B:678:0x1240, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageStickerEmoji", org.telegram.messenger.R.string.ChannelMessageStickerEmoji, r2, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:679:0x1251, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageSticker", org.telegram.messenger.R.string.ChannelMessageSticker, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:681:0x1260, code lost:
        if (r30 != false) goto L724;
     */
    /* JADX WARN: Code restructure failed: missing block: B:683:0x1268, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageText) != false) goto L724;
     */
    /* JADX WARN: Code restructure failed: missing block: B:684:0x126a, code lost:
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r2, r29.messageText);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:688:0x12a1, code lost:
        if (r29.isMediaEmpty() == false) goto L737;
     */
    /* JADX WARN: Code restructure failed: missing block: B:689:0x12a3, code lost:
        if (r30 != false) goto L735;
     */
    /* JADX WARN: Code restructure failed: missing block: B:691:0x12ad, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L735;
     */
    /* JADX WARN: Code restructure failed: missing block: B:694:0x12de, code lost:
        r3 = r29.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:695:0x12e6, code lost:
        if ((r3.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L748;
     */
    /* JADX WARN: Code restructure failed: missing block: B:696:0x12e8, code lost:
        if (r30 != false) goto L746;
     */
    /* JADX WARN: Code restructure failed: missing block: B:698:0x12ee, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L746;
     */
    /* JADX WARN: Code restructure failed: missing block: B:700:0x12f6, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L746;
     */
    /* JADX WARN: Code restructure failed: missing block: B:701:0x12f8, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:704:0x133a, code lost:
        if (r29.isVideo() == false) goto L759;
     */
    /* JADX WARN: Code restructure failed: missing block: B:705:0x133c, code lost:
        if (r30 != false) goto L757;
     */
    /* JADX WARN: Code restructure failed: missing block: B:707:0x1342, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L757;
     */
    /* JADX WARN: Code restructure failed: missing block: B:709:0x134c, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L757;
     */
    /* JADX WARN: Code restructure failed: missing block: B:710:0x134e, code lost:
        r3 = org.telegram.messenger.R.string.NotificationMessageGroupText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:713:0x1393, code lost:
        if (r29.isVoice() == false) goto L763;
     */
    /* JADX WARN: Code restructure failed: missing block: B:716:0x13ab, code lost:
        if (r29.isRoundVideo() == false) goto L767;
     */
    /* JADX WARN: Code restructure failed: missing block: B:719:0x13c3, code lost:
        if (r29.isMusic() == false) goto L771;
     */
    /* JADX WARN: Code restructure failed: missing block: B:721:0x13d7, code lost:
        r1 = r29.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:722:0x13dd, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L775;
     */
    /* JADX WARN: Code restructure failed: missing block: B:723:0x13df, code lost:
        r1 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:725:0x1403, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L781;
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x1405, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r1).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:727:0x140b, code lost:
        if (r0.quiz == false) goto L780;
     */
    /* JADX WARN: Code restructure failed: missing block: B:728:0x140d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupQuiz2", org.telegram.messenger.R.string.NotificationMessageGroupQuiz2, r2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:729:0x1427, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPoll2", org.telegram.messenger.R.string.NotificationMessageGroupPoll2, r2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:731:0x1443, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L785;
     */
    /* JADX WARN: Code restructure failed: missing block: B:734:0x1463, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L830;
     */
    /* JADX WARN: Code restructure failed: missing block: B:736:0x1467, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L789;
     */
    /* JADX WARN: Code restructure failed: missing block: B:739:0x146d, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L793;
     */
    /* JADX WARN: Code restructure failed: missing block: B:742:0x1486, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L823;
     */
    /* JADX WARN: Code restructure failed: missing block: B:744:0x148c, code lost:
        if (r29.isSticker() != false) goto L819;
     */
    /* JADX WARN: Code restructure failed: missing block: B:746:0x1492, code lost:
        if (r29.isAnimatedSticker() == false) goto L799;
     */
    /* JADX WARN: Code restructure failed: missing block: B:749:0x149a, code lost:
        if (r29.isGif() == false) goto L810;
     */
    /* JADX WARN: Code restructure failed: missing block: B:750:0x149c, code lost:
        if (r30 != false) goto L808;
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x14a2, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L808;
     */
    /* JADX WARN: Code restructure failed: missing block: B:754:0x14ac, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L808;
     */
    /* JADX WARN: Code restructure failed: missing block: B:755:0x14ae, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:757:0x14ec, code lost:
        if (r30 != false) goto L817;
     */
    /* JADX WARN: Code restructure failed: missing block: B:759:0x14f2, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L817;
     */
    /* JADX WARN: Code restructure failed: missing block: B:761:0x14fc, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L817;
     */
    /* JADX WARN: Code restructure failed: missing block: B:762:0x14fe, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:764:0x153c, code lost:
        r0 = r29.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:765:0x1542, code lost:
        if (r0 == null) goto L822;
     */
    /* JADX WARN: Code restructure failed: missing block: B:766:0x1544, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupStickerEmoji", org.telegram.messenger.R.string.NotificationMessageGroupStickerEmoji, r2, r5.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:767:0x155a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupSticker", org.telegram.messenger.R.string.NotificationMessageGroupSticker, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:768:0x156d, code lost:
        if (r30 != false) goto L828;
     */
    /* JADX WARN: Code restructure failed: missing block: B:770:0x1575, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageText) != false) goto L828;
     */
    /* JADX WARN: Code restructure failed: missing block: B:831:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.R.string.NotificationGroupCreatedCall, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:832:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:833:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.R.string.NotificationInvitedToGroupByLink, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:834:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.R.string.NotificationEditedGroupName, r2, r7.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:835:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.R.string.NotificationGroupKickYou, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:836:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.R.string.NotificationGroupLeftMember, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:837:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.R.string.NotificationGroupKickMember, r2, r5.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:838:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:839:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:840:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:841:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r7.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:842:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:843:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:844:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:845:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:846:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:847:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.R.string.ChannelVideoEditNotification, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:848:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.R.string.ChannelPhotoEditNotification, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:849:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.R.string.NotificationEditedGroupVideo, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:850:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.R.string.NotificationEditedGroupPhoto, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:851:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:852:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.R.string.ChannelMessageNoText, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:853:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:854:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessagePhoto", org.telegram.messenger.R.string.ChannelMessagePhoto, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:855:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:856:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageVideo", org.telegram.messenger.R.string.ChannelMessageVideo, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:857:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageAudio", org.telegram.messenger.R.string.ChannelMessageAudio, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:858:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageRound", org.telegram.messenger.R.string.ChannelMessageRound, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:859:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageMusic", org.telegram.messenger.R.string.ChannelMessageMusic, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:860:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageContact2", org.telegram.messenger.R.string.ChannelMessageContact2, r2, org.telegram.messenger.ContactsController.formatName(r1.first_name, r1.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:861:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageLiveLocation", org.telegram.messenger.R.string.ChannelMessageLiveLocation, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:862:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:863:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageGIF", org.telegram.messenger.R.string.ChannelMessageGIF, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:864:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:865:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageDocument", org.telegram.messenger.R.string.ChannelMessageDocument, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:866:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:867:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.R.string.ChannelMessageNoText, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:868:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageMap", org.telegram.messenger.R.string.ChannelMessageMap, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:869:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r2, r5.title, r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:870:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupNoText", org.telegram.messenger.R.string.NotificationMessageGroupNoText, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:871:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r2, r5.title, "🖼 " + r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:872:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPhoto", org.telegram.messenger.R.string.NotificationMessageGroupPhoto, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:873:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r3, r2, r5.title, "📹 " + r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:874:?, code lost:
        return org.telegram.messenger.LocaleController.formatString(" ", org.telegram.messenger.R.string.NotificationMessageGroupVideo, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:875:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupAudio", org.telegram.messenger.R.string.NotificationMessageGroupAudio, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:876:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupRound", org.telegram.messenger.R.string.NotificationMessageGroupRound, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:877:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMusic", org.telegram.messenger.R.string.NotificationMessageGroupMusic, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:878:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupContact2", org.telegram.messenger.R.string.NotificationMessageGroupContact2, r2, r5.title, org.telegram.messenger.ContactsController.formatName(r1.first_name, r1.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:879:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGame", org.telegram.messenger.R.string.NotificationMessageGroupGame, r2, r5.title, r1.game.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:880:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupLiveLocation", org.telegram.messenger.R.string.NotificationMessageGroupLiveLocation, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:881:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r2, r5.title, "🎬 " + r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:882:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGif", org.telegram.messenger.R.string.NotificationMessageGroupGif, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:883:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r2, r5.title, "📎 " + r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:884:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupDocument", org.telegram.messenger.R.string.NotificationMessageGroupDocument, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:885:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r2, r5.title, r29.messageText);
     */
    /* JADX WARN: Code restructure failed: missing block: B:886:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupNoText", org.telegram.messenger.R.string.NotificationMessageGroupNoText, r2, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:887:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMap", org.telegram.messenger.R.string.NotificationMessageGroupMap, r2, r5.title);
     */
    /* JADX WARN: Removed duplicated region for block: B:271:0x0627  */
    /* JADX WARN: Removed duplicated region for block: B:776:0x15bb  */
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
    public /* synthetic */ void lambda$showNotifications$31() {
        showOrUpdateNotification(false);
    }

    public void showNotifications() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$showNotifications$31();
            }
        });
    }

    public void hideNotifications() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$hideNotifications$32();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideNotifications$32() {
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
            AndroidUtilities.runOnUIThread(NotificationsController$$ExternalSyntheticLambda45.INSTANCE);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dismissNotification$33() {
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
            notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$playInChatSound$35();
                }
            });
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playInChatSound$35() {
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
    public static /* synthetic */ void lambda$playInChatSound$34(SoundPool soundPool, int i, int i2) {
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$repeatNotificationMaybe$36();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$repeatNotificationMaybe$36() {
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
    public void lambda$deleteNotificationChannel$37(long j, int i, int i2) {
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$deleteNotificationChannel$37(j, i, i2);
            }
        });
    }

    public void deleteNotificationChannelGlobal(int i) {
        deleteNotificationChannelGlobal(i, -1);
    }

    /* renamed from: deleteNotificationChannelGlobalInternal */
    public void lambda$deleteNotificationChannelGlobal$38(int i, int i2) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        try {
            SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
            SharedPreferences.Editor edit = notificationsSettings.edit();
            if (i2 == 0 || i2 == -1) {
                String str = i == 2 ? "channels" : i == 0 ? "groups" : i == 3 ? "stories" : "private";
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
                String str2 = i == 2 ? "channels_ia" : i == 0 ? "groups_ia" : i == 3 ? "stories_ia" : "private_ia";
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
            edit.remove(i == 2 ? "overwrite_channel" : i == 0 ? "overwrite_group" : i == 3 ? "overwrite_stories" : "overwrite_private");
            edit.commit();
        } catch (Exception e3) {
            FileLog.e(e3);
        }
    }

    public void deleteNotificationChannelGlobal(final int i, final int i2) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$deleteNotificationChannelGlobal$38(i, i2);
            }
        });
    }

    public void deleteAllNotificationChannels() {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$deleteAllNotificationChannels$39();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteAllNotificationChannels$39() {
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
        String str5 = "stories" + this.currentAccount;
        String str6 = "private" + this.currentAccount;
        for (int i2 = 0; i2 < size2; i2++) {
            String id2 = notificationChannelGroups.get(i2).getId();
            if (str2 != null && str2.equals(id2)) {
                str2 = null;
            } else if (str3 != null && str3.equals(id2)) {
                str3 = null;
            } else if (str5 != null && str5.equals(id2)) {
                str5 = null;
            } else if (str6 != null && str6.equals(id2)) {
                str6 = null;
            } else if (str4 != null && str4.equals(id2)) {
                str4 = null;
            }
            if (str2 == null && str5 == null && str3 == null && str6 == null && str4 == null) {
                break;
            }
        }
        if (str2 != null || str3 != null || str5 != null || str6 != null || str4 != null) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(getUserConfig().getClientUserId()));
            if (user == null) {
                getUserConfig().getCurrentUser();
            }
            String str7 = user != null ? " (" + ContactsController.formatName(user.first_name, user.last_name) + ")" : "";
            ArrayList arrayList = new ArrayList();
            if (str2 != null) {
                arrayList.add(new NotificationChannelGroup(str2, LocaleController.getString("NotificationsChannels", R.string.NotificationsChannels) + str7));
            }
            if (str3 != null) {
                arrayList.add(new NotificationChannelGroup(str3, LocaleController.getString("NotificationsGroups", R.string.NotificationsGroups) + str7));
            }
            if (str5 != null) {
                arrayList.add(new NotificationChannelGroup(str5, LocaleController.getString("NotificationsStories", R.string.NotificationsStories) + str7));
            }
            if (str6 != null) {
                arrayList.add(new NotificationChannelGroup(str6, LocaleController.getString("NotificationsPrivateChats", R.string.NotificationsPrivateChats) + str7));
            }
            if (str4 != null) {
                arrayList.add(new NotificationChannelGroup(str4, LocaleController.getString("NotificationsOther", R.string.NotificationsOther) + str7));
            }
            systemNotificationManager.createNotificationChannelGroups(arrayList);
        }
        this.channelGroupsCreated = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:210:0x047f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:216:0x04c2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:224:0x04d1 A[LOOP:1: B:222:0x04ce->B:224:0x04d1, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:227:0x04e2  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x04ee A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:234:0x04fd A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0530  */
    @TargetApi(26)
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String validateChannelId(long j, int i, String str, long[] jArr, int i2, Uri uri, int i3, boolean z, boolean z2, boolean z3, int i4) {
        String str2;
        String str3;
        String formatString;
        String str4;
        int i5;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        boolean z4;
        int i6;
        String str10;
        String str11;
        SharedPreferences sharedPreferences;
        long[] jArr2;
        String str12;
        boolean z5;
        String str13;
        String str14;
        Uri uri2;
        String MD5;
        String str15;
        boolean z6;
        boolean z7;
        boolean z8;
        long[] jArr3;
        String str16;
        String str17;
        String str18;
        long j2;
        long[] jArr4;
        SharedPreferences.Editor editor;
        SharedPreferences.Editor editor2;
        int i7;
        int i8;
        ensureGroupsCreated();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        String str19 = "private";
        if (z3) {
            str2 = "other" + this.currentAccount;
            str3 = null;
        } else if (i4 == 2) {
            str2 = "channels" + this.currentAccount;
            str3 = "overwrite_channel";
        } else if (i4 == 0) {
            str2 = "groups" + this.currentAccount;
            str3 = "overwrite_group";
        } else if (i4 == 3) {
            str2 = "stories" + this.currentAccount;
            str3 = "overwrite_stories";
        } else {
            str2 = "private" + this.currentAccount;
            str3 = "overwrite_private";
        }
        boolean z9 = !z && DialogObject.isEncryptedDialog(j);
        boolean z10 = (z2 || str3 == null || !notificationsSettings.getBoolean(str3, false)) ? false : true;
        String MD52 = Utilities.MD5(uri == null ? "NoSound2" : uri.toString());
        if (MD52 != null && MD52.length() > 5) {
            MD52 = MD52.substring(0, 5);
        }
        if (z3) {
            formatString = LocaleController.getString("NotificationsSilent", R.string.NotificationsSilent);
            str19 = "silent";
        } else if (z) {
            if (z2) {
                str4 = "stories";
                formatString = LocaleController.getString("NotificationsInAppDefault", R.string.NotificationsInAppDefault);
            } else {
                str4 = "stories";
                formatString = LocaleController.getString("NotificationsDefault", R.string.NotificationsDefault);
            }
            if (i4 == 2) {
                str19 = z2 ? "channels_ia" : "channels";
            } else if (i4 == 0) {
                str19 = z2 ? "groups_ia" : "groups";
            } else if (i4 == 3) {
                if (z2) {
                    str4 = "stories_ia";
                }
                str19 = str4;
            } else if (z2) {
                str19 = "private_ia";
            }
        } else {
            formatString = z2 ? LocaleController.formatString("NotificationsChatInApp", R.string.NotificationsChatInApp, str) : str;
            StringBuilder sb = new StringBuilder();
            sb.append(z2 ? "org.telegram.keyia" : "org.telegram.key");
            sb.append(j);
            sb.append("_");
            sb.append(i);
            str19 = sb.toString();
        }
        String str20 = str19 + "_" + MD52;
        String string = notificationsSettings.getString(str20, null);
        String string2 = notificationsSettings.getString(str20 + "_s", null);
        StringBuilder sb2 = new StringBuilder();
        String str21 = formatString;
        String str22 = str2;
        if (string != null) {
            NotificationChannel notificationChannel = systemNotificationManager.getNotificationChannel(string);
            str9 = "_";
            if (BuildVars.LOGS_ENABLED) {
                StringBuilder sb3 = new StringBuilder();
                str8 = "_s";
                sb3.append("current channel for ");
                sb3.append(string);
                sb3.append(" = ");
                sb3.append(notificationChannel);
                FileLog.d(sb3.toString());
            } else {
                str8 = "_s";
            }
            if (notificationChannel == null) {
                i6 = i2;
                i5 = i3;
                str5 = str20;
                z4 = z10;
                jArr2 = jArr;
                sharedPreferences = notificationsSettings;
                str12 = null;
                str11 = null;
                str10 = null;
                z5 = false;
                if (z5) {
                }
                str13 = str8;
                str14 = str5;
                if (!z4) {
                }
                while (r13 < jArr2.length) {
                }
                sb2.append(i6);
                uri2 = uri;
                if (uri2 != null) {
                }
                sb2.append(i5);
                if (!z) {
                    sb2.append("secret");
                }
                MD5 = Utilities.MD5(sb2.toString());
                if (z3) {
                }
                str15 = str11;
                str12 = MD5;
                if (str15 == null) {
                }
                return str15;
            } else if (!z3 && !z10) {
                int importance = notificationChannel.getImportance();
                Uri sound = notificationChannel.getSound();
                long[] vibrationPattern = notificationChannel.getVibrationPattern();
                z4 = z10;
                boolean shouldVibrate = notificationChannel.shouldVibrate();
                if (shouldVibrate || vibrationPattern != null) {
                    str5 = str20;
                    z8 = shouldVibrate;
                    jArr3 = vibrationPattern;
                } else {
                    str5 = str20;
                    z8 = shouldVibrate;
                    jArr3 = new long[]{0, 0};
                }
                int lightColor = notificationChannel.getLightColor();
                if (jArr3 != null) {
                    for (long j3 : jArr3) {
                        sb2.append(j3);
                    }
                }
                sb2.append(lightColor);
                if (sound != null) {
                    sb2.append(sound.toString());
                }
                sb2.append(importance);
                if (!z && z9) {
                    sb2.append("secret");
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("current channel settings for " + string + " = " + ((Object) sb2) + " old = " + string2);
                }
                String MD53 = Utilities.MD5(sb2.toString());
                sb2.setLength(0);
                if (MD53.equals(string2)) {
                    i5 = i3;
                    str16 = MD53;
                    sharedPreferences = notificationsSettings;
                    str17 = string;
                    str18 = string2;
                    jArr3 = jArr;
                    lightColor = i2;
                    z5 = false;
                } else {
                    if (importance == 0) {
                        editor = notificationsSettings.edit();
                        if (z) {
                            if (!z2) {
                                if (i4 == 3) {
                                    editor.putBoolean("EnableAllStories", false);
                                } else {
                                    editor.putInt(getGlobalNotificationsKey(i4), ConnectionsManager.DEFAULT_DATACENTER_ID);
                                }
                                updateServerNotificationsSettings(i4);
                            }
                            sharedPreferences = notificationsSettings;
                            j2 = j;
                        } else {
                            if (i4 == 3) {
                                editor.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + getSharedPrefKey(j, 0), false);
                                sharedPreferences = notificationsSettings;
                                j2 = j;
                                i8 = 0;
                            } else {
                                sharedPreferences = notificationsSettings;
                                j2 = j;
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append(NotificationsSettingsFacade.PROPERTY_NOTIFY);
                                i8 = 0;
                                sb4.append(getSharedPrefKey(j2, 0));
                                editor.putInt(sb4.toString(), 2);
                            }
                            updateServerNotificationsSettings(j2, i8, true);
                        }
                        i5 = i3;
                        str16 = MD53;
                        str17 = string;
                        str18 = string2;
                        z5 = true;
                        jArr4 = jArr;
                    } else {
                        i5 = i3;
                        str16 = MD53;
                        sharedPreferences = notificationsSettings;
                        j2 = j;
                        str17 = string;
                        if (importance != i5) {
                            if (z2) {
                                str18 = string2;
                                editor2 = null;
                            } else {
                                editor2 = sharedPreferences.edit();
                                str18 = string2;
                                int i9 = (importance == 4 || importance == 5) ? 1 : importance == 1 ? 4 : importance == 2 ? 5 : 0;
                                if (z) {
                                    if (i4 == 3) {
                                        editor2.putBoolean("EnableAllStories", true);
                                    } else {
                                        editor2.putInt(getGlobalNotificationsKey(i4), 0);
                                    }
                                    if (i4 == 2) {
                                        editor2.putInt("priority_channel", i9);
                                    } else if (i4 == 0) {
                                        editor2.putInt("priority_group", i9);
                                    } else if (i4 == 3) {
                                        editor2.putInt("priority_stories", i9);
                                    } else {
                                        editor2.putInt("priority_messages", i9);
                                    }
                                } else if (i4 == 3) {
                                    editor2.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + j2, true);
                                } else {
                                    editor2.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + j2, 0);
                                    editor2.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + j2);
                                    editor2.putInt("priority_" + j2, i9);
                                }
                            }
                            jArr4 = jArr;
                            editor = editor2;
                            z5 = true;
                        } else {
                            str18 = string2;
                            jArr4 = jArr;
                            editor = null;
                            z5 = false;
                        }
                    }
                    boolean z11 = z8;
                    if ((!isEmptyVibration(jArr4)) != z11) {
                        if (!z2) {
                            if (editor == null) {
                                editor = sharedPreferences.edit();
                            }
                            if (!z) {
                                editor.putInt("vibrate_" + j2, z11 ? 0 : 2);
                            } else if (i4 == 2) {
                                editor.putInt("vibrate_channel", z11 ? 0 : 2);
                            } else if (i4 == 0) {
                                editor.putInt("vibrate_group", z11 ? 0 : 2);
                            } else if (i4 == 3) {
                                editor.putInt("vibrate_stories", z11 ? 0 : 2);
                            } else {
                                editor.putInt("vibrate_messages", z11 ? 0 : 2);
                            }
                        }
                        i7 = i2;
                        z5 = true;
                    } else {
                        i7 = i2;
                        jArr3 = jArr4;
                    }
                    if (lightColor != i7) {
                        if (!z2) {
                            if (editor == null) {
                                editor = sharedPreferences.edit();
                            }
                            if (!z) {
                                editor.putInt("color_" + j2, lightColor);
                            } else if (i4 == 2) {
                                editor.putInt("ChannelLed", lightColor);
                            } else if (i4 == 0) {
                                editor.putInt("GroupLed", lightColor);
                            } else if (i4 == 3) {
                                editor.putInt("StoriesLed", lightColor);
                            } else {
                                editor.putInt("MessagesLed", lightColor);
                            }
                        }
                        z5 = true;
                    } else {
                        lightColor = i7;
                    }
                    if (editor != null) {
                        editor.commit();
                    }
                }
                i6 = lightColor;
                jArr2 = jArr3;
                str12 = str16;
                str11 = str17;
                str10 = str18;
                if (z5 || str12 == null) {
                    str13 = str8;
                    str14 = str5;
                    if (!z4 || str12 == null || !z2 || !z) {
                        for (long j4 : jArr2) {
                            sb2.append(j4);
                        }
                        sb2.append(i6);
                        uri2 = uri;
                        if (uri2 != null) {
                            sb2.append(uri.toString());
                        }
                        sb2.append(i5);
                        if (!z && z9) {
                            sb2.append("secret");
                        }
                        MD5 = Utilities.MD5(sb2.toString());
                        if (!z3 || str11 == null || (!z4 && str10.equals(MD5))) {
                            str15 = str11;
                            str12 = MD5;
                        } else {
                            try {
                                systemNotificationManager.deleteNotificationChannel(str11);
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("delete channel by settings change " + str11);
                            }
                            str12 = MD5;
                            str15 = null;
                        }
                        if (str15 == null) {
                            str15 = z ? this.currentAccount + "channel_" + str14 + str9 + Utilities.random.nextLong() : this.currentAccount + "channel_" + j + str9 + Utilities.random.nextLong();
                            NotificationChannel notificationChannel2 = new NotificationChannel(str15, z9 ? LocaleController.getString("SecretChatName", R.string.SecretChatName) : str21, i5);
                            notificationChannel2.setGroup(str22);
                            if (i6 != 0) {
                                z6 = true;
                                notificationChannel2.enableLights(true);
                                notificationChannel2.setLightColor(i6);
                                z7 = false;
                            } else {
                                z6 = true;
                                z7 = false;
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
                                notificationChannel2.setSound(null, builder.build());
                            }
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("create new channel " + str15);
                            }
                            this.lastNotificationChannelCreateTime = SystemClock.elapsedRealtime();
                            systemNotificationManager.createNotificationChannel(notificationChannel2);
                            sharedPreferences.edit().putString(str14, str15).putString(str14 + str13, str12).commit();
                        }
                        return str15;
                    }
                } else {
                    str14 = str5;
                    SharedPreferences.Editor putString = sharedPreferences.edit().putString(str14, str11);
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(str14);
                    str13 = str8;
                    sb5.append(str13);
                    putString.putString(sb5.toString(), str12).commit();
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("change edited channel " + str11);
                    }
                }
                uri2 = uri;
                str15 = str11;
                if (str15 == null) {
                }
                return str15;
            } else {
                i5 = i3;
                str5 = str20;
                str6 = string;
                str7 = string2;
            }
        } else {
            i5 = i3;
            str5 = str20;
            str6 = string;
            str7 = string2;
            str8 = "_s";
            str9 = "_";
        }
        z4 = z10;
        i6 = i2;
        jArr2 = jArr;
        sharedPreferences = notificationsSettings;
        str11 = str6;
        str10 = str7;
        str12 = null;
        z5 = false;
        if (z5) {
        }
        str13 = str8;
        str14 = str5;
        if (!z4) {
        }
        while (r13 < jArr2.length) {
        }
        sb2.append(i6);
        uri2 = uri;
        if (uri2 != null) {
        }
        sb2.append(i5);
        if (!z) {
        }
        MD5 = Utilities.MD5(sb2.toString());
        if (z3) {
        }
        str15 = str11;
        str12 = MD5;
        if (str15 == null) {
        }
        return str15;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:409:0x0a42
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    private void showOrUpdateNotification(boolean r50) {
        /*
            Method dump skipped, instructions count: 3529
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.NotificationsController.showOrUpdateNotification(boolean):void");
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
            } else if (i4 == 1) {
                edit.putString("GlobalSound", string);
            } else if (i4 == 3) {
                edit.putString("StoriesSound", string);
            }
            if (i4 == 2) {
                edit.putString("ChannelSoundPath", uri3);
            } else if (i4 == 0) {
                edit.putString("GroupSoundPath", uri3);
            } else if (i4 == 1) {
                edit.putString("GlobalSoundPath", uri3);
            } else if (i4 == 3) {
                edit.putString("StoriesSoundPath", uri3);
            }
            getNotificationsController().lambda$deleteNotificationChannelGlobal$38(i4, -1);
        } else {
            edit.putString("sound_" + getSharedPrefKey(j, i), string);
            edit.putString("sound_path_" + getSharedPrefKey(j, i), uri3);
            lambda$deleteNotificationChannel$37(j, i, -1);
        }
        edit.commit();
        builder.setChannelId(validateChannelId(j, i, str, jArr, i2, Settings.System.DEFAULT_RINGTONE_URI, i3, z, z2, z3, i4));
        notificationManager.notify(this.notificationId, builder.build());
    }

    /* JADX WARN: Can't wrap try/catch for region: R(84:55|(2:57|(3:59|60|61)(4:62|(2:65|63)|66|67))(1:660)|68|(1:70)(1:(1:658)(1:659))|71|72|(4:75|(2:77|78)(1:80)|79|73)|81|82|(4:84|(2:(1:87)(1:572)|88)(1:573)|(1:571)(2:94|(67:98|99|100|(3:102|(1:104)(1:567)|105)(1:568)|(3:107|(3:109|(1:111)(3:553|554|(3:556|(1:558)(1:560)|559)(1:561))|112)(1:565)|564)(1:566)|(3:114|(1:120)|121)(1:552)|122|(3:547|(1:549)(1:551)|550)(1:125)|126|(1:128)|129|(1:131)(1:539)|132|(1:538)(1:136)|137|(3:140|(1:142)|(3:144|145|(48:149|150|151|(4:155|156|157|158)|(1:531)(1:166)|167|(1:530)(1:170)|171|(1:529)|178|(1:528)(1:185)|186|(12:188|(1:190)(5:319|(2:321|(1:(1:324)(1:325))(10:326|(1:328)(2:329|(1:334)(1:333))|192|(2:195|193)|196|197|(1:318)(1:200)|201|(1:203)(1:317)|204))|335|336|61)|191|192|(1:193)|196|197|(0)|318|201|(0)(0)|204)(4:337|(6:339|(1:341)(4:346|(1:348)(2:521|(2:525|(3:351|(1:353)|354)(22:355|(1:357)|358|(2:517|(1:519)(1:520))(1:364)|365|(1:367)(13:(1:512)(2:514|(1:516))|513|369|(2:(2:372|(2:(2:375|(1:377))(1:505)|378)(2:506|(1:508)))(1:509)|504)(1:510)|379|(3:475|(1:503)(3:481|(1:502)(3:484|(1:488)|(1:501)(1:498))|499)|500)(1:383)|384|(6:386|(1:473)(7:399|(1:472)(3:403|(9:454|455|456|457|458|459|460|461|462)(1:405)|406)|407|(1:409)(1:453)|410|(3:447|448|449)(3:412|(1:414)|446)|(6:416|(1:418)|419|(1:421)|422|(2:427|(3:429|(2:434|435)(1:431)|(1:433))))(1:444))|445|(0)|422|(3:425|427|(0)))(1:474)|438|(3:442|443|345)|343|344|345)|368|369|(0)(0)|379|(1:381)|475|(1:477)|503|500|384|(0)(0)|438|(4:440|442|443|345)|343|344|345)))|349|(0)(0))|342|343|344|345)|526|527)|205|(4:207|(2:210|208)|211|212)(2:310|(1:312)(2:313|(1:315)(1:316)))|213|(1:215)|216|(1:218)|219|(2:221|(1:223)(1:305))(2:306|(1:308)(1:309))|(1:225)(1:304)|226|(4:228|(2:231|229)|232|233)(1:303)|234|(1:236)|237|238|239|(1:241)|242|(1:244)|(1:246)|(1:250)|251|(1:299)(1:257)|258|(1:260)|(1:262)|263|(3:268|(4:270|(3:272|(4:274|(1:276)|277|278)(2:280|281)|279)|282|283)|284)|285|(1:298)(2:288|(1:292))|293|(1:295)|296|297|61)))|537|(1:164)|531|167|(0)|530|171|(1:173)|529|178|(1:181)|528|186|(0)(0)|205|(0)(0)|213|(0)|216|(0)|219|(0)(0)|(0)(0)|226|(0)(0)|234|(0)|237|238|239|(0)|242|(0)|(0)|(2:248|250)|251|(1:253)|299|258|(0)|(0)|263|(4:265|268|(0)|284)|285|(0)|298|293|(0)|296|297|61))|569)(5:574|(5:576|(1:578)(1:642)|579|(79:581|(2:583|(1:585)(2:597|(1:599)))(2:600|(78:604|(76:608|588|(1:590)(2:593|(1:595)(73:596|592|100|(0)(0)|(0)(0)|(0)(0)|122|(0)|541|543|545|547|(0)(0)|550|126|(0)|129|(0)(0)|132|(1:134)|538|137|(3:140|(0)|(0))|537|(0)|531|167|(0)|530|171|(0)|529|178|(0)|528|186|(0)(0)|205|(0)(0)|213|(0)|216|(0)|219|(0)(0)|(0)(0)|226|(0)(0)|234|(0)|237|238|239|(0)|242|(0)|(0)|(0)|251|(0)|299|258|(0)|(0)|263|(0)|285|(0)|298|293|(0)|296|297))|591|592|100|(0)(0)|(0)(0)|(0)(0)|122|(0)|541|543|545|547|(0)(0)|550|126|(0)|129|(0)(0)|132|(0)|538|137|(0)|537|(0)|531|167|(0)|530|171|(0)|529|178|(0)|528|186|(0)(0)|205|(0)(0)|213|(0)|216|(0)|219|(0)(0)|(0)(0)|226|(0)(0)|234|(0)|237|238|239|(0)|242|(0)|(0)|(0)|251|(0)|299|258|(0)|(0)|263|(0)|285|(0)|298|293|(0)|296|297)|587|588|(0)(0)|591|592|100|(0)(0)|(0)(0)|(0)(0)|122|(0)|541|543|545|547|(0)(0)|550|126|(0)|129|(0)(0)|132|(0)|538|137|(0)|537|(0)|531|167|(0)|530|171|(0)|529|178|(0)|528|186|(0)(0)|205|(0)(0)|213|(0)|216|(0)|219|(0)(0)|(0)(0)|226|(0)(0)|234|(0)|237|238|239|(0)|242|(0)|(0)|(0)|251|(0)|299|258|(0)|(0)|263|(0)|285|(0)|298|293|(0)|296|297))|586|587|588|(0)(0)|591|592|100|(0)(0)|(0)(0)|(0)(0)|122|(0)|541|543|545|547|(0)(0)|550|126|(0)|129|(0)(0)|132|(0)|538|137|(0)|537|(0)|531|167|(0)|530|171|(0)|529|178|(0)|528|186|(0)(0)|205|(0)(0)|213|(0)|216|(0)|219|(0)(0)|(0)(0)|226|(0)(0)|234|(0)|237|238|239|(0)|242|(0)|(0)|(0)|251|(0)|299|258|(0)|(0)|263|(0)|285|(0)|298|293|(0)|296|297)(74:609|(2:611|(1:613)(2:615|(1:617)))(8:618|(1:641)(1:622)|623|(1:640)(2:627|(1:629))|639|(2:632|(1:634))(1:638)|(1:636)|637)|614|100|(0)(0)|(0)(0)|(0)(0)|122|(0)|541|543|545|547|(0)(0)|550|126|(0)|129|(0)(0)|132|(0)|538|137|(0)|537|(0)|531|167|(0)|530|171|(0)|529|178|(0)|528|186|(0)(0)|205|(0)(0)|213|(0)|216|(0)|219|(0)(0)|(0)(0)|226|(0)(0)|234|(0)|237|238|239|(0)|242|(0)|(0)|(0)|251|(0)|299|258|(0)|(0)|263|(0)|285|(0)|298|293|(0)|296|297)|61)(3:643|(2:645|(2:647|(1:649))(2:650|(2:652|(1:654))))(1:656)|655)|335|336|61)|570|99|100|(0)(0)|(0)(0)|(0)(0)|122|(0)|541|543|545|547|(0)(0)|550|126|(0)|129|(0)(0)|132|(0)|538|137|(0)|537|(0)|531|167|(0)|530|171|(0)|529|178|(0)|528|186|(0)(0)|205|(0)(0)|213|(0)|216|(0)|219|(0)(0)|(0)(0)|226|(0)(0)|234|(0)|237|238|239|(0)|242|(0)|(0)|(0)|251|(0)|299|258|(0)|(0)|263|(0)|285|(0)|298|293|(0)|296|297|61) */
    /* JADX WARN: Code restructure failed: missing block: B:161:0x03c8, code lost:
        if (r5.local_id != 0) goto L631;
     */
    /* JADX WARN: Code restructure failed: missing block: B:583:0x0f81, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:584:0x0f82, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x02c4  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0336  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0343  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x049d  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x04b7  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x04c1  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0519  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0524  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x054a  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x0555 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:239:0x05b0  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x05c1  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x0603  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x060e  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x0615  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x0624  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x0650 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:262:0x0660  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x06be  */
    /* JADX WARN: Removed duplicated region for block: B:292:0x06d0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:297:0x06e4  */
    /* JADX WARN: Removed duplicated region for block: B:305:0x06f9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:315:0x0725  */
    /* JADX WARN: Removed duplicated region for block: B:339:0x082d A[LOOP:5: B:337:0x0825->B:339:0x082d, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:347:0x0872  */
    /* JADX WARN: Removed duplicated region for block: B:348:0x0878  */
    /* JADX WARN: Removed duplicated region for block: B:350:0x0884  */
    /* JADX WARN: Removed duplicated region for block: B:366:0x08ec  */
    /* JADX WARN: Removed duplicated region for block: B:370:0x0917  */
    /* JADX WARN: Removed duplicated region for block: B:396:0x09a3  */
    /* JADX WARN: Removed duplicated region for block: B:410:0x09e5  */
    /* JADX WARN: Removed duplicated region for block: B:449:0x0a8f  */
    /* JADX WARN: Removed duplicated region for block: B:487:0x0b8c  */
    /* JADX WARN: Removed duplicated region for block: B:488:0x0b8f  */
    /* JADX WARN: Removed duplicated region for block: B:495:0x0bbb  */
    /* JADX WARN: Removed duplicated region for block: B:500:0x0c15  */
    /* JADX WARN: Removed duplicated region for block: B:504:0x0c44  */
    /* JADX WARN: Removed duplicated region for block: B:508:0x0c4d  */
    /* JADX WARN: Removed duplicated region for block: B:516:0x0c6e  */
    /* JADX WARN: Removed duplicated region for block: B:524:0x0cb4  */
    /* JADX WARN: Removed duplicated region for block: B:535:0x0d2e  */
    /* JADX WARN: Removed duplicated region for block: B:540:0x0d55  */
    /* JADX WARN: Removed duplicated region for block: B:549:0x0d97  */
    /* JADX WARN: Removed duplicated region for block: B:552:0x0db6  */
    /* JADX WARN: Removed duplicated region for block: B:555:0x0e16  */
    /* JADX WARN: Removed duplicated region for block: B:559:0x0e53  */
    /* JADX WARN: Removed duplicated region for block: B:564:0x0e7a  */
    /* JADX WARN: Removed duplicated region for block: B:565:0x0e9d  */
    /* JADX WARN: Removed duplicated region for block: B:568:0x0eba  */
    /* JADX WARN: Removed duplicated region for block: B:573:0x0edd  */
    /* JADX WARN: Removed duplicated region for block: B:576:0x0f11  */
    /* JADX WARN: Removed duplicated region for block: B:580:0x0f6a A[Catch: Exception -> 0x0f81, TryCatch #9 {Exception -> 0x0f81, blocks: (B:578:0x0f4c, B:580:0x0f6a, B:581:0x0f71), top: B:697:0x0f4c }] */
    /* JADX WARN: Removed duplicated region for block: B:586:0x0f87  */
    /* JADX WARN: Removed duplicated region for block: B:588:0x0f92  */
    /* JADX WARN: Removed duplicated region for block: B:590:0x0f97  */
    /* JADX WARN: Removed duplicated region for block: B:595:0x0fa5  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0163  */
    /* JADX WARN: Removed duplicated region for block: B:603:0x0fbe  */
    /* JADX WARN: Removed duplicated region for block: B:605:0x0fc3  */
    /* JADX WARN: Removed duplicated region for block: B:608:0x0fcf  */
    /* JADX WARN: Removed duplicated region for block: B:613:0x0fdc  */
    /* JADX WARN: Removed duplicated region for block: B:626:0x1058 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:635:0x108a  */
    /* JADX WARN: Removed duplicated region for block: B:640:0x1112  */
    /* JADX WARN: Removed duplicated region for block: B:647:0x115f  */
    /* JADX WARN: Removed duplicated region for block: B:653:0x1178  */
    /* JADX WARN: Removed duplicated region for block: B:663:0x11c7  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x01db  */
    /* JADX WARN: Removed duplicated region for block: B:687:0x0b98 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:699:0x066a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0219  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0222  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x023a  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x025f  */
    @SuppressLint({"InlinedApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showExtraNotifications(NotificationCompat.Builder builder, String str, long j, int i, String str2, long[] jArr, int i2, Uri uri, int i3, boolean z, boolean z2, boolean z3, int i4) {
        boolean z4;
        long clientUserId;
        boolean z5;
        int i5;
        LongSparseArray longSparseArray;
        int size;
        int i6;
        LongSparseArray longSparseArray2;
        ArrayList arrayList;
        NotificationsController notificationsController;
        int i7;
        int size2;
        int i8;
        LongSparseArray longSparseArray3;
        DialogKey dialogKey;
        int i9;
        int i10;
        ArrayList arrayList2;
        ArrayList<StoryNotification> arrayList3;
        int id;
        ArrayList arrayList4;
        int i11;
        MessageObject messageObject;
        LongSparseArray longSparseArray4;
        long j2;
        boolean z6;
        int i12;
        ArrayList<StoryNotification> arrayList5;
        String str3;
        LongSparseArray longSparseArray5;
        int i13;
        DialogKey dialogKey2;
        Integer num;
        TLRPC$User tLRPC$User;
        String string;
        boolean z7;
        boolean z8;
        TLRPC$FileLocation tLRPC$FileLocation;
        TLRPC$FileLocation tLRPC$FileLocation2;
        boolean z9;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$FileLocation tLRPC$FileLocation3;
        TLRPC$TL_forumTopic findTopic;
        TLRPC$User tLRPC$User2;
        String userName;
        TLRPC$FileLocation tLRPC$FileLocation4;
        String str4;
        LongSparseArray longSparseArray6;
        NotificationsController notificationsController2;
        ArrayList arrayList6;
        Notification notification;
        boolean z10;
        long j3;
        TLRPC$User tLRPC$User3;
        boolean z11;
        TLRPC$FileLocation tLRPC$FileLocation5;
        String str5;
        String str6;
        MessageObject messageObject2;
        Bitmap bitmap;
        File file;
        File file2;
        File file3;
        TLRPC$Chat tLRPC$Chat2;
        int i14;
        String str7;
        String formatString;
        NotificationCompat.Action build;
        Integer num2;
        DialogKey dialogKey3;
        int max;
        Person person;
        String str8;
        String str9;
        NotificationCompat.MessagingStyle messagingStyle;
        NotificationCompat.MessagingStyle messagingStyle2;
        int i15;
        NotificationCompat.Action action;
        long j4;
        DialogKey dialogKey4;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList7;
        ArrayList<StoryNotification> arrayList8;
        LongSparseArray longSparseArray7;
        StringBuilder sb;
        int i16;
        Bitmap bitmap2;
        String str10;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList9;
        ArrayList<StoryNotification> arrayList10;
        int i17;
        long senderId;
        int i18;
        String str11;
        LongSparseArray longSparseArray8;
        Person person2;
        StringBuilder sb2;
        String str12;
        String str13;
        String[] strArr;
        boolean z12;
        File file4;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        TLRPC$FileLocation tLRPC$FileLocation6;
        LongSparseArray longSparseArray9;
        String str14;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup;
        boolean z13;
        List<NotificationCompat.MessagingStyle.Message> messages;
        Uri uri2;
        File file5;
        final File file6;
        final Uri uriForFile;
        String str15;
        File file7;
        Bitmap createScaledBitmap;
        Canvas canvas;
        DialogKey dialogKey5;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList11;
        Bitmap bitmap3;
        NotificationCompat.Action build2;
        String str16;
        long j5;
        ArrayList<StoryNotification> arrayList12;
        long j6;
        NotificationCompat.Builder category;
        TLRPC$User tLRPC$User4;
        int size3;
        int i19;
        int i20;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList13;
        TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow;
        LongSparseArray longSparseArray10;
        int i21;
        TLRPC$User user;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto2;
        TLRPC$FileLocation tLRPC$FileLocation7;
        Bitmap bitmap4;
        Bitmap bitmap5;
        String string2;
        String formatPluralString;
        DialogKey dialogKey6;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto3;
        NotificationsController notificationsController3 = this;
        int i22 = Build.VERSION.SDK_INT;
        if (i22 >= 26) {
            builder.setChannelId(validateChannelId(j, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
        }
        Notification build3 = builder.build();
        if (i22 <= 19) {
            notificationManager.notify(notificationsController3.notificationId, build3);
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("show summary notification by SDK check");
                return;
            }
            return;
        }
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        ArrayList arrayList14 = new ArrayList();
        if (!notificationsController3.storyPushMessages.isEmpty()) {
            arrayList14.add(new DialogKey(0L, 0, true));
        }
        LongSparseArray longSparseArray11 = new LongSparseArray();
        int i23 = 0;
        int i24 = 0;
        while (i24 < notificationsController3.pushMessages.size()) {
            MessageObject messageObject3 = notificationsController3.pushMessages.get(i24);
            long dialogId = messageObject3.getDialogId();
            int topicId = MessageObject.getTopicId(messageObject3.messageOwner, getMessagesController().isForum(messageObject3));
            int i25 = notificationsSettings.getInt("dismissDate" + dialogId, i23);
            if (messageObject3.isStoryPush || messageObject3.messageOwner.date > i25) {
                ArrayList arrayList15 = (ArrayList) longSparseArray11.get(dialogId);
                if (arrayList15 == null) {
                    ArrayList arrayList16 = new ArrayList();
                    longSparseArray11.put(dialogId, arrayList16);
                    arrayList14.add(new DialogKey(dialogId, topicId, false));
                    arrayList15 = arrayList16;
                }
                arrayList15.add(messageObject3);
            }
            i24++;
            i23 = 0;
        }
        LongSparseArray longSparseArray12 = new LongSparseArray();
        for (int i26 = 0; i26 < notificationsController3.wearNotificationsIds.size(); i26++) {
            longSparseArray12.put(notificationsController3.wearNotificationsIds.keyAt(i26), notificationsController3.wearNotificationsIds.valueAt(i26));
        }
        notificationsController3.wearNotificationsIds.clear();
        ArrayList arrayList17 = new ArrayList();
        int i27 = Build.VERSION.SDK_INT;
        if (i27 > 27) {
            if (arrayList14.size() <= (notificationsController3.storyPushMessages.isEmpty() ? 1 : 2)) {
                z4 = false;
                if (z4 && i27 >= 26) {
                    checkOtherNotificationsChannel();
                }
                clientUserId = getUserConfig().getClientUserId();
                z5 = !AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter;
                SharedConfig.passcodeHash.length();
                i5 = 7;
                longSparseArray = new LongSparseArray();
                size = arrayList14.size();
                i6 = 0;
                while (i6 < size && arrayList17.size() < i5) {
                    dialogKey = (DialogKey) arrayList14.get(i6);
                    if (!dialogKey.story) {
                        ArrayList<StoryNotification> arrayList18 = new ArrayList<>();
                        if (notificationsController3.storyPushMessages.isEmpty()) {
                            longSparseArray6 = longSparseArray;
                            j3 = clientUserId;
                            z10 = z4;
                            i9 = i6;
                            i10 = size;
                            longSparseArray5 = longSparseArray12;
                            longSparseArray4 = longSparseArray11;
                            arrayList4 = arrayList14;
                            notification = build3;
                            notificationsController2 = notificationsController3;
                            arrayList6 = arrayList17;
                            i6 = i9 + 1;
                            arrayList17 = arrayList6;
                            size = i10;
                            arrayList14 = arrayList4;
                            longSparseArray11 = longSparseArray4;
                            clientUserId = j3;
                            z4 = z10;
                            longSparseArray12 = longSparseArray5;
                            longSparseArray = longSparseArray6;
                            build3 = notification;
                            i5 = 7;
                            notificationsController3 = notificationsController2;
                        } else {
                            i9 = i6;
                            i10 = size;
                            arrayList2 = arrayList17;
                            long j7 = notificationsController3.storyPushMessages.get(0).dialogId;
                            id = 0;
                            for (Integer num3 : notificationsController3.storyPushMessages.get(0).dateByIds.keySet()) {
                                id = Math.max(id, num3.intValue());
                                arrayList18 = arrayList18;
                            }
                            arrayList4 = arrayList14;
                            arrayList3 = arrayList18;
                            longSparseArray4 = longSparseArray11;
                            j2 = j7;
                            i11 = 0;
                            messageObject = null;
                        }
                    } else {
                        i9 = i6;
                        i10 = size;
                        arrayList2 = arrayList17;
                        long j8 = dialogKey.dialogId;
                        int i28 = dialogKey.topicId;
                        arrayList3 = (ArrayList) longSparseArray11.get(j8);
                        id = ((MessageObject) arrayList3.get(0)).getId();
                        arrayList4 = arrayList14;
                        i11 = i28;
                        messageObject = (MessageObject) arrayList3.get(0);
                        longSparseArray4 = longSparseArray11;
                        j2 = j8;
                    }
                    int i29 = (Integer) longSparseArray12.get(j2);
                    Notification notification2 = build3;
                    z6 = z4;
                    if (!dialogKey.story) {
                        i29 = 2147483646;
                    } else if (i29 == null) {
                        i29 = Integer.valueOf(((int) j2) + ((int) (j2 >> 32)));
                    } else {
                        longSparseArray12.remove(j2);
                    }
                    Integer num4 = i29;
                    int i30 = 0;
                    for (i12 = 0; i12 < arrayList3.size(); i12++) {
                        if (i30 < ((MessageObject) arrayList3.get(i12)).messageOwner.date) {
                            i30 = ((MessageObject) arrayList3.get(i12)).messageOwner.date;
                        }
                    }
                    if (!dialogKey.story) {
                        longSparseArray5 = longSparseArray12;
                        tLRPC$User = getMessagesController().getUser(Long.valueOf(j2));
                        i13 = i30;
                        if (notificationsController3.storyPushMessages.size() == 1) {
                            if (tLRPC$User != null) {
                                formatPluralString = UserObject.getFirstName(tLRPC$User);
                            } else {
                                formatPluralString = notificationsController3.storyPushMessages.get(0).localName;
                            }
                            arrayList5 = arrayList3;
                        } else {
                            arrayList5 = arrayList3;
                            formatPluralString = LocaleController.formatPluralString("Stories", notificationsController3.storyPushMessages.size(), new Object[0]);
                        }
                        if (tLRPC$User == null || (tLRPC$UserProfilePhoto3 = tLRPC$User.photo) == null || (tLRPC$FileLocation3 = tLRPC$UserProfilePhoto3.photo_small) == null) {
                            str3 = "Stories";
                            dialogKey6 = dialogKey;
                        } else {
                            str3 = "Stories";
                            dialogKey6 = dialogKey;
                            if (tLRPC$FileLocation3.volume_id != 0 && tLRPC$FileLocation3.local_id != 0) {
                                string = formatPluralString;
                                dialogKey2 = dialogKey6;
                                num = num4;
                                tLRPC$Chat = null;
                                z9 = false;
                                z8 = false;
                                tLRPC$User2 = tLRPC$User;
                                z7 = false;
                                TLRPC$FileLocation tLRPC$FileLocation8 = tLRPC$FileLocation3;
                                if (z5) {
                                    tLRPC$User3 = tLRPC$User2;
                                    z11 = z7;
                                    tLRPC$FileLocation5 = tLRPC$FileLocation8;
                                } else {
                                    if (DialogObject.isChatDialog(j2)) {
                                        string2 = LocaleController.getString("NotificationHiddenChatName", R.string.NotificationHiddenChatName);
                                    } else {
                                        string2 = LocaleController.getString("NotificationHiddenName", R.string.NotificationHiddenName);
                                    }
                                    string = string2;
                                    tLRPC$User3 = tLRPC$User2;
                                    tLRPC$FileLocation5 = null;
                                    z11 = false;
                                }
                                if (tLRPC$FileLocation5 == null) {
                                    str5 = "NotificationHiddenName";
                                    file = getFileLoader().getPathToAttach(tLRPC$FileLocation5, true);
                                    str6 = "NotificationHiddenChatName";
                                    if (Build.VERSION.SDK_INT < 28) {
                                        messageObject2 = messageObject;
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
                                        messageObject2 = messageObject;
                                        bitmap4 = null;
                                    }
                                    bitmap = bitmap4;
                                } else {
                                    str5 = "NotificationHiddenName";
                                    str6 = "NotificationHiddenChatName";
                                    messageObject2 = messageObject;
                                    bitmap = null;
                                    file = null;
                                }
                                if (tLRPC$Chat == null) {
                                    Person.Builder name = new Person.Builder().setName(string);
                                    if (file != null && file.exists() && Build.VERSION.SDK_INT >= 28) {
                                        loadRoundAvatar(file, name);
                                    }
                                    file2 = file;
                                    longSparseArray.put(-tLRPC$Chat.id, name.build());
                                } else {
                                    file2 = file;
                                }
                                Bitmap bitmap6 = bitmap;
                                if (!(z9 || z8) || !z11 || SharedConfig.isWaitingForPasscodeEnter || clientUserId == j2 || UserObject.isReplyUser(j2)) {
                                    file3 = file2;
                                    i14 = id;
                                    str7 = "max_id";
                                    tLRPC$Chat2 = tLRPC$Chat;
                                    build = null;
                                } else {
                                    file3 = file2;
                                    tLRPC$Chat2 = tLRPC$Chat;
                                    Intent intent = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                                    intent.putExtra("dialog_id", j2);
                                    intent.putExtra("max_id", id);
                                    intent.putExtra("topic_id", i11);
                                    intent.putExtra("currentAccount", notificationsController3.currentAccount);
                                    i14 = id;
                                    PendingIntent broadcast = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent, 167772160);
                                    RemoteInput build4 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                                    if (!DialogObject.isChatDialog(j2)) {
                                        str7 = "max_id";
                                        formatString = LocaleController.formatString("ReplyToGroup", R.string.ReplyToGroup, string);
                                    } else {
                                        str7 = "max_id";
                                        formatString = LocaleController.formatString("ReplyToUser", R.string.ReplyToUser, string);
                                    }
                                    build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build4).setShowsUserInterface(false).build();
                                }
                                num2 = notificationsController3.pushDialogs.get(j2);
                                if (num2 == null) {
                                    num2 = 0;
                                }
                                dialogKey3 = dialogKey2;
                                if (!dialogKey3.story) {
                                    max = notificationsController3.storyPushMessages.size();
                                } else {
                                    max = Math.max(num2.intValue(), arrayList5.size());
                                }
                                String format = (max > 1 || Build.VERSION.SDK_INT >= 28) ? string : String.format("%1$s (%2$d)", string, Integer.valueOf(max));
                                person = (Person) longSparseArray.get(clientUserId);
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
                                            str8 = "currentAccount";
                                            str9 = string;
                                        }
                                        if (tLRPC$UserProfilePhoto2 != null && (tLRPC$FileLocation7 = tLRPC$UserProfilePhoto2.photo_small) != null) {
                                            str8 = "currentAccount";
                                            str9 = string;
                                            try {
                                                if (tLRPC$FileLocation7.volume_id != 0 && tLRPC$FileLocation7.local_id != 0) {
                                                    Person.Builder name2 = new Person.Builder().setName(LocaleController.getString("FromYou", R.string.FromYou));
                                                    loadRoundAvatar(getFileLoader().getPathToAttach(user.photo.photo_small, true), name2);
                                                    Person build5 = name2.build();
                                                    try {
                                                        longSparseArray.put(clientUserId, build5);
                                                        person = build5;
                                                    } catch (Throwable th2) {
                                                        th = th2;
                                                        person = build5;
                                                        FileLog.e(th);
                                                        if (messageObject2 == null) {
                                                        }
                                                        String str17 = "";
                                                        if (person == null) {
                                                        }
                                                        messagingStyle = new NotificationCompat.MessagingStyle("");
                                                        messagingStyle2 = messagingStyle;
                                                        i15 = Build.VERSION.SDK_INT;
                                                        if (i15 >= 28) {
                                                        }
                                                        messagingStyle2.setConversationTitle(format);
                                                        messagingStyle2.setGroupConversation(i15 >= 28 || (!z9 && DialogObject.isChatDialog(j2)) || UserObject.isReplyUser(j2));
                                                        StringBuilder sb3 = new StringBuilder();
                                                        String[] strArr2 = new String[1];
                                                        action = build;
                                                        boolean[] zArr = new boolean[1];
                                                        if (!dialogKey3.story) {
                                                        }
                                                        Intent intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                        intent2.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                        intent2.setFlags(ConnectionsManager.FileTypeFile);
                                                        intent2.addCategory("android.intent.category.LAUNCHER");
                                                        dialogKey5 = dialogKey4;
                                                        if (!dialogKey5.story) {
                                                        }
                                                        FileLog.d("show extra notifications chatId " + j2 + " topicId " + i11);
                                                        if (i11 != 0) {
                                                        }
                                                        String str18 = str8;
                                                        intent2.putExtra(str18, notificationsController3.currentAccount);
                                                        PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1140850688);
                                                        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
                                                        if (action != null) {
                                                        }
                                                        int i31 = i11;
                                                        Intent intent3 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                                        intent3.addFlags(32);
                                                        intent3.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                                        intent3.putExtra("dialog_id", j2);
                                                        int i32 = i14;
                                                        intent3.putExtra(str7, i32);
                                                        intent3.putExtra(str18, notificationsController3.currentAccount);
                                                        int i33 = i16;
                                                        arrayList11 = arrayList7;
                                                        bitmap3 = bitmap2;
                                                        build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent3, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                                        if (DialogObject.isEncryptedDialog(j2)) {
                                                        }
                                                        if (str16 == null) {
                                                        }
                                                        StringBuilder sb4 = new StringBuilder();
                                                        sb4.append("tgaccount");
                                                        long j9 = j4;
                                                        sb4.append(j9);
                                                        wearableExtender.setBridgeTag(sb4.toString());
                                                        if (!dialogKey5.story) {
                                                        }
                                                        NotificationCompat.Builder autoCancel = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str10).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                                        if (dialogKey5.story) {
                                                        }
                                                        category = autoCancel.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j6).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity).extend(wearableExtender).setSortKey(String.valueOf(Long.MAX_VALUE - j6)).setCategory("msg");
                                                        Intent intent4 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                        intent4.putExtra("messageDate", i13);
                                                        intent4.putExtra("dialogId", j2);
                                                        intent4.putExtra(str18, notificationsController3.currentAccount);
                                                        if (dialogKey5.story) {
                                                        }
                                                        category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent4, 167772160));
                                                        if (z6) {
                                                        }
                                                        if (action != null) {
                                                        }
                                                        if (!z5) {
                                                        }
                                                        if (arrayList4.size() != 1) {
                                                        }
                                                        if (DialogObject.isEncryptedDialog(j2)) {
                                                        }
                                                        if (bitmap3 != null) {
                                                        }
                                                        if (!AndroidUtilities.needShowPasscode(false)) {
                                                        }
                                                        if (tLRPC$Chat2 == null) {
                                                        }
                                                        tLRPC$User4 = tLRPC$User3;
                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                        }
                                                        j3 = j5;
                                                        longSparseArray6 = longSparseArray7;
                                                        z10 = z6;
                                                        notification = notification2;
                                                        arrayList6 = arrayList2;
                                                        arrayList6.add(new 1NotificationHolder(num.intValue(), j2, dialogKey5.story, i31, str10, tLRPC$User4, tLRPC$Chat2, category, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                                                        notificationsController2 = this;
                                                        notificationsController2.wearNotificationsIds.put(j2, num);
                                                        i6 = i9 + 1;
                                                        arrayList17 = arrayList6;
                                                        size = i10;
                                                        arrayList14 = arrayList4;
                                                        longSparseArray11 = longSparseArray4;
                                                        clientUserId = j3;
                                                        z4 = z10;
                                                        longSparseArray12 = longSparseArray5;
                                                        longSparseArray = longSparseArray6;
                                                        build3 = notification;
                                                        i5 = 7;
                                                        notificationsController3 = notificationsController2;
                                                    }
                                                }
                                            } catch (Throwable th3) {
                                                th = th3;
                                            }
                                            boolean z14 = (messageObject2 == null && (messageObject2.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest)) ? false : true;
                                            String str172 = "";
                                            if (person == null && z14) {
                                                messagingStyle = new NotificationCompat.MessagingStyle(person);
                                            } else {
                                                messagingStyle = new NotificationCompat.MessagingStyle("");
                                            }
                                            messagingStyle2 = messagingStyle;
                                            i15 = Build.VERSION.SDK_INT;
                                            if (i15 >= 28 || ((DialogObject.isChatDialog(j2) && !z9) || UserObject.isReplyUser(j2))) {
                                                messagingStyle2.setConversationTitle(format);
                                            }
                                            messagingStyle2.setGroupConversation(i15 >= 28 || (!z9 && DialogObject.isChatDialog(j2)) || UserObject.isReplyUser(j2));
                                            StringBuilder sb32 = new StringBuilder();
                                            String[] strArr22 = new String[1];
                                            action = build;
                                            boolean[] zArr2 = new boolean[1];
                                            if (!dialogKey3.story) {
                                                ArrayList<String> arrayList19 = new ArrayList<>();
                                                ArrayList<Object> arrayList20 = new ArrayList<>();
                                                Pair<Integer, Boolean> parseStoryPushes = notificationsController3.parseStoryPushes(arrayList19, arrayList20);
                                                int intValue = ((Integer) parseStoryPushes.first).intValue();
                                                boolean booleanValue = ((Boolean) parseStoryPushes.second).booleanValue();
                                                if (booleanValue) {
                                                    sb32.append(LocaleController.formatPluralString("StoryNotificationHidden", intValue, new Object[0]));
                                                } else {
                                                    if (!arrayList19.isEmpty()) {
                                                        if (arrayList19.size() != 1) {
                                                            if (arrayList19.size() == 2) {
                                                                dialogKey4 = dialogKey3;
                                                                sb32.append(LocaleController.formatString(R.string.StoryNotification2, arrayList19.get(0), arrayList19.get(1)));
                                                                longSparseArray10 = longSparseArray;
                                                            } else {
                                                                dialogKey4 = dialogKey3;
                                                                if (arrayList19.size() == 3 && notificationsController3.storyPushMessages.size() == 3) {
                                                                    longSparseArray10 = longSparseArray;
                                                                    sb32.append(LocaleController.formatString(R.string.StoryNotification3, notificationsController3.cutLastName(arrayList19.get(0)), notificationsController3.cutLastName(arrayList19.get(1)), notificationsController3.cutLastName(arrayList19.get(2))));
                                                                } else {
                                                                    longSparseArray10 = longSparseArray;
                                                                    sb32.append(LocaleController.formatPluralString("StoryNotification4", notificationsController3.storyPushMessages.size() - 2, notificationsController3.cutLastName(arrayList19.get(0)), notificationsController3.cutLastName(arrayList19.get(1))));
                                                                }
                                                            }
                                                            long j10 = Long.MAX_VALUE;
                                                            i21 = 0;
                                                            while (i21 < notificationsController3.storyPushMessages.size()) {
                                                                j10 = Math.min(notificationsController3.storyPushMessages.get(i21).date, j10);
                                                                i21++;
                                                                clientUserId = clientUserId;
                                                            }
                                                            j4 = clientUserId;
                                                            messagingStyle2.setGroupConversation(false);
                                                            String formatPluralString2 = (arrayList19.size() == 1 || booleanValue) ? LocaleController.formatPluralString(str3, intValue, new Object[0]) : arrayList19.get(0);
                                                            messagingStyle2.addMessage(sb32, j10, new Person.Builder().setName(formatPluralString2).build());
                                                            bitmap2 = booleanValue ? loadMultipleAvatars(arrayList20) : null;
                                                            arrayList8 = arrayList5;
                                                            longSparseArray7 = longSparseArray10;
                                                            arrayList7 = null;
                                                            i16 = 0;
                                                            sb = sb32;
                                                            str10 = formatPluralString2;
                                                        } else if (intValue == 1) {
                                                            sb32.append(LocaleController.getString("StoryNotificationSingle"));
                                                        } else {
                                                            sb32.append(LocaleController.formatPluralString("StoryNotification1", intValue, arrayList19.get(0)));
                                                        }
                                                    }
                                                    longSparseArray6 = longSparseArray;
                                                    notificationsController2 = notificationsController3;
                                                    arrayList6 = arrayList2;
                                                    notification = notification2;
                                                    z10 = z6;
                                                    j3 = clientUserId;
                                                    i6 = i9 + 1;
                                                    arrayList17 = arrayList6;
                                                    size = i10;
                                                    arrayList14 = arrayList4;
                                                    longSparseArray11 = longSparseArray4;
                                                    clientUserId = j3;
                                                    z4 = z10;
                                                    longSparseArray12 = longSparseArray5;
                                                    longSparseArray = longSparseArray6;
                                                    build3 = notification;
                                                    i5 = 7;
                                                    notificationsController3 = notificationsController2;
                                                }
                                                longSparseArray10 = longSparseArray;
                                                dialogKey4 = dialogKey3;
                                                long j102 = Long.MAX_VALUE;
                                                i21 = 0;
                                                while (i21 < notificationsController3.storyPushMessages.size()) {
                                                }
                                                j4 = clientUserId;
                                                messagingStyle2.setGroupConversation(false);
                                                if (arrayList19.size() == 1) {
                                                }
                                                messagingStyle2.addMessage(sb32, j102, new Person.Builder().setName(formatPluralString2).build());
                                                if (booleanValue) {
                                                }
                                                arrayList8 = arrayList5;
                                                longSparseArray7 = longSparseArray10;
                                                arrayList7 = null;
                                                i16 = 0;
                                                sb = sb32;
                                                str10 = formatPluralString2;
                                            } else {
                                                LongSparseArray longSparseArray13 = longSparseArray;
                                                j4 = clientUserId;
                                                dialogKey4 = dialogKey3;
                                                int size4 = arrayList5.size() - 1;
                                                int i34 = 0;
                                                arrayList7 = null;
                                                while (size4 >= 0) {
                                                    ArrayList<StoryNotification> arrayList21 = arrayList5;
                                                    MessageObject messageObject4 = (MessageObject) arrayList21.get(size4);
                                                    int i35 = i34;
                                                    if (i11 != MessageObject.getTopicId(messageObject4.messageOwner, getMessagesController().isForum(messageObject4))) {
                                                        i18 = size4;
                                                        arrayList9 = arrayList7;
                                                    } else {
                                                        String shortStringForMessage = notificationsController3.getShortStringForMessage(messageObject4, strArr22, zArr2);
                                                        if (j2 == j4) {
                                                            strArr22[0] = str9;
                                                        } else if (DialogObject.isChatDialog(j2) && messageObject4.messageOwner.from_scheduled) {
                                                            arrayList9 = arrayList7;
                                                            strArr22[0] = LocaleController.getString("NotificationMessageScheduledName", R.string.NotificationMessageScheduledName);
                                                            if (shortStringForMessage != null) {
                                                                if (BuildVars.LOGS_ENABLED) {
                                                                    FileLog.w("message text is null for " + messageObject4.getId() + " did = " + messageObject4.getDialogId());
                                                                }
                                                                i18 = size4;
                                                            } else {
                                                                if (sb32.length() > 0) {
                                                                    sb32.append("\n\n");
                                                                }
                                                                if (j2 != j4 && messageObject4.messageOwner.from_scheduled && DialogObject.isUserDialog(j2)) {
                                                                    arrayList10 = arrayList21;
                                                                    shortStringForMessage = String.format("%1$s: %2$s", LocaleController.getString("NotificationMessageScheduledName", R.string.NotificationMessageScheduledName), shortStringForMessage);
                                                                    sb32.append(shortStringForMessage);
                                                                } else {
                                                                    arrayList10 = arrayList21;
                                                                    if (strArr22[0] != null) {
                                                                        sb32.append(String.format("%1$s: %2$s", strArr22[0], shortStringForMessage));
                                                                    } else {
                                                                        sb32.append(shortStringForMessage);
                                                                    }
                                                                }
                                                                String str19 = shortStringForMessage;
                                                                if (DialogObject.isUserDialog(j2)) {
                                                                    i17 = size4;
                                                                } else {
                                                                    if (z9) {
                                                                        i17 = size4;
                                                                        senderId = -j2;
                                                                    } else {
                                                                        i17 = size4;
                                                                        if (DialogObject.isChatDialog(j2)) {
                                                                            senderId = messageObject4.getSenderId();
                                                                        }
                                                                    }
                                                                    i18 = i17;
                                                                    str11 = str172;
                                                                    String str20 = str11;
                                                                    longSparseArray8 = longSparseArray13;
                                                                    person2 = (Person) longSparseArray8.get((i11 << 16) + senderId);
                                                                    if (strArr22[0] != null) {
                                                                        if (!z5) {
                                                                            sb2 = sb32;
                                                                        } else if (DialogObject.isChatDialog(j2)) {
                                                                            if (z9) {
                                                                                sb2 = sb32;
                                                                                if (Build.VERSION.SDK_INT > 27) {
                                                                                    str13 = LocaleController.getString(str6, R.string.NotificationHiddenChatName);
                                                                                }
                                                                            } else {
                                                                                sb2 = sb32;
                                                                                str13 = LocaleController.getString("NotificationHiddenChatUserName", R.string.NotificationHiddenChatUserName);
                                                                            }
                                                                            str12 = str5;
                                                                        } else {
                                                                            sb2 = sb32;
                                                                            if (Build.VERSION.SDK_INT > 27) {
                                                                                str12 = str5;
                                                                                str13 = LocaleController.getString(str12, R.string.NotificationHiddenName);
                                                                            }
                                                                        }
                                                                        str12 = str5;
                                                                        str13 = str20;
                                                                    } else {
                                                                        sb2 = sb32;
                                                                        str12 = str5;
                                                                        str13 = strArr22[0];
                                                                    }
                                                                    str5 = str12;
                                                                    if (person2 == null && TextUtils.equals(person2.getName(), str13)) {
                                                                        strArr = strArr22;
                                                                        z12 = z9;
                                                                    } else {
                                                                        Person.Builder name3 = new Person.Builder().setName(str13);
                                                                        if (zArr2[0] || DialogObject.isEncryptedDialog(j2) || Build.VERSION.SDK_INT < 28) {
                                                                            strArr = strArr22;
                                                                            z12 = z9;
                                                                        } else {
                                                                            if (DialogObject.isUserDialog(j2) || z9) {
                                                                                strArr = strArr22;
                                                                                z12 = z9;
                                                                                file4 = file3;
                                                                            } else {
                                                                                String[] strArr3 = strArr22;
                                                                                long senderId2 = messageObject4.getSenderId();
                                                                                z12 = z9;
                                                                                strArr = strArr3;
                                                                                TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(senderId2));
                                                                                if (user2 == null && (user2 = getMessagesStorage().getUserSync(senderId2)) != null) {
                                                                                    getMessagesController().putUser(user2, true);
                                                                                }
                                                                                file4 = (user2 == null || (tLRPC$UserProfilePhoto = user2.photo) == null || (tLRPC$FileLocation6 = tLRPC$UserProfilePhoto.photo_small) == null || tLRPC$FileLocation6.volume_id == 0 || tLRPC$FileLocation6.local_id == 0) ? null : getFileLoader().getPathToAttach(user2.photo.photo_small, true);
                                                                            }
                                                                            loadRoundAvatar(file4, name3);
                                                                        }
                                                                        person2 = name3.build();
                                                                        longSparseArray8.put(senderId, person2);
                                                                    }
                                                                    if (DialogObject.isEncryptedDialog(j2)) {
                                                                        if (!zArr2[0] || Build.VERSION.SDK_INT < 28 || ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).isLowRamDevice() || z5 || messageObject4.isSecretMedia() || !(messageObject4.type == 1 || messageObject4.isSticker())) {
                                                                            longSparseArray9 = longSparseArray8;
                                                                            str14 = str20;
                                                                        } else {
                                                                            File pathToMessage = getFileLoader().getPathToMessage(messageObject4.messageOwner);
                                                                            if (pathToMessage.exists() && messageObject4.hasMediaSpoilers()) {
                                                                                file6 = new File(pathToMessage.getParentFile(), pathToMessage.getName() + ".blur.jpg");
                                                                                if (file6.exists()) {
                                                                                    file7 = pathToMessage;
                                                                                    longSparseArray9 = longSparseArray8;
                                                                                } else {
                                                                                    try {
                                                                                        Bitmap decodeFile = BitmapFactory.decodeFile(pathToMessage.getAbsolutePath());
                                                                                        Bitmap stackBlurBitmapMax = Utilities.stackBlurBitmapMax(decodeFile);
                                                                                        decodeFile.recycle();
                                                                                        createScaledBitmap = Bitmap.createScaledBitmap(stackBlurBitmapMax, decodeFile.getWidth(), decodeFile.getHeight(), true);
                                                                                        Utilities.stackBlurBitmap(createScaledBitmap, 5);
                                                                                        stackBlurBitmapMax.recycle();
                                                                                        canvas = new Canvas(createScaledBitmap);
                                                                                        file7 = pathToMessage;
                                                                                    } catch (Exception e) {
                                                                                        e = e;
                                                                                        file7 = pathToMessage;
                                                                                    }
                                                                                    try {
                                                                                        notificationsController3.mediaSpoilerEffect.setColor(ColorUtils.setAlphaComponent(-1, (int) (Color.alpha(-1) * 0.325f)));
                                                                                        longSparseArray9 = longSparseArray8;
                                                                                        try {
                                                                                            notificationsController3.mediaSpoilerEffect.setBounds(0, 0, createScaledBitmap.getWidth(), createScaledBitmap.getHeight());
                                                                                            notificationsController3.mediaSpoilerEffect.draw(canvas);
                                                                                            FileOutputStream fileOutputStream = new FileOutputStream(file6);
                                                                                            createScaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                                                                                            fileOutputStream.close();
                                                                                            createScaledBitmap.recycle();
                                                                                            file5 = file6;
                                                                                        } catch (Exception e2) {
                                                                                            e = e2;
                                                                                            FileLog.e(e);
                                                                                            file5 = file7;
                                                                                            NotificationCompat.MessagingStyle.Message message = new NotificationCompat.MessagingStyle.Message(str19, messageObject4.messageOwner.date * 1000, person2);
                                                                                            String str21 = messageObject4.isSticker() ? "image/webp" : "image/jpeg";
                                                                                            if (file5.exists()) {
                                                                                            }
                                                                                            if (uriForFile != null) {
                                                                                            }
                                                                                        }
                                                                                    } catch (Exception e3) {
                                                                                        e = e3;
                                                                                        longSparseArray9 = longSparseArray8;
                                                                                        FileLog.e(e);
                                                                                        file5 = file7;
                                                                                        NotificationCompat.MessagingStyle.Message message2 = new NotificationCompat.MessagingStyle.Message(str19, messageObject4.messageOwner.date * 1000, person2);
                                                                                        String str212 = messageObject4.isSticker() ? "image/webp" : "image/jpeg";
                                                                                        if (file5.exists()) {
                                                                                        }
                                                                                        if (uriForFile != null) {
                                                                                        }
                                                                                    }
                                                                                }
                                                                                file5 = file7;
                                                                            } else {
                                                                                longSparseArray9 = longSparseArray8;
                                                                                file5 = pathToMessage;
                                                                                file6 = null;
                                                                            }
                                                                            NotificationCompat.MessagingStyle.Message message22 = new NotificationCompat.MessagingStyle.Message(str19, messageObject4.messageOwner.date * 1000, person2);
                                                                            String str2122 = messageObject4.isSticker() ? "image/webp" : "image/jpeg";
                                                                            if (file5.exists()) {
                                                                                try {
                                                                                    uriForFile = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", file5);
                                                                                    str15 = str20;
                                                                                } catch (Exception e4) {
                                                                                    FileLog.e(e4);
                                                                                }
                                                                            } else {
                                                                                if (getFileLoader().isLoadingFile(file5.getName())) {
                                                                                    Uri.Builder appendPath = new Uri.Builder().scheme("content").authority(NotificationImageProvider.getAuthority()).appendPath("msg_media_raw");
                                                                                    StringBuilder sb5 = new StringBuilder();
                                                                                    sb5.append(notificationsController3.currentAccount);
                                                                                    str15 = str20;
                                                                                    sb5.append(str15);
                                                                                    uriForFile = appendPath.appendPath(sb5.toString()).appendPath(file5.getName()).appendQueryParameter("final_path", file5.getAbsolutePath()).build();
                                                                                }
                                                                                str15 = str20;
                                                                                uriForFile = null;
                                                                            }
                                                                            if (uriForFile != null) {
                                                                                message22.setData(str2122, uriForFile);
                                                                                messagingStyle2.addMessage(message22);
                                                                                ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile, 1);
                                                                                str14 = str15;
                                                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda4
                                                                                    @Override // java.lang.Runnable
                                                                                    public final void run() {
                                                                                        NotificationsController.lambda$showExtraNotifications$40(uriForFile, file6);
                                                                                    }
                                                                                }, 20000L);
                                                                                if (!TextUtils.isEmpty(messageObject4.caption)) {
                                                                                    messagingStyle2.addMessage(messageObject4.caption, messageObject4.messageOwner.date * 1000, person2);
                                                                                }
                                                                                z13 = true;
                                                                                if (!z13) {
                                                                                    messagingStyle2.addMessage(str19, messageObject4.messageOwner.date * 1000, person2);
                                                                                }
                                                                                if (zArr2[0] && !z5 && messageObject4.isVoice()) {
                                                                                    messages = messagingStyle2.getMessages();
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
                                                                                str14 = str15;
                                                                            }
                                                                        }
                                                                        z13 = false;
                                                                        if (!z13) {
                                                                        }
                                                                        if (zArr2[0]) {
                                                                            messages = messagingStyle2.getMessages();
                                                                            if (!messages.isEmpty()) {
                                                                            }
                                                                        }
                                                                    } else {
                                                                        longSparseArray9 = longSparseArray8;
                                                                        str14 = str20;
                                                                        messagingStyle2.addMessage(str19, messageObject4.messageOwner.date * 1000, person2);
                                                                    }
                                                                    if (j2 == 777000 && (tLRPC$ReplyMarkup = messageObject4.messageOwner.reply_markup) != null) {
                                                                        arrayList7 = tLRPC$ReplyMarkup.rows;
                                                                        i34 = messageObject4.getId();
                                                                        size4 = i18 - 1;
                                                                        str172 = str14;
                                                                        sb32 = sb2;
                                                                        arrayList5 = arrayList10;
                                                                        z9 = z12;
                                                                        strArr22 = strArr;
                                                                        longSparseArray13 = longSparseArray9;
                                                                    }
                                                                    i34 = i35;
                                                                    arrayList7 = arrayList9;
                                                                    size4 = i18 - 1;
                                                                    str172 = str14;
                                                                    sb32 = sb2;
                                                                    arrayList5 = arrayList10;
                                                                    z9 = z12;
                                                                    strArr22 = strArr;
                                                                    longSparseArray13 = longSparseArray9;
                                                                }
                                                                i18 = i17;
                                                                str11 = str172;
                                                                senderId = j2;
                                                                String str202 = str11;
                                                                longSparseArray8 = longSparseArray13;
                                                                person2 = (Person) longSparseArray8.get((i11 << 16) + senderId);
                                                                if (strArr22[0] != null) {
                                                                }
                                                                str5 = str12;
                                                                if (person2 == null) {
                                                                }
                                                                Person.Builder name32 = new Person.Builder().setName(str13);
                                                                if (zArr2[0]) {
                                                                }
                                                                strArr = strArr22;
                                                                z12 = z9;
                                                                person2 = name32.build();
                                                                longSparseArray8.put(senderId, person2);
                                                                if (DialogObject.isEncryptedDialog(j2)) {
                                                                }
                                                                if (j2 == 777000) {
                                                                    arrayList7 = tLRPC$ReplyMarkup.rows;
                                                                    i34 = messageObject4.getId();
                                                                    size4 = i18 - 1;
                                                                    str172 = str14;
                                                                    sb32 = sb2;
                                                                    arrayList5 = arrayList10;
                                                                    z9 = z12;
                                                                    strArr22 = strArr;
                                                                    longSparseArray13 = longSparseArray9;
                                                                }
                                                                i34 = i35;
                                                                arrayList7 = arrayList9;
                                                                size4 = i18 - 1;
                                                                str172 = str14;
                                                                sb32 = sb2;
                                                                arrayList5 = arrayList10;
                                                                z9 = z12;
                                                                strArr22 = strArr;
                                                                longSparseArray13 = longSparseArray9;
                                                            }
                                                        }
                                                        arrayList9 = arrayList7;
                                                        if (shortStringForMessage != null) {
                                                        }
                                                    }
                                                    arrayList10 = arrayList21;
                                                    strArr = strArr22;
                                                    z12 = z9;
                                                    longSparseArray9 = longSparseArray13;
                                                    str14 = str172;
                                                    sb2 = sb32;
                                                    i34 = i35;
                                                    arrayList7 = arrayList9;
                                                    size4 = i18 - 1;
                                                    str172 = str14;
                                                    sb32 = sb2;
                                                    arrayList5 = arrayList10;
                                                    z9 = z12;
                                                    strArr22 = strArr;
                                                    longSparseArray13 = longSparseArray9;
                                                }
                                                arrayList8 = arrayList5;
                                                longSparseArray7 = longSparseArray13;
                                                sb = sb32;
                                                i16 = i34;
                                                bitmap2 = bitmap6;
                                                str10 = str9;
                                            }
                                            Intent intent22 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                            intent22.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                            intent22.setFlags(ConnectionsManager.FileTypeFile);
                                            intent22.addCategory("android.intent.category.LAUNCHER");
                                            dialogKey5 = dialogKey4;
                                            if (!dialogKey5.story) {
                                                long[] jArr2 = new long[notificationsController3.storyPushMessages.size()];
                                                for (int i36 = 0; i36 < notificationsController3.storyPushMessages.size(); i36++) {
                                                    jArr2[i36] = notificationsController3.storyPushMessages.get(i36).dialogId;
                                                }
                                                intent22.putExtra("storyDialogIds", jArr2);
                                            } else if (DialogObject.isEncryptedDialog(j2)) {
                                                intent22.putExtra("encId", DialogObject.getEncryptedChatId(j2));
                                            } else if (DialogObject.isUserDialog(j2)) {
                                                intent22.putExtra("userId", j2);
                                            } else {
                                                intent22.putExtra("chatId", -j2);
                                            }
                                            FileLog.d("show extra notifications chatId " + j2 + " topicId " + i11);
                                            if (i11 != 0) {
                                                intent22.putExtra("topicId", i11);
                                            }
                                            String str182 = str8;
                                            intent22.putExtra(str182, notificationsController3.currentAccount);
                                            PendingIntent activity2 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22, 1140850688);
                                            NotificationCompat.WearableExtender wearableExtender2 = new NotificationCompat.WearableExtender();
                                            if (action != null) {
                                                wearableExtender2.addAction(action);
                                            }
                                            int i312 = i11;
                                            Intent intent32 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                            intent32.addFlags(32);
                                            intent32.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                            intent32.putExtra("dialog_id", j2);
                                            int i322 = i14;
                                            intent32.putExtra(str7, i322);
                                            intent32.putExtra(str182, notificationsController3.currentAccount);
                                            int i332 = i16;
                                            arrayList11 = arrayList7;
                                            bitmap3 = bitmap2;
                                            build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent32, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                            if (DialogObject.isEncryptedDialog(j2)) {
                                                str16 = DialogObject.isUserDialog(j2) ? "tguser" + j2 + "_" + i322 : "tgchat" + (-j2) + "_" + i322;
                                            } else if (j2 != globalSecretChatId) {
                                                str16 = "tgenc" + DialogObject.getEncryptedChatId(j2) + "_" + i322;
                                            } else {
                                                str16 = null;
                                            }
                                            if (str16 == null) {
                                                wearableExtender2.setDismissalId(str16);
                                                NotificationCompat.WearableExtender wearableExtender3 = new NotificationCompat.WearableExtender();
                                                wearableExtender3.setDismissalId("summary_" + str16);
                                                builder.extend(wearableExtender3);
                                            }
                                            StringBuilder sb42 = new StringBuilder();
                                            sb42.append("tgaccount");
                                            long j92 = j4;
                                            sb42.append(j92);
                                            wearableExtender2.setBridgeTag(sb42.toString());
                                            if (!dialogKey5.story) {
                                                j5 = j92;
                                                j6 = Long.MAX_VALUE;
                                                for (int i37 = 0; i37 < notificationsController3.storyPushMessages.size(); i37++) {
                                                    j6 = Math.min(notificationsController3.storyPushMessages.get(i37).date, j6);
                                                }
                                                arrayList12 = arrayList8;
                                            } else {
                                                j5 = j92;
                                                arrayList12 = arrayList8;
                                                j6 = ((MessageObject) arrayList12.get(0)).messageOwner.date * 1000;
                                            }
                                            NotificationCompat.Builder autoCancel2 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str10).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                            if (dialogKey5.story) {
                                                arrayList12 = notificationsController3.storyPushMessages;
                                            }
                                            category = autoCancel2.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j6).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity2).extend(wearableExtender2).setSortKey(String.valueOf(Long.MAX_VALUE - j6)).setCategory("msg");
                                            Intent intent42 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                            intent42.putExtra("messageDate", i13);
                                            intent42.putExtra("dialogId", j2);
                                            intent42.putExtra(str182, notificationsController3.currentAccount);
                                            if (dialogKey5.story) {
                                                intent42.putExtra("story", true);
                                            }
                                            category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent42, 167772160));
                                            if (z6) {
                                                category.setGroup(notificationsController3.notificationGroup);
                                                category.setGroupAlertBehavior(1);
                                            }
                                            if (action != null) {
                                                category.addAction(action);
                                            }
                                            if (!z5 && !dialogKey5.story) {
                                                category.addAction(build2);
                                            }
                                            if (arrayList4.size() != 1 && !TextUtils.isEmpty(str) && !dialogKey5.story) {
                                                category.setSubText(str);
                                            }
                                            if (DialogObject.isEncryptedDialog(j2)) {
                                                category.setLocalOnly(true);
                                            }
                                            if (bitmap3 != null) {
                                                category.setLargeIcon(bitmap3);
                                            }
                                            if (!AndroidUtilities.needShowPasscode(false) && !SharedConfig.isWaitingForPasscodeEnter && arrayList11 != null) {
                                                size3 = arrayList11.size();
                                                for (i19 = 0; i19 < size3; i19++) {
                                                    ArrayList<TLRPC$TL_keyboardButtonRow> arrayList22 = arrayList11;
                                                    TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow2 = arrayList22.get(i19);
                                                    int size5 = tLRPC$TL_keyboardButtonRow2.buttons.size();
                                                    int i38 = 0;
                                                    while (i38 < size5) {
                                                        TLRPC$KeyboardButton tLRPC$KeyboardButton = tLRPC$TL_keyboardButtonRow2.buttons.get(i38);
                                                        if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonCallback) {
                                                            i20 = size3;
                                                            arrayList13 = arrayList22;
                                                            Intent intent5 = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                                            intent5.putExtra(str182, notificationsController3.currentAccount);
                                                            intent5.putExtra("did", j2);
                                                            byte[] bArr = tLRPC$KeyboardButton.data;
                                                            if (bArr != null) {
                                                                intent5.putExtra("data", bArr);
                                                            }
                                                            intent5.putExtra("mid", i332);
                                                            String str22 = tLRPC$KeyboardButton.text;
                                                            Context context = ApplicationLoader.applicationContext;
                                                            int i39 = notificationsController3.lastButtonId;
                                                            tLRPC$TL_keyboardButtonRow = tLRPC$TL_keyboardButtonRow2;
                                                            notificationsController3.lastButtonId = i39 + 1;
                                                            category.addAction(0, str22, PendingIntent.getBroadcast(context, i39, intent5, 167772160));
                                                        } else {
                                                            i20 = size3;
                                                            arrayList13 = arrayList22;
                                                            tLRPC$TL_keyboardButtonRow = tLRPC$TL_keyboardButtonRow2;
                                                        }
                                                        i38++;
                                                        size3 = i20;
                                                        arrayList22 = arrayList13;
                                                        tLRPC$TL_keyboardButtonRow2 = tLRPC$TL_keyboardButtonRow;
                                                    }
                                                    arrayList11 = arrayList22;
                                                }
                                            }
                                            if (tLRPC$Chat2 == null || tLRPC$User3 == null) {
                                                tLRPC$User4 = tLRPC$User3;
                                            } else {
                                                tLRPC$User4 = tLRPC$User3;
                                                String str23 = tLRPC$User4.phone;
                                                if (str23 != null && str23.length() > 0) {
                                                    category.addPerson("tel:+" + tLRPC$User4.phone);
                                                }
                                            }
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                notificationsController3.setNotificationChannel(notification2, category, z6);
                                            }
                                            j3 = j5;
                                            longSparseArray6 = longSparseArray7;
                                            z10 = z6;
                                            notification = notification2;
                                            arrayList6 = arrayList2;
                                            arrayList6.add(new 1NotificationHolder(num.intValue(), j2, dialogKey5.story, i312, str10, tLRPC$User4, tLRPC$Chat2, category, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                                            notificationsController2 = this;
                                            notificationsController2.wearNotificationsIds.put(j2, num);
                                            i6 = i9 + 1;
                                            arrayList17 = arrayList6;
                                            size = i10;
                                            arrayList14 = arrayList4;
                                            longSparseArray11 = longSparseArray4;
                                            clientUserId = j3;
                                            z4 = z10;
                                            longSparseArray12 = longSparseArray5;
                                            longSparseArray = longSparseArray6;
                                            build3 = notification;
                                            i5 = 7;
                                            notificationsController3 = notificationsController2;
                                        }
                                    }
                                }
                                str8 = "currentAccount";
                                str9 = string;
                                if (messageObject2 == null) {
                                }
                                String str1722 = "";
                                if (person == null) {
                                }
                                messagingStyle = new NotificationCompat.MessagingStyle("");
                                messagingStyle2 = messagingStyle;
                                i15 = Build.VERSION.SDK_INT;
                                if (i15 >= 28) {
                                }
                                messagingStyle2.setConversationTitle(format);
                                messagingStyle2.setGroupConversation(i15 >= 28 || (!z9 && DialogObject.isChatDialog(j2)) || UserObject.isReplyUser(j2));
                                StringBuilder sb322 = new StringBuilder();
                                String[] strArr222 = new String[1];
                                action = build;
                                boolean[] zArr22 = new boolean[1];
                                if (!dialogKey3.story) {
                                }
                                Intent intent222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                intent222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                intent222.setFlags(ConnectionsManager.FileTypeFile);
                                intent222.addCategory("android.intent.category.LAUNCHER");
                                dialogKey5 = dialogKey4;
                                if (!dialogKey5.story) {
                                }
                                FileLog.d("show extra notifications chatId " + j2 + " topicId " + i11);
                                if (i11 != 0) {
                                }
                                String str1822 = str8;
                                intent222.putExtra(str1822, notificationsController3.currentAccount);
                                PendingIntent activity22 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222, 1140850688);
                                NotificationCompat.WearableExtender wearableExtender22 = new NotificationCompat.WearableExtender();
                                if (action != null) {
                                }
                                int i3122 = i11;
                                Intent intent322 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                intent322.addFlags(32);
                                intent322.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                intent322.putExtra("dialog_id", j2);
                                int i3222 = i14;
                                intent322.putExtra(str7, i3222);
                                intent322.putExtra(str1822, notificationsController3.currentAccount);
                                int i3322 = i16;
                                arrayList11 = arrayList7;
                                bitmap3 = bitmap2;
                                build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent322, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                if (DialogObject.isEncryptedDialog(j2)) {
                                }
                                if (str16 == null) {
                                }
                                StringBuilder sb422 = new StringBuilder();
                                sb422.append("tgaccount");
                                long j922 = j4;
                                sb422.append(j922);
                                wearableExtender22.setBridgeTag(sb422.toString());
                                if (!dialogKey5.story) {
                                }
                                NotificationCompat.Builder autoCancel22 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str10).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                if (dialogKey5.story) {
                                }
                                category = autoCancel22.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j6).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity22).extend(wearableExtender22).setSortKey(String.valueOf(Long.MAX_VALUE - j6)).setCategory("msg");
                                Intent intent422 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                intent422.putExtra("messageDate", i13);
                                intent422.putExtra("dialogId", j2);
                                intent422.putExtra(str1822, notificationsController3.currentAccount);
                                if (dialogKey5.story) {
                                }
                                category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent422, 167772160));
                                if (z6) {
                                }
                                if (action != null) {
                                }
                                if (!z5) {
                                    category.addAction(build2);
                                }
                                if (arrayList4.size() != 1) {
                                }
                                if (DialogObject.isEncryptedDialog(j2)) {
                                }
                                if (bitmap3 != null) {
                                }
                                if (!AndroidUtilities.needShowPasscode(false)) {
                                    size3 = arrayList11.size();
                                    while (i19 < size3) {
                                    }
                                }
                                if (tLRPC$Chat2 == null) {
                                }
                                tLRPC$User4 = tLRPC$User3;
                                if (Build.VERSION.SDK_INT >= 26) {
                                }
                                j3 = j5;
                                longSparseArray6 = longSparseArray7;
                                z10 = z6;
                                notification = notification2;
                                arrayList6 = arrayList2;
                                arrayList6.add(new 1NotificationHolder(num.intValue(), j2, dialogKey5.story, i3122, str10, tLRPC$User4, tLRPC$Chat2, category, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                                notificationsController2 = this;
                                notificationsController2.wearNotificationsIds.put(j2, num);
                                i6 = i9 + 1;
                                arrayList17 = arrayList6;
                                size = i10;
                                arrayList14 = arrayList4;
                                longSparseArray11 = longSparseArray4;
                                clientUserId = j3;
                                z4 = z10;
                                longSparseArray12 = longSparseArray5;
                                longSparseArray = longSparseArray6;
                                build3 = notification;
                                i5 = 7;
                                notificationsController3 = notificationsController2;
                            }
                        }
                        string = formatPluralString;
                        dialogKey2 = dialogKey6;
                        num = num4;
                    } else {
                        arrayList5 = arrayList3;
                        str3 = "Stories";
                        longSparseArray5 = longSparseArray12;
                        i13 = i30;
                        if (!DialogObject.isEncryptedDialog(j2)) {
                            z7 = j2 != 777000;
                            if (DialogObject.isUserDialog(j2)) {
                                TLRPC$User user3 = getMessagesController().getUser(Long.valueOf(j2));
                                if (user3 == null) {
                                    if (messageObject.isFcmMessage()) {
                                        userName = messageObject.localName;
                                    } else if (BuildVars.LOGS_ENABLED) {
                                        FileLog.w("not found user to show dialog notification " + j2);
                                    }
                                } else {
                                    userName = UserObject.getUserName(user3);
                                    TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto4 = user3.photo;
                                    if (tLRPC$UserProfilePhoto4 != null && (tLRPC$FileLocation4 = tLRPC$UserProfilePhoto4.photo_small) != null) {
                                        tLRPC$User2 = user3;
                                        str4 = userName;
                                        if (tLRPC$FileLocation4.volume_id != 0 && tLRPC$FileLocation4.local_id != 0) {
                                            tLRPC$FileLocation3 = tLRPC$FileLocation4;
                                            if (UserObject.isReplyUser(j2)) {
                                                string = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                                            } else if (j2 == clientUserId) {
                                                string = LocaleController.getString("MessageScheduledReminderNotification", R.string.MessageScheduledReminderNotification);
                                            } else {
                                                dialogKey2 = dialogKey;
                                                num = num4;
                                                string = str4;
                                                tLRPC$Chat = null;
                                                z9 = false;
                                                z8 = false;
                                                TLRPC$FileLocation tLRPC$FileLocation82 = tLRPC$FileLocation3;
                                                if (z5) {
                                                }
                                                if (tLRPC$FileLocation5 == null) {
                                                }
                                                if (tLRPC$Chat == null) {
                                                }
                                                Bitmap bitmap62 = bitmap;
                                                if (z9) {
                                                }
                                                file3 = file2;
                                                tLRPC$Chat2 = tLRPC$Chat;
                                                Intent intent6 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                                                intent6.putExtra("dialog_id", j2);
                                                intent6.putExtra("max_id", id);
                                                intent6.putExtra("topic_id", i11);
                                                intent6.putExtra("currentAccount", notificationsController3.currentAccount);
                                                i14 = id;
                                                PendingIntent broadcast2 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent6, 167772160);
                                                RemoteInput build42 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                                                if (!DialogObject.isChatDialog(j2)) {
                                                }
                                                build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast2).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build42).setShowsUserInterface(false).build();
                                                num2 = notificationsController3.pushDialogs.get(j2);
                                                if (num2 == null) {
                                                }
                                                dialogKey3 = dialogKey2;
                                                if (!dialogKey3.story) {
                                                }
                                                if (max > 1) {
                                                }
                                                person = (Person) longSparseArray.get(clientUserId);
                                                if (Build.VERSION.SDK_INT >= 28) {
                                                    user = getMessagesController().getUser(Long.valueOf(clientUserId));
                                                    if (user == null) {
                                                    }
                                                    if (user != null) {
                                                    }
                                                }
                                                str8 = "currentAccount";
                                                str9 = string;
                                                if (messageObject2 == null) {
                                                }
                                                String str17222 = "";
                                                if (person == null) {
                                                }
                                                messagingStyle = new NotificationCompat.MessagingStyle("");
                                                messagingStyle2 = messagingStyle;
                                                i15 = Build.VERSION.SDK_INT;
                                                if (i15 >= 28) {
                                                }
                                                messagingStyle2.setConversationTitle(format);
                                                messagingStyle2.setGroupConversation(i15 >= 28 || (!z9 && DialogObject.isChatDialog(j2)) || UserObject.isReplyUser(j2));
                                                StringBuilder sb3222 = new StringBuilder();
                                                String[] strArr2222 = new String[1];
                                                action = build;
                                                boolean[] zArr222 = new boolean[1];
                                                if (!dialogKey3.story) {
                                                }
                                                Intent intent2222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                intent2222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                intent2222.setFlags(ConnectionsManager.FileTypeFile);
                                                intent2222.addCategory("android.intent.category.LAUNCHER");
                                                dialogKey5 = dialogKey4;
                                                if (!dialogKey5.story) {
                                                }
                                                FileLog.d("show extra notifications chatId " + j2 + " topicId " + i11);
                                                if (i11 != 0) {
                                                }
                                                String str18222 = str8;
                                                intent2222.putExtra(str18222, notificationsController3.currentAccount);
                                                PendingIntent activity222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2222, 1140850688);
                                                NotificationCompat.WearableExtender wearableExtender222 = new NotificationCompat.WearableExtender();
                                                if (action != null) {
                                                }
                                                int i31222 = i11;
                                                Intent intent3222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                                intent3222.addFlags(32);
                                                intent3222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                                intent3222.putExtra("dialog_id", j2);
                                                int i32222 = i14;
                                                intent3222.putExtra(str7, i32222);
                                                intent3222.putExtra(str18222, notificationsController3.currentAccount);
                                                int i33222 = i16;
                                                arrayList11 = arrayList7;
                                                bitmap3 = bitmap2;
                                                build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent3222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                                if (DialogObject.isEncryptedDialog(j2)) {
                                                }
                                                if (str16 == null) {
                                                }
                                                StringBuilder sb4222 = new StringBuilder();
                                                sb4222.append("tgaccount");
                                                long j9222 = j4;
                                                sb4222.append(j9222);
                                                wearableExtender222.setBridgeTag(sb4222.toString());
                                                if (!dialogKey5.story) {
                                                }
                                                NotificationCompat.Builder autoCancel222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str10).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                                if (dialogKey5.story) {
                                                }
                                                category = autoCancel222.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j6).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity222).extend(wearableExtender222).setSortKey(String.valueOf(Long.MAX_VALUE - j6)).setCategory("msg");
                                                Intent intent4222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                intent4222.putExtra("messageDate", i13);
                                                intent4222.putExtra("dialogId", j2);
                                                intent4222.putExtra(str18222, notificationsController3.currentAccount);
                                                if (dialogKey5.story) {
                                                }
                                                category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent4222, 167772160));
                                                if (z6) {
                                                }
                                                if (action != null) {
                                                }
                                                if (!z5) {
                                                }
                                                if (arrayList4.size() != 1) {
                                                }
                                                if (DialogObject.isEncryptedDialog(j2)) {
                                                }
                                                if (bitmap3 != null) {
                                                }
                                                if (!AndroidUtilities.needShowPasscode(false)) {
                                                }
                                                if (tLRPC$Chat2 == null) {
                                                }
                                                tLRPC$User4 = tLRPC$User3;
                                                if (Build.VERSION.SDK_INT >= 26) {
                                                }
                                                j3 = j5;
                                                longSparseArray6 = longSparseArray7;
                                                z10 = z6;
                                                notification = notification2;
                                                arrayList6 = arrayList2;
                                                arrayList6.add(new 1NotificationHolder(num.intValue(), j2, dialogKey5.story, i31222, str10, tLRPC$User4, tLRPC$Chat2, category, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                                                notificationsController2 = this;
                                                notificationsController2.wearNotificationsIds.put(j2, num);
                                            }
                                            dialogKey2 = dialogKey;
                                            num = num4;
                                            tLRPC$Chat = null;
                                            z9 = false;
                                            z8 = false;
                                            TLRPC$FileLocation tLRPC$FileLocation822 = tLRPC$FileLocation3;
                                            if (z5) {
                                            }
                                            if (tLRPC$FileLocation5 == null) {
                                            }
                                            if (tLRPC$Chat == null) {
                                            }
                                            Bitmap bitmap622 = bitmap;
                                            if (z9) {
                                            }
                                            file3 = file2;
                                            tLRPC$Chat2 = tLRPC$Chat;
                                            Intent intent62 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                                            intent62.putExtra("dialog_id", j2);
                                            intent62.putExtra("max_id", id);
                                            intent62.putExtra("topic_id", i11);
                                            intent62.putExtra("currentAccount", notificationsController3.currentAccount);
                                            i14 = id;
                                            PendingIntent broadcast22 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent62, 167772160);
                                            RemoteInput build422 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                                            if (!DialogObject.isChatDialog(j2)) {
                                            }
                                            build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast22).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build422).setShowsUserInterface(false).build();
                                            num2 = notificationsController3.pushDialogs.get(j2);
                                            if (num2 == null) {
                                            }
                                            dialogKey3 = dialogKey2;
                                            if (!dialogKey3.story) {
                                            }
                                            if (max > 1) {
                                            }
                                            person = (Person) longSparseArray.get(clientUserId);
                                            if (Build.VERSION.SDK_INT >= 28) {
                                            }
                                            str8 = "currentAccount";
                                            str9 = string;
                                            if (messageObject2 == null) {
                                            }
                                            String str172222 = "";
                                            if (person == null) {
                                            }
                                            messagingStyle = new NotificationCompat.MessagingStyle("");
                                            messagingStyle2 = messagingStyle;
                                            i15 = Build.VERSION.SDK_INT;
                                            if (i15 >= 28) {
                                            }
                                            messagingStyle2.setConversationTitle(format);
                                            messagingStyle2.setGroupConversation(i15 >= 28 || (!z9 && DialogObject.isChatDialog(j2)) || UserObject.isReplyUser(j2));
                                            StringBuilder sb32222 = new StringBuilder();
                                            String[] strArr22222 = new String[1];
                                            action = build;
                                            boolean[] zArr2222 = new boolean[1];
                                            if (!dialogKey3.story) {
                                            }
                                            Intent intent22222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                            intent22222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                            intent22222.setFlags(ConnectionsManager.FileTypeFile);
                                            intent22222.addCategory("android.intent.category.LAUNCHER");
                                            dialogKey5 = dialogKey4;
                                            if (!dialogKey5.story) {
                                            }
                                            FileLog.d("show extra notifications chatId " + j2 + " topicId " + i11);
                                            if (i11 != 0) {
                                            }
                                            String str182222 = str8;
                                            intent22222.putExtra(str182222, notificationsController3.currentAccount);
                                            PendingIntent activity2222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22222, 1140850688);
                                            NotificationCompat.WearableExtender wearableExtender2222 = new NotificationCompat.WearableExtender();
                                            if (action != null) {
                                            }
                                            int i312222 = i11;
                                            Intent intent32222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                            intent32222.addFlags(32);
                                            intent32222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                            intent32222.putExtra("dialog_id", j2);
                                            int i322222 = i14;
                                            intent32222.putExtra(str7, i322222);
                                            intent32222.putExtra(str182222, notificationsController3.currentAccount);
                                            int i332222 = i16;
                                            arrayList11 = arrayList7;
                                            bitmap3 = bitmap2;
                                            build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent32222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                            if (DialogObject.isEncryptedDialog(j2)) {
                                            }
                                            if (str16 == null) {
                                            }
                                            StringBuilder sb42222 = new StringBuilder();
                                            sb42222.append("tgaccount");
                                            long j92222 = j4;
                                            sb42222.append(j92222);
                                            wearableExtender2222.setBridgeTag(sb42222.toString());
                                            if (!dialogKey5.story) {
                                            }
                                            NotificationCompat.Builder autoCancel2222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str10).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                            if (dialogKey5.story) {
                                            }
                                            category = autoCancel2222.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j6).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity2222).extend(wearableExtender2222).setSortKey(String.valueOf(Long.MAX_VALUE - j6)).setCategory("msg");
                                            Intent intent42222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                            intent42222.putExtra("messageDate", i13);
                                            intent42222.putExtra("dialogId", j2);
                                            intent42222.putExtra(str182222, notificationsController3.currentAccount);
                                            if (dialogKey5.story) {
                                            }
                                            category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent42222, 167772160));
                                            if (z6) {
                                            }
                                            if (action != null) {
                                            }
                                            if (!z5) {
                                            }
                                            if (arrayList4.size() != 1) {
                                            }
                                            if (DialogObject.isEncryptedDialog(j2)) {
                                            }
                                            if (bitmap3 != null) {
                                            }
                                            if (!AndroidUtilities.needShowPasscode(false)) {
                                            }
                                            if (tLRPC$Chat2 == null) {
                                            }
                                            tLRPC$User4 = tLRPC$User3;
                                            if (Build.VERSION.SDK_INT >= 26) {
                                            }
                                            j3 = j5;
                                            longSparseArray6 = longSparseArray7;
                                            z10 = z6;
                                            notification = notification2;
                                            arrayList6 = arrayList2;
                                            arrayList6.add(new 1NotificationHolder(num.intValue(), j2, dialogKey5.story, i312222, str10, tLRPC$User4, tLRPC$Chat2, category, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                                            notificationsController2 = this;
                                            notificationsController2.wearNotificationsIds.put(j2, num);
                                        }
                                        tLRPC$FileLocation3 = null;
                                        if (UserObject.isReplyUser(j2)) {
                                        }
                                        dialogKey2 = dialogKey;
                                        num = num4;
                                        tLRPC$Chat = null;
                                        z9 = false;
                                        z8 = false;
                                        TLRPC$FileLocation tLRPC$FileLocation8222 = tLRPC$FileLocation3;
                                        if (z5) {
                                        }
                                        if (tLRPC$FileLocation5 == null) {
                                        }
                                        if (tLRPC$Chat == null) {
                                        }
                                        Bitmap bitmap6222 = bitmap;
                                        if (z9) {
                                        }
                                        file3 = file2;
                                        tLRPC$Chat2 = tLRPC$Chat;
                                        Intent intent622 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                                        intent622.putExtra("dialog_id", j2);
                                        intent622.putExtra("max_id", id);
                                        intent622.putExtra("topic_id", i11);
                                        intent622.putExtra("currentAccount", notificationsController3.currentAccount);
                                        i14 = id;
                                        PendingIntent broadcast222 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent622, 167772160);
                                        RemoteInput build4222 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                                        if (!DialogObject.isChatDialog(j2)) {
                                        }
                                        build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast222).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build4222).setShowsUserInterface(false).build();
                                        num2 = notificationsController3.pushDialogs.get(j2);
                                        if (num2 == null) {
                                        }
                                        dialogKey3 = dialogKey2;
                                        if (!dialogKey3.story) {
                                        }
                                        if (max > 1) {
                                        }
                                        person = (Person) longSparseArray.get(clientUserId);
                                        if (Build.VERSION.SDK_INT >= 28) {
                                        }
                                        str8 = "currentAccount";
                                        str9 = string;
                                        if (messageObject2 == null) {
                                        }
                                        String str1722222 = "";
                                        if (person == null) {
                                        }
                                        messagingStyle = new NotificationCompat.MessagingStyle("");
                                        messagingStyle2 = messagingStyle;
                                        i15 = Build.VERSION.SDK_INT;
                                        if (i15 >= 28) {
                                        }
                                        messagingStyle2.setConversationTitle(format);
                                        messagingStyle2.setGroupConversation(i15 >= 28 || (!z9 && DialogObject.isChatDialog(j2)) || UserObject.isReplyUser(j2));
                                        StringBuilder sb322222 = new StringBuilder();
                                        String[] strArr222222 = new String[1];
                                        action = build;
                                        boolean[] zArr22222 = new boolean[1];
                                        if (!dialogKey3.story) {
                                        }
                                        Intent intent222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                        intent222222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                        intent222222.setFlags(ConnectionsManager.FileTypeFile);
                                        intent222222.addCategory("android.intent.category.LAUNCHER");
                                        dialogKey5 = dialogKey4;
                                        if (!dialogKey5.story) {
                                        }
                                        FileLog.d("show extra notifications chatId " + j2 + " topicId " + i11);
                                        if (i11 != 0) {
                                        }
                                        String str1822222 = str8;
                                        intent222222.putExtra(str1822222, notificationsController3.currentAccount);
                                        PendingIntent activity22222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222222, 1140850688);
                                        NotificationCompat.WearableExtender wearableExtender22222 = new NotificationCompat.WearableExtender();
                                        if (action != null) {
                                        }
                                        int i3122222 = i11;
                                        Intent intent322222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                        intent322222.addFlags(32);
                                        intent322222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                        intent322222.putExtra("dialog_id", j2);
                                        int i3222222 = i14;
                                        intent322222.putExtra(str7, i3222222);
                                        intent322222.putExtra(str1822222, notificationsController3.currentAccount);
                                        int i3322222 = i16;
                                        arrayList11 = arrayList7;
                                        bitmap3 = bitmap2;
                                        build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent322222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                        if (DialogObject.isEncryptedDialog(j2)) {
                                        }
                                        if (str16 == null) {
                                        }
                                        StringBuilder sb422222 = new StringBuilder();
                                        sb422222.append("tgaccount");
                                        long j922222 = j4;
                                        sb422222.append(j922222);
                                        wearableExtender22222.setBridgeTag(sb422222.toString());
                                        if (!dialogKey5.story) {
                                        }
                                        NotificationCompat.Builder autoCancel22222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str10).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                        if (dialogKey5.story) {
                                        }
                                        category = autoCancel22222.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j6).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity22222).extend(wearableExtender22222).setSortKey(String.valueOf(Long.MAX_VALUE - j6)).setCategory("msg");
                                        Intent intent422222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                        intent422222.putExtra("messageDate", i13);
                                        intent422222.putExtra("dialogId", j2);
                                        intent422222.putExtra(str1822222, notificationsController3.currentAccount);
                                        if (dialogKey5.story) {
                                        }
                                        category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent422222, 167772160));
                                        if (z6) {
                                        }
                                        if (action != null) {
                                        }
                                        if (!z5) {
                                        }
                                        if (arrayList4.size() != 1) {
                                        }
                                        if (DialogObject.isEncryptedDialog(j2)) {
                                        }
                                        if (bitmap3 != null) {
                                        }
                                        if (!AndroidUtilities.needShowPasscode(false)) {
                                        }
                                        if (tLRPC$Chat2 == null) {
                                        }
                                        tLRPC$User4 = tLRPC$User3;
                                        if (Build.VERSION.SDK_INT >= 26) {
                                        }
                                        j3 = j5;
                                        longSparseArray6 = longSparseArray7;
                                        z10 = z6;
                                        notification = notification2;
                                        arrayList6 = arrayList2;
                                        arrayList6.add(new 1NotificationHolder(num.intValue(), j2, dialogKey5.story, i3122222, str10, tLRPC$User4, tLRPC$Chat2, category, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                                        notificationsController2 = this;
                                        notificationsController2.wearNotificationsIds.put(j2, num);
                                    }
                                }
                                tLRPC$User2 = user3;
                                str4 = userName;
                                tLRPC$FileLocation3 = null;
                                if (UserObject.isReplyUser(j2)) {
                                }
                                dialogKey2 = dialogKey;
                                num = num4;
                                tLRPC$Chat = null;
                                z9 = false;
                                z8 = false;
                                TLRPC$FileLocation tLRPC$FileLocation82222 = tLRPC$FileLocation3;
                                if (z5) {
                                }
                                if (tLRPC$FileLocation5 == null) {
                                }
                                if (tLRPC$Chat == null) {
                                }
                                Bitmap bitmap62222 = bitmap;
                                if (z9) {
                                }
                                file3 = file2;
                                tLRPC$Chat2 = tLRPC$Chat;
                                Intent intent6222 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                                intent6222.putExtra("dialog_id", j2);
                                intent6222.putExtra("max_id", id);
                                intent6222.putExtra("topic_id", i11);
                                intent6222.putExtra("currentAccount", notificationsController3.currentAccount);
                                i14 = id;
                                PendingIntent broadcast2222 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent6222, 167772160);
                                RemoteInput build42222 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                                if (!DialogObject.isChatDialog(j2)) {
                                }
                                build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast2222).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build42222).setShowsUserInterface(false).build();
                                num2 = notificationsController3.pushDialogs.get(j2);
                                if (num2 == null) {
                                }
                                dialogKey3 = dialogKey2;
                                if (!dialogKey3.story) {
                                }
                                if (max > 1) {
                                }
                                person = (Person) longSparseArray.get(clientUserId);
                                if (Build.VERSION.SDK_INT >= 28) {
                                }
                                str8 = "currentAccount";
                                str9 = string;
                                if (messageObject2 == null) {
                                }
                                String str17222222 = "";
                                if (person == null) {
                                }
                                messagingStyle = new NotificationCompat.MessagingStyle("");
                                messagingStyle2 = messagingStyle;
                                i15 = Build.VERSION.SDK_INT;
                                if (i15 >= 28) {
                                }
                                messagingStyle2.setConversationTitle(format);
                                messagingStyle2.setGroupConversation(i15 >= 28 || (!z9 && DialogObject.isChatDialog(j2)) || UserObject.isReplyUser(j2));
                                StringBuilder sb3222222 = new StringBuilder();
                                String[] strArr2222222 = new String[1];
                                action = build;
                                boolean[] zArr222222 = new boolean[1];
                                if (!dialogKey3.story) {
                                }
                                Intent intent2222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                intent2222222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                intent2222222.setFlags(ConnectionsManager.FileTypeFile);
                                intent2222222.addCategory("android.intent.category.LAUNCHER");
                                dialogKey5 = dialogKey4;
                                if (!dialogKey5.story) {
                                }
                                FileLog.d("show extra notifications chatId " + j2 + " topicId " + i11);
                                if (i11 != 0) {
                                }
                                String str18222222 = str8;
                                intent2222222.putExtra(str18222222, notificationsController3.currentAccount);
                                PendingIntent activity222222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2222222, 1140850688);
                                NotificationCompat.WearableExtender wearableExtender222222 = new NotificationCompat.WearableExtender();
                                if (action != null) {
                                }
                                int i31222222 = i11;
                                Intent intent3222222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                intent3222222.addFlags(32);
                                intent3222222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                intent3222222.putExtra("dialog_id", j2);
                                int i32222222 = i14;
                                intent3222222.putExtra(str7, i32222222);
                                intent3222222.putExtra(str18222222, notificationsController3.currentAccount);
                                int i33222222 = i16;
                                arrayList11 = arrayList7;
                                bitmap3 = bitmap2;
                                build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent3222222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                if (DialogObject.isEncryptedDialog(j2)) {
                                }
                                if (str16 == null) {
                                }
                                StringBuilder sb4222222 = new StringBuilder();
                                sb4222222.append("tgaccount");
                                long j9222222 = j4;
                                sb4222222.append(j9222222);
                                wearableExtender222222.setBridgeTag(sb4222222.toString());
                                if (!dialogKey5.story) {
                                }
                                NotificationCompat.Builder autoCancel222222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str10).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                if (dialogKey5.story) {
                                }
                                category = autoCancel222222.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j6).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity222222).extend(wearableExtender222222).setSortKey(String.valueOf(Long.MAX_VALUE - j6)).setCategory("msg");
                                Intent intent4222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                intent4222222.putExtra("messageDate", i13);
                                intent4222222.putExtra("dialogId", j2);
                                intent4222222.putExtra(str18222222, notificationsController3.currentAccount);
                                if (dialogKey5.story) {
                                }
                                category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent4222222, 167772160));
                                if (z6) {
                                }
                                if (action != null) {
                                }
                                if (!z5) {
                                }
                                if (arrayList4.size() != 1) {
                                }
                                if (DialogObject.isEncryptedDialog(j2)) {
                                }
                                if (bitmap3 != null) {
                                }
                                if (!AndroidUtilities.needShowPasscode(false)) {
                                }
                                if (tLRPC$Chat2 == null) {
                                }
                                tLRPC$User4 = tLRPC$User3;
                                if (Build.VERSION.SDK_INT >= 26) {
                                }
                                j3 = j5;
                                longSparseArray6 = longSparseArray7;
                                z10 = z6;
                                notification = notification2;
                                arrayList6 = arrayList2;
                                arrayList6.add(new 1NotificationHolder(num.intValue(), j2, dialogKey5.story, i31222222, str10, tLRPC$User4, tLRPC$Chat2, category, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                                notificationsController2 = this;
                                notificationsController2.wearNotificationsIds.put(j2, num);
                            } else {
                                TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j2));
                                if (chat == null) {
                                    if (messageObject.isFcmMessage()) {
                                        boolean isSupergroup = messageObject.isSupergroup();
                                        String str24 = messageObject.localName;
                                        z8 = isSupergroup;
                                        dialogKey2 = dialogKey;
                                        num = num4;
                                        z7 = false;
                                        tLRPC$Chat = chat;
                                        z9 = messageObject.localChannel;
                                        string = str24;
                                        tLRPC$FileLocation3 = null;
                                    } else if (BuildVars.LOGS_ENABLED) {
                                        FileLog.w("not found chat to show dialog notification " + j2);
                                    }
                                } else {
                                    boolean z15 = chat.megagroup;
                                    boolean z16 = ChatObject.isChannel(chat) && !chat.megagroup;
                                    String str25 = chat.title;
                                    z8 = z15;
                                    TLRPC$ChatPhoto tLRPC$ChatPhoto = chat.photo;
                                    if (tLRPC$ChatPhoto == null || (tLRPC$FileLocation = tLRPC$ChatPhoto.photo_small) == null) {
                                        dialogKey2 = dialogKey;
                                        num = num4;
                                    } else {
                                        dialogKey2 = dialogKey;
                                        num = num4;
                                        if (tLRPC$FileLocation.volume_id != 0) {
                                        }
                                    }
                                    tLRPC$FileLocation = null;
                                    if (i11 != 0) {
                                        tLRPC$FileLocation2 = tLRPC$FileLocation;
                                        z9 = z16;
                                        if (getMessagesController().getTopicsController().findTopic(chat.id, i11) != null) {
                                            str25 = findTopic.title + " in " + str25;
                                        }
                                    } else {
                                        tLRPC$FileLocation2 = tLRPC$FileLocation;
                                        z9 = z16;
                                    }
                                    if (z7) {
                                        z7 = ChatObject.canSendPlain(chat);
                                    }
                                    tLRPC$Chat = chat;
                                    string = str25;
                                    tLRPC$FileLocation3 = tLRPC$FileLocation2;
                                }
                                tLRPC$User2 = null;
                                TLRPC$FileLocation tLRPC$FileLocation822222 = tLRPC$FileLocation3;
                                if (z5) {
                                }
                                if (tLRPC$FileLocation5 == null) {
                                }
                                if (tLRPC$Chat == null) {
                                }
                                Bitmap bitmap622222 = bitmap;
                                if (z9) {
                                }
                                file3 = file2;
                                tLRPC$Chat2 = tLRPC$Chat;
                                Intent intent62222 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                                intent62222.putExtra("dialog_id", j2);
                                intent62222.putExtra("max_id", id);
                                intent62222.putExtra("topic_id", i11);
                                intent62222.putExtra("currentAccount", notificationsController3.currentAccount);
                                i14 = id;
                                PendingIntent broadcast22222 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent62222, 167772160);
                                RemoteInput build422222 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                                if (!DialogObject.isChatDialog(j2)) {
                                }
                                build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast22222).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build422222).setShowsUserInterface(false).build();
                                num2 = notificationsController3.pushDialogs.get(j2);
                                if (num2 == null) {
                                }
                                dialogKey3 = dialogKey2;
                                if (!dialogKey3.story) {
                                }
                                if (max > 1) {
                                }
                                person = (Person) longSparseArray.get(clientUserId);
                                if (Build.VERSION.SDK_INT >= 28) {
                                }
                                str8 = "currentAccount";
                                str9 = string;
                                if (messageObject2 == null) {
                                }
                                String str172222222 = "";
                                if (person == null) {
                                }
                                messagingStyle = new NotificationCompat.MessagingStyle("");
                                messagingStyle2 = messagingStyle;
                                i15 = Build.VERSION.SDK_INT;
                                if (i15 >= 28) {
                                }
                                messagingStyle2.setConversationTitle(format);
                                messagingStyle2.setGroupConversation(i15 >= 28 || (!z9 && DialogObject.isChatDialog(j2)) || UserObject.isReplyUser(j2));
                                StringBuilder sb32222222 = new StringBuilder();
                                String[] strArr22222222 = new String[1];
                                action = build;
                                boolean[] zArr2222222 = new boolean[1];
                                if (!dialogKey3.story) {
                                }
                                Intent intent22222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                intent22222222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                intent22222222.setFlags(ConnectionsManager.FileTypeFile);
                                intent22222222.addCategory("android.intent.category.LAUNCHER");
                                dialogKey5 = dialogKey4;
                                if (!dialogKey5.story) {
                                }
                                FileLog.d("show extra notifications chatId " + j2 + " topicId " + i11);
                                if (i11 != 0) {
                                }
                                String str182222222 = str8;
                                intent22222222.putExtra(str182222222, notificationsController3.currentAccount);
                                PendingIntent activity2222222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22222222, 1140850688);
                                NotificationCompat.WearableExtender wearableExtender2222222 = new NotificationCompat.WearableExtender();
                                if (action != null) {
                                }
                                int i312222222 = i11;
                                Intent intent32222222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                intent32222222.addFlags(32);
                                intent32222222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                intent32222222.putExtra("dialog_id", j2);
                                int i322222222 = i14;
                                intent32222222.putExtra(str7, i322222222);
                                intent32222222.putExtra(str182222222, notificationsController3.currentAccount);
                                int i332222222 = i16;
                                arrayList11 = arrayList7;
                                bitmap3 = bitmap2;
                                build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent32222222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                if (DialogObject.isEncryptedDialog(j2)) {
                                }
                                if (str16 == null) {
                                }
                                StringBuilder sb42222222 = new StringBuilder();
                                sb42222222.append("tgaccount");
                                long j92222222 = j4;
                                sb42222222.append(j92222222);
                                wearableExtender2222222.setBridgeTag(sb42222222.toString());
                                if (!dialogKey5.story) {
                                }
                                NotificationCompat.Builder autoCancel2222222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str10).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                if (dialogKey5.story) {
                                }
                                category = autoCancel2222222.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j6).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity2222222).extend(wearableExtender2222222).setSortKey(String.valueOf(Long.MAX_VALUE - j6)).setCategory("msg");
                                Intent intent42222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                intent42222222.putExtra("messageDate", i13);
                                intent42222222.putExtra("dialogId", j2);
                                intent42222222.putExtra(str182222222, notificationsController3.currentAccount);
                                if (dialogKey5.story) {
                                }
                                category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent42222222, 167772160));
                                if (z6) {
                                }
                                if (action != null) {
                                }
                                if (!z5) {
                                }
                                if (arrayList4.size() != 1) {
                                }
                                if (DialogObject.isEncryptedDialog(j2)) {
                                }
                                if (bitmap3 != null) {
                                }
                                if (!AndroidUtilities.needShowPasscode(false)) {
                                }
                                if (tLRPC$Chat2 == null) {
                                }
                                tLRPC$User4 = tLRPC$User3;
                                if (Build.VERSION.SDK_INT >= 26) {
                                }
                                j3 = j5;
                                longSparseArray6 = longSparseArray7;
                                z10 = z6;
                                notification = notification2;
                                arrayList6 = arrayList2;
                                arrayList6.add(new 1NotificationHolder(num.intValue(), j2, dialogKey5.story, i312222222, str10, tLRPC$User4, tLRPC$Chat2, category, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                                notificationsController2 = this;
                                notificationsController2.wearNotificationsIds.put(j2, num);
                            }
                            i6 = i9 + 1;
                            arrayList17 = arrayList6;
                            size = i10;
                            arrayList14 = arrayList4;
                            longSparseArray11 = longSparseArray4;
                            clientUserId = j3;
                            z4 = z10;
                            longSparseArray12 = longSparseArray5;
                            longSparseArray = longSparseArray6;
                            build3 = notification;
                            i5 = 7;
                            notificationsController3 = notificationsController2;
                        } else {
                            dialogKey2 = dialogKey;
                            num = num4;
                            if (j2 != globalSecretChatId) {
                                int encryptedChatId = DialogObject.getEncryptedChatId(j2);
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
                            } else {
                                tLRPC$User = null;
                            }
                            string = LocaleController.getString("SecretChatName", R.string.SecretChatName);
                        }
                        longSparseArray6 = longSparseArray;
                        notificationsController2 = notificationsController3;
                        arrayList6 = arrayList2;
                        notification = notification2;
                        z10 = z6;
                        j3 = clientUserId;
                        i6 = i9 + 1;
                        arrayList17 = arrayList6;
                        size = i10;
                        arrayList14 = arrayList4;
                        longSparseArray11 = longSparseArray4;
                        clientUserId = j3;
                        z4 = z10;
                        longSparseArray12 = longSparseArray5;
                        longSparseArray = longSparseArray6;
                        build3 = notification;
                        i5 = 7;
                        notificationsController3 = notificationsController2;
                    }
                    tLRPC$FileLocation3 = null;
                    tLRPC$Chat = null;
                    z9 = false;
                    z8 = false;
                    tLRPC$User2 = tLRPC$User;
                    z7 = false;
                    TLRPC$FileLocation tLRPC$FileLocation8222222 = tLRPC$FileLocation3;
                    if (z5) {
                    }
                    if (tLRPC$FileLocation5 == null) {
                    }
                    if (tLRPC$Chat == null) {
                    }
                    Bitmap bitmap6222222 = bitmap;
                    if (z9) {
                    }
                    file3 = file2;
                    tLRPC$Chat2 = tLRPC$Chat;
                    Intent intent622222 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                    intent622222.putExtra("dialog_id", j2);
                    intent622222.putExtra("max_id", id);
                    intent622222.putExtra("topic_id", i11);
                    intent622222.putExtra("currentAccount", notificationsController3.currentAccount);
                    i14 = id;
                    PendingIntent broadcast222222 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent622222, 167772160);
                    RemoteInput build4222222 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                    if (!DialogObject.isChatDialog(j2)) {
                    }
                    build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast222222).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build4222222).setShowsUserInterface(false).build();
                    num2 = notificationsController3.pushDialogs.get(j2);
                    if (num2 == null) {
                    }
                    dialogKey3 = dialogKey2;
                    if (!dialogKey3.story) {
                    }
                    if (max > 1) {
                    }
                    person = (Person) longSparseArray.get(clientUserId);
                    if (Build.VERSION.SDK_INT >= 28) {
                    }
                    str8 = "currentAccount";
                    str9 = string;
                    if (messageObject2 == null) {
                    }
                    String str1722222222 = "";
                    if (person == null) {
                    }
                    messagingStyle = new NotificationCompat.MessagingStyle("");
                    messagingStyle2 = messagingStyle;
                    i15 = Build.VERSION.SDK_INT;
                    if (i15 >= 28) {
                    }
                    messagingStyle2.setConversationTitle(format);
                    messagingStyle2.setGroupConversation(i15 >= 28 || (!z9 && DialogObject.isChatDialog(j2)) || UserObject.isReplyUser(j2));
                    StringBuilder sb322222222 = new StringBuilder();
                    String[] strArr222222222 = new String[1];
                    action = build;
                    boolean[] zArr22222222 = new boolean[1];
                    if (!dialogKey3.story) {
                    }
                    Intent intent222222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    intent222222222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                    intent222222222.setFlags(ConnectionsManager.FileTypeFile);
                    intent222222222.addCategory("android.intent.category.LAUNCHER");
                    dialogKey5 = dialogKey4;
                    if (!dialogKey5.story) {
                    }
                    FileLog.d("show extra notifications chatId " + j2 + " topicId " + i11);
                    if (i11 != 0) {
                    }
                    String str1822222222 = str8;
                    intent222222222.putExtra(str1822222222, notificationsController3.currentAccount);
                    PendingIntent activity22222222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222222222, 1140850688);
                    NotificationCompat.WearableExtender wearableExtender22222222 = new NotificationCompat.WearableExtender();
                    if (action != null) {
                    }
                    int i3122222222 = i11;
                    Intent intent322222222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                    intent322222222.addFlags(32);
                    intent322222222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                    intent322222222.putExtra("dialog_id", j2);
                    int i3222222222 = i14;
                    intent322222222.putExtra(str7, i3222222222);
                    intent322222222.putExtra(str1822222222, notificationsController3.currentAccount);
                    int i3322222222 = i16;
                    arrayList11 = arrayList7;
                    bitmap3 = bitmap2;
                    build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent322222222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                    if (DialogObject.isEncryptedDialog(j2)) {
                    }
                    if (str16 == null) {
                    }
                    StringBuilder sb422222222 = new StringBuilder();
                    sb422222222.append("tgaccount");
                    long j922222222 = j4;
                    sb422222222.append(j922222222);
                    wearableExtender22222222.setBridgeTag(sb422222222.toString());
                    if (!dialogKey5.story) {
                    }
                    NotificationCompat.Builder autoCancel22222222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str10).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                    if (dialogKey5.story) {
                    }
                    category = autoCancel22222222.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j6).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity22222222).extend(wearableExtender22222222).setSortKey(String.valueOf(Long.MAX_VALUE - j6)).setCategory("msg");
                    Intent intent422222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                    intent422222222.putExtra("messageDate", i13);
                    intent422222222.putExtra("dialogId", j2);
                    intent422222222.putExtra(str1822222222, notificationsController3.currentAccount);
                    if (dialogKey5.story) {
                    }
                    category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent422222222, 167772160));
                    if (z6) {
                    }
                    if (action != null) {
                    }
                    if (!z5) {
                    }
                    if (arrayList4.size() != 1) {
                    }
                    if (DialogObject.isEncryptedDialog(j2)) {
                    }
                    if (bitmap3 != null) {
                    }
                    if (!AndroidUtilities.needShowPasscode(false)) {
                    }
                    if (tLRPC$Chat2 == null) {
                    }
                    tLRPC$User4 = tLRPC$User3;
                    if (Build.VERSION.SDK_INT >= 26) {
                    }
                    j3 = j5;
                    longSparseArray6 = longSparseArray7;
                    z10 = z6;
                    notification = notification2;
                    arrayList6 = arrayList2;
                    arrayList6.add(new 1NotificationHolder(num.intValue(), j2, dialogKey5.story, i3122222222, str10, tLRPC$User4, tLRPC$Chat2, category, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
                    notificationsController2 = this;
                    notificationsController2.wearNotificationsIds.put(j2, num);
                    i6 = i9 + 1;
                    arrayList17 = arrayList6;
                    size = i10;
                    arrayList14 = arrayList4;
                    longSparseArray11 = longSparseArray4;
                    clientUserId = j3;
                    z4 = z10;
                    longSparseArray12 = longSparseArray5;
                    longSparseArray = longSparseArray6;
                    build3 = notification;
                    i5 = 7;
                    notificationsController3 = notificationsController2;
                }
                LongSparseArray longSparseArray14 = longSparseArray;
                longSparseArray2 = longSparseArray12;
                Notification notification3 = build3;
                NotificationsController notificationsController4 = notificationsController3;
                ArrayList arrayList23 = arrayList17;
                if (!z4) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("show summary with id " + notificationsController4.notificationId);
                    }
                    try {
                        notificationManager.notify(notificationsController4.notificationId, notification3);
                        arrayList = arrayList23;
                        notificationsController = notificationsController4;
                    } catch (SecurityException e5) {
                        FileLog.e(e5);
                        arrayList = arrayList23;
                        notificationsController = notificationsController4;
                        resetNotificationSound(builder, j, i, str2, jArr, i2, uri, i3, z, z2, z3, i4);
                    }
                } else {
                    arrayList = arrayList23;
                    notificationsController = notificationsController4;
                    if (notificationsController.openedInBubbleDialogs.isEmpty()) {
                        notificationManager.cancel(notificationsController.notificationId);
                    }
                }
                i7 = 0;
                while (i7 < longSparseArray2.size()) {
                    LongSparseArray longSparseArray15 = longSparseArray2;
                    if (!notificationsController.openedInBubbleDialogs.contains(Long.valueOf(longSparseArray15.keyAt(i7)))) {
                        Integer num5 = (Integer) longSparseArray15.valueAt(i7);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("cancel notification id " + num5);
                        }
                        notificationManager.cancel(num5.intValue());
                    }
                    i7++;
                    longSparseArray2 = longSparseArray15;
                }
                ArrayList arrayList24 = new ArrayList(arrayList.size());
                size2 = arrayList.size();
                i8 = 0;
                while (i8 < size2) {
                    ArrayList arrayList25 = arrayList;
                    1NotificationHolder r3 = (1NotificationHolder) arrayList25.get(i8);
                    arrayList24.clear();
                    if (Build.VERSION.SDK_INT < 29 || DialogObject.isEncryptedDialog(r3.dialogId)) {
                        longSparseArray3 = longSparseArray14;
                    } else {
                        NotificationCompat.Builder builder2 = r3.notification;
                        long j11 = r3.dialogId;
                        longSparseArray3 = longSparseArray14;
                        String createNotificationShortcut = createNotificationShortcut(builder2, j11, r3.name, r3.user, r3.chat, (Person) longSparseArray3.get(j11), !r3.story);
                        if (createNotificationShortcut != null) {
                            arrayList24.add(createNotificationShortcut);
                        }
                    }
                    r3.call();
                    if (!unsupportedNotificationShortcut() && !arrayList24.isEmpty()) {
                        ShortcutManagerCompat.removeDynamicShortcuts(ApplicationLoader.applicationContext, arrayList24);
                    }
                    i8++;
                    arrayList = arrayList25;
                    longSparseArray14 = longSparseArray3;
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
        i5 = 7;
        longSparseArray = new LongSparseArray();
        size = arrayList14.size();
        i6 = 0;
        while (i6 < size) {
            dialogKey = (DialogKey) arrayList14.get(i6);
            if (!dialogKey.story) {
            }
            int i292 = (Integer) longSparseArray12.get(j2);
            Notification notification22 = build3;
            z6 = z4;
            if (!dialogKey.story) {
            }
            Integer num42 = i292;
            int i302 = 0;
            while (i12 < arrayList3.size()) {
            }
            if (!dialogKey.story) {
            }
            tLRPC$FileLocation3 = null;
            tLRPC$Chat = null;
            z9 = false;
            z8 = false;
            tLRPC$User2 = tLRPC$User;
            z7 = false;
            TLRPC$FileLocation tLRPC$FileLocation82222222 = tLRPC$FileLocation3;
            if (z5) {
            }
            if (tLRPC$FileLocation5 == null) {
            }
            if (tLRPC$Chat == null) {
            }
            Bitmap bitmap62222222 = bitmap;
            if (z9) {
            }
            file3 = file2;
            tLRPC$Chat2 = tLRPC$Chat;
            Intent intent6222222 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
            intent6222222.putExtra("dialog_id", j2);
            intent6222222.putExtra("max_id", id);
            intent6222222.putExtra("topic_id", i11);
            intent6222222.putExtra("currentAccount", notificationsController3.currentAccount);
            i14 = id;
            PendingIntent broadcast2222222 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent6222222, 167772160);
            RemoteInput build42222222 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
            if (!DialogObject.isChatDialog(j2)) {
            }
            build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast2222222).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build42222222).setShowsUserInterface(false).build();
            num2 = notificationsController3.pushDialogs.get(j2);
            if (num2 == null) {
            }
            dialogKey3 = dialogKey2;
            if (!dialogKey3.story) {
            }
            if (max > 1) {
            }
            person = (Person) longSparseArray.get(clientUserId);
            if (Build.VERSION.SDK_INT >= 28) {
            }
            str8 = "currentAccount";
            str9 = string;
            if (messageObject2 == null) {
            }
            String str17222222222 = "";
            if (person == null) {
            }
            messagingStyle = new NotificationCompat.MessagingStyle("");
            messagingStyle2 = messagingStyle;
            i15 = Build.VERSION.SDK_INT;
            if (i15 >= 28) {
            }
            messagingStyle2.setConversationTitle(format);
            messagingStyle2.setGroupConversation(i15 >= 28 || (!z9 && DialogObject.isChatDialog(j2)) || UserObject.isReplyUser(j2));
            StringBuilder sb3222222222 = new StringBuilder();
            String[] strArr2222222222 = new String[1];
            action = build;
            boolean[] zArr222222222 = new boolean[1];
            if (!dialogKey3.story) {
            }
            Intent intent2222222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
            intent2222222222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
            intent2222222222.setFlags(ConnectionsManager.FileTypeFile);
            intent2222222222.addCategory("android.intent.category.LAUNCHER");
            dialogKey5 = dialogKey4;
            if (!dialogKey5.story) {
            }
            FileLog.d("show extra notifications chatId " + j2 + " topicId " + i11);
            if (i11 != 0) {
            }
            String str18222222222 = str8;
            intent2222222222.putExtra(str18222222222, notificationsController3.currentAccount);
            PendingIntent activity222222222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2222222222, 1140850688);
            NotificationCompat.WearableExtender wearableExtender222222222 = new NotificationCompat.WearableExtender();
            if (action != null) {
            }
            int i31222222222 = i11;
            Intent intent3222222222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
            intent3222222222.addFlags(32);
            intent3222222222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
            intent3222222222.putExtra("dialog_id", j2);
            int i32222222222 = i14;
            intent3222222222.putExtra(str7, i32222222222);
            intent3222222222.putExtra(str18222222222, notificationsController3.currentAccount);
            int i33222222222 = i16;
            arrayList11 = arrayList7;
            bitmap3 = bitmap2;
            build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent3222222222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
            if (DialogObject.isEncryptedDialog(j2)) {
            }
            if (str16 == null) {
            }
            StringBuilder sb4222222222 = new StringBuilder();
            sb4222222222.append("tgaccount");
            long j9222222222 = j4;
            sb4222222222.append(j9222222222);
            wearableExtender222222222.setBridgeTag(sb4222222222.toString());
            if (!dialogKey5.story) {
            }
            NotificationCompat.Builder autoCancel222222222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str10).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
            if (dialogKey5.story) {
            }
            category = autoCancel222222222.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j6).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity222222222).extend(wearableExtender222222222).setSortKey(String.valueOf(Long.MAX_VALUE - j6)).setCategory("msg");
            Intent intent4222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
            intent4222222222.putExtra("messageDate", i13);
            intent4222222222.putExtra("dialogId", j2);
            intent4222222222.putExtra(str18222222222, notificationsController3.currentAccount);
            if (dialogKey5.story) {
            }
            category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent4222222222, 167772160));
            if (z6) {
            }
            if (action != null) {
            }
            if (!z5) {
            }
            if (arrayList4.size() != 1) {
            }
            if (DialogObject.isEncryptedDialog(j2)) {
            }
            if (bitmap3 != null) {
            }
            if (!AndroidUtilities.needShowPasscode(false)) {
            }
            if (tLRPC$Chat2 == null) {
            }
            tLRPC$User4 = tLRPC$User3;
            if (Build.VERSION.SDK_INT >= 26) {
            }
            j3 = j5;
            longSparseArray6 = longSparseArray7;
            z10 = z6;
            notification = notification22;
            arrayList6 = arrayList2;
            arrayList6.add(new 1NotificationHolder(num.intValue(), j2, dialogKey5.story, i31222222222, str10, tLRPC$User4, tLRPC$Chat2, category, i, str2, jArr, i2, uri, i3, z, z2, z3, i4));
            notificationsController2 = this;
            notificationsController2.wearNotificationsIds.put(j2, num);
            i6 = i9 + 1;
            arrayList17 = arrayList6;
            size = i10;
            arrayList14 = arrayList4;
            longSparseArray11 = longSparseArray4;
            clientUserId = j3;
            z4 = z10;
            longSparseArray12 = longSparseArray5;
            longSparseArray = longSparseArray6;
            build3 = notification;
            i5 = 7;
            notificationsController3 = notificationsController2;
        }
        LongSparseArray longSparseArray142 = longSparseArray;
        longSparseArray2 = longSparseArray12;
        Notification notification32 = build3;
        NotificationsController notificationsController42 = notificationsController3;
        ArrayList arrayList232 = arrayList17;
        if (!z4) {
        }
        i7 = 0;
        while (i7 < longSparseArray2.size()) {
        }
        ArrayList arrayList242 = new ArrayList(arrayList.size());
        size2 = arrayList.size();
        i8 = 0;
        while (i8 < size2) {
        }
    }

    /* loaded from: classes.dex */
    class 1NotificationHolder {
        TLRPC$Chat chat;
        long dialogId;
        int id;
        String name;
        NotificationCompat.Builder notification;
        boolean story;
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

        1NotificationHolder(int i, long j, boolean z, int i2, String str, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, NotificationCompat.Builder builder, int i3, String str2, long[] jArr, int i4, Uri uri, int i5, boolean z2, boolean z3, boolean z4, int i6) {
            this.val$lastTopicId = i3;
            this.val$chatName = str2;
            this.val$vibrationPattern = jArr;
            this.val$ledColor = i4;
            this.val$sound = uri;
            this.val$importance = i5;
            this.val$isDefault = z2;
            this.val$isInApp = z3;
            this.val$isSilent = z4;
            this.val$chatType = i6;
            this.id = i;
            this.name = str;
            this.user = tLRPC$User;
            this.chat = tLRPC$Chat;
            this.notification = builder;
            this.dialogId = j;
            this.story = z;
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
    public static /* synthetic */ void lambda$showExtraNotifications$40(Uri uri, File file) {
        ApplicationLoader.applicationContext.revokeUriPermission(uri, 1);
        if (file != null) {
            file.delete();
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
                builder.setIcon(IconCompat.createWithBitmap(ImageDecoder.decodeBitmap(ImageDecoder.createSource(file), NotificationsController$$ExternalSyntheticLambda0.INSTANCE)));
            } catch (Throwable unused) {
            }
        }
        return builder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadRoundAvatar$42(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
        imageDecoder.setPostProcessor(NotificationsController$$ExternalSyntheticLambda1.INSTANCE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$loadRoundAvatar$41(Canvas canvas) {
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
                                        textPaint3.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$playOutChatSound$44();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playOutChatSound$44() {
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
    public static /* synthetic */ void lambda$playOutChatSound$43(SoundPool soundPool, int i, int i2) {
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
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.notificationsSettingsUpdated, new Object[0]);
        }
        if (DialogObject.isEncryptedDialog(j)) {
            return;
        }
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        TLRPC$TL_account_updateNotifySettings tLRPC$TL_account_updateNotifySettings = new TLRPC$TL_account_updateNotifySettings();
        tLRPC$TL_account_updateNotifySettings.settings = new TLRPC$TL_inputPeerNotifySettings();
        String sharedPrefKey = getSharedPrefKey(j, i);
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
        int i2 = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, i), -1);
        if (i2 != -1) {
            TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings4 = tLRPC$TL_account_updateNotifySettings.settings;
            tLRPC$TL_inputPeerNotifySettings4.flags |= 4;
            if (i2 == 3) {
                tLRPC$TL_inputPeerNotifySettings4.mute_until = notificationsSettings.getInt(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + getSharedPrefKey(j, i), 0);
            } else {
                tLRPC$TL_inputPeerNotifySettings4.mute_until = i2 == 2 ? ConnectionsManager.DEFAULT_DATACENTER_ID : 0;
            }
        }
        long j2 = notificationsSettings.getLong("sound_document_id_" + getSharedPrefKey(j, i), 0L);
        String string = notificationsSettings.getString("sound_path_" + getSharedPrefKey(j, i), null);
        TLRPC$TL_inputPeerNotifySettings tLRPC$TL_inputPeerNotifySettings5 = tLRPC$TL_account_updateNotifySettings.settings;
        tLRPC$TL_inputPeerNotifySettings5.flags = tLRPC$TL_inputPeerNotifySettings5.flags | 8;
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
            tLRPC$TL_inputPeerNotifySettings5.sound = new TLRPC$TL_notificationSoundDefault();
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
        getConnectionsManager().sendRequest(tLRPC$TL_account_updateNotifySettings, NotificationsController$$ExternalSyntheticLambda49.INSTANCE);
    }

    public void updateServerNotificationsSettings(int i) {
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
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
        getConnectionsManager().sendRequest(tLRPC$TL_account_updateNotifySettings, NotificationsController$$ExternalSyntheticLambda48.INSTANCE);
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
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$loadTopicsNotificationsExceptions$48(j, consumer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadTopicsNotificationsExceptions$48(long j, final Consumer consumer) {
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
                NotificationsController.lambda$loadTopicsNotificationsExceptions$47(Consumer.this, hashSet);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadTopicsNotificationsExceptions$47(Consumer consumer, HashSet hashSet) {
        if (consumer != null) {
            consumer.accept(hashSet);
        }
    }

    /* loaded from: classes.dex */
    private static class DialogKey {
        final long dialogId;
        final boolean story;
        final int topicId;

        private DialogKey(long j, int i, boolean z) {
            this.dialogId = j;
            this.topicId = i;
            this.story = z;
        }
    }

    /* loaded from: classes.dex */
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
        AndroidUtilities.cancelRunOnUIThread(this.checkStoryPushesRunnable);
        long currentTimeMillis = j - System.currentTimeMillis();
        if (j != Long.MAX_VALUE) {
            AndroidUtilities.runOnUIThread(this.checkStoryPushesRunnable, Math.max(0L, currentTimeMillis));
        }
    }
}
