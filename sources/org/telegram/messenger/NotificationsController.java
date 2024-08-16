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
                try {
                    notificationsController = Instance[i];
                    if (notificationsController == null) {
                        NotificationsController[] notificationsControllerArr = Instance;
                        NotificationsController notificationsController2 = new NotificationsController(i);
                        notificationsControllerArr[i] = notificationsController2;
                        notificationsController = notificationsController2;
                    }
                } finally {
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
        this.checkStoryPushesRunnable = new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda65
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
        this.notificationDelayRunnable = new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda66
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
        NotificationChannel notificationChannel;
        int importance;
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        if (OTHER_NOTIFICATIONS_CHANNEL == null) {
            sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
            OTHER_NOTIFICATIONS_CHANNEL = sharedPreferences.getString("OtherKey", "Other3");
        } else {
            sharedPreferences = null;
        }
        notificationChannel = systemNotificationManager.getNotificationChannel(OTHER_NOTIFICATIONS_CHANNEL);
        if (notificationChannel != null) {
            importance = notificationChannel.getImportance();
            if (importance == 0) {
                try {
                    systemNotificationManager.deleteNotificationChannel(OTHER_NOTIFICATIONS_CHANNEL);
                } catch (Exception e) {
                    FileLog.e(e);
                }
                OTHER_NOTIFICATIONS_CHANNEL = null;
                notificationChannel = null;
            }
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$cleanup$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanup$1() {
        List notificationChannels;
        String id;
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
                notificationChannels = systemNotificationManager.getNotificationChannels();
                int size = notificationChannels.size();
                for (int i = 0; i < size; i++) {
                    id = NotificationsController$$ExternalSyntheticApiModelOutline13.m(notificationChannels.get(i)).getId();
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda37
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda58
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda54
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda74
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda41
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda70
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
                        int intValue2 = num2.intValue() - 1;
                        Integer valueOf = Integer.valueOf(intValue2);
                        if (intValue2 <= 0) {
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
                                int intValue3 = this.total_unread_count - num2.intValue();
                                this.total_unread_count = intValue3;
                                this.total_unread_count = intValue3 + num.intValue();
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda43
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda44
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda28
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda31
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda48
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda47
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processDeleteStory$14(j, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDeleteStory$14(long j, int i) {
        StoryNotification storyNotification = this.storyPushMessagesDict.get(j);
        if (storyNotification != null) {
            storyNotification.dateByIds.remove(Integer.valueOf(i));
            if (storyNotification.dateByIds.isEmpty()) {
                this.storyPushMessagesDict.remove(j);
                this.storyPushMessages.remove(storyNotification);
                getMessagesStorage().deleteStoryPushMessage(j);
                showOrUpdateNotification(false);
                return;
            }
            getMessagesStorage().putStoryPushMessage(storyNotification);
        }
    }

    public void processReadStories(final long j, int i) {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda61
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processReadStories$15(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processReadStories$15(long j) {
        StoryNotification storyNotification = this.storyPushMessagesDict.get(j);
        if (storyNotification != null) {
            this.storyPushMessagesDict.remove(j);
            this.storyPushMessages.remove(storyNotification);
            getMessagesStorage().deleteStoryPushMessage(j);
            showOrUpdateNotification(false);
            updateStoryPushesRunnable();
        }
    }

    public void processIgnoreStories() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda56
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda29
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda45
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda69
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processReadMessages$20(longSparseIntArray, arrayList, j, i2, i, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processReadMessages$20(LongSparseIntArray longSparseIntArray, final ArrayList arrayList, long j, int i, int i2, boolean z) {
        long j2;
        long j3;
        long j4 = 0;
        if (longSparseIntArray != null) {
            int i3 = 0;
            while (i3 < longSparseIntArray.size()) {
                long keyAt = longSparseIntArray.keyAt(i3);
                int i4 = longSparseIntArray.get(keyAt);
                int i5 = 0;
                while (i5 < this.pushMessages.size()) {
                    MessageObject messageObject = this.pushMessages.get(i5);
                    if (!messageObject.messageOwner.from_scheduled && messageObject.getDialogId() == keyAt && messageObject.getId() <= i4 && !messageObject.isStoryReactionPush) {
                        if (isPersonalMessage(messageObject)) {
                            this.personalCount--;
                        }
                        arrayList.add(messageObject);
                        if (messageObject.isStoryReactionPush) {
                            j3 = messageObject.getDialogId();
                        } else {
                            long j5 = messageObject.messageOwner.peer_id.channel_id;
                            j3 = j5 != j4 ? -j5 : j4;
                        }
                        SparseArray<MessageObject> sparseArray = this.pushMessagesDict.get(j3);
                        if (sparseArray != null) {
                            sparseArray.remove(messageObject.getId());
                            if (sparseArray.size() == 0) {
                                this.pushMessagesDict.remove(j3);
                            }
                        }
                        this.delayedPushMessages.remove(messageObject);
                        this.pushMessages.remove(i5);
                        i5--;
                    }
                    i5++;
                    j4 = 0;
                }
                i3++;
                j4 = 0;
            }
        }
        if (j != j4 && (i != 0 || i2 != 0)) {
            int i6 = 0;
            while (i6 < this.pushMessages.size()) {
                MessageObject messageObject2 = this.pushMessages.get(i6);
                if (messageObject2.getDialogId() == j && !messageObject2.isStoryReactionPush && (i2 == 0 ? z ? messageObject2.getId() == i || i < 0 : messageObject2.getId() <= i || i < 0 : messageObject2.messageOwner.date <= i2)) {
                    if (isPersonalMessage(messageObject2)) {
                        this.personalCount--;
                    }
                    if (messageObject2.isStoryReactionPush) {
                        j2 = messageObject2.getDialogId();
                    } else {
                        long j6 = messageObject2.messageOwner.peer_id.channel_id;
                        j2 = j6 != 0 ? -j6 : 0L;
                    }
                    SparseArray<MessageObject> sparseArray2 = this.pushMessagesDict.get(j2);
                    if (sparseArray2 != null) {
                        sparseArray2.remove(messageObject2.getId());
                        if (sparseArray2.size() == 0) {
                            this.pushMessagesDict.remove(j2);
                        }
                    }
                    this.pushMessages.remove(i6);
                    this.delayedPushMessages.remove(messageObject2);
                    arrayList.add(messageObject2);
                    i6--;
                }
                i6++;
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda64
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda75
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$processEditedMessages$21(longSparseArray);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processEditedMessages$21(LongSparseArray longSparseArray) {
        long j;
        int size = longSparseArray.size();
        boolean z = false;
        for (int i = 0; i < size; i++) {
            longSparseArray.keyAt(i);
            ArrayList arrayList = (ArrayList) longSparseArray.valueAt(i);
            int size2 = arrayList.size();
            for (int i2 = 0; i2 < size2; i2++) {
                MessageObject messageObject = (MessageObject) arrayList.get(i2);
                if (messageObject.isStoryReactionPush) {
                    j = messageObject.getDialogId();
                } else {
                    long j2 = messageObject.messageOwner.peer_id.channel_id;
                    j = j2 != 0 ? -j2 : 0L;
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
            notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda68
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
    /* JADX WARN: Removed duplicated region for block: B:73:0x0190  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x01d5  */
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
        long j2;
        int i3;
        boolean z6;
        MessageObject messageObject;
        long j3;
        SparseArray<MessageObject> sparseArray;
        long j4;
        long j5;
        long j6;
        int i4;
        boolean z7;
        long j7;
        boolean z8;
        long j8;
        SparseArray<MessageObject> sparseArray2;
        MessageObject messageObject2;
        TLRPC$Message tLRPC$Message;
        ArrayList arrayList3 = arrayList;
        LongSparseArray longSparseArray = new LongSparseArray();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        boolean z9 = notificationsSettings.getBoolean("PinnedMessages", true);
        int i5 = 0;
        int i6 = 0;
        boolean z10 = false;
        boolean z11 = false;
        boolean z12 = false;
        boolean z13 = false;
        while (i6 < arrayList.size()) {
            MessageObject messageObject3 = (MessageObject) arrayList3.get(i6);
            if (messageObject3.messageOwner != null) {
                if (!messageObject3.isImportedForward()) {
                    TLRPC$Message tLRPC$Message2 = messageObject3.messageOwner;
                    TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message2.action;
                    if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetMessagesTTL)) {
                        if (tLRPC$Message2.silent) {
                            if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp)) {
                            }
                        }
                    }
                }
                z4 = z9;
                i3 = i6;
                z6 = z10;
                z10 = z6;
                i6 = i3 + 1;
                arrayList3 = arrayList;
                z9 = z4;
            }
            if (!MessageObject.isTopicActionMessage(messageObject3)) {
                if (messageObject3.isStoryPush) {
                    long currentTimeMillis = messageObject3.messageOwner == null ? System.currentTimeMillis() : tLRPC$Message.date * 1000;
                    long dialogId = messageObject3.getDialogId();
                    int id = messageObject3.getId();
                    StoryNotification storyNotification = this.storyPushMessagesDict.get(dialogId);
                    if (storyNotification != null) {
                        storyNotification.dateByIds.put(Integer.valueOf(id), new Pair<>(Long.valueOf(currentTimeMillis), Long.valueOf(currentTimeMillis + 86400000)));
                        boolean z14 = storyNotification.hidden;
                        boolean z15 = messageObject3.isStoryPushHidden;
                        if (z14 != z15) {
                            storyNotification.hidden = z15;
                            z13 = true;
                        }
                        storyNotification.date = storyNotification.getLeastDate();
                        getMessagesStorage().putStoryPushMessage(storyNotification);
                        z11 = true;
                    } else {
                        StoryNotification storyNotification2 = new StoryNotification(dialogId, messageObject3.localName, id, currentTimeMillis);
                        storyNotification2.hidden = messageObject3.isStoryPushHidden;
                        this.storyPushMessages.add(storyNotification2);
                        this.storyPushMessagesDict.put(dialogId, storyNotification2);
                        getMessagesStorage().putStoryPushMessage(storyNotification2);
                        z10 = true;
                        z13 = true;
                    }
                    Collections.sort(this.storyPushMessages, Comparator$-CC.comparingLong(new ToLongFunction() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda24
                        @Override // j$.util.function.ToLongFunction
                        public final long applyAsLong(Object obj) {
                            long j9;
                            j9 = ((NotificationsController.StoryNotification) obj).date;
                            return j9;
                        }
                    }));
                    z4 = z9;
                    i3 = i6;
                } else {
                    int id2 = messageObject3.getId();
                    if (messageObject3.isFcmMessage()) {
                        j = messageObject3.messageOwner.random_id;
                        z4 = z9;
                    } else {
                        z4 = z9;
                        j = 0;
                    }
                    long dialogId2 = messageObject3.getDialogId();
                    if (messageObject3.isFcmMessage()) {
                        z5 = messageObject3.localChannel;
                    } else {
                        if (DialogObject.isChatDialog(dialogId2)) {
                            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-dialogId2));
                            if (ChatObject.isChannel(chat) && !chat.megagroup) {
                                z5 = true;
                            }
                        }
                        z5 = false;
                    }
                    if (messageObject3.isStoryReactionPush) {
                        j2 = messageObject3.getDialogId();
                    } else {
                        long j9 = messageObject3.messageOwner.peer_id.channel_id;
                        j2 = j9 != 0 ? -j9 : 0L;
                    }
                    SparseArray<MessageObject> sparseArray3 = this.pushMessagesDict.get(j2);
                    MessageObject messageObject4 = sparseArray3 != null ? sparseArray3.get(id2) : null;
                    long j10 = j;
                    if (messageObject4 == null) {
                        long j11 = messageObject3.messageOwner.random_id;
                        if (j11 != 0 && (messageObject4 = this.fcmRandomMessagesDict.get(j11)) != null) {
                            i3 = i6;
                            z6 = z10;
                            this.fcmRandomMessagesDict.remove(messageObject3.messageOwner.random_id);
                            messageObject = messageObject4;
                            if (messageObject == null) {
                                if (messageObject.isFcmMessage()) {
                                    if (sparseArray3 == null) {
                                        sparseArray3 = new SparseArray<>();
                                        this.pushMessagesDict.put(j2, sparseArray3);
                                    }
                                    sparseArray3.put(id2, messageObject3);
                                    int indexOf = this.pushMessages.indexOf(messageObject);
                                    if (indexOf >= 0) {
                                        this.pushMessages.set(indexOf, messageObject3);
                                        messageObject2 = messageObject3;
                                        i5 = addToPopupMessages(arrayList2, messageObject3, dialogId2, z5, notificationsSettings);
                                    } else {
                                        messageObject2 = messageObject3;
                                    }
                                    if (z) {
                                        boolean z16 = messageObject2.localEdit;
                                        if (z16) {
                                            getMessagesStorage().putPushMessage(messageObject2);
                                        }
                                        z11 = z16;
                                    }
                                }
                            } else if (!z11) {
                                if (z) {
                                    getMessagesStorage().putPushMessage(messageObject3);
                                }
                                long topicId = MessageObject.getTopicId(this.currentAccount, messageObject3.messageOwner, getMessagesController().isForum(messageObject3));
                                if (dialogId2 != this.openedDialogId || !ApplicationLoader.isScreenOn || messageObject3.isStoryReactionPush) {
                                    TLRPC$Message tLRPC$Message3 = messageObject3.messageOwner;
                                    if (!tLRPC$Message3.mentioned) {
                                        j3 = dialogId2;
                                    } else if (z4 || !(tLRPC$Message3.action instanceof TLRPC$TL_messageActionPinMessage)) {
                                        j3 = messageObject3.getFromChatId();
                                    }
                                    if (isPersonalMessage(messageObject3)) {
                                        this.personalCount++;
                                    }
                                    DialogObject.isChatDialog(j3);
                                    int indexOfKey = longSparseArray.indexOfKey(j3);
                                    if (indexOfKey >= 0 && topicId == 0) {
                                        z8 = ((Boolean) longSparseArray.valueAt(indexOfKey)).booleanValue();
                                        sparseArray = sparseArray3;
                                        j6 = j2;
                                        i4 = i5;
                                        j4 = dialogId2;
                                        j5 = j10;
                                        j7 = j3;
                                    } else {
                                        sparseArray = sparseArray3;
                                        j4 = dialogId2;
                                        j5 = j10;
                                        long j12 = j3;
                                        j6 = j2;
                                        i4 = i5;
                                        int notifyOverride = getNotifyOverride(notificationsSettings, j3, topicId);
                                        if (notifyOverride == -1) {
                                            z7 = isGlobalNotificationsEnabled(j12, Boolean.valueOf(z5), messageObject3.isReactionPush, messageObject3.isStoryReactionPush);
                                        } else {
                                            z7 = notifyOverride != 2;
                                        }
                                        j7 = j12;
                                        longSparseArray.put(j7, Boolean.valueOf(z7));
                                        z8 = z7;
                                    }
                                    if (z8) {
                                        if (z) {
                                            j8 = j7;
                                            i5 = i4;
                                        } else {
                                            j8 = j7;
                                            i5 = addToPopupMessages(arrayList2, messageObject3, j7, z5, notificationsSettings);
                                        }
                                        if (!z12) {
                                            z12 = messageObject3.messageOwner.from_scheduled;
                                        }
                                        this.delayedPushMessages.add(messageObject3);
                                        appendMessage(messageObject3);
                                        if (id2 != 0) {
                                            if (sparseArray == null) {
                                                sparseArray2 = new SparseArray<>();
                                                this.pushMessagesDict.put(j6, sparseArray2);
                                            } else {
                                                sparseArray2 = sparseArray;
                                            }
                                            sparseArray2.put(id2, messageObject3);
                                        } else if (j5 != 0) {
                                            this.fcmRandomMessagesDict.put(j5, messageObject3);
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
                                    if (messageObject3.isReactionPush) {
                                        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
                                        sparseBooleanArray.put(id2, true);
                                        getMessagesController().checkUnreadReactions(j8, topicId, sparseBooleanArray);
                                    }
                                    z10 = true;
                                } else if (!z) {
                                    playInChatSound();
                                }
                            }
                            z10 = z6;
                        }
                    }
                    i3 = i6;
                    z6 = z10;
                    messageObject = messageObject4;
                    if (messageObject == null) {
                    }
                    z10 = z6;
                }
                i6 = i3 + 1;
                arrayList3 = arrayList;
                z9 = z4;
            }
            z4 = z9;
            i3 = i6;
            z6 = z10;
            z10 = z6;
            i6 = i3 + 1;
            arrayList3 = arrayList;
            z9 = z4;
        }
        final int i7 = i5;
        boolean z17 = z10;
        if (z17) {
            this.notifyCheck = z2;
        }
        if (!arrayList2.isEmpty() && !AndroidUtilities.needShowPasscode() && !SharedConfig.isWaitingForPasscodeEnter) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda25
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationsController.this.lambda$processNewMessages$23(arrayList2, i7);
                }
            });
        }
        if (z || z12) {
            if (z11) {
                this.delayedPushMessages.clear();
                showOrUpdateNotification(this.notifyCheck);
            } else if (z17) {
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
                if (i8 != this.total_unread_count || z13) {
                    this.delayedPushMessages.clear();
                    showOrUpdateNotification(this.notifyCheck);
                    final int size = this.pushDialogs.size();
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda26
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
        if (z13) {
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda49
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda50
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda51
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda46
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
        LongSparseArray longSparseArray2;
        long j3;
        boolean z2;
        boolean z3;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        SharedPreferences sharedPreferences;
        MessageObject messageObject;
        SparseArray<MessageObject> sparseArray;
        long j4;
        long j5;
        int i;
        TLRPC$Message tLRPC$Message;
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
        LongSparseArray longSparseArray3 = new LongSparseArray();
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
                            int indexOfKey = longSparseArray3.indexOfKey(fromChatId);
                            if (indexOfKey >= 0 && topicId == 0) {
                                z4 = ((Boolean) longSparseArray3.valueAt(indexOfKey)).booleanValue();
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
                                    z4 = isGlobalNotificationsEnabled(fromChatId, messageObject.isReactionPush, messageObject.isStoryReactionPush);
                                } else {
                                    z4 = notifyOverride != 2;
                                }
                                longSparseArray3.put(fromChatId, Boolean.valueOf(z4));
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
            int indexOfKey2 = longSparseArray3.indexOfKey(keyAt);
            if (indexOfKey2 >= 0) {
                z3 = ((Boolean) longSparseArray3.valueAt(indexOfKey2)).booleanValue();
            } else {
                int notifyOverride2 = getNotifyOverride(sharedPreferences2, keyAt, 0L);
                if (notifyOverride2 == -1) {
                    z2 = isGlobalNotificationsEnabled(keyAt, false, false);
                } else {
                    z2 = notifyOverride2 != 2;
                }
                longSparseArray3.put(keyAt, Boolean.valueOf(z2));
                z3 = z2;
            }
            if (z3) {
                Integer num2 = (Integer) longSparseArray.valueAt(i3);
                int intValue = num2.intValue();
                this.pushDialogs.put(keyAt, num2);
                if (getMessagesController().isForum(keyAt)) {
                    this.total_unread_count += intValue > 0 ? 1 : 0;
                } else {
                    this.total_unread_count += intValue;
                }
            }
        }
        if (arrayList2 != null) {
            int i4 = 0;
            while (i4 < arrayList2.size()) {
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
                    int indexOfKey3 = longSparseArray3.indexOfKey(fromChatId2);
                    if (indexOfKey3 >= 0 && topicId2 == 0) {
                        j = j10;
                        z = ((Boolean) longSparseArray3.valueAt(indexOfKey3)).booleanValue();
                        j2 = fromChatId2;
                    } else {
                        long j11 = fromChatId2;
                        j = j10;
                        int notifyOverride3 = getNotifyOverride(sharedPreferences2, j11, topicId2);
                        if (notifyOverride3 == -1) {
                            j2 = j11;
                            z = isGlobalNotificationsEnabled(j2, messageObject3.isReactionPush, messageObject3.isStoryReactionPush);
                        } else {
                            j2 = j11;
                            z = notifyOverride3 != 2;
                        }
                        longSparseArray3.put(j2, Boolean.valueOf(z));
                    }
                    if (z && (j2 != this.openedDialogId || !ApplicationLoader.isScreenOn)) {
                        if (id != 0) {
                            if (messageObject3.isStoryReactionPush) {
                                j3 = messageObject3.getDialogId();
                            } else {
                                long j12 = messageObject3.messageOwner.peer_id.channel_id;
                                j3 = j12 != 0 ? -j12 : 0L;
                            }
                            SparseArray<MessageObject> sparseArray4 = this.pushMessagesDict.get(j3);
                            if (sparseArray4 == null) {
                                sparseArray4 = new SparseArray<>();
                                this.pushMessagesDict.put(j3, sparseArray4);
                            }
                            sparseArray4.put(id, messageObject3);
                            longSparseArray2 = longSparseArray3;
                        } else {
                            longSparseArray2 = longSparseArray3;
                            long j13 = j;
                            if (j13 != 0) {
                                this.fcmRandomMessagesDict.put(j13, messageObject3);
                            }
                        }
                        appendMessage(messageObject3);
                        if (dialogId2 != j2) {
                            Integer num3 = this.pushDialogsOverrideMention.get(dialogId2);
                            this.pushDialogsOverrideMention.put(dialogId2, Integer.valueOf(num3 == null ? 1 : num3.intValue() + 1));
                        }
                        Integer num4 = this.pushDialogs.get(j2);
                        int intValue2 = num4 != null ? num4.intValue() + 1 : 1;
                        if (getMessagesController().isForum(j2)) {
                            if (num4 != null) {
                                this.total_unread_count -= num4.intValue() > 0 ? 1 : 0;
                            }
                            this.total_unread_count += intValue2 > 0 ? 1 : 0;
                        } else {
                            if (num4 != null) {
                                this.total_unread_count -= num4.intValue();
                            }
                            this.total_unread_count += intValue2;
                        }
                        this.pushDialogs.put(j2, Integer.valueOf(intValue2));
                        i4++;
                        longSparseArray3 = longSparseArray2;
                    }
                }
                longSparseArray2 = longSparseArray3;
                i4++;
                longSparseArray3 = longSparseArray2;
            }
        }
        if (collection != null) {
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                StoryNotification storyNotification = (StoryNotification) it.next();
                long j14 = storyNotification.dialogId;
                StoryNotification storyNotification2 = this.storyPushMessagesDict.get(j14);
                if (storyNotification2 != null) {
                    storyNotification2.dateByIds.putAll(storyNotification.dateByIds);
                } else {
                    this.storyPushMessages.add(storyNotification);
                    this.storyPushMessagesDict.put(j14, storyNotification);
                }
            }
            Collections.sort(this.storyPushMessages, Comparator$-CC.comparingLong(new ToLongFunction() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda32
                @Override // j$.util.function.ToLongFunction
                public final long applyAsLong(Object obj) {
                    long j15;
                    j15 = ((NotificationsController.StoryNotification) obj).date;
                    return j15;
                }
            }));
        }
        final int size = this.pushDialogs.size();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda33
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda57
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

    /* JADX WARN: Code restructure failed: missing block: B:154:0x0252, code lost:
        if (r12.getBoolean("EnablePreviewAll", true) == false) goto L828;
     */
    /* JADX WARN: Code restructure failed: missing block: B:161:0x0260, code lost:
        if (r12.getBoolean("EnablePreviewGroup", r9) != false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x026a, code lost:
        if (r12.getBoolean(r28, r9) != false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x026c, code lost:
        r3 = r30.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x027c, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageService) == false) goto L660;
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x027e, code lost:
        r31[0] = null;
        r4 = r3.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x0286, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetSameChatWallPaper) == false) goto L143;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x0290, code lost:
        return org.telegram.messenger.LocaleController.getString("WallpaperSameNotification", org.telegram.messenger.R.string.WallpaperSameNotification);
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x0293, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatWallPaper) == false) goto L147;
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x029d, code lost:
        return org.telegram.messenger.LocaleController.getString("WallpaperNotification", org.telegram.messenger.R.string.WallpaperNotification);
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x02a0, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGeoProximityReached) == false) goto L151;
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x02a8, code lost:
        return r30.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x02ab, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserJoined) != false) goto L657;
     */
    /* JADX WARN: Code restructure failed: missing block: B:182:0x02af, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionContactSignUp) == false) goto L155;
     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x02b6, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionUserUpdatedPhoto) == false) goto L159;
     */
    /* JADX WARN: Code restructure failed: missing block: B:187:0x02c6, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationContactNewPhoto", org.telegram.messenger.R.string.NotificationContactNewPhoto, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x02c9, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionLoginUnknownLocation) == false) goto L163;
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x02cb, code lost:
        r1 = org.telegram.messenger.LocaleController.formatString("formatDateAtTime", org.telegram.messenger.R.string.formatDateAtTime, org.telegram.messenger.LocaleController.getInstance().getFormatterYear().format(r30.messageOwner.date * 1000), org.telegram.messenger.LocaleController.getInstance().getFormatterDay().format(r30.messageOwner.date * 1000));
        r2 = org.telegram.messenger.R.string.NotificationUnrecognizedDevice;
        r3 = getUserConfig().getCurrentUser().first_name;
        r0 = r30.messageOwner.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x032d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationUnrecognizedDevice", r2, r3, r1, r0.title, r0.address);
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x0330, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGameScore) != false) goto L655;
     */
    /* JADX WARN: Code restructure failed: missing block: B:195:0x0334, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPaymentSent) == false) goto L167;
     */
    /* JADX WARN: Code restructure failed: missing block: B:198:0x033a, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPhoneCall) == false) goto L175;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x033e, code lost:
        if (r4.video == false) goto L173;
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x0348, code lost:
        return org.telegram.messenger.LocaleController.getString("CallMessageVideoIncomingMissed", org.telegram.messenger.R.string.CallMessageVideoIncomingMissed);
     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x0351, code lost:
        return org.telegram.messenger.LocaleController.getString("CallMessageIncomingMissed", org.telegram.messenger.R.string.CallMessageIncomingMissed);
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x0356, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser) == false) goto L222;
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x0358, code lost:
        r7 = r4.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:208:0x035c, code lost:
        if (r7 != 0) goto L182;
     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x0365, code lost:
        if (r4.users.size() != 1) goto L182;
     */
    /* JADX WARN: Code restructure failed: missing block: B:211:0x0367, code lost:
        r7 = r30.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:213:0x037c, code lost:
        if (r7 == 0) goto L207;
     */
    /* JADX WARN: Code restructure failed: missing block: B:215:0x0386, code lost:
        if (r30.messageOwner.peer_id.channel_id == 0) goto L190;
     */
    /* JADX WARN: Code restructure failed: missing block: B:217:0x038a, code lost:
        if (r6.megagroup != false) goto L190;
     */
    /* JADX WARN: Code restructure failed: missing block: B:219:0x039f, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.R.string.ChannelAddedByNotification, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:221:0x03a2, code lost:
        if (r7 != r24) goto L194;
     */
    /* JADX WARN: Code restructure failed: missing block: B:223:0x03b7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.R.string.NotificationInvitedToGroup, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:224:0x03b8, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r7));
     */
    /* JADX WARN: Code restructure failed: missing block: B:225:0x03c4, code lost:
        if (r0 != null) goto L197;
     */
    /* JADX WARN: Code restructure failed: missing block: B:226:0x03c6, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:229:0x03cc, code lost:
        if (r1 != r0.id) goto L205;
     */
    /* JADX WARN: Code restructure failed: missing block: B:231:0x03d0, code lost:
        if (r6.megagroup == false) goto L203;
     */
    /* JADX WARN: Code restructure failed: missing block: B:233:0x03e5, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.R.string.NotificationGroupAddSelfMega, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:235:0x03f9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.R.string.NotificationGroupAddSelf, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:237:0x0412, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r5, r6.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:238:0x0413, code lost:
        r1 = new java.lang.StringBuilder();
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x0423, code lost:
        if (r2 >= r30.messageOwner.action.users.size()) goto L219;
     */
    /* JADX WARN: Code restructure failed: missing block: B:241:0x0425, code lost:
        r4 = getMessagesController().getUser(r30.messageOwner.action.users.get(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:242:0x0439, code lost:
        if (r4 == null) goto L218;
     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x043b, code lost:
        r4 = org.telegram.messenger.UserObject.getUserName(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x0443, code lost:
        if (r1.length() == 0) goto L215;
     */
    /* JADX WARN: Code restructure failed: missing block: B:245:0x0445, code lost:
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:246:0x0448, code lost:
        r1.append(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:247:0x044b, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:249:0x0466, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r5, r6.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:251:0x046a, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCall) == false) goto L226;
     */
    /* JADX WARN: Code restructure failed: missing block: B:253:0x047e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.R.string.NotificationGroupCreatedCall, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:255:0x0481, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled) == false) goto L230;
     */
    /* JADX WARN: Code restructure failed: missing block: B:257:0x0489, code lost:
        return r30.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:259:0x048c, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall) == false) goto L263;
     */
    /* JADX WARN: Code restructure failed: missing block: B:260:0x048e, code lost:
        r1 = r4.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:261:0x0492, code lost:
        if (r1 != 0) goto L237;
     */
    /* JADX WARN: Code restructure failed: missing block: B:263:0x049b, code lost:
        if (r4.users.size() != 1) goto L237;
     */
    /* JADX WARN: Code restructure failed: missing block: B:264:0x049d, code lost:
        r1 = r30.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x04b0, code lost:
        if (r1 == 0) goto L248;
     */
    /* JADX WARN: Code restructure failed: missing block: B:268:0x04b4, code lost:
        if (r1 != r24) goto L243;
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x04c9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.R.string.NotificationGroupInvitedYouToCall, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:271:0x04ca, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r1));
     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x04d6, code lost:
        if (r0 != null) goto L246;
     */
    /* JADX WARN: Code restructure failed: missing block: B:273:0x04d8, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x04f4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r5, r6.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:277:0x04f5, code lost:
        r1 = new java.lang.StringBuilder();
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x0507, code lost:
        if (r3 >= r30.messageOwner.action.users.size()) goto L260;
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x0509, code lost:
        r4 = getMessagesController().getUser(r30.messageOwner.action.users.get(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x051d, code lost:
        if (r4 == null) goto L259;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x051f, code lost:
        r4 = org.telegram.messenger.UserObject.getUserName(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x0527, code lost:
        if (r1.length() == 0) goto L256;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x0529, code lost:
        r1.append(", ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x052c, code lost:
        r1.append(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x052f, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x054a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r5, r6.title, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x054d, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGiftCode) == false) goto L267;
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x0557, code lost:
        return org.telegram.messenger.LocaleController.getString("BoostingReceivedGiftNoName", org.telegram.messenger.R.string.BoostingReceivedGiftNoName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x055a, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink) == false) goto L271;
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x056f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.R.string.NotificationInvitedToGroupByLink, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x0574, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle) == false) goto L275;
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x0587, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.R.string.NotificationEditedGroupName, r5, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x058a, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto) != false) goto L639;
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x058e, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto) == false) goto L279;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x0594, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser) == false) goto L294;
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x0596, code lost:
        r3 = r4.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x059a, code lost:
        if (r3 != r24) goto L285;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x05af, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.R.string.NotificationGroupKickYou, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x05b5, code lost:
        if (r3 != r1) goto L289;
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x05c7, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.R.string.NotificationGroupLeftMember, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x05c8, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r30.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x05da, code lost:
        if (r0 != null) goto L292;
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x05dc, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x05f8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.R.string.NotificationGroupKickMember, r5, r6.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x05fb, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatCreate) == false) goto L298;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x0603, code lost:
        return r30.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x0606, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate) == false) goto L302;
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x060e, code lost:
        return r30.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x0611, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo) == false) goto L306;
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x0623, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x062a, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) == false) goto L310;
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x0638, code lost:
        return org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r4.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x063b, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken) == false) goto L314;
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x0643, code lost:
        return r30.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x0646, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPinMessage) == false) goto L618;
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x064c, code lost:
        if (r6 == null) goto L420;
     */
    /* JADX WARN: Code restructure failed: missing block: B:347:0x0652, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r6) == false) goto L322;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x0656, code lost:
        if (r6.megagroup == false) goto L420;
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x065c, code lost:
        r0 = r30.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x065e, code lost:
        if (r0 != null) goto L326;
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x0673, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x067d, code lost:
        if (r0.isMusic() == false) goto L330;
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x068f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.R.string.NotificationActionPinnedMusic, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x0696, code lost:
        if (r0.isVideo() == false) goto L338;
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x06a0, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L336;
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x06c9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r5, "📹 " + r0.messageOwner.message, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x06dd, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.R.string.NotificationActionPinnedVideo, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:369:0x06e2, code lost:
        if (r0.isGif() == false) goto L346;
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x06ec, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L344;
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x0715, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r5, "🎬 " + r0.messageOwner.message, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:375:0x0729, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.R.string.NotificationActionPinnedGif, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:377:0x0731, code lost:
        if (r0.isVoice() == false) goto L350;
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x0743, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.R.string.NotificationActionPinnedVoice, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x0748, code lost:
        if (r0.isRoundVideo() == false) goto L354;
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x075a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.R.string.NotificationActionPinnedRound, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:385:0x075f, code lost:
        if (r0.isSticker() != false) goto L413;
     */
    /* JADX WARN: Code restructure failed: missing block: B:387:0x0765, code lost:
        if (r0.isAnimatedSticker() == false) goto L358;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x076b, code lost:
        r3 = r0.messageOwner;
        r9 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:390:0x0771, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x0779, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L364;
     */
    /* JADX WARN: Code restructure failed: missing block: B:394:0x07a2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r5, "📎 " + r0.messageOwner.message, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:396:0x07b6, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.R.string.NotificationActionPinnedFile, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:0x07b9, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L410;
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x07bd, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L370;
     */
    /* JADX WARN: Code restructure failed: missing block: B:403:0x07c6, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L374;
     */
    /* JADX WARN: Code restructure failed: missing block: B:405:0x07db, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.R.string.NotificationActionPinnedGeoLive, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x07de, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L378;
     */
    /* JADX WARN: Code restructure failed: missing block: B:408:0x07e0, code lost:
        r9 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:409:0x0800, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.R.string.NotificationActionPinnedContact2, r5, r6.title, org.telegram.messenger.ContactsController.formatName(r9.first_name, r9.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x0803, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L386;
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x0805, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r9).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x080b, code lost:
        if (r0.quiz == false) goto L384;
     */
    /* JADX WARN: Code restructure failed: missing block: B:415:0x0827, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.R.string.NotificationActionPinnedQuiz2, r5, r6.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x0842, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.R.string.NotificationActionPinnedPoll2, r5, r6.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x0845, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L394;
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x084d, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L392;
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x0876, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r5, "🖼 " + r0.messageOwner.message, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x088a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.R.string.NotificationActionPinnedPhoto, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x0890, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L398;
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x08a2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.R.string.NotificationActionPinnedGame, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:430:0x08a3, code lost:
        r3 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x08a5, code lost:
        if (r3 == null) goto L408;
     */
    /* JADX WARN: Code restructure failed: missing block: B:433:0x08ab, code lost:
        if (r3.length() <= 0) goto L408;
     */
    /* JADX WARN: Code restructure failed: missing block: B:434:0x08ad, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x08b3, code lost:
        if (r0.length() <= 20) goto L407;
     */
    /* JADX WARN: Code restructure failed: missing block: B:436:0x08b5, code lost:
        r3 = new java.lang.StringBuilder();
        r7 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x08ca, code lost:
        r7 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:438:0x08cb, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedText;
        r2 = r6.title;
        r3 = new java.lang.Object[3];
        r3[r7] = r5;
        r3[1] = r0;
        r3[2] = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x08de, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", r1, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:441:0x08f0, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:443:0x0901, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.R.string.NotificationActionPinnedGeo, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:444:0x0902, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:445:0x0906, code lost:
        if (r0 == null) goto L418;
     */
    /* JADX WARN: Code restructure failed: missing block: B:447:0x091c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmoji, r5, r6.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:449:0x092e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.R.string.NotificationActionPinnedSticker, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:450:0x092f, code lost:
        if (r6 == null) goto L520;
     */
    /* JADX WARN: Code restructure failed: missing block: B:451:0x0931, code lost:
        r0 = r30.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:452:0x0933, code lost:
        if (r0 != null) goto L426;
     */
    /* JADX WARN: Code restructure failed: missing block: B:454:0x0944, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:456:0x094c, code lost:
        if (r0.isMusic() == false) goto L430;
     */
    /* JADX WARN: Code restructure failed: missing block: B:458:0x095c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.R.string.NotificationActionPinnedMusicChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:460:0x0963, code lost:
        if (r0.isVideo() == false) goto L438;
     */
    /* JADX WARN: Code restructure failed: missing block: B:462:0x096d, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L436;
     */
    /* JADX WARN: Code restructure failed: missing block: B:464:0x0993, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r6.title, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:466:0x09a4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.R.string.NotificationActionPinnedVideoChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:468:0x09a9, code lost:
        if (r0.isGif() == false) goto L446;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00c5, code lost:
        if (r12.getBoolean("EnablePreviewGroup", true) != false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:470:0x09b3, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L444;
     */
    /* JADX WARN: Code restructure failed: missing block: B:472:0x09d9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r6.title, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:474:0x09ea, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.R.string.NotificationActionPinnedGifChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:476:0x09f1, code lost:
        if (r0.isVoice() == false) goto L450;
     */
    /* JADX WARN: Code restructure failed: missing block: B:478:0x0a01, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.R.string.NotificationActionPinnedVoiceChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:480:0x0a06, code lost:
        if (r0.isRoundVideo() == false) goto L454;
     */
    /* JADX WARN: Code restructure failed: missing block: B:482:0x0a16, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.R.string.NotificationActionPinnedRoundChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:484:0x0a1b, code lost:
        if (r0.isSticker() != false) goto L513;
     */
    /* JADX WARN: Code restructure failed: missing block: B:486:0x0a21, code lost:
        if (r0.isAnimatedSticker() == false) goto L458;
     */
    /* JADX WARN: Code restructure failed: missing block: B:488:0x0a26, code lost:
        r3 = r0.messageOwner;
        r9 = r3.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x0a2c, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L466;
     */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x0a34, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L464;
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x0a5a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r6.title, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:0x0a6b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.R.string.NotificationActionPinnedFileChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x0a6e, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L510;
     */
    /* JADX WARN: Code restructure failed: missing block: B:499:0x0a72, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L470;
     */
    /* JADX WARN: Code restructure failed: missing block: B:502:0x0a7a, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L474;
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0a8c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:506:0x0a8f, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L478;
     */
    /* JADX WARN: Code restructure failed: missing block: B:507:0x0a91, code lost:
        r9 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x0aae, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.R.string.NotificationActionPinnedContactChannel2, r6.title, org.telegram.messenger.ContactsController.formatName(r9.first_name, r9.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x0ab1, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L486;
     */
    /* JADX WARN: Code restructure failed: missing block: B:511:0x0ab3, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r9).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:512:0x0ab9, code lost:
        if (r0.quiz == false) goto L484;
     */
    /* JADX WARN: Code restructure failed: missing block: B:514:0x0ad2, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.R.string.NotificationActionPinnedQuizChannel2, r6.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:516:0x0aea, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.R.string.NotificationActionPinnedPollChannel2, r6.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x0aed, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L494;
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x0af5, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L492;
     */
    /* JADX WARN: Code restructure failed: missing block: B:522:0x0b1b, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r6.title, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:524:0x0b2c, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.R.string.NotificationActionPinnedPhotoChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:526:0x0b31, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L498;
     */
    /* JADX WARN: Code restructure failed: missing block: B:528:0x0b41, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.R.string.NotificationActionPinnedGameChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x0b42, code lost:
        r3 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00d1, code lost:
        if (r12.getBoolean("EnablePreviewChannel", r1) == false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:530:0x0b44, code lost:
        if (r3 == null) goto L508;
     */
    /* JADX WARN: Code restructure failed: missing block: B:532:0x0b4a, code lost:
        if (r3.length() <= 0) goto L508;
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0b4c, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:534:0x0b52, code lost:
        if (r0.length() <= 20) goto L507;
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0b54, code lost:
        r3 = new java.lang.StringBuilder();
        r11 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:536:0x0b69, code lost:
        r11 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0b6a, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextChannel;
        r3 = new java.lang.Object[2];
        r3[r11] = r6.title;
        r3[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:538:0x0b7a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", r1, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:540:0x0b89, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:542:0x0b98, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:543:0x0b99, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:544:0x0b9d, code lost:
        if (r0 == null) goto L518;
     */
    /* JADX WARN: Code restructure failed: missing block: B:546:0x0bb1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiChannel, r6.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:548:0x0bc1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerChannel, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0bc2, code lost:
        r0 = r30.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:550:0x0bc6, code lost:
        if (r0 != null) goto L524;
     */
    /* JADX WARN: Code restructure failed: missing block: B:552:0x0bd4, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.R.string.NotificationActionPinnedNoTextUser, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:554:0x0bdb, code lost:
        if (r0.isMusic() == false) goto L528;
     */
    /* JADX WARN: Code restructure failed: missing block: B:556:0x0be9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicUser", org.telegram.messenger.R.string.NotificationActionPinnedMusicUser, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:558:0x0bf0, code lost:
        if (r0.isVideo() == false) goto L536;
     */
    /* JADX WARN: Code restructure failed: missing block: B:560:0x0bfa, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L534;
     */
    /* JADX WARN: Code restructure failed: missing block: B:562:0x0c1e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r5, "📹 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:564:0x0c2d, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoUser", org.telegram.messenger.R.string.NotificationActionPinnedVideoUser, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:566:0x0c32, code lost:
        if (r0.isGif() == false) goto L544;
     */
    /* JADX WARN: Code restructure failed: missing block: B:568:0x0c3c, code lost:
        if (android.text.TextUtils.isEmpty(r0.messageOwner.message) != false) goto L542;
     */
    /* JADX WARN: Code restructure failed: missing block: B:570:0x0c60, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r5, "🎬 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:572:0x0c6f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifUser", org.telegram.messenger.R.string.NotificationActionPinnedGifUser, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:574:0x0c76, code lost:
        if (r0.isVoice() == false) goto L548;
     */
    /* JADX WARN: Code restructure failed: missing block: B:576:0x0c84, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceUser", org.telegram.messenger.R.string.NotificationActionPinnedVoiceUser, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:578:0x0c89, code lost:
        if (r0.isRoundVideo() == false) goto L552;
     */
    /* JADX WARN: Code restructure failed: missing block: B:580:0x0c97, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundUser", org.telegram.messenger.R.string.NotificationActionPinnedRoundUser, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:582:0x0c9c, code lost:
        if (r0.isSticker() != false) goto L611;
     */
    /* JADX WARN: Code restructure failed: missing block: B:584:0x0ca2, code lost:
        if (r0.isAnimatedSticker() == false) goto L556;
     */
    /* JADX WARN: Code restructure failed: missing block: B:586:0x0ca8, code lost:
        r4 = r0.messageOwner;
        r9 = r4.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:587:0x0cae, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L564;
     */
    /* JADX WARN: Code restructure failed: missing block: B:589:0x0cb6, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L562;
     */
    /* JADX WARN: Code restructure failed: missing block: B:591:0x0cda, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r5, "📎 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:593:0x0ce9, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileUser", org.telegram.messenger.R.string.NotificationActionPinnedFileUser, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:595:0x0cec, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L608;
     */
    /* JADX WARN: Code restructure failed: missing block: B:597:0x0cf0, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L568;
     */
    /* JADX WARN: Code restructure failed: missing block: B:600:0x0cf8, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L572;
     */
    /* JADX WARN: Code restructure failed: missing block: B:602:0x0d08, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveUser", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveUser, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:604:0x0d0b, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L576;
     */
    /* JADX WARN: Code restructure failed: missing block: B:605:0x0d0d, code lost:
        r9 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:606:0x0d28, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactUser", org.telegram.messenger.R.string.NotificationActionPinnedContactUser, r5, org.telegram.messenger.ContactsController.formatName(r9.first_name, r9.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:608:0x0d2b, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L584;
     */
    /* JADX WARN: Code restructure failed: missing block: B:609:0x0d2d, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r9).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:610:0x0d33, code lost:
        if (r0.quiz == false) goto L582;
     */
    /* JADX WARN: Code restructure failed: missing block: B:612:0x0d4a, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizUser", org.telegram.messenger.R.string.NotificationActionPinnedQuizUser, r5, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:614:0x0d60, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollUser", org.telegram.messenger.R.string.NotificationActionPinnedPollUser, r5, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:616:0x0d63, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L592;
     */
    /* JADX WARN: Code restructure failed: missing block: B:618:0x0d6b, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L590;
     */
    /* JADX WARN: Code restructure failed: missing block: B:620:0x0d8f, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.R.string.NotificationActionPinnedTextUser, r5, "🖼 " + r0.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:622:0x0d9e, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoUser", org.telegram.messenger.R.string.NotificationActionPinnedPhotoUser, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:624:0x0da3, code lost:
        if ((r9 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L596;
     */
    /* JADX WARN: Code restructure failed: missing block: B:626:0x0db1, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameUser", org.telegram.messenger.R.string.NotificationActionPinnedGameUser, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:627:0x0db2, code lost:
        r4 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:628:0x0db4, code lost:
        if (r4 == null) goto L606;
     */
    /* JADX WARN: Code restructure failed: missing block: B:630:0x0dba, code lost:
        if (r4.length() <= 0) goto L606;
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:0x0dbc, code lost:
        r0 = r0.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:632:0x0dc2, code lost:
        if (r0.length() <= 20) goto L605;
     */
    /* JADX WARN: Code restructure failed: missing block: B:633:0x0dc4, code lost:
        r3 = new java.lang.StringBuilder();
        r4 = 0;
        r3.append((java.lang.Object) r0.subSequence(0, 20));
        r3.append("...");
        r0 = r3.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:634:0x0dd9, code lost:
        r4 = 0;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:635:0x0dda, code lost:
        r1 = org.telegram.messenger.R.string.NotificationActionPinnedTextUser;
        r2 = new java.lang.Object[2];
        r2[r4] = r5;
        r2[1] = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:636:0x0de8, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextUser", r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:638:0x0df5, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.R.string.NotificationActionPinnedNoTextUser, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:640:0x0e02, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoUser", org.telegram.messenger.R.string.NotificationActionPinnedGeoUser, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:641:0x0e03, code lost:
        r0 = r0.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:642:0x0e07, code lost:
        if (r0 == null) goto L616;
     */
    /* JADX WARN: Code restructure failed: missing block: B:644:0x0e18, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiUser", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiUser, r5, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:646:0x0e25, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerUser", org.telegram.messenger.R.string.NotificationActionPinnedStickerUser, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:648:0x0e28, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) == false) goto L634;
     */
    /* JADX WARN: Code restructure failed: missing block: B:649:0x0e2a, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) r4).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:650:0x0e32, code lost:
        if (android.text.TextUtils.isEmpty(r0) == false) goto L628;
     */
    /* JADX WARN: Code restructure failed: missing block: B:652:0x0e36, code lost:
        if (r14 != r24) goto L626;
     */
    /* JADX WARN: Code restructure failed: missing block: B:656:0x0e5a, code lost:
        if (r14 != r24) goto L632;
     */
    /* JADX WARN: Code restructure failed: missing block: B:659:0x0e78, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.R.string.ChatThemeChangedTo, r5, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:661:0x0e7b, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest) == false) goto L638;
     */
    /* JADX WARN: Code restructure failed: missing block: B:663:0x0e83, code lost:
        return r30.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:664:0x0e84, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:667:0x0e8c, code lost:
        if (r3.peer_id.channel_id == 0) goto L649;
     */
    /* JADX WARN: Code restructure failed: missing block: B:669:0x0e90, code lost:
        if (r6.megagroup != false) goto L649;
     */
    /* JADX WARN: Code restructure failed: missing block: B:671:0x0e96, code lost:
        if (r30.isVideoAvatar() == false) goto L647;
     */
    /* JADX WARN: Code restructure failed: missing block: B:673:0x0ea8, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.R.string.ChannelVideoEditNotification, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:675:0x0eb9, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.R.string.ChannelPhotoEditNotification, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:677:0x0ebf, code lost:
        if (r30.isVideoAvatar() == false) goto L653;
     */
    /* JADX WARN: Code restructure failed: missing block: B:679:0x0ed3, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.R.string.NotificationEditedGroupVideo, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:681:0x0ee6, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.R.string.NotificationEditedGroupPhoto, r5, r6.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:683:0x0eed, code lost:
        return r30.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:685:0x0efb, code lost:
        return org.telegram.messenger.LocaleController.formatString("NotificationContactJoined", org.telegram.messenger.R.string.NotificationContactJoined, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:687:0x0f00, code lost:
        if (r30.isMediaEmpty() == false) goto L668;
     */
    /* JADX WARN: Code restructure failed: missing block: B:689:0x0f0a, code lost:
        if (android.text.TextUtils.isEmpty(r30.messageOwner.message) != false) goto L666;
     */
    /* JADX WARN: Code restructure failed: missing block: B:691:0x0f10, code lost:
        return replaceSpoilers(r30);
     */
    /* JADX WARN: Code restructure failed: missing block: B:693:0x0f19, code lost:
        return org.telegram.messenger.LocaleController.getString(r27, org.telegram.messenger.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:694:0x0f1a, code lost:
        r1 = r27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:695:0x0f20, code lost:
        if (r30.type != 29) goto L707;
     */
    /* JADX WARN: Code restructure failed: missing block: B:697:0x0f28, code lost:
        if ((org.telegram.messenger.MessageObject.getMedia(r30) instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) == false) goto L707;
     */
    /* JADX WARN: Code restructure failed: missing block: B:698:0x0f2a, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) org.telegram.messenger.MessageObject.getMedia(r30);
        r1 = r0.extended_media.size();
        r2 = 0;
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:699:0x0f38, code lost:
        if (r2 >= r1) goto L706;
     */
    /* JADX WARN: Code restructure failed: missing block: B:700:0x0f3a, code lost:
        r4 = r0.extended_media.get(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:701:0x0f44, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageExtendedMedia) == false) goto L701;
     */
    /* JADX WARN: Code restructure failed: missing block: B:702:0x0f46, code lost:
        r3 = ((org.telegram.tgnet.TLRPC$TL_messageExtendedMedia) r4).media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:703:0x0f4c, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L699;
     */
    /* JADX WARN: Code restructure failed: missing block: B:705:0x0f54, code lost:
        if (org.telegram.messenger.MessageObject.isVideoDocument(r3.document) == false) goto L699;
     */
    /* JADX WARN: Code restructure failed: missing block: B:709:0x0f5c, code lost:
        if ((r4 instanceof org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview) == false) goto L705;
     */
    /* JADX WARN: Code restructure failed: missing block: B:711:0x0f64, code lost:
        if ((((org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview) r4).flags & 4) == 0) goto L700;
     */
    /* JADX WARN: Code restructure failed: missing block: B:712:0x0f66, code lost:
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:713:0x0f68, code lost:
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:715:0x0f6b, code lost:
        if (r3 == false) goto L683;
     */
    /* JADX WARN: Code restructure failed: missing block: B:717:0x0f6f, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:718:0x0f72, code lost:
        r0 = org.telegram.messenger.R.string.AttachPaidMedia;
     */
    /* JADX WARN: Code restructure failed: missing block: B:719:0x0f74, code lost:
        if (r1 != 1) goto L694;
     */
    /* JADX WARN: Code restructure failed: missing block: B:720:0x0f76, code lost:
        if (r3 == false) goto L693;
     */
    /* JADX WARN: Code restructure failed: missing block: B:721:0x0f78, code lost:
        r1 = org.telegram.messenger.R.string.AttachVideo;
     */
    /* JADX WARN: Code restructure failed: missing block: B:722:0x0f7b, code lost:
        r1 = org.telegram.messenger.R.string.AttachPhoto;
     */
    /* JADX WARN: Code restructure failed: missing block: B:723:0x0f7d, code lost:
        r1 = org.telegram.messenger.LocaleController.getString(r1);
        r2 = 1;
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:724:0x0f84, code lost:
        if (r3 == false) goto L698;
     */
    /* JADX WARN: Code restructure failed: missing block: B:725:0x0f86, code lost:
        r2 = "Media";
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x0f88, code lost:
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:727:0x0f8a, code lost:
        r2 = "Photos";
     */
    /* JADX WARN: Code restructure failed: missing block: B:728:0x0f8d, code lost:
        r1 = org.telegram.messenger.LocaleController.formatPluralString(r2, r1, new java.lang.Object[0]);
        r2 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:729:0x0f94, code lost:
        r2 = new java.lang.Object[r2];
        r2[r3] = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:730:0x0f9c, code lost:
        return org.telegram.messenger.LocaleController.formatString(r0, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:732:0x0fa1, code lost:
        if (r30.isVoiceOnce() == false) goto L711;
     */
    /* JADX WARN: Code restructure failed: missing block: B:734:0x0fa9, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachOnceAudio);
     */
    /* JADX WARN: Code restructure failed: missing block: B:736:0x0fae, code lost:
        if (r30.isRoundOnce() == false) goto L715;
     */
    /* JADX WARN: Code restructure failed: missing block: B:738:0x0fb6, code lost:
        return org.telegram.messenger.LocaleController.getString(org.telegram.messenger.R.string.AttachOnceRound);
     */
    /* JADX WARN: Code restructure failed: missing block: B:739:0x0fb7, code lost:
        r2 = r30.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:740:0x0fbd, code lost:
        if ((r2.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L727;
     */
    /* JADX WARN: Code restructure failed: missing block: B:742:0x0fc5, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L721;
     */
    /* JADX WARN: Code restructure failed: missing block: B:744:0x0fda, code lost:
        return "🖼 " + replaceSpoilers(r30);
     */
    /* JADX WARN: Code restructure failed: missing block: B:746:0x0fe1, code lost:
        if (r30.messageOwner.media.ttl_seconds == 0) goto L725;
     */
    /* JADX WARN: Code restructure failed: missing block: B:748:0x0feb, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDestructingPhoto", org.telegram.messenger.R.string.AttachDestructingPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:750:0x0ff4, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachPhoto", org.telegram.messenger.R.string.AttachPhoto);
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x0ff9, code lost:
        if (r30.isVideo() == false) goto L739;
     */
    /* JADX WARN: Code restructure failed: missing block: B:754:0x1003, code lost:
        if (android.text.TextUtils.isEmpty(r30.messageOwner.message) != false) goto L733;
     */
    /* JADX WARN: Code restructure failed: missing block: B:756:0x1018, code lost:
        return "📹 " + replaceSpoilers(r30);
     */
    /* JADX WARN: Code restructure failed: missing block: B:758:0x101f, code lost:
        if (r30.messageOwner.media.ttl_seconds == 0) goto L737;
     */
    /* JADX WARN: Code restructure failed: missing block: B:760:0x1029, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDestructingVideo", org.telegram.messenger.R.string.AttachDestructingVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:762:0x1032, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachVideo", org.telegram.messenger.R.string.AttachVideo);
     */
    /* JADX WARN: Code restructure failed: missing block: B:764:0x1037, code lost:
        if (r30.isGame() == false) goto L743;
     */
    /* JADX WARN: Code restructure failed: missing block: B:766:0x1041, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachGame", org.telegram.messenger.R.string.AttachGame);
     */
    /* JADX WARN: Code restructure failed: missing block: B:768:0x1046, code lost:
        if (r30.isVoice() == false) goto L747;
     */
    /* JADX WARN: Code restructure failed: missing block: B:770:0x1050, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachAudio", org.telegram.messenger.R.string.AttachAudio);
     */
    /* JADX WARN: Code restructure failed: missing block: B:772:0x1055, code lost:
        if (r30.isRoundVideo() == false) goto L751;
     */
    /* JADX WARN: Code restructure failed: missing block: B:774:0x105f, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachRound", org.telegram.messenger.R.string.AttachRound);
     */
    /* JADX WARN: Code restructure failed: missing block: B:776:0x1064, code lost:
        if (r30.isMusic() == false) goto L755;
     */
    /* JADX WARN: Code restructure failed: missing block: B:778:0x106e, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachMusic", org.telegram.messenger.R.string.AttachMusic);
     */
    /* JADX WARN: Code restructure failed: missing block: B:779:0x106f, code lost:
        r2 = r30.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:780:0x1075, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L759;
     */
    /* JADX WARN: Code restructure failed: missing block: B:782:0x107f, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachContact", org.telegram.messenger.R.string.AttachContact);
     */
    /* JADX WARN: Code restructure failed: missing block: B:784:0x1082, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L767;
     */
    /* JADX WARN: Code restructure failed: missing block: B:786:0x108a, code lost:
        if (((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r2).poll.quiz == false) goto L765;
     */
    /* JADX WARN: Code restructure failed: missing block: B:788:0x1094, code lost:
        return org.telegram.messenger.LocaleController.getString("QuizPoll", org.telegram.messenger.R.string.QuizPoll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:790:0x109d, code lost:
        return org.telegram.messenger.LocaleController.getString("Poll", org.telegram.messenger.R.string.Poll);
     */
    /* JADX WARN: Code restructure failed: missing block: B:792:0x10a0, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) == false) goto L771;
     */
    /* JADX WARN: Code restructure failed: missing block: B:794:0x10aa, code lost:
        return org.telegram.messenger.LocaleController.getString("BoostingGiveaway", org.telegram.messenger.R.string.BoostingGiveaway);
     */
    /* JADX WARN: Code restructure failed: missing block: B:796:0x10ad, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveawayResults) == false) goto L775;
     */
    /* JADX WARN: Code restructure failed: missing block: B:798:0x10b7, code lost:
        return org.telegram.messenger.LocaleController.getString("BoostingGiveawayResults", org.telegram.messenger.R.string.BoostingGiveawayResults);
     */
    /* JADX WARN: Code restructure failed: missing block: B:800:0x10ba, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L826;
     */
    /* JADX WARN: Code restructure failed: missing block: B:802:0x10be, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L779;
     */
    /* JADX WARN: Code restructure failed: missing block: B:805:0x10c4, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L783;
     */
    /* JADX WARN: Code restructure failed: missing block: B:807:0x10ce, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachLiveLocation", org.telegram.messenger.R.string.AttachLiveLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:809:0x10d1, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L809;
     */
    /* JADX WARN: Code restructure failed: missing block: B:811:0x10d7, code lost:
        if (r30.isSticker() != false) goto L803;
     */
    /* JADX WARN: Code restructure failed: missing block: B:813:0x10dd, code lost:
        if (r30.isAnimatedSticker() == false) goto L789;
     */
    /* JADX WARN: Code restructure failed: missing block: B:816:0x10e4, code lost:
        if (r30.isGif() == false) goto L797;
     */
    /* JADX WARN: Code restructure failed: missing block: B:818:0x10ee, code lost:
        if (android.text.TextUtils.isEmpty(r30.messageOwner.message) != false) goto L795;
     */
    /* JADX WARN: Code restructure failed: missing block: B:820:0x1103, code lost:
        return "🎬 " + replaceSpoilers(r30);
     */
    /* JADX WARN: Code restructure failed: missing block: B:822:0x110c, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachGif", org.telegram.messenger.R.string.AttachGif);
     */
    /* JADX WARN: Code restructure failed: missing block: B:824:0x1115, code lost:
        if (android.text.TextUtils.isEmpty(r30.messageOwner.message) != false) goto L801;
     */
    /* JADX WARN: Code restructure failed: missing block: B:826:0x112a, code lost:
        return "📎 " + replaceSpoilers(r30);
     */
    /* JADX WARN: Code restructure failed: missing block: B:828:0x1133, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachDocument", org.telegram.messenger.R.string.AttachDocument);
     */
    /* JADX WARN: Code restructure failed: missing block: B:829:0x1134, code lost:
        r0 = r30.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:830:0x1138, code lost:
        if (r0 == null) goto L807;
     */
    /* JADX WARN: Code restructure failed: missing block: B:832:0x1156, code lost:
        return r0 + " " + org.telegram.messenger.LocaleController.getString("AttachSticker", org.telegram.messenger.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:834:0x115f, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachSticker", org.telegram.messenger.R.string.AttachSticker);
     */
    /* JADX WARN: Code restructure failed: missing block: B:836:0x1162, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaStory) == false) goto L820;
     */
    /* JADX WARN: Code restructure failed: missing block: B:838:0x1168, code lost:
        if (((org.telegram.tgnet.TLRPC$TL_messageMediaStory) r2).via_mention == false) goto L818;
     */
    /* JADX WARN: Code restructure failed: missing block: B:839:0x116a, code lost:
        r0 = org.telegram.messenger.R.string.StoryNotificationMention;
        r2 = r31[0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:840:0x116f, code lost:
        if (r2 != null) goto L816;
     */
    /* JADX WARN: Code restructure failed: missing block: B:841:0x1171, code lost:
        r2 = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:843:0x117e, code lost:
        return org.telegram.messenger.LocaleController.formatString("StoryNotificationMention", r0, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:845:0x1187, code lost:
        return org.telegram.messenger.LocaleController.getString("Story", org.telegram.messenger.R.string.Story);
     */
    /* JADX WARN: Code restructure failed: missing block: B:847:0x118e, code lost:
        if (android.text.TextUtils.isEmpty(r30.messageText) != false) goto L824;
     */
    /* JADX WARN: Code restructure failed: missing block: B:849:0x1194, code lost:
        return replaceSpoilers(r30);
     */
    /* JADX WARN: Code restructure failed: missing block: B:851:0x119b, code lost:
        return org.telegram.messenger.LocaleController.getString(r1, org.telegram.messenger.R.string.Message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:853:0x11a4, code lost:
        return org.telegram.messenger.LocaleController.getString("AttachLocation", org.telegram.messenger.R.string.AttachLocation);
     */
    /* JADX WARN: Code restructure failed: missing block: B:869:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:870:?, code lost:
        return org.telegram.messenger.LocaleController.formatString("ChatThemeDisabled", org.telegram.messenger.R.string.ChatThemeDisabled, r5, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:871:?, code lost:
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
                String userName = UserObject.getUserName(user);
                if (j2 != 0) {
                    strArr[0] = userName;
                    str = "Message";
                    str2 = "EnablePreviewChannel";
                } else {
                    str = "Message";
                    str2 = "EnablePreviewChannel";
                    if (Build.VERSION.SDK_INT > 27) {
                        strArr[0] = userName;
                    } else {
                        strArr[0] = null;
                    }
                }
                str3 = userName;
            } else {
                str = "Message";
                str2 = "EnablePreviewChannel";
                str3 = null;
            }
        } else {
            str = "Message";
            str2 = "EnablePreviewChannel";
            TLRPC$Chat chat2 = getMessagesController().getChat(Long.valueOf(-j3));
            if (chat2 != null) {
                str3 = chat2.title;
                strArr[0] = str3;
            }
            str3 = null;
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
        String str4 = str;
        if (zArr != null) {
            zArr[0] = false;
        }
        return LocaleController.getString(str4, R.string.Message);
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

    /* JADX WARN: Code restructure failed: missing block: B:274:0x06a4, code lost:
        if (r13.getBoolean("EnablePreviewGroup", true) == false) goto L756;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x06b0, code lost:
        if (r13.getBoolean("EnablePreviewChannel", r12) != false) goto L257;
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x06b2, code lost:
        r2 = r49.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x06b6, code lost:
        if ((r2 instanceof org.telegram.tgnet.TLRPC$TL_messageService) == false) goto L571;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x06b8, code lost:
        r5 = r2.action;
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x06bc, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser) == false) goto L300;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x06be, code lost:
        r7 = r5.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x06c2, code lost:
        if (r7 != 0) goto L266;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x06cb, code lost:
        if (r5.users.size() != 1) goto L266;
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x06cd, code lost:
        r7 = r49.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x06e0, code lost:
        if (r7 == 0) goto L286;
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x06ea, code lost:
        if (r49.messageOwner.peer_id.channel_id == 0) goto L273;
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x06ee, code lost:
        if (r1.megagroup != false) goto L273;
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x06f0, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelAddedByNotification", org.telegram.messenger.R.string.ChannelAddedByNotification, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x0707, code lost:
        if (r7 != r35) goto L276;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x0709, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.R.string.NotificationInvitedToGroup, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x071e, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r7));
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x072a, code lost:
        if (r0 != null) goto L279;
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x072c, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x0732, code lost:
        if (r3 != r0.id) goto L285;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x0736, code lost:
        if (r1.megagroup == false) goto L284;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x0738, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.R.string.NotificationGroupAddSelfMega, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x074d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.R.string.NotificationGroupAddSelf, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x0762, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r6, r1.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x077e, code lost:
        r2 = new java.lang.StringBuilder();
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x0790, code lost:
        if (r4 >= r49.messageOwner.action.users.size()) goto L298;
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x0792, code lost:
        r5 = getMessagesController().getUser(r49.messageOwner.action.users.get(r4));
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x07a6, code lost:
        if (r5 == null) goto L297;
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x07a8, code lost:
        r5 = org.telegram.messenger.UserObject.getUserName(r5);
        r12 = r29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x07b2, code lost:
        if (r2.length() == 0) goto L294;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x07b4, code lost:
        r2.append(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x07b7, code lost:
        r2.append(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x07bc, code lost:
        r12 = r29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x07bf, code lost:
        r4 = r4 + 1;
        r29 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x07c3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.R.string.NotificationGroupAddMember, r6, r1.title, r2.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x07e4, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCall) == false) goto L303;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x07e6, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.R.string.NotificationGroupCreatedCall, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x07fc, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGroupCallScheduled) == false) goto L306;
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x07fe, code lost:
        r0 = r49.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x0808, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionInviteToGroupCall) == false) goto L336;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x080a, code lost:
        r2 = r5.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x080e, code lost:
        if (r2 != 0) goto L313;
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x0817, code lost:
        if (r5.users.size() != 1) goto L313;
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x0819, code lost:
        r2 = r49.messageOwner.action.users.get(0).longValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x082c, code lost:
        if (r2 == 0) goto L322;
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x0830, code lost:
        if (r2 != r35) goto L318;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x0832, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.R.string.NotificationGroupInvitedYouToCall, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x0847, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x0853, code lost:
        if (r0 != null) goto L321;
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x0855, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x0857, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r6, r1.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x0873, code lost:
        r2 = new java.lang.StringBuilder();
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x0885, code lost:
        if (r3 >= r49.messageOwner.action.users.size()) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x0887, code lost:
        r5 = getMessagesController().getUser(r49.messageOwner.action.users.get(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x089b, code lost:
        if (r5 == null) goto L333;
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x089d, code lost:
        r5 = org.telegram.messenger.UserObject.getUserName(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x08a5, code lost:
        if (r2.length() == 0) goto L330;
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x08a7, code lost:
        r2.append(r29);
     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x08aa, code lost:
        r2.append(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x08ad, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x08b0, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.R.string.NotificationGroupInvitedToCall, r6, r1.title, r2.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x08cc, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGiftCode) == false) goto L345;
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x08ce, code lost:
        r5 = (org.telegram.tgnet.TLRPC$TL_messageActionGiftCode) r5;
        r0 = org.telegram.messenger.MessagesController.getInstance(r48.currentAccount).getChat(java.lang.Long.valueOf(-org.telegram.messenger.DialogObject.getPeerDialogId(r5.boost_peer)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:360:0x08e7, code lost:
        if (r0 != null) goto L344;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x08e9, code lost:
        r6 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:362:0x08eb, code lost:
        r6 = r0.title;
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x08ed, code lost:
        if (r6 != null) goto L343;
     */
    /* JADX WARN: Code restructure failed: missing block: B:364:0x08ef, code lost:
        r0 = org.telegram.messenger.LocaleController.getString("BoostingReceivedGiftNoName", org.telegram.messenger.R.string.BoostingReceivedGiftNoName);
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x08f9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGiftCode", org.telegram.messenger.R.string.NotificationMessageGiftCode, r6, org.telegram.messenger.LocaleController.formatPluralString("Months", r5.months, new java.lang.Object[0]));
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x091d, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink) == false) goto L348;
     */
    /* JADX WARN: Code restructure failed: missing block: B:368:0x091f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationInvitedToGroupByLink", org.telegram.messenger.R.string.NotificationInvitedToGroupByLink, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:370:0x0938, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditTitle) == false) goto L351;
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x093a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.R.string.NotificationEditedGroupName, r6, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x094f, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto) != false) goto L559;
     */
    /* JADX WARN: Code restructure failed: missing block: B:375:0x0953, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto) == false) goto L355;
     */
    /* JADX WARN: Code restructure failed: missing block: B:378:0x0959, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser) == false) goto L367;
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x095b, code lost:
        r7 = r5.user_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:380:0x095f, code lost:
        if (r7 != r35) goto L360;
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x0961, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.R.string.NotificationGroupKickYou, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x097b, code lost:
        if (r7 != r3) goto L363;
     */
    /* JADX WARN: Code restructure failed: missing block: B:384:0x097d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.R.string.NotificationGroupLeftMember, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:385:0x098f, code lost:
        r0 = getMessagesController().getUser(java.lang.Long.valueOf(r49.messageOwner.action.user_id));
     */
    /* JADX WARN: Code restructure failed: missing block: B:386:0x09a1, code lost:
        if (r0 != null) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:387:0x09a3, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x09a5, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.R.string.NotificationGroupKickMember, r6, r1.title, org.telegram.messenger.UserObject.getUserName(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x09c4, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatCreate) == false) goto L370;
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x09c6, code lost:
        r0 = r49.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:394:0x09d0, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelCreate) == false) goto L373;
     */
    /* JADX WARN: Code restructure failed: missing block: B:395:0x09d2, code lost:
        r0 = r49.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:397:0x09dc, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo) == false) goto L376;
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:0x09de, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x09f6, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom) == false) goto L379;
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x09f8, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ActionMigrateFromGroupNotify", org.telegram.messenger.R.string.ActionMigrateFromGroupNotify, r5.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:403:0x0a08, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionScreenshotTaken) == false) goto L382;
     */
    /* JADX WARN: Code restructure failed: missing block: B:404:0x0a0a, code lost:
        r0 = r49.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:406:0x0a14, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionPinMessage) == false) goto L541;
     */
    /* JADX WARN: Code restructure failed: missing block: B:408:0x0a1c, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r1) == false) goto L464;
     */
    /* JADX WARN: Code restructure failed: missing block: B:410:0x0a20, code lost:
        if (r1.megagroup == false) goto L388;
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x0a27, code lost:
        r2 = r49.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x0a29, code lost:
        if (r2 != null) goto L391;
     */
    /* JADX WARN: Code restructure failed: missing block: B:414:0x0a2b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:416:0x0a45, code lost:
        if (r2.isMusic() == false) goto L394;
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x0a47, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusicChannel", org.telegram.messenger.R.string.NotificationActionPinnedMusicChannel, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x0a5d, code lost:
        if (r2.isVideo() == false) goto L400;
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x0a67, code lost:
        if (android.text.TextUtils.isEmpty(r2.messageOwner.message) != false) goto L399;
     */
    /* JADX WARN: Code restructure failed: missing block: B:422:0x0a69, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r1.title, "📹 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x0a8f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.R.string.NotificationActionPinnedVideoChannel, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x0aa5, code lost:
        if (r2.isGif() == false) goto L406;
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x0aaf, code lost:
        if (android.text.TextUtils.isEmpty(r2.messageOwner.message) != false) goto L405;
     */
    /* JADX WARN: Code restructure failed: missing block: B:428:0x0ab1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r1.title, "🎬 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x0ad7, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.R.string.NotificationActionPinnedGifChannel, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x0aef, code lost:
        if (r2.isVoice() == false) goto L409;
     */
    /* JADX WARN: Code restructure failed: missing block: B:432:0x0af1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.R.string.NotificationActionPinnedVoiceChannel, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:434:0x0b05, code lost:
        if (r2.isRoundVideo() == false) goto L412;
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x0b07, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.R.string.NotificationActionPinnedRoundChannel, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x0b1b, code lost:
        if (r2.isSticker() != false) goto L459;
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x0b21, code lost:
        if (r2.isAnimatedSticker() == false) goto L416;
     */
    /* JADX WARN: Code restructure failed: missing block: B:441:0x0b26, code lost:
        r4 = r2.messageOwner;
        r8 = r4.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:442:0x0b2c, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L422;
     */
    /* JADX WARN: Code restructure failed: missing block: B:444:0x0b34, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L421;
     */
    /* JADX WARN: Code restructure failed: missing block: B:445:0x0b36, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r1.title, "📎 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:446:0x0b5c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.R.string.NotificationActionPinnedFileChannel, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:448:0x0b70, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L457;
     */
    /* JADX WARN: Code restructure failed: missing block: B:450:0x0b74, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L426;
     */
    /* JADX WARN: Code restructure failed: missing block: B:453:0x0b7c, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L429;
     */
    /* JADX WARN: Code restructure failed: missing block: B:454:0x0b7e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoLiveChannel, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:456:0x0b92, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L432;
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:0x0b94, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r49.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.R.string.NotificationActionPinnedContactChannel2, r1.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x0bb9, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L438;
     */
    /* JADX WARN: Code restructure failed: missing block: B:460:0x0bbb, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r8).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x0bc1, code lost:
        if (r0.quiz == false) goto L437;
     */
    /* JADX WARN: Code restructure failed: missing block: B:462:0x0bc3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.R.string.NotificationActionPinnedQuizChannel2, r1.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0bdc, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.R.string.NotificationActionPinnedPollChannel2, r1.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x0bf7, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L444;
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x0bff, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L443;
     */
    /* JADX WARN: Code restructure failed: missing block: B:468:0x0c01, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedTextChannel, r1.title, "🖼 " + r2.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x0c27, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.R.string.NotificationActionPinnedPhotoChannel, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x0c3d, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L447;
     */
    /* JADX WARN: Code restructure failed: missing block: B:472:0x0c3f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.R.string.NotificationActionPinnedGameChannel, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x0c4f, code lost:
        r0 = r2.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:474:0x0c51, code lost:
        if (r0 == null) goto L456;
     */
    /* JADX WARN: Code restructure failed: missing block: B:476:0x0c57, code lost:
        if (r0.length() <= 0) goto L456;
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x0c59, code lost:
        r0 = r2.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:478:0x0c5f, code lost:
        if (r0.length() <= 20) goto L455;
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x0c61, code lost:
        r2 = new java.lang.StringBuilder();
        r4 = 0;
        r2.append((java.lang.Object) r0.subSequence(0, 20));
        r2.append("...");
        r0 = r2.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:480:0x0c78, code lost:
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x0c79, code lost:
        r2 = org.telegram.messenger.R.string.NotificationActionPinnedTextChannel;
        r3 = new java.lang.Object[2];
        r3[r4] = r1.title;
        r3[1] = r0;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedTextChannel", r2, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:482:0x0c8b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.R.string.NotificationActionPinnedNoTextChannel, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0c9b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.R.string.NotificationActionPinnedGeoChannel, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:484:0x0cab, code lost:
        r0 = r2.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0caf, code lost:
        if (r0 == null) goto L463;
     */
    /* JADX WARN: Code restructure failed: missing block: B:486:0x0cb1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmojiChannel, r1.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:487:0x0cc5, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.R.string.NotificationActionPinnedStickerChannel, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:488:0x0cd6, code lost:
        r2 = r49.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x0cd8, code lost:
        if (r2 != null) goto L468;
     */
    /* JADX WARN: Code restructure failed: missing block: B:490:0x0cda, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x0cf6, code lost:
        if (r2.isMusic() == false) goto L471;
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x0cf8, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedMusic", org.telegram.messenger.R.string.NotificationActionPinnedMusic, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:0x0d10, code lost:
        if (r2.isVideo() == false) goto L477;
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x0d1a, code lost:
        if (android.text.TextUtils.isEmpty(r2.messageOwner.message) != false) goto L476;
     */
    /* JADX WARN: Code restructure failed: missing block: B:498:0x0d1c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r6, "📹 " + r2.messageOwner.message, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:499:0x0d45, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.R.string.NotificationActionPinnedVideo, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:501:0x0d5e, code lost:
        if (r2.isGif() == false) goto L483;
     */
    /* JADX WARN: Code restructure failed: missing block: B:503:0x0d68, code lost:
        if (android.text.TextUtils.isEmpty(r2.messageOwner.message) != false) goto L482;
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0d6a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r6, "🎬 " + r2.messageOwner.message, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:505:0x0d93, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.R.string.NotificationActionPinnedGif, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:507:0x0daf, code lost:
        if (r2.isVoice() == false) goto L486;
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x0db1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.R.string.NotificationActionPinnedVoice, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x0dc7, code lost:
        if (r2.isRoundVideo() == false) goto L489;
     */
    /* JADX WARN: Code restructure failed: missing block: B:511:0x0dc9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.R.string.NotificationActionPinnedRound, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x0ddf, code lost:
        if (r2.isSticker() != false) goto L536;
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x0de5, code lost:
        if (r2.isAnimatedSticker() == false) goto L493;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0deb, code lost:
        r4 = r2.messageOwner;
        r8 = r4.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x0df1, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L499;
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x0df9, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L498;
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x0dfb, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r6, "📎 " + r2.messageOwner.message, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:522:0x0e24, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.R.string.NotificationActionPinnedFile, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:524:0x0e3b, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L534;
     */
    /* JADX WARN: Code restructure failed: missing block: B:526:0x0e3f, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L503;
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x0e48, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L506;
     */
    /* JADX WARN: Code restructure failed: missing block: B:530:0x0e4a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.R.string.NotificationActionPinnedGeoLive, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:532:0x0e61, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L509;
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0e63, code lost:
        r0 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r49.messageOwner.media;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.R.string.NotificationActionPinnedContact2, r6, r1.title, org.telegram.messenger.ContactsController.formatName(r0.first_name, r0.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0e8b, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L515;
     */
    /* JADX WARN: Code restructure failed: missing block: B:536:0x0e8d, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r8).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0e93, code lost:
        if (r0.quiz == false) goto L514;
     */
    /* JADX WARN: Code restructure failed: missing block: B:538:0x0e95, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.R.string.NotificationActionPinnedQuiz2, r6, r1.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0eb1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.R.string.NotificationActionPinnedPoll2, r6, r1.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x0ecf, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L521;
     */
    /* JADX WARN: Code restructure failed: missing block: B:543:0x0ed7, code lost:
        if (android.text.TextUtils.isEmpty(r4.message) != false) goto L520;
     */
    /* JADX WARN: Code restructure failed: missing block: B:544:0x0ed9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.R.string.NotificationActionPinnedText, r6, "🖼 " + r2.messageOwner.message, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x0f02, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.R.string.NotificationActionPinnedPhoto, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0f1c, code lost:
        if ((r8 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L524;
     */
    /* JADX WARN: Code restructure failed: missing block: B:548:0x0f1e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.R.string.NotificationActionPinnedGame, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0f30, code lost:
        r0 = r2.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:550:0x0f32, code lost:
        if (r0 == null) goto L533;
     */
    /* JADX WARN: Code restructure failed: missing block: B:552:0x0f38, code lost:
        if (r0.length() <= 0) goto L533;
     */
    /* JADX WARN: Code restructure failed: missing block: B:553:0x0f3a, code lost:
        r0 = r2.messageText;
     */
    /* JADX WARN: Code restructure failed: missing block: B:554:0x0f40, code lost:
        if (r0.length() <= 20) goto L532;
     */
    /* JADX WARN: Code restructure failed: missing block: B:555:0x0f42, code lost:
        r2 = new java.lang.StringBuilder();
        r4 = 0;
        r2.append((java.lang.Object) r0.subSequence(0, 20));
        r2.append("...");
        r0 = r2.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:556:0x0f57, code lost:
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:557:0x0f58, code lost:
        r2 = org.telegram.messenger.R.string.NotificationActionPinnedText;
        r1 = r1.title;
        r3 = new java.lang.Object[3];
        r3[r4] = r6;
        r3[1] = r0;
        r3[2] = r1;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedText", r2, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:558:0x0f6d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.R.string.NotificationActionPinnedNoText, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:559:0x0f80, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.R.string.NotificationActionPinnedGeo, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:560:0x0f92, code lost:
        r0 = r2.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0f96, code lost:
        if (r0 == null) goto L540;
     */
    /* JADX WARN: Code restructure failed: missing block: B:562:0x0f98, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.R.string.NotificationActionPinnedStickerEmoji, r6, r1.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:563:0x0fae, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.R.string.NotificationActionPinnedSticker, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:565:0x0fc3, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionGameScore) == false) goto L544;
     */
    /* JADX WARN: Code restructure failed: missing block: B:566:0x0fc5, code lost:
        r0 = r49.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:568:0x0fcf, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) == false) goto L556;
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x0fd1, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme) r5).emoticon;
     */
    /* JADX WARN: Code restructure failed: missing block: B:570:0x0fd9, code lost:
        if (android.text.TextUtils.isEmpty(r0) == false) goto L552;
     */
    /* JADX WARN: Code restructure failed: missing block: B:572:0x0fdd, code lost:
        if (r14 != r35) goto L551;
     */
    /* JADX WARN: Code restructure failed: missing block: B:573:0x0fdf, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChatThemeDisabledYou", org.telegram.messenger.R.string.ChatThemeDisabledYou, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:574:0x0fea, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString(r33, org.telegram.messenger.R.string.ChatThemeDisabled, r6, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:576:0x1001, code lost:
        if (r14 != r35) goto L555;
     */
    /* JADX WARN: Code restructure failed: missing block: B:577:0x1003, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeYou", org.telegram.messenger.R.string.ChatThemeChangedYou, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:578:0x1011, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChangedChatThemeTo", org.telegram.messenger.R.string.ChatThemeChangedTo, r6, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:580:0x1024, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByRequest) == false) goto L145;
     */
    /* JADX WARN: Code restructure failed: missing block: B:581:0x1026, code lost:
        r0 = r49.messageText.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:583:0x1034, code lost:
        if (r2.peer_id.channel_id == 0) goto L567;
     */
    /* JADX WARN: Code restructure failed: missing block: B:585:0x1038, code lost:
        if (r1.megagroup != false) goto L567;
     */
    /* JADX WARN: Code restructure failed: missing block: B:587:0x103e, code lost:
        if (r49.isVideoAvatar() == false) goto L566;
     */
    /* JADX WARN: Code restructure failed: missing block: B:588:0x1040, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelVideoEditNotification", org.telegram.messenger.R.string.ChannelVideoEditNotification, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:589:0x1052, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelPhotoEditNotification", org.telegram.messenger.R.string.ChannelPhotoEditNotification, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:591:0x1069, code lost:
        if (r49.isVideoAvatar() == false) goto L570;
     */
    /* JADX WARN: Code restructure failed: missing block: B:592:0x106b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupVideo", org.telegram.messenger.R.string.NotificationEditedGroupVideo, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:593:0x107f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.R.string.NotificationEditedGroupPhoto, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:595:0x1099, code lost:
        if (org.telegram.messenger.ChatObject.isChannel(r1) == false) goto L666;
     */
    /* JADX WARN: Code restructure failed: missing block: B:597:0x109d, code lost:
        if (r1.megagroup != false) goto L666;
     */
    /* JADX WARN: Code restructure failed: missing block: B:599:0x10a3, code lost:
        if (r49.isMediaEmpty() == false) goto L583;
     */
    /* JADX WARN: Code restructure failed: missing block: B:600:0x10a5, code lost:
        if (r50 != false) goto L582;
     */
    /* JADX WARN: Code restructure failed: missing block: B:602:0x10af, code lost:
        if (android.text.TextUtils.isEmpty(r49.messageOwner.message) != false) goto L582;
     */
    /* JADX WARN: Code restructure failed: missing block: B:603:0x10b1, code lost:
        r6 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r6, r49.messageOwner.message);
        r51[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:604:0x10ca, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString(r39, org.telegram.messenger.R.string.ChannelMessageNoText, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:605:0x10da, code lost:
        r2 = r39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:606:0x10e2, code lost:
        if (r49.type != 29) goto L588;
     */
    /* JADX WARN: Code restructure failed: missing block: B:608:0x10ea, code lost:
        if ((org.telegram.messenger.MessageObject.getMedia(r49) instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) == false) goto L588;
     */
    /* JADX WARN: Code restructure failed: missing block: B:609:0x10ec, code lost:
        r0 = org.telegram.messenger.LocaleController.formatPluralString("NotificationChannelMessagePaidMedia", (int) ((org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) org.telegram.messenger.MessageObject.getMedia(r49)).stars_amount, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:610:0x1105, code lost:
        r3 = r49.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:611:0x110b, code lost:
        if ((r3.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L596;
     */
    /* JADX WARN: Code restructure failed: missing block: B:612:0x110d, code lost:
        if (r50 != false) goto L595;
     */
    /* JADX WARN: Code restructure failed: missing block: B:614:0x1115, code lost:
        if (android.text.TextUtils.isEmpty(r3.message) != false) goto L595;
     */
    /* JADX WARN: Code restructure failed: missing block: B:615:0x1117, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r6 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r6, "🖼 " + r49.messageOwner.message);
        r51[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:616:0x113d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessagePhoto", org.telegram.messenger.R.string.ChannelMessagePhoto, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:618:0x1151, code lost:
        if (r49.isVideo() == false) goto L604;
     */
    /* JADX WARN: Code restructure failed: missing block: B:619:0x1153, code lost:
        if (r50 != false) goto L603;
     */
    /* JADX WARN: Code restructure failed: missing block: B:621:0x115d, code lost:
        if (android.text.TextUtils.isEmpty(r49.messageOwner.message) != false) goto L603;
     */
    /* JADX WARN: Code restructure failed: missing block: B:622:0x115f, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r6 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r6, "📹 " + r49.messageOwner.message);
        r51[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:623:0x1185, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageVideo", org.telegram.messenger.R.string.ChannelMessageVideo, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x119b, code lost:
        if (r49.isVoice() == false) goto L607;
     */
    /* JADX WARN: Code restructure failed: missing block: B:626:0x119d, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageAudio", org.telegram.messenger.R.string.ChannelMessageAudio, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:628:0x11af, code lost:
        if (r49.isRoundVideo() == false) goto L610;
     */
    /* JADX WARN: Code restructure failed: missing block: B:629:0x11b1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageRound", org.telegram.messenger.R.string.ChannelMessageRound, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:631:0x11c3, code lost:
        if (r49.isMusic() == false) goto L613;
     */
    /* JADX WARN: Code restructure failed: missing block: B:632:0x11c5, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageMusic", org.telegram.messenger.R.string.ChannelMessageMusic, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:633:0x11d3, code lost:
        r3 = r49.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:634:0x11d9, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L616;
     */
    /* JADX WARN: Code restructure failed: missing block: B:635:0x11db, code lost:
        r3 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r3;
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageContact2", org.telegram.messenger.R.string.ChannelMessageContact2, r6, org.telegram.messenger.ContactsController.formatName(r3.first_name, r3.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:637:0x11fa, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L622;
     */
    /* JADX WARN: Code restructure failed: missing block: B:638:0x11fc, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r3).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:639:0x1202, code lost:
        if (r0.quiz == false) goto L621;
     */
    /* JADX WARN: Code restructure failed: missing block: B:640:0x1204, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageQuiz2", org.telegram.messenger.R.string.ChannelMessageQuiz2, r6, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:641:0x121b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessagePoll2", org.telegram.messenger.R.string.ChannelMessagePoll2, r6, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:643:0x1234, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) == false) goto L625;
     */
    /* JADX WARN: Code restructure failed: missing block: B:644:0x1236, code lost:
        r3 = (org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) r3;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageChannelGiveaway", org.telegram.messenger.R.string.NotificationMessageChannelGiveaway, r1.title, java.lang.Integer.valueOf(r3.quantity), java.lang.Integer.valueOf(r3.months));
     */
    /* JADX WARN: Code restructure failed: missing block: B:646:0x125e, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L664;
     */
    /* JADX WARN: Code restructure failed: missing block: B:648:0x1262, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L629;
     */
    /* JADX WARN: Code restructure failed: missing block: B:651:0x126a, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L632;
     */
    /* JADX WARN: Code restructure failed: missing block: B:652:0x126c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageLiveLocation", org.telegram.messenger.R.string.ChannelMessageLiveLocation, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:654:0x127e, code lost:
        if ((r3 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L657;
     */
    /* JADX WARN: Code restructure failed: missing block: B:656:0x1284, code lost:
        if (r49.isSticker() != false) goto L652;
     */
    /* JADX WARN: Code restructure failed: missing block: B:658:0x128a, code lost:
        if (r49.isAnimatedSticker() == false) goto L638;
     */
    /* JADX WARN: Code restructure failed: missing block: B:661:0x1294, code lost:
        if (r49.isGif() == false) goto L646;
     */
    /* JADX WARN: Code restructure failed: missing block: B:662:0x1296, code lost:
        if (r50 != false) goto L645;
     */
    /* JADX WARN: Code restructure failed: missing block: B:664:0x12a0, code lost:
        if (android.text.TextUtils.isEmpty(r49.messageOwner.message) != false) goto L645;
     */
    /* JADX WARN: Code restructure failed: missing block: B:665:0x12a2, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r6 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r6, "🎬 " + r49.messageOwner.message);
        r51[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:666:0x12c8, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageGIF", org.telegram.messenger.R.string.ChannelMessageGIF, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:667:0x12d8, code lost:
        if (r50 != false) goto L651;
     */
    /* JADX WARN: Code restructure failed: missing block: B:669:0x12e2, code lost:
        if (android.text.TextUtils.isEmpty(r49.messageOwner.message) != false) goto L651;
     */
    /* JADX WARN: Code restructure failed: missing block: B:670:0x12e4, code lost:
        r1 = org.telegram.messenger.R.string.NotificationMessageText;
        r6 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", r1, r6, "📎 " + r49.messageOwner.message);
        r51[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:671:0x130a, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageDocument", org.telegram.messenger.R.string.ChannelMessageDocument, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:672:0x131a, code lost:
        r0 = r49.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:673:0x131e, code lost:
        if (r0 == null) goto L656;
     */
    /* JADX WARN: Code restructure failed: missing block: B:674:0x1320, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageStickerEmoji", org.telegram.messenger.R.string.ChannelMessageStickerEmoji, r6, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:675:0x1331, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageSticker", org.telegram.messenger.R.string.ChannelMessageSticker, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:677:0x1340, code lost:
        if (r50 != false) goto L663;
     */
    /* JADX WARN: Code restructure failed: missing block: B:679:0x1348, code lost:
        if (android.text.TextUtils.isEmpty(r49.messageText) != false) goto L663;
     */
    /* JADX WARN: Code restructure failed: missing block: B:680:0x134a, code lost:
        r6 = org.telegram.messenger.LocaleController.formatString("NotificationMessageText", org.telegram.messenger.R.string.NotificationMessageText, r6, r49.messageText);
        r51[0] = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:681:0x135e, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString(r2, org.telegram.messenger.R.string.ChannelMessageNoText, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:682:0x136b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("ChannelMessageMap", org.telegram.messenger.R.string.ChannelMessageMap, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:684:0x1381, code lost:
        if (r49.isMediaEmpty() == false) goto L673;
     */
    /* JADX WARN: Code restructure failed: missing block: B:685:0x1383, code lost:
        if (r50 != false) goto L672;
     */
    /* JADX WARN: Code restructure failed: missing block: B:687:0x138d, code lost:
        if (android.text.TextUtils.isEmpty(r49.messageOwner.message) != false) goto L672;
     */
    /* JADX WARN: Code restructure failed: missing block: B:688:0x138f, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r6, r1.title, r49.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:689:0x13a9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString(r38, org.telegram.messenger.R.string.NotificationMessageGroupNoText, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:690:0x13be, code lost:
        r3 = r38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:691:0x13c4, code lost:
        if (r49.type != 29) goto L678;
     */
    /* JADX WARN: Code restructure failed: missing block: B:693:0x13cc, code lost:
        if ((org.telegram.messenger.MessageObject.getMedia(r49) instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) == false) goto L678;
     */
    /* JADX WARN: Code restructure failed: missing block: B:694:0x13ce, code lost:
        r0 = org.telegram.messenger.LocaleController.formatPluralString("NotificationChatMessagePaidMedia", (int) ((org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia) org.telegram.messenger.MessageObject.getMedia(r49)).stars_amount, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:695:0x13ea, code lost:
        r5 = r49.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:696:0x13f0, code lost:
        if ((r5.media instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPhoto) == false) goto L685;
     */
    /* JADX WARN: Code restructure failed: missing block: B:697:0x13f2, code lost:
        if (r50 != false) goto L684;
     */
    /* JADX WARN: Code restructure failed: missing block: B:699:0x13fa, code lost:
        if (android.text.TextUtils.isEmpty(r5.message) != false) goto L684;
     */
    /* JADX WARN: Code restructure failed: missing block: B:700:0x13fc, code lost:
        r2 = org.telegram.messenger.R.string.NotificationMessageGroupText;
        r1 = r1.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r2, r6, r1, "🖼 " + r49.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:701:0x1425, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPhoto", org.telegram.messenger.R.string.NotificationMessageGroupPhoto, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:703:0x143e, code lost:
        if (r49.isVideo() == false) goto L692;
     */
    /* JADX WARN: Code restructure failed: missing block: B:704:0x1440, code lost:
        if (r50 != false) goto L691;
     */
    /* JADX WARN: Code restructure failed: missing block: B:706:0x144a, code lost:
        if (android.text.TextUtils.isEmpty(r49.messageOwner.message) != false) goto L691;
     */
    /* JADX WARN: Code restructure failed: missing block: B:707:0x144c, code lost:
        r2 = org.telegram.messenger.R.string.NotificationMessageGroupText;
        r1 = r1.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r2, r6, r1, "📹 " + r49.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:708:0x1475, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString(" ", org.telegram.messenger.R.string.NotificationMessageGroupVideo, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:710:0x1491, code lost:
        if (r49.isVoice() == false) goto L695;
     */
    /* JADX WARN: Code restructure failed: missing block: B:711:0x1493, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupAudio", org.telegram.messenger.R.string.NotificationMessageGroupAudio, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:713:0x14a9, code lost:
        if (r49.isRoundVideo() == false) goto L698;
     */
    /* JADX WARN: Code restructure failed: missing block: B:714:0x14ab, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupRound", org.telegram.messenger.R.string.NotificationMessageGroupRound, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:716:0x14c1, code lost:
        if (r49.isMusic() == false) goto L701;
     */
    /* JADX WARN: Code restructure failed: missing block: B:717:0x14c3, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMusic", org.telegram.messenger.R.string.NotificationMessageGroupMusic, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:718:0x14d5, code lost:
        r5 = r49.messageOwner.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:719:0x14db, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaContact) == false) goto L704;
     */
    /* JADX WARN: Code restructure failed: missing block: B:720:0x14dd, code lost:
        r5 = (org.telegram.tgnet.TLRPC$TL_messageMediaContact) r5;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupContact2", org.telegram.messenger.R.string.NotificationMessageGroupContact2, r6, r1.title, org.telegram.messenger.ContactsController.formatName(r5.first_name, r5.last_name));
     */
    /* JADX WARN: Code restructure failed: missing block: B:722:0x1501, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaPoll) == false) goto L710;
     */
    /* JADX WARN: Code restructure failed: missing block: B:723:0x1503, code lost:
        r0 = ((org.telegram.tgnet.TLRPC$TL_messageMediaPoll) r5).poll;
     */
    /* JADX WARN: Code restructure failed: missing block: B:724:0x1509, code lost:
        if (r0.quiz == false) goto L709;
     */
    /* JADX WARN: Code restructure failed: missing block: B:725:0x150b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupQuiz2", org.telegram.messenger.R.string.NotificationMessageGroupQuiz2, r6, r1.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x1527, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupPoll2", org.telegram.messenger.R.string.NotificationMessageGroupPoll2, r6, r1.title, r0.question.text);
     */
    /* JADX WARN: Code restructure failed: missing block: B:728:0x1545, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGame) == false) goto L713;
     */
    /* JADX WARN: Code restructure failed: missing block: B:729:0x1547, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGame", org.telegram.messenger.R.string.NotificationMessageGroupGame, r6, r1.title, r5.game.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:731:0x1565, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) == false) goto L716;
     */
    /* JADX WARN: Code restructure failed: missing block: B:732:0x1567, code lost:
        r5 = (org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway) r5;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageChannelGiveaway", org.telegram.messenger.R.string.NotificationMessageChannelGiveaway, r1.title, java.lang.Integer.valueOf(r5.quantity), java.lang.Integer.valueOf(r5.months));
     */
    /* JADX WARN: Code restructure failed: missing block: B:734:0x158e, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGiveawayResults) == false) goto L719;
     */
    /* JADX WARN: Code restructure failed: missing block: B:735:0x1590, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("BoostingGiveawayResults", org.telegram.messenger.R.string.BoostingGiveawayResults, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:737:0x159e, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeo) != false) goto L754;
     */
    /* JADX WARN: Code restructure failed: missing block: B:739:0x15a2, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaVenue) == false) goto L723;
     */
    /* JADX WARN: Code restructure failed: missing block: B:742:0x15ab, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive) == false) goto L726;
     */
    /* JADX WARN: Code restructure failed: missing block: B:743:0x15ad, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupLiveLocation", org.telegram.messenger.R.string.NotificationMessageGroupLiveLocation, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:745:0x15c4, code lost:
        if ((r5 instanceof org.telegram.tgnet.TLRPC$TL_messageMediaDocument) == false) goto L749;
     */
    /* JADX WARN: Code restructure failed: missing block: B:747:0x15ca, code lost:
        if (r49.isSticker() != false) goto L744;
     */
    /* JADX WARN: Code restructure failed: missing block: B:749:0x15d0, code lost:
        if (r49.isAnimatedSticker() == false) goto L732;
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x15da, code lost:
        if (r49.isGif() == false) goto L739;
     */
    /* JADX WARN: Code restructure failed: missing block: B:753:0x15dc, code lost:
        if (r50 != false) goto L738;
     */
    /* JADX WARN: Code restructure failed: missing block: B:755:0x15e6, code lost:
        if (android.text.TextUtils.isEmpty(r49.messageOwner.message) != false) goto L738;
     */
    /* JADX WARN: Code restructure failed: missing block: B:756:0x15e8, code lost:
        r2 = org.telegram.messenger.R.string.NotificationMessageGroupText;
        r1 = r1.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r2, r6, r1, "🎬 " + r49.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:757:0x1611, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupGif", org.telegram.messenger.R.string.NotificationMessageGroupGif, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:758:0x1626, code lost:
        if (r50 != false) goto L743;
     */
    /* JADX WARN: Code restructure failed: missing block: B:760:0x1630, code lost:
        if (android.text.TextUtils.isEmpty(r49.messageOwner.message) != false) goto L743;
     */
    /* JADX WARN: Code restructure failed: missing block: B:761:0x1632, code lost:
        r2 = org.telegram.messenger.R.string.NotificationMessageGroupText;
        r1 = r1.title;
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", r2, r6, r1, "📎 " + r49.messageOwner.message);
     */
    /* JADX WARN: Code restructure failed: missing block: B:762:0x165b, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupDocument", org.telegram.messenger.R.string.NotificationMessageGroupDocument, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:763:0x1670, code lost:
        r0 = r49.getStickerEmoji();
     */
    /* JADX WARN: Code restructure failed: missing block: B:764:0x1674, code lost:
        if (r0 == null) goto L748;
     */
    /* JADX WARN: Code restructure failed: missing block: B:765:0x1676, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupStickerEmoji", org.telegram.messenger.R.string.NotificationMessageGroupStickerEmoji, r6, r1.title, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:766:0x168c, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupSticker", org.telegram.messenger.R.string.NotificationMessageGroupSticker, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:767:0x169f, code lost:
        if (r50 != false) goto L753;
     */
    /* JADX WARN: Code restructure failed: missing block: B:769:0x16a7, code lost:
        if (android.text.TextUtils.isEmpty(r49.messageText) != false) goto L753;
     */
    /* JADX WARN: Code restructure failed: missing block: B:770:0x16a9, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.R.string.NotificationMessageGroupText, r6, r1.title, r49.messageText);
     */
    /* JADX WARN: Code restructure failed: missing block: B:771:0x16c1, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString(r3, org.telegram.messenger.R.string.NotificationMessageGroupNoText, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:772:0x16d4, code lost:
        r0 = org.telegram.messenger.LocaleController.formatString("NotificationMessageGroupMap", org.telegram.messenger.R.string.NotificationMessageGroupMap, r6, r1.title);
     */
    /* JADX WARN: Code restructure failed: missing block: B:799:?, code lost:
        return r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:800:?, code lost:
        return r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:801:?, code lost:
        return r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:802:?, code lost:
        return r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:803:?, code lost:
        return r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:804:?, code lost:
        return r6;
     */
    /* JADX WARN: Removed duplicated region for block: B:272:0x069b  */
    /* JADX WARN: Removed duplicated region for block: B:775:0x16ef  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getStringForMessage(MessageObject messageObject, boolean z, boolean[] zArr, boolean[] zArr2) {
        String str;
        String str2;
        String str3;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$Chat tLRPC$Chat2;
        String str4;
        boolean z2;
        String formatString;
        String formatString2;
        char c;
        boolean z3;
        String formatString3;
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
        StringBuilder sb = new StringBuilder();
        String str5 = ", ";
        sb.append(NotificationsSettingsFacade.PROPERTY_CONTENT_PREVIEW);
        sb.append(j);
        boolean z4 = notificationsSettings.getBoolean(sb.toString(), true);
        if (messageObject.isFcmMessage()) {
            if (j2 == 0 && j3 != 0) {
                if (!z4 || !notificationsSettings.getBoolean("EnablePreviewAll", true)) {
                    if (zArr2 != null) {
                        zArr2[0] = false;
                    }
                    return LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, messageObject.localName);
                }
            } else if (j2 != 0 && (!z4 || ((!messageObject.localChannel && !notificationsSettings.getBoolean("EnablePreviewGroup", true)) || (messageObject.localChannel && !notificationsSettings.getBoolean("EnablePreviewChannel", true))))) {
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
                str = "NotificationMessageGroupNoText";
                TLRPC$User user = getMessagesController().getUser(Long.valueOf(j3));
                if (user != null) {
                    str3 = UserObject.getUserName(user);
                } else {
                    str2 = "ChannelMessageNoText";
                    str3 = null;
                }
            } else if (j == clientUserId) {
                str = "NotificationMessageGroupNoText";
                str3 = LocaleController.getString("MessageScheduledReminderNotification", R.string.MessageScheduledReminderNotification);
            } else {
                str = "NotificationMessageGroupNoText";
                str3 = LocaleController.getString("NotificationMessageScheduledName", R.string.NotificationMessageScheduledName);
            }
            str2 = "ChannelMessageNoText";
        } else {
            str = "NotificationMessageGroupNoText";
            str2 = "ChannelMessageNoText";
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j3));
            if (chat != null) {
                str3 = chat.title;
            }
            str3 = null;
        }
        if (str3 == null) {
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
            formatString = LocaleController.getString("YouHaveNewMessage", R.string.YouHaveNewMessage);
        } else {
            TLRPC$Chat tLRPC$Chat3 = tLRPC$Chat;
            if (j2 != 0 || j3 == 0) {
                if (j2 != 0) {
                    if (ChatObject.isChannel(tLRPC$Chat3)) {
                        tLRPC$Chat2 = tLRPC$Chat3;
                        if (!tLRPC$Chat2.megagroup) {
                            str4 = "ChatThemeDisabled";
                            z2 = true;
                            if (z4) {
                                boolean z5 = !z2 ? true : true;
                                if (z2) {
                                }
                            }
                            String str6 = str;
                            String str7 = str2;
                            if (zArr2 != null) {
                                zArr2[0] = false;
                            }
                            formatString = (ChatObject.isChannel(tLRPC$Chat2) || tLRPC$Chat2.megagroup) ? (messageObject.type == 29 || !(MessageObject.getMedia(messageObject) instanceof TLRPC$TL_messageMediaPaidMedia)) ? LocaleController.formatString(str6, R.string.NotificationMessageGroupNoText, str3, tLRPC$Chat2.title) : LocaleController.formatPluralString("NotificationMessagePaidMedia", (int) ((TLRPC$TL_messageMediaPaidMedia) MessageObject.getMedia(messageObject)).stars_amount, str3) : LocaleController.formatString(str7, R.string.ChannelMessageNoText, str3);
                            return formatString;
                        }
                    } else {
                        tLRPC$Chat2 = tLRPC$Chat3;
                    }
                    str4 = "ChatThemeDisabled";
                    z2 = false;
                    if (z4) {
                    }
                    String str62 = str;
                    String str72 = str2;
                    if (zArr2 != null) {
                    }
                    if (ChatObject.isChannel(tLRPC$Chat2)) {
                    }
                    return formatString;
                }
                return null;
            } else if (z4 && notificationsSettings.getBoolean("EnablePreviewAll", true)) {
                TLRPC$Message tLRPC$Message2 = messageObject.messageOwner;
                if (tLRPC$Message2 instanceof TLRPC$TL_messageService) {
                    TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message2.action;
                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetSameChatWallPaper) {
                        formatString = LocaleController.getString("WallpaperSameNotification", R.string.WallpaperSameNotification);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatWallPaper) {
                        formatString = LocaleController.getString("WallpaperNotification", R.string.WallpaperNotification);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGeoProximityReached) {
                        formatString = messageObject.messageText.toString();
                    } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserJoined) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionContactSignUp)) {
                        formatString = LocaleController.formatString("NotificationContactJoined", R.string.NotificationContactJoined, str3);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionUserUpdatedPhoto) {
                        formatString = LocaleController.formatString("NotificationContactNewPhoto", R.string.NotificationContactNewPhoto, str3);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionLoginUnknownLocation) {
                        String formatString4 = LocaleController.formatString("formatDateAtTime", R.string.formatDateAtTime, LocaleController.getInstance().getFormatterYear().format(messageObject.messageOwner.date * 1000), LocaleController.getInstance().getFormatterDay().format(messageObject.messageOwner.date * 1000));
                        int i = R.string.NotificationUnrecognizedDevice;
                        String str8 = getUserConfig().getCurrentUser().first_name;
                        TLRPC$MessageAction tLRPC$MessageAction2 = messageObject.messageOwner.action;
                        formatString = LocaleController.formatString("NotificationUnrecognizedDevice", i, str8, formatString4, tLRPC$MessageAction2.title, tLRPC$MessageAction2.address);
                    } else if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionGameScore) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPaymentSent)) {
                        formatString = messageObject.messageText.toString();
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPhoneCall) {
                        if (tLRPC$MessageAction.video) {
                            formatString = LocaleController.getString("CallMessageVideoIncomingMissed", R.string.CallMessageVideoIncomingMissed);
                        } else {
                            formatString = LocaleController.getString("CallMessageIncomingMissed", R.string.CallMessageIncomingMissed);
                        }
                    } else {
                        if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatTheme) {
                            String str9 = ((TLRPC$TL_messageActionSetChatTheme) tLRPC$MessageAction).emoticon;
                            if (!TextUtils.isEmpty(str9)) {
                                c = 0;
                                z3 = true;
                                if (j == clientUserId) {
                                    formatString3 = LocaleController.formatString("ChangedChatThemeYou", R.string.ChatThemeChangedYou, str9);
                                } else {
                                    formatString3 = LocaleController.formatString("ChangedChatThemeTo", R.string.ChatThemeChangedTo, str3, str9);
                                }
                            } else if (j == clientUserId) {
                                c = 0;
                                formatString2 = LocaleController.formatString("ChatThemeDisabledYou", R.string.ChatThemeDisabledYou, new Object[0]);
                                z3 = true;
                                zArr[c] = z3;
                                return formatString2;
                            } else {
                                c = 0;
                                z3 = true;
                                formatString3 = LocaleController.formatString("ChatThemeDisabled", R.string.ChatThemeDisabled, str3, str9);
                            }
                            formatString2 = formatString3;
                            zArr[c] = z3;
                            return formatString2;
                        }
                        return null;
                    }
                } else if (!messageObject.isMediaEmpty()) {
                    TLRPC$Message tLRPC$Message3 = messageObject.messageOwner;
                    if (tLRPC$Message3.media instanceof TLRPC$TL_messageMediaPhoto) {
                        if (!z && !TextUtils.isEmpty(tLRPC$Message3.message)) {
                            int i2 = R.string.NotificationMessageText;
                            formatString2 = LocaleController.formatString("NotificationMessageText", i2, str3, "🖼 " + messageObject.messageOwner.message);
                            zArr[0] = true;
                            return formatString2;
                        } else if (messageObject.messageOwner.media.ttl_seconds != 0) {
                            formatString = LocaleController.formatString("NotificationMessageSDPhoto", R.string.NotificationMessageSDPhoto, str3);
                        } else {
                            formatString = LocaleController.formatString("NotificationMessagePhoto", R.string.NotificationMessagePhoto, str3);
                        }
                    } else if (messageObject.isVideo()) {
                        if (!z && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            int i3 = R.string.NotificationMessageText;
                            formatString2 = LocaleController.formatString("NotificationMessageText", i3, str3, "📹 " + messageObject.messageOwner.message);
                            zArr[0] = true;
                            return formatString2;
                        } else if (messageObject.messageOwner.media.ttl_seconds != 0) {
                            formatString = LocaleController.formatString("NotificationMessageSDVideo", R.string.NotificationMessageSDVideo, str3);
                        } else {
                            formatString = LocaleController.formatString("NotificationMessageVideo", R.string.NotificationMessageVideo, str3);
                        }
                    } else if (messageObject.isGame()) {
                        formatString = LocaleController.formatString("NotificationMessageGame", R.string.NotificationMessageGame, str3, messageObject.messageOwner.media.game.title);
                    } else if (messageObject.isVoice()) {
                        formatString = LocaleController.formatString("NotificationMessageAudio", R.string.NotificationMessageAudio, str3);
                    } else if (messageObject.isRoundVideo()) {
                        formatString = LocaleController.formatString("NotificationMessageRound", R.string.NotificationMessageRound, str3);
                    } else if (messageObject.isMusic()) {
                        formatString = LocaleController.formatString("NotificationMessageMusic", R.string.NotificationMessageMusic, str3);
                    } else {
                        TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
                        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaContact) {
                            TLRPC$TL_messageMediaContact tLRPC$TL_messageMediaContact = (TLRPC$TL_messageMediaContact) tLRPC$MessageMedia;
                            formatString = LocaleController.formatString("NotificationMessageContact2", R.string.NotificationMessageContact2, str3, ContactsController.formatName(tLRPC$TL_messageMediaContact.first_name, tLRPC$TL_messageMediaContact.last_name));
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGiveaway) {
                            TLRPC$TL_messageMediaGiveaway tLRPC$TL_messageMediaGiveaway = (TLRPC$TL_messageMediaGiveaway) tLRPC$MessageMedia;
                            formatString = LocaleController.formatString("NotificationMessageChannelGiveaway", R.string.NotificationMessageChannelGiveaway, str3, Integer.valueOf(tLRPC$TL_messageMediaGiveaway.quantity), Integer.valueOf(tLRPC$TL_messageMediaGiveaway.months));
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGiveawayResults) {
                            formatString = LocaleController.formatString("BoostingGiveawayResults", R.string.BoostingGiveawayResults, new Object[0]);
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                            TLRPC$Poll tLRPC$Poll = ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia).poll;
                            if (tLRPC$Poll.quiz) {
                                formatString = LocaleController.formatString("NotificationMessageQuiz2", R.string.NotificationMessageQuiz2, str3, tLRPC$Poll.question.text);
                            } else {
                                formatString = LocaleController.formatString("NotificationMessagePoll2", R.string.NotificationMessagePoll2, str3, tLRPC$Poll.question.text);
                            }
                        } else if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeo) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaVenue)) {
                            formatString = LocaleController.formatString("NotificationMessageMap", R.string.NotificationMessageMap, str3);
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGeoLive) {
                            formatString = LocaleController.formatString("NotificationMessageLiveLocation", R.string.NotificationMessageLiveLocation, str3);
                        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                            if (messageObject.isSticker() || messageObject.isAnimatedSticker()) {
                                String stickerEmoji = messageObject.getStickerEmoji();
                                if (stickerEmoji != null) {
                                    formatString = LocaleController.formatString("NotificationMessageStickerEmoji", R.string.NotificationMessageStickerEmoji, str3, stickerEmoji);
                                } else {
                                    formatString = LocaleController.formatString("NotificationMessageSticker", R.string.NotificationMessageSticker, str3);
                                }
                            } else if (messageObject.isGif()) {
                                if (!z && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                    int i4 = R.string.NotificationMessageText;
                                    formatString2 = LocaleController.formatString("NotificationMessageText", i4, str3, "🎬 " + messageObject.messageOwner.message);
                                    zArr[0] = true;
                                    return formatString2;
                                }
                                formatString = LocaleController.formatString("NotificationMessageGif", R.string.NotificationMessageGif, str3);
                            } else if (!z && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                int i5 = R.string.NotificationMessageText;
                                formatString2 = LocaleController.formatString("NotificationMessageText", i5, str3, "📎 " + messageObject.messageOwner.message);
                                zArr[0] = true;
                                return formatString2;
                            } else {
                                formatString = LocaleController.formatString("NotificationMessageDocument", R.string.NotificationMessageDocument, str3);
                            }
                        } else if (!z && !TextUtils.isEmpty(messageObject.messageText)) {
                            formatString2 = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, str3, messageObject.messageText);
                            zArr[0] = true;
                            return formatString2;
                        } else {
                            formatString = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str3);
                        }
                    }
                } else if (!z) {
                    if (!TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        formatString2 = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, str3, messageObject.messageOwner.message);
                        zArr[0] = true;
                        return formatString2;
                    }
                    formatString = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str3);
                } else {
                    formatString = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str3);
                }
                return formatString;
            } else {
                if (zArr2 != null) {
                    zArr2[0] = false;
                }
                formatString = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, str3);
            }
        }
        return formatString;
    }

    private void scheduleNotificationRepeat() {
        try {
            Intent intent = new Intent(ApplicationLoader.applicationContext, NotificationRepeat.class);
            intent.putExtra("currentAccount", this.currentAccount);
            PendingIntent service = PendingIntent.getService(ApplicationLoader.applicationContext, 0, intent, ConnectionsManager.FileTypeVideo);
            int i = getAccountInstance().getNotificationsSettings().getInt("repeat_messages", 60);
            if (i > 0 && this.personalCount > 0) {
                this.alarmManager.set(2, SystemClock.elapsedRealtime() + (i * 60000), service);
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda73
            @Override // java.lang.Runnable
            public final void run() {
                NotificationsController.this.lambda$showNotifications$33();
            }
        });
    }

    public void hideNotifications() {
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda38
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda40
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
            notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda35
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
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda42
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda39
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda63
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda59
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda60
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

    /* JADX WARN: Removed duplicated region for block: B:28:0x00f4 A[Catch: Exception -> 0x0066, TryCatch #0 {Exception -> 0x0066, blocks: (B:9:0x0021, B:12:0x0062, B:16:0x006d, B:20:0x007d, B:22:0x00a6, B:24:0x00b6, B:26:0x00c0, B:28:0x00f4, B:30:0x00fc, B:32:0x0105, B:41:0x0126, B:45:0x013d, B:46:0x0154, B:34:0x010c, B:36:0x0112, B:38:0x0117, B:37:0x0115, B:39:0x011c, B:29:0x00f8, B:19:0x0079, B:15:0x0069), top: B:50:0x0021 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00f8 A[Catch: Exception -> 0x0066, TryCatch #0 {Exception -> 0x0066, blocks: (B:9:0x0021, B:12:0x0062, B:16:0x006d, B:20:0x007d, B:22:0x00a6, B:24:0x00b6, B:26:0x00c0, B:28:0x00f4, B:30:0x00fc, B:32:0x0105, B:41:0x0126, B:45:0x013d, B:46:0x0154, B:34:0x010c, B:36:0x0112, B:38:0x0117, B:37:0x0115, B:39:0x011c, B:29:0x00f8, B:19:0x0079, B:15:0x0069), top: B:50:0x0021 }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0105 A[Catch: Exception -> 0x0066, TryCatch #0 {Exception -> 0x0066, blocks: (B:9:0x0021, B:12:0x0062, B:16:0x006d, B:20:0x007d, B:22:0x00a6, B:24:0x00b6, B:26:0x00c0, B:28:0x00f4, B:30:0x00fc, B:32:0x0105, B:41:0x0126, B:45:0x013d, B:46:0x0154, B:34:0x010c, B:36:0x0112, B:38:0x0117, B:37:0x0115, B:39:0x011c, B:29:0x00f8, B:19:0x0079, B:15:0x0069), top: B:50:0x0021 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0126 A[Catch: Exception -> 0x0066, TryCatch #0 {Exception -> 0x0066, blocks: (B:9:0x0021, B:12:0x0062, B:16:0x006d, B:20:0x007d, B:22:0x00a6, B:24:0x00b6, B:26:0x00c0, B:28:0x00f4, B:30:0x00fc, B:32:0x0105, B:41:0x0126, B:45:0x013d, B:46:0x0154, B:34:0x010c, B:36:0x0112, B:38:0x0117, B:37:0x0115, B:39:0x011c, B:29:0x00f8, B:19:0x0079, B:15:0x0069), top: B:50:0x0021 }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0154 A[Catch: Exception -> 0x0066, TRY_LEAVE, TryCatch #0 {Exception -> 0x0066, blocks: (B:9:0x0021, B:12:0x0062, B:16:0x006d, B:20:0x007d, B:22:0x00a6, B:24:0x00b6, B:26:0x00c0, B:28:0x00f4, B:30:0x00fc, B:32:0x0105, B:41:0x0126, B:45:0x013d, B:46:0x0154, B:34:0x010c, B:36:0x0112, B:38:0x0117, B:37:0x0115, B:39:0x011c, B:29:0x00f8, B:19:0x0079, B:15:0x0069), top: B:50:0x0021 }] */
    @SuppressLint({"RestrictedApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String createNotificationShortcut(NotificationCompat.Builder builder, long j, String str, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, Person person, boolean z) {
        Bitmap bitmap;
        IconCompat createWithResource;
        if (unsupportedNotificationShortcut() || (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup)) {
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

    @TargetApi(26)
    protected void ensureGroupsCreated() {
        List notificationChannels;
        String id;
        int importance;
        List notificationChannelGroups;
        String id2;
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
        if (this.groupsCreated == null) {
            this.groupsCreated = Boolean.valueOf(notificationsSettings.getBoolean("groupsCreated5", false));
        }
        if (!this.groupsCreated.booleanValue()) {
            try {
                String str = this.currentAccount + "channel";
                notificationChannels = systemNotificationManager.getNotificationChannels();
                int size = notificationChannels.size();
                SharedPreferences.Editor editor = null;
                for (int i = 0; i < size; i++) {
                    NotificationChannel m = NotificationsController$$ExternalSyntheticApiModelOutline13.m(notificationChannels.get(i));
                    id = m.getId();
                    if (id.startsWith(str)) {
                        importance = m.getImportance();
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
        notificationChannelGroups = systemNotificationManager.getNotificationChannelGroups();
        String str2 = "channels" + this.currentAccount;
        String str3 = "groups" + this.currentAccount;
        int size2 = notificationChannelGroups.size();
        String str4 = "other" + this.currentAccount;
        String str5 = "reactions" + this.currentAccount;
        String str6 = "stories" + this.currentAccount;
        String str7 = "private" + this.currentAccount;
        for (int i2 = 0; i2 < size2; i2++) {
            id2 = NotificationsController$$ExternalSyntheticApiModelOutline16.m(notificationChannelGroups.get(i2)).getId();
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

    /* JADX WARN: Removed duplicated region for block: B:183:0x043a  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x04af  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x04b3  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x0502  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x0552  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x0572 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:263:0x05b1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:271:0x05c2 A[LOOP:1: B:269:0x05bf->B:271:0x05c2, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:274:0x05d7  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x05e3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:281:0x05f4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:291:0x060c  */
    /* JADX WARN: Removed duplicated region for block: B:294:0x0623  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01d4  */
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
        String uri2;
        String str7;
        String formatString;
        String sb;
        String str8;
        int i4;
        String str9;
        String str10;
        String string;
        StringBuilder sb2;
        String str11;
        String str12;
        String str13;
        String str14;
        NotificationsController notificationsController;
        long j3;
        String str15;
        String str16;
        boolean z4;
        String str17;
        int i5;
        String str18;
        long[] jArr2;
        String str19;
        String str20;
        String str21;
        boolean z5;
        String str22;
        int i6;
        String str23;
        Uri uri3;
        String MD5;
        boolean z6;
        boolean z7;
        AudioAttributes build;
        AudioAttributes build2;
        NotificationChannel notificationChannel;
        int importance;
        Uri sound;
        long[] vibrationPattern;
        boolean shouldVibrate;
        boolean z8;
        long[] jArr3;
        int lightColor;
        String str24;
        String str25;
        String str26;
        long[] jArr4;
        SharedPreferences.Editor editor;
        boolean z9;
        boolean z10;
        String str27;
        String str28;
        String str29;
        ensureGroupsCreated();
        SharedPreferences notificationsSettings = getAccountInstance().getNotificationsSettings();
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
            } else if (i3 == 4 || i3 == 5) {
                str2 = "reactions" + this.currentAccount;
                str3 = "overwrite_reactions";
            } else {
                str2 = "private" + this.currentAccount;
                str3 = "overwrite_private";
            }
            String str30 = str3;
            str4 = str2;
            str5 = str30;
        }
        boolean z11 = !z && DialogObject.isEncryptedDialog(j);
        boolean z12 = (z2 || str5 == null || !notificationsSettings.getBoolean(str5, false)) ? false : true;
        if (uri == null) {
            StringBuilder sb3 = new StringBuilder();
            str6 = "reactions";
            sb3.append("NoSound");
            sb3.append(2);
            uri2 = sb3.toString();
        } else {
            str6 = "reactions";
            uri2 = uri.toString();
        }
        String MD52 = Utilities.MD5(uri2);
        if (MD52 != null) {
            str7 = "private";
            if (MD52.length() > 5) {
                MD52 = MD52.substring(0, 5);
            }
        } else {
            str7 = "private";
        }
        if (z3) {
            formatString = LocaleController.getString("NotificationsSilent", R.string.NotificationsSilent);
            sb = "silent";
        } else if (!z) {
            formatString = z2 ? LocaleController.formatString("NotificationsChatInApp", R.string.NotificationsChatInApp, str) : str;
            StringBuilder sb4 = new StringBuilder();
            sb4.append(z2 ? "org.telegram.keyia" : "org.telegram.key");
            sb4.append(j);
            sb4.append("_");
            sb4.append(j2);
            sb = sb4.toString();
        } else {
            if (z2) {
                i4 = R.string.NotificationsInAppDefault;
                str8 = "stories";
                str9 = "NotificationsInAppDefault";
            } else {
                str8 = "stories";
                i4 = R.string.NotificationsDefault;
                str9 = "NotificationsDefault";
            }
            formatString = LocaleController.getString(str9, i4);
            if (i3 == 2) {
                if (z2) {
                    sb = "channels_ia";
                } else {
                    str10 = formatString;
                    sb = "channels";
                }
            } else if (i3 == 0) {
                if (z2) {
                    sb = "groups_ia";
                } else {
                    str10 = formatString;
                    sb = "groups";
                }
            } else if (i3 == 3) {
                if (z2) {
                    sb = "stories_ia";
                } else {
                    str10 = formatString;
                    sb = str8;
                }
            } else if (i3 == 4 || i3 == 5) {
                sb = z2 ? "reactions_ia" : str6;
            } else if (z2) {
                sb = "private_ia";
            } else {
                str10 = formatString;
                sb = str7;
            }
            String str31 = sb + "_" + MD52;
            string = notificationsSettings.getString(str31, null);
            String string2 = notificationsSettings.getString(str31 + "_s", null);
            StringBuilder sb5 = new StringBuilder();
            if (string != null) {
                sb2 = sb5;
                str11 = string2;
                str12 = "_s";
                str13 = string;
                str14 = "secret";
                notificationsController = this;
                j3 = j;
                str15 = "channel_";
                str16 = str4;
                z4 = z12;
                str17 = "_";
                i5 = i;
                str18 = str31;
                jArr2 = jArr;
            } else {
                notificationChannel = systemNotificationManager.getNotificationChannel(string);
                if (!BuildVars.LOGS_ENABLED) {
                    str15 = "channel_";
                    str16 = str4;
                } else {
                    str16 = str4;
                    StringBuilder sb6 = new StringBuilder();
                    str15 = "channel_";
                    sb6.append("current channel for ");
                    sb6.append(string);
                    sb6.append(" = ");
                    sb6.append(notificationChannel);
                    FileLog.d(sb6.toString());
                }
                if (notificationChannel == null) {
                    str12 = "_s";
                    jArr2 = jArr;
                    sb2 = sb5;
                    str14 = "secret";
                    notificationsController = this;
                    j3 = j;
                    z4 = z12;
                    str17 = "_";
                    str18 = str31;
                    i5 = i;
                    str21 = null;
                    str20 = null;
                    str19 = null;
                    z5 = false;
                    if (z5) {
                    }
                    str22 = str12;
                    if (!z4) {
                    }
                    i6 = 0;
                    while (i6 < jArr2.length) {
                    }
                    str23 = str22;
                    sb2.append(i5);
                    uri3 = uri;
                    if (uri3 != null) {
                    }
                    sb2.append(i2);
                    if (!z) {
                        sb2.append(str14);
                    }
                    MD5 = Utilities.MD5(sb2.toString());
                    if (!z3) {
                        systemNotificationManager.deleteNotificationChannel(str19);
                        if (BuildVars.LOGS_ENABLED) {
                        }
                        str19 = null;
                    }
                    if (str19 == null) {
                    }
                    return str19;
                } else if (!z3 && !z12) {
                    importance = notificationChannel.getImportance();
                    sound = notificationChannel.getSound();
                    vibrationPattern = notificationChannel.getVibrationPattern();
                    shouldVibrate = notificationChannel.shouldVibrate();
                    if (shouldVibrate || vibrationPattern != null) {
                        z8 = shouldVibrate;
                        z4 = z12;
                        jArr3 = vibrationPattern;
                    } else {
                        z8 = shouldVibrate;
                        z4 = z12;
                        jArr3 = new long[]{0, 0};
                    }
                    lightColor = notificationChannel.getLightColor();
                    if (jArr3 != null) {
                        for (long j4 : jArr3) {
                            sb5.append(j4);
                        }
                    }
                    sb5.append(lightColor);
                    if (sound != null) {
                        sb5.append(sound.toString());
                    }
                    sb5.append(importance);
                    if (!z && z11) {
                        sb5.append("secret");
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("current channel settings for " + string + " = " + ((Object) sb5) + " old = " + string2);
                    }
                    String MD53 = Utilities.MD5(sb5.toString());
                    sb5.setLength(0);
                    if (MD53.equals(string2)) {
                        str12 = "_s";
                        jArr2 = jArr;
                        sb2 = sb5;
                        str24 = string2;
                        str25 = string;
                        str14 = "secret";
                        notificationsController = this;
                        str26 = MD53;
                        str17 = "_";
                        j3 = j;
                        str18 = str31;
                        i5 = i;
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
                                str12 = "_s";
                                str18 = str31;
                                str25 = string;
                                str29 = "secret";
                                str26 = MD53;
                                jArr4 = jArr3;
                                str28 = "_";
                                sb2 = sb5;
                                str24 = string2;
                            } else {
                                if (i3 == 3) {
                                    StringBuilder sb7 = new StringBuilder();
                                    sb7.append(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY);
                                    jArr4 = jArr3;
                                    sb7.append(getSharedPrefKey(j, 0L));
                                    edit.putBoolean(sb7.toString(), false);
                                    str27 = "secret";
                                } else {
                                    str27 = "secret";
                                    jArr4 = jArr3;
                                    edit.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + getSharedPrefKey(j, 0L), 2);
                                }
                                str12 = "_s";
                                str24 = string2;
                                str18 = str31;
                                str25 = string;
                                sb2 = sb5;
                                str26 = MD53;
                                str28 = "_";
                                str29 = str27;
                                updateServerNotificationsSettings(j, 0L, true);
                            }
                            j3 = j;
                            str17 = str28;
                            str14 = str29;
                            editor = edit;
                        } else {
                            str12 = "_s";
                            str18 = str31;
                            str25 = string;
                            str14 = "secret";
                            str26 = MD53;
                            jArr4 = jArr3;
                            sb2 = sb5;
                            j3 = j;
                            str24 = string2;
                            if (importance == i2) {
                                str17 = "_";
                                editor = null;
                                z9 = false;
                                notificationsController = this;
                                jArr2 = jArr;
                                z10 = z8;
                                if ((!notificationsController.isEmptyVibration(jArr2)) == z10) {
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
                                    i5 = i;
                                    jArr2 = jArr4;
                                    z9 = true;
                                } else {
                                    i5 = i;
                                }
                                if (lightColor != i5) {
                                    if (!z2) {
                                        if (editor == null) {
                                            editor = notificationsSettings.edit();
                                        }
                                        if (!z) {
                                            editor.putInt("color_" + j3, lightColor);
                                        } else if (i3 == 2) {
                                            editor.putInt("ChannelLed", lightColor);
                                        } else if (i3 == 0) {
                                            editor.putInt("GroupLed", lightColor);
                                        } else if (i3 == 3) {
                                            editor.putInt("StoriesLed", lightColor);
                                        } else if (i3 == 5 || i3 == 4) {
                                            editor.putInt("ReactionsLed", lightColor);
                                        } else {
                                            editor.putInt("MessagesLed", lightColor);
                                        }
                                    }
                                    i5 = lightColor;
                                    z9 = true;
                                }
                                if (editor != null) {
                                    editor.commit();
                                }
                                z5 = z9;
                            } else if (z2) {
                                str17 = "_";
                                editor = null;
                            } else {
                                SharedPreferences.Editor edit2 = notificationsSettings.edit();
                                str17 = "_";
                                int i7 = (importance == 4 || importance == 5) ? 1 : importance == 1 ? 4 : importance == 2 ? 5 : 0;
                                if (z) {
                                    if (i3 == 3) {
                                        edit2.putBoolean("EnableAllStories", true);
                                    } else if (i3 == 4) {
                                        edit2.putBoolean("EnableReactionsMessages", true);
                                        edit2.putBoolean("EnableReactionsStories", true);
                                    } else {
                                        edit2.putInt(getGlobalNotificationsKey(i3), 0);
                                    }
                                    if (i3 == 2) {
                                        edit2.putInt("priority_channel", i7);
                                    } else if (i3 == 0) {
                                        edit2.putInt("priority_group", i7);
                                    } else if (i3 == 3) {
                                        edit2.putInt("priority_stories", i7);
                                    } else if (i3 == 4 || i3 == 5) {
                                        edit2.putInt("priority_react", i7);
                                    } else {
                                        edit2.putInt("priority_messages", i7);
                                    }
                                } else if (i3 == 3) {
                                    edit2.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + j3, true);
                                } else {
                                    edit2.putInt(NotificationsSettingsFacade.PROPERTY_NOTIFY + j3, 0);
                                    edit2.remove(NotificationsSettingsFacade.PROPERTY_NOTIFY_UNTIL + j3);
                                    edit2.putInt("priority_" + j3, i7);
                                }
                                editor = edit2;
                            }
                        }
                        z9 = true;
                        notificationsController = this;
                        jArr2 = jArr;
                        z10 = z8;
                        if ((!notificationsController.isEmptyVibration(jArr2)) == z10) {
                        }
                        if (lightColor != i5) {
                        }
                        if (editor != null) {
                        }
                        z5 = z9;
                    }
                    str20 = str26;
                    str21 = str24;
                    str19 = str25;
                    if (z5 || str20 == null) {
                        str22 = str12;
                        if (!z4 || str20 == null || !z2 || !z) {
                            i6 = 0;
                            while (i6 < jArr2.length) {
                                sb2.append(jArr2[i6]);
                                i6++;
                                str22 = str22;
                            }
                            str23 = str22;
                            sb2.append(i5);
                            uri3 = uri;
                            if (uri3 != null) {
                                sb2.append(uri.toString());
                            }
                            sb2.append(i2);
                            if (!z && z11) {
                                sb2.append(str14);
                            }
                            MD5 = Utilities.MD5(sb2.toString());
                            if (!z3 && str19 != null && (z4 || !str21.equals(MD5))) {
                                try {
                                    systemNotificationManager.deleteNotificationChannel(str19);
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d("delete channel by settings change " + str19);
                                }
                                str19 = null;
                            }
                            if (str19 == null) {
                                str19 = z ? notificationsController.currentAccount + str15 + str18 + str17 + Utilities.random.nextLong() : notificationsController.currentAccount + str15 + j3 + str17 + Utilities.random.nextLong();
                                if (z11) {
                                    str10 = LocaleController.getString("SecretChatName", R.string.SecretChatName);
                                }
                                NotificationChannel notificationChannel2 = new NotificationChannel(str19, str10, i2);
                                notificationChannel2.setGroup(str16);
                                if (i5 != 0) {
                                    z6 = true;
                                    notificationChannel2.enableLights(true);
                                    notificationChannel2.setLightColor(i5);
                                    z7 = false;
                                } else {
                                    z6 = true;
                                    z7 = false;
                                    notificationChannel2.enableLights(false);
                                }
                                if (!notificationsController.isEmptyVibration(jArr2)) {
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
                                if (uri3 != null) {
                                    build2 = builder.build();
                                    notificationChannel2.setSound(uri3, build2);
                                } else {
                                    build = builder.build();
                                    notificationChannel2.setSound(null, build);
                                }
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d("create new channel " + str19);
                                }
                                notificationsController.lastNotificationChannelCreateTime = SystemClock.elapsedRealtime();
                                systemNotificationManager.createNotificationChannel(notificationChannel2);
                                notificationsSettings.edit().putString(str18, str19).putString(str18 + str23, MD5).commit();
                            }
                            return str19;
                        }
                    } else {
                        SharedPreferences.Editor putString = notificationsSettings.edit().putString(str18, str19);
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append(str18);
                        str22 = str12;
                        sb8.append(str22);
                        putString.putString(sb8.toString(), str20).commit();
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("change edited channel " + str19);
                        }
                    }
                    MD5 = str20;
                    str23 = str22;
                    uri3 = uri;
                    if (str19 == null) {
                    }
                    return str19;
                } else {
                    str12 = "_s";
                    jArr2 = jArr;
                    sb2 = sb5;
                    str11 = string2;
                    str13 = string;
                    str14 = "secret";
                    notificationsController = this;
                    j3 = j;
                    z4 = z12;
                    str17 = "_";
                    str18 = str31;
                    i5 = i;
                }
            }
            str21 = str11;
            str19 = str13;
            str20 = null;
            z5 = false;
            if (z5) {
            }
            str22 = str12;
            if (!z4) {
            }
            i6 = 0;
            while (i6 < jArr2.length) {
            }
            str23 = str22;
            sb2.append(i5);
            uri3 = uri;
            if (uri3 != null) {
            }
            sb2.append(i2);
            if (!z) {
            }
            MD5 = Utilities.MD5(sb2.toString());
            if (!z3) {
            }
            if (str19 == null) {
            }
            return str19;
        }
        str10 = formatString;
        String str312 = sb + "_" + MD52;
        string = notificationsSettings.getString(str312, null);
        String string22 = notificationsSettings.getString(str312 + "_s", null);
        StringBuilder sb52 = new StringBuilder();
        if (string != null) {
        }
        str21 = str11;
        str19 = str13;
        str20 = null;
        z5 = false;
        if (z5) {
        }
        str22 = str12;
        if (!z4) {
        }
        i6 = 0;
        while (i6 < jArr2.length) {
        }
        str23 = str22;
        sb2.append(i5);
        uri3 = uri;
        if (uri3 != null) {
        }
        sb2.append(i2);
        if (!z) {
        }
        MD5 = Utilities.MD5(sb2.toString());
        if (!z3) {
        }
        if (str19 == null) {
        }
        return str19;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:457:0x0bb4
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    private void showOrUpdateNotification(boolean r59) {
        /*
            Method dump skipped, instructions count: 3904
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.NotificationsController.showOrUpdateNotification(boolean):void");
    }

    private boolean isSilentMessage(MessageObject messageObject) {
        return messageObject.messageOwner.silent || messageObject.isReactionPush;
    }

    @SuppressLint({"NewApi"})
    private void setNotificationChannel(Notification notification, NotificationCompat.Builder builder, boolean z) {
        String channelId;
        if (z) {
            builder.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
            return;
        }
        channelId = notification.getChannelId();
        builder.setChannelId(channelId);
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
        builder.setChannelId(validateChannelId(j, j2, str, jArr, i, uri2, i2, z, z2, z3, i3));
        notificationManager.notify(this.notificationId, builder.build());
    }

    /* JADX WARN: Can't wrap try/catch for region: R(91:54|(2:56|(3:58|59|60)(4:61|(2:64|62)|65|66))(1:704)|67|(1:69)(2:(1:701)(1:703)|702)|70|(4:73|(2:75|76)(1:78)|77|71)|79|80|(5:82|(2:(1:85)(1:605)|86)(1:606)|(1:604)(2:92|(2:96|97))|603|97)(3:607|(3:(1:684)(1:616)|617|(8:619|(2:621|(1:623)(5:636|(1:638)|639|640|641))(2:642|(7:646|(1:648)|625|626|(1:628)(2:631|(1:633)(2:634|635))|629|630))|624|625|626|(0)(0)|629|630)(2:650|(2:652|(1:654)(5:655|(1:657)|639|640|641))(9:658|(1:683)(1:662)|663|(1:682)(2:667|(1:669))|681|671|(2:673|(2:675|(1:677)(2:678|635)))(1:680)|679|(0)(0))))(4:685|(5:687|(2:689|(1:691))(2:693|(2:695|(1:697)))|692|640|641)(1:699)|698|630)|60)|98|(1:602)(2:102|(72:104|105|(3:107|(1:109)(1:599)|110)(1:600)|(3:112|(3:114|(1:116)(3:586|587|(3:589|(1:591)(1:593)|592))|117)(1:597)|594)(1:598)|(3:119|(1:125)|126)(1:585)|127|(3:580|(1:582)(1:584)|583)(2:130|131)|132|(1:134)|135|(1:137)(1:572)|138|(2:570|571)(1:142)|143|144|(3:147|(1:149)|(3:151|152|(59:156|157|158|(48:162|163|164|(1:558)(1:168)|169|(1:557)(1:172)|173|174|(1:556)|181|(1:555)(1:188)|189|(12:191|(1:193)(2:332|(3:334|335|60)(12:336|(1:(1:339)(1:340))(2:342|(1:344)(10:345|(1:350)(1:349)|195|(2:198|196)|199|200|(1:331)(1:203)|204|(1:206)(1:330)|207))|341|195|(1:196)|199|200|(0)|331|204|(0)(0)|207))|194|195|(1:196)|199|200|(0)|331|204|(0)(0)|207)(4:351|(6:353|(1:355)(4:360|(1:362)(2:548|(2:552|(3:365|(1:367)|368)(24:369|(1:371)|372|(3:543|(1:545)(1:547)|546)(1:378)|379|(18:(1:382)(2:539|(1:541))|383|(16:(2:386|(1:(2:389|(1:391))(1:533))(2:534|(11:536|393|394|(3:500|(1:530)(3:506|(2:528|529)(4:509|(1:513)|(1:527)(2:519|(1:523))|526)|524)|525)(1:398)|399|(7:401|(1:498)(7:414|(1:497)(3:418|(9:479|480|481|482|483|484|485|486|487)(1:420)|421)|422|(1:424)(1:478)|425|426|(10:460|461|462|463|464|465|466|467|468|(5:456|(1:458)|434|435|(2:440|(3:442|(2:447|448)(1:444)|(1:446)))))(4:428|(2:459|(0))|430|(0)))|432|433|434|435|(3:438|440|(0)))(1:499)|451|(3:455|358|359)|357|358|359)))(1:537)|531|532|394|(1:396)|500|(1:502)|530|525|399|(0)(0)|451|(4:453|455|358|359)|357|358|359)(1:538)|392|393|394|(0)|500|(0)|530|525|399|(0)(0)|451|(0)|357|358|359)|542|383|(0)(0)|392|393|394|(0)|500|(0)|530|525|399|(0)(0)|451|(0)|357|358|359)))|363|(0)(0))|356|357|358|359)|553|554)|208|(3:314|(4:316|(2:319|317)|320|321)(2:323|(1:325)(2:326|(1:328)(1:329)))|322)(1:212)|213|(1:215)|216|(1:218)|219|(2:221|(1:223)(1:309))(2:310|(1:312)(1:313))|(1:225)(1:308)|226|(4:228|(2:231|229)|232|233)(1:307)|234|(1:236)|237|238|239|(1:241)|242|(1:244)|(1:246)|(1:253)|254|(1:302)(1:260)|261|(1:263)|(1:265)|266|(3:271|(4:273|(3:275|(4:277|(1:279)|280|281)(2:283|284)|282)|285|286)|287)|288|(1:301)(2:291|(1:295))|296|(1:298)|299|300|60)|563|(1:166)|558|169|(0)|557|173|174|(1:176)|556|181|(1:184)|555|189|(0)(0)|208|(1:210)|314|(0)(0)|322|213|(0)|216|(0)|219|(0)(0)|(0)(0)|226|(0)(0)|234|(0)|237|238|239|(0)|242|(0)|(0)|(2:248|253)|254|(1:256)|302|261|(0)|(0)|266|(4:268|271|(0)|287)|288|(0)|301|296|(0)|299|300|60)))|569|563|(0)|558|169|(0)|557|173|174|(0)|556|181|(0)|555|189|(0)(0)|208|(0)|314|(0)(0)|322|213|(0)|216|(0)|219|(0)(0)|(0)(0)|226|(0)(0)|234|(0)|237|238|239|(0)|242|(0)|(0)|(0)|254|(0)|302|261|(0)|(0)|266|(0)|288|(0)|301|296|(0)|299|300|60))|601|105|(0)(0)|(0)(0)|(0)(0)|127|(0)|574|576|578|580|(0)(0)|583|132|(0)|135|(0)(0)|138|(1:140)|570|571|143|144|(3:147|(0)|(0))|569|563|(0)|558|169|(0)|557|173|174|(0)|556|181|(0)|555|189|(0)(0)|208|(0)|314|(0)(0)|322|213|(0)|216|(0)|219|(0)(0)|(0)(0)|226|(0)(0)|234|(0)|237|238|239|(0)|242|(0)|(0)|(0)|254|(0)|302|261|(0)|(0)|266|(0)|288|(0)|301|296|(0)|299|300|60) */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x03b1, code lost:
        if (r4.local_id != 0) goto L626;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x044b, code lost:
        if (r2.local_id != 0) goto L671;
     */
    /* JADX WARN: Code restructure failed: missing block: B:623:0x10ef, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:627:0x1101, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0316  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x03b9  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x03cb  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0486  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x0490  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x0554  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x056e  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0577  */
    /* JADX WARN: Removed duplicated region for block: B:233:0x05cf  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x05da  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x0605  */
    /* JADX WARN: Removed duplicated region for block: B:246:0x0615 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:258:0x067d  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x068e  */
    /* JADX WARN: Removed duplicated region for block: B:264:0x06c8  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x06d1  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x06d9  */
    /* JADX WARN: Removed duplicated region for block: B:271:0x06e8  */
    /* JADX WARN: Removed duplicated region for block: B:279:0x0719 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:282:0x0729  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x0783  */
    /* JADX WARN: Removed duplicated region for block: B:313:0x0793 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:319:0x07a8  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x07c0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:338:0x07ee  */
    /* JADX WARN: Removed duplicated region for block: B:363:0x092a A[LOOP:5: B:361:0x0922->B:363:0x092a, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:366:0x0946 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:371:0x0969  */
    /* JADX WARN: Removed duplicated region for block: B:373:0x097e  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x098b  */
    /* JADX WARN: Removed duplicated region for block: B:390:0x09fd  */
    /* JADX WARN: Removed duplicated region for block: B:394:0x0a2a  */
    /* JADX WARN: Removed duplicated region for block: B:420:0x0ab4  */
    /* JADX WARN: Removed duplicated region for block: B:436:0x0af9  */
    /* JADX WARN: Removed duplicated region for block: B:439:0x0b00  */
    /* JADX WARN: Removed duplicated region for block: B:445:0x0b22  */
    /* JADX WARN: Removed duplicated region for block: B:476:0x0ba8  */
    /* JADX WARN: Removed duplicated region for block: B:516:0x0cb4  */
    /* JADX WARN: Removed duplicated region for block: B:518:0x0cb8  */
    /* JADX WARN: Removed duplicated region for block: B:535:0x0cf2  */
    /* JADX WARN: Removed duplicated region for block: B:539:0x0d4c  */
    /* JADX WARN: Removed duplicated region for block: B:546:0x0d8b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:551:0x0d9d  */
    /* JADX WARN: Removed duplicated region for block: B:559:0x0de2  */
    /* JADX WARN: Removed duplicated region for block: B:562:0x0df8  */
    /* JADX WARN: Removed duplicated region for block: B:571:0x0e70  */
    /* JADX WARN: Removed duplicated region for block: B:576:0x0e8c  */
    /* JADX WARN: Removed duplicated region for block: B:582:0x0ebc  */
    /* JADX WARN: Removed duplicated region for block: B:591:0x0f08  */
    /* JADX WARN: Removed duplicated region for block: B:594:0x0f2b  */
    /* JADX WARN: Removed duplicated region for block: B:597:0x0f8b  */
    /* JADX WARN: Removed duplicated region for block: B:601:0x0fc6  */
    /* JADX WARN: Removed duplicated region for block: B:606:0x0fed  */
    /* JADX WARN: Removed duplicated region for block: B:607:0x1010  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x019c  */
    /* JADX WARN: Removed duplicated region for block: B:610:0x102d  */
    /* JADX WARN: Removed duplicated region for block: B:615:0x1056  */
    /* JADX WARN: Removed duplicated region for block: B:618:0x108e  */
    /* JADX WARN: Removed duplicated region for block: B:622:0x10e7 A[Catch: Exception -> 0x10ef, TryCatch #1 {Exception -> 0x10ef, blocks: (B:620:0x10c9, B:622:0x10e7, B:625:0x10f1), top: B:725:0x10c9 }] */
    /* JADX WARN: Removed duplicated region for block: B:629:0x1106  */
    /* JADX WARN: Removed duplicated region for block: B:631:0x1111  */
    /* JADX WARN: Removed duplicated region for block: B:633:0x1116  */
    /* JADX WARN: Removed duplicated region for block: B:641:0x112c  */
    /* JADX WARN: Removed duplicated region for block: B:649:0x1144  */
    /* JADX WARN: Removed duplicated region for block: B:651:0x114a  */
    /* JADX WARN: Removed duplicated region for block: B:654:0x1156  */
    /* JADX WARN: Removed duplicated region for block: B:659:0x1163  */
    /* JADX WARN: Removed duplicated region for block: B:672:0x11e8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:681:0x121a  */
    /* JADX WARN: Removed duplicated region for block: B:685:0x12bb  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x021e  */
    /* JADX WARN: Removed duplicated region for block: B:692:0x1307  */
    /* JADX WARN: Removed duplicated region for block: B:698:0x1320  */
    /* JADX WARN: Removed duplicated region for block: B:708:0x136f  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x025f  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x026a  */
    /* JADX WARN: Removed duplicated region for block: B:737:0x0cc1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:739:0x0733 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0287  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x02ab  */
    @SuppressLint({"InlinedApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showExtraNotifications(NotificationCompat.Builder builder, String str, long j, long j2, String str2, long[] jArr, int i, Uri uri, int i2, boolean z, boolean z2, boolean z3, int i3) {
        String str3;
        String str4;
        String str5;
        NotificationCompat.Builder builder2;
        int i4;
        boolean z4;
        long clientUserId;
        boolean z5;
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
        ArrayList arrayList2;
        Notification notification;
        ArrayList<StoryNotification> arrayList3;
        int id;
        int i8;
        MessageObject messageObject;
        LongSparseArray longSparseArray4;
        ArrayList arrayList4;
        long j3;
        long j4;
        boolean z6;
        LongSparseArray longSparseArray5;
        Integer num;
        int i9;
        int i10;
        ArrayList<StoryNotification> arrayList5;
        LongSparseArray longSparseArray6;
        String str6;
        Integer num2;
        long j5;
        TLRPC$User tLRPC$User;
        String string;
        TLRPC$User tLRPC$User2;
        boolean z7;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$FileLocation tLRPC$FileLocation;
        SharedPreferences sharedPreferences;
        NotificationsController notificationsController2;
        int i11;
        ArrayList arrayList6;
        Notification notification2;
        String str7;
        String str8;
        ArrayList arrayList7;
        int i12;
        boolean z8;
        LongSparseArray longSparseArray7;
        LongSparseArray longSparseArray8;
        long j6;
        String str9;
        boolean z9;
        boolean z10;
        TLRPC$FileLocation tLRPC$FileLocation2;
        TLRPC$FileLocation tLRPC$FileLocation3;
        TLRPC$User tLRPC$User3;
        TLRPC$TL_forumTopic findTopic;
        TLRPC$User tLRPC$User4;
        String str10;
        String str11;
        String str12;
        String str13;
        TLRPC$FileLocation tLRPC$FileLocation4;
        String str14;
        TLRPC$User tLRPC$User5;
        SharedPreferences sharedPreferences2;
        Bitmap bitmap;
        File file;
        Bitmap bitmap2;
        File file2;
        MessageObject messageObject2;
        LongSparseArray longSparseArray9;
        TLRPC$Chat tLRPC$Chat2;
        String str15;
        String str16;
        String str17;
        String formatString;
        NotificationCompat.Action build;
        Integer num3;
        int max;
        long j7;
        NotificationCompat.Action action;
        long j8;
        Person person;
        NotificationCompat.MessagingStyle messagingStyle;
        NotificationCompat.MessagingStyle messagingStyle2;
        int i13;
        MessageObject messageObject3;
        long j9;
        DialogKey dialogKey2;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList8;
        StringBuilder sb;
        String str18;
        ArrayList<StoryNotification> arrayList9;
        LongSparseArray longSparseArray10;
        NotificationCompat.MessagingStyle messagingStyle3;
        String str19;
        int i14;
        Bitmap bitmap3;
        String str20;
        String str21;
        String str22;
        String str23;
        ArrayList<StoryNotification> arrayList10;
        String str24;
        long j10;
        String str25;
        Person person2;
        String str26;
        String[] strArr;
        String str27;
        String str28;
        StringBuilder sb2;
        String str29;
        int i15;
        boolean z11;
        Person person3;
        File file3;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        TLRPC$FileLocation tLRPC$FileLocation5;
        LongSparseArray longSparseArray11;
        String str30;
        int i16;
        NotificationCompat.MessagingStyle messagingStyle4;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup;
        int id2;
        List<NotificationCompat.MessagingStyle.Message> messages;
        Uri uri2;
        String str31;
        File file4;
        final File file5;
        Context context;
        StringBuilder sb3;
        final Uri uriForFile;
        File file6;
        Bitmap createScaledBitmap;
        Canvas canvas;
        DialogKey dialogKey3;
        long j11;
        long j12;
        long j13;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList11;
        Bitmap bitmap4;
        NotificationCompat.Action build2;
        String str32;
        String str33;
        long j14;
        String str34;
        ArrayList<StoryNotification> arrayList12;
        long j15;
        NotificationCompat.Builder category;
        TLRPC$User tLRPC$User6;
        int size3;
        int i17;
        int i18;
        ArrayList<TLRPC$TL_keyboardButtonRow> arrayList13;
        TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow;
        LongSparseArray longSparseArray12;
        int i19;
        TLRPC$User user;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto2;
        TLRPC$FileLocation tLRPC$FileLocation6;
        Bitmap bitmap5;
        Bitmap decodeFile;
        String string2;
        TLRPC$User tLRPC$User7;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto3;
        int i20 = Build.VERSION.SDK_INT;
        if (i20 >= 26) {
            str3 = "NotificationMessageScheduledName";
            str4 = "%1$s: %2$s";
            i4 = -1;
            str5 = ".provider";
            builder2 = builder;
            builder2.setChannelId(validateChannelId(j, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
        } else {
            str3 = "NotificationMessageScheduledName";
            str4 = "%1$s: %2$s";
            str5 = ".provider";
            builder2 = builder;
            i4 = -1;
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
        LongSparseArray longSparseArray13 = new LongSparseArray();
        for (int i21 = 0; i21 < notificationsController3.pushMessages.size(); i21++) {
            MessageObject messageObject4 = notificationsController3.pushMessages.get(i21);
            long dialogId = messageObject4.getDialogId();
            long topicId = MessageObject.getTopicId(notificationsController3.currentAccount, messageObject4.messageOwner, getMessagesController().isForum(messageObject4));
            int i22 = notificationsSettings.getInt("dismissDate" + dialogId, 0);
            if (messageObject4.isStoryPush || messageObject4.messageOwner.date > i22) {
                ArrayList arrayList15 = (ArrayList) longSparseArray13.get(dialogId);
                if (arrayList15 == null) {
                    arrayList15 = new ArrayList();
                    longSparseArray13.put(dialogId, arrayList15);
                    arrayList14.add(new DialogKey(dialogId, topicId, false));
                }
                arrayList15.add(messageObject4);
            }
        }
        LongSparseArray longSparseArray14 = new LongSparseArray();
        for (int i23 = 0; i23 < notificationsController3.wearNotificationsIds.size(); i23++) {
            longSparseArray14.put(notificationsController3.wearNotificationsIds.keyAt(i23), notificationsController3.wearNotificationsIds.valueAt(i23));
        }
        notificationsController3.wearNotificationsIds.clear();
        ArrayList arrayList16 = new ArrayList();
        int i24 = Build.VERSION.SDK_INT;
        if (i24 > 27) {
            if (arrayList14.size() <= (notificationsController3.storyPushMessages.isEmpty() ? 1 : 2)) {
                z4 = false;
                if (z4 && i24 >= 26) {
                    checkOtherNotificationsChannel();
                }
                clientUserId = getUserConfig().getClientUserId();
                z5 = !AndroidUtilities.needShowPasscode() || SharedConfig.isWaitingForPasscodeEnter;
                SharedConfig.passcodeHash.length();
                longSparseArray = new LongSparseArray();
                size = arrayList14.size();
                i5 = 0;
                while (i5 < size && arrayList16.size() < 7) {
                    dialogKey = (DialogKey) arrayList14.get(i5);
                    int i25 = size;
                    if (!dialogKey.story) {
                        ArrayList<StoryNotification> arrayList17 = new ArrayList<>();
                        if (notificationsController3.storyPushMessages.isEmpty()) {
                            longSparseArray7 = longSparseArray;
                            j6 = clientUserId;
                            longSparseArray8 = longSparseArray14;
                            i12 = i5;
                            longSparseArray4 = longSparseArray13;
                            arrayList7 = arrayList14;
                            sharedPreferences = notificationsSettings;
                            notification2 = build3;
                            notificationsController2 = notificationsController3;
                            i11 = i25;
                            str9 = str3;
                            str7 = str4;
                            str8 = str5;
                            z8 = z4;
                            arrayList6 = arrayList16;
                            i5 = i12 + 1;
                            arrayList16 = arrayList6;
                            size = i11;
                            arrayList14 = arrayList7;
                            z4 = z8;
                            notificationsSettings = sharedPreferences;
                            longSparseArray13 = longSparseArray4;
                            clientUserId = j6;
                            str3 = str9;
                            longSparseArray14 = longSparseArray8;
                            str4 = str7;
                            str5 = str8;
                            longSparseArray = longSparseArray7;
                            build3 = notification2;
                            i4 = -1;
                            notificationsController3 = notificationsController2;
                        } else {
                            arrayList2 = arrayList16;
                            long j16 = notificationsController3.storyPushMessages.get(0).dialogId;
                            id = 0;
                            for (Integer num4 : notificationsController3.storyPushMessages.get(0).dateByIds.keySet()) {
                                id = Math.max(id, num4.intValue());
                            }
                            i8 = i5;
                            arrayList3 = arrayList17;
                            messageObject = null;
                            notification = build3;
                            j4 = j16;
                            longSparseArray4 = longSparseArray13;
                            arrayList4 = arrayList14;
                            j3 = 0;
                        }
                    } else {
                        arrayList2 = arrayList16;
                        long j17 = dialogKey.dialogId;
                        notification = build3;
                        long j18 = dialogKey.topicId;
                        arrayList3 = (ArrayList) longSparseArray13.get(j17);
                        id = ((MessageObject) arrayList3.get(0)).getId();
                        i8 = i5;
                        messageObject = (MessageObject) arrayList3.get(0);
                        longSparseArray4 = longSparseArray13;
                        arrayList4 = arrayList14;
                        j3 = j18;
                        j4 = j17;
                    }
                    Integer num5 = (Integer) longSparseArray14.get(j4);
                    z6 = z4;
                    int i26 = id;
                    if (dialogKey.story) {
                        if (num5 == null) {
                            longSparseArray5 = longSparseArray;
                            num5 = Integer.valueOf(((int) j4) + ((int) (j4 >> 32)));
                        } else {
                            longSparseArray5 = longSparseArray;
                            longSparseArray14.remove(j4);
                        }
                        num = num5;
                    } else {
                        num = 2147483646;
                        longSparseArray5 = longSparseArray;
                    }
                    int i27 = 0;
                    for (i9 = 0; i9 < arrayList3.size(); i9++) {
                        if (i27 < ((MessageObject) arrayList3.get(i9)).messageOwner.date) {
                            i27 = ((MessageObject) arrayList3.get(i9)).messageOwner.date;
                        }
                    }
                    if (!dialogKey.story) {
                        longSparseArray6 = longSparseArray14;
                        TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(j4));
                        i10 = i27;
                        if (notificationsController3.storyPushMessages.size() == 1) {
                            if (user2 != null) {
                                string = UserObject.getFirstName(user2);
                            } else {
                                string = notificationsController3.storyPushMessages.get(0).localName;
                            }
                            arrayList5 = arrayList3;
                        } else {
                            arrayList5 = arrayList3;
                            string = LocaleController.formatPluralString("Stories", notificationsController3.storyPushMessages.size(), new Object[0]);
                        }
                        if (user2 == null || (tLRPC$UserProfilePhoto3 = user2.photo) == null || (tLRPC$FileLocation = tLRPC$UserProfilePhoto3.photo_small) == null) {
                            tLRPC$User7 = user2;
                            str6 = "Stories";
                        } else {
                            tLRPC$User7 = user2;
                            str6 = "Stories";
                            if (tLRPC$FileLocation.volume_id != 0 && tLRPC$FileLocation.local_id != 0) {
                                j5 = clientUserId;
                                tLRPC$User2 = tLRPC$User7;
                                z7 = false;
                                z9 = false;
                                z10 = false;
                                num2 = num;
                                tLRPC$Chat = null;
                            }
                        }
                        j5 = clientUserId;
                        tLRPC$User2 = tLRPC$User7;
                        z7 = false;
                        tLRPC$FileLocation = null;
                        z9 = false;
                        z10 = false;
                        num2 = num;
                        tLRPC$Chat = null;
                    } else {
                        i10 = i27;
                        arrayList5 = arrayList3;
                        longSparseArray6 = longSparseArray14;
                        str6 = "Stories";
                        if (!DialogObject.isEncryptedDialog(j4)) {
                            z7 = (messageObject == null || messageObject.isReactionPush || messageObject.isStoryReactionPush || j4 == 777000) ? false : true;
                            if (DialogObject.isUserDialog(j4)) {
                                TLRPC$User user3 = getMessagesController().getUser(Long.valueOf(j4));
                                if (user3 == null) {
                                    if (messageObject.isFcmMessage()) {
                                        string = messageObject.localName;
                                    } else {
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.w("not found user to show dialog notification " + j4);
                                        }
                                        sharedPreferences = notificationsSettings;
                                        notificationsController2 = notificationsController3;
                                        i11 = i25;
                                        arrayList6 = arrayList2;
                                        notification2 = notification;
                                        str7 = str4;
                                        str8 = str5;
                                        arrayList7 = arrayList4;
                                        i12 = i8;
                                        z8 = z6;
                                        longSparseArray7 = longSparseArray5;
                                        longSparseArray8 = longSparseArray6;
                                        j6 = clientUserId;
                                        str9 = str3;
                                    }
                                } else {
                                    string = UserObject.getUserName(user3);
                                    TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto4 = user3.photo;
                                    if (tLRPC$UserProfilePhoto4 != null && (tLRPC$FileLocation = tLRPC$UserProfilePhoto4.photo_small) != null) {
                                        tLRPC$User4 = user3;
                                        num2 = num;
                                        if (tLRPC$FileLocation.volume_id != 0) {
                                        }
                                        tLRPC$FileLocation = null;
                                        if (!UserObject.isReplyUser(j4)) {
                                            string = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                                        } else if (j4 == clientUserId) {
                                            string = LocaleController.getString("MessageScheduledReminderNotification", R.string.MessageScheduledReminderNotification);
                                        } else {
                                            j5 = clientUserId;
                                            tLRPC$User3 = tLRPC$User4;
                                            tLRPC$Chat = null;
                                            z9 = false;
                                            z10 = false;
                                            tLRPC$User2 = tLRPC$User3;
                                        }
                                        j5 = clientUserId;
                                        tLRPC$User2 = tLRPC$User4;
                                        tLRPC$Chat = null;
                                        z9 = false;
                                        z10 = false;
                                    }
                                }
                                tLRPC$User4 = user3;
                                num2 = num;
                                tLRPC$FileLocation = null;
                                if (!UserObject.isReplyUser(j4)) {
                                }
                                j5 = clientUserId;
                                tLRPC$User2 = tLRPC$User4;
                                tLRPC$Chat = null;
                                z9 = false;
                                z10 = false;
                            } else {
                                num2 = num;
                                TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j4));
                                if (chat == null) {
                                    if (messageObject.isFcmMessage()) {
                                        boolean isSupergroup = messageObject.isSupergroup();
                                        string = messageObject.localName;
                                        z10 = isSupergroup;
                                        j5 = clientUserId;
                                        z7 = false;
                                        tLRPC$FileLocation = null;
                                        tLRPC$User2 = null;
                                        z9 = messageObject.localChannel;
                                        tLRPC$Chat = chat;
                                    } else {
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.w("not found chat to show dialog notification " + j4);
                                        }
                                        sharedPreferences = notificationsSettings;
                                        notificationsController2 = notificationsController3;
                                        i11 = i25;
                                        arrayList6 = arrayList2;
                                        notification2 = notification;
                                        str7 = str4;
                                        str8 = str5;
                                        arrayList7 = arrayList4;
                                        i12 = i8;
                                        z8 = z6;
                                        longSparseArray7 = longSparseArray5;
                                        longSparseArray8 = longSparseArray6;
                                        j6 = clientUserId;
                                        str9 = str3;
                                    }
                                } else {
                                    boolean z12 = chat.megagroup;
                                    boolean z13 = ChatObject.isChannel(chat) && !chat.megagroup;
                                    String str35 = chat.title;
                                    z10 = z12;
                                    TLRPC$ChatPhoto tLRPC$ChatPhoto = chat.photo;
                                    if (tLRPC$ChatPhoto == null || (tLRPC$FileLocation2 = tLRPC$ChatPhoto.photo_small) == null) {
                                        j5 = clientUserId;
                                    } else {
                                        j5 = clientUserId;
                                        if (tLRPC$FileLocation2.volume_id != 0) {
                                        }
                                    }
                                    tLRPC$FileLocation2 = null;
                                    if (j3 != 0) {
                                        tLRPC$FileLocation3 = tLRPC$FileLocation2;
                                        z9 = z13;
                                        if (getMessagesController().getTopicsController().findTopic(chat.id, j3) != null) {
                                            string = findTopic.title + " in " + str35;
                                            if (z7) {
                                                tLRPC$Chat = chat;
                                                tLRPC$FileLocation = tLRPC$FileLocation3;
                                                tLRPC$User3 = null;
                                                tLRPC$User2 = tLRPC$User3;
                                            } else {
                                                z7 = ChatObject.canSendPlain(chat);
                                                tLRPC$Chat = chat;
                                                tLRPC$FileLocation = tLRPC$FileLocation3;
                                                tLRPC$User2 = null;
                                            }
                                        }
                                    } else {
                                        tLRPC$FileLocation3 = tLRPC$FileLocation2;
                                        z9 = z13;
                                    }
                                    string = str35;
                                    if (z7) {
                                    }
                                }
                            }
                        } else {
                            num2 = num;
                            j5 = clientUserId;
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
                                sharedPreferences = notificationsSettings;
                                notificationsController2 = notificationsController3;
                                i11 = i25;
                                arrayList6 = arrayList2;
                                notification2 = notification;
                                str7 = str4;
                                str8 = str5;
                                arrayList7 = arrayList4;
                                i12 = i8;
                                z8 = z6;
                                longSparseArray7 = longSparseArray5;
                                longSparseArray8 = longSparseArray6;
                                j6 = j5;
                                str9 = str3;
                            } else {
                                tLRPC$User = null;
                            }
                            string = LocaleController.getString("SecretChatName", R.string.SecretChatName);
                            tLRPC$User2 = tLRPC$User;
                            z7 = false;
                            tLRPC$Chat = null;
                            tLRPC$FileLocation = null;
                            z9 = false;
                            z10 = false;
                        }
                        i5 = i12 + 1;
                        arrayList16 = arrayList6;
                        size = i11;
                        arrayList14 = arrayList7;
                        z4 = z8;
                        notificationsSettings = sharedPreferences;
                        longSparseArray13 = longSparseArray4;
                        clientUserId = j6;
                        str3 = str9;
                        longSparseArray14 = longSparseArray8;
                        str4 = str7;
                        str5 = str8;
                        longSparseArray = longSparseArray7;
                        build3 = notification2;
                        i4 = -1;
                        notificationsController3 = notificationsController2;
                    }
                    boolean z14 = z7;
                    if (messageObject == null && messageObject.isStoryReactionPush) {
                        str10 = string;
                        if (!notificationsSettings.getBoolean("EnableReactionsPreview", true)) {
                            str11 = LocaleController.getString("NotificationHiddenChatName", R.string.NotificationHiddenChatName);
                            tLRPC$FileLocation = null;
                            z14 = false;
                            if (z5) {
                                if (DialogObject.isChatDialog(j4)) {
                                    string2 = LocaleController.getString("NotificationHiddenChatName", R.string.NotificationHiddenChatName);
                                } else {
                                    string2 = LocaleController.getString("NotificationHiddenName", R.string.NotificationHiddenName);
                                }
                                str13 = string2;
                                str12 = str6;
                                tLRPC$FileLocation4 = null;
                                z14 = false;
                            } else {
                                str12 = str6;
                                TLRPC$FileLocation tLRPC$FileLocation7 = tLRPC$FileLocation;
                                str13 = str11;
                                tLRPC$FileLocation4 = tLRPC$FileLocation7;
                            }
                            if (tLRPC$FileLocation4 != null) {
                                sharedPreferences2 = notificationsSettings;
                                file = getFileLoader().getPathToAttach(tLRPC$FileLocation4, true);
                                tLRPC$User5 = tLRPC$User2;
                                if (Build.VERSION.SDK_INT < 28) {
                                    str14 = "NotificationHiddenName";
                                    bitmap5 = null;
                                    BitmapDrawable imageFromMemory = ImageLoader.getInstance().getImageFromMemory(tLRPC$FileLocation4, null, "50_50");
                                    if (imageFromMemory != null) {
                                        decodeFile = imageFromMemory.getBitmap();
                                    } else {
                                        try {
                                            if (file.exists()) {
                                                float dp = 160.0f / AndroidUtilities.dp(50.0f);
                                                BitmapFactory.Options options = new BitmapFactory.Options();
                                                options.inSampleSize = dp < 1.0f ? 1 : (int) dp;
                                                decodeFile = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                                            }
                                        } catch (Throwable unused) {
                                        }
                                    }
                                    bitmap = decodeFile;
                                } else {
                                    str14 = "NotificationHiddenName";
                                    bitmap5 = null;
                                }
                                bitmap = bitmap5;
                            } else {
                                str14 = "NotificationHiddenName";
                                tLRPC$User5 = tLRPC$User2;
                                sharedPreferences2 = notificationsSettings;
                                bitmap = null;
                                file = null;
                            }
                            if (tLRPC$Chat != null) {
                                Person.Builder name = new Person.Builder().setName(str13);
                                if (file != null && file.exists() && Build.VERSION.SDK_INT >= 28) {
                                    loadRoundAvatar(file, name);
                                }
                                file2 = file;
                                messageObject2 = messageObject;
                                bitmap2 = bitmap;
                                longSparseArray9 = longSparseArray5;
                                longSparseArray9.put(-tLRPC$Chat.id, name.build());
                            } else {
                                bitmap2 = bitmap;
                                file2 = file;
                                messageObject2 = messageObject;
                                longSparseArray9 = longSparseArray5;
                            }
                            File file7 = file2;
                            if (!(z9 || z10) || !z14 || SharedConfig.isWaitingForPasscodeEnter || j5 == j4 || UserObject.isReplyUser(j4)) {
                                str15 = "NotificationHiddenChatName";
                                tLRPC$Chat2 = tLRPC$Chat;
                                str16 = "max_id";
                                str17 = "dialog_id";
                                build = null;
                            } else {
                                tLRPC$Chat2 = tLRPC$Chat;
                                str15 = "NotificationHiddenChatName";
                                Intent intent = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                                intent.putExtra("dialog_id", j4);
                                intent.putExtra("max_id", i26);
                                intent.putExtra("topic_id", j3);
                                intent.putExtra("currentAccount", notificationsController3.currentAccount);
                                str16 = "max_id";
                                PendingIntent broadcast = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent, 167772160);
                                RemoteInput build4 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                                if (DialogObject.isChatDialog(j4)) {
                                    str17 = "dialog_id";
                                    formatString = LocaleController.formatString("ReplyToGroup", R.string.ReplyToGroup, str13);
                                } else {
                                    str17 = "dialog_id";
                                    formatString = LocaleController.formatString("ReplyToUser", R.string.ReplyToUser, str13);
                                }
                                build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build4).setShowsUserInterface(false).build();
                            }
                            num3 = notificationsController3.pushDialogs.get(j4);
                            if (num3 == null) {
                                num3 = 0;
                            }
                            if (dialogKey.story) {
                                max = notificationsController3.storyPushMessages.size();
                            } else {
                                max = Math.max(num3.intValue(), arrayList5.size());
                            }
                            String format = (max > 1 || Build.VERSION.SDK_INT >= 28) ? str13 : String.format("%1$s (%2$d)", str13, Integer.valueOf(max));
                            j7 = j5;
                            Person person4 = (Person) longSparseArray9.get(j7);
                            action = build;
                            if (Build.VERSION.SDK_INT >= 28 && person4 == null) {
                                user = getMessagesController().getUser(Long.valueOf(j7));
                                if (user == null) {
                                    user = getUserConfig().getCurrentUser();
                                }
                                if (user != null) {
                                    try {
                                        tLRPC$UserProfilePhoto2 = user.photo;
                                    } catch (Throwable th) {
                                        th = th;
                                        j8 = j3;
                                    }
                                    if (tLRPC$UserProfilePhoto2 != null && (tLRPC$FileLocation6 = tLRPC$UserProfilePhoto2.photo_small) != null) {
                                        j8 = j3;
                                        try {
                                        } catch (Throwable th2) {
                                            th = th2;
                                        }
                                        if (tLRPC$FileLocation6.volume_id != 0 && tLRPC$FileLocation6.local_id != 0) {
                                            Person.Builder name2 = new Person.Builder().setName(LocaleController.getString("FromYou", R.string.FromYou));
                                            loadRoundAvatar(getFileLoader().getPathToAttach(user.photo.photo_small, true), name2);
                                            person = name2.build();
                                            try {
                                                longSparseArray9.put(j7, person);
                                            } catch (Throwable th3) {
                                                th = th3;
                                                person4 = person;
                                                FileLog.e(th);
                                                person = person4;
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
                                                messagingStyle2.setGroupConversation(i13 >= 28 || (!z9 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                                                StringBuilder sb4 = new StringBuilder();
                                                String[] strArr2 = new String[1];
                                                messageObject3 = messageObject2;
                                                boolean[] zArr = new boolean[1];
                                                if (!dialogKey.story) {
                                                }
                                                Intent intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                                intent2.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                                intent2.setFlags(ConnectionsManager.FileTypeFile);
                                                intent2.addCategory("android.intent.category.LAUNCHER");
                                                if (messageObject3 == null) {
                                                }
                                                dialogKey3 = dialogKey2;
                                                if (!dialogKey3.story) {
                                                }
                                                j12 = j11;
                                                StringBuilder sb5 = new StringBuilder();
                                                sb5.append("show extra notifications chatId ");
                                                sb5.append(j12);
                                                sb5.append(" topicId ");
                                                j13 = j8;
                                                sb5.append(j13);
                                                FileLog.d(sb5.toString());
                                                if (j13 != 0) {
                                                }
                                                intent2.putExtra("currentAccount", notificationsController3.currentAccount);
                                                String str36 = str19;
                                                PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1140850688);
                                                NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
                                                if (action != null) {
                                                }
                                                int i28 = i14;
                                                Intent intent3 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                                intent3.addFlags(32);
                                                intent3.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                                intent3.putExtra(str17, j12);
                                                intent3.putExtra(str16, i26);
                                                intent3.putExtra("currentAccount", notificationsController3.currentAccount);
                                                arrayList11 = arrayList8;
                                                bitmap4 = bitmap3;
                                                build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent3, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                                if (DialogObject.isEncryptedDialog(j12)) {
                                                }
                                                if (str33 == null) {
                                                }
                                                StringBuilder sb6 = new StringBuilder();
                                                sb6.append("tgaccount");
                                                long j19 = j9;
                                                sb6.append(j19);
                                                wearableExtender.setBridgeTag(sb6.toString());
                                                if (!dialogKey3.story) {
                                                }
                                                String str37 = str34;
                                                NotificationCompat.Builder autoCancel = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str37).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                                if (dialogKey3.story) {
                                                }
                                                category = autoCancel.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j15).setShowWhen(true).setStyle(messagingStyle3).setContentIntent(activity).extend(wearableExtender).setSortKey(String.valueOf(Long.MAX_VALUE - j15)).setCategory("msg");
                                                Intent intent4 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                                intent4.putExtra("messageDate", i10);
                                                intent4.putExtra("dialogId", j12);
                                                intent4.putExtra("currentAccount", notificationsController3.currentAccount);
                                                if (dialogKey3.story) {
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
                                                if (DialogObject.isEncryptedDialog(j12)) {
                                                }
                                                if (bitmap4 != null) {
                                                }
                                                if (!AndroidUtilities.needShowPasscode(false)) {
                                                }
                                                if (tLRPC$Chat2 == null) {
                                                }
                                                tLRPC$User6 = tLRPC$User5;
                                                Notification notification3 = notification;
                                                if (Build.VERSION.SDK_INT >= 26) {
                                                }
                                                i11 = i25;
                                                j6 = j14;
                                                z8 = z6;
                                                str9 = str3;
                                                longSparseArray7 = longSparseArray10;
                                                str8 = str36;
                                                i12 = i8;
                                                longSparseArray8 = longSparseArray6;
                                                str7 = str18;
                                                arrayList7 = arrayList4;
                                                sharedPreferences = sharedPreferences2;
                                                notification2 = notification3;
                                                arrayList6 = arrayList2;
                                                arrayList6.add(new 1NotificationHolder(num2.intValue(), j12, dialogKey3.story, j13, str37, tLRPC$User6, tLRPC$Chat2, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                                notificationsController2 = this;
                                                notificationsController2.wearNotificationsIds.put(j12, num2);
                                                i5 = i12 + 1;
                                                arrayList16 = arrayList6;
                                                size = i11;
                                                arrayList14 = arrayList7;
                                                z4 = z8;
                                                notificationsSettings = sharedPreferences;
                                                longSparseArray13 = longSparseArray4;
                                                clientUserId = j6;
                                                str3 = str9;
                                                longSparseArray14 = longSparseArray8;
                                                str4 = str7;
                                                str5 = str8;
                                                longSparseArray = longSparseArray7;
                                                build3 = notification2;
                                                i4 = -1;
                                                notificationsController3 = notificationsController2;
                                            }
                                            boolean z15 = (messageObject2 == null && (messageObject2.messageOwner.action instanceof TLRPC$TL_messageActionChatJoinedByRequest)) ? false : true;
                                            if (person == null && z15) {
                                                messagingStyle = new NotificationCompat.MessagingStyle(person);
                                            } else {
                                                messagingStyle = new NotificationCompat.MessagingStyle("");
                                            }
                                            messagingStyle2 = messagingStyle;
                                            i13 = Build.VERSION.SDK_INT;
                                            if (i13 >= 28 || ((DialogObject.isChatDialog(j4) && !z9) || UserObject.isReplyUser(j4))) {
                                                messagingStyle2.setConversationTitle(format);
                                            }
                                            messagingStyle2.setGroupConversation(i13 >= 28 || (!z9 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                                            StringBuilder sb42 = new StringBuilder();
                                            String[] strArr22 = new String[1];
                                            messageObject3 = messageObject2;
                                            boolean[] zArr2 = new boolean[1];
                                            if (!dialogKey.story) {
                                                ArrayList<String> arrayList18 = new ArrayList<>();
                                                ArrayList<Object> arrayList19 = new ArrayList<>();
                                                Pair<Integer, Boolean> parseStoryPushes = notificationsController3.parseStoryPushes(arrayList18, arrayList19);
                                                int intValue = ((Integer) parseStoryPushes.first).intValue();
                                                boolean booleanValue = ((Boolean) parseStoryPushes.second).booleanValue();
                                                if (booleanValue) {
                                                    sb42.append(LocaleController.formatPluralString("StoryNotificationHidden", intValue, new Object[0]));
                                                } else if (arrayList18.isEmpty()) {
                                                    longSparseArray7 = longSparseArray9;
                                                    notificationsController2 = notificationsController3;
                                                    i11 = i25;
                                                    arrayList6 = arrayList2;
                                                    notification2 = notification;
                                                    str9 = str3;
                                                    str7 = str4;
                                                    str8 = str5;
                                                    arrayList7 = arrayList4;
                                                    i12 = i8;
                                                    z8 = z6;
                                                    longSparseArray8 = longSparseArray6;
                                                    sharedPreferences = sharedPreferences2;
                                                    j6 = j7;
                                                    i5 = i12 + 1;
                                                    arrayList16 = arrayList6;
                                                    size = i11;
                                                    arrayList14 = arrayList7;
                                                    z4 = z8;
                                                    notificationsSettings = sharedPreferences;
                                                    longSparseArray13 = longSparseArray4;
                                                    clientUserId = j6;
                                                    str3 = str9;
                                                    longSparseArray14 = longSparseArray8;
                                                    str4 = str7;
                                                    str5 = str8;
                                                    longSparseArray = longSparseArray7;
                                                    build3 = notification2;
                                                    i4 = -1;
                                                    notificationsController3 = notificationsController2;
                                                } else {
                                                    if (arrayList18.size() != 1) {
                                                        dialogKey2 = dialogKey;
                                                        if (arrayList18.size() == 2) {
                                                            longSparseArray12 = longSparseArray9;
                                                            sb42.append(LocaleController.formatString(R.string.StoryNotification2, arrayList18.get(0), arrayList18.get(1)));
                                                        } else {
                                                            longSparseArray12 = longSparseArray9;
                                                            if (arrayList18.size() == 3 && notificationsController3.storyPushMessages.size() == 3) {
                                                                j9 = j7;
                                                                sb42.append(LocaleController.formatString(R.string.StoryNotification3, notificationsController3.cutLastName(arrayList18.get(0)), notificationsController3.cutLastName(arrayList18.get(1)), notificationsController3.cutLastName(arrayList18.get(2))));
                                                            } else {
                                                                j9 = j7;
                                                                sb42.append(LocaleController.formatPluralString("StoryNotification4", notificationsController3.storyPushMessages.size() - 2, notificationsController3.cutLastName(arrayList18.get(0)), notificationsController3.cutLastName(arrayList18.get(1))));
                                                            }
                                                            long j20 = Long.MAX_VALUE;
                                                            for (i19 = 0; i19 < notificationsController3.storyPushMessages.size(); i19++) {
                                                                j20 = Math.min(notificationsController3.storyPushMessages.get(i19).date, j20);
                                                            }
                                                            messagingStyle2.setGroupConversation(false);
                                                            String formatPluralString = (arrayList18.size() == 1 || booleanValue) ? LocaleController.formatPluralString(str12, intValue, new Object[0]) : arrayList18.get(0);
                                                            messagingStyle2.addMessage(sb42, j20, new Person.Builder().setName(formatPluralString).build());
                                                            if (booleanValue) {
                                                                str20 = formatPluralString;
                                                                sb = sb42;
                                                                str18 = str4;
                                                                str19 = str5;
                                                                arrayList9 = arrayList5;
                                                                longSparseArray10 = longSparseArray12;
                                                                bitmap3 = null;
                                                            } else {
                                                                bitmap3 = loadMultipleAvatars(arrayList19);
                                                                str20 = formatPluralString;
                                                                sb = sb42;
                                                                str18 = str4;
                                                                str19 = str5;
                                                                arrayList9 = arrayList5;
                                                                longSparseArray10 = longSparseArray12;
                                                            }
                                                            arrayList8 = null;
                                                            messagingStyle3 = messagingStyle2;
                                                            i14 = 0;
                                                        }
                                                    } else if (intValue == 1) {
                                                        sb42.append(LocaleController.getString("StoryNotificationSingle"));
                                                    } else {
                                                        dialogKey2 = dialogKey;
                                                        sb42.append(LocaleController.formatPluralString("StoryNotification1", intValue, arrayList18.get(0)));
                                                        longSparseArray12 = longSparseArray9;
                                                    }
                                                    j9 = j7;
                                                    long j202 = Long.MAX_VALUE;
                                                    while (i19 < notificationsController3.storyPushMessages.size()) {
                                                    }
                                                    messagingStyle2.setGroupConversation(false);
                                                    if (arrayList18.size() == 1) {
                                                    }
                                                    messagingStyle2.addMessage(sb42, j202, new Person.Builder().setName(formatPluralString).build());
                                                    if (booleanValue) {
                                                    }
                                                    arrayList8 = null;
                                                    messagingStyle3 = messagingStyle2;
                                                    i14 = 0;
                                                }
                                                longSparseArray12 = longSparseArray9;
                                                j9 = j7;
                                                dialogKey2 = dialogKey;
                                                long j2022 = Long.MAX_VALUE;
                                                while (i19 < notificationsController3.storyPushMessages.size()) {
                                                }
                                                messagingStyle2.setGroupConversation(false);
                                                if (arrayList18.size() == 1) {
                                                }
                                                messagingStyle2.addMessage(sb42, j2022, new Person.Builder().setName(formatPluralString).build());
                                                if (booleanValue) {
                                                }
                                                arrayList8 = null;
                                                messagingStyle3 = messagingStyle2;
                                                i14 = 0;
                                            } else {
                                                LongSparseArray longSparseArray15 = longSparseArray9;
                                                j9 = j7;
                                                dialogKey2 = dialogKey;
                                                int size4 = arrayList5.size() - 1;
                                                int i29 = 0;
                                                arrayList8 = null;
                                                while (size4 >= 0) {
                                                    ArrayList<StoryNotification> arrayList20 = arrayList5;
                                                    MessageObject messageObject5 = (MessageObject) arrayList20.get(size4);
                                                    int i30 = i29;
                                                    ArrayList<TLRPC$TL_keyboardButtonRow> arrayList21 = arrayList8;
                                                    if (j8 != MessageObject.getTopicId(notificationsController3.currentAccount, messageObject5.messageOwner, getMessagesController().isForum(messageObject5))) {
                                                        sb2 = sb42;
                                                        str21 = str13;
                                                    } else {
                                                        String shortStringForMessage = notificationsController3.getShortStringForMessage(messageObject5, strArr22, zArr2);
                                                        if (j4 == j9) {
                                                            strArr22[0] = str13;
                                                        } else if (DialogObject.isChatDialog(j4) && messageObject5.messageOwner.from_scheduled) {
                                                            str21 = str13;
                                                            str22 = str3;
                                                            strArr22[0] = LocaleController.getString(str22, R.string.NotificationMessageScheduledName);
                                                            if (shortStringForMessage != null) {
                                                                if (BuildVars.LOGS_ENABLED) {
                                                                    FileLog.w("message text is null for " + messageObject5.getId() + " did = " + messageObject5.getDialogId());
                                                                }
                                                                sb2 = sb42;
                                                                str3 = str22;
                                                            } else {
                                                                if (sb42.length() > 0) {
                                                                    sb42.append("\n\n");
                                                                }
                                                                if (j4 != j9 && messageObject5.messageOwner.from_scheduled && DialogObject.isUserDialog(j4)) {
                                                                    str3 = str22;
                                                                    str23 = str4;
                                                                    String format2 = String.format(str23, LocaleController.getString(str22, R.string.NotificationMessageScheduledName), shortStringForMessage);
                                                                    sb42.append(format2);
                                                                    str24 = format2;
                                                                    arrayList10 = arrayList20;
                                                                } else {
                                                                    str3 = str22;
                                                                    str23 = str4;
                                                                    String str38 = strArr22[0];
                                                                    if (str38 != null) {
                                                                        arrayList10 = arrayList20;
                                                                        sb42.append(String.format(str23, str38, shortStringForMessage));
                                                                    } else {
                                                                        arrayList10 = arrayList20;
                                                                        sb42.append(shortStringForMessage);
                                                                    }
                                                                    str24 = shortStringForMessage;
                                                                }
                                                                if (!DialogObject.isUserDialog(j4)) {
                                                                    if (z9) {
                                                                        j10 = -j4;
                                                                    } else if (DialogObject.isChatDialog(j4)) {
                                                                        j10 = messageObject5.getSenderId();
                                                                    }
                                                                    NotificationCompat.MessagingStyle messagingStyle5 = messagingStyle2;
                                                                    str25 = str23;
                                                                    LongSparseArray longSparseArray16 = longSparseArray15;
                                                                    person2 = (Person) longSparseArray16.get(j10 + (j8 << 16));
                                                                    str26 = strArr22[0];
                                                                    if (str26 != null) {
                                                                        if (!z5) {
                                                                            strArr = strArr22;
                                                                        } else if (!DialogObject.isChatDialog(j4)) {
                                                                            strArr = strArr22;
                                                                            if (Build.VERSION.SDK_INT > 27) {
                                                                                str27 = str14;
                                                                                str26 = LocaleController.getString(str27, R.string.NotificationHiddenName);
                                                                                str28 = str26;
                                                                                str14 = str27;
                                                                                if (person2 == null && TextUtils.equals(person2.getName(), str28)) {
                                                                                    person3 = person2;
                                                                                    sb2 = sb42;
                                                                                    str29 = str24;
                                                                                    i15 = size4;
                                                                                    z11 = z9;
                                                                                } else {
                                                                                    Person.Builder name3 = new Person.Builder().setName(str28);
                                                                                    if (zArr2[0] || DialogObject.isEncryptedDialog(j4) || Build.VERSION.SDK_INT < 28) {
                                                                                        sb2 = sb42;
                                                                                        str29 = str24;
                                                                                        i15 = size4;
                                                                                        z11 = z9;
                                                                                    } else {
                                                                                        if (DialogObject.isUserDialog(j4) || z9) {
                                                                                            sb2 = sb42;
                                                                                            str29 = str24;
                                                                                            i15 = size4;
                                                                                            z11 = z9;
                                                                                            file3 = file7;
                                                                                        } else {
                                                                                            long senderId = messageObject5.getSenderId();
                                                                                            z11 = z9;
                                                                                            sb2 = sb42;
                                                                                            TLRPC$User user4 = getMessagesController().getUser(Long.valueOf(senderId));
                                                                                            if (user4 == null && (user4 = getMessagesStorage().getUserSync(senderId)) != null) {
                                                                                                getMessagesController().putUser(user4, true);
                                                                                            }
                                                                                            if (user4 == null || (tLRPC$UserProfilePhoto = user4.photo) == null || (tLRPC$FileLocation5 = tLRPC$UserProfilePhoto.photo_small) == null) {
                                                                                                str29 = str24;
                                                                                                i15 = size4;
                                                                                            } else {
                                                                                                str29 = str24;
                                                                                                i15 = size4;
                                                                                                if (tLRPC$FileLocation5.volume_id != 0 && tLRPC$FileLocation5.local_id != 0) {
                                                                                                    file3 = getFileLoader().getPathToAttach(user4.photo.photo_small, true);
                                                                                                }
                                                                                            }
                                                                                            file3 = null;
                                                                                        }
                                                                                        loadRoundAvatar(file3, name3);
                                                                                    }
                                                                                    Person build5 = name3.build();
                                                                                    longSparseArray16.put(j10, build5);
                                                                                    person3 = build5;
                                                                                }
                                                                                if (DialogObject.isEncryptedDialog(j4)) {
                                                                                    if (!zArr2[0] || Build.VERSION.SDK_INT < 28 || ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).isLowRamDevice() || z5 || messageObject5.isSecretMedia() || !(messageObject5.type == 1 || messageObject5.isSticker())) {
                                                                                        longSparseArray11 = longSparseArray16;
                                                                                        str30 = str5;
                                                                                        i16 = i15;
                                                                                    } else {
                                                                                        File pathToMessage = getFileLoader().getPathToMessage(messageObject5.messageOwner);
                                                                                        if (pathToMessage.exists() && messageObject5.hasMediaSpoilers()) {
                                                                                            file5 = new File(pathToMessage.getParentFile(), pathToMessage.getName() + ".blur.jpg");
                                                                                            if (file5.exists()) {
                                                                                                file6 = pathToMessage;
                                                                                                str31 = str29;
                                                                                            } else {
                                                                                                try {
                                                                                                    Bitmap decodeFile2 = BitmapFactory.decodeFile(pathToMessage.getAbsolutePath());
                                                                                                    Bitmap stackBlurBitmapMax = Utilities.stackBlurBitmapMax(decodeFile2);
                                                                                                    decodeFile2.recycle();
                                                                                                    createScaledBitmap = Bitmap.createScaledBitmap(stackBlurBitmapMax, decodeFile2.getWidth(), decodeFile2.getHeight(), true);
                                                                                                    Utilities.stackBlurBitmap(createScaledBitmap, 5);
                                                                                                    stackBlurBitmapMax.recycle();
                                                                                                    canvas = new Canvas(createScaledBitmap);
                                                                                                    str31 = str29;
                                                                                                    try {
                                                                                                        notificationsController3.mediaSpoilerEffect.setColor(ColorUtils.setAlphaComponent(-1, (int) (Color.alpha(i4) * 0.325f)));
                                                                                                        file6 = pathToMessage;
                                                                                                    } catch (Exception e) {
                                                                                                        e = e;
                                                                                                        file6 = pathToMessage;
                                                                                                    }
                                                                                                } catch (Exception e2) {
                                                                                                    e = e2;
                                                                                                    file6 = pathToMessage;
                                                                                                    str31 = str29;
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
                                                                                                    str29 = str31;
                                                                                                    NotificationCompat.MessagingStyle.Message message = new NotificationCompat.MessagingStyle.Message(str29, messageObject5.messageOwner.date * 1000, person3);
                                                                                                    String str39 = !messageObject5.isSticker() ? "image/webp" : "image/jpeg";
                                                                                                    if (!file4.exists()) {
                                                                                                    }
                                                                                                    if (j4 == 777000) {
                                                                                                    }
                                                                                                    id2 = i30;
                                                                                                    arrayList8 = arrayList21;
                                                                                                    size4 = i16 - 1;
                                                                                                    messagingStyle2 = messagingStyle4;
                                                                                                    str5 = str30;
                                                                                                    arrayList5 = arrayList10;
                                                                                                    z9 = z11;
                                                                                                    str13 = str21;
                                                                                                    str4 = str25;
                                                                                                    strArr22 = strArr;
                                                                                                    sb42 = sb2;
                                                                                                    longSparseArray15 = longSparseArray11;
                                                                                                    i4 = -1;
                                                                                                    i29 = id2;
                                                                                                }
                                                                                            }
                                                                                            file4 = file6;
                                                                                        } else {
                                                                                            str31 = str29;
                                                                                            file4 = pathToMessage;
                                                                                            file5 = null;
                                                                                        }
                                                                                        str29 = str31;
                                                                                        NotificationCompat.MessagingStyle.Message message2 = new NotificationCompat.MessagingStyle.Message(str29, messageObject5.messageOwner.date * 1000, person3);
                                                                                        String str392 = !messageObject5.isSticker() ? "image/webp" : "image/jpeg";
                                                                                        if (!file4.exists()) {
                                                                                            try {
                                                                                                context = ApplicationLoader.applicationContext;
                                                                                                sb3 = new StringBuilder();
                                                                                                longSparseArray11 = longSparseArray16;
                                                                                            } catch (Exception e4) {
                                                                                                e = e4;
                                                                                                longSparseArray11 = longSparseArray16;
                                                                                            }
                                                                                            try {
                                                                                                sb3.append(ApplicationLoader.getApplicationId());
                                                                                                str30 = str5;
                                                                                            } catch (Exception e5) {
                                                                                                e = e5;
                                                                                                str30 = str5;
                                                                                                FileLog.e(e);
                                                                                                i16 = i15;
                                                                                                uriForFile = null;
                                                                                                if (uriForFile != null) {
                                                                                                }
                                                                                                messagingStyle4 = messagingStyle5;
                                                                                                messagingStyle4.addMessage(str29, messageObject5.messageOwner.date * 1000, person3);
                                                                                                if (zArr2[0]) {
                                                                                                }
                                                                                                if (j4 == 777000) {
                                                                                                }
                                                                                                id2 = i30;
                                                                                                arrayList8 = arrayList21;
                                                                                                size4 = i16 - 1;
                                                                                                messagingStyle2 = messagingStyle4;
                                                                                                str5 = str30;
                                                                                                arrayList5 = arrayList10;
                                                                                                z9 = z11;
                                                                                                str13 = str21;
                                                                                                str4 = str25;
                                                                                                strArr22 = strArr;
                                                                                                sb42 = sb2;
                                                                                                longSparseArray15 = longSparseArray11;
                                                                                                i4 = -1;
                                                                                                i29 = id2;
                                                                                            }
                                                                                            try {
                                                                                                sb3.append(str30);
                                                                                                uriForFile = FileProvider.getUriForFile(context, sb3.toString(), file4);
                                                                                                i16 = i15;
                                                                                            } catch (Exception e6) {
                                                                                                e = e6;
                                                                                                FileLog.e(e);
                                                                                                i16 = i15;
                                                                                                uriForFile = null;
                                                                                                if (uriForFile != null) {
                                                                                                }
                                                                                                messagingStyle4 = messagingStyle5;
                                                                                                messagingStyle4.addMessage(str29, messageObject5.messageOwner.date * 1000, person3);
                                                                                                if (zArr2[0]) {
                                                                                                }
                                                                                                if (j4 == 777000) {
                                                                                                }
                                                                                                id2 = i30;
                                                                                                arrayList8 = arrayList21;
                                                                                                size4 = i16 - 1;
                                                                                                messagingStyle2 = messagingStyle4;
                                                                                                str5 = str30;
                                                                                                arrayList5 = arrayList10;
                                                                                                z9 = z11;
                                                                                                str13 = str21;
                                                                                                str4 = str25;
                                                                                                strArr22 = strArr;
                                                                                                sb42 = sb2;
                                                                                                longSparseArray15 = longSparseArray11;
                                                                                                i4 = -1;
                                                                                                i29 = id2;
                                                                                            }
                                                                                            if (uriForFile != null) {
                                                                                                message2.setData(str392, uriForFile);
                                                                                                messagingStyle4 = messagingStyle5;
                                                                                                messagingStyle4.addMessage(message2);
                                                                                                ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", uriForFile, 1);
                                                                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda55
                                                                                                    @Override // java.lang.Runnable
                                                                                                    public final void run() {
                                                                                                        NotificationsController.lambda$showExtraNotifications$42(uriForFile, file5);
                                                                                                    }
                                                                                                }, 20000L);
                                                                                                if (!TextUtils.isEmpty(messageObject5.caption)) {
                                                                                                    messagingStyle4.addMessage(messageObject5.caption, messageObject5.messageOwner.date * 1000, person3);
                                                                                                }
                                                                                                if (zArr2[0] && !z5 && messageObject5.isVoice()) {
                                                                                                    messages = messagingStyle4.getMessages();
                                                                                                    if (!messages.isEmpty()) {
                                                                                                        File pathToMessage2 = getFileLoader().getPathToMessage(messageObject5.messageOwner);
                                                                                                        if (Build.VERSION.SDK_INT >= 24) {
                                                                                                            try {
                                                                                                                uri2 = FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + str30, pathToMessage2);
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
                                                                                        } else {
                                                                                            longSparseArray11 = longSparseArray16;
                                                                                            str30 = str5;
                                                                                            if (getFileLoader().isLoadingFile(file4.getName())) {
                                                                                                Uri.Builder appendPath = new Uri.Builder().scheme("content").authority(NotificationImageProvider.getAuthority()).appendPath("msg_media_raw");
                                                                                                StringBuilder sb7 = new StringBuilder();
                                                                                                i16 = i15;
                                                                                                sb7.append(notificationsController3.currentAccount);
                                                                                                sb7.append("");
                                                                                                uriForFile = appendPath.appendPath(sb7.toString()).appendPath(file4.getName()).appendQueryParameter("final_path", file4.getAbsolutePath()).build();
                                                                                                if (uriForFile != null) {
                                                                                                }
                                                                                            }
                                                                                            i16 = i15;
                                                                                            uriForFile = null;
                                                                                            if (uriForFile != null) {
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    messagingStyle4 = messagingStyle5;
                                                                                    messagingStyle4.addMessage(str29, messageObject5.messageOwner.date * 1000, person3);
                                                                                    if (zArr2[0]) {
                                                                                        messages = messagingStyle4.getMessages();
                                                                                        if (!messages.isEmpty()) {
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    longSparseArray11 = longSparseArray16;
                                                                                    str30 = str5;
                                                                                    i16 = i15;
                                                                                    messagingStyle4 = messagingStyle5;
                                                                                    messagingStyle4.addMessage(str29, messageObject5.messageOwner.date * 1000, person3);
                                                                                }
                                                                                if (j4 == 777000 && (tLRPC$ReplyMarkup = messageObject5.messageOwner.reply_markup) != null) {
                                                                                    arrayList8 = tLRPC$ReplyMarkup.rows;
                                                                                    id2 = messageObject5.getId();
                                                                                    size4 = i16 - 1;
                                                                                    messagingStyle2 = messagingStyle4;
                                                                                    str5 = str30;
                                                                                    arrayList5 = arrayList10;
                                                                                    z9 = z11;
                                                                                    str13 = str21;
                                                                                    str4 = str25;
                                                                                    strArr22 = strArr;
                                                                                    sb42 = sb2;
                                                                                    longSparseArray15 = longSparseArray11;
                                                                                    i4 = -1;
                                                                                    i29 = id2;
                                                                                }
                                                                                id2 = i30;
                                                                                arrayList8 = arrayList21;
                                                                                size4 = i16 - 1;
                                                                                messagingStyle2 = messagingStyle4;
                                                                                str5 = str30;
                                                                                arrayList5 = arrayList10;
                                                                                z9 = z11;
                                                                                str13 = str21;
                                                                                str4 = str25;
                                                                                strArr22 = strArr;
                                                                                sb42 = sb2;
                                                                                longSparseArray15 = longSparseArray11;
                                                                                i4 = -1;
                                                                                i29 = id2;
                                                                            }
                                                                        } else if (z9) {
                                                                            strArr = strArr22;
                                                                            if (Build.VERSION.SDK_INT > 27) {
                                                                                str26 = LocaleController.getString(str15, R.string.NotificationHiddenChatName);
                                                                            }
                                                                        } else {
                                                                            strArr = strArr22;
                                                                            str26 = LocaleController.getString("NotificationHiddenChatUserName", R.string.NotificationHiddenChatUserName);
                                                                        }
                                                                        str27 = str14;
                                                                        str28 = "";
                                                                        str14 = str27;
                                                                        if (person2 == null) {
                                                                        }
                                                                        Person.Builder name32 = new Person.Builder().setName(str28);
                                                                        if (zArr2[0]) {
                                                                        }
                                                                        sb2 = sb42;
                                                                        str29 = str24;
                                                                        i15 = size4;
                                                                        z11 = z9;
                                                                        Person build52 = name32.build();
                                                                        longSparseArray16.put(j10, build52);
                                                                        person3 = build52;
                                                                        if (DialogObject.isEncryptedDialog(j4)) {
                                                                        }
                                                                        if (j4 == 777000) {
                                                                            arrayList8 = tLRPC$ReplyMarkup.rows;
                                                                            id2 = messageObject5.getId();
                                                                            size4 = i16 - 1;
                                                                            messagingStyle2 = messagingStyle4;
                                                                            str5 = str30;
                                                                            arrayList5 = arrayList10;
                                                                            z9 = z11;
                                                                            str13 = str21;
                                                                            str4 = str25;
                                                                            strArr22 = strArr;
                                                                            sb42 = sb2;
                                                                            longSparseArray15 = longSparseArray11;
                                                                            i4 = -1;
                                                                            i29 = id2;
                                                                        }
                                                                        id2 = i30;
                                                                        arrayList8 = arrayList21;
                                                                        size4 = i16 - 1;
                                                                        messagingStyle2 = messagingStyle4;
                                                                        str5 = str30;
                                                                        arrayList5 = arrayList10;
                                                                        z9 = z11;
                                                                        str13 = str21;
                                                                        str4 = str25;
                                                                        strArr22 = strArr;
                                                                        sb42 = sb2;
                                                                        longSparseArray15 = longSparseArray11;
                                                                        i4 = -1;
                                                                        i29 = id2;
                                                                    } else {
                                                                        strArr = strArr22;
                                                                    }
                                                                    str27 = str14;
                                                                    str28 = str26;
                                                                    str14 = str27;
                                                                    if (person2 == null) {
                                                                    }
                                                                    Person.Builder name322 = new Person.Builder().setName(str28);
                                                                    if (zArr2[0]) {
                                                                    }
                                                                    sb2 = sb42;
                                                                    str29 = str24;
                                                                    i15 = size4;
                                                                    z11 = z9;
                                                                    Person build522 = name322.build();
                                                                    longSparseArray16.put(j10, build522);
                                                                    person3 = build522;
                                                                    if (DialogObject.isEncryptedDialog(j4)) {
                                                                    }
                                                                    if (j4 == 777000) {
                                                                    }
                                                                    id2 = i30;
                                                                    arrayList8 = arrayList21;
                                                                    size4 = i16 - 1;
                                                                    messagingStyle2 = messagingStyle4;
                                                                    str5 = str30;
                                                                    arrayList5 = arrayList10;
                                                                    z9 = z11;
                                                                    str13 = str21;
                                                                    str4 = str25;
                                                                    strArr22 = strArr;
                                                                    sb42 = sb2;
                                                                    longSparseArray15 = longSparseArray11;
                                                                    i4 = -1;
                                                                    i29 = id2;
                                                                }
                                                                j10 = j4;
                                                                NotificationCompat.MessagingStyle messagingStyle52 = messagingStyle2;
                                                                str25 = str23;
                                                                LongSparseArray longSparseArray162 = longSparseArray15;
                                                                person2 = (Person) longSparseArray162.get(j10 + (j8 << 16));
                                                                str26 = strArr22[0];
                                                                if (str26 != null) {
                                                                }
                                                                str27 = str14;
                                                                str28 = str26;
                                                                str14 = str27;
                                                                if (person2 == null) {
                                                                }
                                                                Person.Builder name3222 = new Person.Builder().setName(str28);
                                                                if (zArr2[0]) {
                                                                }
                                                                sb2 = sb42;
                                                                str29 = str24;
                                                                i15 = size4;
                                                                z11 = z9;
                                                                Person build5222 = name3222.build();
                                                                longSparseArray162.put(j10, build5222);
                                                                person3 = build5222;
                                                                if (DialogObject.isEncryptedDialog(j4)) {
                                                                }
                                                                if (j4 == 777000) {
                                                                }
                                                                id2 = i30;
                                                                arrayList8 = arrayList21;
                                                                size4 = i16 - 1;
                                                                messagingStyle2 = messagingStyle4;
                                                                str5 = str30;
                                                                arrayList5 = arrayList10;
                                                                z9 = z11;
                                                                str13 = str21;
                                                                str4 = str25;
                                                                strArr22 = strArr;
                                                                sb42 = sb2;
                                                                longSparseArray15 = longSparseArray11;
                                                                i4 = -1;
                                                                i29 = id2;
                                                            }
                                                        }
                                                        str21 = str13;
                                                        str22 = str3;
                                                        if (shortStringForMessage != null) {
                                                        }
                                                    }
                                                    strArr = strArr22;
                                                    str25 = str4;
                                                    str30 = str5;
                                                    longSparseArray11 = longSparseArray15;
                                                    messagingStyle4 = messagingStyle2;
                                                    i16 = size4;
                                                    z11 = z9;
                                                    arrayList10 = arrayList20;
                                                    id2 = i30;
                                                    arrayList8 = arrayList21;
                                                    size4 = i16 - 1;
                                                    messagingStyle2 = messagingStyle4;
                                                    str5 = str30;
                                                    arrayList5 = arrayList10;
                                                    z9 = z11;
                                                    str13 = str21;
                                                    str4 = str25;
                                                    strArr22 = strArr;
                                                    sb42 = sb2;
                                                    longSparseArray15 = longSparseArray11;
                                                    i4 = -1;
                                                    i29 = id2;
                                                }
                                                sb = sb42;
                                                str18 = str4;
                                                arrayList9 = arrayList5;
                                                longSparseArray10 = longSparseArray15;
                                                messagingStyle3 = messagingStyle2;
                                                int i31 = i29;
                                                str19 = str5;
                                                i14 = i31;
                                                bitmap3 = bitmap2;
                                                str20 = str13;
                                            }
                                            Intent intent22 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                            intent22.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                            intent22.setFlags(ConnectionsManager.FileTypeFile);
                                            intent22.addCategory("android.intent.category.LAUNCHER");
                                            if (messageObject3 == null && messageObject3.isStoryReactionPush) {
                                                intent22.putExtra("storyId", Math.abs(messageObject3.getId()));
                                                j12 = j4;
                                                dialogKey3 = dialogKey2;
                                            } else {
                                                dialogKey3 = dialogKey2;
                                                if (!dialogKey3.story) {
                                                    long[] jArr2 = new long[notificationsController3.storyPushMessages.size()];
                                                    int i32 = 0;
                                                    while (i32 < notificationsController3.storyPushMessages.size()) {
                                                        jArr2[i32] = notificationsController3.storyPushMessages.get(i32).dialogId;
                                                        i32++;
                                                        j4 = j4;
                                                    }
                                                    j11 = j4;
                                                    intent22.putExtra("storyDialogIds", jArr2);
                                                } else {
                                                    j11 = j4;
                                                    if (DialogObject.isEncryptedDialog(j11)) {
                                                        intent22.putExtra("encId", DialogObject.getEncryptedChatId(j11));
                                                    } else if (DialogObject.isUserDialog(j11)) {
                                                        j12 = j11;
                                                        intent22.putExtra("userId", j12);
                                                    } else {
                                                        j12 = j11;
                                                        intent22.putExtra("chatId", -j12);
                                                    }
                                                }
                                                j12 = j11;
                                            }
                                            StringBuilder sb52 = new StringBuilder();
                                            sb52.append("show extra notifications chatId ");
                                            sb52.append(j12);
                                            sb52.append(" topicId ");
                                            j13 = j8;
                                            sb52.append(j13);
                                            FileLog.d(sb52.toString());
                                            if (j13 != 0) {
                                                intent22.putExtra("topicId", j13);
                                            }
                                            intent22.putExtra("currentAccount", notificationsController3.currentAccount);
                                            String str362 = str19;
                                            PendingIntent activity2 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22, 1140850688);
                                            NotificationCompat.WearableExtender wearableExtender2 = new NotificationCompat.WearableExtender();
                                            if (action != null) {
                                                wearableExtender2.addAction(action);
                                            }
                                            int i282 = i14;
                                            Intent intent32 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                            intent32.addFlags(32);
                                            intent32.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                            intent32.putExtra(str17, j12);
                                            intent32.putExtra(str16, i26);
                                            intent32.putExtra("currentAccount", notificationsController3.currentAccount);
                                            arrayList11 = arrayList8;
                                            bitmap4 = bitmap3;
                                            build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent32, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                            if (DialogObject.isEncryptedDialog(j12)) {
                                                if (DialogObject.isUserDialog(j12)) {
                                                    str33 = "tguser" + j12 + "_" + i26;
                                                    str32 = str20;
                                                } else {
                                                    StringBuilder sb8 = new StringBuilder();
                                                    sb8.append("tgchat");
                                                    str32 = str20;
                                                    sb8.append(-j12);
                                                    sb8.append("_");
                                                    sb8.append(i26);
                                                    str33 = sb8.toString();
                                                }
                                            } else {
                                                str32 = str20;
                                                str33 = j12 != globalSecretChatId ? "tgenc" + DialogObject.getEncryptedChatId(j12) + "_" + i26 : null;
                                            }
                                            if (str33 == null) {
                                                wearableExtender2.setDismissalId(str33);
                                                NotificationCompat.WearableExtender wearableExtender3 = new NotificationCompat.WearableExtender();
                                                wearableExtender3.setDismissalId("summary_" + str33);
                                                builder.extend(wearableExtender3);
                                            }
                                            StringBuilder sb62 = new StringBuilder();
                                            sb62.append("tgaccount");
                                            long j192 = j9;
                                            sb62.append(j192);
                                            wearableExtender2.setBridgeTag(sb62.toString());
                                            if (!dialogKey3.story) {
                                                j14 = j192;
                                                j15 = Long.MAX_VALUE;
                                                int i33 = 0;
                                                while (i33 < notificationsController3.storyPushMessages.size()) {
                                                    j15 = Math.min(notificationsController3.storyPushMessages.get(i33).date, j15);
                                                    i33++;
                                                    str32 = str32;
                                                }
                                                str34 = str32;
                                                arrayList12 = arrayList9;
                                            } else {
                                                j14 = j192;
                                                str34 = str32;
                                                arrayList12 = arrayList9;
                                                j15 = ((MessageObject) arrayList12.get(0)).messageOwner.date * 1000;
                                            }
                                            String str372 = str34;
                                            NotificationCompat.Builder autoCancel2 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str372).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                            if (dialogKey3.story) {
                                                arrayList12 = notificationsController3.storyPushMessages;
                                            }
                                            category = autoCancel2.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j15).setShowWhen(true).setStyle(messagingStyle3).setContentIntent(activity2).extend(wearableExtender2).setSortKey(String.valueOf(Long.MAX_VALUE - j15)).setCategory("msg");
                                            Intent intent42 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                            intent42.putExtra("messageDate", i10);
                                            intent42.putExtra("dialogId", j12);
                                            intent42.putExtra("currentAccount", notificationsController3.currentAccount);
                                            if (dialogKey3.story) {
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
                                            if (!z5 && !dialogKey3.story && (messageObject3 == null || !messageObject3.isStoryReactionPush)) {
                                                category.addAction(build2);
                                            }
                                            if (arrayList4.size() != 1 && !TextUtils.isEmpty(str) && !dialogKey3.story) {
                                                category.setSubText(str);
                                            }
                                            if (DialogObject.isEncryptedDialog(j12)) {
                                                category.setLocalOnly(true);
                                            }
                                            if (bitmap4 != null) {
                                                category.setLargeIcon(bitmap4);
                                            }
                                            if (!AndroidUtilities.needShowPasscode(false) && !SharedConfig.isWaitingForPasscodeEnter && arrayList11 != null) {
                                                size3 = arrayList11.size();
                                                i17 = 0;
                                                while (i17 < size3) {
                                                    ArrayList<TLRPC$TL_keyboardButtonRow> arrayList22 = arrayList11;
                                                    TLRPC$TL_keyboardButtonRow tLRPC$TL_keyboardButtonRow2 = arrayList22.get(i17);
                                                    int size5 = tLRPC$TL_keyboardButtonRow2.buttons.size();
                                                    int i34 = 0;
                                                    while (i34 < size5) {
                                                        TLRPC$KeyboardButton tLRPC$KeyboardButton = tLRPC$TL_keyboardButtonRow2.buttons.get(i34);
                                                        if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonCallback) {
                                                            i18 = size3;
                                                            arrayList13 = arrayList22;
                                                            Intent intent5 = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                                            intent5.putExtra("currentAccount", notificationsController3.currentAccount);
                                                            intent5.putExtra("did", j12);
                                                            byte[] bArr = tLRPC$KeyboardButton.data;
                                                            if (bArr != null) {
                                                                intent5.putExtra("data", bArr);
                                                            }
                                                            intent5.putExtra("mid", i282);
                                                            String str40 = tLRPC$KeyboardButton.text;
                                                            Context context2 = ApplicationLoader.applicationContext;
                                                            int i35 = notificationsController3.lastButtonId;
                                                            tLRPC$TL_keyboardButtonRow = tLRPC$TL_keyboardButtonRow2;
                                                            notificationsController3.lastButtonId = i35 + 1;
                                                            category.addAction(0, str40, PendingIntent.getBroadcast(context2, i35, intent5, 167772160));
                                                        } else {
                                                            i18 = size3;
                                                            arrayList13 = arrayList22;
                                                            tLRPC$TL_keyboardButtonRow = tLRPC$TL_keyboardButtonRow2;
                                                        }
                                                        i34++;
                                                        size3 = i18;
                                                        arrayList22 = arrayList13;
                                                        tLRPC$TL_keyboardButtonRow2 = tLRPC$TL_keyboardButtonRow;
                                                    }
                                                    i17++;
                                                    arrayList11 = arrayList22;
                                                }
                                            }
                                            if (tLRPC$Chat2 == null || tLRPC$User5 == null) {
                                                tLRPC$User6 = tLRPC$User5;
                                            } else {
                                                tLRPC$User6 = tLRPC$User5;
                                                String str41 = tLRPC$User6.phone;
                                                if (str41 != null && str41.length() > 0) {
                                                    category.addPerson("tel:+" + tLRPC$User6.phone);
                                                }
                                            }
                                            Notification notification32 = notification;
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                notificationsController3.setNotificationChannel(notification32, category, z6);
                                            }
                                            i11 = i25;
                                            j6 = j14;
                                            z8 = z6;
                                            str9 = str3;
                                            longSparseArray7 = longSparseArray10;
                                            str8 = str362;
                                            i12 = i8;
                                            longSparseArray8 = longSparseArray6;
                                            str7 = str18;
                                            arrayList7 = arrayList4;
                                            sharedPreferences = sharedPreferences2;
                                            notification2 = notification32;
                                            arrayList6 = arrayList2;
                                            arrayList6.add(new 1NotificationHolder(num2.intValue(), j12, dialogKey3.story, j13, str372, tLRPC$User6, tLRPC$Chat2, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                            notificationsController2 = this;
                                            notificationsController2.wearNotificationsIds.put(j12, num2);
                                            i5 = i12 + 1;
                                            arrayList16 = arrayList6;
                                            size = i11;
                                            arrayList14 = arrayList7;
                                            z4 = z8;
                                            notificationsSettings = sharedPreferences;
                                            longSparseArray13 = longSparseArray4;
                                            clientUserId = j6;
                                            str3 = str9;
                                            longSparseArray14 = longSparseArray8;
                                            str4 = str7;
                                            str5 = str8;
                                            longSparseArray = longSparseArray7;
                                            build3 = notification2;
                                            i4 = -1;
                                            notificationsController3 = notificationsController2;
                                        }
                                        person = person4;
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
                                        messagingStyle2.setGroupConversation(i13 >= 28 || (!z9 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                                        StringBuilder sb422 = new StringBuilder();
                                        String[] strArr222 = new String[1];
                                        messageObject3 = messageObject2;
                                        boolean[] zArr22 = new boolean[1];
                                        if (!dialogKey.story) {
                                        }
                                        Intent intent222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                        intent222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                                        intent222.setFlags(ConnectionsManager.FileTypeFile);
                                        intent222.addCategory("android.intent.category.LAUNCHER");
                                        if (messageObject3 == null) {
                                        }
                                        dialogKey3 = dialogKey2;
                                        if (!dialogKey3.story) {
                                        }
                                        j12 = j11;
                                        StringBuilder sb522 = new StringBuilder();
                                        sb522.append("show extra notifications chatId ");
                                        sb522.append(j12);
                                        sb522.append(" topicId ");
                                        j13 = j8;
                                        sb522.append(j13);
                                        FileLog.d(sb522.toString());
                                        if (j13 != 0) {
                                        }
                                        intent222.putExtra("currentAccount", notificationsController3.currentAccount);
                                        String str3622 = str19;
                                        PendingIntent activity22 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222, 1140850688);
                                        NotificationCompat.WearableExtender wearableExtender22 = new NotificationCompat.WearableExtender();
                                        if (action != null) {
                                        }
                                        int i2822 = i14;
                                        Intent intent322 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                                        intent322.addFlags(32);
                                        intent322.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                        intent322.putExtra(str17, j12);
                                        intent322.putExtra(str16, i26);
                                        intent322.putExtra("currentAccount", notificationsController3.currentAccount);
                                        arrayList11 = arrayList8;
                                        bitmap4 = bitmap3;
                                        build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent322, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                                        if (DialogObject.isEncryptedDialog(j12)) {
                                        }
                                        if (str33 == null) {
                                        }
                                        StringBuilder sb622 = new StringBuilder();
                                        sb622.append("tgaccount");
                                        long j1922 = j9;
                                        sb622.append(j1922);
                                        wearableExtender22.setBridgeTag(sb622.toString());
                                        if (!dialogKey3.story) {
                                        }
                                        String str3722 = str34;
                                        NotificationCompat.Builder autoCancel22 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str3722).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                                        if (dialogKey3.story) {
                                        }
                                        category = autoCancel22.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j15).setShowWhen(true).setStyle(messagingStyle3).setContentIntent(activity22).extend(wearableExtender22).setSortKey(String.valueOf(Long.MAX_VALUE - j15)).setCategory("msg");
                                        Intent intent422 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                                        intent422.putExtra("messageDate", i10);
                                        intent422.putExtra("dialogId", j12);
                                        intent422.putExtra("currentAccount", notificationsController3.currentAccount);
                                        if (dialogKey3.story) {
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
                                        if (DialogObject.isEncryptedDialog(j12)) {
                                        }
                                        if (bitmap4 != null) {
                                        }
                                        if (!AndroidUtilities.needShowPasscode(false)) {
                                            size3 = arrayList11.size();
                                            i17 = 0;
                                            while (i17 < size3) {
                                            }
                                        }
                                        if (tLRPC$Chat2 == null) {
                                        }
                                        tLRPC$User6 = tLRPC$User5;
                                        Notification notification322 = notification;
                                        if (Build.VERSION.SDK_INT >= 26) {
                                        }
                                        i11 = i25;
                                        j6 = j14;
                                        z8 = z6;
                                        str9 = str3;
                                        longSparseArray7 = longSparseArray10;
                                        str8 = str3622;
                                        i12 = i8;
                                        longSparseArray8 = longSparseArray6;
                                        str7 = str18;
                                        arrayList7 = arrayList4;
                                        sharedPreferences = sharedPreferences2;
                                        notification2 = notification322;
                                        arrayList6 = arrayList2;
                                        arrayList6.add(new 1NotificationHolder(num2.intValue(), j12, dialogKey3.story, j13, str3722, tLRPC$User6, tLRPC$Chat2, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                                        notificationsController2 = this;
                                        notificationsController2.wearNotificationsIds.put(j12, num2);
                                        i5 = i12 + 1;
                                        arrayList16 = arrayList6;
                                        size = i11;
                                        arrayList14 = arrayList7;
                                        z4 = z8;
                                        notificationsSettings = sharedPreferences;
                                        longSparseArray13 = longSparseArray4;
                                        clientUserId = j6;
                                        str3 = str9;
                                        longSparseArray14 = longSparseArray8;
                                        str4 = str7;
                                        str5 = str8;
                                        longSparseArray = longSparseArray7;
                                        build3 = notification2;
                                        i4 = -1;
                                        notificationsController3 = notificationsController2;
                                    }
                                }
                            }
                            j8 = j3;
                            person = person4;
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
                            messagingStyle2.setGroupConversation(i13 >= 28 || (!z9 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                            StringBuilder sb4222 = new StringBuilder();
                            String[] strArr2222 = new String[1];
                            messageObject3 = messageObject2;
                            boolean[] zArr222 = new boolean[1];
                            if (!dialogKey.story) {
                            }
                            Intent intent2222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                            intent2222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                            intent2222.setFlags(ConnectionsManager.FileTypeFile);
                            intent2222.addCategory("android.intent.category.LAUNCHER");
                            if (messageObject3 == null) {
                            }
                            dialogKey3 = dialogKey2;
                            if (!dialogKey3.story) {
                            }
                            j12 = j11;
                            StringBuilder sb5222 = new StringBuilder();
                            sb5222.append("show extra notifications chatId ");
                            sb5222.append(j12);
                            sb5222.append(" topicId ");
                            j13 = j8;
                            sb5222.append(j13);
                            FileLog.d(sb5222.toString());
                            if (j13 != 0) {
                            }
                            intent2222.putExtra("currentAccount", notificationsController3.currentAccount);
                            String str36222 = str19;
                            PendingIntent activity222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2222, 1140850688);
                            NotificationCompat.WearableExtender wearableExtender222 = new NotificationCompat.WearableExtender();
                            if (action != null) {
                            }
                            int i28222 = i14;
                            Intent intent3222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                            intent3222.addFlags(32);
                            intent3222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                            intent3222.putExtra(str17, j12);
                            intent3222.putExtra(str16, i26);
                            intent3222.putExtra("currentAccount", notificationsController3.currentAccount);
                            arrayList11 = arrayList8;
                            bitmap4 = bitmap3;
                            build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent3222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                            if (DialogObject.isEncryptedDialog(j12)) {
                            }
                            if (str33 == null) {
                            }
                            StringBuilder sb6222 = new StringBuilder();
                            sb6222.append("tgaccount");
                            long j19222 = j9;
                            sb6222.append(j19222);
                            wearableExtender222.setBridgeTag(sb6222.toString());
                            if (!dialogKey3.story) {
                            }
                            String str37222 = str34;
                            NotificationCompat.Builder autoCancel222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str37222).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                            if (dialogKey3.story) {
                            }
                            category = autoCancel222.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j15).setShowWhen(true).setStyle(messagingStyle3).setContentIntent(activity222).extend(wearableExtender222).setSortKey(String.valueOf(Long.MAX_VALUE - j15)).setCategory("msg");
                            Intent intent4222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                            intent4222.putExtra("messageDate", i10);
                            intent4222.putExtra("dialogId", j12);
                            intent4222.putExtra("currentAccount", notificationsController3.currentAccount);
                            if (dialogKey3.story) {
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
                            if (DialogObject.isEncryptedDialog(j12)) {
                            }
                            if (bitmap4 != null) {
                            }
                            if (!AndroidUtilities.needShowPasscode(false)) {
                            }
                            if (tLRPC$Chat2 == null) {
                            }
                            tLRPC$User6 = tLRPC$User5;
                            Notification notification3222 = notification;
                            if (Build.VERSION.SDK_INT >= 26) {
                            }
                            i11 = i25;
                            j6 = j14;
                            z8 = z6;
                            str9 = str3;
                            longSparseArray7 = longSparseArray10;
                            str8 = str36222;
                            i12 = i8;
                            longSparseArray8 = longSparseArray6;
                            str7 = str18;
                            arrayList7 = arrayList4;
                            sharedPreferences = sharedPreferences2;
                            notification2 = notification3222;
                            arrayList6 = arrayList2;
                            arrayList6.add(new 1NotificationHolder(num2.intValue(), j12, dialogKey3.story, j13, str37222, tLRPC$User6, tLRPC$Chat2, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                            notificationsController2 = this;
                            notificationsController2.wearNotificationsIds.put(j12, num2);
                            i5 = i12 + 1;
                            arrayList16 = arrayList6;
                            size = i11;
                            arrayList14 = arrayList7;
                            z4 = z8;
                            notificationsSettings = sharedPreferences;
                            longSparseArray13 = longSparseArray4;
                            clientUserId = j6;
                            str3 = str9;
                            longSparseArray14 = longSparseArray8;
                            str4 = str7;
                            str5 = str8;
                            longSparseArray = longSparseArray7;
                            build3 = notification2;
                            i4 = -1;
                            notificationsController3 = notificationsController2;
                        }
                    } else {
                        str10 = string;
                    }
                    str11 = str10;
                    if (z5) {
                    }
                    if (tLRPC$FileLocation4 != null) {
                    }
                    if (tLRPC$Chat != null) {
                    }
                    File file72 = file2;
                    if (z9) {
                    }
                    tLRPC$Chat2 = tLRPC$Chat;
                    str15 = "NotificationHiddenChatName";
                    Intent intent6 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                    intent6.putExtra("dialog_id", j4);
                    intent6.putExtra("max_id", i26);
                    intent6.putExtra("topic_id", j3);
                    intent6.putExtra("currentAccount", notificationsController3.currentAccount);
                    str16 = "max_id";
                    PendingIntent broadcast2 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent6, 167772160);
                    RemoteInput build42 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                    if (DialogObject.isChatDialog(j4)) {
                    }
                    build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast2).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build42).setShowsUserInterface(false).build();
                    num3 = notificationsController3.pushDialogs.get(j4);
                    if (num3 == null) {
                    }
                    if (dialogKey.story) {
                    }
                    if (max > 1) {
                    }
                    j7 = j5;
                    Person person42 = (Person) longSparseArray9.get(j7);
                    action = build;
                    if (Build.VERSION.SDK_INT >= 28) {
                        user = getMessagesController().getUser(Long.valueOf(j7));
                        if (user == null) {
                        }
                        if (user != null) {
                        }
                    }
                    j8 = j3;
                    person = person42;
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
                    messagingStyle2.setGroupConversation(i13 >= 28 || (!z9 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
                    StringBuilder sb42222 = new StringBuilder();
                    String[] strArr22222 = new String[1];
                    messageObject3 = messageObject2;
                    boolean[] zArr2222 = new boolean[1];
                    if (!dialogKey.story) {
                    }
                    Intent intent22222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    intent22222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                    intent22222.setFlags(ConnectionsManager.FileTypeFile);
                    intent22222.addCategory("android.intent.category.LAUNCHER");
                    if (messageObject3 == null) {
                    }
                    dialogKey3 = dialogKey2;
                    if (!dialogKey3.story) {
                    }
                    j12 = j11;
                    StringBuilder sb52222 = new StringBuilder();
                    sb52222.append("show extra notifications chatId ");
                    sb52222.append(j12);
                    sb52222.append(" topicId ");
                    j13 = j8;
                    sb52222.append(j13);
                    FileLog.d(sb52222.toString());
                    if (j13 != 0) {
                    }
                    intent22222.putExtra("currentAccount", notificationsController3.currentAccount);
                    String str362222 = str19;
                    PendingIntent activity2222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent22222, 1140850688);
                    NotificationCompat.WearableExtender wearableExtender2222 = new NotificationCompat.WearableExtender();
                    if (action != null) {
                    }
                    int i282222 = i14;
                    Intent intent32222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                    intent32222.addFlags(32);
                    intent32222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                    intent32222.putExtra(str17, j12);
                    intent32222.putExtra(str16, i26);
                    intent32222.putExtra("currentAccount", notificationsController3.currentAccount);
                    arrayList11 = arrayList8;
                    bitmap4 = bitmap3;
                    build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent32222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
                    if (DialogObject.isEncryptedDialog(j12)) {
                    }
                    if (str33 == null) {
                    }
                    StringBuilder sb62222 = new StringBuilder();
                    sb62222.append("tgaccount");
                    long j192222 = j9;
                    sb62222.append(j192222);
                    wearableExtender2222.setBridgeTag(sb62222.toString());
                    if (!dialogKey3.story) {
                    }
                    String str372222 = str34;
                    NotificationCompat.Builder autoCancel2222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str372222).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
                    if (dialogKey3.story) {
                    }
                    category = autoCancel2222.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j15).setShowWhen(true).setStyle(messagingStyle3).setContentIntent(activity2222).extend(wearableExtender2222).setSortKey(String.valueOf(Long.MAX_VALUE - j15)).setCategory("msg");
                    Intent intent42222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
                    intent42222.putExtra("messageDate", i10);
                    intent42222.putExtra("dialogId", j12);
                    intent42222.putExtra("currentAccount", notificationsController3.currentAccount);
                    if (dialogKey3.story) {
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
                    if (DialogObject.isEncryptedDialog(j12)) {
                    }
                    if (bitmap4 != null) {
                    }
                    if (!AndroidUtilities.needShowPasscode(false)) {
                    }
                    if (tLRPC$Chat2 == null) {
                    }
                    tLRPC$User6 = tLRPC$User5;
                    Notification notification32222 = notification;
                    if (Build.VERSION.SDK_INT >= 26) {
                    }
                    i11 = i25;
                    j6 = j14;
                    z8 = z6;
                    str9 = str3;
                    longSparseArray7 = longSparseArray10;
                    str8 = str362222;
                    i12 = i8;
                    longSparseArray8 = longSparseArray6;
                    str7 = str18;
                    arrayList7 = arrayList4;
                    sharedPreferences = sharedPreferences2;
                    notification2 = notification32222;
                    arrayList6 = arrayList2;
                    arrayList6.add(new 1NotificationHolder(num2.intValue(), j12, dialogKey3.story, j13, str372222, tLRPC$User6, tLRPC$Chat2, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
                    notificationsController2 = this;
                    notificationsController2.wearNotificationsIds.put(j12, num2);
                    i5 = i12 + 1;
                    arrayList16 = arrayList6;
                    size = i11;
                    arrayList14 = arrayList7;
                    z4 = z8;
                    notificationsSettings = sharedPreferences;
                    longSparseArray13 = longSparseArray4;
                    clientUserId = j6;
                    str3 = str9;
                    longSparseArray14 = longSparseArray8;
                    str4 = str7;
                    str5 = str8;
                    longSparseArray = longSparseArray7;
                    build3 = notification2;
                    i4 = -1;
                    notificationsController3 = notificationsController2;
                }
                LongSparseArray longSparseArray17 = longSparseArray;
                longSparseArray2 = longSparseArray14;
                Notification notification4 = build3;
                NotificationsController notificationsController4 = notificationsController3;
                ArrayList arrayList23 = arrayList16;
                if (!z4) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("show summary with id " + notificationsController4.notificationId);
                    }
                    try {
                        notificationManager.notify(notificationsController4.notificationId, notification4);
                        notificationsController = notificationsController4;
                        arrayList = arrayList23;
                    } catch (SecurityException e7) {
                        FileLog.e(e7);
                        notificationsController = this;
                        arrayList = arrayList23;
                        notificationsController.resetNotificationSound(builder, j, j2, str2, jArr, i, uri, i2, z, z2, z3, i3);
                    }
                } else {
                    notificationsController = notificationsController4;
                    arrayList = arrayList23;
                    if (notificationsController.openedInBubbleDialogs.isEmpty()) {
                        notificationManager.cancel(notificationsController.notificationId);
                    }
                }
                i6 = 0;
                while (i6 < longSparseArray2.size()) {
                    LongSparseArray longSparseArray18 = longSparseArray2;
                    if (!notificationsController.openedInBubbleDialogs.contains(Long.valueOf(longSparseArray18.keyAt(i6)))) {
                        Integer num6 = (Integer) longSparseArray18.valueAt(i6);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("cancel notification id " + num6);
                        }
                        notificationManager.cancel(num6.intValue());
                    }
                    i6++;
                    longSparseArray2 = longSparseArray18;
                }
                ArrayList arrayList24 = new ArrayList(arrayList.size());
                size2 = arrayList.size();
                i7 = 0;
                while (i7 < size2) {
                    ArrayList arrayList25 = arrayList;
                    1NotificationHolder r4 = (1NotificationHolder) arrayList25.get(i7);
                    arrayList24.clear();
                    if (Build.VERSION.SDK_INT < 29 || DialogObject.isEncryptedDialog(r4.dialogId)) {
                        longSparseArray3 = longSparseArray17;
                    } else {
                        NotificationCompat.Builder builder3 = r4.notification;
                        long j21 = r4.dialogId;
                        longSparseArray3 = longSparseArray17;
                        String createNotificationShortcut = createNotificationShortcut(builder3, j21, r4.name, r4.user, r4.chat, (Person) longSparseArray3.get(j21), !r4.story);
                        if (createNotificationShortcut != null) {
                            arrayList24.add(createNotificationShortcut);
                        }
                    }
                    r4.call();
                    if (!unsupportedNotificationShortcut() && !arrayList24.isEmpty()) {
                        ShortcutManagerCompat.removeDynamicShortcuts(ApplicationLoader.applicationContext, arrayList24);
                    }
                    i7++;
                    arrayList = arrayList25;
                    longSparseArray17 = longSparseArray3;
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
        longSparseArray = new LongSparseArray();
        size = arrayList14.size();
        i5 = 0;
        while (i5 < size) {
            dialogKey = (DialogKey) arrayList14.get(i5);
            int i252 = size;
            if (!dialogKey.story) {
            }
            Integer num52 = (Integer) longSparseArray14.get(j4);
            z6 = z4;
            int i262 = id;
            if (dialogKey.story) {
            }
            int i272 = 0;
            while (i9 < arrayList3.size()) {
            }
            if (!dialogKey.story) {
            }
            boolean z142 = z7;
            if (messageObject == null) {
            }
            str10 = string;
            str11 = str10;
            if (z5) {
            }
            if (tLRPC$FileLocation4 != null) {
            }
            if (tLRPC$Chat != null) {
            }
            File file722 = file2;
            if (z9) {
            }
            tLRPC$Chat2 = tLRPC$Chat;
            str15 = "NotificationHiddenChatName";
            Intent intent62 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
            intent62.putExtra("dialog_id", j4);
            intent62.putExtra("max_id", i262);
            intent62.putExtra("topic_id", j3);
            intent62.putExtra("currentAccount", notificationsController3.currentAccount);
            str16 = "max_id";
            PendingIntent broadcast22 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent62, 167772160);
            RemoteInput build422 = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
            if (DialogObject.isChatDialog(j4)) {
            }
            build = new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon, formatString, broadcast22).setAllowGeneratedReplies(true).setSemanticAction(1).addRemoteInput(build422).setShowsUserInterface(false).build();
            num3 = notificationsController3.pushDialogs.get(j4);
            if (num3 == null) {
            }
            if (dialogKey.story) {
            }
            if (max > 1) {
            }
            j7 = j5;
            Person person422 = (Person) longSparseArray9.get(j7);
            action = build;
            if (Build.VERSION.SDK_INT >= 28) {
            }
            j8 = j3;
            person = person422;
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
            messagingStyle2.setGroupConversation(i13 >= 28 || (!z9 && DialogObject.isChatDialog(j4)) || UserObject.isReplyUser(j4));
            StringBuilder sb422222 = new StringBuilder();
            String[] strArr222222 = new String[1];
            messageObject3 = messageObject2;
            boolean[] zArr22222 = new boolean[1];
            if (!dialogKey.story) {
            }
            Intent intent222222 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
            intent222222.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
            intent222222.setFlags(ConnectionsManager.FileTypeFile);
            intent222222.addCategory("android.intent.category.LAUNCHER");
            if (messageObject3 == null) {
            }
            dialogKey3 = dialogKey2;
            if (!dialogKey3.story) {
            }
            j12 = j11;
            StringBuilder sb522222 = new StringBuilder();
            sb522222.append("show extra notifications chatId ");
            sb522222.append(j12);
            sb522222.append(" topicId ");
            j13 = j8;
            sb522222.append(j13);
            FileLog.d(sb522222.toString());
            if (j13 != 0) {
            }
            intent222222.putExtra("currentAccount", notificationsController3.currentAccount);
            String str3622222 = str19;
            PendingIntent activity22222 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent222222, 1140850688);
            NotificationCompat.WearableExtender wearableExtender22222 = new NotificationCompat.WearableExtender();
            if (action != null) {
            }
            int i2822222 = i14;
            Intent intent322222 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
            intent322222.addFlags(32);
            intent322222.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
            intent322222.putExtra(str17, j12);
            intent322222.putExtra(str16, i262);
            intent322222.putExtra("currentAccount", notificationsController3.currentAccount);
            arrayList11 = arrayList8;
            bitmap4 = bitmap3;
            build2 = new NotificationCompat.Action.Builder(R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, num2.intValue(), intent322222, 167772160)).setSemanticAction(2).setShowsUserInterface(false).build();
            if (DialogObject.isEncryptedDialog(j12)) {
            }
            if (str33 == null) {
            }
            StringBuilder sb622222 = new StringBuilder();
            sb622222.append("tgaccount");
            long j1922222 = j9;
            sb622222.append(j1922222);
            wearableExtender22222.setBridgeTag(sb622222.toString());
            if (!dialogKey3.story) {
            }
            String str3722222 = str34;
            NotificationCompat.Builder autoCancel22222 = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(str3722222).setSmallIcon(R.drawable.notification).setContentText(sb.toString()).setAutoCancel(true);
            if (dialogKey3.story) {
            }
            category = autoCancel22222.setNumber(arrayList12.size()).setColor(-15618822).setGroupSummary(false).setWhen(j15).setShowWhen(true).setStyle(messagingStyle3).setContentIntent(activity22222).extend(wearableExtender22222).setSortKey(String.valueOf(Long.MAX_VALUE - j15)).setCategory("msg");
            Intent intent422222 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
            intent422222.putExtra("messageDate", i10);
            intent422222.putExtra("dialogId", j12);
            intent422222.putExtra("currentAccount", notificationsController3.currentAccount);
            if (dialogKey3.story) {
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
            if (DialogObject.isEncryptedDialog(j12)) {
            }
            if (bitmap4 != null) {
            }
            if (!AndroidUtilities.needShowPasscode(false)) {
            }
            if (tLRPC$Chat2 == null) {
            }
            tLRPC$User6 = tLRPC$User5;
            Notification notification322222 = notification;
            if (Build.VERSION.SDK_INT >= 26) {
            }
            i11 = i252;
            j6 = j14;
            z8 = z6;
            str9 = str3;
            longSparseArray7 = longSparseArray10;
            str8 = str3622222;
            i12 = i8;
            longSparseArray8 = longSparseArray6;
            str7 = str18;
            arrayList7 = arrayList4;
            sharedPreferences = sharedPreferences2;
            notification2 = notification322222;
            arrayList6 = arrayList2;
            arrayList6.add(new 1NotificationHolder(num2.intValue(), j12, dialogKey3.story, j13, str3722222, tLRPC$User6, tLRPC$Chat2, category, j2, str2, jArr, i, uri, i2, z, z2, z3, i3));
            notificationsController2 = this;
            notificationsController2.wearNotificationsIds.put(j12, num2);
            i5 = i12 + 1;
            arrayList16 = arrayList6;
            size = i11;
            arrayList14 = arrayList7;
            z4 = z8;
            notificationsSettings = sharedPreferences;
            longSparseArray13 = longSparseArray4;
            clientUserId = j6;
            str3 = str9;
            longSparseArray14 = longSparseArray8;
            str4 = str7;
            str5 = str8;
            longSparseArray = longSparseArray7;
            build3 = notification2;
            i4 = -1;
            notificationsController3 = notificationsController2;
        }
        LongSparseArray longSparseArray172 = longSparseArray;
        longSparseArray2 = longSparseArray14;
        Notification notification42 = build3;
        NotificationsController notificationsController42 = notificationsController3;
        ArrayList arrayList232 = arrayList16;
        if (!z4) {
        }
        i6 = 0;
        while (i6 < longSparseArray2.size()) {
        }
        ArrayList arrayList242 = new ArrayList(arrayList.size());
        size2 = arrayList.size();
        i7 = 0;
        while (i7 < size2) {
        }
    }

    /* loaded from: classes3.dex */
    class 1NotificationHolder {
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
        ImageDecoder.Source createSource;
        Bitmap decodeBitmap;
        if (file != null && Build.VERSION.SDK_INT >= 28) {
            try {
                createSource = ImageDecoder.createSource(file);
                decodeBitmap = ImageDecoder.decodeBitmap(createSource, new ImageDecoder.OnHeaderDecodedListener() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda67
                    @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
                    public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
                        NotificationsController.lambda$loadRoundAvatar$44(imageDecoder, imageInfo, source);
                    }
                });
                builder.setIcon(IconCompat.createWithBitmap(decodeBitmap));
            } catch (Throwable unused) {
            }
        }
        return builder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadRoundAvatar$44(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
        imageDecoder.setPostProcessor(new PostProcessor() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda34
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
        Rect rect;
        float f;
        int i2;
        TextPaint textPaint;
        float size;
        float size2;
        float f2;
        float f3;
        float f4;
        float f5;
        Object obj;
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
        Rect rect2 = new Rect();
        paint3.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        float f6 = 1.0f;
        float f7 = arrayList.size() == 1 ? 1.0f : arrayList.size() == 2 ? 0.65f : 0.5f;
        int i3 = 0;
        TextPaint textPaint2 = null;
        while (i3 < arrayList.size()) {
            float f8 = dp;
            float f9 = (f6 - f7) * f8;
            try {
                size = ((arrayList.size() - 1) - i3) * (f9 / arrayList.size());
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
                } catch (Throwable unused) {
                    paint = paint3;
                }
            } catch (Throwable unused2) {
                i = dp;
                bitmap = createBitmap;
                paint = paint3;
                rect = rect2;
                f = f7;
            }
            try {
            } catch (Throwable unused3) {
                rect = rect2;
                i2 = i3;
                textPaint = textPaint2;
                textPaint2 = textPaint;
                i3 = i2 + 1;
                arrayList2 = arrayList;
                rect2 = rect;
                dp = i;
                f7 = f;
                createBitmap = bitmap;
                paint3 = paint;
                f6 = 1.0f;
            }
            if (obj instanceof File) {
                String absolutePath = ((File) arrayList2.get(i3)).getAbsolutePath();
                BitmapFactory.Options options = new BitmapFactory.Options();
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
            } else if (obj instanceof TLRPC$User) {
                TLRPC$User tLRPC$User = (TLRPC$User) obj;
                Rect rect3 = rect2;
                try {
                    i2 = i3;
                    textPaint = textPaint2;
                    try {
                        try {
                            paint2.setShader(new LinearGradient(size, size2, size, size2 + f2, new int[]{Theme.getColor(Theme.keys_avatar_background[AvatarDrawable.getColorIndex(tLRPC$User.id)]), Theme.getColor(Theme.keys_avatar_background2[AvatarDrawable.getColorIndex(tLRPC$User.id)])}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
                            canvas.drawCircle(f4, f5, f3, paint2);
                            if (textPaint == null) {
                                TextPaint textPaint3 = new TextPaint(1);
                                try {
                                    textPaint3.setTypeface(AndroidUtilities.bold());
                                    textPaint3.setTextSize(f8 * 0.25f);
                                    textPaint3.setColor(-1);
                                    textPaint2 = textPaint3;
                                } catch (Throwable unused4) {
                                    textPaint2 = textPaint3;
                                    rect = rect3;
                                }
                            } else {
                                textPaint2 = textPaint;
                            }
                            try {
                                StringBuilder sb = new StringBuilder();
                                AvatarDrawable.getAvatarSymbols(tLRPC$User.first_name, tLRPC$User.last_name, null, sb);
                                String sb2 = sb.toString();
                                rect = rect3;
                                try {
                                    textPaint2.getTextBounds(sb2, 0, sb2.length(), rect);
                                    canvas.drawText(sb2, (f4 - (rect.width() / 2.0f)) - rect.left, (f5 - (rect.height() / 2.0f)) - rect.top, textPaint2);
                                } catch (Throwable unused5) {
                                }
                            } catch (Throwable unused6) {
                                rect = rect3;
                            }
                        } catch (Throwable unused7) {
                            rect = rect3;
                        }
                    } catch (Throwable unused8) {
                        rect = rect3;
                        textPaint2 = textPaint;
                        i3 = i2 + 1;
                        arrayList2 = arrayList;
                        rect2 = rect;
                        dp = i;
                        f7 = f;
                        createBitmap = bitmap;
                        paint3 = paint;
                        f6 = 1.0f;
                    }
                } catch (Throwable unused9) {
                    i2 = i3;
                    textPaint = textPaint2;
                }
                i3 = i2 + 1;
                arrayList2 = arrayList;
                rect2 = rect;
                dp = i;
                f7 = f;
                createBitmap = bitmap;
                paint3 = paint;
                f6 = 1.0f;
            }
            rect = rect2;
            i2 = i3;
            textPaint = textPaint2;
            textPaint2 = textPaint;
            i3 = i2 + 1;
            arrayList2 = arrayList;
            rect2 = rect;
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
        notificationsQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda62
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
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda23
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
        getConnectionsManager().sendRequest(tLRPC$TL_account_updateNotifySettings, new RequestDelegate() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda72
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
            getConnectionsManager().sendRequest(tLRPC$TL_account_setReactionsNotifySettings, new RequestDelegate() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda53
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
            tLRPC$TL_inputPeerNotifySettings6.flags |= 256;
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

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0014, code lost:
        if (r3.booleanValue() != false) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x002c, code lost:
        if (r1.megagroup == false) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x002e, code lost:
        r1 = 2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isGlobalNotificationsEnabled(long j, Boolean bool, boolean z, boolean z2) {
        int i;
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

    public static String getGlobalNotificationsKey(int i) {
        if (i == 0) {
            return "EnableGroup2";
        }
        if (i == 1) {
            return "EnableAll2";
        }
        return "EnableChannel2";
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
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda71
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
                Integer parseInt = Utilities.parseInt((CharSequence) key.replace(NotificationsSettingsFacade.PROPERTY_NOTIFY + j, ""));
                int intValue = parseInt.intValue();
                if (intValue != 0 && getMessagesController().isDialogMuted(j, intValue) != getMessagesController().isDialogMuted(j, 0L)) {
                    hashSet.add(parseInt);
                }
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.NotificationsController$$ExternalSyntheticLambda27
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

    /* loaded from: classes3.dex */
    private static class DialogKey {
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
