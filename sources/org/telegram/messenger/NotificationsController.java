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
        this.openedTopicId = 0L;
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
            boolean isGlobalNotificationsEnabled = getInstance(this.currentAccount).isGlobalNotificationsEnabled(j);
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda28
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

    /* JADX WARN: Code restructure failed: missing block: B:20:0x005a, code lost:
        if (r0 == 2) goto L30;
     */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0074  */
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
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserJoined) == false) goto L20;
     */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0141  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0143  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x014f  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0156  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0184  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01c6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processNewMessages$23(ArrayList arrayList, final ArrayList arrayList2, boolean z, boolean z2, CountDownLatch countDownLatch) {
        boolean z3;
        int i;
        int i2;
        Integer num;
        boolean z4;
        long j;
        boolean z5;
        boolean z6;
        MessageObject messageObject;
        int i3;
        boolean z7;
        MessageObject messageObject2;
        long j2;
        SparseArray<MessageObject> sparseArray;
        long j3;
        int i4;
        long j4;
        long j5;
        long j6;
        boolean z8;
        boolean z9;
        long j7;
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
                    Collections.sort(this.storyPushMessages, Comparator$-CC.comparingLong(NotificationsController$$ExternalSyntheticLambda47.INSTANCE));
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
                        long j8 = messageObject4.messageOwner.peer_id.channel_id;
                        long j9 = j8 == 0 ? -j8 : 0L;
                        SparseArray<MessageObject> sparseArray3 = this.pushMessagesDict.get(j9);
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
                                            this.pushMessagesDict.put(j9, sparseArray3);
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
                                    if (dialogId2 != this.openedDialogId || !ApplicationLoader.isScreenOn) {
                                        TLRPC$Message tLRPC$Message3 = messageObject4.messageOwner;
                                        if (!tLRPC$Message3.mentioned) {
                                            j2 = dialogId2;
                                        } else if (z4 || !(tLRPC$Message3.action instanceof TLRPC$TL_messageActionPinMessage)) {
                                            j2 = messageObject4.getFromChatId();
                                        }
                                        if (isPersonalMessage(messageObject4)) {
                                            this.personalCount++;
                                        }
                                        DialogObject.isChatDialog(j2);
                                        int indexOfKey = longSparseArray.indexOfKey(j2);
                                        if (indexOfKey >= 0 && topicId == 0) {
                                            z9 = ((Boolean) longSparseArray.valueAt(indexOfKey)).booleanValue();
                                            sparseArray = sparseArray3;
                                            i4 = i5;
                                            j3 = dialogId2;
                                            j4 = j10;
                                            j5 = j9;
                                            j6 = j2;
                                        } else {
                                            sparseArray = sparseArray3;
                                            j3 = dialogId2;
                                            long j12 = j2;
                                            i4 = i5;
                                            j4 = j10;
                                            j5 = j9;
                                            int notifyOverride = getNotifyOverride(notificationsSettings, j2, topicId);
                                            if (notifyOverride == -1) {
                                                j6 = j12;
                                                z8 = isGlobalNotificationsEnabled(j6, Boolean.valueOf(z5));
                                            } else {
                                                j6 = j12;
                                                z8 = notifyOverride != 2;
                                            }
                                            z9 = z8;
                                            longSparseArray.put(j6, Boolean.valueOf(z9));
                                        }
                                        if (z9) {
                                            if (z) {
                                                j7 = j6;
                                                i5 = i4;
                                            } else {
                                                j7 = j6;
                                                i5 = addToPopupMessages(arrayList2, messageObject4, j6, z5, notificationsSettings);
                                            }
                                            if (!z13) {
                                                z13 = messageObject4.messageOwner.from_scheduled;
                                            }
                                            this.delayedPushMessages.add(messageObject4);
                                            appendMessage(messageObject4);
                                            if (id2 != 0) {
                                                if (sparseArray == null) {
                                                    sparseArray2 = new SparseArray<>();
                                                    this.pushMessagesDict.put(j5, sparseArray2);
                                                } else {
                                                    sparseArray2 = sparseArray;
                                                }
                                                sparseArray2.put(id2, messageObject4);
                                            } else if (j4 != 0) {
                                                this.fcmRandomMessagesDict.put(j4, messageObject4);
                                            }
                                            if (j3 != j7) {
                                                long j13 = j3;
                                                Integer num2 = this.pushDialogsOverrideMention.get(j13);
                                                this.pushDialogsOverrideMention.put(j13, Integer.valueOf(num2 == null ? 1 : num2.intValue() + 1));
                                            }
                                        } else {
                                            j7 = j6;
                                            i5 = i4;
                                        }
                                        if (messageObject4.isReactionPush) {
                                            SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
                                            sparseBooleanArray.put(id2, true);
                                            getMessagesController().checkUnreadReactions(j7, topicId, sparseBooleanArray);
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
                    long j82 = messageObject4.messageOwner.peer_id.channel_id;
                    if (j82 == 0) {
                    }
                    SparseArray<MessageObject> sparseArray32 = this.pushMessagesDict.get(j9);
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$processNewMessages$21(arrayList2, i7);
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
                    z3 = isGlobalNotificationsEnabled(dialogId3, valueOf);
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
        if (z14) {
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
    /* JADX WARN: Removed duplicated region for block: B:18:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0074 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00a0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00c8  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0136  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processDialogsUpdateRead$26(LongSparseIntArray longSparseIntArray, final ArrayList arrayList) {
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
                            z2 = isGlobalNotificationsEnabled(keyAt);
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
                                if (!messageObject.messageOwner.from_scheduled && messageObject.getDialogId() == keyAt) {
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda37
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$processDialogsUpdateRead$24(arrayList);
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
        int i;
        long j;
        long j2;
        boolean z;
        boolean z2;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        SharedPreferences sharedPreferences;
        MessageObject messageObject;
        SparseArray<MessageObject> sparseArray;
        long j3;
        long j4;
        int i2;
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
        long j5 = 0;
        if (arrayList3 != null) {
            int i3 = 0;
            while (i3 < arrayList.size()) {
                TLRPC$Message tLRPC$Message2 = (TLRPC$Message) arrayList3.get(i3);
                if (tLRPC$Message2 != null && ((tLRPC$MessageFwdHeader = tLRPC$Message2.fwd_from) == null || !tLRPC$MessageFwdHeader.imported)) {
                    TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message2.action;
                    if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetMessagesTTL) && (!tLRPC$Message2.silent || (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp) && !(tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserJoined)))) {
                        long j6 = tLRPC$Message2.peer_id.channel_id;
                        long j7 = j6 != j5 ? -j6 : j5;
                        SparseArray<MessageObject> sparseArray3 = this.pushMessagesDict.get(j7);
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
                                i2 = i3;
                                j3 = dialogId;
                                j4 = j7;
                                tLRPC$Message = tLRPC$Message2;
                            } else {
                                messageObject = messageObject2;
                                sparseArray = sparseArray3;
                                j3 = dialogId;
                                j4 = j7;
                                i2 = i3;
                                tLRPC$Message = tLRPC$Message2;
                                int notifyOverride = getNotifyOverride(sharedPreferences, fromChatId, topicId);
                                if (notifyOverride == -1) {
                                    z3 = isGlobalNotificationsEnabled(fromChatId);
                                } else {
                                    z3 = notifyOverride != 2;
                                }
                                z4 = z3;
                                longSparseArray2.put(fromChatId, Boolean.valueOf(z4));
                            }
                            if (z4 && (fromChatId != this.openedDialogId || !ApplicationLoader.isScreenOn)) {
                                if (sparseArray == null) {
                                    sparseArray2 = new SparseArray<>();
                                    this.pushMessagesDict.put(j4, sparseArray2);
                                } else {
                                    sparseArray2 = sparseArray;
                                }
                                sparseArray2.put(tLRPC$Message.id, messageObject);
                                appendMessage(messageObject);
                                if (j3 != fromChatId) {
                                    long j8 = j3;
                                    Integer num = this.pushDialogsOverrideMention.get(j8);
                                    this.pushDialogsOverrideMention.put(j8, Integer.valueOf(num == null ? 1 : num.intValue() + 1));
                                }
                            }
                            i3 = i2 + 1;
                            arrayList3 = arrayList;
                            notificationsSettings = sharedPreferences;
                            z5 = false;
                            j5 = 0;
                        }
                    }
                }
                i2 = i3;
                sharedPreferences = notificationsSettings;
                i3 = i2 + 1;
                arrayList3 = arrayList;
                notificationsSettings = sharedPreferences;
                z5 = false;
                j5 = 0;
            }
        }
        SharedPreferences sharedPreferences2 = notificationsSettings;
        for (int i4 = 0; i4 < longSparseArray.size(); i4++) {
            long keyAt = longSparseArray.keyAt(i4);
            int indexOfKey2 = longSparseArray2.indexOfKey(keyAt);
            if (indexOfKey2 >= 0) {
                z2 = ((Boolean) longSparseArray2.valueAt(indexOfKey2)).booleanValue();
            } else {
                int notifyOverride2 = getNotifyOverride(sharedPreferences2, keyAt, 0L);
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
        ArrayList arrayList4 = arrayList2;
        if (arrayList4 != null) {
            int i5 = 0;
            while (i5 < arrayList2.size()) {
                MessageObject messageObject3 = (MessageObject) arrayList4.get(i5);
                int id = messageObject3.getId();
                if (this.pushMessagesDict.indexOfKey(id) >= 0) {
                    i = i5;
                } else {
                    if (isPersonalMessage(messageObject3)) {
                        this.personalCount++;
                    }
                    long dialogId2 = messageObject3.getDialogId();
                    long topicId2 = MessageObject.getTopicId(this.currentAccount, messageObject3.messageOwner, getMessagesController().isForum(messageObject3));
                    TLRPC$Message tLRPC$Message3 = messageObject3.messageOwner;
                    long j9 = tLRPC$Message3.random_id;
                    long fromChatId2 = tLRPC$Message3.mentioned ? messageObject3.getFromChatId() : dialogId2;
                    int indexOfKey3 = longSparseArray2.indexOfKey(fromChatId2);
                    if (indexOfKey3 >= 0 && topicId2 == 0) {
                        i = i5;
                        j = j9;
                        j2 = fromChatId2;
                        z = ((Boolean) longSparseArray2.valueAt(indexOfKey3)).booleanValue();
                    } else {
                        long j10 = fromChatId2;
                        i = i5;
                        j = j9;
                        int notifyOverride3 = getNotifyOverride(sharedPreferences2, j10, topicId2);
                        if (notifyOverride3 == -1) {
                            j2 = j10;
                            z = isGlobalNotificationsEnabled(j2);
                        } else {
                            j2 = j10;
                            z = notifyOverride3 != 2;
                        }
                        longSparseArray2.put(j2, Boolean.valueOf(z));
                    }
                    if (z && (j2 != this.openedDialogId || !ApplicationLoader.isScreenOn)) {
                        if (id != 0) {
                            long j11 = messageObject3.messageOwner.peer_id.channel_id;
                            long j12 = j11 != 0 ? -j11 : 0L;
                            SparseArray<MessageObject> sparseArray4 = this.pushMessagesDict.get(j12);
                            if (sparseArray4 == null) {
                                sparseArray4 = new SparseArray<>();
                                this.pushMessagesDict.put(j12, sparseArray4);
                            }
                            sparseArray4.put(id, messageObject3);
                        } else if (j != 0) {
                            this.fcmRandomMessagesDict.put(j, messageObject3);
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
                        i5 = i + 1;
                        arrayList4 = arrayList2;
                    }
                }
                i5 = i + 1;
                arrayList4 = arrayList2;
            }
        }
        if (collection != null) {
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                StoryNotification storyNotification = (StoryNotification) it.next();
                long j13 = storyNotification.dialogId;
                StoryNotification storyNotification2 = this.storyPushMessagesDict.get(j13);
                if (storyNotification2 != null) {
                    storyNotification2.dateByIds.putAll(storyNotification.dateByIds);
                } else {
                    this.storyPushMessages.add(storyNotification);
                    this.storyPushMessagesDict.put(j13, storyNotification);
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
        if (r12.getBoolean("EnablePreviewAll", true) == false) goto L809;
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
        r1 = org.telegram.messenger.LocaleController.formatString("formatDateAtTime", org.telegram.messenger.R.string.formatDateAtTime, org.telegram.messenger.LocaleController.getInstance().formatterYear.format(r26.messageOwner.date * 1000), org.telegram.messenger.LocaleController.getInstance().formatterDay.format(r26.messageOwner.date * 1000));
        r2 = org.telegram.messenger.R.string.NotificationUnrecognizedDevice;
        r0 = r26.messageOwner.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x0324, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationUnrecognizedDevice", r2, getUserConfig().getCurrentUser().first_name, r1, r0.title, r0.address);
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x0327, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGameScore) != false) goto L671;
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
    /* JADX WARN: Code restructure failed: missing block: B:290:0x054c, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGiftCode) == false) goto L265;
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x0556, code lost:
        return org.telegram.messenger.LocaleController.getString("BoostingReceivedGiftNoName", org.telegram.messenger.R.string.BoostingReceivedGiftNoName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x0559, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink) == false) goto L269;
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x056f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.R.string.NotificationInvitedToGroupByLink, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x0576, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle) == false) goto L273;
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x0588, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.R.string.NotificationEditedGroupName, r7, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x058b, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto) != false) goto L655;
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x058f, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto) == false) goto L277;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x0595, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser) == false) goto L292;
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x0597, code lost:
        r1 = r2.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x059b, code lost:
        if (r1 != r19) goto L283;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x05b0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.R.string.NotificationGroupKickYou, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x05b6, code lost:
        if (r1 != r9) goto L287;
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x05c8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.R.string.NotificationGroupLeftMember, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x05c9, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r26.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x05db, code lost:
        if (r0 != null) goto L290;
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x05dd, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x05f9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.R.string.NotificationGroupKickMember, r7, r8.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x05fc, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatCreate) == false) goto L296;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x0604, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x0607, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate) == false) goto L300;
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x060f, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x0612, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo) == false) goto L304;
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x0624, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x0629, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) == false) goto L308;
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x0639, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r2.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x063c, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken) == false) goto L312;
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x0644, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x0647, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPinMessage) == false) goto L634;
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x064d, code lost:
        if (r8 == null) goto L424;
     */
    /* JADX WARN: Code restructure failed: missing block: B:347:0x0653, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r8) == false) goto L320;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x0657, code lost:
        if (r8.megagroup == false) goto L424;
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x0659, code lost:
        r0 = r26.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x065b, code lost:
        if (r0 != null) goto L324;
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x0670, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x0678, code lost:
        if (r0.isMusic() == false) goto L328;
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x068a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.R.string.NotificationActionPinnedMusic, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x0691, code lost:
        if (r0.isVideo() == false) goto L338;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x0697, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L336;
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x06a1, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L336;
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x06ca, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "📹 " + r0.messageOwner.message, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x06de, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.R.string.NotificationActionPinnedVideo, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:369:0x06e3, code lost:
        if (r0.isGif() == false) goto L348;
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x06e9, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L346;
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x06f3, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L346;
     */
    /* JADX WARN: Code restructure failed: missing block: B:375:0x071c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "🎬 " + r0.messageOwner.message, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:377:0x0730, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.R.string.NotificationActionPinnedGif, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x0738, code lost:
        if (r0.isVoice() == false) goto L352;
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x074a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.R.string.NotificationActionPinnedVoice, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x074f, code lost:
        if (r0.isRoundVideo() == false) goto L356;
     */
    /* JADX WARN: Code restructure failed: missing block: B:385:0x0761, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.R.string.NotificationActionPinnedRound, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:387:0x0766, code lost:
        if (r0.isSticker() != false) goto L418;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x076c, code lost:
        if (r0.isAnimatedSticker() == false) goto L360;
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x0770, code lost:
        r3 = r0.messageOwner;
        r5 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x0776, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L370;
     */
    /* JADX WARN: Code restructure failed: missing block: B:394:0x077c, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L368;
     */
    /* JADX WARN: Code restructure failed: missing block: B:396:0x0784, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L368;
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:0x07ad, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "📎 " + r0.messageOwner.message, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x07c1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.R.string.NotificationActionPinnedFile, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:402:0x07c4, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L416;
     */
    /* JADX WARN: Code restructure failed: missing block: B:404:0x07c8, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L374;
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x07ce, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L378;
     */
    /* JADX WARN: Code restructure failed: missing block: B:409:0x07e3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.R.string.NotificationActionPinnedGeoLive, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x07e8, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L382;
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x07ea, code lost:
        r5 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x0808, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.R.string.NotificationActionPinnedContact2, r7, r8.title, org.telegram.messenger.ContactsController.formatName(r5.first_name, r5.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:415:0x080b, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L390;
     */
    /* JADX WARN: Code restructure failed: missing block: B:416:0x080d, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r5).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x0813, code lost:
        if (r0.quiz == false) goto L388;
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x082d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.R.string.NotificationActionPinnedQuiz2, r7, r8.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x0846, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.R.string.NotificationActionPinnedPoll2, r7, r8.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x0849, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L400;
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x084f, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L398;
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x0857, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L398;
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x0880, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r7, "🖼 " + r0.messageOwner.message, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x0894, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.R.string.NotificationActionPinnedPhoto, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:433:0x089a, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L404;
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x08ac, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.R.string.NotificationActionPinnedGame, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:436:0x08ad, code lost:
        r3 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x08af, code lost:
        if (r3 == null) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x08b5, code lost:
        if (r3.length() <= 0) goto L414;
     */
    /* JADX WARN: Code restructure failed: missing block: B:440:0x08b7, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:441:0x08bd, code lost:
        if (r0.length() <= 20) goto L413;
     */
    /* JADX WARN: Code restructure failed: missing block: B:442:0x08bf, code lost:
        r3 = new java.lang.StringBuilder();
        r5 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:443:0x08d4, code lost:
        r5 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:444:0x08d5, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedText;
        r2 = new java.lang.Object[3];
        r2[r5] = r7;
        r2[1] = r0;
        r2[2] = r8.title;
     */
    /* JADX WARN: Code restructure failed: missing block: B:445:0x08e8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:447:0x08fc, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:449:0x0910, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.R.string.NotificationActionPinnedGeo, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:450:0x0911, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:451:0x0917, code lost:
        if (r0 == null) goto L422;
     */
    /* JADX WARN: Code restructure failed: missing block: B:453:0x092d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmoji, r7, r8.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:455:0x093f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.R.string.NotificationActionPinnedSticker, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:0x0941, code lost:
        if (r8 == null) goto L530;
     */
    /* JADX WARN: Code restructure failed: missing block: B:458:0x0943, code lost:
        r0 = r26.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x0945, code lost:
        if (r0 != null) goto L430;
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x0956, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x095c, code lost:
        if (r0.isMusic() == false) goto L434;
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x096c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.R.string.NotificationActionPinnedMusicChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x0973, code lost:
        if (r0.isVideo() == false) goto L444;
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x0979, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L442;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00b3, code lost:
        if (r12.getBoolean("EnablePreviewGroup", true) != false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x0983, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L442;
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x09a9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r8.title, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:475:0x09ba, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.R.string.NotificationActionPinnedVideoChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x09bf, code lost:
        if (r0.isGif() == false) goto L454;
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x09c5, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L452;
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x09cf, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L452;
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x09f5, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r8.title, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0a06, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.R.string.NotificationActionPinnedGifChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:487:0x0a0d, code lost:
        if (r0.isVoice() == false) goto L458;
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x0a1d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.R.string.NotificationActionPinnedVoiceChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x0a22, code lost:
        if (r0.isRoundVideo() == false) goto L462;
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x0a32, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.R.string.NotificationActionPinnedRoundChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:0x0a37, code lost:
        if (r0.isSticker() != false) goto L524;
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x0a3d, code lost:
        if (r0.isAnimatedSticker() == false) goto L466;
     */
    /* JADX WARN: Code restructure failed: missing block: B:499:0x0a41, code lost:
        r3 = r0.messageOwner;
        r5 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:500:0x0a47, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L476;
     */
    /* JADX WARN: Code restructure failed: missing block: B:502:0x0a4d, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L474;
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0a55, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L474;
     */
    /* JADX WARN: Code restructure failed: missing block: B:506:0x0a7b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r8.title, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x0a8c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.R.string.NotificationActionPinnedFileChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x0a8f, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L522;
     */
    /* JADX WARN: Code restructure failed: missing block: B:512:0x0a93, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L480;
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x0a99, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L484;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0aab, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:519:0x0aaf, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L488;
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x0ab1, code lost:
        r5 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x0acd, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.R.string.NotificationActionPinnedContactChannel2, r8.title, org.telegram.messenger.ContactsController.formatName(r5.first_name, r5.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x0ad0, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L496;
     */
    /* JADX WARN: Code restructure failed: missing block: B:524:0x0ad2, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r5).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:525:0x0ad8, code lost:
        if (r0.quiz == false) goto L494;
     */
    /* JADX WARN: Code restructure failed: missing block: B:527:0x0aef, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.R.string.NotificationActionPinnedQuizChannel2, r8.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x0b05, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.R.string.NotificationActionPinnedPollChannel2, r8.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00bf, code lost:
        if (r12.getBoolean("EnablePreviewChannel", r2) == false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x0b08, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L506;
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0b0e, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L504;
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0b16, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L504;
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0b3c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r8.title, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0b4d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.R.string.NotificationActionPinnedPhotoChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x0b52, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L510;
     */
    /* JADX WARN: Code restructure failed: missing block: B:543:0x0b62, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.R.string.NotificationActionPinnedGameChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:544:0x0b63, code lost:
        r3 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x0b65, code lost:
        if (r3 == null) goto L520;
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0b6b, code lost:
        if (r3.length() <= 0) goto L520;
     */
    /* JADX WARN: Code restructure failed: missing block: B:548:0x0b6d, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0b73, code lost:
        if (r0.length() <= 20) goto L519;
     */
    /* JADX WARN: Code restructure failed: missing block: B:550:0x0b75, code lost:
        r3 = new java.lang.StringBuilder();
        r9 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:551:0x0b8a, code lost:
        r9 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:552:0x0b8b, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextChannel;
        r2 = new java.lang.Object[2];
        r2[r9] = r8.title;
        r2[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:553:0x0b9b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:555:0x0bac, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:557:0x0bbd, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:558:0x0bbe, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:559:0x0bc3, code lost:
        if (r0 == null) goto L528;
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0bd7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiChannel, r8.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:563:0x0be7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerChannel, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:564:0x0be8, code lost:
        r0 = r26.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:565:0x0beb, code lost:
        if (r0 != null) goto L534;
     */
    /* JADX WARN: Code restructure failed: missing block: B:567:0x0bf9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.R.string.NotificationActionPinnedNoTextUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x0bfe, code lost:
        if (r0.isMusic() == false) goto L538;
     */
    /* JADX WARN: Code restructure failed: missing block: B:571:0x0c0c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicUser", org.telegram.messenger.R.string.NotificationActionPinnedMusicUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:573:0x0c13, code lost:
        if (r0.isVideo() == false) goto L548;
     */
    /* JADX WARN: Code restructure failed: missing block: B:575:0x0c19, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L546;
     */
    /* JADX WARN: Code restructure failed: missing block: B:577:0x0c23, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L546;
     */
    /* JADX WARN: Code restructure failed: missing block: B:579:0x0c47, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r7, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:581:0x0c56, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoUser", org.telegram.messenger.R.string.NotificationActionPinnedVideoUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:583:0x0c5b, code lost:
        if (r0.isGif() == false) goto L558;
     */
    /* JADX WARN: Code restructure failed: missing block: B:585:0x0c61, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L556;
     */
    /* JADX WARN: Code restructure failed: missing block: B:587:0x0c6b, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L556;
     */
    /* JADX WARN: Code restructure failed: missing block: B:589:0x0c8f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r7, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:591:0x0c9e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifUser", org.telegram.messenger.R.string.NotificationActionPinnedGifUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:593:0x0ca5, code lost:
        if (r0.isVoice() == false) goto L562;
     */
    /* JADX WARN: Code restructure failed: missing block: B:595:0x0cb3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceUser", org.telegram.messenger.R.string.NotificationActionPinnedVoiceUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:597:0x0cb8, code lost:
        if (r0.isRoundVideo() == false) goto L566;
     */
    /* JADX WARN: Code restructure failed: missing block: B:599:0x0cc6, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundUser", org.telegram.messenger.R.string.NotificationActionPinnedRoundUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:601:0x0ccb, code lost:
        if (r0.isSticker() != false) goto L628;
     */
    /* JADX WARN: Code restructure failed: missing block: B:603:0x0cd1, code lost:
        if (r0.isAnimatedSticker() == false) goto L570;
     */
    /* JADX WARN: Code restructure failed: missing block: B:605:0x0cd5, code lost:
        r3 = r0.messageOwner;
        r5 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:606:0x0cdb, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L580;
     */
    /* JADX WARN: Code restructure failed: missing block: B:608:0x0ce1, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L578;
     */
    /* JADX WARN: Code restructure failed: missing block: B:610:0x0ce9, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L578;
     */
    /* JADX WARN: Code restructure failed: missing block: B:612:0x0d0d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r7, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:614:0x0d1c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileUser", org.telegram.messenger.R.string.NotificationActionPinnedFileUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:616:0x0d1f, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L626;
     */
    /* JADX WARN: Code restructure failed: missing block: B:618:0x0d23, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L584;
     */
    /* JADX WARN: Code restructure failed: missing block: B:621:0x0d29, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L588;
     */
    /* JADX WARN: Code restructure failed: missing block: B:623:0x0d39, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveUser", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x0d3d, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L592;
     */
    /* JADX WARN: Code restructure failed: missing block: B:626:0x0d3f, code lost:
        r5 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:627:0x0d59, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactUser", org.telegram.messenger.R.string.NotificationActionPinnedContactUser, r7, org.telegram.messenger.ContactsController.formatName(r5.first_name, r5.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:629:0x0d5c, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L600;
     */
    /* JADX WARN: Code restructure failed: missing block: B:630:0x0d5e, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r5).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:0x0d64, code lost:
        if (r0.quiz == false) goto L598;
     */
    /* JADX WARN: Code restructure failed: missing block: B:633:0x0d79, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizUser", org.telegram.messenger.R.string.NotificationActionPinnedQuizUser, r7, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:635:0x0d8d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollUser", org.telegram.messenger.R.string.NotificationActionPinnedPollUser, r7, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:637:0x0d90, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L610;
     */
    /* JADX WARN: Code restructure failed: missing block: B:639:0x0d96, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L608;
     */
    /* JADX WARN: Code restructure failed: missing block: B:641:0x0d9e, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L608;
     */
    /* JADX WARN: Code restructure failed: missing block: B:643:0x0dc2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r7, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:645:0x0dd1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoUser", org.telegram.messenger.R.string.NotificationActionPinnedPhotoUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:647:0x0dd6, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L614;
     */
    /* JADX WARN: Code restructure failed: missing block: B:649:0x0de4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameUser", org.telegram.messenger.R.string.NotificationActionPinnedGameUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:650:0x0de5, code lost:
        r3 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:651:0x0de7, code lost:
        if (r3 == null) goto L624;
     */
    /* JADX WARN: Code restructure failed: missing block: B:653:0x0ded, code lost:
        if (r3.length() <= 0) goto L624;
     */
    /* JADX WARN: Code restructure failed: missing block: B:654:0x0def, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:655:0x0df5, code lost:
        if (r0.length() <= 20) goto L623;
     */
    /* JADX WARN: Code restructure failed: missing block: B:656:0x0df7, code lost:
        r3 = new java.lang.StringBuilder();
        r5 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:657:0x0e0c, code lost:
        r5 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:658:0x0e0d, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextUser;
        r2 = new java.lang.Object[2];
        r2[r5] = r7;
        r2[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:659:0x0e1b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:661:0x0e2a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.R.string.NotificationActionPinnedNoTextUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:663:0x0e39, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoUser", org.telegram.messenger.R.string.NotificationActionPinnedGeoUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:664:0x0e3a, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:665:0x0e40, code lost:
        if (r0 == null) goto L632;
     */
    /* JADX WARN: Code restructure failed: missing block: B:667:0x0e51, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiUser", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiUser, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:669:0x0e5e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerUser", org.telegram.messenger.R.string.NotificationActionPinnedStickerUser, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:671:0x0e61, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) == false) goto L650;
     */
    /* JADX WARN: Code restructure failed: missing block: B:672:0x0e63, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) r2).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:673:0x0e6b, code lost:
        if (android.text.TextUtils.isEmpty(r0) == false) goto L644;
     */
    /* JADX WARN: Code restructure failed: missing block: B:675:0x0e6f, code lost:
        if (r3 != r19) goto L642;
     */
    /* JADX WARN: Code restructure failed: missing block: B:679:0x0e93, code lost:
        if (r3 != r19) goto L648;
     */
    /* JADX WARN: Code restructure failed: missing block: B:682:0x0eb1, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.R.string.ChatThemeChangedTo, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:684:0x0eb4, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest) == false) goto L654;
     */
    /* JADX WARN: Code restructure failed: missing block: B:686:0x0ebc, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:687:0x0ebd, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:690:0x0ec7, code lost:
        if (r1.peer_id.channel_id == 0) goto L665;
     */
    /* JADX WARN: Code restructure failed: missing block: B:692:0x0ecb, code lost:
        if (r8.megagroup != false) goto L665;
     */
    /* JADX WARN: Code restructure failed: missing block: B:694:0x0ed1, code lost:
        if (r26.isVideoAvatar() == false) goto L663;
     */
    /* JADX WARN: Code restructure failed: missing block: B:696:0x0ee3, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.R.string.ChannelVideoEditNotification, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:698:0x0ef4, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.R.string.ChannelPhotoEditNotification, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:700:0x0efa, code lost:
        if (r26.isVideoAvatar() == false) goto L669;
     */
    /* JADX WARN: Code restructure failed: missing block: B:702:0x0f0e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.R.string.NotificationEditedGroupVideo, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:704:0x0f21, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.R.string.NotificationEditedGroupPhoto, r7, r8.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:706:0x0f28, code lost:
        return r26.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:708:0x0f37, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationContactJoined", org.telegram.messenger.R.string.NotificationContactJoined, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:710:0x0f3c, code lost:
        if (r26.isMediaEmpty() == false) goto L683;
     */
    /* JADX WARN: Code restructure failed: missing block: B:712:0x0f46, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageOwner.message) != false) goto L681;
     */
    /* JADX WARN: Code restructure failed: missing block: B:714:0x0f4c, code lost:
        return replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:716:0x0f55, code lost:
        return org.telegram.messenger.LocaleController.getString(r23, org.telegram.messenger.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:717:0x0f56, code lost:
        r1 = r23;
        r2 = r26.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:718:0x0f5e, code lost:
        if ((r2.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L697;
     */
    /* JADX WARN: Code restructure failed: missing block: B:720:0x0f64, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L691;
     */
    /* JADX WARN: Code restructure failed: missing block: B:722:0x0f6c, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L691;
     */
    /* JADX WARN: Code restructure failed: missing block: B:724:0x0f81, code lost:
        return "🖼 " + replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x0f88, code lost:
        if (r26.messageOwner.media.ttl_seconds == 0) goto L695;
     */
    /* JADX WARN: Code restructure failed: missing block: B:728:0x0f92, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDestructingPhoto", org.telegram.messenger.R.string.AttachDestructingPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:730:0x0f9b, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachPhoto", org.telegram.messenger.R.string.AttachPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:732:0x0fa0, code lost:
        if (r26.isVideo() == false) goto L715;
     */
    /* JADX WARN: Code restructure failed: missing block: B:734:0x0fa6, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L705;
     */
    /* JADX WARN: Code restructure failed: missing block: B:736:0x0fb0, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageOwner.message) != false) goto L705;
     */
    /* JADX WARN: Code restructure failed: missing block: B:738:0x0fc5, code lost:
        return "📹 " + replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:739:0x0fc6, code lost:
        r0 = r26.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:740:0x0fcc, code lost:
        if (r0.ttl_seconds == 0) goto L713;
     */
    /* JADX WARN: Code restructure failed: missing block: B:742:0x0fd0, code lost:
        if (r0.voice == false) goto L711;
     */
    /* JADX WARN: Code restructure failed: missing block: B:744:0x0fda, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDestructingVoice", org.telegram.messenger.R.string.AttachDestructingVoice);
     */
    /* JADX WARN: Code restructure failed: missing block: B:746:0x0fe3, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDestructingVideo", org.telegram.messenger.R.string.AttachDestructingVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:748:0x0fec, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachVideo", org.telegram.messenger.R.string.AttachVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:750:0x0ff1, code lost:
        if (r26.isGame() == false) goto L719;
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x0ffb, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachGame", org.telegram.messenger.R.string.AttachGame);
     */
    /* JADX WARN: Code restructure failed: missing block: B:754:0x1000, code lost:
        if (r26.isVoice() == false) goto L723;
     */
    /* JADX WARN: Code restructure failed: missing block: B:756:0x100a, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachAudio", org.telegram.messenger.R.string.AttachAudio);
     */
    /* JADX WARN: Code restructure failed: missing block: B:758:0x100f, code lost:
        if (r26.isRoundVideo() == false) goto L727;
     */
    /* JADX WARN: Code restructure failed: missing block: B:760:0x1019, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachRound", org.telegram.messenger.R.string.AttachRound);
     */
    /* JADX WARN: Code restructure failed: missing block: B:762:0x101e, code lost:
        if (r26.isMusic() == false) goto L731;
     */
    /* JADX WARN: Code restructure failed: missing block: B:764:0x1028, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachMusic", org.telegram.messenger.R.string.AttachMusic);
     */
    /* JADX WARN: Code restructure failed: missing block: B:765:0x1029, code lost:
        r2 = r26.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:766:0x102f, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L735;
     */
    /* JADX WARN: Code restructure failed: missing block: B:768:0x1039, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachContact", org.telegram.messenger.R.string.AttachContact);
     */
    /* JADX WARN: Code restructure failed: missing block: B:770:0x103c, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L743;
     */
    /* JADX WARN: Code restructure failed: missing block: B:772:0x1044, code lost:
        if (((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r2).poll.quiz == false) goto L741;
     */
    /* JADX WARN: Code restructure failed: missing block: B:774:0x104e, code lost:
        return org.telegram.messenger.LocaleController.getString("QuizPoll", org.telegram.messenger.R.string.QuizPoll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:776:0x1057, code lost:
        return org.telegram.messenger.LocaleController.getString("Poll", org.telegram.messenger.R.string.Poll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:778:0x105a, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) == false) goto L747;
     */
    /* JADX WARN: Code restructure failed: missing block: B:780:0x1064, code lost:
        return org.telegram.messenger.LocaleController.getString("BoostingGiveaway", org.telegram.messenger.R.string.BoostingGiveaway);
     */
    /* JADX WARN: Code restructure failed: missing block: B:782:0x1067, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveawayResults) == false) goto L751;
     */
    /* JADX WARN: Code restructure failed: missing block: B:784:0x1071, code lost:
        return org.telegram.messenger.LocaleController.getString("BoostingGiveawayResults", org.telegram.messenger.R.string.BoostingGiveawayResults);
     */
    /* JADX WARN: Code restructure failed: missing block: B:786:0x1074, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L807;
     */
    /* JADX WARN: Code restructure failed: missing block: B:788:0x1078, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L755;
     */
    /* JADX WARN: Code restructure failed: missing block: B:791:0x107e, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L759;
     */
    /* JADX WARN: Code restructure failed: missing block: B:793:0x1088, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachLiveLocation", org.telegram.messenger.R.string.AttachLiveLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:795:0x108b, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L789;
     */
    /* JADX WARN: Code restructure failed: missing block: B:797:0x1091, code lost:
        if (r26.isSticker() != false) goto L783;
     */
    /* JADX WARN: Code restructure failed: missing block: B:799:0x1097, code lost:
        if (r26.isAnimatedSticker() == false) goto L765;
     */
    /* JADX WARN: Code restructure failed: missing block: B:802:0x109e, code lost:
        if (r26.isGif() == false) goto L775;
     */
    /* JADX WARN: Code restructure failed: missing block: B:804:0x10a4, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L773;
     */
    /* JADX WARN: Code restructure failed: missing block: B:806:0x10ae, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageOwner.message) != false) goto L773;
     */
    /* JADX WARN: Code restructure failed: missing block: B:808:0x10c3, code lost:
        return "🎬 " + replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:810:0x10cc, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachGif", org.telegram.messenger.R.string.AttachGif);
     */
    /* JADX WARN: Code restructure failed: missing block: B:812:0x10d1, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L781;
     */
    /* JADX WARN: Code restructure failed: missing block: B:814:0x10db, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageOwner.message) != false) goto L781;
     */
    /* JADX WARN: Code restructure failed: missing block: B:816:0x10f0, code lost:
        return "📎 " + replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:818:0x10f9, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDocument", org.telegram.messenger.R.string.AttachDocument);
     */
    /* JADX WARN: Code restructure failed: missing block: B:819:0x10fa, code lost:
        r0 = r26.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:820:0x10fe, code lost:
        if (r0 == null) goto L787;
     */
    /* JADX WARN: Code restructure failed: missing block: B:822:0x111c, code lost:
        return r0 + " " + org.telegram.messenger.LocaleController.getString("AttachSticker", org.telegram.messenger.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:824:0x1125, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachSticker", org.telegram.messenger.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:826:0x1128, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaStory) == false) goto L801;
     */
    /* JADX WARN: Code restructure failed: missing block: B:828:0x112e, code lost:
        if (((org.telegram.tgnet.TLRPC$TL_messageMediaStory) r2).via_mention == false) goto L799;
     */
    /* JADX WARN: Code restructure failed: missing block: B:829:0x1130, code lost:
        r0 = org.telegram.messenger.R.string.StoryNotificationMention;
        r1 = new java.lang.Object[1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:830:0x1138, code lost:
        if (r27[0] != null) goto L798;
     */
    /* JADX WARN: Code restructure failed: missing block: B:831:0x113a, code lost:
        r3 = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:832:0x113d, code lost:
        r3 = r27[0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:833:0x113f, code lost:
        r1[0] = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:834:0x1147, code lost:
        return org.telegram.messenger.LocaleController.formatString("StoryNotificationMention", r0, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:836:0x1150, code lost:
        return org.telegram.messenger.LocaleController.getString("Story", org.telegram.messenger.R.string.Story);
     */
    /* JADX WARN: Code restructure failed: missing block: B:838:0x1157, code lost:
        if (android.text.TextUtils.isEmpty(r26.messageText) != false) goto L805;
     */
    /* JADX WARN: Code restructure failed: missing block: B:840:0x115d, code lost:
        return replaceSpoilers(r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:842:0x1164, code lost:
        return org.telegram.messenger.LocaleController.getString(r1, org.telegram.messenger.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:844:0x116d, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachLocation", org.telegram.messenger.R.string.AttachLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:858:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:859:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.R.string.ChatThemeDisabled, r7, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:860:?, code lost:
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

    /* JADX WARN: Code restructure failed: missing block: B:279:0x0665, code lost:
        if (r12.getBoolean("EnablePreviewGroup", true) == false) goto L825;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x0671, code lost:
        if (r12.getBoolean("EnablePreviewChannel", r2) != false) goto L259;
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x0673, code lost:
        r2 = r29.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x0677, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageService) == false) goto L604;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x0679, code lost:
        r1 = r2.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x067d, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser) == false) goto L302;
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x067f, code lost:
        r2 = r1.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x0685, code lost:
        if (r2 != 0) goto L268;
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x068e, code lost:
        if (r1.users.size() != 1) goto L268;
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x0690, code lost:
        r2 = r29.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x06a5, code lost:
        if (r2 == 0) goto L288;
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x06af, code lost:
        if (r29.messageOwner.peer_id.channel_id == 0) goto L275;
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x06b3, code lost:
        if (r5.megagroup != false) goto L275;
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x06b5, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.R.string.ChannelAddedByNotification, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x06cc, code lost:
        if (r2 != r18) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x06ce, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.R.string.NotificationInvitedToGroup, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x06e3, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x06ef, code lost:
        if (r0 != null) goto L281;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x06f1, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x06f7, code lost:
        if (r9 != r0.id) goto L287;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x06fb, code lost:
        if (r5.megagroup == false) goto L286;
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x06fd, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.R.string.NotificationGroupAddSelfMega, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x0712, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.R.string.NotificationGroupAddSelf, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x0727, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r8, r5.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x0743, code lost:
        r0 = new java.lang.StringBuilder();
        r1 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x0753, code lost:
        if (r1 >= r29.messageOwner.action.users.size()) goto L300;
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x0755, code lost:
        r2 = getMessagesController().getUser(r29.messageOwner.action.users.get(r1));
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x0769, code lost:
        if (r2 == null) goto L299;
     */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x076b, code lost:
        r2 = org.telegram.messenger.UserObject.getUserName(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x0773, code lost:
        if (r0.length() == 0) goto L296;
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x0775, code lost:
        r0.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x077a, code lost:
        r0.append(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x077d, code lost:
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x0780, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r8, r5.title, r0.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x079f, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCall) == false) goto L305;
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x07a1, code lost:
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.R.string.NotificationGroupCreatedCall, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x07b7, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled) == false) goto L308;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x07b9, code lost:
        r14 = r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x07c3, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall) == false) goto L338;
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x07c5, code lost:
        r2 = r1.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x07cb, code lost:
        if (r2 != 0) goto L315;
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x07d4, code lost:
        if (r1.users.size() != 1) goto L315;
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x07d6, code lost:
        r2 = r29.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x07eb, code lost:
        if (r2 == 0) goto L324;
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x07ef, code lost:
        if (r2 != r18) goto L320;
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x07f1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.R.string.NotificationGroupInvitedYouToCall, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:344:0x0806, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x0812, code lost:
        if (r0 != null) goto L323;
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x0814, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x0816, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r8, r5.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x0832, code lost:
        r0 = new java.lang.StringBuilder();
        r1 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x0842, code lost:
        if (r1 >= r29.messageOwner.action.users.size()) goto L336;
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x0844, code lost:
        r2 = getMessagesController().getUser(r29.messageOwner.action.users.get(r1));
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x0858, code lost:
        if (r2 == null) goto L335;
     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x085a, code lost:
        r2 = org.telegram.messenger.UserObject.getUserName(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x0862, code lost:
        if (r0.length() == 0) goto L332;
     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x0864, code lost:
        r0.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x0869, code lost:
        r0.append(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x086c, code lost:
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x086f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r8, r5.title, r0.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x088d, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGiftCode) == false) goto L349;
     */
    /* JADX WARN: Code restructure failed: missing block: B:362:0x088f, code lost:
        r1 = (org.telegram.tgnet.TLRPC$TL_messageActionGiftCode) r1;
        r0 = org.telegram.messenger.MessagesController.getInstance(r28.currentAccount).getChat(java.lang.Long.valueOf(-org.telegram.messenger.DialogObject.getPeerDialogId(r1.boost_peer)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x08a8, code lost:
        if (r0 != null) goto L348;
     */
    /* JADX WARN: Code restructure failed: missing block: B:364:0x08aa, code lost:
        r14 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x08ac, code lost:
        r14 = r0.title;
     */
    /* JADX WARN: Code restructure failed: missing block: B:366:0x08ae, code lost:
        if (r14 != null) goto L347;
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x08b0, code lost:
        r0 = org.telegram.messenger.LocaleController.getString("BoostingReceivedGiftNoName", org.telegram.messenger.R.string.BoostingReceivedGiftNoName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:368:0x08b9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGiftCode", org.telegram.messenger.R.string.NotificationMessageGiftCode, r14, org.telegram.messenger.LocaleController.formatPluralString("Months", r1.months, new java.lang.Object[0]));
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x08dc, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink) == false) goto L353;
     */
    /* JADX WARN: Code restructure failed: missing block: B:374:0x08f6, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle) == false) goto L357;
     */
    /* JADX WARN: Code restructure failed: missing block: B:377:0x090d, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto) != false) goto L588;
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x0911, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto) == false) goto L361;
     */
    /* JADX WARN: Code restructure failed: missing block: B:382:0x0917, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser) == false) goto L376;
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x0919, code lost:
        r0 = r1.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:384:0x091d, code lost:
        if (r0 != r18) goto L367;
     */
    /* JADX WARN: Code restructure failed: missing block: B:387:0x0939, code lost:
        if (r0 != r9) goto L371;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x094d, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r29.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:390:0x095f, code lost:
        if (r0 != null) goto L374;
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x0961, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:394:0x097f, code lost:
        r9 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:395:0x0982, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatCreate) == false) goto L380;
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:0x098e, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate) == false) goto L384;
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x099a, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo) == false) goto L388;
     */
    /* JADX WARN: Code restructure failed: missing block: B:404:0x09b2, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) == false) goto L392;
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x09c6, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken) == false) goto L396;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0097, code lost:
        if (r12.getBoolean("EnablePreviewGroup", true) != false) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:410:0x09d2, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPinMessage) == false) goto L568;
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x09da, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r5) == false) goto L485;
     */
    /* JADX WARN: Code restructure failed: missing block: B:414:0x09de, code lost:
        if (r5.megagroup == false) goto L402;
     */
    /* JADX WARN: Code restructure failed: missing block: B:416:0x09e2, code lost:
        r1 = r29.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x09e4, code lost:
        if (r1 != null) goto L406;
     */
    /* JADX WARN: Code restructure failed: missing block: B:420:0x09fe, code lost:
        if (r1.isMusic() == false) goto L409;
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x0a00, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.R.string.NotificationActionPinnedMusicChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x0a16, code lost:
        if (r1.isVideo() == false) goto L417;
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x0a1c, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L416;
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x0a26, code lost:
        if (android.text.TextUtils.isEmpty(r1.messageOwner.message) != false) goto L416;
     */
    /* JADX WARN: Code restructure failed: missing block: B:428:0x0a28, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "📹 " + r1.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x0a4e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.R.string.NotificationActionPinnedVideoChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x0a64, code lost:
        if (r1.isGif() == false) goto L425;
     */
    /* JADX WARN: Code restructure failed: missing block: B:433:0x0a6a, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L424;
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x0a74, code lost:
        if (android.text.TextUtils.isEmpty(r1.messageOwner.message) != false) goto L424;
     */
    /* JADX WARN: Code restructure failed: missing block: B:436:0x0a76, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "🎬 " + r1.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x0a9c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.R.string.NotificationActionPinnedGifChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x0ab4, code lost:
        if (r1.isVoice() == false) goto L428;
     */
    /* JADX WARN: Code restructure failed: missing block: B:440:0x0ab6, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.R.string.NotificationActionPinnedVoiceChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:442:0x0aca, code lost:
        if (r1.isRoundVideo() == false) goto L431;
     */
    /* JADX WARN: Code restructure failed: missing block: B:443:0x0acc, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.R.string.NotificationActionPinnedRoundChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:445:0x0ae0, code lost:
        if (r1.isSticker() != false) goto L481;
     */
    /* JADX WARN: Code restructure failed: missing block: B:447:0x0ae6, code lost:
        if (r1.isAnimatedSticker() == false) goto L435;
     */
    /* JADX WARN: Code restructure failed: missing block: B:449:0x0aea, code lost:
        r3 = r1.messageOwner;
        r7 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:450:0x0af0, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L443;
     */
    /* JADX WARN: Code restructure failed: missing block: B:452:0x0af6, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L442;
     */
    /* JADX WARN: Code restructure failed: missing block: B:454:0x0afe, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L442;
     */
    /* JADX WARN: Code restructure failed: missing block: B:455:0x0b00, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "📎 " + r1.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:456:0x0b28, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.R.string.NotificationActionPinnedFileChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:458:0x0b3c, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L480;
     */
    /* JADX WARN: Code restructure failed: missing block: B:460:0x0b40, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L447;
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0b46, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L450;
     */
    /* JADX WARN: Code restructure failed: missing block: B:464:0x0b48, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:466:0x0b5c, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L453;
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x0b5e, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r29.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.R.string.NotificationActionPinnedContactChannel2, r5.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x0b83, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L459;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00a5, code lost:
        if (r12.getBoolean("EnablePreviewChannel", r3) == false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:470:0x0b85, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r7).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x0b8b, code lost:
        if (r0.quiz == false) goto L458;
     */
    /* JADX WARN: Code restructure failed: missing block: B:472:0x0b8d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.R.string.NotificationActionPinnedQuizChannel2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x0ba4, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.R.string.NotificationActionPinnedPollChannel2, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:475:0x0bbd, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L467;
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x0bc3, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L466;
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x0bcb, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L466;
     */
    /* JADX WARN: Code restructure failed: missing block: B:480:0x0bcd, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r5.title, "🖼 " + r1.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x0bf3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.R.string.NotificationActionPinnedPhotoChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0c09, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L470;
     */
    /* JADX WARN: Code restructure failed: missing block: B:484:0x0c0b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.R.string.NotificationActionPinnedGameChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0c1b, code lost:
        r0 = r1.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:486:0x0c1d, code lost:
        if (r0 == null) goto L479;
     */
    /* JADX WARN: Code restructure failed: missing block: B:488:0x0c23, code lost:
        if (r0.length() <= 0) goto L479;
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x0c25, code lost:
        r0 = r1.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:490:0x0c2b, code lost:
        if (r0.length() <= 20) goto L478;
     */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x0c2d, code lost:
        r1 = new java.lang.StringBuilder();
        r9 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x0c44, code lost:
        r9 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x0c45, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextChannel;
        r2 = new java.lang.Object[2];
        r2[r9] = r5.title;
        r2[1] = r0;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:494:0x0c57, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:0x0c69, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:496:0x0c7b, code lost:
        r0 = r1.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x0c80, code lost:
        if (r0 == null) goto L484;
     */
    /* JADX WARN: Code restructure failed: missing block: B:498:0x0c82, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiChannel, r5.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:499:0x0c96, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:500:0x0ca7, code lost:
        r1 = r29.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:501:0x0cac, code lost:
        if (r1 != null) goto L489;
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0cc8, code lost:
        if (r1.isMusic() == false) goto L492;
     */
    /* JADX WARN: Code restructure failed: missing block: B:505:0x0cca, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.R.string.NotificationActionPinnedMusic, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:507:0x0ce2, code lost:
        if (r1.isVideo() == false) goto L500;
     */
    /* JADX WARN: Code restructure failed: missing block: B:509:0x0ce8, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L499;
     */
    /* JADX WARN: Code restructure failed: missing block: B:511:0x0cf2, code lost:
        if (android.text.TextUtils.isEmpty(r1.messageOwner.message) != false) goto L499;
     */
    /* JADX WARN: Code restructure failed: missing block: B:512:0x0cf4, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r8, "📹 " + r1.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x0d1d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.R.string.NotificationActionPinnedVideo, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x0d36, code lost:
        if (r1.isGif() == false) goto L508;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0d3c, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L507;
     */
    /* JADX WARN: Code restructure failed: missing block: B:519:0x0d46, code lost:
        if (android.text.TextUtils.isEmpty(r1.messageOwner.message) != false) goto L507;
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x0d48, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r8, "🎬 " + r1.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x0d71, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.R.string.NotificationActionPinnedGif, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x0d8d, code lost:
        if (r1.isVoice() == false) goto L511;
     */
    /* JADX WARN: Code restructure failed: missing block: B:524:0x0d8f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.R.string.NotificationActionPinnedVoice, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:526:0x0da5, code lost:
        if (r1.isRoundVideo() == false) goto L514;
     */
    /* JADX WARN: Code restructure failed: missing block: B:527:0x0da7, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.R.string.NotificationActionPinnedRound, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x0dbd, code lost:
        if (r1.isSticker() != false) goto L564;
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x0dc3, code lost:
        if (r1.isAnimatedSticker() == false) goto L518;
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0dc7, code lost:
        r4 = r1.messageOwner;
        r7 = r4.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:534:0x0dcd, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L526;
     */
    /* JADX WARN: Code restructure failed: missing block: B:536:0x0dd3, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L525;
     */
    /* JADX WARN: Code restructure failed: missing block: B:538:0x0ddb, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L525;
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0ddd, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r8, "📎 " + r1.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:540:0x0e06, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.R.string.NotificationActionPinnedFile, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:542:0x0e1d, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L563;
     */
    /* JADX WARN: Code restructure failed: missing block: B:544:0x0e21, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L530;
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0e27, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L533;
     */
    /* JADX WARN: Code restructure failed: missing block: B:548:0x0e29, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.R.string.NotificationActionPinnedGeoLive, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:550:0x0e40, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L536;
     */
    /* JADX WARN: Code restructure failed: missing block: B:551:0x0e42, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r29.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.R.string.NotificationActionPinnedContact2, r8, r5.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:553:0x0e6a, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L542;
     */
    /* JADX WARN: Code restructure failed: missing block: B:554:0x0e6c, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r7).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:555:0x0e72, code lost:
        if (r0.quiz == false) goto L541;
     */
    /* JADX WARN: Code restructure failed: missing block: B:556:0x0e74, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.R.string.NotificationActionPinnedQuiz2, r8, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:557:0x0e8e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.R.string.NotificationActionPinnedPoll2, r8, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:559:0x0eaa, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L550;
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0eb0, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L549;
     */
    /* JADX WARN: Code restructure failed: missing block: B:563:0x0eb8, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L549;
     */
    /* JADX WARN: Code restructure failed: missing block: B:564:0x0eba, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r8, "🖼 " + r1.messageOwner.message, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:565:0x0ee3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.R.string.NotificationActionPinnedPhoto, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:567:0x0efd, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L553;
     */
    /* JADX WARN: Code restructure failed: missing block: B:568:0x0eff, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.R.string.NotificationActionPinnedGame, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x0f11, code lost:
        r0 = r1.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:570:0x0f13, code lost:
        if (r0 == null) goto L562;
     */
    /* JADX WARN: Code restructure failed: missing block: B:572:0x0f19, code lost:
        if (r0.length() <= 0) goto L562;
     */
    /* JADX WARN: Code restructure failed: missing block: B:573:0x0f1b, code lost:
        r0 = r1.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:574:0x0f21, code lost:
        if (r0.length() <= 20) goto L561;
     */
    /* JADX WARN: Code restructure failed: missing block: B:575:0x0f23, code lost:
        r1 = new java.lang.StringBuilder();
        r3 = 0;
        r1.append((java.lang.Object) r0.subSequence(0, 20));
        r1.append("...");
        r0 = r1.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:576:0x0f3a, code lost:
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:577:0x0f3b, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedText;
        r2 = new java.lang.Object[3];
        r2[r3] = r8;
        r2[1] = r0;
        r2[2] = r5.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:578:0x0f50, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:579:0x0f65, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.R.string.NotificationActionPinnedGeo, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:580:0x0f7a, code lost:
        r0 = r1.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:581:0x0f80, code lost:
        if (r0 == null) goto L567;
     */
    /* JADX WARN: Code restructure failed: missing block: B:582:0x0f82, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmoji, r8, r5.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:583:0x0f98, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.R.string.NotificationActionPinnedSticker, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:585:0x0fad, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGameScore) == false) goto L572;
     */
    /* JADX WARN: Code restructure failed: missing block: B:588:0x0fb9, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) == false) goto L584;
     */
    /* JADX WARN: Code restructure failed: missing block: B:589:0x0fbb, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) r1).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:590:0x0fc3, code lost:
        if (android.text.TextUtils.isEmpty(r0) == false) goto L580;
     */
    /* JADX WARN: Code restructure failed: missing block: B:592:0x0fc7, code lost:
        if (r3 != r18) goto L579;
     */
    /* JADX WARN: Code restructure failed: missing block: B:593:0x0fc9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:594:0x0fd6, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.R.string.ChatThemeDisabled, r8, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:596:0x0fed, code lost:
        if (r3 != r18) goto L583;
     */
    /* JADX WARN: Code restructure failed: missing block: B:597:0x0fef, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeYou", org.telegram.messenger.R.string.ChatThemeChangedYou, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:598:0x0ffd, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.R.string.ChatThemeChangedTo, r8, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:600:0x1010, code lost:
        if ((r1 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest) == false) goto L140;
     */
    /* JADX WARN: Code restructure failed: missing block: B:603:0x1022, code lost:
        if (r2.peer_id.channel_id == 0) goto L598;
     */
    /* JADX WARN: Code restructure failed: missing block: B:605:0x1026, code lost:
        if (r5.megagroup != false) goto L598;
     */
    /* JADX WARN: Code restructure failed: missing block: B:607:0x102c, code lost:
        if (r29.isVideoAvatar() == false) goto L596;
     */
    /* JADX WARN: Code restructure failed: missing block: B:611:0x1057, code lost:
        if (r29.isVideoAvatar() == false) goto L602;
     */
    /* JADX WARN: Code restructure failed: missing block: B:615:0x1089, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r5) == false) goto L713;
     */
    /* JADX WARN: Code restructure failed: missing block: B:617:0x108d, code lost:
        if (r5.megagroup != false) goto L713;
     */
    /* JADX WARN: Code restructure failed: missing block: B:619:0x1093, code lost:
        if (r29.isMediaEmpty() == false) goto L617;
     */
    /* JADX WARN: Code restructure failed: missing block: B:620:0x1095, code lost:
        if (r30 != false) goto L615;
     */
    /* JADX WARN: Code restructure failed: missing block: B:622:0x109f, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L615;
     */
    /* JADX WARN: Code restructure failed: missing block: B:623:0x10a1, code lost:
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r8, r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x10c8, code lost:
        r4 = r29.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:626:0x10d0, code lost:
        if ((r4.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L628;
     */
    /* JADX WARN: Code restructure failed: missing block: B:627:0x10d2, code lost:
        if (r30 != false) goto L626;
     */
    /* JADX WARN: Code restructure failed: missing block: B:629:0x10d8, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L626;
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:0x10e0, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L626;
     */
    /* JADX WARN: Code restructure failed: missing block: B:632:0x10e2, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r8, "🖼 " + r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:635:0x111c, code lost:
        if (r29.isVideo() == false) goto L639;
     */
    /* JADX WARN: Code restructure failed: missing block: B:636:0x111e, code lost:
        if (r30 != false) goto L637;
     */
    /* JADX WARN: Code restructure failed: missing block: B:638:0x1124, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L637;
     */
    /* JADX WARN: Code restructure failed: missing block: B:640:0x112e, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L637;
     */
    /* JADX WARN: Code restructure failed: missing block: B:641:0x1130, code lost:
        r0 = org.telegram.messenger.R.string.NotificationMessageText;
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r0, r8, "📹 " + r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:644:0x116c, code lost:
        if (r29.isVoice() == false) goto L643;
     */
    /* JADX WARN: Code restructure failed: missing block: B:647:0x1180, code lost:
        if (r29.isRoundVideo() == false) goto L647;
     */
    /* JADX WARN: Code restructure failed: missing block: B:650:0x1194, code lost:
        if (r29.isMusic() == false) goto L651;
     */
    /* JADX WARN: Code restructure failed: missing block: B:652:0x11a4, code lost:
        r0 = r29.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:653:0x11aa, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L655;
     */
    /* JADX WARN: Code restructure failed: missing block: B:654:0x11ac, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:656:0x11ca, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L661;
     */
    /* JADX WARN: Code restructure failed: missing block: B:657:0x11cc, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r0).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:658:0x11d2, code lost:
        if (r0.quiz == false) goto L660;
     */
    /* JADX WARN: Code restructure failed: missing block: B:659:0x11d4, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageQuiz2", org.telegram.messenger.R.string.ChannelMessageQuiz2, r8, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:660:0x11e9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessagePoll2", org.telegram.messenger.R.string.ChannelMessagePoll2, r8, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:662:0x1200, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) == false) goto L665;
     */
    /* JADX WARN: Code restructure failed: missing block: B:663:0x1202, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:665:0x1228, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L711;
     */
    /* JADX WARN: Code restructure failed: missing block: B:667:0x122c, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L669;
     */
    /* JADX WARN: Code restructure failed: missing block: B:670:0x1232, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L673;
     */
    /* JADX WARN: Code restructure failed: missing block: B:673:0x1246, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L703;
     */
    /* JADX WARN: Code restructure failed: missing block: B:675:0x124c, code lost:
        if (r29.isSticker() != false) goto L699;
     */
    /* JADX WARN: Code restructure failed: missing block: B:677:0x1252, code lost:
        if (r29.isAnimatedSticker() == false) goto L679;
     */
    /* JADX WARN: Code restructure failed: missing block: B:680:0x125a, code lost:
        if (r29.isGif() == false) goto L690;
     */
    /* JADX WARN: Code restructure failed: missing block: B:681:0x125c, code lost:
        if (r30 != false) goto L688;
     */
    /* JADX WARN: Code restructure failed: missing block: B:683:0x1262, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L688;
     */
    /* JADX WARN: Code restructure failed: missing block: B:685:0x126c, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L688;
     */
    /* JADX WARN: Code restructure failed: missing block: B:686:0x126e, code lost:
        r0 = org.telegram.messenger.R.string.NotificationMessageText;
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r0, r8, "🎬 " + r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:688:0x12a4, code lost:
        if (r30 != false) goto L697;
     */
    /* JADX WARN: Code restructure failed: missing block: B:690:0x12aa, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L697;
     */
    /* JADX WARN: Code restructure failed: missing block: B:692:0x12b4, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L697;
     */
    /* JADX WARN: Code restructure failed: missing block: B:693:0x12b6, code lost:
        r0 = org.telegram.messenger.R.string.NotificationMessageText;
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r0, r8, "📎 " + r29.messageOwner.message);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:695:0x12ec, code lost:
        r0 = r29.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:696:0x12f2, code lost:
        if (r0 == null) goto L702;
     */
    /* JADX WARN: Code restructure failed: missing block: B:697:0x12f4, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageStickerEmoji", org.telegram.messenger.R.string.ChannelMessageStickerEmoji, r8, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:698:0x1305, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageSticker", org.telegram.messenger.R.string.ChannelMessageSticker, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:700:0x1314, code lost:
        if (r30 != false) goto L709;
     */
    /* JADX WARN: Code restructure failed: missing block: B:702:0x131c, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageText) != false) goto L709;
     */
    /* JADX WARN: Code restructure failed: missing block: B:703:0x131e, code lost:
        r14 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r8, r29.messageText);
        r31[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:707:0x1355, code lost:
        if (r29.isMediaEmpty() == false) goto L722;
     */
    /* JADX WARN: Code restructure failed: missing block: B:708:0x1357, code lost:
        if (r30 != false) goto L720;
     */
    /* JADX WARN: Code restructure failed: missing block: B:710:0x1361, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L720;
     */
    /* JADX WARN: Code restructure failed: missing block: B:713:0x1392, code lost:
        r2 = r29.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:714:0x139a, code lost:
        if ((r2.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L733;
     */
    /* JADX WARN: Code restructure failed: missing block: B:715:0x139c, code lost:
        if (r30 != false) goto L731;
     */
    /* JADX WARN: Code restructure failed: missing block: B:717:0x13a2, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L731;
     */
    /* JADX WARN: Code restructure failed: missing block: B:719:0x13aa, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L731;
     */
    /* JADX WARN: Code restructure failed: missing block: B:720:0x13ac, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageGroupText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:723:0x13ee, code lost:
        if (r29.isVideo() == false) goto L744;
     */
    /* JADX WARN: Code restructure failed: missing block: B:724:0x13f0, code lost:
        if (r30 != false) goto L742;
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x13f6, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L742;
     */
    /* JADX WARN: Code restructure failed: missing block: B:728:0x1400, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L742;
     */
    /* JADX WARN: Code restructure failed: missing block: B:729:0x1402, code lost:
        r0 = org.telegram.messenger.R.string.NotificationMessageGroupText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:732:0x1447, code lost:
        if (r29.isVoice() == false) goto L748;
     */
    /* JADX WARN: Code restructure failed: missing block: B:735:0x145f, code lost:
        if (r29.isRoundVideo() == false) goto L752;
     */
    /* JADX WARN: Code restructure failed: missing block: B:738:0x1477, code lost:
        if (r29.isMusic() == false) goto L756;
     */
    /* JADX WARN: Code restructure failed: missing block: B:740:0x148b, code lost:
        r0 = r29.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:741:0x1491, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L760;
     */
    /* JADX WARN: Code restructure failed: missing block: B:742:0x1493, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:744:0x14b7, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L766;
     */
    /* JADX WARN: Code restructure failed: missing block: B:745:0x14b9, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r0).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:746:0x14bf, code lost:
        if (r0.quiz == false) goto L765;
     */
    /* JADX WARN: Code restructure failed: missing block: B:747:0x14c1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupQuiz2", org.telegram.messenger.R.string.NotificationMessageGroupQuiz2, r8, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:748:0x14db, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPoll2", org.telegram.messenger.R.string.NotificationMessageGroupPoll2, r8, r5.title, r0.question);
     */
    /* JADX WARN: Code restructure failed: missing block: B:750:0x14f7, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L770;
     */
    /* JADX WARN: Code restructure failed: missing block: B:753:0x1517, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) == false) goto L774;
     */
    /* JADX WARN: Code restructure failed: missing block: B:754:0x1519, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:756:0x153f, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveawayResults) == false) goto L778;
     */
    /* JADX WARN: Code restructure failed: missing block: B:759:0x1550, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L823;
     */
    /* JADX WARN: Code restructure failed: missing block: B:761:0x1554, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L782;
     */
    /* JADX WARN: Code restructure failed: missing block: B:764:0x155a, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L786;
     */
    /* JADX WARN: Code restructure failed: missing block: B:767:0x1573, code lost:
        if ((r0 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L816;
     */
    /* JADX WARN: Code restructure failed: missing block: B:769:0x1579, code lost:
        if (r29.isSticker() != false) goto L812;
     */
    /* JADX WARN: Code restructure failed: missing block: B:771:0x157f, code lost:
        if (r29.isAnimatedSticker() == false) goto L792;
     */
    /* JADX WARN: Code restructure failed: missing block: B:774:0x1587, code lost:
        if (r29.isGif() == false) goto L803;
     */
    /* JADX WARN: Code restructure failed: missing block: B:775:0x1589, code lost:
        if (r30 != false) goto L801;
     */
    /* JADX WARN: Code restructure failed: missing block: B:777:0x158f, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L801;
     */
    /* JADX WARN: Code restructure failed: missing block: B:779:0x1599, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L801;
     */
    /* JADX WARN: Code restructure failed: missing block: B:780:0x159b, code lost:
        r0 = org.telegram.messenger.R.string.NotificationMessageGroupText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:782:0x15d9, code lost:
        if (r30 != false) goto L810;
     */
    /* JADX WARN: Code restructure failed: missing block: B:784:0x15df, code lost:
        if (android.os.Build.VERSION.SDK_INT < 19) goto L810;
     */
    /* JADX WARN: Code restructure failed: missing block: B:786:0x15e9, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageOwner.message) != false) goto L810;
     */
    /* JADX WARN: Code restructure failed: missing block: B:787:0x15eb, code lost:
        r0 = org.telegram.messenger.R.string.NotificationMessageGroupText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:789:0x1629, code lost:
        r0 = r29.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:790:0x162f, code lost:
        if (r0 == null) goto L815;
     */
    /* JADX WARN: Code restructure failed: missing block: B:791:0x1631, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupStickerEmoji", org.telegram.messenger.R.string.NotificationMessageGroupStickerEmoji, r8, r5.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:792:0x1647, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupSticker", org.telegram.messenger.R.string.NotificationMessageGroupSticker, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:793:0x165a, code lost:
        if (r30 != false) goto L821;
     */
    /* JADX WARN: Code restructure failed: missing block: B:795:0x1662, code lost:
        if (android.text.TextUtils.isEmpty(r29.messageText) != false) goto L821;
     */
    /* JADX WARN: Code restructure failed: missing block: B:822:?, code lost:
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:823:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.R.string.NotificationInvitedToGroupByLink, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:824:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.R.string.NotificationEditedGroupName, r8, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:825:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.R.string.NotificationGroupKickYou, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:826:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.R.string.NotificationGroupLeftMember, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:827:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.R.string.NotificationGroupKickMember, r8, r5.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:828:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:829:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:830:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:831:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:832:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:833:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:834:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:835:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:836:?, code lost:
        return r29.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:837:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.R.string.ChannelVideoEditNotification, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:838:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.R.string.ChannelPhotoEditNotification, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:839:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.R.string.NotificationEditedGroupVideo, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:840:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.R.string.NotificationEditedGroupPhoto, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:841:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:842:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.R.string.ChannelMessageNoText, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:843:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:844:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessagePhoto", org.telegram.messenger.R.string.ChannelMessagePhoto, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:845:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:846:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageVideo", org.telegram.messenger.R.string.ChannelMessageVideo, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:847:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageAudio", org.telegram.messenger.R.string.ChannelMessageAudio, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:848:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageRound", org.telegram.messenger.R.string.ChannelMessageRound, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:849:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageMusic", org.telegram.messenger.R.string.ChannelMessageMusic, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:850:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageContact2", org.telegram.messenger.R.string.ChannelMessageContact2, r8, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:851:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageChannelGiveaway", org.telegram.messenger.R.string.NotificationMessageChannelGiveaway, r5.title, java.lang.Integer.valueOf(r0.quantity), java.lang.Integer.valueOf(r0.months));
     */
    /* JADX WARN: Code restructure failed: missing block: B:852:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageLiveLocation", org.telegram.messenger.R.string.ChannelMessageLiveLocation, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:853:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:854:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageGIF", org.telegram.messenger.R.string.ChannelMessageGIF, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:855:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:856:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageDocument", org.telegram.messenger.R.string.ChannelMessageDocument, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:857:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:858:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.R.string.ChannelMessageNoText, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:859:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelMessageMap", org.telegram.messenger.R.string.ChannelMessageMap, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:860:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r8, r5.title, r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:861:?, code lost:
        return org.telegram.messenger.LocaleController.formatString(r21, org.telegram.messenger.R.string.NotificationMessageGroupNoText, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:862:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r1, r8, r5.title, "🖼 " + r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:863:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPhoto", org.telegram.messenger.R.string.NotificationMessageGroupPhoto, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:864:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r0, r8, r5.title, "📹 " + r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:865:?, code lost:
        return org.telegram.messenger.LocaleController.formatString(" ", org.telegram.messenger.R.string.NotificationMessageGroupVideo, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:866:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupAudio", org.telegram.messenger.R.string.NotificationMessageGroupAudio, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:867:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupRound", org.telegram.messenger.R.string.NotificationMessageGroupRound, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:868:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMusic", org.telegram.messenger.R.string.NotificationMessageGroupMusic, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:869:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupContact2", org.telegram.messenger.R.string.NotificationMessageGroupContact2, r8, r5.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:870:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGame", org.telegram.messenger.R.string.NotificationMessageGroupGame, r8, r5.title, r0.game.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:871:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageChannelGiveaway", org.telegram.messenger.R.string.NotificationMessageChannelGiveaway, r5.title, java.lang.Integer.valueOf(r0.quantity), java.lang.Integer.valueOf(r0.months));
     */
    /* JADX WARN: Code restructure failed: missing block: B:872:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("BoostingGiveawayResults", org.telegram.messenger.R.string.BoostingGiveawayResults, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:873:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupLiveLocation", org.telegram.messenger.R.string.NotificationMessageGroupLiveLocation, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:874:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r0, r8, r5.title, "🎬 " + r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:875:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGif", org.telegram.messenger.R.string.NotificationMessageGroupGif, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:876:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r0, r8, r5.title, "📎 " + r29.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:877:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupDocument", org.telegram.messenger.R.string.NotificationMessageGroupDocument, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:878:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r8, r5.title, r29.messageText);
     */
    /* JADX WARN: Code restructure failed: missing block: B:879:?, code lost:
        return org.telegram.messenger.LocaleController.formatString(r21, org.telegram.messenger.R.string.NotificationMessageGroupNoText, r8, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:880:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMap", org.telegram.messenger.R.string.NotificationMessageGroupMap, r8, r5.title);
     */
    /* JADX WARN: Removed duplicated region for block: B:277:0x065c  */
    /* JADX WARN: Removed duplicated region for block: B:801:0x16aa  */
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
            if (j2 != 0 || j3 == 0) {
                if (j2 != 0) {
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
            } else if (!z4 || !notificationsSettings.getBoolean("EnablePreviewAll", true)) {
                if (zArr2 != null) {
                    zArr2[0] = false;
                }
                return LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, messageObject.localName);
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
                str2 = user != null ? UserObject.getUserName(user) : null;
            } else if (j == clientUserId) {
                str2 = LocaleController.getString("MessageScheduledReminderNotification", R.string.MessageScheduledReminderNotification);
            } else {
                str2 = LocaleController.getString("NotificationMessageScheduledName", R.string.NotificationMessageScheduledName);
            }
            str = "NotificationMessageGroupNoText";
        } else {
            str = "NotificationMessageGroupNoText";
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
            charSequence = LocaleController.getString("YouHaveNewMessage", R.string.YouHaveNewMessage);
        } else {
            String str3 = str;
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
                            return (ChatObject.isChannel(tLRPC$Chat2) || tLRPC$Chat2.megagroup) ? LocaleController.formatString(str3, R.string.NotificationMessageGroupNoText, str2, tLRPC$Chat2.title) : LocaleController.formatString("ChannelMessageNoText", R.string.ChannelMessageNoText, str2);
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
                String str4 = null;
                return str4;
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
                        charSequence = LocaleController.formatString("NotificationContactJoined", R.string.NotificationContactJoined, str2);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserUpdatedPhoto) {
                        charSequence = LocaleController.formatString("NotificationContactNewPhoto", R.string.NotificationContactNewPhoto, str2);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionLoginUnknownLocation) {
                        String formatString3 = LocaleController.formatString("formatDateAtTime", R.string.formatDateAtTime, LocaleController.getInstance().formatterYear.format(messageObject.messageOwner.date * 1000), LocaleController.getInstance().formatterDay.format(messageObject.messageOwner.date * 1000));
                        int i = R.string.NotificationUnrecognizedDevice;
                        TLRPC$MessageAction tLRPC$MessageAction2 = messageObject.messageOwner.action;
                        charSequence = LocaleController.formatString("NotificationUnrecognizedDevice", i, getUserConfig().getCurrentUser().first_name, formatString3, tLRPC$MessageAction2.title, tLRPC$MessageAction2.address);
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
                            String str5 = ((TLRPC$TL_messageActionSetChatTheme) tLRPC$MessageAction).emoticon;
                            if (!TextUtils.isEmpty(str5)) {
                                c = 0;
                                z3 = true;
                                if (j == clientUserId) {
                                    formatString2 = LocaleController.formatString("ChangedChatThemeYou", R.string.ChatThemeChangedYou, str5);
                                } else {
                                    formatString2 = LocaleController.formatString("ChangedChatThemeTo", R.string.ChatThemeChangedTo, str2, str5);
                                }
                            } else if (j == clientUserId) {
                                c = 0;
                                formatString2 = LocaleController.formatString("ChatThemeDisabledYou", R.string.ChatThemeDisabledYou, new Object[0]);
                                z3 = true;
                            } else {
                                c = 0;
                                z3 = true;
                                formatString2 = LocaleController.formatString("ChatThemeDisabled", R.string.ChatThemeDisabled, str2, str5);
                            }
                            charSequence = formatString2;
                            zArr[c] = z3;
                        }
                        String str42 = null;
                        return str42;
                    }
                } else if (!messageObject.isMediaEmpty()) {
                    TLRPC$Message tLRPC$Message3 = messageObject.messageOwner;
                    if (tLRPC$Message3.media instanceof TLRPC$TL_messageMediaPhoto) {
                        if (!z && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(tLRPC$Message3.message)) {
                            int i2 = R.string.NotificationMessageText;
                            charSequence = LocaleController.formatString("NotificationMessageText", i2, str2, "🖼 " + messageObject.messageOwner.message);
                            zArr[0] = true;
                        } else if (messageObject.messageOwner.media.ttl_seconds != 0) {
                            charSequence = LocaleController.formatString("NotificationMessageSDPhoto", R.string.NotificationMessageSDPhoto, str2);
                        } else {
                            charSequence = LocaleController.formatString("NotificationMessagePhoto", R.string.NotificationMessagePhoto, str2);
                        }
                    } else if (messageObject.isVideo()) {
                        if (!z && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            int i3 = R.string.NotificationMessageText;
                            charSequence = LocaleController.formatString("NotificationMessageText", i3, str2, "📹 " + messageObject.messageOwner.message);
                            zArr[0] = true;
                        } else if (messageObject.messageOwner.media.ttl_seconds != 0) {
                            charSequence = LocaleController.formatString("NotificationMessageSDVideo", R.string.NotificationMessageSDVideo, str2);
                        } else {
                            charSequence = LocaleController.formatString("NotificationMessageVideo", R.string.NotificationMessageVideo, str2);
                        }
                    } else if (messageObject.isGame()) {
                        charSequence = LocaleController.formatString("NotificationMessageGame", R.string.NotificationMessageGame, str2, messageObject.messageOwner.media.game.title);
                    } else if (messageObject.isVoice()) {
                        charSequence = LocaleController.formatString("NotificationMessageAudio", R.string.NotificationMessageAudio, str2);
                    } else if (messageObject.isRoundVideo()) {
                        charSequence = LocaleController.formatString("NotificationMessageRound", R.string.NotificationMessageRound, str2);
                    } else if (messageObject.isMusic()) {
                        charSequence = LocaleController.formatString("NotificationMessageMusic", R.string.NotificationMessageMusic, str2);
                    } else {
                        TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
                        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaContact) {
                            TLRPC$TL_messageMediaContact tLRPC$TL_messageMediaContact = (TLRPC$TL_messageMediaContact) tLRPC$MessageMedia;
                            charSequence = LocaleController.formatString("NotificationMessageContact2", R.string.NotificationMessageContact2, str2, ContactsController.formatName(tLRPC$TL_messageMediaContact.first_name, tLRPC$TL_messageMediaContact.last_name));
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGiveaway) {
                            TLRPC$TL_messageMediaGiveaway tLRPC$TL_messageMediaGiveaway = (TLRPC$TL_messageMediaGiveaway) tLRPC$MessageMedia;
                            charSequence = LocaleController.formatString("NotificationMessageChannelGiveaway", R.string.NotificationMessageChannelGiveaway, str2, Integer.valueOf(tLRPC$TL_messageMediaGiveaway.quantity), Integer.valueOf(tLRPC$TL_messageMediaGiveaway.months));
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGiveawayResults) {
                            charSequence = LocaleController.formatString("BoostingGiveawayResults", R.string.BoostingGiveawayResults, new Object[0]);
                        } else {
                            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                                TLRPC$Poll tLRPC$Poll = ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia).poll;
                                if (tLRPC$Poll.quiz) {
                                    formatString = LocaleController.formatString("NotificationMessageQuiz2", R.string.NotificationMessageQuiz2, str2, tLRPC$Poll.question);
                                } else {
                                    formatString = LocaleController.formatString("NotificationMessagePoll2", R.string.NotificationMessagePoll2, str2, tLRPC$Poll.question);
                                }
                            } else if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeo) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaVenue)) {
                                charSequence = LocaleController.formatString("NotificationMessageMap", R.string.NotificationMessageMap, str2);
                            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeoLive) {
                                charSequence = LocaleController.formatString("NotificationMessageLiveLocation", R.string.NotificationMessageLiveLocation, str2);
                            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                                if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {
                                    String stickerEmoji = messageObject.getStickerEmoji();
                                    if (stickerEmoji != null) {
                                        formatString = LocaleController.formatString("NotificationMessageStickerEmoji", R.string.NotificationMessageStickerEmoji, str2, stickerEmoji);
                                    } else {
                                        formatString = LocaleController.formatString("NotificationMessageSticker", R.string.NotificationMessageSticker, str2);
                                    }
                                } else if (messageObject.isGif()) {
                                    if (!z && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                        int i4 = R.string.NotificationMessageText;
                                        charSequence = LocaleController.formatString("NotificationMessageText", i4, str2, "🎬 " + messageObject.messageOwner.message);
                                        zArr[0] = true;
                                    } else {
                                        charSequence = LocaleController.formatString("NotificationMessageGif", R.string.NotificationMessageGif, str2);
                                    }
                                } else if (!z && Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                    int i5 = R.string.NotificationMessageText;
                                    charSequence = LocaleController.formatString("NotificationMessageText", i5, str2, "📎 " + messageObject.messageOwner.message);
                                    zArr[0] = true;
                                } else {
                                    charSequence = LocaleController.formatString("NotificationMessageDocument", R.string.NotificationMessageDocument, str2);
                                }
                            } else if (!z && !TextUtils.isEmpty(messageObject.messageText)) {
                                charSequence = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, str2, messageObject.messageText);
                                zArr[0] = true;
                            } else {
                                charSequence = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str2);
                            }
                            charSequence = formatString;
                        }
                    }
                } else if (!z) {
                    if (!TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        charSequence = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, str2, messageObject.messageOwner.message);
                        zArr[0] = true;
                    } else {
                        charSequence = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str2);
                    }
                } else {
                    charSequence = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str2);
                }
            } else {
                if (zArr2 != null) {
                    zArr2[0] = false;
                }
                charSequence = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str2);
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
        return tLRPC$Peer != null && tLRPC$Peer.chat_id == 0 && tLRPC$Peer.channel_id == 0 && ((tLRPC$MessageAction = tLRPC$Message.action) == null || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionEmpty));
    }

    private int getNotifyOverride(SharedPreferences sharedPreferences, long j, long j2) {
        int property = this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_NOTIFY, j, j2, -1);
        if (property != 3 || this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL, j, j2, 0) < getConnectionsManager().getCurrentTime()) {
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

    public void deleteNotificationChannel(long j, long j2) {
        deleteNotificationChannel(j, j2, -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: deleteNotificationChannelInternal */
    public void lambda$deleteNotificationChannel$37(long j, long j2, int i) {
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$deleteNotificationChannel$37(j, j2, i);
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

    /* JADX WARN: Removed duplicated region for block: B:26:0x00f1 A[Catch: Exception -> 0x0156, TryCatch #0 {Exception -> 0x0156, blocks: (B:9:0x0020, B:12:0x0062, B:14:0x006a, B:18:0x007a, B:20:0x00a3, B:22:0x00b3, B:24:0x00bd, B:26:0x00f1, B:28:0x00f9, B:30:0x0102, B:39:0x0123, B:43:0x013a, B:44:0x0151, B:32:0x0109, B:34:0x010f, B:36:0x0114, B:35:0x0112, B:37:0x0119, B:27:0x00f5, B:17:0x0076, B:13:0x0066), top: B:51:0x0020 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00f5 A[Catch: Exception -> 0x0156, TryCatch #0 {Exception -> 0x0156, blocks: (B:9:0x0020, B:12:0x0062, B:14:0x006a, B:18:0x007a, B:20:0x00a3, B:22:0x00b3, B:24:0x00bd, B:26:0x00f1, B:28:0x00f9, B:30:0x0102, B:39:0x0123, B:43:0x013a, B:44:0x0151, B:32:0x0109, B:34:0x010f, B:36:0x0114, B:35:0x0112, B:37:0x0119, B:27:0x00f5, B:17:0x0076, B:13:0x0066), top: B:51:0x0020 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0102 A[Catch: Exception -> 0x0156, TryCatch #0 {Exception -> 0x0156, blocks: (B:9:0x0020, B:12:0x0062, B:14:0x006a, B:18:0x007a, B:20:0x00a3, B:22:0x00b3, B:24:0x00bd, B:26:0x00f1, B:28:0x00f9, B:30:0x0102, B:39:0x0123, B:43:0x013a, B:44:0x0151, B:32:0x0109, B:34:0x010f, B:36:0x0114, B:35:0x0112, B:37:0x0119, B:27:0x00f5, B:17:0x0076, B:13:0x0066), top: B:51:0x0020 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0123 A[Catch: Exception -> 0x0156, TryCatch #0 {Exception -> 0x0156, blocks: (B:9:0x0020, B:12:0x0062, B:14:0x006a, B:18:0x007a, B:20:0x00a3, B:22:0x00b3, B:24:0x00bd, B:26:0x00f1, B:28:0x00f9, B:30:0x0102, B:39:0x0123, B:43:0x013a, B:44:0x0151, B:32:0x0109, B:34:0x010f, B:36:0x0114, B:35:0x0112, B:37:0x0119, B:27:0x00f5, B:17:0x0076, B:13:0x0066), top: B:51:0x0020 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0151 A[Catch: Exception -> 0x0156, TRY_LEAVE, TryCatch #0 {Exception -> 0x0156, blocks: (B:9:0x0020, B:12:0x0062, B:14:0x006a, B:18:0x007a, B:20:0x00a3, B:22:0x00b3, B:24:0x00bd, B:26:0x00f1, B:28:0x00f9, B:30:0x0102, B:39:0x0123, B:43:0x013a, B:44:0x0151, B:32:0x0109, B:34:0x010f, B:36:0x0114, B:35:0x0112, B:37:0x0119, B:27:0x00f5, B:17:0x0076, B:13:0x0066), top: B:51:0x0020 }] */
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

    /* JADX WARN: Removed duplicated region for block: B:207:0x04ec  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x0509 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0544 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:224:0x0553 A[LOOP:1: B:222:0x0550->B:224:0x0553, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:227:0x0566  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x0572 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:234:0x0583 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:249:0x059f  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x05ba  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x019e  */
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
        String string;
        int i4;
        String str7;
        String str8;
        String str9;
        String str10;
        String str11;
        StringBuilder sb;
        long j3;
        int i5;
        long[] jArr2;
        String str12;
        NotificationsController notificationsController;
        String str13;
        String str14;
        String str15;
        String str16;
        boolean z4;
        int i6;
        String str17;
        Uri uri2;
        String MD5;
        String str18;
        boolean z5;
        boolean z6;
        String str19;
        boolean z7;
        long[] jArr3;
        String str20;
        String str21;
        String str22;
        boolean z8;
        int i7;
        long[] jArr4;
        boolean z9;
        SharedPreferences.Editor editor;
        boolean z10;
        int i8;
        int i9;
        StringBuilder sb2;
        SharedPreferences.Editor editor2;
        ensureGroupsCreated();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        String str23 = "private";
        if (z3) {
            str4 = "other" + this.currentAccount;
            str5 = null;
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
            } else {
                str2 = "private" + this.currentAccount;
                str3 = "overwrite_private";
            }
            str4 = str2;
            str5 = str3;
        }
        boolean z11 = !z && DialogObject.isEncryptedDialog(j);
        boolean z12 = (z2 || str5 == null || !notificationsSettings.getBoolean(str5, false)) ? false : true;
        String MD52 = Utilities.MD5(uri == null ? "NoSound2" : uri.toString());
        if (MD52 != null && MD52.length() > 5) {
            MD52 = MD52.substring(0, 5);
        }
        if (z3) {
            str23 = "silent";
            str6 = LocaleController.getString("NotificationsSilent", R.string.NotificationsSilent);
        } else {
            if (z) {
                String string2 = z2 ? LocaleController.getString("NotificationsInAppDefault", R.string.NotificationsInAppDefault) : LocaleController.getString("NotificationsDefault", R.string.NotificationsDefault);
                if (i3 == 2) {
                    str23 = z2 ? "channels_ia" : "channels";
                    str6 = string2;
                } else {
                    if (i3 == 0) {
                        str23 = z2 ? "groups_ia" : "groups";
                    } else if (i3 == 3) {
                        str23 = z2 ? "stories_ia" : "stories";
                    } else if (z2) {
                        str23 = "private_ia";
                    }
                    str6 = string2;
                }
            } else {
                String formatString = z2 ? LocaleController.formatString("NotificationsChatInApp", R.string.NotificationsChatInApp, str) : str;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(z2 ? "org.telegram.keyia" : "org.telegram.key");
                sb3.append(j);
                sb3.append("_");
                sb3.append(j2);
                str23 = sb3.toString();
                str6 = formatString;
            }
            String str24 = str23 + "_" + MD52;
            string = notificationsSettings.getString(str24, null);
            String string3 = notificationsSettings.getString(str24 + "_s", null);
            StringBuilder sb4 = new StringBuilder();
            if (string == null) {
                NotificationChannel notificationChannel = systemNotificationManager.getNotificationChannel(string);
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder sb5 = new StringBuilder();
                    str19 = "_s";
                    sb5.append("current channel for ");
                    sb5.append(string);
                    sb5.append(" = ");
                    sb5.append(notificationChannel);
                    FileLog.d(sb5.toString());
                } else {
                    str19 = "_s";
                }
                if (notificationChannel == null) {
                    i4 = i;
                    sb = sb4;
                    notificationsController = this;
                    i5 = i2;
                    str9 = "secret";
                    str10 = str4;
                    str11 = "_";
                    str13 = str24;
                    j3 = j;
                    str12 = str19;
                    jArr2 = jArr;
                    str16 = null;
                    str15 = null;
                    str14 = null;
                    z4 = false;
                    if (!z4) {
                    }
                    if (!z12) {
                    }
                    i6 = 0;
                    while (i6 < jArr2.length) {
                    }
                    str17 = str15;
                    sb.append(i4);
                    uri2 = uri;
                    if (uri2 != null) {
                    }
                    sb.append(i5);
                    if (!z) {
                        sb.append(str9);
                    }
                    MD5 = Utilities.MD5(sb.toString());
                    if (z3) {
                    }
                    str15 = str17;
                    str16 = MD5;
                    if (str15 == null) {
                    }
                    return str15;
                } else if (!z3 && !z12) {
                    int importance = notificationChannel.getImportance();
                    Uri sound = notificationChannel.getSound();
                    long[] vibrationPattern = notificationChannel.getVibrationPattern();
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
                            sb4.append(j4);
                        }
                    }
                    sb4.append(lightColor);
                    if (sound != null) {
                        sb4.append(sound.toString());
                    }
                    sb4.append(importance);
                    if (!z && z11) {
                        sb4.append("secret");
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("current channel settings for " + string + " = " + ((Object) sb4) + " old = " + string3);
                    }
                    String MD53 = Utilities.MD5(sb4.toString());
                    sb4.setLength(0);
                    if (MD53.equals(string3)) {
                        jArr2 = jArr;
                        i4 = i;
                        str20 = string3;
                        str21 = string;
                        notificationsController = this;
                        str9 = "secret";
                        str10 = str4;
                        str11 = "_";
                        j3 = j;
                        str13 = str24;
                        sb = sb4;
                        i5 = i2;
                        str12 = str19;
                        str22 = MD53;
                        z8 = false;
                    } else {
                        if (importance == 0) {
                            SharedPreferences.Editor edit = notificationsSettings.edit();
                            if (z) {
                                if (!z2) {
                                    if (i3 == 3) {
                                        edit.putBoolean("EnableAllStories", false);
                                    } else {
                                        edit.putInt(getGlobalNotificationsKey(i3), ConnectionsManager.DEFAULT_DATACENTER_ID);
                                    }
                                    updateServerNotificationsSettings(i3);
                                }
                                str13 = str24;
                                sb2 = sb4;
                                str20 = string3;
                                str21 = string;
                                i7 = lightColor;
                                jArr4 = jArr3;
                                str9 = "secret";
                                str10 = str4;
                                str11 = "_";
                                z9 = z7;
                                editor2 = edit;
                                j3 = j;
                                str12 = str19;
                                str22 = MD53;
                            } else {
                                if (i3 == 3) {
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY);
                                    i9 = lightColor;
                                    sb6.append(getSharedPrefKey(j, 0L));
                                    edit.putBoolean(sb6.toString(), false);
                                } else {
                                    i9 = lightColor;
                                    edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, 0L), 2);
                                }
                                sb2 = sb4;
                                str20 = string3;
                                String str25 = str19;
                                str13 = str24;
                                str21 = string;
                                str22 = MD53;
                                str12 = str25;
                                z9 = z7;
                                jArr4 = jArr3;
                                str9 = "secret";
                                str10 = str4;
                                str11 = "_";
                                i7 = i9;
                                editor2 = edit;
                                j3 = j;
                                updateServerNotificationsSettings(j, 0L, true);
                            }
                            z8 = true;
                            notificationsController = this;
                            jArr2 = jArr;
                            i5 = i2;
                            sb = sb2;
                            editor = editor2;
                        } else {
                            str13 = str24;
                            str20 = string3;
                            str21 = string;
                            i7 = lightColor;
                            jArr4 = jArr3;
                            str9 = "secret";
                            str10 = str4;
                            str11 = "_";
                            z9 = z7;
                            int i10 = 4;
                            j3 = j;
                            sb = sb4;
                            i5 = i2;
                            str12 = str19;
                            str22 = MD53;
                            if (importance != i5) {
                                if (z2) {
                                    editor = null;
                                } else {
                                    editor = notificationsSettings.edit();
                                    if (importance == 4 || importance == 5) {
                                        z10 = true;
                                        i10 = 1;
                                    } else {
                                        z10 = true;
                                        if (importance != 1) {
                                            i10 = importance == 2 ? 5 : 0;
                                        }
                                    }
                                    if (z) {
                                        if (i3 == 3) {
                                            editor.putBoolean("EnableAllStories", z10);
                                        } else {
                                            editor.putInt(getGlobalNotificationsKey(i3), 0);
                                        }
                                        if (i3 == 2) {
                                            editor.putInt("priority_channel", i10);
                                        } else if (i3 == 0) {
                                            editor.putInt("priority_group", i10);
                                        } else if (i3 == 3) {
                                            editor.putInt("priority_stories", i10);
                                        } else {
                                            editor.putInt("priority_messages", i10);
                                        }
                                    } else if (i3 == 3) {
                                        editor.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + j3, true);
                                    } else {
                                        editor.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + j3, 0);
                                        editor.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + j3);
                                        editor.putInt("priority_" + j3, i10);
                                    }
                                }
                                z8 = true;
                            } else {
                                editor = null;
                                z8 = false;
                            }
                            notificationsController = this;
                            jArr2 = jArr;
                        }
                        boolean z13 = z9;
                        if ((!notificationsController.isEmptyVibration(jArr2)) != z13) {
                            if (!z2) {
                                if (editor == null) {
                                    editor = notificationsSettings.edit();
                                }
                                if (!z) {
                                    editor.putInt("vibrate_" + j3, z13 ? 0 : 2);
                                } else if (i3 == 2) {
                                    editor.putInt("vibrate_channel", z13 ? 0 : 2);
                                } else if (i3 == 0) {
                                    editor.putInt("vibrate_group", z13 ? 0 : 2);
                                } else if (i3 == 3) {
                                    editor.putInt("vibrate_stories", z13 ? 0 : 2);
                                } else {
                                    editor.putInt("vibrate_messages", z13 ? 0 : 2);
                                }
                            }
                            jArr2 = jArr4;
                            i4 = i;
                            i8 = i7;
                            z8 = true;
                        } else {
                            i4 = i;
                            i8 = i7;
                        }
                        if (i8 != i4) {
                            if (!z2) {
                                if (editor == null) {
                                    editor = notificationsSettings.edit();
                                }
                                if (!z) {
                                    editor.putInt("color_" + j3, i8);
                                } else if (i3 == 2) {
                                    editor.putInt("ChannelLed", i8);
                                } else if (i3 == 0) {
                                    editor.putInt("GroupLed", i8);
                                } else if (i3 == 3) {
                                    editor.putInt("StoriesLed", i8);
                                } else {
                                    editor.putInt("MessagesLed", i8);
                                }
                            }
                            i4 = i8;
                            z8 = true;
                        }
                        if (editor != null) {
                            editor.commit();
                        }
                    }
                    str16 = str22;
                    z4 = z8;
                    str14 = str20;
                    str15 = str21;
                    if (!z4 && str16 != null) {
                        notificationsSettings.edit().putString(str13, str15).putString(str13 + str12, str16).commit();
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("change edited channel " + str15);
                        }
                    } else if (!z12 || str16 == null || !z2 || !z) {
                        i6 = 0;
                        while (i6 < jArr2.length) {
                            sb.append(jArr2[i6]);
                            i6++;
                            str15 = str15;
                        }
                        str17 = str15;
                        sb.append(i4);
                        uri2 = uri;
                        if (uri2 != null) {
                            sb.append(uri.toString());
                        }
                        sb.append(i5);
                        if (!z && z11) {
                            sb.append(str9);
                        }
                        MD5 = Utilities.MD5(sb.toString());
                        if (!z3 || str17 == null || (!z12 && str14.equals(MD5))) {
                            str15 = str17;
                            str16 = MD5;
                        } else {
                            try {
                                str18 = str17;
                            } catch (Exception e) {
                                e = e;
                                str18 = str17;
                            }
                            try {
                                systemNotificationManager.deleteNotificationChannel(str18);
                            } catch (Exception e2) {
                                e = e2;
                                FileLog.e(e);
                                if (BuildVars.LOGS_ENABLED) {
                                }
                                str16 = MD5;
                                str15 = null;
                                if (str15 == null) {
                                }
                                return str15;
                            }
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("delete channel by settings change " + str18);
                            }
                            str16 = MD5;
                            str15 = null;
                        }
                        if (str15 == null) {
                            str15 = z ? notificationsController.currentAccount + "channel_" + str13 + str11 + Utilities.random.nextLong() : notificationsController.currentAccount + "channel_" + j3 + str11 + Utilities.random.nextLong();
                            if (z11) {
                                str6 = LocaleController.getString("SecretChatName", R.string.SecretChatName);
                            }
                            NotificationChannel notificationChannel2 = new NotificationChannel(str15, str6, i5);
                            notificationChannel2.setGroup(str10);
                            if (i4 != 0) {
                                z5 = true;
                                notificationChannel2.enableLights(true);
                                notificationChannel2.setLightColor(i4);
                                z6 = false;
                            } else {
                                z5 = true;
                                z6 = false;
                                notificationChannel2.enableLights(false);
                            }
                            if (!notificationsController.isEmptyVibration(jArr2)) {
                                notificationChannel2.enableVibration(z5);
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
                            notificationsSettings.edit().putString(str13, str15).putString(str13 + str12, str16).commit();
                        }
                        return str15;
                    }
                    uri2 = uri;
                    if (str15 == null) {
                    }
                    return str15;
                } else {
                    i4 = i;
                    str7 = string3;
                    str8 = string;
                    notificationsController = this;
                    str9 = "secret";
                    str10 = str4;
                    str11 = "_";
                    sb = sb4;
                    str13 = str24;
                    j3 = j;
                    i5 = i2;
                    str12 = str19;
                    jArr2 = jArr;
                }
            } else {
                i4 = i;
                str7 = string3;
                str8 = string;
                str9 = "secret";
                str10 = str4;
                str11 = "_";
                sb = sb4;
                j3 = j;
                i5 = i2;
                jArr2 = jArr;
                str12 = "_s";
                notificationsController = this;
                str13 = str24;
            }
            str14 = str7;
            str15 = str8;
            str16 = null;
            z4 = false;
            if (!z4) {
            }
            if (!z12) {
            }
            i6 = 0;
            while (i6 < jArr2.length) {
            }
            str17 = str15;
            sb.append(i4);
            uri2 = uri;
            if (uri2 != null) {
            }
            sb.append(i5);
            if (!z) {
            }
            MD5 = Utilities.MD5(sb.toString());
            if (z3) {
            }
            str15 = str17;
            str16 = MD5;
            if (str15 == null) {
            }
            return str15;
        }
        String str242 = str23 + "_" + MD52;
        string = notificationsSettings.getString(str242, null);
        String string32 = notificationsSettings.getString(str242 + "_s", null);
        StringBuilder sb42 = new StringBuilder();
        if (string == null) {
        }
        str14 = str7;
        str15 = str8;
        str16 = null;
        z4 = false;
        if (!z4) {
        }
        if (!z12) {
        }
        i6 = 0;
        while (i6 < jArr2.length) {
        }
        str17 = str15;
        sb.append(i4);
        uri2 = uri;
        if (uri2 != null) {
        }
        sb.append(i5);
        if (!z) {
        }
        MD5 = Utilities.MD5(sb.toString());
        if (z3) {
        }
        str15 = str17;
        str16 = MD5;
        if (str15 == null) {
        }
        return str15;
    }

    /* JADX WARN: Code restructure failed: missing block: B:139:0x0338, code lost:
        if (r12 == 0) goto L533;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x033a, code lost:
        r3 = org.telegram.messenger.LocaleController.getString("NotificationHiddenChatName", org.telegram.messenger.R.string.NotificationHiddenChatName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x0343, code lost:
        r3 = org.telegram.messenger.LocaleController.getString("NotificationHiddenName", org.telegram.messenger.R.string.NotificationHiddenName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:462:0x0af2, code lost:
        if (android.os.Build.VERSION.SDK_INT < 26) goto L360;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:146:0x035c A[Catch: Exception -> 0x0db8, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:150:0x0390  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x039a A[Catch: Exception -> 0x0db8, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:160:0x03b5 A[Catch: Exception -> 0x0db8, TRY_ENTER, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:161:0x03d3 A[Catch: Exception -> 0x0db8, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0424 A[Catch: Exception -> 0x0db8, TRY_ENTER, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:177:0x0490 A[Catch: Exception -> 0x0db8, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:204:0x0555 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:213:0x056a  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x062d A[Catch: Exception -> 0x0db8, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:239:0x0650  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x065f  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x0662  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x0679 A[Catch: Exception -> 0x0db8, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:256:0x06fc  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x070f  */
    /* JADX WARN: Removed duplicated region for block: B:271:0x07aa  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x0822  */
    /* JADX WARN: Removed duplicated region for block: B:292:0x0826  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x082f A[Catch: Exception -> 0x0db8, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:300:0x083f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:303:0x0845 A[Catch: Exception -> 0x0db8, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:307:0x0852  */
    /* JADX WARN: Removed duplicated region for block: B:315:0x0863 A[Catch: Exception -> 0x0db8, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:328:0x0887  */
    /* JADX WARN: Removed duplicated region for block: B:339:0x089e  */
    /* JADX WARN: Removed duplicated region for block: B:340:0x08a3  */
    /* JADX WARN: Removed duplicated region for block: B:343:0x08d8 A[Catch: Exception -> 0x0db8, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:350:0x0904 A[Catch: Exception -> 0x0db8, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:399:0x09f7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:408:0x0a38 A[Catch: all -> 0x0a4b, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:409:0x0a40  */
    /* JADX WARN: Removed duplicated region for block: B:416:0x0a52 A[Catch: Exception -> 0x0db8, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:417:0x0a58  */
    /* JADX WARN: Removed duplicated region for block: B:436:0x0ab0  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0119 A[Catch: Exception -> 0x0db8, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:505:0x0bfc A[Catch: Exception -> 0x0db8, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:506:0x0c06  */
    /* JADX WARN: Removed duplicated region for block: B:509:0x0c0b A[Catch: Exception -> 0x0db8, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0125 A[Catch: Exception -> 0x0db8, TryCatch #2 {Exception -> 0x0db8, blocks: (B:13:0x002a, B:14:0x0035, B:16:0x003d, B:18:0x004e, B:19:0x0050, B:21:0x0054, B:23:0x005e, B:25:0x006e, B:26:0x0071, B:29:0x0077, B:32:0x007e, B:33:0x0090, B:35:0x0098, B:36:0x00c9, B:38:0x00e9, B:41:0x00f1, B:43:0x00fa, B:46:0x0101, B:49:0x0119, B:67:0x01de, B:69:0x020a, B:71:0x021d, B:73:0x0223, B:75:0x0227, B:77:0x0243, B:79:0x024a, B:83:0x025c, B:87:0x026c, B:89:0x0278, B:90:0x027e, B:92:0x0292, B:94:0x02a0, B:96:0x02a6, B:105:0x02c4, B:107:0x02e0, B:117:0x02fa, B:119:0x0300, B:123:0x030c, B:125:0x0314, B:130:0x031c, B:132:0x0322, B:144:0x0355, B:146:0x035c, B:148:0x0364, B:151:0x0391, B:153:0x039a, B:162:0x040e, B:165:0x0424, B:170:0x0439, B:176:0x047b, B:205:0x0557, B:216:0x0570, B:218:0x058c, B:221:0x05c3, B:223:0x05cd, B:224:0x05e2, B:226:0x05f5, B:236:0x062d, B:241:0x0655, B:245:0x0664, B:247:0x0679, B:249:0x06b7, B:251:0x06db, B:253:0x06ed, B:260:0x0711, B:262:0x071f, B:264:0x0732, B:293:0x0829, B:295:0x082f, B:303:0x0845, B:305:0x084b, B:315:0x0863, B:318:0x086d, B:321:0x0876, B:337:0x0899, B:341:0x08a6, B:343:0x08d8, B:344:0x08e1, B:346:0x08e9, B:347:0x08f8, B:397:0x099c, B:400:0x09f9, B:402:0x09fd, B:404:0x0a03, B:416:0x0a52, B:441:0x0ab7, B:468:0x0aff, B:477:0x0b3e, B:479:0x0b46, B:481:0x0b4a, B:483:0x0b52, B:487:0x0b5d, B:505:0x0bfc, B:509:0x0c0b, B:525:0x0c6e, B:527:0x0c74, B:529:0x0c78, B:531:0x0c83, B:533:0x0c89, B:535:0x0c93, B:537:0x0ca4, B:539:0x0cb2, B:541:0x0cd3, B:542:0x0cd8, B:544:0x0d08, B:545:0x0d1b, B:549:0x0d48, B:551:0x0d4e, B:553:0x0d56, B:555:0x0d5c, B:557:0x0d6e, B:558:0x0d85, B:559:0x0d9b, B:512:0x0c1c, B:519:0x0c3d, B:522:0x0c53, B:488:0x0b89, B:489:0x0b8e, B:490:0x0b91, B:492:0x0b99, B:495:0x0ba3, B:497:0x0bab, B:501:0x0be9, B:502:0x0bf1, B:471:0x0b09, B:473:0x0b11, B:475:0x0b39, B:524:0x0c5c, B:451:0x0acc, B:455:0x0ad9, B:458:0x0ae2, B:461:0x0aec, B:418:0x0a5a, B:420:0x0a67, B:350:0x0904, B:352:0x090a, B:356:0x0919, B:359:0x0925, B:360:0x092b, B:362:0x0931, B:365:0x0936, B:367:0x093f, B:370:0x0947, B:372:0x094d, B:374:0x0951, B:376:0x0959, B:381:0x0965, B:383:0x096b, B:385:0x096f, B:387:0x0977, B:391:0x097f, B:393:0x098c, B:395:0x0992, B:263:0x072b, B:265:0x0752, B:267:0x0762, B:269:0x0775, B:268:0x076e, B:276:0x07bb, B:278:0x07c9, B:283:0x07e3, B:282:0x07dd, B:250:0x06c6, B:227:0x0601, B:229:0x0605, B:171:0x044d, B:173:0x0452, B:174:0x0466, B:177:0x0490, B:179:0x04b8, B:181:0x04d0, B:183:0x04d4, B:188:0x04de, B:190:0x04e6, B:194:0x04f3, B:195:0x0507, B:197:0x050c, B:198:0x0520, B:199:0x0533, B:201:0x053d, B:202:0x0546, B:157:0x03a8, B:160:0x03b5, B:161:0x03d3, B:149:0x0371, B:140:0x033a, B:141:0x0343, B:142:0x034c, B:121:0x0305, B:122:0x0308, B:97:0x02a9, B:99:0x02af, B:82:0x025a, B:50:0x0125, B:52:0x012b, B:53:0x012f, B:56:0x0137, B:57:0x0141, B:58:0x0154, B:60:0x015b, B:61:0x0173, B:63:0x017a, B:65:0x0182, B:66:0x01b3, B:47:0x010c, B:68:0x01fe, B:499:0x0bb5, B:330:0x088a, B:406:0x0a1c, B:408:0x0a38, B:410:0x0a41), top: B:568:0x002a, inners: #1, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:510:0x0c19  */
    /* JADX WARN: Type inference failed for: r1v73, types: [android.net.Uri] */
    /* JADX WARN: Type inference failed for: r28v2, types: [android.net.Uri] */
    /* JADX WARN: Type inference failed for: r28v3, types: [android.net.Uri] */
    /* JADX WARN: Type inference failed for: r2v134 */
    /* JADX WARN: Type inference failed for: r2v18 */
    /* JADX WARN: Type inference failed for: r2v19 */
    /* JADX WARN: Type inference failed for: r2v20 */
    /* JADX WARN: Type inference failed for: r2v21 */
    /* JADX WARN: Type inference failed for: r3v81, types: [android.content.Context] */
    /* JADX WARN: Type inference failed for: r5v125, types: [org.telegram.messenger.MessageObject] */
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
        String string;
        boolean z5;
        String str;
        TLRPC$User tLRPC$User;
        long j3;
        long j4;
        boolean z6;
        MessageObject messageObject2;
        String str2;
        NotificationCompat.Builder builder;
        boolean z7;
        String str3;
        NotificationCompat.InboxStyle inboxStyle;
        String str4;
        boolean z8;
        String str5;
        boolean z9;
        String str6;
        SharedPreferences sharedPreferences;
        long j5;
        boolean z10;
        boolean z11;
        long j6;
        boolean z12;
        boolean z13;
        CharSequence charSequence;
        String str7;
        long j7;
        int i;
        int i2;
        String str8;
        Integer num;
        boolean z14;
        String str9;
        CharSequence charSequence2;
        long j8;
        boolean z15;
        TLRPC$Chat tLRPC$Chat2;
        Object obj;
        int i3;
        String str10;
        int i4;
        int i5;
        int i6;
        boolean z16;
        String string2;
        boolean z17;
        int i7;
        boolean z18;
        int i8;
        boolean z19;
        int i9;
        boolean z20;
        int i10;
        String str11;
        int i11;
        MessageObject messageObject3;
        TLRPC$Chat tLRPC$Chat3;
        TLRPC$User tLRPC$User2;
        TLRPC$FileLocation tLRPC$FileLocation;
        TLRPC$FileLocation tLRPC$FileLocation2;
        int i12;
        long[] jArr;
        boolean z21;
        int i13;
        int i14;
        long[] jArr2;
        long[] jArr3;
        long[] jArr4;
        int i15;
        long j9;
        boolean z22;
        int i16;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList;
        int i17;
        MessageObject messageObject4;
        long[] jArr5;
        int i18;
        long j10;
        long[] jArr6;
        long[] jArr7;
        String str12;
        int ringerMode;
        String string3;
        boolean z23;
        String str13;
        int i19;
        int i20;
        int i21;
        String string4;
        boolean z24;
        int i22;
        int i23;
        String str14;
        boolean z25;
        int i24;
        String formatPluralString;
        if (!getUserConfig().isClientActivated() || ((this.pushMessages.isEmpty() && this.storyPushMessages.isEmpty()) || (!SharedConfig.showNotificationsForAllAccounts && this.currentAccount != UserConfig.selectedAccount))) {
            dismissNotification();
            return;
        }
        try {
            getConnectionsManager().resumeNetworkMaybe();
            long j11 = 0;
            StoryNotification storyNotification = null;
            for (int i25 = 0; i25 < this.pushMessages.size(); i25++) {
                MessageObject messageObject5 = this.pushMessages.get(i25);
                int i26 = messageObject5.messageOwner.date;
                if (j11 < i26) {
                    j11 = i26;
                    storyNotification = messageObject5;
                }
            }
            for (int i27 = 0; i27 < this.storyPushMessages.size(); i27++) {
                StoryNotification storyNotification2 = this.storyPushMessages.get(i27);
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
                boolean z26 = false;
                int i28 = 0;
                for (int i29 = 0; i29 < this.storyPushMessages.size(); i29++) {
                    z26 |= this.storyPushMessages.get(i29).hidden;
                    tLRPC$TL_message.date = Math.min(tLRPC$TL_message.date, (int) (this.storyPushMessages.get(i29).date / 1000));
                    i28 += this.storyPushMessages.get(i29).dateByIds.size();
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
                if (!z26 && this.storyPushMessages.size() < 2 && !arrayList2.isEmpty()) {
                    formatPluralString = arrayList2.get(0);
                    i24 = 0;
                    if (!z26) {
                        tLRPC$TL_message.message = LocaleController.formatPluralString("StoryNotificationHidden", i28, new Object[i24]);
                    } else if (arrayList2.isEmpty()) {
                        tLRPC$TL_message.message = "";
                    } else if (arrayList2.size() == 1) {
                        if (i28 == 1) {
                            tLRPC$TL_message.message = LocaleController.getString("StoryNotificationSingle");
                        } else {
                            tLRPC$TL_message.message = LocaleController.formatPluralString("StoryNotification1", i28, arrayList2.get(0));
                        }
                    } else if (arrayList2.size() == 2) {
                        tLRPC$TL_message.message = LocaleController.formatString(R.string.StoryNotification2, arrayList2.get(0), arrayList2.get(1));
                    } else if (arrayList2.size() == 3 && this.storyPushMessages.size() == 3) {
                        tLRPC$TL_message.message = LocaleController.formatString(R.string.StoryNotification3, cutLastName(arrayList2.get(0)), cutLastName(arrayList2.get(1)), cutLastName(arrayList2.get(2)));
                    } else {
                        tLRPC$TL_message.message = LocaleController.formatPluralString("StoryNotification4", this.storyPushMessages.size() - 2, cutLastName(arrayList2.get(0)), cutLastName(arrayList2.get(1)));
                    }
                    MessageObject messageObject6 = new MessageObject(this.currentAccount, tLRPC$TL_message, tLRPC$TL_message.message, formatPluralString, formatPluralString, false, false, false, false);
                    messageObject6.isStoryPush = true;
                    messageObject = messageObject6;
                    bitmap = loadMultipleAvatars;
                }
                i24 = 0;
                formatPluralString = LocaleController.formatPluralString("Stories", i28, new Object[0]);
                if (!z26) {
                }
                MessageObject messageObject62 = new MessageObject(this.currentAccount, tLRPC$TL_message, tLRPC$TL_message.message, formatPluralString, formatPluralString, false, false, false, false);
                messageObject62.isStoryPush = true;
                messageObject = messageObject62;
                bitmap = loadMultipleAvatars;
            } else {
                messageObject = this.pushMessages.get(0);
                bitmap = null;
            }
            SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
            int i30 = notificationsSettings.getInt("dismissDate", 0);
            if (!messageObject.isStoryPush && messageObject.messageOwner.date <= i30) {
                dismissNotification();
                return;
            }
            long dialogId = messageObject.getDialogId();
            long topicId = MessageObject.getTopicId(this.currentAccount, messageObject.messageOwner, getMessagesController().isForum(messageObject));
            boolean z27 = messageObject.isStoryPush;
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
                    z25 = messageObject.localChannel;
                } else {
                    z25 = ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup;
                }
                j = j15;
                z2 = z25;
                j2 = topicId;
            } else {
                j = j15;
                j2 = topicId;
                tLRPC$Chat = null;
                z2 = false;
            }
            Bitmap bitmap2 = bitmap;
            TLRPC$Chat tLRPC$Chat4 = tLRPC$Chat;
            int notifyOverride = getNotifyOverride(notificationsSettings, fromChatId, j2);
            if (notifyOverride == -1) {
                z3 = isGlobalNotificationsEnabled(dialogId, Boolean.valueOf(z2));
            } else {
                z3 = notifyOverride != 2;
            }
            if (((j14 != 0 && tLRPC$Chat4 == null) || user == null) && messageObject.isFcmMessage()) {
                userName = messageObject.localName;
            } else if (tLRPC$Chat4 != null) {
                userName = tLRPC$Chat4.title;
            } else {
                userName = UserObject.getUserName(user);
            }
            String str15 = userName;
            if (!AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
                z4 = false;
                if (!DialogObject.isEncryptedDialog(dialogId) && this.pushDialogs.size() <= 1 && !z4) {
                    string = str15;
                    z5 = true;
                    if (UserConfig.getActivatedAccountsCount() > 1) {
                        str = "";
                    } else if (this.pushDialogs.size() == 1) {
                        str = UserObject.getFirstName(getUserConfig().getCurrentUser());
                    } else {
                        str = UserObject.getFirstName(getUserConfig().getCurrentUser()) + "・";
                    }
                    if (this.pushDialogs.size() == 1 && Build.VERSION.SDK_INT >= 23) {
                        tLRPC$User = user;
                        j4 = dialogId;
                        j3 = j14;
                        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                        if (this.pushMessages.size() > 1) {
                            boolean[] zArr = new boolean[1];
                            z6 = z3;
                            str3 = getStringForMessage(messageObject, false, zArr, null);
                            z7 = isSilentMessage(messageObject);
                            if (str3 == null) {
                                return;
                            }
                            if (!z5) {
                                str14 = str3;
                            } else if (tLRPC$Chat4 != null) {
                                str14 = str3.replace(" @ " + string, "");
                            } else if (zArr[0]) {
                                str14 = str3.replace(string + ": ", "");
                            } else {
                                str14 = str3.replace(string + " ", "");
                            }
                            builder2.setContentText(str14);
                            builder2.setStyle(new NotificationCompat.BigTextStyle().bigText(str14));
                            builder = builder2;
                            str2 = str;
                            messageObject2 = messageObject;
                        } else {
                            z6 = z3;
                            builder2.setContentText(str);
                            NotificationCompat.InboxStyle inboxStyle2 = new NotificationCompat.InboxStyle();
                            inboxStyle2.setBigContentTitle(string);
                            int min = Math.min(10, this.pushMessages.size());
                            messageObject2 = messageObject;
                            boolean[] zArr2 = new boolean[1];
                            boolean z28 = 2;
                            int i31 = 0;
                            String str16 = null;
                            while (i31 < min) {
                                int i32 = min;
                                MessageObject messageObject7 = this.pushMessages.get(i31);
                                String str17 = str;
                                NotificationCompat.InboxStyle inboxStyle3 = inboxStyle2;
                                int i33 = i31;
                                String stringForMessage = getStringForMessage(messageObject7, false, zArr2, null);
                                if (stringForMessage != null && (messageObject7.isStoryPush || messageObject7.messageOwner.date > i30)) {
                                    if (z28 == 2) {
                                        str4 = stringForMessage;
                                        z28 = isSilentMessage(messageObject7);
                                    } else {
                                        str4 = str16;
                                        z28 = z28;
                                    }
                                    if (this.pushDialogs.size() == 1 && z5) {
                                        if (tLRPC$Chat4 != null) {
                                            stringForMessage = stringForMessage.replace(" @ " + string, "");
                                        } else if (zArr2[0]) {
                                            stringForMessage = stringForMessage.replace(string + ": ", "");
                                        } else {
                                            stringForMessage = stringForMessage.replace(string + " ", "");
                                        }
                                    }
                                    inboxStyle = inboxStyle3;
                                    inboxStyle.addLine(stringForMessage);
                                    str16 = str4;
                                    i31 = i33 + 1;
                                    inboxStyle2 = inboxStyle;
                                    min = i32;
                                    str = str17;
                                    z28 = z28;
                                }
                                inboxStyle = inboxStyle3;
                                i31 = i33 + 1;
                                inboxStyle2 = inboxStyle;
                                min = i32;
                                str = str17;
                                z28 = z28;
                            }
                            str2 = str;
                            NotificationCompat.InboxStyle inboxStyle4 = inboxStyle2;
                            inboxStyle4.setSummaryText(str2);
                            builder = builder2;
                            builder.setStyle(inboxStyle4);
                            z7 = z28 == true ? 1 : 0;
                            str3 = str16;
                        }
                        if (z && z6 && !MediaController.getInstance().isRecordingAudio() && !z7) {
                            z8 = false;
                            if (z8 && j4 == fromChatId && tLRPC$Chat4 != null) {
                                StringBuilder sb = new StringBuilder();
                                sb.append(NotificationsSettingsFacade.PROPERTY_CUSTOM);
                                j5 = j4;
                                sb.append(j5);
                                sharedPreferences = notificationsSettings;
                                if (sharedPreferences.getBoolean(sb.toString(), false)) {
                                    i22 = sharedPreferences.getInt("smart_max_count_" + j5, 2);
                                    i23 = sharedPreferences.getInt("smart_delay_" + j5, 180);
                                } else {
                                    i22 = 2;
                                    i23 = 180;
                                }
                                if (i22 != 0) {
                                    Point point = this.smartNotificationsDialogs.get(j5);
                                    if (point == null) {
                                        this.smartNotificationsDialogs.put(j5, new Point(1, (int) (SystemClock.elapsedRealtime() / 1000)));
                                    } else {
                                        z9 = z8;
                                        int i34 = point.y + i23;
                                        str5 = str3;
                                        if (i34 < SystemClock.elapsedRealtime() / 1000) {
                                            point.set(1, (int) (SystemClock.elapsedRealtime() / 1000));
                                            str6 = str2;
                                        } else {
                                            int i35 = point.x;
                                            if (i35 < i22) {
                                                str6 = str2;
                                                point.set(i35 + 1, (int) (SystemClock.elapsedRealtime() / 1000));
                                            } else {
                                                str6 = str2;
                                                z10 = true;
                                                if (z10) {
                                                    z11 = z10;
                                                    j6 = j2;
                                                } else {
                                                    StringBuilder sb2 = new StringBuilder();
                                                    sb2.append("sound_enabled_");
                                                    z11 = z10;
                                                    j6 = j2;
                                                    sb2.append(getSharedPrefKey(j5, j6));
                                                    if (!sharedPreferences.getBoolean(sb2.toString(), true)) {
                                                        z12 = true;
                                                        String path = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                                                        z13 = ApplicationLoader.mainInterfacePaused;
                                                        getSharedPrefKey(j5, j6);
                                                        if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, j6, false)) {
                                                            charSequence = "";
                                                            str7 = str5;
                                                            j7 = j6;
                                                            i = 0;
                                                            i2 = 3;
                                                            str8 = null;
                                                            num = null;
                                                            z14 = false;
                                                        } else {
                                                            int property = this.dialogsNotificationsFacade.getProperty("vibrate_", j5, j6, 0);
                                                            int property2 = this.dialogsNotificationsFacade.getProperty("priority_", j5, j6, 3);
                                                            str7 = str5;
                                                            long property3 = this.dialogsNotificationsFacade.getProperty("sound_document_id_", j5, j6, 0L);
                                                            if (property3 != 0) {
                                                                charSequence = "";
                                                                str8 = getMediaDataController().ringtoneDataStore.getSoundPath(property3);
                                                                z24 = true;
                                                            } else {
                                                                charSequence = "";
                                                                str8 = this.dialogsNotificationsFacade.getPropertyString("sound_path_", j5, j6, null);
                                                                z24 = false;
                                                            }
                                                            int property4 = this.dialogsNotificationsFacade.getProperty("color_", j5, j6, 0);
                                                            num = property4 != 0 ? Integer.valueOf(property4) : null;
                                                            j7 = j6;
                                                            z14 = z24;
                                                            i = property;
                                                            i2 = property2;
                                                        }
                                                        NotificationCompat.Builder builder3 = builder;
                                                        if (j3 == 0) {
                                                            if (z2) {
                                                                str9 = str7;
                                                                charSequence2 = string;
                                                                long j16 = sharedPreferences.getLong("ChannelSoundDocId", 0L);
                                                                if (j16 != 0) {
                                                                    string4 = getMediaDataController().ringtoneDataStore.getSoundPath(j16);
                                                                    z23 = true;
                                                                } else {
                                                                    string4 = sharedPreferences.getString("ChannelSoundPath", path);
                                                                    z23 = false;
                                                                }
                                                                int i36 = sharedPreferences.getInt("vibrate_channel", 0);
                                                                str13 = string4;
                                                                i19 = sharedPreferences.getInt("priority_channel", 1);
                                                                i20 = i36;
                                                                i21 = sharedPreferences.getInt("ChannelLed", -16776961);
                                                                i6 = 2;
                                                            } else {
                                                                str9 = str7;
                                                                charSequence2 = string;
                                                                long j17 = sharedPreferences.getLong("GroupSoundDocId", 0L);
                                                                if (j17 != 0) {
                                                                    string3 = getMediaDataController().ringtoneDataStore.getSoundPath(j17);
                                                                    z23 = true;
                                                                } else {
                                                                    string3 = sharedPreferences.getString("GroupSoundPath", path);
                                                                    z23 = false;
                                                                }
                                                                int i37 = sharedPreferences.getInt("vibrate_group", 0);
                                                                str13 = string3;
                                                                i19 = sharedPreferences.getInt("priority_group", 1);
                                                                i20 = i37;
                                                                i21 = sharedPreferences.getInt("GroupLed", -16776961);
                                                                i6 = 0;
                                                            }
                                                            long j18 = j;
                                                            obj = path;
                                                            z16 = z23;
                                                            i3 = i21;
                                                            j8 = j18;
                                                            i5 = i19;
                                                            str10 = str13;
                                                            z15 = z7;
                                                            i4 = i20;
                                                            tLRPC$Chat2 = tLRPC$Chat4;
                                                        } else {
                                                            str9 = str7;
                                                            charSequence2 = string;
                                                            j8 = j;
                                                            if (j8 != 0) {
                                                                z15 = z7;
                                                                tLRPC$Chat2 = tLRPC$Chat4;
                                                                long j19 = sharedPreferences.getLong(z27 ? "StoriesSoundDocId" : "GlobalSoundDocId", 0L);
                                                                if (j19 != 0) {
                                                                    string2 = getMediaDataController().ringtoneDataStore.getSoundPath(j19);
                                                                    z17 = true;
                                                                } else {
                                                                    string2 = sharedPreferences.getString(z27 ? "StoriesSoundPath" : "GlobalSoundPath", path);
                                                                    z17 = false;
                                                                }
                                                                i4 = sharedPreferences.getInt("vibrate_messages", 0);
                                                                z16 = z17;
                                                                i5 = sharedPreferences.getInt("priority_messages", 1);
                                                                String str18 = string2;
                                                                i3 = sharedPreferences.getInt("MessagesLed", -16776961);
                                                                i6 = z27 ? 3 : 1;
                                                                str10 = str18;
                                                                obj = path;
                                                            } else {
                                                                z15 = z7;
                                                                tLRPC$Chat2 = tLRPC$Chat4;
                                                                obj = path;
                                                                i3 = -16776961;
                                                                str10 = null;
                                                                i4 = 0;
                                                                i5 = 0;
                                                                i6 = 1;
                                                                z16 = false;
                                                            }
                                                        }
                                                        if (i4 != 4) {
                                                            z18 = true;
                                                            i7 = 0;
                                                        } else {
                                                            i7 = i4;
                                                            z18 = false;
                                                        }
                                                        if (!TextUtils.isEmpty(str8) || TextUtils.equals(str10, str8)) {
                                                            str8 = str10;
                                                            z14 = z16;
                                                            i8 = 3;
                                                            z19 = true;
                                                        } else {
                                                            i8 = 3;
                                                            z19 = false;
                                                        }
                                                        if (i2 != i8 && i5 != i2) {
                                                            i5 = i2;
                                                            z19 = false;
                                                        }
                                                        if (num != null && num.intValue() != i3) {
                                                            i3 = num.intValue();
                                                            z19 = false;
                                                        }
                                                        if (i != 0 || i == 4) {
                                                            i9 = i7;
                                                        } else {
                                                            i9 = i7;
                                                            if (i != i9) {
                                                                i9 = i;
                                                                z20 = false;
                                                                if (z13) {
                                                                    if (!sharedPreferences.getBoolean("EnableInAppSounds", true)) {
                                                                        str8 = null;
                                                                    }
                                                                    if (!sharedPreferences.getBoolean("EnableInAppVibrate", true)) {
                                                                        i9 = 2;
                                                                    }
                                                                    if (!sharedPreferences.getBoolean("EnableInAppPriority", false)) {
                                                                        i5 = 0;
                                                                    } else if (i5 == 2) {
                                                                        i5 = 1;
                                                                    }
                                                                }
                                                                if (z18 && i9 != 2) {
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
                                                                    str11 = null;
                                                                    i10 = 0;
                                                                    i11 = 0;
                                                                    i5 = 0;
                                                                } else {
                                                                    i10 = i9;
                                                                    str11 = str8;
                                                                    i11 = i3;
                                                                }
                                                                Intent intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                                intent.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                                intent.setFlags(ConnectionsManager.FileTypeFile);
                                                                messageObject3 = messageObject2;
                                                                if (messageObject3.isStoryPush) {
                                                                    long[] jArr8 = new long[this.storyPushMessages.size()];
                                                                    for (int i38 = 0; i38 < this.storyPushMessages.size(); i38++) {
                                                                        jArr8[i38] = this.storyPushMessages.get(i38).dialogId;
                                                                    }
                                                                    intent.putExtra("storyDialogIds", jArr8);
                                                                } else {
                                                                    if (!DialogObject.isEncryptedDialog(j5)) {
                                                                        if (this.pushDialogs.size() == 1) {
                                                                            if (j3 != 0) {
                                                                                intent.putExtra("chatId", j3);
                                                                            } else if (j8 != 0) {
                                                                                intent.putExtra("userId", j8);
                                                                            }
                                                                        }
                                                                        if (!AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
                                                                            if (this.pushDialogs.size() == 1 && Build.VERSION.SDK_INT < 28) {
                                                                                if (tLRPC$Chat2 != null) {
                                                                                    tLRPC$Chat3 = tLRPC$Chat2;
                                                                                    TLRPC$ChatPhoto tLRPC$ChatPhoto = tLRPC$Chat3.photo;
                                                                                    if (tLRPC$ChatPhoto != null && (tLRPC$FileLocation2 = tLRPC$ChatPhoto.photo_small) != null && tLRPC$FileLocation2.volume_id != 0 && tLRPC$FileLocation2.local_id != 0) {
                                                                                        tLRPC$FileLocation = tLRPC$FileLocation2;
                                                                                        tLRPC$User2 = tLRPC$User;
                                                                                    }
                                                                                    tLRPC$User2 = tLRPC$User;
                                                                                } else {
                                                                                    tLRPC$Chat3 = tLRPC$Chat2;
                                                                                    if (tLRPC$User != null) {
                                                                                        tLRPC$User2 = tLRPC$User;
                                                                                        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = tLRPC$User2.photo;
                                                                                        if (tLRPC$UserProfilePhoto != null && (tLRPC$FileLocation = tLRPC$UserProfilePhoto.photo_small) != null && tLRPC$FileLocation.volume_id != 0 && tLRPC$FileLocation.local_id != 0) {
                                                                                        }
                                                                                    }
                                                                                    tLRPC$User2 = tLRPC$User;
                                                                                }
                                                                                intent.putExtra("currentAccount", this.currentAccount);
                                                                                long j20 = j5;
                                                                                int i39 = i11;
                                                                                builder3.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                                                                                builder3.setCategory("msg");
                                                                                if (tLRPC$Chat3 == null && tLRPC$User2 != null && (str12 = tLRPC$User2.phone) != null && str12.length() > 0) {
                                                                                    builder3.addPerson("tel:+" + tLRPC$User2.phone);
                                                                                }
                                                                                Intent intent2 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                                                intent2.putExtra("messageDate", messageObject3.messageOwner.date);
                                                                                intent2.putExtra("currentAccount", this.currentAccount);
                                                                                if (messageObject3.isStoryPush) {
                                                                                    i12 = 1;
                                                                                    intent2.putExtra("story", true);
                                                                                } else {
                                                                                    i12 = 1;
                                                                                }
                                                                                builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent2, 167772160));
                                                                                if (bitmap2 != null) {
                                                                                    builder3.setLargeIcon(bitmap2);
                                                                                } else if (tLRPC$FileLocation != null) {
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
                                                                                    boolean z29 = z15;
                                                                                    if (z || z29) {
                                                                                        builder3.setPriority(-1);
                                                                                    } else if (i5 == 0) {
                                                                                        builder3.setPriority(0);
                                                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                                                            z21 = true;
                                                                                            i13 = 3;
                                                                                        }
                                                                                        z21 = true;
                                                                                        i13 = 0;
                                                                                    } else {
                                                                                        int i40 = 1;
                                                                                        if (i5 != 1) {
                                                                                            if (i5 == 2) {
                                                                                                i40 = 1;
                                                                                            } else {
                                                                                                if (i5 == 4) {
                                                                                                    builder3.setPriority(-2);
                                                                                                    if (Build.VERSION.SDK_INT >= 26) {
                                                                                                        z21 = true;
                                                                                                        i13 = 1;
                                                                                                    }
                                                                                                } else if (i5 == 5) {
                                                                                                    builder3.setPriority(-1);
                                                                                                    if (Build.VERSION.SDK_INT >= 26) {
                                                                                                        z21 = true;
                                                                                                        i13 = 2;
                                                                                                    }
                                                                                                }
                                                                                                z21 = true;
                                                                                                i13 = 0;
                                                                                            }
                                                                                        }
                                                                                        builder3.setPriority(i40);
                                                                                        if (Build.VERSION.SDK_INT >= 26) {
                                                                                            z21 = true;
                                                                                            i13 = 4;
                                                                                        }
                                                                                        z21 = true;
                                                                                        i13 = 0;
                                                                                    }
                                                                                    if (z29 != z21 && !z12) {
                                                                                        if (!z13 || (sharedPreferences.getBoolean("EnableInAppPreview", z21) && str9 != null)) {
                                                                                            builder3.setTicker(str9.length() > 100 ? str9.substring(0, 100).replace('\n', ' ').trim() + "..." : str9);
                                                                                        }
                                                                                        if (str11 != null && !str11.equalsIgnoreCase("NoSound")) {
                                                                                            int i41 = Build.VERSION.SDK_INT;
                                                                                            if (i41 >= 26) {
                                                                                                if (!str11.equalsIgnoreCase("Default") && !str11.equals(obj)) {
                                                                                                    if (z14) {
                                                                                                        ?? uriForFile = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", new File(str11));
                                                                                                        ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile, 1);
                                                                                                        jArr6 = uriForFile;
                                                                                                    } else {
                                                                                                        jArr6 = Uri.parse(str11);
                                                                                                    }
                                                                                                    if (i39 != 0) {
                                                                                                        i14 = i39;
                                                                                                        builder3.setLights(i14, 1000, 1000);
                                                                                                    } else {
                                                                                                        i14 = i39;
                                                                                                    }
                                                                                                    if (i10 == 2) {
                                                                                                        jArr7 = new long[]{0, 0};
                                                                                                        builder3.setVibrate(jArr7);
                                                                                                    } else if (i10 == 1) {
                                                                                                        jArr7 = new long[]{0, 100, 0, 100};
                                                                                                        builder3.setVibrate(jArr7);
                                                                                                    } else {
                                                                                                        if (i10 != 0 && i10 != 4) {
                                                                                                            if (i10 == 3) {
                                                                                                                jArr7 = new long[]{0, 1000};
                                                                                                                builder3.setVibrate(jArr7);
                                                                                                            } else {
                                                                                                                jArr2 = jArr;
                                                                                                                jArr3 = jArr6;
                                                                                                            }
                                                                                                        }
                                                                                                        builder3.setDefaults(2);
                                                                                                        jArr7 = new long[0];
                                                                                                    }
                                                                                                    jArr2 = jArr7;
                                                                                                    jArr3 = jArr6;
                                                                                                }
                                                                                                jArr6 = Settings.System.DEFAULT_NOTIFICATION_URI;
                                                                                                if (i39 != 0) {
                                                                                                }
                                                                                                if (i10 == 2) {
                                                                                                }
                                                                                                jArr2 = jArr7;
                                                                                                jArr3 = jArr6;
                                                                                            } else if (str11.equals(obj)) {
                                                                                                builder3.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, 5);
                                                                                            } else if (i41 >= 24 && str11.startsWith("file://") && !AndroidUtilities.isInternalUri(Uri.parse(str11))) {
                                                                                                try {
                                                                                                    Uri uriForFile2 = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", new File(str11.replace("file://", charSequence)));
                                                                                                    ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile2, 1);
                                                                                                    builder3.setSound(uriForFile2, 5);
                                                                                                } catch (Exception unused2) {
                                                                                                    builder3.setSound(Uri.parse(str11), 5);
                                                                                                }
                                                                                            } else {
                                                                                                builder3.setSound(Uri.parse(str11), 5);
                                                                                            }
                                                                                        }
                                                                                        jArr6 = jArr;
                                                                                        if (i39 != 0) {
                                                                                        }
                                                                                        if (i10 == 2) {
                                                                                        }
                                                                                        jArr2 = jArr7;
                                                                                        jArr3 = jArr6;
                                                                                    } else {
                                                                                        i14 = i39;
                                                                                        long[] jArr9 = {0, 0};
                                                                                        builder3.setVibrate(jArr9);
                                                                                        jArr2 = jArr9;
                                                                                        jArr3 = jArr;
                                                                                    }
                                                                                    if (AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter || messageObject3.getDialogId() != 777000 || (tLRPC$ReplyMarkup = messageObject3.messageOwner.reply_markup) == null) {
                                                                                        jArr4 = jArr3;
                                                                                        i15 = i13;
                                                                                        j9 = j20;
                                                                                        z22 = false;
                                                                                    } else {
                                                                                        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList4 = tLRPC$ReplyMarkup.rows;
                                                                                        int size = arrayList4.size();
                                                                                        int i42 = 0;
                                                                                        boolean z30 = false;
                                                                                        while (i42 < size) {
                                                                                            TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow = arrayList4.get(i42);
                                                                                            int size2 = tLRPC$TL_keyboardButtonRow.buttons.size();
                                                                                            boolean z31 = z30;
                                                                                            int i43 = 0;
                                                                                            while (i43 < size2) {
                                                                                                TLRPC$KeyboardButton tLRPC$KeyboardButton = tLRPC$TL_keyboardButtonRow.buttons.get(i43);
                                                                                                int i44 = size2;
                                                                                                if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonCallback) {
                                                                                                    arrayList = arrayList4;
                                                                                                    i17 = size;
                                                                                                    Intent intent3 = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                                                                                    intent3.putExtra("currentAccount", this.currentAccount);
                                                                                                    jArr5 = jArr3;
                                                                                                    i18 = i13;
                                                                                                    j10 = j20;
                                                                                                    intent3.putExtra("did", j10);
                                                                                                    byte[] bArr = tLRPC$KeyboardButton.data;
                                                                                                    if (bArr != null) {
                                                                                                        intent3.putExtra("data", bArr);
                                                                                                    }
                                                                                                    intent3.putExtra("mid", messageObject3.getId());
                                                                                                    String str19 = tLRPC$KeyboardButton.text;
                                                                                                    Context context = ApplicationLoader.applicationContext;
                                                                                                    int i45 = this.lastButtonId;
                                                                                                    messageObject4 = messageObject3;
                                                                                                    this.lastButtonId = i45 + 1;
                                                                                                    builder3.addAction(0, str19, PendingIntent.getBroadcast(context, i45, intent3, 167772160));
                                                                                                    z31 = true;
                                                                                                } else {
                                                                                                    arrayList = arrayList4;
                                                                                                    i17 = size;
                                                                                                    messageObject4 = messageObject3;
                                                                                                    jArr5 = jArr3;
                                                                                                    i18 = i13;
                                                                                                    j10 = j20;
                                                                                                }
                                                                                                i43++;
                                                                                                size2 = i44;
                                                                                                j20 = j10;
                                                                                                arrayList4 = arrayList;
                                                                                                size = i17;
                                                                                                i13 = i18;
                                                                                                jArr3 = jArr5;
                                                                                                messageObject3 = messageObject4;
                                                                                            }
                                                                                            i42++;
                                                                                            z30 = z31;
                                                                                            arrayList4 = arrayList4;
                                                                                            i13 = i13;
                                                                                            jArr3 = jArr3;
                                                                                        }
                                                                                        jArr4 = jArr3;
                                                                                        i15 = i13;
                                                                                        j9 = j20;
                                                                                        z22 = z30;
                                                                                    }
                                                                                    if (!z22 && (i16 = Build.VERSION.SDK_INT) < 24 && SharedConfig.passcodeHash.length() == 0 && hasMessagesToReply()) {
                                                                                        Intent intent4 = new Intent(ApplicationLoader.applicationContext, PopupReplyReceiver.class);
                                                                                        intent4.putExtra("currentAccount", this.currentAccount);
                                                                                        if (i16 <= 19) {
                                                                                            builder3.addAction(R.drawable.ic_ab_reply2, LocaleController.getString("Reply", R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent4, 167772160));
                                                                                        } else {
                                                                                            builder3.addAction(R.drawable.ic_ab_reply, LocaleController.getString("Reply", R.string.Reply), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent4, 167772160));
                                                                                        }
                                                                                    }
                                                                                    showExtraNotifications(builder3, str6, j9, j7, str15, jArr2, i14, jArr4, i15, z20, z13, z12, i6);
                                                                                    scheduleNotificationRepeat();
                                                                                    return;
                                                                                }
                                                                                jArr = null;
                                                                                boolean z292 = z15;
                                                                                if (z) {
                                                                                }
                                                                                builder3.setPriority(-1);
                                                                            }
                                                                        }
                                                                        tLRPC$Chat3 = tLRPC$Chat2;
                                                                        tLRPC$User2 = tLRPC$User;
                                                                    } else {
                                                                        tLRPC$Chat3 = tLRPC$Chat2;
                                                                        tLRPC$User2 = tLRPC$User;
                                                                        if (this.pushDialogs.size() == 1 && j5 != globalSecretChatId) {
                                                                            intent.putExtra("encId", DialogObject.getEncryptedChatId(j5));
                                                                        }
                                                                    }
                                                                    tLRPC$FileLocation = null;
                                                                    intent.putExtra("currentAccount", this.currentAccount);
                                                                    long j202 = j5;
                                                                    int i392 = i11;
                                                                    builder3.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                                                                    builder3.setCategory("msg");
                                                                    if (tLRPC$Chat3 == null) {
                                                                        builder3.addPerson("tel:+" + tLRPC$User2.phone);
                                                                    }
                                                                    Intent intent22 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                                    intent22.putExtra("messageDate", messageObject3.messageOwner.date);
                                                                    intent22.putExtra("currentAccount", this.currentAccount);
                                                                    if (messageObject3.isStoryPush) {
                                                                    }
                                                                    builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent22, 167772160));
                                                                    if (bitmap2 != null) {
                                                                    }
                                                                    jArr = null;
                                                                    boolean z2922 = z15;
                                                                    if (z) {
                                                                    }
                                                                    builder3.setPriority(-1);
                                                                }
                                                                tLRPC$Chat3 = tLRPC$Chat2;
                                                                tLRPC$User2 = tLRPC$User;
                                                                tLRPC$FileLocation = null;
                                                                intent.putExtra("currentAccount", this.currentAccount);
                                                                long j2022 = j5;
                                                                int i3922 = i11;
                                                                builder3.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                                                                builder3.setCategory("msg");
                                                                if (tLRPC$Chat3 == null) {
                                                                }
                                                                Intent intent222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                                intent222.putExtra("messageDate", messageObject3.messageOwner.date);
                                                                intent222.putExtra("currentAccount", this.currentAccount);
                                                                if (messageObject3.isStoryPush) {
                                                                }
                                                                builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent222, 167772160));
                                                                if (bitmap2 != null) {
                                                                }
                                                                jArr = null;
                                                                boolean z29222 = z15;
                                                                if (z) {
                                                                }
                                                                builder3.setPriority(-1);
                                                            }
                                                        }
                                                        z20 = z19;
                                                        if (z13) {
                                                        }
                                                        if (z18) {
                                                            ringerMode = audioManager.getRingerMode();
                                                            if (ringerMode != 0) {
                                                                i9 = 2;
                                                            }
                                                        }
                                                        if (z12) {
                                                        }
                                                        Intent intent5 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                        intent5.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                        intent5.setFlags(ConnectionsManager.FileTypeFile);
                                                        messageObject3 = messageObject2;
                                                        if (messageObject3.isStoryPush) {
                                                        }
                                                        tLRPC$Chat3 = tLRPC$Chat2;
                                                        tLRPC$User2 = tLRPC$User;
                                                        tLRPC$FileLocation = null;
                                                        intent5.putExtra("currentAccount", this.currentAccount);
                                                        long j20222 = j5;
                                                        int i39222 = i11;
                                                        builder3.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent5, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                                                        builder3.setCategory("msg");
                                                        if (tLRPC$Chat3 == null) {
                                                        }
                                                        Intent intent2222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                        intent2222.putExtra("messageDate", messageObject3.messageOwner.date);
                                                        intent2222.putExtra("currentAccount", this.currentAccount);
                                                        if (messageObject3.isStoryPush) {
                                                        }
                                                        builder3.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent2222, 167772160));
                                                        if (bitmap2 != null) {
                                                        }
                                                        jArr = null;
                                                        boolean z292222 = z15;
                                                        if (z) {
                                                        }
                                                        builder3.setPriority(-1);
                                                    }
                                                }
                                                z12 = z11;
                                                String path2 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                                                if (ApplicationLoader.mainInterfacePaused) {
                                                }
                                                getSharedPrefKey(j5, j6);
                                                if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, j6, false)) {
                                                }
                                                NotificationCompat.Builder builder32 = builder;
                                                if (j3 == 0) {
                                                }
                                                if (i4 != 4) {
                                                }
                                                if (TextUtils.isEmpty(str8)) {
                                                }
                                                str8 = str10;
                                                z14 = z16;
                                                i8 = 3;
                                                z19 = true;
                                                if (i2 != i8) {
                                                    i5 = i2;
                                                    z19 = false;
                                                }
                                                if (num != null) {
                                                    i3 = num.intValue();
                                                    z19 = false;
                                                }
                                                if (i != 0) {
                                                }
                                                i9 = i7;
                                                z20 = z19;
                                                if (z13) {
                                                }
                                                if (z18) {
                                                }
                                                if (z12) {
                                                }
                                                Intent intent52 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                intent52.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                intent52.setFlags(ConnectionsManager.FileTypeFile);
                                                messageObject3 = messageObject2;
                                                if (messageObject3.isStoryPush) {
                                                }
                                                tLRPC$Chat3 = tLRPC$Chat2;
                                                tLRPC$User2 = tLRPC$User;
                                                tLRPC$FileLocation = null;
                                                intent52.putExtra("currentAccount", this.currentAccount);
                                                long j202222 = j5;
                                                int i392222 = i11;
                                                builder32.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent52, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                                                builder32.setCategory("msg");
                                                if (tLRPC$Chat3 == null) {
                                                }
                                                Intent intent22222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                intent22222.putExtra("messageDate", messageObject3.messageOwner.date);
                                                intent22222.putExtra("currentAccount", this.currentAccount);
                                                if (messageObject3.isStoryPush) {
                                                }
                                                builder32.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent22222, 167772160));
                                                if (bitmap2 != null) {
                                                }
                                                jArr = null;
                                                boolean z2922222 = z15;
                                                if (z) {
                                                }
                                                builder32.setPriority(-1);
                                            }
                                        }
                                    }
                                }
                                str5 = str3;
                                z9 = z8;
                                str6 = str2;
                            } else {
                                str5 = str3;
                                z9 = z8;
                                str6 = str2;
                                sharedPreferences = notificationsSettings;
                                j5 = j4;
                            }
                            z10 = z9;
                            if (z10) {
                            }
                            z12 = z11;
                            String path22 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                            if (ApplicationLoader.mainInterfacePaused) {
                            }
                            getSharedPrefKey(j5, j6);
                            if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, j6, false)) {
                            }
                            NotificationCompat.Builder builder322 = builder;
                            if (j3 == 0) {
                            }
                            if (i4 != 4) {
                            }
                            if (TextUtils.isEmpty(str8)) {
                            }
                            str8 = str10;
                            z14 = z16;
                            i8 = 3;
                            z19 = true;
                            if (i2 != i8) {
                            }
                            if (num != null) {
                            }
                            if (i != 0) {
                            }
                            i9 = i7;
                            z20 = z19;
                            if (z13) {
                            }
                            if (z18) {
                            }
                            if (z12) {
                            }
                            Intent intent522 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                            intent522.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                            intent522.setFlags(ConnectionsManager.FileTypeFile);
                            messageObject3 = messageObject2;
                            if (messageObject3.isStoryPush) {
                            }
                            tLRPC$Chat3 = tLRPC$Chat2;
                            tLRPC$User2 = tLRPC$User;
                            tLRPC$FileLocation = null;
                            intent522.putExtra("currentAccount", this.currentAccount);
                            long j2022222 = j5;
                            int i3922222 = i11;
                            builder322.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent522, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                            builder322.setCategory("msg");
                            if (tLRPC$Chat3 == null) {
                            }
                            Intent intent222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                            intent222222.putExtra("messageDate", messageObject3.messageOwner.date);
                            intent222222.putExtra("currentAccount", this.currentAccount);
                            if (messageObject3.isStoryPush) {
                            }
                            builder322.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent222222, 167772160));
                            if (bitmap2 != null) {
                            }
                            jArr = null;
                            boolean z29222222 = z15;
                            if (z) {
                            }
                            builder322.setPriority(-1);
                        }
                        z8 = true;
                        if (z8) {
                        }
                        str5 = str3;
                        z9 = z8;
                        str6 = str2;
                        sharedPreferences = notificationsSettings;
                        j5 = j4;
                        z10 = z9;
                        if (z10) {
                        }
                        z12 = z11;
                        String path222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                        if (ApplicationLoader.mainInterfacePaused) {
                        }
                        getSharedPrefKey(j5, j6);
                        if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, j6, false)) {
                        }
                        NotificationCompat.Builder builder3222 = builder;
                        if (j3 == 0) {
                        }
                        if (i4 != 4) {
                        }
                        if (TextUtils.isEmpty(str8)) {
                        }
                        str8 = str10;
                        z14 = z16;
                        i8 = 3;
                        z19 = true;
                        if (i2 != i8) {
                        }
                        if (num != null) {
                        }
                        if (i != 0) {
                        }
                        i9 = i7;
                        z20 = z19;
                        if (z13) {
                        }
                        if (z18) {
                        }
                        if (z12) {
                        }
                        Intent intent5222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        intent5222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                        intent5222.setFlags(ConnectionsManager.FileTypeFile);
                        messageObject3 = messageObject2;
                        if (messageObject3.isStoryPush) {
                        }
                        tLRPC$Chat3 = tLRPC$Chat2;
                        tLRPC$User2 = tLRPC$User;
                        tLRPC$FileLocation = null;
                        intent5222.putExtra("currentAccount", this.currentAccount);
                        long j20222222 = j5;
                        int i39222222 = i11;
                        builder3222.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent5222, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                        builder3222.setCategory("msg");
                        if (tLRPC$Chat3 == null) {
                        }
                        Intent intent2222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                        intent2222222.putExtra("messageDate", messageObject3.messageOwner.date);
                        intent2222222.putExtra("currentAccount", this.currentAccount);
                        if (messageObject3.isStoryPush) {
                        }
                        builder3222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent2222222, 167772160));
                        if (bitmap2 != null) {
                        }
                        jArr = null;
                        boolean z292222222 = z15;
                        if (z) {
                        }
                        builder3222.setPriority(-1);
                    }
                    tLRPC$User = user;
                    if (this.pushDialogs.size() != 1) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(str);
                        j3 = j14;
                        sb3.append(LocaleController.formatPluralString("NewMessages", this.total_unread_count, new Object[0]));
                        str = sb3.toString();
                        j4 = dialogId;
                    } else {
                        j3 = j14;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(str);
                        j4 = dialogId;
                        sb4.append(LocaleController.formatString("NotificationMessagesPeopleDisplayOrder", R.string.NotificationMessagesPeopleDisplayOrder, LocaleController.formatPluralString("NewMessages", this.total_unread_count, new Object[0]), LocaleController.formatPluralString("FromChats", this.pushDialogs.size(), new Object[0])));
                        str = sb4.toString();
                    }
                    NotificationCompat.Builder builder22 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                    if (this.pushMessages.size() > 1) {
                    }
                    if (z) {
                        z8 = false;
                        if (z8) {
                        }
                        str5 = str3;
                        z9 = z8;
                        str6 = str2;
                        sharedPreferences = notificationsSettings;
                        j5 = j4;
                        z10 = z9;
                        if (z10) {
                        }
                        z12 = z11;
                        String path2222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                        if (ApplicationLoader.mainInterfacePaused) {
                        }
                        getSharedPrefKey(j5, j6);
                        if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, j6, false)) {
                        }
                        NotificationCompat.Builder builder32222 = builder;
                        if (j3 == 0) {
                        }
                        if (i4 != 4) {
                        }
                        if (TextUtils.isEmpty(str8)) {
                        }
                        str8 = str10;
                        z14 = z16;
                        i8 = 3;
                        z19 = true;
                        if (i2 != i8) {
                        }
                        if (num != null) {
                        }
                        if (i != 0) {
                        }
                        i9 = i7;
                        z20 = z19;
                        if (z13) {
                        }
                        if (z18) {
                        }
                        if (z12) {
                        }
                        Intent intent52222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        intent52222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                        intent52222.setFlags(ConnectionsManager.FileTypeFile);
                        messageObject3 = messageObject2;
                        if (messageObject3.isStoryPush) {
                        }
                        tLRPC$Chat3 = tLRPC$Chat2;
                        tLRPC$User2 = tLRPC$User;
                        tLRPC$FileLocation = null;
                        intent52222.putExtra("currentAccount", this.currentAccount);
                        long j202222222 = j5;
                        int i392222222 = i11;
                        builder32222.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent52222, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                        builder32222.setCategory("msg");
                        if (tLRPC$Chat3 == null) {
                        }
                        Intent intent22222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                        intent22222222.putExtra("messageDate", messageObject3.messageOwner.date);
                        intent22222222.putExtra("currentAccount", this.currentAccount);
                        if (messageObject3.isStoryPush) {
                        }
                        builder32222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent22222222, 167772160));
                        if (bitmap2 != null) {
                        }
                        jArr = null;
                        boolean z2922222222 = z15;
                        if (z) {
                        }
                        builder32222.setPriority(-1);
                    }
                    z8 = true;
                    if (z8) {
                    }
                    str5 = str3;
                    z9 = z8;
                    str6 = str2;
                    sharedPreferences = notificationsSettings;
                    j5 = j4;
                    z10 = z9;
                    if (z10) {
                    }
                    z12 = z11;
                    String path22222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                    if (ApplicationLoader.mainInterfacePaused) {
                    }
                    getSharedPrefKey(j5, j6);
                    if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, j6, false)) {
                    }
                    NotificationCompat.Builder builder322222 = builder;
                    if (j3 == 0) {
                    }
                    if (i4 != 4) {
                    }
                    if (TextUtils.isEmpty(str8)) {
                    }
                    str8 = str10;
                    z14 = z16;
                    i8 = 3;
                    z19 = true;
                    if (i2 != i8) {
                    }
                    if (num != null) {
                    }
                    if (i != 0) {
                    }
                    i9 = i7;
                    z20 = z19;
                    if (z13) {
                    }
                    if (z18) {
                    }
                    if (z12) {
                    }
                    Intent intent522222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    intent522222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                    intent522222.setFlags(ConnectionsManager.FileTypeFile);
                    messageObject3 = messageObject2;
                    if (messageObject3.isStoryPush) {
                    }
                    tLRPC$Chat3 = tLRPC$Chat2;
                    tLRPC$User2 = tLRPC$User;
                    tLRPC$FileLocation = null;
                    intent522222.putExtra("currentAccount", this.currentAccount);
                    long j2022222222 = j5;
                    int i3922222222 = i11;
                    builder322222.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent522222, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                    builder322222.setCategory("msg");
                    if (tLRPC$Chat3 == null) {
                    }
                    Intent intent222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                    intent222222222.putExtra("messageDate", messageObject3.messageOwner.date);
                    intent222222222.putExtra("currentAccount", this.currentAccount);
                    if (messageObject3.isStoryPush) {
                    }
                    builder322222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent222222222, 167772160));
                    if (bitmap2 != null) {
                    }
                    jArr = null;
                    boolean z29222222222 = z15;
                    if (z) {
                    }
                    builder322222.setPriority(-1);
                }
                string = LocaleController.getString("AppName", R.string.AppName);
                z5 = false;
                if (UserConfig.getActivatedAccountsCount() > 1) {
                }
                if (this.pushDialogs.size() == 1) {
                    tLRPC$User = user;
                    j4 = dialogId;
                    j3 = j14;
                    NotificationCompat.Builder builder222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                    if (this.pushMessages.size() > 1) {
                    }
                    if (z) {
                    }
                    z8 = true;
                    if (z8) {
                    }
                    str5 = str3;
                    z9 = z8;
                    str6 = str2;
                    sharedPreferences = notificationsSettings;
                    j5 = j4;
                    z10 = z9;
                    if (z10) {
                    }
                    z12 = z11;
                    String path222222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                    if (ApplicationLoader.mainInterfacePaused) {
                    }
                    getSharedPrefKey(j5, j6);
                    if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, j6, false)) {
                    }
                    NotificationCompat.Builder builder3222222 = builder;
                    if (j3 == 0) {
                    }
                    if (i4 != 4) {
                    }
                    if (TextUtils.isEmpty(str8)) {
                    }
                    str8 = str10;
                    z14 = z16;
                    i8 = 3;
                    z19 = true;
                    if (i2 != i8) {
                    }
                    if (num != null) {
                    }
                    if (i != 0) {
                    }
                    i9 = i7;
                    z20 = z19;
                    if (z13) {
                    }
                    if (z18) {
                    }
                    if (z12) {
                    }
                    Intent intent5222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    intent5222222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                    intent5222222.setFlags(ConnectionsManager.FileTypeFile);
                    messageObject3 = messageObject2;
                    if (messageObject3.isStoryPush) {
                    }
                    tLRPC$Chat3 = tLRPC$Chat2;
                    tLRPC$User2 = tLRPC$User;
                    tLRPC$FileLocation = null;
                    intent5222222.putExtra("currentAccount", this.currentAccount);
                    long j20222222222 = j5;
                    int i39222222222 = i11;
                    builder3222222.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent5222222, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                    builder3222222.setCategory("msg");
                    if (tLRPC$Chat3 == null) {
                    }
                    Intent intent2222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                    intent2222222222.putExtra("messageDate", messageObject3.messageOwner.date);
                    intent2222222222.putExtra("currentAccount", this.currentAccount);
                    if (messageObject3.isStoryPush) {
                    }
                    builder3222222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent2222222222, 167772160));
                    if (bitmap2 != null) {
                    }
                    jArr = null;
                    boolean z292222222222 = z15;
                    if (z) {
                    }
                    builder3222222.setPriority(-1);
                }
                tLRPC$User = user;
                if (this.pushDialogs.size() != 1) {
                }
                NotificationCompat.Builder builder2222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                if (this.pushMessages.size() > 1) {
                }
                if (z) {
                }
                z8 = true;
                if (z8) {
                }
                str5 = str3;
                z9 = z8;
                str6 = str2;
                sharedPreferences = notificationsSettings;
                j5 = j4;
                z10 = z9;
                if (z10) {
                }
                z12 = z11;
                String path2222222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                if (ApplicationLoader.mainInterfacePaused) {
                }
                getSharedPrefKey(j5, j6);
                if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, j6, false)) {
                }
                NotificationCompat.Builder builder32222222 = builder;
                if (j3 == 0) {
                }
                if (i4 != 4) {
                }
                if (TextUtils.isEmpty(str8)) {
                }
                str8 = str10;
                z14 = z16;
                i8 = 3;
                z19 = true;
                if (i2 != i8) {
                }
                if (num != null) {
                }
                if (i != 0) {
                }
                i9 = i7;
                z20 = z19;
                if (z13) {
                }
                if (z18) {
                }
                if (z12) {
                }
                Intent intent52222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                intent52222222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                intent52222222.setFlags(ConnectionsManager.FileTypeFile);
                messageObject3 = messageObject2;
                if (messageObject3.isStoryPush) {
                }
                tLRPC$Chat3 = tLRPC$Chat2;
                tLRPC$User2 = tLRPC$User;
                tLRPC$FileLocation = null;
                intent52222222.putExtra("currentAccount", this.currentAccount);
                long j202222222222 = j5;
                int i392222222222 = i11;
                builder32222222.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent52222222, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                builder32222222.setCategory("msg");
                if (tLRPC$Chat3 == null) {
                }
                Intent intent22222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                intent22222222222.putExtra("messageDate", messageObject3.messageOwner.date);
                intent22222222222.putExtra("currentAccount", this.currentAccount);
                if (messageObject3.isStoryPush) {
                }
                builder32222222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent22222222222, 167772160));
                if (bitmap2 != null) {
                }
                jArr = null;
                boolean z2922222222222 = z15;
                if (z) {
                }
                builder32222222.setPriority(-1);
            }
            z4 = true;
            if (!DialogObject.isEncryptedDialog(dialogId)) {
                string = str15;
                z5 = true;
                if (UserConfig.getActivatedAccountsCount() > 1) {
                }
                if (this.pushDialogs.size() == 1) {
                }
                tLRPC$User = user;
                if (this.pushDialogs.size() != 1) {
                }
                NotificationCompat.Builder builder22222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                if (this.pushMessages.size() > 1) {
                }
                if (z) {
                }
                z8 = true;
                if (z8) {
                }
                str5 = str3;
                z9 = z8;
                str6 = str2;
                sharedPreferences = notificationsSettings;
                j5 = j4;
                z10 = z9;
                if (z10) {
                }
                z12 = z11;
                String path22222222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                if (ApplicationLoader.mainInterfacePaused) {
                }
                getSharedPrefKey(j5, j6);
                if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, j6, false)) {
                }
                NotificationCompat.Builder builder322222222 = builder;
                if (j3 == 0) {
                }
                if (i4 != 4) {
                }
                if (TextUtils.isEmpty(str8)) {
                }
                str8 = str10;
                z14 = z16;
                i8 = 3;
                z19 = true;
                if (i2 != i8) {
                }
                if (num != null) {
                }
                if (i != 0) {
                }
                i9 = i7;
                z20 = z19;
                if (z13) {
                }
                if (z18) {
                }
                if (z12) {
                }
                Intent intent522222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                intent522222222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                intent522222222.setFlags(ConnectionsManager.FileTypeFile);
                messageObject3 = messageObject2;
                if (messageObject3.isStoryPush) {
                }
                tLRPC$Chat3 = tLRPC$Chat2;
                tLRPC$User2 = tLRPC$User;
                tLRPC$FileLocation = null;
                intent522222222.putExtra("currentAccount", this.currentAccount);
                long j2022222222222 = j5;
                int i3922222222222 = i11;
                builder322222222.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent522222222, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
                builder322222222.setCategory("msg");
                if (tLRPC$Chat3 == null) {
                }
                Intent intent222222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                intent222222222222.putExtra("messageDate", messageObject3.messageOwner.date);
                intent222222222222.putExtra("currentAccount", this.currentAccount);
                if (messageObject3.isStoryPush) {
                }
                builder322222222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent222222222222, 167772160));
                if (bitmap2 != null) {
                }
                jArr = null;
                boolean z29222222222222 = z15;
                if (z) {
                }
                builder322222222.setPriority(-1);
            }
            string = LocaleController.getString("AppName", R.string.AppName);
            z5 = false;
            if (UserConfig.getActivatedAccountsCount() > 1) {
            }
            if (this.pushDialogs.size() == 1) {
            }
            tLRPC$User = user;
            if (this.pushDialogs.size() != 1) {
            }
            NotificationCompat.Builder builder222222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
            if (this.pushMessages.size() > 1) {
            }
            if (z) {
            }
            z8 = true;
            if (z8) {
            }
            str5 = str3;
            z9 = z8;
            str6 = str2;
            sharedPreferences = notificationsSettings;
            j5 = j4;
            z10 = z9;
            if (z10) {
            }
            z12 = z11;
            String path222222222 = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
            if (ApplicationLoader.mainInterfacePaused) {
            }
            getSharedPrefKey(j5, j6);
            if (this.dialogsNotificationsFacade.getProperty(NotificationsSettingsFacade.PROPERTY_CUSTOM, j5, j6, false)) {
            }
            NotificationCompat.Builder builder3222222222 = builder;
            if (j3 == 0) {
            }
            if (i4 != 4) {
            }
            if (TextUtils.isEmpty(str8)) {
            }
            str8 = str10;
            z14 = z16;
            i8 = 3;
            z19 = true;
            if (i2 != i8) {
            }
            if (num != null) {
            }
            if (i != 0) {
            }
            i9 = i7;
            z20 = z19;
            if (z13) {
            }
            if (z18) {
            }
            if (z12) {
            }
            Intent intent5222222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
            intent5222222222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
            intent5222222222.setFlags(ConnectionsManager.FileTypeFile);
            messageObject3 = messageObject2;
            if (messageObject3.isStoryPush) {
            }
            tLRPC$Chat3 = tLRPC$Chat2;
            tLRPC$User2 = tLRPC$User;
            tLRPC$FileLocation = null;
            intent5222222222.putExtra("currentAccount", this.currentAccount);
            long j20222222222222 = j5;
            int i39222222222222 = i11;
            builder3222222222.setContentTitle(charSequence2).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(this.total_unread_count).setContentIntent(PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent5222222222, 1140850688)).setGroup(this.notificationGroup).setGroupSummary(true).setShowWhen(true).setWhen(messageObject3.messageOwner.date * 1000).setColor(-15618822);
            builder3222222222.setCategory("msg");
            if (tLRPC$Chat3 == null) {
            }
            Intent intent2222222222222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
            intent2222222222222.putExtra("messageDate", messageObject3.messageOwner.date);
            intent2222222222222.putExtra("currentAccount", this.currentAccount);
            if (messageObject3.isStoryPush) {
            }
            builder3222222222.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, i12, intent2222222222222, 167772160));
            if (bitmap2 != null) {
            }
            jArr = null;
            boolean z292222222222222 = z15;
            if (z) {
            }
            builder3222222222.setPriority(-1);
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
            }
            if (i3 == 2) {
                edit.putString("ChannelSoundPath", uri3);
            } else if (i3 == 0) {
                edit.putString("GroupSoundPath", uri3);
            } else if (i3 == 1) {
                edit.putString("GlobalSoundPath", uri3);
            } else if (i3 == 3) {
                edit.putString("StoriesSoundPath", uri3);
            }
            getNotificationsController().lambda$deleteNotificationChannelGlobal$38(i3, -1);
        } else {
            edit.putString("sound_" + getSharedPrefKey(j, j2), string);
            edit.putString("sound_path_" + getSharedPrefKey(j, j2), uri3);
            lambda$deleteNotificationChannel$37(j, j2, -1);
        }
        edit.commit();
        builder.setChannelId(validateChannelId(j, j2, str, jArr, i, Settings.System.DEFAULT_RINGTONE_URI, i2, z, z2, z3, i3));
        notificationManager.notify(this.notificationId, builder.build());
    }

    /* JADX WARN: Can't wrap try/catch for region: R(75:55|(2:57|(3:59|60|61)(4:62|(2:65|63)|66|67))(1:661)|68|(1:70)(1:(1:659)(1:660))|71|72|(4:75|(2:77|78)(1:80)|79|73)|81|82|(5:84|(2:(1:87)(1:571)|88)(1:572)|(1:570)(2:94|(2:98|99))|569|99)(3:573|(6:575|(1:577)(1:643)|578|(8:580|(2:582|(1:584)(2:595|(1:597)))(2:598|(7:602|(5:606|587|(1:589)(2:592|(1:594))|590|591)|586|587|(0)(0)|590|591))|585|586|587|(0)(0)|590|591)(3:607|(2:609|(1:611)(2:613|(1:615)))(10:616|(1:642)(1:620)|621|(1:641)(2:625|(6:629|630|(2:632|(3:634|(1:636)|637))(1:639)|638|(0)|637))|640|630|(0)(0)|638|(0)|637)|612)|336|337)(4:644|(4:646|(2:648|(1:650))(2:651|(2:653|(1:655)))|336|337)(1:657)|656|591)|61)|100|(3:102|(1:104)(1:567)|105)(1:568)|(3:107|(3:109|(1:111)(3:553|554|(3:556|(1:558)(1:560)|559)(1:561))|112)(1:565)|564)(1:566)|(3:114|(1:120)|121)|122|(3:548|(1:550)(1:552)|551)(1:125)|126|(1:128)|129|(1:131)(1:540)|132|(1:539)(1:136)|137|(3:140|(1:142)|(3:144|145|(48:149|150|151|(4:155|156|157|158)|(1:532)(1:166)|167|(1:531)(1:170)|171|(1:530)|178|(1:529)(1:185)|186|(13:188|(1:190)(5:320|(2:322|(1:(1:325)(1:326))(2:327|(10:329|192|193|(2:196|194)|197|198|(1:319)(1:201)|202|(1:204)(1:318)|205)(11:330|(1:335)(1:334)|193|(1:194)|197|198|(0)|319|202|(0)(0)|205)))|336|337|61)|191|192|193|(1:194)|197|198|(0)|319|202|(0)(0)|205)(4:338|(6:340|(1:342)(4:347|(1:349)(2:522|(2:526|(2:352|(1:354))(22:355|(1:357)|358|(2:518|(1:520)(1:521))(1:364)|365|(1:367)(12:(1:514)(2:515|(1:517))|369|(2:(2:372|(2:(2:375|(1:377)(2:504|505))(1:507)|378)(3:508|(1:510)|505))(1:511)|506)(1:512)|379|(3:474|(1:503)(3:480|(1:502)(4:483|(1:487)|(1:501)(2:493|(1:497))|500)|498)|499)(1:383)|384|(6:386|(1:472)(7:399|(1:471)(3:403|(9:453|454|455|456|457|458|459|460|461)(1:405)|406)|407|(1:409)(1:452)|410|(3:446|447|448)(3:412|(1:414)|445)|(6:416|(1:418)|419|(1:421)|422|(2:427|(3:429|(2:434|435)(1:431)|(1:433)))))|444|(0)|422|(3:425|427|(0)))(1:473)|438|(3:442|443|346)|344|345|346)|368|369|(0)(0)|379|(1:381)|474|(1:476)|503|499|384|(0)(0)|438|(4:440|442|443|346)|344|345|346)))|350|(0)(0))|343|344|345|346)|527|528)|206|(4:208|(2:211|209)|212|213)(2:311|(1:313)(2:314|(1:316)(1:317)))|214|(1:216)|217|(1:219)|220|(2:222|(1:224)(1:306))(2:307|(1:309)(1:310))|(1:226)(1:305)|227|(4:229|(2:232|230)|233|234)(1:304)|235|(1:237)|238|239|240|(1:242)|243|(1:245)|(1:247)|(1:251)|252|(1:300)(1:258)|259|(1:261)|(1:263)|264|(3:269|(4:271|(3:273|(4:275|(1:277)|278|279)(2:281|282)|280)|283|284)|285)|286|(1:299)(2:289|(1:293))|294|(1:296)|297|298|61)))|538|(1:164)|532|167|(0)|531|171|(1:173)|530|178|(1:181)|529|186|(0)(0)|206|(0)(0)|214|(0)|217|(0)|220|(0)(0)|(0)(0)|227|(0)(0)|235|(0)|238|239|240|(0)|243|(0)|(0)|(2:249|251)|252|(1:254)|300|259|(0)|(0)|264|(4:266|269|(0)|285)|286|(0)|299|294|(0)|297|298|61) */
    /* JADX WARN: Code restructure failed: missing block: B:584:0x0ffc, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:585:0x0ffd, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:105:0x02dc  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0353  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x035c  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x03f3  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x0420  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x0429  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x04c9  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x04e3  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x04ee  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x0546  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x0551  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x05db  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x05ec  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x062e  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0639  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x0640  */
    /* JADX WARN: Removed duplicated region for block: B:262:0x068b  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x06e9  */
    /* JADX WARN: Removed duplicated region for block: B:292:0x06fb A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:297:0x070f  */
    /* JADX WARN: Removed duplicated region for block: B:305:0x0724 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:315:0x0750  */
    /* JADX WARN: Removed duplicated region for block: B:340:0x0859 A[LOOP:5: B:338:0x0851->B:340:0x0859, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:343:0x087b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:348:0x089e  */
    /* JADX WARN: Removed duplicated region for block: B:349:0x08a4  */
    /* JADX WARN: Removed duplicated region for block: B:351:0x08b4  */
    /* JADX WARN: Removed duplicated region for block: B:367:0x0929  */
    /* JADX WARN: Removed duplicated region for block: B:370:0x0953  */
    /* JADX WARN: Removed duplicated region for block: B:395:0x09e9  */
    /* JADX WARN: Removed duplicated region for block: B:411:0x0a38  */
    /* JADX WARN: Removed duplicated region for block: B:451:0x0aee  */
    /* JADX WARN: Removed duplicated region for block: B:489:0x0bec  */
    /* JADX WARN: Removed duplicated region for block: B:490:0x0bef  */
    /* JADX WARN: Removed duplicated region for block: B:497:0x0c1b  */
    /* JADX WARN: Removed duplicated region for block: B:502:0x0c76  */
    /* JADX WARN: Removed duplicated region for block: B:509:0x0cac  */
    /* JADX WARN: Removed duplicated region for block: B:512:0x0cbb A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:517:0x0ccd  */
    /* JADX WARN: Removed duplicated region for block: B:525:0x0d13  */
    /* JADX WARN: Removed duplicated region for block: B:528:0x0d26  */
    /* JADX WARN: Removed duplicated region for block: B:536:0x0d9d  */
    /* JADX WARN: Removed duplicated region for block: B:541:0x0dc4  */
    /* JADX WARN: Removed duplicated region for block: B:550:0x0e0a  */
    /* JADX WARN: Removed duplicated region for block: B:553:0x0e29  */
    /* JADX WARN: Removed duplicated region for block: B:556:0x0e8a  */
    /* JADX WARN: Removed duplicated region for block: B:560:0x0ec7  */
    /* JADX WARN: Removed duplicated region for block: B:565:0x0eef  */
    /* JADX WARN: Removed duplicated region for block: B:566:0x0f14  */
    /* JADX WARN: Removed duplicated region for block: B:569:0x0f33  */
    /* JADX WARN: Removed duplicated region for block: B:574:0x0f56  */
    /* JADX WARN: Removed duplicated region for block: B:577:0x0f8a  */
    /* JADX WARN: Removed duplicated region for block: B:581:0x0fe5 A[Catch: Exception -> 0x0ffc, TryCatch #3 {Exception -> 0x0ffc, blocks: (B:579:0x0fc6, B:581:0x0fe5, B:582:0x0fec), top: B:686:0x0fc6 }] */
    /* JADX WARN: Removed duplicated region for block: B:587:0x1002  */
    /* JADX WARN: Removed duplicated region for block: B:589:0x100d  */
    /* JADX WARN: Removed duplicated region for block: B:591:0x1014  */
    /* JADX WARN: Removed duplicated region for block: B:596:0x1022  */
    /* JADX WARN: Removed duplicated region for block: B:604:0x103b  */
    /* JADX WARN: Removed duplicated region for block: B:606:0x1040  */
    /* JADX WARN: Removed duplicated region for block: B:609:0x104c  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0176  */
    /* JADX WARN: Removed duplicated region for block: B:614:0x1059  */
    /* JADX WARN: Removed duplicated region for block: B:627:0x10d8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:636:0x110a  */
    /* JADX WARN: Removed duplicated region for block: B:641:0x1196  */
    /* JADX WARN: Removed duplicated region for block: B:648:0x11e2  */
    /* JADX WARN: Removed duplicated region for block: B:654:0x11fb  */
    /* JADX WARN: Removed duplicated region for block: B:664:0x124a  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x01ef  */
    /* JADX WARN: Removed duplicated region for block: B:694:0x0bf8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:696:0x0695 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0225  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0230  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x024c  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x026e  */
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
        int i8;
        int i9;
        ArrayList arrayList2;
        long j3;
        ArrayList arrayList3;
        Notification notification;
        long j4;
        int id;
        MessageObject messageObject;
        ArrayList<StoryNotification> arrayList4;
        LongSparseArray longSparseArray4;
        boolean z6;
        long j5;
        int i10;
        LongSparseArray longSparseArray5;
        int i11;
        String str3;
        Integer num;
        MessageObject messageObject2;
        long j6;
        ArrayList<StoryNotification> arrayList5;
        TLRPC$User tLRPC$User;
        String string;
        TLRPC$User tLRPC$User2;
        boolean z7;
        TLRPC$FileLocation tLRPC$FileLocation;
        TLRPC$Chat tLRPC$Chat;
        boolean z8;
        boolean z9;
        TLRPC$FileLocation tLRPC$FileLocation2;
        TLRPC$FileLocation tLRPC$FileLocation3;
        TLRPC$TL_forumTopic findTopic;
        TLRPC$FileLocation tLRPC$FileLocation4;
        MessageObject messageObject3;
        String userName;
        TLRPC$FileLocation tLRPC$FileLocation5;
        TLRPC$User tLRPC$User3;
        LongSparseArray longSparseArray6;
        NotificationsController notificationsController2;
        ArrayList arrayList6;
        Notification notification2;
        boolean z10;
        long j7;
        TLRPC$User tLRPC$User4;
        boolean z11;
        TLRPC$FileLocation tLRPC$FileLocation6;
        String str4;
        String str5;
        String str6;
        DialogKey dialogKey2;
        Bitmap bitmap;
        File file;
        File file2;
        TLRPC$Chat tLRPC$Chat2;
        String str7;
        int i12;
        String formatString;
        NotificationCompat.Action build;
        Integer num2;
        DialogKey dialogKey3;
        int max;
        Person person;
        String str8;
        NotificationCompat.Action action;
        NotificationCompat.MessagingStyle messagingStyle;
        NotificationCompat.MessagingStyle messagingStyle2;
        int i13;
        long j8;
        DialogKey dialogKey4;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList7;
        StringBuilder sb;
        long j9;
        LongSparseArray longSparseArray7;
        long j10;
        ArrayList<StoryNotification> arrayList8;
        int i14;
        String str9;
        Bitmap bitmap2;
        int i15;
        StringBuilder sb2;
        String str10;
        long j11;
        ArrayList<StoryNotification> arrayList9;
        long senderId;
        long j12;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList10;
        LongSparseArray longSparseArray8;
        Person person2;
        String str11;
        String str12;
        String str13;
        String str14;
        String[] strArr;
        int i16;
        String str15;
        String str16;
        String str17;
        Person person3;
        File file3;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        TLRPC$FileLocation tLRPC$FileLocation7;
        LongSparseArray longSparseArray9;
        String str18;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup;
        String str19;
        boolean z12;
        List<NotificationCompat.MessagingStyle.Message> messages;
        Uri uri2;
        File file4;
        final File file5;
        final Uri uriForFile;
        File file6;
        String string2;
        DialogKey dialogKey5;
        long j13;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList11;
        Bitmap bitmap3;
        NotificationCompat.Action build2;
        NotificationCompat.Action action2;
        String str20;
        long j14;
        long j15;
        ArrayList<StoryNotification> arrayList12;
        long j16;
        NotificationCompat.Builder category;
        TLRPC$User tLRPC$User5;
        int size3;
        int i17;
        int i18;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList13;
        TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow;
        LongSparseArray longSparseArray10;
        int i19;
        TLRPC$User user;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto2;
        TLRPC$FileLocation tLRPC$FileLocation8;
        Bitmap bitmap4;
        Bitmap bitmap5;
        String string3;
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
        NotificationsController notificationsController3 = this;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        ArrayList arrayList14 = new ArrayList();
        if (!notificationsController3.storyPushMessages.isEmpty()) {
            arrayList14.add(new DialogKey(0L, 0L, true));
        }
        LongSparseArray longSparseArray11 = new LongSparseArray();
        int i21 = 0;
        int i22 = 0;
        while (i22 < notificationsController3.pushMessages.size()) {
            MessageObject messageObject4 = notificationsController3.pushMessages.get(i22);
            long dialogId = messageObject4.getDialogId();
            long topicId = MessageObject.getTopicId(notificationsController3.currentAccount, messageObject4.messageOwner, getMessagesController().isForum(messageObject4));
            int i23 = notificationsSettings.getInt("dismissDate" + dialogId, i21);
            if (messageObject4.isStoryPush || messageObject4.messageOwner.date > i23) {
                ArrayList arrayList15 = (ArrayList) longSparseArray11.get(dialogId);
                if (arrayList15 == null) {
                    ArrayList arrayList16 = new ArrayList();
                    longSparseArray11.put(dialogId, arrayList16);
                    arrayList14.add(new DialogKey(dialogId, topicId, false));
                    arrayList15 = arrayList16;
                }
                arrayList15.add(messageObject4);
            }
            i22++;
            i21 = 0;
        }
        LongSparseArray longSparseArray12 = new LongSparseArray();
        for (int i24 = 0; i24 < notificationsController3.wearNotificationsIds.size(); i24++) {
            longSparseArray12.put(notificationsController3.wearNotificationsIds.keyAt(i24), notificationsController3.wearNotificationsIds.valueAt(i24));
        }
        notificationsController3.wearNotificationsIds.clear();
        ArrayList arrayList17 = new ArrayList();
        int i25 = Build.VERSION.SDK_INT;
        if (i25 > 27) {
            if (arrayList14.size() <= (notificationsController3.storyPushMessages.isEmpty() ? 1 : 2)) {
                z4 = false;
                if (z4 && i25 >= 26) {
                    checkOtherNotificationsChannel();
                }
                clientUserId = getUserConfig().getClientUserId();
                z5 = !AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter;
                SharedConfig.passcodeHash.length();
                i4 = 7;
                longSparseArray = new LongSparseArray();
                size = arrayList14.size();
                i5 = 0;
                while (i5 < size && arrayList17.size() < i4) {
                    dialogKey = (DialogKey) arrayList14.get(i5);
                    if (!dialogKey.story) {
                        ArrayList<StoryNotification> arrayList18 = new ArrayList<>();
                        if (notificationsController3.storyPushMessages.isEmpty()) {
                            longSparseArray6 = longSparseArray;
                            j7 = clientUserId;
                            z10 = z4;
                            i8 = size;
                            i9 = i5;
                            longSparseArray5 = longSparseArray12;
                            longSparseArray4 = longSparseArray11;
                            arrayList3 = arrayList14;
                            notification2 = build3;
                            notificationsController2 = notificationsController3;
                            arrayList6 = arrayList17;
                            i5 = i9 + 1;
                            arrayList17 = arrayList6;
                            size = i8;
                            arrayList14 = arrayList3;
                            z4 = z10;
                            longSparseArray11 = longSparseArray4;
                            clientUserId = j7;
                            longSparseArray12 = longSparseArray5;
                            longSparseArray = longSparseArray6;
                            build3 = notification2;
                            i4 = 7;
                            notificationsController3 = notificationsController2;
                        } else {
                            i8 = size;
                            i9 = i5;
                            arrayList2 = arrayList17;
                            j3 = notificationsController3.storyPushMessages.get(0).dialogId;
                            id = 0;
                            for (Integer num3 : notificationsController3.storyPushMessages.get(0).dateByIds.keySet()) {
                                id = Math.max(id, num3.intValue());
                                arrayList18 = arrayList18;
                            }
                            notification = build3;
                            arrayList4 = arrayList18;
                            messageObject = null;
                            arrayList3 = arrayList14;
                            j4 = 0;
                        }
                    } else {
                        i8 = size;
                        i9 = i5;
                        arrayList2 = arrayList17;
                        j3 = dialogKey.dialogId;
                        arrayList3 = arrayList14;
                        notification = build3;
                        j4 = dialogKey.topicId;
                        ArrayList<StoryNotification> arrayList19 = (ArrayList) longSparseArray11.get(j3);
                        id = ((MessageObject) arrayList19.get(0)).getId();
                        messageObject = (MessageObject) arrayList19.get(0);
                        arrayList4 = arrayList19;
                    }
                    int i26 = (Integer) longSparseArray12.get(j3);
                    longSparseArray4 = longSparseArray11;
                    z6 = z4;
                    if (!dialogKey.story) {
                        i26 = 2147483646;
                        j5 = j4;
                    } else if (i26 == null) {
                        j5 = j4;
                        i26 = Integer.valueOf(((int) j3) + ((int) (j3 >> 32)));
                    } else {
                        j5 = j4;
                        longSparseArray12.remove(j3);
                    }
                    Integer num4 = i26;
                    int i27 = 0;
                    for (i10 = 0; i10 < arrayList4.size(); i10++) {
                        if (i27 < ((MessageObject) arrayList4.get(i10)).messageOwner.date) {
                            i27 = ((MessageObject) arrayList4.get(i10)).messageOwner.date;
                        }
                    }
                    if (!dialogKey.story) {
                        TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(j3));
                        longSparseArray5 = longSparseArray12;
                        if (notificationsController3.storyPushMessages.size() == 1) {
                            if (user2 != null) {
                                string = UserObject.getFirstName(user2);
                            } else {
                                string = notificationsController3.storyPushMessages.get(0).localName;
                            }
                            i11 = i27;
                        } else {
                            i11 = i27;
                            string = LocaleController.formatPluralString("Stories", notificationsController3.storyPushMessages.size(), new Object[0]);
                        }
                        if (user2 == null || (tLRPC$UserProfilePhoto3 = user2.photo) == null || (tLRPC$FileLocation = tLRPC$UserProfilePhoto3.photo_small) == null) {
                            str3 = "Stories";
                        } else {
                            str3 = "Stories";
                            if (tLRPC$FileLocation.volume_id != 0 && tLRPC$FileLocation.local_id != 0) {
                                tLRPC$User2 = user2;
                                num = num4;
                                messageObject2 = messageObject;
                                j6 = j5;
                                z7 = false;
                                tLRPC$Chat = null;
                                z8 = false;
                                z9 = false;
                                arrayList5 = arrayList4;
                            }
                        }
                        tLRPC$User2 = user2;
                        num = num4;
                        messageObject2 = messageObject;
                        j6 = j5;
                        z7 = false;
                        tLRPC$FileLocation = null;
                        tLRPC$Chat = null;
                        z8 = false;
                        z9 = false;
                        arrayList5 = arrayList4;
                    } else {
                        longSparseArray5 = longSparseArray12;
                        i11 = i27;
                        str3 = "Stories";
                        if (!DialogObject.isEncryptedDialog(j3)) {
                            z7 = j3 != 777000;
                            if (DialogObject.isUserDialog(j3)) {
                                TLRPC$User user3 = getMessagesController().getUser(Long.valueOf(j3));
                                if (user3 == null) {
                                    if (messageObject.isFcmMessage()) {
                                        messageObject3 = messageObject;
                                        userName = messageObject3.localName;
                                    } else if (BuildVars.LOGS_ENABLED) {
                                        FileLog.w("not found user to show dialog notification " + j3);
                                    }
                                } else {
                                    messageObject3 = messageObject;
                                    userName = UserObject.getUserName(user3);
                                    TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto4 = user3.photo;
                                    if (tLRPC$UserProfilePhoto4 != null && (tLRPC$FileLocation5 = tLRPC$UserProfilePhoto4.photo_small) != null) {
                                        arrayList5 = arrayList4;
                                        tLRPC$User3 = user3;
                                        if (tLRPC$FileLocation5.volume_id != 0 && tLRPC$FileLocation5.local_id != 0) {
                                            string = userName;
                                            if (UserObject.isReplyUser(j3)) {
                                                string = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                                            } else if (j3 == clientUserId) {
                                                string = LocaleController.getString("MessageScheduledReminderNotification", R.string.MessageScheduledReminderNotification);
                                            }
                                            messageObject2 = messageObject3;
                                            tLRPC$FileLocation = tLRPC$FileLocation5;
                                            num = num4;
                                            j6 = j5;
                                            tLRPC$User2 = tLRPC$User3;
                                            tLRPC$Chat = null;
                                            z8 = false;
                                            z9 = false;
                                        }
                                        string = userName;
                                        tLRPC$FileLocation5 = null;
                                        if (UserObject.isReplyUser(j3)) {
                                        }
                                        messageObject2 = messageObject3;
                                        tLRPC$FileLocation = tLRPC$FileLocation5;
                                        num = num4;
                                        j6 = j5;
                                        tLRPC$User2 = tLRPC$User3;
                                        tLRPC$Chat = null;
                                        z8 = false;
                                        z9 = false;
                                    }
                                }
                                arrayList5 = arrayList4;
                                tLRPC$User3 = user3;
                                string = userName;
                                tLRPC$FileLocation5 = null;
                                if (UserObject.isReplyUser(j3)) {
                                }
                                messageObject2 = messageObject3;
                                tLRPC$FileLocation = tLRPC$FileLocation5;
                                num = num4;
                                j6 = j5;
                                tLRPC$User2 = tLRPC$User3;
                                tLRPC$Chat = null;
                                z8 = false;
                                z9 = false;
                            } else {
                                MessageObject messageObject5 = messageObject;
                                arrayList5 = arrayList4;
                                TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j3));
                                if (chat == null) {
                                    if (messageObject5.isFcmMessage()) {
                                        boolean isSupergroup = messageObject5.isSupergroup();
                                        string = messageObject5.localName;
                                        z8 = isSupergroup;
                                        tLRPC$Chat = chat;
                                        messageObject2 = messageObject5;
                                        z9 = messageObject5.localChannel;
                                        num = num4;
                                        j6 = j5;
                                        z7 = false;
                                        tLRPC$FileLocation = null;
                                    } else if (BuildVars.LOGS_ENABLED) {
                                        FileLog.w("not found chat to show dialog notification " + j3);
                                    }
                                } else {
                                    boolean z13 = chat.megagroup;
                                    boolean z14 = ChatObject.isChannel(chat) && !chat.megagroup;
                                    String str21 = chat.title;
                                    z8 = z13;
                                    TLRPC$ChatPhoto tLRPC$ChatPhoto = chat.photo;
                                    if (tLRPC$ChatPhoto == null || (tLRPC$FileLocation4 = tLRPC$ChatPhoto.photo_small) == null) {
                                        messageObject2 = messageObject5;
                                        z9 = z14;
                                    } else {
                                        messageObject2 = messageObject5;
                                        z9 = z14;
                                        if (tLRPC$FileLocation4.volume_id != 0 && tLRPC$FileLocation4.local_id != 0) {
                                            tLRPC$FileLocation2 = tLRPC$FileLocation4;
                                            if (j5 == 0) {
                                                tLRPC$FileLocation3 = tLRPC$FileLocation2;
                                                num = num4;
                                                j6 = j5;
                                                if (getMessagesController().getTopicsController().findTopic(chat.id, j6) != null) {
                                                    string = findTopic.title + " in " + str21;
                                                    if (z7) {
                                                        z7 = ChatObject.canSendPlain(chat);
                                                    }
                                                    tLRPC$Chat = chat;
                                                    tLRPC$FileLocation = tLRPC$FileLocation3;
                                                }
                                            } else {
                                                tLRPC$FileLocation3 = tLRPC$FileLocation2;
                                                num = num4;
                                                j6 = j5;
                                            }
                                            string = str21;
                                            if (z7) {
                                            }
                                            tLRPC$Chat = chat;
                                            tLRPC$FileLocation = tLRPC$FileLocation3;
                                        }
                                    }
                                    tLRPC$FileLocation2 = null;
                                    if (j5 == 0) {
                                    }
                                    string = str21;
                                    if (z7) {
                                    }
                                    tLRPC$Chat = chat;
                                    tLRPC$FileLocation = tLRPC$FileLocation3;
                                }
                                tLRPC$User2 = null;
                            }
                            longSparseArray6 = longSparseArray;
                            notificationsController2 = notificationsController3;
                            arrayList6 = arrayList2;
                            notification2 = notification;
                            z10 = z6;
                            j7 = clientUserId;
                        } else {
                            num = num4;
                            messageObject2 = messageObject;
                            j6 = j5;
                            arrayList5 = arrayList4;
                            if (j3 != globalSecretChatId) {
                                int encryptedChatId = DialogObject.getEncryptedChatId(j3);
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
                                longSparseArray6 = longSparseArray;
                                notificationsController2 = notificationsController3;
                                arrayList6 = arrayList2;
                                notification2 = notification;
                                z10 = z6;
                                j7 = clientUserId;
                            } else {
                                tLRPC$User = null;
                            }
                            string = LocaleController.getString("SecretChatName", R.string.SecretChatName);
                            tLRPC$User2 = tLRPC$User;
                            z7 = false;
                            tLRPC$FileLocation = null;
                            tLRPC$Chat = null;
                            z8 = false;
                            z9 = false;
                        }
                        i5 = i9 + 1;
                        arrayList17 = arrayList6;
                        size = i8;
                        arrayList14 = arrayList3;
                        z4 = z10;
                        longSparseArray11 = longSparseArray4;
                        clientUserId = j7;
                        longSparseArray12 = longSparseArray5;
                        longSparseArray = longSparseArray6;
                        build3 = notification2;
                        i4 = 7;
                        notificationsController3 = notificationsController2;
                    }
                    String str22 = string;
                    if (z5) {
                        tLRPC$User4 = tLRPC$User2;
                        z11 = z7;
                        tLRPC$FileLocation6 = tLRPC$FileLocation;
                        str4 = str22;
                    } else {
                        if (DialogObject.isChatDialog(j3)) {
                            string3 = LocaleController.getString("NotificationHiddenChatName", R.string.NotificationHiddenChatName);
                        } else {
                            string3 = LocaleController.getString("NotificationHiddenName", R.string.NotificationHiddenName);
                        }
                        str4 = string3;
                        tLRPC$User4 = tLRPC$User2;
                        tLRPC$FileLocation6 = null;
                        z11 = false;
                    }
                    if (tLRPC$FileLocation6 == null) {
                        str6 = "NotificationHiddenName";
                        file = getFileLoader().getPathToAttach(tLRPC$FileLocation6, true);
                        str5 = "NotificationHiddenChatName";
                        if (Build.VERSION.SDK_INT < 28) {
                            dialogKey2 = dialogKey;
                            bitmap4 = null;
                            BitmapDrawable imageFromMemory = ImageLoader.getInstance().getImageFromMemory(tLRPC$FileLocation6, null, "50_50");
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
                            dialogKey2 = dialogKey;
                            bitmap4 = null;
                        }
                        bitmap = bitmap4;
                    } else {
                        str5 = "NotificationHiddenChatName";
                        str6 = "NotificationHiddenName";
                        dialogKey2 = dialogKey;
                        bitmap = null;
                        file = null;
                    }
                    if (tLRPC$Chat != null) {
                        Person.Builder name = new Person.Builder().setName(str4);
                        if (file != null && file.exists() && Build.VERSION.SDK_INT >= 28) {
                            loadRoundAvatar(file, name);
                        }
                        longSparseArray.put(-tLRPC$Chat.id, name.build());
                    }
                    Bitmap bitmap6 = bitmap;
                    if (!(z9 || z8) || !z11 || SharedConfig.isWaitingForPasscodeEnter || clientUserId == j3 || UserObject.isReplyUser(j3)) {
                        str7 = "max_id";
                        i12 = id;
                        file2 = file;
                        tLRPC$Chat2 = tLRPC$Chat;
                        build = null;
                    } else {
                        file2 = file;
                        tLRPC$Chat2 = tLRPC$Chat;
                        Intent intent = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                        intent.putExtra("dialog_id", j3);
                        intent.putExtra("max_id", id);
                        intent.putExtra("topic_id", j6);
                        intent.putExtra("currentAccount", notificationsController3.currentAccount);
                        str7 = "max_id";
                        PendingIntent broadcast = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent, 167772160);
                        RemoteInput build4 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                        if (!DialogObject.isChatDialog(j3)) {
                            i12 = id;
                            formatString = LocaleController.formatString("ReplyToGroup", R.string.ReplyToGroup, str4);
                        } else {
                            i12 = id;
                            formatString = LocaleController.formatString("ReplyToUser", R.string.ReplyToUser, str4);
                        }
                        build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build4).setShowsUserInterface(false).build();
                    }
                    num2 = notificationsController3.pushDialogs.get(j3);
                    if (num2 == null) {
                        num2 = 0;
                    }
                    dialogKey3 = dialogKey2;
                    if (!dialogKey3.story) {
                        max = notificationsController3.storyPushMessages.size();
                    } else {
                        max = Math.max(num2.intValue(), arrayList5.size());
                    }
                    String format = (max > 1 || Build.VERSION.SDK_INT >= 28) ? str4 : String.format("%1$s (%2$d)", str4, Integer.valueOf(max));
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
                                action = build;
                            }
                            if (tLRPC$UserProfilePhoto2 != null && (tLRPC$FileLocation8 = tLRPC$UserProfilePhoto2.photo_small) != null) {
                                str8 = "currentAccount";
                                action = build;
                                try {
                                    if (tLRPC$FileLocation8.volume_id != 0 && tLRPC$FileLocation8.local_id != 0) {
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
                                            if (person == null) {
                                            }
                                            messagingStyle = new NotificationCompat.MessagingStyle("");
                                            messagingStyle2 = messagingStyle;
                                            i13 = Build.VERSION.SDK_INT;
                                            if (i13 >= 28) {
                                            }
                                            messagingStyle2.setConversationTitle(format);
                                            messagingStyle2.setGroupConversation(i13 >= 28 || (!z9 && DialogObject.isChatDialog(j3)) || UserObject.isReplyUser(j3));
                                            StringBuilder sb3 = new StringBuilder();
                                            String[] strArr2 = new String[1];
                                            String str23 = "";
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
                                            StringBuilder sb4 = new StringBuilder();
                                            sb4.append("show extra notifications chatId ");
                                            sb4.append(j10);
                                            sb4.append(" topicId ");
                                            j13 = j9;
                                            sb4.append(j13);
                                            FileLog.d(sb4.toString());
                                            if (j13 != 0) {
                                            }
                                            String str24 = str8;
                                            intent2.putExtra(str24, notificationsController3.currentAccount);
                                            PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1140850688);
                                            NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
                                            NotificationCompat.Action action3 = action;
                                            if (action != null) {
                                            }
                                            Intent intent3 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                            intent3.addFlags(32);
                                            intent3.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                            intent3.putExtra("dialog_id", j10);
                                            int i28 = i12;
                                            intent3.putExtra(str7, i28);
                                            intent3.putExtra(str24, notificationsController3.currentAccount);
                                            int i29 = i14;
                                            arrayList11 = arrayList7;
                                            bitmap3 = bitmap2;
                                            build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent3, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                            if (DialogObject.isEncryptedDialog(j10)) {
                                            }
                                            if (str20 == null) {
                                            }
                                            StringBuilder sb5 = new StringBuilder();
                                            sb5.append("tgaccount");
                                            long j17 = j8;
                                            sb5.append(j17);
                                            wearableExtender.setBridgeTag(sb5.toString());
                                            if (!dialogKey5.story) {
                                            }
                                            NotificationCompat.Builder autoCancel = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str9).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                            if (dialogKey5.story) {
                                            }
                                            category = autoCancel.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j16).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity).extend(wearableExtender).setSortKey(String.valueOf(Long.MAX_VALUE - j16)).setCategory("msg");
                                            Intent intent4 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                            intent4.putExtra("messageDate", i11);
                                            intent4.putExtra("dialogId", j10);
                                            intent4.putExtra(str24, notificationsController3.currentAccount);
                                            if (dialogKey5.story) {
                                            }
                                            category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent4, 167772160));
                                            if (z6) {
                                            }
                                            if (action2 != null) {
                                            }
                                            if (!z5) {
                                            }
                                            if (arrayList3.size() != 1) {
                                            }
                                            if (DialogObject.isEncryptedDialog(j10)) {
                                            }
                                            if (bitmap3 != null) {
                                            }
                                            if (!AndroidUtilities.needShowPasscode(false)) {
                                            }
                                            if (tLRPC$Chat2 == null) {
                                            }
                                            tLRPC$User5 = tLRPC$User4;
                                            Notification notification3 = notification;
                                            if (Build.VERSION.SDK_INT >= 26) {
                                            }
                                            j7 = j15;
                                            longSparseArray6 = longSparseArray7;
                                            z10 = z6;
                                            notification2 = notification3;
                                            arrayList6 = arrayList2;
                                            arrayList6.add(new 1NotificationHolder(num.intValue(), j10, dialogKey5.story, j14, str9, tLRPC$User5, tLRPC$Chat2, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                            notificationsController2 = this;
                                            notificationsController2.wearNotificationsIds.put(j10, num);
                                            i5 = i9 + 1;
                                            arrayList17 = arrayList6;
                                            size = i8;
                                            arrayList14 = arrayList3;
                                            z4 = z10;
                                            longSparseArray11 = longSparseArray4;
                                            clientUserId = j7;
                                            longSparseArray12 = longSparseArray5;
                                            longSparseArray = longSparseArray6;
                                            build3 = notification2;
                                            i4 = 7;
                                            notificationsController3 = notificationsController2;
                                        }
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                }
                                boolean z15 = (messageObject2 == null && (messageObject2.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest)) ? false : true;
                                if (person == null && z15) {
                                    messagingStyle = new NotificationCompat.MessagingStyle(person);
                                } else {
                                    messagingStyle = new NotificationCompat.MessagingStyle("");
                                }
                                messagingStyle2 = messagingStyle;
                                i13 = Build.VERSION.SDK_INT;
                                if (i13 >= 28 || ((DialogObject.isChatDialog(j3) && !z9) || UserObject.isReplyUser(j3))) {
                                    messagingStyle2.setConversationTitle(format);
                                }
                                messagingStyle2.setGroupConversation(i13 >= 28 || (!z9 && DialogObject.isChatDialog(j3)) || UserObject.isReplyUser(j3));
                                StringBuilder sb32 = new StringBuilder();
                                String[] strArr22 = new String[1];
                                String str232 = "";
                                boolean[] zArr2 = new boolean[1];
                                if (!dialogKey3.story) {
                                    ArrayList<String> arrayList20 = new ArrayList<>();
                                    ArrayList<Object> arrayList21 = new ArrayList<>();
                                    Pair<Integer, Boolean> parseStoryPushes = notificationsController3.parseStoryPushes(arrayList20, arrayList21);
                                    int intValue = ((Integer) parseStoryPushes.first).intValue();
                                    boolean booleanValue = ((Boolean) parseStoryPushes.second).booleanValue();
                                    if (booleanValue) {
                                        dialogKey4 = dialogKey3;
                                        sb32.append(LocaleController.formatPluralString("StoryNotificationHidden", intValue, new Object[0]));
                                    } else {
                                        dialogKey4 = dialogKey3;
                                        if (!arrayList20.isEmpty()) {
                                            if (arrayList20.size() == 1) {
                                                if (intValue == 1) {
                                                    sb32.append(LocaleController.getString("StoryNotificationSingle"));
                                                } else {
                                                    sb32.append(LocaleController.formatPluralString("StoryNotification1", intValue, arrayList20.get(0)));
                                                }
                                            } else if (arrayList20.size() != 2) {
                                                longSparseArray10 = longSparseArray;
                                                if (arrayList20.size() == 3 && notificationsController3.storyPushMessages.size() == 3) {
                                                    j8 = clientUserId;
                                                    sb32.append(LocaleController.formatString(R.string.StoryNotification3, notificationsController3.cutLastName(arrayList20.get(0)), notificationsController3.cutLastName(arrayList20.get(1)), notificationsController3.cutLastName(arrayList20.get(2))));
                                                } else {
                                                    j8 = clientUserId;
                                                    sb32.append(LocaleController.formatPluralString("StoryNotification4", notificationsController3.storyPushMessages.size() - 2, notificationsController3.cutLastName(arrayList20.get(0)), notificationsController3.cutLastName(arrayList20.get(1))));
                                                }
                                                long j18 = Long.MAX_VALUE;
                                                i19 = 0;
                                                while (i19 < notificationsController3.storyPushMessages.size()) {
                                                }
                                                long j19 = j3;
                                                messagingStyle2.setGroupConversation(false);
                                                if (arrayList20.size() == 1) {
                                                }
                                                messagingStyle2.addMessage(sb32, j18, new Person.Builder().setName(r0).build());
                                                if (!booleanValue) {
                                                }
                                                str9 = r0;
                                                sb = sb32;
                                                j9 = j6;
                                                longSparseArray7 = longSparseArray10;
                                                j10 = j19;
                                                arrayList7 = null;
                                                i14 = 0;
                                                arrayList8 = arrayList5;
                                            } else {
                                                longSparseArray10 = longSparseArray;
                                                sb32.append(LocaleController.formatString(R.string.StoryNotification2, arrayList20.get(0), arrayList20.get(1)));
                                                j8 = clientUserId;
                                                long j182 = Long.MAX_VALUE;
                                                i19 = 0;
                                                while (i19 < notificationsController3.storyPushMessages.size()) {
                                                    j182 = Math.min(notificationsController3.storyPushMessages.get(i19).date, j182);
                                                    i19++;
                                                    j3 = j3;
                                                }
                                                long j192 = j3;
                                                messagingStyle2.setGroupConversation(false);
                                                String formatPluralString = (arrayList20.size() == 1 || booleanValue) ? LocaleController.formatPluralString(str3, intValue, new Object[0]) : arrayList20.get(0);
                                                messagingStyle2.addMessage(sb32, j182, new Person.Builder().setName(formatPluralString).build());
                                                bitmap2 = !booleanValue ? loadMultipleAvatars(arrayList21) : null;
                                                str9 = formatPluralString;
                                                sb = sb32;
                                                j9 = j6;
                                                longSparseArray7 = longSparseArray10;
                                                j10 = j192;
                                                arrayList7 = null;
                                                i14 = 0;
                                                arrayList8 = arrayList5;
                                            }
                                        }
                                        longSparseArray6 = longSparseArray;
                                        notificationsController2 = notificationsController3;
                                        arrayList6 = arrayList2;
                                        notification2 = notification;
                                        z10 = z6;
                                        j7 = clientUserId;
                                        i5 = i9 + 1;
                                        arrayList17 = arrayList6;
                                        size = i8;
                                        arrayList14 = arrayList3;
                                        z4 = z10;
                                        longSparseArray11 = longSparseArray4;
                                        clientUserId = j7;
                                        longSparseArray12 = longSparseArray5;
                                        longSparseArray = longSparseArray6;
                                        build3 = notification2;
                                        i4 = 7;
                                        notificationsController3 = notificationsController2;
                                    }
                                    longSparseArray10 = longSparseArray;
                                    j8 = clientUserId;
                                    long j1822 = Long.MAX_VALUE;
                                    i19 = 0;
                                    while (i19 < notificationsController3.storyPushMessages.size()) {
                                    }
                                    long j1922 = j3;
                                    messagingStyle2.setGroupConversation(false);
                                    if (arrayList20.size() == 1) {
                                    }
                                    messagingStyle2.addMessage(sb32, j1822, new Person.Builder().setName(formatPluralString).build());
                                    if (!booleanValue) {
                                    }
                                    str9 = formatPluralString;
                                    sb = sb32;
                                    j9 = j6;
                                    longSparseArray7 = longSparseArray10;
                                    j10 = j1922;
                                    arrayList7 = null;
                                    i14 = 0;
                                    arrayList8 = arrayList5;
                                } else {
                                    LongSparseArray longSparseArray13 = longSparseArray;
                                    j8 = clientUserId;
                                    dialogKey4 = dialogKey3;
                                    long j20 = j3;
                                    int size4 = arrayList5.size() - 1;
                                    int i30 = 0;
                                    arrayList7 = null;
                                    while (size4 >= 0) {
                                        ArrayList<StoryNotification> arrayList22 = arrayList5;
                                        MessageObject messageObject6 = (MessageObject) arrayList22.get(size4);
                                        if (j6 != MessageObject.getTopicId(notificationsController3.currentAccount, messageObject6.messageOwner, getMessagesController().isForum(messageObject6))) {
                                            i15 = i30;
                                        } else {
                                            String shortStringForMessage = notificationsController3.getShortStringForMessage(messageObject6, strArr22, zArr2);
                                            if (j20 == j8) {
                                                strArr22[0] = str4;
                                            } else if (DialogObject.isChatDialog(j20) && messageObject6.messageOwner.from_scheduled) {
                                                i15 = i30;
                                                strArr22[0] = LocaleController.getString("NotificationMessageScheduledName", R.string.NotificationMessageScheduledName);
                                                if (shortStringForMessage != null) {
                                                    if (BuildVars.LOGS_ENABLED) {
                                                        FileLog.w("message text is null for " + messageObject6.getId() + " did = " + messageObject6.getDialogId());
                                                    }
                                                } else {
                                                    if (sb32.length() > 0) {
                                                        sb32.append("\n\n");
                                                    }
                                                    if (j20 != j8 && messageObject6.messageOwner.from_scheduled && DialogObject.isUserDialog(j20)) {
                                                        shortStringForMessage = String.format("%1$s: %2$s", LocaleController.getString("NotificationMessageScheduledName", R.string.NotificationMessageScheduledName), shortStringForMessage);
                                                        sb32.append(shortStringForMessage);
                                                    } else if (strArr22[0] != null) {
                                                        sb32.append(String.format("%1$s: %2$s", strArr22[0], shortStringForMessage));
                                                    } else {
                                                        sb32.append(shortStringForMessage);
                                                    }
                                                    String str25 = shortStringForMessage;
                                                    if (DialogObject.isUserDialog(j20)) {
                                                        sb2 = sb32;
                                                        str10 = str4;
                                                        j11 = j20;
                                                        arrayList9 = arrayList22;
                                                    } else {
                                                        if (z9) {
                                                            sb2 = sb32;
                                                            str10 = str4;
                                                            j11 = j20;
                                                            arrayList9 = arrayList22;
                                                            senderId = -j11;
                                                        } else {
                                                            sb2 = sb32;
                                                            str10 = str4;
                                                            j11 = j20;
                                                            arrayList9 = arrayList22;
                                                            if (DialogObject.isChatDialog(j11)) {
                                                                senderId = messageObject6.getSenderId();
                                                            }
                                                        }
                                                        j12 = j6;
                                                        arrayList10 = arrayList7;
                                                        longSparseArray8 = longSparseArray13;
                                                        person2 = (Person) longSparseArray8.get(senderId + (j6 << 16));
                                                        if (strArr22[0] != null) {
                                                            if (!z5) {
                                                                str11 = str6;
                                                                str12 = str5;
                                                                str13 = str10;
                                                            } else if (DialogObject.isChatDialog(j11)) {
                                                                if (z9) {
                                                                    if (Build.VERSION.SDK_INT > 27) {
                                                                        str12 = str5;
                                                                        string2 = LocaleController.getString(str12, R.string.NotificationHiddenChatName);
                                                                        str13 = str10;
                                                                    } else {
                                                                        str12 = str5;
                                                                        str13 = str10;
                                                                        str11 = str6;
                                                                    }
                                                                } else {
                                                                    str12 = str5;
                                                                    str13 = str10;
                                                                    string2 = LocaleController.getString("NotificationHiddenChatUserName", R.string.NotificationHiddenChatUserName);
                                                                }
                                                                str14 = string2;
                                                                str11 = str6;
                                                            } else {
                                                                str12 = str5;
                                                                str13 = str10;
                                                                if (Build.VERSION.SDK_INT > 27) {
                                                                    str11 = str6;
                                                                    str14 = LocaleController.getString(str11, R.string.NotificationHiddenName);
                                                                }
                                                                str11 = str6;
                                                            }
                                                            str14 = str232;
                                                        } else {
                                                            str11 = str6;
                                                            str12 = str5;
                                                            str13 = str10;
                                                            str14 = strArr22[0];
                                                        }
                                                        strArr = strArr22;
                                                        if (person2 == null && TextUtils.equals(person2.getName(), str14)) {
                                                            i16 = size4;
                                                            str15 = str11;
                                                            str16 = str12;
                                                            person3 = person2;
                                                            str17 = str25;
                                                        } else {
                                                            Person.Builder name3 = new Person.Builder().setName(str14);
                                                            if (zArr2[0] || DialogObject.isEncryptedDialog(j11) || Build.VERSION.SDK_INT < 28) {
                                                                i16 = size4;
                                                                str15 = str11;
                                                                str16 = str12;
                                                                str17 = str25;
                                                            } else {
                                                                if (DialogObject.isUserDialog(j11) || z9) {
                                                                    i16 = size4;
                                                                    str15 = str11;
                                                                    str16 = str12;
                                                                    str17 = str25;
                                                                    file3 = file2;
                                                                } else {
                                                                    long senderId2 = messageObject6.getSenderId();
                                                                    str15 = str11;
                                                                    str16 = str12;
                                                                    TLRPC$User user4 = getMessagesController().getUser(Long.valueOf(senderId2));
                                                                    if (user4 == null && (user4 = getMessagesStorage().getUserSync(senderId2)) != null) {
                                                                        getMessagesController().putUser(user4, true);
                                                                    }
                                                                    if (user4 == null || (tLRPC$UserProfilePhoto = user4.photo) == null || (tLRPC$FileLocation7 = tLRPC$UserProfilePhoto.photo_small) == null) {
                                                                        i16 = size4;
                                                                        str17 = str25;
                                                                    } else {
                                                                        i16 = size4;
                                                                        str17 = str25;
                                                                        if (tLRPC$FileLocation7.volume_id != 0 && tLRPC$FileLocation7.local_id != 0) {
                                                                            file3 = getFileLoader().getPathToAttach(user4.photo.photo_small, true);
                                                                        }
                                                                    }
                                                                    file3 = null;
                                                                }
                                                                loadRoundAvatar(file3, name3);
                                                            }
                                                            Person build6 = name3.build();
                                                            longSparseArray8.put(senderId, build6);
                                                            person3 = build6;
                                                        }
                                                        if (DialogObject.isEncryptedDialog(j11)) {
                                                            if (!zArr2[0] || Build.VERSION.SDK_INT < 28 || ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).isLowRamDevice() || z5 || messageObject6.isSecretMedia() || !(messageObject6.type == 1 || messageObject6.isSticker())) {
                                                                longSparseArray9 = longSparseArray8;
                                                                str19 = str17;
                                                                str18 = str232;
                                                            } else {
                                                                File pathToMessage = getFileLoader().getPathToMessage(messageObject6.messageOwner);
                                                                if (pathToMessage.exists() && messageObject6.hasMediaSpoilers()) {
                                                                    file5 = new File(pathToMessage.getParentFile(), pathToMessage.getName() + ".blur.jpg");
                                                                    if (file5.exists()) {
                                                                        longSparseArray9 = longSparseArray8;
                                                                        file6 = pathToMessage;
                                                                    } else {
                                                                        try {
                                                                            Bitmap decodeFile = BitmapFactory.decodeFile(pathToMessage.getAbsolutePath());
                                                                            Bitmap stackBlurBitmapMax = Utilities.stackBlurBitmapMax(decodeFile);
                                                                            decodeFile.recycle();
                                                                            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(stackBlurBitmapMax, decodeFile.getWidth(), decodeFile.getHeight(), true);
                                                                            Utilities.stackBlurBitmap(createScaledBitmap, 5);
                                                                            stackBlurBitmapMax.recycle();
                                                                            Canvas canvas = new Canvas(createScaledBitmap);
                                                                            longSparseArray9 = longSparseArray8;
                                                                            try {
                                                                                notificationsController3.mediaSpoilerEffect.setColor(ColorUtils.setAlphaComponent(-1, (int) (Color.alpha(-1) * 0.325f)));
                                                                                file6 = pathToMessage;
                                                                            } catch (Exception e) {
                                                                                e = e;
                                                                                file6 = pathToMessage;
                                                                                FileLog.e(e);
                                                                                file4 = file6;
                                                                                str19 = str17;
                                                                                NotificationCompat.MessagingStyle.Message message = new NotificationCompat.MessagingStyle.Message(str19, messageObject6.messageOwner.date * 1000, person3);
                                                                                String str26 = messageObject6.isSticker() ? "image/webp" : "image/jpeg";
                                                                                if (file4.exists()) {
                                                                                }
                                                                                if (uriForFile != null) {
                                                                                }
                                                                                z12 = false;
                                                                                if (!z12) {
                                                                                }
                                                                                if (zArr2[0]) {
                                                                                }
                                                                                if (j11 == 777000) {
                                                                                }
                                                                                i30 = i15;
                                                                                arrayList7 = arrayList10;
                                                                                size4 = i16 - 1;
                                                                                str232 = str18;
                                                                                sb32 = sb2;
                                                                                strArr22 = strArr;
                                                                                str6 = str15;
                                                                                arrayList5 = arrayList9;
                                                                                j6 = j12;
                                                                                longSparseArray13 = longSparseArray9;
                                                                                j20 = j11;
                                                                                str4 = str13;
                                                                                str5 = str16;
                                                                            }
                                                                            try {
                                                                                notificationsController3.mediaSpoilerEffect.setBounds(0, 0, createScaledBitmap.getWidth(), createScaledBitmap.getHeight());
                                                                                notificationsController3.mediaSpoilerEffect.draw(canvas);
                                                                                FileOutputStream fileOutputStream = new FileOutputStream(file5);
                                                                                createScaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                                                                                fileOutputStream.close();
                                                                                createScaledBitmap.recycle();
                                                                                file4 = file5;
                                                                            } catch (Exception e2) {
                                                                                e = e2;
                                                                                FileLog.e(e);
                                                                                file4 = file6;
                                                                                str19 = str17;
                                                                                NotificationCompat.MessagingStyle.Message message2 = new NotificationCompat.MessagingStyle.Message(str19, messageObject6.messageOwner.date * 1000, person3);
                                                                                String str262 = messageObject6.isSticker() ? "image/webp" : "image/jpeg";
                                                                                if (file4.exists()) {
                                                                                }
                                                                                if (uriForFile != null) {
                                                                                }
                                                                                z12 = false;
                                                                                if (!z12) {
                                                                                }
                                                                                if (zArr2[0]) {
                                                                                }
                                                                                if (j11 == 777000) {
                                                                                }
                                                                                i30 = i15;
                                                                                arrayList7 = arrayList10;
                                                                                size4 = i16 - 1;
                                                                                str232 = str18;
                                                                                sb32 = sb2;
                                                                                strArr22 = strArr;
                                                                                str6 = str15;
                                                                                arrayList5 = arrayList9;
                                                                                j6 = j12;
                                                                                longSparseArray13 = longSparseArray9;
                                                                                j20 = j11;
                                                                                str4 = str13;
                                                                                str5 = str16;
                                                                            }
                                                                        } catch (Exception e3) {
                                                                            e = e3;
                                                                            longSparseArray9 = longSparseArray8;
                                                                        }
                                                                    }
                                                                    file4 = file6;
                                                                } else {
                                                                    longSparseArray9 = longSparseArray8;
                                                                    file4 = pathToMessage;
                                                                    file5 = null;
                                                                }
                                                                str19 = str17;
                                                                NotificationCompat.MessagingStyle.Message message22 = new NotificationCompat.MessagingStyle.Message(str19, messageObject6.messageOwner.date * 1000, person3);
                                                                String str2622 = messageObject6.isSticker() ? "image/webp" : "image/jpeg";
                                                                if (file4.exists()) {
                                                                    try {
                                                                        uriForFile = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", file4);
                                                                        str18 = str232;
                                                                    } catch (Exception e4) {
                                                                        FileLog.e(e4);
                                                                    }
                                                                } else {
                                                                    if (getFileLoader().isLoadingFile(file4.getName())) {
                                                                        Uri.Builder appendPath = new Uri.Builder().scheme("content").authority(NotificationImageProvider.getAuthority()).appendPath("msg_media_raw");
                                                                        StringBuilder sb6 = new StringBuilder();
                                                                        sb6.append(notificationsController3.currentAccount);
                                                                        str18 = str232;
                                                                        sb6.append(str18);
                                                                        uriForFile = appendPath.appendPath(sb6.toString()).appendPath(file4.getName()).appendQueryParameter("final_path", file4.getAbsolutePath()).build();
                                                                    }
                                                                    str18 = str232;
                                                                    uriForFile = null;
                                                                }
                                                                if (uriForFile != null) {
                                                                    message22.setData(str2622, uriForFile);
                                                                    messagingStyle2.addMessage(message22);
                                                                    ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile, 1);
                                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda4
                                                                        @Override // java.lang.Runnable
                                                                        public final void run() {
                                                                            NotificationsController.lambda$showExtraNotifications$40(uriForFile, file5);
                                                                        }
                                                                    }, 20000L);
                                                                    if (!TextUtils.isEmpty(messageObject6.caption)) {
                                                                        messagingStyle2.addMessage(messageObject6.caption, messageObject6.messageOwner.date * 1000, person3);
                                                                    }
                                                                    z12 = true;
                                                                    if (!z12) {
                                                                        messagingStyle2.addMessage(str19, messageObject6.messageOwner.date * 1000, person3);
                                                                    }
                                                                    if (zArr2[0] && !z5 && messageObject6.isVoice()) {
                                                                        messages = messagingStyle2.getMessages();
                                                                        if (!messages.isEmpty()) {
                                                                            File pathToMessage2 = getFileLoader().getPathToMessage(messageObject6.messageOwner);
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
                                                            z12 = false;
                                                            if (!z12) {
                                                            }
                                                            if (zArr2[0]) {
                                                                messages = messagingStyle2.getMessages();
                                                                if (!messages.isEmpty()) {
                                                                }
                                                            }
                                                        } else {
                                                            longSparseArray9 = longSparseArray8;
                                                            String str27 = str17;
                                                            str18 = str232;
                                                            messagingStyle2.addMessage(str27, messageObject6.messageOwner.date * 1000, person3);
                                                        }
                                                        if (j11 == 777000 && (tLRPC$ReplyMarkup = messageObject6.messageOwner.reply_markup) != null) {
                                                            arrayList7 = tLRPC$ReplyMarkup.rows;
                                                            i30 = messageObject6.getId();
                                                            size4 = i16 - 1;
                                                            str232 = str18;
                                                            sb32 = sb2;
                                                            strArr22 = strArr;
                                                            str6 = str15;
                                                            arrayList5 = arrayList9;
                                                            j6 = j12;
                                                            longSparseArray13 = longSparseArray9;
                                                            j20 = j11;
                                                            str4 = str13;
                                                            str5 = str16;
                                                        }
                                                        i30 = i15;
                                                        arrayList7 = arrayList10;
                                                        size4 = i16 - 1;
                                                        str232 = str18;
                                                        sb32 = sb2;
                                                        strArr22 = strArr;
                                                        str6 = str15;
                                                        arrayList5 = arrayList9;
                                                        j6 = j12;
                                                        longSparseArray13 = longSparseArray9;
                                                        j20 = j11;
                                                        str4 = str13;
                                                        str5 = str16;
                                                    }
                                                    senderId = j11;
                                                    j12 = j6;
                                                    arrayList10 = arrayList7;
                                                    longSparseArray8 = longSparseArray13;
                                                    person2 = (Person) longSparseArray8.get(senderId + (j6 << 16));
                                                    if (strArr22[0] != null) {
                                                    }
                                                    strArr = strArr22;
                                                    if (person2 == null) {
                                                    }
                                                    Person.Builder name32 = new Person.Builder().setName(str14);
                                                    if (zArr2[0]) {
                                                    }
                                                    i16 = size4;
                                                    str15 = str11;
                                                    str16 = str12;
                                                    str17 = str25;
                                                    Person build62 = name32.build();
                                                    longSparseArray8.put(senderId, build62);
                                                    person3 = build62;
                                                    if (DialogObject.isEncryptedDialog(j11)) {
                                                    }
                                                    if (j11 == 777000) {
                                                        arrayList7 = tLRPC$ReplyMarkup.rows;
                                                        i30 = messageObject6.getId();
                                                        size4 = i16 - 1;
                                                        str232 = str18;
                                                        sb32 = sb2;
                                                        strArr22 = strArr;
                                                        str6 = str15;
                                                        arrayList5 = arrayList9;
                                                        j6 = j12;
                                                        longSparseArray13 = longSparseArray9;
                                                        j20 = j11;
                                                        str4 = str13;
                                                        str5 = str16;
                                                    }
                                                    i30 = i15;
                                                    arrayList7 = arrayList10;
                                                    size4 = i16 - 1;
                                                    str232 = str18;
                                                    sb32 = sb2;
                                                    strArr22 = strArr;
                                                    str6 = str15;
                                                    arrayList5 = arrayList9;
                                                    j6 = j12;
                                                    longSparseArray13 = longSparseArray9;
                                                    j20 = j11;
                                                    str4 = str13;
                                                    str5 = str16;
                                                }
                                            }
                                            i15 = i30;
                                            if (shortStringForMessage != null) {
                                            }
                                        }
                                        sb2 = sb32;
                                        j12 = j6;
                                        str18 = str232;
                                        str16 = str5;
                                        longSparseArray9 = longSparseArray13;
                                        str13 = str4;
                                        str15 = str6;
                                        j11 = j20;
                                        arrayList10 = arrayList7;
                                        arrayList9 = arrayList22;
                                        strArr = strArr22;
                                        i16 = size4;
                                        i30 = i15;
                                        arrayList7 = arrayList10;
                                        size4 = i16 - 1;
                                        str232 = str18;
                                        sb32 = sb2;
                                        strArr22 = strArr;
                                        str6 = str15;
                                        arrayList5 = arrayList9;
                                        j6 = j12;
                                        longSparseArray13 = longSparseArray9;
                                        j20 = j11;
                                        str4 = str13;
                                        str5 = str16;
                                    }
                                    sb = sb32;
                                    j9 = j6;
                                    longSparseArray7 = longSparseArray13;
                                    j10 = j20;
                                    arrayList8 = arrayList5;
                                    i14 = i30;
                                    str9 = str4;
                                    bitmap2 = bitmap6;
                                }
                                Intent intent22 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                intent22.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                intent22.setFlags(ConnectionsManager.FileTypeFile);
                                intent22.addCategory("android.intent.category.LAUNCHER");
                                dialogKey5 = dialogKey4;
                                if (!dialogKey5.story) {
                                    long[] jArr2 = new long[notificationsController3.storyPushMessages.size()];
                                    for (int i31 = 0; i31 < notificationsController3.storyPushMessages.size(); i31++) {
                                        jArr2[i31] = notificationsController3.storyPushMessages.get(i31).dialogId;
                                    }
                                    intent22.putExtra("storyDialogIds", jArr2);
                                } else if (DialogObject.isEncryptedDialog(j10)) {
                                    intent22.putExtra("encId", DialogObject.getEncryptedChatId(j10));
                                } else if (DialogObject.isUserDialog(j10)) {
                                    intent22.putExtra("userId", j10);
                                } else {
                                    intent22.putExtra("chatId", -j10);
                                }
                                StringBuilder sb42 = new StringBuilder();
                                sb42.append("show extra notifications chatId ");
                                sb42.append(j10);
                                sb42.append(" topicId ");
                                j13 = j9;
                                sb42.append(j13);
                                FileLog.d(sb42.toString());
                                if (j13 != 0) {
                                    intent22.putExtra("topicId", j13);
                                }
                                String str242 = str8;
                                intent22.putExtra(str242, notificationsController3.currentAccount);
                                PendingIntent activity2 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22, 1140850688);
                                NotificationCompat.WearableExtender wearableExtender2 = new NotificationCompat.WearableExtender();
                                NotificationCompat.Action action32 = action;
                                if (action != null) {
                                    wearableExtender2.addAction(action32);
                                }
                                Intent intent32 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                intent32.addFlags(32);
                                intent32.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                intent32.putExtra("dialog_id", j10);
                                int i282 = i12;
                                intent32.putExtra(str7, i282);
                                intent32.putExtra(str242, notificationsController3.currentAccount);
                                int i292 = i14;
                                arrayList11 = arrayList7;
                                bitmap3 = bitmap2;
                                build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent32, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                if (DialogObject.isEncryptedDialog(j10)) {
                                    if (DialogObject.isUserDialog(j10)) {
                                        str20 = "tguser" + j10 + "_" + i282;
                                        action2 = action32;
                                    } else {
                                        StringBuilder sb7 = new StringBuilder();
                                        sb7.append("tgchat");
                                        action2 = action32;
                                        sb7.append(-j10);
                                        sb7.append("_");
                                        sb7.append(i282);
                                        str20 = sb7.toString();
                                    }
                                } else {
                                    action2 = action32;
                                    str20 = j10 != globalSecretChatId ? "tgenc" + DialogObject.getEncryptedChatId(j10) + "_" + i282 : null;
                                }
                                if (str20 == null) {
                                    wearableExtender2.setDismissalId(str20);
                                    NotificationCompat.WearableExtender wearableExtender3 = new NotificationCompat.WearableExtender();
                                    wearableExtender3.setDismissalId("summary_" + str20);
                                    j14 = j13;
                                    builder.extend(wearableExtender3);
                                } else {
                                    j14 = j13;
                                }
                                StringBuilder sb52 = new StringBuilder();
                                sb52.append("tgaccount");
                                long j172 = j8;
                                sb52.append(j172);
                                wearableExtender2.setBridgeTag(sb52.toString());
                                if (!dialogKey5.story) {
                                    j15 = j172;
                                    j16 = Long.MAX_VALUE;
                                    for (int i32 = 0; i32 < notificationsController3.storyPushMessages.size(); i32++) {
                                        j16 = Math.min(notificationsController3.storyPushMessages.get(i32).date, j16);
                                    }
                                    arrayList12 = arrayList8;
                                } else {
                                    j15 = j172;
                                    arrayList12 = arrayList8;
                                    j16 = ((MessageObject) arrayList12.get(0)).messageOwner.date * 1000;
                                }
                                NotificationCompat.Builder autoCancel2 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str9).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                if (dialogKey5.story) {
                                    arrayList12 = notificationsController3.storyPushMessages;
                                }
                                category = autoCancel2.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j16).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity2).extend(wearableExtender2).setSortKey(String.valueOf(Long.MAX_VALUE - j16)).setCategory("msg");
                                Intent intent42 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                intent42.putExtra("messageDate", i11);
                                intent42.putExtra("dialogId", j10);
                                intent42.putExtra(str242, notificationsController3.currentAccount);
                                if (dialogKey5.story) {
                                    intent42.putExtra("story", true);
                                }
                                category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent42, 167772160));
                                if (z6) {
                                    category.setGroup(notificationsController3.notificationGroup);
                                    category.setGroupAlertBehavior(1);
                                }
                                if (action2 != null) {
                                    category.addAction(action2);
                                }
                                if (!z5 && !dialogKey5.story) {
                                    category.addAction(build2);
                                }
                                if (arrayList3.size() != 1 && !TextUtils.isEmpty(str) && !dialogKey5.story) {
                                    category.setSubText(str);
                                }
                                if (DialogObject.isEncryptedDialog(j10)) {
                                    category.setLocalOnly(true);
                                }
                                if (bitmap3 != null) {
                                    category.setLargeIcon(bitmap3);
                                }
                                if (!AndroidUtilities.needShowPasscode(false) && !SharedConfig.isWaitingForPasscodeEnter && arrayList11 != null) {
                                    size3 = arrayList11.size();
                                    i17 = 0;
                                    while (i17 < size3) {
                                        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList23 = arrayList11;
                                        TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow2 = arrayList23.get(i17);
                                        int size5 = tLRPC$TL_keyboardButtonRow2.buttons.size();
                                        int i33 = 0;
                                        while (i33 < size5) {
                                            TLRPC$KeyboardButton tLRPC$KeyboardButton = tLRPC$TL_keyboardButtonRow2.buttons.get(i33);
                                            if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonCallback) {
                                                i18 = size3;
                                                arrayList13 = arrayList23;
                                                Intent intent5 = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                                intent5.putExtra(str242, notificationsController3.currentAccount);
                                                intent5.putExtra("did", j10);
                                                byte[] bArr = tLRPC$KeyboardButton.data;
                                                if (bArr != null) {
                                                    intent5.putExtra("data", bArr);
                                                }
                                                intent5.putExtra("mid", i292);
                                                String str28 = tLRPC$KeyboardButton.text;
                                                Context context = ApplicationLoader.applicationContext;
                                                int i34 = notificationsController3.lastButtonId;
                                                tLRPC$TL_keyboardButtonRow = tLRPC$TL_keyboardButtonRow2;
                                                notificationsController3.lastButtonId = i34 + 1;
                                                category.addAction(0, str28, PendingIntent.getBroadcast(context, i34, intent5, 167772160));
                                            } else {
                                                i18 = size3;
                                                arrayList13 = arrayList23;
                                                tLRPC$TL_keyboardButtonRow = tLRPC$TL_keyboardButtonRow2;
                                            }
                                            i33++;
                                            size3 = i18;
                                            arrayList23 = arrayList13;
                                            tLRPC$TL_keyboardButtonRow2 = tLRPC$TL_keyboardButtonRow;
                                        }
                                        i17++;
                                        arrayList11 = arrayList23;
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
                                Notification notification32 = notification;
                                if (Build.VERSION.SDK_INT >= 26) {
                                    notificationsController3.setNotificationChannel(notification32, category, z6);
                                }
                                j7 = j15;
                                longSparseArray6 = longSparseArray7;
                                z10 = z6;
                                notification2 = notification32;
                                arrayList6 = arrayList2;
                                arrayList6.add(new 1NotificationHolder(num.intValue(), j10, dialogKey5.story, j14, str9, tLRPC$User5, tLRPC$Chat2, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                notificationsController2 = this;
                                notificationsController2.wearNotificationsIds.put(j10, num);
                                i5 = i9 + 1;
                                arrayList17 = arrayList6;
                                size = i8;
                                arrayList14 = arrayList3;
                                z4 = z10;
                                longSparseArray11 = longSparseArray4;
                                clientUserId = j7;
                                longSparseArray12 = longSparseArray5;
                                longSparseArray = longSparseArray6;
                                build3 = notification2;
                                i4 = 7;
                                notificationsController3 = notificationsController2;
                            }
                        }
                    }
                    str8 = "currentAccount";
                    action = build;
                    if (messageObject2 == null) {
                    }
                    if (person == null) {
                    }
                    messagingStyle = new NotificationCompat.MessagingStyle("");
                    messagingStyle2 = messagingStyle;
                    i13 = Build.VERSION.SDK_INT;
                    if (i13 >= 28) {
                    }
                    messagingStyle2.setConversationTitle(format);
                    messagingStyle2.setGroupConversation(i13 >= 28 || (!z9 && DialogObject.isChatDialog(j3)) || UserObject.isReplyUser(j3));
                    StringBuilder sb322 = new StringBuilder();
                    String[] strArr222 = new String[1];
                    String str2322 = "";
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
                    StringBuilder sb422 = new StringBuilder();
                    sb422.append("show extra notifications chatId ");
                    sb422.append(j10);
                    sb422.append(" topicId ");
                    j13 = j9;
                    sb422.append(j13);
                    FileLog.d(sb422.toString());
                    if (j13 != 0) {
                    }
                    String str2422 = str8;
                    intent222.putExtra(str2422, notificationsController3.currentAccount);
                    PendingIntent activity22 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222, 1140850688);
                    NotificationCompat.WearableExtender wearableExtender22 = new NotificationCompat.WearableExtender();
                    NotificationCompat.Action action322 = action;
                    if (action != null) {
                    }
                    Intent intent322 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                    intent322.addFlags(32);
                    intent322.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                    intent322.putExtra("dialog_id", j10);
                    int i2822 = i12;
                    intent322.putExtra(str7, i2822);
                    intent322.putExtra(str2422, notificationsController3.currentAccount);
                    int i2922 = i14;
                    arrayList11 = arrayList7;
                    bitmap3 = bitmap2;
                    build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent322, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                    if (DialogObject.isEncryptedDialog(j10)) {
                    }
                    if (str20 == null) {
                    }
                    StringBuilder sb522 = new StringBuilder();
                    sb522.append("tgaccount");
                    long j1722 = j8;
                    sb522.append(j1722);
                    wearableExtender22.setBridgeTag(sb522.toString());
                    if (!dialogKey5.story) {
                    }
                    NotificationCompat.Builder autoCancel22 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str9).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                    if (dialogKey5.story) {
                    }
                    category = autoCancel22.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j16).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity22).extend(wearableExtender22).setSortKey(String.valueOf(Long.MAX_VALUE - j16)).setCategory("msg");
                    Intent intent422 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                    intent422.putExtra("messageDate", i11);
                    intent422.putExtra("dialogId", j10);
                    intent422.putExtra(str2422, notificationsController3.currentAccount);
                    if (dialogKey5.story) {
                    }
                    category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent422, 167772160));
                    if (z6) {
                    }
                    if (action2 != null) {
                    }
                    if (!z5) {
                        category.addAction(build2);
                    }
                    if (arrayList3.size() != 1) {
                    }
                    if (DialogObject.isEncryptedDialog(j10)) {
                    }
                    if (bitmap3 != null) {
                    }
                    if (!AndroidUtilities.needShowPasscode(false)) {
                        size3 = arrayList11.size();
                        i17 = 0;
                        while (i17 < size3) {
                        }
                    }
                    if (tLRPC$Chat2 == null) {
                    }
                    tLRPC$User5 = tLRPC$User4;
                    Notification notification322 = notification;
                    if (Build.VERSION.SDK_INT >= 26) {
                    }
                    j7 = j15;
                    longSparseArray6 = longSparseArray7;
                    z10 = z6;
                    notification2 = notification322;
                    arrayList6 = arrayList2;
                    arrayList6.add(new 1NotificationHolder(num.intValue(), j10, dialogKey5.story, j14, str9, tLRPC$User5, tLRPC$Chat2, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                    notificationsController2 = this;
                    notificationsController2.wearNotificationsIds.put(j10, num);
                    i5 = i9 + 1;
                    arrayList17 = arrayList6;
                    size = i8;
                    arrayList14 = arrayList3;
                    z4 = z10;
                    longSparseArray11 = longSparseArray4;
                    clientUserId = j7;
                    longSparseArray12 = longSparseArray5;
                    longSparseArray = longSparseArray6;
                    build3 = notification2;
                    i4 = 7;
                    notificationsController3 = notificationsController2;
                }
                LongSparseArray longSparseArray14 = longSparseArray;
                longSparseArray2 = longSparseArray12;
                Notification notification4 = build3;
                NotificationsController notificationsController4 = notificationsController3;
                ArrayList arrayList24 = arrayList17;
                if (!z4) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("show summary with id " + notificationsController4.notificationId);
                    }
                    try {
                        notificationManager.notify(notificationsController4.notificationId, notification4);
                        notificationsController = notificationsController4;
                        arrayList = arrayList24;
                    } catch (SecurityException e5) {
                        FileLog.e(e5);
                        notificationsController = this;
                        arrayList = arrayList24;
                        notificationsController.resetNotificationSound(builder, j, j2, str2, jArr, i, uri, i2, z, z2, z3, i3);
                    }
                } else {
                    notificationsController = notificationsController4;
                    arrayList = arrayList24;
                    if (notificationsController.openedInBubbleDialogs.isEmpty()) {
                        notificationManager.cancel(notificationsController.notificationId);
                    }
                }
                i6 = 0;
                while (i6 < longSparseArray2.size()) {
                    LongSparseArray longSparseArray15 = longSparseArray2;
                    if (!notificationsController.openedInBubbleDialogs.contains(Long.valueOf(longSparseArray15.keyAt(i6)))) {
                        Integer num5 = (Integer) longSparseArray15.valueAt(i6);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("cancel notification id " + num5);
                        }
                        notificationManager.cancel(num5.intValue());
                    }
                    i6++;
                    longSparseArray2 = longSparseArray15;
                }
                ArrayList arrayList25 = new ArrayList(arrayList.size());
                size2 = arrayList.size();
                i7 = 0;
                while (i7 < size2) {
                    ArrayList arrayList26 = arrayList;
                    1NotificationHolder r4 = (1NotificationHolder) arrayList26.get(i7);
                    arrayList25.clear();
                    if (Build.VERSION.SDK_INT < 29 || DialogObject.isEncryptedDialog(r4.dialogId)) {
                        longSparseArray3 = longSparseArray14;
                    } else {
                        NotificationCompat.Builder builder3 = r4.notification;
                        long j21 = r4.dialogId;
                        longSparseArray3 = longSparseArray14;
                        String createNotificationShortcut = createNotificationShortcut(builder3, j21, r4.name, r4.user, r4.chat, (Person) longSparseArray3.get(j21), !r4.story);
                        if (createNotificationShortcut != null) {
                            arrayList25.add(createNotificationShortcut);
                        }
                    }
                    r4.call();
                    if (!unsupportedNotificationShortcut() && !arrayList25.isEmpty()) {
                        ShortcutManagerCompat.removeDynamicShortcuts(ApplicationLoader.applicationContext, arrayList25);
                    }
                    i7++;
                    arrayList = arrayList26;
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
        i4 = 7;
        longSparseArray = new LongSparseArray();
        size = arrayList14.size();
        i5 = 0;
        while (i5 < size) {
            dialogKey = (DialogKey) arrayList14.get(i5);
            if (!dialogKey.story) {
            }
            int i262 = (Integer) longSparseArray12.get(j3);
            longSparseArray4 = longSparseArray11;
            z6 = z4;
            if (!dialogKey.story) {
            }
            Integer num42 = i262;
            int i272 = 0;
            while (i10 < arrayList4.size()) {
            }
            if (!dialogKey.story) {
            }
            String str222 = string;
            if (z5) {
            }
            if (tLRPC$FileLocation6 == null) {
            }
            if (tLRPC$Chat != null) {
            }
            Bitmap bitmap62 = bitmap;
            if (z9) {
            }
            file2 = file;
            tLRPC$Chat2 = tLRPC$Chat;
            Intent intent6 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
            intent6.putExtra("dialog_id", j3);
            intent6.putExtra("max_id", id);
            intent6.putExtra("topic_id", j6);
            intent6.putExtra("currentAccount", notificationsController3.currentAccount);
            str7 = "max_id";
            PendingIntent broadcast2 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent6, 167772160);
            RemoteInput build42 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
            if (!DialogObject.isChatDialog(j3)) {
            }
            build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast2).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build42).setShowsUserInterface(false).build();
            num2 = notificationsController3.pushDialogs.get(j3);
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
            action = build;
            if (messageObject2 == null) {
            }
            if (person == null) {
            }
            messagingStyle = new NotificationCompat.MessagingStyle("");
            messagingStyle2 = messagingStyle;
            i13 = Build.VERSION.SDK_INT;
            if (i13 >= 28) {
            }
            messagingStyle2.setConversationTitle(format);
            messagingStyle2.setGroupConversation(i13 >= 28 || (!z9 && DialogObject.isChatDialog(j3)) || UserObject.isReplyUser(j3));
            StringBuilder sb3222 = new StringBuilder();
            String[] strArr2222 = new String[1];
            String str23222 = "";
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
            StringBuilder sb4222 = new StringBuilder();
            sb4222.append("show extra notifications chatId ");
            sb4222.append(j10);
            sb4222.append(" topicId ");
            j13 = j9;
            sb4222.append(j13);
            FileLog.d(sb4222.toString());
            if (j13 != 0) {
            }
            String str24222 = str8;
            intent2222.putExtra(str24222, notificationsController3.currentAccount);
            PendingIntent activity222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2222, 1140850688);
            NotificationCompat.WearableExtender wearableExtender222 = new NotificationCompat.WearableExtender();
            NotificationCompat.Action action3222 = action;
            if (action != null) {
            }
            Intent intent3222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
            intent3222.addFlags(32);
            intent3222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
            intent3222.putExtra("dialog_id", j10);
            int i28222 = i12;
            intent3222.putExtra(str7, i28222);
            intent3222.putExtra(str24222, notificationsController3.currentAccount);
            int i29222 = i14;
            arrayList11 = arrayList7;
            bitmap3 = bitmap2;
            build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent3222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
            if (DialogObject.isEncryptedDialog(j10)) {
            }
            if (str20 == null) {
            }
            StringBuilder sb5222 = new StringBuilder();
            sb5222.append("tgaccount");
            long j17222 = j8;
            sb5222.append(j17222);
            wearableExtender222.setBridgeTag(sb5222.toString());
            if (!dialogKey5.story) {
            }
            NotificationCompat.Builder autoCancel222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str9).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
            if (dialogKey5.story) {
            }
            category = autoCancel222.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j16).setShowWhen(true).setStyle(messagingStyle2).setContentIntent(activity222).extend(wearableExtender222).setSortKey(String.valueOf(Long.MAX_VALUE - j16)).setCategory("msg");
            Intent intent4222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
            intent4222.putExtra("messageDate", i11);
            intent4222.putExtra("dialogId", j10);
            intent4222.putExtra(str24222, notificationsController3.currentAccount);
            if (dialogKey5.story) {
            }
            category.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num.intValue(), intent4222, 167772160));
            if (z6) {
            }
            if (action2 != null) {
            }
            if (!z5) {
            }
            if (arrayList3.size() != 1) {
            }
            if (DialogObject.isEncryptedDialog(j10)) {
            }
            if (bitmap3 != null) {
            }
            if (!AndroidUtilities.needShowPasscode(false)) {
            }
            if (tLRPC$Chat2 == null) {
            }
            tLRPC$User5 = tLRPC$User4;
            Notification notification3222 = notification;
            if (Build.VERSION.SDK_INT >= 26) {
            }
            j7 = j15;
            longSparseArray6 = longSparseArray7;
            z10 = z6;
            notification2 = notification3222;
            arrayList6 = arrayList2;
            arrayList6.add(new 1NotificationHolder(num.intValue(), j10, dialogKey5.story, j14, str9, tLRPC$User5, tLRPC$Chat2, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
            notificationsController2 = this;
            notificationsController2.wearNotificationsIds.put(j10, num);
            i5 = i9 + 1;
            arrayList17 = arrayList6;
            size = i8;
            arrayList14 = arrayList3;
            z4 = z10;
            longSparseArray11 = longSparseArray4;
            clientUserId = j7;
            longSparseArray12 = longSparseArray5;
            longSparseArray = longSparseArray6;
            build3 = notification2;
            i4 = 7;
            notificationsController3 = notificationsController2;
        }
        LongSparseArray longSparseArray142 = longSparseArray;
        longSparseArray2 = longSparseArray12;
        Notification notification42 = build3;
        NotificationsController notificationsController42 = notificationsController3;
        ArrayList arrayList242 = arrayList17;
        if (!z4) {
        }
        i6 = 0;
        while (i6 < longSparseArray2.size()) {
        }
        ArrayList arrayList252 = new ArrayList(arrayList.size());
        size2 = arrayList.size();
        i7 = 0;
        while (i7 < size2) {
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
            if (isGlobalNotificationsEnabled(j)) {
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

    public void muteDialog(long j, long j2, boolean z) {
        if (z) {
            getInstance(this.currentAccount).muteUntil(j, j2, ConnectionsManager.DEFAULT_DATACENTER_ID);
            return;
        }
        boolean isGlobalNotificationsEnabled = getInstance(this.currentAccount).isGlobalNotificationsEnabled(j);
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
                if (intValue != 0 && getMessagesController().isDialogMuted(j, intValue) != getMessagesController().isDialogMuted(j, 0L)) {
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

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
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
